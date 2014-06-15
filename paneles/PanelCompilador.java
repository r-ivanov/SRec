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


import javax.swing.JTextPane;

import utilidades.Texto;
import ventanas.Ventana;





import conf.*;


public class PanelCompilador implements MouseListener
{
	static final long serialVersionUID=04;

	String texto;
	
	StringBuffer contents;

	JTextPane espacioCompilador;
	PanelEditorJava panelJava;
	
	JScrollPane contenedorPanel;
	
	JPanel panel=new JPanel();
	
	//PanelCodigo pCodigo;
	PanelAlgoritmo pAlgoritmo;
	
	public static final String CODIGO_VACIO="      ";
	
	
	final int LONG_MINIMA=CODIGO_VACIO.length()-1;
	
	
	/**
		Constructor: crea un nuevo panel de traza
		
		@param numPanelesTraza número de panales que se desean
	*/
	public PanelCompilador(PanelAlgoritmo pAlgoritmo)
	{
		this.texto="";
		this.pAlgoritmo=pAlgoritmo;
		//this.pCodigo=pCodigo;
		
		
		espacioCompilador = new JTextPane();
		espacioCompilador.setText("");
		espacioCompilador.setFont(new Font("Courier New",Font.PLAIN, 11));
		espacioCompilador.setBackground(Conf.colorPanel);
		espacioCompilador.setEditable(false);
		espacioCompilador.addMouseListener(this);
		
		panel.setLayout(new BorderLayout());
		panel.add(espacioCompilador);
		
		this.borrarTextoCompilador();
		panel.updateUI();
		
		
	}
	
	
	public JPanel getPanel()
	{
		return this.panel;
	}
	


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==espacioCompilador)
		{
			new Thread(){
				public synchronized void run()
				{
					try { wait(60); } catch (Exception e) {};
					String seleccionado=espacioCompilador.getSelectedText();
					if (seleccionado!=null)
					{
						int numeroLinea=numLinea(seleccionado);
						//System.out.println("LineaDeCodigo="+numeroLinea);
						
						int nLineas=numeroLinea;
						
						if (numeroLinea!=-1)
						{
							int longitud=0;
							String textoareatexto=pAlgoritmo.getPanelCodigo().getText();

							//System.out.println("numeroLinea = "+numeroLinea);
							while (numeroLinea>1)		// !=0
							{
								longitud=longitud+(textoareatexto.substring(0,textoareatexto.indexOf("\n")).length()+1);
								//System.out.println("["+textoareatexto.substring(0,textoareatexto.indexOf("\n"))+"]");
								textoareatexto=textoareatexto.substring(textoareatexto.indexOf("\n")+1,
													textoareatexto.length());
								numeroLinea--;
								//System.out.println("longitud = "+longitud);
							}
							int inicio=longitud;
							
							try {
								longitud=(textoareatexto.substring(0,textoareatexto.indexOf("\n")).length());
							} catch (Exception e) {
								longitud=textoareatexto.length();
							}
							//System.out.println("longitud = "+longitud+" ["+textoareatexto.substring(0,textoareatexto.indexOf("\n"))+"]");
							
							
							pAlgoritmo.getPanelCodigo().select(inicio-nLineas+1,longitud);
							
							//System.out.println("inicio="+inicio+"  final="+longitud);
						}
					}
				}
			}.start();
		}
	}
	
	
	private int numLinea(String texto)
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

	
	
	public void borrarTextoCompilador()
	{
		this.setTextoCompilador(CODIGO_VACIO);
	}
	
	public void setTextoCompilador(String texto)
	{
		
		if (texto.length()<LONG_MINIMA)
		{
			this.espacioCompilador.setBackground(Conf.colorPanel);
			this.espacioCompilador.setText(Texto.get("COMPILAR_NOERRORES",Conf.idioma));
		}
		else if (texto.equals(CODIGO_VACIO))
		{
			this.espacioCompilador.setBackground(new Color(255,255,255));
			this.espacioCompilador.setText(texto);
		}
		else
		{
			this.espacioCompilador.setBackground(Conf.colorPanel);
			this.espacioCompilador.setText(texto);
		}
		
	}
}

