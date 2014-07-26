/**
	Esta clase constituye la ventana principal de la aplicación.

	@author Luis Fernández y Antonio Pérez Carrasco
	@version 2006-2007

*/
package ventanas;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;





import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.OutOfMemoryError;
import java.net.InetAddress;


import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import javax.swing.plaf.metal.MetalBorders;
//import javax.swing.plaf.metal.MetalBorders;

import javax.swing.UIManager;



import conf.*;
import cuadros.*;
import datos.*;
import paneles.*;
import opciones.*;
import utilidades.*;

public class Ventana extends JFrame implements ActionListener//, ComponentListener
{
	static final long serialVersionUID=00;
	
	// Instancia única
	public static Ventana thisventana=null;
	
	public boolean msWindows=false;	// 1=Microsoft Windows 0= otros
	public static final boolean depurar=false;				// Depuracion

	boolean animacionPendienteGuardar=false;
	boolean clasePendienteGuardar=false;
	boolean clasePendienteProcesar=false;
	
	//Logger logger=new Logger();
	
	boolean interfazIniciada=false;	// A true durante varios segundos para implementar antirrebotes en botones
	
	PanelVentana panelVentana;
	
	Preprocesador p=null;
	
	// Algoritmos que contiene en cada momento la ventana
	String titulosPaneles[]=null;
	String idTrazas[]=null;
	
	public Traza traza=null;
	public Traza trazaCompleta=null;
	
	ClaseAlgoritmo claseAlgoritmo=null;		// Nos ayuda a crear animaciones
	boolean claseHabilitada=false;			// A true si la clase cargada permite generar animaciones (correcta y con métodos visualizables)	
	
	
	DatosTrazaBasicos dtb;				// Nos ayuda a almacenar/cargar animaciones con información básica recopilada de la traza
	
	
	String fichTraza[];
	
	String ficheroGIF[]; 
	
	// Menús de la ventana
	JMenuBar barramenu;

	
	JMenu[] menus = new JMenu[8];
		// 0 = menuFichero
		// 1 = menuVisualizacion
		// 3 = menuConfiguracion
		// 4 = menuAyuda
		// 5 = menuFiltradoYSeleccion
		// 6 = menuArbolDeRecursion
		// 7 = menuTraza
	
	String textos[];
	
	String codigos [];
	
	
	// Herramientas
	JToolBar[] barrasHerramientas;
	JButton botones[];
	
	GestorOpciones gOpciones= new GestorOpciones();
	
	boolean barraHerramientasVisible=true;
	
	static String icono="./imagenes/ico32.gif";

	static JFrame ventana;
	
	static String classpathOriginal="";
	
	static GestorVentanaSRec gestorVentana=new GestorVentanaSRec();
	
	
	public int []tamPantalla;
	
	static private boolean procesando=false;
			// A true cuando la aplicación se encuentra en medio de algo:
			// 	-procesando una clase
			//	-generando una nueva visualización -ESTO NO ESTÁ EN USO AÚN-
			//	-...
	
	
	

	/**
		Constructor: crea una nueva instancia de la ventana de la aplicación.
	*/	
	public Ventana() 
	{
		if (thisventana==null)
			thisventana=this;
		else
			System.exit( 0 );
			
		// Detectamos tamaño de la pantalla de salida
		Conf.setTamanoMonitor();

		if (!gOpciones.existeArchivoOpciones())
			gOpciones.crearArchivo();
		if (!gOpciones.existeArchivoOpcionesPorDefecto())
			gOpciones.crearArchivoPorDefecto();
		
		ManipulacionElement.copiarXML(GestorOpciones.getNombreArchivoOpDefecto(),GestorOpciones.getNombreArchivoOpciones());
		
		Conf.setValoresOpsVisualizacion(true);
		Conf.setValoresVisualizacion();
		Conf.setConfiguracionIdioma();
		Conf.setConfiguracionVistas();

		new CuadroIntro(this);
		borrarArchivosInservibles();
	
		Conf.setFicheros();
		if (Conf.fichero_log)
			log_open();
		
		// Cerramos salida por terminal, así no se escapa nada ;)
		//System.out.close();
		//System.err.close();
		//System.out.println("Salida estandar abierta.\n\n");
		
		
		// Cargamos estos textos ahora para no tener que hacer 30 cargados cada vez que pulsamos un botón de menú
		String codigos2[]= 	
		{
			"MENU_ARCH_00", "MENU_ARCH_03", "MENU_ARCH_02", "MENU_ARCH_04",	"MENU_ARCH_05",		//  0 a  4
			"MENU_ARCH_06", "MENU_ARCH_13", "MENU_ARCH_07", "MENU_ARCH_12",	"MENU_ARCH_09",		//  5 a  9  
			"MENU_ARCH_11",	"MENU_ARCH_10",	"MENU_ARCH_08",	"MENU_INFO_01",	"MENU_INFO_02",		// 10 a 14
			"MENU_INFO_03",	"MENU_INFO_04",	"MENU_VISU_02", "MENU_VISU_03", "MENU_VISU_04",		// 15 a 19
			"MENU_VISU_05", "MENU_VISU_06", "MENU_VISU_07", "MENU_VISU_08", "MENU_VISU_09",		// 20 a 24
			"MENU_VISU_10",	"MENU_VISU_11",	"MENU_VISU_12",	"MENU_VISU_13",	"MENU_VISU_14",		// 25 a 29
			"MENU_VISU_15",	"MENU_VISU_16",	"MENU_CONF_01",	"MENU_CONF_02",	"MENU_CONF_03",		// 30 a 34
			"MENU_AYUD_01",	"MENU_AYUD_02", "MENU_CONF_04", "MENU_CONF_05", "MENU_VISU_17",		// 35 a 39
			"MENU_ARCH_14", "MENU_ARCH_15", "MENU_FILT_00", "MENU_FILT_01", "MENU_FILT_02", 	// 40 a 44
			"MENU_FILT_03", "MENU_FILT_04", "MENU_FILT_05", "MENU_ARBL_00", "MENU_ARBL_01", 	// 45 a 49
			"MENU_ARBL_02", "MENU_TRAZ_00", "MENU_VISU_19", "MENU_TRAZ_02", "MENU_TRAZ_03",		// 50 a 54	
			"MENU_TRAZ_04", "MENU_VISU_18", "MENU_ARBL_03", "MENU_ARBL_04"						// 55 a 58
		};
		
		codigos=codigos2;
	
		textos=Texto.get(codigos,Conf.idioma);
		
		for (int i=0; i<textos.length; i++)	// Limpiamos los textos de los elementos del submenú del menú Animación
			if (textos[i].contains("_"))
				textos[i]=textos[i].substring(0,textos[i].indexOf("_"));
		

		// Aplicamos, si podemos, el look and feel de Windows
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

			msWindows=true;	// debe asignar true
			Conf.anchoBoton=79;
			Conf.altoBoton=23;
		} catch (Exception evt1) {
		}
		
		// Colocamos icono para la ventana
		this.setIconImage( new ImageIcon(icono).getImage() );
		
		//OpcionConfVisualizacion ocv=(OpcionConfVisualizacion)gOpciones.getOpcion("OpcionConfVisualizacion");
		
		//menus
		GestorVentanaSRec.crearMenu(menus);

		this.setLayout(new BorderLayout());
	
		this.setTitle(this.getTituloGenerico());
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Creamos la barra de herramientas
		
		JPanel pHerr=new JPanel();
		pHerr.setLayout(new BorderLayout());
		
		JPanel p=new JPanel();
		
		this.barrasHerramientas= GestorVentanaSRec.creaBarrasHeramientas(this.botones);
		
		for (int i=0; i<this.barrasHerramientas.length; i++)
			p.add(barrasHerramientas[i]);
		
		pHerr.add(p,BorderLayout.WEST);
		
		this.habilitarOpcionesDYV(false);  //Deshabilitamos las opciones que dependen de si la clase cargada es DYV o no
		
		//this.add(this.barraHerramientas,BorderLayout.NORTH);
		this.add(pHerr,BorderLayout.NORTH);
		
