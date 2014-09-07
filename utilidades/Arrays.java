package utilidades;

/**
 * Clase de utilidades para operaciones con Arrays.
 */
public class Arrays {

	/**
	 * Permite consultar si un array de enteros contiene un valor concreto.
	 * 
	 * @param n
	 *            valor a consultar.
	 * @param array
	 *            Array de entrada.
	 * 
	 * @return True si el array contiene el valor, false en caso contrario.
	 */
	public static boolean contiene(int n, int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == n) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Permite consultar si un array de Strings contiene un valor concreto.
	 * 
	 * @param n
	 *            valor a consultar.
	 * @param array
	 *            Array de entrada.
	 * 
	 * @return True si el array contiene el valor, false en caso contrario.
	 */
	public static boolean contiene(String n, String[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null && array[i].equals(n)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Permite añadir un valor al final de un array de enteros.
	 * 
	 * @param n
	 *            valor a introducir.
	 * @param array
	 *            Array de entrada.
	 * 
	 * @return Array de salida, con el valor añadido.
	 */
	public static int[] insertar(int n, int[] array) {
		int[] arrayN = new int[array.length + 1];

		for (int i = 0; i < array.length; i++) {
			arrayN[i] = array[i];
		}

		arrayN[arrayN.length - 1] = n;
		return arrayN;
	}
}
