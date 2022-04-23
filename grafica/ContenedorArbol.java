package grafica;

import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map.Entry;

import javax.swing.BorderFactory;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphCellEditor;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

//import com.sun.javafx.geom.Line2D;

import paneles.PanelArbol;
import utilidades.NombresYPrefijos;
import utilidades.ServiciosString;
import ventanas.Ventana;
import conf.Conf;
import datos.ClaseAlgoritmo;
import datos.Estructura;
import datos.MetodoAlgoritmo;
import datos.RegistroActivacion;

/**
 * Celda contenedora (que no se visualiza) que contiene un subárbol sobre el
 * cual permite realizar acciones de manera sencilla
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class ContenedorArbol {

	private RegistroActivacion ra;

	private DefaultGraphCell entrada;
	private DefaultGraphCell salida;

	private DefaultGraphCell marcoEntrada;
	private DefaultGraphCell marcoSalida;

	public DefaultGraphCell celdasEstr[];
	private DefaultGraphCell celdasEstr2[];

	private DefaultPort portEntrada;
	private DefaultPort portSalida;
	private DefaultGraphCell celdaFantasma;

	private ContenedorArbol contenedoresHijos[];
	private DefaultEdge edges[];

	private NombresYPrefijos nyp;

	private boolean tieneHijos = false;

	private int minimoIzquierda = -1; // valor mínimo que un nodo y sus subnodos
	// pueden
	// emplear horizontalmente, sirve para que no se
	// monten nodos
	// cuando un padre es de mayor ancho que hijo(s)

	private int nivel = 0;

	private static final int alturaCelda = 26;
	private static final int alturaCeldaEstr = 18;

	private static int anchoCeldaEstr = -1; // Ancho para celdas de la
	// estructura que se
	// ve en cada nodo, depende de longitud
	// máxima de dato

	private static int altoEstructura = -1; // Si hay varias estructuras, guarda
	// la
	// altitud máxima de todas ellas (arrays =
	// 1)
	private static int anchoEstructura = -1;

	private static int altoEstructuramMin = 4; // Si hay varias estructuras,
	// guarda la
	// altitud máxima de todas ellas (arrays
	// = 1)

	private static int altoDeNivel = 0;

	protected static int espacioInicial = 20; // Entre margen superior e
	// izquierdo del
	// grafo y primer nivel, por seguridad

	private static int maximoAnchoUsado = espacioInicial;
	private static int maximoAltoUsado = espacioInicial;

	private static int[] dimEstr = null;

	private static int tamFuente = 20;
	private Font fuente = new Font("Arial", Font.BOLD, tamFuente);
	private Font fuenteEstr = new Font("Arial", Font.BOLD, 12);
	private float anchoPixelCaracter = (float) 9;
	private FontRenderContext contexto;
	private boolean esAlgoritmoPuntos=false;
	private MetodoAlgoritmo ma =null;
	// Valores para cada nivel
	private static NivelGrafo objetoNivel = null; // Determina la posición

	// horizontal (anchura)
	// donde se tiene que
	// dibujar siguiente celda

	/**
	 * Crea un nuevo contenedor
	 * 
	 * @param ra
	 *            Registro de activación del nodo.
	 * @param graph
	 *            Grafo completo del árbol.
	 * @param nyp_
	 *            Instancia de nombres y prefijos para aplicar.
	 * @param nivel_
	 *            Nivel del árbol para este nodo.
	 */
	public ContenedorArbol(RegistroActivacion ra, JGraph graph,
			NombresYPrefijos nyp_, int nivel_, FontRenderContext context) {
		contexto = context;
		this.ra = ra;
		this.nyp = nyp_;

		this.nivel = nivel_;

		if (this.nivel == 1) {
			maximoAnchoUsado = espacioInicial;
		}

		if (this.minimoIzquierda == -1) {
			this.minimoIzquierda = maximoAnchoUsado;
		}

		if (this.nivel == 1) {
			maximoAltoUsado = espacioInicial;

			ContenedorArbol.objetoNivel = null;
			altoEstructura = ra.getMaxAlturaEstructura();

			altoDeNivel = Math.max(altoEstructuramMin, altoEstructura)
					* alturaCeldaEstr;

			altoEstructura = Math.max(altoEstructuramMin, altoEstructura);

			anchoCeldaEstr = (int) ((this.maximaLongitudContenidoEstructura() + 1) * (this.anchoPixelCaracter));
		}
		if (ContenedorArbol.objetoNivel == null) {
			objetoNivel = new NivelGrafo();
		}

		// ***************************************************
		// FASE DE CREACION DE CELDAS (no ubicamos)
		// ***************************************************
	
		ArrayList<MetodoAlgoritmo> algoritmos = Ventana.thisventana.claseAlgoritmo.getMetodos();
		for(MetodoAlgoritmo m :algoritmos) {
			if(m.getTecnica()==MetodoAlgoritmo.TECNICA_DYV) {
				this.ma=m;
				
				break;
			}
		}
		if(ma!=null) { // TECNICA_DYV
			this.esAlgoritmoPuntos=	this.esAlgortimoPuntos(ma);
			
		}
		
		this.generacionCelda(); // genera entrada y salida, y sus marcos
		// (inicialmente transparentes)
		this.generacionEstr(); // genera celdas estructura, si procede

		int numHijos = ra.numHijos();
		// Recursivamente, hay que crear celdas contenedores de hijos
		this.contenedoresHijos = new ContenedorArbol[(Conf.mostrarArbolColapsado ? ra
				.getHijosVisiblesPantalla() : numHijos)];

		if (Conf.mostrarArbolColapsado) // si tenemos que mostrar los árboles
			// colapsados, sin dejar hueco para
			// nodos futuros, aún no visibles
		{
			if (Conf.historia != 2) // Si tenemos que mostrar todos los nodos
				// (con históricos)
			{
				for (int i = 0; i < this.contenedoresHijos.length; i++) {
					this.contenedoresHijos[i] = new ContenedorArbol(
							ra.getHijo(i), graph, this.nyp, this.nivel + 1, context);
					this.tieneHijos = true;

				}

			} else // Si no tenemos que mostrar todos los nodos (quitamos
				// históricos)
			{
				int x = 0;
				for (int i = 0; i < numHijos; i++) {
					if (!ra.getHijo(i).esHistorico()
							&& !ra.getHijo(i).vacioVisible()) {
						this.contenedoresHijos[x] = new ContenedorArbol(
								ra.getHijo(i), graph, this.nyp, this.nivel + 1, context);
						x++;
						this.tieneHijos = true;
					}
				}
			}
		} else {
			for (int i = 0; i < numHijos; i++) {
				this.contenedoresHijos[i] = new ContenedorArbol(ra.getHijo(i),
						graph, this.nyp, this.nivel + 1, context);
				this.tieneHijos = true;
			}
		}
		
		// ***************************************************
		// FASE DE UBICACION DE NODOS (ubicamos)
		// ***************************************************

		if (this.nivel == 1) // Hemos creado todo el árbol ya, pero no hemos
			// ubicado nada
		{
			this.ubicacionNodo();
		}

		this.generacionBordes();
		if ((Conf.mostrarArbolColapsado ? ra.getHijosVisiblesPantalla() : ra
				.numHijos()) > 0) {
			this.generacionFlechas();
		}

		// Dibujamos celda fantasma a la derecha para que el grafo no se mueva
		// cuando el marco del nodo actual esté a la derecha
		// (eso hace que el ancho del grafo aumente y se mueva todo el grafo,
		// produciendo un efecto incómodo)
		if (this.nivel == 1) {
			this.celdaFantasma = new DefaultGraphCell(new String(""));
			GraphConstants.setOpaque(this.celdaFantasma.getAttributes(), false);
			GraphConstants.setBounds(this.celdaFantasma.getAttributes(),
					new Rectangle(this.maximoAncho() - 40, 0, 80, 80));
			GraphConstants.setMoveable(this.celdaFantasma.getAttributes(),
					false);
			GraphConstants.setSelectable(this.celdaFantasma.getAttributes(),
					false);

		}
	}

	/**
	 * Se usa cuando u árbol tiene factor de ramificación=1 y celdas superiores
	 * son más anchas que celdas inferiores.
	 * 
	 * @param numPixeles
	 *            Número de pixeles a posicionar a la derecha.
	 */
	private void posicionarALaDerecha(int numPixeles) {
		this.posicionarALaDerecha(numPixeles, this.entrada);
		this.posicionarALaDerecha(numPixeles, this.salida);
		if (this.marcoEntrada != null) {
			this.posicionarALaDerecha(numPixeles, this.marcoEntrada);
		}
		if (this.marcoSalida != null) {
			this.posicionarALaDerecha(numPixeles, this.marcoSalida);
		}

		if (this.celdasEstr != null) {
			for (int i = 0; i < this.celdasEstr.length; i++) {
				this.posicionarALaDerecha(numPixeles, this.celdasEstr[i]);
				if(esAlgoritmoPuntos) {
				this.posicionarALaDerecha(numPixeles, this.celdasEstr2[i]);
				}
			}
		}

		if (this.tieneHijos) {
			for (int i = 0; i < this.contenedoresHijos.length; i++) {
				this.contenedoresHijos[i].posicionarALaDerecha(numPixeles);
			}
		}

	}

	/**
	 * Posiciona a la derecha la celda pasada por parámetro.
	 * 
	 * @param numPixeles
	 *            Número de pixeles a desplazar.
	 * @param dgc
	 *            Celda a desplazar
	 */
	private void posicionarALaDerecha(int numPixeles, DefaultGraphCell dgc) {
		Rectangle2D r = GraphConstants.getBounds(dgc.getAttributes());
		if (r != null) {
			int posX = (int) r.getX();
			int posY = (int) r.getY();
			int dimX = (int) r.getWidth();
			int dimY = (int) r.getHeight();

			GraphConstants.setBounds(dgc.getAttributes(), new Rectangle(posX
					+ numPixeles, posY, dimX, dimY));
		}
	}

	/**
	 * Genera la Estructura de DYV.
	 */
	private void generacionEstr() {
		
	
	
		if (this.ra.esDYV() && Conf.mostrarEstructuraEnArbol) {
			int indices[] = this.ra.getEntrada().getIndices();

			Estructura eAux = new Estructura(this.ra.getEntrada()
					.getEstructura());
			//Estructura eAux = new Estructura(ejex);
			if (dimEstr == null) {
				dimEstr = eAux.dimensiones();
			}

			if (indices.length == 0) {
				dimEstr = eAux.dimensiones();
				if (eAux.esMatriz()) {
					indices = new int[4];
					indices[0] = 0;
					indices[1] = dimEstr[0];
					indices[2] = 0;
					indices[3] = dimEstr[1];
				} else {
					indices = new int[2];
					indices[0] = 0;
					indices[1] = dimEstr[0];
				}
			}

			if (anchoEstructura == -1) {
				anchoEstructura = eAux.dimensiones()[0];
			}

			if (this.ra.salidaVisible()
					&& (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
				this.celdasEstr = this
						.extraerCeldasEstructura(
								new Estructura(this.ra.getSalida()
										.getEstructura()),
										indices,
										"salida",
										this.nivel,
								(Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)
										&& this.ra.salidaVisible()
										&& ((!this.ra.esHistorico() || (this.ra
												.esHistorico() && Conf.historia != 2)) && (!this.ra
														.inhibido() || (this.ra
																.inhibido() && Conf.mostrarArbolSalto))));
			
			// this.celdasEstr2 = new DefaultGraphCell[this.celdasEstr.length];
				
				
			} else {
				this.celdasEstr = this
						.extraerCeldasEstructura(
								new Estructura(this.ra.getEntrada()
										.getEstructura()),
										indices,
										"entrada",
										this.nivel,
								(Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)
										&& this.ra.entradaVisible()
										&& ((!this.ra.esHistorico() || (this.ra
												.esHistorico() && Conf.historia != 2)) && (!this.ra
														.inhibido() || (this.ra
																.inhibido() && Conf.mostrarArbolSalto))));
				//this.celdasEstr2 = new DefaultGraphCell[this.celdasEstr.length];
			
			
			
			}
		}
	}

	/**
	 * Genera la celda que contiene la información del nodo.
	 */
	private void generacionCelda() {

		String repEntrada = this.ra.getEntrada().getRepresentacion();

		// Entrada, configuración de la misma y asignación de puerto
		String cadenaEntrada = "";
		if (Conf.idMetodoTraza) {
			cadenaEntrada =this.ra.getNombreMetodo() + ": ";
			if (this.nyp != null) {
				cadenaEntrada = this.nyp.getPrefijo(this.ra.getNombreMetodo())
						+ ": ";
			}
		}
		cadenaEntrada = cadenaEntrada + repEntrada;

		if (repEntrada.length() < 3) {
			cadenaEntrada = "  " + cadenaEntrada + "  ";
		}

		this.entrada = new DefaultGraphCell(new String(cadenaEntrada));

		this.asignarColoresEntrada(cadenaEntrada);

		GraphConstants.setFont(this.entrada.getAttributes(), fuente);
		GraphConstants.setDisconnectable(this.entrada.getAttributes(), false);
		GraphConstants.setMoveable(this.entrada.getAttributes(), false);
		GraphConstants.setSelectable(this.entrada.getAttributes(), false);
		this.portEntrada = new DefaultPort();
		this.entrada.add(this.portEntrada);

		// Salida, configuración de la misma y asignación de puerto
		String cadenaSalida = this.generaCadenaSalida();

		this.salida = new DefaultGraphCell(new String(cadenaSalida));

		this.asignarColoresSalida(cadenaSalida);

		GraphConstants.setFont(this.salida.getAttributes(), fuente);
		GraphConstants.setDisconnectable(this.salida.getAttributes(), false);
		GraphConstants.setMoveable(this.salida.getAttributes(), false);
		GraphConstants.setSelectable(this.salida.getAttributes(), false);

		this.portSalida = new DefaultPort();
		this.salida.add(this.portSalida);

		// Tamaños
		int maximaCadena = Math.max(cadenaEntrada.length(),
				cadenaSalida.length());
		int anchoPixelCaracteres = (int) (anchoPixelCaracter * (maximaCadena));
		if(contexto != null) {
			if(cadenaEntrada.length() > cadenaSalida.length()) {
				anchoPixelCaracteres = (int) fuente.getStringBounds(cadenaEntrada, contexto).getWidth() + 10;
			}else {
				anchoPixelCaracteres = (int) fuente.getStringBounds(cadenaSalida, contexto).getWidth() + 10;
			}
		}
	
		GraphConstants.setSize(this.entrada.getAttributes(), new Dimension(
				anchoPixelCaracteres, alturaCelda));
		GraphConstants.setSize(this.salida.getAttributes(), new Dimension(
				anchoPixelCaracteres, alturaCelda));


		// Marco Entrada
		this.marcoEntrada = new DefaultGraphCell(" ");
		GraphConstants.setFont(this.marcoEntrada.getAttributes(), fuente);
		GraphConstants.setDisconnectable(this.marcoEntrada.getAttributes(),
				false);
		GraphConstants.setMoveable(this.marcoEntrada.getAttributes(), false);
		GraphConstants.setSelectable(this.marcoEntrada.getAttributes(), false);
		GraphConstants.setOpaque(this.marcoEntrada.getAttributes(), false);
		GraphConstants.setSize(this.marcoEntrada.getAttributes(),
				new Dimension(((int) anchoPixelCaracteres)
						+ Conf.grosorMarco, alturaCelda + Conf.grosorMarco));


		// Marco Salida
		this.marcoSalida = new DefaultGraphCell(" ");
		GraphConstants.setFont(this.marcoSalida.getAttributes(), fuente);
		GraphConstants.setDisconnectable(this.marcoSalida.getAttributes(),
				false);
		GraphConstants.setMoveable(this.marcoSalida.getAttributes(), false);
		GraphConstants.setSelectable(this.marcoSalida.getAttributes(), false);
		GraphConstants.setOpaque(this.marcoSalida.getAttributes(), false);
		GraphConstants.setSize(this.marcoSalida.getAttributes(), new Dimension(
				anchoPixelCaracteres + Conf.grosorMarco, alturaCelda + Conf.grosorMarco));

		GraphConstants.setSize(this.marcoEntrada.getAttributes(),
				new Dimension(anchoPixelCaracteres, alturaCelda));
		GraphConstants.setSize(this.marcoSalida.getAttributes(), 
				new Dimension(anchoPixelCaracteres, alturaCelda));
	}

	/**
	 * Posiciona celdas de E y S y de estructura
	 */
	private void ubicacionNodo() {

		Estructura e;
		int dimensiones[] = null;
		if (this.ra.esDYV() && Conf.mostrarEstructuraEnArbol) {
			if (this.ra.salidaVisible()
					&& (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
				e = new Estructura(this.ra.getSalida().getEstructura());
			} else {
				e = new Estructura(this.ra.getEntrada().getEstructura());
			}
			dimensiones = e.dimensiones();
		}
		int ubicacion[] = new int[4];

		if ((Conf.mostrarArbolColapsado && this.ra.getHijosVisiblesPantalla() > 0)
				|| (!Conf.mostrarArbolColapsado && this.ra.numHijos() > 0)) {
			for (int i = 0; i < this.contenedoresHijos.length; i++) {
				this.contenedoresHijos[i].ubicacionNodo();
			}

			// ubicamos en consecuencia de las posiciones de los hijos ...
			int ladoIzquierdo = this.contenedoresHijos[0].posicLadoIzquierdo();
			int ladoDerecho = this.contenedoresHijos[this.contenedoresHijos.length - 1]
					.posicLadoDerecho();
			int puntoMedio = (ladoDerecho + ladoIzquierdo) / 2;

			if (this.ra.esDYV() && Conf.mostrarEstructuraEnArbol) {
				
				int anchoNodo = (int) (GraphConstants.getSize(this.entrada
						.getAttributes()).getWidth());
				anchoNodo = anchoNodo + (anchoCeldaEstr * dimensiones[0]);

				int posicAnchoInicial = puntoMedio - (anchoNodo / 2);
			
				posicAnchoInicial=  posicAnchoInicial
						+ (dimensiones[0] * anchoCeldaEstr)+(int) (GraphConstants
							.getSize(this.entrada.getAttributes()).getWidth())+4;
				 
				this.ubicacionNodo_EstrSiHijos(dimensiones, posicAnchoInicial);

				maximoAltoUsado = Math.max(
						maximoAltoUsado,
						(int) GraphConstants.getBounds(
								this.celdasEstr[this.celdasEstr.length - 1]
										.getAttributes()).getMaxY());
				posicAnchoInicial = puntoMedio - (anchoNodo / 2);
				
				if (objetoNivel.getNivelExacto(this.nivel) > posicAnchoInicial) {
					posicAnchoInicial = objetoNivel.getNivelExacto(this.nivel)
							+ Conf.sepH + anchoCeldaEstr * dimensiones[0]
									+ espacioInicial;
				}
				if (altoEstructura > 2)// Si estructura es más alta que celdas E
					// y S
				{
					ubicacion[3] = this.posic0Nivel()
							+ ((altoEstructura / 2) * alturaCeldaEstr);
					ubicacion[1] = ubicacion[3] - alturaCelda;
				} else {
					ubicacion[1] = this.posic0Nivel();
					ubicacion[3] = ubicacion[1] + alturaCelda;
				}
				ubicacion[0] = posicAnchoInicial
						+ (dimensiones[0] * anchoCeldaEstr);
				ubicacion[2] = ubicacion[0];

				this.ubicacionNodo_posicionarCeldasx4(ubicacion);

				if (this.minimoIzquierda > posicAnchoInicial) // Si hemos
					// ubicado nodos
					// demasiado a
					// la izquierda
					// (padres mas
					// anchos que
					// hijos)
				{
					this.posicionarALaDerecha(this.minimoIzquierda
							- posicAnchoInicial);
				}
				maximoAltoUsado = Math.max(
						maximoAltoUsado,
						(int) GraphConstants.getBounds(
								this.salida.getAttributes()).getMaxY());
				maximoAnchoUsado = Math.max(
						maximoAnchoUsado,
						ubicacion[2]
								+ (int) (GraphConstants.getSize(this.salida
										.getAttributes()).getWidth()));
			} else {
				if (Conf.mostrarEstructuraEnArbol && altoEstructura > 2) // Si
					// estructura
					// es
					// más
					// alta
					// que
					// celdas
					// E
					// y
					// S
				{
					ubicacion[3] = this.posic0Nivel()
							+ ((altoEstructura / 2) * alturaCeldaEstr);
					ubicacion[1] = ubicacion[3] - alturaCelda;
				} else {
					ubicacion[1] = this.posic0Nivel() + (alturaCeldaEstr * 2)
							- alturaCelda;
					ubicacion[3] = this.posic0Nivel() + (alturaCeldaEstr * 2);
				}

				int posicAnchoInicial = puntoMedio
						- ((int) (GraphConstants.getSize(this.entrada
								.getAttributes()).getWidth()) / 2);

				if (objetoNivel.getNivelExacto(this.nivel) > posicAnchoInicial) {
					posicAnchoInicial = objetoNivel.getNivelExacto(this.nivel)
							+ Conf.sepH + espacioInicial;
				}

				ubicacion[0] = posicAnchoInicial;
				ubicacion[2] = posicAnchoInicial;

				this.ubicacionNodo_posicionarCeldasx4(ubicacion);

				if (this.minimoIzquierda > posicAnchoInicial) // Si hemos
					// ubicado nodos
					// demasiado a
					// la izquierda
					// (padres mas
					// anchos que
					// hijos)
				{
					this.posicionarALaDerecha(this.minimoIzquierda
							- posicAnchoInicial);
				}

				maximoAltoUsado = Math.max(
						maximoAltoUsado,
						(int) GraphConstants.getBounds(
								this.salida.getAttributes()).getMaxY());
				maximoAnchoUsado = Math.max(
						maximoAnchoUsado,
						ubicacion[2]
								+ (int) (GraphConstants.getSize(this.salida
										.getAttributes()).getWidth()));

			}

		} else // Nodo sin hijos....
		{
			// Si las hay, situamos en el grafo las celdas de la estructura
			if (this.ra.esDYV() && Conf.mostrarEstructuraEnArbol) {
				
				/*	int ladoIzquierdo = this.contenedoresHijos[0].posicLadoIzquierdo();
				int ladoDerecho = this.contenedoresHijos[this.contenedoresHijos.length - 1]
						.posicLadoDerecho();
				int puntoMedio = (ladoDerecho + ladoIzquierdo) / 2;*/
				int anchoNodo = (int) (GraphConstants.getSize(this.entrada
						.getAttributes()).getWidth());
				anchoNodo = anchoNodo + (anchoCeldaEstr * dimensiones[0]);
				int posicAnchoInicial = maximoAnchoUsado 
						 - (anchoNodo / 2);
				 posicAnchoInicial=  posicAnchoInicial
						+ (dimensiones[0] * anchoCeldaEstr)+(int) (GraphConstants
							.getSize(this.entrada.getAttributes()).getWidth());
				 
				
				this.ubicacionNodo_EstrNoHijos(dimensiones,anchoNodo);

				maximoAltoUsado = Math.max(
						maximoAltoUsado,
						(int) GraphConstants.getBounds(
								this.celdasEstr[this.celdasEstr.length - 1]
										.getAttributes())
											.getMaxY());

				// Situamos las celdas E y S en función de las celdas de
				// estructura

				if (altoEstructura > 2)// Si estructura es más alta que celdas E
					// y S
				{
					ubicacion[3] = this.posic0Nivel()
							+ ((altoEstructura / 2) * alturaCeldaEstr);
					ubicacion[1] = ubicacion[3] - alturaCelda;
				} else {
					ubicacion[1] = this.posic0Nivel();
					ubicacion[3] = ubicacion[1] + alturaCelda;
				}

				ubicacion[0] = maximoAnchoUsado + Conf.sepH
						+ (dimensiones[0] * anchoCeldaEstr);
				ubicacion[2] = ubicacion[0];

				this.ubicacionNodo_posicionarCeldasx4(ubicacion);

				maximoAltoUsado = Math.max(
						maximoAltoUsado,
						(int) GraphConstants.getBounds(
								this.salida.getAttributes()).getMaxY());
				maximoAnchoUsado = Math.max(
						maximoAnchoUsado,
						ubicacion[2]
								+ (int) (GraphConstants.getSize(this.salida
										.getAttributes()).getWidth()));

			} else {
				if (altoEstructura > 2) // Si estructura es más alta que celdas
					// E y S
				{
					ubicacion[3] = this.posic0Nivel()
							+ ((altoEstructura / 2) * alturaCeldaEstr);
					ubicacion[1] = ubicacion[3] - alturaCelda;
				} else {
					ubicacion[1] = this.posic0Nivel() + (alturaCeldaEstr * 2)
							- alturaCelda;
					ubicacion[3] = this.posic0Nivel() + (alturaCeldaEstr * 2);
				}
				ubicacion[0] = maximoAnchoUsado + Conf.sepH;
				ubicacion[2] = ubicacion[0];

				this.ubicacionNodo_posicionarCeldasx4(ubicacion);

				maximoAltoUsado = Math.max(
						maximoAltoUsado,
						(int) GraphConstants.getBounds(
								this.salida.getAttributes()).getMaxY());
				maximoAnchoUsado = Math.max(
						maximoAnchoUsado,
						ubicacion[2]
								+ (int) (GraphConstants.getSize(this.salida
										.getAttributes()).getWidth()));
			}

		}
		// Después de ubicar la celda, guardamos el ancho máximo empleado en el
		// nivel correspondiente.
		// La información de objetoNivel sirve, por ejemplo, para evitar que los
		// nodos se superpongan cuando tienen estrcutura e hijos estrechos.
		objetoNivel.setNivel(
				this.nivel,
				ubicacion[0]
						+ (int) (GraphConstants.getSize(this.entrada
								.getAttributes()).getWidth()));
	}

	/**
	 * Posiciona las celdas de entrada, salida, marco de entrada y marco de
	 * salida.
	 * 
	 * @param ubicacion
	 *            coordenadas de las celdas (xEntrada, yEntrada, xSalida,
	 *            ySalida)
	 */
	private void ubicacionNodo_posicionarCeldasx4(int[] ubicacion) {
		if (this.ra.getEsNodoActual()) {
			PanelArbol.setMinX(ubicacion[0]);
			PanelArbol.setMinY(ubicacion[1]);
		}

		GraphConstants.setBounds(
				this.entrada.getAttributes(),
				new Rectangle(ubicacion[0], ubicacion[1], (int) (GraphConstants
						.getSize(this.entrada.getAttributes()).getWidth()),
						(int) (GraphConstants.getSize(this.entrada
								.getAttributes()).getHeight())));
		GraphConstants.setBounds(
				this.salida.getAttributes(),
				new Rectangle(ubicacion[2], ubicacion[3], (int) (GraphConstants
						.getSize(this.salida.getAttributes()).getWidth()),
						(int) (GraphConstants.getSize(this.salida
								.getAttributes()).getHeight())));

		GraphConstants.setBounds(
				this.marcoEntrada.getAttributes(),
				new Rectangle(ubicacion[0] - Conf.grosorMarco, ubicacion[1]
						- Conf.grosorMarco, (int) (GraphConstants
								.getSize(this.entrada.getAttributes()).getWidth())
								+ Conf.grosorMarco * 2, (int) (GraphConstants
										.getSize(this.entrada.getAttributes()).getHeight())
										+ Conf.grosorMarco * 2));
		GraphConstants.setBounds(
				this.marcoSalida.getAttributes(),
				new Rectangle(ubicacion[2] - Conf.grosorMarco, ubicacion[3]
						- Conf.grosorMarco, (int) (GraphConstants
								.getSize(this.salida.getAttributes()).getWidth())
								+ Conf.grosorMarco * 2, (int) (GraphConstants
										.getSize(this.salida.getAttributes()).getHeight())
										+ Conf.grosorMarco * 2));
	}

	/**
	 * Posiciona las celdas de estructura para cuando el nodo tiene hijos.
	 * 
	 * @param dimensiones
	 *            Dimensiones de la estructura (filas, columnas)
	 * @param posicAnchoInicial
	 *            Posición inicial sobre el eje x.
	 */
	private void ubicacionNodo_EstrSiHijos(int[] dimensiones,
			int posicAnchoInicial) {
		
		/*for(int i =0;i<celdasEstr2.length;i++) {
			AttributeMap y=celdasEstr2[i].getAttributes();
			celdasEstr2[i] = new DefaultGraphCell
					(""+ejey2[i]);
			Exception th = new Exception("1659 -> longitud= "+celdasEstr2.length +"valor= "+ejey2[i]);
			try {
				throw (th);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			celdasEstr2[i].setAttributes(y);
			
		}
*/
		if (dimensiones.length == 2) {
			for (int i = 0; i < dimensiones[0]; i++) {
				for (int j = 0; j < dimensiones[1]; j++) {
					 
					GraphConstants
					.setBounds(
							this.celdasEstr[(dimensiones[0] * i) + j]
									.getAttributes(),
									new Rectangle(
											
											posicAnchoInicial 
											+ (j * anchoCeldaEstr),
											this.posic0Nivel()
											+ (alturaCeldaEstr * (i + (altoEstructura - dimensiones[1]) / 2)),
											anchoCeldaEstr, alturaCeldaEstr));
					if(esAlgoritmoPuntos) {
					GraphConstants
					.setBounds(
							this.celdasEstr2[(dimensiones[0] * i) + j]
									.getAttributes(),
									new Rectangle(
											
											posicAnchoInicial 
											+ (j * anchoCeldaEstr),
											this.posic0Nivel()+17
											+ (alturaCeldaEstr * (i + (altoEstructura - dimensiones[1]) / 2)),
											anchoCeldaEstr, alturaCeldaEstr));
					
				}
					}
			}
		} else {
			
			for (int i = 0; i < dimensiones[0]; i++) {
				GraphConstants.setBounds(this.celdasEstr[i].getAttributes(),
					//	new Rectangle(posicAnchoInicial + (i * anchoCeldaEstr),
								new Rectangle(posicAnchoInicial +   (i * anchoCeldaEstr),
								this.posic0Nivel()
								+ ((int) (alturaCeldaEstr * 1.5)),
								anchoCeldaEstr, alturaCeldaEstr));
			
				//AttributeMap y=celdasEstr[i].getAttributes();
			//	celdasEstr2[i] = new DefaultGraphCell
				//		(""+ejey2[i]);
				//celdasEstr2[i].setAttributes(y);
				if(esAlgoritmoPuntos) {
				GraphConstants.setBounds(this.celdasEstr2[i].getAttributes(),
						//	new Rectangle(posicAnchoInicial + (i * anchoCeldaEstr),
						new Rectangle(posicAnchoInicial +   (i * anchoCeldaEstr),
								this.posic0Nivel()+17
								+ ((int) (alturaCeldaEstr * 1.5)),
								anchoCeldaEstr, alturaCeldaEstr));
				}
			}
		}
	}

	/**
	 * Posiciona las celdas de estructura para cuando el nodo no tiene hijos.
	 * 
	 * @param dimensiones
	 *            Dimensiones de la estructura (filas, columnas)
	 */
	private void ubicacionNodo_EstrNoHijos(int[] dimensiones,int posAncho) {
	
		int posicAnchoInicial =posAncho;
		if (dimensiones.length == 2) {
			for (int i = 0; i < dimensiones[0]; i++) {
				for (int j = 0; j < dimensiones[1]; j++) {
					GraphConstants
					.setBounds(
							this.celdasEstr[(dimensiones[0] * i) + j]
									.getAttributes(),
									new Rectangle(
											posicAnchoInicial +
											maximoAnchoUsado + Conf.sepH
											+ (j * anchoCeldaEstr),
											this.posic0Nivel()
											+ (alturaCeldaEstr * (i + (altoEstructura - dimensiones[1]) / 2)),
						
											anchoCeldaEstr, alturaCeldaEstr));
					
				}
			}
		} else {
			for (int i = 0; i < dimensiones[0]; i++) {
				GraphConstants.setBounds(this.celdasEstr[i].getAttributes(),
						new Rectangle(posicAnchoInicial +maximoAnchoUsado + Conf.sepH
								+ (i * anchoCeldaEstr), this.posic0Nivel()
								+ ((int) (alturaCeldaEstr * 1.5)),
								anchoCeldaEstr, alturaCeldaEstr));
				
				//AttributeMap y=celdasEstr[i].getAttributes();
				//celdasEstr2[i] = new DefaultGraphCell
				//		(""+ejey2[i]);
				//celdasEstr2[i].setAttributes(y);
				if(esAlgoritmoPuntos) {
				GraphConstants.setBounds(this.celdasEstr2[i].getAttributes(),
						//	new Rectangle(posicAnchoInicial + (i * anchoCeldaEstr),
						new Rectangle(posicAnchoInicial +maximoAnchoUsado + Conf.sepH
								+ (i * anchoCeldaEstr), this.posic0Nivel()+17
								+ ((int) (alturaCeldaEstr * 1.5)),
								anchoCeldaEstr, alturaCeldaEstr));
				}
			}
		}
	}

	/**
	 * Devuelve el valor de visibilidad para la entrada.
	 * 
	 * @return true si la entrada es visible, false en caso contrario.
	 */
	private boolean entradaVisible() {
		return this.ra.entradaVisible();
	}

	/**
	 * Devuelve el valor de visibilidad para la salida.
	 * 
	 * @return true si la entrada es salida, false en caso contrario.
	 */
	private boolean salidaVisible() {
		return this.ra.salidaVisible();
	}

	/**
	 * Devuelve si el nodo es histórico.
	 * 
	 * @return true si el nodo es histórico, false en caso contrario.
	 */
	private boolean esHistorico() {
		return this.ra.esHistorico();
	}

	/**
	 * Devuelve si el nodo está inhibido.
	 * 
	 * @return true si el nodo está inhibido, false en caso contrario.
	 */
	private boolean inhibido() {
		return this.ra.inhibido();
	}

	/**
	 * Devuelve la cabeza visible para el ContenedorArbol Padre: bien la celda
	 * de entrada, bien la de salida.
	 * 
	 * @return Cabeza visible del contenedor.
	 */
	public Object getCabezaVisibleParaPadre() {
		if (Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar
				|| Conf.VISUALIZAR_TODO == Conf.elementosVisualizar) {
			return this.entrada;
		} else {
			return this.salida;
		}
	}

	/**
	 * Devuelve la cabeza visible para el ContenedorArbol Hijo: bien la celda de
	 * entrada, bien la de salida.
	 * 
	 * @return Cabeza visible del contenedor.
	 */
	public Object getCabezaVisibleParaHijo() {
		if (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar
				|| Conf.VISUALIZAR_TODO == Conf.elementosVisualizar) {
			return this.salida;
		} else {
			return this.entrada;
		}
	}

	/**
	 * Devuelve el puerto visible visible para el ContenedorArbol Padre.
	 * 
	 * @return Puerto visible.
	 */
	public Object getPuertoVisibleParaPadre() {
		try {
			return ((DefaultGraphCell) this.getCabezaVisibleParaPadre())
					.getChildAt(0);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Devuelve el puerto visible visible para el ContenedorArbol Hijo.
	 * 
	 * @return Puerto visible.
	 */
	public Object getPuertoVisibleParaHijo() {
		return ((DefaultGraphCell) this.getCabezaVisibleParaHijo())
				.getChildAt(0);
	}

	/**
	 * Permite consultar si el contenedor tiene hijos.
	 * 
	 * @return true si tiene hijos, false en caso contrario.
	 */
	private boolean tieneHijos() {
		return this.tieneHijos;
	}

	/**
	 * Devuelve la posición del lado izquierdo del eje x.
	 * 
	 * @return posición del lado izquierdo del eje x.
	 */
	private int posicLadoIzquierdo() {
		if (this.ra.esDYV() && Conf.mostrarEstructuraEnArbol) {
			return (int) GraphConstants.getBounds(
					this.celdasEstr[0].getAttributes()).getMinX();
		} else {
			return (int) GraphConstants.getBounds(this.entrada.getAttributes())
					.getMinX();
		}
	}

	/**
	 * Devuelve la posición del lado derecho del eje x.
	 * 
	 * @return posición del lado derecho del eje x.
	 */
	private int posicLadoDerecho() {
		return (int) GraphConstants.getBounds(this.entrada.getAttributes())
				.getMaxX();
	}

	/**
	 * Máximo ancho de todas las celdas que componen el contenedor.
	 * 
	 * @return Máximo ancho de las celdas que componen el contenedor.
	 */
	public int maximoAncho() {
		return maximoAnchoUsado + 25;
	}

	/**
	 * Devuelve el máximo ancho visible de las celdas que componen el
	 * contenedor.
	 * 
	 * @return Máximo ancho visible de las celdas que componen el contenedor.
	 */
	public int maximoAnchoVisible() {
		int ancho = 0;
		int anchoHijos = 0;

		if (this.entradaVisible() || this.salidaVisible()) {
			ancho = (int) (GraphConstants.getBounds(this.entrada
					.getAttributes())).getMaxX();
			for (int i = 0; i < this.contenedoresHijos.length; i++) {
				anchoHijos = Math.max(anchoHijos,
						this.contenedoresHijos[i].maximoAnchoVisible());
			}
		}

		return Math.max(ancho, anchoHijos);
	}

	/**
	 * Máximo alto de todas las celdas que componen el contenedor.
	 * 
	 * @return Máximo alto de las celdas que componen el contenedor.
	 */
	public int maximoAlto() {
		return maximoAltoUsado;
	}

	/**
	 * Devuelve el máximo alto visible de las celdas que componen el contenedor.
	 * 
	 * @return Máximo alto visible de las celdas que componen el contenedor.
	 */
	public int maximoAltoVisible() {
		if ((!this.entradaVisible() && !this.salidaVisible())
				|| (this.ra.inhibido() && !Conf.mostrarArbolSalto)) {
			return 0;
		}

		if (this.contenedoresHijos.length > 0) {
			int valorAltura = (int) (GraphConstants.getBounds(this.entrada
					.getAttributes()).getMinY())
					+ (int) (GraphConstants.getBounds(
							this.entrada.getAttributes()).getHeight() * 3);
			for (int i = 0; i < this.contenedoresHijos.length; i++) {
				int valoraux = this.contenedoresHijos[i].maximoAltoVisible();
				if (valoraux > valorAltura) {
					valorAltura = valoraux;
				}
			}
			return valorAltura;
		} else {
			return (int) (GraphConstants
					.getBounds(this.entrada.getAttributes()).getMinY())
					+ (int) (GraphConstants.getBounds(
							this.entrada.getAttributes()).getHeight() * 3);
		}
	}

	/**
	 * Devuelve las celdas de entrada y de salida.
	 * 
	 * @return Array con las celdas de: (entrada, salida)
	 */
	public Object[] getCeldasEstado() {
		// Array para todos los hijos
		Object[] estados = new Object[2];
		estados[0] = this.entrada;
		estados[1] = this.salida;
		return estados;
	}

	/**
	 * Método que realiza la actualización del grafo en cada paso de la
	 * visualización que da el usuario, evita redibujar todo el árbol
	 */
	public void actualizar() {

		String cadenaEntrada = "  ";
		if (this.nyp != null) {
			cadenaEntrada = cadenaEntrada
					+ this.nyp.getPrefijo(this.ra.getNombreMetodo()) + ": ";
		}
		cadenaEntrada = cadenaEntrada
				+ this.ra.getEntrada().getRepresentacion() + "  ";

		String cadenaSalida = this.generaCadenaSalida();

		this.asignarColoresEntrada(cadenaEntrada);
		this.asignarColoresSalida(cadenaSalida);

		if (this.celdaFantasma != null) {
			GraphConstants.setForeground(this.celdaFantasma.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setBackground(this.celdaFantasma.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setGradientColor(this.celdaFantasma.getAttributes(),
					Conf.colorPanel);
		}

		this.generacionBordes();
		if ((Conf.mostrarArbolColapsado ? this.ra.getHijosVisiblesPantalla()
				: this.ra.numHijos()) > 0) {

			this.generacionFlechas();
		}

		for (int i = 0; i < this.contenedoresHijos.length; i++) {
			this.contenedoresHijos[i].actualizar();
		}
	}

	/**
	 * Crea una nueva celda de entrada y asigna los colores configurados.
	 * 
	 * @param cadenaEntrada
	 *            Cadena de entrada que mostrará la celda.
	 */
	private void asignarColoresEntrada(String cadenaEntrada) {

		AttributeMap amap = this.entrada.getAttributes();

		if ((Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)
				&& this.ra.entradaVisible()
				&& ((!this.ra.esHistorico() || (this.ra.esHistorico() && Conf.historia == 0)) && (!this.ra
						.inhibido() || (this.ra.inhibido() && Conf.mostrarArbolSalto)))) {
			this.entrada = new DefaultGraphCell(new String(cadenaEntrada));
			this.entrada.setAttributes(amap);
			if (this.portEntrada != null) {
				this.entrada.add(this.portEntrada);
			}
			GraphConstants.setOpaque(this.entrada.getAttributes(), true); // Normales
			GraphConstants.setForeground(this.entrada.getAttributes(),
					Conf.colorFEntrada);
			if (this.ra.estaIluminado()) {
				GraphConstants.setBackground(this.entrada.getAttributes(),
						Conf.colorIluminado);
				GraphConstants.setGradientColor(this.entrada.getAttributes(),
						Conf.colorIluminado);
			} else if (this.ra.estaResaltado()) {
				GraphConstants.setBackground(this.entrada.getAttributes(),
						Conf.colorResaltado);
				GraphConstants.setGradientColor(this.entrada.getAttributes(),
						Conf.colorResaltado);
			} else {
				GraphConstants
				.setBackground(
						this.entrada.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC1Entrada
								: Conf.coloresNodo[this.ra
								                   .getNumMetodo() % 10]));
				GraphConstants
				.setGradientColor(
						this.entrada.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC2Entrada
								: Conf.coloresNodo2[this.ra
								                    .getNumMetodo() % 10]));
			}

			this.marcoCelda(this.entrada.getAttributes(), false);
		} else if ((Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)
				&& (Conf.historia == 1 && this.ra.esHistorico())
				&& !(this.ra.inhibido() && !Conf.mostrarArbolSalto)) {
			this.entrada = new DefaultGraphCell(new String(cadenaEntrada));
			this.entrada.setAttributes(amap);
			if (this.portEntrada != null) {
				this.entrada.add(this.portEntrada);
			}
			GraphConstants.setOpaque(this.entrada.getAttributes(), true); // Atenuados
			GraphConstants.setForeground(this.entrada.getAttributes(),
					Conf.colorFEntrada);
			if (this.ra.estaIluminado()) {
				GraphConstants.setBackground(this.entrada.getAttributes(),
						Conf.colorIluminado);
				GraphConstants.setGradientColor(this.entrada.getAttributes(),
						Conf.colorIluminado);
			} else if (this.ra.estaResaltado()) {
				GraphConstants.setBackground(this.entrada.getAttributes(),
						Conf.colorResaltado);
				GraphConstants.setGradientColor(this.entrada.getAttributes(),
						Conf.colorResaltado);
			} else {
				GraphConstants
				.setBackground(
						this.entrada.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC1AEntrada
								: Conf.coloresNodoA[this.ra
								                    .getNumMetodo() % 10]));
				GraphConstants
				.setGradientColor(
						this.entrada.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC2AEntrada
								: Conf.coloresNodoA2[this.ra
								                     .getNumMetodo() % 10]));
			}
			this.marcoCelda(this.entrada.getAttributes(), false);
		} else {
			int longitud = cadenaEntrada.length();
			cadenaEntrada = " ";
			for (int i = 1; i < longitud; i++) {
				cadenaEntrada = cadenaEntrada + " ";
			}

			this.entrada = new DefaultGraphCell(new String(cadenaEntrada));
			this.entrada.setAttributes(amap);
			if (this.portEntrada != null) {
				this.entrada.add(this.portEntrada);
			}
			GraphConstants.setOpaque(this.entrada.getAttributes(), false); // *1*
			GraphConstants.setBackground(this.entrada.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setGradientColor(this.entrada.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setForeground(this.entrada.getAttributes(),
					Conf.colorPanel);

			this.marcoCelda(this.entrada.getAttributes(), true);
		}
	}

	/**
	 * Crea una nueva celda de salida y asigna los colores configurados.
	 * 
	 * @param cadenaEntrada
	 *            Cadena de salida que mostrará la celda.
	 */
	private void asignarColoresSalida(String cadenaSalida) {
		
		AttributeMap amap = this.salida.getAttributes();

		if ((Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)
				&& this.ra.salidaVisible()
				&& ((!this.ra.esHistorico() || (this.ra.esHistorico() && Conf.historia == 0)) && (!this.ra
						.inhibido() || (this.ra.inhibido() && Conf.mostrarArbolSalto)))) {

			if (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar
					&& !this.ra.esHistorico() && !this.ra.esMostradoEntero()) {
				cadenaSalida = " ";
			}

			this.salida = new DefaultGraphCell(new String(cadenaSalida));
			this.salida.setAttributes(amap);
			if (this.portSalida != null) {
				this.salida.add(this.portSalida);
			}
			GraphConstants.setOpaque(this.salida.getAttributes(), true); // Normales
			GraphConstants.setForeground(this.salida.getAttributes(),
					Conf.colorFSalida);
				//	Conf.colorFEntrada);
			if (this.ra.estaIluminado()) {
				GraphConstants.setBackground(this.salida.getAttributes(),
						Conf.colorIluminado);
				GraphConstants.setGradientColor(this.salida.getAttributes(),
						Conf.colorIluminado);
			} else if (this.ra.estaResaltado()) {
				GraphConstants.setBackground(this.salida.getAttributes(),
						Conf.colorResaltado);
				GraphConstants.setGradientColor(this.salida.getAttributes(),
						Conf.colorResaltado);
			} else {
				GraphConstants
				.setBackground(
						this.salida.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC1Salida
								: Conf.coloresNodo[this.ra
								                   .getNumMetodo() % 10]));
				GraphConstants
				.setGradientColor(
						this.salida.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC2Salida
								: Conf.coloresNodo2[this.ra
								                    .getNumMetodo() % 10]));
			}
			this.marcoCelda(this.salida.getAttributes(), false);
		} else if ((Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)
				&& (Conf.historia == 1 && this.ra.esHistorico())
				&& !(this.ra.inhibido() && !Conf.mostrarArbolSalto)) {
			this.salida = new DefaultGraphCell(new String(cadenaSalida));
			this.salida.setAttributes(amap);
			if (this.portSalida != null) {
				this.salida.add(this.portSalida);
			}
			GraphConstants.setOpaque(this.salida.getAttributes(), true); // Atenuados
			GraphConstants.setForeground(this.salida.getAttributes(),
					Conf.colorFSalida);
			if (this.ra.estaIluminado()) {
				GraphConstants.setBackground(this.salida.getAttributes(),
						Conf.colorIluminado);
				GraphConstants.setGradientColor(this.salida.getAttributes(),
						Conf.colorIluminado);
			} else if (this.ra.estaResaltado()) {
				GraphConstants.setBackground(this.salida.getAttributes(),
						Conf.colorResaltado);
				GraphConstants.setGradientColor(this.salida.getAttributes(),
						Conf.colorResaltado);
			} else {
				GraphConstants
				.setBackground(
						this.salida.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC1ASalida
								: Conf.coloresNodoA[this.ra
								                    .getNumMetodo() % 10]));
				GraphConstants
				.setGradientColor(
						this.salida.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC2ASalida
								: Conf.coloresNodoA2[this.ra
								                     .getNumMetodo() % 10]));
			}
			this.marcoCelda(this.salida.getAttributes(), false);
		} else if (this.ra.entradaVisible()
				&& !this.ra.esHistorico()
				&& (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) // Si
			// no
			// calculado
		{
			int longitud = cadenaSalida.length();
			cadenaSalida = " ";
			for (int i = 1; i < longitud; i++) {
				cadenaSalida = cadenaSalida + " ";
			}

			this.salida = new DefaultGraphCell(new String(cadenaSalida));
			this.salida.setAttributes(amap);
			if (this.portSalida != null) {
				this.salida.add(this.portSalida);
			}
			GraphConstants.setOpaque(this.salida.getAttributes(), true); // Salida
			// no
			// calculada
			GraphConstants.setForeground(this.salida.getAttributes(),
					Conf.colorFSalida);
			if (this.ra.estaIluminado()) {
				GraphConstants.setBackground(this.salida.getAttributes(),
						Conf.colorIluminado);
				GraphConstants.setGradientColor(this.salida.getAttributes(),
						Conf.colorIluminado);
			} else if (this.ra.estaResaltado()) {
				GraphConstants.setBackground(this.salida.getAttributes(),
						Conf.colorResaltado);
				GraphConstants.setGradientColor(this.salida.getAttributes(),
						Conf.colorResaltado);
			} else {
				GraphConstants
				.setBackground(
						this.salida.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC1NCSalida
								: Conf.coloresNodo[this.ra
								                   .getNumMetodo() % 10]));
				GraphConstants
				.setGradientColor(
						this.salida.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC2NCSalida
								: Conf.coloresNodo2[this.ra
								                    .getNumMetodo() % 10]));
			}
			this.marcoCelda(this.salida.getAttributes(), false);
		} else {
			int longitud = cadenaSalida.length();
			cadenaSalida = " ";
			for (int i = 1; i < longitud; i++) {
				cadenaSalida = cadenaSalida + " ";
			}

			this.salida = new DefaultGraphCell(new String(cadenaSalida));
			this.salida.setAttributes(amap);
			if (this.portSalida != null) {
				this.salida.add(this.portSalida);
			}
			GraphConstants.setOpaque(this.salida.getAttributes(), false); // *1*
			GraphConstants.setBackground(this.salida.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setGradientColor(this.salida.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setForeground(this.salida.getAttributes(),
					Conf.colorPanel);
			this.marcoCelda(this.salida.getAttributes(), true);
		}
	}

	/**
	 * Se encarga de generar los bordes de las celdas de entrada y salida.
	 */
	private void generacionBordes() {
		if (this.marcoEntrada != null) {
			GraphConstants.setOpaque(this.marcoEntrada.getAttributes(), false);
			GraphConstants.setBackground(this.marcoEntrada.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setGradientColor(this.marcoEntrada.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setForeground(this.marcoEntrada.getAttributes(),
					Conf.colorPanel);
		}
		if (this.marcoSalida != null) {
			GraphConstants.setOpaque(this.marcoSalida.getAttributes(), false);
			GraphConstants.setBackground(this.marcoSalida.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setGradientColor(this.marcoSalida.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setForeground(this.marcoSalida.getAttributes(),
					Conf.colorPanel);
		}

		// Generación de borde para nodo actual
		if (this.ra.getEsNodoActual()) {

			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
					|| Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
				this.marcoEntrada = new DefaultGraphCell("");
				GraphConstants.setBackground(this.marcoEntrada.getAttributes(),
						Conf.colorMarcoActual);
				GraphConstants.setOpaque(this.marcoEntrada.getAttributes(),
						true);
				GraphConstants.setMoveable(this.marcoEntrada.getAttributes(),
						false);
				GraphConstants.setSelectable(this.marcoEntrada.getAttributes(),
						false);
			}

			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
					|| Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
				this.marcoSalida = new DefaultGraphCell("");

				GraphConstants.setBackground(this.marcoSalida.getAttributes(),
						Conf.colorMarcoActual);
				
				GraphConstants
				.setOpaque(this.marcoSalida.getAttributes(), true);
				GraphConstants.setMoveable(this.marcoSalida.getAttributes(),
						false);
				GraphConstants.setSelectable(this.marcoSalida.getAttributes(),
						false);
			}
		}
		// Generación de borde para camino actual
		else if (this.ra.getEsCaminoActual()) {

			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
					|| Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
				this.marcoEntrada = new DefaultGraphCell("");
				GraphConstants.setBackground(this.marcoEntrada.getAttributes(),
						Conf.colorMarcosCActual);
				GraphConstants.setOpaque(this.marcoEntrada.getAttributes(),
						true);
				GraphConstants.setMoveable(this.marcoEntrada.getAttributes(),
						false);
				GraphConstants.setSelectable(this.marcoEntrada.getAttributes(),
						false);
			}
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
					|| Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
				this.marcoSalida = new DefaultGraphCell("");
				GraphConstants.setBackground(this.marcoSalida.getAttributes(),
						Conf.colorMarcosCActual);
				GraphConstants
				.setOpaque(this.marcoSalida.getAttributes(), true);
				GraphConstants.setMoveable(this.marcoSalida.getAttributes(),
						false);
				GraphConstants.setSelectable(this.marcoSalida.getAttributes(),
						false);
			}
		}
	}

	/**
	 * Genera las aristas entre los distintos nodos del árbol.
	 */
	private void generacionFlechas() {
		// Flechas (edges, tantas como hijos)
		int numeroHijosRecorrer = (Conf.mostrarArbolColapsado ? this.contenedoresHijos.length
				: this.ra.numHijos());
		this.edges = new DefaultEdge[numeroHijosRecorrer];
		for (int i = 0; i < numeroHijosRecorrer; i++) {
			this.edges[i] = new DefaultEdge();

			this.edges[i].setSource(((this.getPuertoVisibleParaHijo())));

			try {

				this.edges[i].setTarget(((this.contenedoresHijos[i]
						.getPuertoVisibleParaPadre())));
			} catch (Exception e) {
				e.printStackTrace();
			}
			int tipoFlecha = GraphConstants.ARROW_NONE;
			switch (Conf.formaFlecha) {
			case 0:
				tipoFlecha = GraphConstants.ARROW_NONE;
				break;
			case 1:
				tipoFlecha = GraphConstants.ARROW_TECHNICAL;
				break;
			case 2:
				tipoFlecha = GraphConstants.ARROW_CLASSIC;
				break;
			case 3:
				tipoFlecha = GraphConstants.ARROW_SIMPLE;
				break;
			case 4:
				tipoFlecha = GraphConstants.ARROW_DIAMOND;
				break;
			case 5:
				tipoFlecha = GraphConstants.ARROW_LINE;
				break;
			case 6:
				tipoFlecha = GraphConstants.ARROW_DOUBLELINE;
				break;
			case 7:
				tipoFlecha = GraphConstants.ARROW_CIRCLE;
				break;
			}

			GraphConstants
			.setLineEnd(this.edges[i].getAttributes(), tipoFlecha);
			GraphConstants.setEndFill(this.edges[i].getAttributes(), true);
			GraphConstants.setSelectable(this.edges[i].getAttributes(), false);

			if ((this.contenedoresHijos[i].entradaVisible() || this.contenedoresHijos[i]
					.salidaVisible())
					&& !(Conf.historia == 2 && this.contenedoresHijos[i]
							.esHistorico())
							&& !(!Conf.mostrarArbolSalto && this.contenedoresHijos[i]
									.inhibido())) {
				GraphConstants.setLineWidth(this.edges[i].getAttributes(),
						Conf.grosorFlecha); // grosor de línea a 8 puntos
				GraphConstants.setLineColor(this.edges[i].getAttributes(),
						Conf.colorFlecha); // color de la línea
			} else {
				GraphConstants.setLineWidth(this.edges[i].getAttributes(),
						Conf.grosorFlecha); // grosor de línea a 8 puntos
				GraphConstants.setLineColor(this.edges[i].getAttributes(),
						Conf.colorPanel);
			}
		}

	}

	/**
	 * Devuelve todas las celdas que componen el grafo del contenedor,
	 * incluyendo los contenedores hijos.
	 * 
	 * @return Celdas que componen el grafo.
	 */
	public Object[] getCeldas() {
		if (GraphConstants.getBounds(this.marcoEntrada.getAttributes()) == null) {
			Rectangle2D r = GraphConstants.getBounds(this.entrada
					.getAttributes());
			GraphConstants.setBounds(
					this.marcoEntrada.getAttributes(),
					new Rectangle((int) (r.getMinX() - Conf.grosorMarco),
							(int) (r.getMinY() - Conf.grosorMarco), (int) (r
									.getWidth() + Conf.grosorMarco * 2),
									(int) (r.getHeight() + Conf.grosorMarco * 2)));
		}

		if (GraphConstants.getBounds(this.marcoSalida.getAttributes()) == null) {
			Rectangle2D r = GraphConstants.getBounds(this.salida
					.getAttributes());
			GraphConstants.setBounds(
					this.marcoSalida.getAttributes(),
					new Rectangle((int) (r.getMinX() - Conf.grosorMarco),
							(int) (r.getMinY() - Conf.grosorMarco), (int) (r
									.getWidth() + Conf.grosorMarco * 2),
									(int) (r.getHeight() + Conf.grosorMarco * 2)));
		}

		Object celdasNodo[];
		Object celdasNodo2[];
		celdasNodo = new Object[4];
		celdasNodo[0] = this.marcoEntrada;
		celdasNodo[1] = this.marcoSalida;
		celdasNodo[2] = this.entrada;
		celdasNodo[3] = this.salida;
		
		
		
		//	this.celdasEstr2=this.celdasEstr;
			//this.celdasEstr[0]= new DefaultGraphCell("Probando");
		/*AttributeMap x =	this.celdasEstr[0].getAttributes();
		Enumeration y =x.elements();
		while(y.hasMoreElements()) {
		Object u=	y.nextElement();
		Exception th = new Exception("Atributo ="+ u.toString());
		try {
			throw(th);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		GraphConstants.setBounds(this.celdasEstr2[0].getAttributes(), new Rectangle(1900
				, 100, 200, 200));
		GraphConstants.setBounds(this.celdasEstr[1].getAttributes(), new Rectangle(1900
				, 400, 40, 40));
			
		*/
		
		if (this.celdasEstr != null) {
			celdasNodo = this.concatenarArrays(celdasNodo,this.celdasEstr);
			//celdasNodo = this.concatenarArrays(celdasNodo,this.celdasEstr);
			
			
		}if (this.celdasEstr2 != null) {
			celdasNodo = this.concatenarArrays(celdasNodo,this.celdasEstr2);
			//celdasNodo = this.concatenarArrays(celdasNodo,this.celdasEstr);
			
			
		}

		if (this.tieneHijos()) {
			Object celdasDeHijos[][] = new Object[this.contenedoresHijos.length][];

			for (int i = 0; i < this.contenedoresHijos.length; i++) {
				celdasDeHijos[i] = this.contenedoresHijos[i].getCeldas();
			}

			Object flecha[] = new Object[1];

			for (int i = 0; i < this.contenedoresHijos.length; i++) {
				flecha[0] = this.edges[i];

				celdasNodo = this
						.concatenarArrays(celdasNodo, celdasDeHijos[i]);
				celdasNodo = this.concatenarArrays(flecha, celdasNodo);
			}
		}

		return celdasNodo;
	}
	/**
	 * Permite concatenar dos arrays.
	 * 
	 * @param a primer array.
	 * @param b segundo array.
	 * 
	 * @return Array resultante de la concatenación de ambos arrays.
	 */
	private Object[] concatenarArrays(Object a[], Object b[]) {
		Object[] x = new Object[a.length + b.length];

		for (int i = 0; i < a.length; i++) {
			x[i] = a[i];
		}

		for (int i = 0; i < b.length; i++) {
			x[i + a.length] = b[i];
		}
		

		return x;
	}
	/**
	 * Permite concatenar dos arrays.
	 * 
	 * @param a primer array.
	 * @param b segundo array.
	 * @param c tercer array.
	 * 
	 * @return Array resultante de la concatenación de ambos arrays.
	 */
	private Object[] concatenarArrays2(Object a[], Object b[],Object c []) {
		Object[] x = new Object[a.length + b.length+c.length];

		for (int i = 0; i < a.length; i++) {
			x[i] = a[i];
		}

		for (int i = 0; i < b.length; i++) {
			x[i + a.length] = b[i];
		}
		for (int i = 0; i < c.length; i++) {
			x[i + a.length+b.length] = c[i];
		}

		return x;
	}
	
	/**
	 * Devuelve las aristas del contenedor.
	 * 
	 * @return Array con las aristas del contenedor.
	 */
	public Object[] getEdges() {
		// Array para los hijos Edge
		Object[] hijosEdge = new Object[this.edges.length];
		for (int i = 0; i < this.edges.length; i++) {
			hijosEdge[i] = this.edges[i];
		}
		return hijosEdge;
	}

	/**
	 * Devuelve las aristas del contenedor incluyendo las de sus hijos.
	 * 
	 * @return Array con las aristas del contenedor y las de sus hijos.
	 */
	public Object[] getAllEdges() {
		int x, i, j;
		Object edgesHijos[][] = new Object[this.contenedoresHijos.length][];

		for (i = 0; i < this.contenedoresHijos.length; i++) {
			edgesHijos[i] = this.contenedoresHijos[i].getAllEdges();
		}

		int numTotalEdges = this.edges.length;
		for (i = 0; i < edgesHijos.length; i++) {
			numTotalEdges += edgesHijos[i].length;
		}

		Object edgesTotales[] = new Object[numTotalEdges];

		for (i = 0; i < this.edges.length; i++) {
			edgesTotales[i] = this.edges[i];
		}
		x = i;

		for (i = 0; i < edgesHijos.length; i++) {
			for (j = 0; j < edgesHijos[i].length; j++, x++) {
				edgesTotales[x] = edgesHijos[i][j];
			}
		}

		return edgesTotales;
	}
	
	/**
	 * Permite obtener el registro de activación correspondiente a una posición del contenedor.
	 * 
	 * @param x Posición sobre x
	 * @param y Posición sobre y
	 * 
	 * @return Registro de activación correspondiente al nodo, null si la posición no corresponde a ninguno.
	 */
	public RegistroActivacion getRegistroPosicion(int x, int y) {
		Rectangle2D rect;

		rect = GraphConstants.getBounds(this.entrada.getAttributes());
		if (rect.getMaxX() >= x && rect.getMinX() <= x && rect.getMaxY() >= y
				&& rect.getMinY() <= y) {
			if (GraphConstants.isOpaque(this.entrada.getAttributes())) {
				return this.ra;
			} else {
				return null;
			}
		}
		rect = GraphConstants.getBounds(this.salida.getAttributes());
		if (rect.getMaxX() >= x && rect.getMinX() <= x && rect.getMaxY() >= y
				&& rect.getMinY() <= y) {
			if (GraphConstants.isOpaque(this.salida.getAttributes())) {
				return this.ra;
			} else {
				return null;
			}
		}

		for (int i = 0; i < this.contenedoresHijos.length; i++) {
			RegistroActivacion ra = this.contenedoresHijos[i]
					.getRegistroPosicion(x, y);
			if (ra != null) {
				return ra;
			}
		}

		return null;
	}
	
	/**
	 * Permite construir las celdas correspondientes a la estructura.
	 * 
	 * @param e Representación de la estructura.
	 * @param indices Índices que delimitan la visualización de la estructura.
	 * @param es "entrada" o "salida"
	 * @param nivel Nivel del árbol en el que se encuentra.
	 * @param visible Si las celdas deben ser visibles o no.
	 * 
	 * @return Matriz de celdas correspondientes a la estructura.
	 */
	private DefaultGraphCell[] extraerCeldasEstructura(Estructura e,
			int indices[], String es, int nivel, boolean visible) {
		DefaultGraphCell[] celdas = null;
		int dimensiones[] = e.dimensiones();
//dimensiones[-1]=0;
		String clase = e.getTipo().getClass().getName();
		String texto;

		if (e.esMatriz()) {
			Exception th = new Exception("es Matriz");
			try {
				throw(th);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			celdas = new DefaultGraphCell[dimensiones[0] * dimensiones[1]];
			int posic = 0;

			for (int i = 0; i < dimensiones[0]; i++) {
				for (int j = 0; j < dimensiones[1]; j++) {
					texto = "";
					if (visible && indices.length > 0 && i >= indices[0]
							&& i <= indices[1] && j >= indices[2]
									&& j <= indices[3]) {
						// Si es nodo de salida no calculado y estamos viendo...
						if (!es.equals("entrada")
								&& (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar)
								&& !this.ra.esMostradoEntero()
								&& !this.ra.esHistorico()) // Si no calculado
						{
							texto = "   ";
						} else {
							if (clase.contains("Integer")) {
								texto = " " + e.posicMatrizInt(i, j) + " ";
							} else if (clase.contains("Long")) {
								texto = " " + e.posicMatrizLong(i, j) + " ";
							} else if (clase.contains("Double")) {
								texto = " " + e.posicMatrizDouble(i, j) + " ";
							} else if (clase.contains("String")) {
								texto = " " + e.posicMatrizString(i, j) + " ";
							} else if (clase.contains("Character")) {
								texto = " " + e.posicMatrizChar(i, j) + " ";
							} else if (clase.contains("Boolean")) {
								texto = " " + e.posicMatrizBool(i, j) + " ";
							} else {
								texto = " " + "·?·" + " ";
							}
						}
					} else {
						texto = "  ";
					}
					celdas[posic] = new DefaultGraphCell(texto);

					GraphConstants.setOpaque(celdas[posic].getAttributes(),
							visible);
					GraphConstants.setFont(celdas[posic].getAttributes(),
							fuenteEstr);
					GraphConstants.setDisconnectable(
							celdas[posic].getAttributes(), false);
					GraphConstants.setMoveable(celdas[posic].getAttributes(),
							false);
					GraphConstants.setSelectable(celdas[posic].getAttributes(),
							false);
					if (visible) {
						this.marcoCelda(celdas[posic].getAttributes(), false);
					}
					String tipo=Ventana.thisventana.claseAlgoritmo.getMetodoPrincipal().getTipo();
					Color colores[]= new Color[3];	
					if(tipo.equals("void")) {
						colores[0]=Conf.colorFSalida;
						colores[1]=Conf.colorC1Salida;
						colores[2]=Conf.colorC1ASalida;
						
					}else 
					{
						colores[0]=Conf.colorFEntrada;
						colores[1]=Conf.colorC1Entrada;
						colores[2]=Conf.colorC1AEntrada;
						
					}
					
						GraphConstants.setForeground(
								celdas[posic].getAttributes(),
								colores[0]);
						if (indices.length > 0 && i >= indices[0]
								&& i <= indices[1] && j >= indices[2]
										&& j <= indices[3]) {
							GraphConstants.setBackground(
									celdas[posic].getAttributes(),
									colores[1]);
						} else {
							GraphConstants.setBackground(
									celdas[posic].getAttributes(),
									colores[2]);
						}
					
					GraphConstants.setSize(celdas[posic].getAttributes(),
							new Dimension(anchoCeldaEstr, alturaCeldaEstr));

					posic++;
				}
			}
		} else {
			
		int [] ejey=null;
		if(this.esAlgoritmoPuntos) {
			
			MetodoAlgoritmo maux=Ventana.thisventana.claseAlgoritmo.getUltimoMetodoSeleccionado();
			String x2 =	maux.getParamValor(maux.getIndiceEstructura()+1);
			
			x2 =x2.replace("{"," " );
			x2= x2.replace("}"," " );
			ServiciosString ss = new ServiciosString();
			
			 ejey=ss.extraerValoresInt(x2, ',');
			//int [] ejey2 =  {1,2,3,4};
			
			this.celdasEstr2=new DefaultGraphCell[dimensiones[0]];
		}
			celdas = new DefaultGraphCell[dimensiones[0]];
			String texto2="";
			int posic = 0;

			for (int i = 0; i < dimensiones[0]; i++) {
				texto = "";
				if (visible && indices.length > 0 && i >= indices[0]
						&& i <= indices[1]) {
					// Si es nodo de salida no calculado y estamos viendo...
					if (!es.equals("entrada")
							&& (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar)
							&& !this.ra.esMostradoEntero()
							&& !this.ra.esHistorico()) // Si no calculado
					{
						texto = "   ";
						texto2 ="   ";
					} else {
						if (clase.contains("Integer")) {
							texto = " " + e.posicArrayInt(i) + " ";
						} else if (clase.contains("Long")) {
							texto = " " + e.posicArrayLong(i) + " ";
						} else if (clase.contains("Double")) {
							texto = " " + e.posicArrayDouble(i) + " ";
						} else if (clase.contains("String")) {
							texto = " " + e.posicArrayString(i) + " ";
						} else if (clase.contains("Character")) {
							texto = " " + e.posicArrayChar(i) + " ";
						} else if (clase.contains("Boolean")) {
							texto = " " + e.posicArrayBool(i) + " ";
						} else {
							texto = "  " + "MNoDef" + "  ";
						}
						if(esAlgoritmoPuntos) {
						texto2 = ""+ejey[i];}
					}
				} else {
					texto = "  ";
					texto2 = "  ";
				}
				celdas[posic] = new DefaultGraphCell(texto);
				
				
				GraphConstants
				.setOpaque(celdas[posic].getAttributes(), visible);
				GraphConstants.setFont(celdas[posic].getAttributes(), fuenteEstr);
				GraphConstants.setDisconnectable(celdas[posic].getAttributes(),
						false);
				GraphConstants
				.setMoveable(celdas[posic].getAttributes(), false);
				GraphConstants.setSelectable(celdas[posic].getAttributes(),
						false);
				if(esAlgoritmoPuntos) {
					this.celdasEstr2[i] = new DefaultGraphCell
							(""+texto2);
					
					GraphConstants
					.setOpaque(this.celdasEstr2[i].getAttributes(), visible);
					GraphConstants.setFont(this.celdasEstr2[i].getAttributes(), fuenteEstr);
					GraphConstants.setDisconnectable(this.celdasEstr2[i].getAttributes(),
							false);
					GraphConstants
					.setMoveable(this.celdasEstr2[i].getAttributes(), false);
					GraphConstants.setSelectable(this.celdasEstr2[i].getAttributes(),
							false);
					}
					
				if (visible) {
					this.marcoCelda(celdas[posic].getAttributes(), false);
					if(esAlgoritmoPuntos) {
					this.marcoCelda(this.celdasEstr2[i].getAttributes(), false);
					}
				}
				if (es.equals("entrada")) {
					GraphConstants.setForeground(celdas[posic].getAttributes(),
							Conf.colorFEntrada);
					if(esAlgoritmoPuntos) {
					GraphConstants.setForeground(this.celdasEstr2[i].getAttributes(),
							Conf.colorFEntrada);
					}
					if (indices.length > 0 && i >= indices[0]
							&& i <= indices[1]) {
						GraphConstants.setBackground(
								celdas[posic].getAttributes(),
								Conf.colorC1Entrada);
						if(esAlgoritmoPuntos) {
						GraphConstants.setBackground(
								this.celdasEstr2[i].getAttributes(),
								Conf.colorC1Entrada);
						}
					} else {
						GraphConstants.setBackground(
								celdas[posic].getAttributes(),
								Conf.colorC1AEntrada);
						if(esAlgoritmoPuntos) {
						GraphConstants.setBackground(
								this.celdasEstr2[i].getAttributes(),
								Conf.colorC1AEntrada);
						}
						}
				} else {
				String tipo=Ventana.thisventana.claseAlgoritmo.getMetodoPrincipal().getTipo();
				Color colores[]= new Color[3];	
				if(tipo.equals("void")) {
					colores[0]=Conf.colorFSalida;
					colores[1]=Conf.colorC1Salida;
					colores[2]=Conf.colorC1ASalida;
					
				}else 
				{
					colores[0]=Conf.colorFEntrada;
					colores[1]=Conf.colorC1Entrada;
					colores[2]=Conf.colorC1AEntrada;
					
				}
				GraphConstants.setForeground(celdas[posic].getAttributes(),
							colores[0]);
				if(esAlgoritmoPuntos) {
				GraphConstants.setForeground(this.celdasEstr2[i].getAttributes(),
									colores[0]);
				}
				if (indices.length > 0 && i >= indices[0]
							&& i <= indices[1]) {
						GraphConstants.setBackground(
								celdas[posic].getAttributes(),
								colores[1]);
						if(esAlgoritmoPuntos) {
						GraphConstants.setBackground(
								this.celdasEstr2[i].getAttributes(),
								colores[1]);
						}
					} else {
						GraphConstants.setBackground(
								celdas[posic].getAttributes(),
								colores[2]);
						if(esAlgoritmoPuntos) {
						GraphConstants.setBackground(
								this.celdasEstr2[i].getAttributes(),
								colores[2]);}
					}
				}

				posic++;
			}

		}

		return celdas;
	}
	
	/**
	 * Posición inicial del nivel actual (Eje y).
	 * 
	 * @return Posición inicial del nivel actual.
	 */
	private int posic0Nivel() {
		return (altoDeNivel + Conf.altoSeguridad + Conf.sepV)
				* (this.nivel - 1) + espacioInicial;
	}
	
	/**
	 * Establece el diseño del marco de la celda dado su mapa de atributos.
	 * 
	 * @param am Mapa de atributos de una celda concreta.
	 * @param anular A true si se desea eliminar el borde, false en caso contrario.
	 */
	private void marcoCelda(AttributeMap am, boolean anular) {
		if (!anular) {
			switch (Conf.bordeCelda) {
			case 1:
				GraphConstants
				.setBorder(am, BorderFactory.createBevelBorder(0));
				break;
			case 2:
				GraphConstants
				.setBorder(am, BorderFactory.createEtchedBorder());
				break;
			case 3:
				GraphConstants.setBorder(am,
						BorderFactory.createLineBorder(Conf.colorFlecha));
				break;
			case 4:
				GraphConstants.setBorder(am,
						BorderFactory.createLoweredBevelBorder());
				break;
			case 5:
				GraphConstants.setBorder(am,
						BorderFactory.createRaisedBevelBorder());
				break;
			}
		} else {
			GraphConstants.setBorder(am, BorderFactory.createEmptyBorder());
		}
	}
	
	/**
	 * Permite crear el Cuadro Visor del árbol.
	 * 
	 * @param anchoCuadroVisor Ancho del cuadro
	 * @param altoCuadroVisor Alto del cuadro
	 * @param posicX posición sobre X
	 * @param posicY posición sobre Y
	 * 
	 * @return Cuadro Visor del árbol.
	 */
	public static DefaultGraphCell crearCuadroVisor(int anchoCuadroVisor,
			int altoCuadroVisor, int posicX, int posicY) {
		DefaultGraphCell cuadroVisor = new DefaultGraphCell();
		GraphConstants.setBounds(cuadroVisor.getAttributes(),
				new Rectangle2D.Double(posicX, posicY, anchoCuadroVisor,
						altoCuadroVisor));
		GraphConstants.setValue(cuadroVisor.getAttributes(), "");
		GraphConstants.setOpaque(cuadroVisor.getAttributes(), false);
		GraphConstants.setBorder(cuadroVisor.getAttributes(),
				BorderFactory.createLineBorder(Conf.colorFlecha, 24));
		GraphConstants.setSizeable(cuadroVisor.getAttributes(), false);
		return cuadroVisor;
	}
	
	/**
	 * Obtiene el rectángulo que delimita la celda pasada por parámetro.
	 * 
	 * @param celda
	 * 
	 * @return Rectángulo que delimita la celda.
	 */
	public static Rectangle2D rectanguloCelda(DefaultGraphCell celda) {
		return GraphConstants.getBounds(celda.getAttributes());
	}
	
	/**
	 * Genera la cadena de salida que debe visualizarse, aplicando los prefijos
	 * correspondientes al método.
	 * 
	 * @return Cadena de salida para visualizar.
	 */
	private String generaCadenaSalida() {
		String repSalida = this.ra.getSalida().getRepresentacion();
		String cadenaSalida = "";
		if (this.nyp != null
				&& (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA)) {
			cadenaSalida = this.nyp.getPrefijo(this.ra.getNombreMetodo())
					+ ": ";
		}

		cadenaSalida = cadenaSalida + repSalida;

		if (repSalida.length() < 3) {
			cadenaSalida = "  " + cadenaSalida + "  ";
		}

		return cadenaSalida;
	}
	
	/**
	 * Devuelve la máxima longitud del contenido de la estructura.
	 * 
	 * @return Máxima longitud del contenido de la estructura.
	 */
	private int maximaLongitudContenidoEstructura() {
		int longMaxima = 0;

		if (this.ra.esDYV()) {
			Estructura e1 = new Estructura(this.ra.getEntrada().getEstructura());
			Estructura e2 = new Estructura(this.ra.getSalida().getEstructura());
			int dimensiones[] = e1.dimensiones();
			String clase = e1.getTipo().getClass().getName();

			if (e1.esMatriz()) {
				for (int i = 0; i < dimensiones[0]; i++) {
					for (int j = 0; j < dimensiones[1]; j++) {
						if (clase.contains("Integer")) {
							longMaxima = Math.max(longMaxima,
									("" + e1.posicMatrizInt(i, j)).length());
							longMaxima = Math.max(longMaxima,
									("" + e2.posicMatrizInt(i, j)).length());
						} else if (clase.contains("Long")) {
							longMaxima = Math.max(longMaxima,
									("" + e1.posicMatrizLong(i, j)).length());
							longMaxima = Math.max(longMaxima,
									("" + e2.posicMatrizLong(i, j)).length());
						} else if (clase.contains("Double")) {
							longMaxima = Math.max(longMaxima,
									("" + e1.posicMatrizDouble(i, j)).length());
							longMaxima = Math.max(longMaxima,
									("" + e2.posicMatrizDouble(i, j)).length());
						} else if (clase.contains("String")) {
							longMaxima = Math.max(longMaxima,
									(e1.posicMatrizString(i, j)).length());
							longMaxima = Math.max(longMaxima,
									(e2.posicMatrizString(i, j)).length());
						} else if (clase.contains("Character")) {
							longMaxima = Math.max(longMaxima,
									("" + e1.posicMatrizChar(i, j)).length());
							longMaxima = Math.max(longMaxima,
									("" + e2.posicMatrizChar(i, j)).length());
						} else if (clase.contains("Boolean")) {
							longMaxima = Math.max(longMaxima,
									("" + e1.posicMatrizBool(i, j)).length());
							longMaxima = Math.max(longMaxima,
									("" + e2.posicMatrizBool(i, j)).length());
						}
					}
				}
			} else {
				for (int i = 0; i < dimensiones[0]; i++) {
					if (clase.contains("Integer")) {
						longMaxima = Math.max(longMaxima,
								("" + e1.posicArrayInt(i)).length());
						longMaxima = Math.max(longMaxima,
								("" + e2.posicArrayInt(i)).length());
					} else if (clase.contains("Long")) {
						longMaxima = Math.max(longMaxima,
								("" + e1.posicArrayLong(i)).length());
						longMaxima = Math.max(longMaxima,
								("" + e2.posicArrayLong(i)).length());
					} else if (clase.contains("Double")) {
						longMaxima = Math.max(longMaxima,
								("" + e1.posicArrayDouble(i)).length());
						longMaxima = Math.max(longMaxima,
								("" + e2.posicArrayDouble(i)).length());
					} else if (clase.contains("String")) {
						longMaxima = Math.max(longMaxima,
								(e1.posicArrayString(i)).length());
						longMaxima = Math.max(longMaxima,
								(e2.posicArrayString(i)).length());
					} else if (clase.contains("Character")) {
						longMaxima = Math.max(longMaxima,
								("" + e1.posicArrayChar(i)).length());
						longMaxima = Math.max(longMaxima,
								("" + e2.posicArrayChar(i)).length());
					} else if (clase.contains("Boolean")) {
						longMaxima = Math.max(longMaxima,
								("" + e1.posicArrayBool(i)).length());
						longMaxima = Math.max(longMaxima,
								("" + e2.posicArrayBool(i)).length());
					}
				}
			}
		}
		if (this.contenedoresHijos != null) {
			for (int i = 0; i < this.contenedoresHijos.length; i++) {
				longMaxima = Math.max(longMaxima, this.contenedoresHijos[i]
						.maximaLongitudContenidoEstructura());
			}
		}

		return this.ra.getMaximaLongitudCeldaEstructura();
	}
	/**
	 * Metodo auxiliar que establece la segunda estructura, solo para algoritmos de puntos
	 * Se utiliza en Panel Arbol
	 * 
	 */
	public void setEstr2(DefaultGraphCell[] celdasEstr22) {
		// TODO Auto-generated method stub
		this.celdasEstr2 =celdasEstr22;
	}
	public boolean esAlgortimoPuntos(MetodoAlgoritmo ma) {
	//	String[] parametros = ma.getParamValores();
		boolean estructurasok=false;
		boolean indicesok=false;
		String[] parametros = new String [ma.getDimParametros().length];
		if(parametros.length>=4) {
		for(int i =0;i<ma.getDimParametros().length;i++) {
			parametros[i]= ma.getDimParametro(i) + " "+ma.getTipoParametro(i);
			
			//ma.getIndices();
		} 
		
		for(int i =0;i<ma.getDimParametros().length;i++) {
			if(i!=ma.getDimParametros().length-2) {
				if(parametros[i].contains("2")){
					estructurasok=false;
					indicesok=false;
					break;
				}
				if(parametros[i].contains("1")&&parametros[i].contains("int")&&parametros[i+1].contains("1")&&parametros[i+1].contains("int")) {
					estructurasok=true;
				}
				if(parametros[i].contains("0")&&parametros[i].contains("int")&&parametros[i+1].contains("0")&&parametros[i+1].contains("int")) {
					indicesok=true;
				}
			}
			
		}
		}
		return estructurasok && indicesok;
		
	}

}