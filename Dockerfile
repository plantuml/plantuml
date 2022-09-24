FROM alpine:latest AS loader

ARG PLANTUML_VERSION

RUN apk add --no-cache \
  wget \
  ca-certificates

RUN wget \
  "https://github.com/plantuml/plantuml/releases/download/${PLANTUML_VERSION}/plantuml-${PLANTUML_VERSION#?}.jar" \
  -O /opt/plantuml.jar

FROM openjdk:17-alpine

ENV LANG en_US.UTF-8

RUN apk add --no-cache \
  graphviz \
  ttf-dejavu

COPY --from=loader /opt/plantuml.jar /opt/plantuml.jar

WORKDIR /data
VOLUME ["/data"]

ENTRYPOINT ["java", "-jar", "/opt/plantuml.jar"]
CMD ["-version"]