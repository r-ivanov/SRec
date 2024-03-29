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
import java.util.ArrayList;

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
 * Permite seleccionar el par�metro del m�todo que contiene la estructura divide
 * y vencer�s para un determinado algoritmo.
 */
public class CuadroIdentificarParametrosDyV extends Thread implements
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
	 * Construye un cuadro que permite seleccionar los par�metros del m�todo que
	 * contiene la estructura divide y vencer�s para un determinado algoritmo.
	 * 
	 * @param ventana
	 *            Ventana a la que quedar� asociada este cuadro.
	 * @param csm
	 *            Cuadro de selecci�n de m�todos desde el que se invoca este
	 *            cuadro.
	 * 
	 * @param numMetodo
	 *            N�mero de m�todo seleccionado para la identificaci�n de
	 *            par�metros DYV.
	 * @param e
	 *            n�mero de par�metro previamente seleccionado que contiene la
	 *            estructura DYV.
	 * @param ind
	 *            n�mero de par�metros con el formato (n,m,...), donde n y m
	 *            representan �ndices que delimitan la estructura DYV.
	 */
	public CuadroIdentificarParametrosDyV(Ventana ventana,
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
		MetodoAlgoritmo ma=null;
		boolean esAlgoritmoPuntos=false;
		ma=this.metodo;
	if(ma!=null) {
		 esAlgoritmoPuntos=	this.esAlgortimoPuntos(ma);
		
	}
	JLabel etiqParamEstructura=new JLabel();
	if(!esAlgoritmoPuntos) {
		 etiqParamEstructura = new JLabel(Texto.get("CIPDYV_PARAMESTR",
				Conf.idioma)
				+ " (0.."
				+ (this.metodo.getNumeroParametros() - 1) + "):");
	}else {
		 etiqParamEstructura = new JLabel(Texto.get("CIPDYV_PARAMESTRX",
				Conf.idioma)
				+ " (0.."
				+ (this.metodo.getNumeroParametros() - 1) + "):");
	}
		this.etiqParamsIndices1 = new JLabel(Texto.get("CIPDYV_PARAMINDI1",
				Conf.idioma));
		this.etiqParamsIndices2 = new JLabel(Texto.get("CIPDYV_PARAMINDI2",
				Conf.idioma)
				+ " (0.."
				+ (this.metodo.getNumeroParametros() - 1) + "):");

		this.panelParamEstructura = new JPanel();
		this.panelParamEstructura.setLayout(new GridLayout(0,1));  

		this.campoEstructura = new JTextField(LONGITUD_CAMPOS);
		this.campoEstructura.setHorizontalAlignment(SwingConstants.CENTER);
		this.campoEstructura.setText(this.e);
		this.campoEstructura.addKeyListener(this);
		JPanel panelCampoEstructura = new JPanel();
		panelCampoEstructura.setLayout(new BorderLayout());
		panelCampoEstructura.add(this.campoEstructura, BorderLayout.WEST);

		this.panelParamEstructura.add(etiqParamEstructura);
		this.panelParamEstructura.add(panelCampoEstructura);
		
		JPanel panelCampoEstructura2 = new JPanel();
		if(esAlgoritmoPuntos) {
		JLabel etiqParamEstructura2 = 
				new JLabel(
						"(El siguiente par�metro contiene la estructura del "
						+ "eje Y)");
		this.panelParamEstructura.add(etiqParamEstructura2);
		}
		this.panelIzqda.add(this.panelParamEstructura, BorderLayout.NORTH);
		//this.panelIzqda.add(panelCampoEstructura2,BorderLayout.SOUTH);

		// panel de la derecha: contiene el gr�fico esquem�tico
		this.panelImagen = new JPanel();
		this.panelImagen.setPreferredSize(new Dimension(300, 162));

		// panel de botones

		// Bot�n Aceptar
		this.aceptar = new BotonAceptar();
		this.aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);
		this.aceptar.addMouseListener(this);

		// Bot�n Cancelar
		this.cancelar = new BotonCancelar();
		this.cancelar.addActionListener(this);
		this.cancelar.addKeyListener(this);
		this.cancelar.addMouseListener(this);

		JPanel panelBoton = new JPanel();
		panelBoton.add(this.aceptar);
		panelBoton.add(this.cancelar);

		// creaci�n del panel general
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
		this.dialogo.setSize(CuadroIdentificarParametrosDyV.ANCHO_CUADRO,
				CuadroIdentificarParametrosDyV.ALTO_CUADRO);
		int coord[] = Conf.ubicarCentro(
				CuadroIdentificarParametrosDyV.ANCHO_CUADRO,
				CuadroIdentificarParametrosDyV.ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);

		this.dialogo
				.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.dialogo.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	accionCancelar();
            }
        });

		pintarCampos();

		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Comprueba si los valores introducidos son correctos y determina si se
	 * debe mostrar un cuadro de error si alguno de los par�metros no es
	 * correcto o devolver los datos de selecci�n al cuadro de selecci�n de
	 * m�todos.
	 */
	private void accionAceptar() {
		int estado = valoresCorrectos();
		if (estado == 0) {
			this.csm.setParametrosMetodo(this.numMetodo,
					this.campoEstructura.getText(), valoresParametrosIndices());
			this.dialogo.setVisible(false);
			dialogo.dispose();
		}
		if (estado != 0 && valoresVacios()) {
			this.csm.marcarMetodo(this.numMetodo, false);
			this.csm.setParametrosMetodo(this.numMetodo,
					this.campoEstructura.getText(), valoresParametrosIndices());
			this.dialogo.setVisible(false);
			dialogo.dispose();
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
		dialogo.dispose();
	}

	/**
	 * Gestiona los eventos de acci�n
	 * 
	 * @param e
	 *            evento de acci�n
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
	 * Determina si alguno de los campos a rellenar est� vacio.
	 * 
	 * @return true, si ning�n valor est� vac�o, false en caso contrario.
	 */
	private boolean valoresVacios() {
		if (this.campoEstructura.getText().replace(" ", "").length() != 0) {
			return false;
		}

		for (int i = 0; i < this.numCampos; i++) {
			if (this.camposIndices[i].getText().replace(" ", "").length() != 0){
				return false;
			}
		}

		return true;
	}

	/**
	 * Valida los valores introducidos. Devuelve un n�mero que representa un
	 * error determinado.
	 * 
	 * @return 0 = todo correcto 1 = n�m. par�metro de estructura no es v�lido 2
	 *         = n�m. par�metro de �ndice no es v�lido 3 = algunos n�m.
	 *         par�metro de �ndices est�n vac�os 4 = valores repetidos
	 */
	private int valoresCorrectos() {

		// Primero comprobamos que todo son n�meros correctamente escritos
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
					if (Integer.parseInt(this.camposIndices[i].getText()) 
								== 
							Integer.parseInt(this.camposIndices[j].getText())) {
						return 4;
					}
				}
			}

			for (int i = 0; i < this.numCampos; i++) {
				if (Integer.parseInt(this.camposIndices[i].getText()) 
						== 
						Integer.parseInt(this.campoEstructura.getText())) {
					return 4;
				}
			}
		}

		// Despu�s comprobamos que el n�mero de par�metro indicado para la
		// estructura tiene realmente una estructura
		String[] tipos = this.metodo.getTiposParametros();
		int[] dim = this.metodo.getDimParametros();

		int ncampo = Integer.parseInt(this.campoEstructura.getText());

		if (dim[ncampo] < 1) {
			return 1;
		}

		// Finalmente comprobamos que los n�meros de par�metro indicados para
		// los �ndices de delimitaci�n son correctos (par�metros int)

		for (int i = 0; i < this.numCampos; i++) {
			try {

				if (this.camposIndices[i].getText().length() == 0) {

				} else {
					if ((dim[Integer.parseInt(
							this.camposIndices[i].getText())] != 0) 
							|| 
							!tipos[Integer.parseInt(
									this.camposIndices[i].getText())].equals(
											"int")) {
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
	 * Devuelve los �ndices que delimitan la estructura DYV.
	 * 
	 * @return Un string con el formato (n, m, ...) donde n y m son �ndices que
	 *         delimitan la estructura.
	 */
	private String valoresParametrosIndices() {
		String resultado = "";

		boolean vacios = true;
		for (int i = 0; i < this.numCampos; i++) {
			if (this.camposIndices[i].getText().replace(" ", "").length() != 0){
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
	 * Actualiza el cuadro con los elementos que debe mostrar seg�n los valores
	 * que se han rellenado hasta el momento.
	 */
	private void pintarCampos() {
		int ncampo = -1;

		try {
			ncampo = Integer.parseInt(this.campoEstructura.getText());
		} catch (Exception e) {

		}

		int dimCampo = -1;
		if (ncampo >= 0 && ncampo < this.metodo.getNumeroParametros()) {
			// Miramos si el par�metro ncampo del m�todo
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
						CuadroIdentificarParametrosDyV.LONGITUD_CAMPOS);
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

			
			
			Icon imagen = new ImageIcon(getClass().getClassLoader().getResource(
					this.numCampos == 2 ? "imagenes/esquema_array.png"
							: "imagenes/esquema_matriz.png"));
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
			// Si se introduce numero de par�metro correcto pero num parametros
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
				// Miramos si el par�metro ncampo del m�todo
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
	 * Gestiona los eventos de rat�n
	 * 
	 * @param e
	 *            evento de rat�n
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de rat�n
	 * 
	 * @param e
	 *            evento de rat�n
	 */
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de rat�n
	 * 
	 * @param e
	 *            evento de rat�n
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de rat�n
	 * 
	 * @param e
	 *            evento de rat�n
	 */
	@Override
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de rat�n
	 * 
	 * @param e
	 *            evento de rat�n
	 */
	@Override
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * Metodo para comprobar si un metodo es Algoritmo de Puntos
	 * 
	 * @param ma
	 *           Metodo Algoritmo
	 */
	
	public boolean esAlgortimoPuntos(MetodoAlgoritmo ma) {
		//	String[] parametros = ma.getParamValores();
		boolean estructurasok=false;
		boolean indicesok=false;
		String[] parametros = new String [ma.getDimParametros().length];
		if(parametros.length>=4) {
			for(int i =0;i<ma.getDimParametros().length;i++) {
				parametros[i]=ma.getDimParametro(i)+" "+ma.getTipoParametro(i);
				//ma.getIndices();
			}
			for(int i =0;i<parametros.length;i++) {
				if(i!=parametros.length-1) {
					if(parametros[i].contains("2")) {
						
						estructurasok=false;
						indicesok=false;
						break;
					}
					if(parametros[i].contains("1")
							&&parametros[i].contains("int")
							&&parametros[i+1].contains("1")
							&&parametros[i+1].contains("int")) {
						estructurasok=true;
					}
					if(parametros[i].contains("0")
							&&parametros[i].contains("int")
							&&parametros[i+1].contains("0")
							&&parametros[i+1].contains("int")) {
						indicesok=true;
					}
				}
			}
		}
		return estructurasok && indicesok;
	}
}
