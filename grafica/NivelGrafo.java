package grafica;



public class NivelGrafo
{
	public int coordMax[]=new int[0];
	public boolean ocupados[]=new boolean[0];
	
	static int valorDefecto=ContenedorArbol.espacioInicial;
	
	public NivelGrafo()
	{

	}
	
	public void setNivel(int nivel, int valorCoordenadaMaxima)
	{
		if (nivel>(coordMax.length-1))
		{
			int coordMaxAux[]=new int[nivel+1];
			for (int i=0; i<coordMax.length; i++)
				coordMaxAux[i]=coordMax[i];
				
			for (int i=coordMax.length; i<(nivel+1); i++)
				coordMaxAux[i]=valorDefecto;
				
			coordMax=coordMaxAux;
			
			boolean ocupadosAux[]=new boolean[nivel+1];
			for (int i=0; i<ocupados.length; i++)
				ocupadosAux[i]=ocupados[i];
				
			for (int i=ocupados.length; i<(nivel+1); i++)
				ocupadosAux[i]=false;
			ocupados=ocupadosAux;
		}
		if (coordMax[nivel]<valorCoordenadaMaxima)
			coordMax[nivel]=valorCoordenadaMaxima;
		ocupados[nivel]=true;
	}
	
	public int getNivel(int nivel)
	{
		if (nivel>(coordMax.length-1))
		{
			this.setNivel(nivel,valorDefecto);
			return this.getNivel(nivel);
		}
		else
		{
			int valor=coordMax[0];
			//for (int i=1; i<=nivel; i++)	// "i<=nivel"  es correcto: provoca que hijos de hermanos queden justo debajo (no está mal así)
			for (int i=1; i<coordMax.length; i++)	// un hijo de un hermano nunca queda debajo, mejor así por seguridad
				if (coordMax[i]>valor && ocupados[i])
					valor=coordMax[i];
			return valor;
		}
	}
	
	public int getNivelExacto(int nivel)
	{
		if (nivel>(coordMax.length-1))
		{
			this.setNivel(nivel,valorDefecto);
			return this.getNivel(nivel);
		}
		else
		{
			return coordMax[nivel];
		}
	}
	
	public int getMaximoAncho()
	{
		int valor=valorDefecto;
		for (int i=0; i<coordMax.length; i++)
			if (coordMax[i]>valor && ocupados[i])
				valor=coordMax[i];
		return valor;
	}
	
	
	public void mostrar()
	{	
		System.out.println("\nNivelgrafo:");
		for (int i=0;i<coordMax.length; i++)
			System.out.println("coordMax["+i+"]="+coordMax[i]+"    ocupados["+i+"]="+ocupados[i]);
		System.out.println("\n");
		
	}
}