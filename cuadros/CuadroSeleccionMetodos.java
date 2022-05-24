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
import java.lang.Integer;
import java.util.ArrayList;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import botones.*;
import datos.*;
import opciones.GestorOpciones;
import opciones.OpcionOpsVisualizacion;
import utilidades.*;
import conf.*;
import ventanas.*;

/**
 * Permite construir un nuevo cuadro de diálogo que permite al usuario elegir
 * qué método de una determinada clase se quiere ejecutar.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroSeleccionMetodos extends Thread implements 
		ActionListener, KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 720;
	private static final int ALTO_CUADRO = 420;

	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	private JPanel panel, panelBoton, panelBotones;

	// Numero total de métodos visualizables
	private int numero;

	private JCheckBox botones[];

	private int[] posicBotones;

	// Cuadro de texto para que el usuario escriba el nº de parámetro en el que
	// está la estructura
	private JTextField[] estructura;
	// Cuadro de texto para que el usuario escriba los nº de parámetro en los
	// que están los índices
	private JTextField[] indices;

	private ClaseAlgoritmo clase;

	private OpcionOpsVisualizacion oov ;
	private GestorOpciones gOpciones = new GestorOpciones();
	// Listado de métodos seleccionados para ser procesados
	private ArrayList<MetodoAlgoritmo> metodos;

	private BorderLayout bl;
	private GridLayout gl;

	private Ventana ventana;
	private Preprocesador preprocesador;
	private JDialog dialogo;
	
	private boolean DyV; // true = Divide y Venceras, false = Arboles de busqueda
	private boolean maximizacion;

	/**
	 * Contructor: genera un nuevo cuadro de diálogo que permite al usuario
	 * elegir qué método de una determinada clase se quiere ejecutar
	 * 
	 * @param claseAlgoritmo
	 *            clase a la que pertenece el método que se quiere ejecutar
	 * @param ventana
	 *            ventana a la que se asociará el cuadro de diálogo
	 * @param preprocesador
	 *            gestor que realizará los pasos necesarios para ejecutar el
	 *            método seleccionado
	 */
	public CuadroSeleccionMetodos(ClaseAlgoritmo claseAlgoritmo,
			Ventana ventana_, Preprocesador preprocesador_, boolean DyV_) {
		dialogo = new JDialog(ventana, true);
		ventana = ventana_;
		clase = claseAlgoritmo;
		metodos = claseAlgoritmo.getMetodos();
		numero = metodos.size();
		preprocesador = preprocesador_;
		DyV = DyV_;
		start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		if (numero > 0) {
			String nombreClase = clase.getNombre();

			int llamadasRepresentar = 0;

			for (MetodoAlgoritmo m : metodos) {
				llamadasRepresentar = llamadasRepresentar
						+ m.getMetodosLlamados().length;
			}

			// Panel de Botones
			gl = new GridLayout(numero + llamadasRepresentar, 1);
			panelBotones = new JPanel();
			panelBotones.setLayout(gl);

			posicBotones = new int[numero];

			estructura = new JTextField[numero];
			indices = new JTextField[numero];

			botones = new JCheckBox[numero];

			JPanel panelFilaSup = new JPanel();
			panelFilaSup.setLayout(new BorderLayout());

			JLabel etiqSignatura = new JLabel(Texto.get("CSM_SIGNAT",
					Conf.idioma));

			JPanel panelDerechaSuperior = new JPanel();

			int alturaFilaSuperior = 20;

			JPanel panelEtiqEstructura = new JPanel();
			panelEtiqEstructura.setPreferredSize(
					new Dimension(70, alturaFilaSuperior));
			if(DyV) {
				panelEtiqEstructura.add(
						new JLabel(Texto.get("CSM_FILASUPESTR", Conf.idioma)));
			}else {
				panelEtiqEstructura.add(
						new JLabel(Texto.get("CSM_FILASUPTIPO", Conf.idioma)));
			}
			

			JPanel panelEtiqIndices = new JPanel();
			panelEtiqIndices.setPreferredSize(new Dimension(70, alturaFilaSuperior));
			panelEtiqIndices.add(new JLabel(Texto.get("CSM_FILASUPINDIC", Conf.idioma)));

			JPanel panelEtiqVacia = new JPanel();
			panelEtiqVacia
					.setPreferredSize(new Dimension(3, alturaFilaSuperior));
			panelEtiqVacia.add(new JLabel(""));

			panelDerechaSuperior.add(panelEtiqEstructura, BorderLayout.WEST);
			panelDerechaSuperior.add(panelEtiqIndices, BorderLayout.CENTER);
			panelDerechaSuperior.add(panelEtiqVacia, BorderLayout.EAST);

			panelFilaSup.add(etiqSignatura, BorderLayout.WEST);
			panelFilaSup.add(panelDerechaSuperior, BorderLayout.EAST);

			String toolTipEstr, toolTipInd, toolTipsi, toolTipno;
			if(DyV) {
				toolTipEstr = Texto.get("CSM_INDICAESTR", Conf.idioma);

				toolTipsi = Texto.get("CSM_MARCPROC", Conf.idioma);
				toolTipno = Texto.get("CSM_MARCPROCNO", Conf.idioma);
			}else {
				toolTipEstr = Texto.get("CSM_INDICATIPO", Conf.idioma);

				toolTipsi = Texto.get("CSM_MARCPROC1", Conf.idioma);
				toolTipno = Texto.get("CSM_MARCPROC1NO", Conf.idioma);
			}
			toolTipInd = Texto.get("CSM_INDICAPARAM", Conf.idioma);


			int y = 0;
			for (int x = 0; x < numero; x++) {
				JPanel panelFila = new JPanel(new BorderLayout());

				// Parte derecha
				JPanel panelFilaParteDerecha = new JPanel(new BorderLayout());

				estructura[x] = new JTextField(8);
				indices[x] = new JTextField(8);

				estructura[x].addKeyListener(this);
				indices[x].addKeyListener(this);

				estructura[x].setEnabled(false);
				indices[x].setEnabled(false);

				estructura[x].setHorizontalAlignment(SwingConstants.CENTER);
				indices[x].setHorizontalAlignment(SwingConstants.CENTER);

				estructura[x].setToolTipText(toolTipEstr);
				indices[x].setToolTipText(toolTipInd);

				JPanel panelContenedorEstructura = new JPanel();
				panelContenedorEstructura.add(estructura[x]);
				panelContenedorEstructura
						.setPreferredSize(new Dimension(70, 24));
				JPanel panelContenedorIndices = new JPanel();
				panelContenedorIndices.add(indices[x]);
				panelContenedorIndices.setPreferredSize(new Dimension(70, 24));

				panelFilaParteDerecha.add(
						panelContenedorEstructura, BorderLayout.WEST);
				panelFilaParteDerecha.add(
						panelContenedorIndices, BorderLayout.CENTER);

				// Parte izquierda
				JPanel panelFilaParteIzquierda = new JPanel();

				String representacion = metodos.get(x).getRepresentacion();
				int[] dimParametros = metodos.get(x).getDimParametros();
				boolean hayArrayOMatriz = false;
				for (int i = 0; i < dimParametros.length; i++) {
					if (dimParametros[i] > 0) {
						hayArrayOMatriz = true;
					}
				}

				botones[x] = new JCheckBox(representacion);
				botones[x].setName(representacion);

				posicBotones[x] = y;

				if (hayArrayOMatriz) {
					botones[x].setToolTipText(toolTipsi);
				} else {
					botones[x].setToolTipText(toolTipno);
					botones[x].setEnabled(false);
				}

				// panelBotones.add(botones[x],x);
				panelFilaParteIzquierda.add(botones[x]);
				botones[x].addKeyListener(this);
				botones[x].addActionListener(this);

				panelFila.add(panelFilaParteIzquierda, BorderLayout.WEST);
				panelFila.add(panelFilaParteDerecha, BorderLayout.EAST);

				panelFila.setPreferredSize(new Dimension(550, 25));

				panelBotones.add(panelFila, y);
				y++;

				int[] llamadas = metodos.get(x).getMetodosLlamados();
				for (int i = 0; i < llamadas.length; i++) {
					panelFila = new JPanel();
					panelFila.setLayout(new BorderLayout());

					JPanel izqdaLlamada = new JPanel();
					JLabel etiquetaSignaturaLlamado = new JLabel(
							"            - "
									+ clase.getMetodoID(llamadas[i])
											.getRepresentacion());
					if (!hayArrayOMatriz) {
						etiquetaSignaturaLlamado.setEnabled(false);
					}
					izqdaLlamada.add(etiquetaSignaturaLlamado);

					panelFila.setPreferredSize(new Dimension(550, 25));

					panelFila.add(izqdaLlamada, BorderLayout.WEST);

					panelBotones.add(panelFila, y);

					y++;
				}

			}

			// Botón Aceptar
			aceptar = new BotonAceptar();
			aceptar.addKeyListener(this);
			aceptar.addMouseListener(this);

			// Botón Cancelar
			cancelar = new BotonCancelar();
			cancelar.addKeyListener(this);
			cancelar.addMouseListener(this);

			// Panel para el botón
			panelBoton = new JPanel();
			panelBoton.add(aceptar);
			panelBoton.add(cancelar);

			// Panel general
			bl = new BorderLayout();
			panel = new JPanel();
			panel.setLayout(bl);

			JPanel panelContenedorBotones = new JPanel();
			panelContenedorBotones.setLayout(new BorderLayout());
			panelContenedorBotones.add(panelBotones, BorderLayout.NORTH);

			JScrollPane jsp = new JScrollPane(panelContenedorBotones);

			// jsp.add(panelBotones);
			jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
			jsp.setPreferredSize(new Dimension(ANCHO_CUADRO - 10,
					ALTO_CUADRO - 120));

			JPanel panelSup = new JPanel();
			panelSup.setLayout(new BorderLayout());

			panelSup.add(panelFilaSup, BorderLayout.NORTH);
			panelSup.add(jsp, BorderLayout.CENTER);
			jsp.setVerticalScrollBarPolicy(
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			panelSup.setBorder(
					new TitledBorder(
							numero + " " 
							+ Texto.get("CSM_METDISP", Conf.idioma) 
							+ " " + nombreClase));

			panel.add(panelSup, BorderLayout.NORTH);
			panel.add(panelBoton, BorderLayout.SOUTH);

			// Preparamos y mostramos cuadro
			dialogo.getContentPane().add(panel);
			if(DyV) {
				dialogo.setTitle(Texto.get("CSM_ESCMET", Conf.idioma));
			}else {
				dialogo.setTitle(Texto.get("CSM_ESCMET2", Conf.idioma));
			}
			
			// dialogo.setSize(anchoCuadro,altoCuadro+(alto*numero));
			dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
			dialogo.setLocation(coord[0], coord[1]);
			dialogo.setResizable(false);
			dialogo.setVisible(true);

		} else {
			new CuadroError(ventana,
					Texto.get("ERROR_CLASE", Conf.idioma), Texto.get(
							"CSM_NOVISMET", Conf.idioma));
		}
	}

	/**
	 * Recoge la selección del método señalado por el usuario y da paso al
	 * cuadro de parámetros
	 */
	private void recogerMetodosSeleccionados() {
		boolean huboError = false;

		for (int i = 0; i < clase.getNumMetodos(); i++) {
			if ((botones[i].isSelected() || !(botones[i].isSelected()))) {
				clase.getMetodo(i).setMarcadoProcesar(true);

				if (botones[i].isSelected()) {
					if(DyV) {
						clase.getMetodo(i).setTecnica(MetodoAlgoritmo.TECNICA_DYV);
						Ventana.thisventana.habilitarOpcionesDYV(true);
						
						clase.getMetodo(i).setIndices(
								Integer.parseInt(estructura[i].getText().replace(" ", "")));

						int parametrosIndice[] = ServiciosString.extraerValoresInt(indices[i].getText(), ',');

						if (parametrosIndice == null) {
							clase.getMetodo(i).setIndices(
									Integer.parseInt(estructura[i].getText().replace(" ", "")) + 1);
						} else if (parametrosIndice.length == 2) {
							clase.getMetodo(i).setIndices(
									Integer.parseInt(estructura[i].getText().replace(" ", "")) + 1,
									parametrosIndice[0] + 1,
									parametrosIndice[1] + 1);
						} else {
							clase.getMetodo(i).setIndices(
									Integer.parseInt(estructura[i].getText().replace(" ", "")) + 1,
									parametrosIndice[0] + 1,
									parametrosIndice[1] + 1,
									parametrosIndice[2] + 1,
									parametrosIndice[3] + 1);
						}
					}else {
						clase.getMetodo(i).setTecnica(MetodoAlgoritmo.TECNICA_AABB); 
						
						int parametrosIndice[] = ServiciosString.extraerValoresInt(indices[i].getText(), ',');
						
						if (parametrosIndice == null) {
							huboError = true;
						} else if(parametrosIndice.length == 2) { // Vuelta Atras	
							clase.getMetodo(i).setSolParcial(parametrosIndice[0]);
							clase.getMetodo(i).setMejorSol(parametrosIndice[1]);
							clase.getMetodo(i).setMaximizacion(maximizacion);
							clase.getMetodo(i).setRyP(false);
						} else if(parametrosIndice.length == 3) { // RyP
							clase.getMetodo(i).setSolParcial(parametrosIndice[0]);
							clase.getMetodo(i).setMejorSol(parametrosIndice[1]);
							clase.getMetodo(i).setCota(parametrosIndice[2]);
							clase.getMetodo(i).setMaximizacion(maximizacion);	
							clase.getMetodo(i).setRyP(true);
						}else {
							huboError = true;
						}
					}
				} else {
					clase.getMetodo(i).setTecnica(MetodoAlgoritmo.TECNICA_REC);
				}
			} else {
				huboError = true;
			}
		}

		if (!huboError) {
			dialogo.setVisible(false);
			preprocesador.fase2(clase);
			dialogo.dispose();
		} else {
			new CuadroError(dialogo, Texto.get("ERROR_VAL", Conf.idioma),
					Texto.get("ERROR_VALESCR", Conf.idioma));
		}

	}

	/**
	 * Permite marcar/desmarcar un método.
	 * 
	 * @param numMetodo
	 *            Número del método.
	 * @param valor
	 *            true para marcar, false para desmarcar.
	 */
	public void marcarMetodo(int numMetodo, boolean valor) {
		botones[numMetodo].setSelected(valor);
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
			recogerMetodosSeleccionados();
		} else if (e.getSource() == cancelar) {
			dialogo.setVisible(false);
			oov.setAnteriorVE(0);
			gOpciones.setOpcion(oov, 1);
			if (Conf.fichero_log) {
				Logger.log_write("Ninguna vista específica");
			}
			preprocesador.fase2(clase);

			dialogo.dispose();
		} else if (e.getSource().getClass().getName().contains("JCheckBox")) {
			for (int i = 0; i < numero; i++) {
				if (botones[i] == e.getSource()) {
					if (botones[i].isSelected()) {
						if(DyV) {
							new CuadroIdentificarParametrosDyV(Ventana.thisventana,
									this, metodos.get(i), i,
									estructura[i].getText(),
									indices[i].getText());
						}else {
							new CuadroIdentificarParametrosAABB(Ventana.thisventana,
									this, metodos.get(i), i);
						}
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
			recogerMetodosSeleccionados();
		} else if (code == KeyEvent.VK_ESCAPE) {
			oov.setAnteriorVE(0);
			gOpciones.setOpcion(oov, 1);
			dialogo.setVisible(false);
			if (Conf.fichero_log) {
				Logger.log_write("Ninguna vista específica");
			}
			preprocesador.fase2(clase);
			dialogo.dispose();
		} else {
			if (e.getSource() instanceof JTextField) {
				for (int i = 0; i < estructura.length; i++) {
					if (e.getSource() == estructura[i]
							|| e.getSource() == indices[i]) {
						botones[i].setSelected(true);
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
		if (e.getSource() == aceptar) {
			recogerMetodosSeleccionados();
		} else if (e.getSource() == cancelar) {
			oov = (OpcionOpsVisualizacion) gOpciones.getOpcion("OpcionOpsVisualizacion", false);
			oov.setAnteriorVE(0);
			gOpciones.setOpcion(oov, 1);
			dialogo.setVisible(false);
			if (Conf.fichero_log) {
				Logger.log_write("Ninguna vista específica");
			}
			preprocesador.fase2(clase);
			dialogo.dispose();
		}
	}

	/**
	 * Permite asignar el parámetro del método que contiene la estructura divide
	 * y vencerás para un determinado algoritmo.
	 */
	public void setParametrosMetodo(int i, String paramE, String paramI) {
		estructura[i].setText(paramE);
		indices[i].setText(paramI);
	}
	/**
	 * Permite asignar los parámetros del método que contienen 
	 * la solucion parcial, la mejor solución y la cota 
	 * para un determinado algoritmo basado en árboles de búsqueda.
	 */
	public void setParametrosMetodo(int i, String paramI, boolean vueltaAtras, boolean maximizacion_) {
		maximizacion  = maximizacion_;
		estructura[i].setText(vueltaAtras ? "Vuelta Atras" : "RyP");
		indices[i].setText(paramI);
	}

}