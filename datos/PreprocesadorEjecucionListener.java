package datos;

import java.util.List;

/**
 * Recoge eventos relacionados con el proceso de la ejecuci�n de
 * un algoritmo.
 * 
 * @author David Pastor Herranz
 */
public interface PreprocesadorEjecucionListener {
	public void ejecucionFinalizada(List<Ejecucion> ejecuciones, boolean satisfactoria);
}
