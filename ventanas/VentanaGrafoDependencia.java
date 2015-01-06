package ventanas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.plaf.metal.MetalBorders;

import org.jgraph.JGraph;

import conf.Conf;
import cuadros.CuadroDibujarTablaGrafoDependencia;
import cuadros.CuadroOpcionConfVisualizacion;
import datos.GrafoDependencia;
import utilidades.Texto;
import ventanas.*;

/**
 * Representa el cuadro donde se mostrará el grafo de dependencia.
 * 
 * @author David Pastor Herranz
 */
public class VentanaGrafoDependencia extends JFrame implements
		ActionListener, KeyListener {
	
	private static final int PORCENTAJE_PANTALLA = 70;
	
	private String nombreMetodo;
	
	private Ventana ventana;
	
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
	public VentanaGrafoDependencia(Ventana ventana, String nombreMetodo) {
			this.ventana = ventana;
			this.nombreMetodo = nombreMetodo;
			
			// Panel general
			BorderLayout bl = new BorderLayout();
			JPanel panel = new JPanel();
			panel.setLayout(bl);
//			panel.add(panelRadio, BorderLayout.NORTH);
//			panel.add(panelBotones, BorderLayout.SOUTH);

			// Preparamos y mostramos cuadro
			this.getContentPane().add(panel);
			this.setTitle("Grafo de Dependencia [> " + this.ventana.getTraza().getTitulo() + " <]");
			
			this.crearBarraDeHerramientas();
			
	        this.grafoDependencia = new GrafoDependencia(this.ventana.trazaCompleta, this.nombreMetodo);
	        this.representacionGrafo = this.grafoDependencia.obtenerRepresentacionGrafo();
	                    
	        this.representacionGrafoScroll = new JScrollPane(this.representacionGrafo);
	        this.add(this.representacionGrafoScroll);
	        
	        Dimension tamanioGrafo = representacionGrafo.getSize();
	        Dimension tamanioDialogo = new Dimension(tamanioGrafo.width + 20, tamanioGrafo.height + 80);
	        this.setSize(tamanioDialogo);
	        
	     	int[] ubicacion = Conf.ubicarCentro(tamanioDialogo.width, tamanioDialogo.height);
	     	this.setLocation(ubicacion[0], ubicacion[1]);
	     		
	        this.setResizable(true);
			this.setVisible(true);
	}
	
	private void crearBarraDeHerramientas() {
		
		this.botones = new JButton[6];
		
		this.botones[0] = new JButton(new ImageIcon("./imagenes/i_tabulado.gif"));
		this.botones[0].setToolTipText(Texto.get("GP_DIBUJAR_MATRIZ", Conf.idioma));
		this.botones[1] = new JButton(new ImageIcon("./imagenes/i_tabulado_automatico.gif"));
		this.botones[1].setToolTipText(Texto.get("GP_TABULAR_MATRIZ", Conf.idioma));
		
//		this.botones[2] = new JButton(new ImageIcon("./imagenes/i_entradasalida.gif"));
//		this.botones[2].setToolTipText(Texto.get("GP_DATOS_ENTRADA", Conf.idioma));
//		this.botones[3] = new JButton(new ImageIcon("./imagenes/i_vermetodosparam.gif"));
//		this.botones[3].setToolTipText(Texto.get("GP_PARAMETROS", Conf.idioma));
		
		this.botones[2] = new JButton(new ImageIcon("./imagenes/i_exportarestado.gif"));
		this.botones[2].setToolTipText(Texto.get("GP_EXPORTAR", Conf.idioma));
		
//		this.botones[4] = new JButton(new ImageIcon("./imagenes/i_formato.gif"));
//		this.botones[4].setToolTipText(Texto.get("GP_FORMATO", Conf.idioma));
		
		this.botones[3] = new JButton(new ImageIcon("./imagenes/i_zoommas.gif"));
		this.botones[3].setToolTipText(Texto.get("GP_AUMENTO_ZOOM", Conf.idioma));
		this.botones[4] = new JButton(new ImageIcon("./imagenes/i_zoommenos.gif"));
		this.botones[4].setToolTipText(Texto.get("GP_DISMINUCION_ZOOM", Conf.idioma));
		this.botones[5] = new JButton(new ImageIcon("./imagenes/i_zoomajuste.gif"));
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
		
		// Grupo formato
		this.barras[2].add(this.botones[3]);
		this.barras[2].add(this.botones[4]);
		this.barras[2].add(this.botones[5]);
		
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
		this.add(this.panelHerramientas, BorderLayout.NORTH);
	}
	
	public void dibujarTabla(int filas, int columnas) {
		
		this.grafoDependencia.debeDibujarseTabla(filas, columnas);
        this.representacionGrafo = this.grafoDependencia.obtenerRepresentacionGrafo();
        
        this.remove(this.representacionGrafoScroll);        
        this.representacionGrafoScroll = new JScrollPane(this.representacionGrafo);
        this.add(this.representacionGrafoScroll);
        this.revalidate();
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		new CuadroDibujarTablaGrafoDependencia(this, this.grafoDependencia.getNumeroFilasTabla(),
				this.grafoDependencia.getNumeroColumnasTabla());
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.setVisible(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
