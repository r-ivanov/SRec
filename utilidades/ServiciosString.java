/**
	Clase que ofrece servicios relacionados con Strings y caracteres

	@author Antonio Pérez Carrasco
	@version 2006-2007

*/
package utilidades;


import java.util.ArrayList;
import java.lang.NumberFormatException;
import java.net.InetAddress;


public class ServiciosString 
{
	static final boolean depurar=false;

	/**
		Devuelve una representación en String del objeto pasado com parámetro
		
		@param objeto
	*/
	public static String representacionInt(Object o)
	{
		String s="";
		return s+((Integer)o).intValue();
	}

	/**
		Devuelve una representación en String del objeto pasado com parámetro
		
		@param objeto
	*/
	public static String representacionChar(Object o)
	{
		String s="";
		return s+"\'"+((Character)o).charValue()+"\'";
	}

	/**
		Devuelve una representación en String del objeto pasado com parámetro
		
		@param objeto
	*/
	public static String representacionLong(Object o)
	{
		String s="";
		return s+((Long)o).longValue();
	}

	/**
		Devuelve una representación en String del objeto pasado com parámetro
		
		@param objeto
	*/
	public static String representacionDouble(Object o)
	{
		String s="";
		return s+((Double)o).doubleValue();
	}

	/**
		Devuelve una representación en String del objeto pasado com parámetro
		
		@param objeto
	*/
	public static String representacionShort(Object o)
	{
		String s="";
		return s+((Short)o).shortValue();
	}

	/**
		Devuelve una representación en String del objeto pasado com parámetro
		
		@param objeto
	*/
	public static String representacionFloat(Object o)
	{
		String s="";
		return s+((Float)o).floatValue();
	}
	
	
	/**
	Devuelve una representación en String del objeto pasado com parámetro
	
	@param objeto
	*/
	public static String truncarNumero(float f, int p)
	{
		String s=""+f;
		if (s.contains("."))
			if (p==0)
				s=s.substring(0,s.indexOf("."));
			else if (p+s.indexOf(".")<s.length())
				s=s.substring(0,s.indexOf(".")+1+p);
		
		return s;
	}
	
	/**
	Devuelve una representación en String del objeto pasado com parámetro
	
	@param objeto
	*/
	public static String truncarNumero(double f, int p)
	{
		String s=""+f;
		if (s.contains("."))
			if (p==0)
				s=s.substring(0,s.indexOf("."));
			else if (p+s.indexOf(".")<s.length())
				s=s.substring(0,s.indexOf(".")+1+p);
		
		return s;
	}

	/**
		Devuelve una representación en String del objeto pasado com parámetro
		
		@param objeto
	*/
	public static String representacionString(Object o)
	{
		return "\""+(String)o+"\"";
	}

	/**
		Devuelve una representación en String del objeto pasado com parámetro
		
		@param objeto
	*/
	public static String representacionBoolean(Object o)
	{
		String s="";
		return s+((Boolean)o).booleanValue();
	}
	
