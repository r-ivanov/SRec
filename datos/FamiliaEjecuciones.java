package datos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JScrollPane;

import paneles.PanelFamiliaEjecuciones;
import ventanas.Ventana;

/**
 * Almacena y representa una familia de ejecuciones.
 * 
 * @author David Pastor Herranz
 */
public class FamiliaEjecuciones {

	private static FamiliaEjecuciones instance = null;

	private List<Ejecucion> ejecuciones;
	private boolean habilitado;

	private Ejecucion ejecucionActiva;

	private PanelFamiliaEjecuciones panelEjecuciones;
	private boolean necesitaRepintarEjecuciones = false;
	private int tamanioPanel = -1;
	private int orientacionPanel = -1;

	/**
	 * Devuelve una nueva instancia del objeto.
	 */
	private FamiliaEjecuciones() {
		this.ejecuciones = new ArrayList<Ejecucion>();
		this.habilitado = false;
		this.panelEjecuciones = new PanelFamiliaEjecuciones(this);
	}

	/**
	 * Devuelve la instancia �nica de la aplicaci�n.
	 * 
	 * @return Instancia �nica.
	 */
	public static FamiliaEjecuciones getInstance() {
		if (instance == null) {
			instance = new FamiliaEjecuciones();
		}
		return instance;
	}

	/**
	 * A�ade una ejecuci�n a la familia.
	 * 
	 * @param ejecucion
	 */
	public void addEjecucion(Ejecucion ejecucion) {
		this.ejecuciones.add(ejecucion);
		this.necesitaRepintarEjecuciones = true;
	}

	/**
	 * Determina si la ejecuci�n es la que se encuentra visualmente activa.
	 * 
	 * @param ejecucion
	 * 
	 * @return true si se encuentra visualmente activa, false en caso contrario.
	 */
	public boolean esEjecucionActiva(Ejecucion ejecucion) {
		return ejecucion.equals(this.ejecucionActiva);
	}

	/**
	 * Refresca la visualizaci�n activa en el panel.
	 */
	public void recargarEjecucionActiva() {
		Ventana.getInstance().visualizarEjecucion(this.ejecucionActiva, false);
	}

	/**
	 * Establece la ejecuci�n como ejecuci�n visualmente activa.
	 * 
	 * @param ejecucion
	 */
	public void setEjecucionActiva(Ejecucion ejecucion) {
		this.ejecucionActiva = ejecucion;
		Ventana.getInstance().visualizarEjecucion(ejecucion, false);
	}

	/**
	 * Establece la primera ejecuci�n de la familia como visualmente activa.
	 */
	public void setPrimeraEjecucionActiva() {
		this.setEjecucionActiva(this.ejecuciones.get(0));
	}

	/**
	 * Elimina todas las ejecuciones de la familia.
	 */
	public void borrarEjecuciones() {
		this.ejecuciones.clear();
		this.necesitaRepintarEjecuciones = true;
	}

	/**
	 * Determina si el modo de visualizaci�n m�ltiple se encuentra habilitado.
	 * 
	 * @return true si el modo de visualizaci�n m�ltiple se encuentra
	 *         habilitado, false en caso contrario.
	 */
	public boolean estaHabilitado() {
		return this.habilitado;
	}

	/**
	 * Habilita el modo de visualizaci�n m�ltiple.
	 */
	public void habilitar() {
		this.habilitado = true;
		this.borrarEjecuciones();
	}

	/**
	 * Deshabilita el modo de visualizaci�n m�ltiple.
	 */
	public void deshabilitar() {
		this.habilitado = false;
		this.borrarEjecuciones();
	}

	/**
	 * Devuelve el n�mero de ejecuciones que contiene la familia.
	 * 
	 * @return N�mero de ejecuciones que contiene la familia.
	 */
	public int numeroEjecuciones() {
		return this.ejecuciones.size();
	}

	/**
	 * Devuelve un iterador para las ejecuciones.
	 * 
	 * @return Iterador para las ejecuciones.
	 */
	public Iterator<Ejecucion> getEjecuciones() {
		return this.ejecuciones.iterator();
	}

	/**
	 * Actualiza el panel de la familia de ejecuciones dado un tama�o y una
	 * orientaci�n.
	 * 
	 * @param orientacion
	 *            Orientaci�n del panel, Conf.PANEL_HORIZONTAL o
	 *            Conf.PANEL_VERTICAL.
	 * @param forzar
	 *            Si debe forzarse un repintado aunque no se estrictamente
	 *            necesario.
	 */
	public void actualizar(int tamanio, int orientacion, boolean forzar) {
		if (tamanio != this.tamanioPanel
				|| orientacion != this.orientacionPanel
				|| this.necesitaRepintarEjecuciones || forzar) {
			this.actualizarVisibilidadEjecuciones();
			this.panelEjecuciones.pintar(tamanio, orientacion);
			this.tamanioPanel = tamanio;
			this.orientacionPanel = orientacion;
			this.necesitaRepintarEjecuciones = false;
		}
	}

	/**
	 * Actualiza la visibilidad de m�todos y par�metros de las distintas
	 * ejecuciones.
	 */
	private void actualizarVisibilidadEjecuciones() {
		for (Iterator<Ejecucion> iterator = this.getEjecuciones(); iterator
				.hasNext();) {
			iterator.next().actualizarVisibilidad();
		}
	}

	/**
	 * Devuelve el panel que representa la familia de ejecuciones.
	 * 
	 * @return Panel que representa la familia de ejecuciones.
	 */
	public JScrollPane obtenerPanelEjecuciones() {
		return this.panelEjecuciones;
	}
}
