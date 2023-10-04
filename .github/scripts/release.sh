#!/usr/bin/env bash
set -ex

find . -name "*.jar"
find . -name "*.asc"

mkdir "github_release"

cp "build/libs/plantuml-${RELEASE_VERSION}.jar" "github_release/plantuml.jar"
cp "build/libs/plantuml-${RELEASE_VERSION}.jar" "github_release/plantuml-${RELEASE_VERSION}.jar"
cp "build/libs/plantuml-${RELEASE_VERSION}-javadoc.jar" "github_release/plantuml-${RELEASE_VERSION}-javadoc.jar"
cp "build/libs/plantuml-${RELEASE_VERSION}-sources.jar" "github_release/plantuml-${RELEASE_VERSION}-sources.jar"
cp "build/libs/plantuml-pdf-${RELEASE_VERSION}.jar" "github_release/plantuml-pdf-${RELEASE_VERSION}.jar"
#cp "build/libs/plantuml-linux-amd64-${RELEASE_VERSION}" "github_release/plantuml-linux-amd64-${RELEASE_VERSION}"
#cp "build/libs/plantuml-darwin-amd64-${RELEASE_VERSION}" "github_release/plantuml-darwin-amd64-${RELEASE_VERSION}"
#cp "build/libs/plantuml-win-amd64-${RELEASE_VERSION}.exe" "github_release/plantuml-win-amd64-${RELEASE_VERSION}.exe"
cp "plantuml-asl/build/libs/plantuml-asl-${RELEASE_VERSION}.jar" "github_release/plantuml-asl-${RELEASE_VERSION}.jar"
cp "plantuml-asl/build/libs/plantuml-asl-${RELEASE_VERSION}-javadoc.jar" "github_release/plantuml-asl-${RELEASE_VERSION}-javadoc.jar"
cp "plantuml-asl/build/libs/plantuml-asl-${RELEASE_VERSION}-sources.jar" "github_release/plantuml-asl-${RELEASE_VERSION}-sources.jar"
cp "plantuml-bsd/build/libs/plantuml-bsd-${RELEASE_VERSION}.jar" "github_release/plantuml-bsd-${RELEASE_VERSION}.jar"
cp "plantuml-bsd/build/libs/plantuml-bsd-${RELEASE_VERSION}-javadoc.jar" "github_release/plantuml-bsd-${RELEASE_VERSION}-javadoc.jar"
cp "plantuml-bsd/build/libs/plantuml-bsd-${RELEASE_VERSION}-sources.jar" "github_release/plantuml-bsd-${RELEASE_VERSION}-sources.jar"
cp "plantuml-epl/build/libs/plantuml-epl-${RELEASE_VERSION}.jar" "github_release/plantuml-epl-${RELEASE_VERSION}.jar"
cp "plantuml-epl/build/libs/plantuml-epl-${RELEASE_VERSION}-javadoc.jar" "github_release/plantuml-epl-${RELEASE_VERSION}-javadoc.jar"
cp "plantuml-epl/build/libs/plantuml-epl-${RELEASE_VERSION}-sources.jar" "github_release/plantuml-epl-${RELEASE_VERSION}-sources.jar"
cp "plantuml-lgpl/build/libs/plantuml-lgpl-${RELEASE_VERSION}.jar" "github_release/plantuml-lgpl-${RELEASE_VERSION}.jar"
cp "plantuml-lgpl/build/libs/plantuml-lgpl-${RELEASE_VERSION}-javadoc.jar" "github_release/plantuml-lgpl-${RELEASE_VERSION}-javadoc.jar"
cp "plantuml-lgpl/build/libs/plantuml-lgpl-${RELEASE_VERSION}-sources.jar" "github_release/plantuml-lgpl-${RELEASE_VERSION}-sources.jar"
cp "plantuml-mit/build/libs/plantuml-mit-${RELEASE_VERSION}.jar" "github_release/plantuml-mit-${RELEASE_VERSION}.jar"
cp "plantuml-mit/build/libs/plantuml-mit-${RELEASE_VERSION}-javadoc.jar" "github_release/plantuml-mit-${RELEASE_VERSION}-javadoc.jar"
cp "plantuml-mit/build/libs/plantuml-mit-${RELEASE_VERSION}-sources.jar" "github_release/plantuml-mit-${RELEASE_VERSION}-sources.jar"
cp "plantuml-gplv2/build/libs/plantuml-gplv2-${RELEASE_VERSION}.jar" "github_release/plantuml-gplv2-${RELEASE_VERSION}.jar"
cp "plantuml-gplv2/build/libs/plantuml-gplv2-${RELEASE_VERSION}-javadoc.jar" "github_release/plantuml-gplv2-${RELEASE_VERSION}-javadoc.jar"
cp "plantuml-gplv2/build/libs/plantuml-gplv2-${RELEASE_VERSION}-sources.jar" "github_release/plantuml-gplv2-${RELEASE_VERSION}-sources.jar"

