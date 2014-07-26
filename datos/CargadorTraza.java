package datos;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import opciones.GestorOpciones;
import opciones.OpcionConfVisualizacion;
import opciones.OpcionFicherosRecientes;
import opciones.OpcionOpsVisualizacion;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import paneles.PanelCompilador;
import utilidades.Logger;
import utilidades.ManipulacionElement;
import utilidades.SelecDireccion;
import utilidades.ServiciosString;
import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroError;
import cuadros.CuadroProgreso;

/**
 * Gestiona todo el procesamiento de un fichero antes de ser ejecutado y
 * visualizado
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2008
 */
public class CargadorTraza extends Thread {

	private String fichero[] = new String[2];
	private Ventana vv;
	private GestorOpciones gOpciones = new GestorOpciones();

	/**
	 * Constructor: Crea una nueva instancia del objeto.
	 * 
	 * @param ventana
	 *            Ventana de la aplicación
	 */
	public CargadorTraza(Ventana ventana) {
		this.vv = ventana;
		this.vv.cerrarVistas();
		this.start();
	}

	/**
	 * Método que se encarga de llevar a cabo, en un nuevo thread, todo el
	 * procesamiento del fichero
	 */
	@Override
	public synchronized void run() {

		CuadroProgreso cuadroProgreso;
		Document documento;

		OpcionFicherosRecientes ofr = (OpcionFicherosRecientes) this.gOpciones
				.getOpcion("OpcionFicherosRecientes", true);
		this.fichero[0] = ofr.getDirXML();

		this.fichero = SelecDireccion.cuadroAbrirFichero(this.fichero[0],
				Texto.get("CA_CARGVIS", Conf.idioma), null, "xml",
				Texto.get("ARCHIVO_XML", Conf.idioma), 1);
		// *1* Comprobar que fichero existe

		if (this.fichero != null && this.fichero[1] != null) {
			// Actualizamos opción de ficheros recientes (para mantener último
			// directorio)
			ofr.setDirXML(this.fichero[0]);
			this.gOpciones.setOpcion(ofr, 2);

			if (this.fichero != null && this.fichero[1] != null) {
				try {
					FileReader fileStream = new FileReader(this.fichero[0]
							+ this.fichero[1]);
					fileStream.close();
				} catch (FileNotFoundException fnfe) {
					new CuadroError(this.vv, Texto.get("ERROR_ARCH",
							Conf.idioma),
							Texto.get("ERROR_ARCHNE", Conf.idioma));
					this.fichero[1] = null;
				} catch (IOException ioe) {
					System.out.println("Error IO");
					this.fichero[1] = null;
				}
			}

			if (this.fichero[1] != null) {
				cuadroProgreso = new CuadroProgreso(this.vv, Texto.get(
						"CP_ESPERE", Conf.idioma), Texto.get("CP_CARGARCH",
								Conf.idioma), 0);
				try {
					// Cargar Document
					documento = ManipulacionElement
							.getDocumento(this.fichero[0] + this.fichero[1]);
					Element eDocument = documento.getDocumentElement();

					if (eDocument != null
							&& !eDocument.getNodeName().equals("Visualizacion")) {
						cuadroProgreso.cerrar();
						new CuadroError(this.vv, Texto.get("ERROR_ARCH",
								Conf.idioma), Texto.get("ERROR_ARCHNV",
										Conf.idioma));
					} else {
						cuadroProgreso.setValores(
								Texto.get("CP_EXTRAYDATOSCONF", Conf.idioma),
								20);

						// Extraer datos de configuración y guardar opciones
						Element datos[] = ManipulacionElement
								.getChildElements(documento
										.getDocumentElement());

						String valoresOpConf[] = this
								.extraerValoresOpConf(datos[0]);

						OpcionOpsVisualizacion oov = new OpcionOpsVisualizacion();
						oov.setDatosMostrar(valoresOpConf[0], valoresOpConf[1]);
						oov.setHistoria(valoresOpConf[2]);
						oov.setMostrarArbolSalto(valoresOpConf[3]
								.equals("true"));
						oov.setMostrarEstructuraEnArbol(valoresOpConf[4]
								.equals("true"));

						this.gOpciones.setOpcion(oov, 1);
						cuadroProgreso.setValores(
								Texto.get("CP_EXTRAYDATOSCONF", Conf.idioma),
								30);
						OpcionConfVisualizacion ocv = new OpcionConfVisualizacion();

						Element nodosOpFormato[] = ManipulacionElement
								.getChildElements(datos[1]);
						int valoresOpFormato[] = this.extraerDatosCelda(
								"Entrada", nodosOpFormato[0]);

						ocv.setColorFEntrada(valoresOpFormato[0],
								valoresOpFormato[1], valoresOpFormato[2]);
						ocv.setColorC1Entrada(valoresOpFormato[3],
								valoresOpFormato[4], valoresOpFormato[5]);
						ocv.setColorCAEntrada(valoresOpFormato[6],
								valoresOpFormato[7], valoresOpFormato[8]);

						valoresOpFormato = this.extraerDatosCelda("Salida",
								nodosOpFormato[1]);

						ocv.setColorFSalida(valoresOpFormato[0],
								valoresOpFormato[1], valoresOpFormato[2]);
						ocv.setColorC1Salida(valoresOpFormato[3],
								valoresOpFormato[4], valoresOpFormato[5]);
						ocv.setColorCASalida(valoresOpFormato[6],
								valoresOpFormato[7], valoresOpFormato[8]);
						ocv.setColorC1NCSalida(valoresOpFormato[9],
								valoresOpFormato[10], valoresOpFormato[11]);

						valoresOpFormato = this
								.extraerDatosOtros(nodosOpFormato[2]);

						ocv.setColorPanel(valoresOpFormato[0],
								valoresOpFormato[1], valoresOpFormato[2]);
						ocv.setColorFlecha(valoresOpFormato[3],
								valoresOpFormato[4], valoresOpFormato[5]);
						ocv.setColorActual(valoresOpFormato[6],
								valoresOpFormato[7], valoresOpFormato[8]);
						ocv.setColorCActual(valoresOpFormato[9],
								valoresOpFormato[10], valoresOpFormato[11]);

						ocv.setColorCodigoPR(valoresOpFormato[12],
								valoresOpFormato[13], valoresOpFormato[14]);
						ocv.setColorCodigoCo(valoresOpFormato[15],
								valoresOpFormato[16], valoresOpFormato[17]);
						ocv.setColorCodigoMF(valoresOpFormato[18],
								valoresOpFormato[19], valoresOpFormato[20]);
						ocv.setColorCodigoMB(valoresOpFormato[21],
								valoresOpFormato[22], valoresOpFormato[23]);
						ocv.setColorCodigoRC(valoresOpFormato[24],
								valoresOpFormato[25], valoresOpFormato[26]);

						ocv.setGrosorFlecha(valoresOpFormato[27]);
						ocv.setGrosorActual(valoresOpFormato[28]);
						ocv.setDistanciaV(valoresOpFormato[29]);
						ocv.setDistanciaH(valoresOpFormato[30]);
						ocv.setTipoBordeCelda(valoresOpFormato[31]);
						ocv.setFormaFlecha(valoresOpFormato[32]);

						int tamFuenteCodigo = (valoresOpFormato[33]);
						int tamFuenteTraza = (valoresOpFormato[34]);
						ocv.setZoomArbol(valoresOpFormato[35]);
						ocv.setZoomPila(valoresOpFormato[36]);

						ocv.setModoColor(valoresOpFormato[37]);

						String nombresFuentes[] = this
								.extraerNombresFuentes(nodosOpFormato[2]);

						ocv.setFuenteCodigo(nombresFuentes[0], tamFuenteCodigo);
						ocv.setFuenteTraza(nombresFuentes[1], tamFuenteTraza);

						boolean degradados[] = this
								.extraerDatosDegradados(nodosOpFormato[2]);

						ocv.setColorDegradadoModo1(degradados[0]);
						ocv.setColorDegradadoModo2(degradados[1]);

						valoresOpFormato = this
								.extraerColoresModo2(nodosOpFormato[2]);

						for (int i = 0; i < Conf.numColoresMetodos; i++) {
							ocv.setColorModo2(valoresOpFormato[(i * 3)],
									valoresOpFormato[(i * 3) + 1],
									valoresOpFormato[(i * 3) + 2], i);
						}

						this.gOpciones.setOpcion(ocv, 1);

						// Actualizar clase Conf
						cuadroProgreso.setValores(
								Texto.get("CP_ACTCONF", Conf.idioma), 40);
						Conf.setValoresOpsVisualizacion(false);
						Conf.setValoresVisualizacion();

						// Crear traza nueva
						cuadroProgreso.setValores(
								Texto.get("CP_CREANUEVTR", Conf.idioma), 50);
						DatosTrazaBasicos dtb = this.crearDTB(datos[2],
								datos[3]);
						Traza traza = this.crearTraza(datos[3], dtb);
						Traza trazaCompleta = this.crearTraza(datos[4], dtb);

						// Asignación de colores
						trazaCompleta.asignarNumeroMetodo();

						traza.asignarNumeroMetodo(trazaCompleta
								.getInterfacesMetodos());

						this.vv.setDTByTrazas(dtb, traza, trazaCompleta);

						// Extraer título del panel
						cuadroProgreso.setValores(
								Texto.get("CP_EXTRTITPAN", Conf.idioma), 70);
						String pathJavaOriginal[] = this
								.extraerPath(ManipulacionElement
										.getChildElements(datos[3])[0]);

						// Abrir nuevo panel de visualización con la
						// traza y el título a través de VentanaVisualizador
						cuadroProgreso.setValores(
								Texto.get("CP_ABRIRPAN", Conf.idioma), 75);
						if (Conf.fichero_log) {
							Logger.log_write("Animación cargada: "
									+ dtb.getArchivo() + " (método "
									+ dtb.getNombreMetodoEjecucion() + ")");
						}
						this.vv.setClase(null);
						this.vv.setTextoCompilador(PanelCompilador.CODIGO_VACIO);
						this.vv.visualizarAlgoritmo(null, false,
								cuadroProgreso, pathJavaOriginal[0],
								pathJavaOriginal[1], false);
						this.vv.setTitulo(this.fichero[1]);
						this.vv.setClasePendienteGuardar(false);
					}

				} catch (NullPointerException exc) {
					cuadroProgreso.cerrar();
					new CuadroError(this.vv, Texto.get("ERROR_ARCH",
							Conf.idioma), Texto.get("ERROR_ARCHCOR",
									Conf.idioma));
				} catch (Exception exc) {
					cuadroProgreso.cerrar();
					new CuadroError(this.vv, Texto.get("ERROR_ARCH",
							Conf.idioma), Texto.get("ERROR_ARCHCOR",
									Conf.idioma));
				}
				cuadroProgreso.cerrar();
			}
		}
	}

