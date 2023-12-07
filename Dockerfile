FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/mai-trpo-lab-3-backend.jar
COPY --from=build hikari.properties /app
WORKDIR /app
ENTRYPOINT ["java","-jar","/app/mai-trpo-lab-3-backend.jar"]