		// Panel de la ventana, que contendrá otros paneles para visualizaciones, etc.
		panelVentana = new PanelVentana();
		//activarCheckBoxMenuItem("1 subventana");
		this.add(panelVentana,BorderLayout.CENTER);

		
		// Ubicamos la ventana según la resolución de pantalla configurada
		tamPantalla=Conf.getTamanoMonitor();
		int []tamVentana=redimensionarVentana(tamPantalla);
		int []ubicacionVentana=Conf.ubicarCentro(tamVentana[0],tamVentana[1]);
		this.setLocation(ubicacionVentana[0],ubicacionVentana[1]);
		this.setSize(tamVentana[0], tamVentana[1]);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);	// Maximiza la ventana
		this.setIconImage( new ImageIcon(icono).getImage() );	// icono_srec.gif
		this.setVisible(true);
		
		this.addWindowListener(gestorVentana);
		this.addWindowStateListener(gestorVentana);
	
		//System.out.println(" AnchoxAlto: "+this.getWidth()+"x"+this.getHeight());
		Conf.setTamanoVentana(this.getWidth(),this.getHeight());
		
		// Cargamos opción de máquina virtual
		OpcionMVJava omvj=(OpcionMVJava)gOpciones.getOpcion("OpcionMVJava",true);
		if (!omvj.getValida())
		{
			String maquinas[]=BuscadorMVJava.buscador(true);
			//System.out.println("VentanaVisualizador: ");
			//for (int i=0; i<maquinas.length; i++)
			//	System.out.println(maquinas[i]);
			
			int contadorMaquinas=0;
			while (!omvj.getValida() && contadorMaquinas<maquinas.length)
			{
				omvj.setDir(maquinas[contadorMaquinas]);
				//System.out.println("Insertamos "+ maquinas[contadorMaquinas]);
				gOpciones.setOpcion(omvj, 2);
				contadorMaquinas++;
			}
			
			if (!omvj.getValida())
			{
				// A true para indicar que cargue un texto más explicativo para la primera ejecución.
				CuadroOpcionMVJava comvj = new CuadroOpcionMVJava(this, true);
			}
		}
		
	}
	
	public static Ventana getInstance() {
		return thisventana;
	}
	
	/**
		Gestiona todos los eventos relacionados con acciones.
		
		@param Evento de acción
	*/
	public synchronized void actionPerformed(ActionEvent e)
	{
		// si está en proceso algo (procesar clase, nueva visualización...)
		//if (procesando)		COMENTADO PORQUE NO FUNCIONA BIEN SI EL PROCESO NO ACABA queda en true)
		//	return;
		panelVentana.requestFocus();
		Object fuente=e.getSource();
		
		
		// Si no acabamos de pulsar un botón o bien el botón ahora pulsado ahora es de manejo directo de Zoom o de guardado
		if (this.interfazIniciada)
			return;
	
		if (fuente!=botones[2] && fuente!=botones[21] && fuente!=botones[22] && fuente!=botones[23] &&
				fuente!=botones[24] && fuente!=botones[25] && fuente!=botones[26] && !(fuente instanceof JMenuItem))
			this.interfazIniciada=true;
		
		new Thread(){
			public synchronized void run()
			{
				try {wait(2000);} catch (java.lang.InterruptedException ie) {}
				Ventana.thisventana.interfazIniciada=false;
			}
		}.start();
		
		


		// Si se ha pulsado un item de menú...
		if (fuente instanceof JMenuItem || fuente instanceof JCheckBoxMenuItem)
		{
			// Botones Menú Archivo //////////////
			String textoFuente=((JMenuItem)fuente).getText();
			
			// Archivo > Nueva clase
			if (  textoFuente.equals(textos[0]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Nueva clase");
				if (panelVentana.haySitio())
					new CuadroNuevaClase(this);
				else
					new CuadroPreguntaNuevaVisualizacion(this,"crear");
			}
			
			
			// Archivo > Cargar y procesar clase...
			else if ( textoFuente.equals(textos[1]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Cargar y procesar clase...");
				if (this.clasePendienteGuardar)
					new CuadroPreguntaEdicionNoGuardada(this, "cargarClase");
				else
					gestionOpcionCargarClase();
			}
			
			// Archivo > Guardar clase...
			else if ( textoFuente.equals(textos[2]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Guardar clase...");
				if (this.getClase()!=null) {
					guardarClase();
					this.clasePendienteProcesar=true;
				}
			}
			

			// Archivo > Procesar clase...
			else if ( textoFuente.equals(textos[3]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Procesar clase...");
				if (this.claseAlgoritmo!=null)
				{
					guardarClase();
					this.clasePendienteGuardar=false;
					if (panelVentana.haySitio()) {
						new Preprocesador(this.claseAlgoritmo.getPath(),this.claseAlgoritmo.getNombre()+".java");
						this.clasePendienteProcesar=false;
					}
					else
						new CuadroPreguntaNuevaVisualizacion(this,"procesar de nuevo");
				}
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOARCHPROC",Conf.idioma));
			}
			
			// Archivo > Seleccionar método
			else if ( textoFuente.equals(textos[40]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Seleccionar método...");
				if (this.claseAlgoritmo!=null) {
					if (this.clasePendienteGuardar||this.clasePendienteProcesar) {
						new CuadroPreguntaEdicionNoGuardada(this, "guardar");
					}
					else {
						iniciarNuevaVisualizacionSelecMetodo();
					}
				}
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOARCHPROC",Conf.idioma));
			}
			
			// Archivo > Asignar parámetros y lanzar animación
			else if ( textoFuente.equals(textos[41]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Asignar parámetros y lanzar animación.");
				if (this.claseAlgoritmo!=null) {
					introducirParametros();
				}
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOARCHPROC",Conf.idioma));
			}
			
			/*
			// Archivo > Nueva animación
			else if ( textoFuente.equals(textos[4]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Nueva animación...");
				if (this.claseAlgoritmo!=null)
					if (panelVentana.haySitio())
						iniciarNuevaVisualizacion();
					else
						new CuadroPreguntaNuevaVisualizacion(this,"nueva");
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOARCHPROC",Conf.idioma));
			}
			*/
			
			// Archivo > Cargar animación
			else if ( textoFuente.equals(textos[5]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Cargar animación...");
				if (this.clasePendienteGuardar)
					new CuadroPreguntaEdicionNoGuardada(this, "cargarAnimacion");
				else
					gestionOpcionCargarAnimacion();

			}
			
			// Archivo > Cargar animación GIF
			else if ( textoFuente.equals(textos[6]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Cargar animación GIF...");
				if (this.clasePendienteGuardar)
					new CuadroPreguntaEdicionNoGuardada(this, "cargarAnimacionGIF");
				else
					gestionOpcionCargarAnimacionGIF();
			}
			
			// Archivo > Guardar animación
			else if ( textoFuente.equals(textos[7]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Guardar animación...");
				if (this.panelOcupado())
				{
					this.trazaCompleta.actualizarEstadoTrazaCompleta(this.traza);
					this.trazaCompleta.setVisibilidad(this.dtb);
					
					new AlmacenadorTraza(this.traza, this.trazaCompleta, this.dtb);
				}
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOVISGUARD",Conf.idioma));
			}
			// Archivo > Guardar traza
			else if ( textoFuente.equals(textos[8]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Guardar traza...");
				guardarTraza();
			}
			
			// Archivo > Captura de animación
			else if ( textoFuente.equals(textos[9]))
			{
				if (Conf.fichero_log) log_write("Archivo > "+textos[9]+"...");
				new CuadroVistasDisponibles(this, this.panelVentana,1);
			}
			
			// Archivo > Capturas de imagen de cada paso
			else if ( textoFuente.equals(textos[10]))
			{
				if (Conf.fichero_log) log_write("Archivo > "+textos[10]+"...");
				new CuadroVistasDisponibles(this, this.panelVentana,2);
			}	
			
			// Archivo > Captura de imagen
			else if ( textoFuente.equals(textos[11]))
			{
				if (Conf.fichero_log) log_write("Archivo > "+textos[11]+"...");
				new CuadroVistasDisponibles(this, this.panelVentana,3);
			}
			
			// Archivo > Salir
			else if ( textoFuente.equals(textos[12]) )
			{
				if (Conf.fichero_log) log_write("Archivo > Cerrar");
				if (depurar) System.out.println("VentanaVisualizador > Menú Archivo > Salir");
				cerrar();
			}


			
			
			// Botones Menú Visualización //////////////
			

			// Visualización > Entrada y salida...
			/*
			else if ( textoFuente.equals(textos[17]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Entrada y salida...");
				new CuadroElegirES();
			}
			*/
			
			// Visualización > Visibilidad de métodos y parámetros...
			/*
			else if ( textoFuente.equals(textos[18]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Visibilidad de métodos y parámetros...");
				new CuadroVisibilidad(dtb);
			}
			*/

			// Visualización > Nodos históricos...
			/*
			else if ( textoFuente.equals(textos[19]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Nodos históricos...");
				new CuadroElegirHistorico();
			}
			*/
			
			// Visualización > Mostrar subárboles en saltos
			/*
			else if ( textoFuente.equals(textos[20]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Mostrar subárboles en saltos");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarArbolSalto( !oov.getMostrarArbolSalto() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Mostrar subárboles en saltos: "+oov.getMostrarArbolSalto());
				actualizarVisualizacion();
			}
			*/
			
			// Visualización > Mostrar visor de navegación
			/*
			else if ( textoFuente.equals(textos[21]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Mostrar visor de navegación");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarVisor( !oov.getMostrarVisor() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Mostrar visor de navegación: "+oov.getMostrarVisor());
				actualizarVisualizacion();
			}
			*/
			
			// Visualización > Mostrar estructura en vista de árbol
			/*
			else if ( textoFuente.equals(textos[22]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Mostrar estructura en vista de árbol");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarEstructuraEnArbol( !oov.getMostrarEstructuraEnArbol() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Mostrar estructura en vista de árbol: "+oov.getMostrarEstructuraEnArbol());
				actualizarVisualizacion();
			}
			*/
			
			// Visualización > Mostrar estructura completa en vista cronológica
			/*
			else if ( textoFuente.equals(textos[23]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Mostrar estructura completa en vista cronológica");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarEstructuraCompletaCrono( !oov.getMostrarEstructuraCompletaCrono() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Mostrar estructura completa en vista cronológica: "+oov.getMostrarEstructuraCompletaCrono());
				actualizarVisualizacion();
			}
			*/
			
			// Visualización > Mostrar salida ligada a entrada en vista cronológica
			/*
			else if ( textoFuente.equals(textos[24]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Mostrar salida ligada a entrada en vista cronológica");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarSalidaLigadaEntrada( !oov.getMostrarSalidaLigadaEntrada() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Mostrar salida ligada a entrada en vista cronológica: "+oov.getMostrarSalidaLigadaEntrada());
				actualizarVisualizacion();
			}
			*/
			
			// Visualización > Mostrar árboles colapsados
			/*
			else if ( textoFuente.equals(textos[39]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Mostrar árboles colapsados");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarArbolColapsado( !oov.getMostrarArbolColapsado() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Mostrar árboles colapsados: "+oov.getMostrarArbolColapsado());
				actualizarVisualizacion();
			}
			*/
			
			// Visualización > Arranque de animación en estado inicial
			else if ( textoFuente.equals(textos[56]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Arranque de animación en estado inicial");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setArranqueEstadoInicial( !oov.getArranqueEstadoInicial() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Arranque de animación en estado inicial: "+oov.getArranqueEstadoInicial());
				if (oov.getArranqueEstadoInicial())
					GestorVentanaSRec.iconoMenuItem(menus[1], Texto.get("MENU_VISU_18",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_estadoInicial.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[1], Texto.get("MENU_VISU_18",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_estadoInicial_des.gif");
				actualizarVisualizacion();
			}
			
			// Visualización > Identificador de método
			else if ( textoFuente.equals(textos[52]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Identificador de método");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setIdMetodoTraza( !oov.getIdMetodoTraza() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Identificador de método: "+oov.getIdMetodoTraza());
				if (oov.getIdMetodoTraza())
					GestorVentanaSRec.iconoMenuItem(menus[1], Texto.get("MENU_VISU_19",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_idMetodo.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[1], Texto.get("MENU_VISU_19",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_idMetodo_des.gif");
				actualizarVisualizacion();
			}
			
			// Visualización > Formato de animación...
			else if ( textoFuente.equals(textos[25]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Formato de animación...");
				new CuadroOpcionConfVisualizacion(this);
			}

			// Visualización > Configuración de Zoom...			
			else if ( textoFuente.equals(textos[26]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Configuración de Zoom...");
				if (this.panelOcupado())
					new CuadroZoom(this, panelVentana);
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOVISZOOM",Conf.idioma));
			}
			
			// Visualización > Ubicación de paneles			
			else if ( textoFuente.equals(textos[27]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Ubicación de paneles");
				new CuadroOpcionVistas(this);
			}
			
			
			// Botones Menú Filtrado y Selección //////////////
			
			// Filtrado > Datos de entrada y salida...
			else if ( textoFuente.equals(textos[42]) )
			{
				if (Conf.fichero_log) log_write("Filtrado y selección > Datos de entrada y salida...");
				new CuadroElegirES(this);
			}
			
			// Filtrado > Métodos y parámetros...
			else if ( textoFuente.equals(textos[43]) )
			{
				if (Conf.fichero_log) log_write("Filtrado y selección > Métodos y parámetros...");
				new CuadroVisibilidad(this, dtb);
			}
			
			// Filtrado > Llamadas terminadas...
			else if ( textoFuente.equals(textos[44]) )
			{
				if (Conf.fichero_log) log_write("Filtrado y selección > Llamadas terminadas...");
				new CuadroElegirHistorico(this);
			}
			
			// Filtrado > Subárboles en saltos
			else if ( textoFuente.equals(textos[45]) )
			{
				if (Conf.fichero_log) log_write("Filtrado y selección > Subárboles en saltos");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarArbolSalto( !oov.getMostrarArbolSalto() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Filtrado y selección > Subárboles en saltos: "+oov.getMostrarArbolSalto());
				if (oov.getMostrarArbolSalto())
					GestorVentanaSRec.iconoMenuItem(menus[5], Texto.get("MENU_FILT_03",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_mostrarsubarbol.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[5], Texto.get("MENU_FILT_03",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_mostrarsubarbol_des.gif");
				actualizarVisualizacion();
			}
			
			// Filtrado > Búsqueda de llamadas
			else if ( textoFuente.equals(textos[46]) )
			{
				if (Conf.fichero_log) log_write("Filtrado y selección > Búsqueda de llamadas...");
				new CuadroBuscarLlamada(this, dtb);
			}
			
			// Filtrado > Restauración de llamadas
			else if ( textoFuente.equals(textos[47]) )
			{
				if (Conf.fichero_log) log_write("Filtrado y selección > Restauración de llamadas");
				thisventana.getTraza().iluminar(0, null, null, false);
				thisventana.refrescarFormato();
			}
			
			//  Botones Árbol de Recursión //////////////
			
			// Árbol de recursión > Árboles dinámicos
			else if ( textoFuente.equals(textos[48]) )
			{
				if (Conf.fichero_log) log_write("Árbol de recursión > Árboles dinámicos");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarArbolColapsado( !oov.getMostrarArbolColapsado() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Árbol de recursión > Árboles dinámicos: "+oov.getMostrarArbolColapsado());
				if (oov.getMostrarArbolColapsado())
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_00",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_arbolcolapsado.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_00",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_arbolcolapsado_des.gif");
				actualizarVisualizacion();
			}
			
			// Árbol de recursión > Visualización dinámica
			else if ( textoFuente.equals(textos[58]) )
			{
				if (Conf.fichero_log) log_write("Árbol de recursión > Visualización dinámica");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setVisualizacionDinamica( !oov.getVisualizacionDinamica() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Árbol de recursión > Visualización dinámica: "+oov.getVisualizacionDinamica());
				if (oov.getVisualizacionDinamica())
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_04",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_visualizacionDinamica.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_04",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_visualizacionDinamica_des.gif");
				actualizarVisualizacion();
			}
			
			// Árbol de recursión > Vista global
			else if ( textoFuente.equals(textos[49]) )
			{
				if (Conf.fichero_log) log_write("Árbol de recursión > Vista global");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarVisor( !oov.getMostrarVisor() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Árbol de recursión > Vista global: "+oov.getMostrarVisor());
				if (oov.getMostrarVisor())
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_01",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_mostrarvisor.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_01",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_mostrarvisor_des.gif");
				actualizarVisualizacion();
			}
			
			// Árbol de recursión > Ajustar vista global
			else if ( textoFuente.equals(textos[57]) )
			{
				if (Conf.fichero_log) log_write("Árbol de recursión > Ajustar vista global");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setAjustarVistaGlobal( !oov.getAjustarVistaGlobal() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Árbol de recursión > Ajustar vista global: "+oov.getAjustarVistaGlobal());
				if (oov.getAjustarVistaGlobal())
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_03",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_ajustarVisor.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_03",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_ajustarVisor_des.gif");
				actualizarVisualizacion();
			}
			
			// Árbol de recursión > Estructura de datos en DYV
			else if ( textoFuente.equals(textos[50]) )
			{
				if (Conf.fichero_log) log_write("Árbol de recursión > Estructura de datos en DYV");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarEstructuraEnArbol( !oov.getMostrarEstructuraEnArbol() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Árbol de recursión > Estructura de datos en DYV: "+oov.getMostrarEstructuraEnArbol());
				if (oov.getMostrarEstructuraEnArbol())
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_02",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_estructuraarbol.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_02",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_estructuraarbol_des.gif");
				actualizarVisualizacion();
			}
			
			//  Botones Traza //////////////
			
			// Traza > Sangrado
			else if ( textoFuente.equals(textos[51]) )
			{
				if (Conf.fichero_log) log_write("Traza > Sangrado");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setSangrado( !oov.getSangrado() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Traza > Sangrado: "+oov.getSangrado());
				if (oov.getSangrado())
					GestorVentanaSRec.iconoMenuItem(menus[7], Texto.get("MENU_TRAZ_00",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_sangrado.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[7], Texto.get("MENU_TRAZ_00",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_sangrado_des.gif");
				actualizarVisualizacion();
			}
			
			// Traza > Orden cronológico
			else if ( textoFuente.equals(textos[53]) )
			{
				if (Conf.fichero_log) log_write("Traza > Orden cronológico");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarSalidaLigadaEntrada( !oov.getMostrarSalidaLigadaEntrada() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Traza > Orden cronológico: "+!oov.getMostrarSalidaLigadaEntrada());
				if (!oov.getMostrarSalidaLigadaEntrada())
					GestorVentanaSRec.iconoMenuItem(menus[7], Texto.get("MENU_TRAZ_02",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_ligarescrono.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[7], Texto.get("MENU_TRAZ_02",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_ligarescrono_des.gif");
				actualizarVisualizacion();
			}
			
			// Traza > Solo estructura de datos en DYV
			else if ( textoFuente.equals(textos[54]) )
			{
				if (Conf.fichero_log) log_write("Traza > Solo estructura de datos en DYV");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setSoloEstructuraDYVcrono( !oov.getSoloEstructuraDYVcrono());
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Traza > Solo estructura de datos en DYV: "+oov.getSoloEstructuraDYVcrono());
				if (oov.getSoloEstructuraDYVcrono())
					GestorVentanaSRec.iconoMenuItem(menus[7], Texto.get("MENU_TRAZ_03",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_soloestructuraprincipal.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[7], Texto.get("MENU_TRAZ_03",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_soloestructuraprincipal_des.gif");
				actualizarVisualizacion();
			}
			
			// Traza > Estructura de datos completa en DYV
			else if ( textoFuente.equals(textos[55]) )
			{
				if (Conf.fichero_log) log_write("Traza > Estructura de datos completa en DYV");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarEstructuraCompletaCrono( !oov.getMostrarEstructuraCompletaCrono() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Traza > Estructura de datos completa en DYV: "+oov.getMostrarEstructuraCompletaCrono());
				if (oov.getMostrarEstructuraCompletaCrono())
					GestorVentanaSRec.iconoMenuItem(menus[7], Texto.get("MENU_TRAZ_04",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_estructcompletacrono.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[7], Texto.get("MENU_TRAZ_04",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_estructcompletacrono_des.gif");
				actualizarVisualizacion();
			}
			
			
			// Botones Menú Información //////////////
			
			// Información > Información traza...
			else if ( textoFuente.equals(textos[13]) )
			{
				if (Conf.fichero_log) log_write("Información > Información traza...");
				new CuadroInfoTraza(this, traza, trazaCompleta);
			}
			
			// Información > Información nodo actual...
			else if ( textoFuente.equals(textos[14]) )
			{
				if (Conf.fichero_log) log_write("Información > Información nodo actual...");
				new CuadroInfoNodo(Ventana.thisventana, Ventana.thisventana.traza);
			}
			
			// Información > Buscar llamada...
			/*
			else if ( textoFuente.equals(textos[15]) )
			{
				if (Conf.fichero_log) log_write("Información > Buscar llamada...");
				new CuadroBuscarLlamada(dtb);
			}
			*/
			
			// Información > Restaurar llamadas
			/*
			else if ( textoFuente.equals(textos[16]) )
			{
				if (Conf.fichero_log) log_write("Información > Restaurar llamadas");
				thisventana.getTraza().iluminar(0, null, null, false);
				thisventana.refrescarFormato();
			}
			*/
			
			
			
			
			// Botones Menú Configuración //////////
			
			// Configuración > Archivo LOG...
			else if ( textoFuente.equals(textos[38]) )
			{
				if (Conf.fichero_log) log_write("Configuración > Archivo LOG...");
				GestorOpciones gOpciones=new GestorOpciones();
				OpcionBorradoFicheros obf=(OpcionBorradoFicheros)gOpciones.getOpcion("OpcionBorradoFicheros",false);
				obf.setLOG( !obf.getLOG() );
				gOpciones.setOpcion(obf,2);
				Conf.setFicheros();
				if (Conf.fichero_log)
					log_open();
				else
					log_close();
			}
			
			// Configuración > Archivos intermedios...
			else if ( textoFuente.equals(textos[32]) )
			{
				if (Conf.fichero_log) log_write("Configuración > Archivos intermedios...");
				new CuadroOpcionBorradoFicheros(this);
			}

			// Configuración > Máquina Virtual Java...
			else if ( textoFuente.equals(textos[33]) )
			{
				if (Conf.fichero_log) log_write("Configuración > Máquina Virtual Java...");
				new CuadroOpcionMVJava(this, false);
			}

			// Configuración > Idioma...
			else if ( textoFuente.equals(textos[34]) )
			{
				if (Conf.fichero_log) log_write("Configuración > Idioma...");
				new CuadroIdioma(this);
			}

			// Configuración > Mostrar/ocultar barra herramientas
			else if ( textoFuente.equals(textos[37]) )
			{
				if (Conf.fichero_log) log_write("Configuración > Mostrar/ocultar barra herramientas");
				GestorVentanaSRec.setVisibleBarraHerramientas(this.barrasHerramientas,!this.barraHerramientasVisible,true);
			}
			

			// Visualización > Restaurar configuración
			else if ( textoFuente.equals(textos[28]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Restaurar configuración");
				gOpciones.cargarArchivoPorDefecto();
				Conf.setValoresOpsVisualizacion(true);
				Conf.setValoresVisualizacion();
				panelVentana.refrescarFormato();
				panelVentana.refrescarOpciones();
				GestorVentanaSRec.activaChecks(menus);
			}
			
			// Visualización > Cargar configuración...		
			else if ( textoFuente.equals(textos[29]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Cargar configuración...");
				OpcionFicherosRecientes ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);
				String fich[]=SelecDireccion.cuadroAbrirFichero(ofr.getDir(),Texto.get("CA_CARCONF",Conf.idioma),
							null,"xml",Texto.get("ARCHIVO_XML",Conf.idioma),1);
	
				// *1* Comprobar si fichero existe
				
				if (fich[1]!=null)
				{
					File f=new File(fich[0]+fich[1]);
					if (f.exists())
					{
						if (gOpciones.cargarArchivo(fich[0]+fich[1]))
						{
							Conf.setValoresOpsVisualizacion(false);
							Conf.setValoresVisualizacion();
							panelVentana.refrescarFormato();
							panelVentana.refrescarOpciones();
						}
						else
							new CuadroError(this, Texto.get("ERROR_ARCH",Conf.idioma) , Texto.get("ERROR_ARCHCOR",Conf.idioma) );
					}
					else
						new CuadroError(this, Texto.get("ERROR_ARCH",Conf.idioma) , Texto.get("ERROR_ARCHNE",Conf.idioma) );
				}
				// Si estamos visualizando algo, habría que actualizar...
			}
			
			// Visualización > Guardar configuración...
			else if ( textoFuente.equals(textos[30]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Guardar configuración...");
				guardarConfiguracionOpciones();
			}
			
			// Visualización > Guardar configuración por defecto...
			else if ( textoFuente.equals(textos[31]) )
			{
				if (Conf.fichero_log) log_write("Visualización > Guardar configuración por defecto...");
				gOpciones.crearArchivoPorDefecto();//GestorOpciones.getNombreArchivoOpDefecto());
			}

			
			
			// Botones Menú Ayuda /////////
			
			// Ayuda > Temas de ayuda...
			else if ( textoFuente.equals(textos[35]) )
			{
				if (Conf.fichero_log) log_write("Ayuda > Temas de ayuda...");
				new VisorAyuda(Conf.idioma+"_index.html");
			}
			
			// Ayuda > Sobre SRec
			else if ( textoFuente.equals(textos[36]) )
			{
				if (Conf.fichero_log) log_write("Ayuda > Sobre SRec");
				new CuadroAcercade(this);
			}

			// JMenuItem no reconocido
			else
			{
				if (Conf.fichero_log) log_write("Elemento de menu no reconocido");
				System.out.println("VentanaVisualizador > JMenuItem no reconocido ["+textoFuente+"]");
			}
		}
		
		
	
		
		// Si se ha pulsado un botón de la barra de herramientas...
		else if (fuente instanceof JButton)
		{
			
			
			if (fuente==botones[0])			// Archivo > Nueva clase
			{
				if (Conf.fichero_log) log_write("Botón: Nueva clase");
				new CuadroNuevaClase(this);
			}
			
			else if(fuente==botones[1])		// Archivo > Cargar y procesar clase
			{
				if (Conf.fichero_log) log_write("Botón: Cargar y procesar clase");
				if (this.clasePendienteGuardar)
					new CuadroPreguntaEdicionNoGuardada(this, "cargarClase");
				else
					gestionOpcionCargarClase();
			}
			
			else if(fuente==botones[2])	// Archivo > Guardar clase
			{
				if (Conf.fichero_log) log_write("Botón: Guardar clase");
				if (this.getClase()!=null) {
					guardarClase();
					this.clasePendienteProcesar=true;
				}
			}
			
			else if(fuente==botones[3])		// Archivo > Procesar clase
			{
				if (Conf.fichero_log) log_write("Botón: Procesar clase");
				if (this.getClase()!=null && this.clasePendienteGuardar) {
					guardarClase();
					this.clasePendienteGuardar=false;
				}
				if (panelVentana.haySitio()) {
					new Preprocesador(this.claseAlgoritmo.getPath(),this.claseAlgoritmo.getNombre()+".java");
					this.clasePendienteProcesar=false;
				}
				else
					new CuadroPreguntaNuevaVisualizacion(this,"procesar de nuevo");
			}
			else if(fuente==botones[28])		// Archivo > Seleccionar Método
			{
				if (Conf.fichero_log) log_write("Botón > Seleccionar método...");
				if (this.claseAlgoritmo!=null) {
					if (this.clasePendienteGuardar||this.clasePendienteProcesar) {
						new CuadroPreguntaEdicionNoGuardada(this, "guardar");
					}
					else {
						iniciarNuevaVisualizacionSelecMetodo();
					}
				}
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOARCHPROC",Conf.idioma));
			}
			else if(fuente==botones[29])		// Archivo > Asignar Parámetros y lanzar animación
			{
				if (Conf.fichero_log) log_write("Botón > Asignar parámetros y lanzar animación.");
				if (this.claseAlgoritmo!=null)
					introducirParametros();
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOARCHPROC",Conf.idioma));
			}
			/*
			else if(fuente==botones[4])		// Archivo > Nueva visualización
			{
				if (Conf.fichero_log) log_write("Botón: Nueva visualización");
				if (this.claseAlgoritmo!=null)
					if (panelVentana.haySitio())
						iniciarNuevaVisualizacion();
					else
						new CuadroPreguntaNuevaVisualizacion(this,"nueva");
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOARCHPROC",Conf.idioma));
			}
			*/
			else if(fuente==botones[5])		// Archivo > Cargar visualización...
			{
				if (Conf.fichero_log) log_write("Botón: Cargar visualización...");
				if (this.clasePendienteGuardar)
					new CuadroPreguntaEdicionNoGuardada(this, "cargarAnimacion");
				else
					gestionOpcionCargarAnimacion();
			}
			else if(fuente==botones[6])		// Archivo > Guardar visualización...
			{
				if (Conf.fichero_log) log_write("Botón: Guardar visualización...");
				if (this.panelOcupado())
				{
					this.trazaCompleta.actualizarEstadoTrazaCompleta(this.traza);
					this.trazaCompleta.setVisibilidad(this.dtb);

					new AlmacenadorTraza(this.traza, this.trazaCompleta, this.dtb);
				}
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOVISGUARD",Conf.idioma));
			}
					
			
			else if ( fuente==botones[7])	// Archivo > Exportar animación GIF...
			{
				if (Conf.fichero_log) log_write("Botón: Exportar animación GIF...");
				new CuadroVistasDisponibles(this, this.panelVentana,1);
			}
			
			else if ( fuente==botones[8])	// Archivo > Exportar capturas de estados
			{
				if (Conf.fichero_log) log_write("Botón: Exportar capturas de estados...");
				new CuadroVistasDisponibles(this, this.panelVentana,2);
			}	
			
			else if ( fuente==botones[9])	// Archivo > Exportar captura de estado actual
			{
				if (Conf.fichero_log) log_write("Botón: Exportar captura de estado actual...");
				new CuadroVistasDisponibles(this, this.panelVentana,3);
			}
			
			
			
			//NUEVOS GRUPOS
			else if(fuente==botones[10])		// Visualización > Datos de entrada y salida...
			{
				if (Conf.fichero_log) log_write("Botón: Datos de Entrada y Salida...");
				new CuadroElegirES(this);
			}
			
			else if(fuente==botones[11])		// Visualización > Visibilidad de métodos y parámetros
			{
				if (Conf.fichero_log) log_write("Botón: Visibilidad de métodos y parámetros...");
				new CuadroVisibilidad(this, dtb);
			}
			
			else if(fuente==botones[12])		// Visualización > Nodos históricos...
			{
				if (Conf.fichero_log) log_write("Botón: Nodos históricos...");
				new CuadroElegirHistorico(this);
			}
			
			else if(fuente==botones[13])		// Visualización > Mostrar subárboles en saltos
			{
				if (Conf.fichero_log) log_write("Botón: Mostrar subárboles en saltos");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarArbolSalto( !oov.getMostrarArbolSalto() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Mostrar subárboles en saltos: "+oov.getMostrarArbolSalto());
				actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(menus);	// La opción de menú es un Check, hay que refrescarlo
			}
			
			else if(fuente==botones[14])		// Visualización > Mostrar visor de navegación
			{
				if (Conf.fichero_log) log_write("Botón: Visibilidad de métodos y parámetros...");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarVisor( !oov.getMostrarVisor() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Mostrar vista global: "+oov.getMostrarVisor());
				actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(menus);	// La opción de menú es un Check, hay que refrescarlo
			}
			
			else if(fuente==botones[15])		// Visualización > Mostrar estructura en la vista de árbol
			{
				if (Conf.fichero_log) log_write("Botón: Mostrar estructura en la vista de árbol");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarEstructuraEnArbol( !oov.getMostrarEstructuraEnArbol() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Mostrar estructura en vista de árbol: "+oov.getMostrarEstructuraEnArbol());
				actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(menus);	// La opción de menú es un Check, hay que refrescarlo
			}
			else if(fuente==botones[16])		// Visualización > Mostrar estructura completa en la vista cronológica
			{
				if (Conf.fichero_log) log_write("Botón: Mostrar estructura completa en vista cronológica");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarEstructuraCompletaCrono( !oov.getMostrarEstructuraCompletaCrono() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Visualización > Mostrar estructura completa en vista cronológica: "+oov.getMostrarEstructuraCompletaCrono());
				actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(menus);	// La opción de menú es un Check, hay que refrescarlo
			}
			
			else if(fuente==botones[17])		// Visualización > Mostrar salida ligada a entrada en vista cronológica
			{
				if (Conf.fichero_log) log_write("Botón: Mostrar salida ligada a entrada en vista cronológica");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarSalidaLigadaEntrada( !oov.getMostrarSalidaLigadaEntrada() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Botón: Mostrar salida ligada a entrada en vista cronológica: "+oov.getMostrarSalidaLigadaEntrada());
				actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(menus);	// La opción de menú es un Check, hay que refrescarlo
			}
			
			else if(fuente==botones[18])		// Visualización > Visibilidad de métodos y parámetros
			{
				if (Conf.fichero_log) log_write("Botón: Mostrar árboles colapsados");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarArbolColapsado( !oov.getMostrarArbolColapsado() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Botón: Mostrar árboles colapsados: "+oov.getMostrarArbolColapsado());
				actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(menus);	// La opción de menú es un Check, hay que refrescarlo
			}
			
			
			// fin nuevos grupos
			
			
			
			

			else if(fuente==botones[19])		// Configuración > Formato de animación
			{
				if (Conf.fichero_log) log_write("Botón: Formato de animación...");
				new CuadroOpcionConfVisualizacion(this);
			}
			else if(fuente==botones[20])		// Configuración > Zoom
			{
				if (Conf.fichero_log) log_write("Botón: Zoom...");
				if (this.panelOcupado())
					new CuadroZoom(this, panelVentana);
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOVISZOOM",Conf.idioma));
			}
			else if(fuente==botones[21])		// Configuración > Zoom+
			{
				if (this.panelOcupado())
				{
					int[] vistasVisiblesAhora=this.panelVentana.getCodigosVistasVisibles();
					if (Conf.fichero_log) log_write("Botón: Zoom+ (panel 1, vista "+Vista.codigos[vistasVisiblesAhora[0]]+")");
					if (vistasVisiblesAhora[0]!=2)
						CuadroZoom.zoomAjuste(this, panelVentana,vistasVisiblesAhora[0],CuadroZoom.MAS5);
				}
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOVISZOOM",Conf.idioma));
			}
			else if(fuente==botones[22])		// Configuración > Zoom-
			{
				if (this.panelOcupado())
				{
					int[] vistasVisiblesAhora=this.panelVentana.getCodigosVistasVisibles();
					if (Conf.fichero_log) log_write("Botón: Zoom- (panel 1, vista "+Vista.codigos[vistasVisiblesAhora[0]]+")");
					if (vistasVisiblesAhora[0]!=2)
						CuadroZoom.zoomAjuste(this, panelVentana,vistasVisiblesAhora[0],CuadroZoom.MENOS5);
				}
					
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOVISZOOM",Conf.idioma));
			}
			else if(fuente==botones[23])		// Configuración > Zoom Ajuste
			{
				if (this.panelOcupado())
				{
					int[] vistasVisiblesAhora=this.panelVentana.getCodigosVistasVisibles();
					if (Conf.fichero_log) log_write("Botón: ZoomAjuste (panel 1, vista "+Vista.codigos[vistasVisiblesAhora[0]]+")");
					if (vistasVisiblesAhora[0]!=2)
						CuadroZoom.zoomAjuste(this, panelVentana,vistasVisiblesAhora[0],CuadroZoom.AJUSTE);
				}
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOVISZOOM",Conf.idioma));
			}
			else if(fuente==botones[24])		// Configuración > Zoom+ (panel 2)
			{
				if (this.panelOcupado())
				{
					int[] vistasVisiblesAhora=this.panelVentana.getCodigosVistasVisibles();
					if (Conf.fichero_log) log_write("Botón: Zoom+ (panel 2, vista "+Vista.codigos[vistasVisiblesAhora[1]]+")");
					if (vistasVisiblesAhora[1]!=2)
						CuadroZoom.zoomAjuste(this, panelVentana,vistasVisiblesAhora[1],CuadroZoom.MAS5);
				}
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOVISZOOM",Conf.idioma));
			}
			else if(fuente==botones[25])		// Configuración > Zoom- (panel 2)
			{
				if (this.panelOcupado())
				{
					int[] vistasVisiblesAhora=this.panelVentana.getCodigosVistasVisibles();
					if (Conf.fichero_log) log_write("Botón: Zoom- (panel 2, vista "+Vista.codigos[vistasVisiblesAhora[1]]+")");
					if (vistasVisiblesAhora[1]!=2)
						CuadroZoom.zoomAjuste(this, panelVentana,vistasVisiblesAhora[1],CuadroZoom.MENOS5);
				}
					
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOVISZOOM",Conf.idioma));
			}
			else if(fuente==botones[26])		// Configuración > Zoom Ajuste (panel 2)
			{
				if (this.panelOcupado())
				{
					int[] vistasVisiblesAhora=this.panelVentana.getCodigosVistasVisibles();
					if (Conf.fichero_log) log_write("Botón: ZoomAjuste (panel 2, vista "+Vista.codigos[vistasVisiblesAhora[1]]+")");
					if (vistasVisiblesAhora[1]!=2)
						CuadroZoom.zoomAjuste(this, panelVentana,vistasVisiblesAhora[1],CuadroZoom.AJUSTE);
				}
				else
					new CuadroError(this,Texto.get("ERROR_VISU",Conf.idioma), Texto.get("ERROR_NOVISZOOM",Conf.idioma));
			}
			else if(fuente==botones[27])		// Configuración > Vistas y paneles
			{
				if (Conf.fichero_log) log_write("Botón: Vistas y paneles");
				new CuadroOpcionVistas(this);
			}
			else if(fuente==botones[30])		// Filtrado y selección > Búsqueda de llamadas...
			{
				if (Conf.fichero_log) log_write("Botón > Búsqueda de llamadas...");
				new CuadroBuscarLlamada(this, dtb);
			}
			else if(fuente==botones[31])		// Filtrado y selección > Restauración de llamadas
			{
				if (Conf.fichero_log) log_write("Botón > Restauración de llamadas");
				thisventana.getTraza().iluminar(0, null, null, false);
				thisventana.refrescarFormato();
			}
			else if(fuente==botones[32])		// Traza > Orden cronológico
			{
				if (Conf.fichero_log) log_write("Botón > Orden cronológico");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarSalidaLigadaEntrada( !oov.getMostrarSalidaLigadaEntrada() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Botón > Orden cronológico: "+!oov.getMostrarSalidaLigadaEntrada());
				if (!oov.getMostrarSalidaLigadaEntrada())
					GestorVentanaSRec.iconoMenuItem(menus[7], Texto.get("MENU_TRAZ_02",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_ligarescrono.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[7], Texto.get("MENU_TRAZ_02",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_ligarescrono_des.gif");
				actualizarVisualizacion();
			}
			else if(fuente==botones[33])		// Árbol de recursión > Estructura de datos en DYV
			{
				if (Conf.fichero_log) log_write("Botón > Estructura de datos en DYV");
				OpcionOpsVisualizacion oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
				oov.setMostrarEstructuraEnArbol( !oov.getMostrarEstructuraEnArbol() );
				gOpciones.setOpcion(oov,1);
				if (Conf.fichero_log) log_write("Botón > Estructura de datos en DYV: "+oov.getMostrarEstructuraEnArbol());
				if (oov.getMostrarEstructuraEnArbol())
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_02",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_estructuraarbol.gif");
				else
					GestorVentanaSRec.iconoMenuItem(menus[6], Texto.get("MENU_ARBL_02",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), "./imagenes/i_estructuraarbol_des.gif");
				actualizarVisualizacion();
			}
			else if(fuente==botones[34])		// Información > Información traza...
			{
				if (Conf.fichero_log) log_write("Botón > Información traza...");
				new CuadroInfoTraza(this, traza, trazaCompleta);
			}

		}
		else
		{
			String nombreclase=((fuente).getClass()).getName();
			if (Conf.fichero_log) log_write("Elemento de interacción no reconocido ("+nombreclase+")");
			if (depurar) System.out.println("VentanaVisualizador > Clase no reconocida: "+nombreclase);
		}

	}
	
	
	public void gestionOpcionCargarClase()
	{
		if (panelVentana.haySitio())
			new Preprocesador();
		else
			new CuadroPreguntaNuevaVisualizacion(this,"procesar");
	}
	
	
	public void gestionOpcionCargarAnimacion()
	{
		if (panelVentana.haySitio())
			new CargadorTraza();
		else
			new CuadroPreguntaNuevaVisualizacion(this,"cargar");
	}
	
	public void gestionOpcionCargarAnimacionGIF()
	{
		if (panelVentana.haySitio())
			this.cargarGIF();
		else
			new CuadroPreguntaNuevaVisualizacion(this,"cargarGIF");
	}
	
	
	
	
	

	/**
		Provoca que las ventanas de visualización se actualicen con los cambios introducidos desde la opción de menú de colores
	*/
	public void refrescarFormato()
	{
		panelVentana.refrescarFormato();
	}

	/**
		Provoca que las ventanas de visualización se actualicen con los cambios introducidos desde la opción de menú de opciones
	*/
	public void refrescarOpciones()
	{
		panelVentana.refrescarOpciones();
	}
	
	
	/**
	Actualiza los valores del estado de la Traza en función de su estado actual y las opciones de configuración
	*/
	public void actualizarTraza()
	{
		//System.out.println("  VentanaVisualizador.actualizarTraza:");
		//System.out.println("    Conf.elementosVisualizar==Conf.VISUALIZAR_TODO: "+(Conf.elementosVisualizar==Conf.VISUALIZAR_TODO));
		//System.out.println("    Conf.elementosVisualizar==Conf.VISUALIZAR_ENTRADA: "+(Conf.elementosVisualizar==Conf.VISUALIZAR_ENTRADA));
		//System.out.println("    Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA: "+(Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA));
		//System.out.println("    --------------------------------------------------------");
		//System.out.println("    Conf.elementosVisualizar_ant!=Conf.elementosVisualizar: "+(Conf.elementosVisualizar_ant!=Conf.elementosVisualizar));

		// Datos que se mostrarán
		if (Conf.elementosVisualizar==Conf.VISUALIZAR_TODO && Conf.elementosVisualizar_ant!=Conf.elementosVisualizar)
			Ventana.thisventana.traza.visualizarEntradaYSalida();
		else if (Conf.elementosVisualizar==Conf.VISUALIZAR_ENTRADA && Conf.elementosVisualizar_ant!=Conf.elementosVisualizar)
			Ventana.thisventana.traza.visualizarSoloEntrada();
		else if (Conf.elementosVisualizar==Conf.VISUALIZAR_SALIDA && Conf.elementosVisualizar_ant!=Conf.elementosVisualizar)
			Ventana.thisventana.traza.visualizarSoloSalida();

	}
	
	// Tras modificar opciones de configuración en el menú...
	private void actualizarVisualizacion()
	{
		Conf.setValoresOpsVisualizacion(false);
		Conf.setValoresVisualizacion();
		if (Ventana.thisventana.traza!=null)
		{
			Ventana.thisventana.actualizarTraza();
			//Ventana.thisventana.refrescarFormato();
			Ventana.thisventana.refrescarOpciones();
			
			Conf.setRedibujarGrafoArbol(false);
		}
	}
	
	
	
	public void abrirPanelCodigo(boolean editable,boolean cargarFichero)
	{
		
		panelVentana.abrirPanelCodigo(this.claseAlgoritmo.getPath(),editable,cargarFichero);
	}
	
	public void setTextoCompilador(String texto)
	{
		panelVentana.setTextoCompilador(texto);
	}
	
	
	
	public PanelVentana getPanelVentana()
	{
		return this.panelVentana;
	}
	

	
	


	
	
	/**
		Actualiza la información sobre el zoom y actualiza la visualización correspondiente
	*/
	public void actualizarZoomArbol(int valor)
	{
		panelVentana.refrescarZoomArbol(valor);
	}
	
	/**
		Actualiza la información sobre el zoom y actualiza la visualización correspondiente
	*/
	public void actualizarZoomPila(int valor)
	{
		panelVentana.refrescarZoomPila(valor);
	}
	
	/**
		Actualiza la información sobre el zoom y actualiza la visualización correspondiente
	*/
	public void actualizarZoom(int vista,int valor)
	{
		panelVentana.refrescarZoom(vista,valor);
	}
	
	/**
		Abre un nuevo panel de algoritmo que permita la visualización de un algoritmo.
		
		@param traza Recibe la traza de ejecución del algoritmo, será el contenido de la visualización
	*/
	public void visualizarAlgoritmo (Traza traza, boolean inicializar, CuadroProgreso cuadroProgreso,
										String directorio, String fichero, boolean editable)
	{
		try {
			if (inicializar)
				traza.nadaVisible();
			if (cuadroProgreso!=null)
				cuadroProgreso.setValores(Texto.get("CP_ABRIRPAN",Conf.idioma),90);
			
			if (traza!=null)
			{
				traza.asignarNumeroMetodo();
	
				this.trazaCompleta=traza.copiar();
				this.traza=traza.copiar();
				this.traza=this.dtb.podar(traza);

			}
			panelVentana.abrirPanelAlgoritmo(this.traza,directorio,fichero,editable);
			//this.refrescarOpciones();

			if (cuadroProgreso!=null)
				cuadroProgreso.setValores(Texto.get("CP_ABRIRPAN",Conf.idioma),95);
			//this.refrescarFormato();

			if (cuadroProgreso!=null)
				cuadroProgreso.setValores(Texto.get("CP_ABRIRPAN",Conf.idioma),98);
			
			habilitarOpcionesAnimacion(true);
			
			if (this.claseAlgoritmo!=null)
				this.animacionPendienteGuardar=true;
			
		} catch(OutOfMemoryError oome) {
			new CuadroError(this,Texto.get("ERROR_EJEC",Conf.idioma), Texto.get("ERROR_NOMEMSUF",Conf.idioma));
		} catch(Exception exp) {
			new CuadroError(this,Texto.get("ERROR_EJEC",Conf.idioma), Texto.get("ERROR_NODET",Conf.idioma));
			exp.printStackTrace();
		}
		
		//memoria();
	}

	/**
	 * Abre un nuevo panel de algoritmo que permita la visualización de de una determinada traza ya cargado
	 * el algoritmo.
	 *
	 * @param traza Recibe la traza de ejecución del algoritmo, será el contenido de la visualización
	 * @param dtb Datos de traza básicos.
	 */
	public void visualizarTraza(Traza traza, DatosTrazaBasicos dtb)
	{
		try {		
			traza.asignarNumeroMetodo();
			
			this.dtb = dtb;
			this.trazaCompleta=traza.copiar();
			this.traza=traza.copiar();
			this.traza=this.dtb.podar(traza);
	
			panelVentana.mostrarEjecucionTraza();
			
			habilitarOpcionesAnimacion(true);
			
			if (this.claseAlgoritmo!=null)
				this.animacionPendienteGuardar=true;
			
		} catch(OutOfMemoryError oome) {
			new CuadroError(this,Texto.get("ERROR_EJEC",Conf.idioma), Texto.get("ERROR_NOMEMSUF",Conf.idioma));
		} catch(Exception exp) {
			new CuadroError(this,Texto.get("ERROR_EJEC",Conf.idioma), Texto.get("ERROR_NODET",Conf.idioma));
			exp.printStackTrace();
		}
	}
	
	public boolean panelOcupado()
	{
		return panelVentana.estaOcupado();
	}
	
	public Traza getTraza()
	{
		return this.traza;
	}
	
	
	public void cargarGIF()
	{
		OpcionFicherosRecientes ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);
		
		String [][]extensiones=new String[1][];
		extensiones[0]=new String[1];
		extensiones[0][0]="gif";
		String []definiciones={Texto.get("ARCHIVO_GIF",Conf.idioma)};
		
		ficheroGIF=SelecDireccion.cuadroAbrirFichero(ofr.getDir(),Texto.get("CA_CARGVISGIF",Conf.idioma),	null,
				extensiones,definiciones,0);


		if (ficheroGIF!=null && ficheroGIF[1]!=null)
		{
			File f=new File(ficheroGIF[0]+ficheroGIF[1]);
			if (f.exists())
				cargarGIF2();
			else
				new CuadroError(this,Texto.get("ERROR_ARCH",Conf.idioma), Texto.get("ERROR_ARCHNE",Conf.idioma));
		}
	}
	
	public void cargarGIF2()
	{
		this.setClase(null);
		this.setTextoCompilador(PanelCompilador.CODIGO_VACIO);
		this.cerrarVentana();
		this.cerrarVistas();
		this.setClasePendienteGuardar(false);
		panelVentana.abrirPanelAlgoritmo(ficheroGIF[0]+ficheroGIF[1]);
		if (Conf.fichero_log) log_write("Animación GIF cargada '"+ficheroGIF[0]+ficheroGIF[1]+"'...");
	}
	
	
	public void cerrarVistas()
	{
		panelVentana.cerrarVistas();
	}
	
		
	public void guardarTraza()
	{
		OpcionFicherosRecientes ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);
		
		String [][]extensiones=new String[1][];
		//extensiones[0]=new String[1];
		//extensiones[0][0]="txt";
		extensiones[0]=new String[1];
		extensiones[0][0]="html";
		String []definiciones={/*Texto.get("ARCHIVO_TXT",Conf.idioma),*/Texto.get("ARCHIVO_HTML",Conf.idioma)};
		
		
		this.fichTraza=SelecDireccion.cuadroAbrirFichero(ofr.getDir(),Texto.get("CA_GUARTRAZA",Conf.idioma),	null,
																	extensiones,definiciones,0);
		

		if (fichTraza!=null && fichTraza[1]!=null)
		{
			//String extension=fichTraza[1].substring( fichTraza[1].lastIndexOf(".") , fichTraza[1].length() );
		
		
			File f=new File(fichTraza[0]+fichTraza[1]);
			if (!f.exists())
				guardarTraza2("html");
			else
				new CuadroPreguntaSobreescribir (this,"html",this, null);
		}
	}
	
	public void guardarTraza2(String extension)
	{
		//System.out.println("OK");
		String contenidoArchivo=contenidoArchivo(extension);
		
		if (!this.fichTraza[1].toLowerCase().contains(".html"))
			this.fichTraza[1]=this.fichTraza[1]+".html";
		
		
		
		// Abrimos fichero
		FileWriter fw=null;
		try {
			fw = new FileWriter(this.fichTraza[0]+this.fichTraza[1]);
		} catch (Exception e) {
			System.out.println("VentanaVisualizador Error FileWriter 1");
		}
		
		// Escribimos
		try {
			fw.write(contenidoArchivo);
		} catch (Exception e) {
			System.out.println("VentanaVisualizador Error FileWriter 2");
		}
		
		// Cerramos fichero
		try {
			fw.close();
		} catch (Exception e) {
			System.out.println("VentanaVisualizador Error FileWriter 2");
		}
		
	}
	
	
	public void setClase(ClaseAlgoritmo clase)
	{
		this.claseAlgoritmo=clase;
		
		this.setClaseHabilitada(clase!=null && clase.getNumMetodos()>0);
		this.setClaseCargada(clase!=null);	// necesario que vaya detrás de setClaseHabilitada
		
		if (clase==null)
			panelVentana.cerrarPanelCodigo();
	}
	
	
	public void setDTByTrazas(DatosTrazaBasicos dtb,Traza traza,Traza trazaCompleta)
	{
		this.dtb=dtb;
		this.traza=traza;
		this.trazaCompleta=trazaCompleta;
	}
	
	
	
	public ClaseAlgoritmo getClase()
	{
		return this.claseAlgoritmo;
	}
	
	
	public void setDTB(DatosTrazaBasicos dtb)
	{
		this.dtb=dtb;
	}
	
	public DatosTrazaBasicos getDTB()
	{
		return this.dtb;
	}
	
	public void delDTB()
	{
		this.dtb=null;
	}
	
	
	
	public void actualizarEstadoTrazaCompleta()
	{
		//this.traza.verArbol("traza0.txt");
		//this.trazaCompleta.verArbol("trazaCompleta0.txt");
	
		// 1º Copiar flags desde traza a trazaCompleta
		this.trazaCompleta.actualizarEstadoTrazaCompleta(this.traza);
		//this.trazaCompleta.verArbol("trazaCompleta1.txt");
		
		// 2º Aplicar nueva visibilidad (datos introducidos en CuadroVisibilidad, recogidos en dtb)
		this.trazaCompleta.setVisibilidad(this.dtb);
		//this.trazaCompleta.verArbol("trazaCompleta2.txt");
		
		// 3º Podar y visualizar
		this.traza=this.trazaCompleta.copiar();
		//this.traza.verArbol("traza1.txt");
		this.traza=this.dtb.podar(traza);
		//this.traza.verArbol("traza2.txt");
		this.traza.hacerCoherente();
		//this.traza.verArbol("traza3.txt");
		this.trazaCompleta.actualizarEstadoTrazaCompleta(this.traza);
		//this.trazaCompleta.verArbol("trazaCompleta3.txt");
		panelVentana.refrescarOpciones();
	}
	
	
	
	public String getTituloGenerico()
	{
		return Texto.get("APLIC",Conf.idioma);
	}
	
	public void iniciarNuevaVisualizacionSelecMetodo()
	{
		new CuadroMetodosProcesadosSelecMetodo(this, this.claseAlgoritmo);
	}
	
	public void introducirParametros()
	{
		new CuadroParamLanzarEjec(this, this.claseAlgoritmo.getMetodoPrincipal(), this.claseAlgoritmo, p);
	}
	
	// Deshabilita las opciones de menu que deben quedar deshabilitadas tras cerrar una animación
	public void cerrarVentana()
	{
		habilitarOpcionesAnimacion(false);
		
		this.animacionPendienteGuardar=false;
		this.traza=null;
		
		if (Ventana.thisventana.getClase()==null)
			Ventana.thisventana.setTitulo("");
	}
	

	private int[] redimensionarVentana(int []d)
	{
		int []tam=new int[2];
		
		tam[0]=(int)(d[0]*(0.85));
		tam[1]=(int)(d[1]*(0.85));
		
		return tam;
	}
	
	public void distribuirPaneles(boolean[] valores1,int disposicion)
	{
		panelVentana.distribuirPaneles(valores1,disposicion);
	}
	
	public void distribuirPaneles(int disposicion)
	{
		panelVentana.distribuirPaneles(disposicion);
	}	
	
	
	/*public static int[] dimensiones()
	{
		int d[]=new int[2];
		
		d[0]=thisventana.getWidth();
		d[1]=thisventana.getHeight();
		return d;
	}*/
	
	
	// Devuelve dimensiones de paneles contenedores de estructura y principal
	public int[] dimensionesPanelesVisualizacion()
	{
		int dimensiones[]=new int[8];
		
		int dimE[]=panelVentana.dimPanelPila();
		int dimP[]=panelVentana.dimPanelPrincipal();
		int dimV[]=panelVentana.getTamanoPaneles();
		
		// Vistas REC
		dimensiones[0]=dimE[0];
		dimensiones[1]=dimE[1];
		dimensiones[2]=dimP[0];
		dimensiones[3]=dimP[1];
		
		// Vistas DYV
		dimensiones[4]=dimV[4];
		dimensiones[5]=dimV[5];
		dimensiones[6]=dimV[6];
		dimensiones[7]=dimV[7];
		
		return dimensiones;
	}
	
	// Devuelve dimensiones grafos de estructura y principal
	public int[] dimensionesGrafosVisualizacion()
	{
		int dimensiones[]=new int[4];
		
		int dimE[]=panelVentana.dimGrafoPila();
		int dimP[]=panelVentana.dimGrafoPrincipal();
		
		dimensiones[0]=dimE[0];
		dimensiones[1]=dimE[1];
		dimensiones[2]=dimP[0];
		dimensiones[3]=dimP[1];
		
		return dimensiones;
	}
	
	// Devuelve dimensiones grafos de estructura y principal
	public int[] dimensionesGrafosVisiblesVisualizacion()
	{
		int dimensiones[]=new int[8];
		
		int dimP[]=panelVentana.dimGrafoPila();
		int dimA[]=panelVentana.dimGrafoVisiblePrincipal();
		int dimV[]=panelVentana.getTamanoGrafos();
		
		// Vistas REC
		dimensiones[0]=dimP[0];
		dimensiones[1]=dimP[1];
		dimensiones[2]=dimA[0];
		dimensiones[3]=dimA[1];

		// Vistas DYV
		dimensiones[4]=dimV[4];
		dimensiones[5]=dimV[5];
		dimensiones[6]=dimV[6];
		dimensiones[7]=dimV[7];
		
		return dimensiones;
	}
	
	
	// capturarGIFs....
	
	
	
	public void deshabilitarOpcionesVentana()
	{
		this.setResizable(false);
		//this.setAlwaysOnTop(true);
		GestorVentanaSRec.setVisibleBarraHerramientas(this.barrasHerramientas,false,false);
		this.barramenu.setVisible(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		panelVentana.deshabilitarControles();
	}
	
	public void habilitarOpcionesVentana()
	{
		this.setResizable(true);
		//this.setAlwaysOnTop(false);
		if (this.barraHerramientasVisible)
			GestorVentanaSRec.setVisibleBarraHerramientas(this.barrasHerramientas,true,true);
		this.barramenu.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		panelVentana.habilitarControles();
	}
	

	public void guardarClase()
	{
		setClasePendienteGuardar(false);
		panelVentana.guardarClase();
		if (Conf.fichero_log) Logger.log_write("Clase guardada en disco tras edición");
	}
	
	
	
	public void setTitulo(String s)
	{
		if (s==null || s.length()==0)
			this.setTitle(this.getTituloGenerico().replace("[","(").replace("]",")"));
		else
			this.setTitle(this.getTituloGenerico()+" ["+s+"]");
	}
	
	public boolean getClasePendienteGuardar()
	{
		return clasePendienteGuardar;
	}
	
	
	
	// Desde el panel de edición de código Java nos llaman aquí para decirnos si alguien está editando el código de la clase cargada
	public void setClasePendienteGuardar(boolean valor)
	{
		String cadenaMarca=" *";
		
		String tituloActual=this.getTitle();
		
		if (tituloActual.contains("["))
		{
			tituloActual=tituloActual.substring(tituloActual.indexOf("[")+1 , tituloActual.indexOf("]"));
			
			clasePendienteGuardar=valor;
			if (clasePendienteGuardar)
			{
				if (!(tituloActual.contains(cadenaMarca)))
					setTitulo(tituloActual+cadenaMarca);
			}
			else
			{
				setTitulo(tituloActual.replace(cadenaMarca,""));
			}
		}
	}
	
	public void setClaseHabilitada(boolean valor)
	{
		this.claseHabilitada=valor;
		
		GestorVentanaSRec.habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_14",Conf.idioma), valor);
		//GestorVentanaSRec.habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_05",Conf.idioma), valor);
		
		setClaseCargada(valor);
		botones[ 4].setEnabled(valor);
		botones[28].setEnabled(valor);
		if(!valor)  //Desactivamos los dos a la vez. Pero no podemos activarlos a la vez. Para lanzar ejecucion, primero se tiene que seleccionar metodo.
			this.setClaseHabilitadaAnimacion(false);
	}
	
	public void setClaseHabilitadaAnimacion(boolean valor)
	{
		GestorVentanaSRec.habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_15",Conf.idioma), valor);
		botones[29].setEnabled(valor);
	}
	
	
	
	public void setClaseCargada(boolean valor)
	{	
		botones[ 2].setEnabled(valor);
		botones[ 3].setEnabled(valor);
		GestorVentanaSRec.habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_04",Conf.idioma), valor);
	}
	
	public boolean getClaseHabilitada()
	{
		return this.claseHabilitada;
	}
	
	
	
	
	
	public void setBotones(JButton[] botones)
	{
		this.botones=botones;
	}
	
	public void habilitarOpcionesDYV(boolean valor) {
		botones[33].setEnabled(valor);
		GestorVentanaSRec.habilitaMenuItem(menus[6], Texto.get("MENU_ARBL_02",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[7], Texto.get("MENU_TRAZ_03",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[7], Texto.get("MENU_TRAZ_04",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), valor);
	}
	
	public void habilitarOpcionesAnimacion(boolean valor)
	{
		botones[ 6].setEnabled(valor);
		
		botones[ 7].setEnabled(valor);
		botones[ 8].setEnabled(valor);
		botones[ 9].setEnabled(valor);
		
		botones[11].setEnabled(valor);
		

		botones[20].setEnabled(valor);
		botones[21].setEnabled(valor);
		botones[22].setEnabled(valor);
		botones[23].setEnabled(valor);
		botones[24].setEnabled(valor);
		botones[25].setEnabled(valor);
		botones[26].setEnabled(valor);
		botones[27].setEnabled(valor);
		
		botones[30].setEnabled(valor);
		botones[31].setEnabled(valor);
		botones[34].setEnabled(valor);

		
		GestorVentanaSRec.habilitaMenuItem(menus[2], Texto.get("MENU_INFO_01",Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[2], Texto.get("MENU_INFO_02",Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[5], Texto.get("MENU_FILT_04",Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[5], Texto.get("MENU_FILT_05",Conf.idioma), valor);
		
		GestorVentanaSRec.habilitaMenuItem(menus[1], Texto.get("MENU_VISU_03",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[5], Texto.get("MENU_FILT_01",Conf.idioma).replace("_SubMenuItem_","").replace("_CheckBoxMenuItem_",""), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[1], Texto.get("MENU_VISU_11",Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[1], Texto.get("MENU_VISU_12",Conf.idioma), valor);
		
		GestorVentanaSRec.habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_07",Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_09",Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_10",Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_11",Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_12",Conf.idioma), valor);
	}
	
	public String contenidoArchivo(String tipo)
	{
		String textoEntrada=Texto.get("TR_E",Conf.idioma);
		String textoSalida=Texto.get("TR_S",Conf.idioma);
		String lineasTraza[]=this.traza.getLineasTraza( );
		String salida="";
		
		if (tipo.toLowerCase().equals("html"))
		{
			String fuente=Conf.fuenteTraza.getFontName();
			boolean negrita=false;
			boolean cursiva=false;
			
			negrita=fuente.toLowerCase().contains("bold") || fuente.toLowerCase().contains("negrita");
			cursiva=fuente.toLowerCase().contains("italic") || fuente.toLowerCase().contains("cursiva");
			
			
			
			if (fuente.contains("."))
				fuente=fuente.substring( 0,fuente.lastIndexOf(".")  );
		
		
			salida+="<html><!-- Generado por SRec / Generated by SRec -->\r\n";
			salida+="<head><title>SRec - "+this.traza.getTitulo()+"</title></head>\r\n";
			
			
			salida+="<body bgcolor=\"#"+ServiciosString.cadenaColorHex(Conf.colorPanel.getRed(), Conf.colorPanel.getGreen(), Conf.colorPanel.getBlue())+"\">\r\n";
			
			int limiteOscuro=170;
			int coloresOscuros=0;
			if (Conf.colorPanel.getRed()<limiteOscuro)
				coloresOscuros++;
			if (Conf.colorPanel.getGreen()<limiteOscuro)
				coloresOscuros++;
			if (Conf.colorPanel.getBlue()<limiteOscuro)
				coloresOscuros++;
			
			if (coloresOscuros>=2)
				salida+="<font face=\"Verdana\" color=\"#FFFFFF\" size=\"4\">SRec - "+this.traza.getTitulo();
			else
				salida+="<font face=\"Verdana\" size=\"4\">SRec - "+this.traza.getTitulo();
				
				
				
			salida+="<br />______________________________<br /><br /></font>\r\n";
			salida+="<font face=\""+fuente+"\">\r\n";
			
			salida+=( negrita ? "<b>" : "" );
			salida+=( cursiva ? "<i>" : "" );
			
			for (int i=0; i<lineasTraza.length; i++)
			{
				String color="";
				// Conf.colorTrazaE
				if (lineasTraza[i].contains("entra "))
				{
					if (lineasTraza[i].contains("<ilum>"))
					{
						color=ServiciosString.cadenaColorHex(Conf.colorIluminado.getRed(),Conf.colorIluminado.getGreen(),Conf.colorIluminado.getBlue());
					}
					else if (Conf.modoColor==1)
						if (lineasTraza[i].contains("<hist>") && Conf.historia!=0)
							color=ServiciosString.cadenaColorHex(Conf.colorC1AEntrada.getRed(),Conf.colorC1AEntrada.getGreen(),Conf.colorC1AEntrada.getBlue());
						else
							color=ServiciosString.cadenaColorHex(Conf.colorC1Entrada.getRed(),Conf.colorC1Entrada.getGreen(),Conf.colorC1Entrada.getBlue());
					else
					{
						int numColor=Integer.parseInt(lineasTraza[i].substring(lineasTraza[i].indexOf("?")+1,lineasTraza[i].lastIndexOf("?")));
						if (lineasTraza[i].contains("<hist>") && Conf.historia!=0)
							color=ServiciosString.cadenaColorHex(Conf.coloresNodoA[numColor].getRed(),Conf.coloresNodoA[numColor].getGreen(),Conf.coloresNodoA[numColor].getBlue());
						else
							color=ServiciosString.cadenaColorHex(Conf.coloresNodo[numColor].getRed(),Conf.coloresNodo[numColor].getGreen(),Conf.coloresNodo[numColor].getBlue());
					}
				}
				else
				{
					if (lineasTraza[i].contains("<ilum>"))
					{
						color=ServiciosString.cadenaColorHex(Conf.colorIluminado.getRed(),Conf.colorIluminado.getGreen(),Conf.colorIluminado.getBlue());
					}
					else if (Conf.modoColor==1)
						if (lineasTraza[i].contains("<hist>") && Conf.historia!=0)
							color=ServiciosString.cadenaColorHex(Conf.colorC1ASalida.getRed(),Conf.colorC1Salida.getGreen(),Conf.colorC1ASalida.getBlue());
						else
							color=ServiciosString.cadenaColorHex(Conf.colorC1Salida.getRed(),Conf.colorC1Salida.getGreen(),Conf.colorC1Salida.getBlue());
					else
					{
						int numColor=Integer.parseInt(lineasTraza[i].substring(lineasTraza[i].indexOf("?")+1,lineasTraza[i].lastIndexOf("?")));
						if (lineasTraza[i].contains("<hist>") && Conf.historia!=0)
							color=ServiciosString.cadenaColorHex(Conf.coloresNodoA[numColor].getRed(),Conf.coloresNodoA[numColor].getGreen(),Conf.coloresNodoA[numColor].getBlue());
						else
							color=ServiciosString.cadenaColorHex(Conf.coloresNodo[numColor].getRed(),Conf.coloresNodo[numColor].getGreen(),Conf.coloresNodo[numColor].getBlue());
					}
				}
				
				
				
				salida+="<font color=\"#"+color+"\">";//+lineasTraza[i]+"</font>"
				//+"<br />\r\n";
				lineasTraza[i]=lineasTraza[i].replace("entra ", textoEntrada );
				lineasTraza[i]=lineasTraza[i].replace("salida ", textoSalida );
				lineasTraza[i]=lineasTraza[i].replace("<hist>", "" );
				lineasTraza[i]=lineasTraza[i].replace("<ilum>", "" );
				lineasTraza[i]=lineasTraza[i].replace(" ", "&nbsp;" );
				lineasTraza[i]=lineasTraza[i].substring(0,lineasTraza[i].indexOf("?"));
				salida+=lineasTraza[i]+"</font>"+"<br />\r\n";
			}		
			salida+=( negrita ? "</b>" : "" );
			salida+=( cursiva ? "</i>" : "" );
			
			salida+="</font></body></html>\r\n";
		}
		else	// .txt
		{
			salida+="-- Generado por SRec / Generated by SRec --\r\n\r\n";
			salida+=this.traza.getNombreMetodoEjecucion()+"\r\n\r\n";
			
			for (int i=0; i<lineasTraza.length; i++)
			{
				lineasTraza[i]=lineasTraza[i].replace("entra ", textoEntrada );
				lineasTraza[i]=lineasTraza[i].replace("salida ", textoSalida );
				lineasTraza[i]=lineasTraza[i].substring(0,lineasTraza[i].indexOf("?"));
				salida+=lineasTraza[i]+"\r\n";
			}		
			
			salida+="\r\n";
		}
		return salida;
	}
	
	private void guardarConfiguracionOpciones()
	{
		OpcionFicherosRecientes ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);
		String fich[]=SelecDireccion.cuadroAbrirFichero(ofr.getDir(),Texto.get("CA_GUARCONF",Conf.idioma),
					null,"xml",Texto.get("ARCHIVO_XML",Conf.idioma),0);
		
		if (fich!=null && fich[1]!=null)
		{
			File f=new File(fich[0]+fich[1]);
			if (!f.exists())
				gOpciones.crearArchivo(fich[0]+fich[1]);
			else
				new CuadroPreguntaSobreescribir (this,fich[0]+fich[1],gOpciones,null);
		}
	}
	
	
	
	public void setAnimacionPendienteGuardar(boolean valor)
	{
		this.animacionPendienteGuardar=valor;
	}
	
	
	public void consultaGuardado()
	{
		if (animacionPendienteGuardar)
			new CuadroPreguntaAnimacionNoGuardada(this);
		else
			cerrar();

	}
	
	
	public void cerrar()
	{
		if (Conf.fichero_log)
			log_close();
		System.exit(0);
	}
	
	
	
	public void guardadoTraza()
	{
		this.trazaCompleta.actualizarEstadoTrazaCompleta(this.traza);
		this.trazaCompleta.setVisibilidad(this.dtb);
		
		new AlmacenadorTraza(this.traza, this.trazaCompleta, this.dtb);
		// AlmacenadorTraza es un thread, el programa no guarda la traza por eso, se sale sin esperar al thread.
	}
	
	
	public void reiniciarIdioma()
	{
		// Necesitamos reiniciar menús, tooltip de la barra de herramientas, tooltip de barra de animación y título de ventana

		// Tooltip de la barra de herramientas
		GestorVentanaSRec.setToolTipTextBHH(this.botones);
		
		// Tooltip de la barra de animación
		panelVentana.idioma();
		
		// Reiniciar menús
		GestorVentanaSRec.crearMenu(this.menus);
		
		// Título de la ventana
		String tituloVentana=this.getTitle();
		if (tituloVentana.contains("["))
		{
			String archivoTitulo=tituloVentana.substring( tituloVentana.indexOf("[")+1 , tituloVentana.indexOf("]"));
			setTitulo(archivoTitulo);
		}
		else
			setTitulo("");
		

		textos=Texto.get(codigos,Conf.idioma);
		for (int i=0; i<textos.length; i++)	// Limpiamos los textos de los elementos del submenú del menú Animación
			if (textos[i].contains("_"))
				textos[i]=textos[i].substring(0,textos[i].indexOf("_"));
		
		new CuadroInformacion(this,Texto.get("INFO_IDIOMAOK",Conf.idioma),Texto.get("INFO_IDIOMA",Conf.idioma),550,100);
	}
	
	/* ********************* */

	public static String getFileJAR()
	{
		File d=new File(".");
		
		// Ficheros del directorio de la aplicacion
		File ficheros[]=d.listFiles();
		
		for (int i=0; i<ficheros.length; i++)
			if (ficheros[i].getName().toLowerCase().contains(".jar") && ficheros[i].getName().toLowerCase().contains("srec"))
				try {
					return ficheros[i].getCanonicalPath();//+ficheros[i].getName().replace("\\.", "")
				} catch (IOException ioe) {
					
				}
		return null;
	}
	

	public static void setClasspath(String classpath)
	{
		classpathOriginal=classpath;
	}
	
	
	
	/*public static void memoria()
	{
				
			//int miVarNueva=14;
			//System.out.println(new PrintfFormat("Pre [%5d] Post").sprintf(miVarNueva) ); 
	
	
	
		Runtime rt=Runtime.getRuntime();
		
		long tm=rt.totalMemory();
		long fm=rt.freeMemory();
		long mm=rt.maxMemory();
		
		String stm=""+tm;
		String sfm=""+(tm-fm);
		String smm=""+mm;
		
		int longitud=13;
		
		while (stm.length()<longitud)
			stm=" "+stm;
		while (sfm.length()<longitud)
			sfm=" "+sfm;
		while (smm.length()<longitud)
			smm=" "+smm;
		
		System.out.println("\n-------- memoria SREC --------");
		System.out.println("- Memoria ocup.: "+sfm);
		System.out.println("          %: "+((((double)(tm-fm))/((double)tm))*100));
		System.out.println("- Memoria total: "+stm);
		System.out.println("          %: "+((((double)(tm))/((double)mm))*100));
		System.out.println("- Memoria máx. : "+smm);
		System.out.println("------------------------------\n");
	}*/
	
	public static void borrarArchivosInservibles()
	{
		// Eliminamos ficheros residuales de sesiones anteriores ("SRec_*");
		File directorioAplicacion=null;
		try {
			directorioAplicacion=new File(".");
		} catch (Exception exc) {
		}
		File archivosParaEliminar[]=directorioAplicacion.listFiles();
		for (int i=0; i<archivosParaEliminar.length; i++)
			if (archivosParaEliminar[i].getName().contains("SRec_"))
				archivosParaEliminar[i].delete();
	}
	
	
	
	public void setPreprocesador(Preprocesador p)
	{
		this.p=p;
	}
	
	
	public static void setProcesando(boolean valor)
	{
		procesando=valor;
	}
	
	public static boolean getProcesando()
	{
		return procesando;
	}
	
	
	public void activarCierre()
	{
		
		if (getTraza()!=null)
			consultaGuardado();
		else
		{
			Ventana.borrarArchivosInservibles();
			log_close();
			System.exit(0);
		}
	}
	
	public void setValoresPanelControl(String tituloPanel) {
		this.panelVentana.setValoresPanelControl(tituloPanel);
	}
	
	public void procesarClaseSeleccionarMetodo() {
		new Preprocesador(this.claseAlgoritmo.getPath(),this.claseAlgoritmo.getNombre()+".java", true);
	}
	
	public void setClasePendienteProcesar(boolean valor) {
		this.clasePendienteProcesar=valor;
	}
	
	public void log_open()
	{
		Logger.log_write("----------------- INICIO DE SESION "+ServiciosString.direccionIP());
	}
	
	public void log_write(String s)
	{
		Logger.log_write(s);
	}
	
	public void log_close()
	{
		Logger.log_write("----------------- FIN DE SESION "+ServiciosString.direccionIP()+"\r\n\r\n\r\n");
	}
}


