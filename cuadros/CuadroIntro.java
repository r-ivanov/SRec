package cuadros;

import java.awt.BorderLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import conf.*;
import ventanas.*;

/**
 * Cuadro de identificación del programa.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroIntro extends Thread {

	private static final int ANCHO_CUADRO = 600;
	private static final int ALTO_CUADRO = 300;

	private JLabel etiqueta1;
	private JPanel panel;

	private BorderLayout bl;

	private JDialog dialogo = null;

	/**
	 * Genera un nuevo cuadro de identificación del programa.
	 * 
	 * @param ventana Ventana a la que quedará asociado el cuadro.
	 */
	public CuadroIntro(Ventana ventana) {
		this.dialogo = new JDialog(ventana, true);
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public synchronized void run() {
		// Etiqueta 1
		this.etiqueta1 = new JLabel();
		Icon imagen = new ImageIcon(getClass().getClassLoader().getResource(
				"imagenes/ImagenIntro_" + idiomaImagen() + ".png"));
		this.etiqueta1.setIcon(imagen);

		// Panel general
		this.bl = new BorderLayout();
		this.panel = new JPanel();
		this.panel.setLayout(this.bl);

		this.panel.add(this.etiqueta1, BorderLayout.WEST);

		this.dialogo.getContentPane().add(this.panel);
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);

		this.dialogo.setTitle("");
		this.dialogo.setUndecorated(true);
		this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);

		this.dialogo.setResizable(false);

		final JDialog dialogoF = this.dialogo;

		new Thread() {
			@Override
			public synchronized void run() {
				try {
					wait(3500);
				} catch (InterruptedException ie) {
				}
				dialogoF.setVisible(false);
			}
		}.start();

		this.dialogo.setVisible(true);
	}

	private String idiomaImagen() {
		if (Conf.idioma.equals("es")) {
			return "es";
		} else {
			return "en";
		}
	}
}
