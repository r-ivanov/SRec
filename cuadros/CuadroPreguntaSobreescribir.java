package cuadros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import conf.*;
import opciones.*;
import datos.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite construir un cuadro de pregunta para que el usuario decida si desea
 * sobreescribir un recurso ya existente.
 * 
 * @author Antonio Pérez Carrasco
 * @author David Pastor Herranz
 */
public class CuadroPreguntaSobreescribir extends CuadroPregunta implements
		ActionListener, KeyListener {

	private Object objeto;
	private String param;
	private JComponent componenteUI;

	/**
	 * Genera un cuadro de pregunta para que el usuario decida si desea
	 * sobreescribir un recurso ya existente.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 * @param param
	 *            Permite pasar un parámetro al cuadro, para que pueda usarlo a
	 *            posteriori como parámetro de alguno de los métodos del objeto.
	 * @param o
	 *            Permite pasar un objeto al cuadro, para que pueda invocar
	 *            acciones sobre el mismo.
	 * @param c
	 *            Permite pasar un componente de UI al cuadro, para que pueda
	 *            usarse como parte de los eventos de acción para determinados
	 *            métodos.
	 */
	public CuadroPreguntaSobreescribir(Ventana ventana, String param, Object o,
			JComponent c) {
		super(ventana, Texto.get("PREG_ARCHEXIST", Conf.idioma), Texto.get(
				"PREGMEN_ARCHEXIST", Conf.idioma), Texto.get("BOTONACEPTAR",
				Conf.idioma), Texto.get("BOTONCANCELAR", Conf.idioma));
		this.param = param;
		this.objeto = o;
		this.componenteUI = c;
		this.start();
	}

	/**
	 * Genera un cuadro de pregunta para que el usuario decida si desea
	 * sobreescribir un recurso ya existente.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 * @param o
	 *            Permite pasar un objeto al cuadro, para que pueda invocar
	 *            acciones sobre el mismo.
	 */
	public CuadroPreguntaSobreescribir(Ventana ventana, Object o) {
		this(ventana, "", o, null);
	}

	/**
	 * Gestiona los eventos de acción.
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.aceptar) {
			if (this.objeto.getClass().getCanonicalName()
					.contains("AlmacenadorTraza")) {
				((AlmacenadorTraza) (this.objeto)).ejecutar();
			} else if (this.objeto.getClass().getCanonicalName()
					.contains("FotografoArbol")) {
				if (this.param.charAt(0) == '1') {
					((FotografoArbol) (this.objeto)).hacerCapturaUnica2(
							this.componenteUI);
				} else if (this.param.charAt(0) == 'A') {
					((FotografoArbol) (this.objeto)).capturarAnimacionGIF2(
							this.componenteUI,
							Integer.parseInt(this.param.substring(1)));
				}
			} else if (this.objeto.getClass().getCanonicalName()
					.contains("GestorOpciones")) {
				((GestorOpciones) (this.objeto)).crearArchivo(this.param);
			} else if (this.objeto.getClass().getCanonicalName()
					.contains("Ventana")) {
				if (this.param.equals("html")) {
					((Ventana) (this.objeto)).guardarTraza2(this.param);
				} else if (this.param.equals("gif")) {
					((Ventana) (this.objeto)).cargarGIF2();
				}
			} else if (this.objeto.getClass().getCanonicalName()
					.contains("CuadroNuevaClase")) {
				((CuadroNuevaClase) (this.objeto)).archivoCorrecto();
			}

		}

		this.d.setVisible(false);
	}

}