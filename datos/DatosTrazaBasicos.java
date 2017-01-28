package datos;

import java.util.ArrayList;
import java.util.List;

import utilidades.ServiciosString;

/**
 * Almacena información relativa a una traza (Información relativa a la traza,
 * métodos de ejecución y título de la traza).
 */
public class DatosTrazaBasicos {

	private String archivo;
	private String idTraza;
	private String nombreMetodoEjecucion;
	private String titulo;

	private ArrayList<DatosMetodoBasicos> metodos;

	/**
	 * Permite construir una instancia dada una traza de ejecución.
	 * 
	 * @param traza
	 */
	public DatosTrazaBasicos(Traza traza) {
		this.archivo = traza.getArchivo();
		this.idTraza = traza.getIDTraza();
		this.nombreMetodoEjecucion = traza.getNombreMetodoEjecucion();
		this.titulo = traza.getTitulo();
		this.metodos = new ArrayList<DatosMetodoBasicos>(0);
		this.anadirMetodosDTB(traza);
	}

	/**
	 * Permite construir una instancia vacía, sin datos previamente
	 * establecidos.
	 */
	public DatosTrazaBasicos() {
		this("", "_codigoSinAsignar_", "", "");
	}

	/**
	 * Permite construir una instancia.
	 * 
	 * @param archivo
	 *            Path de la traza.
	 * @param idTraza
	 *            Identificador de la traza.
	 * @param nombreMetodoEjecucion
	 *            Nombre del método en ejecución de la traza.
	 * @param titulo
	 *            Título de la traza
	 */
	public DatosTrazaBasicos(String archivo, String idTraza,
			String nombreMetodoEjecucion, String titulo) {
		this.archivo = archivo;
		this.idTraza = idTraza;
		this.nombreMetodoEjecucion = nombreMetodoEjecucion;
		this.titulo = titulo;
		this.metodos = new ArrayList<DatosMetodoBasicos>(0);
	}

	/**
	 * Permite establecer el path de la traza.
	 * 
	 * @param a
	 *            Path de la traza.
	 */
	public void setArchivo(String a) {
		this.archivo = a;
	}

	/**
	 * Permite establecer el identificador de la traza.
	 * 
	 * @param id
	 *            Identificador de la traza.
	 */
	public void setId(String id) {
		this.idTraza = id;
	}

	/**
	 * Permite establecer el nombre del método en ejecución.
	 * 
	 * @param n
	 *            Nombre del método en ejecución.
	 */
	public void setNombreMetodoEjecucion(String n) {
		this.nombreMetodoEjecucion = n;
	}

	/**
	 * Permite establecer el título.
	 * 
	 * @param t
	 *            Título.
	 */
	public void setTitulo(String t) {
		this.titulo = t;
	}

	/**
	 * Permite añadir un método.
	 * 
	 * @param m
	 *            Método.
	 */
	public void addMetodo(DatosMetodoBasicos m) {
		this.metodos.add(m);
	}

	/**
	 * Devuelve el path del archivo de traza.
	 * 
	 * @return Archivo de traza.
	 */
	public String getArchivo() {
		return this.archivo;
	}

	/**
	 * Devuelve el identificador de la traza.
	 * 
	 * @return Identificador de la traza.
	 */
	public String getId() {
		return this.idTraza;
	}

	/**
	 * Devuelve el nombre del método en ejecución.
	 * 
	 * @return Nombre del método en ejecución.
	 */
	public String getNombreMetodoEjecucion() {
		return this.nombreMetodoEjecucion;
	}

	/**
	 * Devuelve el título de la traza
	 * 
	 * @return Título
	 */
	public String getTitulo() {
		return this.titulo;
	}

	/**
	 * Devuelve los métodos almacenados.
	 * 
	 * @return Lista de métodos almacenados.
	 */
	public ArrayList<DatosMetodoBasicos> getMetodos() {
		return this.metodos;
	}

	/**
	 * Devuelve un método concreto dada su posición.
	 * 
	 * @param i
	 *            posición del método.
	 * 
	 * @return Método.
	 */
	public DatosMetodoBasicos getMetodo(int i) {
		if (i >= 0 && i < this.metodos.size()) {
			return this.metodos.get(i);
		} else {
			return null;
		}
	}
	
