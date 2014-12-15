package org.jgraph.util;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;

import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.DefaultEdge.LoopRouting;

/**
 * Implementación que permite evitar que las aristas pasen por encima de los
 * nodos.
 */
public class NonCollidingEdgeRouter extends LoopRouting {
	
	private static final long serialVersionUID = 7596676104954178713L;
	private static final int MARGEN_ARISTA_BASE = 10;

	@Override
	public List routeEdge(GraphLayoutCache cache, EdgeView edge) {
		List newPoints = new ArrayList();

		CellView nodeFrom = edge.getSource();
		CellView nodeTo = edge.getTarget();
		// Check presence of source/target nodes
		if (null == nodeFrom) {
			nodeFrom = edge.getSourceParentView();
		}
		if (null == nodeTo) {
			nodeTo = edge.getTargetParentView();
		}
		if ((null == nodeFrom) || (null == nodeTo)) {
			return null;
		}
		if (nodeFrom == nodeTo) {
			return null;
		}

		Point2D nodeFromLocation = AbstractCellView.getCenterPoint(nodeFrom);
		Point2D nodeToLocation = AbstractCellView.getCenterPoint(nodeTo);
		
		newPoints.add(nodeFrom);
		newPoints.addAll(obtenerPuntosArista(cache, edge, nodeFromLocation, nodeToLocation));
		newPoints.add(nodeTo);
		
		return newPoints;
	}

	private List<Point2D> obtenerPuntosArista(GraphLayoutCache cache, EdgeView edgeView, Point2D from, Point2D to) {
		
		GraphModel model = cache.getModel();
		CellView[] celdas = cache.getAllViews();		
		List<CellView> nodos = new ArrayList<CellView>();
		for (int i = 0; i < celdas.length; i++) {
			Object modeloCelda = celdas[i].getCell();
			if (DefaultGraphModel.isVertex(model, modeloCelda) && !celdas[i].getCell().equals(edgeView.getSource().getParentView().getCell())
					&& !celdas[i].getCell().equals(edgeView.getTarget().getParentView().getCell()) && DefaultGraphModel.isGroup(model, modeloCelda)) {				
				nodos.add(celdas[i]);
			}
		}
				
		return this.obtenerVerticesParaArista(nodos, from, to, MARGEN_ARISTA_BASE);
	}
	
	private List<Point2D> obtenerVerticesParaArista(List<CellView> nodos, Point2D puntoOrigen, Point2D puntoDestino, double margenArista) {
		
		List<Point2D> puntosIntermedios = new ArrayList<Point2D>();
		
		boolean hayColisiones = true;
		do {
			Point2D puntoOrigenActual = puntosIntermedios.isEmpty() ? puntoOrigen : puntosIntermedios.get(puntosIntermedios.size() -1);
			Line2D lineaActual = new Line2D.Double(puntoOrigenActual, puntoDestino);
			List<Point2D> puntos = obtenerPuntosDeAristaParaNodoMasCercano(nodos, null, lineaActual, margenArista);
			if (puntos.isEmpty()) {
				hayColisiones = false;
			} else {
				puntosIntermedios.addAll(puntos);
			}
		} while (hayColisiones);
		
		return puntosIntermedios;
	}
	
	private List<Point2D> obtenerPuntosDeAristaParaNodoMasCercano(List<CellView> nodos, List<CellView> nodosAnalizados, Line2D linea, double margenArista) {
		
		if (nodosAnalizados == null) {
			nodosAnalizados = new ArrayList<CellView>();
		}
		
		List<Point2D> puntosArista = new ArrayList<Point2D>();
		CellView nodoQueColisiona = null;
		double distanciaMasCercana = -1;
		for (CellView nodo : nodos) {
			if (linea.intersects(nodo.getBounds()) && !nodosAnalizados.contains(nodo)) {
				Point2D puntoNodoCentro = AbstractCellView.getCenterPoint(nodo);
				double distanciaAlNodo = linea.getP1().distance(puntoNodoCentro);
				if (distanciaMasCercana < 0 || distanciaAlNodo < distanciaMasCercana) {
					distanciaMasCercana = distanciaAlNodo;
					nodoQueColisiona = nodo;
				}
			}
		}
		
		if (nodoQueColisiona != null) {
			puntosArista.addAll(obtenerVerticesParaCurva(linea, nodoQueColisiona.getBounds(), margenArista));
			Line2D nuevaLineaArista = new Line2D.Double(linea.getP1(), puntosArista.get(puntosArista.size() - 1));
			nodosAnalizados.add(nodoQueColisiona);
			List<Point2D> nuevosPuntosArista = this.obtenerPuntosDeAristaParaNodoMasCercano(nodos, nodosAnalizados, nuevaLineaArista, margenArista);
			if (nuevosPuntosArista.isEmpty()) {
				nodos.remove(nodoQueColisiona);
				return puntosArista;
			} else {
				return nuevosPuntosArista;
			}
		} else {
			return puntosArista;
		}
	}
	
