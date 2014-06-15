/**
	Representa el cuadro de error
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

import conf.*;
import opciones.*;
import datos.*;
import utilidades.*;
import ventanas.*;



public class CuadroPreguntaSobreescribir extends CuadroPregunta implements ActionListener, KeyListener
{
	static final long serialVersionUID=01;

	Object objeto;
	String param;
	JComponent componenteUI;

	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/
	public CuadroPreguntaSobreescribir(JFrame frame,Object o)
	{
		this(frame,"",o,null);
	}	
		
		
		
		
	public CuadroPreguntaSobreescribir(JFrame frame,String param,Object o,JComponent c)
	{	
		super(frame,Texto.get("PREG_ARCHEXIST",Conf.idioma), 	Texto.get("PREGMEN_ARCHEXIST",Conf.idioma),2,
			CuadroPregunta.getAnchoPorDefecto(), CuadroPregunta.getAltoPorDefecto());
		this.param=param;
		this.objeto=o;
		this.componenteUI=c;
	}
	
	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param ventana ventana a la que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/
	
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==aceptar)
		{
			if (this.objeto.getClass().getCanonicalName().contains("AlmacenadorTraza"))
				((AlmacenadorTraza)(this.objeto)).ejecutar();
			else if (this.objeto.getClass().getCanonicalName().contains("FotografoArbol"))	
			{
				if (this.param.charAt(0)=='1')
					((FotografoArbol)(this.objeto)).hacerCapturaUnica2(componenteUI,Integer.parseInt(this.param.substring(1)));
				else if (this.param.charAt(0)=='A')
					((FotografoArbol)(this.objeto)).capturarAnimacionGIF2(componenteUI,Integer.parseInt(this.param.substring(1)));
			}
			else if (this.objeto.getClass().getCanonicalName().contains("GestorOpciones"))
				((GestorOpciones)(this.objeto)).crearArchivo(this.param);
			else if (this.objeto.getClass().getCanonicalName().contains("VentanaVisualizador"))
			{
				if (this.param.equals("html"))
					((Ventana)(this.objeto)).guardarTraza2(this.param);
				else if (this.param.equals("gif"))
					((Ventana)(this.objeto)).cargarGIF2();
			}
			else if (this.objeto.getClass().getCanonicalName().contains("CuadroNuevaClase"))
			{
				((CuadroNuevaClase)(this.objeto)).archivoCorrecto();
			}
				
				
		}

		this.d.setVisible(false);
	}
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyPressed(KeyEvent e)
	{
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
	

}