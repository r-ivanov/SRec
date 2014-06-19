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
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import conf.*;
import opciones.*;
import botones.*;
import utilidades.*;
import ventanas.*;

/**
 * Representa la clase del cuadro que maneja la opción que gestiona el
 * mantenimiento de ficheros intermedios
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroOpcionBorradoFicheros extends Thread implements
		ActionListener, KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 400;
	private static final int ALTO_CUADRO = 210;

	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	private JPanel panel, panelBoton, panelBotones;

	private OpcionBorradoFicheros obf = null;
	private GestorOpciones gOpciones = new GestorOpciones();

	private JCheckBox[] botones;

	private BorderLayout bl;
	private GridLayout gl;

	private JDialog dialogo;

	/**
	 * Genera un nuevo cuadro que maneja la opción que gestiona el mantenimiento
	 * de ficheros intermedios.
	 * 
	 * @param ventana
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo.
	 */
	public CuadroOpcionBorradoFicheros(Ventana ventana) {
		this.dialogo = new JDialog(ventana, true);
		this.start();
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		this.obf = (OpcionBorradoFicheros) this.gOpciones.getOpcion(
				"OpcionBorradoFicheros", true);

		// Creamos panel con cinco JCheckBoxes
		this.panelBotones = new JPanel();
		this.panelBotones.addKeyListener(this);

		this.gl = new GridLayout(5, 1);
		this.panelBotones.setLayout(this.gl);
		this.panelBotones.setBorder(new TitledBorder(Texto.get("COBF_SELECT",
				Conf.idioma)));

		this.botones = new JCheckBox[5];
		this.botones[0] = new JCheckBox(Texto.get("COBF_XMLORI", Conf.idioma),
				!this.obf.getfXml());
		this.botones[1] = new JCheckBox(Texto.get("COBF_XMLGEN", Conf.idioma),
				!this.obf.getfXmlzv());
		this.botones[2] = new JCheckBox(Texto.get("COBF_JAVAGEN", Conf.idioma),
				!this.obf.getfJavazv());
		this.botones[3] = new JCheckBox(Texto.get("COBF_COMPORI", Conf.idioma),
				!this.obf.getfClass());
		this.botones[4] = new JCheckBox(Texto.get("COBF_COMPGEN", Conf.idioma),
				!this.obf.getfClasszv());

		this.botones[0].setToolTipText(Texto
				.get("COBF_XMLORI_TTT", Conf.idioma));
		this.botones[1].setToolTipText(Texto
				.get("COBF_XMLGEN_TTT", Conf.idioma));
		this.botones[2].setToolTipText(Texto.get("COBF_JAVAGEN_TTT",
				Conf.idioma));
		this.botones[3].setToolTipText(Texto.get("COBF_COMPORI_TTT",
				Conf.idioma));
		this.botones[4].setToolTipText(Texto.get("COBF_COMPGEN_TTT",
				Conf.idioma));

		for (int i = 0; i < this.botones.length; i++) {
			this.botones[i].addKeyListener(this);
			this.panelBotones.add(this.botones[i]);
		}

		// Botón Aceptar
		this.aceptar = new BotonAceptar();
		this.aceptar.addKeyListener(this);
		this.aceptar.addMouseListener(this);

		// Botón Cancelar
		this.cancelar = new BotonCancelar();
		this.cancelar.addKeyListener(this);
		this.cancelar.addMouseListener(this);

		// Panel para los botones
		this.panelBoton = new JPanel();
		this.panelBoton.add(this.aceptar);
		this.panelBoton.add(this.cancelar);

		// Panel general
		this.bl = new BorderLayout();
		this.panel = new JPanel();
		this.panel.setLayout(this.bl);

		this.panel.add(this.panelBotones, BorderLayout.NORTH);
		this.panel.add(this.panelBoton, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(this.panel);
		this.dialogo.setTitle(Texto.get("COBF_TITULO", Conf.idioma));
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);
		this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setResizable(false);

		this.dialogo.setVisible(true);
	}

	/**
	 * Almacena los valores del cuadro en la opción y guarda la opción en
	 * fichero. Después hace invisible el cuadro
	 */
	private void almacenarValoresYOcultar() {
		this.obf.setfXml(!this.botones[0].isSelected());
		this.obf.setfXmlzv(!this.botones[1].isSelected());
		this.obf.setfJavazv(!this.botones[2].isSelected());
		this.obf.setfClass(!this.botones[3].isSelected());
		this.obf.setfClasszv(!this.botones[4].isSelected());

		this.dialogo.setVisible(false);

		this.gOpciones.setOpcion(this.obf, 2);
	}

	/**
	 * Gestioan los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

	}

	/**
	 * Gestioan los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ENTER) {
			if (this.aceptar.isFocusOwner()) {
				almacenarValoresYOcultar();
			}
		}
	}

	/**
	 * Gestioan los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ENTER) {
			almacenarValoresYOcultar();
		}
		if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		}
	}

	/**
	 * Gestioan los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	/**
	 * Gestioan los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	/**
	 * Gestioan los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	/**
	 * Gestioan los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	/**
	 * Gestioan los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (((e.getSource())) == this.aceptar) {
			almacenarValoresYOcultar();
		} else if (((e.getSource())) == this.cancelar) {
			this.dialogo.setVisible(false);
		}
	}

	/**
	 * Gestioan los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

}
