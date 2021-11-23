#!/usr/bin/env bash
set -ex

TAG="snapshot"
DATE_TIME_UTC=$(date -u +"%F at %T (UTC)")

gh release delete "${TAG}" -y || true

git tag --force "${TAG}"

git push --force origin "${TAG}"

mv plantuml.jar         plantuml-SNAPSHOT.jar
mv plantuml-javadoc.jar plantuml-SNAPSHOT-javadoc.jar
mv plantuml-sources.jar plantuml-SNAPSHOT-sources.jar
echo -n "${DATE_TIME_UTC}" > plantuml-SNAPSHOT-timestamp.lock

cat <<-EOF >notes.txt
  This is a pre-release of [the latest development work](https://github.com/plantuml/plantuml/commits/).
  ⚠️  **It is not ready for general use** ⚠️
  ⏱  _Snapshot taken the ${DATE_TIME_UTC}_
EOF

gh release create --prerelease --target "${GITHUB_SHA}" --title "${TAG}" --notes-file notes.txt "${TAG}" \
  plantuml-SNAPSHOT.jar \
  plantuml-SNAPSHOT-javadoc.jar \
  plantuml-SNAPSHOT-sources.jar \
  plantuml-SNAPSHOT-timestamp.lock

echo "::notice title=release snapshot::Snapshot released at ${GITHUB_SERVER_URL}/${GITHUB_REPOSITORY}/releases/tag/${TAG} and taken the ${DATE_TIME_UTC}"
