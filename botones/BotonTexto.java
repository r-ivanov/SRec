package botones;

import java.awt.Dimension;
import javax.swing.JButton;
import conf.Conf;

/**
 * Bot�n de la aplicaci�n que contiene una cadena de texto.
 * 
 * @author David Pastor Herranz
 */
public class BotonTexto extends JButton {

	private static final long serialVersionUID = -5249565776019093689L;

	/**
	 * Construye un nuevo bot�n que mostrar� el texto recibido por par�metro. En
	 * este caso, la altura y anchura del bot�n vendr�n dadas por los valores
	 * generales de configuraci�n establecidos.
	 * 
	 * @param texto
	 *            texto que aparecer� en el bot�n
	 */
	public BotonTexto(String texto) {
		this.setText(texto);
		this.setPreferredSize(new Dimension(Conf.anchoBoton, Conf.altoBoton));
	}

	/**
	 * Construye un nuevo bot�n que mostrar� el texto recibido por par�metro. En
	 * este caso, la altura vendr� dada por los valores generales de
	 * configuraci�n establecidos, la anchura ser� la recibida por par�metro.
	 * 
	 * @param texto
	 *            texto que aparecer� en el bot�n
	 * @param ancho
	 *            ancho del bot�n
	 */
	public BotonTexto(String texto, int ancho) {
		this.setText(texto);
		this.setPreferredSize(new Dimension(ancho, Conf.altoBoton));
	}
}
