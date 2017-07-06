package paneles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.plaf.metal.MetalBorders;

import conf.Conf;
import utilidades.Texto;
import ventanas.GestorVentanaSRec;

/**
 * Panel de los botones del editor de cógido
 * 
 * @author 
 * 		Daniel Arroyo Cortés
 *
 */
public class PanelCodigoBotones extends JPanel implements KeyListener{
	
	//********************************************************************************
    // 			VARIABLES
    //********************************************************************************
	
	private final int numJToolbar = 3;
	private final int numJButton = 9;
	
	private JToolBar[] jt = new JToolBar[numJToolbar];	
	private JButton[] jb = new JButton[numJButton];	
	
	//********************************************************************************
    // 			CONSTRUCTORES
    //********************************************************************************
	
	/**
	 * Crea un nuevo panel con todos los botones del editor de código desactivados
	 */
	public PanelCodigoBotones() {
		//	TODO			
		this.crearBarraBotones();
	}
	
	//********************************************************************************
    // 			MÉTODOS PÚBLICOS
    //********************************************************************************
	
	/**
	 * Activa todos los botones del editor de código
	 */
	public void activarTodosBotones() {
		for (JButton jButton : jb) {
			jButton.setEnabled(true);
		}
	}
	
	/**
	 * Desactiva todos los botones del editor de código
	 */
	public void desactivarTodosBotones() {
		for (JButton jButton : jb) {
			jButton.setEnabled(false);
		}
	}
	
	//********************************************************************************
    // 			CREACIÓN BARRA
    //********************************************************************************
	
	/**
	 * Crea la barra de botones
	 */
	private void crearBarraBotones() {		
		
		//	Creamos barra
		
		this.crearJToolbar();
		this.creaBotones();
		this.anadeBotones();
	}
	
	/**
	 * Crea los JToolbar de la barra vacíos y los añade a jt[]
	 */
	private void crearJToolbar() {
		for(int i = 0 ; i<this.numJToolbar ; i++) {
			JToolBar jt1 = new JToolBar();
			jt1.setBorderPainted(true);
			jt1.setFloatable(false);
			jt1.setBorder(new MetalBorders.PaletteBorder());
			jt1.setLayout(new FlowLayout(FlowLayout.CENTER));
			this.jt[i] = jt1;			
		}
	}
	
	/**
	 * Crea todos los botones de la barra y los añade a jb[]
	 */
	private void creaBotones() {
		
		this.jb[0] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_deshacer.png")));
		
		this.jb[0].setToolTipText(Texto.get("EDITOR_DESHACER", Conf.idioma));
		
		this.jb[1] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_rehacer.png")));
		
		this.jb[1].setToolTipText(Texto.get("EDITOR_REHACER", Conf.idioma));
		
		this.jb[2] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_cortar.png")));
		
		this.jb[2].setToolTipText(Texto.get("EDITOR_CORTAR", Conf.idioma));
		
		this.jb[3] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_copiar.png")));
		
		this.jb[3].setToolTipText(Texto.get("EDITOR_COPIAR", Conf.idioma));
		
		this.jb[4] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_pegar.png")));
		
		this.jb[4].setToolTipText(Texto.get("EDITOR_PEGAR", Conf.idioma));
		
		this.jb[5] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_irALinea.gif")));
		
		this.jb[5].setToolTipText(Texto.get("EDITOR_IR_A_LINEA", Conf.idioma));
		
		this.jb[6] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_buscarPrimero.png")));
		
		this.jb[6].setToolTipText(Texto.get("EDITOR_BUSCAR_PRIMERO", Conf.idioma));
		
		this.jb[7] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_buscarSiguiente.png")));
		
		this.jb[7].setToolTipText(Texto.get("EDITOR_BUSCAR_SIGUIENTE", Conf.idioma));
		
		this.jb[8] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_seleccionarTodo.png")));
		
		this.jb[8].setToolTipText(Texto.get("EDITOR_SELECCIONAR_TODO", Conf.idioma));
		
		for(int i = 0; i<this.numJButton;i++) {
			this.jb[i].setPreferredSize(new Dimension(27, 27));
		}
		
		this.desactivarTodosBotones();
	}
	
	/**
	 * Añade los botones al panel
	 */
	private void anadeBotones() {
		
		//	Añadimos botones a JToolbar
		
		this.jt[0].add(this.jb[0]);
		this.jt[0].add(this.jb[1]);
		
		this.jt[1].add(this.jb[2]);
		this.jt[1].add(this.jb[3]);
		this.jt[1].add(this.jb[4]);
		
		this.jt[2].add(this.jb[5]);
		this.jt[2].add(this.jb[6]);
		this.jt[2].add(this.jb[7]);
		this.jt[2].add(this.jb[8]);
		
		//	Añadimos JToolbar a panel
		for(int i = 0 ; i<this.numJToolbar;i++) {
			this.add(jt[i]);
		}
	}
	
	//********************************************************************************
    // 			KEYLISTENER
    //********************************************************************************
	
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

}
