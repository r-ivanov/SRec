package paneles;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import opciones.GestorOpciones;
import opciones.OpcionVistas;
import opciones.Vista;
import utilidades.Arrays;
import utilidades.NombresYPrefijos;
import utilidades.ServiciosString;
import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import datos.DatosMetodoBasicos;
import datos.FamiliaEjecuciones;
import datos.MetodoAlgoritmo;
import eventos.NavegacionListener;

/**
 * Contiene los paneles que conforman la representación del algoritmo en
 * ejecución. Está contenido en un panel contenedor del panel de la ventana
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class PanelAlgoritmo extends JPanel implements ChangeListener, ComponentListener {
	static final long serialVersionUID = 14;
	
	private static final int GROSOR_SPLIT_DIVIDER = 8;
	
	private static JSplitPane separadorCodigo, separadorVistas,
			separadorCentral;

	private GestorOpciones gOpciones = new GestorOpciones();

	// Paneles que están contenidos en este panel

	private static PanelCodigo pCodigo;
	private static PanelCompilador pCompilador;
	private static PanelGrafo pGrafo;
	private static PanelCodigoBotones pCodigoBotones;
	
	
	private static PanelTraza pTraza;
	private static PanelPila pPila;
	private static PanelArbol pArbol;

	private static PanelCrono pCrono;
	private static PanelEstructura pEstructura;
	
	private static PanelValoresGlobales pValGlobal;
	private static PanelValoresRama pValRama;

	private static PanelControl pControl;

	private String tituloPanel;
	private String idTraza;

	private String[] nombresMetodos;
	public static NombresYPrefijos nyp = null;

	private static JScrollPane jspCompilador, jspCodigo, jspTraza, jspPila,
			jspCrono, jspEstructura, jspGrafo, jspValGlobales, jspValRama;
	private JPanel jspArbol;

	private JPanel contenedorCompilador, contenedorCodigo, contenedorTraza,
	contenedorPila, contenedorArbol, contenedorControl, contenedorGrafo,
	contenedorCrono, contenedorEstructura, contenedorValGlobales, contenedorValRama;

	private JTabbedPane panel1, panel2;

	private boolean mostrarNombreMetodos = false;

	private boolean ocupado = false;

	private boolean abriendoVistas = false;

	private JPanel panelGral;

	private String[] nombresVistas = new String[Vista.codigos.length];

	private NavegacionListener arbolNavegacionListener;
	
	private static Boolean grafoActivado = false;	
	
	private static int panel1Pestana,panel2Pestana;

	/**
	 * Crea un nuevo PanelAlgoritmo
	 * 
	 * @throws Exception
	 */
	public PanelAlgoritmo() throws Exception {
		// Creamos el panel de la izquierda (contiene el panel del código y el
		// de la traza)
		JPanel izqda = new JPanel();
		izqda.setLayout(new BorderLayout());

		pCodigo = new PanelCodigo(null);
		pTraza = new PanelTraza();
		pCompilador = new PanelCompilador();
		pCodigoBotones = new PanelCodigoBotones(pCodigo);				

		// jspCodigo = new JScrollPane(pCodigo);
		jspTraza = new JScrollPane(pTraza);
		// jspCodigo.setPreferredSize(new Dimension(250,250));

		this.contenedorCodigo = new JPanel();
		this.contenedorCodigo.setLayout(new BorderLayout());
		this.contenedorCodigo.add(pCodigo.getPanel(), BorderLayout.CENTER);
		this.contenedorCodigo.add(pCodigoBotones, BorderLayout.SOUTH);
		
		this.contenedorCompilador = new JPanel();
		this.contenedorCompilador.setLayout(new BorderLayout());
		this.contenedorCompilador.add(pCompilador.getPanel(),
				BorderLayout.CENTER);
		
		this.contenedorTraza = new JPanel();
		this.contenedorTraza.setLayout(new BorderLayout());
		this.contenedorTraza.add(jspTraza);

		jspCompilador = new JScrollPane(this.contenedorCompilador);

		separadorCodigo = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				this.contenedorCodigo, jspCompilador);
		separadorCodigo.setResizeWeight(0.85);
		separadorCodigo.setDividerLocation(0.8);

		izqda.add(separadorCodigo, BorderLayout.CENTER);

		// Creamos el panel de la derecha (contiene el panel del árbol y el de
		// la pila)
		JPanel dcha = new JPanel();
		dcha.setLayout(new BorderLayout());

		try {
			pValGlobal = new PanelValoresGlobales();
			pValRama = new PanelValoresRama();
			
			pPila = new PanelPila(null);
			pArbol = new PanelArbol(null, pValRama);
			pCrono = new PanelCrono(null);
			pGrafo = new PanelGrafo((DatosMetodoBasicos) null,null,null);		
		} catch (OutOfMemoryError oome) {
			pArbol = null;
			throw oome;
		} catch (Exception e) {
			pArbol = null;
			e.printStackTrace();
			throw e;
		}		
		
		jspPila = new JScrollPane(pPila);
		this.contenedorPila = new JPanel();
		this.contenedorPila.setLayout(new BorderLayout());
		this.contenedorPila.add(jspPila, BorderLayout.CENTER);
		
		this.jspArbol = new JPanel();
		this.jspArbol.setLayout(new BorderLayout());
		this.jspArbol.add(pArbol, BorderLayout.CENTER);
		this.contenedorArbol = new JPanel();
		this.contenedorArbol.setLayout(new BorderLayout());
		this.contenedorArbol.add(this.jspArbol, BorderLayout.CENTER);
		
		jspGrafo = new JScrollPane(pGrafo);
		this.contenedorGrafo = new JPanel();
		this.contenedorGrafo.setLayout(new BorderLayout());
		this.contenedorGrafo.add(jspGrafo,BorderLayout.CENTER);

		jspCrono = new JScrollPane(pCrono);
		this.contenedorCrono = new JPanel();
		this.contenedorCrono.setLayout(new BorderLayout());
		this.contenedorCrono.add(jspCrono, BorderLayout.CENTER);

		jspEstructura = new JScrollPane(pEstructura);
		this.contenedorEstructura = new JPanel();
		this.contenedorEstructura.setLayout(new BorderLayout());
		this.contenedorEstructura.add(jspEstructura, BorderLayout.CENTER);
		
		jspValGlobales = new JScrollPane(pValGlobal);
		this.contenedorValGlobales = new JPanel(new BorderLayout());
		this.contenedorValGlobales.add(jspValGlobales, BorderLayout.CENTER);	
		
		jspValRama = new JScrollPane(pValRama);
		this.contenedorValRama = new JPanel(new BorderLayout());
		this.contenedorValRama.add(jspValRama, BorderLayout.CENTER);
		
		this.quitarBordesJSP();

		this.panel1 = new JTabbedPane();
		this.panel2 = new JTabbedPane();

		this.panel1.addChangeListener(this);
		this.panel2.addChangeListener(this);

		this.nombresVistas = this.recopilarNombresVistas();

		int tipoDisposicion;
		if (Conf.disposicionPaneles == Conf.PANEL_VERTICAL) {
			tipoDisposicion = JSplitPane.HORIZONTAL_SPLIT;
		} else {
			tipoDisposicion = JSplitPane.VERTICAL_SPLIT;
		}

		separadorVistas = new JSplitPane(tipoDisposicion, this.panel1,
				this.panel2);
		separadorVistas.setDividerSize(GROSOR_SPLIT_DIVIDER);
		separadorVistas.setOneTouchExpandable(true);
		separadorVistas.setResizeWeight(0.5);
		separadorVistas.setDividerLocation(0.5);

		dcha.add(separadorVistas, BorderLayout.CENTER);

		// Creamos panel superior (nombre de método, botones, ...)
		pControl = new PanelControl("", this);
		this.contenedorControl = new JPanel();
		this.contenedorControl.setLayout(new BorderLayout());
		this.contenedorControl.add(pControl, BorderLayout.CENTER);

		// Creamos panel de contención (contendrá las cuatro vistas)
		JPanel pContencion = new JPanel();

		separadorCentral = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, izqda,
				dcha);
		separadorCentral.setDividerSize(8);
		separadorCentral.setOneTouchExpandable(true);
		separadorCentral.setResizeWeight(0.3);
		separadorCentral.setDividerLocation(0.3);

		pContencion.setLayout(new BorderLayout());
		pContencion.add(separadorCentral, BorderLayout.CENTER);

		this.arbolNavegacionListener = pArbol.getNavegacionListener();
		this.jspArbol.addComponentListener(this.arbolNavegacionListener);

		// Creamos panel general
		this.panelGral = new JPanel();
		this.panelGral.setLayout(new BorderLayout());
		this.panelGral.add(this.contenedorControl, BorderLayout.NORTH);
		this.panelGral.add(pContencion, BorderLayout.CENTER);
		this.setLayout(new BorderLayout());
		this.add(this.panelGral, BorderLayout.CENTER);
		
		//	Añadimos mouse event a los JTabbedPane para recordar pestañas
		this.anadirMouseEventPaneles();
	}

	/**
	 * Distribuye los paneles segun la disposición especificada.
	 * 
	 * @param disposicion
	 *            Conf.PANEL_VERTICAL o Conf.PANEL_HORIZONTAL
	 */
	public void distribuirPaneles(int disposicion) {
		separadorVistas
		.setOrientation(disposicion == Conf.PANEL_VERTICAL ? JSplitPane.HORIZONTAL_SPLIT
				: JSplitPane.VERTICAL_SPLIT);
	}

	/**
	 * Establece el idioma establecido por configuración.
	 */
	public void idioma() {
		pControl.idioma();

		if (this.estaOcupado()) {
			try {
				pTraza = new PanelTraza();
			} catch (Exception e) {
			}
			jspTraza.removeAll();
			jspTraza = new JScrollPane(pTraza);
			pTraza.visualizar();
			this.contenedorTraza.removeAll();
			this.contenedorTraza.add(jspTraza);
			this.contenedorTraza.updateUI();
			this.updateUI();
		}

		String[] nombres = this.getNombreVistasDisponibles();
		String[][] idiomas = Texto.idiomasDisponibles();

		for (int i = 0; i < nombres.length; i++) // Para cada nombre contenido
			// en "nombres" buscamos su
			// traducción
		{
			for (int j = 0; j < idiomas.length; j++) // Buscamos en todos los
				// idiomas, no sabemos
				// en qué idioma está el
				// nombre que tenemos
			{
				for (int k = 0; k < Vista.codigos.length; k++) // Buscamos entre
					// los nombres
					// que tenemos
					// guardados en
					// XML
					// basándonos en
					// códigos de
					// Vista.codigos
				{
					if (nombres[i].equals(Texto.get(Vista.codigos[k],
							idiomas[j][1]))) {
						// Ahora buscamos en panel1 y panel2 para saber dónde
						// tenemos que hacer reemplazo
						for (int x = 0; x < this.panel1.getTabCount(); x++) {
							if (this.panel1.getTitleAt(x).equals(nombres[i])) {
								this.panel1.setTitleAt(x, Texto.get(
										Vista.codigos[k], Conf.idioma));
							}
						}

						for (int x = 0; x < this.panel2.getTabCount(); x++) {
							if (this.panel2.getTitleAt(x).equals(nombres[i])) {
								this.panel2.setTitleAt(x, Texto.get(
										Vista.codigos[k], Conf.idioma));
							}
						}
					}
				}
			}
		}
		this.quitarBordesJSP();
		this.nombresVistas = this.recopilarNombresVistas();
	}

	/**
	 * Devuelve el nombre de cada una de las vistas según la configuración de
	 * idioma.
	 * 
	 * @return array con los nombres de las vistas, en el mismo orden que los
	 *         código especificados en Vista.codigos
	 */
	private String[] recopilarNombresVistas() {
		return Texto.get(Vista.codigos, Conf.idioma);
	}

	/**
	 * Permite abrir el Panel de código.
	 * 
	 * @param nombreArchivo
	 *            Archivo que contiene el código fuente de la clase.
	 * @param editable
	 *            Si el fichero puede editarse.
	 * @param cargarFichero
	 *            Si debe leerse el fichero de nuevo.
	 */
	public void abrirPanelCodigo(String nombreArchivo, boolean editable,
			boolean cargarFichero) {
		this.contenedorCodigo.removeAll();

		// pCodigo = new PanelCodigo(nombreArchivo,nombreMetodo);
		pCodigo.abrir(nombreArchivo, editable, cargarFichero, false);
		jspCodigo = new JScrollPane(pCodigo.getPanel());
		pCodigoBotones.activarTodosBotones();
		this.quitarBordesJSP();

		this.contenedorCodigo.add(jspCodigo, BorderLayout.CENTER);
		this.contenedorCodigo.add(pCodigoBotones, BorderLayout.SOUTH);
		this.contenedorCodigo.updateUI();
	}

	/**
	 * Permite cerrar el Panel de código.
	 */
	public void cerrarPanelCodigo() {
		this.contenedorCodigo.removeAll();

		pCodigo = new PanelCodigo(null);
		pCodigoBotones.desactivarTodosBotones();
		
		this.quitarBordesJSP();
		this.contenedorCodigo.updateUI();

		nyp = null;
	}

	/**
	 * Permite abrir las vistas necesarias para mostrar la ejecución.
	 */
	public void abrirVistas() {
		this.abriendoVistas = true;
		this.ubicarVistas();

		nyp = null;
		this.mostrarNombreMetodos = Ventana.thisventana.traza.getNumMetodos() != 1;

		if (this.mostrarNombreMetodos) {
			nyp = new NombresYPrefijos();
			this.nombresMetodos = Ventana.thisventana.trazaCompleta
					.getNombresMetodos();
			String prefijos[] = ServiciosString
					.obtenerPrefijos(this.nombresMetodos);
			for (int i = 0; i < this.nombresMetodos.length; i++) {
				nyp.add(this.nombresMetodos[i], prefijos[i]);
			}
		}

		try {
			if(Arrays.contiene(MetodoAlgoritmo.TECNICA_AABB,
					Ventana.thisventana.getTraza().getTecnicas())) {
				pValGlobal = new PanelValoresGlobales();
				pValRama = new PanelValoresRama();
				pArbol = new PanelArbol(nyp, pValRama);
			}else {
				pArbol = new PanelArbol(nyp, null);
			}
			pPila = new PanelPila(nyp);
			if(grafoActivado)
				pGrafo.visualizar2(nyp);
			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
					Ventana.thisventana.getTraza().getTecnicas())) {
				Ventana.thisventana.habilitarOpcionesDYV(true);
				pCrono = new PanelCrono(nyp);
				pEstructura = new PanelEstructura(nyp);
			} else {
				pTraza = new PanelTraza();
			}

			pControl.setValores(Ventana.thisventana.traza.getTitulo(), this);

			this.jspArbol.removeComponentListener(this.arbolNavegacionListener);
			this.arbolNavegacionListener = pArbol.getNavegacionListener();
			this.jspArbol.addComponentListener(this.arbolNavegacionListener);

			new Thread() {
				@Override
				public synchronized void run() {
					try {
						this.wait(50);
					} catch (java.lang.InterruptedException ie) {
					}					
					SwingUtilities.invokeLater(new Runnable() {					
						@Override
						public void run() {
							pArbol.getNavegacionListener().ejecucion(1);
							pArbol.updateUI();
						}
					});
				}
			}.start();
			if (!Conf.arranqueEstadoInicial || FamiliaEjecuciones.getInstance().estaHabilitado()) {
				pControl.hacerFinal();
			} else {
				// Actualizamos la vista para que se posicione bien sobre el
				// primer nodo creado				
				this.actualizar();				
			}

			this.ocupado = true;
		} catch (Exception e) {
			try {
				e.printStackTrace();
				System.out
				.println("\n-Ha saltado una excepcion(PanelAlgoritmo)-\n");
				pValGlobal = new PanelValoresGlobales();
				pValRama = new PanelValoresRama();
				
				pArbol = new PanelArbol(null, pValRama);
				pPila = new PanelPila(null);
				pGrafo = new PanelGrafo((DatosMetodoBasicos) null,null,null);
				pTraza = new PanelTraza();
				
				pCrono = new PanelCrono(null);
				pEstructura = new PanelEstructura(null);
				pControl = new PanelControl("", this);
				

			} catch (Exception e2) {
			}
		}

		this.contenedorArbol.removeAll();
		this.contenedorPila.removeAll();
		this.contenedorTraza.removeAll();
		this.contenedorCrono.removeAll();
		this.contenedorEstructura.removeAll();
		this.contenedorGrafo.removeAll();	
		this.contenedorValGlobales.removeAll();
		this.contenedorValRama.removeAll();
		

		this.jspArbol.removeAll();
		jspPila.removeAll();
		jspTraza.removeAll();
		jspCrono.removeAll();
		
		jspEstructura.removeAll();
		jspGrafo.removeAll();
		
		jspValGlobales.removeAll();
		jspValRama.removeAll();
		
		this.jspArbol.add(pArbol);
		jspPila = new JScrollPane(pPila);
		jspTraza = new JScrollPane(pTraza);
		jspCrono = new JScrollPane(pCrono);
		
		jspEstructura = new JScrollPane(pEstructura);
		jspGrafo = new JScrollPane(pGrafo);
		
		jspValGlobales = new JScrollPane(pValGlobal);
		jspValRama = new JScrollPane(pValRama);
		
		this.contenedorArbol.add(this.jspArbol);
		this.contenedorPila.add(jspPila);
		this.contenedorTraza.add(jspTraza);
		this.contenedorCrono.add(jspCrono);
		
		this.contenedorEstructura.add(jspEstructura);
		this.contenedorGrafo.add(jspGrafo);
		
		this.contenedorValGlobales.add(jspValGlobales);
		this.contenedorValRama.add(jspValRama);

		this.quitarBordesJSP();

		this.contenedorArbol.updateUI();
		this.contenedorPila.updateUI();
		this.contenedorTraza.updateUI();
		this.contenedorCrono.updateUI();
		
		this.contenedorEstructura.updateUI();
		this.contenedorControl.updateUI();
		this.contenedorGrafo.updateUI();	
		this.contenedorValGlobales.updateUI();
		this.contenedorValRama.updateUI();
		this.abriendoVistas = false;
		
		//	Recordamos pestaña seleccionada
		this.recordarPestanaPaneles();
		
	}

	/**
	 * Permite abrir las vistas necesarias para mostrar una ejecución dado un
	 * fichero GIF.
	 * 
	 * @param ficheroGIF
	 *            Fichero que contiene la animación de un algoritmo.
	 */
	public void abrirVistas(String ficheroGIF) {
		this.abriendoVistas = true;

		this.ubicarVistas();

		try {
			pArbol = new PanelArbol(ficheroGIF, new ImageIcon(ficheroGIF));
			pPila = new PanelPila(null);
			pTraza = new PanelTraza();
			pGrafo = new PanelGrafo((DatosMetodoBasicos) null,null,null);
			this.ocupado = true;
			pControl.setValores(ficheroGIF.substring(
					ficheroGIF.lastIndexOf("\\") + 1,
					ficheroGIF.lastIndexOf(".")), this);
			Ventana.thisventana.setTitulo(ficheroGIF.substring(
					ficheroGIF.lastIndexOf("\\") + 1, ficheroGIF.length()));

		} catch (Exception e) {
			try {
				e.printStackTrace();
				System.out.println("\n-Ha saltado una excepcion-\n");
				// Cambiar para AABB
				pArbol = new PanelArbol(null, new PanelValoresRama());
				pPila = new PanelPila(null);
				pGrafo = new PanelGrafo((DatosMetodoBasicos) null,null,null);
				pTraza = new PanelTraza();
				pControl = new PanelControl("", this);
				this.ocupado = false;
			} catch (Exception e2) {
			}
		}

		this.contenedorArbol.removeAll();
		this.contenedorPila.removeAll();
		this.contenedorTraza.removeAll();
		this.contenedorGrafo.removeAll();
		
		this.jspArbol.removeAll();
		jspPila.removeAll();
		jspTraza.removeAll();
		jspGrafo.removeAll();

		this.jspArbol.add(pArbol);
		jspPila = new JScrollPane(pPila);
		jspTraza = new JScrollPane(pTraza);
		jspGrafo = new JScrollPane(pGrafo);
		
		this.contenedorArbol.add(this.jspArbol);
		this.contenedorPila.add(jspPila);
		this.contenedorTraza.add(jspTraza);
		this.contenedorGrafo.add(jspGrafo);

		this.quitarBordesJSP();

		this.contenedorArbol.updateUI();
		this.contenedorPila.updateUI();
		this.contenedorTraza.updateUI();
		this.contenedorControl.updateUI();
		this.contenedorGrafo.updateUI();

		this.abriendoVistas = false;
	}

	/**
	 * Permite cerrar las vistas de ejecución del algoritmo.
	 */
	public void cerrarVistas() {
		try {
			this.ocupado = false;
			Ventana.thisventana.traza = null;
			Ventana.thisventana.trazaCompleta = null;
			
			pValGlobal = new PanelValoresGlobales();
			pValRama = new PanelValoresRama();
			pArbol = new PanelArbol(null, pValRama);
			pPila = new PanelPila(null);
			pGrafo = new PanelGrafo((DatosMetodoBasicos) null,null,null);
			pTraza = new PanelTraza();
			pCrono = new PanelCrono(null);
			pControl.setValores("", this);


		} catch (Exception e) {

		}

		this.contenedorArbol.removeAll();
		this.contenedorPila.removeAll();
		this.contenedorTraza.removeAll();
		this.contenedorCrono.removeAll();
		
		this.contenedorEstructura.removeAll();
		
		this.contenedorValGlobales.removeAll();
		this.contenedorValRama.removeAll();
		
		if(this.contenedorGrafo != null)
			this.contenedorGrafo.removeAll();
		
		this.jspArbol.removeAll();
		jspPila.removeAll();
		jspTraza.removeAll();
		jspCrono.removeAll();
		
		jspEstructura.removeAll();
		jspGrafo.removeAll();
		
		jspValGlobales.removeAll();
		jspValRama.removeAll();
		
		this.jspArbol.add(pArbol);
		jspPila = new JScrollPane(pPila);
		jspTraza = new JScrollPane(pTraza);
		jspCrono = new JScrollPane(pCrono);
		
		jspEstructura = new JScrollPane(pEstructura);
		jspGrafo = new JScrollPane(pGrafo);
		
		jspValGlobales = new JScrollPane(pValGlobal);
		jspValRama = new JScrollPane(pValRama);
		
		this.contenedorArbol.add(this.jspArbol);
		this.contenedorPila.add(jspPila);
		this.contenedorTraza.add(jspTraza);
		this.contenedorCrono.add(jspCrono);
	
		this.contenedorEstructura.add(jspEstructura);
		this.contenedorGrafo.add(jspArbol);
		this.contenedorValGlobales.add(jspValGlobales);
		this.contenedorValRama.add(jspValRama);

		this.quitarBordesJSP();

		this.contenedorArbol.updateUI();
		this.contenedorPila.updateUI();
		this.contenedorTraza.updateUI();
		this.contenedorCrono.updateUI();
		this.contenedorEstructura.updateUI();
		this.contenedorGrafo.updateUI();
		this.contenedorValGlobales.updateUI();
		this.contenedorValRama.updateUI();

		this.contenedorControl.updateUI();

		this.panel1.removeAll();
		this.panel2.removeAll();
		
		separadorVistas.setRightComponent(this.panel2);
		int anterior = separadorVistas.getDividerLocation();		
		separadorVistas.setResizeWeight(0.5);
		separadorVistas.setOneTouchExpandable(true);
		separadorVistas.setEnabled(true);
		FamiliaEjecuciones.getInstance().deshabilitar();

		nyp = null;
		
		grafoActivado = false;
		separadorVistas.setDividerLocation(anterior);
	}
	
	/**
	 * Permite actualizar la UI de la familia de árboles, tras cualquier
	 * cambio en la resolución, tamaño o configuración.
	 * 
	 * @param forzar Si debe repintarse aunque según los cambios introducidos
	 * no sea estrictamente necesario actualizar la UI.
	 */
	private void actualizarFamiliaEjecuciones(boolean forzar) {
		int tamanio;
		int dividerLocation;
		int[] tamanioMonitor = Conf.getTamanoMonitor();
		if (Conf.disposicionPaneles == Conf.PANEL_HORIZONTAL) {
			tamanio = tamanioMonitor[1] / 5;
			dividerLocation = Math.max(0, separadorVistas.getHeight() - tamanio - GROSOR_SPLIT_DIVIDER);	
		} else {
			tamanio = tamanioMonitor[0] / 6;
			dividerLocation = Math.max(0, separadorVistas.getWidth() - tamanio - GROSOR_SPLIT_DIVIDER);
		}
		separadorVistas.setDividerLocation(dividerLocation);
		FamiliaEjecuciones.getInstance().actualizar(tamanio, Conf.disposicionPaneles, forzar);
	}
	
	/**
	 * Permite ubicar las distintas vistas según los valores de configuración de
	 * ubicación y disposición de paneles.
	 */
	public void ubicarVistas() {
		boolean familiaEjecucionesHabilitado = FamiliaEjecuciones.getInstance().estaHabilitado();
		int anterior = separadorVistas.getDividerLocation();		
		if (familiaEjecucionesHabilitado) {
			JScrollPane panelEjecuciones = FamiliaEjecuciones.getInstance().obtenerPanelEjecuciones();						
			separadorVistas.setRightComponent(panelEjecuciones);			
			this.actualizarFamiliaEjecuciones(false);			
			separadorVistas.setResizeWeight(1.0);
			separadorVistas.setOneTouchExpandable(true);
			separadorVistas.setEnabled(false);
			panelEjecuciones.removeComponentListener(this);
			panelEjecuciones.addComponentListener(this);
			
		} else {			
			separadorVistas.setRightComponent(this.panel2);	
			separadorVistas.setResizeWeight(0.5);
			separadorVistas.setOneTouchExpandable(true);
			separadorVistas.setEnabled(true);			
		}
		
		// Vista de árbol
		if (Conf.getVista(Vista.codigos[0]).getPanel() == 1 || familiaEjecucionesHabilitado) {
			this.panel1.add(Texto.get(Vista.codigos[0], Conf.idioma),
					this.contenedorArbol);
		} else {
			this.panel2.add(Texto.get(Vista.codigos[0], Conf.idioma),
					this.contenedorArbol);
		}	
		
		if (Ventana.thisventana.getTraza() != null) // Será null si estamos
			// cargando GIF
		{
			
			// Vista de pila
			if (Conf.getVista(Vista.codigos[1]).getPanel() == 1 || familiaEjecucionesHabilitado) {
				this.panel1.add(Texto.get(Vista.codigos[1], Conf.idioma),
						this.contenedorPila);
			} else {
				this.panel2.add(Texto.get(Vista.codigos[1], Conf.idioma),
						this.contenedorPila);
			}			
			
			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
					Ventana.thisventana.getTraza().getTecnicas())) {
				
				// Vista cronológica
				if (Conf.getVista(Vista.codigos[2]).getPanel() == 1 || familiaEjecucionesHabilitado) {
					this.panel1.add(Texto.get(Vista.codigos[2], Conf.idioma),
							this.contenedorCrono);
				} else {
					this.panel2.add(Texto.get(Vista.codigos[2], Conf.idioma),
							this.contenedorCrono);
				}
				
				// Vista de estructura
				if (Conf.getVista(Vista.codigos[3]).getPanel() == 1 || familiaEjecucionesHabilitado) {
					this.panel1.add(Texto.get(Vista.codigos[3], Conf.idioma),
							this.contenedorEstructura);
				} else {
					this.panel2.add(Texto.get(Vista.codigos[3], Conf.idioma),
							this.contenedorEstructura);
				}
				
			} else if(Arrays.contiene(MetodoAlgoritmo.TECNICA_AABB,
					Ventana.thisventana.getTraza().getTecnicas())){
			
				// Vista Global de Valores
				if (Conf.getVista(Vista.codigos[5]).getPanel() == 1 || familiaEjecucionesHabilitado) {					
					this.panel1.add(Texto.get(Vista.codigos[5], Conf.idioma),
							this.contenedorValGlobales);
				} else {					
					this.panel2.add(Texto.get(Vista.codigos[5], Conf.idioma),
							this.contenedorValGlobales);					
				}
				
				// Vista de Rama de Valores
				if (Conf.getVista(Vista.codigos[6]).getPanel() == 1 || familiaEjecucionesHabilitado) {					
					this.panel1.add(Texto.get(Vista.codigos[6], Conf.idioma),
							this.contenedorValRama);
				} else {					
					this.panel2.add(Texto.get(Vista.codigos[6], Conf.idioma),
							this.contenedorValRama);					
				}
				
				// Vista de traza				
				if (Conf.getVista(Vista.codigos[2]).getPanel() == 1 || familiaEjecucionesHabilitado) {					
					this.panel1.add(Texto.get(Vista.codigos[2], Conf.idioma),
							this.contenedorTraza);
				} else {					
					this.panel2.add(Texto.get(Vista.codigos[2], Conf.idioma),
							this.contenedorTraza);					
				}
	
			} else {
				
				// Vista de traza				
				if (Conf.getVista(Vista.codigos[2]).getPanel() == 1 || familiaEjecucionesHabilitado) {					
					this.panel1.add(Texto.get(Vista.codigos[2], Conf.idioma),
							this.contenedorTraza);
				} else {					
					this.panel2.add(Texto.get(Vista.codigos[2], Conf.idioma),
							this.contenedorTraza);					
				}
			}			
			
			// Si las vistas de recursividad fueron colocadas todas en un panel
			if (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
					Ventana.thisventana.getTraza().getTecnicas()) && !familiaEjecucionesHabilitado) {
				if (Conf.getVista(Vista.codigos[0]).getPanel() == 1
						&& Conf.getVista(Vista.codigos[1]).getPanel() == 1
						&& Conf.getVista(Vista.codigos[2]).getPanel() == 1) {
					this.panel2.add(Texto.get(Vista.codigos[0], Conf.idioma),
							this.contenedorArbol);

					OpcionVistas ov = (OpcionVistas) this.gOpciones.getOpcion(
							"OpcionVistas", false);
					ov.getVista(Vista.codigos[0]).setPanel(2);
					this.gOpciones.setOpcion(ov, 2);
					Conf.setConfiguracionVistas();

				} else if (Conf.getVista(Vista.codigos[0]).getPanel() == 2
						&& Conf.getVista(Vista.codigos[1]).getPanel() == 2
						&& Conf.getVista(Vista.codigos[2]).getPanel() == 2) {
					this.panel1.add(Texto.get(Vista.codigos[0], Conf.idioma),
							this.contenedorArbol);

					OpcionVistas ov = (OpcionVistas) this.gOpciones.getOpcion(
							"OpcionVistas", false);
					ov.getVista(Vista.codigos[0]).setPanel(1);
					this.gOpciones.setOpcion(ov, 2);
					Conf.setConfiguracionVistas();
				}
			}
			
			// Vista de grafo
			//	Solo se redibuja la vista si han pulsado previamente 
			//		el botón de generar grafo de dependencia
			if(grafoActivado && panel1 != null && panel2 != null){
				if (Conf.getVista(Vista.codigos[4]).getPanel() == 1 || familiaEjecucionesHabilitado) {
					this.panel1.add(Texto.get(Vista.codigos[4], Conf.idioma),
							this.contenedorGrafo);
				} else {
					this.panel2.add(Texto.get(Vista.codigos[4], Conf.idioma),
							this.contenedorGrafo);
				}
			}
		}
		separadorVistas.setDividerLocation(anterior);		
	}

	/**
	 * Determina si está abierto o cerrado el panel (es decir, si no está
	 * visualizando nada en su interior)
	 * 
	 * @return true si el panel está abierto, false en caso contrario.
	 */
	public boolean estaOcupado() {
		return this.ocupado;
	}

	/**
	 * Permite deshabilitar los controles de la animación.
	 */
	protected void deshabilitarControles() {
		pControl.deshabilitarControles();
	}

	/**
	 * Permite habilitar los controles de la animación.
	 */
	protected void habilitarControles() {
		pControl.habilitarControles();
	}

	/**
	 * Actualiza los valores del panel de control.
	 * 
	 * @param tituloPanel
	 *            Título del panel.
	 */
	protected void setValoresPanelControl(String tituloPanel) {
		pControl.setValores(tituloPanel, this);
	}

	/**
	 * Actualiza la visualización de los distintos paneles que componen el
	 * panel.
	 */
	public void actualizar() {
		int anterior = separadorVistas.getDividerLocation();
		if (Ventana.thisventana.getTraza() != null && this.panel1 != null && this.panel2 != null) {

			boolean[] hemosActualizado = new boolean[Vista.codigos.length];
			for (int i = 0; i < hemosActualizado.length; i++) {
				hemosActualizado[i] = false;
			}
			
			//	Pila
			if (this.panel1.indexOfTab(this.nombresVistas[1]) == this.panel1
					.getSelectedIndex()
					|| this.panel2.indexOfTab(this.nombresVistas[1]) == this.panel2
					.getSelectedIndex()) {
				new Thread() {
					@Override
					public synchronized void run() {

						try {
							this.wait(240);
						} catch (java.lang.InterruptedException ie) {
						}
						SwingUtilities.invokeLater(new Runnable() {							
							@Override
							public void run() {
								pPila.visualizar();
							}
						});
					}
				}.start();
				hemosActualizado[1] = true;
			}			
			
			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
					Ventana.thisventana.getTraza().getTecnicas())) {
				
				//	Crono
				if (this.panel1.indexOfTab(this.nombresVistas[2]) == this.panel1
						.getSelectedIndex()
						|| this.panel2.indexOfTab(this.nombresVistas[2]) == this.panel2
						.getSelectedIndex()) {
					new Thread() {
						@Override
						public synchronized void run() {
							try {
								this.wait(20);
							} catch (java.lang.InterruptedException ie) {
							}
							SwingUtilities.invokeLater(new Runnable() {							
								@Override
								public void run() {
									pCrono.visualizar();
								}
							});
						}
					}.start();
					hemosActualizado[2] = true;
				}
			}
			//	Árbol

			if (this.panel1.indexOfTab(this.nombresVistas[0]) == this.panel1
					.getSelectedIndex()
					|| this.panel2.indexOfTab(this.nombresVistas[0]) == this.panel2
					.getSelectedIndex()) {
				new Thread() {
					@Override
					public synchronized void run() {						
						try {
							this.wait(100);
						} catch (java.lang.InterruptedException ie) {
						}
						SwingUtilities.invokeLater(new Runnable() {						
							@Override
							public void run() {
								pArbol.visualizar(false, true, false);
							}
						});
					}
				}.start();
				hemosActualizado[0] = true;
			}
			
			//	Estructura
			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
					Ventana.thisventana.getTraza().getTecnicas())) {
				if (this.panel1.indexOfTab(this.nombresVistas[3]) == this.panel1
						.getSelectedIndex()
						|| this.panel2.indexOfTab(this.nombresVistas[3]) == this.panel2
						.getSelectedIndex()) {
					new Thread() {
						@Override
						public synchronized void run() {
							try {
								this.wait(220);
							} catch (java.lang.InterruptedException ie) {
							}
							SwingUtilities.invokeLater(new Runnable() {							
								@Override
								public void run() {
									pEstructura.visualizar();									
								}
							});
						}
					}.start();
					hemosActualizado[3] = true;
				}
			}
			
			//	Traza
			if (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
					Ventana.thisventana.getTraza().getTecnicas())) {
				if (this.panel1.indexOfTab(this.nombresVistas[2]) == this.panel1
						.getSelectedIndex()
						|| this.panel2.indexOfTab(this.nombresVistas[2]) == this.panel2
						.getSelectedIndex()) {
					pTraza.visualizar();
					hemosActualizado[2] = true;
				}
			}
			
			//	Grafo
			if (this.panel1.indexOfTab(this.nombresVistas[4]) == this.panel1
					.getSelectedIndex()
					|| this.panel2.indexOfTab(this.nombresVistas[4]) == this.panel2
					.getSelectedIndex()) {
				new Thread() {
					@Override
					public synchronized void run() {

						try {
							this.wait(240);
						} catch (java.lang.InterruptedException ie) {
						}
						SwingUtilities.invokeLater(new Runnable() {							
							@Override
							public void run() {
								if(pGrafo != null)
									pGrafo.visualizar();
							}
						});
					}
				}.start();
				hemosActualizado[4] = true;
			}
			
			// Valores Globales AABB
			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_AABB,
					Ventana.thisventana.getTraza().getTecnicas())) {
				new Thread() {
					@Override
					public synchronized void run() {
						try {
							this.wait(20);
						} catch (java.lang.InterruptedException ie) {
						}
						SwingUtilities.invokeLater(new Runnable() {							
							@Override
							public void run() {
								pValGlobal.visualizar();
							}
						});
					}
				}.start();
				hemosActualizado[5] = true;
			}
			
			// Valores de Rama AABB
			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_AABB,
					Ventana.thisventana.getTraza().getTecnicas())) {
				new Thread() {
					@Override
					public synchronized void run() {
						try {
							this.wait(20);
						} catch (java.lang.InterruptedException ie) {
						}
						SwingUtilities.invokeLater(new Runnable() {							
							@Override
							public void run() {
								pValRama.visualizar();
							}
						});
					}
				}.start();
				hemosActualizado[6] = true;
			}
		}
		separadorVistas.setDividerLocation(anterior);
	}

	/**
	 * Redibuja el contenido del panel del algoritmo, atendiendo a cambios en la
	 * configuración.
	 * 
	 * @param recargarCodigo
	 *            Si es necesario recargar también el panel de código.
	 */
	public void refrescarFormato(boolean recargarCodigo) {
		if (pArbol != null && Ventana.thisventana.traza != null) {
			this.mostrarNombreMetodos = Ventana.thisventana.traza.getNumMetodos() != 1;
			if (this.mostrarNombreMetodos) {
				nyp = new NombresYPrefijos();
				this.nombresMetodos = Ventana.thisventana.trazaCompleta
						.getNombresMetodos();
				String prefijos[] = ServiciosString
						.obtenerPrefijos(this.nombresMetodos);
				for (int i = 0; i < this.nombresMetodos.length; i++) {
					nyp.add(this.nombresMetodos[i], prefijos[i]);
				}
			}
			pArbol.visualizar(true, true, false);
			pPila.visualizar();
			if(grafoActivado)
				pGrafo.visualizar2(nyp);
			nyp = null;
			pCodigo.visualizar(recargarCodigo);

			if (Ventana.thisventana.getTraza() != null
					&& Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
							Ventana.thisventana.getTraza().getTecnicas())) {
				pCrono.visualizar();
				pEstructura.visualizar();
			
			} else {
				pTraza.visualizar();
			}
			if (Ventana.thisventana.getTraza() != null
					&& Arrays.contiene(MetodoAlgoritmo.TECNICA_AABB,
							Ventana.thisventana.getTraza().getTecnicas())) {
				pValGlobal.visualizar();
				pValRama.visualizar();
			}

			
			if (Ventana.thisventana.traza != null) {
				pControl.visualizar();
			}
			new Thread() {
				@Override
				public synchronized void run() {
					try {
						this.wait(250);
					} catch (java.lang.InterruptedException ie) {
					}
					SwingUtilities.invokeLater(new Runnable() {						
						@Override
						public void run() {
							if (pArbol.getNavegacionListener() != null) {
								pArbol.getNavegacionListener().ejecucion(1);
							}
						}
					});
				}
			}.start();
		}
		
		if(pCodigo != null)
			pCodigo.redibujarLineasErrores();
		
		if (FamiliaEjecuciones.getInstance().estaHabilitado()) {
			this.actualizarFamiliaEjecuciones(true);
		}
		
		this.updateUI();
	}

	/**
	 * Establece el valor de zoom para el panel del árbol.
	 * 
	 * @param valor
	 *            Valor de zoom.
	 */
	public void refrescarZoomArbol(int valor) {
		if (pArbol != null) {
			pArbol.refrescarZoom(valor);
			pArbol.visualizar(false, true, false);
		}
		this.updateUI();
	}

	/**
	 * Establece el valor de zoom para el panel de pila.
	 * 
	 * @param valor
	 *            Valor de zoom.
	 */
	public void refrescarZoomPila(int valor) {
		if (pPila != null) {
			pPila.refrescarZoom(valor);
			pPila.visualizar();
		}
		this.updateUI();
	}
	/**
	 * Establece el valor de zoom para el panel de pila.
	 * 
	 * @param valor
	 *            Valor de zoom.
	 */
	public void refrescarZoomTraza(int valor) {
		if (pTraza != null) {
			pTraza.refrescarZoom(valor);
			pTraza.visualizar();
		}
		this.updateUI();
	}
	/**
	 * Establece el valor de zoom para el panel de grafo
	 * 
	 * @param valor
	 * 			Valor de zoom.
	 */
	public void refrescarZoomGrafoDep(int valor){
		if(pGrafo != null){
			pGrafo.refrescarZoom(valor);
			pGrafo.visualizar();
		}
		this.updateUI();
	}

	/**
	 * Establece el valor de zoom para la vista especificada.
	 * 
	 * @param vista
	 *            1 -> pila, 0 -> arbol, 3 -> crono, 4 -> estructura, 5 -> Grafo dependencia.
	 * 
	 * @param valor
	 *            Valor de zoom.
	 * @param tipo
	 *            Necesario para realizar zoom en resultado Panel crono.
	 */
	public void refrescarZoom(int vista, int valor,int tipo) {
		switch (vista) {
		case 1:
			if (pPila != null) {
				pPila.refrescarZoom(valor);
				pPila.visualizar();
				pPila.updateUI();
			}
			break;
		case 0:
			if (pArbol != null) {
				pArbol.refrescarZoom(valor);
				pArbol.visualizar(false, true, false);
				pArbol.updateUI();
			}
			break;
		case 2:
			if (pCrono != null) {
				pCrono.refrescarZoom(valor,tipo);
				
				pCrono.visualizar();
				pCrono.updateUI();
			}
			break;
		case 3:
			if (pEstructura != null) {
				pEstructura.refrescarZoom(valor);
				pEstructura.visualizar();
				pEstructura.updateUI();
			}
			break;
		case 5:
			if (pGrafo != null) {
				pGrafo.refrescarZoom(valor);
				pGrafo.visualizar();
				pGrafo.updateUI();
			}
			break;
		}
	}

	/**
	 * Devuelve el título del panel.
	 * 
	 * @return Título del panel.
	 */
	public String getTituloPanel() {
		return this.tituloPanel;
	}

	/**
	 * Devuelve las dimensiones del panel y grafo del árbol.
	 * 
	 * @return {anchura_panel, altura_panel, achura_grafo, altura_grafo}
	 */
	public int[] dimPanelYGrafo() {
		return pArbol.dimPanelYGrafo();
	}

	/**
	 * Devuelve las dimensiones del panel y grafo de la pila.
	 * 
	 * @return {anchura_panel, altura_panel, achura_grafo, altura_grafo}
	 */
	public int[] dimPanelYPila() {
		return pPila.dimPanelYPila();
	}
	
	/**
	 * Devuelve las dimensiones del panel y grafo de la pila.
	 * 
	 * @return {anchura_panel, altura_panel, achura_grafo, altura_grafo}
	 */
	public int[] dimPanelYGrafoDep() {
		return pGrafo.dimPanelYGrafoDep();
	}

	/**
	 * Devuelve las dimensiones del panel y grafo de la vista cronológica.
	 * 
	 * @return {anchura_panel, altura_panel, achura_grafo, altura_grafo}
	 */
	public int[] dimPanelYGrafoCrono() {
		int[] val1 = pCrono.dimPanelYGrafo();
		int[] val2 = dimPanelCrono();

		int[] val = new int[4];
		val[0] = val2[0];
		val[1] = val2[1];
		val[2] = val1[2];
		val[3] = val1[3];

		return val;
	}

	/**
	 * Devuelve las dimensiones del panel y grafo de la vista de estructura.
	 * 
	 * @return {anchura_panel, altura_panel, achura_grafo, altura_grafo}
	 */
	public int[] dimPanelYGrafoEstructura() {
		int[] val1 = pEstructura.dimPanelYGrafo();
		int[] val2 = dimPanelEstructura();

		int[] val = new int[4];
		val[0] = val2[0];
		val[1] = val2[1];
		val[2] = val1[2];
		val[3] = val1[3];

		return val;
	}

	/**
	 * Devuelve el identificador de la traza actual.
	 * 
	 * @return Identificador de traza.
	 */
	public String getIdTraza() {
		return this.idTraza;
	}

	/**
	 * Devuelve el valor de zoom para cada uno de los paneles.
	 * 
	 * @return {zoom_arbol, zoom_pila, zoom_crono, zoom_estructura, zoom_grafoDependencia}
	 */
	public int[] getZooms() {
		int zooms[] = new int[5];

		zooms[0] = pArbol.getZoom();
		zooms[1] = pPila.getZoom();
		if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV, Ventana.thisventana
				.getTraza().getTecnicas())) {
			zooms[2] = pCrono.getZoom();
			zooms[3] = pEstructura.getZoom();
		}
		zooms[4] = pGrafo.getZoom();
		return zooms;
	}

	/**
	 * Devuelve las dimensiones del scroll panel de la vista de estructura.
	 * 
	 * @return {anchura, altura}
	 */
	public static int[] dimPanelEstructura() {
		int[] dim = new int[2];
		dim[0] = jspEstructura.getWidth();
		dim[1] = jspEstructura.getHeight();

		return dim;
	}

	/**
	 * Devuelve las dimensiones del scroll panel de la vista cronológica.
	 * 
	 * @return {anchura, altura}
	 */
	public static int[] dimPanelCrono() {
		int[] dim = new int[2];
		dim[0] = jspCrono.getWidth();
		dim[1] = jspCrono.getHeight();

		return dim;
	}
	
	/**
	 * Devuelve las dimensiones del scroll panel de la vista de Valores Globales.
	 * 
	 * @return {anchura, altura}
	 */
	public static int[] dimPanelValoresGlobales() {
		int[] dim = new int[2];
		dim[0] = jspValGlobales.getWidth();
		dim[1] = jspValGlobales.getHeight();

		return dim;
	}
	
	/**
	 * Devuelve las dimensiones del scroll panel de la vista de Valores de Rama.
	 * 
	 * @return {anchura, altura}
	 */
	public static int[] dimPanelValoresGRama() {
		int[] dim = new int[2];
		dim[0] = jspValRama.getWidth();
		dim[1] = jspValRama.getHeight();

		return dim;
	}

	/**
	 * Devuelve las dimensiones del scroll panel de la vista de traza.
	 * 
	 * @return {anchura, altura}
	 */
	public static int[] dimPanelTraza() {
		int[] dim = new int[2];
		dim[0] = jspTraza.getWidth();
		dim[1] = jspTraza.getHeight();

		return dim;
	}

	/**
	 * Devuelve las dimensiones del scroll panel de la vista de pila.
	 * 
	 * @return {anchura, altura}
	 */
	public static int[] dimPanelPila() {
		int[] dim = new int[2];
		dim[0] = jspPila.getWidth();
		dim[1] = jspPila.getHeight();

		return dim;
	}
	
	/**
	 * Devuelve las dimensiones del scroll panel de la vista del grafo de dependencia.
	 * 
	 * @return {anchura, altura}
	 */
	public static int[] dimPanelGrafoDep() {
		int[] dim = new int[2];
		dim[0] = jspGrafo.getWidth();
		dim[1] = jspGrafo.getHeight();

		return dim;
	}

	/**
	 * Devuelve las dimensiones del scroll panel principal.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimPanelPrincipal() {
		int[] dim = new int[2];
		dim[0] = this.jspArbol.getWidth();
		dim[1] = pArbol.alturaJSPArbol();

		return dim;
	}

	/**
	 * Devuelve las dimensiones del scroll panel de árbol.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimPanelArbol() {
		int[] dim = new int[2];
		dim[0] = this.jspArbol.getWidth();
		dim[1] = this.jspArbol.getHeight();

		return dim;
	}

	/**
	 * Devuelve las dimensiones del grafo del panel de pila.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoPila() {
		return pPila.dimGrafo();
	}	
	
	
	/**
	 * Devuelve las dimensiones del grafo del panel de grafo
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoDep(){
		return pGrafo.dimGrafo();
	}

	/**
	 * Devuelve las dimensiones del grafo del panel de árbol.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoPrincipal() {
		return pArbol.dimGrafo();
	}

	/**
	 * Devuelve las dimensiones del grafo visible del panel de árbol.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoVisiblePrincipal() {
		return pArbol.dimGrafoVisible();
	}

	/**
	 * Devuelve las dimensiones del grafo visible del panel cronológico.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoVisibleCrono() {
		return pCrono.dimGrafoVisible();
	}

	/**
	 * Devuelve las dimensiones del grafo visible del panel de estructura.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoVisibleEstructura() {
		return pEstructura.dimGrafoVisible();
	}

	/**
	 * Devuelve la posición del panel principal.
	 * 
	 * @return {x, y}
	 */
	public int[] posicPanelPrincipal() {
		int[] posOrigen = new int[2];
		posOrigen[0] = (int) this.jspArbol.getLocationOnScreen().getX();
		posOrigen[1] = (int) this.jspArbol.getLocationOnScreen().getY();

		return posOrigen;
	}

	/**
	 * Devuelve el contenedor de la vista del árbol.
	 * 
	 * @return vista de árbol.
	 */
	public JComponent getPanelArbol() {
		return this.jspArbol;
	}

	/**
	 * Establece como vista activa la indicada por parámetro.
	 * 
	 * @param nombre
	 *            Nombre de la vista.
	 */
	public void setVistaActiva(String nombre) {
		// System.out.println("panelalgoritmo.setVistaActiva");
		for (int i = 0; i < this.panel1.getTabCount(); i++) {
			if (this.panel1.getTitleAt(i).equals(nombre)) {
				//   System.out.println("    (1)setVistaActiva "+i);
				this.panel1.setSelectedIndex(i);
				panel1Pestana = i;
			}
		}

		for (int i = 0; i < this.panel2.getTabCount(); i++) {
			if (this.panel2.getTitleAt(i).equals(nombre)) {
				//	System.out.println("    (2)setVistaActiva "+i);
				this.panel2.setSelectedIndex(i);
				panel2Pestana = i;
			}
		}

	}

	/**
	 * Devuelve el panel de la vista indicada por parámetro.
	 * 
	 * @param nombre
	 *            Nombre de la vista.
	 * 
	 * @return Panel
	 */
	public JComponent getPanelPorNombre(String nombre) {

		if (nombre.equals(Texto.get(Vista.codigos[0], Conf.idioma))) {
			return pArbol;
		} else if (nombre.equals(Texto.get(Vista.codigos[1], Conf.idioma))) {
			return pPila;
		} else if ((nombre.equals(Texto.get(Vista.codigos[2], Conf.idioma)))
				&& (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
						Ventana.thisventana.getTraza().getTecnicas()))) {
			return pTraza;
		} else if ((nombre.equals(Texto.get(Vista.codigos[2], Conf.idioma)))
				&& (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
						Ventana.thisventana.getTraza().getTecnicas()))) {
			return pCrono;
		} else if (nombre.equals(Texto.get(Vista.codigos[3], Conf.idioma))) {
			return pEstructura;
		} else if (nombre.equals(Texto.get(Vista.codigos[4], Conf.idioma))) {
			return pGrafo;	
		} else if(nombre.equals(Texto.get(Vista.codigos[5], Conf.idioma)) 
				&& (Arrays.contiene(MetodoAlgoritmo.TECNICA_AABB,
						Ventana.thisventana.getTraza().getTecnicas()))){
			return pValGlobal;
		} else if(nombre.equals(Texto.get(Vista.codigos[6], Conf.idioma)) 
				&& (Arrays.contiene(MetodoAlgoritmo.TECNICA_AABB,
						Ventana.thisventana.getTraza().getTecnicas()))){
			return pValRama;
		} else {
			return null;
		}

	}

	/**
	 * Devuelve el grafo de la vista indicada por parámetro.
	 * 
	 * @param nombre
	 *            Nombre de la vista.
	 * 
	 * @return Grafo
	 */
	public Object getGrafoPorNombre(String nombre) {
		if (nombre.equals(Texto.get(Vista.codigos[0], Conf.idioma))) {
			return pArbol.getGrafo();
		} else if (nombre.equals(Texto.get(Vista.codigos[1], Conf.idioma))) {
			return pPila.getGrafo();
		} else if ((nombre.equals(Texto.get(Vista.codigos[2], Conf.idioma)))
				&& (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
						Ventana.thisventana.getTraza().getTecnicas()))) {
			return pCrono.getGrafo();
		} else if (nombre.equals(Texto.get(Vista.codigos[3], Conf.idioma))) {
			return pEstructura.getGrafo();
		}else if (nombre.equals(Texto.get(Vista.codigos[4], Conf.idioma))) {
			return pGrafo.getGrafo();
		}  else {
			return null;
		}
	}

	/**
	 * Devuelve el grafo de la vista indicada por parámetro.
	 * 
	 * @param numero
	 *            0 -> arbol, 1 -> pila, 3 -> crono, 4 -> estructura, 5 -> grafo dependencia.
	 * 
	 * @return Grafo
	 */
	public Object getGrafoPorNumero(int numero) {
		switch (numero) {
		case 0:
			return pArbol.getGrafo();
		case 1:
			return pPila.getGrafo();
		case 3:
			return pCrono.getGrafo();
		case 4:
			return pEstructura.getGrafo();
		case 5:
			return pGrafo.getGrafo();
		default:
			return null;
		}

	}

	/**
	 * Devuelve las dimensiones de la vista indicada por parámetro.
	 * 
	 * @param nombre
	 *            Nombre de la vista.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] getDimPanelPorNombre(String nombre) {
		int[] dim = new int[2];

		if (nombre.equals(Texto.get(Vista.codigos[0], Conf.idioma))) {
			dim = this.dimPanelArbol();
		} else if (nombre.equals(Texto.get(Vista.codigos[1], Conf.idioma))) {
			dim = dimPanelPila();
		} else if ((nombre.equals(Texto.get(Vista.codigos[2], Conf.idioma)))
				&& (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
						Ventana.thisventana.getTraza().getTecnicas()))) {
			dim = dimPanelTraza();
		} else if ((nombre.equals(Texto.get(Vista.codigos[2], Conf.idioma)))
				&& (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
						Ventana.thisventana.getTraza().getTecnicas()))) {
			dim = dimPanelCrono();
		} else if (nombre.equals(Texto.get(Vista.codigos[3], Conf.idioma))) {
			dim = dimPanelEstructura();
		}

		return dim;

	}

	/**
	 * Devuelve la posición de la vista indicada por parámetro.
	 * 
	 * @param nombre
	 *            Nombre de la vista.
	 * 
	 * @return {x, y}
	 */
	public int[] getPosicPanelPorNombre(String nombre) {
		int[] pos = new int[2];

		if (nombre.equals(Texto.get(Vista.codigos[0], Conf.idioma))) {
			pos[0] = (int) this.jspArbol.getLocationOnScreen().getX();
			pos[1] = (int) this.jspArbol.getLocationOnScreen().getY();
		} else if (nombre.equals(Texto.get(Vista.codigos[1], Conf.idioma))) {
			pos[0] = (int) jspPila.getLocationOnScreen().getX();
			pos[1] = (int) jspPila.getLocationOnScreen().getY();
		} else if ((nombre.equals(Texto.get(Vista.codigos[2], Conf.idioma)))
				&& (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
						Ventana.thisventana.getTraza().getTecnicas()))) {
			pos[0] = (int) jspTraza.getLocationOnScreen().getX();
			pos[1] = (int) jspTraza.getLocationOnScreen().getY();
		} else if ((nombre.equals(Texto.get(Vista.codigos[2], Conf.idioma)))
				&& (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
						Ventana.thisventana.getTraza().getTecnicas()))) {
			pos[0] = (int) jspCrono.getLocationOnScreen().getX();
			pos[1] = (int) jspCrono.getLocationOnScreen().getY();
		} else if (nombre.equals(Texto.get(Vista.codigos[3], Conf.idioma))) {
			pos[0] = (int) jspEstructura.getLocationOnScreen().getX();
			pos[1] = (int) jspEstructura.getLocationOnScreen().getY();
		} else if (nombre.equals(Texto.get(Vista.codigos[4], Conf.idioma))) {
			pos[0] = (int) jspGrafo.getLocationOnScreen().getX();
			pos[1] = (int) jspGrafo.getLocationOnScreen().getY();
		} else if (nombre.equals(Texto.get(Vista.codigos[5], Conf.idioma)) 
				&& (Arrays.contiene(MetodoAlgoritmo.TECNICA_AABB,
						Ventana.thisventana.getTraza().getTecnicas()))) {
			pos[0] = (int) jspValGlobales.getLocationOnScreen().getX();
			pos[1] = (int) jspValGlobales.getLocationOnScreen().getY();
		} else if (nombre.equals(Texto.get(Vista.codigos[6], Conf.idioma)) 
				&& (Arrays.contiene(MetodoAlgoritmo.TECNICA_AABB,
						Ventana.thisventana.getTraza().getTecnicas()))) {
			pos[0] = (int) jspValRama.getLocationOnScreen().getX();
			pos[1] = (int) jspValRama.getLocationOnScreen().getY();
		}

		return pos;

	}

	/**
	 * Establece el texto del panel del compilador.
	 * 
	 * @param texto
	 */
	public void setTextoCompilador(String texto) {
		pCompilador.setTextoCompilador(texto);
	}

	/**
	 * Permite guardar los cambios en el panel de código.
	 */
	public void guardarClase() {
		pCodigo.guardar();
	}

	/**
	 * Devuelve el panel de código.
	 * 
	 * @return Panel
	 */
	protected PanelCodigo getPanelCodigo() {
		return pCodigo;
	}
	public PanelGrafo getPanelGrafo() {
		return pGrafo;
	}
	/**
	 * Permite cerrar todas las vistas abiertas.
	 */
	public void cerrar() {
		this.cerrarVistas();
		if (Ventana.thisventana.getClase() == null) {
			this.cerrarPanelCodigo();
		}
	}

	/**
	 * Devuelve si el código abierto en el panel de código es editable o no.
	 * 
	 * @return true si es editable, false en caso contrario.
	 */
	public boolean getEditable() {
		return pCodigo.getEditable();
	}

	/**
	 * Elimina los bordes de todos los paneles.
	 */
	private void quitarBordesJSP() {
		// jspCodigo.setBorder(new EmptyBorder(0,0,0,0));	
		jspCompilador.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.jspArbol.setBorder(new EmptyBorder(0, 0, 0, 0));
		jspTraza.setBorder(new EmptyBorder(0, 0, 0, 0));
		jspPila.setBorder(new EmptyBorder(0, 0, 0, 0));
		jspCrono.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		jspEstructura.setBorder(new EmptyBorder(0, 0, 0, 0));
		jspGrafo.setBorder(new EmptyBorder(0, 0, 0, 0));
		jspValGlobales.setBorder(new EmptyBorder(0, 0, 0, 0));
		jspValRama.setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	/**
	 * Devuelve el nombre de todas las vistas que están disponibles en la
	 * visualización abierta
	 * 
	 * @return Lista con el nombre de todas las vistas.
	 */
	public String[] getNombreVistasDisponibles() {
		String[] vistas = new String[this.panel1.getTabCount()
		                             + this.panel2.getTabCount()];

		for (int i = 0; i < this.panel1.getTabCount(); i++) {
			vistas[i] = this.panel1.getTitleAt(i);
		}

		for (int i = 0; i < this.panel2.getTabCount(); i++) {
			vistas[i + this.panel1.getTabCount()] = this.panel2.getTitleAt(i);
		}

		return vistas;
	}

	/**
	 * Devuelve el nombre de las vistas que están visibles en ese instante
	 * (pestañas seleccionadas) en la visualización abierta
	 * 
	 * @return Lista con el nombre de todas las vistas.
	 */
	public String[] getNombreVistasVisibles() {
		String[] vistas = new String[2];

		for (int i = 0; i < this.panel1.getTabCount(); i++) {
			if (this.panel1.getSelectedIndex() == i) {
				vistas[0] = this.panel1.getTitleAt(i);
			}
		}

		for (int i = 0; i < this.panel2.getTabCount(); i++) {
			if (this.panel2.getSelectedIndex() == i) {
				vistas[1] = this.panel2.getTitleAt(i);
			}
		}

		return vistas;
	}

	/**
	 * Devuelve el código del nombre de las vistas que están visibles en ese
	 * instante (pestañas seleccionadas) en la visualización abierta
	 * 
	 * @return Lista con el código de todas las vistas.
	 */
	public int[] getCodigoVistasVisibles() {
		String[] nombresVistas = this.getNombreVistasVisibles();

		String[] codigoVistas = new String[nombresVistas.length];

		int[] vistas = new int[nombresVistas.length];

		for (int i = 0; i < codigoVistas.length; i++) {
			codigoVistas[i] = Texto.getCodigo(nombresVistas[i], Conf.idioma);
			vistas[i] = Vista.getPosic(codigoVistas[i]);
		}

		return vistas;
	}
	
	/**
	 * Permite establecer la vista/pestaña del grafo de dependencia
	 * 	como visible, así solo se hará visible cuando pulsen el botón de generar
	 * 	grafo de dependencia
	 * 
	 * @param metodo
     * 	Método del que queremos generar el grafo de dependencia
	 */
	public void vistaGrafoDependenciaVisible(DatosMetodoBasicos metodo){
		boolean familiaEjecucionesHabilitado = FamiliaEjecuciones.getInstance().estaHabilitado();
	    
	    //	Solo abrimos la pestaña si no está abierta o el método es distinto al actual
	    if(!grafoActivado || (pGrafo!=null && !pGrafo.esIgual(metodo))){
	    	try {
	    		
	    		//	Eliminamos pestaña por si ya estuviera abierta

				if (Conf.getVista(Vista.codigos[4]).getPanel() == 1 || familiaEjecucionesHabilitado) {
					this.panel1.remove(this.contenedorGrafo);
				} else {
					this.panel2.remove(this.contenedorGrafo);
				}
				this.contenedorGrafo=null;
				pGrafo=null;
				jspGrafo=null;
	    		
	    		//	Generamos el grafo de dependencia solo cuando pulsan botón de generar, no antes
				
				nyp = null;
				this.mostrarNombreMetodos = Ventana.thisventana.traza.getNumMetodos() != 1;

				if (this.mostrarNombreMetodos) {
					nyp = new NombresYPrefijos();
					this.nombresMetodos = Ventana.thisventana.trazaCompleta
							.getNombresMetodos();
					String prefijos[] = ServiciosString
							.obtenerPrefijos(this.nombresMetodos);
					for (int i = 0; i < this.nombresMetodos.length; i++) {
						nyp.add(this.nombresMetodos[i], prefijos[i]);
					}
				}
				
				pGrafo = new PanelGrafo(metodo,Ventana.thisventana,nyp);
				nyp = null;
				jspGrafo = new JScrollPane(pGrafo);

				this.contenedorGrafo = new JPanel();
				this.contenedorGrafo.setLayout(new BorderLayout());
				this.contenedorGrafo.add(jspGrafo,BorderLayout.CENTER);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	//	Ubicamos el grafo en el panel correspondiente
			if (Conf.getVista(Vista.codigos[4]).getPanel() == 1 || familiaEjecucionesHabilitado) {
				this.panel1.add(Texto.get(Vista.codigos[4], Conf.idioma),
						this.contenedorGrafo);
			} else {
				this.panel2.add(Texto.get(Vista.codigos[4], Conf.idioma),
						this.contenedorGrafo);
			}
			this.zoomAjusteGrafoInicial();
	    }
	    
	    //	Activa se pone siempre
		this.setVistaActiva(Texto.get(Vista.codigos[4], Conf.idioma));
		
		//	Actualizamos el estado, indica que la pestaña se ha abierto
		//		por primera vez
		grafoActivado = true;
	}
	
	/**
	 * Permite establecer la vista/pestaña del grafo de dependencia
	 * 	como visible, así solo se hará visible cuando pulsen el botón de generar
	 * 	grafo de dependencia
	 * 
	 * @param metodo
     * 	Lista de métodos de los que queremos generar el grafo de dependencia
	 */
	public void vistaGrafoDependenciaVisible(List<DatosMetodoBasicos> metodo){
		boolean familiaEjecucionesHabilitado = FamiliaEjecuciones.getInstance().estaHabilitado();
	    
	    //	Solo abrimos la pestaña si no está abierta o el método es distinto al actual
	    if(!grafoActivado || (pGrafo!=null && !pGrafo.esIgual(metodo))){
	    	try {
	    		
	    		//	Eliminamos pestaña por si ya estuviera abierta

				if (Conf.getVista(Vista.codigos[4]).getPanel() == 1 || familiaEjecucionesHabilitado) {
					this.panel1.remove(this.contenedorGrafo);
				} else {
					this.panel2.remove(this.contenedorGrafo);
				}
				this.contenedorGrafo=null;
				pGrafo=null;
				jspGrafo=null;
	    		
	    		//	Generamos el grafo de dependencia solo cuando pulsan botón de generar, no antes
				
				nyp = null;
				this.mostrarNombreMetodos = Ventana.thisventana.traza.getNumMetodos() != 1;

				if (this.mostrarNombreMetodos) {
					nyp = new NombresYPrefijos();
					this.nombresMetodos = Ventana.thisventana.trazaCompleta
							.getNombresMetodos();
					String prefijos[] = ServiciosString
							.obtenerPrefijos(this.nombresMetodos);
					for (int i = 0; i < this.nombresMetodos.length; i++) {
						nyp.add(this.nombresMetodos[i], prefijos[i]);
					}
				}
				
				pGrafo = new PanelGrafo(metodo,Ventana.thisventana,nyp);
				nyp = null;
				jspGrafo = new JScrollPane(pGrafo);

				this.contenedorGrafo = new JPanel();
				this.contenedorGrafo.setLayout(new BorderLayout());
				this.contenedorGrafo.add(jspGrafo,BorderLayout.CENTER);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	//	Ubicamos el grafo en el panel correspondiente
			if (Conf.getVista(Vista.codigos[4]).getPanel() == 1 || familiaEjecucionesHabilitado) {
				this.panel1.add(Texto.get(Vista.codigos[4], Conf.idioma),
						this.contenedorGrafo);
			} else {
				this.panel2.add(Texto.get(Vista.codigos[4], Conf.idioma),
						this.contenedorGrafo);
			}
			this.zoomAjusteGrafoInicial();
	    }
	    
	    //	Activa se pone siempre
		this.setVistaActiva(Texto.get(Vista.codigos[4], Conf.idioma));
		
		//	Actualizamos el estado, indica que la pestaña se ha abierto
		//		por primera vez
		grafoActivado = true;
	}
	
	/**
	 * Permite obtener si la pestaña del grafo de dependencia
	 * 	esta activa o no
	 * @return True si pestaña activa, false caso contrario 
	 */
	public static Boolean getGrafoActivado() {
		return grafoActivado;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		if (!this.abriendoVistas) {
			this.actualizar();
		}
	}
	
	@Override
    public void componentResized(ComponentEvent e) {
		if (e.getSource() == FamiliaEjecuciones.getInstance().obtenerPanelEjecuciones()) {
			int tamanio = Conf.disposicionPaneles == Conf.PANEL_HORIZONTAL ?
					separadorVistas.getHeight() : separadorVistas.getHeight();
			if (separadorVistas.getDividerLocation() < (tamanio - GROSOR_SPLIT_DIVIDER - 1)) {
				actualizarFamiliaEjecuciones(false);
			}
		}
	}

    @Override
    public void componentMoved(ComponentEvent e) {
    	
    }

    @Override
    public void componentShown(ComponentEvent e) {
    	
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    	
    }
    
    /**
     * 	Permite ajustar el grafo al panel cuando se muestra
     * 	por primera vez
     */
    private void zoomAjusteGrafoInicial(){
    	jspGrafo.getTopLevelAncestor().validate();
    	int[] dimensionesGrafo = this.dimPanelYGrafoDep();	
    	
    	int panelW = dimensionesGrafo[0];
    	int panelH = dimensionesGrafo[1];
    	int grafoW = dimensionesGrafo[2];
    	int grafoH = dimensionesGrafo[3];	
    	
    	double propAncho = (double) panelW / (double) grafoW;
    	double propAlto = (double) panelH / (double) grafoH;
    	double porc = Math.min(propAncho, propAlto);
    	int valorNuevo = 0;

    	if (grafoW > panelW || grafoH > panelH) {
    		valorNuevo = ((int) (porc * 100)) - 100;
    	} else {
    		valorNuevo = (int) ((porc - 1) * 100) - 2;
    	}	
    	pGrafo.refrescarZoom(valorNuevo);
    }
    
    /**
     * Recuerda la pestaña seleccionada en los paneles si es posible,
     * sino la establace a la primera
     */
    private void recordarPestanaPaneles(){
    	if(panel1Pestana<this.panel1.getTabCount())
			this.panel1.setSelectedIndex(panel1Pestana);
		else{
			if(this.panel1!=null && this.panel1.getTabCount()>0)
				this.panel1.setSelectedIndex(0);
			panel1Pestana = 0;
		}
		
		if(panel2Pestana<this.panel2.getTabCount())
			this.panel2.setSelectedIndex(panel2Pestana);
		else{
			if(this.panel2!=null && this.panel2.getTabCount()>0)
				this.panel2.setSelectedIndex(0);
			panel2Pestana = 0;
		}
    }
    
    /**
	 * Permite subrayar una línea del editor
	 * 
	 * @param numeroLinea
	 * 		Número de línea a subrayar
	 */
	public void subrayarLineaEditor(int numeroLinea){
		this.getPanelCodigo().subrayarLineaEditor(numeroLinea);
		this.getPanelCodigo().focusLinea(numeroLinea);
	}
    
	/**
	 * Elimina todas las líneas subrayadas, para limpiar
	 */
	public void removeSelects(){
		this.getPanelCodigo().removeSelects();
	}
	
	/**
	 * Establece el tema visual del editor
	 * 
	 * @param tema
	 * 
	 * 		Número del 0 al 6, temas disponibles:
	 * 
	 * 			0: "default.xml";
	 *
	 *			1: "dark.xml";
	 *		
	 *			2: "eclipse.xml";
	 *			
	 * 			3: "idea.xml";
	 *		
	 *			4: "monokai.xml";
	 * 					
	 *			5: "vs.xml";
	 *	
	 *			otro número: "default.xml";
	 * 
	 */
	public void changeTheme(int tema){
		this.getPanelCodigo().changeTheme(tema);
	}
	
	/**
	 * Obtiene el panel de código
	 * 
	 * @return
	 * 
	 * 		JScrollPane de código
	 */
	public JScrollPane getJSPCodigo() {
		return jspCodigo;
	}
	
    /**
     * Detecta clicks en las pestañas de cada panel unicamente cuando
     * el usuario pulsa en una pestaña, para recordarla entre ejecuciones
     * (para esto no sirve stateChanged porque recibe "clicks extras")
     */
    private void anadirMouseEventPaneles(){
    	this.panel1.addMouseListener(new MouseListener()
    	{

    		@Override
    		public void mouseClicked(MouseEvent e) {
    			// TODO Auto-generated method stub
    			panel1Pestana = PanelAlgoritmo.this.panel1.getSelectedIndex();
    		}

    		@Override
    		public void mousePressed(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void mouseReleased(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void mouseEntered(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void mouseExited(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    	});
    	
    	this.panel2.addMouseListener(new MouseListener()
    	{

    		@Override
    		public void mouseClicked(MouseEvent e) {
    			// TODO Auto-generated method stub
    			panel2Pestana = PanelAlgoritmo.this.panel2.getSelectedIndex();
    		}

    		@Override
    		public void mousePressed(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void mouseReleased(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void mouseEntered(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void mouseExited(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    	});
    }
}
