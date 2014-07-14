package datos;

public class ParametrosParser {
	
	private MetodoAlgoritmo metodoAlgoritmo;
	
	public ParametrosParser(MetodoAlgoritmo metodoAlgoritmo) {
		this.metodoAlgoritmo = metodoAlgoritmo;
	}
	
	private int determinarCombinaciones() {
		int combinaciones = 1;
		for (int i = 0; i < this.metodoAlgoritmo.getNumeroParametros(); i++) {
			String valorParametro = this.metodoAlgoritmo.getParamValor(i);
			int numeroValores = valorParametro.split(",").length;
			combinaciones *= numeroValores; 
		}
		
		return combinaciones;
	}
	
	public String[][] obtenerMatrizParametros() {
		int combinaciones = this.determinarCombinaciones();
		String[][] matrizParametros = new String[combinaciones][];
		
		for (int i = 0; i < combinaciones; i++) {
			matrizParametros[i] = new String[this.metodoAlgoritmo.getNumeroParametros()];
		}
		
		for (int numeroParametro = 0; numeroParametro < this.metodoAlgoritmo.getNumeroParametros(); numeroParametro++) {
			String valorParametro = this.metodoAlgoritmo.getParamValor(numeroParametro);
			String[] valores = valorParametro.split(",");
			int repeticionesPorValor = combinaciones / (valores.length * (numeroParametro + 1));
			for (int i = 0; i < combinaciones; i++) {
				int posicionValor = (i / repeticionesPorValor) % valores.length;
				matrizParametros[i][numeroParametro] = valores[posicionValor];
			}
		}
		
		return matrizParametros;
	}
}
