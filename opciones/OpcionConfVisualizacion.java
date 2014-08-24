package opciones;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.ManipulacionElement;
import conf.Conf;

/**
 * Permite configurar los distintos valores de visualización que se manejan en
 * la aplicación.
 */
public class OpcionConfVisualizacion extends Opcion {

	private static final long serialVersionUID = 1006;

	// Colores de fuente
	private int colorFEntradaR = 255; // Valor R para fuente en celdas de
	// entrada
	private int colorFEntradaG = 255; // Valor G para fuente en celdas de
	// entrada
	private int colorFEntradaB = 255; // Valor B para fuente en celdas de
	// entrada

	private int colorFSalidaR = 255; // Valor R para fuente en celdas de salida
	private int colorFSalidaG = 255; // Valor G para fuente en celdas de salida
	private int colorFSalidaB = 255; // Valor B para fuente en celdas de salida

	// Colores de celda
	private int colorC1EntradaR = 100; // Valor R para color en celdas de
	// entrada
	private int colorC1EntradaG = 100; // Valor G para color en celdas de
	// entrada
	private int colorC1EntradaB = 240; // Valor B para color en celdas de
	// entrada

	private int colorC1SalidaR = 240; // Valor R para color en celdas de salida
	private int colorC1SalidaG = 100; // Valor G para color en celdas de salida
	private int colorC1SalidaB = 100; // Valor B para color en celdas de salida

	// Colores de celda atenuada
	private int colorCAEntradaR = 0; // Valor R para color en celdas de entrada
	private int colorCAEntradaG = 0; // Valor G para color en celdas de entrada
	private int colorCAEntradaB = 140; // Valor B para color en celdas de
	// entrada

	private int colorCASalidaR = 140; // Valor R para color en celdas de salida
	private int colorCASalidaG = 0; // Valor G para color en celdas de salida
	private int colorCASalidaB = 0; // Valor B para color en celdas de salida

	// Colores no calculados salida
	private int colorC1NCSalidaR = 255; // Valor R para color en celdas de
	// entrada
	private int colorC1NCSalidaG = 100; // Valor G para color en celdas de
	// entrada
	private int colorC1NCSalidaB = 100; // Valor B para color en celdas de
	// entrada

	// Color flecha
	private int colorFlechaR = 100; // Valor R para color de flecha
	private int colorFlechaG = 100; // Valor G para color de flecha
	private int colorFlechaB = 100; // Valor B para color de flecha

	// Color panel
	private int colorPanelR = 255; // Valor R para color del panel
	private int colorPanelG = 248; // Valor G para color del panel
	private int colorPanelB = 241; // Valor B para color del panel

	// Color marco nodo actual
	private int colorActualR = 10; // Valor R para color marco nodo actual
	private int colorActualG = 200; // Valor G para color marco nodo actual
	private int colorActualB = 10; // Valor B para color marco nodo actual

	// Color marcos camino actual
	private int colorCActualR = 235; // Valor R para color de marcos camino
	// actual
	private int colorCActualG = 215; // Valor G para color de marcos camino
	// actual
	private int colorCActualB = 0; // Valor B para color de marcos camino actual

	// Color código: palabras reservadas
	private int colorCodigoPRR = 210; // Valor R para color de código: palabras
	// reservadas
	private int colorCodigoPRG = 0; // Valor G para color de código: palabras
	// reservadas
	private int colorCodigoPRB = 0; // Valor B para color de código: palabras
	// reservadas

	// Color código: comentarios
	private int colorCodigoCoR = 0; // Valor R para color de código: comentarios
	private int colorCodigoCoG = 180; // Valor G para color de código:
	// comentarios
	private int colorCodigoCoB = 0; // Valor B para color de código: comentarios

	// Color código: nombre método foreground
	private int colorCodigoMFR = 0; // Valor R para color de código: nombre
	// método foreground
	private int colorCodigoMFG = 0; // Valor G para color de código: nombre
	// método foreground
	private int colorCodigoMFB = 220; // Valor B para color de código: nombre
	// método foreground

	// Color código: nombre método background
	private int colorCodigoMBR = 210; // Valor R para color de código: nombre
	// método background
	private int colorCodigoMBG = 210; // Valor G para color de código: nombre
	// método background
	private int colorCodigoMBB = 255; // Valor B para color de código: nombre
	// método background

	// Color código: resto codigo
	private int colorCodigoRCR = 0; // Valor R para color de código: resto
	// codigo
	private int colorCodigoRCG = 0; // Valor G para color de código: resto
	// codigo
	private int colorCodigoRCB = 0; // Valor B para color de código: resto
	// codigo

