/**
	Establece ciertos parámetros de configuración para la interfaz de la aplicación
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package conf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;


import opciones.*;


public class Conf
{
	static GestorOpciones gOpciones=new GestorOpciones();

	// *************************************************** Politicas de visualizacion
	public static final int  VISUALIZAR_TODO = 1;
	public static final int  VISUALIZAR_ENTRADA = 2;
	public static final int  VISUALIZAR_SALIDA = 3;
	
	/*static final int  BACKTRACKING = 11;
	static final int  DIVIDE_Y_VENCERAS = 12;
	static final int  PROG_DINAMICA = 13;
	static final int  ALG_VORACES = 14;*/
	
	public static int mostrarSalidaVoid = 1;		// 0=no, 1=sí
	
	public static int elementosVisualizar = Conf.VISUALIZAR_TODO;		// Estado actual
	
	public static int elementosVisualizar_ant = Conf.VISUALIZAR_TODO;	// Estado anterior
	
	public static int historia=0; // 0 = mantener, 1 = atenuar, 2 = eliminar historia
	
	public static int historia_ant=0; // 0 = mantener, 1 = atenuar, 2 = eliminar historia

	public static boolean mostrarArbolSalto=false;	
	
	public static boolean mostrarVisor=false;	
	
	public static boolean mostrarEstructuraEnArbol=true;//true;	// VistaArbol
	
	public static boolean mostrarArbolColapsado=true;//true;	// VistaArbol
	
	public static boolean mostrarEstructuraCompleta=true;	// VistaCrono
	
	public static boolean mostrarSalidaLigadaEntrada=true;	// VistaCrono
	
	public static boolean sangrado=true;
	
	public static boolean idMetodoTraza=false;
	
	public static boolean soloEstructuraDYVcrono=true;
	
	public static boolean arranqueEstadoInicial=true;
	
	public static boolean ajustarVistaGlobal=true;
	
	public static boolean visualizacionDinamica=true;
	
	
	// *************************************************** Colores
	
	// Color del fondo de la ventana principal
	public static Color colorPanelVentana=new Color(156,153,136);
	
	// Color de la parte superior de cada panel de visualización
	public static Color colorFrontalPanelContenedor=new Color(226,226,226);
	
	// Color de los botones	
	public static Color colorBoton=new Color(216,213,196);
	
	// Color del fondo del panel de la visualización
	public static Color colorPanelAlgoritmo=new Color(255,255,255);
	
	// Color del fondo de los cuadros de diálogo y de la ventana
	public static Color colorCuadroDialogo=new Color(236,233,216);	

	
	
	// Visualizacion: Color para valores de entrada
	public static Color colorFEntrada=null;
	
	// Visualizacion: Color para valores de salida
	public static Color colorFSalida=null;
	
	// Visualizacion: Color comun para valores
	public static Color colorC1Entrada=null;
	
	// Visualizacion: Color para valores de entrada
	public static Color colorC2Entrada=null;
	
	// Visualizacion: Color para valores de entrada. Resto de parámetros en cronológica
	public static Color colorC3Entrada=null;
	
	// Visualizacion: Color para valores de entrada. Resto de parámetros en cronológica. Degradado
	public static Color colorC4Entrada=null;
	
	// Visualizacion: Color para valores de salida
	public static Color colorC1Salida=null;
	
	// Visualizacion: Color comun para valores
	public static Color colorC2Salida=null;
	
	// Visualizacion: Color para valores de salida. Resto de parámetros en cronológica
	public static Color colorC3Salida=null;
	
	// Visualizacion: Color para valores de salida. Resto de parámetros en cronológica. Degradado
	public static Color colorC4Salida=null;
	
	// Visualizacion: Color comun para valores
	public static Color colorC1AEntrada=null;
	
	// Visualizacion: Color para valores de entrada
	public static Color colorC2AEntrada=null;
	
	// Visualizacion: Color para valores de salida
	public static Color colorC1ASalida=null;
	
	// Visualizacion: Color comun para valores
	public static Color colorC2ASalida=null;
	
	// Visualizacion: Color para valores de salida no calculados
	public static Color colorC1NCSalida=null;
	
	// Visualizacion: Color para valores de salida no calculados
	public static Color colorC2NCSalida=null;
	
	// Visualizacion: Color comun para valores
	public static Color colorFlecha=null;
	
	// Visualizacion: Color para el panel de la visualizacion
	public static Color colorPanel=null;
	
	// Visualizacion: Color para el marco del nodo activo
	public static Color colorMarcoActual=null;
	
	// Visualizacion: Color para los marcos del camino activo
	public static Color colorMarcosCActual=null;
	
	// Visualizacion: Color para las celdas degradadas con atenuación histórica
	public static Color[] coloresAtenuados=null;
	
	// Visualizacion: Color para las celdas de color solido con atenuación histórica
	public static Color[] coloresAtenuadosSolidos=null;
	
	// Visualizacion (código): Color para las palabras reservadas de Java
	public static Color colorCodigoPR=null;
	
	// Visualizacion (código): Color para los comentarios
	public static Color colorCodigoCo=null;
	
	// Visualizacion (código): Color para el nombre del método (foreground)
	public static Color colorCodigoMF=null;
	
	// Visualizacion (código): Color para el nombre del método (background)
	public static Color colorCodigoMB=null;
	
	// Visualizacion (código): Color para el resto del código
	public static Color colorCodigoRC=null;
	
	// Visualizacion (código): Color para el resto del código
	public static Color colorCodigoFP=null;
	
	// Visualizacion (traza): Color para las líneas de entrada
	public static Color colorTrazaE=null;
	
	// Visualizacion (traza): Color para las líneas de salida
	public static Color colorTrazaS=null;
	
	// Visualizacion (traza): Color para las líneas de entrada atenuadas
	public static Color colorTrazaEA=null;
	
	// Visualizacion (traza): Color para las líneas de salida atenuadas
	public static Color colorTrazaSA=null;
	
	// Visualizacion (traza): Color para el fondo del panel
	public static Color colorTrazaFP=null;
	
	// Nodos buscados
	public static Color colorIluminado=new Color(255,125,0);
	
	// Nodos resaltados
	public static Color colorResaltado=new Color(204,204,0);
	
	
	
	public static Color[] degradadosEstructuraEC1=null;
	public static Color[] degradadosEstructuraSC1=null;
	
	public static Color colorC1Eant=null;
	public static Color colorC1Sant=null;
	public static Color colorC2Eant=null;
	public static Color colorC2Sant=null;
	
	
	
	// Nodos buscados (atenuado)
	//public static Color colorIluminadoA=new Color(255,125,0);
	
	public static boolean degradado1=false;	
	public static boolean degradado2=false;
	
	
	public static final int numColoresMetodos=10;
	
	public static Color coloresNodo[]=new Color[numColoresMetodos];		// Foreground
	public static Color coloresNodo2[]=new Color[numColoresMetodos];	// Background
	
	public static Color coloresNodoA[]=new Color[numColoresMetodos];	// Atenuados Foreground
	public static Color coloresNodoA2[]=new Color[numColoresMetodos];	// Atenuados Background
	
	
	public static int modoColor=2;	// 1 = diferenciamos entre entrada y salida
									// 2 = diferenciamos entre métodos

	static int medidaColorIntermedio=3;	// Un cuarto, un tercio... para tonos atenuados en modo 2
	
	
	//********* Combinacion WinXP 
	// colorCuadroDialogo, colorPanelVentana = 236,233,216
	// colorFrontalPanelContenedor = 246,243,226
	
	
	
	// *************************************************** Tamaños;
	public static int altoCeldaDivisoria=4;
	public static int anchoCeldaDivisoria=4;
	
	
	public static int altoSeguridad=40;	//	Espacio entre niveles (alturas)
	
	// *************************************************** Fuentes
	
	// Fuente para los menús
	public static Font fuenteMenu=new Font("Arial",Font.PLAIN,11);
	
	// Fuente para los cuadros de diálogo
	public static Font fuenteCuadro=new Font("Arial",Font.PLAIN,11);
	
	// Fuente para títulos de paneles (nombres de métodos, de ficheros de código, ...)
	public static Font fuenteTitulo=new Font("Arial",Font.BOLD,12);

	// Vistas código y traza
	public static Font fuenteCodigo=null;
	public static Font fuenteTraza=null;
	
	
	// Tamaño máximo para fuentes dentro de visualización
	public static int TamanoMaximoFuente=50;
	
	// *************************************************** Dimensiones para botones
	public static int botonVisualizacionAncho=27;
	public static int botonVisualizacionAlto=21;
	
	public static int grosorFlecha;
	public static int formaFlecha;
	public static int bordeCelda;
	public static int grosorMarco;
	
	public static int sepH;
	public static int sepV;
	
	// Win 79,23	Otros 88,23
	
	public static int anchoBoton=95;
	public static int altoBoton=23;
	
	// *************************************************** Zoom
	public static int zoomArbol=0;
	public static int zoomPila=0;
	
	
	// *************************************************** Tamaño del monitor y de la ventana del programa
	public static int anchoPantalla=1024;		// Monitor
	public static int altoPantalla=768;		// Monitor
		
	public static int anchoVentana=800;		// Ventana
	public static int altoVentana=600;			// Ventana
	
	
	// *************************************************** Generacion valores aleatorios para parámetros
	public static int MaxInt=800;			// Valor máximo que puede devolver la generación aleatoria de numeros
	public static short MaxShort=300;
	public static byte MaxByte=80;
	public static long MaxLong=2000;
	public static float MaxFloat=(float)350.55;
	public static double MaxDouble=3456.78;
	
	public static int MaxLongString=50;	// Valor máximo de la longitud que pueden tomar las cadenas generadas aleatoriamente
	
	public static boolean SoloPosInt=true;			// true= sólo se generan números positivos durante la generación de enteros
	public static boolean SoloPosShort=true;		// true= sólo se generan números positivos durante la generación de cortos
	public static boolean SoloPosByte=true;		// true= sólo se generan números positivos durante la generación de bytes
	public static boolean SoloPosLong=true;		// true= sólo se generan números positivos durante la generación de largos
	public static boolean SoloPosFloat=true;		// true= sólo se generan números positivos durante la generación de coma flotante
	public static boolean SoloPosDouble=true;		// true= sólo se generan números positivos durante la generación de coma flotante doble
	
	

	
	// *************************************************** Configuración de ubicación de Vistas
	
	public static final int PANEL_VERTICAL=1;
	public static final int PANEL_HORIZONTAL=2;
	
	
	public static Vista[] vistas;
	public static int disposicionPaneles;
	

	// *************************************************** Configuración de archivo LOG
	
	public static boolean fichero_log = false;
	
	
	
	// *************************************************** Variables temporales de estado (flags)
	public static boolean redibujarGrafoArbol=false;	// Algunos cuadros de configuración pueden forzar a que se redibuje totalmente el grafo
												// (por ejemplo, al cambiar espacios entre celdas - Conf.sepV, COnf.sepV- )
	
	public static boolean panelArbolReajustado=false;	// Indica que se ha ajustado el tamaño del árbol a la ventana, el comportamiento de
												// la clase PanelArbol tiene que variar en su método visualizar
	
	
	public static boolean haciendoAjuste=false;		// Indica que se va a ajustar el árbol a la ventana, el comportamiento de
												// la clase PanelArbol tiene que variar en su método visualizar
	

	// *************************************************** Configuración general de SRec
	
	public static String idioma="ES";
	
	private static void calculoColoresModo2()
	{
		if (coloresNodo!=null)
		{
			if (degradado2)
				for (int i=0; i<coloresNodo2.length; i++)
					coloresNodo2[i]=calcularDegradado(coloresNodo[i]);
			else
				for (int i=0; i<coloresNodo2.length; i++)
					coloresNodo2[i]=coloresNodo[i];
			
			
			for (int i=0; i<coloresNodo.length; i++)
			{
				if (historia==1)
					coloresNodoA[i]=calcularColorIntermedio(coloresNodo[i],Conf.colorPanel);
				else
					coloresNodoA[i]=coloresNodo[i];
				
				if (degradado2)
					coloresNodoA2[i]=calcularDegradado(coloresNodoA[i]);
				else
					coloresNodoA2[i]=coloresNodoA[i];
			}		
			
			
			
			/*for (int i=0; i<coloresNodo.length; i++)
			{
				System.out.println("     coloresNodo["+i+"] = "+coloresNodo[i].getRed()+","+coloresNodo[i].getGreen()+","+coloresNodo[i].getBlue());
				System.out.println("    coloresNodo2["+i+"] = "+coloresNodo2[i].getRed()+","+coloresNodo2[i].getGreen()+","+coloresNodo2[i].getBlue());
				System.out.println("    coloresNodoA["+i+"] = "+coloresNodoA[i].getRed()+","+coloresNodoA[i].getGreen()+","+coloresNodoA[i].getBlue());
				System.out.println("   coloresNodoA2["+i+"] = "+coloresNodoA2[i].getRed()+","+coloresNodoA2[i].getGreen()+","+coloresNodoA2[i].getBlue());
			}*/
			
		}
	}
	

	/**
		Establece valores acerca de colores, tamaños, fuentes, etc. para la visualización de los algoritmos.
		Los paneles de visualización tomarán de esta clase los valores necesarios para adaptar la visualiza-
		ción a los gustos del usuario.
	*/
	public static void setValoresVisualizacion()
	{
		OpcionConfVisualizacion ocv;
		ocv=(OpcionConfVisualizacion)gOpciones.getOpcion("OpcionConfVisualizacion",false);
		
		int color[]=null;
		
		
		// Colores
		color=ocv.getColorFEntrada();
		colorFEntrada=new Color(color[0],color[1],color[2]);
		
		color=ocv.getColorC1Entrada();
		colorC1Entrada=new Color(color[0],color[1],color[2]);
		
		if (!ocv.getColorDegradadoModo1())
			colorC2Entrada=colorC1Entrada;
		else
			colorC2Entrada=calcularDegradado(colorC1Entrada);
		
		colorC3Entrada=disminuirColor(colorC1Entrada);
		
		if (!ocv.getColorDegradadoModo1())
			colorC4Entrada=colorC3Entrada;
		else
			colorC4Entrada=calcularDegradado(colorC3Entrada);
			
		color=ocv.getColorFSalida();
		colorFSalida=new Color(color[0],color[1],color[2]);

		color=ocv.getColorC1Salida();
		colorC1Salida=new Color(color[0],color[1],color[2]);
		
		if (!ocv.getColorDegradadoModo1())
			colorC2Salida=colorC1Salida;
		else
			colorC2Salida=calcularDegradado(colorC1Salida);
		
		colorC3Salida=disminuirColor(colorC1Salida);
		
		if (!ocv.getColorDegradadoModo1())
			colorC4Salida=colorC3Salida;
		else
			colorC4Salida=calcularDegradado(colorC3Salida);
		
		color=ocv.getColorCAEntrada();
		colorC1AEntrada=new Color(color[0],color[1],color[2]);
		
		if (!ocv.getColorDegradadoModo1())
			colorC2AEntrada=colorC1AEntrada;
		else
			colorC2AEntrada=calcularDegradado(colorC1AEntrada);
		
		color=ocv.getColorCASalida();
		colorC1ASalida=new Color(color[0],color[1],color[2]);
		
		if (!ocv.getColorDegradadoModo1())
			colorC2ASalida=colorC1ASalida;
		else
			colorC2ASalida=calcularDegradado(colorC1ASalida);
		
		
		color=ocv.getColorC1NCSalida();
		colorC1NCSalida=new Color(color[0],color[1],color[2]);	
		
		if (!ocv.getColorDegradadoModo1())
			colorC2NCSalida=colorC1NCSalida;
		else
			colorC2NCSalida=calcularDegradado(colorC1NCSalida);
		
		color=ocv.getColorFlecha();
		colorFlecha=new Color(color[0],color[1],color[2]);

		grosorFlecha=ocv.getGrosorFlecha();
		
		color=ocv.getColorPanel();
		colorPanel=new Color(color[0],color[1],color[2]);
	
		color=ocv.getColorActual();
		colorMarcoActual=new Color(color[0],color[1],color[2]);
		
		color=ocv.getColorCActual();
		colorMarcosCActual=new Color(color[0],color[1],color[2]);
	
		formaFlecha=ocv.getFormaFlecha();
		
		bordeCelda=ocv.getTipoBordeCelda();
		grosorMarco=ocv.getGrosorActual();
		
		sepH=ocv.getDistanciaH();
		sepV=ocv.getDistanciaV();
		
		color=ocv.getColorCodigoPR();
		colorCodigoPR=new Color(color[0],color[1],color[2]);
		
		color=ocv.getColorCodigoCo();
		colorCodigoCo=new Color(color[0],color[1],color[2]);
		
		color=ocv.getColorCodigoMF();
		colorCodigoMF=new Color(color[0],color[1],color[2]);
		
		color=ocv.getColorCodigoMB();
		colorCodigoMB=new Color(color[0],color[1],color[2]);
		
		color=ocv.getColorCodigoRC();
		colorCodigoRC=new Color(color[0],color[1],color[2]);
		
		color=ocv.getColorIluminado();
		colorIluminado=new Color(color[0],color[1],color[2]);
		
		color=ocv.getColorResaltado();
		colorResaltado=new Color(color[0],color[1],color[2]);
		
		
		
		
		zoomArbol=ocv.getZoomArbol();
		zoomPila=ocv.getZoomPila();
		
		for (int i=0; i<Conf.numColoresMetodos; i++)
		{
			int rgb[]=ocv.getColorModo2(i);
			Conf.coloresNodo[i]=new Color(rgb[0],rgb[1],rgb[2]);
		}
				
		Conf.modoColor=ocv.getModoColor();
		
		Conf.degradado1=ocv.getColorDegradadoModo1();
		Conf.degradado2=ocv.getColorDegradadoModo2();
		
		calculoColoresModo2();
	
		String nombreFuente=ocv.getFuenteCodigo();
		int tamFuente=ocv.getTamFuenteCodigo();
	
		fuenteCodigo = new Font(nombreFuente,Font.PLAIN,tamFuente);
		
		nombreFuente=ocv.getFuenteTraza();
		tamFuente=ocv.getTamFuenteTraza();
	
		fuenteTraza = new Font(nombreFuente,Font.PLAIN,tamFuente);
		

	}
	
	
	// Si este método se ejecuta en alguna parte junto a Conf.setValoresVisualizacion, 
	// Conf.setValoresOpsVisualizacion debe ejecutarse siempre antes que Conf.setValoresVisualizacion
	public static void setValoresOpsVisualizacion(boolean valoresPorDefecto)
	{
		OpcionOpsVisualizacion oov;
		oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",valoresPorDefecto);
	
		Conf.elementosVisualizar_ant=Conf.elementosVisualizar;
		Conf.historia_ant=Conf.historia;
		
		if (oov.getDatosMostrar()==OpcionOpsVisualizacion.DATOS_TODOS)
			Conf.elementosVisualizar=Conf.VISUALIZAR_TODO;

		else if (oov.getDatosMostrar()==OpcionOpsVisualizacion.DATOS_ENTRADA)
			Conf.elementosVisualizar=Conf.VISUALIZAR_ENTRADA;

		else if (oov.getDatosMostrar()==OpcionOpsVisualizacion.DATOS_SALIDA)
			Conf.elementosVisualizar=Conf.VISUALIZAR_SALIDA;

		if (oov.getHistoria()==OpcionOpsVisualizacion.MANTENER_HISTORIA)
			Conf.historia=0;

		else if (oov.getHistoria()==OpcionOpsVisualizacion.ATENUAR_HISTORIA)
			Conf.historia=1;

		else if (oov.getHistoria()==OpcionOpsVisualizacion.ELIMINAR_HISTORIA)
			Conf.historia=2;
			
		
		Conf.mostrarArbolSalto=oov.getMostrarArbolSalto();
		
		Conf.mostrarVisor=oov.getMostrarVisor();
		
		Conf.mostrarEstructuraCompleta=oov.getMostrarEstructuraCompletaCrono();
		
		Conf.mostrarSalidaLigadaEntrada=oov.getMostrarSalidaLigadaEntrada();

		Conf.mostrarEstructuraEnArbol=oov.getMostrarEstructuraEnArbol();
		
		Conf.mostrarArbolColapsado=oov.getMostrarArbolColapsado();
		
		Conf.sangrado=oov.getSangrado();
		
		Conf.idMetodoTraza=oov.getIdMetodoTraza();
		
		Conf.soloEstructuraDYVcrono=oov.getSoloEstructuraDYVcrono();
		
		Conf.arranqueEstadoInicial=oov.getArranqueEstadoInicial();
		
		Conf.ajustarVistaGlobal=oov.getAjustarVistaGlobal();
		
		Conf.visualizacionDinamica=oov.getVisualizacionDinamica();
	}
	
	// La clase OpcionConfVisualizacion llamará a este método para cargar valores por defecto de colores de modo 2
	public static int[] getRojoDefecto()
	{
		int[] arrayRojo={  0,100,  0,200,  0,185,225, 85,115,125};
		return arrayRojo;
	}
	
	// La clase OpcionConfVisualizacion llamará a este método para cargar valores por defecto de colores de modo 2
	public static int[] getVerdeDefecto()
	{
		int[] arrayVerde={110,200,100,  0,150,185,100,  0, 80,125};
		return arrayVerde;
	}
	
	// La clase OpcionConfVisualizacion llamará a este método para cargar valores por defecto de colores de modo 2
	public static int[] getAzulDefecto()
	{
		int[] arrayAzul={110,200,185,  0,  0,  0,  0,215, 50,125};
		return arrayAzul;
	}
	
	
	public static void setConfiguracionIdioma()
	{
		// Idioma
		OpcionIdioma oi;
		oi=(OpcionIdioma)gOpciones.getOpcion("OpcionIdioma",true);
		
		idioma=oi.get();
		
	}
	
	
	public static void setConfiguracionVistas()
	{
		// Vistas
		OpcionVistas ov;
		ov=(OpcionVistas)gOpciones.getOpcion("OpcionVistas",true);
		
		vistas=ov.getVistas();
		disposicionPaneles=ov.getDisposicion();
		
	}
	
	
	
	public static void setFicheros()
	{
		// Fichero LOG
		OpcionBorradoFicheros obf;
		obf=(OpcionBorradoFicheros)gOpciones.getOpcion("OpcionBorradoFicheros",true);
		
		fichero_log=obf.getLOG();
		
		
		
		
	}
	
	public static Vista getVista(String codigo)
	{
		for (int i=0; i<vistas.length; i++)
		{
			if (vistas[i].getCodigo().equals(codigo))
				return vistas[i];
		}
		return null;
	}



	public static void calcularDegradadosEstructura(int niveles, boolean forzar)
	{
		if (forzar || colorC1Eant==null || coloresDistintos(colorC1Eant,colorC1Entrada) || coloresDistintos(colorC1Sant,colorC1Salida) 
							 || coloresDistintos(colorC2Eant,colorC1AEntrada) || coloresDistintos(colorC2Sant,colorC1ASalida) 
		)
		{
			if (niveles!=1)
			{
				degradadosEstructuraEC1=new Color[niveles+1];
				degradadosEstructuraSC1=new Color[niveles+1];
				
				for (int i=1; i<=niveles; i++)
				{
					int r,g,b;
					int rgb[];
					
					if ( colorC1Entrada.getRed() > colorC1AEntrada.getRed() )
						r= colorC1AEntrada.getRed() +  ( (((colorC1Entrada.getRed()-colorC1AEntrada.getRed())/(niveles-1))) *(i-1));
					else
						r= colorC1AEntrada.getRed() -  ( ((colorC1AEntrada.getRed()-colorC1Entrada.getRed())/(niveles-1)) *(i-1));
				
					if ( colorC1Entrada.getGreen() > colorC1AEntrada.getGreen() )
						g= colorC1AEntrada.getGreen() +  ( ((colorC1Entrada.getGreen()-colorC1AEntrada.getGreen())/(niveles-1)) *(i-1));
					else
						g= colorC1AEntrada.getGreen() -  ( ((colorC1AEntrada.getGreen()-colorC1Entrada.getGreen())/(niveles-1)) *(i-1));
						
					if ( colorC1Entrada.getBlue() > colorC1AEntrada.getBlue() )
						b= colorC1AEntrada.getBlue() +  ( ((colorC1Entrada.getBlue()-colorC1AEntrada.getBlue())/(niveles-1)) *(i-1));
					else
						b= colorC1AEntrada.getBlue() -  ( ((colorC1AEntrada.getBlue()-colorC1Entrada.getBlue())/(niveles-1)) *(i-1));

					rgb=corregirColor(r,g,b);
					degradadosEstructuraEC1[i]=new Color(rgb[0],rgb[1],rgb[2]);
					//System.out.println("degradadosEstructuraEC1["+i+"]= "+rgb[0]+"."+rgb[1]+"."+rgb[2]);
						
					if ( colorC1Salida.getRed() > colorC1ASalida.getRed() )
						r= colorC1ASalida.getRed() +  ( ((colorC1Salida.getRed()-colorC1ASalida.getRed())/(niveles)) *(i-1));
					else
						r= colorC1ASalida.getRed() -  ( ((colorC1ASalida.getRed()-colorC1Salida.getRed())/(niveles)) *(i-1));
				
					if ( colorC1Salida.getGreen() > colorC1ASalida.getGreen() )
						g= colorC1ASalida.getGreen() +  ( ((colorC1Salida.getGreen()-colorC1ASalida.getGreen())/(niveles)) *(i-1));
					else
						g= colorC1ASalida.getGreen() -  ( ((colorC1ASalida.getGreen()-colorC1Salida.getGreen())/(niveles)) *(i-1));
						
					if ( colorC1Salida.getBlue() > colorC1ASalida.getBlue() )
						b= colorC1ASalida.getBlue() +  ( ((colorC1Salida.getBlue()-colorC1ASalida.getBlue())/(niveles)) *(i-1));
					else
						b= colorC1ASalida.getBlue() -  ( ((colorC1ASalida.getBlue()-colorC1Salida.getBlue())/(niveles)) *(i-1));

					rgb=corregirColor(r,g,b);
					degradadosEstructuraSC1[i]=new Color(rgb[0],rgb[1],rgb[2]);
					//System.out.println("degradadosEstructuraSC1["+i+"]= "+rgb[0]+"."+rgb[1]+"."+rgb[2]);
					
					// Actualizamos almacén de "colores anteriores" para poder comprobar si se han hecho cambios en colores actuales
					// y evitarnos en caso negativo tener que recalcular todo a cada paso de la visualización.
					colorC1Eant=new Color(colorC1Entrada.getRed(), colorC1Entrada.getGreen(), colorC1Entrada.getBlue());
					colorC1Sant=new Color(colorC1Salida.getRed(),  colorC1Salida.getGreen(),  colorC1Salida.getBlue());
					colorC2Eant=new Color(colorC1AEntrada.getRed(), colorC1AEntrada.getGreen(), colorC1AEntrada.getBlue());
					colorC2Sant=new Color(colorC1ASalida.getRed(),  colorC1ASalida.getGreen(),  colorC1ASalida.getBlue());
				}
			}
			else // (niveles == 1)
			{
				degradadosEstructuraEC1=new Color[2];
				degradadosEstructuraSC1=new Color[2];
				
				degradadosEstructuraEC1[0]=new Color(colorC1AEntrada.getRed(),colorC1AEntrada.getGreen(),colorC1AEntrada.getBlue());
				degradadosEstructuraEC1[1]=new Color(colorC1AEntrada.getRed(),colorC1AEntrada.getGreen(),colorC1AEntrada.getBlue());
				degradadosEstructuraSC1[0]=new Color(colorC1ASalida.getRed(),colorC1ASalida.getGreen(),colorC1ASalida.getBlue());
				degradadosEstructuraSC1[1]=new Color(colorC1ASalida.getRed(),colorC1ASalida.getGreen(),colorC1ASalida.getBlue());
			}
		}
	}

	private static boolean coloresDistintos(Color c1, Color c2)
	{
		return c1.getRed()!=c2.getRed() ||
				c1.getGreen()!=c2.getGreen() ||
				c1.getBlue()!=c2.getBlue() ;
	}
	
	private static int [] corregirColor (int r, int g, int b)
	{
		if (r<0)
			r=0;
		else if (r>255)
			r=255;
			
		if (g<0)
			g=0;
		else if (g>255)
			g=255;	
			
		if (b<0)
			b=0;
		else if (b>255)
			b=255;		
			
		int [] retorno=new int[3];
		retorno[0]=r;
		retorno[1]=g;
		retorno[2]=b;
		return retorno;
	}
	
	
	
	public static void setTamanoVentana(int ancho,int alto)
	{
		Conf.anchoVentana=ancho;
		Conf.altoVentana=alto;
	}
	
	public static int[] getTamanoVentana()
	{
		int tam[]=new int[2];
		tam[0]=Conf.anchoVentana;
		tam[1]=Conf.altoVentana;
		return tam;
	}

	
	public static void setTamanoMonitor()
	{
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension tamanoVentana=tk.getScreenSize();
		Conf.setTamanoMonitor((int)tamanoVentana.getWidth(),(int)tamanoVentana.getHeight());
	}
	

	public static void setTamanoMonitor(int ancho,int alto)
	{
		Conf.anchoPantalla=ancho;
		Conf.altoPantalla=alto;
	}
	
	public static int[] getTamanoMonitor()
	{
		Conf.setTamanoMonitor();	// Para que devuelva siempre valores actualizados
		int coordenadas[]=new int[2];
		coordenadas[0]=Conf.anchoPantalla;
		coordenadas[1]=Conf.altoPantalla;
		
		return coordenadas;
		
	}
	
	public static int[] ubicarCentro(int ancho, int alto)
	{
		int coordenadas[]=new int[2];
		coordenadas[0]=(Conf.anchoPantalla-ancho)/2;
		coordenadas[1]=(Conf.altoPantalla-alto)/2;
		return coordenadas;
	}
	
	
	
	public static int[] calcularDegradado(int r, int g, int b)
	{
		int umbral=75;
		int rgbdegradado[]=new int[3];
		
		if (r<umbral && g<umbral && b<umbral)
		{
			rgbdegradado[0]= r+umbral;
			rgbdegradado[1]= g+umbral;
			rgbdegradado[2]= b+umbral;
		}
		else
		{
			rgbdegradado[0]= r>umbral ? r-umbral : 0;
			rgbdegradado[1]= g>umbral ? g-umbral : 0;
			rgbdegradado[2]= b>umbral ? b-umbral : 0;
		}
		
		return rgbdegradado;
	}
	
	public static Color disminuirColor(Color color)
	{
		if (color==null)
			return null;
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int umbral=30;
		int rgbdegradado[]=new int[3];
		rgbdegradado[0]= r<255-umbral ? r+umbral : 255;
		rgbdegradado[1]= g<255-umbral ? g+umbral : 255;
		rgbdegradado[2]= b<255-umbral ? b+umbral : 255;
		return new Color(rgbdegradado[0], rgbdegradado[1], rgbdegradado[2]);
	}
	
	

	public static Color calcularDegradado(Color color)
	{
		if (color==null)
			return null;
		
		int []tonos=calcularDegradado(color.getRed(), color.getGreen(), color.getBlue());
		
		return new Color(tonos[0],tonos[1],tonos[2]);
	}
	
	// El color que calcula está a una cuarta parte del color 2 y a tres cuartas partes del color 1
	public static Color calcularColorIntermedio(Color color1, Color color2)
	{
		if (color1==null || color2==null)
			return null;
		
		int []tonos=calcularColorIntermedio(	color1.getRed(), color1.getGreen(), color1.getBlue(),
											color2.getRed(), color2.getGreen(), color2.getBlue());
		
		return new Color(tonos[0],tonos[1],tonos[2]);
	}
	
	
	public static int[] calcularColorIntermedio(int r1, int g1, int b1,int r2, int g2, int b2)
	{
		int rgbmedio[]=new int[3];
		
		
		
		rgbmedio[0]=tonoIntermedio(r1,r2,medidaColorIntermedio);
		rgbmedio[1]=tonoIntermedio(g1,g2,medidaColorIntermedio);
		rgbmedio[2]=tonoIntermedio(b1,b2,medidaColorIntermedio);
		
		return rgbmedio;
	}
	
	
	private static int tonoIntermedio(int t1, int t2, int medida)
	{
		int dist;
		if (t1>t2)
		{
			dist=(t1-t2)/medida;
			return t2+dist;			
		}
		else
		{
			dist=(t2-t1)/medida;
			return t2-dist;	
		}
	}
	
	
	/*public static Color calcularColorMedio(Color color1, Color color2)
	{
		if (color1==null || color2==null)
			return null;
		
		int []tonos=calcularColorMedio(	color1.getRed(), color1.getGreen(), color1.getBlue(),
										color2.getRed(), color2.getGreen(), color2.getBlue());
		
		return new Color(tonos[0],tonos[1],tonos[2]);
	}
	
	
	public static int[] calcularColorMedio(int r1, int g1, int b1,int r2, int g2, int b2)
	{
		int rgbmedio[]=new int[3];
		
		rgbmedio[0]=(r1+r2)/2;
		rgbmedio[1]=(g1+g2)/2;
		rgbmedio[2]=(b1+b2)/2;
		
		return rgbmedio;
	}*/
	
	
	// Variables temporales de estado (flags)
	public static void setRedibujarGrafoArbol(boolean redibujar)
	{
		redibujarGrafoArbol=redibujar;
	}
	
	public static boolean getRedibujarGrafoArbol()
	{
		return redibujarGrafoArbol;
	}
	
	
	public static void setPanelArbolReajustado(boolean ajuste)
	{
		panelArbolReajustado=ajuste;
	}
	
	public static boolean getPanelArbolReajustado()
	{
		return panelArbolReajustado;
	}	
	
	public static void setHaciendoAjuste(boolean valor)
	{
		haciendoAjuste=valor;
	}
	
	public static boolean getHaciendoAjuste()
	{
		return haciendoAjuste;
	}
	
	
}
