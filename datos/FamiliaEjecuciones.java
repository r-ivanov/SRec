package datos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JScrollPane;

import paneles.PanelFamiliaEjecuciones;
import ventanas.Ventana;

public class FamiliaEjecuciones {

	private static FamiliaEjecuciones instance = null;

	private List<Ejecucion> ejecuciones;
	private boolean habilitado;
	
	private Ejecucion ejecucionActiva;
	
	private PanelFamiliaEjecuciones panelEjecuciones;
	
	private FamiliaEjecuciones() {
		this.ejecuciones = new ArrayList<Ejecucion>();
		this.habilitado = false;
		this.panelEjecuciones = new PanelFamiliaEjecuciones(this);
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
	
	public boolean esEjecucionActiva(Ejecucion ejecucion) {
		return ejecucion.equals(ejecucionActiva);
	}
	
	public void setEjecucionActiva(Ejecucion ejecucion) {
		this.ejecucionActiva = ejecucion;
		Ventana.getInstance().visualizarEjecucion(ejecucion, false);
	}
	
	public void setPrimeraEjecucionActiva() {
		this.setEjecucionActiva(this.ejecuciones.get(0));		
	}

	public void borrarEjecuciones() {
		this.ejecuciones.clear();
	}

	public boolean estaHabilitado() {
		return this.habilitado;
	}
	
	public void habilitar() {
		this.habilitado = true;
	}
	
	public void deshabilitar() {
		this.habilitado = false;
	}
	
	public int numeroEjecuciones() {
		return this.ejecuciones.size();
	}
	
	public Iterator<Ejecucion> getEjecuciones() {
		return this.ejecuciones.iterator();
	}
	
	public void actualizarPanel(int tamanio, int orientacion) {
		this.panelEjecuciones.pintar(tamanio, orientacion);
	}
	
	public void actualizarVisibilidadEjecuciones() {
		for (Iterator<Ejecucion> iterator = this.getEjecuciones(); iterator.hasNext();) {
			iterator.next().actualizarVisibilidad();
		}
	}
	
	public JScrollPane obtenerPanelEjecuciones() {
		return this.panelEjecuciones;
	}
}