	/**
		Devuelve una representación en String del objeto pasado com parámetro
		
		@param objeto
	*/
	public static String representacionObjeto(Object o)
	{
		String cadena="";

		if (depurar) System.out.println("PanelEstado Entrando en objeto...");

		if (o==null)
			return "null";

		Class clase=o.getClass();


		if (clase.getCanonicalName().contains("[][]"))
		{
			Object objeto[]=(Object[])o;

			cadena=cadena+"{";
			if (depurar) System.out.println("PanelEstado Tiene mas dimensiones...");
			for (int i=0; i<objeto.length; i++)
			{
				cadena=cadena+representacionObjeto(objeto[i]);
				if ( i<(objeto.length-1) )
					cadena=cadena+",";
			}
			cadena=cadena+"}";
		}

		else if (clase.getCanonicalName().contains("int") ||
			clase.getCanonicalName().contains("Integer"))  // Si es integer
		{
			if (depurar) System.out.println("PanelEstado int");
			if (!(clase.getCanonicalName().contains("[]")))
				cadena=cadena+representacionInt(o);
			else
			{
				int array[]=(int[])o;
				cadena=cadena+"{";
				for (int y=0; y<array.length; y++)
				{
					cadena=cadena+representacionInt(array[y]);
					if (y<(array.length-1))
						cadena=cadena+",";
				}
				cadena=cadena+"}";
			}
		}

		else if (clase.getCanonicalName().contains("char") ||
			clase.getCanonicalName().contains("Character"))  // Si es char
		{
			if (depurar) System.out.println("PanelEstado char");
			if (!(clase.getCanonicalName().contains("[]")))
				cadena=cadena+representacionChar(o);
			else
			{
				char array[]=(char[])o;
				cadena=cadena+"{";
				for (int y=0; y<array.length; y++)
				{
					cadena=cadena+representacionChar(array[y]);
					if (y<(array.length-1))
						cadena=cadena+",";
				}
				cadena=cadena+"}";

			}
		}

		else if (clase.getCanonicalName().contains("long") ||
			clase.getCanonicalName().contains("Long"))  // Si es long
		{
			if (depurar) System.out.println("PanelEstado long");
			if (!(clase.getCanonicalName().contains("[]")))
				cadena=cadena+representacionLong(o);
			else
			{
				long array[]=(long[])o;
				cadena=cadena+"{";
				for (int y=0; y<array.length; y++)
				{
					cadena=cadena+representacionLong(array[y]);
					if (y<(array.length-1))
						cadena=cadena+",";
				}
				cadena=cadena+"}";
			}
		}

		else if (clase.getCanonicalName().contains("double") ||
			clase.getCanonicalName().contains("Double"))  // Si es double
		{
			if (depurar) System.out.println("PanelEstado double");
			if (!(clase.getCanonicalName().contains("[]")))	
				cadena=cadena+representacionDouble(o);
			else
			{
				double array[]=(double[])o;
				cadena=cadena+"{";
				for (int y=0; y<array.length; y++)
				{
					cadena=cadena+representacionDouble(array[y]);
					if (y<(array.length-1))
						cadena=cadena+",";
				}
				cadena=cadena+"}";

			}
		}

		else if (clase.getCanonicalName().contains("short") ||
			clase.getCanonicalName().contains("Short"))  // Si es short
		{
			if (depurar) System.out.println("PanelEstado short");
			if (!(clase.getCanonicalName().contains("[]")))	
				cadena=cadena+representacionShort(o);
			else
			{
				short array[]=(short[])o;
				cadena=cadena+"{";
				for (int y=0; y<array.length; y++)
				{
					cadena=cadena+representacionShort(array[y]);
					if (y<(array.length-1))
						cadena=cadena+",";
				}
				cadena=cadena+"}";

			}
		}

		else if (clase.getCanonicalName().contains("float") ||
			clase.getCanonicalName().contains("Float"))  // Si es float
		{
			if (depurar) System.out.println("PanelEstado float");
			if (!(clase.getCanonicalName().contains("[]")))	
				cadena=cadena+representacionFloat(o);
			else
			{
				float array[]=(float[])o;
				cadena=cadena+"{";
				for (int y=0; y<array.length; y++)
				{
					cadena=cadena+representacionFloat(array[y]);
					if (y<(array.length-1))
						cadena=cadena+",";
				}
				cadena=cadena+"}";
			}
		}

		else if (clase.getCanonicalName().contains("String") ||
			clase.getCanonicalName().contains("String"))  // Si es String
		{
			if (depurar) System.out.println("PanelEstado String");
			if (!(clase.getCanonicalName().contains("[]")))	
				cadena=cadena+representacionString(o);
			else
			{
				String array[]=(String[])o;
				cadena=cadena+"{";
				for (int y=0; y<array.length; y++)
				{
					cadena=cadena+representacionString(array[y]);
					if (y<(array.length-1))
						cadena=cadena+",";
				}
				cadena=cadena+"}";
			}
		}

		else if (clase.getCanonicalName().contains("boolean") ||
			clase.getCanonicalName().contains("Boolean"))  // Si es boolean
		{
			if (depurar) System.out.println("PanelEstado boolean");
			if (!(clase.getCanonicalName().contains("[]")))
				cadena=cadena+representacionBoolean(o);
			else
			{
				boolean array[]=(boolean[])o;
				cadena=cadena+"{";
				for (int y=0; y<array.length; y++)
				{
					cadena=cadena+representacionBoolean(array[y]);
					if (y<(array.length-1))
						cadena=cadena+",";
				}
				cadena=cadena+"}";
			}
		}

		else
			if (depurar) System.out.println("ServiciosString: Tipo no reconocido ("+o.getClass().getCanonicalName()+")");


		return cadena;
	}
	
