package datos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GrafoDependencia {
	
	private List<NodoGrafoDependencia> nodos;
	private Traza traza;
	private String nombreMetodo;
	
	public GrafoDependencia(Traza traza, String nombreMetodo) {
		this.nodos = new ArrayList<NodoGrafoDependencia>();
		this.traza = traza;
		this.nombreMetodo = nombreMetodo;
	}
	
	private boolean contieneNodo(NodoGrafoDependencia nodo) {
		boolean contiene = false;
		for (Iterator<NodoGrafoDependencia> iterator = this.nodos.iterator(); iterator.hasNext();) {
			if (iterator.next().equals(nodo)) {
				contiene = true;
				break;
			}
		}
		return contiene;
	}
	
	private NodoGrafoDependencia obtenerNodo(RegistroActivacion registroActivacion) {
		NodoGrafoDependencia nodoADevolver = null;
		for (Iterator<NodoGrafoDependencia> iterator = this.nodos.iterator(); iterator.hasNext();) {
			NodoGrafoDependencia nodo = iterator.next();
			if (nodo.tieneParametros(registroActivacion.getNombreParametros(), registroActivacion.getParametros())) {
				nodoADevolver = nodo;
				break;
			}
		}
		return nodoADevolver;
	}
}
