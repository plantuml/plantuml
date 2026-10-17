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

call :findcmake
if errorlevel 1 goto :error

echo === 2/3 Configuring CMake (%PLATFORM%) with %CMAKE% ===
"%CMAKE%" -S build\generated\teavm\c -B build\c-build -A %PLATFORM%
if errorlevel 1 goto :error

echo === 3/3 Building plantuml.exe ===
"%CMAKE%" --build build\c-build --config Release
if errorlevel 1 goto :error

set "EXE="
for /r "build\generated\teavm\c\bin" %%f in (plantuml.exe) do if exist "%%f" set "EXE=%%f"
if "%EXE%"=="" goto :error

rem Copy the executable to the repository root (the current directory)
copy /y "%EXE%" "%~dp0plantuml.exe" >nul
if errorlevel 1 goto :error

echo.
echo Done: %~dp0plantuml.exe
echo Try:  plantuml.exe --help
exit /b 0

:error
echo.
echo Build failed.
exit /b 1

rem ---------------------------------------------------------------------------
rem Locates cmake.exe and sets CMAKE. Order: PATH, CMAKE_EXE variable,
rem standard CMake install folders, then the CMake bundled with Visual Studio.
rem ---------------------------------------------------------------------------
:findcmake
set "CMAKE="
if defined CMAKE_EXE if exist "%CMAKE_EXE%" set "CMAKE=%CMAKE_EXE%"
if defined CMAKE goto :cmakefound
for /f "delims=" %%i in ('where cmake.exe 2^>nul') do if not defined CMAKE set "CMAKE=%%i"
if defined CMAKE goto :cmakefound
for %%d in ("%ProgramFiles%\CMake\bin" "%ProgramFiles(x86)%\CMake\bin" "%LocalAppData%\Programs\CMake\bin") do if not defined CMAKE if exist "%%~d\cmake.exe" set "CMAKE=%%~d\cmake.exe"
if defined CMAKE goto :cmakefound
set "VSWHERE=%ProgramFiles(x86)%\Microsoft Visual Studio\Installer\vswhere.exe"
if not exist "%VSWHERE%" goto :cmakemissing
for /f "delims=" %%i in ('"%VSWHERE%" -latest -products * -find Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin\cmake.exe') do if not defined CMAKE set "CMAKE=%%i"
if defined CMAKE goto :cmakefound

:cmakemissing
echo.
echo ERROR: cmake.exe not found.
echo Install CMake ^(https://cmake.org/download/^) or the Visual Studio component
echo "C++ CMake tools for Windows", then reopen this terminal.
echo You can also set CMAKE_EXE to the full path of cmake.exe.
exit /b 1

:cmakefound
exit /b 0
