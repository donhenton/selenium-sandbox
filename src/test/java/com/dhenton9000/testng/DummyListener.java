/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 *
 * @author dhenton
 */
public class DummyListener extends TestListenerAdapter {

    private Logger LOG = LoggerFactory.getLogger(DummyListener.class);

//    @Override
//    public void onConfigurationFailure(ITestResult itr) {
//        super.onConfigurationFailure(itr);  
//        LOG.debug("in config failure");
//    }
//    
//    @Override
//    public void onConfigurationSuccess(ITestResult itr) {
//        super.onConfigurationFailure(itr);  
//        LOG.debug("in config success");
//    }
    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        LOG.debug("on test start");
    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        if (testContext.getAllTestMethods().length > 0) {
            LOG.debug("on finish class " + 
                    testContext.getAllTestMethods()[0]
                    .getInstance().getClass().getName());
        }
        else
        {
            LOG.info("no methods found for "+testContext.getCurrentXmlTest().getName());
        }
        LOG.debug("on finish");
    }

    /**
     * occurs before the @BeforeClass call of testNg
     *
     * @param testContext
     */
    @Override
    public void onStart(ITestContext testContext) {
        super.onStart(testContext);
        LOG.debug("on start");
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        LOG.debug("on test failure");
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        LOG.debug("on test success");
    }

}
