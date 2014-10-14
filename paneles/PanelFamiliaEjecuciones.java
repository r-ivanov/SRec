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

	protected int width;
	protected int height;

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
	
	public void actualizar(int height, int width, int orientation) {
		this.removeAll();
		
		int numeroEjecuciones = this.familiaEjecuciones.numeroEjecuciones();
		
		if (orientation == Conf.PANEL_HORIZONTAL) {
			this.height = height;
			this.width = (height * 4) / 3;
			this.size = new Dimension(numeroEjecuciones * this.width
					+ (numeroEjecuciones + 1) * this.hGap, this.height);
		} else {
			this.width = width;
			this.height = (width * 3) / 4;
			this.size = new Dimension(this.width, numeroEjecuciones * this.height
					+ (numeroEjecuciones + 1) * this.vGap);
		}
			
		for (Iterator<Ejecucion> iterator = this.familiaEjecuciones.getEjecuciones(); iterator
				.hasNext();) {
			this.add(new GraphPanel(this.familiaEjecuciones, iterator.next(), this.height, this.width));
		}		
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
			return this.width + this.hGap;
		} else {
			return this.height + this.vGap;
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
		
		private int height;
		private int width;
		
		public GraphPanel(FamiliaEjecuciones familiaEjecuciones, Ejecucion ejecucion,
				int height, int width) {
			this.setBackground(Conf.colorPanel);
			
			this.height = height;
			this.width = width;
			
			this.familiaEjecuciones = familiaEjecuciones;
			this.ejecucion = ejecucion;
			
			this.addMouseListener(this);
			
			this.inicializarSnapshot();
			this.updateUI();
		}
		
		/* Obtenemos un primer snapshot del grafo para conocer el tamaño de la imagen,
		 * una vez obtenido, escalaremos el grafo para obtener un snapshot ajustado */
		private void inicializarSnapshot() {
			
			int imageWidth = this.width - BORDE * 2;
			int imageHeight = this.height - BORDE * 2;
			
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
	        int xCenter = this.width / 2;
	        int yCenter = this.height / 2;	        
	        int snapshotXCenter = this.snapshot.getWidth() / 2;
	        int snapshotYCenter = this.snapshot.getHeight() / 2;	        
	        g2d.drawImage(this.snapshot, xCenter - snapshotXCenter, yCenter - snapshotYCenter, null);
	        
	        if (this.familiaEjecuciones.esEjecucionActiva(this.ejecucion)) {
		        /* Pintamos el rectángulo de selección */
		        g2d.setStroke(new BasicStroke(BORDE));
		        g2d.setPaint(new GradientPaint(80, 100, Color.RED, 8, 20, Color.BLUE, true));
		        g2d.draw(new Rectangle2D.Double(0, 0, this.width, this.height));
	        }
	    }
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(this.width, this.height);
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
