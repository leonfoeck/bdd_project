package de.uni_passau.fim.se2.st.mensawebapp.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExcludingStuffStepDefinitions {

    private WebDriver driver;

    private WebDriverWait wait;

    @Given("I open webapp to exclude stuff")
    public void iOpenTheWebappForCheckingDate() {
        WebDriverFactory.initDriver();
        driver = WebDriverFactory.getDriver();
        wait = WebDriverFactory.getWait();
        driver.get("http://localhost:8080/mensawebapp_war_exploded/");
    }

    @When("I clean the date field for pretty excluding")
    public void iCleanTheDateFieldForPrettyExcluding() {
        WebElement dateSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate")));
        dateSelection.clear();
        assertEquals("", dateSelection.getAttribute("value"));
    }

    @And("I select the date 5 December 2022 for excluding")
    public void iSelectTheDateDecemberForExlcluding() {
        WebElement dateSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate")));
        dateSelection.sendKeys("05.12.2022", Keys.ENTER);
        assertEquals("05.12.2022", dateSelection.getAttribute("value"));
    }

    @And("I exclude additives, tags and allergens from the menu")
    public void iExcludeAdditivesTagsAndAllergensFromTheMenu() {
        // Exclude the first additive
        WebElement additiveSelection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtering:filterAdditives")));
        List<WebElement> additives = additiveSelection.findElements(By.tagName("option"));
        additives.get(15).click();

        // Exclude the first allergen
        WebElement allergenSelection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtering:filterAllergens")));
        List<WebElement> allergens = allergenSelection.findElements(By.tagName("option"));
        allergens.get(1).click();

        // Exclude the first tag
        WebElement tagSelection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtering:filterTags")));
        List<WebElement> tags = tagSelection.findElements(By.tagName("option"));
        tags.get(2).click();

    }

    @And("I press the submit button for pretty excluding")
    public void iPressTheSubmitButtonForPrettyExcluding() {
        WebElement submissionButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtering:showMenu")));
        try {
            submissionButton.click();
        } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
            submissionButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtering:showMenu")));
            submissionButton.click();
        }
    }

    @Then("I should have a menu without some additives, tags and allergens")
    public void iShouldHaveAMenuWithoutSomeAdditivesTagsAndAllergens() {
        String expectedNames
                = "Orientalische Linsen-Apfel-Kürbissuppe, Falafel auf Tomatenrisotto, Gartengemüse, Bio-Reis Djuvic Art, Salatmix, "
                  + "Kartoffel-Gurkensalat, Bio-Erdbeerjoghurt, Veganer Apfelpudding";
        String xpathForDishNames = "//*[@id='filtering:dishTable']/tbody/tr/td[2]";
        List<WebElement> namesOfDishes = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathForDishNames)));
        String actualNames = namesOfDishes.stream().map(WebElement::getText).collect(Collectors.joining(", "));
        assertEquals(expectedNames, actualNames);
        driver.close();
    }
}
