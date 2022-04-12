package cuadros;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jgraph.JGraph;
import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import conf.*;
import datos.MetodoAlgoritmo;
import botones.*;
import utilidades.*;
import paneles.*;
import ventanas.*;

/**
 * Genera el cuadro de diálogo que permite exportar el contenido gráfico de una
 * vista
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroVistasDisponibles extends Thread implements ActionListener,
		KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 275;
	private static final int ALTO_CUADRO = 90;
	private static final int ANCHO_CUADRO_NO_WINDOWS = 290;
	private static final int ALTO_CUADRO_NO_WINDOWS = 404;

	private PanelVentana pv;
	private String[] vistasDisponibles;

	private Ventana ventana;
	private JDialog dialogo;

	private BotonAceptar aceptar = new BotonAceptar();
	private BotonCancelar cancelar = new BotonCancelar();

	private int tipoExportacion = 0;
	private JRadioButton botonesVistas[];
	private ButtonGroup grupoBotones;

	private JGraph grafo;
	private int numeroVista;
	
	private boolean vistaValoresGlob = false;
	private boolean vistaValoresRama = false;
	private PanelValoresGlobalesAABB panelValGlob;
	private PanelValoresRamaAABB panelValRama;

	/**
	 * Constructor: genera un nuevo cuadro de opción que permite exportar el
	 * contenido gráfico de una vista.
	 * 
	 * @param ventana
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo.
	 * @param pv
	 *            PanelVentana que contiene las distintas visualizaciones del
	 *            árbol.
	 * @param tipoExportacion
	 *            (1 - animación gif, 2 - de un paso, 3 - captura única).
	 */
	public CuadroVistasDisponibles(Ventana ventana, PanelVentana pv,
			int tipoExportacion) {
		this.pv = pv;
		this.tipoExportacion = tipoExportacion;
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.start();
	}

	/**
	 * Genera una nueva opción
	 */

	@Override
	public void run() {
		this.vistasDisponibles = this.pv.getNombreVistasDisponibles();

		// Panel Datos que se muestran
		JPanel panelVistas = new JPanel();
		GridLayout gl1 = new GridLayout(this.vistasDisponibles.length, 1);
		panelVistas.setLayout(gl1);

		this.grupoBotones = new ButtonGroup();

		this.botonesVistas = new JRadioButton[this.vistasDisponibles.length];
		for (int i = 0; i < this.vistasDisponibles.length; i++) {
			this.botonesVistas[i] = new JRadioButton(this.vistasDisponibles[i]);
			this.botonesVistas[i].setToolTipText(Texto.get("CVD_MARCAR",
					Conf.idioma));
			this.botonesVistas[i].addKeyListener(this);
			if ((vistasDisponibles[i].equals(Texto.get("V_TRAZA", Conf.idioma))) && (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
							this.ventana.getTraza().getTecnicas()))) {
				this.botonesVistas[i].setEnabled(false);
			}
			if(this.vistasDisponibles[i].equals(Texto.get("V_GRAFO_DEP", Conf.idioma)) && this.tipoExportacion!=3){
				this.botonesVistas[i].setEnabled(false);
			}
			this.grupoBotones.add(this.botonesVistas[i]);
			panelVistas.add(this.botonesVistas[i]);
		}
		for(int i=0;i<this.botonesVistas.length;i++){
			if(this.botonesVistas[i].isEnabled()){
				this.botonesVistas[i].setSelected(true);
				break;
			}
		}

