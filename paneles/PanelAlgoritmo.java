package paneles;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
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
import datos.FamiliaEjecuciones;
import datos.MetodoAlgoritmo;
import eventos.NavegacionListener;

/**
 * Contiene los paneles que conforman la representaci�n del algoritmo en
 * ejecuci�n. Est� contenido en un panel contenedor del panel de la ventana
 * 
 * @author Antonio P�rez Carrasco
 * @version 2006-2007
 */
public class PanelAlgoritmo extends JPanel implements ChangeListener {
	static final long serialVersionUID = 14;

	private static JSplitPane separadorCodigo, separadorVistas,
			separadorCentral;

	private GestorOpciones gOpciones = new GestorOpciones();

	// Paneles que est�n contenidos en este panel

	private static PanelCodigo pCodigo;
	private static PanelCompilador pCompilador;

	private static PanelTraza pTraza;
	private static PanelPila pPila;
	private static PanelArbol pArbol;

	private static PanelCrono pCrono;
	private static PanelEstructura pEstructura;

	private static PanelControl pControl;

	private String tituloPanel;
	private String idTraza;

	private String[] nombresMetodos;
	public static NombresYPrefijos nyp = null;

	private static JScrollPane jspCompilador, jspCodigo, jspTraza, jspPila,
			jspCrono, jspEstructura;
	private JPanel jspArbol;

	private JPanel contenedorCompilador, contenedorCodigo, contenedorTraza,
	contenedorPila, contenedorArbol, contenedorControl,
	contenedorCrono, contenedorEstructura;

	private JTabbedPane panel1, panel2;

	private boolean mostrarNombreMetodos = false;

	private boolean ocupado = false;

	private boolean abriendoVistas = false;

	private JPanel panelGral;

	private String[] nombresVistas = new String[Vista.codigos.length];
	private boolean[] vistasActualizadas = new boolean[Vista.codigos.length];

	private NavegacionListener arbolNavegacionListener;

	/**
	 * Crea un nuevo PanelAlgoritmo
	 * 
	 * @throws Exception
	 */
	public PanelAlgoritmo() throws Exception {
		// Creamos el panel de la izquierda (contiene el panel del c�digo y el
		// de la traza)
		JPanel izqda = new JPanel();
		izqda.setLayout(new BorderLayout());

		pCodigo = new PanelCodigo(null);
		pTraza = new PanelTraza();
		pCompilador = new PanelCompilador(this);

		// jspCodigo = new JScrollPane(pCodigo);
		jspTraza = new JScrollPane(pTraza);
		// jspCodigo.setPreferredSize(new Dimension(250,250));

		this.contenedorCodigo = new JPanel();
		this.contenedorCompilador = new JPanel();
		this.contenedorTraza = new JPanel();
		this.contenedorCodigo.setLayout(new BorderLayout());
		this.contenedorCompilador.setLayout(new BorderLayout());
		this.contenedorTraza.setLayout(new BorderLayout());
		this.contenedorCodigo.add(pCodigo.getPanel(), BorderLayout.CENTER);
		this.contenedorCompilador.add(pCompilador.getPanel(),
				BorderLayout.CENTER);
		this.contenedorTraza.add(jspTraza);

		jspCompilador = new JScrollPane(this.contenedorCompilador);

		separadorCodigo = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				this.contenedorCodigo, jspCompilador);
		separadorCodigo.setResizeWeight(0.85);
		separadorCodigo.setDividerLocation(0.8);

		izqda.add(separadorCodigo, BorderLayout.CENTER);

		// Creamos el panel de la derecha (contiene el panel del �rbol y el de
		// la pila)
		JPanel dcha = new JPanel();
		dcha.setLayout(new BorderLayout());

		try {
			pPila = new PanelPila(null);
			pArbol = new PanelArbol(null);
			pCrono = new PanelCrono(null);
		} catch (OutOfMemoryError oome) {
			pArbol = null;
			throw oome;
		} catch (Exception e) {
			pArbol = null;
			e.printStackTrace();
			throw e;
		}
		jspPila = new JScrollPane(pPila);

