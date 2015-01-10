package datos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroError;

/**
 * Se encarga de invocar al método para llevar a cabo la ejecución y obtención
 * de la traza
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class Ejecutador {

	/**
	 * Ejecuta el método cuyo nombre se le pasa por parámetro
	 * 
	 * @param clase
	 *            nombre de la clase a la que pertenece el método que se va a
	 *            invocar
	 * @param metodo
	 *            nombre del método que se va a invocar
	 * @param cparametros
	 *            clases que reprensetan las clases a las que pertenecen los
	 *            parametros
	 * @param parametros
	 *            parametros que se le pasarán al método que se va a invocar
	 * @return Mensaje de error si hubo alguno, null en caso contrario.
	 */
	public static String ejecutar(String clase, String metodo,
			Class cparametros[], Object parametros[]) {
		
		Class c = null;
		ClassLoader cl = ClassLoader.getSystemClassLoader();

		try {
			c = cl.loadClass(clase);
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (java.lang.ClassFormatError cfe) {
			cfe.printStackTrace();
		}

		Method[] mm = c.getDeclaredMethods();

		// Recorrer métodos que hay en esa clase
		for (int x = 0; x < mm.length; x++) {
			if (metodo.equals((mm[x].getName()))) {
				Class[] cp = mm[x].getParameterTypes();
				if (cp.length == 0 || cp.length == cparametros.length) {
					boolean todosIguales = true;

					int z = 0;
					while (z < cp.length && todosIguales == true) {
						if (!(cp[z].equals(cparametros[z]))) {
							todosIguales = false;
						}
						z++;
					}
					if (todosIguales) {
						Object o = null;
						Class cm = mm[x].getDeclaringClass();

						if (cm.isInterface()) {
							return Texto.get("ERROR_CLASEINTERFAZ", Conf.idioma);
						} else if (cm.isArray()) {
							return Texto.get("ERROR_CLASEARRAY", Conf.idioma);
						} else if (cm.isEnum()) {
							return Texto.get("ERROR_CLASEENUM", Conf.idioma);
						} else if (cm.isPrimitive()) {
							return Texto.get("ERROR_CLASEPRIM", Conf.idioma);
						} else {
							boolean instanciacion = false;
							try {
								o = cm.newInstance();
								instanciacion = true;
							} catch (InstantiationException ie) {
							} catch (IllegalAccessException iae) {
							}
							try {
								RegistroActivacion.reinicializar();
								if (instanciacion) {
									// Esta es la correcta
									mm[x].invoke(o, parametros);
								} else {
									mm[x].invoke(new Object(), parametros);
								}
								return null;
							} catch (java.lang.OutOfMemoryError oome) {
								return Texto.get("ERROR_JAVASINMEM", Conf.idioma);
							} catch (IllegalAccessException iae) {
								return Texto.get("ERROR_ILEGALDATOS", Conf.idioma);
							} catch (InvocationTargetException ite) {
								Traza traza = Traza.singleton();
								traza.vaciarTraza();
								String causa = "" + ite.getCause() + "";
								if (causa.contains("emory")
										|| causa.contains("heap space")) {
									return Texto.get("ERROR_METEXP", Conf.idioma) + ": " + causa + ".";
								} else if (causa.contains("ThreadDeath")) { 
									/* Esperado si el usuario ha cancelado la ejecución */
								} else {
									return Texto.get("ERROR_METEXP", Conf.idioma) + ": " + causa + ".";
								}
							} catch (Exception e) {
								return Texto.get("ERROR_METERR", Conf.idioma) + ": " + e.getCause();
							}
						}
					}
				}
			}
		}
		return Texto.get("ERROR_DESCONOCIDO", Conf.idioma);
	}
}
