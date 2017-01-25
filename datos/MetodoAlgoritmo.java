package datos;

import java.util.ArrayList;

import toxml.JavaParser;
import utilidades.Arrays;
import utilidades.ServiciosString;

/**
 * Representa uno de los métodos que componen un algoritmo determinado.
 */
public class MetodoAlgoritmo {

	private static int codID = 0;

	private int id = 0;

	private String nombre;
	private String tipo;
	private int dimRetorno;

	private String paramNombre[];
	private String paramTipo[];
	private int paramDimension[]; // 0=valor único, 1=array, 2=matriz Es un
	// array, una posición para cada parámetro

	private int paramEstructura = 0; // Número del Parametro que alberga la
	// estructura de datos

	private int[] paramIndice = new int[4];
	// 0= Número del Parametro que alberga la fila mínima que determina la parte
	// sobre la que se actúa en cada llamada
	// 1= Número del Parametro que alberga la fila máxima que determina la parte
	// sobre la que se actúa en cada llamada
	// 2= Número del Parametro que alberga la col. mínima que determina la parte
	// sobre la que se actúa en cada llamada
	// 3= Número del Parametro que alberga la col. máxima que determina la parte
	// sobre la que se actúa en cada llamada

	private String paramValor[]; // Almacenamos el último valor que se empleó
	// con cada parámetro en la última
	// visualización de la actual sesión
	private boolean metodoPrincipal; // A true si el método fue el último que se
	// escogió como método principal

	private boolean visiblesEntrada[];
	private boolean visiblesSalida[];

	private boolean procesado; // true si fue seleccionado para ser procesado
	private boolean visto; // true si fue seleccionado en
	// "CuadroMetodosProcesados" para ser visto en la
	// visualización anterior

	private ArrayList<Integer> llamados = new ArrayList<Integer>(); // Metodos a
	// los que
	// se llama
	// desde
	// dentro de
	// este
	// método
	// (ID)

	private int tecnica = 0; // Tomará un valor en función de la técnica para la
	// que ha sido procesado (REC o DYV)

	public static final int TECNICA_REC = 1111;
	public static final int TECNICA_DYV = 1112;

	/**
	 * Permite construir una nueva instancia del objeto para la técina REC dada
	 * la información del método.
	 * 
	 * @param nombre
	 *            Nombre del método.
	 * @param tipo
	 *            Tipo de retorno del método.
	 * @param dimRetorno
	 *            Dimensiones del tipo de retorno.
	 * @param paramNombre
	 *            Nombre de los parámetros del método.
	 * @param paramTipo
	 *            Tipo de los parámetros del método.
	 * @param paramDim
	 *            Dimensiones de los parámetros del método.
	 */
	public MetodoAlgoritmo(String nombre, String tipo, int dimRetorno,
			String paramNombre[], String paramTipo[], int paramDim[]) {
		this(nombre, tipo, dimRetorno, paramNombre, paramTipo, paramDim,
				TECNICA_REC);
	}

	/**
	 * Permite construir una nueva instancia del objeto dada la información del
	 * método.
	 * 
	 * @param nombre
	 *            Nombre del método.
	 * @param tipo
	 *            Tipo de retorno del método.
	 * @param dimRetorno
	 *            Dimensiones del tipo de retorno.
	 * @param paramNombre
	 *            Nombre de los parámetros del método.
	 * @param paramTipo
	 *            Tipo de los parámetros del método.
	 * @param paramDim
	 *            Dimensiones de los parámetros del método.
	 * @param tecnica
	 *            Técnica utilizada TECNICA_REC o TECNICA_DYV
	 */
	public MetodoAlgoritmo(String nombre, String tipo, int dimRetorno,
			String paramNombre[], String paramTipo[], int paramDim[],
			int tecnica) {
		this.setNombre(nombre);
		this.tipo = tipo;
		this.dimRetorno = dimRetorno;
		this.paramNombre = paramNombre;
		this.paramTipo = paramTipo;

		this.paramDimension = new int[this.paramNombre.length];
		this.visiblesEntrada = new boolean[this.paramNombre.length];
		this.visiblesSalida = new boolean[this.tipo.equals("void") ? this.paramNombre.length
				: 1];

		this.paramValor = new String[this.paramNombre.length];

		for (int i = 0; i < this.paramDimension.length; i++) {
			this.paramDimension[i] = paramDim[i];
		}

		for (int i = 0; i < this.visiblesEntrada.length; i++) {
			this.visiblesEntrada[i] = true;
		}

		for (int i = 0; i < this.visiblesSalida.length; i++) {
			this.visiblesSalida[i] = true;
		}

		for (int i = 0; i < this.paramValor.length; i++) {
			this.paramValor[i] = "";
		}

		this.visto = true;

		this.id = codID;
		codID++;
	}

