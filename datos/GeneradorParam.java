/**
	Devuelve en formato String un valor aleatorio válido según el tipo al que pertenezca el parámetro
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/
package datos;



import utilidades.*;

public class GeneradorParam
{
	String valor;
	int valorInt;
	long valorLong;
	double valorDouble;
	char valorChar;
	// Valores simples
	
	
	public GeneradorParam()	// Valido para int
	{
		valorInt=ValorAleatorio.generaInt(1,10);
		this.valor=""+valorInt;
	}
	
	public GeneradorParam(int inf, int sup)	// Valido para int, short y byte
	{
		//System.out.println("INF="+inf+"  SUP="+sup);
		valorInt=ValorAleatorio.generaInt(inf,sup);
		this.valor=""+valorInt;
	}
	
	public GeneradorParam(long inf, long sup)	// Valido para long
	{
		valorLong=ValorAleatorio.generaLong(inf,sup);
		this.valor=""+valorLong;
	}
	
	public GeneradorParam(double inf, double sup)	// Valido para float y double
	{
		valorDouble=ValorAleatorio.generaDouble(inf,sup);
		this.valor=""+valorDouble;
	}
	
	// Arrays una dimensión
	
	public GeneradorParam(int inf, int sup, int dim1, int numOrden)	// Valido para int[], short[] y byte[]
	{
		this.valor="{ ";
		
		long vector[]=new long[dim1];
		for (int i=0; i<dim1; i++)
		{
			vector[i]=ValorAleatorio.generaInt(inf,sup);
			//System.out.println("["+i+"]"+vector[i]);
		}

		if (numOrden==1)
			vector=ordenCreciente(vector);
		else if (numOrden==2)
			vector=ordenDecreciente(vector);
			
		for (int i=0; i<dim1; i++)
		{
			this.valor=this.valor+vector[i];
			if (i<(dim1-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}
	
	public GeneradorParam(long inf, long sup, int dim1, int numOrden)	// Valido para long[]
	{
		this.valor="{ ";
		
		long vector[]=new long[dim1];
		for (int i=0; i<dim1; i++)
		{
			vector[i]=ValorAleatorio.generaLong(inf,sup);
			//System.out.println("["+i+"]"+vector[i]);
		}

		if (numOrden==1)
			vector=ordenCreciente(vector);
		else if (numOrden==2)
			vector=ordenDecreciente(vector);
		
		for (int i=0; i<dim1; i++)
		{
			this.valor=this.valor+vector[i];
			if (i<(dim1-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}

	public GeneradorParam(double inf, double sup, int dim1, int numOrden)	// Valido para float[] y double[]
	{
		this.valor="{ ";
		
		double vector[]=new double[dim1];
		for (int i=0; i<dim1; i++)
		{
			vector[i]=ValorAleatorio.generaDouble(inf,sup);
			//System.out.println("["+i+"]"+vector[i]);
		}

		if (numOrden==1)
			vector=ordenCreciente(vector);
		else if (numOrden==2)
			vector=ordenDecreciente(vector);
			
		for (int i=0; i<dim1; i++)
		{
			this.valor=this.valor+vector[i];
			if (i<(dim1-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}
	
	// Arrays dos dimensiones
	
	public GeneradorParam(int inf, int sup, int dim1, int dim2, int numOrden)	// Valido para int[][], short[][] y byte[][]
	{
		this.valor="{ ";
		for (int i=0; i<dim1; i++)
		{
			this.valor=this.valor+"{ ";
			
			long vector[]=new long[dim2];
			for (int j=0; j<dim2; j++)
				vector[j]=ValorAleatorio.generaInt(inf,sup);

			if (numOrden==1)
				vector=ordenCreciente(vector);
			else if (numOrden==2)
				vector=ordenDecreciente(vector);
			
			for (int j=0; j<dim2; j++)
			{
				this.valor=this.valor+vector[j];
				if (j<(dim2-1))
					this.valor=this.valor+", ";
			}
			this.valor=this.valor+" }";
			if (i<(dim1-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}
	
	public GeneradorParam(long inf, long sup, int dim1, int dim2, int numOrden)	// Valido para long[][]
	{
		this.valor="{ ";
		for (int i=0; i<dim1; i++)
		{
			this.valor=this.valor+"{ ";
			
			long vector[]=new long[dim2];
			for (int j=0; j<dim2; j++)
				vector[j]=ValorAleatorio.generaLong(inf,sup);

			if (numOrden==1)
				vector=ordenCreciente(vector);
			else if (numOrden==2)
				vector=ordenDecreciente(vector);
			
			for (int j=0; j<dim2; j++)
			{
				this.valor=this.valor+vector[j];
				if (j<(dim2-1))
					this.valor=this.valor+", ";
			}
			this.valor=this.valor+" }";
			if (i<(dim1-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}	
	
	public GeneradorParam(double inf, double sup, int dim1, int dim2, int numOrden)	// Valido para float[][] y double[][]
	{
		this.valor="{ ";
		for (int i=0; i<dim1; i++)
		{
			this.valor=this.valor+"{ ";
			
			double vector[]=new double[dim2];
			for (int j=0; j<dim1; j++)
				vector[j]=ValorAleatorio.generaDouble(inf,sup);

			if (numOrden==1)
				vector=ordenCreciente(vector);
			else if (numOrden==2)
				vector=ordenDecreciente(vector);
			
			for (int j=0; j<dim2; j++)
			{
				this.valor=this.valor+vector[j];
				if (j<(dim2-1))
					this.valor=this.valor+", ";
			}
			this.valor=this.valor+" }";
			if (i<(dim1-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}
	
	public GeneradorParam(Character c)
	{
		this.valorChar=ValorAleatorio.generaChar();
		this.valor="\'"+valorChar+"\'";
	}
	
	public GeneradorParam(Character c, int dim, int numOrden)
	{
		this.valor="{ ";
		
		char vector[]=new char[dim];
		for (int i=0; i<dim; i++)
			vector[i]=ValorAleatorio.generaChar();

		if (numOrden==1)
			vector=ordenCreciente(vector);
		else if (numOrden==2)
			vector=ordenDecreciente(vector);
			
		
		for (int i=0; i<dim; i++)
		{
			this.valor=this.valor+"\'"+vector[i]+"\'";
			if (i<(dim-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}
	
	public GeneradorParam(Character c, int dim1, int dim2, int numOrden)
	{
		this.valor="{ ";
		for (int i=0; i<dim1; i++)
		{
			this.valor=this.valor+"{ ";
			for (int j=0; j<dim2; j++)
			{
				this.valor=this.valor+"\'"+ValorAleatorio.generaChar()+"\'";
				if (j<(dim2-1))
					this.valor=this.valor+", ";
			}
			this.valor=this.valor+" }";
			
			if (i<(dim1-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}
	
	public GeneradorParam(boolean b)
	{
		this.valor=""+ValorAleatorio.generaBoolean();
	}
	
	public GeneradorParam(boolean b, int dim)
	{
		this.valor="{ ";
		for (int i=0; i<dim; i++)
		{
			this.valor=this.valor+ValorAleatorio.generaBoolean();
			if (i<(dim-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}
	
	public GeneradorParam(boolean b, int dim1, int dim2)
	{
		this.valor="{ ";
		for (int i=0; i<dim1; i++)
		{
			this.valor=this.valor+"{ ";
			for (int j=0; j<dim2; j++)
			{
				this.valor=this.valor+ValorAleatorio.generaBoolean();
				if (j<(dim2-1))
					this.valor=this.valor+", ";
			}
			this.valor=this.valor+" }";
			
			if (i<(dim1-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}
	
	public GeneradorParam(String s)
	{
		this.valor="\""+ValorAleatorio.generaString()+"\"";
	}

	public GeneradorParam(String s, int dim)
	{
		this.valor="{ ";
		for (int i=0; i<dim; i++)
		{
			this.valor=this.valor+"\""+ValorAleatorio.generaString()+"\"";
			if (i<(dim-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}
	
	public GeneradorParam(String s, int dim1, int dim2)
	{
		this.valor="{ ";
		for (int i=0; i<dim1; i++)
		{
			this.valor=this.valor+"{ ";
			for (int j=0; j<dim2; j++)
			{
				this.valor=this.valor+"\""+ValorAleatorio.generaString()+"\"";
				if (j<(dim2-1))
					this.valor=this.valor+", ";
			}
			this.valor=this.valor+" }";
			
			if (i<(dim1-1))
				this.valor=this.valor+", ";
		}
		this.valor=this.valor+" }";
	}
	
	public String getValor()
	{
		return this.valor;
	}
	
	public int getValorInt()
	{
		return this.valorInt;
	}
	
	public long getValorLong()
	{
		return this.valorLong;
	}
	
	public double getValorDouble()
	{
		return this.valorDouble;
	}
	
	public char getValorChar()
	{
		return this.valorChar;
	}
	
	
	
	private static long[] ordenCreciente(long v[])
	{
		long array[]=new long [v.length];
		

		if (v.length>2)
		{
			int div=(v.length/2);
			
			long array1[]=new long[div];
			long array2[]=new long[v.length-div];
			
			for (int i=0; i<div; i++)
				array1[i]=v[i];
			for (int i=div; i<v.length; i++)
				array2[i-div]=v[i];
			
			array1=ordenCreciente(array1);
			array2=ordenCreciente(array2);
			array=combinar(array1,array2,1);
		}
		else if (v.length==2 && v[0]>v[1])
		{
			long posAux=v[0];
			array[0]=v[v.length-1];
			array[v.length-1]=posAux;
		}
		else if (v.length==2 && v[0]<=v[1])
		{
			array[0]=v[0];
			array[1]=v[1];
		}
		else
			array[0]=v[0];
			
			
		return array;
	}
	
	private static long[] ordenDecreciente(long v[])
	{
		long array[]=new long[v.length];
		
		if (v.length>2)
		{
			int div=(v.length/2);
			
			long array1[]=new long[div];
			long array2[]=new long[v.length-div];
			
			for (int i=0; i<div; i++)
				array1[i]=v[i];
			for (int i=div; i<v.length; i++)
				array2[i-div]=v[i];
			
			array1=ordenDecreciente(array1);
			array2=ordenDecreciente(array2);
			array=combinar(array1,array2,2);
		}
		else if (v.length==2 && v[0]<v[1])
		{
			long posAux=v[0];
			array[0]=v[v.length-1];
			array[v.length-1]=posAux;
		}
		else if (v.length==2 && v[0]>=v[1])
		{
			array[0]=v[0];
			array[1]=v[1];
		}
		else
			array[0]=v[0];
		return array;
	}
	
	public static long[] combinar(long a1[], long a2[],int orden)
	{
		long array[]=new long[a1.length+a2.length];
		int p1=0, p2=0;

		if (orden==1)
			for (int i=0; i<array.length; i++)
			{
				if (p2>=a2.length || (p1<a1.length && a1[p1]<=a2[p2]))
				{
					array[i]=a1[p1];
					p1++;
				}
				else if (p1>=a1.length || (p2<a2.length && a1[p1]>=a2[p2]))
				{
					array[i]=a2[p2];
					p2++;
				}
			}
		else
			for (int i=0; i<array.length; i++)
			{
				if (p2>=a2.length || (p1<a1.length && a1[p1]>=a2[p2]))
				{
					array[i]=a1[p1];
					p1++;
				}
				else if (p1>=a1.length || (p2<a2.length && a1[p1]<=a2[p2]))
				{
					array[i]=a2[p2];
					p2++;
				}
			}
		
		return array;
	}
	
	private static double[] ordenCreciente(double v[])
	{
		double array[]=new double[v.length];
		
		if (v.length>2)
		{
			int div=(v.length/2);
			
			double array1[]=new double[div];
			double array2[]=new double[v.length-div];
			
			for (int i=0; i<div; i++)
				array1[i]=v[i];
			for (int i=div; i<v.length; i++)
				array2[i-div]=v[i];
			
			array1=ordenCreciente(array1);
			array2=ordenCreciente(array2);
			array=combinar(array1,array2,1);
		}
		else if (v.length==2 && v[0]>v[1])
		{
			double posAux=v[0];
			array[0]=v[v.length-1];
			array[v.length-1]=posAux;
		}
		else if (v.length==2 && v[0]<=v[1])
		{
			array[0]=v[0];
			array[1]=v[1];
		}
		else
			array[0]=v[0];
			
		return array;
	}
	
	private static double[] ordenDecreciente(double v[])
	{
		double array[]=new double[v.length];
		
		if (v.length>2)
		{
			int div=(v.length/2);
			
			double array1[]=new double[div];
			double array2[]=new double[v.length-div];
			
			for (int i=0; i<div; i++)
				array1[i]=v[i];
			for (int i=div; i<v.length; i++)
				array2[i-div]=v[i];
			
			array1=ordenDecreciente(array1);
			array2=ordenDecreciente(array2);
			array=combinar(array1,array2,2);
		}
		else if (v.length==2 && v[0]<v[1])
		{
			double posAux=v[0];
			array[0]=v[v.length-1];
			array[v.length-1]=posAux;
		}
		else if (v.length==2 && v[0]>=v[1])
		{
			array[0]=v[0];
			array[1]=v[1];
		}
		else
			array[0]=v[0];
		return array;
	}
	
	public static double[] combinar(double a1[], double a2[],int orden)
	{
		double array[]=new double[a1.length+a2.length];
		int p1=0, p2=0;

		if (orden==1)
			for (int i=0; i<array.length; i++)
			{
				if (p2>=a2.length || (p1<a1.length && a1[p1]<=a2[p2]))
				{
					array[i]=a1[p1];
					p1++;
				}
				else if (p1>=a1.length || (p2<a2.length && a1[p1]>=a2[p2]))
				{
					array[i]=a2[p2];
					p2++;
				}
			}
		else
			for (int i=0; i<array.length; i++)
			{
				if (p2>=a2.length || (p1<a1.length && a1[p1]>=a2[p2]))
				{
					array[i]=a1[p1];
					p1++;
				}
				else if (p1>=a1.length || (p2<a2.length && a1[p1]<=a2[p2]))
				{
					array[i]=a2[p2];
					p2++;
				}
			}
		
		return array;
	}
	
	
	private static char[] ordenCreciente(char v[])
	{
		char array[]=new char[v.length];
		
		if (v.length>2)
		{
			int div=(v.length/2);
			
			char array1[]=new char[div];
			char array2[]=new char[v.length-div];
			
			for (int i=0; i<div; i++)
				array1[i]=v[i];
			for (int i=div; i<v.length; i++)
				array2[i-div]=v[i];
			
			array1=ordenCreciente(array1);
			array2=ordenCreciente(array2);
			array=combinar(array1,array2,1);
		}
		else if (v.length==2 && v[0]>v[1])
		{
			char posAux=v[0];
			array[0]=v[v.length-1];
			array[v.length-1]=posAux;
		}
		else if (v.length==2 && v[0]<=v[1])
		{
			array[0]=v[0];
			array[1]=v[1];
		}
		else
			array[0]=v[0];
			
		
		return array;
	}
	
	private static char[] ordenDecreciente(char v[])
	{
		char array[]=new char[v.length];
		
		if (v.length>2)
		{
			int div=(v.length/2);
			
			char array1[]=new char[div];
			char array2[]=new char[v.length-div];
			
			for (int i=0; i<div; i++)
				array1[i]=v[i];
			for (int i=div; i<v.length; i++)
				array2[i-div]=v[i];
			
			array1=ordenDecreciente(array1);
			array2=ordenDecreciente(array2);
			array=combinar(array1,array2,2);
		}
		else if (v.length==2 && v[0]<v[1])
		{
			char posAux=v[0];
			array[0]=v[v.length-1];
			array[v.length-1]=posAux;
		}
		else if (v.length==2 && v[0]>=v[1])
		{
			array[0]=v[0];
			array[1]=v[1];
		}
		else
			array[0]=v[0];
		return array;
	}
	
	public static char[] combinar(char a1[], char a2[],int orden)
	{
		char array[]=new char[a1.length+a2.length];
		int p1=0, p2=0;

		if (orden==1)
			for (int i=0; i<array.length; i++)
			{
				if (p2>=a2.length || (p1<a1.length && a1[p1]<=a2[p2]))
				{
					array[i]=a1[p1];
					p1++;
				}
				else if (p1>=a1.length || (p2<a2.length && a1[p1]>=a2[p2]))
				{
					array[i]=a2[p2];
					p2++;
				}
			}
		else
			for (int i=0; i<array.length; i++)
			{
				if (p2>=a2.length || (p1<a1.length && a1[p1]>=a2[p2]))
				{
					array[i]=a1[p1];
					p1++;
				}
				else if (p1>=a1.length || (p2<a2.length && a1[p1]<=a2[p2]))
				{
					array[i]=a2[p2];
					p2++;
				}
			}
		
		return array;
	}
	
	public static void main (String argc[])
	{
		int [][] z=new int[4][];
		Class clase;
		
		clase=z.getClass();
		
		int x,y;
	}
}

