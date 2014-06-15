package ventanas;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import cuadros.*;

public class GestorVentanaEditor implements WindowListener, WindowStateListener
{
	VentanaVerCodigo vvc=null;
	

	
	public GestorVentanaEditor(VentanaVerCodigo vvc)
	{
		this.vvc=vvc;
	}
	
		public void windowActivated(WindowEvent e)
	{
		
	}

	public void windowClosed(WindowEvent e)
	{

	}

	public void windowClosing(WindowEvent e)
	{
		vvc.preguntaEdicionNoGuardada();
		
	}

	public void windowDeactivated(WindowEvent e)
	{

	}

	public void windowDeiconified(WindowEvent e)
	{
		
	}

	public void windowIconified(WindowEvent e)
	{
		
	}


	public void windowOpened(WindowEvent e)
	{

		
	}


	public void windowStateChanged(WindowEvent arg0)
	{

	}

}
