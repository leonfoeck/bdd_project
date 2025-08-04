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

public class PrettyPrintTagsStepDefinitions {

    private WebDriver driver;

    private WebDriverWait wait;

    @Given("I open the webapp for pretty printing tags")
    public void iOpenTheWebappForCheckingDate() {
        WebDriverFactory.initDriver();
        driver = WebDriverFactory.getDriver();
        wait = WebDriverFactory.getWait();
        driver.get("http://localhost:8080/mensawebapp_war_exploded/");
    }

    @When("I clean the date field for pretty print tags")
    public void i_clean_the_date_field() {
        WebElement dateSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate")));
        dateSelection.clear();
        assertEquals("", dateSelection.getAttribute("value"));
    }


    @And("I select the date 5 December 2022 for pretty print tags")
    public void iSelectTheDateDecember() {
        WebElement dateSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate")));
        dateSelection.sendKeys("05.12.2022", Keys.ENTER);
        assertEquals("05.12.2022", dateSelection.getAttribute("value"));
    }

    @And("I press the submit button for pretty print tags")
    public void iPressTheSubmitButton() {
        WebElement submissionButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtering:showMenu")));
        try {
            submissionButton.click();
        } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
            submissionButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtering:showMenu")));
            submissionButton.click();
        }
    }

    @Then("The tags for the Cannelloni should be pretty looking")
    public void theTagsForCannelloni() {
        WebElement cannelloniTags = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"filtering:dishTable:4:tags\"]")));
        String expectedTags = "Schweinefleisch";
        assertEquals(expectedTags, cannelloniTags.getAttribute("innerHTML"));
        driver.close();
    }
}
