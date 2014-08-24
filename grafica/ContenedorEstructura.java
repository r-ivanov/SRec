package grafica;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import ventanas.Ventana;
import conf.Conf;
import datos.Estructura;
import datos.RegistroActivacion;

/**
 * Celda contenedora (que no se visualiza) que contiene un subárbol sobre el
 * cual permite realizar acciones de manera sencilla
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class ContenedorEstructura // extends DefaultGraphCell
{
	private static boolean depurar = false;

	// Se usarán si la estructura es un array
	private DefaultGraphCell celdasArray[];
	// Se usarán si la estructura es una matriz
	private DefaultGraphCell celdasMatriz[][];

	private DefaultGraphCell celdasGrafica[];

	private Object celdasDivisiones[];

	private ArrayList<RegistroActivacion> raAnterioresNivel; // Contiene nodos
	// visitados
	// enteros visibles
	private ArrayList<RegistroActivacion> raAnterioresNivelV; // Contiene nodos
	// visitados no enteros
	// visibles

	private Estructura estructura;
	private int dimensiones[];
	private int indices[];

	private int anchoCelda = 0;
	private int altoCelda = 0;

	private int posicGrafica[] = null;

	private boolean entradasalida = true; // true->estamos representando la

	// estructura

	// de entrada de un nodo
	// false->estamos representando la
	// estructura de salida de un nodo
	// NO tiene nada que ver con si se muestran
	// sólo valores de entrada o de salida en
	// visualización

	/**
	 * Crea un nuevo contenedor para la estructura.
	 * 
	 * @param ra
	 *            Registro de activación asociado.
	 */
	public ContenedorEstructura(RegistroActivacion ra) {
		if (ra != null) {
			if (ra.salidaVisible()) {
				this.estructura = new Estructura(ra.getSalida().getEstructura());
				this.indices = ra.getSalida().getIndices();
				this.entradasalida = false;
			} else if (ra.entradaVisible()) {
				this.estructura = new Estructura(ra.getEntrada()
						.getEstructura());
				this.indices = ra.getEntrada().getIndices();
			}
			this.raAnterioresNivel = ra.getRegistrosEnteros();
			this.raAnterioresNivelV = ra.getRegistrosIniciados();
		}

		if (ra != null
				&& Conf.degradadosEstructuraSC1.length + 1 != Ventana.thisventana.traza
				.getAltura()) {
			Conf.calcularDegradadosEstructura(
					Ventana.thisventana.traza.getAltura(), true);
		}

		if (ra != null && this.estructura != null) {
			Object tipoEstructura = this.estructura.getTipo();

			this.dimensiones = this.estructura.dimensiones();
			int longitudMaximaTexto = 0;

			if (ra.esDYV() && this.estructura.esMatriz()) {
				int calculadas[][] = new int[this.dimensiones[0]][this.dimensiones[1]];
				calculadas = ubicarPosicionesCalculadas(calculadas,
						this.raAnterioresNivel);

				int visitadas[][] = new int[this.dimensiones[0]][this.dimensiones[1]];
				visitadas = ubicarPosicionesVisitadas(visitadas,
						this.raAnterioresNivelV);

				// Creamos todas las celdas de la matriz
				this.celdasMatriz = new DefaultGraphCell[this.dimensiones[0]][];
				for (int i = 0; i < this.dimensiones[0]; i++) {
					this.celdasMatriz[i] = new DefaultGraphCell[this.dimensiones[1]];
					for (int j = 0; j < this.dimensiones[1]; j++) {
						String texto = null;
						String clase = tipoEstructura.getClass().getName();
						if (clase.contains("Integer")) {
							texto = "  " + this.estructura.posicMatrizInt(i, j)
									+ "  ";
						} else if (clase.contains("Long")) {
							texto = "  "
									+ this.estructura.posicMatrizLong(i, j)
									+ "  ";
						} else if (clase.contains("Double")) {
							texto = "  "
									+ this.estructura.posicMatrizDouble(i, j)
									+ "  ";
						} else if (clase.contains("String")) {
							texto = "  "
									+ this.estructura.posicMatrizString(i, j)
									+ "  ";
						} else if (clase.contains("Character")) {
							texto = "  "
									+ this.estructura.posicMatrizChar(i, j)
									+ "  ";
						} else if (clase.contains("Boolean")) {
							texto = "  "
									+ this.estructura.posicMatrizBool(i, j)
									+ "  ";
						} else {
							texto = "  " + "MNoDef" + "  ";
						}
						this.celdasMatriz[i][j] = new DefaultGraphCell(
								new String(texto));
						GraphConstants.setOpaque(
								this.celdasMatriz[i][j].getAttributes(), true);
						if (this.indices.length == 4 && i >= this.indices[0]
								&& i <= this.indices[1] && j >= this.indices[2]
										&& j <= this.indices[3]) {
							if (this.entradasalida) {
								GraphConstants
								.setForeground(this.celdasMatriz[i][j]
										.getAttributes(),
										Conf.colorFEntrada);
								GraphConstants
								.setBackground(this.celdasMatriz[i][j]
										.getAttributes(),
										Conf.colorC1Entrada);
							} else {
								GraphConstants
								.setForeground(this.celdasMatriz[i][j]
										.getAttributes(),
										Conf.colorFSalida);
								GraphConstants
								.setBackground(this.celdasMatriz[i][j]
										.getAttributes(),
										Conf.colorC1Salida);
								visitadas[i][j]++;
							}
							if (Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar
									|| Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar) {
							}
						} else if (calculadas[i][j] != 0) // Hemos calculado las
							// posiciones que ya
							// están ejecutadas
						{
							GraphConstants.setForeground(
									this.celdasMatriz[i][j].getAttributes(),
									Conf.colorFSalida);
							GraphConstants
							.setBackground(
									this.celdasMatriz[i][j]
											.getAttributes(),
											Conf.degradadosEstructuraSC1[calculadas[i][j]]);
						} else if (visitadas[i][j] != 0) // Hemos calculado las
							// posiciones que no
							// están terminadas
						{
							GraphConstants.setForeground(
									this.celdasMatriz[i][j].getAttributes(),
									Conf.colorFEntrada);
							GraphConstants
							.setBackground(
									this.celdasMatriz[i][j]
											.getAttributes(),
											Conf.degradadosEstructuraEC1[visitadas[i][j]]);
						} else {
							GraphConstants.setForeground(
									this.celdasMatriz[i][j].getAttributes(),
									Conf.colorFEntrada);
							GraphConstants.setBackground(
									this.celdasMatriz[i][j].getAttributes(),
									Conf.colorC1AEntrada);
						}
						this.marcoCelda(this.celdasMatriz[i][j].getAttributes());
						GraphConstants.setFont(
								this.celdasMatriz[i][j].getAttributes(),
								new Font("Arial", Font.BOLD, 20));
						GraphConstants.setDisconnectable(
								this.celdasMatriz[i][j].getAttributes(), false);
						GraphConstants.setMoveable(
								this.celdasMatriz[i][j].getAttributes(), false);
						GraphConstants.setSelectable(
								this.celdasMatriz[i][j].getAttributes(), false);

						// Vamos averiguando el tamaño de la cadena máxima para
						// estimar tamaño máximo de celda
						if (texto.length() > longitudMaximaTexto) {
							longitudMaximaTexto = texto.length();
						}
					}
				}
				this.posicGrafica = new int[2];

				// El tamaño depende del total de celdas, no sólo de los
				// caracteres que incluya ésta
				for (int i = 0; i < this.dimensiones[0]; i++) {
					for (int j = 0; j < this.dimensiones[1]; j++) {
						GraphConstants.setSize(
								this.celdasMatriz[i][j].getAttributes(),
								new Dimension(11 * longitudMaximaTexto, 26));
					}
				}

				// La posición en que se ubiquen depende del tamaño
				for (int i = 0; i < this.dimensiones[0]; i++) {
					for (int j = 0; j < this.dimensiones[1]; j++) {
						GraphConstants
						.setBounds(
								this.celdasMatriz[i][j].getAttributes(),
								new Rectangle(
										(j)
										* (int) (GraphConstants
												.getSize(this.celdasMatriz[i][j]
														.getAttributes())
														.getWidth()),
														(i)
														* (int) (GraphConstants
																.getSize(this.celdasMatriz[i][j]
																		.getAttributes())
																		.getHeight()),
																		(int) (GraphConstants
																				.getSize(this.celdasMatriz[i][j]
																						.getAttributes())
																						.getWidth()),
																						(int) (GraphConstants
																								.getSize(this.celdasMatriz[i][j]
																										.getAttributes())
																										.getHeight())));
						if (j == 0 && (i == this.dimensiones[0] - 1)) {
							this.posicGrafica[0] = 0;
							this.posicGrafica[1] = (i)
									* (int) (GraphConstants
											.getSize(this.celdasMatriz[i][j]
													.getAttributes())
													.getHeight()) + 60;
						}
					}
				}

				this.anchoCelda = (int) (GraphConstants
						.getSize(this.celdasMatriz[0][0].getAttributes())
						.getWidth());
				this.altoCelda = (int) (GraphConstants
						.getSize(this.celdasMatriz[0][0].getAttributes())
						.getHeight());

				boolean filasMarcadas[][] = null;
				boolean colMarcadas[][] = null;

				filasMarcadas = ubicarPosicionesLineasFilasIniciales(visitadas);
				colMarcadas = ubicarPosicionesLineasColIniciales(visitadas);

				boolean filasMarcadasAux[][] = new boolean[filasMarcadas.length][filasMarcadas[0].length];
				boolean colMarcadasAux[][] = new boolean[colMarcadas.length][colMarcadas[0].length];

				for (int i = 0; i < filasMarcadas.length; i++) {
					for (int j = 0; j < filasMarcadas[i].length; j++) {
						filasMarcadasAux[i][j] = filasMarcadas[i][j];
					}
				}

				for (int i = 0; i < colMarcadas.length; i++) {
					for (int j = 0; j < colMarcadas[i].length; j++) {
						colMarcadasAux[i][j] = colMarcadas[i][j];
					}
				}

				filasMarcadas = ubicarPosicionesLineasFilasCalculadas(
						filasMarcadas, colMarcadasAux);
				colMarcadas = ubicarPosicionesLineasColCalculadas(colMarcadas,
						filasMarcadasAux);
				this.celdasDivisiones = this.crearCeldasDivisiones(
						filasMarcadas, colMarcadas);

				// Gráfica de estructura
				ArrayList<DefaultGraphCell> arrayceldasgrafica = this
						.dibujarGraficaMatriz(Ventana.thisventana.traza
								.getRaiz());
				this.celdasGrafica = new DefaultGraphCell[arrayceldasgrafica
				                                          .size()];
				for (int i = 0; i < arrayceldasgrafica.size(); i++) {
					this.celdasGrafica[i] = arrayceldasgrafica.get(i);
				}

			} else if (ra.esDYV()) {
				int calculadas[] = new int[this.dimensiones[0]];
				calculadas = ubicarPosicionesCalculadas(calculadas,
						this.raAnterioresNivel);

				int visitadas[] = new int[this.dimensiones[0]];
				visitadas = ubicarPosicionesVisitadas(visitadas,
						this.raAnterioresNivelV);

				// Creamos todas las celdas del array
				this.celdasArray = new DefaultGraphCell[this.dimensiones[0]];
				for (int i = 0; i < this.dimensiones[0]; i++) {
					String texto = null;
					String clase = tipoEstructura.getClass().getName();
					if (clase.contains("Integer")) {
						texto = "  " + this.estructura.posicArrayInt(i) + "  ";
					} else if (clase.contains("Long")) {
						texto = "  " + this.estructura.posicArrayLong(i) + "  ";
					} else if (clase.contains("Double")) {
						texto = "  " + this.estructura.posicArrayDouble(i)
								+ "  ";
					} else if (clase.contains("String")) {
						texto = "  " + this.estructura.posicArrayString(i)
								+ "  ";
					} else if (clase.contains("Character")) {
						texto = "  " + this.estructura.posicArrayChar(i) + "  ";
					} else if (clase.contains("Boolean")) {
						texto = "  " + this.estructura.posicArrayBool(i) + "  ";
					} else {
						texto = "  " + "ANoDef" + "  ";
					}
					this.celdasArray[i] = new DefaultGraphCell(
							new String(texto));
					GraphConstants.setOpaque(
							this.celdasArray[i].getAttributes(), true);

					if (this.indices.length == 2 && i >= this.indices[0]
							&& i <= this.indices[1]) {
						if (this.entradasalida) {
							GraphConstants.setForeground(
									this.celdasArray[i].getAttributes(),
									Conf.colorFEntrada);
							GraphConstants.setBackground(
									this.celdasArray[i].getAttributes(),
									Conf.colorC1Entrada);

						} else {
							GraphConstants.setForeground(
									this.celdasArray[i].getAttributes(),
									Conf.colorFSalida);
							GraphConstants.setBackground(
									this.celdasArray[i].getAttributes(),
									Conf.colorC1Salida);
							visitadas[i]++;

						}
					} else if (calculadas[i] != 0) // Hemos calculado las
						// posiciones que ya están
						// ejecutadas
					{
						GraphConstants.setForeground(
								this.celdasArray[i].getAttributes(),
								Conf.colorFSalida);
						GraphConstants.setBackground(
								this.celdasArray[i].getAttributes(),
								Conf.degradadosEstructuraSC1[calculadas[i]]);
					} else if (visitadas[i] != 0) // Hemos calculado las
						// posiciones que no están
						// terminadas
					{
						GraphConstants.setForeground(
								this.celdasArray[i].getAttributes(),
								Conf.colorFEntrada);
						GraphConstants.setBackground(
								this.celdasArray[i].getAttributes(),
								Conf.degradadosEstructuraEC1[visitadas[i]]);
					} else {
						GraphConstants.setForeground(
								this.celdasArray[i].getAttributes(),
								Conf.colorFEntrada);
						GraphConstants.setBackground(
								this.celdasArray[i].getAttributes(),
								Conf.colorC1AEntrada);
					}
					this.marcoCelda(this.celdasArray[i].getAttributes());
					GraphConstants.setFont(this.celdasArray[i].getAttributes(),
							new Font("Arial", Font.BOLD, 20));
					GraphConstants.setDisconnectable(
							this.celdasArray[i].getAttributes(), false);
					GraphConstants.setMoveable(
							this.celdasArray[i].getAttributes(), false);
					GraphConstants.setSelectable(
							this.celdasArray[i].getAttributes(), false);

					// Vamos averiguando el tamaño de la cadena máxima para
					// estimar tamaño máximo de celda
					if (texto.length() > longitudMaximaTexto) {
						longitudMaximaTexto = texto.length();
					}

				}

				this.posicGrafica = new int[2];

				// El tamaño depende del total de celdas, no sólo de los
				// caracteres que incluya ésta
				for (int i = 0; i < this.dimensiones[0]; i++) {
					GraphConstants.setSize(this.celdasArray[i].getAttributes(),
							new Dimension(11 * longitudMaximaTexto, 26));
				}

				// La posición en que se ubiquen depende del tamaño
				for (int i = 0; i < this.dimensiones[0]; i++) {
					GraphConstants.setBounds(
							this.celdasArray[i].getAttributes(),
							new Rectangle((i)
									* (int) (GraphConstants
											.getSize(this.celdasArray[i]
													.getAttributes())
													.getWidth()), 0,
													(int) (GraphConstants
															.getSize(this.celdasArray[i]
																	.getAttributes())
																	.getWidth()), (int) (GraphConstants
																			.getSize(this.celdasArray[i]
																					.getAttributes())
																					.getHeight())));
				}

				this.anchoCelda = (int) (GraphConstants
						.getSize(this.celdasArray[0].getAttributes())
						.getWidth());
				this.altoCelda = (int) (GraphConstants
						.getSize(this.celdasArray[0].getAttributes())
						.getHeight());

				boolean colMarcadas[] = null;
				colMarcadas = ubicarPosicionesLineasColIniciales(visitadas);
				this.celdasDivisiones = this.crearCeldasDivisiones(colMarcadas);

				// Gráfica de estructura
				ArrayList<DefaultGraphCell> arrayceldasgrafica = this
						.dibujarGraficaArray(Ventana.thisventana.traza
								.getRaiz());

				this.celdasGrafica = new DefaultGraphCell[arrayceldasgrafica
				                                          .size()];
				for (int i = 0; i < arrayceldasgrafica.size(); i++) {
					this.celdasGrafica[i] = arrayceldasgrafica.get(i);
				}
			}
		}
	}

	/**
	 * Devuelve el máximo ancho de la estructura.
	 * 
	 * @return Máximo ancho de la estructura.
	 */
	public int maximoAncho() {
		if (this.estructura.esMatriz()) {
			return (int) GraphConstants.getSize(
					this.celdasMatriz[0][0].getAttributes()).getWidth()
					* this.dimensiones[1];
		} else if (this.estructura.esArray()) {
			return (int) GraphConstants.getSize(
					this.celdasArray[0].getAttributes()).getWidth()
					* this.dimensiones[0];
		} else {
			return 0;
		}
	}

	/**
	 * Devuelve el máximo alto de la estructura.
	 * 
	 * @return Máximo alto de la estructura.
	 */
	public int maximoAlto() {
		int maximoAlto = 0;

		for (int i = 0; i < this.celdasGrafica.length; i++) {

			Rectangle2D r = GraphConstants.getBounds(this.celdasGrafica[i]
					.getAttributes());
			if (maximoAlto < (r.getY() + r.getHeight())) {
				maximoAlto = (int) (r.getY() + r.getHeight());
			}
		}

		return maximoAlto;
	}

	/**
	 * Devuelve las celdas del grafo que componen la estructura.
	 * 
	 * @return Celdas del grafo que componen la estructura.
	 */
	public Object[] getCeldas() {
		Object celdasNodo[];

		if (this.estructura == null) {
			return new Object[0];
		}

		if (this.estructura.esMatriz()) {
			celdasNodo = new Object[this.dimensiones[0] * this.dimensiones[1]];
			for (int i = 0; i < this.dimensiones[0]; i++) {
				for (int j = 0; j < this.dimensiones[1]; j++) {
					celdasNodo[(i * this.dimensiones[1]) + j] = this.celdasMatriz[i][j];
				}
			}
		} else {
			celdasNodo = new Object[this.dimensiones[0]];
			for (int i = 0; i < this.dimensiones[0]; i++) {
				celdasNodo[i] = this.celdasArray[i];
			}
		}

		// Habrá que añadir a celdasNodo las celdas que representan las
		// divisiones, que estarán en un array de Object
		// -> usar método concatenarArrays.

		if (this.celdasGrafica != null) {
			celdasNodo = this.concatenarArrays(celdasNodo, this.celdasGrafica);
		}

		if (this.celdasDivisiones != null) {
			celdasNodo = this.concatenarArrays(celdasNodo,
					this.celdasDivisiones);
		}

		return celdasNodo;
	}

	/**
	 * Permite concatenar dos arrays.
	 * 
	 * @param a
	 *            Primer array.
	 * @param b
	 *            Segundo array.
	 * 
	 * @return array concatenado.
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
	 * Devuelve el nivel en la jerarquía para cada una de las posiciones de la
	 * matriz de los registros de activación completamente visibles.
	 * 
	 * @param calculadas
	 *            array de entrada en el que se rellenarán los niveles.
	 * @param raAnterioresNivel
	 *            Registro de activación de los nodos completamente visibles.
	 * 
	 * @return array de salida, con los niveles rellenados.
	 */
	private static int[][] ubicarPosicionesCalculadas(int[][] calculadas,
			ArrayList<RegistroActivacion> raAnterioresNivel) {
		for (int i = 0; i < raAnterioresNivel.size(); i++) {
			int indicesRa[] = raAnterioresNivel.get(i).getEntrada()
					.getIndices();
			if (indicesRa != null && indicesRa.length > 0) {
				for (int x = indicesRa[0]; x <= indicesRa[1]; x++) {
					for (int y = indicesRa[2]; y <= indicesRa[3]; y++) {
						if (calculadas[x][y] > raAnterioresNivel.get(i)
								.getNivel() || calculadas[x][y] == 0) {
							calculadas[x][y] = raAnterioresNivel.get(i)
									.getNivel();
						}
					}
				}
			}
		}
		return calculadas;
	}

	/**
	 * Devuelve el nivel en la jerarquía para cada una de las posiciones de la
	 * matriz de los registros de activación que han sido iniciados en la
	 * visualización.
	 * 
	 * @param calculadas
	 *            array de entrada en el que se rellenarán los niveles.
	 * @param raAnterioresNivel
	 *            Registro de activación de los nodos iniciados.
	 * 
	 * @return array de salida, con los niveles rellenados.
	 */
	private static int[][] ubicarPosicionesVisitadas(int[][] visitadas,
			ArrayList<RegistroActivacion> raAnterioresNivel) {
		for (int i = 0; i < raAnterioresNivel.size(); i++) {
			int indicesRa[] = raAnterioresNivel.get(i).getEntrada()
					.getIndices();
			if (indicesRa != null && indicesRa.length > 0) {
				for (int x = indicesRa[0]; x <= indicesRa[1]; x++) {
					for (int y = indicesRa[2]; y <= indicesRa[3]; y++) {
						if (visitadas[x][y] < raAnterioresNivel.get(i)
								.getNivel() || visitadas[x][y] == 0) {
							visitadas[x][y] = raAnterioresNivel.get(i)
									.getNivel();
						}
					}
				}
			}
		}
		return visitadas;
	}

	/**
	 * Devuelve el nivel en la jerarquía para cada una de las posiciones del
	 * array de los registros de activación completamente visibles.
	 * 
	 * @param calculadas
	 *            array de entrada en el que se rellenarán los niveles.
	 * @param raAnterioresNivel
	 *            Registro de activación de los nodos completamente visibles.
	 * 
	 * @return array de salida, con los niveles rellenados.
	 */
	private static int[] ubicarPosicionesCalculadas(int[] calculadas,
			ArrayList<RegistroActivacion> raAnterioresNivel) {
		for (int i = 0; i < raAnterioresNivel.size(); i++) {
			int indicesRa[] = raAnterioresNivel.get(i).getEntrada()
					.getIndices();
			if (indicesRa != null && indicesRa.length > 1) {
				for (int x = indicesRa[0]; x <= indicesRa[1]; x++) {
					if (calculadas[x] > raAnterioresNivel.get(i).getNivel()
							|| calculadas[x] == 0) {
						calculadas[x] = raAnterioresNivel.get(i).getNivel();
					}
				}
			}
		}
		return calculadas;
	}

	/**
	 * Devuelve el nivel en la jerarquía para cada una de las posiciones de la
	 * array de los registros de activación que han sido iniciados en la
	 * visualización.
	 * 
	 * @param calculadas
	 *            array de entrada en el que se rellenarán los niveles.
	 * @param raAnterioresNivel
	 *            Registro de activación de los nodos iniciados.
	 * 
	 * @return array de salida, con los niveles rellenados.
	 */
	private static int[] ubicarPosicionesVisitadas(int[] visitadas,
			ArrayList<RegistroActivacion> raAnterioresNivel) {
		for (int i = 0; i < raAnterioresNivel.size(); i++) {
			int indicesRa[] = raAnterioresNivel.get(i).getEntrada()
					.getIndices();

			if (indicesRa != null && indicesRa.length > 1) {
				for (int x = indicesRa[0]; x <= indicesRa[1]; x++) {
					if (visitadas[x] < raAnterioresNivel.get(i).getNivel()
							|| visitadas[x] == 0) {
						visitadas[x] = raAnterioresNivel.get(i).getNivel();
					}
				}
			}
		}
		return visitadas;
	}

	/**
	 * Determina las filas que deben aparecer marcadas en la visualización para
	 * matrices según los registros de activación que han sido iniciados.
	 * 
	 * @param visitadas
	 *            matriz que contiene en cada posición el nivel de la posición
	 *            para los registros de activación que han sido iniciados.
	 * 
	 * @return array de salida, especificando por cada posición si debe marcarse
	 *         o no.
	 */
	private static boolean[][] ubicarPosicionesLineasFilasIniciales(
			int[][] visitadas) {
		boolean[][] filasMarcadas = new boolean[visitadas.length - 1][visitadas[0].length];

		for (int i = 0; i < filasMarcadas.length; i++) {
			for (int j = 0; j < filasMarcadas[0].length; j++) {
				if (visitadas[i][j] != visitadas[i + 1][j]) {
					filasMarcadas[i][j] = true;
				}
			}
		}

		return filasMarcadas;
	}

	/**
	 * Determina las columnas que deben aparecer marcadas en la visualización
	 * para matrices según los registros de activación que han sido iniciados.
	 * 
	 * @param visitadas
	 *            matriz que contiene en cada posición el nivel de la posición
	 *            para los registros de activación que han sido iniciados.
	 * 
	 * @return array de salida, especificando por cada posición si debe marcarse
	 *         o no.
	 */
	private static boolean[][] ubicarPosicionesLineasColIniciales(
			int[][] visitadas) {
		boolean[][] colMarcadas = new boolean[visitadas[0].length][visitadas.length - 1];

		for (int i = 0; i < colMarcadas.length; i++) {
			for (int j = 0; j < colMarcadas[0].length; j++) {
				if (visitadas[i][j] != visitadas[i][j + 1]) {
					colMarcadas[i][j] = true;
				}
			}
		}

		return colMarcadas;
	}

	/**
	 * Determina las columnas que deben aparecer marcadas en la visualización
	 * para arrays según los registros de activación que han sido iniciados.
	 * 
	 * @param visitadas
	 *            array que contiene en cada posición el nivel de la posición
	 *            para los registros de activación que han sido iniciados.
	 * 
	 * @return array de salida, especificando por cada posición si debe marcarse
	 *         o no.
	 */
	private static boolean[] ubicarPosicionesLineasColIniciales(int[] visitadas) {
		boolean[] colMarcadas = new boolean[visitadas.length - 1];

		for (int i = 0; i < colMarcadas.length; i++) {
			if (visitadas[i] != visitadas[i + 1]) {
				colMarcadas[i] = true;
			}
		}

		return colMarcadas;
	}

	/**
	 * Determina las filas que deben aparecer marcadas en la visualización para
	 * matrices según los registros de activación que han sido iniciados.
	 * 
	 * @param visitadas
	 *            matriz que contiene en cada posición el nivel de la posición
	 *            para los registros de activación que han sido iniciados.
	 * 
	 * @return array de salida, especificando por cada posición si debe marcarse
	 *         o no.
	 */
	private static boolean[][] ubicarPosicionesLineasFilasCalculadas(
			boolean[][] m, boolean[][] columnas) {
		int fila[] = null; // 0,1,2,3 -> posiciones de la matriz m en la que
		// está la fila (inicio y final)

		for (int i = 0; i < m.length; i++) {
			fila = extraeFila(m, i);
			if (fila != null) {
				m = reasignaFila(m, columnas, fila[0], fila[1], fila[2],
						fila[3]);
			}
			fila = null;
		}
		return m;
	}

	/**
	 * Permite extraer las posiciones de una fila marcada.
	 * 
	 * @param m
	 *            Matriz de posiciones marcadas.
	 * @param filaMatriz
	 *            fila a extraer de la matriz.
	 * 
	 * @return array en el que cada posición devuelve: 0 -> fila de la matriz, 1
	 *         -> posición de inicio de la fila, 2 -> fila de la matriz, 3 ->
	 *         posición final de la fila.
	 */
	private static int[] extraeFila(boolean[][] m, int filaMatriz) {
		int inicioFila = -1;
		int finalFila = -1;

		for (int i = 0; i < m[filaMatriz].length; i++) {
			if (m[filaMatriz][i] && inicioFila == -1) {
				inicioFila = i;
			} else if (!m[filaMatriz][i] && inicioFila != -1 && finalFila == -1) {
				finalFila = i - 1;
			}
		}

		if ((inicioFila != -1) && (finalFila == -1)) {
			finalFila = m[filaMatriz].length - 1;
		}

		if (inicioFila == -1) {
			return null;
		} else {
			int[] fila = new int[4];
			fila[0] = filaMatriz;
			fila[1] = inicioFila;
			fila[2] = filaMatriz;
			fila[3] = finalFila;
			return fila;
		}

	}

	private static boolean[][] reasignaFila(boolean[][] m,
			boolean[][] columnas, int iniciox, int inicioy, int finalx,
			int finaly) {
		int inicioyAux = inicioy - 1;
		while (inicioyAux >= 0
				&& !(columnas[iniciox][inicioyAux] && columnas[iniciox + 1][inicioyAux])) {
			m[iniciox][inicioyAux] = true;
			inicioyAux--;
		}
		inicioyAux = inicioy;

		if (finaly < m.length) {
			while (finaly < columnas[0].length
					&& finalx < columnas.length - 1
					&& !(columnas[finalx][finaly] && columnas[finalx + 1][finaly])) {
				m[finalx][finaly] = true;
				m[finalx][finaly + 1] = true;
				finaly++;
			}
		}
		return m;
	}

	private static boolean[][] ubicarPosicionesLineasColCalculadas(
			boolean[][] m, boolean[][] filas) {
		int col[] = null; // 0,1,2,3 -> posiciones de la matriz m en la que está
		// la columna (inicio y final)

		for (int i = 0; i < m[0].length; i++) {
			col = extraeCol(m, i);
			if (col != null) {
				m = reasignaCol(m, filas, col[1], col[0], col[3], col[2]);
			}
			col = null;
		}
		return m;
	}

	private static int[] extraeCol(boolean[][] m, int colMatriz) {
		int inicioCol = -1;
		int finalCol = -1;

		// cambiar orden de indices, el que varia ahora es el segundo...
		for (int i = 0; i < m.length; i++) {
			if (m[i][colMatriz] && inicioCol == -1) {
				inicioCol = i;
			} else if (!m[i][colMatriz] && inicioCol != -1 && finalCol == -1) {
				finalCol = i - 1;
			}
		}

		if ((inicioCol != -1) && (finalCol == -1)) {
			finalCol = m.length - 1;
		}

		if (inicioCol == -1) {
			return null;
		} else {
			int[] col = new int[4];
			col[0] = colMatriz;
			col[1] = inicioCol;
			col[2] = colMatriz;
			col[3] = finalCol;
			return col;
		}
	}

	private static boolean[][] reasignaCol(boolean[][] m, boolean[][] filas,
			int iniciox, int inicioy, int finalx, int finaly) {
		iniciox--;
		while (iniciox >= 0
				&& !(filas[iniciox][inicioy] && filas[iniciox][inicioy + 1])) {
			m[iniciox][inicioy] = true;
			iniciox--;
		}

		if (finalx < m.length - 1) {
			while (finalx < filas.length
					&& !(filas[finalx][finaly] && filas[finalx][finaly + 1])) {
				m[finalx][finaly] = true;
				m[finalx + 1][finaly] = true;
				finalx++;
			}
		}
		return m;
	}

	/**
	 * Permite construir las celdas del grafo necesarias para representar las
	 * divisiones entre celdas que deben ser mostradas.
	 * 
	 * @param filas
	 *            Valores de marcado para filas.
	 * @param col
	 *            Valores de marcado para columnas.
	 * 
	 * @return Celdas de división para el grafo.
	 */
	private Object[] crearCeldasDivisiones(boolean[][] filas, boolean[][] col) {
		int contador = 0;

		for (int i = 0; i < filas.length; i++) {
			for (int j = 0; j < filas[i].length; j++) {
				if (filas[i][j]) {
					contador++;
				}
			}
		}

		for (int i = 0; i < col.length; i++) {
			for (int j = 0; j < col[i].length; j++) {
				if (col[i][j]) {
					contador++;
				}
			}
		}

		Object[] celdas = new Object[contador];

		contador = 0;

		for (int i = 0; i < filas.length; i++) {
			for (int j = 0; j < filas[i].length; j++) {
				if (filas[i][j]) {
					celdas[contador] = new DefaultGraphCell("");
					GraphConstants.setOpaque(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(), true);

					GraphConstants.setBackground(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(), Conf.colorPanel);
					GraphConstants.setMoveable(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(), false);
					GraphConstants.setSelectable(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(), false);

					GraphConstants.setSize(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(), new Dimension(
									this.anchoCelda, Conf.anchoCeldaDivisoria));

					GraphConstants
					.setBounds(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(),
							new Rectangle(
									(j) * this.anchoCelda,
									((i + 1) * this.altoCelda)
									- (Conf.altoCeldaDivisoria / 2),
									(int) (GraphConstants
											.getSize(((DefaultGraphCell) celdas[contador])
													.getAttributes())
													.getWidth()),
													(int) (GraphConstants
															.getSize(((DefaultGraphCell) celdas[contador])
																	.getAttributes())
																	.getHeight())));

					contador++;
				}
			}
		}

		for (int i = 0; i < col.length; i++) {
			for (int j = 0; j < col[i].length; j++) {
				if (col[i][j]) {
					celdas[contador] = new DefaultGraphCell("");
					GraphConstants.setOpaque(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(), true);

					GraphConstants.setBackground(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(), Conf.colorPanel);
					GraphConstants.setMoveable(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(), false);
					GraphConstants.setSelectable(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(), false);

					GraphConstants.setSize(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(), new Dimension(
									Conf.altoCeldaDivisoria, this.altoCelda));

					GraphConstants
					.setBounds(
							((DefaultGraphCell) celdas[contador])
							.getAttributes(),
							new Rectangle(
									(j + 1)
									* this.anchoCelda
									- (Conf.altoCeldaDivisoria / 2),
									(i) * this.altoCelda,
									(int) (GraphConstants
											.getSize(((DefaultGraphCell) celdas[contador])
													.getAttributes())
													.getWidth()),
													(int) (GraphConstants
															.getSize(((DefaultGraphCell) celdas[contador])
																	.getAttributes())
																	.getHeight())));
					contador++;
				}
			}
		}

		return celdas;
	}

	/**
	 * Permite construir las celdas del grafo necesarias para representar las
	 * divisiones entre celdas que deben ser mostradas.
	 * 
	 * @param col
	 *            Valores de marcado para columnas.
	 * 
	 * @return Celdas de división para el grafo.
	 */
	private Object[] crearCeldasDivisiones(boolean[] col) {
		int contador = 0;

		for (int i = 0; i < col.length; i++) {
			if (col[i]) {
				contador++;
			}
		}

		Object[] celdas = new Object[contador];

		contador = 0;

		for (int i = 0; i < col.length; i++) {
			if (col[i]) {
				celdas[contador] = new DefaultGraphCell("");
				GraphConstants.setOpaque(
						((DefaultGraphCell) celdas[contador]).getAttributes(),
						true);

				GraphConstants.setForeground(
						((DefaultGraphCell) celdas[contador]).getAttributes(),
						Conf.colorPanelAlgoritmo);
				GraphConstants.setBackground(
						((DefaultGraphCell) celdas[contador]).getAttributes(),
						Conf.colorPanelAlgoritmo);
				GraphConstants.setGradientColor(
						((DefaultGraphCell) celdas[contador]).getAttributes(),
						Conf.colorPanelAlgoritmo);

				GraphConstants.setFont(
						((DefaultGraphCell) celdas[contador]).getAttributes(),
						new Font("Arial", Font.BOLD, 8));

				GraphConstants.setDisconnectable(
						((DefaultGraphCell) celdas[contador]).getAttributes(),
						false);
				GraphConstants.setMoveable(
						((DefaultGraphCell) celdas[contador]).getAttributes(),
						false);
				GraphConstants.setSelectable(
						((DefaultGraphCell) celdas[contador]).getAttributes(),
						false);

				GraphConstants.setSize(
						((DefaultGraphCell) celdas[contador]).getAttributes(),
						new Dimension(Conf.altoCeldaDivisoria, this.altoCelda));

				GraphConstants
				.setBounds(
						((DefaultGraphCell) celdas[contador])
						.getAttributes(),
						new Rectangle(
								(i + 1) * this.anchoCelda
								- (Conf.altoCeldaDivisoria / 2),
								0,
								(int) (GraphConstants
										.getSize(((DefaultGraphCell) celdas[contador])
												.getAttributes())
												.getWidth()),
												(int) (GraphConstants
														.getSize(((DefaultGraphCell) celdas[contador])
																.getAttributes())
																.getHeight())));
				contador++;
			}
		}

		return celdas;
	}

	/**
	 * Permite obtener las celdas necesarias para la representación de la estructura
	 * cuando es una matriz.
	 * 
	 * @param ra Registro de activación asociado.
	 * 
	 * @return Lista de celdas del grafo, lista vacia si ni la entrada ni la salida son visibles.
	 */
	private ArrayList<DefaultGraphCell> dibujarGraficaMatriz(
			RegistroActivacion ra) {
		ArrayList<DefaultGraphCell> celdas = new ArrayList<DefaultGraphCell>(0);

		if (ra.entradaVisible() || ra.salidaVisible()) {
			celdas = this.celdasGraficaNodoMatriz(ra);
		}

		return celdas;
	}
	
	/**
	 * Permite obtener las celdas necesarias para la representación de la estructura
	 * cuando es un array.
	 * 
	 * @param ra Registro de activación asociado.
	 * 
	 * @return Lista de celdas del grafo, lista vacia si ni la entrada ni la salida son visibles.
	 */
	private ArrayList<DefaultGraphCell> dibujarGraficaArray(
			RegistroActivacion ra) {
		ArrayList<DefaultGraphCell> celdas = new ArrayList<DefaultGraphCell>(0);

		if (ra.entradaVisible() || ra.salidaVisible()) {
			celdas = this.celdasGraficaNodoArray(ra);
		}

		return celdas;
	}
	
	/**
	 * Permite obtener las celdas necesarias para la representación de la estructura
	 * cuando es una matriz.
	 * 
	 * @param ra Registro de activación asociado.
	 * 
	 * @return Lista de celdas del grafo.
	 */
	private ArrayList<DefaultGraphCell> celdasGraficaNodoMatriz(
			RegistroActivacion ra) {
		// Datos sobre celdas que se van a usar, en general
		int alto = 2;
		int ancho = 2;
		int separacion = 2;
		ArrayList<DefaultGraphCell> celdas = new ArrayList<DefaultGraphCell>(0);

		// return celdas;

		if (ra.esDYV()) {
			// Datos sobre el registro concreto
			int nivel = ra.getNivel();
			int indices[] = ra.getEntrada().getIndices();

			if (indices.length != 4) {
				indices = new int[4];
				indices[0] = 0;
				indices[1] = new Estructura(ra.getEntrada().getEstructura())
				.dimensiones()[0] - 1;
				indices[2] = 0;
				indices[3] = new Estructura(ra.getEntrada().getEstructura())
				.dimensiones()[1] - 1;

			}

			int sumaAltura = 12; // Sumamos cierta cantidad a altura de celdas,
			// para que se puedan dibujar mejor los
			// niveles más grandes

			// a es el punto de coordenadas mínimas (ángulo sup. izq.) de la
			// primera celda que representa este nodo ra
			int[] a = {
					this.posicGrafica[0] + (indices[2] * this.anchoCelda),
					this.posicGrafica[1]
							+ (indices[0] * (this.altoCelda + sumaAltura)) };

			// calculamos los puntos límite de las celdas que vamos a dibujar,
			// son los puntos B, C, D y E
			int[] b = { a[0] + ((nivel - 1) * (ancho + separacion)),
					a[1] + ((nivel - 1) * (alto + separacion)) };
			int[] c = {
					b[0] + ((indices[3] - indices[2] + 1) * (this.anchoCelda))
					- (2 * ((nivel - 1) * (ancho + separacion))), b[1] };
			int[] d = {
					b[0],
					b[1]
							+ ((indices[1] - indices[0] + 1) * ((this.altoCelda + sumaAltura)))
							- (2 * ((nivel - 1) * (alto + separacion))) };
			int[] e = { c[0], d[1] };

			// Debug
			if (depurar) {
				System.out.println("\nNivel= " + ra.getNivel()
						+ "    anchoCelda=" + this.anchoCelda + " x altoCelda="
						+ this.altoCelda);
				System.out.println("Punto A:" + a[0] + "x" + a[1]);
				System.out.println("Punto B:" + b[0] + "x" + b[1]);
				System.out.println("Punto C:" + c[0] + "x" + c[1]);
				System.out.println("Punto D:" + d[0] + "x" + d[1]);
				System.out.println("Punto E:" + e[0] + "x" + e[1] + "\n");
			}

			// Inicio del proceso de creación
			DefaultGraphCell sup = new DefaultGraphCell("");
			DefaultGraphCell inf = new DefaultGraphCell("");
			DefaultGraphCell izq = new DefaultGraphCell("");
			DefaultGraphCell der = new DefaultGraphCell("");

			Color colorCelda = null;
			if (!ra.entradaVisible() && !ra.salidaVisible()) {
				colorCelda = Conf.colorC1AEntrada;
			} else if (ra.esHistorico()) {
				colorCelda = Conf.colorC1ASalida;
			} else if (ra.salidaVisible()) {
				colorCelda = Conf.colorC1Salida;
			} else {
				// if(ra.entradaVisible())
				colorCelda = Conf.colorC1Entrada;
			}

			// sup
			GraphConstants.setOpaque(sup.getAttributes(), true);
			GraphConstants.setBackground(sup.getAttributes(), colorCelda);
			GraphConstants.setMoveable(sup.getAttributes(), false);
			GraphConstants.setSelectable(sup.getAttributes(), false);
			GraphConstants.setSize(sup.getAttributes(), new Dimension(c[0]
					- b[0] - ((separacion + ancho) * (nivel - 1)), 2));

			GraphConstants.setBounds(
					sup.getAttributes(),
					new Rectangle(b[0], b[1], (int) (GraphConstants.getSize(sup
							.getAttributes()).getWidth()),
							(int) (GraphConstants.getSize(sup.getAttributes())
									.getHeight())));

			if (depurar) {
				System.out.println("Posic SUP:"
						+ b[0]
								+ "x"
								+ b[1]
										+ "    ancho="
										+ ((int) (GraphConstants.getSize(sup.getAttributes())
												.getWidth()))
												+ "    alto="
												+ ((int) (GraphConstants.getSize(sup.getAttributes())
														.getHeight())));
			}

			// inf
			GraphConstants.setOpaque(inf.getAttributes(), true);
			GraphConstants.setBackground(inf.getAttributes(), colorCelda);
			GraphConstants.setMoveable(inf.getAttributes(), false);
			GraphConstants.setSelectable(inf.getAttributes(), false);
			GraphConstants.setSize(inf.getAttributes(), new Dimension(c[0]
					- b[0] - ((separacion + ancho) * (nivel - 1)), 2));

			GraphConstants
			.setBounds(
					inf.getAttributes(),
					new Rectangle(d[0], d[1]
							- ((nivel - 1) * (alto + separacion))
							- alto, (int) (GraphConstants.getSize(inf
									.getAttributes()).getWidth()),
									(int) (GraphConstants.getSize(inf
											.getAttributes()).getHeight())));

			if (depurar) {
				System.out.println("Posic INF:"
						+ (d[0])
						+ "x"
						+ (d[1] - ((nivel - 1) * (alto + separacion)) - alto)
						+ "    ancho="
						+ ((int) (GraphConstants.getSize(inf.getAttributes())
								.getWidth()))
								+ "    alto="
								+ ((int) (GraphConstants.getSize(inf.getAttributes())
										.getHeight())));
			}

			// izq
			GraphConstants.setOpaque(izq.getAttributes(), true);
			GraphConstants.setBackground(izq.getAttributes(), colorCelda);
			GraphConstants.setMoveable(izq.getAttributes(), false);
			GraphConstants.setSelectable(izq.getAttributes(), false);
			GraphConstants.setSize(izq.getAttributes(), new Dimension(2, d[1]
					- b[1] - ((separacion + alto) * (nivel - 1))));

			GraphConstants.setBounds(
					izq.getAttributes(),
					new Rectangle(b[0], b[1], (int) (GraphConstants.getSize(izq
							.getAttributes()).getWidth()),
							(int) (GraphConstants.getSize(izq.getAttributes())
									.getHeight())));

			if (depurar) {
				System.out.println("Posic IZQ:"
						+ b[0]
								+ "x"
								+ b[1]
										+ "    ancho="
										+ ((int) (GraphConstants.getSize(izq.getAttributes())
												.getWidth()))
												+ "    alto="
												+ ((int) (GraphConstants.getSize(izq.getAttributes())
														.getHeight())));
			}

			// der
			GraphConstants.setOpaque(der.getAttributes(), true);
			GraphConstants.setBackground(der.getAttributes(), colorCelda);
			GraphConstants.setMoveable(der.getAttributes(), false);
			GraphConstants.setSelectable(der.getAttributes(), false);
			GraphConstants.setSize(der.getAttributes(), new Dimension(2, d[1]
					- b[1] - ((separacion + alto) * (nivel - 1))));

			GraphConstants.setBounds(
					der.getAttributes(),
					new Rectangle(c[0] - ((nivel - 1) * (ancho + separacion))
							- ancho, c[1], (int) (GraphConstants.getSize(der
									.getAttributes()).getWidth()),
									(int) (GraphConstants.getSize(der.getAttributes())
											.getHeight())));

			if (depurar) {
				System.out.println("Posic DER:"
						+ (c[0] - ((nivel - 1) * (ancho + separacion)) - ancho)
						+ "x"
						+ c[1]
								+ "    ancho="
								+ ((int) (GraphConstants.getSize(der.getAttributes())
										.getWidth()))
										+ "    alto="
										+ ((int) (GraphConstants.getSize(der.getAttributes())
												.getHeight())));
			}

			if (indices[0] == indices[1] && indices[2] == indices[3]
					&& ra.numHijos() == 0) {
				// Para expansion en horizontal
				int minx = (int) (GraphConstants.getBounds(sup.getAttributes()))
						.getMinX();
				int anchaux = (int) (GraphConstants.getBounds(sup
						.getAttributes())).getWidth();

				int ensanchar = ((this.anchoCelda - anchaux) / 2) - separacion
						* 2; // píxeles que se ensanchará la celda

				// Para expansion en vertical
				int miny = (int) (GraphConstants.getBounds(izq.getAttributes()))
						.getMinY();
				int altoaux = (int) (GraphConstants.getBounds(izq
						.getAttributes())).getHeight();

				int alargar = (((this.altoCelda + sumaAltura) - altoaux) / 2)
						- separacion * 2; // píxeles que se ensanchará la celda

				if (indices[2] % 2 == 0) // Si es par, hay que extender celda
					// hacia la derecha
				{
					// Celda sup -> más ancha
					// Celda inf -> más ancha
					// Celda der -> desplazamiento hacia la derecha

					GraphConstants.setBounds(
							sup.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(sup
									.getAttributes())).getMinX(),
									(int) (GraphConstants.getBounds(sup
											.getAttributes())).getMinY(),
											(int) (GraphConstants.getBounds(sup
													.getAttributes())).getWidth()
													+ ensanchar, (int) (GraphConstants
															.getBounds(sup.getAttributes()))
															.getHeight()));

					GraphConstants.setBounds(
							inf.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(inf
									.getAttributes()).getMinX()),
									(int) (GraphConstants.getBounds(inf
											.getAttributes()).getMinY()),
											(int) (GraphConstants.getBounds(inf
													.getAttributes()).getWidth())
													+ ensanchar, (int) (GraphConstants
															.getBounds(inf.getAttributes())
															.getHeight())));

					GraphConstants.setBounds(
							der.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(der
									.getAttributes()).getMinX()) + ensanchar,
									(int) (GraphConstants.getBounds(der
											.getAttributes()).getMinY()),
											(int) (GraphConstants.getBounds(der
													.getAttributes()).getWidth()),
													(int) (GraphConstants.getBounds(der
															.getAttributes()).getHeight())));
				} else // Si es impar, hay que extender celda hacia la izquierda
				{
					// Celda sup -> más ancha
					// Celda inf -> más ancha
					// Celda izq -> desplazamiento hacia la izquierda

					GraphConstants.setBounds(
							sup.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(sup
									.getAttributes()).getMinX()) - ensanchar,
									(int) (GraphConstants.getBounds(sup
											.getAttributes()).getMinY()),
											(int) (GraphConstants.getBounds(sup
													.getAttributes()).getWidth())
													+ ensanchar, (int) (GraphConstants
															.getBounds(sup.getAttributes())
															.getHeight())));

					GraphConstants.setBounds(
							inf.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(inf
									.getAttributes()).getMinX()) - ensanchar,
									(int) (GraphConstants.getBounds(inf
											.getAttributes()).getMinY()),
											(int) (GraphConstants.getBounds(inf
													.getAttributes()).getWidth())
													+ ensanchar, (int) (GraphConstants
															.getBounds(inf.getAttributes())
															.getHeight())));

					GraphConstants.setBounds(
							izq.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(izq
									.getAttributes()).getMinX()) - ensanchar,
									(int) (GraphConstants.getBounds(izq
											.getAttributes()).getMinY()),
											(int) (GraphConstants.getBounds(izq
													.getAttributes()).getWidth()),
													(int) (GraphConstants.getBounds(izq
															.getAttributes()).getHeight())));
				}

				if (indices[0] % 2 == 0) // Si es par, hay que extender celda
					// hacia abajo
				{
					// Celda inf -> más baja (desplazada)
					// Celda izq -> más alta (hacia abajo)
					// Celda der -> más alta (hacia abajo)

					GraphConstants.setBounds(
							inf.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(inf
									.getAttributes()).getMinX()),
									(int) (GraphConstants.getBounds(inf
											.getAttributes()).getMinY())
											+ alargar, (int) (GraphConstants
													.getBounds(inf.getAttributes())
													.getWidth()), (int) (GraphConstants
															.getBounds(inf.getAttributes())
															.getHeight())));

					GraphConstants.setBounds(
							izq.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(izq
									.getAttributes()).getMinX()),
									(int) (GraphConstants.getBounds(izq
											.getAttributes()).getMinY()),
											(int) (GraphConstants.getBounds(izq
													.getAttributes()).getWidth()),
													(int) (GraphConstants.getBounds(izq
															.getAttributes()).getHeight())
															+ alargar));

					GraphConstants.setBounds(
							der.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(der
									.getAttributes()).getMinX()),
									(int) (GraphConstants.getBounds(der
											.getAttributes()).getMinY()),
											(int) (GraphConstants.getBounds(der
													.getAttributes()).getWidth()),
													(int) (GraphConstants.getBounds(der
															.getAttributes()).getHeight())
															+ alargar));
				} else // Si es impar, hay que extender celda hacia arriba
				{
					// Celda sup -> más alta (desplazada)
					// Celda izq -> más alta (hacia arriba)
					// Celda der -> más alta (hacia arriba)

					GraphConstants.setBounds(
							sup.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(sup
									.getAttributes()).getMinX()),
									(int) (GraphConstants.getBounds(sup
											.getAttributes()).getMinY())
											- alargar, (int) (GraphConstants
													.getBounds(sup.getAttributes())
													.getWidth()), (int) (GraphConstants
															.getBounds(sup.getAttributes())
															.getHeight())));

					GraphConstants.setBounds(
							der.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(der
									.getAttributes()).getMinX()),
									(int) (GraphConstants.getBounds(der
											.getAttributes()).getMinY())
											- alargar, (int) (GraphConstants
													.getBounds(der.getAttributes())
													.getWidth()), (int) (GraphConstants
															.getBounds(der.getAttributes())
															.getHeight())
															+ alargar));

					GraphConstants.setBounds(
							izq.getAttributes(),
							new Rectangle((int) (GraphConstants.getBounds(izq
									.getAttributes()).getMinX()),
									(int) (GraphConstants.getBounds(izq
											.getAttributes()).getMinY())
											- alargar, (int) (GraphConstants
													.getBounds(izq.getAttributes())
													.getWidth()), (int) (GraphConstants
															.getBounds(izq.getAttributes())
															.getHeight())
															+ alargar));
				}

			}

			if (ra.entradaVisible() || ra.salidaVisible()) {
				celdas.add(sup);
				celdas.add(inf);
				celdas.add(izq);
				celdas.add(der);
			}
		}

		for (int i = 0; i < ra.numHijos(); i++) {
			if (ra.entradaVisible() || ra.salidaVisible()) {
				celdas.addAll(this.celdasGraficaNodoMatriz(ra.getHijo(i)));
			}
		}

		return celdas;
	}
	
	/**
	 * Permite obtener las celdas necesarias para la representación de la estructura
	 * cuando es un array.
	 * 
	 * @param ra Registro de activación asociado.
	 * 
	 * @return Lista de celdas del grafo.
	 */
	private ArrayList<DefaultGraphCell> celdasGraficaNodoArray(
			RegistroActivacion ra) {
		// Datos sobre celdas que se van a usar, en general
		int alto = 6;
		int separacion = 4;
		ArrayList<DefaultGraphCell> celdas = new ArrayList<DefaultGraphCell>(0);

		if (ra.esDYV()) {

			// Datos sobre el registro concreto
			int nivel = ra.getNivel();
			int indices[] = ra.getEntrada().getIndices();
			if (indices.length != 2) {
				indices = new int[2];
				indices[0] = 0;
				indices[1] = new Estructura(ra.getEntrada().getEstructura())
				.dimensiones()[0] - 1;

				// return new ArrayList<DefaultGraphCell>(0);
			}

			// Determinamos color para la celda
			Color colorCelda = null;
			if (!ra.entradaVisible() && !ra.salidaVisible()) {
				colorCelda = Conf.colorC1AEntrada;
			} else if (ra.esHistorico()) {
				colorCelda = Conf.colorC1ASalida;
			} else if (ra.salidaVisible()) {
				colorCelda = Conf.colorC1Salida;
			} else {
				// if(ra.entradaVisible())
				colorCelda = Conf.colorC1Entrada;
			}

			// int sumaAltura=18; // Sumamos cierta cantidad a altura de celdas,
			// para que se puedan dibujar mejor los niveles más grandes

			// Inicio del proceso de creación
			DefaultGraphCell celda = new DefaultGraphCell("");

			// celda
			GraphConstants.setOpaque(celda.getAttributes(), true);
			GraphConstants.setBackground(celda.getAttributes(), colorCelda);
			GraphConstants.setMoveable(celda.getAttributes(), false);
			GraphConstants.setSelectable(celda.getAttributes(), false);
			GraphConstants.setSize(celda.getAttributes(), new Dimension(
					((indices[1] - indices[0] + 1) * this.anchoCelda)
					- (2 * separacion), alto));

			GraphConstants
			.setBounds(
					celda.getAttributes(),
					new Rectangle((indices[0] * this.anchoCelda)
							+ separacion, this.altoCelda + 20
							+ (10 * nivel) + (alto * (nivel - 1)),
							(int) (GraphConstants.getSize(celda
									.getAttributes()).getWidth()),
									(int) (GraphConstants.getSize(celda
											.getAttributes()).getHeight())));

			if (ra.entradaVisible() || ra.salidaVisible()) {
				celdas.add(celda);
			}
		}

		for (int i = 0; i < ra.numHijos(); i++) {
			if (ra.entradaVisible() || ra.salidaVisible()) {
				celdas.addAll(this.celdasGraficaNodoArray(ra.getHijo(i)));
			}
		}

		return celdas;
	}
	
	/**
	 * Permite obtener el máximo ancho visible de la estructura.
	 * 
	 * @return Máximo ancho visible de la estructura.
	 */
	public int maximoAnchoVisible() {
		int maximoAncho = 0;

		if (this.celdasGrafica != null) {
			for (int i = 0; i < this.celdasGrafica.length; i++) {
				if (GraphConstants.isOpaque(this.celdasGrafica[i]
						.getAttributes())) {
					Rectangle2D r = GraphConstants
							.getBounds(this.celdasGrafica[i].getAttributes());
					if (maximoAncho < (r.getX() + r.getWidth())) {
						maximoAncho = (int) (r.getX() + r.getWidth());
					}
				}
			}
		}

		return maximoAncho;
	}
	
	/**
	 * Permite obtener el máximo alto visible de la estructura.
	 * 
	 * @return Máximo alto visible de la estructura.
	 */
	public int maximoAltoVisible() {
		int maximoAlto = 0;

		if (this.celdasGrafica != null) {
			for (int i = 0; i < this.celdasGrafica.length; i++) {
				if (GraphConstants.isOpaque(this.celdasGrafica[i]
						.getAttributes())) {
					Rectangle2D r = GraphConstants
							.getBounds(this.celdasGrafica[i].getAttributes());
					if (maximoAlto < (r.getY() + r.getHeight())) {
						maximoAlto = (int) (r.getY() + r.getHeight());
					}
				}
			}
		}

		return maximoAlto;
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