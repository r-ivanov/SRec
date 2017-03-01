package datos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
 * @author David Pastor Herranz y Daniel Arroyo Cortés
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
	
	private boolean eliminarFilasColumnas = false;
	private int numeroFilasVisibles;
	private int numeroColumnasVisibles;
	
	//	Multiples métodos
	
	private boolean esGrafoDeUnMetodo = true;			//	true = solo se representa un método
														//	false = se representan varios métodos	
	private List<DatosMetodoBasicos> metodos;			//	Métodos en caso de tener varios
	private String ultimaExpresionMultiplesMetodos;		//	Expresión si han tabulado multiples métodos
	private boolean esExpresionDeFila;					//	True - La expresión es de filas, False - Caso contrario
	private List<Integer> parametrosComunes;			//	Lista de índices de parámetros comunes respecto al primer método
	private List<String> parametrosComunesS;			//	Lista de nombres de los parámetros comunes respecto al primer método

	/**
	 * Devuelve una nueva instancia de un grafo.
	 * 
	 * @param metodo
	 *		Método para el que obtener los nodos del grafo.
	 *            
	 * @param nyp
	 * 		Nombres y prefijos, para abreviar nombre de métodos si están visibles
	 *  	y es necesario
	 */
	public GrafoDependencia(DatosMetodoBasicos metodo, NombresYPrefijos nyp) {		
		
		this.esGrafoDeUnMetodo = true;
		this.nyp = nyp;
		this.nodos = new ArrayList<NodoGrafoDependencia>();
		this.metodo = metodo;

		//	Primero añadimos los de la traza actual
		this.insertarNodos(null, Ventana.thisventana.getTraza().getRaiz(),
				new ArrayList<NodoGrafoDependencia>());
		
		//	Después añadimos el resto por si quieren visualizar grafos de métodos
		//		que no pertenecen a la traza actual
		this.insertarNodos(null, Ventana.thisventana.trazaCompleta.getRaiz(),
				new ArrayList<NodoGrafoDependencia>());
		
		this.crearMatrizTabuladoConOrganizacionPorDefecto();
	}
	
	/**
	 * Devuelve una nueva instancia de un grafo.
	 * 
	 * @param metodo
	 *		Métodos para los que obtener los nodos del grafo.
	 *            
	 * @param nyp
	 * 		Nombres y prefijos, para abreviar nombre de métodos si están visibles
	 *  	y es necesario
	 */
	public GrafoDependencia(List<DatosMetodoBasicos> metodo, NombresYPrefijos nyp) {		
		
		this.esGrafoDeUnMetodo = false;
		this.nyp = nyp;
		this.nodos = new ArrayList<NodoGrafoDependencia>();
		this.metodos = metodo;
		
		//	Primero añadimos los de la traza actual
		this.insertarNodosMultiplesMetodos(null, Ventana.thisventana.getTraza().getRaiz(),
				new ArrayList<NodoGrafoDependencia>(),metodo);
		
		//	Después añadimos el resto por si quieren visualizar grafos de métodos
		//		que no pertenecen a la traza actual
		this.insertarNodosMultiplesMetodos(null, Ventana.thisventana.trazaCompleta.getRaiz(),
				new ArrayList<NodoGrafoDependencia>(),metodo);
		
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
				+ Conf.sepH * 2;
		this.alturaCuadroMatriz = (int) alturaNodoMayor + Conf.sepV
				* 2;
	}

	/**
	 * Inserta nodos de la ejecución en el grafo de manera recursiva.
	 * 
	 * @param padre
	 * 		Nodo para el que se están resolviendo sus dependencias.
	 * 
	 * @param registroActivacion
	 * 		Registro de activación que se esta procesando.
	 * 
	 * @param procesados
	 *		Nodos que ya han sido procesados, por lo que no es necesario
	 *		determinar de nuevo sus dependencias.
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
	 * Permite saber si un nombre de método está en una lista de DatosMetodoBasicos
	 * 
	 * @param metodos Lista de DatosMetodoBasicos donde buscar
	 * @param metodo  Nombre del método a buscar
	 * 
	 * @return True si metodo está en metodos, false caso contrario
	 */
	private boolean metodoDentroLista(List<DatosMetodoBasicos> metodos,String metodo){
		for(DatosMetodoBasicos dmb:metodos){
			if(dmb.getNombre().equals(metodo)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Inserta nodos de la ejecución en el grafo de manera recursiva.
	 * 
	 * @param padre
	 * 		Nodo para el que se están resolviendo sus dependencias.
	 * 
	 * @param registroActivacion
	 * 		Registro de activación que se esta procesando.
	 * 
	 * @param procesados
	 *		Nodos que ya han sido procesados, por lo que no es necesario
	 *		determinar de nuevo sus dependencias.
	 *
	 * @param metodo
	 *		Lista de métodos de los que queremos generar el grafo de dependencia
	 */
	private void insertarNodosMultiplesMetodos(NodoGrafoDependencia padre,
			RegistroActivacion registroActivacion,
			List<NodoGrafoDependencia> procesados,
			List<DatosMetodoBasicos> metodo) {
		
		//	Solo se inserta si pertenece a uno de los métodos seleccionados	
		boolean insertar = this.metodoDentroLista(metodo, registroActivacion.getNombreMetodo());		
		
		// Comprobamos si el nodo ya ha sido procesado
		NodoGrafoDependencia nodo = new NodoGrafoDependencia(registroActivacion, this.nyp);
		boolean procesado = false;		
		
		for (NodoGrafoDependencia visitado : procesados) {
			if (nodo.equals(visitado)) {
				procesado = true;
				nodo = visitado;
				break;
			}
		}

		//	Resolvemos las dependencias de sus hijos		
		if (!procesado) {
			if (insertar && !existeNodo(nodo)) {
				this.nodos.add(nodo);
			}
			for (int i = 0; i < registroActivacion.numHijos(); i++) {							
				this.insertarNodosMultiplesMetodos(nodo, registroActivacion.getHijo(i),
						procesados,metodo);
			}
		}

		/*
		 * Establecemos las dependencias del padre una vez resueltas las del
		 * nodo actual
		 */
		if (padre != null) {
			
			//	Si este nodo pertenece a un método de los seleccionados creamos
			//		dependencia del padre a él
			if(insertar)
				padre.addDependencia(nodo);
			
			//	Si este nodo NO pertenece a un método de los seleccionados 
			//	creamos una dependencia del padre a los primeros nodos que si haya que insertar
			else{
				
				Queue<NodoGrafoDependencia> colaNodos = new LinkedList<NodoGrafoDependencia>();
				NodoGrafoDependencia nodoActual = nodo;
				for(NodoGrafoDependencia hijo:nodoActual.getDependencias())
					colaNodos.add(hijo);
				
				//	Bucle para recorrer cola
				do{
					//	Nodo actual es la cabecera de la cola
					nodoActual = colaNodos.poll();
					if(nodoActual == null)
						break;
					
					//	Si el nodo actual es un método de los seleccionados por el usuario 
					//		creamos dependencia de su padre a Él
					if(this.metodoDentroLista(metodo, nodoActual.getMetodo())){
						padre.addDependencia(nodoActual);
					}
					
					//	Si el nodo actual NO es un método de los seleccionados por el usuario 
					//		insertamos sus hijos a la cola
					else{
						for(NodoGrafoDependencia hijo:nodoActual.getDependencias())
							colaNodos.add(hijo);
					}	
				}while(colaNodos.size()>0);
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
	 * @param nodoAInsertar 
	 * 		Nodo que se pretende insertar
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
	 *      Registro de activación.
	 * 
	 * @return 
	 * 		Nodo que corresponde al registro de activación.
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
	 * Crea una celda que representa un indice para la matriz de tabulado
	 * 	si no se han eliminado filas y columnas.
	 * 
	 * @param valor
	 * 		indice para filas o columnas.
	 * 
	 * @param fila
	 *		true si se trata de una fila, false si es una columna.
	 *            
	 * @param invertirFilas
	 *		True si queremos orden decreciente, false si queremos orden creciente (para filas)
	 * 
	 * @param invertirColumnas
	 * 		True si queremos orden decreciente, false si queremos orden creciente (para columnas)
	 * 
	 * @return 
	 * 		Celda para el grafo que representa el índice.
	 */
	private DefaultGraphCell crearIndiceParaTabla(int valor, boolean fila, boolean invertirFilas, boolean invertirColumnas) {
		String valorString="";
		
		if(this.esGrafoDeUnMetodo && fila && invertirFilas){
			valorString = String.valueOf(this.numeroFilasTabla-valor-1);
			
		}else if(this.esGrafoDeUnMetodo && fila && !invertirFilas){
			valorString = String.valueOf(valor);
			
		}else if(this.esGrafoDeUnMetodo && !fila && invertirColumnas){
			valorString = String.valueOf(this.numeroColumnasTabla-valor-1);
			
		}else if(this.esGrafoDeUnMetodo && !fila && !invertirColumnas){
			valorString = String.valueOf(valor);
			
		}else if(!this.esGrafoDeUnMetodo && fila && invertirFilas && this.esExpresionDeFila){
			valorString = String.valueOf(this.numeroFilasTabla-valor-1);
			
		}else if(!this.esGrafoDeUnMetodo && fila && invertirFilas && !this.esExpresionDeFila){
			valorString = String.valueOf(this.nyp.getPrefijo(this.metodos.get(this.numeroFilasTabla-valor-1).getNombre()));
		
		}else if(!this.esGrafoDeUnMetodo && fila && !invertirFilas && this.esExpresionDeFila){
			valorString = String.valueOf(valor);
			
		}else if(!this.esGrafoDeUnMetodo && fila && !invertirFilas && !this.esExpresionDeFila){
			valorString = String.valueOf(this.nyp.getPrefijo(this.metodos.get(valor).getNombre()));
			
		}else if(!this.esGrafoDeUnMetodo && !fila && invertirColumnas && this.esExpresionDeFila){
			valorString = String.valueOf(this.nyp.getPrefijo(this.metodos.get(this.numeroColumnasTabla-valor-1).getNombre()));
		
		}else if(!this.esGrafoDeUnMetodo && !fila && invertirColumnas && !this.esExpresionDeFila){
			valorString = String.valueOf(this.numeroColumnasTabla-valor-1);
		
		}else if(!this.esGrafoDeUnMetodo && !fila && !invertirColumnas && this.esExpresionDeFila){
			valorString = String.valueOf(this.nyp.getPrefijo(this.metodos.get(valor).getNombre()));
		
		}else if(!this.esGrafoDeUnMetodo && !fila && !invertirColumnas && !this.esExpresionDeFila){
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
	 * Crea una celda que representa un indice para la matriz 
	 * 	de tabulado si se han eliminado filas y columnas
	 * 
	 * @param valorImprimir
	 *		Valor que queremos imprimir, pero no determina el lugar
	 *            
	 * @param valorReal
	 * 		Valor que determina el lugar donde se imprimirá
	 * 
	 * @param fila
	 *		true si se trata de una fila, false si es una columna.
	 *            
	 * @param invertirFilas
	 *		True si queremos orden decreciente, false si queremos orden creciente (para filas)
	 * 
	 * @param invertirColumnas
	 *		True si queremos orden decreciente, false si queremos orden creciente (para columnas)
	 *
	 * @param numeroFilasVisibles
	 * 		Número de filas visibles en la matriz al eliminar filas
	 * 
	 * @param numeroColumnasVisibles
	 * 		Número de columnas visibles en la matriz al eliminar filas
	 * 
	 * @return 
	 * 		Celda para el grafo que representa el índice.
	 */
	private DefaultGraphCell crearIndiceParaTabla2(int valorImprimir, int valorReal,
			boolean fila, boolean invertirFilas, boolean invertirColumnas,
			int numeroFilasVisibles, int numeroColumnasVisibles) {
		String valorString;
		if(invertirFilas && fila){
			valorString = String.valueOf(this.numeroFilasTabla-valorImprimir-1);
		}else if(invertirColumnas && !fila){
			valorString = String.valueOf(this.numeroColumnasTabla-valorImprimir-1);
		}else{
			valorString = String.valueOf(valorReal);
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
			y = MARGEN_TABLA + this.alturaCuadroMatriz * valorImprimir
					+ this.alturaCuadroMatriz / 2 - altura / 2;
		} else {
			x = MARGEN_TABLA + this.anchuraCuadroMatriz * valorImprimir
					+ this.anchuraCuadroMatriz / 2 - anchura / 2;
			y = MARGEN_TABLA - MARGEN_INDICES - altura;
		}
		GraphConstants.setBounds(indice.getAttributes(), new Rectangle(x, y,
				anchura, altura));

		return indice;
	}

	/**
	 * Devuelve la representación visual del grafo de dependencia
	 * 	si no se han eliminado filas y columnas.
	 * 
	 * @param esTabulado 
	 * 		Indica si se genera al pulsar "Tabular nodos del grafo" o no
	 * 
	 * @return 
	 * 		Representación visual del grafo.
	 */
	public JGraph obtenerRepresentacionGrafo(boolean esTabulado) {
		this.eliminarFilasColumnas = false;
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
			
			if(	(this.esGrafoDeUnMetodo && this.expresionParaFila!=null && this.expresionParaColumna!=null)	||
				(!this.esGrafoDeUnMetodo && this.ultimaExpresionMultiplesMetodos != null)){
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
	 * Devuelve la representación visual del grafo 
	 * 	de dependencia con las filas y columnas vacías eliminadas.
	 * 	 
	 * @return 
	 * 		Representación visual del grafo con filas y columnas vacías eliminadas.
	 */
	public JGraph obtenerRepresentacionGrafoEliminadasFilasYColumnas() {
		this.eliminarFilasColumnas = true;
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
		boolean[][] fyc= this.matrizTabulado.filasYColumnasVacias();
		int numeroColumnasVisibles = 0;
		for(boolean visible:fyc[1]){
			if(!visible)
				numeroColumnasVisibles++;
		}
		this.numeroColumnasVisibles = numeroColumnasVisibles;
		int columna2=0;
		int numeroFilasVisibles = 0;
		for(boolean visible:fyc[0]){
			if(!visible)
				numeroFilasVisibles++;
		}
		this.numeroFilasVisibles = numeroFilasVisibles;
		if (dibujarTabla) {	
			
			int tamanioMarcadorEjesParaFila = this.numeroFilasTabla > 1 ? TAMANIO_MARCADORES_EJES
					: 0;
			int fila2=0;			
			for (int fila = 0; fila <= this.numeroFilasTabla; fila++) {
				if(fila!=this.numeroFilasTabla && !fyc[0][fila]){
					DefaultGraphCell linea = this.crearLineaParaTabla(MARGEN_TABLA
							- tamanioMarcadorEjesParaFila, MARGEN_TABLA + fila2
							* this.alturaCuadroMatriz, numeroColumnasVisibles
							* this.anchuraCuadroMatriz
							+ tamanioMarcadorEjesParaFila, false);
					representacionGrafo.getGraphLayoutCache().insert(linea);
					if (fila2 != this.numeroFilasTabla && this.numeroFilasTabla > 1) {
						DefaultGraphCell indice = this.crearIndiceParaTabla2(fila2,fila,
								true, invertirEjes[0],invertirEjes[1],numeroFilasVisibles,numeroColumnasVisibles);
						representacionGrafo.getGraphLayoutCache().insert(indice);
					}					
					fila2++;
				}				
			}
			
			DefaultGraphCell lineaFinal = this.crearLineaParaTabla(MARGEN_TABLA
					- tamanioMarcadorEjesParaFila, MARGEN_TABLA + fila2
					* this.alturaCuadroMatriz, numeroColumnasVisibles
					* this.anchuraCuadroMatriz
					+ tamanioMarcadorEjesParaFila, false);
			representacionGrafo.getGraphLayoutCache().insert(lineaFinal);
			
			int tamanioMarcadorEjesParaColumna = this.numeroColumnasTabla > 1 ? TAMANIO_MARCADORES_EJES
					: 0;
			
			for (int columna = 0; columna <= this.numeroColumnasTabla; columna++) {
				if(columna!=this.numeroColumnasTabla && !fyc[1][columna]){
					DefaultGraphCell linea = this.crearLineaParaTabla(MARGEN_TABLA
							+ columna2 * this.anchuraCuadroMatriz, MARGEN_TABLA
							- tamanioMarcadorEjesParaColumna, numeroFilasVisibles
							* this.alturaCuadroMatriz
							+ tamanioMarcadorEjesParaColumna, true);
					representacionGrafo.getGraphLayoutCache().insert(linea);
					if (columna != this.numeroColumnasTabla
							&& (this.numeroColumnasTabla > 1 || this.numeroColumnasTabla == 1
									&& this.numeroFilasTabla == 1)) {
						DefaultGraphCell indice = this.crearIndiceParaTabla2(
								columna2,columna, false, invertirEjes[0],invertirEjes[1],numeroFilasVisibles,numeroColumnasVisibles);
						representacionGrafo.getGraphLayoutCache().insert(indice);
					}
					columna2++;
				}
			}
			
			lineaFinal = this.crearLineaParaTabla(MARGEN_TABLA
					+ columna2 * this.anchuraCuadroMatriz, MARGEN_TABLA
					- tamanioMarcadorEjesParaColumna, numeroFilasVisibles
					* this.alturaCuadroMatriz
					+ tamanioMarcadorEjesParaColumna, true);
			representacionGrafo.getGraphLayoutCache().insert(lineaFinal);
			
			if(	(this.esGrafoDeUnMetodo && this.expresionParaFila!=null && this.expresionParaColumna!=null)	||
					(!this.esGrafoDeUnMetodo && this.ultimaExpresionMultiplesMetodos != null)){
					this.insertarEjesTabularGrafo(representacionGrafo);
			}			
			
		}		
		
		if (!this.nodosPosicionados) {
			
			this.posicionarNodosSegunTabulado2(invertirEjes[0],invertirEjes[1],fyc);
			
			this.nodosPosicionados = true;
		}

		for (NodoGrafoDependencia nodo : this.nodos) {
			representacionGrafo.getGraphLayoutCache().insert(
					nodo.obtenerCeldasDelNodoParaGrafo().toArray());
		}

		int anchuraTotal = numeroColumnasVisibles * this.anchuraCuadroMatriz
				+ MARGEN_TABLA * 2;
		int alturaTotal = numeroFilasVisibles * this.alturaCuadroMatriz + MARGEN_TABLA * 2;
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
	 * 
	 * @param representacionGrafo 
	 * 		Grafo donde vamos a insertarlo
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
		
		//	Grafo de un método texto X
		if(this.esGrafoDeUnMetodo && this.expresionParaColumna != null && !this.expresionParaColumna.equals("")){
			textoX = Texto.get("GP_VALORES_DE", Conf.idioma)+" "+this.expresionParaColumna;
		
			//	Grafo de varios métodos texto Y, columnas son funciones
		}else if(!this.esGrafoDeUnMetodo && this.esExpresionDeFila){
			textoX = Texto.get("GP_VALORES_FUNCIONES", Conf.idioma);
		
			//	Grafo de varios métodos texto Y, columnas son funciones
		}else if(!this.esGrafoDeUnMetodo && !this.esExpresionDeFila 
				&& this.ultimaExpresionMultiplesMetodos!=null && !this.ultimaExpresionMultiplesMetodos.equals("")){
			textoX = Texto.get("GP_VALORES_DE", Conf.idioma) + " " + this.ultimaExpresionMultiplesMetodos;
		}
		
		
		int textoLongitudX = ANCHO_PIXEL_CARACTER*textoX.length();
		int limiteTextoX;
		if(this.eliminarFilasColumnas)
			limiteTextoX= GrafoDependencia.MARGEN_TABLA+this.numeroColumnasVisibles*this.anchuraCuadroMatriz;
		else
			limiteTextoX= GrafoDependencia.MARGEN_TABLA+this.getNumeroColumnasTabla()*this.anchuraCuadroMatriz;
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
		
		//	Grafo de un método texto Y
		if(this.esGrafoDeUnMetodo && this.expresionParaFila != null && !this.expresionParaFila.equals("")){
			textoY = Texto.get("GP_VALORES_DE", Conf.idioma)+" "+this.expresionParaFila;
		
		//	Grafo de varios métodos texto Y, filas son funciones
		}else if(!this.esGrafoDeUnMetodo && !this.esExpresionDeFila){
			textoY = Texto.get("GP_VALORES_FUNCIONES", Conf.idioma);
		
		//	Grafo de varios métodos texto Y, filas son funciones
		}else if(!this.esGrafoDeUnMetodo && this.esExpresionDeFila 
				&& this.ultimaExpresionMultiplesMetodos!=null && !this.ultimaExpresionMultiplesMetodos.equals("")){
			textoY = Texto.get("GP_VALORES_DE", Conf.idioma) + " " + this.ultimaExpresionMultiplesMetodos;
		}
		
		int textoLongitudY = ANCHO_PIXEL_CARACTER*textoY.length();
		int limiteTextoY;
		if(this.eliminarFilasColumnas)
			limiteTextoY = GrafoDependencia.MARGEN_TABLA+this.numeroFilasVisibles*this.alturaCuadroMatriz;
		else
			limiteTextoY = GrafoDependencia.MARGEN_TABLA+this.getNumeroFilasTabla()*this.alturaCuadroMatriz;
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
	 * @return 
	 * 		Número de filas especificado para la matriz de tabulado.
	 */
	public int getNumeroFilasTabla() {
		return this.numeroFilasTabla;
	}

	/**
	 * Devuelve el número de columnas especificado para la matriz de tabulado.
	 * 
	 * @return 
	 * 		Número de columnas especificado para la matriz de tabulado.
	 */
	public int getNumeroColumnasTabla() {
		return this.numeroColumnasTabla;
	}

	/**
	 * Devuelve el tamaño de la representación visual del grafo.
	 * 
	 * @return 
	 * 		Tamaño de la representación visual del grafo.
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
	 * @param invertirFilas 
	 * 		False = Orden creciente, True = Orden decreciente para filas
	 * @param invertirColumnas 
	 * 		False = Orden creciente, True = Orden decreciente para columnas
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
								+ Conf.sepV + (this.matrizTabulado.numFilas()-1-fila)
								* this.alturaCuadroMatriz;
					}else{
						y = MARGEN_TABLA
								+ Conf.sepV + fila
								* this.alturaCuadroMatriz;
					}
					
					if(invertirColumnas){
						x = MARGEN_TABLA + Conf.sepH
								+ (this.matrizTabulado.numColumnas()-1-columna) * this.anchuraCuadroMatriz;
					}else{
						x = MARGEN_TABLA + Conf.sepH
								+ columna * this.anchuraCuadroMatriz;
					}
					
					nodo.setPosicion(x, y);					
				}
			}
		}
	}

	/**
	 * Posiciona los nodos del grafo según lo que especifique la matriz de
	 * tabulado actual, pero teniendo en cuenta que existen filas y/o columnas
	 * eliminadas porque estaban vacías.
	 * 
	 * @param invertirFilas 
	 * 		False = Orden creciente, True = Orden decreciente para filas
	 * @param invertirColumnas 
	 * 		False = Orden creciente, True = Orden decreciente para columnas
	 * @param fyc 
	 * 		Array que contiene las filas y columnas eliminadas
	 */
	private void posicionarNodosSegunTabulado2(boolean invertirFilas,
			boolean invertirColumnas, boolean fyc[][]) {
		for (int fila = 0; fila < this.matrizTabulado.numFilas(); fila++) {
			for (int columna = 0; columna < this.matrizTabulado.numColumnas(); columna++) {
				NodoGrafoDependencia nodo = this.matrizTabulado.get(fila,
						columna);
				if (nodo != null) {					
					
					int x,y;
					
					if(invertirFilas){
						int numFilasEliminadas = 0;
						for(int i=this.numeroFilasTabla-1;i>fila;i--){
							if(fyc[0][i])
								numFilasEliminadas++;
						}
						y = MARGEN_TABLA
								+ Conf.sepV + (this.matrizTabulado.numFilas()-1-(fila+numFilasEliminadas))
								* this.alturaCuadroMatriz;
					}else{
						int numFilasEliminadas = 0;
						for(int i=0;i<fila;i++){
							if(fyc[0][i])
								numFilasEliminadas++;
						}
						y = MARGEN_TABLA
								+ Conf.sepV + (fila - numFilasEliminadas)
								* this.alturaCuadroMatriz;
					}
					
					if(invertirColumnas){
						int numColumnasEliminadas = 0;
						for(int i=this.numeroColumnasTabla-1;i>columna;i--){
							if(fyc[1][i])
								numColumnasEliminadas++;
						}
						x = MARGEN_TABLA + Conf.sepH
								+ (this.matrizTabulado.numColumnas()-1-(columna+numColumnasEliminadas)) * this.anchuraCuadroMatriz;
					}else{
						int numColumnasEliminadas = 0;
						for(int i=0;i<columna;i++){
							if(fyc[1][i])
								numColumnasEliminadas++;
						}
						x = MARGEN_TABLA + Conf.sepH
								+ (columna-numColumnasEliminadas) * this.anchuraCuadroMatriz;
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
	 *		Número de filas de la tabla.
	 * @param numeroColumnasTabla
	 *		Número de columnas de la tabla.
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
	 * 		Expresión del primer parámetro para tabular
	 * 
	 * @param expresionParaColumna
	 * 		Expresión del segundo parámetro para tabular
	 * 
	 * @return 
	 * 		Mensaje de error si ocurrió algun error, null en caso contrario.
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
				engine.put(this.metodo.getNombreParametroE(i), 
						this.obtenerValorConTipo(nodo.getParams()[i]));
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
					this.expresionParaFila = null;
					this.expresionParaColumna = null;
				} else if (matriz.get(fila, columna) != null) {
					mensajeError = Texto
							.get("GP_ERROR_POSICIONAR", Conf.idioma)
							+ " x="
							+ columna + ", y=" + fila;
					this.expresionParaFila = null;
					this.expresionParaColumna = null;
				} else {
					matriz.set(fila, columna, nodo);
				}
			} catch (ScriptException e) {
				String expresion = filaEvaluada ? expresionParaColumna : expresionParaFila;
				mensajeError = "<html><p align=\"center\">" + Texto.get("GP_EXPR_INVALIDA", Conf.idioma) + " \"" + expresion +
						"\" " + Texto.get("GP_EXPR_INVALIDA_2", Conf.idioma) + "</p><p align=\"center\">" + e.getMessage() +
						"</p></html>";
				this.expresionParaFila = null;
				this.expresionParaColumna = null;
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
	 * Tabula automáticamente los nodos del grafo de múltiples métodos,
	 * 	dada una expresión de filas o columnas
	 * 
	 * @param ultimaExpresionMultiplesMetodos
	 * 		Expresión introducida por el usuario
	 * 
	 * @param esExpresionDeFila
	 * 		Indica si la expresión introducida por el usuario es de
	 * 		filas (true) o no (false)
	 * 
	 * @return 
	 * 		Mensaje de error si ocurrió algun error, null en caso contrario.
	 */
	public String tabularMultiplesMetodos(String ultimaExpresionMultiplesMetodos, boolean esExpresionDeFila) {
		
		String mensajeError = null;
		ScriptEngineManager manager = new ScriptEngineManager();
		MatrizDinamica<NodoGrafoDependencia> matriz = new MatrizDinamica<NodoGrafoDependencia>();
		this.ultimaExpresionMultiplesMetodos = ultimaExpresionMultiplesMetodos;
		this.esExpresionDeFila = esExpresionDeFila;
		
		//	Creamos mapa para los nombres y posición de métodos actuales
		HashMap<String, Integer> mapaMetodos = new HashMap<String, Integer>();
		for(int i = 0; i<this.metodos.size(); i++){
			mapaMetodos.put(this.metodos.get(i).getNombre(), i);
		}
		
		//	Recorremos nodos
		for (NodoGrafoDependencia nodo : this.nodos) {
			
			//	Creamos engine para evaluar
			ScriptEngine engine = manager.getEngineByName("js");
			
			//	Introducimos parámetros y su valor del nodo
			DatosMetodoBasicos dmb = this.metodos.get(mapaMetodos.get(nodo.getMetodo()));
			
			for (int i = 0; i < dmb.getNumParametrosE(); i++) {
				
				//	Solo añadimos los parámetros comunes, no más, 
				//	por eficiencia y por evitar errores		
				if(this.parametrosComunes==null)
					this.parametrosComunes = this.getParametrosComunes();
				if(this.parametrosComunesS==null)
					this.parametrosComunesS = this.getParametrosComunesS();
				for(int j = 0; j<this.parametrosComunes.size(); j++){
					String metodosComunesParam = this.metodos.get(0).getNombreParametroE(this.parametrosComunes.get(j));
					String esteMetodoParam = dmb.getNombreParametroE(i);
					if(metodosComunesParam.equals(esteMetodoParam)){
						engine.put(dmb.getNombreParametroE(i), 
								this.obtenerValorConTipo(nodo.getParams()[i]));							
					}
				}				
			}			
			
			try {
				int fila, columna;
				
				//	Evaluación de la expresión si la expresión es de fila
				if(esExpresionDeFila){
					fila = this.obtenerValorEnteroDeEvaluacion(engine
							.eval(ultimaExpresionMultiplesMetodos));
					columna = mapaMetodos.get(nodo.getMetodo());					
					
				//	Evaluación de la expresión si la expresión es de columna
				}else{
					columna = this.obtenerValorEnteroDeEvaluacion(engine
							.eval(ultimaExpresionMultiplesMetodos));
					fila = mapaMetodos.get(nodo.getMetodo());
				}
				
				//	Errores
				if (fila < 0 || columna < 0) {
					mensajeError = Texto.get("GP_EXPR_NEGATIVOS", Conf.idioma);
					this.ultimaExpresionMultiplesMetodos = null;
				} else if (matriz.get(fila, columna) != null) {
					mensajeError = Texto
							.get("GP_ERROR_POSICIONAR", Conf.idioma)
							+ " x="
							+ columna + ", y=" + fila;
					this.ultimaExpresionMultiplesMetodos = null;
				} else {
					matriz.set(fila, columna, nodo);
				}
			} catch (ScriptException e) {
				String expresion = ultimaExpresionMultiplesMetodos;
				mensajeError = "<html><p align=\"center\">" + Texto.get("GP_EXPR_INVALIDA", Conf.idioma) + " \"" + expresion +
						"\" " + Texto.get("GP_EXPR_INVALIDA_2", Conf.idioma) + "</p><p align=\"center\">" + e.getMessage() +
						"</p></html>";
				this.ultimaExpresionMultiplesMetodos = null;
			}
		}

		//	Si no hay error
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
	 *		Objeto devuelto por el motor de expresiones.
	 * 
	 * @return 
	 * 		posición resultante, -1 si el resultado de la evaluación no es un
	 *		entero válido.
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
	 *		Matriz de tabulado.
	 * @param raiz
	 *		Nodo raiz del grafo.
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
	 *		Matriz.
	 * @param fila
	 *		Fila actual
	 * @param columna
	 *		Columna actual
	 * 
	 * @return 
	 * 		[fila, columna] con la posición más cercana y libre.
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
	 * @return 
	 * 		Array de booleanos donde la primera posición indica si las filas deben estar
	 * 		en orden creciente (true) o en orden decreciente(false) y la segunda posición 
	 * 		indica si las columnas deben estar en orden creciente (true) o en orden decreciente(false)
	 * @throws ScriptException 
	 */
	private boolean[] invertirEjes(){
		
		//	Variables		
		boolean[] retorno = new boolean[2];
		retorno[0] = false;
		retorno[1] = false;
		int valorFilaPadre = 0, valorColumnaPadre = 0, valorFilaHijo = 0, valorColumnaHijo = 0;		

		HashMap<String, Integer> mapaMetodos = new HashMap<String, Integer>();
		if(!this.esGrafoDeUnMetodo){
			for(int i = 0; i<this.metodos.size(); i++){
				mapaMetodos.put(this.metodos.get(i).getNombre(), i);
			}	
		}
		//	Si no existen expresiones devolvemos false
		if(  	(this.esGrafoDeUnMetodo && (this.expresionParaFila == null || this.expresionParaColumna == null)) ||
				(!this.esGrafoDeUnMetodo && this.ultimaExpresionMultiplesMetodos == null)
			)
				return retorno;		
		
		//	Obtenemos raíz
		NodoGrafoDependencia raiz = this.nodos.get(0);
		
		//	Obtenemos sus hijos
		List<NodoGrafoDependencia> listaHijos = raiz.getDependencias();
		
		//	Creamos script para evaluar los valores de salida
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		
		//	Introducimos los valores del padre
		if(this.esGrafoDeUnMetodo){
			for (int i = 0; i < this.metodo.getNumParametrosE(); i++) {
				engine.put(this.metodo.getNombreParametroE(i), 
						this.obtenerValorConTipo(raiz.getParams()[i]));
			}
			
			//	Calculamos valores del padre
			try {
				valorFilaPadre = this.obtenerValorEnteroDeEvaluacion(engine
						.eval(this.expresionParaFila));
				valorColumnaPadre = this.obtenerValorEnteroDeEvaluacion(engine
						.eval(this.expresionParaColumna));
			} catch (ScriptException e) {
				
			}
			

		}else{
			DatosMetodoBasicos dmb = this.metodos.get(mapaMetodos.get(raiz.getMetodo()));
			for (int i = 0; i < dmb.getNumParametrosE(); i++) {
				if(this.parametrosComunes==null)
					this.parametrosComunes = this.getParametrosComunes();
				if(this.parametrosComunesS==null)
					this.parametrosComunesS = this.getParametrosComunesS();
				for(int j = 0; j<this.parametrosComunes.size(); j++){
					String metodosComunesParam = this.metodos.get(0).getNombreParametroE(this.parametrosComunes.get(j));
					String esteMetodoParam = dmb.getNombreParametroE(i);
					if(metodosComunesParam.equals(esteMetodoParam)){
						engine.put(dmb.getNombreParametroE(i), 
								this.obtenerValorConTipo(raiz.getParams()[i]));							
					}
				}
			}
			try{
				//	Calculamos valores del padre
				if(this.esExpresionDeFila){
					valorFilaPadre = this.obtenerValorEnteroDeEvaluacion(engine
							.eval(this.ultimaExpresionMultiplesMetodos));
					valorColumnaPadre = mapaMetodos.get(raiz.getMetodo());
				}else{
					valorFilaPadre = mapaMetodos.get(raiz.getMetodo());
					valorColumnaPadre = this.obtenerValorEnteroDeEvaluacion(engine
							.eval(this.ultimaExpresionMultiplesMetodos));
				}
			}catch(ScriptException e) {
				
			}
		}		
		
		//	Hacemos lo mismo con los hijos para ver si tenemos que invertir
		for(NodoGrafoDependencia nodoHijo:listaHijos){
			if(this.esGrafoDeUnMetodo){
				for (int i = 0; i < this.metodo.getNumParametrosE(); i++) {
					engine.put(this.metodo.getNombreParametroE(i), 
							this.obtenerValorConTipo(nodoHijo.getParams()[i]));
				}
				
				//	Calculamos valores de los hijos
				try{
					valorFilaHijo = this.obtenerValorEnteroDeEvaluacion(engine
							.eval(this.expresionParaFila));
					valorColumnaHijo = this.obtenerValorEnteroDeEvaluacion(engine
							.eval(this.expresionParaColumna));
				}catch (ScriptException e) {
					continue;
				}

			}else{
				DatosMetodoBasicos dmb = this.metodos.get(mapaMetodos.get(nodoHijo.getMetodo()));
				for (int i = 0; i < dmb.getNumParametrosE(); i++) {
					if(this.parametrosComunes==null)
						this.parametrosComunes = this.getParametrosComunes();
					if(this.parametrosComunesS==null)
						this.parametrosComunesS = this.getParametrosComunesS();
					for(int j = 0; j<this.parametrosComunes.size(); j++){
						String metodosComunesParam = this.metodos.get(0).getNombreParametroE(this.parametrosComunes.get(j));
						String esteMetodoParam = dmb.getNombreParametroE(i);
						if(metodosComunesParam.equals(esteMetodoParam)){
							engine.put(dmb.getNombreParametroE(i), 
									this.obtenerValorConTipo(nodoHijo.getParams()[i]));							
						}
					}
				}
				
				//	Calculamos valores de los hijos
				try{
					if(this.esExpresionDeFila){
						valorFilaHijo = this.obtenerValorEnteroDeEvaluacion(engine
								.eval(this.ultimaExpresionMultiplesMetodos));
						valorColumnaHijo = mapaMetodos.get(nodoHijo.getMetodo());
					}else{
						valorFilaHijo = mapaMetodos.get(nodoHijo.getMetodo());
						valorColumnaHijo = this.obtenerValorEnteroDeEvaluacion(engine
								.eval(this.ultimaExpresionMultiplesMetodos));
					}
				}catch (ScriptException e) {
					continue;
				}

			}	
			
			//	Finalmente comparamos
			if(valorFilaHijo < valorFilaPadre){
				retorno[0] = true;
			}
			if(valorColumnaHijo < valorColumnaPadre){
				retorno[1] = true;
			}
		}
		return retorno;		
	}
	
	/**
	 * Obtiene una lista inmodificable de los nodos, para cambiar
	 *	la orientación de las aristas
	 *
	 * @return 
	 * 		Lista inmodificable de nodos del grafo
	 */
	public List<NodoGrafoDependencia> getNodos() {
		return Collections.unmodifiableList(nodos);
	}
	
	/**
	 * Establece los nodos como no posicionados, luego
	 * 	fuerza que se recoloquen
	 */
	public void nodosPosicionadosFalse(){
		this.nodosPosicionados=false;
	}
	
	/**
	 * Obtiene una lista de los indices del primer método seleccionado
	 * que son comunes a todos los restantes métodos seleccionados.
	 * 
	 * @return
	 * 	Lista de enteros
	 */
	private List<Integer> parametrosComunes(){
		
		//	Lista de indices de parámetros comunes (respecto al primer método)
		//	que devolveremos
		List<Integer> l = new ArrayList<>();
		
		//	Si es de un método no tienen parámetros comunes
		if(this.esGrafoDeUnMetodo)
			return l;		
		
		//	Obtenemos los datos de los parámetros del primer método
		DatosMetodoBasicos metodoPrimero = this.metodos.get(0);
		
		//	Recorremos todos los parámetros del primer método
		
		forA:for(int i=0 ; i<metodoPrimero.getNumParametrosE() ; i++){
			String nombrePrimero 	= metodoPrimero.getNombreParametroE(i);
			String tipoPrimero 		= metodoPrimero.getTipoParametroE(i);
			int dimensionPrimero 	= metodoPrimero.getDimParametroE(i);
			
			//	Recorremos todos los métodos restantes
			
			forB:for(int j=1 ; j<this.metodos.size(); j++){
				boolean esteMetodoTieneParametrosComunes = false;
				
				//	Recorremos todos los parámetros de los métodos restantes
				
				DatosMetodoBasicos metodoActual = this.metodos.get(j);
				for(int k=0 ; k<metodoActual.getNumParametrosE() ; k++){
					
					//	Comparamos el parámetro actual con el parámetro del primer método
					String nombreActual 	= metodoActual.getNombreParametroE(k);
					String tipoActual 		= metodoActual.getTipoParametroE(k);
					int dimensionActual 	= metodoActual.getDimParametroE(k);
					
					//	Comprobamos nombre, tipo y dimensión de parámetros
					if(	nombreActual.equals(nombrePrimero) &&
						tipoActual.equals(tipoPrimero) &&
						dimensionActual == dimensionPrimero){
						
						esteMetodoTieneParametrosComunes = true;
						
						//	Si lo hemos encontrado en el método actual vamos al siguiente método
						continue forB;
					}
				}
				
				//	Si el método actual no tiene en común ninguno de sus parámetros con el parámetro
				//	por el que vayamos del primer método significa que no es común a todos los métodos
				//	seleccionados, por lo tanto, miramos el siguiente parámetro del primer método
				if(esteMetodoTieneParametrosComunes == false)
					continue forA;
			}
			
			//	Llegados aqui significa que el parámetro i es común a todos, lo añadimos a la lista
			l.add(i);
		}		
		// System.out.println(l);
		return l;
	}
	
	/**
	 * Obtiene una lista de los indices del primer método seleccionado
	 * que son comunes a todos los restantes métodos seleccionados.
	 * 
	 * @return
	 * 	Lista de enteros
	 */
	public List<Integer> getParametrosComunes(){
		this.parametrosComunes = this.parametrosComunes();
		return this.parametrosComunes;
	}
	
	/**
	 * Obtiene una lista de los nombres y tipos de los parámetros comunes a los
	 * 	métodos representados
	 * 
	 * @return
	 * 	Nombres y tipos de los parámetros comunes a los métodos representados
	 */
	private List<String> parametrosComunesS(){
		List<String> l = new ArrayList<>();
		if(this.parametrosComunes == null || this.parametrosComunes.size() == 0){
			return l;
		}
		//	Obtenemos los datos de los parámetros del primer método
		DatosMetodoBasicos metodoPrimero = this.metodos.get(0);
		
		//	Obtenemos los nombres de los parámetros comunes
		for(int i : this.parametrosComunes){
			String texto = metodoPrimero.getTipoParametroE(i);
			for(int j = 0; j<metodoPrimero.getDimParametroE(i);j++){
				texto = texto+"[]";
			}
			l.add(texto + " " + metodoPrimero.getNombreParametroE(i));
		}
		return l;
	}
	
	/**
	 * Obtiene una lista de los nombres y tipos de los parámetros comunes a los
	 * 	métodos representados
	 * 
	 * @return
	 * 	Nombres y tipos de los parámetros comunes a los métodos representados
	 */
	public List<String> getParametrosComunesS(){
		this.parametrosComunesS = this.parametrosComunesS();
		return this.parametrosComunesS;
	}
}
