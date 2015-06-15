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

/**
 * Code for interacting with google search engine.
 *
 * @author dhenton
 */
public class NYTimesRepository {

   
    private final GenericAutomationRepository automation;
    private static final Logger LOG = LoggerFactory.getLogger(NYTimesRepository.class);

    
    public NYTimesRepository(GenericAutomationRepository automation) {

        
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

        String url = "http://www.nytimes.com";
        
        this.getAutomation().getDriver().get(url);
        getAutomation().maximizeWindow();
        getAutomation().getWaitMethods().waitForExpectedElement(2, 
                GenericAutomationRepository.SELECTOR_CHOICE.id, 
                "site-index-navigation");

    }


}
