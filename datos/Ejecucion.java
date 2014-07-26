package datos;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import grafica.ContenedorArbol;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;

import conf.Conf;

/**
 * Almacena una ejecución concreta de un algoritmo.
 */
public class Ejecucion {
	
	private Traza traza;
	private String ficheroFuenteDirectorio;
	private String ficheroFuente;
	private boolean ficheroEditable;
	
	public Ejecucion(Traza traza, String ficheroFuenteDirectorio, String ficheroFuente) {
		this.traza = traza;
		this.ficheroFuenteDirectorio = ficheroFuenteDirectorio;
		this.ficheroFuente = ficheroFuente;
	}

	public Traza getTraza() {
		return traza;
	}

	public String getFicheroFuenteDirectorio() {
		return ficheroFuenteDirectorio;
	}

	public String getFicheroFuente() {
		return ficheroFuente;
	}

	public boolean isFicheroEditable() {
		return ficheroEditable;
	}
	
	public DatosTrazaBasicos getDatosTrazaBasicos() {
		return new DatosTrazaBasicos(this.traza);
	}
	
	public JGraph obtenerGrafo() {
		
		DefaultGraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
		
		JGraph grafo = new JGraph(model, view);
		grafo.getModel().addGraphModelListener(null);
		
		ContenedorArbol c = new ContenedorArbol(this.traza.getRaiz(), grafo, null, 1);
		grafo.setBackground(Conf.colorPanel);
		grafo.getGraphLayoutCache().insert(c.getCeldas());
		
		grafo.setSize(new Dimension(c.maximoAncho(), c.maximoAlto()));
		
		return grafo;
	}
	
	public BufferedImage obtenerSnapshotDelGrafo() {
		JGraph grafo = this.obtenerGrafo();
		BufferedImage bi = grafo.getImage(grafo.getBackground(), 0);
		bi.getGraphics().drawImage(null, 0, 0, null);
		bi.getGraphics().dispose();
		return bi;
	}
}
