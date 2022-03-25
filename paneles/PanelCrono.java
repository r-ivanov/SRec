package paneles;

import grafica.ContenedorCronologica;
import grafica.EtiquetaFlotante;
import opciones.GestorOpciones;
import opciones.OpcionConfVisualizacion;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JViewport;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import utilidades.NombresYPrefijos;
import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroInfoNodo;
import datos.Estado;
import datos.RegistroActivacion;
import eventos.NavegacionListener;

/**
 * Panel que contendrá la visualización de la vista Cronológica del algoritmo.
 * 
 * @author Luis Fernández y Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class PanelCrono extends JPanel implements ActionListener, KeyListener,
MouseListener, MouseMotionListener {
	
	static GestorOpciones gOpciones = new GestorOpciones();
	
	private static OpcionConfVisualizacion ocv  = (OpcionConfVisualizacion) gOpciones.getOpcion(
			"OpcionConfVisualizacion", false);
	static final long serialVersionUID = 04;

	private JGraph graph;
	
	

	private int TamTrazaResult =ocv.getTamFuenteTraza();
	private JViewport vp;
	private ContenedorCronologica cc;

	private Object[] celdas = new Object[0];

	private RegistroActivacion ra;
	private NombresYPrefijos nyp;

	private JPanel panel;

	private double escalaOriginal;
	private double escalaActual;

	private int zoom = 0;

	private JMenuItem opcionesMenuContextual[];

	private int ratonX = 0, ratonY = 0, ratonXAbs = 0, ratonYAbs = 0;

	private NavegacionListener nl = null;

	private boolean haEntradoAhora = true;

	/**
	 * Constructor: crea un nuevo panel cronológico.
	 * 
	 * @param nyp
	 *            Nombres y prefijos para mostrar.
	 */
	public PanelCrono(NombresYPrefijos nyp) throws Exception {
		/*public void paint(Graphics g){
	        super.paint(g);
	        
	        g.setColor(Color.BLUE);
	        g.drawLine(0, 70, 100, 70);
	        g.drawRect(150, 70, 50, 70);
	        g.drawRoundRect(250, 70, 50, 70, 6, 6);
	        g.drawOval(350, 70, 50, 70);
	        
	        int vectorX [] = {500, 550, 450};
	        int vectory [] = {70, 120, 120};
	        g.drawPolygon(vectorX, vectory, 3);
	        
	        g.setColor(Color.GREEN);
	        g.fillRect(150, 270, 50, 70);
	        g.fillRoundRect(250, 270, 50, 70, 6, 6);
	        g.fillOval(350, 270, 50, 70);
	        
	        int[] vectorx2 = {500, 550, 450};
	        int[] vectory2 = {270, 320, 320};
	        g.fillPolygon(vectorx2, vectory2, 3);
	        
	    }*/
		
		this.nyp = nyp;
		if (Ventana.thisventana.traza != null) {
			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,
					new DefaultCellViewFactory());
			this.graph = new JGraph(model, view);

			this.graph.getModel().addGraphModelListener(null);
			try {

				this.cc = new ContenedorCronologica(this.nyp);
				this.graph.getGraphLayoutCache().insert(this.cc.getCeldas());
				this.celdas = this.cc.getCeldas();
				this.graph.getGraphLayoutCache().insert(this.celdas);
			
				
				this.graph.addMouseListener(this);
				this.graph.addMouseMotionListener(this);
				this.graph.setBackground(Conf.colorPanel);

				BorderLayout bl = new BorderLayout();
				this.setLayout(bl);
				this.escalaOriginal = this.graph.getScale(); // Sólo es
				// necesario
				// hacerlo una
				// vez

				this.panel = new JPanel();
				this.panel.add(this.graph);
				this.add(this.panel, BorderLayout.NORTH);

				try {
					this.visualizar();
				} catch (OutOfMemoryError oome) {
					this.graph = null;
					throw oome;
				} catch (Exception e) {
					throw e;
				}

			} catch (OutOfMemoryError oome) {
				this.cc = null;
				this.graph = null;
				throw oome;
			} catch (Exception e) {
				this.cc = null;
				throw e;
			}
		} else {
			this.add(new JPanel(), BorderLayout.CENTER);
		}

	}

	/**
	 * Visualiza y redibuja la vista cronológica en el panel.
	 */
	public void visualizar() {
		if (Ventana.thisventana.traza != null) {
			GraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,
					new DefaultCellViewFactory());
			this.graph = new JGraph(model, view);

			this.graph.getModel().addGraphModelListener(null);
			try {
				this.cc = new ContenedorCronologica(this.nyp);
				this.celdas = this.cc.getCeldas();
				this.graph.getGraphLayoutCache().insert(this.celdas);
				this.graph.addMouseListener(this);
				this.graph.setScale(this.escalaActual);
				this.graph.setBackground(Conf.colorPanel);

				this.setBackground(Conf.colorPanel);
//Codigo para añadir texto a vista de traza en DyV
				BorderLayout bl2 = new BorderLayout();
				this.setLayout(bl2);
				String lineasTraza22[];
				String  lineasTrazaSalLigEnt[];
				 lineasTraza22 = Ventana.thisventana.traza.getLineasTraza();
				
				 lineasTrazaSalLigEnt = Ventana.thisventana.traza.getLineasTrazaSalidaLigadaEntrada();
				 JLabel resultado = new JLabel ();


				 String tipo= Ventana.thisventana.claseAlgoritmo.getMetodoPrincipal().getTipo();
		        JPanel panelTraza = new JPanel();
				this.panel = new JPanel();
				this.panel.add(this.graph,BorderLayout.SOUTH);
				
				this.panel.setBackground(Conf.colorPanel);
				this.removeAll();
				this.add(this.panel, BorderLayout.SOUTH);
				if(!tipo.equals("void")) {
					String textoSalida = Texto.get("TR_S", Conf.idioma);
					String textoEntrada = Texto.get("TR_E", Conf.idioma);

					Ventana.thisventana.trazaCompleta.todoVisible();
					String lineasTraza[] = Ventana.thisventana.trazaCompleta
							.getLineasTrazaSalidaLigadaEntrada();
					String lineasTraza2[] = Ventana.thisventana.trazaCompleta
							.getLineasTrazaSalidaLigadaEntrada();
					String lineasTrazaParcial[] = Ventana.thisventana.traza
							.getLineasTrazaSalidaLigadaEntrada();
					String lineasTrazaParcial2[] = Ventana.thisventana.traza
							.getLineasTrazaSalidaLigadaEntrada();

					int numNoHistoricos = 0;
					if (Conf.historia == 2) // Si nodos históricos son eliminados
					{
						for (int i = 0; i < lineasTraza.length; i++) {
							if (!lineasTraza[i].contains("<hist>")) {
								numNoHistoricos++;
							}
						}

						String lineasTrazaNoHistoricas[] = new String[numNoHistoricos];

						numNoHistoricos = 0;
						for (int i = 0; i < lineasTraza.length; i++) {
							if (!lineasTraza[i].contains("<hist>")) {
								lineasTrazaNoHistoricas[numNoHistoricos] = lineasTraza[i];
								numNoHistoricos++;
							}
						}
						lineasTraza = lineasTrazaNoHistoricas;
					}

					BorderLayout bl = new BorderLayout();
					GridLayout gl = new GridLayout(lineasTraza.length +1, 1);
					JPanel panelEtiquetas = new JPanel();
					panelEtiquetas.setLayout(gl);
					JLabel etiquetas[] = new JLabel[lineasTraza.length];

					boolean siguiente = true;
					boolean visible = false;
					String valor = "";
					int iParcial = 1;
					for (int i = 0; i < lineasTraza.length; i++) {
						if (lineasTraza[i].contains("<hist>")) {
							etiquetas[i] = new JLabel(lineasTraza[i].substring(0,
									lineasTraza[i].indexOf("<hist>")));
							lineasTraza2[i] = lineasTraza[i].substring(0,
									lineasTraza[i].indexOf("<hist>") - 1);
						} else {
							etiquetas[i] = new JLabel(lineasTraza[i]);
						}

						if (lineasTraza2[i].contains("?")) {
							lineasTraza2[i] = lineasTraza2[i].substring(0,
									lineasTraza2[i].indexOf("?"));
						}
						if (lineasTraza2[i].equals(valor)) {
							valor = "";
							visible = true;
							siguiente = true;
							lineasTraza[i] = lineasTrazaParcial2[iParcial - 1];
						} else {
							visible = false;
						}
						if (i == 0) {
							valor = "";
							visible = true;
							siguiente = true;
						}
						if (lineasTraza2[i].contains("?")) {
							lineasTraza2[i] = lineasTraza[i].substring(0,
									lineasTraza[i].indexOf("?"));
						}
						if ((iParcial < lineasTrazaParcial.length) && (siguiente)) {
							if (lineasTrazaParcial[iParcial].contains("?")) {
								lineasTrazaParcial[iParcial] = lineasTrazaParcial[iParcial]
										.substring(0,
												lineasTrazaParcial[iParcial].indexOf("?"));
							}
							if (lineasTrazaParcial[iParcial].contains("<hist>")) {
								lineasTrazaParcial[iParcial] = lineasTrazaParcial[iParcial]
										.substring(0, lineasTrazaParcial[iParcial]
												.indexOf("<hist>") - 1);
							}
							valor = lineasTrazaParcial[iParcial];
							siguiente = false;
							iParcial++;
						}

						if (lineasTraza[i].contains("entra")) // Si son líneas de entrada
						{
							lineasTraza[i] = lineasTraza[i].replace("entra",
									textoEntrada);

							if (lineasTraza[i].contains("<ilum>")) // Si hay que iluminar
							{
								etiquetas[i].setForeground(Conf.colorIluminado);
							} else if (lineasTraza[i].contains("<hist>")
									&& Conf.historia == 1) // Si hay que atenuar
							{
								if (Conf.modoColor == 1) {
									etiquetas[i].setForeground(Conf.colorC1AEntrada);
								} else {
									int numColor = Integer.parseInt(lineasTraza[i]
											.substring(lineasTraza[i].indexOf("?") + 1,
													lineasTraza[i].lastIndexOf("?")));
									etiquetas[i].setForeground(Conf.coloresNodoA[numColor]);
								}
							} else {
								if (Conf.modoColor == 1) {
									etiquetas[i].setForeground(Conf.colorC1Entrada);
								} else {
									int numColor = Integer.parseInt(lineasTraza[i]
											.substring(lineasTraza[i].indexOf("?") + 1,
													lineasTraza[i].lastIndexOf("?")));
									etiquetas[i].setForeground(Conf.coloresNodo[numColor]);
								}
							}
						} else // Si son líneas de salida
						{
							lineasTraza[i] = lineasTraza[i].replace("sale",
									textoSalida);
							if (lineasTraza[i].contains("<ilum>")) // Si hay que iluminar
							{
								etiquetas[i].setForeground(Conf.colorIluminado);
							} else if (lineasTraza[i].contains("<hist>")
									&& Conf.historia == 1) // Si hay que atenuar
							{
								if (Conf.modoColor == 1) {
									etiquetas[i].setForeground(Conf.colorC1ASalida);
								} else {
									int numColor = Integer.parseInt(lineasTraza[i]
											.substring(lineasTraza[i].indexOf("?") + 1,
													lineasTraza[i].lastIndexOf("?")));
									etiquetas[i].setForeground(Conf.coloresNodoA[numColor]);
								}
							} else {
								if (Conf.modoColor == 1) {
									etiquetas[i].setForeground(Conf.colorC1Salida);
								} else {
									int numColor = Integer.parseInt(lineasTraza[i]
											.substring(lineasTraza[i].indexOf("?") + 1,
													lineasTraza[i].lastIndexOf("?")));
									etiquetas[i].setForeground(Conf.coloresNodo[numColor]);
								}
							}
						}
						if (lineasTraza[i].contains("?")) {
							lineasTraza[i] = lineasTraza[i].substring(0,
									lineasTraza[i].lastIndexOf("?"));
							lineasTraza[i] = lineasTraza[i].substring(0,
									lineasTraza[i].lastIndexOf("?"));
						}

						lineasTraza[i] = lineasTraza[i].replace("<hist>", "");
						lineasTraza[i] = lineasTraza[i].replace("<ilum>", "");
						String nombreFuente = ocv.getFuenteTraza();
						 
						etiquetas[i].setText(lineasTraza[i]);
						Font font = new Font(nombreFuente, Font.PLAIN, this.TamTrazaResult);
						etiquetas[i].setFont(font);
					//	etiquetas[i].setFont(Conf.fuenteTraza);
						etiquetas[i].setVisible(visible);
						visible = false;
						panelEtiquetas.add(etiquetas[i]);
					}
					//End codigo para añadir traza de texto
					GridLayout gl2 = new GridLayout(2, 1);
					this.setLayout(bl);
					this.add(panelEtiquetas, BorderLayout.NORTH);
					this.add(graph,BorderLayout.CENTER);
					

					this.setBackground(Conf.colorPanel);
					panelEtiquetas.setBackground(Conf.colorPanel);
					this.panel.updateUI();
					this.updateUI();
				}
					
			} catch (OutOfMemoryError oome) {
				this.cc = null;
				this.graph = null;
				throw oome;
			} catch (Exception e) {
				this.cc = null;
			}
		} else {
			this.add(new JPanel(), BorderLayout.CENTER);
		}

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
	 * Devuelve las dimensiones del grafo visible.
	 * 
	 * @return Array, donde la posición 0 corresponde al ancho, y la 1 al alto.
	 */
	public int[] dimGrafoVisible() {
		int valores[] = new int[2];
		valores[0] = this.cc.maximoAnchoVisible() + 10;
		valores[1] = this.cc.maximoAltoVisible() + 10;

		return valores;
	}

	/**
	 * Devuelve las dimensiones del panel y del grafo.
	 * 
	 * @return Array, donde la posición 0 corresponde al ancho del panel , la 1
	 *         al alto del panel, la 2 al ancho del grafo, y la 3 al alto del
	 *         grafo.
	 */
	public int[] dimPanelYGrafo() {
		int dim[] = new int[4];

		dim[0] = (this.getWidth());
		dim[1] = (this.getHeight());

		dim[2] = (int) (this.graph.getPreferredSize().getWidth());
		dim[3] = (int) (this.graph.getPreferredSize().getHeight());

		return dim;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			JMenuItem item = (JMenuItem) e.getSource();

			// Menú contextual: primera opción: etiqueta detallada
			if (item == this.opcionesMenuContextual[0]) {
				if (this.ra != null && this.haEntradoAhora) {
					new EtiquetaFlotante(this.ratonXAbs, this.ratonYAbs,
							this.ra.getNombreMetodo(),
							this.ra.getEntradaCompletaString(),
							this.ra.getSalidaCompletaString(),
							this.ra.entradaVisible(), this.ra.salidaVisible());
					this.haEntradoAhora = false;
				} else if (this.ra == null) {
					// System.out.print("x");
					this.haEntradoAhora = true;
				}
			}
			// Menú contextual: segunda opción: hacer nodo actual
			else if (item == this.opcionesMenuContextual[1]) {
				if (this.ra != null) {
					this.ra.hacerNodoActual();
					Ventana.thisventana.refrescarOpciones();
				}
			}
			// Menú contextual: tercera opción: cuadro de inforamción sobre nodo
			else if (item == this.opcionesMenuContextual[2]) {
				new CuadroInfoNodo(Ventana.thisventana, this.ra);
			}
			// Menú contextual: cuarta opción: seleccionar/no seleccionar
			else if (item == this.opcionesMenuContextual[3]) {
				boolean valor = !this.ra.estaIluminado();
				if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
					Ventana.thisventana.getTraza().iluminar(
							this.ra.getNumMetodo(),
							this.ra.getEntradasString(), null, valor);
				} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
					Ventana.thisventana.getTraza().iluminar(
							this.ra.getNumMetodo(), null,
							this.ra.getSalidasString(), valor);
				} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {
					Ventana.thisventana.getTraza().iluminar(
							this.ra.getNumMetodo(),
							this.ra.getEntradasString(),
							this.ra.getSalidasString(), valor);
				}

				Ventana.thisventana.refrescarFormato();
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.haEntradoAhora = true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.ratonX = e.getX();
		this.ratonY = e.getY();

		this.ratonXAbs = e.getXOnScreen();
		this.ratonYAbs = e.getYOnScreen();

		this.ra = this.cc.getRegistroPosicion(
				(int) (this.ratonX / this.graph.getScale()),
				(int) (this.ratonY / this.graph.getScale()));

		if (e.getButton() == MouseEvent.BUTTON1) // Boton Izquierda, etiqueta de
			// información
		{
			if (this.ra != null && this.haEntradoAhora) {
				new EtiquetaFlotante(e.getXOnScreen() - 30,
						e.getYOnScreen() - 30, this.ra.getNombreMetodo(),
						this.ra.getEntradaString(), this.ra.getSalidaString(),
						this.ra.entradaVisible(), this.ra.salidaVisible());
				this.haEntradoAhora = false;
			} else if (this.ra == null) {
				this.haEntradoAhora = true;
			}
		} else if (e.getButton() == MouseEvent.BUTTON2) // Boton Centro
		{
			// No hacer nada
		} else if (e.getButton() == MouseEvent.BUTTON3) // Boton Derecha, menú
			// contextual
		{
			// No hacer nada
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Actualiza el valor actual del zoom.
	 * 
	 * @param valor
	 *            Nuevo valor para el zoom.
	 * @param tipo
	 *            Necesario para realizar zoom en resultado Panel crono.
	 */
	public void refrescarZoom(int valor,int tipo) {
		if(tipo==1) {
			this.TamTrazaResult = this.TamTrazaResult+2;
		}else if(tipo==0) {
			this.TamTrazaResult = 12;
		}else if(tipo==-1) {
			this.TamTrazaResult = this.TamTrazaResult-2;
		}
		
		if (valor == 0) {
			
			this.graph.setScale(this.escalaOriginal);
			
		} else if (valor > 0) {
			double v = valor;
			v = v / 100;
			v = v + 1;
			v = v * this.escalaOriginal;
			
			this.graph.setScale(v);
		} else {
			double v = (valor * (-1));
			v = v / 100;
			v = 1 - v;
			v = v * this.escalaOriginal;
		
			this.graph.setScale(v);
		}
		this.escalaActual = this.graph.getScale();
		this.zoom = valor;
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	/**
	 * Devuelve el listener de navegación actual.
	 * 
	 * @return Listener de navegación actual.
	 */
	public NavegacionListener getNavegacionListener() {
		return this.nl;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	/**
	 * Devuelve el ViewPort del panel.
	 * 
	 * @return View port del panel.
	 */
	public JViewport getViewport() {
		return this.vp;
	}

	/**
	 * Deuvelve las celdas que componen el grafo.
	 * 
	 * @return Celdas que componen el grafo.
	 */
	public Object[] getCeldas() {
		return this.celdas;
	}

	/**
	 * Devuelve el grafo de la vista.
	 * 
	 * @return Grafo de la vista.
	 */
	public JGraph getGrafo() {
		return this.graph;
	}

}
