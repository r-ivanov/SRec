/**
	Celda contenedora (que no se visualiza) que contiene un subárbol sobre el cual permite realizar acciones de manera sencilla
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/
package grafica;




import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;




import javax.swing.BorderFactory;

import org.jgraph.JGraph;
import org.jgraph.graph.*;

import paneles.PanelArbol;



import conf.*;
import datos.*;
import utilidades.*;




/*
 * 
 * 
 */












public class ContenedorArbol //extends DefaultGraphCell
{
	static boolean depurar=false;
	
	RegistroActivacion ra;
	JGraph graph;
	
	DefaultGraphCell entrada;
	DefaultGraphCell salida;
	
	DefaultGraphCell marcoEntrada;
	DefaultGraphCell marcoSalida;
	
	DefaultGraphCell celdasEstr[];	//NUEVO
	
	
	DefaultPort portEntrada;
	DefaultPort portSalida;
	//DefaultPort port;
	DefaultGraphCell celdaFantasma;
	
	boolean mostrarNombreMetodo=false;
		
	ContenedorArbol contenedoresHijos[];
	DefaultEdge edges[];
	
	NombresYPrefijos nyp;
	
	ParentMap parentMap[];
	boolean tieneHijos=false;
	
	float anchoPixelCaracter=(float)11.0;
	
	
	int minimoIzquierda=-1;	// valor mínimo que un nodo y sus subnodos pueden emplear horizontalmente, sirve para que no se monten nodos
							// cuando un padre es de mayor ancho que hijo(s)
	
	
	int nivel=0;
	
	//private static NivelGrafo objetoNivel=null;
	
	
	//NUEVO:
	private static final int alturaCelda=26;
	private static final int alturaCeldaEstr=18;
	static int anchoCeldaEstr=-1;		// Ancho para celdas de la estructura que se ve en cada nodo, depende de longitud máxima de dato
	
	static int altoEstructura=-1;	// Si hay varias estructuras, guarda la altitud máxima de todas ellas (arrays = 1)
	static int anchoEstructura=-1;

	static int altoEstructuramMin=4;	// Si hay varias estructuras, guarda la altitud máxima de todas ellas (arrays = 1)
	
	
	static int altoDeNivel=0;

	static int espacioInicial=20;	// Entre margen superior e izquierdo del grafo y primer nivel, por seguridad
	
	static int maximoAnchoUsado=espacioInicial;
	static int maximoAltoUsado=espacioInicial;
	
	static int[] dimEstr=null;
	
	static int tamFuente=20;
	
	static int maximaLongitudContenidoCelda=-1;
	static int maximaLongitudContenidoCeldaEstr=-1;
	
	
	// Valores para cada nivel
	private static NivelGrafo objetoNivel=null;		// Determina la posición horizontal (anchura) donde se tiene que dibujar siguiente celda
	//private static int alturas[]=null;				// Dice el número de celdas de altura que tiene la estructura de un nivel
	//private static int inicioCelda[]=null;			// Posición vertical donde irá la celda de entrada
	//private static int inicioEstr[]=null;			// Posición vertical donde irá la primera celda de la estructura dentro de un nivel
	
	
	public ContenedorArbol(RegistroActivacion ra,JGraph graph,  NombresYPrefijos nyp_, int nivel_)
	{
		
		this.ra=ra;
		this.graph=graph;
		nyp=nyp_;

		nivel=nivel_;
		
		if (nivel==1)	// necesario hacerlo antes de la asignación a "minimoIzquierda"
			maximoAnchoUsado=espacioInicial;
		
		if (minimoIzquierda==-1)
			minimoIzquierda=maximoAnchoUsado;
		
		if (nivel==1)
		{
			maximoAltoUsado=espacioInicial;
			
			ContenedorArbol.objetoNivel=null;
			altoEstructura=ra.getMaxAlturaEstructura();
			
			altoDeNivel=Math.max( altoEstructuramMin , altoEstructura )*alturaCeldaEstr;
					
			altoEstructura=Math.max( altoEstructuramMin , altoEstructura );
			
			anchoCeldaEstr=(int)((maximaLongitudContenidoEstructura()+1)*(anchoPixelCaracter));
			
			//ra.verArbol("arbolTrazasGeneradayCargada.txt");
		}
		if (ContenedorArbol.objetoNivel==null)
			objetoNivel=new NivelGrafo();
			
		
		
		
		// ***************************************************
		// FASE DE CREACION DE CELDAS (no ubicamos)
		// ***************************************************
		
		
		generacionCelda();	// genera entrada y salida, y sus marcos (inicialmente transparentes)
		generacionEstr();	// genera celdas estructura, si procede

		int numHijos=ra.numHijos();
		// Recursivamente, hay que crear celdas contenedores de hijos
		contenedoresHijos=new ContenedorArbol[(Conf.mostrarArbolColapsado ?  ra.getHijosVisiblesPantalla() : numHijos)];
		
		if (Conf.mostrarArbolColapsado)	// si tenemos que mostrar los árboles colapsados, sin dejar hueco para nodos futuros, aún no visibles
		{
			if (Conf.historia!=2)	// Si tenemos que mostrar todos los nodos (con históricos)
			{
				int x=0;
				/*for (int i=0; i<numHijos; i++)
				{
					if (x<contenedoresHijos.length && !ra.getHijo(i).vacioVisiblePantalla())
					{
						System.out.println("CREAMOS UN NODO (x="+x+") DE 'contenedoresHijos'");
						contenedoresHijos[x]=new ContenedorArbol(ra.getHijo(i),graph,nyp, nivel+1);
						x++;
						this.tieneHijos=true;
					}
				}*/
				
				for (int i=0; i<contenedoresHijos.length; i++)
				{
					contenedoresHijos[i]=new ContenedorArbol(ra.getHijo(i),graph,nyp, nivel+1);
					this.tieneHijos=true;
					
				}
				
				
				
				
			}
			else		// Si no tenemos que mostrar todos los nodos (quitamos históricos)
			{
				int x=0;
				for (int i=0; i<numHijos; i++)
				{
					if (!ra.getHijo(i).esHistorico() && !ra.getHijo(i).vacioVisible())
					{
						contenedoresHijos[x]=new ContenedorArbol(ra.getHijo(i),graph,nyp, nivel+1);
						x++;
						this.tieneHijos=true;
					}
				}
			}
		}
		else	// si mostramos los árboles de manera amplia, dejando hueco para nodos futuros, aún no visibles
			for (int i=0; i<numHijos; i++)
			{
				contenedoresHijos[i]=new ContenedorArbol(ra.getHijo(i),graph,nyp, nivel+1);
				this.tieneHijos=true;
			}

		
		// ***************************************************
		// FASE DE UBICACION DE NODOS (ubicamos)
		// ***************************************************
		
		if (nivel==1)	// Hemos creado todo el árbol ya, pero no hemos ubicado nada
		{
			ubicacionNodo();
		}
		
		
		//ubicacionCelda();
		
		generacionBordes();
		if ( (Conf.mostrarArbolColapsado ?  ra.getHijosVisiblesPantalla() : ra.numHijos()) > 0 )
		{
			/*System.out.println("Conf.mostrarArbolColapsado="+Conf.mostrarArbolColapsado+
					"   ra.getHijosVisiblesPantalla()="+ra.getHijosVisiblesPantalla()+
					"   ra.numHijos()="+ra.numHijos());*/
			generacionFlechas();
		}
		
		// Revisar si estas lineas son necesarias, si no, se quitan
		/*graph.getGraphLayoutCache().toBack(edges);
		Object es[]=new Object[2];
		es[0]=this.entrada;
		es[1]=this.salida;
		graph.getGraphLayoutCache().toFront(es);
		graph.getGraphLayoutCache().setVisible(contenedoresHijos,edges);*/
		
	
		// Dibujamos celda fantasma a la derecha para que el grafo no se mueva cuando el marco del nodo actual esté a la derecha
		// (eso hace que el ancho del grafo aumente y se mueva todo el grafo, produciendo un efecto incómodo)
		if (nivel==1)
		{
			celdaFantasma= new DefaultGraphCell(new String(""));
			GraphConstants.setOpaque(        this.celdaFantasma.getAttributes(), false);
			
			//GraphConstants.setForeground(    this.celdaFantasma.getAttributes(), Conf.colorPanel);
			//GraphConstants.setBackground(    this.celdaFantasma.getAttributes(), new Color(255,0,0));
			//GraphConstants.setGradientColor( this.celdaFantasma.getAttributes(), new Color(0,0,0)); 
			GraphConstants.setBounds(this.celdaFantasma.getAttributes(),
									new Rectangle(	this.maximoAncho()-40,	0,	80,	80));
			GraphConstants.setMoveable(      this.celdaFantasma.getAttributes(), false);
			GraphConstants.setSelectable(    this.celdaFantasma.getAttributes(), false);
			
		}	
	}
	