	/**
	 * Devuelve los valores de la opción de configuración.
	 * 
	 * @param e
	 *            Element del que se extraerán los datos
	 * @return valores de la opción de configuración
	 */
	private String[] extraerValoresOpConf(Element e) throws Exception {
		Element componentes[] = ManipulacionElement.getChildElements(e);
		String valoresOpConf[] = new String[5];

		if (componentes.length == 4
				&& componentes[0].getNodeName().equals("DatosMostrar")
				&& componentes[0].hasAttribute("entrada")
				&& componentes[0].hasAttribute("salida")
				&& componentes[1].getNodeName().equals("MostrarHistoria")
				&& componentes[1].hasAttribute("estadoHistoria")
				&& componentes[2].getNodeName().equals("MostrarArbolSalto")
				&& componentes[2].hasAttribute("mostrarArbol")
				&& componentes[3].getNodeName().equals(
						"MostrarEstructuraEnArbol")
						&& componentes[3].hasAttribute("mostrarEstructura")) {
			valoresOpConf[0] = componentes[0].getAttribute("entrada");
			valoresOpConf[1] = componentes[0].getAttribute("salida");
			valoresOpConf[2] = componentes[1].getAttribute("estadoHistoria");
			valoresOpConf[3] = componentes[2].getAttribute("mostrarArbol");
			valoresOpConf[4] = componentes[3].getAttribute("mostrarEstructura");
		} else {
			Exception eee = new Exception(Texto.get("EXCEP_INFOCONF",
					Conf.idioma));
			throw eee;
		}
		return valoresOpConf;
	}

