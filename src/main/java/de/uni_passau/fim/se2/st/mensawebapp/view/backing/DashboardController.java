package de.uni_passau.fim.se2.st.mensawebapp.view.backing;

import de.uni_passau.fim.se2.st.mensawebapp.business.service.CSVLoadException;
import de.uni_passau.fim.se2.st.mensawebapp.business.service.DishService;
import de.uni_passau.fim.se2.st.mensawebapp.business.service.IngredientService;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Additive;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Allergen;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Dish;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Named
@RequestScoped
public class DashboardController {

    private final Logger logger;
    private final IngredientService ingredientService;
    private final DishService dishService;
    private LocalDate selectedDate;
    private List<Dish> dishes;

    private List<Additive> additives;

    private List<Additive> selectedAdditives;

    private List<Allergen> allergens;

    private List<Allergen> selectedAllergens;

    private List<Tag> tags;

    private List<Tag> selectedTags;

    @Inject
    public DashboardController(
            final Logger pLogger,
            final IngredientService pIngredientService,
            final DishService pDishService) {
        logger = pLogger;
        ingredientService = pIngredientService;
        dishService = pDishService;
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing DashboardController");
        selectedDate = LocalDate.now();
        selectedAdditives = new ArrayList<>();
        selectedAllergens = new ArrayList<>();
        selectedTags = new ArrayList<>();
        setDishList();
        logger.info("DashboardController initialized");
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(final LocalDate pSelectedDate) {
        selectedDate = pSelectedDate;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public int getTotalDishes() {
        return dishes.size();
    }

    public List<Additive> getAdditives() {
        return ingredientService.provideAdditives();
    }

    public void setAdditives(final List<Additive> pAdditives) {
        additives = pAdditives;
    }

    public List<Additive> getSelectedAdditives() {
        return selectedAdditives;
    }

    public void setSelectedAdditives(final List<Additive> pSelectedAdditives) {
        selectedAdditives = pSelectedAdditives;
    }

    public List<Allergen> getAllergens() {
        return ingredientService.provideAllergens();
    }

    public void setAllergens(final List<Allergen> pAllergens) {
        allergens = pAllergens;
    }

    public List<Allergen> getSelectedAllergens() {
        return selectedAllergens;
    }

    public void setSelectedAllergens(final List<Allergen> pSelectedAllergens) {
        selectedAllergens = pSelectedAllergens;
    }

    public List<Tag> getTags() {
        return ingredientService.provideTags();
    }

    public void setTags(final List<Tag> pTags) {
        tags = pTags;
    }

    public List<Tag> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(final List<Tag> pSelectedTags) {
        selectedTags = pSelectedTags;
    }

    public void submit() {
        logger.info("Submitting form data with selecte date " + selectedDate + " and selected additives, allergens and tags");
        setDishList();
    }

    private void setDishList() {
        try {
            dishes = dishService.filterDishes(selectedDate, selectedAdditives, selectedAllergens, selectedTags);
            logger.info("executed setDishList");
        } catch (CSVLoadException e) {
            dishes = Collections.emptyList();
            FacesMessage message = new FacesMessage(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}
