package cuadros;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.TitledBorder;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import opciones.GestorOpciones;
import opciones.OpcionOpsVisualizacion;
import utilidades.Logger;
import utilidades.ServiciosString;
import utilidades.Texto;
import ventanas.Ventana;
import botones.BotonAceptar;
import conf.Conf;
import datos.*;



public class CuadroInfoNodo extends Thread implements ActionListener, KeyListener
{
	static final long serialVersionUID=07;

	final int NUM_DECIMALES=1;
	
	
	final int ANCHO_COLUMNA_ANCHA1=350;
	final int ANCHO_COLUMNA_ANCHA2= 70;
	
	final int ALTO_FILA=15;
	
	final int FILAS_INFO_1=7;
	
	final int anchoCuadroNW=290;		// No Windows
	final int altoCuadroNW=404;			// No Windows

	JDialog dialogo;
	Traza traza;
	RegistroActivacion nodo;
	
	BotonAceptar aceptar=new BotonAceptar();
	
	
	/**
		Constructor: genera un cuadro de información sobre el nodo suministrado
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroInfoNodo (Traza traza, RegistroActivacion nodo)
	{
		this.traza=traza;
		this.nodo=nodo;
		dialogo=new JDialog(Ventana.thisventana,true);
		this.start();
	}
	
	
	
	
	/**
		Constructor: genera un cuadro de información sobre el nodo activo
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroInfoNodo (Traza traza)
	{
		this.traza=traza;
		this.nodo=traza.getRegistroActivo();
		dialogo=new JDialog(Ventana.thisventana,true);
		this.start();
	}
	
	
	
	
	
	

	/**
		Genera una nueva opción
	*/
	
	public void run()
	{
		// Tamaño del cuadro de diálogo
		int anchoCuadro=0;
		int altoCuadro=0;
		
		
		// Panel Info general
		JPanel panelInfoGral = new JPanel();
		GridLayout gl1 = new GridLayout(FILAS_INFO_1,1);
		panelInfoGral.setLayout(gl1);
		
			// Etiquetas 
			JLabel infoGral1[]=new JLabel[FILAS_INFO_1];
			
			
			String estado=( (nodo.esMostradoEntero()) ? Texto.get("CINFONODO_EJEC",Conf.idioma) : Texto.get("CINFONODO_PEND",Conf.idioma) );
			estado=estado+( (nodo.esHistorico()) ? " ("+Texto.get("CINFONODO_HIST",Conf.idioma)+")" : "" );
			
			infoGral1[0]=new JLabel(Texto.get("CINFONODO_EST",Conf.idioma)+": "+estado);
			infoGral1[1]=new JLabel(Texto.get("CINFONODO_MET",Conf.idioma)+": "+nodo.interfazMetodo());

			estado="";
			if (nodo.numHijos()>0)
				if (nodo.getHijo(0).inhibido())
					estado=" ("+Texto.get("CINFONODO_INHIS",Conf.idioma)+")";
			
			infoGral1[2]=new JLabel(Texto.get("CINFONODO_SUBNUM",Conf.idioma)+": "+(nodo.getID()+1));
			infoGral1[3]=new JLabel(Texto.get("CINFONODO_INHI",Conf.idioma)+": "+
							(nodo.inhibido() ? Texto.get("SI",Conf.idioma): Texto.get("NO",Conf.idioma))  );
			
			infoGral1[4]=new JLabel(Texto.get("CINFONODO_HIJ",Conf.idioma)+": "+nodo.numHijos()+estado);
			infoGral1[5]=new JLabel(Texto.get("CINFONODO_HIJTV",Conf.idioma)+": "+nodo.getNumHijosVisibles());
			infoGral1[6]=new JLabel(Texto.get("CINFONODO_SUBNOD",Conf.idioma)+": "+nodo.numHijosRec());
			
			
			JPanel panelContGral1[]=new JPanel[FILAS_INFO_1];
			for (int i=0; i<panelContGral1.length; i++)
			{
				panelContGral1[i]=new JPanel();
				panelContGral1[i].setLayout(new BorderLayout());
				panelContGral1[i].add(infoGral1[i], BorderLayout.WEST);
				panelContGral1[i].setPreferredSize(new Dimension(ANCHO_COLUMNA_ANCHA1,ALTO_FILA));
			}
	
			
			// Empaquetamos paneles para poder situarlos en el cuadro de diálogo
			JPanel panelHorizGral[]=new JPanel[FILAS_INFO_1];
			
			for (int i=0; i<panelHorizGral.length; i++)
			{
				panelHorizGral[i]=new JPanel();
				panelHorizGral[i].setLayout(new BorderLayout());
				panelHorizGral[i].add(panelContGral1[i],BorderLayout.WEST);
			}		

			
			// Añadimos los paneles paquetizados al panel que irá en el cuadro de diálogo
			for (int i=0; i<panelHorizGral.length; i++)
				panelInfoGral.add(panelHorizGral[i]);

		panelInfoGral.setBorder(new TitledBorder(Texto.get("CINFONODO_NODSEL",Conf.idioma)));

		

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(aceptar);

		aceptar.addKeyListener(this);
		aceptar.addActionListener(this);
	
		
		// Panel general
		BorderLayout bl= new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);
	
		panel.add(panelInfoGral,BorderLayout.NORTH);
		panel.add(panelBoton,BorderLayout.SOUTH);
			
		// Preparamos y mostramos cuadro
		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("CINFONODO_TITULO",Conf.idioma));//Texto.get("COOV_OPSANIM",Conf.idioma));

		if (Ventana.thisventana.msWindows)
		{
			anchoCuadro=ANCHO_COLUMNA_ANCHA1+40;
			altoCuadro=(ALTO_FILA*FILAS_INFO_1)+100;			// parte de información general

			
			dialogo.setSize(anchoCuadro,altoCuadro);
			int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
			dialogo.setLocation(coord[0],coord[1]);
		}
		else
		{
			dialogo.setSize(anchoCuadroNW,altoCuadroNW);
			int coord[]=Conf.ubicarCentro(anchoCuadroNW,altoCuadroNW);
			dialogo.setLocation(coord[0],coord[1]);
		}
		
		
		
		if (Conf.fichero_log)
		{
			String mensaje="Información > Información nodo actual...: N."+nodo.getID()+"   "+nodo.getNombreMetodo()+"(";
			String paramE[]=nodo.getEntradasString();
			String paramS[]=nodo.getSalidasString();
			
			for (int i=0; i<paramE.length; i++)
			{
				if (i!=0)
					mensaje+=",";
				mensaje+=paramE[i];
			}
			mensaje+=") [";
			
			for (int i=0; i<paramS.length; i++)
			{
				if (i!=0)
					mensaje+=",";
				mensaje+=paramS[i];
			}
			mensaje+="]";
			
			Logger.log_write(mensaje);
		}
		
		
		
		
		
		dialogo.setResizable(false);
		dialogo.setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		this.dialogo.setVisible(false);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {

		
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode()==KeyEvent.VK_ENTER || e.getKeyCode()==KeyEvent.VK_ESCAPE)
			this.dialogo.setVisible(false);
	}

	@Override
	public void keyTyped(KeyEvent e) {

		
	}

}


