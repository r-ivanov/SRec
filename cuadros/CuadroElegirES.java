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
		dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.start();
	}

	/**
	 * Ejecuta el thread.
	 */
	public void run() {
		oov = (OpcionOpsVisualizacion) gOpciones.getOpcion(
				"OpcionOpsVisualizacion", false);

		// Panel Datos que se muestran
		JPanel panelDatos = new JPanel();
		GridLayout gl1 = new GridLayout(3, 1);
		panelDatos.setLayout(gl1);

		botonesDatos = new JRadioButton[3];
		botonesDatos[0] = new JRadioButton(Texto.get("COOV_ENTRADA",
				Conf.idioma));
		botonesDatos[1] = new JRadioButton(
				Texto.get("COOV_SALIDA", Conf.idioma));
		botonesDatos[2] = new JRadioButton(Texto.get("COOV_ENTRSAL",
				Conf.idioma));

		botonesDatos[0].setToolTipText(Texto
				.get("COOV_ENTRADATTT", Conf.idioma));
		botonesDatos[1]
				.setToolTipText(Texto.get("COOV_SALIDATTT", Conf.idioma));
		botonesDatos[2].setToolTipText(Texto
				.get("COOV_ENTRSALTTT", Conf.idioma));

		grupoBotonesDatos = new ButtonGroup();

		for (int i = 0; i < botonesDatos.length; i++) {
			grupoBotonesDatos.add(botonesDatos[i]);
			botonesDatos[i].addActionListener(this);
			botonesDatos[i].addKeyListener(this);
			panelDatos.add(botonesDatos[i]);
		}

		panelDatos.setBorder(new TitledBorder(Texto.get("COOV_INFONODO",
				Conf.idioma)));

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(aceptar);
		panelBoton.add(cancelar);

		aceptar.addMouseListener(this);
		aceptar.addKeyListener(this);
		cancelar.addMouseListener(this);
		cancelar.addKeyListener(this);

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelDatos, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("COOV_OPSANIM", Conf.idioma));

		dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		dialogo.setLocation(coord[0], coord[1]);

		dialogo.setResizable(false);
		setValores();
		dialogo.setVisible(true);
	}

	/**
	 * Establece el valor seleccionado segun la opción de visualización actual.
	 */
	private void setValores() {
		if (oov.getDatosMostrar() == OpcionOpsVisualizacion.DATOS_ENTRADA) {
			botonesDatos[0].setSelected(true);
		} else if (oov.getDatosMostrar() == OpcionOpsVisualizacion.DATOS_SALIDA) {
			botonesDatos[1].setSelected(true);
		} else if (oov.getDatosMostrar() == OpcionOpsVisualizacion.DATOS_TODOS) {
			botonesDatos[2].setSelected(true);
		}
	}

	/**
	 * Una vez seleccionado el botón correspondiente, actualiza la configuración
	 * con el valor seleccionado.
	 */
	private void getValores() {
		if (botonesDatos[0].isSelected()) {
			oov.setDatosMostrar(OpcionOpsVisualizacion.DATOS_ENTRADA);
		} else if (botonesDatos[1].isSelected()) {
			oov.setDatosMostrar(OpcionOpsVisualizacion.DATOS_SALIDA);
		} else if (botonesDatos[2].isSelected()) {
			oov.setDatosMostrar(OpcionOpsVisualizacion.DATOS_TODOS);
		}
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JRadioButton) {
			getValores();
			gOpciones.setOpcion(oov, 1);

			if (Conf.fichero_log) {
				Logger.log_write("Visualización > Entrada y salida: E="
						+ oov.mostrarEntrada() + " S=" + oov.mostrarSalida());
			}

			Conf.setValoresOpsVisualizacion(false);
			Conf.setValoresVisualizacion();
			if (ventana.getTraza() != null) {
				ventana.actualizarTraza();
				ventana.refrescarFormato();
				Conf.setRedibujarGrafoArbol(false);
			}
		} else if (e.getSource() instanceof JButton) {
			if (e.getSource() == aceptar) {
				dialogo.setVisible(false);
			}
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	public void keyPressed(KeyEvent e) {

	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
			dialogo.setVisible(false);
		}

		if (e.getSource().getClass().getName().contains("JRadioButton")) {
			for (int i = 0; i < botonesDatos.length; i++) {
				if (botonesDatos[i].isFocusOwner()) {
					if (code == KeyEvent.VK_DOWN) {
						if (i < botonesDatos.length - 1) {
							botonesDatos[i].transferFocus();
						} else {
							aceptar.requestFocus();
						}
					} else if (code == KeyEvent.VK_UP) {
						if (i > 0) {
							botonesDatos[i].transferFocusBackward();
						} else {
							aceptar.requestFocus();
						}
					}
				}
			}
		}

		if (e.getSource() instanceof JButton) {
			if (code == KeyEvent.VK_DOWN) {
				botonesDatos[0].requestFocus();
			} else if (code == KeyEvent.VK_UP) {
				botonesDatos[(botonesDatos.length) - 1].requestFocus();
			}
		}

	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == aceptar) {
			this.dialogo.setVisible(false);
		}
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == cancelar) {
			this.dialogo.setVisible(false);
		}
	}
}
