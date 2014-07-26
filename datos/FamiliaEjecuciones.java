package datos;

import grafica.ContenedorArbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;

import paneles.PanelFamiliaEjecuciones;
import conf.Conf;
import ventanas.Ventana;

public class FamiliaEjecuciones {

	private static FamiliaEjecuciones instance = null;

	private List<Ejecucion> ejecuciones;
	private boolean habilitado;

	private FamiliaEjecuciones() {
		this.ejecuciones = new ArrayList<Ejecucion>();
		this.habilitado = true;
	}

	public static FamiliaEjecuciones getInstance() {
		if (instance == null) {
			instance = new FamiliaEjecuciones();
		}
		return instance;
	}

	public void addEjecucion(Ejecucion ejecucion) {
		this.ejecuciones.add(ejecucion);
	}

	public void borrarEjecuciones() {
		this.ejecuciones.clear();
	}

	public boolean estaHabilitado() {
		return this.habilitado;
	}
	
	public int numeroEjecuciones() {
		return this.ejecuciones.size();
	}
	
	public Iterator<Ejecucion> getEjecuciones() {
		return this.ejecuciones.iterator();
	}
	
	public void pintaFamilia() {
		JFrame f = new JFrame();
        JScrollPane sp = new JScrollPane(new PanelFamiliaEjecuciones(this));
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        f.add(sp);
        f.pack();
        f.setVisible(true);
	}

	public void visualizarEjecuciones() {
		for (int i = 0; i < this.ejecuciones.size(); i++) {
			Ejecucion ejecucion = this.ejecuciones.get(i);
			if (i == 0) {
				Ventana.getInstance().setDTB(ejecucion.getDatosTrazaBasicos());
				Ventana.getInstance().visualizarAlgoritmo(ejecucion.getTraza(),
						false, null, ejecucion.getFicheroFuenteDirectorio(),
						ejecucion.getFicheroFuente(), true);
			}
		}
	}
}
