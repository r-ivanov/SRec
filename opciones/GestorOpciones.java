/**
	Gestiona la carga y almacenaiento de opciones en fichero
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package opciones;

import java.io.File;


import org.w3c.dom.*;

import javax.xml.parsers.*;


import utilidades.*;

public class GestorOpciones
{
	static boolean depurar=false;

	Opcion opcion;
	private static String direccion="datos/opciones.xml";
	private static String dirdefecto="datos/opdefecto.xml";

	private static String[] clasesOpciones={
				"OpcionBorradoFicheros",
				"OpcionConfVisualizacion",
				"OpcionFicherosRecientes",
				"OpcionIdioma",
				"OpcionMVJava",
				"OpcionOpsVisualizacion",
				"OpcionTipoGrafico",
				"OpcionVistas"
				};
	
	
	
	String claseOpcion;
	
	Document documento;

	/**
		Constructor: genera un gestor de opciones vacío
	*/
	public GestorOpciones()
	{
	}

	
	
	public boolean existeArchivoOpciones()
	{
		File file=new File(direccion);
		return (file.exists());
	}
	
	public boolean existeArchivoOpcionesPorDefecto()
	{
		File file=new File(dirdefecto);
		return (file.exists());
	}
	
	public static String getNombreArchivoOpciones()
	{
		return direccion;
	}
	
	public static String getNombreArchivoOpDefecto()
	{
		return dirdefecto;
	}
	
	public void crearArchivo()
	{
		// Generamos un documento nuevo
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder =null;
		
		try{
			builder = factory.newDocumentBuilder();
			this.documento = builder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Element eInicial=this.documento.createElement("Opciones");
		this.documento.appendChild(eInicial);
		
		// Recorremos el directorio de la aplicacion para buscar sus propios .class que implementen opciones
		// así no tendremos que retocar esta clase cada vez que insertemos una opción nueva a la aplicación
		
		// Nos situamos sobre directorio de la aplicación
		File directorioAplicacion=null;
		try {
			directorioAplicacion=new File(".\\opciones");
		} catch (Exception exc) {
		}
		
		// Listamos todos sus ficheros
		File clases[]=directorioAplicacion.listFiles();
		
	
		// Recorremos el array para detectar las clases que nos interesan
		for (int i=0; i<clases.length; i++)
			if (clases[i].getName().contains(".class") && clases[i].getName().contains("Opcion") &&
						clases[i].getName().indexOf("Opcion")==0 && clases[i].getName().length()>12)
			{
				//System.out.println("* "+clases[i].getName());
				Class clase=null;
				Object opcion=null;	
				try {
					clase = Class.forName("opciones."+clases[i].getName().replace(".class",""));
					opcion=clase.newInstance();

				} catch ( java.lang.ClassNotFoundException cnfe) {
					System.out.println("GestorOpciones.crearArchivo ClassNotFoundException");
				} catch ( java.lang.InstantiationException ie) {
					System.out.println("GestorOpciones.crearArchivo InstantiationException");
				} catch ( java.lang.IllegalAccessException iae) {
					System.out.println("GestorOpciones.crearArchivo IllegalAccessException");
				} 
				
				if (opcion==null)
					System.out.println("GestorOpciones.crearArchivo opcion=null");

				anadirOpcion(((Opcion)opcion).getRepresentacionElement(this.documento));
			}
	}
	
	private void anadirOpcion(Element e)
	{
		// Añadimos la nueva opción, sustituyéndola si ya existía
		Element elementoDocumento=this.documento.getDocumentElement();
		
		Element elementos[]=ManipulacionElement.nodeListToElementArray(elementoDocumento.getElementsByTagName(e.getNodeName()));
		if (elementos.length!=0)	// Si existía, la eliminamos
			elementoDocumento.removeChild(elementos[0]);	// Sólo debería haber un elemento con ese nombre...
			
		elementoDocumento.appendChild(e);
		ManipulacionElement.writeXmlFile(this.documento, direccion);
	}

	public void crearArchivoPorDefecto()
	{
		File f=new File(direccion);
		if (!f.exists())
			crearArchivo();
		
		Document documentoDefecto=ManipulacionElement.getDocumento(direccion);
		ManipulacionElement.writeXmlFile(documentoDefecto, dirdefecto);
	}
	
	public Opcion getOpcion(String nombre, boolean porDefecto)
	{
		this.documento=ManipulacionElement.getDocumento(porDefecto ? dirdefecto: direccion);
		
		Element elementos[]=ManipulacionElement.nodeListToElementArray(
					documento.getElementsByTagName(nombre));

		try {
			Class clase = Class.forName("opciones."+elementos[0].getNodeName());
			this.opcion=(Opcion)(clase.newInstance());
			this.opcion.setValores(elementos[0]);
		} catch ( java.lang.ClassNotFoundException cnfe) {
			System.out.println("GestorOpciones.getOpcion ClassNotFoundException:");
			System.out.println("  elementos[0].getNodeName()="+elementos[0].getNodeName());
			System.out.println("  nombre                    ="+nombre);
		} catch ( java.lang.InstantiationException ie) {
			System.out.println("GestorOpciones.getOpcion InstantiationException");
		} catch ( java.lang.IllegalAccessException iae) {
			System.out.println("GestorOpciones.getOpcion IllegalAccessException");
		} catch ( Exception e) {
			System.out.println("GestorOpciones.getOpcion Exception");
			e.printStackTrace();
			return null;
		} 
		

		return this.opcion;
	}

	public void setOpcion (Opcion op,int donde)	// donde=		0-> defecto  	=1->normal		=2->ambos
	{
		if (donde==1 || donde==2)// Actualizamos la opción en las opciones en uso
		{
			this.documento=ManipulacionElement.getDocumento(direccion);
			Element elementos[]=ManipulacionElement.nodeListToElementArray(
						documento.getElementsByTagName(op.getNombre()));
			
			for (int i=0; i<elementos.length; i++)
			{
				if (elementos[i].getNodeName().equals(op.getNombre()))
					documento.getElementsByTagName("Opciones").item(0).removeChild(elementos[i]);
			}
			
			documento.getElementsByTagName("Opciones").item(0).appendChild( op.getRepresentacionElement(this.documento) );
			ManipulacionElement.writeXmlFile(documento, direccion);
		}
		
		
		if (donde==0 || donde==2)// Actualizamos la opción en las opciones por defecto
		{
			Document docdefecto=ManipulacionElement.getDocumento(dirdefecto);
			Element elementos[]=ManipulacionElement.nodeListToElementArray(
						docdefecto.getElementsByTagName(op.getNombre()));
			
			for (int i=0; i<elementos.length; i++)
			{
				if (elementos[i].getNodeName().equals(op.getNombre()))
					docdefecto.getElementsByTagName("Opciones").item(0).removeChild(elementos[i]);
			}
			
			docdefecto.getElementsByTagName("Opciones").item(0).appendChild( op.getRepresentacionElement(docdefecto) );
			ManipulacionElement.writeXmlFile(docdefecto, dirdefecto);
		}		
		
	}
	
	
	public void crearArchivo(String fichero)
	{
		this.documento=ManipulacionElement.getDocumento(direccion);
		if ( !((fichero.charAt(fichero.length()-1)=='L' || fichero.charAt(fichero.length()-1)=='l') &&
				(fichero.charAt(fichero.length()-2)=='M' || fichero.charAt(fichero.length()-2)=='m') &&
				(fichero.charAt(fichero.length()-3)=='X' || fichero.charAt(fichero.length()-3)=='x') &&
				(fichero.charAt(fichero.length()-4)=='.')) )
				fichero=fichero+".xml";
		ManipulacionElement.writeXmlFile(this.documento, fichero);
		
	}
	
	public boolean cargarArchivo(String fichero)
	{
		this.documento=ManipulacionElement.getDocumento(fichero);
		Element e=this.documento.getDocumentElement();
		if (e.getTagName().equals("Opciones"))
		{
			Element opciones[]=ManipulacionElement.getChildElements(e);
			
			/*
			// Nos situamos sobre directorio de la aplicación
			File directorioAplicacion=null;
			try {
				directorioAplicacion=new File(".\\opciones");
			} catch (Exception exc) {
			}
			
			// Listamos todos sus ficheros
			File clases[]=directorioAplicacion.listFiles();
			String nombres[]=new String[clases.length];
			
			
			for (int i=0; i<clases.length; i++)
				nombres[i]=clases[i].getName().replace(".class","").replace(".java","");
			*/
			
			for (int i=0; i<opciones.length; i++)
				if (!ServiciosString.contieneCadena(clasesOpciones,opciones[i].getTagName(),false))
					return false;
		
			
			ManipulacionElement.writeXmlFile(documento, direccion);
			return true;
		}
		return false;
	}
	
	public boolean cargarArchivoPorDefecto()
	{
		this.documento=ManipulacionElement.getDocumento(dirdefecto);
		ManipulacionElement.writeXmlFile(this.documento, direccion);
		return true;
	}
	
	
}