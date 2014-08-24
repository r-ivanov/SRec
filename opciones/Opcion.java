package opciones;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Representa una opción modificable y que se almacena en fichero
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public abstract class Opcion implements Serializable {

	private static final long serialVersionUID = 1000;
	private String nombre;

	/**
	 * Constructor: crea una nueva opción vacía
	 */
	public Opcion() {
		this("Sin nombre");
	}

	/**
	 * Constructor: crea una nueva opción con un determinado nombre
	 * 
	 * @param nombre
	 *            nombre para la acción
	 */
	public Opcion(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el nombre de la opción
	 * 
	 * @return nombre de la operación
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Permite almacenar en el documento xml los valores establecidos para la
	 * opción.
	 * 
	 * @param d
	 *            Documento XML.
	 * 
	 * @return Elemento que contiene las opciones.
	 */
	public abstract Element getRepresentacionElement(Document d);

	/**
	 * Permite cargar los valores de la opción dado un elemento xml que las
	 * contiene.
	 * 
	 * @param e
	 *            Elemento xml que contiene los valores de las opciones.
	 */
	public abstract void setValores(Element e);

}