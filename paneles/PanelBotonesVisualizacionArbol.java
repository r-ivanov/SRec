package paneles;

import grafica.Animacion;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalBorders;

import utilidades.Logger;
import utilidades.Texto;
import ventanas.Ventana;
import botones.BotonImagen;
import conf.Conf;
import cuadros.CuadroError;

/**
 * Panel que contiene los botones que manejan la visualización del algoritmo
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class PanelBotonesVisualizacionArbol extends JPanel implements
		MouseListener, ChangeListener {
	static final long serialVersionUID = 12;

	// Botones manuales de retroceso
	private BotonImagen retrocM = new BotonImagen("imagenes/ba_salto_at_v.gif",
			"imagenes/ba_salto_at_n.gif", "imagenes/ba_salto_at_r.gif",
			Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);

	private BotonImagen retrocF = new BotonImagen("imagenes/ba_extr_at_v.gif",
			"imagenes/ba_extr_at_n.gif", "imagenes/ba_extr_at_r.gif",
			Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);

	private BotonImagen retroc = new BotonImagen("imagenes/ba_paso_at_v.gif",
			"imagenes/ba_paso_at_n.gif", "imagenes/ba_paso_at_r.gif",
			Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);

	// Botones de animacion
	private BotonImagen aat = new BotonImagen("imagenes/ba_anim_at_v.gif",
			"imagenes/ba_anim_at_n.gif", "imagenes/ba_anim_at_r.gif",
			Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);

	private BotonImagen parar = new BotonImagen("imagenes/ba_pausa_v.gif",
			"imagenes/ba_pausa_n.gif", "imagenes/ba_pausa_r.gif",
			Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);

	private BotonImagen aad = new BotonImagen("imagenes/ba_anim_ad_v.gif",
			"imagenes/ba_anim_ad_n.gif", "imagenes/ba_anim_ad_r.gif",
			Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);

	// Botones manuales de avance
	private BotonImagen avance = new BotonImagen("imagenes/ba_paso_ad_v.gif",
			"imagenes/ba_paso_ad_n.gif", "imagenes/ba_paso_ad_r.gif",
			Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);

	private BotonImagen avanceF = new BotonImagen("imagenes/ba_extr_ad_v.gif",
			"imagenes/ba_extr_ad_n.gif", "imagenes/ba_extr_ad_r.gif",
			Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);

	private BotonImagen avanceM = new BotonImagen("imagenes/ba_salto_ad_v.gif",
			"imagenes/ba_salto_ad_n.gif", "imagenes/ba_salto_ad_r.gif",
			Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);

	// Botón de cierre
	private BotonImagen cerrar = new BotonImagen("imagenes/ba_cerrar_v.gif",
			"imagenes/ba_cerrar_n.gif", "imagenes/ba_cerrar_r.gif",
			Conf.botonVisualizacionAncho, Conf.botonVisualizacionAlto);

	private JTextField campoSeg = new JTextField(3);
	private JSlider barraSeg = new JSlider(0, 5, 1);

	private boolean habilitado = true; // para cuando se hacen capturas animadas

	private PanelAlgoritmo pAlgoritmo;

	private int hayAnimacion = 0; // 0=no hay animación 1=hacia adelante 2=hacia atrás

	private float seg;
	private boolean depurar = true;

	/**
	 * Constructor: crea un nuevo panel de botones de visualización
	 * 
	 * @param pAlgoritmo
	 *            PanelAlgoritmo al que pertenece
	 */
	public PanelBotonesVisualizacionArbol(PanelAlgoritmo pAlgoritmo) {
		this.pAlgoritmo = pAlgoritmo;

		this.seg = (float) 1.0;

		this.campoSeg.setHorizontalAlignment(SwingConstants.RIGHT);

		if (Ventana.thisventana.traza != null) {
			if (Ventana.thisventana.traza.vacioVisible()
					|| Ventana.thisventana.traza.raizInicio()) {
				this.retrocF.deshabilitar();
				this.retroc.deshabilitar();
				this.aat.deshabilitar();
			}
			if (Ventana.thisventana.traza.enteroVisible()) {
				this.avance.deshabilitar();
				this.avanceF.deshabilitar();
				this.aad.deshabilitar();
			}
			if (Ventana.thisventana.traza.nodoActualCompleto()) {
				this.avanceM.deshabilitar();
			} else {
				this.retrocM.deshabilitar();
			}

			this.parar.deshabilitar();
		} else {
			this.retrocM.deshabilitar();
			this.retrocF.deshabilitar();
			this.retroc.deshabilitar();
			this.aat.deshabilitar();
			this.parar.deshabilitar();
			this.aad.deshabilitar();
			this.avance.deshabilitar();
			this.avanceF.deshabilitar();
			this.avanceM.deshabilitar();
			this.cerrar.deshabilitar();
		}

		this.retrocM.addMouseListener(this);
		this.retrocF.addMouseListener(this);
		this.retroc.addMouseListener(this);
		this.aat.addMouseListener(this);
		this.parar.addMouseListener(this);
		this.aad.addMouseListener(this);
		this.avance.addMouseListener(this);
		this.avanceF.addMouseListener(this);
		this.avanceM.addMouseListener(this);
		this.cerrar.addMouseListener(this);

		this.retrocM.setBorder(new EmptyBorder(1, 1, 1, 1));
		this.retrocF.setBorder(new EmptyBorder(1, 1, 1, 1));
		this.retroc.setBorder(new EmptyBorder(1, 1, 1, 1));
		this.aat.setBorder(new EmptyBorder(1, 1, 1, 1));
		this.parar.setBorder(new EmptyBorder(1, 1, 1, 1));
		this.aad.setBorder(new EmptyBorder(1, 1, 1, 1));
		this.avance.setBorder(new EmptyBorder(1, 1, 1, 1));
		this.avanceF.setBorder(new EmptyBorder(1, 1, 1, 1));
		this.avanceM.setBorder(new EmptyBorder(1, 1, 1, 1));
		this.cerrar.setBorder(new EmptyBorder(1, 1, 1, 1));

		this.barraSeg.addChangeListener(this);
		this.barraSeg.setPreferredSize(new Dimension(60, 20));

		this.setToolTipText();

		this.add(this.barraSeg);
		this.add(this.campoSeg);

		JToolBar jtb = new JToolBar();
		jtb.setFloatable(false);

		jtb.add(this.retrocM);
		jtb.add(this.retrocF);
		jtb.add(this.retroc);
		jtb.add(this.aat);
		jtb.add(this.parar);
		jtb.add(this.aad);
		jtb.add(this.avance);
		jtb.add(this.avanceF);
		jtb.add(this.avanceM);
		jtb.add(this.cerrar);

		this.retrocM.setFocusable(false);
		this.retrocF.setFocusable(false);
		this.retroc.setFocusable(false);
		this.aat.setFocusable(false);
		this.parar.setFocusable(false);
		this.aad.setFocusable(false);
		this.avance.setFocusable(false);
		this.avanceF.setFocusable(false);
		this.avanceM.setFocusable(false);
		this.cerrar.setFocusable(false);

		this.add(jtb);
		this.setBorder(new MetalBorders.PaletteBorder());

		this.campoSeg.setText("" + this.seg);
	}
	
	/**
	 * Establece los tooltips para cada uno de los botones.
	 */
	public void setToolTipText() {
		this.retrocM.setToolTipText(Texto.get("PBV_SALTARAT", Conf.idioma));
		this.retrocF.setToolTipText(Texto.get("PBV_INICIO", Conf.idioma));
		this.retroc.setToolTipText(Texto.get("PBV_RETROC", Conf.idioma));
		this.aat.setToolTipText(Texto.get("PBV_ANIMATRAS", Conf.idioma));
		this.parar.setToolTipText(Texto.get("PBV_ANIMPARAR", Conf.idioma));
		this.aad.setToolTipText(Texto.get("PBV_ANIMADEL", Conf.idioma));
		this.avance.setToolTipText(Texto.get("PBV_AVANCE", Conf.idioma));
		this.avanceF.setToolTipText(Texto.get("PBV_FINAL", Conf.idioma));
		this.avanceM.setToolTipText(Texto.get("PBV_SALTARAD", Conf.idioma));
		this.cerrar.setToolTipText(Texto.get("PBV_CERRAR", Conf.idioma));
		this.campoSeg.setToolTipText(Texto.get("PBV_NUMSEG", Conf.idioma));
		this.barraSeg.setToolTipText(Texto.get("PBV_REGULAR05", Conf.idioma));
	}
	
	/**
	 * Habilita y deshabilita los botones correspondientes y actualiza la UI
	 * según el estado del algoritmo.
	 * 
	 * @param pAlgoritmo PanelAlgoritmo asociado.
	 */
	public void setValores(PanelAlgoritmo pAlgoritmo) {
		this.pAlgoritmo = pAlgoritmo;

		this.seg = (float) 1.0;

		if (Ventana.thisventana.traza != null) {
			this.retrocM.habilitar();
			this.retrocF.habilitar();
			this.retroc.habilitar();
			this.aat.habilitar();
			this.parar.habilitar();
			this.aad.habilitar();
			this.avance.habilitar();
			this.avanceF.habilitar();
			this.avanceM.habilitar();
			this.cerrar.habilitar();

			if (Ventana.thisventana.traza.vacioVisible()
					|| Ventana.thisventana.traza.raizInicio()) {
				this.retrocF.deshabilitar();
				this.retroc.deshabilitar();
				this.aat.deshabilitar();
			}
			if (Ventana.thisventana.traza.enteroVisible()) {
				this.avance.deshabilitar();
				this.avanceF.deshabilitar();
				this.aad.deshabilitar();
			}
			if (Ventana.thisventana.traza.nodoActualCompleto()) {
				this.avanceM.deshabilitar();
			} else {
				this.retrocM.deshabilitar();
			}

			this.parar.deshabilitar();
		} else {
			this.retrocM.deshabilitar();
			this.retrocF.deshabilitar();
			this.retroc.deshabilitar();
			this.aat.deshabilitar();
			this.parar.deshabilitar();
			this.aad.deshabilitar();
			this.avance.deshabilitar();
			this.avanceF.deshabilitar();
			this.avanceM.deshabilitar();

			if (Ventana.thisventana.panelOcupado()) {
				this.cerrar.habilitar();
			} else {
				this.cerrar.deshabilitar();
			}
		}
		this.updateUI();

		this.campoSeg.setText("" + this.seg);

		this.setBorder(new MetalBorders.PaletteBorder());
	}

	/**
	 * Comprueba si ya se ha activado alguna animación
	 * 
	 * @return true si ya se ha activado alguna animación
	 */
	public boolean hayAnimacion() {
		return this.hayAnimacion != 0;
	}

	/**
	 * Habilita y deshabilita los botones correspondientes al estado de la ejecución,
	 * únicamente se utiliza al redibujar el árbol. 
	 */
	public void visualizar() {
		this.retrocM.habilitar();
		this.retrocF.habilitar();
		this.retroc.habilitar();
		this.aat.habilitar();
		this.parar.habilitar();
		this.aad.habilitar();
		this.avance.habilitar();
		this.avanceF.habilitar();
		this.avanceM.habilitar();
		this.cerrar.habilitar();

		if (Ventana.thisventana.traza == null
				|| Ventana.thisventana.traza.vacioVisible()
				|| Ventana.thisventana.traza.raizInicio()) {
			this.retrocF.deshabilitar();
			this.retroc.deshabilitar();
			this.aat.deshabilitar();
		}
		if (Ventana.thisventana.traza == null
				|| Ventana.thisventana.traza.enteroVisible()) {
			this.avance.deshabilitar();
			this.avanceF.deshabilitar();
			this.aad.deshabilitar();
		}
		if (Ventana.thisventana.traza == null
				|| Ventana.thisventana.traza.nodoActualCompleto()) {
			this.avanceM.deshabilitar();
		} else {
			this.retrocM.deshabilitar();
		}

		if (this.hayAnimacion == 0) {
			this.parar.deshabilitar();
		}
	}

	/**
	 * Asigna el valor de la variable que dice si hay o no una animación
	 * activada y colorea los botones
	 * 
	 * @param valor
	 *            0=no hay animación, 1=animación hacia adelante, 2=hacia atrás
	 */
	public void setAnimacion(int valor) {
		if (Ventana.thisventana.traza != null) // Si cerramos, puede ser la
												// traza null
		{
			this.hayAnimacion = valor;
			if (valor == 0) {
				this.parar.deshabilitar();
				if (Ventana.thisventana.traza.raizInicio()) {
					this.aad.habilitar();
					this.aat.deshabilitar();
				} else if (Ventana.thisventana.traza.enteroVisible()) {
					this.aad.deshabilitar();
					this.aat.habilitar();
				} else {
					this.aad.habilitar();
					this.aat.habilitar();
				}
			} else if (valor == 1) {
				this.aad.mostrarPulsado();
				this.aat.habilitar();
				this.parar.habilitar();
			} else if (valor == 2) {
				this.aad.habilitar();
				this.aat.mostrarPulsado();
				this.parar.habilitar();
			}
		}
	}

	/**
	 * Sitúa la visualización al principio de la misma
	 */
	public void hacer_inicio() {
		if (Ventana.thisventana.traza == null) {
			return;
		}

		if (!Ventana.thisventana.traza.raizInicio()) {
			Ventana.thisventana.traza.nadaVisible();
			this.actualizar();
		}
		this.avance.habilitar();
		this.avanceF.habilitar();
		this.avanceM.habilitar();
		this.retroc.deshabilitar();
		this.retrocF.deshabilitar();
		this.retrocM.deshabilitar();
		this.aat.deshabilitar();
		this.setAnimacion(0);
	}

	/**
	 * Sitúa la visualización uno o varios pasos hacia atrás
	 */
	public void hacer_retrocMultiple() {
		if (Ventana.thisventana.traza == null) {
			return;
		}

		this.avance.habilitar();
		this.avanceF.habilitar();
		this.avanceM.habilitar();
		this.aad.habilitar();

		try {
			Ventana.thisventana.traza.anteriorMultipleVisible();
			this.actualizar();
		} catch (Exception e) {
			this.hayAnimacion = 0;
			new CuadroError(Ventana.thisventana, Texto.get("ERROR_VISU",
					Conf.idioma), Texto.get("ERROR_VISEXC", Conf.idioma) + ": "
					+ e.getCause());
			if (this.depurar) {
				System.out.println("<PanelBotonesVisualizacionArbol - 1>");
				e.printStackTrace();
			}
			this.pAlgoritmo.cerrar();
		}

		if (Ventana.thisventana.traza.vacioVisible()) {
			this.retroc.deshabilitar();
			this.retrocF.deshabilitar();
			this.setAnimacion(0);
		}
		if (Ventana.thisventana.traza.nodoActualCompleto()) {
			this.avanceM.deshabilitar();
			if (Ventana.thisventana.traza.vacioVisible()) {
				this.retrocM.deshabilitar();
			} else {
				this.retrocM.habilitar();
			}
		} else {
			this.avanceM.habilitar();
			this.retrocM.deshabilitar();
		}
		if (Ventana.thisventana.traza.getRaiz().getEsNodoActual()) {
			this.retroc.deshabilitar();
			this.retrocF.deshabilitar();
			this.retrocM.deshabilitar();
			this.aat.deshabilitar();
		}
	}

	/**
	 * Sitúa la visualización un paso hacia atrás
	 */
	public void hacer_retroc() {
		if (Ventana.thisventana.traza == null) {
			return;
		}

		this.avance.habilitar();
		this.avanceF.habilitar();
		this.avanceM.habilitar();
		this.aad.habilitar();

		try {
			if (!Ventana.thisventana.traza.raizInicio()) {
				Ventana.thisventana.traza.anteriorVisible();
				this.actualizar();
			}
		} catch (Exception e) {
			this.hayAnimacion = 0;
			new CuadroError(Ventana.thisventana, Texto.get("ERROR_VISU",
					Conf.idioma), Texto.get("ERROR_VISEXC", Conf.idioma) + ": "
					+ e.getCause());
			if (this.depurar) {
				System.out.println("<PanelBotonesVisualizacionArbol - 2>");
				e.printStackTrace();
			}
			this.pAlgoritmo.cerrar();
		}

		if (Ventana.thisventana.traza.raizInicio()) {
			// traza.siguienteVisible();
			this.setAnimacion(0);
			this.retroc.deshabilitar();
			this.retrocF.deshabilitar();
			this.retrocM.deshabilitar();
			this.aat.deshabilitar();
		}
		if (Ventana.thisventana.traza.nodoActualCompleto()) {
			this.avanceM.deshabilitar();
			if (Ventana.thisventana.traza.vacioVisible()) {
				this.retrocM.deshabilitar();
			} else {
				this.retrocM.habilitar();
			}
		} else {
			this.avanceM.habilitar();
			this.retrocM.deshabilitar();
		}
		if (Ventana.thisventana.traza.getRaiz().getEsNodoActual()) {
			if (!Ventana.thisventana.traza.getRaiz().esMostradoEntero()) {
				this.retrocM.deshabilitar();
				this.retrocF.deshabilitar();
				this.retroc.deshabilitar();
				this.aat.deshabilitar();
			}
		}
	}

	/**
	 * Sitúa la visualización un paso hacia adelante
	 */
	public void hacer_avance() {
		if (Ventana.thisventana.traza == null) {
			return;
		}

		this.avanceM.habilitar();
		this.retroc.habilitar();
		this.retrocF.habilitar();
		this.aat.habilitar();
		try {
			Ventana.thisventana.traza.siguienteVisible();
			this.actualizar();
		} catch (Exception e) {
			this.hayAnimacion = 0;
			new CuadroError(Ventana.thisventana, Texto.get("ERROR_VISU",
					Conf.idioma), Texto.get("ERROR_VISEXC", Conf.idioma) + ": "
					+ e.getCause());
			if (this.depurar) {
				System.out.println("<PanelBotonesVisualizacionArbol - 3>");
				e.printStackTrace();
			}
			this.pAlgoritmo.cerrar();
			e.printStackTrace();
		}

		if (Ventana.thisventana.traza.enteroVisible()) {
			this.avance.deshabilitar();
			this.avanceF.deshabilitar();
			this.setAnimacion(0);
		}
		if (Ventana.thisventana.traza.nodoActualCompleto()) {
			this.avanceM.deshabilitar();
			this.retrocM.habilitar();
		} else {
			this.avanceM.habilitar();
			this.retrocM.deshabilitar();
		}
		if (!this.habilitado) // Éste es el único método que se ejecuta con
								// habilitado=false, mantenemos colores
		{
			this.retrocM.deshabilitar();
			this.retrocF.deshabilitar();
			this.retroc.deshabilitar();
			this.aat.deshabilitar();
			this.parar.deshabilitar();
			this.aad.deshabilitar();
			this.avance.deshabilitar();
			this.avanceF.deshabilitar();
			this.avanceM.deshabilitar();
			this.cerrar.deshabilitar();
		}
	}

	/**
	 * Sitúa la visualización uno o varios pasos hacia adelante
	 */
	public void hacer_avanceMultiple() {
		if (Ventana.thisventana.traza == null) {
			return;
		}

		this.retroc.habilitar();
		this.retrocF.habilitar();
		this.aat.habilitar();
		try {
			Ventana.thisventana.traza.siguienteMultipleVisible();
			this.actualizar();
		} catch (Exception e) {
			this.hayAnimacion = 0;
			new CuadroError(Ventana.thisventana, Texto.get("ERROR_VISU",
					Conf.idioma), Texto.get("ERROR_VISEXC", Conf.idioma) + ": "
					+ e.getCause());
			if (this.depurar) {
				System.out.println("<PanelBotonesVisualizacionArbol - 4>");
				e.printStackTrace();
			}
			this.pAlgoritmo.cerrar();
		}

		if (Ventana.thisventana.traza.enteroVisible()) {
			this.avance.deshabilitar();
			this.avanceF.deshabilitar();
			this.avanceM.deshabilitar();
			this.setAnimacion(0);
		}
		if (Ventana.thisventana.traza.nodoActualCompleto()) {
			this.avanceM.deshabilitar();
			this.retrocM.habilitar();
		} else {
			this.avanceM.habilitar();
			this.retrocM.deshabilitar();
		}
		if (Ventana.thisventana.traza.getRaiz().getEsNodoActual()) {
			this.avance.deshabilitar();
			this.avanceF.deshabilitar();
			this.avanceM.deshabilitar();
			this.aad.deshabilitar();
		}
	}

	/**
	 * Sitúa la visualización al final
	 */
	public void hacer_final() {
		if (Ventana.thisventana.traza == null) {
			return;
		}

		if (!Ventana.thisventana.traza.enteroVisible()) {
			try {
				Ventana.thisventana.traza.todoVisible();
				this.actualizar();
			} catch (Exception e) {

				this.hayAnimacion = 0;
				new CuadroError(Ventana.thisventana, Texto.get("ERROR_VISU",
						Conf.idioma), Texto.get("ERROR_VISEXC", Conf.idioma)
						+ ": " + e.getCause());
				if (this.depurar) {
					System.out.println("<PanelBotonesVisualizacionArbol - 5>");
					e.printStackTrace();
				}
				this.pAlgoritmo.cerrar();
			}
		}
		this.retrocM.habilitar();
		this.retrocF.habilitar();
		this.retroc.habilitar();
		this.avance.deshabilitar();
		this.avanceF.deshabilitar();
		this.avanceM.deshabilitar();
		this.setAnimacion(0);
	}

	/**
	 * Gestiona la creación de una animación
	 */
	public void hacer_animacion(int sentido) {
		if (Ventana.thisventana.traza == null) {
			return;
		}

		if (this.hayAnimacion == 0) // Si no hay animacion, creamos una
		{
			this.hayAnimacion = sentido; // Debe ir delante de new Animacion,
											// pero después de evaluacion de
											// condición de if
			new Animacion(this); // El nuevo thread leerá de esta clase los
									// valores de sentido y tiempo
		} else // Aprovechamos la que ya hay, actualizándola
		{
			this.hayAnimacion = sentido;
		}
		if (this.hayAnimacion == 1) {
			this.aat.habilitar();
			this.aad.mostrarPulsado();
		} else if (this.hayAnimacion == 2) {
			this.aat.mostrarPulsado();
			this.aad.habilitar();
		}
		this.parar.habilitar();
	}

	/**
	 * Actualiza la interfaz de la visualización
	 */
	public void actualizar() {
		if (Ventana.thisventana.traza == null) {
			return;
		}
		this.pAlgoritmo.actualizar();
	}

	/**
	 * Devuelve el número de segundos que hay configurado entre cada paso de la
	 * animación
	 * 
	 * @return número de segundos entre cada paso de la animación
	 */
	public float getSeg() {
		if (this.valorValidoFloat()) {
			this.seg = Float.parseFloat(this.campoSeg.getText());
		}
		if (this.seg == 0) {
			this.seg = (float) 0.15;
		}
		return this.seg;
	}

	/**
	 * Devuelve el sentido de la animación que hay configurado
	 * 
	 * @return true si la animación es hacia adelante
	 */
	public boolean getSentido() {
		if (this.hayAnimacion == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Método auxiliar que determina si el valor escrito en el cuadro de texto
	 * es válido
	 * 
	 * @return true si la animación es hacia adelante
	 */
	private boolean valorValidoFloat() {
		String s = null;

		try {
			s = this.campoSeg.getText().replace(" ", "");
		} catch (java.lang.NullPointerException npe) {
		}

		if (s == null || s.length() == 0) {
			return false; // No hay número escrito
		}

		for (int i = 0; i < s.length(); i++) {
			if ((s.charAt(i) < '0' || s.charAt(i) > '9') && s.charAt(i) != '.') {
				return false; // Hay caracteres no permitidos
			}
		}

		int x = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '.') {
				x++;
			}
		}
		if (x > 1) {
			return false; // Hay más de un punto
		}

		return true;
	}

	/**
	 * Método que gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Método que gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getComponent() instanceof BotonImagen) {
			BotonImagen b = (BotonImagen) e.getComponent();
			b.ratonEstaSobreBoton();
		}
	}

	/**
	 * Método que gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getComponent() instanceof BotonImagen) {
			BotonImagen b = (BotonImagen) e.getComponent();
			b.ratonNoEstaSobreBoton();
		}
	}

	/**
	 * Método que gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (Ventana.thisventana.traza == null && e.getSource() != this.cerrar) {
			return;
		}

		if (e.getSource() instanceof BotonImagen) {
			BotonImagen b = (BotonImagen) e.getSource();
			if (this.habilitado && b.isEnabled()) {
				if (e.getSource() == this.retrocM) {
					if (Conf.fichero_log) {
						Logger.log_write("Visualización: [ <- ]");
					}
					this.hacer_retrocMultiple();
				} else if (e.getSource() == this.retrocF) {
					if (Conf.fichero_log) {
						Logger.log_write("Visualización: [ << ]");
					}
					this.hacer_inicio();
				} else if (e.getSource() == this.retroc) {
					if (Conf.fichero_log) {
						Logger.log_write("Visualización: [ |< ]");
					}
					this.hacer_retroc();
				} else if (e.getSource() == this.aat) {
					if (Conf.fichero_log) {
						Logger.log_write("Visualización: [ < ]");
					}
					this.hacer_animacion(2);
				} else if (e.getSource() == this.parar) {
					if (Conf.fichero_log) {
						Logger.log_write("Visualización: [ || ]");
					}
					this.setAnimacion(0);
				} else if (e.getSource() == this.aad) {
					if (Conf.fichero_log) {
						Logger.log_write("Visualización: [ > ]");
					}
					this.hacer_animacion(1);
				} else if (e.getSource() == this.avance) {
					if (Conf.fichero_log) {
						Logger.log_write("Visualización: [ >| ]");
					}
					this.hacer_avance();
				} else if (e.getSource() == this.avanceF) {
					if (Conf.fichero_log) {
						Logger.log_write("Visualización: [ >> ]");
					}
					this.hacer_final();
				} else if (e.getSource() == this.avanceM) {
					if (Conf.fichero_log) {
						Logger.log_write("Visualización: [ -> ]");
					}
					this.hacer_avanceMultiple();
				} else if (e.getSource() == this.cerrar) {
					if (Conf.fichero_log) {
						Logger.log_write("Visualización: [ X ]");
					}
					this.setAnimacion(0);
					this.pAlgoritmo.cerrar();
					Ventana.thisventana.cerrarVentana();
				}
			}
		}
	}

	/**
	 * Método que gestiona los eventos de ratón
	 * 
	 * @param e
	 *            evento de ratón
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Método que gestiona los eventos de estado
	 * 
	 * @param e
	 *            evento de estado
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (Conf.fichero_log) {
			Logger.log_write("Barra velocidad animación: [ "
					+ this.barraSeg.getValue() + " ]");
		}
		this.campoSeg.setText("" + this.barraSeg.getValue() + ".0");
	}
	
	/**
	 * Deshabilita todos los botones y controles de visualización.
	 */
	protected void deshabilitarControles() {
		this.habilitado = false;

		this.retrocM.deshabilitar();
		this.retrocF.deshabilitar();
		this.retroc.deshabilitar();
		this.aat.deshabilitar();
		this.parar.deshabilitar();
		this.aad.deshabilitar();
		this.avance.deshabilitar();
		this.avanceF.deshabilitar();
		this.avanceM.deshabilitar();
		this.cerrar.deshabilitar();

		this.campoSeg.setEnabled(false);
		this.barraSeg.setEnabled(false);
	}
	
	/**
	 * Habilita los botones y controles de visualización según
	 * el estado actual.
	 */
	protected void habilitarControles() {
		this.habilitado = true;

		this.campoSeg.setEnabled(true);
		this.barraSeg.setEnabled(true);

		if (Ventana.thisventana.traza.raizInicio()) {
			this.retrocF.deshabilitar();
			this.retroc.deshabilitar();
			this.aat.deshabilitar();
		} else {
			this.retrocM.habilitar();
			this.retrocF.habilitar();
			this.retroc.habilitar();
			this.aat.habilitar();
		}

		this.parar.deshabilitar();

		if (Ventana.thisventana.traza.enteroVisible()) {
			this.aad.deshabilitar();
			this.avance.deshabilitar();
			this.avanceF.deshabilitar();
		} else {
			this.aad.habilitar();
			this.avance.habilitar();
			this.avanceF.habilitar();
		}

		if (Ventana.thisventana.traza.nodoActualCompleto()) {
			this.avanceM.deshabilitar();
			this.retrocM.habilitar();
		} else {
			this.avanceM.habilitar();
			this.retrocM.deshabilitar();
		}

		this.cerrar.habilitar();
	}
}
