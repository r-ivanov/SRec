package botones;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Botón de la aplicación que muestra un icono.
 * 
 * @author David Pastor Herranz
 */
public class BotonIcono extends JButton {

	private static final long serialVersionUID = 8220772493538029951L;

	/**
	 * Construye un nuevo botón que mostrará el icono que recibe por parámetro.
	 * También establece el ancho y alto del botón.
	 * 
	 * @param icono
	 *            Icono que se mostrará para el botón.
	 * @param ancho
	 *            ancho del botón
	 * @param alto
	 *            alto del botón
	 */
	public BotonIcono(ImageIcon icono, int ancho, int alto) {
		super(icono);
		this.setPreferredSize(new Dimension(ancho, alto));
	}
}
