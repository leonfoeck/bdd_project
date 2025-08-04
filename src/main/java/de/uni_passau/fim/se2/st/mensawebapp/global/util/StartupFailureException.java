package de.uni_passau.fim.se2.st.mensawebapp.global.util;

/**
 * Unchecked exception for communicating non-fatal problems on startup.
 */
public class StartupFailureException extends RuntimeException {

    public StartupFailureException(final String pMessage) {
        super(pMessage);
    }

    public StartupFailureException(final String pMessage, final Throwable pCause) {
        super(pMessage, pCause);
    }

    /**
     * Creates an unchecked exception for communicating non-fatal problems on startup, like IO errors.
     * May be used to convert checked exceptions into unchecked exceptions while passing on all
     * necessary information for debugging.
     *
     * @param pCause Throwable that caused the StartupFailureException to be thrown
     */
    public StartupFailureException(final Throwable pCause) {
        super(pCause);
    }
}
