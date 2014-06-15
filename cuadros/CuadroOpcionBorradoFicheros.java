/**
	Representa la clase del cuadro que maneja la opción que gestiona el mantenimiento de ficheros intermedios
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;





import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import conf.*;
import opciones.*;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

public class CuadroOpcionBorradoFicheros extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=02;
	private final boolean DEPURAR=false;

	final int anchoCuadro=400;
	final int altoCuadro=210; // 100+(22*5)



	JLabel[] etiquetas;
	BotonAceptar aceptar;
	BotonCancelar cancelar;
	JPanel panel, panelBoton, panelBotones;
	int numero;

	OpcionBorradoFicheros obf=null;
	GestorOpciones gOpciones=new GestorOpciones();

	ButtonGroup grupoBotones;
	JCheckBox[] botones;

	String [] valores;
	
	BorderLayout bl;
	GridLayout gl;
	
	JDialog dialogo;

	/**
		Constructor: genera un nuevo cuadro de opción
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroOpcionBorradoFicheros ()
	{	
		dialogo=new JDialog(Ventana.thisventana,true);
		this.start();
	}

	/**
		genera una nueva opción
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/	
	public void run()
	{
		obf=(OpcionBorradoFicheros)gOpciones.getOpcion("OpcionBorradoFicheros",true);
		
		// Creamos panel con cinco JCheckBoxes 
		panelBotones=new JPanel();
		panelBotones.addKeyListener(this);

		gl = new GridLayout(5,1);
		panelBotones.setLayout(gl);
		panelBotones.setBorder(new TitledBorder(Texto.get("COBF_SELECT",Conf.idioma)));

		botones = new JCheckBox[5];
		botones[0]= new JCheckBox(Texto.get("COBF_XMLORI",Conf.idioma),!obf.getfXml());
		botones[1]= new JCheckBox(Texto.get("COBF_XMLGEN",Conf.idioma),!obf.getfXmlzv());
		botones[2]= new JCheckBox(Texto.get("COBF_JAVAGEN",Conf.idioma),!obf.getfJavazv());
		botones[3]= new JCheckBox(Texto.get("COBF_COMPORI",Conf.idioma),!obf.getfClass());
		botones[4]= new JCheckBox(Texto.get("COBF_COMPGEN",Conf.idioma),!obf.getfClasszv());
		
		botones[0].setToolTipText(Texto.get("COBF_XMLORI_TTT",Conf.idioma));
		botones[1].setToolTipText(Texto.get("COBF_XMLGEN_TTT",Conf.idioma));
		botones[2].setToolTipText(Texto.get("COBF_JAVAGEN_TTT",Conf.idioma));
		botones[3].setToolTipText(Texto.get("COBF_COMPORI_TTT",Conf.idioma));
		botones[4].setToolTipText(Texto.get("COBF_COMPGEN_TTT",Conf.idioma));
		
		for (int i=0; i<botones.length; i++)
		{
			botones[i].addKeyListener(this);
			panelBotones.add(botones[i]);
		}
		
		// Botón Aceptar
		aceptar=new BotonAceptar();
		aceptar.addKeyListener(this);
		aceptar.addMouseListener(this);
		
		// Botón Cancelar
		cancelar=new BotonCancelar();
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
	
		panel.add(panelBotones,BorderLayout.NORTH);
		panel.add(panelBoton,BorderLayout.SOUTH);
			

		// Preparamos y mostramos cuadro
		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("COBF_TITULO",Conf.idioma));
		int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
		dialogo.setLocation(coord[0],coord[1]);
		dialogo.setSize(anchoCuadro,altoCuadro);
		dialogo.setResizable(false);

		dialogo.setVisible(true);
	}

	/**
		Almacena los valores del caudro en la opción y guarda la opción en fichero.
		Después hace invisible el cuadro
	*/		
	public void almacenarValoresYOcultar()
	{
		obf.setfXml(!botones[0].isSelected());
		obf.setfXmlzv(!botones[1].isSelected());
		obf.setfJavazv(!botones[2].isSelected());
		obf.setfClass(!botones[3].isSelected());
		obf.setfClasszv(!botones[4].isSelected());

		dialogo.setVisible(false);
		
		gOpciones.setOpcion(obf,2);
	}
	
	/**
		Gestioan los eventos de acción
		
		@param e evento de acción
	*/		
	public void actionPerformed(ActionEvent e)
	{
		
	}
	
	/**
		Gestioan los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyPressed(KeyEvent e)
	{
		int code=e.getKeyCode();
		if (code==KeyEvent.VK_ENTER)
			if (aceptar.isFocusOwner())
				almacenarValoresYOcultar();
			/*else
			{
				JCheckBox boton_accion;
				if (botones[0].isFocusOwner())
					boton_accion=botones[0];
				else if (botones[1].isFocusOwner())
					boton_accion=botones[1];
				else if (botones[2].isFocusOwner())
					boton_accion=botones[2];
				else if (botones[3].isFocusOwner())
					boton_accion=botones[3];
				else //if (botones[4].isFocusOwner())
					boton_accion=botones[4];

				boton_accion.setSelected(!boton_accion.isSelected());
			}*/
	}
	
	/**
		Gestioan los eventos de teclado
		
		@param e evento de teclado
	*/				
	public void keyReleased(KeyEvent e)
	{
		int code=e.getKeyCode();
		if (code==KeyEvent.VK_ENTER)
			almacenarValoresYOcultar();
		if (code==KeyEvent.VK_ESCAPE)
			dialogo.setVisible(false);
	}

	/**
		Gestioan los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyTyped(KeyEvent e)
	{
	}
	
	/**
		Gestioan los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseClicked(MouseEvent e) 
	{
	}
	
	/**
		Gestioan los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseEntered(MouseEvent e) 
	{
	}
	
	/**
		Gestioan los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseExited(MouseEvent e) 
	{
	}
	
	/**
		Gestioan los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mousePressed(MouseEvent e) 
	{
		if ( ((e.getSource()))==aceptar)
			almacenarValoresYOcultar();
		else if ( ((e.getSource()))==cancelar)
			this.dialogo.setVisible(false);
	}
	
	/**
		Gestioan los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseReleased(MouseEvent e)
	{
	}

}
