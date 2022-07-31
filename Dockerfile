FROM openjdk:17-alpine

ARG PLANTUML_VERSION=v1.2022.6

ENV LANG en_US.UTF-8

RUN \
  apk add --no-cache graphviz wget ca-certificates ttf-dejavu && \
  wget "https://github.com/plantuml/plantuml/releases/download/${PLANTUML_VERSION}/plantuml-${PLANTUML_VERSION#?}.jar" -O /opt/plantuml.jar && \
  apk del wget ca-certificates

WORKDIR /data
VOLUME ["/data"]

ENTRYPOINT ["java", "-jar", "/opt/plantuml.jar"]
CMD ["-version"]