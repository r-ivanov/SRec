package utilidades;

import java.io.File;
import java.util.ArrayList;

public class BuscadorMVJava
{

	public static String[] buscador(boolean enWindows)
	{
		String[] maquinas=new String[0];
		ArrayList<String> maq=new ArrayList<String>(1);
		int nivel=0;
		

		if (enWindows)	// Usamos sistema de ficheros de Windows, buscamos en C: y D:
		{
			File[] dir={
					new File("C:\\Program Files\\java"),
					new File("C:\\Archivos de programa\\java"),
					new File("C:\\java"),
					
					new File("D:\\Program Files\\java"),
					new File("D:\\Archivos de programa\\java"),
					new File("D:\\java")
				};
			
			for (int i=0; i<dir.length; i++)
				if (dir[i].exists() && dir[i].isDirectory())
					maq.addAll(buscador(dir[i],maq,nivel+1));
		}
		else
		{
			
		}
	
		maquinas = new String[maq.size()];
		
		for (int i=0; i<maquinas.length; i++)
		{
			if (!Arrays.contiene(maq.get(i), maquinas))
				maquinas[i]=maq.get(i);
		}

		int contador=0;
		
		// Miramos si hay posiciones nulas en array maquinas
		for (int i=0; i<maquinas.length; i++)
		{
			if (maquinas[i] != null)
				contador++;
		}
		
		String[] m=new String[contador];
		contador=0;
		
		//System.out.println("Longitud de maquinas = "+contador);
		
		for (int i=0; i<maquinas.length; i++)
		{
			if (maquinas[i] != null)
			{
				m[contador]=maquinas[i];
				contador++;
			}
		}
		
		return m;
	}
	
	
	
	
	public static ArrayList<String> buscador(File f, ArrayList<String> maq, int nivel)
	{
		File[] ficheros=f.listFiles();
		
		for (int i=0; i<ficheros.length; i++)
		{
			if (ficheros[i].isDirectory())
			{
				try {
					System.out.println("BUSCADOR[D]: ("+nivel+") ["+ficheros[i].getCanonicalPath()+"]");
				} catch (Exception e) {
					System.out.println("buscador println -> Exception");
				}
				
				if (	//!ficheros[i].getName().toLowerCase().contains("windows") &&
						//!ficheros[i].getName().toLowerCase().contains("documents and settings") &&
						nivel<4 &&
						(
							(nivel==1 && ficheros[i].getName().toLowerCase().contains("jdk") && ficheros[i].getName().toLowerCase().contains(".6"))
							||
							(nivel==2 && ficheros[i].getName().toLowerCase().contains("bin"))
						)
					)
				{
					System.out.println("NIVEL="+nivel+" ["+ficheros[i].getName().toLowerCase()+"]");
					maq.addAll(buscador(ficheros[i],maq,nivel+1));
				}
			}
			else
			{
				try {
					System.out.println("BUSCADOR[Archivo]: ("+nivel+") ["+ficheros[i].getCanonicalPath()+"] -"+ficheros[i].getName()+"-");

					if (ficheros[i].getName().toLowerCase().contains("javac.exe"))
					{
						System.out.println("Añadimos... "+ficheros[i].getCanonicalPath());
						maq.add(new String(ficheros[i].getCanonicalPath()));
					}

				} catch (Exception e) {
					System.out.println("buscador println -> Exception");
				}
			}
		}
		
		return maq;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
