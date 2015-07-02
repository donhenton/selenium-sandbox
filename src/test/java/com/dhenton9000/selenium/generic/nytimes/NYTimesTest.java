/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic.nytimes;

 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;

/**
 *
 * @author dhenton
 */
public class NYTimesTest extends NYTimesBase {
    private Logger LOG = LoggerFactory.getLogger(NYTimesTest.class);

    @Test
    public void testGoToNYTimes() {
         assertNotNull(this.getNYTimesRepository());
        this.getNYTimesRepository().initialNavigation();
        

   
        
        
        
    }
}
