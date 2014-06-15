/**
	CAMBIAR TODOS LOS COMENTARIOS DE ESTA CLASE
	******************************************************
		
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;





import java.awt.BorderLayout;
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
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import conf.*;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

public class CuadroOpcionOpsVisualizacion extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=07;

	//final int anchoCuadro=275;
	//final int altoCuadro=392;

	final int anchoCuadro=550;
	final int altoCuadro=315;
	final int anchoCuadroNW=290;		// No Windows
	final int altoCuadroNW=404;			// No Windows

	OpcionOpsVisualizacion oov=null;
	GestorOpciones gOpciones=new GestorOpciones();

	ButtonGroup grupoBotonesDatos;
	ButtonGroup grupoBotonesHistoria;
	ButtonGroup grupoBotonesSalto;
	
	JRadioButton botonesDatos[];
	JRadioButton botonesHistoria[];
	JRadioButton botonesSalto[];
	
	JCheckBox botonMostrarVisor;
	
	JCheckBox botonMostrarEstructuraArbol;
	
	JCheckBox botonCronoEstrCompleta;
	JCheckBox botonCronoSalLigadaEnt;
	
	JDialog dialogo;
	
	BotonAceptar aceptar=new BotonAceptar();
	
	
	/**
		Constructor: genera un nuevo cuadro de opción
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroOpcionOpsVisualizacion ()
	{	
		dialogo=new JDialog(Ventana.thisventana,true);
		this.start();
	}

	/**
		Genera una nueva opción
	*/
	public void run()
	{
		oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				
		
		
		// Panel Datos que se muestran
		JPanel panelDatos = new JPanel();
		GridLayout gl1 = new GridLayout(3,1);
		panelDatos.setLayout(gl1);
		
		botonesDatos = new JRadioButton[3];
		botonesDatos[0]= new JRadioButton(Texto.get("COOV_ENTRADA",Conf.idioma));
		botonesDatos[1]= new JRadioButton(Texto.get("COOV_SALIDA",Conf.idioma));
		botonesDatos[2]= new JRadioButton(Texto.get("COOV_ENTRSAL",Conf.idioma));
		
		botonesDatos[0].setToolTipText(Texto.get("COOV_ENTRADATTT",Conf.idioma));
		botonesDatos[1].setToolTipText(Texto.get("COOV_SALIDATTT",Conf.idioma));
		botonesDatos[2].setToolTipText(Texto.get("COOV_ENTRSALTTT",Conf.idioma));
		
		grupoBotonesDatos=new ButtonGroup();
		
		for (int i=0; i<botonesDatos.length; i++)
		{
			grupoBotonesDatos.add(botonesDatos[i]);
			botonesDatos[i].addActionListener(this);
			botonesDatos[i].addKeyListener(this);
			panelDatos.add(botonesDatos[i]);
		}
	
		panelDatos.setBorder(new TitledBorder(Texto.get("COOV_INFONODO",Conf.idioma)));
		//panelDatos.setPreferredSize(new Dimension(490,70));
		
		// Panel Historia
		JPanel panelHistoria = new JPanel();
		GridLayout gl2 = new GridLayout(3,1);
		panelHistoria.setLayout(gl2);
		
		botonesHistoria = new JRadioButton[3];
		botonesHistoria[0]= new JRadioButton(Texto.get("COOV_INFONODOMANTHIST",Conf.idioma));
		botonesHistoria[1]= new JRadioButton(Texto.get("COOV_INFONODOATENHIST",Conf.idioma));
		botonesHistoria[2]= new JRadioButton(Texto.get("COOV_INFONODOELIMHIST",Conf.idioma));
		
		botonesHistoria[0].setToolTipText(Texto.get("COOV_INFONODOMANTHISTTTT",Conf.idioma));
		botonesHistoria[1].setToolTipText(Texto.get("COOV_INFONODOATENHISTTTT",Conf.idioma));
		botonesHistoria[2].setToolTipText(Texto.get("COOV_INFONODOELIMHISTTTT",Conf.idioma));
		
		grupoBotonesHistoria=new ButtonGroup();

		for (int i=0; i<botonesHistoria.length; i++)
		{
			grupoBotonesHistoria.add(botonesHistoria[i]);
			botonesHistoria[i].addActionListener(this);
			botonesHistoria[i].addKeyListener(this);
			panelHistoria.add(botonesHistoria[i]);
		}

		panelHistoria.setBorder(new TitledBorder(Texto.get("COOV_INFOHIST",Conf.idioma)));

		// Panel Mostrar Estructura en Arbol
		JPanel panelMostrarEstructura = new JPanel();
		GridLayout gl6 = new GridLayout(1,1);
		panelMostrarEstructura.setLayout(gl6);
		
		botonMostrarEstructuraArbol=new JCheckBox(Texto.get("COOV_EVATCH",Conf.idioma));
		botonMostrarEstructuraArbol.addActionListener(this);
		botonMostrarEstructuraArbol.addKeyListener(this);
		panelMostrarEstructura.add(botonMostrarEstructuraArbol);
		panelMostrarEstructura.setBorder(new TitledBorder(Texto.get("COOV_EVAT",Conf.idioma)));
		
		
		// Panel Salto
		JPanel panelSalto = new JPanel();
		GridLayout gl3 = new GridLayout(2,1);
		panelSalto.setLayout(gl3);
		
		botonesSalto = new JRadioButton[2];
		botonesSalto[0]= new JRadioButton(Texto.get("COOV_MOSTARBSALT",Conf.idioma));
		botonesSalto[1]= new JRadioButton(Texto.get("COOV_NMOSTARBSALT",Conf.idioma));
		
		botonesSalto[0].setToolTipText(Texto.get("COOV_MOSTARBSALTTTT",Conf.idioma));
		botonesSalto[1].setToolTipText(Texto.get("COOV_NMOSTARBSALTTTT",Conf.idioma));
		
		grupoBotonesSalto=new ButtonGroup();

		for (int i=0; i<botonesSalto.length; i++)
		{
			grupoBotonesSalto.add(botonesSalto[i]);
			botonesSalto[i].addActionListener(this);
			botonesSalto[i].addKeyListener(this);
			panelSalto.add(botonesSalto[i]);
		}

		panelSalto.setBorder(new TitledBorder(Texto.get("COOV_ACCSALTREC",Conf.idioma)));		

		// Panel Mostrar Visor
		JPanel panelMostrarVisor = new JPanel();
		GridLayout gl4 = new GridLayout(1,1);
		panelMostrarVisor.setLayout(gl4);
		
		botonMostrarVisor=new JCheckBox(Texto.get("COOV_VNVAMV",Conf.idioma));
		botonMostrarVisor.addActionListener(this);
		botonMostrarVisor.addKeyListener(this);
		panelMostrarVisor.add(botonMostrarVisor);
		panelMostrarVisor.setBorder(new TitledBorder(Texto.get("COOV_VNVA",Conf.idioma)));
		

		// Panel Vista Cronológica
		JPanel panelVistaCrono = new JPanel();
		GridLayout gl5 = new GridLayout(2,1);
		panelVistaCrono.setLayout(gl5);
		
		botonCronoEstrCompleta=new JCheckBox(Texto.get("COOV_VCRONOEC",Conf.idioma));
		botonCronoEstrCompleta.addActionListener(this);
		botonCronoEstrCompleta.addKeyListener(this);
		
		botonCronoSalLigadaEnt=new JCheckBox(Texto.get("COOV_VCRONOSLE",Conf.idioma));
		botonCronoSalLigadaEnt.addActionListener(this);
		botonCronoSalLigadaEnt.addKeyListener(this);
		
		panelVistaCrono.add(botonCronoEstrCompleta);
		panelVistaCrono.add(botonCronoSalLigadaEnt);
		panelVistaCrono.setBorder(new TitledBorder(Texto.get("V_CRONO",Conf.idioma)));
		
		// Panel que recoge los paneles anteriores
		JPanel panelIzqda = new JPanel();
		panelIzqda.setLayout(new BorderLayout());
		panelIzqda.add(panelDatos,BorderLayout.NORTH);
		panelIzqda.add(panelHistoria,BorderLayout.CENTER);
		panelIzqda.add(panelMostrarEstructura,BorderLayout.SOUTH);
		
		// Panel que recoge los paneles anteriores
		JPanel panelDcha = new JPanel();
		panelDcha.setLayout(new GridLayout(3,1));
		panelDcha.add(panelSalto);
		panelDcha.add(panelMostrarVisor);
		panelDcha.add(panelVistaCrono);
		/*panelDcha.setLayout(new BorderLayout());
		panelDcha.add(panelSalto,BorderLayout.NORTH);
		panelDcha.add(panelMostrarVisor,BorderLayout.CENTER);
		panelDcha.add(panelVistaCrono,BorderLayout.SOUTH);*/
		

		
		// Panel que recoge los tres paneles anteriores
		JPanel panel3paneles = new JPanel();
		GridLayout glf=new GridLayout(1,2);
		panel3paneles.setLayout(glf);
		panel3paneles.add(panelIzqda);
		panel3paneles.add(panelDcha);
		
		
		/*JPanel panel3panelesA = new JPanel();
		JPanel panel3panelesB = new JPanel();
		
		panel3paneles.setLayout(new BorderLayout());
		panel3panelesA.setLayout(new BorderLayout());
		panel3panelesB.setLayout(new BorderLayout());
		
		
		panel3panelesA.add(panelDatos,BorderLayout.NORTH);
		panel3panelesA.add(panelHistoria,BorderLayout.SOUTH);
		
		panel3panelesB.add(panelSalto,BorderLayout.NORTH);
		panel3panelesB.add(panelMostrarVisor,BorderLayout.SOUTH);
		
		panel3paneles.add(panel3panelesA,BorderLayout.NORTH);
		panel3paneles.add(panel3panelesB,BorderLayout.SOUTH);*/

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(aceptar);

		//aceptar.addActionListener(this);
		aceptar.addMouseListener(this);
	
		// Panel general
		BorderLayout bl= new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);
	
		panel.add(panel3paneles,BorderLayout.NORTH);
		panel.add(panelBoton,BorderLayout.SOUTH);
			
		// Preparamos y mostramos cuadro
		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("COOV_OPSANIM",Conf.idioma));

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
		setValores();		
		dialogo.setVisible(true);
	}

	
	
	
	/**
		Asigna valores a los elementos del cuadro
	
		@param tipo ayuda a diferenciar entre rellenar los campos para la entrada y la salida
	*/
	private void setValores()
	{
		if (oov.getDatosMostrar()==OpcionOpsVisualizacion.DATOS_ENTRADA)
			botonesDatos[0].setSelected(true);
		else if (oov.getDatosMostrar()==OpcionOpsVisualizacion.DATOS_SALIDA)
			botonesDatos[1].setSelected(true);
		else if (oov.getDatosMostrar()==OpcionOpsVisualizacion.DATOS_TODOS)
			botonesDatos[2].setSelected(true);

		if (oov.getHistoria()==OpcionOpsVisualizacion.MANTENER_HISTORIA)
			botonesHistoria[0].setSelected(true);
		else if (oov.getHistoria()==OpcionOpsVisualizacion.ATENUAR_HISTORIA)
			botonesHistoria[1].setSelected(true);
		else if (oov.getHistoria()==OpcionOpsVisualizacion.ELIMINAR_HISTORIA)
			botonesHistoria[2].setSelected(true);
			
		if (oov.getMostrarArbolSalto())
			botonesSalto[0].setSelected(true);
		else
			botonesSalto[1].setSelected(true);
		
		if (oov.getMostrarVisor())
			botonMostrarVisor.setSelected(true);
		
		//System.out.println("getMostrarEstructuraEnArbol > "+( oov.getMostrarEstructuraEnArbol() ));
		
		if (oov.getMostrarEstructuraEnArbol())
			botonMostrarEstructuraArbol.setSelected(true);
		
		if (oov.getMostrarEstructuraCompletaCrono())
			botonCronoEstrCompleta.setSelected(true);
		
		if (oov.getMostrarSalidaLigadaEntrada())
			botonCronoSalLigadaEnt.setSelected(true);
	}

	
	/**
		Comprueba que la cadena s contenga un valor válido (0..255)
		
		@param s cadena que será comprobada
		@return true si la cadena contiene un valor válido para una componente RGB
	*/	
	private void getValores()
	{
		if (botonesDatos[0].isSelected())
			oov.setDatosMostrar(OpcionOpsVisualizacion.DATOS_ENTRADA);
		else if (botonesDatos[1].isSelected())
			oov.setDatosMostrar(OpcionOpsVisualizacion.DATOS_SALIDA);	
		else if (botonesDatos[2].isSelected())
			oov.setDatosMostrar(OpcionOpsVisualizacion.DATOS_TODOS);
			
		if (botonesHistoria[0].isSelected())
			oov.setHistoria(OpcionOpsVisualizacion.MANTENER_HISTORIA);
		else if (botonesHistoria[1].isSelected())
			oov.setHistoria(OpcionOpsVisualizacion.ATENUAR_HISTORIA);
		else if (botonesHistoria[2].isSelected())
			oov.setHistoria(OpcionOpsVisualizacion.ELIMINAR_HISTORIA);
			
		oov.setMostrarArbolSalto( botonesSalto[0].isSelected() );
		
		oov.setMostrarVisor( botonMostrarVisor.isSelected() );
		
		oov.setMostrarEstructuraEnArbol( botonMostrarEstructuraArbol.isSelected() );
		
		oov.setMostrarEstructuraCompletaCrono( botonCronoEstrCompleta.isSelected() );
		oov.setMostrarSalidaLigadaEntrada( botonCronoSalLigadaEnt.isSelected() );
	}
	
	

	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JRadioButton ||
			e.getSource() instanceof JCheckBox)
		{
			getValores();
			gOpciones.setOpcion(oov,1);
			
			Conf.setValoresOpsVisualizacion(false);
			Conf.setValoresVisualizacion();
			if (Ventana.thisventana.traza!=null)
			{
				Ventana.thisventana.actualizarTraza();
				Ventana.thisventana.refrescarFormato();
				
				Conf.setRedibujarGrafoArbol(false);
			}
		}
		else if (e.getSource() instanceof JButton)
			if (e.getSource()==aceptar)
				dialogo.setVisible(false);
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
		if (e.getSource().getClass().getName().contains("JRadioButton"))
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
		if (e.getSource()==aceptar)
			this.dialogo.setVisible(false);
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseReleased(MouseEvent e)
	{
	}
}
