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
import cuadros.CuadroTabularGrafoDependencia;
import datos.DatosMetodoBasicos;
import datos.GrafoDependencia;
import datos.NodoGrafoDependencia;
import utilidades.NombresYPrefijos;
import utilidades.Texto;

/**
 * Panel que contendr� la visualizaci�n de la vista de grafo de dependencia del algoritmo.
 * 
 * @author Daniel Arroyo Cort�s
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

	private String ultimaExpresionParaFila;		//	Expresi�n filas si han tabulado
	private String ultimaExpresionParaColumna;	//	Expresi�n columnas si han tabulado

	private JPanel panel;

	private double escalaOriginal;
	private double escalaActual;
	
	private int zoom = 0;	
	
	private int tipoGrafo = -1;	// 	0 - Grafo normal
								//	1 - Grafo con matriz
								//	2 - Grafo tabulado
	
	private boolean orientacionFlechas = true;	// 	True - Normal
												//	False - Invertidas

	private int numeroFilas;	//	N� filas si han dibujado tabla
	private int numeroColumnas;	//	N� columnas si han dibujado tabla

	private NombresYPrefijos nyp;
	
	private boolean eliminarFilasColumnas = false;
	
	/**
	 * Constructor: crea un nuevo panel de visualizaci�n para el grafo.
	 * 
	 * @param metodo
	 * 		M�todo del que queremos crear el grafo de dependencia
	 * 
	 * @param ventana
	 * 		Ventana a la que est� asociada la pesta�a, es la principal
	 * 		y es necesaria para obtener algunos datos
	 * 
	 * @param nyp
	 * 		Nombres y prefijos, para abreviar nombre de m�todos si est�n visibles
	 *  	y es necesario
	 */
	public PanelGrafo(DatosMetodoBasicos metodo, Ventana ventana, NombresYPrefijos nyp) throws Exception {
		
		if (Ventana.thisventana.traza != null && metodo != null && ventana != null) {			
			
			//	Obtenemos datos b�sicos del grafo y su representaci�n
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
			this.refrescarZoom(-50);	//	Tama�o por defecto del grafo al inicio
			
			//	Creamos pesta�a	y llamamos a visualizar	
				
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
	 * Obtiene el tama�o que ocupa el grafo en pantalla aunque el usuario
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
	 * Dibuja una tabla con un n�mero de filas y columnas.
	 * 
	 * @param filas
	 *            N�mero de filas.
	 * @param columnas
	 *            N�mero de columnas.
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
	 * Tabula autom�ticamente los nodos del grafo, dada una expresi�n para filas
	 * y otra para columnas.
	 * 
	 * @param expresionParaFila 		
	 * 		Expresi�n para las filas
	 * @param expresionParaColumna		
	 * 		Expresi�n para columnas
	 * 
	 * @return Mensaje de error si ocurri� algun error, null en caso contrario.
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
	 * Invierte las flechas del grafo
	 * 
	 * @param visualizar
	 * 		True si queremos visualizar junto con el m�todo los cambios
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
	 * Visualiza y redibuja el grafo en la pesta�a.
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
	 * 	Nombres y prefijos, para abreviar nombre de m�todos si est�n visibles
	 *  y es necesario
	 */
	public void visualizar2(NombresYPrefijos nyp){
		if (Ventana.thisventana.traza != null) {
			
			//	Creamos grafo nuevo siempre, para que cargue la nueva traza
			//		y/o las nuevas opciones de visualizaci�n
			this.grafoDependencia = new GrafoDependencia(this.metodo,nyp);
			this.representacionGrafo = this.grafoDependencia
					.obtenerRepresentacionGrafo(false);	
			
			//	Dejamos las flechas con la orientaci�n que ten�an
			if(!this.orientacionFlechas){
				this.invertirFlechasGrafo(false);
				this.orientacionFlechas = !this.orientacionFlechas;
			}
			
			//	Revisamos el tipo de grafo que ten�amos creado
			switch(this.tipoGrafo){
			
				//	Grafo normal
				case 0:						
									
					this.visualizar();
					break;
				
				//	Grafo con tabla
				case 1:	
					
					//	Se crea tabla con valor almacenado previamente
					this.dibujarTabla(this.numeroFilas, this.numeroColumnas);
					
					//	No se llama a visualizar porque ya est� en dibujar tabla					
					break;
				
				//	Grafo tabulado
				case 2:	
			
					//	Se crea grafo tabulado con valor almacenado previamente
					this.tabular(this.ultimaExpresionParaFila, this.ultimaExpresionParaColumna);
					
					//	No se llama a visualizar porque ya est� en dibujar tabla
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
	 * @return Array de int, donde la posici�n 0 contiene el m�ximo ancho, y la
	 *         posici�n 1 el m�ximo alto.
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
	 * @return Array de int, donde la posici�n 0 corresponde al ancho de la pesta�a, la 1
	 *         al alto de la pesta�a, la 2 al ancho del grafo, y la 3 al alto del
	 *         grafo.
	 */
	public int[] dimPanelYGrafoDep() {
		int dim[] = new int[4];

		dim[0] = (int) (this.getVisibleRect().getWidth()); // Anchura de pesta�a
		dim[1] = (int) (this.getVisibleRect().getHeight()); // Altura de pesta�a

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
	 * Permite refrescar el zoom al valor dado por el par�metro
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

		// Grupo tabulaci�n
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
		JLabel labelSignatura = new JLabel("  -  " + this.metodo.getInterfaz()
				+ "   ");
		labelSignatura.setFont(new Font("Arial", Font.ITALIC, 14));

		JPanel panelInfo = new JPanel(new BorderLayout());
		panelInfo.add(labelTitulo, BorderLayout.WEST);
		panelInfo.add(labelSignatura, BorderLayout.EAST);

		this.panelHerramientas.add(panelInfo, BorderLayout.EAST);

		if(this.tipoGrafo != 2)
			this.botones[3].setEnabled(false);
		if(this.eliminarFilasColumnas)
			this.botones[0].setEnabled(false);
//		this.add(this.panelHerramientas, BorderLayout.NORTH);
	}	

	/**
	 * Permite comparar un m�todo pasado como par�metro con el m�todo
	 * 	que se est� visualizando actualmente en la pesta�a
	 * 
	 * @param nuevoMetodo M�todo a comparar con el actual
	 * @return True si nuevoMetodo = metodo que se visualiza actualmente, False caso contrario
	 */
	public boolean esIgual(DatosMetodoBasicos nuevoMetodo){
		return this.metodo.esIgual(nuevoMetodo);
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
			
		} else if (e.getSource() == this.botones[1]) {		//	Tabular
			new CuadroTabularGrafoDependencia(this.ventana,
					this.metodo.getInterfaz(),
					this.ultimaExpresionParaFila,
					this.ultimaExpresionParaColumna,
					this);			
			this.tipoGrafo = 2;
			this.eliminarFilasColumnas = false;
		}else if (e.getSource() == this.botones[2]) {		//	Flechas
			this.invertirFlechasGrafo(true);
				
		}else if (e.getSource() == this.botones[3]) {		//	Eliminar filas y columnas vac�as
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
}
