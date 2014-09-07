package utilidades;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Clase de utilidad que permite recoger en un fichero de log los distintos
 * eventos relevantes que ocurren durante la ejecución de la aplicación.
 */
public class Logger {

	private static final String fichero = "log-SREC.txt";

	/**
	 * Registra el evento pasado por parámetro, añadiendo la fecha y hora
	 * actuales a la información.
	 * 
	 * @param s
	 *            Evento a registrar.
	 */
	public static void log_write(String s) {
		final String cadena = s;
		new Thread() {
			@Override
			public void run() {

				Calendar c = new GregorianCalendar();
				String fecha[] = getFechaActual(c);
				String hora[] = getHoraActual(c);

				String cadena2 = fecha[0] + "/" + fecha[1] + "/" + fecha[2]
						+ " " + hora[0] + ":" + hora[1] + ":" + hora[2] + " | "
						+ cadena + "\r\n";
				try {
					FileOutputStream file = new FileOutputStream(fichero, true);
					DataOutputStream out = new DataOutputStream(file);
					out.writeBytes(cadena2);
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * Devuelve la fecha actual.
	 * 
	 * @param c
	 *            Instancia de un Calendar concreto.
	 * 
	 * @return Array de strings donde cada posición contiene: 0 -> dia, 1-> mes,
	 *         2 -> año.
	 */
	private static String[] getFechaActual(Calendar c) {
		int fecha[] = new int[3];
		String fech[] = new String[3];

		fecha[0] = c.get(Calendar.DAY_OF_MONTH);
		fecha[1] = (c.get(Calendar.MONTH) + 1);
		fecha[2] = c.get(Calendar.YEAR);

		if (fecha[0] < 10) {
			fech[0] = "0" + fecha[0];
		} else {
			fech[0] = "" + fecha[0];
		}

		if (fecha[1] < 10) {
			fech[1] = "0" + fecha[1];
		} else {
			fech[1] = "" + fecha[1];
		}

		fech[2] = "" + fecha[2];
		return fech;
	}

	/**
	 * Devuelve la hora actual.
	 * 
	 * @param c
	 *            Instancia de un Calendar concreto.
	 * 
	 * @return Array de strings donde cada posición contiene: 0 -> hora, 1->
	 *         minutos, 2 -> segundos.
	 */
	private static String[] getHoraActual(Calendar c) {
		int hora[] = new int[3];
		String h[] = new String[3];

		hora[0] = c.get(Calendar.HOUR_OF_DAY);
		hora[1] = c.get(Calendar.MINUTE);
		hora[2] = c.get(Calendar.SECOND);

		if (hora[0] < 10) {
			h[0] = "0" + hora[0];
		} else {
			h[0] = "" + hora[0];
		}

		if (hora[1] < 10) {
			h[1] = "0" + hora[1];
		} else {
			h[1] = "" + hora[1];
		}

		if (hora[2] < 10) {
			h[2] = "0" + hora[2];
		} else {
			h[2] = "" + hora[2];
		}

		return h;
	}

}
