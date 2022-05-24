package cuadros;

import java.awt.BorderLayout; 
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.border.TitledBorder;



import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utilidades.ServiciosString;
import utilidades.Texto;
import ventanas.Ventana;
import botones.BotonAceptar;
import conf.Conf;
import datos.*;

/**
 * Permite construir un cuadro que contiene información general sobre la traza
 * en ejecución.
 * 
 * @author Francisco Alcazar
 * @version 2020-2021
 * 
 */
public class CuadroInfoRedundancia extends Thread implements ActionListener,
		KeyListener {

	private static final int NUM_DECIMALES = 1;

	private static final int ANCHO_COLUMNA_ANCHA1 = 280;
	private static final int ANCHO_COLUMNA_ANCHA2 = 70;

	private static final int ALTO_FILA = 15;
	private static final int FILAS_INFO = 5;

	private static final int ANCHO_CUADRO_NO_WINDOWS = 290;
	private static final int ALTO_CUADRO_NO_WINDOWS = 404;

	private Ventana ventana;
	private JDialog dialogo;

	private Traza traza;
	private Traza trazaCompleta;

	private BotonAceptar aceptar = new BotonAceptar();

	/**
	 * Genera un nuevo cuadro de información de traza.
	 * 
	 * @param ventanaVisualizador
	 *            Ventana a la que permanecerá asociado el cuadro de diálogo
	 * @param traza
	 *            Traza actual de ejecución.
	 * @param trazaCompleta
	 *            Traza de ejecución en su estado final.
	 */
	public CuadroInfoRedundancia(Ventana ventana, Traza traza, Traza trazaCompleta) {
		this.traza = traza;
		this.trazaCompleta = trazaCompleta;
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.start();
		
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		// Tamaño del cuadro de diálogo
		int anchoCuadro = 0;
		int altoCuadro = 0;

		// Estadísticas
		int numNodos = this.traza.getNumNodos();
		int numNodosT = this.trazaCompleta.getNumNodos();
		

		int numNodosVisibles = this.traza.getNumNodosVisibles();
		int numNodosHistoricos = this.traza.getNumNodosHistoricos();
		int numNodosAunNoMostrados = this.traza.getNumNodosAunNoMostrados();
		int numNodosInhibidos = this.traza.getNumNodosInhibidos();

		int numNodosOcultos = numNodosT - numNodos;
		int numNodosResaltados = this.traza.getNumNodosIluminados();
		
		//for(int i=1;i<numNodosT;i++) {
		RegistroActivacion nodoR=this.traza.getRaiz();
		RegistroActivacion nodo=this.traza.getRaiz().getHijo(1);
		RegistroActivacion nodo2=this.traza.getRaiz().getHijo(0);
		//RegistroActivacion nodo3=nodo.getHijo(1);
		
		ArrayList<RegistroActivacion> lista=nodoR.getHijosDescend(this.traza.getRaiz());
		//IMPORTANTE boolean valor = !this.ra.estaIluminado();
		
		int y =lista.size();
		int iluminados=0;
		int iluminadosAux=0;
		int nCasosBasicos=0;
		 Hashtable<String, Integer> mapa= new Hashtable<String, Integer>();
		 Hashtable<String, String> mapa2= new Hashtable<String,String>();
		
				
		
		Set<String> ilumin = new HashSet<String>();
		
		/*Variable obsoleta */int elementos=0;
		for (RegistroActivacion nodon : lista) {
		
			if (nodon.getHijos().size() == 0) {
				
				nCasosBasicos++;
			   
			    elementos++;
			}
			

			else if (!ilumin.contains(nodon.getEntradaString() + nodon.getSalidaString())) {
				ilumin.add(nodon.getEntradaString() + nodon.getSalidaString());

				iluminadosAux = this.traza.getRedundantes(nodon.getNumMetodo(), nodon.getEntradasString(),
						nodon.getSalidasString());
				if (iluminadosAux > 1) {
					iluminados = iluminados + iluminadosAux;
				mapa.put(nodon.getEntradaString() , iluminadosAux);
				mapa2.put(nodon.getEntradaString() , nodon.getNombreMetodo());
				elementos++;
				}
			}
		}
		Ventana.thisventana.refrescarFormato();
		//int y2 = lista.size();
		
	//	}

		// Nombres de Métodos
		String[] metodos = this.trazaCompleta.getInterfacesMetodos();

		// Panel Info general
		JPanel panelInfoGral = new JPanel();
		GridLayout gl1 = new GridLayout(FILAS_INFO, 1);
		panelInfoGral.setLayout(gl1);

		// Etiquetas para columna de la izquierda (identificaciones)
		JLabel infoGral1[] = new JLabel[FILAS_INFO];

		infoGral1[0] = new JLabel("");
		infoGral1[1] = new JLabel(Texto.get("CINFONODOU_REDUNDANCIA", Conf.idioma));
		infoGral1[2] = new JLabel(Texto.get("CINFONODONU_REDUNDANCIA", Conf.idioma));
		infoGral1[3] = new JLabel(Texto.get("CINFONODOBAS_REDUNDANCIA", Conf.idioma));
		infoGral1[4] = new JLabel(Texto.get("CINFONODOTOT_REDUNDANCIA", Conf.idioma));
		

		JPanel panelContGral1[] = new JPanel[FILAS_INFO];
		for (int i = 0; i < panelContGral1.length; i++) {
			panelContGral1[i] = new JPanel();
			panelContGral1[i].setLayout(new BorderLayout());
			panelContGral1[i].add(infoGral1[i], BorderLayout.WEST);
			panelContGral1[i].setPreferredSize(new Dimension(
					ANCHO_COLUMNA_ANCHA1, ALTO_FILA));
		}

		// Etiquetas para columna del centro (núm. nodos)
		JLabel infoGral2[] = new JLabel[FILAS_INFO];

		infoGral2[0] = new JLabel(Texto.get("CINFOANIM_NOD", Conf.idioma));
		infoGral2[1] = new JLabel("" + (numNodosT-iluminados-nCasosBasicos));
		infoGral2[2] = new JLabel("" + iluminados);
		infoGral2[3] = new JLabel("" + nCasosBasicos);
		infoGral2[4] = new JLabel("" + numNodosT);
		

		JPanel panelContGral2[] = new JPanel[FILAS_INFO];
		for (int i = 0; i < panelContGral2.length; i++) {
			panelContGral2[i] = new JPanel();
			panelContGral2[i].setLayout(new BorderLayout());
			panelContGral2[i].add(infoGral2[i], BorderLayout.EAST);
			panelContGral2[i].setPreferredSize(new Dimension(
					ANCHO_COLUMNA_ANCHA2, ALTO_FILA));
		}

		// Etiquetas para columna del centro (núm. nodos)
		JLabel infoGral3[] = new JLabel[FILAS_INFO];

		infoGral3[0] = new JLabel("% "
				+ Texto.get("CINFOANIM_NOD", Conf.idioma));
		infoGral3[1] = new JLabel(""
				+ ServiciosString.truncarNumero(
						((numNodosT-iluminados-nCasosBasicos) * 100.0 / numNodosT), NUM_DECIMALES));
		infoGral3[2] = new JLabel(""
				+ ServiciosString.truncarNumero(
						(iluminados * 100.0 / numNodosT), NUM_DECIMALES));
		infoGral3[3] = new JLabel(
				""
						+ ServiciosString.truncarNumero(
								(nCasosBasicos * 100.0 / numNodosT),
								NUM_DECIMALES));
		infoGral3[4] = new JLabel(
				""
						+ ServiciosString.truncarNumero(
								(numNodosT * 100.0 / numNodosT),
								NUM_DECIMALES));
	

		JPanel panelContGral3[] = new JPanel[FILAS_INFO];
		for (int i = 0; i < panelContGral3.length; i++) {
			panelContGral3[i] = new JPanel();
			panelContGral3[i].setLayout(new BorderLayout());
			panelContGral3[i].add(infoGral3[i], BorderLayout.EAST);
			panelContGral3[i].setPreferredSize(new Dimension(
					ANCHO_COLUMNA_ANCHA2, ALTO_FILA));
		}

		// Empaquetamos paneles para poder situarlos en el cuadro de diálogo
		JPanel panelHorizGral[] = new JPanel[FILAS_INFO];

		for (int i = 0; i < panelHorizGral.length; i++) {
			panelHorizGral[i] = new JPanel();
			panelHorizGral[i].setLayout(new BorderLayout());
			panelHorizGral[i].add(panelContGral1[i], BorderLayout.WEST);
			panelHorizGral[i].add(panelContGral2[i], BorderLayout.CENTER);
			panelHorizGral[i].add(panelContGral3[i], BorderLayout.EAST);
		}

		// Añadimos los paneles paquetizados al panel que irá en el cuadro de
		// diálogo
		for (int i = 0; i < panelHorizGral.length; i++) {
			panelInfoGral.add(panelHorizGral[i]);
		}

		panelInfoGral.setBorder(new TitledBorder(Texto.get("CINFODATOSGLOBALES_REDUNDANCIA",
				Conf.idioma)));

		// Panel Metodos implicados
		JPanel panelMetodos = new JPanel();
		

		// Etiquetas para las interfaces
		
		JLabel infoMetodos[] = new JLabel[5];
		int i2=0;
		int aux=0;
	for(Entry<String,Integer> entrada : mapa.entrySet()) {
		String nombreMetodo = mapa2.get(entrada.getKey());
			infoMetodos[i2] = new JLabel(nombreMetodo+" ("+entrada.getKey()+")    "
					+ Texto.get("CINFONODO_NNODOS",
							Conf.idioma)+ "= "
					+entrada.getValue());
		i2++;
		if(i2==5) {
			aux =i2;
			break;
		}
		}

		JPanel panelMetodo[] = new JPanel[5];
		aux--;
		for (int i = 0; i <i2; i++) {
			panelMetodo[i] = new JPanel();
			panelMetodo[i].setLayout(new BorderLayout());
			panelMetodo[i].add(infoMetodos[i], BorderLayout.WEST);
			panelMetodo[i].setPreferredSize(new Dimension(ANCHO_COLUMNA_ANCHA2,
					ALTO_FILA));
			aux--;
		}

		for (int i = 0; i <i2; i++) {
			panelMetodos.add(panelMetodo[i]);
		}
		GridLayout gl2 = new GridLayout(i2, 1);
		panelMetodos.setLayout(gl2);
		panelMetodos.setBorder(new TitledBorder(Texto.get("CINFOALGUNOSCASOS_REDUNDANCIA",
				Conf.idioma)));

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(this.aceptar);

		this.aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);

		// Panel general
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelInfoGral, BorderLayout.NORTH);
		panel.add(panelMetodos, BorderLayout.CENTER);
		panel.add(panelBoton, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("CINFOTITULO_REDUNDANCIA", Conf.idioma));

		if (this.ventana.msWindows) {
			anchoCuadro = ANCHO_COLUMNA_ANCHA1 + (ANCHO_COLUMNA_ANCHA2 * 2)
					+ 40;
			altoCuadro = (ALTO_FILA * FILAS_INFO) + 130
					+ (ALTO_FILA * i2);

			this.dialogo.setSize(anchoCuadro, altoCuadro);
			int coord[] = Conf.ubicarCentro(anchoCuadro, altoCuadro);
			this.dialogo.setLocation(coord[0], coord[1]);
		} else {
			this.dialogo.setSize(ANCHO_CUADRO_NO_WINDOWS,
					ALTO_CUADRO_NO_WINDOWS);
			int coord[] = Conf.ubicarCentro(ANCHO_CUADRO_NO_WINDOWS,
					ALTO_CUADRO_NO_WINDOWS);
			this.dialogo.setLocation(coord[0], coord[1]);
		}
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.dialogo.setVisible(false);
		dialogo.dispose();
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado.
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER
				|| e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
			dialogo.dispose();
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado.
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}

}
