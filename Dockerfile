FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests


FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/apollo.fastappointments-0.0.1-SNAPSHOT.jar /app/apollo.fastappointments.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/apollo.fastappointments.jar"]
