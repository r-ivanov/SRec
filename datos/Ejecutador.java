/**
	Se encarga de invocar al método para llevar a cabo la ejecución y obtención de la traza
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/
package datos;




import java.lang.Class;
import java.lang.ClassLoader;
import java.lang.ClassNotFoundException;
import java.lang.Exception;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.OutOfMemoryError;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JFrame;


import conf.*;
import cuadros.*;
import utilidades.*;
import ventanas.*;


public class Ejecutador
{
	private static boolean depurar = false;

	
	/*public static void main(String args[]) 
	{
		// Arrays contenedores
		Class[] cp=new Class[2];
		Object[] p=new Object[2];

		// Parámetros
		Integer p1=new Integer(8);
		Character p2=new Character('d');

		// Agrupamos clases de parámetros
		cp[0]=p1.getClass();
		cp[1]=p2.getClass();

		// Agrupamos parámetros
		p[0]=p1;
		p[1]=p2;

		Ejecutador.ejecutar("Fibbonaci","fibbonaci",cp,p);
	}*/

	
	/**
		ejecuta el método cuyo nombre se le pasa por parámetro
		
		@param clase nombre de la clase a la que pertenece el método que se va a invocar
		@param metodo nombre del método que se va a invocar
		@param cparametros clases que reprensetan las clases a las que pertenecen los parametros
		@param parametros parametros que se le pasarán al método que se va a invocar
		@return true si la ejecución resulta satisfactoria
	*/
	public static boolean ejecutar(String clase, String metodo, Class cparametros[], Object parametros[])
	{
		CuadroError ce;
		//Object objeto = new Object();

		if (depurar) System.out.println("Ejecutador.Inicio:");
		if (depurar) System.out.println("Clase: "+clase+"\nMetodo: "+metodo);

		if (depurar) System.out.println("Ejecutador.Paso 01: Delaracion variables");

		Class c=null;
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		if (depurar) System.out.println("Ejecutador.Paso 02: Todo preparado");

		try {
			c = cl.loadClass(clase);
			if (depurar) System.out.println("Ejecutador.Paso 03: Clase cargada en c");
		} catch (ClassNotFoundException cnfe) {
			if (depurar) System.out.println("Ejecutador.Excepcion 01. No se ha podido cargar la clase\n  "+clase);
			cnfe.printStackTrace();	
		} catch (java.lang.ClassFormatError cfe) {
			if (depurar) System.out.println("Ejecutador.Excepcion 01B. No se ha podido cargar la clase\n  "+clase);
			cfe.printStackTrace();	
		}

		Method[] mm=c.getDeclaredMethods();

		if (depurar) System.out.println("Ejecutador.Paso 04: Metodos en mm:"+mm.length);
		for (int x=0; x<mm.length; x++) // Recorrer métodos que hay en esa clase
		{
			if (depurar) System.out.println("Nombre metodo ("+x+"): "+mm[x].getName());
			if ( metodo.equals( (mm[x].getName()) ) )
			{
				if (depurar) System.out.println("Ejecutador.Paso 05: Coincidencia en nombre de metodo");
				Class[] cp = mm[x].getParameterTypes();
				if (depurar) System.out.println("Longitud cp="+cp.length);
				if (cp.length==0 || cp.length==cparametros.length)
				{
					if (depurar) System.out.println("Ejecutador.Paso 06: Coincidencia en longitud de parametros");
					boolean todosIguales=true;

					int z=0;
					while (z<cp.length && todosIguales==true)
					{
						if (!(cp[z].equals(cparametros[z])))
							todosIguales=false;
						if (depurar)
						{
							System.out.println(" Param ["+z+"]:"+cparametros[z].getCanonicalName()+
											"  ("+ServiciosString.representacionObjeto(parametros[z])+")");
						}
						z++;
					}
					if (todosIguales)
					{
						Object o=null;
						Class cm=mm[x].getDeclaringClass();
						if (depurar) System.out.println("Ejecutador.Paso 07: Coincidencia en clases de parametros");
						if (depurar) System.out.println("Ejecutador.Paso 08: Invocacion");
							
						if (cm.isInterface())
							ce= new CuadroError((JFrame)Ventana.thisventana, Texto.get("ERROR_EJEC",Conf.idioma),
							Texto.get("ERROR_CLASEINTERFAZ",Conf.idioma));
						else if (cm.isArray())
							ce= new CuadroError((JFrame)Ventana.thisventana, Texto.get("ERROR_EJEC",Conf.idioma),
							Texto.get("ERROR_CLASEARRAY",Conf.idioma));
						else if (cm.isEnum())
							ce= new CuadroError((JFrame)Ventana.thisventana, Texto.get("ERROR_EJEC",Conf.idioma),
							Texto.get("ERROR_CLASEENUM",Conf.idioma));
						else if (cm.isPrimitive())
							ce= new CuadroError((JFrame)Ventana.thisventana, Texto.get("ERROR_EJEC",Conf.idioma),
							Texto.get("ERROR_CLASEPRIM",Conf.idioma));
						else
						{
							boolean instanciacion=false;
							try {
								o=cm.newInstance();
								instanciacion=true;
							} catch (InstantiationException ie) {
							} catch (IllegalAccessException iae) {
							}
							try {
								if (instanciacion)
								{
									if (depurar) System.out.println("Ejecutador: si ha habido instanciacion");
									mm[x].invoke(o,parametros);	// Esta es la correcta
								}
								else
								{
									if (depurar) System.out.println("Ejecutador: no ha habido instanciacion");
									mm[x].invoke(new Object(),parametros);
								}
								return true;
							} catch (java.lang.OutOfMemoryError oome) {
								if (depurar) System.out.println("Ejecutador.Paso 09: Error 01.\n\n\nJava ha excedido la memoria disponible. Cierre y reabra Visualizador.\n\n\n");
								//ite.printStackTrace();
								ce = new CuadroError((JFrame)Ventana.thisventana, Texto.get("ERROR_EJEC",Conf.idioma),
								Texto.get("ERROR_JAVASINMEM",Conf.idioma));
							} catch (IllegalAccessException iae) {
								if (depurar) System.out.println("Ejecutador.Paso 09: Excepcion 02");
								//iae.printStackTrace();
								ce= new CuadroError((JFrame)Ventana.thisventana, Texto.get("ERROR_EJEC",Conf.idioma),
								Texto.get("ERROR_ILEGALDATOS",Conf.idioma));
							} catch (InvocationTargetException ite) {
								if (depurar) System.out.println("Ejecutador.Paso 09: Excepcion 03");
								Traza traza=Traza.singleton();
								traza.vaciarTraza();
								/*ite.printStackTrace();
								StackTraceElement[] pilas=ite.getStackTrace();
								
								if (depurar)
									for (int i=0; i<pilas.length; i++)
										System.out.println(" - pila "+i+": "+pilas[i].getClassName()+"/"+pilas[i].getMethodName()+"/"+pilas[i].getLineNumber()+"/");
								 */
								String causa = ""+ite.getCause()+"";
								if (depurar) System.out.println(" - "+ite.getMessage()+" - ");
								if (causa.contains("emory") || causa.contains("heap space"))
									ce= new CuadroError((JFrame)Ventana.thisventana, Texto.get("ERROR_EJEC",Conf.idioma),
									Texto.get("ERROR_METEXP",Conf.idioma)+": "+causa+".");
								else
									ce= new CuadroError((JFrame)Ventana.thisventana, Texto.get("ERROR_EJEC",Conf.idioma),
									Texto.get("ERROR_METEXP",Conf.idioma)+": "+causa+".");
							} catch (Exception e) {
								if (depurar) System.out.println("Ejecutador.Paso 09: Excepcion 04");
								//e.printStackTrace();
								ce= new CuadroError((JFrame)Ventana.thisventana, Texto.get("ERROR_EJEC",Conf.idioma),
								Texto.get("ERROR_METERR",Conf.idioma)+": "+e.getCause());
							}
						}
					}
				}
			}
		}
		if (depurar) System.out.println("Ejecutador.Paso 10: Fin (FALLO)");
		return false;
	}
}
