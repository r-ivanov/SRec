package utilidades;

import java.io.File;
import java.util.ArrayList;

/**
 * Clase de utilidad para localizar una M�quina Virtual de Java.
 */
public class BuscadorMVJava {

	/**
	 * Permite obtener los directorios donde se encuentra alguna m�quina virtual
	 * de java.
	 * 
	 * @param enWindows
	 *            Si la b�squeda se realiza desde el sistema operativo windows.
	 * 
	 * @return Lista de directorios con una JVM.
	 */
	public static String[] buscador(boolean enWindows) {
		String[] maquinas = new String[0];
		ArrayList<String> maq = new ArrayList<String>(1);
		int nivel = 0;

		if (enWindows) // Usamos sistema de ficheros de Windows, buscamos en C:
		// y D:
		{
			File[] dir = { new File("C:\\Program Files\\java"),
					new File("C:\\Archivos de programa\\java"),
					new File("C:\\java"),

					new File("D:\\Program Files\\java"),
					new File("D:\\Archivos de programa\\java"),
					new File("D:\\java") };

			for (int i = 0; i < dir.length; i++) {
				if (dir[i].exists() && dir[i].isDirectory()) {
					maq.addAll(buscador(dir[i], maq, nivel + 1));
				}
			}
		} else {

		}

		maquinas = new String[maq.size()];

		for (int i = 0; i < maquinas.length; i++) {
			if (!Arrays.contiene(maq.get(i), maquinas)) {
				maquinas[i] = maq.get(i);
			}
		}

		int contador = 0;

		// Miramos si hay posiciones nulas en array maquinas
		for (int i = 0; i < maquinas.length; i++) {
			if (maquinas[i] != null) {
				contador++;
			}
		}

		String[] m = new String[contador];
		contador = 0;

		for (int i = 0; i < maquinas.length; i++) {
			if (maquinas[i] != null) {
				m[contador] = maquinas[i];
				contador++;
			}
		}

		return m;
	}

	/**
	 * Dado un directorio de una posible localizaci�n, comprueba y devuelve los
	 * directorios que contienen una JVM.
	 * 
	 * @param f
	 *            Directorio a comprobar.
	 * @param maq
	 *            Directos previamente a�adidos.
	 * @param nivel
	 *            Nivel de directorio.
	 * 
	 * @return Array con directorios que contienen una JVM.
	 */
	private static ArrayList<String> buscador(File f, ArrayList<String> maq,
			int nivel) {
		File[] ficheros = f.listFiles();

		for (int i = 0; i < ficheros.length; i++) {
			if (ficheros[i].isDirectory()) {
				if (nivel < 4
						&& ((nivel == 1
								&& ficheros[i].getName().toLowerCase()
										.contains("jdk") && ficheros[i]
								.getName().toLowerCase().contains(".6")) || (nivel == 2 && ficheros[i]
								.getName().toLowerCase().contains("bin")))) {
					maq.addAll(buscador(ficheros[i], maq, nivel + 1));
				}
			} else {
				try {
					if (ficheros[i].getName().toLowerCase()
							.contains("javac.exe")) {
						maq.add(new String(ficheros[i].getCanonicalPath()));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return maq;
	}

}
