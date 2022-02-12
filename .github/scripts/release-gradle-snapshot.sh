#!/usr/bin/env bash
set -ex

POM_VERSION=$(gradle properties -q | grep "version:" | awk '{print $2}')
TAG="snapshot"
DATE_TIME_UTC=$(date -u +"%F at %T (UTC)")
RELEASE_DIR="build/github_release"

gh release delete "${TAG}" -y || true

git tag --force "${TAG}"

git push --force origin "${TAG}"

mkdir "${RELEASE_DIR}"

ln -s "../publications/maven/module.json"                    "${RELEASE_DIR}/plantuml-SNAPSHOT-module.json"
ln -s "../publications/maven/pom-default.xml"                "${RELEASE_DIR}/plantuml-SNAPSHOT.pom"
ln -s "../libs/plantuml-${POM_VERSION}.jar"                  "${RELEASE_DIR}/plantuml-SNAPSHOT.jar"
ln -s "../libs/plantuml-${POM_VERSION}-javadoc.jar"          "${RELEASE_DIR}/plantuml-SNAPSHOT-javadoc.jar"
ln -s "../libs/plantuml-${POM_VERSION}-sources.jar"          "${RELEASE_DIR}/plantuml-SNAPSHOT-sources.jar"

if [[ -e "build/publications/maven/module.json.asc" ]]; then
  # signatures are optional so that forked repos can release snapshots without needing a gpg signing key
  ln -s "../publications/maven/module.json.asc"              "${RELEASE_DIR}/plantuml-SNAPSHOT-module.json.asc"
  ln -s "../publications/maven/pom-default.xml.asc"          "${RELEASE_DIR}/plantuml-SNAPSHOT.pom.asc"
  ln -s "../libs/plantuml-${POM_VERSION}.jar.asc"            "${RELEASE_DIR}/plantuml-SNAPSHOT.jar.asc"
  ln -s "../libs/plantuml-${POM_VERSION}-javadoc.jar.asc"    "${RELEASE_DIR}/plantuml-SNAPSHOT-javadoc.jar.asc"
  ln -s "../libs/plantuml-${POM_VERSION}-sources.jar.asc"    "${RELEASE_DIR}/plantuml-SNAPSHOT-sources.jar.asc"
fi

echo -n "${DATE_TIME_UTC}" > "${RELEASE_DIR}/plantuml-SNAPSHOT.timestamp"

cat <<-EOF >notes.txt
  This is a pre-release of [the latest development work](https://github.com/plantuml/plantuml/commits/).
  ⚠️  **It is not ready for general use** ⚠️
  ⏱  _Snapshot taken the ${DATE_TIME_UTC}_
EOF

gh release create \
  --prerelease \
  --target "${GITHUB_SHA}" \
  --title "${TAG}" \
  --notes-file notes.txt \
  "${TAG}" ${RELEASE_DIR}/*

echo "::notice title=release snapshot::Snapshot released at ${GITHUB_SERVER_URL}/${GITHUB_REPOSITORY}/releases/tag/${TAG} and taken the ${DATE_TIME_UTC}"
