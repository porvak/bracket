# Bracket Application

Bracket application will allow users to choose to fill out a bracket to compete with other users.

## Developer Guide

### Requirements

* Mongo 2.0 or greater
* Java 1.6 or greater
* Servlet 2.5 or higher container (Tomcat 7)
* Maven 3 or greater

### Getting Started

* Clone this repo.
* Startup your mongoDb instance
* run <projectRoot>/data/populateMongoLocal.sh

This will create/replace a db called bracket in mongoDb using seed data found in /data/bracket/

* Run the application by using maven's __mvn package__ and deploying the application to your favorite Servlet container.