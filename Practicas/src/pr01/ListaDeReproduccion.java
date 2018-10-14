package pr01;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


/** Clase para crear instancias como listas de reproducci�n,
 * que permite almacenar listas de ficheros con posici�n de �ndice
 * (al estilo de un array / arraylist)
 * con marcas de error en los ficheros y con m�todos para cambiar la posici�n
 * de los elementos en la lista, borrar elementos y a�adir nuevos.
 */
/**
 * @author javil
 *
 */
public class ListaDeReproduccion implements ListModel<String> {
	ArrayList<File> ficherosLista;     // ficheros de la lista de reproducci�n
	int ficheroEnCurso = -1;           // Fichero seleccionado (-1 si no hay ninguno seleccionado)
	private static Logger logger = Logger.getLogger( ListaDeReproduccion.class.getName());
	
	private static final boolean ANYADIR_A_FIC_LOG = false;  // poner true para no sobreescribir 
	static {  
		try {   
			logger.addHandler(new FileHandler(ListaDeReproduccion.class.getName()+".log.xml", ANYADIR_A_FIC_LOG ));  
			} catch (SecurityException | IOException e) {   
				logger.log( Level.SEVERE, "Error en creaci�n fichero log" );  
				} 
		} 
	
	public ListaDeReproduccion() {
		ficherosLista = new ArrayList<File>();
	}
	
	/** Intercambia la posicion de 2 ficheros en la lista
	 * No hace nada si la posicion introducida es erronea
	 * @param posi1 
	 * @param posi2
	 */
	public void intercambia (int posi1, int posi2 ) {
		if(posi1 >= 0 && posi1 < ficherosLista.size() && posi2 >= 0 && posi2 < ficherosLista.size()) {
			File fMover = ficherosLista.get(posi1);
			File fMovido = ficherosLista.get(posi2);
			ficherosLista.set(posi2 , fMover);
			ficherosLista.set(posi1 , fMovido);
		}
	}
	
	
	/** Devuelve el tama�o de la lista de videos
	 * @return int del tama�o de la lista
	 */
	public int size() {
		return ficherosLista.size();
	}
	
	/** Introduce un fichero al final de la lista
	 * @param f fichero a introducir
	 */
	public void add( File f) {
		ficherosLista.add(f);
	}
	
	/** Elimina un elemento dada su posici�n
	 * @param posi posicion del elemento a eliminar
	 */
	public void removeFic( int posi ) {
		ficherosLista.remove(posi);
	}
	
	/** Limpia totalmente la lista de ficheros
	 */
	public void clear() {
		ficherosLista.clear();
	}
	
	
	/** Devuelve uno de los ficheros de la lista
	 * @param posi	Posici�n del fichero en la lista (de 0 a size()-1)
	 * @return	Devuelve el fichero en esa posici�n
	 * @throws IndexOutOfBoundsException	Si el �ndice no es v�lido
	 */
	public File getFic( int posi ) throws IndexOutOfBoundsException {
		return ficherosLista.get( posi );
	}	

