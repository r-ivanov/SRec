/**
	Contiene a los paneles de traza y control de traza. Está contenido en un panel contenedor del panel de la ventana
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package paneles;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;

import javax.swing.JComponent;

import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import opciones.GestorOpciones;
import opciones.OpcionVistas;
import opciones.Vista;

import conf.Conf;
import datos.MetodoAlgoritmo;



import java.lang.OutOfMemoryError;


import utilidades.*;
import ventanas.*;


public class PanelAlgoritmo extends JPanel implements ChangeListener
{
	static final long serialVersionUID=14;

	static JSplitPane separadorCodigo, separadorVistas, separadorCentral;
	
	GestorOpciones gOpciones=new GestorOpciones();
	
	// Paneles que están contenidos en este panel
	
	
	static PanelCodigo pCodigo;
	static PanelCompilador pCompilador;
	
	static PanelTraza pTraza;
	static PanelPila pPila;
	static PanelArbol pArbol;

	static PanelCrono pCrono;
	static PanelEstructura pEstructura;
	
	
	static PanelControl pControl;
	
	String tituloPanel;
	String idTraza;
	
	String[] nombresMetodos;
	public static NombresYPrefijos nyp=null;
	
	static JScrollPane jspCompilador, jspCodigo, jspTraza, jspPila, jspCrono, jspEstructura;
	JPanel jspArbol;
	
	JPanel contenedorCompilador, contenedorCodigo, contenedorTraza, contenedorPila, contenedorArbol, contenedorControl, 
					contenedorCrono, contenedorEstructura;
	
	JTabbedPane panel1,panel2;
	
	boolean mostrarNombreMetodos=false;
	
	boolean ocupado=false;
	
	boolean abriendoVistas=false;
	
	JPanel panelGral;
	
	String[] nombresVistas=new String[Vista.codigos.length];
	boolean[] vistasActualizadas=new boolean[Vista.codigos.length];
	
	// Panel de paneles vacíos
	public PanelAlgoritmo() throws Exception
	{
		// Creamos el panel de la izquierda (contiene el panel del código y el de la traza)
		JPanel izqda=new JPanel();
		izqda.setLayout(new BorderLayout());
		
		pCodigo = new PanelCodigo(null,null);
		pTraza = new PanelTraza();
		pCompilador = new PanelCompilador(this);
		
		
		//jspCodigo = new JScrollPane(pCodigo);
		jspTraza = new JScrollPane(pTraza);
		//jspCodigo.setPreferredSize(new Dimension(250,250));
		
		contenedorCodigo=new JPanel();
		contenedorCompilador=new JPanel();
		contenedorTraza=new JPanel();
		contenedorCodigo.setLayout(new BorderLayout());
		contenedorCompilador.setLayout(new BorderLayout());
		contenedorTraza.setLayout(new BorderLayout());
		contenedorCodigo.add(pCodigo.getPanel(),BorderLayout.CENTER);
		contenedorCompilador.add(pCompilador.getPanel(),BorderLayout.CENTER);
		contenedorTraza.add(jspTraza);
		
		jspCompilador=new JScrollPane(contenedorCompilador);
		
		
		
		separadorCodigo=new JSplitPane(JSplitPane.VERTICAL_SPLIT,contenedorCodigo,jspCompilador);
		separadorCodigo.setResizeWeight(0.85);
		separadorCodigo.setDividerLocation(0.8);
		
		/*separadorIzqda=new JSplitPane(JSplitPane.VERTICAL_SPLIT,separadorCodigo,contenedorTraza);
		separadorIzqda.setDividerSize(8);
		separadorIzqda.setOneTouchExpandable(true);
        separadorIzqda.setResizeWeight(0.2);
		separadorIzqda.setDividerLocation(0.5);*/
		
		izqda.add(separadorCodigo,BorderLayout.CENTER);
		
		// Creamos el panel de la derecha (contiene el panel del árbol y el de la pila)
		JPanel dcha=new JPanel();
		dcha.setLayout(new BorderLayout());
		
		
		try {
			pPila=new PanelPila(null);
			pArbol=new PanelArbol(null);
			pCrono=new PanelCrono(null);
		} catch (OutOfMemoryError oome) {
			pArbol=null;
			throw oome;
		} catch (Exception e) {
			pArbol=null;
			e.printStackTrace();
			throw e;
		}
		jspPila = new JScrollPane(pPila);
		
		jspArbol = new JPanel();
		jspArbol.setLayout(new BorderLayout());
		jspArbol.add(pArbol,BorderLayout.CENTER);
		
		contenedorPila=new JPanel();
		contenedorArbol=new JPanel();
		contenedorPila.setLayout(new BorderLayout());
		contenedorPila.add(jspPila,BorderLayout.CENTER);
		contenedorArbol.setLayout(new BorderLayout());
		contenedorArbol.add(jspArbol,BorderLayout.CENTER);
		
		
		
		jspCrono = new JScrollPane(pCrono);
		contenedorCrono=new JPanel();
		contenedorCrono.setLayout(new BorderLayout());
		contenedorCrono.add(jspCrono,BorderLayout.CENTER);
		
		jspEstructura = new JScrollPane(pEstructura);
		contenedorEstructura=new JPanel();
		contenedorEstructura.setLayout(new BorderLayout());
		contenedorEstructura.add(jspEstructura,BorderLayout.CENTER);
		
		
		quitarBordesJSP();
		
		panel1=new JTabbedPane();
		panel2=new JTabbedPane();
		
		panel1.addChangeListener(this);
		panel2.addChangeListener(this);


		nombresVistas=recopilarNombresVistas();

		int tipoDisposicion;
		if (Conf.disposicionPaneles==Conf.PANEL_VERTICAL)
			tipoDisposicion=JSplitPane.HORIZONTAL_SPLIT;
		else
			tipoDisposicion=JSplitPane.VERTICAL_SPLIT;

		separadorVistas=new JSplitPane(tipoDisposicion,panel1,panel2);
		separadorVistas.setDividerSize(8);
		separadorVistas.setOneTouchExpandable(true);
		separadorVistas.setResizeWeight(0.5);
		separadorVistas.setDividerLocation(0.5);
		
		dcha.add(separadorVistas,BorderLayout.CENTER);
				
		
		// Creamos panel superior (nombre de método, botones, ...)
		pControl=new PanelControl(null, null, null, "", this);
		contenedorControl=new JPanel();
		contenedorControl.setLayout(new BorderLayout());
		contenedorControl.add(pControl,BorderLayout.CENTER);
		
		// Creamos panel de contención (contendrá las cuatro vistas)
		JPanel pContencion=new JPanel();
		
		separadorCentral=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,izqda,dcha);
		separadorCentral.setDividerSize(8);
		separadorCentral.setOneTouchExpandable(true);
        separadorCentral.setResizeWeight(0.3);
		separadorCentral.setDividerLocation(0.3);
		
		pContencion.setLayout(new BorderLayout());
		pContencion.add(separadorCentral,BorderLayout.CENTER);
		
		jspArbol.addComponentListener(pArbol.getNavegacionListener());

		// Creamos panel general
		panelGral = new JPanel();
		panelGral.setLayout(new BorderLayout());
		panelGral.add(contenedorControl,BorderLayout.NORTH);
		panelGral.add(pContencion,BorderLayout.CENTER);
		this.setLayout(new BorderLayout());
		this.add(panelGral,BorderLayout.CENTER);
	}
	
	
	
	
	public void distribuirPaneles(int disposicion)
	{
		separadorVistas.setOrientation( disposicion==Conf.PANEL_VERTICAL ? JSplitPane.HORIZONTAL_SPLIT : JSplitPane.VERTICAL_SPLIT );
	}
	
	public void idioma()
	{
		pControl.idioma();
		
		if (this.estaOcupado())
		{
			try {
				pTraza = new PanelTraza();
			} catch (Exception e) {
			}
			jspTraza.removeAll();
			jspTraza=new JScrollPane(pTraza);
			pTraza.visualizar();
			contenedorTraza.removeAll();
			contenedorTraza.add(jspTraza);
			contenedorTraza.updateUI();
			this.updateUI();
		}
		
		String []nombres=getNombreVistasDisponibles();
		String [][]idiomas=Texto.idiomasDisponibles();
		
		for (int i=0; i<nombres.length; i++)	// Para cada nombre contenido en "nombres" buscamos su traducción
		{
			for (int j=0; j<idiomas.length; j++)	// Buscamos en todos los idiomas, no sabemos en qué idioma está el nombre que tenemos
			{
				for (int k=0; k<Vista.codigos.length; k++)	// Buscamos entre los nombres que tenemos guardados en XML basándonos en códigos de Vista.codigos
				{
					if (nombres[i].equals(Texto.get(Vista.codigos[k],idiomas[j][1])))
					{
						// Ahora buscamos en panel1 y panel2 para saber dónde tenemos que hacer reemplazo
						for (int x=0; x<panel1.getTabCount(); x++)	
							if (panel1.getTitleAt(x).equals(nombres[i]))
								panel1.setTitleAt(x, Texto.get(Vista.codigos[k],Conf.idioma));
											
						for (int x=0; x<panel2.getTabCount(); x++)	
							if (panel2.getTitleAt(x).equals(nombres[i]))
								panel2.setTitleAt(x, Texto.get(Vista.codigos[k],Conf.idioma));
					}
				}
			}
		}
		quitarBordesJSP();
		nombresVistas=recopilarNombresVistas();
	}
	
	
	public String[] recopilarNombresVistas()
	{
		return Texto.get(Vista.codigos, Conf.idioma);
	}
	
	
	
	
	public void abrirPanelCodigo(String nombreArchivo, String nombreMetodo, boolean editable, boolean cargarFichero)
	{
		contenedorCodigo.removeAll();
		
		//pCodigo = new PanelCodigo(nombreArchivo,nombreMetodo);
		pCodigo.abrir(nombreArchivo, nombreMetodo,editable, cargarFichero , false);
		JScrollPane jsp=new JScrollPane(pCodigo.getPanel());
		quitarBordesJSP();
		
		contenedorCodigo.add(jsp);
		contenedorCodigo.updateUI();
		

		
		
	}
	
	public void cerrarPanelCodigo()
	{
		contenedorCodigo.removeAll();
		
		pCodigo = new PanelCodigo(null,null);
		
		quitarBordesJSP();
		contenedorCodigo.updateUI();

		nyp=null;
	}
	
	public void abrirVistas()
	{
		abriendoVistas=true;
		ubicarVistas();
		
		
		nyp=null;
		this.mostrarNombreMetodos = Ventana.thisventana.traza.getNumMetodos()!= 1;
		
		if (mostrarNombreMetodos)
		{
			nyp=new NombresYPrefijos();
			nombresMetodos=Ventana.thisventana.trazaCompleta.getNombresMetodos();
			String prefijos[]=ServiciosString.obtenerPrefijos(nombresMetodos);
			for (int i=0; i<nombresMetodos.length; i++)
				nyp.add(nombresMetodos[i],prefijos[i]);
		}

		try {
			pArbol = new PanelArbol(nyp);
			pPila = new PanelPila(nyp);
			
			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
			{
				Ventana.thisventana.habilitarOpcionesDYV(true);
				pCrono = new PanelCrono(nyp);
				pEstructura = new PanelEstructura(nyp);
			}
			else
			{
				pTraza = new PanelTraza();
			}
			
			pControl.setValores(Ventana.thisventana.traza.getTitulo(), this);
			jspArbol.addComponentListener(pArbol.getNavegacionListener());
			
			new Thread()
			{		
				public synchronized void run()
				{
					try {wait(50);} catch (java.lang.InterruptedException ie) {}
					pArbol.getNavegacionListener().ejecucion(1);
					
					pArbol.updateUI();

				}
			}.start();
			if(!Conf.arranqueEstadoInicial)
			{
				pControl.hacerFinal();
			}
			else {
				//Actualizamos la vista para que se posicione bien sobre el primer nodo creado
				this.actualizar();
			}

			this.ocupado=true;
		} catch (Exception e) {
			try {
				e.printStackTrace();
				System.out.println("\n-Ha saltado una excepcion(PanelAlgoritmo)-\n");
				pArbol = new PanelArbol(null);
				pPila = new PanelPila(null);
				pTraza = new PanelTraza();
				pCrono = new PanelCrono(null);
				pEstructura = new PanelEstructura(null);
				pControl=new PanelControl(null, null, null, "", this);
			} catch (Exception e2) {
			}
		}
		
		contenedorArbol.removeAll();
		contenedorPila.removeAll();
		contenedorTraza.removeAll();
		contenedorCrono.removeAll();
		contenedorEstructura.removeAll();
		
		jspArbol.removeAll();
		jspPila.removeAll();
		jspTraza.removeAll();
		jspCrono.removeAll();
		jspEstructura.removeAll();
		
		jspArbol.add(pArbol);
		jspPila=new JScrollPane(pPila);
		jspTraza=new JScrollPane(pTraza);
		jspCrono=new JScrollPane(pCrono);
		jspEstructura=new JScrollPane(pEstructura);
		
		contenedorArbol.add(jspArbol);
		contenedorPila.add(jspPila);
		contenedorTraza.add(jspTraza);
		contenedorCrono.add(jspCrono);
		contenedorEstructura.add(jspEstructura);
		
		quitarBordesJSP();
		
		contenedorArbol.updateUI();
		contenedorPila.updateUI();
		contenedorTraza.updateUI();
		contenedorCrono.updateUI();
		contenedorEstructura.updateUI();
		contenedorControl.updateUI();
		abriendoVistas=false;
	}
	
	
	public void abrirVistas(String ficheroGIF)
	{
		abriendoVistas=true;

		ubicarVistas();

		try {
			pArbol = new PanelArbol(ficheroGIF,new ImageIcon(ficheroGIF));	
			pPila = new PanelPila(null);
			pTraza = new PanelTraza();
			this.ocupado=true;
			pControl.setValores(ficheroGIF.substring(ficheroGIF.lastIndexOf("\\")+1,ficheroGIF.lastIndexOf(".")), this);
			Ventana.thisventana.setTitulo(ficheroGIF.substring(ficheroGIF.lastIndexOf("\\")+1,ficheroGIF.length()));
			
		} catch (Exception e) {
			try {
				e.printStackTrace();
				System.out.println("\n-Ha saltado una excepcion-\n");
				pArbol = new PanelArbol(null);
				pPila = new PanelPila(null);
				pTraza = new PanelTraza();
				pControl=new PanelControl(null, null, null, "", this);
				this.ocupado=false;
			} catch (Exception e2) {
			}
		}
		
		contenedorArbol.removeAll();
		contenedorPila.removeAll();
		contenedorTraza.removeAll();
		
		jspArbol.removeAll();
		jspPila.removeAll();
		jspTraza.removeAll();
		
		jspArbol.add(pArbol);
		jspPila=new JScrollPane(pPila);
		jspTraza=new JScrollPane(pTraza);
		
		contenedorArbol.add(jspArbol);
		contenedorPila.add(jspPila);
		contenedorTraza.add(jspTraza);
		
		quitarBordesJSP();
		
		contenedorArbol.updateUI();
		contenedorPila.updateUI();
		contenedorTraza.updateUI();
		contenedorControl.updateUI();
		
		abriendoVistas=false;
	}
	
	public void cerrarVistas()
	{
		try {
			ocupado=false;
			Ventana.thisventana.traza=null;
			Ventana.thisventana.trazaCompleta=null;
			pArbol = new PanelArbol(null);
			pPila = new PanelPila(null);
			pTraza = new PanelTraza();
			pCrono = new PanelCrono(null);
			pControl.setValores("", this);
			
		} catch (Exception e) {

		}
		
		contenedorArbol.removeAll();
		contenedorPila.removeAll();
		contenedorTraza.removeAll();
		contenedorCrono.removeAll();
		contenedorEstructura.removeAll();
		
		jspArbol.removeAll();
		jspPila.removeAll();
		jspTraza.removeAll();
		jspCrono.removeAll();
		jspEstructura.removeAll();
		
		jspArbol.add(pArbol);
		jspPila=new JScrollPane(pPila);
		jspTraza=new JScrollPane(pTraza);
		jspCrono=new JScrollPane(pCrono);
		jspEstructura=new JScrollPane(pEstructura);
		
		contenedorArbol.add(jspArbol);
		contenedorPila.add(jspPila);
		contenedorTraza.add(jspTraza);
		contenedorCrono.add(jspCrono);
		contenedorEstructura.add(jspEstructura);
		
		quitarBordesJSP();
		
		contenedorArbol.updateUI();
		contenedorPila.updateUI();
		contenedorTraza.updateUI();
		contenedorCrono.updateUI();
		contenedorEstructura.updateUI();
		
		contenedorControl.updateUI();
		
		panel1.removeAll();
		panel2.removeAll();
		
		nyp=null;
	}
	
	
	
	
	public void ubicarVistas()
	{
		// Vista de árbol
		if (Conf.getVista(Vista.codigos[0]).getPanel()==1)
			panel1.add(Texto.get(Vista.codigos[0],Conf.idioma),contenedorArbol);
		else
			panel2.add(Texto.get(Vista.codigos[0],Conf.idioma),contenedorArbol);
		
		if (Ventana.thisventana.getTraza()!=null)	// Será null si estamos cargando GIF
		{
			// Vista de pila
			if (Conf.getVista(Vista.codigos[1]).getPanel()==1)
				panel1.add(Texto.get(Vista.codigos[1],Conf.idioma),contenedorPila);
			else
				panel2.add(Texto.get(Vista.codigos[1],Conf.idioma),contenedorPila);
			
			
			//if (VentanaVisualizador.thisventana.getTraza().getTecnica()==MetodoAlgoritmo.TECNICA_DYV)
			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
			{
				// Vista cronológica
				if (Conf.getVista(Vista.codigos[2]).getPanel()==1)
					panel1.add(Texto.get(Vista.codigos[2],Conf.idioma),contenedorCrono);
				else
					panel2.add(Texto.get(Vista.codigos[2],Conf.idioma),contenedorCrono);
				
				// Vista de estructura
				if (Conf.getVista(Vista.codigos[3]).getPanel()==1)
					panel1.add(Texto.get(Vista.codigos[3],Conf.idioma),contenedorEstructura);
				else
					panel2.add(Texto.get(Vista.codigos[3],Conf.idioma),contenedorEstructura);
			}
			else
			{
				// Vista de traza
				if (Conf.getVista(Vista.codigos[2]).getPanel()==1)
					panel1.add(Texto.get(Vista.codigos[2],Conf.idioma),contenedorTraza);
				else
					panel2.add(Texto.get(Vista.codigos[2],Conf.idioma),contenedorTraza);
			}

			// Si las vistas de recursividad fueron colocadas todas en un panel
			if (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
			{
				if (	Conf.getVista(Vista.codigos[0]).getPanel()==1 &&
						Conf.getVista(Vista.codigos[1]).getPanel()==1 &&
						Conf.getVista(Vista.codigos[2]).getPanel()==1 )
				{
					panel2.add(Texto.get(Vista.codigos[0],Conf.idioma),contenedorArbol);
					
					
					OpcionVistas ov=(OpcionVistas)gOpciones.getOpcion("OpcionVistas",false);
					ov.getVista(Vista.codigos[0]).setPanel(2);
					gOpciones.setOpcion(ov,2);
					Conf.setConfiguracionVistas();
					
				}
				else if (	Conf.getVista(Vista.codigos[0]).getPanel()==2 &&
						Conf.getVista(Vista.codigos[1]).getPanel()==2 &&
						Conf.getVista(Vista.codigos[2]).getPanel()==2 )
				{
					panel1.add(Texto.get(Vista.codigos[0],Conf.idioma),contenedorArbol);

					OpcionVistas ov=(OpcionVistas)gOpciones.getOpcion("OpcionVistas",false);
					ov.getVista(Vista.codigos[0]).setPanel(1);
					gOpciones.setOpcion(ov,2);
					Conf.setConfiguracionVistas();
				}
			}
		}
	}
	
	
	
	
	
	
	
	/**
		Comprueba si está cerrado el panel (es decir, si no está visualizando nada en su interior)
		
		@return true si está cerrado el panel
	*/
	public boolean estaOcupado()
	{
		return this.ocupado;
	}

	
	


	protected void deshabilitarControles()
	{
		pControl.deshabilitarControles();
	}
	
	protected void habilitarControles()
	{
		pControl.habilitarControles();
	}
	
	protected void setValoresPanelControl(String tituloPanel) {
		pControl.setValores(tituloPanel, this);
	}
	
	
	
	
	public void actualizar()
	{
		if (Ventana.thisventana.getTraza()!=null)
		{
		
			boolean[] hemosActualizado=new boolean[Vista.codigos.length];
			for (int i=0; i<hemosActualizado.length; i++)
				hemosActualizado[i]=false;
			
	
			
			if (	panel1.indexOfTab(nombresVistas[1])==panel1.getSelectedIndex() ||
					panel2.indexOfTab(nombresVistas[1])==panel2.getSelectedIndex() )
			{
				new Thread()
				{		
					public synchronized void run()
					{
						
						try {wait(240);} catch (java.lang.InterruptedException ie) {}
						pPila.visualizar();
					}
				}.start();
				hemosActualizado[1]=true;
			}
			
			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
			{
				if (	panel1.indexOfTab(nombresVistas[2])==panel1.getSelectedIndex() ||
						panel2.indexOfTab(nombresVistas[2])==panel2.getSelectedIndex() )
				{
					new Thread()
					{		
						public synchronized void run()
						{
							
							try {wait(20);} catch (java.lang.InterruptedException ie) {}
							pCrono.visualizar();
						}
					}.start();
					hemosActualizado[2]=true;
				}
			}
			
			
			if (	panel1.indexOfTab(nombresVistas[0])==panel1.getSelectedIndex() ||
					panel2.indexOfTab(nombresVistas[0])==panel2.getSelectedIndex() )
			{
				new Thread()
				{		
					public synchronized void run()
					{
						
						try {wait(100);} catch (java.lang.InterruptedException ie) {}
						pArbol.visualizar(false,true,false);
					}
				}.start();
				hemosActualizado[0]=true;
			}
			
			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
			{
				if (	panel1.indexOfTab(nombresVistas[3])==panel1.getSelectedIndex() ||
						panel2.indexOfTab(nombresVistas[3])==panel2.getSelectedIndex() )
				{
					new Thread()
					{		
						public synchronized void run()
						{				
							try {wait(220);} catch (java.lang.InterruptedException ie) {}
							pEstructura.visualizar();
						}
					}.start();
					hemosActualizado[3]=true;
				}
			}
			if (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
			{
				if (	panel1.indexOfTab(nombresVistas[2])==panel1.getSelectedIndex() ||
						panel2.indexOfTab(nombresVistas[2])==panel2.getSelectedIndex() )
				{
					pTraza.visualizar();
					hemosActualizado[2]=true;
				}
			}
		}
	}
	
	
	
	
	public void actualizarPestanas()
	{
	
		for (int i=0; i<vistasActualizadas.length; i++)
			vistasActualizadas[i]=false;
		
		
		if (	panel1.indexOfTab(nombresVistas[1])==panel1.getSelectedIndex() ||
				panel2.indexOfTab(nombresVistas[1])==panel2.getSelectedIndex() )
		{
			new Thread()
			{		
				public synchronized void run()
				{
					
					try {wait(240);} catch (java.lang.InterruptedException ie) {}
					pPila.visualizar();
				}
			}.start();
			vistasActualizadas[1]=true;
		}
		
		if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
		{
			if (	panel1.indexOfTab(nombresVistas[2])==panel1.getSelectedIndex() ||
					panel2.indexOfTab(nombresVistas[2])==panel2.getSelectedIndex() )
			{
				new Thread()
				{		
					public synchronized void run()
					{
						
						try {wait(20);} catch (java.lang.InterruptedException ie) {}
						pCrono.visualizar();
					}
				}.start();
				vistasActualizadas[3]=true;
			}
		}
		
		
		if (	panel1.indexOfTab(nombresVistas[0])==panel1.getSelectedIndex() ||
				panel2.indexOfTab(nombresVistas[0])==panel2.getSelectedIndex() )
		{
			new Thread()
			{		
				public synchronized void run()
				{
					
					try {wait(100);} catch (java.lang.InterruptedException ie) {}
					pArbol.visualizar(false,true,false);
				}
			}.start();
			vistasActualizadas[0]=true;
		}
		
		if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
		{
			if (	panel1.indexOfTab(nombresVistas[3])==panel1.getSelectedIndex() ||
					panel2.indexOfTab(nombresVistas[3])==panel2.getSelectedIndex() )
			{
				new Thread()
				{		
					public synchronized void run()
					{				
						try {wait(220);} catch (java.lang.InterruptedException ie) {}
						pEstructura.visualizar();
					}
				}.start();
				vistasActualizadas[4]=true;
			}
		}
		if (!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
		{
			if (	panel1.indexOfTab(nombresVistas[2])==panel1.getSelectedIndex() ||
					panel2.indexOfTab(nombresVistas[2])==panel2.getSelectedIndex() )
			{
				pTraza.visualizar();
				vistasActualizadas[2]=true;
			}
		}
	}
	
	/**
		Redibuja el contenido del panel del algoritmo, atendiendo a cambios en la configuración
	*/
	public void refrescarFormato(boolean recargarCodigo)
	{
		if (pArbol!=null)
		{
			pArbol.visualizar(true,true,false);
			pPila.visualizar();
			
			pCodigo.visualizar(recargarCodigo);
			
			if (Ventana.thisventana.getTraza()!=null && 
						Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
			{
				pCrono.visualizar();
				pEstructura.visualizar();	
			}
			else
				pTraza.visualizar();
			
			if (Ventana.thisventana.traza!=null)
				pControl.visualizar();
			new Thread()
			{		
				public synchronized void run()
				{
					try {wait(250);} catch (java.lang.InterruptedException ie) {}
					if (pArbol.getNavegacionListener()!=null)
						pArbol.getNavegacionListener().ejecucion(1);
				}
			}.start();
		}
		this.updateUI();
	}
	
	/**   
		Redibuja el contenido del panel del algoritmo, atendiendo a cambios en la configuración
	*/
	public void refrescarOpciones()
	{
		if (pArbol!=null)
		{
			pArbol.visualizar(false,true,false);
			pPila.visualizar();
			pTraza.visualizar();
			pCodigo.visualizar(false);
			if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
			{
				pCrono.visualizar();
				pEstructura.visualizar();
			}
		}
		this.updateUI();
	}
	
	
	public void refrescarZoomArbol(int valor)
	{
		if (pArbol!=null)
		{
			pArbol.refrescarZoom(valor);
			pArbol.visualizar(false,true,false);
		}
		this.updateUI();
	}	
	
	public void refrescarZoomPila(int valor)
	{
		if (pPila!=null)
		{
			pPila.refrescarZoom(valor);
			pPila.visualizar();
		}
		this.updateUI();
	}	
	
	public void refrescarZoom(int vista,int valor)
	{
		switch(vista)
		{
			case 1:
				if (pPila!=null)
				{
					//System.out.println("PanelAlgoritmo.refrescarZoom, i="+vista);
					pPila.refrescarZoom(valor);
					pPila.visualizar();
					pPila.updateUI();
				}
				break;
			case 0:
				if (pArbol!=null)
				{
					//System.out.println("PanelAlgoritmo.refrescarZoom, i="+vista);
					pArbol.refrescarZoom(valor);
					pArbol.visualizar(false,true,false);
					pArbol.updateUI();
				}
				break;
			case 3:
				if (pCrono!=null)
				{
					//System.out.println("PanelAlgoritmo.refrescarZoom, i="+vista);
					pCrono.refrescarZoom(valor);
					pCrono.visualizar();
					pCrono.updateUI();
				}
				break;	
			case 4:
				if (pEstructura!=null)
				{
					//System.out.println("PanelAlgoritmo.refrescarZoom, i="+vista);
					pEstructura.refrescarZoom(valor);
					pEstructura.visualizar();
					pEstructura.updateUI();
				}
				break;		
		}

		//
	}	
	
	
	public String getTituloPanel()
	{
		return this.tituloPanel;
	}

	
	public int[] dimPanelYGrafo()
	{
		return pArbol.dimPanelYGrafo();
	}
	
	public int[] dimPanelYPila()
	{
		return pPila.dimPanelYPila();
	}
	
	public int[] dimPanelYGrafoCrono()
	{
		int[] val1= pCrono.dimPanelYGrafo();
		int[] val2= dimPanelCrono();
		
		int[]val=new int[4];
		val[0]=val2[0];
		val[1]=val2[1];
		val[2]=val1[2];
		val[3]=val1[3];
		
		return val;
	}	
	
	public int[] dimPanelYGrafoEstructura()
	{
		int[] val1= pEstructura.dimPanelYGrafo();
		int[] val2= dimPanelEstructura();
		
		int[]val=new int[4];
		val[0]=val2[0];
		val[1]=val2[1];
		val[2]=val1[2];
		val[3]=val1[3];
		
		return val;
	}
	
	

	public String getNombre()
	{
		return tituloPanel;
	}
	
	public String getIdTraza()
	{
		return idTraza;
	}
	
	public int[] getZooms()
	{
		int zooms[]=new int[4];
		
		zooms[0]=pArbol.getZoom();
		zooms[1]=pPila.getZoom();
		if (Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas()))
		{
			zooms[2]=pCrono.getZoom();
			zooms[3]=pEstructura.getZoom();
		}
		return zooms;
	}
	
	

	
	
	public static int[]dimPanelEstructura()
	{
		int []dim=new int[2];
		dim[0]=jspEstructura.getWidth();
		dim[1]=jspEstructura.getHeight();

		return dim;
	}
	
	public static int[]dimPanelCrono()
	{
		int []dim=new int[2];
		dim[0]=jspCrono.getWidth();
		dim[1]=jspCrono.getHeight();

		return dim;
	}
	
	public static int[]dimPanelTraza()
	{
		int []dim=new int[2];
		dim[0]=jspTraza.getWidth();
		dim[1]=jspTraza.getHeight();

		return dim;
	}
	
	
	public static int[]dimPanelPila()
	{
		int []dim=new int[2];
		dim[0]=jspPila.getWidth();
		dim[1]=jspPila.getHeight();

		return dim;
	}
	
	public int[]dimPanelPrincipal()
	{
		int []dim=new int[2];
		dim[0]=jspArbol.getWidth();
		dim[1]=pArbol.alturaJSPArbol();
		
		return dim;
	}
	
	public int[]dimPanelArbol()
	{
		int []dim=new int[2];
		dim[0]=jspArbol.getWidth();
		dim[1]=jspArbol.getHeight();
		
		return dim;
	}
	
	public int[]dimGrafoPila()
	{
		return pPila.dimGrafo();
	}
	
	public int[]dimGrafoPrincipal()
	{
		return pArbol.dimGrafo();
	}
	

	
	public int[]dimGrafoVisiblePrincipal()
	{
		return pArbol.dimGrafoVisible();
	}
	
	public int[] dimGrafoVisibleCrono()
	{
		return pCrono.dimGrafoVisible();
	}
	
	public int[] dimGrafoVisibleEstructura()
	{
		return pEstructura.dimGrafoVisible();
	}
	
	
	
	public int[]posicPanelPrincipal()
	{
		int []posOrigen=new int[2];
		posOrigen[0]=(int)jspArbol.getLocationOnScreen().getX();
		posOrigen[1]=(int)jspArbol.getLocationOnScreen().getY();
		
		return posOrigen;
	}
	
	public JComponent getPanelArbol()
	{
		return this.jspArbol;
	}
	
	public static int[] dimpArbol()
	{
		int d[]=new int[2];
		d[0]=pArbol.getWidth();
		d[1]=pArbol.getHeight();
		return d;
	}
	
	public static int[] dimpPila()
	{
		int d[]=new int[2];
		d[0]=pPila.getWidth();
		d[1]=pPila.getHeight();
		return d;
	}
	
	public static int[] dimpTraza()
	{
		int d[]=new int[2];
		d[0]=pTraza.getWidth();
		d[1]=pTraza.getHeight();
		return d;
	}
	
	public static int[] dimpCodigo()
	{
		int d[]=new int[2];
		d[0]=pCodigo.getPanel().getWidth();
		d[1]=pCodigo.getPanel().getHeight();
		return d;
	}
	
	
	
	public void setVistaActiva(String nombre)
	{
		//System.out.println("panelalgoritmo.setVistaActiva");
		for (int i=0; i<panel1.getTabCount(); i++)
			if (panel1.getTitleAt(i).equals(nombre))
			{
				//System.out.println("    (1)setVistaActiva "+i);
				panel1.setSelectedIndex(i);
			}
		
		for (int i=0; i<panel2.getTabCount(); i++)
			if (panel2.getTitleAt(i).equals(nombre))
			{
				//System.out.println("    (2)setVistaActiva "+i);
				panel2.setSelectedIndex(i);
			}
		
		
	}
	
	
	public JComponent getPanelPorNombre(String nombre)
	{

		
		
		if (nombre.equals(Texto.get(Vista.codigos[0],Conf.idioma)))
			return pArbol;
		else if (nombre.equals(Texto.get(Vista.codigos[1],Conf.idioma)))
			return pPila;
		else if ((nombre.equals(Texto.get(Vista.codigos[2],Conf.idioma)))&&(!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas())))
			return pTraza;
		else if ((nombre.equals(Texto.get(Vista.codigos[2],Conf.idioma)))&&(Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas())))
			return pCrono;
		else if (nombre.equals(Texto.get(Vista.codigos[3],Conf.idioma)))
			return pEstructura;
		else
			return null;
		
		
	}
	
	public Object getGrafoPorNombre(String nombre)
	{
		if (nombre.equals(Texto.get(Vista.codigos[0],Conf.idioma)))
			return pArbol.getGrafo();
		else if (nombre.equals(Texto.get(Vista.codigos[1],Conf.idioma)))
			return pPila.getGrafo();
		
		else if ((nombre.equals(Texto.get(Vista.codigos[2],Conf.idioma)))&&(Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas())))
			return pCrono.getGrafo();
		else if (nombre.equals(Texto.get(Vista.codigos[3],Conf.idioma)))
			return pEstructura.getGrafo();
		else
			return null;
	}
	
	public Object getGrafoPorNumero(int numero)
	{
		switch(numero)
		{
		case 0:
			return pArbol.getGrafo();
		case 1:
			return pPila.getGrafo();
		//case 2:
		//	return null;
		case 3:
			return pCrono.getGrafo();
		case 4:
			return pEstructura.getGrafo();
		default:
			return null;
		}
		
	}
	
	
	
	public int[] getDimPanelPorNombre(String nombre)
	{
		int[] dim=new int[2];
		
		
		if (nombre.equals(Texto.get(Vista.codigos[0],Conf.idioma)))
		{
			dim=dimPanelArbol();
		}
		else if (nombre.equals(Texto.get(Vista.codigos[1],Conf.idioma)))
		{
			dim=dimPanelPila();
		}
		else if ((nombre.equals(Texto.get(Vista.codigos[2],Conf.idioma)))&&(!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas())))
		{
			dim=dimPanelTraza();
		}
		else if ((nombre.equals(Texto.get(Vista.codigos[2],Conf.idioma)))&&(Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas())))
		{
			dim=dimPanelCrono();
		}
		else if (nombre.equals(Texto.get(Vista.codigos[3],Conf.idioma)))
		{
			dim=dimPanelEstructura();
		}

		return dim;
		
	}
	
	public int[] getPosicPanelPorNombre(String nombre)
	{
		int[] pos=new int[2];
		
		
		if (nombre.equals(Texto.get(Vista.codigos[0],Conf.idioma)))
		{
			pos[0]=(int)jspArbol.getLocationOnScreen().getX();
			pos[1]=(int)jspArbol.getLocationOnScreen().getY();
		}
		else if (nombre.equals(Texto.get(Vista.codigos[1],Conf.idioma)))
		{
			pos[0]=(int)jspPila.getLocationOnScreen().getX();
			pos[1]=(int)jspPila.getLocationOnScreen().getY();
		}
		else if ((nombre.equals(Texto.get(Vista.codigos[2],Conf.idioma)))&&(!Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas())))
		{
			pos[0]=(int)jspTraza.getLocationOnScreen().getX();
			pos[1]=(int)jspTraza.getLocationOnScreen().getY();
		}
		else if ((nombre.equals(Texto.get(Vista.codigos[2],Conf.idioma)))&&(Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.getTraza().getTecnicas())))
		{
			pos[0]=(int)jspCrono.getLocationOnScreen().getX();
			pos[1]=(int)jspCrono.getLocationOnScreen().getY();
		}
		else if (nombre.equals(Texto.get(Vista.codigos[3],Conf.idioma)))
		{
			pos[0]=(int)jspEstructura.getLocationOnScreen().getX();
			pos[1]=(int)jspEstructura.getLocationOnScreen().getY();
		}

		return pos;
		
	}
	
	
	public void setTextoCompilador(String texto)
	{
		pCompilador.setTextoCompilador(texto);
	}
	
	
	public void guardarClase()
	{
		pCodigo.guardar();
	}
	
	protected PanelCodigo getPanelCodigo()
	{
		return pCodigo;
	}
	

	public void cerrar()
	{
		cerrarVistas();
		/*if (Ventana.thisventana.getClase()!=null)
			this.abrirPanelCodigo(Ventana.thisventana.getClase().getPath(),"¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿|¿||?|????????????????????",pCodigo.getEditable());
		else
			this.cerrarPanelCodigo();*/
		if (Ventana.thisventana.getClase()==null)
			this.cerrarPanelCodigo();
	}

	
	public boolean getEditable()
	{
		return pCodigo.getEditable();
	}
	
	
	private void quitarBordesJSP()
	{
		//jspCodigo.setBorder(new EmptyBorder(0,0,0,0));
		jspCompilador.setBorder(new EmptyBorder(0,0,0,0));
		jspArbol.setBorder(new EmptyBorder(0,0,0,0));
		jspTraza.setBorder(new EmptyBorder(0,0,0,0));
		jspPila.setBorder(new EmptyBorder(0,0,0,0));
		jspCrono.setBorder(new EmptyBorder(0,0,0,0));
		jspEstructura.setBorder(new EmptyBorder(0,0,0,0));
	}
	
	
	// Devuelve el nombre de todas las vistas que están disponibles en la visualización abierta
	public String[] getNombreVistasDisponibles()
	{
		String[] vistas=new String[panel1.getTabCount()+panel2.getTabCount()];
		
		for (int i=0; i<panel1.getTabCount(); i++)
			vistas[i]=panel1.getTitleAt(i);
		
		for (int i=0; i<panel2.getTabCount(); i++)
			vistas[i+panel1.getTabCount()]=panel2.getTitleAt(i);
		
		return vistas;
	}
	
	// Devuelve el nombre de las vistas que están visibles en ese instante (pestañas seleccionadas) en la visualización abierta
	public String[] getNombreVistasVisibles()
	{
		String[] vistas=new String[2];
		
		for (int i=0; i<panel1.getTabCount(); i++)
			if (panel1.getSelectedIndex()==i)
				vistas[0]=panel1.getTitleAt(i);
		
		for (int i=0; i<panel2.getTabCount(); i++)
			if (panel2.getSelectedIndex()==i)
				vistas[1]=panel2.getTitleAt(i);
		
		return vistas;
	}
	
	public int[] getCodigoVistasVisibles()
	{
		String[] nombresVistas=getNombreVistasVisibles();
		
		String[] codigoVistas=new String[nombresVistas.length];
		
		int[] vistas=new int[nombresVistas.length];
		
		for (int i=0; i<codigoVistas.length; i++)
		{
			codigoVistas[i]=Texto.getCodigo(nombresVistas[i], Conf.idioma);
			vistas[i]=Vista.getPosic(codigoVistas[i]);
			
			// Corrección por vista de traza, que no aparece en Cuadro Zoom
			/*if (vistas[i]>2)
				vistas[i]--;*/
		}
		
		return vistas;
	}
	
	
	

	public void stateChanged(ChangeEvent arg0)
	{
		if (!abriendoVistas)
			actualizar();
	}
}


