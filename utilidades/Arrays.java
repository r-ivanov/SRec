package utilidades;

public class Arrays {

	
	public static boolean contiene(int n, int[]array)
	{
		for (int i=0; i<array.length; i++)
			if (array[i]==n)
				return true;
		return false;
	}
	
	
	public static boolean contiene(String n, String[]array)
	{
		for (int i=0; i<array.length; i++)
			if (array[i]!=null && array[i].equals(n))
				return true;
		return false;
	}
	
	
	public static int[] insertar(int n, int[]array)
	{
		int[]arrayN=new int[array.length+1];
		
		for (int i=0; i<array.length; i++)
			arrayN[i]=array[i];
		
		arrayN[arrayN.length-1]=n;
		return arrayN;
	}
	
	public static String[] insertar(String n, String[]array)
	{
		String[]arrayN=new String[array.length+1];
		
		for (int i=0; i<array.length; i++)
			arrayN[i]=""+array[i];
		
		arrayN[arrayN.length-1]=""+n;
		return arrayN;
	}
	
}
