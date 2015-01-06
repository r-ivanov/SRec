package cuadros;

import java.awt.BorderLayout;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import conf.*;
import botones.*; 
import utilidades.*;
import ventanas.*;

/**
 * Implementa el cuadro que permite dibujar una matriz para grafos de dependencia.
 * 
 * @author David Pastor Herranz
 */
public class CuadroDibujarTablaGrafoDependencia extends Thread implements ActionListener,
		KeyListener, MouseListener {
	
	private static final int ALTURA_CUADRO = 125;
	private static final int ANCHURA_CUADRO = 175;
	
	private VentanaGrafoDependencia ventana;
	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	
	private int filasAnteriores;
	private int columnasAnteriores;
	
	private JTextField textFilas;
	private JTextField textColumnas;

	private JPanel panel, panelBoton, panelParam;
	private JDialog dialogo;

	public CuadroDibujarTablaGrafoDependencia(VentanaGrafoDependencia ventana,
			int filasAnteriores, int columnasAnteriores) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;	
		this.filasAnteriores = filasAnteriores;
		this.columnasAnteriores = columnasAnteriores;
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		
		// Panel general de parámetros
		this.panelParam = new JPanel(new GridLayout(2, 2));
		this.panelParam.setBorder(new TitledBorder(Texto.get("GP_DIBUJAR_TABLA_GRAFO_PANEL", Conf.idioma)));
		
		JLabel labelFilas = new JLabel(Texto.get("GP_DIBUJAR_TABLA_GRAFO_FILAS", Conf.idioma));
		JLabel labelColumnas = new JLabel(Texto.get("GP_DIBUJAR_TABLA_GRAFO_COLUMNAS", Conf.idioma));
		this.textFilas = new JTextField();
		this.textFilas.addKeyListener(this);
		this.textColumnas = new JTextField();
		this.textColumnas.addKeyListener(this);
		
		this.panelParam.add(labelFilas);
		this.panelParam.add(textFilas);
		this.panelParam.add(labelColumnas);
		this.panelParam.add(textColumnas);
		
		if (this.filasAnteriores >= 1) {
			this.textFilas.setText(String.valueOf(this.filasAnteriores));
			this.textFilas.setCaretPosition(this.textFilas.getText().length());
		}
		
		if (this.columnasAnteriores >= 1) {
			this.textColumnas.setText(String.valueOf(this.columnasAnteriores));
			this.textColumnas.setCaretPosition(this.textColumnas.getText().length());
		}
		
		// Botones
		this.aceptar = new BotonAceptar();
		this.aceptar.addMouseListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar = new BotonCancelar();
		this.cancelar.addMouseListener(this);
		this.cancelar.addKeyListener(this);
		
		// Panel para los botones
		this.panelBoton = new JPanel();
		this.panelBoton.add(this.aceptar);
		this.panelBoton.add(this.cancelar);
		
		// Panel general
		this.panel = new JPanel(new BorderLayout());

		this.panel.add(this.panelParam, BorderLayout.NORTH);
		this.panel.add(this.panelBoton, BorderLayout.SOUTH);

		this.dialogo.getContentPane().add(this.panel);
		this.dialogo.setTitle(Texto.get("GP_DIBUJAR_TABLA_GRAFO_TITULO", Conf.idioma));

		// Preparamos y mostramos cuadro
		this.dialogo.setSize(new Dimension(ANCHURA_CUADRO, ALTURA_CUADRO));
		int[] centroVentana = this.ubicarCentroVentana(ANCHURA_CUADRO, ALTURA_CUADRO);
		this.dialogo.setLocation(centroVentana[0], centroVentana[1]);
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}
	
	private int[] ubicarCentroVentana(int anchura, int altura) {
		int[] coord = new int[2];
		coord[0] = (this.ventana.getX() + this.ventana.getWidth() / 2) - anchura / 2;
		coord[1] = (this.ventana.getY() + this.ventana.getHeight() / 2) - altura / 2;
		return coord;
	}
	
	/**
	 * Gestiona los eventos realizados sobre los botones.
	 * 
	 * @param e evento.
	 */
	private void gestionEventoBotones(AWTEvent e) {
		if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		} else if (e.getSource() == this.aceptar) {
			accionDibujarTabla();
		}
	}
	
	private void accionDibujarTabla() {
		boolean error = false;
		int filas = 0;
		int columnas = 0;
		
		try {
			if (this.textFilas.getText().length() != 0) {
				filas = Integer.parseInt(this.textFilas.getText());
			}
		} catch (NumberFormatException formatException) {
			error = true;
		}		
		try {
			if (this.textColumnas.getText().length() != 0) {
				columnas = Integer.parseInt(this.textColumnas.getText());
			}
		} catch (NumberFormatException formatException) {
			error = true;
		}
		
		if (filas < 0) {
			error = true;
		}		
		if (columnas < 0) {
			error = true;
		}
		
		if (filas == 0 && columnas >= 1) {
			filas = 1;
		}
		if (columnas == 0 && filas >= 1) {
			columnas = 1;
		}
		
		if (error) {
			new CuadroError(this.ventana, Texto.get("GP_ERROR_TITULO", Conf.idioma),
					Texto.get("GP_ERROR_DIBUJAR_TABLA", Conf.idioma));
		} else {		
			this.ventana.dibujarTabla(filas, columnas);
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
		} else if (code == KeyEvent.VK_UP) {
			if (this.aceptar.hasFocus() || this.cancelar.hasFocus()) {
				this.textColumnas.requestFocus();
			} else if (this.textColumnas.hasFocus()) {
				this.textFilas.requestFocus();
			}
		} else if (code == KeyEvent.VK_DOWN) {
			if (this.textFilas.hasFocus()) {
				this.textColumnas.requestFocus();
			} else if (this.textColumnas.hasFocus()) {
				this.aceptar.requestFocus();
			}
		} else if (code == KeyEvent.VK_RIGHT) {
			if (this.aceptar.hasFocus()) {
				this.cancelar.requestFocus();
			}
		} else if (code == KeyEvent.VK_LEFT) {
			if (this.cancelar.hasFocus()) {
				this.aceptar.requestFocus();
			}
		} else if (code == KeyEvent.VK_ENTER) {
			this.accionDibujarTabla();
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
