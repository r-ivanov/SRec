/**
	Representa el cuadro de error
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

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
import datos.*;
import utilidades.*;
import ventanas.*;



public class CuadroPreguntaNuevaVisualizacion extends CuadroPregunta implements ActionListener, KeyListener
{
	static final long serialVersionUID=01;

	String accion;

	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/
	public CuadroPreguntaNuevaVisualizacion(JFrame frame,String accion)
	{
		super(frame,Texto.get("PREG_DESCART",Conf.idioma), 	Texto.get("PREGMEN_DESCART",Conf.idioma),2,
			CuadroPregunta.getAnchoPorDefecto(), CuadroPregunta.getAltoPorDefecto());
		this.accion=accion;
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
			if (accion.equals("cargar"))
				new CargadorTraza();
			else if (accion.equals("procesar"))
				new Preprocesador();
			else if (accion.equals("procesar de nuevo"))
			{
				String direccionCompleta=Ventana.thisventana.getClase().getPath();
				String path[]=new String[2];
				path[0]=direccionCompleta.substring(0,direccionCompleta.lastIndexOf("\\")+1);
				path[1]=direccionCompleta.substring(direccionCompleta.lastIndexOf("\\")+1,direccionCompleta.length());
				new Preprocesador(path);
				Ventana.thisventana.setClasePendienteProcesar(false);
			}
			else if (accion.equals("cargarGIF"))
			{
				Ventana.thisventana.cargarGIF();
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