	// Color nodos buscados (iluminados)
	private int colorIluminadoR = 200; // Valor R para color nodos buscados
	// (iluminados)
	private int colorIluminadoG = 100; // Valor G para color nodos buscados
	// (iluminados)
	private int colorIluminadoB = 0; // Valor B para color nodos buscados
	// (iluminados)

	// Color nodos resaltados
	private int colorResaltadoR = 204; // Valor R para color nodos buscados
	// (iluminados)
	private int colorResaltadoG = 204; // Valor G para color nodos buscados
	// (iluminados)
	private int colorResaltadoB = 0; // Valor B para color nodos buscados
	// (iluminados)

	// Colores para el modo 2

	private int colorModo2_R[] = new int[Conf.numColoresMetodos]; // Valor R
	// para
	// color de
	// traza:
	// fondo
	// panel
	private int colorModo2_G[] = new int[Conf.numColoresMetodos]; // Valor G
	// para
	// color de
	// traza:
	// fondo
	// panel
	private int colorModo2_B[] = new int[Conf.numColoresMetodos]; // Valor B
	// para
	// color de
	// traza:
	// fondo
	// panel

	private int modoColor = 1;

	// Otras opciones sobre colores
	private boolean colorDegradadoModo1 = false; // false=se usan dos colores
	// para la celda (degradado)
	private boolean colorDegradadoModo2 = false; // false=se usan dos colores
	// para la celda (degradado)

	// Otras características
	private int grosorFlecha = 2; // Valor para grosor de flechas
	private int grosorActual = 4; // Valor para grosor de marco de nodo actual

	private int formaFlecha = 0; // Valor para el tipo de flecha que se mostrará

	private int distanciaH = 30; // Distancia horizontal entre celdas
	private int distanciaV = 30; // Distancia vertical entre celdas

	private int tipoBordeCelda = 0;

	// Fuentes (vistas de código y traza)
	private String fuenteCodigo = "Courier New Negrita";
	private String fuenteTraza = "Dialog.bold";

	private int tamFuenteCodigo = 12;
	private int tamFuenteTraza = 12;

	// Zoom por defecto
	private int zoomArbol = -35;
	private int zoomPila = -20;

	private int zoomCrono = -20;
	private int zoomEstructura = -20;

	/**
	 * Crea una nueva opción de visualización.
	 */
	public OpcionConfVisualizacion() {
		super("OpcionConfVisualizacion");
		this.colorModo2_R = Conf.getRojoDefecto();
		this.colorModo2_G = Conf.getVerdeDefecto();
		this.colorModo2_B = Conf.getAzulDefecto();
	}

	/**
	 * Permite establecer el color de la fuente para las celdas de entrada.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorFEntrada(int r, int g, int b) {
		this.colorFEntradaR = r;
		this.colorFEntradaG = g;
		this.colorFEntradaB = b;
	}

	/**
	 * Permite establecer el color de la fuente para las celdas de salida.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorFSalida(int r, int g, int b) {
		this.colorFSalidaR = r;
		this.colorFSalidaG = g;
		this.colorFSalidaB = b;
	}

	/**
	 * Permite establecer el color para las celdas de entrada.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorC1Entrada(int r, int g, int b) {
		this.colorC1EntradaR = r;
		this.colorC1EntradaG = g;
		this.colorC1EntradaB = b;
	}

	/**
	 * Permite establecer el color para las celdas de salida.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorC1Salida(int r, int g, int b) {
		this.colorC1SalidaR = r;
		this.colorC1SalidaG = g;
		this.colorC1SalidaB = b;
	}

	/**
	 * Permite establecer el color para las celdas atenuadas de entrada.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorCAEntrada(int r, int g, int b) {
		this.colorCAEntradaR = r;
		this.colorCAEntradaG = g;
		this.colorCAEntradaB = b;
	}

	/**
	 * Permite establecer el color para las celdas atenuadas de salida.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorCASalida(int r, int g, int b) {
		this.colorCASalidaR = r;
		this.colorCASalidaG = g;
		this.colorCASalidaB = b;
	}

	/**
	 * Permite establecer el color para las celdas no calculadas de salida.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorC1NCSalida(int r, int g, int b) {
		this.colorC1NCSalidaR = r;
		this.colorC1NCSalidaG = g;
		this.colorC1NCSalidaB = b;
	}

	/**
	 * Permite establecer si el color es degradado para el modo 1.
	 * 
	 * @param boolean true para degradado, false en caso contrario.
	 */
	public void setColorDegradadoModo1(boolean b) {
		this.colorDegradadoModo1 = b;
	}

