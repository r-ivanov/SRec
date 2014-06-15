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
import datos.*;
import utilidades.*;
import ventanas.*;



public class CuadroPreguntaSeleccMetodosDYV extends CuadroPregunta implements ActionListener, KeyListener
{
	static final long serialVersionUID=01;
	
	String param;
	
	
	ClaseAlgoritmo clase=null;
	Preprocesador p=null;
	boolean fase2;
	
	
	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/
	public CuadroPreguntaSeleccMetodosDYV(JFrame frame, ClaseAlgoritmo clase, Preprocesador p)
	{
		//this(frame,"",o);
		//super(frame,Texto.get("PREG_ANIMNOGUARD",Conf.idioma), 	Texto.get("PREGMEN_ANIMNOGUARD",Conf.idioma),
		//		CuadroPregunta.getAnchoPorDefecto(), CuadroPregunta.getAltoPorDefecto());
		//aceptar.setText("GUARDAR");
		//cancelar.setText("NO GUARDAR");
		
	
		this.clase=clase;
		this.p=p;
		fase2=false;
		
		d=new JDialog(frame,true);
		this.titulo=Texto.get("PREG_SELECCMETODOSDYV",Conf.idioma);
		this.etiq=Texto.get("PREGMEN_SELECCMETODOSDYV",Conf.idioma);
	
		
		this.arranque(2);
		aceptar.setText(Texto.get("SI",Conf.idioma));
		cancelar.setText(Texto.get("NO",Conf.idioma));
		
		
		aceptar.updateUI();
		cancelar.updateUI();
		d.setVisible(true);
		if (fase2) {
			if (Conf.fichero_log)	Logger.log_write("¿Habilitar vistas para DYV? No");
			p.fase2(this.clase);
		}
	
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
			if (Conf.fichero_log)	Logger.log_write("¿Habilitar vistas para DYV? Sí");
			new CuadroSeleccionMetodos(clase,Ventana.thisventana,p);
		}
		else
		{
			//if (Conf.fichero_log)	Logger.log_write("¿Habilitar vistas para DYV? No");
			//p.fase2(this.clase); //Parece un problema con los hilos.
			fase2 = true;
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
		int tecla=e.getKeyCode();
		
		if ( e.getSource()==cancelar && ( tecla==KeyEvent.VK_KP_LEFT || tecla==KeyEvent.VK_LEFT ) )
		{
			cancelar.transferFocusBackward();
		}
		else if ( e.getSource()==aceptar && ( tecla==KeyEvent.VK_KP_RIGHT || tecla==KeyEvent.VK_RIGHT ) )
		{
			aceptar.transferFocus();
		}
	}


}


