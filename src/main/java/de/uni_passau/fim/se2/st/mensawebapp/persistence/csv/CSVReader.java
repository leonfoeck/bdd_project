package de.uni_passau.fim.se2.st.mensawebapp.persistence.csv;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.uni_passau.fim.se2.st.mensawebapp.persistence.csv.CSVReader.CSVRow;
import de.uni_passau.fim.se2.st.mensawebapp.persistence.exception.CSVParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A reader for CSV files.
 *
 * <p>This reader will work on any kind of input that can be represented by a {@link
 * BufferedReader}. It expects that the first line of the CSV file contains the field names.
 */
public class CSVReader implements Iterable<CSVRow> {

    private final BufferedReader reader;
    private final CharSequence delimiter;
    private final List<String> fieldNames;

    /**
     * Initialises a {@code CSVReader} using a {@link BufferedReader} as its input source.
     *
     * <p>This sets the default delimiter character to {@code ;}.
     *
     * @param pReader The input source
     */
    public CSVReader(final BufferedReader pReader) {
        this(pReader, ";");
    }

    /**
     * Initialises a {@code CSVReader} using a {@link BufferedReader} as its input source.
     *
     * <p>This allows to set the delimiter character to {@code pDelimiter}.
     *
     * @param pReader    The input source
     * @param pDelimiter The delimiter character
     */
    public CSVReader(final BufferedReader pReader, final CharSequence pDelimiter) {
        reader = pReader;
        delimiter = pDelimiter;
        fieldNames = new LinkedList<>();
        retrieveFieldNames();
    }

    private void retrieveFieldNames() {
        fieldNames.clear();
        final String line;
        try {
            line = reader.readLine();
            final String[] fields = line.split(delimiter.toString());
            fieldNames.addAll(Arrays.stream(fields).toList());
        } catch (IOException pException) {
            throw new CSVParserException(
                    String.format("Not able to retrieve field names; %s", pException.getMessage()));
        }
    }

    /**
     * Provides the field names from the CSV file.
     *
     * @return The field names from the CSV file
     */
    public List<String> getFieldNames() {
        return ImmutableList.copyOf(fieldNames);
    }

    @Override
    public Iterator<CSVRow> iterator() {
        return reader.lines().map(this::getRowFromLine).iterator();
    }

    private CSVRow getRowFromLine(final String pLine) {
        final String[] fields = pLine.split(delimiter.toString());
        return new CSVRow(zipToMap(fieldNames, Arrays.stream(fields).toList()));
    }

    private <K, V> Map<K, V> zipToMap(final List<K> pKeys, final List<V> pValues) {
        return IntStream.range(0, pKeys.size())
                        .boxed()
                        .collect(Collectors.toMap(pKeys::get, pValues::get));
    }

    /**
     * Represents one data row of the CSV data.
     */
    public static class CSVRow {

        private final Map<String, String> entryMap;

        private CSVRow(final Map<String, String> pEntryMap) {
            entryMap = ImmutableMap.copyOf(pEntryMap);
        }

        /**
         * Returns the value to which the specified key is mapped, or {@code null} if this map contains
         * no mapping for the key.
         *
         * @param pKey the key whose associated value is to be returned
         * @return the value to which the specified key is mapped, or {@code null} if this map contains
         * no mapping for the key
         */
        public String get(final String pKey) {
            return entryMap.get(pKey);
        }

        /**
         * Provides the entry map of the row.
         *
         * <p>The map is a mapping from field name to value.
         *
         * @return The entry map of the row
         */
        public Map<String, String> getEntryMap() {
            return entryMap;
        }
    }
}
