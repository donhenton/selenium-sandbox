/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


/**    
 * 
 * demonstration test. Shows the call position of the listener and
 * the functions in the ng test
 *
 * @author dhenton
 */
@Listeners({DummyListener.class})
public class DummyListenerTest  {

    private Logger LOG = LoggerFactory.getLogger(DummyListenerTest.class);

    @BeforeClass
    public void beforeClass() {
        LOG.debug("beforeClass of test");
       
    }

    @BeforeMethod
    public void beforeTest() {
        LOG.debug("beforemethod of test");
    
    }

    @AfterMethod
    public void afterTest() {
        LOG.debug("aftermethod of test");

    }

    @Test

    public void testShouldSucceedOne() {
        assertTrue(true);
    }
    
     @Test

    public void testShouldFail() {
        fail("fail One");
    }

}
