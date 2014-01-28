package com.saucelabs.health_check;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles retrieving the Sauce Labs system status via the Sauce REST API.
 *
 * @author Ross Rowe
 */
public class SauceStatusHelper {

    private static final Logger logger = Logger.getLogger(SauceStatusHelper.class.getName());

    /**
     * Sauce REST URL which returns the system status.
     */
    private static final String STATUS_URL = "http://saucelabs.com/rest/v1/info/status";

    /**
     * Retrieves the 'status_message' attribute from the Sauce system response from the Sauce REST API.
     * <p/>
     * If the status is unable to be retrieved, then 'Unknown' is returned.
     *
     * @return
     */
    public String retrieveSauceStatus() {
        try {
            URL restEndpoint = new URL(STATUS_URL);
            String response = retrieveResults(restEndpoint);
            JSONObject jsonObject = JSONObject.fromObject(response);
            //typical response includes wait_time, service_operational and status_message attributes
            return (String) jsonObject.get("status_message");
        } catch (JSONException e) {
            logger.log(Level.WARNING, "Error parsing Sauce Status response", e);

        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Error constructing Sauce Status URL", e);
        }
        return "Unknown";

    }

    /**
     * Returns the result of a HTTP GET for the <code>restEndpoint</code>.
     *
     * @param restEndpoint URL to connect to
     * @return String representing the output from the HTTP GET request
     */
    public String retrieveResults(URL restEndpoint) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            HttpURLConnection connection = (HttpURLConnection) restEndpoint.openConnection();
            connection.setDoOutput(true);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                builder.append(inputLine);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error retrieving Sauce Results", e);
        }
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing input stream", e);
        }
        return builder.toString();
    }

}
