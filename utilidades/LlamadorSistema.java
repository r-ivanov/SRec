package utilidades;

import java.io.IOException;
import java.io.InputStream;

import org.omg.CORBA.portable.OutputStream;

public class LlamadorSistema {

	
	//En Java 7, la llamada a Runtime.exec debe recibir un array de Strings.
	public static String ejecucionArray(String[] s)
	{
		String salida="";
		
		Process pr=null;
		
		Runtime runtime = Runtime.getRuntime();
		try {
			pr=runtime.exec(s);
		} catch (IOException ioe) {
			System.out.println("Error LlamadorSistema.ejecucion:");
			ioe.printStackTrace();
			return "";
		}

		// Recogemos la salida de error del programa por terminal
		byte[] bytes=new byte[1];
		InputStream is=pr.getErrorStream();
		int x=0;
		do
		{
			try {
				x=is.read(bytes);
				
			} catch (java.io.IOException ioe) {
				System.out.println("ioe");
			}
			salida=salida+(new String(bytes));

		}
		while (x>0);
		try {
			is.close();
		} catch (java.io.IOException ioe) {
		}
		pr.destroy();

		return salida;
	}

	public static String ejecucion(String s)
	{
		String salida="";
		
		Process pr=null;
		
		Runtime runtime = Runtime.getRuntime();
		try {
			pr=runtime.exec(s);
		} catch (IOException ioe) {
			System.out.println("Error LlamadorSistema.ejecucion:");
			ioe.printStackTrace();
			return "";
		}

		// Recogemos la salida de error del programa por terminal
		byte[] bytes=new byte[1];
		InputStream is=pr.getErrorStream();
		int x=0;
		do
		{
			try {
				x=is.read(bytes);
				
			} catch (java.io.IOException ioe) {
				System.out.println("ioe");
			}
			salida=salida+(new String(bytes));

		}
		while (x>0);
		try {
			is.close();
		} catch (java.io.IOException ioe) {
		}
		pr.destroy();

		return salida;
	}
	
	public static String ejecucionS(String s)
	{
		String salida="";
		
		Process pr=null;
		
		Runtime runtime = Runtime.getRuntime();
		try {
			pr=runtime.exec(s);
		} catch (IOException ioe) {
			System.out.println("Error LlamadorSistema.ejecucion:");
			ioe.printStackTrace();
			return "";
		}

		// Recogemos la salida de error del programa por terminal
		byte[] bytes=new byte[1];
		InputStream is=pr.getInputStream();
		int x=0;
		do
		{
			try {
				x=is.read(bytes);
				
			} catch (java.io.IOException ioe) {
				System.out.println("ioe");
			}
			salida=salida+(new String(bytes));

		}
		while (x>0);
		try {
			is.close();
		} catch (java.io.IOException ioe) {
		}
		pr.destroy();

		return salida;
	}
	
}
