package utilidades;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Clase que ofrece servicios relacionados con Strings y caracteres
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class ServiciosString {

	private static final boolean depurar = false;

	/**
	 * Devuelve una representación en String del objeto Integer pasado como
	 * parámetro
	 * 
	 * @param objeto
	 * 
	 * @return Representación en String
	 */
	public static String representacionInt(Object o) {
		String s = "";
		return s + ((Integer) o).intValue();
	}

	/**
	 * Devuelve una representación en String del objeto Char pasado como
	 * parámetro
	 * 
	 * @param objeto
	 * 
	 * @return Representación en String
	 */
	public static String representacionChar(Object o) {
		String s = "";
		return s + "\'" + ((Character) o).charValue() + "\'";
	}

	/**
	 * Devuelve una representación en String del objeto Long pasado como
	 * parámetro
	 * 
	 * @param objeto
	 * 
	 * @return Representación en String
	 */
	public static String representacionLong(Object o) {
		String s = "";
		return s + ((Long) o).longValue();
	}

	/**
	 * Devuelve una representación en String del objeto Double pasado como
	 * parámetro
	 * 
	 * @param objeto
	 * 
	 * @return Representación en String
	 */
	public static String representacionDouble(Object o) {
		String s = "";
		return s + ((Double) o).doubleValue();
	}

	/**
	 * Devuelve una representación en String del objeto Short pasado como
	 * parámetro
	 * 
	 * @param objeto
	 * 
	 * @return Representación en String
	 */
	public static String representacionShort(Object o) {
		String s = "";
		return s + ((Short) o).shortValue();
	}

	/**
	 * Devuelve una representación en String del objeto Float pasado como
	 * parámetro
	 * 
	 * @param objeto
	 * 
	 * @return Representación en String
	 */
	public static String representacionFloat(Object o) {
		String s = "";
		return s + ((Float) o).floatValue();
	}

	/**
	 * Devuelve una representación en String del valor float especificado.
	 * 
	 * @param f
	 *            valor
	 * @param p
	 *            Precisión
	 * 
	 * @return Representación en String
	 */
	public static String truncarNumero(float f, int p) {
		String s = "" + f;
		if (s.contains(".")) {
			if (p == 0) {
				s = s.substring(0, s.indexOf("."));
			} else if (p + s.indexOf(".") < s.length()) {
				s = s.substring(0, s.indexOf(".") + 1 + p);
			}
		}

		return s;
	}

	/**
	 * Devuelve una representación en String del valor double especificado.
	 * 
	 * @param f
	 *            valor
	 * @param p
	 *            Precisión
	 * 
	 * @return Representación en String
	 */
	public static String truncarNumero(double f, int p) {
		String s = "" + f;
		if (s.contains(".")) {
			if (p == 0) {
				s = s.substring(0, s.indexOf("."));
			} else if (p + s.indexOf(".") < s.length()) {
				s = s.substring(0, s.indexOf(".") + 1 + p);
			}
		}

		return s;
	}

	/**
	 * Devuelve una representación en String del objeto String pasado como
	 * parámetro
	 * 
	 * @param objeto
	 * 
	 * @return Representación en String
	 */
	public static String representacionString(Object o) {
		return "\"" + (String) o + "\"";
	}

	/**
	 * Devuelve una representación en String del objeto Boolean pasado como
	 * parámetro
	 * 
	 * @param objeto
	 * 
	 * @return Representación en String
	 */
	public static String representacionBoolean(Object o) {
		String s = "";
		return s + ((Boolean) o).booleanValue();
	}

	/**
	 * Devuelve una representación en String del objeto pasado como parámetro
	 * 
	 * @param objeto
	 * 
	 * @return Representación en String
	 */
	public static String representacionObjeto(Object o) {
		String cadena = "";

		if (depurar) {
			System.out.println("PanelEstado Entrando en objeto...");
		}

		if (o == null) {
			return "null";
		}

		Class clase = o.getClass();

		if (clase.getCanonicalName().contains("[][]")) {
			Object objeto[] = (Object[]) o;

			cadena = cadena + "{";
			if (depurar) {
				System.out.println("PanelEstado Tiene mas dimensiones...");
			}
			for (int i = 0; i < objeto.length; i++) {
				cadena = cadena + representacionObjeto(objeto[i]);
				if (i < (objeto.length - 1)) {
					cadena = cadena + ",";
				}
			}
			cadena = cadena + "}";
		}

		else if (clase.getCanonicalName().contains("int")
				|| clase.getCanonicalName().contains("Integer")) // Si es
		// integer
		{
			if (depurar) {
				System.out.println("PanelEstado int");
			}
			if (!(clase.getCanonicalName().contains("[]"))) {
				cadena = cadena + representacionInt(o);
			} else {
				int array[] = (int[]) o;
				cadena = cadena + "{";
				for (int y = 0; y < array.length; y++) {
					cadena = cadena + representacionInt(array[y]);
					if (y < (array.length - 1)) {
						cadena = cadena + ",";
					}
				}
				cadena = cadena + "}";
			}
		}

		else if (clase.getCanonicalName().contains("char")
				|| clase.getCanonicalName().contains("Character")) // Si es char
		{
			if (depurar) {
				System.out.println("PanelEstado char");
			}
			if (!(clase.getCanonicalName().contains("[]"))) {
				cadena = cadena + representacionChar(o);
			} else {
				char array[] = (char[]) o;
				cadena = cadena + "{";
				for (int y = 0; y < array.length; y++) {
					cadena = cadena + representacionChar(array[y]);
					if (y < (array.length - 1)) {
						cadena = cadena + ",";
					}
				}
				cadena = cadena + "}";

			}
		}

		else if (clase.getCanonicalName().contains("long")
				|| clase.getCanonicalName().contains("Long")) // Si es long
		{
			if (depurar) {
				System.out.println("PanelEstado long");
			}
			if (!(clase.getCanonicalName().contains("[]"))) {
				cadena = cadena + representacionLong(o);
			} else {
				long array[] = (long[]) o;
				cadena = cadena + "{";
				for (int y = 0; y < array.length; y++) {
					cadena = cadena + representacionLong(array[y]);
					if (y < (array.length - 1)) {
						cadena = cadena + ",";
					}
				}
				cadena = cadena + "}";
			}
		}

		else if (clase.getCanonicalName().contains("double")
				|| clase.getCanonicalName().contains("Double")) // Si es double
		{
			if (depurar) {
				System.out.println("PanelEstado double");
			}
			if (!(clase.getCanonicalName().contains("[]"))) {
				cadena = cadena + representacionDouble(o);
			} else {
				double array[] = (double[]) o;
				cadena = cadena + "{";
				for (int y = 0; y < array.length; y++) {
					cadena = cadena + representacionDouble(array[y]);
					if (y < (array.length - 1)) {
						cadena = cadena + ",";
					}
				}
				cadena = cadena + "}";

			}
		}

		else if (clase.getCanonicalName().contains("short")
				|| clase.getCanonicalName().contains("Short")) // Si es short
		{
			if (depurar) {
				System.out.println("PanelEstado short");
			}
			if (!(clase.getCanonicalName().contains("[]"))) {
				cadena = cadena + representacionShort(o);
			} else {
				short array[] = (short[]) o;
				cadena = cadena + "{";
				for (int y = 0; y < array.length; y++) {
					cadena = cadena + representacionShort(array[y]);
					if (y < (array.length - 1)) {
						cadena = cadena + ",";
					}
				}
				cadena = cadena + "}";

			}
		}

		else if (clase.getCanonicalName().contains("float")
				|| clase.getCanonicalName().contains("Float")) // Si es float
		{
			if (depurar) {
				System.out.println("PanelEstado float");
			}
			if (!(clase.getCanonicalName().contains("[]"))) {
				cadena = cadena + representacionFloat(o);
			} else {
				float array[] = (float[]) o;
				cadena = cadena + "{";
				for (int y = 0; y < array.length; y++) {
					cadena = cadena + representacionFloat(array[y]);
					if (y < (array.length - 1)) {
						cadena = cadena + ",";
					}
				}
				cadena = cadena + "}";
			}
		}

		else if (clase.getCanonicalName().contains("String")
				|| clase.getCanonicalName().contains("String")) // Si es String
		{
			if (depurar) {
				System.out.println("PanelEstado String");
			}
			if (!(clase.getCanonicalName().contains("[]"))) {
				cadena = cadena + representacionString(o);
			} else {
				String array[] = (String[]) o;
				cadena = cadena + "{";
				for (int y = 0; y < array.length; y++) {
					cadena = cadena + representacionString(array[y]);
					if (y < (array.length - 1)) {
						cadena = cadena + ",";
					}
				}
				cadena = cadena + "}";
			}
		}

		else if (clase.getCanonicalName().contains("boolean")
				|| clase.getCanonicalName().contains("Boolean")) // Si es
		// boolean
		{
			if (depurar) {
				System.out.println("PanelEstado boolean");
			}
			if (!(clase.getCanonicalName().contains("[]"))) {
				cadena = cadena + representacionBoolean(o);
			} else {
				boolean array[] = (boolean[]) o;
				cadena = cadena + "{";
				for (int y = 0; y < array.length; y++) {
					cadena = cadena + representacionBoolean(array[y]);
					if (y < (array.length - 1)) {
						cadena = cadena + ",";
					}
				}
				cadena = cadena + "}";
			}
		}

		else if (depurar) {
			System.out.println("ServiciosString: Tipo no reconocido ("
					+ o.getClass().getCanonicalName() + ")");
		}

		return cadena;
	}

	/**
	 * Determina si un carácter es un número
	 * 
	 * @param a
	 *            carácter
	 * @return true si el carácter es un número
	 */
	public static boolean esNumero(char a) {
		return (a == '0' || a == '1' || a == '2' || a == '3' || a == '4'
				|| a == '5' || a == '6' || a == '7' || a == '8' || a == '9');
	}

	/**
	 * Determina si un carácter es una letra
	 * 
	 * @param a
	 *            carácter
	 * @return true si el carácter es una letra
	 */
	public static boolean esLetra(char a) {
		return (a == 'a' || a == 'b' || a == 'c' || a == 'd' || a == 'e'
				|| a == 'f' || a == 'g' || a == 'h' || a == 'i' || a == 'j'
				|| a == 'k' || a == 'l' || a == 'm' || a == 'n' || a == 'o'
				|| a == 'p' || a == 'q' || a == 'r' || a == 's' || a == 't'
				|| a == 'u' || a == 'v' || a == 'w' || a == 'x' || a == 'y'
				|| a == 'z' || a == 'A' || a == 'B' || a == 'C' || a == 'D'
				|| a == 'E' || a == 'F' || a == 'G' || a == 'H' || a == 'I'
				|| a == 'J' || a == 'K' || a == 'L' || a == 'M' || a == 'N'
				|| a == 'O' || a == 'P' || a == 'Q' || a == 'R' || a == 'S'
				|| a == 'T' || a == 'U' || a == 'V' || a == 'W' || a == 'X'
				|| a == 'Y' || a == 'Z' ||

				a == 'Ñ' || a == 'ñ' ||

				a == 'á' || a == 'é' || a == 'í' || a == 'ó' || a == 'ú'
				|| a == 'Á' || a == 'É' || a == 'Í' || a == 'Ó' || a == 'Ú');

	}

	/**
	 * Determina si en el array de strings pasado como parámetro, hay cadenas
	 * repetidas.
	 * 
	 * @param s
	 *            Array de entrada
	 * 
	 * @return True si hay cadenas repetidas, false en caso contrario.
	 */
	public static boolean hayCadenasRepetidas(String s[]) {
		for (int i = 0; i < s.length; i++) {
			for (int j = i; j < s.length; j++) {
				if (s[i].equals(s[j]) && i != j) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Devuelve la representación hexadecimal del color especificado.
	 * 
	 * @param r
	 *            Componente R
	 * @param g
	 *            Componente G
	 * @param b
	 *            Componente B
	 * 
	 * @return Representación hexadecimal.
	 */
	public static String cadenaColorHex(int r, int g, int b) {
		String rojo = Integer.toHexString(r);
		String verde = Integer.toHexString(g);
		String azul = Integer.toHexString(b);

		if (rojo.length() == 1) {
			rojo = "0" + rojo;
		}
		if (verde.length() == 1) {
			verde = "0" + verde;
		}
		if (azul.length() == 1) {
			azul = "0" + azul;
		}

		return rojo + verde + azul;

	}

	/**
	 * Determina si la cadena pasada por parámetro está, o está contenida en el
	 * array de cadenas especificado.
	 * 
	 * @param s
	 *            Array de Strings.
	 * @param c
	 *            Cadena a buscar.
	 * @param exacta
	 *            True si la cadena encontrada debe ser igual, false si es
	 *            suficiente con que este contenida en alguna de ellas.
	 * 
	 * @return True si se ha encontrado, false en caso contrario.
	 */
	public static boolean contieneCadena(String s[], String c, boolean exacta) {
		for (int i = 0; i < s.length; i++) {
			if ((s[i].equals(c) && exacta) || (!exacta && s[i].contains(c))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Devuelve la representación de las dimensiones (para añadir al tipo de
	 * datos "[]...[]") de una estructura de n dimensiones.
	 * 
	 * @param n
	 *            Número de dimensiones+
	 * 
	 * @return Representación de las dimensiones para el tipo de datos.
	 */
	public static String cadenaDimensiones(int n) {
		String cadena = "";

		for (int i = 0; i < n; i++) {
			cadena = cadena + "[]";
		}

		return cadena;
	}

	/**
	 * Permite determinar si un valor es del tipo y dimensiones correctas.
	 * 
	 * @param valor
	 *            Valor a comprobar.
	 * @param tipo
	 *            Tipo de datos
	 * @param dim
	 *            Dimensiones del tipo de datos.
	 * 
	 * @return True si el valor tiene el formato adecuado, false en caso
	 *         contrario.
	 */
	public static boolean esDeTipoCorrecto(String valor, String tipo, int dim) {
		if (dim == 0) {
			// Quitar espacios de delante de y detrás si los hay
			while (valor.charAt(0) == ' ') {
				valor = valor.substring(1, valor.length());
			}
			while (valor.charAt(valor.length() - 1) == ' ') {
				valor = valor.substring(0, valor.length() - 1);
			}

			// Comprobación según los tipos
			if (tipo.equals("int") || tipo.equals("short")
					|| tipo.equals("byte")) {
				try {
					Integer.parseInt(valor);
				} catch (NumberFormatException nfe) {
					return false;
				}
			} else if (tipo.equals("long")) {
				try {
					Long.parseLong(valor);
				} catch (NumberFormatException nfe) {
					return false;
				}
			} else if (tipo.equals("double") || tipo.equals("float")) {
				try {
					Double.parseDouble(valor);
				} catch (NumberFormatException nfe) {
					return false;
				}
			}

			else if (tipo.equals("boolean")) {
				valor = valor.toUpperCase();
				if (!valor.equals("TRUE") && !valor.equals("FALSE")) {
					return false;
				}
			} else if (tipo.equals("String")) {
				// Formato permitido: "hola"
				if ((valor.charAt(0) != '\"')
						|| (valor.charAt(valor.length() - 1) != '\"')) {
					return false;
				}
				for (int i = 1; i < valor.length() - 1; i++) {
					if (!(ServiciosString.esNumero(valor.charAt(i))
							|| ServiciosString.esLetra(valor.charAt(i)) || valor
								.charAt(i) == '_')) {
						return false;
					}
				}
			} else if (tipo.equals("char")) {
				// Formato permitido: 'a'
				if (valor.length() != 3) {
					return false;
				}
				if ((valor.charAt(0) != '\'') || (valor.charAt(2) != '\'')) {
					return false;
				}
				if (!ServiciosString.esNumero(valor.charAt(1))
						&& !ServiciosString.esLetra(valor.charAt(1))
						&& !(valor.charAt(1) == '_')) {
					return false;
				}
			}
		} else if (dim == 1) {
			// Manejar cadenas de tipo " { 4 , 5 , 6 , 7 } " o
			// " { a , b , c , d } " o " { cadena , cad , ca , c } "

			// Quitar espacios de delante de y detrás si los hay
			while (valor.charAt(0) == ' ') {
				valor = valor.substring(1, valor.length());
			}
			while (valor.charAt(valor.length() - 1) == ' ') {
				valor = valor.substring(0, valor.length() - 1);
			}

			// Quitar controladamente las llaves
			if (valor.charAt(0) == '{'
					&& valor.charAt(valor.length() - 1) == '}') {
				valor = valor.substring(1, valor.length() - 1);
			} else {
				return false;
			}

			// Extraer valores y comprobar comas, omitiendo espacios
			ArrayList<String> cadenas = new ArrayList<String>(0);
			String valor2 = new String(valor);
			while (valor2.length() > 0) {
				try {
					cadenas.add(valor2.substring(0, valor2.indexOf(",")));
					valor2 = valor2.substring(valor2.indexOf(",") + 1,
							valor2.length());
				} catch (Exception e) {
					cadenas.add(valor2.substring(0, valor2.length()));
					valor2 = "";
				}
			}

			// Sobre cada valor, llamada recursiva, con dim=0
			for (int i = 0; i < cadenas.size(); i++) {
				if (!esDeTipoCorrecto(cadenas.get(i), tipo, 0)) {
					return false;
				}
			}
		} else if (dim == 2) {
			// Manejar cadenas de tipo
			// " { { 4 , 5 , 6 , 7 } , { 4 , 5 , 6 , 7 } , { 4 , 5 , 6 , 7 } , { 4 , 5 , 6 , 7 } } "

			// Quitar espacios de delante de y detrás si los hay
			while (valor.charAt(0) == ' ') {
				valor = valor.substring(1, valor.length());
			}
			while (valor.charAt(valor.length() - 1) == ' ') {
				valor = valor.substring(0, valor.length() - 1);
			}

			// Quitar controladamente las llaves 'grandes'
			if (valor.charAt(0) == '{'
					&& valor.charAt(valor.length() - 1) == '}') {
				valor = valor.substring(1, valor.length() - 1);
			} else {
				return false;
			}

			String valor2 = new String(valor);

			// Extraer arrays y comprobar comas, omitiendo espacios
			int numArrays = ServiciosString.numVeces(valor, "{");
			String arrays[] = new String[numArrays];

			for (int i = 0; i < arrays.length; i++) {
				if (i < (arrays.length - 1)) {
					arrays[i] = valor2.substring(valor2.indexOf("{"),
							valor2.indexOf("}") + 1);
					valor2 = valor2.substring(valor2.indexOf("}") + 1,
							valor2.length());
					valor2 = valor2.substring(valor2.indexOf(",") + 1,
							valor2.length());
				} else {
					arrays[i] = valor2 + "";
				}
			}

			// Sobre cada array, llamada recursiva, con dim=1
			for (int i = 0; i < arrays.length; i++) {
				if (!esDeTipoCorrecto(arrays[i], tipo, 1)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Elimina los elementos repetidos del array pasado por parámetro,
	 * devolviendo un nuevo array sin repeticiones.
	 * 
	 * @param cadenas
	 *            Array de entrada.
	 * 
	 * @return Array de salida, sin repeticiones.
	 */
	public static String[] quitarCadenasRepetidas(String[] cadenas) {

		for (int i = 0; i < cadenas.length; i++) {
			for (int j = i + 1; j < cadenas.length; j++) {
				if (cadenas[i] != null && cadenas[j] != null
						&& cadenas[i].equals(cadenas[j])) {
					cadenas[j] = null;
				}
			}
		}

		int numNoNulas = 0;
		for (int i = 0; i < cadenas.length; i++) {
			if (cadenas[i] != null) {
				numNoNulas++;
			}
		}

		int c = 0;
		String[] cadenasUnicas = new String[numNoNulas];
		for (int i = 0; i < cadenas.length; i++) {
			if (cadenas[i] != null) {
				cadenasUnicas[c] = cadenas[i];
				c++;
			}
		}

		return cadenasUnicas;

	}

	/**
	 * Obtiene los prefijos para los nombres de métodos pasados por parámetro.
	 * 
	 * @see NombresYPrefijos
	 * 
	 * @param nombres
	 *            Nombres de métodos.
	 * 
	 * @return Array de prefijos.
	 */
	public static String[] obtenerPrefijos(String[] nombres) {
		int longitudMinima = nombres[0].length();
		int posLongitudMinima = 0;

		for (int i = 0; i < nombres.length; i++) {
			if (nombres[i].length() < longitudMinima) {
				longitudMinima = nombres[i].length();
				posLongitudMinima = i;
			}
		}

		// Calculamos parte comun a todos
		boolean finComun = false;
		int y = 0;
		while (y < nombres[posLongitudMinima].length() && !finComun) {
			char c = nombres[0].charAt(y);
			int j = 0;
			while (j < nombres.length && !finComun) {
				if (c != nombres[j].charAt(y)) {
					finComun = true;
				}
				j++;
			}
			y++;
		}
		String comun = nombres[0].substring(0, y - 1);

		String prefijos[] = new String[nombres.length];

		// Asignamos parte inicial común
		for (int i = 0; i < prefijos.length; i++) {
			prefijos[i] = comun;
		}

		for (int i = 0; i < prefijos.length; i++) {
			if (nombres[i].length() > comun.length()) {
				prefijos[i] = prefijos[i] + nombres[i].charAt(comun.length());
			}
		}

		// Bucle mientras haya repetidos, para ir incrementando longitud de los
		// identificadores hasta que sean distintos
		while (ServiciosString.hayCadenasRepetidas(prefijos)) {
			for (int i = 0; i < prefijos.length; i++) {
				boolean encontradoOtroIgual = false;
				for (int j = 0; j < prefijos.length; j++) {
					if (prefijos[i].equals(prefijos[j]) && i != j) {
						if (prefijos[j].length() != nombres[j].length()) {
							prefijos[j] = prefijos[j]
									+ nombres[j].charAt(prefijos[j].length());
						}
						encontradoOtroIgual = true;
					}
				}
				if (encontradoOtroIgual) {
					if (prefijos[i].length() != nombres[i].length()) {
						prefijos[i] = prefijos[i]
								+ nombres[i].charAt(prefijos[i].length());
					}
				}
			}
		}

		return prefijos;
	}

	// Número de veces que cadena contiene cad
	public static int vecesQueContiene(String cadena, String cad) {
		int contador = 0;

		while (cadena.contains(cad)) {
			contador++;

			cadena = cadena.substring(cadena.indexOf(cad) + cad.length(),
					cadena.length());
		}

		return contador;
	}

	/**
	 * Devuelve el tipo de datos primitivo correspondiente a la clase envolvente
	 * pasada por parámetro.
	 * 
	 * @param s
	 *            Nombre de la clase envolvente ejemplo: "Integer", "Byte".
	 * 
	 * @return String con el nombre del tipo primitivo.
	 */
	public static String simplificarClase(String s) {
		if (s.contains("Integer")) {
			return "int";
		} else if (s.contains("Byte")) {
			return "byte";
		} else if (s.contains("Short")) {
			return "short";
		} else if (s.contains("Long")) {
			return "long";
		} else if (s.contains("Float")) {
			return "float";
		} else if (s.contains("Double")) {
			return "double";
		} else if (s.contains("Char")) {
			return "char";
		} else if (s.contains("String")) {
			return "String";
		} else if (s.contains("Boolean")) {
			return "boolean";
		} else {
			return s;
		}
	}

	/**
	 * Dada una cadena que contiene valores enteros separados por un carácter,
	 * devuelve el array de valores resultante.
	 * 
	 * @param s
	 *            Cadena de entrada.
	 * @param car
	 *            Carácter de separación.
	 * 
	 * @return Array de valores enteros resultante.
	 */
	public static int[] extraerValoresInt(String s, char car) {
		int numElem = 1;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == car) {
				numElem++;
			}
		}

		int arraySalida[] = new int[numElem];

		s = s.replace(" ", "");

		for (int i = 0; i < numElem; i++) {
			try {
				if (s.contains("" + car)) {
					int posBarra = s.indexOf(car);
					arraySalida[i] = Integer.parseInt(s.substring(0, posBarra));
					s = s.substring(s.indexOf(car) + 1, s.length());
				} else {
					arraySalida[i] = Integer.parseInt(s);
				}
			} catch (Exception e) {
				// e.printStackTrace();
				return null;
			}
		}
		return arraySalida;
	}

	/**
	 * Dada una cadena que contiene valores long separados por un carácter,
	 * devuelve el array de valores resultante.
	 * 
	 * @param s
	 *            Cadena de entrada.
	 * @param car
	 *            Carácter de separación.
	 * 
	 * @return Array de valores long resultante.
	 */
	public static long[] extraerValoresLong(String s, char car) {
		int numElem = 1;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == car) {
				numElem++;
			}
		}

		long arraySalida[] = new long[numElem];

		s = s.replace(" ", "");

		for (int i = 0; i < numElem; i++) {
			try {
				if (s.contains("" + car)) {
					int posBarra = s.indexOf(car);
					arraySalida[i] = Long.parseLong(s.substring(0, posBarra));
					s = s.substring(s.indexOf(car) + 1, s.length());
				} else {
					arraySalida[i] = Long.parseLong(s.substring(0, s.length()));
				}
			} catch (Exception e) {
				return null;
			}
		}
		return arraySalida;
	}

	/**
	 * Dada una cadena que contiene valores double separados por un carácter,
	 * devuelve el array de valores resultante.
	 * 
	 * @param s
	 *            Cadena de entrada.
	 * @param car
	 *            Carácter de separación.
	 * 
	 * @return Array de valores double resultante.
	 */
	public static double[] extraerValoresDouble(String s, char car) {
		int numElem = 1;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == car) {
				numElem++;
			}
		}

		double arraySalida[] = new double[numElem];

		s = s.replace(" ", "");

		for (int i = 0; i < numElem; i++) {
			try {
				if (s.contains("" + car)) {
					int posBarra = s.indexOf(car);
					arraySalida[i] = Double.parseDouble(s
							.substring(0, posBarra));
					s = s.substring(s.indexOf(car) + 1, s.length());
				} else {
					arraySalida[i] = Double.parseDouble(s.substring(0,
							s.length()));
				}
			} catch (Exception e) {
				return null;
			}
		}
		return arraySalida;
	}

	/**
	 * Dada una cadena que contiene valores char separados por un carácter,
	 * devuelve el array de valores resultante.
	 * 
	 * @param s
	 *            Cadena de entrada.
	 * @param car
	 *            Carácter de separación.
	 * 
	 * @return Array de valores char resultante.
	 */
	public static char[] extraerValoresChar(String s, char car) {
		int numElem = 1;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == car) {
				numElem++;
			}
		}

		char arraySalida[] = new char[numElem];

		s = s.replace(" ", "");

		for (int i = 0; i < numElem; i++) {
			try {
				if (s.contains("" + car)) {
					arraySalida[i] = s.charAt(0);
					s = s.substring(s.indexOf(car) + 1, s.length());
				} else {
					arraySalida[i] = s.charAt(0);
				}
			} catch (Exception e) {
				return null;
			}
		}
		return arraySalida;
	}

	/**
	 * Dada una cadena que contiene valores string separados por un carácter,
	 * devuelve el array de valores resultante.
	 * 
	 * @param s
	 *            Cadena de entrada.
	 * @param car
	 *            Carácter de separación.
	 * 
	 * @return Array de valores string resultante.
	 */
	public static String[] extraerValoresString(String s, char car) {
		int numElem = 1;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == car) {
				numElem++;
			}
		}

		String arraySalida[] = new String[numElem];

		s = s.replace(" ", "");

		for (int i = 0; i < numElem; i++) {
			try {
				if (s.contains("" + car)) {
					int posBarra = s.indexOf(car);
					arraySalida[i] = (s.substring(0, posBarra));
					s = s.substring(s.indexOf(car) + 1, s.length());
				} else {
					arraySalida[i] = (s.substring(0, s.length()));
				}
			} catch (Exception e) {
				return null;
			}
		}
		return arraySalida;
	}

	/**
	 * Dada una cadena que contiene valores boolean separados por un carácter,
	 * devuelve el array de valores resultante.
	 * 
	 * @param s
	 *            Cadena de entrada.
	 * @param car
	 *            Carácter de separación.
	 * 
	 * @return Array de valores boolean resultante.
	 */
	public static boolean[] extraerValoresBoolean(String s, char car) {
		int numElem = 1;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == car) {
				numElem++;
			}
		}

		boolean arraySalida[] = new boolean[numElem];

		s = s.replace(" ", "");

		for (int i = 0; i < numElem; i++) {
			try {
				if (s.contains("" + car)) {
					int posBarra = s.indexOf(car);
					arraySalida[i] = Boolean.parseBoolean(s.substring(0,
							posBarra));
					s = s.substring(s.indexOf(car) + 1, s.length());
				} else {
					arraySalida[i] = Boolean.parseBoolean(s.substring(0,
							s.length()));
				}
			} catch (Exception e) {
				return null;
			}
		}
		return arraySalida;
	}

	/**
	 * Corrige el contenido del valor según la dimensión del tipo especificada.
	 * 
	 * @param valor
	 *            Valor del parámetro.
	 * 
	 * @return Valor corregido.
	 */
	public static String adecuarParametro(String valor, int dimension) {
		if (dimension == 0) {
			valor = adecuarParametroDim0(valor);
		} else if (dimension == 1) {
			valor = adecuarParametroDim1(valor);
		} else {
			valor = adecuarParametroDim2(valor);
		}

		return valor;
	}

	/**
	 * Corrige el contenido del valor para valores de dimensión 0.
	 * 
	 * @param valor
	 *            Valor del parámetro.
	 * 
	 * @return Valor corregido.
	 */
	private static String adecuarParametroDim0(String valor) {
		while (!ServiciosString.esLetra(valor.charAt(0))
				&& valor.charAt(0) != '-' && valor.charAt(0) != '.'
				&& !ServiciosString.esNumero(valor.charAt(0))) {
			valor = valor.substring(1, valor.length());
		}

		while (!ServiciosString.esLetra(valor.charAt(valor.length() - 1))
				&& valor.charAt(0) != '.'
				&& !ServiciosString.esNumero(valor.charAt(valor.length() - 1))) {
			valor = valor.substring(0, valor.length() - 1);
		}

		return valor;
	}

	/**
	 * Corrige el contenido del valor para valores de dimensión 1.
	 * 
	 * @param valor
	 *            Valor del parámetro.
	 * 
	 * @return Valor corregido.
	 */
	private static String adecuarParametroDim1(String valor) {
		String valorNuevo = "{";

		// " { x , x , x , x } "

		// Quitamos llaves
		valor = valor.replace("{", "");
		valor = valor.replace("}", "");

		// Cogemos parámetros y los arreglamos, uno por uno
		String[] valoresNuevos = new String[numVeces(valor, ",") + 1];

		for (int i = 0; i < valoresNuevos.length; i++) {
			if (i < (valoresNuevos.length - 1)) {
				valoresNuevos[i] = valor.substring(0, valor.indexOf(","));
				valor = valor.substring(valor.indexOf(",") + 1, valor.length());
			} else {
				valoresNuevos[i] = valor + "";
			}

			valoresNuevos[i] = adecuarParametroDim0(valoresNuevos[i]);
		}

		// Los insertamos en la cadena final
		for (int i = 0; i < valoresNuevos.length; i++) {
			if (i < (valoresNuevos.length - 1)) {
				valorNuevo = valorNuevo + valoresNuevos[i] + ",";
			} else {
				valorNuevo = valorNuevo + valoresNuevos[i];
			}
		}

		valorNuevo = valorNuevo + "}";

		return valorNuevo;
	}

	/**
	 * Corrige el contenido del valor para valores de dimensión 2.
	 * 
	 * @param valor
	 *            Valor del parámetro.
	 * 
	 * @return Valor corregido.
	 */
	private static String adecuarParametroDim2(String valor) {
		String valorNuevo = "{";

		// " { x , x , x , x } "

		// Quitamos llaves inicial y final
		while (valor.charAt(0) != '{') {
			valor = valor.substring(1, valor.length());
		}
		valor = valor.substring(1, valor.length());

		while (valor.charAt(valor.length() - 1) != '}') {
			valor = valor.substring(0, valor.length() - 1);
		}
		valor = valor.substring(0, valor.length() - 1);

		// Tratamos cada array por separado
		String[] valoresNuevos = new String[numVeces(valor, "{")];

		for (int i = 0; i < valoresNuevos.length; i++) {
			if (i < (valoresNuevos.length - 1)) {
				valoresNuevos[i] = valor.substring(valor.indexOf("{"),
						valor.indexOf("}") + 1);
				valor = valor.substring(valor.indexOf("}") + 1, valor.length());
				valor = valor.substring(valor.indexOf(",") + 1, valor.length());
			} else {
				valoresNuevos[i] = valor + "";
			}

			valoresNuevos[i] = adecuarParametroDim1(valoresNuevos[i]);
		}

		// Los insertamos en la cadena final
		for (int i = 0; i < valoresNuevos.length; i++) {
			if (i < (valoresNuevos.length - 1)) {
				valorNuevo = valorNuevo + valoresNuevos[i] + ",";
			} else {
				valorNuevo = valorNuevo + valoresNuevos[i];
			}
		}

		valorNuevo = valorNuevo + "}";

		return valorNuevo;
	}

	/**
	 * Devuelve el número de veces que una subcadena está contenida en otra.
	 * 
	 * @param cadena
	 *            Cadena
	 * @param muestra
	 *            Subcadena
	 * 
	 * @return Número de veces que 'muestra' está contenida en 'cadena'.
	 */
	public static int numVeces(String cadena, String muestra) {
		int veces = 0;
		while (cadena.contains(muestra)) {
			veces++;
			cadena = cadena
					.substring(cadena.indexOf(muestra) + muestra.length(),
							cadena.length());
		}

		return veces;
	}

	/**
	 * Permite conocer si una cadena tiene al menos una letra o un número en su
	 * contenido.
	 * 
	 * @param cadena
	 *            Cadena de entrada.
	 * 
	 * @return True si la cadena tiene al menos una letra o número, false en
	 *         caso contrario.
	 */
	public static boolean tieneContenido(String cadena) {
		if (cadena == null) {
			return false;
		}

		for (int i = 0; i < cadena.length(); i++) {
			if (esLetra(cadena.charAt(i)) || esNumero(cadena.charAt(i))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Permite obtener la dirección IP de la máquina donde se está ejecutando la
	 * aplicación.
	 * 
	 * @return Dirección IP.
	 */
	public static String direccionIP() {
		InetAddress a = null;
		try {
			a = InetAddress.getByName(InetAddress.getLocalHost()
					.getHostAddress());
		} catch (Exception e) {

		}
		String dir = ("" + a).replace("/", "");

		return dir;
	}
	
	/**
	 * Permite comprobar si dos strings son iguales (Incluyendo posibles valores null)
	 * 
	 * @param s1 Primer string.
	 * @param s2 Segundo string.
	 * 
	 * @return true si son iguales, false en caso contrario.
	 */
	public static boolean sonIguales(String s1, String s2) {
		
		if (s1 == null && s2 == null) {
			return true;
		}
		
		if (s1 != null && s1.equals(s2)) {
			return true;
		}
		
		return false;
	}

}