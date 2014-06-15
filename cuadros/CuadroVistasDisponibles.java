/**
	Genera el cuadro de diálogo que permite exportar el contenido gráfico de una vista
	**********************************************************************************
		
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;





import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import org.jgraph.JGraph;
import org.jgraph.graph.*;

import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;


import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import conf.*;
import datos.MetodoAlgoritmo;
import botones.*;
import opciones.*;
import utilidades.*;
import paneles.*;
import ventanas.*;

public class CuadroVistasDisponibles extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=07;

	final int anchoCuadro=275;
	final int altoCuadro=90;
	final int anchoCuadroNW=290;		// No Windows
	final int altoCuadroNW=404;			// No Windows

	PanelVentana pv;
	String [] vistasDisponibles;
	String [] vistasVisibles;
	
	GestorOpciones gOpciones=new GestorOpciones();

	JDialog dialogo;
	
	BotonAceptar aceptar=new BotonAceptar();
	BotonCancelar cancelar=new BotonCancelar();
	
	int tipoExportacion=0;
	JRadioButton botonesVistas[];
	ButtonGroup grupoBotones;
	
	JComponent panelFotografia; 
	JGraph grafo;
	String nombreVista;
	int numeroVista;
	int[] dimPanelFotografia;
	int[] posicPanelFotografia;
	/**
		Constructor: genera un nuevo cuadro de opción
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroVistasDisponibles (PanelVentana pv,int tipoExportacion)
	{	
		this.pv=pv;
		this.tipoExportacion=tipoExportacion;
		dialogo=new JDialog(Ventana.thisventana,true);
		this.start();
	}

	/**
		Genera una nueva opción
	*/
	
	public void run()
	{
		vistasDisponibles=pv.getNombreVistasDisponibles();
		vistasVisibles=pv.getNombreVistasVisibles();
		
		// Panel Datos que se muestran
		JPanel panelVistas = new JPanel();
		GridLayout gl1 = new GridLayout(vistasDisponibles.length,1);
		panelVistas.setLayout(gl1);
		
		grupoBotones=new ButtonGroup();
		
		botonesVistas = new JRadioButton[vistasDisponibles.length];
		for (int i=0; i<vistasDisponibles.length; i++)
		{
			botonesVistas[i]=new JRadioButton(vistasDisponibles[i]);
			botonesVistas[i].setToolTipText(Texto.get("CVD_MARCAR",Conf.idioma));
			botonesVistas[i].addKeyListener(this);
			if ((i==2)&&(!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.traza.getTecnicas())))
				botonesVistas[i].setEnabled(false);
			grupoBotones.add(botonesVistas[i]);
			panelVistas.add(botonesVistas[i]);
		}
		botonesVistas[0].setSelected(true);
		
		if((vistasDisponibles.length>=4)&&(this.tipoExportacion<3)) 
			botonesVistas[3].setEnabled(false);
		
		panelVistas.setBorder(new TitledBorder(Texto.get("CVD_SELECVISTA",Conf.idioma)));
		
		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(aceptar);
		panelBoton.add(cancelar);

		//aceptar.addActionListener(this);
		aceptar.addMouseListener(this);
		aceptar.addActionListener(this);
		cancelar.addMouseListener(this);
		cancelar.addActionListener(this);
	
		// Panel general
		BorderLayout bl= new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);
	
		panel.add(panelVistas,BorderLayout.NORTH);
		panel.add(panelBoton,BorderLayout.SOUTH);
			
		// Preparamos y mostramos cuadro
		
		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("CVD_VISTASDIS",Conf.idioma));

		if (Ventana.thisventana.msWindows)
		{
			dialogo.setSize(anchoCuadro,altoCuadro+(24*vistasDisponibles.length));
			int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
			dialogo.setLocation(coord[0],coord[1]);
		}
		else
		{
			dialogo.setSize(anchoCuadroNW,altoCuadroNW);
			int coord[]=Conf.ubicarCentro(anchoCuadroNW,altoCuadroNW);
			dialogo.setLocation(coord[0],coord[1]);
		}
		dialogo.setResizable(false);
		dialogo.setVisible(true);
	}



	
	/**
		Comprueba que la cadena s contenga un valor válido (0..255)
		
		@param s cadena que será comprobada
		@return true si la cadena contiene un valor válido para una componente RGB
	*/	
	private void getValores()
	{
		for (int i=0; i<this.botonesVistas.length; i++)
		{
			if (botonesVistas[i].isSelected())
			{
				pv.setVistaActiva(botonesVistas[i].getText());
				panelFotografia=pv.getPanelPorNombre(botonesVistas[i].getText());
				grafo=(JGraph)pv.getGrafoPorNombre(botonesVistas[i].getText());
				dimPanelFotografia=pv.getDimPanelPorNombre(botonesVistas[i].getText());
				posicPanelFotografia=pv.getPosicPanelPorNombre(botonesVistas[i].getText());
				nombreVista=botonesVistas[i].getText();
				numeroVista=i;
			}
		}
	}
	
	
	private void accion()
	{
		getValores();
		this.dialogo.setVisible(false);
		if (this.tipoExportacion==1)
		{
			if (Conf.fichero_log) Logger.log_write("Guardar GIF: capturarAnimacionGIF");
			//new FotografoArbol().capturarAnimacionGIF(panelFotografia);
			new FotografoArbol().capturarAnimacionGIF(grafo,numeroVista);
			
		}
		else if (this.tipoExportacion==2)
		{
			if (Conf.fichero_log) Logger.log_write("Guardar GIF: hacerCapturasPaso");
			//new FotografoArbol().hacerCapturasPaso(panelFotografia);
			new FotografoArbol().hacerCapturasPaso(grafo,numeroVista);
		}
		else if (this.tipoExportacion==3)
		{
			if (Conf.fichero_log) Logger.log_write("Guardar GIF: hacerCapturaUnica");
			//new FotografoArbol().hacerCapturaUnica(panelFotografia);
			new FotografoArbol().hacerCapturaUnica(grafo,numeroVista);
		}
	
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
		int code=e.getKeyCode();
		
		//System.out.println("keyReleased");
		if (code==KeyEvent.VK_DOWN || code==KeyEvent.VK_UP)
		{
			if (e.getSource().getClass().getName().contains("JRadioButton"))
				for (int i=0; i<botonesVistas.length; i++)
					if (botonesVistas[i].isFocusOwner())
					{
						if (code==KeyEvent.VK_DOWN)
						{
							if (i<botonesVistas.length-1)
								botonesVistas[i].transferFocus();
						}
						else if (code==KeyEvent.VK_UP)
						{
							if (i>0)
								botonesVistas[i].transferFocusBackward();
						}
					}
		}
		else if (code==KeyEvent.VK_ENTER)
		{
			accion();
		}
		else if (code==KeyEvent.VK_ESCAPE)
		{
			dialogo.setVisible(false);
		}
		
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
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseEntered(MouseEvent e) 
	{
	}

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseExited(MouseEvent e) 
	{
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mousePressed(MouseEvent e) 
	{
		
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseReleased(MouseEvent e)
	{
		if (e.getSource()==aceptar)
		{
			accion();
		}
		if (e.getSource()==cancelar)
		{
			dialogo.setVisible(false);
		}
	}
}
