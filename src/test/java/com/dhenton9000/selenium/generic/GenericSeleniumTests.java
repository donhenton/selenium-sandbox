/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;

import java.util.List;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class GenericSeleniumTests extends AppspotTestBase {

    private Logger LOG = LoggerFactory.getLogger(GenericSeleniumTests.class);

    @Test
    public void testSimple() {
         assertNotNull(this.getAppspotRepository());
        this.getAppspotRepository().initialNavigation();
        

   
        
        
        
    }
    
    
      // Select a group of elements by complicated css selector
      // this is all the inputs in a table
    
      //WebElement inputElem = 
      //  getAutomation().findElements(SELECTOR_CHOICE.cssSelector, "table.cssTableCass tbody tr td input").get(0);

      // this can also be done via  jquery where filters can be used
      /*
         List<WebElement> inputSample = (List<WebElement>) this.getAppspotRepository()
                .getAutomation()
                .getJavascriptExecutor()
                .executeScript(
                        "return  $('table.cssTableCass tbody tr td input').get( )");
         
        // this uses a filter in jquery
    
        List<WebElement> things = (List<WebElement>) this.getAdminRepository()
//                .getAutomation()
//                .getJavascriptExecutor()
//                .executeScript(
//                        "return $('#slpha div#beta div.controlItem').children().filter(function(){ return $(this).css('display')!='none'}).get()");
// 
    
      
    
    
     */    
     // note the use of return and .get() at the end which converts to HTMLElements
         

    
}
