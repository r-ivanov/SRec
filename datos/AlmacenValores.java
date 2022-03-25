package datos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import opciones.GestorOpciones;
import opciones.OpcionFicherosRecientes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.ManipulacionElement;
import utilidades.SelecDireccion;
import utilidades.Texto;
import conf.Conf;
import cuadros.CuadroError;

/**
 * Almacena archivos con valores para determinados m�todos (opci�n "Guardar" de
 * cuadro de inserci�n de par�metros)
 * 
 * @author Antonio P�rez Carrasco
 * @version 2006-2008
 */
public class AlmacenValores {

	private String cadenas[];
	private String fichero[];
	private String mensajeError;
	private String tipoFich;

	private GestorOpciones gOpciones = new GestorOpciones();
	private OpcionFicherosRecientes ofr;

	/**
	 * Constructor: crea una nueva instancia
	 */
	public AlmacenValores() {
		this.cadenas = null;

		// Cargamos opci�n de ficheros recientes (para saber �ltimo directorio)
		this.ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
				"OpcionFicherosRecientes", true);
		this.fichero = new String[3];
		this.fichero[0] = this.ofr.getDir();
	}

	/**
	 * Guarda los contenidos de los par�metros, las clases a las que pertenecen
	 * y el nombre del m�todo en un XML o TXT
	 * 
	 * @param cadenas
	 *            valores para los par�emtros
	 * @param metodo
	 *            M�todo cuyos par�metros se quieren guardar
	 */
	public void guardar(String cadenas[], MetodoAlgoritmo metodo) {

		this.fichero[1] = null;
		String[][] ext = new String[2][1];
		ext[0][0]="xml";
		ext[1][0]="txt";
		String[] def = new String[2];
		def[0]=Texto.get("ARCHIVO_XML", Conf.idioma);
		def[1]="Documento TXT";
		this.fichero = SelecDireccion.cuadroAbrirFichero(this.fichero[0],
				Texto.get("CA_GUARPAR", Conf.idioma), null, ext,
				def, 0);

		// *1* Comprobarmos que el fichero existe

		if (this.fichero != null && this.fichero[1] != null && this.fichero[2]=="false") {
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

			// Inicializaci�n de escritura
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
					cadenas[i] = cadenas[i].replace("�", "&ntilde;");
					cadenas[i] = cadenas[i].replace("�", "&Ntilde;");
					cadenas[i] = cadenas[i].replace("�", "&aacute;");
					cadenas[i] = cadenas[i].replace("�", "&eacute;");
					cadenas[i] = cadenas[i].replace("�", "&iacute;");
					cadenas[i] = cadenas[i].replace("�", "&oacute;");
					cadenas[i] = cadenas[i].replace("�", "&uacute;");
					cadenas[i] = cadenas[i].replace("�", "&Aacute;");
					cadenas[i] = cadenas[i].replace("�", "&Eacute;");
					cadenas[i] = cadenas[i].replace("�", "&Iacute;");
					cadenas[i] = cadenas[i].replace("�", "&Oacute;");
					cadenas[i] = cadenas[i].replace("�", "&Uacute;");
					cadenas[i] = cadenas[i].replace("�", "&agrave;");
					cadenas[i] = cadenas[i].replace("�", "&egrave;");
					cadenas[i] = cadenas[i].replace("�", "&igrave;");
					cadenas[i] = cadenas[i].replace("�", "&ograve;");
					cadenas[i] = cadenas[i].replace("�", "&ugrave;");
					cadenas[i] = cadenas[i].replace("�", "&Agrave;");
					cadenas[i] = cadenas[i].replace("�", "&Egrave;");
					cadenas[i] = cadenas[i].replace("�", "&Igrave;");
					cadenas[i] = cadenas[i].replace("�", "&Ograve;");
					cadenas[i] = cadenas[i].replace("�", "&Ugrave;");
					fw.write("        <parametro val=\"" + cadenas[i]
							+ "\" clase=\"" + metodo.getTipoParametro(i)
							+ "\" />\r\n");
				}
				fw.write("    </algoritmo>\r\n");
				fw.write("</valoresAlgoritmoVisualizador>\r\n");
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 2");
			}

			// Finalizaci�n de escritura
			try {
				fw.close();
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 3");
			}
		} else if(fichero[2]=="true"){   //Tipo txt
			this.ofr.setDir(this.fichero[0]);
			this.gOpciones.setOpcion(this.ofr, 2);
			File f = new File(this.fichero[0] + this.fichero[1]);
			try {
				f.createNewFile();
			} catch (java.io.IOException ioe) {
			}

			// Inicializaci�n de escritura
			FileWriter fw = null;
			try {
				fw = new FileWriter(this.fichero[0] + this.fichero[1]);

			} catch (IOException ioe) {
				System.out.println("Error FileWriter 1");
			}
			try {
				for (int i = 0; i < metodo.getNumeroParametros(); i++) {
				fw.write(cadenas[i]+"\n ");
				}
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 1");
			}
			// Finalizaci�n de escritura
			try {
				fw.close();
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 3");
			}

		}
	}

	/**
	 * Carga un determinado documento XML para averiguar si pertenece o no al
	 * m�todo que se est� intentando ejecutar
	 * 
	 * @param clases
	 *            clases a las que pertenecen los par�metros del m�todo que se
	 *            intenta ejecutar
	 * @param metodo
	 *            M�todo que se est� intentando ejecutar
	 */
	public boolean cargar(MetodoAlgoritmo metodo) {
		
		this.ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
				"OpcionFicherosRecientes", true);
		String[][] ext = new String[2][1];
		ext[0][0]="xml";
		ext[1][0]="txt";
		String[] def = new String[2];
		def[0]=Texto.get("ARCHIVO_XML", Conf.idioma);
		def[1]="Documento TXT";
		this.fichero = SelecDireccion.cuadroAbrirFichero(this.ofr.getDir(),
				Texto.get("CA_CARGPAR", Conf.idioma), null, ext,
				def, 1);
	
		// *1* Comprobarmos que el fichero existe
		String tipoTxt = this.fichero[2];
		
		

		if (this.fichero != null && this.fichero[1] != null&&tipoTxt=="false") {
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
					this.cadenas[i] = this.cadenas[i].replace("&ntilde;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&Ntilde;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&aacute;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&eacute;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&iacute;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&oacute;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&uacute;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&Aacute;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&Eacute;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&Iacute;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&Oacute;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&Uacute;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&agrave;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&egrave;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&igrave;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&ograve;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&ugrave;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&Agrave;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&Egrave;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&Igrave;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&Ograve;", "�");
					this.cadenas[i] = this.cadenas[i].replace("&Ugrave;", "�");
				
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
			}
			else {
				this.mensajeError = Texto.get("ERROR_PARAMALG", Conf.idioma)
						+ elementAlgoritmo[0].getAttribute("nombre") + ").";
				return false; // Sucedio error
			}
		
			}
		else  { 
			//Si no contiene el "nombre" se tratara de un fichero txt
			try {
				FileReader fr = new FileReader(fichero[0] + fichero[1]);
				BufferedReader br = new BufferedReader(fr);
				ArrayList<String> cadenasList = new ArrayList<String>();
				String linea;
				while ((linea = br.readLine()) != null) {
					cadenasList.add(linea);
				}
				this.cadenas = new String[cadenasList.size()];
				for (int i = 0; i < cadenas.length; i++) {
				/*	if (cadenasList.get(i).matches(".*[a-z].*")) { 
						this.mensajeError =  Texto.get("ERROR_PARAMALG", Conf.idioma);
						return false;  
					}*/
					
				
				cadenas[i] = cadenasList.get(i);
					
				}
				fr.close();
			} catch (Exception e) {
				this.mensajeError = "Error al encontrar el fichero";

				return false;
			}
			/*
			 * this.cadenas = new String[1]; this.cadenas[0]=tipoTxt;
			 */

			return true;
		}
		/*this.mensajeError = "Error al encontrar el fichero";
		return false;*/
	}

	/**
	 * Devuelve los valores de los par�metros
	 * 
	 * @return los valores de los par�metros le�dos en el documento XML
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