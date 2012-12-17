@echo off
set TAB=
	
echo Creant directori bin (si no existeix)
mkdir .\bin

echo Compilant fitxers font de src a bin
cd .\src
javac -encoding UTF-8 -d ..\bin prop\hex\presentacio\*.java prop\hex\domini\controladors\*.java prop\hex\domini\controladors\IA\*.java

echo Copiant les imatges de la interfície
cd ..\bin
xcopy /S /Y ..\img .\prop\img\

echo Creant el fitxer principal jHex.jar
jar cfe jHex.jar prop.hex.presentacio.JHex .\*
cd ..

echo Creant els directoris jHex i jHex\doc (si no existeixen)
mkdir .\jHex\doc

echo Traslladant el fitxer bin\jHex.jar al directori jHex
move /y .\bin\jHex.jar .\jHex\jHex.jar

echo Copiant la documentació al directori jHex\doc
xcopy /Y .\doc\ajuda.pdf .\jHex\doc\

echo.
echo Ja podeu executar el programa. Només cal que us traslladeu al
echo directori jHex i executeu el fitxer jHex.jar amb la màquina virtual
echo fent servir, per exemple, les comandes següents:
echo %TAB% cd .\jHex
echo %TAB% java -jar jHex.jar