	/**
	 * Devuelve los valores de la configuración de las celdas
	 * 
	 * @param tipoCelda
	 *            determina el tipo de la celda
	 * @param e
	 *            Element del que se extraerán los datos
	 * @return valores de la opción de configuración de las celdas
	 */
	private int[] extraerDatosCelda(String tipoCelda, Element e)
			throws Exception {
		Element componentes[] = ManipulacionElement.getChildElements(e);
		int valores[];
		if (tipoCelda.equals("Entrada")) {
			valores = new int[9];
		} else {
			valores = new int[12];
		}

		if (componentes[0].getNodeName().equals("Color")
				&& componentes[0].getAttribute("destino").equals("fuente")
				&& componentes[1].getNodeName().equals("Color")
				&& componentes[1].getAttribute("destino").equals("color1")
				&& componentes[2].getNodeName().equals("Color")
				&& componentes[2].getAttribute("destino").equals("color1a")) {
			int valoresFuente[] = this.extraerRGB(componentes[0]);
			int valoresColor1[] = this.extraerRGB(componentes[1]);
			int valoresColor2[] = this.extraerRGB(componentes[2]);
			// Reservamos por si es celda de salida, para salida no calculada
			int valoresColor3[] = null;

			if (!tipoCelda.equals("Entrada")) {
				valoresColor3 = this.extraerRGB(componentes[3]);
			}

			for (int i = 0; i < 3; i++) {
				valores[i] = valoresFuente[i];
				valores[i + 3] = valoresColor1[i];
				valores[i + 6] = valoresColor2[i];

				if (!tipoCelda.equals("Entrada")) {
					valores[i + 9] = valoresColor3[i];
				}
			}
		} else {
			Exception eee = new Exception(Texto.get("EXCEP_DATFORMINC",
					Conf.idioma));
			throw eee;
		}
		return valores;
	}

	/**
	 * Devuelve los valores de color para algún elemento de la visualización
	 * 
	 * @param e
	 *            Element del que se extraerán los datos
	 * @return valores del color
	 */
	private int[] extraerRGB(Element e) throws Exception {
		int valores[] = new int[3];
		if (e.hasAttribute("r") && e.hasAttribute("g") && e.hasAttribute("b")) {
			valores[0] = Integer.parseInt(e.getAttribute("r"));
			valores[1] = Integer.parseInt(e.getAttribute("g"));
			valores[2] = Integer.parseInt(e.getAttribute("b"));
		} else {
			Exception eee = new Exception(Texto.get("EXCEP_DATRGBINC",
					Conf.idioma));
			throw eee;
		}
		return valores;
	}

