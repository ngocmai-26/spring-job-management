FROM openjdk:17.0.2-jdk-slim
COPY /target/fira-0.0.1-SNAPSHOT.jar fira.jar
EXPOSE 8082
ENTRYPOINT ["java" , "-jar" , "fira.jar"]





