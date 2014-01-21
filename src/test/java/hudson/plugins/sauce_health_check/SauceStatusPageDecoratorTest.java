package hudson.plugins.sauce_health_check;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * @author Ross Rowe
 */
public class SauceStatusPageDecoratorTest {

    private SauceStatusPageDecorator pageDecorator;

    @Test
    public void errorResponse() {

        SauceStatusHelper statusHelper = new SauceStatusHelper() {

            @Override
            public String retrieveResults(URL restEndpoint) {
                return "{\"service_operational\" : false, \"status_message\" : \"Sauce Labs down\"}";
            }
        };
        pageDecorator = new SauceStatusPageDecorator(statusHelper);
        assertEquals("Sauce status mismatch", "Sauce Labs down", pageDecorator.getsauceStatus());

    }

    @Test
    public void successResponse() {
        SauceStatusHelper statusHelper = new SauceStatusHelper() {

            @Override
            public String retrieveResults(URL restEndpoint) {
                return "{\"service_operational\" : true, \"status_message\" : \"Basic service status checks passed.\"}";
            }
        };
        pageDecorator = new SauceStatusPageDecorator(statusHelper);
        assertEquals("Sauce status mismatch", "Basic service status checks passed.", pageDecorator.getsauceStatus());
    }

    @Test
    public void invalidResponse() {
        SauceStatusHelper statusHelper = new SauceStatusHelper() {

            @Override
            public String retrieveResults(URL restEndpoint) {
                return "";
            }
        };
        pageDecorator = new SauceStatusPageDecorator(statusHelper);
        assertEquals("Sauce status mismatch", "Unknown", pageDecorator.getsauceStatus());
    }

}
