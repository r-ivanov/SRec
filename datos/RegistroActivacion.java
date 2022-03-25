package datos;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import paneles.PanelAlgoritmo;
import utilidades.ServiciosString;
import utilidades.Texto;
import conf.Conf;

/**
 * Clase que representa cada una de las llamadas al m�todo seleccionado
 * 
 * @author Luis Fern�ndez y Antonio P�rez Carrasco
 * @version 2006-2007
 */
public class RegistroActivacion {
	private static boolean depurar = false;
	
	private int MAX_REGISTROS_ACTIVACION = 100000;
	
	private static final long serialVersionUID = 2L;

	private static int nodoID = 0; // Va contando n�mero de nodos que se crean,
	// se reinicia al generar cada nueva
	// animaci�n

	private int nID = -1; // N�mero que identifica al nodo, ayuda a creaci�n de
	// traza gemela

	private Estado entrada = null; // Valores de entrada
	private Estado salida = null; // Valores de salida

	private RegistroActivacion padre = null; // Referencia a su padre y a sus
	// hijos
	private ArrayList<RegistroActivacion> hijos = new ArrayList<RegistroActivacion>(
			0);

	private boolean iluminado = false; // Iluinado tras buscar llamadas con sus
	// mismos par�metros desde opci�n de
	// men�
	private boolean resaltado = false; // Indica si el nodo actual se ha
	// resaltado o no

	private boolean metodoVisible = false; // Determina si los nodos de este
	// �rbol aparecer�n o no en el �rbol

	private boolean entradaVisible = false; // Determina si la entrada es
	// visible actualmente
	private boolean salidaVisible = false; // Determina si la salida es visible
	// actualmente

	private boolean historico = false; // true si el nodo es hist�rico
	private boolean mostradoEntero = false; // true si siguienteVisible no es
	// aplicable (s�lo queda ser
	// hist�rico en sig. paso)

	private boolean soyNodoActual = false; // Es el �ltimo nodo sobre el que se
	// ha aplicado un paso
	private boolean soyCaminoActual = false; // Forma parte del camino hasta el
	// nodo activo actual

	private String nombreMetodo; // M�todo al que pertenece este
	// RegistroActivacion
	private String nombreParametros[]; // Nombres de los par�metros de ese
	// m�todo
	private boolean devuelveValor; // A true si devuelve un valor el m�todo, a
	// false si devuelve void
	private int numMetodo = 2; // N�mero de m�todo dentro de la animaci�n

	private boolean contraido = false; // Cuando pulsamos sobre �l para hacer
	// salto m�ltiple
	private boolean inhibido = false; // Cuando sobre alg�n ancestro se hizo un
	// salto m�ltiple

	/**
	 * Constructor: crea un nuevo registro de activaci�n
	 */
	public RegistroActivacion() {

	}

	/**
	 * Constructor: crea un nuevo registro de activaci�n con el estado de
	 * entrada que se le pasa
	 * 
	 * @param entrada
	 *            estado de entrada
	 * @param nombreMetodo
	 *            Nombre del m�todo que se ejecuta.
	 * @param nombreParametros
	 *            Nombre de los par�metros del m�todo.
	 */
	public RegistroActivacion(Estado entrada, String nombreMetodo,
			String nombreParametros[]) {
		this(null, entrada, nombreMetodo, nombreParametros);
	}

	/**
	 * Constructor: crea un nuevo registro de activaci�n con estados de entrada
	 * y salida
	 * 
	 * @param entrada
	 *            estado de entrada
	 * @param salida
	 *            estado de salida
	 * @param nombreMetodo
	 *            Nombre del m�todo que se ejecuta.
	 * @param nombreParametros
	 *            Nombre de los par�metros del m�todo.
	 */
	private RegistroActivacion(RegistroActivacion padre, Estado entrada,
			String nombreMetodo, String nombreParametros[]) {
		this.padre = padre;
		this.entrada = entrada;

		this.nombreMetodo = nombreMetodo;
		this.nombreParametros = nombreParametros;

		this.nID = RegistroActivacion.nodoID;
		RegistroActivacion.nodoID++;
		
		if (RegistroActivacion.nodoID >= MAX_REGISTROS_ACTIVACION) {
			throw new RuntimeException(Texto.get("DEMASIADOS_NODOS", Conf.idioma));
		}
		
		
	}

	/**
	 * Establece el n�mero de nodos creados a 0.
	 */
	static public void reinicializar() {
		nodoID = 0;
	}

	/**
	 * Devuelve el nivel del registro de activaci�n en la jerarqu�a.
	 * 
	 * @return Nivel del registro de activaci�n en la jerarqu�a.
	 */
	public int getNivel() {
		int nivel = 1;
		RegistroActivacion ra_aux = this;

		while (ra_aux.padre != null) {
			nivel++;
			ra_aux = ra_aux.padre;
		}
		return nivel;
	}

	/**
	 * Establece la visibilidad de los distintos elementos.
	 * 
	 * @param entrada
	 *            Visibilidad de la entrada.
	 * @param salida
	 *            Visibilidad de la salida.
	 * @param mostradoEntero
	 *            Si el registro se muestra entero.
	 */
	public void setVisibilidad(boolean entrada, boolean salida,
			boolean mostradoEntero) {
		this.entradaVisible = entrada;
		this.salidaVisible = salida;
		this.mostradoEntero = mostradoEntero;
	}

	/**
	 * Establece informaci�n sobre los hijos del registro.
	 * 
	 * @param historico
	 *            Si pertenece a un nodo hist�rico.
	 * @param mostradoEntero
	 *            Si el registro se muestra entero.
	 */
	public void setInformacionHijos(boolean historico, boolean mostradoEntero) {
		this.historico = historico;
		this.mostradoEntero = mostradoEntero;
	}

