package opciones;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.ManipulacionElement;
import utilidades.ServiciosString;

/**
 * Gestiona la carga y almacenaiento de opciones en fichero.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class GestorOpciones {

	private Opcion opcion;
	private static String direccion = "datos/opciones.xml";
	private static String dirdefecto = "datos/opdefecto.xml";

	private static String[] clasesOpciones = { "OpcionBorradoFicheros",
			"OpcionConfVisualizacion", "OpcionFicherosRecientes",
			"OpcionIdioma", "OpcionMVJava", "OpcionOpsVisualizacion",
			"OpcionTipoGrafico", "OpcionVistas" };

	private Document documento;
	
	/**
	 * Determina si actualmente existe el archivo de opciones.
	 * 
	 * @return true si el archivo existe, false en caso contrario.
	 */
	public static boolean existeArchivoOpciones() {
		File file = new File(direccion);
		return (file.exists());
	}
	
	/**
	 * Determina si actualmente existe el archivo de opciones por defecto.
	 * 
	 * @return true si el archivo existe, false en caso contrario.
	 */
	public static boolean existeArchivoOpcionesPorDefecto() {
		File file = new File(dirdefecto);
		return (file.exists());
	}
	
	/**
	 * Devuelve el path del archivo de opciones.
	 * 
	 * @return Path del archivo de opciones.
	 */
	public static String getNombreArchivoOpciones() {
		return direccion;
	}
	
	/**
	 * Devuelve el path del archivo de opciones por defecto.
	 * 
	 * @return Path del archivo de opciones por defecto.
	 */
	public static String getNombreArchivoOpDefecto() {
		return dirdefecto;
	}
	
	/**
	 * Crea el archivo de opciones.
	 */
	public void crearArchivo() {
		// Generamos un documento nuevo
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
			this.documento = builder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Element eInicial = this.documento.createElement("Opciones");
		this.documento.appendChild(eInicial);

		// Recorremos el directorio de la aplicacion para buscar sus propios
		// .class que implementen opciones
		// así no tendremos que retocar esta clase cada vez que insertemos una
		// opción nueva a la aplicación

		// Nos situamos sobre directorio de la aplicación
		File directorioAplicacion = null;
		try {
			directorioAplicacion = new File(".\\opciones");
		} catch (Exception exc) {
		}

		// Listamos todos sus ficheros
		File clases[] = directorioAplicacion.listFiles();

		// Recorremos el array para detectar las clases que nos interesan
		for (int i = 0; i < clases.length; i++) {
			if (clases[i].getName().contains(".class")
					&& clases[i].getName().contains("Opcion")
					&& clases[i].getName().indexOf("Opcion") == 0
					&& clases[i].getName().length() > 12) {
				Class clase = null;
				Object opcion = null;
				try {
					clase = Class.forName("opciones."
							+ clases[i].getName().replace(".class", ""));
					opcion = clase.newInstance();

				} catch (java.lang.ClassNotFoundException cnfe) {
					System.out
							.println("GestorOpciones.crearArchivo ClassNotFoundException");
				} catch (java.lang.InstantiationException ie) {
					System.out
							.println("GestorOpciones.crearArchivo InstantiationException");
				} catch (java.lang.IllegalAccessException iae) {
					System.out
							.println("GestorOpciones.crearArchivo IllegalAccessException");
				}

				if (opcion == null) {
					System.out
							.println("GestorOpciones.crearArchivo opcion=null");
				}

				this.anadirOpcion(((Opcion) opcion)
						.getRepresentacionElement(this.documento));
			}
		}
	}
	
	/**
	 * Añade una opción al fichero de opciones. Si la opción existía
	 * previamente, será reemplazada con el nuevo valor.
	 * 
	 * @param e Elemento xml para la opción.
	 */
	private void anadirOpcion(Element e) {
		// Añadimos la nueva opción, sustituyéndola si ya existía
		Element elementoDocumento = this.documento.getDocumentElement();

		Element elementos[] = ManipulacionElement
				.nodeListToElementArray(elementoDocumento
						.getElementsByTagName(e.getNodeName()));
		if (elementos.length != 0) {
			elementoDocumento.removeChild(elementos[0]); // Sólo debería haber
															// un elemento con
															// ese nombre...
		}

		elementoDocumento.appendChild(e);
		ManipulacionElement.writeXmlFile(this.documento, direccion);
	}
	
	/**
	 * Crea el archivo de opciones por defecto.
	 */
	public void crearArchivoPorDefecto() {
		File f = new File(direccion);
		if (!f.exists()) {
			this.crearArchivo();
		}

		Document documentoDefecto = ManipulacionElement.getDocumento(direccion);
		ManipulacionElement.writeXmlFile(documentoDefecto, dirdefecto);
	}
	
	/**
	 * Permite obtener el valor de una opción.
	 * 
	 * @param nombre Nombre de la opción.
	 * @param porDefecto Si la opción debe obtenerse del archivo de opciones por defecto.
	 * 
	 * @return Valor de la opción.
	 */
	public Opcion getOpcion(String nombre, boolean porDefecto) {
		this.documento = ManipulacionElement
				.getDocumento(porDefecto ? dirdefecto : direccion);

		Element elementos[] = ManipulacionElement
				.nodeListToElementArray(this.documento
						.getElementsByTagName(nombre));

		try {
			Class clase = Class.forName("opciones."
					+ elementos[0].getNodeName());
			this.opcion = (Opcion) (clase.newInstance());
			this.opcion.setValores(elementos[0]);
		} catch (java.lang.ClassNotFoundException cnfe) {
			System.out
					.println("GestorOpciones.getOpcion ClassNotFoundException:");
			System.out.println("  elementos[0].getNodeName()="
					+ elementos[0].getNodeName());
			System.out.println("  nombre                    =" + nombre);
		} catch (java.lang.InstantiationException ie) {
			System.out
					.println("GestorOpciones.getOpcion InstantiationException");
		} catch (java.lang.IllegalAccessException iae) {
			System.out
					.println("GestorOpciones.getOpcion IllegalAccessException");
		} catch (Exception e) {
			System.out.println("GestorOpciones.getOpcion Exception");
			e.printStackTrace();
			return null;
		}

		return this.opcion;
	}
	
	/**
	 * Permite establecer el valor de una opción.
	 * 
	 * @param op Opción a establecer.
	 * @param donde 0 para el fichero de opciones por defecto, 1 para el normal, 2 para ambos.
	 */
	public void setOpcion(Opcion op, int donde) {
		if (donde == 1 || donde == 2)// Actualizamos la opción en las opciones
										// en uso
		{
			this.documento = ManipulacionElement.getDocumento(direccion);
			Element elementos[] = ManipulacionElement
					.nodeListToElementArray(this.documento
							.getElementsByTagName(op.getNombre()));

			for (int i = 0; i < elementos.length; i++) {
				if (elementos[i].getNodeName().equals(op.getNombre())) {
					this.documento.getElementsByTagName("Opciones").item(0)
							.removeChild(elementos[i]);
				}
			}

			this.documento.getElementsByTagName("Opciones").item(0)
					.appendChild(op.getRepresentacionElement(this.documento));
			ManipulacionElement.writeXmlFile(this.documento, direccion);
		}

		if (donde == 0 || donde == 2)// Actualizamos la opción en las opciones
										// por defecto
		{
			Document docdefecto = ManipulacionElement.getDocumento(dirdefecto);
			Element elementos[] = ManipulacionElement
					.nodeListToElementArray(docdefecto.getElementsByTagName(op
							.getNombre()));

			for (int i = 0; i < elementos.length; i++) {
				if (elementos[i].getNodeName().equals(op.getNombre())) {
					docdefecto.getElementsByTagName("Opciones").item(0)
							.removeChild(elementos[i]);
				}
			}

			docdefecto.getElementsByTagName("Opciones").item(0)
					.appendChild(op.getRepresentacionElement(docdefecto));
			ManipulacionElement.writeXmlFile(docdefecto, dirdefecto);
		}

	}
	
	/**
	 * Permite crear una copia del fichero de opciones.
	 * 
	 * @param fichero Fichero destino donde serán almacenadas las opciones.
	 */
	public void crearArchivo(String fichero) {
		this.documento = ManipulacionElement.getDocumento(direccion);
		if (!((fichero.charAt(fichero.length() - 1) == 'L' || fichero
				.charAt(fichero.length() - 1) == 'l')
				&& (fichero.charAt(fichero.length() - 2) == 'M' || fichero
						.charAt(fichero.length() - 2) == 'm')
				&& (fichero.charAt(fichero.length() - 3) == 'X' || fichero
						.charAt(fichero.length() - 3) == 'x') && (fichero
					.charAt(fichero.length() - 4) == '.'))) {
			fichero = fichero + ".xml";
		}
		ManipulacionElement.writeXmlFile(this.documento, fichero);

	}
	
	/**
	 * Permite almacenar en el fichero de opciones, las opciones especificadas
	 * en el fichero especificado.
	 * 
	 * @param fichero Fichero fuente donde están almacenadas las opciones.
	 */
	public boolean cargarArchivo(String fichero) {
		this.documento = ManipulacionElement.getDocumento(fichero);
		Element e = this.documento.getDocumentElement();
		if (e.getTagName().equals("Opciones")) {
			Element opciones[] = ManipulacionElement.getChildElements(e);

			for (int i = 0; i < opciones.length; i++) {
				if (!ServiciosString.contieneCadena(clasesOpciones,
						opciones[i].getTagName(), false)) {
					return false;
				}
			}

			ManipulacionElement.writeXmlFile(this.documento, direccion);
			return true;
		}
		return false;
	}
	
	/**
	 * Almacena las opciones del fichero con las opciones por defecto
	 * en el fichero de opciones.
	 */
	public void cargarArchivoPorDefecto() {
		this.documento = ManipulacionElement.getDocumento(dirdefecto);
		ManipulacionElement.writeXmlFile(this.documento, direccion);
	}

}