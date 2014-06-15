
package opciones;



import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.IOException;
import org.w3c.dom.*;

import conf.Conf;


import utilidades.*;

public class OpcionConfVisualizacion extends Opcion
{
	private static final long serialVersionUID = 1006;

	// Colores de fuente
	int colorFEntradaR = 255;		// Valor R para fuente en celdas de entrada
	int colorFEntradaG = 255;		// Valor G para fuente en celdas de entrada
	int colorFEntradaB = 255;		// Valor B para fuente en celdas de entrada
	
	int colorFSalidaR = 255;		// Valor R para fuente en celdas de salida
	int colorFSalidaG = 255;		// Valor G para fuente en celdas de salida
	int colorFSalidaB = 255;		// Valor B para fuente en celdas de salida
	
	// Colores de celda 
	int colorC1EntradaR = 100;	// Valor R para color en celdas de entrada
	int colorC1EntradaG = 100;	// Valor G para color en celdas de entrada
	int colorC1EntradaB = 240;	// Valor B para color en celdas de entrada
	
	int colorC1SalidaR = 240;	// Valor R para color en celdas de salida
	int colorC1SalidaG = 100;	// Valor G para color en celdas de salida
	int colorC1SalidaB = 100;	// Valor B para color en celdas de salida
	
	// Colores de celda atenuada
	int colorCAEntradaR = 0;	// Valor R para color en celdas de entrada
	int colorCAEntradaG = 0;	// Valor G para color en celdas de entrada
	int colorCAEntradaB = 140;	// Valor B para color en celdas de entrada
	
	int colorCASalidaR = 140;	// Valor R para color en celdas de salida
	int colorCASalidaG = 0;	// Valor G para color en celdas de salida
	int colorCASalidaB = 0;	// Valor B para color en celdas de salida
	
	// Colores no calculados salida
	int colorC1NCSalidaR = 255;	// Valor R para color en celdas de entrada
	int colorC1NCSalidaG = 100;	// Valor G para color en celdas de entrada
	int colorC1NCSalidaB = 100;	// Valor B para color en celdas de entrada
	
	// Color flecha
	int colorFlechaR = 100;		// Valor R para color de flecha
	int colorFlechaG = 100;		// Valor G para color de flecha
	int colorFlechaB = 100;		// Valor B para color de flecha
	
	// Color panel
	int colorPanelR = 255;		// Valor R para color del panel
	int colorPanelG = 248;		// Valor G para color del panel
	int colorPanelB = 241;		// Valor B para color del panel
	
	// Color marco nodo actual
	int colorActualR = 10;		// Valor R para color marco nodo actual
	int colorActualG = 200;		// Valor G para color marco nodo actual
	int colorActualB = 10;		// Valor B para color marco nodo actual
	
	// Color marcos camino actual
	int colorCActualR = 235;	// Valor R para color de marcos camino actual
	int colorCActualG = 215;	// Valor G para color de marcos camino actual
	int colorCActualB = 0;		// Valor B para color de marcos camino actual
	
	// Color código: palabras reservadas
	int colorCodigoPRR = 210;		// Valor R para color de código: palabras reservadas
	int colorCodigoPRG = 0;		// Valor G para color de código: palabras reservadas
	int colorCodigoPRB = 0;		// Valor B para color de código: palabras reservadas
	
	// Color código: comentarios
	int colorCodigoCoR = 0;			// Valor R para color de código: comentarios
	int colorCodigoCoG = 180;		// Valor G para color de código: comentarios
	int colorCodigoCoB = 0;			// Valor B para color de código: comentarios
	
	// Color código: nombre método foreground
	int colorCodigoMFR = 0;		// Valor R para color de código: nombre método foreground
	int colorCodigoMFG = 0;		// Valor G para color de código: nombre método foreground
	int colorCodigoMFB = 220;		// Valor B para color de código: nombre método foreground
	
	// Color código: nombre método background
	int colorCodigoMBR = 210;		// Valor R para color de código: nombre método background
	int colorCodigoMBG = 210;		// Valor G para color de código: nombre método background
	int colorCodigoMBB = 255;		// Valor B para color de código: nombre método background
	
	// Color código: resto codigo
	int colorCodigoRCR = 0;			// Valor R para color de código: resto codigo
	int colorCodigoRCG = 0;			// Valor G para color de código: resto codigo
	int colorCodigoRCB = 0;			// Valor B para color de código: resto codigo
	
	
	// Color nodos buscados (iluminados)
	int colorIluminadoR = 200;			// Valor R para color nodos buscados (iluminados)
	int colorIluminadoG = 100;			// Valor G para color nodos buscados (iluminados)
	int colorIluminadoB = 0;			// Valor B para color nodos buscados (iluminados)
	
