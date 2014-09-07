package utilidades;

/**
 * Almacena una correspondencia entre un nombre de un m�todo y su prefijo. Estos
 * prefijos son utilizados en la visualizaci�n de algoritmos para reducir el
 * tama�o de los nodos de entrada cuando los nombres de par�metros son muchos
 * y/o muy largos.
 */
public class NombresYPrefijos {

	private String nombre[];
	private String prefijo[];

	/**
	 * Crea una nueva instancia de NombresYPrefijos.
	 */
	public NombresYPrefijos() {
		this.nombre = new String[0];
		this.prefijo = new String[0];
	}

	/**
	 * A�ade una correspondencia entre el nombre de un m�todo y un prefijo.
	 * 
	 * @param nombre
	 *            Nombre dle m�todo.
	 * @param prefijo
	 *            Prefijo asociado.
	 */
	public void add(String nombre, String prefijo) {
		if (this.nombre.length == 0) {
			this.nombre = new String[1];
			this.prefijo = new String[1];

			this.nombre[0] = nombre;
			this.prefijo[0] = prefijo;
		} else {
			String nombreAux[] = new String[this.nombre.length + 1];
			String prefijAux[] = new String[this.prefijo.length + 1];

			for (int i = 0; i < this.nombre.length; i++) {
				nombreAux[i] = this.nombre[i];
				prefijAux[i] = this.prefijo[i];
			}

			nombreAux[this.nombre.length] = nombre;
			prefijAux[this.nombre.length] = prefijo;

			this.nombre = nombreAux;
			this.prefijo = prefijAux;
		}
	}

	/**
	 * Devuelve el prefijo asociado al nombre del m�todo pasado por par�metro.
	 * 
	 * @param nombre
	 *            Nombre del m�todo.
	 * 
	 * @return Prefijo asociado, o "No encontrado", si el nombre del m�todo no
	 *         tiene un prefijo registrado.
	 */
	public String getPrefijo(String nombre) {
		for (int i = 0; i < this.nombre.length; i++) {
			if (this.nombre[i].equals(nombre)) {
				return this.prefijo[i];
			}
		}
		return "No encontrado";
	}

	/**
	 * Devuelve la longitud del prefijo m�s largo registrado.
	 * 
	 * @return longitud del prefijo m�s largo registrado.
	 */
	public int getLongitudPrefijoMasLargo() {
		String prefijoLargo = "";

		for (int i = 0; i < this.prefijo.length; i++) {
			if (this.prefijo[i].length() > prefijoLargo.length()) {
				prefijoLargo = this.prefijo[i];
			}
		}

		return prefijoLargo.length();
	}
}
