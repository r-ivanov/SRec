package ventanas;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;


import javax.swing.ImageIcon;

import javax.swing.JEditorPane;
import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.SwingUtilities;



import botones.*;
import conf.*;
import utilidades.*;

public class VisorAyuda extends Thread implements ActionListener, MouseListener, KeyListener, HyperlinkListener
{

	private JFrame frame;
	private JEditorPane pane;

	private String fichero;
	private String index;

	private BotonImagen atras;
	private BotonImagen adelante;
	private BotonImagen indice;
	
	String pilaAt[]=new String[0];	// Pila para ir hacia atrás
	String pilaAd[]=new String[0];	// Pila para ir hacia adelante

	
	final int anchoCuadro=750;
	final int altoCuadro=570;
	
	
	
	public VisorAyuda()
	{
		this("index.html");
	}
	
	public VisorAyuda(String s)
	{
		this.fichero=s;
		this.start();
	}
	
	public void run()
	{
		frame=new JFrame(Texto.get("VA_AYUDA",Conf.idioma));
		
		//Botones
		atras = new BotonImagen("imagenes/boton_atrasayuda_verde.gif","imagenes/boton_atrasayuda_naranja.gif",
					"imagenes/boton_atrasayuda_rojo.gif", Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
		adelante = new BotonImagen("imagenes/boton_adelanteayuda_verde.gif","imagenes/boton_adelanteayuda_naranja.gif",
					"imagenes/boton_adelanteayuda_rojo.gif", Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
		indice = new BotonImagen("imagenes/boton_indice_verde.gif","imagenes/boton_indice_naranja.gif",
					"imagenes/boton_indice_rojo.gif",Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);

		atras.addMouseListener(this);
		adelante.addMouseListener(this);
		indice.addMouseListener(this);
		
		atras.setToolTipText(Texto.get("VA_ATRAS",Conf.idioma));
		adelante.setToolTipText(Texto.get("VA_ADEL",Conf.idioma));
		indice.setToolTipText(Texto.get("VA_INDICE",Conf.idioma));
		
		
		JPanel panelSuperior = new JPanel();
		BorderLayout bl= new BorderLayout();
		panelSuperior.setLayout(bl);
		JPanel panelBotones=new JPanel();
		
		panelBotones.add(atras);
		panelBotones.add(adelante);
		panelBotones.add(indice);
		panelSuperior.add(panelBotones,BorderLayout.WEST);
		frame.getContentPane().add(panelSuperior, "North");
		
		File f=new File("");
		this.fichero="file:///"+f.getAbsolutePath()+"\\ayuda\\"+this.fichero;
		this.index="file:///"+f.getAbsolutePath()+"\\ayuda\\"+Conf.idioma+"_index.html";
		//System.out.println("["+fichero+"]");
		
		pane = new JEditorPane();
		pane.setEditable(false); 
		frame.getContentPane().add(new JScrollPane(pane), "Center");
		try {
			pane.setPage(this.index);
		} catch (IOException e) {
			try {
				pane.setPage("file:///"+f.getAbsolutePath()+"\\ayuda\\"+"error.html");
			} catch (IOException e2) {
				System.out.println("VisorAyuda fichero no encontrado");
			}
		}
		//anadirAt(this.fichero);
		//mostrar(pilaAt,"\n\npilaAt");
		//mostrar(pilaAd,"\n\npilaAd");
		
		adelante.deshabilitar();
		atras.deshabilitar();
			
		frame.setIconImage( new ImageIcon("./imagenes/icono_ayuda.gif").getImage() );
		frame.setVisible(true);
		frame.setResizable(false);
		int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
		frame.setLocation(coord[0],coord[1]);
		frame.setSize(anchoCuadro, altoCuadro);
		//ActivatedHyperlinkListener listener=new ActivatedHyperlinkListener(frame,pane);
		pane.addHyperlinkListener( this );

	}
	
		
	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/	
	public void actionPerformed(ActionEvent e)
	{
		
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/		
	public void keyPressed(KeyEvent e)
	{
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyReleased(KeyEvent e)
	{
	}	

	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyTyped(KeyEvent e)
	{
	}	
		
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseClicked(MouseEvent e) 
	{
		colorearBotonesAdelanteAtras();
	}
	
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseEntered(MouseEvent e) 
	{
		if (e.getSource() instanceof BotonImagen)
			((BotonImagen)(e.getSource())).mostrarPulsado();		
	}

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseExited(MouseEvent e) 
	{
		if (e.getSource()==indice)
			indice.habilitar();
		colorearBotonesAdelanteAtras();
	}

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mousePressed(MouseEvent e) 
	{
		if (e.getSource()==indice)
		{
			// Reinicializamos cuál debe ser el index, por si ha cambiado idioma
			File f=new File("");
			this.index="file:///"+f.getAbsolutePath()+"\\ayuda\\"+Conf.idioma+"_index.html";
			// Fin reinicializacion
			
			if (!this.fichero.equals(this.index))
				anadirAt(this.fichero);
			try {
				pane.setPage(this.index);
			} catch (IOException ioe) {
				try {
					pane.setPage("file:///"+f.getAbsolutePath()+"\\ayuda\\"+"error.html");
				} catch (IOException e2) {
					System.out.println("VisorAyuda fichero no encontrado");
				}
			}
			this.fichero=this.index;
			
			borrarAd();
		}
		else if (e.getSource()==atras)
		{
			anadirAd(this.fichero);
			this.fichero=extraerAt();
			try {
				pane.setPage(this.fichero);
			} catch (IOException ioe) {
			}
		}
		else if (e.getSource()==adelante)
		{
			anadirAt(this.fichero);
			this.fichero=extraerAd();
			try {
				pane.setPage(this.fichero);
			} catch (IOException ioe) {
			}			
		}
		else
		{
			//System.out.println(e.getSource().getClass().getCanonicalName());
		}
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseReleased(MouseEvent e)
	{
	}
	
	/**
		Gestiona los eventos de hiperenlaces
		
		@param e evento de ratón
	*/		
	public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent)
	{
		HyperlinkEvent.EventType type = hyperlinkEvent.getEventType();
		final URL url = hyperlinkEvent.getURL();
		if (type == HyperlinkEvent.EventType.ENTERED) {
			//System.out.println("URL: " + url);
		}
		else if (type == HyperlinkEvent.EventType.ACTIVATED)
		{
			anadirAt(this.fichero);
			this.fichero=url.toExternalForm();
			//System.out.println("Activated");
			Runnable runner = new Runnable()
			{
				public void run()
				{
					// Retain reference to original
					try {
						pane.setPage(url);
					} catch (IOException ioException) {
						try {
							File f=new File("");
							pane.setPage("file:///"+f.getAbsolutePath()+"\\ayuda\\"+"error.html");
						} catch (IOException ioException2) {
						
						}
						/*JOptionPane.showMessageDialog(frame,
							"El archivo necesario no existe", "Enlace incorrecto",
						JOptionPane.ERROR_MESSAGE);
						pane.setDocument(doc);*/
					}
				}
			};
			SwingUtilities.invokeLater(runner);
			
			borrarAd();
			colorearBotonesAdelanteAtras();
			//mostrar(pilaAt,"\n\npilaAt");
			//mostrar(pilaAd,"\n\npilaAd");
		}
	}
	
	/*private void borrarAt()
	{
		pilaAt=new String[0];
	}*/
	
	private void borrarAd()
	{
		pilaAd=new String[0];
	}
	
	private void anadirAt(String f)
	{
		if (pilaAt.length==0 || !(f.equals(pilaAt[0])))
		{
			String pilaAux[]=new String[pilaAt.length+1];
			pilaAux[0]=f;
			for (int i=0; i<pilaAt.length; i++)
				pilaAux[i+1]=pilaAt[i];
			pilaAt=pilaAux;
		}
	}
	
	private void anadirAd(String f)
	{
		if (pilaAd.length==0 || !(f.equals(pilaAd[0])))
		{
			String pilaAux[]=new String[pilaAd.length+1];
			pilaAux[0]=f;
			for (int i=0; i<pilaAd.length; i++)
				pilaAux[i+1]=pilaAd[i];
			pilaAd=pilaAux;
		}
	}
	
	private String extraerAt()
	{
		if (pilaAt.length>0)
		{
			String pos0=pilaAt[0];
			String pilaAux[]=new String[pilaAt.length-1];
			for (int i=0; i<pilaAux.length; i++)
				pilaAux[i]=pilaAt[i+1];
			pilaAt=pilaAux;
			if (!(pos0.equals(this.fichero)))
				return pos0;
			else
				return extraerAt();
		}
		else
			return this.fichero;
	}
	
	private String extraerAd()
	{
		if (pilaAd.length>0)
		{
			String pos0=pilaAd[0];
			String pilaAux[]=new String[pilaAd.length-1];
			for (int i=0; i<pilaAux.length; i++)
				pilaAux[i]=pilaAd[i+1];
			pilaAd=pilaAux;
			if (!(pos0.equals(this.fichero)))
				return pos0;
			else
				return extraerAd();	
		}
		else
			return this.fichero;
	}
	
	/*private void mostrar(String pila[],String mensaje)
	{
		//System.out.println(mensaje);
		//for (int i=0; i<pila.length; i++)
		//	System.out.println(" - "+pila[i]);
		//System.out.println("fin pila ("+pila.length+" posiciones)");*
	}*/
	
	private void colorearBotonesAdelanteAtras()
	{
		if (pilaAt.length>0)
			atras.habilitar();
		else
			atras.deshabilitar();

		if (pilaAd.length>0)
			adelante.habilitar();
		else
			adelante.deshabilitar();
	}
}

