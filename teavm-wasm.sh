#!/bin/bash
# Experimental: builds both TeaVM engines (JS + Wasm GC) side by side,
# then serves them. Open http://localhost:8080/bench.html to compare them.
#
# Extra arguments are passed to Gradle, e.g. the diagnostic switches:
#   ./teavm-wasm.sh -PjsStrict=true
#   ./teavm-wasm.sh -PwasmStrict=false
#   ./teavm-wasm.sh -PwasmDebug=true     (then open bench.html?wasmdebug)
set -e
./gradlew clean teavmWasm -Pfast "$@"
cd build/generated/teavm/js
python3 -m http.server 8080
