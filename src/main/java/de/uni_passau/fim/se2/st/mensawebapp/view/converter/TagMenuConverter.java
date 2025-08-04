package de.uni_passau.fim.se2.st.mensawebapp.view.converter;

import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Tag;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

/**
 * A converter for single {@link Tag}s for the select menus of the UI.
 */
@FacesConverter(forClass = Tag.class)
public class TagMenuConverter implements Converter<Tag> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag getAsObject(FacesContext context, UIComponent component, String value) {
        return Tag.getTagForName(value).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Tag value) {
        return value.getTag();
    }
}