	/**
	 * Devuelve una lista de métodos dada una lista de posiciones
	 * 
	 * @param listaPosiciones
	 * 		Lista de posiciones de los métodos
	 * 
	 * @return
	 * 		Lista de DatosMetodosBasicos
	 */
	public List<DatosMetodoBasicos> getMetodo(List<Integer> listaPosiciones) {
		List<DatosMetodoBasicos> listaMetodos = new ArrayList<DatosMetodoBasicos>();
		for(int i:listaPosiciones){
			if (i >= 0 && i < this.metodos.size()) {
				listaMetodos.add(this.metodos.get(i));
			}
		}
		return listaMetodos;
	}

	/**
	 * Busca entre los métodos almacenados el que corresponda a los valores
	 * especificados.
	 * 
	 * @param nombre
	 *            Nombre del método.
	 * @param clasesE
	 *            Tipos de los parámetros de entrada.
	 * @param clasesS
	 *            Tipos de los parámetros de salida.
	 * @param dimE
	 *            Dimensiones de los parámetros de entrada.
	 * @param dimS
	 *            Dimensiones de los parámetros de salida.
	 * 
	 * @return Método que cumple las características especificadas, null en caso
	 *         de que ninguno de los métodos las cumpla.
	 */
	public DatosMetodoBasicos getMetodo(String nombre, String[] clasesE,
			String[] clasesS, int[] dimE, int[] dimS) {
		for (int i = 0; i < this.metodos.size(); i++) {
			DatosMetodoBasicos dmb = this.metodos.get(i);

			// Si coincide nombre, miramos parámetros...
			if (dmb.getNombre().equals(nombre)
					&& dmb.getNumParametrosE() == clasesE.length
					&& dmb.getNumParametrosS() == clasesS.length) {
				boolean sonIguales = true;
				for (int j = 0; j < dmb.getNumParametrosE(); j++) {
					if (!dmb.getTipoParametroE(j).equals(clasesE[j])) {
						sonIguales = false;
					}
				}

				if (sonIguales) {
					for (int j = 0; j < dmb.getNumParametrosS(); j++) {
						if (!dmb.getTipoParametroS(j).equals(clasesS[j])) {
							sonIguales = false;
						}
					}
				}

				if (sonIguales) {
					return dmb;
				}
			}
		}
		return null;

	}

	/**
	 * Devuelve el número de métodos almacenados.
	 * 
	 * @return Número de métodos almacenados.
	 */
	public int getNumMetodos() {
		return this.metodos.size();
	}

	/**
	 * Añade el método correspondiente al nodo raiz de la traza.
	 */
	private void anadirMetodosDTB(Traza traza) {
		RegistroActivacion ra = traza.getRaiz();
		this.anadirMetodosDTB(ra);
	}

	/**
	 * Añade los métodos correspondientes al registro de activación especificado
	 * y recursivamente a todos sus hijos.
	 * 
	 * @param ra
	 *            Registro de activación.
	 */
	private void anadirMetodosDTB(RegistroActivacion ra) {
		boolean encontradoIgual = false;

		String nombreMetodo = ra.getNombreMetodo();
		String[] clasesE = ra.clasesParamE();
		String[] clasesS = ra.clasesParamS();

		for (int i = 0; i < this.metodos.size(); i++) {
			String tNombreMetodo = this.metodos.get(i).getNombre();
			if (tNombreMetodo.equals(nombreMetodo)
					&& this.metodos.get(i).getNumParametrosE() == clasesE.length) {
				int[] dimE = ra.dimParamE();
				int[] tDimE = this.metodos.get(i).getDimE();

				boolean encontradoDesigual = false;
				if (dimE.length != tDimE.length) {
					encontradoDesigual = true;
				} else {
					for (int j = 0; j < dimE.length; j++) {
						if (!clasesE[j].equals(this.metodos.get(i)
								.getTipoParametroE(j)) || dimE[j] != tDimE[j]) {
							encontradoDesigual = true;
						}
					}
				}

				if (!encontradoDesigual) {
					encontradoIgual = true;
				}
			}
		}

		if (!encontradoIgual) // Si no hemos detectado que ya esté, lo añadimos
		// a los métodos
		{
			DatosMetodoBasicos dmb = new DatosMetodoBasicos(nombreMetodo,
					clasesE.length, clasesS.length, ra.getDevuelveValor());
			int[] dimE = ra.dimParamE();
			int[] dimS = ra.dimParamS();

			String nombres[] = ra.getNombreParametros();

			boolean visiblesE[] = ra.getVisibilidadEntrada();
			boolean visiblesS[] = ra.getVisibilidadSalida();

			for (int i = 0; i < visiblesE.length; i++) {
				dmb.addParametroEntrada(nombres[i],
						ServiciosString.simplificarClase(clasesE[i]), dimE[i],
						visiblesE[i]);
			}

			for (int i = 0; i < visiblesS.length; i++) {
				dmb.addParametroSalida(nombres[i],
						ServiciosString.simplificarClase(clasesS[i]), dimS[i],
						visiblesS[i]); // Cambiar revisar
			}

			if (ra.esRaiz()) {
				dmb.setEsPrincipal(true);
			}
			dmb.setEsVisible(ra.esMetodoVisible());

			this.addMetodo(dmb);
		}

		for (int i = 0; i < ra.numHijos(); i++) {
			this.anadirMetodosDTB(ra.getHijo(i));
		}
	}

