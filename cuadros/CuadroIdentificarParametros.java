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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import conf.Conf;

import botones.BotonAceptar;
import botones.BotonCancelar;

import datos.ClaseAlgoritmo;
import datos.MetodoAlgoritmo;
import datos.Preprocesador;
import utilidades.Texto;
import ventanas.Ventana;

public class CuadroIdentificarParametros extends Thread implements ActionListener, KeyListener, MouseListener, WindowListener
{
	final int anchoCuadro=590;
	final int altoCuadro=260;
	
	JDialog dialogo;
	JFrame vv;
	
	BotonAceptar aceptar;
	BotonCancelar cancelar;
	
	String e;	// Metemos valores introducidos por usuario en una vez anterior, dentro del mismo proceso de carga de clases
	String ind;	// Metemos valores introducidos por usuario en una vez anterior, dentro del mismo proceso de carga de clases
	
	Boolean esArray;
	
	MetodoAlgoritmo metodo;
	
	final int longitudCampos=4;
	
	int numCampos=-1;
	JTextField campoEstructura;
	JTextField[] camposIndices;
	
	int numMetodo=-1;
	CuadroSeleccionMetodos csm;
	
	
	JLabel etiqParamsIndices1,etiqParamsIndices2;
	
	JPanel panelIzqda;
	JPanel panelParamEstructura;
	JPanel panelImagen;
	JPanel panelCuadro;
	
	boolean avancePermitido = false;
	
	/**
		Contructor: genera un nuevo cuadro de diálogo que permite al usuario elegir qué método de una determinada clase se quiere ejecutar
		
		@param clase clase a la que pertenece el método que se quiere ejecutar
		@param ventanaVisualiazdor ventana a la que se asociará el cuadro de diálogo
		@param gestorEjecucion gestor que realizará los pasos necesarios para ejecutar el método seleccionado
		@param codigounico código único que identifica a la clase y la da nombre
	*/
	public CuadroIdentificarParametros(CuadroSeleccionMetodos csm, MetodoAlgoritmo metodo, int numMetodo, String e, String ind)
	{	
		this.metodo=metodo;

		dialogo = new JDialog(Ventana.thisventana,true);
		this.vv=Ventana.thisventana;

		this.e=e;
		this.ind=ind;
		
		this.csm=csm;
		
		this.numMetodo=numMetodo;
		
		this.start();
	}
	
	
	