	/**
		Determina si un carácter es un número
		
		@param un carácter
		@return true si el carácter es un número
	*/
	public static boolean esNumero(char a)
	{
		return (a=='0' || a=='1' || a=='2' || a=='3' || a=='4' ||
				a=='5' || a=='6' || a=='7' || a=='8' || a=='9');
	}
	
	/**
		Determina si un carácter es una letra
		
		@param un carácter
		@return true si el carácter es una letra
	*/
	public static boolean esLetra(char a)
	{
		return (a=='a' || a=='b' || a=='c' || a=='d' || a=='e' || 
			a=='f' || a=='g' || a=='h' || a=='i' || a=='j' ||
			a=='k' || a=='l' || a=='m' || a=='n' || a=='o' ||
			a=='p' || a=='q' || a=='r' || a=='s' || a=='t' ||
			a=='u' || a=='v' || a=='w' || a=='x' || a=='y' ||
			a=='z' || a=='A' || a=='B' || a=='C' || a=='D' ||
			a=='E' || a=='F' || a=='G' || a=='H' || a=='I' ||
			a=='J' || a=='K' || a=='L' || a=='M' || a=='N' ||
			a=='O' || a=='P' || a=='Q' || a=='R' || a=='S' ||
			a=='T' || a=='U' || a=='V' || a=='W' || a=='X' ||
			a=='Y' || a=='Z' || 
			
			a=='Ñ' || a=='ñ' ||
			
			a=='á' || a=='é' || a=='í' || a=='ó' || a=='ú' || 
			a=='Á' || a=='É' || a=='Í' || a=='Ó' || a=='Ú' );


	}
		
	public static boolean hayCadenasRepetidas(String s[])
	{
		for (int i=0; i<s.length; i++)
			for (int j=i; j<s.length; j++)
				if (s[i].equals(s[j]) && i!=j)
					return true;
		return false;
	}
	
	
	
	public static String cadenaColorHex(int r, int g, int b)
	{
		String rojo= Integer.toHexString(r);
		String verde=Integer.toHexString(g);
		String azul= Integer.toHexString(b);
		
		if (rojo.length()==1)
			rojo="0"+rojo;
		if (verde.length()==1)
			verde="0"+verde;
		if (azul.length()==1)
			azul="0"+azul;
		
		return rojo+verde+azul;
		
	}
	
	
	public static boolean contieneCadena(String s[],String c,boolean exacta)
	{
		// exacta=T -> return true si contiene una posición la cadena exacta			( s[i].length()  = c.length() )
		// exacta=F -> return true si contiene una posición la cadena en su interior	( s[i].length() >= c.length() )
	
		for (int i=0; i<s.length; i++)
			if ((s[i].equals(c) && exacta) || (!exacta && s[i].contains(c)))
				return true;
		return false;
	}
	
	
	public static String cadenaDimensiones(int n)
	{
		String cadena="";
	
		for (int i=0; i<n; i++)
			cadena=cadena+"[]";

		return cadena;
	}
	
