package cuadros;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.border.TitledBorder;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import conf.*;
import botones.*;
import datos.*;
import utilidades.*;
import ventanas.*;

/**
 * Implementa el cuadro que recoge los parámetros desde la interfaz y permite
 * lanzar una ejecución.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroParamLanzarEjec extends Thread implements ActionListener,
		KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 550;
	private static final int ALTO_CUADRO = 92;
	private static final int ALTURA_PISO = 23;

	private CuadroGenerarAleatorio cga = null;

	private JLabel[] etiquetas;
	private JComboBox<String>[] cuadrosvalores;
	private JCheckBox[] mostrarvalores;

	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	private BotonTexto generar;
	private BotonTexto cargar;
	private BotonTexto guardar;
	private BotonTexto verValores;

	private JPanel panel, panelBoton, panelParam;
	private int numero;
	private boolean editarValores;

	private MetodoAlgoritmo metodo;
	private ClaseAlgoritmo clase;

	private String[] valores;

	private boolean estamosCargando = false;

	private Ventana ventana;
	private Preprocesador p;
	private JDialog dialogo;
	private AlmacenValores almacenValores = new AlmacenValores();

	/**
	 * Crea un nuevo cuadro que recoge los parámetros desde la interfaz y
	 * permite lanzar una ejecución.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 * 
	 * @param metodo
	 *            método que se ha seleccionado para modificar sus valores de
	 *            entrada
	 * @param clase
	 *            Clase a la que pertenece el método para la que se introducirán
	 *            los parámetros.
	 * @param p
	 *            Preprocesador que permitirá lanzar la ejecución una vez
	 *            seleccionados los parámetros.
	 */
	public CuadroParamLanzarEjec(Ventana ventana, MetodoAlgoritmo metodo,
			ClaseAlgoritmo clase, Preprocesador p) {

		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;

		ValoresParametros.inicializar(false);

		if (!metodo.getTipo().equals("void")) {
			this.numero = metodo.getNumeroParametros();
		} else {
			this.numero = metodo.getNumeroParametros() * 2;
		}

		this.metodo = metodo;
		this.clase = clase;
		this.p = p;
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		this.editarValores = true;
		if (this.numero > 0) {

			this.etiquetas = new JLabel[this.numero];
			this.cuadrosvalores = new JComboBox[this.numero];
			this.mostrarvalores = new JCheckBox[this.numero];

			// Tendremos n campos: n parámetros
			if (!this.metodo.getTipo().equals("void")) {
				for (int i = 0; i < this.numero; i++) {
					this.etiquetas[i] = new JLabel();
					this.etiquetas[i].setText(Texto.get("ETIQFL_ENTR",
							Conf.idioma)
							+ ": "
							+ this.metodo.getNombreParametro(i)
							+ " ("
							+ this.metodo.getTipoParametro(i)
							+ ServiciosString.cadenaDimensiones(this.metodo
									.getDimParametro(i)) + ")");
					this.cuadrosvalores[i] = new JComboBox<String>();
					this.cuadrosvalores[i].setEditable(true);
					this.cuadrosvalores[i].setEnabled(this.editarValores);
					ValoresParametros.introducirValores(this.cuadrosvalores[i],
							this.metodo.getTipoParametro(i),
							this.metodo.getDimParametro(i));
					ValoresParametros.introducirValor(this.cuadrosvalores[i],
							this.metodo.getTipoParametro(i),
							this.metodo.getDimParametro(i),
							this.metodo.getParamValor(i), true);
					this.cuadrosvalores[i].addKeyListener(this);
					if (this.cuadrosvalores[i].isEnabled()) {
						this.cuadrosvalores[i].setToolTipText(Texto.get(
								"CPARAM_ESCRVALADEC", Conf.idioma)
								+ " "
								+ this.metodo.getTipoParametro(i));
					}
					this.mostrarvalores[i] = new JCheckBox(Texto.get(
							"ETIQFL_ENTR", Conf.idioma)
							+ ": "
							+ this.metodo.getNombreParametro(i)
							+ " ("
							+ this.metodo.getTipoParametro(i)
							+ ServiciosString.cadenaDimensiones(this.metodo
									.getDimParametro(i)) + ")");
					this.mostrarvalores[i].setSelected(true);
					this.mostrarvalores[i].setToolTipText(Texto.get(
							"CPARAM_CASILVIS", Conf.idioma));
					this.mostrarvalores[i].addKeyListener(this);
					this.mostrarvalores[i].addMouseListener(this);
				}
			} else {
				// Tendremos n*2 campos: n parámetros vistos a la entrada y n
				// vistos a la salida.
				for (int i = 0; i < this.numero; i++) {
					this.etiquetas[i] = new JLabel();
					this.cuadrosvalores[i] = new JComboBox<String>();
					this.mostrarvalores[i] = new JCheckBox();
					this.cuadrosvalores[i].addKeyListener(this);
					if (i < (this.numero / 2)) {
						this.mostrarvalores[i].setText(Texto.get("ETIQFL_ENTR",
								Conf.idioma)
								+ ": "
								+ this.metodo.getNombreParametro(i)
								+ " ("
								+ this.metodo.getTipoParametro(i)
								+ ServiciosString.cadenaDimensiones(this.metodo
										.getDimParametro(i)) + ")");
						this.cuadrosvalores[i].setEditable(true);
						this.cuadrosvalores[i].setEnabled(this.editarValores);
						ValoresParametros.introducirValores(
								this.cuadrosvalores[i],
								this.metodo.getTipoParametro(i),
								this.metodo.getDimParametro(i));
						ValoresParametros.introducirValor(
								this.cuadrosvalores[i],
								this.metodo.getTipoParametro(i),
								this.metodo.getDimParametro(i),
								this.metodo.getParamValor(i), true);
						if (this.cuadrosvalores[i].isEnabled()) {
							this.cuadrosvalores[i].setToolTipText(Texto.get(
									"CPARAM_ESCRVALADEC", Conf.idioma)
									+ " "
									+ this.metodo.getTipoParametro(i));
						}
					} else {
						this.mostrarvalores[i]
								.setText(Texto.get("ETIQFL_SALI", Conf.idioma)
										+ ": "
										+ this.metodo.getNombreParametro(i
												- (this.numero / 2))
										+ " ("
										+ this.metodo.getTipoParametro(i
												- (this.numero / 2))
										+ ServiciosString.cadenaDimensiones(this.metodo
												.getDimParametro(i
														- (this.numero / 2)))
										+ ")");
						this.cuadrosvalores[i].setEnabled(false);
						if (this.cuadrosvalores[i].isEnabled()) {
							this.cuadrosvalores[i].setToolTipText(Texto.get(
									"CPARAM_ESCRVALADEC", Conf.idioma)
									+ " "
									+ this.metodo.getTipoParametro(i
											- (this.numero / 2)));
						}
					}
					if (i < this.metodo.getNumeroParametros()) {
						this.mostrarvalores[i].setSelected(this.metodo
								.getVisibilidadEntrada(i));
					} else {
						this.mostrarvalores[i].setSelected(this.metodo
								.getVisibilidadSalida(i
										- this.metodo.getNumeroParametros()));
					}
					if (this.cuadrosvalores[i].isEnabled()) {
						this.mostrarvalores[i].setToolTipText(Texto.get(
								"CPARAM_CASILVIS", Conf.idioma));
					}
					this.mostrarvalores[i].addKeyListener(this);
					this.mostrarvalores[i].addMouseListener(this);
				}
			}

			// Panel vertical para etiquetas
			GridLayout gl1 = new GridLayout(this.numero, 1);
			JPanel panelEtiquetas = new JPanel();
			panelEtiquetas.setLayout(gl1);
			for (int i = 0; i < this.numero; i++) {
				panelEtiquetas.add(this.mostrarvalores[i]);
			}

			// Panel vertical para cuadros de valores
			GridLayout gl2 = new GridLayout(this.numero, 1);
			JPanel panelCuadros = new JPanel();
			panelCuadros.setLayout(gl2);
			for (int i = 0; i < this.numero; i++) {
				panelCuadros.add(this.cuadrosvalores[i]);
			}

			JTextComponent[] editor = new JTextComponent[this.cuadrosvalores.length];
			for (int i = 0; i < editor.length; i++) {
				editor[i] = (JTextComponent) this.cuadrosvalores[i].getEditor()
						.getEditorComponent();
				editor[i].addKeyListener(this);
			}

			// Panel general de parámetros
			BorderLayout bl = new BorderLayout();
			this.panelParam = new JPanel();
			this.panelParam.setLayout(bl);
			this.panelParam.setBorder(new TitledBorder(Texto.get(
					"CPARAM_INSERVPAR", Conf.idioma)
					+ " "
					+ this.metodo.getNombre()));
			this.panelParam.add(panelEtiquetas, BorderLayout.WEST);
			this.panelParam.add(panelCuadros, BorderLayout.CENTER);

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
			
			// Botón Ver Valores
			this.verValores = new BotonTexto(Texto.get("BOTONVALORES", Conf.idioma), 100);
			this.verValores.addActionListener(this);
			this.verValores.addKeyListener(this);
			this.verValores.addMouseListener(this);	

			// Botón Generar
			this.generar = new BotonTexto(
					Texto.get("BOTONGENERAR", Conf.idioma));
			this.generar.addActionListener(this);
			this.generar.addKeyListener(this);
			this.generar.addMouseListener(this);

			// Botón Cargar
			this.cargar = new BotonTexto(Texto.get("BOTONCARGAR", Conf.idioma));
			this.cargar.addActionListener(this);
			this.cargar.addKeyListener(this);
			this.cargar.addMouseListener(this);

			// Botón Almacenar
			this.guardar = new BotonTexto(
					Texto.get("BOTONGUARDAR", Conf.idioma));
			this.guardar.addActionListener(this);
			this.guardar.addKeyListener(this);
			this.guardar.addMouseListener(this);

			if (!this.cuadrosvalores[0].isEnabled()) {
				this.generar.setEnabled(false);
				this.cargar.setEnabled(false);
				this.guardar.setEnabled(false);
			}

			// Panel para el botón
			this.panelBoton = new JPanel();
			this.panelBoton.add(this.aceptar);
			this.panelBoton.add(this.cancelar);
			this.panelBoton.add(this.verValores);
			this.panelBoton.add(this.generar);
			this.panelBoton.add(this.cargar);
			this.panelBoton.add(this.guardar);

			// Panel general
			bl = new BorderLayout();
			this.panel = new JPanel();
			this.panel.setLayout(bl);

			this.panel.add(this.panelParam, BorderLayout.NORTH);
			this.panel.add(this.panelBoton, BorderLayout.SOUTH);

			this.dialogo.getContentPane().add(this.panel);
			if (this.numero != 1) {
				this.dialogo.setTitle(Texto.get("CPARAM_PARAMS", Conf.idioma));
			} else {
				this.dialogo.setTitle(Texto.get("CPARAM_PARAM", Conf.idioma));
			}

			// Preparamos y mostramos cuadro
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO
					+ (ALTURA_PISO * this.numero));
			this.dialogo.setLocation(coord[0], coord[1]);
			this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO
					+ (ALTURA_PISO * this.numero));
			this.dialogo.setResizable(false);
			this.dialogo.setVisible(true);

			this.foco();

		}
	}

	private void foco() {
		this.cuadrosvalores[0].requestFocusInWindow();
		this.cuadrosvalores[0].requestFocus();
		this.mostrarvalores[0].transferFocus();
	}
	
	private boolean comprobarYAsignarValores() {
		for (int i = 0; i < this.metodo.getNumeroParametros(); i++) {
			String texto = (String) (this.cuadrosvalores[i]
					.getSelectedItem());
			if (texto.length() == 0) {
				new CuadroError(this.ventana, Texto.get("ERROR_PARAM",
						Conf.idioma), Texto.get("CPARAM_NOVALOR",
						Conf.idioma) + " nº " + (i + 1) + ".");
				return false;
			}
			
			List<String> valores = ParametrosParser.reemplazarYPartirValores(texto);
			if (!ParametrosParser.comprobarValoresParametro(valores,
					this.metodo.getTipoParametro(i),
					this.metodo.getDimParametro(i))) {
				new CuadroError(this.ventana, Texto.get("ERROR_PARAM",
						Conf.idioma), Texto.get("CPARAM_ELPARAM",
						Conf.idioma)
						+ " nº"
						+ (i + 1)
						+ " ( "
						+ this.metodo.getTipoParametro(i)
						+ ServiciosString.cadenaDimensiones(this.metodo
								.getDimParametro(i))
						+ " ) "
						+ Texto.get("CPARAM_NOESCORR", Conf.idioma));
				return false;
			}
		}
		
		for (int i = 0; i < this.metodo.getNumeroParametros(); i++) {
			String texto = (String) (this.cuadrosvalores[i]
					.getSelectedItem());
			this.metodo.setParamValor(i, texto.replace(" ", ""));
		}
		
		return true;		
	}
	
	/**
	 * Comprueba y recoge los valores
	 * 
	 * @param valoresFinales
	 *            true si el usuario ha dado por buenos los datos introducidos
	 *            false si es una comprobación interna del programa, no
	 *            solicitada por el usuario
	 * 
	 * @param true si los datos introducidos son válidos
	 */
	private synchronized boolean recogerValores(boolean valoresFinales) {
		if (this.editarValores) {
			if (!comprobarYAsignarValores()) {
				return false;
			}			
		}
		
		if (!comprobarVisibilidadCorrecta()) {
			return false;
		}

		// En tercer lugar, actualizamos la visibilidad de los parámetros del
		// método
		for (int i = 0; i < this.numero; i++) {
			if (i < this.metodo.getNumeroParametros()) {
				this.metodo.setVisibilidadEntrada(
						this.mostrarvalores[i].isSelected(), i);
			} else {
				this.metodo.setVisibilidadSalida(
						this.mostrarvalores[i].isSelected(),
						i - this.metodo.getNumeroParametros());
			}
		}

		if (valoresFinales) {
			this.dialogo.setVisible(false);
		}

		for (int i = 0; i < this.metodo.getNumeroParametros(); i++) {
			ValoresParametros.anadirValorListados(
					(String) (this.cuadrosvalores[i].getSelectedItem()),
					this.metodo.getTipoParametro(i),
					this.metodo.getDimParametro(i));
		}

		if (Conf.fichero_log) {
			MetodoAlgoritmo ma = this.clase.getMetodoPrincipal();
			String[] paramMA = ma.getParamValores();
			String mensaje = "Lanzar ejecución: " + ma.getNombre() + "(";
			for (int i = 0; i < paramMA.length; i++) {
				if (i > 0) {
					mensaje += ", ";
				}
				mensaje += paramMA[i];
			}
			mensaje += ");";
			Logger.log_write(mensaje);
		}
		Ventana.thisventana.setClase(this.clase);
		this.dialogo.setVisible(false);
		
		final CuadroProceso cuadroProceso = new CuadroProceso(Ventana.thisventana,
				Texto.get("CP_ESPERE", Conf.idioma), Texto.get("CP_EJECUTANDO", Conf.idioma));
		
		Thread proceso = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					p.ejecutarAlgoritmo(new PreprocesadorEjecucionListener() {
						@Override
						public void ejecucionFinalizada(final List<Ejecucion> ejecuciones, final boolean satisfactoria) {					
							SwingUtilities.invokeLater(new Runnable() {						
								@Override
								public void run() {
									cuadroProceso.cerrar();
									if (satisfactoria) {
										if (ejecuciones.size() > 1) {
											FamiliaEjecuciones.getInstance().habilitar();
											for (Ejecucion e : ejecuciones) {
												FamiliaEjecuciones.getInstance().addEjecucion(e);
											}
											FamiliaEjecuciones.getInstance().setPrimeraEjecucionActiva();
										} else {
											FamiliaEjecuciones.getInstance().deshabilitar();
											if (ejecuciones.size() > 0) {
												Ventana.thisventana.visualizarEjecucion(ejecuciones.get(0), true);
											}
										}
									}
								}
							});
						}
					});
				} catch (Throwable e) {
					/* Debido a que el thread puede detenerse de manera no controlada en cualquier momento,
					 * ignorar cualquier error posible. */
				}
			}
		});	
		proceso.setPriority(Thread.MIN_PRIORITY);
		
		cuadroProceso.setProceso(proceso);
		proceso.start();
		
		return true;
	}

	/**
	 * Escribe en un JTextField el valor pasado como parámetro
	 * 
	 * @param texto
	 *            texto que escribirá en el JTextField correspondiente
	 * @param i
	 *            número de JTextField en el que se debe introducir el texto
	 *            pasado como parámetro
	 */
	public void setValor(String texto, int i) {
		if (i < this.cuadrosvalores.length && this.cuadrosvalores[i] != null) {
			texto = texto.replace(" ", "");
			this.cuadrosvalores[i].addItem(texto);
			this.cuadrosvalores[i].setSelectedItem(texto);
		}
	}

	/**
	 * Permite extraer el JDialog asociado
	 * 
	 * @return JDialog de CuadroParam
	 */
	public JDialog getJDialog() {
		return this.dialogo;
	}
	
	/**
	 * Gestiona los eventos realizados sobre los botones.
	 * 
	 * @param e evento.
	 */
	private void gestionEventoBotones(AWTEvent e) {
		if (e.getSource() == this.aceptar) {
			recogerValores(true);
		} else if (e.getSource() == this.verValores) {
			if (comprobarYAsignarValores()) {
				new CuadroValores(this.ventana, new ParametrosParser(this.metodo));
			}
		} else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		} else if (e.getSource() == this.generar) {
			if (this.cga == null) {
				this.cga = new CuadroGenerarAleatorio(Ventana.thisventana,
						this, this.metodo);
			} else {
				this.cga.setVisible(true);
			}
		} else if (e.getSource() == this.cargar) {
			this.estamosCargando = true;
			if (this.almacenValores.cargar(this.metodo)) {
				this.valores = this.almacenValores.get();
				for (int i = 0; i < this.valores.length; i++) {
					ComboBoxEditor cbe = this.cuadrosvalores[i].getEditor();
					cbe.setItem(this.valores[i]);
					this.cuadrosvalores[i].insertItemAt(this.valores[i], 0);
					this.cuadrosvalores[i].setSelectedIndex(0);
				}
				if (recogerValores(false)) {
					this.aceptar.setEnabled(true);
				} else {
					this.aceptar.setEnabled(false);
				}
			} else {
				if (this.almacenValores.getError().length() > 0) {
					new CuadroError(this.dialogo, "3"
							+ Texto.get("ERROR_ARCH", Conf.idioma),
							this.almacenValores.getError());
				}
			}
		} else if (e.getSource() == this.guardar) {
			String valores[] = new String[this.cuadrosvalores.length];
			for (int i = 0; i < this.cuadrosvalores.length; i++) {
				valores[i] = (String) (this.cuadrosvalores[i].getSelectedItem());
			}
			this.almacenValores.guardar(valores, this.metodo);
			this.guardar.setEnabled(true);
		}
	}
	
	/**
	 * Comprueba que al menos uno de los parámetros
	 * del método esté seleccionado.
	 * 
	 * @return true si al menos un parámetro está seleccionado,
	 * false en caso contrario.
	 */
	private boolean comprobarVisibilidadCorrecta() {
		boolean unoActivo = false;
		// Tendremos n campos: n parámetros
		if (!this.metodo.getTipo().equals("void")) {
			for (int i = 0; i < this.numero; i++) {
				if (this.mostrarvalores[i].isSelected()) {
					unoActivo = true;
				}
			}
			if (!unoActivo) {
				new CuadroError(this.dialogo, Texto.get("ERROR_PARAM",
						Conf.idioma),
						Texto.get("ERROR_VISIBIENTR", Conf.idioma));
			}
		}
		// Tenemos n*2 campos: n de entrada, n de salida
		else {
			for (int i = 0; i < this.numero / 2; i++) {
				if (this.mostrarvalores[i].isSelected()) {
					unoActivo = true;
				}
			}
			if (!unoActivo) {
				new CuadroError(this.dialogo, Texto.get("ERROR_PARAM",
						Conf.idioma),
						Texto.get("ERROR_VISIBIENTR", Conf.idioma));
			}
		}
		return unoActivo;
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
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
			if ((e.getSource() != this.cargar || !this.estamosCargando)) {
				recogerValores(true);
			}
			this.estamosCargando = false;
		} else if ((e.getSource() instanceof JButton)
				&& code == KeyEvent.VK_SPACE) {
			gestionEventoBotones(e);
		}

		if (e.getSource().getClass().getName().contains("JCheckBox")) {
			for (int i = 0; i < this.mostrarvalores.length; i++) {
				if (this.mostrarvalores[i].isFocusOwner()) {
					if (code == KeyEvent.VK_DOWN) {
						this.mostrarvalores[i].transferFocus();
					} else if (code == KeyEvent.VK_UP) {
						this.mostrarvalores[i].transferFocusBackward();
					} else if (code == KeyEvent.VK_RIGHT) {
						this.cuadrosvalores[i].requestFocus();
					}
				}
			}
		}

		else if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		} else if (e.getSource() instanceof JCheckBox) {
			comprobarVisibilidadCorrecta();
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
			gestionEventoBotones(e);
		}
		if (e.getSource() instanceof JCheckBox) {
			comprobarVisibilidadCorrecta();
		}
	}
}
