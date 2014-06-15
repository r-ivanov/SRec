package datos;

import utilidades.ServiciosString;



public class Estructura
{
	
	int matrizInt[][]=null;
	long matrizLong[][]=null;
	double matrizDouble[][]=null;
	char matrizChar[][]=null;
	String matrizString[][]=null;
	boolean matrizBoolean[][]=null;
	
	
	int arrayInt[]=null;
	long arrayLong[]=null;
	double arrayDouble[]=null;
	char arrayChar[]=null;
	String arrayString[]=null;
	boolean arrayBoolean[]=null;
	
	int dim=0;
	String clase;
	
	public Estructura (Object o)
	{
		if (o!=null)
		{
			this.clase=o.getClass().getCanonicalName();
			
			if (this.clase.contains("[][]")) // Si es matriz
			{
				dim=2;
				if (this.clase.contains("byte") || this.clase.contains("short") || this.clase.contains("int"))
					matrizInt=(int[][])o;
				else if (this.clase.contains("long"))
					matrizLong=(long[][])o;
				else if (this.clase.contains("float") || this.clase.contains("double"))
					matrizDouble=(double[][])o;
				else if (this.clase.contains("char"))
					matrizChar=(char[][])o;	
				else if (this.clase.contains("String"))
					matrizString=(String[][])o;
				else if (this.clase.contains("boolean"))
					matrizBoolean=(boolean[][])o;
			}
			else
			{
				dim=1;
				if (this.clase.contains("byte") || this.clase.contains("short") || this.clase.contains("int"))
					arrayInt=(int[])o;
				else if (this.clase.contains("long"))
					arrayLong=(long[])o;
				else if (this.clase.contains("float") || this.clase.contains("double"))
					arrayDouble=(double[])o;
				else if (this.clase.contains("char"))
					arrayChar=(char[])o;	
				else if (this.clase.contains("String"))
					arrayString=(String[])o;
				else if (this.clase.contains("boolean"))
					arrayBoolean=(boolean[])o;
			}
		}
	}
	
	
	public Estructura( String tipo, String dim, String valores, String tam1, String tam2)
	{
		int dim1=-1, dim2=-1;
		this.clase=tipo;
		try {
			dim1=Integer.parseInt(tam1);
			dim2=Integer.parseInt(tam2);
		} catch (Exception e) {
			
		}
		
		try {
			this.dim=Integer.parseInt(dim);
		} catch (Exception e) {
			this.dim=1;
		}
		
		if (this.esMatriz())
		{
			if (tipo.contains("int"))
			{
				int val[]=ServiciosString.extraerValoresInt(valores,'|');
				matrizInt=new int[dim1][];
				int x=0;
				for (int i=0; i<matrizInt.length; i++)
				{
					matrizInt[i]=new int[dim2];
					for (int j=0; j<matrizInt[i].length; j++, x++)
						matrizInt[i][j]=val[x];
				}				
			}
			else if (tipo.contains("long"))
			{
				long val[]=ServiciosString.extraerValoresLong(valores,'|');
				matrizLong=new long[dim1][];
				int x=0;
				for (int i=0; i<matrizLong.length; i++)
				{
					matrizLong[i]=new long[dim2];
					for (int j=0; j<matrizLong[i].length; j++, x++)
						matrizLong[i][j]=val[x];
				}				
			}
			else if (tipo.contains("double"))
			{
				double val[]=ServiciosString.extraerValoresDouble(valores,'|');
				matrizDouble=new double[dim1][];
				int x=0;
				for (int i=0; i<matrizDouble.length; i++)
				{
					matrizDouble[i]=new double[dim2];
					for (int j=0; j<matrizDouble[i].length; j++, x++)
						matrizDouble[i][j]=val[x];
				}				
			}
			else if (tipo.contains("char"))
			{
				char val[]=ServiciosString.extraerValoresChar(valores,'|');
				matrizChar=new char[dim1][];
				int x=0;
				for (int i=0; i<matrizChar.length; i++)
				{
					matrizChar[i]=new char[dim2];
					for (int j=0; j<matrizChar[i].length; j++, x++)
						matrizChar[i][j]=val[x];
				}				
			}
			else if (tipo.contains("String"))
			{
				String val[]=ServiciosString.extraerValoresString(valores,'|');
				matrizString=new String[dim1][];
				int x=0;
				for (int i=0; i<matrizString.length; i++)
				{
					matrizString[i]=new String[dim2];
					for (int j=0; j<matrizString[i].length; j++, x++)
						matrizString[i][j]=new String(val[x]);
				}				
			}
			else if (tipo.contains("boolean"))
			{
				boolean val[]=ServiciosString.extraerValoresBoolean(valores,'|');
				matrizBoolean=new boolean[dim1][];
				int x=0;
				for (int i=0; i<matrizBoolean.length; i++)
				{
					matrizBoolean[i]=new boolean[dim2];
					for (int j=0; j<matrizBoolean[i].length; j++, x++)
						matrizBoolean[i][j]=val[x];
				}
			}
		}
		else
		{
			if (tipo.contains("int"))
				arrayInt=ServiciosString.extraerValoresInt(valores,'|');
			else if (tipo.contains("long"))
				arrayLong=ServiciosString.extraerValoresLong(valores,'|');
			else if (tipo.contains("double"))
				arrayDouble=ServiciosString.extraerValoresDouble(valores,'|');
			else if (tipo.contains("char"))
				arrayChar=ServiciosString.extraerValoresChar(valores,'|');
			else if (tipo.contains("String"))
				arrayString=ServiciosString.extraerValoresString(valores,'|');
			else if (tipo.contains("boolean"))
				arrayBoolean=ServiciosString.extraerValoresBoolean(valores,'|');
		}
		
	}
	
	
	
	
	
