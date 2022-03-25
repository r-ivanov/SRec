package cuadros;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.ImageIcon;

import conf.*;
import botones.*;

/**
 * Permite construir un cuadro de información genérico. Este cuadro únicamente
 * mostrará un texto informativo al usuario y un botón para cerrarlo.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroInformacion extends Thread implements ActionListener,
		KeyListener {

	private int ancho = 550;
	private int alto = 100;

	private JLabel etiqueta, imagen;
	private BotonAceptar aceptar;

	private JPanel panel, panelBoton, panelImagen, panelDerecha, panelEtiqueta;

	private String titulo;
	private String etiq;

	private BorderLayout bl, bl0, bl1;

	private JDialog dialogo;

	/**
	 * Crea un nuevo cuadro de información.
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
	public CuadroInformacion(JFrame ventana, String titulo, String etiq,
			int anc, int alt) {
		this.dialogo = new JDialog(ventana, true);
		this.titulo = titulo;
		this.etiq = etiq;
		this.ancho = anc;
		this.alto = alt;
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		// Etiqueta para icono
		this.panelImagen = new JPanel();
		this.imagen = new JLabel(new ImageIcon(getClass()
				.getClassLoader().getResource("imagenes/info.gif")));
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
		this.aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);

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

		// Panel general de CuadroInformacion
		this.bl = new BorderLayout();
		this.panel = new JPanel();
		this.panel.setLayout(this.bl);
		this.panel.addKeyListener(this);

		this.panel.add(this.imagen, BorderLayout.WEST);
		this.panel.add(this.panelDerecha, BorderLayout.CENTER);

		this.dialogo.getContentPane().add(this.panel);
		int coord[] = Conf.ubicarCentro(this.ancho, this.alto);
		this.dialogo.setLocation(coord[0], coord[1]);
		this.dialogo.setTitle(this.titulo);
		this.dialogo.setSize(this.ancho, this.alto);
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}
	
	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.dialogo.setVisible(false);
	}
	
	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		
	}
	
	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER
				|| e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		}
	}

}