/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshots;

import com.dhenton9000.screenshots.ConfigurationManager.PAGE_ACTION;
import com.dhenton9000.screenshots.navigator.ScreenShotNavigator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author dhenton
 */
public class ScreenShot {

    private String fileDescription;
    private PAGE_ACTION pageAction;
    private ScreenShotNavigator navigator;
    private static final Logger LOG = LoggerFactory.getLogger(ScreenShot.class);
    public static final String IMAGE_EXTENSION = ".png";

    public ScreenShot(Node item) {

        NodeList childNodes = item.getChildNodes();
        if (childNodes == null || item.getChildNodes().getLength() == 0) {
            throw new RuntimeException("cannot find screen node info");
        }
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node n = childNodes.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                String value = n.getFirstChild().getNodeValue();
                String name = n.getNodeName();
                LOG.debug(String.format("name '%s' value '%s'",
                        name, value));

                if (name.equals("fileDesc")) {
                    fileDescription = value;
                } else if (name.equals("pageAction")) {
                    pageAction = PAGE_ACTION.valueOf(value);
                }

            }// end if element
        }

    }

    /**
     * @return the fileDescription, which is the simple file name without
     * an extensions eg ('scr1')
     */
    public String getFileDescription() {
        return fileDescription;
    }

    /**
     * @return the pageAction see {@link 
     * com.dhenton9000.screenshots.ConfigurationManager.PAGE_ACTION}
     */
    public PAGE_ACTION getPageAction() {
        return pageAction;
    }

    /**
     * return the reference to the piece of code that navigates and sets
     * up the screenshot
     * @return the navigator
     */
    public ScreenShotNavigator getNavigator() {
        return navigator;
    }

    /**
     * @param navigator the navigator to set
     */
    public void setNavigator(ScreenShotNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public String toString() {
        return "ScreenShot{" + "fileDescription=" + fileDescription + ", pageAction=" + pageAction + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.fileDescription != null ? this.fileDescription.hashCode() : 0);
        hash = 71 * hash + (this.pageAction != null ? this.pageAction.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScreenShot other = (ScreenShot) obj;
        if ((this.fileDescription == null) ? (other.fileDescription != null) :
                !this.fileDescription.equals(other.fileDescription)) {
            return false;
        }
        if (this.pageAction != other.pageAction) {
            return false;
        }
        return true;
    }

}
