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
 * Permite seleccionar si la vista del árbol debe mostrar entradas, salidas, o
 * ambas.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroElegirES extends Thread implements ActionListener,
		KeyListener, MouseListener {

	private static final int NUM_OPCIONES = 3;

	private static final int ANCHO_CUADRO = 300;
	private static final int ALTO_CUADRO = (NUM_OPCIONES * 30) + 75;

	private OpcionOpsVisualizacion oov = null;
	private GestorOpciones gOpciones = new GestorOpciones();

	private ButtonGroup grupoBotonesDatos;
	private JRadioButton botonesDatos[];

	private JDialog dialogo;

	private BotonAceptar aceptar = new BotonAceptar();
	private BotonCancelar cancelar = new BotonCancelar();

	private Ventana ventana;

	/**
	 * Genera un nuevo cuadro que permite seleccionar si la vista del árbol debe
	 * mostrar entradas, salidas, o ambas.
	 * 
	 * @param ventanaVisualizador
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo.
	 */
	public CuadroElegirES(Ventana ventana) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.start();
	}

	/**
	 * Ejecuta el thread.
	 */
	@Override
	public void run() {
		this.oov = (OpcionOpsVisualizacion) this.gOpciones.getOpcion(
				"OpcionOpsVisualizacion", false);

		// Panel Datos que se muestran
		JPanel panelDatos = new JPanel();
		GridLayout gl1 = new GridLayout(3, 1);
		panelDatos.setLayout(gl1);

		this.botonesDatos = new JRadioButton[3];
		this.botonesDatos[0] = new JRadioButton(Texto.get("COOV_ENTRADA",
				Conf.idioma));
		this.botonesDatos[1] = new JRadioButton(Texto.get("COOV_SALIDA",
				Conf.idioma));
		this.botonesDatos[2] = new JRadioButton(Texto.get("COOV_ENTRSAL",
				Conf.idioma));

		this.botonesDatos[0].setToolTipText(Texto.get("COOV_ENTRADATTT",
				Conf.idioma));
		this.botonesDatos[1].setToolTipText(Texto.get("COOV_SALIDATTT",
				Conf.idioma));
		this.botonesDatos[2].setToolTipText(Texto.get("COOV_ENTRSALTTT",
				Conf.idioma));

		this.grupoBotonesDatos = new ButtonGroup();

		for (int i = 0; i < this.botonesDatos.length; i++) {
			this.grupoBotonesDatos.add(this.botonesDatos[i]);
			this.botonesDatos[i].addActionListener(this);
			this.botonesDatos[i].addKeyListener(this);
			panelDatos.add(this.botonesDatos[i]);
		}

		panelDatos.setBorder(new TitledBorder(Texto.get("COOV_INFONODO",
				Conf.idioma)));

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(this.aceptar);
		panelBoton.add(this.cancelar);

		this.aceptar.addMouseListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar.addMouseListener(this);
		this.cancelar.addKeyListener(this);

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelDatos, BorderLayout.NORTH);
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
	 * Establece el valor seleccionado segun la opción de visualización actual.
	 */
	private void setValores() {
		if (this.oov.getDatosMostrar() == OpcionOpsVisualizacion.DATOS_ENTRADA) {
			this.botonesDatos[0].setSelected(true);
		} else if (this.oov.getDatosMostrar() == OpcionOpsVisualizacion.DATOS_SALIDA) {
			this.botonesDatos[1].setSelected(true);
		} else if (this.oov.getDatosMostrar() == OpcionOpsVisualizacion.DATOS_TODOS) {
			this.botonesDatos[2].setSelected(true);
		}
	}

	/**
	 * Una vez seleccionado el botón correspondiente, actualiza la configuración
	 * con el valor seleccionado.
	 */
	private void getValores() {
		if (this.botonesDatos[0].isSelected()) {
			this.oov.setDatosMostrar(OpcionOpsVisualizacion.DATOS_ENTRADA);
		} else if (this.botonesDatos[1].isSelected()) {
			this.oov.setDatosMostrar(OpcionOpsVisualizacion.DATOS_SALIDA);
		} else if (this.botonesDatos[2].isSelected()) {
			this.oov.setDatosMostrar(OpcionOpsVisualizacion.DATOS_TODOS);
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
				Logger.log_write("Visualización > Entrada y salida: E="
						+ this.oov.mostrarEntrada() + " S="
						+ this.oov.mostrarSalida());
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
			for (int i = 0; i < this.botonesDatos.length; i++) {
				if (this.botonesDatos[i].isFocusOwner()) {
					if (code == KeyEvent.VK_DOWN) {
						if (i < this.botonesDatos.length - 1) {
							this.botonesDatos[i].transferFocus();
						} else {
							this.aceptar.requestFocus();
						}
					} else if (code == KeyEvent.VK_UP) {
						if (i > 0) {
							this.botonesDatos[i].transferFocusBackward();
						} else {
							this.aceptar.requestFocus();
						}
					}
				}
			}
		}

		if (e.getSource() instanceof JButton) {
			if (code == KeyEvent.VK_DOWN) {
				this.botonesDatos[0].requestFocus();
			} else if (code == KeyEvent.VK_UP) {
				this.botonesDatos[(this.botonesDatos.length) - 1]
						.requestFocus();
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
