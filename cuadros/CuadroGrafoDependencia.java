package cuadros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.plaf.metal.MetalBorders;

import org.jgraph.JGraph;

import conf.Conf;
import datos.GrafoDependencia;
import utilidades.Texto;
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
	
	private JPanel panelHerramientas;
	private JToolBar[] barras;
	private JButton[] botones;
	
	
	private GrafoDependencia grafoDependencia;
	private JGraph representacionGrafo;
	private JScrollPane representacionGrafoScroll;
	
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
		this.dialogo.setTitle("Grafo de Dependencia [> " + this.ventana.getTraza().getTitulo() + " <]");
		
		this.crearBarraDeHerramientas();
		
        this.grafoDependencia = new GrafoDependencia(this.ventana.trazaCompleta, this.nombreMetodo);
        this.representacionGrafo = this.grafoDependencia.obtenerRepresentacionGrafo();
                    
        this.representacionGrafoScroll = new JScrollPane(this.representacionGrafo);
        this.dialogo.add(this.representacionGrafoScroll);
        
        Dimension tamanioGrafo = representacionGrafo.getSize();
        Dimension tamanioDialogo = new Dimension(tamanioGrafo.width + 20, tamanioGrafo.height + 80);
        this.dialogo.setSize(tamanioDialogo);
        
     	int[] ubicacion = Conf.ubicarCentro(tamanioDialogo.width, tamanioDialogo.height);
     	this.dialogo.setLocation(ubicacion[0], ubicacion[1]);
     		
        this.dialogo.setResizable(true);
		this.dialogo.setVisible(true);
	}
	
	private void crearBarraDeHerramientas() {
		
		this.botones = new JButton[8];
		
		this.botones[0] = new JButton(new ImageIcon("./imagenes/i_tabulado.gif"));
		this.botones[0].setToolTipText(Texto.get("GP_DIBUJAR_MATRIZ", Conf.idioma));
		this.botones[1] = new JButton(new ImageIcon("./imagenes/i_tabulado_automatico.gif"));
		this.botones[1].setToolTipText(Texto.get("GP_TABULAR_MATRIZ", Conf.idioma));
		
		this.botones[2] = new JButton(new ImageIcon("./imagenes/i_entradasalida.gif"));
		this.botones[2].setToolTipText(Texto.get("GP_DATOS_ENTRADA", Conf.idioma));
		this.botones[3] = new JButton(new ImageIcon("./imagenes/i_vermetodosparam.gif"));
		this.botones[3].setToolTipText(Texto.get("GP_PARAMETROS", Conf.idioma));
		
		this.botones[4] = new JButton(new ImageIcon("./imagenes/i_formato.gif"));
		this.botones[4].setToolTipText(Texto.get("GP_FORMATO", Conf.idioma));
		this.botones[5] = new JButton(new ImageIcon("./imagenes/i_zoommas.gif"));
		this.botones[5].setToolTipText(Texto.get("GP_AUMENTO_ZOOM", Conf.idioma));
		this.botones[6] = new JButton(new ImageIcon("./imagenes/i_zoommenos.gif"));
		this.botones[5].setToolTipText(Texto.get("GP_DISMINUCION_ZOOM", Conf.idioma));
		this.botones[7] = new JButton(new ImageIcon("./imagenes/i_zoomajuste.gif"));
		this.botones[5].setToolTipText(Texto.get("GP_AJUSTE_ZOOM", Conf.idioma));
			
		// Creamos las barras de herramientas
		this.barras = new JToolBar[3];
		for (int i = 0; i < this.barras.length; i++) {
			this.barras[i] = new JToolBar(Texto.get("BARRA_HERR", Conf.idioma));
			this.barras[i].setBorderPainted(true);
			this.barras[i].setFloatable(false);
			this.barras[i].setBorder(new MetalBorders.PaletteBorder());
		}

		// Grupo tabulación
		this.barras[0].add(this.botones[0]);
		this.barras[0].add(this.botones[1]);
		
		// Grupo opciones nodos
		this.barras[1].add(this.botones[2]);
		this.barras[1].add(this.botones[3]);
		
		// Grupo formato
		this.barras[2].add(this.botones[4]);
		this.barras[2].add(this.botones[5]);
		this.barras[2].add(this.botones[6]);
		this.barras[2].add(this.botones[7]);
		
		this.panelHerramientas = new JPanel();
		this.panelHerramientas.setLayout(new BorderLayout());
		
		JPanel p = new JPanel();
		for (int i = 0; i < this.barras.length; i++) {
			p.add(this.barras[i]);
		}
		
		for (int i = 0; i < this.botones.length; i++) {
			botones[i].setFocusable(false);
			botones[i].addActionListener(this);
		}
		
		this.panelHerramientas.add(p, BorderLayout.WEST);
		this.dialogo.add(this.panelHerramientas, BorderLayout.NORTH);
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		new CuadroOpcionConfVisualizacion(this.ventana);
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
