package datos;

import utilidades.ServiciosString;

/**
 * Esta clase almacena globalmente los datos de la ejecuci�n: llamadas, valores,
 * par�metros, ...
 * 
 * @author Luis Fern�ndez y Antonio P�rez Carrasco
 * @version 2006-2007
 */
public class Traza {

	private static Traza traza = new Traza();

	private RegistroActivacion raiz = null;
	private RegistroActivacion ultimo = null;

	private String idTraza;

	private String archivo = "";
	private String metodoEjecucion; // Almacena el metodo cuya ejecuci�n se
	// lanz�

	private String titulo; // Sirve de titulo para el panel que contiene las
	// vistas

	private int[] tecnicas = null;

	/**
	 * Constructor: crea una nueva instancia de la traza.
	 */
	public Traza() {
		RegistroActivacion.reinicializar();
	}

	/**
	 * Devuelve la raiz de la traza
	 * 
	 * @return la raiz de la traza
	 */
	public RegistroActivacion getRaiz() {
		return this.raiz;
	}

	/**
	 * Devuelve el �ltimo elemento de la traza
	 * 
	 * @return el �ltimo elemento de la traza
	 */
	public RegistroActivacion getUltimo() {
		return this.ultimo;
	}

	/**
	 * Devuelve la instancia �nica de la traza de ejecuci�n
	 * 
	 * @return la traza
	 */
	public static Traza singleton() {
		return Traza.traza;
	}

	/**
	 * Devuelve el identificador de la traza.
	 * 
	 * @return Identificador de la traza.
	 */
	public String getIDTraza() {
		return this.idTraza;
	}

	/**
	 * Permite establecer el identificador de la traza.
	 * 
	 * @param id
	 *            Identificador de la traza.
	 */
	public void setIDTraza(String id) {
		this.idTraza = id;
	}

	/**
	 * Permite establecer las t�cnicas que se usan en la visualizaci�n de la
	 * traza.
	 * 
	 * @param tecnicas
	 *            Lista de t�cnicas: 
	 *            	MetodoAlgoritmo.TECNICA_REC,
	 *            	MetodoAlgoritmo.TECNICA_DYV y
	 *            	MetodoAlgoritmo.TECNICA_AABB
	 */
	public void setTecnicas(int[] tecnicas) {
		this.tecnicas = tecnicas;
	}

	/**
	 * Permite obtener las t�cnicas que se usan en la visualizaci�n de la traza.
	 * 
	 * @return Lista de t�cnicas: 
	 *            MetodoAlgoritmo.TECNICA_REC,
	 *            MetodoAlgoritmo.TECNICA_DYV y
	 *            MetodoAlgoritmo.TECNICA_AABB
	 */
	public int[] getTecnicas() {
		return this.tecnicas;
	}

	/**
	 * A�ade una entrada a la traza
	 * 
	 * @param estado Estado a incluir como entrada
	 * @param nombreMetodo Nombre del m�todo
	 * @param nombreParametros Nombre de los distintos par�metros.
	 */
	public void anadirEntrada(Estado estado, String nombreMetodo,
			String nombreParametros[]) {
		if (this.raiz == null) {
			this.raiz = new RegistroActivacion(estado, nombreMetodo,
					nombreParametros);
			this.ultimo = this.raiz;
		} else {
			this.ultimo = this.ultimo.anadirEntrada(estado, nombreMetodo,
					nombreParametros);
		}
	}

	/**
	 * A�ade una salida a la traza
	 * 
	 * @param estado Estado a incluir como salida
	 * @param devuelveValor true si la salida devuelve un valor, false en caso contrario.
	 */
	public void anadirSalida(Estado estado, boolean devuelveValor) {
		this.ultimo.setDevuelveValor(devuelveValor);
		this.ultimo = this.ultimo.anadirSalida(estado);
	}

	/**
	 * A�ade una salida a la traza
	 * 
	 * @param estado Estado a incluir como salida
	 * @param nombreMetodo Nombre del m�todo
	 * @param nombreParametros Nombre de los distintos par�metros.
	 * @param devuelveValor true si la salida devuelve un valor, false en caso contrario.
	 */
	public void anadirSalida(Estado estado, String nombreMetodo,
			String nombreParametros[], boolean devuelveValor) {
		this.ultimo.setDevuelveValor(devuelveValor);
		this.ultimo = this.ultimo.anadirSalida(estado);
	}

