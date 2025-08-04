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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrettyPrintAllergensStepDefinitions {

    private WebDriver driver;

    private WebDriverWait wait;

    @Given("I open the webapp for pretty printing allergens")
    public void iOpenTheWebappForCheckingDate() {
        WebDriverFactory.initDriver();
        driver = WebDriverFactory.getDriver();
        wait = WebDriverFactory.getWait();
        driver.get("http://localhost:8080/mensawebapp_war_exploded/");
    }

    @When("I clean the date field for pretty print")
    public void i_clean_the_date_field() {
        WebElement dateSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate")));
        dateSelection.clear();
        assertEquals("", dateSelection.getAttribute("value"));
    }


    @When("I select the date 5 December 2022 for pretty print")
    public void iSelectTheDateDecember() {
        WebElement dateSelection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtering:selectDate")));
        dateSelection.sendKeys("05.12.2022", Keys.ENTER);
        assertEquals("05.12.2022", dateSelection.getAttribute("value"));
    }

    @And("I press the submit button")
    public void iPressTheSubmitButton() {
        WebElement submissionButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtering:showMenu")));
        try {
            submissionButton.click();
        } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
            submissionButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtering:showMenu")));
            submissionButton.click();
        }
    }

    @Then("The allergens for the Cannelloni should be pretty looking")
    public void theAllergensForCannelloni() {
        WebElement cannelloniAllergens = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"filtering:dishTable:3:allergens\"]")));
        String expectedAllergens
                = "<abbr title=\"Milch und Milchprodukte\">G</abbr>, <abbr title=\"Sellerie\">I</abbr>, <abbr title=\"Soja\">F</abbr>, <abbr "
                  + "title=\"Weizengluten\">AA</abbr>";
        assertEquals(expectedAllergens, cannelloniAllergens.getAttribute("innerHTML"));
        driver.close();
    }
}
