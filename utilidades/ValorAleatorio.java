package utilidades;

import java.util.Random;

/**
 * Genera n�meros aleatorios cuyo valor absoluto es menor o igual que un valor
 * limite dado. Puede generar s�lo valores positivos
 * 
 * @author Antonio P�rez Carrasco
 * @version 2006-2007
 */
public class ValorAleatorio {

	private static Random r = new Random();

	/**
	 * Devuelve un n�mero aleatorio entre 0 y el m�ximo especificado.
	 * 
	 * @param x
	 *            Limite superior del valor.
	 * 
	 * @return N�mero aleatorio entre 0 y x.
	 */
	private static int generaInt(int x) {
		if (x == 2147483647) {
			x = -1;
		}
		return r.nextInt(x + 1);
	}

	/**
	 * Devuelve un n�mero de tipo long aleatorio.
	 * 
	 * @return N�mero de tipo long aleatorio.
	 */
	private static long generaLong() {
		return r.nextLong();
	}

	/**
	 * Devuelve un n�mero de tipo float aleatorio.
	 * 
	 * @return N�mero de tipo float aleatorio.
	 */
	private static float generaFloat() {
		return r.nextInt();
	}

	/**
	 * Devuelve un n�mero de tipo double aleatorio.
	 * 
	 * @return N�mero de tipo double aleatorio.
	 */
	private static double generaDouble() {
		return r.nextInt();
	}

	/**
	 * Dado un n�mero entero, devuelve un car�cter.
	 * 
	 * @param x
	 *            N�mero entero.
	 * 
	 * @return Car�cter.
	 */
	private static String caracter(int x) {
		if (x < 0) {
			x = x * (-1);
		}
		x = x % 37;

		switch (x) {
		case 0:
			return "a";
		case 1:
			return "b";
		case 2:
			return "c";
		case 3:
			return "d";
		case 4:
			return "e";
		case 5:
			return "f";
		case 6:
			return "g";
		case 7:
			return "h";
		case 8:
			return "i";
		case 9:
			return "j";
		case 10:
			return "k";
		case 11:
			return "l";
		case 12:
			return "m";
		case 13:
			return "n";
		case 14:
			return "�";
		case 15:
			return "o";
		case 16:
			return "p";
		case 17:
			return "q";
		case 18:
			return "r";
		case 19:
			return "s";
		case 20:
			return "t";
		case 21:
			return "u";
		case 22:
			return "v";
		case 23:
			return "w";
		case 24:
			return "x";
		case 25:
			return "y";
		case 26:
			return "z";
		case 27:
			return "0";
		case 28:
			return "1";
		case 29:
			return "2";
		case 30:
			return "3";
		case 31:
			return "4";
		case 32:
			return "5";
		case 33:
			return "6";
		case 34:
			return "7";
		case 35:
			return "8";
		case 36:
			return "9";
		}
		return "a";
	}

	/**
	 * Genera un valor aleatorio entero entre un m�nimo y m�ximo especificados.
	 * 
	 * @param min
	 *            Valor m�nimo.
	 * @param max
	 *            Valor m�ximo.
	 * 
	 * @return Valor aleatorio entre el m�nimo y el m�ximo.
	 */
	public static int generaInt(int min, int max) {
		int x;

		if (max < min) {
			max = max + min;
			min = max;
			max = max - min;
		}
		if (max == min) {
			return max;
		}

		do {
			x = generaInt(Math.max(Math.abs(max), Math.abs(min)));
			if (generaBoolean() && max != (-1)) {
				x = x % (max + 1);
			} else if (generaBoolean() && min != (-1)) {
				x = ((x % (min + 1)) * (-1)) - 1;
			}

		} while (x < min || x > max);
		return x;

	}

	/**
	 * Genera un valor aleatorio short entre un m�nimo y m�ximo especificados.
	 * 
	 * @param min
	 *            Valor m�nimo.
	 * @param max
	 *            Valor m�ximo.
	 * 
	 * @return Valor aleatorio entre el m�nimo y el m�ximo.
	 */
	public static short generaShort(int min, int max) {
		return (short) generaInt(min, max);
	}

	/**
	 * Genera un valor aleatorio byte entre un m�nimo y m�ximo especificados.
	 * 
	 * @param min
	 *            Valor m�nimo.
	 * @param max
	 *            Valor m�ximo.
	 * 
	 * @return Valor aleatorio entre el m�nimo y el m�ximo.
	 */
	public static byte generaByte(int min, int max) {
		return (byte) generaInt(min, max);
	}

