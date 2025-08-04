package de.uni_passau.fim.se2.st.mensawebapp.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WelcomePageStepDefinitions {

    private WebDriver driver;

    private WebDriverWait wait;

    @Given("I open the webapp for welcome page")
    public void iOpenTheWebappForCheckingDate() {
        WebDriverFactory.initDriver();
        driver = WebDriverFactory.getDriver();
        wait = WebDriverFactory.getWait();
        driver.get("http://localhost:8080/mensawebapp_war_exploded/");
    }

    @Then("I see the date for today in the date field")
    public void iSeeTheDateForTodayInTheDateField() {
        WebElement dateSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate")));
        assertEquals(dateSelection.getAttribute("value"), LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }


    @And("I should see the menu for today")
    public void iShouldSeeAMenu() {
        WebElement dataTable = null;
        try {
            dataTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtering:dishTable")));
        } catch (org.openqa.selenium.TimeoutException ignored) {
        }
        assertNotNull(dataTable);
        assertTrue(dataTable.isDisplayed(), "The data table is not displayed.");
        driver.close();
    }
}
