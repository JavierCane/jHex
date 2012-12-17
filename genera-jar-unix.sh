#!/bin/sh

echo "Creant directori bin (si no existeix)"
mkdir -p bin

echo "Compilant fitxers font de src a bin"
cd src
javac -encoding UTF-8 -d ../bin $(find ./* | grep .java$)

echo "Copiant les imatges de la interfície"
cd ../bin
cp -fr ../img prop/img

echo "Creant el fitxer principal jHex.jar"
jar cfe jHex.jar prop.hex.presentacio.JHex ./*
cd ..

echo "Creant els directoris jHex i jHex/doc (si no existeixen)"
mkdir -p jHex/doc

echo "Traslladant el fitxer bin/jHex.jar al directori jHex"
mv -f bin/jHex.jar jHex/jHex.jar

echo "Copiant la documentació al directori jHex/doc"
cp -f doc/ajuda.pdf jHex/doc/ajuda.pdf

echo "\nJa podeu executar el programa. Només cal que us traslladeu al"
echo "directori jHex i executeu el fitxer jHex.jar amb la màquina virtual"
echo "fent servir, per exemple, les comandes següents:"
echo "\t cd ./jHex"
echo "\t java -jar jHex.jar"
