/**
	Panel que contendrá la visualización del algoritmo, ya que almacena la propia traza.
	Está contenido en PanelAlgoritmo.
	
	@author Luis Fernández y Antonio Pérez Carrasco
	@version 2006-2007
*/
package paneles;

import java.io.File;
import java.lang.OutOfMemoryError;




import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;




import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

/*import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;*/




import org.jgraph.JGraph;
import org.jgraph.graph.*;

import conf.*;
import cuadros.*;
import datos.*;
import eventos.*;
import grafica.*;
import grafo.SrecCellViewFactory;
import utilidades.*;
import ventanas.*;

public class PanelArbol extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener
{
	static final long serialVersionUID=04;


	

	//private PanelRegistroActivacion panelRaiz;
	
	private JGraph graph;		// Grafo árbol principal
	private JGraph g;			// Grafo visor
	
	
	GraphModel model = new DefaultGraphModel();
	GraphLayoutCache view = new GraphLayoutCache(model,new SrecCellViewFactory());
	
	
	JViewport vp;
	ContenedorArbol cc;
	
	Object []celdas=new Object[0];
	Object []celdasV;
	
	DefaultGraphCell cuadroVisor;
	
	RegistroActivacion ra;		// Nodo que habremos pinchado con algún botón del ratón
	NombresYPrefijos nyp;
	

	JScrollPane jspArbol = null;
	JScrollPane jspVisor = null;
	
	JPanel visor;
	JPanel panel;
	
	public static double ESCALAVISOR=0.10;
	public static double escala = 0.10;  //Escala variable para ajustar la miniatura al tamaño del panel
	
	boolean mostrarNombreMetodos=false;
	
	double escalaOriginal;
	double escalaActual;
	
	int zoom=0;
	private static int anchoGraph;
	private static int altoGraph;
	
	//Coordenadas para la visualización dinamica del arbol
	private static int minX=0;
	private static int minY=0;
	
	//Anchos y altos minimos y maximos para saber si la siguiente celda esta dentro del visor actual
	private int anMin=-1;
	private int anMax=-1;
	private int alMin=-1;
	private int alMax=-1;
	
	JPopupMenu m;
	JMenuItem opcionesMenuContextual[];
	
	int ratonX=0, ratonY=0, ratonXAbs=0, ratonYAbs=0;
	
	NavegacionListener nl=null;
	
	boolean haEntradoAhora=true; 
	EtiquetaFlotante ef;
		// true: la proxima vez que se mueva dentro de un nodo, significará que acaba de entrar en él, mostrará información
		// false: la proxima vez que se mueva dentro de un nodo, significará que ya estaba dentro de él, no hacemos nada
	
	int dim[]=new int[4];	// Dimensiones de panel [0],[1] y grafo [2],[3]
	
