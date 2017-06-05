package paneles;
import org.fife.ui.rtextarea.*;

import ventanas.Ventana;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultHighlighter;

import org.fife.ui.rsyntaxtextarea.*;

/**
 * Clase que representa el editor de c�digo nuevo
 * 
 * @author Daniel Arroyo Cort�s
 *
 */
public class PanelEditorJava2 extends JPanel implements KeyListener{
	
	private static final long serialVersionUID = -5193287286127050841L;
	private static final Color colorErrores = Color.LIGHT_GRAY;
	
	private String texto;
	
	private RSyntaxTextArea textArea;
	
	/**
	 * Construye un nuevo panel editor vac�o.
	 */
	public PanelEditorJava2() {
		
	}
	
	/**
	 * Construye un nuevo panel editor con el contenido especificado.
	 * 
	 * @param texto 
	 * 		C�digo que contendr� el editor.
	 */
	public PanelEditorJava2(String texto) {
		
		this.texto = texto;
		
		this.setLayout(new BorderLayout()); 
		JPanel cp = new JPanel(new BorderLayout());
		
		this.textArea = new RSyntaxTextArea(texto);
		this.textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		this.textArea.setCodeFoldingEnabled(true);
		this.textArea.setMinimumSize(new Dimension(50,50));
		this.textArea.addKeyListener(this);
		
		RTextScrollPane sp = new RTextScrollPane(this.textArea);
		cp.add(sp);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		this.add(cp, BorderLayout.CENTER);
		this.textArea.setCaretPosition(0);	
	}
	
	/**
	 * Devuelve el texto del editor.
	 * 
	 * @return 
	 * 		Texto del editor.
	 */
	public String getText() {
		return this.texto;
	}
	
	/**
	 * Permite seleccionar una porci�n del texto.
	 * 
	 * @param inicio 
	 * 		Posici�n de inicio de la selecci�n.
	 * @param longitud 
	 * 		Tama�o de la selecci�n.
	 */
	public void select(int inicio, int longitud) {
		
		//	Hacemos focus
		
		this.textArea.requestFocusInWindow();
		this.textArea.select(inicio, longitud);
	    
	
	    //	Coloreamos l�nea
	        
		DefaultHighlighter highlighter =  (DefaultHighlighter)this.textArea.getHighlighter();
        DefaultHighlighter.DefaultHighlightPainter painter = 
        		new DefaultHighlighter.DefaultHighlightPainter( colorErrores );
        highlighter.setDrawsLayeredHighlights(false);
        
        try
        {
            highlighter.addHighlight(inicio, inicio+longitud, painter );
        }
        catch(Exception e)
        {
            
        }         
	}
	
	/**
	 * Elimina todas las l�neas subrayadas, para limpiar
	 */
	public void removeSelects(){
		DefaultHighlighter highlighter = (DefaultHighlighter)this.textArea.getHighlighter();
		highlighter.removeAllHighlights();
	}	
	
	/**
	 * Mueve el scroll (focus) del editor de c�digo a la l�nea indicada por
	 * 	numLinea
	 * 
	 * @param numLinea
	 * 		N�mero de l�nea donde queremos hacer scroll o focus
	 * 
	 * @param jsp
	 * 		JScrollPane que moveremos
	 */
	public void focusLinea(int numLinea, JScrollPane jsp){
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
				int numLineasEditor = textArea.getText().split("(\r\n|\r|\n)", -1).length;
				double desplazamiento = (float)numLinea/(float)numLineasEditor;
		
				JScrollBar vertical = jsp.getVerticalScrollBar();
				int numScrollMin = vertical.getMinimum();
				int numScrollMax = vertical.getMaximum();
				int difScroll = numScrollMax-numScrollMin;
				int unidadDesplazamiento = difScroll/numLineasEditor;
				
				double moveScrollTo = (difScroll)*desplazamiento;
				vertical.setValue((int) Math.round(moveScrollTo)-unidadDesplazamiento);
		    }
		});
	}

	/*
	 * SECCION KEY LISTENER
	 */
	
	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.texto = this.textArea.getText();
		Ventana.thisventana.setClasePendienteGuardar(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
