package datos;

import java.util.ArrayList;

/**
 * Representa una clase procesada, sólo contiene métodos que son aptos para ser
 * visualizables, no contiene todos los métodos de la clase original
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
		//	Si el último método seleccionado pertenece a otra
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
	 * Permite añadir un método a la representación de la clase.
	 * 
	 * @param m
	 *            Método.
	 */
	public void addMetodo(MetodoAlgoritmo m) {
		// Buscamos que no exista ya un método igual: si existe, reemplazamos
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
	 * Permite recuperar un método de la clase.
	 * 
	 * @param i
	 *            posición del método a recuperar.
	 * 
	 * @return Método correspondiente a la posición indicada.
	 */
	public MetodoAlgoritmo getMetodo(int i) {
		return this.metodos.get(i);
	}

	/**
	 * Permite recuperar el método de la clase con el identificador
	 * especificado.
	 * 
	 * @param id
	 *            identificador del método.
	 * 
	 * @return Método correspondiente al identificador especificado, null si no
	 *         existe ningún método almacenado con dicho identificador.
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
	 * Permite recuperar el método principal de la clase.
	 * 
	 * @return Método principal de la clase, null si no existe un método
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
	 * Devuelve los métodos de la clase.
	 * 
	 * @return Métodos de la clase.
	 */
	public ArrayList<MetodoAlgoritmo> getMetodos() {
		return this.metodos;
	}

	/**
	 * Permite recuperar los métodos con el nombre especificado.
	 * 
	 * @param nombre
	 *            Nombre del método a recuperar.
	 * 
	 * @return Métodos con el nombre especificado.
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
	 * Permite recuperar los métodos que han sido marcados como procesados.
	 * 
	 * @return Métodos procesados.
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
	 * Devuelve el número total de métodos de la clase.
	 * 
	 * @return Número total de métodos.
	 */
	public int getNumMetodos() {
		return this.metodos.size();
	}

	/**
	 * Devuelve el número total de métodos procesados de la clase.
	 * 
	 * @return Número total de métodos procesados.
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
	 * Marca todos los métodos de la clase como no principales.
	 */
	public void borrarMarcadoPrincipal() {
		for (int i = 0; i < this.metodos.size(); i++) {
			this.metodos.get(i).setMarcadoPrincipal(false);
		}

	}

	/**
	 * Determina si alguno de los métodos de la clase puede ser visualizado con
	 * la técnica DYV.
	 * 
	 * @return true si alguno de los métodos de la clase puede ser visualizado
	 *         con la técnica DYV, false en caso contrario.
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
	 * Establece el último método seleccionado por el usuario
	 * @param ultimoMetodoSeleccionado Último método seleccionado por el usuario
	 * @param ultimaClase Nombre de la clase a la que pertenece el último método seleccionado por el usuario
	 */
	public static void setUltimoMetodoSeleccionado(MetodoAlgoritmo ultimoMetodoSeleccionado, String ultimaClase) {
		ClaseAlgoritmo.ultimoMetodoSeleccionado = ultimoMetodoSeleccionado;
		ClaseAlgoritmo.ultimaClaseSeleccionada = ultimaClase;
	}
	
	/**
	 * Obtiene el último método seleccionado por el usuario
	 * @return Último método seleccionado por el usuario
	 */
	public static MetodoAlgoritmo getUltimoMetodoSeleccionado() {
		return ultimoMetodoSeleccionado;
	}

	/**
	 * Compara un método pasado como parámetro con el último método seleccionado por el usuario.
	 * @param nuevoMetodoAComparar Método a comparar con el último seleccionado
	 * @param nombreClase Nombre de la clase del método a comparar
	 * @return Si coinciden el nombre de la clase, nombre del método y
	 * 	número y tipo de parámetros en el mismo orden TRUE, FALSE caso contrario
	 */
	public boolean compararUltimoMetodoSeleccionado(MetodoAlgoritmo nuevoMetodoAComparar, String nombreClase){
		//	Si el último método seleccionado es null o los nombres de las clases no coinciden
		//		devolvemos false
		if(ClaseAlgoritmo.ultimoMetodoSeleccionado == null || !(ClaseAlgoritmo.ultimaClaseSeleccionada.equals(nombreClase))){
			return false;
		//	Si no comparamos nombre del método, número y tipo de parámetros
		}else{
			//	Nombre y numero de parámetros
			if(ClaseAlgoritmo.ultimoMetodoSeleccionado.getNombre().equals(nuevoMetodoAComparar.getNombre()) &&
					ClaseAlgoritmo.ultimoMetodoSeleccionado.getNumeroParametros() == nuevoMetodoAComparar.getNumeroParametros()){
				//	Comparamos que el tipo de los parámetros sean idénticos y en el mismo orden
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
	 * Obtiene el nombre de la última clase cargada
	 * @return Nombre última clase cargada
	 */
	public static String getUltimaClaseSeleccionada() {
		return ultimaClaseSeleccionada;
	}

}