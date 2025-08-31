FROM eclipse-temurin:17.0.13_11-jre-jammy
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/webapp/WEB-INF/css/ /BOOT-INF/classes/WEB-INF/css/
COPY src/main/webapp/WEB-INF/js/ /BOOT-INF/classes/WEB-INF/js/
COPY src/main/webapp/WEB-INF/images/ /BOOT-INF/classes/WEB-INF/images/


ENTRYPOINT ["java", "-jar", "/app.jar"]