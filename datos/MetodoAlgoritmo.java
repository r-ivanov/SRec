package datos;


import java.util.ArrayList;

import utilidades.*;


public class MetodoAlgoritmo
{
	static int codID=0;
	
	int id=0;
	
	String nombre;
	String tipo;
	int dimRetorno;
	
	String paramNombre[];
	String paramTipo[];
	int paramDimension[];		// 0=valor único, 1=array, 2=matriz		Es un array, una posición para cada parámetro
	int paramRetorno[];			// 0=valor único, 1=array, 2=matriz		Es un array, una posición para cada parámetro (métodos void)
	
	int paramEstructura=0;		// Número del Parametro que alberga la estructura de datos
	
	int[] paramIndice=new int[4];		
				// 0= Número del Parametro que alberga la fila mínima que determina la parte sobre la que se actúa en cada llamada
				// 1= Número del Parametro que alberga la fila máxima que determina la parte sobre la que se actúa en cada llamada
				// 2= Número del Parametro que alberga la col. mínima que determina la parte sobre la que se actúa en cada llamada
				// 3= Número del Parametro que alberga la col. máxima que determina la parte sobre la que se actúa en cada llamada
	
	
	String paramValor[];		// Almacenamos el último valor que se empleó con cada parámetro en la última visualización de la actual sesión
	boolean metodoPrincipal;	// A true si el método fue el último que se escogió como método principal
	
	boolean visiblesEntrada[];
	boolean visiblesSalida[];
	
	boolean procesado;			// true si fue seleccionado para ser procesado
	boolean visto;				// true si fue seleccionado en "CuadroMetodosProcesados" para ser visto en la visualización anterior
	
	ArrayList<Integer> llamados=new ArrayList<Integer>();	// Metodos a los que se llama desde dentro de este método (ID)

	int tecnica=0;				//Tomará un valor en función de la técnica para la que ha sido procesado (REC o DYV)
	
	public static final int TECNICA_REC=1111; 
	public static final int TECNICA_DYV=1112;
	
	
	
	
	
	public MetodoAlgoritmo(String nombre, String tipo, int dimRetorno, String paramNombre[], String paramTipo[], int paramDim[])
	{
		this(nombre,tipo,dimRetorno,paramNombre,paramTipo,paramDim,TECNICA_REC);
	}
	
	
	public MetodoAlgoritmo(String nombre, String tipo, int dimRetorno, String paramNombre[], String paramTipo[], int paramDim[], int tecnica)
	{
		this.nombre=nombre;
		this.tipo=tipo;
		this.dimRetorno=dimRetorno;
		this.paramNombre=paramNombre;
		this.paramTipo=paramTipo;
		
		this.paramDimension=new int[this.paramNombre.length];
		this.visiblesEntrada=new boolean[this.paramNombre.length];
		this.visiblesSalida=new boolean[this.tipo.equals("void") ? this.paramNombre.length : 1];
		
		this.paramValor=new String[this.paramNombre.length];
		
		for (int i=0; i<paramDimension.length; i++)
			this.paramDimension[i]=paramDim[i];
	
		for (int i=0; i<visiblesEntrada.length; i++)
			this.visiblesEntrada[i]=true;
		
		for (int i=0; i<visiblesSalida.length; i++)
			this.visiblesSalida[i]=true;
			
		for (int i=0; i<paramValor.length; i++)
			this.paramValor[i]="";
		
		this.visto=true;
		
		this.id=codID;
		codID++;
	}

	public String getNombre()
	{
		return this.nombre;
	}
	
	public String getTipo()
	{
		return this.tipo;
	}
	
	public int getDimTipo()
	{
		return this.dimRetorno;
	}
	
	public String[] getNombreParametros()
	{
		return this.paramNombre;
	}
	
	public String getNombreParametro(int i)
	{
		if (i>=0 && i<paramNombre.length)
			return this.paramNombre[i];
		else
			return null;
	}
	
	public int getNumeroParametros()
	{
		return this.paramNombre.length;
	}
	
	public String[] getTiposParametros()
	{
		return this.paramTipo;
	}
	
	public String getTipoParametro(int i)
	{
		if (i>=0 && i<paramTipo.length)
			return this.paramTipo[i];
		else
			return null;
	}
	
	
	public int getDimParametro(int i)
	{
		if (i>=0 && i<paramDimension.length)
			return this.paramDimension[i];
		else
			return -1;
	}
	
	public int[] getDimParametros()
	{
		return this.paramDimension;
	}
	
	public boolean[] getVisibilidadEntrada()
	{
		return this.visiblesEntrada;
	}
	
	public boolean getVisibilidadEntrada(int i)
	{
		return this.visiblesEntrada[i];
	}
	
	public boolean[] getVisibilidadSalida()
	{
		return this.visiblesSalida;
	}
	