	/**
	 * Permite establecer si el color es degradado para el modo 2.
	 * 
	 * @param boolean true para degradado, false en caso contrario.
	 */
	public void setColorDegradadoModo2(boolean b) {
		this.colorDegradadoModo2 = b;
	}

	/**
	 * Permite establecer el color de las flechas.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorFlecha(int r, int g, int b) {
		this.colorFlechaR = r;
		this.colorFlechaG = g;
		this.colorFlechaB = b;
	}

	/**
	 * Permite establecer el grosor de las flechas.
	 * 
	 * @param grosor
	 *            Grosor en pixeles.
	 */
	public void setGrosorFlecha(int grosor) {
		this.grosorFlecha = grosor;
	}

	/**
	 * Permite establecer el color de los paneles.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorPanel(int r, int g, int b) {
		this.colorPanelR = r;
		this.colorPanelG = g;
		this.colorPanelB = b;
	}

	/**
	 * Permite establecer el color del marcos del nodo actual en la
	 * visualización de algoritmos.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorActual(int r, int g, int b) {
		this.colorActualR = r;
		this.colorActualG = g;
		this.colorActualB = b;
	}

	/**
	 * Permite establecer el color de los marcos del camino actual en la
	 * visualización de algoritmos.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorCActual(int r, int g, int b) {
		this.colorCActualR = r;
		this.colorCActualG = g;
		this.colorCActualB = b;
	}

	/**
	 * Permite establecer la forma de las flechas en la visualización.
	 * 
	 * @param x
	 *            forma de la flecha.
	 */
	public void setFormaFlecha(int x) {
		this.formaFlecha = x;
	}

	/**
	 * Permite establecer el grosor de los marcos de las celdas.
	 * 
	 * @param x
	 *            Grosor en pixeles.
	 */
	public void setGrosorActual(int x) {
		this.grosorActual = x;
	}

	/**
	 * Permite establecer la distancia horizontal entre celdas.
	 * 
	 * @param x
	 *            distancia en pixeles.
	 */
	public void setDistanciaH(int x) {
		this.distanciaH = x;
	}

	/**
	 * Permite establecer la distancia vertical entre celdas.
	 * 
	 * @param x
	 *            distancia en pixeles.
	 */
	public void setDistanciaV(int x) {
		this.distanciaV = x;
	}

	/**
	 * Permite establecer el tipo de borde de las celdas.
	 * 
	 * @param x
	 *            Tipo de borde de las celdas.
	 */
	public void setTipoBordeCelda(int x) {
		this.tipoBordeCelda = x;
	}

	/**
	 * Permite establecer el color de las palabras reservadas en la vista de
	 * código.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorCodigoPR(int r, int g, int b) {
		this.colorCodigoPRR = r;
		this.colorCodigoPRG = g;
		this.colorCodigoPRB = b;
	}

	/**
	 * Permite establecer el color de los comentarios en la vista de código.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorCodigoCo(int r, int g, int b) {
		this.colorCodigoCoR = r;
		this.colorCodigoCoG = g;
		this.colorCodigoCoB = b;
	}

	/**
	 * Permite establecer el color de foreground para los métodos en la vista de
	 * código.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorCodigoMF(int r, int g, int b) {
		this.colorCodigoMFR = r;
		this.colorCodigoMFG = g;
		this.colorCodigoMFB = b;
	}

	/**
	 * Permite establecer el color de backgrouund para los métodos en la vista
	 * de código.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorCodigoMB(int r, int g, int b) {
		this.colorCodigoMBR = r;
		this.colorCodigoMBG = g;
		this.colorCodigoMBB = b;
	}

	/**
	 * Permite establecer el color del código en la vista de código.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorCodigoRC(int r, int g, int b) {
		this.colorCodigoRCR = r;
		this.colorCodigoRCG = g;
		this.colorCodigoRCB = b;
	}

	/**
	 * Permite establecer el color de los nodos iluminados.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorIluminado(int r, int g, int b) {
		this.colorIluminadoR = r;
		this.colorIluminadoG = g;
		this.colorIluminadoB = b;
	}

	/**
	 * Permite establecer el color de los nodos resaltados.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 */
	public void setColorResaltado(int r, int g, int b) {
		this.colorResaltadoR = r;
		this.colorResaltadoG = g;
		this.colorResaltadoB = b;
	}

	/**
	 * Permite establecer el modo de colores, el habitual, o el de
	 * diferenciación de métodos.
	 * 
	 * @param valor
	 *            1 para el habitual, 2 para el de diferenciación de métodos.
	 */
	public void setModoColor(int valor) {
		this.modoColor = valor;
	}

