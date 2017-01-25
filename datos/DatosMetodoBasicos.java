package datos;

import java.util.ArrayList;
import java.util.Arrays;

import utilidades.ServiciosString;

/**
 * Almacena informaci�n relativa a un m�todo (Par�metros de entrada, de salida,
 * su visibilidad, dimensiones y valor de retorno).
 */
public class DatosMetodoBasicos {

	private String nombre;

	private ArrayList<String> nombreParamE;
	private ArrayList<String> tipoParamE;

	private ArrayList<String> nombreParamS;
	private ArrayList<String> tipoParamS;

	private int[] dimE;
	private int[] dimS;

	private boolean retorno;

	private boolean metodoPrincipal;

	private boolean metodoVisible;
	private boolean visibilidadE[];
	private boolean visibilidadS[];

	/**
	 * Permite construir los datos de m�todo b�sicos para un m�todo.
	 * 
	 * @param nombre
	 *            Nombre del m�todo.
	 * @param numParamE
	 *            N�mero de par�metros de entrada.
	 * @param numParamS
	 *            N�mero de par�metros de salida.
	 * @param retorno
	 *            true si el m�todo tiene un valor de retorno, false si es void.
	 */
	public DatosMetodoBasicos(String nombre, int numParamE, int numParamS,
			boolean retorno) {
		this.nombre = nombre;
		
		this.visibilidadE = new boolean[numParamE];
		this.visibilidadS = new boolean[numParamS];

		this.dimE = new int[this.visibilidadE.length];
		this.dimS = new int[this.visibilidadS.length];

		for (int i = 0; i < this.visibilidadE.length; i++) {
			this.visibilidadE[i] = false;
		}
		for (int i = 0; i < this.visibilidadS.length; i++) {
			this.visibilidadS[i] = false;
		}

		this.retorno = retorno;

		this.nombreParamE = new ArrayList<String>(0);
		this.tipoParamE = new ArrayList<String>(0);
		this.nombreParamS = new ArrayList<String>(0);
		this.tipoParamS = new ArrayList<String>(0);
	}

	/**
	 * A�ade un nuevo par�metro de entrada.
	 * 
	 * @param nombre
	 *            Nombre del par�metro.
	 * @param tipo
	 *            Tipo del par�metro.
	 * @param dim
	 *            Dimensiones del par�metro (0 si es valor �nico, 1 si es un
	 *            array, 2 si es una matriz, ...)
	 * @param visible
	 *            true si el par�metro debe visualizarse en las ejecuciones.
	 */
	public void addParametroEntrada(String nombre, String tipo, int dim,
			boolean visible) {
		this.nombreParamE.add(nombre);
		this.tipoParamE.add(tipo);
		this.dimE[this.nombreParamE.size() - 1] = dim;
		this.visibilidadE[this.nombreParamE.size() - 1] = visible;
	}

	/**
	 * A�ade un nuevo par�metro de salida (par�metros para los que se visualiza
	 * su evoluci�n en la salida).
	 * 
	 * @param nombre
	 *            Nombre del par�metro.
	 * @param tipo
	 *            Tipo del par�metro.
	 * @param dim
	 *            Dimensiones del par�metro (0 si es valor �nico, 1 si es un
	 *            array, 2 si es una matriz, ...)
	 * @param visible
	 *            true si el par�metro debe visualizarse en las ejecuciones.
	 */
	public void addParametroSalida(String nombre, String tipo, int dim,
			boolean visible) {
		this.nombreParamS.add(nombre);
		this.tipoParamS.add(tipo);
		this.dimS[this.nombreParamS.size() - 1] = dim;
		this.visibilidadS[this.nombreParamS.size() - 1] = visible;
	}

	/**
	 * Establece el m�todo como principal o no.
	 * 
	 * @param valor
	 *            true para marcarlo como principal, false en caso contrario.
	 */
	public void setEsPrincipal(boolean valor) {
		this.metodoPrincipal = valor;
	}

	/**
	 * Establece el m�todo como visible o no.
	 * 
	 * @param valor
	 *            true para marcarlo como visible, false en caso contrario.
	 */
	public void setEsVisible(boolean valor) {
		this.metodoVisible = valor;
	}

	/**
	 * Devuelve si el m�todo es visible o no.
	 * 
	 * @return true si es visible, false en caso contrario.
	 */
	public boolean getEsVisible() {
		return this.metodoVisible;
	}

