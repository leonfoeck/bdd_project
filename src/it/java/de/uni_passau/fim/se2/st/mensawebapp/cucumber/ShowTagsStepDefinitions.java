package de.uni_passau.fim.se2.st.mensawebapp.cucumber;

import de.uni_passau.fim.se2.st.mensawebapp.business.service.IngredientService;
import de.uni_passau.fim.se2.st.mensawebapp.global.dish.Tag;
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

public class ShowTagsStepDefinitions {

    private WebDriver driver;

    private WebDriverWait wait;

    @Given("I open the webapp for show tags")
    public void iOpenTheWebappForCheckingDate() {
        WebDriverFactory.initDriver();
        driver = WebDriverFactory.getDriver();
        wait = WebDriverFactory.getWait();
        driver.get("http://localhost:8080/mensawebapp_war_exploded/");
    }

    @When("I choose tag-selection")
    public void i_choose_tag_selection() {
        WebElement tagSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:filterTags")));
        tagSelection.click();
    }

    @Then("I get all possible tags")
    public void i_get_all_possible_tags() {
        WebElement tagSelection = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("filtering:filterTags")));
        List<WebElement> existingTagElements = tagSelection.findElements(By.tagName("option"));
        List<String> existingTagsText = existingTagElements.stream()
                                                           .map(WebElement::getText)
                                                           .collect(Collectors.toList());
        IngredientService ingredientService = new IngredientService();
        List<Tag> expectedTags = ingredientService.provideTags();
        List<String> expectedTagText = expectedTags.stream()
                                                   .map(Tag::toString)
                                                   .collect(Collectors.toList());
        Assert.assertEquals("Lists of tag do not match", expectedTagText, existingTagsText);
        driver.close();
    }
}

