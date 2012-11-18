cd .\src\
for /r %%a in (*.java) do ( javac "%%a" -d ..\out\executables )
cd ..
