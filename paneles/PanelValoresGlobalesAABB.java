package paneles;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.*;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
public class PanelValoresGlobalesAABB extends JPanel implements ActionListener, 
		KeyListener, MouseListener, MouseMotionListener {
	
	private static final long serialVersionUID = 1L;
	
	private RegistroActivacion ra;
	private JPanel panel = new JPanel();
	private static int numNodo = 0;
	private int margenDerecha = 1;
	
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
		
		this.removeAll();
		this.add(this.panel, BorderLayout.NORTH);
		this.setBackground(Conf.colorPanel);

		this.updateUI();
	}
	
	private void initAndShow() {
		final JFXPanel jfxPanel = new JFXPanel();
		this.panel = new JPanel();
		this.panel.add(jfxPanel);
		
		 Platform.runLater(new Runnable() {
			 @Override
			 public void run() {
				 initFX(jfxPanel);
			 }
		 });
	}
	
    private void initFX(JFXPanel panel){
        Scene scene = createScene();
        panel.setScene(scene);
    }
    
    private Scene createScene(){
        // Definir los ejes
    	NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel(Texto.get("PVG_NODOS", Conf.idioma));

        // Crear la grafica
        LineChart<Number,Number> lineChart = 
        		new LineChart<Number,Number>(xAxis, yAxis);
        lineChart.setTitle(Texto.get("V_GLOBAL_VAL", Conf.idioma));
        
        // Definir las series
        Series<Number,Number> seriesCota = new Series<Number,Number>();
        seriesCota.setName(Texto.get("PVG_COTA", Conf.idioma));
        Series<Number,Number> seriesSolActual = new Series<Number,Number>();
        seriesSolActual.setName(Texto.get("PVG_SOLACTUAL", Conf.idioma));
        Series<Number,Number> seriesSolMejor = new Series<Number,Number>();
        seriesSolMejor.setName(Texto.get("PVG_SOLMEJOR", Conf.idioma));
        
        // Llenar la grafica con los datos
        this.ra = Ventana.thisventana.traza.getRaiz();
        if(this.ra != null) {
        	numNodo=0;
    		Number solParcial = ra.getEntrada().getSolParcial();
    		Number solMejor = ra.getEntrada().getMejorSolucion();
    		if(solParcial == null && solMejor == null) {
    			encontrarHijo(this.ra);
    		}
    		this.margenDerecha = numNodo + 1;
        	if(this.ra.getEntrada().getRyP()) {
        		crearGrafica(this.ra, seriesCota, seriesSolActual, seriesSolMejor);
        		lineChart.getData().add(seriesCota);
        	}else {
        		crearGrafica(this.ra, seriesSolActual, seriesSolMejor);
        	}
            lineChart.getData().add(seriesSolActual);
            lineChart.getData().add(seriesSolMejor);
        }
        
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(seriesSolActual.dataProperty().get().size()+this.margenDerecha);
        xAxis.setTickLabelGap(1);
        xAxis.setTickUnit(1);
        xAxis.setMinorTickVisible(false);
        
        Scene scene  = new Scene(lineChart,this.getWidth()*0.9,this.getHeight()*0.9);

        return scene;
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

	private void crearGrafica(RegistroActivacion ra, 
    		Series<Number,Number> seriesCota, 
    		Series<Number,Number> seriesSolActual, 
    		Series<Number,Number> seriesSolMejor) {
    	
    	if(!ra.inhibido()) {
    		int id = numNodo;
    		Number solParcial = ra.getEntrada().getSolParcial();
    		Number solMejor = ra.getEntrada().getMejorSolucion();
    		Number cota = ra.getEntrada().getCota();
    		if(solParcial != null && solMejor != null && cota != null) {
    			numNodo++;
            	seriesSolActual.getData().add(new Data<Number,Number>(id, solParcial));
            	seriesSolMejor.getData().add(new Data<Number,Number>(id, solMejor));
            	seriesCota.getData().add(new Data<Number,Number>(id, cota));
    		}
        	for(RegistroActivacion raHijo: ra.getHijos()) {
        		crearGrafica(raHijo, seriesCota, seriesSolActual, seriesSolMejor);
        	}
        }
    }
    
    private void crearGrafica(RegistroActivacion ra,
    		Series<Number,Number> seriesSolActual, 
    		Series<Number,Number> seriesSolMejor) {
    	
    	if(!ra.inhibido()) {
    		int id = numNodo;
    		Number solParcial = ra.getEntrada().getSolParcial();
    		Number solMejor = ra.getEntrada().getMejorSolucion();
    		if(solParcial != null && solMejor != null) {
    			numNodo++;
            	seriesSolActual.getData().add(new Data<Number,Number>(id, solParcial));
            	seriesSolMejor.getData().add(new Data<Number,Number>(id, solMejor));
    		}
        	for(RegistroActivacion raHijo: ra.getHijos()) {
        		crearGrafica(raHijo, seriesSolActual, seriesSolMejor);
        	}
        }
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
