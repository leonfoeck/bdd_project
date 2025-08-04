package de.uni_passau.fim.se2.st.mensawebapp.business.service;

import jakarta.enterprise.context.Dependent;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

/**
 * A service for calendar computations.
 */
@Dependent
public class CalendarService {

    /**
     * Provides the week number for a given date.
     *
     * @param pDate The date
     * @return The week number for that date
     */
    public int getWeekNumber(final LocalDate pDate) {
        return pDate.get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    /**
     * Provides the year for a given date.
     *
     * @param pDate The date
     * @return The year of the date
     */
    public int getYear(final LocalDate pDate) {
        return pDate.getYear();
    }
}