	public static boolean esDeTipoCorrecto(String valor, String tipo, int dim)
	{
		if (dim==0)
		{
			// Quitar espacios de delante de y detrás si los hay
			while (valor.charAt(0)==' ')
				valor=valor.substring(1,valor.length());
			while (valor.charAt(valor.length()-1)==' ')
				valor=valor.substring(0,valor.length()-1);
				
			// Comprobación según los tipos
			if (tipo.equals("int") || tipo.equals("short") || tipo.equals("byte"))
			{
				try {
					Integer.parseInt(valor);
				} catch (NumberFormatException nfe) {
					return false;
				}
			}
			else if (tipo.equals("long"))
			{
				try {
					Long.parseLong(valor);
				} catch (NumberFormatException nfe) {
					return false;
				}
			}
			else if (tipo.equals("double") || tipo.equals("float"))
			{
				try {
					Double.parseDouble(valor);
				} catch (NumberFormatException nfe) {
					return false;
				}
			}
			
			else if (tipo.equals("boolean"))
			{
				valor=valor.toUpperCase();
				if (!valor.equals("TRUE") && !valor.equals("FALSE"))
					return false;
			}				
			else if (tipo.equals("String"))
			{
				//Formato permitido: "hola"
				if ((valor.charAt(0)!='\"')||(valor.charAt(valor.length()-1)!='\"'))
					return false;
				for (int i=1; i<valor.length()-1; i++)
					if (!(ServiciosString.esNumero(valor.charAt(i)) || ServiciosString.esLetra(valor.charAt(i)) || valor.charAt(i)=='_'))
						return false;
			}
			else if (tipo.equals("char"))
			{
				/*
				if (valor.length()>1)
					return false;
				*/
				//Formato permitido: 'a'
				if (valor.length()!=3)
					return false;
				if ((valor.charAt(0)!='\'')||(valor.charAt(2)!='\''))
					return false;
				if (!ServiciosString.esNumero(valor.charAt(1)) && !ServiciosString.esLetra(valor.charAt(1)) && !(valor.charAt(1)=='_'))
					return false;
			}
		}
		else if (dim==1)
		{
			// Manejar cadenas de tipo    " { 4 , 5 , 6 , 7 } "    o    " { a , b , c , d } "    o    " { cadena , cad , ca , c } "
			
			// Quitar espacios de delante de y detrás si los hay
			while (valor.charAt(0)==' ')
				valor=valor.substring(1,valor.length());
			while (valor.charAt(valor.length()-1)==' ')
				valor=valor.substring(0,valor.length()-1);
						
			// Quitar controladamente las llaves
			if (valor.charAt(0)=='{' && valor.charAt(valor.length()-1)=='}')
				valor=valor.substring(1,valor.length()-1);
			else
				return false;
			
			// Extraer valores y comprobar comas, omitiendo espacios
			ArrayList<String> cadenas=new ArrayList<String>(0);
			String valor2=new String(valor);
			while (valor2.length()>0)
			{
				try {
					cadenas.add(valor2.substring(0, valor2.indexOf(",")));
					valor2=valor2.substring( valor2.indexOf(",")+1 , valor2.length() );
				} catch (Exception e) {
					cadenas.add(valor2.substring(0, valor2.length()));
					valor2="";
				}
			}
					
			// Sobre cada valor, llamada recursiva, con dim=0
			for (int i=0; i<cadenas.size(); i++)
			{
				if (!esDeTipoCorrecto(cadenas.get(i),tipo,0))
					return false;
			}
		}
		else if (dim==2)
		{
			// Manejar cadenas de tipo    " { { 4 , 5 , 6 , 7 } , { 4 , 5 , 6 , 7 } , { 4 , 5 , 6 , 7 } , { 4 , 5 , 6 , 7 } } "
			
			// Quitar espacios de delante de y detrás si los hay
			while (valor.charAt(0)==' ')
				valor=valor.substring(1,valor.length());
			while (valor.charAt(valor.length()-1)==' ')
				valor=valor.substring(0,valor.length()-1);

			// Quitar controladamente las llaves 'grandes'
			if (valor.charAt(0)=='{' && valor.charAt(valor.length()-1)=='}')
				valor=valor.substring(1,valor.length()-1);
			else
				return false;
			
			String valor2=new String(valor);
			
			// Extraer arrays y comprobar comas, omitiendo espacios
			int numArrays=ServiciosString.numVeces(valor, "{");
			String arrays[]=new String[numArrays];
			
			for (int i=0; i<arrays.length; i++)
			{
				if (i<(arrays.length-1))
				{
					arrays[i]=valor2.substring(valor2.indexOf("{"),valor2.indexOf("}")+1);
					valor2=valor2.substring(valor2.indexOf("}")+1,valor2.length());
					valor2=valor2.substring(valor2.indexOf(",")+1,valor2.length());
				}
				else
					arrays[i]=valor2+"";
			}
			
			// Sobre cada array, llamada recursiva, con dim=1
			for (int i=0; i<arrays.length; i++)
			{
				if (!esDeTipoCorrecto(arrays[i],tipo,1))
					return false;
			}
		}
		
		return true;
	}
	
	
	
	
	public static String[] quitarCadenasRepetidas(String []cadenas)
	{
		//for (int i=0; i<cadenas.length; i++)
		//	System.out.println(">> ("+i+"/"+cadenas.length+") > "+cadenas[i]);
		
		for (int i=0; i<cadenas.length; i++)
		{
			for (int j=i+1; j<cadenas.length; j++)
			{
				if (cadenas[i]!=null && cadenas[j]!=null && cadenas[i].equals(cadenas[j]))
					cadenas[j]=null;
			}
		}
		
		//for (int i=0; i<cadenas.length; i++)
		//	System.out.println(">> ("+i+"/"+cadenas.length+") > "+cadenas[i]);
		
		int numNoNulas=0;
		for (int i=0; i<cadenas.length; i++)
			if (cadenas[i]!=null)
				numNoNulas++;
		
		int c=0;
		String[] cadenasUnicas=new String[numNoNulas];
		for (int i=0; i<cadenas.length; i++)
			if (cadenas[i]!=null)
			{
				cadenasUnicas[c]=cadenas[i];
				c++;
			}
			
		return cadenasUnicas;
		
		
	}
	
