@echo off
rem Smoke test of the PDF output of the native (TeaVM C) plantuml.exe, see src/teavm-c/README.md
rem Usage: hello_world_pdf.bat [path\to\plantuml.exe]   Set NO_OPEN=1 to not open the PDF at the end
setlocal

cd /d "%~dp0"

set "EXE=%~1"
if "%EXE%"=="" if exist "%~dp0plantuml.exe" set "EXE=%~dp0plantuml.exe"
if "%EXE%"=="" for /r "build\generated\teavm\c\bin" %%f in (plantuml.exe) do if exist "%%f" set "EXE=%%f"
if "%EXE%"=="" (
	echo plantuml.exe not found: run compilec.bat first, or give its path as argument.
	exit /b 1
)
echo Using %EXE%

set "DIR=%TEMP%\hello_world_pdf"
if exist "%DIR%" rmdir /s /q "%DIR%"
mkdir "%DIR%"

> "%DIR%\hello.puml" (
	echo @startuml
	echo Alice -^> Bob : Hello World
	echo Bob --^> Alice : PDF from a native executable
	echo @enduml
)

echo === 1/2 File mode: -tpdf ===
"%EXE%" -tpdf "%DIR%\hello.puml"
if errorlevel 1 goto :error
if not exist "%DIR%\hello.pdf" goto :error
for %%f in ("%DIR%\hello.pdf") do echo hello.pdf: %%~zf bytes
findstr /b /c:"%%PDF-" "%DIR%\hello.pdf" >nul
if errorlevel 1 (echo hello.pdf does not start with %%PDF- & goto :error)

echo === 2/2 Pipe mode: -pipe -tpdf ===
"%EXE%" -pipe -tpdf < "%DIR%\hello.puml" > "%DIR%\hello_pipe.pdf"
if errorlevel 1 goto :error
for %%f in ("%DIR%\hello_pipe.pdf") do echo hello_pipe.pdf: %%~zf bytes
findstr /b /c:"%%PDF-" "%DIR%\hello_pipe.pdf" >nul
if errorlevel 1 (echo hello_pipe.pdf does not start with %%PDF- & goto :error)

echo.
echo OK: %DIR%\hello.pdf
if not defined NO_OPEN start "" "%DIR%\hello.pdf"
exit /b 0

:error
echo.
echo PDF test failed.
exit /b 1
