package cuadros;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import conf.Conf;
import utilidades.Texto;
import ventanas.Ventana;

public class CuadroTerminal implements WindowListener, ActionListener{
	
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
	
	//	Traducciones e imágenes
	
	private enum controlesNombre {
		TER_LIMPIAR, TER_GUARDAR, TER_COPIAR, TER_IMPRIMIR, TER_LIMPIAR_PANTALLA_ACTIVAR, TER_LIMPIAR_PANTALLA_DESACTIVAR,
		TER_REG_LLAMADAS_ACTIVAR, TER_REG_LLAMADAS_DESACTIVAR, TER_BUFFER_ACTIVAR, TER_BUFFER_DESACTIVAR, TER_CERRAR,
		TER_OPCIONES
	}
	
	private Map<controlesNombre,String> controlesTraducciones;
	private Map<controlesNombre,String> controlesImagenesUrl;
	
	//	Desplegable	
	
	private final int controlesDesplegableNumeroMenu = 8;	
	private JMenu controlesDesplegableMenu;
	private JMenuItem[] controlesDesplegableJMenuItems;
	private JMenuBar controlesDesplegableMenuBar;
	
	//	Botones
	
	private final int controlesBotonesNumeroJButtons = 8;
	private final int controlesBotonesNumeroJToolbar = 3;
	private JToolBar[] controlesBotonesJToolbar;	
	private JButton[] controlesBotonesJButtons;	
	
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
	
	//	Estilo
	
	private final String salidaTextoFuenteGeneral = "Monospaced";
	
	private final Color salidaTextoNormalColor = Color.BLACK;
	private final int salidaTextoNormalTamano = 14;
	private final boolean salidaTextoNormalNegrita = false;

	private final Color salidaTextoErrorColor = Color.RED;
	private final int salidaTextoErrorTamano = 14;
	private final boolean salidaTextoErrorNegrita = false;

	private final Color salidaTextoCabeceraColor = Color.BLUE;
	private final int salidaTextoCabeceraTamano = 16;
	private final boolean salidaTextoCabeceraNegrita = true;
	
	//********************************************************************************
    // 			CONSTRUCTOR
    //********************************************************************************	
	
	/**
	 * Constructor de la terminal
	 * 
	 * @param ventana
	 * 		Ventana a la que estará asociada la terminal
	 */
	public CuadroTerminal(Ventana ventana) {
		
		//	Inicializaciones previas
		
		this.cuadroVentana = ventana;
		this.cuadroEstaVisible = false;	
		
		//	Cuadro diálogo
		
		this.cuadroDialogoInicializar();		
		
		//	Inicializaciones controles / botones	
		
		this.controlesInicializar();
		
		//	Inicialización paneles de salida normal y errores
		
		this.panelesInicializar();
		
		//	Panel general, rellenamos elementos
		
		this.cuadroPanelGeneral();
		
		//	Salidas estilos
		
		this.setSalidaColorCabecera();
		this.setSalidaColorNormal();
		this.setSalidaColorError();		
	}
	
	//********************************************************************************
    // 			MÉTODOS PÚBLICOS
    //********************************************************************************
	
	//***************************************
    // 			CUADRO TERMINAL
    //***************************************
	