	/** A�ade a la lista de reproducci�n todos los ficheros que haya en la 
	 * carpeta indicada, que cumplan el filtro indicado.
	 * Si hay cualquier error, la lista de reproducci�n queda solo con los ficheros
	 * que hayan podido ser cargados de forma correcta.
	 * @param carpetaFicheros	Path de la carpeta donde buscar los ficheros
	 * @param filtroFicheros	Filtro del formato que tienen que tener los nombres de
	 * 							los ficheros para ser cargados.
	 * 							String con cualquier letra o d�gito. Si tiene un asterisco
	 * 							hace referencia a cualquier conjunto de letras o d�gitos.
	 * 							Por ejemplo p*.* hace referencia a cualquier fichero de nombre
	 * 							que empiece por p y tenga cualquier extensi�n.
	 * @return	N�mero de ficheros que han sido a�adidos a la lista
	 */
	public int add(String carpetaFicheros, String filtroFicheros) {
		logger.log(Level.INFO, "A�adiendo ficheros con filtro " + filtroFicheros ); 
		int ret = 0;
		
		filtroFicheros = filtroFicheros.replaceAll( "\\." , "\\\\." );  // Pone el s�mbolo de la expresi�n regular \. donde figure un .
		filtroFicheros = filtroFicheros.replaceAll( "\\*" , ".*" ); //Pone .* donde aparezca un *
		Pattern pat = Pattern.compile( filtroFicheros );
		
		File carpeta = new File(carpetaFicheros);
		
		if(carpeta.isDirectory()) {
			for( File f : carpeta.listFiles() ) { 
				logger.log( Level.FINE, "Procesando fichero " + f.getName() );
				if (pat.matcher(f.getName()).matches()) {
					logger.log(Level.INFO, "A�adiendo ficheros con filtro " + filtroFicheros );
					ficherosLista.add(f);
					ret += 1;
				}else {
					logger.log(Level.SEVERE, "Fichero no corresponde al filtro " + filtroFicheros);
				}
			}
		}
		return ret;
	}
	
	
	//
	// M�todos de selecci�n
	//
	
	/** Seleciona el primer fichero de la lista de reproducci�n
	 * @return	true si la selecci�n es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irAPrimero() {
		ficheroEnCurso = 0;  // Inicia
		if (ficheroEnCurso>=ficherosLista.size()) {
			ficheroEnCurso = -1;  // Si no se encuentra, no hay selecci�n
			return false;  // Y devuelve error
		}
		return true;
	}
	
	/** Seleciona el �ltimo fichero de la lista de reproducci�n
	 * @return	true si la selecci�n es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irAUltimo() {
		ficheroEnCurso = ficherosLista.size()-1;  // Inicia al final
		if (ficheroEnCurso==-1) {  // Si no se encuentra, no hay selecci�n
			return false;  // Y devuelve error
		}
		return true;
	}

	/** Seleciona el anterior fichero de la lista de reproducci�n
	 * @return	true si la selecci�n es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irAAnterior() {
		if (ficheroEnCurso>=0) ficheroEnCurso--;
		if (ficheroEnCurso==-1) {  // Si no se encuentra, no hay selecci�n
			return false;  // Y devuelve error
		}
		return true;
	}

	/** Seleciona el siguiente fichero de la lista de reproducci�n
	 * @return	true si la selecci�n es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irASiguiente() {
		ficheroEnCurso++;
		if (ficheroEnCurso>=ficherosLista.size()) {
			ficheroEnCurso = -1;  // Si no se encuentra, no hay selecci�n
			return false;  // Y devuelve error
		}
		return true;
	}

	/** Devuelve el fichero seleccionado de la lista
	 * @return	Posici�n del fichero seleccionado en la lista de reproducci�n (0 a n-1), -1 si no lo hay
	 */
	public int getFicSeleccionado() {
		return ficheroEnCurso;
	}
	
	/** Selecciona un fichero aleatorio de la lista de reproducci�n.   
	 * @return true si la selecci�n es correcta, false si hay error y no se puede seleccionar   
	 */ 
	public boolean irARandom() {
		Random r = new Random();
		
		try {
			int selecionado = r.nextInt(ficherosLista.size());
			ficherosLista.get(selecionado);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	//
	// M�todos de DefaultListModel
	//
	
	@Override
	public int getSize() {
		return ficherosLista.size();
	}

	@Override
	public String getElementAt(int index) {
		return ficherosLista.get(index).getName();
	}

		// Escuchadores de datos de la lista
		ArrayList<ListDataListener> misEscuchadores = new ArrayList<>();
	@Override
	public void addListDataListener(ListDataListener l) {
		misEscuchadores.add( l );
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		misEscuchadores.remove( l );
	}
	
	// Llamar a este m�todo cuando se a�ada un elemento a la lista
	// (Utilizado para avisar a los escuchadores de cambio de datos de la lista)
	private void avisarAnyadido( int posi ) {
		for (ListDataListener ldl : misEscuchadores) {
			ldl.intervalAdded( new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, posi, posi ));
		}
	}
}
