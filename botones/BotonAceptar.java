/**
	Botón que sirve para validar una acción o inserción de datos
	
	@author Antonio Pérez Carrasco
	@version 2006-2008
*/
package botones;


import conf.*;
import utilidades.*;

public class BotonAceptar extends Boton
{
	static final long serialVersionUID=15;	

	/**
		Constructor: genera un nuevo botón de validación de acción o inserción de datos
	*/
	public BotonAceptar()
	{
		//super("imagenes/boton_aceptar_verde.gif","imagenes/boton_aceptar_naranja.gif","imagenes/boton_aceptar_rojo.gif",65,21);
		super(Texto.get("BOTONACEPTAR",Conf.idioma));
	}
}