package datos;

import java.util.ArrayList;

import toxml.JavaParser;
import utilidades.Arrays;
import utilidades.ServiciosString;

/**
 * Representa uno de los m�todos que componen un algoritmo determinado.
 */
public class MetodoAlgoritmo {

	private static int codID = 0;

	private int id = 0;

	private String nombre;
	private String tipo;
	private int dimRetorno;

	private String paramNombre[];
	private String paramTipo[];
	private int paramDimension[]; // 0=valor �nico, 1=array, 2=matriz Es un
	// array, una posici�n para cada par�metro

	private int paramEstructura = 0; // N�mero del Parametro que alberga la
	// estructura de datos

	private int[] paramIndice = new int[4];
	// 0= N�mero del Parametro que alberga la fila m�nima que determina la parte
	// sobre la que se act�a en cada llamada
	// 1= N�mero del Parametro que alberga la fila m�xima que determina la parte
	// sobre la que se act�a en cada llamada
	// 2= N�mero del Parametro que alberga la col. m�nima que determina la parte
	// sobre la que se act�a en cada llamada
	// 3= N�mero del Parametro que alberga la col. m�xima que determina la parte
	// sobre la que se act�a en cada llamada

	private String paramValor[]; // Almacenamos el �ltimo valor que se emple�
	// con cada par�metro en la �ltima
	// visualizaci�n de la actual sesi�n
	private boolean metodoPrincipal; // A true si el m�todo fue el �ltimo que se
	// escogi� como m�todo principal

	private boolean visiblesEntrada[];
	private boolean visiblesSalida[];

	private boolean procesado; // true si fue seleccionado para ser procesado
	private boolean visto; // true si fue seleccionado en
	// "CuadroMetodosProcesados" para ser visto en la
	// visualizaci�n anterior

	private ArrayList<Integer> llamados = new ArrayList<Integer>(); // Metodos a
	// los que
	// se llama
	// desde
	// dentro de
	// este
	// m�todo
	// (ID)

	private int tecnica = 0; // Tomar� un valor en funci�n de la t�cnica para la
	// que ha sido procesado (REC o DYV)

	public static final int TECNICA_REC = 1111;
	public static final int TECNICA_DYV = 1112;

	/**
	 * Permite construir una nueva instancia del objeto para la t�cina REC dada
	 * la informaci�n del m�todo.
	 * 
	 * @param nombre
	 *            Nombre del m�todo.
	 * @param tipo
	 *            Tipo de retorno del m�todo.
	 * @param dimRetorno
	 *            Dimensiones del tipo de retorno.
	 * @param paramNombre
	 *            Nombre de los par�metros del m�todo.
	 * @param paramTipo
	 *            Tipo de los par�metros del m�todo.
	 * @param paramDim
	 *            Dimensiones de los par�metros del m�todo.
	 */
	public MetodoAlgoritmo(String nombre, String tipo, int dimRetorno,
			String paramNombre[], String paramTipo[], int paramDim[]) {
		this(nombre, tipo, dimRetorno, paramNombre, paramTipo, paramDim,
				TECNICA_REC);
	}

	/**
	 * Permite construir una nueva instancia del objeto dada la informaci�n del
	 * m�todo.
	 * 
	 * @param nombre
	 *            Nombre del m�todo.
	 * @param tipo
	 *            Tipo de retorno del m�todo.
	 * @param dimRetorno
	 *            Dimensiones del tipo de retorno.
	 * @param paramNombre
	 *            Nombre de los par�metros del m�todo.
	 * @param paramTipo
	 *            Tipo de los par�metros del m�todo.
	 * @param paramDim
	 *            Dimensiones de los par�metros del m�todo.
	 * @param tecnica
	 *            T�cnica utilizada TECNICA_REC o TECNICA_DYV
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
	 * Devuelve el nombre del m�todo.
	 * 
	 * @return Nombre del m�todo.
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Devuelve el tipo de retorno del m�todo.
	 * 
	 * @return Tipo de retorno del m�todo.
	 */
	public String getTipo() {
		return this.tipo;
	}

