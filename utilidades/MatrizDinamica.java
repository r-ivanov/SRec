package utilidades;

import java.util.ArrayList;
import java.util.List;

public class MatrizDinamica<T> {

	List<List<T>> matriz;
	
	public MatrizDinamica() {
		this.matriz = new ArrayList<List<T>>();
	}
	
	public void set(int fila, int columna, T elemento) {
		int ultimaFila = matriz.size() - 1;
		for (int i = ultimaFila; i < fila; i++) {
			this.matriz.add(new ArrayList<T>());
		}
		
		for (List<T> filaMatriz : this.matriz) {
			int ultimaColumna = filaMatriz.size() - 1;
			for (int j = ultimaColumna; j < Math.max(columna, this.numColumnas() - 1); j++) {
				filaMatriz.add(null);
			}
		}
		
		this.matriz.get(fila).set(columna, elemento);
	}
	
	public boolean contiene(T elemento) {
		int[] posicion = this.getPosicion(elemento);
		return posicion[0] != -1 && posicion[1] != -1;
	}
	
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
	
	public T get(int fila, int columna) {
		if (fila >= this.numFilas() || columna >= this.numColumnas()) {
			return null;
		}
		return this.matriz.get(fila).get(columna);
	}
	
	public int numFilas() {
		return this.matriz.size();
	}
	
	public int numColumnas() {
		if (matriz.size() == 0) {
			return 0;
		}
		return matriz.get(0).size();
	}
}
