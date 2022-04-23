package paneles;

import grafica.ContenedorArbol;
import grafica.EtiquetaFlotante;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import utilidades.Arrays;
import utilidades.NombresYPrefijos;
import utilidades.ServiciosString;
import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroInfoNodo;
import datos.MetodoAlgoritmo;
import datos.RegistroActivacion;
import eventos.NavegacionListener;

/**
 * Panel que contendrá la visualización del algoritmo, ya que almacena la propia
 * traza. Está contenido en PanelAlgoritmo.
 * 
 * @author Luis Fernández y Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class PanelArbol extends JPanel implements ActionListener, KeyListener,
MouseListener, MouseMotionListener {
	static final long serialVersionUID = 04;

	// private PanelRegistroActivacion panelRaiz;

	private JGraph graph; // Grafo árbol principal
	private JGraph g; // Grafo visor

	private JViewport vp;
	private ContenedorArbol cc;

	private Object[] celdas = new Object[0];
	private Object[] celdasV;

	private DefaultGraphCell cuadroVisor;

	private RegistroActivacion ra; // Nodo que habremos pinchado con algún botón
									// del
	// ratón
	private NombresYPrefijos nyp;

	private JScrollPane jspArbol = null;
	private JScrollPane jspVisor = null;

	private JPanel visor;

	public static double ESCALAVISOR = 0.10;
	public static double escala = 0.10; // Escala variable para ajustar la
	// miniatura al tamaño del panel

	private boolean mostrarNombreMetodos = false;

	private double escalaOriginal;
	private double escalaActual;

	private int zoom = 0;
	private static int anchoGraph;
	private static int altoGraph;

	// Coordenadas para la visualización dinamica del arbol
	private static int minX = 0;
	private static int minY = 0;

	// Anchos y altos minimos y maximos para saber si la siguiente celda esta
	// dentro del visor actual
	private int anMin = -1;
	private int anMax = -1;
	private int alMin = -1;
	private int alMax = -1;

	private JPopupMenu m;
	private JMenuItem opcionesMenuContextual[];

	private int ratonX = 0, ratonY = 0, ratonXAbs = 0, ratonYAbs = 0;

	private NavegacionListener nl = null;

	private boolean haEntradoAhora = true;
	// true: la proxima vez que se mueva dentro de un nodo, significará que
	// acaba de entrar en él, mostrará información
	// false: la proxima vez que se mueva dentro de un nodo, significará que ya
	// estaba dentro de él, no hacemos nada
	
	// Para Vista de Rama de Valores
	private PanelValoresRamaAABB pValRama;


	/**
	 * Constructor: crea un nuevo PanelArbol
	 * 
	 * @param nyp
	 *            NombresYPrefijos para la visualización de métodos.
	 */
	public PanelArbol(NombresYPrefijos nyp, PanelValoresRamaAABB pValRama) throws Exception {
		this.nyp = nyp;
		this.pValRama = pValRama;

		if (Ventana.thisventana.traza != null) {
			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,
					new DefaultCellViewFactory());
			this.graph = new JGraph(model, view);

			this.graph.getModel().addGraphModelListener(null);
			try {

				this.cc = new ContenedorArbol(
						Ventana.thisventana.traza.getRaiz(), this.graph,
						this.nyp, 1, getFontMetrics(getFont()).getFontRenderContext());
				anchoGraph = this.cc.maximoAncho(); // Sólo es necesario hacerlo
				// una vez
				altoGraph = this.cc.maximoAlto(); // Sólo es necesario hacerlo
				// una vez
				this.celdas = this.cc.getCeldas();
				this.graph.getGraphLayoutCache().insert(this.celdas);
				this.graph.addMouseListener(this);
				this.graph.addMouseMotionListener(this);
				this.graph.setBackground(Conf.colorPanel);

				BorderLayout bl = new BorderLayout();
				this.setLayout(bl);
				this.escalaOriginal = this.graph.getScale(); // Sólo es
				// necesario
				// hacerlo una
				// vez

				this.jspArbol = new JScrollPane();

				this.vp = this.jspArbol.getViewport();
				this.jspArbol.setBorder(new EmptyBorder(0, 0, 0, 0));
				this.vp.add(this.graph);

				this.add(this.jspArbol, BorderLayout.CENTER);

				this.visor = this.crearVisor(this.cc.getCeldas(), this.graph);
				escala = this.calculaEscalaMiniatura();
				this.visor.setPreferredSize(new Dimension(
						(int) (anchoGraph * escala),
						(int) (altoGraph * escala) + 25));

				this.visor.updateUI();
				this.jspVisor = new JScrollPane(this.visor);
				// Si se ajusta la vista global al espacio disponible, quitamos
				// las barras de desplazamiento
				if (Conf.ajustarVistaGlobal) {
					this.jspVisor
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
					this.jspVisor
					.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				} else {
					this.jspVisor
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
					this.jspVisor
					.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				}

				if (Conf.mostrarVisor && this.jspVisor != null) {
					this.add(this.jspVisor, BorderLayout.SOUTH);
				}

				this.cuadroVisor = ContenedorArbol.crearCuadroVisor(400, 400,
						0, 0);
				this.refrescarZoom(Conf.zoomArbol);

			} catch (OutOfMemoryError oome) {
				this.cc = null;
				this.graph = null;
				throw oome;
			} catch (Exception e) {
				this.cc = null;
				throw e;
			}
		} else {
			this.add(new JPanel(), BorderLayout.CENTER);

			this.jspArbol = new JScrollPane();
			this.jspArbol.setBorder(new EmptyBorder(0, 0, 0, 0));
			this.vp = this.jspArbol.getViewport();
		}

		// Gestionamos eventos
		this.nl = new NavegacionListener(this.vp, this.celdasV, this);

		// Gestionamos eventos (nuevo)
		this.nl.setAtributos(this.vp, this.celdasV, this.escalaActual);

		this.jspArbol.getHorizontalScrollBar().getComponent(0)
		.addMouseListener(this.nl);
		this.jspArbol.getHorizontalScrollBar().getComponent(1)
		.addMouseListener(this.nl);
		this.jspArbol.getVerticalScrollBar().getComponent(0)
		.addMouseListener(this.nl);
		this.jspArbol.getVerticalScrollBar().getComponent(1)
		.addMouseListener(this.nl);
		this.jspArbol.getHorizontalScrollBar().addMouseWheelListener(this.nl);
		this.jspArbol.getVerticalScrollBar().addMouseWheelListener(this.nl);
		this.jspArbol.getHorizontalScrollBar().addMouseMotionListener(this.nl);
		this.jspArbol.getVerticalScrollBar().addMouseMotionListener(this.nl);
		this.jspArbol.getHorizontalScrollBar().addMouseListener(this.nl);
		this.jspArbol.getVerticalScrollBar().addMouseListener(this.nl);

	}

	/**
	 * Constructor: crea un nuevo PanelArbol
	 * 
	 * @param String
	 *            ubicación del fichero GIF.
	 * @param imagen
	 *            Imagen para mostrar.
	 */
	public PanelArbol(String ficheroGIF, ImageIcon imagen) throws Exception {
		try {

			BorderLayout bl = new BorderLayout();
			this.setLayout(bl);

			BufferedImage image = ImageIO.read(new File(ficheroGIF));
			int c = image.getRGB(3, 3);
			int red = (c & 0x00ff0000) >> 16;
			int green = (c & 0x0000ff00) >> 8;
			int blue = c & 0x000000ff;

			JPanel panel = new JPanel();
			panel.add(new JLabel(imagen));
			panel.setBackground(new Color(red, green, blue));

			this.jspArbol = new JScrollPane(panel);
			this.jspArbol.setBorder(new EmptyBorder(0, 0, 0, 0));
			this.add(this.jspArbol, BorderLayout.CENTER);

		} catch (Exception e) {
			this.cc = null;
			throw e;
		}

	}

	/**
	 * Visualiza y redibuja el árbol en el panel
	 * 
	 * @param redimension
	 *            true cuando redibujamos el panel tras una redimension o
	 *            variacion de zoom
	 * @param recalcular
	 *            true cuando hay que redibujar el árbol
	 * @param movimientoVisor
	 *            true para desplazar el visor.
	 */
	public void visualizar(boolean redimension, boolean recalcular,
			boolean movimientoVisor) {
		// Si se ajusta la vista global al espacio disponible, quitamos las
		// barras de desplazamiento
		if (Conf.ajustarVistaGlobal) {
			this.jspVisor
			.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			this.jspVisor
			.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		} else {
			this.jspVisor
			.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			this.jspVisor
			.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		if (Ventana.thisventana.traza != null) {

			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,
					new DefaultCellViewFactory());
			this.graph = new JGraph(model, view);

			this.graph.getModel().addGraphModelListener(null);

			this.nyp = null;
			this.mostrarNombreMetodos = Ventana.thisventana.traza
					.getNumMetodos() != 1;
			if (this.mostrarNombreMetodos) {
				this.nyp = new NombresYPrefijos();
				String[] nombresMetodos = Ventana.thisventana.trazaCompleta
						.getNombresMetodos();
				String prefijos[] = ServiciosString
						.obtenerPrefijos(nombresMetodos);
				for (int i = 0; i < nombresMetodos.length; i++) {
					this.nyp.add(nombresMetodos[i], prefijos[i]);
				}
			}

			this.cc = new ContenedorArbol(Ventana.thisventana.traza.getRaiz(),
					this.graph, this.nyp, 1, getFontMetrics(getFont()).getFontRenderContext());
		
			
			this.celdas = this.cc.getCeldas();

			this.graph.getGraphLayoutCache().insert(this.celdas);

			this.graph.addMouseListener(this);
			this.graph.addMouseMotionListener(this);
			this.graph.setScale(this.escalaActual);
			this.graph.setBackground(Conf.colorPanel);
			this.graph.addMouseWheelListener(this.nl);
			this.graph.addKeyListener(this);

			Point puntoMinimo = this.vp.getViewPosition();

			if (this.cc != null) {
				anchoGraph = this.cc.maximoAncho();
				altoGraph = this.cc.maximoAlto();
			}

			this.vp.setBackground(Conf.colorPanel);
			this.vp.removeAll();
			this.vp.add(this.graph);
			this.vp.setViewPosition(puntoMinimo);
			this.vp.updateUI();

			if (this.cuadroVisor == null) {
				this.cuadroVisor = ContenedorArbol.crearCuadroVisor(400, 400,
						0, 0);
				GraphConstants.setEditable(this.cuadroVisor.getAttributes(),
						false);

				this.celdasV = new Object[this.celdas.length + 2];
				for (int i = 0; i < this.celdas.length; i++) {
					this.celdasV[i] = this.celdas[i];
				}

				this.visor = this.crearVisor(this.celdas, this.graph);

				System.out.println("PanelArbol.anchoGraph = " + anchoGraph);
				escala = this.calculaEscalaMiniatura();
				this.visor.setPreferredSize(new Dimension(
						(int) (anchoGraph * escala),
						(int) (altoGraph * escala) + 20));
				this.celdasV[this.celdasV.length - 1] = this.cuadroVisor;
			}

			Rectangle2D r2d = ContenedorArbol.rectanguloCelda(this.cuadroVisor);

			Point p = new Point((int) (r2d.getMinX() * this.escalaActual),
					(int) (r2d.getMinY() * this.escalaActual));
			this.vp.setViewPosition(p);

			if (Conf.visualizacionDinamica) {
				int xAux = (int) (minX * this.escalaActual)
						- (this.jspArbol.getWidth() / 2);
				int yAux = (int) (minY * this.escalaActual)
						- (this.jspArbol.getHeight() / 2);
				// Solo desplazamos si la siguiente celda no es visible desde la
				// posicion actual, o en la primera celda
				if ((this.anMin == -1)
						|| ((xAux < this.anMin) || (xAux > this.anMax)
								|| (yAux < this.alMin) || (yAux > this.alMax))) {
					// Posicionamos en el ultimo nodo creado. Estas
					// instrucciones tienen que ir aqui, de otro modo se produce
					// mas efecto parpadeo del habitual
					this.jspArbol.getHorizontalScrollBar().setValue(
							(int) (minX * this.escalaActual)
							- (this.jspArbol.getWidth() / 2));
					this.jspArbol.getVerticalScrollBar().setValue(
							(int) (minY * this.escalaActual)
							- (this.jspArbol.getHeight() / 2));
					// Calculamos el ancho y alto maximo y minimo de la ventana
					// centrada en el nodo actual;
					this.anMin = ((int) (minX * this.escalaActual) - 2 * (this.jspArbol
							.getWidth() / 2));
					this.anMax = this.anMin + this.jspArbol.getWidth();
					this.alMin = ((int) (minY * this.escalaActual) - 2 * (this.jspArbol
							.getHeight() / 2));
					this.alMax = this.alMax + this.jspArbol.getHeight();
				}
			}

			this.visor.removeAll();

			if (!redimension && Conf.getPanelArbolReajustado() == false) {
				this.visor.add(this.crearVisor(this.celdas, this.graph,
						(int) r2d.getMinX(), (int) r2d.getMinY(),
						movimientoVisor));
			} else {
				if (!Conf.getHaciendoAjuste()) {
					this.visor.add(this.crearVisor(this.celdas, this.graph,
							(int) (puntoMinimo.getX() / this.escalaActual),
							(int) (puntoMinimo.getY() / this.escalaActual),
							movimientoVisor));
				} else {
					this.visor.add(this.crearVisor(this.celdas, this.graph, 0,
							0, movimientoVisor));
					Conf.setHaciendoAjuste(false);
				}
			}
			escala = this.calculaEscalaMiniatura();
			this.visor.setPreferredSize(new Dimension(
					(int) (anchoGraph * escala),
					(int) (altoGraph * escala) + 20));
			this.visor.updateUI();
			Conf.setPanelArbolReajustado(false);

			// Gestionamos eventos (viejo)
			this.nl.setAtributos(this.vp, this.celdasV, this.escalaActual);

			this.jspArbol.getHorizontalScrollBar().getComponent(0)
			.addMouseListener(this.nl);
			this.jspArbol.getHorizontalScrollBar().getComponent(1)
			.addMouseListener(this.nl);
			this.jspArbol.getVerticalScrollBar().getComponent(0)
			.addMouseListener(this.nl);
			this.jspArbol.getVerticalScrollBar().getComponent(1)
			.addMouseListener(this.nl);
			this.jspArbol.getHorizontalScrollBar().addMouseWheelListener(
					this.nl);
			this.jspArbol.getVerticalScrollBar().addMouseWheelListener(this.nl);
			this.jspArbol.getHorizontalScrollBar().addMouseMotionListener(
					this.nl);
			this.jspArbol.getVerticalScrollBar()
			.addMouseMotionListener(this.nl);
			this.jspArbol.getHorizontalScrollBar().addMouseListener(this.nl);
			this.jspArbol.getVerticalScrollBar().addMouseListener(this.nl);

			this.graph.addKeyListener(this.nl);

			this.removeAll();

			this.add(this.jspArbol, BorderLayout.CENTER);
			if (Conf.mostrarVisor && this.jspVisor != null) {
				this.add(this.jspVisor, BorderLayout.SOUTH);
			}

			this.updateUI();
		}
	}

	/**
	 * Visualiza y redibuja el árbol en el panel
	 * 
	 * @param Habilita
	 *            el movimiento del visor al hacer scroll.
	 */
	public void visualizar2(boolean movimientoScroll) {
		// Obtenemos dónde estaba cuadro visor en estado anterior
		Rectangle2D r2d = ContenedorArbol.rectanguloCelda(this.cuadroVisor);

		this.visor.removeAll();
		this.visor.add(this.crearVisor(this.celdas, this.graph,
				(int) r2d.getMinX(), (int) r2d.getMinY(), movimientoScroll));
		this.visor.updateUI();

		// Gestionamos eventos
		this.nl.setAtributos(this.vp, this.celdasV, this.escalaActual);
	}

	/**
	 * Calcula la escala del visor.
	 * 
	 * @return Escala del visor.
	 */
	private double calculaEscalaMiniatura() {

		if (Conf.ajustarVistaGlobal) {
			double escalaMiniatura = ESCALAVISOR;
			if (this.jspVisor != null) {
				int tamX = (this.jspVisor.getWidth());
				if (tamX <= 0) {
					int anchoPila = (PanelAlgoritmo.dimPanelPila()[0]);
					int anchoTraza = (PanelAlgoritmo.dimPanelTraza()[0]);
					if (anchoPila > 0 && anchoTraza > 0) {
						tamX = Conf.getTamanoVentana()[0] - anchoPila
								- anchoTraza;
					} else {
						tamX = (int) ((Conf.getTamanoVentana()[0] / 2) / this.escalaActual);
					}
				}
				tamX -= 50; // Dejamos un margen
				escalaMiniatura = (double) tamX / PanelArbol.anchoGraph;
			}
			if (escalaMiniatura >= ESCALAVISOR) {
				return ESCALAVISOR;
			}
			return escalaMiniatura;
		} else {
			return ESCALAVISOR;
		}
	}

	/**
	 * Permite crear el visor dada la lista de celdas y el grafo del árbol.
	 * 
	 * @param celdas
	 *            Lista de celdas.
	 * @param graph
	 *            Grafo.
	 * 
	 * @return Panel visor.
	 */
	private JPanel crearVisor(Object[] celdas, JGraph graph) {
		return this.crearVisor(celdas, graph, 0, 0, false);
	}

	/**
	 * Permite crear el visor dada la lista de celdas y el grafo del árbol con
	 * opciones extra.
	 * 
	 * @param celdas
	 *            Lista de celdas.
	 * @param graph
	 *            Grafo.
	 * @param posX
	 *            Posición x del visor.
	 * @param posY
	 *            Posición y del visor.
	 * @param movimientoVisor
	 *            habilita el movimiento del visor.
	 * 
	 * @return Panel visor.
	 */
	private JPanel crearVisor(Object[] celdas, JGraph graph, int posX,
			int posY, boolean movimientoVisor) {
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,
				new DefaultCellViewFactory());
		this.g = new JGraph(model, view);

		this.g.getModel().addGraphModelListener(null);

		// Creamos repertorio de celdas para el visor: todas las del grafo y una
		// más: el cuadro de movimiento
		this.celdasV = new Object[celdas.length + 1];
		for (int i = 0; i < celdas.length; i++) {
			this.celdasV[i] = celdas[i];
		}

		if (this.jspVisor != null) {
			int tamX = (int) (this.jspVisor.getWidth() / this.escalaActual);
			int tamY = (int) (this.getHeight() / this.escalaActual - this.jspVisor
					.getHeight() / this.escalaActual);

			if (tamX <= 0 || tamY <= 0) {
				int anchoPila = (PanelAlgoritmo.dimPanelPila()[0]);
				int anchoTraza = (PanelAlgoritmo.dimPanelTraza()[0]);

				if (anchoPila > 0 && anchoTraza > 0) {
					tamX = Conf.getTamanoVentana()[0] - anchoPila - anchoTraza;
					tamY = tamX;
				} else {
					tamX = (int) ((Conf.getTamanoVentana()[0] / 2) / this.escalaActual);
					tamY = tamX;
				}

			}
			int posicionX = (minX - (this.jspArbol.getWidth() / 2));
			if (posicionX < 0) {
				posicionX = 0;
			}
			int posicionY = (minY - (this.jspArbol.getHeight() / 2));
			if (posicionY < 0) {
				posicionY = 0;
			}
			if ((Conf.visualizacionDinamica) && (!movimientoVisor)) {
				this.cuadroVisor = ContenedorArbol.crearCuadroVisor(tamX, tamY,
						posicionX, posicionY);
			} else {
				this.cuadroVisor = ContenedorArbol.crearCuadroVisor(tamX, tamY,
						posX, posY);
			}
		} else {
			this.cuadroVisor = ContenedorArbol.crearCuadroVisor(400, 400, 0, 0);
		}

		GraphConstants.setEditable(this.cuadroVisor.getAttributes(), false);
		this.celdasV[this.celdasV.length - 1] = this.cuadroVisor;

		// Generamos el grafo insertando en él las celdas

		this.g.getGraphLayoutCache().insert(this.celdasV);
		escala = this.calculaEscalaMiniatura();
		this.g.setScale(escala);
		this.g.addMouseListener(this);
		this.g.addMouseMotionListener(this);
		this.g.setBackground(Conf.colorPanel);
		this.g.setAutoResizeGraph(false);
		this.g.setSizeable(false);

		JPanel panelVisor = new JPanel();
		panelVisor.setLayout(new BorderLayout());

		this.g.addMouseListener(this.nl);
		this.g.addMouseMotionListener(this.nl);

		panelVisor.add(this.g, BorderLayout.CENTER);
		panelVisor.updateUI();

		return panelVisor;
	}

	/**
	 * Devuelve el nivel de zoom actual.
	 * 
	 * @return Nivel de zoom.
	 */
	public int getZoom() {
		return this.zoom;
	}

	/**
	 * Devuelve la altura del scroll panel del árbol.
	 * 
	 * @return Altura.
	 */
	public int alturaJSPArbol() {
		return this.jspArbol.getHeight();
	}

	/**
	 * Devuelve las dimensiones del grafo.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafo() {
		int valores[] = new int[2];
		valores[0] = this.cc.maximoAncho();
		valores[1] = this.cc.maximoAlto();
		return valores;
	}

	/**
	 * Devuelve las dimensiones del grafo visible.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoVisible() {
		int valores[] = new int[2];
		valores[0] = this.cc.maximoAnchoVisible() + 10;
		valores[1] = this.cc.maximoAltoVisible() + 20;
		new Thread() {
			@Override
			public synchronized void run() {
				try {
					this.wait(40);
				} catch (java.lang.InterruptedException ie) {
				}
				PanelArbol.this.vp.setViewPosition(new Point(0, 0));
			}
		}.start();

		return valores;
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
					// System.out.print("x");
					this.haEntradoAhora = true;
				}
			}
			// Menú contextual: segunda opción: hacer nodo actual
			else if (item == this.opcionesMenuContextual[1]) {
				// this.ra=cc.getRegistroPosicion((int)(this.ratonX/graph.getScale()),(int)(this.ratonY/graph.getScale()));
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

		this.ra = this.cc.getRegistroPosicion(
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
				this.haEntradoAhora = true;
			}
			
			if(Arrays.contiene(MetodoAlgoritmo.TECNICA_AABB,
					Ventana.thisventana.getTraza().getTecnicas()) 
					&& this.ra != null) {
				final RegistroActivacion nodoActual = this.ra;
				new Thread() {
					@Override
					public synchronized void run() {
						
						try {
							this.wait(20);
						} catch (java.lang.InterruptedException ie) {
						}
						SwingUtilities.invokeLater(new Runnable() {							
							@Override
							public void run() {
								if(pValRama != null) {
									pValRama.setNodoActual(nodoActual);
									pValRama.visualizar();
								}
							}
						});
					}
				}.start();
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
				// el menu
				// en la
				// ubicacion
				// del raton
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// System.out.println("Evento de raton, mouseReleased");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("Evento de raton, mousePressed");
	}

	/**
	 * Actualiza el valor del zoom.
	 * 
	 * @param valor
	 *            Nuevo valor para el zoom.
	 */
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
		// this.visualizar();
		this.zoom = valor;
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	/**
	 * Devuelve el listener de navegación asociado al panel.
	 * 
	 * @return listener de navegación.
	 */
	public NavegacionListener getNavegacionListener() {
		return this.nl;
	}

	/**
	 * Devuelve el ancho del grafo.
	 * 
	 * @return Ancho del grafo.
	 */
	public static int anchoGrafo() {
		return anchoGraph;
	}

	/**
	 * Devuelve el alto del grafo.
	 * 
	 * @return Alto del grafo.
	 */
	public static int altoGrafo() {
		return altoGraph;
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

	/**
	 * Devuelve el viewport del panel.
	 * 
	 * @return ViewPort del panel.
	 */
	public JViewport getViewport() {
		return this.vp;
	}

	/**
	 * Devuelve las celdas del grafo.
	 * 
	 * @return Celdas del grafo.
	 */
	public Object[] getCeldas() {
		return this.celdas;
	}

	/**
	 * Devuelve el grafo.
	 * 
	 * @return Grafo.
	 */
	public JGraph getGrafo() {
		return this.graph;
	}

	/**
	 * Establece la posición x mínima para la visualización dinámica del árbol.
	 * 
	 * @param i
	 *            Posición x mínima.
	 */
	public static void setMinX(int i) {
		minX = i;
	}

	/**
	 * Establece la posición y mínima para la visualización dinámica del árbol.
	 * 
	 * @param i
	 *            Posición y mínima.
	 */
	public static void setMinY(int i) {
		minY = i;
	}
}
