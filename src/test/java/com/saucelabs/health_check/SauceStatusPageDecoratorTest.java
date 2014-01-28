package com.saucelabs.health_check;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Unit test which validates the behaviour of the {@link SauceStatusPageDecorator} class by simulating expected responses from
 * the Sauce REST API.
 *
 * @author Ross Rowe
 */
public class SauceStatusPageDecoratorTest {


    /**
     * Validates the behaviour of the {@link SauceStatusPageDecorator} when 'Sauce Labs down' response is returned.
     */
    @Test
    public void errorResponse() {

        SauceStatusHelper statusHelper = new SauceStatusHelper() {

            @Override
            public String retrieveResults(URL restEndpoint) {
                return "{\"service_operational\" : false, \"status_message\" : \"Sauce Labs down\"}";
            }
        };
        SauceStatusPageDecorator pageDecorator = new SauceStatusPageDecorator(statusHelper);
        assertEquals("Sauce status mismatch", "Sauce Labs down", pageDecorator.getsauceStatus());

    }

    /**
     * Validates the behaviour of the {@link SauceStatusPageDecorator} when 'service is up' response is returned.
     */
    @Test
    public void successResponse() {
        SauceStatusHelper statusHelper = new SauceStatusHelper() {

            @Override
            public String retrieveResults(URL restEndpoint) {
                return "{\"service_operational\" : true, \"status_message\" : \"Basic service status checks passed.\"}";
            }
        };
        SauceStatusPageDecorator pageDecorator = new SauceStatusPageDecorator(statusHelper);
        assertEquals("Sauce status mismatch", "Basic service status checks passed.", pageDecorator.getsauceStatus());
    }

    /**
     * Validates the behaviour of the {@link SauceStatusPageDecorator} when an empty string (error response) is returned.
     */
    @Test
    public void invalidResponse() {
        SauceStatusHelper statusHelper = new SauceStatusHelper() {

            @Override
            public String retrieveResults(URL restEndpoint) {
                return "";
            }
        };
        SauceStatusPageDecorator pageDecorator = new SauceStatusPageDecorator(statusHelper);
        assertEquals("Sauce status mismatch", "Unknown", pageDecorator.getsauceStatus());
    }

}