	public boolean getVisibilidadSalida(int i)
	{
		return this.visiblesSalida[i];
	}
	
	public boolean getMarcadoProcesar()
	{
		return this.procesado;
	}
	
	public boolean getMarcadoPrincipal()
	{
		return this.metodoPrincipal;
	}
	
	public boolean getMarcadoVisualizar()
	{
		return this.visto;
	}
	
	public String getRepresentacion()
	{
		String cadenaEtiqueta="";
	
		cadenaEtiqueta=this.getNombre()+" ( ";
		
		for (int j=0; j<this.getNumeroParametros(); j++)
		{
			cadenaEtiqueta=cadenaEtiqueta+this.getTipoParametro(j);
			
			cadenaEtiqueta=cadenaEtiqueta+ServiciosString.cadenaDimensiones( this.getDimParametro(j) );

			if (j<this.getNumeroParametros()-1)
				cadenaEtiqueta=cadenaEtiqueta+" , ";
			else
				cadenaEtiqueta=cadenaEtiqueta+" ) ";
		}
		
		cadenaEtiqueta=cadenaEtiqueta+" [ "+this.getTipo();
		cadenaEtiqueta=cadenaEtiqueta+ServiciosString.cadenaDimensiones( this.getDimTipo() );
		cadenaEtiqueta=cadenaEtiqueta+" ]";
		
		return cadenaEtiqueta;
	}
	
	public String getRepresentacionTotal()
	{
		String cadenaEtiqueta="";
	
		cadenaEtiqueta=this.getNombre()+" ( ";
		
		for (int j=0; j<this.getNumeroParametros(); j++)
		{
			cadenaEtiqueta=cadenaEtiqueta+this.getTipoParametro(j);
			
			cadenaEtiqueta=cadenaEtiqueta+ServiciosString.cadenaDimensiones( this.getDimParametro(j) );

			cadenaEtiqueta=cadenaEtiqueta+" "+this.getNombreParametro(j);
			
			if (j<this.getNumeroParametros()-1)
				cadenaEtiqueta=cadenaEtiqueta+" , ";
			else
				cadenaEtiqueta=cadenaEtiqueta+" ) ";
		}
		
		cadenaEtiqueta=cadenaEtiqueta+" [ "+this.getTipo();
		cadenaEtiqueta=cadenaEtiqueta+ServiciosString.cadenaDimensiones( this.getDimTipo() );
		cadenaEtiqueta=cadenaEtiqueta+" ]";
		
		return cadenaEtiqueta;
	}
	
	
	public String[] getParamValores()
	{
		return this.paramValor;
	}
	
	public String getParamValor(int i)
	{
		return this.paramValor[i];
	}	
	
	public int getTecnica()
	{
		return tecnica;
	}
	
	public int getIndiceEstructura()
	{
		return this.paramEstructura;
	}
	
	public int getIndice(int i)
	{
		if (i>=0 && i<4)
			return this.paramIndice[i];
		else
			return -1;
	}
	
	public int[] getIndices()
	{
		int []indices=new int[getNumIndices()];
		
		for (int i=0; i<indices.length; i++)
			indices[i]=this.getIndice(i);

		return indices;
	}
	
