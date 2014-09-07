package paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

import utilidades.ServiciosString;
import ventanas.Ventana;
import conf.Conf;

/**
 * Representa el panel que permite visualizar el c�digo de la clase cargada y editarlo.
 */
class PanelEditorJava extends JPanel implements DocumentListener, KeyListener {
	private static final long serialVersionUID = -5193287286127050841L;

	private String[] keyWords = new String[] { "abstract", "boolean", "break",
			"byte", "case", "catch", "char", "class", "const", "continue",
			"default", "do", "double", "else", "extends", "false", "final",
			"finally", "float", "for", "goto", "if", "implements", "import",
			"instanceof", "int", "interface", "long", "native", "new", "null",
			"package", "private", "protected", "public", "return", "short",
			"static", "stricfp", "super", "switch", "synchronized", "this",
			"throw", "throws", "transient", "true", "try", "void", "volatile",
			"while" };

	private HashMap<String, String> palabras;
	private HashMap<String, String> metodos;

	private JTextPane areaNum;

	private int longitudTextoInicial = -1;
	private boolean construyendo = false;

	private int numLineasAnterior = -1;

	private DefaultStyledDocument doc;

	private MyDocument md = new MyDocument();

	private JEditorPane edit;
	
	/**
	 * Construye un nuevo panel editor vac�o.
	 */
	public PanelEditorJava() {
		
	}
	
	/**
	 * Construye un nuevo panel editor con el contenido especificado.
	 * 
	 * @param texto C�digo que contendr� el editor.
	 * @param editable A true si se permite edici�n, false en caso contrario.
	 */
	public PanelEditorJava(String texto, boolean editable) {
		super();
		this.edit = new JEditorPane();
		this.edit.setEditorKit(new StyledEditorKit());
		this.edit.setEditable(editable);
		this.edit.setDocument(md);

		this.edit.setBackground(Conf.colorPanel);
		this.edit.setForeground(Conf.colorCodigoRC);

		this.edit.addKeyListener(this);

		md.addDocumentListener(this);

		longitudTextoInicial = texto.length();
		construyendo = true;
		this.edit.setText(texto);

		// �rea de numero de linea
		areaNum = new JTextPane();
		areaNum.setFont(new Font("Courier New", Font.BOLD, 14));
		areaNum.setEditable(false);
		areaNum.setEnabled(false);
		try {
			areaNum.setBackground(new Color(230, 230, 230));
			areaNum.setForeground(new Color(50, 50, 50));
		} catch (Exception e) {
		}
		areaNum.setAlignmentX(Component.RIGHT_ALIGNMENT);
		actualizarBandaNumLinea();

		this.setLayout(new BorderLayout());
		this.add(this.edit, BorderLayout.CENTER);
		this.add(areaNum, BorderLayout.WEST);
		setVisible(true);

		metodos = new HashMap<String, String>();

		palabras = new HashMap<String, String>();
		for (int i = 0; i < keyWords.length; i++) {
			palabras.put(keyWords[i], "1");
		}
	}
	
	/**
	 * Permite establecer el texto que mostrar� el editor.
	 * 
	 * @param texto Texto que mostrar� el editor.
	 */
	public void setText(String texto) {
		longitudTextoInicial = texto.length();
		construyendo = true;
		this.edit.setText(texto);

	}
	
	/**
	 * Permite recoger un comentario o cadena de car�cteres.
	 * 
	 * @param offset Posici�n inicial.
	 * @param sb String de donde se obtendr� el comentario o cadena de car�cteres.
	 * 
	 * @return Comentario o cadena de car�cteres.
	 */
	private String getpalabra(int offset, String sb) {
		String s = "";

		do {
			s = s + sb.charAt(offset);
			offset++;
		} while (offset < sb.length() && !separador(sb.charAt(offset))
				&& !separador(sb.charAt(offset - 1)));

		// Recogemos el comentario entero
		if (s.charAt(0) == '/') {
			if (offset < sb.length()) {
				if (sb.charAt(offset) == '/') // Si comentario de tipo '//'
				{
					do {
						s = s + sb.charAt(offset);
						offset++;
					} while (offset < sb.length() && sb.charAt(offset) != '\r'
							&& sb.charAt(offset) != '\n');
				} else if (sb.charAt(offset) == '*') // Si comentario de tipo
														// '/*'
				{
					int finOffset = sb.indexOf("*/", offset) + 1;

					do {
						s = s + sb.charAt(offset);
						offset++;
					} while (offset <= (finOffset));
				}
			}
		} else if (s.charAt(0) == '"') // Recogemos la cadena de caracteres
		{
			int finOffset = sb.indexOf("\"", offset) + 1;

			while (offset < (finOffset)) {
				s = s + sb.charAt(offset);
				offset++;
			}
			;
		}

		return s;
	}
	
