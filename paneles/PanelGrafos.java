package paneles;

import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Scrollable;

import conf.Conf;
import datos.Ejecucion;
import datos.FamiliaEjecuciones;

/**
 * Panel que permite organizar visualmente distintos árboles, dada una familia
 * de ejecuciones a visualizar.
 * 
 * @author David Pastor Herranz
 */
public class PanelGrafos extends JPanel implements Scrollable {

	private static final long serialVersionUID = -6270875064574073828L;

	private static final int RATIO_HORIZONTAL_V = 3;
	private static final int RATIO_HORIZONTAL_H = 4;
	private static final int RATIO_VERTICAL_V = 4;
	private static final int RATIO_VERTICAL_H = 3;

	private static final int HGAP = 5;
	private static final int VGAP = 5;

	private final FamiliaEjecuciones familiaEjecuciones;

	private int anchuraElemento;
	private int alturaElemento;

	private int anchuraTotal;
	private int alturaTotal;

	/**
	 * Crea una nueva instancia.
	 * 
	 * @param familiaEjecuciones
	 *            Familia de ejecuciones a visualizar.
	 */
	public PanelGrafos(FamiliaEjecuciones familiaEjecuciones) {
		this.familiaEjecuciones = familiaEjecuciones;
	}

	/**
	 * Pinta la familia de ejecuciones dado un tamaño y una orientación.
	 * 
	 * @param tamanio
	 *            Altura del panel si la orientación es horizontal, o Anchura si
	 *            es vertical.
	 * @param orientacion
	 *            Orientación del panel, Conf.PANEL_HORIZONTAL o
	 *            Conf.PANEL_VERTICAL.
	 */
	public void pintar(int tamanio, int orientacion) {
		this.removeAll();

		int numeroEjecuciones = this.familiaEjecuciones.numeroEjecuciones();
		Dimension gap;
		if (orientacion == Conf.PANEL_HORIZONTAL) {
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.alturaElemento = tamanio - 2 * VGAP;
			this.alturaTotal = tamanio;
			this.anchuraElemento = this.alturaElemento * RATIO_HORIZONTAL_H
					/ RATIO_HORIZONTAL_V;
			this.anchuraTotal = numeroEjecuciones * this.anchuraElemento
					+ (numeroEjecuciones + 1) * HGAP;
			gap = new Dimension(HGAP, this.alturaTotal);
		} else {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.anchuraElemento = tamanio - 2 * HGAP;
			this.anchuraTotal = tamanio;
			this.alturaElemento = this.anchuraElemento * RATIO_VERTICAL_V
					/ RATIO_VERTICAL_H;
			this.alturaTotal = numeroEjecuciones * this.alturaElemento
					+ (numeroEjecuciones + 1) * VGAP;
			gap = new Dimension(this.anchuraTotal, VGAP);
		}

		this.add(Box.createRigidArea(gap));
		for (Iterator<Ejecucion> iterator = this.familiaEjecuciones
				.getEjecuciones(); iterator.hasNext();) {
			this.add(new PanelSnapshotGrafo(this.familiaEjecuciones, iterator
					.next(), this.anchuraElemento, this.alturaElemento));
			this.add(Box.createRigidArea(gap));
		}
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(this.anchuraTotal, this.alturaTotal);
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return this.getIncrement(orientation);
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return this.getIncrement(orientation);
	}

	/**
	 * Devuelve el incremento de scroll que debe aplicarse cada vez que se
	 * scrollea hacia un lado o el otro.
	 * 
	 * @param orientation
	 *            Orientación del panel.
	 * 
	 * @return Incremento a aplicar.
	 */
	private int getIncrement(int orientation) {
		if (orientation == Adjustable.HORIZONTAL) {
			return this.anchuraElemento / 2;
		} else {
			return this.alturaElemento / 2;
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