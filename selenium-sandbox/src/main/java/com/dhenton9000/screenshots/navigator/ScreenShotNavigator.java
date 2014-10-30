/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshots.navigator;

import com.dhenton9000.selenium.generic.GenericAutomationRepository; 

/**
 * Code for screenshot setup. 
 * This is a piece of code where complete instructions exist
 * to navigate to a given page and to prep 
 * that page for the screen shot of interest.

 * @author dhenton
 */
public abstract class ScreenShotNavigator {

    

    public ScreenShotNavigator() {
         
    }

    

    /**
     * child classes will fill out this method, it has access to all
     * repositories it needs for navigation and page actions
     *  
     * @param genericRepository
     */
    public abstract void navigate(  GenericAutomationRepository genericRepository);

     
}
