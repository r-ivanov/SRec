package paneles;
import org.fife.ui.rtextarea.*;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	
	/**
	 * Construye un nuevo panel editor con el contenido especificado.
	 * 
	 * @param texto Código que contendrá el editor.
	 * @param editable A true si se permite edición, false en caso contrario.
	 */
	public PanelEditorJava2(String texto/*, boolean editable*/) {
		this.setLayout(new BorderLayout()); 
		JPanel cp = new JPanel(new BorderLayout());
		
		RSyntaxTextArea textArea = new RSyntaxTextArea(texto);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCodeFoldingEnabled(true);
		textArea.setMinimumSize(new Dimension(50,50));
		RTextScrollPane sp = new RTextScrollPane(textArea);
		cp.add(sp);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
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
