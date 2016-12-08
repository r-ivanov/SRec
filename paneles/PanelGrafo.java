package paneles;

import grafica.ContenedorPila;
import grafica.EtiquetaFlotante;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import utilidades.NombresYPrefijos;
import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroInfoNodo;
import datos.RegistroActivacion;

/**
 * Panel que contendrá la visualización de la vista de grafo del algoritmo.
 * 
 * @author Daniel Arroyo Cortés
 * @version 2016
 */
class PanelGrafo extends JPanel implements ActionListener, KeyListener,
MouseListener, MouseMotionListener {

//	static final long serialVersionUID = 04;

	private JGraph graph;

	private JPanel panel = new JPanel();
	
	private int zoom = 0;
	
	private double escalaOriginal;
	private double escalaActual;
	
	private int dimXgrafo;
	private int dimYGrafo;
	
	/**
	 * Constructor: crea un nuevo panel de visualización para el grafo.
	 * 
	 * @param nyp
	 *            Nombres Y Prefijos para aplicar.
	 */
	public PanelGrafo() throws Exception {		
		if (Ventana.thisventana.traza != null) {

			this.setLayout(new BorderLayout());

			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,
					new DefaultCellViewFactory());
			this.graph = new JGraph(model, view);			

			this.panel = new JPanel();
			this.panel.add(this.graph);
			this.add(this.panel, BorderLayout.NORTH);

			try {
				this.visualizar();				
			} catch (OutOfMemoryError oome) {
				this.graph = null;
				throw oome;
			} catch (Exception e) {
				throw e;
			}
			
			this.escalaOriginal = this.graph.getScale();
			
			this.dimXgrafo=800;						//	<=========== Modificar
			this.dimYGrafo=600;						//	<=========== Modificar
		} else {
			this.add(new JPanel());
		}
	}

	/**
	 * Visualiza y redibuja la pila en el panel.
	 */
	public void visualizar() {
		if (Ventana.thisventana.traza != null) {
			
			

			
			///////////////////////////////////////////////////////////////////////////////////
			DefaultEdge edge = new DefaultEdge(new String("Holaaaaa"));
			
			
			
			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,	new	DefaultCellViewFactory());
			this.graph = new JGraph(model, view);
			DefaultGraphCell[] cells = new DefaultGraphCell[3];
			cells[0] = new DefaultGraphCell(new String("Hello HELLO HELLO"));
			GraphConstants.setBounds(cells[0].getAttributes(), new Rectangle2D.Double(20,20,40,20));
			GraphConstants.setGradientColor(cells[0].getAttributes(),Color.orange);
			GraphConstants.setOpaque(cells[0].getAttributes(), true);
			GraphConstants.setAutoSize(cells[0].getAttributes(), true);
			GraphConstants.setSelectable(cells[0].getAttributes(),true);
			
			AttributeMap a = cells[0].getAttributes();
			DefaultPort port0 = new DefaultPort();
			cells[0].add(port0);
			cells[1] = new DefaultGraphCell(new String("World"));
			GraphConstants.setBounds(cells[1].getAttributes(), new Rectangle2D.Double(140,140,40,20));
			GraphConstants.setGradientColor(cells[1].getAttributes(),Color.red);
			GraphConstants.setOpaque(cells[1].getAttributes(), true);
			GraphConstants.setSelectable(cells[1].getAttributes(),true);
			DefaultPort port1 = new DefaultPort();
			cells[1].add(port1);
			edge.setSource(cells[0].getChildAt(0));
			edge.setTarget(cells[1].getChildAt(0));
			cells[2] = edge;
			int arrow = GraphConstants.ARROW_CLASSIC;
			GraphConstants.setLineEnd(edge.getAttributes(), arrow);
			GraphConstants.setEndFill(edge.getAttributes(), true);
			// ************
			GraphConstants.setLabelAlongEdge(edge.getAttributes(), true);
			GraphConstants.setMoveable(edge.getAttributes(), false);
			GraphConstants.setLineBegin(edge.getAttributes(), GraphConstants.ARROW_LINE);
			GraphConstants.setLineEnd(edge.getAttributes(), GraphConstants.ARROW_LINE);
			graph.getGraphLayoutCache().insert(cells);
			///////////////////////////////////////////////////////////////////////////////////
			
			
			
//			GraphModel model = new DefaultGraphModel();
//			GraphLayoutCache view = new GraphLayoutCache(model,
//					new DefaultCellViewFactory());
//			this.graph = new JGraph(model, view);

			this.graph.getModel().addGraphModelListener(null);
//			this.cp = new ContenedorPila(Ventana.thisventana.traza.getRaiz(),
//					Ventana.thisventana.traza, this.nyp, 1);
//
//			Object celdas[] = this.cp.getCeldas();
//
//			this.graph.getGraphLayoutCache().insert(celdas);
			this.graph.addMouseListener(this);
			this.graph.setScale(this.escalaActual);
			this.graph.setBackground(Conf.colorPanel);

			this.graph.setPreferredSize(new Dimension(this.dimXgrafo,this.dimYGrafo));
			this.panel.setPreferredSize(new Dimension(this.dimXgrafo,this.dimYGrafo));

			this.panel.addMouseListener(this);

			this.panel.removeAll();
			this.panel.add(this.graph);
			this.panel.setBackground(Conf.colorPanel);
			this.setBackground(Conf.colorPanel);
			this.panel.updateUI();

			this.updateUI();
			
			
			
			

			if (Ventana.thisventana.traza != null) {
				
			}
		}
	}

	
	/**
	 * Devuelve el grafo de la vista.
	 * 
	 * @return Grafo de la vista.
	 */
	public JGraph getGrafo() {
		return this.graph;
	}
	
	/**
	 * Devuelve el nivel de zoom actual.
	 * 
	 * @return Nivel de zoom actual.
	 */
	public int getZoom() {						
		return this.zoom;
	}
	
	/**
	 * Devuelve las dimensiones del grafo.
	 * 
	 * @return Array, donde la posición 0 contiene el máximo ancho, y la
	 *         posición 1 el máximo alto.
	 */
	public int[] dimGrafo() {				//	<=========== Modificar
		int[] dim = new int[2];

		dim[0] = this.dimXgrafo;			
		dim[1] = this.dimYGrafo;

		return dim;
	}
	
	/**
	 * Devuelve las dimensiones del panel y del grafo.
	 * 
	 * @return Array, donde la posición 0 corresponde al ancho del panel , la 1
	 *         al alto del panel, la 2 al ancho del grafo, y la 3 al alto del
	 *         grafo.
	 */
	public int[] dimPanelYGrafoDep() {			//	<=========== Modificar
		int dim[] = new int[4];

		dim[0] = (int) (this.getSize().getWidth()); // Anchura del panel *
		dim[1] = (int) (this.getSize().getHeight()); // Altura del panel *

		dim[2] = this.dimXgrafo; // Anchura
		// del
		// grafo
		dim[3] = this.dimYGrafo; // Altura
		// del
		// grafo

		// * = Las dimensiones del panel sólo son reales si son mayores que las
		// del grafo. Si son
		// menores, entonces siempre da las dimensiones del grafo+10

		return dim;
	}
	
	public void refrescarZoom(int valor) {					//	<=========== Hecho en grafo	
		if (valor == 0) {
			this.graph.setScale(this.escalaOriginal);
		} else if (valor > 0) {
			double v = valor;
			v = v / 100;
			v = v + 1;
			v = v * this.escalaOriginal;
			this.graph.setScale(v);
		} else // if (valor<0)
		{
			double v = (valor * (-1));
			v = v / 100;
			v = 1 - v;
			v = v * this.escalaOriginal;
			this.graph.setScale(v);
		}
		this.escalaActual = this.graph.getScale();
		this.zoom = valor;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	

	

}
