package datos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.GregorianCalendar;

import opciones.GestorOpciones;
import opciones.OpcionBorradoFicheros;
import opciones.OpcionFicherosRecientes;
import opciones.OpcionMVJava;

import org.w3c.dom.Document;

import paneles.PanelCompilador;
import toxml.Java2XML;
import utilidades.GeneradorJava;
import utilidades.LlamadorSistema;
import utilidades.Logger;
import utilidades.ManipulacionElement;
import utilidades.SelecDireccion;
import utilidades.ServiciosString;
import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroError;
import cuadros.CuadroErrorCompilacion;
import cuadros.CuadroPreguntaSeleccMetodosDYV;
import cuadros.CuadroProgreso;
import cuadros.ValoresParametros;

/**
 * Gestiona todo el procesamiento de un fichero antes de ser ejecutado y
 * visualizado
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class Preprocesador extends Thread {

	private ClaseAlgoritmo claseAlgoritmo = null;

	private static String fichero[] = new String[2];
	private Ventana vv;
	private boolean selecMetodo = false;

	private static String claseProcesada[] = new String[2];

	private GestorOpciones gOpciones = new GestorOpciones();

	private OpcionBorradoFicheros obf = null;
	private OpcionMVJava omvj = null;
	private Document documento;
	private CuadroProgreso cuadroProgreso;

	private static String ficheroclass, ficherosinex, ficheroxml;
	private boolean compilado;

	private static String ahora;

	private String codigoPrevio;

	// a true si reprocesamos la misma clase que ya estaba cargada, es para no
	// borrar históricos de parámetros introducidos
	private boolean distintaClase = false;

	/**
	 * Constructor: crea un nuevo preprocesador
	 */
	public Preprocesador() {
		fichero[0] = null;
		fichero[1] = null;
		this.start();
	}

	/**
	 * Constructor: crea un nuevo preprocesador
	 * 
	 * @param path
	 *            path del fichero que hay que preprocesar
	 * @param nombre
	 *            nombre del fichero que hay que preprocesar
	 */
	public Preprocesador(String path, String nombre) {
		if (path != null && nombre != null) {
			fichero[0] = path.substring(0, path.lastIndexOf("\\") + 1);
			fichero[1] = nombre;
		} else {
			fichero[0] = null;
			fichero[1] = null;
		}
		this.start();
	}

	/**
	 * Constructor: crea un nuevo preprocesador.
	 * 
	 * @param path
	 *            path del fichero que hay que preprocesar
	 * @param nombre
	 *            nombre del fichero que hay que preprocesar
	 * @param selecMetodo
	 *            A true si debe mostrarse el cuadro de selección de métodos al
	 *            terminar de procesar, false en caso contrario.
	 */
	public Preprocesador(String path, String nombre, boolean selecMetodo) {
		if (path != null && nombre != null) {
			fichero[0] = path.substring(0, path.lastIndexOf("\\") + 1);
			fichero[1] = nombre;
		} else {
			fichero[0] = null;
			fichero[1] = null;
		}
		this.selecMetodo = selecMetodo;
		this.start();
	}

	/**
	 * Constructor: crea un nuevo preprocesador
	 * 
	 * @param path
	 *            path del fichero que hay que preprocesar
	 */
	public Preprocesador(String[] path) {
		if (path != null && path.length == 2 && path[0] != null
				&& path[1] != null) {
			fichero[0] = path[0];
			fichero[1] = path[1];
		} else {
			fichero[0] = null;
			fichero[1] = null;
		}
		this.start();
	}

	/**
	 * Método que se encarga de llevar a cabo, en un nuevo thread, todo el
	 * procesamiento del fichero
	 */
	@Override
	public synchronized void run() {
		this.vv = Ventana.thisventana;
		this.vv.setTextoCompilador(PanelCompilador.CODIGO_VACIO);
		this.vv.setDTB(null);

		// Cargamos y manejamos opciones
		this.omvj = (OpcionMVJava) (this.gOpciones.getOpcion("OpcionMVJava",
				true));

		if (!this.omvj.getValida()) {
			fichero[1] = null;
			new CuadroError(this.vv, Texto.get("ERROR_CONF", Conf.idioma),
					Texto.get("ERROR_NOMVJ", Conf.idioma));
			return;
		}
		this.distintaClase = false;
		OpcionFicherosRecientes ofr;

		// Si no hemos recibido nombre fichero por parametro, sacamos
		// JFileChooser...
		if (fichero[0] == null) {
			ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
					"OpcionFicherosRecientes", true);

			fichero[0] = ofr.getDir();
			fichero = SelecDireccion.cuadroAbrirFichero(fichero[0],
					Texto.get("VVC_PROCCLAS", Conf.idioma), null, "java",
					Texto.get("ARCHIVO_JAVA", Conf.idioma), 1);

			// Forzamos a abrir Selector de fichero
			this.distintaClase = true;

			// *1* Comprobar que fichero existe
			if (fichero == null || fichero[1] == null) {
				if (Conf.fichero_log) {
					Logger.log_write("Preprocesador: no se procesará clase.");
				}
			}
		}

		boolean existeFichero = false;
		if (fichero != null && fichero[1] != null
				&& !fichero[1].toLowerCase().contains("srec")) {
			File f = new File(fichero[0] + fichero[1]);

			existeFichero = f.exists();

		}
		if (existeFichero) {

			this.vv.setClaseHabilitada(false);
			// Cargamos opción de borrado de ficheros
			this.obf = (OpcionBorradoFicheros) this.gOpciones.getOpcion(
					"OpcionBorradoFicheros", true);

			// Cargamos lector de fichero
			if (fichero != null && fichero[1] != null) {
				try {
					FileReader fileStream = new FileReader(fichero[0]
							+ fichero[1]);
					fileStream.close();
				} catch (FileNotFoundException fnfe) {
					new CuadroError(this.vv, Texto.get("ERROR_ARCH",
							Conf.idioma),
							Texto.get("ERROR_ARCHNE", Conf.idioma));
					fichero[1] = null;
				} catch (IOException ioe) {
					System.out.println("Error IO");
					fichero[1] = null;
				}

				// Creamos cuadro de progreso para informar al usuario del
				// avance del proceso de carga de la clase Java
				this.cuadroProgreso = new CuadroProgreso(this.vv, Texto.get(
						"CP_ESPERE", Conf.idioma), Texto.get("CP_PROCES",
						Conf.idioma), 0);

				ficheroclass = fichero[1].replace(".java", ".class");
				ficherosinex = fichero[1].replace(".java", "");
				ficheroxml = fichero[1].replace(".java", ".xml");

				// Borramos ficheros que puedan interferir en proceso de
				// ejecución
				File file = new File(fichero[0] + ficheroclass);
				file.delete();
				file = new File(ficheroclass);
				file.delete();
				file = new File(fichero[0] + ficheroxml);
				file.delete();
				file = new File(ficheroxml);
				file.delete();

				this.cuadroProgreso.setValores(
						Texto.get("CP_PROCES", Conf.idioma), 10);

				// Compilamos externamente (mediante compilador de Java) el
				// fichero seleccionado por el usuario
				// A partir de Java 7, Runtime.exec recibe un array de Strings
				if (!this.obf.getfClass()) {
					String aux[] = new String[2];
					aux[0] = "\"" + this.omvj.getDir() + "javac\"";
					aux[1] = "\"" + fichero[0] + fichero[1] + "\"";
					LlamadorSistema.ejecucionArray(aux);
				}
				String aux[] = new String[4];
				aux[0] = "\"" + this.omvj.getDir() + "javac\"";
				aux[1] = "-d";
				aux[2] = ".\\";
				aux[3] = "\"" + fichero[0] + fichero[1] + "\"";
				String salidaCompilador = LlamadorSistema.ejecucionArray(aux);
				// String

				this.compilado = salidaCompilador.length() < 4;
				this.vv.setTextoCompilador(salidaCompilador);

				if (!this.compilado) {
					this.cuadroProgreso.cerrar();

					int posicExtensionNombre = fichero[1].toLowerCase()
							.indexOf(".java");
					String nombreClase = fichero[1].substring(0,
							posicExtensionNombre);

					this.vv.setTitulo(fichero[1]);
					if (this.distintaClase) {
						ValoresParametros.inicializar(this.distintaClase);
						this.vv.habilitarOpcionesAnimacion(false);
						this.vv.setClase(new ClaseAlgoritmo(fichero[0]
								+ fichero[1], nombreClase));
						this.vv.setPreprocesador(this);
						this.vv.abrirPanelCodigo(true, true);
						if (Conf.fichero_log) {
							Logger.log_write("Preprocesador: clase cargada: '"
									+ fichero[0] + fichero[1] + "'");
						}
					} else {
						this.vv.habilitarOpcionesAnimacion(false);
						this.vv.setClase(new ClaseAlgoritmo(fichero[0]
								+ fichero[1], nombreClase));
						this.vv.setPreprocesador(this);
						this.vv.cerrarVistas();

						if (Conf.fichero_log) {
							Logger.log_write("Preprocesador: clase cargada: '"
									+ fichero[0] + fichero[1] + "'");
						}
					}

				} else {
					// Actualizamos opción de ficheros recientes (para mantener
					// último directorio)
					ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
							"OpcionFicherosRecientes", true);
					ofr.setDir(fichero[0]);
					this.gOpciones.setOpcion(ofr, 2);

					// Convertimos la clase a XML
					this.cuadroProgreso.setValores(
							Texto.get("CP_PROCES", Conf.idioma), 20);
					Java2XML.main(fichero[0] + fichero[1]);
					this.cuadroProgreso.setValores(
							Texto.get("CP_PROCES", Conf.idioma), 35);
					this.documento = ManipulacionElement
							.getDocumento(fichero[0] + ficheroxml);
					if (this.obf.getfXml()) // Si el usuario no está interesado
					// en XML original, lo borramos
					{
						file = new File(fichero[0] + ficheroxml);
						file.delete();
					}

					if (this.documento == null) {
						this.cuadroProgreso.cerrar();
						new CuadroError(this.vv, Texto.get("ERROR_ARCH",
								Conf.idioma), Texto.get("ERROR_ANTIESCRIT",
								Conf.idioma));
						return;
					}
					this.cuadroProgreso.setValores(
							Texto.get("CP_PROCES", Conf.idioma), 50);

					Calendar c = new GregorianCalendar();
					this.codigoPrevio = this.generarCodigoUnico(
							"" + c.get(Calendar.DAY_OF_MONTH),
							"" + (c.get(Calendar.MONTH) + 1),
							"" + c.get(Calendar.YEAR),
							"" + c.get(Calendar.HOUR_OF_DAY),
							"" + c.get(Calendar.MINUTE),
							"" + c.get(Calendar.SECOND));

					// Creamos clase nueva, para tener siempre actualizada la
					// información (si cargamos clase original, se tira de caché
					// la 2ª vez)
					Transformador.correccionNombres(
							this.documento,
							fichero[1].replace(".java", this.codigoPrevio
									+ ".java"), "", this.codigoPrevio);
					GeneradorJava.writeJavaFile(
							this.documento,
							fichero[1].replace(".java", this.codigoPrevio
									+ ".java"));

					this.compilado = true;

					this.cuadroProgreso.setValores(
							Texto.get("CP_PROCES", Conf.idioma), 65);

					if (!this.obf.getfClass()) {
						String aux2[] = new String[2];
						aux2[0] = "\"" + this.omvj.getDir() + "javac\"";
						aux2[1] = "\""
								+ fichero[0]
								+ fichero[1].replace(".java", this.codigoPrevio
										+ ".java") + "\"";
						LlamadorSistema.ejecucionArray(aux2);
					}
					String aux2[] = new String[4];
					aux2[0] = "\"" + this.omvj.getDir() + "javac\"";
					aux2[1] = "-d";
					aux2[2] = ".\\";
					aux2[3] = "\""
							+ fichero[1].replace(".java", this.codigoPrevio
									+ ".java") + "\"";
					salidaCompilador = LlamadorSistema.ejecucionArray(aux2);
					this.compilado = salidaCompilador.length() < 4;

					if (!this.compilado) {
						this.cuadroProgreso.cerrar();

						new CuadroErrorCompilacion(this.vv, fichero[1].replace(
								".java", this.codigoPrevio + ".java"),
								salidaCompilador.substring(4,
										salidaCompilador.length() - 1));
						try {
							this.wait(550);
						} catch (java.lang.InterruptedException ie) {
						}
					} else {
						// Creamos la instancia de ClaseAlgoritmo que nos
						// ayudará a gestionar las creaciones de animaciones
						this.cuadroProgreso.setValores(
								Texto.get("CP_PROCES", Conf.idioma), 80);

						try {
							this.claseAlgoritmo = Transformador
									.crearObjetoClaseAlgoritmo(this.documento); // Sólo
							// contiene
							// métodos
							// visualizables
						} catch (Exception e) {
							this.cuadroProgreso.cerrar();
							new CuadroError(this.vv, "TITULO",
									"MENSAJE CLASE VACIA");

							try {
								this.wait(550);
							} catch (java.lang.InterruptedException ie) {
							}
						}

						this.claseAlgoritmo.setId(ficherosinex
								+ this.codigoPrevio);

						this.cuadroProgreso.setValores(
								Texto.get("CP_PROCES", Conf.idioma), 88);
						Class clase = null;
						try {
							clase = Class.forName(ficherosinex
									+ this.codigoPrevio);
						} catch (java.lang.ClassNotFoundException cnfe) {
							if (clase == null) {
								System.out
										.println("No se pudo cargar la clase "
												+ ficherosinex);
								this.cuadroProgreso.cerrar();
							}
							return;
						}
						this.cuadroProgreso.setValores(
								Texto.get("CP_PROCES", Conf.idioma), 95);

						c = new GregorianCalendar();
						ahora = this.generarCodigoUnico(
								"" + c.get(Calendar.DAY_OF_MONTH),
								"" + (c.get(Calendar.MONTH) + 1),
								"" + c.get(Calendar.YEAR),
								"" + c.get(Calendar.HOUR_OF_DAY),
								"" + c.get(Calendar.MINUTE),
								"" + c.get(Calendar.SECOND));

						this.claseAlgoritmo.setId2("SRec_"
								+ this.claseAlgoritmo.getId() + ahora);

						this.cuadroProgreso.cerrar();
						Transformador.correccionNombres(this.documento,
								fichero[1], "SRec_", ahora);

						Ventana.thisventana.habilitarOpcionesDYV(false);
						if (this.claseAlgoritmo.potencialMetodoDYV()) {
							new CuadroPreguntaSeleccMetodosDYV(
									Ventana.thisventana, this.claseAlgoritmo,
									this);
						} else { // Si no lo tiene, directamente procesamos
							Ventana.thisventana.habilitarOpcionesDYV(false);
							this.fase2(this.claseAlgoritmo);
						}
					}
				}
			}
		} else if (fichero[1] != null) {
			new CuadroError(this.vv, Texto.get("ERROR_ARCH", Conf.idioma),
					Texto.get("ERROR_ARCHNE", Conf.idioma));
		}
	}
	
	/**
	 * Una vez procesado el fichero, este método se encargará de insertar las
	 * llamadas necesarias en el xml para procesar la clase final que será ejecutada.
	 * 
	 * @param clase Clase a ser procesada.
	 */
	public synchronized void fase2(ClaseAlgoritmo clase) {

		this.claseAlgoritmo = clase;
		String ficherosinexF = ficherosinex + this.codigoPrevio;

		int errorManipulacion = Transformador.insertarLineasNecesarias(
				this.documento, this.claseAlgoritmo);

		if (errorManipulacion == 1) {
			this.cuadroProgreso.cerrar();
			if (Conf.fichero_log) {
				Logger.log_write("Preprocesador: clase cargada: '" + fichero[0]
						+ fichero[1] + "' (error 1, clase interfaz)");
			}
			new CuadroError(this.vv, Texto.get("ERROR_CLASE", Conf.idioma),
					Texto.get("ERROR_CLASEINTERFAZ", Conf.idioma));
		} else if (errorManipulacion == 3) {
			this.cuadroProgreso.cerrar();
			if (Conf.fichero_log) {
				Logger.log_write("Preprocesador: clase cargada: '" + fichero[0]
						+ fichero[1] + "' (error 3, clase abstracta)");
			}
			new CuadroError(this.vv, Texto.get("ERROR_CLASE", Conf.idioma),
					Texto.get("ERROR_CLASEABS", Conf.idioma));
		} else {
			this.cuadroProgreso.setValores(Texto.get("CP_PROCES", Conf.idioma),
					15);
			// Escribir nuevo fichero XML con nuevos nodos (sentencias)
			if (!this.obf.getfXmlzv()) {
				ManipulacionElement.writeXmlFile(this.documento, fichero[0]
						+ "SRec_" + ficherosinexF + ahora + ".xml");
			}

			String fich2 = fichero[1].replace(".java", "");
			fich2 = fich2 + this.codigoPrevio + ahora + ".java"; // fich2
			// sustituye
			// a
			// fichero[1]
			String fichc = ficheroclass.replace(".class", "");
			fichc = fichc + this.codigoPrevio + ahora + ".class"; // fichc
			// sustituye
			// a
			// ficheroclass

			this.cuadroProgreso.setValores(Texto.get("CP_PROCES", Conf.idioma),
					30);

			// Escribir clase final de Java que ejecutaremos
			if (!this.obf.getfJavazv()) {
				GeneradorJava.writeJavaFile(this.documento, fichero[0]
						+ "SRec_" + fich2);
			}

			GeneradorJava.writeJavaFile(this.documento, "SRec_" + fich2);
			this.cuadroProgreso.setValores(Texto.get("CP_PROCES", Conf.idioma),
					50);

			// Compilamos el nuevo fichero Java
			File file = new File(fichero[0] + "SRec_" + fichc);
			file.delete();
			file = new File("SRec_" + fichc);
			file.delete();

			this.compilado = true;

			if (!this.obf.getfClasszv()) {
				String aux3[] = new String[4];
				aux3[0] = "\"" + this.omvj.getDir() + "javac\"";
				aux3[1] = "-d";
				aux3[2] = "\"" + fichero[0] + "\\";
				aux3[3] = "\" SRec_" + fich2;
				LlamadorSistema.ejecucionArray(aux3);
			}
			this.cuadroProgreso.setValores(Texto.get("CP_PROCES", Conf.idioma),
					85);

			String aux3[] = new String[4];
			aux3[0] = "\"" + this.omvj.getDir() + "javac\"";
			aux3[1] = "-d";
			aux3[2] = ".\\";
			aux3[3] = "SRec_" + fich2;
			String salidaCompilador = LlamadorSistema.ejecucionArray(aux3);
			// String

			this.compilado = salidaCompilador.length() < 4;

			this.cuadroProgreso.setValores(Texto.get("CP_PROCES", Conf.idioma),
					70);

			file = new File("SRec_" + fich2);
			file.delete();
			file = new File(ficherosinex + ".class");
			file.delete();

			if (!this.compilado) {
				this.cuadroProgreso.cerrar();
				if (Conf.fichero_log) {
					Logger.log_write("Preprocesador: clase cargada: '"
							+ fichero[0] + fichero[1]
							+ "' (error tras procesamiento)");
				}
				new CuadroErrorCompilacion(this.vv, fichero[1].replace(".java",
						this.codigoPrevio + ".java"),
						salidaCompilador.substring(4,
								salidaCompilador.length() - 1));
				try {
					this.wait(550);
				} catch (java.lang.InterruptedException ie) {
				}
			} else {
				this.vv.setClase(this.claseAlgoritmo);
				// Ajustamos configuración del programa a procesado realizado
				this.vv.setTitulo(fichero[1]);
				this.cuadroProgreso.setValores(
						Texto.get("CP_PROCES", Conf.idioma), 90);

				try {
					this.wait(100);
				} catch (InterruptedException ie) {
				}

				claseProcesada[0] = fichero[0];
				claseProcesada[1] = fichero[1];
				this.cuadroProgreso.cerrar();

				if (Conf.fichero_log) {
					Logger.log_write("Preprocesador: clase cargada: '"
							+ fichero[0] + fichero[1] + "'");
				}
				ValoresParametros.inicializar(this.distintaClase);
				this.vv.abrirPanelCodigo(true, true);
				this.vv.setClasePendienteGuardar(!this.distintaClase
						&& this.vv.getClasePendienteGuardar());
				this.vv.habilitarOpcionesAnimacion(false);
				this.vv.setClase(this.claseAlgoritmo);
				this.vv.setPreprocesador(this);
				this.vv.setTextoCompilador("");
				if (this.selecMetodo) {
					this.vv.iniciarNuevaVisualizacionSelecMetodo();
				}
			}
		}
	}

	/**
	 * Gestiona los detalles necesarios para poder ejecutar el algoritmo de
	 * forma controlada, tras el procesamiento del mismo
	 */
	public synchronized void ejecutarAlgoritmo() {
		// Desde la clase ClaseAlgoritmo de la ventana, debemos sacar el ID2,
		// que nos dará el nombre del .class que tenemos que analizar
		// extraemos por ahí sus Method, etc. e identificamos el que ha sido
		// escogido con la información retenida en la clasealgoritmo

		this.claseAlgoritmo = this.vv.getClase();
		String nombreClass = this.claseAlgoritmo.getId2();

		Class claseCargada = null;
		try {
			claseCargada = Class.forName(nombreClass);
		} catch (java.lang.ClassNotFoundException cnfe) {
			System.out.println("No se pudo cargar la clase " + nombreClass);
			return;
		}

		MetodoAlgoritmo metodoAlgoritmo = this.claseAlgoritmo
				.getMetodoPrincipal();

		Method metodos[] = claseCargada.getDeclaredMethods();

		Method metodoEjecutar = this.identificarMetodoEjecutar(metodoAlgoritmo,
				metodos);

		Type[] tipos = metodoEjecutar.getGenericParameterTypes();
		Class[] clasesParametros = new Class[tipos.length];
		for (int j = 0; j < tipos.length; j++) {
			clasesParametros[j] = (Class) tipos[j];
		}
		
		for (int j=0; j<tipos.length; j++)
			clasesParametros[j]= (Class)tipos[j];
		
		ParametrosParser parametrosParser = new ParametrosParser(metodoAlgoritmo);
		
		String[][] matrizParametros = parametrosParser.obtenerMatrizParametros();	
		if (matrizParametros.length > 1) {
			FamiliaEjecuciones.getInstance().habilitar();
		} else {
			FamiliaEjecuciones.getInstance().deshabilitar();
		}
		FamiliaEjecuciones.getInstance().borrarEjecuciones();
		
		for (int numeroEjecucion = 0; numeroEjecucion < matrizParametros.length; numeroEjecucion++) {
			
			Object[] valoresParametros = new Object[tipos.length];
	
			for (int i = 0; i < valoresParametros.length; i++) {
				try {
					valoresParametros[i] = GestorParametros
							.asignarParam(matrizParametros[numeroEjecucion][i], clasesParametros[i]);
				} catch (java.lang.Exception ex) {
					ex.printStackTrace();
					System.out
							.println("Excepcion en Preprocesador, llamada a 'asignarParam' (i="
									+ i + ")");
				}
	
			}
	
			String tituloPanel = "";
			tituloPanel = tituloPanel + metodoEjecutar.getName() + " ( ";
			if (valoresParametros != null) {
				for (int i = 0; i < valoresParametros.length; i++) {
					tituloPanel = tituloPanel
							+ ServiciosString
									.representacionObjeto(valoresParametros[i]);
					if (i < valoresParametros.length - 1) {
						tituloPanel = tituloPanel + " , ";
					}
				}
			}
			tituloPanel = tituloPanel + " )";
	
			Traza traza = Traza.singleton();
			traza.vaciarTraza();
	
			try {
				this.wait(250);
			} catch (InterruptedException ie) {
			}
			if (Ejecutador.ejecutar(this.claseAlgoritmo.getId2(),
					metodoEjecutar.getName(), clasesParametros, valoresParametros)) {
				Traza traza_diferido = null;
				traza_diferido = traza.copiar();
				traza_diferido.setIDTraza(ahora);
				traza_diferido.setVisibilidad(this.claseAlgoritmo);
				traza_diferido.setArchivo(this.claseAlgoritmo.getPath());
				traza_diferido.setTecnicas(MetodoAlgoritmo.tecnicasEjecucion(
						this.claseAlgoritmo, metodoAlgoritmo));
				traza_diferido.setNombreMetodoEjecucion(metodoEjecutar.getName());
				traza_diferido.setTitulo(tituloPanel);
				Ejecucion e = new Ejecucion(traza_diferido);
				
				if (FamiliaEjecuciones.getInstance().estaHabilitado()) {
					FamiliaEjecuciones.getInstance().addEjecucion(e);
				} else {						
					vv.visualizarEjecucion(e, true);
				}
			}
		}
		
		if (FamiliaEjecuciones.getInstance().estaHabilitado()) {
			FamiliaEjecuciones.getInstance().setPrimeraEjecucionActiva();
		}

		File file = new File(ficherosinex + ahora + ".class");
		file.delete();
	}
	
	/**
	 * Genera un identificador único dada una fecha concreta.
	 * 
	 * @param dia Dia
	 * @param mes Mes
	 * @param anyo Año
	 * @param h Hora
	 * @param m Minutos
	 * @param s Segundos
	 * 
	 * @return Código único.
	 */
	private String generarCodigoUnico(String dia, String mes, String anyo,
			String h, String m, String s) {
		if (mes.length() == 1) {
			mes = "0" + mes;
		}
		if (dia.length() == 1) {
			dia = "0" + dia;
		}
		if (h.length() == 1) {
			h = "0" + h;
		}
		if (m.length() == 1) {
			m = "0" + m;
		}
		if (s.length() == 1) {
			s = "0" + s;
		}

		return "__codSRec__" + anyo + mes + dia + h + m + s;
	}
	
	/**
	 * Dada la representación de un método del algoritmo, determina el método a ejecutar de la clase.
	 * 
	 * @param metodoAlgoritmo Información del método.
	 * @param metodos Lista de métodos disponibles.
	 * 
	 * @return Método a ejecutar.
	 */
	private Method identificarMetodoEjecutar(MetodoAlgoritmo metodoAlgoritmo,
			Method[] metodos) {
		Method metodoEjecutar = null;

		for (int i = 0; i < metodos.length; i++) {
			// Si se llaman igual y tienen mismo número de parámetros...
			if (metodoAlgoritmo.getNombre().equals(metodos[i].getName())
					&& metodoAlgoritmo.getNumeroParametros() == metodos[i]
							.getGenericParameterTypes().length) {
				// ...y si los tipos de los parámetros son iguales...
				Type[] tipos = metodos[i].getGenericParameterTypes();
				Class[] clases = new Class[tipos.length];
				for (int j = 0; j < tipos.length; j++) {
					clases[j] = (Class) tipos[j];
				}

				// boolean sonTodosIguales=true;
				for (int j = 0; j < tipos.length; j++) {
					if (clases[j].getCanonicalName().contains(
							metodoAlgoritmo.getTipoParametro(j))
							&& this.dimensionesCorrectas(
									clases[j].getCanonicalName(),
									metodoAlgoritmo.getDimParametro(j))) {
						metodoEjecutar = metodos[i];
					}
				}

			}
		}

		return metodoEjecutar;
	}
	
	/**
	 * Determina si las dimensiones de una clase son las correctas.
	 * 
	 * @param claseCanonica Nombre canónico de la clase.
	 * @param numero Número de dimensiones esperado.
	 * 
	 * @return true si las dimensiones son las esperadas, false en caso contrario.
	 */
	private boolean dimensionesCorrectas(String claseCanonica, int numero) {
		int vecesEncontradas = 0;
		for (int i = 0; i < claseCanonica.length(); i++) {
			if (claseCanonica.charAt(i) == '[') {
				vecesEncontradas++;
			}
		}

		return vecesEncontradas == numero;
	}

}
