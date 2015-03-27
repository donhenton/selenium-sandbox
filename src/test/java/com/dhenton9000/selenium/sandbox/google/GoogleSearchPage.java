/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.google;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class GoogleSearchPage {
        
        private final Logger logger = LoggerFactory.getLogger(GoogleSearchPage.class);
	protected WebDriver driver;
	private WebElement q = null;	
	private WebElement btnG = null;

	public GoogleSearchPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void open(String url) {
		driver.get(url);
	}

	public void close() {
		driver.quit();
	}
	
	public String getTitle() {
		return driver.getTitle();
	}

	public void searchFor(String searchTerm) {
		getQ().sendKeys(searchTerm);
		getBtnG().click();
	}

	public void typeSearchTerm(String searchTerm) {
		getQ().sendKeys(searchTerm);
	}
	
	public void clickOnSearch() {
		getBtnG().click();
	}

    /**
     * @return the q
     */
    public WebElement getQ() {
        return q;
    }

    /**
     * @return the btnG
     */
    public WebElement getBtnG() {
        return btnG;
    }
}