package de.uni_passau.fim.se2.st.mensawebapp.view.util;

import com.google.errorprone.annotations.Var;
import jakarta.faces.FacesException;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerFactory;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.ExceptionQueuedEventContext;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A custom exception handler for JSF.
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final Logger logger;

    public CustomExceptionHandler(final ExceptionHandler pWrapped) {
        super(pWrapped);
        logger = Logger.getLogger(CustomExceptionHandler.class.getCanonicalName());
    }

    @Override
    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> iterator = getHandledExceptionQueuedEvents().iterator();

        while (iterator.hasNext()) {
            final ExceptionQueuedEvent event = iterator.next();
            final ExceptionQueuedEventContext context = event.getContext();

            final Throwable throwable = context.getException();
            logger.log(Level.SEVERE, throwable.getMessage());

            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final ExternalContext externalContext = facesContext.getExternalContext();
            final Map<String, Object> requestMap = externalContext.getRequestMap();
            final NavigationHandler navigationHandler =
                    facesContext.getApplication().getNavigationHandler();

            requestMap.put("exceptionMessage", throwable.getMessage());
            final List<Throwable> throwables = new LinkedList<>();
            @Var Throwable t = throwable.getCause();
            while (t != null) {
                throwables.add(t);
                t = t.getCause();
            }
            requestMap.put("exceptionStackTraces", throwables);

            try {
                navigationHandler.handleNavigation(facesContext, null, "/error.xhtml");
                facesContext.renderResponse();
            } finally {
                iterator.remove();
            }
        }
        getWrapped().handle();
    }

    public static class Factory extends ExceptionHandlerFactory {

        public Factory(final ExceptionHandlerFactory pWrapped) {
            super(pWrapped);
        }

        @Override
        public ExceptionHandler getExceptionHandler() {
            return new CustomExceptionHandler(getWrapped().getExceptionHandler());
        }
    }
}
