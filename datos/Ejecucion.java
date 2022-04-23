package datos;

import grafica.ContenedorArbol;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;

import utilidades.NombresYPrefijos;
import utilidades.ServiciosString;
import ventanas.Ventana;
import conf.Conf;

/**
 * Almacena una ejecuci�n concreta de un algoritmo.
 * 
 * @author David Pastor Herranz
 */
public class Ejecucion {

	private Traza traza;

	private DatosTrazaBasicos dtb;

	/**
	 * Devuelve una nueva instancia dada una traza de ejecuci�n
	 * 
	 * @param traza
	 *            Traza de ejecuci�n.
	 */
	public Ejecucion(Traza traza) {
		this.traza = traza;
		this.traza.asignarNumeroMetodo();
		this.actualizarVisibilidad();
	}
	
	/**
	 * Devuelve el n�mero de nodos totales de la ejecuci�n.
	 * 
	 * @return int Nodos Totales.
	 */
	public int numeroNodos() {
		return this.traza.getNumNodos();
	}

	/**
	 * Devuelve una copia de la traza que se us� para crear la ejecuci�n.
	 * 
	 * @return Traza
	 */
	public Traza obtenerTrazaCompleta() {
		return this.traza.copiar();
	}

	/**
	 * Devuelve una copia de la traza podada seg�n los par�metros de visibilidad
	 * de m�todos especificados en el dtb.
	 * 
	 * @return Traza podada
	 */
	public Traza obtenerTrazaConPodaParaVisibilidad() {
		return this.dtb.podar(this.traza);
	}

	/**
	 * Actualiza la visibilidad de la traza con los par�metros de visibilidad de
	 * m�todos especificados en el dtb.
	 */
	public void actualizarVisibilidad() {
		this.dtb = this.obtenerDTBConVisibilidadParaTraza();
		this.traza.setVisibilidad(this.dtb);
	}

	/**
	 * Devuelve los datos de traza b�sicos de la ejecuci�n.
	 * 
	 * @return Datos de traza b�sicos.
	 */
	public DatosTrazaBasicos getDTB() {
		return this.dtb;
	}

	/**
	 * Permite obtener un dtb con los datos de visibilidad de m�todos y
	 * par�metros de la ejecuci�n anterior en el programa.
	 * 
	 * @return Datos de traza b�sicos con la visibilidad de m�todos y par�metros
	 *         anterior.
	 */
	private DatosTrazaBasicos obtenerDTBConVisibilidadParaTraza() {

		ClaseAlgoritmo claseAlgoritmo = Ventana.getInstance().getClase();
		DatosTrazaBasicos datosNuevos = new DatosTrazaBasicos(this.traza);

		if (claseAlgoritmo != null) {
			for (DatosMetodoBasicos metodo : datosNuevos.getMetodos()) {
				for (MetodoAlgoritmo metodoAlgoritmo : claseAlgoritmo.getMetodos()) {
					if (metodo.esIgual(metodoAlgoritmo)) {
						
						metodo.setEsVisible(metodoAlgoritmo.getMarcadoVisualizar());
						
						for (int indiceEntrada = 0; indiceEntrada < metodo.getNumParametrosE(); indiceEntrada++) {
							metodo.setVisibilidadE(metodoAlgoritmo.getVisibilidadEntrada(indiceEntrada),
											indiceEntrada);
						}
						for (int indiceSalida = 0; indiceSalida < metodo.getNumParametrosS(); indiceSalida++) {
							metodo.setVisibilidadS(metodoAlgoritmo.getVisibilidadSalida(indiceSalida),
											indiceSalida);
						}
					}
				}
			}
		}

		return datosNuevos;
	}

	/**
	 * Permite obtener el grafo visual que representa la ejecuci�n.
	 * 
	 * @return Grafo que representa la ejecuci�n.
	 */
	public JGraph obtenerGrafo() {

		DefaultGraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,
				new DefaultCellViewFactory());

		JGraph grafo = new JGraph(model, view);
		grafo.getModel().addGraphModelListener(null);

		Traza traza = this.obtenerTrazaConPodaParaVisibilidad();
		traza.todoVisible();
		
		NombresYPrefijos nyp = null;
		if (this.traza.getNumMetodos() > 1) { 
			nyp = new NombresYPrefijos();
			String[] nombresMetodos = traza.getNombresMetodos();
			String prefijos[] = ServiciosString.obtenerPrefijos(nombresMetodos);
			for (int i = 0; i < nombresMetodos.length; i++) {
				nyp.add(nombresMetodos[i], prefijos[i]);
			}
		}

		ContenedorArbol c = new ContenedorArbol(traza.getRaiz(), grafo, nyp, 1, null);
		grafo.setBackground(Conf.colorPanel);
		grafo.getGraphLayoutCache().insert(c.getCeldas());

		return grafo;
	}
}
