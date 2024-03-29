package datos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import opciones.GestorOpciones;
import opciones.OpcionBorradoFicheros;
import opciones.OpcionFicherosRecientes;
import opciones.OpcionMVJava;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import paneles.PanelCompilador;
import toxml.Java2XML;
import toxml.JavaParser;
import utilidades.GeneradorJava;
import utilidades.LlamadorSistema;
import utilidades.Logger;
import utilidades.ManipulacionElement;
import utilidades.SelecDireccion;
import utilidades.ServiciosString;
import utilidades.SsooValidator;
import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroError;
import cuadros.CuadroErrorCompilacion;
import cuadros.CuadroPreguntaSeleccionVistasEspecificas;
import cuadros.CuadroProgreso;
import cuadros.ValoresParametros;

/**
 * Gestiona todo el procesamiento de un fichero antes de ser ejecutado y
 * visualizado
 * 
 * @author Antonio P�rez Carrasco
 * @version 2006-2007
 */
public class Preprocesador extends Thread {

	private ClaseAlgoritmo claseAlgoritmo = null;

	private static String fichero[] = new String[2];
	private Ventana vv;
	private int metodoOrigen = 0;

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
	// borrar hist�ricos de par�metros introducidos
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
		String nombreModif = JavaParser.convReverse(nombre);
	
		if (path != null && nombreModif != null) {
			if(!SsooValidator.isUnix()){
				fichero[0] = path.substring(0, path.lastIndexOf("\\") + 1);
			}else{
				fichero[0] = path.substring(0, path.lastIndexOf("/") + 1);
			}
			fichero[1] = nombreModif;
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
	 * @param metodoOrigen
	 * 			1 = Mostrar selecci�n de m�todos
	 * 			2 = Mostrar seleccion par�metros
	 */
	public Preprocesador(String path, String nombre, int metodoOrigen) {
		String nombreModif = JavaParser.convReverse(nombre);
		if (path != null && nombreModif != null) {
			if(!SsooValidator.isUnix()){
				fichero[0] = path.substring(0, path.lastIndexOf("\\") + 1);
			}else{
				fichero[0] = path.substring(0, path.lastIndexOf("/") + 1);
			}
			fichero[1] = nombreModif;
		} else {
			fichero[0] = null;
			fichero[1] = null;
		}
		this.metodoOrigen = metodoOrigen;
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
			fichero[1] = JavaParser.convReverse(path[1]);
		} else {
			fichero[0] = null;
			fichero[1] = null;
		}
		this.start();
	}

	/**
	 * M�todo que se encarga de llevar a cabo, en un nuevo thread, todo el
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
					Logger.log_write("Preprocesador: no se procesar� clase.");
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
			// Cargamos opci�n de borrado de ficheros
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
				// ejecuci�n
				/* file = new File(fichero[0] + ficheroclass);
				file.delete();
				file = new File(ficheroclass);
				file.delete();*/
				File file = new File(fichero[0] + ficheroxml);
				file.delete();
				file = new File(ficheroxml);
				file.delete();

				this.cuadroProgreso.setValores(
						Texto.get("CP_PROCES", Conf.idioma), 10);

