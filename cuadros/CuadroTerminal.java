package cuadros;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;

import ventanas.Ventana;

public class CuadroTerminal implements WindowListener{
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
		this.dialogo.addWindowListener(this);
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
		if(this.estaVisible)
			this.estaVisible = false;
		else
			this.estaVisible = true;
		this.dialogo.setVisible(this.estaVisible);
		return this.estaVisible;
	}
	
	//********************************************************************************
    // 			LISTENERS
    //********************************************************************************
	
	//***************************************
    // 			WINDOW LISTENER
    //***************************************
	
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.ventana.abrirCerrarTerminal();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
