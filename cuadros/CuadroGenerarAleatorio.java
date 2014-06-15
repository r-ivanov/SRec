/**
	CAMBIAR TODOS LOS COMENTARIOS DE ESTA CLASE
	******************************************************
		
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

import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import botones.*;
import conf.*;
import datos.*;
import utilidades.*;
import ventanas.*;

public class CuadroGenerarAleatorio extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=07;

	private final int MAX_DIM_ARRAY=1000;
	private final int ALTO_CELDAS=20;

	
	final int anchoCuadro=600;
	final int altoCuadro=130;	// base
	final int anchoCuadroNW=744;		// No Windows
	final int altoCuadroNW=420;			// No Windows
	
	
	ButtonGroup botones;
	
	JCheckBox marcadorTodos;
	JCheckBox marcadores[];
	
	JTextField cuadrosValLimInf[];
	JTextField cuadrosValLimSup[];
	
	JTextField cuadrosDim1Lim[];
	JTextField cuadrosDim2Lim[];
	
	JRadioButton orden[][]; 
	
	BotonAceptar aceptar=new BotonAceptar();
	BotonCancelar cancelar=new BotonCancelar();
	
	CuadroParam cp;
	boolean cpOcple;
	CuadroParamLanzarEjec cple;
	JDialog dialogo;
	MetodoAlgoritmo metodo;
	
	/**
		Constructor: genera un nuevo cuadro de opción
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroGenerarAleatorio (CuadroParam cp, MetodoAlgoritmo metodo)
	{	
		this.cp=cp;
		cpOcple = true;
		this.dialogo=new JDialog(cp.getJDialog(),true);
		this.metodo=metodo;
		this.start();
	}
	
	public CuadroGenerarAleatorio (CuadroParamLanzarEjec cple, MetodoAlgoritmo metodo)
	{	
		this.cple=cple;
		cpOcple = false;
		this.dialogo=new JDialog(cple.getJDialog(),true);
		this.metodo=metodo;
		this.start();
	}

	/**
		Genera una nueva opción
	*/
	public void run()
	{
		this.dialogo.addKeyListener(this);
	
		// Panel que engloba a los paneles superior y medio
		JPanel panelGral = new JPanel();
		panelGral.setBorder(new TitledBorder(Texto.get("CGF_SELEC",Conf.idioma)));
		panelGral.addKeyListener(this);
		
		// Panel superior, para JCheckBox de marcado de todos los parámetros
		JPanel panelSup = new JPanel();
		BorderLayout blSup = new BorderLayout();
		panelSup.setLayout(blSup);
		marcadorTodos = new JCheckBox(Texto.get("CGF_TODOS",Conf.idioma));
		marcadorTodos.setToolTipText(Texto.get("CGF_CASILLATODOS",Conf.idioma));
		marcadorTodos.addActionListener(this);
		marcadorTodos.addKeyListener(this);
		marcadorTodos.setSelected(true);    //AQUI
		panelSup.add(marcadorTodos,BorderLayout.WEST);
		panelSup.setPreferredSize(new Dimension(80,35));
		panelSup.addKeyListener(this);
		
		// Panel medio, para JCheckBox de todos los parámetros
		JPanel panelMedio = new JPanel();
		JPanel panelInterior=new JPanel();
		panelMedio.addKeyListener(this);
		panelMedio.setBorder(new TitledBorder(Texto.get("CGF_BORDE",Conf.idioma)+" ("+this.metodo.getNombre()+")"));
		
		int numFilas=(this.metodo.getNumeroParametros()*2) + numArrays(this.metodo.getDimParametros());
		marcadores = new JCheckBox[this.metodo.getNumeroParametros()];
		cuadrosValLimInf = new JTextField[this.metodo.getNumeroParametros()];
		cuadrosValLimSup = new JTextField[this.metodo.getNumeroParametros()];
		cuadrosDim1Lim = new JTextField[this.metodo.getNumeroParametros()];
		cuadrosDim2Lim = new JTextField[this.metodo.getNumeroParametros()];
		orden = new JRadioButton[this.metodo.getNumeroParametros()][];
		GridLayout gl0 = new GridLayout(numFilas,3);
		panelInterior.setLayout(gl0);
		
		for (int i=0; i<this.metodo.getNumeroParametros(); i++)
		{
			marcadores[i]=new JCheckBox(this.metodo.getNombreParametro(i)+": "+this.metodo.getTipoParametro(i)+ServiciosString.cadenaDimensiones(this.metodo.getDimParametro(i)));
			marcadores[i].setToolTipText(Texto.get("CGF_CASILLAUNO",Conf.idioma));
			marcadores[i].setSelected(true);
			
			cuadrosValLimInf[i]=new JTextField(5);
			cuadrosValLimSup[i]=new JTextField(5);
			cuadrosValLimInf[i].setToolTipText(Texto.get("CGF_LIMINF",Conf.idioma));
			cuadrosValLimSup[i].setToolTipText(Texto.get("CGF_LIMSUP",Conf.idioma));
			cuadrosValLimInf[i].setHorizontalAlignment(JTextField.RIGHT);
			cuadrosValLimSup[i].setHorizontalAlignment(JTextField.RIGHT);
			
			if (this.metodo.getTipoParametro(i).contains("boolean") || this.metodo.getTipoParametro(i).contains("String")
				 || this.metodo.getTipoParametro(i).contains("char"))
			{
				cuadrosValLimInf[i].setEnabled(false);
				cuadrosValLimSup[i].setEnabled(false);
			}
			
			panelInterior.add(creaPanelNombre(marcadores[i]));	// añadimos celda que indica tipo con JCheckBox
			panelInterior.add(creaPanelLim(Texto.get("CGF_LIMITEINF",Conf.idioma),cuadrosValLimInf[i]));	// JTextField valor lím. inf
			panelInterior.add(creaPanelLim(Texto.get("CGF_LIMITESUP",Conf.idioma),cuadrosValLimSup[i]));	// JTextField valor lím. sup

			marcadores[i].addActionListener(this);
			marcadores[i].addKeyListener(this);
			cuadrosValLimInf[i].addKeyListener(this);
			cuadrosValLimSup[i].addKeyListener(this);
		
			if (this.metodo.getDimParametro(i)>0)
			{
				cuadrosDim1Lim[i]=new JTextField(5);
				cuadrosDim2Lim[i]=new JTextField(5);
				cuadrosDim1Lim[i].addKeyListener(this);
				cuadrosDim2Lim[i].addKeyListener(this);
				cuadrosDim1Lim[i].setHorizontalAlignment(JTextField.RIGHT);
				cuadrosDim2Lim[i].setHorizontalAlignment(JTextField.RIGHT);
				
				cuadrosDim1Lim[i].setToolTipText(Texto.get("CGF_NUMELEM1DIM", Conf.idioma));
				cuadrosDim2Lim[i].setToolTipText(Texto.get("CGF_NUMELEM2DIM", Conf.idioma));
				
				orden[i]=new JRadioButton[3];
				
				if (!this.metodo.getTipoParametro(i).contains("String") && !this.metodo.getTipoParametro(i).contains("boolean"))
					panelInterior.add(creaPanelOrden(orden[i]));						// añadimos celda vacía
				else
					panelInterior.add(creaPanelVacio());
				panelInterior.add(creaPanelDim(1,cuadrosDim1Lim[i]));		// celda para insertar límite de tamaño de dimensión 1
				if (this.metodo.getDimParametro(i)>1)
					panelInterior.add(creaPanelDim(2,cuadrosDim2Lim[i]));	// celda para insertar límite de tamaño de dimensión 2
				else
					panelInterior.add(creaPanelVacio());					// añadimos celda vacía
			}
			panelInterior.add(new JSeparator(SwingConstants.HORIZONTAL));
			panelInterior.add(new JSeparator(SwingConstants.HORIZONTAL));
			panelInterior.add(new JSeparator(SwingConstants.HORIZONTAL));
			panelInterior.addKeyListener(this);
		}
		
		panelMedio.add(panelInterior);

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(aceptar);
		panelBoton.add(cancelar);
		
		aceptar.addMouseListener(this);
		cancelar.addMouseListener(this);
		aceptar.addKeyListener(this);
		cancelar.addKeyListener(this);
	
		// Panel general
		BorderLayout bl= new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);
	
		panel.add(panelSup,BorderLayout.NORTH);
		panel.add(panelMedio,BorderLayout.CENTER);	//jsp
		panel.add(panelBoton,BorderLayout.SOUTH);
		panel.addKeyListener(this);
			
		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("CGF_TITULO",Conf.idioma));

		if (Ventana.thisventana.msWindows)
		{
			this.dialogo.setSize(anchoCuadro,(numFilas*20)+altoCuadro);
			//this.dialogo.setSize(anchoCuadro,altoCuadro);
			int coord[]=Conf.ubicarCentro(anchoCuadro,(numFilas*20)+altoCuadro);
			dialogo.setLocation(coord[0],coord[1]);
		}
		else
		{
			this.dialogo.setSize(anchoCuadroNW,altoCuadroNW);
			int coord[]=Conf.ubicarCentro(anchoCuadroNW,altoCuadroNW);
			dialogo.setLocation(coord[0],coord[1]);
		}
		this.dialogo.setResizable(false);			
		
		//setValores();
		this.dialogo.setVisible(true);		
	}

	
	private int numArrays(int dimensiones[])
	{
		int x=0;
		
		for (int i=0; i<dimensiones.length; i++)
			if (dimensiones[i]>0)
				x++;
		return x;
	}
	
	
	private JPanel creaPanelNombre(JCheckBox jcb)
	{
		JPanel panel = new JPanel ();
		BorderLayout bl = new BorderLayout();
		panel.setLayout(bl);
		panel.add(jcb,BorderLayout.WEST);
		if (Ventana.thisventana.msWindows)
			panel.setPreferredSize(new Dimension(182,ALTO_CELDAS));
		else
			panel.setPreferredSize(new Dimension(236,ALTO_CELDAS));
		panel.addKeyListener(this);
		return panel;
	}
	
	private JPanel creaPanelLim(String tipo, JTextField jtf)
	{
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel ();
		panel.setLayout(bl);
		
		JLabel etiq=new JLabel("  "+tipo);
		
		panel.add(etiq,BorderLayout.CENTER);
		panel.add(jtf,BorderLayout.EAST);
		panel.setPreferredSize(new Dimension(182,ALTO_CELDAS));
		panel.addKeyListener(this);
		return panel;
	}
	
	private JPanel creaPanelDim(int dim, JTextField jtf)
	{
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel ();
		panel.setLayout(bl);
		
		JLabel etiq=new JLabel("  "+Texto.get("CGF_TAMDIM",Conf.idioma)+" "+dim+":");
	
		panel.add(etiq,BorderLayout.CENTER);
		panel.add(jtf,BorderLayout.EAST);
		panel.setPreferredSize(new Dimension(182,ALTO_CELDAS));
		panel.addKeyListener(this);
		return panel;
	}
	
	private JPanel creaPanelVacio()
	{
		JPanel panel= new JPanel();
		panel.addKeyListener(this);
		return panel;
	}
	
	private JPanel creaPanelOrden(JRadioButton botones[])
	{
		JPanel panel= new JPanel();
		BorderLayout bl = new BorderLayout();
		panel.setLayout(bl);
		
		JPanel panelBotones=new JPanel();
		//GridLayout gl = new GridLayout(1,3);
		//panelBotones.setLayout(gl);
		panelBotones.setLayout(new BorderLayout());
		botones[0]=new JRadioButton(Texto.get("CGF_SINORDEN",Conf.idioma));
		botones[1]=new JRadioButton(Texto.get("CGF_ORDENCREC",Conf.idioma));
		botones[2]=new JRadioButton(Texto.get("CGF_ORDENDECREC",Conf.idioma));
		
		botones[0].setToolTipText(Texto.get("CGF_SINORDEN_TTT",Conf.idioma));
		botones[1].setToolTipText(Texto.get("CGF_ORDENCREC_TTT",Conf.idioma));
		botones[2].setToolTipText(Texto.get("CGF_ORDENDECREC_TTT",Conf.idioma));
		
		for (int i=0; i<botones.length; i++)
			botones[i].addActionListener(this);
		
		botones[0].setSelected(true);
		
		ButtonGroup bg=new ButtonGroup();
		bg.add(botones[0]);
		bg.add(botones[1]);
		bg.add(botones[2]);
		panelBotones.add(botones[0],BorderLayout.WEST);
		panelBotones.add(botones[1],BorderLayout.CENTER);
		panelBotones.add(botones[2],BorderLayout.EAST);
		
		panel.add(panelBotones,BorderLayout.EAST);
		if (Ventana.thisventana.msWindows)
			panel.setPreferredSize(new Dimension(182,ALTO_CELDAS-10));
		else
			panel.setPreferredSize(new Dimension(236,ALTO_CELDAS-10));
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel.addKeyListener(this);
		return panel;
	}
	

	
	/**
		.....................
		
		@param i númeero de parámetro
	*/	
	private void peticion(int i)
	{
		// Manejar excepciones al leer y corregir límites por defecto en función del otro límite si existe
		boolean insertadoMin=cuadrosValLimInf[i].getText().length()>0;
		boolean insertadoSup=cuadrosValLimSup[i].getText().length()>0;
		
		
		
		this.dialogo.setTitle(Texto.get("CGF_TITULO_GEN",Conf.idioma)+(i+1)+")");
		GeneradorParam gp=new GeneradorParam();
		String c=this.metodo.getTipoParametro(i)+ServiciosString.cadenaDimensiones(this.metodo.getDimParametro(i));

		if (c.contains("int") || c.contains("short"))
		{
			int limInf=-100, limSup=100;
			try {
				if (cuadrosValLimInf[i].getText().length()>0)
					limInf=Integer.parseInt(cuadrosValLimInf[i].getText());
				// else limInf=-2147483647;
				if (cuadrosValLimSup[i].getText().length()>0)
					limSup=Integer.parseInt(cuadrosValLimSup[i].getText());
				// else limSup=2147483647;
	
					
				if (!c.contains("[]"))
					gp=new GeneradorParam(limInf,limSup);
				else if (c.contains("[][]"))
				{
					int dim[]=extraerLimitesDimensiones(i);
					gp=new GeneradorParam(limInf,limSup, dim[0], dim[1], getNumOrden(i));
				}
				else if (c.contains("[]"))
					gp=new GeneradorParam(limInf,limSup, extraerLimiteDimension(i), getNumOrden(i));
			} catch (Exception exc) {

			}
			if (limInf>=limSup && ((insertadoMin && !insertadoSup) || (!insertadoMin && insertadoSup))   )
			{
				if (insertadoMin)
					limSup=limInf+100;
				else
					limInf=limSup-100;
			}
		}
		else if (c.contains("byte"))
		{
			int limInf=-128, limSup=127;
			try {
				//System.out.println("peticion int");

				
				if (cuadrosValLimInf[i].getText().length()>0)
					limInf=Integer.parseInt(cuadrosValLimInf[i].getText());

				if (cuadrosValLimSup[i].getText().length()>0)
					limSup=Integer.parseInt(cuadrosValLimSup[i].getText());

				if (!c.contains("[]"))
					gp=new GeneradorParam(limInf,limSup);
				else if (c.contains("[][]"))
				{
					int dim[]=extraerLimitesDimensiones(i);
					gp=new GeneradorParam(limInf,limSup, dim[0], dim[1], getNumOrden(i));
				}
				else if (c.contains("[]"))
					gp=new GeneradorParam(limInf,limSup, extraerLimiteDimension(i), getNumOrden(i));
			} catch (Exception exc) {

			}
		}
		else if (c.contains("long"))
		{
			long limInf=-10000, limSup=10000;
			try {

				if (cuadrosValLimInf[i].getText().length()>0)
					limInf=Long.parseLong(cuadrosValLimInf[i].getText());
				//else limInf=-2147483647;

				if (cuadrosValLimSup[i].getText().length()>0)
					limSup=Long.parseLong(cuadrosValLimSup[i].getText());
				//else limSup=(long)Math.pow(2,64)-5;//2147483647;
					
				if (!c.contains("[]"))
					gp=new GeneradorParam(limInf,limSup);
				else if (c.contains("[][]"))
				{
					int dim[]=extraerLimitesDimensiones(i);
					gp=new GeneradorParam(limInf,limSup, dim[0], dim[1], getNumOrden(i));
				}
				else if (c.contains("[]"))
					gp=new GeneradorParam(limInf,limSup, extraerLimiteDimension(i), getNumOrden(i));
			} catch (Exception exc) {

			}
			if (limInf>=limSup && ((insertadoMin && !insertadoSup) || (!insertadoMin && insertadoSup))   )
			{
				if (insertadoMin)
					limSup=limInf+1000;
				else
					limInf=limSup-1000;
			}
		}
		else if (c.contains("float") || c.contains("double"))
		{
			double limInf=-100.0, limSup=100.0;
			try {
			
				if (cuadrosValLimInf[i].getText().length()>0)
					limInf=Double.parseDouble(cuadrosValLimInf[i].getText());
				// else limInf=((long)Math.pow(2,64)+5)*(-1);//-2147483647;

				if (cuadrosValLimSup[i].getText().length()>0)
					limSup=Double.parseDouble(cuadrosValLimSup[i].getText());
				// else limSup=(long)Math.pow(2,64)-5;//2147483647;
				
				if (!c.contains("[]"))
					gp=new GeneradorParam(limInf,limSup);
				else if (c.contains("[][]"))
				{
					int dim[]=extraerLimitesDimensiones(i);
					gp=new GeneradorParam(limInf,limSup, dim[0], dim[1], getNumOrden(i));
				}
				else if (c.contains("[]"))
					gp=new GeneradorParam(limInf,limSup, extraerLimiteDimension(i), getNumOrden(i));
			} catch (Exception exc) {

			}
			if (limInf>=limSup && ((insertadoMin && !insertadoSup) || (!insertadoMin && insertadoSup))   )
			{
				if (insertadoMin)
					limSup=limInf+1000.0;
				else
					limInf=limSup-1000.0;
			}
		}
		else if (c.contains("char"))
		{
			try {
				//System.out.println("peticion char");
				if (!c.contains("[]"))
					gp=new GeneradorParam(new Character('c'));
				else if (c.contains("[][]"))
				{
					int dim[]=extraerLimitesDimensiones(i);
					gp=new GeneradorParam(new Character('c'), dim[0], dim[1], getNumOrden(i));
				}
				else if (c.contains("[]"))
					gp=new GeneradorParam(new Character('c'), extraerLimiteDimension(i), getNumOrden(i));
			} catch (Exception exc) {

			}
		}
		else if (c.contains("boolean"))
		{
			try {
				//System.out.println("peticion boolean");
				if (!c.contains("[]"))
					gp=new GeneradorParam(new Boolean(true));
				else if (c.contains("[][]"))
				{
					int dim[]=extraerLimitesDimensiones(i);
					gp=new GeneradorParam(new Boolean(true), dim[0], dim[1]);
				}
				else if (c.contains("[]"))
					gp=new GeneradorParam(new Boolean(true), extraerLimiteDimension(i));
			} catch (Exception exc) {

			}
		}
		else if (c.contains("String"))
		{
			try {
				if (!c.contains("[]"))
					gp=new GeneradorParam(new String(""));
				else if (c.contains("[][]"))
				{
					int dim[]=extraerLimitesDimensiones(i);
					gp=new GeneradorParam(new String(""), dim[0], dim[1]);
				}
				else if (c.contains("[]"))
					gp=new GeneradorParam(new String(""), extraerLimiteDimension(i));
			} catch (Exception exc) {

			}
		}

		String valor=gp.getValor();
		
		if (Conf.fichero_log) Logger.log_write("Valor aleatorio de "+i+"º parámetro: "+valor);
		
		if (this.cpOcple)
			this.cp.setValor( valor , i);
		else
			this.cple.setValor( valor , i);
		this.dialogo.setTitle(Texto.get("CGF_TITULO",Conf.idioma));
	}
	
	
	private int[] extraerLimitesDimensiones(int i)
	{
		GeneradorParam gp2;
		int limDim1=16;
		int limDim2=16;
		
		if (cuadrosDim1Lim[i].getText().length()>0)
			limDim1=Integer.parseInt(cuadrosDim1Lim[i].getText());
		else
		{
			gp2=new GeneradorParam(1,limDim1);
			limDim1=gp2.getValorInt();
		}
		
		if (cuadrosDim2Lim[i].getText().length()>0)
			limDim2=Integer.parseInt(cuadrosDim2Lim[i].getText());
		else
		{
			gp2=new GeneradorParam(1,limDim2);
			limDim2=gp2.getValorInt();
		}
		
		int x[]=new int[2];
		x[0]=limDim1;
		x[1]=limDim2;
		return x;
	}
	
	
	private int extraerLimiteDimension(int i)
	{
		GeneradorParam gp2;
		int limDim=16;
		
		if (cuadrosDim1Lim[i].getText().length()>0)
			limDim=Integer.parseInt(cuadrosDim1Lim[i].getText());
		else
		{
			gp2=new GeneradorParam(1,limDim);
			limDim=gp2.getValorInt();
		}
		return limDim;
	}
	
	
	
	/**
		Comprueba que todos los valores escritos en los cuadros de texto sean válidos (números o vacíos)
		
		@return true si todos los valores son válidos (bien porque son números, bien porque están vacíos)
	*/
	private boolean comprobarValores()
	{
		for (int i=0; i<marcadores.length; i++)
			if (marcadores[i].isSelected())
			{
				// Comprobamos escritura correcta
				if (!valorValido(cuadrosValLimInf[i],this.metodo.getTipoParametro(i)) || !valorValido(cuadrosValLimSup[i],this.metodo.getTipoParametro(i)))
					return false;
				if (this.metodo.getTipoParametro(i).contains("[]")  &&  !valorValido(cuadrosDim1Lim[i]))
					return false;
				if (this.metodo.getTipoParametro(i).contains("[]") && cuadrosDim1Lim[i].getText().length()>0 &&
					Integer.parseInt(cuadrosDim1Lim[i].getText())>MAX_DIM_ARRAY)
					return false;
				if (this.metodo.getTipoParametro(i).contains("[][]") && !valorValido(cuadrosDim2Lim[i]))	
					return false;
				if (this.metodo.getTipoParametro(i).contains("[][]") && cuadrosDim2Lim[i].getText().length()>0 &&
					Integer.parseInt(cuadrosDim2Lim[i].getText())>MAX_DIM_ARRAY)
					return false;
					
				// Comprobamos que límite inferior sea menor que límite superior
				if (cuadrosValLimInf[i].getText().length()>0 && cuadrosValLimSup[i].getText().length()>0)
				{
					if (this.metodo.getTipoParametro(i).contains("int") || this.metodo.getTipoParametro(i).contains("short")
						|| this.metodo.getTipoParametro(i).contains("byte"))
					{
						int min = Integer.parseInt(cuadrosValLimInf[i].getText());
						int max = Integer.parseInt(cuadrosValLimSup[i].getText());
						if (max<=min)
							return false;
					}
					else if (this.metodo.getTipoParametro(i).contains("long"))
					{
						long min = Long.parseLong(cuadrosValLimInf[i].getText());
						long max = Long.parseLong(cuadrosValLimSup[i].getText());
						if (max<=min)
							return false;
					}
					else if (this.metodo.getTipoParametro(i).contains("float") || this.metodo.getTipoParametro(i).contains("double"))
					{
						double min = Double.parseDouble(cuadrosValLimInf[i].getText());
						double max = Double.parseDouble(cuadrosValLimSup[i].getText());
						//System.out.println("MIN = "+min+"\nMAX = "+max+"\nmax<=min = "+(max<=min));
						if (max<=min)
							return false;
					}
				}
			}
		return true;
	}
	
	
	/**
		Comprueba que el valor escrito en un JTextField para dimensiones es un valor apto (número entero)
		
		@return true si el valor es válido
	*/
	private boolean valorValido(JTextField jtf)
	{
		String texto=jtf.getText();
		
		if (texto.length()==0)
			return true;
		//System.out.println("texto : "+texto);
		for (int i=0; i<texto.length(); i++)
			if (!(ServiciosString.esNumero(texto.charAt(i))) )
				return false;
		if (texto.length()==1 && ServiciosString.esNumero(texto.charAt(0)) && texto.charAt(0)=='0')
			return false;	// Debemos introducir un valor mayor que cero
		return true;
	}
	
	/**
		Comprueba que el valor escrito en un JTextField para valores límite es un valor apto (número entero)
		
		@return true si el valor es válido
	*/
	private boolean valorValido(JTextField jtf, String clase)
	{
		String texto=jtf.getText();
		
		if (texto.length()==0)
			return true;
		
		if (texto.length()==1 && texto.charAt(0)=='-')
			return false;

		if (clase.contains("int") || 	clase.contains("short")
			|| clase.contains("byte") || clase.contains("long"))
		{
			//System.out.println("int - short - byte - long :"+clase.getCanonicalName());
			for (int i=0; i<texto.length(); i++)
			{
				//System.out.println("\t["+i+"]="+texto.charAt(i));
				if (!(ServiciosString.esNumero(texto.charAt(i))) && !(texto.charAt(i)=='-' && i==0))
					return false;
			}
		}
		else if (clase.contains("float") || 	clase.contains("double"))
		{
			//System.out.println("float - double :"+clase.getCanonicalName()+" texto:"+texto);
			int numPuntos=0;
			int x=0;
			if (texto.charAt(0)=='-')
				x=1;
			if (texto.charAt(0)=='-' && texto.length()<2)
				return false;
			for (int i=x; i<texto.length(); i++)
			{
				if (texto.charAt(i)=='.')
					numPuntos++;
				if ((!(ServiciosString.esNumero(texto.charAt(i))) && !(texto.charAt(i)=='.')) || numPuntos>1)
					return false;
			}
		}
		return true;
	}
	
	
	
	private int getNumOrden(int x)
	{
		for (int i=0; i<orden[x].length; i++)
			if (orden[x][i].isSelected())
				return i;
		return -1;
	}

	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==marcadorTodos && marcadorTodos.isSelected())
		{
			for (int i=0; i<marcadores.length; i++)
			{
				marcadores[i].setSelected(true);
			}
			new Thread() {
				public synchronized void run()
				{
					try { wait (100); } catch (InterruptedException ie) {System.out.println("Error de delay");};
					if (comprobarValores())
						aceptar.setEnabled(true);
					else 
						aceptar.setEnabled(false);
				}
			}.start();
		}
		else if (e.getSource().getClass().getCanonicalName().contains("JCheckBox"))
		{
			for (int i=0; i<marcadores.length; i++)
			{
				if (e.getSource()==marcadores[i] && !(marcadores[i].isSelected()))
					marcadorTodos.setSelected(false);
				else if (e.getSource()==marcadores[i] && (marcadores[i].isSelected()))
				{
					boolean marcarMarcadorTodos=true;
					int j=0;
					do
					{
						marcarMarcadorTodos=marcadores[j].isSelected();
						j++;
					}
					while(j<marcadores.length && marcarMarcadorTodos==true);
					marcadorTodos.setSelected(marcarMarcadorTodos);	
				}
			}
			new Thread() {
				public synchronized void run()
				{
					try { wait (10); } catch (InterruptedException ie) {System.out.println("Error de delay");};
					if (comprobarValores())
						aceptar.setEnabled(true);
					else 
						aceptar.setEnabled(false);
				}
			}.start();
		}
		else if (e.getSource().getClass().getCanonicalName().contains("JRadioButton"))
		{
			
			int x=0;
			for (int i=0; i<orden.length; i++)
				if (orden[i]!=null)
					for (int j=0; j<orden[i].length; j++)
						if (e.getSource()==orden[i][j])
							x=i;
			marcadores[x].setSelected(true);
			boolean marcarMarcadorTodos=true;
			int j=0;
			do
			{
				marcarMarcadorTodos=marcadores[j].isSelected();
				j++;
			}
			while(j<marcadores.length && marcarMarcadorTodos==true);
			marcadorTodos.setSelected(marcarMarcadorTodos);	
		}
		
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/		
	public void keyPressed(KeyEvent e)
	{
		int code=e.getKeyCode();
		
		
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyReleased(KeyEvent e)
	{
		int code=e.getKeyCode();
		//System.out.println(code+" Released");
		if (code==KeyEvent.VK_ESCAPE)
			dialogo.setVisible(false);
		else if (code==KeyEvent.VK_ENTER)
		{
			if(comprobarValores())
			{
				for (int i=0; i<this.marcadores.length; i++)
					if (marcadores[i].isSelected())
						peticion(i);
				this.dialogo.setVisible(false);
				//Conf.setValoresOpsVisualizacion();
			}
			else
				new CuadroError(this.dialogo,Texto.get("ERROR_VAL",Conf.idioma),Texto.get("ERROR_VALESCR",Conf.idioma));
		}
		//else
			//System.out.println(code+" Released");
	}

	
	
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyTyped(KeyEvent e)
	{
		int code=e.getKeyCode();

		//System.out.println(code+" Typed");
		//System.out.println("");
		for (int i=0; i<this.metodo.getNumeroParametros(); i++)
		{
			if (e.getSource()==cuadrosValLimInf[i] || e.getSource()==cuadrosValLimSup[i] ||
				e.getSource()==cuadrosDim1Lim[i] || e.getSource()==cuadrosDim2Lim[i])
				marcadores[i].setSelected(true);
		}
		boolean marcarMarcadorTodos=true;
		int j=0;
		do
		{
			marcarMarcadorTodos=marcadores[j].isSelected();
			j++;
		}
		while(j<marcadores.length && marcarMarcadorTodos==true);
		marcadorTodos.setSelected(marcarMarcadorTodos);	
		
		new Thread() {
			public synchronized void run()
			{
				try { wait (10); } catch (InterruptedException ie) {System.out.println("Error de delay");};
				if (comprobarValores())
					aceptar.setEnabled(true);
				else 
					aceptar.setEnabled(false);
			}
		}.start();
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
		if (comprobarValores())
			aceptar.setEnabled(true);
		else
			aceptar.setEnabled(false);
		cancelar.setEnabled(true);
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mousePressed(MouseEvent e) 
	{
		//habilitarDeshabilitar();
		if (e.getSource()==aceptar)
		{
			if(comprobarValores())
			{
				for (int i=0; i<this.marcadores.length; i++)
					if (marcadores[i].isSelected())
						peticion(i);
				this.dialogo.setVisible(false);
				//Conf.setValoresOpsVisualizacion();
			}
			else
				new CuadroError(this.dialogo,Texto.get("ERROR_VAL",Conf.idioma),Texto.get("ERROR_VALESCR",Conf.idioma));
		}
		else if (e.getSource()==cancelar)
			this.dialogo.setVisible(false);
		
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseReleased(MouseEvent e)
	{
	}
	
	public void setVisible(boolean b)
	{
		this.dialogo.setVisible(b);
		this.dialogo.setTitle(Texto.get("CGF_TITULO",Conf.idioma));
		//System.out.println("setVisible");
	}
}

