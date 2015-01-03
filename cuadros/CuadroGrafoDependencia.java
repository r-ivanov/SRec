package cuadros;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;

import conf.Conf;
import datos.GrafoDependencia;
import ventanas.*;

/**
 * Representa el cuadro donde se mostrará el grafo de dependencia.
 * 
 * @author David Pastor Herranz
 */
public class CuadroGrafoDependencia extends Thread implements
		ActionListener, KeyListener {
	
	private static final int PORCENTAJE_PANTALLA = 70;
	
	private String nombreMetodo;
	
	private Ventana ventana;
	private JDialog dialogo;
	
	private GrafoDependencia grafoDependencia;
	private JGraph representacionGrafo;
	private JScrollPane representacionGrafoScroll;
	
//	i_tabulado.gif
//	
//	i_formato.gif
//	i_zoommas.gif
//	i_zoommenos.gif
//	i_zoomajuste.gif
//	
//	i_entradasalida.gif
//	i_vermetodosparam.gif
	
	/**
	 * Genera un nuevo cuadro que permite mostrar un grafo de dependencia.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 * @param nombreMetodo Nombre del método para el que generar el grafo de dependencia.
	 */
	public CuadroGrafoDependencia(Ventana ventana, String nombreMetodo) {
			this.dialogo = new JDialog(ventana, true);
			this.ventana = ventana;
			this.nombreMetodo = nombreMetodo;
			this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);
//		panel.add(panelRadio, BorderLayout.NORTH);
//		panel.add(panelBotones, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle("Grafo de Dependencia");
		
        this.grafoDependencia = new GrafoDependencia(this.ventana.trazaCompleta, this.nombreMetodo);
        this.representacionGrafo = this.grafoDependencia.obtenerRepresentacionGrafo();
                    
        this.representacionGrafoScroll = new JScrollPane(this.representacionGrafo);
        this.dialogo.add(this.representacionGrafoScroll);
        
        this.dialogo.setSize(representacionGrafo.getSize());	
     	int[] ubicacion = Conf.ubicarCentro(representacionGrafo.getSize().width, representacionGrafo.getSize().height);
     	this.dialogo.setLocation(ubicacion[0], ubicacion[1]);
     		
        this.dialogo.setResizable(true);
		this.dialogo.setVisible(true);
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == this.aceptar) {
//		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
