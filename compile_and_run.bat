@echo off
echo =========================================
echo Weather Information App Builder and Runner
echo =========================================

if not exist bin (
    mkdir bin
)

echo Compiling Java source files...
javac -d bin src\model\*.java src\util\*.java src\api\*.java src\gui\*.java src\WeatherApp.java
if %errorlevel% neq 0 (
    echo.
    echo Compilation failed! Please check your Java setup.
    pause
    exit /b %errorlevel%
)

echo Compilation successful!
echo.
echo Running Weather Information App...
java -cp bin WeatherApp

pause
