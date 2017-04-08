package tests.datos;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import utilidades.SsooValidator;
import utilidades.Texto;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Clase de test que comprueba que todos los textos de cualquier idioma tienen su traducción correcta
 * 
 * @author Daniel Arroyo Cortés
 *
 */
@RunWith(Parameterized.class)
public class TextosIdiomas {
	
	//	Lista con texto por defecto para cada idioma
	private static ArrayList<String> listaTextoDefectoIdiomas = new ArrayList<String>();
	
	//	Lista de idiomas
	private static ArrayList<String> listaIdiomas = new ArrayList<String>();
	
	//	Lista de listas de string. Cada lista un idioma. Cada posición la traducción de un elemento
	//		a ese idioma.
	private static ArrayList<ArrayList<String>> listaTraducciones = new ArrayList<>();
	
	//	Lista con id's de las traducciones hechas
	private static ArrayList<String> listaIds = new ArrayList<>();
	
	//	Variables para tests
	private String id;
	private String idioma;
	private String traduccion;
	private String idiomaDefecto;
	
	/**
	 * Constructor para cada test
	 * 
	 * @param id
	 * 		Id de la linea del fichero XML que queremos traducir
	 * 
	 * @param idioma
	 * 		Idioma del que queremos obtener la traducción
	 * 
	 * @param traduccion
	 * 		Resultado de traducir ese id a ese idioma
	 * 
	 * @param idiomaDefecto
	 * 		Texto que se devuelve por defecto si no existe traducción
	 * 
	 */
	public TextosIdiomas(String traduccion, String idiomaDefecto, String id, String idioma){
		this.id = id;
		this.idioma = idioma;
		this.traduccion = traduccion;
		this.idiomaDefecto = idiomaDefecto;
	}
	
	/**
	 * Acciones previas al test, inicializamos
	 */	
	public static void inicializarTest(){
		try{
			inicializar();
		}catch(Exception e){
			System.out.println("Error al inicializar parámetros de test");
			System.out.println(e);
		}
	}
	
	/**
	 * Parámetros del test, son los del constructor
	 */
	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		inicializarTest();
		Object[][] retorno = new Object[listaIds.size()*listaIdiomas.size()][4];
		int contador = 0;
		for(int i=0;i<listaIdiomas.size();i++){
			for(int j=0;j<listaIds.size();j++){
				retorno[contador][0]=listaTraducciones.get(i).get(j);
				retorno[contador][1]=listaTextoDefectoIdiomas.get(i);
				retorno[contador][2]=listaIds.get(j);
				retorno[contador][3]=listaIdiomas.get(i);
				contador++;
			}
		}
        return Arrays.asList(retorno);
	}
	
	/**
	 * Test que se ejecutará tantas veces como posiciones 
	 * 	tenga retorno = número de id's únicas * número idiomas
	 */
	@Test
	public void test() {
			if(!id.equals("DEFECTO")){
				assertThat(
						"Error de traducción en id: '"+id+"' e idioma '"+idioma+"'",
						traduccion,
						not(idiomaDefecto));	
			}
    }
	
	/**
	 * Inicializa todas las listas necesarias para realizar el test
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static void inicializar() throws ParserConfigurationException, SAXException, IOException{
		
		//	Mapa para guardar id's unicas
		Map<String,String> mapaIds = new HashMap<String,String>();	
		
		//	Abrimos fichero XML
		File filesXML;
		
		if(!SsooValidator.isUnix()){	//	No Linux
			filesXML = new File("Sources\\srec\\datos\\Textos.xml");
		}else{							//	Si linux			
			filesXML = new File("Sources/srec/datos/Textos.xml");
		}
		
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(filesXML);
	    
	    //	Rellenamos idiomas y textos por defecto
    	NodeList nList2 = doc.getElementsByTagName("IdiomaDef");
    	for(int i = 0; i<nList2.getLength(); i++){
    		Node nNode2 = nList2.item(i);
    		if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
		        Element eElement = (Element) nNode2;	
		        String idioma = eElement.getAttribute("id");
		        listaIdiomas.add(idioma);
		        listaTextoDefectoIdiomas.add(Texto.get("DEFECTO",idioma));
	        }
    	}    	
	    
	    //	Obtenemos las id sin repetir de Textos.xml y las metemos al mapa
	    NodeList nList = doc.getElementsByTagName("T");

	    for (int i= 0; i< nList.getLength(); i++) {

	    	Node nNode = nList.item(i);

	    	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		        Element eElement = (Element) nNode;	
		        String id = eElement.getAttribute("id");
		        mapaIds.put(id, "");
	        }
	    }
	    
	    //	Rellenamos lista id = Copia de mapa
	    for(String id:mapaIds.keySet()){
	    	listaIds.add(id);
	    }
	    
	    //	Finalmente rellenamos la lista de traducciones
	    for(int i=0;i<listaIdiomas.size();i++){
	    	String idioma = listaIdiomas.get(i);
	    	listaTraducciones.add(new ArrayList<String>());
	    	for(String id:listaIds){	    		
	    		listaTraducciones.get(listaTraducciones.size()-1).add(Texto.get(id, idioma));
	    	}
	    }
	}
}