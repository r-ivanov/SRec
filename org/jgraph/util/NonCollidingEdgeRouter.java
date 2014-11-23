package org.jgraph.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jgraph.JGraph;
import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.AttributeMap.SerializablePoint2D;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.Edge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.DefaultEdge.LoopRouting;
import org.jgraph.graph.PortView;
import org.w3c.dom.views.AbstractView;

/**
 * Implementación que permite evitar que las aristas pasen por encima de los
 * nodos.
 */
public class NonCollidingEdgeRouter extends LoopRouting {
	
	private JGraph graph;
	
	public NonCollidingEdgeRouter(JGraph graph) {
		this.graph = graph;
	}

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
			// System.out.println("EdgeView has no source or target view : "
			// + edge.toString());
			return null;
		}
		if (nodeFrom == nodeTo) {
			// System.out.println("nodeFrom and NodeTo are the same cell view");
			return null;
		}

//		List points = edge.getPoints();
//		Object startPort = points.get(0);
//		Object endPort = points.get(points.size() - 1);

		Point2D nodeFromLocation = AbstractCellView.getCenterPoint(nodeFrom);
		Point2D nodeToLocation = AbstractCellView.getCenterPoint(nodeTo);

		System.out.println("startPortPoint: " + nodeFromLocation);
		System.out.println("endPortPoint: " + nodeToLocation);
		boolean intersects = obtenerCeldasEntrePuntos(cache, edge, nodeFromLocation, nodeToLocation);
		System.out.println("==================================");
		System.out.println("");

		newPoints.add(nodeFrom);
		if (intersects) {
			newPoints.add(new Point2D.Double(nodeFromLocation.getX(), nodeFromLocation.getY() - 50));
		}
		newPoints.add(nodeTo);
		return newPoints;
	}

	private boolean obtenerCeldasEntrePuntos(GraphLayoutCache cache,
			EdgeView edgeView, Point2D from, Point2D to) {

		List<CellView> celdasEntrePuntos = new ArrayList<CellView>();
		
		boolean intersects = false;
		
		GraphModel model = cache.getModel();
		CellView[] celdas = cache.getAllViews();

		for (int i = 0; i < celdas.length; i++) {
			Object modeloCelda = celdas[i].getCell();
			if (DefaultGraphModel.isVertex(model, modeloCelda) && !celdas[i].getCell().equals(edgeView.getSource().getParentView().getCell())
					&& !celdas[i].getCell().equals(edgeView.getTarget().getParentView().getCell())) {				
				intersects =  celdas[i].getBounds().intersectsLine(from.getX(), from.getY(), to.getX(), to.getY());
				System.out.println("Intersecciona?: " + intersects);
				if (intersects) {
					System.out.println("Intersecciona!!!!!");
				}
				
			}
		}

		return intersects;
	}

//	/**
//	 * Determina el vertice de la celda por el que la arista debe bordear para
//	 * evitar la colision con la celda.
//	 * 
//	 * @param cellView
//	 *            Celda
//	 * @param edgeStart
//	 *            Punto de comienzo de la arista
//	 * @param edgeEnd
//	 *            Punto final de la arista
//	 * 
//	 * @return Punto para evitar colision, null si no hay colision.
//	 */
//	private boolean interseccionAristaConCelda(CellView cellView,
//			Point2D edgeStart, Point2D edgeEnd) {
//		
//		if (edgeStart.getY() == edgeEnd.getY()) {
//			if (cellView.edgeStart.getY())
//		}
//		
//		double interseccionSuperior;
//		double intersectionInferior;
//		double puntoSuperior;
//		double puntoInferior;
//
//		// Calcular m y c para la ecuacuón de la recta (y = mx+c)
//		double m = (edgeEnd.getY() - edgeStart.getY()) / (edgeEnd.getX() - edgeStart.getX());
//		double c = edgeStart.getY() - (m * edgeStart.getX());
//
//		if (m > 0) {
//			interseccionSuperior = (m * cellView.getBounds().getX() + c);
//			intersectionInferior = (m * (cellView.getBounds().getX() + cellView.getBounds().getWidth()) + c);
//		} else {
//			interseccionSuperior = (m * (cellView.getBounds().getX() + cellView.getBounds().getWidth()) + c);
//			intersectionInferior = (m * cellView.getBounds().getX() + c);
//		}
//
//		if (edgeStart.getY() < edgeEnd.getY()) {
//			puntoSuperior = edgeStart.getY();
//			puntoInferior = edgeEnd.getY();
//		} else {
//			puntoSuperior = edgeEnd.getY();
//			puntoInferior = edgeStart.getY();
//		}
//
//		double topOverlap;
//		double botOverlap;
//		
//		topOverlap = interseccionSuperior > puntoSuperior ? interseccionSuperior : puntoSuperior;
//		botOverlap = intersectionInferior < puntoInferior ? intersectionInferior
//				: puntoInferior;
//
//		return (topOverlap < botOverlap)
//				&& (!((botOverlap < cellView.getBounds().getY()) || (topOverlap > cellView.getBounds().getY() + cellView.getBounds().getHeight())));
//
//	}

}
