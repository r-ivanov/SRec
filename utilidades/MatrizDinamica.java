package utilidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Estructura de datos que permite almacenar datos en cualquier posici�n sin
 * necesidad de reservar espacio previamente, la matriz , su altura, y su
 * anchura, se ajustan autom�ticamente seg�n las operaciones que se realizan
 * sobre ella.
 * 
 * @author David Pastor Herranz
 *
 * @param <T>
 *            Tipo de datos que contendr� la matriz.
 */
public class MatrizDinamica<T> {

	List<List<T>> matriz;

	/**
	 * Devuelve una nueva instancia de una matriz.
	 */
	public MatrizDinamica() {
		this.matriz = new ArrayList<List<T>>();
	}

	/**
	 * Setea el elemento pasado por par�metro en la posici�n dada por la fila y
	 * la columna.
	 * 
	 * @param fila
	 *            Fila
	 * @param columna
	 *            Columna
	 * @param elemento
	 *            Elemento a establecer.
	 */
	public void set(int fila, int columna, T elemento) {
		int ultimaFila = this.matriz.size() - 1;
		for (int i = ultimaFila; i < fila; i++) {
			this.matriz.add(new ArrayList<T>());
		}

		for (List<T> filaMatriz : this.matriz) {
			int ultimaColumna = filaMatriz.size() - 1;
			for (int j = ultimaColumna; j < Math.max(columna,
					this.numColumnas() - 1); j++) {
				filaMatriz.add(null);
			}
		}

		this.matriz.get(fila).set(columna, elemento);
	}

	/**
	 * Determina si el elemento pasado por par�metro est� en la matriz.
	 * 
	 * @param elemento
	 *            Elemento.
	 * 
	 * @return true si est� en la matriz, false en caso contrario.
	 */
	public boolean contiene(T elemento) {
		int[] posicion = this.getPosicion(elemento);
		return posicion[0] != -1 && posicion[1] != -1;
	}

	/**
	 * Devuelve la posici�n del elemento en la matriz.
	 * 
	 * @param elemento
	 *            Elemento.
	 * 
	 * @return [fila, columna], siendo ambos valores -1 si el elmento no se
	 *         encuentra en la matriz.
	 */
	public int[] getPosicion(T elemento) {
		int[] posicion = new int[2];
		posicion[0] = -1;
		posicion[1] = -1;
		if (elemento != null) {
			for (int i = 0; i < this.matriz.size(); i++) {
				for (int j = 0; j < this.matriz.get(i).size(); j++) {
					if (elemento.equals(this.get(i, j))) {
						posicion[0] = i;
						posicion[1] = j;
						break;
					}
				}
			}
		}
		return posicion;
	}

	/**
	 * Devuelve el elemento que se encuentra en la posici�n dada por la fila y
	 * columna especificadas.
	 * 
	 * @param fila
	 *            Fila
	 * @param columna
	 *            Columna
	 * 
	 * @return Elemento que se encuentra en la posici�n indicada, null si no hay
	 *         ning�n elemento en dicha posici�n.
	 */
	public T get(int fila, int columna) {
		if (fila >= this.numFilas() || columna >= this.numColumnas()) {
			return null;
		}
		return this.matriz.get(fila).get(columna);
	}

	/**
	 * Devuelve el n�mero de filas actuales en la matriz.
	 * 
	 * @return N�mero de filas actuales en la matriz.
	 */
	public int numFilas() {
		return this.matriz.size();
	}

	/**
	 * Devuelve el n�mero de columnas actuales en la matriz.
	 * 
	 * @return N�mero de columnas actuales en la matriz.
	 */
	public int numColumnas() {
		if (this.matriz.size() == 0) {
			return 0;
		}
		return this.matriz.get(0).size();
	}
	
	/**
	 * Obtiene filas y columnas vacias, que no tienen nodos
	 * @return
	 * 		Array de arrays donde la primera posici�n son las filas vac�as
	 * 		y la segunda posici�n las columnas vac�as
	 */
	public boolean[][] filasYColumnasVacias(){
		
		boolean[] filasVacias = new boolean[this.matriz.size()];
		boolean[] columnasVacias = new boolean[this.matriz.get(0).size()];
		java.util.Arrays.fill(filasVacias, Boolean.TRUE);
		java.util.Arrays.fill(columnasVacias, Boolean.TRUE);		
		
		for (int i = 0; i < this.matriz.size(); i++) {			
			for (int j = 0; j < this.matriz.get(i).size(); j++) {
				if (this.get(i, j)!=null) {
					filasVacias[i]=false;	
					columnasVacias[j]=false;
				}
			}
			
		}
		
		boolean[][] valorDevuelto = new boolean[][] {filasVacias, columnasVacias};		
		return valorDevuelto;
	}
}
