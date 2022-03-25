package opciones;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utilidades.ManipulacionElement;
import conf.Conf;

/**
 * Opción que permite la configuración de las distintas vistas del algoritmo y
 * su disposición.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class OpcionVistas extends Opcion {
	private static final long serialVersionUID = 1002;

	private ArrayList<Vista> vistas = new ArrayList<Vista>();

	private int posic = Conf.PANEL_HORIZONTAL;

	/**
	 * Constructor: crea una opción vacía con los valores de opción por defecto.
	 */
	public OpcionVistas() {
		super("OpcionVistas");
		if (this.vistas.size() == 0) {
			for (int i = 0; i < Vista.codigos.length; i++) {
				Vista v = new Vista(Vista.codigos[i]);
				if(i <= 2) {
					v.setTipo(Vista.TIPO_REC);
				}else if(i <= 4) {
					v.setTipo(Vista.TIPO_DYV);
				}else {
					v.setTipo(Vista.TIPO_AABB);
				}
				if (i == 0 || i == 5) {
					v.setPanel(1);
				} else {
					v.setPanel(2);
				}
				v.setActiva(i < 2 ? true : false);				
				this.vistas.add(v);
			}
		}

	}

	/**
	 * Permite obtener la vista correspondiente al código indicado.
	 * 
	 * @param codigo
	 *            "V_ARBOL", "V_PILA", "V_TRAZA", "V_ESTRUC", "V_GRAFO_DEP", 
	 *            "V_GLOBAL_VAL" o "V_RAMA_VAL"
	 * 
	 * @return Vista correspondiente o null si no existe ninguna con el código
	 *         indicado.
	 */
	public Vista getVista(String codigo) {
		for (int i = 0; i < this.vistas.size(); i++) {
			if (codigo.equals(this.vistas.get(i).getCodigo())) {
				return this.vistas.get(i);
			}
		}
		return null;
	}

	/**
	 * Permite obtener todas las vistas.
	 * 
	 * @return Lista con las vistas disponibles.
	 */
	public Vista[] getVistas() {
		Vista[] v = new Vista[this.vistas.size()];

		for (int i = 0; i < v.length; i++) {
			v[i] = this.vistas.get(i);
		}

		return v;
	}

	/**
	 * Permite añadir una nueva vista, eliminando la vista anterior que contiene
	 * el mismo código identificativo.
	 * 
	 * @param v
	 *            Vista a añadir.
	 */
	public void actualizarVista(Vista v) {
		for (int i = 0; i < this.vistas.size(); i++) {
			if (v.getCodigo().equals(this.vistas.get(i).getCodigo())) {
				Vista vieja = this.vistas.get(i);
				this.vistas.add(i, v);
				this.vistas.remove(vieja);
			}
		}

	}

	/**
	 * Permite establecer la disposición de los paneles de vistas.
	 * 
	 * @param posic
	 *            Conf.PANEL_VERTICAL o Conf.PANEL_HORIZONTAL
	 */
	public void setDisposicion(int posic) {
		this.posic = posic;
	}

	/**
	 * Permite obtener la disposición de los paneles de vistas.
	 * 
	 * @return Conf.PANEL_VERTICAL o Conf.PANEL_HORIZONTAL
	 */
	public int getDisposicion() {
		return this.posic;
	}

	@Override
	public Element getRepresentacionElement(Document d) {
		Element e = d.createElement("OpcionVistas");

		for (int i = 0; i < this.vistas.size(); i++) {
			Element eV = d.createElement("Vista");

			eV.setAttribute("codigo", this.vistas.get(i).getCodigo());
			eV.setAttribute("activa", "" + this.vistas.get(i).esActiva());
			eV.setAttribute("tipo", "" + this.vistas.get(i).getTipo());
			eV.setAttribute("panel", "" + this.vistas.get(i).getPanel());

			e.appendChild(eV);
		}

		Element eDP = d.createElement("Disposicion");
		eDP.setAttribute("posic", "" + this.posic);
		e.appendChild(eDP);

		return e;
	}

	@Override
	public void setValores(Element e) {
		Element elements[];

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("Vista"));
		for (int i = 0; i < elements.length; i++) {
			Vista v = new Vista(elements[i].getAttribute("codigo"));
			v.setActiva(elements[i].getAttribute("activa").equals("true"));
			v.setTipo(Integer.parseInt(elements[i].getAttribute("tipo")));
			v.setPanel(Integer.parseInt(elements[i].getAttribute("panel")));

			this.actualizarVista(v);
		}

		elements = ManipulacionElement.nodeListToElementArray(e
				.getElementsByTagName("Disposicion"));

		this.posic = Integer.parseInt(elements[0].getAttribute("posic"));

	}
}