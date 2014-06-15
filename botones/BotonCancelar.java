/**
	Bot�n que sirve para cancelar una acci�n o hacer desaparecer sin ejecutar acci�n alguna un cuadro de di�logo
	
	@author Antonio P�rez Carrasco
	@version 2006-2008
*/
package botones;


import conf.*;
import utilidades.*;

public class BotonCancelar extends Boton
{
	static final long serialVersionUID=16;	

	/**
		Constructor: genera un nuevo bot�n de cancelaci�n
	*/
	public BotonCancelar()
	{
		//super("imagenes/boton_cancelar_verde.gif","imagenes/boton_cancelar_naranja.gif","imagenes/boton_cancelar_rojo.gif",65,21);
		super(Texto.get("BOTONCANCELAR",Conf.idioma));
	}
}