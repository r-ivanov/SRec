package cuadros;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import botones.BotonAceptar;
import botones.BotonCancelar;
import conf.Conf;
import paneles.PanelEditorJava2;
import utilidades.Texto;
import ventanas.GestorVentanaSRec;
import ventanas.Ventana;

/**
 * Implementa el cuadro que permite introducir la línea a la que 
 * 	el usuario quiere ir en el editor de código
 * 
 * @author Daniel Arroyo Cortés
 * 
 */

public class CuadroEditorIrALinea extends Thread implements ActionListener,
KeyListener, MouseListener {
	
	private JDialog dialogo;
	private Ventana ventana;
	private PanelEditorJava2 panelJava;
	private JScrollPane jsp;	
	
	private JPanel panelIrALinea, panelSuperior, panelBotones;
	private JLabel labelImagen, labelSuperior;
	private JSpinner numeroEntrada;
	
	private GridBagConstraints c;
	

	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	
	
	private static final int ANCHO_CUADRO = 300;
	private static final int ALTO_CUADRO = 130;

	/**
	 * Constructor del cuadro
	 * 
	 * @param ventana
	 * 		Ventana a la que permanecerá asociado el cuadro de diálogo
	 * 
	 * @param panelJava
	 * 		Panel Java sobre el que actuará este cuadro
	 * 
	 * @param jsp
	 * 		JScrollPane que moverá este cuadro
	 */
	public CuadroEditorIrALinea(Ventana ventana, PanelEditorJava2 panelJava, JScrollPane jsp) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.panelJava = panelJava;
		this.jsp = jsp;
		this.start();
	}
	
	/**
	 * Lanza la ejecución
	 */
	@Override
	public void run() {
		
		//	Inicializaciones previas
		
		this.c = new GridBagConstraints();		
		
		//	Creamos elementos
		
		this.inicializarComponentes();
		
		//	Ordenamos elementos
		
		this.ordenarComponentes();
		
		//	Dialogo
		
		this.dialogo.getContentPane().add(this.panelIrALinea);
		this.dialogo.setTitle(Texto.get("EDITOR_IR_A_LINEA", Conf.idioma));

		this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);

		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}
	
	//********************************************************************************
    // 			MÉTODOS PRIVADOS
    //********************************************************************************	
	
	/**
	 * Crea todos los componentes que vamos a añadir al diálogo
	 */
	private void inicializarComponentes() {
		
		//	Panel general
	
		this.panelIrALinea = new JPanel(new GridBagLayout());
		
		//	Panel superior
		
		this.panelSuperior = new JPanel(new GridBagLayout());
		
		//	Imagen superior
		
		this.labelImagen = new JLabel(
			new ImageIcon(
					GestorVentanaSRec.class
					.getClassLoader().getResource("imagenes/cod_irALinea.gif")
			)
		);		
		
		//	Label superior
		
		this.labelSuperior = new JLabel(Texto.get("EDITOR_NUMERO_LINEA",Conf.idioma));
		
		//	Input number
		
		SpinnerModel model =
		        new SpinnerNumberModel(1, 					//	Inicial
		                               1, 					//	Min
		                               Integer.MAX_VALUE, 	//	Max
		                               1);                	//	Paso
		
		this.numeroEntrada = new JSpinner(model);
		
		((JSpinner.DefaultEditor)this.numeroEntrada.getEditor()).getTextField().addKeyListener(this);
		
		//	Panel botones
		
		this.panelBotones = new JPanel(new GridBagLayout());
		
		//	Botones
		
		this.aceptar = new BotonAceptar();
		this.cancelar = new BotonCancelar();
		this.aceptar.addMouseListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar.addMouseListener(this);
		this.cancelar.addKeyListener(this);
	}
	
	/**
	 * Ordena visualmente los componentes
	 */
	private void ordenarComponentes() {
		
		//	Panel superior
		
		this.panelSuperior.add(this.labelImagen, this.getConstraints(0, 0, 1, 1));
		this.panelSuperior.add(this.labelSuperior, this.getConstraints(1, 0, 3, 1));
		this.panelSuperior.add(this.numeroEntrada, this.getConstraints(4, 0, 3, 1));
		
		//	Panel inferior
		
		this.panelBotones.add(this.aceptar, this.getConstraints(1, 0, 2, 1));
		this.panelBotones.add(this.cancelar, this.getConstraints(4, 0, 2, 1));
		
		//	Panel general
		
		this.panelIrALinea.add(this.panelSuperior, this.getConstraints(0, 0, 1, 1));
		this.panelIrALinea.add(this.panelBotones, this.getConstraints(0, 1, 1, 1));
		
	}
	
	/**
	 * Obtiene las GridBagConstraints para ordenar visualmente
	 * 
	 * @param x
	 * 		Empieza en la columna x
	 * @param y
	 * 		Empieza en la fila y
	 * 
	 * @param width
	 * 		Ocupa 'width' columnas.
	 * 
	 * @param height
	 * 		Ocupa 'height' filas.
	 * 
	 * @return
	 * 		GridBagConstraints
	 * 	
	 */
	private GridBagConstraints getConstraints(int x, int y, int width, int height) {
		this.c.gridx = x;
		this.c.gridy = y;
		this.c.gridwidth = width;
		this.c.gridheight = height;
		this.c.insets = new Insets(3,3,3,3);
		return this.c;
	}
	
	//********************************************************************************
    // 			ACTION, KEY Y MOUSE LISTENER
    //********************************************************************************
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		}else if(e.getSource() == this.aceptar) {
			this.panelJava.focusLinea((Integer)this.numeroEntrada.getValue(), this.jsp);	
			this.dialogo.setVisible(false);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		}else if(code == KeyEvent.VK_ENTER) {
			this.panelJava.focusLinea((Integer)this.numeroEntrada.getValue(), this.jsp);
			this.dialogo.setVisible(false);
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
