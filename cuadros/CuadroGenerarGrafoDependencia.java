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
			String nombreMetodo = null;
			for (int i = 0; i < this.botonesMetodos.length; i++) {
				if (this.botonesMetodos[i].isSelected()) {
					nombreMetodo = this.botonesMetodos[i].getText();
					break;
				}
			}
			
			GrafoDependencia grafoDependencia = new GrafoDependencia(this.ventana.trazaCompleta, nombreMetodo);
			
			MatrizDinamica<NodoGrafoDependencia> matriz = grafoDependencia.obtenerMatrizPorDefecto();
			
			for (int i = 0; i < matriz.numFilas(); i++) {
				for (int j = 0; j < matriz.numColumnas(); j++) {
					NodoGrafoDependencia nodo = matriz.get(i, j);
					if (nodo != null) {
						System.out.print(matriz.get(i, j).getParams()[0] + " ");
					} else {
						System.out.print("  ");
					}
				}
				System.out.println();
			}
			
			DefaultGraphModel model = new DefaultGraphModel();
			GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
			final JGraph grafo = new JGraph(model, view);
			
			for (NodoGrafoDependencia nodo : grafoDependencia.obtenerNodos()) {
				grafo.getGraphLayoutCache().insert(nodo.obtenerCeldasDelNodoParaGrafo().toArray());
			}
			
			grafo.setBackground(Conf.colorPanel);		
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
