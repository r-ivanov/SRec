/**
	Representa un estado del problema, bien a la entrada, bien a la salida de una llamada al método
	
	@author Luis Fernández y Antonio Pérez Carrasco
	@version 2006-2007
*/
package datos;



import utilidades.*;

public class Estado 
{
	//private Object parametros[]=null;
	//private Class clases[]=null;

	

	boolean visibles[];
	String param[];
	int dim[];
	String clases[];
	
	String representacion;
	String representacionLineaTraza;
	
	// DYV:
	private Object estructura=null;		// Copia de la estructura
	private int indiceEstructura=-1;
	private int indices[]=null;			// valores finales, posiciones de la estructura, no numeros de parámetros
	
	
	
	/**
		Constructor: genera un nuevo estado vacío
	*/
	public Estado()
	{
		this(null,null);
	}
	
	/**
		Constructor: genera un nuevo estado con una serie de valores, bien parámetros, bien de salida
		
		@param p conjunto de valores
	*/
	public Estado (Object p[])
	{
		this.param = new String[p.length];
		this.clases=new String[p.length];
		this.dim = new int[p.length];
		
		for (int i=0; i<p.length; i++)
		{
			/*
			if (!(p[i].getClass().getCanonicalName().contains("[]")))	// No es array
				this.param[i]=""+p[i];
			else
			*/
			this.param[i]=ServiciosString.representacionObjeto(p[i]);
		}
		
		for (int i=0; i<p.length; i++)
		{
			this.clases[i]=ServiciosString.simplificarClase(  p[i].getClass().getCanonicalName().replace("[]","")  );
			this.dim[i]=ServiciosString.vecesQueContiene(p[i].getClass().getCanonicalName(),"[]");
		}
		
		this.visibles=new boolean[p.length];
		for (int i=0; i<p.length; i++)
			this.visibles[i]=true;
			
		setRepresentacion(this.visibles);
	}

	/**
		Constructor: genera un nuevo estado con una serie de valores, bien parámetros, bien de salida y sus clases
		
		@param p array de valores
		@param c array de clases de los valores de p
	*/
	public Estado (Object p[], Class<?> c[])
	{
		if (p!=null && c!=null)
		{
			this.param = new String[p.length];
			this.clases=new String[c.length];
			
			for (int i=0; i<p.length; i++)
				/*
				if (!(c[i].getCanonicalName().contains("[]")))	// No es array
					this.param[i]=""+p[i];
				else
				*/
				this.param[i]=ServiciosString.representacionObjeto(p[i]);
					
			for (int i=0; i<c.length; i++)
				if (c[i]!=null)
					this.clases[i]=c[i].getCanonicalName();
					
			this.visibles=new boolean[p.length];
				for (int i=0; i<p.length; i++)
					this.visibles[i]=true;
					
			setRepresentacion(this.visibles);
		}
		else
		{
			this.param=new String[0];
			this.clases=new String[0];
		}
		
	}

	
	
	/**
	Constructor: genera un nuevo estado con una serie de valores, bien parámetros, bien de salida
	
	@param p conjunto de valores
	@param e objeto que contiene la estructura que se desea visualizar
	@param ind valores para las corrdenadas que se desean resaltar de la estructura
	*/
	public Estado (Object p[], Object e, int indEstructura, int ind[])
	{
		this(p);
						
		if (e!=null)	// Quizá sobre esta comprobación una vez se haya implantado todo sobre la estructura
		{		
			this.setEstructuraIndices(e,indEstructura,ind);
			
		}
		//else
		//	System.out.println("Estado: e=null");
	}
		
	
	
	
	/**
		Constructor: genera un nuevo estado con una serie de valores, bien parámetros, bien de salida y sus clases
		
		@param p array de valores
		@param c array de clases de los valores de p
	*/
	public Estado (Estado e)
	{
		if (e!=null)
		{
			this.param=e.getParametros();
			this.clases=e.getClases();
		}
	}

	
	/**
		Devuelve la estructura
		
		@return valores
	*/
	public Object getEstructura()
	{
		return this.estructura;
	}
	
