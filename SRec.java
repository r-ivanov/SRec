/**
	Esta clase representa la aplicación, invoca a una instancia de la ventana de la aplicación.

	@author Luis Fernández
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
		Método main de la clase Visualizador. Permite la invocación de la aplicación.
	*/
	public static void main(String args[])
	{
		if (args.length!=0)
			System.out.println("\n\n"+Texto.get("SREC_NOARG",Conf.idioma)+"\n\n");
		new SRec();
    }
}