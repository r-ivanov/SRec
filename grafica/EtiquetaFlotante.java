package grafica;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;

/**
 * Etiqueta flotante que permite representar información básica de un nodo.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class EtiquetaFlotante extends Thread implements MouseListener,
MouseMotionListener {
	static final long serialVersionUID = 82;

	private static boolean hayEtiquetaActiva = false;

	private int x;
	private int y;
	private int ancho;
	private int alto;

	private int ratonX = 0, ratonY = 0, ratonXAnt = 0, ratonYAnt = 0;

	private String nombre;
	private String entrada;
	private String salida;

	private JLabel etiqNombre, etiqEntrada, etiqSalida;
	private static JDialog dialogo = null;

	/**
	 * Crea una nueva Etiqueta flotante que permite representar información
	 * básica de un nodo.
	 * 
	 * @param x
	 *            Posición x de la etiqueta.
	 * @param y
	 *            Posición y de la etiqueta.
	 * @param nombre
	 *            Nombre para visualizar.
	 * @param entrada
	 *            Representación de entrada.
	 * @param salida
	 *            Representación de salida.
	 * @param entradaVisible
	 *            Si la entrada es visible.
	 * @param salidaVisible
	 *            Si la salida es visible.
	 */
	public EtiquetaFlotante(int x, int y, String nombre, String entrada,
			String salida, boolean entradaVisible, boolean salidaVisible) {
		if (!hayEtiquetaActiva) {
			hayEtiquetaActiva = true;
			if (dialogo != null) {
				dialogo.setVisible(false);
			}
			dialogo = new JDialog(Ventana.thisventana, false);

			this.x = x;
			this.y = y;
			this.nombre = " " + nombre;

			if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA
					|| Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {
				if (entradaVisible) {
					this.entrada = " " + Texto.get("ETIQFL_ENTR", Conf.idioma)
							+ ": " + entrada;
				} else {
					this.entrada = " " + Texto.get("ETIQFL_ENTR", Conf.idioma)
							+ ": --------";
				}
			} else {
				this.entrada = " ";
			}

			if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA
					|| Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {
				if (salidaVisible) {
					this.salida = " " + Texto.get("ETIQFL_SALI", Conf.idioma)
							+ ": " + salida;
				} else {
					this.salida = " " + Texto.get("ETIQFL_SALI", Conf.idioma)
							+ ": --------";
				}
			} else {
				this.salida = " ";
			}

			this.start();
		}
	}

	/**
	 * Permite al thread ejecutar el código en otro hilo.
	 */
	@Override
	public synchronized void run() {
		// Etiquetas
		this.etiqNombre = new JLabel(this.nombre);

		this.etiqEntrada = new JLabel(this.entrada);
		this.etiqSalida = new JLabel(this.salida);

		// Panel que las contendrá
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		panel.add(this.etiqNombre, BorderLayout.NORTH);
		panel.add(this.etiqEntrada, BorderLayout.CENTER);
		panel.add(this.etiqSalida, BorderLayout.SOUTH);

		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);

		this.etiqNombre.setFont(new Font(this.etiqNombre.getFont().getName(),
				Font.BOLD, this.etiqNombre.getFont().getSize()));

		dialogo.getContentPane().add(panel);
		dialogo.setUndecorated(true);

		this.ancho = Math.max(this.nombre.length(),
				Math.max(this.entrada.length(), this.salida.length())) * 6;
		this.alto = 40;
		dialogo.setSize(this.ancho, this.alto);

		int margenMinimo = 12;

		// Ubicamos la etiqueta centrada sobre el ratón
		this.x = this.x - (this.ancho / 2);
		this.y = this.y - (this.alto / 2);

		// Comprobamos que no se salga por algún lateral
		if (this.x < margenMinimo) {
			this.x = margenMinimo;
		}

		if (this.x + this.ancho > Ventana.thisventana.tamPantalla[0]) {
			this.x = this.x
					- (this.x + this.ancho + margenMinimo - Ventana.thisventana.tamPantalla[0]);
		}

		dialogo.setLocation(this.x, this.y);

		dialogo.setVisible(true);
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
		dialogo.setVisible(false);
		hayEtiquetaActiva = false;
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
		dialogo.setVisible(false);
	}

	/**
	 * Gestiona los eventos de movimiento de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
	}

	/**
	 * Gestiona los eventos de movimiento de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseMoved(MouseEvent e) {

		this.ratonXAnt = this.ratonX;
		this.ratonYAnt = this.ratonY;
		this.ratonX = (e.getXOnScreen());
		this.ratonY = (e.getYOnScreen());

		if (this.ratonX != this.ratonXAnt || this.ratonY != this.ratonYAnt) {
			if ((this.ratonX < this.x || this.ratonX > this.x + this.ancho)
					|| (this.ratonY < this.y || this.ratonY > this.y
							+ this.alto)) // Si raton está fuera
			{
				dialogo.setVisible(false);
				hayEtiquetaActiva = false;
			}
		}
	}
}