	public void run()
	{
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		
		// panel de la izquierda: contiene las etiquetas y los cuadros de texto
		
		panelIzqda=new JPanel();
		panelIzqda.setLayout(new BorderLayout());
		
		JLabel etiqParamEstructura=new JLabel(Texto.get("CIPDYV_PARAMESTR",Conf.idioma)+" (0.."+(this.metodo.getNumeroParametros()-1)+"):");
		etiqParamsIndices1=new JLabel(Texto.get("CIPDYV_PARAMINDI1",Conf.idioma));
		etiqParamsIndices2=new JLabel(Texto.get("CIPDYV_PARAMINDI2",Conf.idioma)+" (0.."+(this.metodo.getNumeroParametros()-1)+"):");

		/*JLabel[] etiqIndices=new JLabel[numCampos];
		camposIndices = new JTextField[numCampos];*/
		
		panelParamEstructura=new JPanel();
		panelParamEstructura.setLayout(new BorderLayout());
		
		campoEstructura = new JTextField(longitudCampos);
		campoEstructura.setHorizontalAlignment(JTextField.CENTER);
		campoEstructura.setText(e);
		campoEstructura.addKeyListener(this);
		JPanel panelCampoEstructura=new JPanel();
		panelCampoEstructura.setLayout(new BorderLayout());
		panelCampoEstructura.add(campoEstructura,BorderLayout.WEST);
		
		panelParamEstructura.add(etiqParamEstructura,BorderLayout.NORTH);
		panelParamEstructura.add(panelCampoEstructura,BorderLayout.SOUTH);

		
		/*JPanel panelParamIndices=new JPanel();
		panelParamIndices.setLayout(new GridLayout(numCampos+2,1));
		
		panelParamIndices.add(etiqParamsIndices1);
		panelParamIndices.add(etiqParamsIndices2);
		
		JPanel[] panelFilaIndice=new JPanel[numCampos];
		
		
		String[] textosInd=ind.replace(" ","").split(",");
		

		for (int i=0; i<numCampos; i++)
		{
			camposIndices[i]=new JTextField(longitudCampos);
			etiqIndices[i]=new JLabel("  "+Texto.get("CIPDYV_PARAMINDICE"+numCampos+i,Conf.idioma));
			camposIndices[i].setHorizontalAlignment(JTextField.CENTER);
			camposIndices[i].addKeyListener(this);
			if (i<textosInd.length)
				camposIndices[i].setText(textosInd[i]);
			panelFilaIndice[i]=new JPanel();
			panelFilaIndice[i].setLayout(new BorderLayout());
			panelFilaIndice[i].add(camposIndices[i],BorderLayout.WEST);
			panelFilaIndice[i].add(etiqIndices[i],BorderLayout.CENTER);
			panelParamIndices.add(panelFilaIndice[i]);
		}*/
		
		panelIzqda.add(panelParamEstructura,BorderLayout.NORTH);
		/*panelIzqda.add(panelParamIndices,BorderLayout.SOUTH);*/
		
		
		// panel de la derecha: contiene el gráfico esquemático
		panelImagen=new JPanel();
		
		panelImagen.setPreferredSize(new Dimension(300,162));
		
		/*Icon imagen = new ImageIcon( (esArray ? "imagenes/esquema_array.png" : "imagenes/esquema_matriz.png"));
		JLabel etiquetaImagen=new JLabel();
		etiquetaImagen.setIcon(imagen);
		
		panelImagen.add(etiquetaImagen,BorderLayout.CENTER);*/
		
		// panel de botones

		// Botón Aceptar
		aceptar=new BotonAceptar();//aceptar=new JButton ("Aceptar");
		aceptar.addActionListener(this);
		aceptar.addKeyListener(this);
		aceptar.addMouseListener(this);
		
		// Botón Cancelar
		cancelar=new BotonCancelar();
		cancelar.addActionListener(this);
		cancelar.addKeyListener(this);
		cancelar.addMouseListener(this);

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(aceptar);
		panelBoton.add(cancelar);
		
		// creación del panel general
		panelCuadro=new JPanel();
		panelCuadro.setLayout(new BorderLayout());
		panelCuadro.add(panelIzqda,BorderLayout.WEST);
		panelCuadro.add(panelImagen,BorderLayout.EAST);
		panelCuadro.setBorder(new TitledBorder(Texto.get("CIPDYV_METODO",Conf.idioma)+": "+this.metodo.getRepresentacionTotal()));
		
		panel.add(panelCuadro,BorderLayout.NORTH);
		panel.add(panelBoton,BorderLayout.SOUTH);
		
		dialogo.addWindowListener(this);
		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("CIPDYV_TITULO",Conf.idioma));
		dialogo.setSize(anchoCuadro,altoCuadro);
		int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
		dialogo.setLocation(coord[0],coord[1]);
		
		dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		pintarCampos();
		
