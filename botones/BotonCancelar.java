package botones;

import conf.*;
import utilidades.*;

/**
 * Bot�n que sirve para cancelar una acci�n o hacer desaparecer sin ejecutar
 * acci�n alguna un cuadro de di�logo
 * 
 * @author Antonio P�rez Carrasco
 * @author David Pastor Herranz
 */
public class BotonCancelar extends BotonTexto {

	private static final long serialVersionUID = 2031540017740802055L;

	/**
	 * Genera un nuevo bot�n de cancelaci�n.
	 */
	public BotonCancelar() {
		super(Texto.get("BOTONCANCELAR", Conf.idioma));
	}
}