//		if ((this.vistasDisponibles.length >= 4) && (this.tipoExportacion < 3)) {
//			this.botonesVistas[3].setEnabled(false);
//		}

		panelVistas.setBorder(new TitledBorder(Texto.get("CVD_SELECVISTA",
				Conf.idioma)));

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(this.aceptar);
		panelBoton.add(this.cancelar);

		this.aceptar.addMouseListener(this);
		this.aceptar.addActionListener(this);
		this.cancelar.addMouseListener(this);
		this.cancelar.addActionListener(this);

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelVistas, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro

		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("CVD_VISTASDIS", Conf.idioma));

		if (Ventana.thisventana.msWindows) {
			this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO
					+ (24 * this.vistasDisponibles.length));
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
			this.dialogo.setLocation(coord[0], coord[1]);
		} else {
			this.dialogo.setSize(ANCHO_CUADRO_NO_WINDOWS,
					ALTO_CUADRO_NO_WINDOWS);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO_NO_WINDOWS,
					ALTO_CUADRO_NO_WINDOWS);
			this.dialogo.setLocation(coord[0], coord[1]);
		}
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Obtiene la vista que está seleccionada y la establece como vista activa.
	 */
	private void getValores() {
		for (int i = 0; i < this.botonesVistas.length; i++) {
			if (this.botonesVistas[i].isSelected()) {
				String nombreVista = this.botonesVistas[i].getText();
                this.pv.setVistaActiva(nombreVista);

                // En caso de que la vista no sea un grafo
                String vistaGlobalVal = Texto.get("V_GLOBAL_VAL", Conf.idioma);
                String vistaRamaVal = Texto.get("V_RAMA_VAL", Conf.idioma);
            	if(nombreVista.equalsIgnoreCase(vistaGlobalVal)) {
            		panelValGlob = (PanelValoresGlobalesAABB) pv.getPanelAlgoritmo().getPanelPorNombre(nombreVista);
            		vistaValoresGlob = true;
            	}else if(nombreVista.equalsIgnoreCase(vistaRamaVal)){
            		panelValRama = (PanelValoresRamaAABB) pv.getPanelAlgoritmo().getPanelPorNombre(nombreVista);
            		vistaValoresRama = true;
            	}else {
                	this.grafo = (JGraph) this.pv.getGrafoPorNombre(nombreVista);
                }
                
				this.numeroVista = i;				
			}
		}
	}

	/**
	 * Hace la captura según las opciones seleccionadas.
	 */
	private void accion() {
		getValores();
		dialogo.setVisible(false);
		if (this.tipoExportacion == 1) {
			if (Conf.fichero_log) {
				Logger.log_write("Guardar GIF: capturarAnimacionGIF");
			}
			if(vistaValoresGlob) {
				new FotografoArbol().capturarAnimacionGIF(panelValGlob, numeroVista);
			}else if(vistaValoresRama) {
				new FotografoArbol().capturarAnimacionGIF(panelValRama, numeroVista);
			}else {
				new FotografoArbol().capturarAnimacionGIF(grafo, numeroVista);
			}
		} else if (this.tipoExportacion == 2) {
			if (Conf.fichero_log) {
				Logger.log_write("Guardar GIF: hacerCapturasPaso");
			}
			if(vistaValoresGlob) {
				//new FotografoArbol().hacerCapturasPaso(panelValGlob, numeroVista);
			}else if(vistaValoresRama) {
				//new FotografoArbol().hacerCapturasPaso(panelValRama, numeroVista);
			}else {
				new FotografoArbol().hacerCapturasPaso(grafo, numeroVista);
			}
		} else if (this.tipoExportacion == 3) {
			if (Conf.fichero_log) {
				Logger.log_write("Guardar GIF: hacerCapturaUnica");
			}
			if(vistaValoresGlob) {
				new FotografoArbol().hacerCapturaUnica(panelValGlob, numeroVista);
			}else if(vistaValoresRama) {
				new FotografoArbol().hacerCapturaUnica(panelValRama, numeroVista);
			}else {
				new FotografoArbol().hacerCapturaUnica(grafo, numeroVista);
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

	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyPressed(KeyEvent e) {
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
		if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_UP) {
			if (e.getSource().getClass().getName().contains("JRadioButton")) {
				for (int i = 0; i < this.botonesVistas.length; i++) {
					if (this.botonesVistas[i].isFocusOwner()) {
						if (code == KeyEvent.VK_DOWN) {
							if (i < this.botonesVistas.length - 1) {
								this.botonesVistas[i].transferFocus();
							}
						} else if (code == KeyEvent.VK_UP) {
							if (i > 0) {
								this.botonesVistas[i].transferFocusBackward();
							}
						}
					}
				}
			}
		} else if (code == KeyEvent.VK_ENTER) {
			accion();
		} else if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyTyped(KeyEvent e) {

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

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == this.aceptar) {
			accion();
		}
		if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		}
	}
}
