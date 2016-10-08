package utilidades;

import java.io.File;
import java.util.ArrayList;

/**
 * Clase de utilidad para localizar una Máquina Virtual de Java.
 */
public class BuscadorMVJava {

	/**
	 * Permite obtener los directorios donde se encuentra alguna máquina virtual
	 * de java.
	 * 
	 * @param enWindows
	 *            Si la búsqueda se realiza desde el sistema operativo windows.
	 *            
	 * @param versionEspecifica
	 * 			  Si es necesario buscar la versión específica 1.6 de la MV (true)
	 * 				o no
	 * 
	 * @return Lista de directorios con una JVM.
	 */
	public static String[] buscador(boolean enWindows, boolean versionEspecifica) {
		String[] maquinas = new String[0];
		ArrayList<String> maq = new ArrayList<String>(1);
		int nivel = 0;

		if (enWindows) // Usamos sistema de ficheros de Windows, buscamos en C:
		// y D:
		{
			File[] dir = { 	new File("C:\\Program Files\\java"),
							new File("C:\\Archivos de programa\\java"),
							new File("C:\\java"),
							new File("C:\\Program Files (x86)\\Java"),
							new File("C:\\Archivos de programa (x86)\\Java"),
		
							new File("D:\\Program Files\\java"),
							new File("D:\\Archivos de programa\\java"),
							new File("D:\\java"),
							new File("D:\\Program Files (x86)\\Java"),
							new File("D:\\Archivos de programa (x86)\\Java")
					};

			for (int i = 0; i < dir.length; i++) {
				if (dir[i].exists() && dir[i].isDirectory()) {					
					maq.addAll(buscador(dir[i], maq, nivel + 1,versionEspecifica));					
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
	 * Dado un directorio de una posible localización, comprueba y devuelve los
	 * directorios que contienen una JVM.
	 * 
	 * @param f
	 *            Directorio a comprobar.
	 * @param maq
	 *            Directos previamente añadidos.
	 * @param nivel
	 *            Nivel de directorio.
	 *            
	 * @param versionEspecifica
	 * 			  Si es necesario buscar la versión específica 1.6 de la MV (true)
	 * 				o no
	 * 
	 * @return Array con directorios que contienen una JVM.
	 */
	private static ArrayList<String> buscador(File f, ArrayList<String> maq,
			int nivel, boolean versionEspecifica) {
		File[] ficheros = f.listFiles();

		for (int i = 0; i < ficheros.length; i++) {			
			if (ficheros[i].isDirectory()) {
				if(versionEspecifica){
					if (nivel < 4
							&& ((nivel == 1
									&& ficheros[i].getName().toLowerCase()
											.contains("jdk") && ficheros[i]
									.getName().toLowerCase().contains(".6")) || (nivel == 2 && ficheros[i]
									.getName().toLowerCase().contains("bin")))) {
						maq.addAll(buscador(ficheros[i], maq, nivel + 1,true));
					}
				}else{
					if (nivel < 4
							&& ((nivel == 1
									&& ficheros[i].getName().toLowerCase()
											.contains("jdk")) || (nivel == 2 && ficheros[i]
									.getName().toLowerCase().contains("bin")))) {
						maq.addAll(buscador(ficheros[i], maq, nivel + 1,false));
					}
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
