/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class JSMethods {

    private final WebDriver driver;
    private Logger LOG = LoggerFactory.getLogger(JSMethods.class);
    private final GenericAutomationRepository repository;

    public JSMethods(GenericAutomationRepository repos) {
        this.repository = repos ;
        this.driver = repos.getDriver();
    }

    
    
    public void contextMenu(String cssSelector, int xOffset, int yOffset)
    {
        WebElement we = getRepository().findElement(
                GenericAutomationRepository.SELECTOR_CHOICE.cssSelector, 
                cssSelector);
        xOffset += we.getLocation().x;
        yOffset += we.getLocation().y;
        
        
        String jsAction =              
           "var sClick = document.createEvent('MouseEvents'); "
           + "sClick.initMouseEvent('contextmenu', true, true, $(arguments[0])[0].defaultView, 1, "
           + "arguments[1], arguments[2], arguments[1], arguments[2], "
           + "false, false, false, 0, null, null); "
           + "$(arguments[0])[0].dispatchEvent(sClick); ";

        ((JavascriptExecutor) driver).executeScript(jsAction, cssSelector,xOffset,yOffset );
        
        
    }        
            
    
    
    public void loadScriptFile(String pathToFile) throws IOException {
 
        String scriptBody = "";
        String scriptRunner = "var demo_script = document.createElement(\"script\"); "
                + "var demo_head = document.getElementsByTagName(\"head\")[0]; ";
        scriptRunner += "$(demo_script).append("+scriptBody+"); demo_head.appendChild(demo_script);";
       // scriptRunner += "$(demo_script).append("+scriptBody+"); head_demo.appendChild(script_demo)";
        
        LOG.debug(scriptRunner);
        // give jQuery time to load asynchronously
        getDriver().manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(scriptRunner);
    }

    private String readFileFromClassPath(String pathToFile) throws IOException {
        Charset cs = Charset.forName("UTF-8");
        LOG.debug("reading js file '"+pathToFile+"'");
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(pathToFile);
        try {
            Reader reader = new BufferedReader(new InputStreamReader(stream, cs));
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[8192];
            int read;
            while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, read);
            }
            return builder.toString();
        } finally {
            stream.close();
        }
    }

    /**
     * @return the driver
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * @return the repository
     */
    public GenericAutomationRepository getRepository() {
        return repository;
    }

}
