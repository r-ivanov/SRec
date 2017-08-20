package paneles;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

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
public class PanelCodigoBotones extends JPanel implements ActionListener{
	
	//********************************************************************************
    // 			VARIABLES
    //********************************************************************************	
	
	private static final long serialVersionUID = 3942482496745699703L;
	private final int numJToolbar = 3;
	private final int numJButton = 8;
	
	private JToolBar[] jt = new JToolBar[numJToolbar];	
	private JButton[] jb = new JButton[numJButton];	
	
	private enum botonesNombre {
		EDITOR_DESHACER, EDITOR_REHACER, EDITOR_CORTAR, EDITOR_COPIAR,
		EDITOR_PEGAR, EDITOR_IR_A_LINEA, EDITOR_BUSCAR,
		EDITOR_SELECCIONAR_TODO
	}
	
	private static final Map<botonesNombre,String> traduccionesBotones = 
			new HashMap<botonesNombre, String>();
	
	private PanelCodigo panelCodigo;	
	
	//********************************************************************************
    // 			CONSTRUCTORES
    //********************************************************************************
	
	/**
	 * Crea un nuevo panel con todos los botones del editor de código desactivados
	 * 
	 * @param pc
	 * 		Panel código asociado a este panel
	 */
	public PanelCodigoBotones(PanelCodigo pc) {
		this.panelCodigo = pc;	
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
		
		this.anadeTraduccionesBotones();
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
			this.jt[i] = jt1;			
		}
	}
	
	/**
	 * Crea todos los botones de la barra y los añade a jb[]
	 */
	private void creaBotones() {		
		
		this.jb[0] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_deshacer.png")));
		
		this.jb[0].setToolTipText(traduccionesBotones.get(botonesNombre.EDITOR_DESHACER));
		this.jb[0].setName(botonesNombre.EDITOR_DESHACER.toString());
		
		this.jb[1] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_rehacer.png")));
		
		this.jb[1].setToolTipText(traduccionesBotones.get(botonesNombre.EDITOR_REHACER));
		this.jb[1].setName(botonesNombre.EDITOR_REHACER.toString());
		
		this.jb[2] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_cortar.png")));
		
		this.jb[2].setToolTipText(traduccionesBotones.get(botonesNombre.EDITOR_CORTAR));
		this.jb[2].setName(botonesNombre.EDITOR_CORTAR.toString());
		
		this.jb[3] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_copiar.png")));
		
		this.jb[3].setToolTipText(traduccionesBotones.get(botonesNombre.EDITOR_COPIAR));
		this.jb[3].setName(botonesNombre.EDITOR_COPIAR.toString());
		
		this.jb[4] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_pegar.png")));
		
		this.jb[4].setToolTipText(traduccionesBotones.get(botonesNombre.EDITOR_PEGAR));
		this.jb[4].setName(botonesNombre.EDITOR_PEGAR.toString());
		
		this.jb[5] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_irALinea.gif")));
		
		this.jb[5].setToolTipText(traduccionesBotones.get(botonesNombre.EDITOR_IR_A_LINEA));
		this.jb[5].setName(botonesNombre.EDITOR_IR_A_LINEA.toString());
		
		this.jb[6] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_buscar.png")));
		
		this.jb[6].setToolTipText(traduccionesBotones.get(botonesNombre.EDITOR_BUSCAR));
		this.jb[6].setName(botonesNombre.EDITOR_BUSCAR.toString());		
		
		this.jb[7] = new JButton(new ImageIcon(GestorVentanaSRec.class
				.getClassLoader().getResource("imagenes/cod_seleccionarTodo.png")));		
		
		this.jb[7].setToolTipText(traduccionesBotones.get(botonesNombre.EDITOR_SELECCIONAR_TODO));
		this.jb[7].setName(botonesNombre.EDITOR_SELECCIONAR_TODO.toString());
		
		for(int i = 0; i<this.numJButton;i++) {
			this.jb[i].addActionListener(this);
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
		
		//	Añadimos JToolbar a panel
		
		for(int i = 0 ; i<this.numJToolbar;i++) {
			this.add(jt[i]);
		}
	}
	
	/**
	 * Precarga las traducciones de los botones
	 */
	private void anadeTraduccionesBotones() {		
		traduccionesBotones.put(botonesNombre.EDITOR_DESHACER, 
				Texto.get(botonesNombre.EDITOR_DESHACER.toString(), Conf.idioma));
		
		traduccionesBotones.put(botonesNombre.EDITOR_REHACER, 
				Texto.get(botonesNombre.EDITOR_REHACER.toString(), Conf.idioma));
		
		traduccionesBotones.put(botonesNombre.EDITOR_CORTAR, 
				Texto.get(botonesNombre.EDITOR_CORTAR.toString(), Conf.idioma));
		
		traduccionesBotones.put(botonesNombre.EDITOR_COPIAR, 
				Texto.get(botonesNombre.EDITOR_COPIAR.toString(), Conf.idioma));
		
		traduccionesBotones.put(botonesNombre.EDITOR_PEGAR, 
				Texto.get(botonesNombre.EDITOR_PEGAR.toString(), Conf.idioma));
		
		traduccionesBotones.put(botonesNombre.EDITOR_IR_A_LINEA, 
				Texto.get(botonesNombre.EDITOR_IR_A_LINEA.toString(), Conf.idioma));
		
		traduccionesBotones.put(botonesNombre.EDITOR_BUSCAR, 
				Texto.get(botonesNombre.EDITOR_BUSCAR.toString(), Conf.idioma));
		
		traduccionesBotones.put(botonesNombre.EDITOR_SELECCIONAR_TODO, 
				Texto.get(botonesNombre.EDITOR_SELECCIONAR_TODO.toString(), Conf.idioma));		
	}
	
	//********************************************************************************
    // 			ACTION LISTENER
    //********************************************************************************
	
	public void actionPerformed(ActionEvent e) {
		
		//	JButtons
		if(e.getSource().getClass().equals(JButton.class)){
			
			JButton button = (JButton) e.getSource();
			String buttonName = button.getName();
			
			if(buttonName.equals(botonesNombre.EDITOR_DESHACER.toString())){
				this.panelCodigo.doUndo();
			}else if(buttonName.equals(botonesNombre.EDITOR_REHACER.toString())){
				this.panelCodigo.doRedo();
			}else if(buttonName.equals(botonesNombre.EDITOR_CORTAR.toString())){
				this.panelCodigo.doCut();
			}else if(buttonName.equals(botonesNombre.EDITOR_COPIAR.toString())){
				this.panelCodigo.doCopy();
			}else if(buttonName.equals(botonesNombre.EDITOR_PEGAR.toString())){
				this.panelCodigo.doPaste();
			}else if(buttonName.equals(botonesNombre.EDITOR_IR_A_LINEA.toString())){
				this.panelCodigo.doSelectLine();
			}else if(buttonName.equals(botonesNombre.EDITOR_BUSCAR.toString())){
				this.panelCodigo.doSearch();
			}else if(buttonName.equals(botonesNombre.EDITOR_SELECCIONAR_TODO.toString())){
				this.panelCodigo.doSelectAll();
			}
		}
		
	}
}
