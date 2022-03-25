package datos;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import opciones.GestorOpciones;
import opciones.OpcionConfVisualizacion;
import opciones.OpcionFicherosRecientes;
import opciones.OpcionOpsVisualizacion;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.Arrays;
import utilidades.ManipulacionElement;
import utilidades.SelecDireccion;
import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroPreguntaSobreescribir;
import cuadros.CuadroProgreso;

/**
 * Almacena trazas guardadas como objeto Traza en un fichero XMl para su
 * posterior recuperación
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2008
 */
public class AlmacenadorTraza {

	private String fichero[] = new String[2];
	private Ventana vv;

	private GestorOpciones gOpciones = new GestorOpciones();
	private OpcionFicherosRecientes ofr;
	private OpcionOpsVisualizacion oov;
	private OpcionConfVisualizacion ocv;

	private CuadroProgreso cuadroProgreso;

	private Traza traza;
	private Traza trazaCompleta;
	private DatosTrazaBasicos dtb;

	/**
	 * Crea un nuevo gestor para el almacenamiento de trazas en formato XML
	 * 
	 * @param ventana
	 *            Ventana de la aplicación.
	 * @param traza
	 *            Contiene la traza a almacenar.
	 * @param trazaCompleta
	 *            contiene la traza en su estado final a almacenar.
	 * @param dtb
	 *            Datos básicos de la traza.
	 */
	public AlmacenadorTraza(Ventana ventana, Traza traza, Traza trazaCompleta,
			DatosTrazaBasicos dtb) {

		this.vv = ventana;
		this.traza = traza;
		this.trazaCompleta = trazaCompleta;
		this.dtb = dtb;

		// Cargamos opción de ficheros recientes (para saber último directorio)
		this.ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
				"OpcionFicherosRecientes", true);
		this.fichero[0] = this.ofr.getDirXML();

		this.fichero = SelecDireccion.cuadroAbrirFichero(this.fichero[0],
				Texto.get("CA_GUARDVIS", Conf.idioma), null, "xml",
				Texto.get("ARCHIVO_XML", Conf.idioma), 0);

