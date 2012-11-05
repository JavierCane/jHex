package prop.hex.gestors;


import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: javierferrer
 * Date: 29/10/12
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseModel<T>
{

	protected String subcarpeta_dades;
	private String carpeta_dades = "dat";
	private String extensio_fitxers = "dat";

	public boolean guardaElement( T element_a_guardar, String nom_element ) throws FileNotFoundException, IOException
	{
		File carpeta_a_accedir = new File( this.carpeta_dades + '/' + this.subcarpeta_dades );
		File arxiu_a_accedir = new File( this.carpeta_dades + '/' + this.subcarpeta_dades + '/' + nom_element + '.' +
		                                 this.extensio_fitxers );

		// Elimino la versio previa i creo la nova del fitxer de l'objecte per evitar concatenar objectes als fitxers
		if ( arxiu_a_accedir.exists() )
		{
			arxiu_a_accedir.delete();
		}

		carpeta_a_accedir.mkdirs(); // Creo les carpetes necessaries fins al path final del fitxer (per si de cas)
		arxiu_a_accedir.createNewFile(); // Ignoro el retorn perque ja se que previament no estava creat

		// Instanciamos los streams necesarios para escribir en el fichero
		FileOutputStream fos = new FileOutputStream( arxiu_a_accedir );
		ObjectOutputStream oos = new ObjectOutputStream( fos );

		// Escribimos en disco mediante el stream de datos abierto
		oos.writeObject( element_a_guardar );

		// Hacemos flush y cerramos el stream de datos
		oos.flush();
		oos.close();

		// Si el fitxer existeix, es de tipus arxiu y te contingut, retorno true
		if ( arxiu_a_accedir.isFile() && arxiu_a_accedir.length() > 1 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public T carregaElement( String nom_element ) throws IOException, ClassNotFoundException
	{
		// Instanciamos los streams necesarios para leer del fichero
		FileInputStream fis = new FileInputStream(
				this.carpeta_dades + '/' + this.subcarpeta_dades + '/' + nom_element + '.' + this.extensio_fitxers );
		ObjectInputStream ois = new ObjectInputStream( fis );

		// Leemos de disco mediante el stream de datos abierto
		T element_carregat = ( T ) ois.readObject();

		// Cerramos el stream de datos
		ois.close();

		return element_carregat;
	}
}