	public static String[] obtenerPrefijos(String []nombres)
	{
		int longitudMinima=nombres[0].length();
		int posLongitudMinima=0;
		
		for (int i=0; i<nombres.length; i++)
			if (nombres[i].length()<longitudMinima)
			{
				longitudMinima=nombres[i].length();
				posLongitudMinima=i;
			}
				
		// Calculamos parte comun a todos
		boolean finComun=false;
		int y=0;
		while (y<nombres[posLongitudMinima].length() && !finComun)
		{
			char c=nombres[0].charAt(y);
			int j=0;
			while (j<nombres.length  && !finComun)
			{
				if (c!=nombres[j].charAt(y))
					finComun=true;
				j++;
			}
			y++;
		}
		String comun=nombres[0].substring(0,y-1);
			
			
		String prefijos[]=new String[nombres.length];
		
		// Asignamos parte inicial común
		for (int i=0; i<prefijos.length; i++)
			prefijos[i]=comun;
			
				
		for (int i=0; i<prefijos.length; i++)
			if (nombres[i].length()>comun.length())
				prefijos[i]=prefijos[i]+nombres[i].charAt(comun.length());
		
		// Bucle mientras haya repetidos, para ir incrementando longitud de los identificadores hasta que sean distintos
		while (ServiciosString.hayCadenasRepetidas(prefijos))
		{
			for (int i=0; i<prefijos.length; i++)
			{
				boolean encontradoOtroIgual=false;
				for (int j=0; j<prefijos.length; j++)
				{
					if (prefijos[i].equals(prefijos[j]) && i!=j)
					{
						if (prefijos[j].length()!=nombres[j].length())
							prefijos[j]=prefijos[j]+nombres[j].charAt(prefijos[j].length());
						encontradoOtroIgual=true;
					}
				}
				if (encontradoOtroIgual)
					if (prefijos[i].length()!=nombres[i].length())
						prefijos[i]=prefijos[i]+nombres[i].charAt(prefijos[i].length());
			}		
		}
		//for (int i=0; i<nombres.length; i++)
		//	System.out.println(i+": \t"+nombres[i]+"\t\t"+prefijos[i]);		
		return prefijos;
	}
	
	
	
