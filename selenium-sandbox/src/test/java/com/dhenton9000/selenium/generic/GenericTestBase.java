/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;
 
 
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

/**
 *
 * @author dhenton
 */
public class GenericTestBase {

    private   GenericRepository genericRepository = null;
    private static final Logger LOG = LoggerFactory.getLogger(GenericTestBase.class);

    public GenericTestBase() {
        Configuration config = null;
        LOG.debug("using properties file");
        try {
            config = new PropertiesConfiguration("env.properties");
            LOG.debug("reading config in " + this.getClass().getName());
        } catch (ConfigurationException ex) {
            LOG.info("did not find env.properties file");
        }

        genericRepository = new GenericRepository(config);
    }

    public GenericTestBase(Configuration config) {

        genericRepository = new GenericRepository(config);
    }

    /**
     * @return the genericRepository
     */
    public GenericRepository getGenericRepository() {
        return genericRepository;
    }
    
    public GenericAutomationRepository getAutomation()
    {
        return genericRepository.getAutomation();
    }

    

    @After 
    public void teardownTest () {
      LOG.info("quitting driver");
       getGenericRepository().getAutomation().quitDriver();
    }
}
