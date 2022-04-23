package datos;

import grafica.ContenedorArbol;
import grafica.ContenedorCronologica;
import grafica.ContenedorEstructura;
import grafica.ContenedorPila;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import utilidades.FotografoArbol;
import utilidades.NombresYPrefijos;
import utilidades.ServiciosString;
import ventanas.Ventana;
import conf.Conf;

/**
 * Permite generar el grafo (Árbol, Pila y Traza) correspondiente a una traza
 * para posteriormente ser exportado.
 */
public class GestorTrazaExportacion {

	// traza para la exportacion, así no tenemos que rebobinar la traza que se
	// usa en vistas
	private Traza t;
	private JGraph graph;
	private int numeroVista;

	private int alto;
	private int ancho;

	private NombresYPrefijos nyp;

	/**
	 * Construye una nueva instancia del objeto
	 * 
	 * @param numeroVista
	 *            Número de vista para la que se generará el grafo (0 para
	 *            árbol, 1 para pila, 2 para cronológica y 3 para estructura)
	 */
	public GestorTrazaExportacion(Ventana ventana, int numeroVista) {
		this.t = ventana.getTraza().copiar();
		this.t.nadaVisible();
		this.numeroVista = numeroVista;

		boolean mostrarNombreMetodos = ventana.getTraza().getNumMetodos() != 1;

		if (mostrarNombreMetodos) {
			this.nyp = new NombresYPrefijos();
			String[] nombresMetodos = ventana.trazaCompleta.getNombresMetodos();
			String prefijos[] = ServiciosString.obtenerPrefijos(nombresMetodos);
			for (int i = 0; i < nombresMetodos.length; i++) {
				this.nyp.add(nombresMetodos[i], prefijos[i]);
			}
		}
	}

	/**
	 * Mueve la traza a su estado final.
	 * 
	 * @return true si todos los elementos son visibles, false en caso
	 *         contrario.
	 */
	public boolean finalTraza() {
		return this.t.enteroVisible();
	}

	/**
	 * Avanza la traza al siguiente estado.
	 */
	public void avanzarTraza() {
		this.t.siguienteVisible();
	}

	/**
	 * Devuelve el ancho de la vista a generar.
	 * 
	 * @return ancho de la vista
	 */
	public int getAncho() {
		return this.ancho;
	}

	/**
	 * Devuelve el alto de la vista a generar.
	 * 
	 * @return ancho de la vista
	 */
	public int getAlto() {
		return this.alto;
	}

	/**
	 * Devuelve un grafo correspondiente a la vista seleccionada del estado
	 * actual de la traza.
	 * 
	 * @return Grafo correspondiente a la vista seleccionada.
	 */
	public JGraph grafoEstadoActual() {
		this.graph = null;
		GraphModel model = null;
		GraphLayoutCache view;
		Object[] celdas;
		switch (this.numeroVista) {
		case 0:
			model = new DefaultGraphModel();
			view = new GraphLayoutCache(model, new DefaultCellViewFactory());

			this.graph = new JGraph(model, view);
			this.graph.getModel().addGraphModelListener(null);
			ContenedorArbol c = new ContenedorArbol(this.t.getRaiz(),
					this.graph, this.nyp, 1, null);
			celdas = c.getCeldas();
			this.graph.setBackground(Conf.colorPanel);
			this.graph.getGraphLayoutCache().insert(celdas);
			this.ancho = c.maximoAncho();
			this.alto = c.maximoAlto();
			FotografoArbol.gifPila = false; // Indicamos que NO vamos a hacer
											// una animacion gif de la vista de
											// pila
			break;
		case 1:
			model = new DefaultGraphModel();
			view = new GraphLayoutCache(model, new DefaultCellViewFactory());

			this.graph = new JGraph(model, view);
			this.graph.getModel().addGraphModelListener(null);
			ContenedorPila cp = new ContenedorPila(this.t.getRaiz(),
					this.t.copiar(), this.nyp, 1);
			celdas = cp.getCeldas();
			this.graph.setBackground(Conf.colorPanel);
			this.graph.getGraphLayoutCache().insert(celdas);
			this.ancho = cp.maximoAncho();
			this.alto = cp.maximoAlto();
			FotografoArbol.gifPila = true; // Indicamos que vamos a hacer una
											// animacion gif de la vista de pila
			break;
		case 2:
			model = new DefaultGraphModel();
			view = new GraphLayoutCache(model, new DefaultCellViewFactory());

			this.graph = new JGraph(model, view);
			this.graph.getModel().addGraphModelListener(null);
			ContenedorCronologica cc = new ContenedorCronologica(this.nyp,
					this.t.getRaiz());
			celdas = cc.getCeldas();
			this.graph.setBackground(Conf.colorPanel);
			this.graph.getGraphLayoutCache().insert(celdas);
			this.ancho = cc.maximoAncho();
			this.alto = cc.maximoAlto();
			FotografoArbol.gifPila = true; // Indicamos que vamos a hacer una
											// animacion gif de la vista de
											// pila. En este caso tiene que ser
											// TRUE
			break;
		case 3:
			model = new DefaultGraphModel();
			view = new GraphLayoutCache(model, new DefaultCellViewFactory());

			this.graph = new JGraph(model, view);
			this.graph.getModel().addGraphModelListener(null);
			ContenedorEstructura ce = new ContenedorEstructura(
					this.t.getRaiz());
			celdas = ce.getCeldas();
			this.graph.setBackground(Conf.colorPanel);
			this.graph.getGraphLayoutCache().insert(celdas);
			this.ancho = ce.maximoAncho();
			this.alto = ce.maximoAlto();
			FotografoArbol.gifPila = false; // Indicamos que vamos NO a hacer
											// una animacion gif de la vista de
											// pila
			break;
		case 4:
			break;
		default:
			return null;
		}
		return this.graph;
	}

}
