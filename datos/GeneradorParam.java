package datos;

import utilidades.ValorAleatorio;

/**
 * Devuelve en formato String un valor aleatorio válido según el tipo al que
 * pertenezca el parámetro
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class GeneradorParam {

	private String valor;
	private int valorInt;
	private long valorLong;
	private double valorDouble;
	private char valorChar;

	/**
	 * Construye una nueva instancia que contiene un valor entero aleatorio
	 * entre 1 y 10 (Válido para int, short y byte).
	 */
	public GeneradorParam() {
		this.valorInt = ValorAleatorio.generaInt(1, 10);
		this.valor = "" + this.valorInt;
	}

	/**
	 * Construye una nueva instancia que contiene un valor entero aleatorio
	 * entre los límites especificados (Válido para int, short y byte).
	 * 
	 * @param inf
	 *            Límite inferior
	 * @param sup
	 *            Límite superior
	 */
	public GeneradorParam(int inf, int sup) {
		this.valorInt = ValorAleatorio.generaInt(inf, sup);
		this.valor = "" + this.valorInt;
	}

	/**
	 * Construye una nueva instancia que contiene un valor long aleatorio entre
	 * los límites especificados.
	 * 
	 * @param inf
	 *            Límite inferior
	 * @param sup
	 *            Límite superior
	 */
	public GeneradorParam(long inf, long sup) {
		this.valorLong = ValorAleatorio.generaLong(inf, sup);
		this.valor = "" + this.valorLong;
	}

	/**
	 * Construye una nueva instancia que contiene un valor double aleatorio
	 * entre los límites especificados (Válido para float y double).
	 * 
	 * @param inf
	 *            Límite inferior
	 * @param sup
	 *            Límite superior
	 */
	public GeneradorParam(double inf, double sup) {
		this.valorDouble = ValorAleatorio.generaDouble(inf, sup);
		this.valor = "" + this.valorDouble;
	}

	/**
	 * Construye una nueva instancia que contiene un valor de un array de
	 * enteros aleatorio entre los límites especificados (Válido para int[],
	 * short[] y byte[]).
	 * 
	 * @param inf
	 *            Límite inferior
	 * @param sup
	 *            Límite superior
	 * @param dim1
	 *            Tamaño del array
	 * @param numOrden
	 *            1 para creciente, 2 para decreciente.
	 */
	public GeneradorParam(int inf, int sup, int dim1, int numOrden) {
		this.valor = "{ ";

		long vector[] = new long[dim1];
		for (int i = 0; i < dim1; i++) {
			vector[i] = ValorAleatorio.generaInt(inf, sup);
		}

		if (numOrden == 1) {
			vector = ordenCreciente(vector);
		} else if (numOrden == 2) {
			vector = ordenDecreciente(vector);
		}

		for (int i = 0; i < dim1; i++) {
			this.valor = this.valor + vector[i];
			if (i < (dim1 - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Construye una nueva instancia que contiene un valor de un array de longs
	 * aleatorio entre los límites especificados.
	 * 
	 * @param inf
	 *            Límite inferior
	 * @param sup
	 *            Límite superior
	 * @param dim1
	 *            Tamaño del array
	 * @param numOrden
	 *            1 para creciente, 2 para decreciente.
	 */
	public GeneradorParam(long inf, long sup, int dim1, int numOrden) {
		this.valor = "{ ";

		long vector[] = new long[dim1];
		for (int i = 0; i < dim1; i++) {
			vector[i] = ValorAleatorio.generaLong(inf, sup);
		}

		if (numOrden == 1) {
			vector = ordenCreciente(vector);
		} else if (numOrden == 2) {
			vector = ordenDecreciente(vector);
		}

		for (int i = 0; i < dim1; i++) {
			this.valor = this.valor + vector[i];
			if (i < (dim1 - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Construye una nueva instancia que contiene un valor de un array de
	 * doubles aleatorio entre los límites especificados (Válido para float[] y
	 * double[]).
	 * 
	 * @param inf
	 *            Límite inferior
	 * @param sup
	 *            Límite superior
	 * @param dim1
	 *            Tamaño del array
	 * @param numOrden
	 *            1 para creciente, 2 para decreciente.
	 */
	public GeneradorParam(double inf, double sup, int dim1, int numOrden) {
		this.valor = "{ ";

		double vector[] = new double[dim1];
		for (int i = 0; i < dim1; i++) {
			vector[i] = ValorAleatorio.generaDouble(inf, sup);
		}

		if (numOrden == 1) {
			vector = ordenCreciente(vector);
		} else if (numOrden == 2) {
			vector = ordenDecreciente(vector);
		}

		for (int i = 0; i < dim1; i++) {
			this.valor = this.valor + vector[i];
			if (i < (dim1 - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Construye una nueva instancia que contiene un valor de una matriz de
	 * enteros aleatorio entre los límites especificados (Válido para int[][],
	 * short[][] y byte[][]).
	 * 
	 * @param inf
	 *            Límite inferior
	 * @param sup
	 *            Límite superior
	 * @param dim1
	 *            Tamaño de la primera dimension
	 * @param dim2
	 *            Tamaño de la segunda dimension
	 * @param numOrden
	 *            1 para creciente, 2 para decreciente.
	 */
	public GeneradorParam(int inf, int sup, int dim1, int dim2, int numOrden) {
		this.valor = "{ ";
		for (int i = 0; i < dim1; i++) {
			this.valor = this.valor + "{ ";

			long vector[] = new long[dim2];
			for (int j = 0; j < dim2; j++) {
				vector[j] = ValorAleatorio.generaInt(inf, sup);
			}

			if (numOrden == 1) {
				vector = ordenCreciente(vector);
			} else if (numOrden == 2) {
				vector = ordenDecreciente(vector);
			}

			for (int j = 0; j < dim2; j++) {
				this.valor = this.valor + vector[j];
				if (j < (dim2 - 1)) {
					this.valor = this.valor + ", ";
				}
			}
			this.valor = this.valor + " }";
			if (i < (dim1 - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Construye una nueva instancia que contiene un valor de una matriz de
	 * longs aleatorio entre los límites especificados.
	 * 
	 * @param inf
	 *            Límite inferior
	 * @param sup
	 *            Límite superior
	 * @param dim1
	 *            Tamaño de la primera dimension
	 * @param dim2
	 *            Tamaño de la segunda dimension
	 * @param numOrden
	 *            1 para creciente, 2 para decreciente.
	 */
	public GeneradorParam(long inf, long sup, int dim1, int dim2, int numOrden) {
		this.valor = "{ ";
		for (int i = 0; i < dim1; i++) {
			this.valor = this.valor + "{ ";

			long vector[] = new long[dim2];
			for (int j = 0; j < dim2; j++) {
				vector[j] = ValorAleatorio.generaLong(inf, sup);
			}

			if (numOrden == 1) {
				vector = ordenCreciente(vector);
			} else if (numOrden == 2) {
				vector = ordenDecreciente(vector);
			}

			for (int j = 0; j < dim2; j++) {
				this.valor = this.valor + vector[j];
				if (j < (dim2 - 1)) {
					this.valor = this.valor + ", ";
				}
			}
			this.valor = this.valor + " }";
			if (i < (dim1 - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Construye una nueva instancia que contiene un valor de una matriz de
	 * doubles aleatorio entre los límites especificados (Válido para float[][]
	 * y double[][]).
	 * 
	 * @param inf
	 *            Límite inferior
	 * @param sup
	 *            Límite superior
	 * @param dim1
	 *            Tamaño de la primera dimension
	 * @param dim2
	 *            Tamaño de la segunda dimension
	 * @param numOrden
	 *            1 para creciente, 2 para decreciente.
	 */
	public GeneradorParam(double inf, double sup, int dim1, int dim2,
			int numOrden) {
		this.valor = "{ ";
		for (int i = 0; i < dim1; i++) {
			this.valor = this.valor + "{ ";

			double vector[] = new double[dim2];
			for (int j = 0; j < dim1; j++) {
				vector[j] = ValorAleatorio.generaDouble(inf, sup);
			}

			if (numOrden == 1) {
				vector = ordenCreciente(vector);
			} else if (numOrden == 2) {
				vector = ordenDecreciente(vector);
			}

			for (int j = 0; j < dim2; j++) {
				this.valor = this.valor + vector[j];
				if (j < (dim2 - 1)) {
					this.valor = this.valor + ", ";
				}
			}
			this.valor = this.valor + " }";
			if (i < (dim1 - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Construye una nueva instancia que contiene un char aleatorio.
	 */
	public GeneradorParam(Character c) {
		this.valorChar = ValorAleatorio.generaChar();
		this.valor = "\'" + this.valorChar + "\'";
	}

	/**
	 * Construye una nueva instancia que contiene un valor de un array de chars
	 * aleatorio.
	 * 
	 * @param dim
	 *            Tamaño del array
	 * @param numOrden
	 *            1 para creciente, 2 para decreciente.
	 */
	public GeneradorParam(Character c, int dim, int numOrden) {
		this.valor = "{ ";

		char vector[] = new char[dim];
		for (int i = 0; i < dim; i++) {
			vector[i] = ValorAleatorio.generaChar();
		}

		if (numOrden == 1) {
			vector = ordenCreciente(vector);
		} else if (numOrden == 2) {
			vector = ordenDecreciente(vector);
		}

		for (int i = 0; i < dim; i++) {
			this.valor = this.valor + "\'" + vector[i] + "\'";
			if (i < (dim - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Construye una nueva instancia que contiene un valor de una matriz de
	 * chars aleatorio.
	 * 
	 * @param dim1
	 *            Tamaño de la primera dimensión
	 * @param dim2
	 *            Tamaño de la segunda dimensión
	 * @param numOrden
	 *            1 para creciente, 2 para decreciente.
	 */
	public GeneradorParam(Character c, int dim1, int dim2, int numOrden) {
		this.valor = "{ ";
		for (int i = 0; i < dim1; i++) {
			this.valor = this.valor + "{ ";
			for (int j = 0; j < dim2; j++) {
				this.valor = this.valor + "\'" + ValorAleatorio.generaChar()
						+ "\'";
				if (j < (dim2 - 1)) {
					this.valor = this.valor + ", ";
				}
			}
			this.valor = this.valor + " }";

			if (i < (dim1 - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Construye una nueva instancia que contiene un boolean aleatorio.
	 */
	public GeneradorParam(boolean b) {
		this.valor = "" + ValorAleatorio.generaBoolean();
	}

	/**
	 * Construye una nueva instancia que contiene un valor de un array de
	 * booleans aleatorio.
	 * 
	 * @param dim
	 *            Tamaño del array
	 */
	public GeneradorParam(boolean b, int dim) {
		this.valor = "{ ";
		for (int i = 0; i < dim; i++) {
			this.valor = this.valor + ValorAleatorio.generaBoolean();
			if (i < (dim - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Construye una nueva instancia que contiene un valor de una matriz de
	 * booleans aleatorio.
	 * 
	 * @param dim1
	 *            Tamaño de la primera dimensión
	 * @param dim2
	 *            Tamaño de la segunda dimensión
	 */
	public GeneradorParam(boolean b, int dim1, int dim2) {
		this.valor = "{ ";
		for (int i = 0; i < dim1; i++) {
			this.valor = this.valor + "{ ";
			for (int j = 0; j < dim2; j++) {
				this.valor = this.valor + ValorAleatorio.generaBoolean();
				if (j < (dim2 - 1)) {
					this.valor = this.valor + ", ";
				}
			}
			this.valor = this.valor + " }";

			if (i < (dim1 - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Construye una nueva instancia que contiene un string aleatorio.
	 */
	public GeneradorParam(String s) {
		this.valor = "\"" + ValorAleatorio.generaString() + "\"";
	}

	/**
	 * Construye una nueva instancia que contiene un valor de un array de
	 * strings aleatorio.
	 * 
	 * @param dim
	 *            Tamaño del array
	 */
	public GeneradorParam(String s, int dim) {
		this.valor = "{ ";
		for (int i = 0; i < dim; i++) {
			this.valor = this.valor + "\"" + ValorAleatorio.generaString()
					+ "\"";
			if (i < (dim - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Construye una nueva instancia que contiene un valor de una matriz de
	 * strings aleatorio.
	 * 
	 * @param dim1
	 *            Tamaño de la primera dimensión
	 * @param dim2
	 *            Tamaño de la segunda dimensión
	 */
	public GeneradorParam(String s, int dim1, int dim2) {
		this.valor = "{ ";
		for (int i = 0; i < dim1; i++) {
			this.valor = this.valor + "{ ";
			for (int j = 0; j < dim2; j++) {
				this.valor = this.valor + "\"" + ValorAleatorio.generaString()
						+ "\"";
				if (j < (dim2 - 1)) {
					this.valor = this.valor + ", ";
				}
			}
			this.valor = this.valor + " }";

			if (i < (dim1 - 1)) {
				this.valor = this.valor + ", ";
			}
		}
		this.valor = this.valor + " }";
	}

	/**
	 * Devuelve un string con la representación del valor.
	 * 
	 * @return String con la representación del valor.
	 */
	public String getValor() {
		return this.valor;
	}

	/**
	 * Devuelve el valor entero generado.
	 * 
	 * @return valor entero generado.
	 */
	public int getValorInt() {
		return this.valorInt;
	}

	/**
	 * Devuelve el valor long generado.
	 * 
	 * @return valor long generado.
	 */
	public long getValorLong() {
		return this.valorLong;
	}

	/**
	 * Devuelve el valor double generado.
	 * 
	 * @return valor double generado.
	 */
	public double getValorDouble() {
		return this.valorDouble;
	}

	/**
	 * Devuelve el valor char generado.
	 * 
	 * @return valor char generado.
	 */
	public char getValorChar() {
		return this.valorChar;
	}

	/**
	 * Ordena el array en orden creciente pasado por parámetro.
	 * 
	 * @param v
	 *            array de entrada
	 * 
	 * @return array ordenado
	 */
	private static long[] ordenCreciente(long v[]) {
		long array[] = new long[v.length];

		if (v.length > 2) {
			int div = (v.length / 2);

			long array1[] = new long[div];
			long array2[] = new long[v.length - div];

			for (int i = 0; i < div; i++) {
				array1[i] = v[i];
			}
			for (int i = div; i < v.length; i++) {
				array2[i - div] = v[i];
			}

			array1 = ordenCreciente(array1);
			array2 = ordenCreciente(array2);
			array = combinar(array1, array2, 1);
		} else if (v.length == 2 && v[0] > v[1]) {
			long posAux = v[0];
			array[0] = v[v.length - 1];
			array[v.length - 1] = posAux;
		} else if (v.length == 2 && v[0] <= v[1]) {
			array[0] = v[0];
			array[1] = v[1];
		} else {
			array[0] = v[0];
		}

		return array;
	}

	/**
	 * Ordena el array en orden decreciente pasado por parámetro.
	 * 
	 * @param v
	 *            array de entrada
	 * 
	 * @return array ordenado
	 */
	private static long[] ordenDecreciente(long v[]) {
		long array[] = new long[v.length];

		if (v.length > 2) {
			int div = (v.length / 2);

			long array1[] = new long[div];
			long array2[] = new long[v.length - div];

			for (int i = 0; i < div; i++) {
				array1[i] = v[i];
			}
			for (int i = div; i < v.length; i++) {
				array2[i - div] = v[i];
			}

			array1 = ordenDecreciente(array1);
			array2 = ordenDecreciente(array2);
			array = combinar(array1, array2, 2);
		} else if (v.length == 2 && v[0] < v[1]) {
			long posAux = v[0];
			array[0] = v[v.length - 1];
			array[v.length - 1] = posAux;
		} else if (v.length == 2 && v[0] >= v[1]) {
			array[0] = v[0];
			array[1] = v[1];
		} else {
			array[0] = v[0];
		}
		return array;
	}

	/**
	 * Combina varios arrays en el orden especificado.
	 * 
	 * @param a1
	 *            array de entrada
	 * @param a2
	 *            array de entrada
	 * @param orden
	 *            1 para creciente, 2 para decreciente.
	 * 
	 * @return array combinado
	 */
	private static long[] combinar(long a1[], long a2[], int orden) {
		long array[] = new long[a1.length + a2.length];
		int p1 = 0, p2 = 0;

		if (orden == 1) {
			for (int i = 0; i < array.length; i++) {
				if (p2 >= a2.length || (p1 < a1.length && a1[p1] <= a2[p2])) {
					array[i] = a1[p1];
					p1++;
				} else if (p1 >= a1.length
						|| (p2 < a2.length && a1[p1] >= a2[p2])) {
					array[i] = a2[p2];
					p2++;
				}
			}
		} else {
			for (int i = 0; i < array.length; i++) {
				if (p2 >= a2.length || (p1 < a1.length && a1[p1] >= a2[p2])) {
					array[i] = a1[p1];
					p1++;
				} else if (p1 >= a1.length
						|| (p2 < a2.length && a1[p1] <= a2[p2])) {
					array[i] = a2[p2];
					p2++;
				}
			}
		}

		return array;
	}

	/**
	 * Ordena el array en orden creciente pasado por parámetro.
	 * 
	 * @param v
	 *            array de entrada
	 * 
	 * @return array ordenado
	 */
	private static double[] ordenCreciente(double v[]) {
		double array[] = new double[v.length];

		if (v.length > 2) {
			int div = (v.length / 2);

			double array1[] = new double[div];
			double array2[] = new double[v.length - div];

			for (int i = 0; i < div; i++) {
				array1[i] = v[i];
			}
			for (int i = div; i < v.length; i++) {
				array2[i - div] = v[i];
			}

			array1 = ordenCreciente(array1);
			array2 = ordenCreciente(array2);
			array = combinar(array1, array2, 1);
		} else if (v.length == 2 && v[0] > v[1]) {
			double posAux = v[0];
			array[0] = v[v.length - 1];
			array[v.length - 1] = posAux;
		} else if (v.length == 2 && v[0] <= v[1]) {
			array[0] = v[0];
			array[1] = v[1];
		} else {
			array[0] = v[0];
		}

		return array;
	}

	/**
	 * Ordena el array en orden decreciente pasado por parámetro.
	 * 
	 * @param v
	 *            array de entrada
	 * 
	 * @return array ordenado
	 */
	private static double[] ordenDecreciente(double v[]) {
		double array[] = new double[v.length];

		if (v.length > 2) {
			int div = (v.length / 2);

			double array1[] = new double[div];
			double array2[] = new double[v.length - div];

			for (int i = 0; i < div; i++) {
				array1[i] = v[i];
			}
			for (int i = div; i < v.length; i++) {
				array2[i - div] = v[i];
			}

			array1 = ordenDecreciente(array1);
			array2 = ordenDecreciente(array2);
			array = combinar(array1, array2, 2);
		} else if (v.length == 2 && v[0] < v[1]) {
			double posAux = v[0];
			array[0] = v[v.length - 1];
			array[v.length - 1] = posAux;
		} else if (v.length == 2 && v[0] >= v[1]) {
			array[0] = v[0];
			array[1] = v[1];
		} else {
			array[0] = v[0];
		}
		return array;
	}

	/**
	 * Combina varios arrays en el orden especificado.
	 * 
	 * @param a1
	 *            array de entrada
	 * @param a2
	 *            array de entrada
	 * @param orden
	 *            1 para creciente, 2 para decreciente.
	 * 
	 * @return array combinado
	 */
	private static double[] combinar(double a1[], double a2[], int orden) {
		double array[] = new double[a1.length + a2.length];
		int p1 = 0, p2 = 0;

		if (orden == 1) {
			for (int i = 0; i < array.length; i++) {
				if (p2 >= a2.length || (p1 < a1.length && a1[p1] <= a2[p2])) {
					array[i] = a1[p1];
					p1++;
				} else if (p1 >= a1.length
						|| (p2 < a2.length && a1[p1] >= a2[p2])) {
					array[i] = a2[p2];
					p2++;
				}
			}
		} else {
			for (int i = 0; i < array.length; i++) {
				if (p2 >= a2.length || (p1 < a1.length && a1[p1] >= a2[p2])) {
					array[i] = a1[p1];
					p1++;
				} else if (p1 >= a1.length
						|| (p2 < a2.length && a1[p1] <= a2[p2])) {
					array[i] = a2[p2];
					p2++;
				}
			}
		}

		return array;
	}

	/**
	 * Ordena el array en orden creciente pasado por parámetro.
	 * 
	 * @param v
	 *            array de entrada
	 * 
	 * @return array ordenado
	 */
	private static char[] ordenCreciente(char v[]) {
		char array[] = new char[v.length];

		if (v.length > 2) {
			int div = (v.length / 2);

			char array1[] = new char[div];
			char array2[] = new char[v.length - div];

			for (int i = 0; i < div; i++) {
				array1[i] = v[i];
			}
			for (int i = div; i < v.length; i++) {
				array2[i - div] = v[i];
			}

			array1 = ordenCreciente(array1);
			array2 = ordenCreciente(array2);
			array = combinar(array1, array2, 1);
		} else if (v.length == 2 && v[0] > v[1]) {
			char posAux = v[0];
			array[0] = v[v.length - 1];
			array[v.length - 1] = posAux;
		} else if (v.length == 2 && v[0] <= v[1]) {
			array[0] = v[0];
			array[1] = v[1];
		} else {
			array[0] = v[0];
		}

		return array;
	}

	/**
	 * Ordena el array en orden decreciente pasado por parámetro.
	 * 
	 * @param v
	 *            array de entrada
	 * 
	 * @return array ordenado
	 */
	private static char[] ordenDecreciente(char v[]) {
		char array[] = new char[v.length];

		if (v.length > 2) {
			int div = (v.length / 2);

			char array1[] = new char[div];
			char array2[] = new char[v.length - div];

			for (int i = 0; i < div; i++) {
				array1[i] = v[i];
			}
			for (int i = div; i < v.length; i++) {
				array2[i - div] = v[i];
			}

			array1 = ordenDecreciente(array1);
			array2 = ordenDecreciente(array2);
			array = combinar(array1, array2, 2);
		} else if (v.length == 2 && v[0] < v[1]) {
			char posAux = v[0];
			array[0] = v[v.length - 1];
			array[v.length - 1] = posAux;
		} else if (v.length == 2 && v[0] >= v[1]) {
			array[0] = v[0];
			array[1] = v[1];
		} else {
			array[0] = v[0];
		}
		return array;
	}

	/**
	 * Combina varios arrays en el orden especificado.
	 * 
	 * @param a1
	 *            array de entrada
	 * @param a2
	 *            array de entrada
	 * @param orden
	 *            1 para creciente, 2 para decreciente.
	 * 
	 * @return array combinado
	 */
	private static char[] combinar(char a1[], char a2[], int orden) {
		char array[] = new char[a1.length + a2.length];
		int p1 = 0, p2 = 0;

		if (orden == 1) {
			for (int i = 0; i < array.length; i++) {
				if (p2 >= a2.length || (p1 < a1.length && a1[p1] <= a2[p2])) {
					array[i] = a1[p1];
					p1++;
				} else if (p1 >= a1.length
						|| (p2 < a2.length && a1[p1] >= a2[p2])) {
					array[i] = a2[p2];
					p2++;
				}
			}
		} else {
			for (int i = 0; i < array.length; i++) {
				if (p2 >= a2.length || (p1 < a1.length && a1[p1] >= a2[p2])) {
					array[i] = a1[p1];
					p1++;
				} else if (p1 >= a1.length
						|| (p2 < a2.length && a1[p1] <= a2[p2])) {
					array[i] = a2[p2];
					p2++;
				}
			}
		}

		return array;
	}
}
