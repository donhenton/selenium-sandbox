/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.testng;

import com.dhenton9000.embedded.jetty.JettyServer;
import com.dhenton9000.testng.listeners.TestingListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import static org.junit.Assert.assertNotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 *
 * demonstration test. Shows the call position of the listener and the functions
 * in the ng test
 *
 * @author dhenton
 */
@Listeners({TestingListener.class})
public class TestNgSeleniumTests   {

    private Logger LOG = LoggerFactory.getLogger(TestNgSeleniumTests.class);
     private static final int PORT = 4444;
    private static final String CONTEXT_PATH = "/app";
    
    private static JettyServer localWebServer;
    private static final String APP_URL = "http://localhost:" 
            + PORT + CONTEXT_PATH;

    @BeforeClass
    public void beforeClass() throws Exception {
        localWebServer = new JettyServer(PORT, CONTEXT_PATH,null);

    }

    @AfterClass
    public void afterClass() throws Exception {
       localWebServer.stopJettyServer();

    }

    

    @Test
    public void testShouldSucceedOne() throws IOException {
       HttpClient client = HttpClientBuilder.create().build();
        HttpGet mockRequest = new HttpGet(APP_URL);

        HttpResponse mockResponse = client.execute(mockRequest);
        BufferedReader rd = new BufferedReader(new 
        InputStreamReader(mockResponse.getEntity().getContent()));
        String theString = IOUtils.toString(rd);
        assertNotNull(theString);
        LOG.debug(theString);
        assertTrue(theString.contains("Hello World"));
    }

     

}
