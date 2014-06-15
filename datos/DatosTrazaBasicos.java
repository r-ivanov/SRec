package datos;


import java.util.ArrayList;

import utilidades.*;

public class DatosTrazaBasicos
{
	String archivo;
	String idTraza;
	String nombreMetodoEjecucion;
	String titulo;
	int[] tecnicas=null;
	
	ArrayList<DatosMetodoBasicos> metodos;
	
	
	public DatosTrazaBasicos(Traza traza)
	{
		this.archivo=traza.getArchivo();
		this.idTraza=traza.getIDTraza();
		this.nombreMetodoEjecucion=traza.getNombreMetodoEjecucion();
		this.titulo=traza.getTitulo();
	
		this.metodos=new ArrayList<DatosMetodoBasicos>(0);
		this.anadirMetodosDTB(traza);
		
	}
	
	
	
	public DatosTrazaBasicos()
	{
		this("","_codigoSinAsignar_","","",null);
	}
	
	public DatosTrazaBasicos(String archivo, String idTraza, String nombreMetodoEjecucion, String titulo, int[] tecnicas)
	{
		this.archivo=archivo;
		this.idTraza=idTraza;
		this.nombreMetodoEjecucion=nombreMetodoEjecucion;
		this.titulo=titulo;
		this.tecnicas=tecnicas;
		this.metodos=new ArrayList<DatosMetodoBasicos>(0);
	}	
	
	
	public void setArchivo(String a)
	{
		this.archivo=a;
	}
	
	public void setId(String id)
	{
		this.idTraza=id;
	}
	
	public void setNombreMetodoEjecucion(String n)
	{
		this.nombreMetodoEjecucion=n;
	}
	
	public void setTitulo(String t)
	{
		this.titulo=t;
	}
	
	public void addMetodo(DatosMetodoBasicos m)
	{
		this.metodos.add(m);
	}
	
	public void addMetodo(ArrayList<DatosMetodoBasicos> ms)
	{
		this.metodos.addAll(ms);
	}	
	

	public String getArchivo()
	{
		return this.archivo;
	}
	
	public String getId()
	{
		return this.idTraza;
	}
	
	public String getNombreMetodoEjecucion()
	{
		return this.nombreMetodoEjecucion;
	}
	
	public String getTitulo()
	{
		return this.archivo;
	}
	

	
	public ArrayList<DatosMetodoBasicos> getMetodos()
	{
		return this.metodos;
	}
	
	public DatosMetodoBasicos getMetodo(int i)
	{
		if (i>=0 && i<this.metodos.size())
			return this.metodos.get(i);
		else
			return null;
	}
	
	public DatosMetodoBasicos getMetodo(String nombre, String[] clasesE, String[] clasesS, int[] dimE, int[] dimS )
	{
		for (int i=0; i<this.metodos.size(); i++)
		{
			DatosMetodoBasicos dmb=this.metodos.get(i);
			
			/*System.out.println("Nombre1="+dmb.getNombre()+" Nombre2="+nombre);
			System.out.println("  NumPE1="+dmb.getNumParametrosE()+" NumPE2="+clasesE.length);
			System.out.println("  NumPS1="+dmb.getNumParametrosS()+" NumPS2="+clasesS.length);*/
			// Si coincide nombre, miramos parámetros...
			if (dmb.getNombre().equals(nombre) && dmb.getNumParametrosE()==clasesE.length && dmb.getNumParametrosS()==clasesS.length)
			{
				boolean sonIguales=true;
				for (int j=0; j<dmb.getNumParametrosE(); j++)
				{
					//System.out.println("  TipoPE1="+dmb.getTipoParametroE(j)+" TipoPE2="+clasesE[j]);
					if (!dmb.getTipoParametroE(j).equals(clasesE[j]))
						sonIguales=false;
				}
				
				if (sonIguales)
				{
					for (int j=0; j<dmb.getNumParametrosS(); j++)
					{
						//System.out.println("  TipoPS1="+dmb.getTipoParametroS(j)+" TipoPS2="+clasesS[j]);
						if (!dmb.getTipoParametroS(j).equals(clasesS[j]))
							sonIguales=false;
					}
				}
				
				if (sonIguales)
					return dmb;
			}
		}
		return null;

	}
	
	
	public int getNumMetodos()
	{
		return this.metodos.size();
	}	
	
