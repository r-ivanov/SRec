/**
	Panel que contendrá la visualización del algoritmo, ya que almacena la propia traza.
	Está contenido en PanelAlgoritmo.
	
	@author Luis Fernández y Antonio Pérez Carrasco
	@version 2006-2007
*/
package paneles;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;  
import java.util.Arrays;  
import java.util.ArrayList;  



import conf.*;
import utilidades.*;
import ventanas.*;

class PanelTraza extends JPanel //implements MouseListener
{
	static final long serialVersionUID=04;

	String textoEntrada;
	String textoSalida;
	
	//int numVeces=0;
	
	/**
		Constructor: crea un nuevo panel de traza
		
		@param numPanelesTraza número de panales que se desean
	*/		
	public PanelTraza()
	{
		if (Ventana.thisventana.traza!=null)
		{
			this.textoEntrada=Texto.get("TR_E",Conf.idioma);
			this.textoSalida=Texto.get("TR_S",Conf.idioma);
			
			//int numLlamadas = traza.getNumNodos();
			String lineasTraza[]=Ventana.thisventana.traza.getLineasTraza( );
			
			BorderLayout bl=new BorderLayout();
			GridLayout gl=new GridLayout(lineasTraza.length,1);
			JPanel panelEtiquetas = new JPanel();
			panelEtiquetas.setLayout(gl);
			JLabel etiquetas[]=new JLabel[lineasTraza.length];

			for (int i=0; i<lineasTraza.length; i++)
			{
				if (lineasTraza[i].contains("<hist>"))
					etiquetas[i]=new JLabel( lineasTraza[i].substring(0, lineasTraza[i].indexOf("<hist>")));
				else
					etiquetas[i]=new JLabel( lineasTraza[i]);
				if (lineasTraza[i].contains("entra"))		// Si son líneas de entrada
				{
					lineasTraza[i]=lineasTraza[i].replace("entra",Texto.get("TR_E",Conf.idioma));
					if (lineasTraza[i].contains("<hist>") && Conf.historia==1)
						etiquetas[i].setForeground(Conf.colorTrazaEA);
					else
						etiquetas[i].setForeground(Conf.colorTrazaE);
				}
				else										// Si son líneas de salida
				{
					lineasTraza[i]=lineasTraza[i].replace("sale",Texto.get("TR_S",Conf.idioma));
					if (lineasTraza[i].contains("<hist>") && Conf.historia==1)
						etiquetas[i].setForeground(Conf.colorTrazaSA);
					else
						etiquetas[i].setForeground(Conf.colorTrazaS);
				}
				etiquetas[i].setFont(Conf.fuenteTraza);
				panelEtiquetas.add(etiquetas[i]);
			}
			
			this.setLayout(bl);
			this.add(panelEtiquetas, BorderLayout.NORTH);
			
			//numVeces=2;
			this.setBackground(Conf.colorPanel);
			panelEtiquetas.setBackground(Conf.colorPanel);
			
		}
		else
		{
			this.add(new JPanel());
		}
	}

