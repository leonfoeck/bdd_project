package de.uni_passau.fim.se2.st.mensawebapp.global.util;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

import java.util.logging.Logger;

/**
 * Provides a producer that can be used for getting a {@link Logger}.
 *
 * <p>Simply inject the {@link Logger} instance wherever you need it by {@code @Inject private
 * Logger logger;}.
 */
public class LoggerProducer {

    @Produces
    Logger produceLogger(final InjectionPoint pInjectionPoint) {
        return Logger.getLogger(pInjectionPoint.getMember().getDeclaringClass().getCanonicalName());
    }
}
