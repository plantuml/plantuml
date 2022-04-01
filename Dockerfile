FROM gradle:jdk11-alpine AS builder

COPY *.gradle.kts gradle.properties manifest.txt /app/
COPY gradle/ /app/src/
COPY src/ /app/src/
COPY skin/ /app/skin/
COPY stdlib/ /app/stdlib/
COPY svg/ /app/svg/
COPY test/ /app/test/
COPY themes/ /app/themes/

WORKDIR /app
RUN gradle --no-daemon clean build -x javaDoc -PjavacRelease=11 -x test

RUN find /app -name '*.jar'

################################

FROM openjdk:11

RUN apt-get update && apt-get install -y \
  graphviz \
  && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/build/libs/plantuml-*-SNAPSHOT.jar /app/plantuml.jar
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

ENTRYPOINT ["java", "-jar", "/app/plantuml.jar"]
