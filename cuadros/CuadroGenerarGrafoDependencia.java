package cuadros;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import utilidades.Texto;
import ventanas.Ventana;
import ventanas.VentanaGrafoDependencia;
import botones.BotonAceptar;
import botones.BotonCancelar;
import conf.Conf;
import datos.DatosMetodoBasicos;
import datos.DatosTrazaBasicos;

/**
 * Permite seleccionar un m�todo de entre los distintos activos en la ejecuci�n
 * para generar el correspondiente grafo de dependencia para el mismo.
 * 
 * @author David Pastor Herranz
 */
public class CuadroGenerarGrafoDependencia extends Thread implements
ActionListener, KeyListener {

	private static final int ANCHO_CUADRO = 275;

	private DatosTrazaBasicos dtb;

	private JRadioButton botonesMetodos[];

	private Ventana ventana;
	private JDialog dialogo;

	private BotonAceptar aceptar = new BotonAceptar();
	private BotonCancelar cancelar = new BotonCancelar();

	private int numeroFilas = 0;

	/**
	 * Genera un nuevo cuadro que permite seleccionar un m�todo de entre los
	 * distintos activos en la ejecuci�n para generar el correspondiente grafo
	 * de dependencia para el mismo.
	 * 
	 * @param ventana
	 *            Ventana a la que quedar� asociado el cuadro.
	 * @param dtb
	 *            Datos de traza b�sicos de la traza en ejecuci�n.
	 */
	public CuadroGenerarGrafoDependencia(Ventana ventana, DatosTrazaBasicos dtb) {
		if (dtb != null) {
			this.dialogo = new JDialog(ventana, true);
			this.ventana = ventana;
			this.dtb = dtb;
			this.start();
		}
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		this.numeroFilas = this.dtb.getNumMetodos();

		this.botonesMetodos = new JRadioButton[this.dtb.getNumMetodos()];

		JPanel panelRadio = new JPanel();
		panelRadio.setLayout(new GridLayout(this.numeroFilas, 1));
		panelRadio.setBorder(new TitledBorder(Texto.get("GP_METODOS_DISPONIBLES", Conf.idioma)));

		ButtonGroup radioButtonGroup = new ButtonGroup();
		for (int i = 0; i < this.dtb.getNumMetodos(); i++) {
			DatosMetodoBasicos dmb = this.dtb.getMetodo(i);
			this.botonesMetodos[i] = new JRadioButton(dmb.getInterfaz());
			this.botonesMetodos[i].addActionListener(this);
			this.botonesMetodos[i].addKeyListener(this);
			if (i == this.dtb.getNumMetodos()-1) {
				this.botonesMetodos[i].setSelected(true);
			}
			radioButtonGroup.add(this.botonesMetodos[i]);
			panelRadio.add(this.botonesMetodos[i]);
		}

		// Panel para el bot�n
		JPanel panelBotones = new JPanel();
		panelBotones.add(this.aceptar);
		panelBotones.add(this.cancelar);

		this.aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar.addActionListener(this);
		this.cancelar.addKeyListener(this);

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelRadio, BorderLayout.NORTH);
		panel.add(panelBotones, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("GP_GENERAR_GRAFO", Conf.idioma));

		if (this.ventana.msWindows) {
			this.dialogo.setSize(ANCHO_CUADRO, this.numeroFilas * 23 + 90);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO,
					this.numeroFilas * 23 + 90);
			this.dialogo.setLocation(coord[0], coord[1]);
		} else {
			this.dialogo.setSize(ANCHO_CUADRO, this.numeroFilas * 23 + 90);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO,
					this.numeroFilas * 23 + 90);
			this.dialogo.setLocation(coord[0], coord[1]);
		}
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
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

			String nombreMetodo = null;
			int posicionMetodo = 0;
			for (int i = 0; i < this.botonesMetodos.length; i++) {
				if (this.botonesMetodos[i].isSelected()) {
					posicionMetodo = i;
					break;
				}
			}

			this.dialogo.setVisible(false);
			new VentanaGrafoDependencia(this.ventana, this.dtb.getMetodo(posicionMetodo));

		} else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
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
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// Procesar grafo de dependencia
			this.dialogo.setVisible(false);
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

}
