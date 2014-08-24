package opciones;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.ManipulacionElement;

/**
 * Opci�n que permite la configuraci�n de mantenimiento de ficheros recientes
 * usados por la aplicaci�n
 * 
 * @author Antonio P�rez Carrasco
 * @version 2006-2007
 */
public class OpcionFicherosRecientes extends Opcion {
	private static final long serialVersionUID = 1003;

	private static final int TAMANO = 8;
	// Direcciones de Ficheros
	private String ficheros[];

	// Path �ltimo fichero Java
	private String dir = ".\\";

	// Path �ltimo fichero XML
	private String dirXML = ".\\";

	/**
	 * Constructor: crea una nueva opci�n vac�a
	 */
	public OpcionFicherosRecientes() {
		this(".\\", ".\\");
	}

	/**
	 * Constructor: crea una nueva opci�n con el directorio dado
	 * 
	 * @param dir
	 *            directorio en el que se ley�/guard� el �ltimo fichero por
	 *            parte de la aplicaci�n
	 */
	public OpcionFicherosRecientes(String dir) {
		super("OpcionFicherosRecientes");
		if (dir.length() > 3) {
			this.setDir(dir);
		}
		this.ficheros = new String[TAMANO];
		this.reinicializarFicheros();
	}

	/**
	 * Constructor: crea una nueva opci�n con los directorios dados
	 * 
	 * @param dir
	 *            directorio en el que se ley�/guard� el �ltimo fichero JAVA por
	 *            parte de la aplicaci�n
	 * @param dirXML
	 *            directorio en el que se ley�/guard� el �ltimo fichero XML por
	 *            parte de la aplicaci�n
	 */
	public OpcionFicherosRecientes(String dir, String dirXML) {
		super("OpcionFicherosRecientes");
		if (dir.length() > 3) {
			this.setDir(dir);
		}
		if (dirXML.length() > 3) {
			this.setDirXML(dirXML);
		}
		this.ficheros = new String[TAMANO];
		this.reinicializarFicheros();
	}

	/**
	 * Constructor: crea una nueva opci�n con los nombres de ficheros usados
	 * recientemente
	 * 
	 * @param ficheros
	 *            nombres de ficheros usados recientemente por parte de la
	 *            aplicaci�n
	 */
	public OpcionFicherosRecientes(String ficheros[]) {
		super("FicherosRecientes");
		this.ficheros = new String[TAMANO];
		this.reinicializarFicheros();
		this.setFicheros(ficheros);
	}

	/**
	 * Constructor: crea una nueva opci�n con los nombres de ficheros usados
	 * recientemente
	 * 
	 * @param ficheros
	 *            nombres de ficheros usados recientemente por parte de la
	 *            aplicaci�n
	 * @param dir
	 *            directorio en el que se ley� el �ltimo fichero por parte de la
	 *            aplicaci�n
	 */
	public OpcionFicherosRecientes(String ficheros[], String dir) {
		super("FicherosRecientes");
		this.setDir(dir);
		this.ficheros = new String[TAMANO];
		this.reinicializarFicheros();
		this.setFicheros(ficheros);
	}

	/**
	 * Asigna el directorio del �ltimo fichero usado por parte de la aplicaci�n
	 * 
	 * @param dir
	 *            directorio en el que se ley� el �ltimo fichero por parte de la
	 *            aplicaci�n
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * Asigna el directorio del �ltimo fichero usado por parte de la aplicaci�n
	 * 
	 * @param dir
	 *            directorio en el que se ley� el �ltimo fichero por parte de la
	 *            aplicaci�n
	 */
	public void setDirXML(String dirXML) {
		this.dirXML = dirXML;
	}

	/**
	 * Asigna los nombres de ficheros abiertos recientemente por la aplicaci�n
	 * 
	 * @param ficheros
	 *            nombres de ficheros usados recientemente por parte de la
	 *            aplicaci�n
	 */
	public void setFicheros(String ficheros[]) {
		for (int i = 0; i < TAMANO; i++) {
			this.ficheros[i] = ficheros[i];
		}
	}

