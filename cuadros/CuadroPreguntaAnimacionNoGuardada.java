/**
	Representa el cuadro de error
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
 */

package cuadros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import conf.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite construir un cuadro de pregunta que permite al usuario guardar la
 * animación ejecutada cuando el usuario finaliza la visualización de una
 * animación sin haber sido guardada.
 * 
 * @author Antonio Pérez Carrasco
 * @author David Pastor Herranz
 */
public class CuadroPreguntaAnimacionNoGuardada extends CuadroPregunta implements
		ActionListener, KeyListener {

	/**
	 * Genera un cuadro de pregunta que permite al usuario guardar la animación
	 * ejecutada cuando el usuario finaliza la visualización de una animación
	 * sin haber sido guardada.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 */
	public CuadroPreguntaAnimacionNoGuardada(Ventana ventana) {
		super(ventana, Texto.get("PREG_ANIMNOGUARD", Conf.idioma), Texto.get(
				"PREGMEN_ANIMNOGUARD", Conf.idioma), Texto.get("GUARDAR",
				Conf.idioma), Texto.get("BOTONCANCELAR", Conf.idioma), Texto
				.get("NO_GUARDAR", Conf.idioma));
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
			this.getVentana().guardadoTraza();
		}
		if (e.getSource() != this.cancelar) {
			this.getVentana().cerrar();
		}
	}

}