	/**
	 * A�ade una salida a la traza
	 * 
	 * @param estado Estado a incluir como salida
	 * @param nombreMetodo Nombre del m�todo
	 * @param nombreParametros Nombre de los distintos par�metros.
	 */
	public void anadirSalida(Estado estado, String nombreMetodo,
			String nombreParametros[]) {
		this.ultimo.setDevuelveValor(false);
		this.ultimo = this.ultimo.anadirSalida(estado);
	}

	/**
	 * Establece el nombre del m�todo en ejecuci�n.
	 * 
	 * @param metodoEjecucion
	 *            Nombre del m�todo en ejecuci�n.
	 */
	public void setNombreMetodoEjecucion(String metodoEjecucion) {
		this.metodoEjecucion = metodoEjecucion;
	}

	/**
	 * Devuelve el nombre del m�todo en ejecuci�n
	 * 
	 * @return Nombre del m�todo en ejecuci�n.
	 */
	public String getNombreMetodoEjecucion() {
		return this.metodoEjecucion;
	}

	/**
	 * Devuelve la representaci�n de las lineas de traza.
	 * 
	 * @return Representaci�n de las lineas de traza.
	 */
	public String[] getLineasTraza() {
		return this.getRaiz().getLineasTraza(4);
	}

	/**
	 * Devuelve la representaci�n de las lineas de traza con salida ligada a
	 * entrada.
	 * 
	 * @return Representaci�n de las lineas de traza con salida ligada a
	 *         entrada.
	 */
	public String[] getLineasTrazaSalidaLigadaEntrada() {
		return this.getRaiz().getLineasTrazaSalidaLigadaEntrada(4);
	}

	/**
	 * Permite establecer el archivo correspondiente a la traza.
	 * 
	 * @param s
	 *            Archivo correspondiente a la traza.
	 */
	public void setArchivo(String s) {
		this.archivo = s;
	}

	/**
	 * Devuelve el archivo correspondiente a la traza.
	 * 
	 * @return Archivo correspondiente a la traza.
	 */
	public String getArchivo() {
		return this.archivo;
	}

	/**
	 * Devuelve el n�mero de nodos que componen la traza.
	 * 
	 * @return N�mero de nods que componen la traza.
	 */
	public int getNumNodos() {
		return this.raiz.getNumNodos();
	}

	/**
	 * Devuelve la altura del �rbol
	 * 
	 * @return �ltura del �rbol.
	 */
	public int getAltura() {
		return this.raiz.getMaxAltura();
	}

	/**
	 * Devuelve el n�mero de nodos visibles del �rbol.
	 * 
	 * @return N�mero de nodos visibles.
	 */
	public int getNumNodosVisibles() {
		return this.raiz.getNumNodosVisibles();
	}

	/**
	 * Devuelve el n�mero de nodos hist�ricos del �rbol.
	 * 
	 * @return N�mero de nodos hist�ricos.
	 */
	public int getNumNodosHistoricos() {
		return this.raiz.getNumNodosHistoricos();
	}

	/**
	 * Devuelve el n�mero de nodos a�n no mostrados del �rbol.
	 * 
	 * @return N�mero de nodos a�n no mostrados.
	 */
	public int getNumNodosAunNoMostrados() {
		return this.raiz.getNumNodosAunNoMostrados();
	}

	/**
	 * Devuelve una matriz con todos los valores de los par�metros de entrada
	 * para los distintos nodos del �rbol.
	 * 
	 * @param interfaz
	 *            Interfaz del m�todo de la cual se obtendr�n los valores.
	 * @param filtrarRepetidos
	 *            true para eliminar valores repetidos, false en caso contrario.
	 * 
	 * @return Matriz con todos los valores de los par�metros de entrada.
	 */
	public String[][] getValoresParametros(String interfaz,
			boolean filtrarRepetidos) {
		String interfazSinTipo = interfaz.substring(0,
				interfaz.lastIndexOf(" ["));
		interfazSinTipo = interfazSinTipo.replace("( ", " (");
		interfazSinTipo = interfazSinTipo.replace(" )", ")");

		String[][] datos = this.raiz
				.getValoresParametrosInicio(interfazSinTipo);

		if (filtrarRepetidos) {
			for (int i = 0; i < datos.length; i++) // i = num Nodo almacenado,
				// los vamos recorriendo
			{
				for (int j = i + 1; j < datos.length; j++) // j = num Nodo de
					// comparaci�n,
					// comparamos hacia
					// adelante
				{
					for (int k = 0; k < datos[i].length; k++) // k = num
						// Par�metro
					{
						if (datos[i][k] != null && datos[j][k] != null
								&& datos[i][k].equals(datos[j][k])) {
							datos[j][k] = null;
						}
					}
				}
			}
		}

		return datos;
	}

