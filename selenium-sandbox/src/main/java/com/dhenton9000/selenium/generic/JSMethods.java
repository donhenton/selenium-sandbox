/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class JSMethods {

    private final WebDriver driver;
    private Logger LOG = LoggerFactory.getLogger(JSMethods.class);
    private final GenericAutomationRepository repository;

    public JSMethods(GenericAutomationRepository repos) {
        this.repository = repos;
        this.driver = repos.getDriver();
    }

    /**
     * get the position of an object using jquery, may be different
     * from the information provided by WebElement
     * 
     * This will not work for d3 elements as it won't take into
     * account any transforms 
     * 
     * http://stackoverflow.com/questions/19154631/how-to-get-coordinates-of-an-svg-element
     * 
     * Note: requires JQuery present
     * s
     * @param cssSelector
     * @return 
     */
    public ElementDimension getElementDimensions(String cssSelector) {
        String dimensionFinder
                = "return [$(arguments[0]).offset().left,$(arguments[0]).offset().top, "
                + "$(arguments[0]).width(),$(arguments[0]).height()] ;";
        ArrayList<Number> ret = (ArrayList<Number>) ((JavascriptExecutor) 
                driver).executeScript(dimensionFinder, cssSelector);
        return new ElementDimension(ret);
    }

    public class ElementDimension {

        public int left;
        public int top;
        public int width;
        public int height;

        private ElementDimension(ArrayList<Number> ret) {
            left = ret.get(0).intValue();
            top = ret.get(1).intValue();
            width = ret.get(2).intValue();
            height = ret.get(3).intValue();
        }
    }

    /**
     * perform a right click via javascript. Note that this is not a true
     * mouse action, only calling the code, therefore a simulation. providing
     * the location of the mouse click is the problem.
     * 
     * The click position is relative to the window, so it is up to the calling
     * program to figure out how to position the call.
     * 
     * require JQuery
     * 
     * @param cssSelector valid jquery selector
     * @param xOffSet arbitrary offset of the click
     * @param yOffset arbitrary offset of the click
     */
    public void contextMenu(String cssSelector, int xOffSet, int yOffset) {

        String jsAction
                = "var sClick = document.createEvent('MouseEvents'); "
                + "sClick.initMouseEvent('contextmenu', true, true, $(arguments[0])[0].defaultView, 1, "
                + "arguments[1], arguments[2], arguments[1], arguments[2], "
                + "false, false, false, 0, null, null); "
                + "$(arguments[0])[0].dispatchEvent(sClick); ";

        ((JavascriptExecutor) driver).executeScript(jsAction, cssSelector, xOffSet, yOffset);

    }

    /**
     * @return the driver
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * @return the repository
     */
    public GenericAutomationRepository getRepository() {
        return repository;
    }

}
