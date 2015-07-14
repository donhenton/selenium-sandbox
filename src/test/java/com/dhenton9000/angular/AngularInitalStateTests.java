/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.angular;

import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author dhenton
 */
public class AngularInitalStateTests extends AngularTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(AngularTests.class);

    @BeforeClass
    public void beforeClass() {

        this.getRestaurantRepository().getAutomation().maximizeWindow();
    }

    @BeforeMethod
    public void beforeTest() {
        this.getRestaurantRepository().navigateToRestaurantApp();
    }

    @Test
    public void checkInitialStateAndClickDisplaysEditArea() {
        assertEquals(this.getRestaurantRepository()
                .getReviewsForCurrentRestaurant().size(), 0);
        //edit form is not visible
        List<WebElement> formEdit
                = this.getAutomationRepository().findElements(
                        GenericAutomationRepository.SELECTOR_CHOICE.cssSelector,
                        "form[name=\"editRestaurantForm\"]");
        assertEquals(formEdit.size(), 1);
        assertFalse(formEdit.get(0).isDisplayed());
        clickOnRestaurant(3);

        formEdit
                = this.getAutomationRepository().findElements(
                        GenericAutomationRepository.SELECTOR_CHOICE.cssSelector,
                        "form[name=\"editRestaurantForm\"]");

        assertTrue(formEdit.get(0).isDisplayed());

    }

    @Test
    public void testEditAreaAndRowHaveSameData() {

        int idx = 2;
        clickOnRestaurant(idx);

        List<WebElement> formEdit
                = this.getAutomationRepository().findElements(
                        GenericAutomationRepository.SELECTOR_CHOICE.cssSelector,
                        "form[name=\"editRestaurantForm\"]");

        String[] res= 
        this.getRestaurantRepository().getRestaurantRowByIdx(idx);
        assertEquals(res[0],"Arby's Roast Beef Restaurant");

    }

    private void clickOnRestaurant(int idx) {
        String rowSelector = "div[ng-controller="
                + "\"listRestaurantController\"] "
                + "span[data-ng-click^=\"change\"]";

        List<WebElement> rowEditButtons
                = this.getAutomationRepository().findElements(
                        GenericAutomationRepository.SELECTOR_CHOICE.cssSelector,
                        rowSelector);
        if (idx < rowEditButtons.size()) {
            rowEditButtons.get(idx).click();
        } else {
            throw new RuntimeException("not enough rows for " + idx);
        }

    }
}
