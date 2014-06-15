/**
	Botón que sirve para cancelar una acción o hacer desaparecer sin ejecutar acción alguna un cuadro de diálogo
	
	@author Antonio Pérez Carrasco
	@version 2006-2008
*/
package botones;


import conf.*;
import utilidades.*;

public class BotonCancelar extends Boton
{
	static final long serialVersionUID=16;	

	/**
		Constructor: genera un nuevo botón de cancelación
	*/
	public BotonCancelar()
	{
		//super("imagenes/boton_cancelar_verde.gif","imagenes/boton_cancelar_naranja.gif","imagenes/boton_cancelar_rojo.gif",65,21);
		super(Texto.get("BOTONCANCELAR",Conf.idioma));
	}
}