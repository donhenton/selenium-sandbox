/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.wicket;

import java.util.EnumMap;
import org.openqa.selenium.WebDriver;
 

/**
 *
 * @author dhenton
 */
public class RestaurantPageTester {
    
    public enum STATE {INITIAL,ADD,EDIT,DELETE};
    private EnumMap<STATE,BaseRestaurantPage> statePages = 
            new EnumMap<STATE,BaseRestaurantPage>(STATE.class);
    
    
    public RestaurantPageTester(WebDriver driver)
    {
        statePages.put(STATE.INITIAL,new InitialRestaurantPage(driver));
    }
    
    
    
}
