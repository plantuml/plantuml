#!/usr/bin/env bash
# Print gradle.properties file and RELEASE_VERSION.
set -euo pipefail

echo "::group::[gradle.properties]"
cat gradle.properties
echo
echo "::endgroup::"
echo "RELEASE_VERSION=${RELEASE_VERSION}"
