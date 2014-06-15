/**
	Genera números aleatorios cuyo valor absoluto es menor o igual que un valor limite dado. Puede generar sólo valores positivos
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/
package utilidades;



import java.util.Random;


public class ValorAleatorio
{
	
	static Random r=new Random();
	
	
	
	private static int generaInt(int x)	
	{
		//System.out.println("MAX="+x);
		if (x==2147483647)
			x=-1;
		return r.nextInt(x+1);
	}
	
	
	private static long generaLong()
	{
		return r.nextLong();
	}
	
	private static float generaFloat()
	{
		return r.nextInt();
	}
	
	private static double generaDouble()
	{
		return r.nextInt();
	}
	
	private static String caracter(int x)
	{
		if (x<0)
			x=x*(-1);
		x=x%37;	
		
		switch(x)
		{
			case  0: return "a";
			case  1: return "b";
			case  2: return "c";
			case  3: return "d";
			case  4: return "e";
			case  5: return "f";
			case  6: return "g";
			case  7: return "h";
			case  8: return "i";
			case  9: return "j";
			case 10: return "k";
			case 11: return "l";
			case 12: return "m";
			case 13: return "n";
			case 14: return "ñ";
			case 15: return "o";
			case 16: return "p";
			case 17: return "q";
			case 18: return "r";
			case 19: return "s";
			case 20: return "t";
			case 21: return "u";
			case 22: return "v";
			case 23: return "w";
			case 24: return "x";
			case 25: return "y";
			case 26: return "z";
			case 27: return "0";
			case 28: return "1";
			case 29: return "2";
			case 30: return "3";
			case 31: return "4";
			case 32: return "5";
			case 33: return "6";
			case 34: return "7";
			case 35: return "8";
			case 36: return "9";
		}
		return "a";
	}	
	
	
	
	public static int generaInt(int min, int max)
	{
		int x;
		
		if (max<min)
		{
			max=max+min;
			min=max;
			max=max-min;
		}
		if (max==min)
			return max;
		
		//System.out.print("min="+min+"\tmax="+max+" Maximo="+Math.max(Math.abs(max),Math.abs(min)));
		do
		{
			//System.out.print("min="+min+"\tmax"+max+" Maximo="+Math.max(Math.abs(max),Math.abs(min)));
			x=generaInt(Math.max(Math.abs(max),Math.abs(min)));
			//System.out.print("  Candidato1="+x);
			if (generaBoolean() && max!=(-1))
				x=x%(max+1);
			else if (generaBoolean() && min!=(-1))
				x=((x%(min+1))*(-1))-1;
			//System.out.println("\t\tCandidato2="+x);

		}
		while (x<min || x>max);
		return x;

	}
	
	public static short generaShort(int min, int max)
	{
		 return (short)generaInt(min,max);
	}
	
	public static byte generaByte(int min, int max)
	{
		return (byte)generaInt(min,max);
	}

	public static long generaLong(long min, long max)
	{
		long x;
		
		if (max<min)
		{
			max=max+min;
			min=max;
			max=max-min;
		}
		if (max==min)
			return max;
			
		do
		{
			x=generaLong();
			//System.out.print("Candidato1="+x);
			if (generaBoolean() && max!=(-1))
				x=x%(max+1);
			else if (generaBoolean() && min!=(-1))
				x=((x%(min+1))*(-1))-1;
			//System.out.println("\t\tCandidato2="+x);

		}
		while (x<min || x>max);
		return x;
	}
	
	public static double generaDouble(double min, double max)
	{
		double x;
		boolean limNegativos=false;
		
		
		if (max<min)
		{
			max=max+min;
			min=max;
			max=max-min;
		}
		if (max==min)
			return max;

		if (max<0 && min<0)
		{
			limNegativos=true;
			double aux=max;
			max=min*(-1);
			min=aux*(-1);
		}
			
		do
		{
			int contador=0;
			x=generaDouble();
			x=truncarDoble(x);
			
			while (x>max && contador<10)
			{
				x=x/10;
				contador++;
			}
			while (x<min && contador<10)
			{
				x=x*10;
				contador++;
			}
			
			if (generaBoolean())
				x=x*(-1);


		}
		while (x<min || x>max);
		


		
		if (generaBoolean())
		{
			if (truncarDoble((x*1000))>min && truncarDoble((x*1000))<max && generaBoolean())
				x=x*1000;
			if (truncarDoble((x*100))>min && truncarDoble((x*100))<max && generaBoolean())
				x=x*100;
			if (truncarDoble((x*10))>min && truncarDoble((x*10))<max && generaBoolean())
				x=x*10;
		}
		else if (generaBoolean())
		{
			if (truncarDoble((x/1000))<max && truncarDoble((x/1000))>min && generaBoolean())
				x=x/1000;
			if (truncarDoble((x/100))<max && truncarDoble((x/100))>min && generaBoolean())
				x=x/100;
			if (truncarDoble((x/10))<max && truncarDoble((x/10))>min && generaBoolean())
				x=x/10;
		}
		
		if (limNegativos)	// Si límites eran negativos
			x=x*(-1);		// hacemos valor negativo
		x=truncarDoble(x);

		return x;
	}
	

	private static double truncarDoble(double x)
	{
		Double xx=new Double(x);
		String numero=xx.toString();
		//System.out.println("- numero="+numero);
		
		if (numero.contains("E"))
			numero=numero.substring(0,numero.indexOf(('E')));
			
		//System.out.println("  numero="+numero);
		
		x=Double.parseDouble(numero);
		return x;
	}
	
		
	
	public static boolean generaBoolean()
	{
		return r.nextBoolean();
	}
	
	public static String generaString (int longitud)
	{
		String s="";
		
		if (longitud>50)
			longitud=50;
		
		for (int i=0; i<longitud; i++)
			s=s+caracter(generaInt(1,37));
		
		return s;
	}
	
	public static String generaString ()
	{
		return generaString(generaInt(3,10));
	}
	
	public static char generaChar ()
	{
		return generaString(1).charAt(0);
	}

	
	public static void main(String argc[])
	{
		//for (int i=0; i<50; i++)
		//	System.out.println("int:\t"+generaInt(4000,true));
		
		//int x=0;
		
		//for (int i=0; i<60; i++)
		//	System.out.println(">\t"+generaChar());//(-0.1,0.5));
		
		for (int i=0; i<60; i++)
			truncarDoble(generaDouble());
		
	}
}