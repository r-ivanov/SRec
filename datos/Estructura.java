package datos;

import utilidades.ServiciosString;

/**
 * Representa una estructura (array o matriz) dada por un array de una o dos
 * dimensiones de un tipo primitivo concreto.
 */
public class Estructura {

	private int matrizInt[][] = null;
	private long matrizLong[][] = null;
	private double matrizDouble[][] = null;
	private char matrizChar[][] = null;
	private String matrizString[][] = null;
	private boolean matrizBoolean[][] = null;

	private int arrayInt[] = null;
	private long arrayLong[] = null;
	private double arrayDouble[] = null;
	private char arrayChar[] = null;
	private String arrayString[] = null;
	private boolean arrayBoolean[] = null;

	private int dim = 0;
	private String clase;

	/**
	 * Crea una nueva estructura, dado un array de una o dos dimensiones.
	 * 
	 * @param o
	 *            Array de una o dos dimensiones de cualquier tipo primitivo.
	 */
	public Estructura(Object o) {
		if (o != null) {
			this.clase = o.getClass().getCanonicalName();

			// Si es matriz
			if (this.clase.contains("[][]")) {
				this.dim = 2;
				if (this.clase.contains("byte") || this.clase.contains("short")
						|| this.clase.contains("int")) {
					this.matrizInt = (int[][]) o;
				} else if (this.clase.contains("long")) {
					this.matrizLong = (long[][]) o;
				} else if (this.clase.contains("float")
						|| this.clase.contains("double")) {
					this.matrizDouble = (double[][]) o;
				} else if (this.clase.contains("char")) {
					this.matrizChar = (char[][]) o;
				} else if (this.clase.contains("String")) {
					this.matrizString = (String[][]) o;
				} else if (this.clase.contains("boolean")) {
					this.matrizBoolean = (boolean[][]) o;
				}
			} else {
				this.dim = 1;
				if (this.clase.contains("byte") || this.clase.contains("short")
						|| this.clase.contains("int")) {
					this.arrayInt = (int[]) o;
				} else if (this.clase.contains("long")) {
					this.arrayLong = (long[]) o;
				} else if (this.clase.contains("float")
						|| this.clase.contains("double")) {
					this.arrayDouble = (double[]) o;
				} else if (this.clase.contains("char")) {
					this.arrayChar = (char[]) o;
				} else if (this.clase.contains("String")) {
					this.arrayString = (String[]) o;
				} else if (this.clase.contains("boolean")) {
					this.arrayBoolean = (boolean[]) o;
				}
			}
		}
	}