				// Compilamos externamente (mediante compilador de Java) el
				// fichero seleccionado por el usuario
				// A partir de Java 7, Runtime.exec recibe un array de Strings
				if (!this.obf.getfClass()) {
					String aux[] = new String[4];
					if(!SsooValidator.isUnix()){	//	No Linux
						aux[0] = "\"" + this.omvj.getDir() + "javac\"";
						aux[1] = "\"" + fichero[0] + fichero[1] + "\"";
					}else{							//	Si linux
						aux[0] = this.omvj.getDir() + "javac";
						aux[1] = fichero[0] + fichero[1];
					}
					
					aux[2] = "-classpath";
					aux[3] = getRunningPath();
					LlamadorSistema.ejecucionArray(aux);
				}
				String aux[] = new String[6];
				if(!SsooValidator.isUnix()){	//	No Linux
					aux[0] = "\"" + this.omvj.getDir() + "javac\"";
					aux[2] = ".\\";
					aux[3] = "\"" + fichero[0] + fichero[1] + "\"";
				}else{							//	Si linux
					aux[0] = this.omvj.getDir() + "javac";
					aux[2] = "./";
					aux[3] = fichero[0] + fichero[1];
				}
				aux[1] = "-d";
				aux[4] = "-classpath";
				aux[5] = getRunningPath();

				
				String salidaCompilador="";
				List<String> salidaCompletaCompilador;
				try {
					salidaCompletaCompilador = LlamadorSistema.getErrorDetallado(aux);
					if(salidaCompletaCompilador.size()>0)
						
						//	Salida que se mostrar� en el compilador
						salidaCompilador = salidaCompletaCompilador.get(0);
					
				} catch (IOException e1) {	
					salidaCompletaCompilador = new ArrayList<String>();
				} catch (InterruptedException e) {
					salidaCompletaCompilador = new ArrayList<String>();
				}
				
				boolean copiado = false;
				HashSet<String> imports = null;
				if(LlamadorSistema.isProcesoInterrumpido()) {
					// Se ha interumpido el compilado porque ha tardado demasiado, 
					// posiblemente un bucle infinito
					
					/*
					 * De momento no funciona correctamente hasta que el proceso de Java2XML y
					 * GeneradorJava.writeJavaFile sean capaces de incluir tipos genericos
					 * "<ClaseGenerica>". Ahora los transforma en tipo Object lo cual hace que
					 * a�adir imports sobre estos genericos sea inutil.
					 */
					
					//addImports(imports, copiado, salidaCompilador, salidaCompletaCompilador);
				}

				this.compilado = salidaCompilador.length() < 4;
				this.vv.setTextoCompilador(salidaCompilador);

				if (!this.compilado) {
					this.cuadroProgreso.cerrar();
					//this.vv.getPanelVentana().cerrarPanelCodigo();
					
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

					//	Subrayar	
					if(copiado) {
						new CuadroErrorCompilacion(vv, fichero[1].replace(
								".java", codigoPrevio + ".java"),
								salidaCompilador.substring(4,
										salidaCompilador.length() - 1));
					}else {
						subrayarLineas(salidaCompletaCompilador, imports);	
					}			
				} else {
					// Actualizamos opci�n de ficheros recientes (para mantener
					// �ltimo directorio)
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
					if (this.obf.getfXml()) // Si el usuario no est� interesado
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
					// informaci�n (si cargamos clase original, se tira de cach�
					// la 2� vez)
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
						String aux2[] = new String[4];
						
						if(!SsooValidator.isUnix()){	//	No Linux
							aux2[0] = "\"" + this.omvj.getDir() + "javac\"";
							aux2[1] = "\""
									+ fichero[0]
									+ fichero[1].replace(".java", this.codigoPrevio
											+ ".java") + "\"";
						}else{							//	Si linux
							aux2[0] = this.omvj.getDir() + "javac";
							aux2[1] = fichero[0]
									+ fichero[1].replace(".java", this.codigoPrevio
											+ ".java");
						}
						
						aux2[2] = "-classpath";
						aux2[3] = getRunningPath();
						LlamadorSistema.ejecucionArray(aux2);
					}
					String aux2[] = new String[6];
					if(!SsooValidator.isUnix()){	//	No Linux
						aux2[0] = "\"" + this.omvj.getDir() + "javac\"";
						aux2[2] = ".\\";
						aux2[3] = "\""
								+ fichero[1].replace(".java", this.codigoPrevio
										+ ".java") + "\"";
					}else{							//	Si linux
						aux2[0] = this.omvj.getDir() + "javac";
						aux2[2] = "./";
						aux2[3] = fichero[1].replace(".java", this.codigoPrevio
										+ ".java");
					}
					aux2[1] = "-d";					
					
					aux2[4] = "-classpath";
					aux2[5] = getRunningPath();
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
						// ayudar� a gestionar las creaciones de animaciones
						this.cuadroProgreso.setValores(
								Texto.get("CP_PROCES", Conf.idioma), 80);

