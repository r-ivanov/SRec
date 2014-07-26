package datos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import opciones.GestorOpciones;
import opciones.OpcionFicherosRecientes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.ManipulacionElement;
import utilidades.SelecDireccion;
import utilidades.Texto;
import conf.Conf;

/**
 * Almacena archivos con valores para determinados métodos (opción "Guardar" de
 * cuadro de inserción de parámetros)
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2008
 */
public class AlmacenValores {

	private String cadenas[];
	private String fichero[];
	private String mensajeError;

	private GestorOpciones gOpciones = new GestorOpciones();
	private OpcionFicherosRecientes ofr;

	/**
	 * Constructor: crea una nueva instancia
	 */
	public AlmacenValores() {
		this.cadenas = null;

		// Cargamos opción de ficheros recientes (para saber último directorio)
		this.ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
				"OpcionFicherosRecientes", true);
		this.fichero = new String[2];
		this.fichero[0] = this.ofr.getDir();
	}

	/**
	 * Guarda los contenidos de los parámetros, las clases a las que pertenecen
	 * y el nombre del método en un XML
	 * 
	 * @param cadenas
	 *            valores para los paráemtros
	 * @param metodo
	 *            Método cuyos parámetros se quieren guardar
	 */
	public void guardar(String cadenas[], MetodoAlgoritmo metodo) {

		this.fichero[1] = null;
		this.fichero = SelecDireccion.cuadroAbrirFichero(this.fichero[0],
				Texto.get("CA_GUARPAR", Conf.idioma), null, "xml",
				Texto.get("ARCHIVO_XML", Conf.idioma), 0);

		// *1* Comprobarmos que el fichero existe

		if (this.fichero != null && this.fichero[1] != null) {
			this.ofr.setDir(this.fichero[0]);
			this.gOpciones.setOpcion(this.ofr, 2);

			if (this.fichero[1].length() < 4
					|| (this.fichero[1].charAt(this.fichero[1].length() - 4) != '.'
							&& (this.fichero[1]
									.charAt(this.fichero[1].length() - 3) != 'x' || this.fichero[1]
									.charAt(this.fichero[1].length() - 3) != 'X')
							&& (this.fichero[1]
									.charAt(this.fichero[1].length() - 2) != 'm' || this.fichero[1]
									.charAt(this.fichero[1].length() - 2) != 'M') && (this.fichero[1]
							.charAt(this.fichero[1].length() - 1) != 'l' || this.fichero[1]
							.charAt(this.fichero[1].length() - 1) != 'L'))) {
				this.fichero[1] = new String(this.fichero[1] + ".xml");
			}

			File f = new File(this.fichero[0] + this.fichero[1]);
			try {
				f.createNewFile();
			} catch (java.io.IOException ioe) {
			}

			// Inicialización de escritura
			FileWriter fw = null;
			try {
				fw = new FileWriter(this.fichero[0] + this.fichero[1]);
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 1");
			}

			// Escritura
			try {
				fw.write("<valoresAlgoritmoVisualizador>\r\n");
				fw.write("    <algoritmo nombre=\"" + metodo.getNombre()
						+ "\">\r\n");
				for (int i = 0; i < metodo.getNumeroParametros(); i++) {
					cadenas[i] = cadenas[i].replace("\"", "&quot;");
					cadenas[i] = cadenas[i].replace("ñ", "&ntilde;");
					cadenas[i] = cadenas[i].replace("Ñ", "&Ntilde;");
					cadenas[i] = cadenas[i].replace("á", "&aacute;");
					cadenas[i] = cadenas[i].replace("é", "&eacute;");
					cadenas[i] = cadenas[i].replace("í", "&iacute;");
					cadenas[i] = cadenas[i].replace("ó", "&oacute;");
					cadenas[i] = cadenas[i].replace("ú", "&uacute;");
					cadenas[i] = cadenas[i].replace("Á", "&Aacute;");
					cadenas[i] = cadenas[i].replace("É", "&Eacute;");
					cadenas[i] = cadenas[i].replace("Í", "&Iacute;");
					cadenas[i] = cadenas[i].replace("Ó", "&Oacute;");
					cadenas[i] = cadenas[i].replace("Ú", "&Uacute;");
					cadenas[i] = cadenas[i].replace("à", "&agrave;");
					cadenas[i] = cadenas[i].replace("è", "&egrave;");
					cadenas[i] = cadenas[i].replace("ì", "&igrave;");
					cadenas[i] = cadenas[i].replace("ò", "&ograve;");
					cadenas[i] = cadenas[i].replace("ù", "&ugrave;");
					cadenas[i] = cadenas[i].replace("À", "&Agrave;");
					cadenas[i] = cadenas[i].replace("È", "&Egrave;");
					cadenas[i] = cadenas[i].replace("Ì", "&Igrave;");
					cadenas[i] = cadenas[i].replace("Ò", "&Ograve;");
					cadenas[i] = cadenas[i].replace("Ù", "&Ugrave;");
					fw.write("        <parametro val=\"" + cadenas[i]
							+ "\" clase=\"" + metodo.getTipoParametro(i)
							+ "\" />\r\n");
				}
				fw.write("    </algoritmo>\r\n");
				fw.write("</valoresAlgoritmoVisualizador>\r\n");
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 2");
			}

			// Finalización de escritura
			try {
				fw.close();
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 3");
			}
		}
	}

	/**
	 * Carga un determinado documento XML para averiguar si pertenece o no al
	 * método que se está intentando ejecutar
	 * 
	 * @param clases
	 *            clases a las que pertenecen los parámetros del método que se
	 *            intenta ejecutar
	 * @param metodo
	 *            Método que se está intentando ejecutar
	 */
	public boolean cargar(MetodoAlgoritmo metodo) {
		
		this.ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
				"OpcionFicherosRecientes", true);
		this.fichero = SelecDireccion.cuadroAbrirFichero(this.ofr.getDir(),
				Texto.get("CA_CARGPAR", Conf.idioma), null, "xml",
				Texto.get("ARCHIVO_XML", Conf.idioma), 1);

		// *1* Comprobarmos que el fichero existe

		if (this.fichero != null && this.fichero[1] != null) {
			this.ofr.setDir(this.fichero[0]);
			this.gOpciones.setOpcion(this.ofr, 2);

			Document documento = ManipulacionElement
					.getDocumento(this.fichero[0] + this.fichero[1]);

			Element elementDocumento;
			Element elementAlgoritmo[];
			Element parametros[];

			try {
				elementDocumento = documento.getDocumentElement();
				elementAlgoritmo = ManipulacionElement
						.getChildElements(elementDocumento);
				parametros = ManipulacionElement
						.getChildElements(elementAlgoritmo[0]);
			} catch (Exception e) {
				this.mensajeError = Texto.get("ARCHCOR", Conf.idioma);
				return false;
			}
			if (elementAlgoritmo[0].getAttribute("nombre").equals(
					metodo.getNombre())) {
				if (metodo.getNumeroParametros() != parametros.length) {
					this.mensajeError = Texto
							.get("ERROR_PARAMINC", Conf.idioma);
					return false;
				}
				this.cadenas = new String[parametros.length];
				for (int i = 0; i < parametros.length; i++) {
					this.cadenas[i] = parametros[i].getAttribute("val");
					this.cadenas[i] = this.cadenas[i].replace("&quot;", "\"");
					this.cadenas[i] = this.cadenas[i].replace("&ntilde;", "ñ");
					this.cadenas[i] = this.cadenas[i].replace("&Ntilde;", "Ñ");
					this.cadenas[i] = this.cadenas[i].replace("&aacute;", "á");
					this.cadenas[i] = this.cadenas[i].replace("&eacute;", "é");
					this.cadenas[i] = this.cadenas[i].replace("&iacute;", "í");
					this.cadenas[i] = this.cadenas[i].replace("&oacute;", "ó");
					this.cadenas[i] = this.cadenas[i].replace("&uacute;", "ú");
					this.cadenas[i] = this.cadenas[i].replace("&Aacute;", "Á");
					this.cadenas[i] = this.cadenas[i].replace("&Eacute;", "É");
					this.cadenas[i] = this.cadenas[i].replace("&Iacute;", "Í");
					this.cadenas[i] = this.cadenas[i].replace("&Oacute;", "Ó");
					this.cadenas[i] = this.cadenas[i].replace("&Uacute;", "Ú");
					this.cadenas[i] = this.cadenas[i].replace("&agrave;", "à");
					this.cadenas[i] = this.cadenas[i].replace("&egrave;", "è");
					this.cadenas[i] = this.cadenas[i].replace("&igrave;", "ì");
					this.cadenas[i] = this.cadenas[i].replace("&ograve;", "ò");
					this.cadenas[i] = this.cadenas[i].replace("&ugrave;", "ù");
					this.cadenas[i] = this.cadenas[i].replace("&Agrave;", "À");
					this.cadenas[i] = this.cadenas[i].replace("&Egrave;", "È");
					this.cadenas[i] = this.cadenas[i].replace("&Igrave;", "Ì");
					this.cadenas[i] = this.cadenas[i].replace("&Ograve;", "Ò");
					this.cadenas[i] = this.cadenas[i].replace("&Ugrave;", "Ù");
				}

				for (int i = 0; i < parametros.length; i++) {
					if (!(metodo.getTipoParametro(i).equals(parametros[i]
							.getAttribute("clase")))) {
						this.mensajeError = Texto.get("ERROR_PARAMTIP",
								Conf.idioma) + (i + 1) + ").";
						return false;
					}
				}
				return true; // Carga satisfactoria
			} else {
				this.mensajeError = Texto.get("ERROR_PARAMALG", Conf.idioma)
						+ elementAlgoritmo[0].getAttribute("nombre") + ").";
				return false; // Sucedio error
			}
		}
		this.mensajeError = "";
		return false;
	}

	/**
	 * Devuelve los valores de los parámetros
	 * 
	 * @return los valores de los parámetros leídos en el documento XML
	 */
	public String[] get() {
		return this.cadenas;
	}

	/**
	 * Devuelve el mensaje de error, si lo hubo
	 * 
	 * @return mensaje de error, si lo hubo
	 */
	public String getError() {
		return this.mensajeError;
	}
}