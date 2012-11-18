#!/bin/sh
javac $(find ./src/* | grep .java$) -d out/executables/
