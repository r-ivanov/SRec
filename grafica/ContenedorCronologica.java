package grafica;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import utilidades.NombresYPrefijos;
import utilidades.ServiciosString;
import ventanas.Ventana;
import conf.Conf;
import datos.Estructura;
import datos.RegistroActivacion;

/**
 * Celda contenedora para la vista cronológica (que no se visualiza) que contiene un subárbol sobre el
 * cual permite realizar acciones de manera sencilla
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class ContenedorCronologica {

	private RegistroActivacion ra;

	private static ArrayList<DefaultGraphCell> celdas;

	private static int contadorMatrices = 0;

	private int pixelAnchoCaracter = 10;
	private String esp = " "; // espacio antes y detrás del texto de cada celda

	private int nivelCelda = 1;

	private static int linea = 1;

	private static int maximaLongitudContenidoCelda = -1;

	private int altCelda = 26;

	private int sepIni = 20; // Separación inicial (arriba e izquierda)

	private int separacionAuxiliar;
	private int separacionAuxiliarMatricesNoCrono;
	private int separacionAuxiliarMatricesNoCrono2;

	/**
	 * Crea una nueva instancia del Contenedor.
	 * 
	 * @param nyp
	 *            Nombres y prefijos a aplicar.
	 */
	public ContenedorCronologica(NombresYPrefijos nyp) {
		contadorMatrices = 0;

		this.ra = Ventana.thisventana.getTraza().getRaiz();
		calcularMaximaLongitudContenidoCelda(this.ra);
		int tipoEstrutura = Ventana.thisventana.getTraza().tipoEstructura();

		if (tipoEstrutura == 1)// Si la estructura que se emplea en la traza es
			// un array
		{
			if (Conf.mostrarSalidaLigadaEntrada) {
				celdas = this.dameCeldasArrays(this.ra, this.nivelCelda);
			} else {
				linea = 1;
				celdas = this.dameCeldasArraysSalidaCronologica(this.ra,
						this.nivelCelda);
			}
		} else if (tipoEstrutura == 2)// Si la estructura que se emplea en la
			// traza es una matriz
		{
			if (Conf.mostrarSalidaLigadaEntrada) {
				celdas = this.dameCeldasMatrices(this.ra, this.nivelCelda);
			} else {
				linea = 1;
				celdas = this.dameCeldasMatricesSalidaCronologica(this.ra,
						this.nivelCelda);
			}
		} else {
			celdas = new ArrayList<DefaultGraphCell>();
		}
	}

	/**
	 * Crea una nueva instancia del Contenedor.
	 * 
	 * @param nyp
	 *            Nombres y prefijos a aplicar.
	 * @param ra
	 *            Registro de activación correspondiente al contenedor.
	 */
	public ContenedorCronologica(NombresYPrefijos nyp, RegistroActivacion ra) {
		contadorMatrices = 0;

		calcularMaximaLongitudContenidoCelda(ra);

		int tipoEstrutura = Ventana.thisventana.getTraza().tipoEstructura();

		if (tipoEstrutura == 1)// Si la estructura que se emplea en la traza es
			// un array
		{
			if (Conf.mostrarSalidaLigadaEntrada) {
				celdas = this.dameCeldasArrays(ra, this.nivelCelda);
			} else {
				linea = 1;
				celdas = this.dameCeldasArraysSalidaCronologica(ra,
						this.nivelCelda);
			}
		} else if (tipoEstrutura == 2)// Si la estructura que se emplea en la
			// traza es una matriz
		{
			if (Conf.mostrarSalidaLigadaEntrada) {
				celdas = this.dameCeldasMatrices(ra, this.nivelCelda);
			} else {
				linea = 1;
				celdas = this.dameCeldasMatricesSalidaCronologica(ra,
						this.nivelCelda);
			}
		} else {
			celdas = new ArrayList<DefaultGraphCell>();
		}
	}

	/**
	 * Método para obtener las celdas del resto de parámetros de entrada en modo
	 * cronológico.
	 * 
	 * @param texto
	 *            Texto de entrada.
	 * @param r
	 *            Registro de activación correspondiente.
	 * 
	 * @return Celda del grafo
	 */
	private DefaultGraphCell celdaParametrosEnt(String texto,
			RegistroActivacion r) {
		DefaultGraphCell celda = new DefaultGraphCell(new String(texto));
		GraphConstants
		.setBackground(celda.getAttributes(), Conf.colorC3Entrada);
		GraphConstants.setGradientColor(celda.getAttributes(),
				Conf.colorC4Entrada);
		GraphConstants.setOpaque(celda.getAttributes(), r.entradaVisible());
		if (r.entradaVisible()) {
			this.marcoCelda(celda.getAttributes());
			GraphConstants.setForeground(celda.getAttributes(),
					Conf.colorFEntrada);
		} else {
			GraphConstants
			.setForeground(celda.getAttributes(), Conf.colorPanel);
		}
		GraphConstants.setFont(celda.getAttributes(), new Font("Arial",
				Font.BOLD, 16));
		GraphConstants.setDisconnectable(celda.getAttributes(), true);
		GraphConstants.setMoveable(celda.getAttributes(), false);
		GraphConstants.setSelectable(celda.getAttributes(), false);
		GraphConstants.setSize(celda.getAttributes(), new Dimension(
				this.pixelAnchoCaracter * (texto.length() + 2), this.altCelda));
		GraphConstants.setResize(celda.getAttributes(), false);
		GraphConstants.setBounds(
				celda.getAttributes(),
				new Rectangle(this.separacionAuxiliar, this.sepIni
						+ (linea - 1)
						* ((int) (GraphConstants.getSize(celda.getAttributes())
								.getHeight()) * 2), (int) (GraphConstants
										.getSize(celda.getAttributes()).getWidth()),
										(int) (GraphConstants.getSize(celda.getAttributes())
												.getHeight())));
		this.separacionAuxiliar += (int) (GraphConstants.getSize(celda
				.getAttributes()).getWidth()) + 10;
		return celda;
	}

	/**
	 * Método para obtener las celdas del resto de parámetros de entrada en modo
	 * NO cronológico.
	 * 
	 * @param texto
	 *            Texto de entrada.
	 * @param r
	 *            Registro de activación correspondiente.
	 * 
	 * @return Celda del grafo
	 */
	private DefaultGraphCell celdaParametrosEntNoCrono(String texto,
			RegistroActivacion r) {
		DefaultGraphCell celda = new DefaultGraphCell(new String(texto));
		GraphConstants
		.setBackground(celda.getAttributes(), Conf.colorC3Entrada);
		GraphConstants.setGradientColor(celda.getAttributes(),
				Conf.colorC4Entrada);
		GraphConstants.setOpaque(celda.getAttributes(), r.entradaVisible());
		if (r.entradaVisible()) {
			this.marcoCelda(celda.getAttributes());
			GraphConstants.setForeground(celda.getAttributes(),
					Conf.colorFEntrada);
		} else {
			GraphConstants
			.setForeground(celda.getAttributes(), Conf.colorPanel);
		}
		GraphConstants.setFont(celda.getAttributes(), new Font("Arial",
				Font.BOLD, 16));
		GraphConstants.setDisconnectable(celda.getAttributes(), true);
		GraphConstants.setMoveable(celda.getAttributes(), false);
		GraphConstants.setSelectable(celda.getAttributes(), false);
		GraphConstants.setSize(celda.getAttributes(), new Dimension(
				this.pixelAnchoCaracter * (texto.length() + 2), this.altCelda));
		GraphConstants.setResize(celda.getAttributes(), false);
		GraphConstants.setBounds(
				celda.getAttributes(),
				new Rectangle(this.separacionAuxiliar, this.sepIni
						+ (this.nivelCelda - 1)
						* ((int) (GraphConstants.getSize(celda.getAttributes())
								.getHeight()) * 3), (int) (GraphConstants
										.getSize(celda.getAttributes()).getWidth()),
										(int) (GraphConstants.getSize(celda.getAttributes())
												.getHeight())));
		this.separacionAuxiliar += (int) (GraphConstants.getSize(celda
				.getAttributes()).getWidth()) + 10;
		return celda;
	}

	/**
	 * Método para obtener las celdas del resto de parámetros de entrada en modo
	 * cronológico para matrices.
	 * 
	 * @param texto
	 *            Texto de entrada.
	 * @param r
	 *            Registro de activación correspondiente.
	 * @param dimensiones
	 *            Tamaño de las dimensiones de la matriz.
	 * 
	 * @return Celda del grafo
	 */
	private DefaultGraphCell celdaParametrosEntMatrices(String texto,
			RegistroActivacion r, int[] dimensiones) {
		DefaultGraphCell celda = new DefaultGraphCell(new String(texto));
		GraphConstants
		.setBackground(celda.getAttributes(), Conf.colorC3Entrada);
		GraphConstants.setGradientColor(celda.getAttributes(),
				Conf.colorC4Entrada);
		GraphConstants.setOpaque(celda.getAttributes(), r.entradaVisible());
		if (r.entradaVisible()) {
			this.marcoCelda(celda.getAttributes());
			GraphConstants.setForeground(celda.getAttributes(),
					Conf.colorFEntrada);
		} else {
			GraphConstants
			.setForeground(celda.getAttributes(), Conf.colorPanel);
		}
		GraphConstants.setFont(celda.getAttributes(), new Font("Arial",
				Font.BOLD, 16));
		GraphConstants.setDisconnectable(celda.getAttributes(), true);
		GraphConstants.setMoveable(celda.getAttributes(), false);
		GraphConstants.setSelectable(celda.getAttributes(), false);
		GraphConstants.setSize(celda.getAttributes(), new Dimension(
				this.pixelAnchoCaracter * (texto.length() + 2), this.altCelda));
		GraphConstants.setResize(celda.getAttributes(), false);
		GraphConstants.setBounds(
				celda.getAttributes(),
				new Rectangle(this.separacionAuxiliar, this.sepIni
						+ (contadorMatrices * dimensiones[1] * this.altCelda)
						+ (contadorMatrices * 30), (int) (GraphConstants
						.getSize(celda.getAttributes()).getWidth()),
						(int) (GraphConstants.getSize(celda.getAttributes())
								.getHeight())));
		this.separacionAuxiliar += (int) (GraphConstants.getSize(celda
				.getAttributes()).getWidth()) + 10;
		return celda;
	}

	/**
	 * Método para obtener las celdas del resto de parámetros de entrada en modo
	 * NO cronológico para matrices.
	 * 
	 * @param texto
	 *            Texto de entrada.
	 * @param r
	 *            Registro de activación correspondiente.
	 * @param dimensiones
	 *            Tamaño de las dimensiones de la matriz.
	 * 
	 * @return Celda del grafo
	 */
	private DefaultGraphCell celdaParametrosEntNoCronoMatrices(String texto,
			RegistroActivacion r, int[] dimensiones) {
		DefaultGraphCell celda = new DefaultGraphCell(new String(texto));
		GraphConstants
		.setBackground(celda.getAttributes(), Conf.colorC3Entrada);
		GraphConstants.setGradientColor(celda.getAttributes(),
				Conf.colorC4Entrada);
		GraphConstants.setOpaque(celda.getAttributes(), r.entradaVisible());
		if (r.entradaVisible()) {
			this.marcoCelda(celda.getAttributes());
			GraphConstants.setForeground(celda.getAttributes(),
					Conf.colorFEntrada);
		} else {
			GraphConstants
			.setForeground(celda.getAttributes(), Conf.colorPanel);
		}
		GraphConstants.setFont(celda.getAttributes(), new Font("Arial",
				Font.BOLD, 16));
		GraphConstants.setDisconnectable(celda.getAttributes(), true);
		GraphConstants.setMoveable(celda.getAttributes(), false);
		GraphConstants.setSelectable(celda.getAttributes(), false);
		GraphConstants.setSize(celda.getAttributes(), new Dimension(
				this.pixelAnchoCaracter * (texto.length() + 2), this.altCelda));
		GraphConstants.setResize(celda.getAttributes(), false);
		GraphConstants.setBounds(
				celda.getAttributes(),
				new Rectangle(this.separacionAuxiliar, this.sepIni
						+ (contadorMatrices * dimensiones[1] * this.altCelda)
						+ (contadorMatrices * 30), (int) (GraphConstants
						.getSize(celda.getAttributes()).getWidth()),
						(int) (GraphConstants.getSize(celda.getAttributes())
								.getHeight())));
		this.separacionAuxiliar += (int) (GraphConstants.getSize(celda
				.getAttributes()).getWidth()) + 10;
		return celda;
	}

	/**
	 * Método para obtener las celdas del resto de parámetros de salida en modo
	 * cronológico.
	 * 
	 * @param texto
	 *            Texto de entrada.
	 * @param r
	 *            Registro de activación correspondiente.
	 * 
	 * @return Celda del grafo
	 */
	private DefaultGraphCell celdaParametrosSal(String texto,
			RegistroActivacion r) {
		DefaultGraphCell celda = new DefaultGraphCell(new String(texto));
		GraphConstants.setBackground(celda.getAttributes(), Conf.colorC3Salida);
		GraphConstants.setGradientColor(celda.getAttributes(),
				Conf.colorC4Salida);
		GraphConstants.setOpaque(celda.getAttributes(), r.salidaVisible());
		if (r.salidaVisible()) {
			this.marcoCelda(celda.getAttributes());
			GraphConstants.setForeground(celda.getAttributes(),
					Conf.colorFSalida);
		} else {
			GraphConstants
			.setForeground(celda.getAttributes(), Conf.colorPanel);
		}
		GraphConstants.setFont(celda.getAttributes(), new Font("Arial",
				Font.BOLD, 16));
		GraphConstants.setDisconnectable(celda.getAttributes(), true);
		GraphConstants.setMoveable(celda.getAttributes(), false);
		GraphConstants.setSelectable(celda.getAttributes(), false);
		GraphConstants.setSize(celda.getAttributes(), new Dimension(
				this.pixelAnchoCaracter * (texto.length() + 2), this.altCelda));
		GraphConstants.setResize(celda.getAttributes(), false);
		GraphConstants.setBounds(
				celda.getAttributes(),
				new Rectangle(this.separacionAuxiliar, this.sepIni
						+ (linea - 1)
						* ((int) (GraphConstants.getSize(celda.getAttributes())
								.getHeight()) * 2), (int) (GraphConstants
										.getSize(celda.getAttributes()).getWidth()),
										(int) (GraphConstants.getSize(celda.getAttributes())
												.getHeight())));
		this.separacionAuxiliar += (int) (GraphConstants.getSize(celda
				.getAttributes()).getWidth()) + 10;
		return celda;
	}

	/**
	 * Método para obtener las celdas del resto de parámetros de salida en modo
	 * NO cronológico.
	 * 
	 * @param texto
	 *            Texto de entrada.
	 * @param r
	 *            Registro de activación correspondiente.
	 * 
	 * @return Celda del grafo
	 */
	private DefaultGraphCell celdaParametrosSalNoCrono(String texto,
			RegistroActivacion r) {
		DefaultGraphCell celda = new DefaultGraphCell(new String(texto));
		GraphConstants.setBackground(celda.getAttributes(), Conf.colorC3Salida);
		GraphConstants.setGradientColor(celda.getAttributes(),
				Conf.colorC4Salida);
		GraphConstants.setOpaque(celda.getAttributes(), r.salidaVisible());
		if (r.salidaVisible()) {
			this.marcoCelda(celda.getAttributes());
			GraphConstants.setForeground(celda.getAttributes(),
					Conf.colorFSalida);
		} else {
			GraphConstants
			.setForeground(celda.getAttributes(), Conf.colorPanel);
		}
		GraphConstants.setFont(celda.getAttributes(), new Font("Arial",
				Font.BOLD, 16));
		GraphConstants.setDisconnectable(celda.getAttributes(), true);
		GraphConstants.setMoveable(celda.getAttributes(), false);
		GraphConstants.setSelectable(celda.getAttributes(), false);
		GraphConstants.setSize(celda.getAttributes(), new Dimension(
				this.pixelAnchoCaracter * (texto.length() + 2), this.altCelda));
		GraphConstants.setResize(celda.getAttributes(), false);
		GraphConstants
		.setBounds(
				celda.getAttributes(),
				new Rectangle(
						this.separacionAuxiliar,
								this.sepIni
						+ ((this.nivelCelda - 1) * ((int) (GraphConstants
								.getSize(celda.getAttributes())
								.getHeight()) * 3))
								+ (int) (GraphConstants.getSize(celda
										.getAttributes()).getHeight()),
								(int) (GraphConstants.getSize(celda
												.getAttributes()).getWidth()),
								(int) (GraphConstants.getSize(celda
														.getAttributes()).getHeight())));
		this.separacionAuxiliar += (int) (GraphConstants.getSize(celda
				.getAttributes()).getWidth()) + 10;
		return celda;
	}

	/**
	 * Método para obtener las celdas del resto de parámetros de salida en modo
	 * cronológico para matrices.
	 * 
	 * @param texto
	 *            Texto de entrada.
	 * @param r
	 *            Registro de activación correspondiente.
	 * @param dimensiones
	 *            Tamaño de las dimensiones de la matriz.
	 * 
	 * @return Celda del grafo
	 */
	private DefaultGraphCell celdaParametrosSalMatrices(String texto,
			RegistroActivacion r, int[] dimensiones) {
		DefaultGraphCell celda = new DefaultGraphCell(new String(texto));
		GraphConstants.setBackground(celda.getAttributes(), Conf.colorC3Salida);
		GraphConstants.setGradientColor(celda.getAttributes(),
				Conf.colorC4Salida);
		GraphConstants.setOpaque(celda.getAttributes(), r.salidaVisible());
		if (r.salidaVisible()) {
			this.marcoCelda(celda.getAttributes());
			GraphConstants.setForeground(celda.getAttributes(),
					Conf.colorFSalida);
		} else {
			GraphConstants
			.setForeground(celda.getAttributes(), Conf.colorPanel);
		}
		GraphConstants.setFont(celda.getAttributes(), new Font("Arial",
				Font.BOLD, 16));
		GraphConstants.setDisconnectable(celda.getAttributes(), true);
		GraphConstants.setMoveable(celda.getAttributes(), false);
		GraphConstants.setSelectable(celda.getAttributes(), false);
		GraphConstants.setSize(celda.getAttributes(), new Dimension(
				this.pixelAnchoCaracter * (texto.length() + 2), this.altCelda));
		GraphConstants.setResize(celda.getAttributes(), false);
		GraphConstants.setBounds(
				celda.getAttributes(),
				new Rectangle(this.separacionAuxiliar, this.sepIni
						+ (contadorMatrices * dimensiones[1] * this.altCelda)
						+ (contadorMatrices * 30), (int) (GraphConstants
						.getSize(celda.getAttributes()).getWidth()),
						(int) (GraphConstants.getSize(celda.getAttributes())
								.getHeight())));
		this.separacionAuxiliar += (int) (GraphConstants.getSize(celda
				.getAttributes()).getWidth()) + 10;
		return celda;
	}

	/**
	 * Método para obtener las celdas del resto de parámetros de salida en modo
	 * NO cronológico para matrices.
	 * 
	 * @param texto
	 *            Texto de entrada.
	 * @param r
	 *            Registro de activación correspondiente.
	 * @param dimensiones
	 *            Tamaño de las dimensiones de la matriz.
	 * 
	 * @return Celda del grafo
	 */
	private DefaultGraphCell celdaParametrosSalNoCronoMatrices(String texto,
			RegistroActivacion r, int[] dimensiones) {
		DefaultGraphCell celda = new DefaultGraphCell(new String(texto));
		GraphConstants.setBackground(celda.getAttributes(), Conf.colorC3Salida);
		GraphConstants.setGradientColor(celda.getAttributes(),
				Conf.colorC4Salida);
		GraphConstants.setOpaque(celda.getAttributes(), r.salidaVisible());
		if (r.salidaVisible()) {
			this.marcoCelda(celda.getAttributes());
			GraphConstants.setForeground(celda.getAttributes(),
					Conf.colorFSalida);
		} else {
			GraphConstants
			.setForeground(celda.getAttributes(), Conf.colorPanel);
		}
		GraphConstants.setFont(celda.getAttributes(), new Font("Arial",
				Font.BOLD, 16));
		GraphConstants.setDisconnectable(celda.getAttributes(), true);
		GraphConstants.setMoveable(celda.getAttributes(), false);
		GraphConstants.setSelectable(celda.getAttributes(), false);
		GraphConstants.setSize(celda.getAttributes(), new Dimension(
				this.pixelAnchoCaracter * (texto.length() + 2), this.altCelda));
		GraphConstants.setResize(celda.getAttributes(), false);
		GraphConstants.setBounds(
				celda.getAttributes(),
				new Rectangle(this.separacionAuxiliar, this.sepIni
						+ (contadorMatrices * dimensiones[1] * this.altCelda)
						+ (contadorMatrices * 30), (int) (GraphConstants
						.getSize(celda.getAttributes()).getWidth()),
						(int) (GraphConstants.getSize(celda.getAttributes())
								.getHeight())));
		this.separacionAuxiliar += (int) (GraphConstants.getSize(celda
				.getAttributes()).getWidth()) + 10;
		return celda;
	}

	/**
	 * Dibujo de arrays (con la salida ligada a la entrada)
	 * 
	 * @param r
	 *            Registro de Activación.
	 * @param nc
	 *            Nivel de la celda.
	 * 
	 * @return Array de celdas.
	 */
	private ArrayList<DefaultGraphCell> dameCeldasArrays(RegistroActivacion r,
			int nc) {
		Estructura eE = null;
		Estructura eS = null;
		ArrayList<DefaultGraphCell> c = new ArrayList<DefaultGraphCell>(0);

		int indices[] = r.getEntrada().getIndices();

		eE = new Estructura(r.getEntrada().getEstructura());
		eS = new Estructura(r.getSalida().getEstructura());

		if (r != null && r.esDYV()) {
			// Celdas de entrada de este nodo
			int contador = 0;

			if (indices == null || indices.length == 0) {
				indices = new int[2];
				indices[0] = 0;
				indices[1] = eE.dimensiones()[0] - 1;
			}

			for (int i = 0; i < eE.dimensiones()[0]; i++) {
				String texto;
				String clase = eE.getTipo().getClass().getName();
				if (clase.contains("Integer")) {
					texto = this.esp + eE.posicArrayInt(i) + this.esp;
				} else if (clase.contains("Long")) {
					texto = this.esp + eE.posicArrayLong(i) + this.esp;
				} else if (clase.contains("Double")) {
					texto = this.esp + eE.posicArrayDouble(i) + this.esp;
				} else if (clase.contains("String")) {
					texto = this.esp + eE.posicArrayString(i) + this.esp;
				} else if (clase.contains("Character")) {
					texto = this.esp + eE.posicArrayChar(i) + this.esp;
				} else if (clase.contains("Boolean")) {
					texto = this.esp + eE.posicArrayBool(i) + this.esp;
				} else {
					texto = this.esp + "ANoDef" + this.esp;
				}

				DefaultGraphCell celda = new DefaultGraphCell(new String(texto));

				if (i >= indices[0] && i <= indices[1]) {
					GraphConstants.setBackground(celda.getAttributes(),
							Conf.colorC1Entrada);
					GraphConstants.setGradientColor(celda.getAttributes(),
							Conf.colorC2Entrada);
					GraphConstants.setOpaque(celda.getAttributes(),
							r.entradaVisible());
					if (r.entradaVisible()) {
						this.marcoCelda(celda.getAttributes());
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorFEntrada);
					} else {
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorPanel);
					}
				} else {
					GraphConstants.setBackground(celda.getAttributes(),
							Conf.colorC1AEntrada);
					GraphConstants.setGradientColor(celda.getAttributes(),
							Conf.colorC2AEntrada);
					GraphConstants.setOpaque(celda.getAttributes(),
							r.entradaVisible()
							&& Conf.mostrarEstructuraCompleta);
					if (r.entradaVisible() && Conf.mostrarEstructuraCompleta) {
						this.marcoCelda(celda.getAttributes());
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorFEntrada);
					} else {
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorPanel);
					}
				}

				GraphConstants.setFont(celda.getAttributes(), new Font("Arial",
						Font.BOLD, 20));
				GraphConstants.setDisconnectable(celda.getAttributes(), false);
				GraphConstants.setMoveable(celda.getAttributes(), false);
				GraphConstants.setSelectable(celda.getAttributes(), false);
				GraphConstants.setSize(celda.getAttributes(), new Dimension(
						this.pixelAnchoCaracter
						* (maximaLongitudContenidoCelda + 4),
						this.altCelda));

				GraphConstants.setResize(celda.getAttributes(), false);
				GraphConstants
				.setBounds(
						celda.getAttributes(),
						new Rectangle(
								this.sepIni
								+ (contador)
								* (int) (GraphConstants.getSize(celda
										.getAttributes())
										.getWidth()),
										this.sepIni
										+ (this.nivelCelda - 1)
										* ((int) (GraphConstants.getSize(celda
												.getAttributes())
												.getHeight()) * 3),
										(int) (GraphConstants.getSize(celda
														.getAttributes()).getWidth()),
										(int) (GraphConstants.getSize(celda
																.getAttributes()).getHeight())));
				c.add(celda);
				this.separacionAuxiliar = (this.sepIni + (contador)
						* (int) (GraphConstants.getSize(celda.getAttributes())
								.getWidth()))
								+ (int) (GraphConstants.getSize(celda.getAttributes())
										.getWidth()) + 20;
				contador++;
			}
			// Mostramos resto de parámetros
			if (!Conf.soloEstructuraDYVcrono) {
				String entAux[] = r.getNombreParametros();
				String ent[] = ServiciosString.obtenerPrefijos(entAux);
				String ent2[] = r.getEntradasString();
				for (int i = 0; (i < ent.length) && (i < ent2.length); i++) {
					if (i != r.getEntrada().getIndiceDeEstructura()) {
						c.add(this.celdaParametrosEntNoCrono(ent[i] + "="
								+ ent2[i], r));
					}
				}
			}

			// Celdas de salida de este nodo
			contador = 0;
			for (int i = 0; i < eS.dimensiones()[0]; i++) {
				String texto;
				String clase = eS.getTipo().getClass().getName();
				if (clase.contains("Integer")) {
					texto = this.esp + eS.posicArrayInt(i) + this.esp;
				} else if (clase.contains("Long")) {
					texto = this.esp + eS.posicArrayLong(i) + this.esp;
				} else if (clase.contains("Double")) {
					texto = this.esp + eS.posicArrayDouble(i) + this.esp;
				} else if (clase.contains("String")) {
					texto = this.esp + eS.posicArrayString(i) + this.esp;
				} else if (clase.contains("Character")) {
					texto = this.esp + eS.posicArrayChar(i) + this.esp;
				} else if (clase.contains("Boolean")) {
					texto = this.esp + eS.posicArrayBool(i) + this.esp;
				} else {
					texto = this.esp + "ANoDef" + this.esp;
				}

				DefaultGraphCell celda = new DefaultGraphCell(new String(texto));

				if (i >= indices[0] && i <= indices[1]) {
					GraphConstants.setBackground(celda.getAttributes(),
							Conf.colorC1Salida);
					GraphConstants.setGradientColor(celda.getAttributes(),
							Conf.colorC2Salida);
					GraphConstants.setOpaque(celda.getAttributes(),
							r.salidaVisible());
					if (r.salidaVisible()) {
						this.marcoCelda(celda.getAttributes());
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorFSalida);
					} else {
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorPanel);
					}
				} else {
					GraphConstants.setBackground(celda.getAttributes(),
							Conf.colorC1ASalida);
					GraphConstants.setGradientColor(celda.getAttributes(),
							Conf.colorC2ASalida);
					GraphConstants
					.setOpaque(celda.getAttributes(), r.salidaVisible()
							&& Conf.mostrarEstructuraCompleta);
					if (r.salidaVisible() && Conf.mostrarEstructuraCompleta) {
						this.marcoCelda(celda.getAttributes());
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorFSalida);
					} else {
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorPanel);
					}
				}

				GraphConstants.setFont(celda.getAttributes(), new Font("Arial",
						Font.BOLD, 20));
				GraphConstants.setDisconnectable(celda.getAttributes(), false);
				GraphConstants.setMoveable(celda.getAttributes(), false);
				GraphConstants.setSelectable(celda.getAttributes(), false);
				GraphConstants.setSize(celda.getAttributes(), new Dimension(
						this.pixelAnchoCaracter
						* (maximaLongitudContenidoCelda + 4),
						this.altCelda));

				GraphConstants.setResize(celda.getAttributes(), false);
				GraphConstants
				.setBounds(
						celda.getAttributes(),
						new Rectangle(
								this.sepIni
								+ (contador)
								* (int) (GraphConstants.getSize(celda
										.getAttributes())
										.getWidth()),
										this.sepIni
										+ ((this.nivelCelda - 1) * ((int) (GraphConstants.getSize(celda
												.getAttributes())
												.getHeight()) * 3))
												+ (int) (GraphConstants.getSize(celda
														.getAttributes())
														.getHeight()),
										(int) (GraphConstants.getSize(celda
																.getAttributes()).getWidth()),
										(int) (GraphConstants.getSize(celda
																		.getAttributes()).getHeight())));

				c.add(celda);
				this.separacionAuxiliar = (this.sepIni + (contador)
						* (int) (GraphConstants.getSize(celda.getAttributes())
								.getWidth()))
								+ (int) (GraphConstants.getSize(celda.getAttributes())
										.getWidth()) + 20;
				contador++;
			}
			// Mostramos resto de parámetros
			if (!Conf.soloEstructuraDYVcrono) {
				String entAux[] = r.getNombreParametros();
				String ent[] = ServiciosString.obtenerPrefijos(entAux);
				String[] ent2 = r.getSalidasString();
				for (int i = 0; (i < ent.length) && (i < ent2.length); i++) {
					if (i != r.getEntrada().getIndiceDeEstructura()) {
						c.add(this.celdaParametrosSalNoCrono(ent[i] + "="
								+ ent2[i], r));
					}
				}
			}

			this.nivelCelda++;
		}

		for (int i = 0; i < r.numHijos(); i++) {
			if ((r.getHijo(i).inhibido() && Conf.mostrarArbolSalto)
					|| (!r.getHijo(i).inhibido())) {
				c.addAll(this.dameCeldasArrays(r.getHijo(i), this.nivelCelda));
			}
		}

		return c;
	}

	/**
	 * Dibujo de arrays (con la salida en modo cronológico independiente)
	 * 
	 * @param r
	 *            Registro de Activación.
	 * @param nc
	 *            Nivel de la celda.
	 * 
	 * @return Array de celdas.
	 */
	private ArrayList<DefaultGraphCell> dameCeldasArraysSalidaCronologica(
			RegistroActivacion r, int nc) {
		Estructura eE = null;
		Estructura eS = null;
		ArrayList<DefaultGraphCell> c = new ArrayList<DefaultGraphCell>(0);

		int indices[] = r.getEntrada().getIndices();

		eE = new Estructura(r.getEntrada().getEstructura());

		if (r != null
				&& r.esDYV()
				&& eE != null
				&& r.entradaVisible()
				&& (Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {

			// Celdas de entrada de este nodo
			int contador = 0;

			if (indices == null || indices.length == 0) {
				indices = new int[2];
				indices[0] = 0;
				indices[1] = eE.dimensiones()[0] - 1;
			}
			if (Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar
					|| Conf.VISUALIZAR_TODO == Conf.elementosVisualizar) {
				for (int i = 0; i < eE.dimensiones()[0]; i++) {
					String texto;
					String clase = eE.getTipo().getClass().getName();
					if (clase.contains("Integer")) {
						texto = this.esp + eE.posicArrayInt(i) + this.esp;
					} else if (clase.contains("Long")) {
						texto = this.esp + eE.posicArrayLong(i) + this.esp;
					} else if (clase.contains("Double")) {
						texto = this.esp + eE.posicArrayDouble(i) + this.esp;
					} else if (clase.contains("String")) {
						texto = this.esp + eE.posicArrayString(i) + this.esp;
					} else if (clase.contains("Character")) {
						texto = this.esp + eE.posicArrayChar(i) + this.esp;
					} else if (clase.contains("Boolean")) {
						texto = this.esp + eE.posicArrayBool(i) + this.esp;
					} else {
						texto = this.esp + "ANoDef" + this.esp;
					}

					DefaultGraphCell celda = new DefaultGraphCell(new String(
							texto));
					if (i >= indices[0] && i <= indices[1]) {
						GraphConstants.setBackground(celda.getAttributes(),
								Conf.colorC1Entrada);
						GraphConstants.setGradientColor(celda.getAttributes(),
								Conf.colorC2Entrada);
						GraphConstants.setOpaque(celda.getAttributes(),
								r.entradaVisible());
						if (r.entradaVisible()) {
							this.marcoCelda(celda.getAttributes());
							GraphConstants.setForeground(celda.getAttributes(),
									Conf.colorFEntrada);
						} else {
							GraphConstants.setForeground(celda.getAttributes(),
									Conf.colorPanel);
						}
					} else {
						GraphConstants.setBackground(celda.getAttributes(),
								Conf.colorC1AEntrada);
						GraphConstants.setGradientColor(celda.getAttributes(),
								Conf.colorC2AEntrada);
						GraphConstants.setOpaque(celda.getAttributes(),
								r.entradaVisible()
								&& Conf.mostrarEstructuraCompleta);
						if (r.entradaVisible()
								&& Conf.mostrarEstructuraCompleta) {
							this.marcoCelda(celda.getAttributes());
							GraphConstants.setForeground(celda.getAttributes(),
									Conf.colorFEntrada);
						} else {
							GraphConstants.setForeground(celda.getAttributes(),
									Conf.colorPanel);
						}
					}

					GraphConstants.setFont(celda.getAttributes(), new Font(
							"Arial", Font.BOLD, 20));
					GraphConstants.setDisconnectable(celda.getAttributes(),
							false);
					GraphConstants.setMoveable(celda.getAttributes(), false);
					GraphConstants.setSelectable(celda.getAttributes(), false);
					GraphConstants.setSize(celda.getAttributes(),
							new Dimension(this.pixelAnchoCaracter
									* (maximaLongitudContenidoCelda + 4),
									this.altCelda));

					GraphConstants.setResize(celda.getAttributes(), false);
					GraphConstants.setBounds(
							celda.getAttributes(),
							new Rectangle(this.sepIni
									+ (contador)
									* (int) (GraphConstants.getSize(celda
											.getAttributes()).getWidth()),
											this.sepIni
											+ (linea - 1)
											* ((int) (GraphConstants
													.getSize(celda
															.getAttributes())
															.getHeight()) * 2),
															(int) (GraphConstants.getSize(celda
																	.getAttributes()).getWidth()),
																	(int) (GraphConstants.getSize(celda
																			.getAttributes()).getHeight())));
					c.add(celda);

					this.separacionAuxiliar = (this.sepIni + (contador)
							* (int) (GraphConstants.getSize(celda
									.getAttributes()).getWidth()))
									+ (int) (GraphConstants.getSize(celda
											.getAttributes()).getWidth()) + 20;
					contador++;
				}
				// Mostramos resto de parámetros
				if (!Conf.soloEstructuraDYVcrono) {
					String entAux[] = r.getNombreParametros();
					String ent[] = ServiciosString.obtenerPrefijos(entAux);
					String ent2[] = r.getEntradasString();
					for (int i = 0; (i < ent.length) && (i < ent2.length); i++) {
						if (i != r.getEntrada().getIndiceDeEstructura()) {
							c.add(this.celdaParametrosEnt(ent[i] + "="
									+ ent2[i], r));
						}
					}
				}
				linea++;
			}
		}

		for (int i = 0; i < r.numHijos(); i++) {
			if ((r.getHijo(i).inhibido() && Conf.mostrarArbolSalto)
					|| (!r.getHijo(i).inhibido())) {
				c.addAll(this.dameCeldasArraysSalidaCronologica(r.getHijo(i),
						++this.nivelCelda));
			}
		}

		if (r != null
				&& r.esDYV()
				&& (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar || (Conf.VISUALIZAR_TODO == Conf.elementosVisualizar))) {
			// Celdas de salida de este nodo
			eS = new Estructura(r.getSalida().getEstructura());
			int contador = 0;
			for (int i = 0; i < eS.dimensiones()[0]; i++) {
				String texto;

				String clase = eS.getTipo().getClass().getName();
				if (clase.contains("Integer")) {
					texto = this.esp + eS.posicArrayInt(i) + this.esp;
				} else if (clase.contains("Long")) {
					texto = this.esp + eS.posicArrayLong(i) + this.esp;
				} else if (clase.contains("Double")) {
					texto = this.esp + eS.posicArrayDouble(i) + this.esp;
				} else if (clase.contains("String")) {
					texto = this.esp + eS.posicArrayString(i) + this.esp;
				} else if (clase.contains("Character")) {
					texto = this.esp + eS.posicArrayChar(i) + this.esp;
				} else if (clase.contains("Boolean")) {
					texto = this.esp + eS.posicArrayBool(i) + this.esp;
				} else {
					texto = this.esp + "ANoDef" + this.esp;
				}

				DefaultGraphCell celda = new DefaultGraphCell(new String(texto));

				if (indices.length > 0 && i >= indices[0] && i <= indices[1]) {
					GraphConstants.setBackground(celda.getAttributes(),
							Conf.colorC1Salida);
					GraphConstants.setGradientColor(celda.getAttributes(),
							Conf.colorC2Salida);
					GraphConstants.setOpaque(celda.getAttributes(),
							r.salidaVisible());
					if (r.salidaVisible()) {
						this.marcoCelda(celda.getAttributes());
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorFSalida);
					} else {
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorPanel);
					}
				} else {
					GraphConstants.setBackground(celda.getAttributes(),
							Conf.colorC1ASalida);
					GraphConstants.setGradientColor(celda.getAttributes(),
							Conf.colorC2ASalida);
					GraphConstants
					.setOpaque(celda.getAttributes(), r.salidaVisible()
							&& Conf.mostrarEstructuraCompleta);
					if (r.salidaVisible() && Conf.mostrarEstructuraCompleta) {
						this.marcoCelda(celda.getAttributes());
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorFSalida);
					} else {
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorPanel);
					}
				}

				GraphConstants.setFont(celda.getAttributes(), new Font("Arial",
						Font.BOLD, 20));
				GraphConstants.setDisconnectable(celda.getAttributes(), false);
				GraphConstants.setMoveable(celda.getAttributes(), false);
				GraphConstants.setSelectable(celda.getAttributes(), false);
				GraphConstants.setSize(celda.getAttributes(), new Dimension(
						this.pixelAnchoCaracter
						* (maximaLongitudContenidoCelda + 4),
						this.altCelda));

				GraphConstants.setResize(celda.getAttributes(), false);
				GraphConstants.setBounds(
						celda.getAttributes(),
						new Rectangle(this.sepIni
								+ (contador)
								* (int) (GraphConstants.getSize(celda
										.getAttributes()).getWidth()),
										this.sepIni
										+ ((linea - 1) * ((int) (GraphConstants
												.getSize(celda.getAttributes())
												.getHeight()) * 2)),
												(int) (GraphConstants.getSize(celda
														.getAttributes()).getWidth()),
														(int) (GraphConstants.getSize(celda
																.getAttributes()).getHeight())));

				c.add(celda);
				this.separacionAuxiliar = (this.sepIni + (contador)
						* (int) (GraphConstants.getSize(celda.getAttributes())
								.getWidth()))
								+ (int) (GraphConstants.getSize(celda.getAttributes())
										.getWidth()) + 20;
				contador++;
			}
			// Mostramos resto de parámetros
			if (!Conf.soloEstructuraDYVcrono) {
				String entAux[] = r.getNombreParametros();
				String ent[] = ServiciosString.obtenerPrefijos(entAux);
				String ent2[] = r.getSalidasString();
				for (int i = 0; (i < ent.length) && (i < ent2.length); i++) {
					if (i != r.getEntrada().getIndiceDeEstructura()) {
						c.add(this
								.celdaParametrosSal(ent[i] + "=" + ent2[i], r));
					}
				}
			}
			linea++;
		}

		return c;
	}

	/**
	 * Dibujo de matrices salida NO cronológica.
	 * 
	 * @param r
	 *            Registro de Activación.
	 * @param nc
	 *            Nivel de la celda.
	 * 
	 * @return Array de celdas.
	 */
	private ArrayList<DefaultGraphCell> dameCeldasMatrices(
			RegistroActivacion r, int nc) {
		Estructura eE = null;
		Estructura eS = null;
		ArrayList<DefaultGraphCell> c = new ArrayList<DefaultGraphCell>(0);

		int indices[] = r.getEntrada().getIndices();

		eE = new Estructura(r.getEntrada().getEstructura());
		eS = new Estructura(r.getSalida().getEstructura());

		int dimensiones[] = eE.dimensiones();
		// Celdas de entrada de este nodo

		if (r != null && r.esDYV()) {

			if (indices == null || indices.length == 0) {
				indices = new int[4];
				indices[0] = 0;
				indices[1] = eE.dimensiones()[0] - 1;
				indices[2] = 0;
				indices[3] = eE.dimensiones()[1] - 1;
			}

			for (int i = 0; i < dimensiones[0]; i++) {
				int contador = 0;
				for (int j = 0; j < dimensiones[1]; j++) {
					String texto;
					String clase = eE.getTipo().getClass().getName();
					if (clase.contains("Integer")) {
						texto = this.esp + eE.posicMatrizInt(i, j) + this.esp;
					} else if (clase.contains("Long")) {
						texto = this.esp + eE.posicMatrizLong(i, j) + this.esp;
					} else if (clase.contains("Double")) {
						texto = this.esp + eE.posicMatrizDouble(i, j)
								+ this.esp;
					} else if (clase.contains("String")) {
						texto = this.esp + eE.posicMatrizString(i, j)
								+ this.esp;
					} else if (clase.contains("Character")) {
						texto = this.esp + eE.posicMatrizChar(i, j) + this.esp;
					} else if (clase.contains("Boolean")) {
						texto = this.esp + eE.posicMatrizBool(i, j) + this.esp;
					} else {
						texto = this.esp + "ANoDef" + this.esp;
					}

					DefaultGraphCell celda = new DefaultGraphCell(new String(
							texto));

					GraphConstants.setFont(celda.getAttributes(), new Font(
							"Arial", Font.BOLD, 20));
					GraphConstants.setDisconnectable(celda.getAttributes(),
							false);
					GraphConstants.setMoveable(celda.getAttributes(), false);
					GraphConstants.setSelectable(celda.getAttributes(), false);
					GraphConstants.setSize(celda.getAttributes(),
							new Dimension(this.pixelAnchoCaracter
									* (maximaLongitudContenidoCelda + 4),
									this.altCelda));

					if (r.entradaVisible()) {
						this.marcoCelda(celda.getAttributes());
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorFEntrada);
						if (i >= indices[0] && i <= indices[1]
								&& j >= indices[2] && j <= indices[3]) {
							GraphConstants.setBackground(celda.getAttributes(),
									Conf.colorC1Entrada);
							GraphConstants.setGradientColor(
									celda.getAttributes(), Conf.colorC2Entrada);
							GraphConstants.setOpaque(celda.getAttributes(),
									r.entradaVisible());
						} else {
							GraphConstants.setBackground(celda.getAttributes(),
									Conf.colorC1AEntrada);
							GraphConstants
							.setGradientColor(celda.getAttributes(),
									Conf.colorC2AEntrada);
							GraphConstants.setOpaque(celda.getAttributes(),
									r.entradaVisible()
									&& Conf.mostrarEstructuraCompleta);
						}
					} else {
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorPanel);
					}

					GraphConstants.setResize(celda.getAttributes(), false);
					GraphConstants
					.setBounds(
							celda.getAttributes(),
							new Rectangle(
									this.sepIni
									+ j
									* (int) (GraphConstants.getSize(celda
											.getAttributes())
											.getWidth()),
											this.sepIni
											+ (contadorMatrices
													* dimensiones[1] * this.altCelda)
													+ (contadorMatrices * 30)
													+ (i * this.altCelda),
											(int) (GraphConstants.getSize(celda
															.getAttributes())
															.getWidth()),
											(int) (GraphConstants.getSize(celda
																	.getAttributes())
																	.getHeight())));

					c.add(celda);
					this.separacionAuxiliar = (this.sepIni + (contador)
							* (int) (GraphConstants.getSize(celda
									.getAttributes()).getWidth()))
									+ (int) (GraphConstants.getSize(celda
											.getAttributes()).getWidth()) + 20;
					contador++;
				}
				// Mostramos resto de parámetros
				if (!Conf.soloEstructuraDYVcrono) {
					if (i == 0) {
						String entAux[] = r.getNombreParametros();
						String ent[] = ServiciosString.obtenerPrefijos(entAux);
						String ent2[] = r.getEntradasString();
						for (int x = 0; (x < ent.length) && (x < ent2.length); x++) {
							if (x != r.getEntrada().getIndiceDeEstructura()) {
								c.add(this.celdaParametrosEntNoCronoMatrices(
										ent[x] + "=" + ent2[x], r, dimensiones));
							}
						}
						this.separacionAuxiliarMatricesNoCrono2 = this.separacionAuxiliar;
					}
				}
			}

			// Celdas de salida de este nodo
			for (int i = 0; i < dimensiones[0]; i++) {
				this.separacionAuxiliarMatricesNoCrono = this.separacionAuxiliarMatricesNoCrono2;
				for (int j = 0; j < dimensiones[1]; j++) {
					String texto;
					String clase = eS.getTipo().getClass().getName();
					if (clase.contains("Integer")) {
						texto = this.esp + eS.posicMatrizInt(i, j) + this.esp;
					} else if (clase.contains("Long")) {
						texto = this.esp + eS.posicMatrizLong(i, j) + this.esp;
					} else if (clase.contains("Double")) {
						texto = this.esp + eS.posicMatrizDouble(i, j)
								+ this.esp;
					} else if (clase.contains("String")) {
						texto = this.esp + eS.posicMatrizString(i, j)
								+ this.esp;
					} else if (clase.contains("Character")) {
						texto = this.esp + eS.posicMatrizChar(i, j) + this.esp;
					} else if (clase.contains("Boolean")) {
						texto = this.esp + eS.posicMatrizBool(i, j) + this.esp;
					} else {
						texto = this.esp + "ANoDef" + this.esp;
					}

					DefaultGraphCell celda = new DefaultGraphCell(new String(
							texto));

					GraphConstants.setFont(celda.getAttributes(), new Font(
							"Arial", Font.BOLD, 20));
					GraphConstants.setDisconnectable(celda.getAttributes(),
							false);
					GraphConstants.setMoveable(celda.getAttributes(), false);
					GraphConstants.setSelectable(celda.getAttributes(), false);
					GraphConstants.setSize(celda.getAttributes(),
							new Dimension(this.pixelAnchoCaracter
									* (maximaLongitudContenidoCelda + 4),
									this.altCelda));

					if (r.salidaVisible()) {
						this.marcoCelda(celda.getAttributes());
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorFSalida);
						if (i >= indices[0] && i <= indices[1]
								&& j >= indices[2] && j <= indices[3]) {
							GraphConstants.setBackground(celda.getAttributes(),
									Conf.colorC1Salida);
							GraphConstants.setGradientColor(
									celda.getAttributes(), Conf.colorC2Salida);
							GraphConstants.setOpaque(celda.getAttributes(),
									r.salidaVisible());
						} else {
							GraphConstants.setBackground(celda.getAttributes(),
									Conf.colorC1ASalida);
							GraphConstants.setGradientColor(
									celda.getAttributes(), Conf.colorC2ASalida);
							GraphConstants.setOpaque(celda.getAttributes(),
									r.salidaVisible()
									&& Conf.mostrarEstructuraCompleta);
						}
					} else {
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorPanel);
					}

					GraphConstants.setResize(celda.getAttributes(), false);
					if (!Conf.soloEstructuraDYVcrono) {
						GraphConstants
						.setBounds(
								celda.getAttributes(),
								new Rectangle(
										this.separacionAuxiliarMatricesNoCrono,
												this.sepIni
										+ (contadorMatrices
												* dimensiones[1] * this.altCelda)
												+ (contadorMatrices * 30)
												+ (i * this.altCelda),
												(int) (GraphConstants.getSize(celda
														.getAttributes())
														.getWidth()),
												(int) (GraphConstants.getSize(celda
																.getAttributes())
																.getHeight())));
					} else {
						GraphConstants
						.setBounds(
								celda.getAttributes(),
								new Rectangle(
										this.sepIni
										+ (dimensiones[1] + 2 + j)
										* (int) (GraphConstants.getSize(celda
												.getAttributes())
												.getWidth()),
												this.sepIni
												+ (contadorMatrices
														* dimensiones[1] * this.altCelda)
														+ (contadorMatrices * 30)
														+ (i * this.altCelda),
												(int) (GraphConstants.getSize(celda
																.getAttributes())
																.getWidth()),
												(int) (GraphConstants.getSize(celda
																		.getAttributes())
																		.getHeight())));
					}
					this.separacionAuxiliarMatricesNoCrono += ((int) GraphConstants
							.getSize(celda.getAttributes()).getWidth());

					c.add(celda);
					this.separacionAuxiliar = this.separacionAuxiliarMatricesNoCrono + 20;
				}
				// Mostramos resto de parámetros
				if (!Conf.soloEstructuraDYVcrono) {
					if (i == 0) {
						String entAux[] = r.getNombreParametros();
						String ent[] = ServiciosString.obtenerPrefijos(entAux);
						String ent2[] = r.getEntradasString();
						for (int x = 0; (x < ent.length) && (x < ent2.length); x++) {
							if (x != r.getEntrada().getIndiceDeEstructura()) {
								c.add(this.celdaParametrosSalNoCronoMatrices(
										ent[x] + "=" + ent2[x], r, dimensiones));
							}
						}
					}
				}
			}

			contadorMatrices++;
		}

		for (int i = 0; i < r.numHijos(); i++) {
			if ((r.getHijo(i).inhibido() && Conf.mostrarArbolSalto)
					|| (!r.getHijo(i).inhibido())) {
				c.addAll(this.dameCeldasMatrices(r.getHijo(i),
						++this.nivelCelda));
			}
		}

		return c;
	}

	/**
	 * Dibujo de matrices salida cronológica.
	 * 
	 * @param r
	 *            Registro de Activación.
	 * @param nc
	 *            Nivel de la celda.
	 * 
	 * @return Array de celdas.
	 */
	private ArrayList<DefaultGraphCell> dameCeldasMatricesSalidaCronologica(
			RegistroActivacion r, int nc) {

		Estructura eE = null;
		Estructura eS = null;
		ArrayList<DefaultGraphCell> c = new ArrayList<DefaultGraphCell>(0);

		int indices[] = null;
		int dimensiones[] = null;

		if (r != null
				&& r.esDYV()
				&& r.entradaVisible()
				&& (Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
			indices = r.getEntrada().getIndices();

			eE = new Estructura(r.getEntrada().getEstructura());
			dimensiones = eE.dimensiones();
			// Celdas de entrada de este nodo

			if (indices == null || indices.length == 0) {
				indices = new int[4];
				indices[0] = 0;
				indices[1] = eE.dimensiones()[0] - 1;
				indices[2] = 0;
				indices[3] = eE.dimensiones()[1] - 1;
			}

			for (int i = 0; i < dimensiones[0]; i++) {
				int contador = 0;
				for (int j = 0; j < dimensiones[1]; j++) {
					String texto;
					String clase = eE.getTipo().getClass().getName();
					if (clase.contains("Integer")) {
						texto = this.esp + eE.posicMatrizInt(i, j) + this.esp;
					} else if (clase.contains("Long")) {
						texto = this.esp + eE.posicMatrizLong(i, j) + this.esp;
					} else if (clase.contains("Double")) {
						texto = this.esp + eE.posicMatrizDouble(i, j)
								+ this.esp;
					} else if (clase.contains("String")) {
						texto = this.esp + eE.posicMatrizString(i, j)
								+ this.esp;
					} else if (clase.contains("Character")) {
						texto = this.esp + eE.posicMatrizChar(i, j) + this.esp;
					} else if (clase.contains("Boolean")) {
						texto = this.esp + eE.posicMatrizBool(i, j) + this.esp;
					} else {
						texto = this.esp + "ANoDef" + this.esp;
					}

					DefaultGraphCell celda = new DefaultGraphCell(new String(
							texto));

					GraphConstants.setFont(celda.getAttributes(), new Font(
							"Arial", Font.BOLD, 20));
					GraphConstants.setDisconnectable(celda.getAttributes(),
							false);
					GraphConstants.setMoveable(celda.getAttributes(), false);
					GraphConstants.setSelectable(celda.getAttributes(), false);
					GraphConstants.setSize(celda.getAttributes(),
							new Dimension(this.pixelAnchoCaracter
									* (maximaLongitudContenidoCelda + 4),
									this.altCelda));

					if (r.entradaVisible()) {
						this.marcoCelda(celda.getAttributes());
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorFEntrada);
						if (i >= indices[0] && i <= indices[1]
								&& j >= indices[2] && j <= indices[3]) {
							GraphConstants.setBackground(celda.getAttributes(),
									Conf.colorC1Entrada);
							GraphConstants.setGradientColor(
									celda.getAttributes(), Conf.colorC2Entrada);
							GraphConstants.setOpaque(celda.getAttributes(),
									r.entradaVisible());
						} else {
							GraphConstants.setBackground(celda.getAttributes(),
									Conf.colorC1AEntrada);
							GraphConstants
							.setGradientColor(celda.getAttributes(),
									Conf.colorC2AEntrada);
							GraphConstants.setOpaque(celda.getAttributes(),
									r.entradaVisible()
									&& Conf.mostrarEstructuraCompleta);
						}
					} else {
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorPanel);
					}

					GraphConstants.setResize(celda.getAttributes(), false);
					GraphConstants
					.setBounds(
							celda.getAttributes(),
							new Rectangle(
									this.sepIni
									+ j
									* (int) (GraphConstants.getSize(celda
											.getAttributes())
											.getWidth()),
											this.sepIni
											+ (contadorMatrices
													* dimensiones[1] * this.altCelda)
													+ (contadorMatrices * 30)
													+ (i * this.altCelda),
											(int) (GraphConstants.getSize(celda
															.getAttributes())
															.getWidth()),
											(int) (GraphConstants.getSize(celda
																	.getAttributes())
																	.getHeight())));
					c.add(celda);
					this.separacionAuxiliar = (this.sepIni + (contador)
							* (int) (GraphConstants.getSize(celda
									.getAttributes()).getWidth()))
									+ (int) (GraphConstants.getSize(celda
											.getAttributes()).getWidth()) + 20;
					contador++;
				}
				// Mostramos resto de parámetros
				if (!Conf.soloEstructuraDYVcrono) {
					if (i == 0) {
						String entAux[] = r.getNombreParametros();
						String ent[] = ServiciosString.obtenerPrefijos(entAux);
						String ent2[] = r.getEntradasString();
						for (int x = 0; (x < ent.length) && (x < ent2.length); x++) {
							if (x != r.getEntrada().getIndiceDeEstructura()) {
								c.add(this.celdaParametrosEntMatrices(ent[x]
										+ "=" + ent2[x], r, dimensiones));
							}
						}
					}
				}
			}

			if (r.entradaVisible()) {
				contadorMatrices++;
			}
		}

		for (int i = 0; i < r.numHijos(); i++) {
			if ((r.getHijo(i).inhibido() && Conf.mostrarArbolSalto)
					|| (!r.getHijo(i).inhibido())) {
				c.addAll(this.dameCeldasMatricesSalidaCronologica(r.getHijo(i),
						++this.nivelCelda));
			}
		}

		if (r != null && r.esDYV()) {
			indices = r.getSalida().getIndices();

			// Celdas de salida de este nodo
			eS = new Estructura(r.getSalida().getEstructura());
			dimensiones = eS.dimensiones();

			for (int i = 0; i < dimensiones[0]; i++) {
				int contador = 0;

				for (int j = 0; j < dimensiones[1]; j++) {
					String texto;
					String clase = eS.getTipo().getClass().getName();

					if (clase.contains("Integer")) {
						texto = this.esp + eS.posicMatrizInt(i, j) + this.esp;
					} else if (clase.contains("Long")) {
						texto = this.esp + eS.posicMatrizLong(i, j) + this.esp;
					} else if (clase.contains("Double")) {
						texto = this.esp + eS.posicMatrizDouble(i, j)
								+ this.esp;
					} else if (clase.contains("String")) {
						texto = this.esp + eS.posicMatrizString(i, j)
								+ this.esp;
					} else if (clase.contains("Character")) {
						texto = this.esp + eS.posicMatrizChar(i, j) + this.esp;
					} else if (clase.contains("Boolean")) {
						texto = this.esp + eS.posicMatrizBool(i, j) + this.esp;
					} else {
						texto = this.esp + "ANoDef" + this.esp;
					}

					DefaultGraphCell celda = new DefaultGraphCell(new String(
							texto));

					GraphConstants.setFont(celda.getAttributes(), new Font(
							"Arial", Font.BOLD, 20));
					GraphConstants.setDisconnectable(celda.getAttributes(),
							false);
					GraphConstants.setMoveable(celda.getAttributes(), false);
					GraphConstants.setSelectable(celda.getAttributes(), false);
					GraphConstants.setSize(celda.getAttributes(),
							new Dimension(this.pixelAnchoCaracter
									* (maximaLongitudContenidoCelda + 4),
									this.altCelda));

					if (r.salidaVisible()) {
						this.marcoCelda(celda.getAttributes());
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorFSalida);

						try {

							if (i >= indices[0] && i <= indices[1]
									&& j >= indices[2] && j <= indices[3]) {
								GraphConstants.setBackground(
										celda.getAttributes(),
										Conf.colorC1Salida);
								GraphConstants.setGradientColor(
										celda.getAttributes(),
										Conf.colorC2Salida);
								GraphConstants.setOpaque(celda.getAttributes(),
										r.salidaVisible());
							} else {
								GraphConstants.setBackground(
										celda.getAttributes(),
										Conf.colorC1ASalida);
								GraphConstants.setGradientColor(
										celda.getAttributes(),
										Conf.colorC2ASalida);
								GraphConstants
								.setOpaque(
										celda.getAttributes(),
										r.salidaVisible()
										&& Conf.mostrarEstructuraCompleta);
							}

						} catch (Exception ex) {
							System.out
							.println("ContenedorCronologica.dameCeldasMatricesSalidaCronologica");
						}

					} else {
						GraphConstants.setForeground(celda.getAttributes(),
								Conf.colorPanel);
					}

					GraphConstants.setResize(celda.getAttributes(), false);
					GraphConstants
					.setBounds(
							celda.getAttributes(),

							new Rectangle(
									this.sepIni
									+ j
									* (int) (GraphConstants.getSize(celda
											.getAttributes())
											.getWidth()),
											this.sepIni
											+ (contadorMatrices
													* dimensiones[1] * this.altCelda)
													+ (contadorMatrices * 30)
													+ (i * this.altCelda),
													(int) (GraphConstants.getSize(celda
															.getAttributes())
															.getWidth()),
															(int) (GraphConstants.getSize(celda
																	.getAttributes())
																	.getHeight())));

					c.add(celda);
					this.separacionAuxiliar = (this.sepIni + (contador)
							* (int) (GraphConstants.getSize(celda
									.getAttributes()).getWidth()))
									+ (int) (GraphConstants.getSize(celda
											.getAttributes()).getWidth()) + 20;
					contador++;
				}
				// Mostramos resto de parámetros
				if (!Conf.soloEstructuraDYVcrono) {
					if (i == 0) {
						String entAux[] = r.getNombreParametros();
						String ent[] = ServiciosString.obtenerPrefijos(entAux);
						String ent2[] = r.getEntradasString();
						for (int x = 0; (x < ent.length) && (x < ent2.length); x++) {
							if (x != r.getEntrada().getIndiceDeEstructura()) {
								c.add(this.celdaParametrosSalMatrices(ent[x]
										+ "=" + ent2[x], r, dimensiones));
							}
						}
					}
				}
			}

			if (r.salidaVisible()) {
				contadorMatrices++;
			}
		}

		return c;
	}

	/**
	 * Permite calcula la máxima longitud del contenido de una celda.
	 * 
	 * @param r
	 *            Registro de activación.
	 * 
	 * @return Máxima longitud del contenido de la celda.
	 */
	private static int calcularMaximaLongitudContenidoCelda(RegistroActivacion r) {
		int x = -1;

		if (r != null) {
			x = Math.max(x, calcularMaximaLongitudContenidoCeldaRA(r));

			for (int i = 0; i < r.numHijos(); i++) {
				x = Math.max(x,
						calcularMaximaLongitudContenidoCeldaRA(r.getHijo(i)));
			}

			ContenedorCronologica.maximaLongitudContenidoCelda = x;
			ContenedorCronologica.celdas = null;
		}

		return x;
	}

	private static int calcularMaximaLongitudContenidoCeldaRA(
			RegistroActivacion r) {
		int x = -1;

		if (r != null && r.esDYV()) {
			Estructura eE = new Estructura(r.getEntrada().getEstructura());
			Estructura eS = new Estructura(r.getSalida().getEstructura());

			String clase = eE.getTipo().getClass().getName();

			if (r.esDYV()) {
				if (eE.esArray()) {
					for (int i = 0; i < eE.dimensiones()[0]; i++) {
						if (clase.contains("Integer")) {
							x = Math.max(x, ("" + eE.posicArrayInt(i)).length());
							x = Math.max(x, ("" + eS.posicArrayInt(i)).length());
						} else if (clase.contains("Long")) {
							x = Math.max(x,
									("" + eE.posicArrayLong(i)).length());
							x = Math.max(x,
									("" + eS.posicArrayLong(i)).length());
						} else if (clase.contains("Double")) {
							x = Math.max(x,
									("" + eE.posicArrayDouble(i)).length());
							x = Math.max(x,
									("" + eS.posicArrayDouble(i)).length());
						} else if (clase.contains("String")) {
							x = Math.max(x,
									("" + eE.posicArrayString(i)).length());
							x = Math.max(x,
									("" + eS.posicArrayString(i)).length());
						} else if (clase.contains("Character")) {
							x = Math.max(x,
									("" + eE.posicArrayChar(i)).length());
							x = Math.max(x,
									("" + eS.posicArrayChar(i)).length());
						} else if (clase.contains("Boolean")) {
							x = Math.max(x,
									("" + eE.posicArrayBool(i)).length());
							x = Math.max(x,
									("" + eS.posicArrayBool(i)).length());
						} else {
							x = Math.max(x, 0);
						}
					}
				} else {
					for (int i = 0; i < eE.dimensiones()[0]; i++) {
						for (int j = 0; j < eE.dimensiones()[1]; j++) {
							if (clase.contains("Integer")) {
								x = Math.max(x,
										("" + eE.posicMatrizInt(i, j)).length());
								x = Math.max(x,
										("" + eS.posicMatrizInt(i, j)).length());
							} else if (clase.contains("Long")) {
								x = Math.max(x, ("" + eE.posicMatrizLong(i, j))
										.length());
								x = Math.max(x, ("" + eS.posicMatrizLong(i, j))
										.length());
							} else if (clase.contains("Double")) {
								x = Math.max(x, ("" + eE
										.posicMatrizDouble(i, j)).length());
								x = Math.max(x, ("" + eS
										.posicMatrizDouble(i, j)).length());
							} else if (clase.contains("String")) {
								x = Math.max(x, ("" + eE
										.posicMatrizString(i, j)).length());
								x = Math.max(x, ("" + eS
										.posicMatrizString(i, j)).length());
							} else if (clase.contains("Character")) {
								x = Math.max(x, ("" + eE.posicMatrizChar(i, j))
										.length());
								x = Math.max(x, ("" + eS.posicMatrizChar(i, j))
										.length());
							} else if (clase.contains("Boolean")) {
								x = Math.max(x, ("" + eE.posicMatrizBool(i, j))
										.length());
								x = Math.max(x, ("" + eS.posicMatrizBool(i, j))
										.length());
							} else {
								x = Math.max(x, 0);
							}
						}
					}
				}

			}
		}
		return x;
	}

	/**
	 * Devuelve el ancho máximo de las celdas.
	 * 
	 * @return Ancho máximo de las celdas.
	 */
	public int maximoAncho() {
		if (celdas != null && celdas.size() > 0) {
			return (int) (GraphConstants.getBounds(celdas
					.get(celdas.size() - 1).getAttributes()).getMaxX());
		}

		return 0;
	}

	/**
	 * Devuelve el ancho máximo visible.
	 * 
	 * @return Ancho máximo visible.
	 */
	public int maximoAnchoVisible() {
		int maximoAncho = 0;

		for (int i = 0; i < celdas.size(); i++) {
			if (GraphConstants.isOpaque(celdas.get(i).getAttributes())) {
				Rectangle2D r = GraphConstants.getBounds(celdas.get(i)
						.getAttributes());
				if (maximoAncho < (r.getX() + r.getWidth())) {
					maximoAncho = (int) (r.getX() + r.getWidth());
				}
			}
		}

		return maximoAncho;
	}

	/**
	 * Devuelve el máximo alto
	 * 
	 * @return Máximo alto.
	 */
	public int maximoAlto() {
		int maximoAlto = 0;

		for (int i = 0; i < celdas.size(); i++) {

			Rectangle2D r = GraphConstants.getBounds(celdas.get(i)
					.getAttributes());
			if (maximoAlto < (r.getY() + r.getHeight())) {
				maximoAlto = (int) (r.getY() + r.getHeight());
			}
		}

		return maximoAlto;
	}

	/**
	 * Devuelve el alto máximo visible.
	 * 
	 * @return Alto máximo visible.
	 */
	public int maximoAltoVisible() {
		int maximoAlto = 0;

		for (int i = 0; i < celdas.size(); i++) {
			if (GraphConstants.isOpaque(celdas.get(i).getAttributes())) {
				Rectangle2D r = GraphConstants.getBounds(celdas.get(i)
						.getAttributes());
				if (maximoAlto < (r.getY() + r.getHeight())) {
					maximoAlto = (int) (r.getY() + r.getHeight());
				}
			}
		}

		return maximoAlto;
	}

	/**
	 * Devuelve las celdas del nodo.
	 * 
	 * @return Celdas
	 */
	public Object[] getCeldas() {
		if (celdas == null) {
			return new Object[0];
		}

		Object celdasNodo[] = new Object[celdas.size()];

		for (int i = 0; i < celdas.size(); i++) {
			celdasNodo[i] = celdas.get(i);
		}

		return celdasNodo;

	}

	/**
	 * Permite obtener el registro de activación correspondiente a la posición
	 * indicada.
	 * 
	 * @param x
	 *            Posición x
	 * @param y
	 *            Posición y
	 * 
	 * @return Registro de activación correspondiente o null si no se ha
	 *         encontrado.
	 */
	public RegistroActivacion getRegistroPosicion(int x, int y) {
		for (int i = 0; i < celdas.size(); i++) {

			Rectangle2D rect = GraphConstants.getBounds(celdas.get(i)
					.getAttributes());
			if (rect.getMaxX() >= x && rect.getMinX() <= x
					&& rect.getMaxY() >= y && rect.getMinY() <= y) {
				if (GraphConstants.isOpaque(celdas.get(i).getAttributes())) {
					return this.ra;
				}

			}
		}

		return null;
	}

	/**
	 * Aplica el borde de la celda seleccionado por configuración.
	 * 
	 * @param am
	 *            Mapa de atributos de la celda en cuestión.
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