	/**
	 * Devuelve el nombre del método.
	 * 
	 * @return Nombre del método.
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Devuelve el tipo de retorno del método.
	 * 
	 * @return Tipo de retorno del método.
	 */
	public String getTipo() {
		return this.tipo;
	}

	/**
	 * Devuelve el número de dimensiones del tipo de retorno.
	 * 
	 * @return Número de dimensiones del tipo de retorno.
	 */
	public int getDimTipo() {
		return this.dimRetorno;
	}

	/**
	 * Devuelve el nombre de los parámetros.
	 * 
	 * @return Nombre de los parámetros.
	 */
	public String[] getNombreParametros() {
		return this.paramNombre;
	}

	/**
	 * Devuelve el nombre del parámetro dado por una posición concreta del
	 * parámetro.
	 * 
	 * @param i
	 *            Posición del parámetro.
	 * 
	 * @return Nombre del parámetro.
	 */
	public String getNombreParametro(int i) {
		if (i >= 0 && i < this.paramNombre.length) {
			return this.paramNombre[i];
		} else {
			return null;
		}
	}

	/**
	 * Devuelve el número de parámetros del método.
	 * 
	 * @return Número de parámetros del método.
	 */
	public int getNumeroParametros() {
		return this.paramNombre.length;
	}

	/**
	 * Devuelve los tipos de los parámetros del método.
	 * 
	 * @return Tipos de los parámetros del método.
	 */
	public String[] getTiposParametros() {
		return this.paramTipo;
	}

	/**
	 * Devuelve el tipo del parámetro dado por una posición concreta del
	 * parámetro.
	 * 
	 * @param i
	 *            Posición del parámetro.
	 * 
	 * @return Tipo del parámetro.
	 */
	public String getTipoParametro(int i) {
		if (i >= 0 && i < this.paramTipo.length) {
			return this.paramTipo[i];
		} else {
			return null;
		}
	}

	/**
	 * Devuelve las dimensiones del parámetro dado por una posición concreta del
	 * parámetro.
	 * 
	 * @param i
	 *            Posición del parámetro.
	 * 
	 * @return Dimensiones del parámetro.
	 */
	public int getDimParametro(int i) {
		if (i >= 0 && i < this.paramDimension.length) {
			return this.paramDimension[i];
		} else {
			return -1;
		}
	}

	/**
	 * Devuelve las dimensiones de los parámetros del método.
	 * 
	 * @return Dimensiones de los parámetros del método.
	 */
	public int[] getDimParametros() {
		return this.paramDimension;
	}

	/**
	 * Devuelve la visibilidad de los parámetros de entrada del método.
	 * 
	 * @return Visibilidad de los parámetros de entrada del método.
	 */
	public boolean[] getVisibilidadEntrada() {
		return this.visiblesEntrada;
	}

	/**
	 * Devuelve la visibilidad del parámetro de entrada dado por una posición
	 * concreta del parámetro.
	 * 
	 * @param i
	 *            Posición del parámetro.
	 * 
	 * @return Visibilidad del parámetro de entrada.
	 */
	public boolean getVisibilidadEntrada(int i) {
		return this.visiblesEntrada[i];
	}

	/**
	 * Devuelve la visibilidad de los parámetros de salida del método.
	 * 
	 * @return Visibilidad de los parámetros de salida del método.
	 */
	public boolean[] getVisibilidadSalida() {
		return this.visiblesSalida;
	}

	/**
	 * Devuelve la visibilidad del parámetro de salida dado por una posición
	 * concreta del parámetro.
	 * 
	 * @param i
	 *            Posición del parámetro.
	 * 
	 * @return Visibilidad del parámetro de salida.
	 */
	public boolean getVisibilidadSalida(int i) {
		return this.visiblesSalida[i];
	}

