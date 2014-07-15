package datos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NodoGrafoDependencia {

	private List<NodoGrafoDependencia> dependencias;
	private String[] nombreParametros;
	private String[] valoresParametros;

	public NodoGrafoDependencia(String[] nombreParametros,
			String[] valoresParametros) {
		this.dependencias = new ArrayList<NodoGrafoDependencia>();
		this.nombreParametros = nombreParametros;
		this.valoresParametros = valoresParametros;
	}

	public boolean equals(NodoGrafoDependencia nodoGrafoDependencia) {
		return this.tieneParametros(nodoGrafoDependencia.nombreParametros,
				nodoGrafoDependencia.valoresParametros);
	}

	public boolean tieneParametros(String[] nombreParametros,
			String[] valoresParametros) {
		return this.nombreParametros.equals(nombreParametros)
				&& this.valoresParametros.equals(valoresParametros);
	}
	
	public void addDependencia(NodoGrafoDependencia nodoGrafoDependencia) {
		this.dependencias.add(nodoGrafoDependencia);
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