	public int getNumIndices()
	{
		int i=0; 
		int contados=0;
		while (i<4 && this.paramIndice[i]!=-1)
		{
			contados++;
			i++;
		}
		return contados;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public int[] getMetodosLlamados()
	{
		int[] metLlamados=new int[this.llamados.size()];
		
		for (int i=0; i<metLlamados.length; i++)
			metLlamados[i]=this.llamados.get(i).intValue();
		
		return metLlamados;
	}
	
	public void setMetodoLlamado(int n)
	{
		for (Integer numero : this.llamados)
			if (numero.intValue()==n)
				return;
		
		this.llamados.add(new Integer(n));
	}
	
	
	public void setNombre(String nombre)
	{
		this.nombre=nombre;
	}
	
	public void setTipo(String tipo)
	{
		this.tipo=tipo;
	}
	
	public void setNombresParametros(String[] paramNombre)
	{
		this.paramNombre=paramNombre;
	}
	
	public void setTiposParametros(String[] paramTipo)
	{
		this.paramTipo=paramTipo;
	}
	
	public void setVisibilidadEntrada(boolean[] visiblesEntrada)
	{
		this.visiblesEntrada=visiblesEntrada;
	}
	
	public void setVisibilidadEntrada(boolean marcado, int i)
	{
		this.visiblesEntrada[i]=marcado;
	}
	
	public void setVisibilidadSalida(boolean marcado, int i)
	{
		this.visiblesSalida[i]=marcado;
	}
	
	public void setVisibilidadSalida(boolean[] visiblesSalida)
	{
		this.visiblesSalida=visiblesSalida;
	}
	
	public void setMarcadoProcesar(boolean marcado)
	{
		this.procesado=marcado;
	}
	
	public void setMarcadoPrincipal(boolean marcado)
	{
		this.metodoPrincipal=marcado;
	}
	
	public void setMarcadoVisualizar(boolean marcado)
	{
		this.visto=marcado;
	}
	
	public void setParamValores(String valores[])
	{
		this.paramValor=valores;
	}
	
	public void setParamValor(int i,String valor)
	{
		this.paramValor[i]=valor;
	}	
	
	public void setTecnica(int tecnica)
	{
		this.tecnica=tecnica;
	}
	
	public void setIndices(int indiceEstructura)
	{
		this.setIndices(indiceEstructura,-1,-1,-1,-1);
	}
	
	public void setIndices(int indiceEstructura, int i1, int i2)
	{
		this.setIndices(indiceEstructura,i1,i2,-1,-1);
	}
	
	public void setIndices(int indiceEstructura, int i1, int i2, int i3, int i4)
	{
		this.paramEstructura=indiceEstructura;
		this.paramIndice[0]=i1;
		this.paramIndice[1]=i2;
		this.paramIndice[2]=i3;
		this.paramIndice[3]=i4;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Métodos de clase
	
	
	
	public static MetodoAlgoritmo getMetodo(ArrayList<MetodoAlgoritmo> listaMetodos, String[] datos, int[] dim)	
	{
		// datos[0]=nombre método
		// datos[1]=parametro 0...
		// datos[datos.length-1]=tipo retorno o valor null, no se debe tener en cuena en este método este valor
		
	
		for (int i=0; i<listaMetodos.size(); i++)
		{
			MetodoAlgoritmo m=listaMetodos.get(i);
			
			if (m.getNombre().equals(datos[0]) && (datos.length-2)==m.getNumeroParametros())
				// Si el nombre es igual y tiene el mismo número de parámetros, nos molestamos en mirar los tipos de los parámetros...
			{
				boolean todosIguales=true;
				
				for (int j=0; j<m.getNumeroParametros(); j++)
				{
					if (!(m.getTipoParametro(j).equals(datos[j+1]) && m.getDimParametro(j)==dim[j]))
					{
						//System.out.println("  getMetodo>DIFF ["+m.getTipoParametro(j)+"]["+datos[j+1]+"] ("+m.getDimParametro(j)+"/"+dim[j]+")");
						todosIguales=false;
					}
					
				}
				
				if (todosIguales)
					return m;
			}
			
		}
		
		
		return null;
	}
	
	
	public static int[] tecnicasEjecucion(ClaseAlgoritmo c, MetodoAlgoritmo m)
	{
		int[] ret=null;
		int[] metodosRecorridos=new int[0];
	
		int[] valorRetorno=tecnicasEjecucion(c,m,metodosRecorridos);
		
		if (valorRetorno[0]==1 && valorRetorno[1]==1)
		{
			ret=new int[2];
			ret[0]=TECNICA_REC;
			ret[1]=TECNICA_DYV;
		}
		else if (valorRetorno[0]==1)
		{
			ret=new int[1];
			ret[0]=TECNICA_REC;
		}
		else if (valorRetorno[1]==1)
		{
			ret=new int[1];
			ret[0]=TECNICA_DYV;
		}
				
		return ret; 
	}
	
	
	public static int[] tecnicasEjecucion(ClaseAlgoritmo c, MetodoAlgoritmo m, int[] metodosRecorridos)
	{	
		int[] retorno=new int[2];	// primer entero indica técnica REC, segundo indica técnica DYV // 0=NO, 1=SI
		
		retorno[0]=(m.getTecnica()==TECNICA_REC ? 1 : 0);
		retorno[1]=(m.getTecnica()==TECNICA_DYV ? 1 : 0);
		
		if (!Arrays.contiene(m.getID(),metodosRecorridos))
		{
			int[] idMetodosLlamados=m.getMetodosLlamados();
			
			for (int i=0; i<idMetodosLlamados.length; i++)
			{
				if (!Arrays.contiene(idMetodosLlamados[i],metodosRecorridos))
				{
					if (c.getMetodoID(idMetodosLlamados[i]).getTecnica()==TECNICA_REC)
						retorno[0]=1;
					else
						retorno[1]=1;
					
					metodosRecorridos=Arrays.insertar(idMetodosLlamados[i],metodosRecorridos);
				}
				
				int []tecnicasHijos=tecnicasEjecucion(c,c.getMetodoID(idMetodosLlamados[i]),idMetodosLlamados);
				if (tecnicasHijos[0]==1)
					retorno[0]=1;
				if (tecnicasHijos[1]==1)
					retorno[1]=1;
			}
		}

		return retorno;
	
	}
	
	
	
	
}
