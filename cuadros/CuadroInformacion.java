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

import conf.*;
import botones.*;


public class CuadroInformacion extends Thread implements ActionListener, KeyListener
{
	static final long serialVersionUID=01;

	private static int ancho = 550;
	private static int alto  = 100;
	
	JLabel etiqueta, imagen;
	BotonAceptar aceptar;
	//BotonCancelar cancelar;
	JButton botonComando;
	JPanel panel, panelBoton, panelImagen, panelDerecha, panelEtiqueta;

	String titulo;
	String etiq;

	String boton2;

	BorderLayout bl, bl0, bl1, bl2;
	
	JDialog dialogo;

	/**
		Constructor: crea una nueva instancia del cuadro de error. DESECHADO
		
		@param ventana ventana a la que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/	
	public CuadroInformacion(JFrame ventana,String titulo, String etiq)
	{
		dialogo=new JDialog(ventana,true);
		this.titulo=titulo;
		this.etiq=etiq;
		this.start();
	}

	/**
		Constructor: crea una nueva instancia del cuadro de error. DESECHADO
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/		
	public CuadroInformacion(JDialog dialogo,String titulo, String etiq)
	{
		dialogo=new JDialog(dialogo,true);
		this.titulo=titulo;
		this.etiq=etiq;
		this.start();
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
	public CuadroInformacion(JFrame ventana,String titulo, String etiq, int anc, int alt)
	{
		dialogo=new JDialog(ventana,true);
		this.titulo=titulo;
		this.etiq=etiq;
		ancho=anc;
		alto=alt;
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
	public CuadroInformacion(JDialog dialogo,String titulo, String etiq, int anc, int alt)
	{
		dialogo=new JDialog(dialogo,true);
		this.titulo=titulo;
		this.etiq=etiq;

		ancho=anc;
		alto=alt;
		this.start();
	}
	
	/**
		crea una nueva instancia del cuadro de error. DESECHADO
	*/
	public void run()
	{


		// Etiqueta para icono
		panelImagen = new JPanel();
		imagen=new JLabel(new ImageIcon("imagenes/info.gif"));
		imagen.addKeyListener(this);
		imagen.setHorizontalAlignment(0);
		imagen.setVerticalAlignment(0);
		//imagen.setBackground(Conf.colorCuadroDialogo);
		panelImagen.add(imagen);
		//panelImagen.setBackground(Conf.colorCuadroDialogo);
		//panelImagen.setSize(new Dimension(80,80));
				
		// Etiqueta de texto
		etiqueta=new JLabel(this.etiq);
		etiqueta.addKeyListener(this);
		etiqueta.setHorizontalAlignment(0);
		etiqueta.setVerticalAlignment(0);
		//etiqueta.setFont(Conf.fuenteCuadro);
		//etiqueta.setBackground(Conf.colorCuadroDialogo);
		
		// Panel para etiqueta
		bl2= new BorderLayout();
		panelEtiqueta = new JPanel();
		panelEtiqueta.setLayout(bl2);
		panelEtiqueta.add(etiqueta,BorderLayout.CENTER);
		//panelEtiqueta.setBackground(Conf.colorCuadroDialogo);
		
		// Botón Aceptar
		aceptar=new BotonAceptar();
		aceptar.addActionListener(this);
		aceptar.addKeyListener(this);

		// Botón Cancelar
		/*cancelar=new BotonCancelar();
		cancelar.addActionListener(this);
		cancelar.addKeyListener(this);*/
		
		// Panel de botón Aceptar
		panelBoton = new JPanel();
		panelBoton.add(aceptar);
		//panelBoton.add(cancelar);
		panelBoton.addKeyListener(this);
		panelBoton.transferFocus();
		
		// Panel de la derecha
		bl0= new BorderLayout();
		panelDerecha = new JPanel();
		panelDerecha.setLayout(bl0);
		panelDerecha.add(panelEtiqueta,BorderLayout.CENTER);
		panelDerecha.add(panelBoton,BorderLayout.SOUTH);
		panelDerecha.addKeyListener(this);

		// Panel general de CuadroInformacion
		bl= new BorderLayout();
		panel = new JPanel();
		panel.setLayout(bl);
		panel.addKeyListener(this);

		panel.add(imagen,BorderLayout.WEST);
		panel.add(panelDerecha,BorderLayout.CENTER);
		
		dialogo.getContentPane().add(panel);
		int coord[]=Conf.ubicarCentro(ancho,alto);
		dialogo.setLocation(coord[0],coord[1]);
		dialogo.setTitle(titulo);
		dialogo.setSize(ancho,alto);
		dialogo.setResizable(false);	
		dialogo.setVisible(true);
		//aceptar.transferFocus();
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
		dialogo.setVisible(false);
	}
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyPressed(KeyEvent e)
	{
	}
	
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode()==KeyEvent.VK_ENTER || e.getKeyCode()==KeyEvent.VK_ESCAPE)
			dialogo.setVisible(false);
	}
	

}