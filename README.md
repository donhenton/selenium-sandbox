# selenium-sandbox
A playground for the selenium testing framework, for java.

## TestNg

The pom file is set up to read testNg files and point tests to use the
remote.server system property to configure the driver and repositories. See
DriverFactory.

> command line use: 
mvn clean test -PtestNg -Dremote.server=phantomjs -DtestNgFile=testng-sample

This runs the testNg file testing-sample with a phantomjs driver (a substitute for
a remote selenium server, technically its a local browser type)

## Current Best Sample
The best example is in the Angular test folder:

> mvn clean test -PtestNg -Dremote.server=docker -DtestNgFile=angular

the docker server needs to be running (see docker.txt)

This illustrates:

1. Use of remote driver
2. Use of TestNG listeners
3. Selenium testing for angular
4. Use communicating info from maven to running tests (see AngularTests#testFromMaven)




