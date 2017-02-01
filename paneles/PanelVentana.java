package paneles;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import utilidades.Arrays;
import ventanas.Ventana;
import datos.DatosMetodoBasicos;
import datos.MetodoAlgoritmo;
import datos.Traza;

/**
 * Panel que contendrá los paneles de visualización. Permanece asociado en todo
 * momento a la ventana de la aplicación, no desaparece nunca.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class PanelVentana extends JPanel {
	static final long serialVersionUID = 14;

	private PanelAlgoritmo pAlgoritmo;

	/**
	 * Constructor: crea un nuevo panel para la ventana.
	 * 
	 */
	public PanelVentana() {
		this.setLayout(new BorderLayout());

		try {
			this.pAlgoritmo = new PanelAlgoritmo();
		} catch (java.lang.Exception exp) {
			exp.printStackTrace();
		}
		this.add(this.pAlgoritmo, BorderLayout.CENTER);

	}

	/**
	 * Actualiza la visualización de los paneles por si ha habido cambios en la
	 * configuración de la aplicación
	 */
	public void refrescarFormato() {
		this.pAlgoritmo.refrescarFormato(true);
	}

	/**
	 * Actualiza la visualización de los paneles por si ha habido cambios en la
	 * configuración de la aplicación
	 */
	public void refrescarOpciones() {
		this.pAlgoritmo.refrescarFormato(false);
	}

	/**
	 * Actualiza el zoom de la visualización
	 * 
	 * @param valor
	 *            Nuevo valor de zoom.
	 */
	public void refrescarZoomArbol(int valor) {
		this.pAlgoritmo.refrescarZoomArbol(valor);
	}

	/**
	 * Actualiza el zoom de la visualización (vista de pila)
	 * 
	 * @param valor
	 *            Nuevo valor de zoom.
	 */
	public void refrescarZoomPila(int valor) {
		this.pAlgoritmo.refrescarZoomPila(valor);
	}
	
	/**
	 * Actualiza el zoom de la visualización (vista de grafo de dependencia)
	 * 
	 * @param valor
	 *            Nuevo valor de zoom.
	 */
	public void refrescarZoomPanelGrafoDep(int valor) {
		this.pAlgoritmo.refrescarZoomGrafoDep(valor);
	}

	/**
	 * Actualiza el zoom de una visualización.
	 * 
	 * @param vista
	 *            1 -> pila, 0 -> arbol, 3 -> crono, 4 -> estructura, 5 -> grafo
	 * @param valor
	 *            nuevo valor de zoom.
	 */
	public void refrescarZoom(int vista, int valor) {
		this.pAlgoritmo.refrescarZoom(vista, valor);
	}

	/**
	 * Determina si está abierto o cerrado el panel de algoritmo (es decir, si
	 * no está visualizando nada en su interior)
	 * 
	 * @return True si no está ocupado, false en caso contrario.
	 */
	public boolean haySitio() {
		return !this.pAlgoritmo.estaOcupado();
	}

	/**
	 * Determina si está abierto o cerrado el panel de algoritmo (es decir, si
	 * no está visualizando nada en su interior)
	 * 
	 * @return True si está ocupado, false en caso contrario.
	 */
	public boolean estaOcupado() {
		return this.pAlgoritmo.estaOcupado();
	}

	/**
	 * Abre el panel del algoritmo, incluido el panel de código.
	 * 
	 * @param traza
	 *            Traza que será visualizada.
	 * @param directorio
	 *            Directorio donde se encuentra la clase.
	 * @param fichero
	 *            Clase con el código fuente para la ejecución.
	 * @param editable
	 *            True si se permite edición, false en caso contrario.
	 */
	public void abrirPanelAlgoritmo(Traza traza, String directorio,
			String fichero, boolean editable) throws Exception {
		try {
			if (directorio != null && fichero != null) {
				this.pAlgoritmo.abrirPanelCodigo(directorio + fichero,
						editable, !editable);
			}
			this.pAlgoritmo.abrirVistas();
		} catch (Exception e) {
			this.pAlgoritmo = null;
			e.printStackTrace();
			throw e;
		}

		this.removeAll();
		this.add(this.pAlgoritmo);
		this.updateUI();
	}
	
    /**
     * Abre los paneles necesarios para visualizar la traza cuando
     * ya se encuentra un algoritmo cargado.
     */
    public void mostrarEjecucionTraza() throws Exception
    {
        try {
            pAlgoritmo.abrirVistas();
        }
        catch (Exception e) {
            pAlgoritmo=null;
            e.printStackTrace();
            throw e;
        }
        this.removeAll();
        this.add(pAlgoritmo);
        this.updateUI();
    }

	/**
	 * Abre el panel del algoritmo.
	 * 
	 * @param fichero
	 *            Clase con el código fuente para la ejecución.
	 */
	public void abrirPanelAlgoritmo(String fichero) {

		try {
			this.pAlgoritmo.abrirVistas(fichero);
		} catch (Exception e) {
			this.pAlgoritmo = null;
			e.printStackTrace();
		}

		this.removeAll();
		this.add(this.pAlgoritmo);
		this.updateUI();
	}

	/**
	 * Abre el panel de código.
	 * 
	 * @param archivo
	 *            Path de la clase con el código fuente para la ejecución.
	 * @param editable
	 *            True si se permite edición, false en caso contrario.
	 * @param cargarFichero
	 *            Si debe leerse el fichero de nuevo.
	 */
	public void abrirPanelCodigo(String archivo, boolean editable,
			boolean cargarFichero) {
		this.pAlgoritmo.cerrarVistas();
		this.pAlgoritmo.abrirPanelCodigo(archivo, editable, cargarFichero);
	}

	/**
	 * Cierra el panel de código.
	 */
	public void cerrarPanelCodigo() {
		this.pAlgoritmo.cerrarPanelCodigo();
	}

	/**
	 * Permite establer el texto del panel del compilador.
	 * 
	 * @param texto Texto que mostrará el panel del compilador.
	 */
	public void setTextoCompilador(String texto) {
		this.pAlgoritmo.setTextoCompilador(texto);
	}

	/**
	 * Permite guardar los cambios realizados en el panel de código.
	 */
	public void guardarClase() {
		this.pAlgoritmo.guardarClase();
	}

	/**
	 * Permite deshabilitar los controles de la animación.
	 */
	public void deshabilitarControles() {
		this.pAlgoritmo.deshabilitarControles();
	}

	/**
	 * Permite habilitar los controles de la animación.
	 */
	public void habilitarControles() {
		this.pAlgoritmo.habilitarControles();
	}

	/**
	 * Devuelve las dimensiones del panel y grafo del árbol.
	 * 
	 * @return {anchura_panel, altura_panel, achura_grafo, altura_grafo}
	 */
	private int[] dimPanelYGrafo() {
		return this.pAlgoritmo.dimPanelYGrafo();
	}

	/**
	 * Devuelve las dimensiones del panel y grafo de la pila.
	 * 
	 * @return {anchura_panel, altura_panel, achura_grafo, altura_grafo}
	 */
	private int[] dimPanelYPila() {
		return this.pAlgoritmo.dimPanelYPila();
	}
	
	/**
	 * Devuelve las dimensiones del panel y grafo de dependencia.
	 * 
	 * @return {anchura_panel, altura_panel, achura_grafo, altura_grafo}
	 */
	private int[] dimPanelYGrafoDep() {
		return this.pAlgoritmo.dimPanelYGrafoDep();
	}

	/**
	 * Devuelve las dimensiones del grafo visible del panel cronológico.
	 * 
	 * @return {anchura, altura}
	 */
	private int[] dimGrafoVisibleCrono() {
		return this.pAlgoritmo.dimGrafoVisibleCrono();
	}

	/**
	 * Devuelve las dimensiones del grafo visible del panel de estructura.
	 * 
	 * @return {anchura, altura}
	 */
	private int[] dimGrafoVisibleEstructura() {
		return this.pAlgoritmo.dimGrafoVisibleEstructura();
	}

	/**
	 * Devuelve las dimensiones del panel y grafo de la vista cronológica.
	 * 
	 * @return {anchura_panel, altura_panel, achura_grafo, altura_grafo}
	 */
	private int[] dimPanelYGrafoCrono() {
		return this.pAlgoritmo.dimPanelYGrafoCrono();
	}

	/**
	 * Devuelve las dimensiones del panel y grafo de la vista de estructura.
	 * 
	 * @return {anchura_panel, altura_panel, achura_grafo, altura_grafo}
	 */
	private int[] dimPanelYGrafoEstructura() {
		return this.pAlgoritmo.dimPanelYGrafoEstructura();
	}

	/**
	 * Devuelve el tamaño de los paneles de árbol, pila, grafo dependencia, crono y estructura
	 * (estos dos últimos, únicamente si la visualización activa es del tipo
	 * DYV).
	 * 
	 * @return {anchura_panel_arbol, altura_panel_arbol, anchura_panel_pila,
	 *         altura_panel_pila, anchura_panel_crono, altura_panel_crono,
	 *         anchura_panel_estructura, altura_panel_estructura, anchura_panel_grafoDependencia,
	 *         altura_panel_grafoDependencia}
	 */
	public int[] getTamanoPaneles() {
		int[] dimensiones = new int[10];

		dimensiones[0] = this.dimPanelYGrafo()[0]; // Árbol
		dimensiones[1] = this.dimPanelYGrafo()[1];
		dimensiones[2] = this.dimPanelYPila()[0]; // Pila
		dimensiones[3] = this.dimPanelYPila()[1];
		if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV, Ventana.thisventana
				.getTraza().getTecnicas())) {
			dimensiones[4] = this.dimPanelYGrafoCrono()[0]; // Crono
			dimensiones[5] = this.dimPanelYGrafoCrono()[1];
			dimensiones[6] = this.dimPanelYGrafoEstructura()[0]; // Estrutura
			dimensiones[7] = this.dimPanelYGrafoEstructura()[1];
		}
		dimensiones[8] = this.dimPanelYGrafoDep()[0];	//	Grafo dependencia
		dimensiones[9] = this.dimPanelYGrafoDep()[1];
		return dimensiones;
	}

	/**
	 * Devuelve el tamaño de los grafos de árbol, pila, grafo de dependencia, crono, estructura
	 * (estos dos últimos, únicamente si la visualización activa es del tipo
	 * DYV).
	 * 
	 * @return {anchura_grafo_arbol, altura_grafo_arbol, anchura_grafo_pila,
	 *         altura_grafo_pila, anchura_grafo_crono, altura_grafo_crono,
	 *         anchura_grafo_estructura, altura_grafo_estructura, anchura_grafo_grafoDependencia,
	 *         altura_grafo_grafoDependencia}
	 */
	public int[] getTamanoGrafos() {
		int[] dimensiones = new int[10];

		dimensiones[0] = this.dimPanelYGrafo()[2]; // Árbol
		dimensiones[1] = this.dimPanelYGrafo()[3];
		dimensiones[2] = this.dimPanelYPila()[2]; // Pila
		dimensiones[3] = this.dimPanelYPila()[3];
		if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV, Ventana.thisventana
				.getTraza().getTecnicas())) {
			dimensiones[4] = this.dimGrafoVisibleCrono()[0]; // Crono
			dimensiones[5] = this.dimGrafoVisibleCrono()[1];
			dimensiones[6] = this.dimGrafoVisibleEstructura()[0]; // Estrutura
			dimensiones[7] = this.dimGrafoVisibleEstructura()[1];
		}
		dimensiones[8] = this.dimPanelYGrafoDep()[2];
		dimensiones[9] = this.dimPanelYGrafoDep()[3];
		return dimensiones;
	}

	/**
	 * Devuelve el título de la animación actual.
	 * 
	 * @return Título de la animación actual.
	 */
	public String getNombresAnimaciones() {
		return this.pAlgoritmo.getTituloPanel();
	}

	/**
	 * Devuelve el identificador de la traza actual.
	 * 
	 * @return Identificador de traza.
	 */
	public String getIdTrazas() {
		return this.pAlgoritmo.getIdTraza();
	}

	/**
	 * Devuelve el nombre de todas las vistas que están disponibles en la
	 * visualización abierta.
	 * 
	 * @return Lista con el nombre de todas las vistas.
	 */
	public String[] getNombreVistasDisponibles() {
		return this.pAlgoritmo.getNombreVistasDisponibles();
	}

	/**
	 * Devuelve el nombre de todas las vistas visibles en la visualización
	 * abierta.
	 * 
	 * @return Lista con el nombre de todas las vistas.
	 */
	public String[] getNombreVistasVisibles() {
		return this.pAlgoritmo.getNombreVistasVisibles();
	}

	/**
	 * Devuelve el código del nombre de las vistas que están visibles en ese
	 * instante (pestañas seleccionadas) en la visualización abierta
	 * 
	 * @return Lista con el código de todas las vistas.
	 */
	public int[] getCodigosVistasVisibles() {
		return this.pAlgoritmo.getCodigoVistasVisibles();
	}

	/**
	 * Establece como vista activa la indicada por parámetro.
	 * 
	 * @param nombre
	 *            Nombre de la vista.
	 */
	public void setVistaActiva(String nombre) {
		this.pAlgoritmo.setVistaActiva(nombre);
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
		return this.pAlgoritmo.getGrafoPorNombre(nombre);
	}

	/**
	 * Devuelve el panel de visualización del algoritmo.
	 * 
	 * @return Panel Algorimto.
	 */
	public PanelAlgoritmo getPanelAlgoritmo() {
		return this.pAlgoritmo;
	}

	/**
	 * Devuelve el valor de zoom para cada uno de los paneles.
	 * 
	 * @return {zoom_arbol, zoom_pila, zoom_crono, zoom_estructura}
	 */
	public int[] getZooms() {
		return this.pAlgoritmo.getZooms();
	}

	/**
	 * Establece el idioma establecido por configuración.
	 */
	public void idioma() {
		this.pAlgoritmo.idioma();
	}

	// Métodos para extracción de dimensiones de paneles y grafos de
	// visualización

	/**
	 * Devuelve las dimensiones del scroll panel de la vista de pila.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimPanelPila() {
		return PanelAlgoritmo.dimPanelPila();
	}

	/**
	 * Devuelve las dimensiones del scroll panel principal.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimPanelPrincipal() {
		return this.pAlgoritmo.dimPanelPrincipal();
	}

	/**
	 * Devuelve las dimensiones del grafo del panel de pila.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoPila() {
		return this.pAlgoritmo.dimGrafoPila();
	}
	
	/**
	 * Devuelve las dimensiones del grafo del panel de grafo de dependencia.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoDep() {
		return this.pAlgoritmo.dimGrafoDep();
	}

	/**
	 * Devuelve las dimensiones del grafo del panel de árbol.
	 * 
	 * @return {anchura, altura}
	 */
	public int[] dimGrafoPrincipal() {
		return this.pAlgoritmo.dimGrafoPrincipal();
	}

	/**
	 * Devuelve las dimensiones del grafo visible del panel de árbol.
	 * 
	 * @return {anchura, altura}
	 * 
	 */
	public int[] dimGrafoVisiblePrincipal() {
		return this.pAlgoritmo.dimGrafoVisiblePrincipal();
	}

	/**
	 * Devuelve el contenedor de la vista del árbol.
	 * 
	 * @return vista de árbol.
	 * 
	 */
	public JComponent getPanelArbol() {
		return this.pAlgoritmo.getPanelArbol();
	}

	/**
	 * Ubica y distribuye los paneles segun la disposición especificada.
	 * 
	 * @param disposicion
	 *            Conf.PANEL_VERTICAL o Conf.PANEL_HORIZONTAL
	 */
	public void ubicarYDistribuirPaneles(int disposicion) {
		this.pAlgoritmo.ubicarVistas();
		this.pAlgoritmo.distribuirPaneles(disposicion);
	}

	/**
	 * Distribuye los paneles segun la disposición especificada.
	 * 
	 * @param disposicion
	 *            Conf.PANEL_VERTICAL o Conf.PANEL_HORIZONTAL
	 */
	public void distribuirPaneles(int disposicion) {
		this.pAlgoritmo.distribuirPaneles(disposicion);
	}

	/**
	 * Permite cerrar las vistas de ejecución del algoritmo.
	 */
	public void cerrarVistas() {
		this.pAlgoritmo.cerrarVistas();
	}

	/**
	 * Actualiza los valores del panel de control.
	 * 
	 * @param tituloPanel
	 *            Título del panel.
	 */
	public void setValoresPanelControl(String tituloPanel) {
		this.pAlgoritmo.setValoresPanelControl(tituloPanel);
	}
	
	/**
	 * 	Permite abrir la pestaña del grafo de dependencia
	 * 
	 * @param metodo
     * 	Método del que queremos generar el grafo de dependencia
	 */
	public void abrirPestanaGrafoDependencia(DatosMetodoBasicos metodo){
		this.pAlgoritmo.vistaGrafoDependenciaVisible(metodo);
	}
	
	/**
	 * 	Permite abrir la pestaña del grafo de dependencia
	 * 
	 * @param metodo
     * 	Lista de métodos de los que queremos generar el grafo de dependencia
	 */
	public void abrirPestanaGrafoDependencia(List<DatosMetodoBasicos> metodo){
		this.pAlgoritmo.vistaGrafoDependenciaVisible(metodo);
	}

}