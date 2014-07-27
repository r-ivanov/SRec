/**
	Esta clase almacena globalmente los datos de la ejecución: llamadas, valores, parámetros, ...

	@author Luis Fernández y Antonio Pérez Carrasco
	@version 2006-2007

*/
package datos;



import utilidades.ServiciosString;
import ventanas.Ventana;



public class Traza
{
	private static Traza traza = new Traza();
	private RegistroActivacion raiz = null;
	private RegistroActivacion ultimo = null;
	
	private String idTraza;
	
	private String archivo="";
	private String metodoEjecucion;	// Almacena el metodo cuya ejecución se lanzó
	
	private String titulo;	// Sirve de titulo para el panel que contiene las vistas

	private int[] tecnicas=null;
	
	/**
		Constructor: crea una nueva instancia de la traza.
	*/
	public Traza ()
	{
		RegistroActivacion.reinicializar();
	}
	
	/**
		Devuelve la raiz de la traza
		
		@return la raiz de la traza
	*/
	public RegistroActivacion getRaiz ()
	{
		return raiz;
	}
	
	/**
		Devuelve el último elemento de la traza
		
		@return el último elemento de la traza
	*/
	public RegistroActivacion getUltimo ()
	{
		return ultimo;
	}

	/**
		Fuerza a que el primer elemento sea el último también
		
		@return el último elemento de la traza
	*/
	public void eqRaizUltimo ()
	{
		this.ultimo=this.raiz;
	}

	/**
		Devuelve la instancia única de la traza de ejecución
		
		@return la traza
	*/
	public static Traza singleton ()
	{
		return Traza.traza;
	}

	public String getIDTraza()
	{
		return this.idTraza;
	}
	
	public void setIDTraza(String id)
	{
		this.idTraza=id;
	}
	
	
	/*public void setTecnica(int tecnica)
	{
		this.tecnica=tecnica;
	}*/
	
	public void setTecnicas(int[] tecnicas)
	{
		this.tecnicas=tecnicas;
	}
	
	
	public int[] getTecnicas()
	{
		return this.tecnicas;
	}
	
	public void setNombreMetodoEjecucion(String metodoEjecucion)
	{
		this.metodoEjecucion=metodoEjecucion;
	}
	
	public String getNombreMetodoEjecucion()
	{
		return this.metodoEjecucion;
	}
	
	/**
		Añade una entrada a la traza
	*/
	public void anadirEntrada(Estado estado,String nombreMetodo,String nombreParametros[])
	{
		if (raiz==null)
		{
			raiz = new RegistroActivacion(estado,nombreMetodo,nombreParametros);
			ultimo = raiz;
		}
		else 
			ultimo = ultimo.anadirEntrada(estado,nombreMetodo,nombreParametros);
	}

	/**
		Añade una salida a la traza
	*/
	public void anadirSalida(Estado estado,boolean devuelveValor)//String nombreMetodo,String nombreParametros[])
	{
		ultimo.setDevuelveValor( devuelveValor );
		ultimo = ultimo.anadirSalida(estado);
	}
	
	/**
		Añade una salida a la traza
	*/
	public void anadirSalida(Estado estado,String nombreMetodo,String nombreParametros[],boolean devuelveValor)
	{
		ultimo.setDevuelveValor( devuelveValor );
		ultimo = ultimo.anadirSalida(estado);
	}

	public void anadirSalida(Estado estado,String nombreMetodo,String nombreParametros[])
	{
		ultimo.setDevuelveValor( false );
		ultimo = ultimo.anadirSalida(estado);
	}

	
	public String[] getLineasTraza()
	{
		return this.getRaiz().getLineasTraza(4);
	}
	
	public String[] getLineasTrazaSalidaLigadaEntrada()
	{
		return this.getRaiz().getLineasTrazaSalidaLigadaEntrada(4);
	}
	
	
	public void setArchivo(String s)
	{
		this.archivo=s;
	}
	
	public String getArchivo()
	{
		return this.archivo;
	}
	
	
	
	public int getNumNodos()
	{
		return this.raiz.getNumNodos();
	}

	
	public int getAltura()	// Devuelve la altura del árbol
	{
		return this.raiz.getMaxAltura();
	}

