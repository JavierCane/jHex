#!/bin/sh

rm -fr bin
mkdir bin
cd src
javac -encoding UTF-8 -d ../bin $(find ./* | grep .java$)
cd ../bin
cp -r ../img prop/img
jar cfe JHex.jar prop.hex.presentacio.JHex ./*
rm -fr prop
cd ..

