package cuadros;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PageRanges;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import conf.Conf;
import utilidades.Texto;
import ventanas.GestorVentanaSRec;
import ventanas.Ventana;

public class CuadroTerminal implements WindowListener, ActionListener, Printable {
	
	//********************************************************************************
    // 			VARIABLES
    //********************************************************************************	
	
	//***************************************
    // 			CUADRO TERMINAL
    //***************************************
	
	//	Generales
	
	boolean cuadroEstaVisible;
	private Ventana cuadroVentana;
	private JFrame cuadroDialogo;
	private final int CUADRO_ANCHO = 500;
	private final int CUADRO_ALTO = 500;
	private boolean cuadroAbiertoPrimeraVez = false;
	private final String cuadroTitulo = Texto.get("TER_TITULO", Conf.idioma);
	private final String cuadroImagen = "imagenes/ter_terminal.png";
	
	//	Panel general
	
	private JPanel cuadroPanelGeneral;
	
	//	Panel botones
	
	private JPanel cuadroPanelBotones;
	
	//	Panel de salidas
	
	private JPanel cuadroPanelSalidas;	
	
	//***************************************
    // 			CONTROLES
    //***************************************
	
	//	Traducciones e im�genes
	
	private enum controlesNombre {
		TER_LIMPIAR, TER_GUARDAR, TER_COPIAR, TER_IMPRIMIR, TER_LIMPIAR_PANTALLA_DESACTIVADO, TER_LIMPIAR_PANTALLA_ACTIVADO,
		TER_REG_LLAMADAS_DESACTIVADO, TER_REG_LLAMADAS_ACTIVADO, TER_BUFFER_DESACTIVADO, TER_BUFFER_ACTIVADO, TER_CERRAR,
		TER_OPCIONES
	}
	
	private Map<controlesNombre,String> controlesTraducciones;
	private Map<controlesNombre,String> controlesImagenesUrl;
	
	//	Desplegable	
	
	private final int controlesDesplegableNumeroMenu = 8;	
	private JMenu controlesDesplegableMenu;
	private JCheckBoxMenuItem[] controlesDesplegableJCheckBoxMenuItems;
	private JMenuBar controlesDesplegableMenuBar;
	
	//	Botones
	
	private final int controlesBotonesNumeroJButtons = 8;
	private final int controlesBotonesNumeroJToolbar = 3;
	private JToolBar[] controlesBotonesJToolbar;	
	private JButton[] controlesBotonesJButtons;	
	
	//	Estado por defecto botones variables
	
	private boolean controlesBotonesEstadoLimpiarPantalla = false;
	private boolean controlesBotonesEstadoRegistroLlamadas = true;
	private boolean controlesBotonesEstadoBuffer = false;
	
	//***************************************
    // 			PANELES
    //***************************************
	
	//	General split
	
	private JSplitPane panelesPanelSplitPane;
	private double panelesPanelSplitPaneValor = 0.5;
	
	//	Salida normal
	
	private JPanel panelesPanelNormal;
	private JScrollPane panelesPanelNormalScroll;
	private panelTextoClase panelesPanelNormalTexto;
	
	//	Salida errores
	
	private JPanel panelesPanelError;
	private JScrollPane panelesPanelErrorScroll;
	private panelTextoClase panelesPanelErrorTexto;
	
	//***************************************
    // 			SALIDAS
    //***************************************
	
	//	Estilo terminal
	
	private final String salidaTextoFuenteGeneral = Conf.terminalSalidaTextoFuenteGeneral;
	
	private final Color salidaTextoNormalColor = Conf.terminalSalidaTextoNormalColor;
	private final int salidaTextoNormalTamano = Conf.terminalSalidaTextoNormalTamano;
	private final boolean salidaTextoNormalNegrita = Conf.terminalSalidaTextoNormalNegrita;
	
	private final Color salidaTextoNormalResultadoMetodoColor = Conf.terminalSalidaTextoNormalResultadoMetodoColor;
	private final int salidaTextoNormalResultadoMetodoTamano = Conf.terminalSalidaTextoNormalResultadoMetodoTamano;
	private final boolean salidaTextoNormalResultadoMetodoNegrita = Conf.terminalSalidaTextoNormalResultadoMetodoNegrita;

	private final Color salidaTextoErrorColor = Conf.terminalSalidaTextoErrorColor;
	private final int salidaTextoErrorTamano = Conf.terminalSalidaTextoErrorTamano;
	private final boolean salidaTextoErrorNegrita = Conf.terminalSalidaTextoErrorNegrita;

	private final Color salidaTextoCabeceraColor = Conf.terminalSalidaTextoCabeceraColor;
	private final int salidaTextoCabeceraTamano = Conf.terminalSalidaTextoCabeceraTamano;
	private final boolean salidaTextoCabeceraNegrita = Conf.terminalSalidaTextoCabeceraNegrita;
	
	//	Exportar cabecera (copiar, guardar e imprimir)
	
	private final String salidaTextoCabeceraExportarNormal = 			 
			
	 "\n\n"+"**************************************************************"+"\n"+
			"		"+Texto.get("TER_IMPRIMIR_SALIDA_NORMAL", Conf.idioma) 	   +"\n"+
			"**************************************************************"+"\n\n";
					
	private final String salidaTextoCabeceraExportarError = 
			
	 "\n\n"+"**************************************************************"+"\n"+
			"		"+Texto.get("TER_IMPRIMIR_SALIDA_ERROR", Conf.idioma)      +"\n"+
			"**************************************************************"+"\n\n";
	
	//	Exportar nombre arhivo (copiar e imprimir)
	
	private final String salidaTextoNombreArchivo =
			Texto.get("TER_IMPRIMIR_NOMBRE_ARCHIVO", Conf.idioma);
	
	//	Imprimir
	
	private String[] salidaTextoImprimirContenido;
	
	private Font salidaTextoImprimirFuente;
	
	private int salidaTextoImprimirNumeroPaginas, 
				salidaTextoImprimirLineasPorPagina,
				salidaTextoImprimirFuenteHeight;
	
	private final int salidaTextoImprimirNumeroCaracteresLinea = 65;
	
	//	Guardar
	
	private final String salidaTextoGuardarTitulo =
			Texto.get("TER_GUARDAR_TITULO", Conf.idioma);
	
	//	Buffer
	
	private final int salidaBuffer = 2000;
	
	//********************************************************************************
    // 			CONSTRUCTOR
    //********************************************************************************	
	
	/**
	 * Constructor de la terminal
	 * 
	 * @param ventana
	 * 		Ventana a la que estar� asociada la terminal
	 */
	public CuadroTerminal(Ventana ventana) {
		
		//	Inicializaciones previas
		
		this.cuadroVentana = ventana;
		this.cuadroEstaVisible = false;	
		
		//	Cuadro di�logo
		
		this.cuadroDialogoInicializar();		
		
		//	Inicializaciones controles / botones	
		
		this.controlesInicializar();
		
		//	Inicializaci�n paneles de salida normal y errores
		
		this.panelesInicializar();
		
		//	Panel general, rellenamos elementos
		
		this.cuadroPanelGeneral();
		
		//	Salidas estilos
		
		this.setSalidaColorCabecera();
		this.setSalidaColorNormal();
		this.setSalidaColorError();	
		this.setSalidaColorResultadoMetodo();
	}
	
	//********************************************************************************
    // 			M�TODOS P�BLICOS
    //********************************************************************************
	
	//***************************************
    // 			CUADRO TERMINAL
    //***************************************
	
	/**
	 * M�todo que abre o cierra la terminal
	 * 
	 * @return
	 * 		Nuevo valor de la visibilidad de la terminal
	 */
	public boolean terminalAbrirCerrar() {
		if(!this.cuadroAbiertoPrimeraVez) {			
			//this.cuadroAbiertoPrimeraVez = true;
		}
		
		if(this.cuadroEstaVisible)
			this.cuadroEstaVisible = false;
		else
			this.cuadroEstaVisible = true;		
		
		if(this.cuadroEstaVisible) {
			SwingUtilities.invokeLater(new Runnable() {	
	            @Override
	            public void run() {	 
	                if(cuadroDialogo != null) {
	                	cuadroDialogo.setVisible(true);
	                	terminalPrimerPlano();	                	
	                }
	            }
	        });
			
		}else {
			cuadroDialogo.setVisible(false);
		}
		
		return this.cuadroEstaVisible;		
	}
	
