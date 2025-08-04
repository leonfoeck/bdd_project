package de.uni_passau.fim.se2.st.mensawebapp.view.converter;

import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Tag;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@FacesConverter(value = "tagPrettyPrintConverter")
public class TagPrettyPrintConverter implements Converter<LinkedHashSet<Tag>> {
    @Override
    public LinkedHashSet<Tag> getAsObject(FacesContext context, UIComponent component, String value) {
        List<String> list = List.of(value.split(", "));
        LinkedHashSet<Tag> tagLinkedHashSet = new LinkedHashSet<>();
        for (String s : list) {
            tagLinkedHashSet.add(Tag.getTagForName(s.trim()).orElse(null));
        }
        return tagLinkedHashSet;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LinkedHashSet<Tag> value) {
        return value.stream()
                    .sorted(Comparator.comparing(Tag::getTag))
                    .map(Tag::getTag)
                    .collect(Collectors.joining(", "));
    }
}
