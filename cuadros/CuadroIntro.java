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


public class CuadroIntro extends Thread //implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=42;

	static final boolean depurar=false;

	final int anchoCuadro=600;
	final int altoCuadro=300;
	
	
	JLabel etiqueta1;
	JPanel panel;

	BorderLayout bl;
	GridLayout gl;
	
	JDialog dialogo=null;

	/**
		Constructor: Genera un nuevo cuadro de identificación del programa
	*/
	public CuadroIntro()
	{	
		dialogo =new JDialog(Ventana.thisventana,true);
		dialogo.setAlwaysOnTop(true);
		this.start();
	}
	
	/**
		Genera un nuevo cuadro de identifiación del programa
	*/
	public synchronized void run()
	{
		// Etiqueta 1
		this.etiqueta1=new JLabel();
		Icon imagen = new ImageIcon( "imagenes/ImagenIntro_"+idiomaImagen()+".png");
		etiqueta1.setIcon(imagen);
		
		// Panel general
		bl= new BorderLayout();
		panel = new JPanel();
		panel.setLayout(bl);
		
		panel.add(etiqueta1,BorderLayout.WEST);

		dialogo.getContentPane().add(panel);
		int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
		dialogo.setLocation(coord[0],coord[1]);
		
		dialogo.setTitle("");
		dialogo.setUndecorated(true);
		dialogo.setSize(anchoCuadro,altoCuadro);
		
		dialogo.setResizable(false);
		
		final JDialog dialogoF=dialogo;

		new Thread (){
			public synchronized void run()
			{
				try { wait(3500); } catch(InterruptedException ie) {}
				dialogoF.setVisible(false);
			}
		}.start();
		
		dialogo.setVisible(true);
	}
	
	private String idiomaImagen()
	{
		if (Conf.idioma.equals("es"))
			return "es";
		else
			return "en";
	}
}
