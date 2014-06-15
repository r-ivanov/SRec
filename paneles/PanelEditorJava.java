package paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import javax.swing.JEditorPane;
import javax.swing.JPanel;

import javax.swing.JTextPane;

import java.util.HashMap;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

import javax.swing.text.MutableAttributeSet;

import conf.Conf;

import utilidades.ServiciosString;
import ventanas.Ventana;

class PanelEditorJava extends JPanel implements DocumentListener, KeyListener
{

	private String[] keyWords= new String[] {"abstract","boolean","break","byte","case","catch","char","class","const",
			"continue", "default", "do", "double", "else", "extends", "false", "final", "finally", "float", "for", "goto", "if",
			"implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", 
			"public", "return", "short", "static", "stricfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", 
			"true", "try", "void", "volatile", "while" };
	
	private char[] separators=new char[] { ' ',  '=', ',', '\n', '(', ')', ';', '[', ']', '{', '}', '!', '.', '/' };
	
	private HashMap<String, String> palabras;
	private HashMap<String, String> metodos;
	
	public static int contador=0;
	
	JTextPane areaNum;
	
	int longitudTextoInicial=-1;
	boolean construyendo=false;
	
	int numLineasAnterior=-1;
	
	DefaultStyledDocument doc;
	
	int[] cmini;	// Guarda las posiciones offset del principio de los comentarios multilinea
	int[] cmfin;	// Guarda las posiciones offset  del  final  de  los comentarios multilinea
	
	// Si la posición de caret es 0, buscamos en la última línea
	// Si no es 0, buscamos carácter fin de linea anterior y posterior, y buscamos ahí
	
	MyDocument md=new MyDocument();
	
	
	JEditorPane edit;

	public PanelEditorJava()
	{
		//edit=new JEditorPane();
		//edit.setText("probando");
		//System.out.println("probando1");
	}
	
	
	public PanelEditorJava(String texto, boolean editable)
	{
		super();
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.edit = new JEditorPane();
		this.edit.setEditorKit(new StyledEditorKit());
		this.edit.setEditable(editable);
		this.edit.setDocument(md);
		
		this.edit.setBackground(Conf.colorPanel);
		this.edit.setForeground(Conf.colorCodigoRC);
		
		this.edit.addKeyListener(this);
		
		md.addDocumentListener(this);
		
		longitudTextoInicial=texto.length();
		construyendo=true;
		this.edit.setText(texto);
		
		// Área de numero de linea
		areaNum = new JTextPane();
		areaNum.setFont(new Font("Courier New",Font.BOLD,14));		
		areaNum.setEditable(false);
		areaNum.setEnabled(false);
		try {
			areaNum.setBackground(new Color(230,230,230));
			areaNum.setForeground(new Color(50,50,50));
		} catch (Exception e) {}
			areaNum.setAlignmentX(Container.RIGHT_ALIGNMENT);
		actualizarBandaNumLinea();

		
		this.setLayout(new BorderLayout());
		this.add(this.edit,BorderLayout.CENTER);
		this.add(areaNum,BorderLayout.WEST);
		setVisible(true);
		
		metodos=new HashMap<String, String>();
		
		palabras=new HashMap<String, String>();
		for (int i=0; i<keyWords.length; i++)
			palabras.put(keyWords[i], "1");
	}

	
	public void setText(String texto)
	{
		longitudTextoInicial=texto.length();
		construyendo=true;
		this.edit.setText(texto);
		
		
	}
	
	
	public void setNombre(String n)
	{
		String ns[]=new String[1];
		ns[0]=""+n;
		this.setNombres(ns);
	}
	
	public void setNombres(String[] n)
	{
		metodos=new HashMap<String, String>();
		for (int i=0; i<n.length; i++)
			metodos.put(n[i], "2");
	}
	
	
	
