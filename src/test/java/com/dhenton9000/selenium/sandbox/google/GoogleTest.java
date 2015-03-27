/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.google;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import static org.junit.Assert.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * http://weblogs.java.net/blog/johnsmart/archive/2010/08/09/selenium-2web-driver-land-where-page-objects-are-king
 * @author dhenton
 */
 


public class GoogleTest {

	private AnnotatedGoogleSearchPage page;
	
	@Before
	public void openTheBrowser() {
		page = PageFactory.initElements(new HtmlUnitDriver(), AnnotatedGoogleSearchPage.class);
		page.open("http://google.co.nz/");
             
	}

	@After
	public void closeTheBrowser() {
		page.close();
	}

	@Test
	public void whenTheUserSearchesForCatsTheResultPageTitleShouldContainCats() {
                
		page.searchFor("cats");
               // System.out.println(page.getTitle().toUpperCase());
                assertTrue(page.getTitle().toUpperCase().indexOf("CATS")> -1 );
	}	
}
