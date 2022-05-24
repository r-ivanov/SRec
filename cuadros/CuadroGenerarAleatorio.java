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

import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import botones.*;
import conf.*;
import datos.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite construir un cuadro de diálogo para generar valores aleatorios que
 * serán usados para la ejecución del algoritmo.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroGenerarAleatorio extends Thread implements ActionListener,
		KeyListener, MouseListener {

	private static final int MAX_DIM_ARRAY = 1000;
	private static final int ALTO_CELDAS = 20;

	private static final int ANCHO_CUADRO_POR_DEFECTO = 600;
	private static final int ALTO_CUADRO_POR_DEFECTO = 130;
	private static final int ANCHO_CUADRO_POR_DEFECTO_NO_WINDOWS = 744;
	private static final int ALTO_CUADRO_POR_DEFECTO_NO_WINDOWS = 420;

	private JCheckBox marcadorTodos;
	private JCheckBox marcadores[];

	private JTextField cuadrosValLimInf[];
	private JTextField cuadrosValLimSup[];

	private JTextField cuadrosDim1Lim[];
	private JTextField cuadrosDim2Lim[];

	private JRadioButton orden[][];

	private BotonAceptar aceptar = new BotonAceptar();
	private BotonCancelar cancelar = new BotonCancelar();

	private CuadroParamLanzarEjec cple;
	private JDialog dialogo;

	private MetodoAlgoritmo metodo;

	private Ventana ventana;

	/**
	 * Construye un cuadro de diálogo para generar valores aleatorios para los
	 * parámetros de un determinado método, que después serán utilizados para la
	 * ejecución del algoritmo.
	 * 
	 * @param ventana
	 *            Ventana de la aplicación a la que quedará asociado el cuadro.
	 * @param cp
	 *            Cuadro de introducción de parámetros desde el que se invoca
	 *            este diálogo.
	 * @param metodo
	 *            Información del método para el que necesitamos generar valores
	 *            aleatorios para sus parámetros.
	 */
	public CuadroGenerarAleatorio(Ventana ventana, CuadroParamLanzarEjec cple,
			MetodoAlgoritmo metodo) {
		this.ventana = ventana;
		this.cple = cple;
		this.dialogo = new JDialog(cple.getJDialog(), true);
		this.metodo = metodo;
		this.start();
	}

	/**
	 * Ejecuta el thread.
	 */
	@Override
	public void run() {
		this.dialogo.addKeyListener(this);

		// Panel que engloba a los paneles superior y medio
		JPanel panelGral = new JPanel();
		panelGral.setBorder(new TitledBorder(Texto
				.get("CGF_SELEC", Conf.idioma)));
		panelGral.addKeyListener(this);

		// Panel superior, para JCheckBox de marcado de todos los parámetros
		JPanel panelSup = new JPanel();
		BorderLayout blSup = new BorderLayout();
		panelSup.setLayout(blSup);
		this.marcadorTodos = new JCheckBox(Texto.get("CGF_TODOS", Conf.idioma));
		this.marcadorTodos.setToolTipText(Texto.get("CGF_CASILLATODOS",
				Conf.idioma));
		this.marcadorTodos.addActionListener(this);
		this.marcadorTodos.addKeyListener(this);
		this.marcadorTodos.setSelected(true);
		panelSup.add(this.marcadorTodos, BorderLayout.WEST);
		panelSup.setPreferredSize(new Dimension(80, 35));
		panelSup.addKeyListener(this);

		// Panel medio, para JCheckBox de todos los parámetros
		JPanel panelMedio = new JPanel();
		JPanel panelInterior = new JPanel();
		panelMedio.addKeyListener(this);
		panelMedio.setBorder(new TitledBorder(Texto.get("CGF_BORDE",
				Conf.idioma) + " (" + this.metodo.getNombre() + ")"));

		int numFilas = (this.metodo.getNumeroParametros() * 2)
				+ numArrays(this.metodo.getDimParametros());
		this.marcadores = new JCheckBox[this.metodo.getNumeroParametros()];
		this.cuadrosValLimInf = new JTextField[this.metodo
				.getNumeroParametros()];
		this.cuadrosValLimSup = new JTextField[this.metodo
				.getNumeroParametros()];
		this.cuadrosDim1Lim = new JTextField[this.metodo.getNumeroParametros()];
		this.cuadrosDim2Lim = new JTextField[this.metodo.getNumeroParametros()];
		this.orden = new JRadioButton[this.metodo.getNumeroParametros()][];
		GridLayout gl0 = new GridLayout(numFilas, 3);
		panelInterior.setLayout(gl0);

		for (int i = 0; i < this.metodo.getNumeroParametros(); i++) {
			this.marcadores[i] = new JCheckBox(
					this.metodo.getNombreParametro(i)
							+ ": "
							+ this.metodo.getTipoParametro(i)
							+ ServiciosString.cadenaDimensiones(this.metodo
									.getDimParametro(i)));
			this.marcadores[i].setToolTipText(Texto.get("CGF_CASILLAUNO",
					Conf.idioma));
			this.marcadores[i].setSelected(true);

			this.cuadrosValLimInf[i] = new JTextField(5);
			this.cuadrosValLimSup[i] = new JTextField(5);
			this.cuadrosValLimInf[i].setToolTipText(Texto.get("CGF_LIMINF",
					Conf.idioma));
			this.cuadrosValLimSup[i].setToolTipText(Texto.get("CGF_LIMSUP",
					Conf.idioma));
			this.cuadrosValLimInf[i]
					.setHorizontalAlignment(SwingConstants.RIGHT);
			this.cuadrosValLimSup[i]
					.setHorizontalAlignment(SwingConstants.RIGHT);

			if (this.metodo.getTipoParametro(i).contains("boolean")
					|| this.metodo.getTipoParametro(i).contains("String")
					|| this.metodo.getTipoParametro(i).contains("char")) {
				this.cuadrosValLimInf[i].setEnabled(false);
				this.cuadrosValLimSup[i].setEnabled(false);
			}

			// añadimos celda que indica tipo con JCheckBox
			panelInterior.add(creaPanelNombre(this.marcadores[i]));
			// JTextField valor lím. inf
			panelInterior.add(creaPanelLim(
					Texto.get("CGF_LIMITEINF", Conf.idioma),
					this.cuadrosValLimInf[i]));
			// JTextField valor lím. sup
			panelInterior.add(creaPanelLim(
					Texto.get("CGF_LIMITESUP", Conf.idioma),
					this.cuadrosValLimSup[i]));

			this.marcadores[i].addActionListener(this);
			this.marcadores[i].addKeyListener(this);
			this.cuadrosValLimInf[i].addKeyListener(this);
			this.cuadrosValLimSup[i].addKeyListener(this);

			if (this.metodo.getDimParametro(i) > 0) {
				this.cuadrosDim1Lim[i] = new JTextField(5);
				this.cuadrosDim2Lim[i] = new JTextField(5);
				this.cuadrosDim1Lim[i].addKeyListener(this);
				this.cuadrosDim2Lim[i].addKeyListener(this);
				this.cuadrosDim1Lim[i]
						.setHorizontalAlignment(SwingConstants.RIGHT);
				this.cuadrosDim2Lim[i]
						.setHorizontalAlignment(SwingConstants.RIGHT);

				this.cuadrosDim1Lim[i].setToolTipText(Texto.get(
						"CGF_NUMELEM1DIM", Conf.idioma));
				this.cuadrosDim2Lim[i].setToolTipText(Texto.get(
						"CGF_NUMELEM2DIM", Conf.idioma));

				this.orden[i] = new JRadioButton[3];

				if (!this.metodo.getTipoParametro(i).contains("String")
						&& !this.metodo.getTipoParametro(i).contains("boolean")) {
					// Añadimos celda vacía
					panelInterior.add(creaPanelOrden(this.orden[i]));
				} else {
					panelInterior.add(creaPanelVacio());
				}

				// Celda para insertar límite de tamaño de dimensión 1
				panelInterior.add(creaPanelDim(1, this.cuadrosDim1Lim[i]));
				if (this.metodo.getDimParametro(i) > 1) {
					// Celda para insertar límite de tamaño de dimensión 2
					panelInterior.add(creaPanelDim(2, this.cuadrosDim2Lim[i]));
				} else {
					// añadimos celda vacía
					panelInterior.add(creaPanelVacio());
				}
			}
			panelInterior.add(new JSeparator(SwingConstants.HORIZONTAL));
			panelInterior.add(new JSeparator(SwingConstants.HORIZONTAL));
			panelInterior.add(new JSeparator(SwingConstants.HORIZONTAL));
			panelInterior.addKeyListener(this);
		}

		panelMedio.add(panelInterior);

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(this.aceptar);
		panelBoton.add(this.cancelar);

		this.aceptar.addMouseListener(this);
		this.cancelar.addMouseListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar.addKeyListener(this);

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelSup, BorderLayout.NORTH);
		panel.add(panelMedio, BorderLayout.CENTER);
		panel.add(panelBoton, BorderLayout.SOUTH);
		panel.addKeyListener(this);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("CGF_TITULO", Conf.idioma));

		if (this.ventana.msWindows) {
			this.dialogo.setSize(ANCHO_CUADRO_POR_DEFECTO, (numFilas * 20)
					+ ALTO_CUADRO_POR_DEFECTO);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO_POR_DEFECTO,
					(numFilas * 20) + ALTO_CUADRO_POR_DEFECTO);
			this.dialogo.setLocation(coord[0], coord[1]);
		} else {
			this.dialogo.setSize(ANCHO_CUADRO_POR_DEFECTO_NO_WINDOWS,
					ALTO_CUADRO_POR_DEFECTO_NO_WINDOWS);
			int coord[] = Conf.ubicarCentro(
					ANCHO_CUADRO_POR_DEFECTO_NO_WINDOWS,
					ALTO_CUADRO_POR_DEFECTO_NO_WINDOWS);
			this.dialogo.setLocation(coord[0], coord[1]);
		}
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}
	
	/**
	 * Establece el cuadro pasado por parámetro, como destino de los valores generados.
	 * 
	 * @param cuadroParamLanzarEjec Cuadro para lanzar ejecuciones.
	 */
	public void setCuadroParamLanzarEjec(CuadroParamLanzarEjec cuadroParamLanzarEjec) {
		this.cple = cuadroParamLanzarEjec;
	}

	/**
	 * Determina el número de parámetros que son arrays.
	 * 
	 * @param dimensiones
	 *            array con la información del número de dimensiones de cada
	 *            parámetro.
	 * 
	 * @return número de parámetros que son arrays.
	 */
	private int numArrays(int dimensiones[]) {
		int x = 0;

		for (int i = 0; i < dimensiones.length; i++) {
			if (dimensiones[i] > 0) {
				x++;
			}
		}
		return x;
	}

	/**
	 * Crea un nuevo panel para indicar el nombre y tipo de uno de los
	 * parámetros.
	 * 
	 * @param jcb
	 *            checkbox correspondiente al parámetro.
	 * 
	 * @return panel que contiene el checkbox correspondiente.
	 */
	private JPanel creaPanelNombre(JCheckBox jcb) {
		JPanel panel = new JPanel();
		BorderLayout bl = new BorderLayout();
		panel.setLayout(bl);
		panel.add(jcb, BorderLayout.WEST);
		if (this.ventana.msWindows) {
			panel.setPreferredSize(new Dimension(182, ALTO_CELDAS));
		} else {
			panel.setPreferredSize(new Dimension(236, ALTO_CELDAS));
		}
		panel.addKeyListener(this);
		return panel;
	}

	/**
	 * Crea un nuevo panel para indicar el límite inferior o superior de uno de
	 * los parámetros.
	 * 
	 * @param String
	 *            tipo del parámetro.
	 * @param JTextField
	 *            caja de texto para indicar el límite.
	 * 
	 * @return panel que contiene el tipo y la caja de texto correspondiente.
	 */
	private JPanel creaPanelLim(String tipo, JTextField jtf) {
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		JLabel etiq = new JLabel("  " + tipo);

		panel.add(etiq, BorderLayout.CENTER);
		panel.add(jtf, BorderLayout.EAST);
		panel.setPreferredSize(new Dimension(182,
				CuadroGenerarAleatorio.ALTO_CELDAS));
		panel.addKeyListener(this);
		return panel;
	}

	/**
	 * Crea un nuevo panel para indicar el tamaño de los datos para de una de
	 * las dimensiones del parámetro.
	 * 
	 * @param int número de dimension.
	 * @param JTextField
	 *            caja de texto para indicar el tamaño de los datos para la
	 *            dimension.
	 * 
	 * @return panel que contiene información sobre la dimension y la caja de
	 *         texto correspondiente.
	 */
	private JPanel creaPanelDim(int dim, JTextField jtf) {
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		JLabel etiq = new JLabel("  " + Texto.get("CGF_TAMDIM", Conf.idioma)
				+ " " + dim + ":");

		panel.add(etiq, BorderLayout.CENTER);
		panel.add(jtf, BorderLayout.EAST);
		panel.setPreferredSize(new Dimension(182,
				CuadroGenerarAleatorio.ALTO_CELDAS));
		panel.addKeyListener(this);
		return panel;
	}

	/**
	 * Crea un panel vacío para el que se capturarán eventos de teclado.
	 * 
	 * @return panel vacío.
	 */
	private JPanel creaPanelVacio() {
		JPanel panel = new JPanel();
		panel.addKeyListener(this);
		return panel;
	}

	/**
	 * Crea un nuevo panel para indicar el orden de los datos generados para un
	 * determinado parámetro con dimesion mayor que 1 (sin orden, creciente, o
	 * decreciente).
	 * 
	 * @param botones
	 *            array que contiene los botones de selección.
	 * 
	 * @return panel que contiene las distintas opciones.
	 */
	private JPanel creaPanelOrden(JRadioButton botones[]) {
		JPanel panel = new JPanel();
		BorderLayout bl = new BorderLayout();
		panel.setLayout(bl);

		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new BorderLayout());
		botones[0] = new JRadioButton(Texto.get("CGF_SINORDEN", Conf.idioma));
		botones[1] = new JRadioButton(Texto.get("CGF_ORDENCREC", Conf.idioma));
		botones[2] = new JRadioButton(Texto.get("CGF_ORDENDECREC", Conf.idioma));

		botones[0].setToolTipText(Texto.get("CGF_SINORDEN_TTT", Conf.idioma));
		botones[1].setToolTipText(Texto.get("CGF_ORDENCREC_TTT", Conf.idioma));
		botones[2]
				.setToolTipText(Texto.get("CGF_ORDENDECREC_TTT", Conf.idioma));

		for (int i = 0; i < botones.length; i++) {
			botones[i].addActionListener(this);
		}

		botones[0].setSelected(true);

		ButtonGroup bg = new ButtonGroup();
		bg.add(botones[0]);
		bg.add(botones[1]);
		bg.add(botones[2]);
		panelBotones.add(botones[0], BorderLayout.WEST);
		panelBotones.add(botones[1], BorderLayout.CENTER);
		panelBotones.add(botones[2], BorderLayout.EAST);

		panel.add(panelBotones, BorderLayout.EAST);
		if (this.ventana.msWindows) {
			panel.setPreferredSize(new Dimension(182,
					CuadroGenerarAleatorio.ALTO_CELDAS - 10));
		} else {
			panel.setPreferredSize(new Dimension(236,
					CuadroGenerarAleatorio.ALTO_CELDAS - 10));
		}
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel.addKeyListener(this);
		return panel;
	}

	/**
	 * Una vez se han configurado los distintos parámetros para la generación de
	 * valores aleatorios, este método se encarga de generar dichos valores y
	 * devolver los datos al panel que invocó a este.
	 * 
	 * @param i
	 *            número de parámetro.
	 */
	private void peticion(int i) {
		// Manejar excepciones al leer y corregir límites por defecto en función
		// del otro límite si existe
		boolean insertadoMin = this.cuadrosValLimInf[i].getText().length() > 0;
		boolean insertadoSup = this.cuadrosValLimSup[i].getText().length() > 0;

		this.dialogo.setTitle(Texto.get("CGF_TITULO_GEN", Conf.idioma)
				+ (i + 1) + ")");
		GeneradorParam gp = new GeneradorParam();
		String c = this.metodo.getTipoParametro(i)
				+ ServiciosString.cadenaDimensiones(this.metodo
						.getDimParametro(i));

		if (c.contains("int") || c.contains("short")) {
			int limInf = -100, limSup = 100;
			try {
				if (this.cuadrosValLimInf[i].getText().length() > 0) {
					limInf = Integer.parseInt(this.cuadrosValLimInf[i]
							.getText());
				}
				if (this.cuadrosValLimSup[i].getText().length() > 0) {
					limSup = Integer.parseInt(this.cuadrosValLimSup[i]
							.getText());
				}

				if (!c.contains("[]")) {
					gp = new GeneradorParam(limInf, limSup);
				} else if (c.contains("[][]")) {
					int dim[] = extraerLimitesDimensiones(i);
					gp = new GeneradorParam(limInf, limSup, dim[0], dim[1],
							getNumOrden(i));
				} else if (c.contains("[]")) {
					gp = new GeneradorParam(limInf, limSup,
							extraerLimiteDimension(i), getNumOrden(i));
				}
			} catch (Exception exc) {

			}
			if (limInf >= limSup
					&& ((insertadoMin && !insertadoSup) || (!insertadoMin && insertadoSup))) {
				if (insertadoMin) {
					limSup = limInf + 100;
				} else {
					limInf = limSup - 100;
				}
			}
		} else if (c.contains("byte")) {
			int limInf = -128, limSup = 127;
			try {

				if (this.cuadrosValLimInf[i].getText().length() > 0) {
					limInf = Integer.parseInt(this.cuadrosValLimInf[i]
							.getText());
				}

				if (this.cuadrosValLimSup[i].getText().length() > 0) {
					limSup = Integer.parseInt(this.cuadrosValLimSup[i]
							.getText());
				}

				if (!c.contains("[]")) {
					gp = new GeneradorParam(limInf, limSup);
				} else if (c.contains("[][]")) {
					int dim[] = extraerLimitesDimensiones(i);
					gp = new GeneradorParam(limInf, limSup, dim[0], dim[1],
							getNumOrden(i));
				} else if (c.contains("[]")) {
					gp = new GeneradorParam(limInf, limSup,
							extraerLimiteDimension(i), getNumOrden(i));
				}
			} catch (Exception exc) {

			}
		} else if (c.contains("long")) {
			long limInf = -10000, limSup = 10000;
			try {

				if (this.cuadrosValLimInf[i].getText().length() > 0) {
					limInf = Long.parseLong(this.cuadrosValLimInf[i].getText());
				}

				if (this.cuadrosValLimSup[i].getText().length() > 0) {
					limSup = Long.parseLong(this.cuadrosValLimSup[i].getText());
				}

				if (!c.contains("[]")) {
					gp = new GeneradorParam(limInf, limSup);
				} else if (c.contains("[][]")) {
					int dim[] = extraerLimitesDimensiones(i);
					gp = new GeneradorParam(limInf, limSup, dim[0], dim[1],
							getNumOrden(i));
				} else if (c.contains("[]")) {
					gp = new GeneradorParam(limInf, limSup,
							extraerLimiteDimension(i), getNumOrden(i));
				}
			} catch (Exception exc) {

			}
			if (limInf >= limSup
					&& ((insertadoMin && !insertadoSup) || (!insertadoMin && insertadoSup))) {
				if (insertadoMin) {
					limSup = limInf + 1000;
				} else {
					limInf = limSup - 1000;
				}
			}
		} else if (c.contains("float") || c.contains("double")) {
			double limInf = -100.0, limSup = 100.0;
			try {

				if (this.cuadrosValLimInf[i].getText().length() > 0) {
					limInf = Double.parseDouble(this.cuadrosValLimInf[i]
							.getText());
				}

				if (this.cuadrosValLimSup[i].getText().length() > 0) {
					limSup = Double.parseDouble(this.cuadrosValLimSup[i]
							.getText());
				}

				if (!c.contains("[]")) {
					gp = new GeneradorParam(limInf, limSup);
				} else if (c.contains("[][]")) {
					int dim[] = extraerLimitesDimensiones(i);
					gp = new GeneradorParam(limInf, limSup, dim[0], dim[1],
							getNumOrden(i));
				} else if (c.contains("[]")) {
					gp = new GeneradorParam(limInf, limSup,
							extraerLimiteDimension(i), getNumOrden(i));
				}
			} catch (Exception exc) {

			}
			if (limInf >= limSup
					&& ((insertadoMin && !insertadoSup) || (!insertadoMin && insertadoSup))) {
				if (insertadoMin) {
					limSup = limInf + 1000.0;
				} else {
					limInf = limSup - 1000.0;
				}
			}
		} else if (c.contains("char")) {
			try {
				if (!c.contains("[]")) {
					gp = new GeneradorParam(new Character('c'));
				} else if (c.contains("[][]")) {
					int dim[] = extraerLimitesDimensiones(i);
					gp = new GeneradorParam(new Character('c'), dim[0], dim[1],
							getNumOrden(i));
				} else if (c.contains("[]")) {
					gp = new GeneradorParam(new Character('c'),
							extraerLimiteDimension(i), getNumOrden(i));
				}
			} catch (Exception exc) {

			}
		} else if (c.contains("boolean")) {
			try {
				if (!c.contains("[]")) {
					gp = new GeneradorParam(new Boolean(true));
				} else if (c.contains("[][]")) {
					int dim[] = extraerLimitesDimensiones(i);
					gp = new GeneradorParam(new Boolean(true), dim[0], dim[1]);
				} else if (c.contains("[]")) {
					gp = new GeneradorParam(new Boolean(true),
							extraerLimiteDimension(i));
				}
			} catch (Exception exc) {

			}
		} else if (c.contains("String")) {
			try {
				if (!c.contains("[]")) {
					gp = new GeneradorParam(new String(""));
				} else if (c.contains("[][]")) {
					int dim[] = extraerLimitesDimensiones(i);
					gp = new GeneradorParam(new String(""), dim[0], dim[1]);
				} else if (c.contains("[]")) {
					gp = new GeneradorParam(new String(""),
							extraerLimiteDimension(i));
				}
			} catch (Exception exc) {

			}
		}

		String valor = gp.getValor();

		if (Conf.fichero_log) {
			Logger.log_write("Valor aleatorio de " + i + "º parámetro: "
					+ valor);
		}
		
		this.cple.setValor(valor, i);
		
		this.dialogo.setTitle(Texto.get("CGF_TITULO", Conf.idioma));
	}

	/**
	 * Obtiene los valores de los límites para las dimensiones introducidos por
	 * el usuario. Si el usuario no ha introducido ningún límite, se generará un
	 * valor aleatorio entre 1 y 16.
	 * 
	 * @param i
	 *            número del parámetro.
	 * 
	 * @return array cuya posición 0 contiene el valor para la primera
	 *         dimension, y cuya posición 1 contiene el valor para la segunda.
	 */
	private int[] extraerLimitesDimensiones(int i) {
		GeneradorParam gp2;
		int limDim1 = 16;
		int limDim2 = 16;

		if (this.cuadrosDim1Lim[i].getText().length() > 0) {
			limDim1 = Integer.parseInt(this.cuadrosDim1Lim[i].getText());
		} else {
			gp2 = new GeneradorParam(1, limDim1);
			limDim1 = gp2.getValorInt();
		}

		if (this.cuadrosDim2Lim[i].getText().length() > 0) {
			limDim2 = Integer.parseInt(this.cuadrosDim2Lim[i].getText());
		} else {
			gp2 = new GeneradorParam(1, limDim2);
			limDim2 = gp2.getValorInt();
		}

		int x[] = new int[2];
		x[0] = limDim1;
		x[1] = limDim2;
		return x;
	}

	/**
	 * Obtiene el valor del límite de la dimension introducido por el usuario.
	 * Si el usuario no ha introducido ningún límite, se generará un valor
	 * aleatorio entre 1 y 16.
	 * 
	 * @param i
	 *            número del parámetro.
	 * 
	 * @return valor del límite.
	 */
	private int extraerLimiteDimension(int i) {
		GeneradorParam gp2;
		int limDim = 16;

		if (this.cuadrosDim1Lim[i].getText().length() > 0) {
			limDim = Integer.parseInt(this.cuadrosDim1Lim[i].getText());
		} else {
			gp2 = new GeneradorParam(1, limDim);
			limDim = gp2.getValorInt();
		}
		return limDim;
	}

	/**
	 * Comprueba que todos los valores escritos en los cuadros de texto sean
	 * válidos (números o vacíos)
	 * 
	 * @return true si todos los valores son válidos (bien porque son números,
	 *         bien porque están vacíos)
	 */
	private boolean comprobarValores() {
		for (int i = 0; i < this.marcadores.length; i++) {
			if (this.marcadores[i].isSelected()) {
				// Comprobamos escritura correcta
				if (!valorValido(this.cuadrosValLimInf[i],
						this.metodo.getTipoParametro(i))
						|| !valorValido(this.cuadrosValLimSup[i],
								this.metodo.getTipoParametro(i))) {
					return false;
				}
				if (this.metodo.getTipoParametro(i).contains("[]")
						&& !valorValido(this.cuadrosDim1Lim[i])) {
					return false;
				}
				if (this.metodo.getTipoParametro(i).contains("[]")
						&& this.cuadrosDim1Lim[i].getText().length() > 0
						&& Integer.parseInt(this.cuadrosDim1Lim[i].getText()) > CuadroGenerarAleatorio.MAX_DIM_ARRAY) {
					return false;
				}
				if (this.metodo.getTipoParametro(i).contains("[][]")
						&& !valorValido(this.cuadrosDim2Lim[i])) {
					return false;
				}
				if (this.metodo.getTipoParametro(i).contains("[][]")
						&& this.cuadrosDim2Lim[i].getText().length() > 0
						&& Integer.parseInt(this.cuadrosDim2Lim[i].getText()) > CuadroGenerarAleatorio.MAX_DIM_ARRAY) {
					return false;
				}

				// Comprobamos que límite inferior sea menor que límite superior
				if (this.cuadrosValLimInf[i].getText().length() > 0
						&& this.cuadrosValLimSup[i].getText().length() > 0) {
					if (this.metodo.getTipoParametro(i).contains("int")
							|| this.metodo.getTipoParametro(i)
									.contains("short")
							|| this.metodo.getTipoParametro(i).contains("byte")) {
						int min = Integer.parseInt(this.cuadrosValLimInf[i]
								.getText());
						int max = Integer.parseInt(this.cuadrosValLimSup[i]
								.getText());
						if (max <= min) {
							return false;
						}
					} else if (this.metodo.getTipoParametro(i).contains("long")) {
						long min = Long.parseLong(this.cuadrosValLimInf[i]
								.getText());
						long max = Long.parseLong(this.cuadrosValLimSup[i]
								.getText());
						if (max <= min) {
							return false;
						}
					} else if (this.metodo.getTipoParametro(i)
							.contains("float")
							|| this.metodo.getTipoParametro(i).contains(
									"double")) {
						double min = Double
								.parseDouble(this.cuadrosValLimInf[i].getText());
						double max = Double
								.parseDouble(this.cuadrosValLimSup[i].getText());
						if (max <= min) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Comprueba que el valor escrito en un JTextField para dimensiones es un
	 * valor apto (número entero)
	 * 
	 * @return true si el valor es válido
	 */
	private boolean valorValido(JTextField jtf) {
		String texto = jtf.getText();

		if (texto.length() == 0) {
			return true;
		}

		for (int i = 0; i < texto.length(); i++) {
			if (!(ServiciosString.esNumero(texto.charAt(i)))) {
				return false;
			}
		}
		if (texto.length() == 1 && ServiciosString.esNumero(texto.charAt(0))
				&& texto.charAt(0) == '0') {
			// Debemos introducir un valor mayor que cero
			return false;
		}
		return true;
	}

	/**
	 * Comprueba que el valor escrito en un JTextField para valores límite es un
	 * valor apto (número entero)
	 * 
	 * @return true si el valor es válido
	 */
	private boolean valorValido(JTextField jtf, String clase) {
		String texto = jtf.getText();

		if (texto.length() == 0) {
			return true;
		}

		if (texto.length() == 1 && texto.charAt(0) == '-') {
			return false;
		}

		if (clase.contains("int") || clase.contains("short")
				|| clase.contains("byte") || clase.contains("long")) {
			for (int i = 0; i < texto.length(); i++) {
				if (!(ServiciosString.esNumero(texto.charAt(i)))
						&& !(texto.charAt(i) == '-' && i == 0)) {
					return false;
				}
			}
		} else if (clase.contains("float") || clase.contains("double")) {
			int numPuntos = 0;
			int x = 0;
			if (texto.charAt(0) == '-') {
				x = 1;
			}
			if (texto.charAt(0) == '-' && texto.length() < 2) {
				return false;
			}
			for (int i = x; i < texto.length(); i++) {
				if (texto.charAt(i) == '.') {
					numPuntos++;
				}
				if ((!(ServiciosString.esNumero(texto.charAt(i))) && !(texto
						.charAt(i) == '.')) || numPuntos > 1) {
					return false;
				}
			}
		}
		return true;
	}

	private int getNumOrden(int x) {
		for (int i = 0; i < this.orden[x].length; i++) {
			if (this.orden[x][i].isSelected()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.marcadorTodos
				&& this.marcadorTodos.isSelected()) {
			for (int i = 0; i < this.marcadores.length; i++) {
				this.marcadores[i].setSelected(true);
			}
			new Thread() {
				@Override
				public synchronized void run() {
					try {
						wait(100);
					} catch (InterruptedException ie) {
						System.out.println("Error de delay");
					}
					;
					if (comprobarValores()) {
						CuadroGenerarAleatorio.this.aceptar.setEnabled(true);
					} else {
						CuadroGenerarAleatorio.this.aceptar.setEnabled(false);
					}
				}
			}.start();
		} else if (e.getSource().getClass().getCanonicalName()
				.contains("JCheckBox")) {
			for (int i = 0; i < this.marcadores.length; i++) {
				if (e.getSource() == this.marcadores[i]
						&& !(this.marcadores[i].isSelected())) {
					this.marcadorTodos.setSelected(false);
				} else if (e.getSource() == this.marcadores[i]
						&& (this.marcadores[i].isSelected())) {
					boolean marcarMarcadorTodos = true;
					int j = 0;
					do {
						marcarMarcadorTodos = this.marcadores[j].isSelected();
						j++;
					} while (j < this.marcadores.length
							&& marcarMarcadorTodos == true);
					this.marcadorTodos.setSelected(marcarMarcadorTodos);
				}
			}
			new Thread() {
				@Override
				public synchronized void run() {
					try {
						wait(10);
					} catch (InterruptedException ie) {
						System.out.println("Error de delay");
					}
					;
					if (comprobarValores()) {
						CuadroGenerarAleatorio.this.aceptar.setEnabled(true);
					} else {
						CuadroGenerarAleatorio.this.aceptar.setEnabled(false);
					}
				}
			}.start();
		} else if (e.getSource().getClass().getCanonicalName()
				.contains("JRadioButton")) {

			int x = 0;
			for (int i = 0; i < this.orden.length; i++) {
				if (this.orden[i] != null) {
					for (int j = 0; j < this.orden[i].length; j++) {
						if (e.getSource() == this.orden[i][j]) {
							x = i;
						}
					}
				}
			}
			this.marcadores[x].setSelected(true);
			boolean marcarMarcadorTodos = true;
			int j = 0;
			do {
				marcarMarcadorTodos = this.marcadores[j].isSelected();
				j++;
			} while (j < this.marcadores.length && marcarMarcadorTodos == true);
			this.marcadorTodos.setSelected(marcarMarcadorTodos);
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
		if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
			dialogo.dispose();
		} else if (code == KeyEvent.VK_ENTER) {
			if (comprobarValores()) {
				for (int i = 0; i < this.marcadores.length; i++) {
					if (this.marcadores[i].isSelected()) {
						peticion(i);
					}
				}
				this.dialogo.setVisible(false);
				dialogo.dispose();
			} else {
				new CuadroError(this.dialogo, Texto.get("ERROR_VAL",
						Conf.idioma), Texto.get("ERROR_VALESCR", Conf.idioma));
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
		for (int i = 0; i < this.metodo.getNumeroParametros(); i++) {
			if (e.getSource() == this.cuadrosValLimInf[i]
					|| e.getSource() == this.cuadrosValLimSup[i]
					|| e.getSource() == this.cuadrosDim1Lim[i]
					|| e.getSource() == this.cuadrosDim2Lim[i]) {
				this.marcadores[i].setSelected(true);
			}
		}
		boolean marcarMarcadorTodos = true;
		int j = 0;
		do {
			marcarMarcadorTodos = this.marcadores[j].isSelected();
			j++;
		} while (j < this.marcadores.length && marcarMarcadorTodos == true);
		this.marcadorTodos.setSelected(marcarMarcadorTodos);

		new Thread() {
			@Override
			public synchronized void run() {
				try {
					wait(10);
				} catch (InterruptedException ie) {
					System.out.println("Error de delay");
				}
				;
				if (comprobarValores()) {
					CuadroGenerarAleatorio.this.aceptar.setEnabled(true);
				} else {
					CuadroGenerarAleatorio.this.aceptar.setEnabled(false);
				}
			}
		}.start();
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
		if (comprobarValores()) {
			this.aceptar.setEnabled(true);
		} else {
			this.aceptar.setEnabled(false);
		}
		this.cancelar.setEnabled(true);
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == this.aceptar) {
			if (comprobarValores()) {
				for (int i = 0; i < this.marcadores.length; i++) {
					if (this.marcadores[i].isSelected()) {
						peticion(i);
					}
				}
				this.dialogo.setVisible(false);
				dialogo.dispose();
			} else {
				new CuadroError(this.dialogo, Texto.get("ERROR_VAL",
						Conf.idioma), Texto.get("ERROR_VALESCR", Conf.idioma));
			}
		} else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
			dialogo.dispose();
		}
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

	public void setVisible(boolean b) {
		this.dialogo.setVisible(b);
		this.dialogo.setTitle(Texto.get("CGF_TITULO", Conf.idioma));
	}
}
