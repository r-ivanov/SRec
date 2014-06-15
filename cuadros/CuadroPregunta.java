/**
	Representa el cuadro de error
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;




import java.awt.BorderLayout;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;




import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.ImageIcon;

import utilidades.Texto;

import conf.*;
import botones.*;


public abstract class CuadroPregunta extends Thread implements ActionListener, KeyListener
{
	static final long serialVersionUID=01;

	protected static int ancho = 650;
	protected static int alto  = 100;
	
	int numBotones=2;
	
	JLabel etiqueta, imagen;
	BotonAceptar aceptar;
	BotonCancelar cancelar;
	BotonTexto ignorar;
	
	JButton botonComando;
	JPanel panel, panelBoton, panelImagen, panelDerecha, panelEtiqueta;

	String titulo;
	String etiq;
	String comando;
	String boton2;

	BorderLayout bl, bl0, bl1, bl2;
	
	JDialog d;

	
	public CuadroPregunta()
	{

		//this.arranque();
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
	public CuadroPregunta(JFrame ventana,String titulo, String etiq, int numBotones, int anc, int alt)
	{
		d=new JDialog(ventana,true);
		this.titulo=titulo;
		this.etiq=etiq;
		ancho=anc;
		alto=alt;
		this.numBotones=numBotones;
		this.start();
		//this.arranque();
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
	public CuadroPregunta(JDialog dialogo,String titulo, String etiq, int numBotones, int anc, int alt)
	{
		d=new JDialog(dialogo,true);
		this.titulo=titulo;
		this.etiq=etiq;
		ancho=anc;
		alto=alt;
		this.numBotones=numBotones;
		this.start();
		//this.arranque();
	}
	
	
	
	public void run()
	{
		arranque();
		d.setVisible(true);
	}
	
	public void arranque()
	{
		arranque(this.numBotones);
		
	}
	
	public void arranque(int n)
	{
		// Etiqueta para icono
		panelImagen = new JPanel();
		imagen=new JLabel(new ImageIcon("imagenes/alarma.gif"));
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
		
		
		
		if (n==3)
		{
			// Botón Ignorar
			ignorar=new BotonTexto(Texto.get("IGNORAR",Conf.idioma),130);
			ignorar.addActionListener(this);
			ignorar.addKeyListener(this);
		}
		
		// Botón Aceptar
		aceptar=new BotonAceptar();
		aceptar.addActionListener(this);
		aceptar.addKeyListener(this);

		// Botón Cancelar
		cancelar=new BotonCancelar();
		cancelar.addActionListener(this);
		cancelar.addKeyListener(this);
		
		
		
		// Panel de botón Aceptar
		panelBoton = new JPanel();
		if (n==3)
			panelBoton.add(ignorar);
		panelBoton.add(aceptar);
		panelBoton.add(cancelar);
		panelBoton.addKeyListener(this);
		panelBoton.transferFocus();
		
		// Panel de la derecha
		bl0= new BorderLayout();
		panelDerecha = new JPanel();
		panelDerecha.setLayout(bl0);
		panelDerecha.add(panelEtiqueta,BorderLayout.CENTER);
		panelDerecha.add(panelBoton,BorderLayout.SOUTH);
		panelDerecha.addKeyListener(this);

		// Panel general de CuadroPregunta
		bl= new BorderLayout();
		panel = new JPanel();
		panel.setLayout(bl);
		panel.addKeyListener(this);

		panel.add(imagen,BorderLayout.WEST);
		panel.add(panelDerecha,BorderLayout.CENTER);
		
		d.getContentPane().add(panel);
		int coord[]=Conf.ubicarCentro(ancho,alto);
		d.setLocation(coord[0],coord[1]);
		d.setTitle(titulo);
		d.setSize(ancho,alto);
		d.setResizable(false);	
		
		aceptar.transferFocus();
	}

	public static int getAltoPorDefecto()
	{
		return alto;
	}
	
	public static int getAnchoPorDefecto()
	{
		return ancho;
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
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