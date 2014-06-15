/**
	Representa la clase del cuadro que maneja la opción que gestiona la configuración de la visualización
		
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/


package cuadros;




import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;



import conf.*;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;


public class CuadroOpcionConfVisualizacion extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=07;
	//private final boolean DEPURAR=false;

	final int anchoCuadro=900;
	final int altoCuadro=485;
	final int anchoCuadroNW=900;		// No Windows
	final int altoCuadroNW=465; 		// No Windows
	
	OpcionConfVisualizacion ocv=null;
	GestorOpciones gOpciones=new GestorOpciones();

	ButtonGroup grupoBotones;
	JCheckBox[] botones;
	
	JCheckBox checkModo1;
	JCheckBox checkModo2;

	String [] valores;
	
	BorderLayout bl, bl01,bl021;
	GridLayout gl0,gl011,gl012,gl02;
	
	JTabbedPane pestanas;
	JDialog dialogo;

	BotonAceptar aceptar=new BotonAceptar();
	BotonCancelar cancelar=new BotonCancelar();
	
	JPanel panelEntrada, panelSalida, panelFlecha, panel;

	
	JLabel colores[][]=new JLabel[2][];
	//JPanel panelesColores[][]=new JPanel[2][];
	
	JLabel colorFlecha;
	JLabel colorPanel;
	JPanel panelColoresFlecha;
	JPanel panelColoresPanel;
	
	JCheckBox colorDegradado[]=new JCheckBox[2];	// [0] para modo 1 y [1] para modo 2
	JComboBox grosorFlecha, tipoFlecha;
	JComboBox grosorMarco, bordeCelda;
	JComboBox sepHorizontal, sepVertical;
	JComboBox fuentesCodigo, fuentesTraza;
	JComboBox fuentesTamCodigo, fuentesTamTraza;

	// nuevo
	
	JColorChooser jcc1,jcc2,jcc3;
	
	private final int numSelectores1=5;							// Num. selectores en pestaña 1
	private final int numSelectores2=Conf.numColoresMetodos;	// Num. selectores en pestaña 2
	private final int numSelectores3=13;						// Num. selectores en pestaña 3
	
	JRadioButton selectores1[]=new JRadioButton[numSelectores1];
	JRadioButton selectores2[]=new JRadioButton[numSelectores2];
	JRadioButton selectores3[]=new JRadioButton[numSelectores3];
	JLabel etiqColores1[]=new JLabel[numSelectores1];
	JLabel etiqColores2[]=new JLabel[numSelectores2];
	JLabel etiqColores3[]=new JLabel[numSelectores3];
	
	JPanel panelesColores1[]=new JPanel[numSelectores1];
	JPanel panelesColores2[]=new JPanel[numSelectores2];
	JPanel panelesColores3[]=new JPanel[numSelectores3];
	
	BotonTexto asignar1, resetear1,asignar2,resetear2,asignar3,resetear3;
	
	static String creandoPanel=Texto.get("CP_CREARPAN",Conf.idioma);
	static String marcarPanel=Texto.get("COCV_MARCAR",Conf.idioma);
	
	CuadroProgreso cp;
	
	boolean creandoCuadro=false;
	
	/**
		Constructor: genera un nuevo cuadro de opción
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroOpcionConfVisualizacion ()
	{	
		dialogo=new JDialog(Ventana.thisventana,true);
		this.start();
	}

	/**
		Genera una nueva opción
	*/
	
	public void run()
	{

	
		// Haremos una búsqueda "colectiva" para ahorrarnos cargar el fichero Textos.xml varias veces desde el sistema de archivos
		String [] codigos= 	
			{
				"COCV_SELEC", 			"COCV_FUENTEE", 		"COCV_CELDE1", 			"COCV_CELDEA1",			"COCV_FUENTES",
				"COCV_CELDS1", 			"COCV_CELDSA1", 		"COCV_CELDSNC1", 		"COCV_FONDOPANEL",		"COCV_FLECHA",
				"COCV_MARCONODACT", 	"COCV_MARCOCAMACT", 	"COCV_MARCAR", 			"COCV_HABDEGR",			"COCV_HABDEGR_TTT",
				"BOTONASIGNAR",			"BOTONDESCARTAR",		"COCV_ETIQ_1",			"COCV_PALRESERV",		"COCV_COMENT",
				"COCV_MET",				"COCV_METFON",			"COCV_CODIGO",			"COCV_HABILDEGR",		"COCV_ETIQ_3",
				"COCV_COL_ILUM",		"COCV_TRENA",/*26*/		"COCV_TRSAA",/*27*/		"COCV_TRPANEL",/*28*/	"COCV_SUBORDCOL",
				"COCV_SUBORDCOLTR",		"COCV_SUBORDCOLCO",		"COCV_SUBORDCOLCE",		"COCV_ETIQ_2",			"COCV_CONFFLEC",
				"COCV_CONFFLECANCH",	"COCV_CONFFLECTIPO",	"COCV_CONFFLECTIPONEU",	"COCV_CONFFLECTIPOTEC",	"COCV_CONFFLECTIPOCLA",
				"COCV_CONFFLECTIPOSIM",	"COCV_CONFFLECTIPOROM",	"COCV_CONFFLECTIPOSLI",	"COCV_CONFFLECTIPODLI",	"COCV_CONFFLECTIPOCIR",
				"COCV_CARACCELD",		"COCV_CARACCELDMARC",	"COCV_CARACCELDMARCT",	"COCV_CARACCELDBORDV",	"COCV_CARACCELDBORDB",
				"COCV_CARACCELDBORDG",	"COCV_CARACCELDBORDL",	"COCV_CARACCELDBORDBB",	"COCV_CARACCELDBORDBL",	"COCV_DISTCELD",
				"COCV_DISTCELDH",		"COCV_DISTCELDV",		"COCV_FUENTCOD",		"COCV_FUENT",			"COCV_FUENTTAM",
				"COCV_FUENTTR",			"COCV_ETIQ_4",	"COCV_ETIQ_ARBPILCODTRATTT",	"COCV_ETIQ_TAMFORTTT",	"COCV_FORMATO",
				"COCV_COL_RESA"
				
				// 26 a 28 no se están usando
				// 29 a 32 se podrían no estar usando (comprobar)
			};
		
		String textos[]=Texto.get(codigos,Conf.idioma);
		
		
	
	
	
	
	
		// Cargar opción desde fichero (si no existe, todo true, para que se borre todo)
		//obf = new OpcionBorradoFicheros(); // mientras se implementa...

		cp = new CuadroProgreso(Ventana.thisventana,Texto.get("CP_ESPERE",Conf.idioma),creandoPanel,0);
							
		this.creandoCuadro=true;		// Evita refrescos innecesarios en ventanas al formar cuadro de diálogo
		
		ocv = (OpcionConfVisualizacion)gOpciones.getOpcion("OpcionConfVisualizacion",false);
		
		cp.setValores(creandoPanel,10);
		// Creamos estructura de pestañas
		pestanas=new JTabbedPane();
		pestanas.addMouseListener(this);
		pestanas.addKeyListener(this);
		
		// PESTAÑA 1 . MODO DE COLORES QUE DISTINGUE ENTRE ENTRADA Y SALIDA EN CADA NODO
		
		// Panel Izquierda
		JPanel panelIzquierda=new JPanel();
		panelIzquierda.setLayout(new BorderLayout());
		
			// Panel checkbox modo
			JPanel pCheckModo1=new JPanel();
			checkModo1=new JCheckBox(Texto.get("COCV_MODO1",Conf.idioma));
			pCheckModo1.add(checkModo1);
			checkModo1.addActionListener(this);
			checkModo1.addMouseListener(this);
			checkModo1.addKeyListener(this);
			
		
			// Panel colores
			JPanel panelSeleccionColores=new JPanel();
			panelSeleccionColores.setBorder(new TitledBorder(textos[0]));
			panelSeleccionColores.setLayout(new GridLayout(numSelectores1,1));
		
			ButtonGroup bg1 = new ButtonGroup();

			selectores1[ 0]=new JRadioButton(textos[2]);
			selectores1[ 1]=new JRadioButton(textos[3]);
			
			selectores1[ 2]=new JRadioButton(textos[5]);
			selectores1[ 3]=new JRadioButton(textos[6]);
			selectores1[ 4]=new JRadioButton(textos[7]);
			
			selectores1[0].setSelected(true);

			for (int i=0; i<numSelectores1; i++)
			{
				selectores1[i].addKeyListener(this);
				selectores1[i].addActionListener(this);
				selectores1[i].addMouseListener(this);
				selectores1[i].setToolTipText(textos[12]);
				bg1.add(selectores1[i]);
				panelSeleccionColores.add(selectores1[i]);
			}
			
			cp.setValores(creandoPanel,10);
			JPanel panelFila[]=new JPanel[numSelectores1];
			for (int i=0; i<numSelectores1; i++)
			{
				panelFila[i]=new JPanel();
				panelFila[i].setLayout(new BorderLayout());
				panelFila[i].add(selectores1[i],BorderLayout.WEST);
				etiqColores1[i]=new JLabel("");
				etiqColores1[i].addMouseListener(this);
				etiqColores1[i].setForeground(new Color(0,0,0));
				etiqColores1[i].setBackground(new Color(0,0,0));
				panelesColores1[i]=creaPanelColor(etiqColores1[i]);
				panelesColores1[i].addMouseListener(this);
				panelFila[i].add(panelesColores1[i],BorderLayout.EAST);
				panelSeleccionColores.add(panelFila[i]);
			}
			panelSeleccionColores.setPreferredSize(new Dimension(270,300));
			//JPanel panelContenedorBotones=new JPanel();
	
			
			// Panel Degradados
			JPanel panelDegradados1=new JPanel();
			panelDegradados1.setBorder(new TitledBorder(textos[13]));
			panelDegradados1.setLayout(new GridLayout(1,1));
			colorDegradado[0]=new JCheckBox(textos[23]);
			panelDegradados1.add(colorDegradado[0]);
			colorDegradado[0].addActionListener(this);
			colorDegradado[0].setToolTipText(textos[14]);
			
		panelIzquierda.add(pCheckModo1,BorderLayout.NORTH);
		panelIzquierda.add(panelSeleccionColores,BorderLayout.CENTER);
		panelIzquierda.add(panelDegradados1,BorderLayout.SOUTH);
			
		cp.setValores(creandoPanel,15);	
			
		// Panel Derecha
		JPanel panelDerecha=new JPanel();
		panelDerecha.setLayout(new BorderLayout());
			
			// JColorChooser
			jcc1 = new JColorChooser(new Color(0,0,0));
			jcc1.addMouseListener(this);
		
			// Panel Botones Asignar y Borrar
			JPanel panelBotonesDerecha=new JPanel();
			asignar1 = new BotonTexto(textos[15]);
			resetear1 = new BotonTexto(textos[16]);
			
			panelBotonesDerecha.add(asignar1);
			panelBotonesDerecha.add(resetear1);
			
			asignar1.addMouseListener(this);
			resetear1.addMouseListener(this);
			
			asignar1.addKeyListener(this);
			resetear1.addKeyListener(this);
		
		panelDerecha.add(jcc1,BorderLayout.NORTH);		
		panelDerecha.add(panelBotonesDerecha,BorderLayout.SOUTH);
		
		// Panel pestana color
		JPanel panelPestana1=new JPanel();
		panelPestana1.setLayout(new BorderLayout());
		panelPestana1.add(panelIzquierda,BorderLayout.WEST);
		panelPestana1.add(panelDerecha,BorderLayout.EAST);
		
		pestanas.add(textos[17],panelPestana1);
		
		cp.setValores(creandoPanel,20);
		// PESTAÑA 2 . MODO DE COLORES QUE DISTINGUE ENTRE NODOS DE MÉTODOS
		
		// Panel Izquierda
		JPanel panelIzquierda2=new JPanel();
		panelIzquierda2.setLayout(new BorderLayout());
		
			// Panel checkbox modo
			JPanel pCheckModo2=new JPanel();
			checkModo2=new JCheckBox(Texto.get("COCV_MODO2",Conf.idioma));
			pCheckModo2.add(checkModo2);
			checkModo2.addActionListener(this);
			checkModo2.addMouseListener(this);
			checkModo2.addKeyListener(this);
			
			// Panel colores
			JPanel panelSeleccionColores2=new JPanel();
			panelSeleccionColores2.setBorder(new TitledBorder(textos[0]));
			panelSeleccionColores2.setLayout(new GridLayout(numSelectores2,1));
			
			ButtonGroup bg2 = new ButtonGroup();
			selectores2[ 0]=new JRadioButton(Texto.get("COCV_COLOR", Conf.idioma)+" 1 ("+Texto.get("CMP_METPRINC",Conf.idioma)+")");
			selectores2[ 1]=new JRadioButton(Texto.get("COCV_COLOR", Conf.idioma)+" 2");
			selectores2[ 2]=new JRadioButton(Texto.get("COCV_COLOR", Conf.idioma)+" 3");
			selectores2[ 3]=new JRadioButton(Texto.get("COCV_COLOR", Conf.idioma)+" 4");
			selectores2[ 4]=new JRadioButton(Texto.get("COCV_COLOR", Conf.idioma)+" 5");
			selectores2[ 5]=new JRadioButton(Texto.get("COCV_COLOR", Conf.idioma)+" 6");
			selectores2[ 6]=new JRadioButton(Texto.get("COCV_COLOR", Conf.idioma)+" 7");
			selectores2[ 7]=new JRadioButton(Texto.get("COCV_COLOR", Conf.idioma)+" 8");
			selectores2[ 8]=new JRadioButton(Texto.get("COCV_COLOR", Conf.idioma)+" 9");
			selectores2[ 9]=new JRadioButton(Texto.get("COCV_COLOR", Conf.idioma)+" 10");
			//selectores2[10]=new JRadioButton(textos[28]);
			selectores2[0].setSelected(true);

			for (int i=0; i<numSelectores2; i++)
			{
				selectores2[i].addKeyListener(this);
				selectores2[i].addActionListener(this);
				selectores2[i].addMouseListener(this);
				selectores2[i].setToolTipText(textos[12]);
				bg2.add(selectores2[i]);
				panelSeleccionColores2.add(selectores2[i]);
			}
			
			cp.setValores(creandoPanel,30);
			
			JPanel panelFila2[]=new JPanel[numSelectores2];
			for (int i=0; i<numSelectores2; i++)
			{
				panelFila2[i]=new JPanel();
				panelFila2[i].setLayout(new BorderLayout());
				panelFila2[i].add(selectores2[i],BorderLayout.WEST);
				etiqColores2[i]=new JLabel("");
				etiqColores2[i].addMouseListener(this);
				panelesColores2[i]=creaPanelColor(etiqColores2[i]);
				panelesColores2[i].addMouseListener(this);
				panelFila2[i].add(panelesColores2[i],BorderLayout.EAST);
				panelSeleccionColores2.add(panelFila2[i]);
			}
			panelSeleccionColores2.setPreferredSize(new Dimension(270,300));

			// Panel degradados
			JPanel panelDegradados2=new JPanel();
			panelDegradados2.setBorder(new TitledBorder(textos[13]));
			panelDegradados2.setLayout(new GridLayout(1,1));
			colorDegradado[1]=new JCheckBox(textos[23]);
			panelDegradados2.add(colorDegradado[1]);
			colorDegradado[1].addActionListener(this);
			colorDegradado[1].setToolTipText(textos[14]);
			colorDegradado[1].addKeyListener(this);

			
		panelIzquierda2.add(pCheckModo2,BorderLayout.NORTH);	
		panelIzquierda2.add(panelSeleccionColores2,BorderLayout.CENTER);
		panelIzquierda2.add(panelDegradados2,BorderLayout.SOUTH);
			
		cp.setValores(creandoPanel,35);	
			
		// Panel Derecha
		JPanel panelDerecha2=new JPanel();
		panelDerecha2.setLayout(new BorderLayout());
			
			// JColorChooser
			jcc2 = new JColorChooser(new Color(0,0,0));
			jcc2.addMouseListener(this);
			
		
			// Panel Botones Asignar y Borrar
			JPanel panelBotonesDerecha2=new JPanel();
			asignar2 = new BotonTexto(textos[15]);
			resetear2 = new BotonTexto(textos[16]);
			
			panelBotonesDerecha2.add(asignar2);
			panelBotonesDerecha2.add(resetear2);
			
			asignar2.addMouseListener(this);
			resetear2.addMouseListener(this);
			
			asignar2.addKeyListener(this);
			resetear2.addKeyListener(this);
		
		panelDerecha2.add(jcc2,BorderLayout.NORTH);		
		panelDerecha2.add(panelBotonesDerecha2,BorderLayout.SOUTH);
		
		// Panel pestana color
		JPanel panelPestana2=new JPanel();
		panelPestana2.setLayout(new BorderLayout());
		panelPestana2.add(panelIzquierda2,BorderLayout.WEST);
		panelPestana2.add(panelDerecha2,BorderLayout.EAST);
		
		pestanas.add(textos[33],panelPestana2);
		

		cp.setValores(creandoPanel,40);
		
		
		// PESTAÑA 3 . OTROS COLORES
		
		// Panel Izquierda
		JPanel panelIzquierda3=new JPanel();
		panelIzquierda3.setLayout(new BorderLayout());
		
			// Panel colores
			JPanel panelSeleccionColores3=new JPanel();
			panelSeleccionColores3.setBorder(new TitledBorder(textos[0]));
			panelSeleccionColores3.setLayout(new GridLayout(numSelectores3,1));
		
			ButtonGroup bg3 = new ButtonGroup();
			selectores3[ 0]=new JRadioButton(textos[1]);

			selectores3[ 1]=new JRadioButton(textos[4]);


			selectores3[ 2]=new JRadioButton(textos[8]);
			selectores3[ 3]=new JRadioButton(textos[9]);
			selectores3[ 4]=new JRadioButton(textos[10]);
			selectores3[ 5]=new JRadioButton(textos[11]);
			
			selectores3[ 6]=new JRadioButton(textos[18]);
			selectores3[ 7]=new JRadioButton(textos[19]);
			selectores3[ 8]=new JRadioButton(textos[20]);
			selectores3[ 9]=new JRadioButton(textos[21]);
			selectores3[10]=new JRadioButton(textos[22]);
			selectores3[11]=new JRadioButton(textos[25]);
			selectores3[12]=new JRadioButton(textos[65]);
			
			
			selectores3[0].setSelected(true);

			for (int i=0; i<numSelectores3; i++)
			{
				selectores3[i].addKeyListener(this);
				selectores3[i].addActionListener(this);
				selectores3[i].addMouseListener(this);
				selectores3[i].setToolTipText(textos[12]);
				bg3.add(selectores3[i]);
				panelSeleccionColores3.add(selectores3[i]);
			}
			
			cp.setValores(creandoPanel,45);
			JPanel panelFila3[]=new JPanel[numSelectores3];
			for (int i=0; i<numSelectores3; i++)
			{
				panelFila3[i]=new JPanel();
				panelFila3[i].setLayout(new BorderLayout());
				panelFila3[i].add(selectores3[i],BorderLayout.WEST);
				etiqColores3[i]=new JLabel("");
				etiqColores3[i].addMouseListener(this);
				etiqColores3[i].setForeground(new Color(0,0,0));
				etiqColores3[i].setBackground(new Color(0,0,0));
				panelesColores3[i]=creaPanelColor(etiqColores3[i]);
				panelesColores3[i].addMouseListener(this);
				panelFila3[i].add(panelesColores3[i],BorderLayout.EAST);
				panelSeleccionColores3.add(panelFila3[i]);
			}
			panelSeleccionColores3.setPreferredSize(new Dimension(270,300));
			//JPanel panelContenedorBotones=new JPanel();
	
			
		panelIzquierda3.add(panelSeleccionColores3,BorderLayout.CENTER);
		//panelIzquierda3.add(panelDegradados3,BorderLayout.SOUTH);
			
		cp.setValores(creandoPanel,55);	
			
		// Panel Derecha
		JPanel panelDerecha3=new JPanel();
		panelDerecha3.setLayout(new BorderLayout());
			
			// JColorChooser
			jcc3 = new JColorChooser(new Color(0,0,0));
			jcc3.addMouseListener(this);
		
			// Panel Botones Asignar y Borrar
			JPanel panelBotonesDerecha3=new JPanel();
			asignar3 = new BotonTexto(textos[15]);
			resetear3 = new BotonTexto(textos[16]);
			
			panelBotonesDerecha3.add(asignar3);
			panelBotonesDerecha3.add(resetear3);
			
			asignar3.addMouseListener(this);
			resetear3.addMouseListener(this);
			
			asignar3.addKeyListener(this);
			resetear3.addKeyListener(this);
		
		panelDerecha3.add(jcc3,BorderLayout.NORTH);		
		panelDerecha3.add(panelBotonesDerecha3,BorderLayout.SOUTH);
		
		// Panel pestana color
		JPanel panelPestana3=new JPanel();
		panelPestana3.setLayout(new BorderLayout());
		panelPestana3.add(panelIzquierda3,BorderLayout.WEST);
		panelPestana3.add(panelDerecha3,BorderLayout.EAST);
		
		pestanas.add(textos[24],panelPestana3);
		
		cp.setValores(creandoPanel,60);
		
		
		
		
		
		
		// PESTAÑA 4 . TAMAÑOS Y FORMAS
		JPanel panelPestana22=new JPanel();
		panelPestana22.setLayout(new BorderLayout());
		
		
		
		JPanel panelSuperior2=new JPanel();
		panelSuperior2.setLayout(new BorderLayout());
		JPanel panelInferior2=new JPanel();
		panelInferior2.setLayout(new BorderLayout());
		
			// Panel flechas
			JPanel panelFlecha=new JPanel();
			panelFlecha.setBorder(new TitledBorder(textos[34]));
			panelFlecha.setLayout(new BorderLayout());
			
				// Panel JComboBoxes
				JPanel panelJCBs = new JPanel();
				panelJCBs.setLayout(new BorderLayout());
				
				// Panel etiquetas
				JPanel panelEtiquetas = new JPanel();
				panelEtiquetas.setLayout(new GridLayout(4,1));
				
				JLabel etiqGrosor = new JLabel(textos[35]+"    ");
				JLabel etiqTipo = new JLabel(textos[36]);
				
				panelEtiquetas.add(etiqGrosor);
				panelEtiquetas.add(new JPanel());
				panelEtiquetas.add(etiqTipo);
				panelEtiquetas.add(new JPanel());
				
				// Panel JComboBoxes
				JPanel panelDesplegables = new JPanel();
				panelDesplegables.setLayout(new GridLayout(4,1));
				
				grosorFlecha = new JComboBox();
				for (int i=0; i<10; i++)
					grosorFlecha.addItem((i+1)+"");
				grosorFlecha.addActionListener(this);
				grosorFlecha.addKeyListener(this);
				grosorFlecha.setAlignmentX(Component.RIGHT_ALIGNMENT);
				tipoFlecha = new JComboBox();
				tipoFlecha.addItem(textos[37]);
				tipoFlecha.addItem(textos[38]);
				tipoFlecha.addItem(textos[39]);
				tipoFlecha.addItem(textos[40]);
				tipoFlecha.addItem(textos[41]);
				tipoFlecha.addItem(textos[42]);
				tipoFlecha.addItem(textos[43]);
				tipoFlecha.addItem(textos[44]);
				tipoFlecha.addActionListener(this);
				tipoFlecha.addKeyListener(this);
				
				grosorFlecha.setPreferredSize(new Dimension(75,19));
				tipoFlecha.setPreferredSize(new Dimension(75,19));
				
				panelDesplegables.add(grosorFlecha);
				panelDesplegables.add(new JPanel());
				panelDesplegables.add(tipoFlecha);
				panelDesplegables.add(new JPanel());

				panelJCBs.add(panelEtiquetas,BorderLayout.WEST);
				panelJCBs.add(panelDesplegables,BorderLayout.EAST);
				
				// Panel etiquetaImagen
				JPanel panelEtiquetaImagen= new JPanel();
				JLabel etiqImagen = new JLabel(new ImageIcon("./imagenes/cuadroconf_flechas.gif"));
				panelEtiquetaImagen.add(etiqImagen);

			panelFlecha.add(panelJCBs,BorderLayout.NORTH);
			panelFlecha.add(panelEtiquetaImagen,BorderLayout.CENTER);
			
			cp.setValores(creandoPanel,65);
			
			// Panel características 
			JPanel panelCaracteristicas=new JPanel();
			panelCaracteristicas.setBorder(new TitledBorder(textos[45]));
			panelCaracteristicas.setLayout(new BorderLayout());
			
				// Panel JComboBoxes
				JPanel panelJCBs2 = new JPanel();
				panelJCBs2.setLayout(new BorderLayout());
				
				// Panel etiquetas
				JPanel panelEtiquetas2 = new JPanel();
				panelEtiquetas2.setLayout(new GridLayout(4,1));
				
				JLabel etiqGrosorMarco = new JLabel(textos[46]+"   ");
				JLabel etiqTipoBorde = new JLabel(textos[47]);
				
				panelEtiquetas2.add(etiqGrosorMarco);
				panelEtiquetas2.add(new JPanel());
				panelEtiquetas2.add(etiqTipoBorde);
				panelEtiquetas2.add(new JPanel());
				
				// Panel JComboBoxes
				JPanel panelDesplegables2 = new JPanel();
				panelDesplegables2.setLayout(new GridLayout(4,1));
				
				grosorMarco = new JComboBox();
				for (int i=0; i<9; i++)
					grosorMarco.addItem((i)+"");
				grosorMarco.addActionListener(this);
				grosorMarco.addKeyListener(this);

				bordeCelda = new JComboBox();
				bordeCelda.addItem(textos[48]);
				bordeCelda.addItem(textos[49]);
				bordeCelda.addItem(textos[50]);
				bordeCelda.addItem(textos[51]);
				bordeCelda.addItem(textos[52]);
				bordeCelda.addItem(textos[53]);
				bordeCelda.addActionListener(this);
				bordeCelda.addKeyListener(this);
				
				grosorMarco.setPreferredSize(new Dimension(75,19));
				bordeCelda.setPreferredSize(new Dimension(75,19));
				
				panelDesplegables2.add(grosorMarco);
				panelDesplegables2.add(new JPanel());
				panelDesplegables2.add(bordeCelda);
				panelDesplegables2.add(new JPanel());

				panelJCBs2.add(panelEtiquetas2,BorderLayout.WEST);
				panelJCBs2.add(panelDesplegables2,BorderLayout.EAST);
				
				// Panel etiquetaImagen
				JPanel panelEtiquetaImagen2= new JPanel();
				JLabel etiqImagen2 = new JLabel(new ImageIcon("./imagenes/cuadroconf_bordes.gif"));
				panelEtiquetaImagen2.add(etiqImagen2);

			panelCaracteristicas.add(panelJCBs2,BorderLayout.NORTH);
			panelCaracteristicas.add(panelEtiquetaImagen2,BorderLayout.CENTER);
			
			cp.setValores(creandoPanel,75);
			
			// Panel distancias 
			JPanel panelDistancias=new JPanel();
			panelDistancias.setBorder(new TitledBorder(textos[54]));
			panelDistancias.setLayout(new BorderLayout());
			
				// Panel JComboBoxes
				JPanel panelJCBs3 = new JPanel();
				panelJCBs3.setLayout(new BorderLayout());
				
				// Panel etiquetas
				JPanel panelEtiquetas3 = new JPanel();
				panelEtiquetas3.setLayout(new GridLayout(4,1));
				
				JLabel etiqSepHorizontal = new JLabel(textos[55]+"    ");
				JLabel etiqSepVertical = new JLabel(textos[56]);
				
				panelEtiquetas3.add(etiqSepHorizontal);
				panelEtiquetas3.add(new JPanel());
				panelEtiquetas3.add(etiqSepVertical);
				panelEtiquetas3.add(new JPanel());
				
				// Panel JComboBoxes
				JPanel panelDesplegables3 = new JPanel();
				panelDesplegables3.setLayout(new GridLayout(4,1));
				
				sepHorizontal = new JComboBox();
				for (int i=0; i<91; i++)
					sepHorizontal.addItem((i+10)+"");
				sepHorizontal.addActionListener(this);
				sepHorizontal.addKeyListener(this);

				sepVertical = new JComboBox();
				for (int i=0; i<91; i++)
					sepVertical.addItem((i+10)+"");
				sepVertical.addActionListener(this);
				sepVertical.addKeyListener(this);
				
				sepHorizontal.setPreferredSize(new Dimension(75,19));
				sepVertical.setPreferredSize(new Dimension(75,19));
				
				panelDesplegables3.add(sepHorizontal);
				panelDesplegables3.add(new JPanel());
				panelDesplegables3.add(sepVertical);
				panelDesplegables3.add(new JPanel());

				panelJCBs3.add(panelEtiquetas3,BorderLayout.WEST);
				panelJCBs3.add(panelDesplegables3,BorderLayout.EAST);
				
				// Panel etiquetaImagen
				JPanel panelEtiquetaImagen3= new JPanel();
				JLabel etiqImagen3 = new JLabel(new ImageIcon("./imagenes/cuadroconf_distancias.gif"));
				panelEtiquetaImagen3.add(etiqImagen3);

			panelDistancias.add(panelJCBs3,BorderLayout.NORTH);
			panelDistancias.add(panelEtiquetaImagen3,BorderLayout.CENTER);
			
		panelSuperior2.add(panelCaracteristicas,BorderLayout.WEST);
		panelSuperior2.add(panelDistancias,BorderLayout.CENTER);
		panelSuperior2.add(panelFlecha,BorderLayout.EAST);
			
			cp.setValores(creandoPanel,80);
			
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font fonts[]=ge.getAllFonts();

			// Panel vista código texto
			JPanel panelFuenteCodigo=new JPanel();
			panelFuenteCodigo.setBorder(new TitledBorder(textos[57]));
			panelFuenteCodigo.setLayout(new BorderLayout());
			
			JLabel etiqFuenteCodigo=new JLabel(textos[58]+"   ");
			JLabel etiqTFCodigo=new JLabel(textos[59]);

			fuentesCodigo=new JComboBox();
			for (int i=0; i<fonts.length; i++)
				fuentesCodigo.addItem(fonts[i].getFontName());
				
			fuentesTamCodigo=new JComboBox();
			for (int i=0; i<73; i++)
				fuentesTamCodigo.addItem(""+(i+8));
				
			fuentesCodigo.addActionListener(this);
			fuentesTamCodigo.addActionListener(this);
			
			JPanel tamFormaCodigo = new JPanel();
			tamFormaCodigo.setLayout(new BorderLayout());
			tamFormaCodigo.add(fuentesTamCodigo, BorderLayout.EAST);
			
			JPanel linea1Codigo=new JPanel();
			linea1Codigo.setLayout(new BorderLayout());
			linea1Codigo.add(etiqFuenteCodigo, BorderLayout.WEST);
			linea1Codigo.add(fuentesCodigo, BorderLayout.EAST);
			
			JPanel linea2Codigo=new JPanel();
			linea2Codigo.setLayout(new BorderLayout());
			linea2Codigo.add(etiqTFCodigo, BorderLayout.WEST);
			linea2Codigo.add(tamFormaCodigo, BorderLayout.EAST);
			
			panelFuenteCodigo.add(linea1Codigo,BorderLayout.NORTH);
			panelFuenteCodigo.add(linea2Codigo,BorderLayout.SOUTH);
			
			// Panel vista código traza
			JPanel panelFuenteTraza=new JPanel();
			panelFuenteTraza.setBorder(new TitledBorder(textos[60]));
			panelFuenteTraza.setLayout(new BorderLayout());
			
			JLabel etiqFuenteTraza=new JLabel(textos[58]+"   ");
			JLabel etiqTFTraza=new JLabel(textos[59]);
			
			fuentesTraza=new JComboBox();
			for (int i=0; i<fonts.length; i++)
				fuentesTraza.addItem(fonts[i].getFontName());
				
			fuentesTamTraza=new JComboBox();
			for (int i=0; i<73; i++)
				fuentesTamTraza.addItem(""+(i+8));
				
			fuentesTraza.addActionListener(this);
			fuentesTamTraza.addActionListener(this);
						
			JPanel tamFormaTraza = new JPanel();
			tamFormaTraza.setLayout(new BorderLayout());
			tamFormaTraza.add(fuentesTamTraza, BorderLayout.EAST);
			
			JPanel linea1Traza=new JPanel();
			linea1Traza.setLayout(new BorderLayout());
			linea1Traza.add(etiqFuenteTraza, BorderLayout.WEST);
			linea1Traza.add(fuentesTraza, BorderLayout.EAST);
			
			JPanel linea2Traza=new JPanel();
			linea2Traza.setLayout(new BorderLayout());
			linea2Traza.add(etiqTFTraza, BorderLayout.WEST);
			linea2Traza.add(tamFormaTraza, BorderLayout.EAST);
			
			panelFuenteTraza.add(linea1Traza,BorderLayout.NORTH);
			panelFuenteTraza.add(linea2Traza,BorderLayout.SOUTH);
			
			
			panelInferior2.add(panelFuenteCodigo,BorderLayout.WEST);
			panelInferior2.add(panelFuenteTraza,BorderLayout.EAST);
			
		panelPestana22.add(panelSuperior2,BorderLayout.NORTH);
		panelPestana22.add(panelInferior2,BorderLayout.CENTER);
		
		pestanas.add(textos[61],panelPestana22);
		cp.setValores(creandoPanel,90);
		
		pestanas.setToolTipTextAt(0, textos[62]);
		pestanas.setToolTipTextAt(1, textos[62]);
		pestanas.setToolTipTextAt(2, textos[62]);
		pestanas.setToolTipTextAt(3, textos[63]);
		pestanas.setMnemonicAt(0, 'C') ;
		pestanas.setMnemonicAt(1, 'T') ;
		pestanas.setMnemonicAt(2, 'M') ;
		pestanas.setMnemonicAt(3, 'S') ;			// ¡¡¡ Adaptar a multidioma !!!
		
		
		
		// Panel Botones
		JPanel panelBotones=new JPanel();
		panelBotones.add(aceptar);
		panelBotones.add(cancelar);
		aceptar.addMouseListener(this);
		aceptar.addKeyListener(this);
		cancelar.addMouseListener(this);
		cancelar.addKeyListener(this);
		
		// Panel general
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(pestanas,BorderLayout.NORTH);
		panel.add(panelBotones,BorderLayout.SOUTH);
				
		setValores();
		jcc1.setColor(etiqColores1[0].getBackground());
		jcc2.setColor(etiqColores2[0].getBackground());
		jcc3.setColor(etiqColores3[0].getBackground());
		
		this.creandoCuadro=false;		// Evita refrescos innecesarios en ventanas
		//System.out.println("creandoCuadro="+creandoCuadro);
		
		// Preparamos y mostramos cuadro
		dialogo.getContentPane().add(panel);

		dialogo.setTitle(textos[64]);
		cp.setValores(creandoPanel,95);
		
		if (Ventana.thisventana.msWindows)
		{
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
		cp.cerrar();	
		dialogo.setResizable(false);
		dialogo.setVisible(true);

	}
		
	private JPanel creaPanelColor(JLabel etiqColor)
	{

		JPanel panelEtiqueta=new JPanel();
		panelEtiqueta.add(etiqColor);
		panelEtiqueta.setToolTipText(marcarPanel);
		panelEtiqueta.setForeground(new Color(180,0,0));
		panelEtiqueta.setBackground(new Color(180,0,0));
		panelEtiqueta.setPreferredSize(new Dimension(25,12));
		return panelEtiqueta;
	}
	
	
	
	/**
		
	
		@param tipo ayuda a diferenciar entre rellenar los campos para la entrada y la salida
	*/
	private void setValores()
	{
		int color[]=new int[3];
				
		for (int i=0; i<numSelectores1; i++)
		{
			switch(i)
			{
				
				case  0:
					color=ocv.getColorC1Entrada();
					break;
				case  1:
					color=ocv.getColorCAEntrada();
					break;
				case  2:
					color=ocv.getColorC1Salida();
					break;
				case  3:
					color=ocv.getColorCASalida();
					break;
				case  4:
					color=ocv.getColorC1NCSalida();
					break;
				
				default:
					color[0]=color[1]=color[2]=0;
			}
			panelesColores1[i].setForeground(new Color(color[0],color[1],color[2]));
			panelesColores1[i].setBackground(new Color(color[0],color[1],color[2]));
			etiqColores1[i].setForeground(new Color(color[0],color[1],color[2]));
			etiqColores1[i].setBackground(new Color(color[0],color[1],color[2]));
		}
		
		
		for (int i=0; i<numSelectores3; i++)
		{
			switch(i)
			{
				case  0:
					color=ocv.getColorFEntrada();
					break;
				
				case  1:
					color=ocv.getColorFSalida();
					break;
				
				case  2:
					color=ocv.getColorPanel();
					break;
				case  3:
					color=ocv.getColorFlecha();
					break;
				case  4:
					color=ocv.getColorActual();
					break;
				case  5:
					color=ocv.getColorCActual();
					break;
				case  6:
					color=ocv.getColorCodigoPR();
					break;
				case  7:
					color=ocv.getColorCodigoCo();
					break;
				case  8:
					color=ocv.getColorCodigoMF();
					break;
				case  9:
					color=ocv.getColorCodigoMB();
					break;
				case 10:
					color=ocv.getColorCodigoRC();
					break;
				case 11:
					color=ocv.getColorIluminado();
					break;
				case 12:
					color=ocv.getColorResaltado();
					break;
				default:
					color[0]=color[1]=color[2]=0;
			}
			panelesColores3[i].setForeground(new Color(color[0],color[1],color[2]));
			panelesColores3[i].setBackground(new Color(color[0],color[1],color[2]));
			etiqColores3[i].setForeground(new Color(color[0],color[1],color[2]));
			etiqColores3[i].setBackground(new Color(color[0],color[1],color[2]));
		}
		
		for (int i=0; i<numSelectores2; i++)
		{
			panelesColores2[i].setForeground(Conf.coloresNodo[i]);
			panelesColores2[i].setBackground(Conf.coloresNodo[i]);
			etiqColores2[i].setForeground(Conf.coloresNodo[i]);
			etiqColores2[i].setBackground(Conf.coloresNodo[i]);
		}
		
		if (Conf.modoColor==1)
		{
			checkModo1.setSelected(true);
			habilitarElementosModo1(checkModo1.isSelected());
			habilitarElementosModo2(!checkModo1.isSelected());
		}
		else if (Conf.modoColor==2)
		{
			checkModo2.setSelected(true);
			habilitarElementosModo2(checkModo2.isSelected());
			habilitarElementosModo1(!checkModo2.isSelected());
		}
		
		this.colorDegradado[0].setSelected(ocv.getColorDegradadoModo1());
		this.colorDegradado[1].setSelected(ocv.getColorDegradadoModo2());
		
		//this.colorSubordinado[0].setSelected(ocv.getSubordinarTraza());
		//this.colorSubordinado[1].setSelected(ocv.getSubordinarCodigo());
		
		grosorFlecha.setSelectedIndex(ocv.getGrosorFlecha()-1);		// con -1 convertimos de ancho real a posición de lista
		
		tipoFlecha.setSelectedIndex(ocv.getFormaFlecha());
		grosorMarco.setSelectedIndex(ocv.getGrosorActual());
		bordeCelda.setSelectedIndex(ocv.getTipoBordeCelda());
		
		sepHorizontal.setSelectedIndex(ocv.getDistanciaH());
		sepVertical.setSelectedIndex(ocv.getDistanciaV());
		
		fuentesCodigo.setSelectedItem(ocv.getFuenteCodigo());
		fuentesTamCodigo.setSelectedItem(""+ocv.getTamFuenteCodigo());
		
		fuentesTraza.setSelectedItem(ocv.getFuenteTraza());
		fuentesTamTraza.setSelectedItem(""+ocv.getTamFuenteTraza());
		
		
		//selectores2[6].setEnabled( !this.colorSubordinado[0].isSelected() );
		//selectores2[7].setEnabled( !this.colorSubordinado[0].isSelected() );
		//selectores2[8].setEnabled( !this.colorSubordinado[0].isSelected() );
		//selectores2[9].setEnabled( !this.colorSubordinado[0].isSelected() );
		//selectores2[10].setEnabled( !this.colorSubordinado[0].isSelected() );
		//selectores2[5].setEnabled( !this.colorSubordinado[1].isSelected() );
		
		
	}
	
	
	/**
		Comprueba que la cadena s contenga un valor válido (0..255)
		
		@param s cadena que será comprobada
		@return true si la cadena contiene un valor válido para una componente RGB
	*/	
	private void getValores()
	{
		Color c;
		
		// Colores pestaña 1 (árbol y pila)
		c=etiqColores1[0].getBackground();
		ocv.setColorC1Entrada( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores1[1].getBackground();
		ocv.setColorCAEntrada( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores1[2].getBackground();
		ocv.setColorC1Salida( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores1[3].getBackground();
		ocv.setColorCASalida( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores1[4].getBackground();
		ocv.setColorC1NCSalida( c.getRed(),c.getGreen(),c.getBlue() );
		
		
		c=etiqColores3[0].getBackground();
		ocv.setColorFEntrada( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores3[1].getBackground();
		ocv.setColorFSalida( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores3[2].getBackground();
		ocv.setColorPanel( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores3[3].getBackground();
		ocv.setColorFlecha( c.getRed(),c.getGreen(),c.getBlue() );
	
		c=etiqColores3[4].getBackground();
		ocv.setColorActual( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores3[5].getBackground();
		ocv.setColorCActual( c.getRed(),c.getGreen(),c.getBlue() );
		
		// Colores antigua pestaña 2 (traza y código)
		c=etiqColores3[6].getBackground();
		ocv.setColorCodigoPR( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores3[7].getBackground();
		ocv.setColorCodigoCo( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores3[8].getBackground();
		ocv.setColorCodigoMF( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores3[9].getBackground();
		ocv.setColorCodigoMB( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores3[10].getBackground();
		ocv.setColorCodigoRC( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores3[11].getBackground();
		ocv.setColorIluminado( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores3[12].getBackground();
		ocv.setColorResaltado( c.getRed(),c.getGreen(),c.getBlue() );
		
		/*c=etiqColores2[5].getBackground();
		ocv.setColorCodigoFP( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores2[6].getBackground();
		ocv.setColorTrazaE( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores2[7].getBackground();
		ocv.setColorTrazaS( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores2[8].getBackground();
		ocv.setColorTrazaEA( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores2[9].getBackground();
		ocv.setColorTrazaSA( c.getRed(),c.getGreen(),c.getBlue() );
		
		c=etiqColores2[10].getBackground();
		ocv.setColorTrazaFP( c.getRed(),c.getGreen(),c.getBlue() );*/
		
		ocv.setGrosorActual( grosorMarco.getSelectedIndex() );
		ocv.setGrosorFlecha( grosorFlecha.getSelectedIndex()+1 );
		ocv.setFormaFlecha( tipoFlecha.getSelectedIndex() );
		ocv.setTipoBordeCelda( bordeCelda.getSelectedIndex() );
		
		ocv.setModoColor( Conf.modoColor );
		
		for (int i=0; i<Conf.numColoresMetodos; i++)
		{
			ocv.setColorModo2( 	etiqColores2[i].getBackground().getRed(),
								etiqColores2[i].getBackground().getGreen(),
								etiqColores2[i].getBackground().getBlue(),i);
		}
		
		ocv.setColorDegradadoModo1(colorDegradado[0].isSelected());
		ocv.setColorDegradadoModo2(colorDegradado[1].isSelected());
		
		

		ocv.setDistanciaH( sepHorizontal.getSelectedIndex() );
		ocv.setDistanciaV( sepVertical.getSelectedIndex() );
		
		//ocv.setSubordinarTraza( this.colorSubordinado[0].isSelected() );
		//ocv.setSubordinarCodigo(  this.colorSubordinado[1].isSelected() );
		
		Integer iAux=Integer.parseInt((String)fuentesTamCodigo.getSelectedItem());
		ocv.setFuenteCodigo( (String)fuentesCodigo.getSelectedItem(),iAux.intValue());//,(String)fuentesCodigo.getSelectedItem() );
		iAux=Integer.parseInt((String)fuentesTamTraza.getSelectedItem());
		ocv.setFuenteTraza( (String)fuentesTraza.getSelectedItem(),iAux.intValue());//,(String)fuentesTraza.getSelectedItem() );
	}
	

	
	/*private void colorearEtiqueta()
	{
		for (int i=0; i<selectores1.length; i++)
			if (selectores1[i].isSelected())
			{
				Color c=jcc1.getColor();
				panelesColores1[i].setForeground(c);
				panelesColores1[i].setBackground(c);
				etiqColores1[i].setForeground(c);
				etiqColores1[i].setBackground(c);
			}
			
		for (int i=0; i<selectores2.length; i++)
			if (selectores2[i].isSelected())
			{
				Color c=jcc2.getColor();
				panelesColores2[i].setForeground(c);
				panelesColores2[i].setBackground(c);
				etiqColores2[i].setForeground(c);
				etiqColores2[i].setBackground(c);
			}
	}*/
	
	
	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/	
	public void actionPerformed(ActionEvent e)
	{
		// Esto debe ir en guardadoRecarga, para que se almacene en la opción
		if (e.getSource()==checkModo1)
		{
			Conf.modoColor=( checkModo1.isSelected() ? 1 : 2);
			//System.out.println("checkModo1 -> Conf.modoColor="+Conf.modoColor);
		}
		if (e.getSource()==checkModo2)
		{
			Conf.modoColor=( checkModo2.isSelected() ? 2 : 1);
			//System.out.println("checkModo2 -> Conf.modoColor="+Conf.modoColor);
		}
	
		if (e.getSource()==aceptar)
			guardadoRecarga();

		else if (e.getSource().getClass().getName().contains("JRadioButton"))
		{
			for (int i=0; i<numSelectores1; i++)
				if (selectores1[i].isSelected())
					jcc1.setColor(etiqColores1[i].getBackground());

			for (int i=0; i<numSelectores2; i++)
				if (selectores2[i].isSelected())
					jcc2.setColor(etiqColores2[i].getBackground());
			
			for (int i=0; i<numSelectores3; i++)
				if (selectores3[i].isSelected())
					jcc3.setColor(etiqColores3[i].getBackground());
		}
		else if (e.getSource().getClass().getName().contains("JComboBox") || 
					e.getSource().getClass().getName().contains("JCheckBox"))
		{
			if (e.getSource()==checkModo1)
			{
				habilitarElementosModo1(checkModo1.isSelected());
				habilitarElementosModo2(!checkModo1.isSelected());
			}
			else if (e.getSource()==checkModo2)
			{
				habilitarElementosModo2(checkModo2.isSelected());
				habilitarElementosModo1(!checkModo2.isSelected());
			}
			
			Conf.degradado1=colorDegradado[0].isSelected();
			Conf.degradado2=colorDegradado[1].isSelected();
			
			if (!creandoCuadro)		// Evita refrescos innecesarios en ventanas al formar cuadro de diálogo
				guardadoRecarga();
		}
	}
	
	
	public void habilitarElementosModo1(boolean valor)
	{
		checkModo1.setSelected(valor);
		colorDegradado[0].setEnabled(valor);
		jcc1.setEnabled(valor);
		for (int i=0; i<selectores1.length; i++)
			selectores1[i].setEnabled(valor);
		asignar1.setEnabled(valor);
		resetear1.setEnabled(valor);
		jcc1.setEnabled(valor);
	}
	
	public void habilitarElementosModo2(boolean valor)
	{
		checkModo2.setSelected(valor);
		colorDegradado[1].setEnabled(valor);
		jcc2.setEnabled(valor);
		for (int i=0; i<selectores2.length; i++)
			selectores2[i].setEnabled(valor);
		asignar2.setEnabled(valor);
		resetear2.setEnabled(valor);
		jcc2.setEnabled(valor);
		//jcc1.setEnabled(valor);  ***Posible Error***
	}	
	
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/		
	public void keyPressed(KeyEvent e)
	{
		int code=e.getKeyCode();
		if (code==KeyEvent.VK_DOWN)
		{
			for (int i=0; i<selectores1.length; i++)
				if (selectores1[i].isFocusOwner())
					if (i!=selectores1.length-1)
						selectores1[i].transferFocus();
			for (int i=0; i<selectores2.length; i++)
				if (selectores2[i].isFocusOwner())
					if (i!=selectores2.length-1)
						selectores2[i].transferFocus();
		}
		else if (code==KeyEvent.VK_UP)
		{
			for (int i=0; i<selectores1.length; i++)
				if (selectores1[i].isFocusOwner())
					if (i!=0)
						selectores1[i].transferFocusBackward();
			for (int i=0; i<selectores2.length; i++)
				if (selectores2[i].isFocusOwner())
					if (i!=0)
						selectores2[i].transferFocusBackward();
		}
				
		if (e.getComponent()==aceptar)		
			this.dialogo.setVisible(false);
				
		else if (e.getComponent()==asignar1)
			accionAsignar1();

		else if (e.getComponent()==asignar2)
			accionAsignar2();
		
		else if (e.getComponent()==asignar3)
			accionAsignar3();
			
		else if (e.getComponent()==resetear1)
			accionResetear1();
				
		else if (e.getComponent()==resetear2)
			accionResetear2();
		
		else if (e.getComponent()==resetear3)
			accionResetear3();

		if (e.getComponent()==asignar1 || e.getComponent()==asignar2 || e.getComponent()==asignar3 || e.getComponent()==aceptar)
			guardadoRecarga();		
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyReleased(KeyEvent e)
	{

		int code=e.getKeyCode();
		if (code==KeyEvent.VK_ESCAPE)
			dialogo.setVisible(false);
		else if (code==KeyEvent.VK_ENTER)
		{
			guardadoRecarga();
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
		//if (e.getComponent().getClass().getCanonicalName().contains("Boton"))
		//	((Boton)e.getComponent()).setNaranja();

	}

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseExited(MouseEvent e) 
	{
		//if (e.getComponent().getClass().getCanonicalName().contains("Boton"))
		//	((Boton)e.getComponent()).setVerde();
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mousePressed(MouseEvent e) 
	{
		if (e.getComponent().getClass().getCanonicalName().contains("JPanel"))
		{
			for (int i=0; i<numSelectores1; i++)
				if (panelesColores1[i]==e.getComponent())
				{
					selectores1[i].setSelected(true);
					jcc1.setColor(etiqColores1[i].getBackground());
				}
			for (int i=0; i<numSelectores2; i++)
				if (panelesColores2[i]==e.getComponent() && selectores2[i].isEnabled())
				{
					selectores2[i].setSelected(true);
					jcc2.setColor(etiqColores2[i].getBackground());
				}
			for (int i=0; i<numSelectores3; i++)
				if (panelesColores3[i]==e.getComponent() && selectores3[i].isEnabled())
				{
					selectores3[i].setSelected(true);
					jcc3.setColor(etiqColores3[i].getBackground());
				}
		}
		else if (e.getComponent().getClass().getCanonicalName().contains("JRadioButton"))
		{
			for (int i=0; i<numSelectores1; i++)
				if (selectores1[i]==e.getComponent())
					jcc1.setColor(etiqColores1[i].getBackground());
					
			for (int i=0; i<numSelectores2; i++)
				if (selectores2[i]==e.getComponent() && selectores2[i].isEnabled())
					jcc2.setColor(etiqColores2[i].getBackground());
			for (int i=0; i<numSelectores3; i++)
				if (selectores3[i]==e.getComponent() && selectores3[i].isEnabled())
					jcc3.setColor(etiqColores3[i].getBackground());
		}
		else if (e.getComponent().getClass().getCanonicalName().contains("BotonAceptar"))
		{
			this.dialogo.setVisible(false);
			guardadoRecarga();
		}
		else if (e.getComponent() instanceof JButton)
		{
			if (e.getComponent()==aceptar)		
				this.dialogo.setVisible(false);
			
			if (e.getComponent()==asignar1)
				accionAsignar1();

			else if (e.getComponent()==asignar2)
				accionAsignar2();
			
			else if (e.getComponent()==asignar3)
				accionAsignar3();
				
			else if (e.getComponent()==resetear1)
				accionResetear1();
				
			else if (e.getComponent()==resetear2)
				accionResetear2();
			
			else if (e.getComponent()==resetear3)
				accionResetear3();
				
			if (e.getComponent()==asignar1 || e.getComponent()==asignar2 || e.getComponent()==asignar3 || e.getComponent()==aceptar)
				guardadoRecarga();

			
		}
		else if (e.getSource().getClass().getName().contains("JComboBox") || 
					e.getSource().getClass().getName().contains("JCheckBox"))
		{
			if (!creandoCuadro)		// Evita refrescos innecesarios en ventanas al formar cuadro de diálogo
				guardadoRecarga();
		}
			
	}
	
	
	private void accionAsignar1()
	{
		Color c=jcc1.getColor();

		if (asignar1.isEnabled())
			for (int i=0; i<numSelectores1; i++)
				if (selectores1[i].isSelected())
				{
					panelesColores1[i].setForeground(c);
					panelesColores1[i].setBackground(c);
					etiqColores1[i].setForeground(c);
					etiqColores1[i].setBackground(c);
				}
	}

	private void accionAsignar2()
	{
		Color c=jcc2.getColor();
		
		if (asignar2.isEnabled())
			for (int i=0; i<numSelectores2; i++)
				if (selectores2[i].isSelected())
				{
					panelesColores2[i].setForeground(c);
					panelesColores2[i].setBackground(c);
					etiqColores2[i].setForeground(c);
					etiqColores2[i].setBackground(c);
				}
	}
	
	
	
	private void accionAsignar3()
	{
		Color c=jcc3.getColor();

		for (int i=0; i<numSelectores3; i++)
			if (selectores3[i].isSelected())
			{
				panelesColores3[i].setForeground(c);
				panelesColores3[i].setBackground(c);
				etiqColores3[i].setForeground(c);
				etiqColores3[i].setBackground(c);
			}
	}
	
	private void accionResetear1()
	{
		if (resetear1.isEnabled())
			for (int i=0; i<numSelectores1; i++)
				if (selectores1[i].isSelected())
					jcc1.setColor(etiqColores1[i].getBackground());
	}
		
	private void accionResetear2()
	{
		if (resetear2.isEnabled())
			for (int i=0; i<numSelectores2; i++)
				if (selectores2[i].isSelected())
					jcc2.setColor(etiqColores2[i].getBackground());
	}
	
	private void accionResetear3()
	{
		for (int i=0; i<numSelectores3; i++)
			if (selectores3[i].isSelected())
				jcc3.setColor(etiqColores3[i].getBackground());
	}
	

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseReleased(MouseEvent e)
	{
		//System.out.println("mouseReleased");
		if (e.getSource()==cancelar)
		{
			dialogo.setVisible(false);
		}
	}


	public void guardadoRecarga()
	{
		getValores();
		gOpciones.setOpcion(ocv,1);
		Conf.setValoresVisualizacion();
		if (Ventana.thisventana.traza!=null)
		{
			Conf.setRedibujarGrafoArbol(true);
			
			Conf.setRedibujarGrafoArbol(false);
		}
		Ventana.thisventana.refrescarFormato();	// Debe ejecutarse tras la actualización de Conf
	}
	
	
	public JDialog getDialogo()
	{
		return this.dialogo;
	}
	
	public void setVisible(boolean valor)
	{
		this.dialogo.setVisible(valor);
		if (valor)
		{
			ocv=(OpcionConfVisualizacion)gOpciones.getOpcion("OpcionConfVisualizacion",true);
			setValores();
			for (int i=0; i<numSelectores1; i++)
				if (selectores1[i].isSelected())
					jcc1.setColor(etiqColores1[i].getBackground());
			for (int i=0; i<numSelectores2; i++)
				if (selectores2[i].isSelected())
					jcc2.setColor(etiqColores2[i].getBackground());
			for (int i=0; i<numSelectores3; i++)
				if (selectores3[i].isSelected())
					jcc3.setColor(etiqColores3[i].getBackground());
		}
	}
}

