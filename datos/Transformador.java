package datos;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import toxml.JavaParser;
import utilidades.ManipulacionElement;
import utilidades.ServiciosString;
import utilidades.SsooValidator;

/**
 * Gestiona toda la manipulaci�n del �rbol DOM, una vez se ha cargado el fichero
 * XML del c�digo java original. Clase de servicios.
 * 
 * @author Antonio P�rez Carrasco
 * @version 2006-2007
 */
public class Transformador {

	private static boolean depurar = false;

	/**
	 * A�ade nodos al Document, en concreto, los de las l�neas de traza
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param es
	 *            nombre del m�todo que se va a signar a la l�nea
	 * @param nombreArray
	 *            nmbre del array que se pasa como apr�metro al m�todo es
	 * @param nombreMetodo
	 *            Nombre del m�todo que se ejecuta.
	 * @return Element a�adido
	 */
	private static Element elementoAnadirEntrada(Document d, String es,
			String nombreArray, String nombreMetodo) {

		// Creamos nuevo elemento y lo a�adimos como hijo a nodos[i]
		// en el futuro habr� que hacer que quede por delante de nodo return

		// 01. Creamos nodo send y a�adimos al �rbol
		Element elem01 = d.createElement("send");
		elem01.setAttribute("message", es);

		// 02. Creamos nodo target y a�adimos como hijo a elem01
		Element elem02 = d.createElement("target");
		elem01.appendChild(elem02);

		// 03. Creamos nodo arguments y a�adimos como hijo a elem01
		Element elem10 = d.createElement("arguments");
		elem01.appendChild(elem10);

		// 04. (hecho)
		// 05. Creamos nodo send y a�adimos como hijo a elem02
		Element elem03 = d.createElement("send");
		elem03.setAttribute("message", "singleton");
		elem02.appendChild(elem03);

		// 06. Creamos nodo target y a�adimos como hijo a elem03
		Element elem04 = d.createElement("target");
		elem03.appendChild(elem04);

		// 07. Creamos nodo arguments y a�adimos como hijo a elem03
		Element elem07 = d.createElement("arguments");
		elem03.appendChild(elem07);

		// 08. Creamos nodo var-ref y a�adimos como hijo a elem04
		Element elem05 = d.createElement("var-ref");
		elem05.setAttribute("name", "Traza");
		elem04.appendChild(elem05);

		// 09. Creamos nodo new y a�adimos como hijo a elem10
		Element elem11 = d.createElement("new");
		elem10.appendChild(elem11);

		//
		Element elem15 = d.createElement("literal-string");
		elem10.appendChild(elem15);
		elem15.setAttribute("value", nombreMetodo);

		//
		Element elem16 = d.createElement("var-ref");
		elem10.appendChild(elem16);
		elem16.setAttribute("name", "nnnnnn01");

		// 10. Creamos nodo type y a�adimos como hijo a elem11
		Element elem12 = d.createElement("type");
		elem12.setAttribute("name", "Estado");
		elem11.appendChild(elem12);

		// 11. Creamos nodo arguments y a�adimos como hijo a elem11
		Element elem13 = d.createElement("arguments");
		elem11.appendChild(elem13);

		// 12. Creamos nodo var-ref y a�adimos como hijo a elem13
		Element elem14 = d.createElement("var-ref");
		elem14.setAttribute("name", nombreArray);
		elem13.appendChild(elem14);

		return elem01;
	}
	
	/**
	 * A�ade nodos al Document, en concreto, los de las l�neas de traza
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param es
	 *            Nombre del m�todo que se va a signar a la l�nea
	 * @param nombreArray
	 *            Nombre del array que se pasa como par�metro al m�todo es
	 * @param RyP
	 *            true = Recorta y Poda, false = Vuelta Atras.
	 * @param nombreSolParcial
	 *            Nombre de la variable que apunta a la soluci�n parcial.
	 * @param nombreMejorSolucion
	 *            Nombre de la variable que apunta a la mejor soluci�n encontrada.
	 * @param nombreCota
	 *            Nombre de la variable que apunta a la cota.
	 * @param nombreMetodo
	 *            Nombre del m�todo que se ejecuta.                    
	 * @return Element a�adido
	 */
	private static Element elementoAnadirEntradaSalida(Document d, String es,
			String nombreArray, boolean RyP, boolean maximizacion, String nombreSolParcial, String nombreMejorSolucion, 
			String nombreCota, String nombreMetodo) {	
		
		// Codigo en xml	
		//    <send message="anadirEntrada">
		//        <target>
		//            <send message="singleton">
		//                <target>
		//                    <var-ref name="Traza"/>
		//                </target>
		//                <arguments/>
		//            </send>
		//        </target>
		//        <arguments>
		//            <new>
		//                <type name="Estado"/>
		//                <arguments>
		//                    <var-ref name="pppppp01"/>
		//					  <literal-boolean value ="true">
		//					  <literal-boolean value ="true">
		//                    <var-ref name="ssssss01"/>
		//                    <var-ref name="mmmmmm01"/>
		//                    <var-ref name="cccccc01"/>
		//                </arguments>
		//            </new>
		//            <literal-string value="buscar01a"/>
		//            <var-ref name="nnnnnn01"/>
		//        </arguments>
		//    </send>
		
		// Codigo transformado de xml a java	
		// Traza.singleton().anadirEntrada(new Estado(pppppp01, RyP, maximizacion, ssssss01, mmmmmm01, cccccc01),"buscar01a",nnnnnn01);
		
		// Creamos nuevo elemento y lo a�adimos como hijo a nodos[i]
		// en el futuro habr� que hacer que quede por delante de nodo return

		// 01. Creamos nodo send y a�adimos al �rbol
		Element elem01 = d.createElement("send");
		elem01.setAttribute("message", es);

		// 02. Creamos nodo target y a�adimos como hijo a elem01
		Element elem02 = d.createElement("target");
		elem01.appendChild(elem02);

		// 03. Creamos nodo arguments y a�adimos como hijo a elem01
		Element elem10 = d.createElement("arguments");
		elem01.appendChild(elem10);

		// 04. (hecho)
		// 05. Creamos nodo send y a�adimos como hijo a elem02
		Element elem03 = d.createElement("send");
		elem03.setAttribute("message", "singleton");
		elem02.appendChild(elem03);

		// 06. Creamos nodo target y a�adimos como hijo a elem03
		Element elem04 = d.createElement("target");
		elem03.appendChild(elem04);

		// 07. Creamos nodo arguments y a�adimos como hijo a elem03
		Element elem07 = d.createElement("arguments");
		elem03.appendChild(elem07);

		// 08. Creamos nodo var-ref y a�adimos como hijo a elem04
		Element elem05 = d.createElement("var-ref");
		elem05.setAttribute("name", "Traza");
		elem04.appendChild(elem05);

		// 09. Creamos nodo new y a�adimos como hijo a elem10
		Element elem11 = d.createElement("new");
		elem10.appendChild(elem11);

		//
		Element elem15 = d.createElement("literal-string");
		elem10.appendChild(elem15);
		elem15.setAttribute("value", nombreMetodo);

		//
		Element elem16 = d.createElement("var-ref");
		elem10.appendChild(elem16);
		elem16.setAttribute("name", "nnnnnn01");

		// 10. Creamos nodo type y a�adimos como hijo a elem11
		Element elem12 = d.createElement("type");
		elem12.setAttribute("name", "Estado");
		elem11.appendChild(elem12);

		// 11. Creamos nodo arguments y a�adimos como hijo a elem11
		Element elem13 = d.createElement("arguments");
		elem11.appendChild(elem13);

		// 12. Creamos nodo var-ref y a�adimos como hijo a elem13 (array de
		// par�metros o valor de retorno)
		Element elem14 = d.createElement("var-ref");
		elem14.setAttribute("name", nombreArray);
		elem13.appendChild(elem14);
		
		// 13. Creamos nodo literal-boolean y a�adimos como hijo a elem13 (RyP)
		Element elem17 = d.createElement("literal-boolean");
		elem17.setAttribute("value", "" + RyP);
		elem13.appendChild(elem17);
		
		// 13. Creamos nodo literal-boolean y a�adimos como hijo a elem13 (maximizacion)
		Element elem18 = d.createElement("literal-boolean");
		elem18.setAttribute("value", "" + maximizacion);
		elem13.appendChild(elem18);

		// 14. Creamos nodo var-ref y a�adimos como hijo a elem13 (solucion parcial)
		Element elem19 = d.createElement("var-ref");
		elem19.setAttribute("name", nombreSolParcial);
		elem13.appendChild(elem19);
		
		// 15. Creamos nodo var-ref y a�adimos como hijo a elem13 (mejor solucion)
		Element elem20 = d.createElement("var-ref");
		elem20.setAttribute("name", nombreMejorSolucion);
		elem13.appendChild(elem20);

		Element elem21;
		if(RyP) {
			// 16. Creamos nodo var-ref y a�adimos como hijo a elem13 (cota)
			elem21 = d.createElement("var-ref");
			elem21.setAttribute("name", nombreCota);
		}else {
			// 16. Creamos nodo literal-number con valor "-1" y a�adimos 
			// como hijo a elem13 (cota)
			elem21 = d.createElement("literal-number");
			elem21.setAttribute("kind", "int");
			elem21.setAttribute("value", "-1");
		}
		elem13.appendChild(elem21);
		
		return elem01;
	}

	/**
	 * A�ade nodos al Document, en conreto, los de las l�neas de traza
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param es
	 *            nombre del m�todo que se va a signar a la l�nea
	 * @param nombreArray
	 *            nmbre del array que se pasa como par�metro al m�todo es
	 * @param nombreEstr
	 *            Nombre del par�metro de la estructura.
	 * @param posicEstructuraEnParam
	 *            Posici�n de la estructura en los par�metros.
	 * @param nombreMetodo
	 *            Nombre del m�todo que se ejecuta.
	 * @return Element a�adido
	 */
	private static Element elementoAnadirEntradaSalida(Document d, String es,
			String nombreArray, String nombreEstr, int posicEstructuraEnParam,
			String nombreMetodo) {

		// Creamos nuevo elemento y lo a�adimos como hijo a nodos[i]
		// en el futuro habr� que hacer que quede por delante de nodo return

		// 01. Creamos nodo send y a�adimos al �rbol
		Element elem01 = d.createElement("send");
		elem01.setAttribute("message", es);

		// 02. Creamos nodo target y a�adimos como hijo a elem01
		Element elem02 = d.createElement("target");
		elem01.appendChild(elem02);

		// 03. Creamos nodo arguments y a�adimos como hijo a elem01
		Element elem10 = d.createElement("arguments");
		elem01.appendChild(elem10);

		// 04. (hecho)
		// 05. Creamos nodo send y a�adimos como hijo a elem02
		Element elem03 = d.createElement("send");
		elem03.setAttribute("message", "singleton");
		elem02.appendChild(elem03);

		// 06. Creamos nodo target y a�adimos como hijo a elem03
		Element elem04 = d.createElement("target");
		elem03.appendChild(elem04);

		// 07. Creamos nodo arguments y a�adimos como hijo a elem03
		Element elem07 = d.createElement("arguments");
		elem03.appendChild(elem07);

		// 08. Creamos nodo var-ref y a�adimos como hijo a elem04
		Element elem05 = d.createElement("var-ref");
		elem05.setAttribute("name", "Traza");
		elem04.appendChild(elem05);

		// 09. Creamos nodo new y a�adimos como hijo a elem10
		Element elem11 = d.createElement("new");
		elem10.appendChild(elem11);

		//
		Element elem15 = d.createElement("literal-string");
		elem10.appendChild(elem15);
		elem15.setAttribute("value", nombreMetodo);

		//
		Element elem16 = d.createElement("var-ref");
		elem10.appendChild(elem16);
		elem16.setAttribute("name", "nnnnnn01");

		// 10. Creamos nodo type y a�adimos como hijo a elem11
		Element elem12 = d.createElement("type");
		elem12.setAttribute("name", "Estado");
		elem11.appendChild(elem12);

		// 11. Creamos nodo arguments y a�adimos como hijo a elem11
		Element elem13 = d.createElement("arguments");
		elem11.appendChild(elem13);

		// 12. Creamos nodo var-ref y a�adimos como hijo a elem13 (array de
		// par�metros o valor de retorno)
		Element elem14 = d.createElement("var-ref");
		elem14.setAttribute("name", nombreArray);
		elem13.appendChild(elem14);

		// 13. Creamos nodo var-ref y a�adimos como hijo a elem13 (estructura)
		Element elem17 = d.createElement("var-ref");
		elem17.setAttribute("name", nombreEstr);
		elem13.appendChild(elem17);

		// 14. Creamos nodo var-ref y a�adimos como hijo a elem13 (estructura)
		Element elem18 = d.createElement("literal-number");
		elem18.setAttribute("kind", "in");
		elem18.setAttribute("value", "" + posicEstructuraEnParam);
		elem13.appendChild(elem18);

		// 15. Creamos nodo var-ref y a�adimos como hijo a elem13 (array de
		// indices)
		Element elem19 = d.createElement("var-ref");
		elem19.setAttribute("name", "iiiiii01");
		elem13.appendChild(elem19);

		return elem01;
	}

