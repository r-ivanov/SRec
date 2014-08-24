package grafica;

/**
 * Clase auxiliar que permite tratar y almacenar las coordenadas en las que
 * deben ubicarse los distintos nodos en un determinado nivel.
 */
public class NivelGrafo {

	private int coordMax[] = new int[0];
	private boolean ocupados[] = new boolean[0];

	private static int valorDefecto = ContenedorArbol.espacioInicial;

	/**
	 * Establece el valor de la coordenada máxima para un determinado nivel.
	 * 
	 * @param nivel
	 *            Nivel en la jerarquía.
	 * @param valorCoordenadaMaxima
	 *            Valor de la coordenada máxima.
	 */
	public void setNivel(int nivel, int valorCoordenadaMaxima) {
		if (nivel > (this.coordMax.length - 1)) {
			int coordMaxAux[] = new int[nivel + 1];
			for (int i = 0; i < this.coordMax.length; i++) {
				coordMaxAux[i] = this.coordMax[i];
			}

			for (int i = this.coordMax.length; i < (nivel + 1); i++) {
				coordMaxAux[i] = valorDefecto;
			}

			this.coordMax = coordMaxAux;

			boolean ocupadosAux[] = new boolean[nivel + 1];
			for (int i = 0; i < this.ocupados.length; i++) {
				ocupadosAux[i] = this.ocupados[i];
			}

			for (int i = this.ocupados.length; i < (nivel + 1); i++) {
				ocupadosAux[i] = false;
			}
			this.ocupados = ocupadosAux;
		}
		if (this.coordMax[nivel] < valorCoordenadaMaxima) {
			this.coordMax[nivel] = valorCoordenadaMaxima;
		}
		this.ocupados[nivel] = true;
	}

	/**
	 * Permite otener el valor de la coordenada máxima para un nivel.
	 * 
	 * @param nivel
	 *            Nivel en la jerarquía.
	 * 
	 * @return Valor de la coordenada máxima.
	 */
	public int getNivel(int nivel) {
		if (nivel > (this.coordMax.length - 1)) {
			this.setNivel(nivel, valorDefecto);
			return this.getNivel(nivel);
		} else {
			int valor = this.coordMax[0];
			// for (int i=1; i<=nivel; i++) // "i<=nivel" es correcto: provoca
			// que hijos de hermanos queden justo debajo (no está mal así)
			for (int i = 1; i < this.coordMax.length; i++) {
				if (this.coordMax[i] > valor && this.ocupados[i]) {
					valor = this.coordMax[i];
				}
			}
			return valor;
		}
	}

	/**
	 * Permite obtener directamente el último valor almacenado de la coordenada
	 * para el nivel especificado.
	 * 
	 * @param nivel
	 *            Nivel en la jerarquía.
	 * 
	 * @return Valor de la coordenada.
	 */
	public int getNivelExacto(int nivel) {
		if (nivel > (this.coordMax.length - 1)) {
			this.setNivel(nivel, valorDefecto);
			return this.getNivel(nivel);
		} else {
			return this.coordMax[nivel];
		}
	}

	/**
	 * Devuelve el máximo ancho de entre todos los niveles almacenados.
	 * 
	 * @return Máximo ancho.
	 */
	public int getMaximoAncho() {
		int valor = valorDefecto;
		for (int i = 0; i < this.coordMax.length; i++) {
			if (this.coordMax[i] > valor && this.ocupados[i]) {
				valor = this.coordMax[i];
			}
		}
		return valor;
	}
}