/**
	Panel que contendrá uno, dos o cuatro paneles de visualización.
	Permanece asociado en todo momento a la ventana de la aplicación, no desaparece nunca.

	@author Antonio Pérez Carrasco
	@version 2006-2007
*/
package paneles;

import java.awt.Color;
import java.awt.GridLayout;

import java.lang.OutOfMemoryError;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;


import conf.*;
import cuadros.*;
import datos.*;
import utilidades.*;
import ventanas.*;

public class PanelVentana extends JPanel
{
	static final long serialVersionUID=14;

	//PanelContenedor paneles[];		// Contiene paneles contenedores
	PanelAlgoritmo pAlgoritmo;
	GridLayout gl;
	

	/**
		Constructor: crea un nuevo panel para la ventana con una, dos o cuatro subventanas
		
		@param numPanelesTraza número de panales que se desean
	*/		
	public PanelVentana()
	{
		this.setLayout(new BorderLayout());
	
		try {
			this.pAlgoritmo=new PanelAlgoritmo();
		} catch (java.lang.Exception exp) {
			System.out.println("PanelVentana java.lang.Exception");
			exp.printStackTrace();
		}
		this.add(this.pAlgoritmo,BorderLayout.CENTER);

	}

	
	/**
		Actualiza la visualización de los paneles por si ha habido cambios en la configuración de la aplicación
	*/
	public void refrescarFormato()
	{
		pAlgoritmo.refrescarFormato(true);
	}
	
	/**
		Actualiza la visualización de los paneles por si ha habido cambios en la configuración de la aplicación
	*/
	public void refrescarOpciones()
	{
		pAlgoritmo.refrescarFormato(false);
	}
		
	

	/**
		Actualiza el zoom de la visualización
		
		@param titulo Envía el título para comprobar si debe realmente actualizarse o no
	*/
	public void refrescarZoomArbol(int valor)
	{
		pAlgoritmo.refrescarZoomArbol(valor);
	}
	
	/**
		Actualiza el zoom de la visualización (vista de pila)
		
		@param titulo Envía el título para comprobar si debe realmente actualizarse o no
	*/
	public void refrescarZoomPila(int valor)
	{
		pAlgoritmo.refrescarZoomPila(valor);
	}
	
	/**
		Actualiza el zoom de una visualización
		
	*/
	public void refrescarZoom(int vista,int valor)
	{
		pAlgoritmo.refrescarZoom(vista,valor);
	}


	public boolean haySitio()
	{
		return !pAlgoritmo.estaOcupado();
	}

	public boolean estaOcupado()
	{
		return pAlgoritmo.estaOcupado();
	}
	
	

	/**
		Abre una nueva subventana
		
		@param traza Traza que será visualizada en la nueva subventana
		@param tituloPanel Título del panel que se va a habilitar
	*/
	public void abrirPanelAlgoritmo(Traza traza, String directorio, String fichero, boolean editable) throws Exception
	{
		try {
			if (directorio!=null && fichero!=null)
				pAlgoritmo.abrirPanelCodigo(directorio+fichero,traza.getNombreMetodoEjecucion(),editable,!editable);
			pAlgoritmo.abrirVistas();
		}
		catch (Exception e) {
			pAlgoritmo=null;
			e.printStackTrace();
			throw e;
		}
					
		//abrirPanelAlgoritmo(pAlgoritmo);
		this.removeAll();
		this.add(pAlgoritmo);
		this.updateUI();
	}
	
	public void abrirPanelAlgoritmo(String fichero)
	{

		try {
			pAlgoritmo.abrirVistas(fichero);
		}
		catch (Exception e) {
			pAlgoritmo=null;
			e.printStackTrace();
		}

		//abrirPanelAlgoritmo(pAlgoritmo);
		this.removeAll();
		this.add(pAlgoritmo);
		this.updateUI();
	}	

	public void abrirPanelCodigo(String archivo, boolean editable, boolean cargarFichero)
	{
		pAlgoritmo.cerrarVistas();
		pAlgoritmo.abrirPanelCodigo(archivo,"¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿|¿||?|????????????????????",editable,cargarFichero);
	}
	
	public void cerrarPanelCodigo()
	{
		pAlgoritmo.cerrarPanelCodigo();
	}	
	
	public void setTextoCompilador(String texto)
	{
		pAlgoritmo.setTextoCompilador(texto);
	}
	

	public void guardarClase()
	{
		pAlgoritmo.guardarClase();
	}
	
	
	public void deshabilitarControles()
	{
		pAlgoritmo.deshabilitarControles();
	}
	
	public void habilitarControles()
	{
		pAlgoritmo.habilitarControles();
	}
		
	public int[] dimPanelYGrafo()
	{
		return pAlgoritmo.dimPanelYGrafo();
	}
	
	public int[] dimPanelYPila()
	{
		return pAlgoritmo.dimPanelYPila();
	}
	
	
	//public int[] dimGrafoVisibleArbolEstr()
	//{
	//	return pAlgoritmo.dimGrafoVisibleArbolEstr();
	//}
	
	public int[] dimGrafoVisibleCrono()
	{
		return pAlgoritmo.dimGrafoVisibleCrono();
	}
	
	public int[] dimGrafoVisibleEstructura()
	{
		return pAlgoritmo.dimGrafoVisibleEstructura();
	}
	
	
	//public int[] dimPanelYGrafoArbolEstr()
	//{
	//	return pAlgoritmo.dimPanelYGrafoArbolEstr();
	//}
	