						try {
							this.claseAlgoritmo = Transformador
									.crearObjetoClaseAlgoritmo(this.documento); 
							// S�lo
							// contiene
							// m�todos
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
						Class<?> clase = null;
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

						this.claseAlgoritmo.setId2("SRec_"
								+ this.claseAlgoritmo.getId() + ahora);

						this.cuadroProgreso.cerrar();
						Transformador.correccionNombres(this.documento,
								fichero[1], "SRec_", ahora);

						Ventana.thisventana.habilitarOpcionesDYV(false);
						if (this.claseAlgoritmo.potencialMetodoDYV()) {
							new CuadroPreguntaSeleccionVistasEspecificas(
								this.claseAlgoritmo, Ventana.thisventana,
								this,true);
						} else { // Si no lo tiene, directamente procesamos
							if(this.claseAlgoritmo.getMetodos().size()!=0) {
								new CuadroPreguntaSeleccionVistasEspecificas(
									this.claseAlgoritmo, Ventana.thisventana,
									this,false);
							}
							else {
								Ventana.thisventana.habilitarOpcionesDYV(false);
								this.fase2(this.claseAlgoritmo);
							}
						}
					}
				}
			}
		} else if (fichero[1] != null) {
			new CuadroError(this.vv, Texto.get("ERROR_ARCH", Conf.idioma),
					Texto.get("ERROR_ARCHNE", Conf.idioma));
		}
	}

	private boolean addImports(HashSet<String> imports, boolean copiado, String salidaCompilador, List<String> salidaCompletaCompilador) {
		// De momento no se usa
		// Para que pueda funcionar correctamente necesita que la librer�a Java2XML
		// pueda soportar genericos. Ej: ArrayList<BandaEdif>
		Java2XML.main(fichero[0] + fichero[1]);
		documento = ManipulacionElement
				.getDocumento(fichero[0] + ficheroxml);
		if (documento == null) {
			new CuadroError(vv, Texto.get("ERROR_ARCH",
					Conf.idioma), Texto.get("ERROR_ANTIESCRIT",
					Conf.idioma));
			return false;
		}
		
		// Encontrar imports para a�adir
		imports = encontrarPosiblesImports(salidaCompilador);
		if(imports != null) {
			for(String imp: imports) {
				//Intentar copiar archivo con ese nombre si existe a la carpeta imports
				Path from = Paths.get(fichero[0], imp + ".java");
				Path to = Paths.get(".\\imports\\");
				Path dest = null;
				if(Files.exists(from)) {
					// Si el archivo existe lo copiamos a la carpeta imports
					File f = new File(fichero[0], imp + ".java");
					
					if (!Files.exists(to)) {
						// Si la carpeta imports no existe se crea
					   try {
					      Files.createDirectories(to);
					   } catch (IOException ioe) {
					      ioe.printStackTrace();
					   }
					}
					dest = Paths.get(to.toString() + "\\" + f.getName());
					
					if(dest != null) {
						// A�adir "package imports;" a la clase importada y escribir en la carpeta imports
						String xml = from.toString().replace(".java", ".xml");
						
						
						// Cambiar por otro XML parser
						Java2XML.main(from.toString());
						Document documentoImport = ManipulacionElement
								.getDocumento(xml);
						// Cambiar por otro XML parser
						
						
						Element elemento = documentoImport.getDocumentElement();
						Element elementoImport = documentoImport.createElement("package-decl");
						elementoImport.setAttribute("name", "imports");
						
						elemento.appendChild(elementoImport);

						// Poner la declaraci�n "package" al principio
						while (elemento.getFirstChild() != elementoImport) {
							Node nodeAux = elemento.getFirstChild();
							elemento.removeChild(nodeAux);
							elemento.appendChild(nodeAux);
						}
						
						// Cambiar por otro XML parser
						GeneradorJava.writeJavaFile(documentoImport, dest.toString());
						// Cambiar por otro XML parser
						
						
						copiado = true;
					}
				}	
				
				// A�adir imports
				Element elemento = documento.getDocumentElement();
				Element elementoImport = documento.createElement("import");
				if(copiado) {
					elementoImport.setAttribute("module", "imports."+imp);
				}else {
					elementoImport.setAttribute("module", imp);
				}
				
				elemento.appendChild(elementoImport);

				// Poner los imports al principio de todo
				while (elemento.getFirstChild() != elementoImport) {
					Node nodeAux = elemento.getFirstChild();
					elemento.removeChild(nodeAux);
					elemento.appendChild(nodeAux);
				}
			}
			
			// Creamos un nuevo archivo java con los nuevos imports
			Calendar c = new GregorianCalendar();
			this.codigoPrevio = this.generarCodigoUnico(
					"" + c.get(Calendar.DAY_OF_MONTH),
					"" + (c.get(Calendar.MONTH) + 1),
					"" + c.get(Calendar.YEAR),
					"" + c.get(Calendar.HOUR_OF_DAY),
					"" + c.get(Calendar.MINUTE),
					"" + c.get(Calendar.SECOND));
			Transformador.correccionNombres(
					documento,
					fichero[1].replace(".java", codigoPrevio + ".java"), "", codigoPrevio);
			GeneradorJava.writeJavaFile(
					documento,
					fichero[1].replace(".java", codigoPrevio + ".java"));
			
			// Intentamos compilar de nuevo	
			String aux2[] = new String[6];
			if(!SsooValidator.isUnix()){	//	No Linux
				aux2[0] = "\"" + this.omvj.getDir() + "javac\"";
				aux2[2] = ".\\";
				aux2[3] = "\"" + fichero[1].replace(".java", codigoPrevio + ".java") + "\"";
			}else{							//	Si linux
				aux2[0] = this.omvj.getDir() + "javac";
				aux2[2] = "./";
				aux2[3] = fichero[1].replace(".java", codigoPrevio + ".java");
			}
			aux2[1] = "-d";
			aux2[4] = "-classpath";
			aux2[5] = getRunningPath();
			try {
				salidaCompletaCompilador = LlamadorSistema.getErrorDetallado(aux2);
				if(salidaCompletaCompilador.size()>0)
					//	Salida que se mostrar� en el compilador
					salidaCompilador = salidaCompletaCompilador.get(0);
			} catch (IOException e1) {	
				salidaCompletaCompilador = new ArrayList<String>();
			} catch (InterruptedException e) {
				salidaCompletaCompilador = new ArrayList<String>();
			}
		}

		// Si el usuario no est� interesado en XML original, lo borramos
		File file;
		if (obf.getfXml()) {
			file = new File(fichero[0] + ficheroxml);
			file.delete();
		}
		
		return copiado;
	}

	private HashSet<String> encontrarPosiblesImports(String salidaCompilador) {
		Reader inputString = new StringReader(salidaCompilador);
		BufferedReader reader = new BufferedReader(inputString);
		
		String oldLine;
		String newLine;
		char anterior;
		int start = -1;
		int end = -1;
		String strImport;
		
		HashSet<String> imports = new HashSet<>();
		
		try {
			oldLine = reader.readLine();
			newLine = reader.readLine();
			while(newLine != null) {
				if(newLine.contains("^")) {
					start = newLine.indexOf('^');
					
					anterior = oldLine.charAt(start - 1);
					if(anterior == ' ') {
						end = oldLine.indexOf(anterior, start);
					}else if(anterior == '<') {
						end = oldLine.indexOf('>', start);
					}
					if(start > -1 && end > -1) {
						strImport = oldLine.substring(start, end);
						imports.add(strImport);
					}
				}

				oldLine = newLine;
				newLine = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			inputString.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return imports;
	}

	/**
	 * Una vez procesado el fichero, este m�todo se encargar� de insertar las
	 * llamadas necesarias en el xml para procesar la clase final que ser�
	 * ejecutada.
	 * 
	 * @param clase
	 *            Clase a ser procesada.
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
				String aux3[] = new String[6];
				if(!SsooValidator.isUnix()){	//	No Linux
					aux3[0] = "\"" + this.omvj.getDir() + "javac\"";
				}else{							//	Si linux
					aux3[0] = this.omvj.getDir() + "javac";
				}
				aux3[1] = "-d";
				aux3[2] = "\"" + fichero[0] + "\\";
				aux3[3] = "\" SRec_" + fich2;
				aux3[4] = "-classpath";
				aux3[5] = getRunningPath();
				LlamadorSistema.ejecucionArray(aux3);
			}
			this.cuadroProgreso.setValores(Texto.get("CP_PROCES", Conf.idioma),
					85);

			String aux3[] = new String[6];
			if(!SsooValidator.isUnix()){	//	No Linux
				aux3[0] = "\"" + this.omvj.getDir() + "javac\"";
				aux3[2] = ".\\";
			}else{							//	Si linux
				aux3[0] = this.omvj.getDir() + "javac";
				aux3[2] = "./";
			}
			aux3[1] = "-d";
			aux3[3] = "SRec_" + fich2;
			aux3[4] = "-classpath";
			aux3[5] = getRunningPath();
			String salidaCompilador = LlamadorSistema.ejecucionArray(aux3);
			// String

			this.compilado = salidaCompilador.length() < 4;

			this.cuadroProgreso.setValores(Texto.get("CP_PROCES", Conf.idioma),
					70);

			file = new File("SRec_" + fich2);
			file.delete();
			/*file = new File(ficherosinex + ".class");
			file.delete();
*/
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
				// Ajustamos configuraci�n del programa a procesado realizado
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
				if (this.metodoOrigen == 1) {
					this.vv.iniciarNuevaVisualizacionSelecMetodo();
				}else if(this.metodoOrigen == 2) {
					this.vv.introducirParametros();
				}
			}
		}
	}

	/**
	 * Gestiona los detalles necesarios para poder ejecutar el algoritmo de
	 * forma controlada, tras el procesamiento del mismo
	 * 
	 * @param procesoListener
	 *            Si se especifica un valor distinto de null. se notificar� a
	 *            trav�s de este objeto, los resultados del proceso.
	 */
	public synchronized void ejecutarAlgoritmo(
			PreprocesadorEjecucionListener procesoListener) {
		// Desde la clase ClaseAlgoritmo de la ventana, debemos sacar el ID2,
		// que nos dar� el nombre del .class que tenemos que analizar
		// extraemos por ah� sus Method, etc. e identificamos el que ha sido
		// escogido con la informaci�n retenida en la clasealgoritmo

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

		for (int j = 0; j < tipos.length; j++)
			clasesParametros[j] = (Class) tipos[j];

		ParametrosParser parametrosParser = new ParametrosParser(
				metodoAlgoritmo);
		String[][] matrizParametros = parametrosParser
				.obtenerMatrizParametros();

		List<Ejecucion> ejecuciones = new ArrayList<Ejecucion>();
		for (int numeroEjecucion = 0; numeroEjecucion < matrizParametros.length; numeroEjecucion++) {

			Object[] valoresParametros = new Object[tipos.length];

			for (int i = 0; i < valoresParametros.length; i++) {
				try {
					valoresParametros[i] = GestorParametros.asignarParam(
							matrizParametros[numeroEjecucion][i],
							clasesParametros[i]);
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

			String error = Ejecutador.ejecutar(this.claseAlgoritmo.getId2(),
					metodoEjecutar.getName(), clasesParametros,
					valoresParametros);

			if (error == null) {
				Traza traza_diferido = null;
				traza_diferido = traza.copiar();
				traza_diferido.setIDTraza(ahora);
				traza_diferido.setVisibilidad(this.claseAlgoritmo);
				traza_diferido.setArchivo(this.claseAlgoritmo.getPath());
				traza_diferido.setTecnicas(MetodoAlgoritmo.tecnicasEjecucion(
						this.claseAlgoritmo, metodoAlgoritmo));
				traza_diferido.setNombreMetodoEjecucion(metodoEjecutar
						.getName());
				traza_diferido.setTitulo(tituloPanel);

				Ejecucion e = new Ejecucion(traza_diferido);
				ejecuciones.add(e);
			} else if (error.equals("Cancelado")) {
				System.gc();
				break;
			} else {
				System.gc();
				break;
			}
		}

		File file = new File(ficherosinex + ahora + ".class");
		file.delete();

		if (procesoListener != null) {
			procesoListener.ejecucionFinalizada(ejecuciones,
					matrizParametros.length == ejecuciones.size());
		}
	}
	
	/**
	 * Devuelve el path desde el que se est� ejecutando la aplicaci�n.
	 * 
	 * @return Path desde donde se est� ejecutando la aplicaci�n
	 */
	private String getRunningPath() {
		try {
			String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			return URLDecoder.decode(path, "UTF-8");
		} catch (Throwable throwable) {}
		return ".";
	}

	/**
	 * Genera un identificador �nico dada una fecha concreta.
	 * 
	 * @param dia
	 *            Dia
	 * @param mes
	 *            Mes
	 * @param anyo
	 *            A�o
	 * @param h
	 *            Hora
	 * @param m
	 *            Minutos
	 * @param s
	 *            Segundos
	 * 
	 * @return C�digo �nico.
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
	 * Dada la representaci�n de un m�todo del algoritmo, determina el m�todo a
	 * ejecutar de la clase.
	 * 
	 * @param metodoAlgoritmo
	 *            Informaci�n del m�todo.
	 * @param metodos
	 *            Lista de m�todos disponibles.
	 * 
	 * @return M�todo a ejecutar.
	 */
	private Method identificarMetodoEjecutar(MetodoAlgoritmo metodoAlgoritmo,
			Method[] metodos) {
		Method metodoEjecutar = null;

		for (int i = 0; i < metodos.length; i++) {
			// Si se llaman igual y tienen mismo n�mero de par�metros...
			if (metodoAlgoritmo.getNombre().equals(metodos[i].getName())
					&& metodoAlgoritmo.getNumeroParametros() == metodos[i]
							.getGenericParameterTypes().length) {
				// ...y si los tipos de los par�metros son iguales...
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
				
				if(tipos.length == 0)
					metodoEjecutar = metodos[i];

			}
		}

		return metodoEjecutar;
	}

	/**
	 * Determina si las dimensiones de una clase son las correctas.
	 * 
	 * @param claseCanonica
	 *            Nombre can�nico de la clase.
	 * @param numero
	 *            N�mero de dimensiones esperado.
	 * 
	 * @return true si las dimensiones son las esperadas, false en caso
	 *         contrario.
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
	
	/**
	 * Subraya las l�neas del editor
	 * 
	 * @param salidaCompletaCompilador
	 * Lista de string donde:
	 * 			- Pos 0 = Error formateado listo para el panelCompilador
	 * 			- Resto = L�neas donde ha habido errores, 
	 * 				se comprueba aqu� que son n�meros
	 * 		
	 */
	private void subrayarLineas(List<String> salidaCompletaCompilador, HashSet<String> imports){
		if(salidaCompletaCompilador.size()>0){	
			
			vv.getPanelVentana().removeSelects();
			
			//	Subrayamos l�neas
			int lineaASubrayar = 1;
			while(lineaASubrayar<salidaCompletaCompilador.size()){
				if(imports != null) {
					vv.getPanelVentana().subrayarLineaEditor(
							imports.size() + Integer.parseInt(salidaCompletaCompilador.get(lineaASubrayar))
					);
				}else {
					vv.getPanelVentana().subrayarLineaEditor(
							Integer.parseInt(salidaCompletaCompilador.get(lineaASubrayar))
					);
				}

				lineaASubrayar++;
			}
		}
	}
	
	/**
	 * Obtiene el nombre real de la clase procesada, 
	 * no la generada por SRec
	 * 
	 * @return
	 * 		String donde:
	 * 			[0] - Archivo clase ejecutada
	 * 			[1] - Nombre clase ejecutada
	 */
	public String[] getClaseProcesada() {
		return claseProcesada;
	}

}
