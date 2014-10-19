package paneles;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JRootPane;

import org.jgraph.JGraph;

import conf.Conf;
import datos.Ejecucion;
import datos.FamiliaEjecuciones;

public class PanelSnapshotGrafo extends JPanel implements MouseListener {

	private static final long serialVersionUID = 5487058416021891922L;
	private static final int BORDE_SELECCION = 10;

	private final FamiliaEjecuciones familiaEjecuciones;
	private final Ejecucion ejecucion;

	private final int anchura;
	private final int altura;

	private BufferedImage snapshot;

	public PanelSnapshotGrafo(FamiliaEjecuciones familiaEjecuciones,
			Ejecucion ejecucion, int anchura, int altura) {
		this.setBackground(Conf.colorPanel);

		this.familiaEjecuciones = familiaEjecuciones;
		this.ejecucion = ejecucion;

		this.anchura = anchura;
		this.altura = altura;

		this.addMouseListener(this);

		this.inicializarSnapshot();
		this.updateUI();
	}

	/**
	 * Obtenemos un primer snapshot del grafo para conocer el tamaño de la
	 * imagen, una vez obtenido, escalaremos el grafo para obtener un snapshot
	 * ajustado.
	 */
	private void inicializarSnapshot() {

		int imageWidth = this.anchura - BORDE_SELECCION * 2;
		int imageHeight = this.altura - BORDE_SELECCION * 2;

		JGraph graph = this.ejecucion.obtenerGrafo();

		BufferedImage snapshotOriginal = graph.getImage(graph.getBackground(),
				0);
		graph.setSize(snapshotOriginal.getWidth(), snapshotOriginal.getHeight());

		Dimension graphDimension = graph.getSize();
		double xScale = imageWidth / graphDimension.getWidth();
		double yScale = imageHeight / graphDimension.getHeight();
		double minScale = Math.min(xScale, yScale);

		graph.setScale(minScale, new Point2D.Double(0, 0));
		int grafoEscaladoWidth = new Double(Math.ceil((graphDimension
				.getWidth() + 50) * minScale)).intValue();
		int grafoEscaladoHeight = new Double(Math.ceil((graphDimension
				.getHeight() + 30) * minScale)).intValue();
		graph.setSize(grafoEscaladoWidth, grafoEscaladoHeight);

		BufferedImage snapshot = graph.getImage(graph.getBackground(), 0);
		this.snapshot = snapshot;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		/* Pintamos la captura */
		int xCenter = this.anchura / 2;
		int yCenter = this.altura / 2;
		int snapshotXCenter = this.snapshot.getWidth() / 2;
		int snapshotYCenter = this.snapshot.getHeight() / 2;
		g2d.drawImage(this.snapshot, xCenter - snapshotXCenter, yCenter
				- snapshotYCenter, null);

		if (this.familiaEjecuciones.esEjecucionActiva(this.ejecucion)) {
			/* Pintamos el rectángulo de selección */
			g2d.setStroke(new BasicStroke(BORDE_SELECCION));
			g2d.setPaint(new GradientPaint(80, 100, Conf.colorC1Entrada, 8, 20,
					Conf.colorC1Salida, true));
			g2d.draw(new Rectangle2D.Double(0, 0, this.anchura, this.altura));
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.anchura, this.altura);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(this.anchura, this.altura);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(this.anchura, this.altura);
	}

	/**
	 * Método que gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		this.familiaEjecuciones.setEjecucionActiva(this.ejecucion);
	}

	/**
	 * Método que gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		JRootPane rootPane = this.getRootPane();
		if (rootPane != null) {
			rootPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}

	/**
	 * Método que gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		JRootPane rootPane = this.getRootPane();
		if (rootPane != null) {
			rootPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Método que gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mousePressed(MouseEvent e) { 

	}

	/**
	 * Método que gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseReleased(MouseEvent e) {

	}
}