	public int[] dimPanelYGrafoCrono()
	{
		return pAlgoritmo.dimPanelYGrafoCrono();
	}
	
	public int[] dimPanelYGrafoEstructura()
	{
		return pAlgoritmo.dimPanelYGrafoEstructura();
	}
	

	
	
	
	public int[] getTamanoPaneles()
	{
		int []dimensiones=new int[8];
		
		dimensiones[ 0]=dimPanelYGrafo()[0];			// Árbol
		dimensiones[ 1]=dimPanelYGrafo()[1];
		dimensiones[ 2]=dimPanelYPila()[0]; 			// Pila
		dimensiones[ 3]=dimPanelYPila()[1]; 
		if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
		{
			//dimensiones[ 4]=dimPanelYGrafoArbolEstr()[0];	// Árbol Estr
			//dimensiones[ 5]=dimPanelYGrafoArbolEstr()[1];
			dimensiones[ 4]=dimPanelYGrafoCrono()[0];		// Crono
			dimensiones[ 5]=dimPanelYGrafoCrono()[1];
			dimensiones[ 6]=dimPanelYGrafoEstructura()[0];	// Estrutura
			dimensiones[ 7]=dimPanelYGrafoEstructura()[1];
		}
		return dimensiones;
	}
	
	public int[] getTamanoGrafos()
	{
		int []dimensiones=new int[10];

		dimensiones[ 0]=dimPanelYGrafo()[2];			// Árbol
		dimensiones[ 1]=dimPanelYGrafo()[3];
		dimensiones[ 2]=dimPanelYPila()[2]; 			// Pila
		dimensiones[ 3]=dimPanelYPila()[3]; 
		if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
		{
			//dimensiones[ 4]=dimGrafoVisibleArbolEstr()[0];	// Árbol Estr
			//dimensiones[ 5]=dimGrafoVisibleArbolEstr()[1];
			dimensiones[ 4]=dimGrafoVisibleCrono()[0];		// Crono
			dimensiones[ 5]=dimGrafoVisibleCrono()[1];
			dimensiones[ 6]=dimGrafoVisibleEstructura()[0];	// Estrutura
			dimensiones[ 7]=dimGrafoVisibleEstructura()[1];
		}
		return dimensiones;
	}
	
	public String getNombresAnimaciones()
	{
		return pAlgoritmo.getNombre();
	}
	
	public String getIdTrazas()
	{
		return pAlgoritmo.getIdTraza();
	}
	
	public String[] getNombreVistasDisponibles()
	{
		return pAlgoritmo.getNombreVistasDisponibles();
	}
	
	public String[] getNombreVistasVisibles()
	{
		return pAlgoritmo.getNombreVistasVisibles();
	}
	
	public int[] getCodigosVistasVisibles()
	{
		return pAlgoritmo.getCodigoVistasVisibles();
	}
	
	public void setVistaActiva(String nombre)
	{
		System.out.println("panelvenana.setVistaActiva");
		pAlgoritmo.setVistaActiva(nombre);
	}
	
	public JComponent getPanelPorNombre(String nombre)
	{
		return pAlgoritmo.getPanelPorNombre(nombre);
	}
	
	public Object getGrafoPorNombre(String nombre)
	{
		return pAlgoritmo.getGrafoPorNombre(nombre);
	}
	
	public Object getGrafoPorNumero(int numero)
	{
		return pAlgoritmo.getGrafoPorNumero(numero);
	}
	
	
	public int[] getDimPanelPorNombre(String nombre)
	{
		return pAlgoritmo.getDimPanelPorNombre(nombre);
	}
	
	public int[] getPosicPanelPorNombre(String nombre)
	{
		return pAlgoritmo.getPosicPanelPorNombre(nombre);
	}
	
	
	
	public PanelAlgoritmo getPanelAlgoritmo()
	{
		return pAlgoritmo;
	}
	
	
	public int[] getZooms()
	{
		return pAlgoritmo.getZooms();
	}
	
	public void idioma()
	{
		pAlgoritmo.idioma();
	}
	
	// Métodos para extracción de dimensiones de paneles y grafos de visualización
	
	public int[] dimPanelPila()
	{
		return PanelAlgoritmo.dimPanelPila();
	}
	
	public int[] dimPanelPrincipal()
	{
		return pAlgoritmo.dimPanelPrincipal();
	}
	
	public int[] dimGrafoPila()
	{
		return pAlgoritmo.dimGrafoPila();
	}
	
	public int[] dimGrafoPrincipal()
	{
		return pAlgoritmo.dimGrafoPrincipal();
	}
	
	public int[] dimGrafoVisiblePrincipal()
	{
		return pAlgoritmo.dimGrafoVisiblePrincipal();
	}
	
	public int[] posicPanelPrincipal()
	{
		return pAlgoritmo.posicPanelPrincipal();
	}
	
	public JComponent getPanelArbol()
	{
		return pAlgoritmo.getPanelArbol();
	}
	
	public void distribuirPaneles(boolean[] valores1,int disposicion)
	{
		pAlgoritmo.ubicarVistas();
		pAlgoritmo.distribuirPaneles(disposicion);
	}
	
	public void distribuirPaneles(int disposicion)
	{
		pAlgoritmo.distribuirPaneles(disposicion);
	}

	public void cerrarVistas()
	{
		pAlgoritmo.cerrarVistas();
	}
	
	public void setValoresPanelControl(String tituloPanel) {
		pAlgoritmo.setValoresPanelControl(tituloPanel);
	}
	

	
	
}