package utilidades;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

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
	 *         Lista de parámetros del comando.
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
	
	/**
	 * Obtiene un error detallado al compilar un fichero .java
	 * 
	 * @param source
	 * 		Ruta que contiene el archivo a compilar
	 * @param mv
	 * 		Ruta que contiene el jdk
	 * @return
	 * 		String con el error detallado
	 */
	public static String getErrorDetallado(String source, String mv) { 
		String retorno = "";
		String mvCorregida="";
		
		mvCorregida = mv.substring(0,mv.length()-2);
		
		if(!SsooValidator.isUnix()){	//	No Linux			
			mvCorregida = mvCorregida.substring(0,mvCorregida.lastIndexOf('\\'));
		}else{							//	Linux			
			mvCorregida = mvCorregida.substring(0,mvCorregida.lastIndexOf('/'));
		}
		
		System.setProperty("java.home", mvCorregida);
		String sourceFile = "";
		if(!SsooValidator.isUnix())		//	No Linux
			sourceFile = source.substring(1,source.length()-1);
		else							//	Linux
			sourceFile = source;
	    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
	    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

	    List<File> sourceFileList = new ArrayList<File>();
	    sourceFileList.add(new File(sourceFile));
	    Iterable<? extends JavaFileObject> compilationUnits = fileManager
	        .getJavaFileObjectsFromFiles(sourceFileList);
	    CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
	    task.call();
	    List<Diagnostic<? extends JavaFileObject>> diagnosticList = diagnostics.getDiagnostics();
	    
	    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticList) {
	      retorno += Texto.get("COMPILAR_FICHERO",Conf.idioma) +
	    		  diagnostic.getSource().getName() + "\n";
	      retorno += Texto.get("COMPILAR_LINEA",Conf.idioma) +
	    		  diagnostic.getLineNumber()+ "\n";
	      retorno += Texto.get("COMPILAR_COLUMNA",Conf.idioma) +
	    		  diagnostic.getColumnNumber()+ "\n";
	      retorno += Texto.get("COMPILAR_ERROR_RESUMEN",Conf.idioma) +
	    		  diagnostic.getMessage(null)+ "\n";
	      retorno += Texto.get("COMPILAR_ERROR_AMPLIADO",Conf.idioma) +
	    		  diagnostic.toString()+"\n"+
	    		  Texto.get("COMPILAR_SEPARADOR",Conf.idioma)
	    		  + "\n";
	    }
	    try {
			fileManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	    return retorno;
    } 
}