	/**
	 * Devuelve los valores de los nombres de fuentes
	 * 
	 * @param e
	 *            Element del que se extraerán los datos
	 * @return nombres de fuentes
	 */
	private String[] extraerNombresFuentes(Element e) {
		Element componentes[] = ManipulacionElement.getChildElements(e);

		String[] s = new String[2];
		s[0] = componentes[28].getAttribute("tam");
		s[1] = componentes[29].getAttribute("tam");
		return s;
	}

	/**
	 * Devuelve los valores de un elemento
	 * 
	 * @param e
	 *            Element del que se extraerán los datos
	 * @return valores numéricos
	 */
	private int[] extraerDatosOtros(Element e) throws Exception {
		Element componentes[] = ManipulacionElement.getChildElements(e);
		int valores[] = new int[38];

		if (componentes[Conf.numColoresMetodos + 0].getNodeName().equals(
				"Color")
				&& componentes[Conf.numColoresMetodos + 0].getAttribute(
						"destino").equals("flecha")
						&& componentes[Conf.numColoresMetodos + 1].getNodeName()
						.equals("Color")
						&& componentes[Conf.numColoresMetodos + 1].getAttribute(
								"destino").equals("panel")
								&& componentes[Conf.numColoresMetodos + 2].getNodeName()
								.equals("Color")
								&& componentes[Conf.numColoresMetodos + 2].getAttribute(
										"destino").equals("marcoActual")
										&& componentes[Conf.numColoresMetodos + 3].getNodeName()
										.equals("Color")
										&& componentes[Conf.numColoresMetodos + 3].getAttribute(
												"destino").equals("caminoActual")
												&&

												componentes[Conf.numColoresMetodos + 4].getNodeName().equals(
														"Grosor")
														&& componentes[Conf.numColoresMetodos + 4].getAttribute(
																"destino").equals("flecha")
																&& componentes[Conf.numColoresMetodos + 5].getNodeName()
																.equals("Grosor")
																&& componentes[Conf.numColoresMetodos + 5].getAttribute(
																		"destino").equals("marcoActual")
																		&& componentes[Conf.numColoresMetodos + 6].getNodeName()
																		.equals("Distancia")
																		&& componentes[Conf.numColoresMetodos + 6].getAttribute(
																				"destino").equals("vertical")
																				&& componentes[Conf.numColoresMetodos + 7].getNodeName()
																				.equals("Distancia")
																				&& componentes[Conf.numColoresMetodos + 7].getAttribute(
																						"destino").equals("horizontal")
																						&& componentes[Conf.numColoresMetodos + 8].getNodeName()
																						.equals("Tipo")
																						&& componentes[Conf.numColoresMetodos + 8].getAttribute(
																								"destino").equals("bordeCelda")
																								&& componentes[Conf.numColoresMetodos + 9].getNodeName()
																								.equals("Tipo")
																								&& componentes[Conf.numColoresMetodos + 9].getAttribute(
																										"destino").equals("formaFlecha")
																										&&

																										componentes[Conf.numColoresMetodos + 10].getNodeName().equals(
																												"Color")
																												&& componentes[Conf.numColoresMetodos + 10].getAttribute(
																														"destino").equals("codigoPR")
																														&& componentes[Conf.numColoresMetodos + 11].getNodeName()
																														.equals("Color")
																														&& componentes[Conf.numColoresMetodos + 11].getAttribute(
																																"destino").equals("codigoCo")
																																&& componentes[Conf.numColoresMetodos + 12].getNodeName()
																																.equals("Color")
																																&& componentes[Conf.numColoresMetodos + 12].getAttribute(
																																		"destino").equals("codigoMF")
																																		&& componentes[Conf.numColoresMetodos + 13].getNodeName()
																																		.equals("Color")
																																		&& componentes[Conf.numColoresMetodos + 13].getAttribute(
																																				"destino").equals("codigoMB")
																																				&& componentes[Conf.numColoresMetodos + 14].getNodeName()
																																				.equals("Color")
																																				&& componentes[Conf.numColoresMetodos + 14].getAttribute(
																																						"destino").equals("codigoRC")
																																						&&

																																						componentes[Conf.numColoresMetodos + 15].getNodeName().equals(
																																								"modoColor")
																																								&& componentes[Conf.numColoresMetodos + 15].getAttribute(
																																										"destino").equals("modo")
																																										&&

																																										// No sacamos 16 y 17 por no ser enteros
																																										componentes[Conf.numColoresMetodos + 18].getNodeName().equals(
																																												"Fuente")
																																												&& componentes[Conf.numColoresMetodos + 18].getAttribute(
																																														"destino").equals("trazaE")
																																														&& componentes[Conf.numColoresMetodos + 19].getNodeName()
																																														.equals("Fuente")
																																														&& componentes[Conf.numColoresMetodos + 19].getAttribute(
																																																"destino").equals("trazaS")
																																																&&

																																																componentes[Conf.numColoresMetodos + 20].getNodeName().equals(
																																																		"FuenteTam")
																																																		&& componentes[Conf.numColoresMetodos + 20].getAttribute(
																																																				"destino").equals("trazaE")
																																																				&& componentes[Conf.numColoresMetodos + 21].getNodeName()
																																																				.equals("FuenteTam")
																																																				&& componentes[Conf.numColoresMetodos + 21].getAttribute(
																																																						"destino").equals("trazaS")
																																																						&&

																																																						componentes[Conf.numColoresMetodos + 22].getNodeName().equals(
																																																								"Zoom")
																																																								&& componentes[Conf.numColoresMetodos + 22].getAttribute(
																																																										"destino").equals("arbol")
																																																										&& componentes[Conf.numColoresMetodos + 23].getNodeName()
																																																										.equals("Zoom")
																																																										&& componentes[Conf.numColoresMetodos + 23].getAttribute(
																																																												"destino").equals("pila")) {
			// Colores
			int valoresFlecha[] = this
					.extraerRGB(componentes[Conf.numColoresMetodos + 0]);
			int valoresPanel[] = this
					.extraerRGB(componentes[Conf.numColoresMetodos + 1]);
			int valoresMarcoActual[] = this
					.extraerRGB(componentes[Conf.numColoresMetodos + 2]);
			int valoresCaminoActual[] = this
					.extraerRGB(componentes[Conf.numColoresMetodos + 3]);
			int valoresCodigoPR[] = this
					.extraerRGB(componentes[Conf.numColoresMetodos + 10]);
			int valoresCodigoCo[] = this
					.extraerRGB(componentes[Conf.numColoresMetodos + 11]);
			int valoresCodigoMF[] = this
					.extraerRGB(componentes[Conf.numColoresMetodos + 12]);
			int valoresCodigoMB[] = this
					.extraerRGB(componentes[Conf.numColoresMetodos + 13]);
			int valoresCodigoRC[] = this
					.extraerRGB(componentes[Conf.numColoresMetodos + 14]);

			for (int i = 0; i < 3; i++) {
				valores[i + 0] = valoresPanel[i];
				valores[i + 3] = valoresFlecha[i];
				valores[i + 6] = valoresMarcoActual[i];
				valores[i + 9] = valoresCaminoActual[i];

				valores[i + 12] = valoresCodigoPR[i];
				valores[i + 15] = valoresCodigoCo[i];
				valores[i + 18] = valoresCodigoMF[i];
				valores[i + 21] = valoresCodigoMB[i];
				valores[i + 24] = valoresCodigoRC[i];
			}

			// Valores numéricos
			valores[27] = Integer
					.parseInt(componentes[Conf.numColoresMetodos + 4]
							.getAttribute("tam"));
			valores[28] = Integer
					.parseInt(componentes[Conf.numColoresMetodos + 5]
							.getAttribute("tam"));
			valores[29] = Integer
					.parseInt(componentes[Conf.numColoresMetodos + 6]
							.getAttribute("tam"));
			valores[30] = Integer
					.parseInt(componentes[Conf.numColoresMetodos + 7]
							.getAttribute("tam"));
			valores[31] = Integer
					.parseInt(componentes[Conf.numColoresMetodos + 8]
							.getAttribute("tam"));
			valores[32] = Integer
					.parseInt(componentes[Conf.numColoresMetodos + 9]
							.getAttribute("tam"));

			// Tamaño de fuentes y zooms
			valores[33] = Integer
					.parseInt(componentes[Conf.numColoresMetodos + 20]
							.getAttribute("tam"));
			valores[34] = Integer
					.parseInt(componentes[Conf.numColoresMetodos + 21]
							.getAttribute("tam"));
			valores[35] = Integer
					.parseInt(componentes[Conf.numColoresMetodos + 22]
							.getAttribute("tam"));
			valores[36] = Integer
					.parseInt(componentes[Conf.numColoresMetodos + 23]
							.getAttribute("tam"));

			// Modo de color
			valores[37] = Integer
					.parseInt(componentes[Conf.numColoresMetodos + 15]
							.getAttribute("tam"));
		} else {
			Exception eee = new Exception(Texto.get("EXCEP_DATRGBINC",
					Conf.idioma));
			throw eee;
		}
		return valores;
	}

