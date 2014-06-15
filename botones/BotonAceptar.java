/**
	Bot�n que sirve para validar una acci�n o inserci�n de datos
	
	@author Antonio P�rez Carrasco
	@version 2006-2008
*/
package botones;


import conf.*;
import utilidades.*;

public class BotonAceptar extends Boton
{
	static final long serialVersionUID=15;	

	/**
		Constructor: genera un nuevo bot�n de validaci�n de acci�n o inserci�n de datos
	*/
	public BotonAceptar()
	{
		//super("imagenes/boton_aceptar_verde.gif","imagenes/boton_aceptar_naranja.gif","imagenes/boton_aceptar_rojo.gif",65,21);
		super(Texto.get("BOTONACEPTAR",Conf.idioma));
	}
}