FROM eclipse-temurin:17.0.13_11-jre-jammy
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

COPY src/main/webapp/WEB-INF/jsp/ /BOOT-INF/classes/WEB-INF/jsp/
COPY src/main/webapp/css/ /BOOT-INF/classes/css/



ENTRYPOINT ["java", "-jar", "/app.jar"]