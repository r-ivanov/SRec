package datos;

/**
 * Almacena una ejecución concreta de un algoritmo.
 */
public class Ejecucion {
	
	private Traza traza;
	private String ficheroFuenteDirectorio;
	private String ficheroFuente;
	private boolean ficheroEditable;
	
	public Ejecucion(Traza traza, String ficheroFuenteDirectorio, String ficheroFuente) {
		this.traza = traza;
		this.ficheroFuenteDirectorio = ficheroFuenteDirectorio;
		this.ficheroFuente = ficheroFuente;
	}

	public Traza getTraza() {
		return traza;
	}

	public String getFicheroFuenteDirectorio() {
		return ficheroFuenteDirectorio;
	}

	public String getFicheroFuente() {
		return ficheroFuente;
	}

	public boolean isFicheroEditable() {
		return ficheroEditable;
	}
	
	public DatosTrazaBasicos getDatosTrazaBasicos() {
		return new DatosTrazaBasicos(this.traza);
	}
}
