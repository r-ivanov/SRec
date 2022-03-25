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
import java.util.ArrayList;

import javax.swing.border.EmptyBorder;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
	
	private OpcionOpsVisualizacion oov ;
	private BotonAceptar aceptar;
	private BotonCancelar cancelar;
	private JPanel panel, panelBoton, panelBotones;

	// Numero total de métodos visualizables
	private int numero;

	private JRadioButton botonesSeleccion[];

	private ClaseAlgoritmo clase;

	// Listado de métodos seleccionados para ser procesados
	private ArrayList<MetodoAlgoritmo> metodos;

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
	 *            clase a la que pertenece el método que se quiere ejecutar
	 * @param ventana
	 *            ventana a la que se asociará el cuadro de diálogo
	 * @param gestorEjecucion
	 *            gestor que realizará los pasos necesarios para ejecutar el
	 *            método seleccionado
	 * @param codigounico
	 *            código único que identifica a la clase y la da nombre
	 */
	public CuadroPreguntaSeleccionVistasEspecificas(
			ClaseAlgoritmo claseAlgoritmo, Ventana ventana, 
			Preprocesador preprocesador,boolean DYV) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.clase = claseAlgoritmo;
		this.metodos = claseAlgoritmo.getMetodos();
		this.numero = this.metodos.size();
		this.preprocesador = preprocesador;
		this.DYV=DYV;
		this.start();
	}
	

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		if (this.numero > 0) {
			// String nombreClase = this.clase.getNombre();

			JPanel panelFilaSup = new JPanel();
			panelFilaSup.setLayout(new BorderLayout());

			JLabel etiqSignatura = 
					new JLabel(Texto.get("CSM_SIGNAT", Conf.idioma));

			JPanel panelDerechaSuperior = new JPanel();

			panelFilaSup.add(etiqSignatura, BorderLayout.WEST);
			panelFilaSup.add(panelDerechaSuperior, BorderLayout.EAST);

			// Botón Aceptar
			this.aceptar = new BotonAceptar();
			this.aceptar.addKeyListener(this);
			this.aceptar.addMouseListener(this);

			// Botón Cancelar
			this.cancelar = new BotonCancelar();
			this.cancelar.addKeyListener(this);
			this.cancelar.addMouseListener(this);

			// Panel para el botón
			this.panelBoton = new JPanel();
			this.panelBoton.add(this.aceptar);
			this.panelBoton.add(this.cancelar);
			GridLayout layoutGrid = new GridLayout(3, 1);
			this.panelBotones= new JPanel(layoutGrid);
			
			this.oov = (OpcionOpsVisualizacion) this.gOpciones
				.getOpcion("OpcionOpsVisualizacion", false);
			int anterior = oov.getAnteriorVE();

			this.botonesSeleccion= new JRadioButton[3];
			ButtonGroup grupoBotones = new ButtonGroup();
			
			for(int i = 0; i < this.botonesSeleccion.length; i++) {
				this.botonesSeleccion[i] = new JRadioButton();
				this.botonesSeleccion[i].setText("  "+Texto.get(
						"BOTON"+Integer.toString(i+1)
						+"_SELECCVISTASESPECIFICAS", Conf.idioma));
				this.botonesSeleccion[i].addKeyListener(this);
				this.botonesSeleccion[i].addActionListener(this);
				grupoBotones.add(botonesSeleccion[i]);
				panelBotones.add(this.botonesSeleccion[i]);
			}			
			
			// Deshabilitar boton de DyV en caso de no tener metodos compatibles
			this.botonesSeleccion[1].setEnabled(DYV);
			
			// Marcar ultima vista seleccionada
			if(anterior == 0) { // 0 = vistas generales
				this.botonesSeleccion[0].setSelected(true);
			}else if(anterior == 1){  // 1 = DyV
				this.botonesSeleccion[1].setSelected(true);
			}else {
				this.botonesSeleccion[2].setSelected(true);
			}
			
			// Panel general
			this.bl = new BorderLayout();
			this.panel = new JPanel();
			this.panel.setLayout(this.bl);

			JPanel panelContenedorBotones = new JPanel();
			panelContenedorBotones.setLayout(new BorderLayout());
			panelContenedorBotones.add(this.panelBotones, BorderLayout.NORTH);

			JScrollPane jsp = new JScrollPane(panelContenedorBotones);

			jsp.add(panelBotones);
			jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
			jsp.setPreferredSize(
					new Dimension(ANCHO_CUADRO - 10, ALTO_CUADRO - 120));

			JPanel panelSup = new JPanel();
			
			JLabel texto = new JLabel();
			texto.setText(" "
					+Texto.get("PREGMEN_SELECCVISTASESPECIFICAS", Conf.idioma));
			//panelSup.add(panelFilaSup, BorderLayout.NORTH);
			//panelSup.add(panelContenedorBotones, BorderLayout.CENTER);
			
			
			panelSup.add(texto);
			//this.panel.add(panelSup, BorderLayout.NORTH);
			this.panel.add(texto, BorderLayout.NORTH);
			this.panel.add(panelBotones, BorderLayout.CENTER);
			this.panel.add(this.panelBoton, BorderLayout.SOUTH);

			// Preparamos y mostramos cuadro
			this.dialogo.getContentPane().add(this.panel);
			this.dialogo.setTitle(" "
					+Texto.get("PREG_SELECCVISTASESPECIFICAS", Conf.idioma));

			// dialogo.setSize(anchoCuadro,altoCuadro+(alto*numero));
			this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO-60);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
			this.dialogo.setLocation(coord[0], coord[1]);
			this.dialogo.setResizable(false);
			this.dialogo.setVisible(true);
			
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
					this.ventana, Texto.get("ERROR_CLASE", Conf.idioma), 
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
		if(this.botonesSeleccion[0]==e.getSource()) {
			this.botonesSeleccion[1].setSelected(false);
			this.botonesSeleccion[2].setSelected(false);
		}
		if(this.botonesSeleccion[1]==e.getSource()) {
			this.botonesSeleccion[0].setSelected(false);
			this.botonesSeleccion[2].setSelected(false);
		}
		if(this.botonesSeleccion[2]==e.getSource()) {
			this.botonesSeleccion[0].setSelected(false);
			this.botonesSeleccion[1].setSelected(false);
		}
		if (e.getSource() == this.cancelar) {
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
			if(this.botonesSeleccion[0].isSelected()) {
				oov.setAnteriorVE(0);
				this.gOpciones.setOpcion(oov, 1);
				this.dialogo.setVisible(false);
				if (Conf.fichero_log) {
					Logger.log_write("Ninguna vista específica");
				}
				this.preprocesador.fase2(this.clase);
			}else if(this.botonesSeleccion[1].isSelected()) {
				if (Conf.fichero_log) {
					Logger.log_write("¿Habilitar vistas para DYV? Sí");
				}
				oov.setAnteriorVE(1);
				this.gOpciones.setOpcion(oov, 1);
				new CuadroSeleccionMetodos(
						this.clase, this.ventana, this.preprocesador);
				this.dialogo.setVisible(false);
			}else if(this.botonesSeleccion[2].isSelected()) {
				if (Conf.fichero_log) {
					Logger.log_write("Habilitar vistas basadas en arboles");
				}
				oov.setAnteriorVE(2);
				this.gOpciones.setOpcion(oov, 1); // Probando si hay que ponerlo
				new CuadroSeleccionMetodosVE(
						this.clase, this.ventana, this.preprocesador);
				this.dialogo.setVisible(false);
			}
		}else if (code == KeyEvent.VK_ESCAPE) {
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
	/*
	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == this.aceptar) {
			if(this.botonesSeleccion[0].isSelected()) {
				oov.setAnteriorVE(0);
				this.gOpciones.setOpcion(oov, 1);
				this.dialogo.setVisible(false);
				if (Conf.fichero_log) {
					Logger.log_write("Ninguna vista específica");
				}
				this.preprocesador.fase2(this.clase);
			}else if(this.botonesSeleccion[1].isSelected()) {
				if (Conf.fichero_log) {
					Logger.log_write("¿Habilitar vistas para DYV? Sí");
				}
				oov.setAnteriorVE(1);
				this.gOpciones.setOpcion(oov, 1);

				this.dialogo.setVisible(false);
				new CuadroSeleccionMetodos(this.clase, this.ventana, 
						this.preprocesador);
			}else if(this.botonesSeleccion[2].isSelected()) {
				if (Conf.fichero_log) {
					Logger.log_write("Habilitar vistas basadas en arboles");
				}
				oov.setAnteriorVE(2);
				this.gOpciones.setOpcion(oov, 1); // Probando si hay que ponerlo
				this.dialogo.setVisible(false);
				new CuadroSeleccionMetodosVE(
						this.clase, this.ventana, this.preprocesador);
			}
		}else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		} 
	}

	
		
	/*
		if (e.getSource() == this.aceptar) {
			recogerMetodosSeleccionados();
		} else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		}
	}*/

//	public void setParametrosMetodo(int i, String paramE, String paramI) {
//		this.estructura[i].setText(paramE);
//		this.indices[i].setText(paramI);
//	}

}