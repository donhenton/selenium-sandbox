/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.angular;

import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import com.dhenton9000.selenium.generic.WaitMethods;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class RestaurantRepository {

    private final GenericAutomationRepository automation;
    private static final Logger LOG = LoggerFactory.getLogger(RestaurantRepository.class);
    public static final String CONTROLLER_SELECTOR = "div[ng-controller=\"listRestaurantController\"]";

    public RestaurantRepository(GenericAutomationRepository automation) {

        this.automation = automation;

    }

    /**
     * @return the automation
     */
    public GenericAutomationRepository getAutomation() {
        return automation;
    }

    public void navigateToRestaurantApp() {
        getAutomation().getDriver()
                .get("http://donhenton-node.herokuapp.com/restaurant.doc");

        this.getAutomation().getWaitMethods()
                .waitForExpectedElement(WaitMethods.getWaitTime(),
                        GenericAutomationRepository.SELECTOR_CHOICE.cssSelector,
                        CONTROLLER_SELECTOR);

    }

    /**
     * find the current selected restaurant.
     *
     * @return
     */
    public WebElement getSelectedRestaurantName() {
        return this.getAutomation().findElement(
                GenericAutomationRepository.SELECTOR_CHOICE.cssSelector,
                "div.scrollList td.currentUserRow[data-id]");
    }

    public List<String> getReviewsForCurrentRestaurant() {
        String reviewSelector = "div[ng-controller=\"reviewController\"] "
                + "table td:nth-of-type(2) span span";
        List<WebElement> reviewElems
                = this.getAutomation().findElements(
                        GenericAutomationRepository.SELECTOR_CHOICE.cssSelector,
                        reviewSelector);

        ArrayList<String> items = new ArrayList<>();

        for (WebElement w : reviewElems) {
            items.add(w.getText().trim());
        }

        return items;

    }

    public String[] getRestaurantRowByIdx(int idx) {
        String[] res = new String[5];
        String rowSelectorTemplate
                = "div[ng-controller=\"listRestaurantController\"] "
                + "table tr:nth-of-type(%d) td";

        String rowSelector = String.format(rowSelectorTemplate, idx);
        List<WebElement> cellsFound = null;
        try {
            cellsFound
                    = this.getAutomation().findElements(
                            GenericAutomationRepository.SELECTOR_CHOICE.cssSelector,
                            rowSelector);
        } catch (Exception err) {
        }
        if (cellsFound.isEmpty()) {
            return null;
        }

        for (int j = 0; j < res.length; j++) {
            res[j] = cellsFound.get(j).getText().trim();
        }

        return res;
    }
}
