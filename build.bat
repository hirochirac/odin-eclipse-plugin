@echo off
echo Building Odin Eclipse Plugin...

if not exist bin mkdir bin
if not exist dist mkdir dist

echo Copying resources...
xcopy /E /I /Y icons bin\icons > nul
copy plugin.xml bin\ > nul
xcopy /E /I /Y META-INF bin\META-INF > nul

echo Creating JAR...
cd bin
jar cvfm ..\dist\com.odin.eclipse_1.0.0.jar META-INF\MANIFEST.MF . > nul
cd ..

echo Done! JAR created at dist\com.odin.eclipse_1.0.0.jar
