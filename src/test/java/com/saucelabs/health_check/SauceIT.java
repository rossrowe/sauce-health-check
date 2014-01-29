package com.saucelabs.health_check;

import com.saucelabs.ci.sauceconnect.SauceConnectTwoManager;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * This class contains the same logic as the {@link AcceptanceIT} class, but also demonstrates how a set of Selenium
 * tests can be easily updated to run against Sauce Labs.  This test is configured to run against multiple browsers (defined
 * by the {@link #browserStrings()} method, which relies on the JUnit {@link Parameterized} rule, which creates a test
 * instance per browser combination.
 * <p/>
 * As the tests are running against a local Jenkins instance, the tests are run within a <a href="https://saucelabs.com/docs/sauce-connect">Sauce Connect</a>
 * tunnel, which allows Selenium tests against local websites to be run using browsers within the Sauce Labs cloud.  Sauce Connect
 * is started before the tests run (via the {@link #startSauceConnect()} method) and stopped when the tests have completed (via the {@link #stopSauceConnect()} method).
 *
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
     * @param os      the operating system of the VM running the tests
     * @param version the browser version to be used for the tests
     * @param browser the browser to be used for the tests
     */
    public SauceIT(String os, String version, String browser) {
        super();
        this.os = os;
        this.version = version;
        this.browser = browser;
    }

    /**
     * @return the browser combinations to be used for the test.
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
//    @BeforeClass
    public static void startSauceConnect() throws IOException {
        sauceConnectManager.openConnection("SAUCE_USER", "SAUCE_ACCESS_KEY", 4445, null, null, null, null);
    }

    /**
     * Stops the Sauce Connect process after the tests have finished.
     *
     * @throws Exception if an error occurrs stopping the Sauce Connect process
     */
//    @AfterClass
    public static void stopSauceConnect() throws Exception {
        sauceConnectManager.closeTunnelsForPlan("SAUCE_USER", null);
    }

    /**
     * Creates a new {@link RemoteWebDriver} instance that is configured to launch a specific os/browser/version combination.
     *
     * @throws Exception thrown if an unexpected error occurs
     */
//    @Before
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
    @Ignore("Restore when a valid Sauce user name/access key is supplied")
    public void statusIsOkay() {
        WebElement sauceStatus = webDriver.findElement(By.id("sauce_status"));

        assertNotNull("Status not found", sauceStatus);
        WebElement sauceStatusMessage = webDriver.findElement(By.id("sauce_status_msg"));
        assertEquals("Status text not expected", "Basic service status checks passed.", sauceStatusMessage.getText());
    }

    /**
     * Click links to verify that the custom footer displays on each page.  This test also
     * demonstrates the various mechanisms you can use to select elements on page, using
     * the link text, element id, xpath and css selectors.
     */
    @Test
    @Ignore("Restore when a valid Sauce user name/access key is supplied")
    public void navigation() {

        assertNotNull("Status not found", webDriver.findElement(By.id("sauce_status")));
        //click on Manage Jenkins
        webDriver.findElement(By.linkText("Manage Jenkins")).click();
        //It takes a few seconds for the page to load, so instead of running Thread.sleep(), we use the WebDriverWait construct
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1")));
        assertNotNull("Status not found", webDriver.findElement(By.id("sauce_status")));

        //Click the 'New Job' link using the link text as a selector
        webDriver.findElement(By.linkText("New Job")).click();
        assertNotNull("Status not found", webDriver.findElement(By.id("sauce_status")));

        //Click on the 'Jenkins' link in the navigation bar using a XPath expression
        webDriver.findElement(By.xpath("//ul[@id=\"breadcrumbs\"]//a[1]")).click();
        //wait until the 'Welcome to Jenkins' div is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[@id='main-panel']/div[2][contains(text(), 'Welcome to Jenkins!')]")));
        assertNotNull("Status not found", webDriver.findElement(By.id("sauce_status")));

        //Click on the UI Samples link using a CSS selector
        webDriver.findElement(By.cssSelector("div.task:nth-child(5) > a:nth-child(2)")).click();
        assertNotNull("Status not found", webDriver.findElement(By.id("sauce_status")));
    }

    /**
     * Verifies the colour of the status element.
     */
    @Test
    @Ignore("Restore when a valid Sauce user name/access key is supplied")
    public void colourOfStatus() {
        WebElement sauceStatus = webDriver.findElement(By.className("sauce_up"));
        assertNotNull("Status not found", sauceStatus);
        String colour = sauceStatus.getCssValue("color");
        assertEquals("Colour not green", "rgba(0, 128, 0, 1)", colour);
    }

    /**
     * Verifies the behaviour of clicking on the 'Check Now' link, which performs an Ajax call to re-query the Sauce status.
     */
    @Test
    @Ignore("Restore when a valid Sauce user name/access key is supplied")
    public void ajaxAction() {
        WebElement sauceStatusProgressImage = webDriver.findElement(By.id("sauce_status_progress"));
        WebElement sauceStatusMessage = webDriver.findElement(By.id("sauce_status_msg"));
        assertFalse("Element is visible", sauceStatusProgressImage.isDisplayed());
        WebElement checkNowLink = webDriver.findElement(By.id("sauce_check_status_now"));
        assertNotNull("Status not found", checkNowLink);
        //click the link
        checkNowLink.click();
        //verify that the loading image is displayed and the status has changed to Checking...
        assertTrue("Element is not visible", sauceStatusProgressImage.isDisplayed());
        assertEquals("Status text not 'Checking'", "Checking...", sauceStatusMessage.getText());

        //wait until the status
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("sauce_status_progress")));
        assertEquals("Status text not expected", "Basic service status checks passed.", sauceStatusMessage.getText());
    }

    /**
     * Closes the webDriver session when the test has finished.
     *
     * @throws Exception thrown if an unexpected error occurs
     */
//    @After
    public void tearDown() throws Exception {
        webDriver.quit();
    }
}
