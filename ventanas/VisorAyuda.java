package ventanas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import utilidades.Texto;
import botones.BotonImagen;
import conf.Conf;

/**
 * Ventana de ayuda de la aplicación.
 */
public class VisorAyuda extends Thread implements ActionListener,
		MouseListener, KeyListener, HyperlinkListener {

	private JFrame frame;
	private JEditorPane pane;

	private String fichero;
	private String index;

	private BotonImagen atras;
	private BotonImagen adelante;
	private BotonImagen indice;

	private String pilaAt[] = new String[0]; // Pila para ir hacia atrás
	private String pilaAd[] = new String[0]; // Pila para ir hacia adelante

	private final int anchoCuadro = 750;
	private final int altoCuadro = 570;
	
	/**
	 * Construye un nuevo Visor de ayuda cargando el fichero html principal.
	 */
	public VisorAyuda() {
		this("index.html");
	}
	
	/**
	 * Construye un nuevo Visor de ayuda cargando el fichero pasado por parámetro.
	 * 
	 * @param s Fichero a visualizar
	 */
	public VisorAyuda(String s) {
		this.fichero = s;
		this.start();
	}

	@Override
	public void run() {
		this.frame = new JFrame(Texto.get("VA_AYUDA", Conf.idioma));

		// Botones
		this.atras = new BotonImagen("imagenes/boton_atrasayuda_verde.gif",
				"imagenes/boton_atrasayuda_naranja.gif",
				"imagenes/boton_atrasayuda_rojo.gif",
				Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);
		this.adelante = new BotonImagen(
				"imagenes/boton_adelanteayuda_verde.gif",
				"imagenes/boton_adelanteayuda_naranja.gif",
				"imagenes/boton_adelanteayuda_rojo.gif",
				Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);
		this.indice = new BotonImagen("imagenes/boton_indice_verde.gif",
				"imagenes/boton_indice_naranja.gif",
				"imagenes/boton_indice_rojo.gif", Conf.botonVisualizacionAncho,
				Conf.botonVisualizacionAlto);

		this.atras.addMouseListener(this);
		this.adelante.addMouseListener(this);
		this.indice.addMouseListener(this);

		this.atras.setToolTipText(Texto.get("VA_ATRAS", Conf.idioma));
		this.adelante.setToolTipText(Texto.get("VA_ADEL", Conf.idioma));
		this.indice.setToolTipText(Texto.get("VA_INDICE", Conf.idioma));

		JPanel panelSuperior = new JPanel();
		BorderLayout bl = new BorderLayout();
		panelSuperior.setLayout(bl);
		JPanel panelBotones = new JPanel();

		panelBotones.add(this.atras);
		panelBotones.add(this.adelante);
		panelBotones.add(this.indice);
		panelSuperior.add(panelBotones, BorderLayout.WEST);
		this.frame.getContentPane().add(panelSuperior, "North");

		File f = new File("");
		this.fichero = "file:///" + f.getAbsolutePath() + "\\ayuda\\"
				+ this.fichero;
		this.index = "file:///" + f.getAbsolutePath() + "\\ayuda\\"
				+ Conf.idioma + "_index.html";

		this.pane = new JEditorPane();
		this.pane.setEditable(false);
		this.frame.getContentPane().add(new JScrollPane(this.pane), "Center");
		try {
			this.pane.setPage(this.index);
		} catch (IOException e) {
			try {
				this.pane.setPage("file:///" + f.getAbsolutePath()
						+ "\\ayuda\\" + "error.html");
			} catch (IOException e2) {
				System.out.println("VisorAyuda fichero no encontrado");
			}
		}

		this.adelante.deshabilitar();
		this.atras.deshabilitar();

		this.frame.setIconImage(new ImageIcon("./imagenes/icono_ayuda.gif")
				.getImage());
		this.frame.setVisible(true);
		this.frame.setResizable(false);
		int coord[] = Conf.ubicarCentro(this.anchoCuadro, this.altoCuadro);
		this.frame.setLocation(coord[0], coord[1]);
		this.frame.setSize(this.anchoCuadro, this.altoCuadro);
		this.pane.addHyperlinkListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.colorearBotonesAdelanteAtras();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() instanceof BotonImagen) {
			((BotonImagen) (e.getSource())).mostrarPulsado();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == this.indice) {
			this.indice.habilitar();
		}
		this.colorearBotonesAdelanteAtras();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == this.indice) {
			// Reinicializamos cuál debe ser el index, por si ha cambiado idioma
			File f = new File("");
			this.index = "file:///" + f.getAbsolutePath() + "\\ayuda\\"
					+ Conf.idioma + "_index.html";
			// Fin reinicializacion

			if (!this.fichero.equals(this.index)) {
				this.anadirAt(this.fichero);
			}
			try {
				this.pane.setPage(this.index);
			} catch (IOException ioe) {
				try {
					this.pane.setPage("file:///" + f.getAbsolutePath()
							+ "\\ayuda\\" + "error.html");
				} catch (IOException e2) {
					System.out.println("VisorAyuda fichero no encontrado");
				}
			}
			this.fichero = this.index;

			this.borrarAd();
		} else if (e.getSource() == this.atras) {
			this.anadirAd(this.fichero);
			this.fichero = this.extraerAt();
			try {
				this.pane.setPage(this.fichero);
			} catch (IOException ioe) {
			}
		} else if (e.getSource() == this.adelante) {
			this.anadirAt(this.fichero);
			this.fichero = this.extraerAd();
			try {
				this.pane.setPage(this.fichero);
			} catch (IOException ioe) {
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
		HyperlinkEvent.EventType type = hyperlinkEvent.getEventType();
		final URL url = hyperlinkEvent.getURL();
		if (type == HyperlinkEvent.EventType.ENTERED) {
			// No hacer nada
		} else if (type == HyperlinkEvent.EventType.ACTIVATED) {
			this.anadirAt(this.fichero);
			this.fichero = url.toExternalForm();
			Runnable runner = new Runnable() {
				@Override
				public void run() {
					// Retain reference to original
					try {
						VisorAyuda.this.pane.setPage(url);
					} catch (IOException ioException) {
						try {
							File f = new File("");
							VisorAyuda.this.pane.setPage("file:///"
									+ f.getAbsolutePath() + "\\ayuda\\"
									+ "error.html");
						} catch (IOException ioException2) {

						}
					}
				}
			};
			SwingUtilities.invokeLater(runner);

			this.borrarAd();
			this.colorearBotonesAdelanteAtras();
		}
	}
	
	/**
	 * Elimina los elementos disponibles en la pila de páginas posteriores.
	 */
	private void borrarAd() {
		this.pilaAd = new String[0];
	}
	
	/**
	 * Añade un elemento en la pila de páginas anteriores.
	 */
	private void anadirAt(String f) {
		if (this.pilaAt.length == 0 || !(f.equals(this.pilaAt[0]))) {
			String pilaAux[] = new String[this.pilaAt.length + 1];
			pilaAux[0] = f;
			for (int i = 0; i < this.pilaAt.length; i++) {
				pilaAux[i + 1] = this.pilaAt[i];
			}
			this.pilaAt = pilaAux;
		}
	}
	
	/**
	 * Añade un elemento en la pila de páginas posteriores.
	 */
	private void anadirAd(String f) {
		if (this.pilaAd.length == 0 || !(f.equals(this.pilaAd[0]))) {
			String pilaAux[] = new String[this.pilaAd.length + 1];
			pilaAux[0] = f;
			for (int i = 0; i < this.pilaAd.length; i++) {
				pilaAux[i + 1] = this.pilaAd[i];
			}
			this.pilaAd = pilaAux;
		}
	}
	
	/**
	 * Extrae un elemento de la pila de páginas anteriores.
	 * 
	 * @return Página anterior
	 */
	private String extraerAt() {
		if (this.pilaAt.length > 0) {
			String pos0 = this.pilaAt[0];
			String pilaAux[] = new String[this.pilaAt.length - 1];
			for (int i = 0; i < pilaAux.length; i++) {
				pilaAux[i] = this.pilaAt[i + 1];
			}
			this.pilaAt = pilaAux;
			if (!(pos0.equals(this.fichero))) {
				return pos0;
			} else {
				return this.extraerAt();
			}
		} else {
			return this.fichero;
		}
	}
	
	/**
	 * Extrae un elemento de la pila de páginas posteriores.
	 * 
	 * @return Página anterior
	 */
	private String extraerAd() {
		if (this.pilaAd.length > 0) {
			String pos0 = this.pilaAd[0];
			String pilaAux[] = new String[this.pilaAd.length - 1];
			for (int i = 0; i < pilaAux.length; i++) {
				pilaAux[i] = this.pilaAd[i + 1];
			}
			this.pilaAd = pilaAux;
			if (!(pos0.equals(this.fichero))) {
				return pos0;
			} else {
				return this.extraerAd();
			}
		} else {
			return this.fichero;
		}
	}
	
	/**
	 * Habilita o deshabilita los botones de adelante y atrás según el estado actual.
	 */
	private void colorearBotonesAdelanteAtras() {
		if (this.pilaAt.length > 0) {
			this.atras.habilitar();
		} else {
			this.atras.deshabilitar();
		}

		if (this.pilaAd.length > 0) {
			this.adelante.habilitar();
		} else {
			this.adelante.deshabilitar();
		}
	}
}
