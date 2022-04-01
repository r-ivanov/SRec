package cuadros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.border.TitledBorder;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utilidades.Logger;
import utilidades.Texto;
import ventanas.Ventana;
import botones.BotonAceptar;
import conf.Conf;
import datos.*;

/**
 * Permite construir cuadros de información sobre un determinado nodo del árbol.
 */
public class CuadroInfoNodo extends Thread implements ActionListener,
		KeyListener {

	private static final int ANCHO_COLUMNA_ANCHA = 350;
	private static final int ALTO_FILA = 15;
	private static final int FILAS_INFO = 8;

	private static final int ANCHO_CUADRO_NO_WINDOWS = 290; // No Windows
	private static final int ALTO_CUADRO_NO_WINDOWS = 404; // No Windows

	private Ventana ventana;
	private JDialog dialogo;
	private RegistroActivacion nodo;
	private Traza traza;

	private BotonAceptar aceptar = new BotonAceptar();

	/**
	 * Genera un cuadro de información sobre el nodo suministrado.
	 * 
	 * @param ventana
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo.
	 * @param nodo
	 *            Nodo para el que se solicita información.
	 */
	public CuadroInfoNodo(Ventana ventana, RegistroActivacion nodo) { 
		this.nodo = nodo;
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		traza = ventana.traza;
		this.start();
	}

	/**
	 * Genera un cuadro de información sobre el nodo activo.
	 * 
	 * @param ventana
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo
	 * @param traza
	 *            Traza de ejecución
	 */
	public CuadroInfoNodo(Ventana ventana, Traza traza) {
		this.nodo = traza.getRegistroActivo();
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		traza = ventana.traza;
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		// Tamaño del cuadro de diálogo
		int anchoCuadro = 0;
		int altoCuadro = 0;

		// Panel Info general
		JPanel panelInfoGral = new JPanel();
		GridLayout gl1 = new GridLayout(FILAS_INFO, 1);
		panelInfoGral.setLayout(gl1);

		// Etiquetas
		JLabel infoGral1[] = new JLabel[FILAS_INFO];

		String estado = ((this.nodo.esMostradoEntero()) ? Texto.get(
				"CINFONODO_EJEC", Conf.idioma) : Texto.get("CINFONODO_PEND",
				Conf.idioma));
		estado = estado
				+ ((this.nodo.esHistorico()) ? " ("
						+ Texto.get("CINFONODO_HIST", Conf.idioma) + ")" : "");

		infoGral1[0] = new JLabel(Texto.get("CINFONODO_EST", Conf.idioma)
				+ ": " + estado);
		infoGral1[1] = new JLabel(Texto.get("CINFONODO_MET", Conf.idioma)
				+ ": " + this.nodo.interfazMetodo());

		estado = "";
		if (this.nodo.numHijos() > 0) {
			if (this.nodo.getHijo(0).inhibido()) {
				estado = " (" + Texto.get("CINFONODO_INHIS", Conf.idioma) + ")";
			}
		}

		infoGral1[2] = new JLabel(Texto.get("CINFONODO_SUBNUM", Conf.idioma)
				+ ": " + (this.nodo.getID() + 1));
		infoGral1[3] = new JLabel(Texto.get("CINFONODO_SUBNUM_VIS", Conf.idioma)
				+ ": " + (traza.getNumNodosVisibles(this.nodo))); 
		infoGral1[4] = new JLabel(Texto.get("CINFONODO_INHI", Conf.idioma)
				+ ": "
				+ (this.nodo.inhibido() ? Texto.get("SI", Conf.idioma)
						: Texto.get("NO", Conf.idioma)));

		infoGral1[5] = new JLabel(Texto.get("CINFONODO_HIJ", Conf.idioma)
				+ ": " + this.nodo.numHijos() + estado);
		infoGral1[6] = new JLabel(Texto.get("CINFONODO_HIJTV", Conf.idioma)
				+ ": " + this.nodo.getNumHijosVisibles());
		infoGral1[7] = new JLabel(Texto.get("CINFONODO_SUBNOD", Conf.idioma)
				+ ": " + this.nodo.numHijosRec());

		JPanel panelContGral1[] = new JPanel[FILAS_INFO];
		for (int i = 0; i < panelContGral1.length; i++) {
			panelContGral1[i] = new JPanel();
			panelContGral1[i].setLayout(new BorderLayout());
			panelContGral1[i].add(infoGral1[i], BorderLayout.WEST);
			panelContGral1[i].setPreferredSize(new Dimension(
					ANCHO_COLUMNA_ANCHA, ALTO_FILA));
		}

		// Empaquetamos paneles para poder situarlos en el cuadro de diálogo
		JPanel panelHorizGral[] = new JPanel[FILAS_INFO];

		for (int i = 0; i < panelHorizGral.length; i++) {
			panelHorizGral[i] = new JPanel();
			panelHorizGral[i].setLayout(new BorderLayout());
			panelHorizGral[i].add(panelContGral1[i], BorderLayout.WEST);
		}

		// Añadimos los paneles paquetizados al panel que irá en el cuadro de
		// diálogo
		for (int i = 0; i < panelHorizGral.length; i++) {
			panelInfoGral.add(panelHorizGral[i]);
		}

		panelInfoGral.setBorder(new TitledBorder(Texto.get("CINFONODO_NODSEL",
				Conf.idioma)));

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(this.aceptar);

		this.aceptar.addKeyListener(this);
		this.aceptar.addActionListener(this);

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelInfoGral, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("CINFONODO_TITULO", Conf.idioma));

		if (this.ventana.msWindows) {
			anchoCuadro = ANCHO_COLUMNA_ANCHA + 40;
			altoCuadro = (ALTO_FILA * FILAS_INFO) + 100;
			this.dialogo.setSize(anchoCuadro, altoCuadro);
			int coord[] = Conf.ubicarCentro(anchoCuadro, altoCuadro);
			this.dialogo.setLocation(coord[0], coord[1]);
		} else {
			this.dialogo.setSize(ANCHO_CUADRO_NO_WINDOWS,
					ALTO_CUADRO_NO_WINDOWS);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO_NO_WINDOWS,
					ALTO_CUADRO_NO_WINDOWS);
			this.dialogo.setLocation(coord[0], coord[1]);
		}

		if (Conf.fichero_log) {
			String mensaje = "Información > Información nodo actual...: N."
					+ this.nodo.getID() + "   " + this.nodo.getNombreMetodo()
					+ "(";
			String paramE[] = this.nodo.getEntradasString();
			String paramS[] = this.nodo.getSalidasString();

			for (int i = 0; i < paramE.length; i++) {
				if (i != 0) {
					mensaje += ",";
				}
				mensaje += paramE[i];
			}
			mensaje += ") [";

			for (int i = 0; i < paramS.length; i++) {
				if (i != 0) {
					mensaje += ",";
				}
				mensaje += paramS[i];
			}
			mensaje += "]";

			Logger.log_write(mensaje);
		}

		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.dialogo.setVisible(false);
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER
				|| e.getKeyCode() == KeyEvent.VK_ESCAPE) {
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
