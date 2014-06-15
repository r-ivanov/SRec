package cuadros;

import java.util.ArrayList;

import javax.swing.JComboBox;


public class ValoresParametros {

	
	static public ArrayList<String> paramInt[]=new ArrayList[3];
	static public ArrayList<String> paramLong[]=new ArrayList[3];
	static public ArrayList<String> paramDouble[]=new ArrayList[3];
	static public ArrayList<String> paramString[]=new ArrayList[3];
	static public ArrayList<String> paramChar[]=new ArrayList[3];
	static public ArrayList<String> paramBoolean[]=new ArrayList[3];
	

	public static void inicializar(boolean forzar)
	{
		// Si es necesario reinicializar o se fuerza a ello...
		if (ValoresParametros.paramInt[0]==null || forzar)	
			for (int i=0; i<ValoresParametros.paramInt.length; i++)
			{
				ValoresParametros.paramInt[i]=new ArrayList<String>();
				ValoresParametros.paramLong[i]=new ArrayList<String>();
				ValoresParametros.paramDouble[i]=new ArrayList<String>();
				ValoresParametros.paramString[i]=new ArrayList<String>();
				ValoresParametros.paramChar[i]=new ArrayList<String>();
				ValoresParametros.paramBoolean[i]=new ArrayList<String>();
			
				ValoresParametros.paramInt[i].add("");
				ValoresParametros.paramLong[i].add("");
				ValoresParametros.paramDouble[i].add("");
				ValoresParametros.paramString[i].add("");
				ValoresParametros.paramChar[i].add("");
				ValoresParametros.paramBoolean[i].add("");
			}
	}
	
