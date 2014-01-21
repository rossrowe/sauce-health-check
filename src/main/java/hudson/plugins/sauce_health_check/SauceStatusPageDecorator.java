package hudson.plugins.sauce_health_check;

import hudson.Extension;
import hudson.model.PageDecorator;

/**
 * @author Ross Rowe
 */
@Extension
public class SauceStatusPageDecorator extends PageDecorator {

    private SauceStatusHelper statusHelper;

    public SauceStatusPageDecorator() {
        this(new SauceStatusHelper());
    }

    public SauceStatusPageDecorator(SauceStatusHelper statusHelper) {
        super();
        this.statusHelper = statusHelper;
    }

    public String getsauceStatus() {
        return statusHelper.retrieveSauceStatus();
    }
}

