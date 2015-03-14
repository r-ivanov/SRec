package utilidades;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Ofrece servicios genéricos para la manipulación de objetos Element. Clase de
 * servicios.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class ManipulacionElement {

	/**
	 * Obtiene un listado de los hijos de un nodo, filtrando los que no son
	 * Elements
	 * 
	 * @param e
	 *            Element del cual se extraerán los hijos Element
	 * @return hijos Element de e
	 */
	public static Element[] getChildElements(Element e) {
		NodeList nl = e.getChildNodes();
		return nodeListToElementArray(nl);
	}

	/**
	 * Convierte un NodeList en un array de Elements
	 * 
	 * @param nl
	 *            NodeList que será convertida en un array de Elements
	 * @return array de Elements que formaban parte de nl
	 */
	public static Element[] nodeListToElementArray(NodeList nl) {
		int num_elem = 0;
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof Element) {
				num_elem++;
			}
		}

		Element el[] = new Element[num_elem];
		num_elem = 0;
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof Element) {
				el[num_elem] = (Element) (nl.item(i));
				num_elem++;
			}
		}

		return el;
	}

	public static void copiarXML(String ficheroEntrada, String ficheroSalida) {
		Document d = getDocumento(ficheroEntrada);
		writeXmlFile(d, ficheroSalida);
	}

	/**
	 * Obtiene el Document de un fichero escrito en formato XML
	 * 
	 * @param fichero
	 *            dirección del fichero XML
	 * @return Document del fichero pasado como parámetro
	 */
	public static Document getDocumento(String fichero) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document documento = null;

		try {
			builder = factory.newDocumentBuilder();
			documento = builder.parse(new File(fichero));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return documento;
	}
	
	/**
	 * Obtiene el Document de un input stream en formato XML
	 * 
	 * @param inputStream
	 *            input stream del fichero XML
	 * @return Document del input stream pasado como parámetro.
	 */
	public static Document getDocumento(InputStream inputStream) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document documento = null;

		try {
			builder = factory.newDocumentBuilder();
			documento = builder.parse(inputStream);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return documento;
	}

	/**
	 * Crea y devuelve un nuevo documento.
	 * 
	 * @return Nuevo documento.
	 */
	public static Document getDocument() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document documento = null;

		try {
			builder = factory.newDocumentBuilder();
			documento = builder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documento;
	}

	/**
	 * Escribe un fichero en formato XML, dado un Document
	 * 
	 * @param d
	 *            Document que se escribirá en fichero
	 * @param fichero
	 *            nombre del fichero que se va a escribir
	 */
	public static void writeXmlFile(Document d, String fichero) {
		try {
			// Prepara el Documento DOM para escribirlo
			Source source = new DOMSource(d);

			// Prepara el fichero de salida
			File file = new File(fichero);
			Result result = new StreamResult(file);

			// Escribimos el fichero
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}