package com.saucelabs.health_check;

import com.saucelabs.ci.sauceconnect.SauceConnectTwoManager;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Ross Rowe
 */
@RunWith(Parameterized.class)
public class SauceIT {

    /**
     * Handles opening and closing the <a href="http://saucelabs.com/docs/sauce-connect">Sauce Connect</a> tunnel.
     */
    private static final SauceConnectTwoManager sauceConnectManager = new SauceConnectTwoManager();

    /**
     * String representing the operating system that the test should be run against.
     */
    private final String os;
    /**
     * String representing the browser version that the test should be run against.
     */
    private final String version;
    /**
     * String representing the browser that the test should be run against.
     */
    private final String browser;
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
     * Constructs a new instance using the specified os/version/browser (as defined by the {@link #browserStrings()} method.
     *
     * @param os
     * @param version
     * @param browser
     */
    public SauceIT(String os, String version, String browser) {
        super();
        this.os = os;
        this.version = version;
        this.browser = browser;
    }

    /**
     *
     * @return the browser combinations to be used for the test.
     *
     * @throws Exception
     */
    @Parameterized.Parameters
    public static LinkedList browserStrings() throws Exception {
        LinkedList browsers = new LinkedList();
        browsers.add(new String[]{"Windows 2003", null, "chrome"});
        browsers.add(new String[]{"Windows 2003", "17", "firefox"});
        browsers.add(new String[]{"linux", "17", "firefox"});
        return browsers;
    }

    /**
     * Starts a Sauce Connect process prior to the execution of the tests.
     *
     * @throws IOException if an error occurs starting Sauce Connect
     */
    @BeforeClass
    public static void startSauceConnect() throws IOException {
        sauceConnectManager.openConnection("SAUCE_USER", "SAUCE_ACCESS_KEY", 4445, null, null, null, null);
    }

    /**
     * Stops the Sauce Connect process after the tests have finished.
     *
     * @throws Exception if an error occurrs stopping the Sauce Connect process
     */
    @AfterClass
    public static void stopSauceConnect() throws Exception {
        sauceConnectManager.closeTunnelsForPlan("SAUCE_USER", null);
    }

    /**
     * Creates a new {@link RemoteWebDriver} instance that is configured to launch a specific os/browser/version combination.
     *
     * @throws Exception thrown if an unexpected error occurs
     */
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        capabilities.setCapability(CapabilityType.VERSION, version);
        capabilities.setCapability(CapabilityType.PLATFORM, Platform.valueOf(os));
        this.webDriver = new RemoteWebDriver(
                new URL("http://SAUCE_USER_ID:SAUCE_ACCESS_KEY@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);

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
