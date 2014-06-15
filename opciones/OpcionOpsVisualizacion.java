/**
	Opción que permite la configuración de algunas opciones relacionadas con las visualizaciones
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package opciones;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.IOException;
import org.w3c.dom.*;

import conf.*;
import utilidades.*;

public class OpcionOpsVisualizacion extends Opcion
{
	private static final long serialVersionUID = 1076;

	public static final int MANTENER_HISTORIA = 10;
	public static final int ATENUAR_HISTORIA = 11;
	public static final int ELIMINAR_HISTORIA = 12;
	
	public static final int DATOS_TODOS = 30;
	public static final int DATOS_ENTRADA = 31;
	public static final int DATOS_SALIDA = 32;
	
	
	
	// Atributos (asignamos valores por defecto)
	private int historia = OpcionOpsVisualizacion.MANTENER_HISTORIA;
	private int datosMostrar = OpcionOpsVisualizacion.DATOS_TODOS;
	private boolean mostrarArbolSalto = true;

	private boolean mostrarVisor = true;//Conf.mostrarVisor;
	
	private boolean mostrarEstructuraEnArbol = true;
	
	private boolean mostrarEstructCompletaCrono = false;
	private boolean mostrarSalidaLigadaEntrada = false;
	
	private boolean mostrarArbolColapsado = false;
	
	private boolean sangrado = true;
	private boolean idMetodoTraza = true;
	private boolean soloEstructuraDYVcrono = true;
	private boolean arranqueEstadoInicial = true;
	private boolean ajustarVistaGlobal = true;
	private boolean visualizacionDinamica = true;
	
	/**
		Constructor: Crea una nueva instancia, con valores por defecto, para la opción OpcionOpsVisualizacion
	*/
	public OpcionOpsVisualizacion()
	{
		super("OpcionOpsVisualizacion");
	}

	/**
		Asigna el valor dado al atributo conHistoria
		
		@param x valor que se quiere asignar a conHistoria
	*/
	public void setHistoria(int x)
	{
		if (x>=MANTENER_HISTORIA && x<=ELIMINAR_HISTORIA)
			this.historia=x;
	}
	
	public void setHistoria(String s)
	{
		if (s.equals("Mantener"))
			setHistoria(MANTENER_HISTORIA);
		else if (s.equals("Atenuar"))
			setHistoria(ATENUAR_HISTORIA);
		else if (s.equals("Eliminar"))
			setHistoria(ELIMINAR_HISTORIA);
	}
	
	
	/**
		Asigna el valor dado al atributo mostrarArbolSalto
		
		@param x valor que se quiere asignar a datosMostrar
	*/	
	public void setMostrarArbolSalto(boolean b)
	{
		this.mostrarArbolSalto=b;
	}
	
	/**
		Asigna el valor dado al atributo datosMostrar
		
		@param x valor que se quiere asignar a datosMostrar
	*/	
	public void setDatosMostrar(int x)
	{
		if (x>=DATOS_TODOS && x<=DATOS_SALIDA)	
			this.datosMostrar=x;
	}
	
	
	public void setDatosMostrar(String s1, String s2)
	{
		if (s1.equals("true") && s2.equals("true"))
			this.setDatosMostrar(DATOS_TODOS);
		else if (s1.equals("true") && s2.equals("false"))
			this.setDatosMostrar(DATOS_ENTRADA);
		else if (s1.equals("false") && s2.equals("true"))
			this.setDatosMostrar(DATOS_SALIDA);
	}


	
	/**
		Asigna los valores dados a los atributos correspondientes
		
		@param a valor que se quiere asignar a conHistoria
		@param b valor que se quiere asignar a datosMostrar
		@param c valor que se quiere asignar a tecnicaDiseno
	*/	
	public void setValores(int a, int b, int c)
	{
		if (a>=MANTENER_HISTORIA && a<=ELIMINAR_HISTORIA)
			this.historia=a;
			
		if (b>=DATOS_TODOS && b<=DATOS_SALIDA)	
			this.datosMostrar=b;
		

	}
	
	/**
		Devuelve el valor del atributo historia
		
		@return valor de atributo historia
	*/	
	public int getHistoria()
	{
		return this.historia;
	}
	
	public String getHistoriaString()
	{
		if (this.historia==MANTENER_HISTORIA)
			return "Mantener";
		else if (this.historia==ATENUAR_HISTORIA)
			return "Atenuar";
		else if (this.historia==ELIMINAR_HISTORIA)
			return "Eliminar";
		return "Mantener";
	}
	
	/**
		Devuelve el valor del atributo datosMostrar
		
		@return valor de atributo datosMostrar
	*/	
	public int getDatosMostrar()
	{
		return this.datosMostrar;
	}
	
	
	/**
		Devuelve el valor del atributo datosMostrar
		
		@return valor de atributo datosMostrar
	*/	
	public boolean getMostrarArbolSalto()
	{
		return this.mostrarArbolSalto;
	}	
	
	
	
	
	
	public boolean getMostrarVisor()
	{
		return this.mostrarVisor;
	}
	
	public void setMostrarVisor(boolean valor)
	{
		this.mostrarVisor=valor;
	}
	
	
	
	
	public boolean getMostrarArbolColapsado()
	{
		return this.mostrarArbolColapsado;
	}
	
	public void setMostrarArbolColapsado(boolean valor)
	{
		this.mostrarArbolColapsado=valor;
	}
	
	
	public boolean getSangrado()
	{
		return this.sangrado;
	}
	
	public void setSangrado(boolean valor)
	{
		this.sangrado=valor;
	}
	

	public boolean getIdMetodoTraza()
	{
		return this.idMetodoTraza;
	}
	
	public void setIdMetodoTraza(boolean valor)
	{
		this.idMetodoTraza=valor;
	}
	
	public boolean getSoloEstructuraDYVcrono()
	{
		return this.soloEstructuraDYVcrono;
	}
	
	public void setSoloEstructuraDYVcrono(boolean valor)
	{
		this.soloEstructuraDYVcrono=valor;
	}
	
	public boolean getMostrarEstructuraCompletaCrono()
	{
		return this.mostrarEstructCompletaCrono;
	}
	
	public void setMostrarEstructuraCompletaCrono(boolean v)
	{
		this.mostrarEstructCompletaCrono=v;
	}
	
	public boolean getArranqueEstadoInicial()
	{
		return this.arranqueEstadoInicial;
	}
	
	public void setArranqueEstadoInicial(boolean v)
	{
		this.arranqueEstadoInicial=v;
	}
	
	public boolean getAjustarVistaGlobal()
	{
		return this.ajustarVistaGlobal;
	}
	
	public void setAjustarVistaGlobal(boolean v)
	{
		this.ajustarVistaGlobal=v;
	}
	
	public boolean getVisualizacionDinamica()
	{
		return this.visualizacionDinamica;
	}
	
	public void setVisualizacionDinamica(boolean v)
	{
		this.visualizacionDinamica=v;
	}
	
	public boolean getMostrarEstructuraEnArbol()
	{
		return this.mostrarEstructuraEnArbol;
	}
	
	public void setMostrarEstructuraEnArbol(boolean v)
	{
		this.mostrarEstructuraEnArbol=v;
	}
	
	
	
	
	
	
	public boolean getMostrarSalidaLigadaEntrada()
	{
		return this.mostrarSalidaLigadaEntrada;
	}
	
	public void setMostrarSalidaLigadaEntrada(boolean v)
	{
		this.mostrarSalidaLigadaEntrada=v;
	}
	
	
	
	
	
	public boolean mostrarEntrada()
	{
		return datosMostrar==DATOS_TODOS || datosMostrar==DATOS_ENTRADA;
	}
	
	public boolean mostrarSalida()
	{
		return datosMostrar==DATOS_TODOS || datosMostrar==DATOS_SALIDA;
	}

	
	public Element getRepresentacionElement(Document d)
	{
		Element e=d.createElement("OpcionOpsVisualizacion");
		
		Element e01=d.createElement("historia");
		e01.setAttribute("valor",this.getHistoriaString());
		
		Element e02=d.createElement("datosMostrar");
		e02.setAttribute("entrada",""+this.mostrarEntrada());
		e02.setAttribute("salida",""+this.mostrarSalida());
		
		Element e03=d.createElement("mostrarArbolSalto");
		e03.setAttribute("valor",""+this.getMostrarArbolSalto());

		Element e04=d.createElement("mostrarVisor");
		e04.setAttribute("valor",""+this.getMostrarVisor());
		
		Element e05=d.createElement("mostrarECCrono");
		e05.setAttribute("valor",""+this.getMostrarEstructuraCompletaCrono());
		
		Element e06=d.createElement("mostrarSalidaLigadaEntrada");
		e06.setAttribute("valor",""+this.getMostrarSalidaLigadaEntrada());
		
		Element e07=d.createElement("mostrarEstructuraArbol");
		e07.setAttribute("valor",""+this.getMostrarEstructuraEnArbol());
		
		Element e08=d.createElement("mostrarArbolColapsado");
		e08.setAttribute("valor",""+this.getMostrarArbolColapsado());
		
		Element e09=d.createElement("sangrado");
		e09.setAttribute("valor",""+this.getSangrado());
		
		Element e10=d.createElement("idMetodoTraza");
		e10.setAttribute("valor",""+this.getIdMetodoTraza());
		
		Element e11=d.createElement("soloEstructuraDYVcrono");
		e11.setAttribute("valor",""+this.getSoloEstructuraDYVcrono());
		
		Element e12=d.createElement("arranqueEstadoInicial");
		e12.setAttribute("valor",""+this.getArranqueEstadoInicial());
		
		Element e13=d.createElement("ajustarVistaGlobal");
		e13.setAttribute("valor",""+this.getAjustarVistaGlobal());
		
		Element e14=d.createElement("visualizacionDinamica");
		e14.setAttribute("valor",""+this.getVisualizacionDinamica());
		
		e.appendChild(e01);
		e.appendChild(e02);
		e.appendChild(e03);
		e.appendChild(e04);
		e.appendChild(e05);
		e.appendChild(e06);
		e.appendChild(e07);
		e.appendChild(e08);
		e.appendChild(e09);
		e.appendChild(e10);
		e.appendChild(e11);
		e.appendChild(e12);
		e.appendChild(e13);
		e.appendChild(e14);
		
		return e;
	}
	
	
	public void setValores(Element e)
	{
		
		/*Element hijos[]=ManipulacionElement.nodeListToElementArray(e.getChildNodes());
		for (int i=0; i<hijos.length; i++)
			System.out.println("Hijo "+i+": "+hijos[i].getNodeName());*/
		
		Element elements[]=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("historia"));
		this.setHistoria(elements[0].getAttribute("valor"));
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("datosMostrar"));
		this.setDatosMostrar(elements[0].getAttribute("entrada"),elements[0].getAttribute("salida"));
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("mostrarArbolSalto"));
		this.setMostrarArbolSalto(elements[0].getAttribute("valor").equals("true"));	

		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("mostrarVisor"));
		this.setMostrarVisor(elements[0].getAttribute("valor").equals("true"));	

		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("mostrarECCrono"));
		this.setMostrarEstructuraCompletaCrono(elements[0].getAttribute("valor").equals("true"));
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("mostrarEstructuraArbol"));
		this.setMostrarEstructuraEnArbol(elements[0].getAttribute("valor").equals("true"));	
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("mostrarSalidaLigadaEntrada"));
		if (elements.length>0)
			this.setMostrarSalidaLigadaEntrada(elements[0].getAttribute("valor").equals("true"));
		else
			this.setMostrarSalidaLigadaEntrada(false);
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("mostrarArbolColapsado"));
		if (elements.length>0)
			this.setMostrarArbolColapsado(elements[0].getAttribute("valor").equals("true"));
		else
			this.setMostrarArbolColapsado(false);
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("sangrado"));
		if (elements.length>0)
			this.setSangrado(elements[0].getAttribute("valor").equals("true"));
		else
			this.setSangrado(false);
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("idMetodoTraza"));
		if (elements.length>0)
			this.setIdMetodoTraza(elements[0].getAttribute("valor").equals("true"));
		else
			this.setIdMetodoTraza(false);
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("soloEstructuraDYVcrono"));
		if (elements.length>0)
			this.setSoloEstructuraDYVcrono(elements[0].getAttribute("valor").equals("true"));
		else
			this.setSoloEstructuraDYVcrono(false);
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("arranqueEstadoInicial"));
		if (elements.length>0)
			this.setArranqueEstadoInicial(elements[0].getAttribute("valor").equals("true"));
		else
			this.setArranqueEstadoInicial(false);
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("ajustarVistaGlobal"));
		if (elements.length>0)
			this.setAjustarVistaGlobal(elements[0].getAttribute("valor").equals("true"));
		else
			this.setAjustarVistaGlobal(false);
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("visualizacionDinamica"));
		if (elements.length>0)
			this.setVisualizacionDinamica(elements[0].getAttribute("valor").equals("true"));
		else
			this.setVisualizacionDinamica(false);
	}
	
	
	
	// Métodos serialización
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
	{
		this.historia=stream.readInt();
		this.datosMostrar=stream.readInt();
	}

	private void writeObject(ObjectOutputStream stream)throws IOException
	{
		stream.writeInt(this.historia);
		stream.writeInt(this.datosMostrar);
	}

}