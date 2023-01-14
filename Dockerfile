FROM alpine:latest AS loader

ARG PLANTUML_VERSION

RUN apk add --no-cache \
  wget \
  ca-certificates

RUN wget \
  "https://github.com/plantuml/plantuml/releases/download/${PLANTUML_VERSION}/plantuml-${PLANTUML_VERSION#?}.jar" \
  -O /opt/plantuml.jar

FROM eclipse-temurin:17-jre-jammy

ENV LANG en_US.UTF-8

RUN apt-get update \
  && apt-get install --no-install-recommends -y \
    graphviz \
    fonts-dejavu \
  && apt-get autoremove \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*

COPY --from=loader /opt/plantuml.jar /opt/plantuml.jar

WORKDIR /data

ENTRYPOINT ["java", "-jar", "/opt/plantuml.jar"]
CMD ["-version"]
