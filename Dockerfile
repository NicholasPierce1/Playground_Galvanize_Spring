FROM openjdk:16
ADD target/demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","demo-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080