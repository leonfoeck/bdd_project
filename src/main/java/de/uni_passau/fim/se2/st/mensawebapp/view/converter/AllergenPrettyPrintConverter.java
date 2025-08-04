package de.uni_passau.fim.se2.st.mensawebapp.view.converter;

import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Allergen;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@FacesConverter(value = "allergenPrettyPrintConverter")
public class AllergenPrettyPrintConverter implements Converter<LinkedHashSet<Allergen>> {
    @Override
    public LinkedHashSet<Allergen> getAsObject(FacesContext context, UIComponent component, String value) {
        List<String> list = List.of(value.split(", "));
        LinkedHashSet<Allergen> allergenLinkedHashSet = new LinkedHashSet<>();
        for (String s : list) {
            allergenLinkedHashSet.add(Allergen.getAllergenForName(s.trim()).orElse(null));
        }
        return allergenLinkedHashSet;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LinkedHashSet<Allergen> value) {
        return value.stream()
                    .sorted(Comparator.comparing(Allergen::getAllergen))
                    .map(allergen -> String.format("<abbr title=\"%s\">%s</abbr>", allergen.getAllergen(), allergen.name()))
                    .collect(Collectors.joining(", "));
    }
}
