package cuadros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import conf.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite construir un cuadro de pregunta que permite al usuario confirmar
 * la salida de la aplicación.
 * 
 * @author Antonio Pérez Carrasco
 * @author David Pastor Herranz
 */
public class CuadroPreguntaSalirAplicacion extends CuadroPregunta implements
		ActionListener, KeyListener {
	
	/**
	 * Permite construir un cuadro de pregunta que permite al usuario confirmar
	 * la salida de la aplicación.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 */
	public CuadroPreguntaSalirAplicacion(Ventana ventana) {
		super(ventana, Texto.get("PREG_SALIR_TITULO", Conf.idioma), Texto.get(
				"PREG_SALIR_BODY", Conf.idioma), Texto.get("BOTONACEPTAR", Conf.idioma),
				Texto.get("BOTONCANCELAR", Conf.idioma));
		this.start();
	}

	/**
	 * Devuelve la ventana asociada.
	 * 
	 * @return Ventana asociada.
	 */
	private Ventana getVentana() {
		return (Ventana) this.ventana;
	}

	/**
	 * Gestiona los eventos de acción.
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.d.setVisible(false);
		if (e.getSource() == this.aceptar) {
			this.getVentana().cerrar();
		}
	}

}