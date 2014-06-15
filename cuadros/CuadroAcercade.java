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
	 *            ventana a la que quedará asociado el cuadro
	 */
	public CuadroAcercade(Ventana ventana) {
		dialogo = new JDialog(ventana, true);
		this.start();
	}

	/**
	 * Genera un nuevo cuadro de identifiación del programa
	 */
	public void run() {

		// Etiqueta con la imagen
		this.etiquetaImagen = new JLabel();
		Icon imagen = new ImageIcon("imagenes/cuadro_acercade.gif");
		etiquetaImagen.setIcon(imagen);
		etiquetaImagen.addKeyListener(this);
		etiquetaImagen.setToolTipText(Texto.get("URJC_SREC", Conf.idioma));

		// Descripción
		texto = new JTextPane();
		texto.setAlignmentX(0);
		texto.setAlignmentY(0);
		texto.setText("\r\n\r\n" + Texto.get("APLIC", Conf.idioma) + "\r\n"
				+ Texto.get("URJC_SREC_02", Conf.idioma)
				+ " 1.2\r\n\r\n2010\n\n\n\n");
		texto.setEditable(false);
		texto.addKeyListener(this);

		// Panel Boton
		JPanel panelBoton = new JPanel();
		aceptar = new BotonAceptar();
		aceptar.addMouseListener(this);
		panelBoton.add(aceptar);

		// Panel general
		bl = new BorderLayout();
		panel = new JPanel();
		panel.setLayout(bl);

		panel.add(etiquetaImagen, BorderLayout.WEST);
		panel.add(texto, BorderLayout.EAST);
		panel.add(panelBoton, BorderLayout.SOUTH);

		texto.setBackground(panel.getBackground());

		dialogo.getContentPane().add(panel);
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		dialogo.setLocation(coord[0], coord[1]);

		dialogo.setTitle(Texto.get("URJC_ACERCADE", Conf.idioma));
		dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);

		dialogo.setResizable(false);
		dialogo.setVisible(true);
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == aceptar) {
			this.dialogo.setVisible(false);
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
			dialogo.setVisible(false);
		}
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
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            eevnto de teclado
	 */
	public void keyTyped(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
			dialogo.setVisible(false);
		}
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

	}
}
