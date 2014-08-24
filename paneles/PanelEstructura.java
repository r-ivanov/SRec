/**
	Panel que contendrá la visualización del algoritmo, ya que almacena la propia traza.
	Está contenido en PanelAlgoritmo.
	
	@author Luis Fernández y Antonio Pérez Carrasco
	@version 2006-2007
*/

package paneles;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jgraph.JGraph;
import org.jgraph.graph.*;

import conf.*;
import datos.*;
import grafica.*;
import utilidades.*;
import ventanas.*;

class PanelEstructura extends JPanel implements MouseListener
{
	static final long serialVersionUID=04;


	private RegistroActivacion registro;
	//private PanelRegistroActivacion panelRaiz;
	
	private JGraph graph;
	DefaultGraphCell cells[];
	ContenedorEstructura ce;

	
	double escalaOriginal;
	double escalaActual;
	NombresYPrefijos nyp=null;
	
	int zoom=0;
	int anchoGraph;
	boolean mostrarNombreMetodos=false;
	
	/**
		Constructor: crea un nuevo panel de traza
		
		@param numPanelesTraza número de panales que se desean
	*/
	public PanelEstructura(NombresYPrefijos nyp) throws Exception
	{
		
		if (nyp!=null)
		{
			this.registro = Ventana.thisventana.traza.getRaiz().getNodoActual();
			this.nyp=nyp;
			
			this.mostrarNombreMetodos = Ventana.thisventana.traza.getNumMetodos()!= 1;
			
			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
			graph = new JGraph(model, view);
			
			escalaOriginal=graph.getScale();
			escalaActual=escalaOriginal;
			//refrescarZoom(-20);
			//System.out.println("PanelPila.Constructor");
			
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
			
		}
		
	}

	/**
		Visualiza y redibuja el árbol en el panel
	*/		
	public void visualizar()
	{
		
		
		
		Conf.calcularDegradadosEstructura(Ventana.thisventana.traza.getAltura(),false);
		
		this.registro = Ventana.thisventana.traza.getRaiz().getNodoActual();
		
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
		graph = new JGraph(model, view);
		
		graph.getModel().addGraphModelListener(null);
		ce=new ContenedorEstructura(this.registro);
		
		Object celdas[]=ce.getCeldas();
		

		//System.out.println("cp.maximoAncho()="+cp.maximoAncho());
		//System.out.println("celdas.length="+celdas.length);
		
		graph.getGraphLayoutCache().insert(celdas);
		graph.addMouseListener(this);
		//System.out.println("Aplicamos escala: "+escalaActual);
		graph.setScale(this.escalaActual);
		graph.setBackground(Conf.colorPanel);
		
		//this.setLayout(bl);
		
		JPanel panel=new JPanel();
		panel.add(graph);
		panel.setBackground(Conf.colorPanel);


		panel.addMouseListener(this);
		panel.setBackground( Conf.colorPanel );
		
		BorderLayout bl = new BorderLayout();
		this.removeAll();
		this.setLayout(bl);
		
		//this.add(panelVacioArriba,BorderLayout.NORTH);
		this.add(panel,BorderLayout.CENTER);
		this.setBackground( Conf.colorPanel );
		
		this.updateUI();

	}
	
	
	
	public int[] dimGrafoVisible()
	{
		int valores[]=new int[2];
		valores[0]=ce.maximoAnchoVisible()+10;//+15
		valores[1]=ce.maximoAltoVisible()+10;
		
		return valores;
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
		
		dim[0]=ce.maximoAncho();
		dim[1]=ce.maximoAlto();
		
		return dim;
	}
	
	
	public int[] dimPanelYGrafo()
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
	
	
	public void mouseEntered(MouseEvent e)
	{
		//System.out.println("Evento de raton, mouseEntered");
	}
	
	public void mouseExited(MouseEvent e)
	{
		//System.out.println("Evento de raton, mouseExited");
	}
	
	public void mouseClicked(MouseEvent e)
	{
		//System.out.println("Evento de raton, mouseClicked");
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
		//System.out.println("PanelEstructura valor="+valor);
		if (valor==0)
			graph.setScale(this.escalaOriginal);
		else if (valor>0)
		{
			double v=valor;
			v=v/100;
			v=v+1;
			//v=v*this.escalaOriginal;
			graph.setScale(v);
			//System.out.println(" >2> PanelEstructura v="+v);
		}
		else	// if (valor<0)
		{
			double v=(valor*(-1));
			v=v/100;
			v=1-v;
			//v=v*this.escalaOriginal;
			graph.setScale(v);
			//System.out.println(" >3> PanelEstructura v="+v);
		}
		escalaActual=graph.getScale();
		//System.out.println("Guardamos escala: "+escalaActual);
		zoom=valor;
	}
	

}