	/**
		Devuelve los valores de los índices
		
		@return valor de las clases de los valores
	*/
	public int[] getIndices()
	{
		return this.indices;
	}	
	
	public void setEstructuraIndices(Object o, int indEstructura, int []ind)
	{
		if (o==null)
			return;
		
		// Copiamos la estructura, que llega en e
		this.estructura=copiaArray(o,o.getClass());

		
		// Copiamos el número de parámetro en el que está la estructura
		this.indiceEstructura=indEstructura;
	
		// Copiamos los valores de los índices que hay que usar, que llegan en in (filaInf, filaSup, colInf, colSup, )
		if (ind!=null)
		{
			this.indices = new int[ind.length];
			for (int i=0; i<ind.length; i++)
				this.indices[i]=ind[i];
		}
		else
			this.indices = new int[0];
	}
	
	
	public void setIndiceDeEstructura(int x)
	{
		this.indiceEstructura=x;
	}
	
	public int getIndiceDeEstructura()
	{
		return indiceEstructura;
	}
		
		
	
	
	
	
	/**
		Visualiza el estado del Estado
	*/
	public void visualizar ()
	{

	}
	
	
	
	public String getParametro(int i)
	{
		if (i<this.param.length)
			return new String(""+this.param[i]+"");
		else
			return null;
	}
	
	
	/**
		Devuelve los valores
		
		@return valores
	*/
	public String[] getParametros()
	{
		String[] copiaParametros=new String[this.param.length];
		
		for (int i=0; i<copiaParametros.length; i++)
		{
			copiaParametros[i]=new String(""+this.param[i]);
			//System.out.println(">>>>> "+i+" "+this.param[i]);
		}
		
		return copiaParametros;
		//return this.param;
	}

	/**
		Devuelve el valor de las clases de los valores
		
		@return valor de las clases de los valores
	*/
	public String[] getClases()
	{
		return this.clases;
	}
	
	public int[] getDimensiones()
	{
		return this.dim;
	}
	
	protected void setRepresentacion(boolean visibles[])
	{
		this.representacion="";
		this.visibles=visibles;
		
		if (visibles.length == this.param.length)
		{
			for (int i=0; i<this.param.length; i++)
				if (visibles[i])
				{
					this.representacion=this.representacion+this.param[i];
					boolean hayMas=false;
					for (int j=i+1; j<visibles.length; j++)
						if (visibles[j])
							hayMas=true;
					if (hayMas)
						this.representacion=this.representacion+" , ";
				}
		}
		else
			this.representacion="Error de visibilidad(1:"+visibles.length+"/"+this.param.length+")";
	}
	
	protected void setRepresentacionLineasTraza(boolean visibles[], String nombres[])
	{
		this.representacionLineaTraza="";
		this.visibles=visibles;
		
		if (visibles.length == this.param.length)
		{
			for (int i=0; i<this.param.length; i++)
				if (visibles[i])
				{
					this.representacionLineaTraza=this.representacionLineaTraza+nombres[i]+"=="+this.param[i];
					boolean hayMas=false;
					for (int j=i+1; j<visibles.length; j++)
						if (visibles[j])
							hayMas=true;
					if (hayMas)
						this.representacionLineaTraza=this.representacionLineaTraza+" , ";
				}
		}
		else
			this.representacion="Error de visibilidad(2)";
	}
	
	
	public String getRepresentacion()
	{
		return this.representacion;
	}	
	
	public String getRepresentacionCompleta(String nombres[])
	{
		String representacion="";

		
		if (visibles.length == this.param.length)
		{
			for (int i=0; i<this.param.length; i++)
			{
				if (nombres!=null)
					representacion=representacion+nombres[i]+"="+this.param[i];
				else
					representacion=representacion+"return="+this.param[i];
				if (i<this.param.length-1)
					representacion=representacion+" , ";
			}	
		}
		else
			representacion="Error de visibilidad(1:"+visibles.length+"/"+this.param.length+")";
		

		return representacion;
	}	
		
