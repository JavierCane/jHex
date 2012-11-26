Joc de taula Hex
========

Dades del projecte
--------

* Joc:			Hex
* Assignatura:	Projectes de Programació (PROP)
* Facultat: 	Facultat d'Informàtica de Barcelona (FIB-UPC)
* Curs:			2012-1013
* Grup: 		7.3
* Integrants:
> * [Ferrer González, Javier](mailto:javier.ferrer.gonzalez@est.fib.upc.edu)
> * [Girona San Miguel, Guillermo](mailto:guillermo.girona@est.fib.upc.edu)
> * [Junyent Martín, Marc](mailto:marc.junyent@est.fib.upc.edu)
> * [Sánchez Barrera, Isaac](mailto:isaac.sanchez.barrera@est.fib.upc.edu)


Com compilar i executar
--------

1. Moure'ns al directori arrel del projecte:
> ``cd /path/absolut/del/projecte/jHex/``
2. Eliminar possibles arxius de compilacions prèvies:
> ``rm $(find ./src/* | grep .class$)``
3. Moure'ns al directori arrel del codi font:
> ``cd /path/absolut/del/projecte/jHex/src/``
4. Compilar incloent tots els fitxers dels drivers:
> ``javac prop/hex/domini/controladors/drivers/*.java``
5. Executar el driver principal:
> ``java prop.hex.domini.controladors.drivers.PrincipalDrvr``

Explicació PrincipalDrvr
--------

Classe principal per realitzar tots els tests dels drivers.
Només s'ha d'executar aquesta classe i (mitjançant la tècnica de reflexió) ja s'encarrega d'anar a buscar tots els
drivers que hi hagi a la carpeta corresponent (carpeta_drivers) i llistar els seus mètodes per poder-los provar.

Estructura de fitxers
--------

* ``dat/``:		Dades del joc (persistència d'usuaris, partides, etc.).
* ``doc/``:		Documentació del codi, classes implementades per cadascú i diagrama estàtic del domini.
* ``res/``:		Recursos diversos utilitzats al projecte (preferències del IDE IntelliJ, disseny de les pantalles, etc.)
* ``src/``:		Codi font del projecte.
