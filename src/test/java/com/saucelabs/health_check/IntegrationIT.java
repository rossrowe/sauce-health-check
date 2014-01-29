package com.saucelabs.health_check;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Integration tests which use Selenium to performs a minimal set of validation to ensure that
 * the plugin is installed and integrates with Jenkins correctly.
 *
 * @author Ross Rowe
 */
public class IntegrationIT {

    /**
     * WebDriver instance which will be used to perform browser interactions.
     */
    private WebDriver webDriver;

    /**
     * JUnit rule which instantiates a local Jenkins instance with our plugin installed.
     */
    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    /**
     * Creates a new {@link FirefoxDriver} instance, and instructs Firefox to open the URL associated with the
     * local Jenkins instance.
     *
     * @throws Exception thrown if an unexpected error occurs
     */
    @Before
    public void setUp() throws Exception {
        webDriver = new FirefoxDriver();
        URL url = jenkinsRule.getURL();
        webDriver.get(url.toString());
    }

    /**
     * Verifies that the plugin has been installed correctly.
     */
    @Test
    public void pluginInstalled() {
        WebElement sauceStatus = webDriver.findElement(By.id("sauce_status"));
        assertNotNull("Status not found", sauceStatus);
    }

    /**
     * Verifies that the plugin appears within the 'Manage Jenkins' - 'Plugins' - 'Installed' list.
     */
    @Test
    public void pluginIsListed() {
        //Find and click on the 'Manage Jenkins' link
        webDriver.findElement(By.linkText("Manage Jenkins")).click();
        //It takes a few seconds for the page to load, so instead of running Thread.sleep(), we use the WebDriverWait construct
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1")));
        //assert that we are on the Manage Jenkins page
        assertEquals("Header not found", "Manage Jenkins", webDriver.findElement(By.cssSelector("h1")).getText());
        //Find and click on the 'Manage Plugins' link
        webDriver.findElement(By.linkText("Manage Plugins")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Installed")));
        //Find and click on the 'Installed' link
        webDriver.findElement(By.linkText("Installed")).click();
        assertEquals("Plugin link not found", "Jenkins Sauce Health Check plugin", webDriver.findElement(By.linkText("Jenkins Sauce Health Check plugin")).getText());
    }

    /**
     * Closes the webDriver session when the test has finished.
     *
     * @throws Exception thrown if an unexpected error occurs
     */
    @After
    public void tearDown() throws Exception {
        webDriver.quit();
    }
}