	public boolean esMatriz()
	{
		return this.dim==2;
	}
	
	public boolean esArray()
	{
		return this.dim==1;
	}
	
	
	public int[] dimensiones()
	{
		int [] dimSalida;
		if (esMatriz())
		{
			dimSalida=new int[2];
			if (matrizInt!=null)
			{
				dimSalida[0]=matrizInt.length;
				if (dimSalida[0]!=0)
					dimSalida[1]=matrizInt[0].length;
				else
					dimSalida[1]=0;
			}
			else if (matrizLong!=null)
			{
				dimSalida[0]=matrizLong.length;
				if (dimSalida[0]!=0)
					dimSalida[1]=matrizLong[0].length;
				else
					dimSalida[1]=0;
			}
			else if (matrizDouble!=null)
			{
				dimSalida[0]=matrizDouble.length;
				if (dimSalida[0]!=0)
					dimSalida[1]=matrizDouble[0].length;
				else
					dimSalida[1]=0;
			}
			else if (matrizChar!=null)
			{
				dimSalida[0]=matrizChar.length;
				if (dimSalida[0]!=0)
					dimSalida[1]=matrizChar[0].length;
				else
					dimSalida[1]=0;
			}
			else if (matrizString!=null)
			{
				dimSalida[0]=matrizString.length;
				if (dimSalida[0]!=0)
					dimSalida[1]=matrizString[0].length;
				else
					dimSalida[1]=0;
			}
			else // if (matrizBoolean!=null)
			{
				dimSalida[0]=matrizBoolean.length;
				if (dimSalida[0]!=0)
					dimSalida[1]=matrizBoolean[0].length;
				else
					dimSalida[1]=0;
			}
			
		}
		else	// es array
		{
			dimSalida=new int[1];
			if (arrayInt!=null)
				dimSalida[0]=arrayInt.length;
			else if (arrayLong!=null)
				dimSalida[0]=arrayLong.length;
			else if (arrayDouble!=null)
				dimSalida[0]=arrayDouble.length;
			else if (arrayDouble!=null)
				dimSalida[0]=arrayDouble.length;
			else if (arrayChar!=null)
				dimSalida[0]=arrayChar.length;
			else if (arrayString!=null)
				dimSalida[0]=arrayString.length;
			else if (arrayBoolean!=null)
				dimSalida[0]=arrayBoolean.length;
			
		}
		return dimSalida;
	}
	
	public Object getObjeto()
	{
		if (esMatriz())
		{
			if (matrizInt!=null)
				return matrizInt;
			else if (matrizLong!=null)
				return matrizLong;
			else if (matrizDouble!=null)
				return matrizDouble;
			else if (matrizChar!=null)
				return matrizChar;
			else if (matrizString!=null)
				return matrizString;
			else if (matrizBoolean!=null)
				return matrizBoolean;
			else
			{
				System.out.println("Matriz null");
				return null;
			}
		}
		else
		{
			if (arrayInt!=null)
				return arrayInt;
			else if (arrayLong!=null)
				return arrayLong;
			else if (arrayDouble!=null)
				return arrayDouble;
			else if (arrayDouble!=null)
				return arrayDouble;
			else if (arrayChar!=null)
				return arrayChar;
			else if (arrayString!=null)
				return arrayString;
			else if (arrayBoolean!=null)
				return arrayBoolean;
			else
			{
				System.out.println("Array null");
				return null;
			}
		}

	}
	
	
	
	
	public Object getTipo()
	{
		if (clase==null)
			return null;
		
		if (clase.contains("byte") || clase.contains("short") || clase.contains("int"))
			return new Integer(0);
		else if (clase.contains("long"))
			return new Long(0);
		else if (clase.contains("float") || clase.contains("double"))
			return new Double(0);
		else if (clase.contains("char"))
			return new Character(' ');
		else if (clase.contains("String"))
			return new String("");
		else //if (clase.contains("boolean"))
			return new Boolean(true);
	}
	
		
	public int posicArrayInt(int p)
	{
		return arrayInt[p];
	}
	
