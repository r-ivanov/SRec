package botones;

import java.awt.Dimension;
import javax.swing.JButton;
import conf.Conf;

/**
 * Botón de la aplicación que contiene una cadena de texto.
 * 
 * @author David Pastor Herranz
 */
public class BotonTexto extends JButton {

	private static final long serialVersionUID = -5249565776019093689L;

	/**
	 * Construye un nuevo botón que mostrará el texto recibido por parámetro. En
	 * este caso, la altura y anchura del botón vendrán dadas por los valores
	 * generales de configuración establecidos.
	 * 
	 * @param texto
	 *            texto que aparecerá en el botón
	 */
	public BotonTexto(String texto) {
		this.setText(texto);
		this.setPreferredSize(new Dimension(Conf.anchoBoton, Conf.altoBoton));
	}

	/**
	 * Construye un nuevo botón que mostrará el texto recibido por parámetro. En
	 * este caso, la altura vendrá dada por los valores generales de
	 * configuración establecidos, la anchura será la recibida por parámetro.
	 * 
	 * @param texto
	 *            texto que aparecerá en el botón
	 * @param ancho
	 *            ancho del botón
	 */
	public BotonTexto(String texto, int ancho) {
		this.setText(texto);
		this.setPreferredSize(new Dimension(ancho, Conf.altoBoton));
	}
}
