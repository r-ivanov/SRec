package opciones;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.ManipulacionElement;

/**
 * Opción que permite la configuración de mantenimiento de ficheros intermedios
 * usados por la aplicación
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class OpcionBorradoFicheros extends Opcion {
	private static final long serialVersionUID = 1001;
	// Cada uno de los ficheros se borrarán si la variable booleana
	// correspondiente está a true

	private boolean fichero_xml; // Fichero XML equivalente a java original
	private boolean fichero_xml_zv; // Fichero XML equivalente a java generado

	private boolean fichero_java_zv; // Fichero java generado

	private boolean fichero_class; // Fichero compilado de java original
	private boolean fichero_class_zv; // Fichero compilado de java generado

	private boolean fichero_LOG; // Fichero LOG de utilización

	/**
	 * Constructor: genera una nueva opción con los valores por defecto
	 */
	public OpcionBorradoFicheros() {
		this(true, true, true, true, true);
	}

	/**
	 * Constructor: genera una nueva opción con los valores dados con parámetros
	 * 
	 * @param a
	 *            true si el fichero XML debe ser mantenido
	 * @param b
	 *            true si el fichero XML del código Java generado debe ser
	 *            mantenido
	 * @param c
	 *            true si el fichero Java generado debe ser mantenido
	 * @param d
	 *            true si el fichero class del código Java original debe ser
	 *            mantenido
	 * @param d
	 *            true si el fichero class del código Java generado debe ser
	 *            mantenido
	 */
	public OpcionBorradoFicheros(boolean a, boolean b, boolean c, boolean d,
			boolean e) {
		super("OpcionBorradoFicheros");
		this.set(a, b, c, d, e);
	}

	/**
	 * Almacena los valores de la opción
	 * 
	 * @param a
	 *            true si el fichero XML debe ser mantenido
	 * @param b
	 *            true si el fichero XML del código Java generado debe ser
	 *            mantenido
	 * @param c
	 *            true si el fichero Java generado debe ser mantenido
	 * @param d
	 *            true si el fichero class del código Java original debe ser
	 *            mantenido
	 * @param d
	 *            true si el fichero class del código Java generado debe ser
	 *            mantenido
	 */
	public void set(boolean a, boolean b, boolean c, boolean d, boolean e) {
		this.setfXml(a);
		this.setfXmlzv(b);
		this.setfJavazv(c);
		this.setfClass(d);
		this.setfClasszv(e);
	}

	/**
	 * Almacena si el fichero XML debe ser mantenido
	 * 
	 * @param a
	 *            true si el fichero XML debe ser mantenido
	 */
	public void setfXml(boolean a) {
		this.fichero_xml = a;
	}

	/**
	 * Almacena si el fichero XML del código generado debe ser mantenido
	 * 
	 * @param a
	 *            true si el fichero XML del código generado debe ser mantenido
	 */
	public void setfXmlzv(boolean a) {
		this.fichero_xml_zv = a;
	}

	/**
	 * Almacena si el fichero Java generado debe ser mantenido
	 * 
	 * @param a
	 *            true si el fichero Java generado debe ser mantenido
	 */
	public void setfJavazv(boolean a) {
		this.fichero_java_zv = a;
	}

	/**
	 * Almacena si el fichero class del código Java original debe ser mantenido
	 * 
	 * @param a
	 *            true si el fichero class del código Java original debe ser
	 *            mantenido
	 */
	public void setfClass(boolean a) {
		this.fichero_class = a;
	}

	/**
	 * Almacena si el fichero class del código Java generado debe ser mantenido
	 * 
	 * @param a
	 *            true si el fichero class del código Java generado debe ser
	 *            mantenido
	 */
	public void setfClasszv(boolean a) {
		this.fichero_class_zv = a;
	}

	/**
	 * Almacena si el fichero LOG debe ser creado y rellenado
	 * 
	 * @param a
	 *            true si el fichero LOG debe ser creado y rellenado
	 */
	public void setLOG(boolean a) {
		this.fichero_LOG = a;
	}

	/**
	 * Devuelve si el fichero XML debe ser mantenido
	 * 
	 * @return true si el fichero class del código Java generado debe ser
	 *         mantenido
	 */
	public boolean getfXml() {
		return this.fichero_xml;
	}

	/**
	 * Devuelve si el fichero XML del código java generado debe ser mantenido
	 * 
	 * @return true si el fichero XML del código java generado debe ser
	 *         mantenido
	 */
	public boolean getfXmlzv() {
		return this.fichero_xml_zv;
	}

	/**
	 * Devuelve si el fichero XML del código java generado debe ser mantenido
	 * 
	 * @return true si el fichero XML del código java generado debe ser
	 *         mantenido
	 */
	public boolean getfJavazv() {
		return this.fichero_java_zv;
	}

	/**
	 * Devuelve si el fichero class del código java original debe ser mantenido
	 * 
	 * @return true si el fichero class del código java original debe ser
	 *         mantenido
	 */
	public boolean getfClass() {
		return this.fichero_class;
	}

	/**
	 * Devuelve si el fichero class del código java generado debe ser mantenido
	 * 
	 * @return true si el fichero class del código java generado debe ser
	 *         mantenido
	 */
	public boolean getfClasszv() {
		return this.fichero_class_zv;
	}

	/**
	 * Devuelve si el fichero LOG debe ser creado y rellenado
	 * 
	 * @return true si el fichero LOG debe ser creado y rellenado
	 */
	public boolean getLOG() {
		return this.fichero_LOG;
	}

	@Override
	public Element getRepresentacionElement(Document d) {
		Element e = d.createElement("OpcionBorradoFicheros");

		Element e01 = d.createElement("xml_original");
		e01.setAttribute("valor", "" + this.getfXml());

		Element e02 = d.createElement("xml_generado");
		e02.setAttribute("valor", "" + this.getfXmlzv());

		Element e03 = d.createElement("java_generado");
		e03.setAttribute("valor", "" + this.getfJavazv());

		Element e04 = d.createElement("class_original");
		e04.setAttribute("valor", "" + this.getfClass());

		Element e05 = d.createElement("class_generado");
		e05.setAttribute("valor", "" + this.getfClasszv());

		Element e06 = d.createElement("archivo_log");
		e06.setAttribute("valor", "" + this.getLOG());

		e.appendChild(e01);
		e.appendChild(e02);
		e.appendChild(e03);
		e.appendChild(e04);
		e.appendChild(e05);
		e.appendChild(e06);

		return e;
	}

	@Override
	public void setValores(Element e) {
		Element elements[] = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("xml_original"));
		this.setfXml(Boolean.parseBoolean(elements[0].getAttribute("valor")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("xml_generado"));
		this.setfXmlzv(Boolean.parseBoolean(elements[0].getAttribute("valor")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("java_generado"));
		this.setfJavazv(Boolean.parseBoolean(elements[0].getAttribute("valor")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("class_original"));
		this.setfClass(Boolean.parseBoolean(elements[0].getAttribute("valor")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("class_generado"));
		this.setfClasszv(Boolean.parseBoolean(elements[0].getAttribute("valor")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("archivo_log"));
		if (elements.length > 0) {
			this.setLOG(Boolean.parseBoolean(elements[0].getAttribute("valor")));
		} else {
			this.setLOG(false);
		}
	}
}