package de.uni_passau.fim.se2.st.mensawebapp.view.backing;

import de.uni_passau.fim.se2.st.mensawebapp.global.util.Configuration;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * A controller for the {@code header.xhtml} facelet.
 */
@Named
@RequestScoped
public class HeaderController {

    private final String applicationName;
    private final String logoPath;

    @Inject
    public HeaderController(final Configuration pConfiguration) {
        applicationName = pConfiguration.getApplicationName();
        logoPath = pConfiguration.getLogo();
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
     * Provides the application's logo path.
     *
     * @return The application's logo path
     */
    public String getLogoPath() {
        return logoPath;
    }
}
