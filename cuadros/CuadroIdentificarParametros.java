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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import conf.Conf;
import botones.BotonAceptar;
import botones.BotonCancelar;
import datos.MetodoAlgoritmo;
import utilidades.Texto;
import ventanas.Ventana;

/**
 * Permite seleccionar el parámetro del método que contiene la estructura divide
 * y vencerás para un determinado algoritmo.
 */
public class CuadroIdentificarParametros extends Thread implements
		ActionListener, KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 590;
	private static final int ALTO_CUADRO = 260;
	private static final int LONGITUD_CAMPOS = 4;

	private JDialog dialogo;

	private BotonAceptar aceptar;
	private BotonCancelar cancelar;

	private String e;
	private String ind;

	private MetodoAlgoritmo metodo;

	private int numCampos = -1;

	private JTextField campoEstructura;
	private JTextField[] camposIndices;

	private int numMetodo = -1;
	private CuadroSeleccionMetodos csm;

	private JLabel etiqParamsIndices1, etiqParamsIndices2;

	private JPanel panelIzqda;
	private JPanel panelParamEstructura;
	private JPanel panelImagen;
	private JPanel panelCuadro;

	private boolean avancePermitido = false;

	/**
	 * Construye un cuadro que permite seleccionar los parámetros del método que
	 * contiene la estructura divide y vencerás para un determinado algoritmo.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociada este cuadro.
	 * @param csm
	 *            Cuadro de selección de métodos desde el que se invoca este
	 *            cuadro.
	 * 
	 * @param numMetodo
	 *            Número de método seleccionado para la identificación de
	 *            parámetros DYV.
	 * @param e
	 *            número de parámetro previamente seleccionado que contiene la
	 *            estructura DYV.
	 * @param ind
	 *            número de parámetros con el formato (n,m,...), donde n y m
	 *            representan índices que delimitan la estructura DYV.
	 */
	public CuadroIdentificarParametros(Ventana ventana,
			CuadroSeleccionMetodos csm, MetodoAlgoritmo metodo, int numMetodo,
			String e, String ind) {
		this.metodo = metodo;
		this.dialogo = new JDialog(ventana, true);
		this.e = e;
		this.ind = ind;
		this.csm = csm;
		this.numMetodo = numMetodo;
		this.start();
	}

	/**
	 * Ejecuta el Thread asociado al cuadro.
	 */
	@Override
	public void run() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		// panel de la izquierda: contiene las etiquetas y los cuadros de texto

		this.panelIzqda = new JPanel();
		this.panelIzqda.setLayout(new BorderLayout());

		JLabel etiqParamEstructura = new JLabel(Texto.get("CIPDYV_PARAMESTR",
				Conf.idioma)
				+ " (0.."
				+ (this.metodo.getNumeroParametros() - 1) + "):");
		this.etiqParamsIndices1 = new JLabel(Texto.get("CIPDYV_PARAMINDI1",
				Conf.idioma));
		this.etiqParamsIndices2 = new JLabel(Texto.get("CIPDYV_PARAMINDI2",
				Conf.idioma)
				+ " (0.."
				+ (this.metodo.getNumeroParametros() - 1) + "):");

		this.panelParamEstructura = new JPanel();
		this.panelParamEstructura.setLayout(new BorderLayout());

		this.campoEstructura = new JTextField(LONGITUD_CAMPOS);
		this.campoEstructura.setHorizontalAlignment(SwingConstants.CENTER);
		this.campoEstructura.setText(this.e);
		this.campoEstructura.addKeyListener(this);
		JPanel panelCampoEstructura = new JPanel();
		panelCampoEstructura.setLayout(new BorderLayout());
		panelCampoEstructura.add(this.campoEstructura, BorderLayout.WEST);

		this.panelParamEstructura.add(etiqParamEstructura, BorderLayout.NORTH);
		this.panelParamEstructura.add(panelCampoEstructura, BorderLayout.SOUTH);

		this.panelIzqda.add(this.panelParamEstructura, BorderLayout.NORTH);

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
		this.panelCuadro = new JPanel();
		this.panelCuadro.setLayout(new BorderLayout());
		this.panelCuadro.add(this.panelIzqda, BorderLayout.WEST);
		this.panelCuadro.add(this.panelImagen, BorderLayout.EAST);
		this.panelCuadro.setBorder(new TitledBorder(Texto.get("CIPDYV_METODO",
				Conf.idioma) + ": " + this.metodo.getRepresentacionTotal()));

		panel.add(this.panelCuadro, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);

		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("CIPDYV_TITULO", Conf.idioma));
		this.dialogo.setSize(CuadroIdentificarParametros.ANCHO_CUADRO,
				CuadroIdentificarParametros.ALTO_CUADRO);
		int coord[] = Conf.ubicarCentro(
				CuadroIdentificarParametros.ANCHO_CUADRO,
				CuadroIdentificarParametros.ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);

		this.dialogo
				.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		pintarCampos();

		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Comprueba si los valores introducidos son correctos y determina si se
	 * debe mostrar un cuadro de error si alguno de los parámetros no es
	 * correcto o devolver los datos de selección al cuadro de selección de
	 * métodos.
	 */
	private void accionAceptar() {
		int estado = valoresCorrectos();
		if (estado == 0) {
			this.csm.setParametrosMetodo(this.numMetodo,
					this.campoEstructura.getText(), valoresParametrosIndices());
			this.dialogo.setVisible(false);
		}
		if (estado != 0 && valoresVacios()) {
			this.csm.marcarMetodo(this.numMetodo, false);
			this.csm.setParametrosMetodo(this.numMetodo,
					this.campoEstructura.getText(), valoresParametrosIndices());
			this.dialogo.setVisible(false);
		} else if (estado != 0) {
			new CuadroError(this.dialogo, Texto.get("ERROR_VAL", Conf.idioma),
					Texto.get("ERROR_INFOPARAM" + estado, Conf.idioma));
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
			accionAceptar();
		} else if (e.getSource() == this.cancelar) {
			accionCancelar();
		}

	}

	/**
	 * Determina si alguno de los campos a rellenar está vacio.
	 * 
	 * @return true, si ningún valor está vacío, false en caso contrario.
	 */
	private boolean valoresVacios() {
		if (this.campoEstructura.getText().replace(" ", "").length() != 0) {
			return false;
		}

		for (int i = 0; i < this.numCampos; i++) {
			if (this.camposIndices[i].getText().replace(" ", "").length() != 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Valida los valores introducidos. Devuelve un número que representa un
	 * error determinado.
	 * 
	 * @return 0 = todo correcto 1 = núm. parámetro de estructura no es válido 2
	 *         = núm. parámetro de índice no es válido 3 = algunos núm.
	 *         parámetro de índices están vacíos 4 = valores repetidos
	 */
	private int valoresCorrectos() {

		// Primero comprobamos que todo son números correctamente escritos
		try {
			int x = Integer.parseInt(this.campoEstructura.getText());
			if (x < 0 || x >= this.metodo.getNumeroParametros()) {
				return 1;
			}
		} catch (Exception e) {
			return 1;
		}

		int vacios = 0;
		try {
			for (int i = 0; i < this.numCampos; i++) {
				if (this.camposIndices[i].getText().length() != 0) {
					int x = Integer.parseInt(this.camposIndices[i].getText());
					if (x < 0 || x >= this.metodo.getNumeroParametros()) {
						return 2;
					}
				} else {
					vacios++;
				}
			}
		} catch (Exception e) {
			return 2;
		}

		// Si usuario ha dejado algunos en blanco... mal (o deja todos o rellena
		// todos)
		if (vacios != 0 && vacios != this.numCampos) {
			return 3;
		}

		// Comprobamos que no haya ni un solo valor repetido
		if (vacios == 0) {
			for (int i = 0; i < this.numCampos; i++) {
				for (int j = i + 1; j < this.numCampos; j++) {
					if (Integer.parseInt(this.camposIndices[i].getText()) == Integer
							.parseInt(this.camposIndices[j].getText())) {
						return 4;
					}
				}
			}

			for (int i = 0; i < this.numCampos; i++) {
				if (Integer.parseInt(this.camposIndices[i].getText()) == Integer
						.parseInt(this.campoEstructura.getText())) {
					return 4;
				}
			}
		}

		// Después comprobamos que el número de parámetro indicado para la
		// estructura tiene realmente una estructura
		String[] tipos = this.metodo.getTiposParametros();
		int[] dim = this.metodo.getDimParametros();

		int ncampo = Integer.parseInt(this.campoEstructura.getText());

		if (dim[ncampo] < 1) {
			return 1;
		}

		// Finalmente comprobamos que los números de parámetro indicados para
		// los índices de delimitación son correctos (parámetros int)

		for (int i = 0; i < this.numCampos; i++) {
			try {

				if (this.camposIndices[i].getText().length() == 0) {

				} else {
					if ((dim[Integer.parseInt(this.camposIndices[i].getText())] != 0)
							|| !tipos[Integer.parseInt(this.camposIndices[i]
									.getText())].equals("int")) {
						return 2;
					}
				}
			} catch (Exception e) {
				return 2;
			}
		}

		return 0;
	}

	/**
	 * Devuelve los índices que delimitan la estructura DYV.
	 * 
	 * @return Un string con el formato (n, m, ...) donde n y m son índices que
	 *         delimitan la estructura.
	 */
	private String valoresParametrosIndices() {
		String resultado = "";

		boolean vacios = true;
		for (int i = 0; i < this.numCampos; i++) {
			if (this.camposIndices[i].getText().replace(" ", "").length() != 0) {
				vacios = false;
			}
		}

		if (vacios) {
			return "";
		}

		for (int i = 0; i < this.numCampos; i++) {
			if (i != 0) {
				resultado = resultado + ", ";
			}
			resultado = resultado + this.camposIndices[i].getText();

		}
		return resultado;
	}

	/**
	 * Actualiza el cuadro con los elementos que debe mostrar según los valores
	 * que se han rellenado hasta el momento.
	 */
	private void pintarCampos() {
		int ncampo = -1;

		try {
			ncampo = Integer.parseInt(this.campoEstructura.getText());
		} catch (Exception e) {

		}

		int dimCampo = -1;
		if (ncampo != -1 && ncampo >= 0
				&& ncampo < this.metodo.getNumeroParametros()) {
			// Miramos si el parámetro ncampo del método
			dimCampo = this.metodo.getDimParametro(ncampo);
			this.numCampos = dimCampo * 2;
		}
		switch (dimCampo) {
		case 1:
		case 2:
			JLabel[] etiqIndices = new JLabel[this.numCampos];
			this.camposIndices = new JTextField[this.numCampos];
			JPanel panelParamIndices = new JPanel();
			panelParamIndices.setLayout(new GridLayout(this.numCampos + 2, 1));

			panelParamIndices.add(this.etiqParamsIndices1);
			panelParamIndices.add(this.etiqParamsIndices2);

			JPanel[] panelFilaIndice = new JPanel[this.numCampos];
			String[] textosInd = this.ind.replace(" ", "").split(",");

			for (int i = 0; i < this.numCampos; i++) {
				this.camposIndices[i] = new JTextField(
						CuadroIdentificarParametros.LONGITUD_CAMPOS);
				etiqIndices[i] = new JLabel("  "
						+ Texto.get("CIPDYV_PARAMINDICE" + this.numCampos + i,
								Conf.idioma));
				this.camposIndices[i]
						.setHorizontalAlignment(SwingConstants.CENTER);
				this.camposIndices[i].addKeyListener(this);
				if (i < textosInd.length) {
					this.camposIndices[i].setText(textosInd[i]);
				}
				panelFilaIndice[i] = new JPanel();
				panelFilaIndice[i].setLayout(new BorderLayout());
				panelFilaIndice[i]
						.add(this.camposIndices[i], BorderLayout.WEST);
				panelFilaIndice[i].add(etiqIndices[i], BorderLayout.CENTER);
				panelParamIndices.add(panelFilaIndice[i]);
			}

			this.panelIzqda.removeAll();
			this.panelIzqda.add(this.panelParamEstructura, BorderLayout.NORTH);

			Icon imagen = new ImageIcon(
					this.numCampos == 2 ? "imagenes/esquema_array.png"
							: "imagenes/esquema_matriz.png");
			JLabel etiquetaImagen = new JLabel();
			etiquetaImagen.setIcon(imagen);

			this.panelImagen.removeAll();
			this.panelImagen.add(etiquetaImagen, BorderLayout.CENTER);
			this.panelIzqda.add(panelParamIndices, BorderLayout.SOUTH);

			if (this.metodo.getNumeroParametros() < 10) {
				// Ponemos el cursor sobre el siguiente campo
				this.camposIndices[0].requestFocus();
			}
			this.avancePermitido = true;
			break;
		default:
			this.avancePermitido = false;
			this.camposIndices = null;
			this.panelImagen.removeAll();
			this.panelIzqda.removeAll();
			this.panelIzqda.add(this.panelParamEstructura, BorderLayout.NORTH);

		}

		if ((dimCampo != 1) && (dimCampo != 2)) {
			this.campoEstructura.requestFocus();
		} else if (this.metodo.getNumeroParametros() >= 10) {
			// Si se introduce numero de parámetro correcto pero num parametros
			// >= 10, mantiene el foco
			this.campoEstructura.requestFocus();
		}

		this.panelImagen.updateUI();
		this.panelIzqda.updateUI();
		this.panelCuadro.updateUI();

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
		if (code == KeyEvent.VK_ENTER) {
			accionAceptar();
		} else if (code == KeyEvent.VK_ESCAPE) {
			accionCancelar();
		} else if (fuente == this.campoEstructura) {
			if ((code == KeyEvent.VK_LEFT) || (code == KeyEvent.VK_RIGHT)
					|| (code == KeyEvent.VK_UP)) {
				// No hacemos nada
			}
			// AvancePermitido nos dice si ya estan cargados (y no cambian) los
			// campos de texto inferiores
			else if ((this.avancePermitido) && (code == KeyEvent.VK_DOWN)) {
				this.camposIndices[0].requestFocus();
			} else {
				pintarCampos();
			}
		}

		// Comprobamos si se ha escrito en alguno de los campos para el resto de
		// parametros
		else {
			int ncampo = -1;
			try {
				ncampo = Integer.parseInt(this.campoEstructura.getText());
			} catch (Exception ex) {

			}
			int dimCampo = -1;
			if (ncampo != -1 && ncampo >= 0
					&& ncampo < this.metodo.getNumeroParametros()) {
				// Miramos si el parámetro ncampo del método
				dimCampo = this.metodo.getDimParametro(ncampo);
				this.numCampos = dimCampo * 2;
			}
			boolean detectado = false;
			boolean avance = false;
			boolean retroceso = false;
			int i;
			for (i = 0; i < this.numCampos; i++) {
				boolean esNumero = true;
				try {
					Integer.parseInt(this.camposIndices[i].getText());
				} catch (NumberFormatException eg) {
					esNumero = false;
				}
				if ((fuente == this.camposIndices[i]) && (esNumero)
						&& (code != KeyEvent.VK_BACK_SPACE)
						&& (code != KeyEvent.VK_LEFT)
						&& (code != KeyEvent.VK_RIGHT)
						&& (code != KeyEvent.VK_UP)
						&& (code != KeyEvent.VK_DOWN)) {
					detectado = true;
					break;
				}
				// Avance
				else if ((fuente == this.camposIndices[i])
						&& (code == KeyEvent.VK_DOWN)) {
					avance = true;
					detectado = true;
					break;
				}
				// Retroceso
				else if ((fuente == this.camposIndices[i])
						&& (code == KeyEvent.VK_UP)) {
					retroceso = true;
					detectado = true;
					break;
				}
			}
			if ((detectado)) {
				if (avance) {
					if (i < this.numCampos - 1) {
						// Ponemos el cursor sobre el siguiente campo
						this.camposIndices[i + 1].requestFocus();
					}
				} else if (retroceso) {
					if (i == 0) {
						this.campoEstructura.requestFocus();
					} else {
						this.camposIndices[i - 1].requestFocus();
					}
				} else if (this.metodo.getNumeroParametros() < 10) {
					if (i < this.numCampos - 1) {
						// Ponemos el cursor sobre el siguiente campo
						this.camposIndices[i + 1].requestFocus();
					}
				}
			}
			detectado = false;
			avance = false;
			retroceso = false;
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