	private String getpalabra(int offset, String sb)
	{
		String s="";
		
		do
		{
			s=s+sb.charAt(offset);
			offset++;
		}
		while (offset<sb.length() && !separador(sb.charAt(offset)) && !separador(sb.charAt(offset-1)));
		
		// Recogemos el comentario entero
		if (s.charAt(0)=='/')
		{
			if (offset<sb.length())
			{
				if (sb.charAt(offset)=='/')	// Si comentario de tipo '//'
				{
					do
					{
						s=s+sb.charAt(offset);
						offset++;
					}
					while (offset<sb.length() && sb.charAt(offset)!='\r' &&  sb.charAt(offset)!='\n');
				}
				else if (sb.charAt(offset)=='*')	// Si comentario de tipo '/*'
				{
					int finOffset = sb.indexOf("*/",offset)+1;
	
					do
					{
						s=s+sb.charAt(offset);
						offset++;
					}
					while (offset<=(finOffset));
				}
			}
		}
		else if (s.charAt(0)=='"')	// Recogemos la cadena de caracteres
		{
			int finOffset = sb.indexOf("\"",offset)+1;
			
			while (offset<(finOffset))
			{
				s=s+sb.charAt(offset);
				offset++;
			}
			;
		}
		
		return s;
	}
	
	private boolean separador(char c)
	{
		return c==' ' || c==';' || c=='\n' || c=='"' || c=='(' || c==')' || c=='+' || c=='-' || c=='*' || c=='/' || c=='=' || c=='&' || c=='|' || 
			c=='[' || c==']' || c=='{' || c=='}' || c==',' || c=='<' || c=='>' || c=='!' || c=='.' || c=='\r' || c=='\t';
	}
	
	
	public boolean apply(int startOffset, int endOffset, boolean analisisCompleto) throws BadLocationException
	{
		doc=(DefaultStyledDocument)edit.getDocument();
		String content=doc.getText(0,doc.getLength());
					
		int numeroCaracteres=endOffset+ServiciosString.vecesQueContiene(content,"\n")+1;
		//System.out.println("apply ("+contador+++"): Posic: "+startOffset+","+endOffset+"  ["+longitudTextoInicial+"] ["+numeroCaracteres+"] ["+construyendo+"]");
		
		if (construyendo && numeroCaracteres<longitudTextoInicial && longitudTextoInicial!=-1)
			return false;
		
		boolean parsearTodo=false;
		if (construyendo)
		{
			construyendo=false;
			parsearTodo=true;
			
			new Thread(){	// Thread para aseguarnos que queda bien coloreado al principio el texto del panel
				
				public synchronized void run()
				{
					
					try {
						wait(2000);
						apply(0,edit.getText().length()-1,true);
					} catch (Exception e) {
						//System.out.println(" removeUpdate: excepcion \n");
						e.printStackTrace();
					}
				}
			
			}.start();
			
		}
		
		boolean result=false;
		
		if (analisisCompleto)
			parsearTodo=true;
		
		content=content.toLowerCase();
		//int cnt=keyWords.length;
		//int cnts=separators.length;
		
		//boolean encontradoSepIni=false;
		//boolean encontradoSepFin=false;
		
		//System.out.print("  Cursor: "+edit.getCaretPosition()+"  ");
		
		//if (edit!=null)
		//	System.out.println("  Long.texto: "+edit.getText().length());
		
		
		String caracterFinLinea="\n";//System.getProperty("line.separator");
		
		if (!parsearTodo)	// Delimitamos la zona de parseo para agilizar
		{
			int cursor=this.edit.getCaretPosition();
			
			int ini=0;
			int fin=0;
			
			if (cursor==0)	// Si la posición de caret es 0, buscamos en la última línea para colorear
			{
				startOffset = content.lastIndexOf(caracterFinLinea);
			}
			else
			{
				int longTotal=content.length();
				
				ini=cursor-1;
				fin=cursor+1;
				String cadena;
				do
				{
					cadena=content.substring(ini,cursor);
					ini--;
				}
				while (!cadena.contains(caracterFinLinea) && ini>=0);
				
				cadena="";
				do
				{
					if (cadena.length()>fin)
						cadena=content.substring(cursor,fin);
					fin++;
				}
				while (!cadena.contains(caracterFinLinea) && fin<longTotal);
				
				startOffset=ini+2;
				endOffset=fin-2;
				
				if (startOffset==1)	// si quitamos este if, entonces la primera palabra no se colorea
					startOffset=0;
				
			}
			
			//System.out.println("    POSIC BUSCADA: "+startOffset+" - "+endOffset);
		}
		//else
		//	actualizarBandaNumLinea();
		//else	// Buscamos donde están los comentarios multilínea 
		//{
		//	actualizarComentariosMultilinea(edit.getText());//content);
		//}
	
		// Reinicializamos el texto que se va a analizar a negro
		MutableAttributeSet attrX=new SimpleAttributeSet();
		StyleConstants.setForeground(attrX,Color.black);
		StyleConstants.setBold(attrX,false);
		doc.setCharacterAttributes(startOffset,endOffset-startOffset,attrX,false);
		
		String c;
		if (parsearTodo)
			c=content.substring(0,content.length());
		else
			c=content.substring(startOffset,endOffset);
		
		//if (!parsearTodo)
		//	System.out.println("LINEA: ["+c+"] ("+startOffset+","+endOffset+")");
		
		//System.out.println("  ["+c+"]");
		
		int encontradoComentarioML1=-1;	// !-1 si hemos detectado ".../*...";
		int encontradoComentarioML2=-1;	// !-1 si hemos detectado "...*/...";
		//int encontradoComentarioML3=-1;	// !-1 si hemos detectado que la linea está íntegramente dentro de un comentario multilinea 
		
		boolean estamosEnComentario=false;
		
		if (!parsearTodo)
		{
			// hay que comprobar que no estén escritos dentro de una cadena literal ".../*..."
		
			
			// Vemos si hay inicio o final de comentario multilinea para colorear parte correspondiente de esa linea
			if (c.contains("/*") && !c.contains("*/"))
			{
				encontradoComentarioML1=c.indexOf("/*");

				if (!comentDentroDeCadena(c,"/*",encontradoComentarioML1))
				{
					//System.out.println("Comentario multilinea inicio sin fin en ("+(encontradoComentarioML1)+","+(c.length()-encontradoComentarioML1)+")");
					MutableAttributeSet attrC=new SimpleAttributeSet();
					StyleConstants.setForeground(attrC,Conf.colorCodigoCo);
					StyleConstants.setBold(attrC,true);
					doc.setCharacterAttributes(startOffset+encontradoComentarioML1,c.length()-encontradoComentarioML1,attrC,false);
				}
				else
					encontradoComentarioML1=-1;
			}
			else if (!c.contains("/*") && c.contains("*/"))
			{
				encontradoComentarioML2=c.indexOf("*/")+2;
				
				if (!comentDentroDeCadena(c,"*/",encontradoComentarioML2))
				{
					//System.out.println("Comentario multilinea final sin inicio en ("+startOffset+","+(encontradoComentarioML2)+")");
					MutableAttributeSet attrC=new SimpleAttributeSet();
					StyleConstants.setForeground(attrC,Conf.colorCodigoCo);
					StyleConstants.setBold(attrC,true);
					doc.setCharacterAttributes(startOffset,encontradoComentarioML2,attrC,false);
				}
				else
					encontradoComentarioML2=-1;
			}
			else if (!c.contains("/*") && !c.contains("*/"))// Línea normal o que podría estar dentro de un comentario multilinea, comprobamos
			{
				//System.out.println("Comentario multilinea intermedio o linea normal");
				boolean encontradaRespuesta=false;
				int i=startOffset;
				
				if (i==content.length())
					i--;
				
				while (i>=0 && !encontradaRespuesta)
				{
					if (content.charAt(i)=='/')
					{
						if (content.charAt(i-1)=='*')		// Hemos encontrado */
						{
							encontradaRespuesta=true;
							estamosEnComentario=false;
						}
						else if (content.charAt(i+1)=='*')	// Hemos encontrado /*
						{
							encontradaRespuesta=true;
							estamosEnComentario=true;
						}
					}
					else if (content.charAt(i)=='"')
					{
						if (content.length()>=i)
							do
							{
								i--;
							}
							while (i>=0 && content.charAt(i)!='"');
					}
					i--;
				}
				//System.out.println("Solucion: "+(estamosEnComentario ? "sí" : "no")+" estamos en un comentario multilinea.");
				if (estamosEnComentario)
				{
					MutableAttributeSet attrC=new SimpleAttributeSet();
					StyleConstants.setForeground(attrC,Conf.colorCodigoCo);
					StyleConstants.setBold(attrC,true);
					doc.setCharacterAttributes(startOffset,endOffset-startOffset,attrC,false);
					//System.out.println("Comentario multilinea intermedio en ("+startOffset+","+(endOffset-startOffset)+")");
				}
				
			}
		}
		
		
		//System.out.println("encontradoComentarioML1="+encontradoComentarioML1+" encontradoComentarioML2="+encontradoComentarioML2);
		
		int endOffsetC=c.length();
		
		int index=startOffset;
		int indexC=0;
		while (indexC<endOffsetC)
		{
			String p=getpalabra(indexC,c);
			int longPalabra=p.length();
			//System.out.println("  __["+p+"]__  "+indexC+","+(indexC+longPalabra));
			
			index=index+longPalabra;
			indexC=indexC+longPalabra;
			
			if (p.charAt(0)!=' ' && p.charAt(0)!=';')
			{
				if (palabras!=null && palabras.get(p)!=null)	// si la palabra está entre las palabras reservadas
				{
					
					if (	((encontradoComentarioML1==-1 && encontradoComentarioML2==-1) || 
							(encontradoComentarioML1!=-1 && indexC<=encontradoComentarioML1) || 
							(encontradoComentarioML2!=-1 && indexC>=encontradoComentarioML2-2)) &&
							!estamosEnComentario)
					{
						//System.out.println("Coloreamos: indexC="+indexC+" index="+index+" longPalabra="+longPalabra);
						
						MutableAttributeSet attr=new SimpleAttributeSet();
						StyleConstants.setForeground(attr,Conf.colorCodigoPR);
						StyleConstants.setBold(attr,true);
						doc.setCharacterAttributes(index-longPalabra,longPalabra,attr,false);
					}
				}
				else if (metodos!=null && metodos.get(p)!=null)	// si la palabra está entre los nombres de metodos que tenemos que resaltar
				{
					MutableAttributeSet attr=new SimpleAttributeSet();
					StyleConstants.setForeground(attr,new Color(150,150,150));
					StyleConstants.setBold(attr,true);
					doc.setCharacterAttributes(index-longPalabra,longPalabra,attr,false);
				}
				else if (p.length()>=2 && p.charAt(0)=='/' && p.charAt(1)=='/')	// si la palabra es un comentario monolinea
				{
					MutableAttributeSet attr=new SimpleAttributeSet();
					StyleConstants.setForeground(attr,Conf.colorCodigoCo);
					StyleConstants.setBold(attr,true);
					doc.setCharacterAttributes(index-longPalabra,longPalabra,attr,false);
				}
				else if (p.length()>=2 && p.charAt(0)=='/' && p.charAt(1)=='*')	// si la palabra es un comentario multilinea
				{
					MutableAttributeSet attr=new SimpleAttributeSet();
					StyleConstants.setForeground(attr,Conf.colorCodigoCo);
					StyleConstants.setBold(attr,true);
					doc.setCharacterAttributes(index-longPalabra,longPalabra,attr,false);
				}
			}	
		}
		actualizarBandaNumLinea();
		

		return result;
	}

	
	
	
	
