#FROM maven:3.8.4-openjdk-11 AS build
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests
#
#FROM openjdk:17-jdk-slim
#WORKDIR /app
#
#COPY --from=build /app/target/*.jar number_service.jar
#
#EXPOSE 8081
#ENTRYPOINT ["java", "-jar", "number_service.jar"]
#
#FROM openjdk:17-jdk-slim
#WORKDIR /app
#
#COPY --from=build /app/target/*.jar order_service.jar
#
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "order_service.jar"]
