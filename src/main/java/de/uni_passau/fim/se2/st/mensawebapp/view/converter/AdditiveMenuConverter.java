package de.uni_passau.fim.se2.st.mensawebapp.view.converter;

import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Additive;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

/**
 * A converter for single {@link Additive}s for the select menus of the UI.
 */
@FacesConverter(forClass = Additive.class)
public class AdditiveMenuConverter implements Converter<Additive> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Additive getAsObject(FacesContext context, UIComponent component, String value) {
        return Additive.getAdditiveForName(value).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Additive value) {
        return value.getAdditive();
    }
}
