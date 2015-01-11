package datos;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utilidades.ServiciosString;

/**
 * Permite parsear par�metros de entrada para una ejecuci�n, determinando el
 * n�mero de ejecuciones necesarias.
 * 
 * @author David Pastor Herranz
 */
public class ParametrosParser {

	private MetodoAlgoritmo metodoAlgoritmo;

	/**
	 * Devuelve una nueva instancia.
	 * 
	 * @param metodoAlgoritmo
	 *            M�todo algoritmo asociado.
	 */
	public ParametrosParser(MetodoAlgoritmo metodoAlgoritmo) {
		this.metodoAlgoritmo = metodoAlgoritmo;
	}

	/**
	 * Determina el n�mero de ejecuciones necesarias para los par�metros de
	 * entrada especificados.
	 * 
	 * @return N�mero de ejecuciones necesarias.
	 */
	private int determinarCombinaciones() {
		int combinaciones = 1;
		for (int i = 0; i < this.metodoAlgoritmo.getNumeroParametros(); i++) {
			String valorParametro = this.metodoAlgoritmo.getParamValor(i);
			int numeroValores = reemplazarYPartirValores(valorParametro).size();
			combinaciones *= numeroValores;
		}
		return combinaciones;
	}

	/**
	 * Dado un valor para un par�metro de entrada, si este contiene m�ltiples
	 * par�metros, los parte y devuelve los distintos par�metros separados en
	 * una lista.
	 * 
	 * @param cadenaEntrada
	 * 
	 * @return Lista de valores
	 */
	public static List<String> reemplazarYPartirValores(String cadenaEntrada) {

		Pattern p = Pattern.compile("(-?[0-9]+)\\.\\.(-?[0-9]+)");
		Matcher matcher = p.matcher(cadenaEntrada);
		StringBuffer sb = new StringBuffer(cadenaEntrada.length());
		while (matcher.find()) {
			int digito1 = Integer.parseInt(matcher.group(1));
			int digito2 = Integer.parseInt(matcher.group(2));
			String secuencia = "";

			if (digito2 >= digito1) {
				/* Rango creciente */
				for (int i = digito1; i <= digito2; i++) {
					secuencia += i;
					if (i != digito2) {
						secuencia += ",";
					}
				}
			} else {
				/* Rango decreciente */
				for (int i = digito1; i >= digito2; i--) {
					secuencia += i;
					if (i != digito2) {
						secuencia += ",";
					}
				}
			}
			matcher.appendReplacement(sb, Matcher.quoteReplacement(secuencia));
		}
		matcher.appendTail(sb);
		cadenaEntrada = sb.toString();

		ArrayList<String> valores = new ArrayList<String>();
		int balanceoArray = 0;
		boolean enCadena = false;
		int ultimoValor = 0;
		for (int i = 0; i < cadenaEntrada.length(); i++) {
			char caracter = cadenaEntrada.charAt(i);
			if (caracter == '{') {
				balanceoArray++;
			} else if (caracter == '}') {
				balanceoArray--;
			} else if (caracter == '"') {
				enCadena = !enCadena;
			} else if (caracter == ',' && balanceoArray == 0 && !enCadena) {
				valores.add(cadenaEntrada.substring(ultimoValor, i).trim());
				ultimoValor = i + 1;
			}
		}
		valores.add(cadenaEntrada
				.substring(ultimoValor, cadenaEntrada.length()).trim());

		return valores;
	}

	/**
	 * Determina si los valores pasados por par�metro, cumplen las restricciones
	 * de tipo y dimensi�n especificadas.
	 * 
	 * @param valores
	 *            Lista de valores de entrada.
	 * @param tipo
	 *            Tipo de los valores.
	 * @param dim
	 *            Dimensi�n de los valores.
	 * 
	 * @return true si todos los valores son del tipo y dimensi�n especificados.
	 */
	public static boolean comprobarValoresParametro(List<String> valores,
			String tipo, int dim) {
		for (int i = 0; i < valores.size(); i++) {
			if (!ServiciosString.esDeTipoCorrecto(valores.get(i), tipo, dim)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Devuelve una matriz con el producto cartesiano de todos los valores
	 * especificados para todos los par�metros, representando en cada fila, los
	 * valores de cada ejecuci�n individual.
	 * 
	 * @return Matriz con valores de ejecuci�n por cada fila.
	 */
	public String[][] obtenerMatrizParametros() {
		int combinaciones = this.determinarCombinaciones();
		String[][] matrizParametros = new String[combinaciones][];

		for (int i = 0; i < combinaciones; i++) {
			matrizParametros[i] = new String[this.metodoAlgoritmo
					.getNumeroParametros()];
		}

		int productoCombinacionesAcumulado = 1;
		for (int numeroParametro = 0; numeroParametro < this.metodoAlgoritmo
				.getNumeroParametros(); numeroParametro++) {
			String valorParametro = this.metodoAlgoritmo
					.getParamValor(numeroParametro);
			List<String> valores = reemplazarYPartirValores(valorParametro);

			int repeticionesPorValor = combinaciones / valores.size()
					/ productoCombinacionesAcumulado;
			productoCombinacionesAcumulado *= valores.size();

			for (int i = 0; i < combinaciones; i++) {
				int posicionValor = (i / repeticionesPorValor) % valores.size();
				matrizParametros[i][numeroParametro] = valores
						.get(posicionValor);
			}
		}

		return matrizParametros;
	}

	/**
	 * Devuelve el nombre de los par�metros de entrada.
	 * 
	 * @return Lista con el nombre de los par�metros de entrada.
	 */
	public String[] obtenerNombresParametros() {
		return this.metodoAlgoritmo.getNombreParametros();
	}

	/**
	 * Devuelve el nombre del m�todo.
	 * 
	 * @return Nombre del m�todo.
	 */
	public String obtenerNombreMetodo() {
		return this.metodoAlgoritmo.getNombre();
	}
}
