mkdir .\bin
cd .\src
javac -encoding UTF-8 -d ..\bin prop\hex\presentacio\*.java prop\hex\domini\controladors\*.java prop\hex\domini\controladors\IA\*.java
cd ..\bin
xcopy /S /Y ..\img .\prop\img\
jar cfe jHex.jar prop.hex.presentacio.JHex .\*
mkdir ..\jHex
xcopy /Y jHex.jar ..\jHex\
del /F jHex.jar
cd ..
