package datos;

import javax.swing.JFrame;

import grafica.ContenedorArbol;
import grafica.ContenedorCronologica;
import grafica.ContenedorEstructura;
import grafica.ContenedorPila;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import conf.Conf;

import utilidades.FotografoArbol;
import utilidades.NombresYPrefijos;
import utilidades.ServiciosString;
import ventanas.Ventana;

public class GestorTrazaExportacion {

	Traza t;// traza para la exportacion, así no tenemos que rebobinar la traza que se usa en vistas
	JGraph graph;
	int numeroVista;
	
	int alto;
	int ancho;
	
	GraphModel model = new DefaultGraphModel();
	GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
	NombresYPrefijos nyp;
	public GestorTrazaExportacion(int numeroVista)
	{
		this.t=Ventana.thisventana.getTraza().copiar();
		t.nadaVisible();
		this.numeroVista=numeroVista;
		

		boolean mostrarNombreMetodos = Ventana.thisventana.traza.getNumMetodos()!= 1;
		
		if (mostrarNombreMetodos)
		{
			nyp=new NombresYPrefijos();
			String[] nombresMetodos=Ventana.thisventana.trazaCompleta.getNombresMetodos();
			String prefijos[]=ServiciosString.obtenerPrefijos(nombresMetodos);
			for (int i=0; i<nombresMetodos.length; i++)
				nyp.add(nombresMetodos[i],prefijos[i]);
		}
	}
	
	
	public boolean finalTraza()
	{
		return t.enteroVisible();
	}
	
	public void avanzarTraza()
	{
		t.siguienteVisible();
	}
	
	public int getAncho()
	{
		return this.ancho;
	}
	
	public int getAlto()
	{
		return this.alto;
	}
	
	public JGraph grafoEstadoActual()
	{
		this.graph=null;
		GraphModel model=null;
		GraphLayoutCache view;
		Object []celdas;
		switch(numeroVista)
		{
			case 0:
				model = new DefaultGraphModel();
				view = new GraphLayoutCache(model,new DefaultCellViewFactory());
				
				this.graph = new JGraph(model, view);
				this.graph.getModel().addGraphModelListener(null);
				ContenedorArbol c=new ContenedorArbol(t.getRaiz(),this.graph,nyp,1);
				celdas=c.getCeldas();
				System.out.println("grafoEstadoActual Numero de celdas= "+celdas.length);
				this.graph.setBackground(Conf.colorPanel);
				this.graph.getGraphLayoutCache().insert(celdas);
				this.ancho=c.maximoAncho();
				this.alto=c.maximoAlto();
				FotografoArbol.gifPila = false; //Indicamos que NO vamos a hacer una animacion gif de la vista de pila
				break;
			case 1:
				model = new DefaultGraphModel();
				view = new GraphLayoutCache(model,new DefaultCellViewFactory());
				
				this.graph = new JGraph(model, view);
				this.graph.getModel().addGraphModelListener(null);
				ContenedorPila cp=new ContenedorPila(t.getRaiz(),this.graph, t.copiar(),nyp,1);
				celdas=cp.getCeldas();
				System.out.println("grafoEstadoActual Numero de celdas= "+celdas.length);
				this.graph.setBackground(Conf.colorPanel);
				this.graph.getGraphLayoutCache().insert(celdas);
				this.ancho=cp.maximoAncho();
				this.alto=cp.maximoAlto();
				FotografoArbol.gifPila = true; //Indicamos que vamos a hacer una animacion gif de la vista de pila
				break;
			case 2:
				model = new DefaultGraphModel();
				view = new GraphLayoutCache(model,new DefaultCellViewFactory());
				
				this.graph = new JGraph(model, view);
				this.graph.getModel().addGraphModelListener(null);
				ContenedorCronologica cc=new ContenedorCronologica(nyp, t.getRaiz());
				celdas=cc.getCeldas();
				System.out.println("grafoEstadoActual Numero de celdas= "+celdas.length);
				this.graph.setBackground(Conf.colorPanel);
				this.graph.getGraphLayoutCache().insert(celdas);
				this.ancho=cc.maximoAncho();
				this.alto=cc.maximoAlto();
				FotografoArbol.gifPila = true; //Indicamos que vamos a hacer una animacion gif de la vista de pila. En este caso tiene que ser TRUE
				break;
			case 3:
				model = new DefaultGraphModel();
				view = new GraphLayoutCache(model,new DefaultCellViewFactory());
				
				this.graph = new JGraph(model, view);
				this.graph.getModel().addGraphModelListener(null);
				ContenedorEstructura ce=new ContenedorEstructura(t.getRaiz(),this.graph);
				celdas=ce.getCeldas();
				System.out.println("grafoEstadoActual Numero de celdas= "+celdas.length);
				this.graph.setBackground(Conf.colorPanel);
				this.graph.getGraphLayoutCache().insert(celdas);
				this.ancho=ce.maximoAncho();
				this.alto=ce.maximoAlto();
				FotografoArbol.gifPila = false; //Indicamos que vamos NO a hacer una animacion gif de la vista de pila
				break;
			case 4:
				break;
			default:
				return null;
		}
		return this.graph;
	}
	
	
	
}
