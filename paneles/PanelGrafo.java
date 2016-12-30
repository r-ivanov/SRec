package paneles;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.plaf.metal.MetalBorders;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import ventanas.Ventana;
import ventanas.VentanaGrafoDependencia;
import conf.Conf;
import datos.DatosMetodoBasicos;
import datos.GrafoDependencia;
import datos.NodoGrafoDependencia;
import utilidades.Texto;

/**
 * Panel que contendr� la visualizaci�n de la vista de grafo de dependencia del algoritmo.
 * 
 * @author Daniel Arroyo Cort�s
 * @version 2016
 */
class PanelGrafo extends JPanel implements ActionListener, KeyListener,
MouseListener, MouseMotionListener {
	
	private static final long serialVersionUID = 1L;

	private static final java.net.URL ICONO = VentanaGrafoDependencia.class.getClassLoader().getResource("imagenes/ico32.gif");

	private static final double MAX_SCALE = 1.5;
	private static final double MIN_SCALE = 0.5;
//	private static final double SCALE_INCREMENTO = 0.05;

	private static final int WINDOW_WIDTH_MARGIN = 20;
	private static final int WINDOW_HEIGHT_MARGIN = 80;

//	private static final int PORCENTAJE_PANTALLA_MAXIMO = 75;
//	private static final int PORCENTAJE_PANTALLA_MINIMO = 30;

	private Ventana ventana;

	private DatosMetodoBasicos metodo;

	private JPanel panelHerramientas;
	private JToolBar[] barras;
	private JButton[] botones;

	private GrafoDependencia grafoDependencia;
	private JGraph representacionGrafo;
	private JScrollPane representacionGrafoScroll;

	private String ultimaExpresionParaFila;
	private String ultimaExpresionParaColumna;

	private JPanel panel;

	private double escalaOriginal;
	private double escalaActual;
	
	private int zoom = 0;
	
	
	/**
	 * Constructor: crea un nuevo panel de visualizaci�n para el grafo.
	 * 
	 * @param metodo
	 * 		M�todo del que queremos crear el grafo de dependencia
	 * @param ventana
	 * 		Ventana a la que est� asociada la pesta�a, es la principal
	 * 		y es necesaria para obtener algunos datos
	 */
	public PanelGrafo(DatosMetodoBasicos metodo, Ventana ventana) throws Exception {
		
		if (Ventana.thisventana.traza != null && metodo != null && ventana != null) {
			
			//	Obtenemos datos b�sicos del grafo y su representaci�n
			
			this.metodo = metodo;
			this.ventana = ventana;	
				
			this.grafoDependencia = new GrafoDependencia(
					this.ventana.trazaCompleta, this.metodo);
			this.representacionGrafo = this.grafoDependencia
					.obtenerRepresentacionGrafo(false);
			this.representacionGrafo.setScale(this.representacionGrafo.getScale());
			this.escalaOriginal = this.representacionGrafo.getScale();
			this.escalaActual = this.representacionGrafo.getScale();
			this.refrescarZoom(-50);	//	Tama�o por defecto del grafo al inicio
			
			//	Creamos pesta�a	y llamamos a visualizar	

			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,
					new DefaultCellViewFactory());					
			this.panel = new JPanel();			
			this.add(this.panel, BorderLayout.NORTH);
			
			try {
				this.visualizar();				
			} catch (OutOfMemoryError oome) {
				this.representacionGrafo = null;
				throw oome;
			} catch (Exception e) {
				throw e;
			}
		} else {
			this.add(new JPanel());
		}
	}

	/**
	 * Ajusta el tama�o del grafo al tama�o de la pesta�a.						---> Por definir aun
	 */
	private void ajustarGrafoATamanioPestana() {

		Dimension tamanioGrafo = this.grafoDependencia
				.getTamanioRepresentacion();

		/* Recorremos las vistas activas por si se ha desplazado alg�n nodo */
		Rectangle rectangulo = new Rectangle(0, 0, tamanioGrafo.width,
				tamanioGrafo.height);
		for (CellView cell : this.representacionGrafo.getGraphLayoutCache()
				.getAllViews()) {
			Rectangle2D nodoBounds = cell.getBounds();
			rectangulo = rectangulo.union(new Rectangle(
					(int) nodoBounds.getX(), (int) nodoBounds.getY(),
					(int) nodoBounds.getWidth(), (int) nodoBounds.getHeight()));
		}
		tamanioGrafo = new Dimension(rectangulo.width, rectangulo.height);

		Dimension tamanioVentana = new Dimension(
				(int)Math.round(this.getVisibleRect().getWidth()),
				(int)Math.round(this.getVisibleRect().getHeight()));	
		
		double ratioAnchura = (this.getVisibleRect().getSize().getWidth() - WINDOW_WIDTH_MARGIN)
				/ tamanioGrafo.getWidth();
		double ratioAltura = (this.getVisibleRect().getSize().getHeight() - WINDOW_HEIGHT_MARGIN)
				/ tamanioGrafo.getHeight();
		double ratioMenor = Math.min(ratioAnchura, ratioAltura);

		double ratio = Math.max(ratioMenor, MIN_SCALE);
		ratio = Math.min(ratio, MAX_SCALE);

		this.representacionGrafo.setScale(ratio);
		this.escalaActual = this.representacionGrafo.getScale();
		
		
		System.out.println(this.representacionGrafo.getScale());
		System.out.println(ratio);
		System.out.println(tamanioVentana);
		System.out.println(tamanioGrafo);
		
		
		
		
		this.panel.updateUI();
		this.updateUI();
	}
	
	/**
	 * Obtiene el tama�o que ocupa el grafo en pantalla aunque el usuario
	 * 	haya desplazado nodos a mano
	 * @return Array donde [0] = Ancho grafo y [1] = Alto grafo
	 */
	private double[] obtenerTamanioGrafo(){
		Dimension tamanioGrafo = this.grafoDependencia
				.getTamanioRepresentacion();
		Rectangle rectangulo = new Rectangle(0, 0, tamanioGrafo.width,
				tamanioGrafo.height);
		for (CellView cell : this.representacionGrafo.getGraphLayoutCache()
				.getAllViews()) {
			Rectangle2D nodoBounds = cell.getBounds();
			rectangulo = rectangulo.union(new Rectangle(
					(int) nodoBounds.getX(), (int) nodoBounds.getY(),
					(int) nodoBounds.getWidth(), (int) nodoBounds.getHeight()));
		}
		tamanioGrafo = new Dimension(rectangulo.width, rectangulo.height);
		double[] valorDevuelto = new double[2];
		valorDevuelto[0] = tamanioGrafo.getWidth();
		valorDevuelto[1] = tamanioGrafo.getHeight();		
		return valorDevuelto;
	}
	
	/**
	 * Dibuja una tabla con un n�mero de filas y columnas.			---> Por definir aun
	 * 
	 * @param filas
	 *            N�mero de filas.
	 * @param columnas
	 *            N�mero de columnas.
	 */
	public void dibujarTabla(int filas, int columnas) {
		this.grafoDependencia.setTamanioTabla(filas, columnas);
		this.representacionGrafo = this.grafoDependencia
				.obtenerRepresentacionGrafo(false);

		this.remove(this.representacionGrafoScroll);
		this.representacionGrafoScroll = new JScrollPane(
				this.representacionGrafo);
		this.add(this.representacionGrafoScroll);

		this.ajustarGrafoATamanioPestana();
		this.revalidate();	
	}
	
	/**
	 * Tabula autom�ticamente los nodos del grafo, dada una expresi�n para filas	---> Por definir aun
	 * y otra para columnas.
	 * 
	 * @param expresionParaFila
	 * @param expresionParaColumna
	 * 
	 * @return Mensaje de error si ocurri� algun error, null en caso contrario.
	 */
	public String tabular(String expresionParaFila, String expresionParaColumna) {
		this.ultimaExpresionParaFila = expresionParaFila;
		this.ultimaExpresionParaColumna = expresionParaColumna;
		String mensajeError = this.grafoDependencia.tabular(expresionParaFila,
				expresionParaColumna);
		if (mensajeError == null) {
			this.representacionGrafo = this.grafoDependencia
					.obtenerRepresentacionGrafo(true);

			this.remove(this.representacionGrafoScroll);
			this.representacionGrafoScroll = new JScrollPane(
					this.representacionGrafo);
			this.add(this.representacionGrafoScroll);
			
			this.ajustarGrafoATamanioPestana();
			this.revalidate();
		}
		return mensajeError;
	}
	
	/**
	 * Invierte las flechas del grafo										---> Por definir aun
	 */
	private void invertirFlechasGrafo(){
		List<NodoGrafoDependencia> listaNodos = this.grafoDependencia.getNodos();
		for(NodoGrafoDependencia nodo:listaNodos){
			nodo.invertirAristas();					
		}
		this.representacionGrafo = this.grafoDependencia
				.obtenerRepresentacionGrafo(true);

		this.remove(this.representacionGrafoScroll);
		this.representacionGrafoScroll = new JScrollPane(
				this.representacionGrafo);
		this.add(this.representacionGrafoScroll);
		
		this.ajustarGrafoATamanioPestana();
		this.revalidate();
	}
	
	/**
	 * Visualiza y redibuja el grafo en la pesta�a.
	 */
	public void visualizar() {		
		if (Ventana.thisventana.traza != null) {
			this.representacionGrafo.getModel().addGraphModelListener(null);
			this.representacionGrafo.addMouseListener(this);
			this.representacionGrafo.setScale(this.escalaActual);
			this.representacionGrafo.setBackground(Conf.colorPanel);

			this.panel.addMouseListener(this);

			this.panel.removeAll();
			this.crearBarraDeHerramientas();
			this.panel.setLayout (new BorderLayout());
			this.panel.add (this.representacionGrafo, BorderLayout.CENTER);
			this.panel.add (this.panelHerramientas, BorderLayout.NORTH);
			
			this.panel.setBackground(Conf.colorPanel);
			this.setBackground(Conf.colorPanel);
			this.panel.updateUI();
			this.updateUI();		
		}
	}
	
	/**
	 * Devuelve el grafo de la vista.
	 * 
	 * @return Grafo de la vista.
	 */
	public JGraph getGrafo() {
		return this.representacionGrafo;
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
	 * @return Array, donde la posici�n 0 contiene el m�ximo ancho, y la
	 *         posici�n 1 el m�ximo alto.
	 */
	public int[] dimGrafo() {				
		int[] dim = new int[2];
		dim[0] = this.grafoDependencia.getTamanioRepresentacion().width;			
		dim[1] = this.grafoDependencia.getTamanioRepresentacion().height;	
		return dim;
	}
	
	/**
	 * Devuelve las dimensiones del panel y del grafo.
	 * 
	 * @return Array, donde la posici�n 0 corresponde al ancho de la pesta�a , la 1
	 *         al alto de la pesta�a, la 2 al ancho del grafo, y la 3 al alto del
	 *         grafo.
	 */
	public int[] dimPanelYGrafoDep() {
		int dim[] = new int[4];

		dim[0] = (int) (this.getVisibleRect().getWidth()); // Anchura del panel *
		dim[1] = (int) (this.getVisibleRect().getHeight()); // Altura del panel *

		if(this.representacionGrafo != null){
			dim[2] = (int) (this.obtenerTamanioGrafo()[0]); // Anchura			
			dim[3] = (int) (this.obtenerTamanioGrafo()[1]); // Altura
		}else{
			dim[2] = 0;
			dim[3] = 0;
		}
		
		return dim;
	}
	
	/**
	 * Permite refrescar el zoom al valor dado por el par�metro
	 * @param valor Valor al que queremos ajustar el zoom
	 */
	public void refrescarZoom(int valor) {	
		if (valor == 0) {
			this.representacionGrafo.setScale(this.escalaOriginal);
		} else if (valor > 0) {
			double v = valor;
			v = v / 100;
			v = v + 1;
			v = v * this.escalaOriginal;
			this.representacionGrafo.setScale(v);
		} else // if (valor<0)
		{
			double v = (valor * (-1));
			v = v / 100;
			v = 1 - v;
			v = v * this.escalaOriginal;
			this.representacionGrafo.setScale(v);
		}
		this.escalaActual = this.representacionGrafo.getScale();
		this.zoom = valor;
	}
	
	
	/**
	 * Crea la barra de herramientas de la ventana.								---> Por definir aun
	 */
	private void crearBarraDeHerramientas() {

		this.botones = new JButton[7];

		this.botones[0] = new JButton(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/i_tabulado.gif")));
		this.botones[0].setToolTipText(Texto.get("GP_DIBUJAR_MATRIZ",
				Conf.idioma));
		this.botones[1] = new JButton(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/i_tabulado_automatico.gif")));
		this.botones[1].setToolTipText(Texto.get("GP_TABULAR_MATRIZ",
				Conf.idioma));

		this.botones[2] = new JButton(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/i_exportarestado.gif")));
		this.botones[2].setToolTipText(Texto.get("GP_EXPORTAR", Conf.idioma));

		this.botones[3] = new JButton(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/i_zoommas.gif")));
		this.botones[3].setToolTipText(Texto
				.get("GP_AUMENTO_ZOOM", Conf.idioma));
		this.botones[4] = new JButton(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/i_zoommenos.gif")));
		this.botones[4].setToolTipText(Texto.get("GP_DISMINUCION_ZOOM",
				Conf.idioma));
		this.botones[5] = new JButton(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/i_zoomajuste.gif")));
		this.botones[5]
				.setToolTipText(Texto.get("GP_AJUSTE_ZOOM", Conf.idioma));
		this.botones[6] = new JButton(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/i_invertir_flechas_grafo.gif")));
		this.botones[6]
				.setToolTipText(Texto.get("GP_INVERTIR_FLECHAS", Conf.idioma));

		// Creamos las barras de herramientas
		this.barras = new JToolBar[3];
		for (int i = 0; i < this.barras.length; i++) {
			this.barras[i] = new JToolBar(Texto.get("BARRA_HERR", Conf.idioma));
			this.barras[i].setBorderPainted(true);
			this.barras[i].setFloatable(false);
			this.barras[i].setBorder(new MetalBorders.PaletteBorder());
		}

		// Grupo tabulaci�n
		this.barras[0].add(this.botones[0]);
		this.barras[0].add(this.botones[1]);

		// Grupo opciones nodos
		this.barras[1].add(this.botones[2]);

		// Grupo formato
		this.barras[2].add(this.botones[3]);
		this.barras[2].add(this.botones[4]);
		this.barras[2].add(this.botones[5]);
		this.barras[2].add(this.botones[6]);

		this.panelHerramientas = new JPanel();
		this.panelHerramientas.setLayout(new BorderLayout());

		JPanel p = new JPanel();
		for (int i = 0; i < this.barras.length; i++) {
			p.add(this.barras[i]);
		}

		for (int i = 0; i < this.botones.length; i++) {
			this.botones[i].setFocusable(false);
			this.botones[i].addActionListener(this);
		}

		this.panelHerramientas.add(p, BorderLayout.WEST);

		JLabel labelTitulo = new JLabel(this.ventana.getTraza().getTitulo());
		labelTitulo.setFont(new Font("Arial", Font.BOLD, 14));
		JLabel labelSignatura = new JLabel("  -  " + this.metodo.getInterfaz()
				+ "   ");
		labelSignatura.setFont(new Font("Arial", Font.ITALIC, 14));

		JPanel panelInfo = new JPanel(new BorderLayout());
		panelInfo.add(labelTitulo, BorderLayout.WEST);
		panelInfo.add(labelSignatura, BorderLayout.EAST);

		this.panelHerramientas.add(panelInfo, BorderLayout.EAST);

//		this.add(this.panelHerramientas, BorderLayout.NORTH);
	}
	
	
	
	
	
	
	
	
	
//	/**
//	 * Visualiza y redibuja la pila en el panel.
//	 */
//	public void visualizar() {
//		if (Ventana.thisventana.traza != null) {
//			
//			if (Ventana.thisventana.traza != null) {
//				
//			}
//		}
//	}
//	
//	/**
//	 * Devuelve el grafo de la vista.
//	 * 
//	 * @return Grafo de la vista.
//	 */
//	public JGraph getGrafo() {
//		
//	}
//	
//	/**
//	 * Devuelve el nivel de zoom actual.
//	 * 
//	 * @return Nivel de zoom actual.
//	 */
//	public int getZoom() {						
//		
//	}
//	
//	/**
//	 * Devuelve las dimensiones del grafo.
//	 * 
//	 * @return Array, donde la posici�n 0 contiene el m�ximo ancho, y la
//	 *         posici�n 1 el m�ximo alto.
//	 */
//	public int[] dimGrafo() {				//	<=========== Modificar
//		
//	}
//	
//	/**
//	 * Devuelve las dimensiones del panel y del grafo.
//	 * 
//	 * @return Array, donde la posici�n 0 corresponde al ancho del panel , la 1
//	 *         al alto del panel, la 2 al ancho del grafo, y la 3 al alto del
//	 *         grafo.
//	 */
//	public int[] dimPanelYGrafoDep() {			//	<=========== Modificar
//		
//	}
//	
//	public void refrescarZoom(int valor) {					//	<=========== Hecho en grafo	
//		
//	}

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
