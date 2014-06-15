package cuadros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;
import conf.*;
import botones.*;
import datos.*;
import utilidades.*;
import ventanas.*;

/**
 * Implementa el cuadro que permite buscar llamadas concretas en la ejecución
 * actual.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroBuscarLlamada extends Thread implements ActionListener,
		KeyListener {

	private static final int ANCHO_CUADRO = 460;
	private static final int ALTO_CUADRO = 215;
	
	private Ventana ventana;
	
	private JComboBox<String> selectorMetodo;

	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	private BotonTexto restaurar;

	private JPanel panel, panelBoton, panelSelecMetodo, panelContenedorValores;

	private DatosTrazaBasicos dtb;

	private JComboBox<String> campos[];

	private JDialog dialogo;

	private JTextComponent[] editor = null;

	private int numMetodo;

	/**
	 * Crea un nuevo cuadro que permite buscar llamadas en la ejecución actual.
	 * 
	 * @param ventana
	 *            ventana a la que quedará asociado el cuadro.
	 * @param dtb
	 *            datos de traza básicos de la ejecución actual.
	 */
	public CuadroBuscarLlamada(Ventana ventana, DatosTrazaBasicos dtb) {
		this.dialogo = new JDialog(ventana, true);
		this.dtb = dtb;
		this.ventana = ventana;
		this.start();
	}

	/**
	 * Crea el cuadro de parámetros
	 */
	public void run() {

		ValoresParametros.inicializar(false);

		if (dtb != null) {
			
			ArrayList<DatosMetodoBasicos> metodos = dtb.getMetodos();

			// Panel Selección Método
			selectorMetodo = new JComboBox<String>();
			for (int i = 0; i < metodos.size(); i++) {
				if (metodos.get(i).getEsVisible()) {
					selectorMetodo.addItem(metodos.get(i).getInterfaz());
				}
			}
			selectorMetodo.addActionListener(this);
			selectorMetodo.addKeyListener(this);

			panelSelecMetodo = new JPanel();
			panelSelecMetodo.setLayout(new BorderLayout());
			panelSelecMetodo.add(selectorMetodo);
			panelSelecMetodo.setBorder(new TitledBorder(Texto.get(
					"CUBSQ_SELMET", Conf.idioma)));

			// Panel Inserción valores
			panelContenedorValores = new JPanel();
			panelContenedorValores.setLayout(new BorderLayout());

			String interfazMetodo = metodos.get(0).getInterfaz();

			String[][] valoresParam = ventana.getTraza()
					.getValoresParametros(interfazMetodo, true);
			String[][] valoresResult = ventana.getTraza()
					.getValoresResultado(interfazMetodo, true);

			JPanel ppa = panelParametros(metodos.get(0), valoresParam,
					valoresResult);
			panelContenedorValores.add(ppa, BorderLayout.NORTH);

			JScrollPane jsp = new JScrollPane(panelContenedorValores);

			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {
				jsp.setBorder(new TitledBorder(Texto.get("CUBSQ_VALPAMRES",
						Conf.idioma)));
			} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
				jsp.setBorder(new TitledBorder(Texto.get("CUBSQ_VALPAM",
						Conf.idioma)));
			} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
				jsp.setBorder(new TitledBorder(Texto.get("CUBSQ_VALRES",
						Conf.idioma)));
			}

			// Botones

			// Botón Aceptar
			aceptar = new BotonAceptar();
			aceptar.addActionListener(this);
			aceptar.addKeyListener(this);

			// Botón Restaurar
			restaurar = new BotonTexto(Texto.get("PARB_RESTAURAR", Conf.idioma));
			restaurar.setPreferredSize(new Dimension(95, 23));
			restaurar.addActionListener(this);
			restaurar.addKeyListener(this);

			// Botón Cancelar
			cancelar = new BotonCancelar();
			cancelar.addActionListener(this);
			cancelar.addKeyListener(this);

			// Panel para los botones
			panelBoton = new JPanel();
			panelBoton.add(aceptar);
			panelBoton.add(restaurar);
			panelBoton.add(cancelar);

			// Panel general
			panel = new JPanel();
			panel.setLayout(new BorderLayout());

			panel.add(panelSelecMetodo, BorderLayout.NORTH);
			panel.add(jsp, BorderLayout.CENTER);
			panel.add(panelBoton, BorderLayout.SOUTH);

			dialogo.getContentPane().add(panel);
			dialogo.setTitle(Texto.get("CUBSQ_TITULO", Conf.idioma));

			// Preparamos y mostramos cuadro
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
			dialogo.setLocation(coord[0], coord[1]);
			dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
			dialogo.setResizable(false);
			dialogo.setVisible(true);
		}
	}
	
	/**
	 * Devuelve la posición del método seleccionado con respecto
	 * a los métodos contenidos en los datos de traza básicos.
	 * 
	 * @return posicion del método para la lista de métodos de los
	 * datos de traza básicos.
	 */
	private int numMetodoSeleccionado() {
		int numMetodo = selectorMetodo.getSelectedIndex();
		int aux = 0, posic = 0;
		for (int i = 0; i < this.dtb.getNumMetodos(); i++) {
			if (aux == numMetodo) {
				posic = i;
			}
			if (this.dtb.getMetodo(i).getEsVisible()) {
				aux++;
			}
		}
		return posic;
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == selectorMetodo) {
			int numMetodo = numMetodoSeleccionado();

			String valoresE[][] = ventana.getTraza()
					.getValoresParametros(
							this.dtb.getMetodo(numMetodo).getInterfaz(), true);
			String valoresS[][] = ventana.getTraza()
					.getValoresResultado(
							this.dtb.getMetodo(numMetodo).getInterfaz(), true);

			JPanel panelMetodo = panelParametros(this.dtb.getMetodo(numMetodo),
					valoresE, valoresS);

			panelContenedorValores.removeAll();
			panelContenedorValores.add(panelMetodo, BorderLayout.NORTH);
			panelContenedorValores.updateUI();
			
		} else if (e.getSource() == aceptar) {
			accion();
		} else if (e.getSource() == restaurar) {
			ventana.getTraza().iluminar(numMetodo, null, null,
					false);
			ventana.refrescarFormato();
			dialogo.setVisible(false);
		} else if (e.getSource() == cancelar) {
			dialogo.setVisible(false);
		}
	}
	
	/**
	 * Tras introducir los datos y pulsar aceptar, se ejecuta el siguiente método
	 * que comprueba si los valores introducidos son correctos. Si es así comenzará
	 * la búsqueda y se ocultara el diálogo, en caso contrario se mostrará un error
	 * avisando de ello.
	 */
	private void accion() {
		if (valoresCorrectos()) {
			activarBusqueda();
			dialogo.setVisible(false);
		} else {
			new CuadroError(Ventana.thisventana, Texto.get("ERROR_PARAM",
					Conf.idioma),
					Texto.get("ERROR_PARAMINCSIMPLE", Conf.idioma));
		}
	}
	
	/**
	 * Comprueba si los parametros introducidos son correctos teniendo
	 * en cuenta el formato del tipo de dato que espera recibir.
	 * 
	 * @return Devuelve true si los valores son correctos, false en caso contrario.
	 */
	private boolean valoresCorrectos() {
		int numMetodo = numMetodoSeleccionado();

		String[] tiposE = this.dtb.getMetodo(numMetodo).getTipoParametrosE();
		String[] tiposS = this.dtb.getMetodo(numMetodo).getTipoParametrosS();
		int[] dimE = this.dtb.getMetodo(numMetodo).getDimE();
		int[] dimS = this.dtb.getMetodo(numMetodo).getDimS();

		String[] tipos = new String[tiposE.length + tiposS.length];
		int[] dim = new int[dimE.length + dimS.length];

		for (int i = 0; i < tiposE.length; i++) {
			tipos[i] = tiposE[i];
			dim[i] = dimE[i];
		}

		for (int i = 0; i < tiposS.length; i++) {
			tipos[i + tiposE.length] = tiposS[i];
			dim[i + dimE.length] = dimS[i];
		}

		for (int i = 0; i < campos.length; i++) {
			if (this.campos[i] != null
					&& this.campos[i].getSelectedItem().toString().length() > 0)
				if (!ServiciosString.esDeTipoCorrecto(this.campos[i]
						.getSelectedItem().toString(), tipos[i], dim[i]))
					return false;
		}
		return true;
	}
	
	/**
	 * Una vez que se ha comprobado que los parámetros son correctos, se procede
	 * a obtener los valores de entrada y salida (dependiendo de las opciones de
	 * configuración establecidas) con el formato correcto, para posteriormente
	 * pedir a la traza que ilumine los nodos que cumplan con las condiciones calculadas.
	 */
	private void activarBusqueda() {
		int numMetodo = numMetodoSeleccionado();
		DatosMetodoBasicos dmb = this.dtb.getMetodo(numMetodo);

		int numE = dmb.getNumParametrosE();
		int numS = dmb.getNumParametrosS();

		String[] valoresE = new String[numE];
		String[] tiposE = new String[numE];
		int[] dimE = new int[numE];

		String[] valoresS = new String[numS];
		String[] tiposS = new String[numS];
		int[] dimS = new int[numS];

		int posic = 0;

		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
				|| Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
			for (int i = 0; i < dmb.getNumParametrosE(); i++) {
				if (dmb.getVisibilidadE(i)) {
					valoresE[i] = this.campos[posic].getSelectedItem()
							.toString();
					tiposE[i] = dmb.getTipoParametroE(i);
					dimE[i] = dmb.getDimParametroE(i);
					posic++;
				}
			}
		}

		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
				|| Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
			for (int i = 0; i < dmb.getNumParametrosS(); i++) {
				if (dmb.getVisibilidadS(i)) {
					valoresS[i] = this.campos[posic].getSelectedItem()
							.toString();
					tiposS[i] = dmb.getTipoParametroS(i);
					dimS[i] = dmb.getDimParametroS(i);
					posic++;
				}
			}
		}

		for (int i = 0; i < valoresE.length; i++) {
			if (ServiciosString.tieneContenido(valoresE[i])) {
				valoresE[i] = ServiciosString.adecuarParametro(valoresE[i],
						tiposE[i], dimE[i]);
			}
		}

		for (int i = 0; i < valoresS.length; i++) {
			if (ServiciosString.tieneContenido(valoresS[i])) {
				valoresS[i] = ServiciosString.adecuarParametro(valoresS[i],
						tiposS[i], dimS[i]);
			}
		}

		if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
			valoresE = null;
		} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
			valoresS = null;
		}

		if (Conf.fichero_log) {
			DatosMetodoBasicos ma = this.dtb.getMetodo(numMetodoSeleccionado());
			String mensaje = "Información > Buscar llamada...: "
					+ ma.getNombre() + "(";

			for (int i = 0; i < valoresE.length; i++) {
				if (i != 0) {
					mensaje += ",";
				}
				mensaje += valoresE[i];
			}
			mensaje += ") [";
			for (int i = 0; i < valoresS.length; i++) {
				if (i != 0) {
					mensaje += ",";
				}
				mensaje += valoresS[i];
			}
			mensaje += "]";
			Logger.log_write(mensaje);
		}

		ventana.getTraza().iluminar(numMetodoSeleccionado(),
				valoresE, valoresS, true);
		ventana.refrescarFormato();
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		if (code != KeyEvent.VK_ENTER && e.getSource() == selectorMetodo) {
			int numMetodo = numMetodoSeleccionado();

			String valoresE[][] = ventana.getTraza()
					.getValoresParametros(
							this.dtb.getMetodo(numMetodo).getInterfaz(), true);
			String valoresS[][] = ventana.getTraza()
					.getValoresResultado(
							this.dtb.getMetodo(numMetodo).getInterfaz(), true);

			JPanel panelMetodo = panelParametros(this.dtb.getMetodo(numMetodo),
					valoresE, valoresS);

			panelContenedorValores.removeAll();
			panelContenedorValores.add(panelMetodo, BorderLayout.NORTH);
			panelContenedorValores.updateUI();
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ENTER)
			if (e.getSource().getClass().getName().contains("JTextField")
					|| e.getSource().getClass().getName()
							.contains("BorderlessTextField"))
				accion();
			else if (e.getSource().getClass().getName().contains("JComboBox"))
				campos[0].requestFocus();
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	public void keyTyped(KeyEvent e) {

	}
	
	/**
	 * Permite construir el panel que albergará los distintos parámetros
	 * de entrada y de salida, de manera que sea posible seleccionar mediante
	 * un combobox los distintos parámetros de la búsqueda de entre todos los
	 * existentes en la ejecución.
	 */
	private JPanel panelParametros(DatosMetodoBasicos metodo,
			String[][] valoresE, String[][] valoresS) {

		int numParamE = metodo.getNumParametrosE();
		int numParam = 0;

		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {
			numParam = numParamE
					+ (metodo.getTipo().equals("void") ? metodo
							.getNumParametrosE() : 1);
			
			// Si no es método void, recorremos visiblidad de "parámetros de salida"
			if (numParam == (numParamE * 2)) {
				for (int i = 0; i < numParamE; i++) {
					if (!metodo.getVisibilidadS(i)) {
						numParam--;
					}
				}
			}

			for (int i = 0; i < numParamE; i++) {
				if (!metodo.getVisibilidadE(i)) {
					numParam--;
				}
			}
			
		} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
			numParam = numParamE;
			for (int i = 0; i < numParamE; i++) {
				if (!metodo.getVisibilidadE(i)) {
					numParam--;
				}
			}
		} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
			numParam = (metodo.getTipo().equals("void") ? metodo
					.getNumParametrosE() : 1);
			
			// Si no es método void, recorremos visiblidad de "parámetros de salida"
			if (numParam == (numParamE * 2)) { 
				for (int i = 0; i < numParamE; i++) {
					if (!metodo.getVisibilidadS(i)) {
						numParam--;
					}
				}
			}
		}

		JPanel panelValoresParam = new JPanel();
		panelValoresParam.setLayout(new BorderLayout());

		JPanel panelValoresParamI = new JPanel();
		JPanel panelValoresParamD = new JPanel();
		panelValoresParamI.setLayout(new GridLayout(numParam, 1));
		panelValoresParamD.setLayout(new GridLayout(numParam, 1));

		campos = new JComboBox[numParam];
		editor = new JTextComponent[numParam];

		String textoEntrada = Texto.get("ETIQFL_ENTR", Conf.idioma);
		String textoSalida = Texto.get("ETIQFL_SALI", Conf.idioma);

		int posic = 0;

		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
				|| Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA)
			for (int i = 0; i < metodo.getNumParametrosE(); i++) {
				if (metodo.getVisibilidadE(i)) {
					String textoEtiqueta = textoEntrada + ": "
							+ metodo.getTipoParametroE(i);
					if (metodo.getDimParametroE(i) > 0) {
						textoEtiqueta = textoEtiqueta
								+ " "
								+ ServiciosString.cadenaDimensiones(metodo
										.getDimParametroE(i));
					}

					textoEtiqueta = textoEtiqueta + " "
							+ metodo.getNombreParametroE(i) + "  ";

					JLabel etiqueta = new JLabel(textoEtiqueta);

					campos[posic] = new JComboBox<String>();
					campos[posic].setEditable(true);
					campos[posic].setPreferredSize(new Dimension(250, 20));

					campos[posic].addItem("");

					for (int j = 0; j < valoresE.length; j++) {
						if (valoresE[j][i] != null) {
							campos[posic].addItem(valoresE[j][i]);
						}
					}

					editor[posic] = (JTextComponent) campos[posic].getEditor()
							.getEditorComponent();
					editor[posic].addKeyListener(this);

					panelValoresParamI.add(etiqueta);
					panelValoresParamD.add(campos[posic]);

					posic++;
				}
			}

		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
				|| Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
			if (metodo.getTipo().equals("void")) {
				if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
					numParamE = 0;
				}

				for (int i = 0; i < metodo.getNumParametrosE(); i++) {
					if (metodo.getVisibilidadS(i)) {
						String textoEtiqueta = textoSalida + ": "
								+ metodo.getTipoParametroS(i);
						if (metodo.getDimParametroS(i) > 0) {
							textoEtiqueta = textoEtiqueta
									+ " "
									+ ServiciosString.cadenaDimensiones(metodo
											.getDimParametroS(i));
						}

						textoEtiqueta = textoEtiqueta + " "
								+ metodo.getNombreParametroS(i) + "  ";

						JLabel etiqueta = new JLabel(textoEtiqueta);

						campos[posic] = new JComboBox<String>();
						campos[posic].setEditable(true);
						campos[posic].setPreferredSize(new Dimension(250, 20));

						campos[posic].addItem("");

						for (int j = 0; j < valoresS.length; j++) {
							if (valoresS[j][i] != null) {
								campos[posic].addItem(valoresS[j][i]);
							}
						}

						editor[posic] = (JTextComponent) campos[posic]
								.getEditor().getEditorComponent();
						editor[posic].addKeyListener(this);

						panelValoresParamI.add(etiqueta);
						panelValoresParamD.add(campos[posic]);

						posic++;
					}
				}
			} else {
				String textoEtiqueta = textoSalida + ": " + metodo.getTipo();
				if (metodo.getDimTipo() > 0) {
					textoEtiqueta = textoEtiqueta
							+ " "
							+ ServiciosString.cadenaDimensiones(metodo
									.getDimTipo());
				}

				textoEtiqueta = textoEtiqueta + " "
						+ Texto.get("NOMBRE_RETORNO", Conf.idioma);

				JLabel etiqueta = new JLabel(textoEtiqueta);

				campos[campos.length - 1] = new JComboBox<String>();
				campos[campos.length - 1].setEditable(true);
				campos[campos.length - 1].setPreferredSize(new Dimension(250,
						20));

				campos[campos.length - 1].addItem("");

				for (int j = 0; j < valoresS.length; j++) {
					if (valoresS[j][0] != null) {
						campos[campos.length - 1].addItem(valoresS[j][0]);
					}
				}

				editor[campos.length - 1] = (JTextComponent) campos[campos.length - 1]
						.getEditor().getEditorComponent();
				editor[campos.length - 1].addKeyListener(this);

				panelValoresParamI.add(etiqueta);
				panelValoresParamD.add(campos[campos.length - 1]);
			}
		}

		panelValoresParam.add(panelValoresParamI, BorderLayout.WEST);
		panelValoresParam.add(panelValoresParamD, BorderLayout.CENTER);

		return panelValoresParam;
	}

}
