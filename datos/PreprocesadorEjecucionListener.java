package datos;

import java.util.List;

/**
 * Recoge eventos relacionados con el proceso de la ejecuci�n de un algoritmo.
 * 
 * @author David Pastor Herranz
 */
public interface PreprocesadorEjecucionListener {

	/**
	 * Se invoca cuando la ejecuci�n ha finalizado.
	 * 
	 * @param ejecuciones
	 *            Lista de ejecuciones generadas.
	 * @param satisfactoria
	 *            true si pudo completarse exitosamente, false si hubo cualquier
	 *            tipo de error.
	 */
	public void ejecucionFinalizada(List<Ejecucion> ejecuciones,
			boolean satisfactoria);
}
