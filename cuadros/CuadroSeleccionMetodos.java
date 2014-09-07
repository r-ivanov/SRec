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
public class CuadroSeleccionMetodos extends Thread implements ActionListener,
		KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 720;
	private static final int ALTO_CUADRO = 420;

	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	private JPanel panel, panelBoton, panelBotones;

	// Numero total de métodos visualizables
	private int numero;

	private JCheckBox botonesDYV[];

	private int[] posicBotones;

	// Cuadro de texto para que el usuario escriba el nº de parámetro en el que
	// está la estructura
	private JTextField[] estructura;
	// Cuadro de texto para que el usuario escriba los nº de parámetro en los
	// que están los índices
	private JTextField[] indices;

	private ClaseAlgoritmo clase;

	// Listado de métodos seleccionados para ser procesados
	private ArrayList<MetodoAlgoritmo> metodos;

	private BorderLayout bl;
	private GridLayout gl;

	private Ventana ventana;
	private Preprocesador preprocesador;
	private JDialog dialogo;

	/**
	 * Contructor: genera un nuevo cuadro de diálogo que permite al usuario
	 * elegir qué método de una determinada clase se quiere ejecutar
	 * 
	 * @param clase
	 *            clase a la que pertenece el método que se quiere ejecutar
	 * @param ventana
	 *            ventana a la que se asociará el cuadro de diálogo
	 * @param gestorEjecucion
	 *            gestor que realizará los pasos necesarios para ejecutar el
	 *            método seleccionado
	 * @param codigounico
	 *            código único que identifica a la clase y la da nombre
	 */
	public CuadroSeleccionMetodos(ClaseAlgoritmo claseAlgoritmo,
			Ventana ventana, Preprocesador preprocesador) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.clase = claseAlgoritmo;
		this.metodos = claseAlgoritmo.getMetodos();
		this.numero = this.metodos.size();
		this.preprocesador = preprocesador;
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		if (this.numero > 0) {
			String nombreClase = this.clase.getNombre();

			int llamadasRepresentar = 0;

			for (MetodoAlgoritmo m : this.metodos) {
				llamadasRepresentar = llamadasRepresentar
						+ m.getMetodosLlamados().length;
			}

			// Panel de Botones
			this.gl = new GridLayout(this.numero + llamadasRepresentar, 1);
			this.panelBotones = new JPanel();
			this.panelBotones.setLayout(this.gl);

			this.posicBotones = new int[this.numero];

			this.estructura = new JTextField[this.numero];
			this.indices = new JTextField[this.numero];

			this.botonesDYV = new JCheckBox[this.numero];

			JPanel panelFilaSup = new JPanel();
			panelFilaSup.setLayout(new BorderLayout());

			JLabel etiqSignatura = new JLabel(Texto.get("CSM_SIGNAT",
					Conf.idioma));

			JPanel panelDerechaSuperior = new JPanel();

			int alturaFilaSuperior = 20;

			JPanel panelEtiqEstructura = new JPanel();
			panelEtiqEstructura.setPreferredSize(new Dimension(70,
					alturaFilaSuperior));
			panelEtiqEstructura.add(new JLabel(Texto.get("CSM_FILASUPESTR",
					Conf.idioma)));

			JPanel panelEtiqIndices = new JPanel();
			panelEtiqIndices.setPreferredSize(new Dimension(70,
					alturaFilaSuperior));
			panelEtiqIndices.add(new JLabel(Texto.get("CSM_FILASUPINDIC",
					Conf.idioma)));

			JPanel panelEtiqVacia = new JPanel();
			panelEtiqVacia
					.setPreferredSize(new Dimension(3, alturaFilaSuperior));
			panelEtiqVacia.add(new JLabel(""));

			panelDerechaSuperior.add(panelEtiqEstructura, BorderLayout.WEST);
			panelDerechaSuperior.add(panelEtiqIndices, BorderLayout.CENTER);
			panelDerechaSuperior.add(panelEtiqVacia, BorderLayout.EAST);

			panelFilaSup.add(etiqSignatura, BorderLayout.WEST);
			panelFilaSup.add(panelDerechaSuperior, BorderLayout.EAST);

			String toolTipEstr = Texto.get("CSM_INDICAESTR", Conf.idioma);
			String toolTipInd = Texto.get("CSM_INDICAPARAM", Conf.idioma);

			String toolTipDYVsi = Texto.get("CSM_MARCPROC", Conf.idioma);
			String toolTipDYVno = Texto.get("CSM_MARCPROCNO", Conf.idioma);

			int y = 0;
			for (int x = 0; x < this.numero; x++) {
				JPanel panelFila = new JPanel();
				panelFila.setLayout(new BorderLayout());

				// Parte derecha
				JPanel panelFilaParteDerecha = new JPanel();
				panelFilaParteDerecha.setLayout(new BorderLayout());

				this.estructura[x] = new JTextField(8);
				this.indices[x] = new JTextField(8);

				this.estructura[x].addKeyListener(this);
				this.indices[x].addKeyListener(this);

				this.estructura[x].setEnabled(false);
				this.indices[x].setEnabled(false);

				this.estructura[x]
						.setHorizontalAlignment(SwingConstants.CENTER);
				this.indices[x].setHorizontalAlignment(SwingConstants.CENTER);

				this.estructura[x].setToolTipText(toolTipEstr);
				this.indices[x].setToolTipText(toolTipInd);

				JPanel panelContenedorEstructura = new JPanel();
				panelContenedorEstructura.add(this.estructura[x]);
				panelContenedorEstructura
						.setPreferredSize(new Dimension(70, 24));
				JPanel panelContenedorIndices = new JPanel();
				panelContenedorIndices.add(this.indices[x]);
				panelContenedorIndices.setPreferredSize(new Dimension(70, 24));

				panelFilaParteDerecha.add(panelContenedorEstructura,
						BorderLayout.WEST);
				panelFilaParteDerecha.add(panelContenedorIndices,
						BorderLayout.CENTER);

				// Parte izquierda
				JPanel panelFilaParteIzquierda = new JPanel();

				String representacion = this.metodos.get(x).getRepresentacion();
				int[] dimParametros = this.metodos.get(x).getDimParametros();
				boolean hayArrayOMatriz = false;
				for (int i = 0; i < dimParametros.length; i++) {
					if (dimParametros[i] > 0) {
						hayArrayOMatriz = true;
					}
				}

				this.botonesDYV[x] = new JCheckBox(representacion);
				this.botonesDYV[x].setName(representacion);

				this.posicBotones[x] = y;

				if (hayArrayOMatriz) {
					this.botonesDYV[x].setToolTipText(toolTipDYVsi);
				} else {
					this.botonesDYV[x].setToolTipText(toolTipDYVno);
					this.botonesDYV[x].setEnabled(false);
				}

				// panelBotones.add(botones[x],x);
				panelFilaParteIzquierda.add(this.botonesDYV[x]);
				this.botonesDYV[x].addKeyListener(this);
				this.botonesDYV[x].addActionListener(this);

				panelFila.add(panelFilaParteIzquierda, BorderLayout.WEST);
				panelFila.add(panelFilaParteDerecha, BorderLayout.EAST);

				panelFila.setPreferredSize(new Dimension(550, 25));

				this.panelBotones.add(panelFila, y);
				y++;

				int[] llamadas = this.metodos.get(x).getMetodosLlamados();
				for (int i = 0; i < llamadas.length; i++) {
					panelFila = new JPanel();
					panelFila.setLayout(new BorderLayout());

					JPanel izqdaLlamada = new JPanel();
					JLabel etiquetaSignaturaLlamado = new JLabel(
							"            - "
									+ this.clase.getMetodoID(llamadas[i])
											.getRepresentacion());
					if (!hayArrayOMatriz) {
						etiquetaSignaturaLlamado.setEnabled(false);
					}
					izqdaLlamada.add(etiquetaSignaturaLlamado);

					panelFila.setPreferredSize(new Dimension(550, 25));

					panelFila.add(izqdaLlamada, BorderLayout.WEST);

					this.panelBotones.add(panelFila, y);

					y++;
				}

			}

			// Botón Aceptar
			this.aceptar = new BotonAceptar();
			this.aceptar.addKeyListener(this);
			this.aceptar.addMouseListener(this);

			// Botón Cancelar
			this.cancelar = new BotonCancelar();
			this.cancelar.addKeyListener(this);
			this.cancelar.addMouseListener(this);

			// Panel para el botón
			this.panelBoton = new JPanel();
			this.panelBoton.add(this.aceptar);
			this.panelBoton.add(this.cancelar);

			// Panel general
			this.bl = new BorderLayout();
			this.panel = new JPanel();
			this.panel.setLayout(this.bl);

			JPanel panelContenedorBotones = new JPanel();
			panelContenedorBotones.setLayout(new BorderLayout());
			panelContenedorBotones.add(this.panelBotones, BorderLayout.NORTH);

			JScrollPane jsp = new JScrollPane(panelContenedorBotones);

			// jsp.add(panelBotones);
			jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
			jsp.setPreferredSize(new Dimension(ANCHO_CUADRO - 10,
					ALTO_CUADRO - 120));

			JPanel panelSup = new JPanel();
			panelSup.setLayout(new BorderLayout());

			panelSup.add(panelFilaSup, BorderLayout.NORTH);
			panelSup.add(jsp, BorderLayout.CENTER);
			jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			panelSup.setBorder(new TitledBorder(this.numero + " "
					+ Texto.get("CSM_METDISP", Conf.idioma) + " " + nombreClase));

			this.panel.add(panelSup, BorderLayout.NORTH);
			this.panel.add(this.panelBoton, BorderLayout.SOUTH);

			// Preparamos y mostramos cuadro
			this.dialogo.getContentPane().add(this.panel);
			this.dialogo.setTitle(Texto.get("CSM_ESCMET", Conf.idioma));

			// dialogo.setSize(anchoCuadro,altoCuadro+(alto*numero));
			this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
			this.dialogo.setLocation(coord[0], coord[1]);
			this.dialogo.setResizable(false);
			this.dialogo.setVisible(true);

		} else {
			new CuadroError(this.ventana,
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

		for (int i = 0; i < this.clase.getNumMetodos(); i++) {
			if ((this.botonesDYV[i].isSelected() || !(this.botonesDYV[i]
					.isSelected()))) {
				this.clase.getMetodo(i).setMarcadoProcesar(true);

				if (this.botonesDYV[i].isSelected()) {
					this.clase.getMetodo(i).setTecnica(
							MetodoAlgoritmo.TECNICA_DYV);
					Ventana.thisventana.habilitarOpcionesDYV(true);
					this.clase.getMetodo(i).setIndices(
							Integer.parseInt(this.estructura[i].getText()
									.replace(" ", "")));

					int parametrosIndice[] = ServiciosString.extraerValoresInt(
							this.indices[i].getText(), ',');

					if (parametrosIndice == null) {
						this.clase.getMetodo(i).setIndices(
								Integer.parseInt(this.estructura[i].getText()
										.replace(" ", "")) + 1);
					} else if (parametrosIndice.length == 2) {
						this.clase.getMetodo(i).setIndices(
								Integer.parseInt(this.estructura[i].getText()
										.replace(" ", "")) + 1,
								parametrosIndice[0] + 1,
								parametrosIndice[1] + 1);
					} else {
						this.clase.getMetodo(i).setIndices(
								Integer.parseInt(this.estructura[i].getText()
										.replace(" ", "")) + 1,
								parametrosIndice[0] + 1,
								parametrosIndice[1] + 1,
								parametrosIndice[2] + 1,
								parametrosIndice[3] + 1);
					}
				} else {
					this.clase.getMetodo(i).setTecnica(
							MetodoAlgoritmo.TECNICA_REC);
				}
			} else {
				huboError = true;
			}
		}

		if (!huboError) {
			this.dialogo.setVisible(false);
			this.preprocesador.fase2(this.clase);
		} else {
			new CuadroError(this.dialogo, Texto.get("ERROR_VAL", Conf.idioma),
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
		this.botonesDYV[numMetodo].setSelected(valor);
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
			recogerMetodosSeleccionados();
		} else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		} else if (e.getSource().getClass().getName().contains("JCheckBox")) {
			for (int i = 0; i < this.numero; i++) {
				if (this.botonesDYV[i] == e.getSource()) {
					if (this.botonesDYV[i].isSelected()) {
						new CuadroIdentificarParametros(Ventana.thisventana,
								this, this.metodos.get(i), i,
								this.estructura[i].getText(),
								this.indices[i].getText());
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
			this.dialogo.setVisible(false);
		} else {
			if (e.getSource() instanceof JTextField) {
				for (int i = 0; i < this.estructura.length; i++) {
					if (e.getSource() == this.estructura[i]
							|| e.getSource() == this.indices[i]) {
						this.botonesDYV[i].setSelected(true);
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
			recogerMetodosSeleccionados();
		} else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		}
	}

	public void setParametrosMetodo(int i, String paramE, String paramI) {
		this.estructura[i].setText(paramE);
		this.indices[i].setText(paramI);
	}

}