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
import utilidades.ServiciosString;
import utilidades.Texto;
import ventanas.Ventana;
import botones.BotonAceptar;
import conf.Conf;
import datos.*;



public class CuadroInfoTraza extends Thread implements ActionListener, KeyListener
{
	static final long serialVersionUID=07;

	final int NUM_DECIMALES=1;
	
	
	final int ANCHO_COLUMNA_ANCHA1=280;
	final int ANCHO_COLUMNA_ANCHA2= 70;
	
	final int ALTO_FILA=15;
	
	
	
	final int FILAS_INFO_1=8;
	
	final int anchoCuadroNW=290;		// No Windows
	final int altoCuadroNW=404;			// No Windows

	JDialog dialogo;
	Traza traza;
	Traza trazaCompleta;
	
	BotonAceptar aceptar=new BotonAceptar();
	
	
	/**
		Constructor: genera un nuevo cuadro de opción
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroInfoTraza (Traza traza, Traza trazaCompleta)
	{
		this.traza=traza;
		this.trazaCompleta=trazaCompleta;
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
		
		
		
		
		// Estadísticas 
		int numNodos=traza.getNumNodos();
		int numNodosT=trazaCompleta.getNumNodos();
		
		int numNodosVisibles=traza.getNumNodosVisibles();
		int numNodosHistoricos=traza.getNumNodosHistoricos();
		int numNodosAunNoMostrados=traza.getNumNodosAunNoMostrados();
		int numNodosInhibidos=traza.getNumNodosInhibidos();
		
		int numNodosOcultos=numNodosT-numNodos;
		int numNodosResaltados=traza.getNumNodosIluminados();

		// Nombres de Métodos
		String []metodos=trazaCompleta.getInterfacesMetodos();
		
		//for (int i=0; i<metodos.length; i++)
		//	System.out.println("> "+metodos[i]);
		
		
		
		
		// Panel Info general
		JPanel panelInfoGral = new JPanel();
		GridLayout gl1 = new GridLayout(FILAS_INFO_1,1);
		panelInfoGral.setLayout(gl1);
		
			// Etiquetas para columna de la izquierda (identificaciones)
			JLabel infoGral1[]=new JLabel[FILAS_INFO_1];
			
			infoGral1[0]=new JLabel("");
			infoGral1[1]=new JLabel(Texto.get("CINFOANIM_NODTOT",Conf.idioma));
			infoGral1[2]=new JLabel(Texto.get("CINFOANIM_NODVIS",Conf.idioma));
			infoGral1[3]=new JLabel(Texto.get("CINFOANIM_NODHIST",Conf.idioma));
			infoGral1[4]=new JLabel(Texto.get("CINFOANIM_NODINHIB",Conf.idioma));
			infoGral1[5]=new JLabel(Texto.get("CINFOANIM_NODNOMOSTAUN",Conf.idioma));
			infoGral1[6]=new JLabel(Texto.get("CINFOANIM_NODOCULT",Conf.idioma));
			infoGral1[7]=new JLabel(Texto.get("CINFOANIM_NODOILUM",Conf.idioma));
			
			JPanel panelContGral1[]=new JPanel[FILAS_INFO_1];
			for (int i=0; i<panelContGral1.length; i++)
			{
				panelContGral1[i]=new JPanel();
				panelContGral1[i].setLayout(new BorderLayout());
				panelContGral1[i].add(infoGral1[i], BorderLayout.WEST);
				panelContGral1[i].setPreferredSize(new Dimension(ANCHO_COLUMNA_ANCHA1,ALTO_FILA));
			}
	
			// Etiquetas para columna del centro (núm. nodos)
			JLabel infoGral2[]=new JLabel[FILAS_INFO_1];
	
			infoGral2[0]=new JLabel(Texto.get("CINFOANIM_NOD",Conf.idioma));
			infoGral2[1]=new JLabel(""+numNodosT);
			infoGral2[2]=new JLabel(""+numNodosVisibles);
			infoGral2[3]=new JLabel(""+numNodosHistoricos);
			infoGral2[4]=new JLabel(""+numNodosInhibidos);
			infoGral2[5]=new JLabel(""+numNodosAunNoMostrados);
			infoGral2[6]=new JLabel(""+numNodosOcultos);
			infoGral2[7]=new JLabel(""+numNodosResaltados);
			
			JPanel panelContGral2[]=new JPanel[FILAS_INFO_1];
			for (int i=0; i<panelContGral2.length; i++)
			{
				panelContGral2[i]=new JPanel();
				panelContGral2[i].setLayout(new BorderLayout());
				panelContGral2[i].add(infoGral2[i], BorderLayout.EAST);
				panelContGral2[i].setPreferredSize(new Dimension(ANCHO_COLUMNA_ANCHA2,ALTO_FILA));
			}
			
			// Etiquetas para columna del centro (núm. nodos)		
			JLabel infoGral3[]=new JLabel[FILAS_INFO_1];
	
			infoGral3[0]=new JLabel("% "+Texto.get("CINFOANIM_NOD",Conf.idioma));
			infoGral3[1]=new JLabel("100");
			infoGral3[2]=new JLabel(""+ServiciosString.truncarNumero( (numNodosVisibles*100.0/numNodosT) , NUM_DECIMALES ));
			infoGral3[3]=new JLabel(""+ServiciosString.truncarNumero( (numNodosHistoricos*100.0/numNodosT) , NUM_DECIMALES ));
			infoGral3[4]=new JLabel(""+ServiciosString.truncarNumero( (numNodosInhibidos*100.0/numNodosT) , NUM_DECIMALES ));
			infoGral3[5]=new JLabel(""+ServiciosString.truncarNumero( (numNodosAunNoMostrados*100.0/numNodosT) , NUM_DECIMALES ));
			infoGral3[6]=new JLabel(""+ServiciosString.truncarNumero( (numNodosOcultos*100.0/numNodosT) , NUM_DECIMALES ));
			infoGral3[7]=new JLabel(""+ServiciosString.truncarNumero( (numNodosResaltados*100.0/numNodosT) , NUM_DECIMALES ));
			
			JPanel panelContGral3[]=new JPanel[FILAS_INFO_1];
			for (int i=0; i<panelContGral3.length; i++)
			{
				panelContGral3[i]=new JPanel();
				panelContGral3[i].setLayout(new BorderLayout());
				panelContGral3[i].add(infoGral3[i], BorderLayout.EAST);
				panelContGral3[i].setPreferredSize(new Dimension(ANCHO_COLUMNA_ANCHA2,ALTO_FILA));
			}	
			
			
			// Empaquetamos paneles para poder situarlos en el cuadro de diálogo
			JPanel panelHorizGral[]=new JPanel[FILAS_INFO_1];
			
			for (int i=0; i<panelHorizGral.length; i++)
			{
				panelHorizGral[i]=new JPanel();
				panelHorizGral[i].setLayout(new BorderLayout());
				panelHorizGral[i].add(panelContGral1[i],BorderLayout.WEST);
				panelHorizGral[i].add(panelContGral2[i],BorderLayout.CENTER);
				panelHorizGral[i].add(panelContGral3[i],BorderLayout.EAST);
			}
			
			// Añadimos los paneles paquetizados al panel que irá en el cuadro de diálogo
			for (int i=0; i<panelHorizGral.length; i++)
				panelInfoGral.add(panelHorizGral[i]);

		panelInfoGral.setBorder(new TitledBorder(Texto.get("CINFOANIM_VG",Conf.idioma)));

		// Panel Metodos implicados
		JPanel panelMetodos = new JPanel();
		GridLayout gl2 = new GridLayout(metodos.length,1);
		panelMetodos.setLayout(gl2);
		
		// Etiquetas para las interfaces
		JLabel infoMetodos[]=new JLabel[metodos.length];
		for (int i=0; i<infoMetodos.length; i++)
		{
			infoMetodos[i]=new JLabel(metodos[i]);
		}
				
		JPanel panelMetodo[]=new JPanel[metodos.length];
		for (int i=0; i<panelMetodo.length; i++)
		{
			panelMetodo[i]=new JPanel();
			panelMetodo[i].setLayout(new BorderLayout());
			panelMetodo[i].add(infoMetodos[i], BorderLayout.WEST);
			panelMetodo[i].setPreferredSize(new Dimension(ANCHO_COLUMNA_ANCHA2,ALTO_FILA));
		}
		
		for (int i=0; i<panelMetodo.length; i++)
			panelMetodos.add(panelMetodo[i]);
		
		panelMetodos.setBorder(new TitledBorder(Texto.get("CINFOANIM_MI",Conf.idioma)));

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(aceptar);


		aceptar.addActionListener(this);
		aceptar.addKeyListener(this);
	
		
		// Panel general
		BorderLayout bl= new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);
	
		panel.add(panelInfoGral,BorderLayout.NORTH);
		panel.add(panelMetodos,BorderLayout.CENTER);
		panel.add(panelBoton,BorderLayout.SOUTH);
			
		// Preparamos y mostramos cuadro
		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("CINFOANIM_TITULO",Conf.idioma));//Texto.get("COOV_OPSANIM",Conf.idioma));

		if (Ventana.thisventana.msWindows)
		{
			anchoCuadro=ANCHO_COLUMNA_ANCHA1+(ANCHO_COLUMNA_ANCHA2*2)+40;
			altoCuadro=(ALTO_FILA*FILAS_INFO_1)+130			// parte de información general
					+(ALTO_FILA*metodos.length);
			
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


