/**
	Se encarga de proporcionar a la aplicaci�n el texto que �sta solicita mediante un c�digo en el idioma adecuado
	
	@author Antonio P�rez Carrasco
	@version 2006-2007
*/
package utilidades;




import org.w3c.dom.*;


public class Texto 
{
	static String ficheroTextos="datos/Textos.xml";
	
	
	/**
		Devuelve los idiomas disponibles en el fichero de textos
	*/
	public static String[][] idiomasDisponibles()
	{
		Document documento=ManipulacionElement.getDocumento(ficheroTextos);
		Element eDocument=documento.getDocumentElement();
					
		Element eIdiomas[]=ManipulacionElement.nodeListToElementArray(eDocument.getElementsByTagName("IdiomaDef"));
		
		String idiomas[][]=new String[eIdiomas.length][2];
		
		for (int i=0; i<eIdiomas.length; i++)
		{
			//idiomas[i]=new String[2];
			idiomas[i][0]=reparar(eIdiomas[i].getAttribute("nombre"));
			idiomas[i][1]=reparar(eIdiomas[i].getAttribute("id"));
		}
		return idiomas;
	}

	
	public static String getCodigo(String texto, String idioma)
	{
		Document documento=ManipulacionElement.getDocumento(ficheroTextos);
		Element eDocument=documento.getDocumentElement();
		
		// Buscamos texto y devolvemos
		Element textos[]=ManipulacionElement.nodeListToElementArray(eDocument.getElementsByTagName("T"));
		for (int i=0; i<textos.length; i++)
			if (textos[i].getAttribute("idioma").equals(idioma) && reparar(textos[i].getAttribute("valor")).equals(texto))
				return textos[i].getAttribute("id");
		
		// No existe el texto, devolvemos texto de error
		return "Error de idioma";
	}
	
	public static String get(String codigo, String idioma)
	{
		Document documento=ManipulacionElement.getDocumento(ficheroTextos);
		Element eDocument=documento.getDocumentElement();
		
		// Buscamos texto y devolvemos
		Element textos[]=ManipulacionElement.nodeListToElementArray(eDocument.getElementsByTagName("T"));
		for (int i=0; i<textos.length; i++)
			if (textos[i].getAttribute("idioma").equals(idioma) && textos[i].getAttribute("id").equals(codigo))
				return reparar(textos[i].getAttribute("valor"));
		
		// No lo hemos encontrado, devolvemos texto por defecto de ese idioma
		for (int i=0; i<textos.length; i++)
			if (textos[i].getAttribute("idioma").equals(idioma) && textos[i].getAttribute("id").equals("DEFECTO"))
				return reparar(textos[i].getAttribute("valor"));
		
		// Tampoco existe el texto por defecto, devolvemos texto de error
		return "Error de idioma";
	}
	
	public static String get(String codigo, String idioma, char c)
	{
		Document documento=ManipulacionElement.getDocumento(ficheroTextos);
		Element eDocument=documento.getDocumentElement();
		
		// Buscamos texto y devolvemos
		Element textos[]=ManipulacionElement.nodeListToElementArray(eDocument.getElementsByTagName("T"));
		for (int i=0; i<textos.length; i++)
			if (textos[i].getAttribute("idioma").equals(idioma) && textos[i].getAttribute("id").equals(codigo))
			{
				String s=textos[i].getAttribute("valor");
				return reparar(s.substring(0,s.indexOf(c)));
			}
		
		// No lo hemos encontrado, devolvemos texto por defecto de ese idioma
		for (int i=0; i<textos.length; i++)
			if (textos[i].getAttribute("idioma").equals(idioma) && textos[i].getAttribute("id").equals("DEFECTO"))
				return reparar(textos[i].getAttribute("valor"));
		
		// Tampoco existe el texto por defecto, devolvemos texto de error
		return "Error de idioma";
	}
	
	
	public static String[] get(String[] codigos, String idioma)
	{
		Document documento=ManipulacionElement.getDocumento(ficheroTextos);
		Element eDocument=documento.getDocumentElement();
		
		String [] cadenas=new String [codigos.length];
		Element textosIdiomas[]=ManipulacionElement.nodeListToElementArray(eDocument.getElementsByTagName("T"));
		
		int contadorPalabrasIdioma=0;
		for (int i=0; i<textosIdiomas.length; i++)
			if (textosIdiomas[i].getAttribute("idioma").equals(idioma))
				contadorPalabrasIdioma++;
		
		
		Element textos[]=new Element[contadorPalabrasIdioma];
		contadorPalabrasIdioma=0;
		for (int i=0; i<textosIdiomas.length; i++)
		{
			if (textosIdiomas[i].getAttribute("idioma").equals(idioma))
			{
				textos[contadorPalabrasIdioma]=textosIdiomas[i];
				contadorPalabrasIdioma++;
			}
		}
		
		//int cadenasEncontradas=0;
		
		// Buscamos texto y devolvemos
		for (int x=0; x<codigos.length; x++)
		{
			for (int i=0; i<textos.length; i++)
			{
				//System.out.print("codigos[x]="+codigos[x]+"  textos[i]="+textos[i].getAttribute("id")+"    >");
				if (textos[i].getAttribute("id").equals(codigos[x]))
				{
					cadenas[x]=reparar(textos[i].getAttribute("valor"));
					//System.out.println("TRUE");
				}
				//else
					//System.out.println("FALSE");
			}
		}
		
		// No lo hemos encontrado, devolvemos texto por defecto de ese idioma
		for (int i=0; i<cadenas.length; i++)
			if (cadenas[i]==null)
				cadenas[i]=get("DEFECTO",idioma);
		
		
		return cadenas;
	}
	
	private static String reparar(String s)
	{
		if (s.contains("["))
		{
			s=s.replace("[aacute]","�");
			s=s.replace("[eacute]","�");
			s=s.replace("[iacute]","�");
			s=s.replace("[oacute]","�");
			s=s.replace("[uacute]","�");
			
			s=s.replace("[Aacute]","�");
			s=s.replace("[Eacute]","�");
			s=s.replace("[Iacute]","�");
			s=s.replace("[Oacute]","�");
			s=s.replace("[Uacute]","�");
			
			s=s.replace("[ntilde]","�");
			s=s.replace("[Ntilde]","�");
			
			s=s.replace("[uuml]","�");
			s=s.replace("[Uuml]","�");
			
			s=s.replace("[amp]","&");
			s=s.replace("[lt]","<");
			s=s.replace("[gt]",">");
			
			s=s.replace("[iexcl]","�");
			s=s.replace("[iquest]","�");
		}
		return s;
	}
}