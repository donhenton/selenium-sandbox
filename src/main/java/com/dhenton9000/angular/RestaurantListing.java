/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.angular;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * represents a row in the restaurant app pick list.
 *
 * @author dhenton
 */
public class RestaurantListing {

    private String name;
    private String city;
    private String state;
    private String zip;
    private int version;
    private WebElement editButton;
    private WebElement deleteButton;
    private boolean currentlySelected = false;
    private static final Logger LOG = LoggerFactory.getLogger(RestaurantListing.class);

    RestaurantListing(WebElement row) {
        
        name = row.findElement(By.cssSelector("td:nth-of-type(1)")).getText().trim();
        LOG.debug(String.format("loading '%s'",name));
        city = row.findElement(By.cssSelector("td:nth-of-type(2)")).getText().trim();
        state = row.findElement(By.cssSelector("td:nth-of-type(3)")).getText().trim();
        zip = row.findElement(By.cssSelector("td:nth-of-type(4)")).getText().trim();
        version = 
        Integer.parseInt(row.findElement(By.cssSelector("td:nth-of-type(5)")).getText().trim());
        editButton = 
        row.findElement(By.cssSelector("td:nth-of-type(6) span"));
        deleteButton = 
        row.findElement(By.cssSelector("td:nth-of-type(7) span"));
        
        
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

     

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    

    /**
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    

    /**
     * @return the editButton
     */
    public WebElement getEditButton() {
        return editButton;
    }

     

    /**
     * @return the deleteButton
     */
    public WebElement getDeleteButton() {
        return deleteButton;
    }

    

    /**
     * @return the currentlySelected
     */
    public boolean isCurrentlySelected() {
        return currentlySelected;
    }

    /**
     * @param currentlySelected the currentlySelected to set
     */
    public void setCurrentlySelected(boolean currentlySelected) {
        this.currentlySelected = currentlySelected;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

}
