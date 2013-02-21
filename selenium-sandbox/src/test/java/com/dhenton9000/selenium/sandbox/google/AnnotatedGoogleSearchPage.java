/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.google;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author dhenton
 */
public class AnnotatedGoogleSearchPage {
    	protected WebDriver driver;
//selenium.type("id=gbqfq", "cats");
//		selenium.click("id=gbqfb");	
	@FindBy(name="q")
        //@FindBy(id="gbqfq")
	private WebElement searchField;	

	@FindBy(name="btnG")
        //@FindBy(id="gbqfb")
	private WebElement searchButton;

	public AnnotatedGoogleSearchPage(WebDriver driver) {
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
		searchField.sendKeys(searchTerm);
		searchButton.click();
	}

	public void typeSearchTerm(String searchTerm) {
		searchField.sendKeys(searchTerm);
	}
	
	public void clickOnSearch() {
		searchButton.click();
	}
}
