FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=./datapool-service-1.0-SNAPSHOT.jar
ADD ./build /opt/build/
EXPOSE 8086
WORKDIR /opt/build
ENTRYPOINT ["java","-jar","datapool-service-1.0-SNAPSHOT.jar","--spring.config.location=/opt/build/docker_application.yml"]