	/**
	 * Pasa a primer plano la ventana de la terminal
	 * refrescando el formato visual
	 */
	public void terminalPrimerPlano() {
		
		if(this.cuadroDialogo.isVisible()) {
	    	cuadroDialogo.setAlwaysOnTop(true);
	    	cuadroDialogo.toFront();
	    	panelesSeparadorRefrescar();
	    	panelesPanelErrorTexto.setScrollAbajo();
	    	panelesPanelNormalTexto.setScrollAbajo();
	    	
	    	//	Necesario esperar para quitar el always on top,
	    	//	sino no hace toFront mientras se escribe... BUG JAVA
	    	
	    	new Thread("Non edt Thread") {
	            public void run() {
	                try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
	                cuadroDialogo.setAlwaysOnTop(false);
	            }
	
	        }.start();
		}
	}
	
	//***************************************
    // 			SALIDAS
    //***************************************
	
	/**
	 * Retorna el ByteArrayOutputStream de la salida normal
	 * 
	 * @return
	 * 		ByteArrayOutputStream de la salida normal
	 */
	public ByteArrayOutputStream getSalidaNormal() {
		return this.panelesPanelNormalTexto;
	}
	
	/**
	 * Retorna el ByteArrayOutputStream de la salida de error
	 * 
	 * @return
	 * 		ByteArrayOutputStream de la salida de error
	 */
	public ByteArrayOutputStream getSalidaError() {
		return this.panelesPanelErrorTexto;
	}	
	
	/**
	 * Establece la cabecera de ambas salidas
	 * 
	 * @param s
	 * 		Valor de la cabacera
	 */
	public void setSalidaCabecera(String s) {
		
		//	Establecemos cabecera
		
		this.panelesPanelErrorTexto.setCabecera(s);
		this.panelesPanelNormalTexto.setCabecera(s);
		
		//	Comprobamos si hay que vaciar antes de llamada al m�todo
		
		if(this.controlesBotonesEstadoLimpiarPantalla)
			this.setSalidasVacias(true);
	}
	
	/**
	 * Establece el resultado del m�todo
	 * 
	 * @param s
	 * 		Valor de resultado del m�todo
	 */
	public void setSalidaResultadoMetodo(String s) {
		this.panelesPanelNormalTexto.setSalidaResultadoMetodo(s);
	}
	
	/**
	 * Obtiene, tras la escritura en ambas salidas,
	 * si la terminal se tiene que abrir o no
	 * 
	 * @return
	 * 		True si la terminal se tiene que abrir,
	 * 		false caso contrario
	 */
	public boolean getSalidasTerminalAbrir() {
		if(this.cuadroDialogo.isVisible())
			return false;
		return this.panelesPanelErrorTexto.getTerminalAbrir() ||
				this.panelesPanelNormalTexto.getTerminalAbrir();
	}
	
	/**
	 * Reinicializa los valores necesarios cuando una llamada
	 * a un m�todo termina de escribir en las salidas.
	 * Hacer tras llamada getSalidasTerminalAbrir, no antes.
	 */
	public void setSalidasFin() {
		this.panelesPanelErrorTexto.setEscribirFin();
		this.panelesPanelNormalTexto.setEscribirFin();
		this.panelesPanelErrorTexto.setBufferIlimitado(this.controlesBotonesEstadoBuffer);
		this.panelesPanelNormalTexto.setBufferIlimitado(this.controlesBotonesEstadoBuffer);
		this.panelesSeparadorRefrescar();
	}	
	
	//********************************************************************************
    // 			M�TODOS PRIVADOS
    //********************************************************************************
	
	//***************************************
    // 			CUADRO TERMINAL
    //***************************************	
	
	/**
	 * Inicializa el cuadro de di�logo general, la ventana
	 */
	private void cuadroDialogoInicializar() {
		this.cuadroDialogo = new JFrame();
		this.cuadroDialogo.addWindowListener(this);		
		this.cuadroDialogo.setSize(CUADRO_ANCHO, CUADRO_ALTO);
		URL icono = getClass().getClassLoader().getResource(this.cuadroImagen);
		this.cuadroDialogo.setIconImage(new ImageIcon(icono).getImage());
		this.cuadroDialogo.setTitle(this.cuadroTitulo);
		this.cuadroDialogo.getContentPane().setLayout(new GridBagLayout());	
	}
	
	/**
	 * Inicializa y rellena el panel general
	 */
	private void cuadroPanelGeneral() {
		
		//	Inicializaciones
		
		this.cuadroPanelGeneral = new JPanel(new GridBagLayout());			
		
		//	Desplegable
		
		this.cuadroDialogo.setJMenuBar(this.controlesDesplegableMenuBar);
		
		//	Panel general = Panel botones + Panel salidas	
		
		this.cuadroPanelGeneral.add(this.cuadroPanelBotones, this.variosGetConstraints(0, 0, 1, 1, 0, 0, false, GridBagConstraints.WEST));
		this.cuadroPanelGeneral.add(this.cuadroPanelSalidas, this.variosGetConstraints(0, 1, 1, 1, 1, 1, true, GridBagConstraints.CENTER));
		
		this.cuadroDialogo.getContentPane().add(this.cuadroPanelGeneral, this.variosGetConstraints(0, 0, 1, 1, 1, 1, true, GridBagConstraints.CENTER));
	}
	
	//***************************************
    // 		CONTROLES CREACI�N Y EDICI�N
    //***************************************
	
	/**
	 * Inicializa los controles, Men� desplegable + Panel botones
	 */
	private void controlesInicializar() {
		this.controlesRellenarImagenesUrl();
		this.controlesRellenarTraducciones();
		this.controlesDesplegableCrear();
		this.controlesBotonesCrear();
	}
	
