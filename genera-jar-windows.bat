rmdir /s /q .\bin\
mkdir .\bin
cd .\src\
for /r %%a in (*.java) do ( javac -encoding UTF-8 -d ..\bin "%%a" )
cd ..\bin\
jar cfe JHex.jar prop.hex.presentacio.JHex .\*
cd ..\bin\
xcopy /S ..\img\ .\prop\img\
rmdir /s /q .\prop\
cd ..
