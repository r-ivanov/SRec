package datos;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.util.NonCollidingEdgeRouter;

import conf.Conf;

public class NodoGrafoDependencia {
	
	private static final int anchoPixelCaracter = 13;
	private static final int alturaCelda = 26;
	private static final int tamFuente = 20;
	private static final double margenColision = 10;
	
	private static final Edge.Routing edgeRouter;
	static {
		edgeRouter = new NonCollidingEdgeRouter();
	}
	
	private List<NodoGrafoDependencia> dependencias;
	private RegistroActivacion registroActivacion;
	
	private DefaultGraphCell celdaEntrada;
	private DefaultGraphCell celdaSalida;
	private DefaultGraphCell celdaGrafo;
	
	private DefaultPort portCelda;
	private List<DefaultEdge> aristas;
	
	public NodoGrafoDependencia(RegistroActivacion registroActivacionAsociado) {
		this.dependencias = new ArrayList<NodoGrafoDependencia>();
		this.aristas = new ArrayList<DefaultEdge>();
		this.registroActivacion = registroActivacionAsociado;
		this.inicializarRepresentacion();
	}
	
	private void inicializarRepresentacion() {
		
		String repEntrada = this.registroActivacion.getEntrada().getRepresentacion();
		if (repEntrada.length() < 3) {
			repEntrada = "  " + repEntrada + "  ";
		}
		
		String repSalida = this.registroActivacion.getSalida().getRepresentacion();
		if (repSalida.length() < 3) {
			repSalida = "  " + repSalida + "  ";
		}
		
		this.celdaEntrada = new DefaultGraphCell(repEntrada);
		GraphConstants.setDisconnectable(this.celdaEntrada.getAttributes(), false);
		GraphConstants.setMoveable(this.celdaEntrada.getAttributes(), true);
		GraphConstants.setSelectable(this.celdaEntrada.getAttributes(), false);
		GraphConstants.setEditable(this.celdaEntrada.getAttributes(), false);
		GraphConstants.setFont(this.celdaEntrada.getAttributes(), new Font("Arial",Font.BOLD, tamFuente));
		GraphConstants.setForeground(this.celdaEntrada.getAttributes(), Conf.colorFEntrada);
		GraphConstants.setOpaque(this.celdaEntrada.getAttributes(), true);
		GraphConstants.setBackground(
				this.celdaEntrada.getAttributes(),
				(Conf.modoColor == 1 ? Conf.colorC1Entrada : Conf.coloresNodo[this.registroActivacion.getNumMetodo() % 10]));
		GraphConstants.setGradientColor(
				this.celdaEntrada.getAttributes(),
				(Conf.modoColor == 1 ? Conf.colorC2Entrada : Conf.coloresNodo2[this.registroActivacion.getNumMetodo() % 10]));
		this.marcoCelda(this.celdaEntrada.getAttributes());
		
		this.celdaSalida = new DefaultGraphCell(repSalida);
		GraphConstants.setDisconnectable(this.celdaSalida.getAttributes(), false);
		GraphConstants.setMoveable(this.celdaSalida.getAttributes(), true);
		GraphConstants.setSelectable(this.celdaSalida.getAttributes(), false);
		GraphConstants.setEditable(this.celdaSalida.getAttributes(), false);
		GraphConstants.setFont(this.celdaSalida.getAttributes(), new Font("Arial",Font.BOLD, tamFuente));
		GraphConstants.setForeground(this.celdaSalida.getAttributes(), Conf.colorFSalida);
		GraphConstants.setOpaque(this.celdaSalida.getAttributes(), true);
		GraphConstants.setBackground(
				this.celdaSalida.getAttributes(),
				(Conf.modoColor == 1 ? Conf.colorC1Salida : Conf.coloresNodo[this.registroActivacion.getNumMetodo() % 10]));
		GraphConstants.setGradientColor(
				this.celdaSalida.getAttributes(),
				(Conf.modoColor == 1 ? Conf.colorC2Salida : Conf.coloresNodo2[this.registroActivacion.getNumMetodo() % 10]));
		this.marcoCelda(this.celdaSalida.getAttributes());
		
		// Tamaños
		int tamanioCadena = Math.max(repEntrada.length(), repSalida.length()) * anchoPixelCaracter;
		GraphConstants.setBounds(this.celdaEntrada.getAttributes(), new Rectangle(
				0, 0, tamanioCadena, alturaCelda));
		GraphConstants.setBounds(this.celdaSalida.getAttributes(), new Rectangle(
				0, alturaCelda, tamanioCadena, alturaCelda));
		
		
		this.celdaGrafo = new DefaultGraphCell();
		this.celdaGrafo.add(this.celdaEntrada);
		this.celdaGrafo.add(this.celdaSalida);
		GraphConstants.setBounds(this.celdaGrafo.getAttributes(), new Rectangle(
				0, 0, tamanioCadena, alturaCelda * 2));
		GraphConstants.setSize(this.celdaGrafo.getAttributes(), new Dimension(tamanioCadena, alturaCelda * 2));
		GraphConstants.setDisconnectable(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setMoveable(this.celdaGrafo.getAttributes(), true);
		GraphConstants.setSelectable(this.celdaGrafo.getAttributes(), true);
		GraphConstants.setResize(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setOpaque(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setAutoSize(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setSizeable(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setCollisionMargin(this.celdaGrafo.getAttributes(), margenColision);
				
		this.portCelda = new DefaultPort();
		this.celdaGrafo.add(this.portCelda);
	}
	
	/**
	 * Establece el diseño del marco de la celda dado su mapa de atributos.
	 * 
	 * @param am Mapa de atributos de una celda concreta.
	 * @param anular A true si se desea eliminar el borde, false en caso contrario.
	 */
	private void marcoCelda(AttributeMap am) {
		switch (Conf.bordeCelda) {
			case 1:
				GraphConstants
				.setBorder(am, BorderFactory.createBevelBorder(0));
				break;
			case 2:
				GraphConstants
				.setBorder(am, BorderFactory.createEtchedBorder());
				break;
			case 3:
				GraphConstants.setBorder(am,
						BorderFactory.createLineBorder(Conf.colorFlecha));
				break;
			case 4:
				GraphConstants.setBorder(am,
						BorderFactory.createLoweredBevelBorder());
				break;
			case 5:
				GraphConstants.setBorder(am,
						BorderFactory.createRaisedBevelBorder());
				break;
		}
	}
	
	public void setAnchura(double anchura) {
		Rectangle2D bounds = GraphConstants.getBounds(this.celdaGrafo.getAttributes());
		GraphConstants.setBounds(this.celdaGrafo.getAttributes(),
				new Rectangle2D.Double(bounds.getX(), bounds.getY(), anchura, bounds.getHeight()));
		GraphConstants.setBounds(this.celdaEntrada.getAttributes(),
				new Rectangle2D.Double(bounds.getX(), bounds.getY(), anchura, alturaCelda));
		GraphConstants.setBounds(this.celdaSalida.getAttributes(),
				new Rectangle2D.Double(bounds.getX(), bounds.getY() + alturaCelda, anchura, alturaCelda));
	}
	
	public double getAnchura() {	
		return GraphConstants.getSize(this.celdaGrafo.getAttributes()).getWidth();
	}
	
	public void setPosicion(int x, int y) {	
		Rectangle2D bounds = GraphConstants.getBounds(this.celdaGrafo.getAttributes());
		
		int dimX = (int) bounds.getWidth();
		int dimY = (int) bounds.getHeight();
		
		/* Posicion del centro del nodo */
		int nuevaX = x - dimX/2;
		int nuevaY = y - dimY/2;
		
		GraphConstants.setBounds(this.celdaGrafo.getAttributes(),
				new Rectangle(nuevaX, nuevaY, dimX, dimY));
		GraphConstants.setBounds(this.celdaEntrada.getAttributes(),
				new Rectangle(nuevaX, nuevaY, dimX, alturaCelda));
		GraphConstants.setBounds(this.celdaSalida.getAttributes(),
				new Rectangle(nuevaX, nuevaY + alturaCelda, dimX, alturaCelda));
	}

	public boolean equals(NodoGrafoDependencia nodo) {
		return this.clasesParametrosEntradaIguales(nodo) &&
				this.nombreParametrosEntradaIguales(nodo) &&
				this.valorParametrosEntradaIguales(nodo) &&
				this.nombreMetodoIgual(nodo);
	}
	
	public String[] getParams() {
		return this.registroActivacion.getParametros();
	}
	
	private boolean nombreMetodoIgual(NodoGrafoDependencia nodo) {
		return this.registroActivacion.getNombreMetodo().equals(nodo.registroActivacion.getNombreMetodo());
	}
	
	private boolean clasesParametrosEntradaIguales(NodoGrafoDependencia nodo) {
		return this.arraysIguales(this.registroActivacion.clasesParamE(), nodo.registroActivacion.clasesParamE());
	}
	
	private boolean nombreParametrosEntradaIguales(NodoGrafoDependencia nodo) {
		return this.arraysIguales(this.registroActivacion.getNombreParametros(), nodo.registroActivacion.getNombreParametros());
	}
	
	private boolean valorParametrosEntradaIguales(NodoGrafoDependencia nodo) {
		return this.arraysIguales(this.registroActivacion.getParametros(), nodo.registroActivacion.getParametros());
	}
	
	private boolean arraysIguales(String[] array1, String[] array2) {
		boolean iguales = array1.length == array2.length;
		if (iguales) {
			for (int i = 0; i < array1.length; i++) {
				if (!array1[i].equals(array2[i])) {
					iguales = false;
					break;
				}
			}
		}
		return iguales;
	}
	
	public void addDependencia(NodoGrafoDependencia nodo) {		
		DefaultEdge arista = new DefaultEdge();
		GraphConstants.setLineEnd(arista.getAttributes(), GraphConstants.ARROW_CLASSIC);
		GraphConstants.setEndFill(arista.getAttributes(), true);
		GraphConstants.setSelectable(arista.getAttributes(),false);
		GraphConstants.setLineWidth(arista.getAttributes(), Conf.grosorFlecha);
		GraphConstants.setLineColor(arista.getAttributes(), Conf.colorFlecha);
		GraphConstants.setRouting(arista.getAttributes(), edgeRouter);
		GraphConstants.setLineStyle(arista.getAttributes(), GraphConstants.STYLE_SPLINE);
		arista.setSource(this.portCelda);
		arista.setTarget(nodo.portCelda);
		
		this.aristas.add(arista);
		this.dependencias.add(nodo);
	}
	
	public List<GraphCell> obtenerCeldasDelNodoParaGrafo() {
		List<GraphCell> celdas = new ArrayList<GraphCell>();
		celdas.add(this.celdaGrafo);
		celdas.addAll(aristas);
		return celdas;
	}
	
	public List<NodoGrafoDependencia> getDependencias() {
		return this.dependencias;
	}
	
	public boolean contieneDependencia(NodoGrafoDependencia nodoGrafoDependencia) {
		boolean contiene = false;
		for (Iterator<NodoGrafoDependencia> iterator = this.dependencias.iterator(); iterator.hasNext();) {
			if (iterator.next().equals(nodoGrafoDependencia)) {
				contiene = true;
				break;
			}
		}
		return contiene;
	}
}
