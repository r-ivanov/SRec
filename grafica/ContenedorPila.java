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


import conf.*;
import datos.*;
import utilidades.*;


public class ContenedorPila //extends DefaultGraphCell
{
	static boolean depurar=false;
	static int nivelMaximo=0;
	
	RegistroActivacion ra;
	JGraph graph;
	
	DefaultGraphCell entrada;
	DefaultGraphCell salida;
	
	DefaultGraphCell marcoEntrada;
	DefaultGraphCell marcoSalida;
	
	DefaultPort portEntrada;
	DefaultPort portSalida;
	//DefaultPort port;
	DefaultGraphCell celdaFantasma;
	
	
	ContenedorPila contenedorHijo;
	DefaultEdge edges[];
	
	ParentMap parentMap[];
	boolean tieneHijos=false;
	
	static int alturaMaximaArbol=0;	// Número máximo de alturas que tiene el árbol que se quiere representar
	int anchoMaximoCelda=0;		// Ancho máximo de entre todas las celdas, para alinear centralmente respecto a ella

	
	private static NivelGrafo objetoNivel=null;
	
	public ContenedorPila(RegistroActivacion ra,JGraph graph, Traza traza, NombresYPrefijos nyp, int nivel)
	{
		this.ra=ra;
		this.graph=graph;
		
		if (nivel>ContenedorPila.nivelMaximo)		// Necesitamos almacenar el nivel de altura máximo para ubicar las celdas
			ContenedorPila.nivelMaximo=nivel;
		
		if (nivel==1)
		{
			objetoNivel=new NivelGrafo();
			alturaMaximaArbol=ra.getMaxAltura();
		}

		
		// Entrada, configuración de la misma y asignación de puerto
		String cadenaEntrada="  ";
		if(Conf.idMetodoTraza) {
			cadenaEntrada = "  "+ra.getNombreMetodo()+": ";
			if (nyp!=null)
				cadenaEntrada="  "+nyp.getPrefijo(ra.getNombreMetodo())+": ";
		}
		cadenaEntrada=cadenaEntrada+ra.getEntrada().getRepresentacion()+"  ";
		this.entrada = new DefaultGraphCell(new String(cadenaEntrada));
		GraphConstants.setOpaque(        this.entrada.getAttributes(), true);
		if (this.ra.entradaVisible() &&
			(Conf.VISUALIZAR_ENTRADA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))
		{
			GraphConstants.setForeground(    this.entrada.getAttributes(), Conf.colorFEntrada);	// Entrada
			
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
			//GraphConstants.setBorder(		 this.entrada.getAttributes(), BorderFactory.createRaisedBevelBorder());
			marcoCelda(						 this.entrada.getAttributes());
		}
		else
		{
			int longitud=cadenaEntrada.length();
			cadenaEntrada=" ";
			for (int i=1; i<longitud; i++)
				cadenaEntrada=cadenaEntrada+" ";
			this.entrada = new DefaultGraphCell(new String(cadenaEntrada));
			GraphConstants.setOpaque(        this.entrada.getAttributes(), false);
			GraphConstants.setBackground(    this.entrada.getAttributes(), Conf.colorPanel);
			GraphConstants.setGradientColor( this.entrada.getAttributes(), Conf.colorPanel);
			GraphConstants.setForeground(    this.entrada.getAttributes(), Conf.colorPanel);
		}
		GraphConstants.setFont(          this.entrada.getAttributes(), new Font("Arial",Font.BOLD,20));
		//GraphConstants.setResize(        this.entrada.getAttributes(), true);	// Tamaño justo para que texto quepa.
		GraphConstants.setDisconnectable(this.entrada.getAttributes(), false);
		GraphConstants.setMoveable(      this.entrada.getAttributes(), false);
		GraphConstants.setSelectable(    this.entrada.getAttributes(), false);
		GraphConstants.setSize(          this.entrada.getAttributes(), new Dimension(11*cadenaEntrada.length(),26));
		
		this.portEntrada = new DefaultPort();
		this.entrada.add(portEntrada);
		
		
		// Salida, configuración de la misma y asignación de puerto
		String cadenaSalida="  "+ra.getSalida().getRepresentacion()+"  ";
		
		if (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar && !this.ra.esHistorico() && !this.ra.esMostradoEntero())
		{
			int longitud=cadenaSalida.length();
			cadenaSalida=" ";
			for (int i=1; i<longitud; i++)
				cadenaSalida=cadenaSalida+" ";
		}
		
		
		this.salida = new DefaultGraphCell(new String(cadenaSalida));
		GraphConstants.setOpaque(        this.salida.getAttributes(), true);
		if (this.ra.salidaVisible() &&
			(Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))
		{
			GraphConstants.setForeground(    this.salida.getAttributes(), Conf.colorFSalida);	// Salida
			
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
			//GraphConstants.setBorder(		 this.salida.getAttributes(), BorderFactory.createRaisedBevelBorder());
			marcoCelda(						 this.salida.getAttributes());
		}
		else if (ra.entradaVisible() && !ra.esHistorico() && 			// si no calculado
			(Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))	
		{
			this.salida = new DefaultGraphCell("  ");
			GraphConstants.setOpaque(        this.salida.getAttributes(), true);
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
			else {
				GraphConstants.setBackground(    this.salida.getAttributes(), (Conf.modoColor==1 ? Conf.colorC1NCSalida : Conf.coloresNodo[ra.getNumMetodo()%10]  ));
				GraphConstants.setGradientColor( this.salida.getAttributes(), (Conf.modoColor==1 ? Conf.colorC2NCSalida : Conf.coloresNodo2[ra.getNumMetodo()%10] ));
			}
			marcoCelda(						 this.salida.getAttributes());
		}
		else
		{
			int longitud=cadenaSalida.length();
			cadenaSalida=" ";
			for (int i=1; i<longitud; i++)
				cadenaSalida=cadenaSalida+" ";
			this.salida = new DefaultGraphCell(new String(cadenaSalida));
			GraphConstants.setOpaque(        this.salida.getAttributes(), false);
			GraphConstants.setBackground(    this.salida.getAttributes(), Conf.colorPanel);
			GraphConstants.setGradientColor( this.salida.getAttributes(), Conf.colorPanel);
			GraphConstants.setForeground(    this.salida.getAttributes(), Conf.colorPanel);
		}
		GraphConstants.setFont(          this.salida.getAttributes(), new Font("Arial",Font.BOLD,20));
		//GraphConstants.setResize(        this.salida.getAttributes(), true); // Tamaño justo para que texto quepa.
		GraphConstants.setDisconnectable(this.salida.getAttributes(), false);
		GraphConstants.setMoveable(      this.salida.getAttributes(), false);
		GraphConstants.setSelectable(    this.salida.getAttributes(), false);
		GraphConstants.setSize(          this.salida.getAttributes(), new Dimension(11*cadenaSalida.length(),26));
		
		portSalida = new DefaultPort();
		salida.add(portSalida);
	
		int ramaActual=getRamaActual(ra);
		
		if (!ra.getEsNodoActual() && ramaActual!=-1)
			contenedorHijo=new ContenedorPila(ra.getHijo(ramaActual),graph,traza, nyp, nivel+1);
		else
			contenedorHijo=null;
			
			
		int posicionamiento[]={0,0,0,0};	// [0]=pos horizontal, [1]=pos vertical, [2]=tam horizontal, [3]=tam vertical	
		// Ahora sacamos la anchura máxima de celda, para alinear centralmente en horizontal
		if (nivel==1)
		{
			anchoMaximoCelda=getAnchoMaximoCelda();
			posicionamiento=ubicacion(nivel, 20, 26, anchoMaximoCelda);
		}
	
			
		// Dibujamos celda fantasma a la derecha para que el grafo no se mueva cuando el marco del nodo actual esté a la derecha
		// (eso hace que el ancho del grafo aumente y se mueva todo el grafo, produciendo un efecto incómodo)
		if (nivel==1)
		{
			celdaFantasma= new DefaultGraphCell(new String(""));
			GraphConstants.setOpaque(        this.celdaFantasma.getAttributes(), true);
			
			GraphConstants.setForeground(    this.celdaFantasma.getAttributes(), Conf.colorPanel);
			GraphConstants.setBackground(    this.celdaFantasma.getAttributes(), Conf.colorPanel);
			GraphConstants.setGradientColor( this.celdaFantasma.getAttributes(), Conf.colorPanel); 
			GraphConstants.setBounds(this.celdaFantasma.getAttributes(),
									new Rectangle(	this.maximoAncho()/2,	0,	8,	8));
			GraphConstants.setMoveable(      this.celdaFantasma.getAttributes(), false);
			GraphConstants.setSelectable(    this.celdaFantasma.getAttributes(), false);
		}
	}
	
	
	private int getRamaActual(RegistroActivacion ra)
	{
		// Si somos nodo actual, se lo indicamos al padre
		if (ra==null)
			return -1;
		
		if (ra.getEsNodoActual())
			return 0;
		
		
		// Si no somos nodo actual, buscamos entre hijos visibles
		for (int i=0; i<ra.getHijosVisibles(); i++)
			if (getRamaActual(ra.getHijo(i))!=-1)
				return i;
		
		// Si no hay nodo actual en este subárbol
		return -1;
	}
	
	
	
