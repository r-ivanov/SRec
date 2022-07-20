package utilidades;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import ventanas.Ventana;
import conf.Conf;

/**
 * Permite la cómoda selección de una dirección física para cargar o guardar un
 * fichero
 */
public class SelecDireccion {
	
	/**
	 * Permite seleccionar un fichero.
	 * 
	 * @param path
	 *            Ubicación del fichero.
	 * @param titulo
	 *            título que aparecerá en el cuadro de diálogo de selección de
	 *            fichero
	 * @param nombreFichero
	 *            Nombre del fichero.
	 * @param extension
	 *            extensión de los ficheros que se desea poder seleccionar
	 * @param definicion
	 *            Descripción del tipo de fichero.
	 * @param load
	 *            =0 si el cuadro es para seleccionar un fichero para escritura,
	 *            =1 si es para lectura
	 */
	public static String[] cuadroAbrirFichero(String path, String titulo,
			String nombreFichero, String extension, String definicion, int load) {
		String[] ext = new String[1];
		ext[0] = extension;
		return cuadroAbrirFichero(path, titulo, nombreFichero, ext, definicion,
				load);
	}

	/**
	 * Permite seleccionar un fichero.
	 * 
	 * @param path
	 *            Ubicación del fichero.
	 * @param titulo
	 *            título que aparecerá en el cuadro de diálogo de selección de
	 *            fichero
	 * @param nombreFichero
	 *            Nombre del fichero.
	 * @param extension
	 *            extensión de los ficheros que se desea poder seleccionar
	 * @param definicion
	 *            Descripción del tipo de fichero.
	 * @param load
	 *            =0 si el cuadro es para seleccionar un fichero para escritura,
	 *            =1 si es para lectura
	 * @param soloDirectorio
	 *            Si la selección ha de ser de un directorio en lugar de un
	 *            fichero concreto.
	 */
	public static String[] cuadroAbrirFichero(String path, String titulo,
			String nombreFichero, String extension, String definicion,
			int load, boolean soloDirectorio) {
		String[] ext = new String[1];
		ext[0] = extension;
		return cuadroAbrirFichero(path, titulo, nombreFichero, ext, definicion,
				load, soloDirectorio);
	}

	/**
	 * Permite seleccionar un fichero.
	 * 
	 * @param path
	 *            Ubicación del fichero.
	 * @param titulo
	 *            título que aparecerá en el cuadro de diálogo de selección de
	 *            fichero
	 * @param nombreFichero
	 *            Nombre del fichero.
	 * @param extensiones
	 *            extensiones de los ficheros que se desea poder seleccionar
	 * @param definicion
	 *            Descripción del tipo de fichero.
	 * @param load
	 *            =0 si el cuadro es para seleccionar un fichero para escritura,
	 *            =1 si es para lectura
	 */
	public static String[] cuadroAbrirFichero(String path, String titulo,
			String nombreFichero, String[] extensiones, String definicion,
			int load) {
		String[][] ext = new String[1][];
		ext[0] = extensiones;
		String[] def = new String[1];
		def[0] = definicion;
		return cuadroAbrirFichero(Ventana.thisventana, path, titulo,
				nombreFichero, ext, def, load);
	}

	/**
	 * Permite seleccionar un fichero.
	 * 
	 * @param path
	 *            Ubicación del fichero.
	 * @param titulo
	 *            título que aparecerá en el cuadro de diálogo de selección de
	 *            fichero
	 * @param nombreFichero
	 *            Nombre del fichero.
	 * @param extensiones
	 *            extensiones de los ficheros que se desea poder seleccionar
	 * @param definicion
	 *            Descripción del tipo de fichero.
	 * @param load
	 *            =0 si el cuadro es para seleccionar un fichero para escritura,
	 *            =1 si es para lectura
	 * @param soloDirectorio
	 *            Si la selección ha de ser de un directorio en lugar de un
	 *            fichero concreto.
	 */
	public static String[] cuadroAbrirFichero(String path, String titulo,
			String nombreFichero, String[] extensiones, String definicion,
			int load, boolean soloDirectorio) {
		String[][] ext = new String[1][];
		ext[0] = extensiones;
		String[] def = new String[1];
		def[0] = definicion;
		return cuadroAbrirFichero(Ventana.thisventana, path, titulo,
				nombreFichero, ext, def, load, soloDirectorio);
	}

