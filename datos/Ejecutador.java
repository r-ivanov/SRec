package datos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroTerminal;

/**
 * Se encarga de invocar al método para llevar a cabo la ejecución y obtención
 * de la traza
 * 
 * @author Antonio Pérez Carrasco y Daniel Arroyo Cortés
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

						//	Escribimos cabecera = clase y método
						
						String claseReal = Ventana.thisventana.getPreprocesador().getClaseProcesada()[1];
						
						 String cabecera = claseReal.replaceAll(".java", "")+"."+mm[x].getName()+"("
						;
						 
						for(Object obj : parametros) {
							cabecera = cabecera + obj.toString()+", ";
						}
						
						cabecera = cabecera.substring(0, cabecera.length()-2);
						
						if(parametros.length > 0)
							cabecera = "\n"+cabecera + ") \n";
						else
							cabecera = "\n"+cabecera + "\n";
						
						terminal.setSalidaCabecera(cabecera);						
						
						//	Errores 1
						
						if (cm.isInterface()) {
							String error = Texto.get("ERROR_CLASEINTERFAZ", Conf.idioma);
							terminalEscribir(terminalSalidaError,error+"\n");
							setSalidasFin(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter,terminal);
							return error;
						} else if (cm.isArray()) {
							String error = Texto.get("ERROR_CLASEARRAY", Conf.idioma);
							terminalEscribir(terminalSalidaError,error+"\n");
							setSalidasFin(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter,terminal);
							return error;
						} else if (cm.isEnum()) {
							String error = Texto.get("ERROR_CLASEENUM", Conf.idioma);
							terminalEscribir(terminalSalidaError,error+"\n");
							setSalidasFin(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter,terminal);
							return error;
						} else if (cm.isPrimitive()) {
							String error = Texto.get("ERROR_CLASEPRIM", Conf.idioma);
							terminalEscribir(terminalSalidaError,error+"\n");
							setSalidasFin(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter,terminal);
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
								
								Object returnInvoke;
								
								if (instanciacion) {
									// Esta es la correcta
									returnInvoke = mm[x].invoke(o, parametros);
								} else {
									returnInvoke = mm[x].invoke(new Object(), parametros);
								}								
								
								//	Reestablecemos salidas a sistema y cerramos terminal
								
								System.setOut(psOut);
								System.setErr(psErr);
								
								if(returnInvoke != null)
									terminal.setSalidaResultadoMetodo(mm[x].getReturnType().toString() + " "+returnInvoke.toString());
								else
									terminal.setSalidaResultadoMetodo(mm[x].getReturnType().toString() + " "+Texto.get("TER_RESULTADO_VOID", Conf.idioma));
								
								setSalidasFin(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter,terminal);
								
								return null;
							
							//	Errores 2
								
							} catch (java.lang.OutOfMemoryError oome) {
								System.setOut(psOut);
								System.setErr(psErr);
								String error = Texto.get("ERROR_JAVASINMEM", Conf.idioma);
								terminalEscribir(terminalSalidaError,error+"\n"+oome.getCause()+"\n");
								setSalidasFin(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter,terminal);
								return error;
							} catch (IllegalAccessException iae) {
								System.setOut(psOut);
								System.setErr(psErr);
								String error = Texto.get("ERROR_ILEGALDATOS", Conf.idioma);
								terminalEscribir(terminalSalidaError,error+"\n"+iae.getCause()+"\n");
								setSalidasFin(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter,terminal);
								return error;
							} catch (InvocationTargetException ite) {
								System.setOut(psOut);
								System.setErr(psErr);
								Traza traza = Traza.singleton();
								traza.vaciarTraza();
								String causa = ite.getCause().toString();
								String error = "";
								
								if (causa.contains("emory")
										|| causa.contains("heap space")) {
									error = Texto.get("ERROR_METEXP", Conf.idioma);
								} else if (causa.contains("ThreadDeath")) { 
									/* Esperado si el usuario ha cancelado la ejecución */
									error = Texto.get("ERROR_CANCELADO", Conf.idioma);	
								} else {
									error = Texto.get("ERROR_METEXP", Conf.idioma);
								}
								
								terminalEscribir(terminalSalidaError,error+"\n"+ite.getCause()+"\n");
								setSalidasFin(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter,terminal);
								return error;
							} catch (Exception e) {
								System.setOut(psOut);
								System.setErr(psErr);
								String error = Texto.get("ERROR_METERR", Conf.idioma);
								terminalEscribir(terminalSalidaError,error+"\n"+e.getCause()+"\n");
								setSalidasFin(terminalSalidaError,terminalSalidaNormal,terminalSalidaErrorWriter,terminalSalidaNormalWriter,terminal);
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
	 * Acciones a llevar a cabo cuando se termina de escribir
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
	 * @param terminal
	 * 		Terminal
	 * 
	 */
	private static void setSalidasFin(
			final ByteArrayOutputStream o1,
			final ByteArrayOutputStream o2,
			final PrintStream p1,
			final PrintStream p2,
			final CuadroTerminal terminal) {
		SwingUtilities.invokeLater(new Runnable() {	
	        @Override
	        public void run() {	
	        	try {
	        		
	        		//	Cerramos
	        		
	            	o1.close();
	    			o2.close();
	    			p1.close();
	    			p2.close();
	        	} catch (IOException e) {
	        		
	    		}
	        	
	        	//	Comprobamos si hay que abrir o no terminal
	        	
	        	if(terminal.getSalidasTerminalAbrir()) {
	        		Ventana.thisventana.terminalAbrirCerrar();
	        	}else {
	        		terminal.terminalPrimerPlano();
	        	}
	        	
	        	terminal.setSalidasFin();
	        }
		});				
	}
	
	/**
	 * Escribe en la salida especificada el texto especificado
	 * 
	 * @param b
	 * 		ByteArrayOutputStream donde escribiremos (error o normal)
	 * 
	 * @param s
	 * 		String a escribir
	 */
	private static void terminalEscribir(ByteArrayOutputStream b, String s) {
		try {
			b.write(s.getBytes());
		} catch (IOException e) {
			
		}
	}
}
