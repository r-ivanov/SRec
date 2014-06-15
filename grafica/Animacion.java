/**
	Thread que gestiona la animación que avanza y retrocede la visualización del algoritmo
	
	@author Antonio Pérez Carrasco
	@version 2006-2008
*/
package grafica;

import paneles.*;

public class Animacion extends Thread
{
	PanelBotonesVisualizacionArbol pbv=null;
	float segundos;
	boolean adelante;

	boolean depurar=false;
	
	/**
		Crea un nuevo thread que gestiona la animación que avanza y retrocede la visualización del algoritmo
		
		@param pbv panel de botones de visualización que gestionará el thread, indicando el sentido y la velocidad
	*/
	public Animacion (PanelBotonesVisualizacionArbol pbv)
	{
		this.pbv=pbv;
		this.segundos=this.pbv.getSeg();
		this.adelante=this.pbv.getSentido(); // Devuelve true si es hacia adelante y false si es hacia atrás
		start();
	}

	/**
		Crea un nuevo thread que gestiona la animación que avanza y retrocede la visualización del algoritmo
	*/
	public synchronized void run()
	{
		//try { wait ((int)(this.segundos*1000)); } catch (InterruptedException ie) {};
		if (depurar) System.out.println("Animacion: inicio bucle, empieza la animacion");
		do
		{
			this.adelante=pbv.getSentido();
			if (this.adelante)
			{
				pbv.hacer_avance();
				if (depurar) System.out.println("Animacion: paso hacia adelante");
			}
			else
			{
				pbv.hacer_retroc();
				if (depurar) System.out.println("Animacion: paso hacia atras");
			}
			this.segundos=pbv.getSeg();
			try { wait ((int)(this.segundos*1000)/2); } catch (InterruptedException ie) {System.out.println("Error de delay");};
			
			if (pbv.hayAnimacion())
			{
				this.segundos=pbv.getSeg();
				try { wait ((int)(this.segundos*1000)/2); } catch (InterruptedException ie) {System.out.println("Error de delay");};
			}
		}
		while (pbv.hayAnimacion());
		if (depurar) System.out.println("Animacion: fin bucle, se acaba la animacion");
		pbv.setAnimacion(0);
	}
	
	
}