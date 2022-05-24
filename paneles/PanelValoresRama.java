package paneles;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import cuadros.CuadroInformacion;
import cuadros.CuadroProgreso;
import datos.MetodoAlgoritmo;
import datos.RegistroActivacion;
import utilidades.Arrays;
import utilidades.Fotografo;
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
public class PanelValoresRama extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private RegistroActivacion ra;
	private RegistroActivacion nodoActual = null;
	private JPanel panel = new JPanel();
	private static int numNodo = 0;
	
	// nodoActual encontrado -> paramos busqueda recursiva
	private boolean encontrado; 
	
	private JFreeChart chart;
	private JPanel chartPanel;
	private XYSeriesCollection dataset;
	private int width;
	private int height;
	
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
        	if(PanelValoresRama.podas.contains(column-numItem+1)) {
            	shape = ShapeUtilities.createDiagonalCross(Conf.grosorSolParc + 4.0f, 1);
        	}else if(PanelValoresRama.hojas.contains(column-numItem+1)) {
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
	public PanelValoresRama() {
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
        width = (int) Math.round(getWidth()*0.5);
        height = (int) Math.round(getHeight()*0.9);
        chartPanel.setPreferredSize(new Dimension(width, height));
        chartPanel.updateUI();
		
		panel.removeAll();
		panel.setLayout(new java.awt.BorderLayout());
		panel.add(chartPanel, BorderLayout.CENTER);
		panel.updateUI();
	}
	
	private JPanel createChartPanel() {
		dataset = createDataset();

		String chartTitle = Texto.get("V_RAMA_VAL", Conf.idioma);
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
    		
    		Number solParcialAux = nodoActual.getEntrada().getSolParcial();
    		Number solMejorAux = nodoActual.getEntrada().getMejorSolucion();
    		Number cotaAux = nodoActual.getEntrada().getCota();
    		
    		if(solParcialAux == null && solMejorAux == null && cotaAux == null) {
    			return dataset;
    		}
    		
    		if(solParcial == null && solMejor == null && cota == null) {
    			ra = encontrarHijo();	
    		}

    		if(ra == null) {
    			// No hacemos nada y retornamos 
    			return dataset;
    		}
    		
    		encontrado = false;
    		mapGrafica(ra);
    		
    		boolean ryp = nodoActual.getEntrada().esRyP();
			RegistroActivacion reg = nodoActual;
			Integer llamada;
			numItem = 0;
			while(reg != ra && reg.getID() != ra.getID()) {
        		solParcial = reg.getEntrada().getSolParcial();
        		solMejor = reg.getEntrada().getMejorSolucion();
        		cota = reg.getEntrada().getCota();
        		llamada = mapa.get(reg.getID());
        		
        		if(solParcial != null || solMejor != null || cota != null) {
        			numItem++;
            		if(reg.esHoja()) {
            			hojas.add(numItem - 1);
            		}
        			
            		if(llamada != null) {
            			if(solParcial != null) {
            				serieSolActual.add(llamada, solParcial);
            			}
            			if(solMejor != null) {
            				serieSolMejor.add(llamada, solMejor);
            			}
            			
                		if(ryp && cota != null && solMejor != null) {
                			podar(reg, cota, solMejor, numItem);
                			serieCota.add(llamada, cota);
                		}
            		}
        		}
        		
				reg = reg.getPadre();
			}
			// Añadimos la raiz tambien
    		solParcial = ra.getEntrada().getSolParcial();
    		solMejor = ra.getEntrada().getMejorSolucion();
    		cota = ra.getEntrada().getCota();
    		llamada = mapa.get(ra.getID());
			
    		if(solParcial != null || solMejor != null || cota != null) {
    			numItem++;
        		if(ra.esHoja()) {
        			hojas.add(numItem - 1);
        		}
    			
        		if(llamada != null) {
        			if(solParcial != null) {
        				serieSolActual.add(llamada, solParcial);
        			}
        			if(solMejor != null) {
        				serieSolMejor.add(llamada, solMejor);
        			}
        			
            		if(ryp && cota != null && solMejor != null) {
            			podar(ra, cota, solMejor, numItem);
            			serieCota.add(llamada, cota);
            		}
        		}
    		}
			if(Conf.solActualVisible) {
				dataset.addSeries(serieSolActual);
			}
			if(Conf.solMejorVisible) {
				dataset.addSeries(serieSolMejor);
			}
    		if(ryp && cota != null) {
    			if(Conf.cotaVisible) {
    				dataset.addSeries(serieCota);
    			}
    		}
        }
        return dataset;
	}
	
	private void podar(RegistroActivacion ra2, Number cota, Number solMejor, int id) {
		ArrayList<MetodoAlgoritmo> metodos = Ventana.thisventana.claseAlgoritmo.getMetodos();
		MetodoAlgoritmo metodo = null;
		
		for(MetodoAlgoritmo m : metodos) {
			if(m.getTecnica() == MetodoAlgoritmo.TECNICA_AABB) {
				metodo = m;
				break;
			}
		}
		
		if(metodo != null) {
			int indiceCotaParam = metodo.getCota();
			if(indiceCotaParam > 0) {
				String tipoCota = metodo.getTiposParametros()[indiceCotaParam];
				
				// Tenemos que tener mucho cuidado con la comprobación de igualdad en float y double.
				switch(tipoCota) {
				case "int":
				case "Integer":
					if(ra2.getEntrada().esMaximizacion()) {
						if(Integer.parseInt(cota.toString()) <= Integer.parseInt(solMejor.toString())) {
    	    				podas.add(id - 1);
    	    			}
					}else {
						if(Integer.parseInt(cota.toString()) >= Integer.parseInt(solMejor.toString())) {
    	    				podas.add(id - 1);
    	    			}
					}
					break;
				case "double":
				case "Double":
					Double cotaD = Double.parseDouble(cota.toString());
					Double solMejorD = Double.parseDouble(solMejor.toString());
					Double epsilonD = 0.000001d; // umbral para comparar double
					
					if (Math.abs(Math.abs(cotaD) - Math.abs(solMejorD)) < epsilonD) {
						// casi iguales
						podas.add(id - 1);
					}else {
    					if(ra2.getEntrada().esMaximizacion()) {
    						if(cotaD < solMejorD) {
        	    				podas.add(id - 1);
        	    			}
    					}else {
    						if(cotaD > solMejorD) {
        	    				podas.add(id - 1);
        	    			}
    					}
					}
					break;
				case "float":
				case "Float":
					Float cotaF = Float.parseFloat(cota.toString());
					Float solMejorF = Float.parseFloat(solMejor.toString());
					Float epsilonF = 0.000001f; // umbral para comparar float
					
					if (Math.abs(Math.abs(cotaF) - Math.abs(solMejorF)) < epsilonF) {
						// casi iguales
						podas.add(id - 1);
					}else {
    					if(ra2.getEntrada().esMaximizacion()) {
    						if(cotaF < solMejorF) {
        	    				podas.add(id - 1);
        	    			}
    					}else {
    						if(cotaF > solMejorF) {
        	    				podas.add(id - 1);
        	    			}
    					}
					}
					break;
				case "long":
				case "Long":
					if(ra2.getEntrada().esMaximizacion()) {
						if(Long.parseLong(cota.toString()) <= Long.parseLong(solMejor.toString())) {
    	    				podas.add(id - 1);
    	    			}
					}else {
						if(Long.parseLong(cota.toString()) >= Long.parseLong(solMejor.toString())) {
    	    				podas.add(id - 1);
    	    			}
					}
					break;
				case "byte":
				case "Byte":
					if(ra2.getEntrada().esMaximizacion()) {
						if(Byte.parseByte(cota.toString()) <= Byte.parseByte(solMejor.toString())) {
    	    				podas.add(id - 1);
    	    			}
					}else {
						if(Byte.parseByte(cota.toString()) >= Byte.parseByte(solMejor.toString())) {
    	    				podas.add(id - 1);
    	    			}
					}
					break;
				case "short":
				case "Short":
					if(ra2.getEntrada().esMaximizacion()) {
						if(Short.parseShort(cota.toString()) <= Short.parseShort(solMejor.toString())) {
    	    				podas.add(id - 1);
    	    			}
					}else {
						if(Short.parseShort(cota.toString()) >= Short.parseShort(solMejor.toString())) {
    	    				podas.add(id - 1);
    	    			}
					}
					break;
				default:
					break;
				}
			}
		}
	}
	
	private void mapGrafica(RegistroActivacion ra2) {
	    if(!ra2.inhibido() && !encontrado) {
			Number solParcial = ra2.getEntrada().getSolParcial();
			Number solMejor = ra2.getEntrada().getMejorSolucion();
			Number cota = ra2.getEntrada().getCota();
			
			if(solParcial != null || solMejor != null || cota != null) {
				Integer llamada = numNodo;
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

	public void setNodoActual(RegistroActivacion nodoActual2) {
		nodoActual = nodoActual2;
	}
	
	public File saveChartAs(String name, String type) {
		File imageFile = new File(name);
		 
		try {
			if(type.equalsIgnoreCase("png")) {
				ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
			}else if(type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("jpg")) {
				ChartUtilities.saveChartAsJPEG(imageFile, chart, width, height);
			}else if(type.equalsIgnoreCase("gif")){
				int[] dim = {width, height};
				Fotografo.guardarFoto(chartPanel, 1, name, dim);
			}else {
				return null;
			}
		} catch (IOException ex) {
		    System.err.println(ex);
		}
		return imageFile;
	}
	
	public void saveChartAsGIF(String name) {
		int numDataPoint = 0;
		int numFotos = 0;
		List<XYSeries> series = null;
		if(dataset != null) {
			series = dataset.getSeries();
		}
		int total;
		if(series != null && series.size() > 0) {
			total = series.size();
		}else {
			total = 0;
		}
		CuadroProgreso cp = new CuadroProgreso(
				Ventana.thisventana, Texto.get("CP_ESPERE",
						Conf.idioma), Texto.get("CP_PROCES",
						Conf.idioma), (numFotos*100)/total);
		while(numDataPoint >= 0) {
			if(series != null && series.size() > 0) {
				cp.setValores(
						Texto.get("CP_PROCES",
								Conf.idioma),
						(int) (numFotos*100)/total);
				
				numDataPoint = series.get(0).getItemCount() - 1;
				Fotografo.guardarFoto(chartPanel, numDataPoint);
				numFotos++;
				for(XYSeries serie:series) {
					serie.remove(numDataPoint);
				}
				numDataPoint--;
			}else {
				break;
			}
		}
		visualizar();
		Fotografo.crearGIFAnimado(numFotos, name, false);
		if (cp != null) {
			cp.cerrar();
		}
		new CuadroInformacion(Ventana.thisventana, Texto.get(
				"INFO_EXPCORRECTT", Conf.idioma), Texto.get(
				"INFO_EXPCORRECT", Conf.idioma), 550, 100);
	}
	
	public void saveChartAsCapturasAnimacion(String name, int type) {
		int numDataPoint = 0;
		int numFotos = 0;
		List<XYSeries> series = null;
		if(dataset != null) {
			series = dataset.getSeries();
		}
		int total;
		if(series != null && series.size() > 0) {
			total = series.size();
		}else {
			total = 0;
		}
		CuadroProgreso cp = new CuadroProgreso(
				Ventana.thisventana, Texto.get("CP_ESPERE",
						Conf.idioma), Texto.get("CP_PROCES",
						Conf.idioma), (numFotos*100)/total);
		
		while(numDataPoint >= 0) {
			if(series != null && series.size() > 0) {
				cp.setValores(
						Texto.get("CP_PROCES",
								Conf.idioma),
						(int) (numFotos*100)/total);
				
				numDataPoint = series.get(0).getItemCount() - 1;
				Fotografo.guardarFoto(chartPanel, name, type, numDataPoint);
				numFotos++;
				for(XYSeries serie:series) {
					serie.remove(numDataPoint);
				}
				numDataPoint--;
			}else {
				break;
			}
		}
		visualizar();
		if (cp != null) {
			cp.cerrar();
		}
		new CuadroInformacion(Ventana.thisventana, Texto.get(
				"INFO_EXPCORRECTT", Conf.idioma), Texto.get(
				"INFO_EXPCORRECT", Conf.idioma), 550, 100);
	}
}
