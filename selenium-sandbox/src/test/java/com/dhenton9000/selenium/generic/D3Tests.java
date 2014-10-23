/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.dhenton9000.selenium.generic.GenericAutomationRepository.*;
/**
 *
 * @author dhenton
 */
public class D3Tests extends GenericTestBase {
    
    
    private static final Logger LOG = LoggerFactory.getLogger(D3Tests.class);
    
    public D3Tests()
    {
        /**
         * window will stay open and driver not close
         * if set to false
         */
        this.setCloseDriver(false);
        
    }
    
    @Before
    public void beforeTest()
    {
        assertNotNull(this.getAppspotRepository());
        this.getAppspotRepository().initialNavigation();
        
        
    }
    
    @Test
    public void testD3()
    {
        assertTrue(true);
        //this.getAutomation().findElement(GenericAutomationRepository.SELECTOR_CHOICE.cssSelector, null);
        //$('#menu ul li ul li ul li a[href$="treeDemo"]')
        gotoD3Page();
    }
    
    
     private void gotoD3Page() {
         
        this.getAutomation().maximizeWindow();
         
        WebElement jQueryDemoLink = this.getAutomation().findElement(
                GenericAutomationRepository.SELECTOR_CHOICE.partialLinkText, 
                "Backbone and D3");
        jQueryDemoLink.click();

       

        this.getAutomation().getWaitMethods().waitForElementToBeClickable(2,
                SELECTOR_CHOICE.linkText, "D3 Demos");
        
        WebElement d3Hover = getAutomation()
                .findElement( SELECTOR_CHOICE.linkText, "D3 Demos");
         
        assertNotNull(d3Hover);
        assertEquals("D3 Demos",d3Hover.getText());
        this.getAutomation().hoverOn(d3Hover);
        
        this.getAutomation().getWaitMethods().waitForElementToBeClickable(2,
                SELECTOR_CHOICE.linkText, "Tree Demo"); 
        
        this.getAutomation().findElement(
                SELECTOR_CHOICE.linkText, "Tree Demo").click(); 
        
    }
}
// to left click on a node
//d3.selectAll('svg g[data-id="4"]').each(function(d,i) { var f = d3.select(this).on("click");  f.apply(this,[d],i)})


//to get a context menu
//var clickEvent = document.createEvent('MouseEvents');
//clickEvent.initMouseEvent(............ )
//d3.selectAll('svg g[data-id="4"]').dispatchEvent(clickEvent)

//https://github.com/mbostock/d3/issues/100