	// Se usa cuando u árbol tiene factor de ramificación=1 y celdas superiores son más anchas que celdas inferiores
	private void posicionarALaDerecha(int numPixeles)
	{
		posicionarALaDerecha(numPixeles,entrada     );
		posicionarALaDerecha(numPixeles,salida      );
		if (marcoEntrada!=null)
			posicionarALaDerecha(numPixeles,marcoEntrada);
		if (marcoSalida!=null)
			posicionarALaDerecha(numPixeles,marcoSalida );
		
		if (celdasEstr!=null)
			for (int i=0; i<celdasEstr.length; i++)
				posicionarALaDerecha(numPixeles,celdasEstr[i] );
		
		
		if (tieneHijos)
			for (int i=0; i<contenedoresHijos.length; i++)
				contenedoresHijos[i].posicionarALaDerecha(numPixeles);

	}
	
	private void posicionarALaDerecha(int numPixeles, DefaultGraphCell dgc)
	{
		Rectangle2D r=GraphConstants.getBounds(dgc.getAttributes());
		if (r!=null)
		{
			int posX=(int)r.getX();
			int posY=(int)r.getY();
			int dimX=(int)r.getWidth();
			int dimY=(int)r.getHeight();
			
			GraphConstants.setBounds(dgc.getAttributes(),
										new Rectangle(	posX+numPixeles,	posY,	dimX,	dimY));
		}
	}
	
	
	
	
	
	public void generacionEstr()
	{
		if (ra.esDYV() && Conf.mostrarEstructuraEnArbol)
		{
			int indices[]=ra.getEntrada().getIndices();
			
			Estructura eAux=new Estructura(ra.getEntrada().getEstructura());
			if (dimEstr==null)
				dimEstr=eAux.dimensiones();
			
			if (indices.length==0)
			{
				dimEstr=eAux.dimensiones();
				if (eAux.esMatriz())
				{
					indices=new int[4];
					indices[0]=0;
					indices[1]=dimEstr[0];
					indices[2]=0;
					indices[3]=dimEstr[1];
					//indices={ 0,ra.getSalida().getEstructura().dimensiones()[0], 0 , ra.getSalida().getEstructura().dimensiones()[1]};
				}
				else
				{
					indices=new int[2];
					indices[0]=0;
					indices[1]=dimEstr[0];
					//indices={ 0,ra.getSalida().getEstructura().dimensiones()[0]};
				}
			}
			
			if (anchoEstructura==-1)
				anchoEstructura=eAux.dimensiones()[0];
			

			if (ra.salidaVisible() && (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))
			{
				celdasEstr=extraerCeldasEstructura(
						new Estructura(ra.getSalida().getEstructura()),indices,"salida",nivel,
								(Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar) &&
								this.ra.salidaVisible() && ( 	(!this.ra.esHistorico() ||
								(this.ra.esHistorico() && Conf.historia!=2) ) &&
								(!this.ra.inhibido() || (this.ra.inhibido() && Conf.mostrarArbolSalto)) ));
										
				//objetoNivel.setNivel(nivel,objetoNivel.getNivelExacto(nivel)+(int)(GraphConstants.getBounds(celdasEstr[celdasEstr.length-1].getAttributes()).getMaxX()-espacioInicial));
			}
			else 
			{
				celdasEstr=extraerCeldasEstructura(
						new Estructura(ra.getEntrada().getEstructura()),indices,"entrada",nivel,
								(Conf.VISUALIZAR_ENTRADA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar) &&
								this.ra.entradaVisible() && 
								( 	(!this.ra.esHistorico() || (this.ra.esHistorico() && Conf.historia!=2) ) &&
									(!this.ra.inhibido() ||	(this.ra.inhibido() && Conf.mostrarArbolSalto)) ));

				//objetoNivel.setNivel(nivel,objetoNivel.getNivelExacto(nivel)+(int)(GraphConstants.getBounds(celdasEstr[celdasEstr.length-1].getAttributes()).getMaxX()-espacioInicial));
			}
		}
	}
	
	
	
