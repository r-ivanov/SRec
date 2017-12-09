package ventanas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.plaf.metal.MetalBorders;

import opciones.GestorOpciones;
import opciones.OpcionBorradoFicheros;
import opciones.OpcionOpsVisualizacion;
import utilidades.Texto;
import conf.Conf;
import cuadros.CuadroPreguntaEdicionNoGuardada;

/**
 * Gestiona los eventos de la Ventana principal de SRec
 */
public class GestorVentanaSRec implements WindowListener, WindowStateListener {

	/**
	 * Crea el menú de la aplicación, añadiendo todos los submenús.
	 * 
	 * @param menus
	 *            Menús de la aplicación.
	 */
	protected static void crearMenu(JMenu menus[]) {
		boolean[][] estadoMenu = getEstadoVisibilidadMenu(Ventana.thisventana.barramenu);

		if (Ventana.thisventana.getJMenuBar() != null) {
			Ventana.thisventana.getJMenuBar().setVisible(false);
			Ventana.thisventana.remove(Ventana.thisventana.getJMenuBar());
			Ventana.thisventana.validate();
		}

		Ventana.thisventana.barramenu = new JMenuBar();
		try {
			anadirMenus(Ventana.thisventana.barramenu, menus);
		} catch (java.lang.NullPointerException npe) {
			System.out.println("\n\n\n\n");
			for (int i = 0; i < 50; i++) {
				System.out.print("-");
			}
			System.out
			.println(" SREC ERROR\n\n Errores con el fichero 'Textos.xml', no se pudieron cargar los textos.\n");
			System.out.println(" SRec se ha cerrado.\n\n");
			for (int i = 0; i < 50; i++) {
				System.out.print("-");
			}
			System.out
			.println(" SREC ERROR\n\n Errors in the file 'Textos.xml', SRec could not load the texts.\n");
			System.out.println(" SRec is exiting.\n\n");
			for (int i = 0; i < 50; i++) {
				System.out.print("-");
			}
			System.out.print("\n\n\n\n");

			Ventana.thisventana.cerrar();
		}

		for (int i = 0; i < menus.length; i++) {
			menus[i].addMenuListener(new GestorMenuVentanaSRec());
		}

		if (estadoMenu != null) {
			setEstadoVisibilidadMenu(Ventana.thisventana.barramenu, estadoMenu);
		}

		Ventana.thisventana.setJMenuBar(Ventana.thisventana.barramenu);
	}