	/**
	 * Permite establecer el color para cada uno de los niveles del modo 2.
	 * 
	 * @param r
	 *            componente R
	 * @param g
	 *            componente G
	 * @param b
	 *            componente B
	 * @param i
	 *            nivel del modo 2.
	 */
	public void setColorModo2(int r, int g, int b, int i) {
		if (i >= 0 && i < this.colorModo2_R.length) {
			this.colorModo2_R[i] = r;
			this.colorModo2_G[i] = g;
			this.colorModo2_B[i] = b;
		}
	}

	/**
	 * Permite establecer la fuente y el tamaño de fuente de la vista de código.
	 * 
	 * @param nombre
	 *            nombre de la fuente.
	 * @param tam
	 *            Tamaño de la fuente.
	 */
	public void setFuenteCodigo(String nombre, int tam) {
		this.fuenteCodigo = nombre;
		this.tamFuenteCodigo = tam;
	}

	/**
	 * Permite establecer la fuente y el tamaño de fuente de la vista de traza.
	 * 
	 * @param nombre
	 *            nombre de la fuente.
	 * @param tam
	 *            Tamaño de la fuente.
	 */
	public void setFuenteTraza(String nombre, int tam) {
		this.fuenteTraza = nombre;
		this.tamFuenteTraza = tam;
	}

	/**
	 * Permite establecer el nivel de zoom de la vista de árbol.
	 * 
	 * @param zoom
	 *            nivel de zoom.
	 */
	public void setZoomArbol(int zoom) {
		this.zoomArbol = zoom;
	}

	/**
	 * Permite establecer el nivel de zoom de la vista de pila.
	 * 
	 * @param zoom
	 *            nivel de zoom.
	 */
	public void setZoomPila(int zoom) {
		this.zoomPila = zoom;
	}

	/**
	 * Permite establecer el nivel de zoom de la vista cronológica.
	 * 
	 * @param zoom
	 *            nivel de zoom.
	 */
	public void setZoomCrono(int zoom) {
		this.zoomCrono = zoom;
	}

	/**
	 * Permite establecer el nivel de zoom de la vista de estructura.
	 * 
	 * @param zoom
	 *            nivel de zoom.
	 */
	public void setZoomEstructura(int zoom) {
		this.zoomEstructura = zoom;
	}

	/**
	 * Devuelve el color de la fuente de entrada
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorFEntrada() {
		int color[] = new int[3];
		color[0] = this.colorFEntradaR;
		color[1] = this.colorFEntradaG;
		color[2] = this.colorFEntradaB;
		return color;
	}

	/**
	 * Devuelve el color de la fuente de salida
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorFSalida() {
		int color[] = new int[3];
		color[0] = this.colorFSalidaR;
		color[1] = this.colorFSalidaG;
		color[2] = this.colorFSalidaB;
		return color;
	}

	/**
	 * Devuelve el color de las celdas de entrada.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorC1Entrada() {
		int color[] = new int[3];
		color[0] = this.colorC1EntradaR;
		color[1] = this.colorC1EntradaG;
		color[2] = this.colorC1EntradaB;
		return color;
	}

	/**
	 * Devuelve el color de las celdas de salida.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorC1Salida() {
		int color[] = new int[3];
		color[0] = this.colorC1SalidaR;
		color[1] = this.colorC1SalidaG;
		color[2] = this.colorC1SalidaB;
		return color;
	}

	/**
	 * Devuelve el color de las celdas atenuadas de entrada.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorCAEntrada() {
		int color[] = new int[3];
		color[0] = this.colorCAEntradaR;
		color[1] = this.colorCAEntradaG;
		color[2] = this.colorCAEntradaB;
		return color;
	}

	/**
	 * Devuelve el color de las celdas atenuadas de salida.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorCASalida() {
		int color[] = new int[3];
		color[0] = this.colorCASalidaR;
		color[1] = this.colorCASalidaG;
		color[2] = this.colorCASalidaB;
		return color;
	}

	/**
	 * Devuelve el color de las celdas pendientes.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorC1NCSalida() {
		int color[] = new int[3];
		color[0] = this.colorC1NCSalidaR;
		color[1] = this.colorC1NCSalidaG;
		color[2] = this.colorC1NCSalidaB;
		return color;
	}

	/**
	 * Devuelve si el modo 1 debe mostrarse con degradado.
	 * 
	 * @return true si el modo 1 debe mostrarse con degradado, false en caso
	 *         contrario.
	 */
	public boolean getColorDegradadoModo1() {
		return this.colorDegradadoModo1;
	}

