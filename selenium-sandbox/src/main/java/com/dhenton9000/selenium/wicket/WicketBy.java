/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.wicket;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

/**
 * http://www.wijsmullerbros.nl/content/wicket-selenium
 *
 * @author dhenton
 */
public class WicketBy extends By {

    private final String xpathExpression;
    private final String wicketPath;

    /**
     * Creates a new instance of {@link WicketBy}.
     *
     * @param wicketPath the wicket path (eg: "id1:id2:id3")
     * @param exactMatch if true the path must match exactly 
     * otherwise the path just has to contain the string
     * 
     */
    private WicketBy(String wicketPath,boolean matchExact) {
        this.wicketPath = wicketPath;
        this.xpathExpression = convert(wicketPath,matchExact);
        ////*[starts-with(@class,"atag")]
    }


    public static By wicketPathMatch(String wicketPath) {
        return new WicketBy(wicketPath,true);
    }
    
    public static By wicketPath(String wicketPath)
    {
        return wicketPathMatch(wicketPath);
    }

    public static By wicketPathContains(String wicketPath)
    {
        return new WicketBy(wicketPath,false);
    }        
    
    private String convert(String wicketPath,boolean matchExact) {
        String renderedPathVal = wicketPath.replaceAll(":", "_");
        String matchVariable = "//*[@wicketpath='%s']";
        if (!matchExact)
        {
            matchVariable = "//*[contains(@wicketpath,'%s')]";
        }
        
        return String.format(matchVariable, renderedPathVal);
    }

 
    @Override
    public String toString() {
        return "By.wicketPath: " + wicketPath + " (xpath: " + xpathExpression + ")";
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
       return ((FindsByXPath) context).findElementsByXPath(xpathExpression);
    }
    
     @Override
    public WebElement findElement(SearchContext context) {
        return ((FindsByXPath) context).findElementByXPath(xpathExpression);
    }
}