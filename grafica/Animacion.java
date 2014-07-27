package grafica;

import paneles.PanelBotonesVisualizacionArbol;

/**
 * Thread que gestiona la animaci�n que avanza y retrocede la visualizaci�n del
 * algoritmo
 * 
 * @author Antonio P�rez Carrasco
 * @version 2006-2008
 */
public class Animacion extends Thread {
	
	private PanelBotonesVisualizacionArbol pbv = null;
	private float segundos;
	private boolean adelante;

	/**
	 * Crea un nuevo thread que gestiona la animaci�n que avanza y retrocede la
	 * visualizaci�n del algoritmo
	 * 
	 * @param pbv
	 *            panel de botones de visualizaci�n que gestionar� el thread,
	 *            indicando el sentido y la velocidad
	 */
	public Animacion(PanelBotonesVisualizacionArbol pbv) {
		this.pbv = pbv;
		this.segundos = this.pbv.getSeg();
		this.adelante = this.pbv.getSentido();
		this.start();
	}

	/**
	 * M�todo que ejecuta el thread al ser iniciado.
	 */
	@Override
	public synchronized void run() {
		do {
			this.adelante = this.pbv.getSentido();
			if (this.adelante) {
				this.pbv.hacer_avance();
			} else {
				this.pbv.hacer_retroc();
			}
			this.segundos = this.pbv.getSeg();
			try {
				this.wait((int) (this.segundos * 1000) / 2);
			} catch (InterruptedException ie) {
				System.out.println("Error de delay");
			}
			;

			if (this.pbv.hayAnimacion()) {
				this.segundos = this.pbv.getSeg();
				try {
					this.wait((int) (this.segundos * 1000) / 2);
				} catch (InterruptedException ie) {
					System.out.println("Error de delay");
				}
				;
			}
		} while (this.pbv.hayAnimacion());
		this.pbv.setAnimacion(0);
	}
}