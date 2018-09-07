#FROM openjdk:8-jdk-alpine
#ADD target/ccri-reference-auth-server-webapp-*.war app.war
#ADD target/dependency/jetty-runner.jar jetty-runner.jar
#ADD src/main/resources/jetty.xml jetty.xml
#ENV JAVA_OPTS=""
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=local,users-ldap -jar jetty-runner.jar --config jetty.xml app.war" ]

FROM jetty:9.4-jre8-alpine
ADD target/ccri-auth-*.war /var/lib/jetty/webapps/ROOT.war