		// *1* Comprobar que fichero existe
		File f = new File(this.fichero[0] + this.fichero[1]);
		if (!f.exists()) {
			this.ejecutar();
		} else {
			new CuadroPreguntaSobreescribir(this.vv, this);
		}

	}

	/**
	 * Almacena la traza en el fichero especificado.
	 */
	public synchronized void ejecutar() {

		if (this.fichero != null && this.fichero[1] != null) {
			this.ofr.setDirXML(this.fichero[0]);
			this.gOpciones.setOpcion(this.ofr, 2);

			this.cuadroProgreso = new CuadroProgreso(this.vv, Texto.get(
					"CP_ESPERE", Conf.idioma), Texto.get("CP_ACTUAL",
							Conf.idioma), 0);

			if (!this.fichero[1].toLowerCase().contains(".xml")) {
				this.fichero[1] = this.fichero[1] + ".xml";
			}

			// Generamos fichero con la estructura básica, la rellenaremos
			// después
			this.cuadroProgreso.setValores(Texto.get("CP_ACTUAL", Conf.idioma),
					10);

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = null;
			Document documento = null;

			try {
				builder = factory.newDocumentBuilder();
				documento = builder.newDocument();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Element elemV = documento.createElement("Visualizacion");
			Element elem1 = documento.createElement("OpConf");
			Element elem2 = documento.createElement("OpFormato");
			Element elem3 = documento.createElement("DatosVisibilidad");
			Element elem4 = documento.createElement("Traza");
			Element elem5 = documento.createElement("TrazaCompleta");

			elemV.appendChild(elem1);
			elemV.appendChild(elem2);
			elemV.appendChild(elem3);
			elemV.appendChild(elem4);
			elemV.appendChild(elem5);
			documento.appendChild(elemV);

			Element nodos[] = ManipulacionElement.getChildElements(documento
					.getDocumentElement());

			// Almacenamos los valores de las opciones de configuración de la
			// visualización
			// Primero cargamos la opción desde fichero para obtener valores
			// actualizados

			this.cuadroProgreso.setValores(Texto.get("CP_ESCOP", Conf.idioma),
					20);

			this.oov = (OpcionOpsVisualizacion) this.gOpciones.getOpcion(
					"OpcionOpsVisualizacion", false);
			this.ocv = (OpcionConfVisualizacion) this.gOpciones.getOpcion(
					"OpcionConfVisualizacion", false);

			this.generarValoresOpConf(documento, nodos[0]);

			// Almacenamos los valores del formato de la visualización
			// Generamos los valores de las opciones de configuración
			this.cuadroProgreso.setValores(Texto.get("CP_ESCOP", Conf.idioma),
					35);

			this.generarValoresOpFormato(documento, nodos[1]);
			this.generarDatosVisibilidad(documento, nodos[2]);

			// Almacenamos los valores de la traza
			this.cuadroProgreso.setValores(Texto.get("CP_ESCTR", Conf.idioma),
					65);
			this.generarTraza(this.traza, documento, nodos[3]);
			this.generarTraza(this.trazaCompleta, documento, nodos[4]);

			// Guardamos la estructura Document al fichero
			this.cuadroProgreso.setValores(Texto.get("CP_ESCFI", Conf.idioma),
					90);
			ManipulacionElement.writeXmlFile(documento, this.fichero[0]
					+ this.fichero[1]);
			this.cuadroProgreso.cerrar();
		}
	}

	/**
	 * Creamos un elemento del arbol DOM que contiene los valores de las
	 * opciones
	 * 
	 * @param d
	 *            Árbol DOM que contendrá toda la información sobre la traza y
	 *            que será volcado al XML
	 * @param e
	 *            Elemento al que se añadirán los valores insertados
	 */
	private void generarValoresOpConf(Document d, Element e) {
		Element datosMostrar = d.createElement("DatosMostrar");
		e.appendChild(datosMostrar);
		datosMostrar.setAttribute("entrada", "" + this.oov.mostrarEntrada());
		datosMostrar.setAttribute("salida", "" + this.oov.mostrarSalida());

		Element mostrarHistoria = d.createElement("MostrarHistoria");
		e.appendChild(mostrarHistoria);
		mostrarHistoria.setAttribute("estadoHistoria",
				"" + this.oov.getHistoriaString());

		Element mostrarArbolSalto = d.createElement("MostrarArbolSalto");
		e.appendChild(mostrarArbolSalto);
		mostrarArbolSalto.setAttribute("mostrarArbol",
				"" + this.oov.getMostrarArbolSalto());

		Element mostrarEstructuraEnArbol = d
				.createElement("MostrarEstructuraEnArbol");
		e.appendChild(mostrarEstructuraEnArbol);
		mostrarEstructuraEnArbol.setAttribute("mostrarEstructura", ""
				+ this.oov.getMostrarEstructuraEnArbol());

	}

	/**
	 * Creamos un elemento del arbol DOM que contiene los valores de las
	 * opciones de formato
	 * 
	 * @param d
	 *            Árbol DOM que contendrá toda la información sobre la traza y
	 *            que será volcado al XML
	 * @param e
	 *            Elemento al que se añadirán los valores insertados
	 */
	private void generarValoresOpFormato(Document d, Element e) {
		Element celda1 = d.createElement("Celda");
		e.appendChild(celda1);
		celda1.setAttribute("nombre", "entrada");
		celda1.setAttribute("degradado", "" + this.ocv.getColorDegradadoModo1());

		Element celda2 = d.createElement("Celda");
		e.appendChild(celda2);
		celda2.setAttribute("nombre", "salida");
		celda2.setAttribute("degradado", "" + this.ocv.getColorDegradadoModo2());

		Element otros = d.createElement("Otros");
		e.appendChild(otros);

		this.anadirColor(d, celda1, "fuente", this.ocv.getColorFEntrada());
		this.anadirColor(d, celda1, "color1", this.ocv.getColorC1Entrada());
		this.anadirColor(d, celda1, "color1a", this.ocv.getColorCAEntrada());

		this.anadirColor(d, celda2, "fuente", this.ocv.getColorFSalida());
		this.anadirColor(d, celda2, "color1", this.ocv.getColorC1Salida());
		this.anadirColor(d, celda2, "color1a", this.ocv.getColorCASalida());
		this.anadirColor(d, celda2, "color1nc", this.ocv.getColorC1NCSalida());

		for (int i = 0; i < Conf.numColoresMetodos; i++) {
			this.anadirColor(d, otros, "ecm2_" + i, this.ocv.getColorModo2(i));
		}

		this.anadirColor(d, otros, "flecha", this.ocv.getColorFlecha());
		this.anadirColor(d, otros, "panel", this.ocv.getColorPanel());
		this.anadirColor(d, otros, "marcoActual", this.ocv.getColorActual());
		this.anadirColor(d, otros, "caminoActual", this.ocv.getColorCActual());

		this.anadirNodo(d, otros, "Grosor", "flecha",
				this.ocv.getGrosorFlecha());
		this.anadirNodo(d, otros, "Grosor", "marcoActual",
				this.ocv.getGrosorActual());
		this.anadirNodo(d, otros, "Distancia", "vertical",
				this.ocv.getDistanciaV());
		this.anadirNodo(d, otros, "Distancia", "horizontal",
				this.ocv.getDistanciaH());
		this.anadirNodo(d, otros, "Tipo", "bordeCelda",
				this.ocv.getTipoBordeCelda());
		this.anadirNodo(d, otros, "Tipo", "formaFlecha",
				this.ocv.getFormaFlecha());

		this.anadirNodo(d, otros, "modoColor", "modo",
				"" + this.ocv.getModoColor());

		this.anadirNodo(d, otros, "modoColorDegr1", "degr",
				"" + this.ocv.getColorDegradadoModo1());
		this.anadirNodo(d, otros, "modoColorDegr2", "degr",
				"" + this.ocv.getColorDegradadoModo2());

		this.anadirNodo(d, otros, "Fuente", "trazaE",
				this.ocv.getFuenteCodigo());
		this.anadirNodo(d, otros, "Fuente", "trazaS", this.ocv.getFuenteTraza());
		this.anadirNodo(d, otros, "FuenteTam", "trazaE",
				this.ocv.getTamFuenteCodigo());
		this.anadirNodo(d, otros, "FuenteTam", "trazaS",
				this.ocv.getTamFuenteTraza());

		this.anadirNodo(d, otros, "Zoom", "arbol", this.ocv.getZoomArbol());
		this.anadirNodo(d, otros, "Zoom", "pila", this.ocv.getZoomPila());
		
        this.anadirColor(d, otros, "colorErroresCodigo", this.ocv.getColorErroresCodigo());
        
        Element temaColorEditor = d.createElement("temaColorEditor");
        otros.appendChild(temaColorEditor);
        temaColorEditor.setAttribute("tema", Integer.toString(this.ocv.getTemaColorEditor()));
	}

	/**
	 * Función auxiliar para generar un elemento con la información de un color
	 * 
	 * @param d
	 *            Árbol DOM que contendrá toda la información sobre la traza y
	 *            que será volcado al XML
	 * @param e
	 *            Elemento al que se añadirán los valores insertados
	 * @param destino
	 *            Nombre del elemento que tendrá el color
	 * @param valores
	 *            repertorio de valores
	 */
	private void anadirColor(Document d, Element e, String destino,
			int valores[]) {
		Element color = d.createElement("Color");
		e.appendChild(color);

		color.setAttribute("destino", destino);
		color.setAttribute("r", "" + valores[0]);
		color.setAttribute("g", "" + valores[1]);
		color.setAttribute("b", "" + valores[2]);
	}

	/**
	 * Función auxiliar para generar un elemento.
	 * 
	 * @param d
	 *            Árbol DOM que contendrá toda la información sobre la traza y
	 *            que será volcado al XML
	 * @param e
	 *            Elemento al que se añadirán los valores insertados
	 * @param nombre
	 *            define el tipo de elemento
	 * @param destino
	 *            Nombre del elemento que tendrá el valor
	 * @param valor
	 *            Valor del elemento
	 */
	private void anadirNodo(Document d, Element e, String nombre,
			String destino, int valor) {
		Element grosor = d.createElement(nombre);
		e.appendChild(grosor);

		grosor.setAttribute("destino", destino);
		grosor.setAttribute("tam", "" + valor);
	}

	/**
	 * Función auxiliar para generar un elemento.
	 * 
	 * @param d
	 *            Árbol DOM que contendrá toda la información sobre la traza y
	 *            que será volcado al XML
	 * @param e
	 *            Elemento al que se añadirán los valores insertados
	 * @param nombre
	 *            define el tipo de elemento
	 * @param destino
	 *            Nombre del elemento que tendrá el valor
	 * @param valor
	 *            Valor del elemento
	 */
	private void anadirNodo(Document d, Element e, String nombre,
			String destino, String valor) {
		Element grosor = d.createElement(nombre);
		e.appendChild(grosor);

		grosor.setAttribute("destino", destino);
		grosor.setAttribute("tam", valor);
	}

	/**
	 * Función auxiliar para generar los datos de visibilidad para los distintos
	 * métodos.
	 * 
	 * @param d
	 *            Árbol DOM que contendrá toda la información sobre la traza y
	 *            que será volcado al XML
	 * @param e
	 *            Elemento al que se añadirán los valores de visibilidad
	 */
	private void generarDatosVisibilidad(Document d, Element e) {
		for (int i = 0; i < this.dtb.getNumMetodos(); i++) {
			DatosMetodoBasicos dmt = this.dtb.getMetodo(i);
			int numParamE = dmt.getNumParametrosE();
			int numParamS = dmt.getNumParametrosS();

			Element metodo = d.createElement("Metodo");

			metodo.setAttribute("nombre", dmt.getNombre());
			metodo.setAttribute("retorno", "" + dmt.esMetodoConRetorno());
			metodo.setAttribute("metodoPrincipal", "" + dmt.getEsPrincipal());
			metodo.setAttribute("metodoVisible", "" + dmt.getEsVisible());

			Element param = d.createElement("Param");
			for (int j = 0; j < numParamE; j++) {
				Element paramE = d.createElement("ParamE");
				paramE.setAttribute("orden", "" + j);
				paramE.setAttribute("nombre", dmt.getNombreParametroE(j));
				paramE.setAttribute("tipo", dmt.getTipoParametroE(j));
				paramE.setAttribute("dim", "" + dmt.getDimParametroE(j));
				paramE.setAttribute("visible", "" + dmt.getVisibilidadE(j));
				param.appendChild(paramE);
			}

			for (int j = 0; j < numParamS; j++) {
				Element paramS = d.createElement("ParamS");
				paramS.setAttribute("orden", "" + j);
				paramS.setAttribute("nombre", dmt.getNombreParametroS(j));
				paramS.setAttribute("tipo", dmt.getTipoParametroS(j));
				paramS.setAttribute("dim", "" + dmt.getDimParametroS(j));
				paramS.setAttribute("visible", "" + dmt.getVisibilidadS(j));
				param.appendChild(paramS);
			}
			metodo.appendChild(param);
			e.appendChild(metodo);
		}

	}

	/**
	 * Función auxiliar para generar un elemento con la información de una traza
	 * completa
	 * 
	 * @param trazaParaGuardar
	 *            Traza
	 * @param d
	 *            Árbol DOM que contendrá toda la información sobre la traza y
	 *            que será volcado al XML
	 * @param e
	 *            Elemento al que se añadirán los valores insertados
	 */
	private void generarTraza(Traza trazaParaGuardar, Document d, Element e) {
		Element datosTraza = d.createElement("Datos");
		e.appendChild(datosTraza);

		datosTraza.setAttribute("nombre", trazaParaGuardar.getTitulo());

		datosTraza.setAttribute("archivo", trazaParaGuardar.getArchivo());
		datosTraza.setAttribute("idTraza", trazaParaGuardar.getIDTraza());
		datosTraza.setAttribute("metodoEjecucion",
				trazaParaGuardar.getNombreMetodoEjecucion());

		int[] tecnicas = trazaParaGuardar.getTecnicas();

		datosTraza.setAttribute("tecnicaREC",
				"" + Arrays.contiene(MetodoAlgoritmo.TECNICA_REC, tecnicas));
		datosTraza.setAttribute("tecnicaDYV",
				"" + Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV, tecnicas));
		datosTraza.setAttribute("tecnicaAABB",
				"" + Arrays.contiene(MetodoAlgoritmo.TECNICA_AABB, tecnicas));

		this.anadirRegistroActivacion(d, e, trazaParaGuardar.getRaiz());
	}

	/**
	 * Función auxiliar para generar un elemento con la información de un
	 * registro de activacion
	 * 
	 * @param d
	 *            Árbol DOM que contendrá toda la información sobre la traza y
	 *            que será volcado al XML
	 * @param e
	 *            Elemento al que se añadirán los valores insertados
	 * @param ra
	 *            registro de activación que contiene los datos que se guardarán
	 *            en el elemento
	 */
	private void anadirRegistroActivacion(Document d, Element e,
			RegistroActivacion ra) {
		Element raElement = d.createElement("RegistroActivacion");

		e.appendChild(raElement);

		Element valor = d.createElement("Valor");
		raElement.appendChild(valor);

		String[] parametrosE = ra.getEntradasString();
		String[] parametrosS = ra.getSalidasString();

		String[] clasesE = ra.clasesParamE();
		String[] clasesS = ra.clasesParamS();
		int[] dimE = ra.dimParamE();
		int[] dimS = ra.dimParamS();

		for (int i = 0; i < parametrosE.length; i++) {
			valor.setAttribute("paramE" + (i + 1), parametrosE[i]);
		}

		for (int i = 0; i < parametrosS.length; i++) {
			valor.setAttribute("paramS" + (i + 1), parametrosS[i]);
		}

		for (int i = 0; i < clasesE.length; i++) {
			valor.setAttribute("tipoE" + (i + 1), clasesE[i]);
		}

		for (int i = 0; i < clasesS.length; i++) {
			valor.setAttribute("tipoS" + (i + 1), clasesS[i]);
		}

		for (int i = 0; i < dimE.length; i++) {
			valor.setAttribute("dimE" + (i + 1), "" + dimE[i]);
		}

		for (int i = 0; i < dimS.length; i++) {
			valor.setAttribute("dimS" + (i + 1), "" + dimS[i]);
		}

		valor.setAttribute("entradaVisible", "" + ra.entradaVisible());
		valor.setAttribute("salidaVisible", "" + ra.salidaVisible());

		Element param = d.createElement("Param");
		raElement.appendChild(param);
		param.setAttribute("numHijos", "" + ra.numHijos());
		param.setAttribute("hijoVisible", "" + (ra.getHijosVisibles() - 1));
		param.setAttribute("historico", "" + ra.esHistorico());
		valor.setAttribute("actual", "" + ra.getEsNodoActual());
		valor.setAttribute("caminoActual", "" + ra.getEsCaminoActual());
		param.setAttribute("mostradoEntero", "" + ra.esMostradoEntero());
		param.setAttribute("contraido", "" + ra.contraido());
		param.setAttribute("inhibido", "" + ra.inhibido());
		param.setAttribute("nID", "" + ra.getID());
		param.setAttribute("iluminado", "" + ra.estaIluminado());

		Element metodo = d.createElement("Metodo");
		raElement.appendChild(metodo);
		metodo.setAttribute("nombreMetodo", ra.getNombreMetodo());
		metodo.setAttribute("devuelveValor", "" + ra.getDevuelveValor());
		String nombreParametros[] = ra.getNombreParametros();
		for (int i = 0; i < nombreParametros.length; i++) {
			metodo.setAttribute("nombreParametro" + (i + 1),
					nombreParametros[i]);
		}

		// Inicio - Almacenaje de estructura
		Element estructura = d.createElement("Estructura");
		raElement.appendChild(estructura);

		Estructura estr = null;
		if ((ra.getEntrada().getEstructura()) != null) {
			estr = new Estructura(ra.getEntrada().getEstructura());

			String claseEstructura = estr.getClase();

			estructura.setAttribute("tipo",
					claseEstructura.substring(0, claseEstructura.indexOf("[")));
			estructura.setAttribute("dim",
					(claseEstructura.contains("[][]") ? "2" : "1"));
			estructura.setAttribute("tam1", "" + estr.dimensiones()[0]);
			if (estr.esMatriz()) {
				estructura.setAttribute("tam2", "" + estr.dimensiones()[1]);
			}

			int indices[] = ra.getEntrada().getIndices();
			String stringIndices = "";
			for (int i = 0; i < indices.length; i++) {
				stringIndices = stringIndices + indices[i];
				if (i < (indices.length - 1)) {
					stringIndices = stringIndices + "|";
				}
			}

			estructura.setAttribute("indices", stringIndices);
			estructura.setAttribute("entrada", estr.getValor());
			estructura.setAttribute("indEstructuraE", ""
					+ ra.getEntrada().getIndiceDeEstructura());
			estructura.setAttribute("indEstructuraS", ""
					+ ra.getSalida().getIndiceDeEstructura());
			estructura.setAttribute("salida", new Estructura(ra.getSalida()
					.getEstructura()).getValor());

		}
		// Fin - Almacenaje de estructura

		Element hijos = d.createElement("Hijos");
		raElement.appendChild(hijos);
		for (int i = 0; i < ra.numHijos(); i++) {
			this.anadirRegistroActivacion(d, hijos, ra.getHijo(i));
		}
	}

}
