package datos;

import java.util.ArrayList;

/**
 * Representa una clase procesada, s�lo contiene m�todos que son aptos para ser
 * visualizables, no contiene todos los m�todos de la clase original
 */
public class ClaseAlgoritmo {

	private String path;
	private String nombre;
	private String nombre_id;
	private String nombre_id2;

	private ArrayList<MetodoAlgoritmo> metodos = new ArrayList<MetodoAlgoritmo>(0);

	private static MetodoAlgoritmo ultimoMetodoSeleccionado;
	private static String ultimaClaseSeleccionada;	

	/**
	 * Permite construir una nueva instancia dados el path de la clase y el
	 * nombre.
	 * 
	 * @param path
	 *            Path donde se encuentra la clase.
	 * @param nombre
	 *            Nombre de la clase.
	 */
	public ClaseAlgoritmo(String path, String nombre) {
		this.path = path;
		this.nombre = nombre;
		//	Si el �ltimo m�todo seleccionado pertenece a otra
		//	clase se borra
		if(ultimoMetodoSeleccionado != null && ultimaClaseSeleccionada != null &&
				!nombre.equals(ultimaClaseSeleccionada)){
			ultimoMetodoSeleccionado=null;
			ultimaClaseSeleccionada=null;
		}
	}

	/**
	 * Devuelve el nombre de la clase.
	 * 
	 * @return Nombre de la clase.
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Devuelve el path donde se encuentra la clase.
	 * 
	 * @return Path donde se encuentra la clase.
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Permite a�adir un m�todo a la representaci�n de la clase.
	 * 
	 * @param m
	 *            M�todo.
	 */
	public void addMetodo(MetodoAlgoritmo m) {
		// Buscamos que no exista ya un m�todo igual: si existe, reemplazamos
		String tiposParam[] = m.getTiposParametros();
		String nombreParam = m.getNombre();

		if (this.metodos.size() == 0) {
			m.setMarcadoPrincipal(true);
		}		

		boolean sonIguales = false;
		int i = 0;
		while (i < this.metodos.size() && sonIguales == false) {
			sonIguales = true;
			String tipos2Param[] = this.getMetodo(i).getTiposParametros();
			String nombre2Param = this.getMetodo(i).getNombre();

			if (nombre2Param.equals(nombreParam)
					&& tiposParam.length == tipos2Param.length) {
				for (int j = 0; j < tiposParam.length; j++) {
					if (!tiposParam[j].equals(tipos2Param[j])) {
						sonIguales = false;
					}
				}
			} else {
				sonIguales = false;
			}
			i++;
		}
		if (sonIguales) {

			this.metodos.remove(i - 1);
			this.metodos.add(i - 1, m);
		} else {
			this.metodos.add(m);
		}
	}

	/**
	 * Permite recuperar un m�todo de la clase.
	 * 
	 * @param i
	 *            posici�n del m�todo a recuperar.
	 * 
	 * @return M�todo correspondiente a la posici�n indicada.
	 */
	public MetodoAlgoritmo getMetodo(int i) {
		return this.metodos.get(i);
	}

	/**
	 * Permite recuperar el m�todo de la clase con el identificador
	 * especificado.
	 * 
	 * @param id
	 *            identificador del m�todo.
	 * 
	 * @return M�todo correspondiente al identificador especificado, null si no
	 *         existe ning�n m�todo almacenado con dicho identificador.
	 */
	public MetodoAlgoritmo getMetodoID(int id) {
		for (MetodoAlgoritmo m : this.metodos) {
			if (m.getID() == id) {
				return m;
			}
		}
		return null;
	}

	/**
	 * Permite recuperar el m�todo principal de la clase.
	 * 
	 * @return M�todo principal de la clase, null si no existe un m�todo
	 *         principal.
	 */
	public MetodoAlgoritmo getMetodoPrincipal() {
		for (int i = 0; i < this.metodos.size(); i++) {
			if (this.metodos.get(i).getMarcadoPrincipal()) {
				return this.metodos.get(i);
			}
		}
		return null;
	}

	/**
	 * Devuelve los m�todos de la clase.
	 * 
	 * @return M�todos de la clase.
	 */
	public ArrayList<MetodoAlgoritmo> getMetodos() {
		return this.metodos;
	}

	/**
	 * Permite recuperar los m�todos con el nombre especificado.
	 * 
	 * @param nombre
	 *            Nombre del m�todo a recuperar.
	 * 
	 * @return M�todos con el nombre especificado.
	 */
	public ArrayList<MetodoAlgoritmo> getMetodos(String nombre) {
		ArrayList<MetodoAlgoritmo> metodosNombre = new ArrayList<MetodoAlgoritmo>();

		for (int i = 0; i < this.metodos.size(); i++) {
			if (this.metodos.get(i).getNombre().equals(nombre)) {
				metodosNombre.add(this.metodos.get(i));
			}
		}

		return metodosNombre;
	}

	/**
	 * Permite recuperar los m�todos que han sido marcados como procesados.
	 * 
	 * @return M�todos procesados.
	 */
	public ArrayList<MetodoAlgoritmo> getMetodosProcesados() {
		ArrayList<MetodoAlgoritmo> mp = new ArrayList<MetodoAlgoritmo>();

		for (int i = 0; i < this.metodos.size(); i++) {
			if (this.metodos.get(i).getMarcadoProcesar()) {
				mp.add(this.metodos.get(i));
			}
		}

		return mp;
	}