	// Número de veces que cadena contiene cad
	public static int vecesQueContiene(String cadena, String cad)
	{
		int contador=0;
		
		while (cadena.contains(cad))
		{
			contador++;
			
			cadena=cadena.substring( cadena.indexOf(cad)+cad.length() , cadena.length() );
		}
		
		/*System.out.println("Funcion vecesQueContiene\n------------------------");
		System.out.println("cadena='"+cadena+"'");
		System.out.println("cad='"+cad+"'");
		System.out.println("resultado='"+contador+"'\n");*/
		
		return contador;
	}
	
		/**
		Convierte un array de booleanos (true,true,false,true,false,...) a String ("11010...")
		
		@param valores valores booleanos
		@return cadena de caracteres compuesta de '1' y '0'.
	*/
	public static String booleanArrayToString(boolean valores[])
	{
		String s="";
				
		for (int i=0; i<valores.length; i++)
			if (valores[i])
				s=s+"1";
			else
				s=s+"0";
		return s;
	}
	
	/**
	Convierte un String en un array de booleanos
	
	@param s cadena de carácteres de la forma "1101010010101..."
	@return array de booleans en función de los valores introducidos en la cadena s
	 */
	private boolean[] stringToBooleanArray(String s)
	{
		boolean resultado[] = new boolean[s.length()];
		
		for (int i=0; i<s.length(); i++)
			resultado[i]=s.charAt(i)=='1';
		return resultado;
	}
	
	public static String tab(int n)
	{
		String s="";
		
		for (int i=0; i<n; i++)
			s=s+" ";
		return s;
	}
	
	
	
	public static String simplificarClase(String s)
	{
		if (s.contains("Integer"))
			return "int";
		else if (s.contains("Byte"))
			return "byte";
		else if (s.contains("Short"))
			return "short";
		else if (s.contains("Long"))
			return "long";
		else if (s.contains("Float"))
			return "float";
		else if (s.contains("Double"))
			return "double";
		else if (s.contains("Char"))
			return "char";
		else if (s.contains("String"))
			return "String";
		else if (s.contains("Boolean"))
			return "boolean";
		else
			return s;
	}
	
	
	

	
	public static int[] extraerValoresInt(String s,char car)
	{
		int numElem=1;
		
		for (int i=0; i<s.length(); i++)
			if (s.charAt(i)==car)
				numElem++;
				
		int arraySalida[]=new int[numElem];
				
		s=s.replace(" ","");	
				
		for (int i=0; i<numElem; i++)
		{
			try {
				if (s.contains(""+car))
				{
					int posBarra=s.indexOf(car);
					arraySalida[i]=Integer.parseInt( s.substring(0,posBarra));
					s=s.substring( s.indexOf(car)+1,s.length());
				}
				else
					arraySalida[i]=Integer.parseInt( s);
			} catch (Exception e) {
				//e.printStackTrace();
				return null;
			}
		}
		return arraySalida;
	}
	
	public static long[] extraerValoresLong(String s,char car)
	{
		int numElem=1;
		
		for (int i=0; i<s.length(); i++)
			if (s.charAt(i)==car)
				numElem++;
				
		long arraySalida[]=new long[numElem];
				
		s=s.replace(" ","");	
				
		for (int i=0; i<numElem; i++)
		{
			try {
				if (s.contains(""+car))
				{
					int posBarra=s.indexOf(car);
					arraySalida[i]=Long.parseLong( s.substring(0,posBarra));
					s=s.substring( s.indexOf(car)+1,s.length());
				}
				else
					arraySalida[i]=Long.parseLong( s.substring(0,s.length()));
			} catch (Exception e) {
				return null;
			}
		}
		return arraySalida;
	}
	
