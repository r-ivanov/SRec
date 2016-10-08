package cuadros;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import conf.*;
import javafx.scene.control.RadioButton;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

import utilidades.BuscadorMVJava;

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
	private JPanel panel, panelBoton, panelpanelElementos, panelElementos, panelRadioButtons;	

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
		this.campoDireccion.setMinimumSize(new Dimension(ANCHO_CUADRO/2, 20));		
		this.campoDireccion.getDocument().addDocumentListener(this.getDocumentListener());
		
		this.examinar = new BotonTexto(Texto.get("BOTONEXAMINAR", Conf.idioma));		
		// examinar.addActionListener(this);
		this.examinar.addKeyListener(this);
		this.examinar.addMouseListener(this);
		this.examinar.setToolTipText(Texto.get("COMVJ_ESCRDIRVALTTT",
				Conf.idioma));

		// Panel de elementos
		this.panelElementos = new JPanel();
		this.panelElementos.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		this.panelElementos.add(this.campoDireccion,c);
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 0;
		this.panelElementos.add(this.examinar,c);
		this.panelElementos.setPreferredSize(new Dimension(ANCHO_CUADRO,ALTO_CUADRO/4));
		
		// Panel de panel de Elementos
		this.panelpanelElementos = new JPanel();
		this.panelpanelElementos.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		this.panelpanelElementos.setBorder(new TitledBorder(
				this.textoIndicativo));
		this.panelpanelElementos.add(this.panelElementos,c);
		this.panelpanelElementos.setPreferredSize(panelpanelElementos.getPreferredSize());
		
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
		this.panelBoton.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		this.panelBoton.add(this.aceptar,c);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		this.panelBoton.add(this.cancelar,c);
		this.panelBoton.setBorder(new EmptyBorder(0, 0, 5, 0));
		
		// Panel general
		this.panel = new JPanel();
		this.panel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		this.panel.add(this.panelpanelElementos, c);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		this.panel.add(this.panelBoton, c);
		this.panel.setPreferredSize(panel.getPreferredSize());		
		this.dialogo.getContentPane().add(this.panel);			
		
		// Preparamos y mostramos cuadro
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);
		this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setResizable(false);
		this.dialogo.setTitle(Texto.get("COMVJ_SELECTITULO", Conf.idioma));
		this.dialogo.validate();
		this.dialogo.pack();		
		this.dialogo.setVisible(true);
		this.dialogo.setAlwaysOnTop(true);
	}

	/**
	 * Comprueba que la dirección seleccionada es correcta
	 * 
	 * @param aceptarPulsado
	 *            true si el usuario pulsó el botón Aceptar false si el método
	 *            es ejecutado por la propia aplicación de manera interna 
	 *            o mediante el botón examinar o mediante la inserción / modificación de texto
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
			if ((this.aceptar.isFocusOwner()
					|| this.campoDireccion.isFocusOwner()) && this.aceptar.isEnabled()) {
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
		if (this.aceptar.isEnabled() && e.getSource() == this.aceptar) {			
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
	
	/**
	 * Creates a document listener for Text elements
	 * @return The document listener
	 */
	private DocumentListener getDocumentListener(){
		return new DocumentListener() {
			
		      public void changedUpdate(DocumentEvent documentEvent) {
		    	  valorarSeleccion(false);	    	  
		      }
		      
		      public void insertUpdate(DocumentEvent documentEvent) {
		    	  valorarSeleccion(false);	    	  
		      }
		      
		      public void removeUpdate(DocumentEvent documentEvent) {
		    	  valorarSeleccion(false);	    	  
		      }
		      
		};		
	}
}
