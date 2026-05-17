#!/usr/bin/env bash
# Print build diagnostic information.
set -euo pipefail

cat <<EOF
===========================================
Vega .puml files: $(find src/test/resources/vega -name '*.puml' | wc -l)
Vega subdirs:     $(find src/test/resources/vega -type d | wc -l)
Test classes:     $(find src/test/java -name '*Test.java' | wc -l)
===========================================
EOF
