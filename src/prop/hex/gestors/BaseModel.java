package prop.hex.gestors;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: javierferrer
 * Date: 29/10/12
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class BaseModel<T>
{

	protected String subcarpeta_dades;
	private String carpeta_dades = "dat";
	private String extensio_fitxers = "dat";

	public void guardaElement( T element_a_guardar, String nom_element ) throws FileNotFoundException, IOException
	{
		// Instanciamos los streams necesarios para escribir en el fichero
		FileOutputStream fos = new FileOutputStream(
				this.carpeta_dades + '/' + this.subcarpeta_dades + '/' + nom_element + '.' + this.extensio_fitxers );
		ObjectOutputStream oos = new ObjectOutputStream( fos );

		// Escribimos en disco mediante el stream de datos abierto
		oos.writeObject( element_a_guardar );

		// Hacemos flush y cerramos el stream de datos
		oos.flush();
		oos.close();
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