	/**
	 * Genera un valor aleatorio long entre un m�nimo y m�ximo especificados.
	 * 
	 * @param min
	 *            Valor m�nimo.
	 * @param max
	 *            Valor m�ximo.
	 * 
	 * @return Valor aleatorio entre el m�nimo y el m�ximo.
	 */
	public static long generaLong(long min, long max) {
		long x;

		if (max < min) {
			max = max + min;
			min = max;
			max = max - min;
		}
		if (max == min) {
			return max;
		}

		do {
			x = generaLong();
			if (generaBoolean() && max != (-1)) {
				x = x % (max + 1);
			} else if (generaBoolean() && min != (-1)) {
				x = ((x % (min + 1)) * (-1)) - 1;
			}

		} while (x < min || x > max);
		return x;
	}

	/**
	 * Genera un valor aleatorio double entre un m�nimo y m�ximo especificados.
	 * 
	 * @param min
	 *            Valor m�nimo.
	 * @param max
	 *            Valor m�ximo.
	 * 
	 * @return Valor aleatorio entre el m�nimo y el m�ximo.
	 */
	public static double generaDouble(double min, double max) {
		double x;
		boolean limNegativos = false;

		if (max < min) {
			max = max + min;
			min = max;
			max = max - min;
		}
		if (max == min) {
			return max;
		}

		if (max < 0 && min < 0) {
			limNegativos = true;
			double aux = max;
			max = min * (-1);
			min = aux * (-1);
		}

		do {
			int contador = 0;
			x = generaDouble();
			x = truncarDoble(x);

			while (x > max && contador < 10) {
				x = x / 10;
				contador++;
			}
			while (x < min && contador < 10) {
				x = x * 10;
				contador++;
			}

			if (generaBoolean()) {
				x = x * (-1);
			}

		} while (x < min || x > max);

		if (generaBoolean()) {
			if (truncarDoble((x * 1000)) > min
					&& truncarDoble((x * 1000)) < max && generaBoolean()) {
				x = x * 1000;
			}
			if (truncarDoble((x * 100)) > min && truncarDoble((x * 100)) < max
					&& generaBoolean()) {
				x = x * 100;
			}
			if (truncarDoble((x * 10)) > min && truncarDoble((x * 10)) < max
					&& generaBoolean()) {
				x = x * 10;
			}
		} else if (generaBoolean()) {
			if (truncarDoble((x / 1000)) < max
					&& truncarDoble((x / 1000)) > min && generaBoolean()) {
				x = x / 1000;
			}
			if (truncarDoble((x / 100)) < max && truncarDoble((x / 100)) > min
					&& generaBoolean()) {
				x = x / 100;
			}
			if (truncarDoble((x / 10)) < max && truncarDoble((x / 10)) > min
					&& generaBoolean()) {
				x = x / 10;
			}
		}

		if (limNegativos) {
			x = x * (-1); // hacemos valor negativo
		}
		x = truncarDoble(x);

		return x;
	}

	/**
	 * Trunca un valor double eliminando la representaci�n exponencial de
	 * decimales.
	 * 
	 * @param x
	 *            valor double.
	 * 
	 * @return Valor double truncado.
	 */
	private static double truncarDoble(double x) {
		Double xx = new Double(x);
		String numero = xx.toString();

		if (numero.contains("E")) {
			numero = numero.substring(0, numero.indexOf(('E')));
		}

		x = Double.parseDouble(numero);
		return x;
	}

	/**
	 * Genera un valor boolean aleatorio.
	 * 
	 * @return Valor boolean aleatorio.
	 */
	public static boolean generaBoolean() {
		return r.nextBoolean();
	}

	/**
	 * Genera un string aleatorio con la longitud especificada.
	 * 
	 * @param longitud
	 *            Longitud del string.
	 * 
	 * @return String aleatorio.
	 */
	public static String generaString(int longitud) {
		String s = "";

		if (longitud > 50) {
			longitud = 50;
		}

		for (int i = 0; i < longitud; i++) {
			s = s + caracter(generaInt(1, 37));
		}

		return s;
	}

	/**
	 * Genera un String aleatorio de tama�o entre 3 y 10.
	 * 
	 * @return String aleatorio
	 */
	public static String generaString() {
		return generaString(generaInt(3, 10));
	}

	/**
	 * Genera un char aleatorio.
	 * 
	 * @return Char aleatorio.
	 */
	public static char generaChar() {
		return generaString(1).charAt(0);
	}
}