	/*
	private void actualizarComentariosMultilinea(String content)
	{
		String c=""+content.replace("\n"," ")+"";
		
		
		// no sale bien con indexOf, así que vamos a probar con un bucle  while a ver si así no es demasiado lento y corta bien los comentarios

		
		
		// opción alternativa: no guardar esta inforamción y buscar qué hay antes y después:
		//	- si antes hay: lo más anterior ' * / ' no está en comentario, si lo más jutamente anterior es ' / * ', sí, si no hay nada, no está
		// ¡¡no hace falta buscar qué hay después!!!
		
		int numComentarios=ServiciosString.vecesQueContiene(c, "/*");
		cmini=new int[numComentarios];
		cmfin=new int[numComentarios];
		
		int base=0;	
		int i=0;
		while (c.contains("/*"))
		{
			cmini[i]=base+c.indexOf("/*");
			cmfin[i]=base+c.indexOf("*//*")+2;
			
			if (cmini[i]<c.length() && cmfin[i]<c.length() && cmini[i]<cmfin[i])
			{
				String elComentario=c.substring(cmini[i],cmfin[i]);
				//System.out.println(" - Comentario recogido: ["+elComentario+"]");
			}
			
			base=cmfin[i];
			if (c.length()>(cmfin[i]+1))
			{
				//System.out.println("    Cortamos cadena para comentario en "+cmfin[i]);
				c=c.substring(cmfin[i]);
				//System.out.println("    Lo siguiente es: ["+c.substring(0,50)+"]");
			}
			else
				c="";
			i++;
		}
	}*/
	
	
	