	// Color nodos resaltados
	int colorResaltadoR = 204;			// Valor R para color nodos buscados (iluminados)
	int colorResaltadoG = 204;			// Valor G para color nodos buscados (iluminados)
	int colorResaltadoB = 0;			// Valor B para color nodos buscados (iluminados)
	
	
	// Colores para el modo 2
	
	int colorModo2_R[] = new int[Conf.numColoresMetodos];		// Valor R para color de traza: fondo panel
	int colorModo2_G[] = new int[Conf.numColoresMetodos];		// Valor G para color de traza: fondo panel
	int colorModo2_B[] = new int[Conf.numColoresMetodos];		// Valor B para color de traza: fondo panel
	
	
	int modoColor=1;
	
	
	
	// Otras opciones sobre colores
	boolean colorDegradadoModo1=false; 	// false=se usan dos colores para la celda (degradado)
	boolean colorDegradadoModo2=false; 	// false=se usan dos colores para la celda (degradado)

	
	
	// Otras características
	int grosorFlecha = 2;		// Valor para grosor de flechas
	int grosorActual = 4;		// Valor para grosor de marco de nodo actual
	
	int formaFlecha = 0;		// Valor para el tipo de flecha que se mostrará
	
	int distanciaH = 30;		// Distancia horizontal entre celdas
	int distanciaV = 30;		// Distancia vertical entre celdas
	
	int tipoBordeCelda= 0;

	// Fuentes (vistas de código y traza)
	String fuenteCodigo="Courier New Negrita";
	String fuenteTraza="Dialog.bold";
	
	int tamFuenteCodigo=12;
	int tamFuenteTraza=12;
	
	
	// Zoom por defecto
	int zoomArbol=-35;
	int zoomPila=-20;
	

	int zoomCrono=-20;
	int zoomEstructura=-20;
	

	
	public OpcionConfVisualizacion()
	{
		this(null);
		
		colorModo2_R=Conf.getRojoDefecto();
		colorModo2_G=Conf.getVerdeDefecto();
		colorModo2_B=Conf.getAzulDefecto();
	}

	public OpcionConfVisualizacion(String dir)
	{
		super("OpcionConfVisualizacion");
	}

	// Métodos set
	public void setColorFEntrada(int r, int g, int b)
	{
		this.colorFEntradaR=r;
		this.colorFEntradaG=g;
		this.colorFEntradaB=b;
	}
	
	public void setColorFSalida(int r, int g, int b)
	{
		this.colorFSalidaR=r;
		this.colorFSalidaG=g;
		this.colorFSalidaB=b;
	}
	
	public void setColorC1Entrada(int r, int g, int b)
	{
		this.colorC1EntradaR=r;
		this.colorC1EntradaG=g;
		this.colorC1EntradaB=b;
	}
	
	public void setColorC1Salida(int r, int g, int b)
	{
		this.colorC1SalidaR=r;
		this.colorC1SalidaG=g;
		this.colorC1SalidaB=b;
	}
	
	public void setColorCAEntrada(int r, int g, int b)
	{
		this.colorCAEntradaR=r;
		this.colorCAEntradaG=g;
		this.colorCAEntradaB=b;
	}
	
	public void setColorCASalida(int r, int g, int b)
	{
		this.colorCASalidaR=r;
		this.colorCASalidaG=g;
		this.colorCASalidaB=b;
	}
	
	public void setColorC1NCSalida(int r, int g, int b)
	{
		this.colorC1NCSalidaR=r;
		this.colorC1NCSalidaG=g;
		this.colorC1NCSalidaB=b;
	}
	
	public void setColorDegradadoModo1(boolean b)
	{
		this.colorDegradadoModo1=b;
	}
	
	public void setColorDegradadoModo2(boolean b)
	{
		this.colorDegradadoModo2=b;
	}
	
	
	
	
	
	
	public void setColorFlecha(int r, int g, int b)
	{
		this.colorFlechaR=r;
		this.colorFlechaG=g;
		this.colorFlechaB=b;
	}
	
	public void setGrosorFlecha(int grosor)
	{
		this.grosorFlecha=grosor;
	}
	
	public void setColorPanel(int r, int g, int b)
	{
		this.colorPanelR=r;
		this.colorPanelG=g;
		this.colorPanelB=b;
	}	
	
	public void setColorActual(int r, int g, int b)
	{
		this.colorActualR=r;
		this.colorActualG=g;
		this.colorActualB=b;
	}	
	
