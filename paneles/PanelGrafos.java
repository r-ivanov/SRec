package paneles;

import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import conf.Conf;
import datos.Ejecucion;
import datos.FamiliaEjecuciones;

public class PanelGrafos extends JPanel implements Scrollable {

	private static final long serialVersionUID = -6270875064574073828L;
	
	private static final int RATIO_HORIZONTAL_V = 3;
	private static final int RATIO_HORIZONTAL_H = 4;
	private static final int RATIO_VERTICAL_V = 4;
	private static final int RATIO_VERTICAL_H = 3;
	
	private final FamiliaEjecuciones familiaEjecuciones;
	
	private final FlowLayout layout;
	private final int hGap;
	private final int vGap;
	
	private int anchuraElemento;
	private int alturaElemento;
	
	private int anchuraTotal;
	private int alturaTotal;

	public PanelGrafos(FamiliaEjecuciones familiaEjecuciones) {
		this.layout = new FlowLayout();
		this.hGap = this.layout.getHgap();
		this.vGap = this.layout.getVgap();
		this.familiaEjecuciones = familiaEjecuciones;
		
		setLayout(this.layout);
	}
	
	public void pintar(int anchura, int altura, int orientacion) {
		this.removeAll();
		
		int numeroEjecuciones = this.familiaEjecuciones.numeroEjecuciones();
		if (orientacion == Conf.PANEL_HORIZONTAL) {			
			this.alturaElemento = altura - 2 * this.vGap;
			this.alturaTotal = altura;					
			this.anchuraElemento = this.alturaElemento * RATIO_HORIZONTAL_H / RATIO_HORIZONTAL_V;
			this.anchuraTotal = numeroEjecuciones * this.anchuraElemento + (numeroEjecuciones + 1) * this.hGap;
		} else {			
			this.anchuraElemento = anchura - 2 * this.hGap;
			this.anchuraTotal = anchura;
			this.alturaElemento = this.anchuraElemento * RATIO_VERTICAL_V / RATIO_VERTICAL_H;
			this.alturaTotal = numeroEjecuciones * this.alturaElemento + (numeroEjecuciones + 1) * this.vGap;
		}
		
		for (Iterator<Ejecucion> iterator = this.familiaEjecuciones.getEjecuciones(); iterator
				.hasNext();) {
			this.add(new PanelSnapshotGrafo(this.familiaEjecuciones, iterator.next(), this.anchuraElemento, this.alturaElemento));
		}
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(this.anchuraTotal, this.alturaTotal);
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
			return this.anchuraElemento + this.hGap;
		} else {
			return this.alturaElemento + this.vGap;
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
}