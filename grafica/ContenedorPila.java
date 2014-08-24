package grafica;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

import utilidades.NombresYPrefijos;
import conf.Conf;
import datos.RegistroActivacion;
import datos.Traza;

/**
 * Celda contenedora (que no se visualiza) para la representación de la vista de
 * "Pila", que contiene un subárbol sobre el cual permite realizar acciones de
 * manera sencilla
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class ContenedorPila {

	private static int nivelMaximo = 0;

	private RegistroActivacion ra;

	private DefaultGraphCell entrada;
	private DefaultGraphCell salida;

	private DefaultGraphCell marcoEntrada;
	private DefaultGraphCell marcoSalida;

	private DefaultPort portEntrada;
	private DefaultPort portSalida;
	// DefaultPort port;
	private DefaultGraphCell celdaFantasma;

	private ContenedorPila contenedorHijo;
	private DefaultEdge edges[];

	private int anchoMaximoCelda = 0; // Ancho máximo de entre todas las celdas, para
								// alinear centralmente respecto a ella
	
	/**
	 * Permite construir un nuevo ContenedorPila
	 * 
	 * @param ra Registro de activación asociado al estado que se desea visualizar.
	 * @param traza Traza asociada a la pila.
	 * @param nyp Nombres y prefijos para la representación de métodos.
	 * @param nivel Nivel en la jerarquía.
	 */
	public ContenedorPila(RegistroActivacion ra, Traza traza,
			NombresYPrefijos nyp, int nivel) {
		this.ra = ra;

		if (nivel > ContenedorPila.nivelMaximo) {
			ContenedorPila.nivelMaximo = nivel;
		}

		// Entrada, configuración de la misma y asignación de puerto
		String cadenaEntrada = "  ";
		if (Conf.idMetodoTraza) {
			cadenaEntrada = "  " + ra.getNombreMetodo() + ": ";
			if (nyp != null) {
				cadenaEntrada = "  " + nyp.getPrefijo(ra.getNombreMetodo())
						+ ": ";
			}
		}
		cadenaEntrada = cadenaEntrada + ra.getEntrada().getRepresentacion()
				+ "  ";
		this.entrada = new DefaultGraphCell(new String(cadenaEntrada));
		GraphConstants.setOpaque(this.entrada.getAttributes(), true);
		if (this.ra.entradaVisible()
				&& (Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
			GraphConstants.setForeground(this.entrada.getAttributes(),
					Conf.colorFEntrada); // Entrada

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
				GraphConstants.setBackground(this.entrada.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC1Entrada
								: Conf.coloresNodo[ra.getNumMetodo() % 10]));
				GraphConstants.setGradientColor(this.entrada.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC2Entrada
								: Conf.coloresNodo2[ra.getNumMetodo() % 10]));
			}
			// GraphConstants.setBorder( this.entrada.getAttributes(),
			// BorderFactory.createRaisedBevelBorder());
			this.marcoCelda(this.entrada.getAttributes());
		} else {
			int longitud = cadenaEntrada.length();
			cadenaEntrada = " ";
			for (int i = 1; i < longitud; i++) {
				cadenaEntrada = cadenaEntrada + " ";
			}
			this.entrada = new DefaultGraphCell(new String(cadenaEntrada));
			GraphConstants.setOpaque(this.entrada.getAttributes(), false);
			GraphConstants.setBackground(this.entrada.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setGradientColor(this.entrada.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setForeground(this.entrada.getAttributes(),
					Conf.colorPanel);
		}
		GraphConstants.setFont(this.entrada.getAttributes(), new Font("Arial",
				Font.BOLD, 20));
		// GraphConstants.setResize( this.entrada.getAttributes(), true); //
		// Tamaño justo para que texto quepa.
		GraphConstants.setDisconnectable(this.entrada.getAttributes(), false);
		GraphConstants.setMoveable(this.entrada.getAttributes(), false);
		GraphConstants.setSelectable(this.entrada.getAttributes(), false);
		GraphConstants.setSize(this.entrada.getAttributes(), new Dimension(
				11 * cadenaEntrada.length(), 26));

		this.portEntrada = new DefaultPort();
		this.entrada.add(this.portEntrada);

		// Salida, configuración de la misma y asignación de puerto
		String cadenaSalida = "  " + ra.getSalida().getRepresentacion() + "  ";

		if (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar
				&& !this.ra.esHistorico() && !this.ra.esMostradoEntero()) {
			int longitud = cadenaSalida.length();
			cadenaSalida = " ";
			for (int i = 1; i < longitud; i++) {
				cadenaSalida = cadenaSalida + " ";
			}
		}

		this.salida = new DefaultGraphCell(new String(cadenaSalida));
		GraphConstants.setOpaque(this.salida.getAttributes(), true);
		if (this.ra.salidaVisible()
				&& (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
			GraphConstants.setForeground(this.salida.getAttributes(),
					Conf.colorFSalida); // Salida

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
				GraphConstants.setBackground(this.salida.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC1Salida
								: Conf.coloresNodo[ra.getNumMetodo() % 10]));
				GraphConstants.setGradientColor(this.salida.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC2Salida
								: Conf.coloresNodo2[ra.getNumMetodo() % 10]));
			}
			// GraphConstants.setBorder( this.salida.getAttributes(),
			// BorderFactory.createRaisedBevelBorder());
			this.marcoCelda(this.salida.getAttributes());
		} else if (ra.entradaVisible()
				&& !ra.esHistorico()
				&& // si no calculado
				(Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
			this.salida = new DefaultGraphCell("  ");
			GraphConstants.setOpaque(this.salida.getAttributes(), true);
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
				GraphConstants.setBackground(this.salida.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC1NCSalida
								: Conf.coloresNodo[ra.getNumMetodo() % 10]));
				GraphConstants.setGradientColor(this.salida.getAttributes(),
						(Conf.modoColor == 1 ? Conf.colorC2NCSalida
								: Conf.coloresNodo2[ra.getNumMetodo() % 10]));
			}
			this.marcoCelda(this.salida.getAttributes());
		} else {
			int longitud = cadenaSalida.length();
			cadenaSalida = " ";
			for (int i = 1; i < longitud; i++) {
				cadenaSalida = cadenaSalida + " ";
			}
			this.salida = new DefaultGraphCell(new String(cadenaSalida));
			GraphConstants.setOpaque(this.salida.getAttributes(), false);
			GraphConstants.setBackground(this.salida.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setGradientColor(this.salida.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setForeground(this.salida.getAttributes(),
					Conf.colorPanel);
		}
		GraphConstants.setFont(this.salida.getAttributes(), new Font("Arial",
				Font.BOLD, 20));
		// GraphConstants.setResize( this.salida.getAttributes(), true); //
		// Tamaño justo para que texto quepa.
		GraphConstants.setDisconnectable(this.salida.getAttributes(), false);
		GraphConstants.setMoveable(this.salida.getAttributes(), false);
		GraphConstants.setSelectable(this.salida.getAttributes(), false);
		GraphConstants.setSize(this.salida.getAttributes(), new Dimension(
				11 * cadenaSalida.length(), 26));

		this.portSalida = new DefaultPort();
		this.salida.add(this.portSalida);

		int ramaActual = this.getRamaActual(ra);

		if (!ra.getEsNodoActual() && ramaActual != -1) {
			this.contenedorHijo = new ContenedorPila(ra.getHijo(ramaActual),
					traza, nyp, nivel + 1);
		} else {
			this.contenedorHijo = null;
		}

		int posicionamiento[] = { 0, 0, 0, 0 }; // [0]=pos horizontal, [1]=pos
												// vertical, [2]=tam horizontal,
												// [3]=tam vertical
		// Ahora sacamos la anchura máxima de celda, para alinear centralmente
		// en horizontal
		if (nivel == 1) {
			this.anchoMaximoCelda = this.getAnchoMaximoCelda();
			posicionamiento = this.ubicacion(nivel, 20, 26,
					this.anchoMaximoCelda);
		}

		// Dibujamos celda fantasma a la derecha para que el grafo no se mueva
		// cuando el marco del nodo actual esté a la derecha
		// (eso hace que el ancho del grafo aumente y se mueva todo el grafo,
		// produciendo un efecto incómodo)
		if (nivel == 1) {
			this.celdaFantasma = new DefaultGraphCell(new String(""));
			GraphConstants.setOpaque(this.celdaFantasma.getAttributes(), true);

			GraphConstants.setForeground(this.celdaFantasma.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setBackground(this.celdaFantasma.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setGradientColor(this.celdaFantasma.getAttributes(),
					Conf.colorPanel);
			GraphConstants.setBounds(this.celdaFantasma.getAttributes(),
					new Rectangle(this.maximoAncho() / 2, 0, 8, 8));
			GraphConstants.setMoveable(this.celdaFantasma.getAttributes(),
					false);
			GraphConstants.setSelectable(this.celdaFantasma.getAttributes(),
					false);
		}
	}
	
	/**
	 * Devuelve la rama actual sobre la que se está operando.
	 * 
	 * @param ra Registro de activación.
	 * 
	 * @return -1 si no hay nodo actual, 0 si es el nodo actual, o la posición
	 * del hijo directo que determina la rama actual.
	 */
	private int getRamaActual(RegistroActivacion ra) {
		// Si somos nodo actual, se lo indicamos al padre
		if (ra == null) {
			return -1;
		}

		if (ra.getEsNodoActual()) {
			return 0;
		}

		// Si no somos nodo actual, buscamos entre hijos visibles
		for (int i = 0; i < ra.getHijosVisibles(); i++) {
			if (this.getRamaActual(ra.getHijo(i)) != -1) {
				return i;
			}
		}

		// Si no hay nodo actual en este subárbol
		return -1;
	}
	
	/**
	 * Devuelve el ancho máximo de la celda.
	 * 
	 * @return Ancho máximo de la celda.
	 */
	public int getAnchoMaximoCelda() {
		int x;
		Dimension d = GraphConstants.getSize(this.entrada.getAttributes());
		x = (int) d.getWidth();

		d = GraphConstants.getSize(this.salida.getAttributes());
		if (x < (int) d.getWidth()) {
			x = (int) d.getWidth();
		}

		if (this.contenedorHijo != null
				&& x < this.contenedorHijo.getAnchoMaximoCelda()) {
			x = this.contenedorHijo.getAnchoMaximoCelda();
		}
		return x;
	}

	/**
	 * Devuelve la cabeza visible del ContenedorPila para el padre: bien la celda de
	 * entrada, bien la de salida.
	 * 
	 * @return Celda que determina la cabeza visible del nodo.
	 */
	public Object getCabezaVisibleParaPadre() {
		if (this.ra.entradaVisible()) {
			return this.entrada;
		}
		return this.salida;
	}

	/**
	 * Devuelve la cabeza visible del ContenedorPila para el hijo: bien la celda de
	 * entrada, bien la de salida.
	 * 
	 * @return Celda que determina la cabeza visible del nodo.
	 */
	public Object getCabezaVisibleParaHijo() {
		if (this.ra.salidaVisible()) {
			return this.salida;
		}
		return this.entrada;
	}

	/**
	 * Devuelve el puerto visible del ContenedorPila para el padre: bien el de
	 * entrada, bien el de salida.
	 * 
	 * @return puerto visible para el padre.
	 */
	public Object getPuertoVisibleParaPadre() {
		return ((DefaultGraphCell) this.getCabezaVisibleParaPadre())
				.getChildAt(0);
	}
	
	/**
	 * Devuelve el puerto visible del ContenedorPila para el hijo: bien el de
	 * entrada, bien el de salida.
	 * 
	 * @return puerto visible para el hijo.
	 */
	public Object getPuertoVisibleParaHijo() {
		return ((DefaultGraphCell) this.getCabezaVisibleParaHijo())
				.getChildAt(0);
	}
	
	/**
	 * Ubica las celdas de entrada y salida.
	 * 
	 * @param nivel Nivel en la jerarquía.
	 * @param tamFuente Tamaño de la fuente.
	 * @param alturaCelda Altura de la celda a representar.
	 * @param anchoMaximoCelda Ancho máximo de la celda a representar.
	 * 
	 * @return Bounds de la celda ubicada.
	 */
	private int[] ubicacion(int nivel, int tamFuente, int alturaCelda,
			int anchoMaximoCelda) {
		// Calculamos la posición vertical de ambas celdas para este nodo
		// (entrada y salida)
		int ubicacion[] = new int[4];
		ubicacion[1] = 20 // Espacio superior en panel, entre "techo" del mismo
							// y la celda raiz del árbol
				+ (2 * alturaCelda * (nivel - 1)) // Espacio según número de
													// celdas que tendremos por
													// encima
				+ ((10 + Conf.altoSeguridad + Conf.sepV) * (nivel - 1));
		/* ContenedorPila.nivelMaximo */
		ubicacion[3] = ubicacion[1] + alturaCelda;

		ubicacion[0] = 10 + (int) ((anchoMaximoCelda - GraphConstants.getSize(
				this.entrada.getAttributes()).getWidth()) / 2);
		ubicacion[2] = 10 + (int) ((anchoMaximoCelda - GraphConstants.getSize(
				this.salida.getAttributes()).getWidth()) / 2);

		GraphConstants.setBounds(
				this.entrada.getAttributes(),
				new java.awt.geom.Rectangle2D.Double(ubicacion[0],
						ubicacion[1], GraphConstants.getSize(
								this.entrada.getAttributes()).getWidth(),
						GraphConstants.getSize(this.entrada.getAttributes())
								.getHeight()));

		GraphConstants.setBounds(
				this.salida.getAttributes(),
				new java.awt.geom.Rectangle2D.Double(ubicacion[2],
						ubicacion[3], GraphConstants.getSize(
								this.salida.getAttributes()).getWidth(),
						GraphConstants.getSize(this.salida.getAttributes())
								.getHeight()));

		// Reubicación y redimensionamiento de una de las dos celdas, para
		// ajustarse a la otra
		if (GraphConstants.getSize(this.entrada.getAttributes()).getWidth() < GraphConstants
				.getSize(this.salida.getAttributes()).getWidth()) {
			GraphConstants.setResize(this.entrada.getAttributes(), false);
			GraphConstants.setBounds(
					this.entrada.getAttributes(),
					new Rectangle((int) (GraphConstants.getBounds(this.salida
							.getAttributes()).getMinX()),
							(int) (GraphConstants.getBounds(this.entrada
									.getAttributes()).getMinY()),
							(int) (GraphConstants.getSize(this.salida
									.getAttributes()).getWidth()),
							(int) (GraphConstants.getSize(this.entrada
									.getAttributes()).getHeight())));// -2
			GraphConstants.setResize(this.salida.getAttributes(), false);
			GraphConstants.setBounds(
					this.salida.getAttributes(),
					new Rectangle((int) (GraphConstants.getBounds(this.salida
							.getAttributes()).getMinX()), (int) (GraphConstants
							.getBounds(this.salida.getAttributes()).getMinY()),
							(int) (GraphConstants.getSize(this.salida
									.getAttributes()).getWidth()),
							(int) (GraphConstants.getSize(this.salida
									.getAttributes()).getHeight())));// -2
		} else if (GraphConstants.getSize(this.salida.getAttributes())
				.getWidth() < GraphConstants.getSize(
				this.entrada.getAttributes()).getWidth()) {
			GraphConstants.setResize(this.salida.getAttributes(), false);
			GraphConstants.setBounds(
					this.salida.getAttributes(),
					new Rectangle((int) (GraphConstants.getBounds(this.entrada
							.getAttributes()).getMinX()), (int) (GraphConstants
							.getBounds(this.salida.getAttributes()).getMinY()),
							(int) (GraphConstants.getSize(this.entrada
									.getAttributes()).getWidth()),
							(int) (GraphConstants.getSize(this.salida
									.getAttributes()).getHeight())));// -2
			GraphConstants.setResize(this.entrada.getAttributes(), false);
			GraphConstants.setBounds(
					this.entrada.getAttributes(),
					new Rectangle((int) (GraphConstants.getBounds(this.entrada
							.getAttributes()).getMinX()),
							(int) (GraphConstants.getBounds(this.entrada
									.getAttributes()).getMinY()),
							(int) (GraphConstants.getSize(this.entrada
									.getAttributes()).getWidth()),
							(int) (GraphConstants.getSize(this.entrada
									.getAttributes()).getHeight()))); // -2
		}

		// Marcos
		if (this.ra.getEsNodoActual()) {
			Rectangle2D rect;

			if (this.ra.entradaVisible()
					&& (Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
				this.marcoEntrada = new DefaultGraphCell("");
				rect = GraphConstants.getBounds(this.entrada.getAttributes());

				// if (rect==null)
				// System.out.println("rect null");

				GraphConstants
						.setBounds(
								this.marcoEntrada.getAttributes(),
								new Rectangle(
										(int) rect.getMinX() - Conf.grosorMarco,
										(int) rect.getMinY() - Conf.grosorMarco,
										(int) (rect.getMaxX() - rect.getMinX() + (Conf.grosorMarco * 2)),
										(int) (rect.getMaxY() - rect.getMinY() + (Conf.grosorMarco * 2))));
				GraphConstants.setBackground(this.marcoEntrada.getAttributes(),
						Conf.colorMarcoActual);
				GraphConstants.setOpaque(this.marcoEntrada.getAttributes(),
						true);
				GraphConstants.setMoveable(this.marcoEntrada.getAttributes(),
						false);
				GraphConstants.setSelectable(this.marcoEntrada.getAttributes(),
						false);

			}
			if (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar
					|| Conf.VISUALIZAR_TODO == Conf.elementosVisualizar) {
				this.marcoSalida = new DefaultGraphCell("");
				rect = GraphConstants.getBounds(this.salida.getAttributes());
				GraphConstants
						.setBounds(
								this.marcoSalida.getAttributes(),
								new Rectangle(
										(int) rect.getMinX() - Conf.grosorMarco,
										(int) rect.getMinY() - Conf.grosorMarco,
										(int) (rect.getMaxX() - rect.getMinX() + (Conf.grosorMarco * 2)),
										(int) (rect.getMaxY() - rect.getMinY() + (Conf.grosorMarco * 2))));
				GraphConstants.setBackground(this.marcoSalida.getAttributes(),
						Conf.colorMarcoActual);
				GraphConstants
						.setOpaque(this.marcoSalida.getAttributes(), true);
				GraphConstants.setMoveable(this.marcoSalida.getAttributes(),
						false);
				GraphConstants.setSelectable(this.marcoSalida.getAttributes(),
						false);
			}
		} else if (this.ra.getEsCaminoActual()) {
			Rectangle2D rect;

			if (this.ra.entradaVisible()
					&& (Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
				this.marcoEntrada = new DefaultGraphCell("");
				rect = GraphConstants.getBounds(this.entrada.getAttributes());

				GraphConstants
						.setBounds(
								this.marcoEntrada.getAttributes(),
								new Rectangle(
										(int) rect.getMinX() - Conf.grosorMarco,
										(int) rect.getMinY() - Conf.grosorMarco,
										(int) (rect.getMaxX() - rect.getMinX() + (Conf.grosorMarco * 2)),
										(int) (rect.getMaxY() - rect.getMinY() + (Conf.grosorMarco * 2))));
				GraphConstants.setBackground(this.marcoEntrada.getAttributes(),
						Conf.colorMarcosCActual);
				GraphConstants.setOpaque(this.marcoEntrada.getAttributes(),
						true);
				GraphConstants.setMoveable(this.marcoEntrada.getAttributes(),
						false);
				GraphConstants.setSelectable(this.marcoEntrada.getAttributes(),
						false);
			}
			if (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar
					|| Conf.VISUALIZAR_TODO == Conf.elementosVisualizar) {
				this.marcoSalida = new DefaultGraphCell("");
				rect = GraphConstants.getBounds(this.salida.getAttributes());
				GraphConstants
						.setBounds(
								this.marcoSalida.getAttributes(),
								new Rectangle(
										(int) rect.getMinX() - Conf.grosorMarco,
										(int) rect.getMinY() - Conf.grosorMarco,
										(int) (rect.getMaxX() - rect.getMinX() + (Conf.grosorMarco * 2)),
										(int) (rect.getMaxY() - rect.getMinY() + (Conf.grosorMarco * 2))));
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

		if (this.contenedorHijo != null) {
			this.contenedorHijo.ubicacion(nivel + 1, tamFuente, alturaCelda,
					anchoMaximoCelda);
		}

		// Flechas (edges, tantas como hijos)
		this.edges = new DefaultEdge[this.contenedorHijo != null ? 1 : 0];
		if (this.contenedorHijo != null) {
			this.edges[0] = new DefaultEdge();

			this.edges[0].setSource(((this.getPuertoVisibleParaHijo())));
			this.edges[0].setTarget(((this.contenedorHijo
					.getPuertoVisibleParaPadre())));

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
					.setLineEnd(this.edges[0].getAttributes(), tipoFlecha);
			GraphConstants.setEndFill(this.edges[0].getAttributes(), true);
			GraphConstants.setSelectable(this.edges[0].getAttributes(), false);
			GraphConstants.setLineWidth(this.edges[0].getAttributes(),
					Conf.grosorFlecha); // grosor de línea a 8 puntos
			GraphConstants.setLineColor(this.edges[0].getAttributes(),
					Conf.colorFlecha); // color de la línea
		}

		return ubicacion;
	}
	
	/**
	 * Devuelve el ancho máximo.
	 * 
	 * @return Ancho máximo.
	 */
	public int maximoAncho() {
		return (this.getAnchoMaximoCelda() + (Conf.grosorMarco * 2))
				+ (10 - Conf.grosorMarco);
	}
	
	/**
	 * Devuelve el alto máximo.
	 * 
	 * @return Alto máximo.
	 */
	public int maximoAlto() {
		if (this.contenedorHijo != null) {
			return this.contenedorHijo.maximoAlto();
		} else {
			return (int) ((GraphConstants.getBounds(this.entrada
					.getAttributes()).getMinY()) + (GraphConstants.getBounds(
					this.entrada.getAttributes()).getHeight() * 3));
		}

	}
	
	/**
	 * Devuelve las celdas de entrada y salida.
	 * 
	 * @return Celdas de entrada y salida, en las posiciones 0 y 1 respectivamente.
	 */
	public Object[] getCeldasEstado() {
		// Array para todos los hijos
		Object[] estados = new Object[2];
		estados[0] = this.entrada;
		estados[1] = this.salida;
		return estados;
	}
	
	/**
	 * Permite obtener las celdas que conforman la representación del nodo.
	 * 
	 * @return Array con las celdas del nodo.
	 */
	public Object[] getCeldas() {
		Object celdasNodo[];
		if (this.marcoEntrada == null && this.marcoSalida == null) {
			celdasNodo = new Object[2];
			celdasNodo[0] = this.entrada;
			celdasNodo[1] = this.salida;
		} else if (this.marcoEntrada != null && this.marcoSalida != null) {
			celdasNodo = new Object[4];
			celdasNodo[0] = this.marcoEntrada;
			celdasNodo[1] = this.marcoSalida;
			celdasNodo[2] = this.entrada;
			celdasNodo[3] = this.salida;
		} else {
			celdasNodo = new Object[3];
			if (this.marcoEntrada != null) {
				celdasNodo[0] = this.salida;
				celdasNodo[1] = this.marcoEntrada;
				celdasNodo[2] = this.entrada;
			} else {
				celdasNodo[0] = this.entrada;
				celdasNodo[1] = this.marcoSalida;
				celdasNodo[2] = this.salida;
			}
		}

		Object cFantasma[] = new Object[1];
		cFantasma[0] = this.celdaFantasma;

		if (this.celdaFantasma != null) {
			celdasNodo = this.concatenarArrays(cFantasma, celdasNodo);
		}

		if (this.contenedorHijo != null) {
			Object celdasDeHijos[] = this.contenedorHijo.getCeldas();

			Object flecha[] = new Object[1];

			flecha[0] = this.edges[0];

			celdasNodo = this.concatenarArrays(celdasNodo, celdasDeHijos);
			celdasNodo = this.concatenarArrays(flecha, celdasNodo);
		}

		return celdasNodo;
	}
	
	/**
	 * Permite concatenar dos arrays.
	 * 
	 * @param a Primer array.
	 * @param b Segundo array.
	 * 
	 * @return Array concatenado.
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
	 * Devuelve las aristas asociadas al nodo.
	 * 
	 * @return Aristas asociadas al nodo.
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
	 * Permite obtener el registro de activación correspondiente a las
	 * coordenadas pasadas por parámetro.
	 * 
	 * @param x Posición x.
	 * @param y Posición y.
	 * 
	 * @return Registro de activación asociado.
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
		if (this.contenedorHijo != null) {
			RegistroActivacion ra = this.contenedorHijo.getRegistroPosicion(x,
					y);
			if (ra != null) {
				return ra;
			}
		}

		return null;
	}
	
	/**
	 * Permite establecer el tipo de borde establecido por configuración
	 * para los atributos de una celda.
	 */
	private void marcoCelda(AttributeMap am) {
		switch (Conf.bordeCelda) {
		case 1:
			GraphConstants.setBorder(am, BorderFactory.createBevelBorder(0));
			break;
		case 2:
			GraphConstants.setBorder(am, BorderFactory.createEtchedBorder());
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
	}
}