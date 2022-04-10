package paneles;

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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import conf.Conf;
import datos.RegistroActivacion;
import utilidades.Texto;
import ventanas.Ventana;

/**
 * Panel que contendrá la visualización de la vista global de valores para 
 * algoritmos de optimización basados en ramificación y poda.
 * 
 * @author Roumen Ivanov Andreev
 * @version 2021-2022
 */
public class PanelValoresGlobalesAABB extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private RegistroActivacion ra;
	private JPanel panel = new JPanel();
	private static int numNodo = 0;
	
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
	
	// Nodos hojas y nodos podados
	private static Set<Integer> hojas;
	private static Set<Integer> podas;
	
    private class MyRenderer extends XYLineAndShapeRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
	    public Shape getItemShape(int row, int column) {
        	Shape shape;
        	if(PanelValoresGlobalesAABB.podas.contains(column+1)) {
            	shape = ShapeUtilities.createDiagonalCross(Conf.grosorSolParc + 4.0f, 1);
        	}else if(PanelValoresGlobalesAABB.hojas.contains(column+1)) {
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
	 * Valores Globales.
	 */
	public PanelValoresGlobalesAABB() {
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
		panel.validate();
	}

	private JPanel createChartPanel() {
		XYSeriesCollection dataset = createDataset();

		String chartTitle = Texto.get("V_GLOBAL_VAL", Conf.idioma);
	    String xAxisLabel = Texto.get("PVG_NODOS", Conf.idioma);
	    String yAxisLabel = "Valores";

	    chart = 
    		ChartFactory.createXYLineChart(
    				chartTitle, xAxisLabel, yAxisLabel, dataset, 
    				PlotOrientation.VERTICAL, true, true, false);
	    
	    List<XYSeries> series = dataset.getSeries();
	    
	    if(series != null && series.size() > 0) {
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
	 
	    return new ChartPanel(chart);
	}
	
	private XYSeriesCollection createDataset() {	
    	XYSeriesCollection dataset = new XYSeriesCollection();

        // Llenar la grafica con los datos
        ra = Ventana.thisventana.traza.getRaiz();
        if(ra != null) {
            XYSeries serieSolActual = new XYSeries(Texto.get("PVG_SOLACTUAL", Conf.idioma));
            XYSeries serieSolMejor = new XYSeries(Texto.get("PVG_SOLMEJOR", Conf.idioma));
            XYSeries serieCota = new XYSeries(Texto.get("PVG_COTA", Conf.idioma));
        	
            hojas = new HashSet<>();
            podas = new HashSet<>();
            
        	numNodo = 1;
    		Number solParcial = ra.getEntrada().getSolParcial();
    		Number solMejor = ra.getEntrada().getMejorSolucion();
    		Number cota = ra.getEntrada().getCota();
    		if(solParcial == null && solMejor == null && cota == null) {
    			ra = encontrarHijo();	
    			if(ra == null) {
    				return dataset;
    			}
    		}
    		
        	if(ra.getEntrada().getRyP()) {
        		crearGrafica(ra, dataset, serieSolActual, serieSolMejor, serieCota);
        		if(Conf.cotaVisible) {
        			dataset.addSeries(serieCota);
        		}
        		
        	}else {
        		crearGrafica(ra, dataset, serieSolActual, serieSolMejor);
        	}
        	if(Conf.solActualVisible) {
        		dataset.addSeries(serieSolActual);
        	}
            if(Conf.solMejorVisible) {
            	dataset.addSeries(serieSolMejor);
            }
        }
        
        return dataset;
	}
	
    private void crearGrafica(RegistroActivacion ra2, XYSeriesCollection dataset, 
    		XYSeries serieSolActual, XYSeries serieSolMejor) {
    	
    	if(!ra2.inhibido()) {
    		int id = numNodo;
    		Number solParcial = ra2.getEntrada().getSolParcial();
    		Number solMejor = ra2.getEntrada().getMejorSolucion();

    		if(solParcial != null || solMejor != null) {
        		if(ra2.esHoja()) {
        			hojas.add(id - 1);
        		}
    			numNodo++;
    			if(solParcial != null) {
    				serieSolActual.add(id, solParcial);
    			} 
    			if(serieSolMejor != null){
    				serieSolMejor.add(id, solMejor);
    			}
    		}
        	for(RegistroActivacion raHijo: ra2.getHijos()) {
        		crearGrafica(raHijo, dataset, serieSolActual, serieSolMejor);
        	}
        }
	}

	private void crearGrafica(RegistroActivacion ra2, XYSeriesCollection dataset, 
			XYSeries serieSolActual, XYSeries serieSolMejor, XYSeries serieCota) {
		
    	if(!ra2.inhibido()) {
    		int id = numNodo;
    		Number solParcial = ra2.getEntrada().getSolParcial();
    		Number solMejor = ra2.getEntrada().getMejorSolucion();
    		Number cota = ra2.getEntrada().getCota();

    		if(solParcial != null || solMejor != null || cota != null) {
        		if(ra2.esHoja()) {
        			hojas.add(id - 1);
        		}
        		numNodo++;
        		
    			if(solParcial != null) {
    				serieSolActual.add(id, solParcial);
    			}
    			if(solMejor != null) {
    				serieSolMejor.add(id, solMejor);
    				if(cota != null) {
    	    			if(Double.parseDouble(cota.toString()) < Double.parseDouble(solMejor.toString())) {
    	    				podas.add(id - 1);
    	    			}
    				}
    			}
    			if(cota != null) {
    				serieCota.add(id, cota);
    			}
    		}
        	for(RegistroActivacion raHijo: ra2.getHijos()) {
        		crearGrafica(raHijo, dataset, serieSolActual, serieSolMejor, serieCota);
        	}
        }	
	}
	
	private RegistroActivacion encontrarHijo() {
		for(RegistroActivacion raHijo: ra.getHijos()) {
    		Number solParcial = raHijo.getEntrada().getSolParcial();
    		Number solMejor = raHijo.getEntrada().getMejorSolucion();
    		Number cota = raHijo.getEntrada().getCota();
    		
    		if(solParcial != null || solMejor != null || cota != null) {
    			numNodo++;
    			return raHijo;
    		}
		}
		return null;
	}
	
	public File saveChartAs(String name, String type) {
		File imageFile = new File(name);
		int width = 640;
		int height = 480;
		 
		try {
			if(type.equalsIgnoreCase("png")) {
				ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
			}else if(type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("jpg")) {
				ChartUtilities.saveChartAsJPEG(imageFile, chart, width, height);
			}else {
				return null;
			}
		} catch (IOException ex) {
		    System.err.println(ex);
		}
		return imageFile;
	}
}
