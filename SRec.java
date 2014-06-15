import ventanas.*;
import conf.*;
import utilidades.*;

/**
 * Representa la aplicación.
 * 
 * @author Luis Fernández
 */
public class SRec {
	
	/**
	 * Invoca una nueva instancia de la ventana de la aplicación.
	 */
	public SRec() {
		new Ventana();
	}

	/**
	 * Método main de la aplicación. Permite la invocación de la ventana de la
	 * aplicación.
	 */
	public static void main(String args[]) {
		if (args.length != 0) {
			System.out.println("\n\n" + Texto.get("SREC_NOARG", Conf.idioma) + "\n\n");
		}
		new SRec();
	}
}