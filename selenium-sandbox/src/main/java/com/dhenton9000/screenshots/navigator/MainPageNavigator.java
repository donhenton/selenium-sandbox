/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshots.navigator;

import com.dhenton9000.selenium.generic.GenericAutomationRepository;

 
/**
 *
 * @author dhenton
 */
public class MainPageNavigator extends ScreenShotNavigator {

    

    @Override
    public void navigate(GenericAutomationRepository genericRepository) {
        
       genericRepository.getDriver().get("http://donhenton.appspot.com");
       genericRepository.maximizeWindow();
        
    }

    
    
    
}
