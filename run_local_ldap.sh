#!/usr/bin/env bash

echo "starting reference-auth-server-webapp with ldap user management"
cd reference-auth-server-webapp
java \
  -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
  -Xms64M \
  -Xmx128M \
  -Dspring.profiles.active=local,users-ldap \
  -jar target/dependency/jetty-runner.jar \
  --config src/main/resources/jetty.xml \
  target/*.war

# use this to run the war file using the maven jetty plugin
#mvn jetty:run

