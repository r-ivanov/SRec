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
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;

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
			
			DefaultGraphCell celda1 = new DefaultGraphCell("3, 4");			
			GraphConstants.setFont(celda1.getAttributes(), new Font("Arial",Font.BOLD,20));
			GraphConstants.setDisconnectable(celda1.getAttributes(), false);
			GraphConstants.setMoveable(celda1.getAttributes(), true);
			GraphConstants.setSelectable(celda1.getAttributes(), true);
			GraphConstants.setResize(celda1.getAttributes(), false);
			GraphConstants.setOpaque(        celda1.getAttributes(), true);		// Normales
			GraphConstants.setForeground(    celda1.getAttributes(), Conf.colorFEntrada);
			GraphConstants.setBackground(celda1.getAttributes(), (Conf.colorC1AEntrada));
			GraphConstants.setGradientColor(celda1.getAttributes(), (Conf.colorC2AEntrada));
			GraphConstants.setBounds(celda1.getAttributes(), new Rectangle(0,0, 75, 40));
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
			GraphConstants.setBounds(celda2.getAttributes(), new Rectangle(100,0, 75, 40));
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
			GraphConstants.setBounds(celda3.getAttributes(), new Rectangle(0,150, 75, 40));
//			GraphConstants.setAutoSize(celda1.getAttributes(), true);
			GraphConstants.setBorder(celda3.getAttributes(), BorderFactory.createBevelBorder(0));
			GraphConstants.setSizeable(celda3.getAttributes(), false);
			DefaultPort port3 = new DefaultPort();
			celda3.add(port3);
			
//			this.portEntrada = new DefaultPort();
//			this.entrada.add(portEntrada);
			
			DefaultEdge arista1 =new DefaultEdge();
			GraphConstants.setLineEnd(arista1.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista1.getAttributes(), true);
			GraphConstants.setSelectable(arista1.getAttributes(),false);
			GraphConstants.setLineWidth(arista1.getAttributes(), Conf.grosorFlecha); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista1.getAttributes(), Conf.colorFlecha);
			arista1.setSource(port1);
			arista1.setTarget(port2);
			
			DefaultEdge arista2 =new DefaultEdge();
			GraphConstants.setLineEnd(arista2.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista2.getAttributes(), true);
			GraphConstants.setSelectable(arista2.getAttributes(),false);
			GraphConstants.setLineWidth(arista2.getAttributes(), Conf.grosorFlecha); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista2.getAttributes(), Conf.colorFlecha);
			arista2.setSource(port1);
			arista2.setTarget(port3);
			
			DefaultEdge arista3 =new DefaultEdge();
			GraphConstants.setLineEnd(arista3.getAttributes(), GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(arista3.getAttributes(), true);
			GraphConstants.setSelectable(arista3.getAttributes(),false);
			GraphConstants.setLineWidth(arista3.getAttributes(), Conf.grosorFlecha); // grosor de línea a  8 puntos
			GraphConstants.setLineColor(arista3.getAttributes(), Conf.colorFlecha);
			arista3.setSource(port2);
			arista3.setTarget(port3);
			
			//System.out.println("i="+i+"  "+(contenedoresHijos[i]==null)+"  "+this.ra.getEntradaCompletaString());
			//System.out.println(" - this.ra.getHijosVisibles()="+this.ra.getHijosVisibles()+
			//		" this.ra.getHijosVisiblesPantalla()="+this.ra.getHijosVisiblesPantalla()+
			//		" this.ra.getNumHijosVisibles()="+this.ra.getNumHijosVisibles());
			
//			try {
			
//			edges[i].setTarget( ((DefaultGraphCell)(contenedoresHijos[i].getPuertoVisibleParaPadre())) );
			
			DefaultGraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
			
			JGraph grafo = new JGraph(model, view);
			grafo.getModel().addGraphModelListener(null);
			
			grafo.setBackground(Conf.colorPanel);
			grafo.getGraphLayoutCache().insert(celda1);
			grafo.getGraphLayoutCache().insert(celda2);
			grafo.getGraphLayoutCache().insert(celda3);
			grafo.getGraphLayoutCache().insert(arista1);
			grafo.getGraphLayoutCache().insert(arista2);
			grafo.getGraphLayoutCache().insert(arista3);
			
			grafo.setSize(800, 600);
				
	        JFrame f = new JFrame();
	        f.setSize(800, 600);
	        JScrollPane sp = new JScrollPane(grafo);
	        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	        f.add(sp);
	        f.pack();
	        f.setVisible(true);

			
			this.dialogo.setVisible(false);
			
		} else if (e.getSource() == this.cancelar) {
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
