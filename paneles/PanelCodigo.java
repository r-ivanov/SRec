package paneles;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JPanel;

import utilidades.Texto;
import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroEditorBuscarTexto;
import cuadros.CuadroEditorIrALinea;

/**
 * Panel que contiene el c�digo fuente de la clase cargada.
 * 
 * @author Antonio P�rez Carrasco
 * @version 2006-2007
 */
class PanelCodigo implements MouseListener {
	static final long serialVersionUID = 04;

	private String archivo;

	private StringBuffer contents;

	private PanelEditorJava2 panelJava;

	private JPanel panel = new JPanel();

	private boolean editable = true;

	/**
	 * Constructor: crea un nuevo PanelCodigo
	 * 
	 * @param archivo 
	 * 		Archivo que contiene el c�digo fuente de la clase.
	 * 
	 */
	public PanelCodigo(String archivo) {
		this.archivo = archivo;
		this.abrir(archivo, this.editable, true, true);
	}

	/**
	 * Permite visualizar el fichero configurado.
	 * 
	 * @param recargarFormato Si debe volver a recargarse el formato del fichero mostrado.
	 */
	public void visualizar(boolean recargarFormato) {
		this.abrir(this.archivo, this.editable,
				Ventana.thisventana.getClase() == null, recargarFormato);
	}
	
	/**
	 * Permite visualizar un fichero.
	 * 
	 * @param archivo Archivo que contiene el c�digo fuente de la clase.
	 * @param editable Si se permiten modificaciones sobre el mismo.
	 * @param cargarFichero Si debe leerse de disco de nuevo.
	 * @param recargarFormato Si debe recargarse el formato del fichero.
	 */
	public void abrir(String archivo, boolean editable, boolean cargarFichero,
			boolean recargarFormato) {
		if (archivo != null) {
			this.editable = editable;

			if (cargarFichero) {
				this.archivo = archivo;

				// Carga de fichero
				this.contents = new StringBuffer();
				BufferedReader input = null;
				try {
					input = new BufferedReader(new FileReader(archivo));
					String line = null;
					while ((line = input.readLine()) != null) {
						this.contents.append(line);
						this.contents.append(System
								.getProperty("line.separator"));
					}
				} catch (FileNotFoundException ex) {
					this.contents = new StringBuffer(Texto.get(
							"PCOD_ARCHJAVANOENC", Conf.idioma)
							+ "\""
							+ archivo
							+ "\"");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				try {
					if (input != null) {
						input.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}

			} else if (recargarFormato) {
				this.contents = new StringBuffer(this.getText());
			}
			
			//	Llamada al panel de Java	
        	this.panelJava = new PanelEditorJava2(new String(this.contents));
        	this.panelJava.setVisible(true);
		      
			this.panel.setLayout(new BorderLayout());
			this.panel.removeAll();

			this.panel.add(this.panelJava);
			this.panel.updateUI();
		}
	}
	
	/**
	 * Devuelve el panel contenido por el PanelCodigo.
	 * 
	 * @return Panel
	 */
	public JPanel getPanel() {
		return this.panel;
	}
	
	/**
	 * Devuelve el contenido del editor.
	 * 
	 * @return Contenido del editor.
	 */
	public String getText() {
		
		if(this.panelJava==null)
			return "";
		
		String s;
		s = this.panelJava.getText();
		return s;
	}
	
	/**
	 * Permite seleccionar un fragmento de c�digo.
	 * 
	 * @param inicio Posici�n inicial.
	 * 
	 * @param longitud Posici�n final.
	 */
	private void select(int inicio, int longitud) {
		this.panelJava.select(inicio, longitud, false);
	}
	
	/**
	 * Elimina todas las l�neas subrayadas, para limpiar
	 */
	public void removeSelects(){
		this.panelJava.removeSelects();
	}
	
	 /**
	 * Permite subrayar una l�nea del editor
	 * 
	 * @param numeroLinea
	 * 		N�mero de l�nea a subrayar
	 */
	public void subrayarLineaEditor(int numeroLinea){
		
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

		this.select(inicio - numeroLinea + 1, longitud);
	}	
	
	/**
	 * Mueve el scroll (focus) del editor de c�digo a la l�nea indicada por
	 * 	numLinea
	 * 
	 * @param numLinea
	 * 		N�mero de l�nea donde queremos hacer scroll o focus
	 * 
	 */
	public void focusLinea(int numLinea){
		this.panelJava.focusLinea(numLinea);
	}
	
	/**
	 * Permite guardar los cambios efectuados en el fichero.
	 */
	public void guardar() {
		try {
			FileWriter fileStream = new FileWriter(this.archivo);
			fileStream.write(this.panelJava.getText());
			fileStream.close();
		} catch (IOException ioe) {
			System.out.println("Error IOException  PanelCodigo.guardar");
		} catch (NullPointerException npe) {
			System.out
					.println("Error NullPointerException  PanelCodigo.guardar");
		}

	}

	/**
	 * Redibuja las l�neas sobrayadas del editor, por si cambian el color
	 */
	public void redibujarLineasErrores(){
		if(this.panelJava != null)
			this.panelJava.redibujarLineasErrores();	
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
	 *			otro n�mero: "default.xml";
	 * 
	 */
	public void changeTheme(int tema){
		if(this.panelJava != null)
			this.panelJava.changeTheme(tema);
	}

	
	/**
	 * Operaci�n deshacer
	 */
	public void doUndo() {
		this.panelJava.doUndo();
	}
	
	/**
	 * Operaci�n rehacer
	 */
	public void doRedo() {
		this.panelJava.doRedo();
	}
	
	/**
	 * Operaci�n copiar
	 */
	public void doCopy() {
		this.panelJava.doCopy();
	}
	
	/**
	 * Operaci�n pegar
	 */
	public void doPaste() {
		this.panelJava.doPaste();
	}
	
	/**
	 * Operaci�n cortar
	 */
	public void doCut() {
		this.panelJava.doCut();
	}
	
	/**
	 * Operaci�n seleccionar todo
	 */
	public void doSelectAll() {
		this.panelJava.doSelectAll();
	}
	
	/**
	 * Operaci�n seleccionar l�nea
	 */
	public void doSelectLine() {
		new CuadroEditorIrALinea(Ventana.thisventana, this.panelJava);
	}
	
	/**
	 * Operaci�n buscar
	 */
	public void doSearch() {
		new CuadroEditorBuscarTexto(Ventana.thisventana, this.panelJava);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
	
	/**
	 * Devuelve si el fichero puede editarse o no.
	 * 
	 * @return true si es editable, false en caso contrario.
	 */
	public boolean getEditable() {
		return this.editable;
	}
}
