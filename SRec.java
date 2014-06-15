/**
	Esta clase representa la aplicaci�n, invoca a una instancia de la ventana de la aplicaci�n.

	@author Luis Fern�ndez
	@version Desconocida

*/

import ventanas.*;
import conf.*;
import utilidades.*;

public class SRec
{
	/**
		Invoca una ventana del visualizador.
	*/
	public SRec()
	{
		new Ventana();
	}

	/**
		M�todo main de la clase Visualizador. Permite la invocaci�n de la aplicaci�n.
	*/
	public static void main(String args[])
	{
		if (args.length!=0)
			System.out.println("\n\n"+Texto.get("SREC_NOARG",Conf.idioma)+"\n\n");
		new SRec();
    }
}