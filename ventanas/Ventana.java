package ventanas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import opciones.GestorOpciones;
import opciones.OpcionBorradoFicheros;
import opciones.OpcionFicherosRecientes;
import opciones.OpcionMVJava;
import opciones.OpcionOpsVisualizacion;
import opciones.Vista;
import paneles.PanelCompilador;
import paneles.PanelVentana;
import utilidades.BuscadorMVJava;
import utilidades.FotografoArbol;
import utilidades.Logger;
import utilidades.ManipulacionElement;
import utilidades.SelecDireccion;
import utilidades.ServiciosString;
import utilidades.Texto;
import conf.Conf;
import cuadros.CuadroAcercade;
import cuadros.CuadroBuscarLlamada;
import cuadros.CuadroElegirES;
import cuadros.CuadroElegirHistorico;
import cuadros.CuadroError;
import cuadros.CuadroGenerarAleatorio;
import cuadros.CuadroGenerarGrafoDependencia;
import cuadros.CuadroIdioma;
import cuadros.CuadroInfoNodo;
import cuadros.CuadroInfoTraza;
import cuadros.CuadroInformacion;
import cuadros.CuadroIntro;
import cuadros.CuadroMetodosProcesadosSelecMetodo;
import cuadros.CuadroNuevaClase;
import cuadros.CuadroOpcionBorradoFicheros;
import cuadros.CuadroOpcionConfVisualizacion;
import cuadros.CuadroOpcionMVJava;
import cuadros.CuadroOpcionMVJavaEncontradas;
import cuadros.CuadroOpcionVistas;
import cuadros.CuadroParamLanzarEjec;
import cuadros.CuadroPreguntaSalirAplicacion;
import cuadros.CuadroPreguntaEdicionNoGuardada;
import cuadros.CuadroPreguntaNuevaVisualizacion;
import cuadros.CuadroPreguntaSobreescribir;
import cuadros.CuadroProgreso;
import cuadros.CuadroTerminal;
import cuadros.CuadroVisibilidad;
import cuadros.CuadroVistasDisponibles;
import cuadros.CuadroZoom;
import datos.AlmacenadorTraza;
import datos.CargadorTraza;
import datos.ClaseAlgoritmo;
import datos.DatosMetodoBasicos;
import datos.DatosTrazaBasicos;
import datos.Ejecucion;
import datos.FamiliaEjecuciones;
import datos.MetodoAlgoritmo;
import datos.Preprocesador;
import datos.Traza;

/**
 * Esta clase constituye la ventana principal de la aplicaci�n.
 * 
 * @author Luis Fern�ndez y Antonio P�rez Carrasco
 * @version 2006-2007
 */
public class Ventana extends JFrame implements ActionListener {

	static final long serialVersionUID = 00;

	// Instancia �nica
	public static Ventana thisventana = null;

	public boolean msWindows = false; // 1=Microsoft Windows 0= otros
	private static final boolean depurar = false;

	private boolean clasePendienteGuardar = false;
	private boolean clasePendienteProcesar = false;

	private boolean interfazIniciada = false; // A true durante varios segundos
	// para
	// implementar antirrebotes en botones

	private PanelVentana panelVentana;

	private Preprocesador p = null;

	public Traza traza = null;
	public Traza trazaCompleta = null;

	private ClaseAlgoritmo claseAlgoritmo = null; // Nos ayuda a crear
	// animaciones
	private boolean claseHabilitada = false; // A true si la clase cargada
	// permite
	// generar animaciones (correcta y con
	// m�todos visualizables)

	private DatosTrazaBasicos dtb; // Nos ayuda a almacenar/cargar animaciones
	// con
	// informaci�n b�sica recopilada de la traza	

	private String fichTraza[];

	private String ficheroGIF[];

	// Men�s de la ventana
	public JMenuBar barramenu;

	private JMenu[] menus = new JMenu[8];
	// 0 = menuFichero
	// 1 = menuVisualizacion
	// 3 = menuConfiguracion
	// 4 = menuAyuda
	// 5 = menuFiltradoYSeleccion
	// 6 = menuArbolDeRecursion
	// 7 = menuTraza

	private String textos[];

	private String codigos[];

	// Herramientas
	private JToolBar[] barrasHerramientas;
	private JButton botones[];

	private GestorOpciones gOpciones = new GestorOpciones();

	public boolean barraHerramientasVisible = true;

	private static GestorVentanaSRec gestorVentana = new GestorVentanaSRec();

	public int[] tamPantalla;
	
	private CuadroParamLanzarEjec cuadroLanzarEjec;

	private boolean usuarioPulsadoIdTraza=false;	//	Indica si el usuario ha pulsado el bot�n idTraza o no
	
	private CuadroTerminal cuadroTerminal;	

	/**
	 * Crea una nueva instancia de la ventana de la aplicaci�n.
	 */
	public Ventana() {
		if (thisventana == null) {
			thisventana = this;
		} else {
			System.exit(0);
		}

		// Detectamos tama�o de la pantalla de salida
		Conf.setTamanoMonitor();
		
		GestorOpciones.crearDirectorioDatosSiNoExiste();
		
		if (!GestorOpciones.existeArchivoOpciones()) {
			this.gOpciones.crearArchivo();
		}
		if (!GestorOpciones.existeArchivoOpcionesPorDefecto()) {
			this.gOpciones.crearArchivoPorDefecto();
		}

		ManipulacionElement.copiarXML(
				GestorOpciones.getNombreArchivoOpDefecto(),
				GestorOpciones.getNombreArchivoOpciones());

		Conf.setValoresOpsVisualizacion(true);
		Conf.setValoresVisualizacion();
		Conf.setConfiguracionIdioma();
		Conf.setConfiguracionVistas();

		new CuadroIntro(this);
		borrarArchivosInservibles();

		Conf.setFicheros();
		if (Conf.fichero_log) {
			this.log_open();
		}

		// Cargamos estos textos ahora para no tener que hacer 30 cargados cada
		// vez que pulsamos un bot�n de men�
		String codigos2[] = {
				"MENU_ARCH_00",
				"MENU_ARCH_03",
				"MENU_ARCH_02",
				"MENU_ARCH_04",
				"MENU_ARCH_05", // 0 a 4
				"MENU_ARCH_06",
				"MENU_ARCH_13",
				"MENU_ARCH_07",
				"MENU_ARCH_12",
				"MENU_ARCH_09", // 5 a 9
				"MENU_ARCH_11",
				"MENU_ARCH_10",
				"MENU_ARCH_08",
				"MENU_INFO_01",
				"MENU_INFO_02", // 10 a 14
				"MENU_INFO_03",
				"MENU_INFO_04",
				"MENU_VISU_02",
				"MENU_VISU_03",
				"MENU_VISU_04", // 15 a 19
				"MENU_VISU_05",
				"MENU_VISU_06",
				"MENU_VISU_07",
				"MENU_VISU_08",
				"MENU_VISU_09", // 20 a 24
				"MENU_VISU_10",
				"MENU_VISU_11",
				"MENU_VISU_12",
				"MENU_VISU_13",
				"MENU_VISU_14", // 25 a 29
				"MENU_VISU_15",
				"MENU_VISU_16",
				"MENU_CONF_01",
				"MENU_CONF_02",
				"MENU_CONF_03", // 30 a 34
				"MENU_AYUD_01",
				"MENU_AYUD_02",
				"MENU_CONF_04",
				"MENU_CONF_05",
				"MENU_VISU_17", // 35 a 39
				"MENU_ARCH_14", "MENU_ARCH_15",
				"MENU_FILT_00",
				"MENU_FILT_01",
				"MENU_FILT_02", // 40 a 44
				"MENU_FILT_03", "MENU_FILT_04", "MENU_FILT_05",
				"MENU_ARBL_00",
				"MENU_ARBL_01", // 45 a 49
				"MENU_ARBL_02", "MENU_TRAZ_00", "MENU_VISU_19", "MENU_TRAZ_02",
				"MENU_TRAZ_03", // 50 a 54
				"MENU_TRAZ_04", "MENU_VISU_18", "MENU_ARBL_03", "MENU_ARBL_04", // 55
				"MENU_ARCH_16"
				// a 59
				,"BARRA_HERR_TTT36_OPEN","BARRA_HERR_TTT36_CLOSE",
				"BARRA_HERR_TTT35"
		};

		this.codigos = codigos2;

		this.textos = Texto.get(this.codigos, Conf.idioma);

		for (int i = 0; i < this.textos.length; i++) {
			if (this.textos[i].contains("_")) {
				this.textos[i] = this.textos[i].substring(0,
						this.textos[i].indexOf("_"));
			}
		}

		// Aplicamos, si podemos, el look and feel de Windows
		try {
			UIManager
			.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

			this.msWindows = true; // debe asignar true
			Conf.anchoBoton = 79;
			Conf.altoBoton = 23;
		} catch (Exception evt1) {
		}
		
		// Colocamos icono para la ventana
		URL icono = getClass().getClassLoader().getResource("imagenes/ico32.gif");
		this.setIconImage(new ImageIcon(icono).getImage());

		// menus
		GestorVentanaSRec.crearMenu(this.menus);

		this.setLayout(new BorderLayout());

		this.setTitle(this.getTituloGenerico());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Creamos la barra de herramientas

		JPanel pHerr = new JPanel();
		pHerr.setLayout(new BorderLayout());

		JPanel p = new JPanel();

		this.barrasHerramientas = GestorVentanaSRec.creaBarrasHeramientas();

		for (int i = 0; i < this.barrasHerramientas.length; i++) {
			p.add(this.barrasHerramientas[i]);
		}

		pHerr.add(p, BorderLayout.WEST);

		this.habilitarOpcionesDYV(false); // Deshabilitamos las opciones que
		// dependen de si la clase cargada
		// es DYV o no

		this.add(pHerr, BorderLayout.NORTH);

		// Panel de la ventana, que contendr� otros paneles para
		// visualizaciones, etc.
		this.panelVentana = new PanelVentana();
		this.add(this.panelVentana, BorderLayout.CENTER);

		// Ubicamos la ventana seg�n la resoluci�n de pantalla configurada
		this.tamPantalla = Conf.getTamanoMonitor();
		int[] tamVentana = this.redimensionarVentana(this.tamPantalla);
		int[] ubicacionVentana = Conf
				.ubicarCentro(tamVentana[0], tamVentana[1]);
		this.setLocation(ubicacionVentana[0], ubicacionVentana[1]);
		this.setSize(tamVentana[0], tamVentana[1]);
		this.setIconImage(new ImageIcon(icono).getImage());
		this.setVisible(true);

		this.addWindowListener(gestorVentana);
		this.addWindowStateListener(gestorVentana);

		Conf.setTamanoVentana(this.getWidth(), this.getHeight());

		// Cargamos opci�n de m�quina virtual
		OpcionMVJava omvj = (OpcionMVJava) this.gOpciones.getOpcion(
				"OpcionMVJava", true);
		if (!omvj.getValida()) {
			String maquinas[] = BuscadorMVJava.buscador(true,true);
			int contadorMaquinas = 0;
			while (!omvj.getValida() && contadorMaquinas < maquinas.length) {
				omvj.setDir(maquinas[contadorMaquinas]);
				this.gOpciones.setOpcion(omvj, 2);
				contadorMaquinas++;
			}

			if (!omvj.getValida()) {
				this.newSeleccionJVM();				
			}
		}

		this.setCuadroTerminal(new CuadroTerminal(this));
	}
	
	/**
	 * Devuelve la �nica instancia de la ventana de la aplicaci�n.
	 * 
	 * @return Ventana de la aplicaci�n.
	 */
	public static Ventana getInstance() {
		return thisventana;
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {

		this.panelVentana.requestFocus();
		Object fuente = e.getSource();

		// Si no acabamos de pulsar un bot�n o bien el bot�n ahora pulsado ahora
		// es de manejo directo de Zoom o de guardado
		if (this.interfazIniciada) {
			return;
		}

		if (fuente != this.botones[2] && fuente != this.botones[21]
				&& fuente != this.botones[22] && fuente != this.botones[23]
						&& fuente != this.botones[24] && fuente != this.botones[25]
								&& fuente != this.botones[26] && !(fuente instanceof JMenuItem)) {
			this.interfazIniciada = true;
		}

		new Thread() {
			@Override
			public synchronized void run() {
				try {
					this.wait(2000);
				} catch (java.lang.InterruptedException ie) {
				}
				Ventana.thisventana.interfazIniciada = false;
			}
		}.start();

		// Si se ha pulsado un item de men�...
		if (fuente instanceof JMenuItem || fuente instanceof JCheckBoxMenuItem) {
			// Botones Men� Archivo //////////////
			String textoFuente = ((JMenuItem) fuente).getText();

			// Archivo > Nueva clase
			if (textoFuente.equals(this.textos[0])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > Nueva clase");
				}
				if (this.panelVentana.haySitio()) {
					new CuadroNuevaClase(this);
				} else {
					new CuadroPreguntaNuevaVisualizacion(this, "crear");
				}
			}

			// Archivo > Cargar y procesar clase...
			else if (textoFuente.equals(this.textos[1])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > Cargar y procesar clase...");
				}
				if (this.clasePendienteGuardar) {
					new CuadroPreguntaEdicionNoGuardada(this, "cargarClase");
				} else {
					this.gestionOpcionCargarClase();
				}
			}

			// Archivo > Guardar clase...
			else if (textoFuente.equals(this.textos[2])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > Guardar clase...");
				}
				if (this.getClase() != null) {
					this.guardarClase();
					this.clasePendienteProcesar = true;
				}
			}