	public int getAnchoMaximoCelda()
	{
		int x;
		Dimension d=GraphConstants.getSize(this.entrada.getAttributes());
		x=(int)d.getWidth();
		
		d=GraphConstants.getSize(this.salida.getAttributes());
		if (x< (int)d.getWidth())
			x=(int)d.getWidth();
			
		if (contenedorHijo!=null && x<contenedorHijo.getAnchoMaximoCelda())
			x=contenedorHijo.getAnchoMaximoCelda();
		return x;
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
		
	
	
	// Devuelve la cabeza visible de la ContenedorPila: bien la celda de entrada, bien la de salida. 
	public Object getCabezaVisibleParaPadre()
	{
		if (ra.entradaVisible())
			return this.entrada;
		return this.salida;
	}
	
	// Devuelve la cabeza visible de la ContenedorPila: bien la celda de entrada, bien la de salida. 
	public Object getCabezaVisibleParaHijo()
	{
		if (ra.salidaVisible())
			return this.salida;
		return this.entrada;
	}
	
	// Devuelve el puerto visible de la ContenedorPila: bien el de la celda de entrada, bien el de la de salida. 
	public Object getPuertoVisibleParaPadre()
	{
		return ((DefaultGraphCell)getCabezaVisibleParaPadre()).getChildAt(0);
	}
	
	public Object getPuertoVisibleParaHijo()
	{
		return ((DefaultGraphCell)getCabezaVisibleParaHijo()).getChildAt(0);
	}
	
	
	public ParentMap[] getMapasParientes()
	{
		return this.parentMap;
	}
	
	private void addMapa(ParentMap pm)
	{
		if (this.parentMap==null)
		{
			this.parentMap=new ParentMap[1];
			this.parentMap[0]=pm;
		}
		else
		{
			ParentMap pma[]=new ParentMap[this.parentMap.length+1];
			for (int i=0; i<this.parentMap.length; i++)
				pma[i]=this.parentMap[i];
			pma[this.parentMap.length]=pm;
			this.parentMap=pma;
		}
	}
	
	private void addMapa(ParentMap pm[])
	{
		if (pm!=null)
			if (this.parentMap==null)
			{
				this.parentMap=new ParentMap[pm.length];
				for (int i=0; i<pm.length; i++)
					this.parentMap[i]=pm[i];
			}
			else
			{
				ParentMap pma[]=new ParentMap[this.parentMap.length+pm.length];
				for (int i=0; i<this.parentMap.length; i++)
					pma[i]=this.parentMap[i];
				for (int i=this.parentMap.length; i<this.parentMap.length+pm.length; i++)
					pma[i]=pm[i-this.parentMap.length];
				this.parentMap=pma;
			}
		//else
			//System.out.println("Mapa null");
	}
	
	private int[] ubicacion(int nivel, int tamFuente, int alturaCelda, int anchoMaximoCelda)
	{
		// Calculamos la posición vertical de ambas celdas para este nodo (entrada y salida)
		int ubicacion[]=new int[4];
		ubicacion[1]= 20 	// Espacio superior en panel, entre "techo" del mismo y la celda raiz del árbol
				+ ( 2 * alturaCelda * (nivel-1 ))	// Espacio según número de celdas que tendremos por encima
				+ ( (10+Conf.altoSeguridad+Conf.sepV) * (nivel-1) );	
									/*ContenedorPila.nivelMaximo*/
		ubicacion[3]=ubicacion[1]+alturaCelda;
		
		
		ubicacion[0]= 10 + (int)( (anchoMaximoCelda - GraphConstants.getSize(this.entrada.getAttributes()).getWidth()) / 2 );
		ubicacion[2]= 10 + (int)( (anchoMaximoCelda - GraphConstants.getSize(this.salida.getAttributes()).getWidth()) / 2 );
		
		GraphConstants.setBounds(entrada.getAttributes(), new java.awt.geom.Rectangle2D.Double(ubicacion[0],ubicacion[1],
				GraphConstants.getSize(entrada.getAttributes()).getWidth(),GraphConstants.getSize(entrada.getAttributes()).getHeight()));	
			
		GraphConstants.setBounds(salida.getAttributes(), new java.awt.geom.Rectangle2D.Double(ubicacion[2],ubicacion[3],
				GraphConstants.getSize(salida.getAttributes()).getWidth(),GraphConstants.getSize(salida.getAttributes()).getHeight()));	

		// Reubicación y redimensionamiento de una de las dos celdas, para ajustarse a la otra
		if ( GraphConstants.getSize(entrada.getAttributes()).getWidth() < GraphConstants.getSize(salida.getAttributes()).getWidth() )
		{
			GraphConstants.setResize(this.entrada.getAttributes(), false);
			GraphConstants.setBounds(this.entrada.getAttributes(),
									new Rectangle(	(int)(GraphConstants.getBounds(salida.getAttributes()).getMinX()),
													(int)(GraphConstants.getBounds(entrada.getAttributes()).getMinY()),
													(int)(GraphConstants.getSize(salida.getAttributes()).getWidth()),
													(int)(GraphConstants.getSize(entrada.getAttributes()).getHeight() )));//-2
			GraphConstants.setResize(this.salida.getAttributes(), false);
			GraphConstants.setBounds(this.salida.getAttributes(),
									new Rectangle(	(int)(GraphConstants.getBounds(salida.getAttributes()).getMinX()),
													(int)(GraphConstants.getBounds(salida.getAttributes()).getMinY()),
													(int)(GraphConstants.getSize(salida.getAttributes()).getWidth()),
													(int)(GraphConstants.getSize(salida.getAttributes()).getHeight() )));//-2
		}
		else if ( GraphConstants.getSize(salida.getAttributes()).getWidth() < GraphConstants.getSize(entrada.getAttributes()).getWidth() )
		{
			GraphConstants.setResize(this.salida.getAttributes(), false);
			GraphConstants.setBounds(this.salida.getAttributes(),
									new Rectangle(	(int)(GraphConstants.getBounds(entrada.getAttributes()).getMinX()),
													(int)(GraphConstants.getBounds(salida.getAttributes()).getMinY()),
													(int)(GraphConstants.getSize(entrada.getAttributes()).getWidth()),
													(int)(GraphConstants.getSize(salida.getAttributes()).getHeight() )));//-2
			GraphConstants.setResize(this.entrada.getAttributes(), false);
			GraphConstants.setBounds(this.entrada.getAttributes(),
									new Rectangle(	(int)(GraphConstants.getBounds(entrada.getAttributes()).getMinX()),
													(int)(GraphConstants.getBounds(entrada.getAttributes()).getMinY()),
													(int)(GraphConstants.getSize(entrada.getAttributes()).getWidth()),
													(int)(GraphConstants.getSize(entrada.getAttributes()).getHeight() ))); //-2
		}
		

		
		
		// Marcos
		if (ra.getEsNodoActual())
		{
			Rectangle2D rect;
			
			if (ra.entradaVisible() && (Conf.VISUALIZAR_ENTRADA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))
			{
				this.marcoEntrada = new DefaultGraphCell("");
				rect=GraphConstants.getBounds(this.entrada.getAttributes());
				
				//if (rect==null)
				//	System.out.println("rect null");
				
				GraphConstants.setBounds(this.marcoEntrada.getAttributes(),
									new Rectangle(	(int)rect.getMinX()-Conf.grosorMarco,
													(int)rect.getMinY()-Conf.grosorMarco,
													(int)(rect.getMaxX()-rect.getMinX()+(Conf.grosorMarco*2)),
													(int)(rect.getMaxY()-rect.getMinY()+(Conf.grosorMarco*2))));
				GraphConstants.setBackground(    this.marcoEntrada.getAttributes(), Conf.colorMarcoActual);
				GraphConstants.setOpaque(        this.marcoEntrada.getAttributes(), true);
				GraphConstants.setMoveable(      this.marcoEntrada.getAttributes(), false);
				GraphConstants.setSelectable(    this.marcoEntrada.getAttributes(), false);

			}
			if (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar)
			{			
				this.marcoSalida = new DefaultGraphCell("");
				rect=GraphConstants.getBounds(this.salida.getAttributes());
				GraphConstants.setBounds(this.marcoSalida.getAttributes(),
									new Rectangle(	(int)rect.getMinX()-Conf.grosorMarco,
													(int)rect.getMinY()-Conf.grosorMarco,
													(int)(rect.getMaxX()-rect.getMinX()+(Conf.grosorMarco*2)),
													(int)(rect.getMaxY()-rect.getMinY()+(Conf.grosorMarco*2))));
				GraphConstants.setBackground(    this.marcoSalida.getAttributes(), Conf.colorMarcoActual);
				GraphConstants.setOpaque(        this.marcoSalida.getAttributes(), true);
				GraphConstants.setMoveable(      this.marcoSalida.getAttributes(), false);
				GraphConstants.setSelectable(    this.marcoSalida.getAttributes(), false);
			}
		}
		else if (ra.getEsCaminoActual())
		{
			Rectangle2D rect;
			
			if (ra.entradaVisible() && (Conf.VISUALIZAR_ENTRADA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar))
			{
				this.marcoEntrada = new DefaultGraphCell("");
				rect=GraphConstants.getBounds(this.entrada.getAttributes());
				
				GraphConstants.setBounds(this.marcoEntrada.getAttributes(),
									new Rectangle(	(int)rect.getMinX()-Conf.grosorMarco,
													(int)rect.getMinY()-Conf.grosorMarco,
													(int)(rect.getMaxX()-rect.getMinX()+(Conf.grosorMarco*2)),
													(int)(rect.getMaxY()-rect.getMinY()+(Conf.grosorMarco*2))));
				GraphConstants.setBackground(    this.marcoEntrada.getAttributes(), Conf.colorMarcosCActual);
				GraphConstants.setOpaque(        this.marcoEntrada.getAttributes(), true);
				GraphConstants.setMoveable(      this.marcoEntrada.getAttributes(), false);
				GraphConstants.setSelectable(    this.marcoEntrada.getAttributes(), false);
			}
			if (Conf.VISUALIZAR_SALIDA==Conf.elementosVisualizar || Conf.VISUALIZAR_TODO==Conf.elementosVisualizar)
			{	
				this.marcoSalida = new DefaultGraphCell("");
				rect=GraphConstants.getBounds(this.salida.getAttributes());
				GraphConstants.setBounds(this.marcoSalida.getAttributes(),
									new Rectangle(	(int)rect.getMinX()-Conf.grosorMarco,
													(int)rect.getMinY()-Conf.grosorMarco,
													(int)(rect.getMaxX()-rect.getMinX()+(Conf.grosorMarco*2)),
													(int)(rect.getMaxY()-rect.getMinY()+(Conf.grosorMarco*2))));
				GraphConstants.setBackground(    this.marcoSalida.getAttributes(), Conf.colorMarcosCActual);
				GraphConstants.setOpaque(        this.marcoSalida.getAttributes(), true);
				GraphConstants.setMoveable(      this.marcoSalida.getAttributes(), false);
				GraphConstants.setSelectable(    this.marcoSalida.getAttributes(), false);
			}
		}

		
		if(contenedorHijo!=null)
			contenedorHijo.ubicacion(nivel+1,tamFuente,alturaCelda, anchoMaximoCelda);
		
		// Flechas (edges, tantas como hijos)
		edges=new DefaultEdge[contenedorHijo!=null?1:0];
		if (contenedorHijo!=null)
		{
			edges[0]=new DefaultEdge();
			
			
			
			edges[0].setSource( ((DefaultGraphCell)(this.getPuertoVisibleParaHijo())) );
			edges[0].setTarget( ((DefaultGraphCell)(contenedorHijo.getPuertoVisibleParaPadre())) );
		
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
		
			GraphConstants.setLineEnd(edges[0].getAttributes(), tipoFlecha);
			GraphConstants.setEndFill(edges[0].getAttributes(), true);
			GraphConstants.setSelectable(edges[0].getAttributes(),false);
			GraphConstants.setLineWidth(edges[0].getAttributes(), Conf.grosorFlecha); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(edges[0].getAttributes(), Conf.colorFlecha); // color de la línea
		}
		
		return ubicacion;
	}
	
	
	public boolean tieneHijos()
	{
		return this.tieneHijos;
	}
	
	private int posIzquierda(int nivel, boolean preguntaPadre)
	{
		int pos;
		if (contenedorHijo!=null)
		{
			pos=contenedorHijo.posIzquierda(nivel+1,preguntaPadre);
			return pos;
		}
		else if (preguntaPadre==false)
			return objetoNivel.getNivel(nivel);
		else
			return (int)GraphConstants.getBounds(entrada.getAttributes()).getMinX();
	}
	
	private int posDerecha(int nivel, boolean preguntaPadre)
	{
		int pos;
		if (contenedorHijo!=null)
		{
			pos=contenedorHijo.posDerecha(nivel+1,preguntaPadre);
			return pos;
		}
		else if (preguntaPadre==false)
		{
			return (int)GraphConstants.getBounds(entrada.getAttributes()).getMaxX();
		}
		else
			return ((int)(GraphConstants.getBounds(entrada.getAttributes())).getMinX())+
					((int)GraphConstants.getSize(entrada.getAttributes()).getWidth());
	}
	
	
	public int maximoAncho()
	{
		return (this.getAnchoMaximoCelda()+(Conf.grosorMarco*2))+(10-Conf.grosorMarco);
		//return objetoNivel.getMaximoAncho();
	}

		
	public int maximoAlto()
	{
		if (contenedorHijo!=null)
			return contenedorHijo.maximoAlto();

		else
			return (int)((GraphConstants.getBounds(entrada.getAttributes()).getMinY())+
					(GraphConstants.getBounds(entrada.getAttributes()).getHeight()*3));

	}
	
	
	public Object[] getCeldasEstado()
	{
		// Array para todos los hijos
		Object[] estados=new Object[2];
		estados[0]=this.entrada;
		estados[1]=this.salida;
		return estados;	
	}
	
	
	public Object[] getCeldas()
	{
		Object celdasNodo[];
		if (this.marcoEntrada==null && this.marcoSalida==null)
		{
			celdasNodo=new Object[2];
			celdasNodo[0]=this.entrada;
			celdasNodo[1]=this.salida;
		}
		else if (this.marcoEntrada!=null && this.marcoSalida!=null)
		{
			celdasNodo=new Object[4];
			celdasNodo[0]=this.marcoEntrada;
			celdasNodo[1]=this.marcoSalida;
			celdasNodo[2]=this.entrada;
			celdasNodo[3]=this.salida;
		}
		else
		{
			celdasNodo=new Object[3];
			if (this.marcoEntrada!=null)
			{
				celdasNodo[0]=this.salida;
				celdasNodo[1]=this.marcoEntrada;
				celdasNodo[2]=this.entrada;
			}
			else
			{
				celdasNodo[0]=this.entrada;
				celdasNodo[1]=this.marcoSalida;
				celdasNodo[2]=this.salida;
			}
		}
		
		Object cFantasma[]=new Object[1];
		cFantasma[0]=this.celdaFantasma;
		
		if (this.celdaFantasma!=null)
			celdasNodo=concatenarArrays(cFantasma,celdasNodo);
	
		if (contenedorHijo!=null)
		{
			Object celdasDeHijos[]=contenedorHijo.getCeldas();
	
				
			Object flecha[]=new Object[1];
			
			flecha[0]=this.edges[0];
				
			celdasNodo=concatenarArrays(celdasNodo,celdasDeHijos);
			celdasNodo=concatenarArrays(flecha,celdasNodo);
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
	

	
	private Object[] condensarHijos()
	{
		if (contenedorHijo!=null)
		{
			Object hijos[]=new Object[ 6 ];
			hijos[0]=entrada;
			hijos[1]=salida;
			hijos[2]=portEntrada;
			hijos[3]=portSalida;
			hijos[4]=contenedorHijo;
			hijos[5]=edges[0];	
			return hijos;
		}
		else
		{
			Object hijos[]=new Object[ 4 ];
			hijos[0]=entrada;
			hijos[1]=salida;
			hijos[2]=portEntrada;
			hijos[3]=portSalida;
			return hijos;
		}
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
		if (contenedorHijo!=null)
		{
			RegistroActivacion ra = contenedorHijo.getRegistroPosicion(x,y);
			if (ra!=null)
				return ra;
		}
		
		return null;
	}
	
	
	public int alturaMaximaGrafo()
	{
		return (alturaMaximaArbol * ((26 * 2) +10+Conf.altoSeguridad+Conf.sepV   ) )+20;
					// 26 = alturaCelda (dos por cada nivel)
					// 20 = espacio superior entre grafo y techo de panel
					// 10 separacion mínima entre celdas (mayor que máximo ancho de marco)
	}
	
	
	public void marcoCelda(AttributeMap am)
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
}