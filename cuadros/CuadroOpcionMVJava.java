/**
	Implementa el cuadro que permite configurar la opción de Máquina virtual de java
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;




import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.TitledBorder;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import conf.*;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;



public class CuadroOpcionMVJava extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=02;

	static final boolean depurar=false;

	final int anchoCuadro=448;
	final int altoCuadro=124;

	JLabel etiqueta;
	JTextField campoDireccion;
	Boton examinar;
	BotonAceptar aceptar;
	BotonCancelar cancelar;
	JPanel panel, panelBoton, panelpanelElementos, panelElementos;
	int numero;

	BorderLayout bl;
	GridLayout gl;
	
	JDialog dialogo;
	
	GestorOpciones gOpciones=new GestorOpciones();
	OpcionMVJava omvj = null;

	String fichero[]=new String[2];
	
	String textoIndicativo = "";

	
	/**
		Constructor: crea una nueva instancia de la opción
		
		@param ventanaVisualizador Ventana a la que estará asociado el cuadro de diálogo
		@param inicioAplicacion true si se ha de cargar texto más explicativo para la primera ejecución de la aplicación
	*/
	public CuadroOpcionMVJava(boolean inicioAplicacion)
	{	
		// Llamamos a JDialog estableciendo algunos parámetros
		dialogo = new JDialog(Ventana.thisventana,true);
		if(inicioAplicacion)
			textoIndicativo = Texto.get("COMVJ_ESCRDIRVALTTT3",Conf.idioma);
		else
			textoIndicativo = Texto.get("COMVJ_ESCRDIRVALTTT2",Conf.idioma);
		this.start();
	}

	/**
		Crea una nueva instancia de la opción
	*/	
	public void run()
	{
		omvj=(OpcionMVJava)gOpciones.getOpcion("OpcionMVJava",true);
		
		fichero[0]="C:\\";

		int anchoCampo=35;
		if (!Ventana.thisventana.msWindows)
			anchoCampo=23;
			
		
		// Elementos
		if (omvj.getDir()==null || omvj.getDir().equals(""))
			campoDireccion = new JTextField(Texto.get("COMVJ_ESCRDIR",Conf.idioma),anchoCampo);
		else
			campoDireccion = new JTextField(omvj.getDir(),anchoCampo);
		
		campoDireccion.addKeyListener(this);
		campoDireccion.setToolTipText(Texto.get("COMVJ_ESCRDIRVAL",Conf.idioma));
		
		examinar = new Boton(Texto.get("BOTONEXAMINAR",Conf.idioma));
		//examinar.addActionListener(this);
		examinar.addKeyListener(this);
		examinar.addMouseListener(this);
		examinar.setToolTipText(Texto.get("COMVJ_ESCRDIRVALTTT",Conf.idioma));

		// Panel de elementos
		panelElementos = new JPanel();
		panelElementos.add(campoDireccion);
		panelElementos.add(examinar);
		panelElementos.setSize(120,45);

		// Panel de panel de Elementos
		gl = new GridLayout(1,1);
		panelpanelElementos = new JPanel();
		panelpanelElementos.setLayout(gl);
		panelpanelElementos.setBorder(new TitledBorder(textoIndicativo));
		panelpanelElementos.add(panelElementos);

		// Botón Aceptar
		aceptar=new BotonAceptar();//aceptar=new JButton ("Aceptar");
		//aceptar.addActionListener(this);
		aceptar.addKeyListener(this);
		aceptar.addMouseListener(this);
		if (!valorarSeleccion(false))
			aceptar.setRojo();
		
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
	
		panel.add(panelpanelElementos,BorderLayout.NORTH);
		panel.add(panelBoton,BorderLayout.SOUTH);
			
		dialogo.getContentPane().add(panel);
	
		//panelElementos.setBackground(Conf.colorCuadroDialogo);
		//panelpanelElementos.setBackground(Conf.colorCuadroDialogo);
		//panelBoton.setBackground(Conf.colorCuadroDialogo);
		//dialogo.setBackground(Conf.colorCuadroDialogo);
	
		// Preparamos y mostramos cuadro
		int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
		dialogo.setLocation(coord[0],coord[1]);
		dialogo.setSize(anchoCuadro,altoCuadro);
		dialogo.setResizable(false);
		dialogo.setTitle(Texto.get("COMVJ_SELECTITULO",Conf.idioma));
		dialogo.setVisible(true);
		dialogo.setAlwaysOnTop(true);
	}
	
	/**
		Comprueba que la dirección seleccionada es correcta
		
		@param aceptarPulsado true si el usuario pulsó el botón Aceptar
				false si el método es ejecutado por la propia aplicación de manera interna
		@return true si la dirección es válida
	*/
	private boolean valorarSeleccion(boolean aceptarPulsado)
	{
		omvj.setDir(campoDireccion.getText());
		if (omvj.getValida())
		{
			aceptar.setVerde();
			if (aceptarPulsado)
			{
				gOpciones.setOpcion(omvj,2);
				dialogo.setVisible(false);
				new CuadroInformacion(Ventana.thisventana,Texto.get("INFO_MVJOK",Conf.idioma),Texto.get("INFO_MVJAVA",Conf.idioma),550,100);
			}
			return true;	// Es válida la dirección de la MV
		}
		else
		{
			aceptar.setRojo();
			if(aceptarPulsado)
				new CuadroError(dialogo,Texto.get("ERROR_MVJAVA",Conf.idioma),Texto.get("ERROR_NOMVJ",Conf.idioma));
			return false; 		// No es válida la dirección de la MV
		}
	}
	
	
	
	public JDialog getDialogo()
	{
		return this.dialogo;
	}
	
	
	
	
	
	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/	
	public void actionPerformed(ActionEvent e)
	{
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/		
	public void keyPressed(KeyEvent e)
	{
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyReleased(KeyEvent e)
	{
		int code=e.getKeyCode();
		if (code==KeyEvent.VK_ESCAPE)
			dialogo.setVisible(false);
		else if (code==KeyEvent.VK_ENTER)
		{
			if (aceptar.isFocusOwner() || campoDireccion.isFocusOwner())
				valorarSeleccion(true);
			else if (examinar.isFocusOwner())
				fichero = SelecDireccion.cuadroAbrirFichero(fichero[0],Texto.get("COMVJ_SELECTITULO",Conf.idioma),
							"java.exe","exe",Texto.get("ARCHIVO_EXE",Conf.idioma),1);
				// *1* Comprobar que fichero existe
		}
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
			valorarSeleccion(true);

		else if ( e.getSource()==cancelar)
			this.dialogo.setVisible(false);
			
		else if ( e.getSource()==examinar)
		{
			fichero = SelecDireccion.cuadroAbrirFichero(fichero[0],Texto.get("COMVJ_SELECTITULO",Conf.idioma),
						"java.exe","exe",Texto.get("ARCHIVO_EXE",Conf.idioma),1);
			// *1* Comprobar que fichero existe
			if (fichero[1]!=null)
				campoDireccion.setText(fichero[0]+fichero[1]);
			valorarSeleccion(false);
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