	public long posicArrayLong(int p)
	{
		return arrayLong[p];
	}
	
	public double posicArrayDouble(int p)
	{
		return arrayDouble[p];
	}
	
	public char posicArrayChar(int p)
	{
		return arrayChar[p];
	}
	
	public String posicArrayString(int p)
	{
		return arrayString[p];
	}
	
	public boolean posicArrayBool(int p)
	{
		return arrayBoolean[p];
	}
	
	public int posicMatrizInt(int f, int c)
	{
		return matrizInt[f][c];
	}
	
	public long posicMatrizLong(int f, int c)
	{
		return matrizLong[f][c];
	}
	
	public double posicMatrizDouble(int f, int c)
	{
		return matrizDouble[f][c];
	}
	
	public char posicMatrizChar(int f, int c)
	{
		return matrizChar[f][c];
	}
	
	public String posicMatrizString(int f, int c)
	{
		return matrizString[f][c];
	}
	
	public boolean posicMatrizBool(int f, int c)
	{
		return matrizBoolean[f][c];
	}
	
	public String getClase()
	{
		return new String(""+this.clase);
	}
	
	public String getValor()
	{
		String salida="";
		if (esMatriz())
		{
			if (matrizInt!=null)
			{
				for (int i=0; i<matrizInt.length; i++)
					for (int j=0; j<matrizInt[i].length; j++)
					{
						salida=salida+matrizInt[i][j];
						if (i<(matrizInt.length-1) || j<(matrizInt[i].length-1))
							salida=salida+"|";
					}
			}
			else if (matrizLong!=null)
			{
				for (int i=0; i<matrizLong.length; i++)
					for (int j=0; j<matrizLong[i].length; j++)
					{
						salida=salida+matrizLong[i][j];
						if (i<(matrizLong.length-1) || j<(matrizLong[i].length-1))
							salida=salida+"|";
					}
			}
			else if (matrizDouble!=null)
			{
				for (int i=0; i<matrizDouble.length; i++)
					for (int j=0; j<matrizDouble[i].length; j++)
					{
						salida=salida+matrizDouble[i][j];
						if (i<(matrizDouble.length-1) || j<(matrizDouble[i].length-1))
							salida=salida+"|";
					}
			}
			else if (matrizChar!=null)
			{
				for (int i=0; i<matrizChar.length; i++)
					for (int j=0; j<matrizChar[i].length; j++)
					{
						salida=salida+matrizChar[i][j];
						if (i<(matrizChar.length-1) || j<(matrizChar[i].length-1))
							salida=salida+"|";
					}
			}
			else if (matrizString!=null)
			{
				for (int i=0; i<matrizString.length; i++)
					for (int j=0; j<matrizString[i].length; j++)
					{
						salida=salida+matrizString[i][j];
						if (i<(matrizString.length-1) || j<(matrizString[i].length-1))
							salida=salida+"|";
					}
			}
			else // if (matrizBoolean!=null)
			{
				for (int i=0; i<matrizBoolean.length; i++)
					for (int j=0; j<matrizBoolean[i].length; j++)
					{
						salida=salida+matrizBoolean[i][j];
						if (i<(matrizBoolean.length-1) || j<(matrizBoolean[i].length-1))
							salida=salida+"|";
					}
			}
		}
		else
		{
			if (arrayInt!=null)
				for (int i=0; i<arrayInt.length; i++)
				{
					salida=salida+arrayInt[i];
					if (i<(arrayInt.length-1))
						salida=salida+"|";
				}
			else if (arrayLong!=null)
				for (int i=0; i<arrayLong.length; i++)
				{
					salida=salida+arrayLong[i];
					if (i<(arrayLong.length-1))
						salida=salida+"|";
				}
			else if (arrayDouble!=null)
				for (int i=0; i<arrayDouble.length; i++)
				{
					salida=salida+arrayDouble[i];
					if (i<(arrayDouble.length-1))
						salida=salida+"|";
				}
			else if (arrayDouble!=null)
				for (int i=0; i<arrayDouble.length; i++)
				{
					salida=salida+arrayDouble[i];
					if (i<(arrayDouble.length-1))
						salida=salida+"|";
				}
			else if (arrayChar!=null)
				for (int i=0; i<arrayChar.length; i++)
				{
					salida=salida+arrayChar[i];
					if (i<(arrayChar.length-1))
						salida=salida+"|";
				}
			else if (arrayString!=null)
				for (int i=0; i<arrayString.length; i++)
				{
					salida=salida+arrayString[i];
					if (i<(arrayString.length-1))
						salida=salida+"|";
				}
			else if (arrayBoolean!=null)
				for (int i=0; i<arrayBoolean.length; i++)
				{
					salida=salida+arrayBoolean[i];
					if (i<(arrayBoolean.length-1))
						salida=salida+"|";
				}
		}
		return salida;
	}
	
}