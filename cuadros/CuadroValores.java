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
import java.util.List;

import javax.swing.border.TitledBorder;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

import conf.*;
import botones.*;
import datos.*;
import utilidades.*;
import ventanas.*;

/**
 * Implementa el cuadro que muestra los distintos parámetros de ejecuciones.
 * 
 * @author David Pastor Herranz
 */
public class CuadroValores extends Thread implements ActionListener,
		KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 400;
	private static final int ALTO_CUADRO = 300;
	
	private Ventana ventana;
	private BotonAceptar aceptar;

	private JPanel panel, panelBoton, panelParam;
	private JDialog dialogo;
	
	private ParametrosParser parametrosParser;

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
	public CuadroValores(Ventana ventana, ParametrosParser p) {

		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;	
		this.parametrosParser = p;

		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		// Panel general de parámetros			
		this.panelParam = new JPanel();		
		JTable table = new JTable(this.parametrosParser.obtenerMatrizParametros(),
				this.parametrosParser.obtenerNombresParametros());
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		this.panelParam.add(scrollPane);
		

		// Botón Aceptar
		this.aceptar = new BotonAceptar();
		this.aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);
		this.aceptar.addMouseListener(this);

		// Panel para el botón
		this.panelBoton = new JPanel();
		this.panelBoton.add(this.aceptar);

		// Panel general
		BorderLayout bl = new BorderLayout();
		this.panel = new JPanel();
		this.panel.setLayout(bl);

		this.panel.add(this.panelParam, BorderLayout.NORTH);
		this.panel.add(this.panelBoton, BorderLayout.SOUTH);

		this.dialogo.getContentPane().add(this.panel);
		this.dialogo.setTitle(Texto.get("CVALORES_VALORES", Conf.idioma) +
				" " + this.parametrosParser.obtenerNombreMetodo());

		// Preparamos y mostramos cuadro
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);
		this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setResizable(true);
		this.dialogo.setVisible(true);
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
			this.dialogo.setVisible(false);
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
		if ((e.getSource() instanceof JButton) && code == KeyEvent.VK_SPACE) {
			gestionEventoBotones(e);
		} else if (code == KeyEvent.VK_ESCAPE) {
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
	}
}
