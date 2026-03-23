@echo off
echo Starting FinTrack Finance Tracker...
java -jar finance-tracker.jar
if errorlevel 1 (
    echo.
    echo Error launching. Make sure Java 17+ is installed.
    echo Download Java: https://adoptium.net
    pause
)
