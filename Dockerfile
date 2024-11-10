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
    fonts-dejavu \
  && apt-get autoremove \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*

# Build Graphviz from source because there are no binary distributions for recent versions
ARG GRAPHVIZ_VERSION
ARG GRAPHVIZ_BUILD_DIR=/tmp/graphiz-build
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        build-essential \
        jq \
        libexpat1-dev \
        libgd-dev \
        zlib1g-dev \
        && \
    mkdir -p $GRAPHVIZ_BUILD_DIR && \
    cd $GRAPHVIZ_BUILD_DIR && \
    GRAPHVIZ_VERSION=${GRAPHVIZ_VERSION:-$(curl -s https://gitlab.com/api/v4/projects/4207231/releases/ | jq -r '.[] | .name' | sort -V -r | head -1)} && \
    curl -o graphviz.tar.gz https://gitlab.com/api/v4/projects/4207231/packages/generic/graphviz-releases/${GRAPHVIZ_VERSION}/graphviz-${GRAPHVIZ_VERSION}.tar.gz && \
    tar -xzf graphviz.tar.gz && \
    cd graphviz-$GRAPHVIZ_VERSION && \
    ./configure && \
    make && \
    make install && \
    apt-get remove -y \
        build-essential \
        jq \
        libexpat1-dev \
        libgd-dev \
        zlib1g-dev \
        && \
    apt-get autoremove -y && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf $GRAPHVIZ_BUILD_DIR

COPY --from=loader /opt/plantuml.jar /opt/plantuml.jar

WORKDIR /data

ENTRYPOINT ["java", "-jar", "/opt/plantuml.jar"]
CMD ["-version"]