	/**
		Constructor: crea un nuevo panel de traza
		
		@param numPanelesTraza número de panales que se desean
	*/
	public PanelArbol(NombresYPrefijos nyp) throws Exception 
	{
		this.nyp=nyp;
		if (Ventana.thisventana.traza!=null)
		{
			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,new SrecCellViewFactory());
			this.graph = new JGraph(model, view);
			
			this.graph.getModel().addGraphModelListener(null);
			try {
				
				cc=new ContenedorArbol(Ventana.thisventana.traza.getRaiz(),	this.graph,this.nyp,1);
				anchoGraph=cc.maximoAncho();		// Sólo es necesario hacerlo una vez
				altoGraph=cc.maximoAlto();			// Sólo es necesario hacerlo una vez
				this.celdas=cc.getCeldas();
				this.graph.getGraphLayoutCache().insert(this.celdas);
				this.graph.addMouseListener(this);
				this.graph.addMouseMotionListener(this);
				this.graph.setBackground(Conf.colorPanel);
				
				BorderLayout bl = new BorderLayout();
				this.setLayout(bl);
				escalaOriginal=	this.graph.getScale();	// Sólo es necesario hacerlo una vez
				
				jspArbol=new JScrollPane();
				
				vp=jspArbol.getViewport();//new JViewport();
				jspArbol.setBorder(new EmptyBorder(0,0,0,0));
				vp.add(	this.graph);
				
				this.add(jspArbol,BorderLayout.CENTER);
				
				this.visor=crearVisor(cc.getCeldas(),	this.graph);
				escala = this.calculaEscalaMiniatura();
				this.visor.setPreferredSize(new Dimension((int)(anchoGraph*escala),(int)(altoGraph*escala)+25));
				
				//this.visor.setPreferredSize(new Dimension(10,80));
				
				this.visor.updateUI();
				jspVisor = new JScrollPane(this.visor);
				//Si se ajusta la vista global al espacio disponible, quitamos las barras de desplazamiento
				if(Conf.ajustarVistaGlobal) {
					jspVisor.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			        jspVisor.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				}
				else {
					jspVisor.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			        jspVisor.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				}
				//jspVisor.setPreferredSize(new Dimension(400,400));
				//jspVisor.setBorder(new EmptyBorder(0,0,0,0));
				if (Conf.mostrarVisor && jspVisor!=null)
					this.add(jspVisor,BorderLayout.SOUTH);		

				cuadroVisor=ContenedorArbol.crearCuadroVisor(400,400,0,0);
				refrescarZoom(Conf.zoomArbol);

				
			} catch (OutOfMemoryError oome) {
				cc=null;
				this.graph=null;
				throw oome;
			} catch (Exception e) {
				cc=null;
				throw e;
			}
		}
		else
		{
			this.add(new JPanel(),BorderLayout.CENTER);
			
			jspArbol=new JScrollPane();
			jspArbol.setBorder(new EmptyBorder(0,0,0,0));
			vp=jspArbol.getViewport();//new JViewport();
		}
		
		// Gestionamos eventos
		nl=new NavegacionListener(vp,celdasV,this);
		
		
		// Gestionamos eventos (nuevo)
		nl.setAtributos(vp,celdasV,this.escalaActual);

