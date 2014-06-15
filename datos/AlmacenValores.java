/**
	Almacena archivos con valores para determinados métodos (opción "Guardar" de cuadro de inserción de parámetros)
	
	@author Antonio Pérez Carrasco
	@version 2006-2008
*/
package datos;




import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.awt.FileDialog;
import javax.swing.JFrame;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;


import conf.*;
import opciones.*;
import utilidades.*;



public class AlmacenValores
{
	String cadenas[];
	Class clases[];
	String fichero[];
	String mensajeError;
	
	GestorOpciones gOpciones=new GestorOpciones();
	OpcionFicherosRecientes ofr;
	
	/**
		Constructor: crea una nueva instancia
	*/
	public AlmacenValores()
	{
		this.cadenas=null;
		this.clases=null;
		
		// Cargamos opción de ficheros recientes (para saber último directorio)
		ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);
		this.fichero=new String[2];
		this.fichero[0]=ofr.getDir();
	}
	
	/**
		Guarda los contenidos de los parámetros, las clases a las que pertenecen y el nombre del método en un XML
		
		@param cadenas valores para los paráemtros
		@param clases clases a las que pertenecen cada uno de los paráemtros del método
		@param nombreMetodo nombre del método cuyos parámetros se quieren guardar
	*/
	public void guardar(String cadenas[], MetodoAlgoritmo metodo)//Class clases[], String nombreMetodo)
	{
		fichero[1]=null;
		fichero=SelecDireccion.cuadroAbrirFichero(this.fichero[0],Texto.get("CA_GUARPAR",Conf.idioma),
						null,"xml",Texto.get("ARCHIVO_XML",Conf.idioma),0);
		
		// *1* Comprobarmos que el fichero existe
		
		if (fichero!=null && fichero[1]!=null)
		{
			ofr.setDir(fichero[0]);
			gOpciones.setOpcion(ofr,2);
		
			if (fichero[1].length()<4 || (fichero[1].charAt(fichero[1].length()-4)!='.' &&
					(fichero[1].charAt(fichero[1].length()-3)!='x' || fichero[1].charAt(fichero[1].length()-3)!='X') &&
					(fichero[1].charAt(fichero[1].length()-2)!='m' || fichero[1].charAt(fichero[1].length()-2)!='M') &&
					(fichero[1].charAt(fichero[1].length()-1)!='l' || fichero[1].charAt(fichero[1].length()-1)!='L')))
				fichero[1]=new String(fichero[1]+".xml");
			
			File f=new File(fichero[0]+fichero[1]);
			try {
				f.createNewFile();
			} catch (java.io.IOException ioe) {
			}
			
			// Inicialización de escritura
			FileWriter fw=null;
			try {
				fw = new FileWriter(fichero[0]+fichero[1]);
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 1");
			}
			
			// Escritura
			try {
				fw.write("<valoresAlgoritmoVisualizador>\r\n");
				fw.write("    <algoritmo nombre=\""+metodo.getNombre()+"\">\r\n");
				for (int i=0; i<metodo.getNumeroParametros(); i++)
				{
					cadenas[i]=cadenas[i].replace("\"","&quot;");
					cadenas[i]=cadenas[i].replace("ñ","&ntilde;");
					cadenas[i]=cadenas[i].replace("Ñ","&Ntilde;");
					cadenas[i]=cadenas[i].replace("á","&aacute;");
					cadenas[i]=cadenas[i].replace("é","&eacute;");
					cadenas[i]=cadenas[i].replace("í","&iacute;");
					cadenas[i]=cadenas[i].replace("ó","&oacute;");
					cadenas[i]=cadenas[i].replace("ú","&uacute;");
					cadenas[i]=cadenas[i].replace("Á","&Aacute;");
					cadenas[i]=cadenas[i].replace("É","&Eacute;");
					cadenas[i]=cadenas[i].replace("Í","&Iacute;");
					cadenas[i]=cadenas[i].replace("Ó","&Oacute;");
					cadenas[i]=cadenas[i].replace("Ú","&Uacute;");
					cadenas[i]=cadenas[i].replace("à","&agrave;");
					cadenas[i]=cadenas[i].replace("è","&egrave;");
					cadenas[i]=cadenas[i].replace("ì","&igrave;");
					cadenas[i]=cadenas[i].replace("ò","&ograve;");
					cadenas[i]=cadenas[i].replace("ù","&ugrave;");
					cadenas[i]=cadenas[i].replace("À","&Agrave;");
					cadenas[i]=cadenas[i].replace("È","&Egrave;");
					cadenas[i]=cadenas[i].replace("Ì","&Igrave;");
					cadenas[i]=cadenas[i].replace("Ò","&Ograve;");
					cadenas[i]=cadenas[i].replace("Ù","&Ugrave;");

					
					
					/*cadenas[i]=cadenas[i].replace("\"","[[[[[_comillas_]]]]]");
					cadenas[i]=cadenas[i].replace("ñ","[[[[[_nsombrero_]]]]]");
					cadenas[i]=cadenas[i].replace("Ñ","[[[[[_Nsombrero_]]]]]");
					cadenas[i]=cadenas[i].replace("á","[[[[[_aac_]]]]]");
					cadenas[i]=cadenas[i].replace("é","[[[[[_eac_]]]]]");
					cadenas[i]=cadenas[i].replace("í","[[[[[_iac_]]]]]");
					cadenas[i]=cadenas[i].replace("ó","[[[[[_oac_]]]]]");
					cadenas[i]=cadenas[i].replace("ú","[[[[[_uac_]]]]]");*/
					fw.write("        <parametro val=\""+cadenas[i]+"\" clase=\""+metodo.getTipoParametro(i)+"\" />\r\n");
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
		Carga un determinado documento XML para averiguar si pertenece o no al método que se está intentando ejecutar
		
		@param clases clases a las que pertenecen los parámetros del método que se intenta ejecutar
		@param nombreMetodo nombre del método que se está intentando ejecutar
		@return true si el método coincide con el método que aparece en el documento XML
	*/
	public boolean cargar(MetodoAlgoritmo metodo)//Class clases[], String nombreMetodo)
	{
		ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);
		fichero=SelecDireccion.cuadroAbrirFichero(ofr.getDir(),Texto.get("CA_CARGPAR",Conf.idioma),
						null,"xml",Texto.get("ARCHIVO_XML",Conf.idioma),1);
		
		// *1* Comprobarmos que el fichero existe
		
		if (fichero!=null && fichero[1]!=null)
		{
			ofr.setDir(fichero[0]);
			gOpciones.setOpcion(ofr,2);
		
		
			Document documento =ManipulacionElement.getDocumento(fichero[0]+fichero[1]);
			
			Element elementDocumento;
			Element elementAlgoritmo[];
			Element parametros[];
			
			try {
				elementDocumento=documento.getDocumentElement();
				elementAlgoritmo=ManipulacionElement.getChildElements(elementDocumento);
				parametros=ManipulacionElement.getChildElements( elementAlgoritmo[0] );
			} catch (Exception e) {
				mensajeError=Texto.get("ARCHCOR",Conf.idioma);
				return false;
			}
			if (elementAlgoritmo[0].getAttribute("nombre").equals(metodo.getNombre()))
			{
				if (metodo.getNumeroParametros() != parametros.length)
				{
					mensajeError=Texto.get("ERROR_PARAMINC",Conf.idioma);
					return false;
				}
				this.cadenas=new String[parametros.length];
				for (int i=0; i<parametros.length; i++)
				{
					//System.out.println("Valor:"+parametros[i].getAttribute("val")+"\t\t\tClase="+parametros[i].getAttribute("clase"));
					this.cadenas[i]=parametros[i].getAttribute("val");
					/*cadenas[i]=cadenas[i].replace("[[[[[_comillas_]]]]]","\"");
					cadenas[i]=cadenas[i].replace("[[[[[_nsombrero_]]]]]","ñ");
					cadenas[i]=cadenas[i].replace("[[[[[_Nsombrero_]]]]]","Ñ");
					cadenas[i]=cadenas[i].replace("[[[[[_aac_]]]]]","á");
					cadenas[i]=cadenas[i].replace("[[[[[_eac_]]]]]","é");
					cadenas[i]=cadenas[i].replace("[[[[[_iac_]]]]]","í");
					cadenas[i]=cadenas[i].replace("[[[[[_oac_]]]]]","ó");
					cadenas[i]=cadenas[i].replace("[[[[[_uac_]]]]]","ú");*/
					
					
					
					cadenas[i]=cadenas[i].replace("&quot;","\"");
					cadenas[i]=cadenas[i].replace("&ntilde;","ñ");
					cadenas[i]=cadenas[i].replace("&Ntilde;","Ñ");
					cadenas[i]=cadenas[i].replace("&aacute;","á");
					cadenas[i]=cadenas[i].replace("&eacute;","é");
					cadenas[i]=cadenas[i].replace("&iacute;","í");
					cadenas[i]=cadenas[i].replace("&oacute;","ó");
					cadenas[i]=cadenas[i].replace("&uacute;","ú");
					cadenas[i]=cadenas[i].replace("&Aacute;","Á");
					cadenas[i]=cadenas[i].replace("&Eacute;","É");
					cadenas[i]=cadenas[i].replace("&Iacute;","Í");
					cadenas[i]=cadenas[i].replace("&Oacute;","Ó");
					cadenas[i]=cadenas[i].replace("&Uacute;","Ú");
					cadenas[i]=cadenas[i].replace("&agrave;","à");
					cadenas[i]=cadenas[i].replace("&egrave;","è");
					cadenas[i]=cadenas[i].replace("&igrave;","ì");
					cadenas[i]=cadenas[i].replace("&ograve;","ò");
					cadenas[i]=cadenas[i].replace("&ugrave;","ù");
					cadenas[i]=cadenas[i].replace("&Agrave;","À");
					cadenas[i]=cadenas[i].replace("&Egrave;","È");
					cadenas[i]=cadenas[i].replace("&Igrave;","Ì");
					cadenas[i]=cadenas[i].replace("&Ograve;","Ò");
					cadenas[i]=cadenas[i].replace("&Ugrave;","Ù");
				}
				
				for (int i=0; i<parametros.length; i++)
					if (!(metodo.getTipoParametro(i).equals( parametros[i].getAttribute("clase") )))
					{
						mensajeError=Texto.get("ERROR_PARAMTIP",Conf.idioma)+(i+1)+").";
						return false;
					}
				return true;	// Carga satisfactoria
			}
			else
			{
				mensajeError=Texto.get("ERROR_PARAMALG",Conf.idioma)+elementAlgoritmo[0].getAttribute("nombre")+").";
				return false;	// Sucedio error
			}
		}
		mensajeError="";
		return false;
	}
	
	/**
		Devuelve los valores de los parámetros
		
		@return los valores de los parámetros leídos en el documento XML
	*/
	public String[] get()
	{
		return this.cadenas;
	}
	
	/**
		Devuelve el mensaje de error, si lo hubo
		
		@return mensaje de error, si lo hubo
	*/
	public String getError()
	{
		return mensajeError;
	}
	
	public static void main(String argc[])
	{
		AlmacenValores av= new AlmacenValores();
		//av.cargar(null);
		
	}
}