	/**
	 * Devuelve el n�mero de dimensiones del tipo de retorno.
	 * 
	 * @return N�mero de dimensiones del tipo de retorno.
	 */
	public int getDimTipo() {
		return this.dimRetorno;
	}

	/**
	 * Devuelve el nombre de los par�metros.
	 * 
	 * @return Nombre de los par�metros.
	 */
	public String[] getNombreParametros() {
		return this.paramNombre;
	}

	/**
	 * Devuelve el nombre del par�metro dado por una posici�n concreta del
	 * par�metro.
	 * 
	 * @param i
	 *            Posici�n del par�metro.
	 * 
	 * @return Nombre del par�metro.
	 */
	public String getNombreParametro(int i) {
		if (i >= 0 && i < this.paramNombre.length) {
			return this.paramNombre[i];
		} else {
			return null;
		}
	}

	/**
	 * Devuelve el n�mero de par�metros del m�todo.
	 * 
	 * @return N�mero de par�metros del m�todo.
	 */
	public int getNumeroParametros() {
		return this.paramNombre.length;
	}

	/**
	 * Devuelve los tipos de los par�metros del m�todo.
	 * 
	 * @return Tipos de los par�metros del m�todo.
	 */
	public String[] getTiposParametros() {
		return this.paramTipo;
	}

	/**
	 * Devuelve el tipo del par�metro dado por una posici�n concreta del
	 * par�metro.
	 * 
	 * @param i
	 *            Posici�n del par�metro.
	 * 
	 * @return Tipo del par�metro.
	 */
	public String getTipoParametro(int i) {
		if (i >= 0 && i < this.paramTipo.length) {
			return this.paramTipo[i];
		} else {
			return null;
		}
	}

	/**
	 * Devuelve las dimensiones del par�metro dado por una posici�n concreta del
	 * par�metro.
	 * 
	 * @param i
	 *            Posici�n del par�metro.
	 * 
	 * @return Dimensiones del par�metro.
	 */
	public int getDimParametro(int i) {
		if (i >= 0 && i < this.paramDimension.length) {
			return this.paramDimension[i];
		} else {
			return -1;
		}
	}

	/**
	 * Devuelve las dimensiones de los par�metros del m�todo.
	 * 
	 * @return Dimensiones de los par�metros del m�todo.
	 */
	public int[] getDimParametros() {
		return this.paramDimension;
	}

	/**
	 * Devuelve la visibilidad de los par�metros de entrada del m�todo.
	 * 
	 * @return Visibilidad de los par�metros de entrada del m�todo.
	 */
	public boolean[] getVisibilidadEntrada() {
		return this.visiblesEntrada;
	}

	/**
	 * Devuelve la visibilidad del par�metro de entrada dado por una posici�n
	 * concreta del par�metro.
	 * 
	 * @param i
	 *            Posici�n del par�metro.
	 * 
	 * @return Visibilidad del par�metro de entrada.
	 */
	public boolean getVisibilidadEntrada(int i) {
		return this.visiblesEntrada[i];
	}

	/**
	 * Devuelve la visibilidad de los par�metros de salida del m�todo.
	 * 
	 * @return Visibilidad de los par�metros de salida del m�todo.
	 */
	public boolean[] getVisibilidadSalida() {
		return this.visiblesSalida;
	}

	/**
	 * Devuelve la visibilidad del par�metro de salida dado por una posici�n
	 * concreta del par�metro.
	 * 
	 * @param i
	 *            Posici�n del par�metro.
	 * 
	 * @return Visibilidad del par�metro de salida.
	 */
	public boolean getVisibilidadSalida(int i) {
		return this.visiblesSalida[i];
	}

	/**
	 * Devuelve si el m�todo ha sido procesado.
	 * 
	 * @return true si ha sido procesado, false en caso contrario.
	 */
	public boolean getMarcadoProcesar() {
		return this.procesado;
	}

	/**
	 * Devuelve si el m�todo ha sido marcado como principal.
	 * 
	 * @return true si ha sido marcado como principal, false en caso contrario.
	 */
	public boolean getMarcadoPrincipal() {
		return this.metodoPrincipal;
	}

