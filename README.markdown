# The Bracket App

Bracket application will allow users to choose to fill out a bracket to compete with other users.

## Developer Guide

### Requirements

* Mongo 2.0 or greater
* Java 1.6 or greater
* Servlet 2.5 or higher container (Tomcat 7)
* Maven 3 or greater
* Node.js and npm
* CoffeeScript module for compiling coffee

### Getting Started

* Clone this repo.

#### Database Setup

This will create/replace a db called bracket in mongoDb using seed data found in /data/bracket/

* Startup your mongoDb instance
* run <projectRoot>/data/populateMongoLocal.sh, or populateMongoLocal.bat

#### CoffeeScript setup

You can run compile the CoffeeScript to javascript by executing:

* /buildCoffee/compileOnce.sh - one time compilation
* /buildCoffee/compileAndWatch.sh - this will continually monitor /src/main/webapp/resources/coffee for changes

#### Java Setup

This application should be automatically configured in any IDE that supports maven

#### Running the application

To run the application use run __mvn package__ from the project's root directory.

Once the build sucessfully finishes, you can deploy /target/bracket.war to your favorite Servlet container.


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/porvak/bracket/trend.png)](https://bitdeli.com/free "Bitdeli Badge")



