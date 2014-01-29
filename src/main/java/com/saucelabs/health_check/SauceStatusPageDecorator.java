package com.saucelabs.health_check;

import hudson.Extension;
import hudson.model.PageDecorator;
import org.kohsuke.stapler.bind.JavaScriptMethod;

/**
 * Supporting class which handles retrieving the Sauce system status to be displayed on the Jenkins UI.
 * <p/>
 * The status is only displayed on the footer of the page (via the resources/hudson/plugins/sauce_health_check/SauceStatusPageDecorator/footer.jelly file).
 *
 * @author Ross Rowe
 */
@Extension
public class SauceStatusPageDecorator extends PageDecorator {

    /**
     * Handles performing the Sauce REST API call.
     */
    private SauceStatusHelper statusHelper;

    /**
     * Constructs a new instance.
     */
    public SauceStatusPageDecorator() {
        this(new SauceStatusHelper());
    }

    /**
     * Constructs a new instance.
     *
     * @param statusHelper
     */
    public SauceStatusPageDecorator(SauceStatusHelper statusHelper) {
        super();
        this.statusHelper = statusHelper;
    }

    /**
     * @return the Sauce system status via the Sauce REST API
     */
    public String getsauceStatus() {
        return statusHelper.retrieveSauceStatus();
    }

    /**
     * @return the Sauce system status via the Sauce REST API
     */
    public String getsauceStatusClass() {
        String sauceStatus = getsauceStatus();
        if (sauceStatus.equals("Unknown")) {
            return "sauce_unknown";
        } else if (sauceStatus.equals("Basic service status checks passed.")) {
            return "sauce_up";
        }
        return "sauce_down";
    }

    /**
     * Method which is exposed as a javascript method.
     * @return
     */
    @JavaScriptMethod
    public String checkStatusNow() {
        //Wait for 5 seconds to simulate some server activity
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getsauceStatus();
    }
}

