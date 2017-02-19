package cuadros;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Insets;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import utilidades.Texto;
import ventanas.Ventana;
import botones.BotonAceptar;
import botones.BotonCancelar;
import conf.Conf;
import paneles.PanelGrafo;



/**
 * Implementa el cuadro que permite tabular un grafo de dependencia dada una
 * expresión para filas o columnas con múltiples métodos.
 * 
 * @author Daniel Arroyo Cortés
 */
public class CuadroTabularGrafoDependenciaMultiplesMetodos extends Thread implements
		ActionListener, KeyListener, MouseListener {

	private static final int ALTURA_CUADRO = 150;
	private static final int ANCHURA_CUADRO = 450;

	private Ventana ventana;
	private BotonAceptar aceptar;
	private BotonCancelar cancelar;

	private JTextField textFilasColumnas;

	private JLabel labelSignatura;
	private JLabel labelParametrosComunesTit;
	private JLabel labelParametrosComunesContent;
	private List<String> parametrosComunesS;

	private JPanel panel, panelBoton, panelParam;
	private ButtonGroup grupoRadioButtons;
	private JRadioButton columnasRadioButton, filasRadioButton;
	private JDialog dialogo;
	
	private PanelGrafo pg;
	
	private String ultimaExpresionMultiplesMetodos;
	
	JLabel labelFilasColumnas;
	
	/**
	 * Crea un nuevo cuadro para tabular automáticamente grafos 
	 * 	de dependencia de múltiples métodos
	 * 
	 * @param ventana							
	 * 		Ventana a la que está asociado el cuadro
	 * @param pg								
	 * 		Panel grafo asociado
	 * @param labelSignatura					
	 * 		Label de la signatura de los métodos
	 * @param parametrosComunesS				
	 * 		String de los parámetros comunes con su tipo
	 * @param ultimaExpresionMultiplesMetodos	
	 * 		String con la última expresión introducida por el usuario
	 */
	public CuadroTabularGrafoDependenciaMultiplesMetodos(
			Ventana ventana,
			PanelGrafo pg,
			JLabel labelSignatura,
			List<String> parametrosComunesS,
			String ultimaExpresionMultiplesMetodos) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.pg = pg;
		this.labelSignatura = labelSignatura;
		this.parametrosComunesS = parametrosComunesS;
		this.ultimaExpresionMultiplesMetodos = ultimaExpresionMultiplesMetodos;
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		// 	Panel general de parámetros
		
		this.panelParam = new JPanel(new GridBagLayout());
		this.panelParam.setBorder(new TitledBorder(Texto.get(
				"GP_TABULAR_PANEL", Conf.idioma)));
		
		//	Métodos para generar el grafo de dependencia
		
		JLabel labelMetodo = new JLabel(Texto.get("GP_TABULAR_SIGNATURA_S",
				Conf.idioma));
		JLabel labelSignatura = this.labelSignatura;
		labelSignatura.setFont(labelSignatura.getFont().deriveFont(
				labelSignatura.getFont().getStyle() | Font.BOLD));
		
		//	Parámetros comunes
		
		this.labelParametrosComunesTit = new JLabel(Texto.get("GP_CUADROMMSELECT_PARAMC",
				Conf.idioma));
		
		this.labelParametrosComunesContent = new JLabel(this.parametrosComunesAString());
		
		//	Los métodos determinan
		
		JLabel labelMetodos = new JLabel(Texto.get("GP_CUADROMMSELECT_METODOS_DETERMINAN",
				Conf.idioma));		
		
		//	RaddioButtons filas / columnas
		
		this.columnasRadioButton = new JRadioButton(Texto.get("GP_CUADROMMSELECT_COLUMNAS",Conf.idioma));
		this.columnasRadioButton.setActionCommand(Texto.get("GP_CUADROMMSELECT_COLUMNAS",Conf.idioma));
		this.columnasRadioButton.setSelected(true);
		this.columnasRadioButton.setName("c");
		this.columnasRadioButton.addActionListener(this);
		
		this.filasRadioButton = new JRadioButton(Texto.get("GP_CUADROMMSELECT_FILAS",Conf.idioma));
		this.filasRadioButton.setActionCommand(Texto.get("GP_CUADROMMSELECT_FILAS",Conf.idioma));
		this.filasRadioButton.setName("f");
		this.filasRadioButton.addActionListener(this);
		
		this.grupoRadioButtons = new ButtonGroup();
		this.grupoRadioButtons.add(this.columnasRadioButton);
		this.grupoRadioButtons.add(this.filasRadioButton);
	    
		//	Expresión introducida por el usuario
		
		this.labelFilasColumnas = new JLabel(Texto.get("GP_TABULAR_FILAS",
				Conf.idioma));
		this.labelFilasColumnas.setPreferredSize(new Dimension(Math.max(
				Texto.get("GP_TABULAR_FILAS",Conf.idioma).length(),
				Texto.get("GP_TABULAR_COLUMNAS",Conf.idioma).length()
				)*7, 50));
		this.textFilasColumnas = new JTextField(40);		
		this.textFilasColumnas.addKeyListener(this);
		if (this.ultimaExpresionMultiplesMetodos != null) {
			this.textFilasColumnas.setText(this.ultimaExpresionMultiplesMetodos);
		}		

		//	Colocación de elementos
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 3; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		this.panelParam.add(labelMetodo,constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 3; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 7; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		this.panelParam.add(labelSignatura,constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 1; 		// Empieza fila
		constraints.gridwidth = 3; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);		
		
		this.panelParam.add(this.labelParametrosComunesTit,constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 3; 		// Empieza columna
		constraints.gridy = 1; 		// Empieza fila
		constraints.gridwidth = 7; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		this.panelParam.add(this.labelParametrosComunesContent,constraints);		
		

		constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 2; 		// Empieza fila
		constraints.gridwidth = 3; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		this.panelParam.add(labelMetodos,constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 3; 		// Empieza columna
		constraints.gridy = 2; 		// Empieza fila
		constraints.gridwidth = 3; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		this.panelParam.add(this.columnasRadioButton,constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 6; 		// Empieza columna
		constraints.gridy = 2; 		// Empieza fila
		constraints.gridwidth = 2; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		this.panelParam.add(this.filasRadioButton,constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 3; 		// Empieza fila
		constraints.gridwidth = 3; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		this.panelParam.add(this.labelFilasColumnas,constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 3; 		// Empieza columna
		constraints.gridy = 3; 		// Empieza fila
		constraints.gridwidth = 7; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		this.panelParam.add(this.textFilasColumnas,constraints);

		// Botones
		
		this.aceptar = new BotonAceptar();
		this.aceptar.addMouseListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar = new BotonCancelar();
		this.cancelar.addMouseListener(this);
		this.cancelar.addKeyListener(this);

		// Panel para los botones
		
		this.panelBoton = new JPanel();
		this.panelBoton.add(this.aceptar);
		this.panelBoton.add(this.cancelar);

		// Panel general
		
		this.panel = new JPanel(new BorderLayout());

		this.panel.add(this.panelParam, BorderLayout.NORTH);
		this.panel.add(this.panelBoton, BorderLayout.SOUTH);

		this.dialogo.getContentPane().add(this.panel);
		this.dialogo.setTitle(Texto.get("GP_TABULAR_TITULO", Conf.idioma));

		// Preparamos y mostramos cuadro
		
		this.dialogo.setSize(new Dimension(ANCHURA_CUADRO, ALTURA_CUADRO));
		int[] centroVentana = this.ubicarCentroVentana(ANCHURA_CUADRO,
				ALTURA_CUADRO);
		this.dialogo.pack();
		this.dialogo.setLocation(centroVentana[0], centroVentana[1]);
		this.dialogo.setResizable(true);
		this.dialogo.setVisible(true);
	}

	/**
	 * Devuelve las coordenadas para colocar el cuadro en el centro de la
	 * ventana asociada.
	 * 
	 * @param anchura
	 * @param altura
	 * 
	 * @return Coordenadas para colocar el cuadro en el centro de la ventana
	 *         asociada.
	 */
	private int[] ubicarCentroVentana(int anchura, int altura) {
		int[] coord = new int[2];
		coord[0] = (this.ventana.getX() + this.ventana.getWidth() / 2)
				- anchura / 2;
		coord[1] = (this.ventana.getY() + this.ventana.getHeight() / 2)
				- altura / 2;
		return coord;
	}

	/**
	 * Gestiona los eventos realizados sobre los botones.
	 * 
	 * @param e
	 * 		evento.
	 */
	private void gestionEventoBotones(AWTEvent e) {
		if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		} else if (e.getSource() == this.aceptar) {
			this.accionTabulado();
		}
	}

	/**
	 * Realiza las acciones correspondientes para tabular el grafo, una vez el
	 * usuario ha solicitado el tabulado dadas las expresiones.
	 */
	private void accionTabulado() {

		String mensajeError = null;

		if (this.textFilasColumnas.getText().length() == 0) {
			mensajeError = Texto.get("GP_ERROR_EXPR_REQ", Conf.idioma);
		}

		if (mensajeError == null) {
			mensajeError = this.pg.tabularMultiplesMetodos(this.textFilasColumnas.getText(),this.filasRadioButton.isSelected());
		}

		if (mensajeError != null) {
			new CuadroError(this.ventana, Texto.get("GP_ERROR_TITULO",
					Conf.idioma), mensajeError, 550, 125);
		} else {
			this.dialogo.setVisible(false);
		}
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JRadioButton){
			JRadioButton b = (JRadioButton) e.getSource();
			if(b.getName().toString().equals("f")){
				this.labelFilasColumnas.setText(Texto.get("GP_TABULAR_COLUMNAS",Conf.idioma));
			}else if(b.getName().toString().equals("c")){
				this.labelFilasColumnas.setText(Texto.get("GP_TABULAR_FILAS",Conf.idioma));
			}
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyPressed(KeyEvent e) {

	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if ((e.getSource() instanceof JButton) && code == KeyEvent.VK_SPACE) {
			this.gestionEventoBotones(e);
		} else if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		} else if (code == KeyEvent.VK_UP) {
			if (this.aceptar.hasFocus() || this.cancelar.hasFocus()) {
				this.textFilasColumnas.requestFocus();
			} 
		} else if (code == KeyEvent.VK_DOWN) {
			if(this.columnasRadioButton.hasFocus()){
				this.filasRadioButton.requestFocus();
			} else if (this.filasRadioButton.hasFocus()){
				this.textFilasColumnas.requestFocus();
			} else if(this.textFilasColumnas.hasFocus()){
				this.aceptar.requestFocus();
			}
			
		} else if (code == KeyEvent.VK_RIGHT) {
			if (this.aceptar.hasFocus()) {
				this.cancelar.requestFocus();
			}
		} else if (code == KeyEvent.VK_LEFT) {
			if (this.cancelar.hasFocus()) {
				this.aceptar.requestFocus();
			}
		} else if (code == KeyEvent.VK_ENTER) {
			this.accionTabulado();
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() instanceof JButton) {
			this.gestionEventoBotones(e);
		}
	}
	
	/**
	 * Convierte el array de parámetros comunes a String para
	 * 	representarlo
	 * @return
	 * 	String para representar los parámetros comunes
	 */
	private String parametrosComunesAString(){
		String retorno = "";
		for(String x : this.parametrosComunesS){
			retorno = retorno + x + ", ";
		}
		retorno = retorno.substring(0, retorno.length()-2);
		return retorno;
	}
}
