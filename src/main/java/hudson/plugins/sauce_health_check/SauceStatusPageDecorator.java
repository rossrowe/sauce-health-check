package hudson.plugins.sauce_health_check;

import hudson.Extension;
import hudson.model.PageDecorator;

/**
 * Supporting class which handles retrieving the Sauce system status to be displayed on the Jenkins UI.
 *
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
     *
     * @return the Sauce system status via the Sauce REST API
     */
    public String getsauceStatus() {
        return statusHelper.retrieveSauceStatus();
    }
}

