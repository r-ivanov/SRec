package paneles;
import org.fife.ui.rtextarea.*;

import utilidades.SsooValidator;
import ventanas.Ventana;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultHighlighter;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.fife.ui.rsyntaxtextarea.*;

/**
 * Clase que representa el editor de código nuevo
 * 
 * @author Daniel Arroyo Cortés
 *
 */
public class PanelEditorJava2 extends JPanel implements KeyListener{
	
	private static final long serialVersionUID = -5193287286127050841L;
	private static final Color colorErrores = Color.LIGHT_GRAY;
	
	private String texto;
	
	private RSyntaxTextArea textArea;
	
	/**
	 * Construye un nuevo panel editor vacío.
	 */
	public PanelEditorJava2() {
		
	}
	
	/**
	 * Construye un nuevo panel editor con el contenido especificado.
	 * 
	 * @param texto 
	 * 		Código que contendrá el editor.
	 */
	public PanelEditorJava2(String texto) {
		
		//	Editor
		
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
		
		//	Autocompletar
		
	    CompletionProvider provider = createCompletionProvider();	
	    
	    AutoCompletion ac = new AutoCompletion(provider);
	    ac.install(textArea);

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
	 * Permite seleccionar una porción del texto.
	 * 
	 * @param inicio 
	 * 		Posición de inicio de la selección.
	 * @param longitud 
	 * 		Tamaño de la selección.
	 */
	public void select(int inicio, int longitud) {
		
		//	Hacemos focus
		
		this.textArea.requestFocusInWindow();
		this.textArea.select(inicio, longitud);
	    
	
	    //	Coloreamos línea
	        
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
	 * Elimina todas las líneas subrayadas, para limpiar
	 */
	public void removeSelects(){
		DefaultHighlighter highlighter = (DefaultHighlighter)this.textArea.getHighlighter();
		highlighter.removeAllHighlights();
	}	
	
	/**
	 * Mueve el scroll (focus) del editor de código a la línea indicada por
	 * 	numLinea
	 * 
	 * @param numLinea
	 * 		Número de línea donde queremos hacer scroll o focus
	 * 
	 * @param jsp
	 * 		JScrollPane que moveremos
	 */
	public void focusLinea(final int numLinea, final JScrollPane jsp){
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

	/**
	 * Provee las palabras y las expresiones que se autocompletarán
	 * 
	 * @return
	 * 		Objeto de la clase CompletionProvider
	 */
	private CompletionProvider createCompletionProvider() {

		String ficheroOrigen = 
				(!SsooValidator.isUnix()) ?
					"org\\fife\\ui\\rsyntaxtextarea\\modes\\JavaTokenMaker.flex"
				:
					"/org/fife/ui/rsyntaxtextarea/modes/JavaTokenMaker.flex";
				
		String ficheroExcluir =
				(!SsooValidator.isUnix()) ?
					"datos\\editorPalabrasEliminarAutocompletar.txt"
				:
					"/datos/editorPalabrasEliminarAutocompletar.txt";
		
		String ficheroExtras =
				(!SsooValidator.isUnix()) ?
					"datos\\editorPalabrasAnadirExtras.txt"
				:
					"/datos/editorPalabrasAnadirExtras.txt";
		
		Set<String> wordsToInclude = 
				editorObtenerAutorrellenar(ficheroOrigen, ficheroExcluir, ficheroExtras);
		
		return this.fillCompletionProvider(wordsToInclude);

	}
	
	/**
	 * Método intermedio que rellena el CompletionProvider con palabras y atajos de autocompletar
	 * 
	 * @param words
	 * 		Palabras con las que rellenaremos el CompletionProvider
	 * 
	 * @return
	 * 		CompletionProvider completo
	 */
	private CompletionProvider fillCompletionProvider(Set<String> words){
		
		DefaultCompletionProvider p = new DefaultCompletionProvider();
		
		for(String s : words){
			p.addCompletion(new BasicCompletion(p, s));
		}
		
		p.addCompletion(new ShorthandCompletion(p, "syso",
				"System.out.println(", "System.out.println("));		
		
		p.addCompletion(new ShorthandCompletion(p, "syse",
				"System.err.println(", "System.err.println("));
		
		return p;
	}
	
	/**
	 * Devuelve un Tree Set de las palabras que se utilizarán para autocompletar en el editor
	 * 
	 * @param ficheroOrigen
	 * 		Fichero que contiene las palabras del léxico de Java. Debe de apuntar a este mismo proyecto,
	 * 		fichero .flex JAVA de la librería rsyntaxtextarea
	 * 
	 * 		Actual: srec\\org\\fife\\ui\\rsyntaxtextarea\\modes\\JavaTokenMaker.flex
	 * 
	 * @param ficheroExcluir
	 * 		Fichero que contiene las palabras a excluir separadas por saltos de línea, para limpiar
	 * 		la lista de "ficheroOrigen"
	 * 
	 * 		Actual: srec\\datos\\editorPalabrasEliminarAutocompletar.txt
	 * 
	 * @param ficheroExtras
	 * 		Fichero con palabras extras que queremos añadir al autocompletar, separadas por 
	 * 		saltos de línea
	 * 
	 * 		Actual: srec\\datos\\editorPalabrasAnadirExtras.txt
	 * 
	 * @return
	 * 		Set ordenado alfabéticamente con las palabras que utilizaremos para autocompletar
	 */
	private static Set<String> editorObtenerAutorrellenar(String ficheroOrigen, String ficheroExcluir, String ficheroExtras){
		try{
			InputStream is1 = PanelEditorJava2.class.getResourceAsStream(ficheroOrigen);
			InputStream is2 = PanelEditorJava2.class.getResourceAsStream(ficheroExcluir);
			InputStream is3 = PanelEditorJava2.class.getResourceAsStream(ficheroExtras);
			
		    InputStreamReader fr1 = new InputStreamReader(is1);
		    InputStreamReader fr2 = new InputStreamReader(is2);
		    InputStreamReader fr3 = new InputStreamReader(is3);
		    
		    BufferedReader r1 = new BufferedReader(fr1);
		    BufferedReader r2 = new BufferedReader(fr2);
		    BufferedReader r3 = new BufferedReader(fr3);
		    
			Pattern patt = Pattern.compile("\"([^\"]*)\"");
			
		    

		    Set<String> linesToExclude = new HashSet<String>();
		    Set<String> autoCompleteWords = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		    String line;
		    
		    while ((line = r2.readLine()) != null) {	    	
		    	linesToExclude.add(line);		      
			}    
		    
		    while ((line = r1.readLine()) != null) {
		    	
		      Matcher m = patt.matcher(line);
		      
		      while (m.find()) {
		        String s = m.group(0);
		        s = s.substring(1, s.length()-1);		        
		        if(!linesToExclude.contains(s) && !(s.equals(""))){
		        	autoCompleteWords.add(s);		        	
		        }
		      }		      
		    }
		    
		    while((line = r3.readLine()) != null){
		    	autoCompleteWords.add(line);
		    }
		    
		    r1.close();
		    r2.close();		    
		    
		    return autoCompleteWords;
		    
		}catch(Exception e){
			e.printStackTrace();
			return new HashSet<String>();
		}
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
