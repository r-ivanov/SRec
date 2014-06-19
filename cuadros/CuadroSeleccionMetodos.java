package cuadros;

/**
Representa la clase del cuadro que maneja la opción que gestiona el mantenimiento de ficheros intermedios

@author Antonio Pérez Carrasco
@version 2006-2007
*/

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.lang.Integer;

import java.util.ArrayList;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


import botones.*;
import datos.*;
import utilidades.*;
import conf.*;
import ventanas.*;

public class CuadroSeleccionMetodos extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=02;
	//private final boolean DEPURAR=false;
	
	final int anchoCuadro=720;
	final int altoCuadro=420;		// 120 Actúa como base
	
	BotonAceptar aceptar;
	BotonCancelar cancelar;
	JPanel panel, panelBoton, panelBotones;
	int numero;			// Numero total de métodos visualizables
	
	ButtonGroup grupoBotones[];
	JCheckBox botonesDYV[];
	
	
	int alturaFila=23;
	
	//JCheckBox[] botones;
	int []posicBotones;
	
	JTextField[] estructura;	// Cuadro de texto para que el usuario escriba el nº de parámetro en el que está la estructura
	JTextField[] indices;		// Cuadro de texto para que el usuario escriba los nº de parámetro en los que están los índices
	
	String [] valores;
	
	ClaseAlgoritmo clase;
	ArrayList<MetodoAlgoritmo> metodos;			// Listado de métodos seleccionados para ser procesados
	
	BorderLayout bl;
	GridLayout gl;
	
	JFrame vv;
	Preprocesador preprocesador;
	JDialog dialogo;
	CuadroParam cuadroParam;
	boolean luzVerde=false;
	
	/**
		Contructor: genera un nuevo cuadro de diálogo que permite al usuario elegir qué método de una determinada clase se quiere ejecutar
		
		@param clase clase a la que pertenece el método que se quiere ejecutar
		@param ventanaVisualiazdor ventana a la que se asociará el cuadro de diálogo
		@param gestorEjecucion gestor que realizará los pasos necesarios para ejecutar el método seleccionado
		@param codigounico código único que identifica a la clase y la da nombre
	*/
	public CuadroSeleccionMetodos(ClaseAlgoritmo claseAlgoritmo, JFrame ventanaVisualizador, Preprocesador preprocesador)
	{	
		dialogo = new JDialog(ventanaVisualizador,true);
	
		// Inicializamos atributos del cuadro de parámetros
		this.vv=ventanaVisualizador;
		this.clase=claseAlgoritmo;
		this.metodos=claseAlgoritmo.getMetodos();
		this.numero=this.metodos.size();
		this.preprocesador=preprocesador;
	
		this.start();
	}
	
	/**
		Genera un nuevo cuadro de diálogo que permite al usuario elegir qué métodos de una determinada
		clase se quieren mantener visualizables
	*/	
	public void run()
	{
		
		//new CuadroIdentificarParametros(true,"HOLA");
		
		if (this.numero>0)
		{
			String nombreClase=this.clase.getNombre();
				
			int llamadasRepresentar=0;
			
			for (MetodoAlgoritmo m: this.metodos)
				llamadasRepresentar= llamadasRepresentar + m.getMetodosLlamados().length;

			// Panel de Botones
			gl = new GridLayout(numero+llamadasRepresentar,1);
			panelBotones = new JPanel();
			panelBotones.setLayout(gl);
			//panelBotones.setLayout(new BorderLayout());
			
			
			posicBotones=new int[this.numero];
			
			//botones = new JCheckBox[this.numero];
			estructura = new JTextField[this.numero];
			indices = new JTextField[this.numero];
			
			grupoBotones = new ButtonGroup[this.numero];

			botonesDYV = new JCheckBox[this.numero];
		
		
			JPanel panelFilaSup=new JPanel();
			panelFilaSup.setLayout(new BorderLayout());
			
			JLabel etiqSignatura=new JLabel(Texto.get("CSM_SIGNAT",Conf.idioma));
			//JLabel etiqIndices=new JLabel(Texto.get("CSM_INDICAC",Conf.idioma));
			
			
			JPanel panelDerechaSuperior=new JPanel();
			
			int alturaFilaSuperior=20;
			
			JPanel panelEtiqEstructura=new JPanel();
			panelEtiqEstructura.setPreferredSize(new Dimension(70,alturaFilaSuperior));
			panelEtiqEstructura.add(new JLabel(Texto.get("CSM_FILASUPESTR",Conf.idioma)));
						
			JPanel panelEtiqIndices=new JPanel();
			panelEtiqIndices.setPreferredSize(new Dimension(70,alturaFilaSuperior));
			panelEtiqIndices.add(new JLabel(Texto.get("CSM_FILASUPINDIC",Conf.idioma)));
			
			JPanel panelEtiqVacia=new JPanel();
			panelEtiqVacia.setPreferredSize(new Dimension(3,alturaFilaSuperior));
			panelEtiqVacia.add(new JLabel(""));
			
			
			panelDerechaSuperior.add(panelEtiqEstructura, BorderLayout.WEST);
			panelDerechaSuperior.add(panelEtiqIndices, BorderLayout.CENTER);
			panelDerechaSuperior.add(panelEtiqVacia, BorderLayout.EAST);
			
			
			panelFilaSup.add(etiqSignatura,BorderLayout.WEST);
			panelFilaSup.add(panelDerechaSuperior,BorderLayout.EAST);
		
			//panelBotones.add(panelFilaSup,0);
			
			//panelBotones.add(new JSeparator(SwingConstants.HORIZONTAL),1);
		
			String toolTipEstr	= Texto.get("CSM_INDICAESTR",Conf.idioma);
			String toolTipInd	= Texto.get("CSM_INDICAPARAM",Conf.idioma);
			
			String toolTipDYVsi	= Texto.get("CSM_MARCPROC",Conf.idioma);
			String toolTipDYVno	= Texto.get("CSM_MARCPROCNO",Conf.idioma);
			
			
			int y=0;
			for (int x=0; x<numero; x++)
			{
				JPanel panelFila=new JPanel();
				panelFila.setLayout(new BorderLayout());
				
				// Parte derecha
				JPanel panelFilaParteDerecha=new JPanel();
				panelFilaParteDerecha.setLayout(new BorderLayout());
				
				estructura[x]=new JTextField(8);
				indices[x]=new JTextField(8);
				
				estructura[x].addKeyListener(this);
				indices[x].addKeyListener(this);
				
				estructura[x].setEnabled(false);
				indices[x].setEnabled(false);
				
				estructura[x].setHorizontalAlignment(JTextField.CENTER);//JTextField.RIGHT
				indices[x].setHorizontalAlignment(JTextField.CENTER);
				
				estructura[x].setToolTipText( toolTipEstr );
				indices[x].setToolTipText( toolTipInd );
				
				
				
				JPanel panelContenedorEstructura=new JPanel();
				panelContenedorEstructura.add(estructura[x]);
				panelContenedorEstructura.setPreferredSize(new Dimension(70,24));
				JPanel panelContenedorIndices=new JPanel();
				panelContenedorIndices.add(indices[x]);
				panelContenedorIndices.setPreferredSize(new Dimension(70,24));
				//JPanel panelContenedorRadio=new JPanel();
				//panelContenedorRadio.add(botonesREC[x]);
				//panelContenedorRadio.add(botonesDYV[x]);
	
				panelFilaParteDerecha.add(panelContenedorEstructura, BorderLayout.WEST);
				panelFilaParteDerecha.add(panelContenedorIndices, BorderLayout.CENTER);
				//panelFilaParteDerecha.add(panelContenedorRadio, BorderLayout.EAST);
				
				/*panelFilaParteDerecha.add(estructura[x], BorderLayout.WEST);
				panelFilaParteDerecha.add(indices[x], BorderLayout.EAST);*/
							
				
				// Parte izquierda
				JPanel panelFilaParteIzquierda=new JPanel();
								
				String representacion = this.metodos.get(x).getRepresentacion();
				int[] dimParametros = this.metodos.get(x).getDimParametros();
				boolean hayArrayOMatriz=false;
				for (int i=0; i<dimParametros.length; i++)
					if (dimParametros[i]>0)
						hayArrayOMatriz=true;
				
				botonesDYV[x] = new JCheckBox(representacion);
				botonesDYV[x].setName(representacion);
				
				posicBotones[x]=y;
				
				if (hayArrayOMatriz)
				{
					botonesDYV[x].setToolTipText(toolTipDYVsi);
				}
				else
				{
					botonesDYV[x].setToolTipText(toolTipDYVno);
					botonesDYV[x].setEnabled(false);
				}
				
				
				//panelBotones.add(botones[x],x);
				panelFilaParteIzquierda.add(botonesDYV[x]);
				botonesDYV[x].addKeyListener(this);
				botonesDYV[x].addActionListener(this);
				
								
				panelFila.add(panelFilaParteIzquierda,BorderLayout.WEST);
				panelFila.add(panelFilaParteDerecha,BorderLayout.EAST);
				
				panelFila.setPreferredSize(new Dimension(550,25));
				
				panelBotones.add(panelFila,y);
				y++;
				
				int[] llamadas=this.metodos.get(x).getMetodosLlamados();
				for (int i=0; i<llamadas.length; i++)
				{
					panelFila=new JPanel();
					panelFila.setLayout(new BorderLayout());
					
					JPanel izqdaLlamada=new JPanel();
					JLabel etiquetaSignaturaLlamado=new JLabel("            - "+this.clase.getMetodoID(llamadas[i]).getRepresentacion());
					if (!hayArrayOMatriz)
						etiquetaSignaturaLlamado.setEnabled(false);
					izqdaLlamada.add(etiquetaSignaturaLlamado);
					
					panelFila.setPreferredSize(new Dimension(550,25));
					
					panelFila.add(izqdaLlamada,BorderLayout.WEST);
					//panelFila.add(panelFilaParteDerecha,BorderLayout.EAST);
					
					panelBotones.add(panelFila,y);

					y++;
				}
				
			}
	
			
			
			
			
			
			// Botón Aceptar
			aceptar=new BotonAceptar();//aceptar=new JButton ("Aceptar");
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
	
			
			
			JPanel panelContenedorBotones=new JPanel();
			panelContenedorBotones.setLayout(new BorderLayout());
			panelContenedorBotones.add(panelBotones,BorderLayout.NORTH);
			
			
			JScrollPane jsp=new JScrollPane(panelContenedorBotones);
			
			//jsp.add(panelBotones);
			jsp.setBorder(new EmptyBorder(0,0,0,0));
			jsp.setPreferredSize(new Dimension(anchoCuadro-10,altoCuadro-120));
			
			
			JPanel panelSup=new JPanel();
			panelSup.setLayout(new BorderLayout());
			
			panelSup.add(panelFilaSup,BorderLayout.NORTH);
			panelSup.add(jsp,BorderLayout.CENTER);
			jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			panelSup.setBorder(new TitledBorder(this.numero+" "+Texto.get("CSM_METDISP",Conf.idioma)+" "+nombreClase));
			
			
			
			panel.add(panelSup,BorderLayout.NORTH);
			panel.add(panelBoton,BorderLayout.SOUTH);
			
			// Preparamos y mostramos cuadro
			dialogo.getContentPane().add(panel);
			dialogo.setTitle(Texto.get("CSM_ESCMET",Conf.idioma));
	
			
			//dialogo.setSize(anchoCuadro,altoCuadro+(alto*numero));
			dialogo.setSize(anchoCuadro,altoCuadro);
			int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
			dialogo.setLocation(coord[0],coord[1]);
			dialogo.setResizable(false);
			dialogo.setVisible(true);
			
			
		}
		else
			new CuadroError(vv,Texto.get("ERROR_CLASE",Conf.idioma), Texto.get("CSM_NOVISMET",Conf.idioma));
	}
	
	/**
		Recoge la selección del método señalado por el usuario y da paso al cuadro de parámetros
	*/	
	public void recogerMetodosSeleccionados()
	{
		boolean huboError=false;

		for (int i=0; i<this.clase.getNumMetodos(); i++)
			if (	(this.botonesDYV[i].isSelected() //&& (valorCorrectoE(estructura[i],i) && valorCorrectoI(indices[i],i)))
					|| !(this.botonesDYV[i].isSelected()) )	)
			{
				this.clase.getMetodo(i).setMarcadoProcesar(true);
				
				if (this.botonesDYV[i].isSelected())
				{
					this.clase.getMetodo(i).setTecnica(MetodoAlgoritmo.TECNICA_DYV);
					Ventana.thisventana.habilitarOpcionesDYV(true);
					this.clase.getMetodo(i).setIndices(Integer.parseInt(estructura[i].getText().replace(" ","")));
					
					int parametrosIndice[]=ServiciosString.extraerValoresInt(indices[i].getText(), ',');

					if (parametrosIndice==null)
						this.clase.getMetodo(i).setIndices(Integer.parseInt(estructura[i].getText().replace(" ",""))+1);
					else if (parametrosIndice.length==2)
						this.clase.getMetodo(i).setIndices(Integer.parseInt(estructura[i].getText().replace(" ",""))+1,
							parametrosIndice[0]+1,parametrosIndice[1]+1);
					else
						this.clase.getMetodo(i).setIndices(Integer.parseInt(estructura[i].getText().replace(" ",""))+1,
							parametrosIndice[0]+1,parametrosIndice[1]+1,parametrosIndice[2]+1,parametrosIndice[3]+1);
				}
				else
					this.clase.getMetodo(i).setTecnica(MetodoAlgoritmo.TECNICA_REC);
			}
			else
				huboError=true;
	
		
		if (!huboError)
		{
			dialogo.setVisible(false);
			preprocesador.fase2(this.clase);
		}
		else
			new CuadroError(this.dialogo,Texto.get("ERROR_VAL",Conf.idioma),Texto.get("ERROR_VALESCR",Conf.idioma));

		
	}
	
	
	
	private boolean valorCorrectoE(JTextField jtf, int numMetodo)
	{
		// Tenemos n parámetros en el método, numerados internamente de 0 a n-1,
		// el usuario introducirá un número entre 1 y n, por lo que al número introducido hay que restarle 1
		int x;
		
		try {
			 x= (Integer.parseInt(jtf.getText()));
			 
			 MetodoAlgoritmo m=this.clase.getMetodo(numMetodo);
			 
			 if (x<0)	// Si el número dado es negativo...
				 return false;
			 
			 if (x>m.getNumeroParametros())		// Si el número dado es mayor que el número de parámetros...
				 return false;
			 
			 if (m.getDimParametro(x)<=0 || m.getDimParametro(x)>=3)	// Si el parámetro no es ni array ni matriz
				 return false;
			 
			 return true;
			 
		} catch (Exception e) {
			return false;
		}
	}
	
	
	private boolean valorCorrectoI(JTextField jtf, int numMetodo)
	{
		// Tenemos n parámetros en el método, numerados internamente de 0 a n-1,
		// el usuario introducirá un número entre 1 y n, por lo que al número introducido hay que restarle 1
	
		String s=jtf.getText().replace(" ","");
		if (s.length()==0)
			return true;
	
		int elem[]=ServiciosString.extraerValoresInt(s,',');
		if (elem==null)
			return false;
		for (int i=0; i<elem.length; i++)
			elem[i]=elem[i]-1;
		
		
		int numElementos=elem.length;
		if (numElementos!=2 && numElementos!=4 && numElementos!=0)
			return false;
		
		int numParametros=this.clase.getMetodo(numMetodo).getNumeroParametros();
		
		for (int i=0; i<numElementos; i++)
		{
			//System.out.println("NumParametro="+(i+1)+" Dim="+this.clase.getMetodo(numMetodo).getDimParametro(i));
			try {
				if (!(elem[i]>0 && elem[i]<=numParametros))	// Si el número de parámetro está fuera del rango
					return false;
				
				if (elem[i]==valorE(estructura[numMetodo]))	// Si el número de Param se ha teclado ya para la estructura
					return false;
				
				if (!(this.clase.getMetodo(numMetodo).getTipoParametro(elem[i]).equals("int")) ||
						this.clase.getMetodo(numMetodo).getDimParametro(elem[i])>0)
				{
					//System.out.println("  Error en "+(i+1));
					return false;
				}
					
			} catch (Exception e) {
				return false;
			}
		}
	
		// Miramos si hay valores repetidos
		for (int i=0; i<numElementos; i++)
			for (int j=0; j<numElementos; j++)
				if (elem[i]==elem[j] && i!=j)
					return false;
	
		
					
		return true;
	}	
	
	private int valorE(JTextField jtf)
	{
		String s=jtf.getText().replace(" ","");
		return (Integer.parseInt(s)-1);
	}
	
	
	
	protected void marcarMetodo(int numMetodo, boolean valor)
	{
		botonesDYV[numMetodo].setSelected(valor);
		
	}
	
	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/		
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==aceptar)
			recogerMetodosSeleccionados();
		else if (e.getSource()==cancelar)
		{
			Ventana.setProcesando(false);
			dialogo.setVisible(false);
		}
		else if (e.getSource().getClass().getName().contains("JCheckBox"))
		{
			for (int i=0; i<numero; i++)
				if (botonesDYV[i]==e.getSource())
				{
					if (botonesDYV[i].isSelected())
						new CuadroIdentificarParametros(Ventana.thisventana, this,this.metodos.get(i),i,
								estructura[i].getText(),indices[i].getText());
				}
		}
	}
	
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyPressed(KeyEvent e)
	{
		//int code=e.getKeyCode();
	
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
			recogerMetodosSeleccionados();
		}
		/*else if (code==KeyEvent.VK_DOWN)
		{
			for (int i=0; i<botones.length; i++)
				if (botones[i].isFocusOwner())
					if (i!=botones.length-1)
						botones[i].transferFocus();
		}
		else if (code==KeyEvent.VK_UP)
		{
			for (int i=0; i<botones.length; i++)
				if (botones[i].isFocusOwner())
					if (i!=0)
						botones[i].transferFocusBackward();
		}*/
		else if (code==KeyEvent.VK_ESCAPE)
		{
			dialogo.setVisible(false);
			Ventana.setProcesando(false);
		}
		else
		{
			if (e.getSource() instanceof JTextField)
			{
				for (int i=0; i<estructura.length; i++)
				{
					if (e.getSource()==estructura[i] || e.getSource()==indices[i])
					{
						botonesDYV[i].setSelected(true);
						//if (estructura[i].getText().replace(" ","").length()==0 && indices[i].getText().replace(" ","").length()==0)
						//	botonesREC[i].setSelected(true);
					}
					
					
				}
			}
		}
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyTyped(KeyEvent e)
	{
		//int code=e.getKeyCode();
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
			recogerMetodosSeleccionados();
		else if ( e.getSource()==cancelar)
		{
			Ventana.setProcesando(false);
			this.dialogo.setVisible(false);
		}
	}
	
	
	public void setLuzVerde(boolean v)
	{
		this.luzVerde=v;
		if (cuadroParam!=null)
			cuadroParam.setLuzVerde(v);
	}
	
	
	
	protected void setParametrosMetodo(int i, String paramE, String paramI)
	{
		estructura[i].setText(paramE);
		indices[i].setText(paramI);
	}
	
}