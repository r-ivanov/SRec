package paneles;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import conf.Conf;

/**
 * Panel que contiene los botones de la visualizaci�n y la etiqueta que presenta
 * el t�tulo del panel
 * 
 * @author Antonio P�rez Carrasco
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
	 *            titulo del panel, que se mostrar� a trav�s de la etiqueta
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
	 * Permite establecer el t�tulo del panel de control y el PanelAlgoritmo al que pertenece.
	 * 
	 * @param titulopanel
	 *            titulo del panel, que se mostrar� a trav�s de la etiqueta
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
	 * Habilita y deshabilita los botones correspondientes al estado de la ejecuci�n,
	 * �nicamente se utiliza al redibujar el �rbol. 
	 */
	public void visualizar() {
		this.pbv.visualizar();
	}
	
	/**
	 * Establece el tooltip para los botones seg�n el idoma configurado.
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
	 * Habilita los botones y controles de visualizaci�n seg�n
	 * el estado actual.
	 */
	protected void habilitarControles() {
		this.pbv.habilitarControles();
	}
	
	/**
	 * Sit�a la visualizaci�n al final
	 */
	public void hacerFinal() {
		this.pbv.hacer_final();
	}

}
