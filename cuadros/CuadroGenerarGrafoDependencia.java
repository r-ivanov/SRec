package cuadros;

import grafica.ContenedorArbol;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.util.NonCollidingEdgeRouter;
import org.jgraph.util.ParallelEdgeRouter;

import paneles.PanelArbol;
import paneles.PanelGrafoDependencia;
import conf.*;
import botones.*;
import datos.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite seleccionar un método de entre los distintos activos en la ejecución
 * para generar el correspondiente grafo de dependencia para el mismo.
 * 
 * @author David Pastor Herranz
 */
public class CuadroGenerarGrafoDependencia extends Thread implements
		ActionListener, KeyListener {

	private static final int ANCHO_CUADRO = 275;

	private DatosTrazaBasicos dtb;

	private JRadioButton botonesMetodos[];

	private Ventana ventana;
	private JDialog dialogo;

	private BotonAceptar aceptar = new BotonAceptar();
	private BotonCancelar cancelar = new BotonCancelar();

	private int numeroFilas = 0;

	/**
	 * Genera un nuevo cuadro que permite seleccionar un método de entre los
	 * distintos activos en la ejecución para generar el correspondiente grafo
	 * de dependencia para el mismo.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 * @param dtb
	 *            Datos de traza básicos de la traza en ejecución.
	 */
	public CuadroGenerarGrafoDependencia(Ventana ventana, DatosTrazaBasicos dtb) {
		if (dtb != null) {
			this.dialogo = new JDialog(ventana, true);
			this.ventana = ventana;
			this.dtb = dtb;
			this.start();
		}
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		this.numeroFilas = this.dtb.getNumMetodos();

		this.botonesMetodos = new JRadioButton[this.dtb.getNumMetodos()];

		JPanel panelRadio = new JPanel();
		panelRadio.setLayout(new GridLayout(this.numeroFilas, 1));
//		panelRadio.setBorder(new TitledBorder(Texto.get("CVIS_BORDER",
//				Conf.idioma)));
		panelRadio.setBorder(new TitledBorder("Métodos disponibles"));
		
		ButtonGroup radioButtonGroup = new ButtonGroup();
		for (int i = 0; i < this.dtb.getNumMetodos(); i++) {
			DatosMetodoBasicos dmb = this.dtb.getMetodo(i);
			this.botonesMetodos[i] = new JRadioButton(dmb.getNombre());
			this.botonesMetodos[i].addActionListener(this);
			this.botonesMetodos[i].addKeyListener(this);
			if (i == 0) {
				this.botonesMetodos[i].setSelected(true);
			}
			radioButtonGroup.add(this.botonesMetodos[i]);
			panelRadio.add(this.botonesMetodos[i]);
		}

		// Panel para el botón
		JPanel panelBotones = new JPanel();
		panelBotones.add(this.aceptar);
		panelBotones.add(this.cancelar);

		this.aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar.addActionListener(this);
		this.cancelar.addKeyListener(this);

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelRadio, BorderLayout.NORTH);
		panel.add(panelBotones, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
//		this.dialogo.setTitle(Texto.get("CVIS_VIS", Conf.idioma));
		this.dialogo.setTitle("Generar grafo de dependencia");
		
		if (this.ventana.msWindows) {
			this.dialogo.setSize(ANCHO_CUADRO, this.numeroFilas * 23 + 90);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO,
					this.numeroFilas * 23 + 90);
			this.dialogo.setLocation(coord[0], coord[1]);
		} else {
			this.dialogo.setSize(ANCHO_CUADRO, this.numeroFilas * 23 + 90);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO,
					this.numeroFilas * 23 + 90);
			this.dialogo.setLocation(coord[0], coord[1]);
		}
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.aceptar) {
			// Procesar grafo de dependencia
			
			DefaultGraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
			final JGraph grafo = new JGraph(model, view);
			NonCollidingEdgeRouter edgeRouter = new NonCollidingEdgeRouter(grafo);
			
			DefaultGraphCell celda1 = new DefaultGraphCell("1, 2");			
			GraphConstants.setFont(celda1.getAttributes(), new Font("Arial",Font.BOLD,20));
			GraphConstants.setDisconnectable(celda1.getAttributes(), false);
			GraphConstants.setMoveable(celda1.getAttributes(), true);
			GraphConstants.setSelectable(celda1.getAttributes(), true);
			GraphConstants.setResize(celda1.getAttributes(), false);
			GraphConstants.setOpaque(        celda1.getAttributes(), true);		// Normales
			GraphConstants.setForeground(    celda1.getAttributes(), Conf.colorFEntrada);
			GraphConstants.setBackground(celda1.getAttributes(), (Conf.colorC1AEntrada));
			GraphConstants.setGradientColor(celda1.getAttributes(), (Conf.colorC2AEntrada));
			GraphConstants.setBounds(celda1.getAttributes(), new Rectangle(200, 200, 75, 40));
//			GraphConstants.setAutoSize(celda1.getAttributes(), true);
			GraphConstants.setBorder(celda1.getAttributes(), BorderFactory.createBevelBorder(0));
			GraphConstants.setSizeable(celda1.getAttributes(), false);
			DefaultPort port1 = new DefaultPort();
			celda1.add(port1);
			
			DefaultGraphCell celda2 = new DefaultGraphCell("5, 6");			
			GraphConstants.setFont(celda2.getAttributes(), new Font("Arial",Font.BOLD,20));
			GraphConstants.setDisconnectable(celda2.getAttributes(), false);
			GraphConstants.setMoveable(celda2.getAttributes(), true);
			GraphConstants.setSelectable(celda2.getAttributes(), true);
			GraphConstants.setResize(celda2.getAttributes(), false);
			GraphConstants.setOpaque(        celda2.getAttributes(), true);		// Normales
			GraphConstants.setForeground(    celda2.getAttributes(), Conf.colorFEntrada);
			GraphConstants.setBackground(celda2.getAttributes(), (Conf.colorC1AEntrada));
			GraphConstants.setGradientColor(celda2.getAttributes(), (Conf.colorC2AEntrada));
			GraphConstants.setBounds(celda2.getAttributes(), new Rectangle(400, 200, 75, 40));
//			GraphConstants.setAutoSize(celda1.getAttributes(), true);
			GraphConstants.setBorder(celda2.getAttributes(), BorderFactory.createBevelBorder(0));
			GraphConstants.setSizeable(celda2.getAttributes(), false);
			DefaultPort port2 = new DefaultPort();
			celda2.add(port2);
			
			DefaultGraphCell celda3 = new DefaultGraphCell("1, 4");			
			GraphConstants.setFont(celda3.getAttributes(), new Font("Arial",Font.BOLD,20));
			GraphConstants.setDisconnectable(celda3.getAttributes(), false);
			GraphConstants.setMoveable(celda3.getAttributes(), true);
			GraphConstants.setSelectable(celda3.getAttributes(), true);
			GraphConstants.setResize(celda3.getAttributes(), false);
			GraphConstants.setOpaque(        celda3.getAttributes(), true);		// Normales
			GraphConstants.setForeground(    celda3.getAttributes(), Conf.colorFEntrada);
			GraphConstants.setBackground(celda3.getAttributes(), (Conf.colorC1AEntrada));
			GraphConstants.setGradientColor(celda3.getAttributes(), (Conf.colorC2AEntrada));
			GraphConstants.setBounds(celda3.getAttributes(), new Rectangle(600, 200, 75, 40));
//			GraphConstants.setAutoSize(celda1.getAttributes(), true);
			GraphConstants.setBorder(celda3.getAttributes(), BorderFactory.createBevelBorder(0));
			GraphConstants.setSizeable(celda3.getAttributes(), false);
			DefaultPort port3 = new DefaultPort();
			celda3.add(port3);
			
			DefaultGraphCell celda4 = new DefaultGraphCell("18, 19");			
			GraphConstants.setFont(celda4.getAttributes(), new Font("Arial",Font.BOLD,20));
			GraphConstants.setDisconnectable(celda4.getAttributes(), false);
			GraphConstants.setMoveable(celda4.getAttributes(), true);
			GraphConstants.setSelectable(celda4.getAttributes(), true);
			GraphConstants.setResize(celda4.getAttributes(), false);
			GraphConstants.setOpaque(        celda4.getAttributes(), true);		// Normales
			GraphConstants.setForeground(    celda4.getAttributes(), Conf.colorFEntrada);
			GraphConstants.setBackground(celda4.getAttributes(), (Conf.colorC1AEntrada));
			GraphConstants.setGradientColor(celda4.getAttributes(), (Conf.colorC2AEntrada));
			GraphConstants.setBounds(celda4.getAttributes(), new Rectangle(800, 200, 75, 40));
//			GraphConstants.setAutoSize(celda1.getAttributes(), true);
			GraphConstants.setBorder(celda4.getAttributes(), BorderFactory.createBevelBorder(0));
			GraphConstants.setSizeable(celda4.getAttributes(), false);
			DefaultPort port4 = new DefaultPort();
			celda4.add(port4);
			
			DefaultGraphCell celda5 = new DefaultGraphCell("4, 12");			
			GraphConstants.setFont(celda5.getAttributes(), new Font("Arial",Font.BOLD,20));
			GraphConstants.setDisconnectable(celda5.getAttributes(), false);
			GraphConstants.setMoveable(celda5.getAttributes(), true);
			GraphConstants.setSelectable(celda5.getAttributes(), true);
			GraphConstants.setResize(celda5.getAttributes(), false);
			GraphConstants.setOpaque(        celda5.getAttributes(), true);		// Normales
			GraphConstants.setForeground(    celda5.getAttributes(), Conf.colorFEntrada);
			GraphConstants.setBackground(celda5.getAttributes(), (Conf.colorC1AEntrada));
			GraphConstants.setGradientColor(celda5.getAttributes(), (Conf.colorC2AEntrada));
			GraphConstants.setBounds(celda5.getAttributes(), new Rectangle(1000, 200, 75, 40));
//			GraphConstants.setAutoSize(celda1.getAttributes(), true);
			GraphConstants.setBorder(celda5.getAttributes(), BorderFactory.createBevelBorder(0));
			GraphConstants.setSizeable(celda5.getAttributes(), false);
			DefaultPort port5 = new DefaultPort();
			celda5.add(port5);
			
			DefaultGraphCell celda6 = new DefaultGraphCell("13, 21");			
			GraphConstants.setFont(celda6.getAttributes(), new Font("Arial",Font.BOLD,20));
			GraphConstants.setDisconnectable(celda6.getAttributes(), false);
			GraphConstants.setMoveable(celda6.getAttributes(), true);
			GraphConstants.setSelectable(celda6.getAttributes(), true);
			GraphConstants.setResize(celda6.getAttributes(), false);
			GraphConstants.setOpaque(        celda6.getAttributes(), true);		// Normales
			GraphConstants.setForeground(    celda6.getAttributes(), Conf.colorFEntrada);
			GraphConstants.setBackground(celda6.getAttributes(), (Conf.colorC1AEntrada));
			GraphConstants.setGradientColor(celda6.getAttributes(), (Conf.colorC2AEntrada));
			GraphConstants.setBounds(celda6.getAttributes(), new Rectangle(1200, 200, 75, 40));
//			GraphConstants.setAutoSize(celda1.getAttributes(), true);
			GraphConstants.setBorder(celda6.getAttributes(), BorderFactory.createBevelBorder(0));
			GraphConstants.setSizeable(celda6.getAttributes(), false);
			DefaultPort port6 = new DefaultPort();
			celda6.add(port6);
			
			DefaultGraphCell celda7 = new DefaultGraphCell("18, 19");			
			GraphConstants.setFont(celda7.getAttributes(), new Font("Arial",Font.BOLD,20));
			GraphConstants.setDisconnectable(celda7.getAttributes(), false);
			GraphConstants.setMoveable(celda7.getAttributes(), true);
			GraphConstants.setSelectable(celda7.getAttributes(), true);
			GraphConstants.setResize(celda7.getAttributes(), false);
			GraphConstants.setOpaque(        celda7.getAttributes(), true);		// Normales
			GraphConstants.setForeground(    celda7.getAttributes(), Conf.colorFEntrada);
			GraphConstants.setBackground(celda7.getAttributes(), (Conf.colorC1AEntrada));
			GraphConstants.setGradientColor(celda7.getAttributes(), (Conf.colorC2AEntrada));
			GraphConstants.setBounds(celda7.getAttributes(), new Rectangle(1400, 200, 75, 40));
//			GraphConstants.setAutoSize(celda1.getAttributes(), true);
			GraphConstants.setBorder(celda7.getAttributes(), BorderFactory.createBevelBorder(0));
			GraphConstants.setSizeable(celda7.getAttributes(), false);
			DefaultPort port7 = new DefaultPort();
			celda7.add(port7);
			
//			this.portEntrada = new DefaultPort();
//			this.entrada.add(portEntrada);
			
			DefaultEdge arista1 =new DefaultEdge();
			GraphConstants.setLineEnd(arista1.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista1.getAttributes(), true);
			GraphConstants.setSelectable(arista1.getAttributes(),false);
			GraphConstants.setLineWidth(arista1.getAttributes(), 1); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista1.getAttributes(), Conf.colorFlecha);
			GraphConstants.setRouting(arista1.getAttributes(), edgeRouter);
			GraphConstants.setLineStyle(arista1.getAttributes(), GraphConstants.STYLE_SPLINE);
			arista1.setSource(port1);
			arista1.setTarget(port2);
			
			DefaultEdge arista2 =new DefaultEdge();
			GraphConstants.setLineEnd(arista2.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista2.getAttributes(), true);
			GraphConstants.setSelectable(arista2.getAttributes(),false);
			GraphConstants.setLineWidth(arista2.getAttributes(), 1); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista2.getAttributes(), Conf.colorFlecha);
			GraphConstants.setRouting(arista2.getAttributes(), edgeRouter);
			GraphConstants.setLineStyle(arista2.getAttributes(), GraphConstants.STYLE_SPLINE);
			arista2.setSource(port1);
			arista2.setTarget(port3);
			
			DefaultEdge arista3 =new DefaultEdge();
			GraphConstants.setLineEnd(arista3.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista3.getAttributes(), true);
			GraphConstants.setSelectable(arista3.getAttributes(),false);
			GraphConstants.setLineWidth(arista3.getAttributes(), 1); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista3.getAttributes(), Conf.colorFlecha);
			GraphConstants.setRouting(arista3.getAttributes(), edgeRouter);
			GraphConstants.setLineStyle(arista3.getAttributes(), GraphConstants.STYLE_SPLINE);
			arista3.setSource(port2);
			arista3.setTarget(port3);
			
			DefaultEdge arista4 =new DefaultEdge();
			GraphConstants.setLineEnd(arista4.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista4.getAttributes(), true);
			GraphConstants.setSelectable(arista4.getAttributes(),false);
			GraphConstants.setLineWidth(arista4.getAttributes(), 1); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista4.getAttributes(), Conf.colorFlecha);
			GraphConstants.setRouting(arista4.getAttributes(), edgeRouter);
			GraphConstants.setLineStyle(arista4.getAttributes(), GraphConstants.STYLE_SPLINE);
			arista4.setSource(port1);
			arista4.setTarget(port4);
			
			DefaultEdge arista5 =new DefaultEdge();
			GraphConstants.setLineEnd(arista5.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista5.getAttributes(), true);
			GraphConstants.setSelectable(arista5.getAttributes(),false);
			GraphConstants.setLineWidth(arista5.getAttributes(), 1); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista5.getAttributes(), Conf.colorFlecha);
			GraphConstants.setRouting(arista5.getAttributes(), edgeRouter);
			GraphConstants.setLineStyle(arista5.getAttributes(), GraphConstants.STYLE_SPLINE);
			arista5.setSource(port2);
			arista5.setTarget(port4);
			
			DefaultEdge arista6 =new DefaultEdge();
			GraphConstants.setLineEnd(arista6.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista6.getAttributes(), true);
			GraphConstants.setSelectable(arista6.getAttributes(),false);
			GraphConstants.setLineWidth(arista6.getAttributes(), 1); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista6.getAttributes(), Conf.colorFlecha);
			GraphConstants.setRouting(arista6.getAttributes(), edgeRouter);
			GraphConstants.setLineStyle(arista6.getAttributes(), GraphConstants.STYLE_SPLINE);
			arista6.setSource(port1);
			arista6.setTarget(port5);
			
			DefaultEdge arista7 =new DefaultEdge();
			GraphConstants.setLineEnd(arista7.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista7.getAttributes(), true);
			GraphConstants.setSelectable(arista7.getAttributes(),false);
			GraphConstants.setLineWidth(arista7.getAttributes(), 1); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista7.getAttributes(), Conf.colorFlecha);
			GraphConstants.setRouting(arista7.getAttributes(), edgeRouter);
			GraphConstants.setLineStyle(arista7.getAttributes(), GraphConstants.STYLE_SPLINE);
			arista7.setSource(port2);
			arista7.setTarget(port6);
			
			DefaultEdge arista8 =new DefaultEdge();
			GraphConstants.setLineEnd(arista8.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista8.getAttributes(), true);
			GraphConstants.setSelectable(arista8.getAttributes(),false);
			GraphConstants.setLineWidth(arista8.getAttributes(), 1); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista8.getAttributes(), Conf.colorFlecha);
			GraphConstants.setRouting(arista8.getAttributes(), edgeRouter);
			GraphConstants.setLineStyle(arista8.getAttributes(), GraphConstants.STYLE_SPLINE);
			arista8.setSource(port3);
			arista8.setTarget(port7);
			
			DefaultEdge arista9 =new DefaultEdge();
			GraphConstants.setLineEnd(arista9.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista9.getAttributes(), true);
			GraphConstants.setSelectable(arista9.getAttributes(),false);
			GraphConstants.setLineWidth(arista9.getAttributes(), 1); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista9.getAttributes(), Conf.colorFlecha);
			GraphConstants.setRouting(arista9.getAttributes(), edgeRouter);
			GraphConstants.setLineStyle(arista9.getAttributes(), GraphConstants.STYLE_SPLINE);
			arista9.setSource(port6);
			arista9.setTarget(port7);
			
			//System.out.println("i="+i+"  "+(contenedoresHijos[i]==null)+"  "+this.ra.getEntradaCompletaString());
			//System.out.println(" - this.ra.getHijosVisibles()="+this.ra.getHijosVisibles()+
			//		" this.ra.getHijosVisiblesPantalla()="+this.ra.getHijosVisiblesPantalla()+
			//		" this.ra.getNumHijosVisibles()="+this.ra.getNumHijosVisibles());
			