	/**
	 * A�ade nodos al Document, en conreto, los de las l�neas de traza
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param es
	 *            nombre del m�todo que se va a signar a la l�nea
	 * @param nombreArray
	 *            nmbre del array que se pasa como par�metro al m�todo es
	 * @param nombreEstr
	 *            Nombre del par�metro de la estructura.
	 * @param posicionEstructuraEnParam
	 *            Posici�n de la estructura en los par�metros.
	 * @param nombreMetodo
	 *            Nombre del m�todo que se ejecuta.
	 * @param devuelveValor
	 *            True si el m�todo devuelve valor, false en caso contrario.
	 * @return Element a�adido
	 */
	private static Element elementoAnadirEntradaSalida(Document d, String es,
			String nombreArray, String nombreEstr, int posicEstructuraEnParam,
			String nombreMetodo, boolean devuelveValor) {

		// Creamos nuevo elemento y lo a�adimos como hijo a nodos[i]
		// en el futuro habr� que hacer que quede por delante de nodo return

		// 01. Creamos nodo send y a�adimos al �rbol
		Element elem01 = d.createElement("send");
		elem01.setAttribute("message", es);

		// 02. Creamos nodo target y a�adimos como hijo a elem01
		Element elem02 = d.createElement("target");
		elem01.appendChild(elem02);

		// 03. Creamos nodo arguments y a�adimos como hijo a elem01
		Element elem10 = d.createElement("arguments");
		elem01.appendChild(elem10);

		// 04. (hecho)
		// 05. Creamos nodo send y a�adimos como hijo a elem02
		Element elem03 = d.createElement("send");
		elem03.setAttribute("message", "singleton");
		elem02.appendChild(elem03);

		// 06. Creamos nodo target y a�adimos como hijo a elem03
		Element elem04 = d.createElement("target");
		elem03.appendChild(elem04);

		// 07. Creamos nodo arguments y a�adimos como hijo a elem03
		Element elem07 = d.createElement("arguments");
		elem03.appendChild(elem07);

		// 08. Creamos nodo var-ref y a�adimos como hijo a elem04
		Element elem05 = d.createElement("var-ref");
		elem05.setAttribute("name", "Traza");
		elem04.appendChild(elem05);

		// 09. Creamos nodo new y a�adimos como hijo a elem10
		Element elem11 = d.createElement("new");
		elem10.appendChild(elem11);

		//
		Element elem15 = d.createElement("literal-string");
		elem10.appendChild(elem15);
		elem15.setAttribute("value", nombreMetodo);

		//
		Element elem16 = d.createElement("var-ref");
		elem10.appendChild(elem16);
		elem16.setAttribute("name", "nnnnnn01");

		//
		Element elem20 = d.createElement("literal-boolean");
		elem10.appendChild(elem20);
		elem20.setAttribute("value", "" + devuelveValor);

		// 10. Creamos nodo type y a�adimos como hijo a elem11
		Element elem12 = d.createElement("type");
		elem12.setAttribute("name", "Estado");
		elem11.appendChild(elem12);

		// 11. Creamos nodo arguments y a�adimos como hijo a elem11
		Element elem13 = d.createElement("arguments");
		elem11.appendChild(elem13);

		// 12. Creamos nodo var-ref y a�adimos como hijo a elem13 (array de
		// par�metros o valor de retorno)
		Element elem14 = d.createElement("var-ref");
		elem14.setAttribute("name", nombreArray);
		elem13.appendChild(elem14);

		// 13. Creamos nodo var-ref y a�adimos como hijo a elem13 (estructura)
		Element elem17 = d.createElement("var-ref");
		elem17.setAttribute("name", nombreEstr);
		elem13.appendChild(elem17);

		// 14. Creamos nodo var-ref y a�adimos como hijo a elem13 (estructura)
		Element elem18 = d.createElement("literal-number");
		elem18.setAttribute("kind", "in");
		elem18.setAttribute("value", "" + posicEstructuraEnParam);
		elem13.appendChild(elem18);

		// 15. Creamos nodo var-ref y a�adimos como hijo a elem13 (array de
		// indices)
		Element elem19 = d.createElement("var-ref");
		elem19.setAttribute("name", "iiiiii01");
		elem13.appendChild(elem19);

		return elem01;
	}

	/**
	 * A�ade nodos al Document, en concreto, los de las l�neas de traza
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param es
	 *            nombre del m�todo que se va a signar a la l�nea
	 * @param nombreArray
	 *            Nombre del array que se pasa como apr�metro al m�todo es
	 * @param devuelveValor
	 *            True si el m�todo devuelve valor, false en caso contrario.
	 * @return Element a�adido
	 */
	private static Element elementoAnadirSalida(Document d, String es,
			String nombreArray, boolean devuelveValor) {

		// Creamos nuevo elemento y lo a�adimos como hijo a nodos[i]
		// en el futuro habr� que hacer que quede por delante de nodo return

		// 01. Creamos nodo send y a�adimos al �rbol
		Element elem01 = d.createElement("send");
		elem01.setAttribute("message", es);

		// 02. Creamos nodo target y a�adimos como hijo a elem01
		Element elem02 = d.createElement("target");
		elem01.appendChild(elem02);

		// 03. Creamos nodo arguments y a�adimos como hijo a elem01
		Element elem10 = d.createElement("arguments");
		elem01.appendChild(elem10);

		// 04. (hecho)
		// 05. Creamos nodo send y a�adimos como hijo a elem02
		Element elem03 = d.createElement("send");
		elem03.setAttribute("message", "singleton");
		elem02.appendChild(elem03);

		// 06. Creamos nodo target y a�adimos como hijo a elem03
		Element elem04 = d.createElement("target");
		elem03.appendChild(elem04);

		// 07. Creamos nodo arguments y a�adimos como hijo a elem03
		Element elem07 = d.createElement("arguments");
		elem03.appendChild(elem07);

		// 08. Creamos nodo var-ref y a�adimos como hijo a elem04
		Element elem05 = d.createElement("var-ref");
		elem05.setAttribute("name", "Traza");
		elem04.appendChild(elem05);

		// 09. Creamos nodo new y a�adimos como hijo a elem10
		Element elem11 = d.createElement("new");
		elem10.appendChild(elem11);

		Element elem15 = d.createElement("literal-boolean");
		elem10.appendChild(elem15);
		elem15.setAttribute("value", "" + devuelveValor);

		// 10. Creamos nodo type y a�adimos como hijo a elem11
		Element elem12 = d.createElement("type");
		elem12.setAttribute("name", "Estado");
		elem11.appendChild(elem12);

		// 11. Creamos nodo arguments y a�adimos como hijo a elem11
		Element elem13 = d.createElement("arguments");
		elem11.appendChild(elem13);

		// 12. Creamos nodo var-ref y a�adimos como hijo a elem13
		Element elem14 = d.createElement("var-ref");
		elem14.setAttribute("name", nombreArray);
		elem13.appendChild(elem14);

		return elem01;
	}

	/**
	 * Inserta los nodos que declaran alg�n array de valores
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param clase
	 *            nombre de la clase a la que pertenece el array
	 * @param nombre
	 *            nmbre del array que queremos declarar
	 * @param num_param
	 *            n�mero de valores que tendr� el array
	 * @return Element a�adido
	 */
	private static Element elementoDeclaracion(Document d, String clase,
			String nombre, int num_param) {
		// 01. Creamos nodo local-variable y a�adimos al �rbol
		Element elem01 = d.createElement("local-variable");
		elem01.setAttribute("name", nombre);

		// 02. Creamos nodo type
		Element elem02 = d.createElement("type");
		elem02.setAttribute("dimensions", "1");
		elem02.setAttribute("name", clase);
		elem01.appendChild(elem02);

		// 03. Creamos nodo new-array
		Element elem03 = d.createElement("new-array");
		elem03.setAttribute("dimensions", "1");
		elem01.appendChild(elem03);

		// 04. Creamos nodo type
		Element elem04 = d.createElement("type");
		elem04.setAttribute("name", clase);
		elem03.appendChild(elem04);

		// 05. Creamos nodo dim-expr
		Element elem05 = d.createElement("dim-expr");
		elem03.appendChild(elem05);

		// 06. Creamos nodo literal-number
		Element elem07 = d.createElement("literal-number");
		elem07.setAttribute("kind", "int");
		elem07.setAttribute("value", Integer.toString(num_param));
		elem05.appendChild(elem07);

		return elem01;
	}

	/**
	 * Inserta los nodos que asigan valores a una posici�n del array
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param indice
	 *            posici�n del array a la que vamos a asignar un valor
	 * @param nombre
	 *            nombre del valor que vamos a asignar a una posici�n del array
	 * @param nombreArray
	 *            nombre del array al que vamos a asignar un valor
	 * @return Element a�adido
	 */
	private static Element elementosAsigParametros(Document d, int indice,
			String nombre, String nombreArray) {
		// 01. Creamos nodo assignment-expr y a�adimos al �rbol
		Element elem01 = d.createElement("assignment-expr");
		elem01.setAttribute("op", "=");

		// 02. Creamos nodo lvalue
		Element elem02 = d.createElement("lvalue");
		elem01.appendChild(elem02);

		// 03. Creamos nodo array-ref
		Element elem03 = d.createElement("array-ref");
		elem02.appendChild(elem03);

		// 04. Creamos nodo base
		Element elem04 = d.createElement("base");
		elem03.appendChild(elem04);

		// 05. Creamos nodo var-ref
		Element elem05 = d.createElement("var-ref");
		elem05.setAttribute("name", nombreArray);
		elem04.appendChild(elem05);

		// 06. Creamos nodo offset
		Element elem06 = d.createElement("offset");
		elem03.appendChild(elem06);

		// 07. Creamos nodo literal-number
		Element elem07 = d.createElement("literal-number");
		elem07.setAttribute("kind", "int");
		elem07.setAttribute("value", Integer.toString(indice));
		elem06.appendChild(elem07);

		// 08. Creamos nodo var-ref
		Element elem08 = d.createElement("var-ref");
		elem08.setAttribute("name", nombre);
		elem01.appendChild(elem08);

		return elem01;
	}

