package datos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import utilidades.MatrizDinamica;

public class GrafoDependencia {
	
	private List<NodoGrafoDependencia> nodos;
	private String nombreMetodo;
	
	public GrafoDependencia(Traza traza, String nombreMetodo) {
		this.nodos = new ArrayList<NodoGrafoDependencia>();
		this.nombreMetodo = nombreMetodo;
		this.insertarNodos(null, traza.getRaiz(), new ArrayList<NodoGrafoDependencia>());
	}
	
	private void insertarNodos(NodoGrafoDependencia padre, RegistroActivacion registroActivacion, List<NodoGrafoDependencia> procesados) {	
		
		/* Comprobamos si el nodo ya ha sido procesado */
		NodoGrafoDependencia nodo = new NodoGrafoDependencia(registroActivacion);
		boolean procesado = false;
		for (NodoGrafoDependencia visitado : procesados) {
			if (nodo.equals(visitado)) {
				procesado = true;
				nodo = visitado;
				break;
			}
		}
			
		/* Resolvemos las dependencias de los nodos hijos */
		boolean mismoMetodo = this.nombreMetodo.equals(registroActivacion.getNombreMetodo());
		if (!procesado) {
			if (mismoMetodo) {
				this.nodos.add(nodo);
			}		
			for (int i = 0; i < registroActivacion.numHijos(); i++) {
				this.insertarNodos(nodo, registroActivacion.getHijo(i), procesados);
			}
		}
		
		/* Establecemos las dependencias del padre una vez resueltas las del nodo actual */
		if (padre != null) {
			if (mismoMetodo) {
				padre.addDependencia(nodo);
			} else {
				for (NodoGrafoDependencia dependencia : nodo.getDependencias()) {
					padre.addDependencia(dependencia);
				}
			}
		}
		
		/* Establecemos el nodo como procesado si no lo estaba */
		if (!procesado) {
			procesados.add(nodo);
		}
	}
	
	private NodoGrafoDependencia obtenerNodo(RegistroActivacion registroActivacion) {
		NodoGrafoDependencia nodoObtenido = null;
		NodoGrafoDependencia nodoObjetivo = new NodoGrafoDependencia(registroActivacion);
		for (Iterator<NodoGrafoDependencia> iterator = this.nodos.iterator(); iterator.hasNext();) {
			NodoGrafoDependencia nodo = iterator.next();
			if (nodo.equals(nodoObjetivo)) {
				nodoObtenido = nodo;
				break;
			}
		}
		return nodoObtenido;
	}
	
	public List<NodoGrafoDependencia> obtenerNodos() {
		return this.nodos;
	}
	
	public MatrizDinamica<NodoGrafoDependencia> obtenerMatrizPorDefecto() {
		
		MatrizDinamica<NodoGrafoDependencia> matriz = new MatrizDinamica<NodoGrafoDependencia>();
		if (this.nodos.size() > 0) {
			NodoGrafoDependencia raiz = this.nodos.get(0);
			this.aniadirDependenciasAMatriz(matriz, raiz);
		}
		
		return matriz;
	}
	
	private void aniadirDependenciasAMatriz(MatrizDinamica<NodoGrafoDependencia> matriz, NodoGrafoDependencia raiz) {
		
		/* Insertamos el primer nodo, y lo añadimos a la cola */
		matriz.set(0, 0, raiz);
		LinkedList<NodoGrafoDependencia> colaDependencias = new LinkedList<NodoGrafoDependencia>();
		colaDependencias.add(raiz);
		
		/* Segun obtenemos dependencias, las añadimos a la cola para ir procesándolas en anchura */
		while(!colaDependencias.isEmpty()) {
			NodoGrafoDependencia nodo = colaDependencias.remove();
			int[] posicionNodo = matriz.getPosicion(nodo);
			int fila = posicionNodo[0];
			int columna = posicionNodo[1];
			for (NodoGrafoDependencia dependencia: nodo.getDependencias()) {
				if (!matriz.contiene(dependencia)) {
					int[] posicion = this.encontrarPosicionMasCercanaLibre(matriz, fila, columna);
					matriz.set(posicion[0], posicion[1], dependencia);
					colaDependencias.add(dependencia);
				}
			}
			
			/* Buscamos entre los nodos por si alguno no depende del nodo raiz, esto puede darse
			 * cuando se desea obtener las dependencias de los métodos auxiliares. */
			if (colaDependencias.isEmpty()) {
				for (NodoGrafoDependencia nodoGrafo : this.nodos) {
					if (!matriz.contiene(nodoGrafo)) {
						int[] posicion = this.encontrarPosicionMasCercanaLibre(matriz, 0, 0);
						matriz.set(posicion[0], posicion[1], nodoGrafo);
						colaDependencias.add(nodoGrafo);
						break;
					}
				}
			}
		}
	}
	
	private int[] encontrarPosicionMasCercanaLibre(MatrizDinamica<NodoGrafoDependencia> matriz, int fila, int columna) {
		int[] posicion = new int[2];
		boolean huecoEncontrado = false;
		int distancia = 0;
		while (!huecoEncontrado) {
			distancia++;
			
			if (!huecoEncontrado) {
				for (int i = fila; i < fila + distancia; i++) {
					if (matriz.get(i, columna + distancia) == null) {
						posicion[0] = i;
						posicion[1] = columna + distancia;
						huecoEncontrado = true;
					}
				}
			}
			
			if (!huecoEncontrado) {
				for (int i = columna; i < columna + distancia; i++) {
					if (matriz.get(fila + distancia, i) == null) {
						posicion[0] = fila + distancia;
						posicion[1] = i;
						huecoEncontrado = true;
					}
				}
			}
			
			if (!huecoEncontrado) {
				if (matriz.get(fila + distancia, columna + distancia) == null) {
					posicion[0] = fila + distancia;
					posicion[1] = columna + distancia;
					huecoEncontrado = true;
				}
			}
		}
		return posicion;
	}
}
