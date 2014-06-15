package datos;

import java.util.ArrayList;

import utilidades.ServiciosString;


public class DatosMetodoBasicos
{
	String nombre;
	
	ArrayList<String> nombreParamE;
	ArrayList<String> tipoParamE;
	
	ArrayList<String> nombreParamS;
	ArrayList<String> tipoParamS;
	
	int[]dimE;
	int[]dimS;
	
	boolean retorno;	// A true si el método en cuestión devuelve un valor, a false si devuelve void
	
	boolean metodoPrincipal;	// a true si es el método que inició la ejecución
	
	boolean tecnica=false;
	
	boolean metodoVisible;
	boolean visibilidadE[];
	boolean visibilidadS[];


	public DatosMetodoBasicos(String nombre, int numParamE, int numParamS, boolean retorno)
	{
		this.nombre=nombre;
		
		this.visibilidadE=new boolean[numParamE];
		this.visibilidadS=new boolean[numParamS];
		
		this.dimE=new int[visibilidadE.length];
		this.dimS=new int[visibilidadS.length];
		
		for (int i=0; i<visibilidadE.length; i++)
			visibilidadE[i]=false;
		for (int i=0; i<visibilidadS.length; i++)
			visibilidadS[i]=false;
		
		this.retorno=retorno;	
		
		this.nombreParamE=new ArrayList<String>(0);
		this.tipoParamE=new ArrayList<String>(0);
		this.nombreParamS=new ArrayList<String>(0);
		this.tipoParamS=new ArrayList<String>(0);
		
	}
	
	public void addParametroEntrada(String nombre, String tipo, int dim, boolean visible)
	{
		nombreParamE.add(nombre);
		tipoParamE.add(tipo);
		dimE[nombreParamE.size()-1]=dim;
		visibilidadE[nombreParamE.size()-1]=visible;
	}
	
	public void addParametroSalida(String nombre, String tipo, int dim, boolean visible)
	{
		nombreParamS.add(nombre);
		tipoParamS.add(tipo);
		dimS[nombreParamS.size()-1]=dim;
		visibilidadS[nombreParamS.size()-1]=visible;
	}
	
	public void setEsPrincipal(boolean valor)
	{
		this.metodoPrincipal=valor;
	}
	
	public void setEsVisible(boolean valor)
	{
		this.metodoVisible=valor;
	}
	
	public boolean getEsVisible()
	{
		return this.metodoVisible;
	}
	
	public int getNumParametrosE()
	{
		return visibilidadE.length;
	}
	
	public int getNumParametrosS()
	{
		return visibilidadS.length;
	}
	
	public String getNombre()
	{
		return this.nombre;
	}
	
	public String getNombreParametroE (int numParametro)
	{
		return this.nombreParamE.get(numParametro);
	}
	
	public String[] getNombreParametrosE ()
	{
		String []nombres=new String[this.nombreParamE.size()];
		
		for (int i=0; i<this.nombreParamE.size(); i++)
			nombres[i]=this.nombreParamE.get(i);
		
		return nombres;
	}
	
	public String getNombreParametroS (int numParametro)
	{
		return this.nombreParamS.get(numParametro);
	}
	
	public String[] getNombreParametrosS ()
	{
		String []nombres=new String[this.nombreParamS.size()];
		
		for (int i=0; i<this.nombreParamS.size(); i++)
			nombres[i]=this.nombreParamS.get(i);
			
		return nombres;
	}
	
	public String getTipoParametroE (int numParametro)
	{
		return this.tipoParamE.get(numParametro);
	}
	
	public String[] getTipoParametrosE ()
	{
		String []tipos=new String[this.tipoParamE.size()];
		
		for (int i=0; i<tipos.length; i++)
			tipos[i]=new String(this.tipoParamE.get(i));
		
		return tipos;
	}
	
	public String getTipoParametroS (int numParametro)
	{
		return this.tipoParamS.get(numParametro);
	}
	
	public String[] getTipoParametrosS ()
	{
		String []tipos=new String[this.tipoParamS.size()];
		
		for (int i=0; i<tipos.length; i++)
			tipos[i]=new String(this.tipoParamS.get(i));
		
		return tipos;
	}
	
	public int getDimParametroE (int numParametro)
	{
		return this.dimE[numParametro];
	}
	
	public int getDimParametroS (int numParametro)
	{
		return this.dimS[numParametro];
	}
	
	public boolean esMetodoConRetorno()
	{
		return this.retorno;
	}
	
	public String getTipo()
	{
		if (!this.retorno)
			return "void";
		else
			return tipoParamS.get(0);
	}
	
	public int getDimTipo()
	{
		if (!this.retorno)
			return 0;
		else
			return dimS[0];
	}
	
	public boolean[] getVisibilidadE ()
	{
		return visibilidadE;
	}
	
	public boolean getVisibilidadE (int i)
	{
		return visibilidadE[i];
	}
	
	public void setVisibilidadE(boolean v,int i)
	{
		visibilidadE[i]=v;
	}
	
	public void setVisibilidadS(boolean v,int i)
	{
		visibilidadS[i]=v;
	}
	
	public boolean[] getVisibilidadS ()
	{
		return visibilidadS;
	}
	
	public boolean getVisibilidadS (int i)
	{
		return visibilidadS[i];
	}
	
	public int[] getDimE ()
	{
		return dimE;
	}
	
	public int[] getDimS ()
	{
		return dimS;
	}
	
	public boolean getEsPrincipal()
	{
		return metodoPrincipal;
	}
	
	public String getInterfaz()
	{
		String interfaz=this.nombre+ "(";
		
		String tipoParam[]=this.getTipoParametrosE();
		String nombreParam[]=this.getNombreParametrosE();
		int dimParam[]= this.getDimE();
		
		for (int i=0; i<tipoParam.length; i++)
		{
			interfaz=interfaz+" "+tipoParam[i];
			if (dimParam[i]>0)
				interfaz=interfaz+" "+ServiciosString.cadenaDimensiones(dimParam[i]);
			interfaz=interfaz+" "+nombreParam[i];
			if (i<(tipoParam.length-1))
				interfaz=interfaz+",";
		}
		
		interfaz=interfaz+" ) [ "+this.getTipo();
		if (this.getDimTipo()>0)
		{
			interfaz=interfaz+" "+ServiciosString.cadenaDimensiones(this.getDimTipo());
		}
		interfaz=interfaz+" ]";
		return interfaz;
	}
}