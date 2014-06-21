package cuadros;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.TitledBorder;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import conf.*;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

/**
 * Implementa el cuadro que permite configurar la opción de Máquina virtual de
 * java
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroOpcionMVJava extends Thread implements ActionListener,
		KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 448;
	private static final int ALTO_CUADRO = 124;

	private JTextField campoDireccion;
	private BotonTexto examinar;
	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	private JPanel panel, panelBoton, panelpanelElementos, panelElementos;

	private BorderLayout bl;
	private GridLayout gl;

	private Ventana ventana;
	private JDialog dialogo;

	private GestorOpciones gOpciones = new GestorOpciones();
	private OpcionMVJava omvj = null;

	private String fichero[] = new String[2];

	private String textoIndicativo = "";

	/**
	 * Crea una nueva instancia de la opción que permite configurar la opción de
	 * Máquina virtual de java.
	 * 
	 * @param ventana
	 *            Ventana a la que estará asociado el cuadro de diálogo
	 * @param inicioAplicacion
	 *            true si se ha de cargar texto más explicativo para la primera
	 *            ejecución de la aplicación
	 */
	public CuadroOpcionMVJava(Ventana ventana, boolean inicioAplicacion) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		if (inicioAplicacion) {
			this.textoIndicativo = Texto.get("COMVJ_ESCRDIRVALTTT3",
					Conf.idioma);
		} else {
			this.textoIndicativo = Texto.get("COMVJ_ESCRDIRVALTTT2",
					Conf.idioma);
		}
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		this.omvj = (OpcionMVJava) this.gOpciones.getOpcion("OpcionMVJava",
				true);

		this.fichero[0] = "C:\\";

		int anchoCampo = 35;
		if (!this.ventana.msWindows) {
			anchoCampo = 23;
		}

		// Elementos
		if (this.omvj.getDir() == null || this.omvj.getDir().equals("")) {
			this.campoDireccion = new JTextField(Texto.get("COMVJ_ESCRDIR",
					Conf.idioma), anchoCampo);
		} else {
			this.campoDireccion = new JTextField(this.omvj.getDir(), anchoCampo);
		}

		this.campoDireccion.addKeyListener(this);
		this.campoDireccion.setToolTipText(Texto.get("COMVJ_ESCRDIRVAL",
				Conf.idioma));

		this.examinar = new BotonTexto(Texto.get("BOTONEXAMINAR", Conf.idioma));
		// examinar.addActionListener(this);
		this.examinar.addKeyListener(this);
		this.examinar.addMouseListener(this);
		this.examinar.setToolTipText(Texto.get("COMVJ_ESCRDIRVALTTT",
				Conf.idioma));

		// Panel de elementos
		this.panelElementos = new JPanel();
		this.panelElementos.add(this.campoDireccion);
		this.panelElementos.add(this.examinar);
		this.panelElementos.setSize(120, 45);

		// Panel de panel de Elementos
		this.gl = new GridLayout(1, 1);
		this.panelpanelElementos = new JPanel();
		this.panelpanelElementos.setLayout(this.gl);
		this.panelpanelElementos.setBorder(new TitledBorder(
				this.textoIndicativo));
		this.panelpanelElementos.add(this.panelElementos);

		// Botón Aceptar
		this.aceptar = new BotonAceptar();// aceptar=new JButton ("Aceptar");
		// aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);
		this.aceptar.addMouseListener(this);
		if (!valorarSeleccion(false)) {
			this.aceptar.setEnabled(false);
		}

		// Botón Cancelar
		this.cancelar = new BotonCancelar();
		// cancelar.addActionListener(this);
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

		this.panel.add(this.panelpanelElementos, BorderLayout.NORTH);
		this.panel.add(this.panelBoton, BorderLayout.SOUTH);

		this.dialogo.getContentPane().add(this.panel);

		// Preparamos y mostramos cuadro
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);
		this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setResizable(false);
		this.dialogo.setTitle(Texto.get("COMVJ_SELECTITULO", Conf.idioma));
		this.dialogo.setVisible(true);
		this.dialogo.setAlwaysOnTop(true);
	}

	/**
	 * Comprueba que la dirección seleccionada es correcta
	 * 
	 * @param aceptarPulsado
	 *            true si el usuario pulsó el botón Aceptar false si el método
	 *            es ejecutado por la propia aplicación de manera interna
	 * @return true si la dirección es válida
	 */
	private boolean valorarSeleccion(boolean aceptarPulsado) {
		this.omvj.setDir(this.campoDireccion.getText());
		if (this.omvj.getValida()) {
			this.aceptar.setEnabled(true);
			if (aceptarPulsado) {
				this.gOpciones.setOpcion(this.omvj, 2);
				this.dialogo.setVisible(false);
				new CuadroInformacion(this.ventana, Texto.get("INFO_MVJOK",
						Conf.idioma), Texto.get("INFO_MVJAVA", Conf.idioma),
						550, 100);
			}
			return true;
		} else {
			this.aceptar.setEnabled(false);
			if (aceptarPulsado) {
				new CuadroError(this.dialogo, Texto.get("ERROR_MVJAVA",
						Conf.idioma), Texto.get("ERROR_NOMVJ", Conf.idioma));
			}
			return false;
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
		if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		} else if (code == KeyEvent.VK_ENTER) {
			if (this.aceptar.isFocusOwner()
					|| this.campoDireccion.isFocusOwner()) {
				valorarSeleccion(true);
			} else if (this.examinar.isFocusOwner()) {
				this.fichero = SelecDireccion.cuadroAbrirFichero(
						this.fichero[0],
						Texto.get("COMVJ_SELECTITULO", Conf.idioma),
						"java.exe", "exe",
						Texto.get("ARCHIVO_EXE", Conf.idioma), 1);
				// *1* Comprobar que fichero existe
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
		if (e.getSource() == this.aceptar) {
			valorarSeleccion(true);
		} else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		} else if (e.getSource() == this.examinar) {
			this.fichero = SelecDireccion.cuadroAbrirFichero(this.fichero[0],
					Texto.get("COMVJ_SELECTITULO", Conf.idioma), "java.exe",
					"exe", Texto.get("ARCHIVO_EXE", Conf.idioma), 1);
			// *1* Comprobar que fichero existe
			if (this.fichero[1] != null) {
				this.campoDireccion.setText(this.fichero[0] + this.fichero[1]);
			}
			valorarSeleccion(false);
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
