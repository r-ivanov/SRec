package botones;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Bot�n de la aplicaci�n que muestra un icono.
 * 
 * @author David Pastor Herranz
 */
public class BotonIcono extends JButton {

	private static final long serialVersionUID = 8220772493538029951L;

	/**
	 * Construye un nuevo bot�n que mostrar� el icono que recibe por par�metro.
	 * Tambi�n establece el ancho y alto del bot�n.
	 * 
	 * @param icono
	 *            Icono que se mostrar� para el bot�n.
	 * @param ancho
	 *            ancho del bot�n
	 * @param alto
	 *            alto del bot�n
	 */
	public BotonIcono(ImageIcon icono, int ancho, int alto) {
		super(icono);
		this.setPreferredSize(new Dimension(ancho, alto));
	}
}
