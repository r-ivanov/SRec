package datos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import paneles.PanelFamiliaEjecuciones;
import ventanas.Ventana;

public class FamiliaEjecuciones {

	private static FamiliaEjecuciones instance = null;

	private List<Ejecucion> ejecuciones;
	private boolean habilitado;
	
	private Ejecucion ejecucionActiva;
	
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
	
	public boolean esEjecucionActiva(Ejecucion ejecucion) {
		return ejecucion.equals(ejecucionActiva);
	}
	
	public void setEjecucionActiva(Ejecucion ejecucion) {
		this.ejecucionActiva = ejecucion;
		Ventana.getInstance().setDTB(ejecucion.getDatosTrazaBasicos());
		Ventana.getInstance().visualizarAlgoritmo(
				ejecucion.getTraza(), false, null,
				ejecucion.getFicheroFuenteDirectorio(),
				ejecucion.getFicheroFuente(), true);
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
	
	public PanelFamiliaEjecuciones obtenerPanelEjecuciones() {
		return new PanelFamiliaEjecuciones(this);
	}

//	public void visualizarEjecuciones() {
//		for (int i = 0; i < this.ejecuciones.size(); i++) {
//			Ejecucion ejecucion = this.ejecuciones.get(i);
//			if (i == 0) {
//				Ventana.getInstance().setDTB(ejecucion.getDatosTrazaBasicos());
//				Ventana.getInstance().visualizarAlgoritmo(ejecucion.getTraza(),
//						false, null, ejecucion.getFicheroFuenteDirectorio(),
//						ejecucion.getFicheroFuente(), true);
//			}
//		}
//	}
}
