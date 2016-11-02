package utilidades;

/**
 * Permite determinar en qué sistema operativo nos encontramos
 * @author Daniel
 *
 */
public class SsooValidator {

	private static String OS = System.getProperty("os.name").toLowerCase();
	
	/**
	 * Determina si estamos en Windows
	 * @return True si SSOO es Windows
	 */
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	/**
	 * Determina si estamos en Mac
	 * @return True si SSOO es Mac
	 */
	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	/**
	 * Determina si estamos en Unix
	 * @return True si SSOO es Unix
	 */
	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );

	}

	/**
	 * Determina si estamos en Linux o Unix
	 * @return True si SSOO es Linux o Unix
	 */
	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

}