	/**
	 * Devuelve el directorio del �ltimo fichero usado por parte de la
	 * aplicaci�n
	 * 
	 * @return directorio del �ltimo fichero usado por parte de la aplicaci�n
	 */
	public String getDir() {
		return this.dir;
	}

	/**
	 * Devuelve el directorio del �ltimo fichero usado por parte de la
	 * aplicaci�n
	 * 
	 * @return directorio del �ltimo fichero usado por parte de la aplicaci�n
	 */
	public String getDirXML() {
		return this.dirXML;
	}

	/**
	 * Devuelve los nombres de ficheros abiertos recientemente por la aplicaci�n
	 * 
	 * @return nombres de ficheros abiertos recientemente por la aplicaci�n
	 */
	public String[] getFicheros() {
		return this.ficheros;
	}

	/**
	 * Devuelve un nombre de fichero abierto recientemente por la aplicaci�n
	 * 
	 * @param i
	 *            numero de fichero del que queremos extraer el nombre
	 * @return ombre de fichero abierto recientemente por la aplicaci�n
	 */
	public String getFichero(int i) {
		return this.ficheros[i];
	}

	/**
	 * A�ade un fichero reciente a la lista de fichers recientes, eliminando el
	 * m�s antiguo de ellos
	 * 
	 * @param fichero
	 *            nombre del nuevo fichero reciente
	 */
	public void addFichero(String fichero) {
		if (this.lleno()) {
			this.reemplazoFicheros(fichero);
		} else {
			this.ficheros[this.primerVacio()] = fichero;
		}
	}

	/**
	 * Comprueba si la lista de ficheros recientes est� llena
	 * 
	 * @return true si la lista de ficheros recientes est� llena
	 */
	private boolean lleno() {
		return this.primerVacio() == -1;
	}

	/**
	 * Reemplaza el fichero m�s antiguo por el fichero m�s reciente, manteniendo
	 * el orden de antig�edad
	 * 
	 * @param fichero
	 *            nombre del nuevo fichero reciente
	 */
	private void reemplazoFicheros(String fichero) {
		for (int i = 1; i < this.ficheros.length - 1; i++) {
			this.ficheros[i - 1] = this.ficheros[i];
		}
		this.ficheros[this.ficheros.length - 1] = fichero;
	}

	/**
	 * Devuelve la primera posici�n vac�a de la lista
	 * 
	 * @return primera posici�n vac�a de la lista
	 */
	private int primerVacio() {
		for (int i = 0; i < this.ficheros.length; i++) {
			if (this.ficheros[i].length() < 4) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Borra la lista de fiheros recientes, dejando las posiciones vac�as
	 */
	private void reinicializarFicheros() {
		this.ficheros = new String[TAMANO];
		for (int i = 0; i < this.ficheros.length; i++) {
			this.ficheros[i] = "<>";
		}
	}

	/**
	 * Devuelve el n�mero de nombres de ficheros recientes que hay actualmente
	 * guardados en la lista
	 * 
	 * @return n�mero de nombres de ficheros recientes que hay actualmente
	 *         guardados en la lista
	 */
	public int numFicherosGuardados() {
		if (this.primerVacio() != -1) {
			return this.primerVacio();
		} else {
			return this.ficheros.length;
		}
	}

	@Override
	public Element getRepresentacionElement(Document d) {
		Element e = d.createElement("OpcionFicherosRecientes");

		Element e01 = d.createElement("dir");
		e01.setAttribute("valor", this.getDir());

		Element e02 = d.createElement("dirxml");
		e02.setAttribute("valor", this.getDirXML());

		e.appendChild(e01);
		e.appendChild(e02);

		return e;
	}

	@Override
	public void setValores(Element e) {
		Element elements[] = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("dir"));
		this.setDir(elements[0].getAttribute("valor"));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("dirxml"));
		this.setDirXML(elements[0].getAttribute("valor"));
	}
}