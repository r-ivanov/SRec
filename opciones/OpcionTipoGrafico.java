/**
	Opción que permite la configuración de mantenimiento de ficheros intermedios usados por la aplicación
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package opciones;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.IOException;
import org.w3c.dom.*;



import utilidades.*;


public class OpcionTipoGrafico extends Opcion
{
	private static final long serialVersionUID = 1013;

	String tipos[];
	String extensiones[][];
	boolean usadoUltimaVez[];		
	
	final int numTipos=3;
	/**
		Constructor: crea una nueva opción vacía
	*/
	public OpcionTipoGrafico()
	{
		super("OpcionTipoGrafico");
		
		this.tipos=new String[numTipos];
		this.tipos[0]="JPEG";	// (Joint Photographic Experts Group)
		this.tipos[1]="GIF";	// (Graphics Interchange Format)
		this.tipos[2]="PNG";	// (Portable Network Graphic)
		
		this.extensiones=new String[numTipos][];
		
		this.extensiones[0]=new String[2];
		this.extensiones[0][0]="jpg";
		this.extensiones[0][1]="jpeg";
		
		this.extensiones[1]=new String[1];
		this.extensiones[1][0]="gif";
		
		this.extensiones[2]=new String[1];
		this.extensiones[2][0]="png";
				
		this.usadoUltimaVez=new boolean[numTipos];
		this.usadoUltimaVez[0]=false;
		this.usadoUltimaVez[1]=false;
		this.usadoUltimaVez[2]=true;
	}
	
	
	
	public String[] getTipos(boolean valor)
	{
		// Comprobación de que los valores almacenados en this.usadoUltimaVez son coherentes
		int posicionesTrue=0;
		for (int i=0; i<this.usadoUltimaVez.length; i++)
			if (this.usadoUltimaVez[i])
				posicionesTrue++;
				
		// Si error...
		if (posicionesTrue!=1)
		{
			boolean encontradouno=false;
			for (int i=0; i<this.usadoUltimaVez.length; i++)
				if (!encontradouno && this.usadoUltimaVez[i])
					encontradouno=true;
				else if (encontradouno)
					this.usadoUltimaVez[i]=false;
		}
		
		String []tiposRetorno=new String [ (valor) ? 1 : this.usadoUltimaVez.length];
		int j=0;
		for (int i=0; i<this.usadoUltimaVez.length; i++)
			if (this.usadoUltimaVez[i]==valor)
			{
				tiposRetorno[j]=this.tipos[i];
				j++;
			}
		return tiposRetorno;
	}
	
	
	public String[][] getExtensiones()
	{
		String[][] extRetorno=new String[numTipos][];
		int j=0;
		
		for (int i=0; i<this.usadoUltimaVez.length; i++)
		{
			if (!this.usadoUltimaVez[i])
			{
				extRetorno[j]=new String[this.extensiones[i].length];
				for (int k=0; k<this.extensiones[i].length; k++)
					extRetorno[j][k]=new String(this.extensiones[i][k]);
				j++;
			}
		}
		
		for (int i=0; i<this.usadoUltimaVez.length; i++)
		{
			if (this.usadoUltimaVez[i])
			{
				extRetorno[j]=new String[this.extensiones[i].length];
				for (int k=0; k<this.extensiones[i].length; k++)
					extRetorno[j][k]=new String(this.extensiones[i][k]);
				j++;
			}
		}
		return extRetorno;
	}


	public void setTipoUsado(String extension)
	{
		int posicAnterior=0;
		for (int i=0; i<this.usadoUltimaVez.length; i++)
			if (this.usadoUltimaVez[i])
			{
				posicAnterior=i;
				this.usadoUltimaVez[i]=false;
			}
	
		boolean encontrado=false;
		for (int i=0; i<this.extensiones.length; i++)
			for (int j=0; j<this.extensiones[i].length; j++)
				if (this.extensiones[i][j].toLowerCase().equals(extension.toLowerCase()))
				{
					this.usadoUltimaVez[i]=true;
					encontrado=true;
				}
		
		if (!encontrado)
			this.usadoUltimaVez[posicAnterior]=true;
					
	}
	
	

	
	
	public Element getRepresentacionElement(Document d)
	{
		Element e=d.createElement("OpcionTipoGrafico");
		
		for (int i=0; i<this.tipos.length; i++)
		{
			Element t=d.createElement("tipo");
			t.setAttribute("nombre",this.tipos[i]);
			t.setAttribute("ultimavez",""+this.usadoUltimaVez[i]);
			e.appendChild(t);
		}
		
		return e;
	}
	
	
	public void setValores(Element e)
	{
		Element elements[]=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("tipo"));
		
		for (int i=0; i<elements.length; i++)
		{
			String tipo=elements[i].getAttribute("nombre");
			for (int j=0; j<this.tipos.length; j++)
				if (this.tipos[j].equals(tipo))
					this.usadoUltimaVez[j]=(elements[i].getAttribute("ultimavez").equals("true") ? true : false);
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	/**
		Gestiona la lectura desde fichero
	*/
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
	{
		/*this.dir=stream.readUTF();
		this.ficheros=new String[TAMANO];
		for (int i=0; i<TAMANO; i++)
			this.ficheros[i]=stream.readUTF();*/
	}

	/**
		Gestiona la escritura a fichero
	*/
	private void writeObject(ObjectOutputStream stream) throws IOException
	{
		/*stream.writeUTF(this.dir);
		for (int i=0; i<TAMANO; i++)
			stream.writeUTF(this.ficheros[i]);*/
	}

}