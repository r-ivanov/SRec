package paneles;
import org.fife.ui.rtextarea.*;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.*;

/**
 * Clase que representa el editor de código
 * 
 * @author Daniel Arroyo Cortés
 *
 */
public class PanelEditorJava2 extends JPanel{
	private static final long serialVersionUID = -5193287286127050841L;
	
	/**
	 * Construye un nuevo panel editor vacío.
	 */
	public PanelEditorJava2() {
		
	}
	
	public PanelEditorJava2(String texto, boolean editable) {
		this.setLayout(new BorderLayout()); 
		JPanel cp = new JPanel(new BorderLayout());
		
		RSyntaxTextArea textArea = new RSyntaxTextArea(texto);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCodeFoldingEnabled(true);
		textArea.setMinimumSize(new Dimension(800,600));
		RTextScrollPane sp = new RTextScrollPane(textArea);
		cp.add(sp);
		this.add(cp, BorderLayout.CENTER);

	      
	}
	
	//	TODO Falta implementar
	public String getText() {
		return "Dani";
	}
	
	//	TODO Falta implementar
	public void select(int inicio, int longitud) {
		
	}
}
