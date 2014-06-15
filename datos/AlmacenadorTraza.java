/**
	Almacena trazas guardadas como objeto Traza en un fichero XMl para su posterior recuperación
	
	@author Antonio Pérez Carrasco
	@version 2006-2008
*/
package datos;





import java.io.File;




import javax.swing.JFrame;

import org.w3c.dom.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import conf.*;
import cuadros.*;
import opciones.*;
import utilidades.*;
import ventanas.*;


public class AlmacenadorTraza 
{
	String fichero[] = new String[2];
	Ventana vv;
	boolean depurar=false;  			// Depurar

	GestorOpciones gOpciones=new GestorOpciones();
	OpcionFicherosRecientes ofr;
	OpcionOpsVisualizacion oov;
	OpcionConfVisualizacion ocv;
	
	CuadroProgreso cuadroProgreso;
	CuadroError cuadroError; 
	Document documento;
	
	static JFrame ventana;
	
	Traza traza;
	Traza trazaCompleta;
	DatosTrazaBasicos dtb;
	String titulo;

	/**
		Constructor: crea un nuevo gestor para el almacenamiento de trazas en formato XML
	*/
	public AlmacenadorTraza(Traza traza, Traza trazaCompleta, DatosTrazaBasicos dtb)
	{
		this.vv=Ventana.thisventana;
		this.traza=traza;
		this.trazaCompleta=trazaCompleta;
		this.dtb=dtb;
		
		if (this.traza==null && depurar)		
		{
			System.out.println("traza=null!!!!");
		}
		
		// Cargamos opción de ficheros recientes (para saber último directorio)
		ofr=(OpcionFicherosRecientes)gOpciones.getOpcion("OpcionFicherosRecientes",true);
		this.fichero[0]=ofr.getDirXML();
		
		this.fichero=SelecDireccion.cuadroAbrirFichero(this.fichero[0],Texto.get("CA_GUARDVIS",Conf.idioma),
							null,"xml",Texto.get("ARCHIVO_XML",Conf.idioma),0);

							
		// *1* Comprobar que fichero existe
		File f=new File(this.fichero[0]+this.fichero[1]);
		if (!f.exists())
		{
			if (depurar)	System.out.println("AlmacenadorTraza -- antes de 'ejecutar");
			ejecutar();
			if (depurar)	System.out.println("AlmacenadorTraza -- despues de 'ejecutar'");
		}
		else
		{
			new CuadroPreguntaSobreescribir (this.vv,this);
		}
		
		
	}