	public int getNumNodosVisibles()
	{
		return this.raiz.getNumNodosVisibles();
	}
	
	
	
	public int getNumNodosHistoricos()
	{
		return this.raiz.getNumNodosHistoricos();
	}
	
	
	public int getNumNodosAunNoMostrados()
	{
		return this.raiz.getNumNodosAunNoMostrados();
	}
	
	
	public int getNumNodosMetodo(String interfaz)
	{
		return this.raiz.getNumNodosMetodo(interfaz);
	}
	
	
	public String[][] getValoresParametros(String interfaz, boolean filtrarRepetidos)
	{
		String interfazSinTipo=interfaz.substring(0,interfaz.lastIndexOf(" ["));
		interfazSinTipo=interfazSinTipo.replace("( ", " (");
		interfazSinTipo=interfazSinTipo.replace(" )", ")");

		String[][] datos=this.raiz.getValoresParametrosInicio(interfazSinTipo);
		
		/*for (int i=0; i<datos.length; i++)
		{
			System.out.print("i= "+i+"  ");
			for (int j=0; j<datos[i].length; j++)
				System.out.print("["+datos[i][j]+"] ");
			System.out.println();
		}*/
	
		if (filtrarRepetidos)
		{
			for (int i=0; i<datos.length; i++)				// i = num Nodo almacenado, los vamos recorriendo
			{
				for (int j=i+1; j<datos.length; j++)			// j = num Nodo de comparación, comparamos hacia adelante
				{
					for (int k=0; k<datos[i].length; k++)	// k = num Parámetro
					{
						if (datos[i][k]!=null && datos[j][k]!=null && datos[i][k].equals(datos[j][k]))
							datos[j][k]=null;
					}
				}
			}

			/*System.out.println("Traza.getValoresParametros: <"+interfazSinTipo+"> <"+(""+filtrarRepetidos)+">");
			for (int i=0; i<datos.length; i++)
			{
				for (int j=0; j<datos[i].length; j++)
					System.out.print("["+datos[i][j]+"] ");
				System.out.println();
			}*/
		}
		
		
		return datos;
	}
	
	public String[][] getValoresResultado(String interfaz, boolean filtrarRepetidos)
	{
		String interfazSinTipo=interfaz.substring(0,interfaz.lastIndexOf(" ["));
		interfazSinTipo=interfazSinTipo.replace("( ", " (");
		interfazSinTipo=interfazSinTipo.replace(" )", ")");

		String[][] datos=this.raiz.getValoresResultadoInicio(interfazSinTipo);
		
		for (int i=0; i<datos.length; i++)
		{
			System.out.print("i= "+i+"  ");
			for (int j=0; j<datos[i].length; j++)
				System.out.print("["+datos[i][j]+"] ");
			System.out.println();
		}
	
		if (filtrarRepetidos)
		{
			for (int i=0; i<datos.length; i++)				// i = num Nodo almacenado, los vamos recorriendo
			{
				for (int j=i+1; j<datos.length; j++)			// j = num Nodo de comparación, comparamos hacia adelante
				{
					for (int k=0; k<datos[i].length; k++)	// k = num Parámetro
					{
						if (datos[i][k]!=null && datos[j][k]!=null && datos[i][k].equals(datos[j][k]))
							datos[j][k]=null;
					}
				}
			}

			System.out.println("Traza.getValoresResultado: <"+interfazSinTipo+"> <"+(""+filtrarRepetidos)+">");
			for (int i=0; i<datos.length; i++)
			{
				for (int j=0; j<datos[i].length; j++)
					System.out.print("["+datos[i][j]+"] ");
				System.out.println();
			}
		}
		
		
		return datos;
	}
	