	/**
	 * Devuelve si el método ha sido procesado.
	 * 
	 * @return true si ha sido procesado, false en caso contrario.
	 */
	public boolean getMarcadoProcesar() {
		return this.procesado;
	}

	/**
	 * Devuelve si el método ha sido marcado como principal.
	 * 
	 * @return true si ha sido marcado como principal, false en caso contrario.
	 */
	public boolean getMarcadoPrincipal() {
		return this.metodoPrincipal;
	}

	/**
	 * Devuelve si el método ha sido marcado como visto.
	 * 
	 * @return true si ha sido marcado como visto, false en caso contrario.
	 */
	public boolean getMarcadoVisualizar() {
		return this.visto;
	}

	/**
	 * Devuelve una representación del método.
	 * 
	 * @return Representación del método
	 */
	public String getRepresentacion() {
		String cadenaEtiqueta = "";

		cadenaEtiqueta = this.getNombre() + " ( ";

		for (int j = 0; j < this.getNumeroParametros(); j++) {
			cadenaEtiqueta = cadenaEtiqueta + this.getTipoParametro(j);

			cadenaEtiqueta = cadenaEtiqueta
					+ ServiciosString
							.cadenaDimensiones(this.getDimParametro(j));

			if (j < this.getNumeroParametros() - 1) {
				cadenaEtiqueta = cadenaEtiqueta + " , ";
			} else {
				cadenaEtiqueta = cadenaEtiqueta + " ) ";
			}
		}

		cadenaEtiqueta = cadenaEtiqueta + " [ " + this.getTipo();
		cadenaEtiqueta = cadenaEtiqueta
				+ ServiciosString.cadenaDimensiones(this.getDimTipo());
		cadenaEtiqueta = cadenaEtiqueta + " ]";

		return cadenaEtiqueta;
	}

	/**
	 * Devuelve una representación del método, incluyendo los nombres de los
	 * parámetros.
	 * 
	 * @return Representación del método incluyendo nombres de parámetros.
	 */
	public String getRepresentacionTotal() {
		String cadenaEtiqueta = "";

		cadenaEtiqueta = this.getNombre() + " ( ";

		for (int j = 0; j < this.getNumeroParametros(); j++) {
			cadenaEtiqueta = cadenaEtiqueta + this.getTipoParametro(j);

			cadenaEtiqueta = cadenaEtiqueta
					+ ServiciosString
							.cadenaDimensiones(this.getDimParametro(j));

			cadenaEtiqueta = cadenaEtiqueta + " " + this.getNombreParametro(j);

			if (j < this.getNumeroParametros() - 1) {
				cadenaEtiqueta = cadenaEtiqueta + " , ";
			} else {
				cadenaEtiqueta = cadenaEtiqueta + " ) ";
			}
		}

		cadenaEtiqueta = cadenaEtiqueta + " [ " + this.getTipo();
		cadenaEtiqueta = cadenaEtiqueta
				+ ServiciosString.cadenaDimensiones(this.getDimTipo());
		cadenaEtiqueta = cadenaEtiqueta + " ]";

		return cadenaEtiqueta;
	}

	/**
	 * Devuelve los valores de los parámetros.
	 * 
	 * @return Valores de los parámetros.
	 */
	public String[] getParamValores() {
		return this.paramValor;
	}

	/**
	 * Devuelve el valor del parámetro dado por una posición concreta.
	 * 
	 * @param i
	 *            Posición del parámetro.
	 * 
	 * @return Valor del parámetro.
	 */
	public String getParamValor(int i) {
		return this.paramValor[i];
	}

	/**
	 * Devuelve la técnica que se está utilizando.
	 * 
	 * @return TECNICA_REC o TECNICA_DYV
	 */
	public int getTecnica() {
		return this.tecnica;
	}

	/**
	 * Devuelve el índice del parámetro que representa la estructura DYV.
	 * 
	 * @return Índice del parámetro que representa la estructura DYV.
	 */
	public int getIndiceEstructura() {
		return this.paramEstructura;
	}

