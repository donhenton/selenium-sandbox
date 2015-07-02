/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.testng.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 *
 * @author dhenton
 */
public class TestingListener extends TestListenerAdapter {

    private Logger LOG = LoggerFactory.getLogger(TestingListener.class);

 
     

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        if (testContext.getAllTestMethods().length > 0) {
           LOG.info(testContext.getAllTestMethods()[0]
                .getRealClass().getSimpleName() + " finished");
        } else {
            LOG.info("no tests found in finish "+testContext.getName());
        }
        
    }

   
    @Override
    public void onConfigurationFailure(ITestResult itr) {
        super.onConfigurationFailure(itr);
        
        LOG.warn("configuration failure for "+ getTestDescription(itr));
    }

   
    @Override
    public void onStart(ITestContext testContext) {
        super.onStart(testContext);
        if (testContext.getAllTestMethods().length > 0) {
            LOG.info(testContext.getAllTestMethods()[0]
                    .getRealClass().getSimpleName() + " start");
        } else {
            LOG.info("no tests found in start "+testContext.getName());
        }

    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr); 
        
        
    }

  

    /**
     * @param result
     */
    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);

    
        LOG.info(getTestDescription(result) + " started ");

        

    }

    /**
     * @param result
     */
    @Override
    public void onTestFailure(ITestResult result) {
        LOG.info(getTestDescription(result) + " failed ");
        super.onTestFailure(result);
        
    }

    private static String getTestDescription(ITestResult result) {
        return "test " + result.getTestName() + "-->"
                + result.getMethod().getMethodName();
    }

    /**
     * @param result
     */
    @Override
    public void onTestSuccess(ITestResult result) {

        LOG.info(getTestDescription(result) + " successful ");
        super.onTestSuccess(result);
        

    }

   

}
