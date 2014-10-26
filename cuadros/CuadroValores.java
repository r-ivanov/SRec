package cuadros;

import java.awt.BorderLayout;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

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
	
	private static final int VIEWPORT_FILAS_MAXIMAS = 10;
	
	private Ventana ventana;
	private BotonTexto cerrar;

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
	
	private static String[][] obtenerMatrizParametrosConNumeroDeFila(String[][] matrizParametros) {
		String[][] parametrosConNumeroDeFila = new String[matrizParametros.length][];
		for (int i = 0; i < matrizParametros.length; i++) {
			String[] filaParametros = new String[matrizParametros[i].length + 1];
			filaParametros[0] = "<html><b>" + (i + 1) + "</b></html>";
			for (int j = 0; j < matrizParametros[i].length; j++) {
				filaParametros[j+1] = matrizParametros[i][j];
			}
			parametrosConNumeroDeFila[i] = filaParametros;
		}
		return parametrosConNumeroDeFila;
	}
	
	private static String[] obtenerListaParametrosConCabeceraDeEjecucion(String[] nombreParametros) {
		String[] nombreParametrosConNumero = new String[nombreParametros.length + 1];
		nombreParametrosConNumero[0] = "<html><b>#</b></html>";
		for (int i = 0; i < nombreParametros.length; i++) {
			nombreParametrosConNumero[i+1] = nombreParametros[i];
		}
		return nombreParametrosConNumero;
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		// Panel general de parámetros			
		BorderLayout bl = new BorderLayout();
		this.panelParam = new JPanel();
		this.panelParam.setLayout(bl);
		this.panelParam.setBorder(new TitledBorder(Texto.get("CVALORES_VALORES", Conf.idioma) +
				" " + this.parametrosParser.obtenerNombreMetodo() + " :"));
		
		String[][] matrizParametros = obtenerMatrizParametrosConNumeroDeFila(this.parametrosParser.obtenerMatrizParametros());		
		String[] nombreParametros = obtenerListaParametrosConCabeceraDeEjecucion(this.parametrosParser.obtenerNombresParametros());		
		JTable tabla = new JTable(matrizParametros, nombreParametros);
		tabla.setEnabled(false);
		
		TableColumnSizer.setColumnsWidthToFit(tabla, true, false);
		Dimension tamanioTabla = TableColumnSizer
				.getPreferredScrollableViewportSize(tabla, Math.min(matrizParametros.length, VIEWPORT_FILAS_MAXIMAS));
		
		JScrollPane scrollPane = new JScrollPane(tabla);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		tabla.setPreferredScrollableViewportSize(tamanioTabla);
		this.panelParam.add(scrollPane);

		// Botón Cerrar
		this.cerrar = new BotonTexto(Texto.get("BOTONCERRAR", Conf.idioma));
		this.cerrar.addActionListener(this);
		this.cerrar.addKeyListener(this);
		this.cerrar.addMouseListener(this);

		// Panel para el botón
		this.panelBoton = new JPanel();
		this.panelBoton.add(this.cerrar);

		// Panel general
		bl = new BorderLayout();
		this.panel = new JPanel();
		this.panel.setLayout(bl);		

		this.panel.add(this.panelParam, BorderLayout.NORTH);
		this.panel.add(this.panelBoton, BorderLayout.SOUTH);

		this.dialogo.getContentPane().add(this.panel);
		this.dialogo.setTitle(Texto.get("CVALORES_VALORES", Conf.idioma) +
				" " + this.parametrosParser.obtenerNombreMetodo());

		// Preparamos y mostramos cuadro
		Dimension tamanioPanel = new Dimension(tamanioTabla.width + 10, tamanioTabla.height + 110);
		int coord[] = Conf.ubicarCentro(tamanioPanel.width, tamanioPanel.height);
		this.dialogo.setLocation(coord[0], coord[1]);
		this.dialogo.setSize(tamanioPanel);
		this.dialogo.setResizable(false);
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
		if (e.getSource() == this.cerrar) {
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
