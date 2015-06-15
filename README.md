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


