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
import utilidades.*;
import ventanas.*;

public class CuadroError extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=01;

	private static int ancho = 550;
	private static int alto  = 100;
	JLabel etiqueta, imagen;
	BotonAceptar aceptar;

	JPanel panel, panelBoton, panelImagen, panelDerecha, panelEtiqueta;

	String titulo;
	String etiq;
	String comando;
	String boton2;

	BorderLayout bl, bl0, bl1, bl2;
	
	JDialog d;

	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/
	public CuadroError(JDialog dialogo, String titulo, String etiq)
	{
		this(dialogo,titulo,etiq,ancho,alto);
	}
	
	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param ventana ventana a la que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/
	public CuadroError(JFrame ventana, String titulo, String etiq)
	{
		this(ventana,titulo,etiq,ancho,alto);
	}

	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
		@param anc ancho del cuadro
		@param alt alto del cuadro
	*/
	public CuadroError(JDialog dialogo,String titulo, String etiq, int anc, int alt)
	{
		this(dialogo,titulo,etiq,"","",anc,alt);
	}

	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param ventana ventana a la que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
		@param anc ancho del cuadro
		@param alt alto del cuadro
	*/	
	public CuadroError(JFrame ventana,String titulo, String etiq, int anc, int alt)
	{
		this(ventana,titulo,etiq,"","",anc,alt);
	}

	/**
		Constructor: crea una nueva instancia del cuadro de error. DESECHADO
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
		@param comando comando que ejecutará el cuadro
		@param boton2 texto que aparecerá en el segundo botón de acción
	*/	
	public CuadroError(JDialog dialogo, String titulo, String etiq, String comando, String boton2)
	{
		this(dialogo,titulo,etiq,comando,boton2,ancho,alto);
	}
	
	/**
		Constructor: crea una nueva instancia del cuadro de error. DESECHADO
		
		@param ventana ventana a la que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
		@param comando comando que ejecutará el cuadro
		@param boton2 texto que aparecerá en el segundo botón de acción
	*/	
	public CuadroError(JFrame ventana, String titulo, String etiq, String comando, String boton2)
	{
		this(ventana,titulo,etiq,comando,boton2,ancho,alto);
	}

	/**
		Constructor: crea una nueva instancia del cuadro de error. DESECHADO
		
		@param ventana ventana a la que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
		@param comando comando que ejecutará el cuadro
		@param boton2 texto que aparecerá en el segundo botón de acción
		@param anc ancho del cuadro
		@param alt alto del cuadro
	*/	
	public CuadroError(JFrame ventana,String titulo, String etiq, String comando, String boton2, int anc, int alt)
	{
		d=new JDialog(ventana,true);
		this.titulo=titulo;
		this.etiq=etiq;
		this.comando=comando;
		this.boton2=boton2;
		this.ancho=anc;
		this.alto=alt;
		this.start();
	}

	/**
		Constructor: crea una nueva instancia del cuadro de error. DESECHADO
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
		@param comando comando que ejecutará el cuadro
		@param boton2 texto que aparecerá en el segundo botón de acción
		@param anc ancho del cuadro
		@param alt alto del cuadro
	*/		
	public CuadroError(JDialog dialogo,String titulo, String etiq, String comando, String boton2, int anc, int alt)
	{
		d=new JDialog(dialogo,true);
		this.titulo=titulo;
		this.etiq=etiq;
		this.comando=comando;
		this.boton2=boton2;
		this.ancho=anc;
		this.alto=alt;
		this.start();
	}
	
	/**
		crea una nueva instancia del cuadro de error. DESECHADO
	*/
	public void run()
	{
		this.comando=comando;

		// Etiqueta para icono
		panelImagen = new JPanel();
		imagen=new JLabel(new ImageIcon("imagenes/error.gif"));
		imagen.addKeyListener(this);
		imagen.setHorizontalAlignment(0);
		imagen.setVerticalAlignment(0);
		panelImagen.add(imagen);

				
		// Etiqueta de texto
		etiqueta=new JLabel(this.etiq);
		etiqueta.addKeyListener(this);
		etiqueta.setHorizontalAlignment(0);
		etiqueta.setVerticalAlignment(0);
		
		// Panel para etiqueta
		bl2= new BorderLayout();
		panelEtiqueta = new JPanel();
		panelEtiqueta.setLayout(bl2);
		panelEtiqueta.add(etiqueta,BorderLayout.CENTER);
		
		// Botón Aceptar
		aceptar=new BotonAceptar();
		aceptar.addKeyListener(this);
		aceptar.addMouseListener(this);
		
		// Panel de botón Aceptar
		panelBoton = new JPanel();
		panelBoton.add(aceptar);
		panelBoton.addKeyListener(this);
		panelBoton.transferFocus();

		// Panel de la derecha
		bl0= new BorderLayout();
		panelDerecha = new JPanel();
		panelDerecha.setLayout(bl0);
		panelDerecha.add(panelEtiqueta,BorderLayout.CENTER);
		panelDerecha.add(panelBoton,BorderLayout.SOUTH);
		panelDerecha.addKeyListener(this);

		// Panel general de CuadroError
		bl= new BorderLayout();
		panel = new JPanel();
		panel.setLayout(bl);
		panel.addKeyListener(this);

		panel.add(imagen,BorderLayout.WEST);
		panel.add(panelDerecha,BorderLayout.CENTER);
		
		if (Conf.fichero_log) 	Logger.log_write("ERROR ["+this.titulo+"]: "+this.etiq);
		
		d.getContentPane().add(panel);
		int coord[]=Conf.ubicarCentro(ancho,alto);
		d.setLocation(coord[0],coord[1]);
		d.setTitle(titulo);
		d.setSize(ancho,alto);
		d.setResizable(false);	
		d.setVisible(true);
		aceptar.transferFocus();
	}

	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/
	public void actionPerformed(ActionEvent e)
	{
		d.setVisible(false);
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyPressed(KeyEvent e)
	{
		int code=e.getKeyCode();
		//System.out.println("keyPressed");
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/	
	public void keyReleased(KeyEvent e)
	{
		int code=e.getKeyCode();
		//System.out.println("keyReleased");
		if (code==KeyEvent.VK_ESCAPE || code==KeyEvent.VK_ENTER)
			d.setVisible(false);
	}

	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/	
	public void keyTyped(KeyEvent e)
	{
		int code=e.getKeyCode();
		//System.out.println("keyTyped");
		if (code==KeyEvent.VK_ENTER)
			d.setVisible(false);
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
	}
	

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseExited(MouseEvent e) 
	{
	}
	

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mousePressed(MouseEvent e) 
	{
		if (e.getComponent().equals(aceptar))
			d.setVisible(false);
	}
	

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseReleased(MouseEvent e)
	{
	}
	

}