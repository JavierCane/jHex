jHex - Joc de Taula Hex
=======================

Dades del projecte
------------------

* Joc:			Hex
* Assignatura:	Projectes de Programació (PROP)
* Facultat: 	Facultat d'Informàtica de Barcelona (FIB-UPC)
* Curs:			2012-1013
* Data entrega:	2012-12-18
* Grup: 		7.3
* Integrants:
> * [Ferrer González, Javier](mailto:javier.ferrer.gonzalez@est.fib.upc.edu)
> * [Girona San Miguel, Guillermo](mailto:guillermo.girona@est.fib.upc.edu)
> * [Junyent Martín, Marc](mailto:marc.junyent@est.fib.upc.edu)
> * [Sánchez Barrera, Isaac](mailto:isaac.sanchez.barrera@est.fib.upc.edu)


Com compilar i executar
-----------------------

**Important**: Estem suposant un sistema amb JDK/JRE 1.6 i amb els programes javac, java i jar en el PATH.
Recomanem l'execució en un entorn Windows.

### Execució mitjançant el fitxer .jar ###

El programa principal es pot executar mitjançant un fitxer .jar, que es troba al subdirectori jHex dins l'arrel del
projecte. Si no el tenim, o el volem actualitzar, només ens cal executar la següent comanda (des de l'arrel del
projecte):

* Sistemes Unix:
> ``sh genera-jar-unix.sh``
* Sistemes Windows:
> ``.\genera-jar-windows.bat``

Per executar-lo, aleshores, només ens cal dirigir-nos al directori jHex i obrir el fitxer jHex.jar amb la màquina
virtual de Java.

### Execució mitjançant fitxers .class ###

> 1- Moure'ns al directori arrel del projecte:
> > ``cd /path/absolut/del/projecte/jHex/``
>
> 2- Si hem fet compilacions prèvies, esborrar el directori d'arxius binaris:
> > Sistemes Unix:
> > ``rm -rf bin``
> > 
> > Sistemes Windows:
> > ``rmdir /S /Q .\bin``
>
> 3- Creem el directori bin:
> > Sistemes Unix:
> > ``mkdir bin``
> > 
> > Sistemes Windows:
> > ``mkdir .\bin``
>
> 4- Moure'ns al directori arrel del codi font:
> > ``cd src/``
>
> 5- Compilar incloent tots els fitxers java:
> > Sistemes Unix:
> > ``javac -encoding UTF-8 -d ../bin $(find ./* | grep .java$)``
>
> > Sistemes Windows:
> > ``for /r %a in (*.java) do ( javac -encoding UTF-8 -d ../bin "%a" )``
>
> 6- Moure'ns al directori arrel del projecte:
> > ``cd ..``
>
> 7a- Si volem executar el joc al complet amb interfície gràfica:
> > ``java -Dfile.encoding=UTF-8 -classpath ./bin prop.hex.presentacio.JHex``
>
> 7b- Si volem fer testing via els drivers dels controladors mitjançant la consola:
> > ``java -Dfile.encoding=UTF-8 -classpath ./bin prop.hex.domini.controladors.drivers.PrincipalDrvr``

Explicació PrincipalDrvr
------------------------

Classe principal per realitzar tots els tests dels drivers.
Només s'ha d'executar aquesta classe i (mitjançant la tècnica de reflexió) ja s'encarrega d'anar a buscar tots els
drivers que hi hagi a la carpeta corresponent (carpeta_drivers) i llistar els seus mètodes per poder-los provar.

Estructura de fitxers
---------------------

* ``bin/``:		Arxius binaris del projecte resultants de la compilació d'aquest.
* ``dat/``:		Dades del joc (persistència a disc d'usuaris, partides i rànquing), si es fan servir els .class.
* ``doc/``:		Documentació del codi, manual del joc, classes implementades per cadascú i diagrames de classes.
* ``jHex/``:	Directori amb les dades d'ús normal del programa.
* ``img/``:		Imatges utilitzades a l'interfície gràfica.
* ``res/``:		Recursos diversos utilitzats durant el desenvolupament del projecte (preferències del IDE IntelliJ,
disseny de les pantalles i documentació d'algorismes per implementar les IAs)
* ``src/``:		Codi font del projecte.