		dialogo.setResizable(false);
		dialogo.setVisible(true);
	}
	

	// INTRO -> ACCIÓN, ESC -> SALIR
	
	
	
	
	public void accionAceptar()
	{
		int estado=valoresCorrectos();
		//System.out.println("estado="+estado);
		if (estado==0)
		{
			csm.setParametrosMetodo(numMetodo, campoEstructura.getText(), valoresParametrosIndices());
			dialogo.setVisible(false);
		}
		if (estado!=0 && valoresVacios())
		{
			csm.marcarMetodo(numMetodo, false);
			csm.setParametrosMetodo(numMetodo, campoEstructura.getText(), valoresParametrosIndices());
			dialogo.setVisible(false);
		}
		else if (estado!=0)
		{
			new CuadroError(dialogo,Texto.get("ERROR_VAL",Conf.idioma),Texto.get("ERROR_INFOPARAM"+estado,Conf.idioma));
		}
	}
	
	public void accionCancelar()
	{
		csm.marcarMetodo(numMetodo, false);
		dialogo.setVisible(false);
	}
	
	
	
	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/		
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==aceptar)
			accionAceptar();

		else if (e.getSource()==cancelar)
			accionCancelar();
			
	}
	
	
	
	private boolean valoresVacios()
	{
		if (campoEstructura.getText().replace(" ","").length()!=0)
			return false;
	
		for (int i=0; i<numCampos; i++)
			if (camposIndices[i].getText().replace(" ","").length()!=0)
				return false;
			
		return true;
	}
	
	
	private int valoresCorrectos()
	{
		// Devielve un número que representa un error determinado:
		// 0 = todo correcto
		// 1 = núm. parámetro de estructura no es válido
		// 2 = núm. parámetro de índice no es válido 
		// 3 = algunos núm. parámetro de índices están vacíos
		// 4 = valores repetidos

		
		// Primero comprobamos que todo son números correctamente escritos
		try {
			int x=Integer.parseInt(campoEstructura.getText());
			if (x<0 || x>=this.metodo.getNumeroParametros())
			{
				return 1;
			}
		} catch (Exception e) {
			return 1;
		}
		
		int vacios=0;
		try {
			for (int i=0; i<numCampos; i++)
			{
				if (camposIndices[i].getText().length()!=0)
				{
					int x=Integer.parseInt(camposIndices[i].getText());
					if (x<0 || x>=this.metodo.getNumeroParametros())
					{
						return 2;
					}
				}
				else
					vacios++;
			}
		} catch (Exception e) {
			return 2;
		}
		
		if (vacios!=0 && vacios!=numCampos)	// Si usuario ha dejado algunos en blanco... mal (o deja todos o rellena todos)
		{
			return 3;
		}
		
		// Comprobamos que no haya ni un solo valor repetido
		if (vacios==0)
		{
			for (int i=0; i<numCampos; i++)
				for (int j=i+1; j<numCampos; j++)
				{
					if (Integer.parseInt(camposIndices[i].getText()) == Integer.parseInt(camposIndices[j].getText()))
					{
						return 4;
					}
				}
			
			for (int i=0; i<numCampos; i++)
				if (Integer.parseInt(camposIndices[i].getText()) == Integer.parseInt(campoEstructura.getText()))
				{
					return 4;
				}
		}	
		
		
		
		
		// Después comprobamos que el número de parámetro indicado para la estructura tiene realmente una estructura
		String[] tipos=this.metodo.getTiposParametros();
		int[] dim=this.metodo.getDimParametros();
		
		int ncampo=Integer.parseInt(campoEstructura.getText());
		
		if (dim[ncampo]<1)
			return 1;

		
		// Finalmente comprobamos que los números de parámetro indicados para los índices de delimitación son correctos (parámetros int)
		
		for (int i=0; i<numCampos; i++)
		{
			try {
				
				if (camposIndices[i].getText().length()==0)
				{
					
				}
				else
				{
					if (  (dim[Integer.parseInt(camposIndices[i].getText())]!=0) ||
						!tipos[Integer.parseInt(camposIndices[i].getText())].equals("int"))
					{
						return 2;
					}
				}
			} catch(Exception e) {
				return 2;
			}
		}
		
		return 0;
	}
	
	
	
	private String valoresParametrosIndices()
	{
		String resultado="";
		
		boolean vacios=true;
		for (int i=0; i<numCampos; i++)
		{
			if (camposIndices[i].getText().replace(" ","").length()!=0)
				vacios=false;
		}
		
		if (vacios)
			return "";
		
		for (int i=0; i<numCampos; i++)
		{
			if (i!=0)
				resultado=resultado+", ";
			resultado=resultado+camposIndices[i].getText();
			
		}
		return resultado;
	}
	
	
	
	private void pintarCampos()
	{
		int ncampo=-1;
		
		try {
			ncampo=Integer.parseInt(campoEstructura.getText());
		} catch (Exception e) {
			//System.out.println("Error recogiendo numero en cuadro de dialogo");
		}
		
		int dimCampo=-1;
		if (ncampo!=-1 && ncampo>=0 && ncampo<this.metodo.getNumeroParametros())
		{
			// Miramos si el parámetro ncampo del método
			dimCampo=this.metodo.getDimParametro(ncampo);
			numCampos=dimCampo*2;
		}
		switch(dimCampo)
		{
			case 1:
			case 2:
				JLabel[] etiqIndices=new JLabel[numCampos];
				camposIndices = new JTextField[numCampos];
				JPanel panelParamIndices=new JPanel();
				panelParamIndices.setLayout(new GridLayout(numCampos+2,1));
				
				panelParamIndices.add(etiqParamsIndices1);
				panelParamIndices.add(etiqParamsIndices2);
				
				JPanel[] panelFilaIndice=new JPanel[numCampos];
				String[] textosInd=ind.replace(" ","").split(",");
				
				for (int i=0; i<numCampos; i++)
				{
					camposIndices[i]=new JTextField(longitudCampos);
					etiqIndices[i]=new JLabel("  "+Texto.get("CIPDYV_PARAMINDICE"+numCampos+i,Conf.idioma));
					camposIndices[i].setHorizontalAlignment(JTextField.CENTER);
					camposIndices[i].addKeyListener(this);
					if (i<textosInd.length)
						camposIndices[i].setText(textosInd[i]);
					panelFilaIndice[i]=new JPanel();
					panelFilaIndice[i].setLayout(new BorderLayout());
					panelFilaIndice[i].add(camposIndices[i],BorderLayout.WEST);
					panelFilaIndice[i].add(etiqIndices[i],BorderLayout.CENTER);
					panelParamIndices.add(panelFilaIndice[i]);
				}
				
				panelIzqda.removeAll();
				panelIzqda.add(panelParamEstructura,BorderLayout.NORTH);
				
				Icon imagen = new ImageIcon(numCampos==2 ? "imagenes/esquema_array.png" : "imagenes/esquema_matriz.png");
				JLabel etiquetaImagen=new JLabel();
				etiquetaImagen.setIcon(imagen);
				
				panelImagen.removeAll();
				panelImagen.add(etiquetaImagen,BorderLayout.CENTER);
				panelIzqda.add(panelParamIndices,BorderLayout.SOUTH);
				
				if(this.metodo.getNumeroParametros()<10)
					camposIndices[0].requestFocus(); //Ponemos el cursor sobre el siguiente campo
				avancePermitido = true;
				break;
			default:
				avancePermitido = false;
				camposIndices = null;
				panelImagen.removeAll();
				panelIzqda.removeAll();
				panelIzqda.add(panelParamEstructura,BorderLayout.NORTH);
				
		}

		if((dimCampo!=1)&&(dimCampo!=2))	//Si no se introduce un numero de parametro correcto, mantiene el foco
			campoEstructura.requestFocus();
		else if (this.metodo.getNumeroParametros()>=10)
			campoEstructura.requestFocus(); //Si se introduce numero de parametro correcto pero num parametros >= 10, mantiene el foco

		panelImagen.updateUI();
		panelIzqda.updateUI();
		panelCuadro.updateUI();
	
		
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
		Object fuente=e.getSource();
		
		int code=e.getKeyCode();
		if (code==KeyEvent.VK_ENTER)
			accionAceptar();
		else if (code==KeyEvent.VK_ESCAPE)
			accionCancelar();
		else if (fuente==campoEstructura)
		{
			if ((code==KeyEvent.VK_LEFT)||(code==KeyEvent.VK_RIGHT)||(code==KeyEvent.VK_UP)) {
				//No hacemos nada
			}
			//AvancePermitido nos dice si ya estan cargados (y no cambian) los campos de texto inferiores
			else if((avancePermitido)&&(code==KeyEvent.VK_DOWN)) {
				camposIndices[0].requestFocus();
			}
			else {
				//System.out.println("Tecla pulsada en el JTextField de estructura");
				pintarCampos();
			}
		}
		
		//Comprobamos si se ha escrito en alguno de los campos para el resto de parametros
		else {
			int ncampo=-1;
			try {
				ncampo=Integer.parseInt(campoEstructura.getText());
			} catch (Exception ex) {
				//System.out.println("Error recogiendo numero en cuadro de dialogo");
			}
			int dimCampo=-1;
			if (ncampo!=-1 && ncampo>=0 && ncampo<this.metodo.getNumeroParametros())
			{
				// Miramos si el parámetro ncampo del método
				dimCampo=this.metodo.getDimParametro(ncampo);
				numCampos=dimCampo*2;
			}
			boolean detectado = false;
			boolean avance = false;
			boolean retroceso = false;
			int i;
			for (i=0; i<numCampos; i++)
			{
				boolean esNumero = true;
				try {  
					Integer.parseInt(camposIndices[i].getText());  
				}  
					catch (NumberFormatException eg) {  
					esNumero = false;
				}
				if ((fuente==camposIndices[i])&&(esNumero)&&(code!=KeyEvent.VK_BACK_SPACE)&&(code!=KeyEvent.VK_LEFT)&&(code!=KeyEvent.VK_RIGHT)&&(code!=KeyEvent.VK_UP)&&(code!=KeyEvent.VK_DOWN)) {
					detectado = true;
					break;
				}
				//Avance
				else if ((fuente==camposIndices[i])&&(code==KeyEvent.VK_DOWN)) {
					avance = true;
					detectado = true;
					break;
				}
				//Retroceso
				else if ((fuente==camposIndices[i])&&(code==KeyEvent.VK_UP)) {
					retroceso = true;
					detectado = true;
					break;
				}
			}
			if((detectado)) 
			{
				if(avance) {
					if(i<numCampos-1)
						camposIndices[i+1].requestFocus(); //Ponemos el cursor sobre el siguiente campo
				}
				else if (retroceso) {
					if(i==0)
						campoEstructura.requestFocus();
					else 
						camposIndices[i-1].requestFocus();
				}
				else if(this.metodo.getNumeroParametros()<10) {
					if(i<numCampos-1)
						camposIndices[i+1].requestFocus(); //Ponemos el cursor sobre el siguiente campo
				}
			}
			detectado = false;
			avance = false;
			retroceso = false;
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
		
	}



	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		accionCancelar();
	}



	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



}
