FROM openjdk:17
WORKDIR /app
LABEL maintainer = "anikethta4200@gmail.com"
COPY ./target/anikethta-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "anikethta-0.0.1-SNAPSHOT.jar"]
