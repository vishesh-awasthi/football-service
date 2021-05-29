FROM openjdk:8-jre-alpine
MAINTAINER Vishesh Awasthi <visheshkawasthi@gmail.com>
ADD /target/football-service-1.0.jar football-service.jar
ENTRYPOINT ["java", "-jar", "football-service.jar"]