	/**
	 * Devuelve el valor del índice que determina los límites de la estructura
	 * DYV.
	 * 
	 * @param i
	 *            0 = Número del Parametro que alberga la fila mínima que
	 *            determina la parte sobre la que se actúa en cada llamada, 1 =
	 *            Número del Parametro que alberga la fila máxima que determina
	 *            la parte sobre la que se actúa en cada llamada, 2 = Número del
	 *            Parametro que alberga la col. mínima que determina la parte
	 *            sobre la que se actúa en cada llamada, 3 = Número del
	 *            Parametro que alberga la col. máxima que determina la parte
	 *            sobre la que se actúa en cada llamada
	 * @return Valor del índice
	 */
	private int getIndice(int i) {
		if (i >= 0 && i < 4) {
			return this.paramIndice[i];
		} else {
			return -1;
		}
	}

	/**
	 * Devuelve el valor de los índices que determinan los límites de la
	 * estructura DYV.
	 * 
	 * @return Índices, cada posición contiene: 0 = Número del Parametro que
	 *         alberga la fila mínima que determina la parte sobre la que se
	 *         actúa en cada llamada, 1 = Número del Parametro que alberga la
	 *         fila máxima que determina la parte sobre la que se actúa en cada
	 *         llamada, 2 = Número del Parametro que alberga la col. mínima que
	 *         determina la parte sobre la que se actúa en cada llamada, 3 =
	 *         Número del Parametro que alberga la col. máxima que determina la
	 *         parte sobre la que se actúa en cada llamada
	 */
	public int[] getIndices() {
		int[] indices = new int[this.getNumIndices()];

		for (int i = 0; i < indices.length; i++) {
			indices[i] = this.getIndice(i);
		}

		return indices;
	}

	/**
	 * Devuelve el número de índices especificados que determinan la estructura
	 * DYV.
	 * 
	 * @return Número de índices especificados para la estructura DYV.
	 */
	public int getNumIndices() {
		int i = 0;
		int contados = 0;
		while (i < 4 && this.paramIndice[i] != -1) {
			contados++;
			i++;
		}
		return contados;
	}

	/**
	 * Devuelve el identificador generado para el médoto.
	 * 
	 * @return Identificador del método.
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Devuelve los identificadores de los métodos a los que se llama dentro de
	 * este método.
	 * 
	 * @return Identificadores de los métodos a los que se llama dentro de este
	 *         método.
	 */
	public int[] getMetodosLlamados() {
		int[] metLlamados = new int[this.llamados.size()];

		for (int i = 0; i < metLlamados.length; i++) {
			metLlamados[i] = this.llamados.get(i).intValue();
		}

		return metLlamados;
	}

	/**
	 * Añade un método como llamado dentro de este método.
	 * 
	 * @param n
	 *            Id del método llamado.
	 */
	public void setMetodoLlamado(int n) {
		for (Integer numero : this.llamados) {
			if (numero.intValue() == n) {
				return;
			}
		}

		this.llamados.add(new Integer(n));
	}

	/**
	 * Establece el nombre del método.
	 * 
	 * @param nombre
	 *            Nombre del método
	 */
	public void setNombre(String nombre) {
		this.nombre = JavaParser.convReverse(nombre);
	}

	/**
	 * Establece el tipo de retorno del método.
	 * 
	 * @param Tipo
	 *            de retorno del método.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Establece los nombres de los parámetros del método.
	 * 
	 * @param Nombres
	 *            de los parámetros del método.
	 */
	public void setNombresParametros(String[] paramNombre) {
		this.paramNombre = paramNombre;
	}

	/**
	 * Establece el tipo de los parámetros del método.
	 * 
	 * @param Tipo
	 *            de los parámetros del método.
	 */
	public void setTiposParametros(String[] paramTipo) {
		this.paramTipo = paramTipo;
	}

	/**
	 * Establece los valores de visibilidad para los parámetros de entrada del
	 * método.
	 * 
	 * @param Visibilidad
	 *            de los parámetros de entrada del método.
	 */
	public void setVisibilidadEntrada(boolean[] visiblesEntrada) {
		this.visiblesEntrada = visiblesEntrada;
	}

	/**
	 * Establece el valor de visibilidad para uno de los parámetros de entrada
	 * del método.
	 * 
	 * @param marcado
	 *            true para visible, false en caso contrario.
	 * @param i
	 *            Posición del parámetro de entrada.
	 */
	public void setVisibilidadEntrada(boolean marcado, int i) {
		this.visiblesEntrada[i] = marcado;
	}

