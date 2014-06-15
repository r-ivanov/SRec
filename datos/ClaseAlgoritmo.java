package datos;

import java.util.ArrayList;

public class ClaseAlgoritmo
{
	// Representa una clase procesada, sólo contiene métodos que son aptos para ser visualizables,
	// no contiene todos los métodos de la clase original
	String path;
	String nombre;
	String nombre_id;
	String nombre_id2;
	
	ArrayList<MetodoAlgoritmo> metodos=new ArrayList<MetodoAlgoritmo>(0);

	public ClaseAlgoritmo (String path, String nombre, String id, String id2)
	{
		this.path=path;
		this.nombre=nombre;
		this.nombre_id=id;
		this.nombre_id2=id2;
	}
	
	public ClaseAlgoritmo (String path, String nombre, String id)
	{
		this(path,nombre,id,null);
	}
	
	public ClaseAlgoritmo (String path, String nombre)
	{
		this(path,nombre,null,null);
	}

	public String getNombre()
	{
		return this.nombre;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public void addMetodo(String nombre, String tipo, int dimTipo, String nombresParametros[], String tiposParametros[], int dimParametros[])
	{
		MetodoAlgoritmo m=new MetodoAlgoritmo(nombre, tipo, dimTipo, nombresParametros, tiposParametros, dimParametros);
		
		this.addMetodo(m);
	}
	
	public void addMetodo(MetodoAlgoritmo m)
	{
		// Buscamos que no exista ya un método igual: si existe, reemplazamos
		String tiposParam[]=m.getTiposParametros();
		String nombreParam=m.getNombre();
		
		if (this.metodos.size()==0)
			m.setMarcadoPrincipal(true);

		boolean sonIguales=false;		
		int i=0;
		while (i<this.metodos.size() && sonIguales==false)
		{
			sonIguales=true;
			String tipos2Param[]=this.getMetodo(i).getTiposParametros();
			String nombre2Param=this.getMetodo(i).getNombre();
			
			if (nombre2Param.equals(nombreParam) && tiposParam.length==tipos2Param.length)
			{
				for (int j=0; j<tiposParam.length; j++)
				{
					if (!tiposParam[j].equals(tipos2Param[j]))
						sonIguales=false;
				}
			}
			else
				sonIguales=false;
			i++;
		}
		if (sonIguales)
		{

			this.metodos.remove(i-1);	
			this.metodos.add(i-1,m);
		}
		else
			this.metodos.add(m);
	}
	
	private void delMetodo(int i)
	{
		this.metodos.remove(i);
	}
	
	private void delMetodo(MetodoAlgoritmo m)
	{
		String tiposParam[]=m.getTiposParametros();
		String nombreParam=m.getNombre();
	
		for (int i=0; i<this.metodos.size(); i++)
		{
			String tipos2Param[]=this.getMetodo(i).getTiposParametros();
			String nombre2Param=this.getMetodo(i).getNombre();
			
			if (nombre2Param.equals(nombreParam) && tiposParam.length==tipos2Param.length)
			{
				boolean sonIguales=true;
				for (int j=0; j<tiposParam.length; j++)
				{
					if (!tiposParam[i].equals(tipos2Param[i]))
						sonIguales=false;
				}
				if (sonIguales)
					this.metodos.remove(i);
			}
		}
	}
	
	public MetodoAlgoritmo getMetodo(int i)
	{
		return this.metodos.get(i);
	}
	
	public MetodoAlgoritmo getMetodoID(int i)
	{
		for (MetodoAlgoritmo m : this.metodos)
		{
			if (m.getID()==i)
				return m;
		}

		return null;
	}
	
	public MetodoAlgoritmo getMetodoPrincipal()
	{
		for (int i=0; i<this.metodos.size(); i++)
			if (this.metodos.get(i).getMarcadoPrincipal())
				return this.metodos.get(i);
	
	
		return null;
	}
	
	public ArrayList<MetodoAlgoritmo> getMetodos()
	{
		return this.metodos;
	}
	
	
	public ArrayList<MetodoAlgoritmo> getMetodos(String nombre)
	{
		ArrayList<MetodoAlgoritmo> metodosNombre=new ArrayList<MetodoAlgoritmo>();
		
		for (int i=0; i<this.metodos.size(); i++)
			if (this.metodos.get(i).getNombre().equals(nombre))
				metodosNombre.add(this.metodos.get(i));

		return metodosNombre;
	}

	
	public ArrayList<MetodoAlgoritmo> getMetodosProcesados()
	{
		ArrayList<MetodoAlgoritmo> mp=new ArrayList<MetodoAlgoritmo>();
		
		for (int i=0; i<this.metodos.size(); i++)
			if (this.metodos.get(i).getMarcadoProcesar())
				mp.add(this.metodos.get(i));
				
		return mp;
	}
	
	
	public int getNumMetodos()
	{
		return this.metodos.size();
	}
	
	public int getNumMetodosProcesados()
	{
		int contador=0;
		
		for (int i=0; i<this.metodos.size(); i++)
			if (this.metodos.get(i).getMarcadoProcesar())
				contador++;
		
		return contador;
	}
	
	public String getId()
	{
		return this.nombre_id;
	}
	
	public String getId2()
	{
		return this.nombre_id2;
	}
	
	public void setId(String id)
	{
		this.nombre_id=id;
	}
	
	public void setId2(String id2)
	{
		this.nombre_id2=id2;
	}
	
	
	public void borrarMarcadoPrincipal()
	{
		for (int i=0; i<this.metodos.size(); i++)
			this.metodos.get(i).setMarcadoPrincipal(false);

	}
	
	public void visualizarClase()
	{
		System.out.println("\nClaseAlgoritmo\n--------------\n");
		System.out.println("Path: "+this.path);
		System.out.println("Nombre: "+this.nombre);
		System.out.println("NombreID: "+this.nombre_id);
		System.out.println("NombreID2: "+this.nombre_id2);
		
		System.out.println("Métodos: "+this.metodos.size());
		
		for (int i=0; i<this.metodos.size(); i++)
		{
			MetodoAlgoritmo m=this.metodos.get(i);
			System.out.println("\n- Nombre: "+m.getNombre()+ (m.getMarcadoPrincipal() ? "    PPAL" : "") + (m.getMarcadoVisualizar() ? "   VISIBLE" : ""));
			System.out.print("  Tipo: "+m.getTipo() );
			for (int k=0; k<m.getDimTipo(); k++)
				System.out.print("[]");
			
			System.out.println("  N. parametros: "+m.getNumeroParametros());
			for (int j=0; j<m.getNumeroParametros(); j++)
			{
				System.out.print("    "+m.getNombreParametro(j)+": "+m.getTipoParametro(j)+" valor: "+m.getParamValor(j)+"   "+m.getVisibilidadEntrada(j));
				for (int k=0; k<m.getDimParametro(j); k++)
					System.out.print("[]");
				System.out.println();	
			}
			
			if (m.getTipo().equals("void"))
			{
				for (int j=0; j<m.getNumeroParametros(); j++)
				{
					System.out.print("    "+m.getNombreParametro(j)+": "+m.getTipoParametro(j)+" valor: "+m.getParamValor(j)+"   "+m.getVisibilidadSalida(j));
					System.out.println();
				}
			}
			else
				System.out.print("    retorno: "+m.getVisibilidadSalida(0));
			
		}
		
		
	}
	
	
	
	public boolean potencialMetodoDYV()
	{
		for (MetodoAlgoritmo m : metodos)
		{
			int[] dimParam=m.getDimParametros();
			
			for (int i=0; i<dimParam.length; i++)
				if (dimParam[i]>0)
					return true;
		}

		return false;
	}

}