	public void setColorCActual(int r, int g, int b)
	{
		this.colorCActualR=r;
		this.colorCActualG=g;
		this.colorCActualB=b;
	}	
	
	public void setFormaFlecha(int x)
	{
		this.formaFlecha=x;
	}
	
	public void setGrosorActual(int x)
	{
		this.grosorActual=x;
	}
	
	public void setDistanciaH(int x)
	{
		this.distanciaH=x;
	}
	
	public void setDistanciaV(int x)
	{
		this.distanciaV=x;
	}
	
	public void setTipoBordeCelda(int x)
	{
		this.tipoBordeCelda=x;
	}
	
	public void setColorCodigoPR(int r, int g, int b)
	{
		this.colorCodigoPRR=r;
		this.colorCodigoPRG=g;
		this.colorCodigoPRB=b;
	}

	public void setColorCodigoCo(int r, int g, int b)
	{
		this.colorCodigoCoR=r;
		this.colorCodigoCoG=g;
		this.colorCodigoCoB=b;
	}
	
	public void setColorCodigoMF(int r, int g, int b)
	{
		this.colorCodigoMFR=r;
		this.colorCodigoMFG=g;
		this.colorCodigoMFB=b;
	}
	
	public void setColorCodigoMB(int r, int g, int b)
	{
		this.colorCodigoMBR=r;
		this.colorCodigoMBG=g;
		this.colorCodigoMBB=b;
	}
	
	public void setColorCodigoRC(int r, int g, int b)
	{
		this.colorCodigoRCR=r;
		this.colorCodigoRCG=g;
		this.colorCodigoRCB=b;
	}
	
	public void setColorIluminado(int r, int g, int b)
	{
		this.colorIluminadoR=r;
		this.colorIluminadoG=g;
		this.colorIluminadoB=b;
	}
	
	public void setColorResaltado(int r, int g, int b)
	{
		this.colorResaltadoR=r;
		this.colorResaltadoG=g;
		this.colorResaltadoB=b;
	}
	
	
	public void setModoColor(int valor)
	{
		this.modoColor=valor;
	}
	public void setColorModo2(int r, int g, int b, int i)
	{
		if (i>=0 && i<this.colorModo2_R.length)
		{
			this.colorModo2_R[i]=r;
			this.colorModo2_G[i]=g;
			this.colorModo2_B[i]=b;
		}
	}
	
	
	public void setFuenteCodigo(String nombre, int tam)//, String atr)
	{
		this.fuenteCodigo=nombre;
		this.tamFuenteCodigo=tam;
	}
	
	public void setFuenteTraza(String nombre, int tam)//, String atr)
	{
		this.fuenteTraza=nombre;
		this.tamFuenteTraza=tam;
	}
	
	public void setZoomArbol(int zoom)
	{
		this.zoomArbol=zoom;
	}
	
	public void setZoomPila(int zoom)
	{
		this.zoomPila=zoom;
	}
	
	/*public void setZoomArbolEstr(int zoom)
	{
		this.zoomArbolEstr=zoom;
	}*/
	
	public void setZoomCrono(int zoom)
	{
		this.zoomCrono=zoom;
	}
	
	public void setZoomEstructura(int zoom)
	{
		this.zoomEstructura=zoom;
	}
	
	
	// Métodos get
	
	public int[] getColorFEntrada()
	{
		int color[]=new int[3];
		color[0]=this.colorFEntradaR;
		color[1]=this.colorFEntradaG;
		color[2]=this.colorFEntradaB;
		return color;
	}
	
	public int[] getColorFSalida()
	{
		int color[]=new int[3];
		color[0]=this.colorFSalidaR;
		color[1]=this.colorFSalidaG;
		color[2]=this.colorFSalidaB;
		return color;
	}

	public int[] getColorC1Entrada()
	{
		int color[]=new int[3];
		color[0]=this.colorC1EntradaR;
		color[1]=this.colorC1EntradaG;
		color[2]=this.colorC1EntradaB;
		return color;
	}
	
	public int[] getColorC1Salida()
	{
		int color[]=new int[3];
		color[0]=this.colorC1SalidaR;
		color[1]=this.colorC1SalidaG;
		color[2]=this.colorC1SalidaB;
		return color;
	}
	
	public int[] getColorCAEntrada()
	{
		int color[]=new int[3];
		color[0]=this.colorCAEntradaR;
		color[1]=this.colorCAEntradaG;
		color[2]=this.colorCAEntradaB;
		return color;
	}
	
