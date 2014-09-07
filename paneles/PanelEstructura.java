package paneles;

import grafica.ContenedorEstructura;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import utilidades.NombresYPrefijos;
import ventanas.Ventana;
import conf.Conf;
import datos.RegistroActivacion;

/**
 * Panel que contendrá la visualización de la estructura algoritmo, para vistas
 * DYV.
 * 
 * @author Luis Fernández y Antonio Pérez Carrasco
 * @version 2006-2007
 */
class PanelEstructura extends JPanel implements MouseListener {

	static final long serialVersionUID = 04;

	private RegistroActivacion registro;

	private JGraph graph;
	private ContenedorEstructura ce;

	private double escalaOriginal;
	private double escalaActual;

	private int zoom = 0;

	/**
	 * Constructor: crea un nuevo panel para la visualización de la estructura DYV.
	 * 
	 * @param nyp
	 *            Nombres y Prefijos para mostrar.
	 */
	public PanelEstructura(NombresYPrefijos nyp) throws Exception {

		if (nyp != null) {
			this.registro = Ventana.thisventana.traza.getRaiz().getNodoActual();

			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,
					new DefaultCellViewFactory());
			graph = new JGraph(model, view);

			escalaOriginal = graph.getScale();
			escalaActual = escalaOriginal;

			try {
				this.visualizar();
				refrescarZoom(Conf.zoomPila);

			} catch (OutOfMemoryError oome) {
				graph = null;
				throw oome;
			} catch (Exception e) {
				throw e;
			}
		} else {

		}

	}

	/**
	 * Visualiza y redibuja la estructura en el panel
	 */
	public void visualizar() {

		Conf.calcularDegradadosEstructura(
				Ventana.thisventana.traza.getAltura(), false);

		this.registro = Ventana.thisventana.traza.getRaiz().getNodoActual();

		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,
				new DefaultCellViewFactory());
		graph = new JGraph(model, view);

		graph.getModel().addGraphModelListener(null);
		ce = new ContenedorEstructura(this.registro);

		Object celdas[] = ce.getCeldas();

		graph.getGraphLayoutCache().insert(celdas);
		graph.addMouseListener(this);
		graph.setScale(this.escalaActual);
		graph.setBackground(Conf.colorPanel);

		JPanel panel = new JPanel();
		panel.add(graph);
		panel.setBackground(Conf.colorPanel);

		panel.addMouseListener(this);
		panel.setBackground(Conf.colorPanel);

		BorderLayout bl = new BorderLayout();
		this.removeAll();
		this.setLayout(bl);

		this.add(panel, BorderLayout.CENTER);
		this.setBackground(Conf.colorPanel);

		this.updateUI();

	}

	/**
	 * Devuelve las dimensiones del grafo visible.
	 * 
	 * @return Array, donde la posición 0 corresponde al ancho, y la 1 al alto.
	 */
	public int[] dimGrafoVisible() {
		int valores[] = new int[2];
		valores[0] = ce.maximoAnchoVisible() + 10;
		valores[1] = ce.maximoAltoVisible() + 10;

		return valores;
	}

	/**
	 * Devuelve el nivel de zoom actual.
	 * 
	 * @return Nivel de zoom actual.
	 */
	public int getZoom() {
		return zoom;
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
	 * Devuelve las dimensiones del panel y del grafo.
	 * 
	 * @return Array, donde la posición 0 corresponde al ancho del panel , la 1
	 *         al alto del panel, la 2 al ancho del grafo, y la 3 al alto del
	 *         grafo.
	 */
	public int[] dimPanelYGrafo() {
		int dim[] = new int[4];

		dim[0] = (int) (this.getSize().getWidth()); // Anchura del panel *
		dim[1] = (int) (this.getSize().getHeight()); // Altura del panel *

		dim[2] = (int) (graph.getPreferredSize().getWidth()); // Anchura del
		// grafo
		dim[3] = (int) (graph.getPreferredSize().getHeight()); // Altura del
		// grafo

		// * = Las dimensiones del panel sólo son reales si son mayores que las
		// del grafo. Si son
		// menores, entonces siempre da las dimensiones del grafo+10

		return dim;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Actualiza el valor actual del zoom.
	 * 
	 * @param valor
	 *            Nuevo valor para el zoom.
	 */
	public void refrescarZoom(int valor) {
		if (valor == 0) {
			graph.setScale(this.escalaOriginal);
		} else if (valor > 0) {
			double v = valor;
			v = v / 100;
			v = v + 1;
			graph.setScale(v);
		} else {
			double v = (valor * (-1));
			v = v / 100;
			v = 1 - v;
			graph.setScale(v);
		}
		escalaActual = graph.getScale();
		zoom = valor;
	}

}