	/**
	 * Crea una nueva estructura, dados los parámetros que definen la
	 * estructura.
	 * 
	 * @param tipo
	 *            Tipo de datos de la estructura.
	 * @param dim
	 *            Dimensiones de la estructura.
	 * @param valores
	 *            Valores de la estructura.
	 * @param tam1
	 *            Tamaño de la primera dimensión de la estructura.
	 * @param tam2
	 *            Tamaño de la segunda dimensión de la estructura.
	 */
	public Estructura(String tipo, String dim, String valores, String tam1,
			String tam2) {
		int dim1 = -1, dim2 = -1;
		this.clase = tipo;
		try {
			dim1 = Integer.parseInt(tam1);
			dim2 = Integer.parseInt(tam2);
		} catch (Exception e) {

		}

		try {
			this.dim = Integer.parseInt(dim);
		} catch (Exception e) {
			this.dim = 1;
		}

		if (this.esMatriz()) {
			if (tipo.contains("int")) {
				int val[] = ServiciosString.extraerValoresInt(valores, '|');
				this.matrizInt = new int[dim1][];
				int x = 0;
				for (int i = 0; i < this.matrizInt.length; i++) {
					this.matrizInt[i] = new int[dim2];
					for (int j = 0; j < this.matrizInt[i].length; j++, x++) {
						this.matrizInt[i][j] = val[x];
					}
				}
			} else if (tipo.contains("long")) {
				long val[] = ServiciosString.extraerValoresLong(valores, '|');
				this.matrizLong = new long[dim1][];
				int x = 0;
				for (int i = 0; i < this.matrizLong.length; i++) {
					this.matrizLong[i] = new long[dim2];
					for (int j = 0; j < this.matrizLong[i].length; j++, x++) {
						this.matrizLong[i][j] = val[x];
					}
				}
			} else if (tipo.contains("double")) {
				double val[] = ServiciosString.extraerValoresDouble(valores,
						'|');
				this.matrizDouble = new double[dim1][];
				int x = 0;
				for (int i = 0; i < this.matrizDouble.length; i++) {
					this.matrizDouble[i] = new double[dim2];
					for (int j = 0; j < this.matrizDouble[i].length; j++, x++) {
						this.matrizDouble[i][j] = val[x];
					}
				}
			} else if (tipo.contains("char")) {
				char val[] = ServiciosString.extraerValoresChar(valores, '|');
				this.matrizChar = new char[dim1][];
				int x = 0;
				for (int i = 0; i < this.matrizChar.length; i++) {
					this.matrizChar[i] = new char[dim2];
					for (int j = 0; j < this.matrizChar[i].length; j++, x++) {
						this.matrizChar[i][j] = val[x];
					}
				}
			} else if (tipo.contains("String")) {
				String val[] = ServiciosString.extraerValoresString(valores,
						'|');
				this.matrizString = new String[dim1][];
				int x = 0;
				for (int i = 0; i < this.matrizString.length; i++) {
					this.matrizString[i] = new String[dim2];
					for (int j = 0; j < this.matrizString[i].length; j++, x++) {
						this.matrizString[i][j] = new String(val[x]);
					}
				}
			} else if (tipo.contains("boolean")) {
				boolean val[] = ServiciosString.extraerValoresBoolean(valores,
						'|');
				this.matrizBoolean = new boolean[dim1][];
				int x = 0;
				for (int i = 0; i < this.matrizBoolean.length; i++) {
					this.matrizBoolean[i] = new boolean[dim2];
					for (int j = 0; j < this.matrizBoolean[i].length; j++, x++) {
						this.matrizBoolean[i][j] = val[x];
					}
				}
			}
		} else {
			if (tipo.contains("int")) {
				this.arrayInt = ServiciosString.extraerValoresInt(valores, '|');
			} else if (tipo.contains("long")) {
				this.arrayLong = ServiciosString.extraerValoresLong(valores,
						'|');
			} else if (tipo.contains("double")) {
				this.arrayDouble = ServiciosString.extraerValoresDouble(
						valores, '|');
			} else if (tipo.contains("char")) {
				this.arrayChar = ServiciosString.extraerValoresChar(valores,
						'|');
			} else if (tipo.contains("String")) {
				this.arrayString = ServiciosString.extraerValoresString(
						valores, '|');
			} else if (tipo.contains("boolean")) {
				this.arrayBoolean = ServiciosString.extraerValoresBoolean(
						valores, '|');
			}
		}

	}

	/**
	 * Determina si la estructura es una matriz.
	 * 
	 * @return true si la estructura es una matriz, false en caso contrario.
	 */
	public boolean esMatriz() {
		return this.dim == 2;
	}

	/**
	 * Determina si la estructura es un array.
	 * 
	 * @return true si la estructura es un array, false en caso contrario.
	 */
	public boolean esArray() {
		return this.dim == 1;
	}

