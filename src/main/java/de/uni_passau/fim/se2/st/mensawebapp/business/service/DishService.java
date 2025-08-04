package de.uni_passau.fim.se2.st.mensawebapp.business.service;

import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Additive;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Allergen;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Dish;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Tag;
import de.uni_passau.fim.se2.st.mensawebapp.global.util.Configuration;
import de.uni_passau.fim.se2.st.mensawebapp.persistence.csv.CSVFile;
import de.uni_passau.fim.se2.st.mensawebapp.persistence.csv.CSVParser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DishService {

    private final CalendarService calendarService;
    private final Logger logger;
    private final DateTimeFormatter dateTimeFormatter;
    private final Path storagePath;
    private final URI baseURI;
    private final long maxAge;

    private final Map<Integer, List<Dish>> dishCache;
    private final Map<Integer, CSVFile> dataFileCache;

    @Inject
    public DishService(
            final Configuration pConfiguration,
            final CalendarService pCalendarService,
            final Logger pLogger) {
        calendarService = pCalendarService;
        logger = pLogger;

        dateTimeFormatter = DateTimeFormatter.ofPattern(pConfiguration.getDateTimePattern());
        storagePath = pConfiguration.getStoragePath();
        baseURI = pConfiguration.getBaseURI();
        maxAge = pConfiguration.getMaxAge();

        dishCache = new ConcurrentHashMap<>();
        dataFileCache = new ConcurrentHashMap<>();
    }

    /**
     * Provides the list of {@link Dish}es for a given date.
     *
     * @param pSelectedDate The selected date
     * @return A list of {@link Dish}es for that day
     * @throws CSVLoadException In case the CSV file could not be loaded
     */
    public synchronized List<Dish> getDishes(final LocalDate pSelectedDate) throws CSVLoadException {
        final int weekNumber = calendarService.getWeekNumber(pSelectedDate);
        final int year = calendarService.getYear(pSelectedDate);

        final List<Dish> dishes = getDishesForWeek(weekNumber, year);

        return dishes.stream().filter(d -> d.date().equals(pSelectedDate)).toList();
    }

    private synchronized List<Dish> getDishesForWeek(final int pWeekNumber, final int pYear)
            throws CSVLoadException {
        final int cacheKey = pWeekNumber * 10000 + pYear;
        final String weekOfYear = "for week " + pWeekNumber + " of year " + pYear;
        if (dishCache.containsKey(cacheKey)) {
            logger.log(Level.FINE, () -> "Use cached dishes " + weekOfYear);
            return dishCache.get(cacheKey);
        }

        final CSVFile csvFile;
        final List<Dish> dishes;
        try {
            csvFile = new CSVFile(storagePath, baseURI, pYear, pWeekNumber);
            if (!csvFile.download()) {
                logger.log(Level.SEVERE, () -> "Could not load data " + weekOfYear);
                throw new CSVLoadException(
                        String.format("Konnte Daten für KW %d des Jahres %d nicht laden.", pWeekNumber, pYear));
            }
            dishes = getDishesFromDataFile(csvFile);
        } catch (final IOException pException) {
            logger.severe(pException.getMessage());
            throw new CSVLoadException(
                    String.format("Konnte Daten für KW %d des Jahres %d nicht laden.", pWeekNumber, pYear));
        }

        dishCache.put(cacheKey, dishes);
        dataFileCache.put(cacheKey, csvFile);
        logger.log(Level.FINE, () -> "Cached " + dishes.size() + " dishes " + weekOfYear);
        return dishes;
    }

    /**
     * Filter the {@link Dish}es for a given day.
     *
     * <p>Filtering criteria are lists of {@link Additive}s, {@link Allergen}s, and {@link Tag}s.
     *
     * @param pSelectedDate      The selected date
     * @param pSelectedAdditives A list of selected {@link Additive}s
     * @param pSelectedAllergens A list of selected {@link Allergen}s
     * @param pSelectedTags      A list of selected {@link Tag}s
     * @return A list of {@link Dish}es that are suitable for the selection criteria
     * @throws CSVLoadException In case the CSV file could not be loaded
     */
    public synchronized List<Dish> filterDishes(
            final LocalDate pSelectedDate,
            final Collection<Additive> pSelectedAdditives,
            final Collection<Allergen> pSelectedAllergens,
            final Collection<Tag> pSelectedTags)
            throws CSVLoadException {
        final List<Dish> dishes = getDishes(pSelectedDate);
        return dishes.stream()
                     .filter(d -> filterAdditives(d, pSelectedAdditives))
                     .filter(d -> filterAllergens(d, pSelectedAllergens))
                     .filter(d -> filterTags(d, pSelectedTags))
                     .toList();
    }

    private boolean filterAdditives(final Dish pDish, final Collection<Additive> pExcludedAdditives) {
        for (final Additive additive : pExcludedAdditives) {
            if (pDish.additives().contains(additive)) {
                return false;
            }
        }
        return true;
    }

    private boolean filterAllergens(final Dish pDish, final Collection<Allergen> pExcludedAllergens) {
        for (final Allergen allergen : pExcludedAllergens) {
            if (pDish.allergens().contains(allergen)) {
                return false;
            }
        }
        return true;
    }

    private boolean filterTags(final Dish pDish, final Collection<Tag> pExcludedTags) {
        for (final Tag tag : pExcludedTags) {
            if (pDish.tags().contains(tag)) {
                return false;
            }
        }
        return true;
    }

    private List<Dish> getDishesFromDataFile(final CSVFile pDataFile) throws IOException {
        final CSVParser parser = new CSVParser(pDataFile, dateTimeFormatter);
        return parser.parseDishes();
    }

    /**
     * Delete old data.
     *
     * <p>Old data is data that was downloaded longer than {@link Configuration#getMaxAge()} seconds
     * ago.
     *
     * @see Configuration#getMaxAge()
     */
    public synchronized void deleteOldData() {
        logger.info("Deleting old data...");
        for (final Entry<Integer, CSVFile> dataFileEntry : dataFileCache.entrySet()) {
            final Integer week = dataFileEntry.getKey();
            final CSVFile dataFile = dataFileEntry.getValue();
            if (dataFile.hasAge(maxAge)) {
                logger.log(Level.INFO, () -> "Delete data for week " + week);
                dataFile.delete();
                dataFileCache.remove(week);
                dishCache.remove(week);
            }
        }
        logger.info("Deletion of old files done.");
    }

    /**
     * Delete all data.
     */
    public synchronized void deleteAllData() {
        dataFileCache.forEach((key, value) -> value.delete());
        dataFileCache.clear();
        dishCache.clear();
    }
}
