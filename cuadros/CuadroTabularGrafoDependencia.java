package cuadros;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import utilidades.Texto;
import ventanas.VentanaGrafoDependencia;
import botones.BotonAceptar;
import botones.BotonCancelar;
import conf.Conf;

/**
 * Implementa el cuadro que permite tabular un grafo de dependencia dada una
 * expresión para filas o columnas.
 * 
 * @author David Pastor Herranz
 */
public class CuadroTabularGrafoDependencia extends Thread implements
		ActionListener, KeyListener, MouseListener {

	private static final int ALTURA_CUADRO = 150;
	private static final int ANCHURA_CUADRO = 400;

	private VentanaGrafoDependencia ventana;
	private BotonAceptar aceptar;
	private BotonCancelar cancelar;

	private JTextField textFilas;
	private JTextField textColumnas;

	private String signaturaMetodo;

	private JPanel panel, panelBoton, panelParam;
	private JDialog dialogo;

	private String ultimaExpresionParaFila;
	private String ultimaExpresionParaColumna;

	/**
	 * Devuelve una nueva instancia del Cuadro.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 * @param signaturaMetodo
	 *            Signatura del método del grafo.
	 * @param ultimaExpresionParaFila
	 *            Última expresión que se utilizó para la fila.
	 * @param ultimaExpresionParaColumna
	 *            Última expresión que se utilizó para la columna.
	 */
	public CuadroTabularGrafoDependencia(VentanaGrafoDependencia ventana,
			String signaturaMetodo, String ultimaExpresionParaFila,
			String ultimaExpresionParaColumna) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.signaturaMetodo = signaturaMetodo;
		this.ultimaExpresionParaFila = ultimaExpresionParaFila;
		this.ultimaExpresionParaColumna = ultimaExpresionParaColumna;
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		// Panel general de parámetros
		this.panelParam = new JPanel(new GridLayout(3, 2));
		this.panelParam.setBorder(new TitledBorder(Texto.get(
				"GP_TABULAR_PANEL", Conf.idioma)));

		JLabel labelMetodo = new JLabel(Texto.get("GP_TABULAR_SIGNATURA",
				Conf.idioma));
		JLabel labelSignatura = new JLabel(this.signaturaMetodo);
		labelSignatura.setFont(labelSignatura.getFont().deriveFont(
				labelSignatura.getFont().getStyle() | Font.BOLD));

		JLabel labelFilas = new JLabel(Texto.get("GP_TABULAR_FILAS",
				Conf.idioma));
		JLabel labelColumnas = new JLabel(Texto.get("GP_TABULAR_COLUMNAS",
				Conf.idioma));
		this.textFilas = new JTextField();
		if (this.ultimaExpresionParaFila != null) {
			this.textFilas.setText(this.ultimaExpresionParaFila);
			this.textFilas.setCaretPosition(this.ultimaExpresionParaFila
					.length());
		}
		this.textFilas.addKeyListener(this);
		this.textColumnas = new JTextField();
		if (this.ultimaExpresionParaColumna != null) {
			this.textColumnas.setText(this.ultimaExpresionParaColumna);
			this.textColumnas.setCaretPosition(this.ultimaExpresionParaColumna
					.length());
		}
		this.textColumnas.addKeyListener(this);

		this.panelParam.add(labelMetodo);
		this.panelParam.add(labelSignatura);

		this.panelParam.add(labelFilas);
		this.panelParam.add(this.textFilas);
		this.panelParam.add(labelColumnas);
		this.panelParam.add(this.textColumnas);

		// Botones
		this.aceptar = new BotonAceptar();
		this.aceptar.addMouseListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar = new BotonCancelar();
		this.cancelar.addMouseListener(this);
		this.cancelar.addKeyListener(this);

		// Panel para los botones
		this.panelBoton = new JPanel();
		this.panelBoton.add(this.aceptar);
		this.panelBoton.add(this.cancelar);

		// Panel general
		this.panel = new JPanel(new BorderLayout());

		this.panel.add(this.panelParam, BorderLayout.NORTH);
		this.panel.add(this.panelBoton, BorderLayout.SOUTH);

		this.dialogo.getContentPane().add(this.panel);
		this.dialogo.setTitle(Texto.get("GP_TABULAR_TITULO", Conf.idioma));

		// Preparamos y mostramos cuadro
		this.dialogo.setSize(new Dimension(ANCHURA_CUADRO, ALTURA_CUADRO));
		int[] centroVentana = this.ubicarCentroVentana(ANCHURA_CUADRO,
				ALTURA_CUADRO);
		this.dialogo.setLocation(centroVentana[0], centroVentana[1]);
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Devuelve las coordenadas para colocar el cuadro en el centro de la
	 * ventana asociada.
	 * 
	 * @param anchura
	 * @param altura
	 * 
	 * @return Coordenadas para colocar el cuadro en el centro de la ventana
	 *         asociada.
	 */
	private int[] ubicarCentroVentana(int anchura, int altura) {
		int[] coord = new int[2];
		coord[0] = (this.ventana.getX() + this.ventana.getWidth() / 2)
				- anchura / 2;
		coord[1] = (this.ventana.getY() + this.ventana.getHeight() / 2)
				- altura / 2;
		return coord;
	}

	/**
	 * Gestiona los eventos realizados sobre los botones.
	 * 
	 * @param e
	 *            evento.
	 */
	private void gestionEventoBotones(AWTEvent e) {
		if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		} else if (e.getSource() == this.aceptar) {
			this.accionTabulado();
		}
	}

	/**
	 * Realiza las acciones correspondientes para tabular el grafo, una vez el
	 * usuario ha solicitado el tabulado dadas las expresiones.
	 */
	private void accionTabulado() {
		boolean error = false;

		if (this.textFilas.getText().length() == 0
				&& this.textColumnas.getText().length() == 0) {
			error = true;
		}

		if (error) {
			new CuadroError(this.ventana, Texto.get("GP_ERROR_TITULO",
					Conf.idioma), Texto.get("GP_ERROR_EXPR_REQ", Conf.idioma));
		} else {
			error = this.ventana.tabular(this.textFilas.getText(),
					this.textColumnas.getText());
			if (error) {
				new CuadroError(this.ventana, Texto.get("GP_ERROR_TITULO",
						Conf.idioma), Texto.get("GP_ERROR_EXPR_EVAL",
						Conf.idioma));
			} else {
				this.dialogo.setVisible(false);
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
		if ((e.getSource() instanceof JButton) && code == KeyEvent.VK_SPACE) {
			this.gestionEventoBotones(e);
		} else if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		} else if (code == KeyEvent.VK_UP) {
			if (this.aceptar.hasFocus() || this.cancelar.hasFocus()) {
				this.textColumnas.requestFocus();
			} else if (this.textColumnas.hasFocus()) {
				this.textFilas.requestFocus();
			}
		} else if (code == KeyEvent.VK_DOWN) {
			if (this.textFilas.hasFocus()) {
				this.textColumnas.requestFocus();
			} else if (this.textColumnas.hasFocus()) {
				this.aceptar.requestFocus();
			}
		} else if (code == KeyEvent.VK_RIGHT) {
			if (this.aceptar.hasFocus()) {
				this.cancelar.requestFocus();
			}
		} else if (code == KeyEvent.VK_LEFT) {
			if (this.cancelar.hasFocus()) {
				this.aceptar.requestFocus();
			}
		} else if (code == KeyEvent.VK_ENTER) {
			this.accionTabulado();
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
		if (e.getSource() instanceof JButton) {
			this.gestionEventoBotones(e);
		}
	}
}
