package cuadros;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

import ventanas.*;

/**
 * Representa el cuadro donde se mostrará el grafo de dependencia.
 * 
 * @author David Pastor Herranz
 */
public class CuadroGrafoDependencia extends Thread implements
		ActionListener, KeyListener {

	private Ventana ventana;
	private JDialog dialogo;

	/**
	 * Genera un nuevo cuadro que permite mostrar un grafo de dependencia.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 */
	public CuadroGrafoDependencia(Ventana ventana) {
			this.dialogo = new JDialog(ventana, true);
			this.ventana = ventana;
			this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

//		panel.add(panelRadio, BorderLayout.NORTH);
//		panel.add(panelBotones, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle("Grafo de Dependencia");
//		this.dialogo.setSize(ANCHO_CUADRO, this.numeroFilas * 23 + 90);
//		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO,
//				this.numeroFilas * 23 + 90);
//		this.dialogo.setLocation(coord[0], coord[1]);
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == this.aceptar) {
//		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
