@echo off
echo Building Odin Eclipse Plugin...
set MAVEN_HOME=I:\ide\maven
call "%MAVEN_HOME%\bin\mvn" clean package
if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful!
    echo Update site available at: site\target\repository
    echo.
    echo To install:
    echo 1. In Eclipse: Help ^> Install New Software ^> Add ^> Local
    echo 2. Select: site\target\repository
) else (
    echo Build failed!
)