	private boolean comentDentroDeCadena (String cad, String cadena, int posic)
	{
		boolean ok=false;
		
		if (cad.contains("\""))
		{
			int comillaIni=-1;
			int comillaFin=-1;
			
			do
			{
				comillaIni=cad.indexOf("\"",comillaFin+1);
				comillaFin=cad.indexOf("\"",comillaIni+1);
			}
			while (comillaFin<posic);
			
			if (comillaIni<posic && posic<comillaFin)
				ok=true;	// No lo pintamos porque está dentro de una cadena
		}
		return ok;
	}
	
	

	private void actualizarBandaNumLinea()
	{
		if (areaNum!=null)
		{
			String texto="";
			try {
				doc=(DefaultStyledDocument)edit.getDocument();
				texto=doc.getText(0,doc.getLength());
			} catch (Exception e) {
				
			}
			
			int numLineas=this.edit.getText().length()-texto.length()+1;
			
			int numCifras=(""+numLineas).length();
			
						
			String[] espacios={""," ","  ","   ","    ","     ","      ","       ","        ","         ","          "};
			
			if (numLineas!=numLineasAnterior)
			{
				String textoNum="";
				
				int i=1;
				while (i<=numLineas)
				{
					textoNum=textoNum+espacios[numCifras-(""+i).length()]+i+" \n";
					i++;
				}

				
				try {
					areaNum.setText(textoNum);
					areaNum.updateUI();
				} catch (Exception e) {
					
				}
			}
			numLineasAnterior=numLineas;
		}
	}
	
	