	/**
	 * Permite podar una traza para adaptarla a los valores de visibilidad de
	 * los distintos métodos.
	 * 
	 * @param traza1
	 *            Traza a podar.
	 * 
	 * @return Traza con la poda aplicada.
	 */
	public Traza podar(Traza traza1) {
		Traza traza2 = traza1.copiar();
		traza2.setRaiz(this.podarDesdeRaiz(traza2.getRaiz()));
		return traza2;
	}

	/**
	 * Permite podar la jerarquía de Registros de activaciones de una traza con
	 * respecto a los valores de visibilidad de los métodos.
	 * 
	 * @param ra
	 *            Registro de activación raíz.
	 * 
	 * @return Registro de activación raíz podado.
	 */
	private RegistroActivacion podarDesdeRaiz(RegistroActivacion ra) {
		return this.podar(ra, null)[0];
	}

	/**
	 * Permite podar un registro de activación (con hijos) recursivamente con
	 * respecto a los valores de visibilidad de los métodos.
	 * 
	 * @param ra
	 *            Registro de activación.
	 * @param padre
	 *            Registro de activación padre.
	 * 
	 * @return Registros de activación hijos.
	 */
	private RegistroActivacion[] podar(RegistroActivacion ra,
			RegistroActivacion padre) {
		int totalHijosAdquiridos = 0;
		ra.setPadre(padre);

		// Creamos lista nueva de hijos, serán los hijos del nodo una vez acabe
		// el procesamiento
		ArrayList<RegistroActivacion> hijosNuevo = new ArrayList<RegistroActivacion>(
				0);
		for (int i = 0; i < ra.numHijos(); i++) {
			hijosNuevo.add(ra.getHijo(i));
		}

		// Procesamos hijos, y vamos generando lista nueva de hijos
		for (int i = 0; i < ra.numHijos(); i++) {
			RegistroActivacion[] ras;

			if (ra.esMetodoVisible()) {
				ras = this.podar(ra.getHijo(i), ra);
			} else {
				ras = this.podar(ra.getHijo(i), padre);
			}

			if (!ra.getHijo(i).esMetodoVisible()) {
				hijosNuevo.remove(i + totalHijosAdquiridos);

				for (int j = ras.length - 1; j >= 0; j--) {
					hijosNuevo.add(i + totalHijosAdquiridos, ras[j]);
				}
				totalHijosAdquiridos--; // por el remove de un par de líneas
				// atras
				totalHijosAdquiridos = totalHijosAdquiridos + ras.length;
			}
		}

		// Guardamos los hijos nuevos de este nodo
		for (int i = ra.numHijos() - 1; i >= 0; i--) {
			ra.delHijo(i);
		}
		for (int i = 0; i < hijosNuevo.size(); i++) {
			ra.setHijo(hijosNuevo.get(i));
		}

		if (ra.esMetodoVisible()) {
			RegistroActivacion raTotal[] = new RegistroActivacion[1];
			raTotal[0] = ra;
			return raTotal;
		} else {
			RegistroActivacion raTotal[] = new RegistroActivacion[ra.numHijos()];

			for (int i = 0; i < ra.numHijos(); i++) {
				raTotal[i] = ra.getHijo(i);
			}
			return raTotal;
		}
	}
}