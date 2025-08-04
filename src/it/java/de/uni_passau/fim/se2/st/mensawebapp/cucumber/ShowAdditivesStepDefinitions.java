package de.uni_passau.fim.se2.st.mensawebapp.cucumber;

import de.uni_passau.fim.se2.st.mensawebapp.business.service.IngredientService;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Additive;
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

public class ShowAdditivesStepDefinitions {

    private WebDriver driver;

    private WebDriverWait wait;

    @Given("I open the webapp for show additives")
    public void iOpenTheWebappForCheckingDate() {
        WebDriverFactory.initDriver();
        driver = WebDriverFactory.getDriver();
        wait = WebDriverFactory.getWait();
        driver.get("http://localhost:8080/mensawebapp_war_exploded/");
    }


    @When("I choose additive-selection")
    public void i_choose_additive_selection() {
        WebElement additiveSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:filterAdditives")));
        additiveSelection.click();
    }

    @Then("I get all possible additives")
    public void i_get_all_possible_additives() {
        WebElement additiveSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:filterAdditives")));
        List<WebElement> existingAdditivesElements = additiveSelection.findElements(By.tagName("option"));
        List<String> existingAdditivesText = existingAdditivesElements.stream()
                                                                      .map(WebElement::getText)
                                                                      .collect(Collectors.toList());
        IngredientService ingredientService = new IngredientService();
        List<Additive> expectedAdditives = ingredientService.provideAdditives();
        List<String> expectedAdditivesText = expectedAdditives.stream()
                                                              .map(Additive::toString)
                                                              .collect(Collectors.toList());
        Assert.assertEquals("Lists of additives do not match", expectedAdditivesText, existingAdditivesText);
        driver.close();
    }
}
