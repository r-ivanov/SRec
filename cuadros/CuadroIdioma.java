package cuadros;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import conf.*;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

/**
 * Cuadro que permite modificar el idioma de SRec.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroIdioma extends Thread implements ActionListener, KeyListener {

	private static final int ANCHO_CUADRO = 275;

	private OpcionIdioma oi = null;
	private GestorOpciones gOpciones = new GestorOpciones();

	private ButtonGroup grupoBotonesIdiomas;
	private JRadioButton botonesIdiomas[];

	private JDialog dialogo;

	private BotonAceptar aceptar = new BotonAceptar();
	private BotonCancelar cancelar = new BotonCancelar();

	private Ventana ventana;

	private String idiomas[][];

	/**
	 * Genera un nuevo cuadro que permite modificar el idioma de SRec.
	 * 
	 * @param ventana
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo
	 */
	public CuadroIdioma(Ventana ventana) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		this.oi = (OpcionIdioma) this.gOpciones.getOpcion("OpcionIdioma", true);

		this.idiomas = Texto.idiomasDisponibles();

		String idiomaActual[] = new String[2];

		for (int i = 0; i < this.idiomas.length; i++) {
			if (this.idiomas[i][1].equals(this.oi.get())) {
				idiomaActual[0] = this.idiomas[i][0];
				idiomaActual[1] = this.idiomas[i][1];
			}
		}

		// Etiqueta idioma actual
		JLabel etiqIdiomaActual = new JLabel(
				Texto.get("CI_ACTUAL", Conf.idioma) + ": " + idiomaActual[0]);

		// Panel para botones de selección
		JPanel panelBotones = new JPanel();
		GridLayout gl1 = new GridLayout(this.idiomas.length, 1);
		panelBotones.setLayout(gl1);

		this.botonesIdiomas = new JRadioButton[this.idiomas.length];
		for (int i = 0; i < this.idiomas.length; i++) {
			this.botonesIdiomas[i] = new JRadioButton(this.idiomas[i][0]);
			this.botonesIdiomas[i].setToolTipText(Texto.get("CI_ACTUAL",
					Conf.idioma) + " " + this.idiomas[i][0]);
			if (this.idiomas[i][0].equals(idiomaActual[0])) {
				this.botonesIdiomas[i].setSelected(true);
			}
		}

		this.grupoBotonesIdiomas = new ButtonGroup();

		for (int i = 0; i < this.botonesIdiomas.length; i++) {
			this.grupoBotonesIdiomas.add(this.botonesIdiomas[i]);
			this.botonesIdiomas[i].addActionListener(this);
			this.botonesIdiomas[i].addKeyListener(this);
			panelBotones.add(this.botonesIdiomas[i]);
		}

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(this.aceptar);
		panelBoton.add(this.cancelar);
		this.aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar.addActionListener(this);
		this.cancelar.addKeyListener(this);

		// Panel elementos
		JPanel panelElementos = new JPanel();
		panelElementos.setLayout(new BorderLayout());

		panelElementos.add(etiqIdiomaActual, BorderLayout.NORTH);
		panelElementos.add(panelBotones, BorderLayout.CENTER);
		panelElementos.setBorder(new TitledBorder(Texto.get("CI_SELEC",
				Conf.idioma)));

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelElementos, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("CI_TITULO", Conf.idioma));

		if (this.ventana.msWindows) {
			this.dialogo
					.setSize(ANCHO_CUADRO, 120 + (this.idiomas.length * 22));
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO,
					120 + (this.idiomas.length * 22));
			this.dialogo.setLocation(coord[0], coord[1]);
		} else {
			this.dialogo
					.setSize(ANCHO_CUADRO, 130 + (this.idiomas.length * 24));
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO,
					130 + (this.idiomas.length * 24));
			this.dialogo.setLocation(coord[0], coord[1]);
		}
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Obtiene los valores establecidos por el usuario y actualiza
	 * la configuración.
	 */
	private void getValores() {
		for (int i = 0; i < this.botonesIdiomas.length; i++) {
			if (this.botonesIdiomas[i].isSelected()) {
				for (int j = 0; j < this.idiomas.length; j++) {
					if (this.botonesIdiomas[j].getText().equals(
							this.idiomas[i][0])) {
						this.oi.set(this.idiomas[i][1]);
						Conf.idioma = this.idiomas[i][1];
						if (Conf.fichero_log) {
							Logger.log_write("Idioma cambiado: "
									+ this.idiomas[i][1]);
						}
					}
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

		if (e.getSource() instanceof JRadioButton) {

		} else if (e.getSource() instanceof JButton) {
			if (e.getSource() == this.aceptar) {
				getValores();
				this.gOpciones.setOpcion(this.oi, 2);
				this.dialogo.setVisible(false);
				this.ventana.reiniciarIdioma();
				dialogo.dispose();
			} else {
				this.dialogo.setVisible(false);
				dialogo.dispose();
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
		if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
			dialogo.dispose();
		} else if (code == KeyEvent.VK_ENTER) {
			getValores();
			this.gOpciones.setOpcion(this.oi, 2);
			this.dialogo.setVisible(false);
			this.ventana.reiniciarIdioma();
			dialogo.dispose();
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