	/**
	 * Método que abre o cierra la terminal
	 * 
	 * @return
	 * 		Nuevo valor de la visibilidad de la terminal
	 */
	public boolean terminalAbrirCerrar() {
		if(!this.cuadroAbiertoPrimeraVez) {			
			this.cuadroAbiertoPrimeraVez = true;
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
		this.panelesPanelErrorTexto.setCabecera(s);
		this.panelesPanelNormalTexto.setCabecera(s);
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
	 * a un método termina de escribir en las salidas.
	 * Hacer tras llamada getSalidasTerminalAbrir, no antes.
	 */
	public void setSalidasFin() {
		this.panelesPanelErrorTexto.setEscribirFin();
		this.panelesPanelNormalTexto.setEscribirFin();
	}
	
	
	//********************************************************************************
    // 			MÉTODOS PRIVADOS
    //********************************************************************************
	
	//***************************************
    // 			CUADRO TERMINAL
    //***************************************	
	
	/**
	 * Inicializa el cuadro de diálogo general, la ventana
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
    // 		CONTROLES CREACIÓN Y EDICIÓN
    //***************************************
	
	/**
	 * Inicializa los controles, Menú desplegable + Panel botones
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
		this.controlesTraducciones.put(controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVAR,
				Texto.get(controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVAR.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVAR,
				Texto.get(controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVAR.toString(), Conf.idioma));	
		this.controlesTraducciones.put(controlesNombre.TER_REG_LLAMADAS_ACTIVAR,
				Texto.get(controlesNombre.TER_REG_LLAMADAS_ACTIVAR.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_REG_LLAMADAS_DESACTIVAR,
				Texto.get(controlesNombre.TER_REG_LLAMADAS_DESACTIVAR.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_BUFFER_ACTIVAR,
				Texto.get(controlesNombre.TER_BUFFER_ACTIVAR.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_BUFFER_DESACTIVAR,
				Texto.get(controlesNombre.TER_BUFFER_DESACTIVAR.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_CERRAR,
				Texto.get(controlesNombre.TER_CERRAR.toString(), Conf.idioma));
		this.controlesTraducciones.put(controlesNombre.TER_OPCIONES,
				Texto.get(controlesNombre.TER_OPCIONES.toString(), Conf.idioma));
	}
	
	/**
	 * Rellena el mapa de url de imágenes
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
		this.controlesImagenesUrl.put(controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVAR,
				preUrl+controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVAR.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVAR,
				preUrl+controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVAR.toString().toLowerCase()+".png");	
		this.controlesImagenesUrl.put(controlesNombre.TER_REG_LLAMADAS_ACTIVAR,
				preUrl+controlesNombre.TER_REG_LLAMADAS_ACTIVAR.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_REG_LLAMADAS_DESACTIVAR,
				preUrl+controlesNombre.TER_REG_LLAMADAS_DESACTIVAR.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_BUFFER_ACTIVAR,
				preUrl+controlesNombre.TER_BUFFER_ACTIVAR.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_BUFFER_DESACTIVAR,
				preUrl+controlesNombre.TER_BUFFER_DESACTIVAR.toString().toLowerCase()+".png");
		this.controlesImagenesUrl.put(controlesNombre.TER_CERRAR,
				preUrl+controlesNombre.TER_CERRAR.toString().toLowerCase()+".png");
	}
	
	/**
	 * Crea el menú desplegable
	 */
	private void controlesDesplegableCrear() {
		
		//	Inicializaciones
		
		this.controlesDesplegableJMenuItems = new JMenuItem[this.controlesDesplegableNumeroMenu];
		this.controlesDesplegableMenuBar = new JMenuBar();
		this.controlesDesplegableMenu = new JMenu(this.controlesTraducciones.get(controlesNombre.TER_OPCIONES));
		
		//	Creamos items 
		
		controlesNombre itemNombre = controlesNombre.TER_LIMPIAR;
		
		this.controlesDesplegableJMenuItems[0] = this.controlesDesplegableGetMenuItem(itemNombre);
		
		itemNombre = controlesNombre.TER_COPIAR;
		
		this.controlesDesplegableJMenuItems[1] = this.controlesDesplegableGetMenuItem(itemNombre);
		
		itemNombre = controlesNombre.TER_GUARDAR;
		
		this.controlesDesplegableJMenuItems[2] = this.controlesDesplegableGetMenuItem(itemNombre);
		
		itemNombre = controlesNombre.TER_IMPRIMIR;
		
		this.controlesDesplegableJMenuItems[3] = this.controlesDesplegableGetMenuItem(itemNombre);
		
		itemNombre = controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVAR;
		
		this.controlesDesplegableJMenuItems[4] = this.controlesDesplegableGetMenuItem(itemNombre);
		
		itemNombre = controlesNombre.TER_REG_LLAMADAS_DESACTIVAR;
		
		this.controlesDesplegableJMenuItems[5] = this.controlesDesplegableGetMenuItem(itemNombre);
		
		itemNombre = controlesNombre.TER_BUFFER_ACTIVAR;
		
		this.controlesDesplegableJMenuItems[6] = this.controlesDesplegableGetMenuItem(itemNombre);
		
		itemNombre = controlesNombre.TER_CERRAR;
		
		this.controlesDesplegableJMenuItems[7] = this.controlesDesplegableGetMenuItem(itemNombre);				
		
		//	Añadimos items
		
		this.controlesDesplegableMenu.add(this.controlesDesplegableJMenuItems[0]);
		this.controlesDesplegableMenu.add(this.controlesDesplegableJMenuItems[1]);
		this.controlesDesplegableMenu.add(this.controlesDesplegableJMenuItems[2]);
		this.controlesDesplegableMenu.add(this.controlesDesplegableJMenuItems[3]);
		
		this.controlesDesplegableMenu.add(new JSeparator());
		
		this.controlesDesplegableMenu.add(this.controlesDesplegableJMenuItems[4]);
		this.controlesDesplegableMenu.add(this.controlesDesplegableJMenuItems[5]);
		this.controlesDesplegableMenu.add(this.controlesDesplegableJMenuItems[6]);
		
		this.controlesDesplegableMenu.add(new JSeparator());
		
		this.controlesDesplegableMenu.add(this.controlesDesplegableJMenuItems[7]);
		
		//	Añadimos action listener
		
		for(JMenuItem item:this.controlesDesplegableJMenuItems)
			item.addActionListener(this);
		
		//	Añadimos
		
		this.controlesDesplegableMenuBar.add(this.controlesDesplegableMenu);
	}
	
	/**
	 * Obtiene un JMenuItem
	 * 
	 * @param nombre
	 * 		Determina el texto, imagen y action command
	 * 
	 * @return
	 * 		JMenuItem creado
	 */
	private JMenuItem controlesDesplegableGetMenuItem(controlesNombre nombre) {
		JMenuItem item = new JMenuItem(
				this.controlesTraducciones.get(nombre),
				new ImageIcon(this.controlesImagenesUrl.get(nombre))
		);
		
		item.setActionCommand(nombre.toString());
		
		return item;
	}
	
	/**
	 * Cambia la imagen, texto y action command de un JMenuItem
	 * 
	 * @param item
	 * 		Elemento que queremos cambiar
	 * 
	 * @param nombre
	 * 		Determina el texto, imagen y action command nuevos
	 */
	private void controlesDesplegableGetMenuItemCambiado(JMenuItem item, controlesNombre nombre) {
		item.setIcon(
			new ImageIcon(this.controlesImagenesUrl.get(nombre))
		);
		item.setText(this.controlesTraducciones.get(nombre));
		item.setActionCommand(nombre.toString());
	}
	
	/**
	 * Crea la barra de botones
	 */
	private void controlesBotonesCrear() {

		//	Inicializaciones
		
		this.controlesBotonesJToolbar = new JToolBar[this.controlesBotonesNumeroJToolbar];
		this.controlesBotonesJButtons = new JButton[this.controlesBotonesNumeroJButtons];
		this.cuadroPanelBotones = new JPanel(new GridBagLayout());
		
		//	Creamos botones
		
		controlesNombre itemNombre = controlesNombre.TER_LIMPIAR;
		
		this.controlesBotonesJButtons[0] = this.controlesBotonesGetBoton(itemNombre);
		
		itemNombre = controlesNombre.TER_COPIAR;
		
		this.controlesBotonesJButtons[1] = this.controlesBotonesGetBoton(itemNombre);
		
		itemNombre = controlesNombre.TER_GUARDAR;
		
		this.controlesBotonesJButtons[2] = this.controlesBotonesGetBoton(itemNombre);
		
		itemNombre = controlesNombre.TER_IMPRIMIR;
		
		this.controlesBotonesJButtons[3] = this.controlesBotonesGetBoton(itemNombre);
		
		itemNombre = controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVAR;
		
		this.controlesBotonesJButtons[4] = this.controlesBotonesGetBoton(itemNombre);
		
		itemNombre = controlesNombre.TER_REG_LLAMADAS_DESACTIVAR;
		
		this.controlesBotonesJButtons[5] = this.controlesBotonesGetBoton(itemNombre);
		
		itemNombre = controlesNombre.TER_BUFFER_ACTIVAR;
		
		this.controlesBotonesJButtons[6] = this.controlesBotonesGetBoton(itemNombre);
		
		itemNombre = controlesNombre.TER_CERRAR;
		
		this.controlesBotonesJButtons[7] = this.controlesBotonesGetBoton(itemNombre);
		
		//	Creamos JToolBar
		
		for(int i=0; i<this.controlesBotonesNumeroJToolbar; i++)
			this.controlesBotonesJToolbar[i] = this.controlesBotonesGetJToolBar();
		
		//	Añadimos botones a JToolbar
		
		this.controlesBotonesJToolbar[0].add(this.controlesBotonesJButtons[0]);
		this.controlesBotonesJToolbar[0].add(this.controlesBotonesJButtons[1]);
		this.controlesBotonesJToolbar[0].add(this.controlesBotonesJButtons[2]);
		this.controlesBotonesJToolbar[0].add(this.controlesBotonesJButtons[3]);
		
		this.controlesBotonesJToolbar[1].add(this.controlesBotonesJButtons[4]);
		this.controlesBotonesJToolbar[1].add(this.controlesBotonesJButtons[5]);
		this.controlesBotonesJToolbar[1].add(this.controlesBotonesJButtons[6]);
		
		this.controlesBotonesJToolbar[2].add(this.controlesBotonesJButtons[7]);
		
		//	Añadimos JToolbar al panel
		
		for(int i=0;i<this.controlesBotonesNumeroJToolbar;i++) {
			GridBagConstraints g = this.variosGetConstraints(i, 0, 1, 1, 0, 0, false, GridBagConstraints.CENTER);
			g.insets = new Insets(2, 2, 2, 5);
			this.cuadroPanelBotones.add(this.controlesBotonesJToolbar[i],g);
		}
		
		//	Añadimos action listener y dimensión a los botones
		
		for(JButton item : this.controlesBotonesJButtons) {
			item.setPreferredSize(new Dimension(27, 27));
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
				new ImageIcon(this.controlesImagenesUrl.get(nombre))
		);
		item.setToolTipText(this.controlesTraducciones.get(nombre));
		item.setActionCommand(nombre.toString());
		
		return item;
	}
	
	/**
	 * Cambia la imagen, tooltip y action command de un JButton
	 * 
	 * @param boton
	 * 		Botón que queremos cambiar
	 * 
	 * @param nombre
	 * 		Determina el tooltip, imagen y action command nuevos
	 */
	private void controlesBotonesGetBotonCambiado(JButton boton, controlesNombre nombre) {
		boton.setIcon(
			new ImageIcon(this.controlesImagenesUrl.get(nombre))
		);
		boton.setToolTipText(this.controlesTraducciones.get(nombre));
		boton.setActionCommand(nombre.toString());
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
		jt.setLayout(new FlowLayout(FlowLayout.CENTER));
		return jt;
	}	
	
	/**
	 * Método que cambia la apariencia del botón limpiar pantalla tras llamada
	 * a método
	 * 
	 * @param estadoNuevo
	 * 		True activado nuevo, false caso contrario
	 */
	private void controlesBotonesCambiarLimpiarPantalla(boolean estadoNuevo) {
		
		controlesNombre itemNombre;
		
		if(estadoNuevo) {
			itemNombre = controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVAR;
		}else {
			itemNombre = controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVAR;
		}
		
		this.controlesBotonesGetBotonCambiado(this.controlesBotonesJButtons[4], itemNombre);		
		this.controlesDesplegableGetMenuItemCambiado(this.controlesDesplegableJMenuItems[4], itemNombre);
	}
	
	/**
	 * Método que cambia la apariencia del botón registro llamadas
	 * 
	 * @param estadoNuevo
	 * 		True activado nuevo, false caso contrario
	 */
	private void controlesBotonesCambiarRegistroLlamadas(boolean estadoNuevo) {
		
		controlesNombre itemNombre;
		
		if(estadoNuevo) {
			itemNombre = controlesNombre.TER_REG_LLAMADAS_ACTIVAR;
		}else {
			itemNombre = controlesNombre.TER_REG_LLAMADAS_DESACTIVAR;
		}
		
		this.controlesBotonesGetBotonCambiado(this.controlesBotonesJButtons[5], itemNombre);
		this.controlesDesplegableGetMenuItemCambiado(this.controlesDesplegableJMenuItems[5], itemNombre);
	}
	
	/**
	 * Método que cambia la apariencia del botón del buffer
	 * 
	 * @param estadoNuevo
	 * 		True activado nuevo, false caso contrario
	 */
	private void controlesBotonesCambiarBuffer(boolean estadoNuevo) {
		
		controlesNombre itemNombre;
		
		if(estadoNuevo) {
			itemNombre = controlesNombre.TER_BUFFER_ACTIVAR;
		}else {
			itemNombre = controlesNombre.TER_BUFFER_DESACTIVAR;
		}
		
		this.controlesBotonesGetBotonCambiado(this.controlesBotonesJButtons[6], itemNombre);
		this.controlesDesplegableGetMenuItemCambiado(this.controlesDesplegableJMenuItems[6], itemNombre);
	}
	
	//***************************************
    // 			CONTROLES ACCIONES
    //***************************************
	
	/**
	 * Método que se ejecuta cuando pulsan el botón de limpiar pantalla
	 */
	private void controlesAccionLimpiar() {
		
	}

	/**
	 * Método que se ejecuta cuando pulsan el botón de copiar
	 */
	private void controlesAccionCopiar() {
			
	}

	/**
	 * Método que se ejecuta cuando pulsan el botón de guardar
	 */
	private void controlesAccionGuardar() {
		
	}

	/**
	 * Método que se ejecuta cuando pulsan el botón de imprimir
	 */
	private void controlesAccionImprimir() {
		
	}

	/**
	 * Método que se ejecuta cuando pulsan el botón de limpiar pantalla 
	 * tras llamada a método
	 * 
	 * @param estadoActual
	 * 		True activado actual, false caso contrario
	 */
	private void controlesAccionLimpiarPantalla(boolean estadoActual) {
		boolean estadoNuevo = !estadoActual;
		
		controlesBotonesCambiarLimpiarPantalla(estadoNuevo);
	}

	/**
	 * Método que se ejecuta cuando pulsan el botón de registro de llamadas a método
	 * 
	 * @param estadoActual
	 * 		True activado actual, false caso contrario
	 */
	private void controlesAccionRegistroLlamadas(boolean estadoActual) {
		boolean estadoNuevo = !estadoActual;
		
		controlesBotonesCambiarRegistroLlamadas(estadoNuevo);
	}

	/**
	 * Método que se ejecuta cuando pulsan el botón del buffer
	 * 
	 * @param estadoActual
	 * 		True activado actual, false caso contrario
	 */
	private void controlesAccionBuffer(boolean estadoActual) {
		boolean estadoNuevo = !estadoActual;
		
		controlesBotonesCambiarBuffer(estadoNuevo);
	}

	/**
	 * Método que se ejecuta cuando pulsan el botón de cerrar
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
		
		//	Añadimos a panel general
		
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
		
		this.panelesPanelNormalTexto = new panelTextoClase(10000000, true); //TODO
		
		this.panelesPanelNormalScroll = new JScrollPane(this.panelesPanelNormalTexto.getPanelTexto());
				
		this.panelesPanelNormal = new JPanel(new GridBagLayout());
		
		this.panelesPanelNormalTexto.setJScrollPane(this.panelesPanelNormalScroll);
		
		//	Añadimos
		
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
		
		this.panelesPanelErrorTexto = new panelTextoClase(10000000, false);//TODO
		
		this.panelesPanelErrorScroll = new JScrollPane(this.panelesPanelErrorTexto.getPanelTexto());
				
		this.panelesPanelError = new JPanel(new GridBagLayout());
		
		this.panelesPanelErrorTexto.setJScrollPane(this.panelesPanelErrorScroll);
		
		//	Añadimos
		
		this.panelesPanelError.add(
			this.panelesPanelErrorScroll,
			this.variosGetConstraints(0, 0, 1, 1, 1, 1, true, GridBagConstraints.CENTER)
		);
	}
	
	/**
	 * Crea el panel que contendrá ambos paneles
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
	 * Refresca el separador en función de si se tiene
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
	 * 		Alineación del contenido
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
			
		}else if(origen.equals(controlesNombre.TER_LIMPIAR_PANTALLA_DESACTIVAR.toString()) ||
				origen.equals(controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVAR.toString())) {
			
			controlesAccionLimpiarPantalla(
				origen.equals(controlesNombre.TER_LIMPIAR_PANTALLA_ACTIVAR.toString()
			));
			
		}else if(origen.equals(controlesNombre.TER_REG_LLAMADAS_DESACTIVAR.toString()) ||
				origen.equals(controlesNombre.TER_REG_LLAMADAS_ACTIVAR.toString())) {
			
			controlesAccionRegistroLlamadas(
				origen.equals(controlesNombre.TER_REG_LLAMADAS_ACTIVAR.toString())		
			);
			
		}else if(origen.equals(controlesNombre.TER_BUFFER_ACTIVAR.toString()) ||
				origen.equals(controlesNombre.TER_BUFFER_DESACTIVAR.toString())) {
			
			controlesAccionBuffer(
				origen.equals(controlesNombre.TER_BUFFER_ACTIVAR.toString())		
			);
			
		}else if(origen.equals(controlesNombre.TER_CERRAR.toString())) {
			
			controlesAccionCerrar();
			
		}
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
		private int limiteBuffer;
		private SimpleAttributeSet estiloNormal;
		private SimpleAttributeSet estiloCabecera;
		private String cabecera;
		private JScrollPane panelScroll;
		private final ReentrantLock bloqueo;
		private boolean abrirModificar, abrir, hanEscrito;
		private boolean esSalidaNormal;
		
		//********************************************************************************
		// 			CONSTRUCTOR
		//********************************************************************************
		
		/**
		 * Límite del buffer a aplicar
		 * 
		 * @param limiteBuffer
		 * 		Límite del buffer en caracteres
		 * 
		 * @param esSalidaNormal
		 * 		Indica si esta clase se aplica a la salida
		 * 		normal o la de error
		 * 
		 */
		public panelTextoClase(int limiteBuffer, boolean esSalidaNormal) {
			   
			//	Inicializaciones
				   
			this.panelTexto = new JTextPane();
			this.sb = new StringBuilder();
			this.doc = panelTexto.getStyledDocument();
			this.limiteBuffer = limiteBuffer;
			this.estiloNormal = new SimpleAttributeSet();
			this.estiloCabecera = new SimpleAttributeSet();
			this.cabecera = "";	
			this.bloqueo = new ReentrantLock(true);
			this.abrirModificar = true;
			this.abrir = false;
			this.hanEscrito = false;
			this.esSalidaNormal = esSalidaNormal;
		}
		
		//********************************************************************************
		// 			MÉTODOS PRIVADOS
		//********************************************************************************

		/**
		 * Obtiene el panel texto contenido en esta clase
		 * 
		 * @return
		 * 		Panel texto contenido en esta clase
		 */
		private JTextPane getPanelTexto() {
			return this.panelTexto;
		}
		
		/**
		 * Establece el estilo normal del texto que se mostrará
		 * 
		 * @param texto
		 * 		Color.CONSTANTE
		 * 
		 * @param negrita
		 * 		Texto en negrita o no
		 * 
		 * @param tamanio
		 * 		Tamaño de la letra
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
		 * Establece el estilo de la cabecera del texto que se mostrará
		 * 
		 * @param texto
		 * 		Color.CONSTANTE
		 * 
		 * @param negrita
		 * 		Texto en negrita o no
		 * 
		 * @param tamanio
		 * 		Tamaño de la letra
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
		 * Establece el valor de la cabecera
		 * 
		 * @param cabecera
		 * 		Valor de la cabecera
		 */
		private void setCabecera(String cabecera) {
			this.cabecera = cabecera;
		}		
		
		/**
		 * Escribe el texto pasado como parámetro
		 * incluyendo la cabecera o no
		 * 
		 * @param text
		 * 		Texto a escribir
		 * 
		 * @param cabeceraP
		 * 		Indica si hay que escribir la cabecera o no
		 */
		private void escribir(String text, boolean cabeceraP) {
			SwingUtilities.invokeLater(new Runnable() {
                public void run() {
					try {
						bloqueo.lock();
						
						if(doc.getLength() + text.length() > limiteBuffer) {
						    doc.remove(0, text.length());
						}
						
						if(cabeceraP && !cabecera.equals("")) {
							doc.insertString(doc.getLength(), cabecera, estiloCabecera);
							cabecera = "";
						}
							
						doc.insertString(doc.getLength(), text, estiloNormal);
						
						panelTexto.setDocument(doc);						
						
						bloqueo.unlock();
						
						setScrollAbajo();
						
					}catch(Exception e) {
						
					}
                }
			});
		}
		
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
		
		/**
		 * Establece las variables en cada Write que después permitirán
		 * saber si debemos abrir la terminal automáticamente o no
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
		 * a un método termina de escribir en los paneles.
		 * Hacer tras llamada getEscribirAbrir, no antes.
		 */
		private void setEscribirFin() {
			if(this.esSalidaNormal) {
				this.abrir = false;
				this.abrirModificar = false;
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
		
		//********************************************************************************
		// 			MÉTODOS OUTPUT STREAM
		//********************************************************************************
		
		@Override
		public void flush() {
			
			if(sb.toString().equals(""))
				return;
			
			this.setEscribirWrite();
			
			String text = sb.toString() + "\n";
			escribir(text, false);	
			
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
			escribir(text, true);			
			 
			sb.setLength(0);
			return;
		}
		
		@Override
		public void write(byte[] b, int off, int len){
			
			this.setEscribirWrite();
			
			String text = new String(b, off, len);
			escribir(text, true);
			 
			sb.setLength(0);
			return;
		}
	}
}
