/**
	Esta clase constituye la ventana de visualización de códigos de la aplicación

	@author Antonio Pérez Carrasco
	@version 2006-2007

*/
package ventanas;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.lang.IllegalMonitorStateException;
import java.lang.Runtime;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import botones.*;
import conf.*;
import cuadros.*;
import opciones.*;
import utilidades.*;

public class VentanaVerCodigo extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=03;

	String[] fichero=new String[2];

	// Botones de la ventana
	BotonIcono abrir;
	BotonIcono guardar;
	BotonIcono compilar;
	BotonIcono cortar;
	BotonIcono copiar;
	BotonIcono pegar;
	JPanel panelb;

	JScrollPane panelScroll;
	JTextArea areatexto,areaNum;
	JLabel etiqueta,nombre;

	boolean abortar=false;
	
	boolean modificado=false;
	boolean maquinaValida=true;
	
	OpcionMVJava omvj;
	GestorOpciones gOpciones=new GestorOpciones();
	JFrame ventana;
	
	JTextPane textPane ;
	
	
	String errores="";
	
	
	
	GestorVentanaEditor gestorVentana;


	/**
		Constructor: genera una nueva ventana para visualizar códigos Java.
	*/
	public VentanaVerCodigo()
	{
		this.fichero[0]=null;
		this.fichero[1]=null;
		this.start();
	}
	
	/**
		Constructor: genera una nueva ventana para visualizar códigos Java.
	*/
	public VentanaVerCodigo(String[] path)
	{
		this.fichero[0]=path[0];
		this.fichero[1]=path[1];
		this.start();
	}
	
	
	/**
		Constructor: genera una nueva ventana para visualizar códigos Java.
	*/
	public VentanaVerCodigo(String path)
	{
		this.fichero[0]=path.substring(0,path.lastIndexOf("\\")+1);
		this.fichero[1]=path.substring(path.lastIndexOf("\\")+1,path.length());
		this.start();
	}	
	
	/**
		Genera una nueva ventana para visualizar códigos Java a través de un nuevo thread de ejecución.
	*/
	public void run()
	{
		
		
		
		// Cargamos opción de ficheros recientes (para saber último directorio)
		OpcionFicherosRecientes ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);
		this.fichero[0]=ofr.getDir();
	
		// Revisamos que haya máquina virtual válida configurada
		// Cargamos opción de máquina virtual
		OpcionMVJava omvj=(OpcionMVJava)gOpciones.getOpcion("OpcionMVJava",true);
		maquinaValida=omvj.getValida();

		if (this.fichero[1]==null)		// Si no hemos recibido nombre fichero por parametro, sacamos JFileChooser
		{
			// Cargamos opción de ficheros recientes (para saber último directorio)
			ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);

			this.fichero[0]=ofr.getDir();
			//this.fichero=SelecDirecccion.cuadroAbrirFichero(this.fichero[0],Texto.get("VVC_VEREDITAR",Conf.idioma),null,"java","Código JAVA",1);
			this.fichero=SelecDireccion.cuadroAbrirFichero(this.fichero[0],Texto.get("VVC_VEREDITAR",Conf.idioma),
							null,"java",Texto.get("ARCHIVO_JAVA",Conf.idioma),1);	
			
			ofr.setDir(fichero[0]);
			gOpciones.setOpcion(ofr,2);
		}
		
		// Gestor de la ventana
		BorderLayout bl= new BorderLayout(2,1);
		this.ventana=new JFrame();
		this.ventana.setLayout(bl);
		this.ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//ventana.setBackground(Conf.colorCuadroDialogo);
	
		// Etiqueta
		etiqueta = new JLabel();
		
		// Grupo de botones
		ButtonGroup botones = new ButtonGroup();

		panelb = new JPanel();
		
		abrir = 	new BotonIcono( new ImageIcon("./imagenes/icono_editornuevo.gif"),Conf.anchoBoton/3,Conf.altoBoton );
		guardar = 	new BotonIcono( new ImageIcon("./imagenes/icono_editorguardar.gif"),Conf.anchoBoton/3,Conf.altoBoton );
		compilar = 	new BotonIcono( new ImageIcon("./imagenes/icono_editorcompilar.gif"),Conf.anchoBoton/3,Conf.altoBoton );
		cortar = 	new BotonIcono( new ImageIcon("./imagenes/icono_editorcortar.gif"),Conf.anchoBoton/3,Conf.altoBoton );
		copiar = 	new BotonIcono( new ImageIcon("./imagenes/icono_editorcopiar.gif"),Conf.anchoBoton/3,Conf.altoBoton );
		pegar = 	new BotonIcono( new ImageIcon("./imagenes/icono_editorpegar.gif"),Conf.anchoBoton/3,Conf.altoBoton );

		/*abrir = new Boton("imagenes/boton_abrirfichero_verde.gif","imagenes/boton_abrirfichero_naranja.gif",
						"imagenes/boton_abrirfichero_rojo.gif", Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
		guardar = new Boton("imagenes/boton_guardar2_verde.gif","imagenes/boton_guardar2_naranja.gif",
						"imagenes/boton_guardar2_rojo.gif", Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
		compilar = new Boton("imagenes/boton_compilar_verde.gif","imagenes/boton_compilar_naranja.gif",
						"imagenes/boton_compilar_rojo.gif", Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
		cortar = new Boton("imagenes/boton_cortar_verde.gif","imagenes/boton_cortar_naranja.gif",
						"imagenes/boton_cortar_rojo.gif", Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
		copiar = new Boton("imagenes/boton_copiar_verde.gif","imagenes/boton_copiar_naranja.gif",
						"imagenes/boton_copiar_rojo.gif", Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
		pegar = new Boton("imagenes/boton_pegar_verde.gif","imagenes/boton_pegar_naranja.gif","imagenes/boton_pegar_rojo.gif",
						Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);						*/
						
		abrir.addKeyListener(this);
		guardar.addKeyListener(this);
		compilar.addKeyListener(this);
		cortar.addKeyListener(this);
		copiar.addKeyListener(this);
		pegar.addKeyListener(this);
		
		abrir.addMouseListener(this);
		guardar.addMouseListener(this);
		compilar.addMouseListener(this);
		cortar.addMouseListener(this);
		copiar.addMouseListener(this);
		pegar.addMouseListener(this);
		
		abrir.setToolTipText(Texto.get("VVC_ABRIR",Conf.idioma));
		guardar.setToolTipText(Texto.get("VVC_GUARD",Conf.idioma));
		compilar.setToolTipText(Texto.get("VVC_COMP",Conf.idioma));
		cortar.setToolTipText(Texto.get("VVC_CORT",Conf.idioma));
		copiar.setToolTipText(Texto.get("VVC_COP",Conf.idioma));
		pegar.setToolTipText(Texto.get("VVC_PEG",Conf.idioma));
		
		//guardar.setRojo();
		//cortar.setRojo();
		//copiar.setRojo();
		guardar.setEnabled(false);
		cortar.setEnabled(false);
		copiar.setEnabled(false);
		
		if (!maquinaValida)
			//compilar.setRojo();
			compilar.setEnabled(false);
		
		panelb.add(abrir);
		panelb.add(guardar);
		panelb.add(compilar);
		panelb.add(cortar);
		panelb.add(copiar);
		panelb.add(pegar);
		
		panelb.addKeyListener(this);
		panelb.addMouseListener(this);
		
		// Etiqueta para nombre de fichero
		nombre = new JLabel();
		nombre.setFont(Conf.fuenteTitulo);
		nombre.setHorizontalAlignment(0);
		
		// Área de texto y panel de scroll
		areatexto = new JTextArea(4,4);
		areatexto.setFont(new Font("Courier New",Font.BOLD,14));
		areatexto.addKeyListener(this);
		areatexto.addMouseListener(this);
		
		lecturaFichero(true,false);
		
		
		// Área de numero de linea
		areaNum = new JTextArea(4,4);
		areaNum.setFont(new Font("Courier New",Font.BOLD,14));		
		areaNum.setEditable(false);
		areaNum.setEnabled(false);
		areaNum.setBackground(new Color(230,230,230));
		areaNum.setForeground(new Color(100,100,100));
		areaNum.setAlignmentY(Container.RIGHT_ALIGNMENT);
		actualizarBandaNumLinea();

		JPanel panelEditor=new JPanel();
		panelEditor.setLayout(new BorderLayout());
		
		panelEditor.add(areatexto,BorderLayout.CENTER);
		panelEditor.add(areaNum,BorderLayout.WEST);
		

		panelScroll = new JScrollPane();
		panelScroll.getViewport().add( panelEditor/*areatexto*/ );
		panelScroll.setSize(new Dimension(2,2));

		// Etiqueta
		etiqueta.setText(Texto.get("VVC_LISTO",Conf.idioma));
		etiqueta.setFont(Conf.fuenteCuadro);
		etiqueta.setBackground(Conf.colorCuadroDialogo);
		

						
		// Panel Superior
		BorderLayout bl2= new BorderLayout();
		JPanel panelSup = new JPanel();
		panelSup.setLayout(bl2);
		//panelSup.setBackground(Conf.colorCuadroDialogo);
		panelSup.add(panelb,BorderLayout.WEST);
		panelSup.add(nombre,BorderLayout.CENTER);
		
		JPanel panelAreaCodigo = new JPanel();
		panelAreaCodigo.setLayout(new BorderLayout());
		panelAreaCodigo.add(panelScroll,BorderLayout.CENTER);	
		panelAreaCodigo.add(panelSup,BorderLayout.NORTH);	
		panelAreaCodigo.add(etiqueta,BorderLayout.SOUTH);		
		
		
		
		// JScrollPane para errores
		textPane = new JTextPane();
		textPane.setText(errores);
		textPane.setFont(new Font("Courier New",Font.PLAIN, 11));
		//textPane.setBackground(etiqueta.getBackground());

		textPane.setEditable(false);
		textPane.addMouseListener(this);
		

		JScrollPane jsp=new JScrollPane(textPane);
		
		JSplitPane separador=new JSplitPane(JSplitPane.VERTICAL_SPLIT,panelAreaCodigo,jsp);
        separador.setResizeWeight(0.8);
		separador.setDividerLocation(0.8);
		
		this.ventana.add(separador,BorderLayout.CENTER);

		this.ventana.setTitle(Texto.get("VVC_TITULO",Conf.idioma));
		this.ventana.setLocation(50,20);
		this.ventana.setSize(950,750);
		
		
		this.ventana.setIconImage( new ImageIcon(Ventana.icono).getImage() );		// icono_srec.gif
		
		this.gestorVentana=new GestorVentanaEditor(this);
		this.ventana.addWindowListener(gestorVentana);
		this.ventana.addWindowStateListener(gestorVentana);
		
		
		//lecturaFichero(true,false);
		
	}

	/**
		Gestiona los eventos de acción.
		
		@param e Evento de acción
	*/
	public synchronized void actionPerformed(ActionEvent e)
	{
		
	}
	
	/**
		Gestiona los eventos de teclado.
		
		@param e Evento de teclado
	*/
	public void keyPressed(KeyEvent e)
	{
		
	}
	
	/**
		Gestiona los eventos de teclado.
		
		@param e Evento de teclado
	*/
	public void keyReleased(KeyEvent e)
	{
		
		// Hay que comprobar si se puede seleccionar algo para cortar y copiar
		if ( areatexto.getSelectedText()==null || areatexto.getSelectedText().length()==0)
		{
			//copiar.setRojo();
			//cortar.setRojo();
			copiar.setEnabled(false);
			cortar.setEnabled(false);
		}
		else
		{
			//copiar.setVerde();
			//cortar.setVerde();
			copiar.setEnabled(true);
			cortar.setEnabled(true);
		}
	}

	/**
		Gestiona los eventos de teclado.
		
		@param e Evento de teclado
	*/
	public void keyTyped(KeyEvent e)
	{
		int code=e.getKeyCode();
		
		modificado=true;	// Han escrito algo (o pegado algo): han modificado el texto
		//guardar.setVerde();
		guardar.setEnabled(true);
		etiqueta.setText(Texto.get("VVC_LISTO",Conf.idioma));
		
		actualizarBandaNumLinea();
	}

	/**
		Gestiona los eventos de ratón.
		
		@param e Evento de ratón
	*/	
	public void mouseClicked(MouseEvent e) 
	{
		if (e.getSource().getClass().getCanonicalName().equals("javax.swing.JTextArea"))
		{
			if ( areatexto.getSelectedText()==null || areatexto.getSelectedText().length()==0)
			{
				//copiar.setRojo();
				//cortar.setRojo();
				copiar.setEnabled(false);
				cortar.setEnabled(false);
			}
			else
			{
				//copiar.setVerde();
				//cortar.setVerde();
				copiar.setEnabled(true);
				cortar.setEnabled(true);
			}
		}
		
	}
	
	/**
		Gestiona los eventos de ratón.
		
		@param e Evento de ratón
	*/	
	public void mouseEntered(MouseEvent e) 
	{
		
	}
	
	/**
		Gestiona los eventos de ratón.
		
		@param e Evento de ratón
	*/	
	public void mouseExited(MouseEvent e) 
	{
		if ( (e.getSource())==abrir)
				//abrir.setVerde();
				abrir.setEnabled(true);
	
		else if ( (e.getSource())==guardar)
		{
			if (modificado)
				//guardar.setVerde();
				guardar.setEnabled(true);
			else
				//guardar.setRojo();
				guardar.setEnabled(false);
		}
		else if ( (e.getSource())==cortar)
		{
			if ( areatexto.getSelectedText()==null || areatexto.getSelectedText().length()==0)
				//cortar.setRojo();
				cortar.setEnabled(false);
			else
				//cortar.setVerde();
				cortar.setEnabled(true);
		}
		else if ( (e.getSource())==copiar)
		{
			if ( areatexto.getSelectedText()==null || areatexto.getSelectedText().length()==0)
				//copiar.setRojo();
				copiar.setEnabled(false);
			else
				//copiar.setVerde();
				copiar.setEnabled(true);
		}
		else if ( (e.getSource())==pegar)
			//pegar.setVerde();
			pegar.setEnabled(true);
		else if ( (e.getSource())==compilar)
			if (maquinaValida)
				//compilar.setVerde();
				compilar.setEnabled(true);
			else
				//compilar.setRojo();
				compilar.setEnabled(false);
	}
	
	
	
	public void guardar()
	{
		modificado=false;
		try {
			FileWriter fileStream = new FileWriter( this.fichero[0]+this.fichero[1]);
			fileStream.write(areatexto.getText());
			fileStream.close();
		} catch (IOException ioe) {
			System.out.println("Error IO");
			abortar=true;
		}
		etiqueta.setText(Texto.get("VVC_ARCHGUARD",Conf.idioma));
	}
	
	
	/**
		Gestiona los eventos de ratón.
		
		@param e Evento de ratón
	*/	
	public synchronized void mousePressed(MouseEvent e) 
	{
		if ( e.getSource()==abrir) //Abrir
		{
			lecturaFichero(false,true);
		}

		else if ( e.getSource()==guardar) //Guardar
		{
			guardar();
		}
		else if ( e.getSource()==compilar) //Compilar
		{
			if (maquinaValida)
			{
				etiqueta.setText(Texto.get("VVC_LISTO",Conf.idioma));
				compilar.setEnabled(false);				
				llamadaCompilacion();
				etiqueta.setText(Texto.get("VVC_LISTO",Conf.idioma));
				compilar.setEnabled(true);
			}
			else
				new CuadroError(ventana, Texto.get("ERROR_CONF",Conf.idioma),Texto.get("ERROR_NOMVJ",Conf.idioma));

		}
		else if ( e.getSource()==cortar) //Cortar
		{
			areatexto.cut();
			areatexto.setSelectionStart(0);
			areatexto.setSelectionEnd(0);
			etiqueta.setText(Texto.get("VVC_LISTO",Conf.idioma));
		}
		else if ( e.getSource()==copiar) //Copiar
		{
			areatexto.copy();
			areatexto.setSelectionStart(0);
			areatexto.setSelectionEnd(0);
			etiqueta.setText(Texto.get("VVC_LISTO",Conf.idioma));
		}
		else if ( e.getSource()==pegar) //Pegar
		{
			areatexto.paste();
			modificado=true;
			//guardar.setVerde();
			guardar.setEnabled(true);
			etiqueta.setText(Texto.get("VVC_LISTO",Conf.idioma));
		}
		
		
		
	}
	
	/**
		Gestiona los eventos de ratón.
		
		@param e Evento de ratón
	*/	
	public void mouseReleased(MouseEvent e)
	{
		// Hay que comprobar si se puede seleccionar algo para cortar y copiar
		if ( areatexto.getSelectedText()==null || areatexto.getSelectedText().length()==0)
		{
			copiar.setEnabled(false);
			cortar.setEnabled(false);
		}
		else
		{
			copiar.setEnabled(true);
			cortar.setEnabled(true);
		}
		
		if (e.getSource().getClass().getCanonicalName().equals("javax.swing.JTextPane"))
		{
			new Thread(){
				public synchronized void run()
				{
					try { wait(60); } catch (Exception e) {};
					String seleccionado=textPane.getSelectedText();
					if (seleccionado!=null)
					{
						int numeroLinea=numLinea(seleccionado);
						//System.out.println("LineaDeCodigo="+numeroLinea);
						
						if (numeroLinea!=-1)
						{
							int longitud=0;
							String textoareatexto=areatexto.getText();

							while (numeroLinea>1)		// !=0
							{
								longitud=longitud+(textoareatexto.substring(0,textoareatexto.indexOf("\n")).length()+1);
								//System.out.println("["+textoareatexto.substring(0,textoareatexto.indexOf("\n"))+"]");
								textoareatexto=textoareatexto.substring(textoareatexto.indexOf("\n")+1,
													textoareatexto.length());
								numeroLinea--;
							}
							int inicio=longitud;
							longitud=longitud+(textoareatexto.substring(0,textoareatexto.indexOf("\n")).length());
							areatexto.select(inicio,longitud);
							areatexto.requestFocus();
							//System.out.println("inicio="+inicio+"  final="+longitud);
						}
					}
				}
			}.start();
		}
	}
	
	/**
		Lee un fichero y lo vuelca en el área de texto de la ventana
		
		@param inicio Será true cuando se esté leyendo un fichero para abrir la ventana (false si la ventana ya está abierta).
	*/	
	public void lecturaFichero (boolean inicio, boolean obligamosExaminar)
	{
		String ant0=fichero[0];
		String ant1=fichero[1];
		if ((this.fichero[0]==null && this.fichero[1]==null) || obligamosExaminar)
		{
			//this.fichero=SelecDirecccion.cuadroAbrirFichero(this.fichero[0],
			//					Texto.get("VVC_VEREDITAR",Conf.idioma),null,"java","Código JAVA",1);
			this.fichero=SelecDireccion.cuadroAbrirFichero(this.fichero[0],Texto.get("VVC_VEREDITAR",Conf.idioma),
							null,"java",Texto.get("ARCHIVO_JAVA",Conf.idioma),1);
		}
			
		if (fichero[1]!=null)
		{
			try {
				FileReader fileStream = new FileReader( this.fichero[0]+this.fichero[1]);
				areatexto.read ( fileStream,this.fichero[1] );
				fileStream.close();
				nombre.setText(this.fichero[1]);
			} catch (FileNotFoundException fnfe) {
				System.out.println("Archivo '"+this.fichero[1]+"' no encontrado en\n  "+this.fichero[0]);
				abortar=true;
			} catch (IOException ioe) {
				System.out.println("Error IO");
				abortar=true;
			}
			if(!abortar && inicio)
				ventana.setVisible(true);
			else if (abortar)
				new CuadroError(Ventana.thisventana,Texto.get("ERROR_ARCH",Conf.idioma),
									Texto.get("ERROR_ARCHNE",Conf.idioma));
		}
		else
		{
			fichero[0]=ant0;
			fichero[1]=ant1;
		}
	}
	
	
	private synchronized void llamadaCompilacion()
	{
		
		try {
			FileWriter fileStream = new FileWriter( this.fichero[0]+this.fichero[1]);
			fileStream.write(areatexto.getText());
			fileStream.close();
		} catch (IOException ioe) {
			System.out.println("Error IO");
			abortar=true;
		}
	
		etiqueta.setText(Texto.get("VVC_COMPILANDO",Conf.idioma)+" "+fichero[1]+"...");
		Runtime runtime = Runtime.getRuntime();
		String ficheroclass = fichero[1].replace(".java",".class");
		try { wait(100); } catch(InterruptedException ie) {}

		File file=new File(fichero[0]+ficheroclass);
		file.delete();
		
		omvj=(OpcionMVJava)gOpciones.getOpcion("OpcionMVJava",true);

		Process pr=null;
		try {
			pr=runtime.exec("\""+omvj.getDir()+"javac\" \""+fichero[0]+fichero[1]+"\"");
		} catch (IOException ioe) {
			System.out.println("Error ejecucion");
		}

		boolean compilado=true;
		String mensajeErrorCompilacion=null;
		byte[] bytes=new byte[1];
		InputStream is=pr.getErrorStream();
		int x=0;
		do
		{
			try {
				x=is.read(bytes);
			} catch (java.io.IOException ioe) {
				System.out.println("ioe");
			}
			mensajeErrorCompilacion=mensajeErrorCompilacion+(new String(bytes));
			
			if (x>0)
				compilado=false;
		}
		while (x>0);
		
		etiqueta.setText(" ");
		if (compilado)
		{
			textPane.setText("Compilation with no errors.");
		}
		else
		{
			textPane.setText(mensajeErrorCompilacion.substring(4,mensajeErrorCompilacion.length()));
		}
		compilar.setEnabled(true);
	}
	
	
	private int numLinea(String texto)
	{
		if ( ( texto.indexOf("java:") )>=0 && ( texto.indexOf("java:")+5 )<texto.length())
		{
			String t=texto.substring( texto.indexOf("java:")+5,texto.length());
			
			if (t.indexOf(":")!=-1)
			{
				t=t.substring(0,t.indexOf(":"));
			}
			int x=-1;
			try {
				x=Integer.parseInt(t);
			} catch (Exception e) {
				return -1;
			}
			return x;
			}
		return -1;
	}
	
	
	
	private void actualizarBandaNumLinea()
	{
		String texto=areatexto.getText();
		
		
		
		int numLineas=1;
		while (texto.contains("\n"))
		{
			texto=texto.substring(texto.indexOf("\n")+1,texto.length());
			numLineas++;
		}
		
		String textoNum="";
		int contador=1;
		
		int numCifrasTotal=(""+numLineas).length();
		
		
		while (numLineas>0)
		{
			int numCifras=(""+contador).length();
			String cadenaEspacios="";
			for (int i=0; i<numCifrasTotal-numCifras; i++)
			{
				cadenaEspacios=cadenaEspacios+" ";
			}
			
			
			
			textoNum=textoNum+" "+cadenaEspacios+contador+" \n";
			contador++;
			numLineas--;
		}
		
		
		areaNum.setText(textoNum);
		areaNum.updateUI();
	}
	
	public void preguntaEdicionNoGuardada()
	{
		/*if (guardar.isEnabled())
			new CuadroPreguntaEdicionNoGuardada(this,null);
		else
			cerrarVentana();*/
	}
	
	
	public JFrame getVentana()
	{
		return this.ventana;
	}
	
	public void cerrarVentana()
	{
		this.ventana.dispose();
	}
}