	public void anadirMetodosDTB(Traza traza)
	{
		RegistroActivacion ra=traza.getRaiz();
		anadirMetodosDTB(ra);
	}
	
	public void anadirMetodosDTB(RegistroActivacion ra)
	{
		boolean encontradoIgual=false;
	
		String nombreMetodo=ra.getNombreMetodo();
		String []clasesE=ra.clasesParamE();
		String []clasesS=ra.clasesParamS();
		
		for (int i=0; i<this.metodos.size(); i++)
		{
			String tNombreMetodo=this.metodos.get(i).getNombre();
			if (tNombreMetodo.equals(nombreMetodo) && this.metodos.get(i).getNumParametrosE()==clasesE.length)
			{
				int[] dimE=ra.dimParamE();
				int[] tDimE=this.metodos.get(i).getDimE();
				
				boolean encontradoDesigual=false;
				if (dimE.length!=tDimE.length)
					encontradoDesigual=true;
				else
				{
					for (int j=0; j<dimE.length; j++)
					{
						if (!clasesE[j].equals(this.metodos.get(i).getTipoParametroE(j)) || dimE[j]!=tDimE[j])
						{
							encontradoDesigual=true;
						}
					}
				}
				
				if (!encontradoDesigual)
					encontradoIgual=true;
			}
		}
		
		if (!encontradoIgual)	// Si no hemos detectado que ya esté, lo añadimos a los métodos
		{
			DatosMetodoBasicos dmb=new DatosMetodoBasicos(nombreMetodo,clasesE.length,clasesS.length,ra.getDevuelveValor());
			int[] dimE=ra.dimParamE();
			int[] dimS=ra.dimParamS();
			
			String nombres[]=ra.getNombreParametros();
			
			boolean visiblesE[]=ra.getVisibilidadEntrada();
			boolean visiblesS[]=ra.getVisibilidadSalida();
			
			for (int i=0; i<visiblesE.length; i++)
				dmb.addParametroEntrada(nombres[i], ServiciosString.simplificarClase(clasesE[i]), dimE[i], visiblesE[i]);
				
			for (int i=0; i<visiblesS.length; i++)
				dmb.addParametroSalida(nombres[i], ServiciosString.simplificarClase(clasesS[i]), dimS[i], visiblesS[i]);		// Cambiar revisar
				
			if (ra.esRaiz())
				dmb.setEsPrincipal(true);
			dmb.setEsVisible(ra.esMetodoVisible());
			
			
			
			this.addMetodo(dmb);
		}
		
		for (int i=0; i<ra.numHijos(); i++)
			anadirMetodosDTB(ra.getHijo(i));
	}
	
	
	public Traza podar(Traza traza1)
	{
		Traza traza2=traza1.copiar();
		
		traza2.setRaiz(podarDesdeRaiz(traza2.getRaiz()));
		//traza2.verArbol();
		return traza2;
	}
	