	/**
	 * Devuelve si el m�todo ha sido marcado como visto.
	 * 
	 * @return true si ha sido marcado como visto, false en caso contrario.
	 */
	public boolean getMarcadoVisualizar() {
		return this.visto;
	}

	/**
	 * Devuelve una representaci�n del m�todo.
	 * 
	 * @return Representaci�n del m�todo
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
	 * Devuelve una representaci�n del m�todo, incluyendo los nombres de los
	 * par�metros.
	 * 
	 * @return Representaci�n del m�todo incluyendo nombres de par�metros.
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
	 * Devuelve los valores de los par�metros.
	 * 
	 * @return Valores de los par�metros.
	 */
	public String[] getParamValores() {
		return this.paramValor;
	}

	/**
	 * Devuelve el valor del par�metro dado por una posici�n concreta.
	 * 
	 * @param i
	 *            Posici�n del par�metro.
	 * 
	 * @return Valor del par�metro.
	 */
	public String getParamValor(int i) {
		return this.paramValor[i];
	}

	/**
	 * Devuelve la t�cnica que se est� utilizando.
	 * 
	 * @return TECNICA_REC o TECNICA_DYV
	 */
	public int getTecnica() {
		return this.tecnica;
	}

	/**
	 * Devuelve el �ndice del par�metro que representa la estructura DYV.
	 * 
	 * @return �ndice del par�metro que representa la estructura DYV.
	 */
	public int getIndiceEstructura() {
		return this.paramEstructura;
	}

	/**
	 * Devuelve el valor del �ndice que determina los l�mites de la estructura
	 * DYV.
	 * 
	 * @param i
	 *            0 = N�mero del Parametro que alberga la fila m�nima que
	 *            determina la parte sobre la que se act�a en cada llamada, 1 =
	 *            N�mero del Parametro que alberga la fila m�xima que determina
	 *            la parte sobre la que se act�a en cada llamada, 2 = N�mero del
	 *            Parametro que alberga la col. m�nima que determina la parte
	 *            sobre la que se act�a en cada llamada, 3 = N�mero del
	 *            Parametro que alberga la col. m�xima que determina la parte
	 *            sobre la que se act�a en cada llamada
	 * @return Valor del �ndice
	 */
	private int getIndice(int i) {
		if (i >= 0 && i < 4) {
			return this.paramIndice[i];
		} else {
			return -1;
		}
	}

	/**
	 * Devuelve el valor de los �ndices que determinan los l�mites de la
	 * estructura DYV.
	 * 
	 * @return �ndices, cada posici�n contiene: 0 = N�mero del Parametro que
	 *         alberga la fila m�nima que determina la parte sobre la que se
	 *         act�a en cada llamada, 1 = N�mero del Parametro que alberga la
	 *         fila m�xima que determina la parte sobre la que se act�a en cada
	 *         llamada, 2 = N�mero del Parametro que alberga la col. m�nima que
	 *         determina la parte sobre la que se act�a en cada llamada, 3 =
	 *         N�mero del Parametro que alberga la col. m�xima que determina la
	 *         parte sobre la que se act�a en cada llamada
	 */
	public int[] getIndices() {
		int[] indices = new int[this.getNumIndices()];

		for (int i = 0; i < indices.length; i++) {
			indices[i] = this.getIndice(i);
		}

		return indices;
	}

	/**
	 * Devuelve el n�mero de �ndices especificados que determinan la estructura
	 * DYV.
	 * 
	 * @return N�mero de �ndices especificados para la estructura DYV.
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
	 * Devuelve el identificador generado para el m�doto.
	 * 
	 * @return Identificador del m�todo.
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Devuelve los identificadores de los m�todos a los que se llama dentro de
	 * este m�todo.
	 * 
	 * @return Identificadores de los m�todos a los que se llama dentro de este
	 *         m�todo.
	 */
	public int[] getMetodosLlamados() {
		int[] metLlamados = new int[this.llamados.size()];

		for (int i = 0; i < metLlamados.length; i++) {
			metLlamados[i] = this.llamados.get(i).intValue();
		}

		return metLlamados;
	}

