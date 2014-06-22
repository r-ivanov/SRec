package cuadros;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

import conf.*;
import botones.*;
import utilidades.*;

/**
 * Representa el cuadro de error.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroError extends Thread implements ActionListener, KeyListener,
		MouseListener {

	private static final int ANCHO_CUADRO_POR_DEFECTO = 550;
	private static final int ALTO_CUADRO_POR_DEFECTO = 100;

	private int anchoCuadro;
	private int altoCuadro;

	private JLabel etiqueta, imagen;
	private BotonAceptar aceptar;

	private JPanel panel, panelBoton, panelImagen, panelDerecha, panelEtiqueta;

	private String titulo;
	private String etiq;

	private BorderLayout bl, bl0, bl1;

	private JDialog d;

	/**
	 * Constructor: crea una nueva instancia del cuadro de error
	 * 
	 * @param dialogo
	 *            dialogo al que quedará asociado este cuadro
	 * @param titulo
	 *            título que llevará el cuadro de error
	 * @param etiq
	 *            mensaje que mostrará el cuadro de error
	 */
	public CuadroError(JDialog dialogo, String titulo, String etiq) {
		this(dialogo, titulo, etiq, ANCHO_CUADRO_POR_DEFECTO,
				ALTO_CUADRO_POR_DEFECTO);
	}

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
	public CuadroError(JFrame ventana, String titulo, String etiq) {
		this(ventana, titulo, etiq, ANCHO_CUADRO_POR_DEFECTO,
				ALTO_CUADRO_POR_DEFECTO);
	}

	/**
	 * Constructor: crea una nueva instancia del cuadro de error
	 * 
	 * @param dialogo
	 *            dialogo al que quedará asociado este cuadro
	 * @param titulo
	 *            título que llevará el cuadro de error
	 * @param etiq
	 *            mensaje que mostrará el cuadro de error
	 * @param anc
	 *            ancho del cuadro
	 * @param alt
	 *            alto del cuadro
	 */
	private CuadroError(JDialog dialogo, String titulo, String etiq, int anc,
			int alt) {
		this.d = new JDialog(dialogo, true);
		this.titulo = titulo;
		this.etiq = etiq;
		this.anchoCuadro = anc;
		this.altoCuadro = alt;
		this.start();

	}

	/**
	 * Constructor: crea una nueva instancia del cuadro de error
	 * 
	 * @param ventana
	 *            ventana a la que quedará asociado este cuadro
	 * @param titulo
	 *            título que llevará el cuadro de error
	 * @param etiq
	 *            mensaje que mostrará el cuadro de error
	 * @param anc
	 *            ancho del cuadro
	 * @param alt
	 *            alto del cuadro
	 */
	public CuadroError(JFrame ventana, String titulo, String etiq, int anc,
			int alt) {
		this.d = new JDialog(ventana, true);
		this.titulo = titulo;
		this.etiq = etiq;
		this.anchoCuadro = anc;
		this.altoCuadro = alt;
		this.start();
	}

	/**
	 * Ejecuta el thread.
	 */
	@Override
	public void run() {

		// Etiqueta para icono
		this.panelImagen = new JPanel();
		this.imagen = new JLabel(new ImageIcon("imagenes/error.gif"));
		this.imagen.addKeyListener(this);
		this.imagen.setHorizontalAlignment(0);
		this.imagen.setVerticalAlignment(0);
		this.panelImagen.add(this.imagen);

		// Etiqueta de texto
		this.etiqueta = new JLabel(this.etiq);
		this.etiqueta.addKeyListener(this);
		this.etiqueta.setHorizontalAlignment(0);
		this.etiqueta.setVerticalAlignment(0);

		// Panel para etiqueta
		this.bl1 = new BorderLayout();
		this.panelEtiqueta = new JPanel();
		this.panelEtiqueta.setLayout(this.bl1);
		this.panelEtiqueta.add(this.etiqueta, BorderLayout.CENTER);

		// Botón Aceptar
		this.aceptar = new BotonAceptar();
		this.aceptar.addKeyListener(this);
		this.aceptar.addMouseListener(this);

		// Panel de botón Aceptar
		this.panelBoton = new JPanel();
		this.panelBoton.add(this.aceptar);
		this.panelBoton.addKeyListener(this);
		this.panelBoton.transferFocus();

		// Panel de la derecha
		this.bl0 = new BorderLayout();
		this.panelDerecha = new JPanel();
		this.panelDerecha.setLayout(this.bl0);
		this.panelDerecha.add(this.panelEtiqueta, BorderLayout.CENTER);
		this.panelDerecha.add(this.panelBoton, BorderLayout.SOUTH);
		this.panelDerecha.addKeyListener(this);

		// Panel general de CuadroError
		this.bl = new BorderLayout();
		this.panel = new JPanel();
		this.panel.setLayout(this.bl);
		this.panel.addKeyListener(this);

		this.panel.add(this.imagen, BorderLayout.WEST);
		this.panel.add(this.panelDerecha, BorderLayout.CENTER);

		if (Conf.fichero_log) {
			Logger.log_write("ERROR [" + this.titulo + "]: " + this.etiq);
		}

		this.d.getContentPane().add(this.panel);
		int coord[] = Conf.ubicarCentro(this.anchoCuadro, this.altoCuadro);
		this.d.setLocation(coord[0], coord[1]);
		this.d.setTitle(this.titulo);
		this.d.setSize(this.anchoCuadro, this.altoCuadro);
		this.d.setResizable(false);
		this.d.setVisible(true);
		this.aceptar.transferFocus();
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