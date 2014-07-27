package datos;

/**
 * Representa una variable.
 */
public class Variable {
	
	private String nombre=null;
	private String tipo=null;
	private int dim=-1;
	
	/**
	 * Crea una nueva instacia del objeto.
	 * 
	 * @param nombre Nombre de la variable
	 * @param tipo Tipo de la variable
	 * @param dim Dimensiones de la variable
	 */
	public Variable(String nombre, String tipo, int dim)
	{
		this.nombre=nombre;
		this.tipo=tipo;
		this.dim=dim;
	}
	
	/**
	 * Establece el tipo de la variable
	 * 
	 * @param tipo Tipo de la variable.
	 */
	public void setTipo(String tipo)
	{
		this.tipo=tipo;	
	}
	
	/**
	 * Establece las dimensiones de la variable
	 * @param d Dimensiones de la variable.
	 */
	public void setDimensiones(int d)
	{
		this.dim=d;
	}
	
	/**
	 * Devuelve el nombre de la variable.
	 * 
	 * @return Nombre de la variable.
	 */
	public String getNombre()
	{
		return this.nombre;
	}
	
	/**
	 * Devuelve el tipo de la variable.
	 * 
	 * @return Tipo de la variable.
	 */
	public String getTipo()
	{
		return this.tipo;
	}
	
	/**
	 * Devuelve las dimensiones de la variable.
	 * 
	 * @return Dimensiones de la variable.
	 */
	public int getDimensiones()
	{
		return this.dim;
	}
	
	/**
	 * Dada un array de Variables, devuelve el tipo de la que se corresponde con el nombre especificado.
	 * 
	 * @param v Array de variables.
	 * @param n Nombre de la variable.
	 * 
	 * @return Tipo de la variable especificada.
	 */
	public static String getTipo(Variable[] v,String n)
	{
		for (int i=0; i<v.length; i++)
		{
			if (n.equals(v[i].getNombre()))
				return v[i].getTipo();
		}
		return null;
	}
	
	/**
	 * Dada un array de Variables, devuelve las dimensiones de la que se corresponde con el nombre especificado.
	 * 
	 * @param v Array de variables.
	 * @param n Nombre de la variable.
	 * 
	 * @return Dimensiones de la variable especificada.
	 */
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