	private List<Point2D> obtenerVerticesParaCurva(Line2D lineaArista, Rectangle2D rectangulo, double margenArista) {
		
		List<Point2D> verticesSuperiores = new ArrayList<Point2D>();
		List<Point2D> verticesInferiores = new ArrayList<Point2D>();
		
		List<Point2D> verticesRectangulo = new ArrayList<Point2D>();
		verticesRectangulo.add(new Point2D.Double(rectangulo.getMinX() - margenArista, rectangulo.getMinY() - margenArista));
		verticesRectangulo.add(new Point2D.Double(rectangulo.getMaxX() + margenArista, rectangulo.getMinY() - margenArista));
		verticesRectangulo.add(new Point2D.Double(rectangulo.getMinX() - margenArista, rectangulo.getMaxY() + margenArista));
		verticesRectangulo.add(new Point2D.Double(rectangulo.getMaxX() + margenArista, rectangulo.getMaxY() + margenArista));
		
		for(Point2D vertice : verticesRectangulo) {
			if (lineaArista.relativeCCW(vertice) > 0) {
				verticesSuperiores.add(vertice);
			} else {
				verticesInferiores.add(vertice);
			}
		}
		
		double distanciaTotalVerticesSuperiores = 0;
		double distantiaTotalVerticesInferiores = 0;	
		for(Point2D vertice : verticesSuperiores) {
			distanciaTotalVerticesSuperiores += lineaArista.ptLineDist(vertice);
		}
		for(Point2D vertice : verticesInferiores) {
			distantiaTotalVerticesInferiores += lineaArista.ptLineDist(vertice);
		}
		
		List<Point2D> verticesADevolver;
		if (distanciaTotalVerticesSuperiores < distantiaTotalVerticesInferiores) {
			verticesADevolver = verticesSuperiores;
		} else {
			verticesADevolver = verticesInferiores;
		}
		
		if (verticesADevolver.size() == 2) {
			Point2D vertice1 = verticesADevolver.get(0);
			Point2D vertice2 = verticesADevolver.get(1);
			
			Point2D verticeMasLejano;
			Point2D verticeMasCercano;
			if (lineaArista.ptLineDist(vertice1) > lineaArista.ptLineDist(vertice2)) {
				verticeMasLejano = vertice1;
				verticeMasCercano = vertice2;
			} else {
				verticeMasLejano = vertice2;
				verticeMasCercano = vertice1;
			}
			
			Line2D lineaVerticeMasLejanoANodoFinal = new Line2D.Double(verticeMasLejano, lineaArista.getP2());
			Line2D lineaNodoInicialAVerticeMasLejano = new Line2D.Double(lineaArista.getP1(), verticeMasLejano);
			
			if (!lineaVerticeMasLejanoANodoFinal.intersects(rectangulo) && !lineaNodoInicialAVerticeMasLejano.intersects(rectangulo)) {
				verticesADevolver.remove(verticeMasCercano);
			} else {
				verticesADevolver.clear();
				if (lineaArista.getP1().distance(vertice1) > lineaArista.getP1().distance(vertice2)) {
					verticesADevolver.add(vertice2);
					verticesADevolver.add(vertice1);
				} else {
					verticesADevolver.add(vertice1);
					verticesADevolver.add(vertice2);
				}
			}			
		}
		
		return verticesADevolver;
	}
}
