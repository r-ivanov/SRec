package cuadros;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import conf.*;
import botones.*;
import utilidades.*;
import ventanas.*;

/**
 * Cuadro de identificación del programa
 * 
 * @author Antonio Pérez Carrasco
 * @author David Pastor Herranz
 */
public class CuadroAcercade extends Thread implements ActionListener,
		KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 580;
	private static final int ALTO_CUADRO = 200;

	private JLabel etiquetaImagen;
	private JTextPane texto;
	private JPanel panel;

	private BorderLayout bl;
	private BotonAceptar aceptar;

	private JDialog dialogo;

	/**
	 * Genera un nuevo cuadro de identifiación del programa.
	 * 
	 * @param ventana
	 *            ventana a la que quedará asociado el cuadro.
	 */
	public CuadroAcercade(Ventana ventana) {
		this.dialogo = new JDialog(ventana, true);
		this.start();
	}

	/**
	 * Genera un nuevo cuadro de identifiación del programa
	 */
	@Override
	public void run() {

		// Etiqueta con la imagen
		this.etiquetaImagen = new JLabel();
		Icon imagen = new ImageIcon(getClass().getClassLoader().getResource(
				"imagenes/cuadro_acercade.gif"));
		this.etiquetaImagen.setIcon(imagen);
		this.etiquetaImagen.addKeyListener(this);
		this.etiquetaImagen.setToolTipText(Texto.get("URJC_SREC", Conf.idioma));

		// Descripción
		this.texto = new JTextPane();
		this.texto.setAlignmentX(0);
		this.texto.setAlignmentY(0);
		this.texto.setText("\r\n\r\n" + Texto.get("APLIC", Conf.idioma)
				+ "\r\n" + Texto.get("URJC_SREC_02", Conf.idioma)
				+ " 1.7\r\n\r\n2020\n\n\n\n");
		this.texto.setEditable(false);
		this.texto.addKeyListener(this);

		// Panel Boton
		JPanel panelBoton = new JPanel();
		this.aceptar = new BotonAceptar();
		this.aceptar.addMouseListener(this);
		panelBoton.add(this.aceptar);

		// Panel general
		this.bl = new BorderLayout();
		this.panel = new JPanel();
		this.panel.setLayout(this.bl);

		this.panel.add(this.etiquetaImagen, BorderLayout.WEST);
		this.panel.add(this.texto, BorderLayout.EAST);
		this.panel.add(panelBoton, BorderLayout.SOUTH);

		this.texto.setBackground(this.panel.getBackground());

		this.dialogo.getContentPane().add(this.panel);
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);

		this.dialogo.setTitle(Texto.get("URJC_ACERCADE", Conf.idioma));
		this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);

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
		if (e.getSource() == this.aceptar) {
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
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
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
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
			this.dialogo.setVisible(false);
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            eevnto de teclado
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
			this.dialogo.setVisible(false);
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
		if (e.getSource() == this.aceptar) {
			this.dialogo.setVisible(false);
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
