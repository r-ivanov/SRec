package opciones;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.ManipulacionElement;

/**
 * Opción que permite la configuración de mantenimiento de ficheros recientes
 * usados por la aplicación
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class OpcionFicherosRecientes extends Opcion {
	private static final long serialVersionUID = 1003;

	private static final int TAMANO = 8;
	// Direcciones de Ficheros
	private String ficheros[];

	// Path último fichero Java
	private String dir = ".\\";

	// Path último fichero XML
	private String dirXML = ".\\";

	/**
	 * Constructor: crea una nueva opción vacía
	 */
	public OpcionFicherosRecientes() {
		this(".\\", ".\\");
	}

	/**
	 * Constructor: crea una nueva opción con el directorio dado
	 * 
	 * @param dir
	 *            directorio en el que se leyó/guardó el último fichero por
	 *            parte de la aplicación
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
	 * Constructor: crea una nueva opción con los directorios dados
	 * 
	 * @param dir
	 *            directorio en el que se leyó/guardó el último fichero JAVA por
	 *            parte de la aplicación
	 * @param dirXML
	 *            directorio en el que se leyó/guardó el último fichero XML por
	 *            parte de la aplicación
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
	 * Constructor: crea una nueva opción con los nombres de ficheros usados
	 * recientemente
	 * 
	 * @param ficheros
	 *            nombres de ficheros usados recientemente por parte de la
	 *            aplicación
	 */
	public OpcionFicherosRecientes(String ficheros[]) {
		super("FicherosRecientes");
		this.ficheros = new String[TAMANO];
		this.reinicializarFicheros();
		this.setFicheros(ficheros);
	}

	/**
	 * Constructor: crea una nueva opción con los nombres de ficheros usados
	 * recientemente
	 * 
	 * @param ficheros
	 *            nombres de ficheros usados recientemente por parte de la
	 *            aplicación
	 * @param dir
	 *            directorio en el que se leyó el último fichero por parte de la
	 *            aplicación
	 */
	public OpcionFicherosRecientes(String ficheros[], String dir) {
		super("FicherosRecientes");
		this.setDir(dir);
		this.ficheros = new String[TAMANO];
		this.reinicializarFicheros();
		this.setFicheros(ficheros);
	}

	/**
	 * Asigna el directorio del último fichero usado por parte de la aplicación
	 * 
	 * @param dir
	 *            directorio en el que se leyó el último fichero por parte de la
	 *            aplicación
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * Asigna el directorio del último fichero usado por parte de la aplicación
	 * 
	 * @param dir
	 *            directorio en el que se leyó el último fichero por parte de la
	 *            aplicación
	 */
	public void setDirXML(String dirXML) {
		this.dirXML = dirXML;
	}

	/**
	 * Asigna los nombres de ficheros abiertos recientemente por la aplicación
	 * 
	 * @param ficheros
	 *            nombres de ficheros usados recientemente por parte de la
	 *            aplicación
	 */
	public void setFicheros(String ficheros[]) {
		for (int i = 0; i < TAMANO; i++) {
			this.ficheros[i] = ficheros[i];
		}
	}

	/**
	 * Devuelve el directorio del último fichero usado por parte de la
	 * aplicación
	 * 
	 * @return directorio del último fichero usado por parte de la aplicación
	 */
	public String getDir() {
		return this.dir;
	}

	/**
	 * Devuelve el directorio del último fichero usado por parte de la
	 * aplicación
	 * 
	 * @return directorio del último fichero usado por parte de la aplicación
	 */
	public String getDirXML() {
		return this.dirXML;
	}

	/**
	 * Devuelve los nombres de ficheros abiertos recientemente por la aplicación
	 * 
	 * @return nombres de ficheros abiertos recientemente por la aplicación
	 */
	public String[] getFicheros() {
		return this.ficheros;
	}

	/**
	 * Devuelve un nombre de fichero abierto recientemente por la aplicación
	 * 
	 * @param i
	 *            numero de fichero del que queremos extraer el nombre
	 * @return ombre de fichero abierto recientemente por la aplicación
	 */
	public String getFichero(int i) {
		return this.ficheros[i];
	}

	/**
	 * Añade un fichero reciente a la lista de fichers recientes, eliminando el
	 * más antiguo de ellos
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
	 * Comprueba si la lista de ficheros recientes está llena
	 * 
	 * @return true si la lista de ficheros recientes está llena
	 */
	private boolean lleno() {
		return this.primerVacio() == -1;
	}

	/**
	 * Reemplaza el fichero más antiguo por el fichero más reciente, manteniendo
	 * el orden de antigüedad
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
	 * Devuelve la primera posición vacía de la lista
	 * 
	 * @return primera posición vacía de la lista
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
	 * Borra la lista de fiheros recientes, dejando las posiciones vacías
	 */
	private void reinicializarFicheros() {
		this.ficheros = new String[TAMANO];
		for (int i = 0; i < this.ficheros.length; i++) {
			this.ficheros[i] = "<>";
		}
	}

	/**
	 * Devuelve el número de nombres de ficheros recientes que hay actualmente
	 * guardados en la lista
	 * 
	 * @return número de nombres de ficheros recientes que hay actualmente
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