	public void generacionCelda()
	{

		String repEntrada=ra.getEntrada().getRepresentacion();
		
		// Entrada, configuración de la misma y asignación de puerto
		String cadenaEntrada="";
		if(Conf.idMetodoTraza) {
			cadenaEntrada = ra.getNombreMetodo()+": ";
			if (nyp!=null)
				cadenaEntrada=nyp.getPrefijo(ra.getNombreMetodo())+": ";
		}
		cadenaEntrada=cadenaEntrada+repEntrada;
		
		if (repEntrada.length() <3)
			cadenaEntrada="  "+cadenaEntrada+"  ";
		
		this.entrada = new DefaultGraphCell(new String(cadenaEntrada));
			
		asignarColoresEntrada(cadenaEntrada);
		
		GraphConstants.setFont(          this.entrada.getAttributes(), new Font("Arial",Font.BOLD,tamFuente));
		GraphConstants.setDisconnectable(this.entrada.getAttributes(), false);
		GraphConstants.setMoveable(      this.entrada.getAttributes(), false);
		GraphConstants.setSelectable(    this.entrada.getAttributes(), false);
		this.portEntrada = new DefaultPort();
		this.entrada.add(portEntrada);
		
		// Salida, configuración de la misma y asignación de puerto
		String cadenaSalida=generaCadenaSalida();

		this.salida = new DefaultGraphCell(new String(cadenaSalida));

		asignarColoresSalida(cadenaSalida);
		
		GraphConstants.setFont(          this.salida.getAttributes(), new Font("Arial",Font.BOLD,tamFuente));
		GraphConstants.setDisconnectable(this.salida.getAttributes(), false);
		GraphConstants.setMoveable(      this.salida.getAttributes(), false);
		GraphConstants.setSelectable(    this.salida.getAttributes(), false);

		
		portSalida = new DefaultPort();
		salida.add(portSalida);
		
		// Tamaños 
		int maximaCadena=Math.max(cadenaEntrada.length(), cadenaSalida.length());
		GraphConstants.setSize(          this.entrada.getAttributes(), new Dimension((int)anchoPixelCaracter*maximaCadena,alturaCelda));
		GraphConstants.setSize(          this.salida.getAttributes(), new Dimension((int)anchoPixelCaracter*maximaCadena,alturaCelda));

		
		// Marco Entrada
		this.marcoEntrada = new DefaultGraphCell(" ");
		GraphConstants.setFont(          this.marcoEntrada.getAttributes(), new Font("Arial",Font.BOLD,tamFuente));
		GraphConstants.setDisconnectable(this.marcoEntrada.getAttributes(), false);
		GraphConstants.setMoveable(      this.marcoEntrada.getAttributes(), false);
		GraphConstants.setSelectable(    this.marcoEntrada.getAttributes(), false);
		GraphConstants.setOpaque(        this.marcoEntrada.getAttributes(), false);		
		GraphConstants.setSize(          this.marcoEntrada.getAttributes(),
					new Dimension(((int)anchoPixelCaracter*maximaCadena)+Conf.grosorMarco,alturaCelda+Conf.grosorMarco));
		
		
		// Marco Salida
		this.marcoSalida = new DefaultGraphCell(" ");
		GraphConstants.setFont(          this.marcoSalida.getAttributes(), new Font("Arial",Font.BOLD,tamFuente));
		GraphConstants.setDisconnectable(this.marcoSalida.getAttributes(), false);
		GraphConstants.setMoveable(      this.marcoSalida.getAttributes(), false);
		GraphConstants.setSelectable(    this.marcoSalida.getAttributes(), false);
		GraphConstants.setOpaque(        this.marcoSalida.getAttributes(), false);
		GraphConstants.setSize(          this.marcoSalida.getAttributes(),
					new Dimension(((int)anchoPixelCaracter*maximaCadena)+Conf.grosorMarco,alturaCelda+Conf.grosorMarco));
		
		GraphConstants.setSize(          this.marcoEntrada.getAttributes(), new Dimension((int)anchoPixelCaracter*maximaCadena,alturaCelda));
		GraphConstants.setSize(          this.marcoSalida.getAttributes(), new Dimension((int)anchoPixelCaracter*maximaCadena,alturaCelda));
	}
	
	
	
	
	public void ubicacionNodo()	// Posicionamos celdas de E y S y de estructura
	{
		
		Estructura e;
		int dimensiones[]=null;
		if (ra.esDYV() && Conf.mostrarEstructuraEnArbol)
		{
			if (ra.salidaVisible() && (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))
				e=new Estructura(ra.getSalida().getEstructura());
			else
				e=new Estructura(ra.getEntrada().getEstructura());
			dimensiones=e.dimensiones();
		}
		int ubicacion[]=new int[4];
		
		if ((Conf.mostrarArbolColapsado && ra.getHijosVisiblesPantalla()>0) || (!Conf.mostrarArbolColapsado && ra.numHijos()>0))
		{
			for (int i=0; i<contenedoresHijos.length; i++)
				contenedoresHijos[i].ubicacionNodo();
			
		
			// ubicamos en consecuencia de las posiciones de los hijos ...
			int ladoIzquierdo=contenedoresHijos[0].posicLadoIzquierdo();
			int ladoDerecho=contenedoresHijos[contenedoresHijos.length-1].posicLadoDerecho();
			int puntoMedio=(ladoDerecho+ladoIzquierdo)/2;
			
			if (ra.esDYV() && Conf.mostrarEstructuraEnArbol)
			{
				int anchoNodo=(int)(GraphConstants.getSize(entrada.getAttributes()).getWidth());
				anchoNodo=anchoNodo+(anchoCeldaEstr*dimensiones[0]);
				
				int posicAnchoInicial=puntoMedio-(anchoNodo/2);
				
				System.out.println("ESTOY EN NIVEL "+nivel+", (1)posicAnchoInicial="+posicAnchoInicial+" | "+objetoNivel.getNivelExacto(nivel));
				
				if ( objetoNivel.getNivelExacto(nivel) > posicAnchoInicial )
					posicAnchoInicial = objetoNivel.getNivelExacto(nivel) + Conf.sepH + anchoCeldaEstr*dimensiones[0] + espacioInicial;
				
				ubicacionNodo_EstrSiHijos(dimensiones,posicAnchoInicial);
				
				maximoAltoUsado=Math.max(maximoAltoUsado, (int)GraphConstants.getBounds(celdasEstr[celdasEstr.length-1].getAttributes()).getMaxY());
				
				if (altoEstructura>2)// Si estructura es más alta que celdas E y S
				{
					ubicacion[3]=posic0Nivel()+((altoEstructura/2)*alturaCeldaEstr);
					ubicacion[1]=ubicacion[3]-alturaCelda;
				}
				else
				{
					ubicacion[1]=posic0Nivel();
					ubicacion[3]=ubicacion[1]+alturaCelda;
				}
				ubicacion[0]=posicAnchoInicial+(dimensiones[0]*anchoCeldaEstr);
				ubicacion[2]=ubicacion[0];
				
				
				
				ubicacionNodo_posicionarCeldasx4(ubicacion);
				
				if (minimoIzquierda>posicAnchoInicial)	// Si hemos ubicado nodos demasiado a la izquierda (padres mas anchos que hijos)
				{
					posicionarALaDerecha(minimoIzquierda-posicAnchoInicial);
				}
				maximoAltoUsado=Math.max(maximoAltoUsado, (int)GraphConstants.getBounds(salida.getAttributes()).getMaxY());
				maximoAnchoUsado=Math.max(maximoAnchoUsado, ubicacion[2]+(int)(GraphConstants.getSize(salida.getAttributes()).getWidth() ));
			}
			else
			{
				if (Conf.mostrarEstructuraEnArbol && altoEstructura>2)	// Si estructura es más alta que celdas E y S
				{
					ubicacion[3]=posic0Nivel()+((altoEstructura/2)*alturaCeldaEstr);
					ubicacion[1]=ubicacion[3]-alturaCelda;
				}
				else
				{
					ubicacion[1]=posic0Nivel()+(alturaCeldaEstr*2)-alturaCelda;
					ubicacion[3]=posic0Nivel()+(alturaCeldaEstr*2);
				}
				
				int posicAnchoInicial=puntoMedio-( (int)(GraphConstants.getSize(entrada.getAttributes()).getWidth()) /2);
				
				if ( objetoNivel.getNivelExacto(nivel) > posicAnchoInicial )
					posicAnchoInicial = objetoNivel.getNivelExacto(nivel) + Conf.sepH + espacioInicial;
				
				ubicacion[0]=posicAnchoInicial;
				ubicacion[2]=posicAnchoInicial;
				
				ubicacionNodo_posicionarCeldasx4(ubicacion);
				
				if (minimoIzquierda>posicAnchoInicial)	// Si hemos ubicado nodos demasiado a la izquierda (padres mas anchos que hijos)
				{
					posicionarALaDerecha(minimoIzquierda-posicAnchoInicial);
				}

				maximoAltoUsado=Math.max(maximoAltoUsado, (int)GraphConstants.getBounds(salida.getAttributes()).getMaxY());
				maximoAnchoUsado=Math.max(maximoAnchoUsado, ubicacion[2]+(int)(GraphConstants.getSize(salida.getAttributes()).getWidth() ));

			}
			
		}
		else	// Nodo sin hijos....
		{
			// Si las hay, situamos en el grafo las celdas de la estructura
			if (ra.esDYV() && Conf.mostrarEstructuraEnArbol)
			{
				ubicacionNodo_EstrNoHijos(dimensiones);

				maximoAltoUsado=Math.max(maximoAltoUsado, (int)GraphConstants.getBounds(celdasEstr[celdasEstr.length-1].getAttributes()).getMaxY());
				
				
				// Situamos las celdas E y S en función de las celdas de estructura
				
				if (altoEstructura>2)// Si estructura es más alta que celdas E y S
				{
					ubicacion[3]=posic0Nivel()+((altoEstructura/2)*alturaCeldaEstr);
					ubicacion[1]=ubicacion[3]-alturaCelda;
				}
				else
				{
					ubicacion[1]=posic0Nivel();
					ubicacion[3]=ubicacion[1]+alturaCelda;
				}
								
				ubicacion[0]=maximoAnchoUsado+Conf.sepH+(dimensiones[0]*anchoCeldaEstr); 
				ubicacion[2]=ubicacion[0];
				
				ubicacionNodo_posicionarCeldasx4(ubicacion);

				maximoAltoUsado=Math.max(maximoAltoUsado, (int)GraphConstants.getBounds(salida.getAttributes()).getMaxY());
				maximoAnchoUsado=Math.max(maximoAnchoUsado, ubicacion[2]+(int)(GraphConstants.getSize(salida.getAttributes()).getWidth() ));
			
			}
			else
			{
				if (altoEstructura>2)	// Si estructura es más alta que celdas E y S
				{
					ubicacion[3]=posic0Nivel()+((altoEstructura/2)*alturaCeldaEstr);
					ubicacion[1]=ubicacion[3]-alturaCelda;
				}
				else
				{
					ubicacion[1]=posic0Nivel()+(alturaCeldaEstr*2)-alturaCelda;
					ubicacion[3]=posic0Nivel()+(alturaCeldaEstr*2);
				}
				System.out.println("UBICACION NODO SIN HIJOS (no Estructura), maximoAnchoUsado="+maximoAnchoUsado);
				ubicacion[0]=maximoAnchoUsado+Conf.sepH;
				ubicacion[2]=ubicacion[0];
				
				ubicacionNodo_posicionarCeldasx4(ubicacion);
				
				maximoAltoUsado=Math.max(maximoAltoUsado, (int)GraphConstants.getBounds(salida.getAttributes()).getMaxY());
				maximoAnchoUsado=Math.max(maximoAnchoUsado, ubicacion[2]+(int)(GraphConstants.getSize(salida.getAttributes()).getWidth() ));
			}

		}
		// Después de ubicar la celda, guardamos el ancho máximo empleado en el nivel correspondiente.
		// La información de objetoNivel sirve, por ejemplo, para evitar que los nodos se superpongan cuando tienen estrcutura e hijos estrechos. 
		objetoNivel.setNivel(nivel, ubicacion[0]+ (int)(GraphConstants.getSize(entrada.getAttributes()).getWidth()) );
		objetoNivel.mostrar();
	}
	
	
	