	/**
	 * Devuelve el tamaño de las dimensiones de la estructura, si la estructura
	 * es un array, el segundo valor valdrá 0.
	 * 
	 * @return Tamaño de las dimensiones de la estructura.
	 */
	public int[] dimensiones() {
		int[] dimSalida;
		if (this.esMatriz()) {
			dimSalida = new int[2];
			if (this.matrizInt != null) {
				dimSalida[0] = this.matrizInt.length;
				if (dimSalida[0] != 0) {
					dimSalida[1] = this.matrizInt[0].length;
				} else {
					dimSalida[1] = 0;
				}
			} else if (this.matrizLong != null) {
				dimSalida[0] = this.matrizLong.length;
				if (dimSalida[0] != 0) {
					dimSalida[1] = this.matrizLong[0].length;
				} else {
					dimSalida[1] = 0;
				}
			} else if (this.matrizDouble != null) {
				dimSalida[0] = this.matrizDouble.length;
				if (dimSalida[0] != 0) {
					dimSalida[1] = this.matrizDouble[0].length;
				} else {
					dimSalida[1] = 0;
				}
			} else if (this.matrizChar != null) {
				dimSalida[0] = this.matrizChar.length;
				if (dimSalida[0] != 0) {
					dimSalida[1] = this.matrizChar[0].length;
				} else {
					dimSalida[1] = 0;
				}
			} else if (this.matrizString != null) {
				dimSalida[0] = this.matrizString.length;
				if (dimSalida[0] != 0) {
					dimSalida[1] = this.matrizString[0].length;
				} else {
					dimSalida[1] = 0;
				}
			} else // if (matrizBoolean!=null)
			{
				dimSalida[0] = this.matrizBoolean.length;
				if (dimSalida[0] != 0) {
					dimSalida[1] = this.matrizBoolean[0].length;
				} else {
					dimSalida[1] = 0;
				}
			}

		} else // es array
		{
			dimSalida = new int[1];
			if (this.arrayInt != null) {
				dimSalida[0] = this.arrayInt.length;
			} else if (this.arrayLong != null) {
				dimSalida[0] = this.arrayLong.length;
			} else if (this.arrayDouble != null) {
				dimSalida[0] = this.arrayDouble.length;
			} else if (this.arrayDouble != null) {
				dimSalida[0] = this.arrayDouble.length;
			} else if (this.arrayChar != null) {
				dimSalida[0] = this.arrayChar.length;
			} else if (this.arrayString != null) {
				dimSalida[0] = this.arrayString.length;
			} else if (this.arrayBoolean != null) {
				dimSalida[0] = this.arrayBoolean.length;
			}

		}
		return dimSalida;
	}

	/**
	 * Devuelve la estructura.
	 * 
	 * @return estructura.
	 */
	public Object getObjeto() {
		if (this.esMatriz()) {
			if (this.matrizInt != null) {
				return this.matrizInt;
			} else if (this.matrizLong != null) {
				return this.matrizLong;
			} else if (this.matrizDouble != null) {
				return this.matrizDouble;
			} else if (this.matrizChar != null) {
				return this.matrizChar;
			} else if (this.matrizString != null) {
				return this.matrizString;
			} else if (this.matrizBoolean != null) {
				return this.matrizBoolean;
			} else {
				System.out.println("Matriz null");
				return null;
			}
		} else {
			if (this.arrayInt != null) {
				return this.arrayInt;
			} else if (this.arrayLong != null) {
				return this.arrayLong;
			} else if (this.arrayDouble != null) {
				return this.arrayDouble;
			} else if (this.arrayDouble != null) {
				return this.arrayDouble;
			} else if (this.arrayChar != null) {
				return this.arrayChar;
			} else if (this.arrayString != null) {
				return this.arrayString;
			} else if (this.arrayBoolean != null) {
				return this.arrayBoolean;
			} else {
				System.out.println("Array null");
				return null;
			}
		}

	}