	/**
	 * Devuelve si el modo 2 debe mostrarse con degradado.
	 * 
	 * @return true si el modo 2 debe mostrarse con degradado, false en caso
	 *         contrario.
	 */
	public boolean getColorDegradadoModo2() {
		return this.colorDegradadoModo2;
	}

	/**
	 * Devuelve el color de las flechas.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorFlecha() {
		int color[] = new int[3];
		color[0] = this.colorFlechaR;
		color[1] = this.colorFlechaG;
		color[2] = this.colorFlechaB;
		return color;
	}

	/**
	 * Devuelve el grosor de las flechas.
	 * 
	 * @return Grosor de las flechas en número de píxeles.
	 */
	public int getGrosorFlecha() {
		return this.grosorFlecha;
	}

	/**
	 * Devuelve el color de los paneles.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorPanel() {
		int color[] = new int[3];
		color[0] = this.colorPanelR;
		color[1] = this.colorPanelG;
		color[2] = this.colorPanelB;
		return color;
	}

	/**
	 * Devuelve el color del marco actual.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorActual() {
		int color[] = new int[3];
		color[0] = this.colorActualR;
		color[1] = this.colorActualG;
		color[2] = this.colorActualB;
		return color;
	}

	/**
	 * Devuelve el color del marco del camino actual.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorCActual() {
		int color[] = new int[3];
		color[0] = this.colorCActualR;
		color[1] = this.colorCActualG;
		color[2] = this.colorCActualB;
		return color;
	}

	/**
	 * Devuelve la forma de las flechas.
	 * 
	 * @return forma de las flechas.
	 */
	public int getFormaFlecha() {
		return this.formaFlecha;
	}

	/**
	 * Devuelve el grosor del marco de las celdas.
	 * 
	 * @return grosor del marco de las celdas.
	 */
	public int getGrosorActual() {
		return this.grosorActual;
	}

	/**
	 * Devuelve la separación horizontal de las celdas.
	 * 
	 * @return Separación horizontal de las celdas en píxeles.
	 */
	public int getDistanciaH() {
		return this.distanciaH;
	}

	/**
	 * Devuelve la separación vertical de las celdas.
	 * 
	 * @return Separación vertical de las celdas en píxeles.
	 */
	public int getDistanciaV() {
		return this.distanciaV;
	}

	/**
	 * Devuelve el tipo de borde de las celdas.
	 * 
	 * @return Tipo de borde de las celdas.
	 */
	public int getTipoBordeCelda() {
		return this.tipoBordeCelda;
	}

	/**
	 * Devuelve el color de las palabras reservadas en la vista de código.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorCodigoPR() {
		int color[] = new int[3];
		color[0] = this.colorCodigoPRR;
		color[1] = this.colorCodigoPRG;
		color[2] = this.colorCodigoPRB;
		return color;
	}

	/**
	 * Devuelve el color de los comentarios en la vista de código.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorCodigoCo() {
		int color[] = new int[3];
		color[0] = this.colorCodigoCoR;
		color[1] = this.colorCodigoCoG;
		color[2] = this.colorCodigoCoB;
		return color;
	}

	/**
	 * Devuelve el color de foreground para los métodos en la vista de código.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorCodigoMF() {
		int color[] = new int[3];
		color[0] = this.colorCodigoMFR;
		color[1] = this.colorCodigoMFG;
		color[2] = this.colorCodigoMFB;
		return color;
	}

	/**
	 * Devuelve el color de background para los métodos en la vista de código.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorCodigoMB() {
		int color[] = new int[3];
		color[0] = this.colorCodigoMBR;
		color[1] = this.colorCodigoMBG;
		color[2] = this.colorCodigoMBB;
		return color;
	}

	/**
	 * Devuelve el color del código en la vista de código.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorCodigoRC() {
		int color[] = new int[3];
		color[0] = this.colorCodigoRCR;
		color[1] = this.colorCodigoRCG;
		color[2] = this.colorCodigoRCB;
		return color;
	}

	/**
	 * Devuelve el color de los nodos iluminados.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorIluminado() {
		int color[] = new int[3];
		color[0] = this.colorIluminadoR;
		color[1] = this.colorIluminadoG;
		color[2] = this.colorIluminadoB;
		return color;
	}

	/**
	 * Devuelve el color de los nodos resaltados.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorResaltado() {
		int color[] = new int[3];
		color[0] = this.colorResaltadoR;
		color[1] = this.colorResaltadoG;
		color[2] = this.colorResaltadoB;
		return color;
	}

	/**
	 * Devuelve la fuente usada en la vista de código.
	 * 
	 * @return Fuente para la vista de código.
	 */
	public String getFuenteCodigo() {
		return this.fuenteCodigo;
	}