//			try {
			
//			edges[i].setTarget( ((DefaultGraphCell)(contenedoresHijos[i].getPuertoVisibleParaPadre())) );
			
			grafo.setBackground(Conf.colorPanel);
			grafo.getGraphLayoutCache().insert(celda1);
			grafo.getGraphLayoutCache().insert(celda2);
			grafo.getGraphLayoutCache().insert(celda3);
			grafo.getGraphLayoutCache().insert(celda4);
			grafo.getGraphLayoutCache().insert(celda5);
			grafo.getGraphLayoutCache().insert(celda6);
			grafo.getGraphLayoutCache().insert(celda7);
			grafo.getGraphLayoutCache().insert(arista1);
			grafo.getGraphLayoutCache().insert(arista2);
			grafo.getGraphLayoutCache().insert(arista3);
			grafo.getGraphLayoutCache().insert(arista4);
			grafo.getGraphLayoutCache().insert(arista5);
			grafo.getGraphLayoutCache().insert(arista6);
			grafo.getGraphLayoutCache().insert(arista7);
			grafo.getGraphLayoutCache().insert(arista8);
			grafo.getGraphLayoutCache().insert(arista9);
			
			grafo.setSize(800, 600);
			
			
				
	        final JFrame f = new JFrame();
	        f.setSize(800, 600);
	        final JScrollPane sp = new JScrollPane(grafo);
	        try {				
			    f.add(sp);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	       
	        f.pack();
	        f.setVisible(true);
	        
	        scale = 1.0000000000000001;
	        
	        grafo.getModel().addGraphModelListener(new GraphModelListener() {				
				@Override
				public void graphChanged(GraphModelEvent e) {
					grafo.refreshUI();
				}
			});

			
			this.dialogo.setVisible(false);
			
		} else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		}
	}
	
	double scale = 1.1;

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyPressed(KeyEvent e) {

	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// Procesar grafo de dependencia
			this.dialogo.setVisible(false);
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}

}
