/**
	Cuadro de identificación del programa
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/
package grafica;



import java.awt.BorderLayout;

import java.awt.Font;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;



import javax.swing.JDialog;

import javax.swing.JLabel;
import javax.swing.JPanel;


import conf.*;
import utilidades.*;
import ventanas.*;

public class EtiquetaFlotante extends Thread implements MouseListener, MouseMotionListener //ActionListener, KeyListener
{
	static final long serialVersionUID=82;

	static final boolean depurar=false;

	static boolean hayEtiquetaActiva=false;

	int x;
	int y;
	int ancho;
	int alto;
	
	int ratonX=0, ratonY=0, ratonXAnt=0, ratonYAnt=0;
	
	String nombre;
	String entrada;
	String salida;
	
	JLabel etiqNombre, etiqEntrada, etiqSalida;
	static JDialog dialogo=null;

	/**
		Constructor: Genera un nuevo cuadro de identifiación del programa
		
		@param ventanaVisualizador ventana a la que quedará asociado el cuadro
	*/
	public EtiquetaFlotante(int x, int y, String nombre, String entrada, String salida, boolean entradaVisible, boolean salidaVisible)
	{	
		if (!hayEtiquetaActiva)
		{
			hayEtiquetaActiva=true;
			if (dialogo!=null)
				dialogo.setVisible(false);
			dialogo =new JDialog(Ventana.thisventana,false);
			
			this.x=x;
			this.y=y;
			this.nombre=" "+nombre;

			if (Conf.elementosVisualizar==Conf.VISUALIZAR_ENTRADA || Conf.elementosVisualizar==Conf.VISUALIZAR_TODO)
				if (entradaVisible)
					this.entrada=" "+Texto.get("ETIQFL_ENTR",Conf.idioma)+": "+entrada;
				else
					this.entrada=" "+Texto.get("ETIQFL_ENTR",Conf.idioma)+": --------";
			else
				this.entrada=" ";
			
			if (Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA || Conf.elementosVisualizar==Conf.VISUALIZAR_TODO)
				if (salidaVisible)
					this.salida=" "+Texto.get("ETIQFL_SALI",Conf.idioma)+": "+salida;
				else
					this.salida=" "+Texto.get("ETIQFL_SALI",Conf.idioma)+": --------";
			else
				this.salida=" ";
				
			this.start();
		}
	}
	
	/**
		Crea una etiqueta
	*/
	public synchronized void run()
	{
		// Etiquetas
		this.etiqNombre=new JLabel(this.nombre);
		
		this.etiqEntrada=new JLabel(this.entrada);
		this.etiqSalida=new JLabel(this.salida);
	
		
		// Panel que las contendrá
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		
		panel.add(etiqNombre,BorderLayout.NORTH);
		panel.add(etiqEntrada,BorderLayout.CENTER);
		panel.add(etiqSalida,BorderLayout.SOUTH);
		
		panel.addMouseListener (this);
		panel.addMouseMotionListener (this);
		
		etiqNombre.setFont(new Font(etiqNombre.getFont().getName(),Font.BOLD,
							etiqNombre.getFont().getSize()));
		
		dialogo.getContentPane().add(panel);
		dialogo.setUndecorated(true);
		
	

		ancho=Math.max(nombre.length(),Math.max(entrada.length(),salida.length())) *6;
		alto=40;
		dialogo.setSize(ancho,alto);
		
		int margenMinimo=12;

		// Ubicamos la etiqueta centrada sobre el ratón
		x=x-(ancho/2);
		y=y-(alto/2);
		
		// Comprobamos que no se salga por algún lateral
		if (x<margenMinimo)
			x=margenMinimo;
		
		if (x+ancho>Ventana.thisventana.tamPantalla[0])
		{
			x=x-( x+ancho+margenMinimo-Ventana.thisventana.tamPantalla[0] );
		}
		
		
		
		dialogo.setLocation(x,y);
		
		dialogo.setVisible(true);
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
		dialogo.setVisible(false);
		hayEtiquetaActiva=false;
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/				
	public void mousePressed(MouseEvent e) 
	{
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/				
	public void mouseReleased(MouseEvent e)
	{
		dialogo.setVisible(false);
	}
	
	/**
		Gestiona los eventos de movimiento de ratón
		
		@param e evento de ratón
	*/				
	public void mouseDragged(MouseEvent e) 
	{
	}
	
	/**
		Gestiona los eventos de movimiento de ratón
		
		@param e evento de ratón
	*/				
	public void mouseMoved(MouseEvent e) 
	{

		ratonXAnt=ratonX;
		ratonYAnt=ratonY;
		ratonX = (e.getXOnScreen());
		ratonY = (e.getYOnScreen());
		
		if (ratonX!=ratonXAnt || ratonY!=ratonYAnt)
			if ((ratonX<x || ratonX>x+ancho) || (ratonY<y || ratonY>y+alto))	// Si raton está fuera
			{
				dialogo.setVisible(false);
				hayEtiquetaActiva=false;
			}
	}
	
	public static void quitarEtiqueta()
	{
		if (dialogo!=null)
			dialogo.setVisible(false);
		hayEtiquetaActiva=false;
	}
	
}
