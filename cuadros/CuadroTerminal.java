package cuadros;

import javax.swing.JDialog;

import ventanas.Ventana;

public class CuadroTerminal {
	//********************************************************************************
    // 			VARIABLES
    //********************************************************************************	
	
	//***************************************
    // 			CUADRO TERMINAL
    //***************************************
	boolean estaVisible;
	private Ventana ventana;
	private JDialog dialogo;
	
	//********************************************************************************
    // 			CONSTRUCTOR
    //********************************************************************************	
	public CuadroTerminal(Ventana ventana) {
		this.estaVisible = false;
		this.ventana = ventana;
		this.dialogo = new JDialog(ventana, false);
	}
	
	//********************************************************************************
    // 			MÉTODOS PÚBLICOS
    //********************************************************************************
	
	//***************************************
    // 			CUADRO TERMINAL
    //***************************************
	
	/**
	 * Método que abre o cierra la terminal
	 * 
	 * @return
	 * 		Nuevo valor de la visibilidad de la terminal
	 */
	public boolean abrirCerrarTerminal() {
		this.estaVisible = !this.estaVisible;
		this.dialogo.setVisible(this.estaVisible);
		return this.estaVisible;
	}
}
