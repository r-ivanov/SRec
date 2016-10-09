package opciones;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.ManipulacionElement;

/**
 * Opción que permite la configuración de algunas opciones relacionadas con las
 * visualizaciones
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class OpcionOpsVisualizacion extends Opcion {
	private static final long serialVersionUID = 1076;

	public static final int MANTENER_HISTORIA = 10;
	public static final int ATENUAR_HISTORIA = 11;
	public static final int ELIMINAR_HISTORIA = 12;

	public static final int DATOS_TODOS = 30;
	public static final int DATOS_ENTRADA = 31;
	public static final int DATOS_SALIDA = 32;

	// Atributos (asignamos valores por defecto)
	private int historia = OpcionOpsVisualizacion.MANTENER_HISTORIA;
	private int datosMostrar = OpcionOpsVisualizacion.DATOS_TODOS;
	private boolean mostrarArbolSalto = true;

	private boolean mostrarVisor = true;// Conf.mostrarVisor;

	private boolean mostrarEstructuraEnArbol = true;

	private boolean mostrarEstructCompletaCrono = false;
	private boolean mostrarSalidaLigadaEntrada = false;

	private boolean mostrarArbolColapsado = true;

	private boolean sangrado = true;
	private boolean idMetodoTraza = false;
	private boolean soloEstructuraDYVcrono = true;
	private boolean arranqueEstadoInicial = true;
	private boolean ajustarVistaGlobal = true;
	private boolean visualizacionDinamica = true;

	/**
	 * Constructor: Crea una nueva instancia, con valores por defecto, para la
	 * opción OpcionOpsVisualizacion
	 */
	public OpcionOpsVisualizacion() {
		super("OpcionOpsVisualizacion");
	}

	/**
	 * Permite establecer el tipo de visualización para la historia de
	 * ejecución.
	 * 
	 * @param x
	 *            MANTENER_HISTORIA, ATENUAR_HISTORIA o ELIMINAR_HISTORIA.
	 */
	public void setHistoria(int x) {
		if (x >= MANTENER_HISTORIA && x <= ELIMINAR_HISTORIA) {
			this.historia = x;
		}
	}

	/**
	 * Permite establecer el tipo de visualización para la historia de
	 * ejecución.
	 * 
	 * @param s
	 *            "Mantener", "Atenuar" o "Eliminar"
	 */
	public void setHistoria(String s) {
		if (s.equals("Mantener")) {
			this.setHistoria(MANTENER_HISTORIA);
		} else if (s.equals("Atenuar")) {
			this.setHistoria(ATENUAR_HISTORIA);
		} else if (s.equals("Eliminar")) {
			this.setHistoria(ELIMINAR_HISTORIA);
		}
	}

	/**
	 * Permite establecer el valor para la opción de mostrar saltos de árboles.
	 * 
	 * @param b
	 *            valor para mostrar saltos de árboles.
	 */
	public void setMostrarArbolSalto(boolean b) {
		this.mostrarArbolSalto = b;
	}

	/**
	 * Permite establecer el tipo de visualización para los nodos de entrada y
	 * salida.
	 * 
	 * @param x
	 *            DATOS_TODOS, DATOS_SALIDA, DATOS_ENTRADA
	 */
	public void setDatosMostrar(int x) {
		if (x >= DATOS_TODOS && x <= DATOS_SALIDA) {
			this.datosMostrar = x;
		}
	}

	/**
	 * Permite establecer el tipo de visualización para los nodos de entrada y
	 * salida.
	 * 
	 * @param s1
	 *            "true" si se desean visualizar datos de entrada, "false" en
	 *            caso contrario.
	 * @param s2
	 *            "true" si se desean visualizar datos de salida, "false" en
	 *            caso contrario.
	 */
	public void setDatosMostrar(String s1, String s2) {
		if (s1.equals("true") && s2.equals("true")) {
			this.setDatosMostrar(DATOS_TODOS);
		} else if (s1.equals("true") && s2.equals("false")) {
			this.setDatosMostrar(DATOS_ENTRADA);
		} else if (s1.equals("false") && s2.equals("true")) {
			this.setDatosMostrar(DATOS_SALIDA);
		}
	}

	/**
	 * Devuelve el valor del tipo de visualización para la historia de
	 * ejecución.
	 * 
	 * @return MANTENER_HISTORIA, ATENUAR_HISTORIA o ELIMINAR_HISTORIA
	 */
	public int getHistoria() {
		return this.historia;
	}

	/**
	 * Devuelve el valor del tipo de visualización para la historia de ejecución
	 * como string.
	 * 
	 * @return "Mantener", "Atenuar" o "Eliminar"
	 */
	public String getHistoriaString() {
		if (this.historia == MANTENER_HISTORIA) {
			return "Mantener";
		} else if (this.historia == ATENUAR_HISTORIA) {
			return "Atenuar";
		} else if (this.historia == ELIMINAR_HISTORIA) {
			return "Eliminar";
		}
		return "Mantener";
	}

	/**
	 * Devuelve el valor del atributo datosMostrar
	 * 
	 * @return valor de atributo datosMostrar
	 */
	public int getDatosMostrar() {
		return this.datosMostrar;
	}

	/**
	 * Devuelve el valor para la opción de mostrar saltos de árboles.
	 * 
	 * @return valor para mostrar saltos de árboles.
	 */
	public boolean getMostrarArbolSalto() {
		return this.mostrarArbolSalto;
	}

	/**
	 * Especifica si el visor debe mostrarse o no.
	 * 
	 * @return true si debe mostrarse, false en caso contrario.
	 */
	public boolean getMostrarVisor() {
		return this.mostrarVisor;
	}

	/**
	 * Permite estableder si el visor debe mostrarse o no.
	 * 
	 * @param valor
	 *            true si debe mostrarse, false en caso contrario.
	 */
	public void setMostrarVisor(boolean valor) {
		this.mostrarVisor = valor;
	}

	/**
	 * Especifica si el árbol debe mostrarse colapsado o no.
	 * 
	 * @return true si debe mostrarse colapsado, false en caso contrario.
	 */
	public boolean getMostrarArbolColapsado() {
		return this.mostrarArbolColapsado;
	}

	/**
	 * Permite estableder si el árbol debe mostrarse colapsado o no.
	 * 
	 * @param valor
	 *            true si debe mostrarse colapsado, false en caso contrario.
	 */
	public void setMostrarArbolColapsado(boolean valor) {
		this.mostrarArbolColapsado = valor;
	}

	/**
	 * Devuelve si la vista de traza debe mostrarse tabulada o no.
	 * 
	 * @return true si debe mostrarse tabulada, false en caso contrario.
	 */
	public boolean getSangrado() {
		return this.sangrado;
	}

	/**
	 * Permite estableder si la vista de traza debe mostrarse tabulada o no.
	 * 
	 * @param valor
	 *            true si debe mostrarse tabulada, false en caso contrario.
	 */
	public void setSangrado(boolean valor) {
		this.sangrado = valor;
	}

	/**
	 * Devuelve si debe mostrarse el identificador de método en los nodos del
	 * árbol o no.
	 * 
	 * @return true si debe mostrarse, false en caso contrario.
	 */
	public boolean getIdMetodoTraza() {
		return this.idMetodoTraza;
	}

	/**
	 * Permite establecer si debe mostrarse el identificador de método en los
	 * nodos del árbol o no.
	 * 
	 * @param valor
	 *            true si debe mostrarse, false en caso contrario.
	 */
	public void setIdMetodoTraza(boolean valor) {
		this.idMetodoTraza = valor;
	}

	/**
	 * Devuelve si solo debe mostrarse la estructura de datos en la vista
	 * cronológica de la estructura DYV.
	 * 
	 * @return true si solo debe mostrarse la estructura de datos, false en caso
	 *         contrario.
	 */
	public boolean getSoloEstructuraDYVcrono() {
		return this.soloEstructuraDYVcrono;
	}

	/**
	 * Permite establecer si solo debe mostrarse la estructura de datos en la
	 * vista cronológica de la estructura DYV.
	 * 
	 * @param valor
	 *            true si solo debe mostrarse la estructura de datos, false en
	 *            caso contrario.
	 */
	public void setSoloEstructuraDYVcrono(boolean valor) {
		this.soloEstructuraDYVcrono = valor;
	}

	/**
	 * Devuelve si debe mostrarse la estructura de datos completa en la vista
	 * cronológica de la estructura DYV.
	 * 
	 * @return true si debe mostrarse la estructura de datos completa, false en
	 *         caso contrario.
	 */
	public boolean getMostrarEstructuraCompletaCrono() {
		return this.mostrarEstructCompletaCrono;
	}

	/**
	 * Permite establecer si debe mostrarse la estructura de datos completa en
	 * la vista cronológica de la estructura DYV.
	 * 
	 * @param v
	 *            true si debe mostrarse la estructura de datos completa, false
	 *            en caso contrario.
	 */
	public void setMostrarEstructuraCompletaCrono(boolean v) {
		this.mostrarEstructCompletaCrono = v;
	}

	/**
	 * Devuelve si las ejecuciones de algoritmos deben arrancar desde su estado
	 * inicial.
	 * 
	 * @return true si deben arrancarse desde su estado inicial, false en caso
	 *         contrario.
	 */
	public boolean getArranqueEstadoInicial() {
		return this.arranqueEstadoInicial;
	}

	/**
	 * Permite establecer si las ejecuciones de algoritmos deben arrancar desde
	 * su estado inicial.
	 * 
	 * @param v
	 *            true si deben arrancarse desde su estado inicial, false en
	 *            caso contrario.
	 */
	public void setArranqueEstadoInicial(boolean v) {
		this.arranqueEstadoInicial = v;
	}

	/**
	 * Devuelve si la vista global debe mostrarse ajustada.
	 * 
	 * @return true si debe mostrarse ajustada, false en caso contrario.
	 */
	public boolean getAjustarVistaGlobal() {
		return this.ajustarVistaGlobal;
	}

	/**
	 * Permite establecer si la vista global debe mostrarse ajustada.
	 * 
	 * @param v
	 *            true si debe mostrarse ajustada, false en caso contrario.
	 */
	public void setAjustarVistaGlobal(boolean v) {
		this.ajustarVistaGlobal = v;
	}

	/**
	 * Devuelve si la vista global debe mostrarse dinámicamente, es decir
	 * utilizando los nodos visibles para establecer la forma del árbol.
	 * 
	 * @return true si debe mostrarse dinámicamente, false en caso contrario.
	 */
	public boolean getVisualizacionDinamica() {
		return this.visualizacionDinamica;
	}

	/**
	 * Permite establecer si la vista global debe mostrarse dinámicamente, es
	 * decir utilizando los nodos visibles para establecer la forma del árbol.
	 * 
	 * @param v
	 *            true si debe mostrarse dinámicamente, false en caso contrario.
	 */
	public void setVisualizacionDinamica(boolean v) {
		this.visualizacionDinamica = v;
	}

	/**
	 * Devuelve si debe mostrarse la estructura DYV junto a los nodos del árbol.
	 * 
	 * @return true si debe mostrarse, false en caso contrario.
	 */
	public boolean getMostrarEstructuraEnArbol() {
		return this.mostrarEstructuraEnArbol;
	}

	/**
	 * Permite establecer si debe mostrarse la estructura DYV junto a los nodos
	 * del árbol.
	 * 
	 * @param v
	 *            true si debe mostrarse, false en caso contrario.
	 */
	public void setMostrarEstructuraEnArbol(boolean v) {
		this.mostrarEstructuraEnArbol = v;
	}

	/**
	 * Devuelve si los eventos de entrada deben mostrarse ligados a los de
	 * salida en la vista de traza.
	 * 
	 * @return true si deben mostrarse ligados, false en caso contrario.
	 */
	public boolean getMostrarSalidaLigadaEntrada() {
		return this.mostrarSalidaLigadaEntrada;
	}

	/**
	 * Permite establecer si los eventos de entrada deben mostrarse ligados a
	 * los de salida en la vista de traza.
	 * 
	 * @param v
	 *            true si deben mostrarse ligados, false en caso contrario.
	 */
	public void setMostrarSalidaLigadaEntrada(boolean v) {
		this.mostrarSalidaLigadaEntrada = v;
	}

	/**
	 * Devuelve si deben mostrarse los nodos de entrada en las visualizaciones.
	 * 
	 * @return true si deben mostrarse, false en caso contrario.
	 */
	public boolean mostrarEntrada() {
		return this.datosMostrar == DATOS_TODOS
				|| this.datosMostrar == DATOS_ENTRADA;
	}

	/**
	 * Devuelve si deben mostrarse los nodos de salida en las visualizaciones.
	 * 
	 * @return true si deben mostrarse, false en caso contrario.
	 */
	public boolean mostrarSalida() {
		return this.datosMostrar == DATOS_TODOS
				|| this.datosMostrar == DATOS_SALIDA;
	}

	@Override
	public Element getRepresentacionElement(Document d) {
		Element e = d.createElement("OpcionOpsVisualizacion");

		Element e01 = d.createElement("historia");
		e01.setAttribute("valor", this.getHistoriaString());

		Element e02 = d.createElement("datosMostrar");
		e02.setAttribute("entrada", "" + this.mostrarEntrada());
		e02.setAttribute("salida", "" + this.mostrarSalida());

		Element e03 = d.createElement("mostrarArbolSalto");
		e03.setAttribute("valor", "" + this.getMostrarArbolSalto());

		Element e04 = d.createElement("mostrarVisor");
		e04.setAttribute("valor", "" + this.getMostrarVisor());

		Element e05 = d.createElement("mostrarECCrono");
		e05.setAttribute("valor", "" + this.getMostrarEstructuraCompletaCrono());

		Element e06 = d.createElement("mostrarSalidaLigadaEntrada");
		e06.setAttribute("valor", "" + this.getMostrarSalidaLigadaEntrada());

		Element e07 = d.createElement("mostrarEstructuraArbol");
		e07.setAttribute("valor", "" + this.getMostrarEstructuraEnArbol());

		Element e08 = d.createElement("mostrarArbolColapsado");
		e08.setAttribute("valor", "" + this.getMostrarArbolColapsado());

		Element e09 = d.createElement("sangrado");
		e09.setAttribute("valor", "" + this.getSangrado());

		Element e10 = d.createElement("idMetodoTraza");
		e10.setAttribute("valor", "" + this.getIdMetodoTraza());

		Element e11 = d.createElement("soloEstructuraDYVcrono");
		e11.setAttribute("valor", "" + this.getSoloEstructuraDYVcrono());

		Element e12 = d.createElement("arranqueEstadoInicial");
		e12.setAttribute("valor", "" + this.getArranqueEstadoInicial());

		Element e13 = d.createElement("ajustarVistaGlobal");
		e13.setAttribute("valor", "" + this.getAjustarVistaGlobal());

		Element e14 = d.createElement("visualizacionDinamica");
		e14.setAttribute("valor", "" + this.getVisualizacionDinamica());

		e.appendChild(e01);
		e.appendChild(e02);
		e.appendChild(e03);
		e.appendChild(e04);
		e.appendChild(e05);
		e.appendChild(e06);
		e.appendChild(e07);
		e.appendChild(e08);
		e.appendChild(e09);
		e.appendChild(e10);
		e.appendChild(e11);
		e.appendChild(e12);
		e.appendChild(e13);
		e.appendChild(e14);

		return e;
	}

	@Override
	public void setValores(Element e) {

		Element elements[] = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("historia"));
		this.setHistoria(elements[0].getAttribute("valor"));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("datosMostrar"));
		this.setDatosMostrar(elements[0].getAttribute("entrada"),
				elements[0].getAttribute("salida"));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("mostrarArbolSalto"));
		this.setMostrarArbolSalto(elements[0].getAttribute("valor").equals(
				"true"));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("mostrarVisor"));
		this.setMostrarVisor(elements[0].getAttribute("valor").equals("true"));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("mostrarECCrono"));
		this.setMostrarEstructuraCompletaCrono(elements[0]
				.getAttribute("valor").equals("true"));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("mostrarEstructuraArbol"));
		this.setMostrarEstructuraEnArbol(elements[0].getAttribute("valor")
				.equals("true"));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("mostrarSalidaLigadaEntrada"));
		if (elements.length > 0) {
			this.setMostrarSalidaLigadaEntrada(elements[0]
					.getAttribute("valor").equals("true"));
		} else {
			this.setMostrarSalidaLigadaEntrada(false);
		}

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("mostrarArbolColapsado"));
		if (elements.length > 0) {
			this.setMostrarArbolColapsado(elements[0].getAttribute("valor")
					.equals("true"));
		} else {
			this.setMostrarArbolColapsado(false);
		}

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("sangrado"));
		if (elements.length > 0) {
			this.setSangrado(elements[0].getAttribute("valor").equals("true"));
		} else {
			this.setSangrado(false);
		}

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("idMetodoTraza"));
		if (elements.length > 0) {
			this.setIdMetodoTraza(elements[0].getAttribute("valor").equals(
					"true"));
		} else {
			this.setIdMetodoTraza(false);
		}

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("soloEstructuraDYVcrono"));
		if (elements.length > 0) {
			this.setSoloEstructuraDYVcrono(elements[0].getAttribute("valor")
					.equals("true"));
		} else {
			this.setSoloEstructuraDYVcrono(false);
		}

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("arranqueEstadoInicial"));
		if (elements.length > 0) {
			this.setArranqueEstadoInicial(elements[0].getAttribute("valor")
					.equals("true"));
		} else {
			this.setArranqueEstadoInicial(false);
		}

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("ajustarVistaGlobal"));
		if (elements.length > 0) {
			this.setAjustarVistaGlobal(elements[0].getAttribute("valor")
					.equals("true"));
		} else {
			this.setAjustarVistaGlobal(false);
		}

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("visualizacionDinamica"));
		if (elements.length > 0) {
			this.setVisualizacionDinamica(elements[0].getAttribute("valor")
					.equals("true"));
		} else {
			this.setVisualizacionDinamica(false);
		}
	}
}