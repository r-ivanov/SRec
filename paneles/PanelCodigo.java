/**
	Panel que contendrá la visualización del algoritmo, ya que almacena la propia traza.
	Está contenido en PanelAlgoritmo.
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/
package paneles;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyleConstants;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;




import conf.*;
import utilidades.*;
import ventanas.Ventana;

class PanelCodigo implements MouseListener
{
	static final long serialVersionUID=04;

	String archivo;
	
	StringBuffer contents;
	String nombreMetodo;
	
	
	JTextPane textPane;

	PanelEditorJava panelJava;
	
	JScrollPane contenedorPanel;
	
	JPanel panel=new JPanel();
	
	boolean editable=true;
	
	
	/**
		Constructor: crea un nuevo panel de traza
		
		@param numPanelesTraza número de panales que se desean
	*/
	public PanelCodigo(String archivo,String nombreMetodo)
	{
		this.archivo=archivo;
		this.nombreMetodo=nombreMetodo;
		
		this.abrir(archivo,this.editable,true,true);
	}

	
	/**
		Visualiza el código
	*/		
	/*public void visualizar()
	{
		visualizar(false);
	}*/
	
	public void visualizar(boolean recargarFormato)
	{
		this.abrir(this.archivo, this.editable, Ventana.thisventana.getClase()==null, recargarFormato);
	}
	

	public void abrir(String archivo, boolean editable, boolean cargarFichero, boolean recargarFormato)
	{
		if (archivo!=null)
		{
			this.editable=editable;
			//textPane=crearTextPane(archivo,nombreMetodo);
			//textPane.setEditable(true);

			if (cargarFichero)
			{
				this.archivo=archivo;
				
				// Carga de fichero
				this.contents = new StringBuffer();
				//this.nombreMetodo=nombreMetodo;
				BufferedReader input = null;
				try {
					input = new BufferedReader( new FileReader(archivo) );
					String line = null; //not declared within while loop
					while (( line = input.readLine()) != null)
					{
						//line=line.replace("\t","    ");
						contents.append(line);
						contents.append(System.getProperty("line.separator"));
					}
				} catch (FileNotFoundException ex) {
					this.contents=new StringBuffer(Texto.get("PCOD_ARCHJAVANOENC",Conf.idioma)+"\""+archivo+"\"");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				try {
					if (input!= null)
						input.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}	
				
			}
			else if (recargarFormato)
			{
				this.contents=new StringBuffer(this.getText());
			}
			
			
			this.panelJava=new PanelEditorJava(new String(this.contents),editable);
			//System.out.println("panelJava inicializado");
			panel.setLayout(new BorderLayout());
			panel.removeAll();

			panel.add(this.panelJava);
			panel.updateUI();
		}
		else
		{
			//panelJava=new PanelEditorJava();
			
			
			
			//panel.add(new JPanel());
			/*new Thread(){
				
				public synchronized void run()
				{
					try { wait(800); } catch (Exception e) {
						
					}
					System.out.println("que si, que lo pinto de blanco");
					JPanel panelAux=new JPanel();
					panel.removeAll();
					panel.setLayout(new BorderLayout());
					panelAux.setBackground(new Color(255,255,255));
					panel.add(panelAux,BorderLayout.CENTER);
					panel.updateUI();
				}
			}.start();*/
		}
	}
	
	
	public JPanel getPanel()
	{
		return this.panel;
	}
	

	
	
	public String getText()
	{
		
		String s;
		s=this.panelJava.getText();
		//System.out.println("PanelCodigo.getText -> longitud del texto = "+s.length());
		return s;
	}
	
	
	public void select(int inicio, int longitud)
	{
		//System.out.println("select : inicio="+inicio+" longitud="+longitud);
		this.panelJava.select(inicio,longitud);
	}
    
    public void guardar()
	{
		try {
			//System.out.println("Archivo que se va a guardar: ["+archivo+"]");
			FileWriter fileStream = new FileWriter( archivo);
			fileStream.write(this.panelJava.getText());
			fileStream.close();
		} catch (IOException ioe) {
			System.out.println("Error IOException  PanelCodigo.guardar");
		} catch (NullPointerException npe) {
			System.out.println("Error NullPointerException  PanelCodigo.guardar");
		}
		
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
	
	
	/*private int numLinea(String texto)
	{
		if ( ( texto.indexOf("java:") )>=0 && ( texto.indexOf("java:")+5 )<texto.length())
		{
			String t=texto.substring( texto.indexOf("java:")+5,texto.length());
			
			if (t.indexOf(":")!=-1)
			{
				t=t.substring(0,t.indexOf(":"));
			}
			int x=-1;
			try {
				x=Integer.parseInt(t);
			} catch (Exception e) {
				return -1;
			}
			return x;
			}
		return -1;
	}

	
	
	public void setTextoCompilador(String texto)
	{
		
		if (texto.length()<8)
		{
			this.espacioCompilador.setText("Compilation with no errors.");
		}
		else
		{
			this.espacioCompilador.setText(texto.substring(4,texto.length()));
		}
		
	}*/
	
	public boolean getEditable()
	{
		return this.editable;
	}
}

