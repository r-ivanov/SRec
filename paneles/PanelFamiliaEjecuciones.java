package paneles;

import java.awt.Adjustable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import org.jgraph.JGraph;

import conf.Conf;
import datos.Ejecucion;
import datos.FamiliaEjecuciones;

public class PanelFamiliaEjecuciones extends JPanel implements Scrollable {

	private static final long serialVersionUID = -6270875064574073828L;

	private static final int W = 300;
	private static final int H = 200;

	private static final int BORDE = 10;
	
	private final FamiliaEjecuciones familiaEjecuciones;
	private final FlowLayout layout = new FlowLayout();
	private final int hGap = this.layout.getHgap();
	private final int vGap = this.layout.getVgap();
	private Dimension size;

	public PanelFamiliaEjecuciones(FamiliaEjecuciones familiaEjecuciones) {
		setLayout(this.layout);
		this.familiaEjecuciones = familiaEjecuciones;
	}
	
	public void actualizar() {
		this.removeAll();
		
		for (Iterator<Ejecucion> iterator = this.familiaEjecuciones.getEjecuciones(); iterator
				.hasNext();) {
			this.add(new GraphPanel(this.familiaEjecuciones, iterator.next()));
		}

		int numeroEjecuciones = this.familiaEjecuciones.numeroEjecuciones();
		this.size = new Dimension(numeroEjecuciones * W
				+ (numeroEjecuciones + 1) * this.hGap, H + 2 * this.vGap);
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return this.size;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return getIncrement(orientation);
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return getIncrement(orientation);
	}

	private int getIncrement(int orientation) {
		if (orientation == Adjustable.HORIZONTAL) {
			return W + this.hGap;
		} else {
			return H + this.vGap;
		}
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	private static class GraphPanel extends JPanel implements MouseListener {

		private static final long serialVersionUID = 5487058416021891922L;

		private final FamiliaEjecuciones familiaEjecuciones;
		private final Ejecucion ejecucion;
		
		private BufferedImage snapshot;

		public GraphPanel(FamiliaEjecuciones familiaEjecuciones, Ejecucion ejecucion) {
			this.setBackground(Conf.colorPanel);
			
			this.familiaEjecuciones = familiaEjecuciones;
			this.ejecucion = ejecucion;
			
			this.addMouseListener(this);
			
			this.inicializarSnapshot();
			this.updateUI();
		}
		
		/* Obtenemos un primer snapshot del grafo para conocer el tamaño de la imagen,
		 * una vez obtenido, escalaremos el grafo para obtener un snapshot ajustado */
		private void inicializarSnapshot() {
			
			int imageWidth = W - BORDE * 2;
			int imageHeight = H - BORDE * 2;
			
			JGraph graph = this.ejecucion.obtenerGrafo();
			
			BufferedImage snapshotOriginal = graph.getImage(graph.getBackground(), 0);
			graph.setSize(snapshotOriginal.getWidth(), snapshotOriginal.getHeight());
			
			Dimension graphDimension = graph.getSize();
            double xScale = imageWidth / graphDimension.getWidth();
            double yScale = imageHeight / graphDimension.getHeight();
            double minScale = Math.min(xScale, yScale);
              
            graph.setScale(minScale, new Point2D.Double(0, 0));
            int grafoEscaladoWidth = new Double(Math.ceil((graphDimension.getWidth() + 50) * minScale)).intValue();
            int grafoEscaladoHeight = new Double(Math.ceil((graphDimension.getHeight() + 30) * minScale)).intValue();
            graph.setSize(grafoEscaladoWidth, grafoEscaladoHeight);       
            
            BufferedImage snapshot = graph.getImage(graph.getBackground(), 0);		
			this.snapshot = snapshot;
		}
		
		@Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);        
	        Graphics2D g2d = (Graphics2D) g;
	        
	        /* Pintamos la captura */
	        int xCenter = W / 2;
	        int yCenter = H / 2;	        
	        int snapshotXCenter = this.snapshot.getWidth() / 2;
	        int snapshotYCenter = this.snapshot.getHeight() / 2;	        
	        g2d.drawImage(this.snapshot, xCenter - snapshotXCenter, yCenter - snapshotYCenter, null);
	        
	        if (this.familiaEjecuciones.esEjecucionActiva(this.ejecucion)) {
		        /* Pintamos el rectángulo de selección */
		        g2d.setStroke(new BasicStroke(BORDE));
		        g2d.setPaint(new GradientPaint(80, 100, Color.RED, 8, 20, Color.BLUE, true));
		        g2d.draw(new Rectangle2D.Double(0, 0, W, H));
	        }
	    }
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(W, H);
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

		}

		/**
		 * Método que gestiona los eventos de ratón
		 * 
		 * @param e
		 *            evento de ratón
		 */
		@Override
		public void mouseExited(MouseEvent e) {

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
}
