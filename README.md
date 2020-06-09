# stocks-service-app

Application for stock management.

Prerequisites: Oracle JDK 13.0.2 & Maven

Tech stack: Oracle JDK 13.0.2, RDBMS, Apache Kafka

Run: 
  mvn clean install;
  cd target;
  java -jar stocks-service-app-1.0-SNAPSHOT.jar;

port is defined in application.yml and 20071 by default

Database data is being rolled on and Embedded Apache Kafka started up in "dev" profile defined in application.yml as spring.profiles.active

API:
https://app.swaggerhub.com/apis/paskawonder1/stocks-service-app/1.0-SNAPSHOT