	/**
	 * A�ade un m�todo como llamado dentro de este m�todo.
	 * 
	 * @param n
	 *            Id del m�todo llamado.
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
	 * Establece el nombre del m�todo.
	 * 
	 * @param nombre
	 *            Nombre del m�todo
	 */
	public void setNombre(String nombre) {
		this.nombre = JavaParser.convReverse(nombre);
	}

	/**
	 * Establece el tipo de retorno del m�todo.
	 * 
	 * @param Tipo
	 *            de retorno del m�todo.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Establece los nombres de los par�metros del m�todo.
	 * 
	 * @param Nombres
	 *            de los par�metros del m�todo.
	 */
	public void setNombresParametros(String[] paramNombre) {
		this.paramNombre = paramNombre;
	}

	/**
	 * Establece el tipo de los par�metros del m�todo.
	 * 
	 * @param Tipo
	 *            de los par�metros del m�todo.
	 */
	public void setTiposParametros(String[] paramTipo) {
		this.paramTipo = paramTipo;
	}

	/**
	 * Establece los valores de visibilidad para los par�metros de entrada del
	 * m�todo.
	 * 
	 * @param Visibilidad
	 *            de los par�metros de entrada del m�todo.
	 */
	public void setVisibilidadEntrada(boolean[] visiblesEntrada) {
		this.visiblesEntrada = visiblesEntrada;
	}

	/**
	 * Establece el valor de visibilidad para uno de los par�metros de entrada
	 * del m�todo.
	 * 
	 * @param marcado
	 *            true para visible, false en caso contrario.
	 * @param i
	 *            Posici�n del par�metro de entrada.
	 */
	public void setVisibilidadEntrada(boolean marcado, int i) {
		this.visiblesEntrada[i] = marcado;
	}

	/**
	 * Establece el valor de visibilidad para uno de los par�metros de salida
	 * del m�todo.
	 * 
	 * @param marcado
	 *            true para visible, false en caso contrario.
	 * @param i
	 *            Posici�n del par�metro de salida.
	 */
	public void setVisibilidadSalida(boolean marcado, int i) {
		this.visiblesSalida[i] = marcado;
	}

	/**
	 * Establece los valores de visibilidad para los par�metros de salida del
	 * m�todo.
	 * 
	 * @param Visibilidad
	 *            de los par�metros de salida del m�todo.
	 */
	public void setVisibilidadSalida(boolean[] visiblesSalida) {
		this.visiblesSalida = visiblesSalida;
	}

	/**
	 * Establece el m�todo como marcado para procesar.
	 * 
	 * @param marcado
	 *            true para marcar para procesar, false en caso contrario.
	 */
	public void setMarcadoProcesar(boolean marcado) {
		this.procesado = marcado;
	}

	/**
	 * Establece el m�todo como m�todo principal del algoritmo.
	 * 
	 * @param marcado
	 *            true para principal, false en caso contrario.
	 */
	public void setMarcadoPrincipal(boolean marcado) {
		this.metodoPrincipal = marcado;
	}

	/**
	 * Marca el m�todo para ser visualizado.
	 * 
	 * @param marcado
	 *            true para visualizar, false en caso contrario.
	 */
	public void setMarcadoVisualizar(boolean marcado) {
		this.visto = marcado;
	}

	/**
	 * Establece los valores de los par�metros del m�todo.
	 * 
	 * @param valores
	 *            Valores de los par�metros del m�todo.
	 */
	public void setParamValores(String valores[]) {
		this.paramValor = valores;
	}

	/**
	 * Establece el valor de uno de los par�metros del m�todo.
	 * 
	 * @param i
	 *            Posici�n del par�metros.
	 * @param valor
	 *            Valor del par�metros.
	 */
	public void setParamValor(int i, String valor) {
		this.paramValor[i] = valor;
	}

	/**
	 * Establece la t�cnica de visualizaci�n del m�todo.
	 * 
	 * @param tecnica
	 *            TECNICA_REC o TECNICA_DYV
	 */
	public void setTecnica(int tecnica) {
		this.tecnica = tecnica;
	}

