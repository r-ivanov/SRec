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

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.util.ArrayList;

import botones.*;
import conf.*;
import datos.*;
import utilidades.*;
import ventanas.*;

/**
 * Representa la clase del cuadro que ofrece la selección del método que se
 * desea visualizar
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroMetodosProcesadosSelecMetodo extends Thread implements
		ActionListener, KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 450;
	private static final int ALTO_CUADRO = 550;

	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	private JPanel panel, panelBotones;
	private int numero;

	private JCheckBox checkTodos;

	private ButtonGroup grupoBotones;
	private JRadioButton[] botonesRadio;
	private JCheckBox[] botonesCheck;

	private BorderLayout bl;

	private Ventana ventana;

	private JDialog dialogo;

	private ClaseAlgoritmo clase;
	private ArrayList<MetodoAlgoritmo> metodos = new ArrayList<MetodoAlgoritmo>(
			0);

	/**
	 * Genera un nuevo cuadro de diálogo que permite al usuario elegir qué
	 * método de una determinada clase se quiere ejecutar
	 * 
	 * @param ventana
	 *            Ventana a la que el cuadro quedará asociado.
	 * @param clase
	 *            clase a la que pertenece el método que se quiere ejecutar.
	 */
	public CuadroMetodosProcesadosSelecMetodo(Ventana ventana,
			ClaseAlgoritmo clase) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.clase = clase;
		this.numero = this.clase.getNumMetodosProcesados();
		this.metodos = this.clase.getMetodosProcesados();
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		if (this.numero > 0) {

			this.checkTodos = new JCheckBox(Texto.get("CMP_TODOSMETODOS",
					Conf.idioma));
			this.checkTodos.addMouseListener(this);
			this.checkTodos.addKeyListener(this);
			this.checkTodos.setToolTipText(Texto.get("CMP_SELECTODMET",
					Conf.idioma));

			// Estudiamos si debemos marcar o no checkTodos
			boolean valorCheckTodos = true;
			for (int i = 0; i < this.numero; i++) {
				if (!this.metodos.get(i).getMarcadoVisualizar()) {
					valorCheckTodos = false;
				}
			}

			this.checkTodos.setSelected(valorCheckTodos);

			// Panel general de métodos
			JPanel panelGeneral = new JPanel();
			panelGeneral.setLayout(new BorderLayout());

			this.grupoBotones = new ButtonGroup();
			this.botonesRadio = new JRadioButton[this.numero];
			this.botonesCheck = new JCheckBox[this.numero];

			int llamadasRepresentar = 0;

			for (MetodoAlgoritmo m : this.metodos) {
				llamadasRepresentar = llamadasRepresentar
						+ m.getMetodosLlamados().length;
			}

			// Panel de métodos (se divide en dos, de dos columnas de "cosas" en
			// cada uno)
			JPanel panelMetodos = new JPanel();
			panelMetodos.setLayout(new GridLayout(this.numero
					+ llamadasRepresentar, 1));

			String toolTipTextRadio = Texto.get("CMP_SELECTMET", Conf.idioma);
			String toolTipTextCheck = Texto.get("CMP_SELECTMETV", Conf.idioma);

			for (int i = 0; i < this.numero; i++) {
				String cadenaEtiqueta = this.metodos.get(i).getRepresentacion()
						+ (this.metodos.get(i).getTecnica() == MetodoAlgoritmo.TECNICA_DYV ? "  <D>"
								: "");

				JPanel panel = new JPanel(); // Panel que engloba toda la fila
				panel.setLayout(new BorderLayout());

				JPanel panelIzquierda = new JPanel(); // Panel que tiene check,
														// radio y el texto
				panelIzquierda.setLayout(new BorderLayout());
				this.botonesRadio[i] = new JRadioButton();
				this.botonesRadio[i].setFocusable(false);
				this.botonesCheck[i] = new JCheckBox(cadenaEtiqueta);
				this.botonesCheck[i].setFocusable(false);
				this.grupoBotones.add(this.botonesRadio[i]);
				this.botonesRadio[i].setSelected(this.metodos.get(i)
						.getMarcadoPrincipal());
				this.botonesCheck[i].setSelected(this.metodos.get(i)
						.getMarcadoVisualizar());

				this.botonesRadio[i].setToolTipText(toolTipTextRadio + " "
						+ cadenaEtiqueta);
				this.botonesCheck[i].setToolTipText(toolTipTextCheck + " "
						+ cadenaEtiqueta);

				this.botonesRadio[i].addMouseListener(this);
				this.botonesCheck[i].addMouseListener(this);

				JPanel panelAux = new JPanel();
				panelAux.add(this.botonesRadio[i]);
				panelAux.add(this.botonesCheck[i]);
				panelIzquierda.add(panelAux, BorderLayout.WEST);

				panelIzquierda.setPreferredSize(new Dimension(400, 28));

				panel.add(panelIzquierda, BorderLayout.WEST);

				panelMetodos.add(panel);

				int[] llamadas = this.metodos.get(i).getMetodosLlamados();
				for (int j = 0; j < llamadas.length; j++) {
					panel = new JPanel();
					panel.setLayout(new BorderLayout());

					JPanel izqdaLlamada = new JPanel();
					JLabel etiquetaSignaturaLlamado = new JLabel(
							"                     - "
									+ this.clase.getMetodoID(llamadas[j])
											.getRepresentacion());

					izqdaLlamada.add(etiquetaSignaturaLlamado);

					panel.add(izqdaLlamada, BorderLayout.WEST);

					panelMetodos.add(panel);
				}

			}

			// Si no hay ningún método seleccionado, seleccionamos el primero
			boolean ningunoMarcado = true;
			for (int i = 0; i < this.numero; i++) {
				if (this.botonesRadio[i].isSelected()) {
					ningunoMarcado = false;
				}
			}

			if (ningunoMarcado) {
				this.botonesRadio[0].setSelected(true);
			}

			JPanel panelTextoSignaturas = new JPanel();
			panelTextoSignaturas.add(new JLabel(Texto.get("CSM_SIGNAT",
					Conf.idioma)));

			JPanel panelCheckTodos = new JPanel();
			JPanel panelEspacio = new JPanel();
			panelEspacio.setPreferredSize(new Dimension(31, 10));

			panelCheckTodos.setLayout(new BorderLayout());
			panelCheckTodos.add(panelEspacio, BorderLayout.WEST);
			panelCheckTodos.add(this.checkTodos, BorderLayout.CENTER);

			JPanel panelCabecera = new JPanel();
			panelCabecera.setLayout(new BorderLayout());
			panelCabecera.add(panelTextoSignaturas, BorderLayout.WEST);
			panelCabecera.add(panelCheckTodos, BorderLayout.SOUTH);

			JPanel panelContenedorMetodos = new JPanel();
			panelContenedorMetodos.setLayout(new BorderLayout());
			panelContenedorMetodos.add(panelMetodos, BorderLayout.NORTH);

			JScrollPane jsp = new JScrollPane(panelContenedorMetodos);
			jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
			jsp.setPreferredSize(new Dimension(ANCHO_CUADRO - 10,
					ALTO_CUADRO - 136));
			jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

			panelGeneral.setBorder(new TitledBorder(this.numero + " "
					+ Texto.get("CMP_METPROC", Conf.idioma) + " "
					+ this.clase.getNombre()));
			panelGeneral.add(panelCabecera, BorderLayout.NORTH);
			panelGeneral.add(jsp, BorderLayout.CENTER);

			// Botón Aceptar
			this.aceptar = new BotonAceptar();
			this.aceptar.addKeyListener(this);
			this.aceptar.addMouseListener(this);

			// Botón Cancelar
			this.cancelar = new BotonCancelar();
			this.cancelar.addKeyListener(this);
			this.cancelar.addMouseListener(this);

			// Panel para los botones
			this.panelBotones = new JPanel();
			this.panelBotones.add(this.aceptar);
			this.panelBotones.add(this.cancelar);

			// Panel general
			this.bl = new BorderLayout();
			this.panel = new JPanel();
			this.panel.setLayout(this.bl);

			this.panel.add(panelGeneral, BorderLayout.NORTH);
			this.panel.add(this.panelBotones, BorderLayout.SOUTH);

			// Preparamos y mostramos cuadro
			this.dialogo.getContentPane().add(this.panel);
			this.dialogo.setTitle(Texto.get("CMP_ESCOGER", Conf.idioma));
			this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
			this.dialogo.setLocation(coord[0], coord[1]);
			this.dialogo.setResizable(false);
			this.dialogo.setVisible(true);
		} else {
			new CuadroError(this.ventana,
					Texto.get("ERROR_CLASE", Conf.idioma), Texto.get(
							"ERROR_NOMETVIS", Conf.idioma));
		}

	}

	/**
	 * Recoge la selección del método señalado por el usuario y establece el
	 * método seleccionado para posteriormente permitir su ejecución desde la
	 * ventana.
	 */
	private void recogerMetodoSeleccionado() {
		boolean errorProducido = false;

		// Actualizar cuál es el método principal
		this.clase.borrarMarcadoPrincipal();
		for (int i = 0; i < this.numero; i++) {
			this.metodos.get(i).setMarcadoPrincipal(
					this.botonesRadio[i].isSelected());
			this.metodos.get(i).setMarcadoVisualizar(
					this.botonesCheck[i].isSelected());
			this.clase.addMetodo(this.metodos.get(i));
		}

		if (!errorProducido) {
			// Actualizamos la clase
			MetodoAlgoritmo ma = this.clase.getMetodoPrincipal();
			if (Conf.fichero_log) {
				String mensaje = "Método seleccionado: "
						+ ma.getRepresentacion();
				Logger.log_write(mensaje);
			}
			this.ventana.setClase(this.clase);
			this.dialogo.setVisible(false);

			// Limpiamos los paneles de visualizacion
			this.ventana.abrirPanelCodigo(true, true);
			// Escribir signatura del método seleccionado
			this.ventana.setValoresPanelControl(ma.getRepresentacion());
			// Habilitamos la opcion para asignar parametros y ejecutar
			this.ventana.setClaseHabilitadaAnimacion(true);
			this.ventana.setClasePendienteGuardar(false);
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
			recogerMetodoSeleccionado();
		} else if (code == KeyEvent.VK_DOWN) {
			for (int i = 0; i < this.botonesRadio.length; i++) {
				if (this.botonesRadio[i].isFocusOwner()) {
					if (i != this.botonesRadio.length - 1) {
						this.botonesRadio[i].transferFocus();
					}
				}
			}
		} else if (code == KeyEvent.VK_UP) {
			for (int i = 0; i < this.botonesRadio.length; i++) {
				if (this.botonesRadio[i].isFocusOwner()) {
					if (i != 0) {
						this.botonesRadio[i].transferFocusBackward();
					}
				}
			}
		} else if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		} else {
			if (!(e.getSource().getClass().getName().contains("Boton"))) {
				for (int i = 0; i < this.botonesRadio.length; i++) {
					if (this.botonesRadio[i].isFocusOwner()) {
						this.botonesRadio[i].setSelected(true);
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

		if (e.getSource() == this.aceptar) {
			recogerMetodoSeleccionado();
		} else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		} else if (e.getSource() == this.checkTodos) {
			for (int i = 0; i < this.botonesCheck.length; i++) {
				this.botonesCheck[i].setSelected(this.checkTodos.isSelected()
						|| this.botonesRadio[i].isSelected());
			}
		} else if (e.getSource().getClass().getName().contains("JRadioButton")) {
			for (int i = 0; i < this.numero; i++) {
				if (this.botonesRadio[i].isSelected()) {
					this.botonesCheck[i].setSelected(true);
				}
			}
		} else if (e.getSource().getClass().getName().contains("JCheckBox")) {
			// Hacemos que si se intenta hacer que el método principal no sea
			// visible, se vuelva a marcar como visible
			for (int i = 0; i < this.numero; i++) {
				if (this.botonesRadio[i].isSelected()) {
					this.botonesCheck[i].setSelected(true);
				}
			}

			// Ahora hacemos que los botones de opciones queden inhabilitados
			// para métodos no visibles
			for (int i = 0; i < this.numero; i++) {
				if (this.botonesCheck[i] == ((JCheckBox) e.getSource())) {
					if (!this.botonesCheck[i].isSelected()) {
						this.checkTodos.setSelected(false);
					}
				}
			}

		}
	}
}
