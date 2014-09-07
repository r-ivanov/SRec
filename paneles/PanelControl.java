package paneles;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import conf.Conf;

/**
 * Panel que contiene los botones de la visualización y la etiqueta que presenta
 * el título del panel
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
class PanelControl extends JPanel {
	static final long serialVersionUID = 14;

	private JLabel etiqueta;
	private PanelBotonesVisualizacionArbol pbv;

	/**
	 * Constructor: crea un nuevo panel de control
	 * 
	 * @param titulopanel
	 *            titulo del panel, que se mostrará a través de la etiqueta
	 * @param pa
	 *            panel al que pertenece este panel de control
	 */
	public PanelControl(String titulopanel, PanelAlgoritmo pa) {
		this.setLayout(new BorderLayout());
		this.requestFocusInWindow();

		// Etiqueta superior
		this.etiqueta = new JLabel(titulopanel);
		this.etiqueta.setFont(Conf.fuenteTitulo);
		this.add(this.etiqueta, BorderLayout.CENTER);
		this.etiqueta.setHorizontalAlignment(0);

		// Panel superior de botones
		this.pbv = new PanelBotonesVisualizacionArbol(pa);
		this.add(this.pbv, BorderLayout.EAST);
	}
	
	/**
	 * Permite establecer el título del panel de control y el PanelAlgoritmo al que pertenece.
	 * 
	 * @param titulopanel
	 *            titulo del panel, que se mostrará a través de la etiqueta
	 * @param pa
	 *            panel al que pertenece este panel de control
	 */
	public void setValores(String titulopanel, PanelAlgoritmo pa) {
		this.requestFocusInWindow();

		// Etiqueta superior
		this.etiqueta.setText(titulopanel);

		// Panel superior de botones
		this.pbv.setValores(pa);
	}
	
	/**
	 * Habilita y deshabilita los botones correspondientes al estado de la ejecución,
	 * únicamente se utiliza al redibujar el árbol. 
	 */
	public void visualizar() {
		this.pbv.visualizar();
	}
	
	/**
	 * Establece el tooltip para los botones según el idoma configurado.
	 */
	public void idioma() {
		this.pbv.setToolTipText();
	}
	
	/**
	 * Establece los tooltips para cada uno de los botones.
	 */
	protected void deshabilitarControles() {
		this.pbv.deshabilitarControles();
	}
	
	/**
	 * Habilita los botones y controles de visualización según
	 * el estado actual.
	 */
	protected void habilitarControles() {
		this.pbv.habilitarControles();
	}
	
	/**
	 * Sitúa la visualización al final
	 */
	public void hacerFinal() {
		this.pbv.hacer_final();
	}

}
