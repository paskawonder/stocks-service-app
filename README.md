# stocks-service-app

Application for stock management.

Prerequisites: Oracle JDK 13.0.2 & Maven

Tech stack: Oracle JDK 13.0.2, H2 RDBMS

Run: 
  mvn clean install;
  cd target;
  java -jar stocks-service-app-1.0-SNAPSHOT.jar;

port is defined in application.yml and 20071 by default

At startup, the database is migrated with the help of Flyway and scripts located under src/main/resources/db/migration/ 
Flyway can be disabled by setting spring.flyway.enabled property in application.yml to false
