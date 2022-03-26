package paneles;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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
	private boolean encontrado;
	
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

		this.removeAll();
		this.add(this.panel, BorderLayout.NORTH);
		this.setBackground(Conf.colorPanel);

		this.updateUI();
	}
	
	private void initAndShow() {
        JPanel chartPanel = createChartPanel();
		
		this.panel = new JPanel();
		this.panel.add(chartPanel);
	}
	
	private JPanel createChartPanel() {
		XYSeriesCollection dataset = createDataset();

		String chartTitle = Texto.get("V_RAMA_VAL", Conf.idioma);
	    String xAxisLabel = Texto.get("PVG_NODOS", Conf.idioma);
	    String yAxisLabel = "Valores";

	    JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
	            xAxisLabel, yAxisLabel, dataset);
	    
	    ChartPanel chartPanel = new ChartPanel(chart);
	 
	    return chartPanel;
	}
	
	private XYSeriesCollection createDataset() {	
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	
        XYSeries serieSolActual = new XYSeries(Texto.get("PVG_SOLACTUAL", Conf.idioma));
        XYSeries serieSolMejor = new XYSeries(Texto.get("PVG_SOLMEJOR", Conf.idioma));
        XYSeries serieCota = new XYSeries(Texto.get("PVG_COTA", Conf.idioma));
        
        // Llenar la grafica con los datos
        this.ra = Ventana.thisventana.traza.getRaiz();
        if(this.ra != null && this.nodoActual != null) {
        	numNodo=0;
    		Number solParcial = this.ra.getEntrada().getSolParcial();
    		Number solMejor = this.ra.getEntrada().getMejorSolucion();
    		Number cota = ra.getEntrada().getCota();
    		
    		if((this.ra == this.nodoActual) && (solParcial == null || solMejor == null)) {
    			// No mostramos nada
    		}else {
        		if(solParcial == null && solMejor == null) {
        			encontrarHijo(this.ra);
        			
        			// Actualizamos los datos
            		solParcial = this.ra.getEntrada().getSolParcial();
            		solMejor = this.ra.getEntrada().getMejorSolucion();
            		cota = ra.getEntrada().getCota();
        		}
        		
            	if(this.ra == this.nodoActual && solParcial != null && solMejor != null) {
            		// El nodo actual es la raiz por tanto no seguimos por el arbol    		
            		serieSolActual.add(1, solParcial);
                	serieSolMejor.add(1, solMejor);
                	
                	dataset.addSeries(serieSolActual);
                	dataset.addSeries(serieSolMejor);
                	
                	if(this.ra.getEntrada().getRyP()) {
                		serieCota.add(1, cota);
                		
                		dataset.addSeries(serieCota);
                	}
            	}else if(this.ra.getEntrada().getRyP()) {
            		this.encontrado = false;
            		crearGrafica(this.ra, dataset, serieSolActual, serieSolMejor, serieCota);
            		
            		dataset.addSeries(serieSolActual);
            		dataset.addSeries(serieSolMejor);
            		dataset.addSeries(serieCota);
            	}else {
            		this.encontrado = false;
            		crearGrafica(this.ra, dataset, serieSolActual, serieSolMejor);
            		
            		dataset.addSeries(serieSolActual);
            		dataset.addSeries(serieSolMejor);
            	}
    		}
        }
        
        return dataset;
	}
	
    private void crearGrafica(RegistroActivacion ra2, XYSeriesCollection dataset, 
    		XYSeries serieSolActual, XYSeries serieSolMejor) {
    	
    	if(!ra2.inhibido() && !this.encontrado) {
    		int id = numNodo;
    		Number solParcial = ra2.getEntrada().getSolParcial();
    		Number solMejor = ra2.getEntrada().getMejorSolucion();
    		if(solParcial != null && solMejor != null) {
    			numNodo++;
    			serieSolActual.add(id, solParcial);
    			serieSolMejor.add(id, solMejor);
    		}
    		if(ra2 != this.nodoActual) {
            	for(RegistroActivacion raHijo: ra2.getHijos()) {
            		crearGrafica(raHijo, dataset, serieSolActual, serieSolMejor);
            	}
    		}else {
    			this.encontrado = true;
    		}
        }
	}

	private void crearGrafica(RegistroActivacion ra2, XYSeriesCollection dataset, 
			XYSeries serieSolActual, XYSeries serieSolMejor, XYSeries serieCota) {
		
    	if(!ra2.inhibido() && !this.encontrado) {
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
    		if(ra2 != this.nodoActual) {
            	for(RegistroActivacion raHijo: ra2.getHijos()) {
            		crearGrafica(raHijo, dataset, serieSolActual, serieSolMejor, serieCota);
            	}
    		}else {
    			this.encontrado = true;
    		}
        }	
	}
	
	private void encontrarHijo(RegistroActivacion ra) {
		for(RegistroActivacion raHijo: ra.getHijos()) {
    		Number solParcial = raHijo.getEntrada().getSolParcial();
    		Number solMejor = raHijo.getEntrada().getMejorSolucion();
    		
    		if(solParcial != null && solMejor != null) {
    			this.ra = raHijo;
    			numNodo++;
    			break;
    		}
		}
	}

	public void setNodoActual(RegistroActivacion nodoActual) {
		this.nodoActual = nodoActual;
	}
}
