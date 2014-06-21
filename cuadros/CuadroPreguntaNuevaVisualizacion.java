package cuadros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import conf.*;
import datos.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite construir un cuadro de pregunta para que el usuario confirme si
 * realmente desea descartar la visualización en curso.
 * 
 * @author Antonio Pérez Carrasco
 * @author David Pastor Herranz
 */
public class CuadroPreguntaNuevaVisualizacion extends CuadroPregunta implements
		ActionListener, KeyListener {

	private String accion;

	/**
	 * Genera un cuadro de pregunta para que el usuario confirme si realmente
	 * desea descartar la visualización en curso.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 * 
	 * @param accion
	 *            Acción que debe ejecutarse si el usuario descarta la
	 *            visualización.
	 */
	public CuadroPreguntaNuevaVisualizacion(Ventana ventana, String accion) {
		super(ventana, Texto.get("PREG_DESCART", Conf.idioma), Texto.get(
				"PREGMEN_DESCART", Conf.idioma), Texto.get("BOTONACEPTAR",
				Conf.idioma), Texto.get("BOTONCANCELAR", Conf.idioma));
		this.accion = accion;
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
			if (this.accion.equals("cargar")) {
				new CargadorTraza();
			} else if (this.accion.equals("procesar")) {
				new Preprocesador();
			} else if (this.accion.equals("procesar de nuevo")) {
				String direccionCompleta = this.ventana.getClase().getPath();
				String path[] = new String[2];
				path[0] = direccionCompleta.substring(0,
						direccionCompleta.lastIndexOf("\\") + 1);
				path[1] = direccionCompleta.substring(
						direccionCompleta.lastIndexOf("\\") + 1,
						direccionCompleta.length());
				new Preprocesador(path);
				this.ventana.setClasePendienteProcesar(false);
			} else if (this.accion.equals("cargarGIF")) {
				this.ventana.cargarGIF();
			}
		}

		this.d.setVisible(false);
	}
}