	public static double[] extraerValoresDouble(String s,char car)
	{
		int numElem=1;
		
		for (int i=0; i<s.length(); i++)
			if (s.charAt(i)==car)
				numElem++;
				
		double arraySalida[]=new double[numElem];
				
		s=s.replace(" ","");	
				
		for (int i=0; i<numElem; i++)
		{
			try {
				if (s.contains(""+car))
				{
					int posBarra=s.indexOf(car);
					arraySalida[i]=Double.parseDouble( s.substring(0,posBarra));
					s=s.substring( s.indexOf(car)+1,s.length());
				}
				else
					arraySalida[i]=Double.parseDouble( s.substring(0,s.length()));
			} catch (Exception e) {
				return null;
			}
		}
		return arraySalida;
	}
	
	public static char[] extraerValoresChar(String s,char car)
	{
		int numElem=1;
		
		for (int i=0; i<s.length(); i++)
			if (s.charAt(i)==car)
				numElem++;
				
		char arraySalida[]=new char[numElem];
				
		s=s.replace(" ","");	
				
		for (int i=0; i<numElem; i++)
		{
			try {
				if (s.contains(""+car))
				{
					int posBarra=s.indexOf(car);
					arraySalida[i]=s.charAt(0);
					s=s.substring( s.indexOf(car)+1,s.length());
				}
				else
					arraySalida[i]=s.charAt(0);
			} catch (Exception e) {
				return null;
			}
		}
		return arraySalida;
	}
	
	
	public static String[] extraerValoresString(String s,char car)
	{
		int numElem=1;
		
		for (int i=0; i<s.length(); i++)
			if (s.charAt(i)==car)
				numElem++;
				
		String arraySalida[]=new String[numElem];
				
		s=s.replace(" ","");	
				
		for (int i=0; i<numElem; i++)
		{
			try {
				if (s.contains(""+car))
				{
					int posBarra=s.indexOf(car);
					arraySalida[i]=( s.substring(0,posBarra));
					s=s.substring( s.indexOf(car)+1,s.length());
				}
				else
					arraySalida[i]=( s.substring(0,s.length()));
			} catch (Exception e) {
				return null;
			}
		}
		return arraySalida;
	}	
	
	
	public static boolean[] extraerValoresBoolean(String s,char car)
	{
		int numElem=1;
		
		for (int i=0; i<s.length(); i++)
			if (s.charAt(i)==car)
				numElem++;
				
		boolean arraySalida[]=new boolean[numElem];
				
		s=s.replace(" ","");	
				
		for (int i=0; i<numElem; i++)
		{
			try {
				if (s.contains(""+car))
				{
					int posBarra=s.indexOf(car);
					arraySalida[i]=Boolean.parseBoolean( s.substring(0,posBarra));
					s=s.substring( s.indexOf(car)+1,s.length());
				}
				else
					arraySalida[i]=Boolean.parseBoolean( s.substring(0,s.length()));
			} catch (Exception e) {
				return null;
			}
		}
		return arraySalida;
	}
	
	
	
	
	public static String[] adecuarParametros(String[] valores, String[] tipos, int[] dimensiones)
	{
		for (int i=0; i<valores.length; i++)
		{
			if (dimensiones[i]==0)	// Valores cardinales
				valores[i]=adecuarParametroDim0(valores[i],tipos[i]);
			else if (dimensiones[i]==1)	// Valores en un array
				valores[i]=adecuarParametroDim1(valores[i],tipos[i]);
			else	// Un array de arrays
				valores[i]=adecuarParametroDim2(valores[i],tipos[i]);
		}
		
		return valores;
	}
	
	public static String adecuarParametro(String valor, String tipo, int dimension)
	{
			if (dimension==0)	// Valores cardinales
				valor=adecuarParametroDim0(valor,tipo);
			else if (dimension==1)	// Valores en un array
				valor=adecuarParametroDim1(valor,tipo);
			else	// Un array de arrays
				valor=adecuarParametroDim2(valor,tipo);
		
		return valor;
	}
	
