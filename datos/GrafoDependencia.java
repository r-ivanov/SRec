package datos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;

import utilidades.MatrizDinamica;
import utilidades.ServiciosString;
import utilidades.Texto;
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

	/**
	 * Devuelve una nueva instancia de un grafo.
	 * 
	 * @param traza
	 *            Traza de ejecución asociada.
	 * @param metodo
	 *            Método para el que obtener los nodos del grafo.
	 */
	public GrafoDependencia(Traza traza, DatosMetodoBasicos metodo) {

		this.nodos = new ArrayList<NodoGrafoDependencia>();
		this.metodo = metodo;

		this.insertarNodos(null, traza.getRaiz(),
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
		NodoGrafoDependencia nodo = new NodoGrafoDependencia(registroActivacion);
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
			if (mismoMetodo) {
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
				registroActivacion);
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
	 * @return Celda para el grafo que representa el índice.
	 */
	private DefaultGraphCell crearIndiceParaTabla(int valor, boolean fila) {

		String valorString = (fila ? "y=" : "x=") + String.valueOf(valor);
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
	 * 
	 * @return Representación visual del grafo.
	 */
	public JGraph obtenerRepresentacionGrafo() {

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
							true);
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
							columna, false);
					representacionGrafo.getGraphLayoutCache().insert(indice);
				}
			}
		}

		if (!this.nodosPosicionados) {
			this.posicionarNodosSegunTabulado();
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
	 */
	private void posicionarNodosSegunTabulado() {
		for (int fila = 0; fila < this.matrizTabulado.numFilas(); fila++) {
			for (int columna = 0; columna < this.matrizTabulado.numColumnas(); columna++) {
				NodoGrafoDependencia nodo = this.matrizTabulado.get(fila,
						columna);
				if (nodo != null) {
					nodo.setPosicion(MARGEN_TABLA + MARGEN_NODOS_ANCHURA
							+ columna * this.anchuraCuadroMatriz, MARGEN_TABLA
							+ MARGEN_NODOS_ALTURA + fila
							* this.alturaCuadroMatriz);
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

		for (NodoGrafoDependencia nodo : this.nodos) {
			ScriptEngine engine = manager.getEngineByName("js");
			for (int i = 0; i < this.metodo.getNumParametrosE(); i++) {
				engine.put(this.metodo.getNombreParametroE(i), this.obtenerValorConTipo(nodo.getParams()[i]));
			}
			try {
				int fila = this.obtenerValorEnteroDeEvaluacion(engine
						.eval(expresionParaFila));
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
				mensajeError = Texto.get("GP_EXPR_INVALIDA", Conf.idioma);
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
}
