package cuadros;

import java.awt.BorderLayout;
import java.awt.Component;
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
import java.awt.event.WindowFocusListener;

import javax.swing.ButtonGroup;
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
import opciones.GestorOpciones;
import opciones.OpcionOpsVisualizacion;
import utilidades.Logger;
import utilidades.Texto;
import ventanas.Ventana;

/**
 * Permite seleccionar los parámetros del método que contienen 
 * la solucion parcial, la mejor solución y la cota 
 * para un determinado algoritmo basado en árboles de búsqueda.
 */
public class CuadroIdentificarParametrosAABB extends Thread implements 
		ActionListener, KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 590;
	private static final int ALTO_CUADRO = 260;
	private static final int LONGITUD_CAMPOS = 4;

	private JDialog dialogo;
	
	private OpcionOpsVisualizacion oov;
	private GestorOpciones gOpciones = new GestorOpciones();

	private BotonAceptar aceptar;
	private BotonCancelar cancelar;

	private MetodoAlgoritmo metodo;

	private JTextField[] camposVE;
	private JRadioButton botonesSeleccion[];

	private int numMetodo = -1;
	private CuadroSeleccionMetodos csm;

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
	 * @param m
	 *            Metodo del algoritmo.
	 * @param numM
	 *            Número de método seleccionado para la identificación de
	 *            parámetros.
	 */
	public CuadroIdentificarParametrosAABB(Ventana ventana, 
			CuadroSeleccionMetodos cuadroSeleccionMetodosVE, 
			MetodoAlgoritmo m, int numM) {
		metodo = m;
		dialogo = new JDialog(ventana, true);
		csm = cuadroSeleccionMetodosVE;
		numMetodo = numM;
		start();
	}

	/**
	 * Ejecuta el Thread asociado al cuadro.
	 */
	@Override
	public void run() {
		JPanel panel = new JPanel(new BorderLayout());

		// panel de botones

		// Botón Aceptar
		aceptar = new BotonAceptar();
		aceptar.addActionListener(this);
		aceptar.addKeyListener(this);
		aceptar.addMouseListener(this);

		// Botón Cancelar
		cancelar = new BotonCancelar();
		cancelar.addActionListener(this);
		cancelar.addKeyListener(this);
		cancelar.addMouseListener(this);

		JPanel panelBoton = new JPanel();
		panelBoton.add(aceptar);
		panelBoton.add(cancelar);

		// creación del panel general
		panelCuadro = new JPanel(new BorderLayout());
		panelBotones= new JPanel(new GridLayout(7, 1));
		botonesSeleccion = new JRadioButton[2];
		
		// Solo permite una seleccion para los "botonesSeleccion"
		// Maximizacion o Minimizacion
		ButtonGroup grupoBotonesSeleccion = new ButtonGroup();

		JLabel mensaje = new JLabel(
				Texto.get("PRIMERAPREGUNTA_VISTASESPECIFICAS", Conf.idioma));
		panelBotones.add(mensaje);
		
		// Se configuran los botones de seleccion
		oov = (OpcionOpsVisualizacion) gOpciones
				.getOpcion("OpcionOpsVisualizacion", false);
		boolean anteriorMaximizacion = oov.getAnteriorMaximizacion();
		for(int i = 0; i < botonesSeleccion.length ; i++) {		
			if(i == 0) {
				botonesSeleccion[i] = 
						new JRadioButton(
								Texto.get("BOTON_MAXVISTASESPECIFICAS", 
										Conf.idioma));
			}else if(i == 1) {
				botonesSeleccion[i] = 
						new JRadioButton(
								Texto.get("BOTON_MINVISTASESPECIFICAS", 
										Conf.idioma));
			}
			
			panelBotones.add(botonesSeleccion[i], BorderLayout.NORTH);
			
			botonesSeleccion[i].addKeyListener(this);
			botonesSeleccion[i].addActionListener(this);
			grupoBotonesSeleccion.add(botonesSeleccion[i]);
		}
		// Establecer el boton seleccionado por defecto
		if(anteriorMaximizacion) {
			botonesSeleccion[0].setSelected(true);
		}else {
			botonesSeleccion[1].setSelected(true);
		}
		
		
		JLabel mensaje2 = new JLabel(
				Texto.get("INDICACION_VISTASESPECIFICAS", Conf.idioma) 
					+ (metodo.getNumeroParametros() - 1) + "):");
		panelBotones.add(mensaje2);

		// Campos de texto para introducir solucion parcial, mejor solucion 
		// y estimacion de cota
		camposVE = new JTextField[3];
		for(int i=0; i<3 ; i++) {
			JPanel panelIndices = new JPanel(new BorderLayout());
			camposVE[i] = new JTextField(LONGITUD_CAMPOS);
			//camposVE[i].setBounds(10,10,200,30);

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
			
			camposVE[i].setHorizontalAlignment(SwingConstants.CENTER);
			camposVE[i].addKeyListener(this);
			
			panelIndices.add(camposVE[i], BorderLayout.WEST);
			panelIndices.add(mensajes, BorderLayout.CENTER);
			
			panelBotones.add(panelIndices);
			panelBotones.addKeyListener(this);
		}
		
		panelCuadro.add(panelBotones, BorderLayout.NORTH);
		panelCuadro.setBorder(
				new TitledBorder(
						Texto.get("CIPDYV_METODO", Conf.idioma) 
						+ ": " 
						+ metodo.getRepresentacionTotal()));

		panel.add(panelCuadro, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);
		

		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("CIPDYV_TITULO", Conf.idioma));
		
		dialogo.setSize(
				CuadroIdentificarParametrosAABB.ANCHO_CUADRO, 
				CuadroIdentificarParametrosAABB.ALTO_CUADRO);
		int coord[] = Conf.ubicarCentro(
				CuadroIdentificarParametrosAABB.ANCHO_CUADRO, 
				CuadroIdentificarParametrosAABB.ALTO_CUADRO);
		dialogo.setLocation(coord[0], coord[1]);

		dialogo.setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
		dialogo.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	accionCancelar();
            }
        });
		dialogo.addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		    	camposVE[0].requestFocusInWindow();
		    }
		});
		dialogo.setResizable(false);
		dialogo.setVisible(true);
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
			csm.setParametrosMetodo(numMetodo, valoresParametros(), vueltaAtras, maximizacion);
			dialogo.setVisible(false);
			dialogo.dispose();
		}else {
			new CuadroError(
					dialogo, 
					Texto.get("ERROR_VAL", Conf.idioma) + "estado: " + estado, 
					Texto.get("ERROR_INFOPARAMVE" + estado, Conf.idioma));
		}
	}

	/**
	 * Cierra el cuadro.
	 */
	private void accionCancelar() {
		csm.marcarMetodo(numMetodo, false);
		dialogo.setVisible(false);
		dialogo.dispose();
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == aceptar) {
			if(botonesSeleccion[0].isSelected()) {
				accionAceptar(true);
				oov.setAnteriorMaximizacion(true);
				gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					Logger.log_write("Tipo de problema: Maximización");
				}
			}else if(botonesSeleccion[1].isSelected()) {
				accionAceptar(false);
				oov.setAnteriorMaximizacion(false);
				gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					Logger.log_write("Tipo de problema: Minimización");
				}
			}else {
				accionCancelar();
			}
		} else if (e.getSource() == cancelar) {
			accionCancelar();
		}
	}

	/**
	 * Valida los valores introducidos. Devuelve un número que representa un
	 * error determinado.
	 * 
	 * @return 0 = todo correcto | 
	 * 		   1 = núm. parámetro no es válido | 
	 * 		   2 = núm. parámetro no es válido (dimension) | 
	 * 		   3 = núm. parámetro no es válido (tipo)
	 * 		   4 = el primer y/o segundo núm.parámetro de índices están vacíos | 
	 * 		   5 = valores repetidos |
	 */
	private int valoresCorrectos() {
		// false = ramificación y poda
		// true = vuelta atras
		vueltaAtras = false; 
		for (int i = 0; i < camposVE.length; i++) {
			// Comprobar que los indices sean validos
			if (camposVE[i].getText().length() != 0) {
				try {
					int x = Integer.parseInt(camposVE[i].getText());
					if (x < 0 || x >= metodo.getNumeroParametros()) {
						return 1;
					}
				}catch (Exception e) {
					return 1;
				}
			}else {
				if(i < camposVE.length - 1) {
					return 4;
				}else {
					// Si el campo para la estimacion de cota está vacio 
					// se asume Vuelta Atras y en caso contrario se asume
					// Ramificación y poda
					vueltaAtras = true;
				}
			}
	
		}

		// Comprobamos que no haya ni un solo valor repetido
		for (int i = 0; i < camposVE.length; i++) {
			for (int j = i + 1; j < camposVE.length; j++) {
				if(!vueltaAtras 
						&& !(camposVE[i].getText().equalsIgnoreCase("")) 
						&& (Integer.parseInt(camposVE[i].getText()) 
								== 
								Integer.parseInt(camposVE[j].getText()))) {
					return 5;
				}else {
					if(i < camposVE.length - 1 && j < camposVE.length - 1) {
						if(!camposVE[i].getText().equalsIgnoreCase("") 
								&& Integer.parseInt(camposVE[i].getText()) == Integer.parseInt(camposVE[j].getText())) {
							return 5;
						}
					}
				}
			}
			// Comprobar tambien con this.e
		}
		
		// Comprobamos los tipos de los parametros y su dimension
		String[] tipos = metodo.getTiposParametros();
		int[] dim = metodo.getDimParametros();
		int param = -1;
		for (int i = 0; i < camposVE.length; i++) {
			if((!vueltaAtras) || i < camposVE.length-1){
				param = Integer.parseInt(camposVE[i].getText());
				
				if (dim[param] > 0) {
					return 2;
				}
				switch(tipos[param]) {
				case "int": 
				case "Integer":
				case "double":
				case "Double":
				case "float":
				case "Float":
				case "long":
				case "Long":
				case "short":
				case "Short":
				case "BigInteger":
				case "byte":
				case "Byte":
				case "Number":
					break;
				case "String":
				case "char":
				case "Char":
				default:	// Si es String, char, Char o algun tipo no mencionado -> error
					return 3;	
				}
			}
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

		int num = camposVE.length;
		if(vueltaAtras) {
			// En caso de vuelta atras no se tiene en cuenta el campo para la
			// estimacion de cota
			num--;
		}
		
		for (int i = 0; i < num; i++) {
			if (i != 0) {
				resultado = resultado + ", ";
			}
			resultado = resultado + camposVE[i].getText();
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
		Object fuente = e.getSource();
		int code = e.getKeyCode();
		int numParam = metodo.getNumeroParametros();
		
		switch(code) {
		case KeyEvent.VK_ENTER:
			if(botonesSeleccion[0].isSelected()) {
				accionAceptar(true);
				oov.setAnteriorMaximizacion(true);
				gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					Logger.log_write("Tipo de problema: Maximización");
				}
			} else if(botonesSeleccion[1].isSelected()) {
				accionAceptar(false);
				oov.setAnteriorMaximizacion(false);
				gOpciones.setOpcion(oov, 1);
				if (Conf.fichero_log) {
					Logger.log_write("Tipo de problema: Minimización");
				}
			} else {
				accionCancelar();
			}
			break;
		case KeyEvent.VK_ESCAPE:
			accionCancelar();
			break;
		case KeyEvent.VK_DOWN:
			if(fuente == camposVE[0]) {
				camposVE[1].requestFocusInWindow();
			}else if(fuente == camposVE[1]) {
				camposVE[2].requestFocusInWindow();
			}
			break;
		case KeyEvent.VK_UP:
			if(fuente == camposVE[1]) {
				camposVE[0].requestFocusInWindow();
			}else if(fuente == camposVE[2]) {
				camposVE[1].requestFocusInWindow();
			}
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_BACK_SPACE:
			break;
		default:
			if(fuente == camposVE[0]) {
				if (camposVE[0].getText().length() != 0) { 
					try {
						int x = Integer.parseInt(camposVE[0].getText());
						
						// Comprobar que los indices sean validos
						if (x < 0 || x >= numParam) {
							camposVE[0].requestFocusInWindow();
						} else {
							// Comprobar que la dimension es 0
							int dim = -1;
							dim = metodo.getDimParametro(x);
							if(dim > 0) {
								camposVE[0].requestFocusInWindow();
							} else if(dim != -1){
								camposVE[1].requestFocusInWindow();
							}
						}
						
					} catch (Exception ex) {
						camposVE[0].requestFocusInWindow();
					}
				}

			}else if(fuente == camposVE[1]) {
				if (camposVE[1].getText().length() != 0) { 
					try {
						int x = Integer.parseInt(camposVE[1].getText());
						
						// Comprobar que los indices sean validos
						if (x < 0 || x >= numParam) {
							camposVE[1].requestFocusInWindow();
						} else {
							// Comprobar que la dimension es 0
							int dim = -1;
							dim = metodo.getDimParametro(x);
							if(dim > 0) {
								camposVE[1].requestFocusInWindow();
							} else if(dim != -1){
								camposVE[2].requestFocusInWindow();
							}
						}
						
					} catch (Exception ex) {
						camposVE[1].requestFocusInWindow();
					}
				}
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

	}
}
