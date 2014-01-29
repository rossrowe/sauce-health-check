Jenkins Sauce Health Check plugin
==================

This project contains the source code and corresponding unit tests for the Jenkins Sauce Health Check plugin.

This is a simple Jenkins plugin which displays the Sauce system status within the Jenkins footer (which is retrieved via a call to the Sauce REST API).

The project is intended to demonstrate how you can write [Selenium](http://docs.seleniumhq.org/) tests quickly and easily using [JUnit](http://www.junit.org), and includes sample unit, integration and acceptance tests.  In addition, the project also includes a sample test

This project can either be forked or cloned using Github, or the source can be directly downloaded as a [zip file](https://github.com/rossrowe/sauce-health-check/archive/master.zip).

The project uses [Apache Maven](http://maven.apache.org).  If you don't already have Maven installed, follow the instructions on the [Maven Installation Page](http://maven.apache.org/run-maven/index.html).

To run the unit and integration tests, simply run the following command from the directory where you cloned/extracted the source files to:

    mvn clean verify