	/**
	 * Devuelve los valores de los degradados
	 * 
	 * @param elementoOtros
	 *            Element del que se extraerán los datos
	 * @return valores para los degradados
	 */
	private int[] extraerColoresModo2(Element elementoOtros) throws Exception {
		Element componentes[] = ManipulacionElement
				.getChildElements(elementoOtros);

		int[] componentesColores = new int[3 * Conf.numColoresMetodos];

		for (int i = 0; i < Conf.numColoresMetodos; i++) {
			int color[] = this.extraerRGB(componentes[i]);
			for (int j = 0; j < 3; j++) {
				componentesColores[(i * 3) + j] = color[j];
			}
		}

		return componentesColores;
	}

	/**
	 * Devuelve los valores de los degradados
	 * 
	 * @param elementoOtros
	 *            Element del que se extraerán los datos
	 * @return valores para los degradados
	 */
	private boolean[] extraerDatosDegradados(Element elementoOtros)
			throws Exception {
		Element componentes[] = ManipulacionElement
				.getChildElements(elementoOtros);
		boolean degradados[] = new boolean[2];
		if (componentes[26].hasAttribute("destino")
				&& componentes[27].hasAttribute("destino")) {
			degradados[0] = (componentes[26].getAttribute("tam").equals("true"));
			degradados[1] = (componentes[27].getAttribute("tam").equals("true"));
		} else {
			Exception eee = new Exception(Texto.get("EXCEP_DATDEGINC",
					Conf.idioma));
			throw eee;
		}
		return degradados;
	}

