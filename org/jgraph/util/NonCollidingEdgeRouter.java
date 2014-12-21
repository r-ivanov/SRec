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
		
		List<Rectangle2D> rectangulos = new ArrayList<Rectangle2D>();
		
		CellView sourceCell = edgeView.getSource().getParentView();
		CellView targetCell = edgeView.getTarget().getParentView();
		
		for (int i = 0; i < celdas.length; i++) {
			Object modeloCelda = celdas[i].getCell();
			if (DefaultGraphModel.isVertex(model, modeloCelda) &&
					!celdas[i].getCell().equals(sourceCell.getCell()) &&
					!celdas[i].getCell().equals(targetCell.getCell()) &&
					GraphConstants.getCollisionMargin(celdas[i].getAllAttributes()) >= 0) {
				rectangulos.add(celdas[i].getBounds());
			}
		}
		
		double margenColision = GraphConstants.getCollisionMargin(sourceCell.getAllAttributes());
		
		rectangulos = resolverColisionesEntreRectangulos(rectangulos, margenColision * 2);	
		List<Rectangle2D> copiaRectangulos = new ArrayList<Rectangle2D>(rectangulos);
		for (Rectangle2D rectangulo : copiaRectangulos) {
			if (this.obtenerRectanguloConMargen(sourceCell.getBounds(), margenColision).intersects(rectangulo) ||
					this.obtenerRectanguloConMargen(targetCell.getBounds(), margenColision).intersects(rectangulo)) {
				rectangulos.remove(rectangulo);
			}
		}
		
		List<Point2D> verticesArista = this.obtenerVerticesParaArista(rectangulos, from, to, margenColision);
		
		/* Eliminamos vertices innecesarios */
		List<Point2D> verticesFinales = new ArrayList<Point2D>(verticesArista);
		for (int i = 1; i < verticesArista.size() - 1; i++) {
			Point2D verticeAnterior = verticesArista.get(i-1);
			Point2D verticeActual = verticesArista.get(i);
			Point2D verticeSiguiente = verticesArista.get(i+1);
			if ((verticeAnterior.getX() == verticeActual.getX() && verticeActual.getX() == verticeSiguiente.getX()) ||
					(verticeAnterior.getY() == verticeActual.getY() && verticeActual.getY() == verticeSiguiente.getY())) {
				verticesFinales.remove(verticeActual);
			}
		}
		
		return verticesFinales;
	}
	
	private List<Rectangle2D> resolverColisionesEntreRectangulos(List<Rectangle2D> rectangulos, double margenColision) {
		List<Rectangle2D> rectangulosProcesados = new ArrayList<Rectangle2D>(rectangulos);
		for (Rectangle2D rectangulo : rectangulos) {
			Rectangle2D rectanguloConMargenColision = this.obtenerRectanguloConMargen(rectangulo, margenColision);
			for (Rectangle2D otroRectangulo : rectangulos) {
				Rectangle2D otroRectanguloConMargenColision = this.obtenerRectanguloConMargen(otroRectangulo, margenColision);
				if (rectangulo != otroRectangulo && rectanguloConMargenColision.intersects(otroRectanguloConMargenColision)) {
					rectangulosProcesados.remove(rectangulo);
					rectangulosProcesados.remove(otroRectangulo);
					rectangulosProcesados.add(rectangulo.createUnion(otroRectangulo));
					return resolverColisionesEntreRectangulos(rectangulosProcesados, margenColision);
				}
			}
		}
		return rectangulosProcesados;
	}
	
	private Rectangle2D obtenerRectanguloConMargen(Rectangle2D rectangulo, double margen) {
		return new Rectangle2D.Double(
				rectangulo.getX() - margen,
				rectangulo.getY() - margen,
				rectangulo.getWidth() + margen,
				rectangulo.getHeight() + margen);
	}
	
	private List<Point2D> obtenerVerticesParaArista(List<Rectangle2D> rectangulos, Point2D puntoOrigen, Point2D puntoDestino, double margenArista) {
		
		List<Point2D> puntosIntermedios = new ArrayList<Point2D>();
		
		boolean hayColisiones = true;
		do {
			Point2D puntoOrigenActual = puntosIntermedios.isEmpty() ? puntoOrigen : puntosIntermedios.get(puntosIntermedios.size() -1);
			Line2D lineaActual = new Line2D.Double(puntoOrigenActual, puntoDestino);
			List<Point2D> puntos = obtenerPuntosDeAristaParaNodoMasCercano(rectangulos, null, lineaActual, margenArista);
			if (puntos.isEmpty()) {
				hayColisiones = false;
			} else {
				puntosIntermedios.addAll(puntos);
			}
		} while (hayColisiones);
		
		return puntosIntermedios;
	}
	
	private List<Point2D> obtenerPuntosDeAristaParaNodoMasCercano(List<Rectangle2D> rectangulos, List<Rectangle2D> rectangulosAnalizados, Line2D linea, double margenArista) {
		
		if (rectangulosAnalizados == null) {
			rectangulosAnalizados = new ArrayList<Rectangle2D>();
		}
		
		List<Point2D> puntosArista = new ArrayList<Point2D>();
		Rectangle2D rectanguloQueColisiona = null;
		double distanciaMasCercana = -1;
		for (Rectangle2D rectangulo : rectangulos) {
			if (linea.intersects(rectangulo.getBounds()) && !rectangulosAnalizados.contains(rectangulo)) {
				Point2D puntoNodoCentro = new Point2D.Double(rectangulo.getCenterX(), rectangulo.getCenterY());
				double distanciaAlNodo = linea.getP1().distance(puntoNodoCentro);
				if (distanciaMasCercana < 0 || distanciaAlNodo < distanciaMasCercana) {
					distanciaMasCercana = distanciaAlNodo;
					rectanguloQueColisiona = rectangulo;
				}
			}
		}
		
		if (rectanguloQueColisiona != null) {
			puntosArista.addAll(obtenerVerticesParaCurva(linea, rectanguloQueColisiona.getBounds(), margenArista));
			Line2D nuevaLineaArista = new Line2D.Double(linea.getP1(), puntosArista.get(puntosArista.size() - 1));
			rectangulosAnalizados.add(rectanguloQueColisiona);
			List<Point2D> nuevosPuntosArista = this.obtenerPuntosDeAristaParaNodoMasCercano(rectangulos, rectangulosAnalizados, nuevaLineaArista, margenArista);
			if (nuevosPuntosArista.isEmpty()) {
				rectangulos.remove(rectanguloQueColisiona);
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
