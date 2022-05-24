package cuadros;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import conf.*;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite configurar la visualización de las llamadas terminadas del árbol.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroElegirHistorico extends Thread implements ActionListener,
		KeyListener, MouseListener {

	private static final int NUM_OPCIONES = 3;
	private static final int ANCHO_CUADRO = 300;
	private static final int ALTO_CUADRO = (NUM_OPCIONES * 30) + 75;

	private OpcionOpsVisualizacion oov = null;
	private GestorOpciones gOpciones = new GestorOpciones();

	private ButtonGroup grupoBotonesHistoria;
	private JRadioButton botonesHistoria[];

	private JDialog dialogo;
	private Ventana ventana;

	private BotonAceptar aceptar = new BotonAceptar();
	private BotonCancelar cancelar = new BotonCancelar();

	/**
	 * Construye un nuevo cuadro que permite configurar la visualización de las
	 * llamadas terminadas del árbol.
	 * 
	 * @param Ventana
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo
	 */
	public CuadroElegirHistorico(Ventana ventana) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.start();
	}

	/**
	 * Genera una nueva opción
	 */
	@Override
	public void run() {

		this.oov = (OpcionOpsVisualizacion) this.gOpciones.getOpcion(
				"OpcionOpsVisualizacion", false);

		// Panel Datos que se muestran
		JPanel panelHistoria = new JPanel();
		GridLayout gl1 = new GridLayout(3, 1);
		panelHistoria.setLayout(gl1);

		this.botonesHistoria = new JRadioButton[3];
		this.botonesHistoria[0] = new JRadioButton(Texto.get(
				"COOV_INFONODOMANTHIST", Conf.idioma));
		this.botonesHistoria[1] = new JRadioButton(Texto.get(
				"COOV_INFONODOATENHIST", Conf.idioma));
		this.botonesHistoria[2] = new JRadioButton(Texto.get(
				"COOV_INFONODOELIMHIST", Conf.idioma));

		this.botonesHistoria[0].setToolTipText(Texto.get("COOV_INFONODOMANTHISTTTT",
				Conf.idioma));
		this.botonesHistoria[1].setToolTipText(Texto.get("COOV_INFONODOATENHISTTTT",
				Conf.idioma));
		this.botonesHistoria[2].setToolTipText(Texto.get("COOV_INFONODOELIMHISTTTT",
				Conf.idioma));

		this.grupoBotonesHistoria = new ButtonGroup();

		for (int i = 0; i < this.botonesHistoria.length; i++) {
			this.grupoBotonesHistoria.add(this.botonesHistoria[i]);
			this.botonesHistoria[i].addActionListener(this);
			this.botonesHistoria[i].addKeyListener(this);
			panelHistoria.add(this.botonesHistoria[i]);
		}

		panelHistoria.setBorder(new TitledBorder(Texto.get("COOV_INFOHIST",
				Conf.idioma)));

		// Panel para los botones
		JPanel panelBoton = new JPanel();
		panelBoton.add(this.aceptar);
		panelBoton.add(this.cancelar);

		// aceptar.addActionListener(this);
		this.aceptar.addMouseListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar.addMouseListener(this);
		this.cancelar.addKeyListener(this);

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelHistoria, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("COOV_OPSANIM", Conf.idioma));

		this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);

		this.dialogo.setResizable(false);
		setValores();
		this.dialogo.setVisible(true);
	}

	/**
	 * Obtiene el valor actual de configuración y establece como seleccionado el
	 * botón correspondiente.
	 */
	private void setValores() {
		if (this.oov.getHistoria() == OpcionOpsVisualizacion.MANTENER_HISTORIA) {
			this.botonesHistoria[0].setSelected(true);
		} else if (this.oov.getHistoria() == OpcionOpsVisualizacion.ATENUAR_HISTORIA) {
			this.botonesHistoria[1].setSelected(true);
		} else if (this.oov.getHistoria() == OpcionOpsVisualizacion.ELIMINAR_HISTORIA) {
			this.botonesHistoria[2].setSelected(true);
		}
	}

	/**
	 * Actualiza la configuración con el valor seleccionado.
	 */
	private void getValores() {
		if (this.botonesHistoria[0].isSelected()) {
			this.oov.setHistoria(OpcionOpsVisualizacion.MANTENER_HISTORIA);
		} else if (this.botonesHistoria[1].isSelected()) {
			this.oov.setHistoria(OpcionOpsVisualizacion.ATENUAR_HISTORIA);
		} else if (this.botonesHistoria[2].isSelected()) {
			this.oov.setHistoria(OpcionOpsVisualizacion.ELIMINAR_HISTORIA);
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
			getValores();
			this.gOpciones.setOpcion(this.oov, 1);

			if (Conf.fichero_log) {
				Logger.log_write("Visualización > Históricos: "
						+ this.oov.getHistoriaString());
			}

			Conf.setValoresOpsVisualizacion(false);
			Conf.setValoresVisualizacion();
			if (this.ventana.getTraza() != null) {
				this.ventana.actualizarTraza();
				this.ventana.refrescarFormato();
				Conf.setRedibujarGrafoArbol(false);
			}
		} else if (e.getSource() instanceof JButton) {
			if (e.getSource() == this.aceptar) {
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
		if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
			this.dialogo.setVisible(false);
			dialogo.dispose();
		}

		if (e.getSource().getClass().getName().contains("JRadioButton")) {
			for (int i = 0; i < this.botonesHistoria.length; i++) {
				if (this.botonesHistoria[i].isFocusOwner()) {
					if (code == KeyEvent.VK_DOWN) {
						if (i < this.botonesHistoria.length - 1) {
							this.botonesHistoria[i].transferFocus();
						} else {
							this.aceptar.requestFocus();
						}
					} else if (code == KeyEvent.VK_UP) {
						if (i > 0) {
							this.botonesHistoria[i].transferFocusBackward();
						} else {
							this.aceptar.requestFocus();
						}
					}
				}
			}
		}
		if (e.getSource() instanceof JButton) {
			if (code == KeyEvent.VK_DOWN) {
				this.botonesHistoria[0].requestFocus();
			} else if (code == KeyEvent.VK_UP) {
				this.botonesHistoria[(this.botonesHistoria.length) - 1].requestFocus();
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
		if (e.getSource() == this.aceptar) {
			this.dialogo.setVisible(false);
			dialogo.dispose();
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
		if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
			dialogo.dispose();
		}
	}
}
