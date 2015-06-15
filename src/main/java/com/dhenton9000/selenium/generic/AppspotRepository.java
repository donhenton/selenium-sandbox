/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;

import org.apache.commons.configuration.Configuration;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.dhenton9000.selenium.generic.GenericAutomationRepository.*;

/**
 * code for interacting with the appspot application
 * http://donhenton.appspot.com
 * @author dhenton
 */
public class AppspotRepository {

    
    private GenericAutomationRepository automation;
    private static final Logger LOG = LoggerFactory.getLogger(AppspotRepository.class);

    public AppspotRepository(GenericAutomationRepository automation) {

        
        this.automation = automation;
         

    }

    

    public GenericAutomationRepository getAutomation() {
        return automation;
    }

    /**
     * @return the config
     */
    public Configuration getConfig() {
        return this.getAutomation().getConfig();
    }

    public void initialNavigation() {

        String url = getConfig().getString("test.env.url");
        LOG.debug("beginning load of browser " + url);
        if (url == null) {
            throw new RuntimeException("must specify 'test.env.url' in env.properties");
        }
        this.getAutomation().getDriver().get(url);
        getAutomation().maximizeWindow();
        getAutomation().getWaitMethods().waitForExpectedElement(2, SELECTOR_CHOICE.id, "menu");
        getAutomation().getWaitMethods().waitForDuration(3);

    }

}
