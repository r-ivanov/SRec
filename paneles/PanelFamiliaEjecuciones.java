package paneles;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import conf.Conf;
import datos.FamiliaEjecuciones;

public class PanelFamiliaEjecuciones extends JScrollPane {

	private static final long serialVersionUID = 1668056497167133807L;
	
	private static final int MARGEN_INFERIOR = 20;
	private static final int MARGEN_LATERAL = 20;

	private final PanelGrafos panelGrafos;

	public PanelFamiliaEjecuciones(FamiliaEjecuciones familiaEjecuciones) {
		this.panelGrafos = new PanelGrafos(familiaEjecuciones);
		this.setViewportView(this.panelGrafos);
		this.getViewport().addChangeListener(new ChangeListener() {						
			@Override
			public void stateChanged(ChangeEvent e) {
				getHorizontalScrollBar().setEnabled(
                        getViewport().getWidth() <
                        getViewport().getView().getWidth());
				getVerticalScrollBar().setEnabled(
                        getViewport().getHeight() <
                        getViewport().getView().getHeight());
			}
		});
	}

	public void pintar(int size, int orientacion) {
		if (orientacion == Conf.PANEL_HORIZONTAL) {
			this.setMinimumSize(new Dimension(0, size));
			this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			this.panelGrafos.pintar(size - MARGEN_INFERIOR, orientacion);
		} else {
			this.setMinimumSize(new Dimension(size, 0));
			this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			this.panelGrafos.pintar(size - MARGEN_LATERAL, orientacion);
		}
	}
}