	/**
	 * Permite seleccionar un fichero.
	 * 
	 * @param path
	 *            Ubicación del fichero.
	 * @param titulo
	 *            título que aparecerá en el cuadro de diálogo de selección de
	 *            fichero
	 * @param nombreFichero
	 *            Nombre del fichero.
	 * @param extensiones
	 *            extensiones de los distintos tipos de ficheros que se desean
	 *            poder seleccionar.
	 * @param definiciones
	 *            Descripciones de los tipos de ficheros permitidos.
	 * @param load
	 *            =0 si el cuadro es para seleccionar un fichero para escritura,
	 *            =1 si es para lectura
	 */
	public static String[] cuadroAbrirFichero(String path, String titulo,
			String nombreFichero, String[][] extensiones,
			String definiciones[], int load) {
		return cuadroAbrirFichero(Ventana.thisventana, path, titulo,
				nombreFichero, extensiones, definiciones, load);
	}

	/**
	 * Permite seleccionar un fichero.
	 * 
	 * @param padre
	 *            componente padre al que quedará ligado el cuadro de diálogo
	 * @param path
	 *            Ubicación del fichero.
	 * @param titulo
	 *            título que aparecerá en el cuadro de diálogo de selección de
	 *            fichero
	 * @param nombreFichero
	 *            Nombre del fichero.
	 * @param extensiones
	 *            extensiones de los distintos tipos de ficheros que se desean
	 *            poder seleccionar.
	 * @param definiciones
	 *            Descripciones de los tipos de ficheros permitidos.
	 * @param load
	 *            =0 si el cuadro es para seleccionar un fichero para escritura,
	 *            =1 si es para lectura
	 */
	public static String[] cuadroAbrirFichero(Component padre, String path,
			String titulo, String nombreFichero, String[][] extensiones,
			String[] definiciones, int load) {
		return cuadroAbrirFichero(padre, path, titulo, nombreFichero,
				extensiones, definiciones, load, false);
	}

