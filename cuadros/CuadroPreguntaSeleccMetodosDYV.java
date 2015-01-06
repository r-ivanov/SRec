package cuadros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import conf.*;
import datos.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite construir un cuadro de pregunta para que el usuario decida si desea
 * habilitar la selecci�n de m�todos para Divide y vencer�s.
 * 
 * @author Antonio P�rez Carrasco
 * @author David Pastor Herranz
 */
public class CuadroPreguntaSeleccMetodosDYV extends CuadroPregunta implements
		ActionListener, KeyListener {

	private ClaseAlgoritmo clase;
	private Preprocesador p;

	/**
	 * Genera un cuadro de pregunta para que el usuario decida si desea
	 * habilitar la selecci�n de m�todos para Divide y vencer�s.
	 * 
	 * @param ventana
	 *            Ventana a la que quedar� asociado el cuadro.
	 * @param clase
	 *            Clase del algoritmo que se enviar� al cuadro de selecci�n de
	 *            m�todos.
	 * @param p
	 *            Preprocesador que se enviar� al cuado de selecci�n de m�todos.
	 */
	public CuadroPreguntaSeleccMetodosDYV(Ventana ventana,
			ClaseAlgoritmo clase, Preprocesador p) {
		super(ventana, Texto.get("PREG_SELECCMETODOSDYV", Conf.idioma), Texto
				.get("PREGMEN_SELECCMETODOSDYV", Conf.idioma), Texto.get("SI",
				Conf.idioma), Texto.get("NO", Conf.idioma));
		this.clase = clase;
		this.p = p;
		this.start();
	}
	
	private Ventana getVentana() {
		return (Ventana) this.ventana;
	}

	/**
	 * Gestiona los eventos de acci�n.
	 * 
	 * @param e
	 *            evento de acci�n
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.d.setVisible(false);
		if (e.getSource() == this.aceptar) {
			if (Conf.fichero_log) {
				Logger.log_write("�Habilitar vistas para DYV? S�");
			}
			new CuadroSeleccionMetodos(this.clase, this.getVentana(), this.p);
		} else {
			this.p.fase2(this.clase);
		}
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
