/**
	Representa la clase del cuadro que ofrece la selección del método que se desea visualizar
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;




import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;

import java.lang.Character;
import java.lang.Integer;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

import java.util.ArrayList;

import botones.*;
import conf.*;
import datos.*;
import utilidades.*;
import ventanas.*;

public class CuadroMetodosProcesadosSelecMetodo extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=02;
	private final boolean DEPURAR=false;

	final int anchoCuadro=450;
	final int altoCuadro=550;		// 130 aquí actúa como medida base inicial
	

	BotonAceptar aceptar;
	BotonCancelar cancelar;
	JPanel panel, panelBoton, panelBotones;
	int numero;

	JCheckBox checkTodos;
	
	ButtonGroup grupoBotones;
	JRadioButton[] botonesRadio;
	JCheckBox[] botonesCheck;

	String [] valores;
	
	Preprocesador p;
	BorderLayout bl;
	

	JFrame vv;

	JDialog dialogo;
	CuadroParam cuadroParam;
	boolean luzVerde=false;
	
	ClaseAlgoritmo clase;
	ArrayList<MetodoAlgoritmo> metodos=new ArrayList<MetodoAlgoritmo>(0);

	/**
		Contructor: genera un nuevo cuadro de diálogo que permite al usuario elegir qué método de una determinada clase se quiere ejecutar
		
		@param clase clase a la que pertenece el método que se quiere ejecutar
		@param ventanaVisualiazdor ventana a la que se asociará el cuadro de diálogo
		@param gestorEjecucion gestor que realizará los pasos necesarios para ejecutar el método seleccionado
		@param codigounico código único que identifica a la clase y la da nombre
	*/
	public CuadroMetodosProcesadosSelecMetodo(ClaseAlgoritmo clase, Preprocesador p)
	{	
	
		dialogo = new JDialog(Ventana.thisventana,true);
	
		// Inicializamos atributos del cuadro de parámetros
		this.vv=Ventana.thisventana;
		this.clase=clase;
		this.numero=this.clase.getNumMetodosProcesados();//this.metodos.length;
		this.p=p;
		
		this.metodos=this.clase.getMetodosProcesados();
		
		this.start();
	}

	/**
		Genera un nuevo cuadro de diálogo que permite al usuario elegir qué método de una determinada clase se quiere ejecutar
	*/	
	public void run()
	{
		
		if (this.numero>0)
		{
			
			
			checkTodos=new JCheckBox(Texto.get("CMP_TODOSMETODOS",Conf.idioma));
			checkTodos.addMouseListener(this);
			checkTodos.addKeyListener(this);
			checkTodos.setToolTipText(Texto.get("CMP_SELECTODMET",Conf.idioma));
			
			//Estudiamos si debemos marcar o no checkTodos
			boolean valorCheckTodos=true;
			for (int i=0; i<this.numero; i++)
				if (!this.metodos.get(i).getMarcadoVisualizar())
					valorCheckTodos=false;
			
			checkTodos.setSelected(valorCheckTodos);

			// Panel general de métodos
			JPanel panelGeneral=new JPanel();
			panelGeneral.setLayout(new BorderLayout());
			


			grupoBotones = new ButtonGroup();
			botonesRadio = new JRadioButton[this.numero];
			botonesCheck = new JCheckBox[this.numero];
			
			
			int llamadasRepresentar=0;
			
			for (MetodoAlgoritmo m : this.metodos)
				llamadasRepresentar= llamadasRepresentar + m.getMetodosLlamados().length;
			
			// Panel de métodos (se divide en dos, de dos columnas de "cosas" en cada uno)
			JPanel panelMetodos=new JPanel();
			panelMetodos.setLayout(new GridLayout(numero+llamadasRepresentar,1));
			
			
			String toolTipTextRadio=Texto.get("CMP_SELECTMET",Conf.idioma);
			String toolTipTextCheck=Texto.get("CMP_SELECTMETV",Conf.idioma);
			
			
			int y=0;
			for (int i=0; i<this.numero; i++)
			{
				String cadenaEtiqueta=this.metodos.get(i).getRepresentacion()+( this.metodos.get(i).getTecnica()==MetodoAlgoritmo.TECNICA_DYV ? "  <D>" : "");
				
				JPanel panel=new JPanel();	// Panel que engloba toda la fila
				panel.setLayout(new BorderLayout());
				
				JPanel panelIzquierda=new JPanel();	// Panel que tiene check, radio y el texto
				panelIzquierda.setLayout(new BorderLayout());
				botonesRadio[i] = new JRadioButton();
				botonesRadio[i].setFocusable(false);
				botonesCheck[i] = new JCheckBox(cadenaEtiqueta);
				botonesCheck[i].setFocusable(false);
				grupoBotones.add(botonesRadio[i]);
				botonesRadio[i].setSelected(this.metodos.get(i).getMarcadoPrincipal());
				botonesCheck[i].setSelected(this.metodos.get(i).getMarcadoVisualizar());
			
				botonesRadio[i].setToolTipText(toolTipTextRadio+" "+cadenaEtiqueta);
				botonesCheck[i].setToolTipText(toolTipTextCheck+" "+cadenaEtiqueta);
			
				botonesRadio[i].addMouseListener(this);
				botonesCheck[i].addMouseListener(this);
			
				JPanel panelAux=new JPanel();
				panelAux.add(botonesRadio[i]);
				panelAux.add(botonesCheck[i]);
				panelIzquierda.add(panelAux,BorderLayout.WEST);
								
				panelIzquierda.setPreferredSize(new Dimension(400,28));
				
				panel.add(panelIzquierda,BorderLayout.WEST);
				
				panelMetodos.add(panel);
				
				y++;
				
				int[] llamadas=this.metodos.get(i).getMetodosLlamados();
				for (int j=0; j<llamadas.length; j++)
				{
					panel=new JPanel();
					panel.setLayout(new BorderLayout());
					
					JPanel izqdaLlamada=new JPanel();
					JLabel etiquetaSignaturaLlamado=new JLabel("                     - "+this.clase.getMetodoID(llamadas[j]).getRepresentacion());
					
					izqdaLlamada.add(etiquetaSignaturaLlamado);
					
					panel.add(izqdaLlamada,BorderLayout.WEST);
					
					panelMetodos.add(panel);
					
					y++;
				}

				
			}
			
			// Si no hay ningún método seleccionado, seleccionamos el primero
			boolean ningunoMarcado=true;
			for (int i=0; i<this.numero; i++)
				if (botonesRadio[i].isSelected())
					ningunoMarcado=false;
			
			if (ningunoMarcado)
				botonesRadio[0].setSelected(true);
			
			
			JPanel panelTextoSignaturas=new JPanel();
			panelTextoSignaturas.add(new JLabel( Texto.get("CSM_SIGNAT",Conf.idioma) ));

			JPanel panelCheckTodos=new JPanel();
				JPanel panelEspacio=new JPanel();
				panelEspacio.setPreferredSize(new Dimension(31,10));
			
			panelCheckTodos.setLayout(new BorderLayout());
			panelCheckTodos.add(panelEspacio,BorderLayout.WEST);
			panelCheckTodos.add(checkTodos,BorderLayout.CENTER);
			
			
			
			JPanel panelCabecera=new JPanel();
			panelCabecera.setLayout(new BorderLayout());
			panelCabecera.add(panelTextoSignaturas,BorderLayout.WEST);
			panelCabecera.add(panelCheckTodos,BorderLayout.SOUTH);
			
			
			JPanel panelContenedorMetodos=new JPanel();
			panelContenedorMetodos.setLayout(new BorderLayout());
			panelContenedorMetodos.add(panelMetodos,BorderLayout.NORTH);
			
			JScrollPane jsp=new JScrollPane(panelContenedorMetodos);
			jsp.setBorder(new EmptyBorder(0,0,0,0));
			jsp.setPreferredSize(new Dimension(anchoCuadro-10,altoCuadro-136));
			jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			
			panelGeneral.setBorder(new TitledBorder(this.numero+" "+Texto.get("CMP_METPROC",Conf.idioma)+" "+clase.getNombre() ));	
			panelGeneral.add(panelCabecera,BorderLayout.NORTH);
			panelGeneral.add(jsp,BorderLayout.CENTER);
			
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
	
			panel.add(panelGeneral,BorderLayout.NORTH);
			panel.add(panelBoton,BorderLayout.SOUTH);
			
			// Preparamos y mostramos cuadro
			dialogo.getContentPane().add(panel);
			dialogo.setTitle(Texto.get("CMP_ESCOGER",Conf.idioma));
			int alto=28;
			if (!Ventana.thisventana.msWindows)
				alto=24;
			dialogo.setSize(anchoCuadro,altoCuadro);//+(alto*numero));
			int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);//+(alto*numero));
			dialogo.setLocation(coord[0],coord[1]);
			dialogo.setResizable(false);
			dialogo.setVisible(true);
		}
		else
			new CuadroError(vv , Texto.get("ERROR_CLASE",Conf.idioma) , Texto.get("ERROR_NOMETVIS",Conf.idioma));
		
	}

	/**
		Recoge la selección del método señalado por el usuario y da paso al proprocesador
	*/	
	public void recogerMetodoSeleccionado()
	{
		boolean errorProducido=false;
		
		// Actualizar cuál es el método principal
		this.clase.borrarMarcadoPrincipal();
		for (int i=0; i<this.numero; i++)
		{
			this.metodos.get(i).setMarcadoPrincipal( botonesRadio[i].isSelected() );
			this.metodos.get(i).setMarcadoVisualizar( botonesCheck[i].isSelected() );
			this.clase.addMetodo(this.metodos.get(i));
		}
		
		if (!errorProducido)
		{
			// Actualizamos la clase
			MetodoAlgoritmo ma=this.clase.getMetodoPrincipal();
			if (Conf.fichero_log)
			{
				String mensaje="Método seleccionado: "+ma.getRepresentacion();
				Logger.log_write(mensaje);
			}
			Ventana.thisventana.setClase(this.clase);
			dialogo.setVisible(false);
			
			//Limpiamos los paneles de visualizacion
			Ventana.thisventana.abrirPanelCodigo(true,true);
			//Escribir signatura del método seleccionado
			Ventana.thisventana.setValoresPanelControl(ma.getRepresentacion());
			//Habilitamos la opcion para asignar parametros y ejecutar
			Ventana.thisventana.setClaseHabilitadaAnimacion(true);
			Ventana.thisventana.setClasePendienteGuardar(false);
			
		}
	
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
		
		if (code==KeyEvent.VK_ENTER)
		{
			recogerMetodoSeleccionado();
		}
		else if (code==KeyEvent.VK_DOWN)
		{
			for (int i=0; i<botonesRadio.length; i++)
				if (botonesRadio[i].isFocusOwner())
					if (i!=botonesRadio.length-1)
						botonesRadio[i].transferFocus();
		}
		else if (code==KeyEvent.VK_UP)
		{
			for (int i=0; i<botonesRadio.length; i++)
				if (botonesRadio[i].isFocusOwner())
					if (i!=0)
						botonesRadio[i].transferFocusBackward();
		}
		else if (code==KeyEvent.VK_ESCAPE)
			dialogo.setVisible(false);
		else
		{
			if (!(e.getSource().getClass().getName().contains("Boton")))
			{
				for (int i=0; i<botonesRadio.length; i++)
					if (botonesRadio[i].isFocusOwner())
						botonesRadio[i].setSelected(true);
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

	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/				
	public void mouseReleased(MouseEvent e)
	{
		
		if ( e.getSource()==aceptar)
			recogerMetodoSeleccionado();
		else if ( e.getSource()==cancelar)
			this.dialogo.setVisible(false);
		
		
		
		else if ( e.getSource()==checkTodos)
		{
			for (int i=0; i<botonesCheck.length; i++)
				botonesCheck[i].setSelected(checkTodos.isSelected() || botonesRadio[i].isSelected());
		}
		else if (e.getSource().getClass().getName().contains("JRadioButton"))
		{
			for (int i=0; i<this.numero; i++)
			{
				if (botonesRadio[i].isSelected())
				{
					botonesCheck[i].setSelected(true);
				}
			}
		}
		else if (e.getSource().getClass().getName().contains("JCheckBox"))
		{
			// Hacemos que si se intenta hacer que el método principal no sea visible, se vuelva a marcar como visible
			for (int i=0; i<this.numero; i++)
			{
				if (botonesRadio[i].isSelected())
					botonesCheck[i].setSelected(true);
			}
			
			// Ahora hacemos que los botones de opciones queden inhabilitados para métodos no visibles
			for (int i=0; i<this.numero; i++)
				if (botonesCheck[i]==((JCheckBox)e.getSource()))
				{
					if (!botonesCheck[i].isSelected())
						checkTodos.setSelected(false);
				}
			
			
			
		}
		
	}

	
	public void setLuzVerde(boolean v)
	{
		this.luzVerde=v;
		if (cuadroParam!=null)
			cuadroParam.setLuzVerde(v);
	}
	
	public JDialog getDialogo()
	{
		return this.dialogo;
	}
}


