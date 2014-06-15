/**
	CAMBIAR TODOS LOS COMENTARIOS DE ESTA CLASE
	******************************************************
		
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import conf.*;
import datos.MetodoAlgoritmo;
import botones.*;
import paneles.*;
import opciones.*;
import utilidades.*;
import ventanas.*;

public class CuadroZoom extends Thread implements ActionListener, KeyListener, MouseListener, ChangeListener
{
	static final long serialVersionUID=07;
	private final boolean depurar=false;

	
	public static final int MAS5 = 5;
	public static final int MENOS5 = 15;
	public static final int AJUSTE = 25;
	
	
	final int tamBotones=20;
	
	static PanelVentana panelVentana;
	
	JDialog dialogo;

	static int zoomArbol;
	static int zoomPila;
	static int zoomArbolEstr;
	static int zoomCrono;
	static int zoomEstructura;

	static int dimensionesPaneles[];
	static int dimensionesGrafos[];
	
	static JLabel etiqTitulos[];
	static JLabel etiqPorcentaje[];
	static JSlider barras[];
	static JTextField cuadros[];
	Boton zoommas[];
	Boton zoommenos[];
	Boton zoomajuste[];
	
	BotonAceptar aceptar;
	BotonCancelar cancelar;
	Boton inicializar;
	
	static OpcionConfVisualizacion ocv=null;
	static GestorOpciones gOpciones=new GestorOpciones();

	static int []dimPaneles;	// [0][1]=panelEstructura		[2][3]=panelPrincipal
	static int []dimGrafos;	// [0][1]=grafosEstructura		[2][3]=grafoPrincipal
	
	boolean escribiendo=false;

	/**
		Constructor: genera un nuevo cuadro de opción
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroZoom (PanelVentana pV)
	{	

		dialogo=new JDialog(Ventana.thisventana,true);
		inicializar(pV);
		

		if (depurar)
		{
			int []zooms=panelVentana.getZooms();
			for (int i=0; i<zooms.length; i++)
				System.out.println("zooms "+i+": "+zooms[i]);
					
			for (int i=0; i<dimensionesPaneles.length; i++)
				System.out.println("dimP "+i+": "+dimensionesPaneles[i]);		
					
			for (int i=0; i<dimensionesGrafos.length; i++)
				System.out.println("dimG "+i+": "+dimensionesGrafos[i]);	
		}
		this.start();

	}

	public static void inicializar(PanelVentana pV)
	{
		panelVentana=pV;
		zoomArbol		=pV.getZooms()[0];
		zoomPila		=pV.getZooms()[1];
		//zoomTraza		=pV.getZooms()[2];
		zoomCrono		=pV.getZooms()[2];
		zoomEstructura	=pV.getZooms()[3];
		
		dimensionesPaneles=pV.getTamanoPaneles();
		dimensionesGrafos=pV.getTamanoGrafos();
				
		ocv = (OpcionConfVisualizacion)gOpciones.getOpcion("OpcionConfVisualizacion",false);
	}
	
	
	/**
		Genera una nueva opción
	*/
	
	public void run()
	{
		int numVistas=5;	// Vista de traza no se maneja desde aquí, pero dejamos hueco en estructuras
		
		int numVistasVisibles=(Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV,Ventana.thisventana.traza.getTecnicas()) ? 5 : 3);
		
		etiqTitulos=new JLabel[numVistas];
		etiqPorcentaje=new JLabel[numVistas];
		barras=new JSlider[numVistas];
		cuadros=new JTextField[numVistas];
		zoommenos=new Boton[numVistas];
		zoommas=new Boton[numVistas];
		zoomajuste=new Boton[numVistas];
		
		// Panel superior
		JPanel panelSuperior=new JPanel();
		
		GridLayout gl=new GridLayout(numVistasVisibles-1 ,1);	// quitamos una porque la traza no la manejamos aquí
		panelSuperior.setLayout(gl);
		panelSuperior.setBorder(new TitledBorder(Texto.get("CZ_AJUSTES",Conf.idioma)));
		
		JPanel panelesZoom[]=new JPanel[numVistasVisibles];

		panelesZoom[0]=panelZoom(Texto.get("V_ARBOL",Conf.idioma),zoomArbol,0);
		panelSuperior.add(panelesZoom[0]);
		
		panelesZoom[1]=panelZoom(Texto.get("V_PILA",Conf.idioma),zoomPila,1);
		panelSuperior.add(panelesZoom[1]);		


		if (numVistasVisibles>3)
		{
		
			/*panelesZoom[2]=panelZoom(Texto.get("V_ARBOL_E",Conf.idioma),zoomArbolEstr,2);
			panelSuperior.add(panelesZoom[2]);	*/
			
			panelesZoom[3]=panelZoom(Texto.get("V_CRONO",Conf.idioma),zoomCrono,3);
			panelSuperior.add(panelesZoom[3]);	
			
			panelesZoom[4]=panelZoom(Texto.get("V_ESTRUC",Conf.idioma),zoomEstructura,4);
			panelSuperior.add(panelesZoom[4]);	
		}	
		
		// Panel botón
		JPanel panelBoton =new JPanel();
		aceptar = new BotonAceptar();
		cancelar = new BotonCancelar();
		inicializar = new Boton(Texto.get("BOTONINIC",Conf.idioma));
		aceptar.addMouseListener(this);
		cancelar.addMouseListener(this);
		inicializar.addMouseListener(this);
		panelBoton.add(aceptar);
		panelBoton.add(cancelar);
		panelBoton.add(inicializar);
		
		// Panel general
		JPanel panel= new JPanel();
		BorderLayout bl=new BorderLayout();
		panel.setLayout(bl);
		
		panel.add(panelSuperior, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);

			
		// Preparamos y mostramos cuadro
		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("CZ_CONFZOOM",Conf.idioma));

		if (Ventana.thisventana.msWindows)
			dialogo.setSize(465,50+(50*numVistasVisibles-1));// quitamos una porque la traza no la manejamos aquí
		else
			dialogo.setSize(465,114+(36*numVistasVisibles-1));// quitamos una porque la traza no la manejamos aquí

		int coord[]=Conf.ubicarCentro((int)dialogo.getSize().getWidth(),(int)dialogo.getSize().getHeight());
		dialogo.setLocation(coord[0],coord[1]);
		
		dialogo.setResizable(false);
		
		setValores();		
		dialogo.setVisible(true);
	}

	
	
	
	
	public JPanel panelZoom(String titulo, int valor, int indice)
	{
		JPanel panel=new JPanel();
		BorderLayout bl=new BorderLayout();
		panel.setLayout(bl);
		
		etiqTitulos[indice]=new JLabel(titulo);
		etiqTitulos[indice].setToolTipText(Texto.get("CZ_TITVIS",Conf.idioma));
		etiqTitulos[indice].setPreferredSize(new Dimension(400,16));
		
		etiqPorcentaje[indice]=new JLabel(valor+"%");
		etiqPorcentaje[indice].setToolTipText(Texto.get("CZ_NIVZOOM",Conf.idioma));
		
		barras[indice]=new JSlider(-100,100,valor);
		barras[indice].addChangeListener(this);
		barras[indice].addMouseListener(this);
		barras[indice].addKeyListener(this);
		barras[indice].setToolTipText(Texto.get("CZ_REG100",Conf.idioma));
		
		JLabel leyenda=new JLabel ("(-100%..100%)");
		
		cuadros[indice]=new JTextField(5);
		cuadros[indice].addKeyListener(this);
		cuadros[indice].setHorizontalAlignment(JTextField.TRAILING  );
		cuadros[indice].setToolTipText(Texto.get("CZ_REG100",Conf.idioma));
		
		zoommas[indice]=new Boton( new ImageIcon("./imagenes/icono_zoommas.gif"),tamBotones,tamBotones);
		zoommas[indice].addActionListener(this);
		zoommas[indice].setToolTipText(Texto.get("CZ_MAS5",Conf.idioma));
		
		zoommenos[indice]=new Boton( new ImageIcon("./imagenes/icono_zoommenos.gif"),tamBotones,tamBotones );
		zoommenos[indice].addActionListener(this);
		zoommenos[indice].setToolTipText(Texto.get("CZ_MENS5",Conf.idioma));
		
		zoomajuste[indice]=new Boton( new ImageIcon("./imagenes/icono_zoomajuste.gif"),tamBotones,tamBotones );
		zoomajuste[indice].addActionListener(this);
		zoomajuste[indice].setToolTipText(Texto.get("CZ_AJUST",Conf.idioma));
		
		JPanel panelBarra = new JPanel();
		panelBarra.add(barras[indice]);
		
		JPanel panelCuadroyBotones=new JPanel();
		panelCuadroyBotones.add(leyenda);
		panelCuadroyBotones.add(cuadros[indice]);
		panelCuadroyBotones.add(zoommas[indice]);
		panelCuadroyBotones.add(zoommenos[indice]);
		panelCuadroyBotones.add(zoomajuste[indice]);
		
		JPanel panelSur = new JPanel();
		panelSur.setLayout(new BorderLayout());
		panelSur.add(panelBarra,BorderLayout.WEST);
		panelSur.add(panelCuadroyBotones,BorderLayout.EAST);
		
		panel.add(etiqTitulos[indice],BorderLayout.WEST);
		panel.add(panelSur,BorderLayout.SOUTH);
		
		
		return panel;
	}
	
	
	/**
		Asigna valores a los elementos del cuadro
	
		@param tipo ayuda a diferenciar entre rellenar los campos para la entrada y la salida
	*/
	private void setValores()
	{
		for (int i=0; i<cuadros.length; i++)
		{
			if (cuadros[i]!=null && barras[i]!=null)
				cuadros[i].setText(""+barras[i].getValue());
			/*else
			{
				if (cuadros[i]==null)	System.out.print("cuadros["+i+"]=null");
				if (barras[i]==null)	System.out.print(" barras["+i+"]=null");
			}*/
		}
			
	}

	

	
	

	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().getClass().getCanonicalName().contains("Boton"))
		{
			for (int i=0; i<zoommas.length; i++)
				if (zoommas[i]!=null && zoommas[i]==e.getSource())
				{
					if (Conf.fichero_log) Logger.log_write("Cuadro de Zoom: zoom+ ("+i+")");
					zoomMas5(i);
				}

			for (int i=0; i<zoommenos.length; i++)
				if (zoommenos[i]!=null && zoommenos[i]==e.getSource())
				{
					if (Conf.fichero_log) Logger.log_write("Cuadro de Zoom: zoom- ("+i+")");	
					zoomMenos5(i);
				}

			for (int i=0; i<zoomajuste.length; i++)
				if (zoomajuste[i]!=null && zoomajuste[i]==e.getSource())
				{
					if (Conf.fichero_log) Logger.log_write("Cuadro de Zoom: zoomAjuste ("+i+")");
					zoomAjuste(i);
				}

		}
	}
	
	public void setValor(int valor,int indice)
	{
		barras[indice].setValue(valor);
		cuadros[indice].setText(""+valor);
		if (Conf.fichero_log) Logger.log_write("Cuadro de Zoom: variación a "+valor+" ("+indice+")");
	}
	
	
	
	public static void zoomAjuste(PanelVentana pV,int i,int tipoVariacion)
	{
		inicializar(pV);
		barras=new JSlider[5 ];

		if (i==0)
			barras[i]=new JSlider(-100,100,zoomArbol);
		else if (i==1)
			barras[i]=new JSlider(-100,100,zoomPila);
		/*else if (i==2)
			barras[i]=new JSlider(-100,100,zoomArbolEstr);*/
		else if (i==3)
			barras[i]=new JSlider(-100,100,zoomCrono);
		else if (i==4)
			barras[i]=new JSlider(-100,100,zoomEstructura);

		
		//System.out.println("barras[0] = "+zoomPila);
		//System.out.println("barras[1] = "+zoomArbol);
		//System.out.println("barras[2] = "+zoomTraza);	//NULO
		//System.out.println("barras[2] = "+zoomCrono);
		//System.out.println("barras[3] = "+zoomEstructura);
		
		
		if (tipoVariacion==CuadroZoom.MAS5)
			zoomMas5(i);
		else if (tipoVariacion==CuadroZoom.MENOS5)
			zoomMenos5(i);
		else if (tipoVariacion==CuadroZoom.AJUSTE)
			zoomAjuste(i);
	}

	
	private static void zoomMas5 (int i)
	{
		barras[i].setValue(barras[i].getValue()+5);
		Conf.setPanelArbolReajustado(true);
		//System.out.println(" variable +  = "+Conf.getPanelArbolReajustado());
		peticionRefrescar(i);
	}
	
	private static void zoomMenos5 (int i)
	{
		barras[i].setValue(barras[i].getValue()-5);
		Conf.setPanelArbolReajustado(true);
		//System.out.println(" variable -  = "+Conf.getPanelArbolReajustado());
		peticionRefrescar(i);
	}
	
	
