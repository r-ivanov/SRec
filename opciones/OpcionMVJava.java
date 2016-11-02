package opciones;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.ManipulacionElement;
import utilidades.SsooValidator;

/**
 * Opción que permite la asignación de una máquina virtual para la compilación y
 * ejecución de los distintos algoritmos.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class OpcionMVJava extends Opcion {
	private static final long serialVersionUID = 1002;

	private String dir = null; // Directorio donde se encuentra la máquina
								// virtual
	private String version = null; // Versión (valor autoasignado en función de
									// directorio)
	private boolean valida = false; // Constata que está actualmente intstalada
									// la MV en el directorio "dir"

	/**
	 * Constructor: crea una opción vacía
	 */
	public OpcionMVJava() {
		this(null);
	}

	/**
	 * Constructor: crea una opción asignando una dirección
	 * 
	 * @param dir
	 *            Directorio donde se encuentra la máquina virtual de Java
	 */
	public OpcionMVJava(String dir) {
		super("OpcionMVJava");
		this.setDir(dir);
	}

	/**
	 * Asigna el directorio dado por el usuario, realizando ciertas
	 * comprobaciones sobre la validez. Inserta el directorio donde está la MV
	 * seleccionada y actualiza campos.
	 * 
	 * @param dir
	 *            Directorio donde se encuentra la máquina virtual de Java
	 */
	public boolean setDir(String dir) {
		if (dir != null) {
			if(SsooValidator.isWindows()){
				dir = dir.replace("javac.exe", "java.exe");
			}else{
				dir = dir.replace("javac", "java");
			}
		}

		this.dir = dir;

		if (this.dir == null) {
			this.version = null;
			this.valida = false;
		} else {
			// Hallamos versión
			// Tendremos algo como esto: "C:\Archivos de
			// programa\Java\jdk1.5.0_03\bin\java.exe"
			if (dir.contains("java.exe")) {
				int x = dir.lastIndexOf("jdk") + 3;
				if (x == -1) {
					x = dir.lastIndexOf("jre") + 3;
				}
				int y = dir.lastIndexOf("bin") - 1;
				if (x < 0 && y < 0) {
					this.version = dir.substring(x, y);
				} else {
					this.version = "desconocida";
				}

				if (this.dir.contains("java.exe")) {
					this.dir = this.dir.replace("java.exe", "");
				}

			}
			// o como esto: "/bin/lib/jvm/java-1.5.0-sun/jre/bin/java"
			else if ((dir.contains("/java") || dir.contains("/Java"))
					&& (dir.charAt(dir.length() - 1) == 'a'
							&& dir.charAt(dir.length() - 2) == 'v' && dir
							.charAt(dir.length() - 3) == 'a')) {
				this.version = null;

				int x = dir.indexOf(".");
				int y = dir.indexOf(".", x + 1);
				int z = dir.indexOf("/", y);
				this.version = dir.substring(x - 1, z);

				// Verificamos que existe MV
				File mv = null;
				//	Eliminamos último java si existe porque es como el java.exe
				if(dir.charAt(dir.length()-1)=='a' && 
						dir.charAt(dir.length()-2)=='v' &&
						dir.charAt(dir.length()-3)=='a' &&
						dir.charAt(dir.length()-4)=='j'){
					this.dir = dir.substring(0, dir.length()-4);
				}
				if(SsooValidator.isUnix()){
					mv = new File(dir);
				}else{
					mv = new File(dir + "java");
				}
				this.valida = mv.exists();
			}
		}
		return this.getValida();
	}

	/**
	 * Asigna al campo dir el directorio donde se encuentra la máquina virtual
	 * de java
	 * 
	 * @param dir
	 *            directorio donde se encuentra la máquina virtual de Java
	 */
	private void setDirDirecta(String dir) {
		this.dir = dir;
	}

	/**
	 * Devuelve del campo dir el directorio donde se encuentra la máquina
	 * virtual de java
	 * 
	 * @return directorio donde se encuentra la máquina virtual de Java
	 */
	public String getDir() {
		return this.dir;
	}

	/**
	 * Devuelve la versión de java que se está utilizando
	 * 
	 * @return versión de java que se está utilizando
	 */
	public String getVersion() {
		if (this.version == null) {
			return "";
		} else {
			return this.version;
		}
	}

	/**
	 * Asigna al campo version la version dada como parámetro
	 * 
	 * @param version
	 *            version dada como parámetro
	 */
	private void setVersionDirecta(String version) {
		this.version = version;
	}

	/**
	 * Permite consultar si la dirección contiene los ejecutables necesarios
	 * para determinar si es un directorio válido.
	 * 
	 * @return true si es valida
	 */
	public boolean getValida() {
		File mv1 = null;
		File mv2 = null;
		if(SsooValidator.isWindows()){
			mv1 = new File(this.dir + "java.exe");
			mv2 = new File(this.dir + "javac.exe");
		}else if(SsooValidator.isUnix() && this.dir != null){
			mv1 = new File(this.dir);
			mv2 = new File(this.dir);
		}
		
		if(mv1 != null && mv2 != null){
			this.valida = mv1.exists() && mv2.exists();
			return this.valida;
		}else{
			return false;
		}
	}

	/**
	 * Asigna al campo valida el valor de validez dado como parámetro
	 * 
	 * @param vf
	 *            valor de validez
	 */
	public void setValidaDirecta(boolean vf) {
		this.valida = vf;
	}

	@Override
	public Element getRepresentacionElement(Document d) {
		Element e = d.createElement("OpcionMVJava");

		Element e01 = d.createElement("dir");
		e01.setAttribute("valor", this.getDir());

		Element e02 = d.createElement("version");
		e02.setAttribute("valor", this.getVersion());

		Element e03 = d.createElement("valida");
		e03.setAttribute("valor", "" + this.getValida());

		e.appendChild(e01);
		e.appendChild(e02);
		e.appendChild(e03);

		return e;
	}

	@Override
	public void setValores(Element e) {
		Element elements[];

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("dir"));
		this.setDirDirecta(elements[0].getAttribute("valor"));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("version"));
		this.setVersionDirecta(elements[0].getAttribute("valor"));

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("valida"));
		this.setValidaDirecta(elements[0].getAttribute("valor") == "true");
	}

}