	public String[] getInterfacesMetodos()
	{
		String []interfaces=this.raiz.interfacesMetodos();
		
		interfaces=ServiciosString.quitarCadenasRepetidas(interfaces);
		
		int mitad=interfaces.length/2;
		if (interfaces.length%2==0)
			mitad--;
		//if (interfaces.length>2)
			for (int i=interfaces.length-1; i>mitad; i--)
			{
				String cadAux=interfaces[i];
				interfaces[i]=interfaces[interfaces.length-i-1];
				interfaces[interfaces.length-i-1]=cadAux;
			}
		/*else
		{
			String cadAux=interfaces[0];
			interfaces[0]=interfaces[1];
			interfaces[1]=cadAux;
		}*/

		return interfaces;
	}
	
	
	public int getNumNodosInhibidos()
	{
		return this.raiz.getNumNodosInhibidos();
	}
	
	public int getNumNodosIluminados()
	{
		return this.raiz.getNumNodosIluminados();
	}
	
	
	public String getTitulo()
	{
		return this.titulo;
	}
	
	
	public void setTitulo(String titulo)
	{
		this.titulo=titulo;
	}
	
	
	
	
	/**
		Asigna a la raíz un valor determinado. Actualmente en desuso
	*/
	public void setRaiz(RegistroActivacion ra)
	{
		this.raiz=ra;
	}


	public void verArbol(String fichero)
	{
		raiz.verArbol(fichero);
	}
	
	// Llamar sólo para depurar, ¡gracias!
	public void verArbol()
	{
		verArbol("arbolTraza.txt");
	}
	

	
	
	// Mira si una traza está con la raiz mostrandose en su estado inicial (Entrada pero sin hijos)
	public boolean raizInicio()
	{
		return this.getRaiz().estadoInicio();
	}
	
	public boolean nodoActualCompleto()
	{
		RegistroActivacion ra=raiz.getNodoActual();
		if ((ra!=null && ra.actualyCompleto()) || (ra==null))
			return true;
		else 
			return false;
	}
	
	public int getNumMetodos()
	{
		return raiz.getNumMetodos();
	}
	
	public String[] getNombresMetodos()
	{
		return raiz.getNombresMetodos();
	}
	
	public int tipoEstructura()
	{
		
		return this.raiz.tipoEstructura();
	}
	
	/**
		Recalcula la traza para que haya varios elementos más visibles (consiguiendo la visualización completa de determinado nodo
	*/
	public void siguienteMultipleVisible()
	{
		this.raiz.borrarCaminoActual();
		if (!this.enteroVisible())
		{
			RegistroActivacion ra=raiz.getNodoActual();
			if (ra!=null)
			{
				ra.todoVisible();
				ra.contraer();	// ra queda contraido y sus hijos inhibidos
			}
		}
		this.raiz.crearCaminoActual();
	}
	
	
	
	/**
		Recalcula la traza para que haya un elemento más visible
	*/
	public void siguienteVisible()
	{
		//this.raiz.borrarCaminoActual();
		if (!this.getRaiz().esMostradoEntero())
			raiz.siguienteVisible();
		//this.raiz.crearCaminoActual();
	}

	/**
		Recalcula la traza para que haya un elemento menos visible
	*/
	public void anteriorVisible()
	{
		this.raiz.borrarCaminoActual();
		boolean valor=!(this.vacioVisible());
		
		if (valor)
			raiz.anteriorVisible();
		this.raiz.crearCaminoActual();
	}

	
	public void anteriorMultipleVisible()
	{
		this.raiz.borrarCaminoActual();
		if (!this.vacioVisible())
		{
			RegistroActivacion ra=raiz.getNodoActual();
			if (ra!=null)
			{
				ra.nadaVisible();
				ra.siguienteVisible();
				//ra.asignarInhibidoRecursivo(false);	// no es necesario
			}
		}
		this.raiz.crearCaminoActual();
	}
	
	
	/**
		Recalcula la traza para que todos los elementos sean visibles
	*/
	public void todoVisible()
	{
		this.raiz.borrarCaminoActual();
		raiz.todoVisible();
		this.raiz.crearCaminoActual();
	}

	/**
		Recalcula la traza para que ningún elemento sea visible
	*/
	public void nadaVisible()
	{
		this.raiz.borrarCaminoActual();
		raiz.nadaVisible();
		this.raiz.crearCaminoActual();
		siguienteVisible();
	}

	/**
		Comprueba que todos los elementos sean visibles
		
		@return true si todos los elementos son visibles
	*/
	public boolean enteroVisible()
	{
		return raiz.enteroVisible();
	}