			// Archivo > Procesar clase...
			else if (textoFuente.equals(this.textos[3])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > Procesar clase...");
				}
				if (this.claseAlgoritmo != null) {
					this.guardarClase();
					this.clasePendienteGuardar = false;
					if (this.panelVentana.haySitio()) {
						new Preprocesador(this.claseAlgoritmo.getPath(),
								this.claseAlgoritmo.getNombre() + ".java");
						this.clasePendienteProcesar = false;
					} else {
						new CuadroPreguntaNuevaVisualizacion(this,
								"procesar de nuevo");
					}
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOARCHPROC", Conf.idioma));
				}
			}

			// Archivo > Seleccionar m�todo
			else if (textoFuente.equals(this.textos[40])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > Seleccionar m�todo...");
				}
				if (this.claseAlgoritmo != null) {
					if (this.clasePendienteGuardar
							|| this.clasePendienteProcesar) {
						new CuadroPreguntaEdicionNoGuardada(this, "guardar");
					} else {
						this.iniciarNuevaVisualizacionSelecMetodo();
					}
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOARCHPROC", Conf.idioma));
				}
			}

			// Archivo > Asignar par�metros y lanzar animaci�n
			else if (textoFuente.equals(this.textos[41])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > Asignar par�metros y lanzar animaci�n.");
				}
				if (this.claseAlgoritmo != null) {
					if (this.clasePendienteGuardar
							|| this.clasePendienteProcesar) {
						new CuadroPreguntaEdicionNoGuardada(this, "lanzar");
					}else {
						this.introducirParametros();
					}
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOARCHPROC", Conf.idioma));
				}
			}

			// Archivo > Cargar animaci�n
			else if (textoFuente.equals(this.textos[5])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > Cargar animaci�n...");
				}
				if (this.clasePendienteGuardar) {
					new CuadroPreguntaEdicionNoGuardada(this, "cargarAnimacion");
				} else {
					this.gestionOpcionCargarAnimacion();
				}

			}

			// Archivo > Cargar animaci�n GIF
			else if (textoFuente.equals(this.textos[6])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > Cargar animaci�n GIF...");
				}
				if (this.clasePendienteGuardar) {
					new CuadroPreguntaEdicionNoGuardada(this,
							"cargarAnimacionGIF");
				} else {
					this.gestionOpcionCargarAnimacionGIF();
				}
			}

			// Archivo > Guardar animaci�n
			else if (textoFuente.equals(this.textos[7])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > Guardar animaci�n...");
				}
				if (this.panelOcupado()) {
					this.trazaCompleta
					.actualizarEstadoTrazaCompleta(this.traza);
					this.trazaCompleta.setVisibilidad(this.dtb);

					new AlmacenadorTraza(this, this.traza, this.trazaCompleta,
							this.dtb);
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOVISGUARD", Conf.idioma));
				}
			}
			// Archivo > Guardar traza
			else if (textoFuente.equals(this.textos[8])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > Guardar traza...");
				}
				this.guardarTraza();
			}

			// Archivo > Captura de animaci�n
			else if (textoFuente.equals(this.textos[9])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > " + this.textos[9] + "...");
				}
				new CuadroVistasDisponibles(this, this.panelVentana, 1);
			}

			// Archivo > Capturas de imagen de cada paso
			else if (textoFuente.equals(this.textos[10])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > " + this.textos[10] + "...");
				}
				new CuadroVistasDisponibles(this, this.panelVentana, 2);
			}

			// Archivo > Captura de imagen
			else if (textoFuente.equals(this.textos[11])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > " + this.textos[11] + "...");
				}
				new CuadroVistasDisponibles(this, this.panelVentana, 3);
			}
			
			// Archivo > Capturas de ejecuciones
			else if (textoFuente.equals(this.textos[59])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > " + this.textos[59] + "...");
				}
				new FotografoArbol().hacerCapturasEjecuciones();
			}

			// Archivo > Salir
			else if (textoFuente.equals(this.textos[12])) {
				if (Conf.fichero_log) {
					this.log_write("Archivo > Cerrar");
				}
				if (depurar) {
					System.out
					.println("VentanaVisualizador > Men� Archivo > Salir");
				}
				this.cerrar();
			}

			// Visualizaci�n > Arranque de animaci�n en estado inicial
			else if (textoFuente.equals(this.textos[56])) {
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Arranque de animaci�n en estado inicial");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setArranqueEstadoInicial(!oov.getArranqueEstadoInicial());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Arranque de animaci�n en estado inicial: "
							+ oov.getArranqueEstadoInicial());
				}
				if (oov.getArranqueEstadoInicial()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[1],
							Texto.get("MENU_VISU_18", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_estadoInicial.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[1],
							Texto.get("MENU_VISU_18", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_estadoInicial_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// Visualizaci�n > Identificador de m�todo
			else if (textoFuente.equals(this.textos[52])) {
				this.usuarioPulsadoIdTraza = true;
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Identificador de m�todo");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setIdMetodoTraza(!oov.getIdMetodoTraza());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Identificador de m�todo: "
							+ oov.getIdMetodoTraza());
				}
				if (oov.getIdMetodoTraza()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[1],
							Texto.get("MENU_VISU_19", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_idMetodo.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[1],
							Texto.get("MENU_VISU_19", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_idMetodo_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// Visualizaci�n > Formato de animaci�n...
			else if (textoFuente.equals(this.textos[25])) {
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Formato de animaci�n...");
				}
				new CuadroOpcionConfVisualizacion(this);
			}

			// Visualizaci�n > Configuraci�n de Zoom...
			else if (textoFuente.equals(this.textos[26])) {
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Configuraci�n de Zoom...");
				}
				if (this.panelOcupado()) {
					new CuadroZoom(this, this.panelVentana);
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOVISZOOM", Conf.idioma));
				}
			}

			// Visualizaci�n > Ubicaci�n de paneles
			else if (textoFuente.equals(this.textos[27])) {
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Ubicaci�n de paneles");
				}
				new CuadroOpcionVistas(this);
			}

			// Botones Men� Filtrado y Selecci�n //////////////

			// Filtrado > Datos de entrada y salida...
			else if (textoFuente.equals(this.textos[42])) {
				if (Conf.fichero_log) {
					this.log_write("Filtrado y selecci�n > Datos de entrada y salida...");
				}
				new CuadroElegirES(this);
			}

			// Filtrado > M�todos y par�metros...
			else if (textoFuente.equals(this.textos[43])) {
				if (Conf.fichero_log) {
					this.log_write("Filtrado y selecci�n > M�todos y par�metros...");
				}
				new CuadroVisibilidad(this, this.dtb, this.claseAlgoritmo);
			}

			// Filtrado > Llamadas terminadas...
			else if (textoFuente.equals(this.textos[44])) {
				if (Conf.fichero_log) {
					this.log_write("Filtrado y selecci�n > Llamadas terminadas...");
				}
				new CuadroElegirHistorico(this);
			}

			// Filtrado > Sub�rboles en saltos
			else if (textoFuente.equals(this.textos[45])) {
				if (Conf.fichero_log) {
					this.log_write("Filtrado y selecci�n > Sub�rboles en saltos");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarArbolSalto(!oov.getMostrarArbolSalto());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Filtrado y selecci�n > Sub�rboles en saltos: "
							+ oov.getMostrarArbolSalto());
				}
				if (oov.getMostrarArbolSalto()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[5],
							Texto.get("MENU_FILT_03", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_mostrarsubarbol.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[5],
							Texto.get("MENU_FILT_03", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_mostrarsubarbol_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// Filtrado > B�squeda de llamadas
			else if (textoFuente.equals(this.textos[46])) {
				if (Conf.fichero_log) {
					this.log_write("Filtrado y selecci�n > B�squeda de llamadas...");
				}
				new CuadroBuscarLlamada(this, this.dtb);
			}

			// Filtrado > Restauraci�n de llamadas
			else if (textoFuente.equals(this.textos[47])) {
				if (Conf.fichero_log) {
					this.log_write("Filtrado y selecci�n > Restauraci�n de llamadas");
				}
				thisventana.getTraza().iluminar(0, null, null, false);
				thisventana.refrescarFormato();
			}

			// Botones �rbol de Recursi�n //////////////

			// �rbol de recursi�n > �rboles din�micos
			else if (textoFuente.equals(this.textos[48])) {
				if (Conf.fichero_log) {
					this.log_write("�rbol de recursi�n > �rboles din�micos");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarArbolColapsado(!oov.getMostrarArbolColapsado());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("�rbol de recursi�n > �rboles din�micos: "
							+ oov.getMostrarArbolColapsado());
				}
				if (oov.getMostrarArbolColapsado()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_00", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_arbolcolapsado.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_00", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_arbolcolapsado_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// �rbol de recursi�n > Visualizaci�n din�mica
			else if (textoFuente.equals(this.textos[58])) {
				if (Conf.fichero_log) {
					this.log_write("�rbol de recursi�n > Visualizaci�n din�mica");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setVisualizacionDinamica(!oov.getVisualizacionDinamica());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("�rbol de recursi�n > Visualizaci�n din�mica: "
							+ oov.getVisualizacionDinamica());
				}
				if (oov.getVisualizacionDinamica()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_04", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_visualizacionDinamica.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_04", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_visualizacionDinamica_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// �rbol de recursi�n > Vista global
			else if (textoFuente.equals(this.textos[49])) {
				if (Conf.fichero_log) {
					this.log_write("�rbol de recursi�n > Vista global");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarVisor(!oov.getMostrarVisor());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("�rbol de recursi�n > Vista global: "
							+ oov.getMostrarVisor());
				}
				if (oov.getMostrarVisor()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_01", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_mostrarvisor.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_01", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_mostrarvisor_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// �rbol de recursi�n > Ajustar vista global
			else if (textoFuente.equals(this.textos[57])) {
				if (Conf.fichero_log) {
					this.log_write("�rbol de recursi�n > Ajustar vista global");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setAjustarVistaGlobal(!oov.getAjustarVistaGlobal());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("�rbol de recursi�n > Ajustar vista global: "
							+ oov.getAjustarVistaGlobal());
				}
				if (oov.getAjustarVistaGlobal()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_03", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_ajustarVisor.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_03", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_ajustarVisor_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// �rbol de recursi�n > Estructura de datos en DYV
			else if (textoFuente.equals(this.textos[50])) {
				if (Conf.fichero_log) {
					this.log_write("�rbol de recursi�n > Estructura de datos en DYV");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarEstructuraEnArbol(!oov
						.getMostrarEstructuraEnArbol());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("�rbol de recursi�n > Estructura de datos en DYV: "
							+ oov.getMostrarEstructuraEnArbol());
				}
				if (oov.getMostrarEstructuraEnArbol()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_02", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_estructuraarbol.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_02", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_estructuraarbol_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// Botones Traza //////////////

			// Traza > Sangrado
			else if (textoFuente.equals(this.textos[51])) {
				if (Conf.fichero_log) {
					this.log_write("Traza > Sangrado");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setSangrado(!oov.getSangrado());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Traza > Sangrado: " + oov.getSangrado());
				}
				if (oov.getSangrado()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[7],
							Texto.get("MENU_TRAZ_00", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_sangrado.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[7],
							Texto.get("MENU_TRAZ_00", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_sangrado_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// Traza > Orden cronol�gico
			else if (textoFuente.equals(this.textos[53])) {
				if (Conf.fichero_log) {
					this.log_write("Traza > Orden cronol�gico");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarSalidaLigadaEntrada(!oov
						.getMostrarSalidaLigadaEntrada());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Traza > Orden cronol�gico: "
							+ !oov.getMostrarSalidaLigadaEntrada());
				}
				if (!oov.getMostrarSalidaLigadaEntrada()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[7],
							Texto.get("MENU_TRAZ_02", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_ligarescrono.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[7],
							Texto.get("MENU_TRAZ_02", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_ligarescrono_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// Traza > Solo estructura de datos en DYV
			else if (textoFuente.equals(this.textos[54])) {
				if (Conf.fichero_log) {
					this.log_write("Traza > Solo estructura de datos en DYV");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setSoloEstructuraDYVcrono(!oov.getSoloEstructuraDYVcrono());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Traza > Solo estructura de datos en DYV: "
							+ oov.getSoloEstructuraDYVcrono());
				}
				if (oov.getSoloEstructuraDYVcrono()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[7],
							Texto.get("MENU_TRAZ_03", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_soloestructuraprincipal.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[7],
							Texto.get("MENU_TRAZ_03", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_soloestructuraprincipal_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// Traza > Estructura de datos completa en DYV
			else if (textoFuente.equals(this.textos[55])) {
				if (Conf.fichero_log) {
					this.log_write("Traza > Estructura de datos completa en DYV");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarEstructuraCompletaCrono(!oov
						.getMostrarEstructuraCompletaCrono());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Traza > Estructura de datos completa en DYV: "
							+ oov.getMostrarEstructuraCompletaCrono());
				}
				if (oov.getMostrarEstructuraCompletaCrono()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[7],
							Texto.get("MENU_TRAZ_04", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_estructcompletacrono.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[7],
							Texto.get("MENU_TRAZ_04", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_estructcompletacrono_des.gif"));
				}
				this.actualizarVisualizacion();
			}

			// Botones Men� Informaci�n //////////////

			// Informaci�n > Informaci�n traza...
			else if (textoFuente.equals(this.textos[13])) {
				if (Conf.fichero_log) {
					this.log_write("Informaci�n > Informaci�n traza...");
				}
				new CuadroInfoTraza(this, this.traza, this.trazaCompleta);
			}

			// Informaci�n > Informaci�n nodo actual...
			else if (textoFuente.equals(this.textos[14])) {
				if (Conf.fichero_log) {
					this.log_write("Informaci�n > Informaci�n nodo actual...");
				}
				new CuadroInfoNodo(Ventana.thisventana,
						Ventana.thisventana.traza);
			}

			// Botones Men� Configuraci�n

			// Configuraci�n > Archivo LOG...
			else if (textoFuente.equals(this.textos[38])) {
				if (Conf.fichero_log) {
					this.log_write("Configuraci�n > Archivo LOG...");
				}
				GestorOpciones gOpciones = new GestorOpciones();
				OpcionBorradoFicheros obf = (OpcionBorradoFicheros) gOpciones
						.getOpcion("OpcionBorradoFicheros", false);
				obf.setLOG(!obf.getLOG());
				gOpciones.setOpcion(obf, 2);
				Conf.setFicheros();
			}

			// Configuraci�n > Archivos intermedios...
			else if (textoFuente.equals(this.textos[32])) {
				if (Conf.fichero_log) {
					this.log_write("Configuraci�n > Archivos intermedios...");
				}
				new CuadroOpcionBorradoFicheros(this);
			}

			// Configuraci�n > M�quina Virtual Java...
			else if (textoFuente.equals(this.textos[33])) {
				if (Conf.fichero_log) {
					this.log_write("Configuraci�n > M�quina Virtual Java...");
				}
				this.newSeleccionJVM();
			}

			// Configuraci�n > Idioma...
			else if (textoFuente.equals(this.textos[34])) {
				if (Conf.fichero_log) {
					this.log_write("Configuraci�n > Idioma...");
				}
				new CuadroIdioma(this);
			}

			// Configuraci�n > Mostrar/ocultar barra herramientas
			else if (textoFuente.equals(this.textos[37])) {
				if (Conf.fichero_log) {
					this.log_write("Configuraci�n > Mostrar/ocultar barra herramientas");
				}
				GestorVentanaSRec.setVisibleBarraHerramientas(
						this.barrasHerramientas,
						!this.barraHerramientasVisible, true);
			}

			// Visualizaci�n > Restaurar configuraci�n
			else if (textoFuente.equals(this.textos[28])) {
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Restaurar configuraci�n");
				}
				this.gOpciones.cargarArchivoPorDefecto();
				Conf.setValoresOpsVisualizacion(true);
				Conf.setValoresVisualizacion();
				this.panelVentana.refrescarFormato();
				this.panelVentana.refrescarOpciones();
				GestorVentanaSRec.activaChecks(this.menus);
			}

			// Visualizaci�n > Cargar configuraci�n...
			else if (textoFuente.equals(this.textos[29])) {
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Cargar configuraci�n...");
				}
				OpcionFicherosRecientes ofr = (OpcionFicherosRecientes) this.gOpciones
						.getOpcion("OpcionFicherosRecientes", true);
				String fich[] = SelecDireccion.cuadroAbrirFichero(ofr.getDirConfig(),
						Texto.get("CA_CARCONF", Conf.idioma), null, "xml",
						Texto.get("ARCHIVO_XML", Conf.idioma), 1);

				// *1* Comprobar si fichero existe

				if (fich[1] != null) {
					File f = new File(fich[0] + fich[1]);
					if (f.exists()) {
						if (this.gOpciones.cargarArchivo(fich[0] + fich[1])) {
							ofr.setDirConfig(fich[0]);
							this.gOpciones.setOpcion(ofr, 2);
							Conf.setValoresOpsVisualizacion(false);
							Conf.setValoresVisualizacion();
							this.panelVentana.refrescarFormato();
							this.panelVentana.refrescarOpciones();
						} else {
							new CuadroError(this, Texto.get("ERROR_ARCH",
									Conf.idioma), Texto.get("ERROR_ARCHCOR",
											Conf.idioma));
						}
					} else {
						new CuadroError(this, Texto.get("ERROR_ARCH",
								Conf.idioma), Texto.get("ERROR_ARCHNE",
										Conf.idioma));
					}
				}
				// Si estamos visualizando algo, habr�a que actualizar...
			}

			// Visualizaci�n > Guardar configuraci�n...
			else if (textoFuente.equals(this.textos[30])) {
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Guardar configuraci�n...");
				}
				this.guardarConfiguracionOpciones();
			}

			// Visualizaci�n > Guardar configuraci�n por defecto...
			else if (textoFuente.equals(this.textos[31])) {
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Guardar configuraci�n por defecto...");
				}
				this.gOpciones.crearArchivoPorDefecto();
			}

			// Botones Men� Ayuda /////////

			// Ayuda > Temas de ayuda...
			else if (textoFuente.equals(this.textos[35])) {
				if (Conf.fichero_log) {
					this.log_write("Ayuda > Temas de ayuda...");
				}
				new VisorAyuda();
			}

			// Ayuda > Sobre SRec
			else if (textoFuente.equals(this.textos[36])) {
				if (Conf.fichero_log) {
					this.log_write("Ayuda > Sobre SRec");
				}
				new CuadroAcercade(this);
			}else if (textoFuente.equals(this.textos[60]) || textoFuente.equals(this.textos[61])) { 
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Abrir/Cerrar terminal");
				}
				this.terminalAbrirCerrar();				
			}else if (textoFuente.equals(this.textos[62])) { 
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Generar grafo de dependencia");
				}
				this.generarGrafoDependencia();			
			}

			// JMenuItem no reconocido
			else {
				if (Conf.fichero_log) {
					this.log_write("Elemento de menu no reconocido");
				}
				System.out
				.println("VentanaVisualizador > JMenuItem no reconocido ["
						+ textoFuente + "]");
			}
		}

		// Si se ha pulsado un bot�n de la barra de herramientas...
		else if (fuente instanceof JButton) {

			if (fuente == this.botones[0]) // Archivo > Nueva clase
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Nueva clase");
				}
				new CuadroNuevaClase(this);
			}

			else if (fuente == this.botones[1]) // Archivo > Cargar y procesar
				// clase
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Cargar y procesar clase");
				}
				if (this.clasePendienteGuardar) {
					new CuadroPreguntaEdicionNoGuardada(this, "cargarClase");
				} else {
					this.gestionOpcionCargarClase();
				}
			}

			else if (fuente == this.botones[2]) // Archivo > Guardar clase
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Guardar clase");
				}
				if (this.getClase() != null) {
					this.guardarClase();
					this.clasePendienteProcesar = true;
				}
			}

			else if (fuente == this.botones[3]) // Archivo > Procesar clase
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Procesar clase");
				}
				if (this.getClase() != null && this.clasePendienteGuardar) {
					this.guardarClase();
					this.clasePendienteGuardar = false;
				}
				if (this.panelVentana.haySitio()) {
					new Preprocesador(this.claseAlgoritmo.getPath(),
							this.claseAlgoritmo.getNombre() + ".java");
					this.clasePendienteProcesar = false;
				} else {
					new CuadroPreguntaNuevaVisualizacion(this,
							"procesar de nuevo");
				}
			} else if (fuente == this.botones[28]) // Archivo > Seleccionar
				// M�todo
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n > Seleccionar m�todo...");
				}
				if (this.claseAlgoritmo != null) {
					if (this.clasePendienteGuardar
							|| this.clasePendienteProcesar) {
						new CuadroPreguntaEdicionNoGuardada(this, "guardar");
					} else {
						this.iniciarNuevaVisualizacionSelecMetodo();
					}
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOARCHPROC", Conf.idioma));
				}
			} else if (fuente == this.botones[29]) // Archivo > Asignar
				// Par�metros y lanzar animaci�n
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n > Asignar par�metros y lanzar animaci�n.");
				}
				if (this.claseAlgoritmo != null) {
					if (this.clasePendienteGuardar
							|| this.clasePendienteProcesar) {
						new CuadroPreguntaEdicionNoGuardada(this, "lanzar");
					}else {
						this.introducirParametros();
					}
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOARCHPROC", Conf.idioma));
				}
			} else if (fuente == this.botones[35]) {
				// Generar grafo de dependencia
                if (Conf.fichero_log) {
                	log_write("Bot�n: Generar Grafo de dependencia");
                }
                this.generarGrafoDependencia();
			}else if (fuente == this.botones[5]) // Archivo > Cargar
			// visualizaci�n...
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Cargar visualizaci�n...");
				}
				if (this.clasePendienteGuardar) {
					new CuadroPreguntaEdicionNoGuardada(this, "cargarAnimacion");
				} else {
					this.gestionOpcionCargarAnimacion();
				}
			} else if (fuente == this.botones[6]) // Archivo > Guardar
				// visualizaci�n...
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Guardar visualizaci�n...");
				}
				if (this.panelOcupado()) {
					this.trazaCompleta
					.actualizarEstadoTrazaCompleta(this.traza);
					this.trazaCompleta.setVisibilidad(this.dtb);

					new AlmacenadorTraza(this, this.traza, this.trazaCompleta,
							this.dtb);
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOVISGUARD", Conf.idioma));
				}
			}

			else if (fuente == this.botones[7]) // Archivo > Exportar animaci�n
				// GIF...
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Exportar animaci�n GIF...");
				}
				new CuadroVistasDisponibles(this, this.panelVentana, 1);
			}

			else if (fuente == this.botones[8]) // Archivo > Exportar capturas
				// de estados
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Exportar capturas de estados...");
				}
				new CuadroVistasDisponibles(this, this.panelVentana, 2);
			}

			else if (fuente == this.botones[9]) // Archivo > Exportar captura de
				// estado actual
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Exportar captura de estado actual...");
				}
				new CuadroVistasDisponibles(this, this.panelVentana, 3);
			}

			// NUEVOS GRUPOS
			else if (fuente == this.botones[10]) // Visualizaci�n > Datos de
				// entrada y salida...
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Datos de Entrada y Salida...");
				}
				new CuadroElegirES(this);
			}

			else if (fuente == this.botones[11]) // Visualizaci�n > Visibilidad
				// de m�todos y par�metros
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Visibilidad de m�todos y par�metros...");
				}
				new CuadroVisibilidad(this, this.dtb, this.claseAlgoritmo);
			}

			else if (fuente == this.botones[12]) // Visualizaci�n > Nodos
				// hist�ricos...
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Nodos hist�ricos...");
				}
				new CuadroElegirHistorico(this);
			}

			else if (fuente == this.botones[13]) // Visualizaci�n > Mostrar
				// sub�rboles en saltos
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Mostrar sub�rboles en saltos");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarArbolSalto(!oov.getMostrarArbolSalto());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Mostrar sub�rboles en saltos: "
							+ oov.getMostrarArbolSalto());
				}
				this.actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(this.menus); // La opci�n de men�
				// es un Check, hay
				// que refrescarlo
			}

			else if (fuente == this.botones[14]) // Visualizaci�n > Mostrar
				// visor de navegaci�n
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Visibilidad de m�todos y par�metros...");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarVisor(!oov.getMostrarVisor());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Mostrar vista global: "
							+ oov.getMostrarVisor());
				}
				this.actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(this.menus); // La opci�n de men�
				// es un Check, hay
				// que refrescarlo
			}

			else if (fuente == this.botones[15]) // Visualizaci�n > Mostrar
				// estructura en la vista de
				// �rbol
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Mostrar estructura en la vista de �rbol");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarEstructuraEnArbol(!oov
						.getMostrarEstructuraEnArbol());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Mostrar estructura en vista de �rbol: "
							+ oov.getMostrarEstructuraEnArbol());
				}
				this.actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(this.menus); // La opci�n de men�
				// es un Check, hay
				// que refrescarlo
			} else if (fuente == this.botones[16]) // Visualizaci�n > Mostrar
				// estructura completa en la
				// vista cronol�gica
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Mostrar estructura completa en vista cronol�gica");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarEstructuraCompletaCrono(!oov
						.getMostrarEstructuraCompletaCrono());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Visualizaci�n > Mostrar estructura completa en vista cronol�gica: "
							+ oov.getMostrarEstructuraCompletaCrono());
				}
				this.actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(this.menus); // La opci�n de men�
				// es un Check, hay
				// que refrescarlo
			}

			else if (fuente == this.botones[17]) // Visualizaci�n > Mostrar
				// salida ligada a entrada
				// en vista cronol�gica
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Mostrar salida ligada a entrada en vista cronol�gica");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarSalidaLigadaEntrada(!oov
						.getMostrarSalidaLigadaEntrada());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Mostrar salida ligada a entrada en vista cronol�gica: "
							+ oov.getMostrarSalidaLigadaEntrada());
				}
				this.actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(this.menus); // La opci�n de men�
				// es un Check, hay
				// que refrescarlo
			}

			else if (fuente == this.botones[18]) // Visualizaci�n > Visibilidad
				// de m�todos y par�metros
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Mostrar �rboles colapsados");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarArbolColapsado(!oov.getMostrarArbolColapsado());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Mostrar �rboles colapsados: "
							+ oov.getMostrarArbolColapsado());
				}
				this.actualizarVisualizacion();
				GestorVentanaSRec.activaChecks(this.menus); // La opci�n de men�
				// es un Check, hay
				// que refrescarlo
			}

			// fin nuevos grupos

			else if (fuente == this.botones[19]) // Configuraci�n > Formato de
				// animaci�n
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Formato de animaci�n...");
				}
				new CuadroOpcionConfVisualizacion(this);
			} else if (fuente == this.botones[20]) // Configuraci�n > Zoom
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Zoom...");
				}
				if (this.panelOcupado()) {
					new CuadroZoom(this, this.panelVentana);
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOVISZOOM", Conf.idioma));
				}
			} else if (fuente == this.botones[21]) // Configuraci�n > Zoom+
			{
				if (this.panelOcupado()) {
					int[] vistasVisiblesAhora = this.panelVentana
							.getCodigosVistasVisibles();
					if (Conf.fichero_log) {
						this.log_write("Bot�n: Zoom+ (panel 1, vista "
								+ Vista.codigos[vistasVisiblesAhora[0]] + ")");
					}
					if (vistasVisiblesAhora[0] != 2) {
						CuadroZoom.zoomAjuste(this, this.panelVentana,
								vistasVisiblesAhora[0], CuadroZoom.MAS5);
					}
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOVISZOOM", Conf.idioma));
				}
			} else if (fuente == this.botones[22]) // Configuraci�n > Zoom-
			{
				if (this.panelOcupado()) {
					int[] vistasVisiblesAhora = this.panelVentana
							.getCodigosVistasVisibles();
					if (Conf.fichero_log) {
						this.log_write("Bot�n: Zoom- (panel 1, vista "
								+ Vista.codigos[vistasVisiblesAhora[0]] + ")");
					}
					if (vistasVisiblesAhora[0] != 2) {
						CuadroZoom.zoomAjuste(this, this.panelVentana,
								vistasVisiblesAhora[0], CuadroZoom.MENOS5);
					}
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOVISZOOM", Conf.idioma));
				}
			} else if (fuente == this.botones[23]) // Configuraci�n > Zoom
				// Ajuste
			{
				if (this.panelOcupado()) {
					int[] vistasVisiblesAhora = this.panelVentana
							.getCodigosVistasVisibles();
					if (Conf.fichero_log) {
						this.log_write("Bot�n: ZoomAjuste (panel 1, vista "
								+ Vista.codigos[vistasVisiblesAhora[0]] + ")");
					}
					if (vistasVisiblesAhora[0] != 2) {
						CuadroZoom.zoomAjuste(this, this.panelVentana,
								vistasVisiblesAhora[0], CuadroZoom.AJUSTE);
					}
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOVISZOOM", Conf.idioma));
				}
			} else if (fuente == this.botones[24]) // Configuraci�n > Zoom+
				// (panel 2)
			{
				if (this.panelOcupado()) {
					int[] vistasVisiblesAhora = this.panelVentana
							.getCodigosVistasVisibles();
					if (Conf.fichero_log) {
						this.log_write("Bot�n: Zoom+ (panel 2, vista "
								+ Vista.codigos[vistasVisiblesAhora[1]] + ")");
					}
					if (vistasVisiblesAhora[1] != 2) {
						CuadroZoom.zoomAjuste(this, this.panelVentana,
								vistasVisiblesAhora[1], CuadroZoom.MAS5);
					}
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOVISZOOM", Conf.idioma));
				}
			} else if (fuente == this.botones[25]) // Configuraci�n > Zoom-
				// (panel 2)
			{
				if (this.panelOcupado()) {
					int[] vistasVisiblesAhora = this.panelVentana
							.getCodigosVistasVisibles();
					if (Conf.fichero_log) {
						this.log_write("Bot�n: Zoom- (panel 2, vista "
								+ Vista.codigos[vistasVisiblesAhora[1]] + ")");
					}
					if (vistasVisiblesAhora[1] != 2) {
						CuadroZoom.zoomAjuste(this, this.panelVentana,
								vistasVisiblesAhora[1], CuadroZoom.MENOS5);
					}
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOVISZOOM", Conf.idioma));
				}
			} else if (fuente == this.botones[26]) // Configuraci�n > Zoom
				// Ajuste (panel 2)
			{
				if (this.panelOcupado()) {
					int[] vistasVisiblesAhora = this.panelVentana
							.getCodigosVistasVisibles();
					if (Conf.fichero_log) {
						this.log_write("Bot�n: ZoomAjuste (panel 2, vista "
								+ Vista.codigos[vistasVisiblesAhora[1]] + ")");
					}
					if (vistasVisiblesAhora[1] != 2) {
						CuadroZoom.zoomAjuste(this, this.panelVentana,
								vistasVisiblesAhora[1], CuadroZoom.AJUSTE);
					}
				} else {
					new CuadroError(this, Texto.get("ERROR_VISU", Conf.idioma),
							Texto.get("ERROR_NOVISZOOM", Conf.idioma));
				}
			} else if (fuente == this.botones[27]) // Configuraci�n > Vistas y
				// paneles
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n: Vistas y paneles");
				}
				new CuadroOpcionVistas(this);
			} else if (fuente == this.botones[30]) // Filtrado y selecci�n >
				// B�squeda de llamadas...
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n > B�squeda de llamadas...");
				}
				new CuadroBuscarLlamada(this, this.dtb);
			} else if (fuente == this.botones[31]) // Filtrado y selecci�n >
				// Restauraci�n de llamadas
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n > Restauraci�n de llamadas");
				}
				thisventana.getTraza().iluminar(0, null, null, false);
				thisventana.refrescarFormato();
			} else if (fuente == this.botones[32]) // Traza > Orden cronol�gico
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n > Orden cronol�gico");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarSalidaLigadaEntrada(!oov
						.getMostrarSalidaLigadaEntrada());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Bot�n > Orden cronol�gico: "
							+ !oov.getMostrarSalidaLigadaEntrada());
				}
				if (!oov.getMostrarSalidaLigadaEntrada()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[7],
							Texto.get("MENU_TRAZ_02", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_ligarescrono.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[7],
							Texto.get("MENU_TRAZ_02", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_ligarescrono_des.gif"));
				}
				this.actualizarVisualizacion();
			} else if (fuente == this.botones[33]) // �rbol de recursi�n >
				// Estructura de datos en
				// DYV
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n > Estructura de datos en DYV");
				}
				OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
						.getOpcion("OpcionOpsVisualizacion", false);
				oov.setMostrarEstructuraEnArbol(!oov
						.getMostrarEstructuraEnArbol());
				this.gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					this.log_write("Bot�n > Estructura de datos en DYV: "
							+ oov.getMostrarEstructuraEnArbol());
				}
				if (oov.getMostrarEstructuraEnArbol()) {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_02", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_estructuraarbol.gif"));
				} else {
					GestorVentanaSRec.iconoMenuItem(
							this.menus[6],
							Texto.get("MENU_ARBL_02", Conf.idioma)
							.replace("_SubMenuItem_", "")
							.replace("_CheckBoxMenuItem_", ""),
							getClass().getClassLoader().getResource("imagenes/i_estructuraarbol_des.gif"));
				}
				this.actualizarVisualizacion();
			} else if (fuente == this.botones[34]) // Informaci�n > Informaci�n
				// traza...
			{
				if (Conf.fichero_log) {
					this.log_write("Bot�n > Informaci�n traza...");
				}
				new CuadroInfoTraza(this, this.traza, this.trazaCompleta);
			}

		} else {
			String nombreclase = ((fuente).getClass()).getName();
			if (Conf.fichero_log) {
				this.log_write("Elemento de interacci�n no reconocido ("
						+ nombreclase + ")");
			}
			if (depurar) {
				System.out
				.println("VentanaVisualizador > Clase no reconocida: "
						+ nombreclase);
			}
		}

	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario quiere cargar una
	 * nueva clase.
	 */
	public void gestionOpcionCargarClase() {
		if (this.panelVentana.haySitio()) {
			new Preprocesador();
		} else {
			new CuadroPreguntaNuevaVisualizacion(this, "procesar");
		}
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario quiere cargar una
	 * animaci�n.
	 */
	public void gestionOpcionCargarAnimacion() {
		if (this.panelVentana.haySitio()) {
			new CargadorTraza(this);
		} else {
			new CuadroPreguntaNuevaVisualizacion(this, "cargar");
		}
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario quiere cargar una
	 * animaci�n desde un fichero gif.
	 */
	public void gestionOpcionCargarAnimacionGIF() {
		if (this.panelVentana.haySitio()) {
			this.cargarGIF();
		} else {
			new CuadroPreguntaNuevaVisualizacion(this, "cargarGIF");
		}
	}

	/**
	 * Provoca que las ventanas de visualizaci�n se actualicen con los cambios
	 * introducidos desde la opci�n de men� de colores
	 */
	public void refrescarFormato() {
		this.panelVentana.refrescarFormato();
	}

	/**
	 * Provoca que las ventanas de visualizaci�n se actualicen con los cambios
	 * introducidos desde la opci�n de men� de opciones
	 */
	public void refrescarOpciones() {
		this.panelVentana.refrescarOpciones();
	}

	/**
	 * Actualiza los valores del estado de la Traza en funci�n de su estado
	 * actual y las opciones de configuraci�n
	 */
	public void actualizarTraza() {
		// Datos que se mostrar�n
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
				&& Conf.elementosVisualizar_ant != Conf.elementosVisualizar) {
			Ventana.thisventana.traza.visualizarEntradaYSalida();
		} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA
				&& Conf.elementosVisualizar_ant != Conf.elementosVisualizar) {
			Ventana.thisventana.traza.visualizarSoloEntrada();
		} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA
				&& Conf.elementosVisualizar_ant != Conf.elementosVisualizar) {
			Ventana.thisventana.traza.visualizarSoloSalida();
		}
	}

	/**
	 * Actualiza la visualizaci�n del algoritmo tras modificar opciones de
	 * configuraci�n en el men�.
	 */
	private void actualizarVisualizacion() {
		Conf.setValoresOpsVisualizacion(false);
		Conf.setValoresVisualizacion();
		if (Ventana.thisventana.traza != null) {
			Ventana.thisventana.actualizarTraza();
			Ventana.thisventana.refrescarOpciones();
			Conf.setRedibujarGrafoArbol(false);
		}
	}

	/**
	 * Abre el panel de c�digo.
	 * 
	 * @param editable
	 *            True si se permite edici�n, false en caso contrario.
	 * @param cargarFichero
	 *            Si debe leerse el fichero de nuevo.
	 */
	public void abrirPanelCodigo(boolean editable, boolean cargarFichero) {
		this.panelVentana.abrirPanelCodigo(this.claseAlgoritmo.getPath(),
				editable, cargarFichero);
	}

	/**
	 * Permite establer el texto del panel del compilador.
	 * 
	 * @param texto
	 *            Texto que mostrar� el panel del compilador.
	 */
	public void setTextoCompilador(String texto) {
		this.panelVentana.setTextoCompilador(texto);
	}

	/**
	 * Devuelve el panel de la ventana, que contiene tanto la visualizaci�n de
	 * la ejecuci�n del algoritmo como el del c�digo fuente.
	 * 
	 * @return Panel de la ventana.
	 */
	public PanelVentana getPanelVentana() {
		return this.panelVentana;
	}

	/**
	 * Actualiza la informaci�n sobre el zoom y actualiza la visualizaci�n del
	 * �rbol.
	 * 
	 * @param valor
	 *            Nuevo valor de zoom.
	 */
	public void actualizarZoomArbol(int valor) {
		this.panelVentana.refrescarZoomArbol(valor);
	}

	/**
	 * Actualiza la informaci�n sobre el zoom y actualiza la visualizaci�n de la
	 * pila.
	 * 
	 * @param valor
	 *            Nuevo valor de zoom.
	 */
	public void actualizarZoomPila(int valor) {
		this.panelVentana.refrescarZoomPila(valor);
	}
	
	/**
	 * Actualiza la informaci�n sobre el zoom y actualiza la visualizaci�n de la
	 * pila.
	 * 
	 * @param valor
	 *            Nuevo valor de zoom.
	 */
	public void actualizarZoomGrafoDep(int valor) {
		this.panelVentana.refrescarZoomPanelGrafoDep(valor);
	}

	/**
	 * Actualiza la informaci�n sobre el zoom y actualiza la visualizaci�n de la
	 * vista correspondiente.
	 * 
	 * @param vista
	 *            1 -> pila, 0 -> arbol, 3 -> crono, 4 -> estructura.
	 * @param valor
	 *            Nuevo valor de zoom.
	 */
	public void actualizarZoom(int vista, int valor) {
		this.panelVentana.refrescarZoom(vista, valor);
	}

	/**
	 * Abre un nuevo panel de algoritmo que permite la visualizaci�n de un
	 * algoritmo.
	 * 
	 * @param traza
	 *            Recibe la traza de ejecuci�n del algoritmo, ser� el contenido
	 *            de la visualizaci�n.
	 * @param inicializar
	 *            A true si se desea partir desde el estado inicial de
	 *            visualizaci�n del algoritmo.
	 * @param cuadroProgreso
	 *            Cuadro de progreso para actualizar hasta que la visualizaci�n
	 *            est� completamente cargada, admite null.
	 * @param directorio
	 *            Directorio donde se encuentra la clase procesada.
	 * @param fichero
	 *            Path del fichero con la clase procesada.
	 */
	public void visualizarAlgoritmo(Traza traza, boolean inicializar,
			CuadroProgreso cuadroProgreso, String directorio, String fichero,
			boolean editable) {
		
		try {
			if (inicializar) {
				traza.nadaVisible();
			}
			if (cuadroProgreso != null) {
				cuadroProgreso.setValores(
						Texto.get("CP_ABRIRPAN", Conf.idioma), 90);
			}

			if (traza != null) {
				traza.asignarNumeroMetodo();

				this.trazaCompleta = traza.copiar();
				this.traza = this.dtb.podar(traza);
				this.traza.hacerCoherente();				
				this.trazaCompleta.actualizarEstadoTrazaCompleta(this.traza);				
			}
			this.panelVentana.abrirPanelAlgoritmo(this.traza, directorio,
					fichero, editable);

			if (cuadroProgreso != null) {
				cuadroProgreso.setValores(
						Texto.get("CP_ABRIRPAN", Conf.idioma), 95);
			}

			if (cuadroProgreso != null) {
				cuadroProgreso.setValores(
						Texto.get("CP_ABRIRPAN", Conf.idioma), 98);
			}

			this.habilitarOpcionesAnimacion(true);

		} catch (OutOfMemoryError oome) {
			new CuadroError(this, Texto.get("ERROR_EJEC", Conf.idioma),
					Texto.get("ERROR_NOMEMSUF", Conf.idioma));
		} catch (Exception exp) {
			new CuadroError(this, Texto.get("ERROR_EJEC", Conf.idioma),
					Texto.get("ERROR_NODET", Conf.idioma));
			exp.printStackTrace();
		}
	}
	
	/**
	 * Establece la ejecuci�n correspondiente a la traza pasada por par�metro,
	 * para cuando el algoritmo ya ha sido cargado y se est�n visualizando
	 * distintas ejecuciones.
	 * 
	 * @param ejecucion
	 *            Recibe una ejecuci�n del algoritmo, ser� el contenido
	 *            de la visualizaci�n.
	 * @param inicializar
	 *            A true si se desea partir desde el estado inicial de
	 *            visualizaci�n del algoritmo.
	 */
	public void visualizarEjecucion(Ejecucion ejecucion, boolean inicializar) {
		
		try {
			
			this.setDTB(ejecucion.getDTB());
			this.trazaCompleta = ejecucion.obtenerTrazaCompleta();
			this.traza = ejecucion.obtenerTrazaConPodaParaVisibilidad();
			
			if (inicializar) {
				traza.nadaVisible();
			}

			this.habilitarOpcionesAnimacion(true);
			
			panelVentana.mostrarEjecucionTraza();

		} catch (OutOfMemoryError oome) {
			new CuadroError(this, Texto.get("ERROR_EJEC", Conf.idioma),
					Texto.get("ERROR_NOMEMSUF", Conf.idioma));
		} catch (Exception exp) {
			new CuadroError(this, Texto.get("ERROR_EJEC", Conf.idioma),
					Texto.get("ERROR_NODET", Conf.idioma));
			exp.printStackTrace();
		}
	}

	/**
	 * Determina si est� abierto o cerrado el panel de algoritmo (es decir, si
	 * no est� visualizando nada en su interior).
	 * 
	 * @return True si esta ocupado, false en caso contrario.
	 */
	public boolean panelOcupado() {
		return this.panelVentana.estaOcupado();
	}

	/**
	 * Devuelve la traza actualmente cargada.
	 * 
	 * @return Traza de ejecuci�n actualmente cargada.
	 */
	public Traza getTraza() {
		return this.traza;
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario desea cargar un
	 * fichero gif.
	 */
	public void cargarGIF() {
		OpcionFicherosRecientes ofr = (OpcionFicherosRecientes) this.gOpciones
				.getOpcion("OpcionFicherosRecientes", true);

		String[][] extensiones = new String[1][];
		extensiones[0] = new String[1];
		extensiones[0][0] = "gif";
		String[] definiciones = { Texto.get("ARCHIVO_GIF", Conf.idioma) };

		this.ficheroGIF = SelecDireccion.cuadroAbrirFichero(ofr.getDirExport(),
				Texto.get("CA_CARGVISGIF", Conf.idioma), null, extensiones,
				definiciones, 0);

		if (this.ficheroGIF != null && this.ficheroGIF[1] != null) {
			File f = new File(this.ficheroGIF[0] + this.ficheroGIF[1]);
			if (f.exists()) {
				this.cargarGIF2();
			} else {
				new CuadroError(this, Texto.get("ERROR_ARCH", Conf.idioma),
						Texto.get("ERROR_ARCHNE", Conf.idioma));
			}
		}
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario ha seleccionado un
	 * fichero gif para visualizar.
	 */
	public void cargarGIF2() {
		this.setClase(null);
		this.setTextoCompilador(PanelCompilador.CODIGO_VACIO);
		this.cerrarVentana();
		this.cerrarVistas();
//		this.setClasePendienteGuardar(false);
		this.panelVentana.abrirPanelAlgoritmo(this.ficheroGIF[0]
				+ this.ficheroGIF[1]);
		if (Conf.fichero_log) {
			this.log_write("Animaci�n GIF cargada '" + this.ficheroGIF[0]
					+ this.ficheroGIF[1] + "'...");
		}
	}

	/**
	 * Permite cerrar las vistas de ejecuci�n del algoritmo.
	 */
	public void cerrarVistas() {
		this.panelVentana.cerrarVistas();
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario desea exportar la
	 * traza de ejecuci�n.
	 */
	private void guardarTraza() {
		OpcionFicherosRecientes ofr = (OpcionFicherosRecientes) this.gOpciones
				.getOpcion("OpcionFicherosRecientes", true);

		String[][] extensiones = new String[1][];
		extensiones[0] = new String[1];
		extensiones[0][0] = "html";
		String[] definiciones = { Texto.get("ARCHIVO_HTML", Conf.idioma) };

		this.fichTraza = SelecDireccion.cuadroAbrirFichero(ofr.getDir(),
				Texto.get("CA_GUARTRAZA", Conf.idioma), null, extensiones,
				definiciones, 0);

		if (this.fichTraza != null && this.fichTraza[1] != null) {
			File f = new File(this.fichTraza[0] + this.fichTraza[1]);
			if (!f.exists()) {
				this.guardarTraza2("html");
			} else {
				new CuadroPreguntaSobreescribir(this, "html", this, null);
			}
		}
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario ha seleccionado el
	 * destino y formato de la exportaci�n de la traza.
	 * 
	 * @param Extensi�n
	 *            del fichero que se usar� para el almacenamiento de la traza.
	 *            ("html", o "txt" (Por defecto)).
	 */
	public void guardarTraza2(String extension) {

		String contenidoArchivo = this.contenidoArchivo(extension);

		if (!this.fichTraza[1].toLowerCase().contains(".html")) {
			this.fichTraza[1] = this.fichTraza[1] + ".html";
		}

		// Abrimos fichero
		FileWriter fw = null;
		try {
			fw = new FileWriter(this.fichTraza[0] + this.fichTraza[1]);
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

	/**
	 * Establece la clase actualmente cargada.
	 * 
	 * @param clase
	 *            Objeto ClaseAlgoritmo con la informaci�n de la clase.
	 */
	public void setClase(ClaseAlgoritmo clase) {
		this.claseAlgoritmo = clase;

		this.setClaseHabilitada(clase != null && clase.getNumMetodos() > 0);
		this.setClaseCargada(clase != null); // necesario que vaya detr�s de
		// setClaseHabilitada

		if (clase == null) {
			this.panelVentana.cerrarPanelCodigo();
		}
		
		//	Si solo hay un m�todo deshabilitamos bot�n de selecci�n de m�todo
		//		y llamamos a recoger m�todo unico
		if(this.claseAlgoritmo!=null && this.claseAlgoritmo.getNumMetodos()==1){			
//			this.botones[28].setEnabled(false);
			this.recogerMetodoUnico();
		}
		
		//	Si existe un �ltimo m�todo marcado por el usuario lo establecemos
		//		como activo.
		if(		this.claseAlgoritmo!=null  &&
				ClaseAlgoritmo.getUltimaClaseSeleccionada() != null &&
				ClaseAlgoritmo.getUltimoMetodoSeleccionado()!=null){
			this.recogerUltimoMetodoSeleccionado();
		}
	}

	/**
	 * Establece los datos de traza b�sicos, la traza y la traza completa
	 * actuales.
	 * 
	 * @param dtb
	 *            Datos de traza b�sicos.
	 * @param traza
	 *            Traza.
	 * @param trazaCompleta
	 *            Traza completa en su estado final.
	 */
	public void setDTByTrazas(DatosTrazaBasicos dtb, Traza traza,
			Traza trazaCompleta) {
		this.dtb = dtb;
		this.traza = traza;
		this.trazaCompleta = trazaCompleta;
	}

	/**
	 * Devuelve la clase actualmente cargada.
	 * 
	 * @return Clase actualmente cargada.
	 */
	public ClaseAlgoritmo getClase() {
		return this.claseAlgoritmo;
	}

	/**
	 * Establece los datos de traza b�sicos actuales.
	 * 
	 * @param dtb
	 *            Datos de traza b�sicos.
	 */
	public void setDTB(DatosTrazaBasicos dtb) {
		this.dtb = dtb;
	}

	/**
	 * Devuelve los datos de traza b�sicos actuales.
	 * 
	 * @return Datos de traza b�sicos actuales.
	 */
	public DatosTrazaBasicos getDTB() {
		return this.dtb;
	}

	/**
	 * Actualiza el estado de la traza completa tras haber realizado
	 * modificaciones en la configuraci�n de la visibilidad de m�todos.
	 */
	public void actualizarEstadoTrazaCompleta() {

		// 1� Copiar flags desde traza a trazaCompleta
		this.trazaCompleta.actualizarEstadoTrazaCompleta(this.traza);

		// 2� Aplicar nueva visibilidad (datos introducidos en
		// CuadroVisibilidad, recogidos en dtb)
		this.trazaCompleta.setVisibilidad(this.dtb);

		// 3� Podar y visualizar
		this.traza = this.trazaCompleta.copiar();
		this.traza = this.dtb.podar(this.traza);
		this.traza.hacerCoherente();
		this.trazaCompleta.actualizarEstadoTrazaCompleta(this.traza);

		this.panelVentana.refrescarOpciones();
	}

	/**
	 * Devuelve el texto por defecto para el t�tulo de la ventana.
	 * 
	 * @return Texto por defecto para el t�tulo de la ventana.
	 */
	public String getTituloGenerico() {
		return Texto.get("APLIC", Conf.idioma);
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario desea seleccionar un
	 * m�todo para su ejecuci�n.
	 */
	public void iniciarNuevaVisualizacionSelecMetodo() {
		new CuadroMetodosProcesadosSelecMetodo(this, this.claseAlgoritmo);
	}

	/* Actualiza la visibilidad de los m�todos con la selecci�n pasada por par�metro */
	public void setSeleccionMetodos(ArrayList<MetodoAlgoritmo> metodos) {
		for(int i = 0; i<this.claseAlgoritmo.getNumMetodos(); i++) {
			for(MetodoAlgoritmo m : metodos) {
				if(this.claseAlgoritmo.getMetodo(i).getNombre().equals(m.getNombre())) {
					this.claseAlgoritmo.getMetodo(i).setMarcadoVisualizar(m.getMarcadoVisualizar());
				}
			}
			
		}
	}
	
	/**
	 * Gestiona las acciones necesarias cuando el usuario desea introducir los
	 * par�metros para una nueva visualizaci�n.
	 */
	public void introducirParametros() {
		
		CuadroGenerarAleatorio cuadroGenerarAleatorio = null;
		if (this.cuadroLanzarEjec != null) {
			cuadroGenerarAleatorio = this.cuadroLanzarEjec.getCga();
		}
		
		this.cuadroLanzarEjec = new CuadroParamLanzarEjec(this,
			this.claseAlgoritmo.getMetodoPrincipal(), this.claseAlgoritmo,
			this.p);
		
		if (cuadroGenerarAleatorio != null) {
			cuadroGenerarAleatorio.setCuadroParamLanzarEjec(this.cuadroLanzarEjec);
			this.cuadroLanzarEjec.setCga(cuadroGenerarAleatorio);
		}
	}
	
	/**
	 * Gestiona las acciones necesarias cuando el usuario desea generar
	 * un grafo de dependencia para la ejecuci�n actual.
	 */
    private void generarGrafoDependencia() {
    	if (this.dtb.getNumMetodos() > 1) {
    		new CuadroGenerarGrafoDependencia(this, this.dtb);
    	} else if (this.dtb.getNumMetodos() == 1) {    		
    		//	Habilitamos pesta�a para el grafo de dependencia
            this.abrirPestanaGrafoDependencia(this.dtb.getMetodo(0));
    	}    	
    }
    
    /**
     * Gestiona las acciones necesarias cuando el usuario pulsa
     * el bot�n abrir/cerrar terminal
     */
    public void terminalAbrirCerrar() { 
    	boolean estaVisible = Ventana.this.getCuadroTerminal().terminalAbrirCerrar();
    	JMenuItem item = this.menus[1].getItem(6);
    	String textoClose = Texto.get("BARRA_HERR_TTT36_CLOSE",Conf.idioma);
    	String textoOpen = Texto.get("BARRA_HERR_TTT36_OPEN",Conf.idioma);
    	Icon iconClose = new ImageIcon(
 				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/ter_terminal_desactivar.png"));
    	Icon iconOpen = new ImageIcon(
 				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/ter_terminal_activar.png"));
    	
		if(estaVisible) {
			botones[36].setToolTipText(textoClose);
			botones[36].setIcon(iconClose);
			item.setText(textoClose);
			item.setIcon(iconClose);
		}else {
			botones[36].setToolTipText(textoOpen);
			botones[36].setIcon(iconOpen);
			item.setText(textoOpen);
			item.setIcon(iconOpen);
		}
    }
    
    /**
     * Permite abrir la pesta�a del grafo de dependencia en el panel correspondiente
     * 
     * @param metodo
     * 	M�todo del que queremos generar el grafo de dependencia
     */
    public void abrirPestanaGrafoDependencia(DatosMetodoBasicos metodo){
    	this.panelVentana.abrirPestanaGrafoDependencia(metodo);
    }
    
    /**
     * Permite abrir la pesta�a del grafo de dependencia en el panel correspondiente
     * 
     * @param metodo
     * 	Lista de m�todos de los que queremos generar el grafo de dependencia
     */
    public void abrirPestanaGrafoDependencia(List<DatosMetodoBasicos> metodo){
    	this.panelVentana.abrirPestanaGrafoDependencia(metodo);
    }

	/**
	 * Deshabilita las opciones de menu que deben quedar deshabilitadas tras
	 * cerrar una animaci�n.
	 */
	public void cerrarVentana() {
		this.habilitarOpcionesAnimacion(false);

		this.traza = null;

		if (Ventana.thisventana.getClase() == null) {
			Ventana.thisventana.setTitulo("");
		}
	}

	/**
	 * Devuelve las dimensiones necesarias a aplicar cuando se desea
	 * redimensionar la ventana a su tama�o inicial.
	 * 
	 * @param d
	 *            Dimensiones {anchura, altura}.
	 * 
	 * @return Dimensiones necesarias para redimensionar la ventana {anchura,
	 *         altura}.
	 */
	private int[] redimensionarVentana(int[] d) {
		int[] tam = new int[2];

		tam[0] = (int) (d[0] * (0.85));
		tam[1] = (int) (d[1] * (0.85));

		return tam;
	}

	/**
	 * Ubica y distribuye los paneles segun la disposici�n especificada.
	 * 
	 * @param disposicion
	 *            Conf.PANEL_VERTICAL o Conf.PANEL_HORIZONTAL
	 */
	public void ubicarYDistribuirPaneles(int disposicion) {
		this.panelVentana.ubicarYDistribuirPaneles(disposicion);
	}

	/**
	 * Distribuye los paneles segun la disposici�n especificada.
	 * 
	 * @param disposicion
	 *            Conf.PANEL_VERTICAL o Conf.PANEL_HORIZONTAL
	 */
	public void distribuirPaneles(int disposicion) {
		this.panelVentana.distribuirPaneles(disposicion);
	}

	/**
	 * Devuelve las dimensiones del panel de pila, el panel principal, el panel
	 * crono, y el panel de estructura.
	 * 
	 * @return {anchura_panel_pila, altura_panel_pila, anchura_panel_principal,
	 *         altura_panel_principal, anchura_panel_crono, altura_panel_crono,
	 *         anchura_panel_estructura, altura_panel_estructura,
	 *         anchura_grafo_grafoDep, altura_grafo_grafoDep}
	 */
	public int[] dimensionesPanelesVisualizacion() {
		int dimensiones[] = new int[10];

		int dimE[] = this.panelVentana.dimPanelPila();
		int dimP[] = this.panelVentana.dimPanelPrincipal();
		int dimV[] = this.panelVentana.getTamanoPaneles();

		// Vistas REC
		dimensiones[0] = dimE[0];
		dimensiones[1] = dimE[1];
		dimensiones[2] = dimP[0];
		dimensiones[3] = dimP[1];

		// Vistas DYV
		dimensiones[4] = dimV[4];
		dimensiones[5] = dimV[5];
		dimensiones[6] = dimV[6];
		dimensiones[7] = dimV[7];
		
		dimensiones[8] = dimV[8];
		dimensiones[9] = dimV[9];

		return dimensiones;
	}

	/**
	 * Devuelve las dimensiones de los grafos de pila, �rbol, crono, y
	 * estructura.
	 * 
	 * @return {anchura_grafo_pila, altura_grafo_pila, anchura_grafo_arbol,
	 *         altura_grafo_arbol, anchura_grafo_crono, altura_grafo_crono,
	 *         anchura_grafo_estructura, altura_grafo_estructura, 
	 *         anchura_grafo_grafoDep, altura_grafo_grafoDep}
	 */
	public int[] dimensionesGrafosVisiblesVisualizacion() {
		int dimensiones[] = new int[10];

		int dimP[] = this.panelVentana.dimGrafoPila();
		int dimA[] = this.panelVentana.dimGrafoVisiblePrincipal();
		int dimV[] = this.panelVentana.getTamanoGrafos();

		// Vistas REC
		dimensiones[0] = dimP[0];
		dimensiones[1] = dimP[1];
		dimensiones[2] = dimA[0];
		dimensiones[3] = dimA[1];

		// Vistas DYV
		dimensiones[4] = dimV[4];
		dimensiones[5] = dimV[5];
		dimensiones[6] = dimV[6];
		dimensiones[7] = dimV[7];
		
		dimensiones[8] = dimV[8];
		dimensiones[9] = dimV[9];

		return dimensiones;
	}

	/**
	 * Deshabilita las opciones de la ventana.
	 */
	public void deshabilitarOpcionesVentana() {
		this.setResizable(false);
		GestorVentanaSRec.setVisibleBarraHerramientas(this.barrasHerramientas,
				false, false);
		this.barramenu.setVisible(false);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.panelVentana.deshabilitarControles();
	}

	/**
	 * Habilita las opciones de la ventana.
	 */
	public void habilitarOpcionesVentana() {
		this.setResizable(true);
		if (this.barraHerramientasVisible) {
			GestorVentanaSRec.setVisibleBarraHerramientas(
					this.barrasHerramientas, true, true);
		}
		this.barramenu.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.panelVentana.habilitarControles();
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario desea guardar la clase
	 * tras su modificaci�n.
	 */
	public void guardarClase() {
		this.setClasePendienteGuardar(false);
		this.panelVentana.guardarClase();
		if (Conf.fichero_log) {
			Logger.log_write("Clase guardada en disco tras edici�n");
		}
	}

	/**
	 * Permite establecer el t�tulo de la ventana.
	 * 
	 * @param s
	 *            T�tulo de la ventana.
	 */
	public void setTitulo(String s) {
		if (s == null || s.length() == 0) {
			this.setTitle(this.getTituloGenerico().replace("[", "(")
					.replace("]", ")"));
		} else {
			this.setTitle(this.getTituloGenerico() + " [" + s + "]");
		}
	}

	/**
	 * Devuelve si la clase cargada est� pendiente de guardar.
	 * 
	 * @return True si la clase est� pendiente de guardar, false en caso
	 *         contrario.
	 */
	public boolean getClasePendienteGuardar() {
		return this.clasePendienteGuardar;
	}

	/**
	 * Permite notificar si la clase cargada est� pendiente de guardar o no.
	 * 
	 * @param valor
	 *            True si la clase est� pendiente de guardar, false en caso
	 *            contrario.
	 */
	public void setClasePendienteGuardar(boolean valor) {
		String cadenaMarca = " *";

		String tituloActual = this.getTitle();

		if (tituloActual.contains("[")) {
			tituloActual = tituloActual.substring(
					tituloActual.indexOf("[") + 1, tituloActual.indexOf("]"));

			this.clasePendienteGuardar = valor;
			if (this.clasePendienteGuardar) {
				if (!(tituloActual.contains(cadenaMarca))) {
					this.setTitulo(tituloActual + cadenaMarca);
				}
			} else {
				this.setTitulo(tituloActual.replace(cadenaMarca, ""));
			}
		}
	}

	/**
	 * Permite notificar a la ventana si la clase cargada puede establecerse
	 * como habilitada para realizar acciones sobre ella.
	 * 
	 * @param valor
	 *            True para marcar como habilitada, false para marcar como
	 *            deshabilitada.
	 */
	public void setClaseHabilitada(boolean valor) {
		this.claseHabilitada = valor;

		GestorVentanaSRec.habilitaMenuItem(this.menus[0],
				Texto.get("MENU_ARCH_14", Conf.idioma), valor);

		this.setClaseCargada(valor);
		this.botones[4].setEnabled(valor);
		this.botones[28].setEnabled(valor);
		if (!valor) {
			this.setClaseHabilitadaAnimacion(false);
		}
		
	}
	
	/**
	 * Recoge la selecci�n del �nico m�todo existente y lo establece
	 * para posteriormente permitir su ejecuci�n desde la ventana.
	 */
	private void recogerMetodoUnico() {
		boolean errorProducido = false;
		ArrayList<MetodoAlgoritmo> metodos = new ArrayList<MetodoAlgoritmo>(
				0);
		
		//	Obtenemos el m�todo
		metodos = this.claseAlgoritmo.getMetodosProcesados();
		
		// Actualizar cu�l es el m�todo principal
		this.claseAlgoritmo.borrarMarcadoPrincipal();
		
		metodos.get(0).setMarcadoPrincipal(true);
		metodos.get(0).setMarcadoVisualizar(true);
		this.claseAlgoritmo.addMetodo(metodos.get(0));
		

		if (!errorProducido) {
			// Actualizamos la clase
			MetodoAlgoritmo ma = this.claseAlgoritmo.getMetodoPrincipal();
			if (Conf.fichero_log) {
				String mensaje = "M�todo seleccionado: "
						+ ma.getRepresentacion();
				Logger.log_write(mensaje);				
			}
			//this.setClase(this.claseAlgoritmo);

			// Cerramos la visualizaci�n actual.
			this.cerrarVistas();
			// Escribir signatura del m�todo seleccionado
			this.setValoresPanelControl(ma.getRepresentacion());
			// Habilitamos la opcion para asignar parametros y ejecutar
			this.setClaseHabilitadaAnimacion(true);
            // Deshabilitamos las opciones de la animaci�n por si estuviesen activas.
            this.habilitarOpcionesAnimacion(false);
		}
	}
	
	/**
	 * Permite establecer como activo el �ltimo m�todo seleccionado
	 * @param ultimoMetodoSeleccionado
	 */
	private void recogerUltimoMetodoSeleccionado() {
		ArrayList<MetodoAlgoritmo> metodos = new ArrayList<MetodoAlgoritmo>(0);
		
		//	Obtenemos el m�todo
		metodos = this.claseAlgoritmo.getMetodosProcesados();		
		
		// Actualizar cu�l es el m�todo principal
		this.claseAlgoritmo.borrarMarcadoPrincipal();
		
		for (int i = 0; i < metodos.size(); i++) {
			if(claseAlgoritmo.compararUltimoMetodoSeleccionado(metodos.get(i), this.claseAlgoritmo.getNombre())){
				metodos.get(i).setMarcadoPrincipal(
						true);
				if(metodos.get(i).getMarcadoPrincipal()) {
					metodos.get(i).setMarcadoVisualizar(true);
				}
								
			}else{
				metodos.get(i).setMarcadoPrincipal(
						false);
				if(metodos.get(i).getMarcadoPrincipal()) {
					metodos.get(i).setMarcadoVisualizar(true);
				}
			}
			this.claseAlgoritmo.addMetodo(metodos.get(i));
		}
		
		MetodoAlgoritmo ma = this.claseAlgoritmo.getMetodoPrincipal();
		if (ma!=null) {
			// Actualizamos la clase
			
			if (Conf.fichero_log) {
				String mensaje = "M�todo seleccionado: "
						+ ma.getRepresentacion();
				Logger.log_write(mensaje);				
			}
			//this.setClase(this.claseAlgoritmo);

			// Cerramos la visualizaci�n actual.
			this.cerrarVistas();
			// Escribir signatura del m�todo seleccionado
			this.setValoresPanelControl(ma.getRepresentacion());
			// Habilitamos la opcion para asignar parametros y ejecutar
			this.setClaseHabilitadaAnimacion(true);
            // Deshabilitamos las opciones de la animaci�n por si estuviesen activas.
            this.habilitarOpcionesAnimacion(false);
            
            
		}
	}
	/**
	 * Permite notificar a la ventana si pueden ejecutarse animaciones sobre la
	 * clase cargada o no.
	 * 
	 * @param valor
	 *            True si se permiten animaciones sobre la clase, false en caso
	 *            contrario.
	 */
	public void setClaseHabilitadaAnimacion(boolean valor) {
		GestorVentanaSRec.habilitaMenuItem(this.menus[0],
				Texto.get("MENU_ARCH_15", Conf.idioma), valor);
		this.botones[29].setEnabled(valor);
		this.cuadroLanzarEjec = null;
	}

	/**
	 * Permite notificar a la ventana si la clase ha sido correctamente cargada
	 * o no.
	 * 
	 * @param valor
	 *            True si la clase ha sido correctamente cargada, false en caso
	 *            contrario.
	 */
	public void setClaseCargada(boolean valor) {
		this.botones[2].setEnabled(valor);
		this.botones[3].setEnabled(valor);
		GestorVentanaSRec.habilitaMenuItem(this.menus[0],
				Texto.get("MENU_ARCH_04", Conf.idioma), valor);
	}

	/**
	 * Devuelve si la clase actual est� habilitada o no.
	 * 
	 * @return True en caso de estar habilitada, false en caso contrario.
	 */
	public boolean getClaseHabilitada() {
		return this.claseHabilitada;
	}

	/**
	 * Establece los botones de la barra de herramientas.
	 * 
	 * @param botones
	 *            Lista de botones.
	 */
	public void setBotones(JButton[] botones) {
		this.botones = botones;
	}

	/**
	 * Habilita o deshabilita las opciones correspondientes a las ejecuciones
	 * DYV.
	 * 
	 * @param valor
	 *            True para habilitar, false para deshabilitar.
	 */
	public void habilitarOpcionesDYV(boolean valor) {
		this.botones[33].setEnabled(valor);
		GestorVentanaSRec.habilitaMenuItem(
				this.menus[6],
				Texto.get("MENU_ARBL_02", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""), valor);
		GestorVentanaSRec.habilitaMenuItem(
				this.menus[7],
				Texto.get("MENU_TRAZ_03", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""), valor);
		GestorVentanaSRec.habilitaMenuItem(
				this.menus[7],
				Texto.get("MENU_TRAZ_04", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""), valor);
	}

	public void habilitarOpcionesAnimacion(boolean valor) {
		this.botones[6].setEnabled(valor);

		this.botones[7].setEnabled(valor);
		this.botones[8].setEnabled(valor);
		this.botones[9].setEnabled(valor);

		this.botones[11].setEnabled(valor);

		this.botones[20].setEnabled(valor);
		this.botones[21].setEnabled(valor);
		this.botones[22].setEnabled(valor);
		this.botones[23].setEnabled(valor);
		
		this.botones[24].setEnabled(!FamiliaEjecuciones.getInstance().estaHabilitado() && valor);
		this.botones[25].setEnabled(!FamiliaEjecuciones.getInstance().estaHabilitado() && valor);
		this.botones[26].setEnabled(!FamiliaEjecuciones.getInstance().estaHabilitado() && valor);
		
		this.botones[27].setEnabled(valor);

		this.botones[30].setEnabled(valor);
		this.botones[31].setEnabled(valor);
		this.botones[34].setEnabled(valor);
        this.botones[35].setEnabled(valor);
        this.botones[36].setEnabled(true);
        GestorVentanaSRec.habilitaMenuItem(this.menus[1],
				Texto.get("BARRA_HERR_TTT35", Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(this.menus[2],
				Texto.get("MENU_INFO_01", Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(this.menus[2],
				Texto.get("MENU_INFO_02", Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(this.menus[5],
				Texto.get("MENU_FILT_04", Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(this.menus[5],
				Texto.get("MENU_FILT_05", Conf.idioma), valor);

		GestorVentanaSRec.habilitaMenuItem(
				this.menus[1],
				Texto.get("MENU_VISU_03", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""), valor);
		GestorVentanaSRec.habilitaMenuItem(
				this.menus[5],
				Texto.get("MENU_FILT_01", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""), valor);
		GestorVentanaSRec.habilitaMenuItem(this.menus[1],
				Texto.get("MENU_VISU_11", Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(this.menus[1],
				Texto.get("MENU_VISU_12", Conf.idioma), valor);

		GestorVentanaSRec.habilitaMenuItem(this.menus[0],
				Texto.get("MENU_ARCH_07", Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(this.menus[0],
				Texto.get("MENU_ARCH_09", Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(this.menus[0],
				Texto.get("MENU_ARCH_10", Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(this.menus[0],
				Texto.get("MENU_ARCH_16", Conf.idioma), valor && FamiliaEjecuciones.getInstance().estaHabilitado());
		GestorVentanaSRec.habilitaMenuItem(this.menus[0],
				Texto.get("MENU_ARCH_11", Conf.idioma), valor);
		GestorVentanaSRec.habilitaMenuItem(this.menus[0],
				Texto.get("MENU_ARCH_12", Conf.idioma), valor);
	}

	/**
	 * Obtiene el contenido del fichero de exportaci�n seg�n el tipo de fichero
	 * especificado.
	 * 
	 * @param tipo
	 *            "html", "txt"
	 * 
	 * @return Contenido del fichero de exportaci�n.
	 */
	private String contenidoArchivo(String tipo) {
		String textoEntrada = Texto.get("TR_E", Conf.idioma);
		String textoSalida = Texto.get("TR_S", Conf.idioma);
		String lineasTraza[] = this.traza.getLineasTraza();
		String salida = "";

		if (tipo.toLowerCase().equals("html")) {
			String fuente = Conf.fuenteTraza.getFontName();
			boolean negrita = false;
			boolean cursiva = false;

			negrita = fuente.toLowerCase().contains("bold")
					|| fuente.toLowerCase().contains("negrita");
			cursiva = fuente.toLowerCase().contains("italic")
					|| fuente.toLowerCase().contains("cursiva");

			if (fuente.contains(".")) {
				fuente = fuente.substring(0, fuente.lastIndexOf("."));
			}

			salida += "<html><!-- Generado por SRec / Generated by SRec -->\r\n";
			salida += "<head><title>SRec - " + this.traza.getTitulo()
					+ "</title></head>\r\n";

			salida += "<body bgcolor=\"#"
					+ ServiciosString.cadenaColorHex(Conf.colorPanel.getRed(),
							Conf.colorPanel.getGreen(),
							Conf.colorPanel.getBlue()) + "\">\r\n";

			int limiteOscuro = 170;
			int coloresOscuros = 0;
			if (Conf.colorPanel.getRed() < limiteOscuro) {
				coloresOscuros++;
			}
			if (Conf.colorPanel.getGreen() < limiteOscuro) {
				coloresOscuros++;
			}
			if (Conf.colorPanel.getBlue() < limiteOscuro) {
				coloresOscuros++;
			}

			if (coloresOscuros >= 2) {
				salida += "<font face=\"Verdana\" color=\"#FFFFFF\" size=\"4\">SRec - "
						+ this.traza.getTitulo();
			} else {
				salida += "<font face=\"Verdana\" size=\"4\">SRec - "
						+ this.traza.getTitulo();
			}

			salida += "<br />______________________________<br /><br /></font>\r\n";
			salida += "<font face=\"" + fuente + "\">\r\n";

			salida += (negrita ? "<b>" : "");
			salida += (cursiva ? "<i>" : "");

			for (int i = 0; i < lineasTraza.length; i++) {
				String color = "";
				// Conf.colorTrazaE
				if (lineasTraza[i].contains("entra ")) {
					if (lineasTraza[i].contains("<ilum>")) {
						color = ServiciosString.cadenaColorHex(
								Conf.colorIluminado.getRed(),
								Conf.colorIluminado.getGreen(),
								Conf.colorIluminado.getBlue());
					} else if (Conf.modoColor == 1) {
						if (lineasTraza[i].contains("<hist>")
								&& Conf.historia != 0) {
							color = ServiciosString.cadenaColorHex(
									Conf.colorC1AEntrada.getRed(),
									Conf.colorC1AEntrada.getGreen(),
									Conf.colorC1AEntrada.getBlue());
						} else {
							color = ServiciosString.cadenaColorHex(
									Conf.colorC1Entrada.getRed(),
									Conf.colorC1Entrada.getGreen(),
									Conf.colorC1Entrada.getBlue());
						}
					} else {
						int numColor = Integer.parseInt(lineasTraza[i]
								.substring(lineasTraza[i].indexOf("?") + 1,
										lineasTraza[i].lastIndexOf("?")));
						if (lineasTraza[i].contains("<hist>")
								&& Conf.historia != 0) {
							color = ServiciosString.cadenaColorHex(
									Conf.coloresNodoA[numColor].getRed(),
									Conf.coloresNodoA[numColor].getGreen(),
									Conf.coloresNodoA[numColor].getBlue());
						} else {
							color = ServiciosString.cadenaColorHex(
									Conf.coloresNodo[numColor].getRed(),
									Conf.coloresNodo[numColor].getGreen(),
									Conf.coloresNodo[numColor].getBlue());
						}
					}
				} else {
					if (lineasTraza[i].contains("<ilum>")) {
						color = ServiciosString.cadenaColorHex(
								Conf.colorIluminado.getRed(),
								Conf.colorIluminado.getGreen(),
								Conf.colorIluminado.getBlue());
					} else if (Conf.modoColor == 1) {
						if (lineasTraza[i].contains("<hist>")
								&& Conf.historia != 0) {
							color = ServiciosString.cadenaColorHex(
									Conf.colorC1ASalida.getRed(),
									Conf.colorC1Salida.getGreen(),
									Conf.colorC1ASalida.getBlue());
						} else {
							color = ServiciosString.cadenaColorHex(
									Conf.colorC1Salida.getRed(),
									Conf.colorC1Salida.getGreen(),
									Conf.colorC1Salida.getBlue());
						}
					} else {
						int numColor = Integer.parseInt(lineasTraza[i]
								.substring(lineasTraza[i].indexOf("?") + 1,
										lineasTraza[i].lastIndexOf("?")));
						if (lineasTraza[i].contains("<hist>")
								&& Conf.historia != 0) {
							color = ServiciosString.cadenaColorHex(
									Conf.coloresNodoA[numColor].getRed(),
									Conf.coloresNodoA[numColor].getGreen(),
									Conf.coloresNodoA[numColor].getBlue());
						} else {
							color = ServiciosString.cadenaColorHex(
									Conf.coloresNodo[numColor].getRed(),
									Conf.coloresNodo[numColor].getGreen(),
									Conf.coloresNodo[numColor].getBlue());
						}
					}
				}

				salida += "<font color=\"#" + color + "\">";
				lineasTraza[i] = lineasTraza[i].replace("entra ", textoEntrada);
				lineasTraza[i] = lineasTraza[i].replace("salida ", textoSalida);
				lineasTraza[i] = lineasTraza[i].replace("<hist>", "");
				lineasTraza[i] = lineasTraza[i].replace("<ilum>", "");
				lineasTraza[i] = lineasTraza[i].replace(" ", "&nbsp;");
				lineasTraza[i] = lineasTraza[i].substring(0,
						lineasTraza[i].indexOf("?"));
				salida += lineasTraza[i] + "</font>" + "<br />\r\n";
			}
			salida += (negrita ? "</b>" : "");
			salida += (cursiva ? "</i>" : "");

			salida += "</font></body></html>\r\n";
		} else // .txt
		{
			salida += "-- Generado por SRec / Generated by SRec --\r\n\r\n";
			salida += this.traza.getNombreMetodoEjecucion() + "\r\n\r\n";

			for (int i = 0; i < lineasTraza.length; i++) {
				lineasTraza[i] = lineasTraza[i].replace("entra ", textoEntrada);
				lineasTraza[i] = lineasTraza[i].replace("salida ", textoSalida);
				lineasTraza[i] = lineasTraza[i].substring(0,
						lineasTraza[i].indexOf("?"));
				salida += lineasTraza[i] + "\r\n";
			}

			salida += "\r\n";
		}
		return salida;
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario desea guardar los
	 * par�metros de configuraci�n establecidos.
	 */
	private void guardarConfiguracionOpciones() {
		OpcionFicherosRecientes ofr = (OpcionFicherosRecientes) this.gOpciones
				.getOpcion("OpcionFicherosRecientes", true);
		String fich[] = SelecDireccion.cuadroAbrirFichero(ofr.getDirConfig(),
				Texto.get("CA_GUARCONF", Conf.idioma), null, "xml",
				Texto.get("ARCHIVO_XML", Conf.idioma), 0);

		if (fich != null && fich[1] != null) {
			File f = new File(fich[0] + fich[1]);
			ofr.setDirConfig(fich[0]);
			this.gOpciones.setOpcion(ofr, 2);
			if (!f.exists()) {
				this.gOpciones.crearArchivo(fich[0] + fich[1]);
			} else {
				new CuadroPreguntaSobreescribir(this, fich[0] + fich[1],
						this.gOpciones, null);
			}
		}
	}

	/**
	 * Cuando el usuario desea cerrar la aplicaci�n, este m�todo permite
	 * al usuario confirmar si desea salir o no de la aplicaci�n.
	 */
	private void consultaSalir() {
		new CuadroPreguntaSalirAplicacion(this);
	}

	/**
	 * Finaliza la aplicaci�n.
	 */
	public void cerrar() {
		if (Conf.fichero_log) {
			this.log_close();
		}
		borrarArchivosInservibles();
		System.exit(0);
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario desea guardar la traza
	 * actual en formato xml.
	 */
	public void guardadoTraza() {
		this.trazaCompleta.actualizarEstadoTrazaCompleta(this.traza);
		this.trazaCompleta.setVisibilidad(this.dtb);

		new AlmacenadorTraza(this, this.traza, this.trazaCompleta, this.dtb);
		// AlmacenadorTraza es un thread, el programa no guarda la traza por
		// eso, se sale sin esperar al thread.
	}

	/**
	 * Permite reconfigurar los textos de la ventana cuando el usuario modifica
	 * el idioma.
	 */
	public void reiniciarIdioma() {
		// Necesitamos reiniciar men�s, tooltip de la barra de herramientas,
		// tooltip de barra de animaci�n y t�tulo de ventana

		// Tooltip de la barra de herramientas
		GestorVentanaSRec.setToolTipTextBHH(this.botones);

		// Tooltip de la barra de animaci�n
		this.panelVentana.idioma();

		// Reiniciar men�s
		GestorVentanaSRec.crearMenu(this.menus);

		// T�tulo de la ventana
		String tituloVentana = this.getTitle();
		if (tituloVentana.contains("[")) {
			String archivoTitulo = tituloVentana.substring(
					tituloVentana.indexOf("[") + 1, tituloVentana.indexOf("]"));
			this.setTitulo(archivoTitulo);
		} else {
			this.setTitulo("");
		}

		this.textos = Texto.get(this.codigos, Conf.idioma);
		for (int i = 0; i < this.textos.length; i++) {
			if (this.textos[i].contains("_")) {
				this.textos[i] = this.textos[i].substring(0,
						this.textos[i].indexOf("_"));
			}
		}

		new CuadroInformacion(this, Texto.get("INFO_IDIOMAOK", Conf.idioma),
				Texto.get("INFO_IDIOMA", Conf.idioma), 550, 100);
	}

	/**
	 * Elimina ficheros residuales de sessiones anteriores "SRec_*"
	 */
	private static void borrarArchivosInservibles() {
		File directorioAplicacion = null;
		try {
			directorioAplicacion = new File(".");
		} catch (Exception exc) {
		}
		File archivosParaEliminar[] = directorioAplicacion.listFiles();
		for (int i = 0; i < archivosParaEliminar.length; i++) {
			if (archivosParaEliminar[i].getName().contains("SRec_")) {
				archivosParaEliminar[i].delete();
			}
		}
	}

	/**
	 * Permite establecer el preprocesador actualmente en uso.
	 * 
	 * @param p
	 *            Preprocesador.
	 */
	public void setPreprocesador(Preprocesador p) {
		this.p = p;
	}
	
	/**
	 * Obtiene el preprocesador
	 */
	public Preprocesador getPreprocesador() {
		return this.p;
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario intenta cerrar la
	 * aplicaci�n.
	 */
	public void activarCierre() {

		if (this.getTraza() != null) {
			this.consultaSalir();
		} else {
			Ventana.borrarArchivosInservibles();
			this.log_close();
			System.exit(0);
		}
	}

	/**
	 * Establece el t�tulo del panel de control de la visualizaci�n.
	 * 
	 * @param tituloPanel
	 *            T�tulo del panel.
	 */
	public void setValoresPanelControl(String tituloPanel) {
		this.panelVentana.setValoresPanelControl(tituloPanel);
	}

	/**
	 * Gestiona las acciones necesarias cuando el usuario desea procesar la
	 * clase actual.
	 */
	public void procesarClaseSeleccionarMetodo() {
		new Preprocesador(this.claseAlgoritmo.getPath(),
				this.claseAlgoritmo.getNombre() + ".java", 1);
		this.setClasePendienteProcesar(false);
	}
	
	/**
	 * Gestiona las acciones necesarias cuando el usuario desea procesar la
	 * clase actual.
	 */
	public void procesarClaseLanzarEjecucion() {
		new Preprocesador(this.claseAlgoritmo.getPath(),
				this.claseAlgoritmo.getNombre() + ".java", 2);
		this.setClasePendienteProcesar(false);
	}

	/**
	 * Establece la clase actual como pendiente de procesar.
	 * 
	 * @param valor
	 *            True para establecer la clase actual como pendiente de
	 *            procesar, false en caso contrario.
	 */
	public void setClasePendienteProcesar(boolean valor) {
		this.clasePendienteProcesar = valor;
	}

	/**
	 * Registra en el fichero de log un inicio de sesi�n en SRec.
	 */
	private void log_open() {
		this.log_write("----------------- INICIO DE SESION " +
				ServiciosString.direccionIP());
	}

	/**
	 * Permite registrar eventos en el fichero de log.
	 */
	private void log_write(String s) {
		if (Conf.fichero_log) {
			Logger.log_write(s);
		}
	}

	/**
	 * Registra en el fichero de log un final de sesi�n en SRec.
	 */
	private void log_close() {
		this.log_write("----------------- FIN DE SESION " +
				ServiciosString.direccionIP() + "\r\n\r\n\r\n");
	}
	
	/**
	 * Permite generar los dos cuadros de di�logo para seleccionar m�quina virtual
	 * (Para no replicar c�digo, se utiliza dos veces)
	 */
	private void newSeleccionJVM(){
		
		// Buscamos cualquier versi�n de JDK y dejamos al usuario
		// elegir el que quiera
		String[] maquinas = BuscadorMVJava.buscador(true,false);
		
		// A true para indicar que cargue un texto m�s explicativo para
		// la primera ejecuci�n.
		
		if(maquinas.length>0){					
			new CuadroOpcionMVJavaEncontradas(maquinas,this);
		}else{				
			new CuadroOpcionMVJava(this, true);
		}
	}
	
	/**
	 * Permite obtener los datos b�sicos de la traza
	 * @return	Datos b�sicos de la traza
	 */
	public DatosTrazaBasicos getDtb() {
		return dtb;
	}
	
	/**
	 * Permite simular que han pulsado sobre 
	 * 		"Visualizaci�n --> Identificaci�n m�todo"
	 * 
	 * @param pulsado Indica si es activar idTraza o no
	 */
	public void simularIdTrazaPulsado(boolean activar){
		
		if (Conf.fichero_log) {
			this.log_write("Visualizaci�n > Identificador de m�todo");
		}
		OpcionOpsVisualizacion oov = (OpcionOpsVisualizacion) this.gOpciones
				.getOpcion("OpcionOpsVisualizacion", false);
		
		//	Solo hacemos algo si el valor es distinto al anterior
		//	y el usuario no ha pulsado el bot�n 
		if(this.usuarioPulsadoIdTraza || oov.getIdMetodoTraza() == activar)
			return;
		oov.setIdMetodoTraza(!oov.getIdMetodoTraza());
		this.gOpciones.setOpcion(oov, 1);
		if (Conf.fichero_log) {
			this.log_write("Visualizaci�n > Identificador de m�todo: "
					+ oov.getIdMetodoTraza());
		}
		if (oov.getIdMetodoTraza()) {					//	idMetodo activado
			GestorVentanaSRec.iconoMenuItem(
					this.menus[1],
					Texto.get("MENU_VISU_19", Conf.idioma)
					.replace("_SubMenuItem_", "")
					.replace("_CheckBoxMenuItem_", ""),
					getClass().getClassLoader().getResource("imagenes/i_idMetodo.gif"));
			GestorVentanaSRec.setPulsado(
					this.menus[1],
					Texto.get("MENU_VISU_19", Conf.idioma)
					.replace("_SubMenuItem_", "")
					.replace("_CheckBoxMenuItem_", ""),
					getClass().getClassLoader().getResource("imagenes/i_idMetodo.gif"),
					true);
		} else {
			GestorVentanaSRec.iconoMenuItem(			//	idMetodo NO activado
					this.menus[1],
					Texto.get("MENU_VISU_19", Conf.idioma)
					.replace("_SubMenuItem_", "")
					.replace("_CheckBoxMenuItem_", ""),
					getClass().getClassLoader().getResource("imagenes/i_idMetodo_des.gif"));
			GestorVentanaSRec.setPulsado(				
					this.menus[1],
					Texto.get("MENU_VISU_19", Conf.idioma)
					.replace("_SubMenuItem_", "")
					.replace("_CheckBoxMenuItem_", ""),
					getClass().getClassLoader().getResource("imagenes/i_idMetodo_des.gif"),
					true);
		}
		this.actualizarVisualizacion();
	}
	
	/**
	 * Obtiene el cuadro terminal de la aplicaci�n
	 * 
	 * @return 
	 * 		Cuadro terminal de la aplicaci�n
	 */
	public CuadroTerminal getCuadroTerminal() {
		return cuadroTerminal;
	}

	/**
	 * Establece el cuadro terminal de la aplicaci�n
	 * 
	 * @param 
	 * 		CuadroTerminal de la aplicaci�n
	 */
	public void setCuadroTerminal(CuadroTerminal cuadroTerminal) {
		this.cuadroTerminal = cuadroTerminal;
	}
}
