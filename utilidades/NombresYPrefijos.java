/**
	Almacena una correspondencia entre un nombre de un método y su prefijo
	
*/
package utilidades;




public class NombresYPrefijos
{
	String nombre[];
	String prefijo[];
	
	public NombresYPrefijos()
	{
		this.nombre=new String[0];
		this.prefijo=new String[0];
	}
	
	public void add(String nombre, String prefijo)
	{
		if (this.nombre.length==0)
		{
			this.nombre =new String[1];
			this.prefijo=new String[1];
			
			this.nombre[0]=nombre;
			this.prefijo[0]=prefijo;
		}
		else
		{
			String nombreAux[]=new String[this.nombre.length+1];
			String prefijAux[]=new String[this.prefijo.length+1];
			
			for (int i=0; i<this.nombre.length; i++)
			{
				nombreAux[i]=this.nombre[i];
				prefijAux[i]=this.prefijo[i];
			}
			
			nombreAux[this.nombre.length]=nombre;
			prefijAux[this.nombre.length]=prefijo;
			
			this.nombre =nombreAux;
			this.prefijo=prefijAux;
		}
	}
	
	public String[] getCorrespondencia(int i)
	{
		String [] c=null;
		if (i<this.nombre.length)
		{
			c=new String[2];
			c[0]=this.nombre[i];
			c[1]=this.prefijo[i];
		}
		return c;
	}
	
	public String getPrefijo(String nombre)
	{
		for (int i=0; i<this.nombre.length; i++)
			if (this.nombre[i].equals(nombre))
				return this.prefijo[i];
		return "No encontrado";
	}
	
	public int getLongitudPrefijoMasLargo()
	{
		String prefijoLargo="";
		
		for (int i=0; i<this.prefijo.length; i++)
			if (this.prefijo[i].length()>prefijoLargo.length())
				prefijoLargo=this.prefijo[i];
		
		return prefijoLargo.length();
	}
}