	protected static void introducirValor(JComboBox jc,String clase,int dim,String valor,boolean hacerSeleccionado)
	{
		int contenidoEn=-1;
		if (clase.contains("Integer") || clase.contains("int") || 
				clase.contains("Byte") || clase.contains("byte") || 
				clase.contains("Short") || clase.contains("short"))
		{
			for (int i=0; i<ValoresParametros.paramInt[dim].size(); i++)
				if (ValoresParametros.paramInt[dim].get(i).equals(valor))
					contenidoEn=i;
			if (contenidoEn==-1)
			{
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount()-1);
			}
			else
				jc.setSelectedIndex(contenidoEn);
		}
		else if (clase.contains("Long") || clase.contains("long"))
		{
			for (int i=0; i<ValoresParametros.paramLong[dim].size(); i++)
				if (ValoresParametros.paramLong[dim].get(i).equals(valor))
					contenidoEn=i;
			if (contenidoEn==-1)
			{
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount()-1);
			}
			else
				jc.setSelectedIndex(contenidoEn);
		}
		else if (clase.contains("Float") || clase.contains("float") || 
				clase.contains("Double") || clase.contains("double"))
		{
			for (int i=0; i<ValoresParametros.paramDouble[dim].size(); i++)
				if (ValoresParametros.paramDouble[dim].get(i).equals(valor))
					contenidoEn=i;
			if (contenidoEn==-1)
			{
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount()-1);
			}
			else
				jc.setSelectedIndex(contenidoEn);
		}
		else if (clase.contains("String"))
		{
			for (int i=0; i<ValoresParametros.paramString[dim].size(); i++)
				if (ValoresParametros.paramString[dim].get(i).equals(valor))
					contenidoEn=i;
			if (contenidoEn==-1)
			{
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount()-1);
			}
			else
				jc.setSelectedIndex(contenidoEn);
		}
		else if (clase.contains("Character") || clase.contains("char"))
		{
			for (int i=0; i<ValoresParametros.paramChar[dim].size(); i++)
				if (ValoresParametros.paramChar[dim].get(i).equals(valor))
					contenidoEn=i;
			if (contenidoEn==-1)
			{
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount()-1);
			}
			else
				jc.setSelectedIndex(contenidoEn);
		}
		else if (clase.contains("boolean"))
		{
			for (int i=0; i<ValoresParametros.paramBoolean[dim].size(); i++)
				if (ValoresParametros.paramBoolean[dim].get(i).equals(valor))
					contenidoEn=i;
			if (contenidoEn==-1)
			{
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount()-1);
			}
			else
				jc.setSelectedIndex(contenidoEn);
		}
	}
	
	protected static void introducirValores(JComboBox jc, String clase, int dim)
	{
		if (clase.contains("Integer") || clase.contains("int") || 
				clase.contains("Byte") || clase.contains("byte") || 
				clase.contains("Short") || clase.contains("short"))
		{
			for (int i=0; i<ValoresParametros.paramInt[dim].size(); i++)
				jc.addItem(ValoresParametros.paramInt[dim].get(i));
		}
		else if (clase.contains("Long") || clase.contains("long"))
		{
			for (int i=0; i<ValoresParametros.paramLong[dim].size(); i++)
				jc.addItem(ValoresParametros.paramLong[dim].get(i));
		}
		else if (clase.contains("Float") || clase.contains("float") || 
				clase.contains("Double") || clase.contains("double"))
		{
			for (int i=0; i<ValoresParametros.paramDouble[dim].size(); i++)
				jc.addItem(ValoresParametros.paramDouble[dim].get(i));
		}
		else if (clase.contains("String"))
		{
			for (int i=0; i<ValoresParametros.paramString[dim].size(); i++)
				jc.addItem(ValoresParametros.paramString[dim].get(i));			
		}
		else if (clase.contains("Character") || clase.contains("char"))
		{
			for (int i=0; i<ValoresParametros.paramChar[dim].size(); i++)
				jc.addItem(ValoresParametros.paramChar[dim].get(i));
		}
		else if (clase.contains("boolean"))
		{
			for (int i=0; i<ValoresParametros.paramBoolean[dim].size(); i++)
				jc.addItem(ValoresParametros.paramBoolean[dim].get(i));
		}
			
	}
	
	
	

	protected static void anadirValorListados(String valor,String clase,int dim)
	{
		valor.replace(" ","");
		//System.out.println("Añadimos el valor: ["+valor+"]");
		
		boolean yaContiene=false;
		if (clase.contains("Integer") || clase.contains("int") || 
				clase.contains("Byte") || clase.contains("byte") || 
				clase.contains("Short") || clase.contains("short"))
		{
			for (int i=0; i<ValoresParametros.paramInt[dim].size(); i++)
				if (ValoresParametros.paramInt[dim].get(i).equals(valor))
					yaContiene=true;
			if (!yaContiene)
				ValoresParametros.paramInt[dim].add(valor);
		}
		else if (clase.contains("Long") || clase.contains("long"))
		{
			for (int i=0; i<ValoresParametros.paramLong[dim].size(); i++)
				if (ValoresParametros.paramLong[dim].get(i).equals(valor))
					yaContiene=true;
			if (!yaContiene)
				ValoresParametros.paramLong[dim].add(valor);
		}
		else if (clase.contains("Float") || clase.contains("float") || 
				clase.contains("Double") || clase.contains("double"))
		{
			for (int i=0; i<ValoresParametros.paramDouble[dim].size(); i++)
				if (ValoresParametros.paramDouble[dim].get(i).equals(valor))
					yaContiene=true;
			if (!yaContiene)
				ValoresParametros.paramDouble[dim].add(valor);
		}
		else if (clase.contains("String"))
		{
			for (int i=0; i<ValoresParametros.paramString[dim].size(); i++)
				if (ValoresParametros.paramString[dim].get(i).equals(valor))
					yaContiene=true;
			if (!yaContiene)
				ValoresParametros.paramString[dim].add(valor);
		}
		else if (clase.contains("Character") || clase.contains("char"))
		{
			for (int i=0; i<ValoresParametros.paramChar[dim].size(); i++)
				if (ValoresParametros.paramChar[dim].get(i).equals(valor))
					yaContiene=true;
			if (!yaContiene)
				ValoresParametros.paramChar[dim].add(valor);
		}
		else if (clase.contains("boolean"))
		{
			for (int i=0; i<ValoresParametros.paramBoolean[dim].size(); i++)
				if (ValoresParametros.paramBoolean[dim].get(i).equals(valor))
					yaContiene=true;
			if (!yaContiene)
				ValoresParametros.paramBoolean[dim].add(valor);
		}
	}
	
	
	
	
}