	class MyDocument extends DefaultStyledDocument
	{
		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException
		{
			//System.out.println("prueba insertString ("+offset+","+str.length()+")");
			super.insertString(offset,str,a);
			apply(0,getLength(),false);
		}
	}


	@Override
	public void changedUpdate(DocumentEvent arg0) {
		//System.out.println(" changedUpdate ");
	}


	@Override
	public void insertUpdate(DocumentEvent arg0) {
		//System.out.println(" insertUpdate ["+edit.getText().charAt(arg0.getOffset())+"]");	// NO vale para nada, se come saltos de línea
		//System.out.println(" insertUpdate ");
		
		int longitudIntroduccion=arg0.getLength();
		
		char c='a';
		
		char a='a';	//Caracter anterior al insertado
		char b='a';	//Caracter posterior al insertado
		doc=(DefaultStyledDocument)edit.getDocument();
		try {
			int longCadenaDoc=doc.getLength();
			String cadenaDoc=doc.getText(0,longCadenaDoc);
			int offSetArg0=arg0.getOffset();
			c=cadenaDoc.charAt(offSetArg0);
			if (offSetArg0>0)
				a=cadenaDoc.charAt(offSetArg0-1);
			if (offSetArg0<longCadenaDoc-1)
				b=cadenaDoc.charAt(offSetArg0-1);
			//System.out.println(" insertUpdate ["+c+"] ("+arg0.getLength()+")");
		} catch (Exception e) {
			//System.out.println(" insertUpdate (exception)");
		}
		
		if (longitudIntroduccion>=1 || c=='\n')
			actualizarBandaNumLinea();
		
		if (c=='/' || c=='*' || a=='/' || a=='*' || b=='/' || b=='*' || longitudIntroduccion>1)
		{
			new Thread(){
				
				public synchronized void run()
				{
					try {
						wait(10);
						apply(0,edit.getText().length()-1,true);
					} catch (Exception e) {
						//System.out.println(" removeUpdate: excepcion \n");
						e.printStackTrace();
					}
				}
			
			}.start();
		}
	}


	@Override
	public void removeUpdate(DocumentEvent arg0) {
		//System.out.println(" removeUpdate ");
		
		actualizarBandaNumLinea();
		
		new Thread(){
			public synchronized void run()
			{
				try {
					wait(10);
					apply(0,edit.getText().length()-1,true);
				} catch (Exception e) {
					//System.out.println(" removeUpdate: excepcion \n");
					e.printStackTrace();
				}
			}
		}.start();
	}

	
	public String getText()
	{
		//System.out.println("PanelEditorJava.getText -> longitud del texto = "+edit.getText().length());
		return this.edit.getText();
	}
	
	public void select(int inicio, int longitud)
	{
		//System.out.println("PanelEditorJava.select -> "+inicio+", "+longitud);
		
		this.edit.select(inicio, longitud);
		this.edit.requestFocus();
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		Ventana.thisventana.setClasePendienteGuardar(true);
	}


}