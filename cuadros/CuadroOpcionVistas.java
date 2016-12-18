package cuadros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import opciones.GestorOpciones;
import opciones.OpcionVistas;
import opciones.Vista;
import paneles.PanelAlgoritmo;
import utilidades.Arrays;
import utilidades.Texto;
import ventanas.Ventana;
import botones.BotonAceptar;
import botones.BotonCancelar;
import conf.Conf;
import datos.FamiliaEjecuciones;
import datos.MetodoAlgoritmo;

/**
 * Permite configurar la disposición de las vistas de visualización de la
 * ejecución.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroOpcionVistas extends Thread implements ActionListener,
KeyListener {
	static final long serialVersionUID = 07;

	private static final int ANCHO_CUADRO = 300;
	private static final int ALTO_CUADRO = 290;
	private static final int ANCHO_CUADRO_NO_WINDOWS = 290;
	private static final int ALTO_CUADRO_NO_WINDOWS = 404;

	private OpcionVistas ov = null;
	private GestorOpciones gOpciones = new GestorOpciones();

	private JRadioButton selec1[];
	private JRadioButton selec2[];
	private ButtonGroup grupoSelec[];

	private JComboBox<String> selecPanel;

	private final Ventana ventana;
	private JDialog dialogo;

	private BotonAceptar aceptar = new BotonAceptar();
	private BotonCancelar cancelar = new BotonCancelar();

	/**
	 * Genera un nuevo cuadro que permite configurar la disposición de las
	 * vistas de visualización de la ejecución.
	 * 
	 * @param ventanaVisualizador
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo
	 */
	public CuadroOpcionVistas(Ventana ventana) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.start();
	}

	/**
	 * Genera una nueva opción
	 */

	@Override
	public void run() {
		this.ov = (OpcionVistas) this.gOpciones
				.getOpcion("OpcionVistas", false);

		// Panel para las vistas
		JPanel panelVistas = new JPanel();
		GridLayout gl1 = new GridLayout(6, 1);
		panelVistas.setLayout(gl1);
		panelVistas.setBorder(new TitledBorder(Texto.get("CVI_VISTASPROP",
				Conf.idioma)));

		this.selec1 = new JRadioButton[Vista.codigos.length];
		this.selec2 = new JRadioButton[Vista.codigos.length];
		this.grupoSelec = new ButtonGroup[Vista.codigos.length];

		for (int i = 0; i < Vista.codigos.length; i++) {
			JPanel panelVista = new JPanel();
			panelVista.setLayout(new BorderLayout());
			panelVista.setPreferredSize(new Dimension(ANCHO_CUADRO - 10, 23));

			JLabel etiq = new JLabel(Texto.get(Vista.codigos[i], Conf.idioma));

			this.selec1[i] = new JRadioButton("1");
			this.selec2[i] = new JRadioButton("2");
			this.selec1[i].addActionListener(this);
			this.selec2[i].addActionListener(this);
			this.grupoSelec[i] = new ButtonGroup();
			this.grupoSelec[i].add(this.selec1[i]);
			this.grupoSelec[i].add(this.selec2[i]);

			this.selec1[i].setFocusable(false);
			this.selec2[i].setFocusable(false);

			if ((this.ov.getVista(Vista.codigos[i]).getPanel()) == 1) {
				this.selec1[i].setSelected(true);
			} else {
				this.selec2[i].setSelected(true);
			}		

			JPanel panelRadioB = new JPanel();
			panelRadioB.add(this.selec1[i]);
			panelRadioB.add(this.selec2[i]);

			panelVista.add(etiq, BorderLayout.WEST);
			panelVista.add(panelRadioB, BorderLayout.EAST);

			panelVistas.add(panelVista);
		}

		this.actualizarInterfazRadioButton();

		if (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV, this.ventana
				.getTraza().getTecnicas())) {
			this.selec1[3].setEnabled(false);
			this.selec2[3].setEnabled(false);
		}

		// Panel para la disposición de los paneles
		JPanel panelPaneles = new JPanel();
		panelPaneles.setLayout(new BorderLayout());
		panelPaneles.setBorder(new TitledBorder(Texto.get("CVI_DISPVISTAS",
				Conf.idioma)));

		JLabel etiq = new JLabel(Texto.get("CVI_PANELES", Conf.idioma));
		this.selecPanel = new JComboBox<String>();
		this.selecPanel.addItem(Texto.get("VERTICAL", Conf.idioma));
		this.selecPanel.addItem(Texto.get("HORIZONTAL", Conf.idioma));
		this.selecPanel.setSelectedIndex(this.ov.getDisposicion() - 1);
		this.selecPanel.addActionListener(this);
		this.selecPanel.setFocusable(false);

		panelPaneles.add(etiq, BorderLayout.WEST);
		panelPaneles.add(this.selecPanel, BorderLayout.EAST);

		// Panel que recoge los dos paneles anteriores
		JPanel panelContenedor = new JPanel();
		panelContenedor.setLayout(new BorderLayout());

		panelContenedor.add(panelVistas, BorderLayout.NORTH);
		panelContenedor.add(panelPaneles, BorderLayout.SOUTH);

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(this.aceptar);
		panelBoton.add(this.cancelar);
		this.aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar.addActionListener(this);
		this.cancelar.addKeyListener(this);

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelContenedor, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("CPAN_DISPOS", Conf.idioma));

		if (this.ventana.msWindows) {
			this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
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

		//	Si no han pulsado previamente el botón de generar grafo
		//	no se activan sus radio buttons
		if(!PanelAlgoritmo.getGrafoActivado()){
			this.selec1[4].setEnabled(false);
			this.selec2[4].setEnabled(false);
		}else{
			this.selec1[4].setEnabled(true);
			this.selec2[4].setEnabled(true);
		}	
		
		this.dialogo.setVisible(true);	
	}

	/**
	 * Recoge los valores establecidos por el usuario y actualiza la
	 * configuración.
	 */
	private void getValores() {
		for (int i = 0; i < Vista.codigos.length; i++) {
			Vista v = this.ov.getVista(Vista.codigos[i]);
			v.setPanel(this.selec1[i].isSelected() ? 1 : 2);
			this.ov.actualizarVista(v);
		}
	}

	/**
	 * Habilita/Deshabilita los radio buttons del cuadro según las opciones que
	 * se van seleccionando.
	 */
	private void actualizarInterfazRadioButton() {
		int numVistas = Vista.codigos.length;

		/*
		 * Deshabilitamos la configuración de disposición de vistas cuando la
		 * Familia de ejecuciones está activa
		 */
		if (FamiliaEjecuciones.getInstance().estaHabilitado()) {
			for (int i = 0; i < numVistas; i++) {
				this.selec1[i].setEnabled(false);
				this.selec2[i].setEnabled(false);
			}
			return;
		}

		if (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV, this.ventana
				.getTraza().getTecnicas())) {
			// En rec tenemos 3 vistas
			numVistas = 3;
		}

		// Interfaz para asegurar que siempre habrá una vista como mínimo en
		// cada panel
		for (int i = 0; i < numVistas; i++) {
			this.selec1[i].setEnabled(true);
			this.selec2[i].setEnabled(true);
		}

		// Detectamos si ha quedado sólo una vista en el panel 1
		int contador = 0;
		for (int i = 0; i < numVistas; i++) {
			if (this.selec1[i].isSelected()) {
				contador++;
			}
		}

		if (contador == 1) {
			for (int i = 0; i < numVistas; i++) {
				if (this.selec1[i].isSelected()) {
					this.selec1[i].setEnabled(false);
					this.selec2[i].setEnabled(false);
				}
			}
		}

		// Detectamos si ha quedado sólo una vista en el panel 2
		contador = 0;
		for (int i = 0; i < numVistas; i++) {
			if (this.selec2[i].isSelected()) {
				contador++;
			}
		}

		if (contador == 1) {
			for (int i = 0; i < numVistas; i++) {
				if (this.selec2[i].isSelected()) {
					this.selec1[i].setEnabled(false);
					this.selec2[i].setEnabled(false);
				}
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
		this.getValores();

		if (e.getSource() == this.aceptar) {
			this.dialogo.setVisible(false);
		}
		if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		} else if (e.getSource() instanceof JRadioButton) {			
			this.actualizarInterfazRadioButton();

			final boolean[] panel1activos = new boolean[Vista.codigos.length];
			for (int i = 0; i < Vista.codigos.length; i++) {
				panel1activos[i] = this.selec1[i].isSelected();
			}
			
			new Thread() {
				@Override
				public void run() {					
					SwingUtilities.invokeLater(new Runnable() {							
						@Override
						public void run() {							
							CuadroOpcionVistas.this.ventana
							.ubicarYDistribuirPaneles((CuadroOpcionVistas.this.selecPanel
									.getSelectedIndex() == 0 ? Conf.PANEL_VERTICAL
											: Conf.PANEL_HORIZONTAL));
						}
					});
				}
			}.start();			

			this.gOpciones.setOpcion(this.ov, 2);
			Conf.setConfiguracionVistas();
			
		} else if (e.getSource() instanceof JComboBox) {
			if (this.selecPanel.getSelectedIndex() == 0) {
				this.ov.setDisposicion(Conf.PANEL_VERTICAL);
			} else {
				this.ov.setDisposicion(Conf.PANEL_HORIZONTAL);
			}
			this.ventana
			.distribuirPaneles((this.selecPanel.getSelectedIndex() == 0 ? Conf.PANEL_VERTICAL
					: Conf.PANEL_HORIZONTAL));

			this.gOpciones.setOpcion(this.ov, 2);
			Conf.setConfiguracionVistas();

			if (FamiliaEjecuciones.getInstance().estaHabilitado()) {
				FamiliaEjecuciones.getInstance().recargarEjecucionActiva();
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
		if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
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

}