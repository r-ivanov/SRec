package datos;

import utilidades.ServiciosString;

/**
 * Crea instancias de objetos que serán usados como parámetros en la ejecución
 * del método que se visualice
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class GestorParametros {

	private static boolean depurar = false;

	/**
	 * Transforma la cadena de caracteres leída en el Cuadro de Parámetros en un
	 * parámetro usable por la aplicación
	 * 
	 * @param s
	 *            cadena de caracteres escrita por el usuario en el cuadro de
	 *            texto correspondiente para este parámetro
	 * @param c
	 *            clase a la que pertenecerá el objeto que se va a devolver
	 * @return objeto que representa el parámetro del cual se pasó la cadena de
	 *         caracteres escrita desde teclado
	 */
	public static Object asignarParam(String s, Class c) throws Exception {
		Object o = null;

		if (depurar) {
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		}

		if (!(c.getCanonicalName().contains("[]"))) {
			if (c.getCanonicalName().equals("int")
					|| c.getCanonicalName().contains("Integer")) // Si es
				// integer
			{
				int entero = Integer.parseInt(s);
				o = entero;
				if (depurar) {
					System.out.println("Asignamos integer:" + o);
				}
			} else if (c.getCanonicalName().equals("char")
					|| c.getCanonicalName().contains("Char")) // Si es char
			{
				// Internamente, quitamos las comillas simples a los caracteres
				s = s.replace("\'", "");

				char caracter = s.charAt(0);
				o = caracter;
				if (s.length() > 1
						|| (!ServiciosString.esLetra(s.charAt(0)) && !ServiciosString
								.esNumero(s.charAt(0)))) {
					o = null;
				}
				if (depurar) {
					System.out.println("Asignamos char:" + o);
				}
			} else if (c.getCanonicalName().equals("long")
					|| c.getCanonicalName().contains("Long")) // Si es long
			{
				long largo = Long.parseLong(s);
				o = largo;
				if (depurar) {
					System.out.println("Asignamos long:" + o);
				}
			} else if (c.getCanonicalName().equals("double")
					|| c.getCanonicalName().contains("Double")) // Si es double
			{
				double doble = Double.parseDouble(s);
				o = doble;
				if (depurar) {
					System.out.println("Asignamos doble:" + o);
				}
			} else if (c.getCanonicalName().equals("short")
					|| c.getCanonicalName().contains("Short")) // Si es short
			{
				short corto = Short.parseShort(s);
				o = corto;
				if (depurar) {
					System.out.println("Asignamos corto:" + o);
				}
			} else if (c.getCanonicalName().equals("float")
					|| c.getCanonicalName().contains("Float")) // Si es float
			{
				float flotante = Float.parseFloat(s);
				o = flotante;
				if (depurar) {
					System.out.println("Asignamos flotante:" + o);
				}
			} else if (c.getCanonicalName().equals("byte")
					|| c.getCanonicalName().contains("Byte")) // Si es byte
			{
				byte unByte = Byte.parseByte(s);
				o = unByte;
				if (depurar) {
					System.out.println("Asignamos byte:" + o);
				}
			} else if (c.getCanonicalName().equals("boolean")
					|| c.getCanonicalName().contains("Boolean")) // Si es
				// boolean
			{
				boolean unBooleano;// =Boolean.parseBoolean(s);
				if (s.toLowerCase().equals("true")) {
					unBooleano = true;
					o = unBooleano;
				} else if (s.toLowerCase().equals("false")) {
					unBooleano = false;
					o = unBooleano;
				}
				if (depurar) {
					System.out.println("Asignamos boolean:" + o);
				}
			} else if (c.getCanonicalName().equals("java.lang.String")) // Si es
				// String
			{
				// Internamente, quitamos las comillas dobles a las cadenas
				s = s.replace("\"", "");

				if (esStringCorrecto(s)) {
					o = s;
				}
				if (depurar) {
					System.out.println("Asignamos string:" + o);
				}
			}
		} else if (c.getCanonicalName().contains("int[]")) // Si es int[]
		{
			if (depurar) {
				System.out.println("CuadroParam.asignarParam(): int[]");
			}
			if (c.getCanonicalName().contains("[][]")) {
				o = valoresArrayArray(s, 1);
				if (depurar && o == null) {
					System.out.println("'o' null, parametro vacio");
				}
			} else {
				int array[] = valoresIntArray(s);
				o = array;
			}
		} else if (c.getCanonicalName().contains("short[]")) // Si es short[]
		{
			if (depurar) {
				System.out.println("CuadroParam.asignarParam(): short[]");
			}
			if (c.getCanonicalName().contains("[][]")) {
				o = valoresArrayArray(s, 2);
			} else {
				short array[] = valoresShortArray(s);
				o = array;
			}
		} else if (c.getCanonicalName().contains("long[]")) // Si es long[]
		{
			if (depurar) {
				System.out.println("CuadroParam.asignarParam(): long[]");
			}
			if (c.getCanonicalName().contains("[][]")) {
				o = valoresArrayArray(s, 3);
			} else {
				long array[] = valoresLongArray(s);
				o = array;
			}
		}

		else if (c.getCanonicalName().contains("double[]")) // Si es double[]
		{
			if (depurar) {
				System.out.println("CuadroParam.asignarParam(): double[]");
			}
			if (c.getCanonicalName().contains("[][]")) {
				o = valoresArrayArray(s, 4);
			} else {
				double array[] = valoresDoubleArray(s);
				o = array;
			}
		} else if (c.getCanonicalName().contains("float[]")) // Si es float[]
		{
			if (depurar) {
				System.out.println("CuadroParam.asignarParam(): float[]");
			}
			if (c.getCanonicalName().contains("[][]")) {
				o = valoresArrayArray(s, 5);
			} else {
				float array[] = valoresFloatArray(s);
				o = array;
			}
		} else if (c.getCanonicalName().contains("byte[]")) // Si es byte[]
		{
			if (depurar) {
				System.out.println("CuadroParam.asignarParam(): byte[]");
			}
			if (c.getCanonicalName().contains("[][]")) {
				o = valoresArrayArray(s, 6);
			} else {
				float array[] = valoresFloatArray(s);
				o = array;
			}
		} else if (c.getCanonicalName().contains("java.lang.String[]")) // Si es
			// String[]
		{
			if (depurar) {
				System.out.println("CuadroParam.asignarParam(): String[]");
			}
			if (c.getCanonicalName().contains("[][]")) {
				o = valoresArrayArray(s, 8);
			} else {
				String array[] = valoresStringArray(s);
				o = (array);
			}
		} else if (c.getCanonicalName().contains("char[]")) // Si es char[]
		{
			if (depurar) {
				System.out.println("CuadroParam.asignarParam(): char[]");
			}
			if (c.getCanonicalName().contains("[][]")) {
				o = valoresArrayArray(s, 7);
			} else {
				char arrayCaracteres[] = valoresCharArray(s);
				o = arrayCaracteres;
			}
		} else if (c.getCanonicalName().contains("boolean[]")) // Si es
			// boolean[]
		{
			if (depurar) {
				System.out.println("CuadroParam.asignarParam(): boolean[]");
			}
			if (c.getCanonicalName().contains("[][]")) {
				o = valoresArrayArray(s, 9);
			} else {
				boolean arrayBooleanos[] = valoresBooleanArray(s);
				o = arrayBooleanos;
			}
		} else if (depurar) {
			System.out.println("No asignamos ???????? a 'o'");
		}

		if (o == null) {
			if (depurar) {
				System.out
				.println("CuadroParam.asignarParam va a devolver null");
			}
			Exception eee = new Exception("Error en array");
			throw eee;
		} else {
			if (depurar) {
				System.out
				.println("CuadroParam.asignarParam NO va a devolver null");
			}
		}

		return o;
	}

	/**
	 * Revisa si la cadena dada sólo tiene caracteres válidos
	 * 
	 * @param s
	 *            cadena de caracteres que se comprobará
	 * @return true si la cadena sólo tiene caracteres válidos
	 */
	private static boolean tieneSoloCaracteres(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ' ' && s.charAt(i) != '{' && s.charAt(i) != '}'
					&& !ServiciosString.esLetra(s.charAt(i))
					&& s.charAt(i) != ','
					&& !ServiciosString.esNumero(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Revisa si la cadena dada sólo tiene caracteres numéricos
	 * 
	 * @param s
	 *            cadena de caracteres que se comprobará
	 * @return true si la cadena sólo tiene caracteres numéricos
	 */
	private static boolean tieneSoloNumeros(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ' ' && s.charAt(i) != '-' && s.charAt(i) != '{'
					&& s.charAt(i) != '}' && s.charAt(i) != '.'
					&& s.charAt(i) != ','
					&& !ServiciosString.esNumero(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Revisa si la cadena tiene debidamente ubicadas las llaves de inicio y
	 * fin. Ya se ha comprobado que 's' sólo tiene números, comas, puntos,
	 * espacios y llaves
	 * 
	 * @param s
	 *            cadena de caracteres que se comprobará
	 * @return true si la cadena tiene debidamente ubicadas las llaves de inicio
	 *         y fin.
	 */
	private static boolean tieneLlaves(String s) {
		if (s.contains("{") && s.contains("}")) {
			boolean llave = true;
			boolean encontrada = false;
			int i = 0;
			do {
				while (s.charAt(i) == ' ') {
					i++;
				}
				if (ServiciosString.esNumero(s.charAt(i)) || s.charAt(i) == ','
						|| s.charAt(i) == '.') {
					llave = false;
				} else if (s.charAt(i) == '{') {
					llave = true;
					encontrada = true;
				}
				i++;
			} while (i < s.length() && !encontrada && !llave);
			encontrada = false;
			if (llave) // Si tiene llave inicial ahora comprobamos la llave
				// final
			{
				i = s.length() - 1;
				do {
					if (ServiciosString.esNumero(s.charAt(i))
							|| s.charAt(i) == ',' || s.charAt(i) == '.') {
						llave = false;
					} else if (s.charAt(i) == '}') {
						llave = true;
						encontrada = true;
					}
					i--;
				} while (i < s.length() && llave && !encontrada);
			}
			if (llave) {
				if (depurar) {
					System.out.println(" 2. Tiene llaves");
				}
			}
			return llave;
		} else {
			return false;
		}
	}

	/**
	 * Comprueba que el contenido está escrito correctamente. Suponemos que
	 * posición de llaves es correcta: "  {  ... } ".
	 * 
	 * @param s
	 *            cadena de caracteres que se comprobará
	 * @return true si el contenido está escrito correctamente.
	 */
	private static boolean tieneSecuenciaCorrecta(String s) {
		int i = 0;

		while (s.charAt(i) != '{') {
			i++;
		}
		i++;

		while (i < s.length() && s.charAt(i) != '}') {
			while (i < s.length() && s.charAt(i) == ' ') {
				i++;
			}
			if (i < s.length() && s.charAt(i) == '-') {
				i++;
			}
			if (i < s.length() && !ServiciosString.esNumero(s.charAt(i))) {
				if (depurar) {
					System.out.println("false 1");
				}
				return false;
			}
			while (i < s.length() && ServiciosString.esNumero(s.charAt(i))) {
				i++;
			}
			if (i < s.length() && s.charAt(i) == '.') {
				i++;
				if (!ServiciosString.esNumero(s.charAt(i))) {
					return false;
				}
				while (i < s.length() && ServiciosString.esNumero(s.charAt(i))) {
					i++;
				}
			}
			if (i < s.length() && s.charAt(i) != ' ' && s.charAt(i) != ','
					&& s.charAt(i) != '}') {
				if (depurar) {
					System.out.println("false 2");
				}
				return false;
			}
			if (i < s.length() && s.charAt(i) == '}') {
				i++;
				while (i < s.length() && s.charAt(i) == ' ') {
					i++;
				}
				if (i < s.length() && s.charAt(i) != ' ') {
					if (depurar) {
						System.out.println("false 3");
					}
					return false;
				}
				if (i == s.length()) {
					if (depurar) {
						System.out.println(" 3. Tiene secuencia correcta (A1)");
					}
					return true;
				}
			} else if (i < s.length() && s.charAt(i) == ',') {
				i++;
			} else if (i < s.length() && s.charAt(i) == ' ') {
				while (i < s.length() && s.charAt(i) == ' ') {
					i++;
				}
				if (i < s.length() && s.charAt(i) == ',') {
					i++;
				} else if (i < s.length() && s.charAt(i) == '}') {
					i++;
					while (i < s.length() && s.charAt(i) == ' ') {
						i++;
					}
					if (i < s.length() && s.charAt(i) != ' ') {
						if (depurar) {
							System.out.println("false 4");
						}
						return false;
					}
					if (i == s.length()) {
						if (depurar) {
							System.out
							.println(" 3. Tiene secuencia correcta (A2)");
						}
						return true;
					}
				} else {
					if (depurar) {
						System.out.println("false 5");
					}
					return false;
				}
			}
		}
		if (depurar) {
			System.out.println(" 3. Tiene secuencia correcta (B)");
		}
		return true;
	}

	/**
	 * Comprueba que el contenido está escrito correctamente. Suponemos que
	 * posición de llaves es correcta: "  {  ... } ".
	 * 
	 * @param s
	 *            cadena de caracteres que se comprobará
	 * @return true si el contenido está escrito correctamente.
	 */
	private static boolean tieneSecuenciaCorrectaChar(String s) {
		int i = 0;

		while (s.charAt(i) != '{') {
			i++;
		}
		i++;

		while (i < s.length() - 1 && s.charAt(i) != '}') {
			while (s.charAt(i) == ' ') {
				i++;
			}
			if (!ServiciosString.esNumero(s.charAt(i))
					&& !ServiciosString.esLetra(s.charAt(i))) {
				return false;
			}
			while (ServiciosString.esNumero(s.charAt(i))
					|| ServiciosString.esLetra(s.charAt(i))) {
				i++;
			}
			while (s.charAt(i) == ' ') {
				i++;
			}
			if (s.charAt(i) == ',') {
				i++;
			} else if (s.charAt(i) != '}') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Genera un array en función de la cadena pasada como parámetro Suponemos
	 * arrayInt="{ 5, 8, 23, 40, 7 }"
	 * 
	 * @param arrayInt
	 *            cadena de caracteres que se comprobará
	 * @return array de valores
	 */
	private static int[] valoresIntArray(String arrayInt) {
		int num_objetos = num_objetos(arrayInt);

		int arrayEnteros[] = new int[num_objetos];

		if (depurar) {
			System.out.println("\n\n\nValidamos el array int:\n[" + arrayInt
					+ "]");
		}

		if (tieneSoloNumeros(arrayInt) && tieneLlaves(arrayInt)
				&& tieneSecuenciaCorrecta(arrayInt)) {
			if (depurar) {
				System.out.println("- pasamos primeras comprobaciones");
			}
			arrayInt = arrayInt.replace(" ", "");
			try {
				for (int z = 0; z < num_objetos; z++) {
					while (arrayInt.length() >= 1
							&& !ServiciosString.esNumero(arrayInt.charAt(0))
							&& arrayInt.charAt(0) != '-') {
						arrayInt = (arrayInt.subSequence(1, arrayInt.length()))
								.toString();
					}

					String valor;
					if (z < (num_objetos - 1)) {
						int a = arrayInt.indexOf(',');
						int b = arrayInt.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayInt.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayInt.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayInt.subSequence(0, a)).toString();
						} else {
							valor = (arrayInt.subSequence(0, b)).toString();
						}
					} else {
						int a = arrayInt.indexOf('}');
						int b = arrayInt.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayInt.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayInt.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayInt.subSequence(0, a)).toString();
						} else {
							valor = (arrayInt.subSequence(0, b)).toString();
						}
					}
					arrayEnteros[z] = Integer.parseInt(valor);

					while (z < (num_objetos - 1)
							&& ServiciosString.esNumero(arrayInt.charAt(0))) {
						arrayInt = (arrayInt.subSequence(1, arrayInt.length()))
								.toString();
					}
					while (z < (num_objetos - 1) && arrayInt.charAt(0) != ',') {
						arrayInt = (arrayInt.subSequence(1, arrayInt.length()))
								.toString();
					}

				}
			} catch (java.lang.StringIndexOutOfBoundsException sioobe) {
				return null;
			}
		} else {
			return null;
		}

		return arrayEnteros;
	}

	/**
	 * Genera un array en función de la cadena pasada como parámetro Suponemos
	 * arrayByte="{ 5, 8, 23, 40, 7 }"
	 * 
	 * @param arrayByte
	 *            cadena de caracteres que se comprobará
	 * @return array de valores
	 */
	private static byte[] valoresByteArray(String arrayByte) {
		int num_objetos = num_objetos(arrayByte);

		byte arrayBytes[] = new byte[num_objetos];

		if (tieneSoloNumeros(arrayByte) && tieneLlaves(arrayByte)
				&& tieneSecuenciaCorrecta(arrayByte)) {
			arrayByte = arrayByte.replace(" ", "");
			try {
				for (int z = 0; z < num_objetos; z++) {
					while (arrayByte.length() >= 1
							&& !ServiciosString.esNumero(arrayByte.charAt(0))
							&& arrayByte.charAt(0) != '-') {
						arrayByte = (arrayByte.subSequence(1,
								arrayByte.length())).toString();
					}

					String valor;
					if (z < (num_objetos - 1)) {
						int a = arrayByte.indexOf(',');
						int b = arrayByte.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayByte.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayByte.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayByte.subSequence(0, a)).toString();
						} else {
							valor = (arrayByte.subSequence(0, b)).toString();
						}
					} else {
						int a = arrayByte.indexOf('}');
						int b = arrayByte.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayByte.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayByte.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayByte.subSequence(0, a)).toString();
						} else {
							valor = (arrayByte.subSequence(0, b)).toString();
						}
					}
					arrayBytes[z] = Byte.parseByte(valor);

					while (z < (num_objetos - 1)
							&& ServiciosString.esNumero(arrayByte.charAt(0))) {
						arrayByte = (arrayByte.subSequence(1,
								arrayByte.length())).toString();
					}
					while (z < (num_objetos - 1) && arrayByte.charAt(0) != ',') {
						arrayByte = (arrayByte.subSequence(1,
								arrayByte.length())).toString();
					}
				}
			} catch (java.lang.StringIndexOutOfBoundsException sioobe) {
				return null;
			}
		} else {
			return null;
		}

		return arrayBytes;
	}

	/**
	 * Genera un array en función de la cadena pasada como parámetro Suponemos
	 * arrayLong="{ 5, 8, 23, 40, 7 }"
	 * 
	 * @param arrayLong
	 *            cadena de caracteres que se comprobará
	 * @return array de valores
	 */
	private static long[] valoresLongArray(String arrayLong) {
		int num_objetos = num_objetos(arrayLong);

		long arrayLargos[] = new long[num_objetos];

		if (tieneSoloNumeros(arrayLong) && tieneLlaves(arrayLong)
				&& tieneSecuenciaCorrecta(arrayLong)) {
			arrayLong = arrayLong.replace(" ", "");
			try {
				for (int z = 0; z < num_objetos; z++) {
					while (arrayLong.length() >= 1
							&& !ServiciosString.esNumero(arrayLong.charAt(0))
							&& arrayLong.charAt(0) != '-') {
						arrayLong = (arrayLong.subSequence(1,
								arrayLong.length())).toString();
					}

					String valor;
					if (z < (num_objetos - 1)) {
						int a = arrayLong.indexOf(',');
						int b = arrayLong.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayLong.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayLong.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayLong.subSequence(0, a)).toString();
						} else {
							valor = (arrayLong.subSequence(0, b)).toString();
						}
					} else {
						int a = arrayLong.indexOf('}');
						int b = arrayLong.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayLong.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayLong.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayLong.subSequence(0, a)).toString();
						} else {
							valor = (arrayLong.subSequence(0, b)).toString();
						}
					}
					arrayLargos[z] = Long.parseLong(valor);

					while (z < (num_objetos - 1)
							&& ServiciosString.esNumero(arrayLong.charAt(0))) {
						arrayLong = (arrayLong.subSequence(1,
								arrayLong.length())).toString();
					}
					while (z < (num_objetos - 1) && arrayLong.charAt(0) != ',') {
						arrayLong = (arrayLong.subSequence(1,
								arrayLong.length())).toString();
					}
				}
			} catch (java.lang.StringIndexOutOfBoundsException sioobe) {
				return null;
			}
		} else {
			return null;
		}

		return arrayLargos;
	}

	/**
	 * Genera un array en función de la cadena pasada como parámetro Suponemos
	 * arrayShort="{ 5, 8, 23, 40, 7 }"
	 * 
	 * @param arrayShort
	 *            cadena de caracteres que se comprobará
	 * @return array de valores
	 */
	private static short[] valoresShortArray(String arrayShort) {
		int num_objetos = num_objetos(arrayShort);

		short arrayCortos[] = new short[num_objetos];

		if (tieneSoloNumeros(arrayShort) && tieneLlaves(arrayShort)
				&& tieneSecuenciaCorrecta(arrayShort)) {
			arrayShort = arrayShort.replace(" ", "");
			try {
				for (int z = 0; z < num_objetos; z++) {
					while (arrayShort.length() >= 1
							&& !ServiciosString.esNumero(arrayShort.charAt(0))
							&& arrayShort.charAt(0) != '-') {
						arrayShort = (arrayShort.subSequence(1,
								arrayShort.length())).toString();
					}

					String valor;
					if (z < (num_objetos - 1)) {
						int a = arrayShort.indexOf(',');
						int b = arrayShort.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayShort.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayShort.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayShort.subSequence(0, a)).toString();
						} else {
							valor = (arrayShort.subSequence(0, b)).toString();
						}
					} else {
						int a = arrayShort.indexOf('}');
						int b = arrayShort.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayShort.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayShort.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayShort.subSequence(0, a)).toString();
						} else {
							valor = (arrayShort.subSequence(0, b)).toString();
						}
					}
					arrayCortos[z] = Short.parseShort(valor);

					while (z < (num_objetos - 1)
							&& ServiciosString.esNumero(arrayShort.charAt(0))) {
						arrayShort = (arrayShort.subSequence(1,
								arrayShort.length())).toString();
					}
					while (z < (num_objetos - 1) && arrayShort.charAt(0) != ',') {
						arrayShort = (arrayShort.subSequence(1,
								arrayShort.length())).toString();
					}
				}
			} catch (java.lang.StringIndexOutOfBoundsException sioobe) {
				return null;
			}
		} else {
			return null;
		}
		return arrayCortos;
	}

	/**
	 * Genera un array en función de la cadena pasada como parámetro Suponemos
	 * arrayDouble="{ 5, 8, 23, 40, 7 }"
	 * 
	 * @param arrayDouble
	 *            cadena de caracteres que se comprobará
	 * @return array de valores
	 */
	private static double[] valoresDoubleArray(String arrayDouble) {
		int num_objetos = num_objetos(arrayDouble);

		double arrayDobles[] = new double[num_objetos];

		if (tieneSoloNumeros(arrayDouble) && tieneLlaves(arrayDouble)
				&& tieneSecuenciaCorrecta(arrayDouble)) {
			arrayDouble = arrayDouble.replace(" ", "");
			try {
				for (int z = 0; z < num_objetos; z++) {
					while (arrayDouble.length() >= 1
							&& !ServiciosString.esNumero(arrayDouble.charAt(0))
							&& arrayDouble.charAt(0) != '-') {
						arrayDouble = (arrayDouble.subSequence(1,
								arrayDouble.length())).toString();
					}

					String valor;
					if (z < (num_objetos - 1)) {
						int a = arrayDouble.indexOf(',');
						int b = arrayDouble.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayDouble.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayDouble.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayDouble.subSequence(0, a)).toString();
						} else {
							valor = (arrayDouble.subSequence(0, b)).toString();
						}
					} else {
						int a = arrayDouble.indexOf('}');
						int b = arrayDouble.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayDouble.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayDouble.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayDouble.subSequence(0, a)).toString();
						} else {
							valor = (arrayDouble.subSequence(0, b)).toString();
						}
					}
					arrayDobles[z] = Float.parseFloat(valor);

					while (z < (num_objetos - 1)
							&& ServiciosString.esNumero(arrayDouble.charAt(0))) {
						arrayDouble = (arrayDouble.subSequence(1,
								arrayDouble.length())).toString();
					}
					while (z < (num_objetos - 1)
							&& arrayDouble.charAt(0) != ',') {
						arrayDouble = (arrayDouble.subSequence(1,
								arrayDouble.length())).toString();
					}
				}
			} catch (java.lang.StringIndexOutOfBoundsException sioobe) {
				return null;
			}
		} else {
			return null;
		}

		return arrayDobles;
	}

	/**
	 * Genera un array en función de la cadena pasada como parámetro Suponemos
	 * arrayFloat="{ 5, 8, 23, 40, 7 }"
	 * 
	 * @param arrayFloat
	 *            cadena de caracteres que se comprobará
	 * @return array de valores
	 */
	private static float[] valoresFloatArray(String arrayFloat) {
		int num_objetos = num_objetos(arrayFloat);

		float arrayFlotantes[] = new float[num_objetos];

		if (tieneSoloNumeros(arrayFloat) && tieneLlaves(arrayFloat)
				&& tieneSecuenciaCorrecta(arrayFloat)) {
			arrayFloat = arrayFloat.replace(" ", "");
			try {
				for (int z = 0; z < num_objetos; z++) {
					while (arrayFloat.length() >= 1
							&& !ServiciosString.esNumero(arrayFloat.charAt(0))
							&& arrayFloat.charAt(0) != '-') {
						arrayFloat = (arrayFloat.subSequence(1,
								arrayFloat.length())).toString();
					}

					String valor;
					if (z < (num_objetos - 1)) {
						int a = arrayFloat.indexOf(',');
						int b = arrayFloat.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayFloat.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayFloat.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayFloat.subSequence(0, a)).toString();
						} else {
							valor = (arrayFloat.subSequence(0, b)).toString();
						}
					} else {
						int a = arrayFloat.indexOf('}');
						int b = arrayFloat.indexOf(' ');
						if (a < b && a != -1) {
							valor = (arrayFloat.subSequence(0, a)).toString();
						} else if (b < a && b != -1) {
							valor = (arrayFloat.subSequence(0, b)).toString();
						} else if (a > b) {
							valor = (arrayFloat.subSequence(0, a)).toString();
						} else {
							valor = (arrayFloat.subSequence(0, b)).toString();
						}
					}
					arrayFlotantes[z] = Float.parseFloat(valor);

					if (z < (num_objetos - 1)) {
						while (arrayFloat.charAt(0) != ',') {
							arrayFloat = (arrayFloat.subSequence(1,
									arrayFloat.length())).toString();
						}
					}
				}
			} catch (java.lang.StringIndexOutOfBoundsException sioobe) {
				return null;
			}
		} else {
			return null;
		}

		return arrayFlotantes;
	}

	/**
	 * Genera un array en función de la cadena pasada como parámetro Suponemos
	 * arrayChar="{ '5', '8', '23', '40', '7' }"
	 * 
	 * @param arrayChar
	 *            cadena de caracteres que se comprobará
	 * @return array de valores
	 */
	private static char[] valoresCharArray(String arrayChar) {
		int num_objetos = num_objetos(arrayChar);
		char arrayCaracteres[] = new char[num_objetos];

		// Internamente, quitamos las comillas simples a los caracteres
		arrayChar = arrayChar.replace("\'", "");

		if (tieneSoloCaracteres(arrayChar) && tieneLlaves(arrayChar)
				&& tieneSecuenciaCorrectaChar(arrayChar)) {
			arrayChar = arrayChar.replace(" ", "");
			try {
				for (int z = 0; z < num_objetos; z++) {
					while (arrayChar.length() >= 1
							&& (!ServiciosString.esLetra(arrayChar.charAt(0)) && !ServiciosString
									.esNumero(arrayChar.charAt(0)))) {
						arrayChar = (arrayChar.subSequence(1,
								arrayChar.length())).toString();
					}

					String valor;
					int a = arrayChar.indexOf(',');
					int b = arrayChar.indexOf('}');
					if (!(a < b && a != -1)) {
						a = b;
					}
					valor = (arrayChar.subSequence(0, a)).toString();
					arrayChar = (arrayChar.subSequence(1, arrayChar.length()))
							.toString(); // Borramos carácter

					if (valor.length() == 1) {
						arrayCaracteres[z] = valor.charAt(0);
					} else {
						return null;
					}
					while (z < (num_objetos - 1)
							&& (!ServiciosString.esLetra(arrayChar.charAt(0)) && !ServiciosString
									.esNumero(arrayChar.charAt(0)))) {
						arrayChar = (arrayChar.subSequence(1,
								arrayChar.length())).toString();
					}
				}

			} catch (java.lang.StringIndexOutOfBoundsException sioobe) {
				return null;
			}
		} else {
			return null;
		}

		return arrayCaracteres;
	}

	/**
	 * Genera un array en función de la cadena pasada como parámetro Suponemos
	 * arrayString="{ "5", "8", "23", "40", "7" }"
	 * 
	 * @param arrayString
	 *            cadena de caracteres que se comprobará
	 * @return array de valores
	 */
	private static String[] valoresStringArray(String arrayString) {
		// Internamente, quitamos las comillas dobles a las cadenas
		arrayString = arrayString.replace("\"", "");

		String s = new String(arrayString);
		int num_objetos = num_objetos(arrayString); // Ya no llamamos a
		// "num_objetosString"
		if (depurar) {
			System.out.println("Cadena: [" + arrayString + "]\nNum_cadenas:"
					+ num_objetos);
		}

		if (num_objetos <= 0) {
			return null;
		}

		String arrayCadenas[] = new String[num_objetos];
		s = s.replace(" ", "");
		if (s.charAt(0) != '{' || s.charAt(s.length() - 1) != '}') {
			return null;
		}
		s = s.substring(1, s.length() - 1);
		if (s.charAt(0) == ',' || s.charAt(s.length() - 1) == ',') {
			return null;
		}

		try {
			for (int i = 0; i < num_objetos; i++) {
				if (s.charAt(0) == ',') {
					s = s.substring(1, s.length());
				}
				if (s.charAt(0) == ',') {
					return null;
				}
				int c = s.indexOf(',');
				if (c == -1) {
					c = s.length();
				}
				arrayCadenas[i] = s.substring(0, c);
				s = s.substring(c, s.length());
				if (depurar) {
					System.out
					.println("    " + i + "[" + arrayCadenas[i] + "]");
				}
				if (!(esStringCorrecto(arrayCadenas[i]))) {
					return null;
				}
			}
		} catch (java.lang.StringIndexOutOfBoundsException sioobe) {
			return null;
		}
		return arrayCadenas;
	}

	/**
	 * Genera un array en función de la cadena pasada como parámetro Suponemos
	 * arrayBoolean="{ true, false, true }"
	 * 
	 * @param arrayBoolean
	 *            cadena de caracteres que se comprobará
	 * @return array de valores
	 */
	private static boolean[] valoresBooleanArray(String arrayBoolean) {

		String valores[] = valoresStringArray(arrayBoolean); // Tendremos arrays
		// de "true" y
		// "false"

		if (valores == null) {
			return null;
		}

		boolean arrayBooleanos[] = new boolean[valores.length];

		for (int i = 0; i < valores.length; i++) {
			valores[i] = valores[i].toLowerCase();
			if (valores[i].equals("true")) {
				arrayBooleanos[i] = true;
			} else if (valores[i].equals("false")) {
				arrayBooleanos[i] = false;
			} else {
				return null;
			}
		}

		return arrayBooleanos;
	}

	/**
	 * Genera un array de arrays en función de la cadena pasada como parámetro
	 * Suponemos arrayArray="{ {5, 8, 23} , {40, 7} }"
	 * 
	 * @param arrayArray
	 *            cadena de caracteres que se comprobará
	 * @param tipo
	 *            indica el tipo de array que será (int, long, double, String,
	 *            ...)
	 * 
	 *            // argumento tipo: // 1=int 2=short 3=long 4=double 5=float
	 *            6=byte // 7=char 8=String 9=boolean
	 * 
	 *            // Numero de arrays contenidos directamente // Ejemplo:
	 *            "{ {5, 8, 23} , {40, {7, 564, 345, 77} } }" // devuelve 2, el
	 *            compuesto por 5,8 y 23 y el compuesto por 40 y otro array
	 * @return array de arrays
	 */
	private static Object valoresArrayArray(String arrayArray, int tipo) {
		int num_objetos = num_objetosArray(arrayArray);
		if (depurar) {
			System.out.println("Num objetos: " + num_objetos);
		}
		if (esArrayCorrecto(arrayArray, num_objetos, tipo)) {
			if (depurar) {
				System.out.println("ARRAY ARRAY " + tipo + " CORRECTO");
			}
			String arrays[];
			arrays = arrays(arrayArray, num_objetos);

			switch (tipo) {
			case 1:
				int arrayEnteros[][] = new int[num_objetos][];
				for (int i = 0; i < num_objetos; i++) {
					arrayEnteros[i] = valoresIntArray(arrays[i]);
					if (arrayEnteros[i] == null) {
						return null;
					}
				}
				return arrayEnteros;
			case 2:
				short arrayCortos[][] = new short[num_objetos][];
				for (int i = 0; i < num_objetos; i++) {
					arrayCortos[i] = valoresShortArray(arrays[i]);
					if (arrayCortos[i] == null) {
						return null;
					}
				}
				return arrayCortos;
			case 3:
				long arrayLargos[][] = new long[num_objetos][];
				for (int i = 0; i < num_objetos; i++) {
					arrayLargos[i] = valoresLongArray(arrays[i]);
					if (arrayLargos[i] == null) {
						return null;
					}
				}
				return arrayLargos;
			case 4:
				double arrayDobles[][] = new double[num_objetos][];
				for (int i = 0; i < num_objetos; i++) {
					arrayDobles[i] = valoresDoubleArray(arrays[i]);
					if (arrayDobles[i] == null) {
						return null;
					}
				}
				return arrayDobles;
			case 5:
				float arrayReales[][] = new float[num_objetos][];
				for (int i = 0; i < num_objetos; i++) {
					arrayReales[i] = valoresFloatArray(arrays[i]);
					if (arrayReales[i] == null) {
						return null;
					}
				}
				return arrayReales;
			case 6:
				byte arrayByte[][] = new byte[num_objetos][];
				for (int i = 0; i < num_objetos; i++) {
					arrayByte[i] = valoresByteArray(arrays[i]);
					if (arrayByte[i] == null) {
						return null;
					}
				}
				return arrayByte;
			case 7:
				char arrayCaract[][] = new char[num_objetos][];
				for (int i = 0; i < num_objetos; i++) {
					arrayCaract[i] = valoresCharArray(arrays[i]);
					if (arrayCaract[i] == null) {
						return null;
					}
				}
				return arrayCaract;
			case 8:
				String arrayCadenas[][] = new String[num_objetos][];
				for (int i = 0; i < num_objetos; i++) {
					arrayCadenas[i] = valoresStringArray(arrays[i]);
					if (arrayCadenas[i] == null) {
						return null;
					}
				}
				return arrayCadenas;
			case 9:
				boolean arrayBooleanos[][] = new boolean[num_objetos][];
				for (int i = 0; i < num_objetos; i++) {
					arrayBooleanos[i] = valoresBooleanArray(arrays[i]);
					if (arrayBooleanos[i] == null) {
						return null;
					}
				}
				return arrayBooleanos;
			default:
				if (depurar) {
					System.out
					.println("GestorParametros.valoresArrayArray Valor erroneo para segundo parametro ("
							+ tipo + ") de este método");
				}
			}
			return null;
		} else {
			if (depurar) {
				System.out.println("ARRAY ARRAY INCORRECTO");
			}
			return null;
		}
	}

	/**
	 * Comprueba que una cadena de caracteres contenga un array de arrays
	 * correcto
	 * 
	 * @param cadena
	 *            cadena que se comprobará
	 * @param num
	 *            número de arrays que hay
	 * @param tipo
	 *            tipo al que pertenecerán los arrays
	 * @return true si el array tiene una sintaxis correcta
	 */
	private static boolean esArrayCorrecto(String cadena, int num, int tipo) {
		// Determinamos número de arrays
		// Miramos que no haya nada delante de llave inicial ni detrás de llave
		// final
		// Miramos que no haya más que una coma entre arrays y entre elemenos de
		// los arrays

		int x = 0;
		int numArrays = 0;
		int l = cadena.length();
		for (int i = 0; i < l - 1; i++) {
			if (cadena.charAt(i) == '{') {
				numArrays++;
			}
		}
		numArrays--; // retiramos cuenta de llave inicial

		if (depurar) {
			System.out.println("num=" + num + " numArrays=" + numArrays
					+ " tipo=" + tipo);
		}
		if (depurar) {
			System.out.println(cadena + " (length=" + l + ")");
		}

		while (x < l && cadena.charAt(x) == ' ') {
			x++;
		}
		if (x < l && cadena.charAt(x) != '{') {
			return false;
		}
		x++;

		for (int i = 0; i < numArrays; i++) // Recorremos cada array
		{
			while (x < l && cadena.charAt(x) == ' ') {
				x++;
			}

			if (cadena.charAt(x) != '{') {
				return false;
			}
			x++;
			while (x < l && cadena.charAt(x) != '}') {
				x++;
			}
			x++;
			while (x < l && cadena.charAt(x) == ' ') {
				x++;
			}
			if (x < l && cadena.charAt(x) == ',' && i < (numArrays - 1)) {
				x++;
			} else if (x < l && cadena.charAt(x) == ',' && i >= (numArrays - 1)) {
				return false;
			} else if (x < l && cadena.charAt(x) == '}' && i < (numArrays - 1)) {
				return false;
			} else if (x < l && cadena.charAt(x) == '{') {
				return false;
			}
		}
		while (x < l && cadena.charAt(x) == ' ') {
			x++;
		}
		if (x < l && cadena.charAt(x) == '}') {
			x++;
		} else {
			return false;
		}
		while (x < l && cadena.charAt(x) == ' ') {
			x++;
		}
		if (x < l && cadena.charAt(x) != ' ') {
			return false;
		}
		return true;

	}

	/**
	 * Comprueba que alberga un valor válido para un parámetro String
	 * 
	 * @param s
	 *            cadena que se comprobará
	 * @return true si el string tiene un valor válido
	 */
	private static boolean esStringCorrecto(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!(ServiciosString.esNumero(s.charAt(i))
					|| ServiciosString.esLetra(s.charAt(i)) || s.charAt(i) == '_')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Determina el número de objetos que hay en un string
	 * 
	 * @param s
	 *            cadena que se comprobará
	 * @return número de objetos que se podrán extraer del String
	 */
	private static int num_objetos(String s) {
		int x = 0, comas = 0;

		for (x = 0; x < s.length(); x++) {
			if (s.charAt(x) == ',') {
				comas++;
			}
		}
		return comas + 1;
	}

	/**
	 * Determina el número de arrays que tiene un array, pasado en un String
	 * Suponemos "{  {  4 ,6 } , { 56 , { 45, 67, 78 } } }"
	 * 
	 * @param s
	 *            cadena que se comprobará
	 * @return número de arrays que se podrán extraer del String
	 */
	private static int num_objetosArray(String s) //
	{
		int x = s.indexOf('{') + 1;
		int num = 0;
		int nivel = 0;

		while (x < s.length()) {
			if (s.charAt(x) == '{') {
				num++;
				nivel++;
				while (x < s.length() && nivel != 0) {
					x++;
					if (s.charAt(x) == '{') {
						nivel++;
					} else if (s.charAt(x) == '}') {
						nivel--;
					}
				}
			}
			x++;
		}

		return num;
	}

	/**
	 * Descompone un String que contiene un array de arrays en varios String que
	 * contienen cada uno un array
	 * 
	 * @param s
	 *            cadena que se descompondrá
	 * @param numArrays
	 *            número de arrays que se sabe que existen en el String
	 * @return Strings con los distintos arrays
	 */
	private static String[] arrays(String s, int numArrays) {
		String arrays[] = new String[numArrays];
		int posiciones[][] = new int[numArrays][2];
		int y = 0, x = s.indexOf('{') + 1;
		int nivel = 0;

		// Almacenamos en "posiciones" la posicion de comienzo y final de cada
		// array
		while (x < s.length()) {
			if (s.charAt(x) == '{') {
				posiciones[y][0] = x;
				nivel++;
				while (x < s.length() && nivel != 0) {
					x++;
					if (s.charAt(x) == '{') {
						nivel++;
					} else if (s.charAt(x) == '}') {
						nivel--;
					}
				}
				if (nivel == 0 && x < s.length()) {
					posiciones[y][1] = x + 1;
					y++;
				}
			}
			x++;
		}

		for (int i = 0; i < arrays.length; i++) {
			arrays[i] = s.substring(posiciones[i][0], posiciones[i][1]);
		}

		// Devolvemos cada uno de los subarrays contenidos en las llaves
		// iniciales en un String
		return arrays;
	}

}