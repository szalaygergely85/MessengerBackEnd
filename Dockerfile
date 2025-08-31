FROM eclipse-temurin:17.0.13_11-jre-jammy
ARG WAR_FILE=build/libs/*.war
COPY ${WAR_FILE} app.war
ENTRYPOINT ["java","-jar","/app.war"]