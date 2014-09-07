package utilidades;

import java.io.IOException;
import java.io.InputStream;

/**
 * Clase de utilidad que permite la ejecución de comandos del sistema.
 */
public class LlamadorSistema {

	/**
	 * Ejecuta el comando especificado. En Java 7, la llamada a Runtime.exec
	 * debe recibir un array de Strings.
	 * 
	 * @param s
	 *            Lista de parámetros del comando.
	 * 
	 * @return Contenido volcado en la salida de error tras la ejecución del
	 *         comando.
	 */
	public static String ejecucionArray(String[] s) {
		String salida = "";

		Process pr = null;

		Runtime runtime = Runtime.getRuntime();
		try {
			pr = runtime.exec(s);
		} catch (IOException ioe) {
			System.out.println("Error LlamadorSistema.ejecucion:");
			ioe.printStackTrace();
			return "";
		}

		// Recogemos la salida de error del programa por terminal
		byte[] bytes = new byte[1];
		InputStream is = pr.getErrorStream();
		int x = 0;
		do {
			try {
				x = is.read(bytes);

			} catch (java.io.IOException ioe) {
				System.out.println("ioe");
			}
			salida = salida + (new String(bytes));

		} while (x > 0);
		try {
			is.close();
		} catch (java.io.IOException ioe) {
		}
		pr.destroy();

		return salida;
	}
}
