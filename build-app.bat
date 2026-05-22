@echo off
REM Build a self-contained Windows installer (RARS-1.6.msi) using jpackage.
REM Requires JDK 17+ on PATH and (for .msi) the WiX Toolset 3.x installed
REM and on PATH (light.exe / candle.exe). For .exe installer instead, use
REM --type exe (requires Inno Setup 6 on PATH).
setlocal enableextensions
cd /d "%~dp0"

if not exist rars.jar (
    echo rars.jar not found, building it...
    call build-jar.bat || exit /b 1
)

if exist dist rmdir /s /q dist
mkdir dist
if exist build\jpackage-input rmdir /s /q build\jpackage-input
mkdir build\jpackage-input
copy /y rars.jar build\jpackage-input\ >nul

set ICON_ARG=
if exist src\images\RISC-V.ico set ICON_ARG=--icon src\images\RISC-V.ico

jpackage ^
  --type msi ^
  --name RARS ^
  --app-version 1.6 ^
  --vendor "RARS (accessible build)" ^
  --description "RISC-V Assembler and Runtime Simulator (accessible build)" ^
  --input build\jpackage-input ^
  --main-jar rars.jar ^
  --main-class rars.Launch ^
  --java-options "-Drars.accessibility=true" ^
  --win-shortcut ^
  --win-menu ^
  --win-dir-chooser ^
  %ICON_ARG% ^
  --dest dist
if errorlevel 1 exit /b 1

echo.
echo Built MSI installer in dist\
dir /b dist
exit /b 0