	/**
	 * Devuelve el tamaño de fuente para la vista de código.
	 * 
	 * @return Tamaño de fuente para la vista de código.
	 */
	public int getTamFuenteCodigo() {
		return this.tamFuenteCodigo;
	}

	/**
	 * Permite obtener el modo de colores.
	 * 
	 * @return 1 para el habitual, 2 para el de diferenciación de métodos.
	 */
	public int getModoColor() {
		return this.modoColor;
	}

	/**
	 * Permite obtener el color correspondiente al nivel especificado del modo
	 * 2.
	 * 
	 * @param i
	 *            nivel del modo 2.
	 * 
	 * @return array de enteros con los componentes (R, G y B) respectivamente.
	 */
	public int[] getColorModo2(int i) {
		int color[] = new int[3];
		if (i >= 0 && i < this.colorModo2_R.length) {
			color[0] = this.colorModo2_R[i];
			color[1] = this.colorModo2_G[i];
			color[2] = this.colorModo2_B[i];
		}
		return color;
	}

	/**
	 * Devuelve la fuente usada en la vista de traza.
	 * 
	 * @return Fuente para la vista de traza.
	 */
	public String getFuenteTraza() {
		return this.fuenteTraza;
	}

	/**
	 * Devuelve el tamaño de fuente para la vista de traza.
	 * 
	 * @return Tamaño de fuente para la vista de traza.
	 */
	public int getTamFuenteTraza() {
		return this.tamFuenteTraza;
	}

	/**
	 * Devuelve el nivel de zoom para la vista de árbol.
	 * 
	 * @return Nivel de zooom para la vista de árbol.
	 */
	public int getZoomArbol() {
		return this.zoomArbol;
	}

	/**
	 * Devuelve el nivel de zoom para la vista de pila.
	 * 
	 * @return Nivel de zooom para la vista de pila.
	 */
	public int getZoomPila() {
		return this.zoomPila;
	}

