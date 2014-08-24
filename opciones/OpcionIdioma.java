package opciones;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.ManipulacionElement;

/**
 * Opción que permite la configuración del idioma de la aplicación
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class OpcionIdioma extends Opcion {
	private static final long serialVersionUID = 1171;

	private String idioma;

	/**
	 * Constructor: genera una nueva opción con el idioma español por defecto.
	 */
	public OpcionIdioma() {
		this("es");
	}

	/**
	 * Constructor: genera una nueva opción con el idioma pasado por parámetro.
	 * 
	 * @param idiomaId
	 *            código de idioma
	 */
	public OpcionIdioma(String idiomaId) {
		super("OpcionIdioma");
		this.idioma = idiomaId;
	}

	/**
	 * Permite establecer el idioma.
	 * 
	 * @param idiomaId
	 *            código de idioma
	 */
	public void set(String idiomaId) {
		this.idioma = idiomaId;
	}

	/**
	 * Devuelve el idioma a utilizar.
	 * 
	 * @return idiomaId
	 *            código de idioma
	 */
	public String get() {
		return this.idioma;
	}

	@Override
	public Element getRepresentacionElement(Document d) {
		Element e = d.createElement("OpcionIdioma");

		Element e01 = d.createElement("idioma");
		e01.setAttribute("valor", this.idioma);

		e.appendChild(e01);

		return e;
	}

	@Override
	public void setValores(Element e) {
		Element elements[] = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("idioma"));
		this.idioma = elements[0].getAttribute("valor");
	}

	/**
	 * Gestiona la lectura desde fichero
	 * 
	 * @param stream Input stream para el fichero.
	 */
	private void readObject(ObjectInputStream stream) throws IOException,
	ClassNotFoundException {
		this.idioma = stream.readUTF();
	}

	/**
	 * Gestiona la escritura a fichero
	 * 
	 * @param stream Output stream para el fichero.
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.writeUTF(this.idioma);
	}
}