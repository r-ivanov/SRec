/**
	Clase que representa cada una de las llamadas al método seleccionado

	@author Luis Fernández y Antonio Pérez Carrasco
	@version 2006-2007

*/
package datos;



import java.io.*;
import java.util.ArrayList;

import paneles.PanelAlgoritmo;

import utilidades.ServiciosString;

import conf.*;

public class RegistroActivacion 
{
	private static boolean depurar=false;
	
	private static final long serialVersionUID = 2L;
	
	private static int nodoID = 0;		// Va contando número de nodos que se crean, se reinicia al generar cada nueva animación
	
	private int nID = -1;		// Número que identifica al nodo, ayuda a creación de traza gemela
		
	private Estado entrada = null;		// Valores de entrada
	private Estado salida = null;		// Valores de salida
	
	private RegistroActivacion padre = null;		// Referencia a su padre y a sus hijos
	//private RegistroActivacion gemelo = null;		// Referencia al nodo gemelo de otra traza
	private ArrayList<RegistroActivacion> hijos = new ArrayList<RegistroActivacion>(0);
	
	
	
	private boolean iluminado = false;				// Iluinado tras buscar llamadas con sus mismos parámetros desde opción de menú
	private boolean resaltado = false;				// Indica si el nodo actual se ha resaltado o no
	
	private boolean metodoVisible = false;			// Determina si los nodos de este árbol aparecerán o no en el árbol
	
	private boolean entradaVisible = false;			// Determina si la entrada es visible actualmente
	private boolean salidaVisible = false;			// Determina si la salida es visible actualmente
	
	private boolean historico = false; 				// true si el nodo es histórico
	private boolean mostradoEntero = false;			// true si siguienteVisible no es aplicable (sólo queda ser histórico en sig. paso)
	
	private boolean soyNodoActual=false;			// Es el último nodo sobre el que se ha aplicado un paso
	private boolean soyCaminoActual=false;			// Forma parte del camino hasta el nodo activo actual
	
	private String nombreMetodo;					// Método al que pertenece este RegistroActivacion
	private String nombreParametros[];				// Nombres de los parámetros de ese método
	private boolean devuelveValor;					// A true si devuelve un valor el método, a false si devuelve void
	private int numMetodo=2;							// Número de método dentro de la animación
	
	private boolean contraido=false;				// Cuando pulsamos sobre él para hacer salto múltiple
	private boolean inhibido=false;					// Cuando sobre algún ancestro se hizo un salto múltiple
	
	/**
		Constructor: crea un nuevo registro de activación
	*/
	public RegistroActivacion ()
	{
	}

	/**
		Constructor: crea un nuevo registro de activación con el estado de entrada que se le pasa
		
		@param entrada estado de entrada
	*/
	public RegistroActivacion (Estado entrada,String nombreMetodo,String nombreParametros[])
	{
		this(null, entrada, nombreMetodo, nombreParametros);
	}
	
	/**
		Constructor: crea un nuevo registro de activación con estados de entrada y salida
		
		@param entrada estado de entrada
		@param salida estado de salida
	*/
	private RegistroActivacion (RegistroActivacion padre, Estado entrada,
							String nombreMetodo,String nombreParametros[])
	{
		this.padre = padre;
		this.entrada = entrada;	
		
		this.nombreMetodo=nombreMetodo;
		this.nombreParametros=nombreParametros;

		this.nID=RegistroActivacion.nodoID;
		RegistroActivacion.nodoID++;
	}
	
	static public  void reinicializar()
	{
		nodoID=0;
	}
	
	
	public int getNivel()
	{
		int nivel=1;
		RegistroActivacion ra_aux=this;
		
		while (ra_aux.padre!=null)
		{
			nivel++;
			ra_aux=ra_aux.padre;
		}
		return nivel;
	}
	
	
	// Metodos para asignacion de valores
	public void setVisibilidad (boolean entrada, boolean salida, boolean mostradoEntero)
	{
		this.entradaVisible=entrada;
		this.salidaVisible=salida;
		this.mostradoEntero=mostradoEntero;
	}
	
	public void setInformacionHijos(boolean historico, boolean mostradoEntero)
	{
		this.historico=historico;
		this.mostradoEntero=mostradoEntero;
	}
	
