package botones;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.ImageIcon;

/**
 * Botón de la aplicación que permite mostrar distintas imagenes según el estado
 * actual del botón.
 * 
 * @author Antonio Pérez Carrasco
 * @author David Pastor Herranz
 */
public class BotonImagen extends JButton {

	private static final long serialVersionUID = 4020791979509462884L;

	private boolean ratonEstaSobreBoton = false;
	private boolean mostrarPulsado = false;

	private URL pathImagenBotonHabilitado;
	private URL pathImagenBotonDeshabilitado;
	private URL pathImagenRatonSobreBoton;

	/**
	 * Construye un nuevo botón dadas las distintas imágenes que deberá mostrar
	 * cuando se encuentre habilitado, deshabilitado o con el ratón sobre el
	 * botón. También establece el ancho y alto del botón.
	 * 
	 * @param pathImagenBotonHabilitado
	 *            dirección de la imagen que se usará para mostrar cuando el
	 *            botón está habilitado, disponible para ser pulsado.
	 * @param pathImagenRatonSobreBoton
	 *            dirección de la imagen que se usará para mostrar cuando el
	 *            botón está pulsado o con el ratón encima.
	 * @param pathImagenBotonDeshabilitado
	 *            dirección de la imagen que se usará para mostrar cuando el
	 *            botón está deshabilitado.
	 * @param ancho
	 *            ancho del botón.
	 * @param alto
	 *            alto del botón.
	 */
	public BotonImagen(URL pathImagenBotonHabilitado,
			URL pathImagenRatonSobreBoton,
			URL pathImagenBotonDeshabilitado, int ancho, int alto) {
		super(new ImageIcon(pathImagenBotonHabilitado));
		this.pathImagenBotonHabilitado = pathImagenBotonHabilitado;
		this.pathImagenRatonSobreBoton = pathImagenRatonSobreBoton;
		this.pathImagenBotonDeshabilitado = pathImagenBotonDeshabilitado;
		this.setPreferredSize(new Dimension(ancho, alto));
	}

	/**
	 * Determina y establece la imagen que debe mostrar el botón.
	 */
	private void actualizarImagenMostrada() {
		if (this.isEnabled()) {
			if (this.ratonEstaSobreBoton || this.mostrarPulsado) {
				this.setIcon(new ImageIcon(this.pathImagenRatonSobreBoton));
			} else {
				this.setIcon(new ImageIcon(this.pathImagenBotonHabilitado));
			}
		} else {
			this.setIcon(new ImageIcon(this.pathImagenBotonDeshabilitado));
		}
	}

	/**
	 * Habilita el botón, mostrando la imagen correspondiente.
	 */
	public void habilitar() {
		this.setEnabled(true);
		this.mostrarPulsado = false;
		this.actualizarImagenMostrada();
	}

	/**
	 * Deshabilita el botón, mostrando la imagen correspondiente.
	 */
	public void deshabilitar() {
		this.setEnabled(false);
		this.mostrarPulsado = false;
		this.actualizarImagenMostrada();
	}

	/**
	 * Notifica al botón que el ratón se encuentra actualmente sobre el.
	 */
	public void ratonEstaSobreBoton() {
		this.ratonEstaSobreBoton = true;
		this.actualizarImagenMostrada();
	}

	/**
	 * Notifica al botón que el ratón ya no se encuentra sobre el.
	 */
	public void ratonNoEstaSobreBoton() {
		this.ratonEstaSobreBoton = false;
		this.actualizarImagenMostrada();
	}

	/**
	 * Notifica al botón que debe mostrar la imagen correspondiente a cuando el
	 * botón esta pulsado, el botón mostrará esta imagen hasta que se habilite
	 * de nuevo o se deshabilite.
	 */
	public void mostrarPulsado() {
		this.mostrarPulsado = true;
		this.actualizarImagenMostrada();
	}
}