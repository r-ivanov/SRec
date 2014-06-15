package botones;

import conf.*;
import utilidades.*;

/**
 * Bot�n que sirve para validar una acci�n o inserci�n de datos.
 * 
 * @author Antonio P�rez Carrasco
 * @author David Pastor Herranz
 */
public class BotonAceptar extends BotonTexto {

	private static final long serialVersionUID = 4435173168697233536L;

	/**
	 * Genera un nuevo bot�n de validaci�n de acci�n o inserci�n de datos.
	 */
	public BotonAceptar() {
		super(Texto.get("BOTONACEPTAR", Conf.idioma));
	}
}