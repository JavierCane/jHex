package prop.hex.gestors;

import java.io.*;

/**
 * Classe abstracta i parametritzada pel tractament de fitxers.
 * Conte els metodes generics com guardar i carregar objectes del tipus insanciat desde la subcarpeta de dades
 * que especifiqui la classe que hereti d'aqui
 * <p/>
 * Date: 29/10/12
 */
public abstract class BaseGstr<T>
{

	/**
	 * Carpeta del sistema de fitxers on es guardaran totes les dades del joc
	 */
	private String carpeta_dades = "dat";

	/**
	 * Extensió dels fitxers de dades
	 */
	private String extensio_fitxers = "dat";

	/**
	 * Subcarpeta del sistema de fitxers, dins de carpeta_dades, per guardar els arxius. Especificada a les
	 * extensions de BaseGstr
	 */
	protected String subcarpeta_dades;

	/**
	 * Guarda un element_a_guardar de tipus T a la carpeta path_del_programa/carpeta_dades/subcarpeta_dades/ amb nom
	 * nom_element.extensio_fitxers
	 *
	 * @param element_a_guardar
	 * @param nom_element
	 * @return boolean true si s'ha pogut guardar correctament y el fitxer resultant es contempla com correcte,
	 *         false altrament
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean guardaElement( T element_a_guardar, String nom_element ) throws FileNotFoundException, IOException
	{
		File carpeta_a_accedir = new File( this.carpeta_dades + '/' + this.subcarpeta_dades );
		File arxiu_a_accedir = new File( this.carpeta_dades + '/' + this.subcarpeta_dades + '/' + nom_element + '.' +
		                                 this.extensio_fitxers );

		// Elimino la versio previa i genero la nova del fitxer de l'objecte per evitar concatenar objectes als fitxers
		if ( arxiu_a_accedir.exists() )
		{
			arxiu_a_accedir.delete();
		}

		carpeta_a_accedir.mkdirs(); // Creo les carpetes necessaries fins al path final del fitxer (per si de cas)
		arxiu_a_accedir.createNewFile(); // Ignoro el retorn del metode perque ja se que previament no estava creat

		// Instanciem els streams necessaris per escriure en el fitxer
		FileOutputStream fos = new FileOutputStream( arxiu_a_accedir );
		ObjectOutputStream oos = new ObjectOutputStream( fos );

		oos.writeObject( element_a_guardar );
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

	/**
	 * Carrega un element de tipus T amb nom nom_element.extensio_fitxers de la carpeta
	 * path_del_programa/carpeta_dades/subcarpeta_dades/
	 *
	 * @param nom_element
	 * @return l'objecte de l'element carregat
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public T carregaElement( String nom_element ) throws IOException, ClassNotFoundException, NullPointerException
	{
		T element_carregat = null;

		if ( !existeixElement( nom_element ) )
		{
			throw new FileNotFoundException(
					"Fitxer no trobat a la ruta: " + this.carpeta_dades + '/' + this.subcarpeta_dades + '/' +
					nom_element + '.' + this.extensio_fitxers );
		}
		else
		{
			// Instanciem els streams necessaris per accedir als fitxers
			FileInputStream fis =
					new FileInputStream( this.carpeta_dades + '/' + this.subcarpeta_dades + '/' + nom_element + '.' +
					                     this.extensio_fitxers );
			ObjectInputStream ois = new ObjectInputStream( fis );

			// Llegim de disc l'objecte de tipus T mitjancant l'stream de dades obert
			element_carregat = ( T ) ois.readObject();

			ois.close();
		}

		return element_carregat;
	}


	/**
	 * Comprova si un determinat element existeix
	 *
	 * @param nom_element
	 * @return boolean Dependenent de si existeix o no l'element buscat tornarà true o false.
	 */
	public boolean existeixElement( String nom_element )
	{
		File carpeta_a_accedir = new File( this.carpeta_dades + '/' + this.subcarpeta_dades );
		File arxiu_a_accedir = new File( this.carpeta_dades + '/' + this.subcarpeta_dades + '/' + nom_element + '.' +
		                                 this.extensio_fitxers );

		if ( arxiu_a_accedir.exists() )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