		this.jspArbol = new JPanel();
		this.jspArbol.setLayout(new BorderLayout());
		this.jspArbol.add(pArbol, BorderLayout.CENTER);

		this.contenedorPila = new JPanel();
		this.contenedorArbol = new JPanel();
		this.contenedorPila.setLayout(new BorderLayout());
		this.contenedorPila.add(jspPila, BorderLayout.CENTER);
		this.contenedorArbol.setLayout(new BorderLayout());
		this.contenedorArbol.add(this.jspArbol, BorderLayout.CENTER);

		jspCrono = new JScrollPane(pCrono);
		this.contenedorCrono = new JPanel();
		this.contenedorCrono.setLayout(new BorderLayout());
		this.contenedorCrono.add(jspCrono, BorderLayout.CENTER);

		jspEstructura = new JScrollPane(pEstructura);
		this.contenedorEstructura = new JPanel();
		this.contenedorEstructura.setLayout(new BorderLayout());
		this.contenedorEstructura.add(jspEstructura, BorderLayout.CENTER);

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
		separadorVistas.setDividerSize(8);
		separadorVistas.setOneTouchExpandable(true);
		separadorVistas.setResizeWeight(0.5);
		separadorVistas.setDividerLocation(0.5);

		dcha.add(separadorVistas, BorderLayout.CENTER);

		// Creamos panel superior (nombre de m�todo, botones, ...)
		pControl = new PanelControl("", this);
		this.contenedorControl = new JPanel();
		this.contenedorControl.setLayout(new BorderLayout());
		this.contenedorControl.add(pControl, BorderLayout.CENTER);

