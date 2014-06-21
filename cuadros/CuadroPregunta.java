package cuadros;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

import ventanas.Ventana;
import conf.*;
import botones.*;

/**
 * Permite construir cuadros de pregunta genéricos, en los que el usuario puede
 * decidir aceptar, cancelar o ignorar una acción específica.
 * 
 * @author Antonio Pérez Carrasco
 * @author David Pastor Herranz
 */
public abstract class CuadroPregunta extends Thread implements ActionListener,
		KeyListener {

	private static final int ANCHO_CUADRO = 650;
	private static final int ALTO_CUADRO = 100;

	private JLabel etiqueta, imagen;

	protected BotonTexto aceptar;
	protected BotonTexto cancelar;
	protected BotonTexto ignorar;

	private String textoBotonAceptar;
	private String textoBotonCancelar;
	private String textoBotonIgnorar;

	private JPanel panel, panelBoton, panelImagen, panelDerecha, panelEtiqueta;

	private String titulo;
	private String etiq;

	private BorderLayout bl, bl0, bl1;

	protected Ventana ventana;
	protected JDialog d;

	/**
	 * Genera un cuadro de pregunta, en el que el usuario puede decidir aceptar,
	 * cancelar o ignorar una acción específica.
	 * 
	 * @param ventana
	 *            ventana a la que quedará asociado este cuadro
	 * @param titulo
	 *            título que llevará el cuadro de pregunta.
	 * @param etiq
	 *            mensaje que mostrará el cuadro de pregunta.
	 * @param textoBotonAceptar
	 *            Texto que debe mostrar el botón de aceptar.
	 * @param textoBotonCancelar
	 *            Texto que debe mostrar el botón de cancelar.
	 * @param textoBotonIgnorar
	 *            Texto que debe mostrar el botón de ignorar.
	 */
	public CuadroPregunta(Ventana ventana, String titulo, String etiq,
			String textoBotonAceptar, String textoBotonCancelar,
			String textoBotonIgnorar) {
		this.d = new JDialog(ventana, true);
		this.ventana = ventana;
		this.titulo = titulo;
		this.etiq = etiq;
		this.textoBotonAceptar = textoBotonAceptar;
		this.textoBotonCancelar = textoBotonCancelar;
		this.textoBotonIgnorar = textoBotonIgnorar;
	}

	/**
	 * Genera un cuadro de pregunta, en el que el usuario puede decidir aceptar
	 * o cancelar una acción específica.
	 * 
	 * @param ventana
	 *            ventana a la que quedará asociado este cuadro
	 * @param titulo
	 *            título que llevará el cuadro de pregunta.
	 * @param etiq
	 *            mensaje que mostrará el cuadro de pregunta.
	 * @param textoBotonAceptar
	 *            Texto que debe mostrar el botón de aceptar.
	 * @param textoBotonCancelar
	 *            Texto que debe mostrar el botón de cancelar.
	 */
	public CuadroPregunta(Ventana ventana, String titulo, String etiq,
			String textoBotonAceptar, String textoBotonCancelar) {
		this.d = new JDialog(ventana, true);
		this.ventana = ventana;
		this.titulo = titulo;
		this.etiq = etiq;
		this.textoBotonAceptar = textoBotonAceptar;
		this.textoBotonCancelar = textoBotonCancelar;
	}
	
	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		// Etiqueta para icono
		this.panelImagen = new JPanel();
		this.imagen = new JLabel(new ImageIcon("imagenes/alarma.gif"));
		this.imagen.addKeyListener(this);
		this.imagen.setHorizontalAlignment(0);
		this.imagen.setVerticalAlignment(0);
		this.panelImagen.add(this.imagen);

		// Etiqueta de texto
		this.etiqueta = new JLabel(this.etiq);
		this.etiqueta.addKeyListener(this);
		this.etiqueta.setHorizontalAlignment(0);
		this.etiqueta.setVerticalAlignment(0);

		// Panel para etiqueta
		this.bl1 = new BorderLayout();
		this.panelEtiqueta = new JPanel();
		this.panelEtiqueta.setLayout(this.bl1);
		this.panelEtiqueta.add(this.etiqueta, BorderLayout.CENTER);

		if (this.textoBotonIgnorar != null) {
			// Botón Ignorar
			this.ignorar = new BotonTexto(this.textoBotonIgnorar, 130);
			this.ignorar.addActionListener(this);
			this.ignorar.addKeyListener(this);
		}

		// Botón Aceptar
		this.aceptar = new BotonTexto(this.textoBotonAceptar);
		this.aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);

		// Botón Cancelar
		this.cancelar = new BotonTexto(this.textoBotonCancelar);
		this.cancelar.addActionListener(this);
		this.cancelar.addKeyListener(this);

		// Panel de botones
		this.panelBoton = new JPanel();
		if (this.textoBotonIgnorar != null) {
			this.panelBoton.add(this.ignorar);
		}
		this.panelBoton.add(this.aceptar);
		this.panelBoton.add(this.cancelar);
		this.panelBoton.addKeyListener(this);
		this.panelBoton.transferFocus();

		// Panel de la derecha
		this.bl0 = new BorderLayout();
		this.panelDerecha = new JPanel();
		this.panelDerecha.setLayout(this.bl0);
		this.panelDerecha.add(this.panelEtiqueta, BorderLayout.CENTER);
		this.panelDerecha.add(this.panelBoton, BorderLayout.SOUTH);
		this.panelDerecha.addKeyListener(this);

		// Panel general de CuadroPregunta
		this.bl = new BorderLayout();
		this.panel = new JPanel();
		this.panel.setLayout(this.bl);
		this.panel.addKeyListener(this);

		this.panel.add(this.imagen, BorderLayout.WEST);
		this.panel.add(this.panelDerecha, BorderLayout.CENTER);

		this.d.getContentPane().add(this.panel);
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.d.setLocation(coord[0], coord[1]);
		this.d.setTitle(this.titulo);
		this.d.setSize(ANCHO_CUADRO, ALTO_CUADRO);
		this.d.setResizable(false);

		this.aceptar.transferFocus();
		this.d.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

}