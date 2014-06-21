package cuadros;

import java.util.ArrayList;
import javax.swing.JComboBox;

/**
 * Permite almacenar una serie de valores para parámetros de distintos tipos que
 * pueden utilizarse a posteriori para rellenar los distintos parámetros de un
 * JComboBox para que el usario pueda elegir entre los previamente
 * seleccionados.
 */
public class ValoresParametros {

	private static ArrayList<String> paramInt[] = new ArrayList[3];
	private static ArrayList<String> paramLong[] = new ArrayList[3];
	private static ArrayList<String> paramDouble[] = new ArrayList[3];
	private static ArrayList<String> paramString[] = new ArrayList[3];
	private static ArrayList<String> paramChar[] = new ArrayList[3];
	private static ArrayList<String> paramBoolean[] = new ArrayList[3];

	/**
	 * Inicializa los datos almacenados si no lo estaban.
	 * 
	 * @param forzar
	 *            Si se indica a true, se reinicializaran los datos previamente
	 *            introducidos.
	 */
	public static void inicializar(boolean forzar) {
		// Si es necesario reinicializar o se fuerza a ello...
		if (paramInt[0] == null || forzar) {
			for (int i = 0; i < paramInt.length; i++) {
				paramInt[i] = new ArrayList<String>();
				paramLong[i] = new ArrayList<String>();
				paramDouble[i] = new ArrayList<String>();
				paramString[i] = new ArrayList<String>();
				paramChar[i] = new ArrayList<String>();
				paramBoolean[i] = new ArrayList<String>();

				paramInt[i].add("");
				paramLong[i].add("");
				paramDouble[i].add("");
				paramString[i].add("");
				paramChar[i].add("");
				paramBoolean[i].add("");
			}
		}
	}

