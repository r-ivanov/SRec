package datos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
}
