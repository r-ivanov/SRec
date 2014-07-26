package datos;

import utilidades.ServiciosString;

/**
 * Representa un estado del problema, bien a la entrada, bien a la salida de una
 * llamada al método
 * 
 * @author Luis Fernández y Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class Estado {

	private boolean visibles[];
	private String param[];
	private int dim[];
	private String clases[];

	private String representacion;
	private String representacionLineaTraza;

	// DYV:
	// Copia de la estructura
	private Object estructura = null;
	private int indiceEstructura = -1;
	// valores finales, posiciones de la estructura, no numeros de parámetros
	private int indices[] = null;

	/**
	 * Constructor: genera un nuevo estado vacío
	 */
	public Estado() {
		this(null, null);
	}

	/**
	 * Constructor: genera un nuevo estado con una serie de valores, bien
	 * parámetros, bien de salida
	 * 
	 * @param p
	 *            conjunto de valores
	 */
	public Estado(Object p[]) {
		this.param = new String[p.length];
		this.clases = new String[p.length];
		this.dim = new int[p.length];

		for (int i = 0; i < p.length; i++) {
			this.param[i] = ServiciosString.representacionObjeto(p[i]);
		}

		for (int i = 0; i < p.length; i++) {
			this.clases[i] = ServiciosString.simplificarClase(p[i].getClass()
					.getCanonicalName().replace("[]", ""));
			this.dim[i] = ServiciosString.vecesQueContiene(p[i].getClass()
					.getCanonicalName(), "[]");
		}

		this.visibles = new boolean[p.length];
		for (int i = 0; i < p.length; i++) {
			this.visibles[i] = true;
		}

		this.setRepresentacion(this.visibles);
	}

	/**
	 * Constructor: genera un nuevo estado con una serie de valores, bien
	 * parámetros, bien de salida y sus clases
	 * 
	 * @param p
	 *            array de valores
	 * @param c
	 *            array de clases de los valores de p
	 */
	private Estado(Object p[], Class<?> c[]) {
		if (p != null && c != null) {
			this.param = new String[p.length];
			this.clases = new String[c.length];

			for (int i = 0; i < p.length; i++) {
				this.param[i] = ServiciosString.representacionObjeto(p[i]);
			}

			for (int i = 0; i < c.length; i++) {
				if (c[i] != null) {
					this.clases[i] = c[i].getCanonicalName();
				}
			}

			this.visibles = new boolean[p.length];
			for (int i = 0; i < p.length; i++) {
				this.visibles[i] = true;
			}

			this.setRepresentacion(this.visibles);
		} else {
			this.param = new String[0];
			this.clases = new String[0];
		}

	}

	/**
	 * Devuelve la estructura
	 * 
	 * @return valores
	 */
	public Object getEstructura() {
		return this.estructura;
	}

	/**
	 * Devuelve los valores de los índices
	 * 
	 * @return valor de las clases de los valores
	 */
	public int[] getIndices() {
		return this.indices;
	}

	/**
	 * Establece los indices que identifican la estructura.
	 * 
	 * @param o
	 *            Estructura.
	 * @param indEstructura
	 *            Índice que identifica la estructura.
	 * @param ind
	 *            Índices de la estructura que deben usarse (filaInf, filaSup,
	 *            colInf, colSup, ).
	 */
	public void setEstructuraIndices(Object o, int indEstructura, int[] ind) {
		if (o == null) {
			return;
		}

		// Copiamos la estructura, que llega en e
		this.estructura = this.copiaArray(o, o.getClass());

		// Copiamos el número de parámetro en el que está la estructura
		this.indiceEstructura = indEstructura;

		// Copiamos los valores de los índices que hay que usar, que llegan en
		// in (filaInf, filaSup, colInf, colSup, )
		if (ind != null) {
			this.indices = new int[ind.length];
			for (int i = 0; i < ind.length; i++) {
				this.indices[i] = ind[i];
			}
		} else {
			this.indices = new int[0];
		}
	}
	
	/**
	 * Establece el índice que identifica el parámetro en el que está la estructura.
	 * 
	 * @param x índice de la estructura.
	 */
	public void setIndiceDeEstructura(int x) {
		this.indiceEstructura = x;
	}
	
	/**
	 * Devuelve el índice que identifica el parámetro en el que está la estructura.
	 * 
	 * @return Índice de la estructura.
	 */
	public int getIndiceDeEstructura() {
		return this.indiceEstructura;
	}
	
	/**
	 * Devuelve el valor parámetro dado por la posición especificada.
	 * 
	 * @param i posición del parámetro.
	 * 
	 * @return valor del parámetro.
	 */
	public String getParametro(int i) {
		if (i < this.param.length) {
			return new String("" + this.param[i] + "");
		} else {
			return null;
		}
	}

	/**
	 * Devuelve los valores de los parámetros.
	 * 
	 * @return valores
	 */
	public String[] getParametros() {
		String[] copiaParametros = new String[this.param.length];

		for (int i = 0; i < copiaParametros.length; i++) {
			copiaParametros[i] = new String("" + this.param[i]);
		}

		return copiaParametros;
	}

	/**
	 * Devuelve el valor de las clases de los valores
	 * 
	 * @return valor de las clases de los valores
	 */
	public String[] getClases() {
		return this.clases;
	}
	
	/**
	 * Devuelve el número de dimensiones.
	 * 
	 * @return Número de dimensiones.
	 */
	public int[] getDimensiones() {
		return this.dim;
	}
	
	/**
	 * Establece la representación dada la visibilidad de parámetros especificada.
	 * 
	 * @param visibles Visibilidad de parámetros.
	 */
	protected void setRepresentacion(boolean visibles[]) {
		this.representacion = "";
		this.visibles = visibles;

		if (visibles.length == this.param.length) {
			for (int i = 0; i < this.param.length; i++) {
				if (visibles[i]) {
					this.representacion = this.representacion + this.param[i];
					boolean hayMas = false;
					for (int j = i + 1; j < visibles.length; j++) {
						if (visibles[j]) {
							hayMas = true;
						}
					}
					if (hayMas) {
						this.representacion = this.representacion + " , ";
					}
				}
			}
		} else {
			this.representacion = "Error de visibilidad(1:" + visibles.length
					+ "/" + this.param.length + ")";
		}
	}
	
	/**
	 * Establece la representación dada la visibilidad de parámetros especificada y los nombres.
	 * 
	 * @param visibles Visibilidad de parámetros.
	 * @param nombres Nombres de parámetros.
	 */
	protected void setRepresentacionLineasTraza(boolean visibles[],
			String nombres[]) {
		this.representacionLineaTraza = "";
		this.visibles = visibles;

		if (visibles.length == this.param.length) {
			for (int i = 0; i < this.param.length; i++) {
				if (visibles[i]) {
					this.representacionLineaTraza = this.representacionLineaTraza
							+ nombres[i] + "==" + this.param[i];
					boolean hayMas = false;
					for (int j = i + 1; j < visibles.length; j++) {
						if (visibles[j]) {
							hayMas = true;
						}
					}
					if (hayMas) {
						this.representacionLineaTraza = this.representacionLineaTraza
								+ " , ";
					}
				}
			}
		} else {
			this.representacion = "Error de visibilidad(2)";
		}
	}
	
	/**
	 * Devuelve la representación del estado.
	 * 
	 * @return Representación del estado.
	 */
	public String getRepresentacion() {
		return this.representacion;
	}
	
	/**
	 * Obtiene la representación completa, incluyendo nombres y valores.
	 * 
	 * @param nombres Nombres de los parámetros.
	 * 
	 * @return Representación completa.
	 */
	public String getRepresentacionCompleta(String nombres[]) {
		String representacion = "";

		if (this.visibles.length == this.param.length) {
			for (int i = 0; i < this.param.length; i++) {
				if (nombres != null) {
					representacion = representacion + nombres[i] + "="
							+ this.param[i];
				} else {
					representacion = representacion + "return=" + this.param[i];
				}
				if (i < this.param.length - 1) {
					representacion = representacion + " , ";
				}
			}
		} else {
			representacion = "Error de visibilidad(1:" + this.visibles.length
					+ "/" + this.param.length + ")";
		}

		return representacion;
	}
	
	/**
	 * Devuelve la representación para las lineas de traza.
	 * 
	 * @return Representación para las lineas de traza.
	 */
	public String getRepresentacionLineasTraza() {
		return this.representacionLineaTraza;
	}
	
	/**
	 * Devuelve la visibilidad de parámetros.
	 * 
	 * @return visibilidad de parámetros.
	 */
	public boolean[] getVisibilidad() {
		return this.visibles;
	}
	
	/**
	 * Devuelve los valores de los parámetros.
	 * 
	 * @return Valores de los parámetros.
	 */
	public String[] getValoresStrings() {
		return this.param;

	}
	
	/**
	 * Añade un nuevo valor en la lista de parámetros.
	 * 
	 * @param e nuevo valor.
	 */
	public void nuevoValorParametro(String e) {
		if (this.param == null || this.param.length == 0) {
			this.param = new String[1];
			this.param[0] = e;
		} else {
			String[] pSaux = new String[this.param.length + 1];
			for (int i = 0; i < this.param.length; i++) {
				pSaux[i] = this.param[i];
			}
			pSaux[this.param.length] = e;
			this.param = pSaux;
		}
	}
	
	/**
	 * Añade un nuevo tipo de dato en la lista de tipos.
	 * 
	 * @param e nuevo tipo.
	 */
	public void setClase(String e) {
		if (this.clases == null || this.clases.length == 0) {
			this.clases = new String[1];
			this.clases[0] = e;
		} else {
			String[] pSaux = new String[this.clases.length + 1];
			for (int i = 0; i < this.clases.length; i++) {
				pSaux[i] = this.clases[i];
			}
			pSaux[this.clases.length] = e;
			this.clases = pSaux;
		}
	}
	
	/**
	 * Añade una nueva dimension en la lista de dimensiones.
	 * 
	 * @param e nuevo valor de dimensión.
	 */
	public void setDim(int e) {
		if (this.dim == null || this.dim.length == 0) {
			this.dim = new int[1];
			this.dim[0] = e;
		} else {
			int[] pSaux = new int[this.dim.length + 1];
			for (int i = 0; i < this.dim.length; i++) {
				pSaux[i] = this.dim[i];
			}
			pSaux[this.dim.length] = e;
			this.dim = pSaux;
		}
	}

	/**
	 * Genera una copia del estado.
	 * 
	 * @return Copia del estado.
	 */
	public Estado copiar() {
		Estado e = new Estado();

		if (this.param != null) {
			for (int i = 0; i < this.param.length; i++) {
				e.nuevoValorParametro(new String(this.param[i]));
			}
		}

		if (this.visibles != null) {
			e.visibles = new boolean[this.visibles.length];
			for (int i = 0; i < this.visibles.length; i++) {
				e.visibles[i] = this.visibles[i];
			}
		}

		if (this.param != null) {
			e.param = new String[this.param.length];
			for (int i = 0; i < this.param.length; i++) {
				e.param[i] = new String(this.param[i]);
			}
		}

		if (this.dim != null) {
			e.dim = new int[this.dim.length];
			for (int i = 0; i < this.dim.length; i++) {
				e.dim[i] = this.dim[i];
			}
		}

		if (this.clases != null) {
			e.clases = new String[this.clases.length];
			for (int i = 0; i < this.clases.length; i++) {
				e.clases[i] = new String(this.clases[i]);
			}
		}

		if (this.representacion != null) {
			e.representacion = new String(this.representacion);
		}
		if (this.representacionLineaTraza != null) {
			e.representacionLineaTraza = new String(
					this.representacionLineaTraza);
		}

		// Copiamos cosas de DYV
		e.setEstructuraIndices(this.estructura, this.indiceEstructura,
				this.indices);

		return e;
	}

	/**
	 * Realiza un duplicado de los valores
	 * 
	 * @return duplicado de los valores
	 */
	private Object copiaArray(Object p, Class c) {
		Object o = null;
		if (!(c.getCanonicalName().contains("[][]"))) {
			if (c.getCanonicalName().contains("int")) {
				o = new int[((int[]) p).length];
				for (int j = 0; j < ((int[]) p).length; j++) {
					((int[]) o)[j] = ((int[]) p)[j];
				}
			} else if (c.getCanonicalName().contains("short")) {
				o = new short[((short[]) p).length];
				for (int j = 0; j < ((short[]) p).length; j++) {
					((short[]) o)[j] = ((short[]) p)[j];
				}
			} else if (c.getCanonicalName().contains("byte")) {
				o = new byte[((byte[]) p).length];
				for (int j = 0; j < ((byte[]) p).length; j++) {
					((byte[]) o)[j] = ((byte[]) p)[j];
				}
			} else if (c.getCanonicalName().contains("long")) {
				o = new long[((long[]) p).length];
				for (int j = 0; j < ((long[]) p).length; j++) {
					((long[]) o)[j] = ((long[]) p)[j];
				}
			} else if (c.getCanonicalName().contains("float")) {
				o = new float[((float[]) p).length];
				for (int j = 0; j < ((float[]) p).length; j++) {
					((float[]) o)[j] = ((float[]) p)[j];
				}
			} else if (c.getCanonicalName().contains("double")) {
				o = new double[((double[]) p).length];
				for (int j = 0; j < ((double[]) p).length; j++) {
					((double[]) o)[j] = ((double[]) p)[j];
				}
			} else if (c.getCanonicalName().contains("boolean")) {
				o = new boolean[((boolean[]) p).length];
				for (int j = 0; j < ((boolean[]) p).length; j++) {
					((boolean[]) o)[j] = ((boolean[]) p)[j];
				}
			} else if (c.getCanonicalName().contains("char")) {
				o = new char[((char[]) p).length];
				for (int j = 0; j < ((char[]) p).length; j++) {
					((char[]) o)[j] = ((char[]) p)[j];
				}
			} else if (c.getCanonicalName().contains("java.lang.String")) {
				o = new String[((String[]) p).length];
				for (int j = 0; j < ((String[]) p).length; j++) {
					((String[]) o)[j] = ((String[]) p)[j];
				}
			}
		} else {
			if (c.getCanonicalName().contains("int")) {
				o = new int[((int[][]) p).length][];
				for (int i = 0; i < ((int[][]) p).length; i++) {
					((int[][]) o)[i] = (int[]) this.copiaArray(
							((int[][]) p)[i], ((int[][]) p)[i].getClass());
				}
			} else if (c.getCanonicalName().contains("short")) {
				o = new short[((short[][]) p).length][];
				for (int i = 0; i < ((int[][]) p).length; i++) {
					((short[][]) o)[i] = (short[]) this.copiaArray(
							((short[][]) p)[i], ((short[][]) p)[i].getClass());
				}
			} else if (c.getCanonicalName().contains("byte")) {
				o = new byte[((byte[][]) p).length][];
				for (int i = 0; i < ((int[][]) p).length; i++) {
					((byte[][]) o)[i] = (byte[]) this.copiaArray(
							((byte[][]) p)[i], ((byte[][]) p)[i].getClass());
				}
			} else if (c.getCanonicalName().contains("long")) {
				o = new long[((long[][]) p).length][];
				for (int i = 0; i < ((int[][]) p).length; i++) {
					((long[][]) o)[i] = (long[]) this.copiaArray(
							((long[][]) p)[i], ((long[][]) p)[i].getClass());
				}
			} else if (c.getCanonicalName().contains("float")) {
				o = new float[((float[][]) p).length][];
				for (int i = 0; i < ((int[][]) p).length; i++) {
					((float[][]) o)[i] = (float[]) this.copiaArray(
							((float[][]) p)[i], ((float[][]) p)[i].getClass());
				}
			} else if (c.getCanonicalName().contains("double")) {
				o = new double[((double[][]) p).length][];
				for (int i = 0; i < ((int[][]) p).length; i++) {
					((double[][]) o)[i] = (double[]) this
							.copiaArray(((double[][]) p)[i],
									((double[][]) p)[i].getClass());
				}
			} else if (c.getCanonicalName().contains("boolean")) {
				o = new boolean[((boolean[][]) p).length][];
				for (int i = 0; i < ((int[][]) p).length; i++) {
					((boolean[][]) o)[i] = (boolean[]) this.copiaArray(
							((boolean[][]) p)[i],
							((boolean[][]) p)[i].getClass());
				}
			} else if (c.getCanonicalName().contains("char")) {
				o = new char[((char[][]) p).length][];
				for (int i = 0; i < ((int[][]) p).length; i++) {
					((char[][]) o)[i] = (char[]) this.copiaArray(
							((char[][]) p)[i], ((char[][]) p)[i].getClass());
				}
			} else if (c.getCanonicalName().contains("java.lang.String")) {
				o = new String[((String[][]) p).length][];
				for (int i = 0; i < ((int[][]) p).length; i++) {
					((String[][]) o)[i] = (String[]) this
							.copiaArray(((String[][]) p)[i],
									((String[][]) p)[i].getClass());
				}
			}
		}

		return o;
	}
}
