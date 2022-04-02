package paneles;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import conf.Conf;
import datos.RegistroActivacion;
import utilidades.Texto;
import ventanas.Ventana;

/**
 * Panel que contendrá la visualización de la vista de valores de la rama
 * a la que pertenece el nodo seleccionado. 
 * La vista es para algoritmos de optimización basados en ramificación y poda.
 * 
 * @author Roumen Ivanov Andreev
 * @version 2021-2022
 */
public class PanelValoresRamaAABB extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private RegistroActivacion ra;
	private RegistroActivacion nodoActual = null;
	private JPanel panel = new JPanel();
	private static int numNodo = 0;
	
	// nodoActual encontrado -> paramos busqueda recursiva
	private boolean encontrado; 
	
	private JFreeChart chart;
	private JPanel chartPanel;
	
	private static final Shape circle = new Ellipse2D.Double(-3, -3, 6, 6);
	
	// Color de las lineas
	private Color solParcialColor = Color.YELLOW;
	private Color solMejorColor = Color.GREEN;
	private Color cotaColor = Color.RED;
	// Grosor de las lineas
	private BasicStroke solParcialStroke = new BasicStroke(1.0f);
	private BasicStroke solMejorStroke = new BasicStroke(1.0f);
	private BasicStroke cotaStroke = new BasicStroke(1.0f);
	
	private static Set<Integer> hojas;
	private static Set<Integer> podas;
	private static int numItem;

	private HashMap<Integer, Integer> mapa;
	
    private class MyRenderer extends XYLineAndShapeRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
	    public Shape getItemShape(int row, int column) {
        	Shape shape;
        	if(PanelValoresRamaAABB.podas.contains(column-numItem+1)) {
            	shape = ShapeUtilities.createDiagonalCross(Conf.grosorSolParc + 4.0f, 1);
        	}else if(PanelValoresRamaAABB.hojas.contains(column-numItem+1)) {
            	//shape = ShapeUtilities.createDiagonalCross(Conf.grosorSolParc+4.0f, 1);
            	shape = ShapeUtilities.createDiamond(Conf.grosorSolParc + 5.0f);
            }else {
            	shape = lookupSeriesShape(row);
            }
			
	        return shape;
	    }
    }
	
	/**
	 * Constructor: crea un nuevo panel de visualización para los 
	 * Valores de rama.
	 */
	public PanelValoresRamaAABB() {
		visualizar();
	}

	/**
	 * Visualiza y redibuja la grafica en el panel
	 */
	public void visualizar() {
		if (Ventana.thisventana.traza != null) {
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	initAndShow();
	            }
	        });
		}

		removeAll();
		add(panel, BorderLayout.NORTH);
		setBackground(Conf.colorPanel);

		updateUI();
	}
	
	private void initAndShow() {
        chartPanel = createChartPanel();
        int width = (int) Math.round(getWidth()*0.5);
        int height = (int) Math.round(getHeight()*0.9);
        chartPanel.setPreferredSize(new Dimension(width, height));
        chartPanel.updateUI();
		
		panel.removeAll();
		panel.setLayout(new java.awt.BorderLayout());
		panel.add(chartPanel, BorderLayout.CENTER);
		panel.updateUI();
	}
	
	private JPanel createChartPanel() {
		XYSeriesCollection dataset = createDataset();

		String chartTitle = Texto.get("V_RAMA_VAL", Conf.idioma);
	    String xAxisLabel = Texto.get("PVG_NODOS", Conf.idioma);
	    String yAxisLabel = "Valores";

	    chart = 
    		ChartFactory.createXYLineChart(
    				chartTitle, xAxisLabel, yAxisLabel, dataset, 
    				PlotOrientation.VERTICAL, true, true, false);

	    List<XYSeries> series = dataset.getSeries();
	    
	    if(series != null && series.size() > 1) {
	    	XYPlot plot = chart.getXYPlot();
	    	
		    // Hacer que el eje X sea de numeros enteros
	    	NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
	    	xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	    	
	    	MyRenderer renderer = new MyRenderer();
			plot.setRenderer(renderer);
			renderer.setUseFillPaint(true);
			renderer.setUseOutlinePaint(true);
			
	    	if(Conf.colorSolParc != null) {
	    		solParcialColor = Conf.colorSolParc;
	    	}
	    	
	    	if(Conf.colorSolMej != null) {
	    		solMejorColor = Conf.colorSolMej;
	    	}
	    	
	    	if(Conf.colorCota != null) {
	    		cotaColor = Conf.colorCota;
	    	}
	    	
	    	if(Conf.grosorSolParc > 0) {
	    		solParcialStroke = new BasicStroke(Conf.grosorSolParc);
	    	}
	    	
	    	if(Conf.grosorSolMej > 0) {
	    		solMejorStroke = new BasicStroke(Conf.grosorSolMej);
	    	}
	    	
	    	if(Conf.grosorCota > 0) {
	    		cotaStroke = new BasicStroke(Conf.grosorCota);
	    	}
	    	
			// Poner colores y grosor para las lineas
	    	int index;
	    	for(XYSeries serie: series) {
	    		Comparable<?> key = serie.getKey();
	    		index = dataset.getSeriesIndex(key);
	    		if(key.toString().equalsIgnoreCase(Texto.get("PVG_SOLACTUAL", Conf.idioma))) {
		    		renderer.setSeriesShape(index, circle);
					renderer.setSeriesShapesFilled(index, true);
					renderer.setSeriesShapesVisible(index, true);
		    		renderer.setSeriesPaint(index, solParcialColor);
		    		renderer.setSeriesStroke(index, solParcialStroke);
		    		renderer.setSeriesOutlinePaint(index, solParcialColor);
		    	}else if(key.toString().equalsIgnoreCase(Texto.get("PVG_SOLMEJOR", Conf.idioma))) {
		    		renderer.setSeriesShape(index, circle);
					renderer.setSeriesShapesFilled(index, true);
					renderer.setSeriesShapesVisible(index, true);
		    		renderer.setSeriesPaint(index, solMejorColor);
		    		renderer.setSeriesStroke(index, solMejorStroke);
		    		renderer.setSeriesOutlinePaint(index, solMejorColor);
		    	}else {
		    		renderer.setSeriesShape(index, circle);
					renderer.setSeriesShapesFilled(index, true);
					renderer.setSeriesShapesVisible(index, true);
		    		renderer.setSeriesPaint(index, cotaColor);
		    		renderer.setSeriesStroke(index, cotaStroke);
		    		renderer.setSeriesOutlinePaint(index, cotaColor);
		    	}
	    	}

		    plot.setRenderer(renderer);
	    }

	    ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.removeAll();
	    chartPanel.setMouseWheelEnabled(true);
	 
	    return chartPanel;
	}
	
	private XYSeriesCollection createDataset() {	
    	XYSeriesCollection dataset = new XYSeriesCollection();
        
        // Llenar la grafica con los datos
        ra = Ventana.thisventana.traza.getRaiz();
        if(ra != null && nodoActual != null) {
            XYSeries serieSolActual = new XYSeries(Texto.get("PVG_SOLACTUAL", Conf.idioma));
            XYSeries serieSolMejor = new XYSeries(Texto.get("PVG_SOLMEJOR", Conf.idioma));
            XYSeries serieCota = new XYSeries(Texto.get("PVG_COTA", Conf.idioma));
        	
            hojas = new HashSet<>();
            podas = new HashSet<>();
            
            mapa = new HashMap<Integer, Integer>();
            
        	numNodo = 1;
    		Number solParcial = ra.getEntrada().getSolParcial();
    		Number solMejor = ra.getEntrada().getMejorSolucion();
    		Number cota = ra.getEntrada().getCota();
    		
    		if(solParcial == null || solMejor == null) {
    			encontrarHijo();	
    		}	
    		encontrado = false;
    		mapGrafica(ra);
    		
    		boolean ryp = nodoActual.getEntrada().getRyP();
			RegistroActivacion reg = nodoActual;
			int llamada;
			numItem = 0;
			while(reg != ra) {
        		solParcial = reg.getEntrada().getSolParcial();
        		solMejor = reg.getEntrada().getMejorSolucion();
        		cota = reg.getEntrada().getCota();
        		llamada = mapa.get(reg.getID());
        		
        		if(solParcial != null && solMejor != null ) {
        			numItem++;
            		if(reg.esHoja()) {
            			hojas.add(numItem - 1);
            		}
        			
            		serieSolActual.add(llamada, solParcial);
            		serieSolMejor.add(llamada, solMejor);

            		if(ryp && cota != null) {
            			if(Double.parseDouble(cota.toString()) < Double.parseDouble(solMejor.toString())) {
            				podas.add(numItem - 1);
            			}
            			serieCota.add(llamada, cota);
            		}
        		}
        		
				reg = reg.getPadre();
			}
			// Añadimos la raiz tambien
    		solParcial = ra.getEntrada().getSolParcial();
    		solMejor = ra.getEntrada().getMejorSolucion();
    		cota = ra.getEntrada().getCota();
    		llamada = mapa.get(ra.getID());
			
			if(solParcial != null && solMejor != null) {
				numItem++;
        		if(ra.esHoja()) {
        			hojas.add(numItem - 1);
        		}
				
        		serieSolActual.add(llamada, solParcial);
        		serieSolMejor.add(llamada, solMejor);
        		if(ryp && cota != null) {
        			if(Double.parseDouble(cota.toString()) < Double.parseDouble(solMejor.toString())) {
        				podas.add(numItem - 1);
        			}
        			serieCota.add(llamada, cota);
        		}
			}
			
    		dataset.addSeries(serieSolActual);
    		dataset.addSeries(serieSolMejor);
    		if(ryp && cota != null) {
    			dataset.addSeries(serieCota);
    		}
        }
        return dataset;
	}
	
	private void mapGrafica(RegistroActivacion ra2) {
	    if(!ra2.inhibido() && !encontrado) {
			Number solParcial = ra2.getEntrada().getSolParcial();
			Number solMejor = ra2.getEntrada().getMejorSolucion();
			
			if(solParcial != null && solMejor != null) {
				int llamada = numNodo;
				mapa.put(ra2.getID(), llamada);
				
				numNodo++;
			}
			if(ra2 != nodoActual) {
	        	for(RegistroActivacion raHijo: ra2.getHijos()) {
	        		mapGrafica(raHijo);
	        	}
			}else {
				encontrado = true;
			}
	    }
	}
	
	private void encontrarHijo() {
		for(RegistroActivacion raHijo: ra.getHijos()) {
    		Number solParcial = raHijo.getEntrada().getSolParcial();
    		Number solMejor = raHijo.getEntrada().getMejorSolucion();
    		
    		if(solParcial != null && solMejor != null) {
    			ra = raHijo;
    			numNodo++;
    			break;
    		}
		}
	}

	public void setNodoActual(RegistroActivacion nodoActual2) {
		nodoActual = nodoActual2;
	}
	
	public File saveChartAs(String name, String type) {
		File imageFile = new File(name + "." + type.toLowerCase());
		int width = 640;
		int height = 480;
		 
		try {
			if(type.equalsIgnoreCase("png")) {
				ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
			}else if(type.equalsIgnoreCase("jpeg")) {
				ChartUtilities.saveChartAsJPEG(imageFile, chart, width, height);
			} 
		} catch (IOException ex) {
		    System.err.println(ex);
		}
		return imageFile;
	}
}
