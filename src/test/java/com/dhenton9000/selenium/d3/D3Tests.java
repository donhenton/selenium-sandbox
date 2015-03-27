/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.d3;

import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import com.dhenton9000.selenium.generic.AppspotTestBase;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.dhenton9000.selenium.generic.GenericAutomationRepository.*;
import com.dhenton9000.selenium.generic.JSMethods.ElementDimension;
import java.util.List;
import org.junit.Ignore;
import org.openqa.selenium.NoSuchElementException;

/**
 *
 * @author dhenton
 */
public class D3Tests extends AppspotTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(D3Tests.class);

    public D3Tests() {
        /**
         * window will stay open and driver not close if set to false
         */
       // this.setCloseDriver(false);

    }

    @Before
    public void beforeTest() {

    }

    @Test
    public void testMouseMove() {

        // using github project MouseEvent-sandbox
        // server must be running
        this.getAutomation().getDriver().get("http://localhost:8383/MouseEvent-sandbox/index.html");
        String cssSelector = "div#hoverItem";
        String testSelector = "div.tooltip";
        boolean notThere = true;
        try {
            getAutomation().findElement(
                    SELECTOR_CHOICE.cssSelector, testSelector);
        } catch (NoSuchElementException err) {
            notThere = false;
        }

        assertFalse(notThere);

        ElementDimension dim
                = this.getAutomation().getJsMethods().getElementDimensions(cssSelector);

        this.getAutomation().getJsMethods().mouseOver(cssSelector, 10 + dim.left, 10 + dim.top);
        WebElement w =   null;
        try {
           w =   getAutomation().findElement(
                    SELECTOR_CHOICE.cssSelector, testSelector);
           
        } catch (NoSuchElementException err) {
             
        }
        assertNotNull(w);
    }

    @Test
    public void d3LeftClickDemo() {
        this.getAppspotRepository().initialNavigation();
        gotoD3Page();

        List<WebElement> gElements
                = getAutomation().findElements(
                        SELECTOR_CHOICE.cssSelector, "svg circle");
        int startCount = gElements.size();
        String selector = "svg g[data-id=\"4\"]";

        this.getAutomation().getJsMethods().leftClickForD3(selector);

        gElements
                = getAutomation().findElements(
                        SELECTOR_CHOICE.cssSelector, "svg circle");
        int endCount = gElements.size();
        assertEquals(5, startCount);
        assertTrue(endCount > startCount);
    }

    @Test
    /**
     * note that this does not actually click on the point but needs to compute
     * the whole graph transform
     */
    public void testContextMenu() {
        this.getAppspotRepository().initialNavigation();
        gotoD3Page();
        String cssSelector = "svg g[data-id=\"4\"]";
        int x = 0, y = 0;
        //check that custom menu is not visible
        WebElement customMenu = getAutomation().findElement(SELECTOR_CHOICE.id, "my_custom_menu");
        assertFalse(customMenu.isDisplayed());
        ElementDimension dim
                = this.getAutomation().getJsMethods().getElementDimensions(cssSelector);
        this.getAutomation().getJsMethods().contextMenu(cssSelector, x + dim.left, y + dim.top);
        customMenu = getAutomation().findElement(SELECTOR_CHOICE.id, "my_custom_menu");
        assertTrue(customMenu.isDisplayed());

    }

    private void gotoD3Page() {

        this.getAutomation().maximizeWindow();

        WebElement backboneD3Link = this.getAutomation().findElement(
                GenericAutomationRepository.SELECTOR_CHOICE.partialLinkText,
                "Backbone and D3");
        backboneD3Link.click();

        this.getAutomation().getWaitMethods().waitForElementToBeClickable(2,
                SELECTOR_CHOICE.linkText, "D3 Demos");

        WebElement d3Hover = getAutomation()
                .findElement(SELECTOR_CHOICE.linkText, "D3 Demos");

        assertNotNull(d3Hover);
        assertEquals("D3 Demos", d3Hover.getText());
        this.getAutomation().hoverOn(d3Hover);

        this.getAutomation().getWaitMethods().waitForElementToBeClickable(2,
                SELECTOR_CHOICE.linkText, "Tree Demo");

        this.getAutomation().findElement(
                SELECTOR_CHOICE.linkText, "Tree Demo").click();

    }
}