	public int[] getColorCASalida()
	{
		int color[]=new int[3];
		color[0]=this.colorCASalidaR;
		color[1]=this.colorCASalidaG;
		color[2]=this.colorCASalidaB;
		return color;
	}
	
	
	public int[] getColorC1NCSalida()
	{
		int color[]=new int[3];
		color[0]=this.colorC1NCSalidaR;
		color[1]=this.colorC1NCSalidaG;
		color[2]=this.colorC1NCSalidaB;
		return color;
	}
	
	public boolean getColorDegradadoModo1()
	{
		return this.colorDegradadoModo1;
	}
	
	public boolean getColorDegradadoModo2()
	{
		return this.colorDegradadoModo2;
	}
	
	
	
	
	public int[] getColorFlecha()
	{
		int color[]=new int[3];
		color[0]=this.colorFlechaR;
		color[1]=this.colorFlechaG;
		color[2]=this.colorFlechaB;
		return color;
	}
	
	public int getGrosorFlecha()
	{
		return this.grosorFlecha;
	}

	public int[] getColorPanel()
	{
		int color[]=new int[3];
		color[0]=this.colorPanelR;
		color[1]=this.colorPanelG;
		color[2]=this.colorPanelB;
		return color;
	}
	
	public int[] getColorActual()
	{
		int color[]=new int[3];
		color[0]=this.colorActualR;
		color[1]=this.colorActualG;
		color[2]=this.colorActualB;
		return color;
	}
	
	public int[] getColorCActual()
	{
		int color[]=new int[3];
		color[0]=this.colorCActualR;
		color[1]=this.colorCActualG;
		color[2]=this.colorCActualB;
		return color;
	}
	
	public int getFormaFlecha()
	{
		return this.formaFlecha;
	}
	
	public int getGrosorActual()
	{
		return this.grosorActual;
	}
	
	public int getDistanciaH()
	{
		return this.distanciaH;
	}
	
	public int getDistanciaV()
	{
		return this.distanciaV;
	}
	
	public int getTipoBordeCelda()
	{
		return this.tipoBordeCelda;
	}
	
	public int[] getColorCodigoPR()
	{
		int color[]=new int[3];
		color[0]=this.colorCodigoPRR;
		color[1]=this.colorCodigoPRG;
		color[2]=this.colorCodigoPRB;
		return color;
	}
	
	public int[] getColorCodigoCo()
	{
		int color[]=new int[3];
		color[0]=this.colorCodigoCoR;
		color[1]=this.colorCodigoCoG;
		color[2]=this.colorCodigoCoB;
		return color;
	}
	
	public int[] getColorCodigoMF()
	{
		int color[]=new int[3];
		color[0]=this.colorCodigoMFR;
		color[1]=this.colorCodigoMFG;
		color[2]=this.colorCodigoMFB;
		return color;
	}
	
	public int[] getColorCodigoMB()
	{
		int color[]=new int[3];
		color[0]=this.colorCodigoMBR;
		color[1]=this.colorCodigoMBG;
		color[2]=this.colorCodigoMBB;
		return color;
	}
	
	public int[] getColorCodigoRC()
	{
		int color[]=new int[3];
		color[0]=this.colorCodigoRCR;
		color[1]=this.colorCodigoRCG;
		color[2]=this.colorCodigoRCB;
		return color;
	}
	
	public int[] getColorIluminado()
	{
		int color[]=new int[3];
		color[0]=this.colorIluminadoR;
		color[1]=this.colorIluminadoG;
		color[2]=this.colorIluminadoB;
		return color;
	}
	
	public int[] getColorResaltado()
	{
		int color[]=new int[3];
		color[0]=this.colorResaltadoR;
		color[1]=this.colorResaltadoG;
		color[2]=this.colorResaltadoB;
		return color;
	}

	public String getFuenteCodigo()
	{
		return this.fuenteCodigo;
	}
	
	public int getTamFuenteCodigo()
	{
		return this.tamFuenteCodigo;
	}
	
	public int getModoColor()
	{
		return this.modoColor;
	}
	
	
	public int[] getColorModo2(int i)
	{
		int color[]= new int[3];
		if (i>=0 && i<this.colorModo2_R.length)
		{
			color[0]=this.colorModo2_R[i];
			color[1]=this.colorModo2_G[i];
			color[2]=this.colorModo2_B[i];
		}
		return color;
	}
	
	
	public String getFuenteTraza()
	{
		return this.fuenteTraza;
	}
	
	public int getTamFuenteTraza()
	{
		return this.tamFuenteTraza;
	}
	
	public int getZoomArbol()
	{
		return this.zoomArbol;
	}
	
	public int getZoomPila()
	{
		return this.zoomPila;
	}
	
