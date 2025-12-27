FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 9888

ENTRYPOINT ["java","-jar","/app/app.jar"]
