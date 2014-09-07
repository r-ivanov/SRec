package utilidades;

import gif.AnimatedGifEncoder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.jgraph.JGraph;

import ventanas.Ventana;
import conf.Conf;
import cuadros.CuadroProgreso;

/**
 * Clase de utilidad que permite obtener una captura de una determinada vista.
 */
public class Fotografo {

	/**
	 * Permite crear un solo fichero con un gif animado a partir de las
	 * distintas capturas previamente tomadas.
	 * 
	 * @param numFotos
	 *            Número de capturas que componen el gif final.
	 * @param archivo
	 *            Fichero de salida donde se almacenará el gif final.
	 * @param gifPila
	 *            True si se trata de una visualización para la animación de
	 *            pila.
	 */
	public static void crearGIFAnimado(int numFotos, String archivo,
			boolean gifPila) {
		CuadroProgreso cp = null;

		if (numFotos > 1) {
			cp = new CuadroProgreso(Ventana.thisventana, Texto.get("CP_ESPERE",
					Conf.idioma), Texto.get("CP_PROCES", Conf.idioma), 0);
		}

		// Cargamos esas numFotos capturas
		BufferedImage bi[] = new BufferedImage[numFotos];

		for (int i = 0; i < numFotos; i++) {
			try {
				if (i < 10) {
					bi[i] = ImageIO.read(new File("imagen_SRec_000" + i
							+ ".gif"));
				} else if (i < 100) {
					bi[i] = ImageIO
							.read(new File("imagen_SRec_00" + i + ".gif"));
				} else if (i < 1000) {
					bi[i] = ImageIO
							.read(new File("imagen_SRec_0" + i + ".gif"));
				} else {
					bi[i] = ImageIO.read(new File("imagen_SRec_" + i + ".gif"));
				}
			} catch (java.io.IOException ioe) {
			}
		}

		/*
		 * En el caso de la vista de pila, los tamaños de las imagenes varian, y
		 * el gif coge el tamaño de la primera image.Por lo tanto debemos poner
		 * una primera imagen que tenga el tamaño máximo de las imagenes.
		 */
		BufferedImage blanco = null;
		if (gifPila) {
			// Buscamos imagen más alta y la mas ancha
			int maxAlto = -1;
			int indiceMaxAlto = -1;
			int maxAncho = -1;
			int indiceMaxAncho = -1;
			for (int i = 0; i < numFotos; i++) {
				if (bi[i].getHeight() > maxAlto) {
					maxAlto = bi[i].getHeight();
					indiceMaxAlto = i;
				}
				if (bi[i].getWidth() > maxAncho) {
					maxAncho = bi[i].getWidth();
					indiceMaxAncho = i;
				}
			}
			// Creamos imagen blanca
			blanco = new BufferedImage(bi[indiceMaxAncho].getWidth(),
					bi[indiceMaxAlto].getHeight(), BufferedImage.TYPE_INT_RGB);
		}

		OutputStream os = null;
		try {
			os = new FileOutputStream(archivo);
		} catch (java.io.FileNotFoundException nfne) {
		}
		AnimatedGifEncoder age = new AnimatedGifEncoder();

		age.start(os);

		if (gifPila) {
			age.setTransparent(new Color(0, 0, 0));
			age.addFrame(blanco);
		}

		for (int i = 0; i < numFotos; i++) {
			age.addFrame(bi[i]);
			if (i == 0) {
				age.setDelay(1000);
				age.addFrame(bi[i]);
			}
			age.setDelay(1000);
			if (cp != null) {
				cp.setValores(Texto.get("CP_PROCES", Conf.idioma), (i * 100)
						/ numFotos);
			}
		}
		age.setRepeat(5);
		age.finish();
		try {
			os.flush();
		} catch (java.io.IOException ioe) {
		}
		try {
			os.close();
		} catch (Exception x) {
		}
		if (cp != null) {
			cp.cerrar();
		}

	}