	/**
	 * Devuelve el n�mero total de m�todos de la clase.
	 * 
	 * @return N�mero total de m�todos.
	 */
	public int getNumMetodos() {
		return this.metodos.size();
	}

	/**
	 * Devuelve el n�mero total de m�todos procesados de la clase.
	 * 
	 * @return N�mero total de m�todos procesados.
	 */
	public int getNumMetodosProcesados() {
		int contador = 0;

		for (int i = 0; i < this.metodos.size(); i++) {
			if (this.metodos.get(i).getMarcadoProcesar()) {
				contador++;
			}
		}

		return contador;
	}

	/**
	 * Devuelve el identificador primario de la clase.
	 * 
	 * @return Identificador primario de la clase.
	 */
	public String getId() {
		return this.nombre_id;
	}

	/**
	 * Devuelve el identificador secundario de la clase.
	 * 
	 * @return Identificador secundario de la clase.
	 */
	public String getId2() {
		return this.nombre_id2;
	}

	/**
	 * Establece el identificador primario de la clase.
	 * 
	 * @param id
	 *            Identificador primario de la clase.
	 */
	public void setId(String id) {
		this.nombre_id = id;
	}

	/**
	 * Establece el identificador secundario de la clase.
	 * 
	 * @param id
	 *            Identificador secundario de la clase.
	 */
	public void setId2(String id2) {
		this.nombre_id2 = id2;
	}

	/**
	 * Marca todos los m�todos de la clase como no principales.
	 */
	public void borrarMarcadoPrincipal() {
		for (int i = 0; i < this.metodos.size(); i++) {
			this.metodos.get(i).setMarcadoPrincipal(false);
		}

	}

	/**
	 * Determina si alguno de los m�todos de la clase puede ser visualizado con
	 * la t�cnica DYV.
	 * 
	 * @return true si alguno de los m�todos de la clase puede ser visualizado
	 *         con la t�cnica DYV, false en caso contrario.
	 */
	public boolean potencialMetodoDYV() {
		for (MetodoAlgoritmo m : this.metodos) {
			int[] dimParam = m.getDimParametros();
			
			for (int i = 0; i < dimParam.length; i++) {
				if (dimParam[i] > 0) {					
					return true;
				}
			}
		}

		return false;
	}	

	/**
	 * Establece el �ltimo m�todo seleccionado por el usuario
	 * @param ultimoMetodoSeleccionado �ltimo m�todo seleccionado por el usuario
	 * @param ultimaClase Nombre de la clase a la que pertenece el �ltimo m�todo seleccionado por el usuario
	 */
	public static void setUltimoMetodoSeleccionado(MetodoAlgoritmo ultimoMetodoSeleccionado, String ultimaClase) {
		ClaseAlgoritmo.ultimoMetodoSeleccionado = ultimoMetodoSeleccionado;
		ClaseAlgoritmo.ultimaClaseSeleccionada = ultimaClase;
	}
	
	/**
	 * Obtiene el �ltimo m�todo seleccionado por el usuario
	 * @return �ltimo m�todo seleccionado por el usuario
	 */
	public static MetodoAlgoritmo getUltimoMetodoSeleccionado() {
		return ultimoMetodoSeleccionado;
	}

	/**
	 * Compara un m�todo pasado como par�metro con el �ltimo m�todo seleccionado por el usuario.
	 * @param nuevoMetodoAComparar M�todo a comparar con el �ltimo seleccionado
	 * @param nombreClase Nombre de la clase del m�todo a comparar
	 * @return Si coinciden el nombre de la clase, nombre del m�todo y
	 * 	n�mero y tipo de par�metros en el mismo orden TRUE, FALSE caso contrario
	 */
	public boolean compararUltimoMetodoSeleccionado(MetodoAlgoritmo nuevoMetodoAComparar, String nombreClase){
		//	Si el �ltimo m�todo seleccionado es null o los nombres de las clases no coinciden
		//		devolvemos false
		if(ClaseAlgoritmo.ultimoMetodoSeleccionado == null || !(ClaseAlgoritmo.ultimaClaseSeleccionada.equals(nombreClase))){
			return false;
		//	Si no comparamos nombre del m�todo, n�mero y tipo de par�metros
		}else{
			//	Nombre y numero de par�metros
			if(ClaseAlgoritmo.ultimoMetodoSeleccionado.getNombre().equals(nuevoMetodoAComparar.getNombre()) &&
					ClaseAlgoritmo.ultimoMetodoSeleccionado.getNumeroParametros() == nuevoMetodoAComparar.getNumeroParametros()){
				//	Comparamos que el tipo de los par�metros sean id�nticos y en el mismo orden
				for(int i=0;i<ClaseAlgoritmo.ultimoMetodoSeleccionado.getNumeroParametros(); i++){
					if(!ClaseAlgoritmo.ultimoMetodoSeleccionado.getTipoParametro(i).equals(nuevoMetodoAComparar.getTipoParametro(i))){
						return false;
					}
				}
				return true;
			}
			return false;
		}
	}
	
	/**
	 * Obtiene el nombre de la �ltima clase cargada
	 * @return Nombre �ltima clase cargada
	 */
	public static String getUltimaClaseSeleccionada() {
		return ultimaClaseSeleccionada;
	}

}