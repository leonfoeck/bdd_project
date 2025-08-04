package de.uni_passau.fim.se2.st.mensawebapp.view.converter;

import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Allergen;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

/**
 * A converter for single {@link Allergen}s for the select menus of the UI.
 */
@FacesConverter(forClass = Allergen.class, value = "allergenMenuConverter")
public class AllergenMenuConverter implements Converter<Allergen> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Allergen getAsObject(FacesContext context, UIComponent component, String value) {
        return Allergen.getAllergenForName(value).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Allergen value) {
        return value.getAllergen();
    }
}
