package paneles;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
	
	// Color de las lineas
	private Color solParcialColor = Color.RED;
	private Color solMejorColor = Color.GREEN;
	private Color cotaColor = Color.YELLOW;
	// Grosor de las lineas
	private BasicStroke solParcialStroke = new BasicStroke(1.0f);
	private BasicStroke solMejorStroke = new BasicStroke(1.0f);
	private BasicStroke cotaStroke = new BasicStroke(1.0f);
	
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
        int width = (int) Math.round(getWidth()*0.9);
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

	    chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset);
	    


	    if(dataset.getSeries()!=null) {
	    	XYPlot plot = chart.getXYPlot();
	    	
		    // Hacer que el eje X sea de numeros enteros
		    NumberAxis xAxis = new NumberAxis();
		    xAxis.setTickUnit(new NumberTickUnit(1));
	    	plot.setDomainAxis(xAxis);
	    	
		    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		    // sets paint color for each series
		    renderer.setSeriesPaint(0, solParcialColor);
		    renderer.setSeriesPaint(1, solMejorColor);
		    // sets thickness for series (using strokes)
		    renderer.setSeriesStroke(0, solParcialStroke);
		    renderer.setSeriesStroke(1, solMejorStroke);
		    if(dataset.getSeries().size() == 3){
		    	renderer.setSeriesPaint(2, cotaColor);
		    	
		    	renderer.setSeriesStroke(2, cotaStroke);
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
        	
        	numNodo=0;
    		Number solParcial = ra.getEntrada().getSolParcial();
    		Number solMejor = ra.getEntrada().getMejorSolucion();
    		Number cota = ra.getEntrada().getCota();
    		
    		if((ra == nodoActual) && (solParcial == null || solMejor == null)) {
    			// No mostramos nada
    		}else {
        		if(solParcial == null && solMejor == null) {
        			encontrarHijo();
        			
        			// Actualizamos los datos
            		solParcial = ra.getEntrada().getSolParcial();
            		solMejor = ra.getEntrada().getMejorSolucion();
            		cota = ra.getEntrada().getCota();
        		}
        		
            	if(ra == nodoActual && solParcial != null && solMejor != null) {
            		// El nodo actual es la raiz por tanto no seguimos por el arbol    		
            		serieSolActual.add(1, solParcial);
                	serieSolMejor.add(1, solMejor);
                	
                	dataset.addSeries(serieSolActual);
                	dataset.addSeries(serieSolMejor);
                	
                	if(ra.getEntrada().getRyP()) {
                		serieCota.add(1, cota);
                		
                		dataset.addSeries(serieCota);
                	}
            	}else if(ra.getEntrada().getRyP()) {
            		encontrado = false;
            		crearGrafica(ra, dataset, serieSolActual, serieSolMejor, serieCota);
            		
            		dataset.addSeries(serieSolActual);
            		dataset.addSeries(serieSolMejor);
            		dataset.addSeries(serieCota);
            	}else {
            		encontrado = false;
            		crearGrafica(ra, dataset, serieSolActual, serieSolMejor);
            		
            		dataset.addSeries(serieSolActual);
            		dataset.addSeries(serieSolMejor);
            	}
    		}
        }
        
        return dataset;
	}
	
    private void crearGrafica(RegistroActivacion ra2, XYSeriesCollection dataset, 
    		XYSeries serieSolActual, XYSeries serieSolMejor) {
    	
    	if(!ra2.inhibido() && !encontrado) {
    		int id = numNodo;
    		Number solParcial = ra2.getEntrada().getSolParcial();
    		Number solMejor = ra2.getEntrada().getMejorSolucion();
    		if(solParcial != null && solMejor != null) {
    			numNodo++;
    			serieSolActual.add(id, solParcial);
    			serieSolMejor.add(id, solMejor);
    		}
    		if(ra2 != nodoActual) {
            	for(RegistroActivacion raHijo: ra2.getHijos()) {
            		crearGrafica(raHijo, dataset, serieSolActual, serieSolMejor);
            	}
    		}else {
    			encontrado = true;
    		}
        }
	}

	private void crearGrafica(RegistroActivacion ra2, XYSeriesCollection dataset, 
			XYSeries serieSolActual, XYSeries serieSolMejor, XYSeries serieCota) {
		
    	if(!ra2.inhibido() && !encontrado) {
    		int id = numNodo;
    		Number solParcial = ra2.getEntrada().getSolParcial();
    		Number solMejor = ra2.getEntrada().getMejorSolucion();
    		Number cota = ra2.getEntrada().getCota();
    		if(solParcial != null && solMejor != null && cota != null) {
    			numNodo++;
    			serieSolActual.add(id, solParcial);
    			serieSolMejor.add(id, solMejor);
    			serieCota.add(id, cota);
    		}
    		if(ra2 != nodoActual) {
            	for(RegistroActivacion raHijo: ra2.getHijos()) {
            		crearGrafica(raHijo, dataset, serieSolActual, serieSolMejor, serieCota);
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