if [[ -e "build/publications/maven/module.json.asc" ]]; then
  # signatures are optional so that forked repos can release snapshots without needing a gpg signing key
  cp "build/libs/plantuml-${RELEASE_VERSION}.jar.asc" "github_release/plantuml.jar.asc"
  cp "build/libs/plantuml-${RELEASE_VERSION}.jar.asc" "github_release/plantuml-${RELEASE_VERSION}.jar.asc"
  cp "build/libs/plantuml-${RELEASE_VERSION}-javadoc.jar.asc" "github_release/plantuml-${RELEASE_VERSION}-javadoc.jar.asc"
  cp "build/libs/plantuml-${RELEASE_VERSION}-sources.jar.asc" "github_release/plantuml-${RELEASE_VERSION}-sources.jar.asc"
  cp "build/libs/plantuml-pdf-${RELEASE_VERSION}.jar.asc" "github_release/plantuml-pdf-${RELEASE_VERSION}.jar.asc"
  cp "plantuml-asl/build/libs/plantuml-asl-${RELEASE_VERSION}.jar.asc" "github_release/plantuml-asl-${RELEASE_VERSION}.jar.asc"
  cp "plantuml-asl/build/libs/plantuml-asl-${RELEASE_VERSION}-javadoc.jar.asc" "github_release/plantuml-asl-${RELEASE_VERSION}-javadoc.jar.asc"
  cp "plantuml-asl/build/libs/plantuml-asl-${RELEASE_VERSION}-sources.jar.asc" "github_release/plantuml-asl-${RELEASE_VERSION}-sources.jar.asc"
  cp "plantuml-bsd/build/libs/plantuml-bsd-${RELEASE_VERSION}.jar.asc" "github_release/plantuml-bsd-${RELEASE_VERSION}.jar.asc"
  cp "plantuml-bsd/build/libs/plantuml-bsd-${RELEASE_VERSION}-javadoc.jar.asc" "github_release/plantuml-bsd-${RELEASE_VERSION}-javadoc.jar.asc"
  cp "plantuml-bsd/build/libs/plantuml-bsd-${RELEASE_VERSION}-sources.jar.asc" "github_release/plantuml-bsd-${RELEASE_VERSION}-sources.jar.asc"
  cp "plantuml-epl/build/libs/plantuml-epl-${RELEASE_VERSION}.jar.asc" "github_release/plantuml-epl-${RELEASE_VERSION}.jar.asc"
  cp "plantuml-epl/build/libs/plantuml-epl-${RELEASE_VERSION}-javadoc.jar.asc" "github_release/plantuml-epl-${RELEASE_VERSION}-javadoc.jar.asc"
  cp "plantuml-epl/build/libs/plantuml-epl-${RELEASE_VERSION}-sources.jar.asc" "github_release/plantuml-epl-${RELEASE_VERSION}-sources.jar.asc"
  cp "plantuml-lgpl/build/libs/plantuml-lgpl-${RELEASE_VERSION}.jar.asc" "github_release/plantuml-lgpl-${RELEASE_VERSION}.jar.asc"
  cp "plantuml-lgpl/build/libs/plantuml-lgpl-${RELEASE_VERSION}-javadoc.jar.asc" "github_release/plantuml-lgpl-${RELEASE_VERSION}-javadoc.jar.asc"
  cp "plantuml-lgpl/build/libs/plantuml-lgpl-${RELEASE_VERSION}-sources.jar.asc" "github_release/plantuml-lgpl-${RELEASE_VERSION}-sources.jar.asc"
  cp "plantuml-mit/build/libs/plantuml-mit-${RELEASE_VERSION}.jar.asc" "github_release/plantuml-mit-${RELEASE_VERSION}.jar.asc"
  cp "plantuml-mit/build/libs/plantuml-mit-${RELEASE_VERSION}-javadoc.jar.asc" "github_release/plantuml-mit-${RELEASE_VERSION}-javadoc.jar.asc"
  cp "plantuml-mit/build/libs/plantuml-mit-${RELEASE_VERSION}-sources.jar.asc" "github_release/plantuml-mit-${RELEASE_VERSION}-sources.jar.asc"
  cp "plantuml-gplv2/build/libs/plantuml-gplv2-${RELEASE_VERSION}.jar.asc" "github_release/plantuml-gplv2-${RELEASE_VERSION}.jar.asc"
  cp "plantuml-gplv2/build/libs/plantuml-gplv2-${RELEASE_VERSION}-javadoc.jar.asc" "github_release/plantuml-gplv2-${RELEASE_VERSION}-javadoc.jar.asc"
  cp "plantuml-gplv2/build/libs/plantuml-gplv2-${RELEASE_VERSION}-sources.jar.asc" "github_release/plantuml-gplv2-${RELEASE_VERSION}-sources.jar.asc"
fi

gh release create \
  --target "${GITHUB_SHA}" \
  --title "${TAG}" \
  "${TAG}" github_release/*

echo "::notice title=::Released at ${GITHUB_SERVER_URL}/${GITHUB_REPOSITORY}/releases/tag/${TAG} 🎉"
