/**
	Panel que contendrá la visualización del algoritmo, ya que almacena la propia traza.
	Está contenido en PanelAlgoritmo.
	
	@author Luis Fernández y Antonio Pérez Carrasco
	@version 2006-2007
*/

package paneles;


import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.awt.BorderLayout;

import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.jgraph.JGraph;
import org.jgraph.graph.*;


import conf.*;
import cuadros.*;
import datos.*;
import eventos.*;
import grafica.*;
import utilidades.*;
import ventanas.*;

class PanelPila extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener 
{
	static final long serialVersionUID=04;


	//private PanelRegistroActivacion panelRaiz;
	
	private JGraph graph;
	DefaultGraphCell cells[];
	ContenedorPila cp;
	
	double escalaOriginal;
	double escalaActual;
	NombresYPrefijos nyp=null;
	
	JPanel panel=new JPanel();
	
	int zoom=0;
	int anchoGraph;
	boolean mostrarNombreMetodos=false;
	
	RegistroActivacion ra;
	boolean haEntradoAhora=true; 
	EtiquetaFlotante ef;
	JPopupMenu m;
	JMenuItem opcionesMenuContextual[];
	int ratonX=0, ratonY=0, ratonXAbs=0, ratonYAbs=0;
	
	/**
		Constructor: crea un nuevo panel de traza
		
		@param numPanelesTraza número de panales que se desean
	*/
	public PanelPila(NombresYPrefijos nyp) throws Exception
	{
		this.nyp=nyp;
		if (Ventana.thisventana.traza!=null)
		{
			this.mostrarNombreMetodos = Ventana.thisventana.traza.getNumMetodos()!= 1;
			
			this.setLayout(new BorderLayout());

			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
			graph = new JGraph(model, view);
			
			escalaOriginal=graph.getScale();

			this.panel=new JPanel();
			//this.panel.setLayout(new BorderLayout());
			this.panel.add(graph);//,BorderLayout.NORTH);
			this.add(panel,BorderLayout.NORTH);
			//this.setBackground(Conf.colorPanel);
			
			try {
				this.visualizar();	
				refrescarZoom(Conf.zoomPila);
			} catch (OutOfMemoryError oome) {
				graph=null;
				throw oome;
			} catch (Exception e) {
				throw e;
			}
		}
		else
		{
			this.add(new JPanel());
		}
	}

	/**
		Visualiza y redibuja el árbol en el panel
	*/		
	public void visualizar()
	{
		if (Ventana.thisventana.traza!=null)
		{
			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
			graph = new JGraph(model, view);
			
			graph.getModel().addGraphModelListener(null);
			cp=new ContenedorPila(Ventana.thisventana.traza.getRaiz(),graph,Ventana.thisventana.traza,this.nyp,1);
			
			Object celdas[]=cp.getCeldas();
			
			graph.getGraphLayoutCache().insert(celdas);
			graph.addMouseListener(this);
			graph.setScale(this.escalaActual);
			graph.setBackground(Conf.colorPanel);
			
			graph.setPreferredSize( new Dimension (   (int)(cp.maximoAncho()*Math.max(1,escalaActual)),
									((int)(graph.getPreferredSize().getHeight())) * ((int)(Math.max(1,escalaActual)))   ) );
			this.panel.setPreferredSize(new Dimension(	(cp.maximoAncho()+10)*(int)(Math.max(1,escalaActual)),
									(int)(graph.getPreferredSize().getHeight()+10)*(int)(Math.max(1,escalaActual))));
			
			/*
			System.out.println("----DIMENSIONES----");
			System.out.println("GRAPH: "+graph.getPreferredSize().getWidth()+"x"+graph.getPreferredSize().getHeight());
			System.out.println("PANEL: "+panel.getPreferredSize().getWidth()+"x"+panel.getPreferredSize().getHeight());
			*/
			
			
			this.panel.addMouseListener(this);
			
			this.panel.removeAll();
			this.panel.add(graph);//,BorderLayout.NORTH);
			this.panel.setBackground( Conf.colorPanel );
			this.setBackground(Conf.colorPanel);
			this.panel.updateUI();
			
			this.updateUI();
		}
	}
	
	
	

	
	public int ancho()
	{
		return anchoGraph;
	}
	
	public int getZoom()
	{
		return zoom;
	}
	
	public JGraph getGrafo()
	{
		return this.graph;
	}
	public int[] dimGrafo()
	{
		int[]dim=new int[2];
		
		dim[0]=cp.maximoAncho();
		dim[1]=cp.maximoAlto();
		
		return dim;
	}
	
	
	public int[] dimPanelYPila()
	{
		int dim[]=new int[4];
		
		dim[0]=(int)(this.getSize().getWidth());					// Anchura del panel *
		dim[1]=(int)(this.getSize().getHeight());					// Altura del panel	*
		
		dim[2]=(int)(graph.getPreferredSize().getWidth());			// Anchura del grafo
		dim[3]=(int)(graph.getPreferredSize().getHeight());			// Altura del grafo
		
		// * = Las dimensiones del panel sólo son reales si son mayores que las del grafo. Si son
		//     menores, entonces siempre da las dimensiones del grafo+10
		
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
				new CuadroInfoNodo(Ventana.thisventana.traza,ra);
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
		//System.out.println("Evento de raton, mouseEntered");
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
		
		this.ra=cp.getRegistroPosicion((int)(this.ratonX/graph.getScale()),(int)(this.ratonY/graph.getScale()));	
		
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
		zoom=valor;
	}

	public void mouseDragged(MouseEvent arg0) {

	}

	public void mouseMoved(MouseEvent arg0) {
	
	}

	public void keyPressed(KeyEvent arg0) {
	
	}

	public void keyReleased(KeyEvent arg0) {

	}

	public void keyTyped(KeyEvent arg0) {

	}
	

}

