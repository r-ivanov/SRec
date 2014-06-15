/**
	Cuadro de identificación del programa
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;




import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

import conf.*;
import botones.*;
import utilidades.*;
import ventanas.*;

public class CuadroAcercade extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=42;

	static final boolean depurar=false;

	final int anchoCuadro=580;
	final int altoCuadro=200;
	
	
	JLabel etiqueta1, etiqueta2;
	JTextPane texto;
	JPanel panel;

	BorderLayout bl;
	GridLayout gl;
	BotonAceptar aceptar;
	
	JDialog dialogo=null;

	/**
		Constructor: Genera un nuevo cuadro de identifiación del programa
		
		@param ventanaVisualizador ventana a la que quedará asociado el cuadro
	*/
	public CuadroAcercade()
	{	
		dialogo =new JDialog(Ventana.thisventana,true);
		this.start();
	}
	
	/**
		Genera un nuevo cuadro de identifiación del programa
	*/
	public void run()
	{
		// Etiqueta 1
		this.etiqueta1=new JLabel();
		Icon imagen = new ImageIcon( "imagenes/cuadro_acercade.gif");
		etiqueta1.setIcon(imagen);
		//etiqueta1.setBackground(Conf.colorCuadroDialogo);
		etiqueta1.addKeyListener(this);
		etiqueta1.setToolTipText(Texto.get("URJC_SREC",Conf.idioma));

		// Etiqueta 2
		texto= new JTextPane();
		texto.setAlignmentX(0);
		texto.setAlignmentY(0);
		
		texto.setText("\r\n\r\n"+Texto.get("APLIC",Conf.idioma)+
					"\r\n"+Texto.get("URJC_SREC_02",Conf.idioma)+" 1.2\r\n\r\n2010\n\n\n\n");
		
		//texto.setText("\r\n\r\nSRec, Sistema de animación de la Recursividad\r\n\r\nDesarrollado por Antonio Pérez Carrasco\r\n\r\n11 Septiembre 2006 - ???? 2007\n\n\n\n");
		texto.setEditable(false);
		//texto.setFont(Conf.fuenteCuadro);
		//texto.setBackground(Conf.colorCuadroDialogo);
		texto.addKeyListener(this);
		
		// Panel Boton
		JPanel panelBoton=new JPanel();
		//panelBoton.setBackground(Conf.colorCuadroDialogo);
		aceptar = new BotonAceptar();
		//aceptar.addActionListener(this);
		aceptar.addMouseListener(this);
		panelBoton.add(aceptar);
				
		// Panel general
		bl= new BorderLayout();
		panel = new JPanel();
		panel.setLayout(bl);
		
		panel.add(etiqueta1,BorderLayout.WEST);
		panel.add(texto,BorderLayout.EAST);
		panel.add(panelBoton,BorderLayout.SOUTH);
		
		texto.setBackground(panel.getBackground());
		
		dialogo.getContentPane().add(panel);
		int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
		dialogo.setLocation(coord[0],coord[1]);
		
		dialogo.setTitle(Texto.get("URJC_ACERCADE",Conf.idioma));
		dialogo.setSize(anchoCuadro,altoCuadro);
		
		dialogo.setResizable(false);
		dialogo.setVisible(true);
	}

	
		
	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/		
	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource()==aceptar)
			this.dialogo.setVisible(false);
	}

	
	
	
	/**
		Gestiona los eventos de teclado
		
		@param e eevnto de teclado
	*/
	public void keyPressed(KeyEvent e)
	{
		int code=e.getKeyCode();
		if (code==KeyEvent.VK_ESCAPE || code==KeyEvent.VK_ENTER)
			dialogo.setVisible(false);
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e eevnto de teclado
	*/	
	public void keyReleased(KeyEvent e)
	{
		int code=e.getKeyCode();
		if (code==KeyEvent.VK_ESCAPE || code==KeyEvent.VK_ENTER)
			dialogo.setVisible(false);
	}

	/**
		Gestiona los eventos de teclado
		
		@param e eevnto de teclado
	*/
	public void keyTyped(KeyEvent e)
	{
		int code=e.getKeyCode();
		if (code==KeyEvent.VK_ESCAPE || code==KeyEvent.VK_ENTER)
			dialogo.setVisible(false);
	}

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/				
	public void mouseClicked(MouseEvent e) 
	{
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/				
	public void mouseEntered(MouseEvent e) 
	{
		//if (e.getComponent()==aceptar)
		//	aceptar.setNaranja();
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/				
	public void mouseExited(MouseEvent e) 
	{
		//if (e.getComponent()==aceptar)
		//	aceptar.setVerde();
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/				
	public void mousePressed(MouseEvent e) 
	{
		if ( e.getSource()==aceptar)
			this.dialogo.setVisible(false);
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/				
	public void mouseReleased(MouseEvent e)
	{
	}
}
