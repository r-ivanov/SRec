package botones;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.ImageIcon;

/**
 * Bot�n de la aplicaci�n que permite mostrar distintas imagenes seg�n
 * el estado actual del bot�n.
 * 
 * @author Antonio P�rez Carrasco
 * @author David Pastor Herranz
 */
public class BotonImagen extends JButton {

	private static final long serialVersionUID = 4020791979509462884L;

	private boolean ratonEstaSobreBoton = false;
	private boolean mostrarPulsado = false;

	private String pathImagenBotonHabilitado;
	private String pathImagenBotonDeshabilitado;
	private String pathImagenRatonSobreBoton;

	/**
	 * Construye un nuevo bot�n dadas las distintas im�genes que deber� mostrar
	 * cuando se encuentre habilitado, deshabilitado o con el rat�n sobre el
	 * bot�n. Tambi�n establece el ancho y alto del bot�n.
	 * 
	 * @param pathImagenBotonHabilitado
	 *            direcci�n de la imagen que se usar� para mostrar cuando el
	 *            bot�n est� habilitado, disponible para ser pulsado.
	 * @param pathImagenRatonSobreBoton
	 *            direcci�n de la imagen que se usar� para mostrar cuando el
	 *            bot�n est� pulsado o con el rat�n encima.
	 * @param pathImagenBotonDeshabilitado
	 *            direcci�n de la imagen que se usar� para mostrar cuando el
	 *            bot�n est� deshabilitado.
	 * @param ancho
	 *            ancho del bot�n.
	 * @param alto
	 *            alto del bot�n.
	 */
	public BotonImagen(String pathImagenBotonHabilitado,
			String pathImagenRatonSobreBoton,
			String pathImagenBotonDeshabilitado, int ancho, int alto) {
		super(new ImageIcon(pathImagenBotonHabilitado));
		this.pathImagenBotonHabilitado = pathImagenBotonHabilitado;
		this.pathImagenRatonSobreBoton = pathImagenRatonSobreBoton;
		this.pathImagenBotonDeshabilitado = pathImagenBotonDeshabilitado;
		this.setPreferredSize(new Dimension(ancho, alto));
	}
	
	/**
	 * Determina y establece la imagen que debe mostrar el bot�n.
	 */
	private void actualizarImagenMostrada() {		
		if (this.isEnabled()) {			
			if (ratonEstaSobreBoton || mostrarPulsado) {
				this.setIcon(new ImageIcon(pathImagenRatonSobreBoton));
			} else {
				this.setIcon(new ImageIcon(pathImagenBotonHabilitado));
			}			
		} else {
			this.setIcon(new ImageIcon(pathImagenBotonDeshabilitado));
		}
	}
	
	/**
	 * Habilita el bot�n, mostrando la imagen correspondiente.
	 */
	public void habilitar() {
		this.setEnabled(true);
		mostrarPulsado = false;
		this.actualizarImagenMostrada();
	}

	/**
	 * Deshabilita el bot�n, mostrando la imagen correspondiente.
	 */
	public void deshabilitar() {
		this.setEnabled(false);
		mostrarPulsado = false;
		this.actualizarImagenMostrada();
	}

	/**
	 * Notifica al bot�n que el rat�n se encuentra actualmente
	 * sobre el.
	 */
	public void ratonEstaSobreBoton() {
		this.ratonEstaSobreBoton = true;
		this.actualizarImagenMostrada();
	}
	
	/**
	 * Notifica al bot�n que el rat�n ya no se encuentra sobre el.
	 */
	public void ratonNoEstaSobreBoton() {
		this.ratonEstaSobreBoton = false;
		this.actualizarImagenMostrada();
	}
	
	/**
	 * Notifica al bot�n que debe mostrar la imagen correspondiente
	 * a cuando el bot�n esta pulsado, el bot�n mostrar� esta imagen
	 * hasta que se habilite de nuevo o se deshabilite.
	 */
	public void mostrarPulsado() {
		this.mostrarPulsado = true;
		this.actualizarImagenMostrada();
	}
}