	public void setVisibilidad(ClaseAlgoritmo claseAlgoritmo)
	{
		for (int i=0; i<claseAlgoritmo.getNumMetodos(); i++)
		{
			MetodoAlgoritmo m=claseAlgoritmo.getMetodo(i);
			
			// Si tienen mismo nombre y mismo número de parámetros ...
			if (m.getNombre().equals(this.nombreMetodo) && m.getNumeroParametros()==entrada.getClases().length)
			{
				// ... comprobamos los tipos de estos, si coinciden es el método que buscamos
				String mTipos[]=m.getTiposParametros();
				String tTipos[]=this.entrada.getClases();
				int mDim[]=m.getDimParametros();
				int tDim[]=this.entrada.getDimensiones();
				
				boolean sonIguales=true;
				for (int j=0; j<mTipos.length; j++)
					if (mDim[j]!=tDim[j] || !mTipos[j].equals(tTipos[j]))
						sonIguales=false;
				
				if (sonIguales)
				{
					this.metodoVisible=m.getMarcadoVisualizar();
					this.setVisibilidad(m.getVisibilidadEntrada(),m.getVisibilidadSalida());
					this.entrada.setRepresentacionLineasTraza(m.getVisibilidadEntrada(),this.nombreParametros);
					
				}
			}
		}
		
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).setVisibilidad(claseAlgoritmo);
	}
	
	public void setVisibilidad(DatosTrazaBasicos dtb)
	{
		for (int i=0; i<dtb.getNumMetodos(); i++)
		{
			DatosMetodoBasicos dmb=dtb.getMetodo(i);
			
			// Si tienen mismo nombre y mismo número de parámetros ...
			if (dmb.getNombre().equals(this.nombreMetodo) && dmb.getNumParametrosE()==entrada.getClases().length)
			{
				// ... comprobamos los tipos de estos, si coinciden es el método que buscamos
				String mTipos[]=dmb.getTipoParametrosE();
				String tTipos[]=this.entrada.getClases();
				int mDim[]=dmb.getDimE();
				int tDim[]=this.entrada.getDimensiones();
				
				boolean sonIguales=true;
				for (int j=0; j<mTipos.length; j++)
					if (mDim[j]!=tDim[j] || !mTipos[j].equals(tTipos[j]))
						sonIguales=false;
				
				if (sonIguales)
				{
					this.metodoVisible=dmb.getEsVisible();
					this.setVisibilidad(dmb.getVisibilidadE(),dmb.getVisibilidadS());
					this.entrada.setRepresentacionLineasTraza(dmb.getVisibilidadE(),this.nombreParametros);
					
				}
			}
		}
		
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).setVisibilidad(dtb);
	}
	
	public void setVisibilidad(boolean paramE[], boolean paramS[])
	{
		this.entrada.setRepresentacion(paramE);
		this.salida.setRepresentacion(paramS);
		this.entrada.setRepresentacionLineasTraza(paramE,this.nombreParametros);
	}
	
	
	
	public void setEsMetodoVisible(boolean valor)
	{
		this.metodoVisible=valor;
	}
	
	
	public boolean esMetodoVisible()
	{
		return this.metodoVisible;
	}
	
	
	
	public boolean getDevuelveValor()
	{
		return this.devuelveValor;
	}
	
	
	public void setDevuelveValor(boolean devuelveValor)
	{
		this.devuelveValor=devuelveValor;
	}
	
	
	
	public int getID()
	{
		return this.nID;
	}

	public void setID(int id)
	{
		this.nID=id;
	}	
	
	
	public void asignarNumeroMetodo(String[] interfaces)
	{
		for (int i=0; i<interfaces.length; i++)
		{
			if (interfaces[i].equals(this.interfazMetodo()))
				this.numMetodo=i;
		}
		for (int i=0; i<this.numHijos(); i++)
			this.hijos.get(i).asignarNumeroMetodo(interfaces);
	}
	
	
	public int getNumMetodo()
	{
		return this.numMetodo;
	}
	
	public String[] clasesParamE()
	{
		return this.entrada.getClases();
	}
	
	public String[] clasesParamS()
	{
		return this.salida.getClases();
	}
	
	public int[] dimParamE()
	{
		return this.entrada.getDimensiones();
	}
	
	public int[] dimParamS()
	{
		return this.salida.getDimensiones();
	}	
	
	public boolean[] getVisibilidadEntrada()
	{
		return this.entrada.getVisibilidad();
	}
	
	public boolean[] getVisibilidadSalida()
	{
		return this.salida.getVisibilidad();
	}
	
	/**
		Retorna el estado de entrada
		
		@return estado de entrada
	*/
	public Estado getEntrada ()
	{
		return entrada;
	}
	
	/**
		Retorna el estado de salida
		
		@return estado de salida
	*/
	public Estado getSalida ()
	{
		return salida;
	}	
	
	
	public String[] getLineasTraza(int margen)
	{
		
		if (this.entradaVisible || this.salidaVisible || this.esHistorico())
		{
			String [] lineas=new String[0];
			
			// Si tenemos entrada visible, añadimos su línea
			if (  (this.entradaVisible || (this.esHistorico() && Conf.historia==1)) && 
				(Conf.VISUALIZAR_ENTRADA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))
			{	
				lineas=new String[1];
				
				lineas[0]="";
				for (int i=0; i<margen; i++)
					lineas[0]=lineas[0]+" ";
						
				if(Conf.idMetodoTraza) {
					String metodo = this.getNombreMetodo();
					if (PanelAlgoritmo.nyp!=null)
						metodo = PanelAlgoritmo.nyp.getPrefijo(this.getNombreMetodo());
					lineas[0]=lineas[0]+"entra "+metodo+": "+this.entrada.getRepresentacionLineasTraza();
					//lineas[0]=lineas[0]+"entra "+this.getNombreMetodo()+": "+this.entrada.getRepresentacionLineasTraza();
				}
				else
					lineas[0]=lineas[0]+"entra: "+this.entrada.getRepresentacionLineasTraza();
				
				if (this.esHistorico())
					lineas[0]=lineas[0]+" <hist>";
				
				if (this.iluminado())
					lineas[0]=lineas[0]+" <ilum>";
				
				lineas[0]=lineas[0]+"?"+this.getNumMetodo()+"?";
			}
			
			if (!this.contraido || (this.contraido && Conf.mostrarArbolSalto))
			{
				// Añadimos líneas de hijos, si los tiene y si no está contraido
				for (int i=0; i<hijos.size(); i++)
				{
					if (hijos.get(i)!=null)		// NO DEBERÍA SER NECESARIA ESTA COMPROBACIÓN
					{
						String[] lineasHijo;
						if(Conf.sangrado)
							lineasHijo=hijos.get(i).getLineasTraza(margen+4);
						else
							lineasHijo=hijos.get(i).getLineasTraza(margen);
						
						String [] lhAux=new String[lineas.length+lineasHijo.length];
						for (int j=0; j<lineas.length; j++)
							lhAux[j]=lineas[j];
						for (int j=lineas.length; j<lhAux.length; j++)
							lhAux[j]=lineasHijo[j-lineas.length];
						lineas=lhAux;
					}
				}
			}
			
			// Si tenemos salida visible, añadimos su línea			
			if ( (this.salidaVisible || (this.esHistorico() && Conf.historia==1)) &&
				(Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))
			{	
				String lineaSalida="";

				for (int i=0; i<margen; i++)
					lineaSalida=lineaSalida+" ";
				
				if (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar && !this.esHistorico() && !this.esMostradoEntero())
				{
					if(Conf.idMetodoTraza) {
						String metodo = this.getNombreMetodo();
						if (PanelAlgoritmo.nyp!=null)
							metodo = PanelAlgoritmo.nyp.getPrefijo(this.getNombreMetodo());
						lineaSalida=lineaSalida+"sale "+metodo+": return ?";
						//lineaSalida=lineaSalida+"sale "+this.getNombreMetodo()+": return ?";//ValoresStrings()[0];
					}
					else
						lineaSalida=lineaSalida+"sale: return ?";//ValoresStrings()[0];
				}
				else
				{
					if(Conf.idMetodoTraza) {
						String metodo = this.getNombreMetodo();
						if (PanelAlgoritmo.nyp!=null)
							metodo = PanelAlgoritmo.nyp.getPrefijo(this.getNombreMetodo());
						lineaSalida=lineaSalida+"sale "+metodo+": return "+this.salida.getRepresentacion();
						//lineaSalida=lineaSalida+"sale "+this.getNombreMetodo()+": return "+this.salida.getRepresentacion();//ValoresStrings()[0];
					}
					else
						lineaSalida=lineaSalida+"sale: return "+this.salida.getRepresentacion();//ValoresStrings()[0];
				}
				
				
				if (this.esHistorico())
					lineaSalida=lineaSalida+" <hist>";
				
				if (this.iluminado())
					lineaSalida=lineaSalida+" <ilum>";
				
				lineaSalida=lineaSalida+"?"+this.getNumMetodo()+"?";
				
				String [] lhAux=new String[lineas.length+1];
				for (int j=0; j<lineas.length; j++)
					lhAux[j]=lineas[j];
				lhAux[lineas.length]=lineaSalida;
				lineas=lhAux;
			}
			return lineas;
		}
		else
		{
			return new String[0];
		}
	}
	
	
	public String[] getLineasTrazaSalidaLigadaEntrada(int margen)
	{
		if (this.entradaVisible || this.salidaVisible || this.esHistorico())
		{
			String [] lineas=new String[0];
			
			// Si tenemos entrada visible, añadimos su línea
			if (  (this.entradaVisible || (this.esHistorico() && Conf.historia==1)) && 
				(Conf.VISUALIZAR_ENTRADA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))
			{	
				lineas=new String[1];
				
				lineas[0]="";
				for (int i=0; i<margen; i++)
					lineas[0]=lineas[0]+" ";
						
				if(Conf.idMetodoTraza) {
					String metodo = this.getNombreMetodo();
					if (PanelAlgoritmo.nyp!=null)
						metodo = PanelAlgoritmo.nyp.getPrefijo(this.getNombreMetodo());
					lineas[0]=lineas[0]+"entra "+metodo+": "+this.entrada.getRepresentacionLineasTraza();
					//lineas[0]=lineas[0]+"entra "+this.getNombreMetodo()+": "+this.entrada.getRepresentacionLineasTraza();
				}
				else
					lineas[0]=lineas[0]+"entra: "+this.entrada.getRepresentacionLineasTraza();
				
				if (this.esHistorico())
					lineas[0]=lineas[0]+" <hist>";
				
				if (this.iluminado())
					lineas[0]=lineas[0]+" <ilum>";
				
				lineas[0]=lineas[0]+"?"+this.getNumMetodo()+"?";
			}
			
			// Si tenemos salida visible, añadimos su línea			
			if ( (this.salidaVisible || (this.esHistorico() && Conf.historia==1)) &&
				(Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))
			{	
				String lineaSalida="";

				for (int i=0; i<margen; i++)
					lineaSalida=lineaSalida+" ";
				
				if (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar && !this.esHistorico() && !this.esMostradoEntero())
				{
					if(Conf.idMetodoTraza) {
						String metodo = this.getNombreMetodo();
						if (PanelAlgoritmo.nyp!=null)
							metodo = PanelAlgoritmo.nyp.getPrefijo(this.getNombreMetodo());
						lineaSalida=lineaSalida+"sale "+metodo+": return ?";
						//lineaSalida=lineaSalida+"sale "+this.getNombreMetodo()+": return ?";//ValoresStrings()[0];
					}
					else
						lineaSalida=lineaSalida+"sale: return ?";//ValoresStrings()[0];
				}
				else
				{
					if(Conf.idMetodoTraza) {
						String metodo = this.getNombreMetodo();
						if (PanelAlgoritmo.nyp!=null)
							metodo = PanelAlgoritmo.nyp.getPrefijo(this.getNombreMetodo());
						lineaSalida=lineaSalida+"sale "+metodo+": return "+this.salida.getRepresentacion();
						//lineaSalida=lineaSalida+"sale "+this.getNombreMetodo()+": return "+this.salida.getRepresentacion();//ValoresStrings()[0];
					}
					else
						lineaSalida=lineaSalida+"sale: return "+this.salida.getRepresentacion();//ValoresStrings()[0];
				}
				
				
				if (this.esHistorico())
					lineaSalida=lineaSalida+" <hist>";
				
				if (this.iluminado())
					lineaSalida=lineaSalida+" <ilum>";
				
				lineaSalida=lineaSalida+"?"+this.getNumMetodo()+"?";
				
				String [] lhAux=new String[lineas.length+1];
				for (int j=0; j<lineas.length; j++)
					lhAux[j]=lineas[j];
				lhAux[lineas.length]=lineaSalida;
				lineas=lhAux;
			}
			
			if (!this.contraido || (this.contraido && Conf.mostrarArbolSalto))
			{
				// Añadimos líneas de hijos, si los tiene y si no está contraido
				for (int i=0; i<hijos.size(); i++)
				{
					if (hijos.get(i)!=null)		// NO DEBERÍA SER NECESARIA ESTA COMPROBACIÓN
					{
						String[] lineasHijo;
						if(Conf.sangrado)
							lineasHijo=hijos.get(i).getLineasTrazaSalidaLigadaEntrada(margen+4);
						else
							lineasHijo=hijos.get(i).getLineasTrazaSalidaLigadaEntrada(margen);
						
						String [] lhAux=new String[lineas.length+lineasHijo.length];
						for (int j=0; j<lineas.length; j++)
							lhAux[j]=lineas[j];
						for (int j=lineas.length; j<lhAux.length; j++)
							lhAux[j]=lineasHijo[j-lineas.length];
						lineas=lhAux;
					}
				}
			}
			return lineas;
		}
		else
		{
			return new String[0];
		}
	}
	
	
	public int getMaxAlturaEstructura()
	{
		int altura=0;
		
		Estructura e = new Estructura(this.entrada.getEstructura());
		
		// Calculo la mía...
		if (e==null || e.getTipo()==null)
			altura=0;
		else if (e.esArray())
			altura=1;
		else if (e.esMatriz())
			altura=e.dimensiones()[1];
		
		// Después la de mis hijos...
		for (int i=0; i<this.numHijos(); i++)
			altura= Math.max( altura, this.getHijo(i).getMaxAlturaEstructura());
		
		return altura;
	}
	
	
	public boolean esFinalizado()
	{
		return this.esHistorico();
	}
	
	public int getMaxAltura()
	{
		int alturasHijos=0;
		for (int i=0; i<hijos.size(); i++)
			if (hijos.get(i)!=null)					// NO DEBERÍA SER NECESARIA ESTA COMPROBACIÓN
				if (hijos.get(i).getMaxAltura()>alturasHijos)
					alturasHijos=hijos.get(i).getMaxAltura();
		return (1+alturasHijos);
	}
	
	public String getEntradaString()
	{
		return this.entrada.getRepresentacion();
	}
	
	public String getEntradaCompletaString()
	{
		return this.entrada.getRepresentacionCompleta(this.nombreParametros);
	}
	
	public String[] getEntradasString()
	{
		return this.entrada.getValoresStrings();
	}
	
	
	public String getSalidaString()
	{
		return this.salida.getRepresentacion();
	}
	
	public String getSalidaCompletaString()
	{
		if (this.devuelveValor)
			return this.salida.getRepresentacionCompleta(null);
		else
			return this.salida.getRepresentacionCompleta(this.nombreParametros);
	}
	
	public String[] getSalidasString()
	{
		return this.salida.getValoresStrings();
	}
	
	public String getNombreMetodo()
	{
		return this.nombreMetodo;
	}
	
	public String[] getNombreParametros()
	{
		return this.nombreParametros;
	}
	
	public void setNombreMetodo(String nombreMetodo)
	{
		this.nombreMetodo=nombreMetodo;
	}
	
	public void setNombreParametros(String nombreParametros[])
	{
		this.nombreParametros=nombreParametros;
	}
	
	public void setEntradaString(String es)
	{
		if (this.entrada==null)
			this.entrada=new Estado();
		this.entrada.nuevoValorParametro(es);
	}
	
	public void setSalidaString(String ss)
	{
		if (this.salida==null)
			this.salida=new Estado();
		this.salida.nuevoValorParametro(ss);
	}
	
	public void setEntradaClase(String es)
	{
		this.entrada.setClase(es);
	}
	
	public void setSalidaClase(String ss)
	{
		this.salida.setClase(ss);
	}
	
	public void setEntradaDim(int es)
	{
		this.entrada.setDim(es);
	}
	
	public void setSalidaDim(int ss)
	{
		this.salida.setDim(ss);
	}
	
	
	
	
	
	/**
		Retorna el número de hijos que tiene
		
		@return número de hijos que tiene
	*/
	public int numHijos()
	{
		return this.hijos.size();
	}
	
	/**
		Retorna el número de hijos y subhijos que tiene
	
		@return número de hijos que tiene
	*/
	public int numHijosRec()
	{
		int numSubHijos=this.numHijos();
		for (int i=0; i<this.numHijos(); i++)
			numSubHijos=numSubHijos+this.getHijo(i).numHijosRec();
		
		return numSubHijos;
	}
	
	/**
		Retorna el registro padre
		
		@return registro padre
	*/
	public RegistroActivacion getPadre ()
	{
		return this.padre;
	}
	
	/**
		Asigna el padre
		
		@param p registro padre
	*/
	public void setPadre (RegistroActivacion p)
	{
		this.padre=p;
	}
	
	public boolean esMostradoEntero()
	{
		return this.mostradoEntero;
	}
	
	public void setMostradoEntero(boolean valor)
	{
		this.mostradoEntero=valor;
	}
	
	/**
		Revisa si se puede aplicar un paso múltiple sobre un nodo
		
		@return true si no se puede (es decir, true si el nodo actual ya se ve al completo)
	*/
	public boolean actualyCompleto()
	{
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO)
			return this.entradaVisible && this.salidaVisible;
		
		else if(Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA)
		{
			if (this.hijos.size()>0)
				return this.hijos.get(hijos.size()-1).enteroVisible(false, false);
			else
				return this.entradaVisible;
		}
		else if(Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA)
		{
			if (this.hijos.size()>0)
				return this.hijos.get(hijos.size()-1).enteroVisible(false, false);
			else
				return this.salidaVisible;	
		}
			
		return false;
	}
	
	
	
	/**
		Asigna un hijo. Este método se utiliza sólo para inicialización desde fichero
		
		@param h registro de activación hijo
	*/
	public void setHijo(RegistroActivacion h)
	{
		this.hijos.add(h);
	}
	
	public void setHijo(int i,RegistroActivacion h)
	{
		this.hijos.add(i,h);
	}
	
	public void delHijo(int i)
	{
		this.hijos.remove(i);
	}
	
	

	/**
		Devuelve el valor del atributo historico
		
		@return true si es histórico, false si no lo es
	*/
	public boolean esHistorico()
	{
		return this.historico;
	}
	
	
	/**
		Asigna el valor dado al atributo historico
		
		@param 0 = No es histórico / 1 = Es histórico
	*/
	public void setHistorico(boolean h)
	{
		this.historico=h;
		if (h)
			for (int i=0; i<hijos.size(); i++)
				hijos.get(i).setHistorico(h);
	}
	
	
	public void setHistoricoHijos(boolean h)
	{
		for (int i=0; i<hijos.size(); i++)
		{
			hijos.get(i).setHistorico(h);
			hijos.get(i).setHistoricoHijos(h);
		}
	}


	
	
	public boolean esRaiz()
	{
		return this.padre==null;
	}
	
	
	
	/**
		Calcula si este nodo es histórico revisando sus parientes
	*/
	public boolean tieneParienteHistorico()
	{
		if (this.padre==null)		// Si es raiz...
			return false;					// la raiz nunca es histórico
		
		else
		{
			RegistroActivacion ra=this.padre;
			
			do
			{
				if (ra.esHistorico())
				{
					this.setHistorico(true);
					return true;
				}
				ra=ra.getPadre();
			}
			while (ra!=null && !this.esHistorico());
		}
		return false;
	}
	
	
	/**
		Añade un estado de entrada
		
		@param entrada estado de entrada que se añade
	*/
	public RegistroActivacion anadirEntrada (Estado entrada,String nombreMetodo,String nombreParametros[])
	{
		/*hijos[hijos.size()] = new RegistroActivacion(this, entrada, this.getTraza(), nombreMetodo,nombreParametros);
		hijos.size()++;
		return hijos[hijos.size()-1];*/
		
		hijos.add( new RegistroActivacion(this, entrada, nombreMetodo,nombreParametros));
		return hijos.get(hijos.size()-1);
	}
	
	/**
		Añade un estado de salida
		
		@param padre estado de salida que se añade
	*/
	public RegistroActivacion anadirSalida (Estado salida)
	{
		this.salida = salida;
		return padre;
	}
	
	
	
	
	protected RegistroActivacion copiar(RegistroActivacion padreNuevo)
	{
		RegistroActivacion ra=new RegistroActivacion();
		
		ra.entrada=this.getEntrada().copiar();
		ra.salida=this.getSalida().copiar();
		
		//ra.gemelo=this.gemelo;
		
		ra.padre=padreNuevo;
		ra.nID=this.nID;
		
		for (int i=0; i<this.numHijos(); i++)
			ra.hijos.add(this.getHijo(i).copiar(ra));
		
		ra.metodoVisible=this.metodoVisible;
		ra.entradaVisible=this.entradaVisible;
		ra.salidaVisible=this.salidaVisible;
		
		ra.historico=this.historico;
		ra.mostradoEntero=this.mostradoEntero;
		
		ra.soyNodoActual=this.soyNodoActual;
		ra.soyCaminoActual=this.soyCaminoActual;
		
		ra.iluminar(this.iluminado);
		
		
		ra.numMetodo=this.numMetodo;
		ra.nombreMetodo=new String(this.nombreMetodo);
		ra.nombreParametros=new String[this.nombreParametros.length];
		for (int i=0; i<this.nombreParametros.length; i++)
			ra.nombreParametros[i]=new String(this.nombreParametros[i]);
			
		ra.devuelveValor=this.devuelveValor;
		
		ra.contraido=this.contraido;
		ra.inhibido=this.inhibido;
		
		return ra;
	}
	
	
	
	
	
	private void asignacion(boolean nodoActual, boolean caminoActual, boolean mostradoEntero, boolean historico, boolean eV, boolean sV)
	{
		this.setEsNodoActual(nodoActual);		
		this.setEsCaminoActual(caminoActual);
		this.mostradoEntero=mostradoEntero;
		this.historico=historico;
		
		this.entradaVisible=eV;
		this.salidaVisible=sV;
		//System.out.println("asignacion: "+eval(entradaVisible)+"/"+eval(salidaVisible)+" "+
		//		this.getEntradaString()+"/"+this.getSalidaString());
	}
	
	private void asignacionR(boolean mostradoEntero, boolean historico, boolean eV, boolean sV)
	{
		this.setEsNodoActual(false);		
		this.setEsCaminoActual(false);
		this.mostradoEntero=mostradoEntero;
		this.historico=historico;
		
		this.entradaVisible=eV;
		this.salidaVisible=sV;
		
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).asignacionR(mostradoEntero,historico,eV,sV);		
	}	
	
	
	/**
		Aplicado sobre un nodo, hace que sus hijos estén completamente finalizados y que él se muestre completo
		(pero sin estar finalizado; es decir, se muestra entero, es el nodo actual pero no es histórico (aún))
	*/
	public void todoVisible()
	{
		asignacion(true,false,true,false,true,true);
		
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).finalizar();
	}
	
	
	/**
		Deja un nodo finalizado, así como a todos sus hijos
	*/
	private void finalizar()
	{
		asignacion(false,false,true,true,true,true);
		
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).finalizar();
	}
	
	/**
		Hace que ningún hijo sea visible
	*/
	public void nadaVisible (boolean esRaiz)
	{
		asignacion(false,false,false,false,false,false);

		if (this.contraido)
			for (int i=0; i<hijos.size(); i++)
				hijos.get(i).desinhibir();
		
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).iniciar();
	}
	
	/**
		Hace que ningún hijo sea visible
	*/
	public void iniciar ()
	{
		asignacion(false,false,false,false,false,false);
		
		//if (this.contraido)
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).desinhibir();
		
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).descontraer();
		
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).iniciar();
	}	
	

	/**
		Devuelve el número de hijos visibles
		
		@return número de hijos visibles
	*/
	public int getHijosVisibles ()
	{
		int numHijosVisibles=0;
		for (int i=0; i<hijos.size(); i++)
			if (!hijos.get(i).vacioVisible())
				numHijosVisibles++;
		return numHijosVisibles;
	}
	

	/**
		Devuelve el número de hijos visibles
		
		@return número de hijos visibles
	*/
	public int getHijosVisiblesPantalla ()
	{
		if (this.contraido && !Conf.mostrarArbolSalto)
			return 0;
		
		
		int numHijosVisibles=0;
		if (Conf.historia!=2)		// Si hay que mostrar históricos (coloreados o atenuados)
		{
			for (int i=0; i<hijos.size(); i++)
				if (!hijos.get(i).vacioVisible())
					numHijosVisibles++;
		}
		else 						// Si no hay que mostrar históricos
		{
			for (int i=0; i<hijos.size(); i++)
				if (!hijos.get(i).esHistorico() && !hijos.get(i).vacioVisible())
					numHijosVisibles=1;
		}
			
		
		
		return numHijosVisibles;
	}



	/**
		Devuelve el hijo i-ésimo
		
		@param i número de hijo que deseamos recuperar
		@return hijo i-ésimo
	*/
	public RegistroActivacion getHijo (int i)
	{
		if (i<hijos.size())
			return hijos.get(i);
		else
			return null;
	}


	
	
	public boolean getEsNodoActual()
	{
		return this.soyNodoActual;
	}
	
	
	public void setEsNodoActual(boolean valor)
	{
		this.soyNodoActual=valor;
	}
	
	public void setEsNodoActualRec(boolean valor)
	{
		this.soyNodoActual=valor;
		for (int i=0; i<hijos.size(); i++)
			this.hijos.get(i).setEsNodoActualRec(valor);
	}
	
	public void setEsCaminoActualRec(boolean valor)
	{
		this.soyCaminoActual=valor;
		for (int i=0; i<hijos.size(); i++)
			this.hijos.get(i).setEsCaminoActualRec(valor);
	}
	
	public void setEsNodoActualHijo(boolean valor, int i)
	{
		this.soyNodoActual=false;
		if (i>-1 && i<hijos.size())
			this.hijos.get(i).setEsNodoActual(valor);
	}
	
	public void setEsNodoActualUltimoHijoRec(boolean valor)
	{
		if (hijos.size()>0)
			this.hijos.get(hijos.size()-1).setEsNodoActualUltimoHijoRec(valor);
		else
			this.setEsNodoActual(valor);
	}
	
	public void contraer()
	{
		this.contraido=true;
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).inhibir();
	}
	
	public void inhibir()
	{
		this.inhibido=true;
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).inhibir();
	}
	
	public void descontraer()
	{
		this.contraido=false;
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).desinhibir();
	}
	
	public void desinhibir()
	{
		this.inhibido=false;
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).desinhibir();
	}	
	
	
	
	
	/**
		Hace que el siguiente hijo sea visible
	*/
	public void siguienteVisible(boolean esRaiz)
	{
		
		// *****
		// Mostramos en la visualizacion valores de entrada Y salida
		// *****
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO)
		{
			
			if (depurar) System.out.print("\nConf.VISUALIZAR_TODO siguienteVisible ");
			// Si esta en fase de inicio
			if (!this.entradaVisible)	
			{
				if (depurar) System.out.println("<1>");
				this.asignacion(true,false,false,false,true,false);
				this.setContraido(false);
			}
			// Si ya hemos mostrado todos los hijos o simplemente no tenemos
			else if (hijos.size()==0 || hijos.get(hijos.size()-1).esMostradoEntero())
			{
				if (depurar) System.out.println("<2>");
				for (int i=0; i<hijos.size(); i++)
					hijos.get(i).finalizar();
				this.asignacion(true,false,true,false,true,true);
			}
			// Si tenemos hijos y aún no están mostrados del todo
			else 
			{
				if (depurar) System.out.println("<3>");
				int i=0;
				while (hijos.get(i).esMostradoEntero())
				{
					i++;
				}
				if (i>0)
					hijos.get(i-1).finalizar();
				hijos.get(i).siguienteVisible(false);
				this.asignacion(false,true,false,false,true,false);
			}
		}
		// *****
		// Mostramos en la visualizacion valores de entrada O salida
		// *****
		else //if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA)
		{
			if (depurar) System.out.print("\nConf.VISUALIZAR_ENTRADA siguienteVisible ");
			// Si esta en fase de inicio con Conf.VISUALIZAR_ENTRADA
			if ((!this.entradaVisible && Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA ) 
				 )
			{
				if (depurar) System.out.println("<1>");
				this.asignacion(true,false,hijos.size()==0,false,
					true,hijos.size()==0);//Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
				this.setContraido(false);
			}
			else if(	(!this.salidaVisible && Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA ) )
			{
				if (depurar) System.out.println("<1b>");
				this.asignacion(true,false,hijos.size()==0,false,
					false,true);//Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
			}			
		
			
			
			
			
			
			// Si nuestros hijos están ya totalmente visualizados
			else if (hijos.size()>0 && hijos.get(hijos.size()-1).esMostradoEntero() && !hijos.get(hijos.size()-1).esHistorico())
			{
				if (depurar) System.out.println("<2>");
				hijos.get(hijos.size()-1).finalizar();
				this.asignacion(true,false,true,false,
					/*true,true);*/Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
			}
			// Si tenemos hijos y aún no están mostrados del todo
			else if (hijos.size()>0)
			{
				if (depurar) System.out.println("<3>");
				int i=0;
				while (hijos.get(i).esMostradoEntero())
					i++;
					
				if (i>0)
					hijos.get(i-1).finalizar();
				if (i<hijos.size())
				{
					hijos.get(i).siguienteVisible(false);
					this.asignacion(false,true,false,false,
						Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
				}
			}
			else
			{
				if (depurar) System.out.println("<4>");
			}
		}
	}

	
	/**
		Hace que el último hijo que es visible deje de serlo
	*/
	public void anteriorVisible()
	{
		// *****
		// Mostramos en la visualizacion valores de entrada Y salida
		// *****
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO)
		{
			if (depurar) System.out.print("Conf.VISUALIZAR_EyS anteriorVisible ");
			
			// Si esta en fase de inicio
			if (this.salidaVisible)	
			{
				if (depurar) System.out.println("<1>");
				this.asignacion(false,true,false,false,true,false);
				if (hijos.size()>0 && (!this.contraido || (this.contraido && Conf.mostrarArbolSalto) ))
				{
					if (depurar) System.out.println("<1>1");
					this.hijos.get(hijos.size()-1).asignacion(true,false,true,false,true,true);
				}
				else
				{
					if (depurar) System.out.println("<1>2");
					for (int i=0; i<hijos.size(); i++)
					{
						hijos.get(i).nadaVisible(false);
						hijos.get(i).asignacion(false,false,false,false,false,false);	
					}
					this.asignacion(true,false,false,false,true,false);	
				}
				if (this.contraido)
					this.descontraer();
			}
			// Si no tenemos hijos (mostrandose)
			else if (hijos.size()==0 || hijos.get(0).vacioVisible())
			{
				if (depurar) System.out.println("<2>");
				for (int i=0; i<hijos.size(); i++)
					hijos.get(i).iniciar();
				this.asignacion(false,false,false,false,false,false);
				if (this.padre!=null)
					this.padre.setEsNodoActual(true);
			}
			// Si tenemos hijos y aún no están mostrados del todo
			else 
			{
				if (depurar) System.out.println("<3>");
				int i=0;
				while (i<hijos.size() && !hijos.get(i).vacioVisible())
					i++;
				i--;

				if (i>-1)
					hijos.get(i).anteriorVisible();
				if (hijos.get(i).vacioVisible())
				{
					if (i>0)
					{
						hijos.get(i-1).asignacion(true,false,true,false,true,true);
						this.asignacion(false,true,false,false,true,false);
					}
					else
						this.asignacion(true,false,false,false,true,false);
				}

			}
		}
		// *****
		// Mostramos en la visualizacion valores de entrada O salida
		// *****
		else 
		{
			if (depurar) System.out.print("Conf.VISUALIZAR_EoS anteriorVisible ");
			// Si este nodo es historico
			if (this.esHistorico())
			{
				if (depurar) System.out.println("<1>");
				this.asignacion(true,false,true,false,
					Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
			}
			// Si este nodo, con hijos, está en estado mostradoEntero (es nodo actual)
			else if (hijos.size()>0 && hijos.get(hijos.size()-1).esHistorico())
			{
				if (depurar) System.out.println("<2>");
				if (!this.contraido || (this.contraido && Conf.mostrarArbolSalto) )
				{
					hijos.get(hijos.size()-1).anteriorVisible();
					this.asignacion(false,true,false,false,
						Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
				}
				else
				{
					for (int i=0; i<hijos.size(); i++)
						hijos.get(i).nadaVisible(false);
					this.asignacion(true,false,false,false,
						Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
				}
				if (this.contraido())
					this.descontraer();
			}
			// Si este nodo tiene el conjunto de hijos a medio visualizar
			else if (hijos.size()>0 && !hijos.get(0).vacioVisible())
			{
				if (depurar) System.out.println("<3>");
				int x=hijos.size()-1;
				while (hijos.get(x).vacioVisible())
					x--;
				if (x>=0 && hijos.size()>0)
				{
					if (depurar) System.out.println("<3>1");
					hijos.get(x).anteriorVisible();
					if (hijos.get(x).vacioVisible())
					{
						if (depurar) System.out.println("<3>2");
						hijos.get(x).asignacion(false,false,false,false,false,false);
						if (x==0)	
						{
							if (depurar) System.out.println("<3>2");
							this.asignacion(true,false,false,false,
								Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
						}
						else
						{
							if (depurar) System.out.println("<3>3");
							hijos.get(x-1).asignacion(true,false,true,false,
								Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
							this.asignacion(false,true,false,false,
								Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
						}
					}
				}
				else
				{
					if (depurar) System.out.println("<3>4");
					if (x>=0)
					{
						if (depurar) System.out.println("<3>5 x="+x);
						hijos.get(x).iniciar();
						if (x==0)
						{
							if (depurar) System.out.println("<3>6");
							this.asignacion(true,false,false,false,
								Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
						}
						else
						{
							if (depurar) System.out.println("<3>7");
							this.hijos.get(x-1).asignacion(true,false,true,false,
								Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
						}
					}
					else	// Este nodo tiene hijos pero todos estan inicializados, él debe ser borrado también
					{
						if (depurar) System.out.println("<3>8");
						this.iniciar();
					}
				}
			}
			else
			{
				if (depurar) System.out.println("<4>");
				this.iniciar();
			}
		}
	}

	
	// Solo coloca el valor al atributo (para carga de traza desde XML)
	public void setContraido(boolean b)
	{
		this.contraido=b;
	}

	// Solo coloca el valor al atributo (para carga de traza desde XML)
	public void setInhibido(boolean b)
	{
		this.inhibido=b;
	}


	
	public boolean inhibido()
	{
		return this.inhibido;
	}	
	
	public boolean contraido()
	{
		return this.contraido;
	}

	public boolean iluminado()
	{
		return this.iluminado;
	}	
	
	
	public boolean esDYV()
	{
		return (!(this.entrada.getEstructura()==null ||
				this.entrada.getIndices()==null));
	}

	
	
	
	// Mira que no se muestre totalmente y que ninguno de sus hijos esté visible ni siquiera parcialmente
	public boolean estadoInicio()
	{
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA)
		{
			if (numHijos()>0)
				return (this.entradaVisible() && hijos.get(0).entradaVisible==false);
			else
				return (this.entradaVisible());
		}
		else if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA)
		{
			if (numHijos()>0)
				return (this.salidaVisible() && hijos.get(0).salidaVisible==false);
			else
				return (this.salidaVisible());
		}
		else
		{
			if (numHijos()>0)
				return (this.entradaVisible() && hijos.get(0).entradaVisible==false);
			else
				return (this.entradaVisible() && !this.salidaVisible());
		}
	}
	
	
	
	
	/**
		Sobre un nodo del árbol, muestra todos sus hijos y los últimos hijos de su último hijo, recursivamente
	*/
	private void aparecerHijosFinales()
	{
		if (hijos.size()>0)
		{
			for (int i=0; i<hijos.size(); i++)
			{
				
			}
			hijos.get(hijos.size()-1).aparecerNodo();
			hijos.get(hijos.size()-1).setHistorico(false);
			hijos.get(hijos.size()-1).aparecerHijosFinales();
		}
	}
	
	
	public boolean entradaVisible()
	{
		return entradaVisible;
	}
	
	
	public boolean salidaVisible()
	{
		return salidaVisible;
	}
	
	
	public void desaparecerHijos()
	{
		if (hijos.size()!=0)
			for (int i=0; i<hijos.size(); i++)
				hijos.get(i).desaparecer();
	}
	
	public void aparecerHijos()
	{
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).aparecer();
	}
	
	
	public void desaparecer()
	{
		this.entradaVisible=false;
		this.salidaVisible=false;
		if (hijos.size()!=0)
			for (int i=0; i<hijos.size(); i++)
				hijos.get(i).desaparecer();
	}
	
	public void aparecer()
	{
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA || Conf.elementosVisualizar == Conf.VISUALIZAR_TODO)
			this.entradaVisible=true;
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA || Conf.elementosVisualizar == Conf.VISUALIZAR_TODO)
			this.salidaVisible=true;
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).aparecer();
	}
	
	public void aparecerNodo()
	{
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA || Conf.elementosVisualizar == Conf.VISUALIZAR_TODO)
			this.entradaVisible=true;
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA || Conf.elementosVisualizar == Conf.VISUALIZAR_TODO)
			this.salidaVisible=true;
	}
	
	
	// Este método no tiene ninguna llamada (tiene dos, pero comentadas): puede que sobre
	public void aparecerUltimaEntrada()
	{
		this.entradaVisible=true;
		if (hijos.size()>0)
			hijos.get(hijos.size()-1).aparecerUltimaEntrada();
		if (hijos.size()>0 && hijos.get(hijos.size()-1).hijos.size()==0)
			for (int i=0; i<hijos.size()-1; i++)
				hijos.get(i).aparecerNodo();
	}
	


	/**
		Comprueba si todos los hijos son visibles
		
		@param esRaiz true=si aplicamos sobre raiz del árbol
		@param poderAsignar true=si podemos hacer modificaciones sobre el atributo "mostradoEntero"
		@return true si todos los hijos son visibles
	*/
	public boolean enteroVisible(boolean esRaiz, boolean poderAsignar)
	{
		//if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO)
			return mostradoEntero;
	
	
	}
	
	
	

	private boolean enteroVisibleSimple()
	{
		if (hijos.size()==0)
		{
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA && this.entradaVisible)
				return true;
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA && this.salidaVisible)
				return true;
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO && this.entradaVisible && this.salidaVisible)
				return true;
		}
		else
		{
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO && this.entradaVisible && this.salidaVisible)
				return true;
			
			for (int i=0; i<hijos.size(); i++)
				if (!hijos.get(i).enteroVisibleSimple())
					return false;

			if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA && this.entradaVisible)
				return true;
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA && this.salidaVisible)
				return true;
		}
		return false;
	}
	
	
	
	/**
		Comprueba si ningún hijo es visible
		
		@return true si ningún hijo es visible
	*/
	public boolean vacioVisible()
	{
		return (!this.entradaVisible && !this.salidaVisible);
	}

	
	/**
		Comprueba si ningún hijo es visible
		
		@return true si ningún hijo es visible
	*/
	public boolean vacioVisiblePantalla()
	{
		System.out.println("this.entradaVisible="+this.entradaVisible);
		System.out.println("salidaVisible="+this.salidaVisible);
		System.out.println("inhibido="+this.inhibido);
		return (!this.entradaVisible && !this.salidaVisible) || (this.inhibido);
	}
	
	/**
		Devuelve el nodo que se ha de mostrar visible cuando se pulsa el botón de avance múltiple
	*/
	public RegistroActivacion getNodoActual()
	{
		RegistroActivacion ra;
		
		if (this.soyNodoActual)
			return this;
		
		for (int i=0; i<hijos.size(); i++)
		{
			ra=this.hijos.get(i).getNodoActual();
			if (ra!=null)
				return ra;
		}
		return null;
	}
	
	
	public void setEsCaminoActual(boolean v)
	{
		this.soyCaminoActual=v;
	}
	
	public boolean getEsCaminoActual()
	{
		return this.soyCaminoActual;
	}
	
	
	// Busca el nodo actual y deja su flag a false para que deje de ser el nodoActual
	public boolean borrarNodoActual()
	{
		if (this.getEsNodoActual())
		{
			this.setEsNodoActual(false);
			return true;
		}
		else
		{
			boolean encontrado=false;
			int i=0;
			while (i<this.numHijos() && !encontrado)
			{
				encontrado=this.getHijo(i).borrarNodoActual();
				i++;
			}
			return encontrado;
		}
	}
	
	
	
	
	/**
		Borra la señalizacion de la rama que actualmente se está expandiendo y que lleva al nodo activo
	*/
	public void borrarCaminoActual()
	{
		RegistroActivacion ra=this;
		
		while (ra.padre!=null)
			ra=ra.padre;
	
		// Ahora 'ra' es la raiz del árbol, vamos borrando hacia abajo
		if (ra!=null)
		{
			ra.setEsCaminoActual(false);
			
			int i=0;

			while (ra.hijos.size()!=0 && i<ra.getHijosVisibles())
			{
				if (ra.getHijo(i).getEsCaminoActual())
				{
					(ra.getHijo(i)).setEsCaminoActual(false);
					ra=ra.getHijo(i);
					i=0;
				}
				else
					i++;
			}
		}
	}
	
	
	public void crearCaminoActual()
	{
		RegistroActivacion ra=this;
		RegistroActivacion ra2=null;
		
		while (ra.padre!=null)
			ra=ra.padre;
			
		// Ahora 'ra' es la raiz del árbol
		
		ra=ra.getNodoActual();	
		ra2=ra;
		// Ahora 'ra' es el nodo actual del árbol
		
		while (ra!=null && ra.padre!=null)
		{
			ra=ra.getPadre();
			ra.setEsCaminoActual(true);
		}
		

	}
	
	public void corregirCaminoActual()	// Corregimos en función de la visibilidad de métodos
	{
		// Presuponemos camino Actual coherente, ahora hacemos el camino coherente con la visibilidad
		
		RegistroActivacion ra=this;
		if (!ra.getEsNodoActual())
		{
			while (ra.padre!=null)
				ra=ra.padre;
			// Ahora 'ra' es la raiz del árbol
			
			ra=ra.getNodoActual();
		}
		// Ahora 'ra' es el nodo actual del árbol
		while (ra!=null && ra.padre!=null)
		{
			ra=ra.padre;
			ra.setEsCaminoActual(true);
			ra.setVisibilidad(true,false,false);
		}
		
	}
	
	
	/**
		Recalcula cuál debe ser el nodo actual al pasar de "sólo entradas"/"sólo salidas" a "Entrada y Salida"
	*/
	public void actualizarNodoActualEYS()
	{
		RegistroActivacion ra=this.getNodoActual();

		if (ra!=null)
			while (ra.getPadre()!=null && (ra.getPadre().entradaVisible() && ra.getPadre().salidaVisible()))
			{
				ra.getPadre().setEsNodoActual(true);
				ra.setEsNodoActual(false);
				ra=ra.getPadre();
			}
	}
	
	/**
		Recalcula cuál debe ser el nodo actual al pasar de "Entrada y Salida" a "sólo entradas"/"sólo salidas"
	*/
	public void actualizarNodoActualES()
	{

	}
	
	
	

	/**
		Sirve para pasar al modo de visualización de valores de entrada y salida
	*/
	public void visualizarEntradaYSalida()
	{
		if (hijos.size()==0)
		{
			if (this.entradaVisible || this.salidaVisible)
			{
				this.entradaVisible=true;
				this.salidaVisible=true;
				this.mostradoEntero=true;
			}	
		}
		else	// tiene hijos
		{
			if (this.entradaVisible || this.salidaVisible)
			{
				if (hijos.get(hijos.size()-1).esHistorico())
				{
					this.entradaVisible=true;
					this.salidaVisible=true;
					this.mostradoEntero=true;
				}
				else
				{
					this.entradaVisible=true;
					this.salidaVisible=false;
					this.mostradoEntero=false;
				}
				for (int i=0; i<hijos.size(); i++)
					hijos.get(i).visualizarEntradaYSalida();
			}
		}
	}
	
	/**
		Sirve para pasar al modo de visualización de valores sólo de entrada
	*/
	public void visualizarSoloEntrada()
	{
		if ((this.entradaVisible || this.salidaVisible))
		{
			this.entradaVisible=true;
			this.salidaVisible=false;
		}
		if ((this.hijos.size()==0 || this.hijos.get(hijos.size()-1).esHistorico()) && (this.entradaVisible || this.salidaVisible))
		{
			this.entradaVisible=true;
			this.salidaVisible=false;
			if ((this.hijos.size()==0 || this.hijos.get(hijos.size()-1).esHistorico()))
				this.mostradoEntero=true;
		}
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).visualizarSoloEntrada();
	}
	
	/**
		Sirve para pasar al modo de visualización de valores sólo de salida
	*/
	public void visualizarSoloSalida()
	{
		if ((this.entradaVisible || this.salidaVisible))
		{
			this.entradaVisible=false;
			this.salidaVisible=true;
		}
			
		
		if ((this.hijos.size()==0 || this.hijos.get(hijos.size()-1).esHistorico()) && (this.entradaVisible || this.salidaVisible))
		{
			this.entradaVisible=false;
			this.salidaVisible=true;
			if ((this.hijos.size()==0 || this.hijos.get(hijos.size()-1).esHistorico()))
				this.mostradoEntero=true;
		}
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).visualizarSoloSalida();
		
			
	}
	
	/**
		Sirve para pasar al modo de visualización de valores mostrando todos los nodos recorridos
	*/
	public void visualizarConHistoria()
	{

	}
	
	
	
	/**
		Sirve para pasar al modo de visualización de valores mostrando los nodos influyentes en la rama de expansión y el resto atenuados
	*/
	public void visualizarConHistoriaAtenuada()
	{
	}	
	
	/**
		Sirve para pasar al modo de visualización de valores mostrando sólo los nodos influyentes en la rama de expansión
	*/
	public void visualizarSinHistoria()
	{
	}
	

	/**
		Revisa si todos los hijos de un nodo son enteroVisibles
	
		@return true si todos los hijos responden true a enteroVisible
	*/
	private boolean hijosEnteroVisibles()
	{
		for (int i=0; i<hijos.size(); i++)
			if (!(hijos.get(i).enteroVisible(false, false)))
				return false;
		return true;
	}

	
	/**
		Revisa si todos los hijos de un nodo son vacioVisibles
	
		@return true si todos los hijos responden true a vacioVisible
	*/
	private boolean hijosVacioVisibles()
	{
		for (int i=0; i<hijos.size(); i++)
			if (!(hijos.get(i).vacioVisible()))
				return false;
		return true;
	}
	
	
	public int getNumNodos()
	{
		if (hijos.size()==0)
			return 1;
		else
		{
			int x=1;
			for (int i=0; i<hijos.size(); i++)
				if (hijos.get(i)!=null)						// NO DEBERÍA SER NECESARIA ESTA COMPROBACIÓN
					x=x+hijos.get(i).getNumNodos();
				else
					System.out.println("hijo null");
			return x;
		}
	}
	
	public int getNumNodosVisibles()
	{
		if (this.entradaVisible() || this.salidaVisible())
		{
			int numNodosVisibles=1;
			for (int i=0; i<numHijos(); i++)
				numNodosVisibles=numNodosVisibles+hijos.get(i).getNumNodosVisibles();
			return numNodosVisibles;
		}
		else
			return 0;
	}
	
	public int getNumNodosHistoricos()
	{
		int numNodosHistoricos=0;
		if (this.esHistorico())
			numNodosHistoricos++;

		for (int i=0; i<numHijos(); i++)
			numNodosHistoricos=numNodosHistoricos+hijos.get(i).getNumNodosHistoricos();
		
		return numNodosHistoricos;
	}
	
	
	public int getNumNodosAunNoMostrados()
	{
		int numNodosAunNoMostrados=0;
		
		for (int i=0; i<numHijos(); i++)
			numNodosAunNoMostrados=numNodosAunNoMostrados+hijos.get(i).getNumNodosAunNoMostrados();
		
		if (!this.entradaVisible() && !this.salidaVisible())
			numNodosAunNoMostrados++;
		return numNodosAunNoMostrados;
	}
	
	public int getNumNodosInhibidos()
	{
		int numNodosInhibidos=0;
		
		for (int i=0; i<numHijos(); i++)
			numNodosInhibidos=numNodosInhibidos+hijos.get(i).getNumNodosInhibidos();
		
		if (this.inhibido())
			numNodosInhibidos++;
		return numNodosInhibidos;
	}
	
	public int getNumNodosIluminados()
	{
		int numNodosIluminados=0;
		
		for (int i=0; i<numHijos(); i++)
			numNodosIluminados=numNodosIluminados+hijos.get(i).getNumNodosIluminados();
		
		if (this.iluminado())
			numNodosIluminados++;
		return numNodosIluminados;
	}
	
	
	
	public int getMaximaLongitudCeldaEstructura()
	{
		int longMaxima=0;				
		
		if (this.esDYV())
		{
			Estructura e1=new Estructura(this.getEntrada().getEstructura());
			Estructura e2=new Estructura(this.getSalida().getEstructura());
			int dimensiones[]=e1.dimensiones();
			String clase=e1.getTipo().getClass().getName();
			
			if (e1.esMatriz())
			{
				for (int i=0; i<dimensiones[0]; i++)
					for (int j=0; j<dimensiones[1]; j++)
					{
						if (clase.contains("Integer"))
						{
							longMaxima=Math.max(longMaxima,(""+e1.posicMatrizInt(i,j)).length());
							longMaxima=Math.max(longMaxima,(""+e2.posicMatrizInt(i,j)).length());
						}
						else if (clase.contains("Long"))
						{
							longMaxima=Math.max(longMaxima,(""+e1.posicMatrizLong(i,j)).length());
							longMaxima=Math.max(longMaxima,(""+e2.posicMatrizLong(i,j)).length());
						}
						else if (clase.contains("Double"))
						{
							longMaxima=Math.max(longMaxima,(""+e1.posicMatrizDouble(i,j)).length());
							longMaxima=Math.max(longMaxima,(""+e2.posicMatrizDouble(i,j)).length());
						}
						else if (clase.contains("String"))
						{
							longMaxima=Math.max(longMaxima,(e1.posicMatrizString(i,j)).length());
							longMaxima=Math.max(longMaxima,(e2.posicMatrizString(i,j)).length());
						}
						else if (clase.contains("Character"))
						{
							longMaxima=Math.max(longMaxima,(""+e1.posicMatrizChar(i,j)).length());
							longMaxima=Math.max(longMaxima,(""+e2.posicMatrizChar(i,j)).length());
						}
						else if (clase.contains("Boolean"))
						{
							longMaxima=Math.max(longMaxima,(""+e1.posicMatrizBool(i,j)).length());
							longMaxima=Math.max(longMaxima,(""+e2.posicMatrizBool(i,j)).length());
						}
					}
			}
			else
			{
				for (int i=0; i<dimensiones[0]; i++)
				{
					if (clase.contains("Integer"))
					{
						longMaxima=Math.max(longMaxima,(""+e1.posicArrayInt(i)).length());
						longMaxima=Math.max(longMaxima,(""+e2.posicArrayInt(i)).length());
					}
					else if (clase.contains("Long"))
					{
						longMaxima=Math.max(longMaxima,(""+e1.posicArrayLong(i)).length());
						longMaxima=Math.max(longMaxima,(""+e2.posicArrayLong(i)).length());
					}
					else if (clase.contains("Double"))
					{
						longMaxima=Math.max(longMaxima,(""+e1.posicArrayDouble(i)).length());
						longMaxima=Math.max(longMaxima,(""+e2.posicArrayDouble(i)).length());
					}
					else if (clase.contains("String"))
					{
						longMaxima=Math.max(longMaxima,(e1.posicArrayString(i)).length());
						longMaxima=Math.max(longMaxima,(e2.posicArrayString(i)).length());
					}
					else if (clase.contains("Character"))
					{
						longMaxima=Math.max(longMaxima,(""+e1.posicArrayChar(i)).length());
						longMaxima=Math.max(longMaxima,(""+e2.posicArrayChar(i)).length());
					}
					else if (clase.contains("Boolean"))
					{
						longMaxima=Math.max(longMaxima,(""+e1.posicArrayBool(i)).length());
						longMaxima=Math.max(longMaxima,(""+e2.posicArrayBool(i)).length());
					}
				}
			}
		}
		for (int i=0; i<this.numHijos(); i++)
			longMaxima = Math.max( longMaxima , this.getHijo(i).getMaximaLongitudCeldaEstructura() );

		return longMaxima;
	}
	
	public String getParametro(int i)
	{
		return this.entrada.getParametro(i); 
	}
	
	public String[] getParametros()
	{
		return this.entrada.getParametros(); 
	}
	
	public String[] getResultado()
	{
		return this.salida.getParametros();
	}
	
	
	
	public String[][] getValoresParametrosInicio(String interfaz)
	{
		ArrayList<String[]> valores=this.getValoresParametros(interfaz);
		
		String[][] valoresArray=new String[valores.size()][];
		  
		for (int i=0; i<valores.size(); i++)
			valoresArray[i]=valores.get(i);
		       
		return valoresArray;       
	}
	
	public ArrayList<String[]> getValoresParametros(String interfaz)
	{
		ArrayList<String[]> valores=new ArrayList<String[]>();
		
		if (interfaz.equals(this.interfazMetodo()) && this.entradaVisible())
			valores.add(this.getParametros());
		
		for (int i=0; i<this.numHijos(); i++)
			valores.addAll((this.getHijo(i).getValoresParametros(interfaz)));
		
		return valores;
	}
	
	
	
	public String[][] getValoresResultadoInicio(String interfaz)
	{
		ArrayList<String[]> valores=this.getValoresResultado(interfaz);
		
		String[][] valoresArray=new String[valores.size()][];
		  
		for (int i=0; i<valores.size(); i++)
			valoresArray[i]=valores.get(i);
		       
		return valoresArray;       
	}
	
	public ArrayList<String[]> getValoresResultado(String interfaz)
	{
		ArrayList<String[]> valores=new ArrayList<String[]>();
		
		if (interfaz.equals(this.interfazMetodo()) && this.salidaVisible() && this.esMostradoEntero())
			valores.add(this.getResultado());
		
		for (int i=0; i<this.numHijos(); i++)
			valores.addAll((this.getHijo(i).getValoresResultado(interfaz)));
		
		return valores;
	}
	
	
	
	public int getNumNodosMetodo(String interfaz)
	{
		int nodos=0;
		
		if (interfaz.equals(this.interfazMetodo()))
			nodos++;
		
		for (int i=0; i<this.numHijos(); i++)
			nodos=nodos+this.getHijo(i).getNumNodosMetodo(interfaz);
		
		
		return nodos;
	}
	
	
	public String interfazMetodo()
	{
		String interfaz=this.getNombreMetodo();
		
		String tiposParam[]=this.entrada.getClases();
		
		int dimParam[]=this.entrada.getDimensiones();
		
		interfaz=interfaz+" (";
		
		for (int i=0; i<tiposParam.length; i++)
		{
			interfaz=interfaz+tiposParam[i];
			if (dimParam[i]>0)
				interfaz=interfaz+" "+ServiciosString.cadenaDimensiones(dimParam[i]);
			interfaz=interfaz+" "+this.nombreParametros[i];
			if (i<tiposParam.length-1)
				interfaz=interfaz+", ";
		}
		
		interfaz=interfaz+")";
		return interfaz;
	}
	
	
	public String[] interfacesMetodos()
	{
		String []nombres=new String[1];

		nombres[0]=this.interfazMetodo();

		for (int i=0; i<hijos.size(); i++)
		{
			String []nombreshijo=hijos.get(i).interfacesMetodos();
			String []nombresAux=new String[nombreshijo.length+nombres.length];
			for (int j=0; j<nombreshijo.length; j++)
				nombresAux[j]=nombreshijo[j];
			for (int j=0; j<nombres.length; j++)
				nombresAux[j+nombreshijo.length]=nombres[j];
				
			nombres=nombresAux;
		}
		
		return nombres;
	}
	
	
	
	public String[] nombresMetodos()
	{
		String []nombres=new String[1];
		nombres[0]=this.getNombreMetodo();
			
		for (int i=0; i<hijos.size(); i++)
		{
			String []nombreshijo=hijos.get(i).nombresMetodos();
			String []nombresAux=new String[nombreshijo.length+nombres.length];
			for (int j=0; j<nombreshijo.length; j++)
				nombresAux[j]=nombreshijo[j];
			for (int j=0; j<nombres.length; j++)
				nombresAux[j+nombreshijo.length]=nombres[j];
				
			nombres=nombresAux;
		}
		
		return nombres;
	}
	
	public int getNumMetodos()
	{
		String []nombres=this.nombresMetodos();
		
		//for (int i=0; i<nombres.length; i++)
		//	System.out.println("nombre 1 : ["+nombres[i]+"]");
		
		String []nombresNR=quitarRepetidos(nombres);
		
		//for (int i=0; i<nombresNR.length; i++)
		//	System.out.println("nombre 2 : ["+nombresNR[i]+"]");
		
		return nombresNR.length;
	}
	
	public String[] getNombresMetodos()
	{
		String []nombres=this.nombresMetodos();
		
		return quitarRepetidos(nombres);
	}	
	
	
	private String [] quitarRepetidos(String nombres[])
	{
		String []nombresSinRepeticion=new String[0];
		
		for (int i=0; i<nombres.length; i++)
		{
			if (nombresSinRepeticion.length==0)
			{
				nombresSinRepeticion=new String[1];
				nombresSinRepeticion[0]=nombres[i];
			}
			else
			{
				boolean encontrado=false;
				for (int j=0; j<nombresSinRepeticion.length; j++)
					if (nombresSinRepeticion[j].equals(nombres[i]))
						encontrado=true;
				if (!encontrado)
				{
					String []nombresSinRepeticionAux=new String[nombresSinRepeticion.length+1];
					for (int j=0; j<nombresSinRepeticion.length; j++)
						nombresSinRepeticionAux[j]=nombresSinRepeticion[j];
					nombresSinRepeticionAux[nombresSinRepeticion.length]=nombres[i];
					nombresSinRepeticion=nombresSinRepeticionAux;
				}
			}
		}
		return nombresSinRepeticion;
	}
	
	
	
	public int tipoEstructura()
	{
		int miValor=0;
		if (this.entrada.getEstructura()!=null)
		{
			Estructura e=new Estructura(this.entrada.getEstructura());
			if (e!=null)
			{
				if (e.esArray())
					miValor=1;
				else
					miValor=2;
			}
		}
		
		if (miValor!=2)	// Si no tenemos estructura de tipo matriz, que es el máximo que puede haber...
			for (int i=0; i<this.numHijos(); i++)
				miValor=Math.max( miValor , this.getHijo(i).tipoEstructura() );
		
		return miValor;
	}
	
	
	
	
	private RegistroActivacion getFinalCaminoActual()
	{
		if (!this.soyCaminoActual)
			return null;
		
		if (this.soyCaminoActual && this.numHijos()==0)
			return this;
			
		for (int i=0; i<this.numHijos(); i++)
			if (this.getHijo(i).getEsCaminoActual())
				return this.getHijo(i).getFinalCaminoActual();
		return this;
		
	}
	
	
	protected RegistroActivacion getRegistroActivo()
	{
		if (this.getEsNodoActual())
			return this;
		
		for (int i=0; i<this.numHijos(); i++)
		{
			RegistroActivacion ractivo=this.getHijo(i).getRegistroActivo();
			if (ractivo!=null)
				return ractivo;
		}
		
		
		return null;
	}
	
	
	protected RegistroActivacion getRegistroActivacionPorID(int id)
	{
		if (this.nID==id)
			return this;
			
		for (int i=0; i<this.numHijos(); i++)
		{
			RegistroActivacion ra=this.getHijo(i).getRegistroActivacionPorID(id);
			if (ra!=null)
				return ra;
		}
		return null;
	}
	
	
	
	

	public ArrayList<RegistroActivacion> getRegistrosNivel(int nivel, boolean todos)	// todos=false -> sólo los enteros visibles
	{
		RegistroActivacion ra=this;
		
		while (ra.padre!=null)
			ra=ra.padre;

		return ra.getRegistrosNivelHijos(nivel-1,todos);
	}


	public ArrayList<RegistroActivacion> getRegistrosNivelHijos(int nivel, boolean todos)
	{
		ArrayList<RegistroActivacion> registros=new ArrayList<RegistroActivacion>(0);
		
		if (nivel!=0)
		{
			if (numHijos()>0)
				for (int i=0; i<numHijos(); i++)
					registros.addAll( this.hijos.get(i).getRegistrosNivelHijos(nivel-1,todos));	
			else
				if (todos || this.enteroVisibleSimple())
					registros.add ( this );
		}
		else
		{
			if (todos || this.enteroVisibleSimple())
				registros.add ( this );
		}
		return registros;
	}
	
	protected void actualizarEstadoFlagsDesdeGemelo(Traza traza)
	{
		RegistroActivacion ra=traza.getRegistroActivacionPorID(this.nID);
	
		if (ra!=null)
		{
			this.asignacion(ra.soyNodoActual,ra.soyCaminoActual,ra.mostradoEntero,ra.historico,ra.entradaVisible,ra.salidaVisible);
			this.metodoVisible=ra.metodoVisible;
			this.contraido=ra.contraido;
			this.inhibido=ra.inhibido;
			this.iluminado=ra.iluminado;
		}
		
		for (int i=0; i<this.numHijos(); i++)
			this.getHijo(i).actualizarEstadoFlagsDesdeGemelo(traza);
	}
	
	
	protected void hacerCoherente()
	{
		/*RegistroActivacion ra=this;
		while (ra.padre!=null)
			ra=ra.padre;*/
	
	
		RegistroActivacion actual=this.getNodoActual();	// Suponemos que this es raiz
		
		this.corregirCaminoActual();
		this.revivirNodos();	// Nodos que estaban ocultos y que ahora son visibles, se deben activar sus entradas y salidas
		
		if (actual==null)
		{
			actual=this.getFinalCaminoActual();
			
			if (actual.numHijos()==0)
			{
				actual.setEsCaminoActual(false);
				actual.setEsNodoActual(true);
				
				

				
				
			}
			else
			{
				int i=actual.numHijos()-1;
				boolean asignado=false;
				while (!asignado && i>=0)
				{
					if (actual.getHijo(i).entradaVisible() || actual.getHijo(i).salidaVisible())
					{
						asignado=true;
						actual.getHijo(i).setEsNodoActual(true);
						actual.getHijo(i).setHistorico(false);	//nueva
					}
					i--;
				}
				if (!asignado && i<0)
				{
					actual.setEsCaminoActual(false);
					actual.setEsNodoActual(true);
				}
			}
		}
		else
		{
			if (actual.esMostradoEntero())
			{
				for (int i=0; i<actual.numHijos(); i++)
					actual.getHijo(i).asignacionR(true,true,true,true);
			}
			else
			{
				if (actual.numHijos()!=0)
					for (int i=0; i<actual.numHijos(); i++)
					{
						actual.getHijo(i).setEsNodoActual(false);
						actual.getHijo(i).setEsCaminoActual(false);
					}
				else
					if (Conf.elementosVisualizar != Conf.VISUALIZAR_TODO)
						actual.setMostradoEntero(true);
			}
			
		}
		
	}
	
	
	
	
	
	private void revivirNodos()
	{
		if (this.entradaVisible==false && this.salidaVisible==false)
		{
			// Primero nos fijamos en padre: si está completo visible, este nodo tendra que estar completo visible
			if (this.padreVisible()==null || this.padreVisible().esMostradoEntero())
			{
				//asignacion(nodoActual, caminoActual, mostradoEntero, historico, eV, sV)
				this.asignacion(false, false, true, true, true, true);
				this.asignacionR(true,true,true,true);
			}
			
			// Luego nos fijamos en hijos:
			else if (this.numHijos()>0)
			{
				// Si hay algo visible...
				RegistroActivacion r=this.getPrimerHijoVisible();
				if (r!=null)
				{
					if (this.getUltimoHijoVisible().esHistorico())
					{
						//this.asignacionR(true, true, true, true);
						//this.asignacion(false, false, true, false, true, true);
						
						this.asignacionR(true, true, true, true);
						this.asignacion(false, false, true, true, true, true);
						//this.getUltimoHijoVisible().asignacion(false, false, true, false, true, true);
					}
					this.entradaVisible=true;
				}
			}
		}	
		for (int i=0; i<this.numHijos(); i++)
		{
			if (this.getHijo(i).esHistorico())
			{
				this.getHijo(i).asignacionR(true, true, true, true);
			}
			/*else if (this.getHijo(i).vacioVisible())
			{
				System.out.println("<< revivirNodos "+this.nombreMetodo+" "+this.getEntradaString()+"/"+this.getSalidaString()+" for if 2");
				this.getHijo(i).asignacionR(false, false, false, false);
			}*/
			else
			{
				this.getHijo(i).revivirNodos();
			}
		}
	}
	
	
	
	private RegistroActivacion padreVisible()
	{
		RegistroActivacion ra=this.padre;
		while (ra!=null && !ra.metodoVisible)
			ra=ra.padre;
			
		return ra;	
	}
	
	
	
	public ArrayList<RegistroActivacion> getRegistrosEnteros()	// devuelve nodos enteros visibles
	{
		RegistroActivacion ra=this;
		
		while (ra.padre!=null)
			ra=ra.padre;
		//System.out.println("getRegistrosEnteros");
		return ra.getRegistrosEnterosR();
	}
	
	public ArrayList<RegistroActivacion> getRegistrosEnterosR()
	{
		ArrayList<RegistroActivacion> registros=new ArrayList<RegistroActivacion>(0);
	
		if (numHijos()>0)
			for (int i=0; i<numHijos(); i++)
				registros.addAll( this.hijos.get(i).getRegistrosEnterosR());	

		if (this.enteroVisibleSimple() && this.esMostradoEntero())
		{
			//System.out.println("."+this.getNivel());
			registros.add ( this );
		}

		return registros;
	}	
	
	public ArrayList<RegistroActivacion> getRegistrosIniciados()	// devuelve nodos enteros visibles
	{
		RegistroActivacion ra=this;
		
		while (ra.padre!=null)
			ra=ra.padre;

		return ra.getRegistrosIniciadosR();
	}
	
	public ArrayList<RegistroActivacion> getRegistrosIniciadosR()
	{
		ArrayList<RegistroActivacion> registros=new ArrayList<RegistroActivacion>(0);
	
		if (numHijos()>0)
			for (int i=0; i<numHijos(); i++)
				registros.addAll( this.hijos.get(i).getRegistrosIniciadosR());	

		if ((!this.enteroVisibleSimple() || this.getEsNodoActual() || this.getEsCaminoActual())
			&& 
			(this.entradaVisible || this.salidaVisible))
			
			registros.add ( this );

		return registros;
	}		
	
	
	
	
	
	
	public void asignarInhibidos()
	{
		if (this.contraido())
			for (int i=0; i<this.numHijos(); i++)
				this.getHijo(i).asignarInhibidoRecursivo(true);
		else
			for (int i=0; i<this.numHijos(); i++)
				this.getHijo(i).asignarInhibidos();
	}
	
	public void asignarInhibidoRecursivo(boolean valor)
	{
		this.inhibido=valor;
		for (int i=0; i<this.numHijos(); i++)
				this.getHijo(i).asignarInhibidoRecursivo(valor);
		
	}
	
	
	public void asignarMetodosNoVisibles()
	{
		if (!this.metodoVisible)
		{
			this.asignacion(false,false,false,false,false,false);
			//asignacion(nodoActual, caminoActual, mostradoEntero, historico, eV, sV)
			
		}	
		for (int i=0; i<this.numHijos(); i++)
			this.getHijo(i).asignarMetodosNoVisibles();
	}
	
	
	
	protected void ajustarNodosVisibles()
	{
		int i=0; 
	
		if (this.getEsCaminoActual())
		{
			this.asignacion(
					false,true,false,false,
					Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA || Conf.elementosVisualizar == Conf.VISUALIZAR_TODO,
					Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA		);
			
			// Nos ocupamos de hijos que son parte del pasado
			while (i<this.numHijos() &&
					(!this.getHijo(i).getEsCaminoActual() && !this.getHijo(i).getEsNodoActual()))
			{
				this.getHijo(i).asignacionR(true, true, true, true);
				i++;
			}
			
			if (i<this.numHijos() && (this.getHijo(i).getEsCaminoActual() || this.getHijo(i).getEsNodoActual()))
			{
				this.getHijo(i).ajustarNodosVisibles();
				i++;
			}
			
			while (i<this.numHijos())
			{
				this.getHijo(i).asignacionR(false, false, false, false);
				i++;
			}
		}
		else if (this.getEsNodoActual())
		{
			this.asignacion(true, false, false/*Conf.elementosVisualizar != Conf.VISUALIZAR_TODO*/, false, 
					Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA || Conf.elementosVisualizar == Conf.VISUALIZAR_TODO, 
					Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
			for (i=0; i<this.numHijos(); i++)
				this.getHijo(i).asignacionR(false, false, false, false);
		}
	}
	
	
	
	protected RegistroActivacion getUltimoHijoVisible()
	{
		RegistroActivacion r=null;
		
		for (int i=this.numHijos()-1; i>=0; i--)
		{
			r=this.getHijo(i).getUltimoHijoVisibleRec();
			if (r!=null)
				return r;
		}

		return null;
	}
	
	public int getNumHijosVisibles()
	{
		RegistroActivacion r=null;
		
		for (int i=0; i<this.numHijos(); i++)
			if (!this.getHijo(i).enteroVisible(false,false))
				return i;

		return this.numHijos();
	}
	
	
	protected RegistroActivacion getUltimoHijoVisibleRec()
	{
		RegistroActivacion r=null;
		
		if (this.esMetodoVisible() && (this.entradaVisible() || this.salidaVisible()))
			return this;
			
		for (int i=this.numHijos()-1; i>=0; i--)
		{
			r=this.getHijo(i).getUltimoHijoVisibleRec();
			if (r!=null)
				return r;
		}

		return r;
	}
	
	protected RegistroActivacion getPrimerHijoVisible()
	{
		RegistroActivacion r=null;
		
		for (int i=0; i<this.numHijos(); i++)
		{
			r=this.getHijo(i).getPrimerHijoVisibleRec();
			if (r!=null)
				return r;
		}

		return null;
	}
	
	
	protected RegistroActivacion getPrimerHijoVisibleRec()
	{
		RegistroActivacion r=null;
		
		if (this.esMetodoVisible() && (this.entradaVisible() || this.salidaVisible()))
			return this;
			
		for (int i=0; i<this.numHijos(); i++)
		{
			r=this.getHijo(i).getPrimerHijoVisibleRec();
			if (r!=null)
				return r;
		}

		return r;
	}
	
	
	public void hacerNodoActual()
	{
		// Primero borramos camino actual
		RegistroActivacion ra=this;
		while (ra.padre!=null)
			ra=ra.padre;
		
		ra.borrarCaminoActual();
		ra.borrarNodoActual();

		// Después asignamos a this el nodoActual y creamos caminoActual
		this.setEsNodoActual(true);
		
		ra.crearCaminoActual();
		this.setEsNodoActual(true);
		this.asignacion(true, false, false, false,
				Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA || Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA, false);

		for (int i=0; i<this.numHijos(); i++)
			this.getHijo(i).asignacionR(false,false,false,false);

		// Hacemos coherente el estado del resto de nodos
		ra.ajustarNodosVisibles();
		//VentanaVisualizador.thisventana.traza.verArbol("ComoQueda.txt");
	}
	
	
	public void iluminar(boolean valor)
	{
		this.iluminado=valor;
	}
	
	public void resaltar(boolean valor)
	{
		this.resaltado=valor;
	}
	
	
	
	public int iluminar(int numeroMetodo, String valoresE[],String valoresS[],boolean valor)
	{
		int numNodosIluminados=0;
		boolean candidatoValido=true;
		
		if (valoresE!=null || valoresS!=null)
		{
			if(this.numMetodo==numeroMetodo)
			{
				String []valoresParam=null;
				if (valoresE!=null)
				{
					valoresParam=this.entrada.getParametros();
					for (int i=0; i<valoresParam.length; i++)
					{
						if (!((valoresE[i]==null || valoresE[i].length()==0 )	||	valoresParam[i].equals(valoresE[i])))
							candidatoValido=false;
					}
				}
				
				if (valoresS!=null)
				{
					valoresParam=this.salida.getParametros();
					for (int i=0; i<valoresParam.length; i++)
					{
						if (!((valoresS[i]==null || valoresS[i].length()==0 )	||	valoresParam[i].equals(valoresS[i])))
							candidatoValido=false;
					}
				}
				if (candidatoValido)
				{
					this.iluminado=valor;
					numNodosIluminados++;
				}
			}
		}
		else
		{
			this.iluminado=false;
			numNodosIluminados++;
		}
		for (int i=0; i<this.numHijos(); i++)
			numNodosIluminados=numNodosIluminados+this.hijos.get(i).iluminar(numeroMetodo,valoresE,valoresS,valor);
		
		return numNodosIluminados;
	}
	
	
	public boolean estaIluminado()
	{
		return this.iluminado;
	}
	
	public boolean estaResaltado()
	{
		return this.resaltado;
	}
	
	
	//// Depuracion
	
	
	

	
	public void verDepurar1(String fichero)
	{
		if (this.padre==null) 	// Sólo si es raiz...
		{
			FileWriter fw=null;
			try {
				fw = new FileWriter(fichero);
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 1");
			}

			verDepurar1(fw,0);	// Llamada recursiva

			try {
				fw.close();
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 2");
			}
		}
	}
	

	public void verDepurar1(FileWriter fw,int margen)
	{
		String c="";
		for (int i=0; i<margen; i++) c=c+" ";
		
		try {
			fw.write(c+this.nombreMetodo+" [");
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO || Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA)
				fw.write(this.entrada.getRepresentacion());
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO)
				fw.write("|");
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO || Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA)
				fw.write(this.salida.getRepresentacion());
			fw.write("]");
			fw.write("  "+(this.getEntrada().getEstructura()==null)+"\r\n");
		} catch (IOException ioe) {
			System.out.println("Error FileWriter 2");
		}
		
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).verDepurar1(fw,margen+2);// Llamada recursiva
	}
	
	
	
	
	public void verArbol(String fichero)
	{
		if (this.padre==null) 	// Sólo si es raiz...
		{
			FileWriter fw=null;
			try {
				fw = new FileWriter(fichero);
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 1");
			}

			verArbol(fw,0);

			try {
				fw.close();
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 2");
			}
		}
	}

	public void verArbol(FileWriter fw,int margen)
	{
		String c="";
		for (int i=0; i<margen; i++) c=c+" ";
		
		try {
			fw.write(c+this.nombreMetodo+" [");
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO || Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA)
			{
				for (int i=0; i<this.getParametros().length; i++)
					fw.write(" <"+this.getParametro(i)+"> ");
				//fw.write(this.entrada.getRepresentacion());
			}
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO)
			{
				fw.write("|");
			}
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO || Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA)
			{
				fw.write(this.salida.getRepresentacion());
			}
			fw.write("]");
			fw.write("  "+eval(entradaVisible)+"/"+eval(salidaVisible)+" | Hist="+eval(this.historico)+" | mEnt="+eval(this.mostradoEntero)
								+" | nActual="+eval(this.soyNodoActual)+" | cActual="+eval(this.soyCaminoActual)
								+" | contr="+eval(this.contraido)+" | inh="+eval(this.inhibido)+" | metVisib="+eval(this.esMetodoVisible())+" | devuelve="+eval(this.devuelveValor)+"\r\n");
		} catch (IOException ioe) {
			System.out.println("Error FileWriter 2");
		}
		
		for (int i=0; i<hijos.size(); i++)
			hijos.get(i).verArbol(fw,margen+2);
	}
	
	private String eval(boolean valor)
	{
		if (valor)
			return "V";
		else
			return "F";
	}
	
}