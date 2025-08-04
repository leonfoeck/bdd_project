package de.uni_passau.fim.se2.st.mensawebapp.persistence.csv;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Additive;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Allergen;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Dish;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.DishType;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Tag;
import de.uni_passau.fim.se2.st.mensawebapp.persistence.csv.CSVReader.CSVRow;
import de.uni_passau.fim.se2.st.mensawebapp.persistence.exception.CSVParserException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A parser for the CSV files of STWNO.
 */
public class CSVParser {

    private final CSVFile csvFile;
    private final DateTimeFormatter dateTimeFormatter;

    /**
     * Instantiates a new {@code CSVParser} object.
     *
     * @param pCSVFile           The CSV file object
     * @param pDateTimeFormatter A formatter for dates
     */
    public CSVParser(final CSVFile pCSVFile, final DateTimeFormatter pDateTimeFormatter) {
        csvFile = pCSVFile;
        dateTimeFormatter = pDateTimeFormatter;
    }

    /**
     * Parses the content of the CSV file to a list of {@link Dish}es.
     *
     * @return A list of {@link Dish}es
     * @throws IOException In case of I/O errors
     */
    public List<Dish> parseDishes() throws IOException {
        if (!csvFile.exists() && !csvFile.download()) {
            throw new CSVParserException("Cannot find CSV file!");
        }

        final ImmutableList.Builder<Dish> dishBuilder = new Builder<>();

        try (final BufferedReader reader =
                     new BufferedReader(new FileReader(csvFile.provideFile(), Charset.defaultCharset()))) {
            final CSVReader csvReader = new CSVReader(reader);
            for (final CSVRow row : csvReader) {
                dishBuilder.add(buildDish(row));
            }
            return dishBuilder.build();
        }
    }

    private Dish buildDish(final CSVRow pRow) {
        final LocalDate date = extractDate(pRow.get("datum"));
        final DishType type = extractDishType(pRow.get("warengruppe"));
        final BigDecimal studentPrice = new BigDecimal(replaceDecimalSign(pRow.get("stud")));
        final String name = extractDishName(pRow.get("name"));

        final int bracketIndex = pRow.get("name").indexOf("(");
        final String additivesAllergens =
                pRow.get("name").substring(bracketIndex + 1, pRow.get("name").length() - 1);

        final Dish.Builder builder = new Dish.Builder();
        return builder
                .setDate(date)
                .setType(type)
                .setName(name)
                .setAdditives(extractAdditives(additivesAllergens))
                .setAllergens(extractAllergens(additivesAllergens))
                .setTags(extractTags(pRow.get("kennz")))
                .setPrice(studentPrice)
                .build();
    }

    private String replaceDecimalSign(final String pString) {
        return pString.replace(',', '.');
    }

    private DishType extractDishType(final String pElement) {
        final DishType type;
        if (pElement.equals("Suppe")) {
            type = DishType.APPETISER;
        } else if (pElement.startsWith("HG")) {
            type = DishType.MAIN;
        } else if (pElement.startsWith("B")) {
            type = DishType.SIDE;
        } else if (pElement.startsWith("N")) {
            type = DishType.DESSERT;
        } else {
            throw new CSVParserException("Cannot parse dish type " + pElement);
        }
        return type;
    }

    private LocalDate extractDate(final String pString) {
        return LocalDate.parse(pString, dateTimeFormatter);
    }

    private String extractDishName(final String pString) {
        final String[] names = pString.split("[()]");
        Preconditions.checkState(names.length > 0);
        return names[0].strip();
    }

    private Collection<Additive> extractAdditives(final String pString) {
        return Arrays.stream(pString.split(","))
                     .filter(c -> c.matches("\\d+"))
                     .map(Integer::parseInt)
                     .map(Additive::getAdditiveForIndex)
                     .flatMap(Optional::stream)
                     .toList();
    }

    private Collection<Allergen> extractAllergens(final String pString) {
        return Arrays.stream(pString.split(","))
                     .filter(c -> c.matches("[A-Z]+"))
                     .map(Allergen::getAllergenForToken)
                     .flatMap(Optional::stream)
                     .toList();
    }

    private Collection<Tag> extractTags(final String pString) {
        return Arrays.stream(pString.split(","))
                     .map(Tag::getTagForToken)
                     .flatMap(Optional::stream)
                     .toList();
    }
}
