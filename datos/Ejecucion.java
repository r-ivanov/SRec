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
 * Almacena una ejecución concreta de un algoritmo.
 */
public class Ejecucion {
	
	private Traza traza;
	
	private DatosTrazaBasicos dtb;
	
	public Ejecucion(Traza traza) {
		this.traza = traza;
		this.traza.asignarNumeroMetodo();
		actualizarVisibilidad();
	}

	public Traza obtenerTrazaCompleta() {
		return this.traza.copiar();
	}
	
	public Traza obtenerTrazaConPodaParaVisibilidad() {
		return this.dtb.podar(this.traza);
	}
	
	public void actualizarVisibilidad() {
		this.dtb = obtenerDTBConVisibilidadParaTraza();
		this.traza.setVisibilidad(this.dtb);
	}
	
	public DatosTrazaBasicos getDTB() {
		return this.dtb;
	}
	
	/**
	 * Permite obtener un dtb con los datos de visibilidad de métodos y parámetros
	 * de la ejecución anterior en el programa.
	 * 
	 * @return Datos de traza básicos con la visibilidad de métodos y parámetros anterior.
	 */
	private DatosTrazaBasicos obtenerDTBConVisibilidadParaTraza() {
		
		DatosTrazaBasicos datosActuales = Ventana.getInstance().getDTB();
		DatosTrazaBasicos datosNuevos = new DatosTrazaBasicos(this.traza);
		
		if (datosActuales != null && datosActuales.getNombreMetodoEjecucion() != null &&
				datosActuales.getNombreMetodoEjecucion().equals(datosNuevos.getNombreMetodoEjecucion())) {
			
			if (datosActuales.getNumMetodos() == datosNuevos.getNumMetodos()) {
				
				for (int i = 0; i < datosActuales.getNumMetodos(); i++) {
					
					DatosMetodoBasicos metodoActual = datosActuales.getMetodo(i);
					DatosMetodoBasicos metodoNuevo = datosNuevos.getMetodo(i);		
					
					if (metodoActual.esIgual(metodoNuevo)) {
						
						metodoNuevo.setEsVisible(metodoActual.getEsVisible());
						
						for (int indiceEntrada = 0; indiceEntrada < metodoActual.getNumParametrosE(); indiceEntrada++){
							metodoNuevo.setVisibilidadE(metodoActual.getVisibilidadE(indiceEntrada), indiceEntrada);
						}
						
						for (int indiceSalida = 0; indiceSalida < metodoActual.getNumParametrosS(); indiceSalida++){
							metodoNuevo.setVisibilidadS(metodoActual.getVisibilidadS(indiceSalida), indiceSalida);
						}
					}
				}
			}
		}
			
		return datosNuevos;
	}
	
	public JGraph obtenerGrafo() {
		
		DefaultGraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
		
		JGraph grafo = new JGraph(model, view);
		grafo.getModel().addGraphModelListener(null);
		
		Traza traza = this.obtenerTrazaConPodaParaVisibilidad();
		traza.todoVisible();
		
		NombresYPrefijos nyp = new NombresYPrefijos();
		String[] nombresMetodos = traza.getNombresMetodos();
		String prefijos[] = ServiciosString.obtenerPrefijos(nombresMetodos);
		for (int i = 0; i < nombresMetodos.length; i++) {
			nyp.add(nombresMetodos[i], prefijos[i]);
		}
		
		ContenedorArbol c = new ContenedorArbol(traza.getRaiz(), grafo, nyp, 1);
		grafo.setBackground(Conf.colorPanel);
		grafo.getGraphLayoutCache().insert(c.getCeldas());
		
		return grafo;
	}
}
