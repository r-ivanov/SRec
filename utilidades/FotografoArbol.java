package utilidades;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import opciones.GestorOpciones;
import opciones.OpcionFicherosRecientes;
import opciones.OpcionTipoGrafico;
import paneles.PanelValoresGlobales;
import paneles.PanelValoresRama;

import org.jgraph.JGraph;

import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroError;
import cuadros.CuadroInformacion;
import cuadros.CuadroPreguntaSobreescribir;
import cuadros.CuadroProgreso;
import datos.Ejecucion;
import datos.FamiliaEjecuciones;
import datos.GestorTrazaExportacion;

/**
 * Clase de utilidad que permite realizar capturas de las distintas vistas,
 * seg�n los par�metros de configuraci�n establecidos y solicitando la ubicaci�n
 * de los ficheros de salida al usuario.
 */
public class FotografoArbol {

	private OpcionFicherosRecientes ofr;
	private OpcionTipoGrafico otg;
	private GestorOpciones gOpciones = new GestorOpciones();

	public static boolean gifPila = false; // Indica si vamos a construir
	// animacion gif de la vista de pila

	private String ficheroSalida[] = new String[2];

	/**
	 * Crea un nuevo FotografoArbol.
	 */
	public FotografoArbol() {

	}

	/**
	 * Permite realizar una �nica captura del componente pasado por par�metro en
	 * un fichero especificado por el usuario.
	 * 
	 * @param c
	 *            Componente del que se tomar� la captura.
	 * @param numeroVista
	 *            N�mero de vista para la que se realiza la captura.
	 */
	public void hacerCapturaUnica(JComponent c, int numeroVista) {
		if (Ventana.thisventana.getTraza() != null) {
			this.ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
					"OpcionFicherosRecientes", true);
			this.otg = (OpcionTipoGrafico) this.gOpciones.getOpcion(
					"OpcionTipoGrafico", true);

			String extensionesImagen[][] = this.otg.getExtensiones();

			String definicionesArchivos[] = new String[3];
			definicionesArchivos[0] = Texto.get(
					"ARCHIVO_" + this.otg.getTipos(false)[0], Conf.idioma);
			definicionesArchivos[1] = Texto.get(
					"ARCHIVO_" + this.otg.getTipos(false)[1], Conf.idioma);
			definicionesArchivos[2] = Texto.get(
					"ARCHIVO_" + this.otg.getTipos(true)[0], Conf.idioma);

			this.ficheroSalida[0] = this.ofr.getDirExport();
			this.ficheroSalida = SelecDireccion.cuadroAbrirFichero(
					this.ficheroSalida[0],
					Texto.get("CA_GUARDEXPORTVISTA", Conf.idioma), null,
					extensionesImagen, definicionesArchivos, 0);

			// *1* Comprobarmos que el fichero existe
			File f = new File(this.ficheroSalida[0] + this.ficheroSalida[1]);
			if (!f.exists()) {
				this.hacerCapturaUnica2(Ventana.thisventana, c);
			} else {
				new CuadroPreguntaSobreescribir(Ventana.thisventana, "1"
						+ numeroVista, this, c); // null habr�a que cambiarlo
				// seguramente
			}
		} else {
			new CuadroError(Ventana.thisventana, Texto.get("ERROR_VISU",
					Conf.idioma), Texto.get("ERROR_NOVISCAPT", Conf.idioma));
		}
	}

	/**
	 * Permite realizar una captura de cada ejecuci�n activa cuando la
	 * visualizaci�n m�ltiple se encuentra activada.
	 */
	public void hacerCapturasEjecuciones() {
		GestorOpciones gOpciones = new GestorOpciones();

		OpcionFicherosRecientes ofr = (OpcionFicherosRecientes) gOpciones
				.getOpcion("OpcionFicherosRecientes", true);
		OpcionTipoGrafico otg = (OpcionTipoGrafico) gOpciones.getOpcion(
				"OpcionTipoGrafico", true);

		String extensionesImagen[][] = otg.getExtensiones();

		String definicionesArchivos[] = new String[3];
		definicionesArchivos[0] = Texto.get(
				"ARCHIVO_" + otg.getTipos(false)[0], Conf.idioma);
		definicionesArchivos[1] = Texto.get(
				"ARCHIVO_" + otg.getTipos(false)[1], Conf.idioma);
		definicionesArchivos[2] = Texto.get("ARCHIVO_" + otg.getTipos(true)[0],
				Conf.idioma);

		String ficheroSalida[] = new String[2];
		ficheroSalida[0] = ofr.getDirExport();
		ficheroSalida = SelecDireccion.cuadroAbrirFichero(ficheroSalida[0],
				Texto.get("CA_GUARDEXPORTEJECUCIONES", Conf.idioma), null,
				extensionesImagen, definicionesArchivos, 0);

		if (ficheroSalida[1] != null) {

			// Actualizamos opci�n de ficheros recientes (para mantener
			// �ltimo directorio)
			ofr = (OpcionFicherosRecientes) gOpciones.getOpcion(
					"OpcionFicherosRecientes", true);
			ofr.setDirExport(ficheroSalida[0]);
			gOpciones.setOpcion(ofr, 2);

			// Actualizamos opci�n de formato gr�fico empleado
			final String extension;
			if (ficheroSalida[1].contains(".")) {
				extension = ficheroSalida[1].substring(
						ficheroSalida[1].lastIndexOf(".") + 1,
						ficheroSalida[1].length());
				otg.setTipoUsado(extension);
				gOpciones.setOpcion(otg, 2);
			} else {
				extension = "png";
				ficheroSalida[1] = ficheroSalida[1] + "." + extension;
			}

			final String pathParcial = ficheroSalida[0]
					+ ficheroSalida[1].substring(0,
							ficheroSalida[1].lastIndexOf("."));

			new Thread() {
				@Override
				public synchronized void run() {
					Iterator<Ejecucion> iteradorEjecuciones = FamiliaEjecuciones
							.getInstance().getEjecuciones();
					while (iteradorEjecuciones.hasNext()) {

						Ejecucion e = iteradorEjecuciones.next();
						String path = pathParcial
								+ "_"
								+ e.obtenerTrazaCompleta()
										.getTitulo()
										.replace(" ", "")
										.replaceAll(
												"[^a-zA-Z0-9\\.\\-\\(\\)\\,\\{\\}]",
												"_") + "." + extension;

						JGraph grafo = e.obtenerGrafo();
						BufferedImage snapshotOriginal = grafo.getImage(
								grafo.getBackground(), 0);
						grafo.setSize(snapshotOriginal.getWidth() + 50,
								snapshotOriginal.getHeight() + 30);
						Fotografo.guardarFoto(grafo,
								Fotografo.numFormato(path), path);
					}
					new CuadroInformacion(Ventana.thisventana, Texto.get(
							"INFO_EXPCORRECTT", Conf.idioma), Texto.get(
							"INFO_EXPCORRECT", Conf.idioma), 550, 100);
				}
			}.start();
		}
	}

	/**
	 * Permite realizar una captura de un grafo.
	 * 
	 * @param ventana
	 *            Ventana a la que quedar�n asociados los cuadros.
	 * @param grafo
	 *            Grafo del cual obtendremos la captura.
	 */
	public void hacerCapturaGrafo(JFrame ventana, JGraph grafo) {

		this.ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
				"OpcionFicherosRecientes", true);
		this.otg = (OpcionTipoGrafico) this.gOpciones.getOpcion(
				"OpcionTipoGrafico", true);

		String extensionesImagen[][] = this.otg.getExtensiones();

		String definicionesArchivos[] = new String[3];
		definicionesArchivos[0] = Texto.get(
				"ARCHIVO_" + this.otg.getTipos(false)[0], Conf.idioma);
		definicionesArchivos[1] = Texto.get(
				"ARCHIVO_" + this.otg.getTipos(false)[1], Conf.idioma);
		definicionesArchivos[2] = Texto.get(
				"ARCHIVO_" + this.otg.getTipos(true)[0], Conf.idioma);

		this.ficheroSalida[0] = this.ofr.getDirExport();
		this.ficheroSalida = SelecDireccion.cuadroAbrirFichero(ventana,
				this.ficheroSalida[0],
				Texto.get("CA_GUARDEXPORTGRAFO", Conf.idioma), null,
				extensionesImagen, definicionesArchivos, 0);

		// *1* Comprobarmos que el fichero existe
		File f = new File(this.ficheroSalida[0] + this.ficheroSalida[1]);
		if (!f.exists()) {
			this.hacerCapturaUnica2(ventana, grafo);
		} else {
			new CuadroPreguntaSobreescribir(ventana, "1", this, grafo); // null
																		// habr�a
																		// que
																		// cambiarlo
			// seguramente
		}
	}

	/**
	 * Permite realizar una �nica captura del componente pasado por par�metro en
	 * un fichero especificado previamente por el usuario.
	 * 
	 * @param c
	 *            Componente del que se tomar� la captura.
	 */
	public void hacerCapturaUnica2(JFrame jFrame, JComponent c) {
		// Actualizamos opci�n de formato gr�fico empleado
		if (this.ficheroSalida[1] != null
				&& this.ficheroSalida[1].contains(".")) {
			this.otg.setTipoUsado(this.ficheroSalida[1].substring(
					this.ficheroSalida[1].lastIndexOf(".") + 1,
					this.ficheroSalida[1].length()));
			this.gOpciones.setOpcion(this.otg, 2);
		}

		if (this.ficheroSalida[1] != null) {

			if (!this.ficheroSalida[1].toLowerCase().contains(".gif")
					&& !this.ficheroSalida[1].toLowerCase().contains(".jpg")
					&& !this.ficheroSalida[1].toLowerCase().contains(".jpeg")
					&& !this.ficheroSalida[1].toLowerCase().contains(".png")) {
				this.ficheroSalida[1] = this.ficheroSalida[1] + ".png";
			}

			// Actualizamos opci�n de ficheros recientes (para mantener �ltimo
			// directorio)
			this.ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
					"OpcionFicherosRecientes", true);
			this.ofr.setDirExport(this.ficheroSalida[0]);
			this.gOpciones.setOpcion(this.ofr, 2);

			final String path = this.ficheroSalida[0] + this.ficheroSalida[1];
			File f = null;
			String tipo = this.ficheroSalida[1].substring(this.ficheroSalida[1].indexOf('.')+1);
			if(c instanceof PanelValoresGlobales) {
				f = ((PanelValoresGlobales) c).saveChartAs(path, tipo);
				if(f == null) {
					new CuadroInformacion(
							jFrame, 
							Texto.get("INFO_EXPINCORRECTT", Conf.idioma), 
							Texto.get("INFO_EXPINCORRECT", Conf.idioma),
							550, 100);
				}
			}else if(c instanceof PanelValoresRama) {
				f = ((PanelValoresRama) c).saveChartAs(path, tipo);
				if(f == null) {
					new CuadroInformacion(
							jFrame, 
							Texto.get("INFO_EXPINCORRECTT", Conf.idioma), 
							Texto.get("INFO_EXPINCORRECT", Conf.idioma),
							550, 100);
				}
			}else {
				Fotografo.guardarFoto(c, Fotografo.numFormato(path), path);
				new CuadroInformacion(jFrame, Texto.get("INFO_EXPCORRECTT",
						Conf.idioma), Texto.get("INFO_EXPCORRECT", Conf.idioma),
						550, 100);
			}
		}

	}

	/**
	 * Permite realizar la serie de capturas necesarias para construir
	 * posteriormente la animaci�n de las distintas capturas.
	 * 
	 * @param c
	 *            Componente del que se tomar�n la capturas.
	 * @param numeroVista
	 *            N�mero de vista para la que se realiza la captura.
	 */
	public void hacerCapturasPaso(JComponent c, int numeroVista) {
		GestorOpciones gOpciones = new GestorOpciones();

		if (Ventana.thisventana.getTraza() != null) {
			OpcionFicherosRecientes ofr = (OpcionFicherosRecientes) gOpciones
					.getOpcion("OpcionFicherosRecientes", true);
			OpcionTipoGrafico otg = (OpcionTipoGrafico) gOpciones.getOpcion(
					"OpcionTipoGrafico", true);

			String extensionesImagen[][] = otg.getExtensiones();

			String definicionesArchivos[] = new String[3];
			definicionesArchivos[0] = Texto.get(
					"ARCHIVO_" + otg.getTipos(false)[0], Conf.idioma);
			definicionesArchivos[1] = Texto.get(
					"ARCHIVO_" + otg.getTipos(false)[1], Conf.idioma);
			definicionesArchivos[2] = Texto.get("ARCHIVO_"
					+ otg.getTipos(true)[0], Conf.idioma);

			String ficheroSalida[] = new String[2];
			ficheroSalida[0] = ofr.getDirExport();
			ficheroSalida = SelecDireccion.cuadroAbrirFichero(ficheroSalida[0],
					Texto.get("CA_GUARDEXPORTMULTGIF", Conf.idioma), null,
					extensionesImagen, definicionesArchivos, 0);

			// *1* Comprobarmos que el fichero existe

			// Actualizamos opci�n de formato gr�fico empleado
			if (ficheroSalida[1] != null && ficheroSalida[1].contains(".")) {
				otg.setTipoUsado(ficheroSalida[1].substring(
						ficheroSalida[1].lastIndexOf(".") + 1,
						ficheroSalida[1].length()));
				gOpciones.setOpcion(otg, 2);
			}

			if (ficheroSalida[1] != null) {

				// Si no tiene extensi�n, se la ponemos
				if (!ficheroSalida[1].toLowerCase().contains(".gif")
						&& !ficheroSalida[1].toLowerCase().contains(".jpg")
						&& !ficheroSalida[1].toLowerCase().contains(".jpeg")
						&& !ficheroSalida[1].toLowerCase().contains(".png")) {
					ficheroSalida[1] = ficheroSalida[1] + ".png";
				}

				// Actualizamos opci�n de ficheros recientes (para mantener
				// �ltimo directorio)
				ofr = (OpcionFicherosRecientes) gOpciones.getOpcion(
						"OpcionFicherosRecientes", true);
				ofr.setDirExport(ficheroSalida[0]);
				gOpciones.setOpcion(ofr, 2);

				Ventana.thisventana.deshabilitarOpcionesVentana();

				final String path = ficheroSalida[0]
						+ ficheroSalida[1].substring(0,
								ficheroSalida[1].lastIndexOf("."));

				final int numeroVistaF = numeroVista;
				String tipoS = ficheroSalida[1].substring(ficheroSalida[1].lastIndexOf("."));
				
				if(c instanceof PanelValoresGlobales) {
					((PanelValoresGlobales) c).saveChartAsCapturasAnimacion(path, Fotografo.numFormato(tipoS));
					Ventana.thisventana.habilitarOpcionesVentana();
				}else if(c instanceof PanelValoresRama) {
					((PanelValoresRama) c).saveChartAsCapturasAnimacion(path, Fotografo.numFormato(tipoS));
					Ventana.thisventana.habilitarOpcionesVentana();
				}else {
					new Thread() {
						@Override
						public synchronized void run() {
							GestorTrazaExportacion gte = new GestorTrazaExportacion(
									Ventana.thisventana, numeroVistaF);
							JGraph g = gte.grafoEstadoActual();
							int[] dimGrafo = new int[2];
							dimGrafo[0] = gte.getAncho();
							dimGrafo[1] = gte.getAlto();

							Fotografo.guardarFoto(g, 0);

							int numFotos = 1;

							int x = 0;

							final JFrame jf = new JFrame();
							final JPanel panel = new JPanel();
							jf.setSize(10, 10);
							jf.setLocation(0, 0);
							jf.setResizable(false);

							final int numEstados = Ventana.thisventana.getTraza()
									.getNumNodos() * 2;

							final CuadroProgreso cp = new CuadroProgreso(
									Ventana.thisventana, Texto.get("CP_ESPERE",
											Conf.idioma), Texto.get("CP_PROCES",
											Conf.idioma), 0);

							while (!gte.finalTraza()) {
								if (x != 0) {
									gte.avanzarTraza();
								}

								g = gte.grafoEstadoActual();
								g.repaint();
								g.updateUI();
								final JGraph gg = g;

								new Thread() {
									@Override
									public void run() {
										jf.setVisible(true);
										panel.add(gg);
										panel.updateUI();

										jf.setContentPane(panel);
									}
								}.start();

								try {
									this.wait(1000);
								} catch (java.lang.InterruptedException ie) {
								}
								System.gc();
								Fotografo.guardarFoto(g, path,
										Fotografo.numFormato(path), numFotos);
								numFotos++;

								final int xx = x;
								new Thread() {
									@Override
									public void run() {
										if (cp != null) {
											cp.setValores(
													Texto.get("CP_PROCES",
															Conf.idioma),
													(int) (((xx + 1.0) / numEstados) * 100.0));
										}
									}
								}.start();

								x++;

							}
							if (cp != null) {
								cp.cerrar();
							}

							jf.setVisible(false);

							Ventana.thisventana.habilitarOpcionesVentana();
							new CuadroInformacion(Ventana.thisventana, Texto.get(
									"INFO_EXPCORRECTT", Conf.idioma), Texto.get(
									"INFO_EXPCORRECT", Conf.idioma), 550, 100);
						}
					}.start();
				}
			}
		} else {
			new CuadroError(Ventana.thisventana, Texto.get("ERROR_VISU",
					Conf.idioma), Texto.get("ERROR_NOVISCAPT", Conf.idioma));
		}
	}

	/**
	 * Permite obtener una animaci�n gif de un componente concreto para la
	 * ejecuci�n del algoritmo, solicitando al usuario la ubicaci�n del fichero
	 * de salida.
	 * 
	 * @param c
	 *            Componente del que se obtendr� la animaci�n.
	 * @param numeroVista
	 *            N�mero de vista para la que se realiza la captura.
	 */
	public void capturarAnimacionGIF(JComponent c, int numeroVista) {
		if (Ventana.thisventana.getTraza() != null) {
			this.ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
					"OpcionFicherosRecientes", true);

			this.ficheroSalida = new String[2];
			this.ficheroSalida[0] = this.ofr.getDirExport();
			this.ficheroSalida = SelecDireccion.cuadroAbrirFichero(
					this.ficheroSalida[0],
					Texto.get("CA_GUARDEXPORTGIF", Conf.idioma), null, "gif",
					Texto.get("ARCHIVO_GIF", Conf.idioma), 0);

			// *1* Comprobarmos que el fichero existe
			File f = new File(this.ficheroSalida[0] + this.ficheroSalida[1]);
			if (!f.exists()) {
				this.capturarAnimacionGIF2(c, numeroVista);
			} else {
				new CuadroPreguntaSobreescribir(Ventana.thisventana, "A"
						+ numeroVista, this, c);
			}
		} else {
			new CuadroError(Ventana.thisventana, Texto.get("ERROR_VISU",
					Conf.idioma), Texto.get("ERROR_NOVISCAPT", Conf.idioma));
		}
	}

	/**
	 * Permite obtener una animaci�n gif de un componente concreto para la
	 * ejecuci�n del algoritmo, utilizando el fichero de salida previamente
	 * especificado por el usuario.
	 * 
	 * @param c
	 *            Componente del que se obtendr� la animaci�n.
	 * @param numeroVista
	 *            N�mero de vista para la que se realiza la captura.
	 */
	public void capturarAnimacionGIF2(JComponent c, int numeroVista) {
		if (this.ficheroSalida[1] != null) {

			// Si no tiene extensi�n GIF, se la ponemos
			if (!((this.ficheroSalida[1]
					.charAt(this.ficheroSalida[1].length() - 1) == 'f' || this.ficheroSalida[1]
					.charAt(this.ficheroSalida[1].length() - 1) == 'F')
					&& (this.ficheroSalida[1].charAt(this.ficheroSalida[1]
							.length() - 2) == 'i' || this.ficheroSalida[1]
							.charAt(this.ficheroSalida[1].length() - 2) == 'I')
					&& (this.ficheroSalida[1].charAt(this.ficheroSalida[1]
							.length() - 3) == 'g' || this.ficheroSalida[1]
							.charAt(this.ficheroSalida[1].length() - 3) == 'G') && (this.ficheroSalida[1]
						.charAt(this.ficheroSalida[1].length() - 4) == '.'))) {
				this.ficheroSalida[1] = this.ficheroSalida[1] + ".gif";
			}

			// Actualizamos opci�n de ficheros recientes (para mantener �ltimo
			// directorio)
			this.ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
					"OpcionFicherosRecientes", true);
			this.ofr.setDirExport(this.ficheroSalida[0]);
			this.gOpciones.setOpcion(this.ofr, 2);

			Ventana.thisventana.deshabilitarOpcionesVentana();

			final String path = this.ficheroSalida[0] + this.ficheroSalida[1];
			
			if(c instanceof PanelValoresGlobales) {
				((PanelValoresGlobales) c).saveChartAsGIF(path);
				Ventana.thisventana.habilitarOpcionesVentana();
			}else if(c instanceof PanelValoresRama) {
				((PanelValoresRama) c).saveChartAsGIF(path);
				Ventana.thisventana.habilitarOpcionesVentana();
			}else {
				final int numeroVistaF = numeroVista;

				new Thread() {
					@Override
					public synchronized void run() {
						final boolean estabanArbolesColapsados = Conf.mostrarArbolColapsado;

						Conf.mostrarArbolColapsado = false;

						GestorTrazaExportacion gte = new GestorTrazaExportacion(
								Ventana.thisventana, numeroVistaF);
						JGraph g = gte.grafoEstadoActual();
						int[] dimGrafo = new int[2];
						dimGrafo[0] = gte.getAncho();
						dimGrafo[1] = gte.getAlto();

						Fotografo.guardarFoto(g, 0);

						int numFotos = 1;

						int x = 0;

						final JFrame jf = new JFrame();
						final JPanel panel = new JPanel();
						jf.setSize(10, 10);
						jf.setLocation(0, 0);
						jf.setResizable(false);

						final int numEstados = Ventana.thisventana.getTraza()
								.getNumNodos() * 2;

						final CuadroProgreso cp = new CuadroProgreso(
								Ventana.thisventana, Texto.get("CP_ESPERE",
										Conf.idioma), Texto.get("CP_PROCES",
										Conf.idioma), 0);

						while (!gte.finalTraza()) {
							if (x != 0) {
								gte.avanzarTraza();
							}

							g = gte.grafoEstadoActual();
							g.repaint();
							g.updateUI();
							final JGraph gg = g;

							new Thread() {
								@Override
								public void run() {
									jf.setVisible(true);
									panel.add(gg);
									panel.updateUI();

									jf.setContentPane(panel);
								}
							}.start();

							try {
								this.wait(1000);
							} catch (java.lang.InterruptedException ie) {
							}
							System.gc();
							Fotografo.guardarFoto(g, numFotos);
							numFotos++;

							final int xx = x;
							new Thread() {
								@Override
								public void run() {
									if (cp != null) {
										cp.setValores(
												Texto.get("CP_PROCES", Conf.idioma),
												(int) (((xx + 1.0) / numEstados) * 100.0));
									}
								}
							}.start();

							x++;

						}
						if (cp != null) {
							cp.cerrar();
						}

						jf.setVisible(false);

						Conf.mostrarArbolColapsado = estabanArbolesColapsados;

						Fotografo.crearGIFAnimado(numFotos, path, gifPila);
						gifPila = false;
						Ventana.thisventana.habilitarOpcionesVentana();
						new CuadroInformacion(Ventana.thisventana, Texto.get(
								"INFO_EXPCORRECTT", Conf.idioma), Texto.get(
								"INFO_EXPCORRECT", Conf.idioma), 550, 100);
					}
				}.start();
			}
		}
	}
}