	/**
	 * Devuelve el n�mero de par�metros de entrada.
	 * 
	 * @return N�mero de par�metros de entrada.
	 */
	public int getNumParametrosE() {
		return this.visibilidadE.length;
	}

	/**
	 * Devuelve el n�mero de par�metros de salida.
	 * 
	 * @return N�mero de par�metros de salida.
	 */
	public int getNumParametrosS() {
		return this.visibilidadS.length;
	}

	/**
	 * Devuelve el Nombre del m�todo.
	 * 
	 * @return Nombre del m�todo.
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Devuelve el Nombre del par�metro de entrada especificado.
	 * 
	 * @param numParametro
	 *            posici�n del par�metro.
	 * 
	 * @return Nombre del par�metro especificado.
	 */
	public String getNombreParametroE(int numParametro) {
		return this.nombreParamE.get(numParametro);
	}

	/**
	 * Devuelve los nombres de todos los par�metros de entrada.
	 * 
	 * @return Nombre de los par�metros de entrada.
	 */
	public String[] getNombreParametrosE() {
		String[] nombres = new String[this.nombreParamE.size()];

		for (int i = 0; i < this.nombreParamE.size(); i++) {
			nombres[i] = this.nombreParamE.get(i);
		}

		return nombres;
	}

	/**
	 * Devuelve el Nombre del par�metro de salida especificado.
	 * 
	 * @param numParametro
	 *            posici�n del par�metro.
	 * 
	 * @return Nombre del par�metro especificado.
	 */
	public String getNombreParametroS(int numParametro) {
		return this.nombreParamS.get(numParametro);
	}

	/**
	 * Devuelve los nombres de todos los par�metros de salida.
	 * 
	 * @return Nombre de los par�metros de salida.
	 */
	public String[] getNombreParametrosS() {
		String[] nombres = new String[this.nombreParamS.size()];

		for (int i = 0; i < this.nombreParamS.size(); i++) {
			nombres[i] = this.nombreParamS.get(i);
		}

		return nombres;
	}

	/**
	 * Devuelve el Tipo del par�metro de entrada especificado.
	 * 
	 * @param numParametro
	 *            posici�n del par�metro.
	 * 
	 * @return Tipo del par�metro especificado.
	 */
	public String getTipoParametroE(int numParametro) {
		return this.tipoParamE.get(numParametro);
	}

	/**
	 * Devuelve los tipos de todos los par�metros de entrada.
	 * 
	 * @return Nombre de los par�metros de entrada.
	 */
	public String[] getTipoParametrosE() {
		String[] tipos = new String[this.tipoParamE.size()];

		for (int i = 0; i < tipos.length; i++) {
			tipos[i] = new String(this.tipoParamE.get(i));
		}

		return tipos;
	}

	/**
	 * Devuelve el Tipo del par�metro de salida especificado.
	 * 
	 * @param numParametro
	 *            posici�n del par�metro.
	 * 
	 * @return Tipo del par�metro especificado.
	 */
	public String getTipoParametroS(int numParametro) {
		return this.tipoParamS.get(numParametro);
	}

	/**
	 * Devuelve los tipos de todos los par�metros de salida.
	 * 
	 * @return Nombre de los par�metros de salida.
	 */
	public String[] getTipoParametrosS() {
		String[] tipos = new String[this.tipoParamS.size()];

		for (int i = 0; i < tipos.length; i++) {
			tipos[i] = new String(this.tipoParamS.get(i));
		}

		return tipos;
	}

	/**
	 * Devuelve el n�mero de dimensiones del par�metro de entrada especificado.
	 * 
	 * @param numParametro
	 *            posici�n del par�metro.
	 * 
	 * @return N�mero de dimensiones del par�metro especificado.
	 */
	public int getDimParametroE(int numParametro) {
		return this.dimE[numParametro];
	}

	/**
	 * Devuelve el n�mero de dimensiones del par�metro de salida especificado.
	 * 
	 * @param numParametro
	 *            posici�n del par�metro.
	 * 
	 * @return N�mero de dimensiones del par�metro especificado.
	 */
	public int getDimParametroS(int numParametro) {
		return this.dimS[numParametro];
	}

	/**
	 * Permite consultar si el m�todo tiene un valor de retorno.
	 * 
	 * @return true si el tipo de retorno es distinto de void, false en caso
	 *         contrario.
	 */
	public boolean esMetodoConRetorno() {
		return this.retorno;
	}

