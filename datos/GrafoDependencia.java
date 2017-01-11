package datos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;

import utilidades.MatrizDinamica;
import utilidades.NombresYPrefijos;
import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;

/**
 * Almacena y representa un grafo de dependencia.
 * 
 * @author David Pastor Herranz
 */
public class GrafoDependencia {

	private static final int ANCHO_PIXEL_CARACTER = 13;
	private static final int TAM_FUENTE = 20;

	private static final int MARGEN_TABLA = 70;
	private static final int MARGEN_INDICES = 10;
	private static final int TAMANIO_MARCADORES_EJES = 20;
	private static final int MARGEN_NODOS_ANCHURA = 50;
	private static final int MARGEN_NODOS_ALTURA = 30;

	private List<NodoGrafoDependencia> nodos;	

	private MatrizDinamica<NodoGrafoDependencia> matrizTabulado;
	private int anchuraCuadroMatriz;
	private int alturaCuadroMatriz;

	private DatosMetodoBasicos metodo;

	private boolean nodosPosicionados;

	private int numeroFilasTabla;
	private int numeroColumnasTabla;

	private Dimension tamanioRepresentacion;
	
	private String expresionParaFila;
	private String expresionParaColumna;
	private NombresYPrefijos nyp;

	/**
	 * Devuelve una nueva instancia de un grafo.
	 * 
	 * @param metodo
	 *	Método para el que obtener los nodos del grafo.
	 *            
	 * @param nyp
	 * 	Nombres y prefijos, para abreviar nombre de métodos si están visibles
	 *  y es necesario
	 */
	public GrafoDependencia(DatosMetodoBasicos metodo, NombresYPrefijos nyp) {		
		
		this.nyp = nyp;
		this.nodos = new ArrayList<NodoGrafoDependencia>();
		this.metodo = metodo;

		//	Primero añadimos los de la traza actual
		this.insertarNodos(null, Ventana.thisventana.getTraza().getRaiz(),
				new ArrayList<NodoGrafoDependencia>());
		
		//	Después añadimos el resto por si quieren visualizar grafos de métodos
		//		que no se han establecido como visibles
		this.insertarNodos(null, Ventana.thisventana.trazaCompleta.getRaiz(),
				new ArrayList<NodoGrafoDependencia>());
		
		this.crearMatrizTabuladoConOrganizacionPorDefecto();
	}

	/**
	 * Ajusta el tamaño de los nodos a la misma anchura y calcula el tamaño de
	 * los cuadros de la matriz de tabulado.
	 */
	private void ajustarNodosAMismaAnchuraYCalcularTamanioCuadro() {

		double anchuraNodoMayor = 0;
		double alturaNodoMayor = 0;
		for (NodoGrafoDependencia nodo : this.nodos) {
			if (nodo.getAnchura() > anchuraNodoMayor) {
				anchuraNodoMayor = nodo.getAnchura();
			}
			if (nodo.getAltura() > alturaNodoMayor) {
				alturaNodoMayor = nodo.getAltura();
			}
		}

		/* Establecemos la misma anchura para todos los nodos */
		for (NodoGrafoDependencia nodo : this.nodos) {
			nodo.setAnchura(anchuraNodoMayor);
		}

		this.anchuraCuadroMatriz = (int) anchuraNodoMayor
				+ MARGEN_NODOS_ANCHURA * 2;
		this.alturaCuadroMatriz = (int) alturaNodoMayor + MARGEN_NODOS_ALTURA
				* 2;
	}

	/**
	 * Inserta nodos de la ejecución en el grafo de manera recursiva.
	 * 
	 * @param padre
	 *            Nodo para el que se están resolviendo sus dependencias.
	 * @param registroActivacion
	 *            registro de activación que se esta procesando.
	 * @param procesados
	 *            Nodos que ya han sido procesados, por lo que no es necesario
	 *            determinar de nuevo sus dependencias.
	 */
	private void insertarNodos(NodoGrafoDependencia padre,
			RegistroActivacion registroActivacion,
			List<NodoGrafoDependencia> procesados) {

		/* Comprobamos si el nodo ya ha sido procesado */
		NodoGrafoDependencia nodo = new NodoGrafoDependencia(registroActivacion, this.nyp);
		boolean procesado = false;
		for (NodoGrafoDependencia visitado : procesados) {
			if (nodo.equals(visitado)) {
				procesado = true;
				nodo = visitado;
				break;
			}
		}

		/* Resolvemos las dependencias de los nodos hijos */
		boolean mismoMetodo = this.metodo.getNombre().equals(
				registroActivacion.getNombreMetodo())
				&& this.metodo.getNumParametrosE() == registroActivacion
						.getNombreParametros().length;
		if (!procesado) {
			if (mismoMetodo && !existeNodo(nodo)) {
				this.nodos.add(nodo);
			}
			for (int i = 0; i < registroActivacion.numHijos(); i++) {
				this.insertarNodos(nodo, registroActivacion.getHijo(i),
						procesados);
			}
		}

		/*
		 * Establecemos las dependencias del padre una vez resueltas las del
		 * nodo actual
		 */
		if (padre != null) {
			if (mismoMetodo) {
				padre.addDependencia(nodo);
			} else {
				for (NodoGrafoDependencia dependencia : nodo.getDependencias()) {
					padre.addDependencia(dependencia);
				}
			}
		}

		/* Establecemos el nodo como procesado si no lo estaba */
		if (!procesado) {
			procesados.add(nodo);
		}
	}
	
