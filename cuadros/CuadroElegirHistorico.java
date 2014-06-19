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
import javax.swing.ButtonGroup;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import conf.*;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite configurar la visualización de las llamadas terminadas del árbol.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroElegirHistorico extends Thread implements ActionListener,
	KeyListener, MouseListener {

    private static final int NUM_OPCIONES = 3;
    private static final int ANCHO_CUADRO = 300;
    private static final int ALTO_CUADRO = (NUM_OPCIONES * 30) + 75;

    private OpcionOpsVisualizacion oov = null;
    private GestorOpciones gOpciones = new GestorOpciones();

    private ButtonGroup grupoBotonesHistoria;
    private JRadioButton botonesHistoria[];

    private JDialog dialogo;
    private Ventana ventana;

    private BotonAceptar aceptar = new BotonAceptar();
    private BotonCancelar cancelar = new BotonCancelar();

    /**
     * Construye un nuevo cuadro que permite configurar la visualización de las
     * llamadas terminadas del árbol.
     * 
     * @param Ventana
     *            Ventana a la que permanecerá asociado el cuadro de diálogo
     */
    public CuadroElegirHistorico(Ventana ventana) {
	dialogo = new JDialog(ventana, true);
	this.ventana = ventana;
	this.start();
    }

    /**
     * Genera una nueva opción
     */
    public void run() {

	oov = (OpcionOpsVisualizacion) gOpciones.getOpcion(
		"OpcionOpsVisualizacion", false);

	// Panel Datos que se muestran
	JPanel panelHistoria = new JPanel();
	GridLayout gl1 = new GridLayout(3, 1);
	panelHistoria.setLayout(gl1);

	botonesHistoria = new JRadioButton[3];
	botonesHistoria[0] = new JRadioButton(Texto.get(
		"COOV_INFONODOMANTHIST", Conf.idioma));
	botonesHistoria[1] = new JRadioButton(Texto.get(
		"COOV_INFONODOATENHIST", Conf.idioma));
	botonesHistoria[2] = new JRadioButton(Texto.get(
		"COOV_INFONODOELIMHIST", Conf.idioma));

	botonesHistoria[0].setToolTipText(Texto.get("COOV_INFONODOMANTHISTTTT",
		Conf.idioma));
	botonesHistoria[1].setToolTipText(Texto.get("COOV_INFONODOATENHISTTTT",
		Conf.idioma));
	botonesHistoria[2].setToolTipText(Texto.get("COOV_INFONODOELIMHISTTTT",
		Conf.idioma));

	grupoBotonesHistoria = new ButtonGroup();

	for (int i = 0; i < botonesHistoria.length; i++) {
	    grupoBotonesHistoria.add(botonesHistoria[i]);
	    botonesHistoria[i].addActionListener(this);
	    botonesHistoria[i].addKeyListener(this);
	    panelHistoria.add(botonesHistoria[i]);
	}

	panelHistoria.setBorder(new TitledBorder(Texto.get("COOV_INFOHIST",
		Conf.idioma)));

	// Panel para los botones
	JPanel panelBoton = new JPanel();
	panelBoton.add(aceptar);
	panelBoton.add(cancelar);

	// aceptar.addActionListener(this);
	aceptar.addMouseListener(this);
	aceptar.addKeyListener(this);
	cancelar.addMouseListener(this);
	cancelar.addKeyListener(this);

	// Panel general
	BorderLayout bl = new BorderLayout();
	JPanel panel = new JPanel();
	panel.setLayout(bl);

	panel.add(panelHistoria, BorderLayout.NORTH);
	panel.add(panelBoton, BorderLayout.SOUTH);

	// Preparamos y mostramos cuadro
	dialogo.getContentPane().add(panel);
	dialogo.setTitle(Texto.get("COOV_OPSANIM", Conf.idioma));

	dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
	int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
	dialogo.setLocation(coord[0], coord[1]);

	dialogo.setResizable(false);
	setValores();
	dialogo.setVisible(true);
    }

    /**
     * Obtiene el valor actual de configuración y establece como seleccionado el
     * botón correspondiente.
     */
    private void setValores() {
	if (oov.getHistoria() == OpcionOpsVisualizacion.MANTENER_HISTORIA) {
	    botonesHistoria[0].setSelected(true);
	} else if (oov.getHistoria() == OpcionOpsVisualizacion.ATENUAR_HISTORIA) {
	    botonesHistoria[1].setSelected(true);
	} else if (oov.getHistoria() == OpcionOpsVisualizacion.ELIMINAR_HISTORIA) {
	    botonesHistoria[2].setSelected(true);
	}
    }

    /**
     * Actualiza la configuración con el valor seleccionado.
     */
    private void getValores() {
	if (botonesHistoria[0].isSelected()) {
	    oov.setHistoria(OpcionOpsVisualizacion.MANTENER_HISTORIA);
	} else if (botonesHistoria[1].isSelected()) {
	    oov.setHistoria(OpcionOpsVisualizacion.ATENUAR_HISTORIA);
	} else if (botonesHistoria[2].isSelected()) {
	    oov.setHistoria(OpcionOpsVisualizacion.ELIMINAR_HISTORIA);
	}
    }

    /**
     * Gestiona los eventos de acción
     * 
     * @param e
     *            evento de acción
     */
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() instanceof JRadioButton) {
	    getValores();
	    gOpciones.setOpcion(oov, 1);

	    if (Conf.fichero_log) {
		Logger.log_write("Visualización > Históricos: "
			+ oov.getHistoriaString());
	    }

	    Conf.setValoresOpsVisualizacion(false);
	    Conf.setValoresVisualizacion();
	    if (ventana.getTraza() != null) {
		ventana.actualizarTraza();
		ventana.refrescarFormato();
		Conf.setRedibujarGrafoArbol(false);
	    }
	} else if (e.getSource() instanceof JButton) {
	    if (e.getSource() == aceptar) {
		dialogo.setVisible(false);
	    }
	}
    }

    /**
     * Gestiona los eventos de teclado
     * 
     * @param e
     *            evento de teclado
     */
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Gestiona los eventos de teclado
     * 
     * @param e
     *            evento de teclado
     */
    public void keyReleased(KeyEvent e) {
	int code = e.getKeyCode();
	if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
	    dialogo.setVisible(false);
	}

	if (e.getSource().getClass().getName().contains("JRadioButton")) {
	    for (int i = 0; i < botonesHistoria.length; i++) {
		if (botonesHistoria[i].isFocusOwner()) {
		    if (code == KeyEvent.VK_DOWN) {
			if (i < botonesHistoria.length - 1) {
			    botonesHistoria[i].transferFocus();
			} else {
			    aceptar.requestFocus();
			}
		    } else if (code == KeyEvent.VK_UP) {
			if (i > 0) {
			    botonesHistoria[i].transferFocusBackward();
			} else {
			    aceptar.requestFocus();
			}
		    }
		}
	    }
	}
	if (e.getSource() instanceof JButton) {
	    if (code == KeyEvent.VK_DOWN) {
		botonesHistoria[0].requestFocus();
	    } else if (code == KeyEvent.VK_UP) {
		botonesHistoria[(botonesHistoria.length) - 1].requestFocus();
	    }
	}

    }

    /**
     * Gestiona los eventos de teclado
     * 
     * @param e
     *            evento de teclado
     */
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Gestiona los eventos de ratón
     * 
     * @param e
     *            evento de ratón
     */
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Gestiona los eventos de ratón
     * 
     * @param e
     *            evento de ratón
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Gestiona los eventos de ratón
     * 
     * @param e
     *            evento de ratón
     */
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Gestiona los eventos de ratón
     * 
     * @param e
     *            evento de ratón
     */
    public void mousePressed(MouseEvent e) {
	if (e.getSource() == aceptar) {
	    this.dialogo.setVisible(false);
	}
    }

    /**
     * Gestiona los eventos de ratón
     * 
     * @param e
     *            evento de ratón
     */
    public void mouseReleased(MouseEvent e) {
	if (e.getSource() == cancelar) {
	    dialogo.setVisible(false);
	}
    }
}
