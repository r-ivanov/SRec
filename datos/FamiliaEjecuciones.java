package datos;

import java.util.ArrayList;
import java.util.List;

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
