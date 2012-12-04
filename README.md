jHex - Joc de Taula Hex
========

Dades del projecte
--------

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
--------

1- Moure'ns al directori arrel del projecte:
> ``cd /path/absolut/del/projecte/jHex/``

2a- Si no hem fet compilacions prèvies, crear el directori de arxius binaris:
> ``mkdir bin``

2b- Si hem fet compilacions prèvies, eliminar arxius binaris:
> ``rm -rf bin/*``

3- Moure'ns al directori arrel del codi font:
> ``cd src/``

4- Compilar incloent tots els fitxers java:
> ``javac -d ../bin $(find ./* | grep .java$)``

5- Moure'ns al directori arrel del del projecte:
> ``cd ..``

6a- Si volem executar el joc al complet amb interfície gràfica:
> ``java -classpath ./bin prop.hex.presentacio.JHex``

6b- Si volem fer testing via els drivers dels controladors mitjançant la consola:
> ``java -classpath ./bin prop.hex.domini.controladors.drivers.PrincipalDrvr``

Explicació PrincipalDrvr
--------

Classe principal per realitzar tots els tests dels drivers.
Només s'ha d'executar aquesta classe i (mitjançant la tècnica de reflexió) ja s'encarrega d'anar a buscar tots els
drivers que hi hagi a la carpeta corresponent (carpeta_drivers) i llistar els seus mètodes per poder-los provar.

Estructura de fitxers
--------

* ``bin/``:		Arxius binaris del projecte resultants de la compilació d'aquest.
* ``dat/``:		Dades del joc (persistència a disc d'usuaris, partides i rànquing).
* ``doc/``:		Documentació del codi, classes implementades per cadascú i diagrama estàtic del domini.
* ``img/``:		Imatges utilitzades a l'interfície gràfica.
* ``res/``:		Recursos diversos utilitzats durant el desenvolupament del projecte (preferències del IDE IntelliJ,
disseny de les pantalles i documentació d'algorismes per implementar les IAs)
* ``src/``:		Codi font del projecte.
