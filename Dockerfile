FROM openjdk:11

COPY build/libs/*.jar app.jar

RUN sh -c 'touch /app.jar'
RUN groupadd -g 999 appuser && useradd -r -u 999 -g appuser appuser
USER appuser

ENTRYPOINT [ "sh", "-c", "java -Dspring.profiles.active=prod -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]