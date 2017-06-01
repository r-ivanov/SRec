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
public class PanelCompilador {
	static final long serialVersionUID = 04;

	private JTextPane espacioCompilador;

	private JPanel panel = new JPanel();

	public static final String CODIGO_VACIO = "      ";

	private final int LONG_MINIMA = CODIGO_VACIO.length() - 1;

	/**
	 * Constructor: Crea un nuevo PanelCompilador.
	 * 
	 * @param pAlgoritmo PanelAlgoritmo que contendrá este panel.
	 */
	public PanelCompilador() {
		
		this.espacioCompilador = new JTextPane();
		this.espacioCompilador.setText("");
		this.espacioCompilador.setFont(new Font("Courier New", Font.PLAIN, 11));
		this.espacioCompilador.setBackground(Conf.colorPanel);
		this.espacioCompilador.setEditable(false);

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
