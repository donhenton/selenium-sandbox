/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.angular;

import static com.dhenton9000.angular.RestaurantRepository.CONTROLLER_SELECTOR;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class RestaurantCollection {

    private final ArrayList<RestaurantListing> restaurants
            = new ArrayList<>();
    private static final Logger LOG = LoggerFactory.getLogger(RestaurantCollection.class);
    private static final String rowSelector = CONTROLLER_SELECTOR +" table tr";
    // tr:nth-of-type(1) td
    private final GenericAutomationRepository automation;

    /**
     * @return the restaurants
     */
    public ArrayList<RestaurantListing> getRestaurants() {
        return restaurants;
    }

    public RestaurantCollection(GenericAutomationRepository automation) {
        this.automation = automation;
         
    }

    /**
     * @return the automation
     */
    public GenericAutomationRepository getAutomation() {
        return automation;
    }
    
    public void loadTable()
    {
        List<WebElement> restaurantRows = 
        getAutomation().findElements(
                GenericAutomationRepository.SELECTOR_CHOICE.cssSelector, 
                rowSelector);
        
        for (WebElement row: restaurantRows)
        {
            
            restaurants.add(new RestaurantListing(row));
            
            
        }
    }
}
