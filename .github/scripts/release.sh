#!/usr/bin/env bash
set -ex

RELEASE_DIR="target/github_release"

mkdir "${RELEASE_DIR}"

ln -s "../plantuml.jar"             "${RELEASE_DIR}/plantuml-${POM_VERSION}.jar"
ln -s "../plantuml-javadoc.jar"     "${RELEASE_DIR}/plantuml-${POM_VERSION}-javadoc.jar"
ln -s "../plantuml-sources.jar"     "${RELEASE_DIR}/plantuml-${POM_VERSION}-sources.jar"
# we do not release the .pom or .asc signature files here, they will be added in a later PR

gh release create \
  --target "${GITHUB_SHA}" \
  --title "${TAG}" \
  "${TAG}" ${RELEASE_DIR}/*

echo "::notice title=::Released at ${GITHUB_SERVER_URL}/${GITHUB_REPOSITORY}/releases/tag/${TAG} 🎉"
