/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.wicket;

 
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a testing class that represents the Restaurant Editing Form
 * page in its initial state. 
 *  
 * @author dhenton
 */
public class InitialRestaurantPage extends BaseRestaurantPage {
     
     
      private final Logger logger = LoggerFactory.getLogger(InitialRestaurantPage.class);
      
     
     public InitialRestaurantPage(WebDriver driver)
     {
         super(driver);
     }

  
}
