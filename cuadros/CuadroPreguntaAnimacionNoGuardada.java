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

import javax.swing.JDialog;
import javax.swing.JFrame;

import conf.*;
import utilidades.*;
import ventanas.*;



public class CuadroPreguntaAnimacionNoGuardada extends CuadroPregunta implements ActionListener, KeyListener
{
	static final long serialVersionUID=01;
	
	String param;
	


	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/
	public CuadroPreguntaAnimacionNoGuardada(JFrame frame)
	{
		//this(frame,"",o);
		//super(frame,Texto.get("PREG_ANIMNOGUARD",Conf.idioma), 	Texto.get("PREGMEN_ANIMNOGUARD",Conf.idioma),
		//		CuadroPregunta.getAnchoPorDefecto(), CuadroPregunta.getAltoPorDefecto());
		//aceptar.setText("GUARDAR");
		//cancelar.setText("NO GUARDAR");
		

		d=new JDialog(frame,true);
		this.titulo=Texto.get("PREG_ANIMNOGUARD",Conf.idioma);
		this.etiq=Texto.get("PREGMEN_ANIMNOGUARD",Conf.idioma);

		
		this.arranque(3);
		ignorar.setText(Texto.get("NO_GUARDAR",Conf.idioma));
		aceptar.setText(Texto.get("GUARDAR",Conf.idioma));
		
		
		aceptar.updateUI();
		ignorar.updateUI();
		cancelar.updateUI();
		d.setVisible(true);

	}	
		
		
		
		/*
	public CuadroPreguntaAnimacionNoGuardada(JFrame frame,String param,Object o)
	{	
		super(frame,Texto.get("PREG_ANIMNOGUARD",Conf.idioma), 	Texto.get("PREGMEN_ANIMNOGUARD",Conf.idioma),
			CuadroPregunta.getAnchoPorDefecto(), CuadroPregunta.getAltoPorDefecto());
		this.param=param;
		this.objeto=o;
	}*/
	
	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param ventana ventana a la que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/
	
	
	public void actionPerformed(ActionEvent e)
	{
		this.d.setVisible(false);
		if (e.getSource()==aceptar)
		{
			Ventana.thisventana.guardadoTraza();
		}
		if (e.getSource()!=cancelar)
		{
			Ventana.thisventana.cerrar();
		} 
		

		
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