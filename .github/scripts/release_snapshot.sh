#!/usr/bin/env bash
set -ex

TAG="snapshot"

gh release delete "${TAG}" -y || true

git tag --force "${TAG}"

git push --force origin "${TAG}"

mv plantuml.jar         plantuml-SNAPSHOT.jar
mv plantuml-javadoc.jar plantuml-SNAPSHOT-javadoc.jar
mv plantuml-sources.jar plantuml-SNAPSHOT-sources.jar

cat <<-EOF >notes.txt
  This is a pre-release of the latest development work.
  ⚠️  **It is not ready for general use** ⚠️
EOF

gh release create --prerelease --target "${GITHUB_SHA}" --title "${TAG}" --notes-file notes.txt "${TAG}" \
  plantuml-SNAPSHOT.jar \
  plantuml-SNAPSHOT-javadoc.jar \
  plantuml-SNAPSHOT-sources.jar

echo "::notice title=::Snapshot released at ${GITHUB_SERVER_URL}/${GITHUB_REPOSITORY}/releases/tag/${TAG}"
