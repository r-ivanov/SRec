
package cuadros;




import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import conf.*;
import botones.*;
import opciones.*;
import datos.*;
import utilidades.*;
import ventanas.*;



public class CuadroPreguntaEdicionNoGuardada extends CuadroPregunta implements ActionListener, KeyListener
{
	static final long serialVersionUID=01;

	String accion;
	String param;

	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/
	public CuadroPreguntaEdicionNoGuardada(String accion)
	{
		this.accion=accion;
		
		d=new JDialog(Ventana.thisventana,true);
		if(accion.equals("guardar")) {
			this.titulo=Texto.get("PREG_CLASNOGUARD",Conf.idioma);
			this.etiq=Texto.get("PREGMEN_CLASNOGUARD",Conf.idioma);
		} else {
			this.titulo=Texto.get("PREG_EDITNOGUARD",Conf.idioma);
			this.etiq=Texto.get("PREGMEN_EDITNOGUARD",Conf.idioma);
		}

		
		this.arranque(2);
		aceptar.setText(Texto.get("SI",Conf.idioma));
		cancelar.setText(Texto.get("NO",Conf.idioma));
		
		aceptar.updateUI();
		cancelar.updateUI();
		d.setVisible(true);

	}	

	
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
			Ventana.thisventana.guardarClase();
		
		if ((e.getSource()==aceptar) && (accion.equals("guardar"))) {
			Ventana.thisventana.procesarClaseSeleccionarMetodo();
		}
		if ((e.getSource()==cancelar) && (accion.equals("guardar"))) {
			Ventana.thisventana.iniciarNuevaVisualizacionSelecMetodo();
		}
		
		if (accion.equals("cargarClase"))
			Ventana.thisventana.gestionOpcionCargarClase();
		
		else if (accion.equals("cargarAnimacion"))
			Ventana.thisventana.gestionOpcionCargarAnimacion();
		
		else if (accion.equals("cargarAnimacionGIF"))
			Ventana.thisventana.gestionOpcionCargarAnimacionGIF();
		
		else if (accion.equals("cierreVentana"))
			Ventana.thisventana.activarCierre();
		
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