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
 *
 * @author dhenton
 */
public class BaseRestaurantPage {

    public static final String BASE_RESTAURANT_URL = "http://localhost:9090/wicket-sandbox/MaintainRestaurantsTwo";
    private final Logger logger = LoggerFactory.getLogger(BaseRestaurantPage.class);
    private final WebElement pickList;
    private final WebElement addButton;
    private final WebElement editForm;
    private final WebDriver driver;

    public BaseRestaurantPage(WebDriver driver) {
        this.driver = driver;
        this.driver.get(BASE_RESTAURANT_URL);
        this.pickList = null;
        this.addButton = null;
        this.editForm = null;
    }

    /**
     * @return the pickList
     */
    public WebElement getPickList() {
        return pickList;
    }

    /**
     * @return the addButton
     */
    public WebElement getAddButton() {
        return addButton;
    }

    /**
     * @return the editForm
     */
    public WebElement getEditForm() {
        return editForm;
    }

    /**
     * @return the driver
     */
    public WebDriver getDriver() {
        return driver;
    }
}
