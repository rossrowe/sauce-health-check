package hudson.plugins.sauce_health_check;

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
 * @author Ross Rowe
 */
public class SauceStatusHelper {

    private static final Logger logger = Logger.getLogger(SauceStatusHelper.class.getName());

    private static final String STATUS_URL = "http://saucelabs.com/rest/v1/info/status";

    public String retrieveSauceStatus() {
        URL restEndpoint = null;
        try {
            restEndpoint = new URL(STATUS_URL);
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Error constructing Sauce Status URL", e);
        }
        String response = retrieveResults(restEndpoint);
        try {
            JSONObject jsonObject = JSONObject.fromObject(response);
            //typical response includes wait_time, service_operational and status_message attributes
            return (String) jsonObject.get("status_message");
        } catch (JSONException e) {
            logger.log(Level.WARNING, "Error parsing Sauce Status response", e);
        }
        return "Unknown";

    }

    private HttpURLConnection openConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    public String retrieveResults(URL restEndpoint) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            HttpURLConnection connection = openConnection(restEndpoint);

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
            logger.log(Level.WARNING, "Error closing Sauce input stream", e);
        }
        return builder.toString();
    }

}