	public String getRepresentacionLineasTraza()
	{
		return this.representacionLineaTraza;
	}	
		
	public boolean[] getVisibilidad()
	{
		return this.visibles;
	}
	

	
	public String[] getValoresStrings()
	{
		return this.param;
		
	}
	
	public void setString(String e)
	{
		if (param==null || param.length==0)
		{
			param=new String[1];
			param[0]=e;
		}
		else
		{
			String[] pSaux=new String [param.length+1];
			for (int i=0; i<param.length; i++)
				pSaux[i]=param[i];
			pSaux[param.length]=e;
			param=pSaux;
		}
	}
	
	public void setClase(String e)
	{
		if (clases==null || clases.length==0)
		{
			clases=new String[1];
			clases[0]=e;
		}
		else
		{
			String[] pSaux=new String [clases.length+1];
			for (int i=0; i<clases.length; i++)
				pSaux[i]=clases[i];
			pSaux[clases.length]=e;
			clases=pSaux;
		}
	}
	
	public void setDim(int e)
	{
		if (dim==null || dim.length==0)
		{
			dim=new int[1];
			dim[0]=e;
		}
		else
		{
			int[] pSaux=new int [dim.length+1];
			for (int i=0; i<dim.length; i++)
				pSaux[i]=dim[i];
			pSaux[dim.length]=e;
			dim=pSaux;
		}
	}
	
	
	public Estado copiar()
	{
		Estado e=new Estado();
		
		if (this.param!=null)
			for (int i=0; i<this.param.length; i++)
				e.setString(new String(this.param[i]));
		
		if (this.visibles!=null)
		{
			e.visibles=new boolean[this.visibles.length];
			for (int i=0; i<this.visibles.length; i++)
				e.visibles[i]=this.visibles[i];
		}
		
		if (this.param!=null)
		{
			e.param=new String[this.param.length];
			for (int i=0; i<this.param.length; i++)
				e.param[i]=new String(this.param[i]);
		}
		
		if (this.dim!=null)
		{
			e.dim=new int[this.dim.length];
			for (int i=0; i<this.dim.length; i++)
				e.dim[i]=this.dim[i];
		}
		
		if (this.clases!=null)
		{
			e.clases=new String[this.clases.length];
			for (int i=0; i<this.clases.length; i++)
				e.clases[i]=new String(this.clases[i]);		
		}
		

		if (this.representacion!=null)
			e.representacion=new String(this.representacion);
		if (this.representacionLineaTraza!=null)	
			e.representacionLineaTraza=new String(this.representacionLineaTraza);
		
		// Copiamos cosas de DYV
		e.setEstructuraIndices(this.estructura, this.indiceEstructura, this.indices);
		
		
		return e;
	}
	
