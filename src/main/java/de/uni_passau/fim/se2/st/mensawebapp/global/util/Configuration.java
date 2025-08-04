package de.uni_passau.fim.se2.st.mensawebapp.global.util;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Reads the configuration file and provides a typed API for internal usage.
 */
@ApplicationScoped
public class Configuration {

    private static final String configurationFileName = "mensawebapp.properties";

    private static final String nameErrorVerbosity = "error.errorpageverbosity";
    private static final String nameLogo = "template.logo";
    private static final String nameApplicationName = "application.name";
    private static final String dateTimePatternName = "application.dateTimePattern";
    private static final String storagePathName = "application.storagePath";
    private static final String baseURIName = "application.baseURI";
    private static final String maxAgeName = "application.maxAge";
    private static final String[] keysRequired = {
            nameErrorVerbosity, nameLogo, nameApplicationName, maxAgeName
    };

    // error messages
    private static final String configFileNotFoundMessage =
            "The configuration file " + configurationFileName + " could not be found in the classpath";
    private static final String errorWhileReading =
            "Something went wrong while reading the configuration file.";
    private static final String noNumber = "Verbosity level needs to be a number";
    private static final String invalidVerbosity = "The Verbosity needs to be either 0, 1, or 2";
    private static final String keyRequireMessage = "Key missing in configuration file: ";
    private static final String errorWhileParsing =
            "Something went wrong while parsing the configuration file.";

    /**
     * Verbosity of error messages on the error page (0, 1, or 2):
     *
     * <ul>
     *   <li>0 to only report that an error occurred
     *   <li>1 to report which process did not succeed
     *   <li>2 to show the stack trace
     * </ul>
     */
    private int errorVerbosity = 1;

    private String logo;
    private String applicationName;
    private String dateTimePattern;
    private String storagePath;
    private Path path;
    private String baseURI;
    private long maxAge;

    /**
     * Sets up the values in the bean.
     */
    @PostConstruct
    public void init() {
        try (final InputStream input =
                     Configuration.class.getClassLoader().getResourceAsStream(configurationFileName)) {
            final Properties properties = new Properties();
            if (input == null) {
                throw new StartupFailureException(configFileNotFoundMessage);
            }

            properties.load(input);

            checkKeysPresent(properties);

            // Load values
            applicationName = properties.getProperty(nameApplicationName);
            logo = properties.getProperty(nameLogo);

            // check whether error verbosity is valid
            errorVerbosity = Integer.parseInt(properties.getProperty(nameErrorVerbosity));
            if (!(errorVerbosity == 0 || errorVerbosity == 1 || errorVerbosity == 2)) {
                throw new StartupFailureException(invalidVerbosity);
            }

            dateTimePattern = properties.getProperty(dateTimePatternName);
            storagePath = properties.getProperty(storagePathName);
            baseURI = properties.getProperty(baseURIName);
            maxAge = Long.parseLong(properties.getProperty(maxAgeName));
        } catch (IOException e) {
            throw new StartupFailureException(errorWhileReading, e);
        } catch (NumberFormatException e) {
            throw new StartupFailureException(noNumber, e);
        }
    }

    private void checkKeysPresent(final Properties pProperties) {
        for (final String key : keysRequired) {
            if (!pProperties.containsKey(key)) {
                throw new StartupFailureException(keyRequireMessage + key);
            }
        }
    }

    /**
     * Provides the error-verbosity level.
     *
     * @return The error-verbosity level
     */
    public int getErrorVerbosity() {
        return errorVerbosity;
    }

    /**
     * Provides the application's name.
     *
     * @return The application's name
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Provides the path to the logo.
     *
     * @return The path to the logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Provides the pattern for date and time parsing.
     *
     * @return The pattern for date and time parsing
     */
    public String getDateTimePattern() {
        return dateTimePattern == null ? "dd.MM.yyyy" : dateTimePattern;
    }

    /**
     * Provides the path to store all downloaded data files.
     *
     * @return The path to store all downloaded data files
     */
    public Path getStoragePath() {
        if (path != null && path.toFile().exists()) {
            return path;
        }
        try {
            if (storagePath != null) {
                Paths.get(storagePath);
                Files.createDirectories(path);
            } else {
                path = Files.createTempDirectory("");
            }
            return path;
        } catch (final IOException pE) {
            throw new StartupFailureException(pE);
        }
    }

    /**
     * Provides the base URL for downloading new data files.
     *
     * @return The base URL for downloading new data files
     */
    public URI getBaseURI() {
        try {
            if (baseURI != null) {
                return new URI(baseURI);
            }
            return new URI("http://mensadata.lukasczyk.name");
        } catch (final URISyntaxException pE) {
            throw new StartupFailureException(pE);
        }
    }

    /**
     * Provides the maximum age after which a data file shall be deleted.
     *
     * @return The maximum age for a data file
     */
    public long getMaxAge() {
        return maxAge;
    }
}
