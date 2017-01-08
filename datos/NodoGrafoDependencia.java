package datos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.util.NonCollidingEdgeRouter;

import conf.Conf;

/**
 * Representa un nodo de un grafo de dependencia.
 * 
 * @author David Pastor Herranz
 */
public class NodoGrafoDependencia {

	private static final int ANCHO_PIZEL_CARACTER = 13;
	private static final int ALTURA_CELDA = 26;
	private static final int TAM_FUENTE = 20;
	private static final double MARGEN_COLISION = 10;

	private static final Edge.Routing edgeRouter;
	static {
		edgeRouter = new NonCollidingEdgeRouter();
	}

	private List<NodoGrafoDependencia> dependencias;
	private RegistroActivacion registroActivacion;

	private DefaultGraphCell celdaEntrada;
	private DefaultGraphCell celdaSalida;
	private DefaultGraphCell celdaGrafo;

	private DefaultPort portCelda;
	private List<DefaultEdge> aristas;

	/**
	 * Devuelve una nueva instancia.
	 * 
	 * @param registroActivacionAsociado
	 *            Registro de activación al que está asociado el nodo.
	 */
	public NodoGrafoDependencia(RegistroActivacion registroActivacionAsociado) {
		this.dependencias = new ArrayList<NodoGrafoDependencia>();
		this.aristas = new ArrayList<DefaultEdge>();
		this.registroActivacion = registroActivacionAsociado;
		this.portCelda = new DefaultPort();
		this.inicializarRepresentacion();
	}

