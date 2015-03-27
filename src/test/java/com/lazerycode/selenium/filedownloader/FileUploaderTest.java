/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lazerycode.selenium.filedownloader;

import com.dhenton9000.filedownloader.FileDownloader;
import com.dhenton9000.filedownloader.RequestMethod;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import com.lazerycode.selenium.JettyServer;
import java.io.File;
import java.net.URI;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class FileUploaderTest {

    private static final Logger LOG
            = LoggerFactory.getLogger(FileUploaderTest.class);
    private static JettyServer localWebServer;
    private static String webServerURL = "http://localhost";
    private static int webServerPort = 9081;
    private Configuration config;
    private GenericAutomationRepository automation;
    private WebDriver driver;
    private static URI downloadURI200;
    private static URI downloadURI404;

    public FileUploaderTest() {
        LOG.debug("using properties file");
        try {
            config = new PropertiesConfiguration("env.properties");
            LOG.debug("reading config in " + this.getClass().getName());
        } catch (ConfigurationException ex) {
            LOG.info("did not find env.properties file");

        }
        driver = new HtmlUnitDriver();
        this.automation = new GenericAutomationRepository(driver, config);

    }

    public GenericAutomationRepository getAutomation() {
        return automation;
    }

    @BeforeClass
    public static void start() throws Exception {
        localWebServer = new JettyServer(webServerPort,"/web");
        downloadURI200 = new URI(webServerURL + ":" + webServerPort + "/downloadTest.html");
        downloadURI404 = new URI(webServerURL + ":" + webServerPort + "/doesNotExist.html");

    }

    @AfterClass
    public static void stop() throws Exception {
        localWebServer.stopJettyServer();
    }

    @After
    public void closeWebDriver() {
        this.getAutomation().getDriver().close();
    }

    @Test
    public void testJettySetup() {

        this.getAutomation().getDriver().get(webServerURL + ":" + webServerPort + "/downloadTest.html");
        WebElement w = this.getAutomation().findElement(
                GenericAutomationRepository.SELECTOR_CHOICE.id,
                "fileToDownload");

        assertNotNull(w);
    }

    @Test
    public void statusCode200FromString() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(webServerURL + ":" + webServerPort + "/downloadTest.html");
        downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(200)));
    }

    @Test
    public void statusCode404FromString() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(webServerURL + ":" + webServerPort + "/doesNotExist.html");
        downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(404)));
    }

    @Test
    public void statusCode200FromURI() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(downloadURI200);
        downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(200)));
    }

    @Test
    public void statusCode404FromURI() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(downloadURI404);
        downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(404)));
    }

    @Test
    public void statusCode200FromURL() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(downloadURI200.toURL());
        downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(200)));
    }

    @Test
    public void statusCode404FromURL() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(downloadURI404.toURL());
        downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(404)));
    }

    @Test
    public void statusCode200FromURLUsingHead() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(downloadURI200.toURL());
        downloadHandler.setHTTPRequestMethod(RequestMethod.HEAD);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(200)));
    }

    @Test
    public void statusCode404FromURLUsingHead() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(downloadURI404.toURL());
        downloadHandler.setHTTPRequestMethod(RequestMethod.HEAD);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(404)));
    }

    @Test
    public void statusCode200FromURLUsingOptions() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(downloadURI200.toURL());
        downloadHandler.setHTTPRequestMethod(RequestMethod.OPTIONS);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(200)));
    }

    @Test
    public void statusCode200FromURLUsingPost() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(downloadURI200.toURL());
        downloadHandler.setHTTPRequestMethod(RequestMethod.POST);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(200)));
    }

    @Test
    public void statusCode405FromURLUsingPut() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(downloadURI200.toURL());
        downloadHandler.setHTTPRequestMethod(RequestMethod.PUT);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(405)));
    }

    @Test
    public void statusCode405FromURLUsingTrace() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(downloadURI200.toURL());
        downloadHandler.setHTTPRequestMethod(RequestMethod.TRACE);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(403)));
    }

    @Test
    public void statusCode405FromURLUsingDelete() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        downloadHandler.setURI(downloadURI200.toURL());
        downloadHandler.setHTTPRequestMethod(RequestMethod.DELETE);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(405)));
    }

    @Test
    public void downloadAFile() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        driver.get(webServerURL + ":" + webServerPort + "/downloadTest.html");
        WebElement downloadLink = driver.findElement(By.id("fileToDownload"));
        downloadHandler.setURISpecifiedInAnchorElement(downloadLink);
        File downloadedFile = downloadHandler.downloadFile();

        assertThat(downloadedFile.exists(), is(equalTo(true)));
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(200)));
    }

    @Test
    public void downloadAnImage() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(driver);
        driver.get(webServerURL + ":" + webServerPort + "/downloadTest.html");
        WebElement image = driver.findElement(By.id("ebselenImage"));
        downloadHandler.setURISpecifiedInImageElement(image);
        File downloadedFile = downloadHandler.downloadFile();

        assertThat(downloadedFile.exists(), is(equalTo(true)));
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(200)));
    }

    @Test
    public void downloadAFileFollowingRedirects() throws Exception {
        //TODO modify test page to set a redirect to file download
        FileDownloader downloadHandler = new FileDownloader(driver);
        driver.get(webServerURL + ":" + webServerPort + "/downloadTest.html");
        WebElement downloadLink = driver.findElement(By.id("fileToDownload"));
        downloadHandler.setURISpecifiedInAnchorElement(downloadLink);
        downloadHandler.followRedirectsWhenDownloading(true);
        File downloadedFile = downloadHandler.downloadFile();

        assertThat(downloadedFile.exists(), is(equalTo(true)));
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(200)));
    }

    @Test
    public void downloadAFileWhilstMimicingSeleniumCookies() throws Exception {
        //TODO modify test page to require a cookie for download
        FileDownloader downloadHandler = new FileDownloader(driver);
        driver.get(webServerURL + ":" + webServerPort + "/downloadTest.html");
        WebElement downloadLink = driver.findElement(By.id("fileToDownload"));
        downloadHandler.setURISpecifiedInAnchorElement(downloadLink);
        downloadHandler.mimicWebDriverCookieState(true);
        File downloadedFile = downloadHandler.downloadFile();

        assertThat(downloadedFile.exists(), is(equalTo(true)));
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(200)));
    }

}
