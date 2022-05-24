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
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import conf.*;
//import javafx.scene.layout.Border;
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
	
	private static final int ANCHO_CUADRO = 685;//Original 900 Ingles 675
	
	
	private static final int ALTO_CUADRO = 485;// 485;
	private static final int ANCHO_CUADRO_NO_WINDOWS = 900;
	private static final int ALTO_CUADRO_NO_WINDOWS = 465;

	// Num. selectores en pestaña 1
	private static final int NUM_SELECTORES_1 = 5;
	// Num. selectores en pestaña 2
	private static final int NUM_SELECTORES_2 = 
			Conf.numColoresMetodos;
	// Num. selectores en pestaña 3
	private static final int NUM_SELECTORES_3 = 3;
	// Num. selectores en pestaña 4
	private static final int NUM_SELECTORES_4 = 15;
	// Num. selectores en pestaña 5
	private static final int NUM_SELECTORES_5 = 2;
	private final static String CREANDO_PANEL = 
			Texto.get("CP_CREARPAN", Conf.idioma);
	private final static String MARCAR_PANEL = 
			Texto.get("COCV_MARCAR", Conf.idioma);

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
	private JComboBox<String> fuentesCodigo;
	private JComboBox<String> grosorLineas;


	private JComboBox<Object> fuentesTraza;
	private JComboBox<String> fuentesTamCodigo, fuentesTamTraza;
	private JComboBox<String> comboThemes;
	
	private JColorChooser jcc1, jcc2, jcc3, jcc4, jcc5;

	private JRadioButton[] selectores1 = new JRadioButton[NUM_SELECTORES_1];
	private JRadioButton[] selectores2 = new JRadioButton[NUM_SELECTORES_2];
	private JRadioButton[] selectores3 = new JRadioButton[NUM_SELECTORES_3];
	private JRadioButton[] selectores4 = new JRadioButton[NUM_SELECTORES_4];
	private JRadioButton[] selectores5 = new JRadioButton[NUM_SELECTORES_5];
	private JLabel[] etiqColores1 = new JLabel[NUM_SELECTORES_1];
	private JLabel[] etiqColores2 = new JLabel[NUM_SELECTORES_2];
	private JLabel[] etiqColores3 = new JLabel[NUM_SELECTORES_3];
	private JLabel[] etiqColores4 = new JLabel[NUM_SELECTORES_4];
	private JLabel[] etiqColores5 = new JLabel[NUM_SELECTORES_5];
	private JPanel[] panelesColores1 = new JPanel[NUM_SELECTORES_1];
	private JPanel[] panelesColores2 = new JPanel[NUM_SELECTORES_2];
	private JPanel[] panelesColores3 = new JPanel[NUM_SELECTORES_3];
	private JPanel[] panelesColores4 = new JPanel[NUM_SELECTORES_4];
	private JPanel[] panelesColores5 = new JPanel[NUM_SELECTORES_5];
	

	private BotonTexto asignar1, resetear1, asignar2, resetear2, 
		asignar3, resetear3, asignar4, resetear4, asignar5, resetear5;

	private CuadroProgreso cp;

	private boolean creandoCuadro = false;

	/**
	 * Genera un nuevo cuadro que maneja la opción que gestiona la configuración
	 * de la visualización.
	 * 
	 * @param ventana
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo
	 */
	public CuadroOpcionConfVisualizacion(Ventana ventana2) {
		dialogo = new JDialog(ventana, true);
		ventana = ventana2;
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
		String[] codigos = { 
				"COCV_SELEC", /* 0 */
				"COCV_FUENTEE", /* 1 */
				"COCV_CELDE1", /* 2 */
				"COCV_CELDEA1", /* 3 */
				"COCV_FUENTES", /* 4 */
				"COCV_CELDS1", /* 5 */
				"COCV_CELDSA1", /* 6 */
				"COCV_CELDSNC1", /* 7 */
				"COCV_FONDOPANEL", /* 8 */
				"COCV_FLECHA", /* 9 */
				"COCV_MARCONODACT", /* 10 */
				"COCV_MARCOCAMACT", /* 11 */
				"COCV_MARCAR", /* 12 */
				"COCV_HABDEGR", /* 13 */
				"COCV_HABDEGR_TTT", /* 14 */
				"BOTONASIGNAR", /* 15 */
				"BOTONDESCARTAR", /* 16 */
				"COCV_ETIQ_1", /* 17 */
				"COCV_PALRESERV", /* 18 */
				"COCV_COMENT", /* 19 */
				"COCV_MET", /* 20 */
				"COCV_METFON", /* 21 */
				"COCV_CODIGO", /* 22 */
				"COCV_HABILDEGR", /* 23 */
				"COCV_ETIQ_3", /* 24 */
				"COCV_COL_ILUM", /* 25 */ 
				"COCV_TRENA", /* 26 */
				"COCV_TRSAA", /* 27 */
				"COCV_TRPANEL", /* 28 */
				"COCV_SUBORDCOL", /* 29 */
				"COCV_SUBORDCOLTR", /* 30 */
				"COCV_SUBORDCOLCO", /* 31 */
				"COCV_SUBORDCOLCE", /* 32 */
				"COCV_ETIQ_2", /* 33 */
				"COCV_CONFFLEC", /* 34 */
				"COCV_CONFFLECANCH", /* 35 */
				"COCV_CONFFLECTIPO", /* 36 */
				"COCV_CONFFLECTIPONEU", /* 37 */
				"COCV_CONFFLECTIPOTEC", /* 38 */
				"COCV_CONFFLECTIPOCLA", /* 39 */
				"COCV_CONFFLECTIPOSIM", /* 40 */
				"COCV_CONFFLECTIPOROM", /* 41 */
				"COCV_CONFFLECTIPOSLI", /* 42 */
				"COCV_CONFFLECTIPODLI", /* 43 */
				"COCV_CONFFLECTIPOCIR", /* 44 */
				"COCV_CARACCELD", /* 45 */
				"COCV_CARACCELDMARC", /* 46 */
				"COCV_CARACCELDMARCT", /* 47 */
				"COCV_CARACCELDBORDV", /* 48 */
				"COCV_CARACCELDBORDB", /* 49 */
				"COCV_CARACCELDBORDG", /* 50 */
				"COCV_CARACCELDBORDL", /* 51 */
				"COCV_CARACCELDBORDBB", /* 52 */
				"COCV_CARACCELDBORDBL", /* 53 */
				"COCV_DISTCELD", /* 54 */
				"COCV_DISTCELDH", /* 55 */
				"COCV_DISTCELDV", /* 56 */
				"COCV_FUENTCOD", /* 57 */
				"COCV_FUENT", /* 58 */
				"COCV_FUENTTAM", /* 59 */
				"COCV_FUENTTR", /* 60 */
				"COCV_ETIQ_4", /* 61 */
				"COCV_ETIQ_ARBPILCODTRATTT", /* 62 */
				"COCV_ETIQ_TAMFORTTT", /* 63 */
				"COCV_FORMATO", /* 64 */
				"COCV_COL_RESA", /* 65 */
				"COCV_COL_SEL_ARBOL", /* 66 */
				"COCV_ETIQ_5", /*67*/
				"COCV_DISTCELD_G", /* 68 */
				"COCV_CONFFLEC_G", /* 69 */
				"COCV_COL_COD_ERR", /* 70 */
				"COCV_COL_COD_THEME_TITLE", /* 71 */
				"COCV_COL_COD_THEME_DEFAULT", /* 72 */
				"COCV_COL_COD_THEME_DARK", /* 73 */
				"COCV_COL_COD_THEME_ECLIPSE", /* 74 */
				"COCV_COL_COD_THEME_IDEA", /* 75 */
				"COCV_COL_COD_THEME_MONOKAI", /* 76 */
				"COCV_COL_COD_THEME_VISUALSTUDIO", /* 77 */
				"COCV_TEXTO", /* 78 */
				"COCV_ETIQ_6", /* 79 */
				"COCV_MODO1", /* 80 */
				"COCV_MODO2", /* 81 */
				"CP_ESPERE", /* 82 */
				"COCV_COLOR", /* 83 */
				"CMP_METPRINC", /* 84 */
				"COCV_SOL_PARC", /* 85 */
				"COCV_SOL_MEJ", /* 86 */
				"COCV_COTA", /* 87 */
				"COCV_LINEAS_GROSOR" /* 88 */

		// 26 a 28 no se están usando
		// 29 a 32 se podrían no estar usando (comprobar)
		};

		String textos[] = Texto.get(codigos, Conf.idioma);
		

		cp = new CuadroProgreso(ventana, textos[82], CREANDO_PANEL, 0);

		// Evita refrescos innecesarios en ventanas al formar cuadro de diálogo
		creandoCuadro = true;

		ocv = (OpcionConfVisualizacion) gOpciones.getOpcion(
				"OpcionConfVisualizacion", false);

		cp.setValores(CREANDO_PANEL, 10);

		// Creamos estructura de pestañas
		pestanas = new JTabbedPane();
		pestanas.addMouseListener(this);
		pestanas.addKeyListener(this);
		
		createTab1(textos);
		cp.setValores(CREANDO_PANEL, 20);
		createTab2(textos);
		cp.setValores(CREANDO_PANEL, 30);
		createTab3(textos);
		cp.setValores(CREANDO_PANEL, 40);
		createTab4(textos);
		cp.setValores(CREANDO_PANEL, 50);
		createTab5(textos);
		cp.setValores(CREANDO_PANEL, 60);
		createTab6(textos);
		cp.setValores(CREANDO_PANEL, 70);
		createTab7(textos);
		cp.setValores(CREANDO_PANEL, 80);
		
		pestanas.setToolTipTextAt(0, textos[62]);
		pestanas.setToolTipTextAt(1, textos[62]);
		pestanas.setToolTipTextAt(2, textos[62]);
		pestanas.setToolTipTextAt(3, textos[63]);
		pestanas.setMnemonicAt(0, 'C');
		pestanas.setMnemonicAt(1, 'T');
		pestanas.setMnemonicAt(2, 'M');
		
		// ¡¡¡ Adaptar a multidioma !!!
		pestanas.setMnemonicAt(3, 'S');
		
		// PANEL BOTONES INFERIORES
		JPanel panelBotones = new JPanel();
		panelBotones.add(aceptar);
		panelBotones.add(cancelar);
		aceptar.addMouseListener(this);
		aceptar.addKeyListener(this);
		cancelar.addMouseListener(this);
		cancelar.addKeyListener(this);

		// Panel general
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(pestanas, BorderLayout.NORTH);
		panel.add(panelBotones, BorderLayout.SOUTH);

		setValores();
		jcc1.setColor(etiqColores1[0].getBackground());
		jcc2.setColor(etiqColores2[0].getBackground());
		jcc3.setColor(etiqColores3[0].getBackground());
		jcc4.setColor(etiqColores4[0].getBackground());
		jcc5.setColor(etiqColores5[0].getBackground());

		// Evita refrescos innecesarios en ventanas
		creandoCuadro = false;

		// Preparamos y mostramos cuadro
		dialogo.getContentPane().add(panel);

		dialogo.setTitle(textos[64]);
		cp.setValores(CREANDO_PANEL, 90);

		int coord[];
		if (ventana.msWindows) {
			dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
			coord = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
			dialogo.setLocation(coord[0], coord[1]);
		} else {
			dialogo.setSize(ANCHO_CUADRO_NO_WINDOWS, ALTO_CUADRO_NO_WINDOWS);
			coord = Conf.ubicarCentro(ANCHO_CUADRO_NO_WINDOWS, ALTO_CUADRO_NO_WINDOWS);
			dialogo.setLocation(coord[0], coord[1]);
		}
		cp.cerrar();
		dialogo.setResizable(true);
		dialogo.setVisible(true);

	}

	private void createTab1(String[] textos) {
		// PESTAÑA 1 . MODO DE COLORES QUE DISTINGUE ENTRE ENTRADA Y SALIDA EN CADA NODO
		
		// Panel Izquierda
		JPanel panelIzquierda = new JPanel();
		panelIzquierda.setLayout(new BorderLayout());

		// Panel checkbox modo
		JPanel pCheckModo1 = new JPanel();
		checkModo1 = new JCheckBox(textos[80]);
		pCheckModo1.add(checkModo1);
		checkModo1.addActionListener(this);
		checkModo1.addMouseListener(this);
		checkModo1.addKeyListener(this);

		// Panel colores
		JPanel panelSeleccionColores = new JPanel();
		panelSeleccionColores.setBorder(new TitledBorder(textos[0]));
		panelSeleccionColores.setLayout(new GridLayout(NUM_SELECTORES_1, 1));

		ButtonGroup bg1 = new ButtonGroup();
		
		selectores1 = new JRadioButton[NUM_SELECTORES_1];
		selectores1[0] = new JRadioButton(textos[2]);
		selectores1[1] = new JRadioButton(textos[3]);
		selectores1[2] = new JRadioButton(textos[5]);
		selectores1[3] = new JRadioButton(textos[6]);
		selectores1[4] = new JRadioButton(textos[7]);
		
		selectores1[0].setSelected(true);

		for (int i = 0; i < NUM_SELECTORES_1; i++) {
			selectores1[i].addKeyListener(this);
			selectores1[i].addActionListener(this);
			selectores1[i].addMouseListener(this);
			selectores1[i].setToolTipText(textos[12]);
			bg1.add(selectores1[i]);
			panelSeleccionColores.add(selectores1[i]);
		}

		JPanel panelFila[] = new JPanel[NUM_SELECTORES_1];
		
		for (int i = 0; i < NUM_SELECTORES_1; i++) {
			panelFila[i] = new JPanel();
			panelFila[i].setLayout(new BorderLayout());
			panelFila[i].add(selectores1[i], BorderLayout.WEST);
			etiqColores1[i] = new JLabel("");
			etiqColores1[i].addMouseListener(this);
			etiqColores1[i].setForeground(new Color(0, 0, 0));
			etiqColores1[i].setBackground(new Color(0, 0, 0));
			panelesColores1[i] = creaPanelColor(etiqColores1[i]);
			panelesColores1[i].addMouseListener(this);
			panelFila[i].add(panelesColores1[i], BorderLayout.EAST);
			panelSeleccionColores.add(panelFila[i]);
		}
		//Original panelSeleccionColores.setPreferredSize(new Dimension(270, 300));
		//Ingles panelSeleccionColores.setPreferredSize(new Dimension(200, 300));
		if(Conf.idioma.equals("es")) {
			panelSeleccionColores.setPreferredSize(new Dimension(180, 300));
		}
		else {panelSeleccionColores.setPreferredSize(new Dimension(200, 300));}
		// Panel Degradados
		JPanel panelDegradados1 = new JPanel();
		panelDegradados1.setBorder(new TitledBorder(textos[13]));
		panelDegradados1.setLayout(new GridLayout(1, 1));
		colorDegradado[0] = new JCheckBox(textos[23]);
		panelDegradados1.add(colorDegradado[0]);
		colorDegradado[0].addActionListener(this);
		colorDegradado[0].setToolTipText(textos[14]);

		panelIzquierda.add(pCheckModo1, BorderLayout.NORTH);
		panelIzquierda.add(panelSeleccionColores, BorderLayout.CENTER);
		panelIzquierda.add(panelDegradados1, BorderLayout.SOUTH);

		// Panel Derecha
		JPanel panelDerecha = new JPanel();
		panelDerecha.setLayout(new BorderLayout());

		jcc1 = new JColorChooser(new Color(0, 0, 0));;
		Dimension d= new Dimension();
		Dimension d12= new Dimension();
		//Ingles d.setSize(450, 300);
		//Ingles d12.setSize(425, 200);
		d.setSize(434, 300);
		d12.setSize(430, 200);
		//String idioma =Conf.idioma;
		jcc1.setPreferredSize(d);

		AbstractColorChooserPanel[] ab=	jcc1.getChooserPanels();
		for(int i =1;i<ab.length;i++) {
			ab[i].setPreferredSize(d12);
		}
		jcc1.setChooserPanels(ab);
		jcc1.addMouseListener(this);

		// Panel Botones Asignar y Borrar
		JPanel panelBotonesDerecha = new JPanel();
		asignar1 = new BotonTexto(textos[15]);
		
		resetear1 = new BotonTexto(textos[16]);

		panelBotonesDerecha.add(asignar1);
		panelBotonesDerecha.add(resetear1);

		asignar1.addMouseListener(this);
		resetear1.addMouseListener(this);

		asignar1.addKeyListener(this);
		resetear1.addKeyListener(this);

		panelDerecha.add(jcc1, BorderLayout.NORTH);
		panelDerecha.add(panelBotonesDerecha, BorderLayout.SOUTH);

		// Panel pestana color
		JPanel panelPestana1 = new JPanel();
		panelPestana1.setLayout(new BorderLayout());
		panelPestana1.add(panelIzquierda, BorderLayout.WEST);
		panelPestana1.add(panelDerecha, BorderLayout.EAST);
		
		pestanas.add(textos[17], panelPestana1);
	}
	
	private void createTab2(String[] textos) {
		// PESTAÑA 2 . MODO DE COLORES QUE DISTINGUE ENTRE NODOS DE MÉTODOS
		
		// Panel Izquierda
		JPanel panelIzquierda = new JPanel();
		panelIzquierda.setLayout(new BorderLayout());

		// Panel checkbox modo
		JPanel pCheckModo = new JPanel();
		checkModo2 = new JCheckBox(textos[81]);
		pCheckModo.add(checkModo2);
		checkModo2.addActionListener(this);
		checkModo2.addMouseListener(this);
		checkModo2.addKeyListener(this);

		// Panel colores
		JPanel panelSeleccionColores = new JPanel();
		panelSeleccionColores.setBorder(new TitledBorder(textos[0]));
		panelSeleccionColores.setLayout(new GridLayout(NUM_SELECTORES_2, 1));
		
		ButtonGroup bg = new ButtonGroup();
		selectores2[0] = new JRadioButton(textos[83] + " 1 (" + textos[84] + ")");
		selectores2[1] = new JRadioButton(textos[83] + " 2");
		selectores2[2] = new JRadioButton(textos[83] + " 3");
		selectores2[3] = new JRadioButton(textos[83] + " 4");
		selectores2[4] = new JRadioButton(textos[83] + " 5");
		selectores2[5] = new JRadioButton(textos[83] + " 6");
		selectores2[6] = new JRadioButton(textos[83] + " 7");
		selectores2[7] = new JRadioButton(textos[83] + " 8");
		selectores2[8] = new JRadioButton(textos[83] + " 9");
		selectores2[9] = new JRadioButton(textos[83] + " 10");
		selectores2[0].setSelected(true);

		for (int i = 0; i < NUM_SELECTORES_2; i++) {
			selectores2[i].addKeyListener(this);
			selectores2[i].addActionListener(this);
			selectores2[i].addMouseListener(this);
			selectores2[i].setToolTipText(textos[12]);
			bg.add(selectores2[i]);
			panelSeleccionColores.add(selectores2[i]);
		}

		JPanel panelFila[] = new JPanel[NUM_SELECTORES_2];
		for (int i = 0; i < NUM_SELECTORES_2; i++) {
			panelFila[i] = new JPanel();
			panelFila[i].setLayout(new BorderLayout());
			panelFila[i].add(selectores2[i], BorderLayout.WEST);
			etiqColores2[i] = new JLabel("");
			etiqColores2[i].addMouseListener(this);
			panelesColores2[i] = creaPanelColor(etiqColores2[i]);
			panelesColores2[i].addMouseListener(this);
			panelFila[i].add(panelesColores2[i], BorderLayout.EAST);
			panelSeleccionColores.add(panelFila[i]);
		}
		//panelSeleccionColores2.setPreferredSize(new Dimension(270, 300));
		//panelSeleccionColores2.setSize(220,300);
		panelSeleccionColores.setPreferredSize(new Dimension(230, 300));
		// Panel degradados
		JPanel panelDegradados = new JPanel();
		panelDegradados.setBorder(new TitledBorder(textos[13]));
		panelDegradados.setLayout(new GridLayout(1, 1));
		colorDegradado[1] = new JCheckBox(textos[23]);
		panelDegradados.add(colorDegradado[1]);
		colorDegradado[1].addActionListener(this);
		colorDegradado[1].setToolTipText(textos[14]);
		colorDegradado[1].addKeyListener(this);

		panelIzquierda.add(pCheckModo, BorderLayout.NORTH);
		panelIzquierda.add(panelSeleccionColores, BorderLayout.CENTER);
		panelIzquierda.add(panelDegradados, BorderLayout.SOUTH);

		// Panel Derecha
		JPanel panelDerecha = new JPanel();
		panelDerecha.setLayout(new BorderLayout());

		// JColorChooser
		jcc2 = new JColorChooser(new Color(0, 0, 0));
		
		AbstractColorChooserPanel[] ab = jcc2.getChooserPanels();
		
		Dimension d= new Dimension();
		d.setSize(434, 300);
		Dimension d12= new Dimension();
		d12.setSize(430, 200);
		
		for(int i =1;i<ab.length;i++) {
			ab[i].setPreferredSize(d12);
		}
		//Original d2.setSize(450, 300);

		jcc2.setPreferredSize(d);
		jcc2.addMouseListener(this);
		
		// Panel Botones Asignar y Borrar
		JPanel panelBotonesDerecha = new JPanel();
		asignar2 = new BotonTexto(textos[15]);
		resetear2 = new BotonTexto(textos[16]);

		panelBotonesDerecha.add(asignar2);
		panelBotonesDerecha.add(resetear2);

		asignar2.addMouseListener(this);
		resetear2.addMouseListener(this);

		asignar2.addKeyListener(this);
		resetear2.addKeyListener(this);

		panelDerecha.add(jcc2, BorderLayout.NORTH);
		panelDerecha.add(panelBotonesDerecha, BorderLayout.SOUTH);

		// Panel pestana color
		JPanel panelPestana = new JPanel();
		panelPestana.setLayout(new BorderLayout());
		panelPestana.add(panelIzquierda, BorderLayout.WEST);
		panelPestana.add(panelDerecha, BorderLayout.EAST);

		pestanas.add(textos[33], panelPestana);
	}
	
	private void createTab3(String[] textos) {
		/* PESTAÑA 3. COLORES LÍNEAS DE VALORES	
			Permite la configuracion de: 
				"Línea de solución parcial"	
				"Línea de mejor solución" y
				"Línea de cota".
		*/
		// Panel Izquierda
		JPanel panelIzquierda = new JPanel();
		panelIzquierda.setLayout(new GridBagLayout());

		// Panel colores
		JPanel panelSeleccionColores = new JPanel();
		panelSeleccionColores.setBorder(new TitledBorder(textos[0]));
		panelSeleccionColores.setPreferredSize(new Dimension(230,150));
		
		
		ButtonGroup bg = new ButtonGroup();
		selectores3[0] = new JRadioButton(textos[85]);
		selectores3[1] = new JRadioButton(textos[86]);
		selectores3[2] = new JRadioButton(textos[87]);
		selectores3[0].setSelected(true);

		for (int i = 0; i < NUM_SELECTORES_3; i++) {
			selectores3[i].addKeyListener(this);
			selectores3[i].addActionListener(this);
			selectores3[i].addMouseListener(this);
			selectores3[i].setToolTipText(textos[12]);
			bg.add(selectores3[i]);
			panelSeleccionColores.add(selectores3[i]);			
		}
		
		panelSeleccionColores.setLayout(new GridLayout(NUM_SELECTORES_3, 1));
		
		JPanel panelFila[] = new JPanel[NUM_SELECTORES_3];
		for (int i = 0; i < NUM_SELECTORES_3; i++) {
			panelFila[i] = new JPanel();
			panelFila[i].setLayout(new BorderLayout());
			panelFila[i].add(selectores3[i], BorderLayout.WEST);
			etiqColores3[i] = new JLabel("");
			etiqColores3[i].addMouseListener(this);
			etiqColores3[i].setForeground(new Color(0, 0, 0));
			etiqColores3[i].setBackground(new Color(0, 0, 0));
			panelesColores3[i] = creaPanelColor(etiqColores3[i]);
			panelesColores3[i].addMouseListener(this);
			panelFila[i].add(panelesColores3[i], BorderLayout.EAST);
			panelSeleccionColores.add(panelFila[i]);
		}
		
		//	Panel selección de tema
		JPanel panelGrosorLineas = new JPanel();
		panelGrosorLineas.setBorder(new TitledBorder(textos[88]));
		panelGrosorLineas.setPreferredSize(new Dimension(230,100));
		panelGrosorLineas.setLayout(new FlowLayout());
		
		grosorLineas = new JComboBox<String>();
		grosorLineas.addItem("1");
		grosorLineas.addItem("2");
		grosorLineas.addItem("3");
		grosorLineas.addItem("4");
		grosorLineas.addItem("5");
		grosorLineas.addItem("6");
		grosorLineas.addItem("7");
		grosorLineas.addActionListener(this);
		grosorLineas.addKeyListener(this);
		grosorLineas.setPreferredSize(new Dimension(75, 19));
		
		panelGrosorLineas.add(grosorLineas);
		
		//	Añadimos a panel izquierdo
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1;
		constraints.gridheight = 2; 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.weightx = 1;
		
		panelIzquierda.add(panelSeleccionColores, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 
		constraints.gridy = 3; 
		constraints.gridwidth = 1;
		constraints.gridheight = 1; 
		constraints.fill = GridBagConstraints.BOTH;
		panelIzquierda.add(panelGrosorLineas, constraints);

		// Panel Derecha
		JPanel panelDerecha = new JPanel();
		panelDerecha.setLayout(new BorderLayout());

		// JColorChooser
		jcc3 = new JColorChooser(new Color(0, 0, 0));
		jcc3.addMouseListener(this);
		jcc3.setPreferredSize(new Dimension(430, 300));
		AbstractColorChooserPanel[] ab = jcc3.getChooserPanels();
		Dimension d= new Dimension();
		d.setSize(434, 300);
		Dimension d12= new Dimension();
		d12.setSize(430, 200);
		for(int i =1;i<ab.length;i++) {
			ab[i].setPreferredSize(d12);
		}
		
		// Panel Botones Asignar y Borrar
		JPanel panelBotonesDerecha = new JPanel();
		asignar3 = new BotonTexto(textos[15]);
		resetear3 = new BotonTexto(textos[16]);

		panelBotonesDerecha.add(asignar3);
		panelBotonesDerecha.add(resetear3);

		asignar3.addMouseListener(this);
		resetear3.addMouseListener(this);
		asignar3.addKeyListener(this);
		resetear3.addKeyListener(this);

		panelDerecha.add(jcc3, BorderLayout.NORTH);
		panelDerecha.add(panelBotonesDerecha, BorderLayout.SOUTH);

		// Panel pestana color
		JPanel panelPestana = new JPanel();
		panelPestana.setLayout(new BorderLayout());
		panelPestana.add(panelIzquierda, BorderLayout.WEST);
		panelPestana.add(panelDerecha, BorderLayout.EAST);

		pestanas.add(textos[79], panelPestana);
	}
	
	private void createTab4(String[] textos) {
		// PESTAÑA 4 . OTROS COLORES

		// Panel Izquierda
		JPanel panelIzquierda = new JPanel();
		panelIzquierda.setLayout(new GridBagLayout());

		// Panel colores
		JPanel panelSeleccionColores = new JPanel();
		panelSeleccionColores.setBorder(new TitledBorder(textos[0]));
		//panelSeleccionColores3.setPreferredSize(new Dimension(270, 300));
		panelSeleccionColores.setPreferredSize(new Dimension(230, 300));
		
		//ButtonGroup bg = new ButtonGroup();
		/*this.selectores3[0] = new JRadioButton(textos[1]);
		this.selectores3[1] = new JRadioButton(textos[4]);
		 */
		selectores4[0] = new JRadioButton(textos[8]);
		selectores4[1] = new JRadioButton(textos[9]);
		selectores4[2] = new JRadioButton(textos[10]);
		selectores4[3] = new JRadioButton(textos[11]);

		selectores4[11] = new JRadioButton(textos[25]);
		selectores4[12] = new JRadioButton(textos[65]);
		selectores4[13] = new JRadioButton(textos[66]);
		selectores4[14] = new JRadioButton(textos[70]);

		//TODO Esto hay que cambiarlo, eliminando los selectores, 
		//	textos y códigos estos radio buttons, no ocultándolos
		selectores4[4] = new JRadioButton();
		selectores4[5] = new JRadioButton();
		selectores4[6] = new JRadioButton(textos[18]);
		selectores4[7] = new JRadioButton(textos[19]);
		selectores4[8] = new JRadioButton(textos[20]);
		selectores4[9] = new JRadioButton(textos[21]);
		selectores4[10] = new JRadioButton(textos[22]);
		
		//	TODO Esto hay que cambiarlo, eliminando los selectores, 
		//	textos y códigos estos radio buttons, no ocultándolos
		selectores4[4].setVisible(false);
		selectores4[5].setVisible(false);
		selectores4[6].setVisible(false);
		selectores4[7].setVisible(false);
		selectores4[8].setVisible(false);
		selectores4[9].setVisible(false);
		selectores4[10].setVisible(false);

		int numeroSelectoresVisibles = 0;	
		for (int i = 0; i < NUM_SELECTORES_4; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!selectores4[i].isVisible()){
				continue;
			}
			numeroSelectoresVisibles++;
			selectores4[i].addKeyListener(this);
			selectores4[i].addActionListener(this);
			selectores4[i].addMouseListener(this);
			selectores4[i].setToolTipText(textos[12]);
			//bg.add(this.selectores[i]);
			//panelSeleccionColores.add(this.selectores[i]);			
		}
		
		panelSeleccionColores.setLayout(new GridLayout(numeroSelectoresVisibles, 1));
		
		JPanel panelFila[] = new JPanel[NUM_SELECTORES_4];
		for (int i = 0; i < NUM_SELECTORES_4; i++) {
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!selectores4[i].isVisible()){
				continue;
			}
			panelFila[i] = new JPanel();
			panelFila[i].setLayout(new BorderLayout());
			panelFila[i].add(selectores4[i], BorderLayout.WEST);
			etiqColores4[i] = new JLabel("");
			etiqColores4[i].addMouseListener(this);
			etiqColores4[i].setForeground(new Color(0, 0, 0));
			etiqColores4[i].setBackground(new Color(0, 0, 0));
			panelesColores4[i] = creaPanelColor(etiqColores4[i]);
			panelesColores4[i].addMouseListener(this);
			panelFila[i].add(panelesColores4[i], BorderLayout.EAST);
			panelSeleccionColores.add(panelFila[i]);
		}
		
		//	Panel selección de tema
		JPanel panelSeleccionTema = new JPanel();
		panelSeleccionTema.setBorder(new TitledBorder(textos[71]));
		panelSeleccionTema.setLayout(new FlowLayout());
		
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
		
		panelSeleccionTema.add(comboThemes);
		
		//	Añadimos a panel izquierdo
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1;
		constraints.gridheight = 2; 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.weightx = 1;
		
		panelIzquierda.add(panelSeleccionColores, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 
		constraints.gridy = 3; 
		constraints.gridwidth = 1;
		constraints.gridheight = 1; 
		constraints.fill = GridBagConstraints.BOTH;
		panelIzquierda.add(panelSeleccionTema, constraints);

		// Panel Derecha
		JPanel panelDerecha = new JPanel();
		panelDerecha.setLayout(new BorderLayout());

		// JColorChooser
		jcc4 = new JColorChooser(new Color(0, 0, 0));
		jcc4.addMouseListener(this);
		jcc4.setPreferredSize(new Dimension(430, 300));
		AbstractColorChooserPanel[] ab = jcc4.getChooserPanels();
		Dimension d= new Dimension();
		d.setSize(434, 300);
		Dimension d12= new Dimension();
		d12.setSize(430, 200);
		for(int i =1;i<ab.length;i++) {
			ab[i].setPreferredSize(d12);
		}
		
		// Panel Botones Asignar y Borrar
		JPanel panelBotonesDerecha = new JPanel();
		asignar4 = new BotonTexto(textos[15]);
		resetear4 = new BotonTexto(textos[16]);

		panelBotonesDerecha.add(asignar4);
		panelBotonesDerecha.add(resetear4);

		asignar4.addMouseListener(this);
		resetear4.addMouseListener(this);
		asignar4.addKeyListener(this);
		resetear4.addKeyListener(this);

		panelDerecha.add(jcc4, BorderLayout.NORTH);
		panelDerecha.add(panelBotonesDerecha, BorderLayout.SOUTH);

		// Panel pestana color
		JPanel panelPestana = new JPanel();
		panelPestana.setLayout(new BorderLayout());
		panelPestana.add(panelIzquierda, BorderLayout.WEST);
		panelPestana.add(panelDerecha, BorderLayout.EAST);

		pestanas.add(textos[24], panelPestana);
	}
	
	private void createTab5(String[] textos) {
		// PESTAÑA 5 . ÁRBOLES Y PILAS

		JPanel panelPestana = new JPanel();
		panelPestana.setLayout(new BorderLayout());

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

		grosorFlecha = new JComboBox<String>();
		for (int i = 0; i < 10; i++) {
			grosorFlecha.addItem((i + 1) + "");
		}
		grosorFlecha.addActionListener(this);
		grosorFlecha.addKeyListener(this);
		grosorFlecha.setAlignmentX(Component.RIGHT_ALIGNMENT);
		tipoFlecha = new JComboBox<String>();
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

		grosorFlecha.setPreferredSize(new Dimension(75, 19));
		tipoFlecha.setPreferredSize(new Dimension(75, 19));

		panelDesplegables.add(grosorFlecha);
		panelDesplegables.add(new JPanel());
		panelDesplegables.add(tipoFlecha);
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

		grosorMarco = new JComboBox<String>();
		for (int i = 0; i < 9; i++) {
			grosorMarco.addItem((i) + "");
		}
		grosorMarco.addActionListener(this);
		grosorMarco.addKeyListener(this);

		bordeCelda = new JComboBox<String>();
		bordeCelda.addItem(textos[48]);
		bordeCelda.addItem(textos[49]);
		bordeCelda.addItem(textos[50]);
		bordeCelda.addItem(textos[51]);
		bordeCelda.addItem(textos[52]);
		bordeCelda.addItem(textos[53]);
		bordeCelda.addActionListener(this);
		bordeCelda.addKeyListener(this);

		grosorMarco.setPreferredSize(new Dimension(75, 19));
		bordeCelda.setPreferredSize(new Dimension(75, 19));

		panelDesplegables2.add(grosorMarco);
		panelDesplegables2.add(new JPanel());
		panelDesplegables2.add(bordeCelda);
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

		sepHorizontal = new JComboBox<String>();
		for (int i = 0; i < 91; i++) {
			sepHorizontal.addItem((i + 10) + "");
		}
		sepHorizontal.addActionListener(this);
		sepHorizontal.addKeyListener(this);

		sepVertical = new JComboBox<String>();
		for (int i = 0; i < 91; i++) {
			sepVertical.addItem((i + 10) + "");
		}
		sepVertical.addActionListener(this);
		sepVertical.addKeyListener(this);

		sepHorizontal.setPreferredSize(new Dimension(75, 19));
		sepVertical.setPreferredSize(new Dimension(75, 19));

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

		panelPestana.add(panelCaracteristicas, BorderLayout.WEST);
		panelPestana.add(panelDistancias, BorderLayout.CENTER);
		panelPestana.add(panelFlecha, BorderLayout.EAST);

		//GraphicsEnvironment ge = GraphicsEnvironment
			//.getLocalGraphicsEnvironment();
		//Font fonts[] = ge.getAllFonts();

		pestanas.add(textos[61], panelPestana);
	}
	
	private void createTab6(String[] textos) {
		// PESTAÑA 6. OTROS ELEMENTOS

		// *** Panel general
		JPanel panelPestana = new JPanel();
		panelPestana.setLayout(new GridBagLayout());
		
		// *** Panel flechas
		JPanel panelFlecha = new JPanel();
		panelFlecha.setBorder(new TitledBorder(textos[69]));
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

		grosorFlecha5 = new JComboBox<String>();
		for (int i = 0; i < 10; i++) {
			grosorFlecha5.addItem((i + 1) + "");
		}
		grosorFlecha5.addActionListener(this);
		grosorFlecha5.addKeyListener(this);
		grosorFlecha5.setAlignmentX(Component.RIGHT_ALIGNMENT);
		tipoFlecha5 = new JComboBox<String>();
		tipoFlecha5.addItem(textos[37]);
		tipoFlecha5.addItem(textos[38]);
		tipoFlecha5.addItem(textos[39]);
		tipoFlecha5.addItem(textos[40]);
		tipoFlecha5.addItem(textos[41]);
		tipoFlecha5.addItem(textos[42]);
		tipoFlecha5.addItem(textos[43]);
		tipoFlecha5.addItem(textos[44]);
		tipoFlecha5.addActionListener(this);
		tipoFlecha5.addKeyListener(this);

		grosorFlecha5.setPreferredSize(new Dimension(75, 19));
		tipoFlecha5.setPreferredSize(new Dimension(75, 19));

		panelDesplegables.add(grosorFlecha5);
		panelDesplegables.add(new JPanel());
		panelDesplegables.add(tipoFlecha5);
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

		// *** Panel distancias
		JPanel panelDistancias = new JPanel();
		panelDistancias.setBorder(new TitledBorder(textos[68]));
		panelDistancias.setLayout(new BorderLayout());

		// Panel JComboBoxes
		JPanel panelJCBs2 = new JPanel();
		panelJCBs2.setLayout(new BorderLayout());

		// Panel etiquetas
		JPanel panelEtiquetas2 = new JPanel();
		panelEtiquetas2.setLayout(new GridLayout(4, 1));

		JLabel etiqsepHorizontal = new JLabel(textos[55] + "    ");
		JLabel etiqsepVertical = new JLabel(textos[56]);

		panelEtiquetas2.add(etiqsepHorizontal);
		panelEtiquetas2.add(new JPanel());
		panelEtiquetas2.add(etiqsepVertical);
		panelEtiquetas2.add(new JPanel());

		// Panel JComboBoxes
		JPanel panelDesplegables2 = new JPanel();
		panelDesplegables2.setLayout(new GridLayout(4, 1));

		sepHorizontal5 = new JComboBox<String>();
		for (int i = 0; i < 91; i++) {
			sepHorizontal5.addItem((i + 10) + "");
		}
		sepHorizontal5.addActionListener(this);
		sepHorizontal5.addKeyListener(this);

		sepVertical5 = new JComboBox<String>();
		for (int i = 0; i < 91; i++) {
			sepVertical5.addItem((i + 10) + "");
		}
		sepVertical5.addActionListener(this);
		sepVertical5.addKeyListener(this);

		sepHorizontal5.setPreferredSize(new Dimension(75, 19));
		sepVertical5.setPreferredSize(new Dimension(75, 19));

		panelDesplegables2.add(sepHorizontal5);
		panelDesplegables2.add(new JPanel());
		panelDesplegables2.add(sepVertical5);
		panelDesplegables2.add(new JPanel());

		panelJCBs2.add(panelEtiquetas2, BorderLayout.WEST);
		panelJCBs2.add(panelDesplegables2, BorderLayout.EAST);

		// Panel etiquetaImagen
		JPanel panelEtiquetaImagen2 = new JPanel();
		JLabel etiqImagen2 = new JLabel(new ImageIcon(
				getClass().getClassLoader().getResource("imagenes/cuadroconf_distancias.gif")));
		panelEtiquetaImagen2.add(etiqImagen2);

		panelDistancias.add(panelJCBs2, BorderLayout.NORTH);
		panelDistancias.add(panelEtiquetaImagen2, BorderLayout.CENTER);

		// *** Añadir paneles
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = constraints.weighty = 1.0;
		
		panelPestana.add(panelDistancias, constraints);	
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = constraints.weighty = 1.0;
		
		panelPestana.add(panelFlecha, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 2; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = constraints.weighty = 1.0;
		
		JPanel panelDerecha = new JPanel();
		panelDerecha.setLayout(new GridBagLayout());
		
		panelPestana.add(panelDerecha, constraints);

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font fonts[] = ge.getAllFonts();

		// *** Panel vista código texto
		JPanel panelFuenteCodigo = new JPanel();
		panelFuenteCodigo.setBorder(new TitledBorder(textos[57]));
		panelFuenteCodigo.setLayout(new BorderLayout());

		JLabel etiqFuenteCodigo = new JLabel(textos[58] + "   ");
		JLabel etiqTFCodigo = new JLabel(textos[59]);

		fuentesCodigo = new JComboBox<String>();
		for (int i = 0; i < fonts.length; i++) {
			fuentesCodigo.addItem(fonts[i].getFontName());
		}

		fuentesTamCodigo = new JComboBox<String>();
		for (int i = 0; i < 73; i++) {
			fuentesTamCodigo.addItem("" + (i + 8));
		}

		fuentesCodigo.addActionListener(this);
		fuentesTamCodigo.addActionListener(this);

		JPanel tamFormaCodigo = new JPanel();
		tamFormaCodigo.setLayout(new BorderLayout());
		tamFormaCodigo.add(fuentesTamCodigo, BorderLayout.EAST);

		JPanel linea1Codigo = new JPanel();
		linea1Codigo.setLayout(new BorderLayout());
		linea1Codigo.add(etiqFuenteCodigo, BorderLayout.WEST);
		linea1Codigo.add(fuentesCodigo, BorderLayout.EAST);

		JPanel linea2Codigo = new JPanel();
		linea2Codigo.setLayout(new BorderLayout());
		linea2Codigo.add(etiqTFCodigo, BorderLayout.WEST);
		linea2Codigo.add(tamFormaCodigo, BorderLayout.EAST);

		panelFuenteCodigo.add(linea1Codigo, BorderLayout.NORTH);
		panelFuenteCodigo.add(linea2Codigo, BorderLayout.CENTER);	

		fuentesTraza = new JComboBox<Object>();
		for (int i = 0; i < fonts.length; i++) {
			fuentesTraza.addItem(fonts[i].getFontName());
		}

		fuentesTamTraza = new JComboBox<String>();
		for (int i = 0; i < 73; i++) {
			fuentesTamTraza.addItem("" + (i + 8));
		}
		
		Dimension dBotonTrazas = new Dimension();
		dBotonTrazas.setSize(100
				, 20);
		fuentesTraza.setPreferredSize(dBotonTrazas);
		//Codigo para que el botón de fuentes de traza tenga un scrollbar horizontal
		fuentesTraza.setUI(new BasicComboBoxUI() {
            @Override
            protected ComboPopup createPopup() {
                return new BasicComboPopup(fuentesTraza) {
                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane scroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                       
                        return scroller;
                    }
                };
            }
        });
		fuentesTraza.addActionListener(this);
		fuentesTamTraza.addActionListener(this);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		//constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.weightx = 1;
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 1; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	;
		constraints.fill = GridBagConstraints.BOTH;
		//constraints.anchor = GridBagConstraints.SOUTH;
		constraints.weighty = 1;
		constraints.weightx = 1;
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		
		//TODO Ocultado formato ódigo porque por ahora no funciona
//		panelDerecha.add(panelFuenteCodigo, constraints);
		
		/*constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 1; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.weighty = 1;
		
		panelDerecha.add(panelFuenteTraza, constraints);
*/
		constraints = new GridBagConstraints();
		constraints.gridx = 0; 		// Empieza columna
		constraints.gridy = 0; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.weighty = 1;

		pestanas.add(textos[67], panelPestana);
	}
	
	private void createTab7(String[] textos) {
		//Pestaña 7
		// Panel Izquierda
		JPanel panelIzquierda = new JPanel();
		panelIzquierda.setLayout(new GridBagLayout());

		// Panel colores
		JPanel panelSeleccionColores = new JPanel();
		panelSeleccionColores.setBorder(new TitledBorder(textos[0]));
		//panelSeleccionColores3.setPreferredSize(new Dimension(270, 300));
		//panelSeleccionColores4.setPreferredSize(new Dimension(230, 50));

		ButtonGroup bg = new ButtonGroup();
		selectores5[0] = new JRadioButton(textos[1]);
		selectores5[1] = new JRadioButton(textos[4]);
		selectores5[0].setSelected(true);

		int numeroSelectoresVisibles = 0;

		for (int i = 0; i < NUM_SELECTORES_5; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!selectores5[i].isVisible()){
				continue;
			}
			numeroSelectoresVisibles++;
			selectores5[i].addKeyListener(this);
			selectores5[i].addActionListener(this);
			selectores5[i].addMouseListener(this);
			selectores5[i].setToolTipText(textos[12]);
			bg.add(selectores5[i]);
			panelSeleccionColores.add(selectores5[i]);			
		}

		panelSeleccionColores.setLayout(new GridLayout(numeroSelectoresVisibles, 1));

		JPanel panelFila[] = new JPanel[NUM_SELECTORES_5];
		for (int i = 0; i < NUM_SELECTORES_5; i++) {
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!selectores5[i].isVisible()){
				continue;
			}
			panelFila[i] = new JPanel();
			panelFila[i].setLayout(new BorderLayout());
			panelFila[i].add(this.selectores5[i], BorderLayout.WEST);

			etiqColores5[i] = new JLabel("");
			etiqColores5[i].addMouseListener(this);
			etiqColores5[i].setForeground(new Color(0, 0, 0));
			etiqColores5[i].setBackground(new Color(0, 0, 0));
			panelesColores5[i] = creaPanelColor(etiqColores5[i]);
			panelesColores5[i].addMouseListener(this);

			panelFila[i].add(panelesColores5[i], BorderLayout.EAST);
			panelSeleccionColores.add(panelFila[i]);

		}
		panelSeleccionColores.setPreferredSize(new Dimension(230,100));// 200));
		//	Panel selección de tema
		JPanel panelSeleccionTema = new JPanel();
		//panelSeleccionTema3.setBorder(new TitledBorder(textos[71]));
		panelSeleccionTema.setBorder(new TitledBorder(textos[71]));
		panelSeleccionTema.setPreferredSize(new Dimension(230,100));
		panelSeleccionTema.setLayout(new FlowLayout());
		JPanel panelSeleccion = new JPanel();
		//panelSeleccionTema3.setBorder(new TitledBorder(textos[71]));
		panelSeleccion.setBorder(new TitledBorder(textos[71]));

		panelSeleccion.setLayout(new FlowLayout());

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

		panelSeleccionTema.add(comboThemes);
		//	Añadimos a panel izquierdo

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		
		constraints.gridwidth = 1;
		constraints.gridheight = 1; 
		/*constraints.fill = GridBagConstraints.VERTICAL;
		
		constraints.weighty = 1;
		constraints.weightx = 1;*/
		panelIzquierda.add(panelSeleccionColores,constraints);
		
		JLabel etiqFuenteTraza = new JLabel(textos[58] + "   ");
		JLabel etiqTFTraza = new JLabel(textos[59]);
		
		JPanel linea1Traza = new JPanel();
		linea1Traza.setLayout(new BorderLayout());
		linea1Traza.add(etiqFuenteTraza, BorderLayout.WEST);
		linea1Traza.add(fuentesTraza, BorderLayout.EAST);
		
		JPanel tamFormaTraza = new JPanel();
		tamFormaTraza.setLayout(new BorderLayout());
		tamFormaTraza.add(fuentesTamTraza, BorderLayout.EAST);
		
		JPanel linea2Traza = new JPanel();
		linea2Traza.setLayout(new BorderLayout());
		linea2Traza.add(etiqTFTraza, BorderLayout.WEST);
		linea2Traza.add(tamFormaTraza, BorderLayout.EAST);
		
		// *** Panel vista código traza Esto forma parte de la nueva pestaña Texto
		JPanel panelFuenteTraza = new JPanel();
		panelFuenteTraza.setBorder(new TitledBorder(textos[60]));
		panelFuenteTraza.setLayout(new GridBagLayout());
		
		Dimension dPanelTrazas = new Dimension();
		dPanelTrazas.setSize(150, 70);//70);
		
		panelFuenteTraza.add(linea1Traza,constraints);
		panelFuenteTraza.add(linea2Traza,constraints);//SOUTH
		panelFuenteTraza.setPreferredSize(dPanelTrazas);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0; 		// Empieza columna
		/*constraints.gridy = 2; 		// Empieza fila
		constraints.gridwidth = 1; 	// Ocupa columnas
		constraints.gridheight = 1; // Ocupa filas	
		//constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.weighty = 1;
		constraints.weightx = 1;
		*///panelIzquierda4.add(panelSeleccionColores,constraints);
		
		panelIzquierda.add(panelFuenteTraza,constraints);

		constraints = new GridBagConstraints();
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1;
		constraints.gridheight = 1; 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.weightx = 1;
		panelIzquierda.add(panelSeleccionTema, constraints);

		// Panel Derecha
		JPanel panelDerecha = new JPanel();
		panelDerecha.setLayout(new BorderLayout());

		// JColorChooser
		jcc5 = new JColorChooser(new Color(0, 0, 0));
		jcc5.addMouseListener(this);
		jcc5.setPreferredSize(new Dimension(430, 300));
		AbstractColorChooserPanel[] ab = jcc5.getChooserPanels();	
		Dimension d= new Dimension();
		d.setSize(434, 300);
		Dimension d12= new Dimension();
		d12.setSize(430, 200);
		for(int i =1;i<ab.length;i++) {
			ab[i].setPreferredSize(d12);
		}
		// Panel Botones Asignar y Borrar
		JPanel panelBotonesDerecha = new JPanel();
		asignar5 = new BotonTexto(textos[15]);
		resetear5 = new BotonTexto(textos[16]);

		panelBotonesDerecha.add(asignar5);
		panelBotonesDerecha.add(resetear5);

		asignar5.addMouseListener(this);
		resetear5.addMouseListener(this);

		asignar5.addKeyListener(this);
		resetear5.addKeyListener(this);

		panelDerecha.add(jcc5, BorderLayout.NORTH);
		panelDerecha.add(panelBotonesDerecha, BorderLayout.SOUTH);

		JPanel panelPestana = new JPanel();
		panelPestana.setLayout(new BorderLayout());
		panelPestana.add(panelIzquierda, BorderLayout.WEST);
		panelPestana.add(panelDerecha, BorderLayout.EAST);
		pestanas.add(textos[78], panelPestana);
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
				color = ocv.getColorC1Entrada();
				break;
			case 1:
				color = ocv.getColorCAEntrada();
				break;
			case 2:
				color = ocv.getColorC1Salida();
				break;
			case 3:
				color = ocv.getColorCASalida();
				break;
			case 4:
				color = ocv.getColorC1NCSalida();
				break;

			default:
				color[0] = color[1] = color[2] = 0;
			}
			panelesColores1[i].setForeground(new Color(color[0], color[1],
					color[2]));
			panelesColores1[i].setBackground(new Color(color[0], color[1],
					color[2]));
			etiqColores1[i].setForeground(new Color(color[0], color[1],
					color[2]));
			etiqColores1[i].setBackground(new Color(color[0], color[1],
					color[2]));
		}
		
		for (int i = 0; i < NUM_SELECTORES_3; i++) {
			switch (i) {
				case 0:
					color = ocv.getColorSolParc();
					break;
				case 1:
					color = ocv.getColorSolMej();
					break;
				case 2:
					color = ocv.getColorCota();
					break;
				default:
					color[0] = color[1] = color[2] = 0;
			}
			panelesColores3[i].setForeground(new Color(color[0], color[1],
					color[2]));
			panelesColores3[i].setBackground(new Color(color[0], color[1],
					color[2]));
			etiqColores3[i].setForeground(new Color(color[0], color[1],
					color[2]));
			etiqColores3[i].setBackground(new Color(color[0], color[1],
					color[2]));
		}
		if((int)ocv.getGrosorSolParc() -1  >= 0) {
			grosorLineas.setSelectedIndex((int) ocv.getGrosorSolParc() - 1);
		}
		

		for (int i = 0; i < NUM_SELECTORES_4; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!this.selectores4[i].isVisible()){
				continue;
			}
			switch (i) {
			

			case 0:
				color = ocv.getColorPanel();
				break;
			case 1:
				color = ocv.getColorFlecha();
				break;
			case 2:
				color = ocv.getColorActual();
				break;
			case 3:
				color = ocv.getColorCActual();
				break;
			case 11:
				color = ocv.getColorIluminado();
				break;
			case 12:
				color = ocv.getColorResaltado();
				break;
			case 13:
				color = ocv.getColorMarcoFamilia();
				break;
			case 14:
				color = ocv.getColorErroresCodigo();
				break;
			default:
				color[0] = color[1] = color[2] = 0;
			}
			panelesColores4[i].setForeground(new Color(color[0], color[1],
					color[2]));
			panelesColores4[i].setBackground(new Color(color[0], color[1],
					color[2]));
			etiqColores4[i].setForeground(new Color(color[0], color[1],
					color[2]));
			etiqColores4[i].setBackground(new Color(color[0], color[1],
					color[2]));
		}
		for (int i = 0; i < NUM_SELECTORES_5; i++) {
			switch (i) {

			case 0:
				color = ocv.getColorFEntrada();
				break;

			case 1:
				color = ocv.getColorFSalida();
				break;	

			default:
				color[0] = color[1] = color[2] = 0;
			}
			panelesColores5[i].setForeground(new Color(color[0], color[1],
					color[2]));
			panelesColores5[i].setBackground(new Color(color[0], color[1],
					color[2]));
			etiqColores5[i].setForeground(new Color(color[0], color[1],
					color[2]));
			etiqColores5[i].setBackground(new Color(color[0], color[1],
					color[2]));
		}

		comboThemes.setSelectedIndex(ocv.getTemaColorEditor());

		for (int i = 0; i < NUM_SELECTORES_2; i++) {
			panelesColores2[i].setForeground(Conf.coloresNodo[i]);
			panelesColores2[i].setBackground(Conf.coloresNodo[i]);
			etiqColores2[i].setForeground(Conf.coloresNodo[i]);
			etiqColores2[i].setBackground(Conf.coloresNodo[i]);
		}

		if (Conf.modoColor == 1) {
			checkModo1.setSelected(true);
			habilitarElementosModo1(checkModo1.isSelected());
			habilitarElementosModo2(!checkModo1.isSelected());
		} else if (Conf.modoColor == 2) {
			checkModo2.setSelected(true);
			habilitarElementosModo2(checkModo2.isSelected());
			habilitarElementosModo1(!checkModo2.isSelected());
		}

		colorDegradado[0].setSelected(ocv.getColorDegradadoModo1());
		colorDegradado[1].setSelected(ocv.getColorDegradadoModo2());

		// con -1 convertimos de ancho real a posición de lista
		grosorFlecha.setSelectedIndex(ocv.getGrosorFlecha() - 1);

		tipoFlecha.setSelectedIndex(ocv.getFormaFlecha());
		grosorMarco.setSelectedIndex(ocv.getGrosorActual());
		bordeCelda.setSelectedIndex(ocv.getTipoBordeCelda());

		sepHorizontal.setSelectedIndex(ocv.getDistanciaH());
		sepVertical.setSelectedIndex(ocv.getDistanciaV());

		fuentesCodigo.setSelectedItem(ocv.getFuenteCodigo());
		fuentesTamCodigo.setSelectedItem(""
				+ ocv.getTamFuenteCodigo());

		fuentesTraza.setSelectedItem(ocv.getFuenteTraza());
		fuentesTamTraza.setSelectedItem("" + ocv.getTamFuenteTraza());
		
		//	Pestaña 5
		sepHorizontal5.setSelectedIndex(ocv.getDistanciaHGrafo());
		sepVertical5.setSelectedIndex(ocv.getDistanciaVGrafo());
		
		grosorFlecha5.setSelectedIndex(ocv.getGrosorFlechaGrafo() - 1);
		tipoFlecha5.setSelectedIndex(ocv.getTipoFlechaGrafo());
	}

	/**
	 * Actualiza la configuración con los valores establecidos por el usuario.
	 */
	private void getValores() {
		Color c;

		// Colores pestaña 1 (árbol y pila)
		c = etiqColores1[0].getBackground();
		ocv.setColorC1Entrada(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores1[1].getBackground();
		ocv.setColorCAEntrada(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores1[2].getBackground();
		ocv.setColorC1Salida(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores1[3].getBackground();
		ocv.setColorCASalida(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores1[4].getBackground();
		ocv.setColorC1NCSalida(c.getRed(), c.getGreen(), c.getBlue());
		
		
		c = etiqColores3[0].getBackground();
		ocv.setColorSolParc(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores3[1].getBackground();
		ocv.setColorSolMej(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores3[2].getBackground();
		ocv.setColorCota(c.getRed(), c.getGreen(), c.getBlue());
		

		c = etiqColores5[0].getBackground();
		ocv.setColorFEntrada(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores5[1].getBackground();
		ocv.setColorFSalida(c.getRed(), c.getGreen(), c.getBlue());

	
		c = etiqColores4[0].getBackground();
		ocv.setColorPanel(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores4[1].getBackground();
		ocv.setColorFlecha(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores4[2].getBackground();
		ocv.setColorActual(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores4[3].getBackground();
		ocv.setColorCActual(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores4[11].getBackground();
		ocv.setColorIluminado(c.getRed(), c.getGreen(), c.getBlue());

		c = etiqColores4[12].getBackground();
		ocv.setColorResaltado(c.getRed(), c.getGreen(), c.getBlue());
		
		c = etiqColores4[13].getBackground();
		ocv.setColorMarcoFamilia(c.getRed(), c.getGreen(), c.getBlue());
		
		c = etiqColores4[14].getBackground();
		ocv.setColorErroresCodigo(c.getRed(), c.getGreen(), c.getBlue());
		
		ocv.setTemaColorEditor(comboThemes.getSelectedIndex());

		ocv.setGrosorActual(grosorMarco.getSelectedIndex());
		ocv.setGrosorFlecha(grosorFlecha.getSelectedIndex() + 1);
		ocv.setFormaFlecha(tipoFlecha.getSelectedIndex());
		ocv.setTipoBordeCelda(bordeCelda.getSelectedIndex());
		if(grosorLineas.getSelectedIndex() != -1) {
			ocv.setGrosorSolParc(1.0f + grosorLineas.getSelectedIndex());
			ocv.setGrosorSolMej(1.0f + grosorLineas.getSelectedIndex());
			ocv.setGrosorCota(1.0f + grosorLineas.getSelectedIndex());
		}

		ocv.setModoColor(Conf.modoColor);

		for (int i = 0; i < Conf.numColoresMetodos; i++) {
			ocv.setColorModo2(etiqColores2[i].getBackground()
					.getRed(), etiqColores2[i].getBackground().getGreen(),
					etiqColores2[i].getBackground().getBlue(), i);
		}

		ocv.setColorDegradadoModo1(colorDegradado[0].isSelected());
		ocv.setColorDegradadoModo2(colorDegradado[1].isSelected());

		ocv.setDistanciaH(sepHorizontal.getSelectedIndex());
		ocv.setDistanciaV(sepVertical.getSelectedIndex());

		Integer iAux = Integer.parseInt((String) fuentesTamCodigo
				.getSelectedItem());
		ocv.setFuenteCodigo((String) fuentesCodigo.getSelectedItem(),
				iAux.intValue());
		iAux = Integer
				.parseInt((String) fuentesTamTraza.getSelectedItem());
		
		ocv.setFuenteTraza((String) fuentesTraza.getSelectedItem(),
				iAux.intValue());// ,(String)fuentesTraza.getSelectedItem() );
		
		//	Pestaña 5
		ocv.setDistanciaHGrafo(sepHorizontal5.getSelectedIndex());
		ocv.setDistanciaVGrafo(sepVertical5.getSelectedIndex());
		
		ocv.setGrosorFlechaGrafo(grosorFlecha5.getSelectedIndex() +1);
		ocv.setTipoFlechaGrafo(tipoFlecha5.getSelectedIndex());
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
		if (e.getSource() == checkModo1) {
			Conf.modoColor = (checkModo1.isSelected() ? 1 : 2);
		}
		if (e.getSource() == checkModo2) {
			Conf.modoColor = (checkModo2.isSelected() ? 2 : 1);
		}

		if (e.getSource() == aceptar) {
			guardadoRecarga();
		} else if (e.getSource().getClass().getName().contains("JRadioButton")) {
			for (int i = 0; i < NUM_SELECTORES_1; i++) {
				if (selectores1[i].isSelected()) {
					jcc1.setColor(etiqColores1[i].getBackground());
				}
			}

			for (int i = 0; i < NUM_SELECTORES_2; i++) {
				if (selectores2[i].isSelected()) {
					jcc2.setColor(etiqColores2[i].getBackground());
				}
			}
			
			for (int i = 0; i < NUM_SELECTORES_3; i++) {
				if (selectores3[i].isSelected()) {
					jcc3.setColor(etiqColores3[i].getBackground());
				}
			}

			for (int i = 0; i < NUM_SELECTORES_4; i++) {
				//	TODO Esto hay que cambiarlo, eliminando los selectores, 
				//	textos y códigos estos radio buttons, no ocultándolos
				if(!selectores4[i].isVisible()){
					continue;
				}
				if (selectores4[i].isSelected()) {
					jcc4.setColor(etiqColores4[i].getBackground());
				}
			}
			for (int i = 0; i < NUM_SELECTORES_5; i++) {
				
				if(!selectores5[i].isVisible()){
					continue;
				}
				if (selectores5[i].isSelected()) {
					jcc5.setColor(etiqColores5[i].getBackground());
				}
			}
		} else if (e.getSource().getClass().getName().contains("JComboBox")
				|| e.getSource().getClass().getName().contains("JCheckBox")) {
			if (e.getSource() == checkModo1) {
				habilitarElementosModo1(checkModo1.isSelected());
				habilitarElementosModo2(!checkModo1.isSelected());
			} else if (e.getSource() == checkModo2) {
				habilitarElementosModo2(checkModo2.isSelected());
				habilitarElementosModo1(!checkModo2.isSelected());
			} else if(e.getSource() == comboThemes){
				ventana.getPanelVentana().getPanelAlgoritmo().changeTheme(comboThemes.getSelectedIndex());
			}

			Conf.degradado1 = colorDegradado[0].isSelected();
			Conf.degradado2 = colorDegradado[1].isSelected();

			if (!creandoCuadro) {
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
		checkModo1.setSelected(valor);
		colorDegradado[0].setEnabled(valor);
		jcc1.setEnabled(valor);
		for (int i = 0; i < selectores1.length; i++) {
			selectores1[i].setEnabled(valor);
		}
		asignar1.setEnabled(valor);
		resetear1.setEnabled(valor);
		jcc1.setEnabled(valor);
	}

	/**
	 * Habilita o deshabilita los elementos del modo 2
	 * 
	 * @param valor
	 *            true para habilitar, false para deshabilitar.
	 */
	private void habilitarElementosModo2(boolean valor) {
		checkModo2.setSelected(valor);
		colorDegradado[1].setEnabled(valor);
		jcc2.setEnabled(valor);
		for (int i = 0; i < selectores2.length; i++) {
			selectores2[i].setEnabled(valor);
		}
		asignar2.setEnabled(valor);
		resetear2.setEnabled(valor);
		jcc2.setEnabled(valor);
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
			for (int i = 0; i < selectores1.length; i++) {
				if (selectores1[i].isFocusOwner()) {
					if (i != selectores1.length - 1) {
						selectores1[i].transferFocus();
					}
				}
			}
			for (int i = 0; i < selectores2.length; i++) {
				if (selectores2[i].isFocusOwner()) {
					if (i != selectores2.length - 1) {
						selectores2[i].transferFocus();
					}
				}
			}
		} else if (code == KeyEvent.VK_UP) {
			for (int i = 0; i < selectores1.length; i++) {
				if (selectores1[i].isFocusOwner()) {
					if (i != 0) {
						selectores1[i].transferFocusBackward();
					}
				}
			}
			for (int i = 0; i < selectores2.length; i++) {
				if (selectores2[i].isFocusOwner()) {
					if (i != 0) {
						selectores2[i].transferFocusBackward();
					}
				}
			}
		}

		if (e.getComponent() == aceptar) {
			dialogo.setVisible(false);
			dialogo.dispose();
		} else if (e.getComponent() == asignar1) {
			accionAsignar1();
		} else if (e.getComponent() == asignar2) {
			accionAsignar2();
		} else if (e.getComponent() == asignar3) {
			accionAsignar3();
		} else if (e.getComponent() == asignar4) {
			accionAsignar4();
		}else if (e.getComponent() == asignar5) {
			accionAsignar5();
		} else if (e.getComponent() == resetear1) {
			accionResetear1();
		} else if (e.getComponent() == resetear2) {
			accionResetear2();
		} else if (e.getComponent() == resetear3) {
			accionResetear3();
		} else if (e.getComponent() == resetear4) {
			accionResetear4();
		}else if (e.getComponent() == resetear5) {
			accionResetear5();
		}

		if (e.getComponent() == asignar1
				|| e.getComponent() == asignar2
				|| e.getComponent() == asignar3
				|| e.getComponent() == asignar4
				|| e.getComponent() == aceptar) {
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
			dialogo.setVisible(false);
			dialogo.dispose();
		} else if (code == KeyEvent.VK_ENTER) {
			guardadoRecarga();
			dialogo.setVisible(false);
			dialogo.dispose();
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
				if (panelesColores1[i] == e.getComponent()) {
					selectores1[i].setSelected(true);
					jcc1.setColor(etiqColores1[i].getBackground());
				}
			}
			for (int i = 0; i < NUM_SELECTORES_2; i++) {
				if (panelesColores2[i] == e.getComponent()
						&& selectores2[i].isEnabled()) {
					selectores2[i].setSelected(true);
					jcc2.setColor(etiqColores2[i].getBackground());
				}
			}
			for (int i = 0; i < NUM_SELECTORES_3; i++) {
				if (panelesColores3[i] == e.getComponent()
						&& selectores3[i].isEnabled()) {
					selectores3[i].setSelected(true);
					jcc3.setColor(etiqColores3[i].getBackground());
				}
			}
			for (int i = 0; i < NUM_SELECTORES_4; i++) {
				//	TODO Esto hay que cambiarlo, eliminando los selectores, 
				//	textos y códigos estos radio buttons, no ocultándolos
				if(!selectores4[i].isVisible()){
					continue;
				}
				if (panelesColores4[i] == e.getComponent()
						&& selectores4[i].isEnabled()) {
					selectores4[i].setSelected(true);
					jcc4.setColor(etiqColores4[i].getBackground());
				}
			}
			for (int i = 0; i < NUM_SELECTORES_5; i++) {
				//	TODO Esto hay que cambiarlo, eliminando los selectores, 
				//	textos y códigos estos radio buttons, no ocultándolos
				if(!selectores5[i].isVisible()){
					continue;
				}
				if (panelesColores5[i] == e.getComponent()
						&& selectores5[i].isEnabled()) {
					selectores5[i].setSelected(true);
					jcc5.setColor(etiqColores5[i].getBackground());
				}
			}
		} else if (e.getComponent().getClass().getCanonicalName()
				.contains("JRadioButton")) {
			for (int i = 0; i < NUM_SELECTORES_1; i++) {
				if (selectores1[i] == e.getComponent()) {
					jcc1.setColor(etiqColores1[i].getBackground());
				}
			}

			for (int i = 0; i < NUM_SELECTORES_2; i++) {
				if (selectores2[i] == e.getComponent()
						&& selectores2[i].isEnabled()) {
					jcc2.setColor(etiqColores2[i].getBackground());
				}
			}
			
			for (int i = 0; i < NUM_SELECTORES_3; i++) {
				if (selectores3[i] == e.getComponent()
						&& selectores3[i].isEnabled()) {
					jcc3.setColor(etiqColores3[i].getBackground());
				}
			}
			
			for (int i = 0; i < NUM_SELECTORES_4; i++) {
				//	TODO Esto hay que cambiarlo, eliminando los selectores, 
				//	textos y códigos estos radio buttons, no ocultándolos
				if(!selectores4[i].isVisible()){
					continue;
				}
				if (selectores4[i] == e.getComponent()
						&& selectores4[i].isEnabled()) {
					jcc4.setColor(etiqColores4[i].getBackground());
				}
			}
			for (int i = 0; i < NUM_SELECTORES_5; i++) {
				//	TODO Esto hay que cambiarlo, eliminando los selectores, 
				//	textos y códigos estos radio buttons, no ocultándolos
				if(!selectores5[i].isVisible()){
					continue;
				}
				if (panelesColores5[i] == e.getComponent()
						&& selectores5[i].isEnabled()) {
					selectores5[i].setSelected(true);
					jcc5.setColor(etiqColores5[i].getBackground());
				}
			}
		} else if (e.getComponent().getClass().getCanonicalName()
				.contains("BotonAceptar")) {
			dialogo.setVisible(false);
			guardadoRecarga();
			dialogo.dispose();
		} else if (e.getComponent() instanceof JButton) {
			if (e.getComponent() == aceptar) {
				dialogo.setVisible(false);
				dialogo.dispose();
			}

			if (e.getComponent() == asignar1) {
				accionAsignar1();
			} else if (e.getComponent() == asignar2) {
				accionAsignar2();
			} else if (e.getComponent() == asignar3) {
				accionAsignar3();
			} else if (e.getComponent() == asignar4) {
				accionAsignar4();
			} else if (e.getComponent() == asignar5) {
				accionAsignar5();
			} else if (e.getComponent() == resetear1) {
				accionResetear1();
			} else if (e.getComponent() == resetear2) {
				accionResetear2();
			} else if (e.getComponent() == resetear3) {
				accionResetear3();
			} else if (e.getComponent() == resetear4) {
				accionResetear4();
			}else if (e.getComponent() == resetear5) {
				accionResetear5();
			}

			if (e.getComponent() == asignar1
					|| e.getComponent() == asignar2
					|| e.getComponent() == asignar3
					|| e.getComponent() == asignar4
					|| e.getComponent() == asignar5
					|| e.getComponent() == aceptar) {
				guardadoRecarga();
			}

		} else if (e.getSource().getClass().getName().contains("JComboBox")
				|| e.getSource().getClass().getName().contains("JCheckBox")) {
			if (!creandoCuadro) {
				// formar cuadro de diálogo
				guardadoRecarga();
			}
		}

	}
	
	/**
	 * Asigna el color seleccionado al elemento seleccionado del Modo 1.
	 */
	private void accionAsignar1() {
		Color c = jcc1.getColor();

		if (asignar1.isEnabled()) {
			for (int i = 0; i < NUM_SELECTORES_1; i++) {
				if (selectores1[i].isSelected()) {
					panelesColores1[i].setForeground(c);
					panelesColores1[i].setBackground(c);
					etiqColores1[i].setForeground(c);
					etiqColores1[i].setBackground(c);
				}
			}
		}
	}
	
	/**
	 * Asigna el color seleccionado al elemento seleccionado del Modo 2.
	 */
	private void accionAsignar2() {
		Color c = jcc2.getColor();

		if (asignar2.isEnabled()) {
			for (int i = 0; i < NUM_SELECTORES_2; i++) {
				if (selectores2[i].isSelected()) {
					panelesColores2[i].setForeground(c);
					panelesColores2[i].setBackground(c);
					etiqColores2[i].setForeground(c);
					etiqColores2[i].setBackground(c);
				}
			}
		}
	}

	private void accionAsignar3() {
		Color c = jcc3.getColor();

		if (asignar3.isEnabled()) {
			for (int i = 0; i < NUM_SELECTORES_3; i++) {
				if (selectores3[i].isSelected()) {
					panelesColores3[i].setForeground(c);
					panelesColores3[i].setBackground(c);
					etiqColores3[i].setForeground(c);
					etiqColores3[i].setBackground(c);
				}
			}
		}
	}
	
	/**
	 * Asigna el color seleccionado al elemento seleccionado del Modo 3.
	 */
	private void accionAsignar4() {
		Color c = jcc4.getColor();

		for (int i = 0; i < NUM_SELECTORES_4; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!selectores4[i].isVisible()){
				continue;
			}
			if (selectores4[i].isSelected()) {
				panelesColores4[i].setForeground(c);
				panelesColores4[i].setBackground(c);
				etiqColores4[i].setForeground(c);
				etiqColores4[i].setBackground(c);
			}
		}
	}
	/**
	 * Asigna el color seleccionado al elemento seleccionado del Modo 4.
	 */
	private void accionAsignar5() {
		Color c = jcc5.getColor();

		for (int i = 0; i < NUM_SELECTORES_5; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!selectores5[i].isVisible()){
				continue;
			}
			if (selectores5[i].isSelected()) {
				panelesColores5[i].setForeground(c);
				panelesColores5[i].setBackground(c);
				etiqColores5[i].setForeground(c);
				etiqColores5[i].setBackground(c);
			}
		}
	}
	/**
	 * Restablece el color del elemento seleccionado del Modo 1.
	 */
	private void accionResetear1() {
		if (resetear1.isEnabled()) {
			for (int i = 0; i < NUM_SELECTORES_1; i++) {
				if (selectores1[i].isSelected()) {
					jcc1.setColor(etiqColores1[i].getBackground());
				}
			}
		}
	}
	
	/**
	 * Restablece el color del elemento seleccionado del Modo 2.
	 */
	private void accionResetear2() {
		if (resetear2.isEnabled()) {
			for (int i = 0; i < NUM_SELECTORES_2; i++) {
				if (selectores2[i].isSelected()) {
					jcc2.setColor(etiqColores2[i].getBackground());
				}
			}
		}
	}
	
	private void accionResetear3() {
		if (resetear3.isEnabled()) {
			for (int i = 0; i < NUM_SELECTORES_3; i++) {
				if (selectores3[i].isSelected()) {
					jcc3.setColor(etiqColores3[i].getBackground());
				}
			}
		}
	}
	
	/**
	 * Restablece el color del elemento seleccionado del Modo 3.
	 */
	private void accionResetear4() {
		for (int i = 0; i < NUM_SELECTORES_4; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!selectores4[i].isVisible()){
				continue;
			}
			if (selectores4[i].isSelected()) {
				jcc4.setColor(etiqColores4[i].getBackground());
			}
		}
	}
	private void accionResetear5() {
		for (int i = 0; i < NUM_SELECTORES_5; i++) {
			//	TODO Esto hay que cambiarlo, eliminando los selectores, 
			//	textos y códigos estos radio buttons, no ocultándolos
			if(!selectores5[i].isVisible()){
				continue;
			}
			if (selectores5[i].isSelected()) {
				jcc5.setColor(etiqColores5[i].getBackground());
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
		if (e.getSource() == cancelar) {
			dialogo.setVisible(false);
			dialogo.dispose();
		}
	}
	
	/**
	 * Guarda la configuración y refresca el formato de visualización.
	 */
	private void guardadoRecarga() {
		getValores();
		gOpciones.setOpcion(ocv, 1);
		Conf.setValoresVisualizacion();
		if (ventana.traza != null) {
			Conf.setRedibujarGrafoArbol(true);
			Conf.setRedibujarGrafoArbol(false);
		}
		// Debe ejecutarse tras la actualización de Conf
		ventana.refrescarFormato(); 
	}
}