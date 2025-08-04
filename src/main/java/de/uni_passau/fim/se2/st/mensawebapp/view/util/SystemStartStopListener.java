package de.uni_passau.fim.se2.st.mensawebapp.view.util;

import de.uni_passau.fim.se2.st.mensawebapp.business.service.DishService;
import de.uni_passau.fim.se2.st.mensawebapp.global.util.Configuration;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A listener for the system's start and stop events.
 */
@WebListener
public class SystemStartStopListener implements ServletContextListener {

    @Inject
    private Configuration configuration;
    @Inject
    private DishService dishService;

    private ScheduledExecutorService scheduler;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("ReturnValueIgnored")
    @Override
    public void contextInitialized(final ServletContextEvent pServletContextEvent) {
        configuration.toString(); // Force CDI to create configuration object.
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> dishService.deleteOldData(), 0, 1, TimeUnit.HOURS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextDestroyed(final ServletContextEvent pServletContextEvent) {
        scheduler.shutdownNow();
        dishService.deleteAllData();
        deleteFile(configuration.getStoragePath().toFile());
    }

    @SuppressWarnings("ReturnValueIgnored")
    private void deleteFile(final File pFile) {
        final File[] contents = pFile.listFiles();
        if (contents != null) {
            for (final File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteFile(f);
                }
            }
        }
        pFile.delete();
    }
}
