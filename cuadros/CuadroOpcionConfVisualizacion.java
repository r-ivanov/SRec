package cuadros;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.SwingUtilities;

import conf.*;
import botones.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

/**
 * Representa la clase del cuadro que maneja la opción que gestiona la
 * configuración de la visualización.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroOpcionConfVisualizacion extends Thread implements
		ActionListener, KeyListener, MouseListener {

	private static final int ANCHO_CUADRO = 900;
	private static final int ALTO_CUADRO = 485;
	private static final int ANCHO_CUADRO_NO_WINDOWS = 900;
	private static final int ALTO_CUADRO_NO_WINDOWS = 465;

	// Num. selectores en pestaña 1
	private static final int NUM_SELECTORES_1 = 5;
	// Num. selectores en pestaña 2
	private static final int NUM_SELECTORES_2 = Conf.numColoresMetodos;
	// Num. selectores en pestaña 3
	private static final int NUM_SELECTORES_3 = 15;

	private final static String CREANDO_PANEL = Texto.get("CP_CREARPAN",
			Conf.idioma);
	private final static String MARCAR_PANEL = Texto.get("COCV_MARCAR",
			Conf.idioma);

	private OpcionConfVisualizacion ocv = null;
	private GestorOpciones gOpciones = new GestorOpciones();

	private JCheckBox checkModo1;
	private JCheckBox checkModo2;

	private JTabbedPane pestanas;
	private Ventana ventana;
	private JDialog dialogo;

	private BotonAceptar aceptar = new BotonAceptar();
	private BotonCancelar cancelar = new BotonCancelar();

	// [0] para modo 1 y [1] para modo 2
	private JCheckBox colorDegradado[] = new JCheckBox[2];

	private JComboBox<String> grosorFlecha, tipoFlecha, 
			grosorFlecha5, tipoFlecha5;
	private JComboBox<String> grosorMarco, bordeCelda;
	private JComboBox<String> sepHorizontal, sepVertical,
			sepHorizontal5, sepVertical5;
	private JComboBox<String> fuentesCodigo, fuentesTraza;
	private JComboBox<String> fuentesTamCodigo, fuentesTamTraza;
	private JComboBox<String> comboThemes;
	
	private JColorChooser jcc1, jcc2, jcc3;

	private JRadioButton selectores1[] = new JRadioButton[NUM_SELECTORES_1];
	private JRadioButton selectores2[] = new JRadioButton[NUM_SELECTORES_2];
	private JRadioButton selectores3[] = new JRadioButton[NUM_SELECTORES_3];
	private JLabel etiqColores1[] = new JLabel[NUM_SELECTORES_1];
	private JLabel etiqColores2[] = new JLabel[NUM_SELECTORES_2];
	private JLabel etiqColores3[] = new JLabel[NUM_SELECTORES_3];

	private JPanel panelesColores1[] = new JPanel[NUM_SELECTORES_1];
	private JPanel panelesColores2[] = new JPanel[NUM_SELECTORES_2];
	private JPanel panelesColores3[] = new JPanel[NUM_SELECTORES_3];

	private BotonTexto asignar1, resetear1, asignar2, resetear2, asignar3,
			resetear3;

	private CuadroProgreso cp;

	private boolean creandoCuadro = false;

	/**
	 * Genera un nuevo cuadro que maneja la opción que gestiona la configuración
	 * de la visualización.
	 * 
	 * @param ventana
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo
	 */
	public CuadroOpcionConfVisualizacion(Ventana ventana) {
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		SwingUtilities.invokeLater(new Runnable() { 
	        public void run() { 
	        		CuadroOpcionConfVisualizacion.this.start();
	        }
		});
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		// Haremos una búsqueda "colectiva" para ahorrarnos cargar el fichero
		// Textos.xml varias veces desde el sistema de archivos
		String[] codigos = { "COCV_SELEC", "COCV_FUENTEE", "COCV_CELDE1",
				"COCV_CELDEA1", "COCV_FUENTES", "COCV_CELDS1", "COCV_CELDSA1",
				"COCV_CELDSNC1", "COCV_FONDOPANEL", "COCV_FLECHA",
				"COCV_MARCONODACT", "COCV_MARCOCAMACT", "COCV_MARCAR",
				"COCV_HABDEGR", "COCV_HABDEGR_TTT", "BOTONASIGNAR",
				"BOTONDESCARTAR", "COCV_ETIQ_1", "COCV_PALRESERV",
				"COCV_COMENT", "COCV_MET", "COCV_METFON", "COCV_CODIGO",
				"COCV_HABILDEGR", "COCV_ETIQ_3", "COCV_COL_ILUM", "COCV_TRENA",/* 26 */
				"COCV_TRSAA",/* 27 */"COCV_TRPANEL",/* 28 */"COCV_SUBORDCOL",
				"COCV_SUBORDCOLTR", "COCV_SUBORDCOLCO", "COCV_SUBORDCOLCE",
				"COCV_ETIQ_2", "COCV_CONFFLEC", "COCV_CONFFLECANCH",
				"COCV_CONFFLECTIPO", "COCV_CONFFLECTIPONEU",
				"COCV_CONFFLECTIPOTEC", "COCV_CONFFLECTIPOCLA",
				"COCV_CONFFLECTIPOSIM", "COCV_CONFFLECTIPOROM",
				"COCV_CONFFLECTIPOSLI", "COCV_CONFFLECTIPODLI",
				"COCV_CONFFLECTIPOCIR", "COCV_CARACCELD", "COCV_CARACCELDMARC",
				"COCV_CARACCELDMARCT", "COCV_CARACCELDBORDV",
				"COCV_CARACCELDBORDB", "COCV_CARACCELDBORDG",
				"COCV_CARACCELDBORDL", "COCV_CARACCELDBORDBB",
				"COCV_CARACCELDBORDBL", "COCV_DISTCELD", "COCV_DISTCELDH",
				"COCV_DISTCELDV", "COCV_FUENTCOD", "COCV_FUENT",
				"COCV_FUENTTAM", "COCV_FUENTTR", "COCV_ETIQ_4",
				"COCV_ETIQ_ARBPILCODTRATTT", "COCV_ETIQ_TAMFORTTT",
				"COCV_FORMATO", "COCV_COL_RESA", "COCV_COL_SEL_ARBOL",
				"COCV_ETIQ_5", /*68*/"COCV_DISTCELD_G", "COCV_CONFFLEC_G",
				"COCV_COL_COD_ERR","COCV_COL_COD_THEME_TITLE","COCV_COL_COD_THEME_DEFAULT",
				"COCV_COL_COD_THEME_DARK", "COCV_COL_COD_THEME_ECLIPSE",
				"COCV_COL_COD_THEME_IDEA", "COCV_COL_COD_THEME_MONOKAI","COCV_COL_COD_THEME_VISUALSTUDIO"

		// 26 a 28 no se están usando
		// 29 a 32 se podrían no estar usando (comprobar)
		};

		String textos[] = Texto.get(codigos, Conf.idioma);

		this.cp = new CuadroProgreso(this.ventana, Texto.get("CP_ESPERE",
				Conf.idioma), CREANDO_PANEL, 0);

		// Evita refrescos innecesarios en ventanas al formar cuadro de diálogo
		this.creandoCuadro = true;

		this.ocv = (OpcionConfVisualizacion) this.gOpciones.getOpcion(
				"OpcionConfVisualizacion", false);

		this.cp.setValores(CREANDO_PANEL, 10);

		// Creamos estructura de pestañas
		this.pestanas = new JTabbedPane();
		this.pestanas.addMouseListener(this);
		this.pestanas.addKeyListener(this);

		// PESTAÑA 1 . MODO DE COLORES QUE DISTINGUE ENTRE ENTRADA Y SALIDA EN
		// CADA NODO

		// Panel Izquierda
		JPanel panelIzquierda = new JPanel();
		panelIzquierda.setLayout(new BorderLayout());

		// Panel checkbox modo
		JPanel pCheckModo1 = new JPanel();
		this.checkModo1 = new JCheckBox(Texto.get("COCV_MODO1", Conf.idioma));
		pCheckModo1.add(this.checkModo1);
		this.checkModo1.addActionListener(this);
		this.checkModo1.addMouseListener(this);
		this.checkModo1.addKeyListener(this);

		// Panel colores
		JPanel panelSeleccionColores = new JPanel();
		panelSeleccionColores.setBorder(new TitledBorder(textos[0]));
		panelSeleccionColores.setLayout(new GridLayout(NUM_SELECTORES_1, 1));

		ButtonGroup bg1 = new ButtonGroup();

		this.selectores1[0] = new JRadioButton(textos[2]);
		this.selectores1[1] = new JRadioButton(textos[3]);

		this.selectores1[2] = new JRadioButton(textos[5]);
		this.selectores1[3] = new JRadioButton(textos[6]);
		this.selectores1[4] = new JRadioButton(textos[7]);

		this.selectores1[0].setSelected(true);

		for (int i = 0; i < NUM_SELECTORES_1; i++) {
			this.selectores1[i].addKeyListener(this);
			this.selectores1[i].addActionListener(this);
			this.selectores1[i].addMouseListener(this);
			this.selectores1[i].setToolTipText(textos[12]);
			bg1.add(this.selectores1[i]);
			panelSeleccionColores.add(this.selectores1[i]);
		}

		this.cp.setValores(CREANDO_PANEL, 10);
		JPanel panelFila[] = new JPanel[NUM_SELECTORES_1];
		for (int i = 0; i < NUM_SELECTORES_1; i++) {
			panelFila[i] = new JPanel();
			panelFila[i].setLayout(new BorderLayout());
			panelFila[i].add(this.selectores1[i], BorderLayout.WEST);
			this.etiqColores1[i] = new JLabel("");
			this.etiqColores1[i].addMouseListener(this);
			this.etiqColores1[i].setForeground(new Color(0, 0, 0));
			this.etiqColores1[i].setBackground(new Color(0, 0, 0));
			this.panelesColores1[i] = creaPanelColor(this.etiqColores1[i]);
			this.panelesColores1[i].addMouseListener(this);
			panelFila[i].add(this.panelesColores1[i], BorderLayout.EAST);
			panelSeleccionColores.add(panelFila[i]);
		}
		panelSeleccionColores.setPreferredSize(new Dimension(270, 300));

		// Panel Degradados
		JPanel panelDegradados1 = new JPanel();
		panelDegradados1.setBorder(new TitledBorder(textos[13]));
		panelDegradados1.setLayout(new GridLayout(1, 1));
		this.colorDegradado[0] = new JCheckBox(textos[23]);
		panelDegradados1.add(this.colorDegradado[0]);
		this.colorDegradado[0].addActionListener(this);
		this.colorDegradado[0].setToolTipText(textos[14]);

		panelIzquierda.add(pCheckModo1, BorderLayout.NORTH);
		panelIzquierda.add(panelSeleccionColores, BorderLayout.CENTER);
		panelIzquierda.add(panelDegradados1, BorderLayout.SOUTH);

		this.cp.setValores(CREANDO_PANEL, 15);

		// Panel Derecha
		JPanel panelDerecha = new JPanel();
		panelDerecha.setLayout(new BorderLayout());

		this.jcc1 = new JColorChooser(new Color(0, 0, 0));
		this.jcc1.addMouseListener(this);

		// Panel Botones Asignar y Borrar
		JPanel panelBotonesDerecha = new JPanel();
		this.asignar1 = new BotonTexto(textos[15]);
		this.resetear1 = new BotonTexto(textos[16]);

		panelBotonesDerecha.add(this.asignar1);
		panelBotonesDerecha.add(this.resetear1);

		this.asignar1.addMouseListener(this);
		this.resetear1.addMouseListener(this);

		this.asignar1.addKeyListener(this);
		this.resetear1.addKeyListener(this);

		panelDerecha.add(this.jcc1, BorderLayout.NORTH);
		panelDerecha.add(panelBotonesDerecha, BorderLayout.SOUTH);

		// Panel pestana color
		JPanel panelPestana1 = new JPanel();
		panelPestana1.setLayout(new BorderLayout());
		panelPestana1.add(panelIzquierda, BorderLayout.WEST);
		panelPestana1.add(panelDerecha, BorderLayout.EAST);

		this.pestanas.add(textos[17], panelPestana1);

		this.cp.setValores(CREANDO_PANEL, 20);
		// PESTAÑA 2 . MODO DE COLORES QUE DISTINGUE ENTRE NODOS DE MÉTODOS

		// Panel Izquierda
		JPanel panelIzquierda2 = new JPanel();
		panelIzquierda2.setLayout(new BorderLayout());

		// Panel checkbox modo
		JPanel pCheckModo2 = new JPanel();
		this.checkModo2 = new JCheckBox(Texto.get("COCV_MODO2", Conf.idioma));
		pCheckModo2.add(this.checkModo2);
		this.checkModo2.addActionListener(this);
		this.checkModo2.addMouseListener(this);
		this.checkModo2.addKeyListener(this);

		// Panel colores
		JPanel panelSeleccionColores2 = new JPanel();
		panelSeleccionColores2.setBorder(new TitledBorder(textos[0]));
		panelSeleccionColores2.setLayout(new GridLayout(NUM_SELECTORES_2, 1));

		ButtonGroup bg2 = new ButtonGroup();
		this.selectores2[0] = new JRadioButton(Texto.get("COCV_COLOR",
				Conf.idioma)
				+ " 1 ("
				+ Texto.get("CMP_METPRINC", Conf.idioma)
				+ ")");
		this.selectores2[1] = new JRadioButton(Texto.get("COCV_COLOR",
				Conf.idioma) + " 2");
		this.selectores2[2] = new JRadioButton(Texto.get("COCV_COLOR",
				Conf.idioma) + " 3");
		this.selectores2[3] = new JRadioButton(Texto.get("COCV_COLOR",
				Conf.idioma) + " 4");
		this.selectores2[4] = new JRadioButton(Texto.get("COCV_COLOR",
				Conf.idioma) + " 5");
		this.selectores2[5] = new JRadioButton(Texto.get("COCV_COLOR",
				Conf.idioma) + " 6");
		this.selectores2[6] = new JRadioButton(Texto.get("COCV_COLOR",
				Conf.idioma) + " 7");
		this.selectores2[7] = new JRadioButton(Texto.get("COCV_COLOR",
				Conf.idioma) + " 8");
		this.selectores2[8] = new JRadioButton(Texto.get("COCV_COLOR",
				Conf.idioma) + " 9");
		this.selectores2[9] = new JRadioButton(Texto.get("COCV_COLOR",
				Conf.idioma) + " 10");
		this.selectores2[0].setSelected(true);

		for (int i = 0; i < NUM_SELECTORES_2; i++) {
			this.selectores2[i].addKeyListener(this);
			this.selectores2[i].addActionListener(this);
			this.selectores2[i].addMouseListener(this);
			this.selectores2[i].setToolTipText(textos[12]);
			bg2.add(this.selectores2[i]);
			panelSeleccionColores2.add(this.selectores2[i]);
		}

		this.cp.setValores(CREANDO_PANEL, 30);

		JPanel panelFila2[] = new JPanel[NUM_SELECTORES_2];
		for (int i = 0; i < NUM_SELECTORES_2; i++) {
			panelFila2[i] = new JPanel();
			panelFila2[i].setLayout(new BorderLayout());
			panelFila2[i].add(this.selectores2[i], BorderLayout.WEST);
			this.etiqColores2[i] = new JLabel("");
			this.etiqColores2[i].addMouseListener(this);
			this.panelesColores2[i] = creaPanelColor(this.etiqColores2[i]);
			this.panelesColores2[i].addMouseListener(this);
			panelFila2[i].add(this.panelesColores2[i], BorderLayout.EAST);
			panelSeleccionColores2.add(panelFila2[i]);
		}
		panelSeleccionColores2.setPreferredSize(new Dimension(270, 300));

		// Panel degradados
		JPanel panelDegradados2 = new JPanel();
		panelDegradados2.setBorder(new TitledBorder(textos[13]));
		panelDegradados2.setLayout(new GridLayout(1, 1));
		this.colorDegradado[1] = new JCheckBox(textos[23]);
		panelDegradados2.add(this.colorDegradado[1]);
		this.colorDegradado[1].addActionListener(this);
		this.colorDegradado[1].setToolTipText(textos[14]);
		this.colorDegradado[1].addKeyListener(this);

		panelIzquierda2.add(pCheckModo2, BorderLayout.NORTH);
		panelIzquierda2.add(panelSeleccionColores2, BorderLayout.CENTER);
		panelIzquierda2.add(panelDegradados2, BorderLayout.SOUTH);

		this.cp.setValores(CREANDO_PANEL, 35);

		// Panel Derecha
		JPanel panelDerecha2 = new JPanel();
		panelDerecha2.setLayout(new BorderLayout());

		// JColorChooser
		this.jcc2 = new JColorChooser(new Color(0, 0, 0));
		this.jcc2.addMouseListener(this);

		// Panel Botones Asignar y Borrar
		JPanel panelBotonesDerecha2 = new JPanel();
		this.asignar2 = new BotonTexto(textos[15]);
		this.resetear2 = new BotonTexto(textos[16]);

		panelBotonesDerecha2.add(this.asignar2);
		panelBotonesDerecha2.add(this.resetear2);

		this.asignar2.addMouseListener(this);
		this.resetear2.addMouseListener(this);

		this.asignar2.addKeyListener(this);
		this.resetear2.addKeyListener(this);

		panelDerecha2.add(this.jcc2, BorderLayout.NORTH);
		panelDerecha2.add(panelBotonesDerecha2, BorderLayout.SOUTH);

		// Panel pestana color
		JPanel panelPestana2 = new JPanel();
		panelPestana2.setLayout(new BorderLayout());
		panelPestana2.add(panelIzquierda2, BorderLayout.WEST);
		panelPestana2.add(panelDerecha2, BorderLayout.EAST);

		this.pestanas.add(textos[33], panelPestana2);

		this.cp.setValores(CREANDO_PANEL, 40);

		// PESTAÑA 3 . OTROS COLORES

		// Panel Izquierda
		JPanel panelIzquierda3 = new JPanel();
		panelIzquierda3.setLayout(new GridBagLayout());

		// Panel colores
		
		JPanel panelSeleccionColores3 = new JPanel();
		panelSeleccionColores3.setBorder(new TitledBorder(textos[0]));
		panelSeleccionColores3.setPreferredSize(new Dimension(270, 300));
		
		ButtonGroup bg3 = new ButtonGroup();
		this.selectores3[0] = new JRadioButton(textos[1]);

		this.selectores3[1] = new JRadioButton(textos[4]);

		this.selectores3[2] = new JRadioButton(textos[8]);
		this.selectores3[3] = new JRadioButton(textos[9]);
		this.selectores3[4] = new JRadioButton(textos[10]);
		this.selectores3[5] = new JRadioButton(textos[11]);

		this.selectores3[6] = new JRadioButton(textos[18]);
		this.selectores3[7] = new JRadioButton(textos[19]);
		this.selectores3[8] = new JRadioButton(textos[20]);
		this.selectores3[9] = new JRadioButton(textos[21]);
		this.selectores3[10] = new JRadioButton(textos[22]);
		
		//	TODO Esto hay que cambiarlo, eliminando los selectores, 
		//	textos y códigos estos radio buttons, no ocultándolos
		this.selectores3[6].setVisible(false);
		this.selectores3[7].setVisible(false);
		this.selectores3[8].setVisible(false);
		this.selectores3[9].setVisible(false);
		this.selectores3[10].setVisible(false);
		
		this.selectores3[11] = new JRadioButton(textos[25]);
		this.selectores3[12] = new JRadioButton(textos[65]);
		this.selectores3[13] = new JRadioButton(textos[66]);
		this.selectores3[14] = new JRadioButton(textos[70]);

		this.selectores3[0].setSelected(true);

		int numeroSelectoresVisibles = 0;
		
		for (int i = 0; i < NUM_SELECTORES_3; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!this.selectores3[i].isVisible()){
				continue;
			}
			numeroSelectoresVisibles++;
			this.selectores3[i].addKeyListener(this);
			this.selectores3[i].addActionListener(this);
			this.selectores3[i].addMouseListener(this);
			this.selectores3[i].setToolTipText(textos[12]);
			bg3.add(this.selectores3[i]);
			panelSeleccionColores3.add(this.selectores3[i]);			
		}
		
		panelSeleccionColores3.setLayout(new GridLayout(numeroSelectoresVisibles, 1));
		
		this.cp.setValores(CREANDO_PANEL, 45);
		JPanel panelFila3[] = new JPanel[NUM_SELECTORES_3];
		for (int i = 0; i < NUM_SELECTORES_3; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!this.selectores3[i].isVisible()){
				continue;
			}
			panelFila3[i] = new JPanel();
			panelFila3[i].setLayout(new BorderLayout());
			panelFila3[i].add(this.selectores3[i], BorderLayout.WEST);
			this.etiqColores3[i] = new JLabel("");
			this.etiqColores3[i].addMouseListener(this);
			this.etiqColores3[i].setForeground(new Color(0, 0, 0));
			this.etiqColores3[i].setBackground(new Color(0, 0, 0));
			this.panelesColores3[i] = creaPanelColor(this.etiqColores3[i]);
			this.panelesColores3[i].addMouseListener(this);
			panelFila3[i].add(this.panelesColores3[i], BorderLayout.EAST);
			panelSeleccionColores3.add(panelFila3[i]);
			
		}
		
		//	Panel selección de tema
		JPanel panelSeleccionTema3 = new JPanel();
		panelSeleccionTema3.setBorder(new TitledBorder(textos[71]));
		panelSeleccionTema3.setLayout(new FlowLayout());
		
		comboThemes = new JComboBox<String>();		
		comboThemes.setName("comboThemes");
		comboThemes.addItem(textos[72]);
		comboThemes.addItem(textos[73]);
		comboThemes.addItem(textos[74]);
		comboThemes.addItem(textos[75]);
		comboThemes.addItem(textos[76]);
		comboThemes.addItem(textos[77]);
		comboThemes.addActionListener(this);
		comboThemes.addKeyListener(this);
		
		panelSeleccionTema3.add(comboThemes);
		//	Añadimos a panel izquierdo

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1;
		constraints.gridheight = 2; 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.weightx = 1;
		panelIzquierda3.add(panelSeleccionColores3, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 
		constraints.gridy = 3; 
		constraints.gridwidth = 1;
		constraints.gridheight = 1; 
		constraints.fill = GridBagConstraints.BOTH;
		panelIzquierda3.add(panelSeleccionTema3, constraints);
		
		this.cp.setValores(CREANDO_PANEL, 55);

		// Panel Derecha
		JPanel panelDerecha3 = new JPanel();
		panelDerecha3.setLayout(new BorderLayout());
		

		// JColorChooser
		this.jcc3 = new JColorChooser(new Color(0, 0, 0));
		this.jcc3.addMouseListener(this);

		// Panel Botones Asignar y Borrar
		JPanel panelBotonesDerecha3 = new JPanel();
		this.asignar3 = new BotonTexto(textos[15]);
		this.resetear3 = new BotonTexto(textos[16]);

		panelBotonesDerecha3.add(this.asignar3);
		panelBotonesDerecha3.add(this.resetear3);

		this.asignar3.addMouseListener(this);
		this.resetear3.addMouseListener(this);

		this.asignar3.addKeyListener(this);
		this.resetear3.addKeyListener(this);

		panelDerecha3.add(this.jcc3, BorderLayout.NORTH);
		panelDerecha3.add(panelBotonesDerecha3, BorderLayout.SOUTH);

		// Panel pestana color
		JPanel panelPestana3 = new JPanel();
		panelPestana3.setLayout(new BorderLayout());
		panelPestana3.add(panelIzquierda3, BorderLayout.WEST);
		panelPestana3.add(panelDerecha3, BorderLayout.EAST);

		this.pestanas.add(textos[24], panelPestana3);

		this.cp.setValores(CREANDO_PANEL, 60);

		// PESTAÑA 4 . ÁRBOLES Y PILAS

		JPanel panelPestana4 = new JPanel();
		panelPestana4.setLayout(new BorderLayout());

		// Panel flechas
		JPanel panelFlecha = new JPanel();
		panelFlecha.setBorder(new TitledBorder(textos[34]));
		panelFlecha.setLayout(new BorderLayout());

		// Panel JComboBoxes
		JPanel panelJCBs = new JPanel();
		panelJCBs.setLayout(new BorderLayout());

		// Panel etiquetas
		JPanel panelEtiquetas = new JPanel();
		panelEtiquetas.setLayout(new GridLayout(4, 1));

		JLabel etiqGrosor = new JLabel(textos[35] + "    ");
		JLabel etiqTipo = new JLabel(textos[36]);

		panelEtiquetas.add(etiqGrosor);
		panelEtiquetas.add(new JPanel());
		panelEtiquetas.add(etiqTipo);
		panelEtiquetas.add(new JPanel());

		// Panel JComboBoxes
		JPanel panelDesplegables = new JPanel();
		panelDesplegables.setLayout(new GridLayout(4, 1));

		this.grosorFlecha = new JComboBox<String>();
		for (int i = 0; i < 10; i++) {
			this.grosorFlecha.addItem((i + 1) + "");
		}
		this.grosorFlecha.addActionListener(this);
		this.grosorFlecha.addKeyListener(this);
		this.grosorFlecha.setAlignmentX(Component.RIGHT_ALIGNMENT);
		this.tipoFlecha = new JComboBox<String>();
		this.tipoFlecha.addItem(textos[37]);
		this.tipoFlecha.addItem(textos[38]);
		this.tipoFlecha.addItem(textos[39]);
		this.tipoFlecha.addItem(textos[40]);
		this.tipoFlecha.addItem(textos[41]);
		this.tipoFlecha.addItem(textos[42]);
		this.tipoFlecha.addItem(textos[43]);
		this.tipoFlecha.addItem(textos[44]);
		this.tipoFlecha.addActionListener(this);
		this.tipoFlecha.addKeyListener(this);

		this.grosorFlecha.setPreferredSize(new Dimension(75, 19));
		this.tipoFlecha.setPreferredSize(new Dimension(75, 19));

		panelDesplegables.add(this.grosorFlecha);
		panelDesplegables.add(new JPanel());
		panelDesplegables.add(this.tipoFlecha);
		panelDesplegables.add(new JPanel());

		panelJCBs.add(panelEtiquetas, BorderLayout.WEST);
		panelJCBs.add(panelDesplegables, BorderLayout.EAST);

		// Panel etiquetaImagen
		JPanel panelEtiquetaImagen = new JPanel();
		JLabel etiqImagen = new JLabel(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/cuadroconf_flechas.gif")));
		panelEtiquetaImagen.add(etiqImagen);

		panelFlecha.add(panelJCBs, BorderLayout.NORTH);
		panelFlecha.add(panelEtiquetaImagen, BorderLayout.CENTER);

		this.cp.setValores(CREANDO_PANEL, 65);

		// Panel características
		JPanel panelCaracteristicas = new JPanel();
		panelCaracteristicas.setBorder(new TitledBorder(textos[45]));
		panelCaracteristicas.setLayout(new BorderLayout());

		// Panel JComboBoxes
		JPanel panelJCBs2 = new JPanel();
		panelJCBs2.setLayout(new BorderLayout());

		// Panel etiquetas
		JPanel panelEtiquetas2 = new JPanel();
		panelEtiquetas2.setLayout(new GridLayout(4, 1));

		JLabel etiqGrosorMarco = new JLabel(textos[46] + "   ");
		JLabel etiqTipoBorde = new JLabel(textos[47]);

		panelEtiquetas2.add(etiqGrosorMarco);
		panelEtiquetas2.add(new JPanel());
		panelEtiquetas2.add(etiqTipoBorde);
		panelEtiquetas2.add(new JPanel());

		// Panel JComboBoxes
		JPanel panelDesplegables2 = new JPanel();
		panelDesplegables2.setLayout(new GridLayout(4, 1));

		this.grosorMarco = new JComboBox<String>();
		for (int i = 0; i < 9; i++) {
			this.grosorMarco.addItem((i) + "");
		}
		this.grosorMarco.addActionListener(this);
		this.grosorMarco.addKeyListener(this);

		this.bordeCelda = new JComboBox<String>();
		this.bordeCelda.addItem(textos[48]);
		this.bordeCelda.addItem(textos[49]);
		this.bordeCelda.addItem(textos[50]);
		this.bordeCelda.addItem(textos[51]);
		this.bordeCelda.addItem(textos[52]);
		this.bordeCelda.addItem(textos[53]);
		this.bordeCelda.addActionListener(this);
		this.bordeCelda.addKeyListener(this);

		this.grosorMarco.setPreferredSize(new Dimension(75, 19));
		this.bordeCelda.setPreferredSize(new Dimension(75, 19));

		panelDesplegables2.add(this.grosorMarco);
		panelDesplegables2.add(new JPanel());
		panelDesplegables2.add(this.bordeCelda);
		panelDesplegables2.add(new JPanel());

		panelJCBs2.add(panelEtiquetas2, BorderLayout.WEST);
		panelJCBs2.add(panelDesplegables2, BorderLayout.EAST);

		// Panel etiquetaImagen
		JPanel panelEtiquetaImagen2 = new JPanel();
		JLabel etiqImagen2 = new JLabel(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/cuadroconf_bordes.gif")));
		panelEtiquetaImagen2.add(etiqImagen2);

		panelCaracteristicas.add(panelJCBs2, BorderLayout.NORTH);
		panelCaracteristicas.add(panelEtiquetaImagen2, BorderLayout.CENTER);

		this.cp.setValores(CREANDO_PANEL, 75);

		// Panel distancias
		JPanel panelDistancias = new JPanel();
		panelDistancias.setBorder(new TitledBorder(textos[54]));
		panelDistancias.setLayout(new BorderLayout());

		// Panel JComboBoxes
		JPanel panelJCBs3 = new JPanel();
		panelJCBs3.setLayout(new BorderLayout());

		// Panel etiquetas
		JPanel panelEtiquetas3 = new JPanel();
		panelEtiquetas3.setLayout(new GridLayout(4, 1));

		JLabel etiqSepHorizontal = new JLabel(textos[55] + "    ");
		JLabel etiqSepVertical = new JLabel(textos[56]);

		panelEtiquetas3.add(etiqSepHorizontal);
		panelEtiquetas3.add(new JPanel());
		panelEtiquetas3.add(etiqSepVertical);
		panelEtiquetas3.add(new JPanel());

		// Panel JComboBoxes
		JPanel panelDesplegables3 = new JPanel();
		panelDesplegables3.setLayout(new GridLayout(4, 1));

		this.sepHorizontal = new JComboBox<String>();
		for (int i = 0; i < 91; i++) {
			this.sepHorizontal.addItem((i + 10) + "");
		}
		this.sepHorizontal.addActionListener(this);
		this.sepHorizontal.addKeyListener(this);

		this.sepVertical = new JComboBox<String>();
		for (int i = 0; i < 91; i++) {
			this.sepVertical.addItem((i + 10) + "");
		}
		this.sepVertical.addActionListener(this);
		this.sepVertical.addKeyListener(this);

		this.sepHorizontal.setPreferredSize(new Dimension(75, 19));
		this.sepVertical.setPreferredSize(new Dimension(75, 19));

		panelDesplegables3.add(this.sepHorizontal);
		panelDesplegables3.add(new JPanel());
		panelDesplegables3.add(this.sepVertical);
		panelDesplegables3.add(new JPanel());

		panelJCBs3.add(panelEtiquetas3, BorderLayout.WEST);
		panelJCBs3.add(panelDesplegables3, BorderLayout.EAST);

		// Panel etiquetaImagen
		JPanel panelEtiquetaImagen3 = new JPanel();
		JLabel etiqImagen3 = new JLabel(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/cuadroconf_distancias.gif")));
		panelEtiquetaImagen3.add(etiqImagen3);

		panelDistancias.add(panelJCBs3, BorderLayout.NORTH);
		panelDistancias.add(panelEtiquetaImagen3, BorderLayout.CENTER);

		panelPestana4.add(panelCaracteristicas, BorderLayout.WEST);
		panelPestana4.add(panelDistancias, BorderLayout.CENTER);
		panelPestana4.add(panelFlecha, BorderLayout.EAST);

		this.cp.setValores(CREANDO_PANEL, 80);

//		GraphicsEnvironment ge = GraphicsEnvironment
//				.getLocalGraphicsEnvironment();
//		Font fonts[] = ge.getAllFonts();

		this.pestanas.add(textos[61], panelPestana4);
		this.cp.setValores(CREANDO_PANEL, 90);
		
		// PESTAÑA 5. OTROS ELEMENTOS

		// *** Panel general
		JPanel panelPestana5 = new JPanel();
		panelPestana5.setLayout(new GridBagLayout());
		
		// *** Panel flechas
		JPanel panelFlecha5 = new JPanel();
		panelFlecha5.setBorder(new TitledBorder(textos[69]));
		panelFlecha5.setLayout(new BorderLayout());

		// Panel JComboBoxes
		JPanel panelJCBs5 = new JPanel();
		panelJCBs5.setLayout(new BorderLayout());

		// Panel etiquetas
		JPanel panelEtiquetas5 = new JPanel();
		panelEtiquetas5.setLayout(new GridLayout(4, 1));

		JLabel etiqGrosor5 = new JLabel(textos[35] + "    ");
		JLabel etiqTipo5 = new JLabel(textos[36]);

		panelEtiquetas5.add(etiqGrosor5);
		panelEtiquetas5.add(new JPanel());
		panelEtiquetas5.add(etiqTipo5);
		panelEtiquetas5.add(new JPanel());

		// Panel JComboBoxes
		JPanel panelDesplegables5 = new JPanel();
		panelDesplegables5.setLayout(new GridLayout(4, 1));

		this.grosorFlecha5 = new JComboBox<String>();
		for (int i = 0; i < 10; i++) {
			this.grosorFlecha5.addItem((i + 1) + "");
		}
		this.grosorFlecha5.addActionListener(this);
		this.grosorFlecha5.addKeyListener(this);
		this.grosorFlecha5.setAlignmentX(Component.RIGHT_ALIGNMENT);
		this.tipoFlecha5 = new JComboBox<String>();
		this.tipoFlecha5.addItem(textos[37]);
		this.tipoFlecha5.addItem(textos[38]);
		this.tipoFlecha5.addItem(textos[39]);
		this.tipoFlecha5.addItem(textos[40]);
		this.tipoFlecha5.addItem(textos[41]);
		this.tipoFlecha5.addItem(textos[42]);
		this.tipoFlecha5.addItem(textos[43]);
		this.tipoFlecha5.addItem(textos[44]);
		this.tipoFlecha5.addActionListener(this);
		this.tipoFlecha5.addKeyListener(this);

		this.grosorFlecha5.setPreferredSize(new Dimension(75, 19));
		this.tipoFlecha5.setPreferredSize(new Dimension(75, 19));

		panelDesplegables5.add(this.grosorFlecha5);
		panelDesplegables5.add(new JPanel());
		panelDesplegables5.add(this.tipoFlecha5);
		panelDesplegables5.add(new JPanel());

		panelJCBs5.add(panelEtiquetas5, BorderLayout.WEST);
		panelJCBs5.add(panelDesplegables5, BorderLayout.EAST);

		// Panel etiquetaImagen
		JPanel panelEtiquetaImagen5 = new JPanel();
		JLabel etiqImagen5 = new JLabel(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/cuadroconf_flechas.gif")));
		panelEtiquetaImagen5.add(etiqImagen5);

		panelFlecha5.add(panelJCBs5, BorderLayout.NORTH);
		panelFlecha5.add(panelEtiquetaImagen5, BorderLayout.CENTER);

		this.cp.setValores(CREANDO_PANEL, 65);

		// *** Panel distancias
		JPanel panelDistancias5 = new JPanel();
		panelDistancias5.setBorder(new TitledBorder(textos[68]));
		panelDistancias5.setLayout(new BorderLayout());

		// Panel JComboBoxes
		JPanel panelJCBs53 = new JPanel();
		panelJCBs53.setLayout(new BorderLayout());

		// Panel etiquetas
		JPanel panelEtiquetas53 = new JPanel();
		panelEtiquetas53.setLayout(new GridLayout(4, 1));

		JLabel etiqsepHorizontal55 = new JLabel(textos[55] + "    ");
		JLabel etiqsepVertical55 = new JLabel(textos[56]);

		panelEtiquetas53.add(etiqsepHorizontal55);
		panelEtiquetas53.add(new JPanel());
		panelEtiquetas53.add(etiqsepVertical55);
		panelEtiquetas53.add(new JPanel());

		// Panel JComboBoxes
		JPanel panelDesplegables53 = new JPanel();
		panelDesplegables53.setLayout(new GridLayout(4, 1));

		this.sepHorizontal5 = new JComboBox<String>();
		for (int i = 0; i < 91; i++) {
			this.sepHorizontal5.addItem((i + 10) + "");
		}
		this.sepHorizontal5.addActionListener(this);
		this.sepHorizontal5.addKeyListener(this);

		this.sepVertical5 = new JComboBox<String>();
		for (int i = 0; i < 91; i++) {
			this.sepVertical5.addItem((i + 10) + "");
		}
		this.sepVertical5.addActionListener(this);
		this.sepVertical5.addKeyListener(this);

		this.sepHorizontal5.setPreferredSize(new Dimension(75, 19));
		this.sepVertical5.setPreferredSize(new Dimension(75, 19));

		panelDesplegables53.add(this.sepHorizontal5);
		panelDesplegables53.add(new JPanel());
		panelDesplegables53.add(this.sepVertical5);
		panelDesplegables53.add(new JPanel());

		panelJCBs53.add(panelEtiquetas53, BorderLayout.WEST);
		panelJCBs53.add(panelDesplegables53, BorderLayout.EAST);

		// Panel etiquetaImagen
		JPanel panelEtiquetaImagen53 = new JPanel();
		JLabel etiqImagen53 = new JLabel(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/cuadroconf_distancias.gif")));
		panelEtiquetaImagen53.add(etiqImagen53);

		panelDistancias5.add(panelJCBs53, BorderLayout.NORTH);
		panelDistancias5.add(panelEtiquetaImagen53, BorderLayout.CENTER);

		// *** Añadir paneles
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = constraints.weighty = 1.0;
		
		panelPestana5.add(panelDistancias5, constraints);	
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = constraints.weighty = 1.0;
		
		panelPestana5.add(panelFlecha5, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 2; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = constraints.weighty = 1.0;
		
		JPanel panelDerecha5 = new JPanel();
		panelDerecha5.setLayout(new GridBagLayout());
		
		panelPestana5.add(panelDerecha5, constraints);

		this.cp.setValores(CREANDO_PANEL, 80);

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font fonts[] = ge.getAllFonts();

		// *** Panel vista código texto
		JPanel panelFuenteCodigo = new JPanel();
		panelFuenteCodigo.setBorder(new TitledBorder(textos[57]));
		panelFuenteCodigo.setLayout(new BorderLayout());

		JLabel etiqFuenteCodigo = new JLabel(textos[58] + "   ");
		JLabel etiqTFCodigo = new JLabel(textos[59]);

		this.fuentesCodigo = new JComboBox<String>();
		for (int i = 0; i < fonts.length; i++) {
			this.fuentesCodigo.addItem(fonts[i].getFontName());
		}

		this.fuentesTamCodigo = new JComboBox<String>();
		for (int i = 0; i < 73; i++) {
			this.fuentesTamCodigo.addItem("" + (i + 8));
		}

		this.fuentesCodigo.addActionListener(this);
		this.fuentesTamCodigo.addActionListener(this);

		JPanel tamFormaCodigo = new JPanel();
		tamFormaCodigo.setLayout(new BorderLayout());
		tamFormaCodigo.add(this.fuentesTamCodigo, BorderLayout.EAST);

		JPanel linea1Codigo = new JPanel();
		linea1Codigo.setLayout(new BorderLayout());
		linea1Codigo.add(etiqFuenteCodigo, BorderLayout.WEST);
		linea1Codigo.add(this.fuentesCodigo, BorderLayout.EAST);

		JPanel linea2Codigo = new JPanel();
		linea2Codigo.setLayout(new BorderLayout());
		linea2Codigo.add(etiqTFCodigo, BorderLayout.WEST);
		linea2Codigo.add(tamFormaCodigo, BorderLayout.EAST);

		panelFuenteCodigo.add(linea1Codigo, BorderLayout.NORTH);
		panelFuenteCodigo.add(linea2Codigo, BorderLayout.SOUTH);		

		// *** Panel vista código traza
		JPanel panelFuenteTraza = new JPanel();
		panelFuenteTraza.setBorder(new TitledBorder(textos[60]));
		panelFuenteTraza.setLayout(new BorderLayout());

		JLabel etiqFuenteTraza = new JLabel(textos[58] + "   ");
		JLabel etiqTFTraza = new JLabel(textos[59]);

		this.fuentesTraza = new JComboBox<String>();
		for (int i = 0; i < fonts.length; i++) {
			this.fuentesTraza.addItem(fonts[i].getFontName());
		}

		this.fuentesTamTraza = new JComboBox<String>();
		for (int i = 0; i < 73; i++) {
			this.fuentesTamTraza.addItem("" + (i + 8));
		}

		this.fuentesTraza.addActionListener(this);
		this.fuentesTamTraza.addActionListener(this);

		JPanel tamFormaTraza = new JPanel();
		tamFormaTraza.setLayout(new BorderLayout());
		tamFormaTraza.add(this.fuentesTamTraza, BorderLayout.EAST);

		JPanel linea1Traza = new JPanel();
		linea1Traza.setLayout(new BorderLayout());
		linea1Traza.add(etiqFuenteTraza, BorderLayout.WEST);
		linea1Traza.add(this.fuentesTraza, BorderLayout.EAST);

		JPanel linea2Traza = new JPanel();
		linea2Traza.setLayout(new BorderLayout());
		linea2Traza.add(etiqTFTraza, BorderLayout.WEST);
		linea2Traza.add(tamFormaTraza, BorderLayout.EAST);

		panelFuenteTraza.add(linea1Traza, BorderLayout.NORTH);
		panelFuenteTraza.add(linea2Traza, BorderLayout.SOUTH);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		
		//TODO Ocultado formato ódigo porque por ahora no funciona
//		panelDerecha5.add(panelFuenteCodigo, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 1; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.weighty = 1;
		
		panelDerecha5.add(panelFuenteTraza, constraints);

		constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.weighty = 1;

		this.pestanas.add(textos[67], panelPestana5);
		this.cp.setValores(CREANDO_PANEL, 90);
		
		this.pestanas.setToolTipTextAt(0, textos[62]);
		this.pestanas.setToolTipTextAt(1, textos[62]);
		this.pestanas.setToolTipTextAt(2, textos[62]);
		this.pestanas.setToolTipTextAt(3, textos[63]);
		this.pestanas.setMnemonicAt(0, 'C');
		this.pestanas.setMnemonicAt(1, 'T');
		this.pestanas.setMnemonicAt(2, 'M');
		
		// ¡¡¡ Adaptar a multidioma !!!
		this.pestanas.setMnemonicAt(3, 'S');
		
		// PANEL BOTONES INFERIORES
		JPanel panelBotones = new JPanel();
		panelBotones.add(this.aceptar);
		panelBotones.add(this.cancelar);
		this.aceptar.addMouseListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar.addMouseListener(this);
		this.cancelar.addKeyListener(this);

		// Panel general
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(this.pestanas, BorderLayout.NORTH);
		panel.add(panelBotones, BorderLayout.SOUTH);

		setValores();
		this.jcc1.setColor(this.etiqColores1[0].getBackground());
		this.jcc2.setColor(this.etiqColores2[0].getBackground());
		this.jcc3.setColor(this.etiqColores3[0].getBackground());

		// Evita refrescos innecesarios en ventanas
		this.creandoCuadro = false;

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);

		this.dialogo.setTitle(textos[64]);
		this.cp.setValores(CREANDO_PANEL, 95);

		if (this.ventana.msWindows) {
			this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
			this.dialogo.setLocation(coord[0], coord[1]);
		} else {
			this.dialogo.setSize(ANCHO_CUADRO_NO_WINDOWS,
					ALTO_CUADRO_NO_WINDOWS);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO_NO_WINDOWS,
					ALTO_CUADRO_NO_WINDOWS);
			this.dialogo.setLocation(coord[0], coord[1]);
		}
		this.cp.cerrar();
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);

	}

	/**
	 * Crea un nuevo panel para la selección de un determinado elemento del
	 * árbol, para después asignarle un color.
	 * 
	 * @param etiqColor
	 *            etiqueta con el color actual.
	 * 
	 * @return panel
	 */
	private JPanel creaPanelColor(JLabel etiqColor) {
		JPanel panelEtiqueta = new JPanel();
		panelEtiqueta.add(etiqColor);
		panelEtiqueta.setToolTipText(MARCAR_PANEL);
		panelEtiqueta.setForeground(new Color(180, 0, 0));
		panelEtiqueta.setBackground(new Color(180, 0, 0));
		panelEtiqueta.setPreferredSize(new Dimension(25, 12));
		return panelEtiqueta;
	}

	/**
	 * Lee de la configuración las distintas opciones para actualizarlas en el
	 * cuadro.
	 */
	private void setValores() {
		int color[] = new int[3];

		for (int i = 0; i < NUM_SELECTORES_1; i++) {
			switch (i) {

			case 0:
				color = this.ocv.getColorC1Entrada();
				break;
			case 1:
				color = this.ocv.getColorCAEntrada();
				break;
			case 2:
				color = this.ocv.getColorC1Salida();
				break;
			case 3:
				color = this.ocv.getColorCASalida();
				break;
			case 4:
				color = this.ocv.getColorC1NCSalida();
				break;

			default:
				color[0] = color[1] = color[2] = 0;
			}
			this.panelesColores1[i].setForeground(new Color(color[0], color[1],
					color[2]));
			this.panelesColores1[i].setBackground(new Color(color[0], color[1],
					color[2]));
			this.etiqColores1[i].setForeground(new Color(color[0], color[1],
					color[2]));
			this.etiqColores1[i].setBackground(new Color(color[0], color[1],
					color[2]));
		}

		for (int i = 0; i < NUM_SELECTORES_3; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!this.selectores3[i].isVisible()){
				continue;
			}
			switch (i) {
			case 0:
				color = this.ocv.getColorFEntrada();
				break;

			case 1:
				color = this.ocv.getColorFSalida();
				break;

			case 2:
				color = this.ocv.getColorPanel();
				break;
			case 3:
				color = this.ocv.getColorFlecha();
				break;
			case 4:
				color = this.ocv.getColorActual();
				break;
			case 5:
				color = this.ocv.getColorCActual();
				break;
			case 11:
				color = this.ocv.getColorIluminado();
				break;
			case 12:
				color = this.ocv.getColorResaltado();
				break;
			case 13:
				color = this.ocv.getColorMarcoFamilia();
				break;
			case 14:
				color = this.ocv.getColorErroresCodigo();
				break;
			default:
				color[0] = color[1] = color[2] = 0;
			}
			this.panelesColores3[i].setForeground(new Color(color[0], color[1],
					color[2]));
			this.panelesColores3[i].setBackground(new Color(color[0], color[1],
					color[2]));
			this.etiqColores3[i].setForeground(new Color(color[0], color[1],
					color[2]));
			this.etiqColores3[i].setBackground(new Color(color[0], color[1],
					color[2]));
		}
		
		this.comboThemes.setSelectedIndex(this.ocv.getTemaColorEditor());

		for (int i = 0; i < NUM_SELECTORES_2; i++) {
			this.panelesColores2[i].setForeground(Conf.coloresNodo[i]);
			this.panelesColores2[i].setBackground(Conf.coloresNodo[i]);
			this.etiqColores2[i].setForeground(Conf.coloresNodo[i]);
			this.etiqColores2[i].setBackground(Conf.coloresNodo[i]);
		}

		if (Conf.modoColor == 1) {
			this.checkModo1.setSelected(true);
			habilitarElementosModo1(this.checkModo1.isSelected());
			habilitarElementosModo2(!this.checkModo1.isSelected());
		} else if (Conf.modoColor == 2) {
			this.checkModo2.setSelected(true);
			habilitarElementosModo2(this.checkModo2.isSelected());
			habilitarElementosModo1(!this.checkModo2.isSelected());
		}

		this.colorDegradado[0].setSelected(this.ocv.getColorDegradadoModo1());
		this.colorDegradado[1].setSelected(this.ocv.getColorDegradadoModo2());

		// con -1 convertimos de ancho real a posición de lista
		this.grosorFlecha.setSelectedIndex(this.ocv.getGrosorFlecha() - 1);

		this.tipoFlecha.setSelectedIndex(this.ocv.getFormaFlecha());
		this.grosorMarco.setSelectedIndex(this.ocv.getGrosorActual());
		this.bordeCelda.setSelectedIndex(this.ocv.getTipoBordeCelda());

		this.sepHorizontal.setSelectedIndex(this.ocv.getDistanciaH());
		this.sepVertical.setSelectedIndex(this.ocv.getDistanciaV());

		this.fuentesCodigo.setSelectedItem(this.ocv.getFuenteCodigo());
		this.fuentesTamCodigo.setSelectedItem(""
				+ this.ocv.getTamFuenteCodigo());

		this.fuentesTraza.setSelectedItem(this.ocv.getFuenteTraza());
		this.fuentesTamTraza.setSelectedItem("" + this.ocv.getTamFuenteTraza());
		
		//	Pestaña 5
		this.sepHorizontal5.setSelectedIndex(this.ocv.getDistanciaHGrafo());
		this.sepVertical5.setSelectedIndex(this.ocv.getDistanciaVGrafo());
		
		this.grosorFlecha5.setSelectedIndex(this.ocv.getGrosorFlechaGrafo() - 1);
		this.tipoFlecha5.setSelectedIndex(this.ocv.getTipoFlechaGrafo());
	}

	/**
	 * Actualiza la configuración con los valores establecidos por el usuario.
	 */
	private void getValores() {
		Color c;

		// Colores pestaña 1 (árbol y pila)
		c = this.etiqColores1[0].getBackground();
		this.ocv.setColorC1Entrada(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores1[1].getBackground();
		this.ocv.setColorCAEntrada(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores1[2].getBackground();
		this.ocv.setColorC1Salida(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores1[3].getBackground();
		this.ocv.setColorCASalida(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores1[4].getBackground();
		this.ocv.setColorC1NCSalida(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores3[0].getBackground();
		this.ocv.setColorFEntrada(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores3[1].getBackground();
		this.ocv.setColorFSalida(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores3[2].getBackground();
		this.ocv.setColorPanel(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores3[3].getBackground();
		this.ocv.setColorFlecha(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores3[4].getBackground();
		this.ocv.setColorActual(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores3[5].getBackground();
		this.ocv.setColorCActual(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores3[11].getBackground();
		this.ocv.setColorIluminado(c.getRed(), c.getGreen(), c.getBlue());

		c = this.etiqColores3[12].getBackground();
		this.ocv.setColorResaltado(c.getRed(), c.getGreen(), c.getBlue());
		
		c = this.etiqColores3[13].getBackground();
		this.ocv.setColorMarcoFamilia(c.getRed(), c.getGreen(), c.getBlue());
		
		c = this.etiqColores3[14].getBackground();
		this.ocv.setColorErroresCodigo(c.getRed(), c.getGreen(), c.getBlue());
		
		this.ocv.setTemaColorEditor(this.comboThemes.getSelectedIndex());

		this.ocv.setGrosorActual(this.grosorMarco.getSelectedIndex());
		this.ocv.setGrosorFlecha(this.grosorFlecha.getSelectedIndex() + 1);
		this.ocv.setFormaFlecha(this.tipoFlecha.getSelectedIndex());
		this.ocv.setTipoBordeCelda(this.bordeCelda.getSelectedIndex());

		this.ocv.setModoColor(Conf.modoColor);

		for (int i = 0; i < Conf.numColoresMetodos; i++) {
			this.ocv.setColorModo2(this.etiqColores2[i].getBackground()
					.getRed(), this.etiqColores2[i].getBackground().getGreen(),
					this.etiqColores2[i].getBackground().getBlue(), i);
		}

		this.ocv.setColorDegradadoModo1(this.colorDegradado[0].isSelected());
		this.ocv.setColorDegradadoModo2(this.colorDegradado[1].isSelected());

		this.ocv.setDistanciaH(this.sepHorizontal.getSelectedIndex());
		this.ocv.setDistanciaV(this.sepVertical.getSelectedIndex());

		Integer iAux = Integer.parseInt((String) this.fuentesTamCodigo
				.getSelectedItem());
		this.ocv.setFuenteCodigo((String) this.fuentesCodigo.getSelectedItem(),
				iAux.intValue());
		iAux = Integer
				.parseInt((String) this.fuentesTamTraza.getSelectedItem());
		this.ocv.setFuenteTraza((String) this.fuentesTraza.getSelectedItem(),
				iAux.intValue());// ,(String)fuentesTraza.getSelectedItem() );
		
		//	Pestaña 5
		this.ocv.setDistanciaHGrafo(this.sepHorizontal5.getSelectedIndex());
		this.ocv.setDistanciaVGrafo(this.sepVertical5.getSelectedIndex());
		
		this.ocv.setGrosorFlechaGrafo(this.grosorFlecha5.getSelectedIndex() +1);
		this.ocv.setTipoFlechaGrafo(this.tipoFlecha5.getSelectedIndex());
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Esto debe ir en guardadoRecarga, para que se almacene en la opción
		if (e.getSource() == this.checkModo1) {
			Conf.modoColor = (this.checkModo1.isSelected() ? 1 : 2);
		}
		if (e.getSource() == this.checkModo2) {
			Conf.modoColor = (this.checkModo2.isSelected() ? 2 : 1);
		}

		if (e.getSource() == this.aceptar) {
			guardadoRecarga();
		} else if (e.getSource().getClass().getName().contains("JRadioButton")) {
			for (int i = 0; i < NUM_SELECTORES_1; i++) {
				if (this.selectores1[i].isSelected()) {
					this.jcc1.setColor(this.etiqColores1[i].getBackground());
				}
			}

			for (int i = 0; i < NUM_SELECTORES_2; i++) {
				if (this.selectores2[i].isSelected()) {
					this.jcc2.setColor(this.etiqColores2[i].getBackground());
				}
			}

			for (int i = 0; i < NUM_SELECTORES_3; i++) {
				//	TODO Esto hay que cambiarlo, eliminando los selectores, 
				//	textos y códigos estos radio buttons, no ocultándolos
				if(!this.selectores3[i].isVisible()){
					continue;
				}
				if (this.selectores3[i].isSelected()) {
					this.jcc3.setColor(this.etiqColores3[i].getBackground());
				}
			}
		} else if (e.getSource().getClass().getName().contains("JComboBox")
				|| e.getSource().getClass().getName().contains("JCheckBox")) {
			if (e.getSource() == this.checkModo1) {
				habilitarElementosModo1(this.checkModo1.isSelected());
				habilitarElementosModo2(!this.checkModo1.isSelected());
			} else if (e.getSource() == this.checkModo2) {
				habilitarElementosModo2(this.checkModo2.isSelected());
				habilitarElementosModo1(!this.checkModo2.isSelected());
			} else if(e.getSource() == this.comboThemes){
				this.ventana.getPanelVentana().getPanelAlgoritmo().changeTheme(this.comboThemes.getSelectedIndex());
			}

			Conf.degradado1 = this.colorDegradado[0].isSelected();
			Conf.degradado2 = this.colorDegradado[1].isSelected();

			if (!this.creandoCuadro) {
				// formar cuadro de diálogo
				guardadoRecarga();
			}
		}
	}

	/**
	 * Habilita o deshabilita los elementos del modo 1
	 * 
	 * @param valor
	 *            true para habilitar, false para deshabilitar.
	 */
	private void habilitarElementosModo1(boolean valor) {
		this.checkModo1.setSelected(valor);
		this.colorDegradado[0].setEnabled(valor);
		this.jcc1.setEnabled(valor);
		for (int i = 0; i < this.selectores1.length; i++) {
			this.selectores1[i].setEnabled(valor);
		}
		this.asignar1.setEnabled(valor);
		this.resetear1.setEnabled(valor);
		this.jcc1.setEnabled(valor);
	}

	/**
	 * Habilita o deshabilita los elementos del modo 2
	 * 
	 * @param valor
	 *            true para habilitar, false para deshabilitar.
	 */
	private void habilitarElementosModo2(boolean valor) {
		this.checkModo2.setSelected(valor);
		this.colorDegradado[1].setEnabled(valor);
		this.jcc2.setEnabled(valor);
		for (int i = 0; i < this.selectores2.length; i++) {
			this.selectores2[i].setEnabled(valor);
		}
		this.asignar2.setEnabled(valor);
		this.resetear2.setEnabled(valor);
		this.jcc2.setEnabled(valor);
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_DOWN) {
			for (int i = 0; i < this.selectores1.length; i++) {
				if (this.selectores1[i].isFocusOwner()) {
					if (i != this.selectores1.length - 1) {
						this.selectores1[i].transferFocus();
					}
				}
			}
			for (int i = 0; i < this.selectores2.length; i++) {
				if (this.selectores2[i].isFocusOwner()) {
					if (i != this.selectores2.length - 1) {
						this.selectores2[i].transferFocus();
					}
				}
			}
		} else if (code == KeyEvent.VK_UP) {
			for (int i = 0; i < this.selectores1.length; i++) {
				if (this.selectores1[i].isFocusOwner()) {
					if (i != 0) {
						this.selectores1[i].transferFocusBackward();
					}
				}
			}
			for (int i = 0; i < this.selectores2.length; i++) {
				if (this.selectores2[i].isFocusOwner()) {
					if (i != 0) {
						this.selectores2[i].transferFocusBackward();
					}
				}
			}
		}

		if (e.getComponent() == this.aceptar) {
			this.dialogo.setVisible(false);
		} else if (e.getComponent() == this.asignar1) {
			accionAsignar1();
		} else if (e.getComponent() == this.asignar2) {
			accionAsignar2();
		} else if (e.getComponent() == this.asignar3) {
			accionAsignar3();
		} else if (e.getComponent() == this.resetear1) {
			accionResetear1();
		} else if (e.getComponent() == this.resetear2) {
			accionResetear2();
		} else if (e.getComponent() == this.resetear3) {
			accionResetear3();
		}

		if (e.getComponent() == this.asignar1
				|| e.getComponent() == this.asignar2
				|| e.getComponent() == this.asignar3
				|| e.getComponent() == this.aceptar) {
			guardadoRecarga();
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyReleased(KeyEvent e) {

		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		} else if (code == KeyEvent.VK_ENTER) {
			guardadoRecarga();
			this.dialogo.setVisible(false);
		}

	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getComponent().getClass().getCanonicalName().contains("JPanel")) {
			for (int i = 0; i < NUM_SELECTORES_1; i++) {
				if (this.panelesColores1[i] == e.getComponent()) {
					this.selectores1[i].setSelected(true);
					this.jcc1.setColor(this.etiqColores1[i].getBackground());
				}
			}
			for (int i = 0; i < NUM_SELECTORES_2; i++) {
				if (this.panelesColores2[i] == e.getComponent()
						&& this.selectores2[i].isEnabled()) {
					this.selectores2[i].setSelected(true);
					this.jcc2.setColor(this.etiqColores2[i].getBackground());
				}
			}
			for (int i = 0; i < NUM_SELECTORES_3; i++) {
				//	TODO Esto hay que cambiarlo, eliminando los selectores, 
				//	textos y códigos estos radio buttons, no ocultándolos
				if(!this.selectores3[i].isVisible()){
					continue;
				}
				if (this.panelesColores3[i] == e.getComponent()
						&& this.selectores3[i].isEnabled()) {
					this.selectores3[i].setSelected(true);
					this.jcc3.setColor(this.etiqColores3[i].getBackground());
				}
			}
		} else if (e.getComponent().getClass().getCanonicalName()
				.contains("JRadioButton")) {
			for (int i = 0; i < NUM_SELECTORES_1; i++) {
				if (this.selectores1[i] == e.getComponent()) {
					this.jcc1.setColor(this.etiqColores1[i].getBackground());
				}
			}

			for (int i = 0; i < NUM_SELECTORES_2; i++) {
				if (this.selectores2[i] == e.getComponent()
						&& this.selectores2[i].isEnabled()) {
					this.jcc2.setColor(this.etiqColores2[i].getBackground());
				}
			}
			for (int i = 0; i < NUM_SELECTORES_3; i++) {
				//	TODO Esto hay que cambiarlo, eliminando los selectores, 
				//	textos y códigos estos radio buttons, no ocultándolos
				if(!this.selectores3[i].isVisible()){
					continue;
				}
				if (this.selectores3[i] == e.getComponent()
						&& this.selectores3[i].isEnabled()) {
					this.jcc3.setColor(this.etiqColores3[i].getBackground());
				}
			}
		} else if (e.getComponent().getClass().getCanonicalName()
				.contains("BotonAceptar")) {
			this.dialogo.setVisible(false);
			guardadoRecarga();
		} else if (e.getComponent() instanceof JButton) {
			if (e.getComponent() == this.aceptar) {
				this.dialogo.setVisible(false);
			}

			if (e.getComponent() == this.asignar1) {
				accionAsignar1();
			} else if (e.getComponent() == this.asignar2) {
				accionAsignar2();
			} else if (e.getComponent() == this.asignar3) {
				accionAsignar3();
			} else if (e.getComponent() == this.resetear1) {
				accionResetear1();
			} else if (e.getComponent() == this.resetear2) {
				accionResetear2();
			} else if (e.getComponent() == this.resetear3) {
				accionResetear3();
			}

			if (e.getComponent() == this.asignar1
					|| e.getComponent() == this.asignar2
					|| e.getComponent() == this.asignar3
					|| e.getComponent() == this.aceptar) {
				guardadoRecarga();
			}

		} else if (e.getSource().getClass().getName().contains("JComboBox")
				|| e.getSource().getClass().getName().contains("JCheckBox")) {
			if (!this.creandoCuadro) {
				// formar cuadro de diálogo
				guardadoRecarga();
			}
		}

	}
	
	/**
	 * Asigna el color seleccionado al elemento seleccionado del Modo 1.
	 */
	private void accionAsignar1() {
		Color c = this.jcc1.getColor();

		if (this.asignar1.isEnabled()) {
			for (int i = 0; i < NUM_SELECTORES_1; i++) {
				if (this.selectores1[i].isSelected()) {
					this.panelesColores1[i].setForeground(c);
					this.panelesColores1[i].setBackground(c);
					this.etiqColores1[i].setForeground(c);
					this.etiqColores1[i].setBackground(c);
				}
			}
		}
	}
	
	/**
	 * Asigna el color seleccionado al elemento seleccionado del Modo 2.
	 */
	private void accionAsignar2() {
		Color c = this.jcc2.getColor();

		if (this.asignar2.isEnabled()) {
			for (int i = 0; i < NUM_SELECTORES_2; i++) {
				if (this.selectores2[i].isSelected()) {
					this.panelesColores2[i].setForeground(c);
					this.panelesColores2[i].setBackground(c);
					this.etiqColores2[i].setForeground(c);
					this.etiqColores2[i].setBackground(c);
				}
			}
		}
	}
	
	/**
	 * Asigna el color seleccionado al elemento seleccionado del Modo 3.
	 */
	private void accionAsignar3() {
		Color c = this.jcc3.getColor();

		for (int i = 0; i < NUM_SELECTORES_3; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!this.selectores3[i].isVisible()){
				continue;
			}
			if (this.selectores3[i].isSelected()) {
				this.panelesColores3[i].setForeground(c);
				this.panelesColores3[i].setBackground(c);
				this.etiqColores3[i].setForeground(c);
				this.etiqColores3[i].setBackground(c);
			}
		}
	}
	
	/**
	 * Restablece el color del elemento seleccionado del Modo 1.
	 */
	private void accionResetear1() {
		if (this.resetear1.isEnabled()) {
			for (int i = 0; i < NUM_SELECTORES_1; i++) {
				if (this.selectores1[i].isSelected()) {
					this.jcc1.setColor(this.etiqColores1[i].getBackground());
				}
			}
		}
	}
	
	/**
	 * Restablece el color del elemento seleccionado del Modo 2.
	 */
	private void accionResetear2() {
		if (this.resetear2.isEnabled()) {
			for (int i = 0; i < NUM_SELECTORES_2; i++) {
				if (this.selectores2[i].isSelected()) {
					this.jcc2.setColor(this.etiqColores2[i].getBackground());
				}
			}
		}
	}
	
	/**
	 * Restablece el color del elemento seleccionado del Modo 3.
	 */
	private void accionResetear3() {
		for (int i = 0; i < NUM_SELECTORES_3; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!this.selectores3[i].isVisible()){
				continue;
			}
			if (this.selectores3[i].isSelected()) {
				this.jcc3.setColor(this.etiqColores3[i].getBackground());
			}
		}
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		}
	}
	
	/**
	 * Guarda la configuración y refresca el formato de visualización.
	 */
	private void guardadoRecarga() {
		getValores();
		this.gOpciones.setOpcion(this.ocv, 1);
		Conf.setValoresVisualizacion();
		if (this.ventana.traza != null) {
			Conf.setRedibujarGrafoArbol(true);
			Conf.setRedibujarGrafoArbol(false);
		}
		// Debe ejecutarse tras la actualización de Conf
		this.ventana.refrescarFormato(); 
	}
}
