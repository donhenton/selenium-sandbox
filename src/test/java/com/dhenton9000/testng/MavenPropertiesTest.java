/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.testng;

import com.dhenton9000.testng.listeners.TestingListener;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.ITest;
import org.testng.annotations.Listeners;

/**
 * This test must be run from the mvn command line
 * mvn clean test -PtestNg
 * @author dhenton
 */
@Listeners({TestingListener.class})
public class MavenPropertiesTest implements ITest {

    private Logger LOG = LoggerFactory.getLogger(DummyListenerTest.class);
    private PropertiesConfiguration config;

    @BeforeClass
    public void beforeClass() {
        LOG.debug("beforeClass of test");
        try {
            config = new PropertiesConfiguration("dep.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("config error "+e.getMessage());
        }
    }
    
    @Test
    public void testEnvVars()
    {
        String systemEnv = System.getProperty("test.env");
        String propEnv = config.getString("test.env", null);
        assertEquals(propEnv,systemEnv);
        assertNotNull(propEnv);
        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("alpha");
        testArray.add("beta");
        assertTrue(testArray.contains(propEnv));
        
        
    }

    @Override
    public String getTestName() {
        return this.getClass().getSimpleName();
    }

}