	private RegistroActivacion podarDesdeRaiz(RegistroActivacion ra)
	{
		return podar(ra,null,2)[0];
	}
	
	
	private RegistroActivacion[] podar(RegistroActivacion ra, RegistroActivacion padre, int nivel)	// nivel sólo para tabular salida por terminal
	{
		boolean depurar=false;
	
		int totalHijosAdquiridos=0;	
		ra.setPadre(padre);
		
		if (depurar) System.out.println("\n"+ServiciosString.tab(nivel)+"<<<<< ----- podar -----");
		if (depurar) System.out.println("\n"+ServiciosString.tab(nivel)+ra.getNombreMetodo()+"(1)["+ra.getEntradaString()+
				"] ["+ra.getSalidaString()+"]  "+ra.esMetodoVisible());
				
		// Creamos lista nueva de hijos, serán los hijos del nodo una vez acabe el procesamiento
		ArrayList<RegistroActivacion> hijosNuevo=new ArrayList<RegistroActivacion>(0);
		for (int i=0; i<ra.numHijos(); i++)
			hijosNuevo.add(ra.getHijo(i));

		// Procesamos hijos, y vamos generando lista nueva de hijos
		for (int i=0; i<ra.numHijos(); i++)
		{
			RegistroActivacion[] ras;
			
			if (ra.esMetodoVisible())
				ras=podar(ra.getHijo(i),ra,nivel+3);
			else
				ras=podar(ra.getHijo(i),padre,nivel+3);
				
			if (depurar) System.out.println(ServiciosString.tab(nivel)+"ras.length="+ras.length);
			
			if (!ra.getHijo(i).esMetodoVisible())
			{
				hijosNuevo.remove(i+totalHijosAdquiridos);
				
				
				for (int j=ras.length-1; j>=0; j--)
					hijosNuevo.add(i+totalHijosAdquiridos,ras[j]);
				totalHijosAdquiridos--;	// por el remove de un par de líneas atras
				totalHijosAdquiridos=totalHijosAdquiridos+ras.length;
			}
		}
		
		if (depurar) System.out.println("\n"+ServiciosString.tab(nivel)+ra.getNombreMetodo()+"(2)["+ra.getEntradaString()+
				"] ["+ra.getSalidaString()+"]  "+ra.esMetodoVisible());
		if (depurar) System.out.println(ServiciosString.tab(nivel)+"ra.numHijos (1)="+ra.numHijos());
		if (depurar) System.out.println(ServiciosString.tab(nivel)+"hijosNuevo.size="+hijosNuevo.size());
		
		// Guardamos los hijos nuevos de este nodo
		for (int i=ra.numHijos()-1; i>=0; i--)
			ra.delHijo(i);
		for (int i=0; i<hijosNuevo.size(); i++)
			ra.setHijo(hijosNuevo.get(i));
		
		if (depurar) System.out.println(ServiciosString.tab(nivel)+"ra.numHijos (2)="+ra.numHijos());
		
		if (ra.esMetodoVisible())
		{
			RegistroActivacion raTotal[]=new RegistroActivacion[1];
			raTotal[0]=ra;
			if (depurar) System.out.println("\n"+ServiciosString.tab(nivel)+"----- podar ----- (1)>>>>>");
			return raTotal;
		}
		else
		{
			RegistroActivacion raTotal[]=new RegistroActivacion[ra.numHijos()];
			
			//ra.anularEnlaceDeGemelo();
			
			for (int i=0; i<ra.numHijos(); i++)
				raTotal[i]=ra.getHijo(i);
			if (depurar) System.out.println("\n"+ServiciosString.tab(nivel)+"----- podar ----- (2)>>>>>");
			return raTotal;
		}
	}
	
	
	
	public void visualizarDatos()
	{
		System.out.println("\nDatosTrazaBasicos\n-----------------\n");
	
		System.out.println("Path: "+this.archivo);
		System.out.println("Nombre Metodo: "+this.nombreMetodoEjecucion);
		System.out.println("NombreID: "+this.idTraza);
		System.out.println("Titulo: "+this.titulo);

		System.out.println("Métodos: "+this.metodos.size());
		
		for (int i=0; i<this.metodos.size(); i++)
		{
			DatosMetodoBasicos m=this.metodos.get(i);
			System.out.println("\n- Nombre: "+m.getNombre()+ (m.getEsPrincipal() ? "    PPAL" : "") + (m.getEsVisible() ? "   VISIBLE" : ""));
			System.out.print("  Tipo: "+m.getTipo() );
			for (int k=0; k<m.getDimTipo(); k++)
				System.out.print("[]");
			
			System.out.println("  N. parametros: "+m.getNumParametrosE());
			for (int j=0; j<m.getNumParametrosE(); j++)
			{
				System.out.print("    "+m.getNombreParametroE(j)+": "+m.getTipoParametroE(j)+ (m.getVisibilidadE()[j] ? "   > Visible":"   > No visible") );
				for (int k=0; k<m.getDimParametroE(j); k++)
					System.out.print("[]");
				
				System.out.println();	
			}
			System.out.println("  N. retornos: "+m.getNumParametrosS());
			for (int j=0; j<m.getNumParametrosS(); j++)
			{
				System.out.print("    "+m.getNombreParametroS(j)+": "+m.getTipoParametroS(j)+ (m.getVisibilidadS()[j] ? "   > Visible":"   > No visible") );
				for (int k=0; k<m.getDimParametroS(j); k++)
					System.out.print("[]");
				
				System.out.println();	
			}
		}
	}
	
}