/**
		Ajusta el zoom al tamaño del panel
		
		@param i indica la posición que se desea actualizar
	*/
	private static void zoomAjuste(int i)
	{
		dimPaneles=Ventana.thisventana.dimensionesPanelesVisualizacion();
		dimGrafos =Ventana.thisventana.dimensionesGrafosVisiblesVisualizacion();	// Dimensiones de los nodos que se ven
		//dimGrafos =VentanaVisualizador.thisventana.dimensionesGrafosVisualizacion();			// Dimensiones de todos los nodos
		
		if (i==0)	// Arbol
		{
			Conf.setHaciendoAjuste(true);
			
			double propAncho=(double)dimPaneles[2] / (double)dimGrafos[2];
			double propAlto=(double)dimPaneles[3] / (double)dimGrafos[3];	
			int valorNuevoAncho, valorNuevoAlto;
			
			if (dimGrafos[2]>dimPaneles[2] || dimGrafos[3]>dimPaneles[3])	// Si hay que reducir tamaño...
			{
				valorNuevoAncho= ((int)(propAncho*100))-101;
				valorNuevoAlto= ((int)(propAlto*100))-100;
			}
			else															// Si hay que ampliar tamaño...
			{
				valorNuevoAncho=(int)((propAncho-1)*100)-2;
				valorNuevoAlto=(int)((propAlto-1)*100)-2;
			}		
			barras[i].setValue(Math.min(valorNuevoAncho,valorNuevoAlto));
			Conf.setPanelArbolReajustado(true);
			peticionRefrescar(i);
		}
		else if (i==1)	// Pila
		{
			double propAncho=(double)dimPaneles[0] / (double)dimGrafos[0];
			double propAlto=(double)dimPaneles[1] / (double)dimGrafos[1];
			double porc=Math.min(propAncho,propAlto);
			int valorNuevo=0;
			
			if (dimGrafos[0]>dimPaneles[0] || dimGrafos[1]>dimPaneles[1])	// Si hay que reducir tamaño...
				valorNuevo= ((int)(porc*100))-100;
			else															// Si hay que ampliar tamaño...
				valorNuevo=(int)((porc-1)*100)-2;

			barras[i].setValue(valorNuevo);
			peticionRefrescar(i);
		}
		/*else if (i==2)	// Traza
		{
		
		
		
		}*/
		else if (i==3)	// Crono
		{
			Conf.setHaciendoAjuste(true);
			
			double propAncho=(double)dimPaneles[4] / (double)dimGrafos[4];
			double propAlto=(double)dimPaneles[5] / (double)dimGrafos[5];	
			int valorNuevoAncho, valorNuevoAlto;
			
			if (dimGrafos[4]>dimPaneles[4] || dimGrafos[5]>dimPaneles[5])	// Si hay que reducir tamaño...
			{
				valorNuevoAncho= ((int)(propAncho*100))-101;
				valorNuevoAlto= ((int)(propAlto*100))-100;
			}
			else															// Si hay que ampliar tamaño...
			{
				valorNuevoAncho=(int)((propAncho-1)*100)-2;
				valorNuevoAlto=(int)((propAlto-1)*100)-2;
			}
			
			barras[i].setValue(Math.min(valorNuevoAncho,valorNuevoAlto));
			Conf.setPanelArbolReajustado(true);
			peticionRefrescar(i);
		}
		else if (i==4)	// Estructura
		{
			Conf.setHaciendoAjuste(true);
			
			double propAncho=(double)dimPaneles[6] / (double)dimGrafos[6];
			double propAlto=(double)dimPaneles[7] / (double)dimGrafos[7];	
			int valorNuevoAncho, valorNuevoAlto;
			
			if (dimGrafos[6]>dimPaneles[6] || dimGrafos[7]>dimPaneles[7])	// Si hay que reducir tamaño...
			{
				valorNuevoAncho= ((int)(propAncho*100))-101;
				valorNuevoAlto= ((int)(propAlto*100))-100;
			}
			else															// Si hay que ampliar tamaño...
			{
				valorNuevoAncho=(int)((propAncho-1)*100)-2;
				valorNuevoAlto=(int)((propAlto-1)*100)-2;
			}
			
			barras[i].setValue(Math.min(valorNuevoAncho,valorNuevoAlto));
			Conf.setPanelArbolReajustado(true);
			peticionRefrescar(i);
		}
	}
	
	
	
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/		
	public void keyPressed(KeyEvent e)
	{
		if (e.getSource().getClass().getCanonicalName().contains("JSlider"))
		{
			for (int i=0; i<barras.length; i++)
				if (barras[i]!=null && barras[i]==e.getSource())
					cuadros[i].setText(""+barras[i].getValue());
		}
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyReleased(KeyEvent e)
	{
		int code=e.getKeyCode();
		
		if (code==KeyEvent.VK_ESCAPE || code==KeyEvent.VK_ENTER)
			dialogo.setVisible(false);
		else if (e.getSource().getClass().getCanonicalName().contains("JSlider"))
			for (int i=0; i<barras.length; i++)
				if (barras[i]!=null && barras[i]==e.getSource())
					peticionRefrescar(i);
	}

	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyTyped(KeyEvent ke)
	{
		final KeyEvent e=ke;

		if (ke.getSource().getClass().getCanonicalName().contains("JTextField"))
		{
			new Thread(){
				public void run()
				{
					for (int i=0; i<cuadros.length; i++)
					{
						if (cuadros[i]!=null && cuadros[i]==e.getSource())
						{
							try {
								escribiendo=true;
								barras[i].setValue(Integer.parseInt(cuadros[i].getText()));
								
								peticionRefrescar(i);
							} catch (Exception exc) {
							}
						}
					}
				}
			}.start();
		}
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseClicked(MouseEvent e) 
	{
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseEntered(MouseEvent e) 
	{
	}

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/			
	public void mouseExited(MouseEvent e) 
	{
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mousePressed(MouseEvent e) 
	{
		if (e.getSource()==aceptar)
			dialogo.setVisible(false);
		else if (e.getSource()==inicializar)
			for (int i=0; i<barras.length; i++)
			{
				if (barras[i]!=null)
				{
					barras[i].setValue(0);
					etiqPorcentaje[i].setText("0%");
					peticionRefrescar(i);
				}
			}
	}
	
	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseReleased(MouseEvent e)
	{
		if (e.getSource().getClass().getName().contains("JSlider"))
			for (int i=0; i<barras.length; i++)
				if (barras[i]!=null && barras[i]==e.getSource())
					peticionRefrescar(i);
		if (e.getSource()==cancelar)
		{
			dialogo.setVisible(false);
		}
	}
	
	private static void peticionRefrescar(int i)
	{
		ocv = (OpcionConfVisualizacion)gOpciones.getOpcion("OpcionConfVisualizacion",false);
		
		int valor=barras[i].getValue();
		if (valor<(-99))
			valor=-99;

		if (i==0)	// Si es barra de árbol
		{
			Ventana.thisventana.actualizarZoomArbol(valor);
			ocv.setZoomArbol( valor );
		}
		else if (i==1) // Si es barra de pila
		{
			Ventana.thisventana.actualizarZoomPila(valor);
			ocv.setZoomPila( valor );
		}
		/*else if (i==2)	// Si es barra de árbol con estr
		{
			VentanaVisualizador.thisventana.actualizarZoom(i,valor);
			ocv.setZoomArbolEstr( valor );
		}		*/	
		else if (i==3)	// Si es barra de crono
		{
			Ventana.thisventana.actualizarZoom(i,valor);
			ocv.setZoomCrono( valor );
		}			
		else if (i==4)	// Si es barra de estructura
		{
			Ventana.thisventana.actualizarZoom(i,valor);
			ocv.setZoomEstructura( valor );
		}			
		gOpciones.setOpcion(ocv,1);
		Conf.setValoresVisualizacion();
	}
	
	
	/**
		Gestiona los eventos de cambio de estado de la barra deslizante
	*/
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource().getClass().getName().contains("JSlider"))
		{
			for (int i=0; i<barras.length; i++)
				if (barras[i]!=null && barras[i]==e.getSource())
				{
					if (!escribiendo)
					{
						final int iF=i;
						new Thread(){
						
							public void run()
							{
								if (cuadros[iF]!=null)
									cuadros[iF].setText(barras[iF].getValue()+"");
							}
						}.start();
					}
					else
						escribiendo=false;
				}
		}		
	}
}