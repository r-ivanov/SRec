package datos;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.util.NonCollidingEdgeRouter;

import conf.Conf;

public class NodoGrafoDependencia {
	
	private static final Edge.Routing edgeRouter;
	static {
		edgeRouter = new NonCollidingEdgeRouter();
	}
	
	private List<NodoGrafoDependencia> dependencias;
	private RegistroActivacion registroActivacion;
	
	private DefaultGraphCell celdaGrafo;
	private DefaultPort portCelda;
	private List<DefaultEdge> aristas;
	
	public NodoGrafoDependencia(RegistroActivacion registroActivacionAsociado) {
		this.dependencias = new ArrayList<NodoGrafoDependencia>();
		this.aristas = new ArrayList<DefaultEdge>();
		this.registroActivacion = registroActivacionAsociado;
		
		String repEntrada = registroActivacionAsociado.getEntrada().getRepresentacion();
		if (repEntrada.length() < 3) {
			repEntrada = "  " + repEntrada + "  ";
		}

		this.celdaGrafo = new DefaultGraphCell(repEntrada);
		GraphConstants.setFont(this.celdaGrafo.getAttributes(), new Font("Arial", Font.BOLD, 20));
		GraphConstants.setDisconnectable(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setMoveable(this.celdaGrafo.getAttributes(), true);
		GraphConstants.setSelectable(this.celdaGrafo.getAttributes(), true);
		GraphConstants.setResize(this.celdaGrafo.getAttributes(), false);
		GraphConstants.setOpaque(this.celdaGrafo.getAttributes(), true);
		GraphConstants.setForeground(this.celdaGrafo.getAttributes(), Conf.colorFEntrada);
		GraphConstants.setBackground(this.celdaGrafo.getAttributes(), (Conf.colorC1AEntrada));
		GraphConstants.setGradientColor(this.celdaGrafo.getAttributes(), (Conf.colorC2AEntrada));
		//GraphConstants.setBounds(celda1.getAttributes(), new Rectangle(200, 200, 75, 40));
		GraphConstants.setAutoSize(this.celdaGrafo.getAttributes(), true);
		GraphConstants.setBorder(this.celdaGrafo.getAttributes(), BorderFactory.createBevelBorder(0));
		GraphConstants.setSizeable(this.celdaGrafo.getAttributes(), false);
		this.portCelda = new DefaultPort();
		this.celdaGrafo.add(this.portCelda);
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
		GraphConstants.setLineWidth(arista.getAttributes(), 1);
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