	public static String adecuarParametroDim0(String valor, String tipo)
	{
		while (	!ServiciosString.esLetra(valor.charAt(0)) && valor.charAt(0)!='-' && valor.charAt(0)!='.' && 
				!ServiciosString.esNumero(valor.charAt(0)))
			valor=valor.substring(1,valor.length());
		
		while (	!ServiciosString.esLetra(valor.charAt(valor.length()-1)) && valor.charAt(0)!='.' && 
				!ServiciosString.esNumero(valor.charAt(valor.length()-1)))
			valor=valor.substring(0,valor.length()-1);
		
		
		return valor;
	}
	
	public static String adecuarParametroDim1(String valor, String tipo)
	{
		String valorNuevo="{";
		
		// " { x , x , x , x } "
		
		//Quitamos llaves
		valor=valor.replace("{", "");
		valor=valor.replace("}", "");
		
		// Cogemos parámetros y los arreglamos, uno por uno
		String []valoresNuevos=new String[numVeces(valor,",")+1];
		
		for (int i=0; i<valoresNuevos.length; i++)
		{
			if (i<(valoresNuevos.length-1))
			{
				valoresNuevos[i]=valor.substring(0,valor.indexOf(","));
				valor=valor.substring(valor.indexOf(",")+1,valor.length());
			}
			else
				valoresNuevos[i]=valor+"";
			
			valoresNuevos[i]=adecuarParametroDim0(valoresNuevos[i],tipo);
		}
		
		// Los insertamos en la cadena final
		for (int i=0; i<valoresNuevos.length; i++)
		{
			if (i<(valoresNuevos.length-1))
				valorNuevo=valorNuevo+valoresNuevos[i]+",";
			else
				valorNuevo=valorNuevo+valoresNuevos[i];
		}
		
		valorNuevo=valorNuevo+"}";
		
		return valorNuevo;
	}
	
	public static String adecuarParametroDim2(String valor, String tipo)
	{
		String valorNuevo="{";
		
		// " { x , x , x , x } "
		
		//Quitamos llaves inicial y final
		while (	valor.charAt(0)!='{' )
			valor=valor.substring(1,valor.length());
		valor=valor.substring(1,valor.length());
		
		while (	valor.charAt(valor.length()-1)!='}' )
			valor=valor.substring(0,valor.length()-1);
		valor=valor.substring(0,valor.length()-1);
		
		//Tratamos cada array por separado
		String []valoresNuevos=new String[numVeces(valor,"{")];
		
		for (int i=0; i<valoresNuevos.length; i++)
		{
			if (i<(valoresNuevos.length-1))
			{
				valoresNuevos[i]=valor.substring(valor.indexOf("{"),valor.indexOf("}")+1);
				valor=valor.substring(valor.indexOf("}")+1,valor.length());
				valor=valor.substring(valor.indexOf(",")+1,valor.length());
			}
			else
				valoresNuevos[i]=valor+"";
			
			valoresNuevos[i]=adecuarParametroDim1(valoresNuevos[i],tipo);
		}
		
		
		
		// Los insertamos en la cadena final
		for (int i=0; i<valoresNuevos.length; i++)
		{
			if (i<(valoresNuevos.length-1))
				valorNuevo=valorNuevo+valoresNuevos[i]+",";
			else
				valorNuevo=valorNuevo+valoresNuevos[i];
		}
		
		valorNuevo=valorNuevo+"}";
		
		return valorNuevo;
	}
	
	
	
	public static int numVeces(String cadena,String muestra)
	{
		int veces=0;
		while (cadena.contains(muestra))
		{
			veces++;
			cadena=cadena.substring( cadena.indexOf(muestra)+muestra.length() , cadena.length() );
		}

		return veces;
	}
	
	
	public static boolean tieneContenido(String cadena)
	{
		if (cadena==null)
			return false;
		
		for (int i=0; i<cadena.length(); i++)
			if (esLetra(cadena.charAt(i)) || esNumero(cadena.charAt(i)) )
				return true;
		
		return false;
	}
	
	
	public static String direccionIP()
	{
		InetAddress a=null;
		try {
			a = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
			
		}
		String dir=(""+a).replace("/","");
		
		
		return dir;
	}
	
	
}