package cuadros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import conf.Conf;
import botones.BotonAceptar;
import botones.BotonCancelar;
import datos.MetodoAlgoritmo;
import utilidades.Logger;
import utilidades.Texto;
import ventanas.Ventana;

/**
 * Permite seleccionar el parámetro del método que contiene la estructura 
 * basada en arboles para un determinado algoritmo.
 */
public class CuadroIdentificarParametrosVE extends Thread implements 
		ActionListener, KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 590;
	private static final int ALTO_CUADRO = 450;
	private static final int LONGITUD_CAMPOS = 4;

	private JDialog dialogo;

	private BotonAceptar aceptar;
	private BotonCancelar cancelar;

	private MetodoAlgoritmo metodo;

	private JTextField[] camposVE;
	private JRadioButton botonesSeleccion[];

	private int numMetodo = -1;
	private CuadroSeleccionMetodosVE csm;

	private JPanel panelImagen;
	private JPanel panelCuadro;

	private boolean vueltaAtras;
	private JPanel panelBotones;

	/**
	 * Construye un cuadro que permite seleccionar los parámetros del método que
	 * contiene la estructura basada en arboles para un determinado algoritmo.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociada este cuadro.
	 * @param cuadroSeleccionMetodosVE
	 *            Cuadro de selección de métodos desde el que se invoca este
	 *            cuadro.    
	 * @param numMetodo
	 *            Número de método seleccionado para la identificación de
	 *            parámetros.
	 */
	public CuadroIdentificarParametrosVE(Ventana ventana, 
			CuadroSeleccionMetodosVE cuadroSeleccionMetodosVE, 
			MetodoAlgoritmo metodo, int numMetodo) {
		this.metodo = metodo;
		this.dialogo = new JDialog(ventana, true);
		this.csm = cuadroSeleccionMetodosVE;
		this.numMetodo = numMetodo;
		this.start();
	}

	/**
	 * Ejecuta el Thread asociado al cuadro.
	 */
	@Override
	public void run() {
		JPanel panel = new JPanel(new BorderLayout());

		// panel de la derecha: contiene el gráfico esquemático
		this.panelImagen = new JPanel();
		this.panelImagen.setPreferredSize(new Dimension(300, 162));

		// panel de botones

		// Botón Aceptar
		this.aceptar = new BotonAceptar();
		this.aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);
		this.aceptar.addMouseListener(this);

		// Botón Cancelar
		this.cancelar = new BotonCancelar();
		this.cancelar.addActionListener(this);
		this.cancelar.addKeyListener(this);
		this.cancelar.addMouseListener(this);

		JPanel panelBoton = new JPanel();
		panelBoton.add(this.aceptar);
		panelBoton.add(this.cancelar);

		// creación del panel general
		this.panelCuadro = new JPanel(new BorderLayout());
		this.panelBotones= new JPanel(new GridLayout(10, 1));
		this.botonesSeleccion = new JRadioButton[2];
		
		// Solo permite una seleccion para los "botonesSeleccion"
		// Maximizacion o Minimizacion
		ButtonGroup grupoBotonesSeleccion = new ButtonGroup();

		JLabel mensaje = new JLabel(
				Texto.get("PRIMERAPREGUNTA_VISTASESPECIFICAS", Conf.idioma));
		panelBotones.add(mensaje);
		
		// Se configuran los botones de seleccion
		for(int i=0; i<this.botonesSeleccion.length ; i++) {		
			if(i==0) {
				this.botonesSeleccion[i] = 
						new JRadioButton(
								Texto.get("BOTON_MAXVISTASESPECIFICAS", 
										Conf.idioma));
			}else if(i==1) {
				this.botonesSeleccion[i] = 
						new JRadioButton(
								Texto.get("BOTON_MINVISTASESPECIFICAS", 
										Conf.idioma));
			}
			
			panelBotones.add(this.botonesSeleccion[i], BorderLayout.NORTH);
			
			this.botonesSeleccion[i].addKeyListener(this);
			this.botonesSeleccion[i].addActionListener(this);
			grupoBotonesSeleccion.add(this.botonesSeleccion[i]);
		}
		this.botonesSeleccion[0].setSelected(true); // Por defecto Maximizacion
		
		JLabel mensaje2 = new JLabel(
				Texto.get("INDICACION_VISTASESPECIFICAS", Conf.idioma) 
					+ (this.metodo.getNumeroParametros() - 1) + "):");
		panelBotones.add(mensaje2);

		// Campos de texto para introducir solucion parcial, mejor solucion 
		// y estimacion de cota
		this.camposVE = new JTextField[3];
		for(int i=0; i<3 ; i++) {
			JPanel panelIndices = new JPanel(new BorderLayout());
			this.camposVE[i] = new JTextField(LONGITUD_CAMPOS);
			this.camposVE[i].setBounds(10,10,200,30);

			JLabel mensajes = new JLabel();
			if (i == 0) {
				mensajes.setText(
						Texto.get("SOLPARCIAL_VISTASESPECIFICAS", Conf.idioma));
			} else if (i == 1) {
				mensajes.setText(
						Texto.get("MEJORSOL_VISTASESPECIFICAS", Conf.idioma));
			} else if (i == 2) {
				mensajes.setText(
						Texto.get("ESTIM_VISTASESPECIFICAS", Conf.idioma));
			}
			
			this.camposVE[i].setHorizontalAlignment(SwingConstants.CENTER);
			
			panelIndices.add(this.camposVE[i], BorderLayout.WEST);
			panelIndices.add(mensajes, BorderLayout.CENTER);
			
			panelBotones.add(panelIndices);
			panelBotones.addKeyListener(this);
		}
		
		
		this.panelCuadro.add(this.panelBotones, BorderLayout.NORTH);
		this.panelCuadro.setBorder(
				new TitledBorder(
						Texto.get("CIPDYV_METODO", Conf.idioma) 
						+ ": " 
						+ this.metodo.getRepresentacionTotal()));

		panel.add(this.panelCuadro, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);
		

		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("CIPDYV_TITULO", Conf.idioma));
		
		this.dialogo.setSize(
				CuadroIdentificarParametrosVE.ANCHO_CUADRO, 
				CuadroIdentificarParametrosVE.ALTO_CUADRO);
		int coord[] = Conf.ubicarCentro(
				CuadroIdentificarParametrosVE.ANCHO_CUADRO, 
				CuadroIdentificarParametrosVE.ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);

		this.dialogo.setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
		this.dialogo.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	accionCancelar();
            }
        });
		
		
		//pintarCampos();

		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Comprueba si los valores introducidos son correctos y determina si se
	 * debe mostrar un cuadro de error si alguno de los parámetros no es
	 * correcto o devolver los datos de selección al cuadro de selección de
	 * métodos.
	 */
	private void accionAceptar(boolean maximizacion) {
		int estado = valoresCorrectos();
		if (estado == 0) {		
			this.csm.setParametrosMetodo(this.numMetodo, valoresParametros(), this.vueltaAtras, maximizacion);
			this.dialogo.setVisible(false);
		}else {
			new CuadroError(
					this.dialogo, 
					Texto.get("ERROR_VAL", Conf.idioma) + "estado: " + estado, 
					Texto.get("ERROR_INFOPARAMVE" + estado, Conf.idioma));
		}
	}

	/**
	 * Cierra el cuadro.
	 */
	private void accionCancelar() {
		this.csm.marcarMetodo(this.numMetodo, false);
		this.dialogo.setVisible(false);
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.aceptar) {
			if(this.botonesSeleccion[0].isSelected()) {
				accionAceptar(true);
			}else if(this.botonesSeleccion[1].isSelected()) {
				accionAceptar(false);
			}else {
				accionCancelar();
			}
		} else if (e.getSource() == this.cancelar) {
			accionCancelar();
		}
	}

	/**
	 * Valida los valores introducidos. Devuelve un número que representa un
	 * error determinado.
	 * 
	 * @return 0 = todo correcto | 
	 * 		   1 = núm. parámetro de estructura no es válido | 
	 * 		   2 = núm. parámetro de índice no es válido | 
	 * 		   3 = el primer y/o segundo núm.parámetro de índices están vacíos | 
	 * 		   4 = valores repetidos |
	 */
	private int valoresCorrectos() {
		// false = ramificación y poda
		// true = vuelta atras
		this.vueltaAtras = false; 
		for (int i = 0; i < this.camposVE.length; i++) {
			// Comprobar que los indices sean validos
			if (this.camposVE[i].getText().length() != 0) {
				try {
					int x = Integer.parseInt(this.camposVE[i].getText());
					if (x < 0 || x >= this.metodo.getNumeroParametros()) {
						return 2;
					}
				}catch (Exception e) {
					return 2;
				}
			}else {
				if(i < 2) {
					return 3;
				}else {
					// Si el campo para la estimacion de cota está vacio 
					// se asume Vuelta Atras y en caso contrario se asume
					// Ramificación y poda
					this.vueltaAtras = true;
				}
			}
	
		}

		// Comprobamos que no haya ni un solo valor repetido
		for (int i = 0; i < this.camposVE.length; i++) {
			for (int j = i + 1; j < this.camposVE.length; j++) {
				if(!this.vueltaAtras 
						&& !(this.camposVE[i].getText().equalsIgnoreCase("")) 
						&& (Integer.parseInt(this.camposVE[i].getText()) 
								== 
								Integer.parseInt(this.camposVE[j].getText()))) {
					return 4;
				}
			}
			// Comprobar tambien con this.e
		}
		return 0;
	}

	/**
	 * Devuelve los valores de los parametros.
	 * 
	 * @return Un string con el formato (p, m, c, ...) donde
	 *  p = La medida asociada con la solución parcial
	 *  m = La medida asociada a la mejor solución encontrada
	 *  c = La estimacion de la cota (puede estar sin rellenar en caso de vuelta atras)
	 */
	private String valoresParametros() {
		String resultado = "";

		int num = this.camposVE.length;
		if(this.vueltaAtras) {
			// En caso de vuelta atras no se tiene en cuenta el campo para la
			// estimacion de cota
			num--;
		}
		
		for (int i = 0; i < num; i++) {
			if (i != 0) {
				resultado = resultado + ", ";
			}
			resultado = resultado + this.camposVE[i].getText();
		}
		
		return resultado;
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
		if (code == KeyEvent.VK_ENTER) {
			if(this.botonesSeleccion[0].isSelected()) {
				accionAceptar(true);
			}else if(this.botonesSeleccion[1].isSelected()) {
				accionAceptar(false);
			}else {
				accionCancelar();
			}
		} else if (code == KeyEvent.VK_ESCAPE) {
			accionCancelar();
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
		if (e.getSource() == this.aceptar) {
			if(this.botonesSeleccion[0].isSelected()) {
				accionAceptar(true);
			}else if(this.botonesSeleccion[1].isSelected()) {
				accionAceptar(false);
			}else {
				accionCancelar();
			}
		} else if (e.getSource() == this.cancelar) {
			accionCancelar();
		}
	}
}
