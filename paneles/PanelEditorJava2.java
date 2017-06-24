package paneles;
import org.fife.ui.rtextarea.*;

import opciones.GestorOpciones;
import opciones.OpcionConfVisualizacion;
import ventanas.Ventana;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
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
 * Clase que representa el editor de c�digo nuevo
 * 
 * @author Daniel Arroyo Cort�s
 *
 */
public class PanelEditorJava2 extends JPanel implements KeyListener{
	
	private static final long serialVersionUID = -5193287286127050841L;
	private Color colorErrores;
	
	private String texto;
	
	private RSyntaxTextArea textArea;
	
	private OpcionConfVisualizacion ocv = null;
	
	private GestorOpciones gOpciones = new GestorOpciones();
	
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
	    
	    //	Temas
	    this.changeTheme(0,textArea);
	    
	    //	Opciones fichero
	    this.ocv = (OpcionConfVisualizacion) this.gOpciones.getOpcion(
				"OpcionConfVisualizacion", false);
	    
	    int[] colorErroresArray = this.ocv.getColorErroresCodigo();
	    
	    this.colorErrores = new Color(colorErroresArray[0],colorErroresArray[1],colorErroresArray[2]);
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
        		new DefaultHighlighter.DefaultHighlightPainter(colorErrores);
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
	 * Provee las palabras y las expresiones que se autocompletar�n
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
	 * M�todo intermedio que rellena el CompletionProvider con palabras y atajos de autocompletar
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
	 * Devuelve un Tree Set de las palabras que se utilizar�n para autocompletar en el editor
	 * 
	 * @param ficheroOrigen
	 * 		Fichero que contiene las palabras del l�xico de Java. Debe de apuntar a este mismo proyecto,
	 * 		fichero .flex JAVA de la librer�a rsyntaxtextarea
	 * 
	 * 		Actual: srec\\org\\fife\\ui\\rsyntaxtextarea\\modes\\JavaTokenMaker.flex
	 * 
	 * @param ficheroExcluir
	 * 		Fichero que contiene las palabras a excluir separadas por saltos de l�nea, para limpiar
	 * 		la lista de "ficheroOrigen"
	 * 
	 * 		Actual: srec\\datos\\editorPalabrasEliminarAutocompletar.txt
	 * 
	 * @param ficheroExtras
	 * 		Fichero con palabras extras que queremos a�adir al autocompletar, separadas por 
	 * 		saltos de l�nea
	 * 
	 * 		Actual: srec\\datos\\editorPalabrasAnadirExtras.txt
	 * 
	 * @return
	 * 		Set ordenado alfab�ticamente con las palabras que utilizaremos para autocompletar
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
	 * @param tema
	 * 
	 * 		N�mero del 0 al 6, temas disponibles:
	 * 
	 * 			0: "default.xml";
	 *			
	 *			1: "default-alt.xml";
	 *
	 *			2: "dark.xml";
	 *		
	 *			3: "eclipse.xml";
	 *			
	 * 			4: "idea.xml";
	 *		
	 *			5: "monokai.xml";
	 * 					
	 *			6: "vs.xml";
	 *	
	 *			otro n�mero: "default.xml";
	 *			
	 * @param textArea
	 * 
	 * 		RSyntaxTextArea donde aplicaremos el cambio
	 * 
	 */
	private void changeTheme(int tema, RSyntaxTextArea textArea){
		
		String ruta = "";
		
		switch (tema) {
			case 0:
				ruta = "default.xml";
				break;
			case 1:
				ruta = "default-alt.xml";
				break;
			case 2:
				ruta = "dark.xml";
				break;
			case 3:
				ruta = "eclipse.xml";
				break;
			case 4:
				ruta = "idea.xml";
				break;
			case 5:
				ruta = "monokai.xml";
				break;
			case 6:
				ruta = "vs.xml";
				break;
			default:
				ruta = "default.xml";
				break;
		}
		
//		ruta = SsooValidator.isUnix() ? 
//				"/org/fife/ui/rsyntaxtextarea/themes/" + ruta :
//				"\\org\\fife\\ui\\rsyntaxtextarea\\themes\\" + ruta ;	
		
		ruta = "/org/fife/ui/rsyntaxtextarea/themes/" + ruta;
		
		try {
	         Theme theme = Theme.load(getClass().getResourceAsStream(ruta));
	         theme.apply(textArea);
	      } catch (IOException ioe) { // Never happens
	         ioe.printStackTrace();
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