	@Override
	public Element getRepresentacionElement(Document d) {
		Element e = d.createElement("OpcionConfVisualizacion");

		// Color fuente para celdas de entrada y salida
		Element e01 = d.createElement("colorFEntrada");
		e01.setAttribute("r", "" + this.colorFEntradaR);
		e01.setAttribute("g", "" + this.colorFEntradaG);
		e01.setAttribute("b", "" + this.colorFEntradaB);

		Element e02 = d.createElement("colorFSalida");
		e02.setAttribute("r", "" + this.colorFSalidaR);
		e02.setAttribute("g", "" + this.colorFSalidaG);
		e02.setAttribute("b", "" + this.colorFSalidaB);

		// Color 1 para celdas de entrada y salida
		Element e03 = d.createElement("colorC1Entrada");
		e03.setAttribute("r", "" + this.colorC1EntradaR);
		e03.setAttribute("g", "" + this.colorC1EntradaG);
		e03.setAttribute("b", "" + this.colorC1EntradaB);

		Element e04 = d.createElement("colorC1Salida");
		e04.setAttribute("r", "" + this.colorC1SalidaR);
		e04.setAttribute("g", "" + this.colorC1SalidaG);
		e04.setAttribute("b", "" + this.colorC1SalidaB);

		// Color atenuacion para celdas de entrada y salida
		Element e07 = d.createElement("colorC1AEntrada");
		e07.setAttribute("r", "" + this.colorCAEntradaR);
		e07.setAttribute("g", "" + this.colorCAEntradaG);
		e07.setAttribute("b", "" + this.colorCAEntradaB);

		Element e08 = d.createElement("colorC1ASalida");
		e08.setAttribute("r", "" + this.colorCASalidaR);
		e08.setAttribute("g", "" + this.colorCASalidaG);
		e08.setAttribute("b", "" + this.colorCASalidaB);

		// Colores no calculados para celdas de salida
		Element e11 = d.createElement("colorC1NCSalida");
		e11.setAttribute("r", "" + this.colorC1NCSalidaR);
		e11.setAttribute("g", "" + this.colorC1NCSalidaG);
		e11.setAttribute("b", "" + this.colorC1NCSalidaB);

		// Deben las celdas aplicar degradado
		Element e13 = d.createElement("degradados");
		e13.setAttribute("modo1", "" + this.colorDegradadoModo1);
		e13.setAttribute("modo2", "" + this.colorDegradadoModo2);

		// Colores para flechas y paneles
		Element e15 = d.createElement("colorFlecha");
		e15.setAttribute("r", "" + this.colorFlechaR);
		e15.setAttribute("g", "" + this.colorFlechaG);
		e15.setAttribute("b", "" + this.colorFlechaB);

		Element e16 = d.createElement("colorPanel");
		e16.setAttribute("r", "" + this.colorPanelR);
		e16.setAttribute("g", "" + this.colorPanelG);
		e16.setAttribute("b", "" + this.colorPanelB);

		// Colores para nodo actual y camino actual
		Element e17 = d.createElement("colorActual");
		e17.setAttribute("r", "" + this.colorActualR);
		e17.setAttribute("g", "" + this.colorActualG);
		e17.setAttribute("b", "" + this.colorActualB);

		Element e18 = d.createElement("colorCActual");
		e18.setAttribute("r", "" + this.colorCActualR);
		e18.setAttribute("g", "" + this.colorCActualG);
		e18.setAttribute("b", "" + this.colorCActualB);

		// Valores varios
		Element e19 = d.createElement("varios");
		e19.setAttribute("grosorMarcos", "" + this.grosorActual);
		e19.setAttribute("grosorFlecha", "" + this.grosorFlecha);
		e19.setAttribute("tipoFlecha", "" + this.formaFlecha);
		e19.setAttribute("tipoBordeCelda", "" + this.tipoBordeCelda);

		// Color para vista código: palabras reservadas
		Element e20 = d.createElement("colorPalabrasReservadas");
		e20.setAttribute("r", "" + this.colorCodigoPRR);
		e20.setAttribute("g", "" + this.colorCodigoPRG);
		e20.setAttribute("b", "" + this.colorCodigoPRB);

		// Color para vista código: comentarios
		Element e21 = d.createElement("colorComentarios");
		e21.setAttribute("r", "" + this.colorCodigoCoR);
		e21.setAttribute("g", "" + this.colorCodigoCoG);
		e21.setAttribute("b", "" + this.colorCodigoCoB);

		// Color para vista código: nombre método foreground
		Element e22 = d.createElement("colorMetodoForeground");
		e22.setAttribute("r", "" + this.colorCodigoMFR);
		e22.setAttribute("g", "" + this.colorCodigoMFG);
		e22.setAttribute("b", "" + this.colorCodigoMFB);

		// Color para vista código: nombre método background
		Element e23 = d.createElement("colorMetodoBackground");
		e23.setAttribute("r", "" + this.colorCodigoMBR);
		e23.setAttribute("g", "" + this.colorCodigoMBG);
		e23.setAttribute("b", "" + this.colorCodigoMBB);

		// Color para vista código: nombre código
		Element e24 = d.createElement("colorCodigo");
		e24.setAttribute("r", "" + this.colorCodigoRCR);
		e24.setAttribute("g", "" + this.colorCodigoRCG);
		e24.setAttribute("b", "" + this.colorCodigoRCB);

		// Color para vista código: nombre código
		Element e25 = d.createElement("colorIluminado");
		e25.setAttribute("r", "" + this.colorIluminadoR);
		e25.setAttribute("g", "" + this.colorIluminadoG);
		e25.setAttribute("b", "" + this.colorIluminadoB);

		// Color para vista código: nombre código
		Element e34 = d.createElement("colorResaltado");
		e34.setAttribute("r", "" + this.colorResaltadoR);
		e34.setAttribute("g", "" + this.colorResaltadoG);
		e34.setAttribute("b", "" + this.colorResaltadoB);

		// Modo de color
		Element e14 = d.createElement("modoColor");
		e14.setAttribute("modo", "" + this.getModoColor());

		// Fuentes y sus tamanos
		Element e31 = d.createElement("fuentesTrazaCodigo");
		e31.setAttribute("fuenteCodigo", this.fuenteCodigo);
		e31.setAttribute("tamFuenteCodigo", "" + this.tamFuenteCodigo);
		e31.setAttribute("fuenteTraza", this.fuenteTraza);
		e31.setAttribute("tamFuenteTraza", "" + this.tamFuenteTraza);

		// Zooms
		Element e32 = d.createElement("zoomsDefecto");
		e32.setAttribute("zoomArbol", "" + this.zoomArbol);
		e32.setAttribute("zoomPila", "" + this.zoomPila);
		e32.setAttribute("zoomCrono", "" + this.zoomCrono);
		e32.setAttribute("zoomEstructura", "" + this.zoomEstructura);

		// Distancias
		Element e33 = d.createElement("distancias");
		e33.setAttribute("vertical", "" + this.distanciaV);
		e33.setAttribute("horizontal", "" + this.distanciaH);

		e.appendChild(e01);
		e.appendChild(e02);
		e.appendChild(e03);
		e.appendChild(e04);
		e.appendChild(e07);
		e.appendChild(e08);
		e.appendChild(e11);
		e.appendChild(e13);
		e.appendChild(e14);
		e.appendChild(e15);
		e.appendChild(e16);
		e.appendChild(e17);
		e.appendChild(e18);
		e.appendChild(e19);
		e.appendChild(e20);
		e.appendChild(e21);
		e.appendChild(e22);
		e.appendChild(e23);
		e.appendChild(e24);
		e.appendChild(e25);
		e.appendChild(e31);
		e.appendChild(e32);
		e.appendChild(e33);
		e.appendChild(e34);

		// Colores Modo 2
		for (int i = 0; i < Conf.numColoresMetodos; i++) {
			Element ecm2 = d.createElement("colorm2_" + i);
			ecm2.setAttribute("r", "" + this.colorModo2_R[i]);
			ecm2.setAttribute("g", "" + this.colorModo2_G[i]);
			ecm2.setAttribute("b", "" + this.colorModo2_B[i]);
			e.appendChild(ecm2);
		}

		return e;
	}

