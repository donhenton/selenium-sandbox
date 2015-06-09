/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.js;

 

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;




/**
 * Loads and runs javascript libraries. Taken directly 
 * from old selenium rc code.
 * 
 * This serves as an example of loading a large javascript library
 * without placing it on the server
 * 
 * Included in this package is the js code for injectableSelenium
 * the jar this was taken from is:
 * selenium-leg-rc-2.45.0.jar
 * com.thoughtworks.selenium.webdriven
 * 
 * @author dhenton
 */
public class JavascriptLibrary {

  static final String PREFIX = "/" + JavascriptLibrary.class.getPackage()
      .getName().replace(".", "/") + "/";
  private final ConcurrentHashMap<String, String> scripts = new ConcurrentHashMap<>();

  private static final String injectableSelenium =
      "/com/dhenton9000/selenium/js/injectableSelenium.js";
  private static final String htmlUtils =
      "/com/thoughtworks/selenium/webdriven/htmlutils.js";

  /**
   * Loads the named Selenium script and returns it wrapped in an anonymous function.
   *
   * @param name The script to load.
   * @return The loaded script wrapped in an anonymous function.
   */
  public String getSeleniumScript(String name) {
    String rawFunction = readScript(PREFIX + name);

    return String.format("function() { return (%s).apply(null, arguments);}",
                         rawFunction);
  }

  public void callEmbeddedSelenium(WebDriver driver, String functionName,
                                   WebElement element, Object... values) {
    StringBuilder builder = new StringBuilder(readScript(injectableSelenium));
    builder.append("return browserbot.").append(functionName)
        .append(".apply(browserbot, arguments);");

    List<Object> args = new ArrayList<Object>();
    args.add(element);
    args.addAll(Arrays.asList(values));

    ((JavascriptExecutor) driver).executeScript(builder.toString(), args.toArray());
  }

  public Object callEmbeddedHtmlUtils(WebDriver driver, String functionName, WebElement element,
                                      Object... values) {
    StringBuilder builder = new StringBuilder(readScript(htmlUtils));

    builder.append("return htmlutils.").append(functionName)
        .append(".apply(htmlutils, arguments);");

    List<Object> args = new ArrayList<Object>();
    args.add(element);
    args.addAll(Arrays.asList(values));

    return ((JavascriptExecutor) driver).executeScript(builder.toString(), args.toArray());
  }

  public Object executeScript(WebDriver driver, String script, Object... args) {
    if (driver instanceof JavascriptExecutor) {
      return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    throw new UnsupportedOperationException(
        "The underlying WebDriver instance does not support executing javascript");
  }

  private String readScript(String script) {
    String result = scripts.get(script);
    if (result == null) {
      result = readScriptImpl(script);
      scripts.put(script, result);
    }
    return result;
  }

  String readScriptImpl(String script) {
    URL url = getClass().getResource(script);

    if (url == null) {
      throw new RuntimeException("Cannot locate " + script);
    }

    try {
      return Resources.toString(url, Charsets.UTF_8);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }
}
