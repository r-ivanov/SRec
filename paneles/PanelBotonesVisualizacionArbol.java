/**
	Panel que contiene los botones que manejan la visualizaci�n del algoritmo
	
	@author Antonio P�rez Carrasco
	@version 2006-2007
*/
package paneles;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import botones.*;
import conf.*;
import cuadros.*;
import grafica.*;
import utilidades.*;
import ventanas.*;

public class PanelBotonesVisualizacionArbol extends JPanel
						implements MouseListener, ChangeListener
{
	static final long serialVersionUID=12;
	

	// Botones manuales de retroceso
	private BotonImagen retrocM = new BotonImagen("imagenes/ba_salto_at_v.gif","imagenes/ba_salto_at_n.gif","imagenes/ba_salto_at_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);

	private BotonImagen retrocF = new BotonImagen("imagenes/ba_extr_at_v.gif","imagenes/ba_extr_at_n.gif","imagenes/ba_extr_at_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);

	private BotonImagen retroc = new BotonImagen("imagenes/ba_paso_at_v.gif","imagenes/ba_paso_at_n.gif","imagenes/ba_paso_at_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);

	// Botones de animacion
	private BotonImagen aat = new BotonImagen("imagenes/ba_anim_at_v.gif","imagenes/ba_anim_at_n.gif","imagenes/ba_anim_at_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
									
	private BotonImagen parar = new BotonImagen("imagenes/ba_pausa_v.gif","imagenes/ba_pausa_n.gif","imagenes/ba_pausa_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
										
	private BotonImagen aad = new BotonImagen("imagenes/ba_anim_ad_v.gif","imagenes/ba_anim_ad_n.gif","imagenes/ba_anim_ad_r.gif",  
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
	
	// Botones manuales de avance
	private BotonImagen avance = new BotonImagen("imagenes/ba_paso_ad_v.gif","imagenes/ba_paso_ad_n.gif","imagenes/ba_paso_ad_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);

	private BotonImagen avanceF = new BotonImagen("imagenes/ba_extr_ad_v.gif","imagenes/ba_extr_ad_n.gif","imagenes/ba_extr_ad_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
	
	private BotonImagen avanceM = new BotonImagen("imagenes/ba_salto_ad_v.gif","imagenes/ba_salto_ad_n.gif","imagenes/ba_salto_ad_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);

	// Bot�n de cierre
	private BotonImagen cerrar = new BotonImagen("imagenes/ba_cerrar_v.gif","imagenes/ba_cerrar_n.gif","imagenes/ba_cerrar_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
	
	
	
	
	private JTextField campoSeg = new JTextField(3);
	private JSlider barraSeg = new JSlider(0,5,1);
	

	boolean habilitado= true;	// para cuando se hacen capturas animadas
	
	
	private PanelAlgoritmo pAlgoritmo;
	
	int hayAnimacion=0;	// 0=no hay animaci�n  1=hacia adelante  2=hacia atr�s
	
	float seg;
	boolean adelante;
	boolean depurar=true;
	
	/**
		Constructor: crea un nuevo panel de botones de visualizaci�n
		
		@param pt Panel de traza que contiene la traza del algoritmo
		@param pa Panel al que pertenece
	*/
	public PanelBotonesVisualizacionArbol(PanelAlgoritmo pAlgoritmo)
	{
		this.pAlgoritmo=pAlgoritmo;
		
		this.seg=(float)1.0;
		this.adelante=true;
	
		campoSeg.setHorizontalAlignment(JTextField.RIGHT);
		
		if (Ventana.thisventana.traza!=null)
		{
			if (Ventana.thisventana.traza.vacioVisible() || Ventana.thisventana.traza.raizInicio())
			{
				retrocF.deshabilitar();
				retroc.deshabilitar();
				aat.deshabilitar();
			}
			if (Ventana.thisventana.traza.enteroVisible())
			{
				avance.deshabilitar();
				avanceF.deshabilitar();
				aad.deshabilitar();
			}
			if (Ventana.thisventana.traza.nodoActualCompleto())
				avanceM.deshabilitar();
			else
				retrocM.deshabilitar();
			
			parar.deshabilitar();
		}
		else
		{
			retrocM.deshabilitar();
			retrocF.deshabilitar();
			retroc.deshabilitar();
			aat.deshabilitar();
			parar.deshabilitar();
			aad.deshabilitar();
			avance.deshabilitar();
			avanceF.deshabilitar();
			avanceM.deshabilitar();
			cerrar.deshabilitar();
		}

		
		retrocM.addMouseListener(this);
		retrocF.addMouseListener(this);
		retroc.addMouseListener(this);
		aat.addMouseListener(this);
		parar.addMouseListener(this);
		aad.addMouseListener(this);
		avance.addMouseListener(this);
		avanceF.addMouseListener(this);
		avanceM.addMouseListener(this);
		cerrar.addMouseListener(this);
		
		retrocM.setBorder(new EmptyBorder(1,1,1,1));
		retrocF.setBorder(new EmptyBorder(1,1,1,1));
		retroc.setBorder(new EmptyBorder(1,1,1,1));
		aat.setBorder(new EmptyBorder(1,1,1,1));
		parar.setBorder(new EmptyBorder(1,1,1,1));
		aad.setBorder(new EmptyBorder(1,1,1,1));
		avance.setBorder(new EmptyBorder(1,1,1,1));
		avanceF.setBorder(new EmptyBorder(1,1,1,1));
		avanceM.setBorder(new EmptyBorder(1,1,1,1));
		cerrar.setBorder(new EmptyBorder(1,1,1,1));
		
		barraSeg.addChangeListener(this);
		barraSeg.setPreferredSize(new Dimension(60,20));
		
		setToolTipText();
		
		this.add(barraSeg);
		this.add(campoSeg);
		
		
		JToolBar jtb=new JToolBar();
		jtb.setFloatable(false);
		
		jtb.add(retrocM);
		jtb.add(retrocF);
		jtb.add(retroc);
		jtb.add(aat);
		jtb.add(parar);
		jtb.add(aad);
		jtb.add(avance);
		jtb.add(avanceF);
		jtb.add(avanceM);
		jtb.add(cerrar);
		
		retrocM.setFocusable(false);
		retrocF.setFocusable(false);
		retroc.setFocusable(false);
		aat.setFocusable(false);
		parar.setFocusable(false);
		aad.setFocusable(false);
		avance.setFocusable(false);
		avanceF.setFocusable(false);
		avanceM.setFocusable(false);
		cerrar.setFocusable(false);
		
		
		this.add(jtb);
		/*this.add(retrocM);
		this.add(retrocF);
		this.add(retroc);
		this.add(aat);
		this.add(parar);
		this.add(aad);
		this.add(avance);
		this.add(avanceF);
		this.add(avanceM);
		this.add(cerrar);*/
		
		//this.setBorder(new  BorderUIResource.MatteBorderUIResource(1, 1, 2, 2, new Color(130,130,130)));	// l�neas de grosor configurable
		this.setBorder(new  MetalBorders.PaletteBorder());
		//this.setBackground(Conf.colorFrontalPanelContenedor);
		//barraSeg.setBackground(Conf.colorFrontalPanelContenedor);	// no quitar
		
		campoSeg.setText(""+this.seg);
	}
	
	
	public void setToolTipText()
	{
		retrocM.setToolTipText(Texto.get("PBV_SALTARAT",Conf.idioma));
		retrocF.setToolTipText(Texto.get("PBV_INICIO",Conf.idioma));
		retroc.setToolTipText(Texto.get("PBV_RETROC",Conf.idioma));
		aat.setToolTipText(Texto.get("PBV_ANIMATRAS",Conf.idioma));
		parar.setToolTipText(Texto.get("PBV_ANIMPARAR",Conf.idioma));
		aad.setToolTipText(Texto.get("PBV_ANIMADEL",Conf.idioma));
		avance.setToolTipText(Texto.get("PBV_AVANCE",Conf.idioma));
		avanceF.setToolTipText(Texto.get("PBV_FINAL",Conf.idioma));
		avanceM.setToolTipText(Texto.get("PBV_SALTARAD",Conf.idioma));
		cerrar.setToolTipText(Texto.get("PBV_CERRAR",Conf.idioma));
		campoSeg.setToolTipText(Texto.get("PBV_NUMSEG",Conf.idioma));
		barraSeg.setToolTipText(Texto.get("PBV_REGULAR05",Conf.idioma));
	}
	
	
	
	
	
	
	public void setValores(PanelAlgoritmo pAlgoritmo)
	{
		this.pAlgoritmo=pAlgoritmo;
		
		this.seg=(float)1.0;
		this.adelante=true;
	
		if (Ventana.thisventana.traza!=null)
		{
			retrocM.habilitar();
			retrocF.habilitar();
			retroc.habilitar();
			aat.habilitar();
			parar.habilitar();
			aad.habilitar();
			avance.habilitar();
			avanceF.habilitar();
			avanceM.habilitar();
			cerrar.habilitar();
		
			if (Ventana.thisventana.traza.vacioVisible() || Ventana.thisventana.traza.raizInicio())
			{
				retrocF.deshabilitar();
				retroc.deshabilitar();
				aat.deshabilitar();
			}
			if (Ventana.thisventana.traza.enteroVisible())
			{
				avance.deshabilitar();
				avanceF.deshabilitar();
				aad.deshabilitar();
			}
			if (Ventana.thisventana.traza.nodoActualCompleto())
				avanceM.deshabilitar();
			else
				retrocM.deshabilitar();
			
			parar.deshabilitar();
		}
		else
		{
			retrocM.deshabilitar();
			retrocF.deshabilitar();
			retroc.deshabilitar();
			aat.deshabilitar();
			parar.deshabilitar();
			aad.deshabilitar();
			avance.deshabilitar();
			avanceF.deshabilitar();
			avanceM.deshabilitar();
			
			if (Ventana.thisventana.panelOcupado())
				cerrar.habilitar();
			else
				cerrar.deshabilitar();
		}
		this.updateUI();
		
		campoSeg.setText(""+this.seg);
		
		this.setBorder(new  MetalBorders.PaletteBorder());
		//this.setBorder(new  BorderUIResource.MatteBorderUIResource(1, 1, 2, 2, new Color(130,130,130)));	// l�neas de grosor configurable
	}
	
	/**
		Comprueba si la traza es completamente visible
		
		@return true si la traza es completamente visible
	*/
	public boolean enteroVisible()
	{
		return Ventana.thisventana.traza.enteroVisible();
	}

	/**
		Comprueba si la traza es completamente no visible
		
		@return true si la traza es completamente no visible
	*/
	public boolean vacioVisible()
	{
		return Ventana.thisventana.traza.vacioVisible();
	}
	
	/**
		Comprueba si ya se ha activado alguna animaci�n
		
		@return true si ya se ha activado alguna animaci�n
	*/
	public boolean hayAnimacion()
	{
		return hayAnimacion!=0;
	}
	
	// Se empla s�lo al redibujar �rbol
	public void visualizar()
	{
		retrocM.habilitar();
		retrocF.habilitar();
		retroc.habilitar();
		aat.habilitar();
		parar.habilitar();
		aad.habilitar();
		avance.habilitar();
		avanceF.habilitar();
		avanceM.habilitar();
		cerrar.habilitar();
	
		if (Ventana.thisventana.traza==null || Ventana.thisventana.traza.vacioVisible() || Ventana.thisventana.traza.raizInicio())
		{
			retrocF.deshabilitar();
			retroc.deshabilitar();
			aat.deshabilitar();
		}
		if (Ventana.thisventana.traza==null || Ventana.thisventana.traza.enteroVisible())
		{
			avance.deshabilitar();
			avanceF.deshabilitar();
			aad.deshabilitar();
		}
		if (Ventana.thisventana.traza==null || Ventana.thisventana.traza.nodoActualCompleto())
			avanceM.deshabilitar();
		else
			retrocM.deshabilitar();
		
		if (hayAnimacion==0)	// Si no hay animaci�n
			parar.deshabilitar();
		
		//this.setBorder(new  BorderUIResource.MatteBorderUIResource(1, 1, 2, 2, new Color(130,130,130)));	// l�neas de grosor configurable
	}
	
	
	
	/**
		Asigna el valor de la variable que dice si hay o no una animaci�n activada y colorea los botones
		
		@param valor 0=no hay animaci�n, 1=animaci�n hacia adelante, 2=hacia atr�s
	*/
	public void setAnimacion(int valor)
	{
		if (Ventana.thisventana.traza!=null)	// Si cerramos, puede ser la traza null
		{
			this.hayAnimacion=valor;
			if (valor==0)
			{
				parar.deshabilitar();
				if (Ventana.thisventana.traza.raizInicio())
				{
					aad.habilitar();
					aat.deshabilitar();
				}
				else if (Ventana.thisventana.traza.enteroVisible())
				{
					aad.deshabilitar();
					aat.habilitar();
				}
				else
				{
					aad.habilitar();
					aat.habilitar();
				}
			}
			else if (valor==1)
			{
				aad.mostrarPulsado();
				aat.habilitar();
				parar.habilitar();
			}
			else if (valor==2)
			{
				aad.habilitar();
				aat.mostrarPulsado();
				parar.habilitar();
			}
		}
	}

	/**
		Asigna la configuraci�n correspondiente a la animaci�n
		
		@param seg n�mero de segundos entre cada paso de la animaci�n
		@param adelante true si la animaci�n es hacia adelante
	*/	
	public void setValoresAnimacion(float seg, boolean adelante)
	{
		this.seg=seg;
		this.adelante=adelante;
	}
	
	/**
		Sit�a la visualizaci�n al principio de la misma
	*/
	public void hacer_inicio()
	{
		if (Ventana.thisventana.traza==null)
			return;
	
		if (!Ventana.thisventana.traza.raizInicio())
		{
			Ventana.thisventana.traza.nadaVisible();
			actualizar();
		}
		avance.habilitar();
		avanceF.habilitar();
		avanceM.habilitar();
		retroc.deshabilitar();
		retrocF.deshabilitar();
		retrocM.deshabilitar();
		aat.deshabilitar();
		setAnimacion(0);

		//avanceM.deshabilitar();
		

	}
	
	/**
		Sit�a la visualizaci�n un paso hacia atr�s
	*/
	public void hacer_retrocMultiple()
	{
		if (Ventana.thisventana.traza==null)
			return;
			
		avance.habilitar();
		avanceF.habilitar();
		avanceM.habilitar();
		aad.habilitar();

		try {
			Ventana.thisventana.traza.anteriorMultipleVisible();
			actualizar();
			} catch (Exception e) {
			hayAnimacion=0;
			new CuadroError(Ventana.thisventana,Texto.get("ERROR_VISU",Conf.idioma), 
								Texto.get("ERROR_VISEXC",Conf.idioma)+": "+e.getCause());
			if (depurar) { System.out.println("<PanelBotonesVisualizacionArbol - 1>"); e.printStackTrace(); }
			pAlgoritmo.cerrar();
		}
		
		if (Ventana.thisventana.traza.vacioVisible())
		{
			retroc.deshabilitar();
			retrocF.deshabilitar();
			setAnimacion(0);
		}
		if (Ventana.thisventana.traza.nodoActualCompleto())
		{
			avanceM.deshabilitar();
			if (Ventana.thisventana.traza.vacioVisible())
				retrocM.deshabilitar();
			else
				retrocM.habilitar();
		}
		else
		{
			avanceM.habilitar();
			retrocM.deshabilitar();
		}
		if (Ventana.thisventana.traza.getRaiz().getEsNodoActual())
		{
			retroc.deshabilitar();
			retrocF.deshabilitar();
			retrocM.deshabilitar();
			aat.deshabilitar();
		}
	}
	
	/**
		Sit�a la visualizaci�n un paso hacia atr�s
	*/
	public void hacer_retroc()
	{	
		if (Ventana.thisventana.traza==null)
			return;
			
		avance.habilitar();
		avanceF.habilitar();
		avanceM.habilitar();
		aad.habilitar();

		try {
			if (!Ventana.thisventana.traza.raizInicio())
			{
				Ventana.thisventana.traza.anteriorVisible();
				actualizar();
			}
			} catch (Exception e) {
				hayAnimacion=0;
				new CuadroError(Ventana.thisventana,Texto.get("ERROR_VISU",Conf.idioma), 
								Texto.get("ERROR_VISEXC",Conf.idioma)+": "+e.getCause());
				if (depurar) { System.out.println("<PanelBotonesVisualizacionArbol - 2>"); e.printStackTrace(); }				
			pAlgoritmo.cerrar();
		}
		
		if (Ventana.thisventana.traza.raizInicio())
		{
			//traza.siguienteVisible();
			setAnimacion(0);
			retroc.deshabilitar();
			retrocF.deshabilitar();
			retrocM.deshabilitar();
			aat.deshabilitar();
		}
		if (Ventana.thisventana.traza.nodoActualCompleto())
		{
			avanceM.deshabilitar();
			if (Ventana.thisventana.traza.vacioVisible())
				retrocM.deshabilitar();
			else
				retrocM.habilitar();
		}
		else
		{
			avanceM.habilitar();
			retrocM.deshabilitar();
		}
		if (Ventana.thisventana.traza.getRaiz().getEsNodoActual())
		{
			if (!Ventana.thisventana.traza.getRaiz().esMostradoEntero())
			{
				retrocM.deshabilitar();
				retrocF.deshabilitar();
				retroc.deshabilitar();
				aat.deshabilitar();
			}
		}
	}
	
	/**
		Sit�a la visualizaci�n un paso hacia adelante
	*/
	public void hacer_avance()
	{
		if (Ventana.thisventana.traza==null)
			return;
	
		//Ventana.thisventana.getTraza().verArbol();
		
		avanceM.habilitar();
		retroc.habilitar();
		retrocF.habilitar();
		aat.habilitar();
		try {
			Ventana.thisventana.traza.siguienteVisible();
			actualizar();
		} catch (Exception e) {
			hayAnimacion=0;
			new CuadroError(Ventana.thisventana,Texto.get("ERROR_VISU",Conf.idioma),
							Texto.get("ERROR_VISEXC",Conf.idioma)+": "+e.getCause());
			if (depurar) { System.out.println("<PanelBotonesVisualizacionArbol - 3>"); e.printStackTrace(); }
			pAlgoritmo.cerrar();
			e.printStackTrace();
		}

		if (Ventana.thisventana.traza.enteroVisible())
		{
			avance.deshabilitar();
			avanceF.deshabilitar();
			setAnimacion(0);
		}
		if (Ventana.thisventana.traza.nodoActualCompleto())
		{
			avanceM.deshabilitar();
			retrocM.habilitar();
		}
		else
		{
			avanceM.habilitar();
			retrocM.deshabilitar();
		}
		if (!habilitado)	// �ste es el �nico m�todo que se ejecuta con habilitado=false, mantenemos colores
		{
			retrocM.deshabilitar();
			retrocF.deshabilitar();
			retroc.deshabilitar();
			aat.deshabilitar();
			parar.deshabilitar();
			aad.deshabilitar();
			avance.deshabilitar();
			avanceF.deshabilitar();
			avanceM.deshabilitar();
			cerrar.deshabilitar();
		}
	}
	
	/**
		Sit�a la visualizaci�n uno o varios pasos hacia adelante
	*/
	public void hacer_avanceMultiple()
	{
		if (Ventana.thisventana.traza==null)
			return;
			
		retroc.habilitar();
		retrocF.habilitar();
		aat.habilitar();
		try {
			Ventana.thisventana.traza.siguienteMultipleVisible();
			actualizar();
		} catch (Exception e) {
			hayAnimacion=0;
			new CuadroError(Ventana.thisventana,Texto.get("ERROR_VISU",Conf.idioma),
							Texto.get("ERROR_VISEXC",Conf.idioma)+": "+e.getCause());
			if (depurar) { System.out.println("<PanelBotonesVisualizacionArbol - 4>"); e.printStackTrace(); }
			pAlgoritmo.cerrar();
		}

		if (Ventana.thisventana.traza.enteroVisible())
		{
			avance.deshabilitar();
			avanceF.deshabilitar();
			avanceM.deshabilitar();
			setAnimacion(0);
		}
		if (Ventana.thisventana.traza.nodoActualCompleto())
		{
			avanceM.deshabilitar();
			retrocM.habilitar();
		}
		else
		{
			avanceM.habilitar();
			retrocM.deshabilitar();
		}
		if (Ventana.thisventana.traza.getRaiz().getEsNodoActual())
		{
			avance.deshabilitar();
			avanceF.deshabilitar();
			avanceM.deshabilitar();
			aad.deshabilitar();
		}
	}
	
	/**
		Sit�a la visualizaci�n al final
	*/
	public void hacer_final()
	{
		if (Ventana.thisventana.traza==null)
			return;
			
		if (!Ventana.thisventana.traza.enteroVisible())
		{
			try {
				Ventana.thisventana.traza.todoVisible();
				actualizar();
			} catch (Exception e) {
				
				hayAnimacion=0;
				new CuadroError(Ventana.thisventana,Texto.get("ERROR_VISU",Conf.idioma),
						Texto.get("ERROR_VISEXC",Conf.idioma)+": "+e.getCause());
				if (depurar) { System.out.println("<PanelBotonesVisualizacionArbol - 5>"); e.printStackTrace(); }
				pAlgoritmo.cerrar();
			}
		}
		retrocM.habilitar();
		retrocF.habilitar();
		retroc.habilitar();
		avance.deshabilitar();
		avanceF.deshabilitar();
		avanceM.deshabilitar();
		setAnimacion(0);
	}

	/**
		Gestiona la creaci�n de una animaci�n
	*/
	public void hacer_animacion(int sentido)
	{
		if (Ventana.thisventana.traza==null)
			return;
			
		if (hayAnimacion==0)	// Si no hay animacion, creamos una
		{
			this.hayAnimacion=sentido;	// Debe ir delante de new Animacion, pero despu�s de evaluacion de condici�n de if
			new Animacion(this);	// El nuevo thread leer� de esta clase los valores de sentido y tiempo
		}
		else	// Aprovechamos la que ya hay, actualiz�ndola
		{
			this.hayAnimacion=sentido;
		}
		if (this.hayAnimacion==1)
		{
			aat.habilitar();
			aad.mostrarPulsado();
		}
		else if (this.hayAnimacion==2)
		{
			aat.mostrarPulsado();
			aad.habilitar();
		}
		parar.habilitar();
	}
	
	/**
		Actualiza la interfaz de la visualizaci�n
	*/
	public void actualizar()
	{
		if (Ventana.thisventana.traza==null)
			return;
			
		pAlgoritmo.actualizar();						
	}

	/**
		Devuelve el n�mero de segundos que hay configurado entre cada paso de la animaci�n
		
		@return n�mero de segundos entre cada paso de la animaci�n
	*/	
	public float getSeg()
	{
		if (valorValidoFloat())
			this.seg=Float.parseFloat(campoSeg.getText());
		if (this.seg==0)
			seg=(float)0.15;
		return this.seg;
	}
	
	/**
		Devuelve el sentido de la animaci�n que hay configurado
		
		@return true si la animaci�n es hacia adelante
	*/	
	public boolean getSentido()
	{
		if (hayAnimacion==1)
			return true;
		else
			return false;
	}
	
	/**
		M�todo auxiliar que determina si el valor escrito en el cuadro de texto es v�lido
		
		@return true si la animaci�n es hacia adelante
	*/	
	private boolean valorValidoFloat()
	{
		String s=null;
		
		try {
			s=campoSeg.getText().replace(" ","");
		} catch (java.lang.NullPointerException npe) {
		}
		
		if (s==null || s.length()==0)
			return false;	// No hay n�mero escrito

		for (int i=0; i<s.length(); i++)
			if ( (s.charAt(i)<'0' || s.charAt(i)>'9') && s.charAt(i)!='.')
				return false;	// Hay caracteres no permitidos
		
		int x=0;
		for (int i=0; i<s.length(); i++)
			if ( s.charAt(i)=='.' )
				x++;
		if (x>1)
			return false;	// Hay m�s de un punto
		
		return true;
	}

			/**
		M�todo que gestiona los eventos de rat�n
		
		@param e evento de rat�n
	*/	
	public void mouseClicked(MouseEvent e) 
	{
	}
	
	/**
		M�todo que gestiona los eventos de rat�n
		
		@param e evento de rat�n
	*/
	public void mouseEntered(MouseEvent e) {
		if (e.getComponent() instanceof BotonImagen) {
			BotonImagen b = (BotonImagen)e.getComponent();
			b.ratonEstaSobreBoton();
		}
	}
	
	/**
		M�todo que gestiona los eventos de rat�n
		
		@param e evento de rat�n
	*/
	public void mouseExited(MouseEvent e) {
		if (e.getComponent() instanceof BotonImagen) {
			BotonImagen b = (BotonImagen) e.getComponent();
			b.ratonNoEstaSobreBoton();
		}
	}
	
	/**
		M�todo que gestiona los eventos de rat�n
		
		@param e evento de rat�n
	*/
	public void mousePressed(MouseEvent e) 
	{
		if (Ventana.thisventana.traza==null && e.getSource()!=cerrar)
			return;
			
		//VentanaVisualizador.memoria();
		
		if (e.getSource() instanceof BotonImagen)
		{
			BotonImagen b = (BotonImagen)e.getSource();
			if (habilitado && b.isEnabled())
			{
				if (e.getSource()==retrocM)
				{
					if (Conf.fichero_log) Logger.log_write("Visualizaci�n: [ <- ]");
					hacer_retrocMultiple();
				}
				else if (e.getSource()==retrocF)
				{
					if (Conf.fichero_log) Logger.log_write("Visualizaci�n: [ << ]");
					hacer_inicio();
				}
				else if (e.getSource()==retroc)
				{
					if (Conf.fichero_log) Logger.log_write("Visualizaci�n: [ |< ]");
					hacer_retroc();
				}
				else if (e.getSource()==aat)
				{
					if (Conf.fichero_log) Logger.log_write("Visualizaci�n: [ < ]");
					hacer_animacion(2);	
				}
				else if (e.getSource()==parar)
				{
					if (Conf.fichero_log) Logger.log_write("Visualizaci�n: [ || ]");
					setAnimacion(0);
				}
				else if (e.getSource()==aad)
				{
					if (Conf.fichero_log) Logger.log_write("Visualizaci�n: [ > ]");
					hacer_animacion(1);
				}
				else if (e.getSource()==avance)
				{
					if (Conf.fichero_log) Logger.log_write("Visualizaci�n: [ >| ]");
					hacer_avance();
				}
				else if (e.getSource()==avanceF)
				{
					if (Conf.fichero_log) Logger.log_write("Visualizaci�n: [ >> ]");
					hacer_final();
				}
				else if (e.getSource()==avanceM)
				{
					if (Conf.fichero_log) Logger.log_write("Visualizaci�n: [ -> ]");
					hacer_avanceMultiple();	
				}
				else if (e.getSource()==cerrar)
				{
					if (Conf.fichero_log) Logger.log_write("Visualizaci�n: [ X ]");
					setAnimacion(0);
					pAlgoritmo.cerrar();
					Ventana.thisventana.cerrarVentana();
					//if (Ventana.thisventana.getClase()==null)
					//	Ventana.thisventana.setTitulo("");
				}
			}
		}
		// DEPURAR
	}
	
	/**
		M�todo que gestiona los eventos de rat�n
		
		@param e evento de rat�n
	*/
	public void mouseReleased(MouseEvent e)
	{
	}

	/**
		M�todo que gestiona los eventos de estado
		
		@param e evento de estado
	*/
	public void stateChanged(ChangeEvent e)
	{
		if (Conf.fichero_log) Logger.log_write("Barra velocidad animaci�n: [ "+barraSeg.getValue()+" ]");
		campoSeg.setText(""+barraSeg.getValue()+".0");
	}
	
	protected void deshabilitarControles()
	{
		this.habilitado=false;
		
		retrocM.deshabilitar();
		retrocF.deshabilitar();
		retroc.deshabilitar();
		aat.deshabilitar();
		parar.deshabilitar();
		aad.deshabilitar();
		avance.deshabilitar();
		avanceF.deshabilitar();
		avanceM.deshabilitar();
		cerrar.deshabilitar();
		
		campoSeg.setEnabled(false);
		barraSeg.setEnabled(false);
	}
	
	protected void habilitarControles()
	{
		this.habilitado=true;
		
		campoSeg.setEnabled(true);
		barraSeg.setEnabled(true);
		
		if (Ventana.thisventana.traza.raizInicio())
		{
			retrocF.deshabilitar();
			retroc.deshabilitar();
			aat.deshabilitar();
		}
		else 
		{
			retrocM.habilitar();
			retrocF.habilitar();
			retroc.habilitar();
			aat.habilitar();
		}
		
		parar.deshabilitar();
		
		if (Ventana.thisventana.traza.enteroVisible())
		{
			aad.deshabilitar();
			avance.deshabilitar();
			avanceF.deshabilitar();
		}
		else
		{
			aad.habilitar();
			avance.habilitar();
			avanceF.habilitar();
		}
		
		if (Ventana.thisventana.traza.nodoActualCompleto())
		{
			avanceM.deshabilitar();
			retrocM.habilitar();
		}
		else
		{
			avanceM.habilitar();
			retrocM.deshabilitar();
		}
		
		cerrar.habilitar();
	}	
}