	/**
	 * Devuelve el tipo de la estructura.
	 * 
	 * @return tipo de la estructura.
	 */
	public Object getTipo() {
		if (this.clase == null) {
			return null;
		}

		if (this.clase.contains("byte") || this.clase.contains("short")
				|| this.clase.contains("int")) {
			return new Integer(0);
		} else if (this.clase.contains("long")) {
			return new Long(0);
		} else if (this.clase.contains("float")
				|| this.clase.contains("double")) {
			return new Double(0);
		} else if (this.clase.contains("char")) {
			return new Character(' ');
		} else if (this.clase.contains("String")) {
			return new String("");
		} else {
			return new Boolean(true);
		}
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es un array de
	 * enteros.
	 * 
	 * @param p
	 *            posición
	 * 
	 * @return valor
	 */
	public int posicArrayInt(int p) {
		return this.arrayInt[p];
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es un array de
	 * longs.
	 * 
	 * @param p
	 *            posición
	 * 
	 * @return valor
	 */
	public long posicArrayLong(int p) {
		return this.arrayLong[p];
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es un array de
	 * doubles.
	 * 
	 * @param p
	 *            posición
	 * 
	 * @return valor
	 */
	public double posicArrayDouble(int p) {
		return this.arrayDouble[p];
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es un array de
	 * chars.
	 * 
	 * @param p
	 *            posición
	 * 
	 * @return valor
	 */
	public char posicArrayChar(int p) {
		return this.arrayChar[p];
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es un array de
	 * Strings.
	 * 
	 * @param p
	 *            posición
	 * 
	 * @return valor
	 */
	public String posicArrayString(int p) {
		return this.arrayString[p];
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es un array de
	 * booleans.
	 * 
	 * @param p
	 *            posición
	 * 
	 * @return valor
	 */
	public boolean posicArrayBool(int p) {
		return this.arrayBoolean[p];
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es una matriz de
	 * enteros.
	 * 
	 * @param f
	 *            fila
	 * @param c
	 *            columna
	 * 
	 * @return valor
	 */
	public int posicMatrizInt(int f, int c) {
		return this.matrizInt[f][c];
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es una matriz de
	 * longs.
	 * 
	 * @param f
	 *            fila
	 * @param c
	 *            columna
	 * 
	 * @return valor
	 */
	public long posicMatrizLong(int f, int c) {
		return this.matrizLong[f][c];
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es una matriz de
	 * doubles.
	 * 
	 * @param f
	 *            fila
	 * @param c
	 *            columna
	 * 
	 * @return valor
	 */
	public double posicMatrizDouble(int f, int c) {
		return this.matrizDouble[f][c];
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es una matriz de
	 * chars.
	 * 
	 * @param f
	 *            fila
	 * @param c
	 *            columna
	 * 
	 * @return valor
	 */
	public char posicMatrizChar(int f, int c) {
		return this.matrizChar[f][c];
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es una matriz de
	 * Strings.
	 * 
	 * @param f
	 *            fila
	 * @param c
	 *            columna
	 * 
	 * @return valor
	 */
	public String posicMatrizString(int f, int c) {
		return this.matrizString[f][c];
	}

	/**
	 * Devuelve el valor de la posición p cuando la estructura es una matriz de
	 * booleans.
	 * 
	 * @param f
	 *            fila
	 * @param c
	 *            columna
	 * 
	 * @return valor
	 */
	public boolean posicMatrizBool(int f, int c) {
		return this.matrizBoolean[f][c];
	}

	/**
	 * Devuelve el tipo de los valores de la estructura.
	 * 
	 * @return tipo
	 */
	public String getClase() {
		return new String("" + this.clase);
	}

	/**
	 * Devuelve una representación de los valores de la estructura.
	 * 
	 * @return Representación de los valores de la estructura.
	 */
	public String getValor() {
		String salida = "";
		if (this.esMatriz()) {
			if (this.matrizInt != null) {
				for (int i = 0; i < this.matrizInt.length; i++) {
					for (int j = 0; j < this.matrizInt[i].length; j++) {
						salida = salida + this.matrizInt[i][j];
						if (i < (this.matrizInt.length - 1)
								|| j < (this.matrizInt[i].length - 1)) {
							salida = salida + "|";
						}
					}
				}
			} else if (this.matrizLong != null) {
				for (int i = 0; i < this.matrizLong.length; i++) {
					for (int j = 0; j < this.matrizLong[i].length; j++) {
						salida = salida + this.matrizLong[i][j];
						if (i < (this.matrizLong.length - 1)
								|| j < (this.matrizLong[i].length - 1)) {
							salida = salida + "|";
						}
					}
				}
			} else if (this.matrizDouble != null) {
				for (int i = 0; i < this.matrizDouble.length; i++) {
					for (int j = 0; j < this.matrizDouble[i].length; j++) {
						salida = salida + this.matrizDouble[i][j];
						if (i < (this.matrizDouble.length - 1)
								|| j < (this.matrizDouble[i].length - 1)) {
							salida = salida + "|";
						}
					}
				}
			} else if (this.matrizChar != null) {
				for (int i = 0; i < this.matrizChar.length; i++) {
					for (int j = 0; j < this.matrizChar[i].length; j++) {
						salida = salida + this.matrizChar[i][j];
						if (i < (this.matrizChar.length - 1)
								|| j < (this.matrizChar[i].length - 1)) {
							salida = salida + "|";
						}
					}
				}
			} else if (this.matrizString != null) {
				for (int i = 0; i < this.matrizString.length; i++) {
					for (int j = 0; j < this.matrizString[i].length; j++) {
						salida = salida + this.matrizString[i][j];
						if (i < (this.matrizString.length - 1)
								|| j < (this.matrizString[i].length - 1)) {
							salida = salida + "|";
						}
					}
				}
			} else {
				for (int i = 0; i < this.matrizBoolean.length; i++) {
					for (int j = 0; j < this.matrizBoolean[i].length; j++) {
						salida = salida + this.matrizBoolean[i][j];
						if (i < (this.matrizBoolean.length - 1)
								|| j < (this.matrizBoolean[i].length - 1)) {
							salida = salida + "|";
						}
					}
				}
			}
		} else {
			if (this.arrayInt != null) {
				for (int i = 0; i < this.arrayInt.length; i++) {
					salida = salida + this.arrayInt[i];
					if (i < (this.arrayInt.length - 1)) {
						salida = salida + "|";
					}
				}
			} else if (this.arrayLong != null) {
				for (int i = 0; i < this.arrayLong.length; i++) {
					salida = salida + this.arrayLong[i];
					if (i < (this.arrayLong.length - 1)) {
						salida = salida + "|";
					}
				}
			} else if (this.arrayDouble != null) {
				for (int i = 0; i < this.arrayDouble.length; i++) {
					salida = salida + this.arrayDouble[i];
					if (i < (this.arrayDouble.length - 1)) {
						salida = salida + "|";
					}
				}
			} else if (this.arrayDouble != null) {
				for (int i = 0; i < this.arrayDouble.length; i++) {
					salida = salida + this.arrayDouble[i];
					if (i < (this.arrayDouble.length - 1)) {
						salida = salida + "|";
					}
				}
			} else if (this.arrayChar != null) {
				for (int i = 0; i < this.arrayChar.length; i++) {
					salida = salida + this.arrayChar[i];
					if (i < (this.arrayChar.length - 1)) {
						salida = salida + "|";
					}
				}
			} else if (this.arrayString != null) {
				for (int i = 0; i < this.arrayString.length; i++) {
					salida = salida + this.arrayString[i];
					if (i < (this.arrayString.length - 1)) {
						salida = salida + "|";
					}
				}
			} else if (this.arrayBoolean != null) {
				for (int i = 0; i < this.arrayBoolean.length; i++) {
					salida = salida + this.arrayBoolean[i];
					if (i < (this.arrayBoolean.length - 1)) {
						salida = salida + "|";
					}
				}
			}
		}
		return salida;
	}

}