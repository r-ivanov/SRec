package paneles;

import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
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

	private final FlowLayout layout = new FlowLayout();
	private final int hGap = this.layout.getHgap();
	private final int vGap = this.layout.getVgap();
	private final Dimension size;

	public PanelFamiliaEjecuciones(FamiliaEjecuciones familiaEjecuciones) {
		setLayout(this.layout);
		for (Iterator<Ejecucion> iterator = familiaEjecuciones.getEjecuciones(); iterator
				.hasNext();) {
			this.add(new GraphPanel(iterator.next().obtenerGrafo()));
		}

		int numeroEjecuciones = familiaEjecuciones.numeroEjecuciones();
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

	private static class GraphPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5487058416021891922L;

		public GraphPanel(JGraph graph) {
			super(new GridBagLayout());
			this.setBackground(Conf.colorPanel);

			Dimension graphDimension = graph.getSize();
			double xScale = (W - BORDE * 2) / graphDimension.getWidth();
			double yScale = (H - BORDE * 2) / graphDimension.getHeight();
			double minScale = Math.min(xScale, yScale);
			graph.setScale(minScale);

			graph.setBackground(Conf.colorPanel);
			graph.setAutoResizeGraph(false);
			graph.setSizeable(false);

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			this.add(graph, gbc);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(W, H);
		}
	}
}