	public synchronized void ejecutar()
	{
		if (depurar)	System.out.println("AlmacenadorTraza 01");
		
		if (fichero!=null && fichero[1]!=null)
		{
			ofr.setDirXML(fichero[0]);
			gOpciones.setOpcion(ofr,2);
		
			cuadroProgreso = new CuadroProgreso(this.vv,Texto.get("CP_ESPERE",Conf.idioma),Texto.get("CP_ACTUAL",Conf.idioma),0);
		
			if (!fichero[1].toLowerCase().contains(".xml"))
				fichero[1]=fichero[1]+".xml";
			

			if (depurar)	System.out.println("AlmacenadorTraza 02");
			// Generamos fichero con la estructura básica, la rellenaremos después
			cuadroProgreso.setValores(Texto.get("CP_ACTUAL",Conf.idioma),10);	
		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder =null;
			Document documento =null;
			
			try{
				builder = factory.newDocumentBuilder();
				documento = builder.newDocument();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (depurar)	System.out.println("AlmacenadorTraza 03");
			
			Element elemV=documento.createElement("Visualizacion");
			Element elem1=documento.createElement("OpConf");
			Element elem2=documento.createElement("OpFormato");
			Element elem3=documento.createElement("DatosVisibilidad");
			Element elem4=documento.createElement("Traza");
			Element elem5=documento.createElement("TrazaCompleta");
			
			elemV.appendChild(elem1);
			elemV.appendChild(elem2);
			elemV.appendChild(elem3);
			elemV.appendChild(elem4);
			elemV.appendChild(elem5);
			documento.appendChild(elemV);
			
			if (depurar)	System.out.println("AlmacenadorTraza 04");
			
			Element nodos[]=ManipulacionElement.getChildElements(documento.getDocumentElement());
				
				// nodos[0]=<OpConf>  nodos[1]=<OpFormato>  nodos[2]=<DatosVisibilidad>  nodos[3]=<Traza>  nodos[4]=<TrazaCompleta>
				
			
			// Almacenamos los valores de las opciones de configuración de la visualización
				// Primero cargamos la opción desde fichero para obtener valores actualizados
			
			cuadroProgreso.setValores(Texto.get("CP_ESCOP",Conf.idioma),20);
			
			if (depurar)	System.out.println("AlmacenadorTraza 05");
			
			oov=(OpcionOpsVisualizacion)gOpciones.getOpcion("OpcionOpsVisualizacion",false);
			ocv=(OpcionConfVisualizacion)gOpciones.getOpcion("OpcionConfVisualizacion",false);
			
			generarValoresOpConf(documento,nodos[0]);
			
			// Almacenamos los valores del formato de la visualización
				// Generamos los valores de las opciones de configuración
			
			if (depurar)	System.out.println("AlmacenadorTraza 06");
			
			cuadroProgreso.setValores(Texto.get("CP_ESCOP",Conf.idioma),35);
			
			generarValoresOpFormato(documento,nodos[1]);
			generarDatosVisibilidad(documento,nodos[2]);
			
			if (depurar)	System.out.println("AlmacenadorTraza 07");
			
			// Almacenamos los valores de la traza
			cuadroProgreso.setValores(Texto.get("CP_ESCTR",Conf.idioma),65);
			generarTraza(this.traza,documento,nodos[3]);
			generarTraza(this.trazaCompleta,documento,nodos[4]);
			
			// Guardamos la estructura Document al fichero
			cuadroProgreso.setValores(Texto.get("CP_ESCFI",Conf.idioma),90);
			ManipulacionElement.writeXmlFile(documento,fichero[0]+fichero[1]);
			cuadroProgreso.cerrar();
			if (depurar)	System.out.println("AlmacenadorTraza 08");

			this.vv.setAnimacionPendienteGuardar(false);
			
			if (depurar)	System.out.println("AlmacenadorTraza 09");
		}
	}
	
	
	/**
		Creamos un elemento del arbol DOM que contiene los valores de las opciones
		
		@param d Árbol DOM que contendrá toda la información sobre la traza y que será volcado al XML
		@param e Elemento al que se añadirán los valores insertados
	*/
	private void generarValoresOpConf(Document d,Element e)
	{
		Element datosMostrar = d.createElement("DatosMostrar");
		e.appendChild(datosMostrar);
		datosMostrar.setAttribute("entrada",""+oov.mostrarEntrada());
		datosMostrar.setAttribute("salida",""+oov.mostrarSalida());
		
		Element mostrarHistoria = d.createElement("MostrarHistoria");
		e.appendChild(mostrarHistoria);
		mostrarHistoria.setAttribute("estadoHistoria",""+oov.getHistoriaString());
		
		Element mostrarArbolSalto = d.createElement("MostrarArbolSalto");
		e.appendChild(mostrarArbolSalto);
		mostrarArbolSalto.setAttribute("mostrarArbol",""+oov.getMostrarArbolSalto());
		
		Element mostrarEstructuraEnArbol = d.createElement("MostrarEstructuraEnArbol");
		e.appendChild(mostrarEstructuraEnArbol);
		mostrarEstructuraEnArbol.setAttribute("mostrarEstructura",""+oov.getMostrarEstructuraEnArbol());
		
		
	}
	
	/**
		Creamos un elemento del arbol DOM que contiene los valores de las opciones de formato
		
		@param d Árbol DOM que contendrá toda la información sobre la traza y que será volcado al XML
		@param e Elemento al que se añadirán los valores insertados
	*/	
	private void generarValoresOpFormato(Document d,Element e)
	{
		Element celda1 = d.createElement("Celda");
		e.appendChild(celda1);
		celda1.setAttribute("nombre","entrada");
		celda1.setAttribute("degradado",""+ocv.getColorDegradadoModo1());
		
		Element celda2 = d.createElement("Celda");
		e.appendChild(celda2);
		celda2.setAttribute("nombre","salida");
		celda2.setAttribute("degradado",""+ocv.getColorDegradadoModo2());
		
		Element otros = d.createElement("Otros");
		e.appendChild(otros);
				
		anadirColor(d,celda1,"fuente",ocv.getColorFEntrada());
		anadirColor(d,celda1,"color1",ocv.getColorC1Entrada());
		anadirColor(d,celda1,"color1a",ocv.getColorCAEntrada());
		
		anadirColor(d,celda2,"fuente",ocv.getColorFSalida());
		anadirColor(d,celda2,"color1",ocv.getColorC1Salida());
		anadirColor(d,celda2,"color1a",ocv.getColorCASalida());
		anadirColor(d,celda2,"color1nc",ocv.getColorC1NCSalida());
		
		
		for (int i=0; i<Conf.numColoresMetodos; i++)
			anadirColor(d,otros, "ecm2_"+i, ocv.getColorModo2(i));
		
		
		anadirColor(d,otros, "flecha",ocv.getColorFlecha());
		anadirColor(d,otros, "panel",ocv.getColorPanel());
		anadirColor(d,otros, "marcoActual",ocv.getColorActual());
		anadirColor(d,otros, "caminoActual", ocv.getColorCActual());
		
		anadirNodo (d,otros, "Grosor", "flecha", ocv.getGrosorFlecha());
		anadirNodo (d,otros, "Grosor", "marcoActual", ocv.getGrosorActual());
		anadirNodo (d,otros, "Distancia", "vertical", ocv.getDistanciaV());
		anadirNodo (d,otros, "Distancia", "horizontal", ocv.getDistanciaH());
		anadirNodo (d,otros, "Tipo", "bordeCelda", ocv.getTipoBordeCelda());
		anadirNodo (d,otros, "Tipo", "formaFlecha", ocv.getFormaFlecha());
		
		anadirColor(d,otros, "codigoPR", ocv.getColorCodigoPR());
		anadirColor(d,otros, "codigoCo", ocv.getColorCodigoCo());
		anadirColor(d,otros, "codigoMF", ocv.getColorCodigoMF());
		anadirColor(d,otros, "codigoMB", ocv.getColorCodigoMB());
		anadirColor(d,otros, "codigoRC", ocv.getColorCodigoRC());

		

		
		anadirNodo (d,otros, "modoColor", "modo", ""+ocv.getModoColor());
		
		anadirNodo (d,otros, "modoColorDegr1", "degr", ""+ocv.getColorDegradadoModo1());
		anadirNodo (d,otros, "modoColorDegr2", "degr", ""+ocv.getColorDegradadoModo2());
		

		
		
		anadirNodo (d,otros, "Fuente", "trazaE", ocv.getFuenteCodigo());
		anadirNodo (d,otros, "Fuente", "trazaS", ocv.getFuenteTraza());
		anadirNodo (d,otros, "FuenteTam", "trazaE", ocv.getTamFuenteCodigo());
		anadirNodo (d,otros, "FuenteTam", "trazaS", ocv.getTamFuenteTraza());
		
		anadirNodo (d,otros, "Zoom", "arbol", ocv.getZoomArbol());
		anadirNodo (d,otros, "Zoom", "pila", ocv.getZoomPila());
		
	} 
	
	/**
		Función auxiliar para generar un elemento con la información de un color
		
		@param d Árbol DOM que contendrá toda la información sobre la traza y que será volcado al XML
		@param e Elemento al que se añadirán los valores insertados
		@param destino Nombre del elemento que tendrá el color
		@param valores repertorio de valores
	*/
	private void anadirColor(Document d, Element e,String destino, int valores[])
	{
		Element color=d.createElement("Color");
		e.appendChild(color);
		
		color.setAttribute("destino",destino);
		color.setAttribute("r",""+valores[0]);
		color.setAttribute("g",""+valores[1]);
		color.setAttribute("b",""+valores[2]);
	}
	
	/**
		Función auxiliar para generar un elemento con la información de un color
		
		@param d Árbol DOM que contendrá toda la información sobre la traza y que será volcado al XML
		@param e Elemento al que se añadirán los valores insertados
		@param nombre define el tipo de elemento
		@param destino Nombre del elemento que tendrá el valor
		@param valores repertorio de valores
	*/
	private void anadirNodo(Document d,Element e, String nombre, String destino, int valor)
	{
		Element grosor=d.createElement(nombre);
		e.appendChild(grosor);
		
		grosor.setAttribute("destino",destino);
		grosor.setAttribute("tam",""+valor);
	}
	
	/**
		Función auxiliar para generar un elemento con la información de un color
		
		@param d Árbol DOM que contendrá toda la información sobre la traza y que será volcado al XML
		@param e Elemento al que se añadirán los valores insertados
		@param nombre define el tipo de elemento
		@param destino Nombre del elemento que tendrá el valor
		@param valores repertorio de valores
	*/
	private void anadirNodo(Document d,Element e, String nombre, String destino, String valor)
	{
		Element grosor=d.createElement(nombre);
		e.appendChild(grosor);
		
		grosor.setAttribute("destino",destino);
		grosor.setAttribute("tam",valor);
	}
	
	
	
	
	
	private void generarDatosVisibilidad(Document d,Element e)
	{
		for (int i=0; i<this.dtb.getNumMetodos(); i++)
		{
			DatosMetodoBasicos dmt=this.dtb.getMetodo(i);
			int numParamE=dmt.getNumParametrosE();
			int numParamS=dmt.getNumParametrosS();
			
			
			Element metodo=d.createElement("Metodo");
			
			metodo.setAttribute("nombre",dmt.getNombre());
			metodo.setAttribute("retorno",""+dmt.esMetodoConRetorno());
			metodo.setAttribute("metodoPrincipal",""+dmt.getEsPrincipal());
			metodo.setAttribute("metodoVisible",""+dmt.getEsVisible());
			
			Element param=d.createElement("Param");
			for (int j=0; j<numParamE; j++)
			{
				Element paramE=d.createElement("ParamE");
				paramE.setAttribute("orden",""+j);
				paramE.setAttribute("nombre",dmt.getNombreParametroE(j));
				paramE.setAttribute("tipo",dmt.getTipoParametroE(j));
				paramE.setAttribute("dim",""+dmt.getDimParametroE(j));
				paramE.setAttribute("visible",""+dmt.getVisibilidadE(j));
				param.appendChild(paramE);
			}

			for (int j=0; j<numParamS; j++)
			{
				Element paramS=d.createElement("ParamS");
				paramS.setAttribute("orden",""+j);
				paramS.setAttribute("nombre",dmt.getNombreParametroS(j));
				paramS.setAttribute("tipo",dmt.getTipoParametroS(j));
				paramS.setAttribute("dim",""+dmt.getDimParametroS(j));
				paramS.setAttribute("visible",""+dmt.getVisibilidadS(j));
				param.appendChild(paramS);
			}
			metodo.appendChild(param);
			e.appendChild(metodo);
		}
	
	}
	
	
	
	
	/**
		Función auxiliar para generar un elemento con la información de una traza completa
		
		@param d Árbol DOM que contendrá toda la información sobre la traza y que será volcado al XML
		@param e Elemento al que se añadirán los valores insertados
		@param nombre define el tipo de elemento
		@param destino Nombre del elemento que tendrá el color
		@param valores repertorio de valores
	*/
	private void generarTraza(Traza trazaParaGuardar,Document d,Element e)
	{
		Element datosTraza=d.createElement("Datos");
		e.appendChild(datosTraza);
		
		//datosTraza.setAttribute("ID",""+this.traza.getID());
		datosTraza.setAttribute("nombre",trazaParaGuardar.getTitulo());
		
		
		datosTraza.setAttribute("archivo",trazaParaGuardar.getArchivo());
		datosTraza.setAttribute("idTraza",trazaParaGuardar.getIDTraza());
		datosTraza.setAttribute("metodoEjecucion",trazaParaGuardar.getNombreMetodoEjecucion());
		
		
		int []tecnicas=trazaParaGuardar.getTecnicas();
		
		datosTraza.setAttribute("tecnicaREC",""+Arrays.contiene(MetodoAlgoritmo.TECNICA_REC, tecnicas));
		datosTraza.setAttribute("tecnicaDYV",""+Arrays.contiene(MetodoAlgoritmo.TECNICA_DYV, tecnicas));
		
		anadirRegistroActivacion(d,e,trazaParaGuardar.getRaiz());
	}
	
	/**
		Función auxiliar para generar un elemento con la información de un registro de activacion
		
		@param d Árbol DOM que contendrá toda la información sobre la traza y que será volcado al XML
		@param e Elemento al que se añadirán los valores insertados
		@param ra registro de activación que contiene los datos que se guardarán en el elemento
	*/
	private void anadirRegistroActivacion(Document d, Element e, RegistroActivacion ra)
	{
		Element raElement=d.createElement("RegistroActivacion");
		
		e.appendChild(raElement);
		
		Element valor=d.createElement("Valor");
		raElement.appendChild(valor);
		
		String []parametrosE=ra.getEntradasString();
		String []parametrosS=ra.getSalidasString();
		
		String []clasesE=ra.clasesParamE();
		String []clasesS=ra.clasesParamS();
		int []dimE=ra.dimParamE();
		int []dimS=ra.dimParamS();
		
		for (int i=0; i<parametrosE.length; i++)
			valor.setAttribute("paramE"+(i+1),parametrosE[i]);
			
		for (int i=0; i<parametrosS.length; i++)
			valor.setAttribute("paramS"+(i+1),parametrosS[i]);
			
		for (int i=0; i<clasesE.length; i++)
			valor.setAttribute("tipoE"+(i+1),clasesE[i]);
			
		for (int i=0; i<clasesS.length; i++)
			valor.setAttribute("tipoS"+(i+1),clasesS[i]);
			
		for (int i=0; i<dimE.length; i++)
			valor.setAttribute("dimE"+(i+1),""+dimE[i]);
			
		for (int i=0; i<dimS.length; i++)
			valor.setAttribute("dimS"+(i+1),""+dimS[i]);
			
		
		//valor.setAttribute("salida",ra.getSalidaString());
		valor.setAttribute("entradaVisible",""+ra.entradaVisible());
		valor.setAttribute("salidaVisible",""+ra.salidaVisible());

		
		Element param=d.createElement("Param");
		raElement.appendChild(param);
		param.setAttribute("numHijos",""+ra.numHijos());
		param.setAttribute("hijoVisible",""+(ra.getHijosVisibles()-1));
		param.setAttribute("historico",""+ra.esHistorico());
		valor.setAttribute("actual",""+ra.getEsNodoActual());
		valor.setAttribute("caminoActual",""+ra.getEsCaminoActual());
		param.setAttribute("mostradoEntero",""+ra.esMostradoEntero());
		param.setAttribute("contraido",""+ra.contraido());
		param.setAttribute("inhibido",""+ra.inhibido());
		param.setAttribute("nID",""+ra.getID());
		param.setAttribute("iluminado",""+ra.estaIluminado());
		
		Element metodo=d.createElement("Metodo");
		raElement.appendChild(metodo);
		metodo.setAttribute("nombreMetodo",ra.getNombreMetodo());
		metodo.setAttribute("devuelveValor",""+ra.getDevuelveValor());
		String nombreParametros[]=ra.getNombreParametros();
		for (int i=0; i<nombreParametros.length; i++)
			metodo.setAttribute("nombreParametro"+(i+1),nombreParametros[i]);
		
		// Inicio - Almacenaje de estructura
		Element estructura=d.createElement("Estructura");
		raElement.appendChild(estructura);
		
		Estructura estr=null;
		if ((ra.getEntrada().getEstructura())!=null)
		{
			estr=new Estructura(ra.getEntrada().getEstructura());

			String claseEstructura=estr.getClase();
			
			estructura.setAttribute("tipo",claseEstructura.substring(0,claseEstructura.indexOf("[")));
			estructura.setAttribute("dim",(claseEstructura.contains("[][]") ? "2" : "1"));
			estructura.setAttribute("tam1",""+estr.dimensiones()[0]);
			if (estr.esMatriz())
				estructura.setAttribute("tam2",""+estr.dimensiones()[1]);
			
			int indices[]=ra.getEntrada().getIndices();
			String stringIndices="";
			for (int i=0; i<indices.length; i++)
			{
				stringIndices=stringIndices+indices[i];
				if (i<(indices.length-1))
					stringIndices=stringIndices+"|";
			}
				
			estructura.setAttribute("indices",stringIndices);
			estructura.setAttribute("entrada",estr.getValor());
			estructura.setAttribute("indEstructuraE",""+ra.getEntrada().getIndiceDeEstructura());
			estructura.setAttribute("indEstructuraS",""+ra.getSalida().getIndiceDeEstructura());
			estructura.setAttribute("salida",new Estructura(ra.getSalida().getEstructura()).getValor());
			
		}
		// Fin - Almacenaje de estructura
		
		
		Element hijos=d.createElement("Hijos");
		raElement.appendChild(hijos);
		for (int i=0; i<ra.numHijos(); i++)
			anadirRegistroActivacion(d,hijos,ra.getHijo(i));
	}
	

	


}