	/**
	 * Permite corregir los datos de visibilidad de los distintos m�todos de la
	 * jerarqu�a dada la informaci�n de visibilidad almacenada en la clase del
	 * algoritmo.
	 * 
	 * @param claseAlgoritmo
	 *            Informaci�n de la clase en ejecuci�n.
	 */
	public void setVisibilidad(ClaseAlgoritmo claseAlgoritmo) {
		for (int i = 0; i < claseAlgoritmo.getNumMetodos(); i++) {
			MetodoAlgoritmo m = claseAlgoritmo.getMetodo(i);

			// Si tienen mismo nombre y mismo n�mero de par�metros ...
			if (m.getNombre().equals(this.nombreMetodo)
					&& m.getNumeroParametros() == this.entrada.getClases().length) {
				// ... comprobamos los tipos de estos, si coinciden es el m�todo
				// que buscamos
				String mTipos[] = m.getTiposParametros();
				String tTipos[] = this.entrada.getClases();
				int mDim[] = m.getDimParametros();
				int tDim[] = this.entrada.getDimensiones();

				boolean sonIguales = true;
				for (int j = 0; j < mTipos.length; j++) {
					if (mDim[j] != tDim[j] || !mTipos[j].equals(tTipos[j])) {
						sonIguales = false;
					}
				}

				if (sonIguales) {
					this.metodoVisible = m.getMarcadoVisualizar();
					this.setVisibilidad(m.getVisibilidadEntrada(),
							m.getVisibilidadSalida());
					this.entrada.setRepresentacionLineasTraza(
							m.getVisibilidadEntrada(), this.nombreParametros);

				}
			}
		}

		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).setVisibilidad(claseAlgoritmo);
		}
	}

	/**
	 * Permite corregir los datos de visibilidad de los distintos m�todos de la
	 * jerarqu�a dada la informaci�n de visibilidad almacenada en los datos de
	 * traza b�sicos.
	 * 
	 * @param dtb
	 *            Datos de traza b�sicos de la traza.
	 */
	public void setVisibilidad(DatosTrazaBasicos dtb) {
		for (int i = 0; i < dtb.getNumMetodos(); i++) {
			DatosMetodoBasicos dmb = dtb.getMetodo(i);

			// Si tienen mismo nombre y mismo n�mero de par�metros ...
			if (dmb.getNombre().equals(this.nombreMetodo)
					&& dmb.getNumParametrosE() == this.entrada.getClases().length) {
				// ... comprobamos los tipos de estos, si coinciden es el m�todo
				// que buscamos
				String mTipos[] = dmb.getTipoParametrosE();
				String tTipos[] = this.entrada.getClases();
				int mDim[] = dmb.getDimE();
				int tDim[] = this.entrada.getDimensiones();

				boolean sonIguales = true;
				for (int j = 0; j < mTipos.length; j++) {
					if (mDim[j] != tDim[j] || !mTipos[j].equals(tTipos[j])) {
						sonIguales = false;
					}
				}

				if (sonIguales) {
					this.metodoVisible = dmb.getEsVisible();
					this.setVisibilidad(dmb.getVisibilidadE(),
							dmb.getVisibilidadS());
					this.entrada.setRepresentacionLineasTraza(
							dmb.getVisibilidadE(), this.nombreParametros);

				}
			}
		}

		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).setVisibilidad(dtb);
		}
	}

	/**
	 * Permite configurar la visibilidad de los par�metros de entrada y de
	 * salida.
	 * 
	 * @param paramE
	 *            Visibilidad de los par�metros de entrada.
	 * @param paramS
	 *            Visibilidad de los par�metros de salida.
	 */
	public void setVisibilidad(boolean paramE[], boolean paramS[]) {
		this.entrada.setRepresentacion(paramE);
		this.salida.setRepresentacion(paramS);
		this.entrada
				.setRepresentacionLineasTraza(paramE, this.nombreParametros);
	}

	/**
	 * Establece la visibilidad del m�todo del registro de activaci�n.
	 * 
	 * @param valor
	 *            true si es visible, false en caso contrario.
	 */
	public void setEsMetodoVisible(boolean valor) {
		this.metodoVisible = valor;
	}

	/**
	 * Devuelve la visibilidad del m�todo del registro de activaci�n
	 * 
	 * @return true si es visible, false en caso contrario.
	 */
	public boolean esMetodoVisible() {
		return this.metodoVisible;
	}

	/**
	 * Determina si el nodo tiene un tipo de retorno.
	 * 
	 * @return true si tiene un tipo de retorno, false en caso contrario.
	 */
	public boolean getDevuelveValor() {
		return this.devuelveValor;
	}

	/**
	 * Establece si el nodo tiene un tipo de retorno.
	 * 
	 * @param devuelveValor
	 *            true si tiene un tipo de retorno, false en caso contrario.
	 */
	public void setDevuelveValor(boolean devuelveValor) {
		this.devuelveValor = devuelveValor;
	}

	/**
	 * Devuelve el identificador del registro.
	 * 
	 * @return Identificador del registro.
	 */
	public int getID() {
		return this.nID;
	}

	/**
	 * Establece el identificador del registro.
	 * 
	 * @param id
	 *            Identificador del registro.
	 */
	public void setID(int id) {
		this.nID = id;
	}

	/**
	 * Establece la posici�n del m�todo con respecto a los distintos m�todos
	 * disponibles.
	 * 
	 * @param interfaces
	 *            Lista de m�todos disponibles.
	 */
	public void asignarNumeroMetodo(String[] interfaces) {
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i].equals(this.interfazMetodo())) {
				this.numMetodo = i;
			}
		}
		for (int i = 0; i < this.numHijos(); i++) {
			this.hijos.get(i).asignarNumeroMetodo(interfaces);
		}
	}

	/**
	 * Devuelve la posici�n del m�todo con respecto a los distintos m�todos
	 * disponibles.
	 * 
	 * @return posici�n del m�todo.
	 */
	public int getNumMetodo() {
		return this.numMetodo;
	}

	/**
	 * Devuelve los tipos de los par�metros de entrada.
	 * 
	 * @return Tipos de los par�metros de entrada.
	 */
	public String[] clasesParamE() {
		return this.entrada.getClases();
	}

	/**
	 * Devuelve los tipos de los par�metros de salida.
	 * 
	 * @return Tipos de los par�metros de salida.
	 */
	public String[] clasesParamS() {
		return this.salida.getClases();
	}

	/**
	 * Devuelve las dimensiones de los par�metros de entrada.
	 * 
	 * @return Dimensiones de los par�metros de entrada.
	 */
	public int[] dimParamE() {
		return this.entrada.getDimensiones();
	}

	/**
	 * Devuelve las dimensiones de los par�metros de salida.
	 * 
	 * @return Dimensiones de los par�metros de salida.
	 */
	public int[] dimParamS() {
		return this.salida.getDimensiones();
	}

	/**
	 * Devuelve la visibilidad de los par�metros de entrada.
	 * 
	 * @return Visibilidad de los par�metros de entrada.
	 */
	public boolean[] getVisibilidadEntrada() {
		return this.entrada.getVisibilidad();
	}

	/**
	 * Devuelve la visibilidad de los par�metros de salida.
	 * 
	 * @return Visibilidad de los par�metros de salida.
	 */
	public boolean[] getVisibilidadSalida() {
		return this.salida.getVisibilidad();
	}

	/**
	 * Retorna el estado de entrada
	 * 
	 * @return estado de entrada
	 */
	public Estado getEntrada() {
		return this.entrada;
	}

	/**
	 * Retorna el estado de salida
	 * 
	 * @return estado de salida
	 */
	public Estado getSalida() {
		return this.salida;
	}

	/**
	 * Devuelve la representacion de las lineas de traza.
	 * 
	 * @param margen
	 *            Margen de la l�nea.
	 * 
	 * @return L�neas de traza.
	 */
	public String[] getLineasTraza(int margen) {

		if (this.entradaVisible || this.salidaVisible || this.esHistorico()) {
			String[] lineas = new String[0];

			// Si tenemos entrada visible, a�adimos su l�nea
			if ((this.entradaVisible || (this.esHistorico() && Conf.historia == 1))
					&& (Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
				lineas = new String[1];

				lineas[0] = "";
				for (int i = 0; i < margen; i++) {
					lineas[0] = lineas[0] + " ";
				}

				if (Conf.idMetodoTraza) {
					String metodo = this.getNombreMetodo();
					if (PanelAlgoritmo.nyp != null) {
						metodo = PanelAlgoritmo.nyp.getPrefijo(this
								.getNombreMetodo());
					}
					lineas[0] = lineas[0] + "entra " + metodo + ": "
							+ this.entrada.getRepresentacionLineasTraza();
				} else {
					lineas[0] = lineas[0] + "entra: "
							+ this.entrada.getRepresentacionLineasTraza();
				}

				if (this.esHistorico()) {
					lineas[0] = lineas[0] + " <hist>";
				}

				if (this.iluminado()) {
					lineas[0] = lineas[0] + " <ilum>";
				}

				lineas[0] = lineas[0] + "?" + this.getNumMetodo() + "?";
			}

			if (!this.contraido || (this.contraido && Conf.mostrarArbolSalto)) {
				// A�adimos l�neas de hijos, si los tiene y si no est� contraido
				for (int i = 0; i < this.hijos.size(); i++) {
					if (this.hijos.get(i) != null) // NO DEBER�A SER NECESARIA
					// ESTA COMPROBACI�N
					{
						String[] lineasHijo;
						if (Conf.sangrado) {
							lineasHijo = this.hijos.get(i).getLineasTraza(
									margen + 4);
						} else {
							lineasHijo = this.hijos.get(i).getLineasTraza(
									margen);
						}

						String[] lhAux = new String[lineas.length
								+ lineasHijo.length];
						for (int j = 0; j < lineas.length; j++) {
							lhAux[j] = lineas[j];
						}
						for (int j = lineas.length; j < lhAux.length; j++) {
							lhAux[j] = lineasHijo[j - lineas.length];
						}
						lineas = lhAux;
					}
				}
			}

			// Si tenemos salida visible, a�adimos su l�nea
			if ((this.salidaVisible || (this.esHistorico() && Conf.historia == 1))
					&& (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
				String lineaSalida = "";

				for (int i = 0; i < margen; i++) {
					lineaSalida = lineaSalida + " ";
				}

				if (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar
						&& !this.esHistorico() && !this.esMostradoEntero()) {
					if (Conf.idMetodoTraza) {
						String metodo = this.getNombreMetodo();
						if (PanelAlgoritmo.nyp != null) {
							metodo = PanelAlgoritmo.nyp.getPrefijo(this
									.getNombreMetodo());
						}
						lineaSalida = lineaSalida + "sale " + metodo
								+ ": return ?";
					} else {
						lineaSalida = lineaSalida + "sale: return ?";
					}
				} else {
					if (Conf.idMetodoTraza) {
						String metodo = this.getNombreMetodo();
						if (PanelAlgoritmo.nyp != null) {
							metodo = PanelAlgoritmo.nyp.getPrefijo(this
									.getNombreMetodo());
						}
						lineaSalida = lineaSalida + "sale " + metodo
								+ ": return " + this.salida.getRepresentacion();
					} else {
						lineaSalida = lineaSalida + "sale: return "
								+ this.salida.getRepresentacion();
					}
				}

				if (this.esHistorico()) {
					lineaSalida = lineaSalida + " <hist>";
				}

				if (this.iluminado()) {
					lineaSalida = lineaSalida + " <ilum>";
				}

				lineaSalida = lineaSalida + "?" + this.getNumMetodo() + "?";

				String[] lhAux = new String[lineas.length + 1];
				for (int j = 0; j < lineas.length; j++) {
					lhAux[j] = lineas[j];
				}
				lhAux[lineas.length] = lineaSalida;
				lineas = lhAux;
			}
			return lineas;
		} else {
			return new String[0];
		}
	}

	/**
	 * Devuelve la representacion de las lineas de traza con salida ligada a la
	 * entrada.
	 * 
	 * @param margen
	 *            Margen de la l�nea.
	 * 
	 * @return L�neas de traza.
	 */
	public String[] getLineasTrazaSalidaLigadaEntrada(int margen) {
		if (this.entradaVisible || this.salidaVisible || this.esHistorico()) {
			String[] lineas = new String[0];

			// Si tenemos entrada visible, a�adimos su l�nea
			if ((this.entradaVisible || (this.esHistorico() && Conf.historia == 1))
					&& (Conf.VISUALIZAR_ENTRADA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
				lineas = new String[1];

				lineas[0] = "";
				for (int i = 0; i < margen; i++) {
					lineas[0] = lineas[0] + " ";
				}

				if (Conf.idMetodoTraza) {
					String metodo = this.getNombreMetodo();
					if (PanelAlgoritmo.nyp != null) {
						metodo = PanelAlgoritmo.nyp.getPrefijo(this
								.getNombreMetodo());
					}
					lineas[0] = lineas[0] + "entra " + metodo + ": "
							+ this.entrada.getRepresentacionLineasTraza();
				} else {
					lineas[0] = lineas[0] + "entra: "
							+ this.entrada.getRepresentacionLineasTraza();
				}

				if (this.esHistorico()) {
					lineas[0] = lineas[0] + " <hist>";
				}

				if (this.iluminado()) {
					lineas[0] = lineas[0] + " <ilum>";
				}

				lineas[0] = lineas[0] + "?" + this.getNumMetodo() + "?";
			}

			// Si tenemos salida visible, a�adimos su l�nea
			if ((this.salidaVisible || (this.esHistorico() && Conf.historia == 1))
					&& (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar || Conf.VISUALIZAR_TODO == Conf.elementosVisualizar)) {
				String lineaSalida = "";

				for (int i = 0; i < margen; i++) {
					lineaSalida = lineaSalida + " ";
				}

				if (Conf.VISUALIZAR_SALIDA == Conf.elementosVisualizar
						&& !this.esHistorico() && !this.esMostradoEntero()) {
					if (Conf.idMetodoTraza) {
						String metodo = this.getNombreMetodo();
						if (PanelAlgoritmo.nyp != null) {
							metodo = PanelAlgoritmo.nyp.getPrefijo(this
									.getNombreMetodo());
						}
						lineaSalida = lineaSalida + "sale " + metodo
								+ ": return ?";
					} else {
						lineaSalida = lineaSalida + "sale: return ?";
					}
				} else {
					if (Conf.idMetodoTraza) {
						String metodo = this.getNombreMetodo();
						if (PanelAlgoritmo.nyp != null) {
							metodo = PanelAlgoritmo.nyp.getPrefijo(this
									.getNombreMetodo());
						}
						lineaSalida = lineaSalida + "sale " + metodo
								+ ": return " + this.salida.getRepresentacion();
					} else {
						lineaSalida = lineaSalida + "sale: return "
								+ this.salida.getRepresentacion();
					}
				}

				if (this.esHistorico()) {
					lineaSalida = lineaSalida + " <hist>";
				}

				if (this.iluminado()) {
					lineaSalida = lineaSalida + " <ilum>";
				}

				lineaSalida = lineaSalida + "?" + this.getNumMetodo() + "?";

				String[] lhAux = new String[lineas.length + 1];
				for (int j = 0; j < lineas.length; j++) {
					lhAux[j] = lineas[j];
				}
				lhAux[lineas.length] = lineaSalida;
				lineas = lhAux;
			}

			if (!this.contraido || (this.contraido && Conf.mostrarArbolSalto)) {
				// A�adimos l�neas de hijos, si los tiene y si no est� contraido
				for (int i = 0; i < this.hijos.size(); i++) {
					if (this.hijos.get(i) != null) // NO DEBER�A SER NECESARIA
					// ESTA COMPROBACI�N
					{
						String[] lineasHijo;
						if (Conf.sangrado) {
							lineasHijo = this.hijos.get(i)
									.getLineasTrazaSalidaLigadaEntrada(
											margen + 4);
						} else {
							lineasHijo = this.hijos.get(i)
									.getLineasTrazaSalidaLigadaEntrada(margen);
						}

						String[] lhAux = new String[lineas.length
								+ lineasHijo.length];
						for (int j = 0; j < lineas.length; j++) {
							lhAux[j] = lineas[j];
						}
						for (int j = lineas.length; j < lhAux.length; j++) {
							lhAux[j] = lineasHijo[j - lineas.length];
						}
						lineas = lhAux;
					}
				}
			}
			return lineas;
		} else {
			return new String[0];
		}
	}

	/**
	 * Devuelve la altura m�xima de la estructura de entre el registro de
	 * activaci�n y todos sus hijos.
	 * 
	 * @return Altura m�xima de la estructura.
	 */
	public int getMaxAlturaEstructura() {
		int altura = 0;

		Estructura e = new Estructura(this.entrada.getEstructura());

		// Calculo la m�a...
		if (e == null || e.getTipo() == null) {
			altura = 0;
		} else if (e.esArray()) {
			altura = 1;
		} else if (e.esMatriz()) {
			altura = e.dimensiones()[1];
		}

		// Despu�s la de mis hijos...
		for (int i = 0; i < this.numHijos(); i++) {
			altura = Math.max(altura, this.getHijo(i).getMaxAlturaEstructura());
		}

		return altura;
	}

	/**
	 * Devuelve la altura m�xima de los niveles del �rbol.
	 * 
	 * @return Altura m�xima de los niveles del �rbol.
	 */
	public int getMaxAltura() {
		int alturasHijos = 0;
		for (int i = 0; i < this.hijos.size(); i++) {
			if (this.hijos.get(i) != null) {
				if (this.hijos.get(i).getMaxAltura() > alturasHijos) {
					alturasHijos = this.hijos.get(i).getMaxAltura();
				}
			}
		}
		return (1 + alturasHijos);
	}

	/**
	 * Devuelve la representaci�n del estado de entrada.
	 * 
	 * @return Representaci�n del estado de entrada.
	 */
	public String getEntradaString() {
		return this.entrada.getRepresentacion();
	}

	/**
	 * Devuelve la representaci�n completa (Incluyendo nombres de par�metros)
	 * del estado de entrada.
	 * 
	 * @return Representaci�n completa del estado de entrada.
	 */
	public String getEntradaCompletaString() {
		return this.entrada.getRepresentacionCompleta(this.nombreParametros);
	}

	/**
	 * Devuelve los valores de los par�metros de entrada.
	 * 
	 * @return Valores de los par�metros de entrada.
	 */
	public String[] getEntradasString() {
		return this.entrada.getValoresStrings();
	}

	/**
	 * Devuelve la representaci�n del estado de salida.
	 * 
	 * @return Representaci�n del estado de salida.
	 */
	public String getSalidaString() {
		return this.salida.getRepresentacion();
	}

	/**
	 * Devuelve la representaci�n completa (Incluyendo nombres de par�metros)
	 * del estado de salida.
	 * 
	 * @return Representaci�n completa del estado de salida.
	 */
	public String getSalidaCompletaString() {
		if (this.devuelveValor) {
			return this.salida.getRepresentacionCompleta(null);
		} else {
			return this.salida.getRepresentacionCompleta(this.nombreParametros);
		}
	}

	/**
	 * Devuelve los valores de los par�metros de salida.
	 * 
	 * @return Valores de los par�metros de salida.
	 */
	public String[] getSalidasString() {
		return this.salida.getValoresStrings();
	}

	/**
	 * Devuelve el nombre del m�todo.
	 * 
	 * @return Nombre del m�todo.
	 */
	public String getNombreMetodo() {
		return this.nombreMetodo;
	}

	/**
	 * Devuelve el nombre de los par�metros del m�todo.
	 * 
	 * @return Nombre de los par�metros del m�todo.
	 */
	public String[] getNombreParametros() {
		return this.nombreParametros;
	}

	/**
	 * Establece el nombre del m�todo.
	 * 
	 * @param nombreMetodo
	 *            Nombre del m�todo.
	 */
	public void setNombreMetodo(String nombreMetodo) {
		this.nombreMetodo = nombreMetodo;
	}

	/**
	 * Establece el nombre de los par�metros.
	 * 
	 * @param nombreParametros
	 *            Nombre de los par�metros.
	 */
	public void setNombreParametros(String nombreParametros[]) {
		this.nombreParametros = nombreParametros;
	}

	/**
	 * A�ade un nuevo valor para un par�metro de entrada.
	 * 
	 * @param es
	 *            Valor para un nuevo par�metro de entrada.
	 */
	public void setEntradaString(String es) {
		if (this.entrada == null) {
			this.entrada = new Estado();
		}
		this.entrada.nuevoValorParametro(es);
	}

	/**
	 * A�ade un nuevo valor para un par�metro de salida.
	 * 
	 * @param ss
	 *            Valor para un nuevo par�metro de salida.
	 */
	public void setSalidaString(String ss) {
		if (this.salida == null) {
			this.salida = new Estado();
		}
		this.salida.nuevoValorParametro(ss);
	}

	/**
	 * A�ade un nuevo tipo de dato para un par�metro de entrada.
	 * 
	 * @param es
	 *            Tipo de dato para un nuevo par�metro de entrada.
	 */
	public void setEntradaClase(String es) {
		this.entrada.setClase(es);
	}

	/**
	 * A�ade un nuevo tipo de dato para un par�metro de salida.
	 * 
	 * @param ss
	 *            Tipo de dato para un nuevo par�metro de salida.
	 */
	public void setSalidaClase(String ss) {
		this.salida.setClase(ss);
	}

	/**
	 * A�ade un nuevo valor de dimension para un par�metro de entrada.
	 * 
	 * @param es
	 *            Valor de dimensi�n para un nuevo par�metro de entrada.
	 */
	public void setEntradaDim(int es) {
		this.entrada.setDim(es);
	}

	/**
	 * A�ade un nuevo valor de dimension para un par�metro de salida.
	 * 
	 * @param ss
	 *            Valor de dimensi�n para un nuevo par�metro de salida.
	 */
	public void setSalidaDim(int ss) {
		this.salida.setDim(ss);
	}

	/**
	 * Retorna el n�mero de hijos que tiene
	 * 
	 * @return n�mero de hijos que tiene
	 */
	public int numHijos() {
		return this.hijos.size();
	}

	/**
	 * Retorna el n�mero de hijos y subhijos que tiene
	 * 
	 * @return n�mero de hijos que tiene
	 */
	public int numHijosRec() {
		int numSubHijos = this.numHijos();
		for (int i = 0; i < this.numHijos(); i++) {
			numSubHijos = numSubHijos + this.getHijo(i).numHijosRec();
		}

		return numSubHijos;
	}

	/**
	 * Retorna el registro padre
	 * 
	 * @return registro padre
	 */
	public RegistroActivacion getPadre() {
		return this.padre;
	}

	/**
	 * Asigna el padre
	 * 
	 * @param p
	 *            registro padre
	 */
	public void setPadre(RegistroActivacion p) {
		this.padre = p;
	}

	/**
	 * Determina si el registro es mostrado entero o no
	 * 
	 * @return true si es mostrado entero, false en caso contrario.
	 */
	public boolean esMostradoEntero() {
		return this.mostradoEntero;
	}

	/**
	 * Establece si el registro es mostrado entero o no.
	 * 
	 * @param valor
	 *            true si es mostrado entero, false en caso contrario.
	 */
	public void setMostradoEntero(boolean valor) {
		this.mostradoEntero = valor;
	}

	/**
	 * Revisa si se puede aplicar un paso m�ltiple sobre un nodo
	 * 
	 * @return true si no se puede (es decir, true si el nodo actual ya se ve al
	 *         completo)
	 */
	public boolean actualyCompleto() {
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {
			return this.entradaVisible && this.salidaVisible;
		} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
			if (this.hijos.size() > 0) {
				return this.hijos.get(this.hijos.size() - 1).enteroVisible();
			} else {
				return this.entradaVisible;
			}
		} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
			if (this.hijos.size() > 0) {
				return this.hijos.get(this.hijos.size() - 1).enteroVisible();
			} else {
				return this.salidaVisible;
			}
		}

		return false;
	}

	/**
	 * Asigna un hijo. Este m�todo se utiliza s�lo para inicializaci�n desde
	 * fichero
	 * 
	 * @param h
	 *            registro de activaci�n hijo
	 */
	public void setHijo(RegistroActivacion h) {
		this.hijos.add(h);
	}

	/**
	 * Elimina un hijo.
	 * 
	 * @param i
	 *            Posici�n del hijo a eliminar.
	 */
	public void delHijo(int i) {
		this.hijos.remove(i);
	}

	/**
	 * Devuelve el valor del atributo historico
	 * 
	 * @return true si es hist�rico, false si no lo es
	 */
	public boolean esHistorico() {
		return this.historico;
	}

	/**
	 * Asigna el valor dado al atributo historico
	 * 
	 * @param h
	 *            true si es hist�rico, false en caso contrario.
	 */
	public void setHistorico(boolean h) {
		this.historico = h;
		if (h) {
			for (int i = 0; i < this.hijos.size(); i++) {
				this.hijos.get(i).setHistorico(h);
			}
		}
	}

	/**
	 * Asigna el valor dado al atributo historico para todos los hijos.
	 * 
	 * @param h
	 *            true si es hist�rico, false en caso contrario.
	 */
	public void setHistoricoHijos(boolean h) {
		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).setHistorico(h);
			this.hijos.get(i).setHistoricoHijos(h);
		}
	}

	/**
	 * Determina si el registro de activaci�n es ra�z del �rbol.
	 * 
	 * @return true si es ra�z, false en caso contrario.
	 */
	public boolean esRaiz() {
		return this.padre == null;
	}

	/**
	 * A�ade un estado de entrada
	 * 
	 * @param entrada
	 *            estado de entrada que se a�ade
	 * @param nombreMetodo
	 *            Nombre del m�todo
	 * @param nombreParametros
	 *            Nombre de los par�metros.
	 * 
	 * @return �ltimo hijo a�adido.
	 */
	public RegistroActivacion anadirEntrada(Estado entrada,
			String nombreMetodo, String nombreParametros[]) {
		this.hijos.add(new RegistroActivacion(this, entrada, nombreMetodo,
				nombreParametros));
		return this.hijos.get(this.hijos.size() - 1);
	}

	/**
	 * A�ade un estado de salida
	 * 
	 * @param salida
	 *            estado de salida que se a�ade
	 * 
	 * @return Registro de activaci�n padre.
	 */
	public RegistroActivacion anadirSalida(Estado salida) {
		this.salida = salida;
		return this.padre;
	}

	/**
	 * Permite copiar la informaci�n del registro de activaci�n actual en uno
	 * nuevo, asign�ndole un nuevo nodo padre.
	 * 
	 * @param padreNuevo
	 *            Nuevo padre.
	 * 
	 * @return Nuevo registro de activaci�n.
	 */
	protected RegistroActivacion copiar(RegistroActivacion padreNuevo) {
		RegistroActivacion ra = new RegistroActivacion();

		ra.entrada = this.getEntrada().copiar();
		ra.salida = this.getSalida().copiar();

		// ra.gemelo=this.gemelo;

		ra.padre = padreNuevo;
		ra.nID = this.nID;

		for (int i = 0; i < this.numHijos(); i++) {
			ra.hijos.add(this.getHijo(i).copiar(ra));
		}

		ra.metodoVisible = this.metodoVisible;
		ra.entradaVisible = this.entradaVisible;
		ra.salidaVisible = this.salidaVisible;

		ra.historico = this.historico;
		ra.mostradoEntero = this.mostradoEntero;

		ra.soyNodoActual = this.soyNodoActual;
		ra.soyCaminoActual = this.soyCaminoActual;

		ra.iluminar(this.iluminado);

		ra.numMetodo = this.numMetodo;
		ra.nombreMetodo = new String(this.nombreMetodo);
		ra.nombreParametros = new String[this.nombreParametros.length];
		for (int i = 0; i < this.nombreParametros.length; i++) {
			ra.nombreParametros[i] = new String(this.nombreParametros[i]);
		}

		ra.devuelveValor = this.devuelveValor;

		ra.contraido = this.contraido;
		ra.inhibido = this.inhibido;

		return ra;
	}

	/**
	 * Permite establecer una serie de valores sobre el registro de activaci�n.
	 * 
	 * @param nodoActual
	 *            Si es el nodo actual que se est� visualizando.
	 * @param caminoActual
	 *            Si pertenece al camino actual que se est� visualizando.
	 * @param mostradoEntero
	 *            Si el nodo se est� mostrando entero.
	 * @param historico
	 *            Si es un nodo hist�rico.
	 * @param eV
	 *            Si la entrada es visible.
	 * @param sV
	 *            Si la salida es visible.
	 */
	private void asignacion(boolean nodoActual, boolean caminoActual,
			boolean mostradoEntero, boolean historico, boolean eV, boolean sV) {
		this.setEsNodoActual(nodoActual);
		this.setEsCaminoActual(caminoActual);
		this.mostradoEntero = mostradoEntero;
		this.historico = historico;

		this.entradaVisible = eV;
		this.salidaVisible = sV;
	}

	/**
	 * Permite establecer una serie de valores sobre el registro de activaci�n y
	 * sobre sus hijos.
	 * 
	 * @param mostradoEntero
	 *            Si el nodo se est� mostrando entero.
	 * @param historico
	 *            Si es un nodo hist�rico.
	 * @param eV
	 *            Si la entrada es visible.
	 * @param sV
	 *            Si la salida es visible.
	 */
	private void asignacionR(boolean mostradoEntero, boolean historico,
			boolean eV, boolean sV) {
		this.setEsNodoActual(false);
		this.setEsCaminoActual(false);
		this.mostradoEntero = mostradoEntero;
		this.historico = historico;

		this.entradaVisible = eV;
		this.salidaVisible = sV;

		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).asignacionR(mostradoEntero, historico, eV, sV);
		}
	}

	/**
	 * Aplicado sobre un nodo, hace que sus hijos est�n completamente
	 * finalizados y que �l se muestre completo (pero sin estar finalizado; es
	 * decir, se muestra entero, es el nodo actual pero no es hist�rico (a�n))
	 */
	public void todoVisible() {
		this.asignacion(true, false, true, false, true, true);

		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).finalizar();
		}
	}

	/**
	 * Deja un nodo finalizado, as� como a todos sus hijos
	 */
	private void finalizar() {
		this.asignacion(false, false, true, true, true, true);

		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).finalizar();
		}
	}

	/**
	 * Hace que ning�n hijo sea visible
	 */
	public void nadaVisible() {
		this.asignacion(false, false, false, false, false, false);

		if (this.contraido) {
			for (int i = 0; i < this.hijos.size(); i++) {
				this.hijos.get(i).desinhibir();
			}
		}

		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).iniciar();
		}
	}

	/**
	 * Hace que ning�n hijo sea visible
	 */
	private void iniciar() {
		this.asignacion(false, false, false, false, false, false);

		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).desinhibir();
		}

		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).descontraer();
		}

		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).iniciar();
		}
	}

	/**
	 * Devuelve el n�mero de hijos visibles
	 * 
	 * @return n�mero de hijos visibles
	 */
	public int getHijosVisibles() {
		int numHijosVisibles = 0;
		for (int i = 0; i < this.hijos.size(); i++) {
			if (!this.hijos.get(i).vacioVisible()) {
				numHijosVisibles++;
			}
		}
		return numHijosVisibles;
	}

	/**
	 * Devuelve el n�mero de hijos visibles
	 * 
	 * @return n�mero de hijos visibles
	 */
	public int getHijosVisiblesPantalla() {
		if (this.contraido && !Conf.mostrarArbolSalto) {
			return 0;
		}

		int numHijosVisibles = 0;
		if (Conf.historia != 2) // Si hay que mostrar hist�ricos (coloreados o
		// atenuados)
		{
			for (int i = 0; i < this.hijos.size(); i++) {
				if (!this.hijos.get(i).vacioVisible()) {
					numHijosVisibles++;
				}
			}
		} else // Si no hay que mostrar hist�ricos
		{
			for (int i = 0; i < this.hijos.size(); i++) {
				if (!this.hijos.get(i).esHistorico()
						&& !this.hijos.get(i).vacioVisible()) {
					numHijosVisibles = 1;
				}
			}
		}

		return numHijosVisibles;
	}
	/**
	 * Devuelve los hijos 
	 * 
	 
	 * @return hijos
	 */
	public ArrayList<RegistroActivacion> getHijos() {
		
		return this.hijos;
		
	}
	/**
	 * Devuelve todos los nodos descendientes , dado un nodo 
	 * 
	 @param nodoInicial
	 			nodo padre
	 * @return hijos
	 */
	public ArrayList<RegistroActivacion> getHijosDescend(RegistroActivacion nodoInicial) {
		ArrayList<RegistroActivacion> hijosTotal= new ArrayList <RegistroActivacion>();
		ArrayList<RegistroActivacion> hijosActual= new ArrayList<RegistroActivacion>();
		hijosActual.addAll(nodoInicial.getHijos());
		while(hijosActual.size()!=0) {
			RegistroActivacion nodo = hijosActual.remove(0);
		
			if(!hijosTotal.contains(nodo)) {
				hijosTotal.add(nodo);
					if(nodo.getHijos()!=null)
				hijosActual.addAll(nodo.getHijos());
			}
		}
		return hijosTotal;
		
		//return hijosActual;
	}
	/**
	 * Devuelve el hijo i-�simo
	 * 
	 * @param i
	 *            n�mero de hijo que deseamos recuperar
	 * @return hijo i-�simo
	 */
	public RegistroActivacion getHijo(int i) {
		if (i < this.hijos.size()) {
			return this.hijos.get(i);
		} else {
			return null;
		}
	}

	/**
	 * Devuelve si el registro de activaci�n pertenece al nodo actual.
	 * 
	 * @return true si es nodo actual, false en caso contrario.
	 */
	public boolean getEsNodoActual() {
		return this.soyNodoActual;
	}

	/**
	 * Establece si el registro de activaci�n es el nodo actual.
	 * 
	 * @param valor
	 *            true si es nodo actual, false en caso contrario.
	 */
	public void setEsNodoActual(boolean valor) {
		this.soyNodoActual = valor;
	}

	/**
	 * Establece si el registro de activaci�n y todos sus hijos son nodos
	 * actuales.
	 * 
	 * @param valor
	 *            true si son nodos actuales, false en caso contrario.
	 */
	public void setEsNodoActualRec(boolean valor) {
		this.soyNodoActual = valor;
		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).setEsNodoActualRec(valor);
		}
	}

	/**
	 * Establece si el registro de activaci�n y todos sus hijos son parte del
	 * camino actual.
	 * 
	 * @param valor
	 *            true si son nodos del camino actual, false en caso contrario.
	 */
	public void setEsCaminoActualRec(boolean valor) {
		this.soyCaminoActual = valor;
		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).setEsCaminoActualRec(valor);
		}
	}

	/**
	 * Establece como nodos actual el �ltimo hijo que no tiene descendientes.
	 * 
	 * @param valor
	 *            true para establecer como nodo actual, false en caso
	 *            contrario.
	 */
	public void setEsNodoActualUltimoHijoRec(boolean valor) {
		if (this.hijos.size() > 0) {
			this.hijos.get(this.hijos.size() - 1).setEsNodoActualUltimoHijoRec(
					valor);
		} else {
			this.setEsNodoActual(valor);
		}
	}

	/**
	 * Contrae el �rbol, inhibiendo todos los hijos desde este nodo.
	 */
	public void contraer() {
		this.contraido = true;
		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).inhibir();
		}
	}

	/**
	 * Inhibe este nodo y todos sus hijos.
	 */
	private void inhibir() {
		this.inhibido = true;
		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).inhibir();
		}
	}

	/**
	 * Descontrae el �rbol, desinhibiendo todos sus hijos.
	 */
	private void descontraer() {
		this.contraido = false;
		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).desinhibir();
		}
	}

	/**
	 * Desinhibe este nodo y todos sus hijos.
	 */
	private void desinhibir() {
		this.inhibido = false;
		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).desinhibir();
		}
	}

	/**
	 * Hace que el siguiente hijo sea visible
	 */
	public void siguienteVisible() {

		// *****
		// Mostramos en la visualizacion valores de entrada Y salida
		// *****
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {

			if (depurar) {
				System.out.print("\nConf.VISUALIZAR_TODO siguienteVisible ");
			}
			// Si esta en fase de inicio
			if (!this.entradaVisible) {
				if (depurar) {
					System.out.println("<1>");
				}
				this.asignacion(true, false, false, false, true, false);
				this.setContraido(false);
			}
			// Si ya hemos mostrado todos los hijos o simplemente no tenemos
			else if (this.hijos.size() == 0
					|| this.hijos.get(this.hijos.size() - 1).esMostradoEntero()) {
				if (depurar) {
					System.out.println("<2>");
				}
				for (int i = 0; i < this.hijos.size(); i++) {
					this.hijos.get(i).finalizar();
				}
				this.asignacion(true, false, true, false, true, true);
			}
			// Si tenemos hijos y a�n no est�n mostrados del todo
			else {
				if (depurar) {
					System.out.println("<3>");
				}
				int i = 0;
				while (this.hijos.get(i).esMostradoEntero()) {
					i++;
				}
				if (i > 0) {
					this.hijos.get(i - 1).finalizar();
				}
				this.hijos.get(i).siguienteVisible();
				this.asignacion(false, true, false, false, true, false);
			}
		}
		// *****
		// Mostramos en la visualizacion valores de entrada O salida
		// *****
		else // if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA)
		{
			if (depurar) {
				System.out.print("\nConf.VISUALIZAR_ENTRADA siguienteVisible ");
			}
			// Si esta en fase de inicio con Conf.VISUALIZAR_ENTRADA
			if ((!this.entradaVisible && Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA)) {
				if (depurar) {
					System.out.println("<1>");
				}
				this.asignacion(true, false, this.hijos.size() == 0, false,
						true, this.hijos.size() == 0);// Conf.elementosVisualizar
				// ==
				// Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar
				// ==
				// Conf.VISUALIZAR_SALIDA);
				this.setContraido(false);
			} else if ((!this.salidaVisible && Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA)) {
				if (depurar) {
					System.out.println("<1b>");
				}
				this.asignacion(true, false, this.hijos.size() == 0, false,
						false, true);// Conf.elementosVisualizar ==
				// Conf.VISUALIZAR_ENTRADA,Conf.elementosVisualizar
				// == Conf.VISUALIZAR_SALIDA);
			}

			// Si nuestros hijos est�n ya totalmente visualizados
			else if (this.hijos.size() > 0
					&& this.hijos.get(this.hijos.size() - 1).esMostradoEntero()
					&& !this.hijos.get(this.hijos.size() - 1).esHistorico()) {
				if (depurar) {
					System.out.println("<2>");
				}
				this.hijos.get(this.hijos.size() - 1).finalizar();
				this.asignacion(
						true,
						false,
						true,
						false,
						/* true,true); */Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,
						Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
			}
			// Si tenemos hijos y a�n no est�n mostrados del todo
			else if (this.hijos.size() > 0) {
				if (depurar) {
					System.out.println("<3>");
				}
				int i = 0;
				while (this.hijos.get(i).esMostradoEntero()) {
					i++;
				}

				if (i > 0) {
					this.hijos.get(i - 1).finalizar();
				}
				if (i < this.hijos.size()) {
					this.hijos.get(i).siguienteVisible();
					this.asignacion(
							false,
							true,
							false,
							false,
							Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,
							Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
				}
			} else {
				if (depurar) {
					System.out.println("<4>");
				}
			}
		}
	}

	/**
	 * Hace que el �ltimo hijo que es visible deje de serlo
	 */
	public void anteriorVisible() {
		// *****
		// Mostramos en la visualizacion valores de entrada Y salida
		// *****
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {
			if (depurar) {
				System.out.print("Conf.VISUALIZAR_EyS anteriorVisible ");
			}

			// Si esta en fase de inicio
			if (this.salidaVisible) {
				if (depurar) {
					System.out.println("<1>");
				}
				this.asignacion(false, true, false, false, true, false);
				if (this.hijos.size() > 0
						&& (!this.contraido || (this.contraido && Conf.mostrarArbolSalto))) {
					if (depurar) {
						System.out.println("<1>1");
					}
					this.hijos.get(this.hijos.size() - 1).asignacion(true,
							false, true, false, true, true);
				} else {
					if (depurar) {
						System.out.println("<1>2");
					}
					for (int i = 0; i < this.hijos.size(); i++) {
						this.hijos.get(i).nadaVisible();
						this.hijos.get(i).asignacion(false, false, false,
								false, false, false);
					}
					this.asignacion(true, false, false, false, true, false);
				}
				if (this.contraido) {
					this.descontraer();
				}
			}
			// Si no tenemos hijos (mostrandose)
			else if (this.hijos.size() == 0 || this.hijos.get(0).vacioVisible()) {
				if (depurar) {
					System.out.println("<2>");
				}
				for (int i = 0; i < this.hijos.size(); i++) {
					this.hijos.get(i).iniciar();
				}
				this.asignacion(false, false, false, false, false, false);
				if (this.padre != null) {
					this.padre.setEsNodoActual(true);
				}
			}
			// Si tenemos hijos y a�n no est�n mostrados del todo
			else {
				if (depurar) {
					System.out.println("<3>");
				}
				int i = 0;
				while (i < this.hijos.size()
						&& !this.hijos.get(i).vacioVisible()) {
					i++;
				}
				i--;

				if (i > -1) {
					this.hijos.get(i).anteriorVisible();
				}
				if (this.hijos.get(i).vacioVisible()) {
					if (i > 0) {
						this.hijos.get(i - 1).asignacion(true, false, true,
								false, true, true);
						this.asignacion(false, true, false, false, true, false);
					} else {
						this.asignacion(true, false, false, false, true, false);
					}
				}

			}
		}
		// *****
		// Mostramos en la visualizacion valores de entrada O salida
		// *****
		else {
			if (depurar) {
				System.out.print("Conf.VISUALIZAR_EoS anteriorVisible ");
			}
			// Si este nodo es historico
			if (this.esHistorico()) {
				if (depurar) {
					System.out.println("<1>");
				}
				this.asignacion(true, false, true, false,
						Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,
						Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
			}
			// Si este nodo, con hijos, est� en estado mostradoEntero (es nodo
			// actual)
			else if (this.hijos.size() > 0
					&& this.hijos.get(this.hijos.size() - 1).esHistorico()) {
				if (depurar) {
					System.out.println("<2>");
				}
				if (!this.contraido
						|| (this.contraido && Conf.mostrarArbolSalto)) {
					this.hijos.get(this.hijos.size() - 1).anteriorVisible();
					this.asignacion(
							false,
							true,
							false,
							false,
							Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,
							Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
				} else {
					for (int i = 0; i < this.hijos.size(); i++) {
						this.hijos.get(i).nadaVisible();
					}
					this.asignacion(
							true,
							false,
							false,
							false,
							Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,
							Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
				}
				if (this.contraido()) {
					this.descontraer();
				}
			}
			// Si este nodo tiene el conjunto de hijos a medio visualizar
			else if (this.hijos.size() > 0 && !this.hijos.get(0).vacioVisible()) {
				if (depurar) {
					System.out.println("<3>");
				}
				int x = this.hijos.size() - 1;
				while (this.hijos.get(x).vacioVisible()) {
					x--;
				}
				if (x >= 0 && this.hijos.size() > 0) {
					if (depurar) {
						System.out.println("<3>1");
					}
					this.hijos.get(x).anteriorVisible();
					if (this.hijos.get(x).vacioVisible()) {
						if (depurar) {
							System.out.println("<3>2");
						}
						this.hijos.get(x).asignacion(false, false, false,
								false, false, false);
						if (x == 0) {
							if (depurar) {
								System.out.println("<3>2");
							}
							this.asignacion(
									true,
									false,
									false,
									false,
									Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,
									Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
						} else {
							if (depurar) {
								System.out.println("<3>3");
							}
							this.hijos
									.get(x - 1)
									.asignacion(
											true,
											false,
											true,
											false,
											Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,
											Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
							this.asignacion(
									false,
									true,
									false,
									false,
									Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,
									Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
						}
					}
				} else {
					if (depurar) {
						System.out.println("<3>4");
					}
					if (x >= 0) {
						if (depurar) {
							System.out.println("<3>5 x=" + x);
						}
						this.hijos.get(x).iniciar();
						if (x == 0) {
							if (depurar) {
								System.out.println("<3>6");
							}
							this.asignacion(
									true,
									false,
									false,
									false,
									Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,
									Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
						} else {
							if (depurar) {
								System.out.println("<3>7");
							}
							this.hijos
									.get(x - 1)
									.asignacion(
											true,
											false,
											true,
											false,
											Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,
											Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
						}
					} else // Este nodo tiene hijos pero todos estan
							// inicializados, �l debe ser borrado tambi�n
					{
						if (depurar) {
							System.out.println("<3>8");
						}
						this.iniciar();
					}
				}
			} else {
				if (depurar) {
					System.out.println("<4>");
				}
				this.iniciar();
			}
		}
	}

	/**
	 * Establece el nodo como contraido o no (para carga de traza desde XML)
	 * 
	 * @param b
	 *            true si contraido, false en caso contrario.
	 */
	public void setContraido(boolean b) {
		this.contraido = b;
	}

	/**
	 * Establece el nodo como inhibido o no (para carga de traza desde XML)
	 * 
	 * @param b
	 *            true si inhibido, false en caso contrario.
	 */
	public void setInhibido(boolean b) {
		this.inhibido = b;
	}

	/**
	 * Devuelve si el nodo est� inhibido o no.
	 * 
	 * @return true si est� inhibido, false en caso contrario.
	 */
	public boolean inhibido() {
		return this.inhibido;
	}

	/**
	 * Devuelve si el nodo est� contraido o no.
	 * 
	 * @return true si est� contraido, false en caso contrario.
	 */
	public boolean contraido() {
		return this.contraido;
	}

	/**
	 * Devuelve si el nodo est� iluminado o no.
	 * 
	 * @return true si est� iluminado, false en caso contrario.
	 */
	private boolean iluminado() {
		return this.iluminado;
	}

	/**
	 * Devuelve si el nodo contiene una estructura DYV.
	 * 
	 * @return true si contiene una estructura DYV, false en caso contrario.
	 */
	public boolean esDYV() {
		return (!(this.entrada.getEstructura() == null || this.entrada
				.getIndices() == null));
	}

	/**
	 * Mira que no se muestre totalmente y que ninguno de sus hijos est� visible
	 * ni siquiera parcialmente
	 * 
	 * @return true si no se muestra totalmente y que ninguno de los hijos est�
	 *         visible.
	 */
	public boolean estadoInicio() {
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
			if (this.numHijos() > 0) {
				return (this.entradaVisible() && this.hijos.get(0).entradaVisible == false);
			} else {
				return (this.entradaVisible());
			}
		} else if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
			if (this.numHijos() > 0) {
				return (this.salidaVisible() && this.hijos.get(0).salidaVisible == false);
			} else {
				return (this.salidaVisible());
			}
		} else {
			if (this.numHijos() > 0) {
				return (this.entradaVisible() && this.hijos.get(0).entradaVisible == false);
			} else {
				return (this.entradaVisible() && !this.salidaVisible());
			}
		}
	}

	/**
	 * Sobre un nodo del �rbol, muestra todos sus hijos y los �ltimos hijos de
	 * su �ltimo hijo, recursivamente
	 */
	private void aparecerHijosFinales() {
		if (this.hijos.size() > 0) {
			for (int i = 0; i < this.hijos.size(); i++) {

			}
			this.hijos.get(this.hijos.size() - 1).aparecerNodo();
			this.hijos.get(this.hijos.size() - 1).setHistorico(false);
			this.hijos.get(this.hijos.size() - 1).aparecerHijosFinales();
		}
	}

	/**
	 * Devuelve si la entrada es visible.
	 * 
	 * @return true si la entrada es visible, false en caso contrario.
	 */
	public boolean entradaVisible() {
		return this.entradaVisible;
	}

	/**
	 * Devuelve si la salida es visible.
	 * 
	 * @return true si la salida es visible, false en caso contrario.
	 */
	public boolean salidaVisible() {
		return this.salidaVisible;
	}

	/**
	 * Marca tanto la entrada como la salida visibles si la configuraci�n as� lo
	 * especifica.
	 */
	private void aparecerNodo() {
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA
				|| Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {
			this.entradaVisible = true;
		}
		if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA
				|| Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {
			this.salidaVisible = true;
		}
	}

	/**
	 * Comprueba si el nodo se muestra entero.
	 * 
	 * @return true si se muestra entero.
	 */
	public boolean enteroVisible() {
		return this.mostradoEntero;
	}

	/**
	 * Comprueba si el nodo tiene la visibilidad esperada dependiendo de los
	 * valores de configuraci�n.
	 * 
	 * @return true si tiene la visibilidad esperada.
	 */
	private boolean enteroVisibleSimple() {
		if (this.hijos.size() == 0) {
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA
					&& this.entradaVisible) {
				return true;
			}
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA
					&& this.salidaVisible) {
				return true;
			}
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
					&& this.entradaVisible && this.salidaVisible) {
				return true;
			}
		} else {
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
					&& this.entradaVisible && this.salidaVisible) {
				return true;
			}

			for (int i = 0; i < this.hijos.size(); i++) {
				if (!this.hijos.get(i).enteroVisibleSimple()) {
					return false;
				}
			}

			if (Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA
					&& this.entradaVisible) {
				return true;
			}
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA
					&& this.salidaVisible) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Comprueba si no es visible.
	 * 
	 * @return true si no es visible
	 */
	public boolean vacioVisible() {
		return (!this.entradaVisible && !this.salidaVisible);
	}

	/**
	 * Devuelve el nodo que se ha de mostrar visible cuando se pulsa el bot�n de
	 * avance m�ltiple
	 * 
	 * @return Registro de activaci�n que se ha de mostrar.
	 */
	public RegistroActivacion getNodoActual() {
		RegistroActivacion ra;

		if (this.soyNodoActual) {
			return this;
		}

		for (int i = 0; i < this.hijos.size(); i++) {
			ra = this.hijos.get(i).getNodoActual();
			if (ra != null) {
				return ra;
			}
		}
		return null;
	}

	/**
	 * Establece el nodo como parte del camino actual.
	 * 
	 * @param v
	 *            true si pertenece al camino actual, false en caso contrario.
	 */
	public void setEsCaminoActual(boolean v) {
		this.soyCaminoActual = v;
	}

	/**
	 * Devuelve si el nodo pertenece al camino actual.
	 * 
	 * @return true si pertenece al camino actual, false en caso contrario.
	 */
	public boolean getEsCaminoActual() {
		return this.soyCaminoActual;
	}

	/**
	 * Busca el nodo actual y deja su flag a false para que deje de ser el nodo
	 * actual.
	 * 
	 * @return true si el nodo pudo ser encontrado y actualizado, false en caso
	 *         contrario.
	 */
	private boolean borrarNodoActual() {
		if (this.getEsNodoActual()) {
			this.setEsNodoActual(false);
			return true;
		} else {
			boolean encontrado = false;
			int i = 0;
			while (i < this.numHijos() && !encontrado) {
				encontrado = this.getHijo(i).borrarNodoActual();
				i++;
			}
			return encontrado;
		}
	}

	/**
	 * Borra la se�alizacion de la rama que actualmente se est� expandiendo y
	 * que lleva al nodo activo
	 */
	public void borrarCaminoActual() {
		RegistroActivacion ra = this;

		while (ra.padre != null) {
			ra = ra.padre;
		}

		// Ahora 'ra' es la raiz del �rbol, vamos borrando hacia abajo
		if (ra != null) {
			ra.setEsCaminoActual(false);

			int i = 0;

			while (ra.hijos.size() != 0 && i < ra.getHijosVisibles()) {
				if (ra.getHijo(i).getEsCaminoActual()) {
					(ra.getHijo(i)).setEsCaminoActual(false);
					ra = ra.getHijo(i);
					i = 0;
				} else {
					i++;
				}
			}
		}
	}

	/**
	 * Establece el nodo como parte del camino actual, estableciendo as� todos
	 * los padres como parte del camino actual.
	 */
	public void crearCaminoActual() {
		RegistroActivacion ra = this;

		while (ra.padre != null) {
			ra = ra.padre;
		}

		// Ahora 'ra' es el nodo actual del �rbol
		ra = ra.getNodoActual();

		while (ra != null && ra.padre != null) {
			ra = ra.getPadre();
			ra.setEsCaminoActual(true);
		}
	}

	/**
	 * Corrige el camino actual en funci�n de la visibilidad de m�todos
	 */
	private void corregirCaminoActual() {
		// Presuponemos camino Actual coherente, ahora hacemos el camino
		// coherente con la visibilidad

		RegistroActivacion ra = this;
		if (!ra.getEsNodoActual()) {
			while (ra.padre != null) {
				ra = ra.padre;
				// Ahora 'ra' es la raiz del �rbol
			}

			ra = ra.getNodoActual();
		}
		// Ahora 'ra' es el nodo actual del �rbol
		while (ra != null && ra.padre != null) {
			ra = ra.padre;
			ra.setEsCaminoActual(true);
			ra.setVisibilidad(true, false, false);
		}

	}

	/**
	 * Sirve para pasar al modo de visualizaci�n de valores de entrada y salida
	 */
	public void visualizarEntradaYSalida() {
		if (this.hijos.size() == 0) {
			if (this.entradaVisible || this.salidaVisible) {
				this.entradaVisible = true;
				this.salidaVisible = true;
				this.mostradoEntero = true;
			}
		} else // tiene hijos
		{
			if (this.entradaVisible || this.salidaVisible) {
				if (this.hijos.get(this.hijos.size() - 1).esHistorico()) {
					this.entradaVisible = true;
					this.salidaVisible = true;
					this.mostradoEntero = true;
				} else {
					this.entradaVisible = true;
					this.salidaVisible = false;
					this.mostradoEntero = false;
				}
				for (int i = 0; i < this.hijos.size(); i++) {
					this.hijos.get(i).visualizarEntradaYSalida();
				}
			}
		}
	}

	/**
	 * Sirve para pasar al modo de visualizaci�n de valores s�lo de entrada
	 */
	public void visualizarSoloEntrada() {
		if ((this.entradaVisible || this.salidaVisible)) {
			this.entradaVisible = true;
			this.salidaVisible = false;
		}
		if ((this.hijos.size() == 0 || this.hijos.get(this.hijos.size() - 1)
				.esHistorico()) && (this.entradaVisible || this.salidaVisible)) {
			this.entradaVisible = true;
			this.salidaVisible = false;
			if ((this.hijos.size() == 0 || this.hijos
					.get(this.hijos.size() - 1).esHistorico())) {
				this.mostradoEntero = true;
			}
		}
		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).visualizarSoloEntrada();
		}
	}

	/**
	 * Sirve para pasar al modo de visualizaci�n de valores s�lo de salida
	 */
	public void visualizarSoloSalida() {
		if ((this.entradaVisible || this.salidaVisible)) {
			this.entradaVisible = false;
			this.salidaVisible = true;
		}

		if ((this.hijos.size() == 0 || this.hijos.get(this.hijos.size() - 1)
				.esHistorico()) && (this.entradaVisible || this.salidaVisible)) {
			this.entradaVisible = false;
			this.salidaVisible = true;
			if ((this.hijos.size() == 0 || this.hijos
					.get(this.hijos.size() - 1).esHistorico())) {
				this.mostradoEntero = true;
			}
		}
		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).visualizarSoloSalida();
		}

	}

	/**
	 * Devuelve el n�mero de nodos a partir de este nodo (incluido).
	 * 
	 * @return N�mero de nodos
	 */
	public int getNumNodos() {
		if (this.hijos.size() == 0) {
			return 1;
		} else {
			int x = 1;
			for (int i = 0; i < this.hijos.size(); i++) {
				if (this.hijos.get(i) != null) {
					x = x + this.hijos.get(i).getNumNodos();
				} else {
					System.out.println("hijo null");
				}
			}
			return x;
		}
	}

	/**
	 * Devuelve el n�mero de nodos visibles a partir de este nodo (incluido).
	 * 
	 * @return N�mero de nodos visibles
	 */
	public int getNumNodosVisibles() {
		if (this.entradaVisible() || this.salidaVisible()) {
			int numNodosVisibles = 1;
			for (int i = 0; i < this.numHijos(); i++) {
				numNodosVisibles = numNodosVisibles
						+ this.hijos.get(i).getNumNodosVisibles();
			}
			return numNodosVisibles;
		} else {
			return 0;
		}
	}

	/**
	 * Devuelve el n�mero de nodos hist�ricos a partir de este nodo (incluido).
	 * 
	 * @return N�mero de nodos hist�ricos
	 */
	public int getNumNodosHistoricos() {
		int numNodosHistoricos = 0;
		if (this.esHistorico()) {
			numNodosHistoricos++;
		}

		for (int i = 0; i < this.numHijos(); i++) {
			numNodosHistoricos = numNodosHistoricos
					+ this.hijos.get(i).getNumNodosHistoricos();
		}

		return numNodosHistoricos;
	}

	/**
	 * Devuelve el n�mero de nodos a�n no mostrados a partir de este nodo
	 * (incluido).
	 * 
	 * @return N�mero de nodos a�n no mostrados.
	 */
	public int getNumNodosAunNoMostrados() {
		int numNodosAunNoMostrados = 0;

		for (int i = 0; i < this.numHijos(); i++) {
			numNodosAunNoMostrados = numNodosAunNoMostrados
					+ this.hijos.get(i).getNumNodosAunNoMostrados();
		}

		if (!this.entradaVisible() && !this.salidaVisible()) {
			numNodosAunNoMostrados++;
		}
		return numNodosAunNoMostrados;
	}

	/**
	 * Devuelve el n�mero de nodos inhibidos a partir de este nodo (incluido).
	 * 
	 * @return N�mero de nodos inhibidos.
	 */
	public int getNumNodosInhibidos() {
		int numNodosInhibidos = 0;

		for (int i = 0; i < this.numHijos(); i++) {
			numNodosInhibidos = numNodosInhibidos
					+ this.hijos.get(i).getNumNodosInhibidos();
		}

		if (this.inhibido()) {
			numNodosInhibidos++;
		}
		return numNodosInhibidos;
	}

	/**
	 * Devuelve el n�mero de nodos iluminados a partir de este nodo (incluido).
	 * 
	 * @return N�mero de nodos iluminados.
	 */
	public int getNumNodosIluminados() {
		int numNodosIluminados = 0;

		for (int i = 0; i < this.numHijos(); i++) {
			numNodosIluminados = numNodosIluminados
					+ this.hijos.get(i).getNumNodosIluminados();
		}

		if (this.iluminado()) {
			numNodosIluminados++;
		}
		return numNodosIluminados;
	}

	/**
	 * Devuelve la m�xima longitud de la estructura del nodo.
	 * 
	 * @return M�xima longitud de la estructura del nodo.
	 */
	public int getMaximaLongitudCeldaEstructura() {
		int longMaxima = 0;

		if (this.esDYV()) {
			Estructura e1 = new Estructura(this.getEntrada().getEstructura());
			Estructura e2 = new Estructura(this.getSalida().getEstructura());
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
		for (int i = 0; i < this.numHijos(); i++) {
			longMaxima = Math.max(longMaxima, this.getHijo(i)
					.getMaximaLongitudCeldaEstructura());
		}

		return longMaxima;
	}

	/**
	 * Devuelve el valor par�metro de entrada dado por la posici�n especificada.
	 * 
	 * @param i
	 *            Posici�n del par�metro.
	 * 
	 * @return Valor del par�metro.
	 */
	private String getParametro(int i) {
		return this.entrada.getParametro(i);
	}

	/**
	 * Devuelve los valores de los par�metros de entrada.
	 * 
	 * @return Valores de los par�metros de entrada.
	 */
	public String[] getParametros() {
		return this.entrada.getParametros();
	}

	/**
	 * Devuelve los valores de los par�metros de salida.
	 * 
	 * @return Valores de los par�metros de salida.
	 */
	public String[] getResultado() {
		return this.salida.getParametros();
	}

	/**
	 * Devuelve una matriz con todos los valores para una interfaz de un m�todo
	 * de los par�metros de entrada para este nodo y todos sus hijos.
	 * 
	 * @param interfaz
	 *            Interfaz del m�todo para la que obtener los valores de los
	 *            par�metros.
	 * 
	 * @return Matriz en la que cada fila corresponde a cada una de las listas
	 *         de valores obtenidos.
	 */
	public String[][] getValoresParametrosInicio(String interfaz) {
		ArrayList<String[]> valores = this.getValoresParametros(interfaz);

		String[][] valoresArray = new String[valores.size()][];

		for (int i = 0; i < valores.size(); i++) {
			valoresArray[i] = valores.get(i);
		}

		return valoresArray;
	}

	private ArrayList<String[]> getValoresParametros(String interfaz) {
		ArrayList<String[]> valores = new ArrayList<String[]>();

		if (interfaz.equals(this.interfazMetodo()) && this.entradaVisible()) {
			valores.add(this.getParametros());
		}

		for (int i = 0; i < this.numHijos(); i++) {
			valores.addAll((this.getHijo(i).getValoresParametros(interfaz)));
		}

		return valores;
	}

	/**
	 * Devuelve una matriz con todos los valores para una interfaz de un m�todo
	 * de los par�metros de salida para este nodo y todos sus hijos.
	 * 
	 * @param interfaz
	 *            Interfaz del m�todo para la que obtener los valores de los
	 *            par�metros.
	 * 
	 * @return Matriz en la que cada fila corresponde a cada una de las listas
	 *         de valores obtenidos.
	 */
	public String[][] getValoresResultadoInicio(String interfaz) {
		ArrayList<String[]> valores = this.getValoresResultado(interfaz);

		String[][] valoresArray = new String[valores.size()][];

		for (int i = 0; i < valores.size(); i++) {
			valoresArray[i] = valores.get(i);
		}

		return valoresArray;
	}

	private ArrayList<String[]> getValoresResultado(String interfaz) {
		ArrayList<String[]> valores = new ArrayList<String[]>();

		if (interfaz.equals(this.interfazMetodo()) && this.salidaVisible()
				&& this.esMostradoEntero()) {
			valores.add(this.getResultado());
		}

		for (int i = 0; i < this.numHijos(); i++) {
			valores.addAll((this.getHijo(i).getValoresResultado(interfaz)));
		}

		return valores;
	}

	/**
	 * Devuelve el n�mero de nodos que se corresponden con un m�todo concreto.
	 * 
	 * @param interfaz
	 *            Interfaz del m�todo.
	 * 
	 * @return N�mero de nodos que se corresponden con un m�todo concreto.
	 */
	public int getNumNodosMetodo(String interfaz) {
		int nodos = 0;

		if (interfaz.equals(this.interfazMetodo())) {
			nodos++;
		}

		for (int i = 0; i < this.numHijos(); i++) {
			nodos = nodos + this.getHijo(i).getNumNodosMetodo(interfaz);
		}

		return nodos;
	}

	/**
	 * Devuelve la representaci�n en String de la interfaz del m�todo.
	 * 
	 * @return Interfaz del m�todo.
	 */
	public String interfazMetodo() {
		String interfaz = this.getNombreMetodo();

		String tiposParam[] = this.entrada.getClases();

		int dimParam[] = this.entrada.getDimensiones();

		interfaz = interfaz + " (";

		for (int i = 0; i < tiposParam.length; i++) {
			interfaz = interfaz + tiposParam[i];
			if (dimParam[i] > 0) {
				interfaz = interfaz + " "
						+ ServiciosString.cadenaDimensiones(dimParam[i]);
			}
			interfaz = interfaz + " " + this.nombreParametros[i];
			if (i < tiposParam.length - 1) {
				interfaz = interfaz + ", ";
			}
		}

		interfaz = interfaz + ")";
		return interfaz;
	}

	/**
	 * Obtiene todas las interfaces de los m�todos del nodo y de sus hijos.
	 * 
	 * @return Lista con todas las interfaces de los m�todos.
	 */
	public String[] interfacesMetodos() {
		String[] nombres = new String[1];

		nombres[0] = this.interfazMetodo();

		for (int i = 0; i < this.hijos.size(); i++) {
			String[] nombreshijo = this.hijos.get(i).interfacesMetodos();
			String[] nombresAux = new String[nombreshijo.length
					+ nombres.length];
			for (int j = 0; j < nombreshijo.length; j++) {
				nombresAux[j] = nombreshijo[j];
			}
			for (int j = 0; j < nombres.length; j++) {
				nombresAux[j + nombreshijo.length] = nombres[j];
			}

			nombres = nombresAux;
		}

		return nombres;
	}

	/**
	 * Obtiene todos los nombres de los m�todos del nodo y de sus hijos (Puede
	 * contener repetidos).
	 * 
	 * @return Lista con todos los nombres de los m�todos.
	 */
	private String[] nombresMetodos() {
		String[] nombres = new String[1];
		nombres[0] = this.getNombreMetodo();

		for (int i = 0; i < this.hijos.size(); i++) {
			String[] nombreshijo = this.hijos.get(i).nombresMetodos();
			String[] nombresAux = new String[nombreshijo.length
					+ nombres.length];
			for (int j = 0; j < nombreshijo.length; j++) {
				nombresAux[j] = nombreshijo[j];
			}
			for (int j = 0; j < nombres.length; j++) {
				nombresAux[j + nombreshijo.length] = nombres[j];
			}

			nombres = nombresAux;
		}

		return nombres;
	}

	/**
	 * Obtiene el n�mero de m�todos nodo y de sus hijos.
	 * 
	 * @return N�mero de m�todos.
	 */
	public int getNumMetodos() {
		String[] nombres = this.nombresMetodos();
		String[] nombresNR = this.quitarRepetidos(nombres);
		return nombresNR.length;
	}

	/**
	 * Obtiene todos los nombres de los m�todos del nodo.
	 * 
	 * @return Lista con todos los nombres de los m�todos.
	 */
	public String[] getNombresMetodos() {
		String[] nombres = this.nombresMetodos();
		return this.quitarRepetidos(nombres);
	}

	/**
	 * Elimina repetidos de la lista de nombres de m�todos proporcionada.
	 * 
	 * @return Lista sin repetidos.
	 */
	private String[] quitarRepetidos(String nombres[]) {
		String[] nombresSinRepeticion = new String[0];

		for (int i = 0; i < nombres.length; i++) {
			if (nombresSinRepeticion.length == 0) {
				nombresSinRepeticion = new String[1];
				nombresSinRepeticion[0] = nombres[i];
			} else {
				boolean encontrado = false;
				for (int j = 0; j < nombresSinRepeticion.length; j++) {
					if (nombresSinRepeticion[j].equals(nombres[i])) {
						encontrado = true;
					}
				}
				if (!encontrado) {
					String[] nombresSinRepeticionAux = new String[nombresSinRepeticion.length + 1];
					for (int j = 0; j < nombresSinRepeticion.length; j++) {
						nombresSinRepeticionAux[j] = nombresSinRepeticion[j];
					}
					nombresSinRepeticionAux[nombresSinRepeticion.length] = nombres[i];
					nombresSinRepeticion = nombresSinRepeticionAux;
				}
			}
		}
		return nombresSinRepeticion;
	}

	/**
	 * Devuelve el tipo de estructura m�s alta (normal - array - matriz) que
	 * contiene el nodo o sus hijos.
	 * 
	 * @return 1 si es array, 2 si es matriz, 0 en cualquier otro caso.
	 */
	public int tipoEstructura() {
		int miValor = 0;
		if (this.entrada.getEstructura() != null) {
			Estructura e = new Estructura(this.entrada.getEstructura());
			if (e != null) {
				if (e.esArray()) {
					miValor = 1;
				} else {
					miValor = 2;
				}
			}
		}

		if (miValor != 2) {
			for (int i = 0; i < this.numHijos(); i++) {
				miValor = Math.max(miValor, this.getHijo(i).tipoEstructura());
			}
		}

		return miValor;
	}

	/**
	 * Devuelve el Registro de Activaci�n final del camino actual.
	 * 
	 * @return Registro de Activaci�n final.
	 */
	private RegistroActivacion getFinalCaminoActual() {
		if (!this.soyCaminoActual) {
			return null;
		}

		if (this.soyCaminoActual && this.numHijos() == 0) {
			return this;
		}

		for (int i = 0; i < this.numHijos(); i++) {
			if (this.getHijo(i).getEsCaminoActual()) {
				return this.getHijo(i).getFinalCaminoActual();
			}
		}
		return this;

	}

	/**
	 * Devuelve el Registro de Activaci�n actual de entre todos los hijos del
	 * nodo.
	 * 
	 * @return Registro de activaci�n actual.
	 */
	protected RegistroActivacion getRegistroActivo() {
		if (this.getEsNodoActual()) {
			return this;
		}

		for (int i = 0; i < this.numHijos(); i++) {
			RegistroActivacion ractivo = this.getHijo(i).getRegistroActivo();
			if (ractivo != null) {
				return ractivo;
			}
		}

		return null;
	}

	/**
	 * Devuelve el Registro de Activaci�n con el id especificado de entre todos
	 * los hijos del nodo.
	 * 
	 * @param id
	 *            Id del registro de activaci�n.
	 * 
	 * @return Registro de activaci�n con el id especificado.
	 */
	protected RegistroActivacion getRegistroActivacionPorID(int id) {
		if (this.nID == id) {
			return this;
		}

		for (int i = 0; i < this.numHijos(); i++) {
			RegistroActivacion ra = this.getHijo(i).getRegistroActivacionPorID(
					id);
			if (ra != null) {
				return ra;
			}
		}
		return null;
	}

	/**
	 * Actualiza los valores del registro de activaci�n y de todos sus hijos con
	 * los mismos valores que tenga el registro de activaci�n de la traza con el
	 * mismo identificador que el actual.
	 * 
	 * @param traza
	 *            Traza de la que obtener el registro de activaci�n con el mismo
	 *            identificador.
	 */
	protected void actualizarEstadoFlagsDesdeGemelo(Traza traza) {
		RegistroActivacion ra = traza.getRegistroActivacionPorID(this.nID);

		if (ra != null) {
			this.asignacion(ra.soyNodoActual, ra.soyCaminoActual,
					ra.mostradoEntero, ra.historico, ra.entradaVisible,
					ra.salidaVisible);
			this.metodoVisible = ra.metodoVisible;
			this.contraido = ra.contraido;
			this.inhibido = ra.inhibido;
			this.iluminado = ra.iluminado;
		}

		for (int i = 0; i < this.numHijos(); i++) {
			this.getHijo(i).actualizarEstadoFlagsDesdeGemelo(traza);
		}
	}

	/**
	 * Corrige los distintos valores del registro de activaci�n y sus hijos
	 * despu�s de haber aplicado modificaciones para las que el �rbol necesita
	 * ser actualizado.
	 */
	protected void hacerCoherente() {
		RegistroActivacion actual = this.getNodoActual(); // Suponemos que this
		// es raiz

		this.corregirCaminoActual();
		this.revivirNodos(); // Nodos que estaban ocultos y que ahora son
		// visibles, se deben activar sus entradas y
		// salidas

		if (actual == null) {
			actual = this.getFinalCaminoActual();

			if (actual.numHijos() == 0) {
				actual.setEsCaminoActual(false);
				actual.setEsNodoActual(true);

			} else {
				int i = actual.numHijos() - 1;
				boolean asignado = false;
				while (!asignado && i >= 0) {
					if (actual.getHijo(i).entradaVisible()
							|| actual.getHijo(i).salidaVisible()) {
						asignado = true;
						actual.getHijo(i).setEsNodoActual(true);
						actual.getHijo(i).setHistorico(false); // nueva
					}
					i--;
				}
				if (!asignado && i < 0) {
					actual.setEsCaminoActual(false);
					actual.setEsNodoActual(true);
				}
			}
		} else {
			if (actual.esMostradoEntero()) {
				for (int i = 0; i < actual.numHijos(); i++) {
					actual.getHijo(i).asignacionR(true, true, true, true);
				}
			} else {
				if (actual.numHijos() != 0) {
					for (int i = 0; i < actual.numHijos(); i++) {
						actual.getHijo(i).setEsNodoActual(false);
						actual.getHijo(i).setEsCaminoActual(false);
					}
				} else if (Conf.elementosVisualizar != Conf.VISUALIZAR_TODO) {
					actual.setMostradoEntero(true);
				}
			}

		}

	}

	/**
	 * Permite reconfigurar nodos que estaban ocultos y que ahora son visibles,
	 * se deben activar sus entradas y salidas.
	 */
	private void revivirNodos() {
		if (this.entradaVisible == false && this.salidaVisible == false) {
			// Primero nos fijamos en padre: si est� completo visible, este nodo
			// tendra que estar completo visible
			if (this.padreVisible() == null
					|| this.padreVisible().esMostradoEntero()) {
				this.asignacion(false, false, true, true, true, true);
				this.asignacionR(true, true, true, true);
			}

			// Luego nos fijamos en hijos:
			else if (this.numHijos() > 0) {
				// Si hay algo visible...
				RegistroActivacion r = this.getPrimerHijoVisible();
				if (r != null) {
					if (this.getUltimoHijoVisible().esHistorico()) {
						this.asignacionR(true, true, true, true);
						this.asignacion(false, false, true, true, true, true);
					}
					this.entradaVisible = true;
				}
			}
		}
		for (int i = 0; i < this.numHijos(); i++) {
			if (this.getHijo(i).esHistorico()) {
				this.getHijo(i).asignacionR(true, true, true, true);
			} else {
				this.getHijo(i).revivirNodos();
			}
		}
	}

	/**
	 * Devuelve el padre m�s cercano visible.
	 * 
	 * @return Padre m�s cercano visible.
	 */
	private RegistroActivacion padreVisible() {
		RegistroActivacion ra = this.padre;
		while (ra != null && !ra.metodoVisible) {
			ra = ra.padre;
		}

		return ra;
	}

	/**
	 * Devuelve los nodos visibles enteros.
	 * 
	 * @return Lista de nodos visibles enteros.
	 */
	public ArrayList<RegistroActivacion> getRegistrosEnteros() {
		RegistroActivacion ra = this;

		while (ra.padre != null) {
			ra = ra.padre;
		}
		return ra.getRegistrosEnterosR();
	}

	private ArrayList<RegistroActivacion> getRegistrosEnterosR() {
		ArrayList<RegistroActivacion> registros = new ArrayList<RegistroActivacion>(
				0);

		if (this.numHijos() > 0) {
			for (int i = 0; i < this.numHijos(); i++) {
				registros.addAll(this.hijos.get(i).getRegistrosEnterosR());
			}
		}

		if (this.enteroVisibleSimple() && this.esMostradoEntero()) {
			registros.add(this);
		}

		return registros;
	}

	/**
	 * Devuelve los nodos que han sido al menos iniciados.
	 * 
	 * @return Lista de nodos al menos iniciados.
	 */
	public ArrayList<RegistroActivacion> getRegistrosIniciados() {
		RegistroActivacion ra = this;

		while (ra.padre != null) {
			ra = ra.padre;
		}

		return ra.getRegistrosIniciadosR();
	}

	private ArrayList<RegistroActivacion> getRegistrosIniciadosR() {
		ArrayList<RegistroActivacion> registros = new ArrayList<RegistroActivacion>(
				0);

		if (this.numHijos() > 0) {
			for (int i = 0; i < this.numHijos(); i++) {
				registros.addAll(this.hijos.get(i).getRegistrosIniciadosR());
			}
		}

		if ((!this.enteroVisibleSimple() || this.getEsNodoActual() || this
				.getEsCaminoActual())
				&& (this.entradaVisible || this.salidaVisible)) {
			registros.add(this);
		}

		return registros;
	}

	/**
	 * Establece los valores oportunos para los registros con metodos no
	 * visibles.
	 */
	public void asignarMetodosNoVisibles() {
		if (!this.metodoVisible) {
			this.asignacion(false, false, false, false, false, false);
		}
		for (int i = 0; i < this.numHijos(); i++) {
			this.getHijo(i).asignarMetodosNoVisibles();
		}
	}

	/**
	 * Establece los valores oportunos para los nodos que no estaban visibles, y
	 * pasan a estarlo.
	 */
	private void ajustarNodosVisibles() {
		int i = 0;

		if (this.getEsCaminoActual()) {
			this.asignacion(
					false,
					true,
					false,
					false,
					Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA
							|| Conf.elementosVisualizar == Conf.VISUALIZAR_TODO,
					Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);

			// Nos ocupamos de hijos que son parte del pasado
			while (i < this.numHijos()
					&& (!this.getHijo(i).getEsCaminoActual() && !this
							.getHijo(i).getEsNodoActual())) {
				this.getHijo(i).asignacionR(true, true, true, true);
				i++;
			}

			if (i < this.numHijos()
					&& (this.getHijo(i).getEsCaminoActual() || this.getHijo(i)
							.getEsNodoActual())) {
				this.getHijo(i).ajustarNodosVisibles();
				i++;
			}

			while (i < this.numHijos()) {
				this.getHijo(i).asignacionR(false, false, false, false);
				i++;
			}
		} else if (this.getEsNodoActual()) {
			this.asignacion(
					true,
					false,
					false/* Conf.elementosVisualizar != Conf.VISUALIZAR_TODO */,
					false,
					Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA
							|| Conf.elementosVisualizar == Conf.VISUALIZAR_TODO,
					Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA);
			for (i = 0; i < this.numHijos(); i++) {
				this.getHijo(i).asignacionR(false, false, false, false);
			}
		}
	}

	/**
	 * Devuelve el �ltimo hijo visible.
	 * 
	 * @return �ltimo hijo visible, null si no hay ninguno.
	 */
	private RegistroActivacion getUltimoHijoVisible() {
		RegistroActivacion r = null;

		for (int i = this.numHijos() - 1; i >= 0; i--) {
			r = this.getHijo(i).getUltimoHijoVisibleRec();
			if (r != null) {
				return r;
			}
		}

		return null;
	}

	/**
	 * Devuelve el n�mero de hijos visibles.
	 * 
	 * @return N�mero de hijos visibles.
	 */
	public int getNumHijosVisibles() {
		RegistroActivacion r = null;

		for (int i = 0; i < this.numHijos(); i++) {
			if (!this.getHijo(i).enteroVisible()) {
				return i;
			}
		}

		return this.numHijos();
	}

	private RegistroActivacion getUltimoHijoVisibleRec() {
		RegistroActivacion r = null;

		if (this.esMetodoVisible()
				&& (this.entradaVisible() || this.salidaVisible())) {
			return this;
		}

		for (int i = this.numHijos() - 1; i >= 0; i--) {
			r = this.getHijo(i).getUltimoHijoVisibleRec();
			if (r != null) {
				return r;
			}
		}

		return r;
	}

	/**
	 * Devuelve el primer hijo visible.
	 * 
	 * @return Primer hijo visible, null si no hay ninguno.
	 */
	private RegistroActivacion getPrimerHijoVisible() {
		RegistroActivacion r = null;

		for (int i = 0; i < this.numHijos(); i++) {
			r = this.getHijo(i).getPrimerHijoVisibleRec();
			if (r != null) {
				return r;
			}
		}

		return null;
	}

	private RegistroActivacion getPrimerHijoVisibleRec() {
		RegistroActivacion r = null;

		if (this.esMetodoVisible()
				&& (this.entradaVisible() || this.salidaVisible())) {
			return this;
		}

		for (int i = 0; i < this.numHijos(); i++) {
			r = this.getHijo(i).getPrimerHijoVisibleRec();
			if (r != null) {
				return r;
			}
		}

		return r;
	}

	/**
	 * Permite establecer el nodo como el nodo actual del �rbol.
	 */
	public void hacerNodoActual() {
		// Primero borramos camino actual
		RegistroActivacion ra = this;
		while (ra.padre != null) {
			ra = ra.padre;
		}

		ra.borrarCaminoActual();
		ra.borrarNodoActual();

		// Despu�s asignamos a this el nodoActual y creamos caminoActual
		this.setEsNodoActual(true);

		ra.crearCaminoActual();
		this.setEsNodoActual(true);
		this.asignacion(true, false, false, false,
				Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA
						|| Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA,
				false);

		for (int i = 0; i < this.numHijos(); i++) {
			this.getHijo(i).asignacionR(false, false, false, false);
		}

		// Hacemos coherente el estado del resto de nodos
		ra.ajustarNodosVisibles();
	}

	/**
	 * Permite marcar como iluminado o no el nodo.
	 * 
	 * @param valor
	 *            true para iluminar, false en caso contrario.
	 */
	public void iluminar(boolean valor) {
		this.iluminado = valor;
	}

	/**
	 * Permite marcar como resaltado el nodo.
	 * 
	 * @param valor
	 *            true para resaltar, false en caso contrario.
	 */
	public void resaltar(boolean valor) {
		this.resaltado = valor;
	}

	/**
	 * Permite Iluminar todos los nodos que se corresponden con los valores de
	 * entrada, y de salida de un m�todo.
	 * 
	 * @param numeroMetodo
	 *            Posici�n del m�todo a iluminar.
	 * @param valoresE
	 *            Valores de entrada del m�todo a iluminar.
	 * @param valoresS
	 *            Valores de salida del m�todo a iluminar.
	 * @param valor
	 *            true para iluminar, false para eliminar iluminaci�n.
	 * 
	 * @return N�mero de nodos iluminados.
	 */
	public int iluminar(int numeroMetodo, String valoresE[], String valoresS[],
			boolean valor) {
		int numNodosIluminados = 0;
		boolean candidatoValido = true;

		if (valoresE != null || valoresS != null) {
			if (this.numMetodo == numeroMetodo) {
				String[] valoresParam = null;
				if (valoresE != null) {
					valoresParam = this.entrada.getParametros();
					for (int i = 0; i < valoresParam.length; i++) {
						if (!((valoresE[i] == null || valoresE[i].length() == 0) || valoresParam[i]
								.equals(valoresE[i]))) {
							candidatoValido = false;
						}
					}
				}

				if (valoresS != null) {
					valoresParam = this.salida.getParametros();
					for (int i = 0; i < valoresParam.length; i++) {
						if (!((valoresS[i] == null || valoresS[i].length() == 0) || valoresParam[i]
								.equals(valoresS[i]))) {
							candidatoValido = false;
						}
					}
				}
				if (candidatoValido) {
					this.iluminado = valor;
					numNodosIluminados++;
				}
			}
		} else {
			this.iluminado = false;
			numNodosIluminados++;
		}
		for (int i = 0; i < this.numHijos(); i++) {
			numNodosIluminados = numNodosIluminados
					+ this.hijos.get(i).iluminar(numeroMetodo, valoresE,
							valoresS, valor);
		}

		return numNodosIluminados;
	}
	/**
	 * Permite devolver el numero de nodos que se corresponden con los valores de
	 * entrada, y de salida de un m�todo.
	 * 
	 * @param numeroMetodo
	 *            Posici�n del m�todo a iluminar.
	 * @param valoresE
	 *            Valores de entrada del m�todo a iluminar.
	 * @param valoresS
	 *            Valores de salida del m�todo a iluminar.
	
	 * @return N�mero de nodos iluminados.
	 */
	public int getRedundantes(int numeroMetodo, String valoresE[], String valoresS[]
			) {
		int numNodosIluminados = 0;
		boolean candidatoValido = true;

		if (valoresE != null || valoresS != null) {
			if (this.numMetodo == numeroMetodo) {
				String[] valoresParam = null;
				if (valoresE != null) {
					valoresParam = this.entrada.getParametros();
					for (int i = 0; i < valoresParam.length; i++) {
						if (!((valoresE[i] == null || valoresE[i].length() == 0) || valoresParam[i]
								.equals(valoresE[i]))) {
							candidatoValido = false;
						}
					}
				}

				if (valoresS != null) {
					valoresParam = this.salida.getParametros();
					for (int i = 0; i < valoresParam.length; i++) {
						if (!((valoresS[i] == null || valoresS[i].length() == 0) || valoresParam[i]
								.equals(valoresS[i]))) {
							candidatoValido = false;
						}
					}
				}
				if (candidatoValido) {
					
					numNodosIluminados++;
				}
			}
		} else {
			
			numNodosIluminados++;
		}
		for (int i = 0; i < this.numHijos(); i++) {
			numNodosIluminados = numNodosIluminados
					+ this.hijos.get(i).getRedundantes(numeroMetodo, valoresE,
							valoresS);
		}

		return numNodosIluminados;
	}

	/**
	 * Devuelve si el nodo est� iluminado o no.
	 * 
	 * @return true si est� iluminado, false en caso contrario.
	 */
	public boolean estaIluminado() {
		return this.iluminado;
	}

	/**
	 * Devuelve si el nodo est� resaltado o no.
	 * 
	 * @return true si est� resaltado, false en caso contrario.
	 */
	public boolean estaResaltado() {
		return this.resaltado;
	}

	/**
	 * Permite almacenar el �rbol en un fichero de texto.
	 * 
	 * @param fichero
	 *            Path al fichero.
	 */
	public void verArbol(String fichero) {
		if (this.padre == null) // S�lo si es raiz...
		{
			FileWriter fw = null;
			try {
				fw = new FileWriter(fichero);
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 1");
			}

			this.verArbol(fw, 0);

			try {
				fw.close();
			} catch (IOException ioe) {
				System.out.println("Error FileWriter 2");
			}
		}
	}

	private void verArbol(FileWriter fw, int margen) {
		String c = "";
		for (int i = 0; i < margen; i++) {
			c = c + " ";
		}

		try {
			fw.write(c + this.nombreMetodo + " [");
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
					|| Conf.elementosVisualizar == Conf.VISUALIZAR_ENTRADA) {
				for (int i = 0; i < this.getParametros().length; i++) {
					fw.write(" <" + this.getParametro(i) + "> ");
					// fw.write(this.entrada.getRepresentacion());
				}
			}
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO) {
				fw.write("|");
			}
			if (Conf.elementosVisualizar == Conf.VISUALIZAR_TODO
					|| Conf.elementosVisualizar == Conf.VISUALIZAR_SALIDA) {
				fw.write(this.salida.getRepresentacion());
			}
			fw.write("]");
			fw.write("  " + this.eval(this.entradaVisible) + "/"
					+ this.eval(this.salidaVisible) + " | Hist="
					+ this.eval(this.historico) + " | mEnt="
					+ this.eval(this.mostradoEntero) + " | nActual="
					+ this.eval(this.soyNodoActual) + " | cActual="
					+ this.eval(this.soyCaminoActual) + " | contr="
					+ this.eval(this.contraido) + " | inh="
					+ this.eval(this.inhibido) + " | metVisib="
					+ this.eval(this.esMetodoVisible()) + " | devuelve="
					+ this.eval(this.devuelveValor) + "\r\n");
		} catch (IOException ioe) {
			System.out.println("Error FileWriter 2");
		}

		for (int i = 0; i < this.hijos.size(); i++) {
			this.hijos.get(i).verArbol(fw, margen + 2);
		}
	}

	/**
	 * Transforma a String el valor booleano pasado.
	 * 
	 * @param valor
	 *            Valor booleano.
	 * 
	 * @return "V" si el valor pasado es true, "F" en caso contrario
	 */
	private String eval(boolean valor) {
		if (valor) {
			return "V";
		} else {
			return "F";
		}
	}

}