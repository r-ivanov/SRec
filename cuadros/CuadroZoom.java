package cuadros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.TitledBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import conf.*;
import datos.MetodoAlgoritmo;
import botones.*;
import paneles.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite construir un cuadro que gestiona la configuración de zoom en las
 * vistas del árbol.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroZoom extends Thread implements ActionListener, KeyListener,
		MouseListener, ChangeListener {
	private static final boolean DEPURAR = false;

	public static final int MAS5 = 5;
	public static final int MENOS5 = 15;
	public static final int AJUSTE = 25;

	private static final int TAM_BOTON = 20;

	private static PanelVentana panelVentana;
	private static Ventana ventana;
	private JDialog dialogo;

	private static int zoomArbol;
	private static int zoomPila;
	private static int zoomTraza;
	private static int zoomCrono;
	private static int zoomEstructura;
	private static int zoomGrafoDep;

	private static int dimensionesPaneles[];
	private static int dimensionesGrafos[];

	private static JLabel etiqTitulos[];
	private static JLabel etiqPorcentaje[];
	private static JSlider barras[];
	private static JTextField cuadros[];
	private BotonIcono zoommas[];
	private BotonIcono zoommenos[];
	private BotonIcono zoomajuste[];

	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	private BotonTexto inicializar;

	private static OpcionConfVisualizacion ocv = null;
	private static GestorOpciones gOpciones = new GestorOpciones();

	// [0][1]=panelEstructura [2][3]=panelPrincipal
	private static int[] dimPaneles;
	// [0][1]=grafosEstructura [2][3]=grafoPrincipal
	private static int[] dimGrafos;

	private boolean escribiendo = false;

	/**
	 * Construye un cuadro que permite configurar el zoom de las distintas
	 * vistas del árbol.
	 * 
	 * @param ventana
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo
	 * @param panelVentana
	 *            PanelVentana que contiene las distintas vistas del árbol.
	 */
	public CuadroZoom(Ventana ventana, PanelVentana pV) {

		this.dialogo = new JDialog(ventana, true);
		inicializar(ventana, pV);

		if (DEPURAR) {
			int[] zooms = panelVentana.getZooms();
			for (int i = 0; i < zooms.length; i++) {
				System.out.println("zooms " + i + ": " + zooms[i]);
			}

			for (int i = 0; i < dimensionesPaneles.length; i++) {
				System.out.println("dimP " + i + ": " + dimensionesPaneles[i]);
			}

			for (int i = 0; i < dimensionesGrafos.length; i++) {
				System.out.println("dimG " + i + ": " + dimensionesGrafos[i]);
			}
		}
		this.start();
	}

	private static void inicializar(Ventana v, PanelVentana pV) {
		ventana = v;
		panelVentana = pV;
		zoomArbol = pV.getZooms()[0];
		zoomPila = pV.getZooms()[1];
		zoomCrono = pV.getZooms()[2];
		zoomEstructura = pV.getZooms()[3];
		zoomGrafoDep = pV.getZooms()[4];

		dimensionesPaneles = pV.getTamanoPaneles();
		dimensionesGrafos = pV.getTamanoGrafos();
		
		ocv = (OpcionConfVisualizacion) gOpciones.getOpcion(
				"OpcionConfVisualizacion", false);
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		// Vista de traza no se maneja desde aquí, pero dejamos hueco en
		// estructuras.
		int numVistas = 6;
		int numVistasVisibles = (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
				ventana.getTraza().getTecnicas()) ? 5 : 3);

		etiqTitulos = new JLabel[numVistas];
		etiqPorcentaje = new JLabel[numVistas];
		barras = new JSlider[numVistas];
		cuadros = new JTextField[numVistas];
		this.zoommenos = new BotonIcono[numVistas];
		this.zoommas = new BotonIcono[numVistas];
		this.zoomajuste = new BotonIcono[numVistas];

		// Panel superior
		JPanel panelSuperior = new JPanel();

		// quitamos una porque la traza no la manejamos aquí.
		GridLayout gl = new GridLayout(numVistasVisibles - 1, 1);
		panelSuperior.setLayout(gl);
		panelSuperior.setBorder(new TitledBorder(Texto.get("CZ_AJUSTES",
				Conf.idioma)));

		JPanel panelesZoom[] = new JPanel[numVistasVisibles];

		panelesZoom[0] = panelZoom(Texto.get("V_ARBOL", Conf.idioma),
				zoomArbol, 0);
		panelSuperior.add(panelesZoom[0]);

		panelesZoom[1] = panelZoom(Texto.get("V_PILA", Conf.idioma), zoomPila,
				1);
		panelSuperior.add(panelesZoom[1]);

		if (numVistasVisibles > 3) {
			panelesZoom[3] = panelZoom(Texto.get("V_CRONO", Conf.idioma),
					zoomCrono, 3);
			panelSuperior.add(panelesZoom[3]);

			panelesZoom[4] = panelZoom(Texto.get("V_ESTRUC", Conf.idioma),
					zoomEstructura, 4);
			panelSuperior.add(panelesZoom[4]);
		}

		// Panel botón
		JPanel panelBoton = new JPanel();
		this.aceptar = new BotonAceptar();
		this.cancelar = new BotonCancelar();
		this.inicializar = new BotonTexto(Texto.get("BOTONINIC", Conf.idioma));
		this.aceptar.addMouseListener(this);
		this.cancelar.addMouseListener(this);
		this.inicializar.addMouseListener(this);
		panelBoton.add(this.aceptar);
		panelBoton.add(this.cancelar);
		panelBoton.add(this.inicializar);

		// Panel general
		JPanel panel = new JPanel();
		BorderLayout bl = new BorderLayout();
		panel.setLayout(bl);

		panel.add(panelSuperior, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("CZ_CONFZOOM", Conf.idioma));

		if (ventana.msWindows) {
			// quitamos una porque la traza no la manejamos aquí
			this.dialogo.setSize(465, 50 + (50 * numVistasVisibles - 1));
		} else {
			// quitamos una porque la traza no la manejamos aquí
			this.dialogo.setSize(465, 114 + (36 * numVistasVisibles - 1));
		}

		int coord[] = Conf.ubicarCentro(
				(int) this.dialogo.getSize().getWidth(), (int) this.dialogo
						.getSize().getHeight());
		this.dialogo.setLocation(coord[0], coord[1]);

		this.dialogo.setResizable(false);

		setValores();
		this.dialogo.setVisible(true);
	}

	/**
	 * Crea un nuevo panel para el control del zoom de una de las vistas.
	 * 
	 * @param titulo
	 *            Título del panel.
	 * @param valor
	 *            Valor actual de zoom.
	 * @param indice
	 *            indice de la vista.
	 * @return Devuelve un nuevo panel con los controles de zoom para una de las
	 *         vistas del árbol.
	 */
	private JPanel panelZoom(String titulo, int valor, int indice) {
		JPanel panel = new JPanel();
		BorderLayout bl = new BorderLayout();
		panel.setLayout(bl);

		etiqTitulos[indice] = new JLabel(titulo);
		etiqTitulos[indice].setToolTipText(Texto.get("CZ_TITVIS", Conf.idioma));
		etiqTitulos[indice].setPreferredSize(new Dimension(400, 16));

		etiqPorcentaje[indice] = new JLabel(valor + "%");
		etiqPorcentaje[indice].setToolTipText(Texto.get("CZ_NIVZOOM",
				Conf.idioma));

		barras[indice] = new JSlider(-100, 100, valor);
		barras[indice].addChangeListener(this);
		barras[indice].addMouseListener(this);
		barras[indice].addKeyListener(this);
		barras[indice].setToolTipText(Texto.get("CZ_REG100", Conf.idioma));

		JLabel leyenda = new JLabel("(-100%..100%)");

		cuadros[indice] = new JTextField(5);
		cuadros[indice].addKeyListener(this);
		cuadros[indice].setHorizontalAlignment(SwingConstants.TRAILING);
		cuadros[indice].setToolTipText(Texto.get("CZ_REG100", Conf.idioma));

		this.zoommas[indice] = new BotonIcono(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/icono_zoommas.gif")), TAM_BOTON, TAM_BOTON);
		this.zoommas[indice].addActionListener(this);
		this.zoommas[indice].setToolTipText(Texto.get("CZ_MAS5", Conf.idioma));

		this.zoommenos[indice] = new BotonIcono(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/icono_zoommenos.gif")), TAM_BOTON, TAM_BOTON);
		this.zoommenos[indice].addActionListener(this);
		this.zoommenos[indice].setToolTipText(Texto
				.get("CZ_MENS5", Conf.idioma));

		this.zoomajuste[indice] = new BotonIcono(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/icono_zoomajuste.gif")), TAM_BOTON, TAM_BOTON);
		this.zoomajuste[indice].addActionListener(this);
		this.zoomajuste[indice].setToolTipText(Texto.get("CZ_AJUST",
				Conf.idioma));

		JPanel panelBarra = new JPanel();
		panelBarra.add(barras[indice]);

		JPanel panelCuadroyBotones = new JPanel();
		panelCuadroyBotones.add(leyenda);
		panelCuadroyBotones.add(cuadros[indice]);
		panelCuadroyBotones.add(this.zoommas[indice]);
		panelCuadroyBotones.add(this.zoommenos[indice]);
		panelCuadroyBotones.add(this.zoomajuste[indice]);

		JPanel panelSur = new JPanel();
		panelSur.setLayout(new BorderLayout());
		panelSur.add(panelBarra, BorderLayout.WEST);
		panelSur.add(panelCuadroyBotones, BorderLayout.EAST);

		panel.add(etiqTitulos[indice], BorderLayout.WEST);
		panel.add(panelSur, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Asigna valores a los elementos del cuadro
	 */
	private void setValores() {
		for (int i = 0; i < cuadros.length; i++) {
			if (cuadros[i] != null && barras[i] != null) {
				cuadros[i].setText("" + barras[i].getValue());
			}
		}
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			for (int i = 0; i < this.zoommas.length; i++) {
				if (this.zoommas[i] != null && this.zoommas[i] == e.getSource()) {
					if (Conf.fichero_log) {
						Logger.log_write("Cuadro de Zoom: zoom+ (" + i + ")");
					}
					zoomMas5(i);
				}
			}

			for (int i = 0; i < this.zoommenos.length; i++) {
				if (this.zoommenos[i] != null
						&& this.zoommenos[i] == e.getSource()) {
					if (Conf.fichero_log) {
						Logger.log_write("Cuadro de Zoom: zoom- (" + i + ")");
					}
					zoomMenos5(i);
				}
			}

			for (int i = 0; i < this.zoomajuste.length; i++) {
				if (this.zoomajuste[i] != null
						&& this.zoomajuste[i] == e.getSource()) {
					if (Conf.fichero_log) {
						Logger.log_write("Cuadro de Zoom: zoomAjuste (" + i
								+ ")");
					}
					zoomAjuste(i);
				}
			}

		}
	}

	/**
	 * Permite ajustar el nivel de zoom de una determinada vista.
	 * 
	 * @param v
	 *            Ventana a la que está asociado el cuadro.
	 * @param pV
	 *            PanelVentana que contiene las distinas vistas del árbol.
	 * @param i
	 *            indice de la vista que se desea modificar.
	 * @param tipoVariacion
	 *            variación del zoom que se desea aplicar.
	 */
	public static void zoomAjuste(Ventana v, PanelVentana pV, int i,
			int tipoVariacion) {
		inicializar(v, pV);
		barras = new JSlider[5];

		if (i == 0) {
			barras[i] = new JSlider(-100, 100, zoomArbol);
		} else if (i == 1) {
			barras[i] = new JSlider(-100, 100, zoomPila);
		}else if (i == 2) {
			barras[i] = new JSlider(-100, 100, zoomCrono);
		}
		else if (i == 3) {
//			barras[i] = new JSlider(-100, 100, zoomCrono);
			barras[i] = new JSlider(-100, 100, zoomEstructura);
		} else if (i == 4) {
//			barras[i] = new JSlider(-100, 100, zoomEstructura);
			barras[i] = new JSlider(-100, 100, zoomGrafoDep);
		}
		if (tipoVariacion == CuadroZoom.MAS5) {
		zoomMas5(i);	
		} else if (tipoVariacion == CuadroZoom.MENOS5) {
			
			
			zoomMenos5(i);
		} else if (tipoVariacion == CuadroZoom.AJUSTE) {
			
			zoomAjuste(i);
		}
		
	}

	/**
	 * Amplia el zoom de una determinada vista en 5 puntos.
	 * 
	 * @param i
	 *            Índice de la vista.
	 */
	private static void zoomMas5(int i) {
		barras[i].setValue(barras[i].getValue() + 5);
		Conf.setPanelArbolReajustado(true);
		peticionRefrescar(i,1);
	}

	/**
	 * Reduce el zoom de una determinada vista en 5 puntos.
	 * 
	 * @param i
	 *            Índice de la vista.
	 */
	private static void zoomMenos5(int i) {
		barras[i].setValue(barras[i].getValue() - 5);
		Conf.setPanelArbolReajustado(true);
		peticionRefrescar(i,-1);
	}

	/**
	 * Ajusta el zoom al tamaño del panel
	 * 
	 * @param i
	 *            indica la posición que se desea actualizar
	 */
	private static void zoomAjuste(int i) {
		dimPaneles = ventana.dimensionesPanelesVisualizacion();
		// Dimensiones de los nodos que se ven
		dimGrafos = ventana.dimensionesGrafosVisiblesVisualizacion();
		if (i == 0) {
			// Arbol
			Conf.setHaciendoAjuste(true);

			double propAncho = (double) dimPaneles[2] / (double) dimGrafos[2];
			double propAlto = (double) dimPaneles[3] / (double) dimGrafos[3];
			int valorNuevoAncho, valorNuevoAlto;

			// Si hay que reducir tamaño...
			if (dimGrafos[2] > dimPaneles[2] || dimGrafos[3] > dimPaneles[3]) {
				valorNuevoAncho = ((int) (propAncho * 100)) - 101;
				valorNuevoAlto = ((int) (propAlto * 100)) - 100;
			} else {
				// Si hay que ampliar tamaño...
				valorNuevoAncho = (int) ((propAncho - 1) * 100) - 2;
				valorNuevoAlto = (int) ((propAlto - 1) * 100) - 2;
			}
			barras[i].setValue(Math.min(valorNuevoAncho, valorNuevoAlto));
			Conf.setPanelArbolReajustado(true);
			peticionRefrescar(i,0);
		} else if (i == 1) {
			// Pila
			double propAncho = (double) dimPaneles[0] / (double) dimGrafos[0];
			double propAlto = (double) dimPaneles[1] / (double) dimGrafos[1];
			double porc = Math.min(propAncho, propAlto);
			int valorNuevo = 0;

			if (dimGrafos[0] > dimPaneles[0] || dimGrafos[1] > dimPaneles[1]) {
				valorNuevo = ((int) (porc * 100)) - 100;
			} else {
				valorNuevo = (int) ((porc - 1) * 100) - 2;
			}

			barras[i].setValue(valorNuevo);
			peticionRefrescar(i,0);
		} else if (i == 2)/*100?*/ {
			// Crono
			Conf.setHaciendoAjuste(true);

			double propAncho = (double) dimPaneles[4] / (double) dimGrafos[4];
			double propAlto = (double) dimPaneles[5] / (double) dimGrafos[5];
			int valorNuevoAncho, valorNuevoAlto;

			// Si hay que reducir tamaño...
			if (dimGrafos[4] > dimPaneles[4] || dimGrafos[5] > dimPaneles[5]) {
				valorNuevoAncho = ((int) (propAncho * 100)) - 101;
				valorNuevoAlto = ((int) (propAlto * 100)) - 100;
			} else {
				// Si hay que ampliar tamaño...
				valorNuevoAncho = (int) ((propAncho - 1) * 100) - 2;
				valorNuevoAlto = (int) ((propAlto - 1) * 100) - 2;
			}

			barras[i].setValue(Math.min(valorNuevoAncho, valorNuevoAlto));
			Conf.setPanelArbolReajustado(true);
			peticionRefrescar(i,0);
		} else if (i == 3) {
			// Estructura
			Conf.setHaciendoAjuste(true);

			double propAncho = (double) dimPaneles[6] / (double) dimGrafos[6];
			double propAlto = (double) dimPaneles[7] / (double) dimGrafos[7];
			int valorNuevoAncho, valorNuevoAlto;

			// Si hay que reducir tamaño...
			if (dimGrafos[6] > dimPaneles[6] || dimGrafos[7] > dimPaneles[7]) {
				valorNuevoAncho = ((int) (propAncho * 100)) - 101;
				valorNuevoAlto = ((int) (propAlto * 100)) - 100;
			} else {
				// Si hay que ampliar tamaño...
				valorNuevoAncho = (int) ((propAncho - 1) * 100) - 2;
				valorNuevoAlto = (int) ((propAlto - 1) * 100) - 2;
			}

			barras[i].setValue(Math.min(valorNuevoAncho, valorNuevoAlto));
			Conf.setPanelArbolReajustado(true);
			peticionRefrescar(i,0);
		}else if (i == 4) {
			// Grafo de dependencia
			double propAncho = (double) dimPaneles[8] / (double) dimGrafos[8];
			double propAlto = (double) dimPaneles[9] / (double) dimGrafos[9];
			double porc = Math.min(propAncho, propAlto);
			int valorNuevo = 0;

			if (dimGrafos[8] > dimPaneles[8] || dimGrafos[9] > dimPaneles[9]) {
				valorNuevo = ((int) (porc * 100)) - 100;
			} else {
				valorNuevo = (int) ((porc - 1) * 100) - 2;
			}

			barras[i].setValue(valorNuevo);
			peticionRefrescar(i,0);
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource().getClass().getCanonicalName().contains("JSlider")) {
			for (int i = 0; i < barras.length; i++) {
				if (barras[i] != null && barras[i] == e.getSource()) {
					cuadros[i].setText("" + barras[i].getValue());
				}
			}
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
			this.dialogo.setVisible(false);
		} else if (e.getSource().getClass().getCanonicalName()
				.contains("JSlider")) {
			for (int i = 0; i < barras.length; i++) {
				if (barras[i] != null && barras[i] == e.getSource()) {
					peticionRefrescar(i,0);
				}
			}
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyTyped(KeyEvent ke) {
		final KeyEvent e = ke;

		if (ke.getSource().getClass().getCanonicalName().contains("JTextField")) {
			new Thread() {
				@Override
				public void run() {
					for (int i = 0; i < cuadros.length; i++) {
						if (cuadros[i] != null && cuadros[i] == e.getSource()) {
							try {
								CuadroZoom.this.escribiendo = true;
								barras[i].setValue(Integer.parseInt(cuadros[i]
										.getText()));

								peticionRefrescar(i,0);
							} catch (Exception exc) {
							}
						}
					}
				}
			}.start();
		}
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == this.aceptar) {
			this.dialogo.setVisible(false);
		} else if (e.getSource() == this.inicializar) {
			for (int i = 0; i < barras.length; i++) {
				if (barras[i] != null) {
					barras[i].setValue(0);
					etiqPorcentaje[i].setText("0%");
					peticionRefrescar(i,0);
				}
			}
		}
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource().getClass().getName().contains("JSlider")) {
			for (int i = 0; i < barras.length; i++) {
				if (barras[i] != null && barras[i] == e.getSource()) {
					peticionRefrescar(i,0);
				}
			}
		}
		if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		}
	}

	/**
	 * Refresca el zoom para una determinada vista
	 * 
	 * @param i
	 *            Índice de la vista.
	 * @param tipo
	 *            Necesario para realizar zoom en resultado Panel crono.
	 */
	private static void peticionRefrescar(int i,int tipo) {
		ocv = (OpcionConfVisualizacion) gOpciones.getOpcion(
				"OpcionConfVisualizacion", false);

		int valor = barras[i].getValue();
		if (valor < (-99)) {
			valor = -99;
		}

		if (i == 0) {
			// Si es barra de árbol
			ventana.actualizarZoomArbol(valor);
			ocv.setZoomArbol(valor);
		} else if (i == 1) {
			// Si es barra de pila
			ventana.actualizarZoomPila(valor);
			ocv.setZoomPila(valor);
		} else if (i == 2) {
			// Si es barra de traza
			ventana.actualizarZoom(i, valor,tipo);
			ocv.setZoomCrono(valor);
		}  
		else if (i == 3) {
			// Si es barra de crono
//				ventana.actualizarZoom(i, valor);
//				ocv.setZoomCrono(valor);
			
			// Si es barra de estructura
			ventana.actualizarZoom(i, valor,tipo);
			ocv.setZoomEstructura(valor);
		} else if (i == 4) {
			// Si es barra de grafo dependencia
			ventana.actualizarZoomGrafoDep(valor);
			ocv.setZoomEstructura(valor);
		}
		
		gOpciones.setOpcion(ocv, 1);
		Conf.setValoresVisualizacion();
	}

	/**
	 * Gestiona los eventos de cambio de estado de la barra deslizante
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().getClass().getName().contains("JSlider")) {
			for (int i = 0; i < barras.length; i++) {
				if (barras[i] != null && barras[i] == e.getSource()) {
					if (!this.escribiendo) {
						final int iF = i;
						new Thread() {

							@Override
							public void run() {
								if (cuadros[iF] != null) {
									cuadros[iF].setText(barras[iF].getValue()
											+ "");
								}
							}
						}.start();
					} else {
						this.escribiendo = false;
					}
				}
			}
		}
	}
}