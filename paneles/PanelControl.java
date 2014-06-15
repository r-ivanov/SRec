/**
	Panel que contiene los botones de la visualización y la etiqueta que presenta el título del panel
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/


package paneles;


import java.awt.BorderLayout;



import javax.swing.JLabel;

import javax.swing.JPanel;

import conf.*;



class PanelControl extends JPanel //implements ActionListener
{
	static final long serialVersionUID=14;

	JLabel etiqueta;
	PanelBotonesVisualizacionArbol pbv;

	
	/**
		Constructor: crea un nuevo panel de control
		
		@param traza traza de la ejecución del método
		@param pArbol panel de traza asociado a este panel de control (comparten traza)
		@param titulopanel titulo del panel, que se mostrará a través de la etiqueta
		@param pa panel al que pertenece este panel de control
	*/
	public PanelControl(PanelArbol pArbol, PanelPila pPila, PanelTraza pTraza, String titulopanel, PanelAlgoritmo pa)
	{
		this.setLayout(new BorderLayout());
		this.requestFocusInWindow();
		
		// Etiqueta superior
		etiqueta = new JLabel(titulopanel);
		etiqueta.setFont(Conf.fuenteTitulo);
		this.add(etiqueta,BorderLayout.CENTER);
		etiqueta.setHorizontalAlignment(0);
		
		// Panel superior de botones
		pbv = new PanelBotonesVisualizacionArbol(pa);
		this.add(pbv,BorderLayout.EAST);
		
		//this.setBackground(Conf.colorFrontalPanelContenedor);
	}
	
	
	public void setValores(String titulopanel, PanelAlgoritmo pa)
	{
		this.requestFocusInWindow();
		
		// Etiqueta superior
		etiqueta.setText(titulopanel);
		
		// Panel superior de botones
		pbv.setValores(pa);
	}
	
	public void visualizar()
	{
		pbv.visualizar();
	}
	
	
	public void idioma()
	{
		this.pbv.setToolTipText();
	}
	
	
	protected void deshabilitarControles()
	{
		pbv.deshabilitarControles();
	}
	
	protected void habilitarControles()
	{
		pbv.habilitarControles();
	}
	
	public void hacerFinal()
	{
		pbv.hacer_final();
	}
	
}

