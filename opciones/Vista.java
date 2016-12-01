package opciones;

/**
 * Representa una vista de ejecución del algoritmo.
 */
public class Vista {

	public static final int TIPO_REC = 1;
	public static final int TIPO_DYV = 2;

	public static final String codigos[] = { "V_ARBOL", "V_PILA", "V_TRAZA",
			"V_ESTRUC", "V_GRAFO_DEP" };
	private String codigo;

	private boolean activa = false;

	private int tipo = Vista.TIPO_REC;

	private int panel = 1;

	/**
	 * Crea una nueva vista, dado uno de los códigos especificados en
	 * Vista.codigos.
	 * 
	 * @param codigo
	 *            Algún código almacenado en Vista.codigos.
	 */
	public Vista(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * Devuelve el código que identifica a la vista.
	 * 
	 * @return Algún código almacenado en Vista.codigos.
	 */
	public String getCodigo() {
		return this.codigo;
	}

	/**
	 * Establece la vista como activa.
	 * 
	 * @param valor
	 *            true para estar activa, false en caso contrario.
	 */
	public void setActiva(boolean valor) {
		this.activa = valor;
	}

	/**
	 * Devuelve si la vista está activa o no.
	 * 
	 * @return true si la vista está activa, false en caso contrario.
	 */
	public boolean esActiva() {
		return this.activa;
	}

	/**
	 * Permite establecer el tipo de la vista dependiendo de el tipo de
	 * ejecución.
	 * 
	 * @param tipo
	 *            TIPO_REC o TIPO_DYV
	 */
	public void setTipo(int tipo) {
		switch (tipo) {
		case Vista.TIPO_REC:
			this.tipo = TIPO_REC;
			break;
		case Vista.TIPO_DYV:
			this.tipo = TIPO_DYV;
			break;
		default:
			this.tipo = TIPO_REC;
		}
	}

	/**
	 * Devuelve el tipo de la vista.
	 * 
	 * @return TIPO_REC o TIPO_DYV
	 */
	public int getTipo() {
		return this.tipo;
	}

	/**
	 * Establece la vista como parte de uno de los dos paneles.
	 * 
	 * @param x
	 *            1 para el panel 1, 2 para el panel 2.
	 */
	public void setPanel(int x) {
		if (x == 2) {
			this.panel = 2;
		} else {
			this.panel = 1;
		}
	}

	/**
	 * Devuelve el panel en el que se encuentra la vista.
	 * 
	 * @return 1, o 2.
	 */
	public int getPanel() {
		return this.panel;
	}

	/**
	 * Permite obtener la posición del código de vista dentro de Vista.codigos.
	 * 
	 * @param nombre
	 *            Código de la vista.
	 * 
	 * @return posición, o -1 si el código no existe.
	 */
	public static int getPosic(String nombre) {
		for (int i = 0; i < codigos.length; i++) {
			if (nombre.equals(codigos[i])) {
				return i;
			}
		}
		return -1;
	}

}