	/**
		Realiza un duplicado de los valores
		
		@return duplicado de los valores
	*/
	private Object copiaArray(Object p, Class c)
	{
		Object o=null;
		if (!(c.getCanonicalName().contains("[][]")))
		{
			if (c.getCanonicalName().contains("int"))
			{
				o=new int[((int [])p).length];
				for (int j=0; j<((int [])p).length; j++)
					((int [])o)[j]=((int [])p)[j];
			}
			else if (c.getCanonicalName().contains("short"))
			{
				o=new short[((short [])p).length];
				for (int j=0; j<((short [])p).length; j++)
					((short [])o)[j]=((short [])p)[j];
			}
			else if (c.getCanonicalName().contains("byte"))
			{
				o=new byte[((byte [])p).length];
				for (int j=0; j<((byte [])p).length; j++)
					((byte [])o)[j]=((byte [])p)[j];
			}
			else if (c.getCanonicalName().contains("long"))
			{
				o=new long[((long [])p).length];
				for (int j=0; j<((long [])p).length; j++)
					((long [])o)[j]=((long [])p)[j];
			}
			else if (c.getCanonicalName().contains("float"))
			{
				o=new float[((float [])p).length];
				for (int j=0; j<((float [])p).length; j++)
					((float [])o)[j]=((float [])p)[j];
			}
			else if (c.getCanonicalName().contains("double"))
			{
				o=new double[((double [])p).length];
				for (int j=0; j<((double [])p).length; j++)
					((double [])o)[j]=((double [])p)[j];
			}
			else if (c.getCanonicalName().contains("boolean"))
			{
				o=new boolean[((boolean [])p).length];
				for (int j=0; j<((boolean [])p).length; j++)
					((boolean [])o)[j]=((boolean [])p)[j];
			}
			else if (c.getCanonicalName().contains("char"))
			{
				o=new char[((char [])p).length];
				for (int j=0; j<((char [])p).length; j++)
					((char [])o)[j]=((char [])p)[j];
			}
			else if (c.getCanonicalName().contains("java.lang.String"))
			{
				o=new String[((String [])p).length];
				for (int j=0; j<((String [])p).length; j++)
					((String [])o)[j]=((String [])p)[j];
			}
		}
		else
		{
			if (c.getCanonicalName().contains("int"))
			{
				o=new int[((int [][])p).length][];
				for (int i=0; i<((int [][])p).length; i++)
					((int [][])o)[i]=(int[])copiaArray(  ((int [][])p)[i],  ((int [][])p)[i].getClass()  );
			}
			else if (c.getCanonicalName().contains("short"))
			{
				o=new short[((short [][])p).length][];
				for (int i=0; i<((int [][])p).length; i++)
					((short [][])o)[i]=(short[])copiaArray(  ((short [][])p)[i],  ((short [][])p)[i].getClass()  );
			}
			else if (c.getCanonicalName().contains("byte"))
			{
				o=new byte[((byte [][])p).length][];
				for (int i=0; i<((int [][])p).length; i++)
					((byte [][])o)[i]=(byte[])copiaArray(  ((byte [][])p)[i],  ((byte [][])p)[i].getClass()  );
			}
			else if (c.getCanonicalName().contains("long"))
			{
				o=new long[((long [][])p).length][];
				for (int i=0; i<((int [][])p).length; i++)
					((long [][])o)[i]=(long[])copiaArray(  ((long [][])p)[i],  ((long [][])p)[i].getClass()  );
			}
			else if (c.getCanonicalName().contains("float"))
			{
				o=new float[((float [][])p).length][];
				for (int i=0; i<((int [][])p).length; i++)
					((float [][])o)[i]=(float[])copiaArray(  ((float [][])p)[i],  ((float [][])p)[i].getClass()  );
			}
			else if (c.getCanonicalName().contains("double"))
			{
				o=new double[((double [][])p).length][];
				for (int i=0; i<((int [][])p).length; i++)
					((double [][])o)[i]=(double[])copiaArray(  ((double [][])p)[i],  ((double [][])p)[i].getClass()  );
			}
			else if (c.getCanonicalName().contains("boolean"))
			{
				o=new boolean[((boolean [][])p).length][];
				for (int i=0; i<((int [][])p).length; i++)
					((boolean [][])o)[i]=(boolean[])copiaArray(  ((boolean [][])p)[i],  ((boolean [][])p)[i].getClass()  );
			}
			else if (c.getCanonicalName().contains("char"))
			{
				o=new char[((char [][])p).length][];
				for (int i=0; i<((int [][])p).length; i++)
					((char [][])o)[i]=(char[])copiaArray(  ((char [][])p)[i],  ((char [][])p)[i].getClass()  );
			}
			else if (c.getCanonicalName().contains("java.lang.String"))
			{
				o=new String[((String [][])p).length][];
				for (int i=0; i<((int [][])p).length; i++)
					((String [][])o)[i]=(String[])copiaArray(  ((String [][])p)[i],  ((String [][])p)[i].getClass()  );
			}
		}
			
		return o;
	}
}
