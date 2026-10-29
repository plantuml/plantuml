@echo off
rem Fuller smoke test of the PDF output of the native (TeaVM C) plantuml.exe: colors, transparency, emoji, sprite
rem Usage: hello_world_2_pdf.bat [path\to\plantuml.exe]   Set NO_OPEN=1 to not open the PDF at the end
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

set "DIR=%TEMP%\hello_world_2_pdf"
if exist "%DIR%" rmdir /s /q "%DIR%"
mkdir "%DIR%"

> "%DIR%\hello2.puml" (
	echo @startuml
	echo skinparam backgroundColor transparent
	echo skinparam sequence {
	echo 	ArrowColor #C00000
	echo 	LifeLineBorderColor #606060
	echo 	ParticipantBackgroundColor #FFE0A0
	echo }
	echo sprite $smile [16x16/16] {
	echo 0000000000000000
	echo 0000000000000000
	echo 0000007777000000
	echo 0000777777770000
	echo 0007777777777000
	echo 0007707777077000
	echo 0077707777077700
	echo 0077777777777700
	echo 0077777777777700
	echo 0077777777777700
	echo 0007000000007000
	echo 0007770000777000
	echo 0000777777770000
	echo 0000007777000000
	echo 0000000000000000
	echo 0000000000000000
	echo }
	echo participant "Alice <$smile>" as A #FFD700
	echo participant "Bob <:1f600:>" as B #LightBlue
	echo A -^> B : Hello ^<color:red^>red^</color^>, ^<color:green^>green^</color^>, ^<b^>bold^</b^>, ^<i^>italic^</i^>
	echo B --^> A : Emoji ^<:1f680:^> ^<:2764:^> and sprite ^<$smile^>
	echo note over A, B #80FF8080 : Semi-transparent note
	echo A -^> B : last message
	echo @enduml
)

echo === 1/2 File mode: -tpdf ===
"%EXE%" -tpdf "%DIR%\hello2.puml"
if errorlevel 1 goto :error
if not exist "%DIR%\hello2.pdf" goto :error
for %%f in ("%DIR%\hello2.pdf") do echo hello2.pdf: %%~zf bytes
findstr /b /c:"%%PDF-" "%DIR%\hello2.pdf" >nul
if errorlevel 1 (echo hello2.pdf does not start with %%PDF- & goto :error)

echo === 2/2 Pipe mode: -pipe -tpdf ===
"%EXE%" -pipe -tpdf < "%DIR%\hello2.puml" > "%DIR%\hello2_pipe.pdf"
if errorlevel 1 goto :error
for %%f in ("%DIR%\hello2_pipe.pdf") do echo hello2_pipe.pdf: %%~zf bytes
findstr /b /c:"%%PDF-" "%DIR%\hello2_pipe.pdf" >nul
if errorlevel 1 (echo hello2_pipe.pdf does not start with %%PDF- & goto :error)

echo.
echo OK: %DIR%\hello2.pdf
if not defined NO_OPEN start "" "%DIR%\hello2.pdf"
exit /b 0

:error
echo.
echo PDF test failed.
exit /b 1
