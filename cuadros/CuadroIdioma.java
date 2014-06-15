/**
	CAMBIAR TODOS LOS COMENTARIOS DE ESTA CLASE
	******************************************************
		
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;





import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import conf.*;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

public class CuadroIdioma extends Thread implements ActionListener, KeyListener
{
	static final long serialVersionUID=07;
	private final boolean DEPURAR=false;


	final int anchoCuadro=275;
	final int altoCuadro=262;
	final int anchoCuadroNW=290;		// No Windows
	final int altoCuadroNW=274;			// No Windows

	OpcionIdioma oi=null;
	GestorOpciones gOpciones=new GestorOpciones();

	ButtonGroup grupoBotonesIdiomas;
	JRadioButton botonesIdiomas[];

	JDialog dialogo;
	
	BotonAceptar aceptar=new BotonAceptar();
	BotonCancelar cancelar=new BotonCancelar();
	

	String idiomas[][];
	
	/**
		Constructor: genera un nuevo cuadro de opción
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroIdioma ()
	{	
		dialogo=new JDialog(Ventana.thisventana,true);
		this.start();
	}

	/**
		Genera una nueva opción
	*/
	
	public void run()
	{
		oi=(OpcionIdioma)gOpciones.getOpcion("OpcionIdioma",true);
		
		idiomas=Texto.idiomasDisponibles();
		
		String idiomaActual[]=new String[2];
		
		for (int i=0; i<idiomas.length; i++)
			if (idiomas[i][1].equals(oi.get()))
			{
				idiomaActual[0]=idiomas[i][0];
				idiomaActual[1]=idiomas[i][1];
			}
		
		// Etiqueta idioma actual
		JLabel etiqIdiomaActual=new JLabel(Texto.get("CI_ACTUAL",Conf.idioma)+": "+idiomaActual[0]);
		
		// Panel para botones de selección
		JPanel panelBotones = new JPanel();
		GridLayout gl1 = new GridLayout(idiomas.length,1);
		panelBotones.setLayout(gl1);
		
		botonesIdiomas = new JRadioButton[idiomas.length];
		for (int i=0; i<idiomas.length; i++)
		{
			botonesIdiomas[i]=new JRadioButton(idiomas[i][0]);
			botonesIdiomas[i].setToolTipText(Texto.get("CI_ACTUAL",Conf.idioma)+" "+idiomas[i][0]);
			if (idiomas[i][0].equals(idiomaActual[0]))
				botonesIdiomas[i].setSelected(true);
		}
		
		grupoBotonesIdiomas=new ButtonGroup();
		
		for (int i=0; i<botonesIdiomas.length; i++)
		{
			grupoBotonesIdiomas.add(botonesIdiomas[i]);
			botonesIdiomas[i].addActionListener(this);
			botonesIdiomas[i].addKeyListener(this);
			panelBotones.add(botonesIdiomas[i]);
		}

		
		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(aceptar);
		panelBoton.add(cancelar);
		aceptar.addActionListener(this);
		aceptar.addKeyListener(this);
		cancelar.addActionListener(this);
		cancelar.addKeyListener(this);
		
		// Panel elementos
		JPanel panelElementos=new JPanel();
		panelElementos.setLayout(new BorderLayout());
		
		panelElementos.add(etiqIdiomaActual,BorderLayout.NORTH);
		panelElementos.add(panelBotones,BorderLayout.CENTER);		
		panelElementos.setBorder(new TitledBorder(Texto.get("CI_SELEC",Conf.idioma)));
	
		// Panel general
		BorderLayout bl= new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);
		
		
		panel.add(panelElementos,BorderLayout.NORTH);
		panel.add(panelBoton,BorderLayout.SOUTH);
			
		// Preparamos y mostramos cuadro
		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("CI_TITULO",Conf.idioma));

		if (Ventana.thisventana.msWindows)
		{
			dialogo.setSize(anchoCuadro,120+(idiomas.length*22));
			int coord[]=Conf.ubicarCentro(anchoCuadro,120+(idiomas.length*22));
			dialogo.setLocation(coord[0],coord[1]);
		}
		else
		{
			dialogo.setSize(anchoCuadro,130+(idiomas.length*24));
			int coord[]=Conf.ubicarCentro(anchoCuadro,130+(idiomas.length*24));
			dialogo.setLocation(coord[0],coord[1]);
		}
		dialogo.setResizable(false);
		dialogo.setVisible(true);
	}

	
	/**
		Comprueba que la cadena s contenga un valor válido (0..255)
		
		@param s cadena que será comprobada
		@return true si la cadena contiene un valor válido para una componente RGB
	*/	
	private void getValores()
	{
		for (int i=0; i<botonesIdiomas.length; i++)
			if (botonesIdiomas[i].isSelected())
			{
				for (int j=0; j<idiomas.length; j++)
					if (botonesIdiomas[j].getText().equals(idiomas[i][0]))
					{
						oi.set(idiomas[i][1]);
						Conf.idioma=idiomas[i][1];
						if (Conf.fichero_log) Logger.log_write("Idioma cambiado: "+idiomas[i][1]);
					}
			}
	}
	
	

	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/	
	public void actionPerformed(ActionEvent e)
	{
	
		if (e.getSource() instanceof JRadioButton)
		{

			
		}
		else if (e.getSource() instanceof JButton)
		{
			if (e.getSource()==aceptar)
			{
				getValores();
				gOpciones.setOpcion(oi,2);
				dialogo.setVisible(false);
				Ventana.thisventana.reiniciarIdioma();

			}
			else
				dialogo.setVisible(false);
		}
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
			getValores();
			gOpciones.setOpcion(oi,2);
			dialogo.setVisible(false);
			Ventana.thisventana.reiniciarIdioma();
		}
			
	}

	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyTyped(KeyEvent e)
	{

	}
	

}