	/**
	 * Devuelve el tipo del valor de retorno del m�todo, si el m�todo devuelve
	 * void, se devuelve el tipo del primer par�metro de salida.
	 * 
	 * @return Tipo del valor de retorno.
	 */
	public String getTipo() {
		if (!this.retorno) {
			return "void";
		} else {
			return this.tipoParamS.get(0);
		}
	}

	/**
	 * Devuelve el n�mero de dimensiones dimensiones del tipo de retorno del
	 * m�todo, si el m�todo devuelve void, se devuelven el n�mero de dimensiones
	 * del primer par�metro de salida.
	 * 
	 * @return N�mero de dimensiones del tipo de retorno.
	 */
	public int getDimTipo() {
		if (!this.retorno) {
			return 0;
		} else {
			return this.dimS[0];
		}
	}

	/**
	 * Devuelve la visibilidad de los par�metros de entrada.
	 * 
	 * @return visibilidad de los par�metros de entrada, cada posici�n contiene
	 *         el valor correspondiente a la posici�n de cada par�metro de
	 *         entrada.
	 */
	public boolean[] getVisibilidadE() {
		return this.visibilidadE;
	}

	/**
	 * Devuelve la visibilidad de uno de los par�metros de entrada.
	 * 
	 * @param i
	 *            posici�n del m�todo de entrada.
	 * 
	 * @return visibilidad del par�metro, true si es visible, false en caso
	 *         contrario.
	 */
	public boolean getVisibilidadE(int i) {
		return this.visibilidadE[i];
	}

	/**
	 * Permite establecer la visibilidad de uno de los par�metros de entrada.
	 * 
	 * @param v
	 *            true si debe ser visible, false en caso contrario.
	 * @param i
	 *            posici�n del m�todo de entrada.
	 */
	public void setVisibilidadE(boolean v, int i) {
		this.visibilidadE[i] = v;
	}

	/**
	 * Permite establecer la visibilidad de uno de los par�metros de salida.
	 * 
	 * @param v
	 *            true si debe ser visible, false en caso contrario.
	 * @param i
	 *            posici�n del m�todo de salida.
	 */
	public void setVisibilidadS(boolean v, int i) {
		this.visibilidadS[i] = v;
	}

	/**
	 * Devuelve la visibilidad de los par�metros de salida.
	 * 
	 * @return visibilidad de los par�metros de entrada, cada posici�n contiene
	 *         el valor correspondiente a la posici�n de cada par�metro de
	 *         salida.
	 */
	public boolean[] getVisibilidadS() {
		return this.visibilidadS;
	}

	/**
	 * Devuelve la visibilidad de uno de los par�metros de salida.
	 * 
	 * @param i
	 *            posici�n del m�todo de salida.
	 * 
	 * @return visibilidad del par�metro, true si es visible, false en caso
	 *         contrario.
	 */
	public boolean getVisibilidadS(int i) {
		return this.visibilidadS[i];
	}

	/**
	 * Devuelve el n�mero de dimensiones de los par�metros de entrada.
	 * 
	 * @return N�mero de dimensiones de los par�metros de entrada, cada posici�n
	 *         contiene el valor correspondiente a la posici�n de cada par�metro
	 *         de entrada.
	 */
	public int[] getDimE() {
		return this.dimE;
	}

	/**
	 * Devuelve el n�mero de dimensiones de los par�metros de salida.
	 * 
	 * @return N�mero de dimensiones de los par�metros de entrada, cada posici�n
	 *         contiene el valor correspondiente a la posici�n de cada par�metro
	 *         de salida.
	 */
	public int[] getDimS() {
		return this.dimS;
	}
	
	/**
	 * Permite consultar si el m�todo est� marcado como principal.
	 * 
	 * @return true si est� marcado como principal, false en caso contrario.
	 */
	public boolean getEsPrincipal() {
		return this.metodoPrincipal;
	}
	