	/**
	 * Determina si un nodo existe en la variable nodos de esta clase
	 * (Para no repetir inserciones de nodo, permite insertar desde varias trazas)
	 * 
	 * @param nodoAInsertar Nodo que se pretende insertar
	 * @return
	 * 		True si existe el nodo a insertar, False caso contrario
	 */
	private boolean existeNodo(NodoGrafoDependencia nodoAInsertar){
		for(NodoGrafoDependencia nodoActual:this.nodos){
			if(nodoActual.equals(nodoAInsertar)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Devuelve el nodo del grafo de dependencia correspondiente a un registro
	 * de activación.
	 * 
	 * @param registroActivacion
	 *            Registro de activación.
	 * 
	 * @return Nodo que corresponde al registro de activación.
	 */
	private NodoGrafoDependencia obtenerNodo(
			RegistroActivacion registroActivacion) {
		NodoGrafoDependencia nodoObtenido = null;
		NodoGrafoDependencia nodoObjetivo = new NodoGrafoDependencia(
				registroActivacion,this.nyp);
		for (Iterator<NodoGrafoDependencia> iterator = this.nodos.iterator(); iterator
				.hasNext();) {
			NodoGrafoDependencia nodo = iterator.next();
			if (nodo.equals(nodoObjetivo)) {
				nodoObtenido = nodo;
				break;
			}
		}
		return nodoObtenido;
	}

	/**
	 * Crea una celda para la representación del grafo que representa una linea
	 * de la matriz de tabulado.
	 * 
	 * @param xInicial
	 *            posición x de origen.
	 * @param yInicial
	 *            posición y de origen.
	 * @param longitud
	 *            longitud de la linea.
	 * @param vertical
	 *            true si la linea es vertical, false si es horizontal.
	 * 
	 * @return celda para el grafo que representa la linea.
	 */
	private DefaultGraphCell crearLineaParaTabla(int xInicial, int yInicial,
			int longitud, boolean vertical) {
		DefaultGraphCell linea = new DefaultGraphCell("");
		GraphConstants.setDisconnectable(linea.getAttributes(), false);
		GraphConstants.setMoveable(linea.getAttributes(), false);
		GraphConstants.setSelectable(linea.getAttributes(), false);
		GraphConstants.setEditable(linea.getAttributes(), false);
		GraphConstants.setOpaque(linea.getAttributes(), true);
		GraphConstants.setBackground(linea.getAttributes(), Color.LIGHT_GRAY);
		GraphConstants.setBounds(linea.getAttributes(), new Rectangle(xInicial,
				yInicial, vertical ? 2 : longitud, vertical ? longitud : 2));
		return linea;
	}

	/**
	 * Crea una celda que representa un indice para la matriz de tabulado.
	 * 
	 * @param valor
	 *            indice para filas o columnas.
	 * @param fila
	 *            true si se trata de una fila, false si es una columna.
	 *            
	 * @param invertirFilas
	 * 				True si queremos orden decreciente, false si queremos orden creciente (para filas)
	 * 
	 * @param invertirColumnas
	 * 				True si queremos orden decreciente, false si queremos orden creciente (para columnas)
	 * 
	 * @return Celda para el grafo que representa el índice.
	 */
	private DefaultGraphCell crearIndiceParaTabla(int valor, boolean fila, boolean invertirFilas, boolean invertirColumnas) {
		String valorString;
		if(invertirFilas && fila){
			valorString = String.valueOf(this.numeroFilasTabla-valor-1);
		}else if(invertirColumnas && !fila){
			valorString = String.valueOf(this.numeroColumnasTabla-valor-1);
		}else{
			valorString = String.valueOf(valor);
		}
		DefaultGraphCell indice = new DefaultGraphCell(valorString);
		GraphConstants.setDisconnectable(indice.getAttributes(), false);
		GraphConstants.setMoveable(indice.getAttributes(), false);
		GraphConstants.setSelectable(indice.getAttributes(), false);
		GraphConstants.setEditable(indice.getAttributes(), false);
		GraphConstants.setFont(indice.getAttributes(), new Font("Arial",
				Font.BOLD, TAM_FUENTE));
		GraphConstants.setBackground(indice.getAttributes(), Conf.colorPanel);
		GraphConstants.setForeground(indice.getAttributes(), Color.LIGHT_GRAY);
		GraphConstants.setOpaque(indice.getAttributes(), true);

		int anchura = valorString.length() * ANCHO_PIXEL_CARACTER;
		int altura = TAM_FUENTE;

		int x = 0;
		int y = 0;
		if (fila) {
			x = MARGEN_TABLA - MARGEN_INDICES - anchura;
			y = MARGEN_TABLA + this.alturaCuadroMatriz * valor
					+ this.alturaCuadroMatriz / 2 - altura / 2;
		} else {
			x = MARGEN_TABLA + this.anchuraCuadroMatriz * valor
					+ this.anchuraCuadroMatriz / 2 - anchura / 2;
			y = MARGEN_TABLA - MARGEN_INDICES - altura;
		}
		GraphConstants.setBounds(indice.getAttributes(), new Rectangle(x, y,
				anchura, altura));

		return indice;
	}

	/**
	 * Devuelve la representación visual del grafo de dependencia.
	 * @param esTabulado Indica si se genera al pulsar "Tabular nodos del grafo" o no
	 * @return Representación visual del grafo.
	 */
	public JGraph obtenerRepresentacionGrafo(boolean esTabulado) {

		DefaultGraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,
				new DefaultCellViewFactory());
		final JGraph representacionGrafo = new JGraph(model, view);
		representacionGrafo.setMarqueeHandler(null);

		representacionGrafo.getModel().addGraphModelListener(
				new GraphModelListener() {
					@Override
					public void graphChanged(final GraphModelEvent e) {
						representacionGrafo.refreshUI();
					}
				});

		representacionGrafo
				.addGraphSelectionListener(new GraphSelectionListener() {
					@Override
					public void valueChanged(GraphSelectionEvent e) {
						if (e.isAddedCell()) {
							Object[] cells = new Object[1];
							cells[0] = e.getCell();
							representacionGrafo.getModel().toFront(cells);
							List incomingEdges = representacionGrafo
									.getGraphLayoutCache().getIncomingEdges(
											e.getCell(), null, false, true);
							List outgoingEdges = representacionGrafo
									.getGraphLayoutCache().getOutgoingEdges(
											e.getCell(), null, false, true);
							representacionGrafo.getModel().toFront(
									incomingEdges.toArray());
							representacionGrafo.getModel().toFront(
									outgoingEdges.toArray());
						}
					}
				});

		representacionGrafo.setBackground(Conf.colorPanel);
		this.ajustarNodosAMismaAnchuraYCalcularTamanioCuadro();

		boolean dibujarTabla = this.numeroFilasTabla >= 1
				&& this.numeroColumnasTabla >= 1;
		boolean[] invertirEjes = this.invertirEjes();
		if (dibujarTabla) {			
			int tamanioMarcadorEjesParaFila = this.numeroFilasTabla > 1 ? TAMANIO_MARCADORES_EJES
					: 0;
			for (int fila = 0; fila <= this.numeroFilasTabla; fila++) {
				DefaultGraphCell linea = this.crearLineaParaTabla(MARGEN_TABLA
						- tamanioMarcadorEjesParaFila, MARGEN_TABLA + fila
						* this.alturaCuadroMatriz, this.numeroColumnasTabla
						* this.anchuraCuadroMatriz
						+ tamanioMarcadorEjesParaFila, false);
				representacionGrafo.getGraphLayoutCache().insert(linea);
				if (fila != this.numeroFilasTabla && this.numeroFilasTabla > 1) {
					DefaultGraphCell indice = this.crearIndiceParaTabla(fila,
							true, invertirEjes[0],invertirEjes[1]);
					representacionGrafo.getGraphLayoutCache().insert(indice);
				}
			}
			int tamanioMarcadorEjesParaColumna = this.numeroColumnasTabla > 1 ? TAMANIO_MARCADORES_EJES
					: 0;
			for (int columna = 0; columna <= this.numeroColumnasTabla; columna++) {
				DefaultGraphCell linea = this.crearLineaParaTabla(MARGEN_TABLA
						+ columna * this.anchuraCuadroMatriz, MARGEN_TABLA
						- tamanioMarcadorEjesParaColumna, this.numeroFilasTabla
						* this.alturaCuadroMatriz
						+ tamanioMarcadorEjesParaColumna, true);
				representacionGrafo.getGraphLayoutCache().insert(linea);
				if (columna != this.numeroColumnasTabla
						&& (this.numeroColumnasTabla > 1 || this.numeroColumnasTabla == 1
								&& this.numeroFilasTabla == 1)) {
					DefaultGraphCell indice = this.crearIndiceParaTabla(
							columna, false, invertirEjes[0],invertirEjes[1]);
					representacionGrafo.getGraphLayoutCache().insert(indice);
				}
			}
			
			if(this.expresionParaFila!=null && this.expresionParaColumna!=null){
				this.insertarEjesTabularGrafo(representacionGrafo);
			}			
			
		}		
		
		if (!this.nodosPosicionados) {
			if(esTabulado){
				this.posicionarNodosSegunTabulado(invertirEjes[0],invertirEjes[1]);
			}else{
				this.posicionarNodosSegunTabulado(false,false);
			}
			this.nodosPosicionados = true;
		}

		for (NodoGrafoDependencia nodo : this.nodos) {
			representacionGrafo.getGraphLayoutCache().insert(
					nodo.obtenerCeldasDelNodoParaGrafo().toArray());
		}

		int numColumnas = this.matrizTabulado.numColumnas();
		if (dibujarTabla
				&& this.numeroColumnasTabla > this.matrizTabulado.numColumnas()) {
			numColumnas = this.numeroColumnasTabla;
		}

		int numFilas = this.matrizTabulado.numFilas();
		if (dibujarTabla
				&& this.numeroFilasTabla > this.matrizTabulado.numFilas()) {
			numFilas = this.numeroFilasTabla;
		}

		int anchuraTotal = numColumnas * this.anchuraCuadroMatriz
				+ MARGEN_TABLA * 2;
		int alturaTotal = numFilas * this.alturaCuadroMatriz + MARGEN_TABLA * 2;
		this.tamanioRepresentacion = new Dimension(anchuraTotal, alturaTotal);

		for (CellView cell : representacionGrafo.getGraphLayoutCache()
				.getAllViews()) {
			cell.update(representacionGrafo.getGraphLayoutCache());
		}

		return representacionGrafo;
	}

	/**
	 * Inserta los ejes con el nombre de la expresión para filas y columnas
	 * 		establecida por el usuario
	 * @param representacionGrafo Grafo donde vamos a insertarlo
	 */
	private void insertarEjesTabularGrafo(JGraph representacionGrafo){
		//	Líneas invisibles para unir los títulos de las etiquetas de filas y columnas
	
		DefaultGraphCell lineaX1 = new DefaultGraphCell("");
		GraphConstants.setDisconnectable(lineaX1.getAttributes(), false);
		GraphConstants.setMoveable(lineaX1.getAttributes(), false);
		GraphConstants.setSelectable(lineaX1.getAttributes(), false);
		GraphConstants.setEditable(lineaX1.getAttributes(), false);
		GraphConstants.setOpaque(lineaX1.getAttributes(), false);
		GraphConstants.setBounds(lineaX1.getAttributes(), new Rectangle(GrafoDependencia.MARGEN_TABLA, 0,
				2,GrafoDependencia.MARGEN_TABLA/2));
		DefaultPort port0 = new DefaultPort();
		lineaX1.add(port0);			
		
		DefaultGraphCell lineaX2 = new DefaultGraphCell("");
		GraphConstants.setDisconnectable(lineaX2.getAttributes(), false);
		GraphConstants.setMoveable(lineaX2.getAttributes(), false);
		GraphConstants.setSelectable(lineaX2.getAttributes(), false);
		GraphConstants.setEditable(lineaX2.getAttributes(), false);
		GraphConstants.setOpaque(lineaX2.getAttributes(), false);
		
		//	Corrección para cuando texto es mas ancho que el ancho de la tabla
		String textoX = "";
		if(this.expresionParaColumna != null && !this.expresionParaColumna.equals("")){
			textoX = "Valores de "+this.expresionParaColumna;
		}		
		int textoLongitudX = ANCHO_PIXEL_CARACTER*textoX.length();
		int limiteTextoX = GrafoDependencia.MARGEN_TABLA+this.getNumeroColumnasTabla()*this.anchuraCuadroMatriz;
		if(textoLongitudX>limiteTextoX){
			limiteTextoX = textoLongitudX +1;		
		}
		
		GraphConstants.setBounds(lineaX2.getAttributes(), new Rectangle(limiteTextoX, 0,
				2,GrafoDependencia.MARGEN_TABLA/2));
		DefaultPort port1 = new DefaultPort();
		lineaX2.add(port1);			
		
		DefaultGraphCell lineaY1 = new DefaultGraphCell("");
		GraphConstants.setDisconnectable(lineaY1.getAttributes(), false);
		GraphConstants.setMoveable(lineaY1.getAttributes(), false);
		GraphConstants.setSelectable(lineaY1.getAttributes(), false);
		GraphConstants.setEditable(lineaY1.getAttributes(), false);
		GraphConstants.setOpaque(lineaY1.getAttributes(), false);
		GraphConstants.setBounds(lineaY1.getAttributes(), new Rectangle(1,GrafoDependencia.MARGEN_TABLA,
				GrafoDependencia.MARGEN_TABLA/2,2));
		DefaultPort port2 = new DefaultPort();
		lineaY1.add(port2);			
		
		DefaultGraphCell lineaY2 = new DefaultGraphCell("");
		GraphConstants.setDisconnectable(lineaY2.getAttributes(), false);
		GraphConstants.setMoveable(lineaY2.getAttributes(), false);
		GraphConstants.setSelectable(lineaY2.getAttributes(), false);
		GraphConstants.setEditable(lineaY2.getAttributes(), false);
		GraphConstants.setOpaque(lineaY2.getAttributes(), false);
		
		//	Corrección para cuando texto es mas alto que el alto de la tabla
		String textoY = "";
		if(this.expresionParaFila != null && !this.expresionParaFila.equals("")){
			textoY = "Valores de "+this.expresionParaFila;
		}
		int textoLongitudY = ANCHO_PIXEL_CARACTER*textoY.length();
		int limiteTextoY = GrafoDependencia.MARGEN_TABLA+this.getNumeroFilasTabla()*this.alturaCuadroMatriz;
		if(textoLongitudY>limiteTextoY){
			limiteTextoY = textoLongitudY +1;		
		}
		
		GraphConstants.setBounds(lineaY2.getAttributes(), new Rectangle(0,limiteTextoY,
				GrafoDependencia.MARGEN_TABLA/2,2));
		
		DefaultPort port3 = new DefaultPort();
		lineaY2.add(port3);			
		
		//	Títulos para cada eje
		
		DefaultEdge edgeX = new DefaultEdge(new String(textoX));
		edgeX.setSource(lineaX1.getChildAt(0));
		edgeX.setTarget(lineaX2.getChildAt(0));
		GraphConstants.setEndFill(edgeX.getAttributes(), true);
		GraphConstants.setLabelAlongEdge(edgeX.getAttributes(), true);
		GraphConstants.setMoveable(edgeX.getAttributes(), false);
		GraphConstants.setLineEnd(edgeX.getAttributes(), GraphConstants.ARROW_TECHNICAL);
		GraphConstants.setFont(edgeX.getAttributes(), new Font("Arial",
				Font.BOLD, TAM_FUENTE));			
		GraphConstants.setLineColor(edgeX.getAttributes(), Color.WHITE);
		GraphConstants.setSelectable(edgeX.getAttributes(), false);
		GraphConstants.setEditable(edgeX.getAttributes(), false);
		GraphConstants.setForeground(edgeX.getAttributes(), Color.LIGHT_GRAY);
		
		DefaultEdge edgeY = new DefaultEdge(textoY);
		edgeY.setSource(lineaY1.getChildAt(0));
		edgeY.setTarget(lineaY2.getChildAt(0));
		GraphConstants.setEndFill(edgeY.getAttributes(), true);
		GraphConstants.setLabelAlongEdge(edgeY.getAttributes(), true);
		GraphConstants.setMoveable(edgeY.getAttributes(), false);
		GraphConstants.setLineEnd(edgeY.getAttributes(), GraphConstants.ARROW_TECHNICAL);
		GraphConstants.setFont(edgeY.getAttributes(), new Font("Arial",
				Font.BOLD, TAM_FUENTE));
		GraphConstants.setLineColor(edgeY.getAttributes(), Color.WHITE);
		GraphConstants.setSelectable(edgeY.getAttributes(), false);
		GraphConstants.setEditable(edgeY.getAttributes(), false);
		GraphConstants.setForeground(edgeY.getAttributes(), Color.LIGHT_GRAY);
		
		
		
		//	Añadimos elementos
		representacionGrafo.getGraphLayoutCache().insert(lineaX1);
		representacionGrafo.getGraphLayoutCache().insert(lineaX2);
		representacionGrafo.getGraphLayoutCache().insert(lineaY1);
		representacionGrafo.getGraphLayoutCache().insert(lineaY2);
		representacionGrafo.getGraphLayoutCache().insert(edgeX);
		representacionGrafo.getGraphLayoutCache().insert(edgeY);
	}
		
	/**
	 * Devuelve el número de filas especificado para la matriz de tabulado.
	 * 
	 * @return Número de filas especificado para la matriz de tabulado.
	 */
	public int getNumeroFilasTabla() {
		return this.numeroFilasTabla;
	}

	/**
	 * Devuelve el número de columnas especificado para la matriz de tabulado.
	 * 
	 * @return Número de columnas especificado para la matriz de tabulado.
	 */
	public int getNumeroColumnasTabla() {
		return this.numeroColumnasTabla;
	}

	/**
	 * Devuelve el tamaño de la representación visual del grafo.
	 * 
	 * @return Tamaño de la representación visual del grafo.
	 */
	public Dimension getTamanioRepresentacion() {
		return this.tamanioRepresentacion;
	}

	/**
	 * Crea una matriz de tabulado con una organización por defecto.
	 */
	private void crearMatrizTabuladoConOrganizacionPorDefecto() {
		this.matrizTabulado = new MatrizDinamica<NodoGrafoDependencia>();
		if (this.nodos.size() > 0) {
			NodoGrafoDependencia raiz = this.nodos.get(0);
			this.aniadirDependenciasAMatriz(this.matrizTabulado, raiz);
		}
	}

	/**
	 * Posiciona los nodos del grafo según lo que especifique la matriz de
	 * tabulado actual.
	 * 
	 * @param invertirFilas False = Orden creciente, True = Orden decreciente para filas
	 * @param invertirColumnas False = Orden creciente, True = Orden decreciente para columnas
	 */
	private void posicionarNodosSegunTabulado(boolean invertirFilas, boolean invertirColumnas) {
		for (int fila = 0; fila < this.matrizTabulado.numFilas(); fila++) {
			for (int columna = 0; columna < this.matrizTabulado.numColumnas(); columna++) {
				NodoGrafoDependencia nodo = this.matrizTabulado.get(fila,
						columna);
				if (nodo != null) {
					int x,y;
					
					if(invertirFilas){
						y = MARGEN_TABLA
								+ MARGEN_NODOS_ALTURA + (this.matrizTabulado.numFilas()-1-fila)
								* this.alturaCuadroMatriz;
					}else{
						y = MARGEN_TABLA
								+ MARGEN_NODOS_ALTURA + fila
								* this.alturaCuadroMatriz;
					}
					
					if(invertirColumnas){
						x = MARGEN_TABLA + MARGEN_NODOS_ANCHURA
								+ (this.matrizTabulado.numColumnas()-1-columna) * this.anchuraCuadroMatriz;
					}else{
						x = MARGEN_TABLA + MARGEN_NODOS_ANCHURA
								+ columna * this.anchuraCuadroMatriz;
					}
					
					nodo.setPosicion(x, y);					
				}
			}
		}
	}

	/**
	 * Establece el tamaño de la matriz de tabulado que se dibujará detrás del
	 * grafo.
	 * 
	 * @param numeroFilasTabla
	 *            Número de filas de la tabla.
	 * @param numeroColumnasTabla
	 *            Número de columnas de la tabla.
	 */
	public void setTamanioTabla(int numeroFilasTabla, int numeroColumnasTabla) {
		this.numeroFilasTabla = numeroFilasTabla;
		this.numeroColumnasTabla = numeroColumnasTabla;
	}
	
	private Object obtenerValorConTipo(String valorAParsear) {
		
		Object valor = null;
		String paramString = valorAParsear.trim();
		
		try {
			valor = Integer.parseInt(paramString);
		} catch (NumberFormatException formatException) {
		}

		if (valor == null) {
			try {
				valor = Long.parseLong(paramString);
			} catch (NumberFormatException formatException) {
			}
		}

		if (valor == null) {
			try {
				valor = Double.parseDouble(paramString);
			} catch (NumberFormatException formatException) {
			}
		}

		if (valor == null
				&& (paramString.startsWith("'") && paramString
						.endsWith("'") && paramString.length() > 1)) {
			valor = paramString.charAt(1);
		}

		if (valor == null
				&& (paramString.startsWith("\"") && paramString
						.endsWith("\"") && paramString.length() > 1)) {
			valor = paramString.substring(1, paramString.length() - 1);
		}

		if (valor == null
				&& (paramString.equals("true") || paramString
						.equals("false"))) {
			valor = Boolean.parseBoolean(paramString);
		}

		/* Listas */
		if (valor == null && paramString.startsWith("{")
				&& paramString.endsWith("}") && paramString.length() > 1) {

			String listaString = paramString.substring(1, paramString.length() - 1).trim();
			List<String> valoresStringLista = ParametrosParser.reemplazarYPartirValores(listaString);
			
			Class claseLista = Object.class;
			if (valoresStringLista.size() > 0) {			
				Class tipoElementos = this.obtenerValorConTipo(valoresStringLista.get(0)).getClass();			
				if (tipoElementos.equals(Integer.class)) {
					claseLista = int.class;
				} else if (tipoElementos.equals(Long.class)) {
					claseLista = long.class;
				} else if (tipoElementos.equals(Double.class)) {
					claseLista = double.class;
				} else if (tipoElementos.equals(Character.class)) {
					claseLista = char.class;
				} else if (tipoElementos.equals(String.class)) {
					claseLista = String.class;
				} else if (tipoElementos.equals(Boolean.class)) {
					claseLista = Boolean.class;
				}
			}
			
			valor = Array.newInstance(claseLista, valoresStringLista.size());
			for (int i = 0; i < valoresStringLista.size(); i++) {
				Array.set(valor, i, this.obtenerValorConTipo(valoresStringLista.get(i)));
			}
		}

		if (valor == null) {
			valor = paramString;
		}
		
		return valor;
	}
	
	/**
	 * Tabula automáticamente los nodos del grafo, dada una expresión para filas
	 * y otra para columnas.
	 * 
	 * @param expresionParaFila
	 * @param expresionParaColumna
	 * 
	 * @return Mensaje de error si ocurrió algun error, null en caso contrario.
	 */
	public String tabular(String expresionParaFila, String expresionParaColumna) {

		String mensajeError = null;
		ScriptEngineManager manager = new ScriptEngineManager();
		MatrizDinamica<NodoGrafoDependencia> matriz = new MatrizDinamica<NodoGrafoDependencia>();
		this.expresionParaFila = expresionParaFila;
		this.expresionParaColumna = expresionParaColumna;
		
		for (NodoGrafoDependencia nodo : this.nodos) {
			ScriptEngine engine = manager.getEngineByName("js");
			for (int i = 0; i < this.metodo.getNumParametrosE(); i++) {
				engine.put(this.metodo.getNombreParametroE(i), this.obtenerValorConTipo(nodo.getParams()[i]));
			}
			boolean filaEvaluada = false;
			try {
				int fila = this.obtenerValorEnteroDeEvaluacion(engine
						.eval(expresionParaFila));
				filaEvaluada = true;
				int columna = this.obtenerValorEnteroDeEvaluacion(engine
						.eval(expresionParaColumna));
				if (fila < 0 || columna < 0) {
					mensajeError = Texto.get("GP_EXPR_NEGATIVOS", Conf.idioma);
				} else if (matriz.get(fila, columna) != null) {
					mensajeError = Texto
							.get("GP_ERROR_POSICIONAR", Conf.idioma)
							+ " x="
							+ columna + ", y=" + fila;
				} else {
					matriz.set(fila, columna, nodo);
				}
			} catch (ScriptException e) {
				String expresion = filaEvaluada ? expresionParaColumna : expresionParaFila;
				mensajeError = "<html><p align=\"center\">" + Texto.get("GP_EXPR_INVALIDA", Conf.idioma) + " \"" + expresion +
						"\" " + Texto.get("GP_EXPR_INVALIDA_2", Conf.idioma) + "</p><p align=\"center\">" + e.getMessage() +
						"</p></html>";
			}
		}

		if (mensajeError == null) {
			this.matrizTabulado = matriz;
			this.setTamanioTabla(matriz.numFilas(), matriz.numColumnas());
			this.nodosPosicionados = false;
		}

		return mensajeError;
	}

	/**
	 * Transforma el valor devuelto por el motor de expresiones en un entero
	 * para tabular.
	 * 
	 * @param valorEval
	 *            Objeto devuelto por el motor de expresiones.
	 * 
	 * @return posición resultante, -1 si el resultado de la evaluación no es un
	 *         entero válido.
	 */
	private int obtenerValorEnteroDeEvaluacion(Object valorEval) {
		int valor = -1;
		if (valorEval == null) {
			valor = 0;
		} else if (valorEval instanceof Integer) {
			valor = ((Integer) valorEval).intValue();
		} else if (valorEval instanceof Long) {
			valor = ((Long) valorEval).intValue();
		} else if (valorEval instanceof Double) {
			valor = ((Double) valorEval).intValue();
		} else if (valorEval instanceof Float) {
			valor = ((Float) valorEval).intValue();
		} else if (valorEval instanceof String) {
			String valorFilaString = (String) valorEval;
			
			try {
				valor = Integer.parseInt(valorFilaString);
			} catch (NumberFormatException formatException) {
			}
			
			if (valor < 0) {
				try {
					valor = Double.valueOf(Double.parseDouble(valorFilaString)).intValue();
				} catch (NumberFormatException formatException) {
				}
			}
		}

		return valor;
	}

	/**
	 * Organiza, con una organización por defecto, los nodos de dependencia en
	 * la matriz.
	 * 
	 * @param matriz
	 *            Matriz de tabulado.
	 * @param raiz
	 *            Nodo raiz del grafo.
	 */
	private void aniadirDependenciasAMatriz(
			MatrizDinamica<NodoGrafoDependencia> matriz,
			NodoGrafoDependencia raiz) {

		/* Insertamos el primer nodo, y lo añadimos a la cola */
		matriz.set(0, 0, raiz);
		LinkedList<NodoGrafoDependencia> colaDependencias = new LinkedList<NodoGrafoDependencia>();
		colaDependencias.add(raiz);

		/*
		 * Segun obtenemos dependencias, las añadimos a la cola para ir
		 * procesándolas en anchura
		 */
		while (!colaDependencias.isEmpty()) {
			NodoGrafoDependencia nodo = colaDependencias.remove();
			int[] posicionNodo = matriz.getPosicion(nodo);
			int fila = posicionNodo[0];
			int columna = posicionNodo[1];
			for (NodoGrafoDependencia dependencia : nodo.getDependencias()) {
				if (!matriz.contiene(dependencia)) {
					int[] posicion = this.encontrarPosicionMasCercanaLibre(
							matriz, fila, columna);
					matriz.set(posicion[0], posicion[1], dependencia);
					colaDependencias.add(dependencia);
				}
			}

			/*
			 * Buscamos entre los nodos por si alguno no depende del nodo raiz,
			 * esto puede darse cuando se desea obtener las dependencias de los
			 * métodos auxiliares.
			 */
			if (colaDependencias.isEmpty()) {
				for (NodoGrafoDependencia nodoGrafo : this.nodos) {
					if (!matriz.contiene(nodoGrafo)) {
						int[] posicion = this.encontrarPosicionMasCercanaLibre(
								matriz, 0, 0);
						matriz.set(posicion[0], posicion[1], nodoGrafo);
						colaDependencias.add(nodoGrafo);
						break;
					}
				}
			}
		}
	}

	/**
	 * Determina cual es la posición más cercana libre en la matriz.
	 * 
	 * @param matriz
	 *            Matriz.
	 * @param fila
	 *            Fila actual
	 * @param columna
	 *            Columna actual
	 * 
	 * @return [fila, columna] con la posición más cercana y libre.
	 */
	private int[] encontrarPosicionMasCercanaLibre(
			MatrizDinamica<NodoGrafoDependencia> matriz, int fila, int columna) {
		int[] posicion = new int[2];
		boolean huecoEncontrado = false;
		int distancia = 0;
		while (!huecoEncontrado) {
			distancia++;

			if (!huecoEncontrado) {
				for (int i = fila; i < fila + distancia; i++) {
					if (matriz.get(i, columna + distancia) == null) {
						posicion[0] = i;
						posicion[1] = columna + distancia;
						huecoEncontrado = true;
					}
				}
			}

			if (!huecoEncontrado) {
				for (int i = columna; i < columna + distancia; i++) {
					if (matriz.get(fila + distancia, i) == null) {
						posicion[0] = fila + distancia;
						posicion[1] = i;
						huecoEncontrado = true;
					}
				}
			}

			if (!huecoEncontrado) {
				if (matriz.get(fila + distancia, columna + distancia) == null) {
					posicion[0] = fila + distancia;
					posicion[1] = columna + distancia;
					huecoEncontrado = true;
				}
			}
		}
		return posicion;
	}
	
	/**
	 * Heurística que intenta obtener la mejor representación del grafo tabulado automáticamente
	 * 
	 * @return Array de booleanos donde la primera posición indica si las filas deben estar
	 * 		en orden creciente (true) o en orden decreciente(false) y la segunda posición 
	 * 		indica si las columnas deben estar en orden creciente (true) o en orden decreciente(false)
	 */
	private boolean[] invertirEjes(){
		//	Miramos si es necesario invertir los ejes de la fila y/o de las columnas
		NodoGrafoDependencia raiz = this.nodos.get(0);
		String nombreFilaPadre = this.expresionParaFila;
		String nombreColumnaPadre = this.expresionParaColumna;
		String valorFilaPadre = raiz.getValorParametro(raiz, nombreFilaPadre);
		String valorColumnaPadre = raiz.getValorParametro(raiz,	nombreColumnaPadre);
		List<NodoGrafoDependencia> listaHijos = raiz.getDependencias();
		boolean[] retorno = new boolean[2];
		retorno[0] = false;
		retorno[1] = false;
		for(NodoGrafoDependencia nodoHijo:listaHijos){
			String valorFilaHijo = raiz.getValorParametro(nodoHijo, nombreFilaPadre);
			String valorColumnaHijo = raiz.getValorParametro(nodoHijo,	nombreColumnaPadre);
			//	Si el valor de la fila del hijo de la raiz es menor 
			//	implica que las filas van en orden decreciente, invertimos
			try{
				if(Integer.parseInt(valorFilaHijo)<Integer.parseInt(valorFilaPadre)){
					retorno[0] = true;
				}
				if(Integer.parseInt(valorColumnaHijo)<Integer.parseInt(valorColumnaPadre)){
					retorno[1] = true;
				}
			}catch(Exception e){
				continue;
			}
		}
		return retorno;
	}
	
	/**
	 * Obtiene una lista inmodificable de los nodos, para cambiar
	 * 		la orientación de las aristas
	 * @return Lista inmodificable de nodos del grafo
	 */
	public List<NodoGrafoDependencia> getNodos() {
		return Collections.unmodifiableList(nodos);
	}
}
