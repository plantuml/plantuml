#!/bin/bash
# Experimental: builds both TeaVM engines (JS + Wasm GC) side by side,
# then serves them. Open http://localhost:8080/bench.html to compare them.
set -e
./gradlew clean teavmWasm -Pfast
cd build/generated/teavm/js
python3 -m http.server 8080
