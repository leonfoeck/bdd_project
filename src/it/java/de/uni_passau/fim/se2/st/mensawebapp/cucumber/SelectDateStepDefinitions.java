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

public class SelectDateStepDefinitions {

    private WebDriver driver;

    private WebDriverWait wait;

    @Given("I open the webapp for selecting date")
    public void iOpenTheWebappForCheckingDate() {
        WebDriverFactory.initDriver();
        driver = WebDriverFactory.getDriver();
        wait = WebDriverFactory.getWait();
        driver.get("http://localhost:8080/mensawebapp_war_exploded/");
    }

    @When("I choose date-selection")
    public void i_choose_date_selection() {
        WebElement additiveSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate")));
        additiveSelection.click();
    }

    @And("I input 21.12.2023 in the date field")
    public void i_input_specific_date() {
        WebElement dateSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate")));
        dateSelection.clear();
        dateSelection.sendKeys("21.12.2023", Keys.ENTER);
    }

    @Then("I get the date 21.12.2023")
    public void i_get_the_date() {
        wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate"))
        ));
        WebElement dateSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate")));
        assertEquals("21.12.2023", dateSelection.getAttribute("value"));
        driver.close();
    }

}
