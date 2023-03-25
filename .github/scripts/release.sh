#!/usr/bin/env bash
set -ex

RELEASE_DIR="build/github_release"

mkdir "${RELEASE_DIR}"

ln -s "../libs/plantuml-${RELEASE_VERSION}.jar"                  "${RELEASE_DIR}/plantuml.jar"
ln -s "../libs/plantuml-${RELEASE_VERSION}.jar"                  "${RELEASE_DIR}/plantuml-${RELEASE_VERSION}.jar"
ln -s "../libs/plantuml-${RELEASE_VERSION}-javadoc.jar"          "${RELEASE_DIR}/plantuml-${RELEASE_VERSION}-javadoc.jar"
ln -s "../libs/plantuml-${RELEASE_VERSION}-sources.jar"          "${RELEASE_DIR}/plantuml-${RELEASE_VERSION}-sources.jar"
ln -s "../libs/plantuml-pdf-${RELEASE_VERSION}.jar"              "${RELEASE_DIR}/plantuml-pdf-${RELEASE_VERSION}.jar"

if [[ -e "build/publications/maven/module.json.asc" ]]; then
  # signatures are optional so that forked repos can release snapshots without needing a gpg signing key
  ln -s "../libs/plantuml-${RELEASE_VERSION}.jar.asc"            "${RELEASE_DIR}/plantuml.jar.asc"
  ln -s "../libs/plantuml-${RELEASE_VERSION}.jar.asc"            "${RELEASE_DIR}/plantuml-${RELEASE_VERSION}.jar.asc"
  ln -s "../libs/plantuml-${RELEASE_VERSION}-javadoc.jar.asc"    "${RELEASE_DIR}/plantuml-${RELEASE_VERSION}-javadoc.jar.asc"
  ln -s "../libs/plantuml-${RELEASE_VERSION}-sources.jar.asc"    "${RELEASE_DIR}/plantuml-${RELEASE_VERSION}-sources.jar.asc"
  ln -s "../libs/plantuml-pdf-${RELEASE_VERSION}.jar.asc"        "${RELEASE_DIR}/plantuml-pdf-${RELEASE_VERSION}.jar.asc"
fi

gh release create \
  --target "${GITHUB_SHA}" \
  --title "${TAG}" \
  "${TAG}" ${RELEASE_DIR}/*

echo "::notice title=::Released at ${GITHUB_SERVER_URL}/${GITHUB_REPOSITORY}/releases/tag/${TAG} 🎉"
