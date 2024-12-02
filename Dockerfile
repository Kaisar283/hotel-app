FROM openjdk:21

WORKDIR /app

COPY target/hotel-app-0.0.1-SNAPSHOT.jar /app/hotel-app-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/hotel-app-0.0.1-SNAPSHOT.jar"]