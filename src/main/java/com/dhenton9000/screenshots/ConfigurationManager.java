/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshots;

import com.dhenton9000.screenshots.navigator.ScreenShotNavigator;
import com.dhenton9000.screenshots.navigator.MainPageNavigator;
import com.dhenton9000.utils.xml.XMLUtils;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author dhenton
 */
public class ConfigurationManager {

    private static final String SCREENSHOT_CONFIG_PROPERTY = "screenshot.config";
    //this is the standard properties file for regressions
    private String appPropertiesKey = "env.properties";
    private Configuration testPropertiesConfig = null;
    Document screenShotDoc = null;
    private final String sourceFolder;
    private final String compareFolder;
    private final String targetFolder;
    private final String userName;
    private final String password;
    private final String environment;
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ConfigurationManager.class);
    private List<ScreenShot> screenShotList = new ArrayList<ScreenShot>();

     /**
     * Key for which page to navigate to. The navigation will be by code, and
     * this enum value is a descriptive key for each screen shot, this points to
     * code that navigates and sets up the screenshot, eg "mainpage" or
     * "customer_page"
     *
     */
    public enum PAGE_ACTION {
        mainPage
    }

    public ConfigurationManager() {

        String screenShotConfigFileLocation = null;
        screenShotConfigFileLocation = getTestPropertiesConfig().getString(SCREENSHOT_CONFIG_PROPERTY);
        if (screenShotConfigFileLocation == null) {
            throw new RuntimeException(
                    "Must specify screenshot config location property '"
                    + SCREENSHOT_CONFIG_PROPERTY + "' in test.properties");
        }

        try {

            screenShotDoc = XMLUtils.fileToDoc(screenShotConfigFileLocation);
            screenShotDoc.normalizeDocument();
        } catch (Exception err) {
            throw new RuntimeException(err);
        }

        this.sourceFolder = screenShotDoc.getDocumentElement().getAttribute("sourceFolder");
        this.compareFolder = screenShotDoc.getDocumentElement().getAttribute("compareFolder");
        this.targetFolder = screenShotDoc.getDocumentElement().getAttribute("targetFolder");
        this.userName = screenShotDoc.getDocumentElement().getAttribute("userName");
        this.password = screenShotDoc.getDocumentElement().getAttribute("password");
        this.environment = screenShotDoc.getDocumentElement().getAttribute("environment");

        //load the screenshot list
        NodeList screenShotNodes = getChildNodes();

        //set up the screen shots
        for (int i = 0; i < screenShotNodes.getLength(); i++) {

            Node shotNode = screenShotNodes.item(i);
            if (shotNode.getNodeType() == Node.ELEMENT_NODE) {
                ScreenShot screenShot
                        = new ScreenShot(shotNode);
                screenShotList.add(screenShot);
                addScreenShotNavigation(screenShot);
            }

        }

    }

    public final List<ScreenShot> getScreenShotList() {

        return screenShotList;
    }

    /**
     * This is where navigation instructions are added. Add to this as new
     * screen shot requests are described. For example, a new case statement
     * would be added for a screen shot of the customer page, or the billing 
     * page
     *
     * A Navigator {@link ScreenShotNavigator} is a piece of code where complete
     * instructions exist to navigate to a given page and to prep that page for
     * the screen shot.
     *
     * @param screenShot
     */
    private void addScreenShotNavigation(ScreenShot screenShot) {

        ScreenShotNavigator navigator = null;
        if (screenShot == null || screenShot.getPageAction() == null) {
            throw new RuntimeException("null thing " + screenShot);

        }

        switch (screenShot.getPageAction()) {
            case mainPage:
                navigator = new MainPageNavigator();
                break;
 
            default:
                throw new RuntimeException("unable to find switch for '"
                        + screenShot.getPageAction() + "'");
        };

        screenShot.setNavigator(navigator);

    }

    /**
     * @return the testPropertiesConfig
     */
    public final Configuration getTestPropertiesConfig() {
        if (testPropertiesConfig == null) {

            try {
                testPropertiesConfig = new PropertiesConfiguration(appPropertiesKey);
            } catch (ConfigurationException ex) {
                throw new RuntimeException("Unable to find test.properties file");
            }

        }
        return testPropertiesConfig;
    }

    final NodeList getChildNodes() {
        return screenShotDoc.getDocumentElement().getChildNodes();
    }

    /**
     * @return the sourceFolder
     */
    public String getSourceFolder() {
        return sourceFolder;
    }

    /**
     * @return the compareFolder
     */
    public String getCompareFolder() {
        return compareFolder;
    }

    /**
     * @return the targetFolder
     */
    public String getTargetFolder() {
        return targetFolder;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the environment
     */
    public String getEnvironment() {
        return environment;
    }

}
