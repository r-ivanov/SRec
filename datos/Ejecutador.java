package datos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroTerminal;

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
						
						//	Inicializaciones terminal
						
						CuadroTerminal terminal =
								Ventana.thisventana.getCuadroTerminal();
						
						ByteArrayOutputStream terminalSalidaNormal = 
								terminal.getSalidaNormal();
						
						ByteArrayOutputStream terminalSalidaError =
								terminal.getSalidaError();
						
						PrintStream terminalSalidaNormalWriter = 
								new PrintStream(terminalSalidaNormal);
						
						PrintStream terminalSalidaErrorWriter =
								new PrintStream(terminalSalidaError);
						
						//	Guardamos salidas sistema para luego
						
						PrintStream psOut = System.out;
						PrintStream psErr = System.err;	

						//	Escribimos clase y método en cabecera terminal
						
						String cabecera = "";
						
						cabecera = 
							Texto.get("TER_CLASE", Conf.idioma)+" "+cm.getName()+"\n"+
							Texto.get("TER_METODO", Conf.idioma)+" "+mm[x].getName()+"("
						;
						
						for(Class<?> parameterType : mm[x].getParameterTypes()) {
							cabecera = cabecera + parameterType.getName()+", ";
						}
						
						cabecera = cabecera.substring(0, cabecera.length()-2);
						cabecera = cabecera + ") \n";
						
						terminal.setSalidaCabecera(cabecera);
						
						//	Errores
						
						if (cm.isInterface()) {
							String error = Texto.get("ERROR_CLASEINTERFAZ", Conf.idioma);
							terminalSalidaErrorEscribir(terminalSalidaError,error+"\n");
							cerrarTerminal(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter);
							return error;
						} else if (cm.isArray()) {
							String error = Texto.get("ERROR_CLASEARRAY", Conf.idioma);
							terminalSalidaErrorEscribir(terminalSalidaError,error+"\n");
							cerrarTerminal(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter);
							return error;
						} else if (cm.isEnum()) {
							String error = Texto.get("ERROR_CLASEENUM", Conf.idioma);
							terminalSalidaErrorEscribir(terminalSalidaError,error+"\n");
							cerrarTerminal(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter);
							return error;
						} else if (cm.isPrimitive()) {
							String error = Texto.get("ERROR_CLASEPRIM", Conf.idioma);
							terminalSalidaErrorEscribir(terminalSalidaError,error+"\n");
							cerrarTerminal(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter);
							return error;
						} else {
							boolean instanciacion = false;
							try {
								o = cm.newInstance();
								instanciacion = true;
							} catch (InstantiationException ie) {
							} catch (IllegalAccessException iae) {
							}
							try {
								
								//	Redirigimoa salidas sistema a terminal
								
								System.setOut(terminalSalidaNormalWriter);
								System.setErr(terminalSalidaErrorWriter);
								
								RegistroActivacion.reinicializar();								
								
								//	Ejecución métodos
								
								if (instanciacion) {
									// Esta es la correcta
									mm[x].invoke(o, parametros);
								} else {
									mm[x].invoke(new Object(), parametros);
								}
								
								//	Reestablecemos salidas a sistema
								
								System.setOut(psOut);
								System.setErr(psErr);
								
								cerrarTerminal(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter);
								
								return null;
							
							//	Errores
								
							} catch (java.lang.OutOfMemoryError oome) {
								System.setOut(psOut);
								System.setErr(psErr);
								String error = Texto.get("ERROR_JAVASINMEM", Conf.idioma);
								terminalSalidaErrorEscribir(terminalSalidaError,error+"\n"+oome.getCause());
								cerrarTerminal(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter);
								return error;
							} catch (IllegalAccessException iae) {
								System.setOut(psOut);
								System.setErr(psErr);
								String error = Texto.get("ERROR_ILEGALDATOS", Conf.idioma);
								terminalSalidaErrorEscribir(terminalSalidaError,error+"\n"+iae.getCause());
								cerrarTerminal(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter);
								return error;
							} catch (InvocationTargetException ite) {
								System.setOut(psOut);
								System.setErr(psErr);
								Traza traza = Traza.singleton();
								traza.vaciarTraza();
								String causa = "" + ite.getCause() + "";
								if (causa.contains("emory")
										|| causa.contains("heap space")) {
									String error = Texto.get("ERROR_METEXP", Conf.idioma)+"\n"+causa;
									terminalSalidaErrorEscribir(terminalSalidaError,error+"\n"+ite.getCause());
									cerrarTerminal(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter);
									return error;
								} else if (causa.contains("ThreadDeath")) { 
									/* Esperado si el usuario ha cancelado la ejecución */
									String error = Texto.get("ERROR_CANCELADO", Conf.idioma);
									terminalSalidaErrorEscribir(terminalSalidaError,error+"\n"+ite.getCause());
									cerrarTerminal(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter);
									return error;
								} else {
									String error = Texto.get("ERROR_METEXP", Conf.idioma);
									terminalSalidaErrorEscribir(terminalSalidaError,error+"\n"+ite.getCause());
									cerrarTerminal(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter);
									return error;
								}
							} catch (Exception e) {
								System.setOut(psOut);
								System.setErr(psErr);
								String error = Texto.get("ERROR_METERR", Conf.idioma);
								terminalSalidaErrorEscribir(terminalSalidaError,error+"\n"+e.getCause());
								cerrarTerminal(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter);
								return error;
							}
						}
					}
				}
			}
		}
		String error = Texto.get("ERROR_DESCONOCIDO", Conf.idioma);		
		return error;
	}
	
	/**
	 * Cierra todos los componentes abiertos de la terminal
	 * 
	 * @param o1
	 * 		ByteArrayOutputStream error
	 * 
	 * @param o2
	 * 		ByteArrayOutputStream normal
	 * 
	 * @param p1
	 * 		PrintStream error
	 * 
	 * @param p2
	 * 		PrintStream normal
	 * 
	 */
	private static void cerrarTerminal(ByteArrayOutputStream o1, ByteArrayOutputStream o2, PrintStream p1, PrintStream p2) {
		try {
			o1.close();
			o2.close();
			p1.close();
			p2.close();
		} catch (IOException e) {
		}
		
	}
	
	/**
	 * Escribe en la salida de error de la terminal
	 * 
	 * @param b
	 * 		ByteArrayOutputStream donde escribiremos (error o normal)
	 * 
	 * @param s
	 * 		String a escribir
	 */
	private static void terminalSalidaErrorEscribir(ByteArrayOutputStream b, String s) {
		try {
			b.write(s.getBytes());
		} catch (IOException e) {
			
		}
	}
}