		// Creamos panel de contenci�n (contendr� las cuatro vistas)
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
	}

	/**
	 * Distribuye los paneles segun la disposici�n especificada.
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
	 * Establece el idioma establecido por configuraci�n.
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
			// traducci�n
		{
			for (int j = 0; j < idiomas.length; j++) // Buscamos en todos los
				// idiomas, no sabemos
				// en qu� idioma est� el
				// nombre que tenemos
			{
				for (int k = 0; k < Vista.codigos.length; k++) // Buscamos entre
					// los nombres
					// que tenemos
					// guardados en
					// XML
					// bas�ndonos en
					// c�digos de
					// Vista.codigos
				{
					if (nombres[i].equals(Texto.get(Vista.codigos[k],
							idiomas[j][1]))) {
						// Ahora buscamos en panel1 y panel2 para saber d�nde
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
	 * Devuelve el nombre de cada una de las vistas seg�n la configuraci�n de
	 * idioma.
	 * 
	 * @return array con los nombres de las vistas, en el mismo orden que los
	 *         c�digo especificados en Vista.codigos
	 */
	private String[] recopilarNombresVistas() {
		return Texto.get(Vista.codigos, Conf.idioma);
	}

	/**
	 * Permite abrir el Panel de c�digo.
	 * 
	 * @param nombreArchivo
	 *            Archivo que contiene el c�digo fuente de la clase.
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
		JScrollPane jsp = new JScrollPane(pCodigo.getPanel());
		this.quitarBordesJSP();

		this.contenedorCodigo.add(jsp);
		this.contenedorCodigo.updateUI();

	}

	/**
	 * Permite cerrar el Panel de c�digo.
	 */
	public void cerrarPanelCodigo() {
		this.contenedorCodigo.removeAll();

		pCodigo = new PanelCodigo(null);

		this.quitarBordesJSP();
		this.contenedorCodigo.updateUI();

		nyp = null;
	}

	/**
	 * Permite abrir las vistas necesarias para mostrar la ejecuci�n.
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
			pArbol = new PanelArbol(nyp);
			pPila = new PanelPila(nyp);

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
					pArbol.getNavegacionListener().ejecucion(1);

					pArbol.updateUI();

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
				pArbol = new PanelArbol(null);
				pPila = new PanelPila(null);
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

		this.jspArbol.removeAll();
		jspPila.removeAll();
		jspTraza.removeAll();
		jspCrono.removeAll();
		jspEstructura.removeAll();

		this.jspArbol.add(pArbol);
		jspPila = new JScrollPane(pPila);
		jspTraza = new JScrollPane(pTraza);
		jspCrono = new JScrollPane(pCrono);
		jspEstructura = new JScrollPane(pEstructura);

		this.contenedorArbol.add(this.jspArbol);
		this.contenedorPila.add(jspPila);
		this.contenedorTraza.add(jspTraza);
		this.contenedorCrono.add(jspCrono);
		this.contenedorEstructura.add(jspEstructura);

		this.quitarBordesJSP();

		this.contenedorArbol.updateUI();
		this.contenedorPila.updateUI();
		this.contenedorTraza.updateUI();
		this.contenedorCrono.updateUI();
		this.contenedorEstructura.updateUI();
		this.contenedorControl.updateUI();
		this.abriendoVistas = false;
	}

	/**
	 * Permite abrir las vistas necesarias para mostrar una ejecuci�n dado un
	 * fichero GIF.
	 * 
	 * @param ficheroGIF
	 *            Fichero que contiene la animaci�n de un algoritmo.
	 */
	public void abrirVistas(String ficheroGIF) {
		this.abriendoVistas = true;

		this.ubicarVistas();

		try {
			pArbol = new PanelArbol(ficheroGIF, new ImageIcon(ficheroGIF));
			pPila = new PanelPila(null);
			pTraza = new PanelTraza();
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
				pArbol = new PanelArbol(null);
				pPila = new PanelPila(null);
				pTraza = new PanelTraza();
				pControl = new PanelControl("", this);
				this.ocupado = false;
			} catch (Exception e2) {
			}
		}

		this.contenedorArbol.removeAll();
		this.contenedorPila.removeAll();
		this.contenedorTraza.removeAll();

		this.jspArbol.removeAll();
		jspPila.removeAll();
		jspTraza.removeAll();

		this.jspArbol.add(pArbol);
		jspPila = new JScrollPane(pPila);
		jspTraza = new JScrollPane(pTraza);

		this.contenedorArbol.add(this.jspArbol);
		this.contenedorPila.add(jspPila);
		this.contenedorTraza.add(jspTraza);

		this.quitarBordesJSP();

		this.contenedorArbol.updateUI();
		this.contenedorPila.updateUI();
		this.contenedorTraza.updateUI();
		this.contenedorControl.updateUI();

		this.abriendoVistas = false;
	}

	/**
	 * Permite cerrar las vistas de ejecuci�n del algoritmo.
	 */
	public void cerrarVistas() {
		try {
			this.ocupado = false;
			Ventana.thisventana.traza = null;
			Ventana.thisventana.trazaCompleta = null;
			pArbol = new PanelArbol(null);
			pPila = new PanelPila(null);
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

		this.jspArbol.removeAll();
		jspPila.removeAll();
		jspTraza.removeAll();
		jspCrono.removeAll();
		jspEstructura.removeAll();

		this.jspArbol.add(pArbol);
		jspPila = new JScrollPane(pPila);
		jspTraza = new JScrollPane(pTraza);
		jspCrono = new JScrollPane(pCrono);
		jspEstructura = new JScrollPane(pEstructura);

		this.contenedorArbol.add(this.jspArbol);
		this.contenedorPila.add(jspPila);
		this.contenedorTraza.add(jspTraza);
		this.contenedorCrono.add(jspCrono);
		this.contenedorEstructura.add(jspEstructura);

		this.quitarBordesJSP();

		this.contenedorArbol.updateUI();
		this.contenedorPila.updateUI();
		this.contenedorTraza.updateUI();
		this.contenedorCrono.updateUI();
		this.contenedorEstructura.updateUI();

		this.contenedorControl.updateUI();

		this.panel1.removeAll();
		this.panel2.removeAll();

		nyp = null;
	}
	
	/**
	 * Permite ubicar las distintas vistas seg�n los valores de configuraci�n de
	 * ubicaci�n y disposici�n de paneles.
	 */
	public void ubicarVistas() {
		
		boolean familiaEjecucionesHabilitado = FamiliaEjecuciones.getInstance().estaHabilitado();
		
		if (familiaEjecucionesHabilitado) {
			separadorVistas.setRightComponent(FamiliaEjecuciones.getInstance().obtenerPanelEjecuciones());
		} else {
			separadorVistas.setRightComponent(this.panel2);
		}
		
		// Vista de �rbol
		if (Conf.getVista(Vista.codigos[0]).getPanel() == 1 || familiaEjecucionesHabilitado) {
			this.panel1.add(Texto.get(Vista.codigos[0], Conf.idioma),
					this.contenedorArbol);
		} else {
			this.panel2.add(Texto.get(Vista.codigos[0], Conf.idioma),
					this.contenedorArbol);
		}

		if (Ventana.thisventana.getTraza() != null) // Ser� null si estamos
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
				// Vista cronol�gica
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
		}
	}

	/**
	 * Determina si est� abierto o cerrado el panel (es decir, si no est�
	 * visualizando nada en su interior)
	 * 
	 * @return true si el panel est� abierto, false en caso contrario.
	 */
	public boolean estaOcupado() {
		return this.ocupado;
	}

	/**
	 * Permite deshabilitar los controles de la animaci�n.
	 */
	protected void deshabilitarControles() {
		pControl.deshabilitarControles();
	}

	/**
	 * Permite habilitar los controles de la animaci�n.
	 */
	protected void habilitarControles() {
		pControl.habilitarControles();
	}

	/**
	 * Actualiza los valores del panel de control.
	 * 
	 * @param tituloPanel
	 *            T�tulo del panel.
	 */
	protected void setValoresPanelControl(String tituloPanel) {
		pControl.setValores(tituloPanel, this);
	}

	/**
	 * Actualiza la visualizaci�n de los distintos paneles que componen el
	 * panel.
	 */
	public void actualizar() {
		if (Ventana.thisventana.getTraza() != null) {

			boolean[] hemosActualizado = new boolean[Vista.codigos.length];
			for (int i = 0; i < hemosActualizado.length; i++) {
				hemosActualizado[i] = false;
			}

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
						pPila.visualizar();
					}
				}.start();
				hemosActualizado[1] = true;
			}

			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
					Ventana.thisventana.getTraza().getTecnicas())) {
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
							pCrono.visualizar();
						}
					}.start();
					hemosActualizado[2] = true;
				}
			}

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
						pArbol.visualizar(false, true, false);
					}
				}.start();
				hemosActualizado[0] = true;
			}

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
							pEstructura.visualizar();
						}
					}.start();
					hemosActualizado[3] = true;
				}
			}
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
		}
	}

	/**
	 * Redibuja el contenido del panel del algoritmo, atendiendo a cambios en la
	 * configuraci�n.
	 * 
	 * @param recargarCodigo
	 *            Si es necesario recargar tambi�n el panel de c�digo.
	 */
	public void refrescarFormato(boolean recargarCodigo) {
		if (pArbol != null) {
			pArbol.visualizar(true, true, false);
			pPila.visualizar();

			pCodigo.visualizar(recargarCodigo);

			if (Ventana.thisventana.getTraza() != null
					&& Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,
							Ventana.thisventana.getTraza().getTecnicas())) {
				pCrono.visualizar();
				pEstructura.visualizar();
			} else {
				pTraza.visualizar();
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
					if (pArbol.getNavegacionListener() != null) {
						pArbol.getNavegacionListener().ejecucion(1);
					}
				}
			}.start();
		}
		this.updateUI();
	}

	/**
	 * Establece el valor de zoom para el panel del �rbol.
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
	 * Establece el valor de zoom para la vista especificada.
	 * 
	 * @param vista
	 *            1 -> pila, 0 -> arbol, 3 -> crono, 4 -> estructura.
	 * 
	 * @param valor
	 *            Valor de zoom.
	 */
	public void refrescarZoom(int vista, int valor) {
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
		case 3:
			if (pCrono != null) {
				pCrono.refrescarZoom(valor);
				pCrono.visualizar();
				pCrono.updateUI();
			}
			break;
		case 4:
			if (pEstructura != null) {
				pEstructura.refrescarZoom(valor);
				pEstructura.visualizar();
				pEstructura.updateUI();
			}
			break;
		}
	}

	/**
	 * Devuelve el t�tulo del panel.
	 * 
	 * @return T�tulo del panel.
	 */
	public String getTituloPanel() {
		return this.tituloPanel;
	}

	/**
	 * Devuelve las dimensiones del panel y grafo del �rbol.
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
	 * Devuelve las dimensiones del panel y grafo de la vista cronol�gica.
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
	 * @return {zoom_arbol, zoom_pila, zoom_crono, zoom_estructura}
	 */
	public int[] getZooms() {
		int zooms[] = new int[4];

		zooms[0] = pArbol.getZoom();
		zooms[1] = pPila.getZoom();
		if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV, Ventana.thisventana
				.getTraza().getTecnicas())) {
			zooms[2] = pCrono.getZoom();
			zooms[3] = pEstructura.getZoom();
		}
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
	 * Devuelve las dimensiones del scroll panel de la vista cronol�gica.
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
	 * Devuelve las dimensiones del scroll panel de �rbol.
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
	 * Devuelve las dimensiones del grafo del panel de �rbol.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoPrincipal() {
		return pArbol.dimGrafo();
	}

	/**
	 * Devuelve las dimensiones del grafo visible del panel de �rbol.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoVisiblePrincipal() {
		return pArbol.dimGrafoVisible();
	}

	/**
	 * Devuelve las dimensiones del grafo visible del panel cronol�gico.
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
	 * Devuelve la posici�n del panel principal.
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
	 * Devuelve el contenedor de la vista del �rbol.
	 * 
	 * @return vista de �rbol.
	 */
	public JComponent getPanelArbol() {
		return this.jspArbol;
	}

	/**
	 * Establece como vista activa la indicada por par�metro.
	 * 
	 * @param nombre
	 *            Nombre de la vista.
	 */
	public void setVistaActiva(String nombre) {
		// System.out.println("panelalgoritmo.setVistaActiva");
		for (int i = 0; i < this.panel1.getTabCount(); i++) {
			if (this.panel1.getTitleAt(i).equals(nombre)) {
				// System.out.println("    (1)setVistaActiva "+i);
				this.panel1.setSelectedIndex(i);
			}
		}

		for (int i = 0; i < this.panel2.getTabCount(); i++) {
			if (this.panel2.getTitleAt(i).equals(nombre)) {
				// System.out.println("    (2)setVistaActiva "+i);
				this.panel2.setSelectedIndex(i);
			}
		}

	}

	/**
	 * Devuelve el panel de la vista indicada por par�metro.
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
		} else {
			return null;
		}

	}

	/**
	 * Devuelve el grafo de la vista indicada por par�metro.
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
		} else {
			return null;
		}
	}

	/**
	 * Devuelve el grafo de la vista indicada por par�metro.
	 * 
	 * @param numero
	 *            0 -> arbol, 1 -> pila, 3 -> crono, 4 -> estructura.
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
		default:
			return null;
		}

	}

	/**
	 * Devuelve las dimensiones de la vista indicada por par�metro.
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
	 * Devuelve la posici�n de la vista indicada por par�metro.
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
	 * Permite guardar los cambios en el panel de c�digo.
	 */
	public void guardarClase() {
		pCodigo.guardar();
	}

	/**
	 * Devuelve el panel de c�digo.
	 * 
	 * @return Panel
	 */
	protected PanelCodigo getPanelCodigo() {
		return pCodigo;
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
	 * Devuelve si el c�digo abierto en el panel de c�digo es editable o no.
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
	}

	/**
	 * Devuelve el nombre de todas las vistas que est�n disponibles en la
	 * visualizaci�n abierta
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
	 * Devuelve el nombre de las vistas que est�n visibles en ese instante
	 * (pesta�as seleccionadas) en la visualizaci�n abierta
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
	 * Devuelve el c�digo del nombre de las vistas que est�n visibles en ese
	 * instante (pesta�as seleccionadas) en la visualizaci�n abierta
	 * 
	 * @return Lista con el c�digo de todas las vistas.
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

	@Override
	public void stateChanged(ChangeEvent arg0) {
		if (!this.abriendoVistas) {
			this.actualizar();
		}
	}
}
