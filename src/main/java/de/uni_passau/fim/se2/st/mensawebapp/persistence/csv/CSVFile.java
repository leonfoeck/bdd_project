package de.uni_passau.fim.se2.st.mensawebapp.persistence.csv;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.Date;

/**
 * A utility class to deal with a CSV file.
 */
public class CSVFile {

    private final Path filePath;
    private final URL fileURL;

    /**
     * Initialises the {@code CSVFile} class.
     *
     * @param pStoragePath The path to store the CSV file after downloading it
     * @param pBaseURL     The base URL for the CSV files to get downloaded
     * @param pYear        The year for the menu
     * @param pWeek        The week for the menu
     * @throws MalformedURLException In case the resulting URL is invalid
     * @throws IllegalStateException In case the week number is illegal
     */
    public CSVFile(final Path pStoragePath, final URI pBaseURL, final int pYear, final int pWeek)
            throws MalformedURLException {
        Preconditions.checkState(pWeek > 0 && pWeek < 54);
        filePath = pStoragePath.resolve(String.format("%d-%d.csv", pYear, pWeek));
        fileURL = pBaseURL.resolve(String.format("%s/%d-%d.csv", pBaseURL, pYear, pWeek)).toURL();
    }

    /**
     * Delete the file from the storage.
     *
     * @return Whether deletion was successful
     */
    public boolean delete() {
        return filePath.toFile().delete();
    }

    /**
     * Delete the file if its age is more than {@code pAgeSeconds}.
     *
     * @param pAgeSeconds The minimum age of the file in seconds to be deleted
     * @return Whether deletion was successful
     */
    public boolean delete(final long pAgeSeconds) {
        if (hasAge(pAgeSeconds)) {
            return delete();
        }
        return false;
    }

    /**
     * Download the file.
     *
     * @return Whether the download was successful
     * @throws IOException In case of I/O errors
     */
    public boolean download() throws IOException {
        final long numberOfBytes;
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(fileURL.openStream())) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile())) {
                numberOfBytes =
                        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            }
        }
        return numberOfBytes > 0;
    }

    /**
     * Check whether the file exists on the storage.
     *
     * @return Whether the file exists
     */
    public boolean exists() {
        return filePath.toFile().exists();
    }

    /**
     * Checks whether the file already is older than {@code pAgeSeconds} seconds.
     *
     * @param pAgeSeconds The minimum age of the file in seconds
     * @return Whether the file is older than the given number of seconds
     */
    public boolean hasAge(final long pAgeSeconds) {
        // Compute number of milliseconds since last modification, i.e., creation of the file
        final long diff = new Date().getTime() - filePath.toFile().lastModified();
        return diff > pAgeSeconds * 1000;
    }

    /**
     * Provides the file object for the downloaded CSV file.
     *
     * @return The file object for the downloaded CSV file.
     */
    public File provideFile() {
        Preconditions.checkState(exists());
        return filePath.toFile();
    }
}
