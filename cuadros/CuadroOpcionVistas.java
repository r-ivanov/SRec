/**
	CAMBIAR TODOS LOS COMENTARIOS DE ESTA CLASE
	******************************************************
		
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;





import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;


import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import conf.*;
import datos.MetodoAlgoritmo;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

public class CuadroOpcionVistas extends Thread implements ActionListener, KeyListener
{
	static final long serialVersionUID=07;



	final int anchoCuadro=300;
	final int altoCuadro=290;
	final int anchoCuadroNW=290;		// No Windows
	final int altoCuadroNW=404;			// No Windows

	OpcionVistas ov=null;
	GestorOpciones gOpciones=new GestorOpciones();


	JRadioButton selec1[];
	JRadioButton selec2[];
	ButtonGroup grupoSelec[];
	
	
	JComboBox selecPanel;

	
	JDialog dialogo;
	
	BotonAceptar aceptar=new BotonAceptar();
	BotonCancelar cancelar=new BotonCancelar();
	
	/**
		Constructor: genera un nuevo cuadro de opción
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroOpcionVistas ()
	{	
		dialogo=new JDialog(Ventana.thisventana,true);
		this.start();
	}

	/**
		Genera una nueva opción
	*/
	
	public void run()
	{
		ov=(OpcionVistas)gOpciones.getOpcion("OpcionVistas",false);
				
		// Panel para las vistas
		JPanel panelVistas = new JPanel();
		GridLayout gl1 = new GridLayout(6,1);
		panelVistas.setLayout(gl1);
		panelVistas.setBorder(new TitledBorder(Texto.get("CVI_VISTASPROP",Conf.idioma)));
		
		selec1=new JRadioButton[Vista.codigos.length];
		selec2=new JRadioButton[Vista.codigos.length];
		grupoSelec=new ButtonGroup[Vista.codigos.length];
		
		
		for (int i=0; i<Vista.codigos.length; i++)
		{
			JPanel panelVista=new JPanel();
			panelVista.setLayout(new BorderLayout());
			panelVista.setPreferredSize(new Dimension(anchoCuadro-10,23));
			
			JLabel etiq=new JLabel(Texto.get(Vista.codigos[i], Conf.idioma));
			
			selec1[i]=new JRadioButton("1");
			selec2[i]=new JRadioButton("2");
			selec1[i].addActionListener(this);
			selec2[i].addActionListener(this);
			grupoSelec[i]=new ButtonGroup();
			grupoSelec[i].add(selec1[i]);
			grupoSelec[i].add(selec2[i]);
			
			
			selec1[i].setFocusable(false);
			selec2[i].setFocusable(false);
			
			if ((ov.getVista(Vista.codigos[i]).getPanel())==1)
				selec1[i].setSelected(true);
			else
				selec2[i].setSelected(true);
			
			JPanel panelRadioB=new JPanel();
			panelRadioB.add(selec1[i]);
			panelRadioB.add(selec2[i]);
			
			panelVista.add(etiq,BorderLayout.WEST);
			panelVista.add(panelRadioB,BorderLayout.EAST);
			
			panelVistas.add(panelVista);
		}
		
		actualizarInterfazRadioButton();
		
		//if (VentanaVisualizador.thisventana.traza.getTecnica()!=MetodoAlgoritmo.TECNICA_DYV)
		if (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.traza.getTecnicas()))
		{
			//selec1[2].setEnabled(false);
			//selec2[2].setEnabled(false);
			
			selec1[3].setEnabled(false);
			selec2[3].setEnabled(false);
			
			//selec1[5].setEnabled(false);
			//selec2[5].setEnabled(false);
		}
		
		// Panel para la disposición de los paneles
		JPanel panelPaneles=new JPanel();
		panelPaneles.setLayout(new BorderLayout());
		panelPaneles.setBorder(new TitledBorder(Texto.get("CVI_DISPVISTAS",Conf.idioma)));
		
		JLabel etiq=new JLabel(Texto.get("CVI_PANELES",Conf.idioma));
		selecPanel=new JComboBox();
		selecPanel.addItem(Texto.get("VERTICAL",Conf.idioma));
		selecPanel.addItem(Texto.get("HORIZONTAL",Conf.idioma));
		selecPanel.setSelectedIndex(ov.getDisposicion()-1);
		selecPanel.addActionListener(this);
		selecPanel.setFocusable(false);
		
		panelPaneles.add(etiq,BorderLayout.WEST);
		panelPaneles.add(selecPanel,BorderLayout.EAST);
		
		// Panel que recoge los dos paneles anteriores
		JPanel panelContenedor=new JPanel();
		panelContenedor.setLayout(new BorderLayout());
		
		panelContenedor.add(panelVistas,BorderLayout.NORTH);
		panelContenedor.add(panelPaneles,BorderLayout.SOUTH);
		
		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(aceptar);
		panelBoton.add(cancelar);
		aceptar.addActionListener(this);
		aceptar.addKeyListener(this);
		cancelar.addActionListener(this);
		cancelar.addKeyListener(this);
	
		// Panel general
		BorderLayout bl= new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);
	
		panel.add(panelContenedor,BorderLayout.NORTH);
		panel.add(panelBoton,BorderLayout.SOUTH);
			
		// Preparamos y mostramos cuadro
		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("CPAN_DISPOS",Conf.idioma));

		if (Ventana.thisventana.msWindows)
		{
			dialogo.setSize(anchoCuadro,altoCuadro);
			int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
			dialogo.setLocation(coord[0],coord[1]);
		}
		else
		{
			dialogo.setSize(anchoCuadroNW,altoCuadroNW);
			int coord[]=Conf.ubicarCentro(anchoCuadroNW,altoCuadroNW);
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
		for (int i=0; i<Vista.codigos.length; i++)
		{
			Vista v=ov.getVista(Vista.codigos[i]);
			v.setPanel(selec1[i].isSelected() ? 1 : 2);
			ov.actualizarVista(v);
		}
	}
	
	

	
	private void actualizarInterfazRadioButton()
	{
		int numVistas=Vista.codigos.length;
		//if (VentanaVisualizador.thisventana.traza.getTecnica()!=MetodoAlgoritmo.TECNICA_DYV)
		if (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.traza.getTecnicas()))
			numVistas=3;	// en rec tenemos 3 vistas
		
		
		
		
		// Interfaz para asegurar que siempre habrá una vista como mínimo en cada panel
		for (int i=0; i<numVistas; i++)
		{
			selec1[i].setEnabled(true);
			selec2[i].setEnabled(true);
		}
		
		// Detectamos si ha quedado sólo una vista en el panel 1
		int contador=0;
		for (int i=0; i<numVistas; i++)
			if (selec1[i].isSelected())
				contador++;
		
		if (contador==1)
		{
			for (int i=0; i<numVistas; i++)
				if (selec1[i].isSelected())
				{
					selec1[i].setEnabled(false);
					selec2[i].setEnabled(false);
				}
		}
		
		// Detectamos si ha quedado sólo una vista en el panel 2			
		contador=0;
		for (int i=0; i<numVistas; i++)
			if (selec2[i].isSelected())
				contador++;
		
		if (contador==1)
		{
			for (int i=0; i<numVistas; i++)
				if (selec2[i].isSelected())
				{
					selec1[i].setEnabled(false);
					selec2[i].setEnabled(false);
				}
		}
		// Fin interfaz 
	}
	
	
	
	

	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/	
	public void actionPerformed(ActionEvent e)
	{
		getValores();


		if (e.getSource()==aceptar)
		{
			/*boolean[] panel1activos=new boolean[Vista.codigos.length];
			for (int i=0; i<Vista.codigos.length; i++)
			{
				panel1activos[i]=false;
			}
			
			VentanaVisualizador.thisventana.distribuirPaneles(panel1activos,(selecPanel.getSelectedIndex()==0 ? Conf.PANEL_VERTICAL: Conf.PANEL_HORIZONTAL));*/
			dialogo.setVisible(false);
		}
		if (e.getSource()==cancelar)
		{
			dialogo.setVisible(false);
		}
		else if (e.getSource() instanceof JRadioButton)
		{
			actualizarInterfazRadioButton();
			
			final boolean[] panel1activos=new boolean[Vista.codigos.length];
			for (int i=0; i<Vista.codigos.length; i++)
				panel1activos[i]=selec1[i].isSelected();
			
			new Thread(){
				public synchronized void run()
				{
					try {wait(300);} catch (java.lang.InterruptedException ie) {}
					Ventana.thisventana.distribuirPaneles(panel1activos,(selecPanel.getSelectedIndex()==0 ? Conf.PANEL_VERTICAL: Conf.PANEL_HORIZONTAL));
				}
			}.start();

			
			
			
			
			
		}
		else if (e.getSource() instanceof JComboBox)
		{
			if (selecPanel.getSelectedIndex()==0)
				ov.setDisposicion(Conf.PANEL_VERTICAL);
			else
				ov.setDisposicion(Conf.PANEL_HORIZONTAL);
			Ventana.thisventana.distribuirPaneles((selecPanel.getSelectedIndex()==0 ? Conf.PANEL_VERTICAL: Conf.PANEL_HORIZONTAL));
			
		}
		gOpciones.setOpcion(ov,2);
		Conf.setConfiguracionVistas();
			
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
		if (code==KeyEvent.VK_ESCAPE || code==KeyEvent.VK_ENTER)
			dialogo.setVisible(false);
		
		//System.out.println("keyReleased");
		/*if (e.getSource().getClass().getName().contains("JRadioButton"))
		{
			for (int i=0; i<botonesDatos.length; i++)
			{
				if (botonesDatos[i].isFocusOwner())
				{
					if (code==KeyEvent.VK_DOWN)
					{
						if (i<botonesDatos.length-1)
							botonesDatos[i].transferFocus();
						else
							botonesHistoria[0].requestFocus();
					}
					else if (code==KeyEvent.VK_UP)
					{
						if (i>0)
							botonesDatos[i].transferFocusBackward();
					}
				}
			}
			
			for (int i=0; i<botonesHistoria.length; i++)
			{
				if (botonesHistoria[i].isFocusOwner())
				{
					if (code==KeyEvent.VK_DOWN)
					{
						if (i<botonesHistoria.length-1)
							botonesHistoria[i].transferFocus();
						else
							botonesSalto[0].requestFocus();
					}
					else if (code==KeyEvent.VK_UP)
					{
						if (i>0)
							botonesHistoria[i].transferFocusBackward();
						else
							botonesDatos[botonesDatos.length-1].requestFocus();
					}
				}
			}
			
		
			for (int i=0; i<botonesSalto.length; i++)
			{
				if (botonesSalto[i].isFocusOwner())
				{
					if (code==KeyEvent.VK_DOWN)
					{
						if (i<botonesSalto.length-1)
							botonesSalto[i].transferFocus();
						else
							botonMostrarVisor.requestFocus();
					}
					else if (code==KeyEvent.VK_UP)
					{
						if (i>0)
							botonesSalto[i].transferFocusBackward();
						else
							botonesHistoria[botonesHistoria.length-1].requestFocus();
					}
				}
			}
			
			if (botonMostrarVisor.isFocusOwner() && code==KeyEvent.VK_UP)
			{
				botonesSalto[botonesSalto.length-1].requestFocus();
			}
			
			
		}*/
		
		
		
		
	}

	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyTyped(KeyEvent e)
	{
	}
	
	
}