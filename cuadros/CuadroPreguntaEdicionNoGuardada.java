package cuadros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import conf.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite construir un cuadro de pregunta para que antes de que el usuario
 * realice una determinada acci�n que depende de la clase actualmente cargada,
 * el usuario pueda guardar los cambios efectuados en ella.
 * 
 * @author Antonio P�rez Carrasco
 * @author David Pastor Herranz
 */
public class CuadroPreguntaEdicionNoGuardada extends CuadroPregunta implements
		ActionListener, KeyListener {

	private String accion;

	/**
	 * Genera un cuadro de pregunta que permite construir un cuadro de pregunta
	 * para que antes de que el usuario realice una determinada acci�n que
	 * depende de la clase actualmente cargada, el usuario pueda guardar los
	 * cambios efectuados en ella.
	 * 
	 * @param ventana
	 *            Ventana a la que quedar� asociado el cuadro.
	 * @param accion
	 *            Acci�n que debe ejecutarse tras procesar la decisi�n del
	 *            usuario.
	 */
	public CuadroPreguntaEdicionNoGuardada(Ventana ventana, String accion) {
		super(ventana,
				accion.equals("guardar") ? Texto.get("PREG_CLASNOGUARD",Conf.idioma) : 
				Texto.get("PREG_EDITNOGUARD", Conf.idioma),
				accion.equals("guardar") ? Texto.get("PREGMEN_CLASNOGUARD",
						Conf.idioma) : Texto.get("PREGMEN_EDITNOGUARD",
						Conf.idioma), Texto.get("SI", Conf.idioma), Texto.get(
						"NO", Conf.idioma));
		this.accion = accion;
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
	 * Gestiona los eventos de acci�n.
	 * 
	 * @param e
	 *            evento de acci�n
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.d.setVisible(false);
		if (e.getSource() == this.aceptar) {
			this.getVentana().guardarClase();
		}

		//	Cuando el di�logo viene de seleccionar m�todo
		if ((e.getSource() == this.aceptar) && (this.accion.equals("guardar"))) {
			this.getVentana().procesarClaseSeleccionarMetodo();
		}
		if ((e.getSource() == this.cancelar) && (this.accion.equals("guardar"))) {
			this.getVentana().iniciarNuevaVisualizacionSelecMetodo();
		}
		
		//	Cuando el di�logo viene de lanzar ejecucion
		if ((e.getSource() == this.aceptar) && (this.accion.equals("lanzar"))) {
			this.getVentana().procesarClaseLanzarEjecucion();
		}
		if ((e.getSource() == this.cancelar) && (this.accion.equals("lanzar"))) {
			this.getVentana().introducirParametros();
		}

		if (this.accion.equals("cargarClase")) {
			this.getVentana().gestionOpcionCargarClase();
		} else if (this.accion.equals("cargarAnimacion")) {
			this.getVentana().gestionOpcionCargarAnimacion();
		} else if (this.accion.equals("cargarAnimacionGIF")) {
			this.getVentana().gestionOpcionCargarAnimacionGIF();
		} else if (this.accion.equals("cierreVentana")) {
			this.getVentana().activarCierre();
		}
		d.dispose();
	}

}