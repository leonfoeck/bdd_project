package de.uni_passau.fim.se2.st.mensawebapp.cucumber;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverFactory {
  private static WebDriver driver;
  private static WebDriverWait wait;

  public static WebDriver getDriver() {
    return driver;
  }

  public static WebDriverWait getWait() {
    return wait;
  }

  public static void initDriver() {
    driver = createWebDriver();
    wait = new WebDriverWait(driver, Duration.ofSeconds(5));
  }

  public static void closeDriver() {
    driver.close();
  }

  public static WebDriver createWebDriver() {
    WebDriverManager.firefoxdriver().setup();
    final FirefoxOptions options = new FirefoxOptions();
    if (System.getenv("GITLAB_CI") != null || System.getenv("JENKINS_NODE_COOKIE") != null) {
      options.addArguments("--headless");
    }
    return new FirefoxDriver(options);
  }
}
