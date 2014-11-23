package paneles;

import grafica.ContenedorArbol;
import grafica.ContenedorGrafoDependencia;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import utilidades.NombresYPrefijos;
import utilidades.ServiciosString;
import ventanas.Ventana;
import conf.Conf;

/**
 * Panel que contendrá el grafo de dependencia del algoritmo.
 * 
 * @author David Pastor Herranz
 */
public class PanelGrafoDependencia extends JPanel implements ActionListener,
		KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -8913344374512057051L;

	private JGraph graph; // Grafo principal

	private JViewport vp;
	private ContenedorGrafoDependencia cc;

	private Object[] celdas;

	private NombresYPrefijos nyp;

	private JScrollPane jspArbol = null;

	private boolean mostrarNombreMetodos = false;

	private int anchoGraph;
	private int altoGraph;

	/**
	 * Constructor: crea un nuevo PanelArbol
	 * 
	 * @param nyp
	 *            NombresYPrefijos para la visualización de métodos.
	 */
	public PanelGrafoDependencia() throws Exception {

		this.nyp = null;
		this.mostrarNombreMetodos = Ventana.thisventana.traza.getNumMetodos() != 1;
		if (this.mostrarNombreMetodos) {
			this.nyp = new NombresYPrefijos();
			String[] nombresMetodos = Ventana.thisventana.trazaCompleta
					.getNombresMetodos();
			String prefijos[] = ServiciosString.obtenerPrefijos(nombresMetodos);
			for (int i = 0; i < nombresMetodos.length; i++) {
				this.nyp.add(nombresMetodos[i], prefijos[i]);
			}
		}

		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,
				new DefaultCellViewFactory());
		this.graph = new JGraph(model, view);
		this.graph.getModel().addGraphModelListener(null);
		try {
			this.cc = new ContenedorGrafoDependencia(Ventana.thisventana.traza.getRaiz(),
					this.graph, this.nyp, 1);
			this.anchoGraph = this.cc.maximoAncho(); // Sólo es necesario
			// hacerlo
			// una vez
			this.altoGraph = this.cc.maximoAlto(); // Sólo es necesario hacerlo
			// una vez
			this.celdas = this.cc.getCeldas();
			this.graph.getGraphLayoutCache().insert(this.celdas);
			this.graph.addMouseListener(this);
			this.graph.addMouseMotionListener(this);
			this.graph.setBackground(Conf.colorPanel);

			BorderLayout bl = new BorderLayout();
			this.setLayout(bl);

			this.jspArbol = new JScrollPane();

			this.vp = this.jspArbol.getViewport();
			this.jspArbol.setBorder(new EmptyBorder(0, 0, 0, 0));
			this.vp.add(this.graph);

			this.add(this.jspArbol, BorderLayout.CENTER);

		} catch (OutOfMemoryError oome) {
			this.cc = null;
			this.graph = null;
			throw oome;
		} catch (Exception e) {
			this.cc = null;
			throw e;
		}
	}

	/**
	 * Devuelve las dimensiones del panel y del grafo.
	 * 
	 * @return {anchura_panel, altura_panel, anchura_grafo, altura_grafo}
	 */
	public int[] dimPanelYGrafo() {
		int dim[] = new int[4];

		dim[0] = (this.getWidth()); // Anchura del panel *
		dim[1] = (this.getHeight()); // Altura del panel *

		dim[2] = (int) (this.graph.getPreferredSize().getWidth()); // Anchura
		// del grafo
		dim[3] = (int) (this.graph.getPreferredSize().getHeight()); // Altura
		// del grafo

		// * = Las dimensiones del panel sólo son reales si son mayores que las
		// del grafo. Si son
		// menores, entonces siempre da las dimensiones del grafo+10

		return dim;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

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

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	/**
	 * Devuelve el ancho del grafo.
	 * 
	 * @return Ancho del grafo.
	 */
	public int anchoGrafo() {
		return this.anchoGraph;
	}

	/**
	 * Devuelve el alto del grafo.
	 * 
	 * @return Alto del grafo.
	 */
	public int altoGrafo() {
		return this.altoGraph;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
