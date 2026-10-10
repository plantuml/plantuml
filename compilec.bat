@echo off
rem Java -> C (TeaVM) -> native Windows executable (MSVC), see src/teavm-c/README.md
rem Usage: compilec.bat [x64|ARM64]   (default: the architecture of this machine)
setlocal

cd /d "%~dp0"

set "PLATFORM=%~1"
if "%PLATFORM%"=="" (
	if /i "%PROCESSOR_ARCHITECTURE%"=="ARM64" (set "PLATFORM=ARM64") else (set "PLATFORM=x64")
)

echo === 1/3 Generating C sources with TeaVM ===
call gradlew.bat :generateC
if errorlevel 1 goto :error

echo === 2/3 Configuring CMake (%PLATFORM%) ===
cmake -S build\generated\teavm\c -B build\c-build -A %PLATFORM%
if errorlevel 1 goto :error

echo === 3/3 Building plantuml-c.exe ===
cmake --build build\c-build --config Release
if errorlevel 1 goto :error

set "EXE="
for /r "build\generated\teavm\c\bin" %%f in (plantuml-c.exe) do if exist "%%f" set "EXE=%%f"
if "%EXE%"=="" goto :error

echo.
echo Done: %EXE%
echo Try:  "%EXE%" mindmap.puml mindmap.svg
exit /b 0

:error
echo.
echo Build failed.
exit /b 1