	/**
	 * Permite determinar si el car�cter act�a como separador.
	 * 
	 * @param c Car�cter de entrada.
	 * 
	 * @return True si el car�cter act�a como separador, false en caso contrario.
	 */
	private boolean separador(char c) {
		return c == ' ' || c == ';' || c == '\n' || c == '"' || c == '('
				|| c == ')' || c == '+' || c == '-' || c == '*' || c == '/'
				|| c == '=' || c == '&' || c == '|' || c == '[' || c == ']'
				|| c == '{' || c == '}' || c == ',' || c == '<' || c == '>'
				|| c == '!' || c == '.' || c == '\r' || c == '\t';
	}
	
	/**
	 * Permite analizar el documento y aplicar el formato necesario a cada elemento.
	 * 
	 * @param startOffset Posici�n de inicio.
	 * @param endOffset Posici�n final-
	 * @param analisisCompleto A True si se debe realizar un an�lisis concreto, false en caso contrario.
	 * 
	 * @throws BadLocationException
	 */
	private void apply(int startOffset, int endOffset,
			boolean analisisCompleto) throws BadLocationException {
		doc = (DefaultStyledDocument) edit.getDocument();
		String content = doc.getText(0, doc.getLength());

		int numeroCaracteres = endOffset
				+ ServiciosString.vecesQueContiene(content, "\n") + 1;

		if (construyendo && numeroCaracteres < longitudTextoInicial
				&& longitudTextoInicial != -1) {
			return;
		}

		boolean parsearTodo = false;
		if (construyendo) {
			construyendo = false;
			parsearTodo = true;

			new Thread() { // Thread para aseguarnos que queda bien coloreado al
							// principio el texto del panel

				@Override
				public synchronized void run() {

					try {
						wait(2000);
						apply(0, edit.getText().length() - 1, true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();

		}

		if (analisisCompleto) {
			parsearTodo = true;
		}

		content = content.toLowerCase();

		String caracterFinLinea = "\n";

		if (!parsearTodo) // Delimitamos la zona de parseo para agilizar
		{
			int cursor = this.edit.getCaretPosition();

			int ini = 0;
			int fin = 0;

			if (cursor == 0) // Si la posici�n de caret es 0, buscamos en la
								// �ltima l�nea para colorear
			{
				startOffset = content.lastIndexOf(caracterFinLinea);
			} else {
				int longTotal = content.length();

				ini = cursor - 1;
				fin = cursor + 1;
				String cadena;
				do {
					cadena = content.substring(ini, cursor);
					ini--;
				} while (!cadena.contains(caracterFinLinea) && ini >= 0);

				cadena = "";
				do {
					if (cadena.length() > fin) {
						cadena = content.substring(cursor, fin);
					}
					fin++;
				} while (!cadena.contains(caracterFinLinea) && fin < longTotal);

				startOffset = ini + 2;
				endOffset = fin - 2;

				if (startOffset == 1) {
					startOffset = 0;
				}

			}
		}
		
		// Reinicializamos el texto que se va a analizar a negro
		MutableAttributeSet attrX = new SimpleAttributeSet();
		StyleConstants.setForeground(attrX, Color.black);
		StyleConstants.setBold(attrX, false);
		doc.setCharacterAttributes(startOffset, endOffset - startOffset, attrX,
				false);

		String c;
		if (parsearTodo) {
			c = content.substring(0, content.length());
		} else {
			c = content.substring(startOffset, endOffset);
		}

		int encontradoComentarioML1 = -1; // !-1 si hemos detectado ".../*...";
		int encontradoComentarioML2 = -1; // !-1 si hemos detectado "...*/...";

		boolean estamosEnComentario = false;

		if (!parsearTodo) {
			// hay que comprobar que no est�n escritos dentro de una cadena
			// literal ".../*..."

			// Vemos si hay inicio o final de comentario multilinea para
			// colorear parte correspondiente de esa linea
			if (c.contains("/*") && !c.contains("*/")) {
				encontradoComentarioML1 = c.indexOf("/*");

				if (!comentDentroDeCadena(c, encontradoComentarioML1)) {
					MutableAttributeSet attrC = new SimpleAttributeSet();
					StyleConstants.setForeground(attrC, Conf.colorCodigoCo);
					StyleConstants.setBold(attrC, true);
					doc.setCharacterAttributes(startOffset
							+ encontradoComentarioML1, c.length()
							- encontradoComentarioML1, attrC, false);
				} else {
					encontradoComentarioML1 = -1;
				}
			} else if (!c.contains("/*") && c.contains("*/")) {
				encontradoComentarioML2 = c.indexOf("*/") + 2;

				if (!comentDentroDeCadena(c, encontradoComentarioML2)) {
					MutableAttributeSet attrC = new SimpleAttributeSet();
					StyleConstants.setForeground(attrC, Conf.colorCodigoCo);
					StyleConstants.setBold(attrC, true);
					doc.setCharacterAttributes(startOffset,
							encontradoComentarioML2, attrC, false);
				} else {
					encontradoComentarioML2 = -1;
				}
			} else if (!c.contains("/*") && !c.contains("*/"))// L�nea normal o
																// que podr�a
																// estar dentro
																// de un
																// comentario
																// multilinea,
																// comprobamos
			{
				boolean encontradaRespuesta = false;
				int i = startOffset;

				if (i == content.length()) {
					i--;
				}

				while (i >= 0 && !encontradaRespuesta) {
					if (content.charAt(i) == '/') {
						if (content.charAt(i - 1) == '*') // Hemos encontrado */
						{
							encontradaRespuesta = true;
							estamosEnComentario = false;
						} else if (content.charAt(i + 1) == '*') // Hemos
																	// encontrado
																	// /*
						{
							encontradaRespuesta = true;
							estamosEnComentario = true;
						}
					} else if (content.charAt(i) == '"') {
						if (content.length() >= i) {
							do {
								i--;
							} while (i >= 0 && content.charAt(i) != '"');
						}
					}
					i--;
				}
				if (estamosEnComentario) {
					MutableAttributeSet attrC = new SimpleAttributeSet();
					StyleConstants.setForeground(attrC, Conf.colorCodigoCo);
					StyleConstants.setBold(attrC, true);
					doc.setCharacterAttributes(startOffset, endOffset
							- startOffset, attrC, false);
				}

			}
		}

		int endOffsetC = c.length();

		int index = startOffset;
		int indexC = 0;
		while (indexC < endOffsetC) {
			String p = getpalabra(indexC, c);
			int longPalabra = p.length();

			index = index + longPalabra;
			indexC = indexC + longPalabra;

			if (p.charAt(0) != ' ' && p.charAt(0) != ';') {
				if (palabras != null && palabras.get(p) != null) // si la
																	// palabra
																	// est�
																	// entre las
																	// palabras
																	// reservadas
				{

					if (((encontradoComentarioML1 == -1 && encontradoComentarioML2 == -1)
							|| (encontradoComentarioML1 != -1 && indexC <= encontradoComentarioML1) || (encontradoComentarioML2 != -1 && indexC >= encontradoComentarioML2 - 2))
							&& !estamosEnComentario) {

						MutableAttributeSet attr = new SimpleAttributeSet();
						StyleConstants.setForeground(attr, Conf.colorCodigoPR);
						StyleConstants.setBold(attr, true);
						doc.setCharacterAttributes(index - longPalabra,
								longPalabra, attr, false);
					}
				} else if (metodos != null && metodos.get(p) != null) // si la
																		// palabra
																		// est�
																		// entre
																		// los
																		// nombres
																		// de
																		// metodos
																		// que
																		// tenemos
																		// que
																		// resaltar
				{
					MutableAttributeSet attr = new SimpleAttributeSet();
					StyleConstants
							.setForeground(attr, new Color(150, 150, 150));
					StyleConstants.setBold(attr, true);
					doc.setCharacterAttributes(index - longPalabra,
							longPalabra, attr, false);
				} else if (p.length() >= 2 && p.charAt(0) == '/'
						&& p.charAt(1) == '/') // si la palabra es un comentario
												// monolinea
				{
					MutableAttributeSet attr = new SimpleAttributeSet();
					StyleConstants.setForeground(attr, Conf.colorCodigoCo);
					StyleConstants.setBold(attr, true);
					doc.setCharacterAttributes(index - longPalabra,
							longPalabra, attr, false);
				} else if (p.length() >= 2 && p.charAt(0) == '/'
						&& p.charAt(1) == '*') // si la palabra es un comentario
												// multilinea
				{
					MutableAttributeSet attr = new SimpleAttributeSet();
					StyleConstants.setForeground(attr, Conf.colorCodigoCo);
					StyleConstants.setBold(attr, true);
					doc.setCharacterAttributes(index - longPalabra,
							longPalabra, attr, false);
				}
			}
		}
		actualizarBandaNumLinea();
	}
	
	/**
	 * Permite determinar si dentro de la cadena especificada se encuentra un comentario.
	 * 
	 * @param cad Cadena de entrada.
	 * @param posic Posici�n inicial.
	 * 
	 * @return True si hay un comentario, false en caso contrario.
	 */
	private boolean comentDentroDeCadena(String cad, int posic) {
		boolean ok = false;

		if (cad.contains("\"")) {
			int comillaIni = -1;
			int comillaFin = -1;

			do {
				comillaIni = cad.indexOf("\"", comillaFin + 1);
				comillaFin = cad.indexOf("\"", comillaIni + 1);
			} while (comillaFin < posic);

			if (comillaIni < posic && posic < comillaFin) {
				ok = true; // No lo pintamos porque est� dentro de una cadena
			}
		}
		return ok;
	}
	
	/**
	 * Actualiza los n�meros de l�nea de la banda izquierda.
	 */
	private void actualizarBandaNumLinea() {
		if (areaNum != null) {
			String texto = "";
			try {
				doc = (DefaultStyledDocument) edit.getDocument();
				texto = doc.getText(0, doc.getLength());
			} catch (Exception e) {

			}

			int numLineas = this.edit.getText().length() - texto.length() + 1;

			int numCifras = ("" + numLineas).length();

			String[] espacios = { "", " ", "  ", "   ", "    ", "     ",
					"      ", "       ", "        ", "         ", "          " };

			if (numLineas != numLineasAnterior) {
				String textoNum = "";

				int i = 1;
				while (i <= numLineas) {
					textoNum = textoNum
							+ espacios[numCifras - ("" + i).length()] + i
							+ " \n";
					i++;
				}

				try {
					areaNum.setText(textoNum);
					areaNum.updateUI();
				} catch (Exception e) {

				}
			}
			numLineasAnterior = numLineas;
		}
	}
	
	/**
	 * Inner class que permite analizar y aplicar el formato necesario
	 * a un documento al insertar nuevas cadenas.
	 */
	private class MyDocument extends DefaultStyledDocument {

		private static final long serialVersionUID = -2027166096474442532L;

		@Override
		public void insertString(int offset, String str, AttributeSet a)
				throws BadLocationException {
			super.insertString(offset, str, a);
			apply(0, getLength(), false);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		
		int longitudIntroduccion = arg0.getLength();

		char c = 'a';

		char a = 'a'; // Caracter anterior al insertado
		char b = 'a'; // Caracter posterior al insertado
		doc = (DefaultStyledDocument) edit.getDocument();
		try {
			int longCadenaDoc = doc.getLength();
			String cadenaDoc = doc.getText(0, longCadenaDoc);
			int offSetArg0 = arg0.getOffset();
			c = cadenaDoc.charAt(offSetArg0);
			if (offSetArg0 > 0) {
				a = cadenaDoc.charAt(offSetArg0 - 1);
			}
			if (offSetArg0 < longCadenaDoc - 1) {
				b = cadenaDoc.charAt(offSetArg0 - 1);
			}
		} catch (Exception e) {
			
		}

		if (longitudIntroduccion >= 1 || c == '\n') {
			actualizarBandaNumLinea();
		}

		if (c == '/' || c == '*' || a == '/' || a == '*' || b == '/'
				|| b == '*' || longitudIntroduccion > 1) {
			new Thread() {

				@Override
				public synchronized void run() {
					try {
						wait(10);
						apply(0, edit.getText().length() - 1, true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		
		actualizarBandaNumLinea();

		new Thread() {
			@Override
			public synchronized void run() {
				try {
					wait(10);
					apply(0, edit.getText().length() - 1, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
	 * Devuelve el texto del editor.
	 * 
	 * @return Texto del editor.
	 */
	public String getText() {
		return this.edit.getText();
	}
	
	/**
	 * Permite seleccionar una porci�n del texto.
	 * 
	 * @param inicio Posici�n de inicio de la selecci�n.
	 * @param longitud Tama�o de la selecci�n.
	 */
	public void select(int inicio, int longitud) {
		this.edit.select(inicio, longitud);
		this.edit.requestFocus();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		Ventana.thisventana.setClasePendienteGuardar(true);
	}

}