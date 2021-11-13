#!/usr/bin/env bash
set -ex

mv plantuml.jar         "plantuml-${POM_VERSION}.jar"
mv plantuml-javadoc.jar "plantuml-${POM_VERSION}-javadoc.jar"
mv plantuml-sources.jar "plantuml-${POM_VERSION}-sources.jar"

gh release create --target "${GITHUB_SHA}" "${TAG}" \
  "plantuml-${POM_VERSION}.jar" \
  "plantuml-${POM_VERSION}-javadoc.jar" \
  "plantuml-${POM_VERSION}-sources.jar"

echo "::notice title=::Released at ${GITHUB_SERVER_URL}/${GITHUB_REPOSITORY}/releases/tag/${TAG} 🎉"
