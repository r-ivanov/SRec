import ventanas.*;
import conf.*;
import utilidades.*;

/**
 * Representa la aplicaci�n.
 * 
 * @author Luis Fern�ndez
 */
public class SRec {
	
	/**
	 * Invoca una nueva instancia de la ventana de la aplicaci�n.
	 */
	public SRec() {
		new Ventana();
	}

	/**
	 * M�todo main de la aplicaci�n. Permite la invocaci�n de la ventana de la
	 * aplicaci�n.
	 */
	public static void main(String args[]) {
		if (args.length != 0) {
			System.out.println("\n\n" + Texto.get("SREC_NOARG", Conf.idioma) + "\n\n");
		}
		new SRec();
	}
}