	/**
		Comprueba que ningún elemento sea visible
		
		@return true si ningún elementos es visible
	*/
	public boolean vacioVisible()
	{
		return raiz.vacioVisible();
	}

	/**
		Muestra el estado de la traza por terminal
	*/
	/*public void visualizar ()
	{
		if (!raiz.vacioVisible())
			raiz.visualizar();	
	}*/


	


	/**
		Vacía la traza para borrar la ejecución
	*/
	public void vaciarTraza()
	{
		raiz = null;
		ultimo = null;
	}

	/**
		Copias las referencias a la traza
	*/
	public Traza copiar()
	{
		Traza t=new Traza();
		t.raiz=this.getRaiz().copiar(null);
		
		t.setTecnicas(this.tecnicas);
		t.setIDTraza(new String(this.getIDTraza()+""));
		t.setArchivo(new String(this.getArchivo()+""));
		t.setNombreMetodoEjecucion(new String(this.getNombreMetodoEjecucion()+""));
		t.setTitulo(new String(this.getTitulo()+""));
	
		return t;
	}

	
	public int iluminar(int numMetodo, String valoresE[],String valoresS[],boolean valor)
	{
		return this.raiz.iluminar(numMetodo,valoresE,valoresS,valor);
	}
	
	
	/**
		Asigna qué parametros son visibles durante la animación
		
		@param 
	*/	
	public void setVisibilidad(ClaseAlgoritmo claseAlgoritmo)
	{
		raiz.setVisibilidad(claseAlgoritmo);
	}
	/**
		Asigna qué parametros son visibles durante la animación
		
		@param 
	*/	
	public void setVisibilidad(DatosTrazaBasicos dtb)
	{
		raiz.setVisibilidad(dtb);
	}	
	
	public void visualizarEntradaYSalida()
	{
		this.raiz.borrarCaminoActual();
		raiz.visualizarEntradaYSalida();
		//raiz.actualizarNodoActualEYS();
		this.raiz.crearCaminoActual();
	}
	
	public void visualizarSoloEntrada()
	{
		this.raiz.borrarCaminoActual();
		raiz.visualizarSoloEntrada();
		//raiz.actualizarNodoActualES();
		this.raiz.crearCaminoActual();
	}
	
	public void visualizarSoloSalida()
	{
		this.raiz.borrarCaminoActual();
		raiz.visualizarSoloSalida();
		//raiz.actualizarNodoActualES();
		this.raiz.crearCaminoActual();
	}
	
	public RegistroActivacion getRegistroActivacionPorID(int id)
	{
		return this.raiz.getRegistroActivacionPorID(id);
	}
	
	public RegistroActivacion getRegistroActivo()
	{
		return this.raiz.getRegistroActivo();
	}
	
	
	
	public void actualizarEstadoTrazaCompleta(Traza traza)
	{
		this.raiz.actualizarEstadoFlagsDesdeGemelo(traza);
		this.raiz.asignarMetodosNoVisibles();
	}
	
	/*public void recalcularFlagsSinGemelos()
	{
		this.raiz.recalcularFlagsSinGemelos();
	}*/
	
	public void hacerCoherente()
	{
		this.raiz.hacerCoherente();
	}
		
	// Asigna a cada registro de activación un número en función del método al que pertenece
	// El método principal tendrá un 0 en sus nodos, ...
	public void asignarNumeroMetodo()
	{
		this.raiz.asignarNumeroMetodo( this.getInterfacesMetodos() );
	}
	
	// Asigna a cada registro de activación un número en función del método al que pertenece
	// El método principal tendrá un 0 en sus nodos, ...
	public void asignarNumeroMetodo(String []interfaces)
	{
		this.raiz.asignarNumeroMetodo(interfaces);
	}	
	
	
	
	
	// Este método presupone que traza y this son iguales en cuanto a datos contenidos (entradas, salidas, métodos, ...)
	/*public void hacerGemelas(Traza traza)
	{
		System.out.println("  .hacerGemelas.  ");
		this.getRaiz().hacerGemelo(traza.getRaiz());
	}*/
}