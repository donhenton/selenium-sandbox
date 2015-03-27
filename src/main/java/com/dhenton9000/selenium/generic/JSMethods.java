/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;

import java.util.ArrayList;
import java.util.Arrays;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
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
     * all selectors must use \" for strings
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
     * perform a left click for a D3 node, which doesn't respond to 
     * WebElement click
     * 
     * all selectors must use \" for strings
     * 
     * @param cssSelector valid cssSelector
     * Note: d3 must be present
     */
    public void leftClickForD3(String cssSelector)
    {
        String template = "d3.selectAll('%s')"
                + ".each("
                + "function(d,i) { var f = d3.select(this).on(\"click\");  "
                + "f.apply(this,[d],i)})";
        String nodeClick = String.format(template,cssSelector);
        getRepository().getJavascriptExecutor().executeScript(nodeClick);
    }
    
    /**
     * perform a right click via javascript. Note that this is not a true
     * mouse action, only calling the code, therefore a simulation. providing
     * the location of the mouse click is the problem.
     * 
     * The click position is relative to the window, so it is up to the calling
     * program to figure out how to position the call.
     * 
     * all selectors must use \" for strings
     * 
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

        getRepository().getJavascriptExecutor().executeScript(jsAction, cssSelector, xOffSet, yOffset);

    }
    
    public void mouseOver(String cssSelector, int xOffSet, int yOffset) {

        String jsAction
                = "var sClick = document.createEvent('MouseEvents'); "
                + "sClick.initMouseEvent('mouseover', true, true, $(arguments[0])[0].defaultView, 1, "
                + "arguments[1], arguments[2], arguments[1], arguments[2], "
                + "false, false, false, 0, null, null); "
                + "$(arguments[0])[0].dispatchEvent(sClick); ";

        getRepository().getJavascriptExecutor().executeScript(jsAction, cssSelector, xOffSet, yOffset);

    }

    /**
     * send text and the <ENTER> key to a text box via javascript.
     * Useful for triggering keydown, keypress events that look for the
     * enter key. If these are inside a form and that code supresses the form
     * submission, this method should honor that supression.
     * 
     * @param bodyText the text to enter
     * @param event 'keydown' or 'keypress'
     * @param cssSelector the selector for the input box, the selector should
     * not contain single quotes
     */
    public void enterTextAndPressEnter(String bodyText, String event,String cssSelector)
    {
        String[] events = {"keypress","keydown"};
        if (!Arrays.asList(events).contains(event))
        {
            throw new RuntimeException("event is not accepted '"+event+"' ");
        }
        
        String jsAction = String.format("var targetSelector = $('%s'); ",cssSelector);
        //send the body text to the input box
        jsAction += String.format("targetSelector.val('%s'); ",bodyText);
        //define the event 
        jsAction += String.format("var e =$.Event('%s'); e.keyCode = 13 ",event);  
        //trigger it
        jsAction += "targetSelector.trigger(e); ";
        
        getRepository().getJavascriptExecutor().executeScript(jsAction);
        
        
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
