/**
	Opci�n que permite la configuraci�n de mantenimiento de ficheros intermedios usados por la aplicaci�n
	
	@author Antonio P�rez Carrasco
	@version 2006-2007
*/

package opciones;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.IOException;
import org.w3c.dom.*;


import utilidades.*;

public class OpcionIdioma extends Opcion
{
	private static final long serialVersionUID = 1171;


	String idioma;			


	/**
		Constructor: genera una nueva opci�n con los valores por defecto
	*/
	public OpcionIdioma()
	{
		this("es");
	}

	/**
		Constructor: genera una nueva opci�n con los valores dados con par�metros
		
		@param idiomaId c�digo de idioma
	*/
	public OpcionIdioma(String idiomaId)
	{
		super("OpcionIdioma");
		this.idioma=idiomaId;
	}

	/**
		Almacena los valores de la opci�n
		
		@param idiomaId c�digo de idioma
	*/
	public void set(String idiomaId)
	{
		this.idioma=idiomaId;
	}

	
	/**
		Almacena los valores de la opci�n
		
		@param idiomaId c�digo de idioma
	*/
	public String get()
	{
		return this.idioma;
	}
	
	
	public Element getRepresentacionElement(Document d)
	{
		Element e=d.createElement("OpcionIdioma");
		
		Element e01=d.createElement("idioma");
		e01.setAttribute("valor",this.idioma);
		
		e.appendChild(e01);
	
		return e;
	}
	
	
	public void setValores(Element e)
	{
		Element elements[]=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("idioma"));
		this.idioma=elements[0].getAttribute("valor");
	}
	

	
	/**
		Gestiona la lectura desde fichero
	*/
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
	{
		this.idioma=stream.readUTF();
	}

	/**
		Gestiona la escritura a fichero
	*/
	private void writeObject(ObjectOutputStream stream) throws IOException
	{
		stream.writeUTF(this.idioma);
	}
}