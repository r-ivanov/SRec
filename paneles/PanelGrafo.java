package paneles;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.plaf.metal.MetalBorders;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;

import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroDibujarTablaGrafoDependencia;
import cuadros.CuadroError;
import cuadros.CuadroTabularGrafoDependencia;
import cuadros.CuadroTabularGrafoDependenciaMultiplesMetodos;
import datos.DatosMetodoBasicos;
import datos.GrafoDependencia;
import datos.NodoGrafoDependencia;
import utilidades.NombresYPrefijos;
import utilidades.Texto;

/**
 * Panel que contendrá la visualización de la vista de grafo de dependencia del algoritmo.
 * 
 * @author Daniel Arroyo Cortés
 * @version 2016
 */
public class PanelGrafo extends JPanel implements ActionListener, KeyListener,
MouseListener, MouseMotionListener {
	
	private static final long serialVersionUID = 1L;
	
	private Ventana ventana;

	private DatosMetodoBasicos metodo;
	
	private JPanel panelHerramientas;
	private JToolBar[] barras;
	private JButton[] botones;

	private GrafoDependencia grafoDependencia;
	private JGraph representacionGrafo;

	private String ultimaExpresionParaFila;				//	Expresión filas si han tabulado 1 método
	private String ultimaExpresionParaColumna;			//	Expresión columnas si han tabulado 1 método

	private JPanel panel;

	private double escalaOriginal;
	private double escalaActual;
	
	private int zoom = 0;	
	
	private int tipoGrafo = -1;	// 	0 - Grafo normal
								//	1 - Grafo con matriz
								//	2 - Grafo tabulado
	
	private boolean orientacionFlechas = true;	// 	True - Normal
												//	False - Invertidas

	private int numeroFilas;	//	Nº filas si han dibujado tabla
	private int numeroColumnas;	//	Nº columnas si han dibujado tabla

	private NombresYPrefijos nyp;
	
	private boolean eliminarFilasColumnas = false;
	
	//	Multiples métodos
	
	private boolean esGrafoDeUnMetodo = true;			//	true = solo se representa un método
														//	false = se representan varios métodos	
	private List<DatosMetodoBasicos> metodos;			//	Métodos en caso de tener varios
	private String ultimaExpresionMultiplesMetodos;		//	Expresión si han tabulado multiples métodos
	private boolean esExpresionDeFila;					//	True - La expresión es de filas, False - Caso contrario
	private List<Integer> parametrosComunes;			//	Lista de índices de parámetros comunes respecto al primer método
	private List<String> parametrosComunesS;			//	Lista de nombres de los parámetros comunes respecto al primer método
	
	/**
	 * Constructor: crea un nuevo panel de visualización para el grafo.
	 * 
	 * @param metodo
	 * 		Método del que queremos crear el grafo de dependencia
	 * 
	 * @param ventana
	 * 		Ventana a la que está asociada la pestaña, es la principal
	 * 		y es necesaria para obtener algunos datos
	 * 
	 * @param nyp
	 * 		Nombres y prefijos, para abreviar nombre de métodos si están visibles
	 *  	y es necesario
	 */
	public PanelGrafo(DatosMetodoBasicos metodo, Ventana ventana, NombresYPrefijos nyp) throws Exception {
		
		if (Ventana.thisventana.traza != null && metodo != null && ventana != null) {			
			this.esGrafoDeUnMetodo = true;
			this.metodos = null;
			//	Obtenemos datos básicos del grafo y su representación
			this.nyp = nyp;
			this.metodo = metodo;
			this.ventana = ventana;	
				
			this.grafoDependencia = new GrafoDependencia(this.metodo,this.nyp);
			this.representacionGrafo = this.grafoDependencia
					.obtenerRepresentacionGrafo(false);
			this.tipoGrafo = 0;
			this.representacionGrafo.setScale(this.representacionGrafo.getScale());
			this.escalaOriginal = this.representacionGrafo.getScale();
			this.escalaActual = this.representacionGrafo.getScale();
			this.refrescarZoom(-50);	//	Tamaño por defecto del grafo al inicio
			
			//	Creamos pestaña	y llamamos a visualizar	
				
			this.panel = new JPanel();			
			this.add(this.panel, BorderLayout.NORTH);
			
			try {
				this.visualizar();				
			} catch (OutOfMemoryError oome) {
				this.representacionGrafo = null;
				throw oome;
			} catch (Exception e) {
				throw e;
			}
		} else {
			this.add(new JPanel());
		}
	}
	
	/**
	 * Constructor: crea un nuevo panel de visualización para el grafo.
	 * 
	 * @param metodo
	 * 		Lista de métodos de los que queremos crear el grafo de dependencia
	 * 
	 * @param ventana
	 * 		Ventana a la que está asociada la pestaña, es la principal
	 * 		y es necesaria para obtener algunos datos
	 * 
	 * @param nyp
	 * 		Nombres y prefijos, para abreviar nombre de métodos si están visibles
	 *  	y es necesario
	 */
	public PanelGrafo(List<DatosMetodoBasicos> metodo, Ventana ventana, NombresYPrefijos nyp) throws Exception {
		
		if (Ventana.thisventana.traza != null && metodo != null && ventana != null) {			
			this.esGrafoDeUnMetodo = false;
			this.metodo = null;
			//	Obtenemos datos básicos del grafo y su representación
			this.nyp = nyp;
			this.metodos = metodo;
			this.ventana = ventana;	
				
			this.grafoDependencia = new GrafoDependencia(this.metodos,this.nyp);
			this.representacionGrafo = this.grafoDependencia
					.obtenerRepresentacionGrafo(false);
			this.tipoGrafo = 0;
			this.representacionGrafo.setScale(this.representacionGrafo.getScale());
			this.escalaOriginal = this.representacionGrafo.getScale();
			this.escalaActual = this.representacionGrafo.getScale();
			this.refrescarZoom(-50);	//	Tamaño por defecto del grafo al inicio
			
			//	Creamos pestaña	y llamamos a visualizar	
				
			this.panel = new JPanel();			
			this.add(this.panel, BorderLayout.NORTH);
			
			try {
				this.visualizar();				
			} catch (OutOfMemoryError oome) {
				this.representacionGrafo = null;
				throw oome;
			} catch (Exception e) {
				throw e;
			}
		} else {
			this.add(new JPanel());
		}
	}
	
	/**
	 * Obtiene el tamaño que ocupa el grafo en pantalla aunque el usuario
	 * 	haya desplazado nodos a mano
	 * 
	 * @return Array de double donde [0] = Ancho grafo y [1] = Alto grafo
	 */
	private double[] obtenerTamanioGrafo(){
		Dimension tamanioGrafo = this.grafoDependencia
				.getTamanioRepresentacion();
		Rectangle rectangulo = new Rectangle(0, 0, tamanioGrafo.width,
				tamanioGrafo.height);
		for (CellView cell : this.representacionGrafo.getGraphLayoutCache()
				.getAllViews()) {
			Rectangle2D nodoBounds = cell.getBounds();
			rectangulo = rectangulo.union(new Rectangle(
					(int) nodoBounds.getX(), (int) nodoBounds.getY(),
					(int) nodoBounds.getWidth(), (int) nodoBounds.getHeight()));
		}
		tamanioGrafo = new Dimension(rectangulo.width, rectangulo.height);
		double[] valorDevuelto = new double[2];
		valorDevuelto[0] = tamanioGrafo.getWidth();
		valorDevuelto[1] = tamanioGrafo.getHeight();		
		return valorDevuelto;
	}
	
	/**
	 * Dibuja una tabla con un número de filas y columnas.
	 * 
	 * @param filas
	 *            Número de filas.
	 * @param columnas
	 *            Número de columnas.
	 */
	public void dibujarTabla(int filas, int columnas) {
		this.representacionGrafo = null;
		this.grafoDependencia.setTamanioTabla(filas, columnas);
		this.representacionGrafo = this.grafoDependencia
				.obtenerRepresentacionGrafo(false);
		this.numeroFilas = filas;
		this.numeroColumnas = columnas;
		this.visualizar();
	}
	
	/**
	 * Tabula automáticamente los nodos del grafo, dada una expresión para filas
	 * y otra para columnas.
	 * 
	 * @param expresionParaFila 		
	 * 		Expresión para las filas
	 * @param expresionParaColumna		
	 * 		Expresión para columnas
	 * 
	 * @return 
	 * 		Mensaje de error si ocurrió algun error, null en caso contrario.
	 */
	public String tabular(String expresionParaFila, String expresionParaColumna) {
		this.ultimaExpresionParaFila = expresionParaFila;
		this.ultimaExpresionParaColumna = expresionParaColumna;
		String mensajeError = this.grafoDependencia.tabular(expresionParaFila,
				expresionParaColumna);
		if (mensajeError == null) {
			this.representacionGrafo = null;
			this.representacionGrafo = this.grafoDependencia
					.obtenerRepresentacionGrafo(true);

			this.visualizar();
		}
		return mensajeError;
	}
	
	/**
	 * 	Tabula automáticamente los nodos del grafo de múltiples métodos,
	 * dada una expresión y un booleano que indica si la espresión determina
	 * las filas o las columnas
	 * 
	 * @param ultimaExpresionMultiplesMetodos
	 * 		Expresión introducida por el usuario para tabular
	 * 		el grafo de múltiples métodos
	 * 
	 * @param esExpresionDeFila
	 * 		True, la expresión determina filas y las funciones columnas
	 * 		False, la expresión determina columnas y las funciones filas
	 * 
	 * @return
	 * 		Mensaje de error si ocurrió algun error, null en caso contrario.
	 */
	public String tabularMultiplesMetodos(String ultimaExpresionMultiplesMetodos, boolean esExpresionDeFila) {
		
		String mensajeError = this.grafoDependencia.tabularMultiplesMetodos(ultimaExpresionMultiplesMetodos, esExpresionDeFila);
		if (mensajeError == null) {
			this.representacionGrafo = null;
			this.representacionGrafo = this.grafoDependencia
					.obtenerRepresentacionGrafo(true);

			this.visualizar();
		}		
		this.ultimaExpresionMultiplesMetodos = ultimaExpresionMultiplesMetodos;
		this.esExpresionDeFila = esExpresionDeFila;		
		return mensajeError;
	}
	
	/**
	 * Invierte las flechas del grafo
	 * 
	 * @param visualizar
	 * 		True si queremos visualizar junto con el método los cambios
	 * 		False caso contrario
	 */
	private void invertirFlechasGrafo(boolean visualizar){
		List<NodoGrafoDependencia> listaNodos = this.grafoDependencia.getNodos();
		for(NodoGrafoDependencia nodo:listaNodos){
			nodo.invertirAristas();					
		}
		if(this.eliminarFilasColumnas){
			this.representacionGrafo = this.grafoDependencia
					.obtenerRepresentacionGrafoEliminadasFilasYColumnas();
		}else{
			this.representacionGrafo = this.grafoDependencia
					.obtenerRepresentacionGrafo(true);
		}
		this.orientacionFlechas = !this.orientacionFlechas;
		if(visualizar){
			this.visualizar();
		}
	}
	
	/**
	 * Visualiza y redibuja el grafo en la pestaña.
	 */
	public void visualizar() {		
		if (Ventana.thisventana.traza != null && this.representacionGrafo != null) {			
			this.representacionGrafo.getModel().addGraphModelListener(null);
			this.representacionGrafo.addMouseListener(this);
			this.representacionGrafo.setScale(this.escalaActual);
			this.representacionGrafo.setBackground(Conf.colorPanel);

			this.panel.addMouseListener(this);

			this.panel.removeAll();
			this.crearBarraDeHerramientas();
			this.panel.setLayout (new BorderLayout());
			this.panel.add (this.representacionGrafo, BorderLayout.CENTER);
			this.panel.add (this.panelHerramientas, BorderLayout.NORTH);
			
			this.panel.setBackground(Conf.colorPanel);
			this.setBackground(Conf.colorPanel);
			this.panel.updateUI();
			this.updateUI();		
		}
	}
	
	/**
	 * Permite actualizar el grafo cuando haya un cambio en la traza
	 * (cambio colores, entrada, salida... etc)
	 * 
	 * @param nyp
	 * 	Nombres y prefijos, para abreviar nombre de métodos si están visibles
	 *  y es necesario
	 */
	public void visualizar2(NombresYPrefijos nyp){
		if (Ventana.thisventana.traza != null) {
			
			//	Creamos grafo nuevo siempre, para que cargue la nueva traza
			//		y/o las nuevas opciones de visualización
			if(this.esGrafoDeUnMetodo)
				this.grafoDependencia = new GrafoDependencia(this.metodo,nyp);
			else
				this.grafoDependencia = new GrafoDependencia(this.metodos,nyp);
			
			this.representacionGrafo = this.grafoDependencia
					.obtenerRepresentacionGrafo(false);	
			
			//	Dejamos las flechas con la orientación que tenían
			if(!this.orientacionFlechas){
				this.invertirFlechasGrafo(false);
				this.orientacionFlechas = !this.orientacionFlechas;
			}
			
			//	Revisamos el tipo de grafo que teníamos creado
			switch(this.tipoGrafo){
			
				//	Grafo normal
				case 0:						
									
					this.visualizar();
					break;
				
				//	Grafo con tabla
				case 1:	
					
					//	Se crea tabla con valor almacenado previamente
					this.dibujarTabla(this.numeroFilas, this.numeroColumnas);
					
					//	No se llama a visualizar porque ya está en dibujar tabla					
					break;
				
				//	Grafo tabulado
				case 2:	
			
					//	Se crea grafo tabulado con valor almacenado previamente
					if(this.esGrafoDeUnMetodo)
						this.tabular(this.ultimaExpresionParaFila, this.ultimaExpresionParaColumna);
					else
						this.tabularMultiplesMetodos(this.ultimaExpresionMultiplesMetodos, this.esExpresionDeFila);
					
					//	Eliminar filas y columnas
					if(this.eliminarFilasColumnas){
						this.grafoDependencia.nodosPosicionadosFalse();
						this.representacionGrafo = this.grafoDependencia
								.obtenerRepresentacionGrafoEliminadasFilasYColumnas();
						this.botones[0].setEnabled(false);
						this.visualizar();
					}
					
					//	No se llama a visualizar porque ya está en dibujar tabla
					break;
			}
			
		}
	}
	
	/**
	 * Devuelve el grafo de la vista.
	 * 
	 * @return Grafo de la vista.
	 */
	public JGraph getGrafo() {
		return this.representacionGrafo;
	}
	
	/**
	 * Devuelve el nivel de zoom actual.
	 * 
	 * @return Nivel de zoom actual.
	 */
	public int getZoom() {						
		return this.zoom;
	}
	
	/**
	 * Devuelve las dimensiones del grafo.
	 * 
	 * @return Array de int, donde la posición 0 contiene el máximo ancho, y la
	 *         posición 1 el máximo alto.
	 */
	public int[] dimGrafo() {				
		int[] dim = new int[2];
		dim[0] = this.grafoDependencia.getTamanioRepresentacion().width;			
		dim[1] = this.grafoDependencia.getTamanioRepresentacion().height;	
		return dim;
	}
	
	/**
	 * Devuelve las dimensiones del panel y del grafo.
	 * 
	 * @return Array de int, donde la posición 0 corresponde al ancho de la pestaña, la 1
	 *         al alto de la pestaña, la 2 al ancho del grafo, y la 3 al alto del
	 *         grafo.
	 */
	public int[] dimPanelYGrafoDep() {
		int dim[] = new int[4];

		dim[0] = (int) (this.getVisibleRect().getWidth()); // Anchura de pestaña
		dim[1] = (int) (this.getVisibleRect().getHeight()); // Altura de pestaña

		if(this.representacionGrafo != null){
			double[] tamanioGrafo = obtenerTamanioGrafo();
			dim[2] = (int) (tamanioGrafo[0]); // Anchura			
			dim[3] = (int) (tamanioGrafo[1]); // Altura
		}else{
			dim[2] = 0;
			dim[3] = 0;
		}
		
		return dim;
	}
	
	/**
	 * Permite refrescar el zoom al valor dado por el parámetro
	 * 
	 * @param valor Valor al que queremos ajustar el zoom
	 */
	public void refrescarZoom(int valor) {	
		if (valor == 0) {
			this.representacionGrafo.setScale(this.escalaOriginal);
		} else if (valor > 0) {
			double v = valor;
			v = v / 100;
			v = v + 1;
			v = v * this.escalaOriginal;
			this.representacionGrafo.setScale(v);
		} else // if (valor<0)
		{
			double v = (valor * (-1));
			v = v / 100;
			v = 1 - v;
			v = v * this.escalaOriginal;
			this.representacionGrafo.setScale(v);
		}
		this.escalaActual = this.representacionGrafo.getScale();
		this.zoom = valor;
	}
	
	
	/**
	 * Crea la barra de herramientas de la ventana.
	 */
	private void crearBarraDeHerramientas() {

		this.botones = new JButton[4];

		this.botones[0] = new JButton(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/i_tabulado.gif")));
		this.botones[0].setToolTipText(Texto.get("GP_DIBUJAR_MATRIZ",
				Conf.idioma));
		
		this.botones[1] = new JButton(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/i_tabulado_automatico.gif")));
		this.botones[1].setToolTipText(Texto.get("GP_TABULAR_MATRIZ",
				Conf.idioma));	
		
		this.botones[2] = new JButton(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/i_invertir_flechas_grafo.gif")));
		this.botones[2]
				.setToolTipText(Texto.get("GP_INVERTIR_FLECHAS", Conf.idioma));
		
		this.botones[3] = new JButton(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/i_tabulado_eliminarFilas.gif")));
		this.botones[3]
				.setToolTipText(Texto.get("GP_ELIMINAR_FYC", Conf.idioma));
				
		// Creamos las barras de herramientas
		this.barras = new JToolBar[2];
		for (int i = 0; i < this.barras.length; i++) {
			this.barras[i] = new JToolBar(Texto.get("BARRA_HERR", Conf.idioma));
			this.barras[i].setBorderPainted(true);
			this.barras[i].setFloatable(false);
			this.barras[i].setBorder(new MetalBorders.PaletteBorder());
		}

		// Grupo tabulación
		this.barras[0].add(this.botones[0]);
		this.barras[0].add(this.botones[1]);

		// Grupo opciones nodos
		this.barras[1].add(this.botones[2]);	
		this.barras[1].add(this.botones[3]);

		this.panelHerramientas = new JPanel();
		this.panelHerramientas.setLayout(new BorderLayout());

		JPanel p = new JPanel();
		for (int i = 0; i < this.barras.length; i++) {
			p.add(this.barras[i]);
		}

		for (int i = 0; i < this.botones.length; i++) {
			this.botones[i].setFocusable(false);
			this.botones[i].addActionListener(this);
		}

		this.panelHerramientas.add(p, BorderLayout.WEST);

		JLabel labelTitulo = new JLabel(this.ventana.getTraza().getTitulo());
		labelTitulo.setFont(new Font("Arial", Font.BOLD, 14));
		JLabel labelSignatura;
		if(this.esGrafoDeUnMetodo)
			labelSignatura = new JLabel("  -  " + this.metodo.getInterfaz()
					+ "   ");
		else{
			labelSignatura = this.getSignaturaMultiplesMetodos();
		}
			
		labelSignatura.setFont(new Font("Arial", Font.ITALIC, 14));

		JPanel panelInfo = new JPanel(new BorderLayout());
		panelInfo.add(labelTitulo, BorderLayout.WEST);
		panelInfo.add(new JLabel("    -    "), BorderLayout.CENTER);
		panelInfo.add(labelSignatura, BorderLayout.EAST);

		this.panelHerramientas.add(panelInfo, BorderLayout.EAST);

		if(this.tipoGrafo != 2)
			this.botones[3].setEnabled(false);
		if(this.eliminarFilasColumnas)
			this.botones[0].setEnabled(false);
//		this.add(this.panelHerramientas, BorderLayout.NORTH);
	}	

	/**
	 * Obtiene el label de la signatura de los métodos seleccionados
	 * 
	 * @return
	 * 	Label de la signatura de los métodos seleccionados
	 */
	private JLabel getSignaturaMultiplesMetodos(){
		JLabel labelSignatura;
		String signatura = "";
		for(DatosMetodoBasicos dmb : this.metodos){
			signatura += dmb.getInterfaz() + " , <br />";
		}
		signatura = signatura.substring(0,signatura.length()-9);
		labelSignatura = new JLabel("<html>" + signatura
		+ "</html>");
		return labelSignatura;
	}
	
	/**
	 * Permite comparar un método pasado como parámetro con el método
	 * 	que se está visualizando actualmente en la pestaña
	 * 
	 * @param nuevoMetodo Método a comparar con el actual
	 * @return True si nuevoMetodo = metodo que se visualiza actualmente, False caso contrario
	 */
	public boolean esIgual(DatosMetodoBasicos nuevoMetodo){
		return (this.metodo != null && this.metodo.esIgual(nuevoMetodo));
	}
	
	/**
	 * Permite comparar una lista de métodos pasados como parámetro con 
	 * los métodos que se estan visualizando actualmente
	 * 
	 * @param nuevoMetodo Lista de métodos a comparar con la lista de métodos actuales
	 * @return True si lista nuevoMetodo = lista métodos que se visualizan actualmente, 
	 * 			False caso contrario
	 */
	public boolean esIgual(List<DatosMetodoBasicos> nuevoMetodo){		
		if(this.metodos == null || nuevoMetodo.size() != this.metodos.size()){
			return false;
		}
		
		for(int i=0 ; i<nuevoMetodo.size() ; i++){
			if(!nuevoMetodo.get(i).esIgual(this.metodos.get(i))){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.botones[0]) {				//	Matriz
			new CuadroDibujarTablaGrafoDependencia(this.ventana,
					this.grafoDependencia.getNumeroFilasTabla(),
					this.grafoDependencia.getNumeroColumnasTabla(),
					this);
			this.tipoGrafo = 1;
			
		} else if (e.getSource() == this.botones[1]) {
			if(this.esGrafoDeUnMetodo){						//	Tabular solo un método				
				new CuadroTabularGrafoDependencia(this.ventana,
						this.metodo.getInterfaz(),
						this.ultimaExpresionParaFila,
						this.ultimaExpresionParaColumna,
						this);	
			}
			else{											//	Tabular múltiples métodos
				this.parametrosComunes = this.grafoDependencia.getParametrosComunes();
				this.parametrosComunesS = this.grafoDependencia.getParametrosComunesS();
				if(this.parametrosComunes.size()==0){
					new CuadroError(this.ventana, Texto.get("ERROR_PARAM_GD",Conf.idioma),
							Texto.get("ERROR_PARAM_GD_TXT",
							Conf.idioma));
				}else{					
                    this.parametrosComunesS = this.grafoDependencia.getParametrosComunesS();
                    new CuadroTabularGrafoDependenciaMultiplesMetodos(
                            this.ventana,
                            this,
                            this.getSignaturaMultiplesMetodos(),
                            this.parametrosComunesS,
                            this.ultimaExpresionMultiplesMetodos);
				}
			}
//            this.tipoGrafo = 2;
//            this.eliminarFilasColumnas = false;
            
		}else if (e.getSource() == this.botones[2]) {		//	Flechas
			this.invertirFlechasGrafo(true);
				
		}else if (e.getSource() == this.botones[3]) {		//	Eliminar filas y columnas vacías
			this.grafoDependencia.nodosPosicionadosFalse();	
			this.eliminarFilasColumnas  = !this.eliminarFilasColumnas;
			if(this.eliminarFilasColumnas){
				this.representacionGrafo = this.grafoDependencia
						.obtenerRepresentacionGrafoEliminadasFilasYColumnas();
			}else{
				this.representacionGrafo = this.grafoDependencia
						.obtenerRepresentacionGrafo(true);
			}
//			this.grafoDependencia.nodosPosicionadosFalse();			
			this.visualizar();			
			this.tipoGrafo = 2;
		}
	}
	
	/**
	 * Obtiene el tipo de grafo que se está representando
	 * 
	 * @return
	 * 	0 - Grafo normal
	 *	1 - Grafo con matriz
	 *	2 - Grafo tabulado
	 */
	public int getTipoGrafo(){
		return this.tipoGrafo;
	}
	
	/**
	 * Establece el tipo de grafo que se está representando
	 * 
	 * @param tipo
	 * 	0 - Grafo normal
	 *	1 - Grafo con matriz
	 *	2 - Grafo tabulado
	 */
	public void setTipoGrafo(int tipo){
		this.tipoGrafo = tipo;
		if(this.tipoGrafo != 2)
			this.botones[3].setEnabled(false);
		else
			this.botones[3].setEnabled(true);		
	}
	
	/**
	 * Obtiene si las flechas están invertidas en el grafo
	 * 	o no
	 * 
	 * @return
	 * 
	 *	True - Normal
	 *	False - Invertidas
	 */
	public boolean getEliminarFilasColumnas(){
		return this.eliminarFilasColumnas;
	}
	
	/**
	 * Establece si las filas están invertidas en el grafo
	 * 	o no
	 * 
	 * @param eliminar
	 * 
	 * 	True - Normal
	 *	False - Invertidas
	 */
	public void setEliminarFilasColumnas(boolean eliminar){
		this.eliminarFilasColumnas = eliminar;
		if(this.eliminarFilasColumnas)
			this.botones[0].setEnabled(false);
		else
			this.botones[0].setEnabled(true);
	}
}