	/**
	 * Establece el �ndice del par�metro que determina la estructura DYV,
	 * dejando los indices que delimitan las dimensiones de la estructura
	 * vac�os.
	 * 
	 * @param indiceEstructura
	 *            �ndice del par�metro que determina la estructura DYV.
	 */
	public void setIndices(int indiceEstructura) {
		this.setIndices(indiceEstructura, -1, -1, -1, -1);
	}

	/**
	 * Establece el �ndice del par�metro que determina la estructura DYV,
	 * permitiendo establecer los l�mites de la estructura a visualizar cuando
	 * es un array.
	 * 
	 * @param indiceEstructura
	 *            �ndice del par�metro que determina la estructura DYV.
	 * @param i1
	 *            L�mite inferior del array.
	 * @param i2
	 *            L�mite superior del array.
	 */
	public void setIndices(int indiceEstructura, int i1, int i2) {
		this.setIndices(indiceEstructura, i1, i2, -1, -1);
	}

	/**
	 * Establece el �ndice del par�metro que determina la estructura DYV,
	 * permitiendo establecer los l�mites de la estructura a visualizar cuando
	 * es una matriz.
	 * 
	 * @param indiceEstructura
	 *            �ndice del par�metro que determina la estructura DYV.
	 * @param i1
	 *            L�mite inferior de la fila.
	 * @param i2
	 *            L�mite superior de la fila.
	 * @param i3
	 *            L�mite inferior de la columna.
	 * @param i4
	 *            L�mite superior de la columna.
	 */
	public void setIndices(int indiceEstructura, int i1, int i2, int i3, int i4) {
		this.paramEstructura = indiceEstructura;
		this.paramIndice[0] = i1;
		this.paramIndice[1] = i2;
		this.paramIndice[2] = i3;
		this.paramIndice[3] = i4;
	}

	// M�todos de clase

	/**
	 * Permite recuperar un m�todo de una lista de m�todos dada cierta
	 * informaci�n sobre el m�todo
	 * 
	 * @param listaMetodos
	 *            Lista de m�todos.
	 * @param datos
	 *            datos[0]=nombre m�todo, datos[1]=parametro 0...,
	 *            datos[datos.length-1]=tipo retorno o valor null, no se debe
	 *            tener en cuenta en este m�todo este valor
	 * @param dim
	 *            Dimensiones de los par�metros.
	 * 
	 * @return M�todo correspondiente, o null si no se ha encontrado.
	 */
	public static MetodoAlgoritmo getMetodo(
			ArrayList<MetodoAlgoritmo> listaMetodos, String[] datos, int[] dim) {
		for (int i = 0; i < listaMetodos.size(); i++) {
			MetodoAlgoritmo m = listaMetodos.get(i);

			if (m.getNombre().equals(datos[0])
					&& (datos.length - 2) == m.getNumeroParametros())
			// Si el nombre es igual y tiene el mismo n�mero de par�metros, nos
			// molestamos en mirar los tipos de los par�metros...
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
	 * Obtiene recursivamente las t�cnicas de ejecuci�n que utiliza un m�todo y los m�todos
	 * de los que hace uso, para devolver una lista con las t�cnicas de ejecuci�n utilizadas en 
	 * un algoritmo.
	 * 
	 * @param c Clase del algoritmo.
	 * @param m M�todo del algoritmo.
	 * 
	 * @return Lista con las t�cnicas de ejecuci�n utilizadas.
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
		int[] retorno = new int[2]; // primer entero indica t�cnica REC, segundo
		// indica t�cnica DYV // 0=NO, 1=SI

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
	 * M�todo para obtener los m�todos distintos llamados desde este m�todo
	 * (Contando el mismo). 
	 * Si por ejemplo el m�todo x llama a y este m�todo devuelve 2
	 * Si por ejemplo el m�todo x llama a x este m�todo devuelve 1
	 * 
	 * @return
	 * 	M�todos distintos llamados desde este m�todo incluyendo a �l mismo
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
