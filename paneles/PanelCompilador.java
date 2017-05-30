package paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import utilidades.Texto;
import conf.Conf;

/**
 * Panel que contiene los resultados de compilación de una determinada clase.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class PanelCompilador implements MouseListener {
	static final long serialVersionUID = 04;

	private JTextPane espacioCompilador;

	private JPanel panel = new JPanel();

//	private PanelAlgoritmo pAlgoritmo;

	public static final String CODIGO_VACIO = "      ";

	private final int LONG_MINIMA = CODIGO_VACIO.length() - 1;

	/**
	 * Constructor: Crea un nuevo PanelCompilador.
	 * 
	 * @param pAlgoritmo PanelAlgoritmo que contendrá este panel.
	 */
	public PanelCompilador(PanelAlgoritmo pAlgoritmo) {
//		this.pAlgoritmo = pAlgoritmo;

		this.espacioCompilador = new JTextPane();
		this.espacioCompilador.setText("");
		this.espacioCompilador.setFont(new Font("Courier New", Font.PLAIN, 11));
		this.espacioCompilador.setBackground(Conf.colorPanel);
		this.espacioCompilador.setEditable(false);
		this.espacioCompilador.addMouseListener(this);

		this.panel.setLayout(new BorderLayout());
		this.panel.add(this.espacioCompilador);

		this.borrarTextoCompilador();
		this.panel.updateUI();

	}
	
	/**
	 * Devuelve el panel interno.
	 * 
	 * @return Panel.
	 */
	public JPanel getPanel() {
		return this.panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
//		if (e.getSource() == this.espacioCompilador) {
//			new Thread() {
//				@Override
//				public synchronized void run() {
//					try {
//						this.wait(60);
//					} catch (Exception e) {
//					}
//					;
//					String seleccionado = PanelCompilador.this.espacioCompilador
//							.getSelectedText();
//					if (seleccionado != null) {
//						int numeroLinea = PanelCompilador.this
//								.numLinea(seleccionado);
//
//						int nLineas = numeroLinea;
//
//						if (numeroLinea != -1) {
//							int longitud = 0;
//							String textoareatexto = PanelCompilador.this.pAlgoritmo
//									.getPanelCodigo().getText();
//
//							while (numeroLinea > 1) // !=0
//							{
//								longitud = longitud
//										+ (textoareatexto.substring(0,
//												textoareatexto.indexOf("\n"))
//												.length() + 1);
//								textoareatexto = textoareatexto.substring(
//										textoareatexto.indexOf("\n") + 1,
//										textoareatexto.length());
//								numeroLinea--;
//							}
//							int inicio = longitud;
//
//							try {
//								longitud = (textoareatexto.substring(0,
//										textoareatexto.indexOf("\n")).length());
//							} catch (Exception e) {
//								longitud = textoareatexto.length();
//							}
//							
//							PanelCompilador.this.pAlgoritmo.getPanelCodigo()
//									.select(inicio - nLineas + 1, longitud);
//						}
//					}
//				}
//			}.start();
//		}
	}
//
//	private int numLinea(String texto) {
//		if ((texto.indexOf("java:")) >= 0
//				&& (texto.indexOf("java:") + 5) < texto.length()) {
//			String t = texto.substring(texto.indexOf("java:") + 5,
//					texto.length());
//
//			if (t.indexOf(":") != -1) {
//				t = t.substring(0, t.indexOf(":"));
//			}
//			int x = -1;
//			try {
//				x = Integer.parseInt(t);
//			} catch (Exception e) {
//				return -1;
//			}
//			return x;
//		}
//		return -1;
//	}
	
	/**
	 * Permite borrar el texto mostrado.
	 */
	public void borrarTextoCompilador() {
		this.setTextoCompilador(CODIGO_VACIO);
	}
	
	/**
	 * Permite establecer el texto mostrado.
	 */
	public void setTextoCompilador(String texto) {

		if (texto.length() < this.LONG_MINIMA) {
			this.espacioCompilador.setBackground(Conf.colorPanel);
			this.espacioCompilador.setText(Texto.get("COMPILAR_NOERRORES",
					Conf.idioma));
		} else if (texto.equals(CODIGO_VACIO)) {
			this.espacioCompilador.setBackground(new Color(255, 255, 255));
			this.espacioCompilador.setText(texto);
		} else {
			this.espacioCompilador.setBackground(Conf.colorPanel);
			this.espacioCompilador.setText(texto);
		}

	}
}
