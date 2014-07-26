/**
	Gestiona todo el procesamiento de un fichero antes de ser ejecutado y visualizado
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/
package datos;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;


import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import conf.*;
import cuadros.*;
import opciones.*;
import paneles.PanelCompilador;
import toxml.*;
import utilidades.*;
import ventanas.*;


public class Preprocesador extends Thread
{
	ClaseAlgoritmo claseAlgoritmo=null;

	static String fichero[] = new String[2];
	Ventana vv;
	Runtime runtime;
	boolean depurar=false;  			// Depurar
	boolean selecMetodo=false;

	static String claseProcesada[]=new String[2];
	
	GestorOpciones gOpciones=new GestorOpciones();

	OpcionBorradoFicheros obf=null;
	OpcionMVJava omvj=null;
	Document documento;
	CuadroProgreso cuadroProgreso;

	static String ficheroclass, ficherosinex, ficheroxml;
	//CuadroSeleccionMetodos cuadroSeleccionMetodos;
	CuadroError cuadroError; 
	boolean compilado;
	
	static String ahora;
	
	String codigoPrevio;
	
	
	boolean distintaClase=false;	
	// a true si reprocesamos la misma clase que ya estaba cargada, es para no borrar históricos de parámetros introducidos
	
	
	/**
		Constructor: crea un nuevo gestor
	*/
	public Preprocesador()
	{
		fichero[0]=null;
		fichero[1]=null;
		start();
	}
	
	
	/**
		Constructor: crea un nuevo gestor
		
		@param dir path del fichero que hay que preprocesar
		@param nombre nombre del fichero que hay que preprocesar
	*/
	public Preprocesador(String path,String nombre)
	{
		if (path!=null && nombre!=null)
		{
			fichero[0]=path.substring(0,path.lastIndexOf("\\")+1);
			fichero[1]=nombre;
		}
		else
		{
			fichero[0]=null;
			fichero[1]=null;
		}
		start();
	}
	
	public Preprocesador(String path,String nombre, boolean selecMetodo)
	{
		if (path!=null && nombre!=null)
		{
			fichero[0]=path.substring(0,path.lastIndexOf("\\")+1);
			fichero[1]=nombre;
		}
		else
		{
			fichero[0]=null;
			fichero[1]=null;
		}
		this.selecMetodo=selecMetodo;
		start();
	}
	
	
	/**
		Constructor: crea un nuevo gestor
		
		@param path path del fichero que hay que preprocesar
	*/
	public Preprocesador(String []path)
	{
		if (path!=null && path.length==2 && path[0]!=null && path[1]!=null)
		{
			fichero[0]=path[0];
			fichero[1]=path[1];
		}
		else
		{
			fichero[0]=null;
			fichero[1]=null;
		}
		start();
	}	
	
	
	/**
		Método que se encarga de llevar a cabo, en un nuevo thread, todo el procesamiento del fichero
	*/
	public synchronized void run()
	{
		this.vv=Ventana.thisventana;
		this.vv.setTextoCompilador(PanelCompilador.CODIGO_VACIO);
		
		
		
		
		Ventana.setProcesando(true);
	
		//LlamadorSistema.ejecucion("getClasspath.bat");
		
		// Cargamos y manejamos opciones
		omvj = (OpcionMVJava)(gOpciones.getOpcion("OpcionMVJava",true));
		
		if (!omvj.getValida())
		{
			fichero[1]=null;
			new CuadroError(vv, Texto.get("ERROR_CONF",Conf.idioma),Texto.get("ERROR_NOMVJ",Conf.idioma));
				return;
		}
		distintaClase=false;	
		OpcionFicherosRecientes ofr;
		
		// Si no hemos recibido nombre fichero por parametro, sacamos JFileChooser...
		if (fichero[0]==null)		
		{
			ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);
			
			fichero[0]=ofr.getDir();
			fichero=SelecDireccion.cuadroAbrirFichero(fichero[0],Texto.get("VVC_PROCCLAS",Conf.idioma),
						null,"java",Texto.get("ARCHIVO_JAVA",Conf.idioma),1);
			
			// Forzamos a abrir Selector de fichero
			distintaClase=true;
			
			// *1* Comprobar que fichero existe
			if (fichero==null || fichero[1]==null)
			{
				if (Conf.fichero_log) Logger.log_write("Preprocesador: no se procesará clase.");
				Ventana.setProcesando(false);
			}
		}

		
		
		boolean existeFichero=false;
		if (fichero!=null && fichero[1]!=null && !fichero[1].toLowerCase().contains("srec"))
		{
			File f=new File(fichero[0]+fichero[1]);
			
			existeFichero=f.exists();
				
		}
		if (existeFichero)//fichero!=null && fichero[1]!=null && !fichero[1].toLowerCase().contains("srec"))
		{

			this.vv.setClaseHabilitada(false);
			// Cargamos opción de borrado de ficheros
			obf=(OpcionBorradoFicheros)gOpciones.getOpcion("OpcionBorradoFicheros", true);
			
			
			// Cargamos lector de fichero
			if (fichero!=null && fichero[1]!=null)
			{
				try {
					FileReader fileStream = new FileReader( fichero[0]+fichero[1]);
					fileStream.close();
				} catch (FileNotFoundException fnfe) {
					new CuadroError(vv, Texto.get("ERROR_ARCH",Conf.idioma), Texto.get("ERROR_ARCHNE",Conf.idioma));
					fichero[1]=null;
				} catch (IOException ioe) {
					System.out.println("Error IO");
					fichero[1]=null;
				}

				// Creamos cuadro de progreso para informar al usuario del avance del proceso de carga de la clase Java
				cuadroProgreso = new CuadroProgreso(this.vv,Texto.get("CP_ESPERE",Conf.idioma),Texto.get("CP_PROCES",Conf.idioma),0);

				ficheroclass = fichero[1].replace(".java",".class");
				ficherosinex = fichero[1].replace(".java","");
				ficheroxml = fichero[1].replace(".java",".xml");
		
				// Borramos ficheros que puedan interferir en proceso de ejecución
				File file=new File(fichero[0]+ficheroclass);
				file.delete();
				file=new File(ficheroclass);
				file.delete();
				file=new File(fichero[0]+ficheroxml);
				file.delete();
				file=new File(ficheroxml);
				file.delete();

				cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),10);	
				
				
				// Compilamos externamente (mediante compilador de Java) el fichero seleccionado por el usuario
				// A partir de Java 7, Runtime.exec recibe un array de Strings
				if (!obf.getfClass()){
					String aux[]=new String[2];
					aux[0]="\""+omvj.getDir()+"javac\"";
					aux[1]="\""+fichero[0]+fichero[1]+"\"";
					LlamadorSistema.ejecucionArray(aux);
					//LlamadorSistema.ejecucion("\""+omvj.getDir()+"javac\" \""+fichero[0]+fichero[1]+"\"");
				}
				String aux[]=new String[4];
				aux[0]="\""+omvj.getDir()+"javac\"";
				aux[1]="-d";
				aux[2]=".\\";
				aux[3]="\""+fichero[0]+fichero[1]+"\"";
				String salidaCompilador=LlamadorSistema.ejecucionArray(aux);
				//String salidaCompilador=LlamadorSistema.ejecucion("\""+omvj.getDir()+"javac\" -d .\\ \""+fichero[0]+fichero[1]+"\"");
								
				compilado=salidaCompilador.length()<4;
				this.vv.setTextoCompilador(salidaCompilador);
			
				if (!compilado)
				{
					cuadroProgreso.cerrar();
					//System.out.print(""+"");
					
					int posicExtensionNombre=fichero[1].toLowerCase().indexOf(".java");
					String nombreClase=fichero[1].substring(0,posicExtensionNombre);
					
					this.vv.setTitulo(fichero[1]);
					if (distintaClase)
					{
						ValoresParametros.inicializar(distintaClase);
						this.vv.habilitarOpcionesAnimacion(false);
						this.vv.setClase(new ClaseAlgoritmo(fichero[0]+fichero[1],nombreClase));
						this.vv.setPreprocesador(this);
						this.vv.abrirPanelCodigo(true,true);
						//this.vv.setTextoCompilador(salidaCompilador);
						if (Conf.fichero_log) Logger.log_write("Preprocesador: clase cargada: '"+fichero[0]+fichero[1]+"'");
						Ventana.setProcesando(false);
					}
					else
					{
						//ValoresParametros.inicializar(distintaClase);
						this.vv.habilitarOpcionesAnimacion(false);
						this.vv.setClase(new ClaseAlgoritmo(fichero[0]+fichero[1],nombreClase));
						this.vv.setPreprocesador(this);
						this.vv.cerrarVistas();

						if (Conf.fichero_log) Logger.log_write("Preprocesador: clase cargada: '"+fichero[0]+fichero[1]+"'");
						Ventana.setProcesando(false);

					}
					
				}
				else
				{
					// Actualizamos opción de ficheros recientes (para mantener último directorio)
					ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes", true);
					ofr.setDir(fichero[0]);
					gOpciones.setOpcion(ofr,2);
					
					
					
					
					// Convertimos la clase a XML
					cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),20);	
					Java2XML.main(fichero[0]+fichero[1]);
					cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),35);	
					documento=ManipulacionElement.getDocumento(fichero[0]+ficheroxml);
					if (obf.getfXml())	// Si el usuario no está interesado en XML original, lo borramos
					{
						file=new File(fichero[0]+ficheroxml);
						file.delete();
					}

					if (documento==null)
					{
						//System.out.println("documento es null");
						cuadroProgreso.cerrar();	
						new CuadroError(vv, Texto.get("ERROR_ARCH",Conf.idioma),Texto.get("ERROR_ANTIESCRIT",Conf.idioma));
						return;
					}
					cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),50);
						
					Calendar c = new GregorianCalendar();
					codigoPrevio=generarCodigoUnico(""+c.get(Calendar.DAY_OF_MONTH), ""+(c.get(Calendar.MONTH)+1),
								""+c.get(Calendar.YEAR), ""+c.get(Calendar.HOUR_OF_DAY),
								""+c.get(Calendar.MINUTE), ""+c.get(Calendar.SECOND));	
					
					// Creamos clase nueva, para tener siempre actualizada la información (si cargamos clase original, se tira de caché la 2ª vez)
					Transformador.correccionNombres(documento,fichero[1].replace(".java",codigoPrevio+".java"),"",codigoPrevio);
					GeneradorJava.writeJavaFile(documento,fichero[1].replace(".java",codigoPrevio+".java"));
					
					compilado=true;
					
					cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),65);	
					
					if (!obf.getfClass()) {
						String aux2[]=new String[2];
						aux2[0]="\""+omvj.getDir()+"javac\"";
						aux2[1]="\""+fichero[0]+fichero[1].replace(".java",codigoPrevio+".java")+"\"";
						LlamadorSistema.ejecucionArray(aux2);
						//LlamadorSistema.ejecucion("\""+omvj.getDir()+"javac\" \""+
						//		fichero[0]+fichero[1].replace(".java",codigoPrevio+".java")+"\"");
					}
					String aux2[]=new String[4];
					aux2[0]="\""+omvj.getDir()+"javac\"";
					aux2[1]="-d";
					aux2[2]=".\\";
					aux2[3]="\""+fichero[1].replace(".java",codigoPrevio+".java")+"\"";
					salidaCompilador=LlamadorSistema.ejecucionArray(aux2);
					//salidaCompilador=LlamadorSistema.ejecucion("\""+omvj.getDir()+"javac\" -d .\\ \""+
					//					/*fichero[0]+*/fichero[1].replace(".java",codigoPrevio+".java")+"\"");
					compilado=salidaCompilador.length()<4;
										
					if (!compilado)
					{
						cuadroProgreso.cerrar();
						//System.out.print(""+"");
						
						new CuadroErrorCompilacion(vv, fichero[1].replace(".java",codigoPrevio+".java"),
							salidaCompilador.substring(4,salidaCompilador.length()-1));
						try {wait(550);} catch (java.lang.InterruptedException ie) {}
					}
					else
					{
						// Creamos la instancia de ClaseAlgoritmo que nos ayudará a gestionar las creaciones de animaciones
						cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),80);
						
						try {
							claseAlgoritmo=Transformador.crearObjetoClaseAlgoritmo(documento);	// Sólo contiene métodos visualizables
						} catch (Exception e) {
							cuadroProgreso.cerrar();
							new CuadroError(vv, "TITULO","MENSAJE CLASE VACIA");
								
							try {wait(550);} catch (java.lang.InterruptedException ie) {}
						}
					
						
						claseAlgoritmo.setId(ficherosinex+codigoPrevio);

					
						cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),88);
						Class clase=null;
						try {
							clase = Class.forName(ficherosinex+codigoPrevio);
						} catch (java.lang.ClassNotFoundException cnfe) {
							if (clase==null)
							{
								System.out.println("No se pudo cargar la clase "+ficherosinex);
								cuadroProgreso.cerrar();
							}
							return;
						}
						cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),95);		
						
						c = new GregorianCalendar();
						ahora=generarCodigoUnico(""+c.get(Calendar.DAY_OF_MONTH), ""+(c.get(Calendar.MONTH)+1),
												""+c.get(Calendar.YEAR), ""+c.get(Calendar.HOUR_OF_DAY),
												""+c.get(Calendar.MINUTE), ""+c.get(Calendar.SECOND));
						
						claseAlgoritmo.setId2( "SRec_"+claseAlgoritmo.getId()+ahora);
						
						cuadroProgreso.cerrar();	
						Transformador.correccionNombres(documento, fichero[1],"SRec_",ahora);
						
						
						
						
						
						Ventana.thisventana.habilitarOpcionesDYV(false);
						if (claseAlgoritmo.potencialMetodoDYV())	// Si la clase tiene algún método que se pueda visualizar con DYV
							//new CuadroSeleccionMetodos(claseAlgoritmo,this.vv,this);
							new CuadroPreguntaSeleccMetodosDYV(Ventana.thisventana,claseAlgoritmo,this);
						else {	// Si no lo tiene, directamente procesamos
							Ventana.thisventana.habilitarOpcionesDYV(false);
							fase2(claseAlgoritmo);
						}
					}
				}
			}
		}
		else if(fichero[1]!=null)
			new CuadroError(vv, Texto.get("ERROR_ARCH",Conf.idioma) , Texto.get("ERROR_ARCHNE",Conf.idioma) );
	}
		
	public synchronized void fase2(ClaseAlgoritmo clase)
	{
		
		this.claseAlgoritmo=clase;
		//cuadroProgreso = new CuadroProgreso(this.vv,Texto.get("CP_ESPERE",Conf.idioma),Texto.get("CP_PROCES",Conf.idioma),0);
		String ficherosinexF=ficherosinex+codigoPrevio;
		
		int errorManipulacion=Transformador.insertarLineasNecesarias(documento,claseAlgoritmo);

		
		if (errorManipulacion==1)
		{
			cuadroProgreso.cerrar();
			Ventana.setProcesando(false);
			if (Conf.fichero_log) Logger.log_write("Preprocesador: clase cargada: '"+fichero[0]+fichero[1]+"' (error 1, clase interfaz)");
			new CuadroError(vv, Texto.get("ERROR_CLASE",Conf.idioma),	Texto.get("ERROR_CLASEINTERFAZ",Conf.idioma));
		}
		/*else if (errorManipulacion==2)
		{
			System.out.println("prueba fase2 (2.2) ["+errorManipulacion+"]");
			cuadroProgreso.cerrar();
			VentanaVisualizador.setProcesando(false);
			new CuadroError(vv, Texto.get("ERROR_CLASE",Conf.idioma),	Texto.get("ERROR_CLASENOSENTEN",Conf.idioma));
		}*/
		else if (errorManipulacion==3)
		{
			cuadroProgreso.cerrar();
			Ventana.setProcesando(false);
			if (Conf.fichero_log) Logger.log_write("Preprocesador: clase cargada: '"+fichero[0]+fichero[1]+"' (error 3, clase abstracta)");
			new CuadroError(vv, Texto.get("ERROR_CLASE",Conf.idioma),	Texto.get("ERROR_CLASEABS",Conf.idioma));
		}
		else
		{
			cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),15);	
			// Escribir nuevo fichero XML con nuevos nodos (sentencias)
			if (!obf.getfXmlzv())
				ManipulacionElement.writeXmlFile(documento,fichero[0]+"SRec_"+ficherosinexF+ahora+".xml");

			String fich2=fichero[1].replace(".java","");
			fich2=fich2+codigoPrevio+ahora+".java";		// fich2 sustituye a fichero[1]
			String fichc=ficheroclass.replace(".class","");
			fichc=fichc+codigoPrevio+ahora+".class";		// fichc sustituye a ficheroclass
			
			cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),30);
			
			// Escribir clase final de Java que ejecutaremos
			if (!obf.getfJavazv())
				GeneradorJava.writeJavaFile(documento,fichero[0]+"SRec_"+fich2);
			
			GeneradorJava.writeJavaFile(documento,"SRec_"+fich2);
			cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),50);
			
			// Compilamos el nuevo fichero Java
			File file=new File(fichero[0]+"SRec_"+fichc);
			file.delete();
			file=new File("SRec_"+fichc);
			file.delete();

			compilado=true;

			if (!obf.getfClasszv()) {
				String aux3[]=new String[4];
				aux3[0]="\""+omvj.getDir()+"javac\"";
				aux3[1]="-d";
				aux3[2]="\""+fichero[0]+"\\";
				aux3[3]="\" SRec_"+fich2;
				LlamadorSistema.ejecucionArray(aux3);
				//LlamadorSistema.ejecucion("\""+omvj.getDir()+"javac\" -d \""+fichero[0]+"\\\" SRec_"+fich2);
			}
			cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),85);
			
			String aux3[]=new String[4];
			aux3[0]="\""+omvj.getDir()+"javac\"";
			aux3[1]="-d";
			aux3[2]=".\\";
			aux3[3]="SRec_"+fich2;
			String salidaCompilador=LlamadorSistema.ejecucionArray(aux3);
			//String salidaCompilador=LlamadorSistema.ejecucion("\""+omvj.getDir()+"javac\" -d .\\ SRec_"+fich2);
			
			compilado=salidaCompilador.length()<4;
			
			cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),70);	
			
			file=new File("SRec_"+fich2);
			file.delete();
			file=new File(ficherosinex+".class");
			file.delete();
			
			
			
			if (!compilado)
			{
				cuadroProgreso.cerrar();
				//System.out.print(""+"");
				Ventana.setProcesando(false);
				if (Conf.fichero_log) Logger.log_write("Preprocesador: clase cargada: '"+fichero[0]+fichero[1]+"' (error tras procesamiento)");
				new CuadroErrorCompilacion(vv, fichero[1].replace(".java",codigoPrevio+".java"),
					salidaCompilador.substring(4,salidaCompilador.length()-1));
				try {wait(550);} catch (java.lang.InterruptedException ie) {}
			}
			else
			{
				this.vv.setClase(this.claseAlgoritmo);
				// Ajustamos configuración del programa a procesado realizado
				//vv.setClasePreprocesada(fich2.replace(".java",""),fichero[1].replace(".java",""));
				
				this.vv.setTitulo(fichero[1]);
				cuadroProgreso.setValores(Texto.get("CP_PROCES",Conf.idioma),90);	
				
				try { wait(100); } catch(InterruptedException ie) {} 
				
				claseProcesada[0]=fichero[0];
				claseProcesada[1]=fichero[1];
				cuadroProgreso.cerrar();
				
				// Informamos del fin del proceso
				//new CuadroInformacion(VentanaVisualizador.thisventana,Texto.get("INFO_PROCOK",Conf.idioma),
				//		Texto.get("INFO_METPROCOK",Conf.idioma)+
				//		ficherosinexF.substring(0,ficherosinexF.indexOf("__codSRec__"))+".",550,100);
				
				if (Conf.fichero_log) Logger.log_write("Preprocesador: clase cargada: '"+fichero[0]+fichero[1]+"'");
				ValoresParametros.inicializar(distintaClase);
				this.vv.abrirPanelCodigo(true,true);
				this.vv.setClasePendienteGuardar(!distintaClase && this.vv.getClasePendienteGuardar());
				this.vv.habilitarOpcionesAnimacion(false);
				this.vv.setClase(this.claseAlgoritmo);
				this.vv.setPreprocesador(this);
				this.vv.setTextoCompilador("");
				Ventana.setProcesando(false);
				if(this.selecMetodo)
					this.vv.iniciarNuevaVisualizacionSelecMetodo();
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/**
		Gestiona los detalles necesarios para poder ejecutar el algoritmo de forma controlada, tras el procesamiento del mismo
		
		@param metodo instancia del método que se va a invocar
		@param p parámetros que se le pasarán al método
		@param cp clases a las que pertenecen esos parámetros
		@param paramVisibles determina si un parámetro deberá visualizarse o no a posteriori
		@param ficherosinex nombre de la clase a la que pertenece el método que se va a invocar
	*/
	public synchronized void ejecutarAlgoritmo()
	{
		// Desde la clase ClaseAlgoritmo de la ventana, debemos sacar el ID2, que nos dará el nombre del .class que tenemos que analizar
		// extraemos por ahí sus Method, etc. e identificamos el que ha sido escogido con la información retenida en la clasealgoritmo
	
		//cuadroProgreso = new CuadroProgreso(this.vv,Texto.get("CP_ESPERE",Conf.idioma),Texto.get("CP_PROCES",Conf.idioma),0);
		
		claseAlgoritmo=vv.getClase();	
		String nombreClass=claseAlgoritmo.getId2();
		
		Class claseCargada=null;
		try {
			claseCargada = Class.forName(nombreClass);
		} catch (java.lang.ClassNotFoundException cnfe) {
			System.out.println("No se pudo cargar la clase "+nombreClass);
			return;
		}
	
		//cuadroProgreso.setValores(Texto.get("CP_EJEC",Conf.idioma),20);	
		MetodoAlgoritmo metodoAlgoritmo=claseAlgoritmo.getMetodoPrincipal();
		
		
		//System.out.println("Preprocesador, detectado metodo principal: "+metodoAlgoritmo.getRepresentacion());
		//for (int i=0; i<claseAlgoritmo.getNumMetodos(); i++)
		//	System.out.println(claseAlgoritmo.getMetodo(i).getMarcadoPrincipal()+": "+claseAlgoritmo.getMetodo(i).getRepresentacion());

		
		
		
		
		
		Method metodos[]=claseCargada.getDeclaredMethods();
	
		Method metodoEjecutar=identificarMetodoEjecutar(metodoAlgoritmo,metodos);
		
		Type[] tipos = metodoEjecutar.getGenericParameterTypes();
		Class[] clasesParametros = new Class[tipos.length];
		for (int j=0; j<tipos.length; j++)
			clasesParametros[j]= (Class)tipos[j];
	
		//cuadroProgreso.setValores(Texto.get("CP_EJEC",Conf.idioma),30);	
		Object[] valoresParametros=new Object[tipos.length];
		
		for (int i=0; i<valoresParametros.length; i++)
		{
			try {
				valoresParametros[i]=GestorParametros.asignarParam(metodoAlgoritmo.getParamValor(i),clasesParametros[i]);
			} catch (java.lang.Exception ex) {
				ex.printStackTrace();
				System.out.println("Excepcion en Preprocesador, llamada a 'asignarParam' (i="+i+")");
			}
			
		}
		

		//cuadroProgreso.setValores(Texto.get("CP_EJEC",Conf.idioma),35);	
		
		String tituloPanel="";
		tituloPanel=tituloPanel+metodoEjecutar.getName()+" ( ";
		if (valoresParametros!=null)
			for (int i=0; i<valoresParametros.length; i++)
			{
				tituloPanel=tituloPanel+ServiciosString.representacionObjeto(valoresParametros[i]);
				if (i<valoresParametros.length-1)
					tituloPanel=tituloPanel+" , ";
			}
		tituloPanel=tituloPanel+" )";

		Traza traza=Traza.singleton();
		traza.vaciarTraza();
		//cuadroProgreso.setValores(Texto.get("CP_EJEC",Conf.idioma),50);	

		//cuadroProgreso.setValores(Texto.get("CP_EJEC",Conf.idioma),60);	
		
		try { wait(250); } catch(InterruptedException ie) {}
		if (Ejecutador.ejecutar(claseAlgoritmo.getId2(),metodoEjecutar.getName(),clasesParametros,valoresParametros))
		{
			Traza traza_diferido=null;
			//cuadroProgreso.setValores(Texto.get("CP_EJEC",Conf.idioma),70);	
			traza_diferido=traza.copiar();
			traza_diferido.setIDTraza(ahora);
			traza_diferido.setVisibilidad(claseAlgoritmo);
			traza_diferido.setArchivo(claseAlgoritmo.getPath());
			traza_diferido.setTecnicas(MetodoAlgoritmo.tecnicasEjecucion(claseAlgoritmo,metodoAlgoritmo));
			traza_diferido.setNombreMetodoEjecucion(metodoEjecutar.getName());
			traza_diferido.setTitulo(tituloPanel);
			
			Ejecucion ejecucion = new Ejecucion(traza_diferido, claseProcesada[0], claseProcesada[1]);		
			if (FamiliaEjecuciones.getInstance().estaHabilitado()) {
				ejecucion.getTraza().todoVisible();
				FamiliaEjecuciones.getInstance().addEjecucion(ejecucion);
				FamiliaEjecuciones.getInstance().pintaFamilia();
			} else {		
				vv.setDTB(ejecucion.getDatosTrazaBasicos());			
				vv.visualizarAlgoritmo(traza_diferido, true, cuadroProgreso, ejecucion.getFicheroFuenteDirectorio(),
						ejecucion.getFicheroFuente(), true);
			}
		}

		File file=new File(ficherosinex+ahora+".class");
		file.delete();
	}
	
	
	private Traza poda(Traza traza)
	{
		claseAlgoritmo=vv.getClase();
		
		RegistroActivacion ra=traza.getRaiz();
		
		ra=poda(ra);
		
		return traza;
	}
	
	
	private RegistroActivacion poda(RegistroActivacion ra)
	{
		String nombre=ra.getNombreMetodo();
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	private String generarCodigoUnico(String dia, String mes, String anyo, String h, String m, String s)
	{
		if (mes.length()==1)
			mes="0"+mes;
		if (dia.length()==1)
			dia="0"+dia;
		if (h.length()==1)
			h="0"+h;	
		if (m.length()==1)
			m="0"+m;		
		if (s.length()==1)
			s="0"+s;	
	
		return "__codSRec__"+anyo+mes+dia+h+m+s;
	}
	
	
	private Method identificarMetodoEjecutar(MetodoAlgoritmo metodoAlgoritmo, Method []metodos)
	{
		Method metodoEjecutar=null;
		
		for (int i=0; i<metodos.length; i++)
		{
			// Si se llaman igual y tienen mismo número de parámetros...
			if (metodoAlgoritmo.getNombre().equals(metodos[i].getName()) && 
				metodoAlgoritmo.getNumeroParametros()==metodos[i].getGenericParameterTypes().length)
			{
				// ...y si los tipos de los parámetros son iguales...
				Type[] tipos = metodos[i].getGenericParameterTypes();
				Class[] clases = new Class[tipos.length];
				for (int j=0; j<tipos.length; j++)
					clases[j]= (Class)tipos[j];
				
				//boolean sonTodosIguales=true;
				for (int j=0; j<tipos.length; j++)
					if (clases[j].getCanonicalName().contains(metodoAlgoritmo.getTipoParametro(j)) &&
						dimensionesCorrectas( clases[j].getCanonicalName(), metodoAlgoritmo.getDimParametro(j)))
						metodoEjecutar=metodos[i];

			}
		}
		
		return metodoEjecutar;
	}
	
	
	private boolean dimensionesCorrectas(String claseCanonica , int numero)
	{
		int vecesEncontradas=0;
		for (int i=0; i<claseCanonica.length(); i++)
			if (claseCanonica.charAt(i)=='[')
				vecesEncontradas++;
		
		return vecesEncontradas==numero;
	}
	
	
}
