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

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Runs a series of tests which verify that the plugin's functionality works in a 'live' environment.
 *
 * @author Ross Rowe
 */
public class AcceptanceIT {

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
    public void statusIsOkay() {
        WebElement sauceStatus = webDriver.findElement(By.id("sauce_status"));
        assertNotNull("Status not found", sauceStatus);
    }

    /**
     * Click links to verify that footer displays on each page
     */
    @Test
    public void navigation() {


        WebElement sauceStatus = webDriver.findElement(By.id("sauce_status"));
        assertNotNull("Status not found", sauceStatus);

        //click on Manage Jenkins

    }

    /**
     * Verifies the colour of the status element.
     */
    @Test
    public void colourOfStatus() {
        WebElement sauceStatus = webDriver.findElement(By.className("sauce_up"));
        assertNotNull("Status not found", sauceStatus);
        String colour = sauceStatus.getCssValue("color");
        assertEquals("Colour not green", "rgba(0, 128, 0, 1)", colour);
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
