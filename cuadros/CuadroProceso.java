package cuadros;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.TitledBorder;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import botones.BotonCancelar;
import conf.*;
import utilidades.AnimatedIcon;
import utilidades.TextIcon;
import ventanas.Ventana;

/**
 * Implementa el cuadro de proceso que informa que un proceso de carga
 * se está llevando a cabo.
 * 
 * @author David Pastor Herranz
 */
public class CuadroProceso extends Thread implements ActionListener, KeyListener, MouseListener{

	private static final int ANCHO_CUADRO = 250;
	private static final int ALTO_CUADRO = 90;

	private JLabel etiqueta;
	private JPanel panel, panelTexto, panelBoton;
	
	private BotonCancelar cancelar;

	private JDialog dialogo;

	private String nombre, texto;
	private AnimatedIcon animacion;
	
	private Thread proceso;

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
	 */
	public CuadroProceso(Ventana ventana, String nombre, String texto) {

		this.dialogo = new JDialog(ventana, true);
		this.dialogo.addKeyListener(this);
		this.dialogo.addMouseListener(this);
		
		this.nombre = nombre;
		this.texto = texto;
		
		this.etiqueta = new JLabel();
		
		this.cancelar = new BotonCancelar();
		this.cancelar.addKeyListener(this);
		this.cancelar.addMouseListener(this);
		
		this.start();
	}
	
	/**
	 * Relaciona un determinado proceso con el cuadro, para
	 * ser gestionado desde el cuadro.
	 * 
	 * @param proceso Proceso que gestiona el cuadro.
	 */
	public void setProceso(Thread proceso) {
		this.proceso = proceso;
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		
		// Panel de texto
		this.panelTexto = new JPanel();
		this.panelTexto.setBorder(new TitledBorder(""));
		this.panelTexto.add(this.etiqueta);

		// Panel de progreso
		this.panelBoton = new JPanel();
		this.panelBoton.add(this.cancelar);

		// Panel general
		this.panel = new JPanel(new BorderLayout());
		this.panel.add(this.panelBoton, BorderLayout.SOUTH);
		this.panel.add(this.panelTexto, BorderLayout.NORTH);
		this.panel.addKeyListener(this);

		// Inicializacion de valores
		this.etiqueta.setText(texto);
		this.etiqueta.setHorizontalTextPosition( JLabel.LEADING );
		this.animacion = new AnimatedIcon(this.etiqueta);
		this.animacion.setAlignmentX( AnimatedIcon.LEFT );
		this.animacion.addIcon( new TextIcon(this.etiqueta, ".") );
		this.animacion.addIcon( new TextIcon(this.etiqueta, "..") );
		this.animacion.addIcon( new TextIcon(this.etiqueta, "...") );
		this.animacion.addIcon( new TextIcon(this.etiqueta, "....") );
		this.etiqueta.setIcon(this.animacion);		
		this.animacion.start();

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
	 * Detiene la ejecución y hace invisible el cuadro, una vez ha finalizado el procesamiento
	 */
	private void detener() {
		if (this.proceso != null) {
			this.proceso.interrupt();
			this.proceso.stop();
		}
		this.cerrar();
	}
	
	/**
	 * Cierra el cuadro de dialogo;
	 */
	public void cerrar() {
		this.animacion.stop();
		this.dialogo.setVisible(false);
	}
	
	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.detener();
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
		if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_SPACE) {
			this.detener();
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
		if (e.getComponent().equals(this.cancelar)) {
			this.detener();
		}
	}
}
