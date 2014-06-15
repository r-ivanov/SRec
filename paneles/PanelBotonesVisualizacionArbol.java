/**
	Panel que contiene los botones que manejan la visualización del algoritmo
	
	@author Antonio Pérez Carrasco
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

import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import botones.*;
import conf.*;
import cuadros.*;

import grafica.*;
import utilidades.*;

import ventanas.*;

public class PanelBotonesVisualizacionArbol extends PanelBotonesVisualizacion
						implements MouseListener, ChangeListener
{
	static final long serialVersionUID=12;
	

	// Botones manuales de retroceso
	private Boton retrocM = new Boton("imagenes/ba_salto_at_v.gif","imagenes/ba_salto_at_n.gif","imagenes/ba_salto_at_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);

	private Boton retrocF = new Boton("imagenes/ba_extr_at_v.gif","imagenes/ba_extr_at_n.gif","imagenes/ba_extr_at_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);

	private Boton retroc = new Boton("imagenes/ba_paso_at_v.gif","imagenes/ba_paso_at_n.gif","imagenes/ba_paso_at_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);

	// Botones de animacion
	private Boton aat = new Boton("imagenes/ba_anim_at_v.gif","imagenes/ba_anim_at_n.gif","imagenes/ba_anim_at_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
									
	private Boton parar = new Boton("imagenes/ba_pausa_v.gif","imagenes/ba_pausa_n.gif","imagenes/ba_pausa_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
										
	private Boton aad = new Boton("imagenes/ba_anim_ad_v.gif","imagenes/ba_anim_ad_n.gif","imagenes/ba_anim_ad_r.gif",  
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
	
	// Botones manuales de avance
	private Boton avance = new Boton("imagenes/ba_paso_ad_v.gif","imagenes/ba_paso_ad_n.gif","imagenes/ba_paso_ad_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);

	private Boton avanceF = new Boton("imagenes/ba_extr_ad_v.gif","imagenes/ba_extr_ad_n.gif","imagenes/ba_extr_ad_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
	
	private Boton avanceM = new Boton("imagenes/ba_salto_ad_v.gif","imagenes/ba_salto_ad_n.gif","imagenes/ba_salto_ad_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);

	// Botón de cierre
	private Boton cerrar = new Boton("imagenes/ba_cerrar_v.gif","imagenes/ba_cerrar_n.gif","imagenes/ba_cerrar_r.gif", 
										Conf.botonVisualizacionAncho,Conf.botonVisualizacionAlto);
	
	
	
	
	private JTextField campoSeg = new JTextField(3);
	private JSlider barraSeg = new JSlider(0,5,1);
	

	boolean habilitado= true;	// para cuando se hacen capturas animadas
	
	
	private PanelAlgoritmo pAlgoritmo;
	
	int hayAnimacion=0;	// 0=no hay animación  1=hacia adelante  2=hacia atrás
	
	float seg;
	boolean adelante;
	boolean depurar=true;
	
	/**
		Constructor: crea un nuevo panel de botones de visualización
		
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
				retrocF.setRojo();
				retroc.setRojo();
				aat.setRojo();
			}
			if (Ventana.thisventana.traza.enteroVisible())
			{
				avance.setRojo();
				avanceF.setRojo();
				aad.setRojo();
			}
			if (Ventana.thisventana.traza.nodoActualCompleto())
				avanceM.setRojo();
			else
				retrocM.setRojo();
			
			parar.setRojo();
		}
		else
		{
			retrocM.setRojo();
			retrocF.setRojo();
			retroc.setRojo();
			aat.setRojo();
			parar.setRojo();
			aad.setRojo();
			avance.setRojo();
			avanceF.setRojo();
			avanceM.setRojo();
			cerrar.setRojo();
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
		
		//this.setBorder(new  BorderUIResource.MatteBorderUIResource(1, 1, 2, 2, new Color(130,130,130)));	// líneas de grosor configurable
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
			retrocM.setVerde();
			retrocF.setVerde();
			retroc.setVerde();
			aat.setVerde();
			parar.setVerde();
			aad.setVerde();
			avance.setVerde();
			avanceF.setVerde();
			avanceM.setVerde();
			cerrar.setVerde();
		
			if (Ventana.thisventana.traza.vacioVisible() || Ventana.thisventana.traza.raizInicio())
			{
				retrocF.setRojo();
				retroc.setRojo();
				aat.setRojo();
			}
			if (Ventana.thisventana.traza.enteroVisible())
			{
				avance.setRojo();
				avanceF.setRojo();
				aad.setRojo();
			}
			if (Ventana.thisventana.traza.nodoActualCompleto())
				avanceM.setRojo();
			else
				retrocM.setRojo();
			
			parar.setRojo();
		}
		else
		{
			retrocM.setRojo();
			retrocF.setRojo();
			retroc.setRojo();
			aat.setRojo();
			parar.setRojo();
			aad.setRojo();
			avance.setRojo();
			avanceF.setRojo();
			avanceM.setRojo();
			
			if (Ventana.thisventana.panelOcupado())
				cerrar.setVerde();
			else
				cerrar.setRojo();
		}
		this.updateUI();
		
		campoSeg.setText(""+this.seg);
		
		this.setBorder(new  MetalBorders.PaletteBorder());
		//this.setBorder(new  BorderUIResource.MatteBorderUIResource(1, 1, 2, 2, new Color(130,130,130)));	// líneas de grosor configurable
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
		Comprueba si ya se ha activado alguna animación
		
		@return true si ya se ha activado alguna animación
	*/
	public boolean hayAnimacion()
	{
		return hayAnimacion!=0;
	}
	
	// Se empla sólo al redibujar árbol
	public void visualizar()
	{
		retrocM.setVerde();
		retrocF.setVerde();
		retroc.setVerde();
		aat.setVerde();
		parar.setVerde();
		aad.setVerde();
		avance.setVerde();
		avanceF.setVerde();
		avanceM.setVerde();
		cerrar.setVerde();
	
		if (Ventana.thisventana.traza==null || Ventana.thisventana.traza.vacioVisible() || Ventana.thisventana.traza.raizInicio())
		{
			retrocF.setRojo();
			retroc.setRojo();
			aat.setRojo();
		}
		if (Ventana.thisventana.traza==null || Ventana.thisventana.traza.enteroVisible())
		{
			avance.setRojo();
			avanceF.setRojo();
			aad.setRojo();
		}
		if (Ventana.thisventana.traza==null || Ventana.thisventana.traza.nodoActualCompleto())
			avanceM.setRojo();
		else
			retrocM.setRojo();
		
		if (hayAnimacion==0)	// Si no hay animación
			parar.setRojo();
		
		//this.setBorder(new  BorderUIResource.MatteBorderUIResource(1, 1, 2, 2, new Color(130,130,130)));	// líneas de grosor configurable
	}
	
	
	
	/**
		Asigna el valor de la variable que dice si hay o no una animación activada y colorea los botones
		
		@param valor 0=no hay animación, 1=animación hacia adelante, 2=hacia atrás
	*/
	public void setAnimacion(int valor)
	{
		if (Ventana.thisventana.traza!=null)	// Si cerramos, puede ser la traza null
		{
			this.hayAnimacion=valor;
			if (valor==0)
			{
				parar.setRojo();
				if (Ventana.thisventana.traza.raizInicio())
				{
					aad.setVerde();
					aat.setRojo();
				}
				else if (Ventana.thisventana.traza.enteroVisible())
				{
					aad.setRojo();
					aat.setVerde();
				}
				else
				{
					aad.setVerde();
					aat.setVerde();
				}
			}
			else if (valor==1)
			{
				aad.setNaranja();
				aat.setVerde();
				parar.setVerde();
			}
			else if (valor==2)
			{
				aad.setVerde();
				aat.setNaranja();
				parar.setVerde();
			}
		}
	}

	/**
		Asigna la configuración correspondiente a la animación
		
		@param seg número de segundos entre cada paso de la animación
		@param adelante true si la animación es hacia adelante
	*/	
	public void setValoresAnimacion(float seg, boolean adelante)
	{
		this.seg=seg;
		this.adelante=adelante;
	}
	
	/**
		Sitúa la visualización al principio de la misma
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
		avance.setVerde();
		avanceF.setVerde();
		avanceM.setVerde();
		retroc.setRojo();
		retrocF.setRojo();
		retrocM.setRojo();
		aat.setRojo();
		setAnimacion(0);

		//avanceM.setRojo();
		

	}
	
	/**
		Sitúa la visualización un paso hacia atrás
	*/
	public void hacer_retrocMultiple()
	{
		if (Ventana.thisventana.traza==null)
			return;
			
		avance.setVerde();
		avanceF.setVerde();
		avanceM.setVerde();
		aad.setVerde();

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
			retroc.setRojo();
			retrocF.setRojo();
			setAnimacion(0);
		}
		if (Ventana.thisventana.traza.nodoActualCompleto())
		{
			avanceM.setRojo();
			if (Ventana.thisventana.traza.vacioVisible())
				retrocM.setRojo();
			else
				retrocM.setVerde();
		}
		else
		{
			avanceM.setVerde();
			retrocM.setRojo();
		}
		if (Ventana.thisventana.traza.getRaiz().getEsNodoActual())
		{
			retroc.setRojo();
			retrocF.setRojo();
			retrocM.setRojo();
			aat.setRojo();
		}
	}
	
	/**
		Sitúa la visualización un paso hacia atrás
	*/
	public void hacer_retroc()
	{	
		if (Ventana.thisventana.traza==null)
			return;
			
		avance.setVerde();
		avanceF.setVerde();
		avanceM.setVerde();
		aad.setVerde();

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
			retroc.setRojo();
			retrocF.setRojo();
			retrocM.setRojo();
			aat.setRojo();
		}
		if (Ventana.thisventana.traza.nodoActualCompleto())
		{
			avanceM.setRojo();
			if (Ventana.thisventana.traza.vacioVisible())
				retrocM.setRojo();
			else
				retrocM.setVerde();
		}
		else
		{
			avanceM.setVerde();
			retrocM.setRojo();
		}
		if (Ventana.thisventana.traza.getRaiz().getEsNodoActual())
		{
			if (!Ventana.thisventana.traza.getRaiz().esMostradoEntero())
			{
				retrocM.setRojo();
				retrocF.setRojo();
				retroc.setRojo();
				aat.setRojo();
			}
		}
	}
	
	/**
		Sitúa la visualización un paso hacia adelante
	*/
	public void hacer_avance()
	{
		if (Ventana.thisventana.traza==null)
			return;
	
		//Ventana.thisventana.getTraza().verArbol();
		
		avanceM.setVerde();
		retroc.setVerde();
		retrocF.setVerde();
		aat.setVerde();
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
			avance.setRojo();
			avanceF.setRojo();
			setAnimacion(0);
		}
		if (Ventana.thisventana.traza.nodoActualCompleto())
		{
			avanceM.setRojo();
			retrocM.setVerde();
		}
		else
		{
			avanceM.setVerde();
			retrocM.setRojo();
		}
		if (!habilitado)	// Éste es el único método que se ejecuta con habilitado=false, mantenemos colores
		{
			retrocM.setRojo();
			retrocF.setRojo();
			retroc.setRojo();
			aat.setRojo();
			parar.setRojo();
			aad.setRojo();
			avance.setRojo();
			avanceF.setRojo();
			avanceM.setRojo();
			cerrar.setRojo();
		}
	}
	
	/**
		Sitúa la visualización uno o varios pasos hacia adelante
	*/
	public void hacer_avanceMultiple()
	{
		if (Ventana.thisventana.traza==null)
			return;
			
		retroc.setVerde();
		retrocF.setVerde();
		aat.setVerde();
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
			avance.setRojo();
			avanceF.setRojo();
			avanceM.setRojo();
			setAnimacion(0);
		}
		if (Ventana.thisventana.traza.nodoActualCompleto())
		{
			avanceM.setRojo();
			retrocM.setVerde();
		}
		else
		{
			avanceM.setVerde();
			retrocM.setRojo();
		}
		if (Ventana.thisventana.traza.getRaiz().getEsNodoActual())
		{
			avance.setRojo();
			avanceF.setRojo();
			avanceM.setRojo();
			aad.setRojo();
		}
	}
	
	/**
		Sitúa la visualización al final
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
		retrocM.setVerde();
		retrocF.setVerde();
		retroc.setVerde();
		avance.setRojo();
		avanceF.setRojo();
		avanceM.setRojo();
		setAnimacion(0);
	}

	/**
		Gestiona la creación de una animación
	*/
	public void hacer_animacion(int sentido)
	{
		if (Ventana.thisventana.traza==null)
			return;
			
		if (hayAnimacion==0)	// Si no hay animacion, creamos una
		{
			this.hayAnimacion=sentido;	// Debe ir delante de new Animacion, pero después de evaluacion de condición de if
			new Animacion(this);	// El nuevo thread leerá de esta clase los valores de sentido y tiempo
		}
		else	// Aprovechamos la que ya hay, actualizándola
		{
			this.hayAnimacion=sentido;
		}
		if (this.hayAnimacion==1)
		{
			aat.setVerde();
			aad.setNaranja();
		}
		else if (this.hayAnimacion==2)
		{
			aat.setNaranja();
			aad.setVerde();
		}
		parar.setVerde();
	}
	
	/**
		Actualiza la interfaz de la visualización
	*/
	public void actualizar()
	{
		if (Ventana.thisventana.traza==null)
			return;
			
		pAlgoritmo.actualizar();						
	}

	/**
		Devuelve el número de segundos que hay configurado entre cada paso de la animación
		
		@return número de segundos entre cada paso de la animación
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
		Devuelve el sentido de la animación que hay configurado
		
		@return true si la animación es hacia adelante
	*/	
	public boolean getSentido()
	{
		if (hayAnimacion==1)
			return true;
		else
			return false;
	}
	
	/**
		Método auxiliar que determina si el valor escrito en el cuadro de texto es válido
		
		@return true si la animación es hacia adelante
	*/	
	private boolean valorValidoFloat()
	{
		String s=null;
		
		try {
			s=campoSeg.getText().replace(" ","");
		} catch (java.lang.NullPointerException npe) {
		}
		
		if (s==null || s.length()==0)
			return false;	// No hay número escrito

		for (int i=0; i<s.length(); i++)
			if ( (s.charAt(i)<'0' || s.charAt(i)>'9') && s.charAt(i)!='.')
				return false;	// Hay caracteres no permitidos
		
		int x=0;
		for (int i=0; i<s.length(); i++)
			if ( s.charAt(i)=='.' )
				x++;
		if (x>1)
			return false;	// Hay más de un punto
		
		return true;
	}

			/**
		Método que gestiona los eventos de ratón
		
		@param e evento de ratón
	*/	
	public void mouseClicked(MouseEvent e) 
	{
	}
	
	/**
		Método que gestiona los eventos de ratón
		
		@param e evento de ratón
	*/
	public void mouseEntered(MouseEvent e) 
	{
		if (e.getComponent() instanceof Boton)
		{
			Boton b=(Boton)e.getComponent();
			b.setNaranja();
			b.tieneRaton(true);
		}
	}
	
	/**
		Método que gestiona los eventos de ratón
		
		@param e evento de ratón
	*/
	public void mouseExited(MouseEvent e) 
	{
		if (e.getComponent() instanceof Boton)
		{
			Boton b=(Boton)e.getComponent();
			b.tieneRaton(false);
			b.setColorEstado();
		}
	
		if (hayAnimacion==1)
			aad.setNaranja();
		if (hayAnimacion==2)
			aat.setNaranja();


	}
	
	/**
		Método que gestiona los eventos de ratón
		
		@param e evento de ratón
	*/
	public void mousePressed(MouseEvent e) 
	{
		if (Ventana.thisventana.traza==null && e.getSource()!=cerrar)
			return;
			
		//VentanaVisualizador.memoria();
		
		if (e.getSource() instanceof Boton)
		{
			Boton b=(Boton)e.getSource();
			if (habilitado && b.isEnabled())
			{
				if (e.getSource()==retrocM)
				{
					if (Conf.fichero_log) Logger.log_write("Visualización: [ <- ]");
					hacer_retrocMultiple();
				}
				else if (e.getSource()==retrocF)
				{
					if (Conf.fichero_log) Logger.log_write("Visualización: [ << ]");
					hacer_inicio();
				}
				else if (e.getSource()==retroc)
				{
					if (Conf.fichero_log) Logger.log_write("Visualización: [ |< ]");
					hacer_retroc();
				}
				else if (e.getSource()==aat)
				{
					if (Conf.fichero_log) Logger.log_write("Visualización: [ < ]");
					hacer_animacion(2);	
				}
				else if (e.getSource()==parar)
				{
					if (Conf.fichero_log) Logger.log_write("Visualización: [ || ]");
					setAnimacion(0);
				}
				else if (e.getSource()==aad)
				{
					if (Conf.fichero_log) Logger.log_write("Visualización: [ > ]");
					hacer_animacion(1);
				}
				else if (e.getSource()==avance)
				{
					if (Conf.fichero_log) Logger.log_write("Visualización: [ >| ]");
					hacer_avance();
				}
				else if (e.getSource()==avanceF)
				{
					if (Conf.fichero_log) Logger.log_write("Visualización: [ >> ]");
					hacer_final();
				}
				else if (e.getSource()==avanceM)
				{
					if (Conf.fichero_log) Logger.log_write("Visualización: [ -> ]");
					hacer_avanceMultiple();	
				}
				else if (e.getSource()==cerrar)
				{
					if (Conf.fichero_log) Logger.log_write("Visualización: [ X ]");
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
		Método que gestiona los eventos de ratón
		
		@param e evento de ratón
	*/
	public void mouseReleased(MouseEvent e)
	{
	}

	/**
		Método que gestiona los eventos de estado
		
		@param e evento de estado
	*/
	public void stateChanged(ChangeEvent e)
	{
		if (Conf.fichero_log) Logger.log_write("Barra velocidad animación: [ "+barraSeg.getValue()+" ]");
		campoSeg.setText(""+barraSeg.getValue()+".0");
	}
	
	protected void deshabilitarControles()
	{
		this.habilitado=false;
		
		retrocM.setRojo();
		retrocF.setRojo();
		retroc.setRojo();
		aat.setRojo();
		parar.setRojo();
		aad.setRojo();
		avance.setRojo();
		avanceF.setRojo();
		avanceM.setRojo();
		cerrar.setRojo();
		
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
			retrocF.setRojo();
			retroc.setRojo();
			aat.setRojo();
		}
		else 
		{
			retrocM.setVerde();
			retrocF.setVerde();
			retroc.setVerde();
			aat.setVerde();
		}
		
		parar.setRojo();
		
		if (Ventana.thisventana.traza.enteroVisible())
		{
			aad.setRojo();
			avance.setRojo();
			avanceF.setRojo();
		}
		else
		{
			aad.setVerde();
			avance.setVerde();
			avanceF.setVerde();
		}
		
		if (Ventana.thisventana.traza.nodoActualCompleto())
		{
			avanceM.setRojo();
			retrocM.setVerde();
		}
		else
		{
			avanceM.setVerde();
			retrocM.setRojo();
		}
		
		cerrar.setVerde();
	}
	
	
}


