package botones;

import conf.*;
import utilidades.*;

/**
 * Botón que sirve para cancelar una acción o hacer desaparecer sin ejecutar
 * acción alguna un cuadro de diálogo
 * 
 * @author Antonio Pérez Carrasco
 * @author David Pastor Herranz
 */
public class BotonCancelar extends BotonTexto {

	private static final long serialVersionUID = 2031540017740802055L;

	/**
	 * Genera un nuevo botón de cancelación.
	 */
	public BotonCancelar() {
		super(Texto.get("BOTONCANCELAR", Conf.idioma));
	}
}