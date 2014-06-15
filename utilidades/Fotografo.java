package utilidades;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

import javax.swing.JComponent;

import org.jgraph.JGraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import gif.*;
import conf.*;
import cuadros.*;
import utilidades.*;
import ventanas.*;

public class Fotografo
{
	
	static int posicInicialVentanaX=0;
	static int posicInicialVentanaY=0;
	
	static int tamVentanaX=0;
	static int tamVentanaY=0;
	
	static int tamPantallaX=0;
	static int tamPantallaY=0;
	
	
	public Fotografo (int posicInicialVentanaX0, int posicInicialVentanaY0, 
						int tamVentanaX0, int tamVentanaY0, 
						int tamPantallaX0, int tamPantallaY0)
	{
		posicInicialVentanaX=Math.max(0,posicInicialVentanaX0);
		posicInicialVentanaY=Math.max(0,posicInicialVentanaY0);
		tamVentanaX=Math.min(tamVentanaX0,tamPantallaX0-posicInicialVentanaX);
		tamVentanaY=Math.min(tamVentanaY0,tamPantallaY0-posicInicialVentanaY);
		tamPantallaX=tamPantallaX0;
		tamPantallaY=tamPantallaY0;
	}
	
	public static void foto(int numC)
	{
		String numCaptura=numC+"";
		if (numCaptura.length()==1)
			numCaptura="000"+numCaptura;
		else if (numCaptura.length()==2)
			numCaptura="00"+numCaptura;
		else if (numCaptura.length()==3)
			numCaptura="0"+numCaptura;
	
		Robot robot=null;
		try {
			robot=new Robot();
		} catch (java.awt.AWTException awte) {
			System.out.println("ERROR IMAGEN CAPTURA 1");
		}
		
	
		BufferedImage capturaInicial=robot.createScreenCapture(new Rectangle(tamPantallaX,tamPantallaY)); 
		
		BufferedImage imagenSRec = new BufferedImage( tamVentanaX , tamVentanaY , BufferedImage.TYPE_INT_RGB  );
		
		for (int i=0; i<imagenSRec.getWidth(); i++)
			for (int j=0; j<imagenSRec.getHeight(); j++)
			{
				//System.out.println("i="+i+" j="+j);
				imagenSRec.setRGB(i,j, capturaInicial.getRGB(i+posicInicialVentanaX,j+posicInicialVentanaY));
			}
			
		
		
		File outputFile = new File("imagen_SRec_"+numCaptura+".gif");
		try {
			ImageIO.write(imagenSRec, "GIF", outputFile);
		} catch (java.io.IOException ioe) {
			System.out.println("ERROR IMAGEN CAPTURA 2");
		}
	}
	
	public static void foto(int numC, int tipo, String destino)	// tipo: 	0=png 	1=gif 	2=jpg	
	{
		String numCaptura=numC+"";
		if (numCaptura.length()==1)
			numCaptura="000"+numCaptura;
		else if (numCaptura.length()==2)
			numCaptura="00"+numCaptura;
		else if (numCaptura.length()==3)
			numCaptura="0"+numCaptura;
	
		Robot robot=null;
		try {
			robot=new Robot();
		} catch (java.awt.AWTException awte) {
			System.out.println("ERROR IMAGEN CAPTURA 1");
		}
		
		BufferedImage capturaInicial=robot.createScreenCapture(new Rectangle(tamPantallaX,tamPantallaY)); 
		BufferedImage imagenSRec = new BufferedImage( tamVentanaX , tamVentanaY , BufferedImage.TYPE_INT_RGB  );
		
		for (int i=0; i<imagenSRec.getWidth(); i++)
			for (int j=0; j<imagenSRec.getHeight(); j++)
			{
				//System.out.println("i="+i+" j="+j);
				imagenSRec.setRGB(i,j, capturaInicial.getRGB(i+posicInicialVentanaX,j+posicInicialVentanaY));
			}
		
		File outputFile;
		if (destino==null)
			outputFile = new File("imagen_SRec_"+numCaptura+".gif");
		else
			outputFile = new File(destino);
		
		
		
		
		try {
			if (tipo==1)
				ImageIO.write(imagenSRec, "gif", outputFile);
			else if (tipo==2)
				ImageIO.write(imagenSRec, "jpg", outputFile);
			else
				ImageIO.write(imagenSRec, "png", outputFile);
				
		} catch (java.io.IOException ioe) {
			System.out.println(" java.io.IOException (tipo="+tipo+")");
		}
		
		
		
		/*if (tipo==1)
		{
			try {
				ImageIO.write(imagenSRec, "GIF", outputFile);
			} catch (java.io.IOException ioe) {
				System.out.println("ERROR IMAGEN CAPTURA 2");
			}
		}
		else if (tipo==2 || tipo==0)
		{
			try {
				if (tipo==2)
					ImageIO.write(imagenSRec,"jpg",outputFile);
				else
					ImageIO.write(imagenSRec,"png",outputFile);
			} catch (java.io.IOException ioe) {
				System.out.println(" java.io.IOException JPG _ PNG");
			}
		}*/
	}	
	