	/**
	 * Permite seleccionar un fichero.
	 * 
	 * @param padre
	 *            componente padre al que quedará ligado el cuadro de diálogo
	 * @param path
	 *            Ubicación del fichero.
	 * @param titulo
	 *            título que aparecerá en el cuadro de diálogo de selección de
	 *            fichero
	 * @param nombreFichero
	 *            Nombre del fichero.
	 * @param extensiones
	 *            extensiones de los distintos tipos de ficheros que se desean
	 *            poder seleccionar.
	 * @param definiciones
	 *            Descripciones de los tipos de ficheros permitidos.
	 * @param load
	 *            =0 si el cuadro es para seleccionar un fichero para escritura,
	 *            =1 si es para lectura
	 * @param soloDirectorio
	 *            Si la selección ha de ser de un directorio en lugar de un
	 *            fichero concreto.
	 */
	public static String[] cuadroAbrirFichero(Component padre, String path,
			String titulo, String nombreFichero, String[][] extensiones,
			String[] definiciones, int load, boolean soloDirectorio) {
		String nombres[] = new String[3];

		final JFileChooser jfc = new JFileChooser(path);
		jfc.setDialogTitle(titulo);
		String boton = "";
		int tipo = 0;
		if (load == 0) {
			tipo = JFileChooser.SAVE_DIALOG;
			boton = Texto.get("GUARDAR", Conf.idioma);
		} else {
			tipo = JFileChooser.OPEN_DIALOG;
			boton = Texto.get("ABRIR", Conf.idioma);
		}

		if (soloDirectorio) {
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}

		jfc.setDialogType(tipo);		
        AbstractAction a=new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                try {
                	File selectedFile = jfc.getSelectedFile();
                    if(selectedFile != null) {                 	
                        java.nio.file.Files.delete(selectedFile.toPath());
                        jfc.rescanCurrentDirectory();
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        jfc.getActionMap().put("delAction",a);
        jfc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"),"delAction");
		
	FileNameExtensionFilter filtros[] = new FileNameExtensionFilter[definiciones.length];
		
		
		if (definiciones[0] != null && extensiones[0] != null
				&& definiciones.length > 0 && extensiones[0].length > 0) {
			for (int i = 0; i < filtros.length; i++) {
				if (extensiones[i].length == 1) {
					filtros[i] = new FileNameExtensionFilter(definiciones[i],
							extensiones[i][0]);
				} else if (extensiones[i].length == 2) {
					filtros[i] = new FileNameExtensionFilter(definiciones[i],
							extensiones[i][0], extensiones[i][1]);
				} else if (extensiones[i].length == 3) {
					filtros[i] = new FileNameExtensionFilter(definiciones[i],
							extensiones[i][0], extensiones[i][1],
							extensiones[i][2]);
				} else if (extensiones[i].length == 4) {
					filtros[i] = new FileNameExtensionFilter(definiciones[i],
							extensiones[i][0], extensiones[i][1],
							extensiones[i][2], extensiones[i][3]);
				} else {
					filtros[i] = new FileNameExtensionFilter(definiciones[i],
							extensiones[i][0], extensiones[i][1],
							extensiones[i][2], extensiones[i][3],
							extensiones[i][4]);
				}
				jfc.setFileFilter(filtros[i]);
			}
		}
		File fichero;
		if (nombreFichero != null && !soloDirectorio) {
			 fichero = new File(nombreFichero);
		
			jfc.setSelectedFile(fichero);
			
		} else {
			 fichero = new File("");
			jfc.setSelectedFile(fichero);
		}
		
		
		int returnVal = 0;
		if (tipo == JFileChooser.SAVE_DIALOG) {
			returnVal = jfc.showSaveDialog(padre);
		} else if (tipo == JFileChooser.OPEN_DIALOG) {
			returnVal = jfc.showOpenDialog(padre);
		} else {
			returnVal = jfc.showDialog(padre, boton);
		}

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			nombres[0] = jfc.getSelectedFile().getParent() + File.separator;
			nombres[1] = jfc.getSelectedFile().getName();
			
		
			FileFilter fne = jfc.getFileFilter();
			boolean text= fne.getDescription().toLowerCase().contains("txt");
			if (text == true) {
				nombres[2] = "true";
			} else {
				nombres[2] = "false";
			}

			if (tipo == JFileChooser.SAVE_DIALOG && !soloDirectorio) {
				if (fne.getDescription().toLowerCase().contains("txt")
						&& (!nombres[1].toLowerCase().contains(".txt") && !nombres[1]
								.toLowerCase().contains(".txt"))) {
					nombres[1] = nombres[1] + ".txt";
				}if (fne.getDescription().toLowerCase().contains("jpeg")
						&& (!nombres[1].toLowerCase().contains(".jpeg") && !nombres[1]
								.toLowerCase().contains(".jpg"))) {
					nombres[1] = nombres[1] + ".jpg";
				} else if (fne.getDescription().toLowerCase().contains("png")
						&& !nombres[1].toLowerCase().contains(".png")) {
					nombres[1] = nombres[1] + ".png";
				} else if (fne.getDescription().toLowerCase().contains("gif")
						&& !nombres[1].toLowerCase().contains(".gif")) {
					nombres[1] = nombres[1] + ".gif";
				}
			}
		} else {
			/*FileFilter fne = jfc.getFileFilter();
			boolean text= jfc.getSelectedFile().getName().endsWith("txt");
			if(text==true) {nombres[2]="true";
			}
			else {nombres[2]="false";
			}
			*/
			nombres[0] = path;
			nombres[1] = null;

			if(jfc.getSelectedFile() != null) {
				boolean text= jfc.getSelectedFile().getName().endsWith("txt");
				if (text == true) {
					nombres[2] = "true";
				} else {
					nombres[2] = "false";
				}
			}
			
		}

		return nombres;

	}

	
	

}