	/**
	 * Inicializa la representación visual del nodo.
	 */
	private void inicializarRepresentacion() {

		String repEntrada = this.registroActivacion.getEntrada()
				.getRepresentacion();
		if (repEntrada.length() < 3) {
			repEntrada = "  " + repEntrada + "  ";
		}

		String repSalida = this.registroActivacion.getSalida()
				.getRepresentacion();
		if (repSalida.length() < 3) {
			repSalida = "  " + repSalida + "  ";
		}

		// Tamaños
		int tamanioCadena;
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
			tamanioCadena = repSalida.length() * ANCHO_PIZEL_CARACTER;
		} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
			tamanioCadena = repEntrada.length() * ANCHO_PIZEL_CARACTER;
		} else {
			tamanioCadena = Math.max(repSalida.length(), repEntrada.length())
					* ANCHO_PIZEL_CARACTER;
		}

		int previousX = 0;
		int previousY = 0;
		if (this.celdaGrafo != null) {
			Rectangle2D previousBounds = GraphConstants
					.getBounds(this.celdaGrafo.getAttributes());
			previousX = (int) previousBounds.getX();
			previousY = (int) previousBounds.getY();
		}

		this.celdaEntrada = new DefaultGraphCell(repEntrada);
		GraphConstants.setDisconnectable(this.celdaEntrada.getAttributes(),
				false);
		GraphConstants.setMoveable(this.celdaEntrada.getAttributes(), true);
		GraphConstants.setSelectable(this.celdaEntrada.getAttributes(), false);
		GraphConstants.setEditable(this.celdaEntrada.getAttributes(), false);
		GraphConstants.setFont(this.celdaEntrada.getAttributes(), new Font(
				"Arial", Font.BOLD, TAM_FUENTE));
		GraphConstants.setForeground(this.celdaEntrada.getAttributes(),
				Conf.colorFEntrada);
		GraphConstants.setOpaque(this.celdaEntrada.getAttributes(), true);
		
		if (this.registroActivacion.estaIluminado()) {
			GraphConstants.setBackground(this.celdaEntrada.getAttributes(),
					Conf.colorIluminado);
			GraphConstants.setGradientColor(this.celdaEntrada.getAttributes(),
					Conf.colorIluminado);
		} else if (this.registroActivacion.estaResaltado()) {
			GraphConstants.setBackground(this.celdaEntrada.getAttributes(),
					Conf.colorResaltado);
			GraphConstants.setGradientColor(this.celdaEntrada.getAttributes(),
					Conf.colorResaltado);
		} else {
			GraphConstants.setBackground(this.celdaEntrada.getAttributes(),
					(Conf.modoColor == 1 ? Conf.colorC1Entrada
							: Conf.coloresNodo[registroActivacion.getNumMetodo() % 10]));
			GraphConstants.setGradientColor(this.celdaEntrada.getAttributes(),
					(Conf.modoColor == 1 ? Conf.colorC2Entrada
							: Conf.coloresNodo2[registroActivacion.getNumMetodo() % 10]));
		}
		
		this.marcoCelda(this.celdaEntrada.getAttributes());
		GraphConstants
		.setBounds(this.celdaEntrada.getAttributes(), new Rectangle(
				previousX, previousY, tamanioCadena, ALTURA_CELDA));

		this.celdaSalida = new DefaultGraphCell(repSalida);
		GraphConstants.setDisconnectable(this.celdaSalida.getAttributes(),
				false);
		GraphConstants.setMoveable(this.celdaSalida.getAttributes(), true);
		GraphConstants.setSelectable(this.celdaSalida.getAttributes(), false);
		GraphConstants.setEditable(this.celdaSalida.getAttributes(), false);
		GraphConstants.setFont(this.celdaSalida.getAttributes(), new Font(
				"Arial", Font.BOLD, TAM_FUENTE));
		GraphConstants.setForeground(this.celdaSalida.getAttributes(),
				Conf.colorFSalida);
		GraphConstants.setOpaque(this.celdaSalida.getAttributes(), true);

		if (this.registroActivacion.estaIluminado()) {
			GraphConstants.setBackground(this.celdaSalida.getAttributes(),
					Conf.colorIluminado);
			GraphConstants.setGradientColor(this.celdaSalida.getAttributes(),
					Conf.colorIluminado);
		} else if (this.registroActivacion.estaResaltado()) {
			GraphConstants.setBackground(this.celdaSalida.getAttributes(),
					Conf.colorResaltado);
			GraphConstants.setGradientColor(this.celdaSalida.getAttributes(),
					Conf.colorResaltado);
		} else {
			GraphConstants.setBackground(this.celdaSalida.getAttributes(),
					(Conf.modoColor == 1 ? Conf.colorC1Salida
							: Conf.coloresNodo[registroActivacion.getNumMetodo() % 10]));
			GraphConstants.setGradientColor(this.celdaSalida.getAttributes(),
					(Conf.modoColor == 1 ? Conf.colorC2Salida
							: Conf.coloresNodo2[registroActivacion.getNumMetodo() % 10]));
		}
		this.marcoCelda(this.celdaSalida.getAttributes());
		GraphConstants
		.setBounds(
				this.celdaSalida.getAttributes(),
				new Rectangle(
						previousX,
						previousY
						+ (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA ? 0
								: ALTURA_CELDA), tamanioCadena,
								ALTURA_CELDA));

		this.celdaGrafo = new DefaultGraphCell();
		int celdasVisibles = 0;
		if (Conf.elementosVisualizar != Conf.VISUALIZAR_SALIDA) {
			this.celdaGrafo.add(this.celdaEntrada);
			celdasVisibles++;
		}

		if (Conf.elementosVisualizar != Conf.VISUALIZAR_ENTRADA) {
			this.celdaGrafo.add(this.celdaSalida);
			celdasVisibles++;
		}

		GraphConstants.setBounds(this.celdaGrafo.getAttributes(),
				new Rectangle(previousX, previousY, tamanioCadena, ALTURA_CELDA
						* celdasVisibles));
		GraphConstants.setSize(this.celdaGrafo.getAttributes(), new Dimension(
				tamanioCadena, ALTURA_CELDA * celdasVisibles));
		GraphConstants
		.setDisconnectable(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setMoveable(this.celdaGrafo.getAttributes(), true);
		GraphConstants.setSelectable(this.celdaGrafo.getAttributes(), true);
		GraphConstants.setResize(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setOpaque(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setAutoSize(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setSizeable(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setCollisionMargin(this.celdaGrafo.getAttributes(),
				MARGEN_COLISION);

		this.celdaGrafo.add(this.portCelda);
	}

	/**
	 * Establece el diseño del marco de la celda dado su mapa de atributos.
	 * 
	 * @param am
	 *            Mapa de atributos de una celda concreta.
	 * @param anular
	 *            A true si se desea eliminar el borde, false en caso contrario.
	 */
	private void marcoCelda(AttributeMap am) {
		switch (Conf.bordeCelda) {
		case 1:
			GraphConstants.setBorder(am, BorderFactory.createBevelBorder(0));
			break;
		case 2:
			GraphConstants.setBorder(am, BorderFactory.createEtchedBorder());
			break;
		case 3:
			GraphConstants.setBorder(am,
					BorderFactory.createLineBorder(Conf.colorFlecha));
			break;
		case 4:
			GraphConstants.setBorder(am,
					BorderFactory.createLoweredBevelBorder());
			break;
		case 5:
			GraphConstants.setBorder(am,
					BorderFactory.createRaisedBevelBorder());
			break;
		}
	}

	/**
	 * Establece la anchura de la representación visual del nodo.
	 * 
	 * @param anchura
	 *            Anchura de la representación visual del nodo.
	 */
	public void setAnchura(double anchura) {
		Rectangle2D bounds = GraphConstants.getBounds(this.celdaGrafo
				.getAttributes());
		GraphConstants.setBounds(this.celdaGrafo.getAttributes(),
				new Rectangle2D.Double(bounds.getX(), bounds.getY(), anchura,
						bounds.getHeight()));

		Rectangle2D boundsEntrada = GraphConstants.getBounds(this.celdaEntrada
				.getAttributes());
		GraphConstants.setBounds(
				this.celdaEntrada.getAttributes(),
				new Rectangle2D.Double(boundsEntrada.getX(), boundsEntrada
						.getY(), anchura, ALTURA_CELDA));

		Rectangle2D boundsSalida = GraphConstants.getBounds(this.celdaSalida
				.getAttributes());
		GraphConstants.setBounds(this.celdaSalida.getAttributes(),
				new Rectangle2D.Double(boundsSalida.getX(),
						boundsSalida.getY(), anchura, ALTURA_CELDA));
	}

	/**
	 * Devuelve la anchura actual.
	 * 
	 * @return Anchura.
	 */
	public double getAnchura() {
		return GraphConstants.getSize(this.celdaGrafo.getAttributes())
				.getWidth();
	}

	/**
	 * Devuelve la altura actual.
	 * 
	 * @return Anchura.
	 */
	public double getAltura() {
		return GraphConstants.getSize(this.celdaGrafo.getAttributes())
				.getHeight();
	}

	/**
	 * Devuelve los bounds de la representación visual.
	 * 
	 * @return Bounds de la representación visual.
	 */
	public Rectangle2D getBounds() {
		return GraphConstants.getBounds(this.celdaGrafo.getAttributes());
	}

	/**
	 * Establece el nodo en la posición indicada.
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosicion(int x, int y) {
		Rectangle2D bounds = GraphConstants.getBounds(this.celdaGrafo
				.getAttributes());

		int dimX = (int) bounds.getWidth();
		int dimY = (int) bounds.getHeight();

		GraphConstants.setBounds(this.celdaGrafo.getAttributes(),
				new Rectangle(x, y, dimX, dimY));
		GraphConstants.setBounds(this.celdaEntrada.getAttributes(),
				new Rectangle(x, y, dimX, ALTURA_CELDA));
		GraphConstants
		.setBounds(
				this.celdaSalida.getAttributes(),
				new Rectangle(
						x,
						y
						+ (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA ? 0
								: ALTURA_CELDA), dimX,
								ALTURA_CELDA));
	}

	/**
	 * Permite comparar dos nodos de un grafo de dependencia.
	 * 
	 * @param nodo
	 * 
	 * @return true si este nodo y el pasado por parámetro son iguales, false en
	 *         caso contrario.
	 */
	public boolean equals(NodoGrafoDependencia nodo) {
		return this.clasesParametrosEntradaIguales(nodo)
				&& this.nombreParametrosEntradaIguales(nodo)
				&& this.valorParametrosEntradaIguales(nodo)
				&& this.nombreMetodoIgual(nodo);
	}

	/**
	 * Devuelve los parámetros de entrada del nodo.
	 * 
	 * @return Parámetros de entrada del nodo.
	 */
	public String[] getParams() {
		return this.registroActivacion.getParametros();
	}

	/**
	 * Devuelve true si el nombre del método de este nodo es igual que el del
	 * nodo pasado por parámetro.
	 * 
	 * @param nodo
	 * 
	 * @return true si son iguales, false en caso contrario.
	 */
	private boolean nombreMetodoIgual(NodoGrafoDependencia nodo) {
		return this.registroActivacion.getNombreMetodo().equals(
				nodo.registroActivacion.getNombreMetodo());
	}

	/**
	 * Devuelve true si los tipos de los parametros de entrada de este nodo, son
	 * iguales que los del nodo pasado por parámetro.
	 * 
	 * @param nodo
	 * 
	 * @return true si son iguales, false en caso contrario.
	 */
	private boolean clasesParametrosEntradaIguales(NodoGrafoDependencia nodo) {
		return this.arraysIguales(this.registroActivacion.clasesParamE(),
				nodo.registroActivacion.clasesParamE());
	}

	/**
	 * Devuelve true si los nombres de los parametros de entrada de este nodo,
	 * son iguales que los del nodo pasado por parámetro.
	 * 
	 * @param nodo
	 * 
	 * @return true si son iguales, false en caso contrario.
	 */
	private boolean nombreParametrosEntradaIguales(NodoGrafoDependencia nodo) {
		return this.arraysIguales(
				this.registroActivacion.getNombreParametros(),
				nodo.registroActivacion.getNombreParametros());
	}

	/**
	 * Devuelve true si los valores de los parametros de entrada de este nodo,
	 * son iguales que los del nodo pasado por parámetro.
	 * 
	 * @param nodo
	 * 
	 * @return true si son iguales, false en caso contrario.
	 */
	private boolean valorParametrosEntradaIguales(NodoGrafoDependencia nodo) {
		return this.arraysIguales(this.registroActivacion.getParametros(),
				nodo.registroActivacion.getParametros());
	}
	
	/**
	 * Devuelve el valor del parámetro de entrada especificado en "parametro"
	 * 		para el nodo "nodo"
	 * @param nodo	Nodo del que queremos obtener el valor del parámetro
	 * @param parametro	Parámetro del que queremos obtener el valor
	 * @return	Valor del parámetro especificado en "parametro" para el nodo "nodo"
	 */
	public String getValorParametro(NodoGrafoDependencia nodo, String parametro){
		String[] nombreParametros = nodo.registroActivacion.getNombreParametros();
		for(int i=0;i<nombreParametros.length;i++){
			if(nombreParametros[i].equals(parametro)){
				return nodo.registroActivacion.getEntradasString()[i];
			}
		}
		return new String("");
	}

	/**
	 * Permite comprar el contenido de dos arrays.
	 * 
	 * @param array1
	 * @param array2
	 * 
	 * @return true si son iguales, false en caso contrario.
	 */
	private boolean arraysIguales(String[] array1, String[] array2) {
		boolean iguales = array1.length == array2.length;
		if (iguales) {
			for (int i = 0; i < array1.length; i++) {
				if (!array1[i].equals(array2[i])) {
					iguales = false;
					break;
				}
			}
		}
		return iguales;
	}

	/**
	 * Añade una dependencia desde el nodo actual al pasado por parámetro.
	 * 
	 * @param nodo
	 */
	public void addDependencia(NodoGrafoDependencia nodo) {
		DefaultEdge arista = new DefaultEdge();
		GraphConstants.setLineEnd(arista.getAttributes(),
				GraphConstants.ARROW_CLASSIC);
		GraphConstants.setEndFill(arista.getAttributes(), true);
		GraphConstants.setSelectable(arista.getAttributes(), false);
		GraphConstants.setLineWidth(arista.getAttributes(), 2);
		GraphConstants.setLineColor(arista.getAttributes(), Color.BLACK);
		GraphConstants.setRouting(arista.getAttributes(), edgeRouter);
		GraphConstants.setLineStyle(arista.getAttributes(),
				GraphConstants.STYLE_SPLINE);
		arista.setSource(this.portCelda);
		arista.setTarget(nodo.portCelda);

		this.aristas.add(arista);
		this.dependencias.add(nodo);
	}
	
	/**
	 * Permite invertir las aristas / flechas del nodo
	 */
	public void invertirAristas(){
		for(DefaultEdge e:this.aristas){
			Object origen = e.getSource();
			Object destino = e.getTarget();
			e.setSource(destino);
			e.setTarget(origen);
		}		
	}

	/**
	 * Devuelve las celdas que modelan la representación visual del nodo como
	 * parte del grafo.
	 * 
	 * @return Celdas del nodo para el grafo.
	 */
	public List<GraphCell> obtenerCeldasDelNodoParaGrafo() {
		List<GraphCell> celdas = new ArrayList<GraphCell>();
		celdas.add(this.celdaGrafo);
		celdas.addAll(this.aristas);
		return celdas;
	}

	/**
	 * Devuelve la lista de nodos de los que depende este nodo.
	 * 
	 * @return Lista de nodos.
	 */
	public List<NodoGrafoDependencia> getDependencias() {
		return this.dependencias;
	}

	/**
	 * Determina si el nodo pasado por parámetro es dependencia de este.
	 * 
	 * @param nodoGrafoDependencia
	 * 
	 * @return true si es dependencia, false en caso contrario.
	 */
	public boolean contieneDependencia(NodoGrafoDependencia nodoGrafoDependencia) {
		boolean contiene = false;
		for (Iterator<NodoGrafoDependencia> iterator = this.dependencias
				.iterator(); iterator.hasNext();) {
			if (iterator.next().equals(nodoGrafoDependencia)) {
				contiene = true;
				break;
			}
		}
		return contiene;
	}
}
