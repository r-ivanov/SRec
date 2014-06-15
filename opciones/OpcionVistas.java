/**
	Opción que permite la asignación de una máquina virtual para 
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package opciones;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

import java.io.IOException;
import org.w3c.dom.*;

import conf.Conf;
import utilidades.*;

public class OpcionVistas extends Opcion
{
	private static final long serialVersionUID = 1002;

	ArrayList<Vista> vistas=new ArrayList<Vista>();

	int posic=Conf.PANEL_VERTICAL;
	

	
	/**
		Constructor: crea una opción vacía
	*/
	public OpcionVistas()
	{
		super("OpcionVistas");
		if (this.vistas.size()==0)
			for (int i=0; i<Vista.codigos.length; i++)
			{
				Vista v=new Vista(Vista.codigos[i]);
				v.setTipo(i<=2 ? Vista.TIPO_REC : Vista.TIPO_DYV);
				v.setPanel(i%2==0 ? 2 : 1);
				v.setActiva(i<2 ? true : false);
				this.vistas.add(v);
			}
			
	}

	public Vista getVista(String codigo)
	{
		for (int i=0; i<this.vistas.size(); i++)
			if (codigo.equals(this.vistas.get(i).getCodigo()))
				return this.vistas.get(i);
		return null;
	}
	
	
	public Vista[] getVistas()
	{
		Vista[] v=new Vista[this.vistas.size()];
		
		for (int i=0; i<v.length; i++)
			v[i]=this.vistas.get(i);
		
		return v;
	}
	
	
	
	public void actualizarVista(Vista v)
	{
		//System.out.println("Antes de actualizar ("+this.vistas.size()+"): "+v.getCodigo());
		//for (int i=0; i<this.vistas.size(); i++)
		//	System.out.println(" > "+this.vistas.get(i).getCodigo());
		
		for (int i=0; i<this.vistas.size(); i++)
			if (v.getCodigo().equals(this.vistas.get(i).getCodigo()))
			{
				Vista vieja=this.vistas.get(i);
				this.vistas.add(i,v);
				this.vistas.remove(vieja);
			}
		//System.out.println("Despues de actualizar ("+this.vistas.size()+"): "+v.getCodigo());
		//for (int i=0; i<this.vistas.size(); i++)
		//	System.out.println(" > "+this.vistas.get(i).getCodigo());
		
	}
	
	
	public void setDisposicion(int posic)
	{
		this.posic=posic;
	}


	public int getDisposicion()
	{
		return this.posic;
	}
	

	
	
	public Element getRepresentacionElement(Document d)
	{
		Element e=d.createElement("OpcionVistas");
		//System.out.println("getRepresentacionElement INICIO: "+this.vistas.size());
		
		for (int i=0; i<this.vistas.size(); i++)
		{
			Element eV=d.createElement("Vista");
			
			eV.setAttribute("codigo", this.vistas.get(i).getCodigo());
			eV.setAttribute("activa", ""+this.vistas.get(i).esActiva());
			eV.setAttribute("tipo", ""+this.vistas.get(i).getTipo());
			eV.setAttribute("panel", ""+this.vistas.get(i).getPanel());
			
			e.appendChild(eV);
		}
		
		Element eDP=d.createElement("Disposicion");
		eDP.setAttribute("posic", ""+this.posic);
		e.appendChild(eDP);
		
		//System.out.println("getRepresentacionElement FINAL: "+this.vistas.size());
		return e;
	}
	
	
	public void setValores(Element e)
	{
		Element elements[];
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("Vista"));
		//System.out.println("setValores INICIO: ["+elements.length+"]  "+this.vistas.size());
		for (int i=0; i<elements.length; i++)
		{
			Vista v=new Vista(elements[i].getAttribute("codigo"));
			v.setActiva(elements[i].getAttribute("activa").equals("true"));
			v.setTipo(Integer.parseInt(elements[i].getAttribute("tipo")));
			v.setPanel(Integer.parseInt(elements[i].getAttribute("panel")));
			
			this.actualizarVista(v);
		}
		
		//System.out.println("setValores FINAL: "+this.vistas.size());
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("Disposicion"));
		
		this.posic=Integer.parseInt(elements[0].getAttribute("posic"));
		
	
	}
	
	
	
	
	
	
	/**
		Gestiona la lectura desde fichero
		
		@param stram
	*/
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
	{

	}

	/**
		Gestiona la escritura a fichero
		
		@param stram
	*/
	private void writeObject(ObjectOutputStream stream)throws IOException
	{

	}

}