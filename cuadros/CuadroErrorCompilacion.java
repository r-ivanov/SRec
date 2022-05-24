package cuadros;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import conf.*;
import botones.*;
import utilidades.*;

/**
 * Representa un cuadro de error para errores de compilación.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroErrorCompilacion extends Thread implements ActionListener,
		KeyListener, MouseListener {

	private static final int ANCHO_CUADRO_POR_DEFECTO = 760;
	private static int ALTO_CUADRO_POR_DEFECTO = 440;

	private JLabel etiqueta;
	private BotonAceptar aceptar;
	private JPanel panelBoton;

	private String fichero;
	private String errores;

	private JDialog d;

	/**
	 * Constructor: crea una nueva instancia del cuadro de error
	 * 
	 * @param ventana
	 *            ventana a la que quedará asociado este cuadro
	 * @param titulo
	 *            título que llevará el cuadro de error
	 * @param etiq
	 *            mensaje que mostrará el cuadro de error
	 */
	public CuadroErrorCompilacion(JFrame ventana, String fichero, String errores) {
		this.d = new JDialog(ventana, true);
		this.fichero = fichero;
		this.errores = errores;
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al hilo.
	 */
	@Override
	public synchronized void run() {

		JPanel panel = new JPanel();
		this.d.setContentPane(panel);
		panel.setLayout(new BorderLayout());

		// Etiqueta de texto
		this.etiqueta = new JLabel(Texto.get("ERROR_COMP", Conf.idioma) + " "
				+ this.fichero + ":");
		this.etiqueta.addKeyListener(this);
		JPanel panelEtiqueta = new JPanel();
		panelEtiqueta.add(this.etiqueta);
		panelEtiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(panelEtiqueta, BorderLayout.NORTH);

		// JTextPane para errores
		JTextArea textPane = new JTextArea();
		textPane.setText(this.errores);
		textPane.setFont(new Font("Courier New", Font.PLAIN, 11));
		textPane.setEditable(false);
		textPane.addKeyListener(this);
		JScrollPane jsp = new JScrollPane(textPane);
		panel.add(jsp, BorderLayout.CENTER);

		// Botón Aceptar
		this.aceptar = new BotonAceptar();
		this.aceptar.addKeyListener(this);
		this.aceptar.addMouseListener(this);

		// Panel de botón Aceptar
		this.panelBoton = new JPanel();
		this.panelBoton.add(this.aceptar);
		this.panelBoton.addKeyListener(this);
		panel.add(this.panelBoton, BorderLayout.SOUTH);

		if (Conf.fichero_log) {
			Logger.log_write("ERROR COMPILACIÓN [" + this.fichero + "]");
		}

		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO_POR_DEFECTO,
				ALTO_CUADRO_POR_DEFECTO);
		this.d.setLocation(coord[0], coord[1]);
		this.d.setTitle(Texto.get("ERROR_ARCH", Conf.idioma));
		this.d.setSize(ANCHO_CUADRO_POR_DEFECTO, ALTO_CUADRO_POR_DEFECTO);

		this.d.setResizable(false);
		this.d.setVisible(true);

	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.d.setVisible(false);
		d.dispose();
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
			this.d.setVisible(false);
			d.dispose();
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
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ENTER) {
			this.d.setVisible(false);
			d.dispose();
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
		if (e.getComponent().equals(this.aceptar)) {
			this.d.setVisible(false);
			d.dispose();
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

	}

}