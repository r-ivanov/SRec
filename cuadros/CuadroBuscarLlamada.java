/**
	Implementa el cuadro que recoge los parámetros desde la interfaz
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;




import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import java.util.ArrayList;

import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import conf.*;
import botones.*;
import datos.*;
import utilidades.*;
import utilidades.*;
import ventanas.*;

public class CuadroBuscarLlamada extends Thread implements ActionListener, KeyListener
{
	static final long serialVersionUID=02;

	static final boolean depurar=false;

	final int anchoCuadro=460;
	final int altoCuadro=215;		// Base inicial
	final int alturaPiso=23;

	CuadroGenerarAleatorio cga=null;
	
	JLabel[] etiquetas;
	JComboBox selectorMetodo;
	JCheckBox[] mostrarvalores;
	
	BotonAceptar aceptar;
	BotonCancelar cancelar;
	BotonTexto restaurar;

	
	JPanel panel, panelBoton, panelSelecMetodo, panelContenedorValores;
	int numero;
	boolean editarValores;
	
	MetodoAlgoritmo metodo;
	ClaseAlgoritmo clase;
	
	//String [] valores;
	DatosTrazaBasicos dtb;
	//JTextField campos[];
	JComboBox campos[];

	String codigounico;
	
	JFrame vv;
	Preprocesador p;
	JDialog dialogo;
	CuadroMetodosProcesados cmp;
	AlmacenValores almacenValores=new AlmacenValores();

	JTextComponent[] editor = null;
	
	int numMetodo;
	
	/**
		Constructor: crea un nuevo cuadro de parámetros
		
		@param metodo método que se ha seleccionado para modificar sus valores de entrada
		@param cmp Cuadro al que se devolverá el control de ejecución, padre de este cuadro
	*/
	public CuadroBuscarLlamada(DatosTrazaBasicos dtb)
	{	
		this.dialogo = new JDialog(Ventana.thisventana,true);
		this.dtb=dtb;

		this.start();
	}

	/**
		Crea el cuadro de parámetros
	*/
	public void run()
	{
		
		ValoresParametros.inicializar(false);
		
		if (dtb!=null)
		{
			ArrayList<DatosMetodoBasicos> dmb=dtb.getMetodos();

			
			//for (int i=0; i<dmb.size(); i++)
			//	System.out.println(dmb.get(i).getNombre()+" es visible: "+dmb.get(i).getEsVisible());
			
			// Panel Selección Método
			String[] nombresMetodos=new String[dmb.size()];
			numero=dmb.size();
			
			for (int i=0; i<numero; i++)
				nombresMetodos[i]=dmb.get(i).getInterfaz();
			
			selectorMetodo = new JComboBox();
			for (int i=0; i<numero; i++)
			{
				if (dmb.get(i).getEsVisible())
					selectorMetodo.addItem(nombresMetodos[i]);
			}
			selectorMetodo.addActionListener(this);
			selectorMetodo.addKeyListener(this);
			
			panelSelecMetodo=new JPanel();
			panelSelecMetodo.setLayout(new BorderLayout());
			panelSelecMetodo.add(selectorMetodo);
			panelSelecMetodo.setBorder(new TitledBorder(Texto.get("CUBSQ_SELMET",Conf.idioma)));
			
			// Panel Inserción valores
			panelContenedorValores=new JPanel();
			panelContenedorValores.setLayout(new BorderLayout());
			
			String interfazMetodo=dmb.get(0).getInterfaz();
			
			String[][] valoresParam=Ventana.thisventana.getTraza().getValoresParametros(interfazMetodo,true);
			String[][] valoresResult=Ventana.thisventana.getTraza().getValoresResultado(interfazMetodo,true);
			
			JPanel ppa=panelParametros(dmb.get(0),valoresParam,valoresResult);
			panelContenedorValores.add(ppa	,	BorderLayout.NORTH);

			JScrollPane jsp=new JScrollPane(panelContenedorValores);
			
			if (Conf.elementosVisualizar==Conf.VISUALIZAR_TODO)
				jsp.setBorder(new TitledBorder(Texto.get("CUBSQ_VALPAMRES",Conf.idioma)));
			else if (Conf.elementosVisualizar==Conf.VISUALIZAR_ENTRADA)
				jsp.setBorder(new TitledBorder(Texto.get("CUBSQ_VALPAM",Conf.idioma)));
			else if (Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA)
				jsp.setBorder(new TitledBorder(Texto.get("CUBSQ_VALRES",Conf.idioma)));
			
			// Botones
			
			// Botón Aceptar
			aceptar=new BotonAceptar();
			aceptar.addActionListener(this);
			aceptar.addKeyListener(this);
			//aceptar.addMouseListener(this);
			//aceptar.setRojo();
			
			// Botón Restaurar
			restaurar=new BotonTexto(Texto.get("PARB_RESTAURAR",Conf.idioma));
			restaurar.setPreferredSize(new Dimension(95,23));
			restaurar.addActionListener(this);
			restaurar.addKeyListener(this);
			
			
			// Botón Cancelar
			cancelar=new BotonCancelar();
			cancelar.addActionListener(this);
			cancelar.addKeyListener(this);
			//cancelar.addMouseListener(this);
	
			// Panel para el botón
			panelBoton = new JPanel();
			panelBoton.add(aceptar);
			panelBoton.add(restaurar);
			panelBoton.add(cancelar);
	
			
			
			// Panel general
			panel = new JPanel();
			panel.setLayout(new BorderLayout());
	
			panel.add(panelSelecMetodo,BorderLayout.NORTH);
			panel.add(jsp,BorderLayout.CENTER);
			panel.add(panelBoton,BorderLayout.SOUTH);
			
			dialogo.getContentPane().add(panel);
			dialogo.setTitle(Texto.get("CUBSQ_TITULO", Conf.idioma));
	
			// Preparamos y mostramos cuadro
			int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
			dialogo.setLocation(coord[0],coord[1]);
			dialogo.setSize(anchoCuadro,altoCuadro);
			dialogo.setResizable(false);
			dialogo.setVisible(true);
			
		}
	}


	/**
		Escribe en un JTextField el valor pasado como parámetro
		
		@param texto texto que escribirá en el JTextField correspondiente
		@param i número de JTextField en el que se debe introducir el texto pasado como parámetro
	*/
	public void setValor(String texto, int i)
	{
		
	}
	
	/**
		Permite extraer el JDialog asociado
		
		@return JDialog de CuadroParam
	*/
	public JDialog getJDialog()
	{
		return this.dialogo;
	}
	
	

	
	
	private int numMetodoSeleccionado()
	{
		int numMetodo=selectorMetodo.getSelectedIndex();
		//System.out.println("actionPerformed: numMetodo="+numMetodo);
		
		int aux=0,posic=0;
		
		for (int i=0; i<this.dtb.getNumMetodos(); i++)
		{

			//System.out.println("  i="+i+" ["+this.dtb.getMetodo(i).getInterfaz()+"]");
			if (aux==numMetodo)
				posic=i;
			
			if (this.dtb.getMetodo(i).getEsVisible())
				aux++;
		}
		return posic;
	}
	
	
	
		
	
	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==selectorMetodo)
		{
			
			
		
			int numMetodo=numMetodoSeleccionado();
			
			String valoresE[][]=Ventana.thisventana.getTraza().getValoresParametros(this.dtb.getMetodo(numMetodo).getInterfaz(),true);
			String valoresS[][]=Ventana.thisventana.getTraza().getValoresResultado(this.dtb.getMetodo(numMetodo).getInterfaz(),true);
			
			JPanel panelMetodo=panelParametros(this.dtb.getMetodo(numMetodo),valoresE,valoresS);

			
			panelContenedorValores.removeAll();
			panelContenedorValores.add( panelMetodo , BorderLayout.NORTH);
			panelContenedorValores.updateUI();
		}
		
		
		if (e.getSource()==aceptar)
			accion();
		else if (e.getSource()==restaurar)
		{
			
			Ventana.thisventana.getTraza().iluminar(numMetodo,null,null,false);
			Ventana.thisventana.refrescarFormato();
			dialogo.setVisible(false);
			
		}
		else if (e.getSource()==cancelar)
			dialogo.setVisible(false);
	}
	
	
	
	
	public void accion()
	{
		if (valoresCorrectos())
		{
			
			activarBusqueda();
			dialogo.setVisible(false);
		}
		else
			new CuadroError(Ventana.thisventana,Texto.get("ERROR_PARAM",Conf.idioma),Texto.get("ERROR_PARAMINCSIMPLE",Conf.idioma));
	}
	
	
	
	
	
	public boolean valoresCorrectos()
	{
		int numMetodo=numMetodoSeleccionado();
		
		String []tiposE=this.dtb.getMetodo(numMetodo).getTipoParametrosE();
		String []tiposS=this.dtb.getMetodo(numMetodo).getTipoParametrosS();
		int []dimE=this.dtb.getMetodo(numMetodo).getDimE();
		int []dimS=this.dtb.getMetodo(numMetodo).getDimS();
		
		String []tipos=new String[tiposE.length+tiposS.length];
		int []dim=new int[dimE.length+dimS.length];
	
		for (int i=0; i<tiposE.length; i++)
		{
			tipos[i]=tiposE[i];
			dim[i]=dimE[i];
		}
		
		for (int i=0; i<tiposS.length; i++)
		{
			tipos[i+tiposE.length]=tiposS[i];
			dim[i+dimE.length]=dimS[i];
		}
		
		
		
		for (int i=0; i<campos.length; i++)
		{
			if (this.campos[i]!=null && this.campos[i].getSelectedItem().toString().length()>0)
				if (!ServiciosString.esDeTipoCorrecto(this.campos[i].getSelectedItem().toString(), tipos[i], dim[i]))
					return false;
		}
		return true;
	}
	
	
	public void activarBusqueda()
	{
		int numMetodo=numMetodoSeleccionado();
		DatosMetodoBasicos dmb=this.dtb.getMetodo(numMetodo); 
		
		int numE=dmb.getNumParametrosE();
		int numS=dmb.getNumParametrosS();
		
		String []valoresE=new String[numE];
		String []tiposE=new String[numE];
		int []dimE=new int[numE];
		
		String []valoresS=new String[numS];
		String []tiposS=new String[numS];
		int []dimS=new int[numS];
		
		int posic=0;
		
		if (Conf.elementosVisualizar==Conf.VISUALIZAR_TODO || Conf.elementosVisualizar==Conf.VISUALIZAR_ENTRADA)
		{
			for (int i=0; i<dmb.getNumParametrosE(); i++)
			{
				if (dmb.getVisibilidadE(i))
				{
					valoresE[i]=this.campos[posic].getSelectedItem().toString();
					tiposE[i]=dmb.getTipoParametroE(i);
					dimE[i]=dmb.getDimParametroE(i);
					posic++;
				}
			}
		}
		
		if (Conf.elementosVisualizar==Conf.VISUALIZAR_TODO || Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA)
		{
			for (int i=0; i<dmb.getNumParametrosS(); i++)
			{
				if (dmb.getVisibilidadS(i))
				{
					valoresS[i]=this.campos[posic].getSelectedItem().toString();
					tiposS[i]=dmb.getTipoParametroS(i);
					dimS[i]=dmb.getDimParametroS(i);
					posic++;
				}
			}
		}
		
		
		

		for (int i=0; i<valoresE.length; i++)
			if (ServiciosString.tieneContenido(valoresE[i]))
				valoresE[i]=ServiciosString.adecuarParametro(valoresE[i],tiposE[i],dimE[i]);
		
		for (int i=0; i<valoresS.length; i++)
			if (ServiciosString.tieneContenido(valoresS[i]))
				valoresS[i]=ServiciosString.adecuarParametro(valoresS[i],tiposS[i],dimS[i]);
		
		if (Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA)
			valoresE=null;
		else if (Conf.elementosVisualizar==Conf.VISUALIZAR_ENTRADA)
			valoresS=null;
		
		
		
		
		if (Conf.fichero_log)
		{
			DatosMetodoBasicos ma=this.dtb.getMetodo(numMetodoSeleccionado());
			String mensaje="Información > Buscar llamada...: "+ma.getNombre()+"(";
			
			for (int i=0; i<valoresE.length; i++)
			{
				if (i!=0)
					mensaje+=",";
				mensaje+=valoresE[i];
			}
			mensaje+=") [";
			for (int i=0; i<valoresS.length; i++)
			{
				if (i!=0)
					mensaje+=",";
				mensaje+=valoresS[i];
			}
			mensaje+="]";
			Logger.log_write(mensaje);
		}
		
		Ventana.thisventana.getTraza().iluminar(numMetodoSeleccionado(),valoresE,valoresS,true);
		Ventana.thisventana.refrescarFormato();
	}

	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/
	public void keyPressed(KeyEvent e)
	{
		int code=e.getKeyCode();
		
		if (code!=KeyEvent.VK_ENTER && e.getSource()==selectorMetodo)
		{
			int numMetodo=numMetodoSeleccionado();

			/*for (int i=0; i<this.dtb.getNumMetodos(); i++)
			{
				System.out.println("  i="+i+" ["+this.dtb.getMetodo(i).getInterfaz()+"]");
			}*/
			
			String valoresE[][]=Ventana.thisventana.getTraza().getValoresParametros(this.dtb.getMetodo(numMetodo).getInterfaz(),true);
			String valoresS[][]=Ventana.thisventana.getTraza().getValoresResultado(this.dtb.getMetodo(numMetodo).getInterfaz(),true);
			
			JPanel panelMetodo=panelParametros(this.dtb.getMetodo(numMetodo),valoresE,valoresS);
			
			panelContenedorValores.removeAll();
			panelContenedorValores.add( panelMetodo , BorderLayout.NORTH);
			panelContenedorValores.updateUI();
		}
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/	
	public void keyReleased(KeyEvent e)
	{
		int code=e.getKeyCode();
		if (code==KeyEvent.VK_ENTER)
			if (e.getSource().getClass().getName().contains("JTextField") || e.getSource().getClass().getName().contains("BorderlessTextField"))
				accion();
			else if (e.getSource().getClass().getName().contains("JComboBox"))
				campos[0].requestFocus();
	}

	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/
	public void keyTyped(KeyEvent e)
	{

	}

		
	private JPanel panelParametros(DatosMetodoBasicos metodo, String[][] valoresE, String[][] valoresS)
	{
		
		int numParamE=metodo.getNumParametrosE();
		int numParam=0;
		
		
		if (Conf.elementosVisualizar==Conf.VISUALIZAR_TODO)
		{
			numParam=numParamE+( metodo.getTipo().equals("void") ? metodo.getNumParametrosE() : 1);
			
			
			
			if (numParam==(numParamE*2))	// Si no es método void, recorremos visiblidad de "parámetros de salida"
				for (int i=0; i<numParamE; i++)
					if (!metodo.getVisibilidadS(i))
						numParam--;
			
			for (int i=0; i<numParamE; i++)
				if (!metodo.getVisibilidadE(i))
					numParam--;
		}
		else if (Conf.elementosVisualizar==Conf.VISUALIZAR_ENTRADA)
		{
			numParam=numParamE;
			
			for (int i=0; i<numParamE; i++)
				if (!metodo.getVisibilidadE(i))
					numParam--;
		}
		else if (Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA)
		{
			numParam=( metodo.getTipo().equals("void") ? metodo.getNumParametrosE() : 1);
			
			if (numParam==(numParamE*2))	// Si no es método void, recorremos visiblidad de "parámetros de salida"
				for (int i=0; i<numParamE; i++)
					if (!metodo.getVisibilidadS(i))
						numParam--;
		}
		
		
		JPanel panelValoresParam=new JPanel();
		panelValoresParam.setLayout(new BorderLayout());
		
		JPanel panelValoresParamI=new JPanel();
		JPanel panelValoresParamD=new JPanel();
		panelValoresParamI.setLayout(new GridLayout(numParam,1));
		panelValoresParamD.setLayout(new GridLayout(numParam,1));
		
		//campos=new JTextField[numParam];
		campos=new JComboBox[numParam];
		editor=new JTextComponent[numParam];
		
		String textoEntrada=Texto.get("ETIQFL_ENTR",Conf.idioma);
		String textoSalida=Texto.get("ETIQFL_SALI",Conf.idioma);
		
		/*
		for (int i=0; i<metodo.getNumParametrosE(); i++)
			System.out.println("CuadroBuscarLlamada.panelParametros VISIBILIDAD E"+i+"= "+(metodo.getVisibilidadE(i)));
		
		for (int i=0; i<metodo.getNumParametrosS(); i++)
			System.out.println("CuadroBuscarLlamada.panelParametros VISIBILIDAD S"+i+"= "+(metodo.getVisibilidadS(i)));
		
		*/
		
		int posic=0;
		
		if (Conf.elementosVisualizar==Conf.VISUALIZAR_TODO || Conf.elementosVisualizar==Conf.VISUALIZAR_ENTRADA)
			for (int i=0; i<metodo.getNumParametrosE(); i++)
			{
				if (metodo.getVisibilidadE(i))
				{
					String textoEtiqueta=textoEntrada+": "+metodo.getTipoParametroE(i);
					if (metodo.getDimParametroE(i)>0)
						textoEtiqueta=textoEtiqueta+" "+ServiciosString.cadenaDimensiones(metodo.getDimParametroE(i));
				
					textoEtiqueta=textoEtiqueta+" "+metodo.getNombreParametroE(i)+"  ";
					
					JLabel etiqueta=new JLabel(textoEtiqueta);
					
					campos[posic]=new JComboBox();
					campos[posic].setEditable(true);
					campos[posic].setPreferredSize(new Dimension(250,20));
					
					campos[posic].addItem( "" );
					//ValoresParametros.introducirValores(campos[i],metodo.getTipoParametroE(i),metodo.getDimParametroE(i));
								
					for (int j=0; j<valoresE.length; j++)
						if (valoresE[j][i]!=null)
							campos[posic].addItem( valoresE[j][i] );
					
					editor[posic]=(JTextComponent)campos[posic].getEditor().getEditorComponent();
					editor[posic].addKeyListener(this);
					//campos[i].addKeyListener(this);
				
				
					panelValoresParamI.add(etiqueta);
					panelValoresParamD.add(campos[posic]);
					
					posic++;
				}
			}
		
		if (Conf.elementosVisualizar==Conf.VISUALIZAR_TODO || Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA)
		{
			if (metodo.getTipo().equals("void"))
			{
				if (Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA)
					numParamE=0;
				
				for (int i=0; i<metodo.getNumParametrosE(); i++)
				{
					if (metodo.getVisibilidadS(i))
					{
						String textoEtiqueta=textoSalida+": "+metodo.getTipoParametroS(i);
						if (metodo.getDimParametroS(i)>0)
							textoEtiqueta=textoEtiqueta+" "+ServiciosString.cadenaDimensiones(metodo.getDimParametroS(i));
					
						textoEtiqueta=textoEtiqueta+" "+metodo.getNombreParametroS(i)+"  ";
						
						JLabel etiqueta=new JLabel(textoEtiqueta);
						
						campos[posic]=new JComboBox();
						campos[posic].setEditable(true);
						campos[posic].setPreferredSize(new Dimension(250,20));
						
						campos[posic].addItem( "" );
						//ValoresParametros.introducirValores(campos[i],metodo.getTipoParametroE(i),metodo.getDimParametroE(i));
									
						for (int j=0; j<valoresS.length; j++)
							if (valoresS[j][i]!=null)
								campos[posic].addItem( valoresS[j][i] );
						
						editor[posic]=(JTextComponent)campos[posic].getEditor().getEditorComponent();
						editor[posic].addKeyListener(this);
						//campos[i].addKeyListener(this);
						
						
						panelValoresParamI.add(etiqueta);
						panelValoresParamD.add(campos[posic]);
						
						posic++;
					}
				}
			}
			else
			{
				String textoEtiqueta=textoSalida+": "+metodo.getTipo();
				if (metodo.getDimTipo()>0)
					textoEtiqueta=textoEtiqueta+" "+ServiciosString.cadenaDimensiones(metodo.getDimTipo());
			
				textoEtiqueta=textoEtiqueta+" "+Texto.get("NOMBRE_RETORNO",Conf.idioma);
				
				JLabel etiqueta=new JLabel(textoEtiqueta);
				
				campos[campos.length-1]=new JComboBox();
				campos[campos.length-1].setEditable(true);
				campos[campos.length-1].setPreferredSize(new Dimension(250,20));
				
				campos[campos.length-1].addItem( "" );
				//ValoresParametros.introducirValores(campos[i],metodo.getTipoParametroE(i),metodo.getDimParametroE(i));
							
				for (int j=0; j<valoresS.length; j++)
					if (valoresS[j][0]!=null)
						campos[campos.length-1].addItem( valoresS[j][0] );
				
				editor[campos.length-1]=(JTextComponent)campos[campos.length-1].getEditor().getEditorComponent();
				editor[campos.length-1].addKeyListener(this);
				//campos[i].addKeyListener(this);
				
				panelValoresParamI.add(etiqueta);
				panelValoresParamD.add(campos[campos.length-1]);	
			}
		}
		
		panelValoresParam.add(panelValoresParamI,BorderLayout.WEST);
		panelValoresParam.add(panelValoresParamD,BorderLayout.CENTER);
		
		return panelValoresParam;
	}
	
	
	


}
