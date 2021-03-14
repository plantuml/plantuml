FROM openjdk:14-jdk-alpine3.10
ENV LANG en_US.UTF-8
RUN \
  apk add --no-cache graphviz ttf-dejavu fontconfig
COPY target/plantuml-1.2021.3-SNAPSHOT.jar plantuml.jar
RUN ["java", "-Djava.awt.headless=true", "-jar", "plantuml.jar", "-version"]
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "plantuml.jar", "-p"]
CMD ["-tsvg"]