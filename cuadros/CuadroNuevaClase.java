/**Implementa el cuadro que permite configurar la opción de Máquina virtual de java
 * 
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;




import java.awt.BorderLayout;

import java.awt.Dimension;

import java.awt.GridLayout;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.border.TitledBorder;

import javax.swing.JDialog;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



import conf.*;
import datos.Preprocesador;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;



public class CuadroNuevaClase extends Thread implements KeyListener, MouseListener
{
	static final long serialVersionUID=02;

	static final boolean depurar=false;

	final int anchoCuadro=650;
	final int altoCuadro=138;

	
	
	
	JLabel etiqueta;

	BotonTexto examinar;
	BotonAceptar aceptar;
	BotonCancelar cancelar;
	JPanel panel, panelBoton, panelpanelElementos, panelElementos;
	int numero;

	BorderLayout bl;
	GridLayout gl;
	
	JDialog dialogo;
	
	GestorOpciones gOpciones=new GestorOpciones();
	OpcionFicherosRecientes ofr = null;

	String fichero[]=new String[2];

	JTextField campoDirectorio;
	
	
	/**
		Constructor: crea una nueva instancia de la opción
		
		@param ventanaVisualizador Ventana a la que estará asociado el cuadro de diálogo
	*/
	public CuadroNuevaClase()
	{	
		// Llamamos a JDialog estableciendo algunos parámetros
		dialogo = new JDialog(Ventana.thisventana,true);
		this.start();
	}

	/**
		Crea una nueva instancia de la opción
	*/	
	public void run()
	{
		ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);
		
		fichero[0]=ofr.getDir();
		fichero[1]=null;
		
		campoDirectorio=new JTextField();
		campoDirectorio.setEditable(false);
		
		// Panel nombre nueva clase
		JPanel panelClase=new JPanel();
		panelClase.setLayout(new BorderLayout());
		panelClase.add(new JLabel("Clase: "),BorderLayout.WEST);
		panelClase.add(campoDirectorio,BorderLayout.CENTER);
		
		// Panel nombre nueva clase
		JPanel panelDirectorio=new JPanel();
		panelDirectorio.setLayout(new BorderLayout());
		examinar=new BotonTexto(Texto.get("BOTONEXAMINAR",Conf.idioma));
		examinar.addMouseListener(this);
		examinar.addKeyListener(this);
		panelDirectorio.add(examinar,BorderLayout.EAST);
		
		// Panel vacío, para hacer hueco
		JPanel panelHueco=new JPanel();
		panelHueco.setPreferredSize(new Dimension(5,5));
		
		// Panel para recoger los dos paneles anteriores
		JPanel panelDatos=new JPanel();
		panelDatos.setLayout(new BorderLayout());
		panelDatos.add(panelClase,BorderLayout.NORTH);
		panelDatos.add(panelHueco,BorderLayout.CENTER);
		panelDatos.add(panelDirectorio,BorderLayout.SOUTH);
		panelDatos.setBorder(new TitledBorder(Texto.get("CCCN_PARACREARCLASE",Conf.idioma)));
		
		
		// Botón Aceptar
		aceptar=new BotonAceptar();//aceptar=new JButton ("Aceptar");
		//aceptar.addActionListener(this);
		aceptar.addKeyListener(this);
		aceptar.addMouseListener(this);
		
		
		// Botón Cancelar
		cancelar=new BotonCancelar();
		//cancelar.addActionListener(this);
		cancelar.addKeyListener(this);
		cancelar.addMouseListener(this);
		
		// Panel para el botón
		panelBoton = new JPanel();
		panelBoton.add(aceptar);
		panelBoton.add(cancelar);
		

		// Panel general
		bl= new BorderLayout();
		panel = new JPanel();
		panel.setLayout(bl);
	
		panel.add(panelDatos,BorderLayout.NORTH);
		panel.add(panelBoton,BorderLayout.SOUTH);
			
		dialogo.getContentPane().add(panel);
		
		// Preparamos y mostramos cuadro
		int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
		dialogo.setLocation(coord[0],coord[1]);
		dialogo.setSize(anchoCuadro,altoCuadro);
		dialogo.setResizable(false);
		dialogo.setTitle(Texto.get("CCCN_CREARCLASE",Conf.idioma));
		dialogo.setVisible(true);
		dialogo.setAlwaysOnTop(true);
	}
	
	/**
		Hace lo necsario una vez que se sabe que la dirección para el fichero es correcta
	*/
	public void archivoCorrecto()
	{
		String path=campoDirectorio.getText();
		
		GeneradorJava.crearClase(path);
		this.dialogo.setVisible(false);
		
		String carpeta=path.substring(0,path.lastIndexOf(File.separator)+1);
		String nombre=path.substring(path.lastIndexOf(File.separator)+1);
		
		//System.out.println("archivoCorrecto\ncarpeta="+carpeta+"nombre="+nombre);
		
		if (Conf.fichero_log) Logger.log_write("CuadroNuevaClase: nueva clase '"+nombre+"' creada en '"+carpeta+"'");
		new Preprocesador(carpeta,nombre);
		
		ofr.setDir(fichero[0]);
		gOpciones.setOpcion(ofr,2);
	}
	
	
	
	public JDialog getDialogo()
	{
		return this.dialogo;
	}
	
	
	

	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/		
	public void keyPressed(KeyEvent e)
	{
		int code=e.getKeyCode();
		if (code==KeyEvent.VK_ESCAPE)
			dialogo.setVisible(false);
		else if (code==KeyEvent.VK_ENTER)
		{
			File f=new File(fichero[0]+fichero[1]);
			if (f.exists())
				new CuadroPreguntaSobreescribir (Ventana.thisventana,this);
			else	
				archivoCorrecto();
			/*else if (examinar.isFocusOwner())
			{
				fichero = SelecDireccion.cuadroAbrirFichero(fichero[0],Texto.get("COMVJ_SELECTITULO",Conf.idioma),
						null,"java",Texto.get("ARCHIVO_JAVA",Conf.idioma),0,false);
				if (fichero[1]!=null)
				{
					if (!fichero[1].toLowerCase().endsWith(".java"))
						fichero[1]=fichero[1]+".java";
					campoDirectorio.setText(fichero[0]+fichero[1]);
					
					File f=new File(fichero[0]+fichero[1]);
					if (f.exists())
						new CuadroPreguntaSobreescribir (VentanaVisualizador.thisventana,this);
				}
			}*/
		}
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyReleased(KeyEvent e)
	{
		
	}

	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyTyped(KeyEvent e)
	{
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
		if ( e.getSource()==aceptar)
		{
			File f=new File(fichero[0]+fichero[1]);
			if (f.exists())
				new CuadroPreguntaSobreescribir (Ventana.thisventana,this);
			else	
				archivoCorrecto();
			
		}

		else if ( e.getSource()==cancelar)
			this.dialogo.setVisible(false);
			
		else if ( e.getSource()==examinar)
		{
			String []f = SelecDireccion.cuadroAbrirFichero(fichero[0],Texto.get("CA_GNUEVACLASE",Conf.idioma),
					null,"java",Texto.get("ARCHIVO_JAVA",Conf.idioma),0,false);
			
			if (f[1]!=null)
			{
				fichero[0]=f[0];
				fichero[1]=f[1];
				
				if (!fichero[1].toLowerCase().endsWith(".java"))
					fichero[1]=fichero[1]+".java";
				campoDirectorio.setText(fichero[0]+fichero[1]);
				
			}
			
		}
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseReleased(MouseEvent e)
	{
	}

}