	public void ubicacionNodo_posicionarCeldasx4(int[] ubicacion)
	{
		if(ra.getEsNodoActual()) {
			PanelArbol.setMinX(ubicacion[0]);
			PanelArbol.setMinY(ubicacion[1]);
		}
		
		GraphConstants.setBounds(this.entrada.getAttributes(),
				new Rectangle(	ubicacion[0],
								ubicacion[1],
								(int)(GraphConstants.getSize(entrada.getAttributes()).getWidth()),
								(int)(GraphConstants.getSize(entrada.getAttributes()).getHeight() )));
		GraphConstants.setBounds(this.salida.getAttributes(),
				new Rectangle(	ubicacion[2],
								ubicacion[3],
								(int)(GraphConstants.getSize(salida.getAttributes()).getWidth()),
								(int)(GraphConstants.getSize(salida.getAttributes()).getHeight() )));
		
		GraphConstants.setBounds(this.marcoEntrada.getAttributes(),
				new Rectangle(	ubicacion[0]-Conf.grosorMarco,
								ubicacion[1]-Conf.grosorMarco,
								(int)(GraphConstants.getSize(entrada.getAttributes()).getWidth())+Conf.grosorMarco*2,
								(int)(GraphConstants.getSize(entrada.getAttributes()).getHeight() )+Conf.grosorMarco*2));
		GraphConstants.setBounds(this.marcoSalida.getAttributes(),
				new Rectangle(	ubicacion[2]-Conf.grosorMarco,
								ubicacion[3]-Conf.grosorMarco,
								(int)(GraphConstants.getSize(salida.getAttributes()).getWidth())+Conf.grosorMarco*2,
								(int)(GraphConstants.getSize(salida.getAttributes()).getHeight() )+Conf.grosorMarco*2));
	}
	
	
	public void ubicacionNodo_EstrSiHijos(int[] dimensiones, int posicAnchoInicial )
	{
		if (dimensiones.length==2)
			for (int i=0; i<dimensiones[0]; i++)
				for (int j=0; j<dimensiones[1]; j++)
				{
					GraphConstants.setBounds(		celdasEstr[(dimensiones[0] * i) + j].getAttributes(),
									new Rectangle(	posicAnchoInicial+(j*anchoCeldaEstr),
													posic0Nivel() + (alturaCeldaEstr*(i+(altoEstructura-dimensiones[1])/2)),
													anchoCeldaEstr,
													alturaCeldaEstr));

				}
		else
			for (int i=0; i<dimensiones[0]; i++)
			{
				GraphConstants.setBounds(		celdasEstr[i].getAttributes(),
								new Rectangle(	posicAnchoInicial+(i*anchoCeldaEstr),
												posic0Nivel() + ((int)(alturaCeldaEstr*1.5)),//posic0Nivel() + (((altoEstructura-1)/2) + (alturaCeldaEstr)),
												anchoCeldaEstr,
												alturaCeldaEstr));	
			}
	}
	
	public void ubicacionNodo_EstrNoHijos(int[] dimensiones)
	{
		if (dimensiones.length==2)
			for (int i=0; i<dimensiones[0]; i++)
				for (int j=0; j<dimensiones[1]; j++)
				{
					GraphConstants.setBounds(		celdasEstr[(dimensiones[0] * i) + j].getAttributes(),
									new Rectangle(	maximoAnchoUsado+Conf.sepH+(j*anchoCeldaEstr),
													posic0Nivel() + (alturaCeldaEstr*(i+(altoEstructura-dimensiones[1])/2)),
													//posic0Nivel() + (((altoEstructura-dimensiones[1])/2) + (alturaCeldaEstr*i)),
													anchoCeldaEstr,//(int)(GraphConstants.getSize(celdas[0].getAttributes()).getWidth()),
													alturaCeldaEstr));//(int)(GraphConstants.getSize(celdas[0].getAttributes()).getHeight() )));

				}
		else
			for (int i=0; i<dimensiones[0]; i++)
			{
				GraphConstants.setBounds(		celdasEstr[i].getAttributes(),
								new Rectangle(	maximoAnchoUsado+Conf.sepH+(i*anchoCeldaEstr),
												posic0Nivel() + ((int)(alturaCeldaEstr*1.5)),//posic0Nivel() + (((altoEstructura-1)/2) + (alturaCeldaEstr)),
												anchoCeldaEstr,
												alturaCeldaEstr));
			}
	}
	
	
	
	
	

	public boolean entradaVisible()
	{
		return ra.entradaVisible();
	}
	
	public boolean salidaVisible()
	{
		return ra.salidaVisible();
	}
	
	public boolean esHistorico()
	{
		return ra.esHistorico();
	}
	
	public boolean inhibido()
	{
		return ra.inhibido();
	}
		
	// Devuelve la cabeza visible de la ContenedorArbol: bien la celda de entrada, bien la de salida. 
	public Object getCabezaVisibleParaPadre()
	{
		if (Conf.VISUALIZAR_ENTRADA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar)
			return this.entrada;
		else
			return this.salida;
	}
	
