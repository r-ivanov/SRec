/**
	Botón de la aplicación, que mantendrá una interfaz similar a la de otros botones, basada en imágenes
	
	@author Antonio Pérez Carrasco
	@version 2006-2008
*/
package botones;




import java.awt.Dimension;



import javax.swing.JButton;

import javax.swing.ImageIcon;

import conf.*;


public class Boton extends JButton //implements MouseListener
{
	static final long serialVersionUID=15;	

	static final int VERDE=1;
	static final int NARANJA=2;
	static final int ROJO=3;
	
	boolean raton=false; // false=raton no esta sobre boton, true=raton sí está dentro del área del boton
	
	String verde;
	String rojo;
	String naranja;
	int estado;
	
	/**
		Constructor: genera un nuevo botón, reinicializando algunos de sus atributos
		
		@param verde dirección de la imagen que se usará para mostrar cuando el botón está verde, disponible para ser pulsado
		@param naranja dirección de la imagen que se usará para mostrar cuando el botón está naranja, pulsado o con el ratón encima
		@param rojo dirección de la imagen que se usará para mostrar cuando el botón está rojo, deshabilitado por algún motivo
		@param ancho ancho del botón
		@param alto alto del botón
	*/
	public Boton(String verde, String naranja, String rojo, int ancho, int alto)
	{
		super(new ImageIcon(verde));
		//this.setText("Boton");
		this.verde=verde;
		this.naranja=naranja;
		this.rojo=rojo;
		this.setPreferredSize(new Dimension(ancho, alto));
		estado=VERDE;
	}
	
	
		/**
		Constructor: genera un nuevo botón, reinicializando algunos de sus atributos
		
		@param verde dirección de la imagen que se usará para mostrar cuando el botón está verde, disponible para ser pulsado
		@param naranja dirección de la imagen que se usará para mostrar cuando el botón está naranja, pulsado o con el ratón encima
		@param rojo dirección de la imagen que se usará para mostrar cuando el botón está rojo, deshabilitado por algún motivo
		@param ancho ancho del botón
		@param alto alto del botón
	*/
	public Boton(ImageIcon ii, int ancho, int alto)
	{
		super(ii);
		this.setPreferredSize(new Dimension(ancho, alto));
		estado=VERDE;
	}
	

	/**
		Constructor: genera un nuevo botón, reinicializando algunos de sus atributos
		
		@param texto texto que aparecerá en el botón
	*/
	public Boton(String texto)
	{
		//super(new ImageIcon(cadenaimagen));
		this.setText(texto);
		/*this.verde=verde;
		this.naranja=naranja;
		this.rojo=rojo;*/

		this.setPreferredSize(new Dimension(Conf.anchoBoton, Conf.altoBoton));
		
		estado=VERDE;
	}
	
	/**
		Constructor: genera un nuevo botón, reinicializando algunos de sus atributos
		
		@param texto texto que aparecerá en el botón
	*/
	public Boton(String texto, int ancho, int alto)
	{
		//super(new ImageIcon(cadenaimagen));
		this.setText(texto);
		/*this.verde=verde;
		this.naranja=naranja;
		this.rojo=rojo;*/

		this.setPreferredSize(new Dimension(ancho,alto));
		
		estado=VERDE;
	}
	
	/**
	Constructor: genera un nuevo botón, reinicializando algunos de sus atributos
	
	@param texto texto que aparecerá en el botón
	*/
	public Boton(String texto, int ancho)
	{
		//super(new ImageIcon(cadenaimagen));
		this.setText(texto);
		/*this.verde=verde;
		this.naranja=naranja;
		this.rojo=rojo;*/
	
		this.setPreferredSize(new Dimension(ancho,Conf.altoBoton));
		
		estado=VERDE;
	}

	/**
		Cambia el estado del botón a verde y hace que la imagen verde sea visible
	*/	
	public void setVerde()
	{
		this.setEnabled(true);
		if (verde!=null)
			if (!this.tieneRaton())
				this.setIcon(new ImageIcon(verde));
		estado=VERDE;
		//this.setEnabled(true);
	}
	
	/**
		Cambia el estado del botón a rojo y hace que la imagen rojo sea visible
	*/	
	public void setRojo()
	{
		this.setEnabled(false);
		if (rojo!=null)
			if (!this.tieneRaton())
				this.setIcon(new ImageIcon(rojo));
		estado=ROJO;
		//this.setEnabled(false);
	}
	
	/**
		Hace que la imagen naranja sea visible, no cambia el estado del botón
	*/		
	public void setNaranja()
	{
		if (naranja!=null)
			this.setIcon(new ImageIcon(naranja));
	}
	
	/**
		Devuelve el color correspondiente al estado en que se encuetra el botón
	*/		
	public void setColorEstado()
	{
		if (this.estado==VERDE)
			this.setVerde();
		else if (this.estado==ROJO)
			this.setRojo();
	}
	
	/**
		Comprueba si el estado del botón es verde
		
		@return true si el botón está verde
	*/		
	public boolean estaVerde()
	{
		return estado==VERDE;
	}
	
	/**
		Comprueba si el estado del botón es naranja (nunca debería dar true)
		
		@return true si el botón está naranja
	*/		
	public boolean estaNaranja()
	{
		return estado==NARANJA;
	}
	
	/**
		Comprueba si el estado del botón es rojo
		
		@return true si el botón está rojo
	*/			
	public boolean estaRojo()
	{
		return estado==ROJO;
	}
	
	/**
		Asigna una imagen para un determinado color
		
		@param imagen dirección de la imagen que se quire asignar
		@param color al que se asigna la imagen
	*/			
	public void setImagen(String imagen,int color)
	{
		if (color==VERDE)
			verde=imagen;
		else if (color==NARANJA)
			naranja=imagen;
		else if (color==ROJO)
			rojo=imagen;
	}
	
	/**
		Asigna true o false al botón según tenga el ratón encima
		
		@param tiene true si el ratón está sobre el botón
	*/
	public void tieneRaton(boolean tiene)
	{
		this.raton=tiene;
	}
	
	/**
		Comrpueba si el botón tiene el ratón encima
		
		@return true si el ratón está sobre el botón
	*/
	public boolean tieneRaton()
	{
		return this.raton;
	}
}