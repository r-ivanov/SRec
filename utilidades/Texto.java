package utilidades;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Se encarga de proporcionar a la aplicación el texto que ésta solicita
 * mediante un código en el idioma adecuado
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class Texto {

	private static String ficheroTextos = "datos/Textos.xml";

	/**
	 * Devuelve los idiomas disponibles en el fichero de textos.
	 * 
	 * @return Matriz, donde cada fila corresponde a un idioma, la columna 0 al
	 *         nombre del idioma, y la columna 1 al identificador del idioma.
	 */
	public static String[][] idiomasDisponibles() {
		Document documento = ManipulacionElement.getDocumento(ficheroTextos);
		Element eDocument = documento.getDocumentElement();

		Element eIdiomas[] = ManipulacionElement
				.nodeListToElementArray(eDocument
						.getElementsByTagName("IdiomaDef"));

		String idiomas[][] = new String[eIdiomas.length][2];

		for (int i = 0; i < eIdiomas.length; i++) {
			idiomas[i][0] = reparar(eIdiomas[i].getAttribute("nombre"));
			idiomas[i][1] = reparar(eIdiomas[i].getAttribute("id"));
		}
		return idiomas;
	}

	/**
	 * Dado un idioma y el texto correspondiente, devuelve el identificador del
	 * texto.
	 * 
	 * @param texto
	 *            Texto correspondiente.
	 * @param idioma
	 *            Idioma.
	 * 
	 * @return Identificador del texto.
	 */
	public static String getCodigo(String texto, String idioma) {
		Document documento = ManipulacionElement.getDocumento(ficheroTextos);
		Element eDocument = documento.getDocumentElement();

		// Buscamos texto y devolvemos
		Element textos[] = ManipulacionElement.nodeListToElementArray(eDocument
				.getElementsByTagName("T"));
		for (int i = 0; i < textos.length; i++) {
			if (textos[i].getAttribute("idioma").equals(idioma)
					&& reparar(textos[i].getAttribute("valor")).equals(texto)) {
				return textos[i].getAttribute("id");
			}
		}

		// No existe el texto, devolvemos texto de error
		return "Error de idioma";
	}

	/**
	 * Dado un idioma y el identificador correspondiente, devuelve el texto
	 * asociado.
	 * 
	 * @param codigo
	 *            Identificador del texto.
	 * @param idioma
	 *            Idioma.
	 * 
	 * @return Texto asociado.
	 */
	public static String get(String codigo, String idioma) {
		Document documento = ManipulacionElement.getDocumento(ficheroTextos);
		Element eDocument = documento.getDocumentElement();

		// Buscamos texto y devolvemos
		Element textos[] = ManipulacionElement.nodeListToElementArray(eDocument
				.getElementsByTagName("T"));
		for (int i = 0; i < textos.length; i++) {
			if (textos[i].getAttribute("idioma").equals(idioma)
					&& textos[i].getAttribute("id").equals(codigo)) {
				return reparar(textos[i].getAttribute("valor"));
			}
		}

		// No lo hemos encontrado, devolvemos texto por defecto de ese idioma
		for (int i = 0; i < textos.length; i++) {
			if (textos[i].getAttribute("idioma").equals(idioma)
					&& textos[i].getAttribute("id").equals("DEFECTO")) {
				return reparar(textos[i].getAttribute("valor"));
			}
		}

		// Tampoco existe el texto por defecto, devolvemos texto de error
		return "Error de idioma";
	}

	/**
	 * Dado un idioma y una lista de identificadores, devuelve los texto
	 * asociados a cada uno de ellos.
	 * 
	 * @param codigso
	 *            Identificadores de los textos.
	 * @param idioma
	 *            Idioma.
	 * 
	 * @return Textos asociados.
	 */
	public static String[] get(String[] codigos, String idioma) {
		Document documento = ManipulacionElement.getDocumento(ficheroTextos);
		Element eDocument = documento.getDocumentElement();

		String[] cadenas = new String[codigos.length];
		Element textosIdiomas[] = ManipulacionElement
				.nodeListToElementArray(eDocument.getElementsByTagName("T"));

		int contadorPalabrasIdioma = 0;
		for (int i = 0; i < textosIdiomas.length; i++) {
			if (textosIdiomas[i].getAttribute("idioma").equals(idioma)) {
				contadorPalabrasIdioma++;
			}
		}

		Element textos[] = new Element[contadorPalabrasIdioma];
		contadorPalabrasIdioma = 0;
		for (int i = 0; i < textosIdiomas.length; i++) {
			if (textosIdiomas[i].getAttribute("idioma").equals(idioma)) {
				textos[contadorPalabrasIdioma] = textosIdiomas[i];
				contadorPalabrasIdioma++;
			}
		}

		// Buscamos texto y devolvemos
		for (int x = 0; x < codigos.length; x++) {
			for (int i = 0; i < textos.length; i++) {
				if (textos[i].getAttribute("id").equals(codigos[x])) {
					cadenas[x] = reparar(textos[i].getAttribute("valor"));
				}
			}
		}

		// No lo hemos encontrado, devolvemos texto por defecto de ese idioma
		for (int i = 0; i < cadenas.length; i++) {
			if (cadenas[i] == null) {
				cadenas[i] = get("DEFECTO", idioma);
			}
		}

		return cadenas;
	}

	/**
	 * Elimina de la cadena de entrada los elementos especificados con una
	 * codificación especial, devolviendo una nueva cadena con los
	 * correspondientes elementos reemplazados.
	 * 
	 * @param s
	 *            Cadena de entrada.
	 * 
	 * @return Cadena de salida con los elementos correctamente reemplazados.
	 */
	private static String reparar(String s) {
		if (s.contains("[")) {
			s = s.replace("[aacute]", "á");
			s = s.replace("[eacute]", "é");
			s = s.replace("[iacute]", "í");
			s = s.replace("[oacute]", "ó");
			s = s.replace("[uacute]", "ú");

			s = s.replace("[Aacute]", "Á");
			s = s.replace("[Eacute]", "É");
			s = s.replace("[Iacute]", "Í");
			s = s.replace("[Oacute]", "Ó");
			s = s.replace("[Uacute]", "Ú");

			s = s.replace("[ntilde]", "ñ");
			s = s.replace("[Ntilde]", "Ñ");

			s = s.replace("[uuml]", "ü");
			s = s.replace("[Uuml]", "Ú");

			s = s.replace("[amp]", "&");
			s = s.replace("[lt]", "<");
			s = s.replace("[gt]", ">");

			s = s.replace("[iexcl]", "¡");
			s = s.replace("[iquest]", "¿");
		}
		return s;
	}
}