/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.angular;

 
import com.dhenton9000.selenium.drivers.DriverFactory;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 *
 * @author dhenton
 */
public class AngularTests extends AngularTestBase {
    private static final Logger LOG = LoggerFactory.getLogger(AngularTests.class);
    
    
    
    @BeforeClass
    public  void beforeClass()
    {
         this.getRestaurantRepository().navigateToRestaurantApp();
         this.getRestaurantRepository().getAutomation().maximizeWindow();
    }
    
    
    @BeforeMethod
    public void beforeTest()
    {
       
    }
    
    @Test
    public void testGetRestaurants()
    {
        this.setCloseDriver(false);
         RestaurantCollection restaurantCollection = 
            new RestaurantCollection(this.getAutomationRepository());
         LOG.debug("begin load table");
         restaurantCollection.loadTable();
         LOG.debug("end load table");
         assertEquals(restaurantCollection.getRestaurants().get(0).getName(),"A1 Ocean Cafe #22");
         assertTrue(restaurantCollection.getRestaurants().size()>40);
         assertEquals(this.getRestaurantRepository().getReviewsForCurrentRestaurant().size(),0);
         restaurantCollection.getRestaurants().get(3).getEditButton().click();
         assertEquals(this.getRestaurantRepository().getReviewsForCurrentRestaurant().size(),2);
        
    }
    
    @Test
    public void testFromMaven()
    {
        assertNotNull(DriverFactory.getRemoteServerValue());
        assertEquals(DriverFactory.getTestENV(),"alpha");
    }
    
}
