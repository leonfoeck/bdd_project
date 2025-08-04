package de.uni_passau.fim.se2.st.mensawebapp.cucumber;

import de.uni_passau.fim.se2.st.mensawebapp.business.service.IngredientService;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Allergen;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class ShowAllergensStepDefinitions {

    private WebDriver driver;

    private WebDriverWait wait;

    @Given("I open the webapp for show allergens")
    public void iOpenTheWebappForCheckingDate() {
        WebDriverFactory.initDriver();
        driver = WebDriverFactory.getDriver();
        wait = WebDriverFactory.getWait();
        driver.get("http://localhost:8080/mensawebapp_war_exploded/");
    }

    @When("I choose allergens-selection")
    public void i_choose_allergen_selection() {
        WebElement allergenSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:filterAllergens")));
        allergenSelection.click();
    }

    @Then("I get all possible allergens")
    public void i_get_all_possible_allergens() {
        WebElement allergenSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:filterAllergens")));
        List<WebElement> existingAllergenElements = allergenSelection.findElements(By.tagName("option"));
        List<String> existingAllergensText = existingAllergenElements.stream()
                                                                     .map(WebElement::getText)
                                                                     .collect(Collectors.toList());
        IngredientService ingredientService = new IngredientService();
        List<Allergen> expectedAllergens = ingredientService.provideAllergens();
        List<String> expectedAllergenText = expectedAllergens.stream()
                                                             .map(Allergen::toString)
                                                             .collect(Collectors.toList());
        Assert.assertEquals("Lists of allergen do not match", expectedAllergenText, existingAllergensText);
        driver.close();
    }
}

