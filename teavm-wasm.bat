@echo off
rem Experimental: builds both TeaVM engines (JS + Wasm GC) side by side,
rem then serves them. Open http://localhost:8080/bench.html to compare them.
rem
rem Extra arguments are passed to Gradle, e.g. the diagnostic switches:
rem   teavm-wasm.bat -PjsStrict=true
rem   teavm-wasm.bat -PwasmStrict=false
call gradlew.bat clean teavmWasm -Pfast %*
if errorlevel 1 exit /b %errorlevel%
pushd build\generated\teavm\js
python -m http.server 8080
popd
