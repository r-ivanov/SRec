package paneles;

import grafica.ContenedorPila;
import grafica.EtiquetaFlotante;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import utilidades.NombresYPrefijos;
import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroInfoNodo;
import datos.RegistroActivacion;

/**
 * Panel que contendrá la visualización de la vista de Pila del algoritmo.
 * 
 * @author Luis Fernández y Antonio Pérez Carrasco
 * @version 2006-2007
 */
class PanelPila extends JPanel implements ActionListener, KeyListener,
MouseListener, MouseMotionListener {

	static final long serialVersionUID = 04;

	private JGraph graph;
	private ContenedorPila cp;

	private double escalaOriginal;
	private double escalaActual;
	private NombresYPrefijos nyp = null;

	private JPanel panel = new JPanel();

	private int zoom = 0;

	private RegistroActivacion ra;
	private boolean haEntradoAhora = true;
	private JPopupMenu m;
	private JMenuItem opcionesMenuContextual[];
	private int ratonX = 0, ratonY = 0, ratonXAbs = 0, ratonYAbs = 0;

	/**
	 * Constructor: crea un nuevo panel de visualización Para la pila.
	 * 
	 * @param nyp
	 *            Nombres Y Prefijos para aplicar.
	 */
	public PanelPila(NombresYPrefijos nyp) throws Exception {
		this.nyp = nyp;
		if (Ventana.thisventana.traza != null) {

			this.setLayout(new BorderLayout());

			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,
					new DefaultCellViewFactory());
			this.graph = new JGraph(model, view);

			this.escalaOriginal = this.graph.getScale();

			this.panel = new JPanel();
			this.panel.add(this.graph);
			this.add(this.panel, BorderLayout.NORTH);

			try {
				this.visualizar();
				this.refrescarZoom(Conf.zoomPila);
			} catch (OutOfMemoryError oome) {
				this.graph = null;
				throw oome;
			} catch (Exception e) {
				throw e;
			}
		} else {
			this.add(new JPanel());
		}
	}

	/**
	 * Visualiza y redibuja la pila en el panel.
	 */
	public void visualizar() {
		if (Ventana.thisventana.traza != null) {
			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,
					new DefaultCellViewFactory());
			this.graph = new JGraph(model, view);

			this.graph.getModel().addGraphModelListener(null);
			this.cp = new ContenedorPila(Ventana.thisventana.traza.getRaiz(),
					Ventana.thisventana.traza, this.nyp, 1);

			Object celdas[] = this.cp.getCeldas();

			this.graph.getGraphLayoutCache().insert(celdas);
			this.graph.addMouseListener(this);
			this.graph.setScale(this.escalaActual);
			this.graph.setBackground(Conf.colorPanel);

			this.graph.setPreferredSize(new Dimension((int) (this.cp
					.maximoAncho() * Math.max(1, this.escalaActual)),
					((int) (this.graph.getPreferredSize().getHeight()))
					* ((int) (Math.max(1, this.escalaActual)))));
			this.panel.setPreferredSize(new Dimension(
					(this.cp.maximoAncho() + 10)
					* (int) (Math.max(1, this.escalaActual)),
					(int) (this.graph.getPreferredSize().getHeight() + 10)
					* (int) (Math.max(1, this.escalaActual))));

			this.panel.addMouseListener(this);

			this.panel.removeAll();
			this.panel.add(this.graph);
			this.panel.setBackground(Conf.colorPanel);
			this.setBackground(Conf.colorPanel);
			this.panel.updateUI();

			this.updateUI();
		}
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
	 * Devuelve el grafo de la vista.
	 * 
	 * @return Grafo de la vista.
	 */
	public JGraph getGrafo() {
		return this.graph;
	}

	/**
	 * Devuelve las dimensiones del grafo.
	 * 
	 * @return Array, donde la posición 0 contiene el máximo ancho, y la
	 *         posición 1 el máximo alto.
	 */
	public int[] dimGrafo() {
		int[] dim = new int[2];

		dim[0] = this.cp.maximoAncho();
		dim[1] = this.cp.maximoAlto();

		return dim;
	}

	/**
	 * Devuelve las dimensiones del panel y del grafo.
	 * 
	 * @return Array, donde la posición 0 corresponde al ancho del panel , la 1
	 *         al alto del panel, la 2 al ancho del grafo, y la 3 al alto del
	 *         grafo.
	 */
	public int[] dimPanelYPila() {
		int dim[] = new int[4];

		dim[0] = (int) (this.getSize().getWidth()); // Anchura del panel *
		dim[1] = (int) (this.getSize().getHeight()); // Altura del panel *

		dim[2] = (int) (this.graph.getPreferredSize().getWidth()); // Anchura
		// del
		// grafo
		dim[3] = (int) (this.graph.getPreferredSize().getHeight()); // Altura
		// del
		// grafo

		// * = Las dimensiones del panel sólo son reales si son mayores que las
		// del grafo. Si son
		// menores, entonces siempre da las dimensiones del grafo+10

		return dim;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			JMenuItem item = (JMenuItem) e.getSource();

			// Menú contextual: primera opción: etiqueta detallada
			if (item == this.opcionesMenuContextual[0]) {
				if (this.ra != null && this.haEntradoAhora) {
					new EtiquetaFlotante(this.ratonXAbs, this.ratonYAbs,
							this.ra.getNombreMetodo(),
							this.ra.getEntradaCompletaString(),
							this.ra.getSalidaCompletaString(),
							this.ra.entradaVisible(), this.ra.salidaVisible());
					this.haEntradoAhora = false;
				} else if (this.ra == null) {
					this.haEntradoAhora = true;
				}
			}
			// Menú contextual: segunda opción: hacer nodo actual
			else if (item == this.opcionesMenuContextual[1]) {
				if (this.ra != null) {
					this.ra.hacerNodoActual();
					Ventana.thisventana.refrescarOpciones();
				}
			}
			// Menú contextual: tercera opción: cuadro de inforamción sobre nodo
			else if (item == this.opcionesMenuContextual[2]) {
				new CuadroInfoNodo(Ventana.thisventana, this.ra);
			}
			// Menú contextual: cuarta opción: seleccionar/no seleccionar
			else if (item == this.opcionesMenuContextual[3]) {
				boolean valor = !this.ra.estaIluminado();
				if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
					Ventana.thisventana.getTraza().iluminar(
							this.ra.getNumMetodo(),
							this.ra.getEntradasString(), null, valor);
				} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
					Ventana.thisventana.getTraza().iluminar(
							this.ra.getNumMetodo(), null,
							this.ra.getSalidasString(), valor);
				} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {
					Ventana.thisventana.getTraza().iluminar(
							this.ra.getNumMetodo(),
							this.ra.getEntradasString(),
							this.ra.getSalidasString(), valor);
				}

				Ventana.thisventana.refrescarFormato();
			}
			// Menú contextual: quinta opción: resaltar nodo
			else if (item == this.opcionesMenuContextual[4]) {
				boolean valor = this.ra.estaResaltado();
				this.ra.resaltar(!valor);
				Ventana.thisventana.refrescarFormato();
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.haEntradoAhora = true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.ratonX = e.getX();
		this.ratonY = e.getY();

		this.ratonXAbs = e.getXOnScreen();
		this.ratonYAbs = e.getYOnScreen();

		this.ra = this.cp.getRegistroPosicion(
				(int) (this.ratonX / this.graph.getScale()),
				(int) (this.ratonY / this.graph.getScale()));

		if (e.getButton() == MouseEvent.BUTTON1) // Boton Izquierda, etiqueta de
			// información
		{
			if (this.ra != null && this.haEntradoAhora) {
				new EtiquetaFlotante(e.getXOnScreen(), e.getYOnScreen(),
						this.ra.getNombreMetodo(), this.ra.getEntradaString(),
						this.ra.getSalidaString(), this.ra.entradaVisible(),
						this.ra.salidaVisible());
				this.haEntradoAhora = false;
			} else if (this.ra == null) {
				// System.out.print("x");
				this.haEntradoAhora = true;
			}
		} else if (e.getButton() == MouseEvent.BUTTON2) // Boton Centro
		{

		} else if (e.getButton() == MouseEvent.BUTTON3) // Boton Derecha, menú
			// contextual
		{
			if (this.ra != null) {
				this.m = new JPopupMenu();
				this.opcionesMenuContextual = new JMenuItem[5];
				this.opcionesMenuContextual[0] = new JMenuItem(Texto.get(
						"PARB_DET", Conf.idioma));
				this.opcionesMenuContextual[1] = new JMenuItem(Texto.get(
						"PARB_NODACT", Conf.idioma));
				this.opcionesMenuContextual[2] = new JMenuItem(Texto.get(
						"PARB_TEXT", Conf.idioma));
				this.opcionesMenuContextual[3] = new JMenuItem(
						(this.ra.estaIluminado() ? Texto.get("PARB_RESTAURAR",
								Conf.idioma) : Texto.get("PARB_BUSCAR",
										Conf.idioma)));
				this.opcionesMenuContextual[4] = new JMenuItem(
						(this.ra.estaResaltado() ? Texto.get("PARB_RESTAURAR",
								Conf.idioma) : Texto.get("PARB_RESALTAR",
										Conf.idioma)));

				for (int i = 0; i < this.opcionesMenuContextual.length; i++) {
					this.opcionesMenuContextual[i].addActionListener(this);
					this.m.add(this.opcionesMenuContextual[i]);
				}

				this.m.updateUI();
				this.m.show(e.getComponent(), e.getX(), e.getY()); // ...
				// mostramos
				// el menu en la
				// ubicacion del
				// raton
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	public void refrescarZoom(int valor) {
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
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

}