	/**
	 * Devuelve una matriz con todos los valores de los par�metros de salida
	 * para los distintos nodos del �rbol.
	 * 
	 * @param interfaz
	 *            Interfaz del m�todo de la cual se obtendr�n los valores.
	 * @param filtrarRepetidos
	 *            true para eliminar valores repetidos, false en caso contrario.
	 * 
	 * @return Matriz con todos los valores de los par�metros de salida.
	 */
	public String[][] getValoresResultado(String interfaz,
			boolean filtrarRepetidos) {
		String interfazSinTipo = interfaz.substring(0,
				interfaz.lastIndexOf(" ["));
		interfazSinTipo = interfazSinTipo.replace("( ", " (");
		interfazSinTipo = interfazSinTipo.replace(" )", ")");

		String[][] datos = this.raiz.getValoresResultadoInicio(interfazSinTipo);

		for (int i = 0; i < datos.length; i++) {
			System.out.print("i= " + i + "  ");
			for (int j = 0; j < datos[i].length; j++) {
				System.out.print("[" + datos[i][j] + "] ");
			}
			System.out.println();
		}

		if (filtrarRepetidos) {
			for (int i = 0; i < datos.length; i++) // i = num Nodo almacenado,
				// los vamos recorriendo
			{
				for (int j = i + 1; j < datos.length; j++) // j = num Nodo de
					// comparaci�n,
					// comparamos hacia
					// adelante
				{
					for (int k = 0; k < datos[i].length; k++) // k = num
						// Par�metro
					{
						if (datos[i][k] != null && datos[j][k] != null
								&& datos[i][k].equals(datos[j][k])) {
							datos[j][k] = null;
						}
					}
				}
			}

			System.out.println("Traza.getValoresResultado: <" + interfazSinTipo
					+ "> <" + ("" + filtrarRepetidos) + ">");
			for (int i = 0; i < datos.length; i++) {
				for (int j = 0; j < datos[i].length; j++) {
					System.out.print("[" + datos[i][j] + "] ");
				}
				System.out.println();
			}
		}

		return datos;
	}

	/**
	 * Obtiene todas las interfaces de los distintos m�todos que componen el
	 * �rbol.
	 * 
	 * @return Interfaces de los distintos m�todos que componen el �rbol.
	 */
	public String[] getInterfacesMetodos() {
		String[] interfaces = this.raiz.interfacesMetodos();

		interfaces = ServiciosString.quitarCadenasRepetidas(interfaces);

		int mitad = interfaces.length / 2;
		if (interfaces.length % 2 == 0) {
			mitad--;
		}

		for (int i = interfaces.length - 1; i > mitad; i--) {
			String cadAux = interfaces[i];
			interfaces[i] = interfaces[interfaces.length - i - 1];
			interfaces[interfaces.length - i - 1] = cadAux;
		}

		return interfaces;
	}

	/**
	 * Devuelve el n�mero de nodos inhibidos del �rbol.
	 * 
	 * @return N�mero de nodos inhibidos del �rbol.
	 */
	public int getNumNodosInhibidos() {
		return this.raiz.getNumNodosInhibidos();
	}

	/**
	 * Devuelve el n�mero de nodos iluminados del �rbol.
	 * 
	 * @return N�mero de nodos iluminados del �rbol.
	 */
	public int getNumNodosIluminados() {
		return this.raiz.getNumNodosIluminados();
	}

	/**
	 * Devuelve el t�tulo de la traza.
	 * 
	 * @return T�tulo de la traza.
	 */
	public String getTitulo() {
		return this.titulo;
	}

	/**
	 * Permite establecer el t�tulo de la traza.
	 * 
	 * @param titulo
	 *            T�tulo de la traza.
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * Asigna a la ra�z un valor determinado.
	 * 
	 * @param ra
	 *            Nueva ra�z del �rbol.
	 */
	public void setRaiz(RegistroActivacion ra) {
		this.raiz = ra;
	}

	/**
	 * Permite visualizar el �rbol en un fichero de texto plano.
	 * 
	 * @param fichero
	 */
	private void verArbol(String fichero) {
		this.raiz.verArbol(fichero);
	}

	/**
	 * Mira si la traza est� con la ra�z mostrandose en su estado inicial
	 * (Entrada pero sin hijos)
	 * 
	 * @return true si la ra�z se muestra en su estado inicial, false en caso
	 *         contrario.
	 */
	public boolean raizInicio() {
		return this.getRaiz().estadoInicio();
	}

