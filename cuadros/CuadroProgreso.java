package cuadros;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.border.TitledBorder;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import conf.*;
import utilidades.*;
import ventanas.Ventana;

/**
 * Implementa el cuadro de progreso que informa del estado de un determinado
 * proceso de carga.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroProgreso extends Thread {

	private static final int ANCHO_CUADRO = 450;
	private static final int ALTO_CUADRO = 110;

	private JLabel etiqueta;
	private JPanel panel, panelTexto, panelProgreso;
	private JProgressBar barra;

	private BorderLayout bl;
	private GridLayout gl;

	private JDialog dialogo;

	private String nombre, texto;
	private int valor;

	/**
	 * Genera un nuevo cuadro de progreso.
	 * 
	 * @param Ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 * @param nombre
	 *            Título que aparecerá en el cuadro
	 * @param texto
	 *            texto que se colocará en primera instancia en la etiqueta del
	 *            panel
	 * @param valor
	 *            valor inicial que se asignará a la barra
	 */
	public CuadroProgreso(Ventana ventana, String nombre, String texto,
			int valor) {

		this.dialogo = new JDialog(ventana, true);
		this.nombre = nombre;
		this.texto = texto;
		this.valor = valor;
		this.etiqueta = new JLabel();

		this.barra = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
		this.barra.setSize(300, 35);
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		// Panel de texto
		this.panelTexto = new JPanel();
		this.panelTexto.add(this.etiqueta);
		this.panelTexto.setBorder(new TitledBorder(Texto.get("CP_PROGPROC",
				Conf.idioma)));
		this.panelTexto.setSize(320, 80);

		// Panel de progreso
		this.gl = new GridLayout(0, 1);
		this.panelProgreso = new JPanel();
		this.panelProgreso.setLayout(this.gl);
		this.panelProgreso.add(this.barra);
		this.panelProgreso.setBorder(new TitledBorder(""));

		// Panel general
		this.bl = new BorderLayout();
		this.panel = new JPanel();
		this.panel.setLayout(this.bl);

		this.panel.add(this.panelProgreso, BorderLayout.SOUTH);
		this.panel.add(this.panelTexto, BorderLayout.NORTH);

		// Inicializacion de valores
		setValores(this.texto, this.valor);

		this.dialogo
				.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(this.panel);
		this.dialogo.setTitle(this.nombre);
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);
		this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Asigna valores a la etiqueta y a la barra
	 * 
	 * @param texto
	 *            texto que aparecerá en la etiqueta
	 * @param valor
	 *            valor que quedará representado en la barra
	 */
	public void setValores(String texto, int valor) {
		this.etiqueta.setText(texto);
		if (valor >= this.barra.getMinimum()
				&& valor <= this.barra.getMaximum()) {
			this.barra.setValue(valor);
		}
	}

	/**
	 * Asigna valor a la etiqueta.
	 * 
	 * @param texto
	 *            texto que aparecerá en la etiqueta
	 */
	public void setValores(String texto) {
		this.etiqueta.setText(texto);
	}

	/**
	 * Asigna valor a la barra
	 * 
	 * @param valor
	 *            valor que quedará representado en la barra
	 */
	public void setValores(int valor) {
		if (valor >= this.barra.getMinimum()
				&& valor <= this.barra.getMaximum()) {
			this.barra.setValue(valor);
		}
	}

	/**
	 * Hace invisible el cuadro, una vez ha finalizado el procesamiento
	 */
	public void cerrar() {
		this.dialogo.setVisible(false);
	}
}