	/**
	 * Permite crear los datos de traza básicos dados los elementos de datos de
	 * traza básicos y el de traza.
	 * 
	 * @param edtb
	 *            Elemento de datos de traza básicos.
	 * @param etraza
	 *            Elemento de la traza.
	 * 
	 * @return Datos de traza básicos
	 */
	private DatosTrazaBasicos crearDTB(Element edtb, Element etraza) {
		Element datos = ManipulacionElement.nodeListToElementArray(etraza
				.getChildNodes())[0];

		String archivo = datos.getAttribute("archivo");
		String idTraza = datos.getAttribute("idTraza");
		String metodoEjecucion = datos.getAttribute("metodoEjecucion");
		String nombre = datos.getAttribute("nombre");

		DatosTrazaBasicos dtb = new DatosTrazaBasicos(archivo, idTraza,
				metodoEjecucion, nombre);
		Element metodos[] = ManipulacionElement.nodeListToElementArray(edtb
				.getElementsByTagName("Metodo"));

		for (int i = 0; i < metodos.length; i++) {
			Element paramE[] = ManipulacionElement
					.nodeListToElementArray(metodos[i]
							.getElementsByTagName("ParamE"));
			Element paramS[] = ManipulacionElement
					.nodeListToElementArray(metodos[i]
							.getElementsByTagName("ParamS"));
			DatosMetodoBasicos dmt = new DatosMetodoBasicos(
					metodos[i].getAttribute("nombre"), paramE.length,
					paramS.length, (metodos[i].getAttribute("retorno").equals(
							"true") ? true : false));

			dmt.setEsPrincipal((metodos[i].getAttribute("metodoPrincipal")
					.equals("true") ? true : false));
			dmt.setEsVisible((metodos[i].getAttribute("metodoVisible").equals(
					"true") ? true : false));

			for (int j = 0; j < paramE.length; j++) {
				int dim = Integer.parseInt(paramE[j].getAttribute("dim"));
				String nombreParametro = paramE[j].getAttribute("nombre");
				String tipo = paramE[j].getAttribute("tipo");
				boolean visible = Boolean.parseBoolean(paramE[j]
						.getAttribute("visible"));

				dmt.addParametroEntrada(nombreParametro, tipo, dim, visible);
			}

			for (int j = 0; j < paramS.length; j++) {
				int dim = Integer.parseInt(paramS[j].getAttribute("dim"));
				String nombreParametro = paramS[j].getAttribute("nombre");
				String tipo = paramS[j].getAttribute("tipo");
				boolean visible = Boolean.parseBoolean(paramS[j]
						.getAttribute("visible"));

				dmt.addParametroSalida(nombreParametro, tipo, dim, visible);
			}
			dtb.addMetodo(dmt);
		}

		return dtb;
	}

