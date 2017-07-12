package cuadros;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import botones.BotonCancelar;
import botones.BotonTexto;
import conf.Conf;
import paneles.PanelEditorJava2;
import utilidades.Texto;
import ventanas.GestorVentanaSRec;
import ventanas.Ventana;

/**
 * Implementa el cuadro que permite introducir y buscar la línea a la que 
 * 	el usuario quiere ir en el editor de código
 * 
 * @author Daniel Arroyo Cortés
 * 
 */

public class CuadroEditorBuscarTexto extends Thread implements ActionListener,
KeyListener, MouseListener, WindowListener {
	
	private JDialog dialogo;
	private PanelEditorJava2 panelJava;
	
	private JPanel panelBuscar, panelLabelEImagen, panelJTextArea, panelBotones, panelNumeroOcurrencias;
	private JLabel labelImagen, labelTextoABuscar, labelNumeroOcurrencias;
	private JTextField JTextAreaBuscar;
	
	private GridBagConstraints c;	

	private BotonTexto buscar;
	private BotonCancelar cancelar;
	
	private int numActualOcurrencias, numTotalOcurrencias;
	
	private String textoAnterior;
	
	private static final int ANCHO_CUADRO = 300;
	private static final int ALTO_CUADRO = 150;

	/**
	 * Constructor del cuadro
	 * 
	 * @param ventana
	 * 		Ventana a la que permanecerá asociado el cuadro de diálogo
	 * 
	 * @param panelJava
	 * 		Panel Java sobre el que actuará este cuadro
	 */
	public CuadroEditorBuscarTexto(Ventana ventana, PanelEditorJava2 panelJava) {
		this.dialogo = new JDialog(ventana, true);
		this.dialogo.addWindowListener(this);
		this.panelJava = panelJava;
		this.reiniciarBuscador();
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
		
		this.dialogo.getContentPane().add(this.panelBuscar);
		this.dialogo.setTitle(Texto.get("EDITOR_BUSCAR", Conf.idioma));

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
		
		this.panelBuscar = new JPanel(new GridBagLayout());
		
		//	Panel label e imagen
		
		this.panelLabelEImagen = new JPanel(new GridBagLayout());
		
		//	Imagen		
		
		this.labelImagen = new JLabel(
				new ImageIcon(
						GestorVentanaSRec.class
						.getClassLoader().getResource("imagenes/cod_buscar.png")
				)
			);
		
		//	Label superior
		
		this.labelTextoABuscar = new JLabel(Texto.get("EDITOR_TEXTO_A_BUSCAR", Conf.idioma));
		
		//	Panel JTextArea
		
		this.panelJTextArea = new JPanel(new GridBagLayout());
		
		//	JTextArea introducir texto
		
		this.JTextAreaBuscar = new JTextField();	
		
		//	Panel label ocurrencias
		
		this.panelNumeroOcurrencias = new JPanel(new GridBagLayout());
		
		//	Label número ocurrencias
		
		this.labelNumeroOcurrencias = new JLabel();
		
		//	Panel botones
		
		this.panelBotones = new JPanel(new GridBagLayout());
			
		//	Botones
			
		this.buscar = new BotonTexto(Texto.get("EDITOR_BOTON_BUSCAR", Conf.idioma));
		this.cancelar = new BotonCancelar();
		this.buscar.addMouseListener(this);
		this.buscar.addKeyListener(this);
		this.cancelar.addMouseListener(this);
		this.cancelar.addKeyListener(this);
	
	}
	
	/**
	 * Ordena visualmente los componentes
	 */
	private void ordenarComponentes() {
		
		//	Panel superior
		
		this.panelLabelEImagen.add(this.labelImagen, this.getConstraints(0, 0, 1, 1));
		this.panelLabelEImagen.add(this.labelTextoABuscar, this.getConstraints(1, 0, 6, 1));
		
		//	JTextArea introducir texto
		
		this.panelJTextArea.add(this.JTextAreaBuscar, this.getConstraintsForAllSpace(0, 0, 7, 1));
		
		//	Panel número de ocurrencias
		
		this.panelNumeroOcurrencias.add(this.labelNumeroOcurrencias, this.getConstraints(0, 0, 7, 1));
		
		//	Panel botones
		
		this.panelBotones.add(this.buscar, this.getConstraints(1, 0, 2, 1));
		this.panelBotones.add(this.cancelar, this.getConstraints(4, 0, 2, 1));
		
		//	Panel general
		
		this.panelBuscar.add(this.panelLabelEImagen, this.getConstraints(0, 0, 7, 1));
		this.panelBuscar.add(this.panelJTextArea, this.getConstraints(0, 1, 7, 1));
		this.panelBuscar.add(this.panelNumeroOcurrencias, this.getConstraints(0, 2, 7, 1));
		this.panelBuscar.add(this.panelBotones, this.getConstraints(0, 3, 7, 1));
	}
	
	/**
	 * Obtiene las GridBagConstraints normales para ordenar visualmente
	 * 
	 * @param x
	 * 		Empieza en la columna x
	 * 
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
	
	/**
	 * Obtiene las GridBagConstraints para ordenar visualmente, para que un elemento ocupe
	 * 	todo el espacio
	 * 
	 * @param x
	 * 		Empieza en la columna x
	 * 
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
	private GridBagConstraints getConstraintsForAllSpace(int x, int y, int width, int height) {
		this.c.gridx = x;
		this.c.gridy = y;
		this.c.gridwidth = width;
		this.c.gridheight = height;
		this.c.insets = new Insets(3,3,3,3);
		this.c.weighty = 1;
		this.c.weightx = 1;
		this.c.fill = GridBagConstraints.BOTH;
		return this.c;
	}
	
	/**
	 * Cuenta el número de ocurrencias en el panel de código
	 * 
	 * @return
	 * 		Número de veces que "texto" está en el panel de código
	 * 
	 */
	private int getNumberOcurrences() {
		String content = this.panelJava.getText();
        Pattern pattern = Pattern.compile(this.JTextAreaBuscar.getText());
        Matcher  matcher = pattern.matcher(content);

        int count = 0;
        while (matcher.find())
            count++;

        return count; 
	}
	
	/**
	 * Método que se encarga de buscar y resaltar las coincidencias en
	 * el panel de código
	 */
	private void searchOcurrences() {
		
		String textoBuscar = this.JTextAreaBuscar.getText();
		
		//	Mismo texto y con ocurrencias, vamos a siguiente e incrementamos
		
		if(textoAnterior.equals(textoBuscar) && this.numTotalOcurrencias != 0) {	
			
			this.numActualOcurrencias++;
			if(this.numActualOcurrencias > this.numTotalOcurrencias) {
				this.numActualOcurrencias = 1;		
				this.panelJava.reiniciarBuscador();
			}
			
			this.subrayarOcurrencia();
			
		//	Mismo texto y sin ocurrencias, NO hacemos nada
			
		} else if(textoAnterior.equals(textoBuscar) && this.numTotalOcurrencias == 0) {
			this.numActualOcurrencias = 1;		
			this.panelJava.reiniciarBuscador();
			
		//	Distinto texto, inicializamos
			
		}else {		
			
			int numeroOcurrencias = this.getNumberOcurrences();
			this.textoAnterior = textoBuscar;
			this.numTotalOcurrencias = numeroOcurrencias;
			
			if(numeroOcurrencias == 0) {
				this.labelNumeroOcurrencias.setText(Texto.get("EDITOR_NO_COINCIDENCIAS", Conf.idioma));
				return;
			}		
			
			this.numActualOcurrencias = 1;			
			
			this.subrayarOcurrencia();
		}
	}
	
	/**
	 * Subraya la ocurrencia del texto "this.JTextAreaBuscar.getText()" en "this.numActualOcurrencias"
	 */
	private void subrayarOcurrencia() {
		this.labelNumeroOcurrencias.setText(this.numActualOcurrencias+" / "+this.numTotalOcurrencias+" "+Texto.get("EDITOR_COINCIDENCIAS", Conf.idioma));
		
		if(numActualOcurrencias <= this.numTotalOcurrencias)
			this.panelJava.subrayarPalabra(this.JTextAreaBuscar.getText(), this.numActualOcurrencias);
	}
	
	/*
	 * Realiza todos los reinicios necesarios en el buscador, para cuando lo cierran
	 */
	private void reiniciarBuscador() {
		this.numActualOcurrencias=0;
		this.numTotalOcurrencias=0;
		this.textoAnterior="";
		this.panelJava.reiniciarBuscador();
	}
	//********************************************************************************
    // 			ACTION, KEY, MOUSE Y WINDOW LISTENER
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
			this.panelJava.removeSelects();
			this.dialogo.setVisible(false);			
		}else if(e.getSource() == this.buscar) {
			this.searchOcurrences();
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
			this.panelJava.removeSelects();
			this.dialogo.setVisible(false);			
		}else if(code == KeyEvent.VK_ENTER) {
			this.searchOcurrences();
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.panelJava.removeSelects();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}

}
