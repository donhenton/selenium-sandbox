/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshots.testbeds;

import com.dhenton9000.screenshots.AbstractSingleScreenShot;
import com.dhenton9000.selenium.generic.AppspotRepository;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;

/**
 *
 * @author dhenton
 */
public class AppComparisonScreenShot extends AbstractSingleScreenShot {
    
    public static final String SIMPLE_IMAGE_NAME = "appSample";
    
    public AppComparisonScreenShot(GenericAutomationRepository g, AppspotRepository app) {
        super(g, app);
    }
    
    @Override
    public void setUpScreenshot() {
        this.getGenericAutomationRepository().maximizeWindow();
        this.getGenericAutomationRepository().getDriver().get("http://donhenton.com");
        this.getGenericAutomationRepository().getWaitMethods().waitForDuration(3);
    }
    
    @Override
    public void cleanUp() {
         this.getGenericAutomationRepository().getDriver().close();
          this.getGenericAutomationRepository().getDriver().quit();
    }
    
}
