/**
	Representa una opción modificable y que se almacena en fichero
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package opciones;

import java.io.Serializable;
import org.w3c.dom.*;

public abstract class Opcion implements Serializable
{
	private static final long serialVersionUID = 1000;
	String nombre;

	/**
		Constructor: crea una nueva opción vacía
	*/
	public Opcion()
	{
		this("Sin nombre");
	}

	/**
		Constructor: crea una nueva opción con un determinado nombre
		
		@param nombre nombre para la acción
	*/
	public Opcion(String nombre)
	{
		this.nombre=nombre;
	}

	/**
		Devuelve el nombre de la opción
		
		@return nombre de la operación
	*/
	public String getNombre()
	{
		return this.nombre;
	}
	
	public abstract Element getRepresentacionElement(Document d);
	
	public abstract void setValores(Element e);
	
}