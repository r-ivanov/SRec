package cuadros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.border.EmptyBorder;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import botones.*;
import datos.*;
import opciones.GestorOpciones;
import opciones.OpcionOpsVisualizacion;
import utilidades.*;
import conf.*;
import ventanas.*;

/**
 * Permite construir un nuevo cuadro de diálogo que permite al usuario elegir
 * qué método de una determinada clase se quiere ejecutar.
 * 
 * @author Francisco Alcazar
 * @version 2020-2021
 */
public class CuadroPreguntaSeleccionVistasEspecificas 
		extends Thread implements ActionListener, KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 390;//420
	private static final int ALTO_CUADRO = 190;//220
	
	private OpcionOpsVisualizacion oov;
	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	private JPanel panel, panelBoton, panelBotones;

	private JRadioButton botonesSeleccion[];

	private ClaseAlgoritmo clase;

	private BorderLayout bl;
	private boolean DYV;
	private Ventana ventana;
	private Preprocesador preprocesador;
	private JDialog dialogo;
	private GestorOpciones gOpciones = new GestorOpciones();

	/**
	 * Contructor: genera un nuevo cuadro de diálogo que permite al usuario
	 * elegir qué método de una determinada clase se quiere ejecutar
	 * 
	 * @param clase
	 *            Clase a la que pertenece el método que se quiere ejecutar
	 * @param ventana_
	 *            Ventana a la que se asociará el cuadro de diálogo
	 * @param preprocesador_
	 *            Gestor que realizará los pasos necesarios para ejecutar el
	 *            método seleccionado
	 * @param DYV_
	 *            true significa que tiene metodos que pueden ser de tipo DyV, false significa lo contrario
	 */
	public CuadroPreguntaSeleccionVistasEspecificas(
			ClaseAlgoritmo claseAlgoritmo, Ventana ventana_, 
			Preprocesador preprocesador_,boolean DYV_) {
		dialogo = new JDialog(ventana, true);
		ventana = ventana_;
		clase = claseAlgoritmo;
		preprocesador = preprocesador_;
		DYV=DYV_;
		start();
	}
	

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		int numeroMetodos = 0;
		if(clase != null) {
			numeroMetodos = clase.getMetodos().size();
		}
		
		if (numeroMetodos > 0) {

			JPanel panelFilaSup = new JPanel();
			panelFilaSup.setLayout(new BorderLayout());

			JLabel etiqSignatura = 
					new JLabel(Texto.get("CSM_SIGNAT", Conf.idioma));

			JPanel panelDerechaSuperior = new JPanel();

			panelFilaSup.add(etiqSignatura, BorderLayout.WEST);
			panelFilaSup.add(panelDerechaSuperior, BorderLayout.EAST);

			// Botón Aceptar
			aceptar = new BotonAceptar();
			aceptar.addKeyListener(this);
			aceptar.addMouseListener(this);

			// Botón Cancelar
			cancelar = new BotonCancelar();
			cancelar.addKeyListener(this);
			cancelar.addMouseListener(this);

			// Panel para el botón
			panelBoton = new JPanel();
			panelBoton.add(aceptar);
			panelBoton.add(cancelar);
			GridLayout layoutGrid = new GridLayout(3, 1);
			panelBotones= new JPanel(layoutGrid);
			
			oov = (OpcionOpsVisualizacion) gOpciones.getOpcion("OpcionOpsVisualizacion", false);
			int anterior = oov.getAnteriorVE();

			botonesSeleccion= new JRadioButton[3];
			ButtonGroup grupoBotones = new ButtonGroup();
			
			for(int i = 0; i < botonesSeleccion.length; i++) {
				botonesSeleccion[i] = new JRadioButton();
				botonesSeleccion[i].setText("  "+Texto.get(
						"BOTON"+Integer.toString(i + 1)
						+ "_SELECCVISTASESPECIFICAS", Conf.idioma));
				botonesSeleccion[i].addKeyListener(this);
				botonesSeleccion[i].addActionListener(this);
				grupoBotones.add(botonesSeleccion[i]);
				panelBotones.add(botonesSeleccion[i]);
			}			
			
			// Deshabilitar boton de DyV en caso de no tener metodos compatibles
			botonesSeleccion[1].setEnabled(DYV);
			
			// Marcar ultima vista seleccionada
			if(anterior == 0) { // 0 = vistas generales
				botonesSeleccion[0].setSelected(true);
			}else if(anterior == 1){  // 1 = DyV
				botonesSeleccion[1].setSelected(true);
			}else {
				botonesSeleccion[2].setSelected(true);
			}
			
			// Panel general
			bl = new BorderLayout();
			panel = new JPanel();
			panel.setLayout(bl);

			JPanel panelContenedorBotones = new JPanel();
			panelContenedorBotones.setLayout(new BorderLayout());
			panelContenedorBotones.add(panelBotones, BorderLayout.NORTH);

			JScrollPane jsp = new JScrollPane(panelContenedorBotones);

			jsp.add(panelBotones);
			jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
			jsp.setPreferredSize(new Dimension(ANCHO_CUADRO - 10, ALTO_CUADRO - 120));

			JPanel panelSup = new JPanel();
			
			JLabel texto = new JLabel();
			texto.setText(" " + Texto.get("PREGMEN_SELECCVISTASESPECIFICAS", Conf.idioma));
			
			panelSup.add(texto);
			panel.add(texto, BorderLayout.NORTH);
			panel.add(panelBotones, BorderLayout.CENTER);
			panel.add(panelBoton, BorderLayout.SOUTH);

			// Preparamos y mostramos cuadro
			dialogo.getContentPane().add(panel);
			dialogo.setTitle(" " + Texto.get("PREG_SELECCVISTASESPECIFICAS", Conf.idioma));

			dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO-60);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
			dialogo.setLocation(coord[0], coord[1]);
			dialogo.setResizable(false);
			dialogo.setVisible(true);
			
			// Eliminamos archivos inservibles 
			// (Todos los creados menos el necesario para lanzar la ejecución)
			File directorioAplicacion = null;
			try {
				directorioAplicacion = new File(".");
			} catch (Exception exc) {
			}
			File archivosParaEliminar[] = directorioAplicacion.listFiles();
			for (int i = 0; i < archivosParaEliminar.length; i++) {
				if (!archivosParaEliminar[i].getName().startsWith("SRec_")&&
						archivosParaEliminar[i].getName().contains("codSRec_")){
					archivosParaEliminar[i].delete();
				}
			}
		} else {
			new CuadroError(
					ventana, Texto.get("ERROR_CLASE", Conf.idioma), 
					Texto.get("CSM_NOVISMET", Conf.idioma));
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
		if(botonesSeleccion[0]==e.getSource()) {
			botonesSeleccion[1].setSelected(false);
			botonesSeleccion[2].setSelected(false);
		}
		if(botonesSeleccion[1]==e.getSource()) {
			botonesSeleccion[0].setSelected(false);
			botonesSeleccion[2].setSelected(false);
		}
		if(botonesSeleccion[2]==e.getSource()) {
			botonesSeleccion[0].setSelected(false);
			botonesSeleccion[1].setSelected(false);
		}
		if (e.getSource() == cancelar) {
			dialogo.setVisible(false);
			dialogo.dispose();
		} 
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

		if (code == KeyEvent.VK_ENTER) {
			if(botonesSeleccion[0].isSelected()) {
				oov.setAnteriorVE(0);
				gOpciones.setOpcion(oov, 1);
				dialogo.setVisible(false);
				if (Conf.fichero_log) {
					Logger.log_write("Ninguna vista específica");
				}
				preprocesador.fase2(clase);
				dialogo.dispose();
			}else if(botonesSeleccion[1].isSelected()) {
				if (Conf.fichero_log) {
					Logger.log_write("¿Habilitar vistas para DYV? Sí");
				}
				oov.setAnteriorVE(1);
				gOpciones.setOpcion(oov, 1);
				new CuadroSeleccionMetodos(clase, ventana, preprocesador, true);
				dialogo.setVisible(false);
				dialogo.dispose();
			}else if(botonesSeleccion[2].isSelected()) {
				if (Conf.fichero_log) {
					Logger.log_write("Habilitar vistas basadas en arboles");
				}
				oov.setAnteriorVE(2);
				gOpciones.setOpcion(oov, 1);
				new CuadroSeleccionMetodos(clase, ventana, preprocesador, false);
				dialogo.setVisible(false);
				dialogo.dispose();
			}
		}else if (code == KeyEvent.VK_ESCAPE) {
			dialogo.setVisible(false);
			dialogo.dispose();
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
	/*
	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == aceptar) {
			if(botonesSeleccion[0].isSelected()) {
				oov.setAnteriorVE(0);
				gOpciones.setOpcion(oov, 1);
				dialogo.setVisible(false);
				if (Conf.fichero_log) {
					Logger.log_write("Ninguna vista específica");
				}
				preprocesador.fase2(clase);
				dialogo.dispose();
			}else if(botonesSeleccion[1].isSelected()) {
				if (Conf.fichero_log) {
					Logger.log_write("¿Habilitar vistas para DYV? Sí");
				}
				oov.setAnteriorVE(1);
				gOpciones.setOpcion(oov, 1);

				dialogo.setVisible(false);
				new CuadroSeleccionMetodos(clase, ventana, preprocesador, true);
				dialogo.dispose();
			}else if(botonesSeleccion[2].isSelected()) {
				if (Conf.fichero_log) {
					Logger.log_write("¿Habilitar vistas basadas en árboles de búsqueda? Sí");
				}
				oov.setAnteriorVE(2);
				gOpciones.setOpcion(oov, 1);
				dialogo.setVisible(false);
				new CuadroSeleccionMetodos(clase, ventana, preprocesador, false);
				dialogo.dispose();
			}
		}else if (e.getSource() == cancelar) {
			dialogo.setVisible(false);
			dialogo.dispose();
		} 
	}
}