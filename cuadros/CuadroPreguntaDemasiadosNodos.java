package cuadros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import conf.*;
import datos.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite construir un cuadro de pregunta para que el usuario decida
 * si desea continuar con la visualización tras la ejecución de un
 * algoritmo con demasiados nodos.
 * 
 * @author David Pastor Herranz
 */
public class CuadroPreguntaDemasiadosNodos extends CuadroPregunta implements
		ActionListener, KeyListener {
	
	private Runnable accionPositiva;
	
	/**
	 * Genera un nuevo cuadro de pregunta.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 * @param accionPositiva Acción a realizar si el usuario confirma la pregunta.
	 */
	public CuadroPreguntaDemasiadosNodos(Ventana ventana, Runnable accionPositiva) {
		super(ventana, Texto.get("PREG_DEMASIADOS_NODOS_TITULO", Conf.idioma), Texto
				.get("PREG_DEMASIADOS_NODOS_BODY", Conf.idioma), Texto.get("SI",
				Conf.idioma), Texto.get("NO", Conf.idioma));
		this.getDialogo().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.accionPositiva = accionPositiva;
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
			SwingUtilities.invokeLater(this.accionPositiva);
		}
		d.dispose();
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int tecla = e.getKeyCode();
		if (e.getSource() == this.cancelar
				&& (tecla == KeyEvent.VK_KP_LEFT || tecla == KeyEvent.VK_LEFT)) {
			this.cancelar.transferFocusBackward();
		} else if (e.getSource() == this.aceptar
				&& (tecla == KeyEvent.VK_KP_RIGHT || tecla == KeyEvent.VK_RIGHT)) {
			this.aceptar.transferFocus();
		}
	}

}
