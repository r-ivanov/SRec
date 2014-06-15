package botones;

import conf.*;
import utilidades.*;

/**
 * Botón que sirve para validar una acción o inserción de datos.
 * 
 * @author Antonio Pérez Carrasco
 * @author David Pastor Herranz
 */
public class BotonAceptar extends BotonTexto {

	private static final long serialVersionUID = 4435173168697233536L;

	/**
	 * Genera un nuevo botón de validación de acción o inserción de datos.
	 */
	public BotonAceptar() {
		super(Texto.get("BOTONACEPTAR", Conf.idioma));
	}
}