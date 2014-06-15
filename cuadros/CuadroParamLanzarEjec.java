/**
	Implementa el cuadro que recoge los parámetros desde la interfaz
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;




import java.awt.BorderLayout;
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
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import conf.*;
import botones.*;
import datos.*;
import utilidades.*;
import ventanas.*;

public class CuadroParamLanzarEjec extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=02;

	static final boolean depurar=false;

	final int anchoCuadro=490;
	final int altoCuadro=92;		// Base inicial
	final int alturaPiso=23;

	CuadroGenerarAleatorio cga=null;
	
	JLabel[] etiquetas;
	JComboBox[] cuadrosvalores;
	JCheckBox[] mostrarvalores;
	
	BotonAceptar aceptar;
	BotonCancelar cancelar;
	BotonTexto generar;
	BotonTexto cargar;
	BotonTexto guardar;
	
	JPanel panel, panelBoton, panelParam;
	int numero;
	boolean editarValores;
	
	MetodoAlgoritmo metodo;
	ClaseAlgoritmo clase;
	
	String [] valores;

	

	boolean estamosCargando=false;


	String codigounico;
	
	JFrame vv;
	Preprocesador p;
	JDialog dialogo;
	CuadroMetodosProcesados cmp;
	AlmacenValores almacenValores=new AlmacenValores();
	boolean luzVerde=false;

	int numMetodo;
	
	/**
		Constructor: crea un nuevo cuadro de parámetros
		
		@param metodo método que se ha seleccionado para modificar sus valores de entrada
		@param cmp Cuadro al que se devolverá el control de ejecución, padre de este cuadro
	*/
	public CuadroParamLanzarEjec(MetodoAlgoritmo metodo, ClaseAlgoritmo clase, Preprocesador p)
	{	
		dialogo = new JDialog(Ventana.thisventana,true);
	
		ValoresParametros.inicializar(false);
		
	
		// Inicializamos atributos del cuadro de parámetros
		if (!metodo.getTipo().equals("void"))
			this.numero=metodo.getNumeroParametros();
		else
			this.numero=metodo.getNumeroParametros()*2;
		this.metodo=metodo;
		this.clase=clase;
		this.p=p;
		this.start();
	}

	/**
		Crea el cuadro de parámetros
	*/
	public void run()
	{
		this.editarValores = true;
		if (this.numero>0)
		{
			etiquetas = new JLabel[numero];
			cuadrosvalores = new JComboBox[numero];
			mostrarvalores = new JCheckBox[numero];
			
			// Tendremos n campos: n parámetros
			if (!metodo.getTipo().equals("void"))
			{
				for (int i=0; i<numero; i++)
				{
					etiquetas[i]=new JLabel();
					etiquetas[i].setText(Texto.get("ETIQFL_ENTR",Conf.idioma)+": "+metodo.getNombreParametro(i)+" ("+metodo.getTipoParametro(i)+ServiciosString.cadenaDimensiones(metodo.getDimParametro(i))+")");
					cuadrosvalores[i]=new JComboBox();	// Cambiar por caja con desplegable
					cuadrosvalores[i].setEditable(true);
					cuadrosvalores[i].setEnabled(editarValores);
					ValoresParametros.introducirValores(cuadrosvalores[i],metodo.getTipoParametro(i),metodo.getDimParametro(i));
					ValoresParametros.introducirValor(cuadrosvalores[i],metodo.getTipoParametro(i),metodo.getDimParametro(i),
							metodo.getParamValor(i),true);
					//cuadrosvalores[i].setText(metodo.getParamValor(i));
					//cuadrosvalores[i].setHorizontalAlignment(JTextField.CENTER);
					cuadrosvalores[i].addKeyListener(this);
					if (cuadrosvalores[i].isEnabled())
						cuadrosvalores[i].setToolTipText(Texto.get("CPARAM_ESCRVALADEC",Conf.idioma)+" "+metodo.getTipoParametro(i));
					mostrarvalores[i]=new JCheckBox(Texto.get("ETIQFL_ENTR",Conf.idioma)+": "+metodo.getNombreParametro(i)+" ("+metodo.getTipoParametro(i)+ServiciosString.cadenaDimensiones(metodo.getDimParametro(i))+")");
					mostrarvalores[i].setSelected(true);
					mostrarvalores[i].setToolTipText(Texto.get("CPARAM_CASILVIS",Conf.idioma));
					mostrarvalores[i].addKeyListener(this);
					mostrarvalores[i].addMouseListener(this);
				}
			}
			else		// Tendremos n*2 campos: n parámetros vistos a la entrada y n vistos a la salida
			{
				for (int i=0; i<numero; i++)
				{
					etiquetas[i]=new JLabel();
					cuadrosvalores[i]=new JComboBox();	// Cambiar por caja con desplegable
					mostrarvalores[i]=new JCheckBox();
					//cuadrosvalores[i].setHorizontalAlignment(JTextField.CENTER);
					cuadrosvalores[i].addKeyListener(this);
					if (i<(numero/2))
					{
						mostrarvalores[i].setText(Texto.get("ETIQFL_ENTR",Conf.idioma)+": "+metodo.getNombreParametro(i)+" ("+metodo.getTipoParametro(i)+ServiciosString.cadenaDimensiones(metodo.getDimParametro(i))+")");
						cuadrosvalores[i].setEditable(true);
						cuadrosvalores[i].setEnabled(editarValores);
						ValoresParametros.introducirValores(cuadrosvalores[i],metodo.getTipoParametro(i),metodo.getDimParametro(i));
						ValoresParametros.introducirValor(cuadrosvalores[i],metodo.getTipoParametro(i),metodo.getDimParametro(i),
								metodo.getParamValor(i),true);
						//cuadrosvalores[i].setText(metodo.getParamValor(i));
						if (cuadrosvalores[i].isEnabled())
							cuadrosvalores[i].setToolTipText(Texto.get("CPARAM_ESCRVALADEC",Conf.idioma)+" "+metodo.getTipoParametro(i));
					}
					else
					{
						mostrarvalores[i].setText(Texto.get("ETIQFL_SALI",Conf.idioma)+": "+metodo.getNombreParametro(i-(numero/2))+" ("+metodo.getTipoParametro(i-(numero/2))+ServiciosString.cadenaDimensiones(metodo.getDimParametro(i-(numero/2)))+")");
						cuadrosvalores[i].setEnabled(false);
						if (cuadrosvalores[i].isEnabled())
							cuadrosvalores[i].setToolTipText(Texto.get("CPARAM_ESCRVALADEC",Conf.idioma)+" "+metodo.getTipoParametro(i-(numero/2)));
					}
					if (i<metodo.getNumeroParametros())
						mostrarvalores[i].setSelected(metodo.getVisibilidadEntrada(i));
					else
						mostrarvalores[i].setSelected(metodo.getVisibilidadSalida(i-metodo.getNumeroParametros()));
					if (cuadrosvalores[i].isEnabled())
						mostrarvalores[i].setToolTipText(Texto.get("CPARAM_CASILVIS",Conf.idioma));
					mostrarvalores[i].addKeyListener(this);
					mostrarvalores[i].addMouseListener(this);
				}
			}

			
			// Panel vertical para etiquetas
			GridLayout gl1 = new GridLayout(numero,1);
			JPanel panelEtiquetas = new JPanel();
			panelEtiquetas.setLayout(gl1);
			for (int i=0; i<numero; i++)
				panelEtiquetas.add(mostrarvalores[i]);
				
			// Panel vertical para cuadros de valores
			GridLayout gl2 = new GridLayout(numero,1);
			JPanel panelCuadros = new JPanel();
			panelCuadros.setLayout(gl2);
			for (int i=0; i<numero; i++)
				panelCuadros.add(cuadrosvalores[i]);
			
			JTextComponent[] editor = new  JTextComponent[cuadrosvalores.length];
			for (int i=0; i<editor.length; i++)	
			{
				editor[i]=(JTextComponent)cuadrosvalores[i].getEditor().getEditorComponent();
				editor[i].addKeyListener(this);
			}
			
			// Panel vertical para check boxes
			/*GridLayout gl3 = new GridLayout(numero,1);
			JPanel panelChecks = new JPanel();
			panelChecks.setLayout(gl3);
			for (int i=0; i<numero; i++)
				panelChecks.add(mostrarvalores[i]);*/
				
			// Panel general de parámetros
			BorderLayout bl = new BorderLayout();
			panelParam = new JPanel();
			panelParam.setLayout(bl);
			panelParam.setBorder(new TitledBorder(Texto.get("CPARAM_INSERVPAR",Conf.idioma)+" "+this.metodo.getNombre()));
			panelParam.add(panelEtiquetas,BorderLayout.WEST);
			panelParam.add(panelCuadros  ,BorderLayout.CENTER);
			//panelParam.add(panelChecks   ,BorderLayout.EAST);
	
			// Botón Aceptar
			aceptar=new BotonAceptar();
			aceptar.addActionListener(this);
			aceptar.addKeyListener(this);
			aceptar.addMouseListener(this);
			//aceptar.setRojo();
			
			// Botón Cancelar
			cancelar=new BotonCancelar();
			cancelar.addActionListener(this);
			cancelar.addKeyListener(this);
			cancelar.addMouseListener(this);
	
			// Botón Generar
			generar = new BotonTexto(Texto.get("BOTONGENERAR",Conf.idioma));
			generar.addActionListener(this);
			generar.addKeyListener(this);
			generar.addMouseListener(this);

			// Botón Cargar
			cargar = new BotonTexto(Texto.get("BOTONCARGAR",Conf.idioma));
			cargar.addActionListener(this);
			cargar.addKeyListener(this);
			cargar.addMouseListener(this);	

			// Botón Almacenar
			guardar = new BotonTexto(Texto.get("BOTONGUARDAR",Conf.idioma));
			guardar.addActionListener(this);
			guardar.addKeyListener(this);
			guardar.addMouseListener(this);
	
			
			if (!cuadrosvalores[0].isEnabled())
			{
				generar.setEnabled(false);
				cargar.setEnabled(false);
				guardar.setEnabled(false);
			}
			
			
			// Panel para el botón
			panelBoton = new JPanel();
			panelBoton.add(aceptar);
			panelBoton.add(cancelar);
			panelBoton.add(generar);
			panelBoton.add(cargar);
			panelBoton.add(guardar);
	
			// Panel general
			bl= new BorderLayout();
			panel = new JPanel();
			panel.setLayout(bl);
	
			panel.add(panelParam,BorderLayout.NORTH);
			panel.add(panelBoton,BorderLayout.SOUTH);
			
			dialogo.getContentPane().add(panel);
			if (numero!=1)
				dialogo.setTitle(Texto.get("CPARAM_PARAMS",Conf.idioma));
			else
				dialogo.setTitle(Texto.get("CPARAM_PARAM",Conf.idioma));
	
			//cuadrosvalores[0].requestFocusInWindow();
			
			// Preparamos y mostramos cuadro
			int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro+(alturaPiso*numero));
			dialogo.setLocation(coord[0],coord[1]);
			dialogo.setSize(anchoCuadro,altoCuadro+(alturaPiso*numero));
			dialogo.setResizable(false);
			dialogo.setVisible(true);
			
			this.foco();

		}
	}
	
	
	
	
	private void foco()
	{
		cuadrosvalores[0].requestFocusInWindow();
		cuadrosvalores[0].requestFocus();
		mostrarvalores[0].transferFocus();
	}

	/**
		Comprueba y recoge los valores
		
		@param valoresFinales true si el usuario ha dado por buenos los datos introducidos
					false si es una comprobación interna del programa, no solicitada por el usuario
		@param true si los datos introducidos son válidos
	*/
	private synchronized boolean recogerValores(boolean valoresFinales)
	{
		if (this.editarValores)
		{
			
			// En primer lugar, comprobamos que todos los valores escritos son correctos:
			for (int i=0; i<this.metodo.getNumeroParametros(); i++)
			{
				String texto=(String)(cuadrosvalores[i].getSelectedItem());//getText();
				if ( texto.length()==0 )
				{
					new CuadroError(vv,Texto.get("ERROR_PARAM",Conf.idioma), Texto.get("CPARAM_NOVALOR",Conf.idioma)+" nº "+(i+1)+".");
					return false;
				}
				
				if (!ServiciosString.esDeTipoCorrecto(texto,this.metodo.getTipoParametro(i),this.metodo.getDimParametro(i)))
				{
					new CuadroError(vv,Texto.get("ERROR_PARAM",Conf.idioma), Texto.get("CPARAM_ELPARAM",Conf.idioma)
										+" nº"+(i+1)+" ( "+this.metodo.getTipoParametro(i)+
										ServiciosString.cadenaDimensiones(this.metodo.getDimParametro(i))
										+" ) "+Texto.get("CPARAM_NOESCORR",Conf.idioma));
					return false;
				}
			}

			// En segundo lugar, actualizamos los valores de los parámetros del método
			for (int i=0; i<this.metodo.getNumeroParametros(); i++)
			{
				String texto=(String)(cuadrosvalores[i].getSelectedItem());//getText();
				this.metodo.setParamValor(i,texto.replace(" ",""));
			}
		}
		if (!comprobarVisibilidadCorrecta())
			return false;
		

		// En tercer lugar, actualizamos la visibilidad de los parámetros del método
		for (int i=0; i<numero; i++)
		{
			if (i<this.metodo.getNumeroParametros())
				this.metodo.setVisibilidadEntrada(mostrarvalores[i].isSelected(),i);
			else
				this.metodo.setVisibilidadSalida(mostrarvalores[i].isSelected(),i-metodo.getNumeroParametros());
		}	
		
		if (valoresFinales)
		{
			//cmp.actualizarMetodo(this.metodo,this.numMetodo);
			dialogo.setVisible(false);
		}
		
		for (int i=0; i<this.metodo.getNumeroParametros(); i++)
		{
			ValoresParametros.anadirValorListados((String)(cuadrosvalores[i].getSelectedItem())
						,this.metodo.getTipoParametro(i),this.metodo.getDimParametro(i));
		}
		
		if (Conf.fichero_log)
		{
			MetodoAlgoritmo ma=this.clase.getMetodoPrincipal();
			String[] paramMA=ma.getParamValores();
			String mensaje="Lanzar ejecución: "+ma.getNombre()+"(";
			for (int i=0; i<paramMA.length; i++)
			{
				if (i>0)
					mensaje+=", ";
				mensaje+=paramMA[i];
			}
			mensaje+=");";
			Logger.log_write(mensaje);
		}
		Ventana.thisventana.setClase(this.clase);
		dialogo.setVisible(false);
		p.ejecutarAlgoritmo();
		
		return true;
	}
	
	
	
	/**
		Escribe en un JTextField el valor pasado como parámetro
		
		@param texto texto que escribirá en el JTextField correspondiente
		@param i número de JTextField en el que se debe introducir el texto pasado como parámetro
	*/
	public void setValor(String texto, int i)
	{
		if (i<cuadrosvalores.length && cuadrosvalores[i]!=null)
		{
			texto=texto.replace(" ","");
			cuadrosvalores[i].addItem(texto);
			cuadrosvalores[i].setSelectedItem(texto);
		}
			//cuadrosvalores[i].setText(texto);
	}
	
	/**
		Permite extraer el JDialog asociado
		
		@return JDialog de CuadroParam
	*/
	public JDialog getJDialog()
	{
		return this.dialogo;
	}
	
	
	public void gestionEventoBotones(AWTEvent e)
	{
		if (e.getSource()==aceptar)
			recogerValores(true);
		else if (e.getSource()==cancelar)
			dialogo.setVisible(false);
		else if (e.getSource()==generar)
			if (cga==null)
				cga=new CuadroGenerarAleatorio(this,this.metodo);
			else
				cga.setVisible(true);
		else if (e.getSource()==cargar)
		{
			estamosCargando=true;
			if (almacenValores.cargar(this.metodo))
			{
				this.valores=almacenValores.get();
				for (int i=0; i<valores.length; i++)
				{
					ComboBoxEditor cbe=cuadrosvalores[i].getEditor();
					cbe.setItem(this.valores[i]);
					cuadrosvalores[i].insertItemAt(this.valores[i],0);
					cuadrosvalores[i].setSelectedIndex(0);
					//cuadrosvalores[i].setText(this.valores[i]);
				}
				if (recogerValores(false))
					aceptar.setEnabled(true);
				else
					aceptar.setEnabled(false);
			}
			else
			{
				if (almacenValores.getError().length()>0)
					new CuadroError(this.dialogo, "3"+Texto.get("ERROR_ARCH",Conf.idioma),almacenValores.getError());
			}
		}
		else if ( e.getSource()==guardar)
		{
			String valores[]=new String[cuadrosvalores.length];
			for (int i=0; i<cuadrosvalores.length; i++)
				valores[i]=(String)(cuadrosvalores[i].getSelectedItem());
				//valores[i]=cuadrosvalores[i].getText();
			almacenValores.guardar(valores, this.metodo);
			guardar.setEnabled(true);
		}
	}
	
	
	private boolean comprobarVisibilidadCorrecta()
	{
		boolean unoActivo=false;
		// Tendremos n campos: n parámetros
		if (!metodo.getTipo().equals("void"))
		{
			for (int i=0; i<numero; i++)
				if (mostrarvalores[i].isSelected())
					unoActivo=true;
			if (!unoActivo)
				new CuadroError(this.dialogo, Texto.get("ERROR_PARAM",Conf.idioma),Texto.get("ERROR_VISIBIENTR",Conf.idioma));
		}
		// Tenemos n*2 campos: n de entrada, n de salida
		else
		{
			for (int i=0; i<numero/2; i++)
				if (mostrarvalores[i].isSelected())
					unoActivo=true;
			if (!unoActivo)
				new CuadroError(this.dialogo, Texto.get("ERROR_PARAM",Conf.idioma),Texto.get("ERROR_VISIBIENTR",Conf.idioma));
				
			/*if (unoActivo)
			{
				unoActivo=false;
				for (int i=numero/2; i<numero; i++)
					if (mostrarvalores[i].isSelected())
						unoActivo=true;
				if (!unoActivo)
					new CuadroError(this.dialogo, Texto.get("ERROR_PARAM",Conf.idioma),Texto.get("ERROR_VISIBISALI",Conf.idioma));
			}*/
		}
		return unoActivo;
	}
	
	
	
	
	
	
	
	
	
	
	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/
	public void actionPerformed(ActionEvent e)
	{
		//gestionEventoBotones(e);
			
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
		//System.out.println("CuadroParam.keyPressed");
		int code=e.getKeyCode();
		if (code==KeyEvent.VK_ENTER)
		{
			if ((e.getSource()!=cargar || !estamosCargando))
				recogerValores(true);
			estamosCargando=false;
		}
		else if ((e.getSource() instanceof JButton) && code==KeyEvent.VK_SPACE)
			gestionEventoBotones(e);
		
		
		if (e.getSource().getClass().getName().contains("JCheckBox"))
		{
			for (int i=0; i<mostrarvalores.length; i++)
			{
				if (mostrarvalores[i].isFocusOwner())
				{
					if (code==KeyEvent.VK_DOWN)
					{
						mostrarvalores[i].transferFocus();
					}
					else if (code==KeyEvent.VK_UP)
					{
						mostrarvalores[i].transferFocusBackward();
					}
					else if (code==KeyEvent.VK_RIGHT)
					{
						cuadrosvalores[i].requestFocus();
					}
				}
			}
		}
		
		else if (code==KeyEvent.VK_ESCAPE)
			dialogo.setVisible(false);
		else if (e.getSource() instanceof JCheckBox)
			comprobarVisibilidadCorrecta();
	}

	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/
	public void keyTyped(KeyEvent e)
	{
		//System.out.println("CuadroParam.keyTyped");
		/*int code=e.getKeyCode();
		new Thread() {
			public synchronized void run()
			{
				try { wait (10); } catch (InterruptedException ie) {System.out.println("Error de delay");};
				if (recogerValores(false))
					aceptar.setVerde();
				else
					aceptar.setRojo();
			}
		}.start();*/

	}

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/	
	public void mouseClicked(MouseEvent e) 
	{
		/*if (e.getSource()==aceptar)
			recogerValores(true);
		else if (e.getSource()==cancelar)
			dialogo.setVisible(false);*/


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
		if (e.getSource() instanceof JButton)
			gestionEventoBotones(e);
		if (e.getSource() instanceof JCheckBox)
			comprobarVisibilidadCorrecta();
	}
	
	public void setLuzVerde(boolean v)
	{
		this.luzVerde=v;
	}
	
}
