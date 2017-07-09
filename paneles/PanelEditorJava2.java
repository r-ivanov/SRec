package paneles;
import org.fife.ui.rtextarea.*;

import opciones.GestorOpciones;
import opciones.OpcionConfVisualizacion;
import ventanas.Ventana;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

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
	
	private Color colorErrores;
	
	private String texto;
	
	private RSyntaxTextArea textArea;
	
	private OpcionConfVisualizacion ocv = null;
	
	private GestorOpciones gOpciones = new GestorOpciones();
	
	private List<Integer> lineasSubrayadas = new ArrayList<Integer>();
	
	private static final int numeroTemas = 7;
	
	private String[] listaTemas = new String[numeroTemas];
	
	private final UndoManager undo = new UndoManager();
	
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
		
		this.ocv = (OpcionConfVisualizacion) this.gOpciones.getOpcion(
				"OpcionConfVisualizacion", false);
		
		this.texto = texto.replaceAll("\r","");
		
		//	Editor
		
		this.setLayout(new BorderLayout()); 
		JPanel cp = new JPanel(new BorderLayout());
		
		this.textArea = new RSyntaxTextArea(this.texto);
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
	    
	    //	Temas
	    
	    listaTemas[0] = "default.xml";
	    listaTemas[1] = "dark.xml";
	    listaTemas[2] = "eclipse.xml";
	    listaTemas[3] = "idea.xml";
	    listaTemas[4] = "monokai.xml";
	    listaTemas[5] = "vs.xml";	    
	    
	    this.changeTheme(this.ocv.getTemaColorEditor());
	    
	    //	Opciones fichero
	    
	    int[] colorErroresArray = this.ocv.getColorErroresCodigo();
	    
	    this.colorErrores = new Color(colorErroresArray[0],colorErroresArray[1],colorErroresArray[2]); 
	    
	    //	Undo-redo manager
	    
	    this.inicializateUndoRedoManager();	    
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
	 * 
	 * @param longitud 
	 * 		Tamaño de la selección.
	 * 
	 * @param esInterno
	 * 		Indica si el subrayado lo provoca el usuario (false) 
	 * 		o lo hacemos nosotros internamente
	 */
	public void select(int inicio, int longitud, boolean esInterno) {
		
		//	Hacemos focus
		
		this.textArea.requestFocusInWindow();
		this.textArea.select(inicio, longitud);
	    
	
	    //	Coloreamos línea
	        
		DefaultHighlighter highlighter =  (DefaultHighlighter)this.textArea.getHighlighter();
        DefaultHighlighter.DefaultHighlightPainter painter = 
        		new DefaultHighlighter.DefaultHighlightPainter(colorErrores);
        highlighter.setDrawsLayeredHighlights(false);
        
        try
        {
            highlighter.addHighlight(inicio, inicio+longitud, painter);
            if(!esInterno){
	            this.lineasSubrayadas.add(inicio);
	            this.lineasSubrayadas.add(longitud);
            }
        }
        catch(Exception e)
        {
            
        }         
	}
	
	/**
	 * Redibuja las líneas sobrayadas del editor, por si cambian el color
	 */
	public void redibujarLineasErrores(){
		
		//	Opciones fichero
	    this.ocv = (OpcionConfVisualizacion) this.gOpciones.getOpcion(
				"OpcionConfVisualizacion", false);
	    
	    int[] colorErroresArray = this.ocv.getColorErroresCodigo();
	    
	    this.colorErrores = new Color(colorErroresArray[0],colorErroresArray[1],colorErroresArray[2]);
	    
		int i = 0;
		while(i<this.lineasSubrayadas.size()){
			this.select(this.lineasSubrayadas.get(i), this.lineasSubrayadas.get(i+1), true);
			i = i+2;
		}
		
	}
	
	/**
	 * Elimina todas las líneas subrayadas, para limpiar
	 */
	public void removeSelects(){
		DefaultHighlighter highlighter = (DefaultHighlighter)this.textArea.getHighlighter();
		highlighter.removeAllHighlights();
		
		this.lineasSubrayadas = new ArrayList<Integer>();
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
				int numLineaFinal = numLinea;
				if(numLinea<0 || numLinea>numLineasEditor)
					numLineaFinal = numLineasEditor;
				
				double desplazamiento = (float)numLineaFinal/(float)numLineasEditor;
		
				JScrollBar vertical = jsp.getVerticalScrollBar();
				int numScrollMin = vertical.getMinimum();
				int numScrollMax = vertical.getMaximum();
				int difScroll = numScrollMax-numScrollMin;
				int unidadDesplazamiento = difScroll/numLineasEditor;
				
				double moveScrollTo = (difScroll)*desplazamiento;
				vertical.setValue((int) Math.round(moveScrollTo)-unidadDesplazamiento);	
				
				PanelEditorJava2.this.moverLineaEditor(numLineaFinal);
		    }
		});
	}
	
	/**
	 * Mueve el cursor/ratón a la línea especificada
	 * 
	 * @param numeroLinea
	 * 
	 * 		Número de línea donde mover el cursor
	 */
	private void moverLineaEditor(int numeroLinea){
		
		int longitud = 0;
		String textoareatexto = this.getText();
		
		if(textoareatexto.equals(""))
			return;
		
		while (numeroLinea > 1) // !=0
		{
			longitud = longitud
					+ (textoareatexto.substring(0,
							textoareatexto.indexOf("\n"))
							.length() + 1);
			textoareatexto = textoareatexto.substring(
					textoareatexto.indexOf("\n") + 1,
					textoareatexto.length());
			numeroLinea--;
		}
		
		int inicio = longitud;

		try {
			longitud = (textoareatexto.substring(0,
					textoareatexto.indexOf("\n")).length());
		} catch (Exception e) {
			longitud = textoareatexto.length();
		}
		
		textArea.requestFocus();
		this.textArea.setCaretPosition(inicio - numeroLinea + 1);
	}

	/**
	 * Provee las palabras y las expresiones que se autocompletarán
	 * 
	 * @return
	 * 		Objeto de la clase CompletionProvider
	 */
	private CompletionProvider createCompletionProvider() {

		String ficheroOrigen = 	"/org/fife/ui/rsyntaxtextarea/modes/JavaTokenMaker.flex";
				
		String ficheroExcluir =	"/datos/editorPalabrasEliminarAutocompletar.txt";
		
		String ficheroExtras =	"/datos/editorPalabrasAnadirExtras.txt";
		
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
	
	/**
	 * Establece el tema visual del editor
	 * 
	 * @param temaColorEditor
	 * 
	 * 		Número del 0 al 6, temas disponibles:
	 * 
	 * 			0: "default.xml";
	 *
	 *			1: "dark.xml";
	 *		
	 *			2: "eclipse.xml";
	 *			
	 * 			3: "idea.xml";
	 *		
	 *			4: "monokai.xml";
	 * 					
	 *			5: "vs.xml";
	 *	
	 *			otro número: "default.xml";
	 * 
	 */
	public void changeTheme(final int temaColorEditor){
		SwingUtilities.invokeLater(new Runnable() { 
	        public void run() { 
	        	String ruta = "";
		
			if(temaColorEditor>0 && temaColorEditor<PanelEditorJava2.this.listaTemas.length)
				ruta = PanelEditorJava2.this.listaTemas[temaColorEditor];
			else
				ruta = PanelEditorJava2.this.listaTemas[0];		
			
	        ruta = "/org/fife/ui/rsyntaxtextarea/themes/" + ruta;
	        InputStream is1 = PanelEditorJava2.class.getResourceAsStream(ruta);
	        		
			try {
		         Theme theme = Theme.load(is1);
		         theme.apply(PanelEditorJava2.this.textArea);
		      } catch (IOException ioe) {
		         ioe.printStackTrace();
		      }
	        }
		});
		
	}
	
	/**
	 * 	Inicializa el undo-redo manager
	 */
	private void inicializateUndoRedoManager() {
		
	    Document doc = this.textArea.getDocument();
	    
	    doc.addUndoableEditListener(new UndoableEditListener() { 
	    	public void undoableEditHappened(UndoableEditEvent evt) { undo.addEdit(evt.getEdit()); } 
	    });
	    
	    this.textArea.getActionMap().put("Undo", new AbstractAction("Undo") {
			private static final long serialVersionUID = -5838527237464903916L;

			public void actionPerformed(ActionEvent evt) {
	    		try {
	    			if (undo.canUndo()) { 
	    				undo.undo(); 
	    			} 
	    		} catch (CannotUndoException e) { 
	    			
	    		} 
	    	} 
	    });
	    
	    this.textArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
	    
	    this.textArea.getActionMap().put("Redo", new AbstractAction("Redo") {
			private static final long serialVersionUID = 228170695013853021L;

			public void actionPerformed(ActionEvent evt) {
	    		try { 
	    			if (undo.canRedo()) {
	    				undo.redo(); } 
	    			}
	    		catch (CannotRedoException e) { 
	    			
	    		} 
	    	} 
	    });
	    
	    this.textArea.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
	}
	
	/**
	 * Operación deshacer
	 */
	public void doUndo() {
		try {
			if (undo.canUndo()) { 
				undo.undo(); 
				Ventana.thisventana.setClasePendienteGuardar(true);
				this.texto = this.textArea.getText();
			} 
		} catch (CannotUndoException e) { 
			
		}
	}
	
	/**
	 * Operación rehacer
	 */
	public void doRedo() {
		try { 
			if (undo.canRedo()) {
				undo.redo(); 
				Ventana.thisventana.setClasePendienteGuardar(true);
				this.texto = this.textArea.getText();
			} 
		}catch (CannotRedoException e) { 
			
		} 
	}
	
	/**
	 * Operación copiar
	 */
	public void doCopy() {
		String anterior = this.textArea.getText();
		this.textArea.copy();
		if(!anterior.equals(this.textArea.getText())) {
			Ventana.thisventana.setClasePendienteGuardar(true);
			this.texto = this.textArea.getText();
		}
	}
	
	/**
	 * Operación pegar
	 */
	public void doPaste() {		
		String anterior = this.textArea.getText();
		this.textArea.paste();
		if(!anterior.equals(this.textArea.getText())) {
			Ventana.thisventana.setClasePendienteGuardar(true);
			this.texto = this.textArea.getText();
		}
	}
	
	/**
	 * Operación cortar
	 */
	public void doCut() {
		String anterior = this.textArea.getText();
		this.textArea.cut();
		if(!anterior.equals(this.textArea.getText())) {
			Ventana.thisventana.setClasePendienteGuardar(true);
			this.texto = this.textArea.getText();
		}
	}
	
	/**
	 * Operación seleccionar todo
	 */
	public void doSelectAll() {
		this.textArea.requestFocus();
		this.textArea.selectAll();
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