	// Devuelve la cabeza visible de la ContenedorArbol: bien la celda de entrada, bien la de salida. 
	public Object getCabezaVisibleParaHijo()
	{
		if (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar)
			return this.salida;
		else
			return this.entrada;
	}
	
	
	// Devuelve el puerto visible de la ContenedorArbol: bien el de la celda de entrada, bien el de la de salida. 
	public Object getPuertoVisibleParaPadre()
	{
		try {
			return ((DefaultGraphCell)getCabezaVisibleParaPadre()).getChildAt(0);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Object getPuertoVisibleParaHijo()
	{
		return ((DefaultGraphCell)getCabezaVisibleParaHijo()).getChildAt(0);
	}	
	

	
	
	public ParentMap[] getMapasParientes()
	{
		return this.parentMap;
	}
	

	
	public boolean tieneHijos()
	{
		return this.tieneHijos;
	}
	

	
	
	private int posicLadoIzquierdo()
	{
		if (ra.esDYV() && Conf.mostrarEstructuraEnArbol)
			return (int)GraphConstants.getBounds(celdasEstr[0].getAttributes()).getMinX();
		else
			return (int)GraphConstants.getBounds(entrada.getAttributes()).getMinX();
	}
	
	private int posicLadoDerecho()
	{
		return (int)GraphConstants.getBounds(entrada.getAttributes()).getMaxX();
	}
	
	public int maximoAncho()
	{
		return maximoAnchoUsado+25;//objetoNivel.getMaximoAncho()+25;
	}
	
	
	public int maximoAnchoVisible()
	{
		int ancho=0;
		int anchoHijos=0;
		
		if (this.entradaVisible() || this.salidaVisible())
		{
			ancho= (int)(GraphConstants.getBounds(entrada.getAttributes())).getMaxX();
			for (int i=0; i<this.contenedoresHijos.length; i++)
				anchoHijos=Math.max(anchoHijos, this.contenedoresHijos[i].maximoAnchoVisible());
		}
		
		return Math.max(ancho, anchoHijos);
	}
	
	public int maximoAlto()
	{
		return maximoAltoUsado;
	}
	
	public int maximoAltoVisible()
	{
		if ((!this.entradaVisible() && !this.salidaVisible()) ||
			(this.ra.inhibido() && !Conf.mostrarArbolSalto))
			return 0;
		
		if (contenedoresHijos.length>0)
		{
			int valorAltura=(int)(GraphConstants.getBounds(entrada.getAttributes()).getMinY())+
							(int)(GraphConstants.getBounds(entrada.getAttributes()).getHeight()*3);
			for (int i=0; i<contenedoresHijos.length; i++)
			{
				int valoraux=contenedoresHijos[i].maximoAltoVisible();
				if (valoraux>valorAltura)
					valorAltura=valoraux;
			}
			return valorAltura;
		}
		else
		{
			return (int)(GraphConstants.getBounds(entrada.getAttributes()).getMinY())+
					(int)(GraphConstants.getBounds(entrada.getAttributes()).getHeight()*3);
		}
	}
	
	public Object[] getCeldasEstado()
	{
		// Array para todos los hijos
		Object[] estados=new Object[2];
		estados[0]=this.entrada;
		estados[1]=this.salida;
		return estados;	
	}
	
	/**
		Método que realiza la actualización del grafo en cada paso de la visualización que da el usuario, evita redibujar todo el árbol
	*/
	public void actualizar()
	{

		String cadenaEntrada="  ";
		if (nyp!=null)
			cadenaEntrada=cadenaEntrada+nyp.getPrefijo(ra.getNombreMetodo())+": ";
		cadenaEntrada=cadenaEntrada+ra.getEntrada().getRepresentacion()+"  ";

		String cadenaSalida=generaCadenaSalida();
	
		asignarColoresEntrada(cadenaEntrada);
		asignarColoresSalida(cadenaSalida);
		
		if (this.celdaFantasma!=null)
		{
			GraphConstants.setForeground(    this.celdaFantasma.getAttributes(), Conf.colorPanel);
			GraphConstants.setBackground(    this.celdaFantasma.getAttributes(), Conf.colorPanel);
			GraphConstants.setGradientColor( this.celdaFantasma.getAttributes(), Conf.colorPanel); 
		}

		generacionBordes();
		if ( (Conf.mostrarArbolColapsado ?  ra.getHijosVisiblesPantalla() : ra.numHijos()) > 0 )
		{
			
			generacionFlechas();
		}
		
		for (int i=0; i<contenedoresHijos.length; i++)
			contenedoresHijos[i].actualizar();
	}
	
	
	
	
	private void asignarColoresEntrada(String cadenaEntrada)
	{
		
		AttributeMap amap= this.entrada.getAttributes();
		
		if ( (Conf.VISUALIZAR_ENTRADA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar) &&
			this.ra.entradaVisible() && 
			( 	(!this.ra.esHistorico() || (this.ra.esHistorico() && Conf.historia==0) ) &&
				(!this.ra.inhibido() || (this.ra.inhibido() && Conf.mostrarArbolSalto)) ) )
		{
			this.entrada = new DefaultGraphCell(new String(cadenaEntrada));
			this.entrada.setAttributes(amap);
			if (this.portEntrada!=null)
				this.entrada.add(this.portEntrada);
			GraphConstants.setOpaque(        this.entrada.getAttributes(), true);		// Normales
			GraphConstants.setForeground(    this.entrada.getAttributes(), Conf.colorFEntrada);
			if (this.ra.estaIluminado())
			{
				GraphConstants.setBackground(    this.entrada.getAttributes(), Conf.colorIluminado );
				GraphConstants.setGradientColor( this.entrada.getAttributes(), Conf.colorIluminado);
			}
			else if (this.ra.estaResaltado())
			{
				GraphConstants.setBackground(    this.entrada.getAttributes(), Conf.colorResaltado );
				GraphConstants.setGradientColor( this.entrada.getAttributes(), Conf.colorResaltado);
			}
			else
			{
				GraphConstants.setBackground(    this.entrada.getAttributes(), (Conf.modoColor==1 ? Conf.colorC1Entrada : Conf.coloresNodo[ra.getNumMetodo()%10]  ));
				GraphConstants.setGradientColor( this.entrada.getAttributes(), (Conf.modoColor==1 ? Conf.colorC2Entrada : Conf.coloresNodo2[ra.getNumMetodo()%10]  ));
			}
			
			marcoCelda(						 this.entrada.getAttributes(),false);
		}
		else if ( (Conf.VISUALIZAR_ENTRADA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar) &&
			 (Conf.historia==1 && this.ra.esHistorico()) &&
			 !(this.ra.inhibido() && !Conf.mostrarArbolSalto) )
		{
			this.entrada = new DefaultGraphCell(new String(cadenaEntrada));
			this.entrada.setAttributes(amap);
			if (this.portEntrada!=null)
				this.entrada.add(this.portEntrada);
			GraphConstants.setOpaque(        this.entrada.getAttributes(), true);		// Atenuados
			GraphConstants.setForeground(    this.entrada.getAttributes(), Conf.colorFEntrada);
			if (this.ra.estaIluminado())
			{
				GraphConstants.setBackground(    this.entrada.getAttributes(), Conf.colorIluminado );
				GraphConstants.setGradientColor( this.entrada.getAttributes(), Conf.colorIluminado);
			}
			else if (this.ra.estaResaltado())
			{
				GraphConstants.setBackground(    this.entrada.getAttributes(), Conf.colorResaltado );
				GraphConstants.setGradientColor( this.entrada.getAttributes(), Conf.colorResaltado);
			}
			else
			{
				GraphConstants.setBackground(    this.entrada.getAttributes(), (Conf.modoColor==1 ? Conf.colorC1AEntrada : Conf.coloresNodoA[ra.getNumMetodo()%10]  ));
				GraphConstants.setGradientColor( this.entrada.getAttributes(), (Conf.modoColor==1 ? Conf.colorC2AEntrada : Conf.coloresNodoA2[ra.getNumMetodo()%10] ));
			}
			marcoCelda(						 this.entrada.getAttributes(),false);
		}
		else
		{
			int longitud=cadenaEntrada.length();
			cadenaEntrada=" ";
			for (int i=1; i<longitud; i++)
				cadenaEntrada=cadenaEntrada+" ";

			this.entrada = new DefaultGraphCell(new String(cadenaEntrada));
			this.entrada.setAttributes(amap);
			if (this.portEntrada!=null)
				this.entrada.add(this.portEntrada);
			GraphConstants.setOpaque(        this.entrada.getAttributes(), false);		// *1*
			GraphConstants.setBackground(    this.entrada.getAttributes(), Conf.colorPanel);
			GraphConstants.setGradientColor( this.entrada.getAttributes(), Conf.colorPanel);
			GraphConstants.setForeground(    this.entrada.getAttributes(), Conf.colorPanel);
			
			marcoCelda(						 this.entrada.getAttributes(), true);
		}
	}

	
	private void asignarColoresSalida(String cadenaSalida)
	{
		AttributeMap amap= this.salida.getAttributes();
		
		if ( (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar) &&
			this.ra.salidaVisible() && 
			( 	(!this.ra.esHistorico() ||
				(this.ra.esHistorico() && Conf.historia==0) ) &&
				(!this.ra.inhibido() ||
				(this.ra.inhibido() && Conf.mostrarArbolSalto)) ) )
		{
			
			if (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar && !this.ra.esHistorico() && !this.ra.esMostradoEntero())
			{
				int longitud=cadenaSalida.length();
				cadenaSalida=" ";
				//for (int i=1; i<longitud; i++)
				//	cadenaSalida=cadenaSalida+" ";
			}

			this.salida = new DefaultGraphCell(new String(cadenaSalida));
			this.salida.setAttributes(amap);
			if (this.portSalida!=null)
				this.salida.add(this.portSalida);
			GraphConstants.setOpaque(        this.salida.getAttributes(), true);		// Normales
			GraphConstants.setForeground(    this.salida.getAttributes(), Conf.colorFSalida);
			if (this.ra.estaIluminado())
			{
				GraphConstants.setBackground(    this.salida.getAttributes(), Conf.colorIluminado );
				GraphConstants.setGradientColor( this.salida.getAttributes(), Conf.colorIluminado);
			}
			else if (this.ra.estaResaltado())
			{
				GraphConstants.setBackground(    this.salida.getAttributes(), Conf.colorResaltado );
				GraphConstants.setGradientColor( this.salida.getAttributes(), Conf.colorResaltado);
			}
			else
			{
				GraphConstants.setBackground(    this.salida.getAttributes(), (Conf.modoColor==1 ? Conf.colorC1Salida : Conf.coloresNodo[ra.getNumMetodo()%10] ));
				GraphConstants.setGradientColor( this.salida.getAttributes(), (Conf.modoColor==1 ? Conf.colorC2Salida : Conf.coloresNodo2[ra.getNumMetodo()%10] ));	
			}
			marcoCelda(						 this.salida.getAttributes(),false);
		}
		else if ( (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar) &&
			 (Conf.historia==1 && this.ra.esHistorico()) &&
			 !(this.ra.inhibido() && !Conf.mostrarArbolSalto) )
		{
			this.salida = new DefaultGraphCell(new String(cadenaSalida));
			this.salida.setAttributes(amap);
			if (this.portSalida!=null)
				this.salida.add(this.portSalida);
			GraphConstants.setOpaque(        this.salida.getAttributes(), true);		// Atenuados	
			GraphConstants.setForeground(    this.salida.getAttributes(), Conf.colorFSalida);
			if (this.ra.estaIluminado())
			{
				GraphConstants.setBackground(    this.salida.getAttributes(), Conf.colorIluminado );
				GraphConstants.setGradientColor( this.salida.getAttributes(), Conf.colorIluminado);
			}
			else if (this.ra.estaResaltado())
			{
				GraphConstants.setBackground(    this.salida.getAttributes(), Conf.colorResaltado );
				GraphConstants.setGradientColor( this.salida.getAttributes(), Conf.colorResaltado);
			}
			else
			{
				GraphConstants.setBackground(    this.salida.getAttributes(), (Conf.modoColor==1 ? Conf.colorC1ASalida : Conf.coloresNodoA[ra.getNumMetodo()%10]  ));
				GraphConstants.setGradientColor( this.salida.getAttributes(), (Conf.modoColor==1 ? Conf.colorC2ASalida : Conf.coloresNodoA2[ra.getNumMetodo()%10] ));
			}
			marcoCelda(						 this.salida.getAttributes(),false);
		}
		else if (ra.entradaVisible() && !ra.esHistorico() &&
			(Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))	// Si no calculado
		{
			int longitud=cadenaSalida.length();
			cadenaSalida=" ";
			for (int i=1; i<longitud; i++)
				cadenaSalida=cadenaSalida+" ";
			
			this.salida = new DefaultGraphCell(new String(cadenaSalida));
			this.salida.setAttributes(amap);
			if (this.portSalida!=null)
				this.salida.add(this.portSalida);
			GraphConstants.setOpaque(        this.salida.getAttributes(), true);		// Salida no calculada
			GraphConstants.setForeground(    this.salida.getAttributes(), Conf.colorFSalida);
			if (this.ra.estaIluminado())
			{
				GraphConstants.setBackground(    this.salida.getAttributes(), Conf.colorIluminado );
				GraphConstants.setGradientColor( this.salida.getAttributes(), Conf.colorIluminado);
			}
			else if (this.ra.estaResaltado())
			{
				GraphConstants.setBackground(    this.salida.getAttributes(), Conf.colorResaltado );
				GraphConstants.setGradientColor( this.salida.getAttributes(), Conf.colorResaltado);
			}
			else
			{
				GraphConstants.setBackground(    this.salida.getAttributes(), (Conf.modoColor==1 ? Conf.colorC1NCSalida : Conf.coloresNodo[ra.getNumMetodo()%10]  ));
				GraphConstants.setGradientColor( this.salida.getAttributes(), (Conf.modoColor==1 ? Conf.colorC2NCSalida : Conf.coloresNodo2[ra.getNumMetodo()%10] ));
			}
			marcoCelda(						 this.salida.getAttributes(),false);
		}
		else
		{
			int longitud=cadenaSalida.length();
			cadenaSalida=" ";
			for (int i=1; i<longitud; i++)
				cadenaSalida=cadenaSalida+" ";

			this.salida = new DefaultGraphCell(new String(cadenaSalida));
			this.salida.setAttributes(amap);
			if (this.portSalida!=null)
				this.salida.add(this.portSalida);
			GraphConstants.setOpaque(        this.salida.getAttributes(), false);	// *1*
			GraphConstants.setBackground(    this.salida.getAttributes(), Conf.colorPanel);
			GraphConstants.setGradientColor( this.salida.getAttributes(), Conf.colorPanel);
			GraphConstants.setForeground(    this.salida.getAttributes(), Conf.colorPanel);
			marcoCelda(						 this.salida.getAttributes(),true);
		}
	}
	
	private void generacionBordes()
	{
		if (marcoEntrada!=null)
		{
			GraphConstants.setOpaque(        this.marcoEntrada.getAttributes(), false);
			GraphConstants.setBackground(    this.marcoEntrada.getAttributes(), Conf.colorPanel);
			GraphConstants.setGradientColor( this.marcoEntrada.getAttributes(), Conf.colorPanel);
			GraphConstants.setForeground(    this.marcoEntrada.getAttributes(), Conf.colorPanel);
		}
		if (marcoSalida!=null)
		{
			GraphConstants.setOpaque(        this.marcoSalida.getAttributes(), false);
			GraphConstants.setBackground(    this.marcoSalida.getAttributes(), Conf.colorPanel);
			GraphConstants.setGradientColor( this.marcoSalida.getAttributes(), Conf.colorPanel);
			GraphConstants.setForeground(    this.marcoSalida.getAttributes(), Conf.colorPanel);
		}
	
		// Generación de borde para nodo actual
		if (ra.getEsNodoActual())
		{
			//if (ra.entradaVisible())
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO || Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA)
			{
				this.marcoEntrada = new DefaultGraphCell("");
				//rect=GraphConstants.getBounds(this.entrada.getAttributes());
				
				/*GraphConstants.setBounds(this.marcoEntrada.getAttributes(),
									new Rectangle(	(int)rect.getMinX()-Conf.grosorMarco,
													(int)rect.getMinY()-Conf.grosorMarco,
													(int)(rect.getMaxX()-rect.getMinX()+(Conf.grosorMarco*2)),
													(int)(rect.getMaxY()-rect.getMinY()+(Conf.grosorMarco*2))));*/
				GraphConstants.setBackground(    this.marcoEntrada.getAttributes(), Conf.colorMarcoActual);
				GraphConstants.setOpaque(        this.marcoEntrada.getAttributes(), true);
				GraphConstants.setMoveable(      this.marcoEntrada.getAttributes(), false);
				GraphConstants.setSelectable(    this.marcoEntrada.getAttributes(), false);
			}
			
			//if (ra.salidaVisible())
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO || Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA)
			{
				this.marcoSalida = new DefaultGraphCell("");
				//rect=GraphConstants.getBounds(this.salida.getAttributes());
				
				/*GraphConstants.setBounds(this.marcoSalida.getAttributes(),
									new Rectangle(	(int)rect.getMinX()-Conf.grosorMarco,
													(int)rect.getMinY()-Conf.grosorMarco,
													(int)(rect.getMaxX()-rect.getMinX()+(Conf.grosorMarco*2)),
													(int)(rect.getMaxY()-rect.getMinY()+(Conf.grosorMarco*2))));*/
				GraphConstants.setBackground(    this.marcoSalida.getAttributes(), Conf.colorMarcoActual);
				GraphConstants.setOpaque(        this.marcoSalida.getAttributes(), true);
				GraphConstants.setMoveable(      this.marcoSalida.getAttributes(), false);
				GraphConstants.setSelectable(    this.marcoSalida.getAttributes(), false);
			}
		}
		// Generación de borde para camino actual
		else if (ra.getEsCaminoActual())
		{
			//Rectangle2D rect=null;
			
			//if (ra.entradaVisible())
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO || Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA)
			{
				this.marcoEntrada = new DefaultGraphCell("");
				//rect=GraphConstants.getBounds(this.entrada.getAttributes());
				
				/*GraphConstants.setBounds(this.marcoEntrada.getAttributes(),
									new Rectangle(	(int)rect.getMinX()-Conf.grosorMarco,
													(int)rect.getMinY()-Conf.grosorMarco,
													(int)(rect.getMaxX()-rect.getMinX()+(Conf.grosorMarco*2)),
													(int)(rect.getMaxY()-rect.getMinY()+(Conf.grosorMarco*2))));*/
				GraphConstants.setBackground(    this.marcoEntrada.getAttributes(), Conf.colorMarcosCActual);
				GraphConstants.setOpaque(        this.marcoEntrada.getAttributes(), true);
				GraphConstants.setMoveable(      this.marcoEntrada.getAttributes(), false);
				GraphConstants.setSelectable(    this.marcoEntrada.getAttributes(), false);
			}
			//if (ra.salidaVisible())
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO || Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA)
			{
				this.marcoSalida = new DefaultGraphCell("");
				//rect=GraphConstants.getBounds(this.salida.getAttributes());
				/*GraphConstants.setBounds(this.marcoSalida.getAttributes(),
									new Rectangle(	(int)rect.getMinX()-Conf.grosorMarco,
													(int)rect.getMinY()-Conf.grosorMarco,
													(int)(rect.getMaxX()-rect.getMinX()+(Conf.grosorMarco*2)),
													(int)(rect.getMaxY()-rect.getMinY()+(Conf.grosorMarco*2))));*/
				GraphConstants.setBackground(    this.marcoSalida.getAttributes(), Conf.colorMarcosCActual);
				GraphConstants.setOpaque(        this.marcoSalida.getAttributes(), true);
				GraphConstants.setMoveable(      this.marcoSalida.getAttributes(), false);
				GraphConstants.setSelectable(    this.marcoSalida.getAttributes(), false);
			}
		}
	}
	
	
	private void generacionFlechas()
	{
		// Flechas (edges, tantas como hijos)
		int numeroHijosRecorrer = (Conf.mostrarArbolColapsado ?  contenedoresHijos.length : ra.numHijos());
		edges=new DefaultEdge[numeroHijosRecorrer];
		for (int i=0; i<numeroHijosRecorrer; i++)
		{
			edges[i]=new DefaultEdge();
			
			edges[i].setSource( ((DefaultGraphCell)(this.getPuertoVisibleParaHijo())) );
			//System.out.println("i="+i+"  "+(contenedoresHijos[i]==null)+"  "+this.ra.getEntradaCompletaString());
			//System.out.println(" - this.ra.getHijosVisibles()="+this.ra.getHijosVisibles()+
			//		" this.ra.getHijosVisiblesPantalla()="+this.ra.getHijosVisiblesPantalla()+
			//		" this.ra.getNumHijosVisibles()="+this.ra.getNumHijosVisibles());
			
			try {
			
			edges[i].setTarget( ((DefaultGraphCell)(contenedoresHijos[i].getPuertoVisibleParaPadre())) );
			} catch (Exception e) {
				//System.out.println("----------------------------------EXCEPCION");
				e.printStackTrace();
			}
			int tipoFlecha=GraphConstants.ARROW_NONE;
			switch(Conf.formaFlecha)
			{
				case 0:
					tipoFlecha=GraphConstants.ARROW_NONE;
					break;
				case 1:
					tipoFlecha=GraphConstants.ARROW_TECHNICAL;
					break;
				case 2:
					tipoFlecha=GraphConstants.ARROW_CLASSIC;
					break;
				case 3:
					tipoFlecha=GraphConstants.ARROW_SIMPLE;
					break;
				case 4:
					tipoFlecha=GraphConstants.ARROW_DIAMOND;
					break;
				case 5:
					tipoFlecha=GraphConstants.ARROW_LINE;
					break;
				case 6:
					tipoFlecha=GraphConstants.ARROW_DOUBLELINE;
					break;
				case 7:
					tipoFlecha=GraphConstants.ARROW_CIRCLE;
					break;
			}
		
			GraphConstants.setLineEnd(edges[i].getAttributes(), tipoFlecha);
			GraphConstants.setEndFill(edges[i].getAttributes(), true);
			GraphConstants.setSelectable(edges[i].getAttributes(),false);
			
			if ( 	(contenedoresHijos[i].entradaVisible() || contenedoresHijos[i].salidaVisible()) &&
					!( Conf.historia==2 &&  contenedoresHijos[i].esHistorico() ) &&
					!( !Conf.mostrarArbolSalto && contenedoresHijos[i].inhibido() ) )
			{	
				GraphConstants.setLineWidth(edges[i].getAttributes(), Conf.grosorFlecha); // grosor de línea a  8 puntos
				GraphConstants.setLineColor(edges[i].getAttributes(), Conf.colorFlecha); // color de la línea
			}
			else//if (i>= (ra.getHijosVisiblesPantalla()))
			{
				GraphConstants.setLineWidth(edges[i].getAttributes(), Conf.grosorFlecha); // grosor de línea a  8 puntos
				GraphConstants.setLineColor(edges[i].getAttributes(), Conf.colorPanel);
			}
		}
		
	}
	
	
	public Object[] getCeldas()
	{
		if (GraphConstants.getBounds(this.marcoEntrada.getAttributes())==null)
		{
			Rectangle2D r=GraphConstants.getBounds(this.entrada.getAttributes());			
			GraphConstants.setBounds(this.marcoEntrada.getAttributes(),
						new Rectangle(
									(int)(r.getMinX()-Conf.grosorMarco),
									(int)(r.getMinY()-Conf.grosorMarco),
									(int)(r.getWidth()+Conf.grosorMarco*2),
									(int)(r.getHeight()+Conf.grosorMarco*2)) );
		}
		
		if (GraphConstants.getBounds(this.marcoSalida.getAttributes())==null)
		{
			Rectangle2D r=GraphConstants.getBounds(this.salida.getAttributes());
			GraphConstants.setBounds(this.marcoSalida.getAttributes(),
					new Rectangle(
								(int)(r.getMinX()-Conf.grosorMarco),
								(int)(r.getMinY()-Conf.grosorMarco),
								(int)(r.getWidth()+Conf.grosorMarco*2),
								(int)(r.getHeight()+Conf.grosorMarco*2)) );
		}
		
		
		
		Object celdasNodo[];
		celdasNodo=new Object[4];
		celdasNodo[0]=this.marcoEntrada;
		celdasNodo[1]=this.marcoSalida;
		celdasNodo[2]=this.entrada;
		celdasNodo[3]=this.salida;

		
		if (this.celdasEstr!=null)
			celdasNodo=concatenarArrays(celdasNodo,celdasEstr);

		if (this.tieneHijos())
		{
			Object celdasDeHijos[][]=new Object[contenedoresHijos.length][];
			
			for (int i=0; i<contenedoresHijos.length; i++)
				celdasDeHijos[i]=contenedoresHijos[i].getCeldas();
			
			Object flecha[]=new Object[1];
			
			for (int i=0; i<contenedoresHijos.length; i++)
			{
				flecha[0]=this.edges[i];
				
				celdasNodo=concatenarArrays(celdasNodo,celdasDeHijos[i]);
				celdasNodo=concatenarArrays(flecha,celdasNodo);
			}	
		}
		
	
		return celdasNodo;
	}
	
	
	
	private Object[] concatenarArrays(Object a[], Object b[])
	{
		Object[] x=new Object [a.length+b.length];
		
		for (int i=0; i<a.length; i++)
			x[i]=a[i];
		
		for (int i=0; i<b.length; i++)
			x[i+a.length]=b[i];
			
		return x;
	}
	
	public Object[] getEdges()
	{
		// Array para los hijos Edge
		Object[] hijosEdge=new Object[this.edges.length];
		for (int i=0; i<edges.length; i++)
			hijosEdge[i]=edges[i];
		return hijosEdge;
	}
	
	// Devuelve sus edges y los de sus hijos
	public Object[] getAllEdges()
	{
		int x,i,j;
		Object edgesHijos[][]=new Object[this.contenedoresHijos.length][];
		
		
		for (i=0; i<this.contenedoresHijos.length; i++)
			edgesHijos[i]=contenedoresHijos[i].getAllEdges();
		
		int numTotalEdges=this.edges.length;
		for (i=0; i<edgesHijos.length; i++)
			numTotalEdges+=edgesHijos[i].length;
		
		Object edgesTotales[]=new Object[numTotalEdges];
		
		for (i=0; i<this.edges.length; i++)
			edgesTotales[i]=this.edges[i];
		x=i;
		
		for (i=0; i<edgesHijos.length; i++)
			for (j=0; j<edgesHijos[i].length; j++,x++)
				edgesTotales[x]=edgesHijos[i][j];
				
		return edgesTotales;
	}
	
	
	public RegistroActivacion getRegistroPosicion(int x, int y)
	{
		Rectangle2D rect;
		
		rect=GraphConstants.getBounds(this.entrada.getAttributes());
		if (rect.getMaxX()>=x && rect.getMinX()<=x &&
			rect.getMaxY()>=y && rect.getMinY()<=y)
		{
			if (GraphConstants.isOpaque(this.entrada.getAttributes()))
				return this.ra;
			else
				return null;
		}
		rect=GraphConstants.getBounds(this.salida.getAttributes());
		if (rect.getMaxX()>=x && rect.getMinX()<=x &&
			rect.getMaxY()>=y && rect.getMinY()<=y)
		{
			if (GraphConstants.isOpaque(this.salida.getAttributes()))
				return this.ra;
			else
				return null;
		}
		
		
		for (int i=0; i<contenedoresHijos.length; i++)
		{
			RegistroActivacion ra = contenedoresHijos[i].getRegistroPosicion(x,y);
			if (ra!=null)
				return ra;
		}
		
		return null;
	}
	
	
	

	private DefaultGraphCell[] extraerCeldasEstructura(Estructura e,int indices[],String es,int nivel,boolean visible)
	{
		DefaultGraphCell[] celdas=null;
		//int inicioE=inicioEstr[nivel-1];
		int dimensiones[]=e.dimensiones();
		
		
		
		String clase=e.getTipo().getClass().getName();
		String texto;
		
		//escribir("["+ra.getID()+"]"+ra.getEntradaCompletaString());
		
		if (e.esMatriz())
		{
			celdas=new DefaultGraphCell[dimensiones[0]*dimensiones[1]];//(indices[3]-indices[2]+1)*(indices[1]-indices[0]+1)];
			int posic=0;
			
			//for (int i=indices[0]; i<=indices[1]; i++)
			//	for (int j=indices[2]; j<=indices[3]; j++)
			for (int i=0; i<dimensiones[0]; i++)
				for (int j=0; j<dimensiones[1]; j++)
				{
					texto="";
					if (visible && indices.length>0 && i>=indices[0] && i<=indices[1] && j>=indices[2] && j<=indices[3])
					{
						// Si es nodo de salida no calculado y estamos viendo...
						if (!es.equals("entrada") && (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar  )
								&& !ra.esMostradoEntero() && !ra.esHistorico()
								 )	// Si no calculado
						{
							texto="   ";
						}
						else
						{
							if (clase.contains("Integer"))
								texto=" "+e.posicMatrizInt(i,j)+" ";
							else if (clase.contains("Long"))
								texto=" "+e.posicMatrizLong(i,j)+" ";
							else if (clase.contains("Double"))
								texto=" "+e.posicMatrizDouble(i,j)+" ";
							else if (clase.contains("String"))
								texto=" "+e.posicMatrizString(i,j)+" ";
							else if (clase.contains("Character"))
								texto=" "+e.posicMatrizChar(i,j)+" ";	
							else if (clase.contains("Boolean"))
								texto=" "+e.posicMatrizBool(i,j)+" ";
							else
								texto=" "+"·?·"+" ";
						}
					}
					else
					{
						texto="  ";
					}
					celdas[posic] = new DefaultGraphCell(texto);
					
					GraphConstants.setOpaque(        celdas[posic].getAttributes(), visible);
					GraphConstants.setFont(          celdas[posic].getAttributes(), new Font("Arial",Font.BOLD,12));
					GraphConstants.setDisconnectable(celdas[posic].getAttributes(), false);
					GraphConstants.setMoveable(      celdas[posic].getAttributes(), false);
					GraphConstants.setSelectable(    celdas[posic].getAttributes(), false);
					if (visible)
						marcoCelda(					 celdas[posic].getAttributes(), false);
					if (es.equals("entrada"))
					{
						GraphConstants.setForeground(celdas[posic].getAttributes(), Conf.colorFEntrada);
						if (indices.length>0 && i>=indices[0] && i<=indices[1] && j>=indices[2] && j<=indices[3])
							GraphConstants.setBackground(celdas[posic].getAttributes(), Conf.colorC1Entrada);
						else	
							GraphConstants.setBackground(celdas[posic].getAttributes(), Conf.colorC1AEntrada);
					}
					else
					{
						GraphConstants.setForeground(celdas[posic].getAttributes(), Conf.colorFSalida);
						if (indices.length>0 && i>=indices[0] && i<=indices[1] && j>=indices[2] && j<=indices[3])
							GraphConstants.setBackground(celdas[posic].getAttributes(), Conf.colorC1Salida);
						else
							GraphConstants.setBackground(celdas[posic].getAttributes(), Conf.colorC1ASalida);
					}
					GraphConstants.setSize(          celdas[posic].getAttributes(), 
									new Dimension(anchoCeldaEstr,alturaCeldaEstr));
					
					posic++;
				}
		}
		else
		{
			celdas=new DefaultGraphCell[dimensiones[0]];
			int posic= 0 ;
			
			//for (int i=indices[0]; i<=indices[1]; i++)
			for (int i=0; i<dimensiones[0]; i++)
			{
				texto="";
				if (visible && indices.length>0 && i>=indices[0] && i<=indices[1])
				{
					// Si es nodo de salida no calculado y estamos viendo...
					if (!es.equals("entrada") && (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar  )
							&& !ra.esMostradoEntero() && !ra.esHistorico()
							 )	// Si no calculado
					{
						texto="   ";
					}
					else
					{
						if (clase.contains("Integer"))
							texto=" "+e.posicArrayInt(i)+" ";
						else if (clase.contains("Long"))
							texto=" "+e.posicArrayLong(i)+" ";
						else if (clase.contains("Double"))
							texto=" "+e.posicArrayDouble(i)+" ";
						else if (clase.contains("String"))
							texto=" "+e.posicArrayString(i)+" ";
						else if (clase.contains("Character"))
							texto=" "+e.posicArrayChar(i)+" ";	
						else if (clase.contains("Boolean"))
							texto=" "+e.posicArrayBool(i)+" ";
						else
							texto="  "+"MNoDef"+"  ";
					}
				}
				else
				{
					texto="  ";
				}
				celdas[posic] = new DefaultGraphCell(texto);
				GraphConstants.setOpaque(        celdas[posic].getAttributes(), visible);
				GraphConstants.setFont(          celdas[posic].getAttributes(), new Font("Arial",Font.BOLD,12));
				GraphConstants.setDisconnectable(celdas[posic].getAttributes(), false);
				GraphConstants.setMoveable(      celdas[posic].getAttributes(), false);
				GraphConstants.setSelectable(    celdas[posic].getAttributes(), false);
				if (visible)
					marcoCelda(					 celdas[posic].getAttributes(), false);
				if (es.equals("entrada"))
				{
					GraphConstants.setForeground(celdas[posic].getAttributes(), Conf.colorFEntrada);
					if (indices.length>0 && i>=indices[0] && i<=indices[1])
						GraphConstants.setBackground(celdas[posic].getAttributes(), Conf.colorC1Entrada);
					else	
						GraphConstants.setBackground(celdas[posic].getAttributes(), Conf.colorC1AEntrada);
				}
				else
				{
					GraphConstants.setForeground(celdas[posic].getAttributes(), Conf.colorFSalida);
					if (indices.length>0 && i>=indices[0] && i<=indices[1])
						GraphConstants.setBackground(celdas[posic].getAttributes(), Conf.colorC1Salida);
					else
						GraphConstants.setBackground(celdas[posic].getAttributes(), Conf.colorC1ASalida);
				}
				
				posic++;
			}

		}
	
		return celdas;
	}
	
	
	
	
	private int posic0Nivel()
	{
		return (altoDeNivel+Conf.altoSeguridad+Conf.sepV)*(nivel-1) + espacioInicial;
	}
	
	
	
	public void marcoCelda(AttributeMap am,boolean anular)
	{
		if (!anular)
		{
			switch(Conf.bordeCelda)
			{
				case 1:
					GraphConstants.setBorder(am, BorderFactory.createBevelBorder(0));
					break;
				case 2:
					GraphConstants.setBorder(am, BorderFactory.createEtchedBorder());
					break;
				case 3:
					GraphConstants.setBorder(am, BorderFactory.createLineBorder(Conf.colorFlecha));
					break;
				case 4:
					GraphConstants.setBorder(am, BorderFactory.createLoweredBevelBorder());
					break;
				case 5:
					GraphConstants.setBorder(am, BorderFactory.createRaisedBevelBorder() );
					break;
			}
		}
		else
			GraphConstants.setBorder(am, BorderFactory.createEmptyBorder() );
	}
	
	public static DefaultGraphCell crearCuadroVisor(int anchoCuadroVisor,int altoCuadroVisor,int posicX,int posicY)
	{
		DefaultGraphCell cuadroVisor=new DefaultGraphCell();
		GraphConstants.setBounds(cuadroVisor.getAttributes(), new Rectangle2D.Double(posicX,posicY,anchoCuadroVisor, altoCuadroVisor));
		GraphConstants.setValue(cuadroVisor.getAttributes(),"");
		GraphConstants.setOpaque(cuadroVisor.getAttributes(), false);
		GraphConstants.setBorder(cuadroVisor.getAttributes(),BorderFactory.createLineBorder(Conf.colorFlecha,24));
		GraphConstants.setSizeable(cuadroVisor.getAttributes(), false);
		//graph.getGraphLayoutCache().insert(cuadroVisor);
		return cuadroVisor;
	}
	
	public static Rectangle2D rectanguloCelda(DefaultGraphCell celda)
	{
		return GraphConstants.getBounds(celda.getAttributes());
		
	}
	
	
	
	private String generaCadenaSalida()
	{
		String repSalida=ra.getSalida().getRepresentacion();
		String cadenaSalida="";
		if (nyp!=null && (Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA))
			cadenaSalida=nyp.getPrefijo(ra.getNombreMetodo())+": ";
		
		cadenaSalida=cadenaSalida+repSalida;
		
		if (repSalida.length() < 3)
			cadenaSalida="  "+cadenaSalida+"  ";
		
		return cadenaSalida;
	}
	
	
	private int maximaLongitudContenidoEstructura()
	{
		int longMaxima=0;				
		
		if (this.ra.esDYV())
		{
			Estructura e1=new Estructura(ra.getEntrada().getEstructura());
			Estructura e2=new Estructura(ra.getSalida().getEstructura());
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
		if (contenedoresHijos!=null)
			for (int i=0; i<contenedoresHijos.length; i++)
				longMaxima = Math.max( longMaxima , contenedoresHijos[i].maximaLongitudContenidoEstructura() );
		
		return ra.getMaximaLongitudCeldaEstructura();	
	}
	
}