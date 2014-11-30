package org.jgraph.util;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.EdgeView;
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
					&& !celdas[i].getCell().equals(edgeView.getTarget().getParentView().getCell())) {				
				nodos.add(celdas[i]);
			}
		}
		
		int totalNodos = nodos.size();
		int margenArista = MARGEN_ARISTA_BASE;
		List<Point2D> verticesParaCurva = new ArrayList<Point2D>();		
		Line2D lineaArista = new Line2D.Double(from, to);
		
		Rectangle2D rectanguloActual = null;
		Rectangle2D rectanguloObtenido = null;
		do {	
			List<Point2D> puntosArista = new ArrayList<Point2D>();
			puntosArista.add(from);
			puntosArista.addAll(verticesParaCurva);
			puntosArista.add(to);
			
			rectanguloActual = rectanguloObtenido;
			rectanguloObtenido = obtenerRectanguloARodear(nodos, rectanguloActual, puntosArista, margenArista);
			if (rectanguloObtenido != null && !rectanguloObtenido.equals(rectanguloActual)) {
				margenArista = (totalNodos - nodos.size()) * MARGEN_ARISTA_BASE;
				verticesParaCurva = obtenerVerticesParaCurva(lineaArista, rectanguloObtenido, margenArista, edgeView);
			}
		
		} while (rectanguloObtenido != null && !rectanguloObtenido.equals(rectanguloActual));
		
		return verticesParaCurva;
	}
	
	private Rectangle2D obtenerRectanguloARodear(List<CellView> nodos, Rectangle2D rectangulo, List<Point2D> puntosArista, double margenArista) {
		
		List<CellView> copiaNodos = new ArrayList<CellView>(nodos);
		for (int i = 0; i < puntosArista.size() - 1; i++) {
			Line2D lineaArista = new Line2D.Double(puntosArista.get(i), puntosArista.get(i + 1));
			for (CellView nodo : copiaNodos) {
				Rectangle2D boundsNodo = nodo.getBounds();
				if (lineaArista.intersects(boundsNodo.getX() - margenArista,
						boundsNodo.getY() - margenArista,
						boundsNodo.getWidth() + margenArista,
						boundsNodo.getHeight() + margenArista)) {
					
					if (rectangulo == null) {
						rectangulo = new Rectangle2D.Double(nodo.getBounds().getX(), nodo.getBounds().getY(),
								nodo.getBounds().getWidth(), nodo.getBounds().getHeight());
					} else {
						rectangulo = rectangulo.createUnion(nodo.getBounds());
					}
					nodos.remove(nodo);
				}
			}
		}
		
		return rectangulo;
	}
	
	private List<Point2D> obtenerVerticesParaCurva(Line2D lineaArista, Rectangle2D rectangulo, double margenArista, EdgeView edge) {
		
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
