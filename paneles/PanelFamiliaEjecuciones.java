package paneles;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import conf.Conf;
import datos.FamiliaEjecuciones;

public class PanelFamiliaEjecuciones extends JScrollPane {
	
	private static final long serialVersionUID = 1668056497167133807L;
	private static final int MARGEN_INFERIOR = 18;
	private static final int MARGEN_LATERAL = 18;
	
	private final FamiliaEjecuciones familiaEjecuciones;
	private final PanelGrafos panelGrafos;
	
	private Dimension size;
	
	public PanelFamiliaEjecuciones(FamiliaEjecuciones familiaEjecuciones) {
		this.familiaEjecuciones = familiaEjecuciones;
		this.panelGrafos = new PanelGrafos(familiaEjecuciones);
		this.size = new Dimension();
		this.setViewportView(this.panelGrafos);
	}
	
	public void pintar(int anchura, int altura, int orientacion) {
		
		this.size = new Dimension(anchura, altura);
		
		if (orientacion == Conf.PANEL_HORIZONTAL) {
			this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			this.panelGrafos.pintar(anchura, altura - MARGEN_INFERIOR, orientacion);
		} else {
			this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			this.panelGrafos.pintar(anchura - MARGEN_LATERAL, altura, orientacion);
		}		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return this.size;
	}
}