	/**
	 * Introduce el valor correspondiente en el combo box y almacena el valor.
	 * 
	 * @param jc
	 *            ComboBox donde se introducirá el valor.
	 * @param clase
	 *            tipo del parámetro.
	 * @param dim
	 *            número de dimensiones del parámetro.
	 * @param valor
	 *            Valor a introducir.
	 * @param hacerSeleccionado
	 *            true si además de introducir el valor, debe marcarse como
	 *            seleccionado, false en caso contrario.
	 */
	protected static void introducirValor(JComboBox<String> jc, String clase,
			int dim, String valor, boolean hacerSeleccionado) {
		int contenidoEn = -1;
		if (clase.contains("Integer") || clase.contains("int")
				|| clase.contains("Byte") || clase.contains("byte")
				|| clase.contains("Short") || clase.contains("short")) {
			for (int i = 0; i < paramInt[dim].size(); i++) {
				if (paramInt[dim].get(i).equals(valor)) {
					contenidoEn = i;
				}
			}
			if (contenidoEn == -1) {
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount() - 1);
			} else {
				jc.setSelectedIndex(contenidoEn);
			}
		} else if (clase.contains("Long") || clase.contains("long")) {
			for (int i = 0; i < paramLong[dim].size(); i++) {
				if (paramLong[dim].get(i).equals(valor)) {
					contenidoEn = i;
				}
			}
			if (contenidoEn == -1) {
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount() - 1);
			} else {
				jc.setSelectedIndex(contenidoEn);
			}
		} else if (clase.contains("Float") || clase.contains("float")
				|| clase.contains("Double") || clase.contains("double")) {
			for (int i = 0; i < paramDouble[dim].size(); i++) {
				if (paramDouble[dim].get(i).equals(valor)) {
					contenidoEn = i;
				}
			}
			if (contenidoEn == -1) {
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount() - 1);
			} else {
				jc.setSelectedIndex(contenidoEn);
			}
		} else if (clase.contains("String")) {
			for (int i = 0; i < paramString[dim].size(); i++) {
				if (paramString[dim].get(i).equals(valor)) {
					contenidoEn = i;
				}
			}
			if (contenidoEn == -1) {
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount() - 1);
			} else {
				jc.setSelectedIndex(contenidoEn);
			}
		} else if (clase.contains("Character") || clase.contains("char")) {
			for (int i = 0; i < paramChar[dim].size(); i++) {
				if (paramChar[dim].get(i).equals(valor)) {
					contenidoEn = i;
				}
			}
			if (contenidoEn == -1) {
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount() - 1);
			} else {
				jc.setSelectedIndex(contenidoEn);
			}
		} else if (clase.contains("boolean")) {
			for (int i = 0; i < paramBoolean[dim].size(); i++) {
				if (paramBoolean[dim].get(i).equals(valor)) {
					contenidoEn = i;
				}
			}
			if (contenidoEn == -1) {
				jc.addItem(valor);
				jc.setSelectedIndex(jc.getItemCount() - 1);
			} else {
				jc.setSelectedIndex(contenidoEn);
			}
		}
	}

	/**
	 * Popula el ComboBox con los distintos valores que esta clase tiene
	 * almacenados.
	 * 
	 * @param jc
	 *            ComboBox donde se introducirá el valor.
	 * @param clase
	 *            tipo del parámetro.
	 * @param dim
	 *            número de dimensiones del parámetro.
	 */
	protected static void introducirValores(JComboBox<String> jc, String clase,
			int dim) {
		if (clase.contains("Integer") || clase.contains("int")
				|| clase.contains("Byte") || clase.contains("byte")
				|| clase.contains("Short") || clase.contains("short")) {
			for (int i = 0; i < paramInt[dim].size(); i++) {
				jc.addItem(paramInt[dim].get(i));
			}
		} else if (clase.contains("Long") || clase.contains("long")) {
			for (int i = 0; i < paramLong[dim].size(); i++) {
				jc.addItem(paramLong[dim].get(i));
			}
		} else if (clase.contains("Float") || clase.contains("float")
				|| clase.contains("Double") || clase.contains("double")) {
			for (int i = 0; i < paramDouble[dim].size(); i++) {
				jc.addItem(paramDouble[dim].get(i));
			}
		} else if (clase.contains("String")) {
			for (int i = 0; i < paramString[dim].size(); i++) {
				jc.addItem(paramString[dim].get(i));
			}
		} else if (clase.contains("Character") || clase.contains("char")) {
			for (int i = 0; i < paramChar[dim].size(); i++) {
				jc.addItem(paramChar[dim].get(i));
			}
		} else if (clase.contains("boolean")) {
			for (int i = 0; i < paramBoolean[dim].size(); i++) {
				jc.addItem(paramBoolean[dim].get(i));
			}
		}
	}

	/**
	 * Añade el valor a los listados de valores que esta clase almacena.
	 * 
	 * @param String
	 *            Valor a añadir.
	 * @param clase
	 *            tipo del parámetro.
	 * @param dim
	 *            número de dimensiones del parámetro.
	 */
	protected static void anadirValorListados(String valor, String clase,
			int dim) {
		valor.replace(" ", "");
		boolean yaContiene = false;
		if (clase.contains("Integer") || clase.contains("int")
				|| clase.contains("Byte") || clase.contains("byte")
				|| clase.contains("Short") || clase.contains("short")) {
			for (int i = 0; i < paramInt[dim].size(); i++) {
				if (paramInt[dim].get(i).equals(valor)) {
					yaContiene = true;
				}
			}
			if (!yaContiene) {
				paramInt[dim].add(valor);
			}
		} else if (clase.contains("Long") || clase.contains("long")) {
			for (int i = 0; i < paramLong[dim].size(); i++) {
				if (paramLong[dim].get(i).equals(valor)) {
					yaContiene = true;
				}
			}
			if (!yaContiene) {
				paramLong[dim].add(valor);
			}
		} else if (clase.contains("Float") || clase.contains("float")
				|| clase.contains("Double") || clase.contains("double")) {
			for (int i = 0; i < paramDouble[dim].size(); i++) {
				if (paramDouble[dim].get(i).equals(valor)) {
					yaContiene = true;
				}
			}
			if (!yaContiene) {
				paramDouble[dim].add(valor);
			}
		} else if (clase.contains("String")) {
			for (int i = 0; i < paramString[dim].size(); i++) {
				if (paramString[dim].get(i).equals(valor)) {
					yaContiene = true;
				}
			}
			if (!yaContiene) {
				paramString[dim].add(valor);
			}
		} else if (clase.contains("Character") || clase.contains("char")) {
			for (int i = 0; i < paramChar[dim].size(); i++) {
				if (paramChar[dim].get(i).equals(valor)) {
					yaContiene = true;
				}
			}
			if (!yaContiene) {
				paramChar[dim].add(valor);
			}
		} else if (clase.contains("boolean")) {
			for (int i = 0; i < paramBoolean[dim].size(); i++) {
				if (paramBoolean[dim].get(i).equals(valor)) {
					yaContiene = true;
				}
			}
			if (!yaContiene) {
				paramBoolean[dim].add(valor);
			}
		}
	}

}
