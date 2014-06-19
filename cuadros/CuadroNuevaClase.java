package cuadros;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.border.TitledBorder;

import javax.swing.JDialog;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import conf.*;
import datos.Preprocesador;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite construir un cuadro para solicitar al usuario una dirección donde
 * almacenar una nueva clase.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroNuevaClase extends Thread implements KeyListener,
		MouseListener {

	private static final int ANCHO_CUADRO = 650;
	private static final int ALTO_CUADRO = 138;

	private BotonTexto examinar;
	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	private JPanel panel, panelBotones;

	private BorderLayout bl;

	private Ventana ventana;
	private JDialog dialogo;

	private GestorOpciones gOpciones = new GestorOpciones();
	private OpcionFicherosRecientes ofr = null;

	private String fichero[] = new String[2];

	private JTextField campoDirectorio;

	/**
	 * Genera un nuevo cuadro para solicitar al usuario una dirección donde
	 * almacenar una nueva clase.
	 * 
	 * @param ventana
	 *            Ventana a la que estará asociado el cuadro de diálogo.
	 */
	public CuadroNuevaClase(Ventana ventana) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		this.ofr = (OpcionFicherosRecientes) this.gOpciones.getOpcion(
				"OpcionFicherosRecientes", true);

		this.fichero[0] = this.ofr.getDir();
		this.fichero[1] = null;

		this.campoDirectorio = new JTextField();
		this.campoDirectorio.setEditable(false);

		// Panel nombre nueva clase
		JPanel panelClase = new JPanel();
		panelClase.setLayout(new BorderLayout());
		panelClase.add(new JLabel("Clase: "), BorderLayout.WEST);
		panelClase.add(this.campoDirectorio, BorderLayout.CENTER);

		// Panel nombre nueva clase
		JPanel panelDirectorio = new JPanel();
		panelDirectorio.setLayout(new BorderLayout());
		this.examinar = new BotonTexto(Texto.get("BOTONEXAMINAR", Conf.idioma));
		this.examinar.addMouseListener(this);
		this.examinar.addKeyListener(this);
		panelDirectorio.add(this.examinar, BorderLayout.EAST);

		// Panel vacío, para hacer hueco
		JPanel panelHueco = new JPanel();
		panelHueco.setPreferredSize(new Dimension(5, 5));

		// Panel para recoger los dos paneles anteriores
		JPanel panelDatos = new JPanel();
		panelDatos.setLayout(new BorderLayout());
		panelDatos.add(panelClase, BorderLayout.NORTH);
		panelDatos.add(panelHueco, BorderLayout.CENTER);
		panelDatos.add(panelDirectorio, BorderLayout.SOUTH);
		panelDatos.setBorder(new TitledBorder(Texto.get("CCCN_PARACREARCLASE",
				Conf.idioma)));

		// Botón Aceptar
		this.aceptar = new BotonAceptar();
		this.aceptar.addKeyListener(this);
		this.aceptar.addMouseListener(this);

		// Botón Cancelar
		this.cancelar = new BotonCancelar();
		this.cancelar.addKeyListener(this);
		this.cancelar.addMouseListener(this);

		// Panel para el botón
		this.panelBotones = new JPanel();
		this.panelBotones.add(this.aceptar);
		this.panelBotones.add(this.cancelar);

		// Panel general
		this.bl = new BorderLayout();
		this.panel = new JPanel();
		this.panel.setLayout(this.bl);

		this.panel.add(panelDatos, BorderLayout.NORTH);
		this.panel.add(this.panelBotones, BorderLayout.SOUTH);

		this.dialogo.getContentPane().add(this.panel);

		// Preparamos y mostramos cuadro
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);
		this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setResizable(false);
		this.dialogo.setTitle(Texto.get("CCCN_CREARCLASE", Conf.idioma));
		this.dialogo.setVisible(true);
		this.dialogo.setAlwaysOnTop(true);
	}

	/**
	 * Una vez que se determina que la dirección del fichero es la correcta,
	 * crea la clase y la preprocesa.
	 */
	public void archivoCorrecto() {
		String path = this.campoDirectorio.getText();

		GeneradorJava.crearClase(path);
		this.dialogo.setVisible(false);

		String carpeta = path
				.substring(0, path.lastIndexOf(File.separator) + 1);
		String nombre = path.substring(path.lastIndexOf(File.separator) + 1);

		if (Conf.fichero_log) {
			Logger.log_write("CuadroNuevaClase: nueva clase '" + nombre
					+ "' creada en '" + carpeta + "'");
		}
		new Preprocesador(carpeta, nombre);

		this.ofr.setDir(this.fichero[0]);
		this.gOpciones.setOpcion(this.ofr, 2);
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		} else if (code == KeyEvent.VK_ENTER) {
			File f = new File(this.fichero[0] + this.fichero[1]);
			if (f.exists()) {
				new CuadroPreguntaSobreescribir(this.ventana, this);
			} else {
				archivoCorrecto();
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
	public void keyReleased(KeyEvent e) {

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
		if (e.getSource() == this.aceptar) {
			File f = new File(this.fichero[0] + this.fichero[1]);
			if (f.exists()) {
				new CuadroPreguntaSobreescribir(this.ventana, this);
			} else {
				archivoCorrecto();
			}

		}

		else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		} else if (e.getSource() == this.examinar) {
			String[] f = SelecDireccion.cuadroAbrirFichero(this.fichero[0],
					Texto.get("CA_GNUEVACLASE", Conf.idioma), null, "java",
					Texto.get("ARCHIVO_JAVA", Conf.idioma), 0, false);

			if (f[1] != null) {
				this.fichero[0] = f[0];
				this.fichero[1] = f[1];

				if (!this.fichero[1].toLowerCase().endsWith(".java")) {
					this.fichero[1] = this.fichero[1] + ".java";
				}
				this.campoDirectorio.setText(this.fichero[0] + this.fichero[1]);

			}

		}
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseReleased(MouseEvent e) {

	}

}
