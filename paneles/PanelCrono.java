/**
	Panel que contendrá la visualización del algoritmo, ya que almacena la propia traza.
	Está contenido en PanelAlgoritmo.
	
	@author Luis Fernández y Antonio Pérez Carrasco
	@version 2006-2007
*/
package paneles;



import java.lang.OutOfMemoryError;


import javax.swing.JMenuItem;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JViewport;


import java.awt.BorderLayout;


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

public class PanelCrono extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener
{
	static final long serialVersionUID=04;


	

	//private PanelRegistroActivacion panelRaiz;
	
	private JGraph graph;		// Grafo árbol principal

	
	GraphModel model = new DefaultGraphModel();
	GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
	
	
	JViewport vp;
	ContenedorCronologica cc;
	
	Object []celdas=new Object[0];
	Object []celdasV;
	
	DefaultGraphCell cuadroVisor;
	
	RegistroActivacion ra;		// Nodo que habremos pinchado con algún botón del ratón
	NombresYPrefijos nyp;
	

	JScrollPane jspArbol = null;
	JScrollPane jspVisor = null;
	
	JPanel visor;
	JPanel panel;
	
	boolean mostrarNombreMetodos=false;
	
	double escalaOriginal;
	double escalaActual;
	
	int zoom=0;
	private static int anchoGraph;
	private static int altoGraph;
	
	//JPopupMenu m;
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
	public PanelCrono(NombresYPrefijos nyp) throws Exception 
	{
		
		this.nyp=nyp;
		if (Ventana.thisventana.traza!=null)
		{
			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
			this.graph = new JGraph(model, view);
			
			this.graph.getModel().addGraphModelListener(null);
			try {
				//Object eE=VentanaVisualizador.thisventana.traza.getRaiz().getEntrada().getEstructura();
				//Object eS=VentanaVisualizador.thisventana.traza.getRaiz().getSalida().getEstructura();				

				cc=new ContenedorCronologica(this.nyp);
				anchoGraph=cc.maximoAncho();		// Sólo es necesario hacerlo una vez
				graph.getGraphLayoutCache().insert(cc.getCeldas());

				altoGraph=cc.maximoAlto();			// Sólo es necesario hacerlo una vez
				this.celdas=cc.getCeldas();
				this.graph.getGraphLayoutCache().insert(this.celdas);
				this.graph.addMouseListener(this);
				this.graph.addMouseMotionListener(this);
				this.graph.setBackground(Conf.colorPanel);
				
				BorderLayout bl = new BorderLayout();
				this.setLayout(bl);
				escalaOriginal=	this.graph.getScale();	// Sólo es necesario hacerlo una vez
				
				this.panel=new JPanel();
				//this.panel.setLayout(new BorderLayout());
				this.panel.add(graph);//,BorderLayout.NORTH);
				this.add(panel,BorderLayout.NORTH);
				//this.setBackground(Conf.colorPanel);
				
				try {
					this.visualizar();	
					//refrescarZoom(Conf.zoomPila);
				} catch (OutOfMemoryError oome) {
					graph=null;
					throw oome;
				} catch (Exception e) {
					throw e;
				}
				
			
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
			//vp=jspArbol.getViewport();//new JViewport();
		}
		
	}

	
	
	
	
	/**
		Visualiza y redibuja el árbol en el panel
		
		@param redimension true cuando redibujamos el panel tras una redimension o variacion de zoom
		@param recalcular true cuando hay que redibujar el árbol
	*/		
	public void visualizar()
	{
		if (Ventana.thisventana.traza!=null)
		{
			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
			this.graph = new JGraph(model, view);
			
			this.graph.getModel().addGraphModelListener(null);
			try {
				cc=new ContenedorCronologica(this.nyp);
				//anchoGraph=cc.maximoAncho();		// Sólo es necesario hacerlo una vez
				//graph.getGraphLayoutCache().insert(cc.getCeldas());

		
				
				//altoGraph=cc.maximoAlto();			// Sólo es necesario hacerlo una vez
				this.celdas=cc.getCeldas();
				this.graph.getGraphLayoutCache().insert(this.celdas);
				this.graph.addMouseListener(this);
				this.graph.setScale(this.escalaActual);
				//this.graph.addMouseMotionListener(this);
				this.graph.setBackground(Conf.colorPanel);
				
				this.setBackground(Conf.colorPanel);
				
				BorderLayout bl = new BorderLayout();
				this.setLayout(bl);
				//escalaOriginal=	this.graph.getScale();	// Sólo es necesario hacerlo una vez
				
			
				
				this.panel=new JPanel();
				this.panel.add(graph);//,BorderLayout.NORTH);
				this.panel.setBackground(Conf.colorPanel);
				this.removeAll();
				this.add(panel,BorderLayout.NORTH);
				
				this.panel.updateUI();
				
				this.updateUI();
				
			} catch (OutOfMemoryError oome) {
				cc=null;
				this.graph=null;
				throw oome;
			} catch (Exception e) {
				cc=null;
			}
		}
		else
		{
			this.add(new JPanel(),BorderLayout.CENTER);
			
			jspArbol=new JScrollPane();
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
		valores[1]=cc.maximoAltoVisible()+10;
		
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
				this.ef=new EtiquetaFlotante(e.getXOnScreen()-30,e.getYOnScreen()-30,ra.getNombreMetodo(),
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
			/*if (ra!=null)
			{
				m = new JPopupMenu(); //JMenu m=new JMenu();
				opcionesMenuContextual=new JMenuItem[4];
				opcionesMenuContextual[0]=new JMenuItem(Texto.get("PARB_DET",Conf.idioma));
				opcionesMenuContextual[1]=new JMenuItem(Texto.get("PARB_NODACT",Conf.idioma));
				opcionesMenuContextual[2]=new JMenuItem(Texto.get("PARB_TEXT",Conf.idioma));
				opcionesMenuContextual[3]=new JMenuItem(
						(this.ra.estaIluminado()?Texto.get("PARB_RESTAURAR",Conf.idioma):Texto.get("PARB_BUSCAR",Conf.idioma)));
				
				for (int i=0; i<opcionesMenuContextual.length; i++)
				{
					opcionesMenuContextual[i].addActionListener(this);
					m.add(opcionesMenuContextual[i]);
				}
				
				
				m.updateUI();
				m.show(e.getComponent(),e.getX(), e.getY()); //... mostramos el menu en la ubicacion del raton
			}*/
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
		this.escalaActual=graph.getScale();
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


}