	/**
	 * Rellena el mapa de traducciones
	 */
	private void controlesRellenarTraducciones() {
		
		this.controlesTraducciones = new HashMap<controlesNombre, String>();
		
		this.controlesTraducciones.put(controlesNombre.TER_LIMPIAR,
				Texto.get(controlesNombre.TER_LIMPIAR.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_GUARDAR,
				Texto.get(controlesNombre.TER_GUARDAR.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_COPIAR,
				Texto.get(controlesNombre.TER_COPIAR.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_IMPRIMIR,
				Texto.get(controlesNombre.TER_IMPRIMIR.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVADO,
				Texto.get(controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVADO.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVADO,
				Texto.get(controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVADO.toString(), Conf.idioma));	
		this.controlesTraducciones.put(controlesNombre.TER_REG_LLAMADAS_DESACTIVADO,
				Texto.get(controlesNombre.TER_REG_LLAMADAS_DESACTIVADO.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_REG_LLAMADAS_ACTIVADO,
				Texto.get(controlesNombre.TER_REG_LLAMADAS_ACTIVADO.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_BUFFER_DESACTIVADO,
				Texto.get(controlesNombre.TER_BUFFER_DESACTIVADO.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_BUFFER_ACTIVADO,
				Texto.get(controlesNombre.TER_BUFFER_ACTIVADO.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_CERRAR,
				Texto.get(controlesNombre.TER_CERRAR.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_OPCIONES,
				Texto.get(controlesNombre.TER_OPCIONES.toString(), Conf.idioma));
	}
	
	/**
	 * Rellena el mapa de url de im�genes
	 */
	private void controlesRellenarImagenesUrl() {
		
		this.controlesImagenesUrl = new HashMap<controlesNombre, String>();
		
		String preUrl = "imagenes/";
		
		this.controlesImagenesUrl.put(controlesNombre.TER_LIMPIAR,
				preUrl+controlesNombre.TER_LIMPIAR.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_GUARDAR,
				preUrl+controlesNombre.TER_GUARDAR.toString().toLowerCase()+".gif");
		this.controlesImagenesUrl.put(controlesNombre.TER_COPIAR,
				preUrl+controlesNombre.TER_COPIAR.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_IMPRIMIR,
				preUrl+controlesNombre.TER_IMPRIMIR.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVADO,
				preUrl+controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVADO.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVADO,
				preUrl+controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVADO.toString().toLowerCase()+".png");	
		this.controlesImagenesUrl.put(controlesNombre.TER_REG_LLAMADAS_DESACTIVADO,
				preUrl+controlesNombre.TER_REG_LLAMADAS_DESACTIVADO.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_REG_LLAMADAS_ACTIVADO,
				preUrl+controlesNombre.TER_REG_LLAMADAS_ACTIVADO.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_BUFFER_DESACTIVADO,
				preUrl+controlesNombre.TER_BUFFER_DESACTIVADO.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_BUFFER_ACTIVADO,
				preUrl+controlesNombre.TER_BUFFER_ACTIVADO.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_CERRAR,
				preUrl+controlesNombre.TER_CERRAR.toString().toLowerCase()+".png");
	}
	
	/**
	 * Crea el men� desplegable
	 */
	private void controlesDesplegableCrear() {
		
		//	Inicializaciones
		
		this.controlesDesplegableJCheckBoxMenuItems = new JCheckBoxMenuItem[this.controlesDesplegableNumeroMenu];
		this.controlesDesplegableMenuBar = new JMenuBar();
		this.controlesDesplegableMenu = new JMenu(this.controlesTraducciones.get(controlesNombre.TER_OPCIONES));
		boolean selected;
		
		//	Creamos items 
		
		controlesNombre itemNombre = controlesNombre.TER_LIMPIAR;
		
		this.controlesDesplegableJCheckBoxMenuItems[0] = this.controlesDesplegableGetMenuItem(itemNombre);
		
		itemNombre = controlesNombre.TER_COPIAR;
		
		this.controlesDesplegableJCheckBoxMenuItems[1] = this.controlesDesplegableGetMenuItem(itemNombre);
		
		itemNombre = controlesNombre.TER_GUARDAR;
		
		this.controlesDesplegableJCheckBoxMenuItems[2] = this.controlesDesplegableGetMenuItem(itemNombre);
		
		itemNombre = controlesNombre.TER_IMPRIMIR;
		
		this.controlesDesplegableJCheckBoxMenuItems[3] = this.controlesDesplegableGetMenuItem(itemNombre);
		
		if(controlesBotonesEstadoLimpiarPantalla) {
			itemNombre = controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVADO;
			selected = true;
		}else {
			itemNombre = controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVADO;
			selected = false;
		}
		
		this.controlesDesplegableJCheckBoxMenuItems[4] = this.controlesDesplegableGetMenuItem(itemNombre);
		this.controlesDesplegableGetMenuItemCambiado(this.controlesDesplegableJCheckBoxMenuItems[4], itemNombre, selected);
		
		if(controlesBotonesEstadoRegistroLlamadas) {
			itemNombre = controlesNombre.TER_REG_LLAMADAS_ACTIVADO;
			selected = true;
		}else {
			itemNombre = controlesNombre.TER_REG_LLAMADAS_DESACTIVADO;
			selected = false;
		}
		
		this.controlesDesplegableJCheckBoxMenuItems[5] = this.controlesDesplegableGetMenuItem(itemNombre);
		this.controlesDesplegableGetMenuItemCambiado(this.controlesDesplegableJCheckBoxMenuItems[5], itemNombre, selected);
		
		if(controlesBotonesEstadoBuffer) {
			itemNombre = controlesNombre.TER_BUFFER_ACTIVADO;
			selected = true;
		}else {
			itemNombre = controlesNombre.TER_BUFFER_DESACTIVADO;
			selected = false;
		}
		
		this.controlesDesplegableJCheckBoxMenuItems[6] = this.controlesDesplegableGetMenuItem(itemNombre);
		this.controlesDesplegableGetMenuItemCambiado(this.controlesDesplegableJCheckBoxMenuItems[6], itemNombre, selected);
		
		itemNombre = controlesNombre.TER_CERRAR;
		
		this.controlesDesplegableJCheckBoxMenuItems[7] = this.controlesDesplegableGetMenuItem(itemNombre);				
		
		//	A�adimos items
		
		this.controlesDesplegableMenu.add(this.controlesDesplegableJCheckBoxMenuItems[0]);
		this.controlesDesplegableMenu.add(this.controlesDesplegableJCheckBoxMenuItems[1]);
		this.controlesDesplegableMenu.add(this.controlesDesplegableJCheckBoxMenuItems[2]);
		this.controlesDesplegableMenu.add(this.controlesDesplegableJCheckBoxMenuItems[3]);
		
		this.controlesDesplegableMenu.add(new JSeparator());
		
		this.controlesDesplegableMenu.add(this.controlesDesplegableJCheckBoxMenuItems[4]);
		this.controlesDesplegableMenu.add(this.controlesDesplegableJCheckBoxMenuItems[5]);
		this.controlesDesplegableMenu.add(this.controlesDesplegableJCheckBoxMenuItems[6]);
		
		this.controlesDesplegableMenu.add(new JSeparator());
		
		this.controlesDesplegableMenu.add(this.controlesDesplegableJCheckBoxMenuItems[7]);
		
		//	A�adimos action listener
		
		for(JCheckBoxMenuItem item:this.controlesDesplegableJCheckBoxMenuItems)
			item.addActionListener(this);
		
		//	A�adimos
		
		this.controlesDesplegableMenuBar.add(this.controlesDesplegableMenu);
	}
	
	/**
	 * Obtiene un JCheckBoxMenuItem
	 * 
	 * @param nombre
	 * 		Determina el texto, imagen y action command
	 * 
	 * @return
	 * 		JCheckBoxMenuItem creado
	 */
	private JCheckBoxMenuItem controlesDesplegableGetMenuItem(controlesNombre nombre) {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(
				this.controlesTraducciones.get(nombre),
				new ImageIcon(GestorVentanaSRec.class.getClassLoader().getResource(this.controlesImagenesUrl.get(nombre)))
		);
		
		item.setActionCommand(nombre.toString());
		
		return item;
	}
	
	/**
	 * Cambia la imagen, texto y action command de un JCheckBoxMenuItem
	 * 
	 * @param item
	 * 		Elemento que queremos cambiar
	 * 
	 * @param nombre
	 * 		Determina el texto, imagen y action command nuevos
	 * 
	 * @param selected
	 * 		Determina si el boton estar� seleccionado o no
	 */
	private void controlesDesplegableGetMenuItemCambiado(final JCheckBoxMenuItem item, final controlesNombre nombre, final boolean selected) {
		SwingUtilities.invokeLater(new Runnable() {	
	        @Override
	        public void run() {	
	        	
	        	//	Con esta l�nea cambiamos la imagen, por ahora no se quiere as�
//				item.setIcon(
//					new ImageIcon(GestorVentanaSRec.class.getClassLoader().getResource(controlesImagenesUrl.get(nombre)))
//				);
	        	
	        	item.setSelected(selected);
				item.setText(controlesTraducciones.get(nombre));
				item.setActionCommand(nombre.toString());
				
	        }
		});
	}
	
	/**
	 * Crea la barra de botones
	 */
	private void controlesBotonesCrear() {

		//	Inicializaciones
		
		this.controlesBotonesJToolbar = new JToolBar[this.controlesBotonesNumeroJToolbar];
		this.controlesBotonesJButtons = new JButton[this.controlesBotonesNumeroJButtons];
		this.cuadroPanelBotones = new JPanel(new GridBagLayout());
		boolean selected;
		
		//	Creamos botones
		
		controlesNombre itemNombre = controlesNombre.TER_LIMPIAR;
		
		this.controlesBotonesJButtons[0] = this.controlesBotonesGetBoton(itemNombre);
		
		itemNombre = controlesNombre.TER_COPIAR;
		
		this.controlesBotonesJButtons[1] = this.controlesBotonesGetBoton(itemNombre);
		
		itemNombre = controlesNombre.TER_GUARDAR;
		
		this.controlesBotonesJButtons[2] = this.controlesBotonesGetBoton(itemNombre);
		
		itemNombre = controlesNombre.TER_IMPRIMIR;
		
		this.controlesBotonesJButtons[3] = this.controlesBotonesGetBoton(itemNombre);
		
		if(controlesBotonesEstadoLimpiarPantalla) {
			itemNombre = controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVADO;
			selected = true;
		}else {
			itemNombre = controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVADO;
			selected = false;
		}
		
		this.controlesBotonesJButtons[4] = this.controlesBotonesGetBoton(itemNombre);
		this.controlesBotonesGetBotonCambiado(this.controlesBotonesJButtons[4], itemNombre, selected);
		
		if(controlesBotonesEstadoRegistroLlamadas) {
			itemNombre = controlesNombre.TER_REG_LLAMADAS_ACTIVADO;
			selected = true;
		}else {
			itemNombre = controlesNombre.TER_REG_LLAMADAS_DESACTIVADO;
			selected = false;
		}
		
		this.controlesBotonesJButtons[5] = this.controlesBotonesGetBoton(itemNombre);
		this.controlesBotonesGetBotonCambiado(this.controlesBotonesJButtons[5], itemNombre, selected);
		
		if(controlesBotonesEstadoBuffer) {
			itemNombre = controlesNombre.TER_BUFFER_ACTIVADO;
			selected = true;
		}else {
			itemNombre = controlesNombre.TER_BUFFER_DESACTIVADO;
			selected = false;
		}
		
		this.controlesBotonesJButtons[6] = this.controlesBotonesGetBoton(itemNombre);
		this.controlesBotonesGetBotonCambiado(this.controlesBotonesJButtons[6], itemNombre, selected);
		
		itemNombre = controlesNombre.TER_CERRAR;
		
		this.controlesBotonesJButtons[7] = this.controlesBotonesGetBoton(itemNombre);
		
		//	Creamos JToolBar
		
		for(int i=0; i<this.controlesBotonesNumeroJToolbar; i++)
			this.controlesBotonesJToolbar[i] = this.controlesBotonesGetJToolBar();
		
		//	A�adimos botones a JToolbar
		
		this.controlesBotonesJToolbar[0].add(this.controlesBotonesJButtons[0]);
		this.controlesBotonesJToolbar[0].add(this.controlesBotonesJButtons[1]);
		this.controlesBotonesJToolbar[0].add(this.controlesBotonesJButtons[2]);
		this.controlesBotonesJToolbar[0].add(this.controlesBotonesJButtons[3]);
		
		this.controlesBotonesJToolbar[1].add(this.controlesBotonesJButtons[4]);
		this.controlesBotonesJToolbar[1].add(this.controlesBotonesJButtons[5]);
		this.controlesBotonesJToolbar[1].add(this.controlesBotonesJButtons[6]);
		
		this.controlesBotonesJToolbar[2].add(this.controlesBotonesJButtons[7]);
		
		//	A�adimos JToolbar al panel
		
		for(int i=0;i<this.controlesBotonesNumeroJToolbar;i++) {
			GridBagConstraints g = this.variosGetConstraints(i, 0, 1, 1, 0, 0, false, GridBagConstraints.CENTER);
			g.insets = new Insets(2, 2, 2, 5);
			this.cuadroPanelBotones.add(this.controlesBotonesJToolbar[i],g);
		}
		
		//	A�adimos action listener y dimensi�n a los botones
		
		for(JButton item : this.controlesBotonesJButtons) {
			item.addActionListener(this);
		}
	}
	
	/**
	 * Obtiene un JButton
	 * 
	 * @param nombre
	 * 		Determina el texto, imagen y action command
	 * 
	 * @return
	 * 		JButton creado
	 */
	private JButton controlesBotonesGetBoton(controlesNombre nombre) {
		JButton item = new JButton(
				new ImageIcon(GestorVentanaSRec.class.getClassLoader().getResource(this.controlesImagenesUrl.get(nombre)))
		);
		item.setToolTipText(this.controlesTraducciones.get(nombre));
		item.setActionCommand(nombre.toString());
		
		return item;
	}
	
	/**
	 * Cambia la imagen, tooltip y action command de un JButton
	 * 
	 * @param boton
	 * 		Bot�n que queremos cambiar
	 * 
	 * @param nombre
	 * 		Determina el tooltip, imagen y action command nuevos
	 * 
	 * @param selected
	 * 		Determina si el boton estar� seleccionado o no
	 */
	private void controlesBotonesGetBotonCambiado(final JButton boton, final controlesNombre nombre, final boolean selected) {
		SwingUtilities.invokeLater(new Runnable() {	
	        @Override
	        public void run() {	
	        	
	        	//	Con esta l�nea cambiamos la imagen, por ahora no se quiere as�
//				boton.setIcon(
//					new ImageIcon(GestorVentanaSRec.class.getClassLoader().getResource(controlesImagenesUrl.get(nombre)))
//				);
	        	
	        	boton.setSelected(selected);
				boton.setToolTipText(controlesTraducciones.get(nombre));
				boton.setActionCommand(nombre.toString());
				
	        }
		});
	}	        
	
	/**
	 * Obtiene un JToolbar
	 * 
	 * @return
	 * 		JToolbar creado
	 */
	private JToolBar controlesBotonesGetJToolBar() {
		JToolBar jt = new JToolBar();
		jt.setBorderPainted(true);
		jt.setFloatable(false);		
		jt.setBorder(new MetalBorders.PaletteBorder());
		return jt;
	}	
	
	/**
	 * M�todo que cambia la apariencia del bot�n limpiar pantalla tras llamada
	 * a m�todo
	 */
	private void controlesBotonesCambiarLimpiarPantalla() {
		
		controlesNombre itemNombre;
		boolean selected;
		
		if(this.controlesBotonesEstadoLimpiarPantalla) {
			itemNombre = controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVADO;
			selected = true;
		}else {
			itemNombre = controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVADO;
			selected = false;
		}
		
		this.controlesBotonesGetBotonCambiado(this.controlesBotonesJButtons[4], itemNombre, selected);		
		this.controlesDesplegableGetMenuItemCambiado(this.controlesDesplegableJCheckBoxMenuItems[4], itemNombre, selected);
	}
	
	/**
	 * M�todo que cambia la apariencia del bot�n registro llamadas
	 */
	private void controlesBotonesCambiarRegistroLlamadas() {
		
		controlesNombre itemNombre;
		boolean selected;
		
		if(this.controlesBotonesEstadoRegistroLlamadas) {
			itemNombre = controlesNombre.TER_REG_LLAMADAS_ACTIVADO;
			selected = true;
		}else {
			itemNombre = controlesNombre.TER_REG_LLAMADAS_DESACTIVADO;
			selected = false;
		}
		
		this.controlesBotonesGetBotonCambiado(this.controlesBotonesJButtons[5], itemNombre, selected);
		this.controlesDesplegableGetMenuItemCambiado(this.controlesDesplegableJCheckBoxMenuItems[5], itemNombre, selected);
	}
	
	/**
	 * M�todo que cambia la apariencia del bot�n del buffer
	 */
	private void controlesBotonesCambiarBuffer() {
		
		controlesNombre itemNombre;
		boolean selected;
		
		if(this.controlesBotonesEstadoBuffer) {
			itemNombre = controlesNombre.TER_BUFFER_ACTIVADO;
			selected = true;
		}else {
			itemNombre = controlesNombre.TER_BUFFER_DESACTIVADO;
			selected = false;
		}
		
		this.controlesBotonesGetBotonCambiado(this.controlesBotonesJButtons[6], itemNombre, selected);
		this.controlesDesplegableGetMenuItemCambiado(this.controlesDesplegableJCheckBoxMenuItems[6], itemNombre, selected);
	}
	
	//***************************************
    // 			CONTROLES ACCIONES
    //***************************************
	
	/**
	 * M�todo que se ejecuta cuando pulsan el bot�n de limpiar pantalla
	 */
	private void controlesAccionLimpiar() {
		this.setSalidasVacias(false);
	}

	/**
	 * M�todo que se ejecuta cuando pulsan el bot�n de copiar
	 */
	private void controlesAccionCopiar() {
		
		//	No copiamos el texto hasta que la EDT se encuentre inactiva
		
		new Thread(new Runnable() {
		    @Override
		    public void run() {				
		    	try {
					SwingUtilities.invokeAndWait(new Thread(new Runnable() {
						@Override
						public void run() {
							String salidasTexto = getSalidasTextos();
							
							StringSelection stringSelection = new StringSelection(salidasTexto);
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null); 							
						}
					}));					
					
				} catch (InvocationTargetException e) {

				} catch (InterruptedException e) {

				}
		    }
		}).start();	
	}

	/**
	 * M�todo que se ejecuta cuando pulsan el bot�n de guardar
	 */
	private void controlesAccionGuardar() {
		
		//	No guardamos el texto hasta que la EDT se encuentre inactiva
	
		new Thread(new Runnable() {
		    @Override
		    public void run() {				
		    	try {
					SwingUtilities.invokeAndWait(new Thread(new Runnable() {
						@Override
						public void run() {
							
							//	Creamos file chooser
							
							final JFileChooser chooser = new JFileChooser();	    
						    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						    chooser.setSelectedFile(new File(salidaTextoNombreArchivo+".txt"));	    
						    chooser.setDialogTitle(salidaTextoGuardarTitulo);
						    chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
						    
						    //	Mostramos di�logo
						    
						    int retVal = chooser.showSaveDialog(cuadroDialogo);
						    
						    //	Aceptar pulsado
						    
						    if (retVal == JFileChooser.APPROVE_OPTION) {
						    	
						    	//	Escribimos texto
						    	
						    	BufferedWriter writer = null;
						    	
						        try {
						            
						            File file = chooser.getSelectedFile();
						            writer = new BufferedWriter(new FileWriter(file));
						            writer.write(getSalidasTextos());
						            
						        } catch (Exception e) {	            
						        } finally {
						        	try {
						                writer.close();
						            } catch (Exception e) {
						            }
						        }
						    }
						}
					}));					
					
				} catch (InvocationTargetException e) {
	
				} catch (InterruptedException e) {
	
				}
		    }
		}).start();	
	}

	/**
	 * M�todo que se ejecuta cuando pulsan el bot�n de imprimir
	 */
	private void controlesAccionImprimir() {
		
		//	No imprimimos el texto hasta que la EDT se encuentre inactiva
		
		new Thread(new Runnable() {
		    @Override
		    public void run() {				
		    	try {
					SwingUtilities.invokeAndWait(new Thread(new Runnable() {
						@Override
						public void run() {
							
							//	Obtenemos datos
							
							getImprimirDatos();							
							
							//	Di�logo con n� de p�ginas
							
					        PrintRequestAttributeSet printAttribute = new HashPrintRequestAttributeSet();
							printAttribute.add(
								new PageRanges(
										1, salidaTextoImprimirNumeroPaginas == 0 ? 1 : salidaTextoImprimirNumeroPaginas
								)
							);   
							
							//	Creamos impresora
							
					        PrinterJob job = PrinterJob.getPrinterJob();    
						    job.setJobName(salidaTextoNombreArchivo);
						    job.setPrintable(CuadroTerminal.this);
						    
							if (job.printDialog(printAttribute)) {			
								try {
									
									//	Imprimimos
									
									job.print();
									
									//	Limpiamos
									
									setImprimirDatosVacios();
									
								} catch (Exception e) {
									
								}			
							}
							
						}
					}));					
					
				} catch (InvocationTargetException e) {

				} catch (InterruptedException e) {

				}
		    }
		}).start();			
	}

	/**
	 * M�todo que se ejecuta cuando pulsan el bot�n de limpiar pantalla 
	 * tras llamada a m�todo
	 */
	private void controlesAccionLimpiarPantalla() {
		
		this.controlesBotonesEstadoLimpiarPantalla = 
				!this.controlesBotonesEstadoLimpiarPantalla;		
				
		this.controlesBotonesCambiarLimpiarPantalla();
	}

	/**
	 * M�todo que se ejecuta cuando pulsan el bot�n de registro de llamadas a m�todo
	 */
	private void controlesAccionRegistroLlamadas() {		
		
		this.controlesBotonesEstadoRegistroLlamadas =
				!this.controlesBotonesEstadoRegistroLlamadas;
		
		this.controlesBotonesCambiarRegistroLlamadas();
	}

	/**
	 * M�todo que se ejecuta cuando pulsan el bot�n del buffer
	 */
	private void controlesAccionBuffer() {
		
		this.controlesBotonesEstadoBuffer =
				!this.controlesBotonesEstadoBuffer;
		
		this.controlesBotonesCambiarBuffer();
		
		this.panelesPanelErrorTexto.setBufferIlimitado(this.controlesBotonesEstadoBuffer);
		
		this.panelesPanelNormalTexto.setBufferIlimitado(this.controlesBotonesEstadoBuffer);
	}

	/**
	 * M�todo que se ejecuta cuando pulsan el bot�n de cerrar
	 */
	private void controlesAccionCerrar() {
		Ventana.thisventana.terminalAbrirCerrar();
	}
	
	
	//***************************************
    // 			PANELES
    //***************************************
	
	/**
	 * Inicializa los paneles de salida normal y error
	 */
	private void panelesInicializar() {
		
		//	Inicializamos general
		
		this.cuadroPanelSalidas = new JPanel(new GridBagLayout());
		
		//	Creamos hijos
		
		this.panelesNormalCrear();
		this.panelesErrorCrear();
		this.panelesSeparadorCrear();
		
		//	A�adimos a panel general
		
		this.cuadroPanelSalidas.add(
			this.panelesPanelSplitPane,
			this.variosGetConstraints(0, 0, 1, 1, 1, 1, true, GridBagConstraints.CENTER)
		);
	}
	
	/**
	 * Crea el panel de salida normal
	 */
	private void panelesNormalCrear() {		
		
		//	Inicializaciones	
		
		this.panelesPanelNormalTexto = new panelTextoClase(this.salidaBuffer, true);
		
		this.panelesPanelNormalScroll = new JScrollPane(
				this.panelesPanelNormalTexto.getPanelTexto(),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				
		this.panelesPanelNormal = new JPanel(new GridBagLayout());
		
		this.panelesPanelNormalTexto.setJScrollPane(this.panelesPanelNormalScroll);
		
		//	A�adimos
		
		this.panelesPanelNormal.add(
			this.panelesPanelNormalScroll,
			this.variosGetConstraints(0, 0, 1, 1, 1, 1, true, GridBagConstraints.CENTER)
		);		
	}
	
	/**
	 * Crea el panel de salida de error
	 */
	private void panelesErrorCrear() {
		
		//	Inicializaciones		
		
		this.panelesPanelErrorTexto = new panelTextoClase(this.salidaBuffer, false);
		
		this.panelesPanelErrorScroll = new JScrollPane(
				this.panelesPanelErrorTexto.getPanelTexto(),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				
		this.panelesPanelError = new JPanel(new GridBagLayout());
		
		this.panelesPanelErrorTexto.setJScrollPane(this.panelesPanelErrorScroll);
		
		//	A�adimos
		
		this.panelesPanelError.add(
			this.panelesPanelErrorScroll,
			this.variosGetConstraints(0, 0, 1, 1, 1, 1, true, GridBagConstraints.CENTER)
		);
	}
	
	/**
	 * Crea el panel que contendr� ambos paneles
	 */
	private void panelesSeparadorCrear() {
		
		//	Inicializaciones
		
		this.panelesPanelSplitPane = 
			new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				this.panelesPanelNormal,
				this.panelesPanelError
			);
		
		this.panelesPanelSplitPane.setDividerLocation(this.panelesPanelSplitPaneValor);
		this.panelesPanelSplitPane.setResizeWeight(0.5);		
	}
	
	/**
	 * Refresca el separador en funci�n de si se tiene
	 * que mostrar el panel de error o no
	 */
	private void panelesSeparadorRefrescar() {			
		if(this.panelesPanelErrorTexto.hanEscrito) {
			this.panelesPanelSplitPane.setDividerLocation(this.panelesPanelSplitPaneValor);
		}else {
			this.panelesPanelSplitPane.setDividerLocation(1.0);
		}
	}
	
	//***************************************
	//		SALIDAS
	//***************************************
	
	/**
	 * Establece el color de la salida de error
	 */
	private void setSalidaColorError() {
		this.panelesPanelErrorTexto.setEstiloNormal(
				this.salidaTextoErrorColor,
				this.salidaTextoErrorNegrita,
				this.salidaTextoErrorTamano,
				this.salidaTextoFuenteGeneral
		);
	}	
	
	/**
	 * Establece el color de la salida normal
	 */
	private void setSalidaColorNormal() {
		this.panelesPanelNormalTexto.setEstiloNormal(
				this.salidaTextoNormalColor,
				this.salidaTextoNormalNegrita,
				this.salidaTextoNormalTamano,
				this.salidaTextoFuenteGeneral
		);
	}
	
	/**
	 * Establece el color del resultado del m�todo
	 */
	private void setSalidaColorResultadoMetodo() {
		this.panelesPanelNormalTexto.setEstiloResultadoMetodo(
				this.salidaTextoNormalResultadoMetodoColor, 
				this.salidaTextoNormalResultadoMetodoNegrita, 
				this.salidaTextoNormalResultadoMetodoTamano, 
				this.salidaTextoFuenteGeneral);
	}
	
	/**
	 * Establece el color de la cabecera para ambas salidas
	 */
	private void setSalidaColorCabecera() {
		this.panelesPanelNormalTexto.setEstiloCabecera(
				this.salidaTextoCabeceraColor,
				this.salidaTextoCabeceraNegrita,
				this.salidaTextoCabeceraTamano,
				this.salidaTextoFuenteGeneral
		);
		this.panelesPanelErrorTexto.setEstiloCabecera(
				this.salidaTextoCabeceraColor,
				this.salidaTextoCabeceraNegrita,
				this.salidaTextoCabeceraTamano,
				this.salidaTextoFuenteGeneral
		);
	}	
	
	/**
	 * Vac�a ambas salidas, normal y error
	 * 
	 * @param comprobar
	 * 		Indica si hay que comprobar la variable
	 * 		controlesBotonesEstadoLimpiarPantalla
	 */
	private void setSalidasVacias(boolean comprobar) {
		if(!comprobar || this.controlesBotonesEstadoLimpiarPantalla) {            
			panelesPanelNormalTexto.setSalidaVacia();
			panelesPanelErrorTexto.setSalidaVacia();
			panelesSeparadorRefrescar();
		}   
	}
	
	/**
	 * Obtiene el texto completo de ambas salidas 
	 * junto con la cabecera de exportar
	 * 
	 * @return
	 * 		String de ambas salidas 		
	 */
	private String getSalidasTextos() {
		
		String retorno = "";
		
		retorno += this.salidaTextoCabeceraExportarNormal;
		
		retorno += this.panelesPanelNormalTexto.getTextoSalida();
		
		retorno += this.salidaTextoCabeceraExportarError;
		
		retorno += this.panelesPanelErrorTexto.getTextoSalida();
		
		return retorno;
	}
	
	//***************************************
    // 			IMPRIMIR
    //***************************************
	
	/**  
	 * Rellena el texto a imprimir.
	 * 
	 * Calcula:
	 * 		- Fuente
	 * 		- Fuente height
	 * 		- N� p�ginas
	 * 		- N� l�neas por p�gina
	 * 		
	 */
	private void getImprimirDatos() {
		
		//	Fuente
		
		this.salidaTextoImprimirFuente = 
				new Font(this.salidaTextoFuenteGeneral, Font.PLAIN, this.salidaTextoNormalTamano);
		
		//	Fuente height
		
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = image.createGraphics();
		FontMetrics metrics = graphics2D.getFontMetrics(this.salidaTextoImprimirFuente);
		this.salidaTextoImprimirFuenteHeight = metrics.getHeight();
		
		//	Texto a imprimir
		
		this.salidaTextoImprimirContenido = new String[0];		
		
		this.salidaTextoImprimirContenido = 
				this.getImprimirContenido();

		//	L�neas por p�gina		
		
		this.salidaTextoImprimirLineasPorPagina =
				(int)(new PageFormat().getImageableHeight()/this.salidaTextoImprimirFuenteHeight);
		
		//	N�mero p�ginas

		this.salidaTextoImprimirNumeroPaginas =
			 (salidaTextoImprimirContenido.length-1)/this.salidaTextoImprimirLineasPorPagina;
		
	}
	
	/**
	 * Vac�a todas las variables rellenadas anteriormente
	 * para ahorrar memoria.
	 * 
	 * 		- Texto a imprimir
	 * 		- Fuente
	 * 		- Fuente height
	 * 		- N� de p�ginas
	 * 		- N� de l�neas por p�gina
	 */
	private void setImprimirDatosVacios() {
		this.salidaTextoImprimirContenido = null;
		this.salidaTextoImprimirFuente = null;
		this.salidaTextoImprimirNumeroPaginas = 0;
		this.salidaTextoImprimirLineasPorPagina = 0;
		this.salidaTextoImprimirFuenteHeight = 0;
	}
	
	/**
	 * Obtiene el texto a imprimir que no es mas que el de las salidas corregidas,
	 * hay que pasar del string de java al string necesario para el PDF:
	 * 
	 * - Un split por cada salto de l�nea y una correccion por si cada
	 * l�nea de las obtenidas ocupa mas de lo visualmente permitido
	 * 
	 * - Se cambian tabuladores por 3 espacios
	 */
	private String[] getImprimirContenido() {
		
		//	Obtenemos texto de las salidas
		
		String textoSalidas =
				this.getSalidasTextos();
		
		//	Correcci�n tabuladores
		
		textoSalidas =
				textoSalidas.replaceAll("\t", "   ");
		
		//	Obtenemos texto con split
		
		String[] textoSalidasSplit = 
				textoSalidas.split("\n");		
		
		//	Correcci�n salto de l�nea	
		
		String textoCorregidoSinSplit = "";
		
		for(String textoLinea : textoSalidasSplit) {
			if(textoLinea.length()>this.salidaTextoImprimirNumeroCaracteresLinea) {
				textoLinea = textoLinea.replaceAll("(.{"+this.salidaTextoImprimirNumeroCaracteresLinea+"})", "$1\n");
			}
			textoCorregidoSinSplit += textoLinea+"\n";
		}
		
		//	Retornamos
		
		return textoCorregidoSinSplit.split("\n");
	}
	
	//***************************************
    // 			VARIOS
    //***************************************
	
	/**
	 * Obtiene GridBagConstraints
	 * 
	 * @param x
	 * 		Empieza en la columna x
	 * 
	 * @param y
	 * 		Empieza en la fila y
	 * 
	 * @param xS
	 * 		Ocupa xS columnas
	 * 
	 * @param yS
	 * 		Ocupa yS filas
	 * 
	 * @param weightX
	 * 		Relleno horizontal
	 * 
	 * @param weightY
	 * 		Relleno vertical
	 * 
	 * @param fill
	 * 		Rellena en ambos sentidos o no
	 * 
	 * @param align
	 * 		Alineaci�n del contenido
	 * 
	 * @return
	 * 		GridBagConstraints
	 */
	private GridBagConstraints variosGetConstraints(int x, int y, int xS, int yS, int weightX, int weightY, boolean fill, int align) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = x;
		constraints.gridy = y; 
		constraints.gridwidth = xS;
		constraints.gridheight = yS;
		constraints.weightx = weightX;
		constraints.weighty = weightY;
		if(fill) {
			constraints.fill = GridBagConstraints.BOTH;
		}
		constraints.anchor = align;
		return constraints;
	}
	
	//********************************************************************************
    // 			LISTENERS
    //********************************************************************************
	
	//***************************************
    // 	CUADRO TERMINAL: WINDOW LISTENER
    //***************************************
	
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.cuadroVentana.terminalAbrirCerrar();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	//***************************************
    // 	CONTROLES: ACTION LISTENER
    //***************************************
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String origen = e.getActionCommand();
		
		if(origen.equals(controlesNombre.TER_LIMPIAR.toString())) {
			
			controlesAccionLimpiar();
			
		}else if(origen.equals(controlesNombre.TER_COPIAR.toString())) {
			
			controlesAccionCopiar();
			
		}else if(origen.equals(controlesNombre.TER_GUARDAR.toString())) {
			
			controlesAccionGuardar();
			
		}else if(origen.equals(controlesNombre.TER_IMPRIMIR.toString())) {
			
			controlesAccionImprimir();
			
		}else if(origen.equals(controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVADO.toString()) ||
				origen.equals(controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVADO.toString())) {
			
			controlesAccionLimpiarPantalla();
			
		}else if(origen.equals(controlesNombre.TER_REG_LLAMADAS_ACTIVADO.toString()) ||
				origen.equals(controlesNombre.TER_REG_LLAMADAS_DESACTIVADO.toString())) {
			
			controlesAccionRegistroLlamadas();
			
		}else if(origen.equals(controlesNombre.TER_BUFFER_DESACTIVADO.toString()) ||
				origen.equals(controlesNombre.TER_BUFFER_ACTIVADO.toString())) {
			
			controlesAccionBuffer();
			
		}else if(origen.equals(controlesNombre.TER_CERRAR.toString())) {
			
			controlesAccionCerrar();
			
		}
	}
	
	//***************************************
    // 	SALIDAS: IMPRIMIR
    //***************************************
	
	@Override
	public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
		
		//	TODO Implementar con colores, ser�a dificil pero es posible	
		
		//	Rellenamos array de n�mero de p�ginas para pintar
		
		int[] pageBreaks = new int[this.salidaTextoImprimirNumeroPaginas];
		for (int b=0; b<salidaTextoImprimirNumeroPaginas; b++) {
		    pageBreaks[b] = (b+1)*salidaTextoImprimirLineasPorPagina; 
		}
		
		//	Excedido n�mero p�ginas --> Fuera
			
		if (pageIndex > pageBreaks.length)
			return NO_SUCH_PAGE;		
		
		
		//	Dibujamos
			
		Graphics2D g2d = (Graphics2D)g;
		g.setFont(this.salidaTextoImprimirFuente);
		g2d.translate(pf.getImageableX(), pf.getImageableY()); 
		
		int y = 0; 
		int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex-1];
		int end   = (pageIndex == pageBreaks.length)
		                 ? this.salidaTextoImprimirContenido.length : pageBreaks[pageIndex];
		for (int line=start; line<end; line++) {
		    y += this.salidaTextoImprimirFuenteHeight;
		    g.drawString(salidaTextoImprimirContenido[line], 0, y);
		}	
		
		return PAGE_EXISTS;   
	}
	
	
	//********************************************************************************
    // 			CLASE PRIVADA BYTE ARRAY OUTPUT STREAM
    //********************************************************************************
	
	
	private class panelTextoClase extends ByteArrayOutputStream{
		
		
		//********************************************************************************
		// 			VARIABLES
		//********************************************************************************
		

		private JTextPane panelTexto;
		private StringBuilder sb;
		private StyledDocument doc;
		private int bufferLimite;
		private boolean bufferIlimitadoActivado;
		private SimpleAttributeSet estiloNormal, estiloCabecera, estiloResultadoMetodo;
		private String cabecera, resultadoMetodo;
		private JScrollPane panelScroll;
		private final ReentrantLock bloqueo;
		private boolean abrirModificar, abrir, hanEscrito;
		private boolean esSalidaNormal;
		
		
		//******************************************************************************
		// 			CONSTRUCTOR
		//********************************************************************************
		
		
		/**
		 * L�mite del buffer a aplicar
		 * 
		 * @param bufferLimite
		 * 		L�mite del buffer en caracteres
		 * 
		 * @param esSalidaNormal
		 * 		Indica si esta clase se aplica a la salida
		 * 		normal o la de error
		 * 
		 */
		public panelTextoClase(int bufferLimite, boolean esSalidaNormal) {
			   
			//	Inicializaciones
				   
			this.panelTexto = new JTextPane() {
				
				private static final long serialVersionUID = 1L;

				//	Para scroll horizontal
				@Override
			    public boolean getScrollableTracksViewportWidth() {
			        return getUI().getPreferredSize(this).width
			                        <= getParent().getSize().width;
			    }

			};
			this.panelTexto.setEditable(false);			
			this.sb = new StringBuilder();
			this.doc = panelTexto.getStyledDocument();
			this.bufferLimite = bufferLimite;
			this.bufferIlimitadoActivado = false;
			this.estiloNormal = new SimpleAttributeSet();
			this.estiloCabecera = new SimpleAttributeSet();
			this.estiloResultadoMetodo = new SimpleAttributeSet();
			this.cabecera = "";	
			this.resultadoMetodo = "";
            this.bloqueo = new ReentrantLock(true);
			this.abrirModificar = true;
			this.abrir = false;
			this.hanEscrito = false;
			this.esSalidaNormal = esSalidaNormal;			
		}
		
		
		//********************************************************************************
		// 			M�TODOS PRIVADOS
		//********************************************************************************	
		
		
		//***************************************
	    // 			ESTILOS
	    //***************************************
		
		
		/**
		 * Establece el estilo normal del texto que se mostrar�
		 * 
		 * @param texto
		 * 		Color.CONSTANTE
		 * 
		 * @param negrita
		 * 		Texto en negrita o no
		 * 
		 * @param tamanio
		 * 		Tama�o de la letra
		 * 
		 * @param fuente
		 * 		Nombre de la fuente
		 * 
		 */
		private void setEstiloNormal(Color texto, boolean negrita, int tamanio, String fuente) {			
			SimpleAttributeSet keyWord = new SimpleAttributeSet();
			
			StyleConstants.setForeground(keyWord, texto);
			StyleConstants.setBold(keyWord, negrita);
			StyleConstants.setFontFamily(keyWord, fuente);
			StyleConstants.setFontSize(keyWord, tamanio);			
			
			this.estiloNormal = keyWord;
		}
		
		/**
		 * Establece el estilo de la cabecera del texto que se mostrar�
		 * 
		 * @param texto
		 * 		Color.CONSTANTE
		 * 
		 * @param negrita
		 * 		Texto en negrita o no
		 * 
		 * @param tamanio
		 * 		Tama�o de la letra
		 * 
		 * @param fuente
		 * 		Nombre de la fuente
		 * 
		 */
		private void setEstiloCabecera(Color texto, boolean negrita, int tamanio, String fuente) {			
			SimpleAttributeSet keyWord = new SimpleAttributeSet();
			
			StyleConstants.setForeground(keyWord, texto);
			StyleConstants.setBold(keyWord, negrita);
			StyleConstants.setFontFamily(keyWord, fuente);
			StyleConstants.setFontSize(keyWord, tamanio);	
			
			this.estiloCabecera = keyWord;
		}
		
		/**
		 * Establece el estilo del resultado del m�todo del texto que se mostrar�
		 * 
		 * @param texto
		 * 		Color.CONSTANTE
		 * 
		 * @param negrita
		 * 		Texto en negrita o no
		 * 
		 * @param tamanio
		 * 		Tama�o de la letra
		 * 
		 * @param fuente
		 * 		Nombre de la fuente
		 * 
		 */
		private void setEstiloResultadoMetodo(Color texto, boolean negrita, int tamanio, String fuente) {			
			SimpleAttributeSet keyWord = new SimpleAttributeSet();
			
			StyleConstants.setForeground(keyWord, texto);
			StyleConstants.setBold(keyWord, negrita);
			StyleConstants.setFontFamily(keyWord, fuente);
			StyleConstants.setFontSize(keyWord, tamanio);	
			
			this.estiloResultadoMetodo = keyWord;
		}		
		
		//***************************************
	    // 		ESCRIBIR
	    //***************************************
		
		
		/**
		 * Establece el valor de la cabecera
		 * 
		 * @param cabecera
		 * 		Valor de la cabecera
		 */
		private void setCabecera(String cabecera) {
			if(CuadroTerminal.this.controlesBotonesEstadoRegistroLlamadas)
				this.cabecera = cabecera;
			else
				this.cabecera = "\n";
			
			//	TODO Eliminar las dos siguientes l�neas si quieren que la terminal se abra autom�ticamente cuando
			//	recibe una salida normal y real por primera vez
			
			if(this.esSalidaNormal)
				this.setEscribirWrite();
		}	
		
		/**
		 * Establece el resultado del m�todo
		 * @param s
		 * 		Valor de resultado del m�todo
		 */
		private void setSalidaResultadoMetodo(String s) {
			if(this.esSalidaNormal)
				this.resultadoMetodo = Texto.get("TER_RESULTADO_METODO", Conf.idioma) + s + "\n";
		}
		
		/**
		 * Escribe el texto pasado como par�metro
		 * incluyendo la cabecera o no
		 * 
		 * @param text
		 * 		Texto a escribir
		 * 
		 * @param cabeceraP
		 * 		Indica si hay que escribir la cabecera o no
		 * 
		 * @param metodoResultado
		 * 		Indica si hay que escribir el resultado del m�todo o no
		 */
		private void escribir(final String text, final boolean cabeceraP, final boolean metodoResultado) {
			SwingUtilities.invokeLater(new Runnable() {
                public void run() {
					try {
						bloqueo.lock();
						
						//	Cabecera
						
						if(cabeceraP && !cabecera.equals("")) {
							doc.insertString(doc.getLength(), cabecera, estiloCabecera);
							cabecera = "";
						}
						
						//	Escribir con o sin resultado de m�todo
						
						if(!metodoResultado) {
							doc.insertString(doc.getLength(), text, estiloNormal);
						}else if(metodoResultado && !resultadoMetodo.equals("")) {							
							doc.insertString(doc.getLength(), resultadoMetodo, estiloResultadoMetodo);	
							resultadoMetodo = "";
						}
						
						panelTexto.setDocument(doc);						
						
						bloqueo.unlock();
						
						setScrollAbajo();
						
					}catch(Exception e) {
						
					}
                }
			});
		}		
		
		
		//***************************************
	    // 		ABRIR TERMINAL TRAS ESCRIBIR
	    //***************************************
		
		
		/**
		 * Establece las variables en cada Write que despu�s permitir�n
		 * saber si debemos abrir la terminal autom�ticamente o no
		 */
		private void setEscribirWrite() {			
			if(this.esSalidaNormal && this.abrirModificar) 				
				this.abrir = true;		
			
			else if(this.esSalidaNormal && !this.abrirModificar)				
				this.abrir = false;
			
			else 				
				this.abrir = true;
			
			this.hanEscrito = true;
		}
		
		/**
		 * Reinicializa los valores necesarios cuando una llamada
		 * a un m�todo termina de escribir en los paneles.
		 * Hacer tras llamada getTerminalAbrir, no antes.
		 */
		private void setEscribirFin() {			
			if(this.esSalidaNormal) {					
				this.abrir = false;
				this.abrirModificar = false;
				this.escribir(this.resultadoMetodo, true, true);
			}else {
				this.abrir = false;
				this.abrirModificar = true;
			}
		}		
		
		/**
		 * Obtiene, tras la escritura en ambos paneles,
		 * si la terminal se tiene que abrir o no
		 * 
		 * @return
		 * 		True si la terminal se tiene que abrir,
		 * 		false caso contrario
		 */
		private boolean getTerminalAbrir() {
			return this.abrir;
		}
		
		
		//***************************************
	    // 		SCROLL
	    //***************************************
		
		
		/**
		 * Establece el JScrollPane asociado a la salida
		 * 
		 * @param p
		 * 		JScrollPane asociado a la salida
		 */
		private void setJScrollPane(JScrollPane p) {
			this.panelScroll = p;
		}
		
		/**
		 * Establece el scroll del panel abajo
		 */
		private void setScrollAbajo() {	
			SwingUtilities.invokeLater(new Runnable() {
                public void run() {
					try {
						bloqueo.lock();
						
						JScrollBar vertical = panelScroll.getVerticalScrollBar();
						vertical.setValue( vertical.getMaximum() );
						
						bloqueo.unlock();
					}catch(Exception e) {
						
					}
                }
			});
		}
		
		
		//***************************************
	    // 		VACIAR
	    //***************************************
		
		
		/**
		 * Vacia el contenido de la salida/panel
		 */		
		private void setSalidaVacia() {
			
			this.hanEscrito = false;
			
			SwingUtilities.invokeLater(new Runnable() {
                public void run() {
					try {	
						
						bloqueo.lock();
						
						doc.remove(0, doc.getLength());
						
						panelTexto.setDocument(doc);
						
						bloqueo.unlock();
						
						setScrollAbajo();
						
					}catch(Exception e) {
						
					}
                }
			});
		}
		
		//***************************************
	    // 		BUFFER
	    //***************************************
		
		/**
		 * Establece si el buffer ilimitado estar� activado o no
		 * 
		 * @param bufferIlimitadoActivado
		 * 	Buffer ilimitado activado o no
		 */
		private void setBufferIlimitado(boolean bufferIlimitadoActivado) {
			this.bufferIlimitadoActivado = bufferIlimitadoActivado;
			if(!this.bufferIlimitadoActivado)
				this.setBufferRecalcular();
		}
		
		/**
		 * Recalcula el buffer, es decir, vac�a lo necesario del
		 * documento si lo ponen como limitado y excede el tama�o
		 */
		private void setBufferRecalcular() {
						
			int longitud = doc.getLength();
			
			if(!bufferIlimitadoActivado && longitud > bufferLimite && longitud>0) {	
				int fin  = doc.getLength()-bufferLimite;
				try {
					doc.remove(0, fin);
				} catch (BadLocationException e) {
				}
				panelTexto.setDocument(doc);				
			}					

		}
		
		
		//***************************************
	    // 		GET TEXTO SALIDA
	    //***************************************
		
		
		/**
		 * Obtiene el texto de la salida
		 * 
		 * @return
		 * 		String con el texto de la salida
		 */
		private String getTextoSalida() {
			
			try {
				return doc.getText(0, doc.getLength());
			} catch (BadLocationException e) {
				return "";
			}			
				
		}		
		
		
		//***************************************
	    // 		GET PANEL TEXTO
	    //***************************************
		
		
		/**
		 * Obtiene el panel texto contenido en esta clase
		 * 
		 * @return
		 * 		Panel texto contenido en esta clase
		 */
		private JTextPane getPanelTexto() {
			return this.panelTexto;
		}
		
		
		//********************************************************************************
		// 			M�TODOS OUTPUT STREAM
		//********************************************************************************
		
		
		@Override
		public void flush() {
			
			if(sb.toString().equals(""))
				return;
			
			this.setEscribirWrite();
			
			String text = sb.toString() + "\n";
			escribir(text, false, false);	
			
			sb.setLength(0);
			return;
		}

		@Override
		public void close() {
			this.flush();
		}		
		
		@Override
		public void write(byte[] b) {
			
			this.setEscribirWrite();
			
			String text = new String(b);
			escribir(text, true, false);			
			 
			sb.setLength(0);
			return;
		}
		
		@Override
		public void write(byte[] b, int off, int len){
			
			this.setEscribirWrite();
			
			String text = new String(b, off, len);
			escribir(text, true, false);
			 
			sb.setLength(0);
			return;
		}
	}
}
