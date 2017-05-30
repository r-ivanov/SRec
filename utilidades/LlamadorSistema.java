package utilidades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import conf.Conf;

/**
 * Clase de utilidad que permite la ejecución de comandos del sistema.
 */
public class LlamadorSistema {

	/**
	 * Ejecuta el comando especificado. En Java 7, la llamada a Runtime.exec
	 * debe recibir un array de Strings.
	 * 
	 * @param s
	 * 		Lista de parámetros del comando.
	 * 
	 * @return 
	 * 		Contenido volcado en la salida de error tras la ejecución del
	 *      comando.
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
	
	/**
	 * Obtiene un error detallado al compilar un fichero .java
	 * 	y las líneas donde ha habido error
	 * 
	 * @param comando
	 * 		Comando que ejecuta el fichero java
	 * 
	 * @return
	 * 		Lista de string donde:
	 * 			- Pos 0 = Error formateado listo para el panelCompilador
	 * 			- Resto = Líneas donde ha habido errores, 
	 * 				se comprueba aquí que son números
	 * 
	 * @throws IOException 
	 */
	public static List<String> getErrorDetallado(String[] comando) throws IOException { 
		Process proceso = Runtime.getRuntime().exec(comando);
		String retornoString = "";		
		List<String> listaRetorno = new ArrayList<>();
		int contador = 0;
		
        try {
            proceso.waitFor();
        }catch (InterruptedException ex) {
        	
        }
        
        BufferedReader br = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
        while (br.ready()){
            String aux = br.readLine();
            if (aux.compareTo("") != 0){
                retornoString+=aux+"\n";
            }
        }
        
        br = new BufferedReader(new InputStreamReader(proceso.getErrorStream()));   	
        while (br.ready()){        	
            String aux = br.readLine();
            String[] parseo = Parsear(aux, contador);  
            
           	if((parseo.length == 3) && (aux.compareTo("") != 0)){
           		
           		try{
                	Integer.parseInt(parseo[1]);
                }catch(Exception e){
                	continue;
                }
                
           		retornoString+="\n"+Texto.get("COMPILAR_SEPARADOR",Conf.idioma);
           		retornoString+="\n" + Texto.get("COMPILAR_FICHERO",Conf.idioma);
           		retornoString+=parseo[0];
           		retornoString+="\n" + Texto.get("COMPILAR_LINEA",Conf.idioma);
           		retornoString+=parseo[1];           		
           		retornoString+="\n" + Texto.get("COMPILAR_ERROR",Conf.idioma);
           		retornoString+=parseo[2] + "\n";           		
           		
           		listaRetorno.add(parseo[1]);           		
           		
           	}else if((aux.indexOf("error")!=-1) && (aux.length()<9)){ 	//línea errores
           		retornoString+=parseo[0];
            }else if(aux.indexOf("^")!=-1){ 							//línea 2 código
            	retornoString+=parseo[0];
            }else{ 														//línea 1 código
            	retornoString+=parseo[0] + "\n";
            }
           	
            if (aux.compareTo("") == 0){
            	retornoString+=aux+"\n";
            }
            
            contador++;            
        }
        
        retornoString = invertirTotalErrores(retornoString);
        listaRetorno.add(0, retornoString);
        return listaRetorno;
    } 
	
	/**
	 * Parsea una línea de la salida de errores
	 * 
	 * @param aux
	 * 		Línea
	 * 
	 * @param auxLinea
	 * 		Número línea
	 * 
	 * @return
	 * 		Error parseado
	 */
	private static String[] Parsear(String aux, int auxLinea){
		String[] array = new String[1];
		
		if((aux.indexOf("error")!=-1) && (aux.length()<9)){ 
			array = new String[1];
			int numErrores = aux.indexOf("error");
			String numErroresString = aux.substring(0,numErrores);
			array[0]="\n"+Texto.get("COMPILAR_SEPARADOR",Conf.idioma);
			array[0]+= "\n"+Texto.get("COMPILAR_NUM_ERROR",Conf.idioma) + numErroresString;
			array[0]+="\n"+Texto.get("COMPILAR_SEPARADOR",Conf.idioma);
			return array;
		}else{		
			if(auxLinea%3 == 0){
				array = new String[3];
				array = ParsearTipo0(aux);		
				return array;
			}else if(auxLinea%3 == 1){
				array = new String[1];
				array[0] = aux;
				return array;
			}else if(auxLinea%3 == 2){
				array = new String[1];
				array[0] = aux;
				return array;
			}
		}
		return array;
	}
	
	/**
	 * Parsea cuando realmente es una línea de error
	 * 
	 * @param aux
	 * 		Línea de error a parsear
	 * 
	 * @return
	 * 		array = {fichero, linea, error}
	 */
	private static String[] ParsearTipo0(String aux){ 
		int finRuta = aux.indexOf(".java") + 5;
		String fichero = aux.substring(0,finRuta);
		int finLinea = aux.indexOf(": ");
		
		String linea = aux.substring(finRuta+1,finLinea);
		String error = aux.substring(finLinea+1, aux.length());
		
		String[] array = {fichero, linea, error};
		return array;
	}
	
	/**
	 * 	Método que pone el total de errores arriba, 
	 * 	en vez de abajo de todo el texto del compilador
	 * 
	 * @param errores
	 * 		String con todos los errores parseados y formateados
	 * 
	 * @return
	 * 		Mismo que error pero con TOTAL ERRORES al principio
	 */
	private static String invertirTotalErrores(String errores){
		if(!errores.contains(Texto.get("COMPILAR_NUM_ERROR",Conf.idioma))){
			return errores;
		}
		String temp = errores.substring(0,errores.lastIndexOf('\n'));
		String tempErrores = temp.substring(0,temp.lastIndexOf('\n'));
		String tempErrores2 = temp.substring(tempErrores.length(),errores.lastIndexOf('\n'));
		tempErrores2 = tempErrores2.substring(1);
		tempErrores = 
				Texto.get("COMPILAR_SEPARADOR",Conf.idioma) + "\n"+
				tempErrores2 + 				
				tempErrores;
		return tempErrores;
	}
}