	/**
	 * Crea una traza desde el Element dado y los datos de traza básicos.
	 * 
	 * @param e
	 *            Element del que se extraerán los datos para la traza
	 * @param dtb
	 *            Datos de traza básicos.
	 * @return traza cargada
	 */
	private Traza crearTraza(Element e, DatosTrazaBasicos dtb) throws Exception {

		Element datosTrazaElement = ManipulacionElement.getChildElements(e)[0];

		Element raElement = ManipulacionElement.getChildElements(e)[1];

		Traza traza = new Traza();

		if (datosTrazaElement.hasAttribute("metodoEjecucion")
				&& datosTrazaElement.hasAttribute("nombre")
				&& datosTrazaElement.hasAttribute("archivo")
				&& datosTrazaElement.hasAttribute("idTraza")) {

			traza.setArchivo(datosTrazaElement.getAttribute("archivo"));
			traza.setIDTraza(datosTrazaElement.getAttribute("idTraza"));
			traza.setNombreMetodoEjecucion(datosTrazaElement
					.getAttribute("metodoEjecucion"));
			traza.setTitulo(datosTrazaElement.getAttribute("nombre"));

			boolean[] tecnicasXML = new boolean[2];
			tecnicasXML[0] = (datosTrazaElement.getAttribute("tecnicaREC")
					.toLowerCase().equals("true"));
			tecnicasXML[1] = (datosTrazaElement.getAttribute("tecnicaDYV")
					.toLowerCase().equals("true"));

			int tecnicas[] = null;
			if (tecnicasXML[0] && tecnicasXML[1]) {
				tecnicas = new int[2];
				tecnicas[0] = MetodoAlgoritmo.TECNICA_REC;
				tecnicas[1] = MetodoAlgoritmo.TECNICA_DYV;
			} else if (tecnicasXML[0]) {
				tecnicas = new int[1];
				tecnicas[0] = MetodoAlgoritmo.TECNICA_REC;
			} else if (tecnicasXML[1]) {
				tecnicas = new int[1];
				tecnicas[0] = MetodoAlgoritmo.TECNICA_DYV;
			}

			traza.setTecnicas(tecnicas);

			RegistroActivacion ra = new RegistroActivacion();

			this.crearTraza(ra, raElement, traza, dtb);

			traza.setRaiz(ra);
		} else {
			Exception eee = new Exception(Texto.get("EXCEP_DATTRINC",
					Conf.idioma));
			throw eee;
		}
		return traza;
	}

	/**
	 * Devuelve el path del fichero Java original
	 * 
	 * @param e
	 *            Element del que se extraerán los datos para la traza
	 * @return título
	 */
	private String[] extraerPath(Element e) throws Exception {
		String path[] = new String[2];
		if (e.hasAttribute("archivo")) {
			String contenido = e.getAttribute("archivo");
			path[0] = contenido.substring(0, contenido.lastIndexOf("\\") + 1);
			path[1] = contenido.substring(contenido.lastIndexOf("\\") + 1,
					contenido.length());
		} else {
			Exception eee = new Exception(Texto.get("EXCEP_JAVAORINOENC",
					Conf.idioma));
			throw eee;
		}
		return path;
	}

