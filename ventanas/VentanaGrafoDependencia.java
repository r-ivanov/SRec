package ventanas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.plaf.metal.MetalBorders;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;

import utilidades.FotografoArbol;
import utilidades.Texto;
import conf.Conf;
import cuadros.CuadroDibujarTablaGrafoDependencia;
import cuadros.CuadroTabularGrafoDependencia;
import datos.DatosMetodoBasicos;
import datos.GrafoDependencia;

/**
 * Representa el cuadro donde se mostrará el grafo de dependencia.
 * 
 * @author David Pastor Herranz
 */
public class VentanaGrafoDependencia extends JFrame implements ActionListener,
		KeyListener {

	private static final long serialVersionUID = -7397386753145487405L;

	private static final String ICONO = "./imagenes/ico32.gif";

	private static final double MAX_SCALE = 1.5;
	private static final double MIN_SCALE = 0.5;
	private static final double SCALE_INCREMENTO = 0.05;

	private static final int WINDOW_WIDTH_MARGIN = 20;
	private static final int WINDOW_HEIGHT_MARGIN = 80;

	private static final int PORCENTAJE_PANTALLA_MAXIMO = 75;
	private static final int PORCENTAJE_PANTALLA_MINIMO = 30;

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

	/**
	 * Genera un nuevo cuadro que permite mostrar un grafo de dependencia.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 * @param metodo
	 *            Datos del método para el que generar el grafo de dependencia.
	 */
	public VentanaGrafoDependencia(Ventana ventana, DatosMetodoBasicos metodo) {
		this.ventana = ventana;
		this.metodo = metodo;

		this.setIconImage(new ImageIcon(ICONO).getImage());
		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		// Preparamos y mostramos cuadro
		this.getContentPane().add(panel);
		this.setTitle(Texto.get("GP_GRAFO_TITULO", Conf.idioma));

		this.crearBarraDeHerramientas();

		this.grafoDependencia = new GrafoDependencia(
				this.ventana.trazaCompleta, this.metodo);
		this.representacionGrafo = this.grafoDependencia
				.obtenerRepresentacionGrafo();

		this.representacionGrafoScroll = new JScrollPane(
				this.representacionGrafo);
		this.add(this.representacionGrafoScroll);

		this.ajustarGrafoATamanioVentana(true);

		this.setMinimumSize(new Dimension(Conf.anchoPantalla
				* PORCENTAJE_PANTALLA_MINIMO / 100, Conf.altoPantalla
				* PORCENTAJE_PANTALLA_MINIMO / 100));
		this.setResizable(true);
		this.setVisible(true);
	}

	/**
	 * Ajusta el tamaño del grafo al tamaño de la ventana.
	 * 
	 * @param ajustarTamanioVentana
	 *            true si debe ajustarse tambien el tamaño de la ventana.
	 */
	private void ajustarGrafoATamanioVentana(boolean ajustarTamanioVentana) {

		Dimension tamanioGrafo = this.grafoDependencia
				.getTamanioRepresentacion();

		/* Recorremos las vistas activas por si se ha desplazado algún nodo */
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

		Dimension tamanioVentana;
		if (ajustarTamanioVentana) {

			int anchura = Math.min(Conf.anchoPantalla
					* PORCENTAJE_PANTALLA_MAXIMO / 100, tamanioGrafo.width
					+ WINDOW_WIDTH_MARGIN);
			anchura = Math.max(this.getMinimumSize().width, anchura);

			int altura = Math.min(Conf.altoPantalla
					* PORCENTAJE_PANTALLA_MAXIMO / 100, tamanioGrafo.height
					+ WINDOW_HEIGHT_MARGIN);
			altura = Math.max(this.getMinimumSize().height, altura);

			tamanioVentana = new Dimension(anchura, altura);
			this.setSize(tamanioVentana);
			int[] ubicacion = Conf.ubicarCentro(tamanioVentana.width,
					tamanioVentana.height);
			this.setLocation(ubicacion[0], ubicacion[1]);

		} else {
			tamanioVentana = this.getSize();
		}

		double ratioAnchura = (tamanioVentana.getWidth() - WINDOW_WIDTH_MARGIN)
				/ tamanioGrafo.getWidth();
		double ratioAltura = (tamanioVentana.getHeight() - WINDOW_HEIGHT_MARGIN)
				/ tamanioGrafo.getHeight();
		double ratioMenor = Math.min(ratioAnchura, ratioAltura);

		double ratio = Math.max(ratioMenor, MIN_SCALE);
		ratio = Math.min(ratio, MAX_SCALE);

		this.representacionGrafo.setScale(ratio);
	}

	/**
	 * Crea la barra de herramientas de la ventana.
	 */
	private void crearBarraDeHerramientas() {

		this.botones = new JButton[6];

		this.botones[0] = new JButton(
				new ImageIcon("./imagenes/i_tabulado.gif"));
		this.botones[0].setToolTipText(Texto.get("GP_DIBUJAR_MATRIZ",
				Conf.idioma));
		this.botones[1] = new JButton(new ImageIcon(
				"./imagenes/i_tabulado_automatico.gif"));
		this.botones[1].setToolTipText(Texto.get("GP_TABULAR_MATRIZ",
				Conf.idioma));

		this.botones[2] = new JButton(new ImageIcon(
				"./imagenes/i_exportarestado.gif"));
		this.botones[2].setToolTipText(Texto.get("GP_EXPORTAR", Conf.idioma));

		this.botones[3] = new JButton(new ImageIcon("./imagenes/i_zoommas.gif"));
		this.botones[3].setToolTipText(Texto
				.get("GP_AUMENTO_ZOOM", Conf.idioma));
		this.botones[4] = new JButton(new ImageIcon(
				"./imagenes/i_zoommenos.gif"));
		this.botones[4].setToolTipText(Texto.get("GP_DISMINUCION_ZOOM",
				Conf.idioma));
		this.botones[5] = new JButton(new ImageIcon(
				"./imagenes/i_zoomajuste.gif"));
		this.botones[5]
				.setToolTipText(Texto.get("GP_AJUSTE_ZOOM", Conf.idioma));

		// Creamos las barras de herramientas
		this.barras = new JToolBar[3];
		for (int i = 0; i < this.barras.length; i++) {
			this.barras[i] = new JToolBar(Texto.get("BARRA_HERR", Conf.idioma));
			this.barras[i].setBorderPainted(true);
			this.barras[i].setFloatable(false);
			this.barras[i].setBorder(new MetalBorders.PaletteBorder());
		}

		// Grupo tabulación
		this.barras[0].add(this.botones[0]);
		this.barras[0].add(this.botones[1]);

		// Grupo opciones nodos
		this.barras[1].add(this.botones[2]);

		// Grupo formato
		this.barras[2].add(this.botones[3]);
		this.barras[2].add(this.botones[4]);
		this.barras[2].add(this.botones[5]);

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

		this.add(this.panelHerramientas, BorderLayout.NORTH);
	}

	/**
	 * Dibuja una tabla con un número de filas y columnas.
	 * 
	 * @param filas
	 *            Número de filas.
	 * @param columnas
	 *            Número de columnas.
	 */
	public void dibujarTabla(int filas, int columnas) {

		this.grafoDependencia.setTamanioTabla(filas, columnas);
		this.representacionGrafo = this.grafoDependencia
				.obtenerRepresentacionGrafo();

		this.remove(this.representacionGrafoScroll);
		this.representacionGrafoScroll = new JScrollPane(
				this.representacionGrafo);
		this.add(this.representacionGrafoScroll);

		this.ajustarGrafoATamanioVentana(true);
		this.revalidate();
	}

	/**
	 * Tabula automáticamente los nodos del grafo, dada una expresión para filas
	 * y otra para columnas.
	 * 
	 * @param expresionParaFila
	 * @param expresionParaColumna
	 * 
	 * @return Mensaje de error si ocurrió algun error, null en caso contrario.
	 */
	public String tabular(String expresionParaFila, String expresionParaColumna) {
		this.ultimaExpresionParaFila = expresionParaFila;
		this.ultimaExpresionParaColumna = expresionParaColumna;
		String mensajeError = this.grafoDependencia.tabular(expresionParaFila,
				expresionParaColumna);
		if (mensajeError == null) {
			this.representacionGrafo = this.grafoDependencia
					.obtenerRepresentacionGrafo();

			this.remove(this.representacionGrafoScroll);
			this.representacionGrafoScroll = new JScrollPane(
					this.representacionGrafo);
			this.add(this.representacionGrafoScroll);
			
			this.ajustarGrafoATamanioVentana(true);
			this.revalidate();
		}
		return mensajeError;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.botones[0]) {
			new CuadroDibujarTablaGrafoDependencia(this,
					this.grafoDependencia.getNumeroFilasTabla(),
					this.grafoDependencia.getNumeroColumnasTabla());
		} else if (e.getSource() == this.botones[1]) {
			new CuadroTabularGrafoDependencia(this, this.metodo.getInterfaz(),
					this.ultimaExpresionParaFila,
					this.ultimaExpresionParaColumna);
		} else if (e.getSource() == this.botones[2]) {
			new FotografoArbol().hacerCapturaGrafo(this,
					this.representacionGrafo);
		} else if (e.getSource() == this.botones[5]) {
			this.ajustarGrafoATamanioVentana(false);
		} else if (e.getSource() == this.botones[4]) {
			double scale = Math.max(this.representacionGrafo.getScale()
					- SCALE_INCREMENTO, MIN_SCALE);
			this.representacionGrafo.setScale(scale);
		} else if (e.getSource() == this.botones[3]) {
			double scale = Math.min(this.representacionGrafo.getScale()
					+ SCALE_INCREMENTO, MAX_SCALE);
			this.representacionGrafo.setScale(scale);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.setVisible(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
