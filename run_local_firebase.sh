#!/usr/bin/env bash

echo "starting reference-auth-server-webapp with firebase user management"
cd reference-auth-server-webapp
java \
  -Xms64M \
  -Xmx128M \
  -Dspring.profiles.active=local,users-firebase \
  -jar target/dependency/jetty-runner.jar \
  --config src/main/resources/jetty.xml \
  target/*.war

# use this to run the war file using the maven jetty plugin
#mvn jetty:run

