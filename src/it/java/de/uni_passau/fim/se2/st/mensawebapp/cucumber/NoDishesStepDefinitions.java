package de.uni_passau.fim.se2.st.mensawebapp.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NoDishesStepDefinitions {

    private WebDriver driver;

    private WebDriverWait wait;

    @Given("I open the webapp on Sunday")
    public void iOpenTheWebappOnSunday() {
        WebDriverFactory.initDriver();
        driver = WebDriverFactory.getDriver();
        wait = WebDriverFactory.getWait();
        driver.get("http://localhost:8080/mensawebapp_war_exploded/");
        WebElement dateSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate")));
        dateSelection.clear();
        assertEquals("", dateSelection.getAttribute("value"));
        WebElement dateSelection2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:selectDate")));
        dateSelection2.sendKeys("10.08.2022", Keys.ENTER);
        assertEquals("10.08.2022", dateSelection2.getAttribute("value"));
    }

    @And("I cant see a datatable")
    public void iCantSeeADatatable() throws InterruptedException {
        WebElement submitSelectBox = null;
        try {
            submitSelectBox = driver.findElement(By.id("filtering:dishTable:0"));
        } catch (org.openqa.selenium.NoSuchElementException ignored) {
        }
        assertNull(submitSelectBox);
        driver.close();
    }
}
