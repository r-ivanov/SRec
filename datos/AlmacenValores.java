/**
	Almacena archivos con valores para determinados m�todos (opci�n "Guardar" de cuadro de inserci�n de par�metros)
	
	@author Antonio P�rez Carrasco
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
		
		// Cargamos opci�n de ficheros recientes (para saber �ltimo directorio)
		ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);
		this.fichero=new String[2];
		this.fichero[0]=ofr.getDir();
	}
	
	/**
		Guarda los contenidos de los par�metros, las clases a las que pertenecen y el nombre del m�todo en un XML
		
		@param cadenas valores para los par�emtros
		@param clases clases a las que pertenecen cada uno de los par�emtros del m�todo
		@param nombreMetodo nombre del m�todo cuyos par�metros se quieren guardar
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
			
			// Inicializaci�n de escritura
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
					cadenas[i]=cadenas[i].replace("�","&ntilde;");
					cadenas[i]=cadenas[i].replace("�","&Ntilde;");
					cadenas[i]=cadenas[i].replace("�","&aacute;");
					cadenas[i]=cadenas[i].replace("�","&eacute;");
					cadenas[i]=cadenas[i].replace("�","&iacute;");
					cadenas[i]=cadenas[i].replace("�","&oacute;");
					cadenas[i]=cadenas[i].replace("�","&uacute;");
					cadenas[i]=cadenas[i].replace("�","&Aacute;");
					cadenas[i]=cadenas[i].replace("�","&Eacute;");
					cadenas[i]=cadenas[i].replace("�","&Iacute;");
					cadenas[i]=cadenas[i].replace("�","&Oacute;");
					cadenas[i]=cadenas[i].replace("�","&Uacute;");
					cadenas[i]=cadenas[i].replace("�","&agrave;");
					cadenas[i]=cadenas[i].replace("�","&egrave;");
					cadenas[i]=cadenas[i].replace("�","&igrave;");
					cadenas[i]=cadenas[i].replace("�","&ograve;");
					cadenas[i]=cadenas[i].replace("�","&ugrave;");
					cadenas[i]=cadenas[i].replace("�","&Agrave;");
					cadenas[i]=cadenas[i].replace("�","&Egrave;");
					cadenas[i]=cadenas[i].replace("�","&Igrave;");
					cadenas[i]=cadenas[i].replace("�","&Ograve;");
					cadenas[i]=cadenas[i].replace("�","&Ugrave;");

					
					
					/*cadenas[i]=cadenas[i].replace("\"","[[[[[_comillas_]]]]]");
					cadenas[i]=cadenas[i].replace("�","[[[[[_nsombrero_]]]]]");
					cadenas[i]=cadenas[i].replace("�","[[[[[_Nsombrero_]]]]]");
					cadenas[i]=cadenas[i].replace("�","[[[[[_aac_]]]]]");
					cadenas[i]=cadenas[i].replace("�","[[[[[_eac_]]]]]");
					cadenas[i]=cadenas[i].replace("�","[[[[[_iac_]]]]]");
					cadenas[i]=cadenas[i].replace("�","[[[[[_oac_]]]]]");
					cadenas[i]=cadenas[i].replace("�","[[[[[_uac_]]]]]");*/
					fw.write("        <parametro val=\""+cadenas[i]+"\" clase=\""+metodo.getTipoParametro(i)+"\" />\r\n");
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
		}
	}
	
	/**
		Carga un determinado documento XML para averiguar si pertenece o no al m�todo que se est� intentando ejecutar
		
		@param clases clases a las que pertenecen los par�metros del m�todo que se intenta ejecutar
		@param nombreMetodo nombre del m�todo que se est� intentando ejecutar
		@return true si el m�todo coincide con el m�todo que aparece en el documento XML
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
					cadenas[i]=cadenas[i].replace("[[[[[_nsombrero_]]]]]","�");
					cadenas[i]=cadenas[i].replace("[[[[[_Nsombrero_]]]]]","�");
					cadenas[i]=cadenas[i].replace("[[[[[_aac_]]]]]","�");
					cadenas[i]=cadenas[i].replace("[[[[[_eac_]]]]]","�");
					cadenas[i]=cadenas[i].replace("[[[[[_iac_]]]]]","�");
					cadenas[i]=cadenas[i].replace("[[[[[_oac_]]]]]","�");
					cadenas[i]=cadenas[i].replace("[[[[[_uac_]]]]]","�");*/
					
					
					
					cadenas[i]=cadenas[i].replace("&quot;","\"");
					cadenas[i]=cadenas[i].replace("&ntilde;","�");
					cadenas[i]=cadenas[i].replace("&Ntilde;","�");
					cadenas[i]=cadenas[i].replace("&aacute;","�");
					cadenas[i]=cadenas[i].replace("&eacute;","�");
					cadenas[i]=cadenas[i].replace("&iacute;","�");
					cadenas[i]=cadenas[i].replace("&oacute;","�");
					cadenas[i]=cadenas[i].replace("&uacute;","�");
					cadenas[i]=cadenas[i].replace("&Aacute;","�");
					cadenas[i]=cadenas[i].replace("&Eacute;","�");
					cadenas[i]=cadenas[i].replace("&Iacute;","�");
					cadenas[i]=cadenas[i].replace("&Oacute;","�");
					cadenas[i]=cadenas[i].replace("&Uacute;","�");
					cadenas[i]=cadenas[i].replace("&agrave;","�");
					cadenas[i]=cadenas[i].replace("&egrave;","�");
					cadenas[i]=cadenas[i].replace("&igrave;","�");
					cadenas[i]=cadenas[i].replace("&ograve;","�");
					cadenas[i]=cadenas[i].replace("&ugrave;","�");
					cadenas[i]=cadenas[i].replace("&Agrave;","�");
					cadenas[i]=cadenas[i].replace("&Egrave;","�");
					cadenas[i]=cadenas[i].replace("&Igrave;","�");
					cadenas[i]=cadenas[i].replace("&Ograve;","�");
					cadenas[i]=cadenas[i].replace("&Ugrave;","�");
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
		Devuelve los valores de los par�metros
		
		@return los valores de los par�metros le�dos en el documento XML
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