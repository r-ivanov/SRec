package datos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NodoGrafoDependencia {

	private List<NodoGrafoDependencia> dependencias;
	private RegistroActivacion registroActivacion;
	
	public NodoGrafoDependencia(RegistroActivacion registroActivacionAsociado) {
		this.dependencias = new ArrayList<NodoGrafoDependencia>();
		this.registroActivacion = registroActivacionAsociado;
	}

	public boolean equals(NodoGrafoDependencia nodo) {
		return this.clasesParametrosEntradaIguales(nodo) &&
				this.nombreParametrosEntradaIguales(nodo) &&
				this.valorParametrosEntradaIguales(nodo) &&
				this.nombreMetodoIgual(nodo);
	}
	
	private boolean nombreMetodoIgual(NodoGrafoDependencia nodo) {
		return this.registroActivacion.getNombreMetodo().equals(nodo.registroActivacion.getNombreMetodo());
	}
	
	private boolean clasesParametrosEntradaIguales(NodoGrafoDependencia nodo) {
		return this.arraysIguales(this.registroActivacion.clasesParamE(), nodo.registroActivacion.clasesParamE());
	}
	
	private boolean nombreParametrosEntradaIguales(NodoGrafoDependencia nodo) {
		return this.arraysIguales(this.registroActivacion.getNombreParametros(), nodo.registroActivacion.getNombreParametros());
	}
	
	private boolean valorParametrosEntradaIguales(NodoGrafoDependencia nodo) {
		return this.arraysIguales(this.registroActivacion.getParametros(), nodo.registroActivacion.getParametros());
	}
	
	private boolean arraysIguales(String[] array1, String[] array2) {
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
	
	public void addDependencia(NodoGrafoDependencia nodo) {
		this.dependencias.add(nodo);
	}
	
	public List<NodoGrafoDependencia> getDependencias() {
		return this.dependencias;
	}
	
	public boolean contieneDependencia(NodoGrafoDependencia nodoGrafoDependencia) {
		boolean contiene = false;
		for (Iterator<NodoGrafoDependencia> iterator = this.dependencias.iterator(); iterator.hasNext();) {
			if (iterator.next().equals(nodoGrafoDependencia)) {
				contiene = true;
				break;
			}
		}
		return contiene;
	}
}
