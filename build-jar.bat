@echo off
REM Build rars.jar on Windows. Requires JDK 17+ on PATH and the JSoftFloat
REM submodule initialised (git submodule update --init).
setlocal enableextensions
cd /d "%~dp0"

if not exist src\jsoftfloat\Environment.java (
    echo It looks like JSoftFloat is not cloned. Run: git submodule update --init
    exit /b 1
)

if exist build rmdir /s /q build
mkdir build

REM Compile all .java under src into build\
dir /s /b src\*.java > build\sources.txt
javac -d build @build\sources.txt
if errorlevel 1 exit /b 1

REM Copy non-Java resources preserving directory structure under build\
for /f "delims=" %%F in ('dir /s /b /a-d src ^| findstr /v /i "\.java$"') do (
    call :copyrel "%%F"
)

REM Flatten build\src\* into build\
xcopy /e /y /q build\src\* build\ >nul
rmdir /s /q build\src

copy /y README.md build\ >nul
copy /y License.txt build\ >nul

pushd build
jar cfm ..\rars.jar META-INF\MANIFEST.MF *
popd

echo Built rars.jar
exit /b 0

:copyrel
set "FULL=%~1"
set "REL=%FULL:*\src\=%"
set "DEST=build\src\%REL%"
for %%D in ("%DEST%") do if not exist "%%~dpD" mkdir "%%~dpD"
copy /y "%FULL%" "%DEST%" >nul
exit /b 0