	/**
	 * Rellena la traza con los valores para los distintos nodos
	 * 
	 * @param ra
	 *            Registro de activación actual (el método es recursivo)
	 * @param e
	 *            Element del que se extraerán los datos
	 * @param traza
	 *            en la cual se irán agregando los registros de activación en
	 *            función de los valores leídos
	 * @param dtb
	 *            datos básicos de la traza a rellenar.
	 */
	private void crearTraza(RegistroActivacion ra, Element e, Traza traza,
			DatosTrazaBasicos dtb) throws Exception {
		Element nodos[] = ManipulacionElement.getChildElements(e);

		if (nodos[0].getNodeName().equals("Valor")
				&& nodos[0].hasAttribute("actual")
				&& nodos[0].hasAttribute("caminoActual")
				&& nodos[0].hasAttribute("entradaVisible")
				&& (nodos[0].getAttribute("entradaVisible").equals("true") || nodos[0]
						.getAttribute("entradaVisible").equals("false"))
						&& nodos[0].hasAttribute("salidaVisible")
						&& (nodos[0].getAttribute("salidaVisible").equals("true") || nodos[0]
								.getAttribute("salidaVisible").equals("false"))
								&&

								nodos[1].getNodeName().equals("Param")
								&& nodos[1].hasAttribute("numHijos")
								&& nodos[1].hasAttribute("nID")
								&& nodos[1].hasAttribute("hijoVisible")
								&& nodos[1].hasAttribute("historico")
								&& (nodos[1].getAttribute("historico").equals("true") || nodos[1]
										.getAttribute("historico").equals("false"))
										&& nodos[1].hasAttribute("contraido")
										&& (nodos[1].getAttribute("contraido").equals("true") || nodos[1]
												.getAttribute("contraido").equals("false"))
												&& nodos[1].hasAttribute("inhibido")
												&& (nodos[1].getAttribute("inhibido").equals("true") || nodos[1]
														.getAttribute("inhibido").equals("false"))
														&& nodos[1].hasAttribute("mostradoEntero")
														&& (nodos[1].getAttribute("mostradoEntero").equals("true") || nodos[1]
																.getAttribute("mostradoEntero").equals("false")) &&

																nodos[2].getNodeName().equals("Metodo")
																&& nodos[2].hasAttribute("nombreMetodo")) {

			int x = 0;
			String param = new String("");
			do {
				param = nodos[0].getAttribute("paramE" + (x + 1));
				if (param != null && param.length() > 0) {
					ra.setEntradaString(param);
				}
				x++;
			} while (param.length() > 0);
			int numParamE = --x;

			x = 0;
			param = new String("");
			do {
				param = nodos[0].getAttribute("paramS" + (x + 1));
				if (param != null && param.length() > 0) {
					ra.setSalidaString(param);
				}
				x++;
			} while (param.length() > 0);
			int numParamS = --x;

			String clasesParamE[] = new String[numParamE];
			String clasesParamS[] = new String[numParamS];

			int dimE[] = new int[numParamE];
			int dimS[] = new int[numParamS];

			x = 0;
			do {
				clasesParamE[x] = nodos[0].getAttribute("tipoE" + (x + 1));
				ra.setEntradaClase(clasesParamE[x]);
				x++;
			} while (x < clasesParamE.length);

			x = 0;
			do {
				clasesParamS[x] = nodos[0].getAttribute("tipoS" + (x + 1));
				ra.setSalidaClase(clasesParamS[x]);
				x++;
			} while (x < clasesParamS.length);

			x = 0;
			do {
				dimE[x] = Integer.parseInt(nodos[0].getAttribute("dimE"
						+ (x + 1)));
				ra.setEntradaDim(dimE[x]);
				x++;
			} while (x < dimE.length);

			x = 0;
			do {
				dimS[x] = Integer.parseInt(nodos[0].getAttribute("dimS"
						+ (x + 1)));
				ra.setSalidaDim(dimS[x]);
				x++;
			} while (x < dimS.length);

			DatosMetodoBasicos dmb = dtb.getMetodo(
					nodos[2].getAttribute("nombreMetodo"), clasesParamE,
					clasesParamS, dimE, dimS);

			ra.setNombreParametros(dmb.getNombreParametrosE());

			ra.setVisibilidad(
					nodos[0].getAttribute("entradaVisible").equals("true"),
					nodos[0].getAttribute("salidaVisible").equals("true"),
					nodos[0].getAttribute("mostradoEntero").equals("true"));

			try {
				ra.setVisibilidad(dmb.getVisibilidadE(), dmb.getVisibilidadS());
			} catch (Exception excep) {
				excep.printStackTrace();
			}

			ra.setContraido(nodos[1].getAttribute("contraido").equals("true"));
			ra.setInhibido(nodos[1].getAttribute("inhibido").equals("true"));

			ra.setID(Integer.parseInt(nodos[1].getAttribute("nID")));

			ra.setInformacionHijos(
					nodos[1].getAttribute("historico").equals("true"), nodos[1]
							.getAttribute("mostradoEntero").equals("true"));

			ra.iluminar(nodos[1].getAttribute("iluminado").equals("true"));

			if (nodos[0].getAttribute("actual").equals("true")) {
				ra.setEsNodoActual(true);
			}
			if (nodos[0].getAttribute("caminoActual").equals("true")) {
				ra.setEsCaminoActual(true);
			}

			if (nodos[3].hasAttribute("indices")) // Si es nodo que tiene estructura
			{
				// Inicio - carga de estructura
				int indices[] = ServiciosString.extraerValoresInt(
						nodos[3].getAttribute("indices"), '|');

				Estructura estrE = new Estructura(
						nodos[3].getAttribute("tipo"),
						nodos[3].getAttribute("dim"),
						nodos[3].getAttribute("entrada"),
						nodos[3].getAttribute("tam1"),
						nodos[3].getAttribute("tam2"));
				Object estructuraEntrada = estrE.getObjeto();

				Estructura estrS = new Estructura(
						nodos[3].getAttribute("tipo"),
						nodos[3].getAttribute("dim"),
						nodos[3].getAttribute("salida"),
						nodos[3].getAttribute("tam1"),
						nodos[3].getAttribute("tam2"));
				Object estructuraSalida = estrS.getObjeto();

				int indEstructuraE = -1;
				int indEstructuraS = -1;
				try {
					indEstructuraE = Integer.parseInt(nodos[3]
							.getAttribute("indEstructuraE"));
					indEstructuraS = Integer.parseInt(nodos[3]
							.getAttribute("indEstructuraS"));
				} catch (Exception exc) {
					System.out
					.println("Error al recoger valor indEstructura desde XML");
				}

				ra.getEntrada().setEstructuraIndices(estructuraEntrada,
						indEstructuraE, indices);
				ra.getSalida().setEstructuraIndices(estructuraSalida,
						indEstructuraS, indices);
				// Final - carga de estructura

			}

			Element nodosHijos[] = ManipulacionElement
					.getChildElements(nodos[4]);
			RegistroActivacion hijos[] = new RegistroActivacion[nodosHijos.length];

			ra.setNombreMetodo(nodos[2].getAttribute("nombreMetodo"));
			ra.setDevuelveValor(nodos[2].getAttribute("devuelveValor").equals(
					"true"));
			ra.setEsMetodoVisible(dmb.getEsVisible());

			for (int i = 0; i < nodosHijos.length; i++) {
				hijos[i] = new RegistroActivacion();
				hijos[i].setPadre(ra);
				this.crearTraza(hijos[i], nodosHijos[i], traza, dtb);
				ra.setHijo(hijos[i]);
			}
		} else {
			Exception eee = new Exception(Texto.get("EXCEP_DATREGERROR",
					Conf.idioma));
			throw eee;
		}
	}

}
