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
		this.dialogo = new JDialog(ventana, true);
	}
}
