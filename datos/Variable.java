package datos;

public class Variable {
	
	String nombre=null;
	String tipo=null;
	int dim=-1;
	
	public Variable(String nombre, String tipo, int dim)
	{
		this.nombre=nombre;
		this.tipo=tipo;
		this.dim=dim;
	}
	
	public Variable(String nombre, String tipo)
	{
		this(nombre,tipo,0);
	}
	
	public Variable(String nombre)
	{
		this(nombre,null,0);
	}
	
	public void setTipo(String tipo)
	{
		this.tipo=tipo;	
	}
	
	public void setDimensiones(int d)
	{
		this.dim=d;
	}
	
	
	public String getNombre()
	{
		return this.nombre;
	}
	
	public String getTipo()
	{
		return this.tipo;
	}
	
	public int getDimensiones()
	{
		return this.dim;
	}

	
	
	public static String getTipo(Variable[] v,String n)
	{
		for (int i=0; i<v.length; i++)
		{
			if (n.equals(v[i].getNombre()))
				return v[i].getTipo();
		}
		return null;
	}
	
	public static int getDimensiones(Variable[] v,String n)
	{
		for (int i=0; i<v.length; i++)
		{
			if (n.equals(v[i].getNombre()))
				return v[i].getDimensiones();
		}
		return -1;
	}
}
