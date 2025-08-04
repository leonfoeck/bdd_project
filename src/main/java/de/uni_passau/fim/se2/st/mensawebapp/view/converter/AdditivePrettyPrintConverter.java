package de.uni_passau.fim.se2.st.mensawebapp.view.converter;

import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Additive;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@FacesConverter(value = "additivePrettyPrintConverter")
public class AdditivePrettyPrintConverter implements Converter<LinkedHashSet<Additive>> {
    @Override
    public LinkedHashSet<Additive> getAsObject(FacesContext context, UIComponent component, String value) {
        List<String> list = List.of(value.split(", "));
        LinkedHashSet<Additive> additiveLinkedHashSet = new LinkedHashSet<>();
        for (String s : list) {
            additiveLinkedHashSet.add(Additive.getAdditiveForName(s.trim()).orElse(null));
        }
        return additiveLinkedHashSet;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LinkedHashSet<Additive> value) {
        return value.stream()
                    .sorted(Comparator.comparing(Additive::getAdditive))
                    .map(additive -> String.format("<abbr title=\"%s\">%s</abbr>", additive.getAdditive(), additive.name().charAt(0) - 64))
                    .collect(Collectors.joining(", "));
    }
}