	public static void crearGIFAnimado(int numFotos, String archivo, boolean gifPila)
	{
		CuadroProgreso cp=null;
		
		if (numFotos>1)
			cp=new CuadroProgreso(Ventana.thisventana,Texto.get("CP_ESPERE",Conf.idioma),
												Texto.get("CP_PROCES",Conf.idioma),0);
	
	
		// Cargamos esas numFotos capturas
		BufferedImage bi[]=new BufferedImage[numFotos];
		
		for (int i=0; i<numFotos; i++)
		{
			try {
				//bi[i]	= is.read(BufferedImage.TYPE_BYTE_INDEXED);
				if (i<10)
					bi[i]=ImageIO.read(new File("imagen_SRec_000"+i+".gif"));
				else if (i<100)
					bi[i]=ImageIO.read(new File("imagen_SRec_00"+i+".gif"));
				else if (i<1000)
					bi[i]=ImageIO.read(new File("imagen_SRec_0"+i+".gif"));	
				else
					bi[i]=ImageIO.read(new File("imagen_SRec_"+i+".gif"));
			} catch (java.io.IOException ioe) {
			}
		}
		
		/*En el caso de la vista de pila, los tamaños de las imagenes varian, y el gif coge el tamaño de la primera image.
	 	 *Por lo tanto debemos poner una primera imagen que tenga el tamaño máximo de las imagenes. */
		BufferedImage blanco = null;
		if (gifPila) {
			//Buscamos imagen más alta y la mas ancha
			int maxAlto = -1;
			int indiceMaxAlto = -1;
			int maxAncho = -1;
			int indiceMaxAncho = -1;
			for (int i=0; i<numFotos; i++) {
				if (bi[i].getHeight()>maxAlto) {
					maxAlto = bi[i].getHeight();
					indiceMaxAlto = i;
				}
				if (bi[i].getWidth()>maxAncho) {
					maxAncho = bi[i].getWidth();
					indiceMaxAncho = i;
				}
			}
			//Creamos imagen blanca
			blanco = new BufferedImage(bi[indiceMaxAncho].getWidth(), bi[indiceMaxAlto].getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		
		
		OutputStream	os	=null;
		try {
			os = new FileOutputStream(archivo);
		} catch (java.io.FileNotFoundException nfne) {
		}
		AnimatedGifEncoder age=new AnimatedGifEncoder();
		
		//System.out.println("START="+age.start(os));
		age.start(os);

		if (gifPila) {
			age.setTransparent(new Color(0,0,0));
			age.addFrame(blanco);
		}
		
		for (int i=0; i<numFotos; i++)
		{
			age.addFrame(bi[i]);
			if (i==0)
			{
				age.setDelay(1000);
				age.addFrame(bi[i]);
			}
			//System.out.println("ADD("+i+")="+age.addFrame(bi[i]));
			age.setDelay(1000);
			if (cp!=null)
				cp.setValores(Texto.get("CP_PROCES",Conf.idioma),(i*100)/numFotos);	
		}
		age.setRepeat(5);
		age.finish();
		//System.out.println("FINISH="+age.finish());
		try {
			os.flush();
		} catch(java.io.IOException ioe) {
		}
		try { os.close(); } catch(Exception x) {}
		if (cp!=null)
			cp.cerrar();		
				
	}
	
	
	public static int numFormato(String s)
	{
		if (s.toLowerCase().contains(".gif"))
			return 1;
		else if (s.toLowerCase().contains(".jpg") || s.toLowerCase().contains(".jpeg"))
			return 2;
		else if (s.toLowerCase().contains(".png"))
			return 0;
		else
			return -1;
	}
	
	
	public static void guardarFoto(JComponent c, int num)
	{
		// Nombre de la imagen
		String numCaptura=num+"";
		if (numCaptura.length()==1)
			numCaptura="000"+numCaptura;
		else if (numCaptura.length()==2)
			numCaptura="00"+numCaptura;
		else if (numCaptura.length()==3)
			numCaptura="0"+numCaptura;
	
		guardarFoto(c,1,"imagen_SRec_"+numCaptura+".gif");
	}
	
	public static void guardarFoto(JGraph c, int num, int[] dim)
	{
		// Nombre de la imagen
		String numCaptura=num+"";
		if (numCaptura.length()==1)
			numCaptura="000"+numCaptura;
		else if (numCaptura.length()==2)
			numCaptura="00"+numCaptura;
		else if (numCaptura.length()==3)
			numCaptura="0"+numCaptura;
	
		guardarFoto(c,1,"imagen_SRec_"+numCaptura+".gif",dim);
	}

	public static void guardarFoto(JComponent c, String nombreFicheroGenerico, int tipo,int num)
	{
		// Nombre de la imagen
		String numCaptura=num+"";
		if (numCaptura.length()==1)
			numCaptura="000"+numCaptura;
		else if (numCaptura.length()==2)
			numCaptura="00"+numCaptura;
		else if (numCaptura.length()==3)
			numCaptura="0"+numCaptura;
	
		String extension;
		if (tipo==1)
			extension=".gif";
		else if (tipo==2)
			extension=".jpg";
		else
			extension=".png";

		guardarFoto(c,1,nombreFicheroGenerico+"_"+numCaptura+extension);
	}
	
	
	
	
	
	public static void guardarFoto(JComponent c, int tipo, String nombre)
	{
		int[]dim=new int[2];
		dim[0]=c.getWidth();
		dim[1]=c.getHeight();
		System.out.println("c.getWidth="+dim[0]+" c.getHeight()="+dim[1]);
		guardarFoto(c, tipo, nombre,dim);
	}
	
	
	public static void guardarFoto(JComponent c, int tipo, String nombre,int []dim)
	{
		Image img=null;
		BufferedImage bi;
		
		//System.out.println("dim[0]="+dim[0]+" dim[1]="+dim[1]);
		if (c instanceof JGraph)
		{
			//System.out.println("guardarFoto: JGraph color de fondo "+c.getBackground().getRed()+", "+c.getBackground().getGreen()+",  "+c.getBackground().getBlue());
			bi = ((JGraph)c).getImage(c.getBackground(), 0);
		}
		else
		{
			//System.out.println("guardarFoto: "+c.getClass().getCanonicalName());
			img = c.createImage(	dim[0], dim[1]	);
			Graphics imgG = img.getGraphics();
			c.paint(imgG);
			
			bi=new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);
			
		}
		bi.getGraphics().drawImage(img, 0, 0, null);
		bi.getGraphics().dispose();
		
		// Guardamos la imagen
		File f=new File(nombre);
		
		try {
			if (tipo==1)
				ImageIO.write(bi, "GIF", f);
			else if (tipo==2)
				ImageIO.write(bi, "JPG", f);
			else
				ImageIO.write(bi, "PNG", f);
				
		} catch (java.io.IOException ioe) {
		}
	}
	
}