	/**
	 * Establece el valor de visibilidad para uno de los parámetros de salida
	 * del método.
	 * 
	 * @param marcado
	 *            true para visible, false en caso contrario.
	 * @param i
	 *            Posición del parámetro de salida.
	 */
	public void setVisibilidadSalida(boolean marcado, int i) {
		this.visiblesSalida[i] = marcado;
	}

	/**
	 * Establece los valores de visibilidad para los parámetros de salida del
	 * método.
	 * 
	 * @param Visibilidad
	 *            de los parámetros de salida del método.
	 */
	public void setVisibilidadSalida(boolean[] visiblesSalida) {
		this.visiblesSalida = visiblesSalida;
	}

	/**
	 * Establece el método como marcado para procesar.
	 * 
	 * @param marcado
	 *            true para marcar para procesar, false en caso contrario.
	 */
	public void setMarcadoProcesar(boolean marcado) {
		this.procesado = marcado;
	}

	/**
	 * Establece el método como método principal del algoritmo.
	 * 
	 * @param marcado
	 *            true para principal, false en caso contrario.
	 */
	public void setMarcadoPrincipal(boolean marcado) {
		this.metodoPrincipal = marcado;
	}

	/**
	 * Marca el método para ser visualizado.
	 * 
	 * @param marcado
	 *            true para visualizar, false en caso contrario.
	 */
	public void setMarcadoVisualizar(boolean marcado) {
		this.visto = marcado;
	}

	/**
	 * Establece los valores de los parámetros del método.
	 * 
	 * @param valores
	 *            Valores de los parámetros del método.
	 */
	public void setParamValores(String valores[]) {
		this.paramValor = valores;
	}

	/**
	 * Establece el valor de uno de los parámetros del método.
	 * 
	 * @param i
	 *            Posición del parámetros.
	 * @param valor
	 *            Valor del parámetros.
	 */
	public void setParamValor(int i, String valor) {
		this.paramValor[i] = valor;
	}

	/**
	 * Establece la técnica de visualización del método.
	 * 
	 * @param tecnica
	 *            TECNICA_REC o TECNICA_DYV
	 */
	public void setTecnica(int tecnica) {
		this.tecnica = tecnica;
	}

	/**
	 * Establece el índice del parámetro que determina la estructura DYV,
	 * dejando los indices que delimitan las dimensiones de la estructura
	 * vacíos.
	 * 
	 * @param indiceEstructura
	 *            índice del parámetro que determina la estructura DYV.
	 */
	public void setIndices(int indiceEstructura) {
		this.setIndices(indiceEstructura, -1, -1, -1, -1);
	}

	/**
	 * Establece el índice del parámetro que determina la estructura DYV,
	 * permitiendo establecer los límites de la estructura a visualizar cuando
	 * es un array.
	 * 
	 * @param indiceEstructura
	 *            índice del parámetro que determina la estructura DYV.
	 * @param i1
	 *            Límite inferior del array.
	 * @param i2
	 *            Límite superior del array.
	 */
	public void setIndices(int indiceEstructura, int i1, int i2) {
		this.setIndices(indiceEstructura, i1, i2, -1, -1);
	}

	/**
	 * Establece el índice del parámetro que determina la estructura DYV,
	 * permitiendo establecer los límites de la estructura a visualizar cuando
	 * es una matriz.
	 * 
	 * @param indiceEstructura
	 *            índice del parámetro que determina la estructura DYV.
	 * @param i1
	 *            Límite inferior de la fila.
	 * @param i2
	 *            Límite superior de la fila.
	 * @param i3
	 *            Límite inferior de la columna.
	 * @param i4
	 *            Límite superior de la columna.
	 */
	public void setIndices(int indiceEstructura, int i1, int i2, int i3, int i4) {
		this.paramEstructura = indiceEstructura;
		this.paramIndice[0] = i1;
		this.paramIndice[1] = i2;
		this.paramIndice[2] = i3;
		this.paramIndice[3] = i4;
	}

	// Métodos de clase