	/**
	 * M�todo que gestiona la inserci�n de l�neas en el c�digo original Java
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param claseAlgoritmo
	 *            Informaci�n de la Clase.
	 * @return 0 si todo va bien, n�meros para distintos tipos de errores
	 */
	public static int insertarLineasNecesarias(Document d,
			ClaseAlgoritmo claseAlgoritmo) {
		// Descartamos casos err�neos
		if (d.getElementsByTagName("interface").getLength() > 0) {
			return 1; // Error: es una interfaz
		} else if (d.getElementsByTagName("block").getLength() == 0) {
			return 2; // Error: no hay bloques de sentencias
		}

		// Revisamos que la clase no sea abstracta
		Element[] inspeccion_clase = ManipulacionElement
				.nodeListToElementArray(d.getElementsByTagName("class"));
		if (inspeccion_clase[0].getAttribute("abstract").equals("true")) {
			return 3; // Error: la clase es abstracta
		}

		// Necesitamos introducir un import: " import utilidades.*; "
		// y adem�s en el lugar adecuado, al principio de todo
		Element elemento = d.getDocumentElement();
		Element elementoImport = d.createElement("import");
		elementoImport.setAttribute("module", "datos.*");
		elemento.appendChild(elementoImport);

		while (elemento.getFirstChild() != elementoImport) {
			Node nodeAux = elemento.getFirstChild();
			elemento.removeChild(nodeAux);
			elemento.appendChild(nodeAux);
		}

		// Necesitamos que la clase sea p�blica, no se lo exigimos a la clase de
		// origen, pero s� a la que estamos creando
		Element elClase[] = ManipulacionElement.nodeListToElementArray(d
				.getElementsByTagName("class"));
		elClase[0].setAttribute("visibility", "public");

		// - Buscamos todos los nodos de m�todos
		// - Por cada metodo, si es void, le tratamos de forma especial (metemos
		// lineas distintas)
		// - En caso contrario, por cada return, si est� dentro de block
		// actuamos normal y si no, lo metemos en block

		Element metodos[] = ManipulacionElement.nodeListToElementArray(d
				.getElementsByTagName("method"));
		for (int i = 0; i < metodos.length; i++) {
			metodos[i].setAttribute("static", "true");
		}

		for (int i = 0; i < metodos.length; i++) {
			for (int j = 0; j < claseAlgoritmo.getNumMetodos(); j++) {

				if (claseAlgoritmo.getMetodo(j).getMarcadoProcesar()
						&& esMetodoEscogido(claseAlgoritmo.getMetodo(j),
								metodos[i])) {
					// El tratamiento de los m�todos var�a s�lo en la generaci�n
					// de l�neas de salida, por lo que en ambas opciones se 
					// generan de la misma manera las l�neas de entrada 
					// (mediante el m�todo "generacionLineasEntrada")
					if (claseAlgoritmo.getMetodo(j).getTecnica() == MetodoAlgoritmo.TECNICA_REC) {
						if (tipoRetornoMetodo(metodos[i]).equals("void")) {
							tratamientoMetodosVoidREC(d, metodos[i]);
						} else {
							tratamientoMetodosConValorREC(d, metodos[i]);
						}
					} else if (claseAlgoritmo.getMetodo(j).getTecnica() == MetodoAlgoritmo.TECNICA_DYV) {
						if (tipoRetornoMetodo(metodos[i]).equals("void")) {
							tratamientoMetodosVoidDYV(d, metodos[i],
									claseAlgoritmo.getMetodo(j)
											.getIndiceEstructura() - 1,
									claseAlgoritmo.getMetodo(j).getIndices());
						} else {
							tratamientoMetodosConValorDYV(d, metodos[i],
									claseAlgoritmo.getMetodo(j)
											.getIndiceEstructura() - 1,
									claseAlgoritmo.getMetodo(j).getIndices());
						}
					}else if (claseAlgoritmo.getMetodo(j).getTecnica() == MetodoAlgoritmo.TECNICA_AABB) {
						if (tipoRetornoMetodo(metodos[i]).equals("void")) {
							tratamientoMetodosVoidAABB(d, metodos[i], 
									claseAlgoritmo.getMetodo(j).getMejorSol(), 
									claseAlgoritmo.getMetodo(j).getSolParcial(), 
									claseAlgoritmo.getMetodo(j).getRyP(),
									claseAlgoritmo.getMetodo(j).getCota(), 
									claseAlgoritmo.getMetodo(j).getMaximizacion());
						} else {
							tratamientoMetodosConValorAABB(d, metodos[i], 
									claseAlgoritmo.getMetodo(j).getMejorSol(), 
									claseAlgoritmo.getMetodo(j).getSolParcial(), 
									claseAlgoritmo.getMetodo(j).getRyP(),
									claseAlgoritmo.getMetodo(j).getCota(), 
									claseAlgoritmo.getMetodo(j).getMaximizacion());
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * Comprueba que la informaci�n del m�todo especificado se corresponde con
	 * la del elemento.
	 * 
	 * @param m
	 *            Informaci�n del m�todo.
	 * @param e
	 *            Elemento.
	 * 
	 * @return true si se corresponde, false en caso contrario.
	 */
	private static boolean esMetodoEscogido(MetodoAlgoritmo m, Element e) {
		if (depurar) {
			System.out.println("____  esMetodoEscogido  ____");
		}

		// Revisamos el nombre
		String nombreM = m.getNombre();
		String nombreE = e.getAttribute("name");

		if (depurar) {
			System.out.println(" - Nombre: [" + m.getNombre() + "]");
		}
		if (depurar) {
			System.out.println("   Nombre: [" + e.getAttribute("name") + "]");
		}

		if (!nombreM.equals(JavaParser.convReverse(nombreE))) {
			return false;
		}

		// Revisamos los par�metros
		String clasesParamM[] = m.getTiposParametros();
		String clasesParamE[];

		// Recogemos tipos de e
		Element hijosMetodo[] = ManipulacionElement.getChildElements(e);
		Element nodoParametros = null;
		for (int i = 0; i < hijosMetodo.length; i++) {
			if (hijosMetodo[i].getNodeName().equals("formal-arguments")) {
				nodoParametros = hijosMetodo[i];
			}
		}

		Element paramE[] = ManipulacionElement.getChildElements(nodoParametros);
		clasesParamE = new String[paramE.length];
		for (int i = 0; i < paramE.length; i++) {
			clasesParamE[i] = ManipulacionElement.getChildElements(paramE[i])[0]
					.getAttribute("name");
		}

		if (depurar) {
			System.out.println(" - NumParametros: [" + clasesParamM.length
					+ "]");
		}
		if (depurar) {
			System.out.println("   NumParametros: [" + clasesParamE.length
					+ "]");
		}

		// Comparamos n�mero de par�metros
		if (clasesParamM.length != clasesParamE.length) {
			return false;
		}

		// Comparamos tipos de m�todos
		for (int i = 0; i < m.getNumeroParametros(); i++) {
			if (depurar) {
				System.out.println("   TipoParam(" + i + "): ["
						+ clasesParamM[i] + "] vs [" + clasesParamE[i] + "]");
			}
			if (!clasesParamM[i].equals(clasesParamE[i])) {
				return false;
			}
		}

		// Comparamos dimensiones
		for (int i = 0; i < m.getNumeroParametros(); i++) {
			int dimM = m.getDimParametro(i);
			int dimE;
			try {
				dimE = Integer.parseInt(ManipulacionElement
						.getChildElements(paramE[i])[0]
						.getAttribute("dimensions"));
			} catch (java.lang.NumberFormatException nfe) {
				dimE = 0;
			}
			if (depurar) {
				System.out.println("   DimParam(" + i + "): [" + dimM
						+ "] vs [" + dimE + "]");
			}
			if (dimM != dimE) {
				return false;
			}

		}
		// Habr�a que revisar el tipo de salida antes de salir pero no hace
		// falta:
		// no puede haber dos m�todos con los mismos tipos de par�metro y
		// distinto tipo de salida

		if (depurar) {
			System.out.println("   ----> Son iguales ----<");
		}
		return true;
	}

	/**
	 * M�todo auxiliar que gestiona la inserci�n de l�neas en el c�digo original
	 * Java
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param metodo
	 *            M�todo al que se a�aden nodos adicionales para las l�enas de
	 *            traza
	 */
	private static void tratamientoMetodosVoidREC(Document d, Element metodo) {
		Object valores[] = recoleccionInformacion(metodo);
		// String tipoMetodo=(String)valores[0];// valores[0]=String tipoMetodo
		Element returns[] = (Element[]) valores[1];
		Element argumentos[] = (Element[]) valores[2];
		Element bloqueMetodo = (Element) valores[3];

		generacionLineasEntrada(d, bloqueMetodo, argumentos);

		// Ahora nos ocupamos de las lineas de salida, bas�ndonos en si hay o no
		// nodos return
		if (returns.length == 0) {
			// 01. "Object rrrrrr01[]=new Object[numParam];"
			Element sentenciaDeclObjectrrrrrr01 = elementoDeclaracion(d,
					"Object", "rrrrrr01", argumentos.length);

			// 02. "rrrrrr01[n]=parametroN;" (varias)
			Element sentenciaAsigParam[] = new Element[argumentos.length];
			for (int i = 0; i < argumentos.length; i++) {
				sentenciaAsigParam[i] = elementosAsigParametros(d, i,
						argumentos[i].getAttribute("name"), "rrrrrr01");
			}

			// 03. Linea de Traza para la salida
			Element sentenciaAnadirSalida = Transformador.elementoAnadirSalida(
					d, "anadirSalida", "rrrrrr01", false);

			// A�adimos al bloque del m�todo las lineas de salida generadas
			bloqueMetodo.appendChild(sentenciaDeclObjectrrrrrr01);
			for (int i = 0; i < argumentos.length; i++) {
				bloqueMetodo.appendChild(sentenciaAsigParam[i]);
			}
			bloqueMetodo.appendChild(sentenciaAnadirSalida);

			// Revisamos que no haya sentencia de throw (debe ser la �ltima)
			Element sentenciasBloqueMetodo[] = ManipulacionElement
					.getChildElements(bloqueMetodo);
			for (int i = 0; i < sentenciasBloqueMetodo.length; i++) {
				if (sentenciasBloqueMetodo[i].getNodeName().equals("throw")) {
					bloqueMetodo.removeChild(sentenciasBloqueMetodo[i]);
					bloqueMetodo.appendChild(sentenciasBloqueMetodo[i]);
				}
			}
		} else {
			// Habr� que insertar tantos conjuntos de lineas de salida para cada
			// return que haya as� como para el final del m�todo

			if (depurar) {
				System.out.println("MetodoVoidconReturn  01");
			}
			// 01. "Object rrrrrr01[]=new Object[numParam];"
			Element sentenciaDeclObjectrrrrrr01 = elementoDeclaracion(d,
					"Object", "rrrrrr01", argumentos.length);

			if (depurar) {
				System.out.println("MetodoVoidconReturn  02");
			}
			// 02. "rrrrrr01[n]=parametroN;" (varias)
			Element sentenciaAsigParam[] = new Element[argumentos.length];
			for (int i = 0; i < argumentos.length; i++) {
				sentenciaAsigParam[i] = elementosAsigParametros(d, i,
						argumentos[i].getAttribute("name"), "rrrrrr01");
			}

			if (depurar) {
				System.out.println("MetodoVoidconReturn  03");
			}
			// 03. Linea de Traza para la salida
			Element sentenciaAnadirSalida = Transformador.elementoAnadirSalida(
					d, "anadirSalida", "rrrrrr01", false);

			int hayReturnFinal = -1; // -1=No hay return al final del m�todo
			for (int i = 0; i < returns.length; i++) {
				if (depurar) {
					System.out.println("MetodoVoidconReturn  04");
				}
				Element padreReturn = (Element) returns[i].getParentNode();
				recolocarReturn(d, returns[i], padreReturn);
				if (returns[i].getParentNode() == bloqueMetodo) {
					hayReturnFinal = i;
				}

				padreReturn = (Element) returns[i].getParentNode();
				if (depurar) {
					System.out.println("MetodoVoidconReturn  05 padreReturn="
							+ padreReturn.getNodeName());
				}
				padreReturn.appendChild(sentenciaDeclObjectrrrrrr01
						.cloneNode(true));
				for (int j = 0; j < argumentos.length; j++) {
					padreReturn.appendChild(sentenciaAsigParam[j]
							.cloneNode(true));
				}
				padreReturn.appendChild(sentenciaAnadirSalida.cloneNode(true));

				if (depurar) {
					System.out.println("MetodoVoidconReturn  06");
				}
				padreReturn.removeChild(returns[i]);
				padreReturn.appendChild(returns[i]);
				if (depurar) {
					System.out.println("MetodoVoidconReturn  07");
				}
			}

			if (hayReturnFinal == -1) {
				if (depurar) {
					System.out.println("MetodoVoidconReturn  08");
				}
				// A�adimos al bloque del m�todo las lineas de salida generadas
				bloqueMetodo.appendChild(sentenciaDeclObjectrrrrrr01);
				for (int i = 0; i < argumentos.length; i++) {
					bloqueMetodo.appendChild(sentenciaAsigParam[i]);
				}
				bloqueMetodo.appendChild(sentenciaAnadirSalida);
				if (depurar) {
					System.out.println("MetodoVoidconReturn  09");
				}
			}
		}
	}

	/**
	 * M�todo auxiliar que gestiona la inserci�n de l�neas en el c�digo original
	 * Java
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param metodo
	 *            M�todo al que se a�aden nodos adicionales para las l�enas de
	 *            traza
	 */
	private static void tratamientoMetodosConValorREC(Document d, 
			Element metodo) {
		// Recopilamos nodos interesantes y el tipo del M�todo
		Object valores[] = recoleccionInformacion(metodo);

		String tipoMetodo = (String) valores[0];// valores[0]=String tipoMetodo
		Element returns[] = (Element[]) valores[1];
		Element argumentos[] = (Element[]) valores[2];
		Element bloqueMetodo = (Element) valores[3];

		// Insertamos todas las lineas de entrada, y nos guardamos la primera
		generacionLineasEntrada(d, bloqueMetodo, argumentos);

		// Ahora nos ocupamos de las lineas de salida, bas�ndonos en los nodos
		// return existentes
		for (int j = 0; j < returns.length; j++) {
			Element padreReturn = (Element) returns[j].getParentNode();

			recolocarReturn(d, returns[j], padreReturn);

			// Creamos todas las lineas de salida que necesitamos

			// 01. "int zzzzzz01 = resultado;"
			Element sentenciaVariableRet = d.createElement("local-variable");
			sentenciaVariableRet.setAttribute("name", "zzzzzz01");

			Element tipoRetorno = d.createElement("type"); // Nodo que expresar�
			// el tipo de la
			// variable

			Element nodoTipoRetornoMetodo = nodoTipoRetornoMetodo(metodo);
			String stringTipoVariable = tipoMetodo;
			if (nodoTipoRetornoMetodo.getAttribute("dimensions").length() > 0) 
			// si
			// aparece
			// atributo
			// "dimensions"
			{
				int dimensiones = Integer.parseInt(nodoTipoRetornoMetodo
						.getAttribute("dimensions"));
				// for (int k=0; k<dimensiones; k++)
				// stringTipoVariable=stringTipoVariable+"[]"; // a�adimos "[]"
				// por cada dimension
				stringTipoVariable = stringTipoVariable
						+ ServiciosString.cadenaDimensiones(dimensiones);
			}

			tipoRetorno.setAttribute("name", stringTipoVariable);
			sentenciaVariableRet.appendChild(tipoRetorno);
			Element hijosReturn[] = ManipulacionElement
					.getChildElements(returns[j]);
			sentenciaVariableRet
					.appendChild(((hijosReturn[0].cloneNode(true))));

			// 02. "Object rrrrrr01[]=new Object[1];"
			Element sentenciaDeclObjectrrrrrr01 = elementoDeclaracion(d,
					"Object", "rrrrrr01", 1);

			// 03. "rrrrrr01[0]=zzzzzz01;"
			Element asigParametros = elementosAsigParametros(d, 0, "zzzzzz01",
					"rrrrrr01");

			// 04. Linea de Traza para la salida
			Element sentenciaAnadirSalida = Transformador.elementoAnadirSalida(
					d, "anadirSalida", "rrrrrr01", true);

			// 05. Cambiamos sentencia return para que devuelva siempre la
			// variable zzzzzz01
			Element var_ref = d.createElement("var-ref");
			var_ref.setAttribute("name", "zzzzzz01");

			while (returns[j].getFirstChild() != null) {
				returns[j].removeChild(returns[j].getFirstChild());
			}

			returns[j].appendChild(var_ref);

			// Ahora insertamos las lineas de salida

			padreReturn = (Element) returns[j].getParentNode();
			Element primeraSentenciaPadreReturn = ManipulacionElement
					.getChildElements(padreReturn)[0];
			if (depurar) {
				System.out.println("  Return Paso01");
			}

			//
			Element bmh[] = ManipulacionElement.getChildElements(padreReturn);
			if (depurar) {
				for (int i = 0; i < bmh.length; i++) {
					System.out.println(" -" + bmh[i].getNodeName());
				}
			}

			while (padreReturn.getFirstChild() != returns[j]) {
				Node nodo_aux = padreReturn.getFirstChild();
				padreReturn.removeChild(nodo_aux);
				padreReturn.appendChild(nodo_aux);

			}

			padreReturn.appendChild(sentenciaVariableRet);
			padreReturn.appendChild(sentenciaDeclObjectrrrrrr01);
			padreReturn.appendChild(asigParametros);
			padreReturn.appendChild(sentenciaAnadirSalida);

			// Recolocamos las lineas para que las l�neas reci�n a�adidas est�n
			// al principio del m�todo
			while (padreReturn.getFirstChild() != primeraSentenciaPadreReturn) {
				Node nodo_aux = padreReturn.getFirstChild();
				padreReturn.removeChild(nodo_aux);
				padreReturn.appendChild(nodo_aux);
				if (depurar) {
					System.out.println("buscando primeraSentenciaPadreReturn");
				}
			}
			if (padreReturn.getFirstChild().getNodeName().equals("return")) {
				Node nodo_aux = padreReturn.getFirstChild();
				padreReturn.removeChild(nodo_aux);
				padreReturn.appendChild(nodo_aux);
				if (depurar) {
					System.out.println("recolocando sentencia return");
				}
			}
		}
	}

	/**
	 * M�todo auxiliar que gestiona la inserci�n de l�neas en el c�digo original
	 * Java
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param metodo
	 *            M�todo al que se a�aden nodos adicionales para las l�enas de
	 *            traza
	 * @param paramEstructura
	 *            Posici�n en la que se encuentra la estructura.
	 * @param paramIndices
	 *            �ndices que delimitan los l�mites de la estructura.
	 */
	private static void tratamientoMetodosVoidDYV(Document d, Element metodo,
			int paramEstructura, int paramIndices[]) {
		Object valores[] = recoleccionInformacion(metodo);
		// String tipoMetodo=(String)valores[0];// valores[0]=String tipoMetodo
		Element returns[] = (Element[]) valores[1];
		Element argumentos[] = (Element[]) valores[2];
		Element bloqueMetodo = (Element) valores[3];

		generacionLineasEntrada(d, bloqueMetodo, argumentos, paramEstructura,
				paramIndices);

		// Ahora nos ocupamos de las lineas de salida, bas�ndonos en si hay o no
		// nodos return
		if (returns.length == 0) {
			// 01. "Object rrrrrr01[]=new Object[numParam];"
			Element sentenciaDeclObjectrrrrrr01 = elementoDeclaracion(d,
					"Object", "rrrrrr01", argumentos.length);

			// 01b. "Object eeeeee02=m;"
			Element sentenciaEstructura = elementoDeclaracionAsignacion(d,
					paramEstructura, "eeeeee02",
					(Element) (((Element) (bloqueMetodo.getParentNode()))
							.getElementsByTagName("formal-arguments").item(0)));

			// 02. "rrrrrr01[n]=parametroN;" (varias)
			Element sentenciaAsigParam[] = new Element[argumentos.length];
			for (int i = 0; i < argumentos.length; i++) {
				sentenciaAsigParam[i] = elementosAsigParametros(d, i,
						argumentos[i].getAttribute("name"), "rrrrrr01");
			}

			// 03. Linea de Traza para la salida
			Element sentenciaAnadirSalida = Transformador
					.elementoAnadirEntradaSalida(d, "anadirSalida", "rrrrrr01",
							"eeeeee02", paramEstructura,
							metodo.getAttribute("name"));

			// A�adimos al bloque del m�todo las lineas de salida generadas
			bloqueMetodo.appendChild(sentenciaDeclObjectrrrrrr01);
			bloqueMetodo.appendChild(sentenciaEstructura);
			for (int i = 0; i < argumentos.length; i++) {
				bloqueMetodo.appendChild(sentenciaAsigParam[i]);
			}
			bloqueMetodo.appendChild(sentenciaAnadirSalida);

			// Revisamos que no haya sentencia de throw (debe ser la �ltima)
			Element sentenciasBloqueMetodo[] = ManipulacionElement
					.getChildElements(bloqueMetodo);
			for (int i = 0; i < sentenciasBloqueMetodo.length; i++) {
				if (sentenciasBloqueMetodo[i].getNodeName().equals("throw")) {
					bloqueMetodo.removeChild(sentenciasBloqueMetodo[i]);
					bloqueMetodo.appendChild(sentenciasBloqueMetodo[i]);
				}
			}
		} else {
			// Habr� que insertar tantos conjuntos de lineas de salida para cada
			// return que haya as� como para el final del m�todo

			if (depurar) {
				System.out.println("MetodoVoidconReturn  01");
			}
			// 01. "Object rrrrrr01[]=new Object[numParam];"
			Element sentenciaDeclObjectrrrrrr01 = elementoDeclaracion(d,
					"Object", "rrrrrr01", argumentos.length);

			// 01b. "Object eeeeee02=m;"
			Element sentenciaEstructura = elementoDeclaracionAsignacion(d,
					paramEstructura, "eeeeee02",
					(Element) (((Element) (bloqueMetodo.getParentNode()))
							.getElementsByTagName("formal-arguments").item(0)));

			if (depurar) {
				System.out.println("MetodoVoidconReturn  02");
			}
			// 02. "rrrrrr01[n]=parametroN;" (varias)
			Element sentenciaAsigParam[] = new Element[argumentos.length];
			for (int i = 0; i < argumentos.length; i++) {
				sentenciaAsigParam[i] = elementosAsigParametros(d, i,
						argumentos[i].getAttribute("name"), "rrrrrr01");
			}

			if (depurar) {
				System.out.println("MetodoVoidconReturn  03");
			}
			// 03. Linea de Traza para la salida
			Element sentenciaAnadirSalida = Transformador
					.elementoAnadirEntradaSalida(d, "anadirSalida", "rrrrrr01",
							"eeeeee02", paramEstructura,
							metodo.getAttribute("name"), false);

			int hayReturnFinal = -1; // -1=No hay return al final del m�todo
			for (int i = 0; i < returns.length; i++) {
				if (depurar) {
					System.out.println("MetodoVoidconReturn  04");
				}
				Element padreReturn = (Element) returns[i].getParentNode();
				recolocarReturn(d, returns[i], padreReturn);
				if (returns[i].getParentNode() == bloqueMetodo) {
					hayReturnFinal = i;
				}

				padreReturn = (Element) returns[i].getParentNode();
				if (depurar) {
					System.out.println("MetodoVoidconReturn  05 padreReturn="
							+ padreReturn.getNodeName());
				}
				padreReturn.appendChild(sentenciaDeclObjectrrrrrr01
						.cloneNode(true));
				padreReturn.appendChild(sentenciaEstructura.cloneNode(true));
				for (int j = 0; j < argumentos.length; j++) {
					padreReturn.appendChild(sentenciaAsigParam[j]
							.cloneNode(true));
				}
				padreReturn.appendChild(sentenciaAnadirSalida.cloneNode(true));

				if (depurar) {
					System.out.println("MetodoVoidconReturn  06");
				}
				padreReturn.removeChild(returns[i]);
				padreReturn.appendChild(returns[i]);
				if (depurar) {
					System.out.println("MetodoVoidconReturn  07");
				}
			}

			if (hayReturnFinal == -1) {
				if (depurar) {
					System.out.println("MetodoVoidconReturn  08");
				}
				// A�adimos al bloque del m�todo las lineas de salida generadas
				bloqueMetodo.appendChild(sentenciaDeclObjectrrrrrr01);
				bloqueMetodo.appendChild(sentenciaEstructura);
				for (int i = 0; i < argumentos.length; i++) {
					bloqueMetodo.appendChild(sentenciaAsigParam[i]);
				}
				bloqueMetodo.appendChild(sentenciaAnadirSalida);
				if (depurar) {
					System.out.println("MetodoVoidconReturn  09");
				}
			}
		}
	}

	/**
	 * M�todo auxiliar que gestiona la inserci�n de l�neas en el c�digo original
	 * Java
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param metodo
	 *            M�todo al que se a�aden nodos adicionales para las l�enas de
	 *            traza
	 * @param paramEstructura
	 *            Posici�n en la que se encuentra la estructura.
	 * @param paramIndices
	 *            �ndices que delimitan los l�mites de la estructura.
	 */
	private static void tratamientoMetodosConValorDYV(Document d, 
			Element metodo, int paramEstructura, int paramIndices[]) {

		// Recopilamos nodos interesantes y el tipo del M�todo
		Object valores[] = recoleccionInformacion(metodo);

		String tipoMetodo = (String) valores[0];// valores[0]=String tipoMetodo
		Element returns[] = (Element[]) valores[1];
		Element argumentos[] = (Element[]) valores[2];
		Element bloqueMetodo = (Element) valores[3];

		// Insertamos todas las lineas de entrada, y nos guardamos la primera
		generacionLineasEntrada(d, bloqueMetodo, argumentos, paramEstructura,
				paramIndices);

		// Ahora nos ocupamos de las lineas de salida, bas�ndonos en los nodos
		// return existentes
		for (int j = 0; j < returns.length; j++) {
			Element padreReturn = (Element) returns[j].getParentNode();

			recolocarReturn(d, returns[j], padreReturn);

			// Creamos todas las lineas de salida que necesitamos

			// 01. "int zzzzzz01 = resultado;"
			Element sentenciaVariableRet = d.createElement("local-variable");
			sentenciaVariableRet.setAttribute("name", "zzzzzz01");

			Element tipoRetorno = d.createElement("type"); // Nodo que expresar�
			// el tipo de la
			// variable

			Element nodoTipoRetornoMetodo = nodoTipoRetornoMetodo(metodo);
			String stringTipoVariable = tipoMetodo;
			if (nodoTipoRetornoMetodo.getAttribute("dimensions").length() > 0) // si
			// aparece
			// atributo
			// "dimensions"
			{
				int dimensiones = Integer.parseInt(nodoTipoRetornoMetodo
						.getAttribute("dimensions"));
				for (int k = 0; k < dimensiones; k++) {
					stringTipoVariable = stringTipoVariable + "[]"; // a�adimos
					// "[]" por
					// cada
					// dimension
				}
			}

			tipoRetorno.setAttribute("name", stringTipoVariable);
			sentenciaVariableRet.appendChild(tipoRetorno);
			Element hijosReturn[] = ManipulacionElement
					.getChildElements(returns[j]);
			sentenciaVariableRet
					.appendChild(((hijosReturn[0].cloneNode(true))));

			// 02. "Object rrrrrr01[]=new Object[1];"
			Element sentenciaDeclObjectrrrrrr01 = elementoDeclaracion(d,
					"Object", "rrrrrr01", 1);

			// 02b. "Object eeeeee02[]=new Object[1];"
			Element sentenciaEstructura = elementoDeclaracionAsignacion(d,
					paramEstructura, "eeeeee02",
					(Element) (((Element) (bloqueMetodo.getParentNode()))
							.getElementsByTagName("formal-arguments").item(0)));

			// 03. "rrrrrr01[0]=zzzzzz01;"
			Element asigParametros = elementosAsigParametros(d, 0, "zzzzzz01",
					"rrrrrr01");

			// 04. Linea de Traza para la salida
			Element sentenciaAnadirSalida = Transformador
					.elementoAnadirEntradaSalida(d, "anadirSalida", "rrrrrr01",
							"eeeeee02", paramEstructura/* (-1) */,
							metodo.getAttribute("name"), true);

			// 05. Cambiamos sentencia return para que devuelva siempre la
			// variable zzzzzz01
			Element var_ref = d.createElement("var-ref");
			var_ref.setAttribute("name", "zzzzzz01");

			while (returns[j].getFirstChild() != null) {
				returns[j].removeChild(returns[j].getFirstChild());
			}

			returns[j].appendChild(var_ref);

			// Ahora insertamos las lineas de salida

			padreReturn = (Element) returns[j].getParentNode();
			Element primeraSentenciaPadreReturn = ManipulacionElement
					.getChildElements(padreReturn)[0];
			if (depurar) {
				System.out.println("  Return Paso01");
			}

			//
			Element bmh[] = ManipulacionElement.getChildElements(padreReturn);
			if (depurar) {
				for (int i = 0; i < bmh.length; i++) {
					System.out.println(" -" + bmh[i].getNodeName());
				}
			}

			while (padreReturn.getFirstChild() != returns[j]) {
				Node nodo_aux = padreReturn.getFirstChild();
				padreReturn.removeChild(nodo_aux);
				padreReturn.appendChild(nodo_aux);

			}

			padreReturn.appendChild(sentenciaVariableRet);
			padreReturn.appendChild(sentenciaDeclObjectrrrrrr01);
			padreReturn.appendChild(sentenciaEstructura);
			padreReturn.appendChild(asigParametros);
			padreReturn.appendChild(sentenciaAnadirSalida);

			// Recolocamos las lineas para que las l�neas reci�n a�adidas est�n
			// al principio del m�todo
			while (padreReturn.getFirstChild() != primeraSentenciaPadreReturn) {
				Node nodo_aux = padreReturn.getFirstChild();
				padreReturn.removeChild(nodo_aux);
				padreReturn.appendChild(nodo_aux);
				if (depurar) {
					System.out.println("buscando primeraSentenciaPadreReturn");
				}
			}

			if (padreReturn.getFirstChild().getNodeName().equals("return")) {
				Node nodo_aux = padreReturn.getFirstChild();
				padreReturn.removeChild(nodo_aux);
				padreReturn.appendChild(nodo_aux);
				if (depurar) {
					System.out.println("recolocando sentencia return");
				}
			}
		}
	}
	
	/**
	 * M�todo auxiliar que gestiona la inserci�n de l�neas en el c�digo original
	 * Java
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param metodo
	 *            M�todo al que se a�aden nodos adicionales para las l�enas de
	 *            traza
	 * @param mejorSol
	 *            Indice del parametro que contiene la mejor soluci�n encontrada.
	 * @param solParcial
	 *            Indice del parametro que contiene la soluci�n parcial.
	 * @param RyP
	 *            true = Recorta y Poda, false = Vuelta Atras.
	 * @param cota
	 *            Indice del parametro que contiene la cota.
	 * @param maximizacion
	 *            true = maximizacion, false = minimizacion.
	 */
	private static void tratamientoMetodosVoidAABB(Document d, Element metodo, 
			int mejorSol, int solParcial, boolean RyP, int cota, 
			boolean maximizacion) {
		Object valores[] = recoleccionInformacion(metodo);
		// String tipoMetodo=(String)valores[0];// valores[0]=String tipoMetodo
		Element returns[] = (Element[]) valores[1];
		Element argumentos[] = (Element[]) valores[2];
		Element bloqueMetodo = (Element) valores[3];

		generacionLineasEntrada(d, bloqueMetodo, argumentos, RyP, maximizacion, solParcial, mejorSol, cota);

		// Ahora nos ocupamos de las lineas de salida, bas�ndonos en si hay o no
		// nodos return
		
		if (depurar) {
			System.out.println("MetodoVoidconReturn  01");
		}
		// 01. "Object rrrrrr01[]=new Object[numParam];"
		Element sentenciaDeclObjectrrrrrr01 = elementoDeclaracion(d, "Object", 
				"rrrrrr01", argumentos.length);

		if (depurar) {
			System.out.println("MetodoVoidconReturn  02");
		}
		// 02. "rrrrrr01[n]=parametroN;" (varias)
		Element sentenciaAsigParam[] = new Element[argumentos.length];
		for (int i = 0; i < argumentos.length; i++) {
			sentenciaAsigParam[i] = elementosAsigParametros(d, i, 
					argumentos[i].getAttribute("name"), "rrrrrr01");
		}

		if (depurar) {
			System.out.println("MetodoVoidconReturn  03");
		}
		// 03. Linea de Traza para la salida
		Element sentenciaAnadirSalida = Transformador.elementoAnadirSalida(d, 
				"anadirSalida", "rrrrrr01", false);
		
		if (returns.length == 0) {
			// A�adimos al bloque del m�todo las lineas de salida generadas
			bloqueMetodo.appendChild(sentenciaDeclObjectrrrrrr01);
			for (int i = 0; i < argumentos.length; i++) {
				bloqueMetodo.appendChild(sentenciaAsigParam[i]);
			}
			bloqueMetodo.appendChild(sentenciaAnadirSalida);

			// Revisamos que no haya sentencia de throw (debe ser la �ltima)
			Element sentenciasBloqueMetodo[] = ManipulacionElement
					.getChildElements(bloqueMetodo);
			for (int i = 0; i < sentenciasBloqueMetodo.length; i++) {
				if (sentenciasBloqueMetodo[i].getNodeName().equals("throw")) {
					bloqueMetodo.removeChild(sentenciasBloqueMetodo[i]);
					bloqueMetodo.appendChild(sentenciasBloqueMetodo[i]);
				}
			}
		} else {
			// Habr� que insertar tantos conjuntos de lineas de salida para cada
			// return que haya as� como para el final del m�todo
			int hayReturnFinal = -1; // -1 = No hay return al final del m�todo
			for (int i = 0; i < returns.length; i++) {
				if (depurar) {
					System.out.println("MetodoVoidconReturn  04");
				}
				Element padreReturn = (Element) returns[i].getParentNode();
				recolocarReturn(d, returns[i], padreReturn);
				if (returns[i].getParentNode() == bloqueMetodo) {
					hayReturnFinal = i;
				}

				padreReturn = (Element) returns[i].getParentNode();
				if (depurar) {
					System.out.println("MetodoVoidconReturn  05 padreReturn="
							+ padreReturn.getNodeName());
				}
				padreReturn.appendChild(sentenciaDeclObjectrrrrrr01
						.cloneNode(true));
				for (int j = 0; j < argumentos.length; j++) {
					padreReturn.appendChild(sentenciaAsigParam[j]
							.cloneNode(true));
				}
				padreReturn.appendChild(sentenciaAnadirSalida.cloneNode(true));

				if (depurar) {
					System.out.println("MetodoVoidconReturn  06");
				}
				padreReturn.removeChild(returns[i]);
				padreReturn.appendChild(returns[i]);
				if (depurar) {
					System.out.println("MetodoVoidconReturn  07");
				}
			}

			if (hayReturnFinal == -1) {
				if (depurar) {
					System.out.println("MetodoVoidconReturn  08");
				}
				// A�adimos al bloque del m�todo las lineas de salida generadas
				bloqueMetodo.appendChild(sentenciaDeclObjectrrrrrr01);
				for (int i = 0; i < argumentos.length; i++) {
					bloqueMetodo.appendChild(sentenciaAsigParam[i]);
				}
				bloqueMetodo.appendChild(sentenciaAnadirSalida);
				if (depurar) {
					System.out.println("MetodoVoidconReturn  09");
				}
			}
		}
	}

	/**
	 * M�todo auxiliar que gestiona la inserci�n de l�neas en el c�digo original
	 * Java
	 * 
	 * @param d
	 *            Document al que se a�adir�n nodos
	 * @param metodo
	 *            M�todo al que se a�aden nodos adicionales para las l�enas de
	 *            traza
	 * @param mejorSol
	 *            Indice del parametro que contiene la mejor soluci�n encontrada.
	 * @param solParcial
	 *            Indice del parametro que contiene la soluci�n parcial.
	 * @param RyP
	 *            true = Recorta y Poda, false = Vuelta Atras.
	 * @param cota
	 *            Indice del parametro que contiene la cota.
	 * @param maximizacion
	 *            true = maximizacion, false = minimizacion.
	 */
	private static void tratamientoMetodosConValorAABB(Document d, 
			Element metodo, int mejorSol, int solParcial, boolean RyP, int cota, 
			boolean maximizacion) {
		// Recopilamos nodos interesantes y el tipo del M�todo
		Object valores[] = recoleccionInformacion(metodo);

		String tipoMetodo = (String) valores[0];// valores[0]=String tipoMetodo
		Element returns[] = (Element[]) valores[1];
		Element argumentos[] = (Element[]) valores[2];
		Element bloqueMetodo = (Element) valores[3];

		// Insertamos todas las lineas de entrada, y nos guardamos la primera
		generacionLineasEntrada(d, bloqueMetodo, argumentos, RyP, maximizacion, solParcial, mejorSol, cota);

		// Ahora nos ocupamos de las lineas de salida, bas�ndonos en los nodos
		// return existentes
		for (int j = 0; j < returns.length; j++) {
			Element padreReturn = (Element) returns[j].getParentNode();

			recolocarReturn(d, returns[j], padreReturn);

			// Creamos todas las lineas de salida que necesitamos

			// 01. "int zzzzzz01 = resultado;"
			Element sentenciaVariableRet = d.createElement("local-variable");
			sentenciaVariableRet.setAttribute("name", "zzzzzz01");

			Element tipoRetorno = d.createElement("type"); // Nodo que expresar�
			// el tipo de la variable

			Element nodoTipoRetornoMetodo = nodoTipoRetornoMetodo(metodo);
			String stringTipoVariable = tipoMetodo;
			if (nodoTipoRetornoMetodo.getAttribute("dimensions").length() > 0) 
			// si aparece atributo "dimensions"
			{
				int dimensiones = Integer.parseInt(nodoTipoRetornoMetodo
						.getAttribute("dimensions"));
				// for (int k=0; k<dimensiones; k++)
				// stringTipoVariable=stringTipoVariable+"[]"; // a�adimos "[]"
				// por cada dimension
				stringTipoVariable = stringTipoVariable + ServiciosString
						.cadenaDimensiones(dimensiones);
			}

			tipoRetorno.setAttribute("name", stringTipoVariable);
			sentenciaVariableRet.appendChild(tipoRetorno);
			Element hijosReturn[] = ManipulacionElement
					.getChildElements(returns[j]);
			sentenciaVariableRet.appendChild(hijosReturn[0].cloneNode(true));

			// 02. "Object rrrrrr01[]=new Object[1];"
			Element sentenciaDeclObjectrrrrrr01 = elementoDeclaracion(d, 
					"Object", "rrrrrr01", 1);

			// 03. "rrrrrr01[0]=zzzzzz01;"
			Element asigParametros = elementosAsigParametros(d, 0, "zzzzzz01",
					"rrrrrr01");

			// 04. Linea de Traza para la salida
			Element sentenciaAnadirSalida = Transformador.elementoAnadirSalida(
					d, "anadirSalida", "rrrrrr01", true);

			// 05. Cambiamos sentencia return para que devuelva siempre la
			// variable zzzzzz01
			Element var_ref = d.createElement("var-ref");
			var_ref.setAttribute("name", "zzzzzz01");

			while (returns[j].getFirstChild() != null) {
				returns[j].removeChild(returns[j].getFirstChild());
			}

			returns[j].appendChild(var_ref);

			// Ahora insertamos las lineas de salida

			padreReturn = (Element) returns[j].getParentNode();
			Element primeraSentenciaPadreReturn = ManipulacionElement
					.getChildElements(padreReturn)[0];
			if (depurar) {
				System.out.println("  Return Paso01");
			}

			//
			Element bmh[] = ManipulacionElement.getChildElements(padreReturn);
			if (depurar) {
				for (int i = 0; i < bmh.length; i++) {
					System.out.println(" -" + bmh[i].getNodeName());
				}
			}

			while (padreReturn.getFirstChild() != returns[j]) {
				Node nodo_aux = padreReturn.getFirstChild();
				padreReturn.removeChild(nodo_aux);
				padreReturn.appendChild(nodo_aux);
			}

			padreReturn.appendChild(sentenciaVariableRet);
			padreReturn.appendChild(sentenciaDeclObjectrrrrrr01);
			padreReturn.appendChild(asigParametros);
			padreReturn.appendChild(sentenciaAnadirSalida);

			// Recolocamos las lineas para que las l�neas reci�n a�adidas est�n
			// al principio del m�todo
			while (padreReturn.getFirstChild() != primeraSentenciaPadreReturn) {
				Node nodo_aux = padreReturn.getFirstChild();
				padreReturn.removeChild(nodo_aux);
				padreReturn.appendChild(nodo_aux);
				if (depurar) {
					System.out.println("buscando primeraSentenciaPadreReturn");
				}
			}
			if (padreReturn.getFirstChild().getNodeName().equals("return")) {
				Node nodo_aux = padreReturn.getFirstChild();
				padreReturn.removeChild(nodo_aux);
				padreReturn.appendChild(nodo_aux);
				if (depurar) {
					System.out.println("recolocando sentencia return");
				}
			}
		}
	}

	private static Element elementoDeclaracionAsignacion(Document d,
			int numParamestructura, String nombreVar, Element formalArguments) {
		Element argumentos[] = ManipulacionElement
				.nodeListToElementArray(formalArguments
						.getElementsByTagName("formal-argument"));

		// Creamos elemento
		Element elemento = d.createElement("local-variable");
		elemento.setAttribute("name", nombreVar);

		// Creamos nodo sobre el tipo
		Element tipoElemento = d.createElement("type");
		elemento.appendChild(tipoElemento);
		// tipoElemento.setAttribute("dimensions","1");
		tipoElemento.setAttribute("name", "Object");

		// Creamos inicializaci�n directa ("={a,b,c,d...};"
		Element inicializador = d.createElement("var-ref");
		elemento.appendChild(inicializador);
		inicializador.setAttribute("name",
				"" + argumentos[numParamestructura].getAttribute("name"));

		return elemento;
	}
	
	private static Element elementoDeclaracionAsignacionNumerico(Document d,
			int numParame, String nombreVar, Element formalArguments) {
		Element argumentos[] = ManipulacionElement
				.nodeListToElementArray(formalArguments
						.getElementsByTagName("formal-argument"));

		// Creamos elemento
		Element elemento = d.createElement("local-variable");
		elemento.setAttribute("name", nombreVar);

		// Creamos nodo sobre el tipo
		Element tipoElemento = d.createElement("type");
		elemento.appendChild(tipoElemento);
		// tipoElemento.setAttribute("dimensions","1");
		tipoElemento.setAttribute("name", "Number");

		// Creamos inicializaci�n directa ("={a,b,c,d...};"
		Element inicializador = d.createElement("var-ref");
		elemento.appendChild(inicializador);
		inicializador.setAttribute("name",
				"" + argumentos[numParame].getAttribute("name"));

		return elemento;
	}
	
	/**
	 * Si el nodo return no est� en un bloque, hace que lo est�
	 * 
	 * @param d
	 *            Document al que pertenecen todos los nodos
	 * @param r
	 *            Element de la sentencia return
	 * @param pr
	 *            Element del padre de la sentencia return
	 */
	private static void recolocarReturn(Document d, Element r, Element pr) {
		if (!(pr.getNodeName().equals("block"))) {
			Element nuevoBloque = d.createElement("block");
			pr.appendChild(nuevoBloque);
			pr.removeChild(r);
			nuevoBloque.appendChild(r);
		}
	}

	/**
	 * Recopila informaci�n sobre un m�todo
	 * 
	 * @param metodo
	 *            M�todo sobre el cual se recopilar� informaci�n
	 * @return informaci�n sobre el m�todo
	 */
	private static Object[] recoleccionInformacion(Element metodo) {
		Object valores[] = new Object[4];
		// valores[0]=String tipoMetodo
		// valores[1]=Element returns[]
		// valores[2]=Element argumentos[]
		// valores[3]=Element bloqueMetodo

		valores[0] = tipoRetornoMetodo(metodo);

		// Nos hacemos con nodos estrat�gicos: returns, argumentos, bloques, ...

		// -- Nodos return del m�todo
		valores[1] = ManipulacionElement.nodeListToElementArray(metodo
				.getElementsByTagName("return"));

		// -- Nodos de argumentos que recibe el m�todo
		valores[2] = ManipulacionElement.nodeListToElementArray(metodo
				.getElementsByTagName("formal-argument"));
		valores[2] = limpiezaArgumentos((Element[]) valores[2]); // Ahora s�lo
		// tenemos
		// nodos de
		// argumentos
		// v�lidos

		// -- Nodo bloque del m�todo
		valores[3] = null;
		Element hijosMetodo[] = ManipulacionElement
				.nodeListToElementArray(metodo.getChildNodes());
		for (int k = 0; k < hijosMetodo.length; k++) {
			if (hijosMetodo[k].getNodeName().equals("block")) {
				valores[3] = hijosMetodo[k];
			}
		}

		if (depurar) {
			System.out.println("METODO " + metodo.getAttribute("name") + " ["
					+ ((String) valores[0]) + "]");
		}
		if (depurar) {
			System.out.println("Numero de returns: "
					+ ((Element[]) (valores[1])).length
					+ "\nNumero de argumentos: "
					+ ((Element[]) (valores[2])).length);
		}
		if (depurar) {
			for (int z = 0; z < ((Element[]) valores[2]).length; z++) {
				System.out.println("Argumento[" + (z + 1) + "]="
						+ ((Element[]) (valores[2]))[z].getAttribute("name"));
			}
		}

		return valores;
	}

	/**
	 * Genera las l�neas de traza que se sit�an al inicio de un m�todo
	 * 
	 * @param d
	 *            Documento al que pertenecen todos los nodos
	 * @param bloqueMetodo
	 *            nodo que representea el bloque de instrucciones del m�todo
	 * @param argumentos
	 *            valores que se asignar�n en las l�neas de traza
	 * @return primera sentencia que colocamos
	 */
	private static Element generacionLineasEntrada(Document d,
			Element bloqueMetodo, Element argumentos[]) {
		// Creamos todas las lineas de entrada que necesitamos

		// 01. "Object pppppp01[]=new Object[numParametros];"
		Element sentenciaDeclObjectpppppp01 = elementoDeclaracion(d, "Object",
				"pppppp01", argumentos.length);

		// 01b.
		// "String nnnnnn01[]={ nombreParam1, nombreParam2, nombreParam3, ... };"
		Element sentenciaArrayNmobreParam = elementoNombresParam(d,
				(Element) (((Element) (bloqueMetodo.getParentNode()))
						.getElementsByTagName("formal-arguments").item(0)));

		// 02. "pppppp01[n]=parametroN;" (varias)
		Element sentenciaAsigParam[] = new Element[argumentos.length];
		for (int i = 0; i < argumentos.length; i++) {
			sentenciaAsigParam[i] = elementosAsigParametros(d, i,
					argumentos[i].getAttribute("name"), "pppppp01");
		}

		// 03. Linea de Traza para la entrada
		Element sentenciaAnadirEntrada = Transformador
				.elementoAnadirEntrada(d, "anadirEntrada", "pppppp01",
						((Element) (bloqueMetodo.getParentNode()))
								.getAttribute("name"));

		// Ahora insertamos las lineas de entrada
		// Element hijosBloqueMetodo[]=
		// ManipulacionElement.nodeListToElementArray(
		// bloqueMetodo.getChildNodes() );

		bloqueMetodo.appendChild(sentenciaDeclObjectpppppp01);
		bloqueMetodo.appendChild(sentenciaArrayNmobreParam);
		for (int i = 0; i < argumentos.length; i++) {
			bloqueMetodo.appendChild(sentenciaAsigParam[i]);
		}
		bloqueMetodo.appendChild(sentenciaAnadirEntrada);

		// Recolocamos las lineas para que las l�neas reci�n a�adidas est�n al
		// principio del m�todo
		while (bloqueMetodo.getFirstChild() != sentenciaDeclObjectpppppp01) {
			Node nodo_aux = bloqueMetodo.getFirstChild();
			bloqueMetodo.removeChild(nodo_aux);
			bloqueMetodo.appendChild(nodo_aux);
		}

		// Devolvemos primera sentencia que colocamos para que despu�s sepamos
		// cu�l debe ir en primer lugar
		return sentenciaDeclObjectpppppp01;
	}

	/**
	 * Genera las l�neas de traza que se sit�an al inicio de un m�todo
	 * 
	 * @param d
	 *            Documento al que pertenecen todos los nodos
	 * @param bloqueMetodo
	 *            nodo que representea el bloque de instrucciones del m�todo
	 * @param argumentos
	 *            valores que se asignar�n en las l�neas de traza
	 * @param paramEstructura
	 *            numero de parametro que alberga la estructura sobre la que
	 *            actua el algoritmo
	 * @param paramIndices
	 *            numeros de par�metros que delimitan la actuaci�n del algoritmo
	 *            en la estructura
	 * @return primera sentencia que colocamos
	 */
	private static Element generacionLineasEntrada(Document d,
			Element bloqueMetodo, Element argumentos[], int paramEstructura,
			int paramIndices[]) {
		// Creamos todas las lineas de entrada que necesitamos

		// 01. "Object pppppp01[]=new Object[numParametros];"
		Element sentenciaDeclObjectpppppp01 = elementoDeclaracion(d, "Object",
				"pppppp01", argumentos.length);

		// 01b.
		// "String nnnnnn01[]={ nombreParam1, nombreParam2, nombreParam3, ... };"
		Element sentenciaArrayNmobreParam = elementoNombresParam(d,
				(Element) (((Element) (bloqueMetodo.getParentNode()))
						.getElementsByTagName("formal-arguments").item(0)));

		// 01c. "Object eeeeee01=nombreParam;"
		Element sentenciaEstructura = elementoDeclaracionAsignacion(d,
				paramEstructura, "eeeeee01",
				(Element) (((Element) (bloqueMetodo.getParentNode()))
						.getElementsByTagName("formal-arguments").item(0)));

		// 01d. "int iiiiii01={ colInf, colSup, filInf, filSup };"
		Element sentenciaIndices = elementoIndices(d, paramIndices,
				(Element) (((Element) (bloqueMetodo.getParentNode()))
						.getElementsByTagName("formal-arguments").item(0)));

		// 02. "pppppp01[n]=parametroN;" (varias)
		Element sentenciaAsigParam[] = new Element[argumentos.length];
		for (int i = 0; i < argumentos.length; i++) {
			sentenciaAsigParam[i] = elementosAsigParametros(d, i,
					argumentos[i].getAttribute("name"), "pppppp01");
		}

		// 03. Linea de Traza para la entrada
		Element sentenciaAnadirEntrada = Transformador
				.elementoAnadirEntradaSalida(d, "anadirEntrada", "pppppp01",
						"eeeeee01", paramEstructura, ((Element) (bloqueMetodo
								.getParentNode())).getAttribute("name"));

		// Ahora insertamos las lineas de entrada
		// Element hijosBloqueMetodo[]=
		// ManipulacionElement.nodeListToElementArray(
		// bloqueMetodo.getChildNodes() );

		bloqueMetodo.appendChild(sentenciaDeclObjectpppppp01);
		bloqueMetodo.appendChild(sentenciaArrayNmobreParam);
		bloqueMetodo.appendChild(sentenciaEstructura);
		bloqueMetodo.appendChild(sentenciaIndices);

		for (int i = 0; i < argumentos.length; i++) {
			bloqueMetodo.appendChild(sentenciaAsigParam[i]);
		}
		bloqueMetodo.appendChild(sentenciaAnadirEntrada);

		// Recolocamos las lineas para que las l�neas reci�n a�adidas est�n al
		// principio del m�todo
		while (bloqueMetodo.getFirstChild() != sentenciaDeclObjectpppppp01) {
			Node nodo_aux = bloqueMetodo.getFirstChild();
			bloqueMetodo.removeChild(nodo_aux);
			bloqueMetodo.appendChild(nodo_aux);
		}

		// Devolvemos primera sentencia que colocamos para que despu�s sepamos
		// cu�l debe ir en primer lugar
		return sentenciaDeclObjectpppppp01;
	}
	
	/**
	 * Genera las l�neas de traza que se sit�an al inicio de un m�todo
	 * 
	 * @param d
	 *            Documento al que pertenecen todos los nodos
	 * @param bloqueMetodo
	 *            nodo que representea el bloque de instrucciones del m�todo
	 * @param argumentos
	 *            valores que se asignar�n en las l�neas de traza
	 * @param paramEstructura
	 *            numero de parametro que alberga la estructura sobre la que
	 *            actua el algoritmo
	 * @param paramIndices
	 *            numeros de par�metros que delimitan la actuaci�n del algoritmo
	 *            en la estructura
	 * @return primera sentencia que colocamos
	 */
	private static Element generacionLineasEntrada(Document d,
			Element bloqueMetodo, Element argumentos[], boolean RyP, boolean maximizacion, 
			int solParcial, int mejorSolucion, int cota) {
		// Creamos todas las lineas de entrada que necesitamos

		// 01. "Object pppppp01[]=new Object[numParametros];"
		Element sentenciaDeclObjectpppppp01 = elementoDeclaracion(d, "Object",
				"pppppp01", argumentos.length);

		// 01b.
		// "String nnnnnn01[]={ nombreParam1, nombreParam2, nombreParam3, ... };"
		Element sentenciaArrayNombreParam = elementoNombresParam(d,
				(Element) (((Element) (bloqueMetodo.getParentNode()))
						.getElementsByTagName("formal-arguments").item(0)));

		// 01c. "Object ssssss01=nombreParam;"
		Element sentenciaSolParcial = elementoDeclaracionAsignacionNumerico(d,
				solParcial, "ssssss01",
				(Element) (((Element) (bloqueMetodo.getParentNode()))
						.getElementsByTagName("formal-arguments").item(0)));

		// 01d. "Object mmmmmm01=nombreParam;"
		Element sentenciaMejorSolucion = elementoDeclaracionAsignacionNumerico(d,
				mejorSolucion, "mmmmmm01",
				(Element) (((Element) (bloqueMetodo.getParentNode()))
						.getElementsByTagName("formal-arguments").item(0)));
		
		// 01e. "int cccccc01=nombreParam;"
		Element sentenciaCota = null;
		if(RyP) {
			sentenciaCota = elementoDeclaracionAsignacionNumerico(d, 
					cota, "cccccc01",
					(Element) (((Element) (bloqueMetodo.getParentNode()))
							.getElementsByTagName("formal-arguments").item(0)));
		}


		// 02. "pppppp01[n]=parametroN;" (varias)
		Element sentenciaAsigParam[] = new Element[argumentos.length];
		for (int i = 0; i < argumentos.length; i++) {
			sentenciaAsigParam[i] = elementosAsigParametros(d, i,
					argumentos[i].getAttribute("name"), "pppppp01");
		}

		// 03. Linea de Traza para la entrada
		Element sentenciaAnadirEntrada = Transformador
				.elementoAnadirEntradaSalida(d, "anadirEntrada", "pppppp01",
						RyP, maximizacion, "ssssss01", "mmmmmm01", "cccccc01", 
						((Element) (bloqueMetodo.getParentNode()))
							.getAttribute("name"));

		// Ahora insertamos las lineas de entrada
		// Element hijosBloqueMetodo[]=
		// ManipulacionElement.nodeListToElementArray(
		// bloqueMetodo.getChildNodes() );

		bloqueMetodo.appendChild(sentenciaDeclObjectpppppp01);
		bloqueMetodo.appendChild(sentenciaArrayNombreParam);
		bloqueMetodo.appendChild(sentenciaSolParcial);
		bloqueMetodo.appendChild(sentenciaMejorSolucion);
		if(RyP) {
			bloqueMetodo.appendChild(sentenciaCota);
		}

		for (int i = 0; i < argumentos.length; i++) {
			bloqueMetodo.appendChild(sentenciaAsigParam[i]);
		}
		bloqueMetodo.appendChild(sentenciaAnadirEntrada);

		// Recolocamos las lineas para que las l�neas reci�n a�adidas est�n al
		// principio del m�todo
		while (bloqueMetodo.getFirstChild() != sentenciaDeclObjectpppppp01) {
			Node nodo_aux = bloqueMetodo.getFirstChild();
			bloqueMetodo.removeChild(nodo_aux);
			bloqueMetodo.appendChild(nodo_aux);
		}

		// Devolvemos primera sentencia que colocamos para que despu�s sepamos
		// cu�l debe ir en primer lugar
		return sentenciaDeclObjectpppppp01;
	}

	private static Element elementoNombresParam(Document d,
			Element formalArguments) {
		Element argumentos[] = ManipulacionElement
				.nodeListToElementArray(formalArguments
						.getElementsByTagName("formal-argument"));

		// Creamos elemento
		Element elemento = d.createElement("local-variable");
		elemento.setAttribute("name", "nnnnnn01");

		// Creamos nodo sobre el tipo
		Element tipoElemento = d.createElement("type");
		elemento.appendChild(tipoElemento);
		tipoElemento.setAttribute("dimensions", "1");
		tipoElemento.setAttribute("name", "String");

		// Creamos inicializaci�n directa ("={a,b,c,d...};"
		Element inicializador = d.createElement("array-initializer");
		elemento.appendChild(inicializador);
		inicializador.setAttribute("length", "" + argumentos.length);

		// A�adimos a la inicializaci�n los nombres de los distintos par�metros
		// del m�todo
		if(argumentos.length==0) {
			Element cadena = d.createElement("literal-string");
			inicializador.appendChild(cadena);
			cadena.setAttribute("value", "");
		}
		for (int i = 0; i < argumentos.length; i++) {
			Element cadena = d.createElement("literal-string");
			inicializador.appendChild(cadena);
			cadena.setAttribute("value", argumentos[i].getAttribute("name"));
		}

		return elemento;
	}

	private static Element elementoIndices(Document d, int numIndices[],
			Element formalArguments) {
		Element argumentos[] = ManipulacionElement
				.nodeListToElementArray(formalArguments
						.getElementsByTagName("formal-argument"));

		// Creamos elemento
		Element elemento = d.createElement("local-variable");
		elemento.setAttribute("name", "iiiiii01");

		// Creamos nodo sobre el tipo
		Element tipoElemento = d.createElement("type");
		elemento.appendChild(tipoElemento);
		tipoElemento.setAttribute("dimensions", "1");
		tipoElemento.setAttribute("name", "int");

		if (numIndices != null && numIndices.length > 0) {
			for (int i = 0; i < numIndices.length; i++) {
				numIndices[i]--;
			}

			// Creamos inicializaci�n directa ("={a,b,c,d...};"
			Element inicializador = d.createElement("array-initializer");
			elemento.appendChild(inicializador);
			inicializador.setAttribute("length", "" + numIndices.length);

			// A�adimos a la inicializaci�n los nombres de los distintos
			// par�metros del m�todo
			for (int i = 0; i < numIndices.length; i++) {
				Element cadena = d.createElement("var-ref");
				inicializador.appendChild(cadena);
				cadena.setAttribute("name",
						argumentos[numIndices[i]].getAttribute("name"));
			}
		} else {
			Element inicializador = d.createElement("literal-null");
			elemento.appendChild(inicializador);
		}

		return elemento;
	}

	/**
	 * Corrige nombres, como el de la clase, los constructores, ... para
	 * asegurarse de que la clase nueva compilar�
	 * 
	 * @param d
	 *            Documento al que pertenecen todos los nodos
	 * @param fichero
	 *            nombre del fichero (el mismo que el de la clase)
	 * @param inicioNombre
	 *            Inicio del nombre que se usar� para guardar el nuevo fichero.
	 * @param codigounico
	 *            C�digo �nico que se sumar� al nombre del nuevo fichero.
	 */
	public static void correccionNombres(Document d, String fichero,
			String inicioNombre, String codigounico) {
		// Corregimos nodo de fichero "java-class-file" con el nombre adaptado
		NodeList nodosFicheros = d.getElementsByTagName("java-class-file");

		String fich2 = fichero.replace(".java", "");
		fich2 = fich2 + codigounico + ".java";

		String directorio = ((Element) nodosFicheros.item(0)).getAttribute(
				"name").replace(fichero, "");

		if (nodosFicheros.getLength() > 0) {
			((Element) nodosFicheros.item(0)).setAttribute("name", directorio
					+ inicioNombre + fich2);
		}

		// Eliminamos nodos de comentarios
		Transformador.borrarComentarios(d, Node.COMMENT_NODE);

		// Hacemos que todos los m�todos sean p�blicos
		NodeList metodosClase = d.getElementsByTagName("method");
		Element eMetodosClase[] = ManipulacionElement
				.nodeListToElementArray(metodosClase);

		for (int i = 0; i < eMetodosClase.length; i++) {
			eMetodosClase[i].setAttribute("visibility", "public");
		}

		// Cambiamos nombre de clase de "Xxx" a "SRec_Xxx"
		NodeList nodosClase = d.getElementsByTagName("class");
		NodeList nodosInterfaz = d.getElementsByTagName("interface");

		if (nodosClase != null && (nodosClase.item(0) instanceof Element)) {
			((Element) nodosClase.item(0)).setAttribute("name", inicioNombre
					+ ((Element) nodosClase.item(0)).getAttribute("name")
					+ codigounico);
		} else if (nodosInterfaz != null
				&& (nodosInterfaz.item(0) instanceof Element)) {
			((Element) nodosInterfaz.item(0)).setAttribute("name", inicioNombre
					+ ((Element) nodosInterfaz.item(0)).getAttribute("name")
					+ codigounico);
		}

		// Corregimos nodos de fichero "constructor" con el nombre adaptado
		nodosFicheros = d.getElementsByTagName("constructor");

		for (int i = 0; i < nodosFicheros.getLength(); i++) {
			if (nodosFicheros.item(i) instanceof Element) {
				((Element) nodosFicheros.item(i)).setAttribute(
						"name",
						inicioNombre
								+ ((Element) nodosFicheros.item(i))
										.getAttribute("name") + codigounico);
			}
		}

	}

	/**
	 * Elimina todos los comentarios del fichero XML (los comentarios java del
	 * fichero original ni siquiera han sido leidos)
	 * 
	 * @param nodo
	 *            Node que representa la cima del �rbol al cual se le va a
	 *            eliminar recursivamente los comentarios
	 * @param tipo
	 *            tipo de nodo que representa los comentarios
	 */
	private static void borrarComentarios(Node nodo, short tipo) {
		if (nodo.getNodeType() == tipo) {
			nodo.getParentNode().removeChild(nodo);
		} else // recorremos hijos
		{
			NodeList nl = nodo.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Transformador.borrarComentarios(nl.item(i), tipo);
			}
		}
	}

	/**
	 * Devuelve en formato String el tipo que devuelve el m�todo cuyo elemento
	 * se pasa como par�metro
	 * 
	 * @param e
	 *            Element del m�todo que se va a estudiar
	 * @return representaci�n String del tipo del m�todo
	 */
	private static String tipoRetornoMetodo(Element e) {
		Element el[] = ManipulacionElement.getChildElements(e);

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("type")) {
				return el[i].getAttribute("name");
			}
		}
		return "";
	}

	/**
	 * Devuelve el nodo del tipo que devuelve el m�todo cuyo elemento se pasa
	 * como par�metro
	 * 
	 * @param e
	 *            Element del m�todo que se va a estudiar
	 * @return nodo del tipo que devuelve el m�todo cuyo elemento se pasa como
	 *         par�metro
	 */
	private static Element nodoTipoRetornoMetodo(Element e) {
		Element el[] = ManipulacionElement.getChildElements(e);

		for (int i = 0; i < el.length; i++) {
			if (el[i].getNodeName().equals("type")) {
				return el[i];
			}
		}
		return null;
	}

	/**
	 * Dada una lista de nodos hijos "formal-argument" de un m�todo, quitamos
	 * los que pertenecen a sentencias catch (es decir, lo que no son argumentos
	 * reales del m�todo)
	 * 
	 * @param el
	 *            lista de argumentos que ser� filtrada para devover s�lo
	 *            aquellos verdaderamente v�lidos
	 * @return lista real de arugumentos de un m�todo
	 */
	private static Element[] limpiezaArgumentos(Element el[]) {
		int x = 0;
		for (int i = 0; i < el.length; i++) {
			if (el[i].getParentNode().getNodeName().equals("catch")) {
				x++;
			}
		}

		Element el_nuevo[] = new Element[el.length - x];

		x = 0;
		for (int i = 0; i < el.length; i++) {
			if (!(el[i].getParentNode().getNodeName().equals("catch"))) {
				el_nuevo[x] = el[i];
				x++;
			}
		}

		return el_nuevo;
	}

	// //////////////////////////////////////////////////////////
	
	/**
	 * Genera una nueva instancia de ClaseAlgoritmo a partir de un Document.
	 * 
	 * @param d Document
	 * 
	 * @return Nueva instancia de ClaseAlgoritmo.
	 */
	protected static ClaseAlgoritmo crearObjetoClaseAlgoritmo(Document d) {
		ClaseAlgoritmo c = null;
		MetodoAlgoritmo m = null;
		Element e[] = null;
		Element eClase[] = null;

		// Sacamos el nombre de la clase y su path y la creamos
		eClase = ManipulacionElement.nodeListToElementArray(d
				.getElementsByTagName("class"));

		if (eClase.length == 0) {
			eClase = ManipulacionElement.nodeListToElementArray(d
					.getElementsByTagName("java-class-file"));
			String pathCompleto = eClase[0].getAttribute("name");

			// String
			// path=pathCompleto.substring(0,pathCompleto.lastIndexOf(File.pathSeparator)+1);
			String nombreClase = (pathCompleto.substring(pathCompleto
					.lastIndexOf(File.pathSeparator) + 1));

			if (nombreClase.toLowerCase().contains(".java")) {
				int posic = nombreClase.toLowerCase().indexOf(".java");
				nombreClase = nombreClase.substring(0, posic);
			}
			c = new ClaseAlgoritmo(pathCompleto, nombreClase);

			return c;
		}

		String nombreAux = eClase[0].getAttribute("name");
		String nombreClase = nombreAux.substring(0,
				nombreAux.indexOf("__codSRec__"));

		e = ManipulacionElement.nodeListToElementArray(d
				.getElementsByTagName("java-class-file"));
		String pathClase = e[0].getAttribute("name").replace(".java", "");

		pathClase = pathClase.substring(0, pathClase.indexOf("__codSRec__"));

		
		String auxNombre = "";
		if(SsooValidator.isWindows()){
			auxNombre = pathClase.substring(pathClase.lastIndexOf("\\") + 1,
				pathClase.length());
		}else{
			auxNombre = pathClase.substring(pathClase.lastIndexOf("/") + 1,
				pathClase.length());
		}
		auxNombre = auxNombre.substring(0, auxNombre.length() / 2);

		if(SsooValidator.isWindows()){
			pathClase = pathClase.substring(0, pathClase.lastIndexOf("\\") + 1);
		}else{
			pathClase = pathClase.substring(0, pathClase.lastIndexOf("/") + 1);
		}

		c = new ClaseAlgoritmo(pathClase + auxNombre + ".java", nombreClase);

		// Sacamos los m�todos y los a�adimos a la clase
		e = ManipulacionElement.nodeListToElementArray(d
				.getElementsByTagName("method"));

		for (int i = 0; i < e.length; i++) {
			String nombreMetodo = null;
			String tipoMetodo = null;
			String[] nombreParametrosMetodo;
			String[] tipoParametrosMetodo;
			int[] dimParametrosMetodo;
			int dimRetornoMetodo;

			// Obtenemos el nombre y el tipo
			nombreMetodo = e[i].getAttribute("name");
			Element hijos[] = ManipulacionElement.getChildElements(e[i]);
			tipoMetodo = hijos[0].getAttribute("name");
			try {
				dimRetornoMetodo = Integer.parseInt(hijos[0]
						.getAttribute("dimensions"));
			} catch (java.lang.NumberFormatException nfe) {
				dimRetornoMetodo = 0;
			}

			// Obtenemos sus par�metros y dimensiones
			Element h[] = ManipulacionElement.getChildElements(hijos[1]);
			nombreParametrosMetodo = new String[h.length];
			tipoParametrosMetodo = new String[h.length];
			dimParametrosMetodo = new int[h.length];

			for (int j = 0; j < h.length; j++) {
				nombreParametrosMetodo[j] = h[j].getAttribute("name");
				tipoParametrosMetodo[j] = ManipulacionElement
						.getChildElements(h[j])[0].getAttribute("name");

				String dim = ManipulacionElement.getChildElements(h[j])[0]
						.getAttribute("dimensions");
				try {
					dimParametrosMetodo[j] = Integer.parseInt(dim);
				} catch (java.lang.NumberFormatException nfe) {
					dimParametrosMetodo[j] = 0;
				}
			}

			m = new MetodoAlgoritmo(nombreMetodo, tipoMetodo, dimRetornoMetodo,
					nombreParametrosMetodo, tipoParametrosMetodo,
					dimParametrosMetodo);
			if (esMetodoApropiado(m)) {
				m.setMarcadoProcesar(true);
				m.setTecnica(MetodoAlgoritmo.TECNICA_REC);
				c.addMetodo(m);
			}
		}

		int numFields = 0;
		Element[] hijosClase = ManipulacionElement
				.nodeListToElementArray(eClase[0].getChildNodes());
		for (int i = 0; i < hijosClase.length; i++) {
			if (hijosClase[i].getNodeName().equals("field")) {
				numFields++;
			}
		}

		Element[] fields = new Element[numFields];

		int x = 0;
		for (int i = 0; i < hijosClase.length; i++) {
			if (hijosClase[i].getNodeName().equals("field")) {
				fields[x] = hijosClase[i];
				x++;
			}
		}

		Variable[] variablesClase = Transformador.getVariablesClase(fields);

		ArrayList<MetodoAlgoritmo> metodosClase = c.getMetodos();

		// Ahora buscamos para cada m�todo...
		for (int i = 0; i < e.length; i++) {

			Node padre = e[i];

			while (!padre.getNodeName().equals("method")) {
				padre = (padre.getParentNode());
			}

			String[] datosMetodo = datosMetodo((Element) padre);
			int[] dimParamMetodo = dimMetodo((Element) padre);

			MetodoAlgoritmo metodoActual = MetodoAlgoritmo.getMetodo(
					metodosClase, datosMetodo, dimParamMetodo);

			// ...par�metros y variables locales
			Variable[] parametrosMetodo = Transformador
					.getParametrosMetodo(ManipulacionElement
							.nodeListToElementArray(e[i].getChildNodes())[1]);

			Element[] elementosMetodo = ManipulacionElement
					.nodeListToElementArray(e[i].getElementsByTagName("block"));
			Variable[] variablesMetodo = Transformador
					.getVariablesMetodo(ManipulacionElement
							.nodeListToElementArray(elementosMetodo[0]
									.getElementsByTagName("local-variable")));

			ArrayList<Variable> varA = new ArrayList<Variable>();

			for (Variable varI : variablesClase) {
				varA.add(varI);
			}
			for (Variable varI : parametrosMetodo) {
				varA.add(varI);
			}
			for (Variable varI : variablesMetodo) {
				varA.add(varI);
			}

			Variable[] var = new Variable[varA.size()];
			for (int j = 0; j < varA.size(); j++) {
				var[j] = varA.get(j);
			}

			// ...qu� llamadas a s� mismo o a otros m�todos contiene
			Element[] elementosSend = ManipulacionElement
					.nodeListToElementArray(e[i].getElementsByTagName("send"));
			MetodoAlgoritmo[] datosMetodos = new MetodoAlgoritmo[elementosSend.length];

			// for (Element elem : elementosSend)
			for (int j = 0; j < elementosSend.length; j++) {
				Element elem = elementosSend[j];
				// System.out.println("elemSEND: "+elem.getNodeName()+" > "+elem.getAttribute("message"));

				datosMetodos[j] = datosMetodoLlamada(elem, var, c);

				if (datosMetodos[j] != null && metodoActual != null) {
					metodoActual.setMetodoLlamado(datosMetodos[j].getID());
					// System.out.println("   Metodo ("+j+"): "+datosMetodos[j].getID());
				}
				// else
				// System.out.println("   Metodo ("+j+"): NULO");
			}
		}

		return c;

	}

	private static boolean esMetodoApropiado(MetodoAlgoritmo m) {

		for (int i = 0; i < m.getNumeroParametros(); i++) {
			// Comprobamos que sean de tipo primitivo
			if (!m.getTipoParametro(i).equals("byte")
					&& !m.getTipoParametro(i).equals("short")
					&& !m.getTipoParametro(i).equals("int")
					&& !m.getTipoParametro(i).equals("long")
					&& !m.getTipoParametro(i).equals("float")
					&& !m.getTipoParametro(i).equals("double")
					&& !m.getTipoParametro(i).equals("char")
					&& !m.getTipoParametro(i).equals("String")
					&& !m.getTipoParametro(i).equals("boolean")) {
				return false;
			}

			// Comprobamos que sean de dimensiones 0 (valor �nico), 1 (array) �
			// 2 (matriz)
			if (m.getDimParametro(i) < 0 || m.getDimParametro(i) > 2) {
				return false;
			}
		}

		// Comprobamos que el valor de retorno tiene dimensiones correctas
		if (m.getDimTipo() < 0 || m.getDimTipo() > 2) {
			return false;
		}

		// El m�todo ha pasado todas las comprobaciones
		return true;
	}

	private static Variable[] getVariablesClase(Element[] elementosField) {
		/*
		 * <field name="miVariable" visibility="protected" final="true"
		 * static="true"> <type primitive="true" name="int"/> <literal-number
		 * kind="int" value="5"/> </field>
		 */

		Variable[] variables = new Variable[elementosField.length];

		for (int i = 0; i < elementosField.length; i++) {
			String nombre = "", tipo = "";
			int dim = 0;

			nombre = elementosField[i].getAttribute("name");
			tipo = ManipulacionElement.nodeListToElementArray(elementosField[i]
					.getChildNodes())[0].getAttribute("name");
			String dimension = ManipulacionElement
					.nodeListToElementArray(elementosField[i].getChildNodes())[0]
					.getAttribute("dimensions");
			dim = (dimension.length() > 0 ? Integer.parseInt(dimension) : 0);

			variables[i] = new Variable(nombre, tipo, dim);
		}

		return variables;
	}

	private static Variable[] getParametrosMetodo(
			Element elementoFormalArguments) {
		/*
		 * <formal-arguments> <formal-argument name="n"> <type primitive="true"
		 * name="int"/> </formal-argument> </formal-arguments>
		 */

		Element[] argumentos = ManipulacionElement
				.nodeListToElementArray(elementoFormalArguments.getChildNodes());
		Variable[] variables = new Variable[argumentos.length];

		for (int i = 0; i < argumentos.length; i++) {
			String nombre = "", tipo = "";
			int dim = 0;

			nombre = argumentos[i].getAttribute("name");
			tipo = ManipulacionElement.nodeListToElementArray(argumentos[i]
					.getChildNodes())[0].getAttribute("name");
			String dimension = ManipulacionElement
					.nodeListToElementArray(argumentos[i].getChildNodes())[0]
					.getAttribute("dimensions");
			dim = (dimension.length() > 0 ? Integer.parseInt(dimension) : 0);
			variables[i] = new Variable(nombre, tipo, dim);
		}

		return variables;
	}

	private static Variable[] getVariablesMetodo(
			Element[] elementosLocalVariable) {
		/*
		 * <local-variable name="miVariable"> <type primitive="true"
		 * name="int"/> <literal-number kind="int" value="5"/> </local-variable>
		 */

		Variable[] variables = new Variable[elementosLocalVariable.length];

		for (int i = 0; i < elementosLocalVariable.length; i++) {
			String nombre = "", tipo = "";
			int dim = 0;

			nombre = elementosLocalVariable[i].getAttribute("name");
			tipo = ManipulacionElement
					.nodeListToElementArray(elementosLocalVariable[i]
							.getChildNodes())[0].getAttribute("name");
			String dimension = ManipulacionElement
					.nodeListToElementArray(elementosLocalVariable[i]
							.getChildNodes())[0].getAttribute("dimensions");
			dim = (dimension.length() > 0 ? Integer.parseInt(dimension) : 0);

			variables[i] = new Variable(nombre, tipo, dim);
		}

		return variables;
	}

	private static MetodoAlgoritmo datosMetodoLlamada(Element e, Variable[] v,
			ClaseAlgoritmo c) {
		/*
		 * <send message="fibonacci"> <arguments> <binary-expr op="-"> <var-ref
		 * name="n"/> <literal-number kind="int" value="1"/> </binary-expr>
		 * </arguments> </send>
		 */

		Element argumentos[] = ManipulacionElement
				.nodeListToElementArray(ManipulacionElement
						.nodeListToElementArray(e
								.getElementsByTagName("arguments"))[0]
						.getChildNodes());

		String[] datos = new String[2 + argumentos.length];
		int[] dimensionesParam = new int[argumentos.length];

		datos[0] = e.getAttribute("message");

		for (int i = 0; i < argumentos.length; i++) {
			datos[i + 1] = tipoDato(argumentos[i], v, c);
			dimensionesParam[i] = dimDato(argumentos[i], v, c);
		}

		// es necesario a�adir el tipo de dato que devuelve el m�todo al final
		ArrayList<MetodoAlgoritmo> metodos = c.getMetodos(datos[0]);

		if (metodos.size() > 1) {
			for (int i = 0; i < metodos.size(); i++) {
				MetodoAlgoritmo m = metodos.get(i);
				int numParamMetodo = m.getNumeroParametros();
				if (numParamMetodo == datos.length - 2) {
					boolean metodoCorrecto = true;
					for (int j = 0; j < numParamMetodo; j++) {
						if (!(m.getTipoParametro(j).equals(datos[j + 1]))) {
							metodoCorrecto = false;
						}
					}
					if (metodoCorrecto) {
						datos[datos.length - 1] = m.getTipo();
						i = metodos.size();
					}
				}
			}
		} else if (metodos.size() == 1) {
			datos[datos.length - 1] = metodos.get(0).getTipo();
		}

		return MetodoAlgoritmo.getMetodo(metodos, datos, dimensionesParam);
	}

	private static int dimDato(Element e, Variable[] v, ClaseAlgoritmo c) {
		String tipoNodo = e.getNodeName();

		if (tipoNodo.equals("binary-expr")) {
			return 0;
		} else if (tipoNodo.equals("unary-expr")) {
			return 0;
		} else if (tipoNodo.equals("cast-expr")) {
			return 0;
		} else if (tipoNodo.equals("var-ref")) {
			return Variable.getDimensiones(v, e.getAttribute("name"));
		} else if (tipoNodo.equals("literal-number")) {
			return 0;
		} else if (tipoNodo.equals("literal-string")) {
			return 0;
		} else if (tipoNodo.equals("literal-string")) {
			return 0;
		} else if (tipoNodo.equals("literal-boolean")) {
			return 0;
		} else if (tipoNodo.equals("send")) {
			MetodoAlgoritmo m = datosMetodoLlamada(e, v, c);
			int dimMetodo = (m != null ? m.getDimTipo() : -1);

			if (dimMetodo != -1) {
				return dimMetodo;
			} else {
				if (e.getAttribute("message").equals("indexOf")
						|| e.getAttribute("message").equals("lastIndexOf")
						|| e.getAttribute("message").equals("codePointAt")
						|| e.getAttribute("message").equals("size")
						|| e.getAttribute("message").equals("length")) {
					return 0;
				}
			}
		} else if (tipoNodo.equals("field-access")) {
			if (e.getAttribute("field").equals("length")) {
				return 0;
			}
		}

		return -1;
	}

	private static String tipoDato(Element e, Variable[] v, ClaseAlgoritmo c) {
		String tipoNodo = e.getNodeName();

		if (tipoNodo.equals("binary-expr")) {
			Element[] hijos = ManipulacionElement.nodeListToElementArray(e
					.getChildNodes());
			String op = e.getAttribute("op");
			String tipos[] = new String[2];

			tipos[0] = tipoDato(hijos[0], v, c);
			tipos[1] = tipoDato(hijos[1], v, c);

			return tipoAdecuado(tipos[0], tipos[1], op);
		} else if (tipoNodo.equals("unary-expr")) {
			Element hijo = ManipulacionElement.nodeListToElementArray(e
					.getChildNodes())[0];
			String nombreHijo = hijo.getNodeName();
			if (nombreHijo.equals("literal-boolean")) {
				return "boolean";
			} else if (nombreHijo.equals("literal-string")) {
				return "String";
			} else if (nombreHijo.equals("literal-char")) {
				return "char";
			} else if (nombreHijo.equals("literal-number")) {
				return hijo.getAttribute("kind");
			} else if (nombreHijo.equals("var-ref")) {
				return Variable.getTipo(v, hijo.getAttribute("name"));
			}
		} else if (tipoNodo.equals("cast-expr")) {
			Element hijo = ManipulacionElement.nodeListToElementArray(e
					.getChildNodes())[0];

			return hijo.getAttribute("name");
		} else if (tipoNodo.equals("var-ref")) {
			return Variable.getTipo(v, e.getAttribute("name"));
		} else if (tipoNodo.equals("literal-number")) {
			return e.getAttribute("kind");
		} else if (tipoNodo.equals("literal-string")) {
			return "String";
		} else if (tipoNodo.equals("literal-string")) {
			return "char";
		} else if (tipoNodo.equals("literal-boolean")) {
			return "boolean";
		} else if (tipoNodo.equals("send")) {
			MetodoAlgoritmo m = datosMetodoLlamada(e, v, c);

			if (m != null) {
				return m.getTipo();
			} else {
				if (e.getAttribute("message").equals("indexOf")
						|| e.getAttribute("message").equals("lastIndexOf")
						|| e.getAttribute("message").equals("codePointAt")
						|| e.getAttribute("message").equals("size")
						|| e.getAttribute("message").equals("length")) {
					return "int";
				}
			}
		} else if (tipoNodo.equals("field-access")) {
			if (e.getAttribute("field").equals("length")) {
				return "int";
			}
		}

		return "sin determinar";
	}

	// Implementa la pol�tica de tipos
	private static String tipoAdecuado(String t1, String t2, String op) {
		String tipoFinal = "-sin hacer-";

		/*
		 * float a=1; char b=2; char c=2;
		 * 
		 * c=a+b;
		 */

		if (t1.equals("byte")) {
			if (t2.equals("byte") || t2.equals("short") || t2.equals("int")
					|| t2.equals("long") || t2.equals("float")
					|| t2.equals("double")) {
				return t2;
			} else if (t2.equals("String")) {
				return "String";
			} else if (t2.equals("char")) {
				return "int";
			}
		} else if (t1.equals("short")) {
			if (t2.equals("byte")) {
				return t1;
			} else if (t2.equals("short") || t2.equals("int")
					|| t2.equals("long") || t2.equals("float")
					|| t2.equals("double")) {
				return t2;
			} else if (t2.equals("String")) {
				return "String";
			} else if (t2.equals("char")) {
				return "int";
			}
		} else if (t1.equals("int")) {
			if (t2.equals("byte") || t2.equals("short")) {
				return t1;
			} else if (t2.equals("int") || t2.equals("long")
					|| t2.equals("float") || t2.equals("double")) {
				return t2;
			} else if (t2.equals("String")) {
				return "String";
			} else if (t2.equals("char")) {
				return t1;
			}
		} else if (t1.equals("long")) {
			if (t2.equals("byte") || t2.equals("short") || t2.equals("int")) {
				return t1;
			} else if (t2.equals("long") || t2.equals("float")
					|| t2.equals("double")) {
				return t2;
			} else if (t2.equals("String")) {
				return "String";
			} else if (t2.equals("char")) {
				return t1;
			}
		} else if (t1.equals("float")) {
			if (t2.equals("byte") || t2.equals("short") || t2.equals("int")
					|| t2.equals("long")) {
				return t1;
			} else if (t2.equals("float") || t2.equals("double")) {
				return t2;
			} else if (t2.equals("String")) {
				return "String";
			} else if (t2.equals("char")) {
				return t1;
			}
		} else if (t1.equals("double")) {
			if (t2.equals("byte") || t2.equals("short") || t2.equals("int")
					|| t2.equals("long") || t2.equals("float")) {
				return t1;
			} else if (t2.equals("double")) {
				return t2;
			} else if (t2.equals("String")) {
				return "String";
			} else if (t2.equals("char")) {
				return t1;
			}
		} else if (t1.equals("String")) {
			if (t2.equals("byte") || t2.equals("short") || t2.equals("int")
					|| t2.equals("long") || t2.equals("float")
					|| t2.equals("double")) {
				return t1;
			} else if (t2.equals("String")) {
				return t1;
			} else if (t2.equals("char")) {
				return t1;
			} else if (t2.equals("boolean")) {
				return t1;
			}
		} else if (t1.equals("char")) {
			if (t2.equals("byte") || t2.equals("short")) {
				return "int";
			} else if (t2.equals("int") || t2.equals("long")
					|| t2.equals("float") || t2.equals("double")) {
				return t1;
			} else if (t2.equals("String")) {
				return t1;
			} else if (t2.equals("char")) {
				return t1;
			} else if (t2.equals("boolean")) {
				return t1;
			}
		} else if (t1.equals("boolean")) {
			if (t2.equals("String")) {
				return t2;
			} else if (t2.equals("boolean")) {
				return t1;
			}
		}

		/*
		 * +,-,*,/ todo menos boolean char x num => num String + int => String
		 * String x int => error
		 */

		return tipoFinal;
	}

	private static String[] datosMetodo(Element e) {
		/*
		 * <method name="nivel4" visibility="private"> <type primitive="true"
		 * name="int" dimensions="2"/> <formal-arguments> <formal-argument
		 * name="s"> <type name="String" dimensions="2"/> </formal-argument>
		 * </formal-arguments> <block> ... </block> </method>
		 */

		Element argumentos[] = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("formal-argument"));

		String[] datos = new String[2 + argumentos.length];

		// Tomamos nombre
		datos[0] = e.getAttribute("name");

		for (int i = 0; i < argumentos.length; i++) {
			datos[i + 1] = ManipulacionElement
					.nodeListToElementArray(argumentos[i].getChildNodes())[0]
					.getAttribute("name");
		}

		// es necesario a�adir el tipo de dato que devuelve el m�todo al final
		Element tipo = ManipulacionElement.nodeListToElementArray(e
				.getChildNodes())[0];
		datos[datos.length - 1] = tipo.getAttribute("name");

		return datos;
	}

	private static int[] dimMetodo(Element e) // recogemos SOLO dimensiones de
	// los par�metros (no del tipo
	// de vuelta)
	{
		/*
		 * <method name="nivel4" visibility="private"> <type primitive="true"
		 * name="int" dimensions="2"/> <formal-arguments> <formal-argument
		 * name="s"> <type name="String" dimensions="2"/> </formal-argument>
		 * </formal-arguments> <block> ... </block> </method>
		 */

		Element argumentos[] = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("formal-argument"));

		int[] dimensionesParam = new int[argumentos.length];

		for (int i = 0; i < argumentos.length; i++) {
			String datoDim = ManipulacionElement
					.nodeListToElementArray(argumentos[i].getChildNodes())[0]
					.getAttribute("dimensions");
			dimensionesParam[i] = (datoDim.length() > 0 ? Integer
					.parseInt(datoDim) : 0);
		}

		return dimensionesParam;
	}

}