	@Override
	public void setValores(Element e) {
		Element elements[];
		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorFEntrada"));
		this.setColorFEntrada(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorFSalida"));
		this.setColorFSalida(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorC1Entrada"));
		this.setColorC1Entrada(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorC1AEntrada"));
		this.setColorCAEntrada(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorC1Salida"));
		this.setColorC1Salida(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorC1ASalida"));
		this.setColorCASalida(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorC1NCSalida"));
		this.setColorC1NCSalida(
				Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("degradados"));
		this.setColorDegradadoModo1(Boolean.parseBoolean(elements[0]
				.getAttribute("modo1")));
		this.setColorDegradadoModo2(Boolean.parseBoolean(elements[0]
				.getAttribute("modo2")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorFlecha"));
		this.setColorFlecha(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorPanel"));
		this.setColorPanel(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorActual"));
		this.setColorActual(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorCActual"));
		this.setColorCActual(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("modoColor"));
		this.setModoColor(Integer.parseInt(elements[0].getAttribute("modo")));

		for (int i = 0; i < Conf.numColoresMetodos; i++) {
			elements = ManipulacionElement.nodeListToElementArray(e
					.getElementsByTagName("colorm2_" + i));
			this.setColorModo2(Integer.parseInt(elements[0].getAttribute("r")),
					Integer.parseInt(elements[0].getAttribute("g")),
					Integer.parseInt(elements[0].getAttribute("b")), i);
		}

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("varios"));
		this.setGrosorActual(Integer.parseInt(elements[0]
				.getAttribute("grosorMarcos")));
		this.setGrosorFlecha(Integer.parseInt(elements[0]
				.getAttribute("grosorFlecha")));
		this.setFormaFlecha(Integer.parseInt(elements[0]
				.getAttribute("tipoFlecha")));
		this.setTipoBordeCelda(Integer.parseInt(elements[0]
				.getAttribute("tipoBordeCelda")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorPalabrasReservadas"));
		this.setColorCodigoPR(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorComentarios"));
		this.setColorCodigoCo(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorMetodoForeground"));
		this.setColorCodigoMF(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorMetodoBackground"));
		this.setColorCodigoMB(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorCodigo"));
		this.setColorCodigoRC(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorIluminado"));
		this.setColorIluminado(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("colorResaltado"));
		this.setColorResaltado(Integer.parseInt(elements[0].getAttribute("r")),
				Integer.parseInt(elements[0].getAttribute("g")),
				Integer.parseInt(elements[0].getAttribute("b")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("fuentesTrazaCodigo"));
		this.setFuenteCodigo((elements[0].getAttribute("fuenteCodigo")),
				Integer.parseInt(elements[0].getAttribute("tamFuenteCodigo")));
		this.setFuenteTraza((elements[0].getAttribute("fuenteTraza")),
				Integer.parseInt(elements[0].getAttribute("tamFuenteTraza")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("zoomsDefecto"));
		this.setZoomArbol(Integer.parseInt(elements[0]
				.getAttribute("zoomArbol")));
		this.setZoomPila(Integer.parseInt(elements[0].getAttribute("zoomPila")));
		this.setZoomCrono(Integer.parseInt(elements[0]
				.getAttribute("zoomCrono")));
		this.setZoomEstructura(Integer.parseInt(elements[0]
				.getAttribute("zoomEstructura")));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("distancias"));
		this.setDistanciaV(Integer.parseInt(elements[0]
				.getAttribute("vertical")));
		this.setDistanciaH(Integer.parseInt(elements[0]
				.getAttribute("horizontal")));

	}
}