	/**
	 * Devuelve si el nodo actual se ve al completo.
	 * 
	 * @return true si el nodo actual se ve al completo, false en caso
	 *         contrario.
	 */
	public boolean nodoActualCompleto() {
		RegistroActivacion ra = this.raiz.getNodoActual();
		if ((ra != null && ra.actualyCompleto()) || (ra == null)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Devuelve el n�mero de m�todos en ejecuci�n.
	 * 
	 * @return N�mero de m�todos en ejecuci�n.
	 */
	public int getNumMetodos() {
		return this.raiz.getNumMetodos();
	}

	/**
	 * Devuelve el nombre de los distintos m�todos en ejecuci�n.
	 * 
	 * @return Nombre de los m�todos.
	 */
	public String[] getNombresMetodos() {
		return this.raiz.getNombresMetodos();
	}

	/**
	 * Devuelve el tipo de estructura para DYV que se est� visualizando.
	 * 
	 * @return (1 si es array, 2 si es matriz, 0 en cualquier otro caso)
	 */
	public int tipoEstructura() {
		return this.raiz.tipoEstructura();
	}

	/**
	 * Recalcula la traza para que haya varios elementos m�s visibles
	 * (consiguiendo la visualizaci�n completa de determinado nodo)
	 */
	public void siguienteMultipleVisible() {
		this.raiz.borrarCaminoActual();
		if (!this.enteroVisible()) {
			RegistroActivacion ra = this.raiz.getNodoActual();
			if (ra != null) {
				ra.todoVisible();
				ra.contraer(); // ra queda contraido y sus hijos inhibidos
			}
		}
		this.raiz.crearCaminoActual();
	}

	/**
	 * Recalcula la traza para que haya un elemento m�s visible
	 */
	public void siguienteVisible() {
		if (!this.getRaiz().esMostradoEntero()) {
			this.raiz.siguienteVisible();
		}
	}

	/**
	 * Recalcula la traza para que haya un elemento menos visible
	 */
	public void anteriorVisible() {
		this.raiz.borrarCaminoActual();
		boolean valor = !(this.vacioVisible());

		if (valor) {
			this.raiz.anteriorVisible();
		}
		this.raiz.crearCaminoActual();
	}

	/**
	 * Recalcula la traza para que haya varios elementos menos visibles.
	 */
	public void anteriorMultipleVisible() {
		this.raiz.borrarCaminoActual();
		if (!this.vacioVisible()) {
			RegistroActivacion ra = this.raiz.getNodoActual();
			if (ra != null) {
				ra.nadaVisible();
				ra.siguienteVisible();
			}
		}
		this.raiz.crearCaminoActual();
	}

	/**
	 * Recalcula la traza para que todos los elementos sean visibles
	 */
	public void todoVisible() {
		this.raiz.borrarCaminoActual();
		this.raiz.todoVisible();
		this.raiz.crearCaminoActual();
	}

	/**
	 * Recalcula la traza para que ning�n elemento sea visible
	 */
	public void nadaVisible() {
		this.raiz.borrarCaminoActual();
		this.raiz.nadaVisible();
		this.raiz.crearCaminoActual();
		this.siguienteVisible();
	}

	/**
	 * Comprueba que todos los elementos sean visibles
	 * 
	 * @return true si todos los elementos son visibles
	 */
	public boolean enteroVisible() {
		return this.raiz.enteroVisible();
	}

	/**
	 * Comprueba que ning�n elemento sea visible
	 * 
	 * @return true si ning�n elementos es visible
	 */
	public boolean vacioVisible() {
		return this.raiz.vacioVisible();
	}

	/**
	 * Vac�a la traza para borrar la ejecuci�n
	 */
	public void vaciarTraza() {
		this.raiz = null;
		this.ultimo = null;
	}

	/**
	 * Crea una copia de la traza.
	 * 
	 * @return Una nueva instancia con los mismo valores que la actual.
	 */
	public Traza copiar() {
		Traza t = new Traza();
		t.raiz = this.getRaiz().copiar(null);

		t.setTecnicas(this.tecnicas);
		t.setIDTraza(new String(this.getIDTraza() + ""));
		t.setArchivo(new String(this.getArchivo() + ""));
		t.setNombreMetodoEjecucion(new String(this.getNombreMetodoEjecucion()
				+ ""));
		t.setTitulo(new String(this.getTitulo() + ""));

		return t;
	}

	/**
	 * Permite Iluminar todos los nodos que se corresponden con los valores de
	 * entrada, y de salida de un m�todo.
	 * 
	 * @param numMetodo
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
	public int iluminar(int numMetodo, String valoresE[], String valoresS[],
			boolean valor) {
		return this.raiz.iluminar(numMetodo, valoresE, valoresS, valor);
	}
	/**
	 * Permite obtener el numero de nodos que se corresponden con los valores de
	 * entrada, y de salida de un m�todo.
	 * 
	 * @param numMetodo
	 *            Posici�n del m�todo a iluminar.
	 * @param valoresE
	 *            Valores de entrada del m�todo a iluminar.
	 * @param valoresS
	 *            Valores de salida del m�todo a iluminar.
	 
	 * @return N�mero de nodos iluminados.
	 */
	public int getRedundantes(int numMetodo, String valoresE[], 
			String valoresS[]) {
		return this.raiz.getRedundantes(numMetodo, valoresE, valoresS);
	}

	/**
	 * Asigna qu� parametros son visibles durante la animaci�n
	 * 
	 * @param claseAlgoritmo
	 *            Informaci�n de la clase actual.
	 */
	public void setVisibilidad(ClaseAlgoritmo claseAlgoritmo) {
		this.raiz.setVisibilidad(claseAlgoritmo);
	}

	/**
	 * Asigna qu� parametros son visibles durante la animaci�n
	 * 
	 * @param dtb
	 *            Datos de traza b�sicos.
	 */
	public void setVisibilidad(DatosTrazaBasicos dtb) {
		this.raiz.setVisibilidad(dtb);
	}

	/**
	 * Configura la traza para visualizar tanto la entrada como la salida.
	 */
	public void visualizarEntradaYSalida() {
		this.raiz.borrarCaminoActual();
		this.raiz.visualizarEntradaYSalida();
		this.raiz.crearCaminoActual();
	}

	/**
	 * Configura la traza para visualizar �nicamente la entrada.
	 */
	public void visualizarSoloEntrada() {
		this.raiz.borrarCaminoActual();
		this.raiz.visualizarSoloEntrada();
		this.raiz.crearCaminoActual();
	}

	/**
	 * Configura la traza para visualizar �nicamente la salida.
	 */
	public void visualizarSoloSalida() {
		this.raiz.borrarCaminoActual();
		this.raiz.visualizarSoloSalida();
		this.raiz.crearCaminoActual();
	}

	/**
	 * Devuelve el registro de activaci�n que se corresponde con el
	 * identificador proporcionado.
	 * 
	 * @param id
	 *            Identificador del registro de activaci�n
	 * 
	 * @return Registro de activaci�n que se corresponde con el identificador.
	 */
	public RegistroActivacion getRegistroActivacionPorID(int id) {
		return this.raiz.getRegistroActivacionPorID(id);
	}

	/**
	 * Devuelve el registro de activaci�n atual.
	 * 
	 * @return Registro de activaci�n actual.
	 */
	public RegistroActivacion getRegistroActivo() {
		return this.raiz.getRegistroActivo();
	}

	/**
	 * Permite actualizar todos los nodos de la traza con los nodos de la traza
	 * pasada por par�metro.
	 * 
	 * @param traza
	 *            Traza de la cual se obtendr� toda la informaci�n de los nodos.
	 */
	public void actualizarEstadoTrazaCompleta(Traza traza) {
		this.raiz.actualizarEstadoFlagsDesdeGemelo(traza);
		this.raiz.asignarMetodosNoVisibles();
	}

	/**
	 * Corrige todos los valores de los nodos cuando necesitan ser reprocesados.
	 */
	public void hacerCoherente() {
		this.raiz.hacerCoherente();
	}

	/**
	 * Asigna a cada registro de activaci�n un n�mero en funci�n del m�todo al
	 * que pertenece. El m�todo principal tendr� un 0 en sus nodos, ...
	 */
	public void asignarNumeroMetodo() {
		this.raiz.asignarNumeroMetodo(this.getInterfacesMetodos());
	}

	/**
	 * Asigna a cada registro de activaci�n un n�mero en funci�n del m�todo al
	 * que pertenece. El m�todo principal tendr� un 0 en sus nodos, ...
	 * 
	 * @param interfaces
	 *            Interfaces de los m�todos disponibles.
	 */
	public void asignarNumeroMetodo(String[] interfaces) {
		this.raiz.asignarNumeroMetodo(interfaces);
	}
}