/**
	Ofrece servicios genéricos sobre manipulación de Element
	Clase de servicios.
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/
package utilidades;





import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.*;

import java.io.File;
import java.io.IOException;



public class ManipulacionElement
{
	/**
		Obtiene un listado de los hijos de un nodo, filtrando los que no son Elements
		
		@param e Element del cual se extraerán los hijos Element
		@return hijos Element de e
	*/
	public static Element[] getChildElements(Element e)
	{
		NodeList nl = e.getChildNodes();
		return nodeListToElementArray(nl);
	}

	/**
		Convierte un NodeList en un array de Elements
		
		@param nl NodeList que será convertida en un array de Elements
		@return array de Elements que formaban parte de nl
	*/
	public static Element[] nodeListToElementArray(NodeList nl)
	{
		int num_elem=0;
		for (int i=0; i<nl.getLength(); i++)
			if (nl.item(i) instanceof Element)
				num_elem++;

		Element el[] = new Element[num_elem];
		num_elem=0;
		for (int i=0; i<nl.getLength(); i++)
			if (nl.item(i) instanceof Element)
			{
				el[num_elem]=(Element)(nl.item(i));
				num_elem++;
			}

		return el;
	}
	
	/**
		Muestra la estructura del árbol por terminal
		
		@param e árbol DOM que se mostrará
		@param level valor que determina el margen que se dejará en el terminal
	*/
	public static void listChildren(Node e, int level)
	{
		//si es un elemento con espacio de nombres asociado...
		/*if(e instanceof Element && ((Element)e).getLocalName() != null){
			Element el = (Element)e;
			print("[ "+el.getPrefix()+" : "+el.getLocalName()+
				" :: "+el.getNamespaceURI()+" ]", level);
		} else {
			println("[ "+e.getNodeName()+" ]", level);
		}*/
		
		/*for (int z=0; z<level; z++)
			System.out.print("  ");

		if(e instanceof Element)
			System.out.print("[E ");
		else
			System.out.print("[N ");
		System.out.println(e.getNodeName()+" ]");*/


		level++;
		
		// imprimimos los atributos, notese que a pesar de que
		// la variable 'e' debe ser un Elemento para tener atributos,
		// no necesitamos hacer un cast porque la interfaz Node
		// provee los metodos para obtener los atributos
		if(e.hasAttributes())
		{
			NamedNodeMap attributes = e.getAttributes();
			int length = attributes.getLength();
			Attr attr = null;
			for(int i=0; i<length; i++){
				attr = (Attr)attributes.item(i);
				//print(attr.getNodeName()+"='"+attr.getNodeValue()+"'", level);
			}
		}
			
		for(Node node = e.getFirstChild(); node != null; node = node.getNextSibling())
		{
			listChildren(node, level);
		}
	}

	
	public static void copiarXML(String ficheroEntrada, String ficheroSalida)
	{
		Document d=getDocumento(ficheroEntrada);
		writeXmlFile(d,ficheroSalida);
	}
	
	
	/**
		Obtiene el Document de un fichero escrito en formato XML
		
		@param fichero dirección del fichero XML
		@return Document del fichero pasado como parámetro
	*/
	public static Document getDocumento(String fichero)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder =null;
		Document documento =null;

		try{
			builder = factory.newDocumentBuilder();
			documento = builder.parse(new File(fichero));
			
			//listChildren(documento, 0);
		}catch(ParserConfigurationException e){
			//System.out.println("ManipulacionElement No se ha podido crear una instancia de DocumentBuilder");
		}catch(SAXException e){
			//System.out.println("ManipulacionElement Error SAX al parsear el archivo");
		}catch(IOException e){
			//System.out.println("ManipulacionElement Se ha producido un error de entrada salida");
			//System.out.println(e.getCause());
		}
		return documento;
	}
	
	
	public static Document getDocument()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder =null;
		Document documento =null;
		
		try{
			builder = factory.newDocumentBuilder();
			documento = builder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documento;
	}
	
	
	
	/**
		Escribe un fichero en formato XML, dado un Document
		
		@param d Document que se escribirá en fichero
		@param fichero nombre del fichero que se va a escribir
	*/
	public static void writeXmlFile(Document d, String fichero)
	{
		try {
			// Prepara el Documento DOM para escribirlo
			Source source = new DOMSource(d);
    
			// Prepara el fichero de salida
			File file = new File(fichero);
			Result result = new StreamResult(file);

			// Escribimos el fichero
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(source, result);

		} catch (TransformerConfigurationException e) {

		} catch (TransformerException e) {

		}
	}
	
	/**
		Imprime una cadena de caracteres dejando un determinado margen a la izquierda
		
		@param s cadena de caracteres que se mostrará
		@param level margen (número de caracteres que se dejarán en blanco en el terminal)
	*/
	public static void print(String s, int level){
		for(; level>0; level--)
			System.out.print("  ");
		System.out.println(s);
	}
}