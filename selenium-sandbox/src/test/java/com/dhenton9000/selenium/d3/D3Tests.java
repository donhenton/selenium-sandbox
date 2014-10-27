/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.d3;

import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import com.dhenton9000.selenium.generic.GenericTestBase;
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
import java.util.ArrayList;
import org.junit.Ignore;
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
    
    @Ignore
    public void leftClickDemo()
    {
        
        gotoD3Page();
        String template = "d3.selectAll('svg g[data-id=\"%d\"]')"
                + ".each("
                + "function(d,i) { var f = d3.select(this).on(\"click\");  "
                + "f.apply(this,[d],i)})";
        String nodeClick = String.format(template,4);
        getAutomation().getJavascriptExecutor().executeScript(nodeClick);
        
        
    }
    
    @Test
    public void testContextMenu()
    {
          gotoD3Page();
          String selector = "svg g[data-id=\"4\"]";
          this.getAutomation().getJsMethods().contextMenu(selector, 4,4);
          
         
          
          
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