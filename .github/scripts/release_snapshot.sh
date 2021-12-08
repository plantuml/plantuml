#!/usr/bin/env bash
set -ex

TAG="snapshot"
DATE_TIME_UTC=$(date -u +"%F at %T (UTC)")
RELEASE_DIR="target/github_release"

gh release delete "${TAG}" -y || true

git tag --force "${TAG}"

git push --force origin "${TAG}"

mkdir "${RELEASE_DIR}"

ln -s "../plantuml.pom"             "${RELEASE_DIR}/plantuml-SNAPSHOT.pom"
ln -s "../plantuml.jar"             "${RELEASE_DIR}/plantuml-SNAPSHOT.jar"
ln -s "../plantuml-javadoc.jar"     "${RELEASE_DIR}/plantuml-SNAPSHOT-javadoc.jar"
ln -s "../plantuml-sources.jar"     "${RELEASE_DIR}/plantuml-SNAPSHOT-sources.jar"

if [[ -e "target/plantuml.pom.asc" ]]; then
  # signatures are optional so that forked repos can release snapshots without needing a gpg signing key
  ln -s "../plantuml.pom.asc"         "${RELEASE_DIR}/plantuml-SNAPSHOT.pom.asc"
  ln -s "../plantuml.jar.asc"         "${RELEASE_DIR}/plantuml-SNAPSHOT.jar.asc"
  ln -s "../plantuml-javadoc.jar.asc" "${RELEASE_DIR}/plantuml-SNAPSHOT-javadoc.jar.asc"
  ln -s "../plantuml-sources.jar.asc" "${RELEASE_DIR}/plantuml-SNAPSHOT-sources.jar.asc"
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