	/**
	 * Devuelve la representaci�n del m�todo.
	 * 
	 * @return representaci�n del m�todo.
	 */
	public String getInterfaz() {
		String interfaz = this.nombre + "(";

		String tipoParam[] = this.getTipoParametrosE();
		String nombreParam[] = this.getNombreParametrosE();
		int dimParam[] = this.getDimE();

		for (int i = 0; i < tipoParam.length; i++) {
			interfaz = interfaz + " " + tipoParam[i];
			if (dimParam[i] > 0) {
				interfaz = interfaz + " "
						+ ServiciosString.cadenaDimensiones(dimParam[i]);
			}
			interfaz = interfaz + " " + nombreParam[i];
			if (i < (tipoParam.length - 1)) {
				interfaz = interfaz + ",";
			}
		}

		interfaz = interfaz + " ) [ " + this.getTipo();
		if (this.getDimTipo() > 0) {
			interfaz = interfaz + " "
					+ ServiciosString.cadenaDimensiones(this.getDimTipo());
		}
		interfaz = interfaz + " ]";
		return interfaz;
	}
	
	/**
	 * Permite comparar el m�todo con otro para determinar si son iguales
	 * (Sin tener en cuenta la visibilidad que presentan).
	 * 
	 * @param dmt Objeto con el que comparar.
	 * 
	 * @return true si son iguales (Ignorando visibilidad), false en caso contrario.
	 */
	public boolean esIgual(DatosMetodoBasicos dmt) {
		
		if (!ServiciosString.sonIguales(this.nombre, dmt.nombre)) {
			return false;
		}
		
		if (this.metodoPrincipal != dmt.metodoPrincipal) {
			return false;
		}
		
		if (this.retorno != dmt.retorno) {
			return false;
		}
		
		if (this.getNumParametrosE() != dmt.getNumParametrosE()) {
			return false;
		}
		
		if (this.getNumParametrosS() != dmt.getNumParametrosS()) {
			return false;
		}
		
		for (int i = 0; i < this.getNumParametrosE(); i++) {
			
			if (!ServiciosString.sonIguales(this.nombreParamE.get(i), dmt.nombreParamE.get(i))) {
				return false;
			}
			
			if (!ServiciosString.sonIguales(this.tipoParamE.get(i), dmt.tipoParamE.get(i))) {
				return false;
			}
			
			if (this.dimE[i] != dmt.dimE[i]) {
				return false;
			}
		}
		
		for (int i = 0; i < this.getNumParametrosS(); i++) {
			
			if (!ServiciosString.sonIguales(this.nombreParamS.get(i), dmt.nombreParamS.get(i))) {
				return false;
			}
			
			if (!ServiciosString.sonIguales(this.tipoParamS.get(i), dmt.tipoParamS.get(i))) {
				return false;
			}
			
			if (this.dimS[i] != dmt.dimS[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Permite comprar el contenido de dos arrays de strings.
	 * 
	 * @param array1
	 * @param array2
	 * 
	 * @return true si son iguales, false en caso contrario.
	 */
	private boolean arraysIguales(Object[] array1, Object[] array2) {
		boolean iguales = array1.length == array2.length;
		if (iguales) {
			for (int i = 0; i < array1.length; i++) {
				if (!array1[i].equals(array2[i])) {
					iguales = false;
					break;
				}
			}
		}
		return iguales;
	}
	
	/**
	 * Permite comprar el contenido de dos arrays de enteros.
	 * 
	 * @param array1
	 * @param array2
	 * 
	 * @return true si son iguales, false en caso contrario.
	 */
	private boolean arraysEnterosIguales(int[] array1, int[] array2) {
		boolean iguales = array1.length == array2.length;
		if (iguales) {
			for (int i = 0; i < array1.length; i++) {
				if (array1[i] != array2[i]) {
					iguales = false;
					break;
				}
			}
		}
		return iguales;
	}
	
	/**
	 * Permite comparar el m�todo con otro para determinar si son iguales
	 * (Sin tener en cuenta la visibilidad que presentan).
	 * 
	 * @param dmt Objeto con el que comparar.
	 * 
	 * @return true si son iguales (Ignorando visibilidad), false en caso contrario.
	 */
	public boolean esIgual(MetodoAlgoritmo metodoAlgoritmo) {
		
		if (!ServiciosString.sonIguales(this.nombre, metodoAlgoritmo.getNombre())) {
			return false;
		}
		
		if (!arraysIguales(this.tipoParamE.toArray(), metodoAlgoritmo.getTiposParametros())) {
			return false;
		}
		
		if (!arraysEnterosIguales(this.dimE, metodoAlgoritmo.getDimParametros())) {
			return false;
		}
		
		return true;
	}
}