	/**
		Visualiza y redibuja el árbol en el panel
	*/		
	public void visualizar()
	{
		if (Ventana.thisventana.traza!=null)
		{
			this.removeAll();
			
			if(Conf.mostrarSalidaLigadaEntrada) 
			{
				this.visualizarSalidaLigadaEntrada();
			}
			else 
			{
			
				String lineasTraza[]=Ventana.thisventana.traza.getLineasTraza(  );
							
				int numNoHistoricos=0;
				if (Conf.historia==2) // Si nodos históricos son eliminados
				{
					for (int i=0; i<lineasTraza.length; i++)
						if (!lineasTraza[i].contains("<hist>"))
							numNoHistoricos++;
							
					String lineasTrazaNoHistoricas[]=new String[numNoHistoricos];
					
					numNoHistoricos=0;
					for (int i=0; i<lineasTraza.length; i++)
						if (!lineasTraza[i].contains("<hist>"))
						{
							lineasTrazaNoHistoricas[numNoHistoricos]=lineasTraza[i];
							numNoHistoricos++;
						}
					lineasTraza=lineasTrazaNoHistoricas;
				}
						
				BorderLayout bl=new BorderLayout();
				GridLayout gl=new GridLayout(lineasTraza.length,1);
				JPanel panelEtiquetas = new JPanel();
				panelEtiquetas.setLayout(gl);
				JLabel etiquetas[]=new JLabel[lineasTraza.length];
				
				for (int i=0; i<lineasTraza.length; i++)
				{
					if (lineasTraza[i].contains("<hist>"))
						etiquetas[i]=new JLabel( lineasTraza[i].substring(0, lineasTraza[i].indexOf("<hist>")));
					else
						etiquetas[i]=new JLabel( lineasTraza[i]);
					
					
					
					if (lineasTraza[i].contains("entra"))		// Si son líneas de entrada
					{
						lineasTraza[i]=lineasTraza[i].replace("entra", textoEntrada );
					
						if (lineasTraza[i].contains("<ilum>"))	// Si hay que iluminar
						{
							etiquetas[i].setForeground(Conf.colorIluminado);
						}
						else if (lineasTraza[i].contains("<hist>") && Conf.historia==1)	// Si hay que atenuar
						{
							if (Conf.modoColor==1)
								etiquetas[i].setForeground(Conf.colorC1AEntrada);
							else
							{
								int numColor=Integer.parseInt(lineasTraza[i].substring(lineasTraza[i].indexOf("?")+1,lineasTraza[i].lastIndexOf("?")));
								etiquetas[i].setForeground(Conf.coloresNodoA[numColor]);
							}
						}		
						else
						{
							if (Conf.modoColor==1)
								etiquetas[i].setForeground(Conf.colorC1Entrada);
							else
							{
								int numColor=Integer.parseInt(lineasTraza[i].substring(lineasTraza[i].indexOf("?")+1,lineasTraza[i].lastIndexOf("?")));
								etiquetas[i].setForeground(Conf.coloresNodo[numColor]);
							}
						}
					}
					else										// Si son líneas de salida
					{	
						lineasTraza[i]=lineasTraza[i].replace("sale",textoSalida);
						if (lineasTraza[i].contains("<ilum>"))	// Si hay que iluminar
						{
							etiquetas[i].setForeground(Conf.colorIluminado);
						}
						else if (lineasTraza[i].contains("<hist>") && Conf.historia==1)		// Si hay que atenuar
						{
							if (Conf.modoColor==1)
								etiquetas[i].setForeground(Conf.colorC1ASalida);
							else
							{
								int numColor=Integer.parseInt(lineasTraza[i].substring(lineasTraza[i].indexOf("?")+1,lineasTraza[i].lastIndexOf("?")));
								etiquetas[i].setForeground(Conf.coloresNodoA[numColor]);
							}
						}
						else
						{
							if (Conf.modoColor==1)
								etiquetas[i].setForeground(Conf.colorC1Salida);
							else
							{
								int numColor=Integer.parseInt(lineasTraza[i].substring(lineasTraza[i].indexOf("?")+1,lineasTraza[i].lastIndexOf("?")));
								etiquetas[i].setForeground(Conf.coloresNodo[numColor]);
							}
						}
					}
					if (lineasTraza[i].contains("?"))
					{
						lineasTraza[i]=lineasTraza[i].substring(0,lineasTraza[i].lastIndexOf("?"));
						lineasTraza[i]=lineasTraza[i].substring(0,lineasTraza[i].lastIndexOf("?"));
					}
					
					lineasTraza[i]=lineasTraza[i].replace("<hist>","");
					lineasTraza[i]=lineasTraza[i].replace("<ilum>","");
					
					etiquetas[i].setText( lineasTraza[i]);
					etiquetas[i].setFont(Conf.fuenteTraza);
					panelEtiquetas.add(etiquetas[i]);
				}
				this.setLayout(bl);
				this.add(panelEtiquetas, BorderLayout.NORTH);
				//numVeces++;
				
				//System.out.println( "TRAZA:\n"+crearArchivo("html") );
				this.setBackground(Conf.colorPanel);
				panelEtiquetas.setBackground(Conf.colorPanel);
				
				this.updateUI();
			}
		}
	}
	
	
	public void visualizarSalidaLigadaEntrada()
	{
		Ventana.thisventana.trazaCompleta.todoVisible();
		String lineasTraza[] = Ventana.thisventana.trazaCompleta.getLineasTrazaSalidaLigadaEntrada();
		String lineasTraza2[] = Ventana.thisventana.trazaCompleta.getLineasTrazaSalidaLigadaEntrada();
		String lineasTrazaParcial[] = Ventana.thisventana.traza.getLineasTrazaSalidaLigadaEntrada();
		String lineasTrazaParcial2[] = Ventana.thisventana.traza.getLineasTrazaSalidaLigadaEntrada();
		
		int numNoHistoricos=0;
		if (Conf.historia==2) // Si nodos históricos son eliminados
		{
			for (int i=0; i<lineasTraza.length; i++)
				if (!lineasTraza[i].contains("<hist>"))
					numNoHistoricos++;
					
			String lineasTrazaNoHistoricas[]=new String[numNoHistoricos];
			
			numNoHistoricos=0;
			for (int i=0; i<lineasTraza.length; i++)
				if (!lineasTraza[i].contains("<hist>"))
				{
					lineasTrazaNoHistoricas[numNoHistoricos]=lineasTraza[i];
					numNoHistoricos++;
				}
			lineasTraza=lineasTrazaNoHistoricas;
		}
				
		BorderLayout bl=new BorderLayout();
		GridLayout gl=new GridLayout(lineasTraza.length*2,1);
		JPanel panelEtiquetas = new JPanel();
		panelEtiquetas.setLayout(gl);
		JLabel etiquetas[]=new JLabel[lineasTraza.length];
		
		boolean siguiente=true;
		boolean visible=false;
		String valor="";
		int iParcial=1;
		for (int i=0; i<lineasTraza.length; i++)
		{
			if (lineasTraza[i].contains("<hist>")) {
				etiquetas[i]=new JLabel( lineasTraza[i].substring(0, lineasTraza[i].indexOf("<hist>")));
				lineasTraza2[i] = lineasTraza[i].substring(0, lineasTraza[i].indexOf("<hist>")-1);
			}
			else
				etiquetas[i]=new JLabel( lineasTraza[i]);
			
			

			if (lineasTraza2[i].contains("?"))
				lineasTraza2[i] = lineasTraza2[i].substring(0, lineasTraza2[i].indexOf("?"));
			if(lineasTraza2[i].equals(valor)) {
				valor="";
				visible=true;
				siguiente=true;
				lineasTraza[i] = lineasTrazaParcial2[iParcial-1];
			}
			else {
				visible=false;
			}
			if(i==0) {
				valor="";
				visible=true;
				siguiente=true;
			}
			if (lineasTraza2[i].contains("?"))
				lineasTraza2[i] = lineasTraza[i].substring(0, lineasTraza[i].indexOf("?"));
			if((iParcial<lineasTrazaParcial.length)&&(siguiente)) {
				if (lineasTrazaParcial[iParcial].contains("?"))
					lineasTrazaParcial[iParcial] = lineasTrazaParcial[iParcial].substring(0, lineasTrazaParcial[iParcial].indexOf("?"));
				if (lineasTrazaParcial[iParcial].contains("<hist>"))
					lineasTrazaParcial[iParcial] = lineasTrazaParcial[iParcial].substring(0, lineasTrazaParcial[iParcial].indexOf("<hist>")-1);
				valor=lineasTrazaParcial[iParcial];
				siguiente=false;
				iParcial++;
			}
			
			
			if (lineasTraza[i].contains("entra"))		// Si son líneas de entrada
			{
				lineasTraza[i]=lineasTraza[i].replace("entra", textoEntrada );
			
				if (lineasTraza[i].contains("<ilum>"))	// Si hay que iluminar
				{
					etiquetas[i].setForeground(Conf.colorIluminado);
				}
				else if (lineasTraza[i].contains("<hist>") && Conf.historia==1)	// Si hay que atenuar
				{
					if (Conf.modoColor==1)
						etiquetas[i].setForeground(Conf.colorC1AEntrada);
					else
					{
						int numColor=Integer.parseInt(lineasTraza[i].substring(lineasTraza[i].indexOf("?")+1,lineasTraza[i].lastIndexOf("?")));
						etiquetas[i].setForeground(Conf.coloresNodoA[numColor]);
					}
				}		
				else
				{
					if (Conf.modoColor==1)
						etiquetas[i].setForeground(Conf.colorC1Entrada);
					else
					{
						int numColor=Integer.parseInt(lineasTraza[i].substring(lineasTraza[i].indexOf("?")+1,lineasTraza[i].lastIndexOf("?")));
						etiquetas[i].setForeground(Conf.coloresNodo[numColor]);
					}
				}
			}
			else										// Si son líneas de salida
			{	
				lineasTraza[i]=lineasTraza[i].replace("sale",textoSalida);
				if (lineasTraza[i].contains("<ilum>"))	// Si hay que iluminar
				{
					etiquetas[i].setForeground(Conf.colorIluminado);
				}
				else if (lineasTraza[i].contains("<hist>") && Conf.historia==1)		// Si hay que atenuar
				{
					if (Conf.modoColor==1)
						etiquetas[i].setForeground(Conf.colorC1ASalida);
					else
					{
						int numColor=Integer.parseInt(lineasTraza[i].substring(lineasTraza[i].indexOf("?")+1,lineasTraza[i].lastIndexOf("?")));
						etiquetas[i].setForeground(Conf.coloresNodoA[numColor]);
					}
				}
				else
				{
					if (Conf.modoColor==1)
						etiquetas[i].setForeground(Conf.colorC1Salida);
					else
					{
						int numColor=Integer.parseInt(lineasTraza[i].substring(lineasTraza[i].indexOf("?")+1,lineasTraza[i].lastIndexOf("?")));
						etiquetas[i].setForeground(Conf.coloresNodo[numColor]);
					}
				}
			}
			if (lineasTraza[i].contains("?"))
			{
				lineasTraza[i]=lineasTraza[i].substring(0,lineasTraza[i].lastIndexOf("?"));
				lineasTraza[i]=lineasTraza[i].substring(0,lineasTraza[i].lastIndexOf("?"));
			}
			
			lineasTraza[i]=lineasTraza[i].replace("<hist>","");
			lineasTraza[i]=lineasTraza[i].replace("<ilum>","");
			
			etiquetas[i].setText( lineasTraza[i]);
			etiquetas[i].setFont(Conf.fuenteTraza);
			etiquetas[i].setVisible(visible);
			visible=false;
			panelEtiquetas.add(etiquetas[i]);
		}
		this.setLayout(bl);
		this.add(panelEtiquetas, BorderLayout.NORTH);
		//numVeces++;
		
		//System.out.println( "TRAZA:\n"+crearArchivo("html") );
		this.setBackground(Conf.colorPanel);
		panelEtiquetas.setBackground(Conf.colorPanel);
		
		this.updateUI();
	}
	
	
	/*public void mouseEntered(MouseEvent e)
	{
		//System.out.println("Evento de raton, mouseEntered");
	}
	
	public void mouseExited(MouseEvent e) 
	{
		//System.out.println("Evento de raton, mouseExited");
	}
	
	public void mouseClicked(MouseEvent e)
	{
		//System.out.println("Evento de raton, mouseClicked");
	}
	
	public void mouseReleased(MouseEvent e)
	{
		//System.out.println("Evento de raton, mouseReleased");
	}
	
	public void mousePressed(MouseEvent e)
	{
		//System.out.println("Evento de raton, mousePressed");
	}*/

	
	
	

}

