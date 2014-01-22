package com.saucelabs.sauce_health_check;

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

import static org.junit.Assert.assertNotNull;

/**
 * Selenium test which validates the plugin's behaviour against a local Jenkins instance
 * (started via the {@link JenkinsRule} instance.
 *
 * @author Ross Rowe
 */
public class SauceHealthCheckIT {

    private WebDriver webDriver;

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    @Before
    public void setUp() throws Exception {
        webDriver = new FirefoxDriver();
        URL url = jenkinsRule.getURL();
        webDriver.get(url.toString());
    }

    @Test
    public void sauceStatusCheck() {
        WebElement sauceStatus = webDriver.findElement(By.id("sauce_status"));
        assertNotNull("Status not found", sauceStatus);
    }

    @After
    public void tearDown() throws Exception {
        webDriver.quit();
    }
}
