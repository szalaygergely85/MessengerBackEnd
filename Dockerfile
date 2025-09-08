FROM eclipse-temurin:17.0.13_11-jre-jammy
ARG WAR_FILE=build/libs/*.war
COPY ${WAR_FILE} app.war

ENV SMTP_USERNAME=${SMTP_USERNAME}
ENV SMTP_PASSWORD=${SMTP_PASSWORD}

ENTRYPOINT ["java","-jar","/app.war"]