		jspArbol.getHorizontalScrollBar().getComponent(0).addMouseListener(nl);
		jspArbol.getHorizontalScrollBar().getComponent(1).addMouseListener(nl);
		jspArbol.getVerticalScrollBar().getComponent(0).addMouseListener(nl);
		jspArbol.getVerticalScrollBar().getComponent(1).addMouseListener(nl);
		jspArbol.getHorizontalScrollBar().addMouseWheelListener(nl);
		jspArbol.getVerticalScrollBar().addMouseWheelListener(nl);
		jspArbol.getHorizontalScrollBar().addMouseMotionListener(nl);
		jspArbol.getVerticalScrollBar().addMouseMotionListener(nl);
		jspArbol.getHorizontalScrollBar().addMouseListener(nl);
		jspArbol.getVerticalScrollBar().addMouseListener(nl);
	
	}

	
	
	public PanelArbol(String ficheroGIF, ImageIcon imagen) throws Exception 
	{
		try {
			
			BorderLayout bl = new BorderLayout();
			this.setLayout(bl);

			BufferedImage image = ImageIO.read(new File(ficheroGIF));
			int c = image.getRGB(3,3);
			int  red = (c & 0x00ff0000) >> 16;
			int  green = (c & 0x0000ff00) >> 8;
			int  blue = c & 0x000000ff;
			// and the Java Color is ...
			
			//Color color = new Color(red,green,blue);
			
			JPanel panel=new JPanel();
			panel.add(new JLabel(imagen));
			panel.setBackground(new Color(red,green,blue));
			
			jspArbol=new JScrollPane(panel);
			jspArbol.setBorder(new EmptyBorder(0,0,0,0));
			this.add(jspArbol,BorderLayout.CENTER);
			
			
		} catch (Exception e) {
			cc=null;
			throw e;
		}
	
		
	}
	
	
	/**
		Visualiza y redibuja el árbol en el panel
		
		@param redimension true cuando redibujamos el panel tras una redimension o variacion de zoom
		@param recalcular true cuando hay que redibujar el árbol
	*/		
	public void visualizar(boolean redimension, boolean recalcular, boolean movimientoVisor)
	{
		//Si se ajusta la vista global al espacio disponible, quitamos las barras de desplazamiento
		if(Conf.ajustarVistaGlobal) {
			jspVisor.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	        jspVisor.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		else {
			jspVisor.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        jspVisor.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		if (Ventana.thisventana.traza!=null)
		{
			
			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,new SrecCellViewFactory());
			graph = new JGraph(model, view);
			
			graph.getModel().addGraphModelListener(null);
			
			
			
			nyp=null;
			this.mostrarNombreMetodos = Ventana.thisventana.traza.getNumMetodos()!= 1;
			if (mostrarNombreMetodos)
			{
				nyp=new NombresYPrefijos();
				String []nombresMetodos=Ventana.thisventana.trazaCompleta.getNombresMetodos();
				String prefijos[]=ServiciosString.obtenerPrefijos(nombresMetodos);
				for (int i=0; i<nombresMetodos.length; i++)
					nyp.add(nombresMetodos[i],prefijos[i]);
			}
			// viejo:
			/*
			if (recalcular)
			{
				if (Conf.getRedibujarGrafoArbol() || cc==null)
					cc=new ContenedorArbol(VentanaVisualizador.thisventana.traza.getRaiz(),graph,this.nyp,1);
				else
					cc.actualizar();

					this.celdas=cc.getCeldas();
			}
			*/
			
			/*if (recalcular)
			{
				if (Conf.getRedibujarGrafoArbol() || cc==null)
				{
					System.out.println("redibujamos árbol entero... (1)");*/
					cc=new ContenedorArbol(Ventana.thisventana.traza.getRaiz(),graph,this.nyp,1);
				/*	System.out.println("redibujamos árbol entero... (2)");
				}
				else
				{
					System.out.println("solo repintamos árbol... (1)");
					cc.actualizar();
					System.out.println("solo repintamos árbol... (2)");
				}
			}*/
			this.celdas=cc.getCeldas();
			
			// nuevas:
			//cc=new ContenedorArbol(VentanaVisualizador.thisventana.traza.getRaiz(),graph,this.nyp,1);
			//this.celdas=cc.getCeldas();
	
			graph.getGraphLayoutCache().insert(this.celdas);
			
			graph.addMouseListener(this);
			graph.addMouseMotionListener(this);
			graph.setScale(this.escalaActual);
			graph.setBackground(Conf.colorPanel);
			graph.addMouseWheelListener(nl);
			graph.addKeyListener(this);

			Point puntoMinimo=vp.getViewPosition();
			
			if (cc!=null)
			{
				anchoGraph=cc.maximoAncho();	
				altoGraph=cc.maximoAlto();		
			}
			
			vp.setBackground(Conf.colorPanel);
			vp.removeAll();
			vp.add(graph);
			vp.setViewPosition(puntoMinimo);
			vp.updateUI();
			
			if (this.cuadroVisor==null)
			{
				cuadroVisor=ContenedorArbol.crearCuadroVisor(400,400,0,0);
				GraphConstants.setEditable(cuadroVisor.getAttributes(),false);
				
				celdasV=new Object[this.celdas.length+2];
				for (int i=0; i<this.celdas.length; i++)
				{
					celdasV[i]=this.celdas[i];
				}
				
				
				this.visor=crearVisor(this.celdas,	this.graph);
				

				System.out.println("PanelArbol.anchoGraph = "+anchoGraph);
				escala = this.calculaEscalaMiniatura();
				this.visor.setPreferredSize(new Dimension((int)(anchoGraph*escala),(int)(altoGraph*escala)+20));
				celdasV[celdasV.length-1]=(Object)cuadroVisor;
			}
			

			
			Rectangle2D r2d=ContenedorArbol.rectanguloCelda(this.cuadroVisor);
			
			Point p=new Point((int)(r2d.getMinX()*this.escalaActual),(int)(r2d.getMinY()*this.escalaActual));
			vp.setViewPosition(p);
			
			if(Conf.visualizacionDinamica) {
				int xAux = (int)(minX*this.escalaActual)-(jspArbol.getWidth()/2);
				int yAux = (int)(minY*this.escalaActual)-(jspArbol.getHeight()/2);
				//Solo desplazamos si la siguiente celda no es visible desde la posicion actual, o en la primera celda
				if((anMin==-1)||((xAux<anMin)||(xAux>anMax)||(yAux<alMin)||(yAux>alMax))) {
					//Posicionamos en el ultimo nodo creado. Estas instrucciones tienen que ir aqui, de otro modo se produce mas efecto parpadeo del habitual
					jspArbol.getHorizontalScrollBar().setValue((int)(minX*this.escalaActual)-(jspArbol.getWidth()/2));
					jspArbol.getVerticalScrollBar().setValue((int)(minY*this.escalaActual)-(jspArbol.getHeight()/2));
					//Calculamos el ancho y alto maximo y minimo de la ventana centrada en el nodo actual;
					anMin = ((int)(minX*this.escalaActual)-2*(jspArbol.getWidth()/2));
					anMax = anMin + jspArbol.getWidth();
					alMin = ((int)(minY*this.escalaActual)-2*(jspArbol.getHeight()/2));
					alMax = alMax + jspArbol.getHeight();
				}
			}
			
			visor.removeAll();

			if (!redimension && Conf.getPanelArbolReajustado()==false)
					visor.add(crearVisor(this.celdas,graph,(int)r2d.getMinX(),(int)r2d.getMinY(),movimientoVisor));
			else
			{
				if (!Conf.getHaciendoAjuste())
					visor.add(crearVisor(this.celdas,graph,
						(int)(puntoMinimo.getX()/this.escalaActual),
						(int)(puntoMinimo.getY()/this.escalaActual),movimientoVisor));
				else
				{
					visor.add(crearVisor(this.celdas,graph,0,0,movimientoVisor));
					Conf.setHaciendoAjuste(false);
				}
			}
			escala = this.calculaEscalaMiniatura();
			this.visor.setPreferredSize(new Dimension((int)(anchoGraph*escala),(int)(altoGraph*escala)+20));
			visor.updateUI();
			Conf.setPanelArbolReajustado(false);

			
			
			// Gestionamos eventos (viejo)
			nl.setAtributos(vp,celdasV,this.escalaActual);
			
			
			
			jspArbol.getHorizontalScrollBar().getComponent(0).addMouseListener(nl);
			jspArbol.getHorizontalScrollBar().getComponent(1).addMouseListener(nl);
			jspArbol.getVerticalScrollBar().getComponent(0).addMouseListener(nl);
			jspArbol.getVerticalScrollBar().getComponent(1).addMouseListener(nl);
			jspArbol.getHorizontalScrollBar().addMouseWheelListener(nl);
			jspArbol.getVerticalScrollBar().addMouseWheelListener(nl);
			jspArbol.getHorizontalScrollBar().addMouseMotionListener(nl);
			jspArbol.getVerticalScrollBar().addMouseMotionListener(nl);
			jspArbol.getHorizontalScrollBar().addMouseListener(nl);
			jspArbol.getVerticalScrollBar().addMouseListener(nl);

			
			graph.addKeyListener(nl);
			
			this.removeAll();
			
	
			this.add(jspArbol,BorderLayout.CENTER);
			if (Conf.mostrarVisor && jspVisor!=null)
				this.add(jspVisor,BorderLayout.SOUTH);
			
			this.updateUI();
		}
	}
	

	/**
		Visualiza y redibuja el árbol en el panel
	*/		
	public void visualizar2(boolean movimientoScroll)
	{
		// Obtenemos dónde estaba cuadro visor en estado anterior
		Rectangle2D r2d=ContenedorArbol.rectanguloCelda(this.cuadroVisor);
		
		//Point p=new Point((int)(r2d.getMinX()*this.escalaActual),(int)(r2d.getMinY()*this.escalaActual));

		this.visor.removeAll();
		this.visor.add(crearVisor(this.celdas,	this.graph,(int)r2d.getMinX(),(int)r2d.getMinY(),movimientoScroll));
		this.visor.updateUI();

		// Gestionamos eventos
		nl.setAtributos(vp,celdasV,this.escalaActual);
	}
	
	private double calculaEscalaMiniatura() {
		
		if(Conf.ajustarVistaGlobal) {
			double escalaMiniatura = ESCALAVISOR;
			if (jspVisor!=null) {
				int tamX= (int)(jspVisor.getWidth());
				if(tamX <= 0) {
					int anchoPila=(PanelAlgoritmo.dimPanelPila()[0]);
					int anchoTraza=(PanelAlgoritmo.dimPanelTraza()[0]);
					if (anchoPila>0 && anchoTraza>0) {
						tamX=Conf.getTamanoVentana()[0]-anchoPila-anchoTraza;
					}
					else {
						tamX=(int)((Conf.getTamanoVentana()[0] / 2)/escalaActual);
					}
				}
				tamX-=50;  //Dejamos un margen 
				escalaMiniatura = (double) ((double)tamX/PanelArbol.anchoGraph);
				}
			if (escalaMiniatura >= ESCALAVISOR)  //Si la escala es mas grande de lo normal, ponemos la estandar
				return ESCALAVISOR;
			return escalaMiniatura;
		}
		else
			return ESCALAVISOR;
	}
	
	private JPanel crearVisor(Object[] celdas,JGraph graph)
	{
		return crearVisor(celdas,graph,0,0,false);
	}
	
	
	private JPanel crearVisor(Object[] celdas,JGraph graph,int posX, int posY, boolean movimientoVisor)
	{
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,new SrecCellViewFactory());
		g=new JGraph(model, view);
	
		g.getModel().addGraphModelListener(null);

		//System.out.println("  - crearVisor: "+posX+"x"+posY);
		
		// Creamos repertorio de celdas para el visor: todas las del grafo y una más: el cuadro de movimiento
		celdasV=new Object[celdas.length+1];
		for (int i=0; i<celdas.length; i++)
		{
			celdasV[i]=celdas[i];
		}

		if (jspVisor!=null)
		{
			int tamX= (int)(jspVisor.getWidth()/this.escalaActual);
			int tamY= (int)(this.getHeight()/this.escalaActual-jspVisor.getHeight()/this.escalaActual);
		
			//System.out.println("  "+tamX+" = (int)"+(jspVisor.getWidth())+"/"+this.escalaActual);
			//System.out.println("  "+tamY+" = (int)("+(this.getHeight())+"/"+this.escalaActual+"-"+
			//					jspVisor.getHeight()+"/"+this.escalaActual+")");

			if (tamX<=0 || tamY<=0)
			{
				int anchoPila=(PanelAlgoritmo.dimPanelPila()[0]);
				//int altoPila=(PanelAlgoritmo.dimPanelPila()[1]);
				int anchoTraza=(PanelAlgoritmo.dimPanelTraza()[0]);
				//int altoTraza=(PanelAlgoritmo.dimPanelTraza()[1]);
				
				if (anchoPila>0 && anchoTraza>0)
				{
					tamX=Conf.getTamanoVentana()[0]-anchoPila-anchoTraza;
					tamY=tamX;
					//System.out.println("    Medida alt 1    (anchoPila="+anchoPila+"  altoTraza="+altoTraza+")");
				}
				else
				{
					tamX=(int)((Conf.getTamanoVentana()[0] / 2)/escalaActual);
					tamY=tamX;
					//System.out.println("    Medida alt 2    (anchoPila="+anchoPila+"  altoTraza="+altoTraza+")");
				}
				
			}
			int posicionX = (minX-(jspArbol.getWidth()/2));
			if(posicionX < 0) 
				posicionX = 0;
			int posicionY = (minY-(jspArbol.getHeight()/2));
			if(posicionY < 0)
				posicionY = 0;
			if((Conf.visualizacionDinamica)&&(!movimientoVisor))
				cuadroVisor=ContenedorArbol.crearCuadroVisor( tamX, tamY, posicionX, posicionY);
			else
				cuadroVisor=ContenedorArbol.crearCuadroVisor( tamX, tamY, posX, posY);
		}
		else
		{
			cuadroVisor=ContenedorArbol.crearCuadroVisor(400,400,0,0);
		}
		
		GraphConstants.setEditable(cuadroVisor.getAttributes(),false);
		celdasV[celdasV.length-1]=(Object)cuadroVisor;
		
		//codeRec=cuadroVisor.hashCode();
			
		// Generamos el grafo insertando en él las celdas

		g.getGraphLayoutCache().insert(celdasV);
		escala = this.calculaEscalaMiniatura();
		g.setScale(escala);
		g.addMouseListener(this);
		g.addMouseMotionListener(this);
		g.setBackground(Conf.colorPanel);
		g.setAutoResizeGraph(false);
		g.setSizeable(false);
		
		
		// inicio comentado el 12 de nov de 2009
		//g.setPreferredSize(new Dimension( (int)(anchoGraph*ESCALAVISOR) , (int)(altoGraph*ESCALAVISOR) ));
		// fin comentado el 12 de nov de 2009
		
		
		
		//g.setHighlightColor(new Color(255,0,0));
		//g.setMoveable(false);
		//g.setMoveBelowZero(false);
		//g.setMoveBeyondGraphBounds(false);
		JPanel panelVisor=new JPanel();
		panelVisor.setLayout(new BorderLayout());
		
		g.addMouseListener(nl);
		g.addMouseMotionListener(nl);
		
		panelVisor.add(g,BorderLayout.CENTER);
		panelVisor.updateUI();
		
		return panelVisor;
	}
	
	
	/*public JPanel getPanelVisor()
	{
		return this.panelVisor;
	}*/
	
	
	

	
	/**
		Devuelve la traza
		
		@return traza de ejecución que visualiza
	*/		
	/*protected Traza traza()
	{
		return this.traza;
	}*/
	
	public int ancho()
	{
		return anchoGraph;
	}
	
	public int getZoom()
	{
		return zoom;
	}
	
	
	
	public int alturaJSPArbol ()
	{
		return this.jspArbol.getHeight();
	}
	
	
	
	public int[] dimGrafo()
	{
		int valores[]=new int[2];
		valores[0]=cc.maximoAncho();
		valores[1]=cc.maximoAlto();
		return valores;
	}
	
	public int[] dimGrafoVisible()
	{
		int valores[]=new int[2];
		valores[0]=cc.maximoAnchoVisible()+10;//+15
		valores[1]=cc.maximoAltoVisible()+20;
		new Thread(){
			public synchronized void run()
			{
				try {wait(40);} catch (java.lang.InterruptedException ie) {}
				vp.setViewPosition(new Point(0,0));
			}
		}.start();
		
		return valores;
	}
	
	
	public int[] dimPanelYGrafo()
	{
		int dim[]=new int[4];
		
		//dim[0]=(int)(this.getSize().getWidth());					// Anchura del panel *
		//dim[1]=(int)(this.getSize().getHeight());					// Altura del panel	*
		
		dim[0]=(this.getWidth());					// Anchura del panel *
		dim[1]=(this.getHeight());					// Altura del panel	*
		
		dim[2]=(int)(graph.getPreferredSize().getWidth());			// Anchura del grafo
		dim[3]=(int)(graph.getPreferredSize().getHeight());			// Altura del grafo
		
		// * = Las dimensiones del panel sólo son reales si son mayores que las del grafo. Si son
		//     menores, entonces siempre da las dimensiones del grafo+10
		
		return dim;
	}
	
	public int[] dimPanelYGrafo2()
	{
		JPanel p=this.panel;
		this.removeAll();
		this.add(new JPanel());
		
		int dim[]=new int[4];
		
		//dim[0]=(int)(this.getSize().getWidth());					// Anchura del panel *
		//dim[1]=(int)(this.getSize().getHeight());					// Altura del panel	*
		
		dim[0]=(this.getWidth());					// Anchura del panel *
		dim[1]=(this.getHeight());					// Altura del panel	*
		
		dim[2]=(int)(graph.getPreferredSize().getWidth());			// Anchura del grafo
		dim[3]=(int)(graph.getPreferredSize().getHeight());			// Altura del grafo
		
		// * = Las dimensiones del panel sólo son reales si son mayores que las del grafo. Si son
		//     menores, entonces siempre da las dimensiones del grafo+10
		this.removeAll();
		this.add(p);
		
		return dim;
	}
	
	

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JMenuItem)
		{
			JMenuItem item=(JMenuItem)e.getSource();
			
			// Menú contextual: primera opción: etiqueta detallada
			if (item==opcionesMenuContextual[0])
			{
				if (ra!=null && haEntradoAhora)
				{
					this.ef=new EtiquetaFlotante(this.ratonXAbs,this.ratonYAbs,ra.getNombreMetodo(),
										ra.getEntradaCompletaString(),ra.getSalidaCompletaString(),
										ra.entradaVisible(),ra.salidaVisible());
					this.haEntradoAhora=false;
				}
				else if (ra==null)
				{
					//System.out.print("x");
					this.haEntradoAhora=true;
				}
			}
			// Menú contextual: segunda opción: hacer nodo actual
			else if (item==opcionesMenuContextual[1])
			{
				//this.ra=cc.getRegistroPosicion((int)(this.ratonX/graph.getScale()),(int)(this.ratonY/graph.getScale()));
				if (ra!=null)
				{
					ra.hacerNodoActual();
					Ventana.thisventana.refrescarOpciones();
				}
			}
			// Menú contextual: tercera opción: cuadro de inforamción sobre nodo
			else if (item==opcionesMenuContextual[2])
			{
				new CuadroInfoNodo(Ventana.thisventana, ra);
			}
			// Menú contextual: cuarta opción: seleccionar/no seleccionar
			else if (item==opcionesMenuContextual[3])
			{
				boolean valor=!this.ra.estaIluminado();
				if (Conf.elementosVisualizar==Conf.VISUALIZAR_ENTRADA)
					Ventana.thisventana.getTraza().iluminar(this.ra.getNumMetodo(),this.ra.getEntradasString(),null,valor);
				else if (Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA)
					Ventana.thisventana.getTraza().iluminar(this.ra.getNumMetodo(),null,this.ra.getSalidasString(),valor);
				else if (Conf.elementosVisualizar==Conf.VISUALIZAR_TODO)
					Ventana.thisventana.getTraza().iluminar(this.ra.getNumMetodo(),this.ra.getEntradasString(),this.ra.getSalidasString(),valor);
				
				Ventana.thisventana.refrescarFormato();
				//if (valor)
				//	new CuadroInformacion(		VentanaVisualizador.thisventana,
				//								Texto.get("INFO_ACCION",Conf.idioma),
				//								Texto.get("INFO_NODOSENC",Conf.idioma)+": "+nodosIluminados);
				
			}
			// Menú contextual: quinta opción: resaltar nodo
			else if (item==opcionesMenuContextual[4])
			{
				boolean valor = ra.estaResaltado();
				ra.resaltar(!valor);
				Ventana.thisventana.refrescarFormato();
			}
		}
		
	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	public void mouseExited(MouseEvent e)
	{
		//System.out.println("Evento de raton, mouseExited");
		this.haEntradoAhora=true;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		//System.out.println("Evento de movimiento de raton, mouseMoved");
		this.ratonX=e.getX();
		this.ratonY=e.getY();
		
		this.ratonXAbs=e.getXOnScreen();
		this.ratonYAbs=e.getYOnScreen();
		
		this.ra=cc.getRegistroPosicion((int)(this.ratonX/graph.getScale()),(int)(this.ratonY/graph.getScale()));	
		
		if (e.getButton()==MouseEvent.BUTTON1)	// Boton Izquierda, etiqueta de información
		{
			if (ra!=null && haEntradoAhora)
			{
				this.ef=new EtiquetaFlotante(e.getXOnScreen(),e.getYOnScreen(),ra.getNombreMetodo(),
									ra.getEntradaString(),ra.getSalidaString(),
									ra.entradaVisible(),ra.salidaVisible());
				this.haEntradoAhora=false;
			}
			else if (ra==null)
			{
				//System.out.print("x");
				this.haEntradoAhora=true;
			}
		}
		else if (e.getButton()==MouseEvent.BUTTON2)	// Boton Centro
		{
			
		}
		else if (e.getButton()==MouseEvent.BUTTON3)	// Boton Derecha, menú contextual
		{
			if (ra!=null)
			{
				m = new JPopupMenu(); //JMenu m=new JMenu();
				opcionesMenuContextual=new JMenuItem[5];
				opcionesMenuContextual[0]=new JMenuItem(Texto.get("PARB_DET",Conf.idioma));
				opcionesMenuContextual[1]=new JMenuItem(Texto.get("PARB_NODACT",Conf.idioma));
				opcionesMenuContextual[2]=new JMenuItem(Texto.get("PARB_TEXT",Conf.idioma));
				opcionesMenuContextual[3]=new JMenuItem(
						(this.ra.estaIluminado()?Texto.get("PARB_RESTAURAR",Conf.idioma):Texto.get("PARB_BUSCAR",Conf.idioma)));
				opcionesMenuContextual[4]=new JMenuItem(
						(this.ra.estaResaltado()?Texto.get("PARB_RESTAURAR",Conf.idioma):Texto.get("PARB_RESALTAR",Conf.idioma)));
				
				for (int i=0; i<opcionesMenuContextual.length; i++)
				{
					opcionesMenuContextual[i].addActionListener(this);
					m.add(opcionesMenuContextual[i]);
				}
				
				
				m.updateUI();
				m.show(e.getComponent(),e.getX(), e.getY()); //... mostramos el menu en la ubicacion del raton
			}
		}
			
	}
	
	public void mouseReleased(MouseEvent e)
	{
		//System.out.println("Evento de raton, mouseReleased");
	}
	
	public void mousePressed(MouseEvent e)
	{
		//System.out.println("Evento de raton, mousePressed");
	}

	public void refrescarZoom(int valor)
	{
		//System.out.println("PanelTraza zoom="+valor);
		if (valor==0)
			graph.setScale(this.escalaOriginal);
		else if (valor>0)
		{
			double v=valor;
			v=v/100;
			v=v+1;
			v=v*this.escalaOriginal;
			graph.setScale(v);
		}
		else	// if (valor<0)
		{
			double v=(valor*(-1));
			v=v/100;
			v=1-v;
			v=v*this.escalaOriginal;
			graph.setScale(v);
		}
		escalaActual=graph.getScale();
		//this.visualizar();
		zoom=valor;
	}
	
	public void mouseDragged(MouseEvent e)
	{
		//System.out.println("Evento de movimiento de raton, mouseDragged");
	}

	public void mouseMoved(MouseEvent e)
	{
		//System.out.println("Evento de movimiento de raton, mouseMoved");
	}
	
	public NavegacionListener getNavegacionListener()
	{
		return this.nl;
	}
	

	public static int anchoGrafo()
	{
		return anchoGraph;
	}
	
	public static int altoGrafo()
	{
		return altoGraph;
	}
	

	public void keyTyped(KeyEvent e)
	{

	}

	public void keyPressed(KeyEvent e)
	{

	}

	public void keyReleased(KeyEvent e)
	{

	}

	public JViewport getViewport()
	{
		return this.vp;
	}

	public Object[] getCeldas()
	{
		return this.celdas;
	}

	public JGraph getGrafo()
	{
		return this.graph;
	}
	
	public static void setMinX(int i) {
		minX=i;
	}
	
	public static void setMinY(int i) {
		minY=i;
	}

}