	/**
	 * Permite recuperar un método de una lista de métodos dada cierta
	 * información sobre el método
	 * 
	 * @param listaMetodos
	 *            Lista de métodos.
	 * @param datos
	 *            datos[0]=nombre método, datos[1]=parametro 0...,
	 *            datos[datos.length-1]=tipo retorno o valor null, no se debe
	 *            tener en cuenta en este método este valor
	 * @param dim
	 *            Dimensiones de los parámetros.
	 * 
	 * @return Método correspondiente, o null si no se ha encontrado.
	 */
	public static MetodoAlgoritmo getMetodo(
			ArrayList<MetodoAlgoritmo> listaMetodos, String[] datos, int[] dim) {
		for (int i = 0; i < listaMetodos.size(); i++) {
			MetodoAlgoritmo m = listaMetodos.get(i);

			if (m.getNombre().equals(datos[0])
					&& (datos.length - 2) == m.getNumeroParametros())
			// Si el nombre es igual y tiene el mismo número de parámetros, nos
			// molestamos en mirar los tipos de los parámetros...
			{
				boolean todosIguales = true;

				for (int j = 0; j < m.getNumeroParametros(); j++) {
					if (!(m.getTipoParametro(j).equals(datos[j + 1]) && m
							.getDimParametro(j) == dim[j])) {
						todosIguales = false;
					}

				}

				if (todosIguales) {
					return m;
				}
			}

		}

		return null;
	}
	
	/**
	 * Obtiene recursivamente las técnicas de ejecución que utiliza un método y los métodos
	 * de los que hace uso, para devolver una lista con las técnicas de ejecución utilizadas en 
	 * un algoritmo.
	 * 
	 * @param c Clase del algoritmo.
	 * @param m Método del algoritmo.
	 * 
	 * @return Lista con las técnicas de ejecución utilizadas.
	 */
	public static int[] tecnicasEjecucion(ClaseAlgoritmo c, MetodoAlgoritmo m) {
		int[] ret = null;
		int[] metodosRecorridos = new int[0];

		int[] valorRetorno = tecnicasEjecucion(c, m, metodosRecorridos);

		if (valorRetorno[0] == 1 && valorRetorno[1] == 1) {
			ret = new int[2];
			ret[0] = TECNICA_REC;
			ret[1] = TECNICA_DYV;
		} else if (valorRetorno[0] == 1) {
			ret = new int[1];
			ret[0] = TECNICA_REC;
		} else if (valorRetorno[1] == 1) {
			ret = new int[1];
			ret[0] = TECNICA_DYV;
		}

		return ret;
	}

	private static int[] tecnicasEjecucion(ClaseAlgoritmo c, MetodoAlgoritmo m,
			int[] metodosRecorridos) {
		int[] retorno = new int[2]; // primer entero indica técnica REC, segundo
		// indica técnica DYV // 0=NO, 1=SI

		retorno[0] = (m.getTecnica() == TECNICA_REC ? 1 : 0);
		retorno[1] = (m.getTecnica() == TECNICA_DYV ? 1 : 0);

		if (!Arrays.contiene(m.getID(), metodosRecorridos)) {
			int[] idMetodosLlamados = m.getMetodosLlamados();

			for (int i = 0; i < idMetodosLlamados.length; i++) {
				if (!Arrays.contiene(idMetodosLlamados[i], metodosRecorridos)) {
					if (c.getMetodoID(idMetodosLlamados[i]).getTecnica() == TECNICA_REC) {
						retorno[0] = 1;
					} else {
						retorno[1] = 1;
					}

					metodosRecorridos = Arrays.insertar(idMetodosLlamados[i],
							metodosRecorridos);
				}

				int[] tecnicasHijos = tecnicasEjecucion(c,
						c.getMetodoID(idMetodosLlamados[i]), idMetodosLlamados);
				if (tecnicasHijos[0] == 1) {
					retorno[0] = 1;
				}
				if (tecnicasHijos[1] == 1) {
					retorno[1] = 1;
				}
			}
		}

		return retorno;

	}
	
	/**
	 * Método para obtener los métodos distintos llamados desde este método
	 * (Contando el mismo). 
	 * Si por ejemplo el método x llama a y este método devuelve 2
	 * Si por ejemplo el método x llama a x este método devuelve 1
	 * 
	 * @return
	 * 	Métodos distintos llamados desde este método incluyendo a él mismo
	 */
	public int metodosDistintosLlamados(){
		int identificadorPadre = this.getID();
		int contador = 1;
		for(int identificadorHijo : this.getMetodosLlamados()){
			if(identificadorHijo != identificadorPadre)
				contador++;
		}
		return contador;
	}

}