	public Element getRepresentacionElement(Document d)
	{
		Element e=d.createElement("OpcionConfVisualizacion");
		
		// Color fuente para celdas de entrada y salida
		Element e01=d.createElement("colorFEntrada");
		e01.setAttribute("r",""+this.colorFEntradaR);
		e01.setAttribute("g",""+this.colorFEntradaG);
		e01.setAttribute("b",""+this.colorFEntradaB);
		
		Element e02=d.createElement("colorFSalida");
		e02.setAttribute("r",""+this.colorFSalidaR);
		e02.setAttribute("g",""+this.colorFSalidaG);
		e02.setAttribute("b",""+this.colorFSalidaB);
		
		// Color 1 para celdas de entrada y salida
		Element e03=d.createElement("colorC1Entrada");
		e03.setAttribute("r",""+this.colorC1EntradaR);
		e03.setAttribute("g",""+this.colorC1EntradaG);
		e03.setAttribute("b",""+this.colorC1EntradaB);
		
		Element e04=d.createElement("colorC1Salida");
		e04.setAttribute("r",""+this.colorC1SalidaR);
		e04.setAttribute("g",""+this.colorC1SalidaG);
		e04.setAttribute("b",""+this.colorC1SalidaB);
		
		// Color atenuacion para celdas de entrada y salida
		Element e07=d.createElement("colorC1AEntrada");
		e07.setAttribute("r",""+this.colorCAEntradaR);
		e07.setAttribute("g",""+this.colorCAEntradaG);
		e07.setAttribute("b",""+this.colorCAEntradaB);
		
		Element e08=d.createElement("colorC1ASalida");
		e08.setAttribute("r",""+this.colorCASalidaR);
		e08.setAttribute("g",""+this.colorCASalidaG);
		e08.setAttribute("b",""+this.colorCASalidaB);

		// Colores no calculados para celdas de salida
		Element e11=d.createElement("colorC1NCSalida");
		e11.setAttribute("r",""+this.colorC1NCSalidaR);
		e11.setAttribute("g",""+this.colorC1NCSalidaG);
		e11.setAttribute("b",""+this.colorC1NCSalidaB);

		// Deben las celdas aplicar degradado
		Element e13=d.createElement("degradados");
		e13.setAttribute("modo1",""+this.colorDegradadoModo1);
		e13.setAttribute("modo2",""+this.colorDegradadoModo2);
		
		// Colores para flechas y paneles
		Element e15=d.createElement("colorFlecha");
		e15.setAttribute("r",""+this.colorFlechaR);
		e15.setAttribute("g",""+this.colorFlechaG);
		e15.setAttribute("b",""+this.colorFlechaB);
		
		Element e16=d.createElement("colorPanel");
		e16.setAttribute("r",""+this.colorPanelR);
		e16.setAttribute("g",""+this.colorPanelG);
		e16.setAttribute("b",""+this.colorPanelB);		
		
		// Colores para nodo actual y camino actual
		Element e17=d.createElement("colorActual");
		e17.setAttribute("r",""+this.colorActualR);
		e17.setAttribute("g",""+this.colorActualG);
		e17.setAttribute("b",""+this.colorActualB);
		
		Element e18=d.createElement("colorCActual");
		e18.setAttribute("r",""+this.colorCActualR);
		e18.setAttribute("g",""+this.colorCActualG);
		e18.setAttribute("b",""+this.colorCActualB);		
		
		// Valores varios
		Element e19=d.createElement("varios");
		e19.setAttribute("grosorMarcos",""+this.grosorActual);
		e19.setAttribute("grosorFlecha",""+this.grosorFlecha);
		e19.setAttribute("tipoFlecha",""+this.formaFlecha);
		e19.setAttribute("tipoBordeCelda",""+this.tipoBordeCelda);
		
		
		// Color para vista código: palabras reservadas
		Element e20=d.createElement("colorPalabrasReservadas");
		e20.setAttribute("r",""+this.colorCodigoPRR);
		e20.setAttribute("g",""+this.colorCodigoPRG);
		e20.setAttribute("b",""+this.colorCodigoPRB);
		
		// Color para vista código: comentarios
		Element e21=d.createElement("colorComentarios");
		e21.setAttribute("r",""+this.colorCodigoCoR);
		e21.setAttribute("g",""+this.colorCodigoCoG);
		e21.setAttribute("b",""+this.colorCodigoCoB);			
		
		// Color para vista código: nombre método foreground
		Element e22=d.createElement("colorMetodoForeground");
		e22.setAttribute("r",""+this.colorCodigoMFR);
		e22.setAttribute("g",""+this.colorCodigoMFG);
		e22.setAttribute("b",""+this.colorCodigoMFB);				
		
		// Color para vista código: nombre método background
		Element e23=d.createElement("colorMetodoBackground");
		e23.setAttribute("r",""+this.colorCodigoMBR);
		e23.setAttribute("g",""+this.colorCodigoMBG);
		e23.setAttribute("b",""+this.colorCodigoMBB);				
				
		// Color para vista código: nombre código
		Element e24=d.createElement("colorCodigo");
		e24.setAttribute("r",""+this.colorCodigoRCR);
		e24.setAttribute("g",""+this.colorCodigoRCG);
		e24.setAttribute("b",""+this.colorCodigoRCB);	
		
		// Color para vista código: nombre código
		Element e25=d.createElement("colorIluminado");
		e25.setAttribute("r",""+this.colorIluminadoR);
		e25.setAttribute("g",""+this.colorIluminadoG);
		e25.setAttribute("b",""+this.colorIluminadoB);
		
		// Color para vista código: nombre código
		Element e34=d.createElement("colorResaltado");
		e34.setAttribute("r",""+this.colorResaltadoR);
		e34.setAttribute("g",""+this.colorResaltadoG);
		e34.setAttribute("b",""+this.colorResaltadoB);
		
		// Modo de color
		Element e14=d.createElement("modoColor");
		e14.setAttribute("modo",""+this.getModoColor());
		
		
		// Fuentes y sus tamanos
		Element e31=d.createElement("fuentesTrazaCodigo");
		e31.setAttribute("fuenteCodigo",this.fuenteCodigo);
		e31.setAttribute("tamFuenteCodigo",""+this.tamFuenteCodigo);
		e31.setAttribute("fuenteTraza",this.fuenteTraza);
		e31.setAttribute("tamFuenteTraza",""+this.tamFuenteTraza);
		
		// Zooms
		Element e32=d.createElement("zoomsDefecto");
		e32.setAttribute("zoomArbol",""+this.zoomArbol);
		e32.setAttribute("zoomPila",""+this.zoomPila);
		//e32.setAttribute("zoomArbolEstr",""+this.zoomArbolEstr);
		e32.setAttribute("zoomCrono",""+this.zoomCrono);
		e32.setAttribute("zoomEstructura",""+this.zoomEstructura);
				
		
		// Distancias
		Element e33=d.createElement("distancias");
		e33.setAttribute("vertical",""+this.distanciaV);
		e33.setAttribute("horizontal",""+this.distanciaH);		
		
		e.appendChild(e01);
		e.appendChild(e02);
		e.appendChild(e03);
		e.appendChild(e04);
		//e.appendChild(e05);
		//e.appendChild(e06);
		e.appendChild(e07);
		e.appendChild(e08);
		//e.appendChild(e09);
		//e.appendChild(e10);
		e.appendChild(e11);
		//e.appendChild(e12);
		e.appendChild(e13);
		e.appendChild(e14);
		e.appendChild(e15);
		e.appendChild(e16);
		e.appendChild(e17);
		e.appendChild(e18);
		e.appendChild(e19);
		e.appendChild(e20);
		e.appendChild(e21);
		e.appendChild(e22);
		e.appendChild(e23);
		e.appendChild(e24);
		e.appendChild(e25);
		//e.appendChild(e26);
		//e.appendChild(e27);
		//e.appendChild(e28);
		//e.appendChild(e29);
		//e.appendChild(e30);
		e.appendChild(e31);
		e.appendChild(e32);
		e.appendChild(e33);
		e.appendChild(e34);
		
		// Colores Modo 2
		for (int i=0; i<Conf.numColoresMetodos; i++)
		{
			Element ecm2=d.createElement("colorm2_"+i);
			ecm2.setAttribute("r",""+this.colorModo2_R[i]);
			ecm2.setAttribute("g",""+this.colorModo2_G[i]);
			ecm2.setAttribute("b",""+this.colorModo2_B[i]);		
			e.appendChild(ecm2);
		}
		
		return e;
	}
	
	
	public void setValores(Element e)
	{
		Element elements[];
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorFEntrada"));
		this.setColorFEntrada( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);
								
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorFSalida"));
		this.setColorFSalida( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);
								
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorC1Entrada"));
		this.setColorC1Entrada( 	Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorC1AEntrada"));
		this.setColorCAEntrada( 	Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);
									
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorC1Salida"));
		this.setColorC1Salida( 	Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorC1ASalida"));
		this.setColorCASalida( 	Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);
									
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorC1NCSalida"));
		this.setColorC1NCSalida( 	Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);

		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("degradados"));
		this.setColorDegradadoModo1( 	Boolean.parseBoolean(elements[0].getAttribute("modo1")));
		this.setColorDegradadoModo2( 	Boolean.parseBoolean(elements[0].getAttribute("modo2")));	

		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorFlecha"));
		this.setColorFlecha( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);	

		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorPanel"));
		this.setColorPanel( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);

		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorActual"));
		this.setColorActual( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);

		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorCActual"));
		this.setColorCActual( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("modoColor"));
		this.setModoColor( 			Integer.parseInt(elements[0].getAttribute("modo")) );
		
		for (int i=0; i<Conf.numColoresMetodos; i++)
		{
			elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorm2_"+i));
			this.setColorModo2 ( 	Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	, i);
		}

		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("varios"));
		this.setGrosorActual( 		Integer.parseInt(elements[0].getAttribute("grosorMarcos")));
		this.setGrosorFlecha( 		Integer.parseInt(elements[0].getAttribute("grosorFlecha")));
		this.setFormaFlecha( 		Integer.parseInt(elements[0].getAttribute("tipoFlecha")));
		this.setTipoBordeCelda( 	Integer.parseInt(elements[0].getAttribute("tipoBordeCelda")));
							
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorPalabrasReservadas"));
		this.setColorCodigoPR( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);

		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorComentarios"));
		this.setColorCodigoCo( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);
									
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorMetodoForeground"));
		this.setColorCodigoMF( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);
									
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorMetodoBackground"));
		this.setColorCodigoMB( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);
									
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorCodigo"));
		this.setColorCodigoRC( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);
									
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorIluminado"));
		this.setColorIluminado( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);	
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("colorResaltado"));
		this.setColorResaltado( 		Integer.parseInt(elements[0].getAttribute("r")),
									Integer.parseInt(elements[0].getAttribute("g")),
									Integer.parseInt(elements[0].getAttribute("b"))	);

		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("fuentesTrazaCodigo"));
		this.setFuenteCodigo( 		(elements[0].getAttribute("fuenteCodigo")),
									Integer.parseInt(elements[0].getAttribute("tamFuenteCodigo")));
		this.setFuenteTraza( 		(elements[0].getAttribute("fuenteTraza")),
									Integer.parseInt(elements[0].getAttribute("tamFuenteTraza")));		
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("zoomsDefecto"));
		this.setZoomArbol( 			Integer.parseInt(elements[0].getAttribute("zoomArbol")));
		this.setZoomPila( 			Integer.parseInt(elements[0].getAttribute("zoomPila")));
		//this.setZoomArbolEstr( 		Integer.parseInt(elements[0].getAttribute("zoomArbolEstr")));
		this.setZoomCrono( 			Integer.parseInt(elements[0].getAttribute("zoomCrono")));
		this.setZoomEstructura( 	Integer.parseInt(elements[0].getAttribute("zoomEstructura")));
		
		elements=ManipulacionElement.nodeListToElementArray(e.getElementsByTagName("distancias"));
		this.setDistanciaV( 		Integer.parseInt(elements[0].getAttribute("vertical")));
		this.setDistanciaH( 		Integer.parseInt(elements[0].getAttribute("horizontal")));	
		
	}
	

		

	
	// Métodos serialización
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
	{
		this.colorFEntradaR=stream.readInt();
		this.colorFEntradaG=stream.readInt();
		this.colorFEntradaB=stream.readInt();
		this.colorFSalidaR=stream.readInt();
		this.colorFSalidaG=stream.readInt();
		this.colorFSalidaB=stream.readInt();
		
		this.colorC1EntradaR=stream.readInt();
		this.colorC1EntradaG=stream.readInt();
		this.colorC1EntradaB=stream.readInt();
		this.colorC1SalidaR=stream.readInt();
		this.colorC1SalidaG=stream.readInt();
		this.colorC1SalidaB=stream.readInt();
		
		this.colorC1NCSalidaR=stream.readInt();
		this.colorC1NCSalidaG=stream.readInt();
		this.colorC1NCSalidaB=stream.readInt();
		
		this.colorDegradadoModo1=stream.readBoolean();
		this.colorDegradadoModo2=stream.readBoolean();

		this.colorFlechaR=stream.readInt();
		this.colorFlechaG=stream.readInt();
		this.colorFlechaB=stream.readInt();
		
		this.colorPanelR=stream.readInt();
		this.colorPanelG=stream.readInt();
		this.colorPanelB=stream.readInt();
		
		this.colorActualR=stream.readInt();
		this.colorActualG=stream.readInt();
		this.colorActualB=stream.readInt();
		
		this.colorCActualR=stream.readInt();
		this.colorCActualG=stream.readInt();
		this.colorCActualB=stream.readInt();
		
		this.grosorActual=stream.readInt();
		this.grosorFlecha=stream.readInt();
		this.formaFlecha=stream.readInt();
		
		this.distanciaH=stream.readInt();
		this.distanciaV=stream.readInt();
		this.tipoBordeCelda=stream.readInt();
		
		this.colorCodigoPRR=stream.readInt();
		this.colorCodigoPRG=stream.readInt();
		this.colorCodigoPRB=stream.readInt();
		
		this.colorCodigoCoR=stream.readInt();
		this.colorCodigoCoG=stream.readInt();
		this.colorCodigoCoB=stream.readInt();
		
		this.colorCodigoMFR=stream.readInt();
		this.colorCodigoMFG=stream.readInt();
		this.colorCodigoMFB=stream.readInt();
		
		this.colorCodigoMBR=stream.readInt();
		this.colorCodigoMBG=stream.readInt();
		this.colorCodigoMBB=stream.readInt();
		
		this.colorCodigoRCR=stream.readInt();
		this.colorCodigoRCG=stream.readInt();
		this.colorCodigoRCB=stream.readInt();
		
		this.fuenteCodigo=stream.readUTF();
		this.tamFuenteCodigo=stream.readInt();
		//this.atributosFuenteCodigo=stream.readUTF();
		
		this.fuenteTraza=stream.readUTF();
		this.tamFuenteTraza=stream.readInt();
		//this.atributosFuenteTraza=stream.readUTF();
		
		this.zoomArbol=stream.readInt();
		this.zoomPila=stream.readInt();

	}

	private void writeObject(ObjectOutputStream stream)throws IOException
	{
		stream.writeInt(this.colorFEntradaR);
		stream.writeInt(this.colorFEntradaG);
		stream.writeInt(this.colorFEntradaB);
		stream.writeInt(this.colorFSalidaR);
		stream.writeInt(this.colorFSalidaG);
		stream.writeInt(this.colorFSalidaB);
		
		stream.writeInt(this.colorC1EntradaR);
		stream.writeInt(this.colorC1EntradaG);
		stream.writeInt(this.colorC1EntradaB);
		stream.writeInt(this.colorC1SalidaR);
		stream.writeInt(this.colorC1SalidaG);
		stream.writeInt(this.colorC1SalidaB);
		
		stream.writeInt(this.colorC1NCSalidaR);
		stream.writeInt(this.colorC1NCSalidaG);
		stream.writeInt(this.colorC1NCSalidaB);
		
		stream.writeBoolean(this.colorDegradadoModo1);
		stream.writeBoolean(this.colorDegradadoModo2);

		stream.writeInt(this.colorFlechaR);
		stream.writeInt(this.colorFlechaG);
		stream.writeInt(this.colorFlechaB);
		
		stream.writeInt(this.colorPanelR);
		stream.writeInt(this.colorPanelG);
		stream.writeInt(this.colorPanelB);
		
		stream.writeInt(this.colorActualR);
		stream.writeInt(this.colorActualG);
		stream.writeInt(this.colorActualB);
		
		stream.writeInt(this.colorCActualR);
		stream.writeInt(this.colorCActualG);
		stream.writeInt(this.colorCActualB);
		
		stream.writeInt(this.grosorActual);
		stream.writeInt(this.grosorFlecha);
		stream.writeInt(this.formaFlecha);
		
		stream.writeInt(this.distanciaH);
		stream.writeInt(this.distanciaV);
		stream.writeInt(this.tipoBordeCelda);

		stream.writeInt(this.colorCodigoPRR);
		stream.writeInt(this.colorCodigoPRG);
		stream.writeInt(this.colorCodigoPRB);
		
		stream.writeInt(this.colorCodigoCoR);
		stream.writeInt(this.colorCodigoCoG);
		stream.writeInt(this.colorCodigoCoB);
		
		stream.writeInt(this.colorCodigoMFR);
		stream.writeInt(this.colorCodigoMFG);
		stream.writeInt(this.colorCodigoMFB);
		
		stream.writeInt(this.colorCodigoMBR);
		stream.writeInt(this.colorCodigoMBG);
		stream.writeInt(this.colorCodigoMBB);
		
		stream.writeInt(this.colorCodigoRCR);
		stream.writeInt(this.colorCodigoRCG);
		stream.writeInt(this.colorCodigoRCB);
		
		stream.writeUTF(this.fuenteCodigo);
		stream.writeInt(this.tamFuenteCodigo);
		
		stream.writeUTF(this.fuenteTraza);
		stream.writeInt(this.tamFuenteTraza);
		
		stream.writeInt(this.zoomArbol);
		stream.writeInt(this.zoomPila);
	}

}