	/**
	 * Determina el número identificativo del tipo de formato, correspondiente
	 * al formato pasado por parámetro.
	 * 
	 * @param s
	 *            ".gif", ".jpg", ".jpeg", o ".png"
	 * 
	 * @return 1 -> gif, 2 -> jpg/jpeg, 0 -> png, -1 -> Formato desconocido.
	 */
	public static int numFormato(String s) {
		if (s.toLowerCase().contains(".gif")) {
			return 1;
		} else if (s.toLowerCase().contains(".jpg")
				|| s.toLowerCase().contains(".jpeg")) {
			return 2;
		} else if (s.toLowerCase().contains(".png")) {
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * Permite capturar un snapshot del componente pasado por parámetro,
	 * aplicando un prefijo por defecto para el nombre de fichero y un número de
	 * secuencia.
	 * 
	 * @param c
	 *            Componente del que se tomará la fotografía.
	 * @param num
	 *            Número de captura, que se añadira al prefijo especificado.
	 */
	public static void guardarFoto(JComponent c, int num) {
		// Nombre de la imagen
		String numCaptura = num + "";
		if (numCaptura.length() == 1) {
			numCaptura = "000" + numCaptura;
		} else if (numCaptura.length() == 2) {
			numCaptura = "00" + numCaptura;
		} else if (numCaptura.length() == 3) {
			numCaptura = "0" + numCaptura;
		}

		guardarFoto(c, 1, "imagen_SRec_" + numCaptura + ".gif");
	}

	/**
	 * Permite capturar un snapshot del componente pasado por parámetro,
	 * aplicando un prefijo para el nombre de fichero y un número de secuencia.
	 * 
	 * @param c
	 *            Componente del que se tomará la fotografía.
	 * @param nombreFicheroGenerico
	 *            Prefijo para la captura.
	 * @param tipo
	 *            1 -> GIF, 2 -> JPG, 0 -> PNG
	 * @param num
	 *            Número de captura, que se añadira al prefijo especificado.
	 */
	public static void guardarFoto(JComponent c, String nombreFicheroGenerico,
			int tipo, int num) {
		// Nombre de la imagen
		String numCaptura = num + "";
		if (numCaptura.length() == 1) {
			numCaptura = "000" + numCaptura;
		} else if (numCaptura.length() == 2) {
			numCaptura = "00" + numCaptura;
		} else if (numCaptura.length() == 3) {
			numCaptura = "0" + numCaptura;
		}

		String extension;
		if (tipo == 1) {
			extension = ".gif";
		} else if (tipo == 2) {
			extension = ".jpg";
		} else {
			extension = ".png";
		}

		guardarFoto(c, 1, nombreFicheroGenerico + "_" + numCaptura + extension);
	}

	/**
	 * Permite capturar un snapshot del componente pasado por parámetro.
	 * 
	 * @param c
	 *            Componente del que se tomará la fotografía.
	 * @param tipo
	 *            1 -> GIF, 2 -> JPG, 0 -> PNG
	 * @param nombre
	 *            Nombre del fichero de salida.
	 */
	public static void guardarFoto(JComponent c, int tipo, String nombre) {
		int[] dim = new int[2];
		dim[0] = c.getWidth();
		dim[1] = c.getHeight();
		guardarFoto(c, tipo, nombre, dim);
	}

	/**
	 * Permite capturar un snapshot del componente pasado por parámetro.
	 * 
	 * @param c
	 *            Componente del que se tomará la fotografía.
	 * @param tipo
	 *            1 -> GIF, 2 -> JPG, 0 -> PNG
	 * @param nombre
	 *            Nombre del fichero de salida.
	 * @param dim
	 *            Dimensiones de la captura (ancho, alto)
	 */
	public static void guardarFoto(JComponent c, int tipo, String nombre,
			int[] dim) {
		Image img = null;
		BufferedImage bi;

		if (c instanceof JGraph) {
			bi = ((JGraph) c).getImage(c.getBackground(), 0);
		} else {
			img = c.createImage(dim[0], dim[1]);
			Graphics imgG = img.getGraphics();
			c.paint(imgG);

			bi = new BufferedImage(img.getWidth(null), img.getHeight(null),
					BufferedImage.TYPE_INT_RGB);

		}
		bi.getGraphics().drawImage(img, 0, 0, null);
		bi.getGraphics().dispose();

		// Guardamos la imagen
		File f = new File(nombre);

		try {
			if (tipo == 1) {
				ImageIO.write(bi, "GIF", f);
			} else if (tipo == 2) {
				ImageIO.write(bi, "JPG", f);
			} else {
				ImageIO.write(bi, "PNG", f);
			}

		} catch (java.io.IOException ioe) {
			ioe.printStackTrace();
		}
	}

}