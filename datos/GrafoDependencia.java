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
		this.insertarNodos(null, traza.getRaiz());
	}
	
	private void insertarNodos(NodoGrafoDependencia padre, RegistroActivacion registroActivacion) {	
		
		boolean recorrerHijos = true;
		
		if (this.nombreMetodo.equals(registroActivacion.getNombreMetodo())) {
			NodoGrafoDependencia nodo = this.obtenerNodo(registroActivacion);
			if (nodo == null) {
				nodo = new NodoGrafoDependencia(registroActivacion);
				this.nodos.add(nodo);
			} else {
				recorrerHijos = false;
			}
			if (padre != null) {
				padre.addDependencia(nodo);
			}
			padre = nodo;
		}
		
		if (recorrerHijos) {
			for (int i = 0; i < registroActivacion.numHijos(); i++) {
				this.insertarNodos(padre, registroActivacion.getHijo(i));
			}
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
