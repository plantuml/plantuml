@echo off
rem Experimental: builds both TeaVM engines (JS + Wasm GC) side by side,
rem then serves them. Open http://localhost:8080/bench.html to compare them.
call gradlew.bat clean teavmWasm -Pfast
if errorlevel 1 exit /b %errorlevel%
cd build/generated/teavm/js
python -m http.server 8080