	/**
	 * Añade los menús necesarios a la barra de menú de la ventana principal.
	 * 
	 * @param barramenu
	 *            Recibe la barra de menús.
	 * @param menus
	 *            Submenús de la ventana principal.
	 */
	private static void anadirMenus(JMenuBar barramenu, JMenu menus[]) {
		String mnemotecnicos;

		// Menú Archivo
		String noArchivo[] = { Texto.get("MENU_ARCH_00", Conf.idioma),
				Texto.get("MENU_ARCH_02", Conf.idioma),
				Texto.get("MENU_ARCH_03", Conf.idioma),
				Texto.get("MENU_ARCH_04", Conf.idioma),
				Texto.get("MENU_ARCH_14", Conf.idioma),
				Texto.get("MENU_ARCH_15", Conf.idioma),
				Texto.get("MENU_ARCH_06", Conf.idioma),
				Texto.get("MENU_ARCH_13", Conf.idioma),
				Texto.get("MENU_ARCH_07", Conf.idioma),
				Texto.get("MENU_ARCH_09", Conf.idioma),
				Texto.get("MENU_ARCH_11", Conf.idioma),
				Texto.get("MENU_ARCH_10", Conf.idioma),
				Texto.get("MENU_ARCH_16", Conf.idioma),
				Texto.get("MENU_ARCH_12", Conf.idioma),
				Texto.get("MENU_ARCH_08", Conf.idioma) };
		char mnArchivo[] = new char[noArchivo.length];
		mnemotecnicos = Texto.get("MNEMO_ARCHIVO", Conf.idioma);
		for (int i = 0; i < mnArchivo.length; i++) {
			mnArchivo[i] = mnemotecnicos.charAt(i);
		}

		String nombreMenuArchivo = Texto.get("MENU_ARCH_N", Conf.idioma);
		char mnMenuArchivo = mnemotecnicos.charAt(mnemotecnicos.length() - 1);
		URL iconosArchivo[] = {
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_nuevaclase.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_guardarclase.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_abrirclase.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_compilarclase.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_seleccionmetodo.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_nuevavisualizacion.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_abrirvisualizacion.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_cargargif.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_guardarvisualizacion.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_exportaranimacion.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_exportarestados.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_exportarestado.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_exportar_ejecuciones.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_exportartraza.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_cerrar.gif") };
		int sepArchivo[] = { 3, 8, 12 }; // Separador debajo de las cuarta,
		// séptima y décima opción de menú
		// (contamos desde cero)
		KeyStroke ksArchivo[] = {
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E,
						java.awt.Event.CTRL_MASK),
						KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
								java.awt.Event.CTRL_MASK),
								KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P,
										java.awt.Event.CTRL_MASK),
										KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,
												java.awt.Event.CTRL_MASK),
												KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M,
														java.awt.Event.CTRL_MASK),
														KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
																java.awt.Event.CTRL_MASK),
																// null,
																KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
																		java.awt.Event.CTRL_MASK),
																		KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I,
																				java.awt.Event.CTRL_MASK),
																				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G,
																						java.awt.Event.CTRL_MASK),
																						null,
																						null,
																						null,
																						null,
																						KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T,
																								java.awt.Event.CTRL_MASK),
																								KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,
																										java.awt.Event.CTRL_MASK) };
		menus[0] = creaMenu(noArchivo, mnArchivo, iconosArchivo,
				nombreMenuArchivo, mnMenuArchivo, sepArchivo, ksArchivo);

		habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_04", Conf.idioma),
				false);
		habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_14", Conf.idioma),
				false);
		habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_15", Conf.idioma),
				false);
		habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_07", Conf.idioma),
				false);
		habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_09", Conf.idioma),
				false);
		habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_10", Conf.idioma),
				false);
		habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_16", Conf.idioma),
				false);
		habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_11", Conf.idioma),
				false);
		habilitaMenuItem(menus[0], Texto.get("MENU_ARCH_12", Conf.idioma),
				false);

		// Menú Visualización
		String noAnimacion[] = { Texto.get("MENU_VISU_19", Conf.idioma),
				Texto.get("MENU_VISU_18", Conf.idioma),
				Texto.get("MENU_VISU_10", Conf.idioma),
				Texto.get("MENU_VISU_11", Conf.idioma),
				Texto.get("MENU_VISU_12", Conf.idioma),
				Texto.get("BARRA_HERR_TTT35", Conf.idioma),
				Texto.get("BARRA_HERR_TTT36_OPEN", Conf.idioma)};

		char mnAnimacion[] = new char[noAnimacion.length];
		mnemotecnicos = Texto.get("MNEMO_VISU", Conf.idioma);
		for (int i = 0; i < mnAnimacion.length; i++) {
			mnAnimacion[i] = mnemotecnicos.charAt(i);
		}

		String nombreMenuAnimacion = Texto.get("MENU_VISU_N", Conf.idioma);
		char mnMenuAnimacion = mnemotecnicos.charAt(mnemotecnicos.length() - 1);

		URL iconosAnimacion[] = {
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_idMetodo_des.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_estadoInicial.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_formato.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_zoom.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_ubicacionpaneles.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_generargrafodependencia.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/ter_terminal_activar.png")};
		int sepAnimacion[] = {}; // Separador debajo de la primera opción de
		// menú (contamos desde cero)
		KeyStroke ksAnimacion[] = {
				null,
				null,
				null,
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
						java.awt.Event.CTRL_MASK),
						KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
								java.awt.Event.CTRL_MASK),
				null,
				null};
		menus[1] = creaMenu(noAnimacion, mnAnimacion, iconosAnimacion,
				nombreMenuAnimacion, mnMenuAnimacion, sepAnimacion, ksAnimacion);

		habilitaMenuItem(menus[1], Texto.get("MENU_VISU_11", Conf.idioma),
				false);
		habilitaMenuItem(menus[1], Texto.get("MENU_VISU_12", Conf.idioma),
				false);
		habilitaMenuItem(menus[1], Texto.get("BARRA_HERR_TTT35", Conf.idioma),
				false);

		// Menú Filtrado y Selección
		String noFiltrado[] = { Texto.get("MENU_FILT_00", Conf.idioma),
				Texto.get("MENU_FILT_01", Conf.idioma),
				Texto.get("MENU_FILT_02", Conf.idioma),
				Texto.get("MENU_FILT_03", Conf.idioma),
				Texto.get("MENU_FILT_04", Conf.idioma),
				Texto.get("MENU_FILT_05", Conf.idioma), };

		char mnFiltrado[] = new char[noFiltrado.length];
		mnemotecnicos = Texto.get("MNEMO_FILT", Conf.idioma);
		for (int i = 0; i < mnFiltrado.length; i++) {
			mnFiltrado[i] = mnemotecnicos.charAt(i);
		}

		String nombreMenuFiltrado = Texto.get("MENU_FILT_N", Conf.idioma);
		char mnMenuFiltrado = mnemotecnicos.charAt(mnemotecnicos.length() - 1);

		URL iconosFiltrado[] = {
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_entradasalida.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_vermetodosparam.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_nodoshistoricos.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_mostrarsubarbol_des.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_llamadasmarcar.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_llamadasdesmarcar.gif")};
		int sepFiltrado[] = { 3 }; // Separador debajo de la primera opción de
		// menú (contamos desde cero)
		KeyStroke ksFiltrado[] = { null, null, null, null, null, null };
		menus[5] = creaMenu(noFiltrado, mnFiltrado, iconosFiltrado,
				nombreMenuFiltrado, mnMenuFiltrado, sepFiltrado, ksFiltrado);

		habilitaMenuItem(
				menus[5],
				Texto.get("MENU_FILT_01", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""), false);
		habilitaMenuItem(menus[5], Texto.get("MENU_FILT_04", Conf.idioma),
				false);
		habilitaMenuItem(menus[5], Texto.get("MENU_FILT_05", Conf.idioma),
				false);

		// Menú Árbol de Recursión
		String noArbol[] = { Texto.get("MENU_ARBL_00", Conf.idioma),
				Texto.get("MENU_ARBL_04", Conf.idioma),
				Texto.get("MENU_ARBL_01", Conf.idioma),
				Texto.get("MENU_ARBL_03", Conf.idioma),
				Texto.get("MENU_ARBL_02", Conf.idioma), };

		char mnArbol[] = new char[noArbol.length];
		mnemotecnicos = Texto.get("MNEMO_ARBL", Conf.idioma);
		for (int i = 0; i < mnArbol.length; i++) {
			mnArbol[i] = mnemotecnicos.charAt(i);
		}

		String nombreMenuArbol = Texto.get("MENU_ARBL_N", Conf.idioma);
		char mnMenuArbol = mnemotecnicos.charAt(mnemotecnicos.length() - 1);

		URL iconosArbol[] = {
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_arbolcolapsado.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_visualizacionDinamica.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_mostrarvisor.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_ajustarVisor.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_estructuraarbol.gif") };
		int sepArbol[] = {}; // Separador debajo de la primera opción de menú
		// (contamos desde cero)
		KeyStroke ksArbol[] = { null, null, null, null, null };
		menus[6] = creaMenu(noArbol, mnArbol, iconosArbol, nombreMenuArbol,
				mnMenuArbol, sepArbol, ksArbol);

		// Menú Traza
		String noTraza[] = { Texto.get("MENU_TRAZ_00", Conf.idioma),
				Texto.get("MENU_TRAZ_02", Conf.idioma),
				Texto.get("MENU_TRAZ_03", Conf.idioma),
				Texto.get("MENU_TRAZ_04", Conf.idioma), };

		char mnTraza[] = new char[noTraza.length];
		mnemotecnicos = Texto.get("MNEMO_TRAZ", Conf.idioma);
		for (int i = 0; i < mnTraza.length; i++) {
			mnTraza[i] = mnemotecnicos.charAt(i);
		}

		String nombreMenuTraza = Texto.get("MENU_TRAZ_N", Conf.idioma);
		char mnMenuTraza = mnemotecnicos.charAt(mnemotecnicos.length() - 1);

		URL iconosTraza[] = {
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_sangrado.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_ligarescrono.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_soloestructuraprincipal.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_estructcompletacrono_des.gif") };
		int sepTraza[] = {}; // Separador debajo de la primera opción de menú
		// (contamos desde cero)
		KeyStroke ksTraza[] = { null, null, null, null };
		menus[7] = creaMenu(noTraza, mnTraza, iconosTraza, nombreMenuTraza,
				mnMenuTraza, sepTraza, ksTraza);

		// Menú Información
		String noInformacion[] = { Texto.get("MENU_INFO_01", Conf.idioma), // Info
				// animación
				Texto.get("MENU_INFO_02", Conf.idioma), // Info nodo activo
		};

		char mnInformacion[] = new char[noInformacion.length];
		mnemotecnicos = Texto.get("MNEMO_INFOR", Conf.idioma);
		for (int i = 0; i < mnInformacion.length; i++) {
			mnInformacion[i] = mnemotecnicos.charAt(i);
		}

		String nombreMenuInformacion = Texto.get("MENU_INFO_N", Conf.idioma);
		char mnMenuInformacion = mnemotecnicos
				.charAt(mnemotecnicos.length() - 1);
		URL iconosInformacion[] = {
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_infovisualizacion.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_infonodo.gif") };
		int sepInformacion[] = {}; // Separador debajo de la primera opción de
		// menú (contamos desde cero)
		KeyStroke ksInformacion[] = { null, null, };
		menus[2] = creaMenu(noInformacion, mnInformacion, iconosInformacion,
				nombreMenuInformacion, mnMenuInformacion, sepInformacion,
				ksInformacion);

		habilitaMenuItem(menus[2], Texto.get("MENU_INFO_01", Conf.idioma),
				false);
		habilitaMenuItem(menus[2], Texto.get("MENU_INFO_02", Conf.idioma),
				false);

		// Menú Configuración
		String noConfiguracion[] = { Texto.get("MENU_VISU_13", Conf.idioma),
				Texto.get("MENU_VISU_14", Conf.idioma),
				Texto.get("MENU_VISU_15", Conf.idioma),
				Texto.get("MENU_VISU_16", Conf.idioma),
				Texto.get("MENU_CONF_05", Conf.idioma), // Archivo LOG
				Texto.get("MENU_CONF_01", Conf.idioma), // Cuadro archivos
				// intermedios
				Texto.get("MENU_CONF_02", Conf.idioma), // Cuadro MV Java
				Texto.get("MENU_CONF_03", Conf.idioma), // Cuadro idioma
				Texto.get("MENU_CONF_04", Conf.idioma), // Opción toggle ver
				// barra herramientas
		};

		char mnConfiguracion[] = new char[noConfiguracion.length];// {'V','O','F','Z','A','M','I','h','R','C','G','D'};
		mnemotecnicos = Texto.get("MNEMO_CONFIG", Conf.idioma);
		for (int i = 0; i < mnConfiguracion.length; i++) {
			mnConfiguracion[i] = mnemotecnicos.charAt(i);
		}

		String nombreMenuConfiguracion = Texto.get("MENU_CONF_N", Conf.idioma);
		char mnMenuConfiguracion = mnemotecnicos
				.charAt(mnemotecnicos.length() - 1);
		URL iconosConfiguracion[] = {
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_vacio.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_vacio.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_vacio.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_vacio.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_vacio.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_archivosintermedios.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_mvjava.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_idioma.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_verbarrahh.gif") };
		int sepConfiguracion[] = { 3, 6 }; // Separador debajo de la primera
		// opción de menú (contamos desde
		// cero)
		KeyStroke ksConfiguracion[] = {
				null,
				null,
				null,
				null,
				null,
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I,
						java.awt.Event.CTRL_MASK),
						KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J,
								java.awt.Event.CTRL_MASK),
								KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H,
										java.awt.Event.CTRL_MASK),
										KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D,
												java.awt.Event.CTRL_MASK), };
		menus[3] = creaMenu(noConfiguracion, mnConfiguracion,
				iconosConfiguracion, nombreMenuConfiguracion,
				mnMenuConfiguracion, sepConfiguracion, ksConfiguracion);

		habilitaMenuItem(menus[3], Texto.get("MENU_VISU_03", Conf.idioma),
				false);
		habilitaMenuItem(menus[3], Texto.get("MENU_VISU_12", Conf.idioma),
				false);

		// Menú Ayuda
		String noAyuda[] = { Texto.get("MENU_AYUD_01", Conf.idioma),
				Texto.get("MENU_AYUD_02", Conf.idioma) };
		char mnAyuda[] = new char[noAyuda.length];// ={'T','S'};
		mnemotecnicos = Texto.get("MNEMO_AYUDA", Conf.idioma);
		for (int i = 0; i < mnAyuda.length; i++) {
			mnAyuda[i] = mnemotecnicos.charAt(i);
		}

		String nombreMenuAyuda = Texto.get("MENU_AYUD_N", Conf.idioma);
		char mnMenuAyuda = mnemotecnicos.charAt(mnemotecnicos.length() - 1);
		URL iconosAyuda[] = {
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_srecayuda.gif"),
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_srec.gif") };
		KeyStroke ksAyuda[] = {
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0),
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,
						java.awt.Event.CTRL_MASK) };
		menus[4] = creaMenu(noAyuda, mnAyuda, iconosAyuda, nombreMenuAyuda,
				mnMenuAyuda, null, ksAyuda);

		activaChecks(menus);

		// Adición de menús.
		barramenu.add(menus[0]);
		barramenu.add(menus[1]);
		barramenu.add(menus[5]);
		barramenu.add(menus[6]);
		barramenu.add(menus[7]);
		barramenu.add(menus[2]);
		barramenu.add(menus[3]);
		barramenu.add(menus[4]);
	}

	/**
	 * Crea un menú, que será añadido a la barra de menús de la ventana de la
	 * aplicación.
	 * 
	 * @param nombreOpciones
	 *            Listado con los textos que aparecerán en el menú para cada
	 *            opción.
	 * @param mnemotecOpciones
	 *            Listado de letras que servirán de mnemotécnicos para cada
	 *            opción.
	 * @param iconos
	 *            Ubicación de los iconos para cada opción.
	 * @param nombreMenu
	 *            Nombre del menú que se va a generar, será lo que aparezca en
	 *            la barra de menús.
	 * @param mnemotecMenu
	 *            Letra que servirá de mnemotécnico al menú.
	 * @param separadores
	 *            Array con el número de posiciones donde deben ir colocados los
	 *            separadores entre las opciones.
	 * @param ks
	 *            Accesos de teclado para cada opción.
	 */
	private static JMenu creaMenu(String nombreOpciones[],
			char mnemotecOpciones[], URL iconos[], String nombreMenu,
			char mnemotecMenu, int separadores[], KeyStroke ks[]) {
		JMenuItem items[] = new JMenuItem[nombreOpciones.length];

		JMenu menu = null;

		try {

			int i = 0, indice_opciones = 0, ultimoSubmenu = -1;
			int separadorlista = -1;
			int longitud = items.length;
			while (indice_opciones < longitud) {
				// si es un item con check (toogle)
				if (nombreOpciones[indice_opciones]
						.contains("_CheckBoxMenuItem_")
						&& !nombreOpciones[indice_opciones]
								.contains("_SubMenuItem_")) {
					items[i] = new JCheckBoxMenuItem(
							nombreOpciones[indice_opciones].replace(
									"_CheckBoxMenuItem_", ""));
					items[i].setMnemonic(new Character(
							mnemotecOpciones[indice_opciones]));
					if (iconos[i] != null) {
						items[i].setIcon(new ImageIcon(iconos[i]));
					}
					items[i].addActionListener(Ventana.thisventana);
					if (ks != null && ks[i] != null) {
						items[i].setAccelerator(ks[i]);
					}
				}
				// si es un submenu
				else if (nombreOpciones[indice_opciones].contains("_SubMenu_")) {
					items[i] = new JMenu(
							nombreOpciones[indice_opciones].replace(
									"_SubMenu_", ""));
					items[i].setMnemonic(new Character(
							mnemotecOpciones[indice_opciones]));
					ultimoSubmenu = i;
					if (iconos[i] != null) {
						items[i].setIcon(new ImageIcon(iconos[i]));
					}
					items[i].addActionListener(Ventana.thisventana);
					if (ks != null && ks[i] != null) {
						items[i].setAccelerator(ks[i]);
					}
				}
				// si es un elemento del último submenú encontrado
				else if (nombreOpciones[indice_opciones]
						.contains("_SubMenuItem_")) {
					if (!nombreOpciones[indice_opciones]
							.contains("_CheckBoxMenuItem_")) {
						JMenuItem smi = new JMenuItem(
								nombreOpciones[indice_opciones].replace(
										"_SubMenuItem_", ""));
						if (iconos[i] != null) {
							smi.setIcon(new ImageIcon(iconos[i]));
						}
						smi.addActionListener(Ventana.thisventana);
						if (ks != null && ks[i] != null) {
							smi.setAccelerator(ks[i]);
						}
						if (ultimoSubmenu > -1) {
							((JMenu) items[ultimoSubmenu]).add(smi);
						}
					} else {
						JMenuItem smi = new JCheckBoxMenuItem(
								nombreOpciones[indice_opciones].replace(
										"_SubMenuItem_", "").replace(
												"_CheckBoxMenuItem_", ""));
						if (iconos[i] != null) {
							smi.setIcon(new ImageIcon(iconos[i]));
						}
						smi.addActionListener(Ventana.thisventana);
						if (ks != null && ks[i] != null) {
							smi.setAccelerator(ks[i]);
						}
						if (ultimoSubmenu > -1) {
							((JMenu) items[ultimoSubmenu]).add(smi);
						}
					}

				}
				// si es un elemento normal
				else {
					items[i] = new JMenuItem(nombreOpciones[indice_opciones],
							new Character(mnemotecOpciones[indice_opciones]));
					if (iconos[i] != null) {
						items[i].setIcon(new ImageIcon(iconos[i]));
					}
					items[i].addActionListener(Ventana.thisventana);
					if (ks != null && ks[i] != null) {
						items[i].setAccelerator(ks[i]);
					}
				}
				i++;
				indice_opciones++;
			}

			menu = new JMenu(nombreMenu);
			menu.setMnemonic(mnemotecMenu);

			for (i = 0; i < items.length; i++) {
				if (items[i] != null) {
					menu.add(items[i]);
				}
				if ((separadores != null && contains(separadores, i))
						|| separadorlista == i) {
					menu.addSeparator();
				}
			}

		} catch (Exception e) {
			System.out
			.println("GestorVentanaSRec creaMenu ha ocasionado un problema");
			e.printStackTrace();
		}
		return menu;
	}

	/**
	 * Determina el estado (Habilitado, o deshabilitado) de todos los elementos
	 * del menú.
	 * 
	 * @param barramenu
	 *            Barra de menú.
	 * 
	 * @return Matriz con los valores para cada elemento del menú.
	 */
	private static boolean[][] getEstadoVisibilidadMenu(JMenuBar barramenu) {
		if (barramenu != null) {
			boolean[][] estado = new boolean[barramenu.getMenuCount()][]; // Hay
			// 3
			// menús

			for (int i = 0; i < barramenu.getMenuCount(); i++) {
				JMenu menu = barramenu.getMenu(i);
				int numComponentes = menu.getItemCount();

				estado[i] = new boolean[numComponentes];

				for (int j = 0; j < estado[i].length; j++) {
					if (menu.getItem(j) == null) {
						j++;
					}
					estado[i][j] = menu.getItem(j).isEnabled();
				}
			}
			return estado;
		} else {
			return null;
		}
	}

	/**
	 * Establece el estado (Habilitado, o deshabilitado) de todos los elementos
	 * del menú.
	 * 
	 * @param barramenu
	 *            Barra de menú.
	 * @param estado
	 *            Matriz de estado para todos los elementos del menú.
	 */
	private static void setEstadoVisibilidadMenu(JMenuBar barramenu,
			boolean[][] estado) {
		try {

			for (int i = 0; i < barramenu.getMenuCount(); i++) {
				JMenu menu = barramenu.getMenu(i);

				for (int j = 0; j < estado[i].length; j++) {
					if (menu.getItem(j) == null) {
						j++;
					}
					menu.getItem(j).setEnabled(estado[i][j]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Establece los distintos valores de selección para todos los elementos del
	 * menú, según la configuración actualmente establecida.
	 * 
	 * @param menus
	 *            Lista de submenús.
	 */
	public static void activaChecks(JMenu[] menus) {
		OpcionOpsVisualizacion oov = null;
		GestorOpciones gOpciones = new GestorOpciones();

		oov = (OpcionOpsVisualizacion) gOpciones.getOpcion(
				"OpcionOpsVisualizacion", false);

		activaCheck(
				menus[1],
				Texto.get("MENU_VISU_05", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getMostrarArbolSalto());
		activaCheck(
				menus[5],
				Texto.get("MENU_FILT_03", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getMostrarArbolSalto());
		activaCheck(
				menus[1],
				Texto.get("MENU_VISU_06", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getMostrarVisor());
		activaCheck(
				menus[6],
				Texto.get("MENU_ARBL_01", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getMostrarVisor());
		activaCheck(
				menus[1],
				Texto.get("MENU_VISU_07", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getMostrarEstructuraEnArbol());
		activaCheck(
				menus[6],
				Texto.get("MENU_ARBL_02", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getMostrarEstructuraEnArbol());
		activaCheck(
				menus[6],
				Texto.get("MENU_ARBL_03", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getAjustarVistaGlobal());
		activaCheck(
				menus[6],
				Texto.get("MENU_ARBL_04", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getVisualizacionDinamica());
		activaCheck(
				menus[1],
				Texto.get("MENU_VISU_08", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getMostrarEstructuraCompletaCrono());

		activaCheck(
				menus[1],
				Texto.get("MENU_VISU_09", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getMostrarSalidaLigadaEntrada());

		activaCheck(
				menus[1],
				Texto.get("MENU_VISU_17", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getMostrarArbolColapsado());
		activaCheck(
				menus[1],
				Texto.get("MENU_VISU_18", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getArranqueEstadoInicial());
		activaCheck(
				menus[6],
				Texto.get("MENU_ARBL_00", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getMostrarArbolColapsado());

		activaCheck(
				menus[7],
				Texto.get("MENU_TRAZ_00", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""), oov.getSangrado());

		activaCheck(
				menus[1],
				Texto.get("MENU_VISU_19", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getIdMetodoTraza());

		activaCheck(
				menus[7],
				Texto.get("MENU_TRAZ_02", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				!oov.getMostrarSalidaLigadaEntrada());

		activaCheck(
				menus[7],
				Texto.get("MENU_TRAZ_03", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getSoloEstructuraDYVcrono());

		activaCheck(
				menus[7],
				Texto.get("MENU_TRAZ_04", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""),
				oov.getMostrarEstructuraCompletaCrono());

		//

		OpcionBorradoFicheros obf = null;
		gOpciones = new GestorOpciones();

		obf = (OpcionBorradoFicheros) gOpciones.getOpcion(
				"OpcionBorradoFicheros", false);

		activaCheck(
				menus[3],
				Texto.get("MENU_CONF_04", Conf.idioma).replace(
						"_CheckBoxMenuItem_", ""), true);
		activaCheck(
				menus[3],
				Texto.get("MENU_CONF_05", Conf.idioma)
				.replace("_SubMenuItem_", "")
				.replace("_CheckBoxMenuItem_", ""), obf.getLOG());
	}

	/**
	 * Establece el valor de selección para un elemento del menú.
	 * 
	 * @param menu
	 *            Menú correspondiente.
	 * @param nombreElemento
	 *            Nombre del elemento dentro del menú.
	 * @param valor
	 *            True para seleccionado, false para no seleccionado.
	 */
	private static void activaCheck(JMenu menu, String nombreElemento,
			boolean valor) {
		JMenuItem items[] = new JMenuItem[menu.getItemCount()];

		for (int i = 0; i < items.length; i++) {
			items[i] = menu.getItem(i);
		}

		for (int i = 0; i < items.length; i++) {
			if (items[i] instanceof JCheckBoxMenuItem
					&& ((JCheckBoxMenuItem) items[i]).getText().equals(
							nombreElemento)) {
				((JCheckBoxMenuItem) items[i]).setState(valor);
			} else if (items[i] instanceof JMenu) {
				activaCheck((JMenu) items[i], nombreElemento, valor);
			}
		}

	}

	/**
	 * Establece el valor de habilitado/Deshabilitado para un elemento del menú.
	 * 
	 * @param menu
	 *            Menú correspondiente.
	 * @param nombreElemento
	 *            Nombre del elemento dentro del menú.
	 * @param valor
	 *            True para habilitado, false para deshabilitado.
	 */
	protected static void habilitaMenuItem(JMenu menu, String nombreElemento,
			boolean valor) {
		JMenuItem items[] = new JMenuItem[menu.getItemCount()];

		for (int i = 0; i < items.length; i++) {
			items[i] = menu.getItem(i);
		}

		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getText().equals(nombreElemento)) {
				items[i].setEnabled(valor);
			} else if (items[i] != null && items[i] instanceof JMenu) {
				habilitaMenuItem((JMenu) items[i], nombreElemento, valor);
			}
		}
	}

	/**
	 * Establece el icono de un elemento del menú.
	 * 
	 * @param menu
	 *            Menú correspondiente.
	 * @param nombreElemento
	 *            Nombre del elemento dentro del menú.
	 * @param path
	 *            Path del icono para el elemento.
	 */
	protected static void iconoMenuItem(JMenu menu, String nombreElemento,
			URL path) {
		JMenuItem items[] = new JMenuItem[menu.getItemCount()];

		for (int i = 0; i < items.length; i++) {
			items[i] = menu.getItem(i);
		}

		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getText().equals(nombreElemento)) {
				items[i].setIcon(new ImageIcon(path));
			} else if (items[i] != null && items[i] instanceof JMenu) {
				iconoMenuItem((JMenu) items[i], nombreElemento, path);
			}
		}
	}
	
	/**
	 * Establece un elemento del menú como seleccionado
	 * (Método principalmente para Linux)
	 * 
	 * @param menu
	 *      Menú correspondiente.
	 * @param nombreElemento
	 *      Nombre del elemento dentro del menú.
	 * @param path
	 *      Path del icono para el elemento.
	 * @param pulsado
	 * 		Indica si el elemento está pulsado o no
	 */
	protected static void setPulsado(JMenu menu, String nombreElemento,
			URL path, boolean pulsado) {
		JMenuItem items[] = new JMenuItem[menu.getItemCount()];

		for (int i = 0; i < items.length; i++) {
			items[i] = menu.getItem(i);
		}

		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getText().equals(nombreElemento)) {
				items[i].setSelected(pulsado);;
			} else if (items[i] != null && items[i] instanceof JMenu) {
				setPulsado((JMenu) items[i], nombreElemento, path, pulsado);
			}
		}
	}

	/**
	 * Crea las distintas barras de herramientas de la aplicación.
	 * 
	 * @return Barras de herramientas de la aplicación.
	 */
	protected static JToolBar[] creaBarrasHeramientas() {
		// Primero creamos los botones
		JButton[] botones = new JButton[37];

		// Grupo archivo Java
		botones[0] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/i_nuevaclase.gif")));
		botones[1] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/i_abrirclase.gif")));
		botones[2] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/i_guardarclase.gif")));
		botones[3] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/i_compilarclase.gif")));

		// Grupo animación
		botones[28] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_seleccionmetodo.gif")));
		botones[29] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_nuevavisualizacion.gif")));
        botones[35]=new JButton(new ImageIcon(
        		GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_generargrafodependencia.gif"))); 		
    
		// Grupo animación - No visibles
		botones[4] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_nuevavisualizacion.gif")));
		botones[5] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_abrirvisualizacion.gif")));
		botones[6] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_guardarvisualizacion.gif")));

		// Grupo exportación
		botones[7] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_exportaranimacion.gif")));
		botones[9] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_exportarestado.gif")));
		// Grupo exportación - No visibles
		botones[8] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_exportarestados.gif")));

		// Grupo opciones 1
		botones[10] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_entradasalida.gif")));
		botones[11] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_vermetodosparam.gif")));
		botones[30] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_llamadasmarcar.gif")));
		botones[31] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_llamadasdesmarcar.gif")));
		botones[36] = new JButton(new ImageIcon(
 				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/ter_terminal_activar.png")));
		
		// Grupo opciones 1 - No Visibles
		botones[12] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_nodoshistoricos.gif")));
		botones[13] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_mostrarsubarbol.gif")));

		// Grupo opciones 2
		botones[14] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_mostrarvisor.gif")));
		botones[32] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_ligarescrono.gif")));
		botones[33] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_estructuraarbol.gif")));
		botones[34] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_infovisualizacion.gif")));

		// Grupo opciones 2 - No Visibles
		botones[15] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_estructuraarbol.gif")));
		botones[16] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_estructcompletacrono.gif")));
		botones[17] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_ligarescrono.gif")));
		botones[18] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_arbolcolapsado.gif")));

		// Grupo formato
		botones[19] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_formato.gif")));
		botones[20] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_zoom.gif")));
		botones[21] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_zoommas1.gif")));
		botones[22] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_zoommenos1.gif")));
		botones[23] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_zoomajuste1.gif")));
		botones[24] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_zoommas2.gif")));
		botones[25] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_zoommenos2.gif")));
		botones[26] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_zoomajuste2.gif")));
		
		// Grupo formato - No Visibles
		botones[27] = new JButton(new ImageIcon(
				GestorVentanaSRec.class.getClassLoader().getResource("imagenes/i_ubicacionpaneles.gif")));
		
		GestorVentanaSRec.setToolTipTextBHH(botones);

		for (int i = 0; i < botones.length; i++) {
			botones[i].addActionListener(Ventana.thisventana);
			botones[i].setFocusable(false);
		}

		botones[2].setEnabled(false);
		botones[3].setEnabled(false);
		botones[4].setEnabled(false);
		botones[28].setEnabled(false);
		botones[29].setEnabled(false);
		botones[33].setEnabled(false);
		botones[35].setEnabled(false);		
		
		Ventana.thisventana.setBotones(botones);
		Ventana.thisventana.habilitarOpcionesAnimacion(false);
		
		//	Terminal - Necesario, sino no funciona bien
		botones[36].setEnabled(true);
		botones[36].addActionListener(new ActionListener() { 		
			  public void actionPerformed(ActionEvent e) { 
				    Ventana.thisventana.terminalAbrirCerrar();
			  } 
		} );
		
		// Creamos las barras de herramientas donde insertaremos los botones
		// creados
		JToolBar[] barras = new JToolBar[6];
		for (int i = 0; i < barras.length; i++) {
			barras[i] = new JToolBar(Texto.get("BARRA_HERR", Conf.idioma));
			barras[i].setBorderPainted(true);
			barras[i].setFloatable(false);

			barras[i].setBorder(new MetalBorders.PaletteBorder());
		}

		// Grupo archivo java
		for (int i = 0; i < 4; i++) {
			barras[0].add(botones[i]);
		}

		// Grupo animación
		for (int i = 28; i < 30; i++) {
			barras[1].add(botones[i]);
		}
        barras[1].add(botones[35]);       
        
		// Grupo exportación
		barras[2].add(botones[7]);
		barras[2].add(botones[9]);

		// Grupo de opciones 1
		for (int i = 10; i < 12; i++) {
			barras[3].add(botones[i]);
		}
		barras[3].add(botones[30]);
		barras[3].add(botones[31]);
		barras[3].add(botones[36]);

		// Grupo de opciones 2
		barras[4].add(botones[14]);
		barras[4].add(botones[33]);
		barras[4].add(botones[32]);
		barras[4].add(botones[34]);

		// Grupo de Formato
		for (int i = 19; i < 27; i++) {
			barras[5].add(botones[i]);
		}

		return barras;
	}

	/**
	 * Establece el tooltip de todos los botones según el idioma configurado.
	 * 
	 * @param botones
	 *            Botones.
	 */
	protected static void setToolTipTextBHH(JButton[] botones) {
		// Clases
		botones[0].setToolTipText(Texto.get("BARRA_HERR_TTT00", Conf.idioma));
		botones[1].setToolTipText(Texto.get("BARRA_HERR_TTT01", Conf.idioma));
		botones[2].setToolTipText(Texto.get("BARRA_HERR_TTT02", Conf.idioma));
		botones[3].setToolTipText(Texto.get("BARRA_HERR_TTT03", Conf.idioma));

		// Animaciones
		botones[28].setToolTipText(Texto.get("BARRA_HERR_TTT26", Conf.idioma));
		botones[29].setToolTipText(Texto.get("BARRA_HERR_TTT27", Conf.idioma));
        botones[35].setToolTipText(Texto.get("BARRA_HERR_TTT35",Conf.idioma));        
		botones[4].setToolTipText(Texto.get("BARRA_HERR_TTT05", Conf.idioma));
		botones[5].setToolTipText(Texto.get("BARRA_HERR_TTT06", Conf.idioma));
		botones[6].setToolTipText(Texto.get("BARRA_HERR_TTT07", Conf.idioma));

		// Exportación
		botones[7].setToolTipText(Texto.get("BARRA_HERR_TTT08", Conf.idioma));
		botones[9].setToolTipText(Texto.get("BARRA_HERR_TTT10", Conf.idioma));
		botones[8].setToolTipText(Texto.get("BARRA_HERR_TTT09", Conf.idioma));

		// Opciones 1
		botones[10].setToolTipText(Texto.get("BARRA_HERR_TTT11", Conf.idioma));
		botones[11].setToolTipText(Texto.get("BARRA_HERR_TTT12", Conf.idioma));
		botones[30].setToolTipText(Texto.get("BARRA_HERR_TTT28", Conf.idioma));
		botones[31].setToolTipText(Texto.get("BARRA_HERR_TTT29", Conf.idioma));
		botones[12].setToolTipText(Texto.get("BARRA_HERR_TTT13", Conf.idioma));
		botones[13].setToolTipText(Texto.get("BARRA_HERR_TTT14", Conf.idioma));
		botones[36].setToolTipText(Texto.get("BARRA_HERR_TTT36_OPEN",Conf.idioma));

		// Opciones 2
		botones[14].setToolTipText(Texto.get("BARRA_HERR_TTT15", Conf.idioma));
		botones[32].setToolTipText(Texto.get("BARRA_HERR_TTT30", Conf.idioma));
		botones[33].setToolTipText(Texto.get("BARRA_HERR_TTT31", Conf.idioma));
		botones[34].setToolTipText(Texto.get("BARRA_HERR_TTT32", Conf.idioma));
		botones[15].setToolTipText(Texto.get("BARRA_HERR_TTT16", Conf.idioma));
		botones[16].setToolTipText(Texto.get("BARRA_HERR_TTT17", Conf.idioma));
		botones[17].setToolTipText(Texto.get("BARRA_HERR_TTT18", Conf.idioma));
		botones[18].setToolTipText(Texto.get("BARRA_HERR_TTT19", Conf.idioma));

		// Formato y zoom
		botones[19].setToolTipText(Texto.get("BARRA_HERR_TTT20", Conf.idioma));
		botones[20].setToolTipText(Texto.get("BARRA_HERR_TTT21", Conf.idioma));
		botones[21].setToolTipText(Texto.get("BARRA_HERR_TTT22", Conf.idioma));
		botones[22].setToolTipText(Texto.get("BARRA_HERR_TTT23", Conf.idioma));
		botones[23].setToolTipText(Texto.get("BARRA_HERR_TTT24", Conf.idioma));
		botones[24].setToolTipText(Texto.get("BARRA_HERR_TTT22", Conf.idioma));
		botones[25].setToolTipText(Texto.get("BARRA_HERR_TTT23", Conf.idioma));
		botones[26].setToolTipText(Texto.get("BARRA_HERR_TTT24", Conf.idioma));

		// Idioma
		botones[27].setToolTipText(Texto.get("BARRA_HERR_TTT25", Conf.idioma));
	}

	/**
	 * Muestra u oculta las barras de herramientas.
	 * 
	 * @param barrasHerramientas
	 *            Barras de herramientas de la aplicación.
	 * @param valor
	 *            True para mostrar, false para ocultar.
	 * @param actualizarEstado
	 *            Actualiza la información de la ventana sobre la visibilidad de
	 *            la barra de herramientas.
	 */
	protected static void setVisibleBarraHerramientas(
			JToolBar[] barrasHerramientas, boolean valor,
			boolean actualizarEstado) {
		for (int i = 0; i < barrasHerramientas.length; i++) {
			barrasHerramientas[i].setVisible(valor);
			if (i == 0 && actualizarEstado) {
				Ventana.thisventana.barraHerramientasVisible = barrasHerramientas[i]
						.isVisible();
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {

		if (Ventana.thisventana.getClasePendienteGuardar()) {
			new CuadroPreguntaEdicionNoGuardada(Ventana.thisventana,
					"cierreVentana");
		} else {
			Ventana.thisventana.activarCierre();
		}

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowStateChanged(WindowEvent arg0) {

	}

	/**
	 * Método auxiliar que revisa si en un array de enteros se encuentra un
	 * determinado número.
	 * 
	 * @param x
	 *            Array de números enteros
	 * @param y
	 *            Número entero que se desea buscar
	 * 
	 * @return true si se encontró el número y en el array x.
	 */
	private static boolean contains(int x[], int y) {
		for (int i = 0; i < x.length; i++) {
			if (x[i] == y) {
				return true;
			}
		}
		return false;
	}

}
