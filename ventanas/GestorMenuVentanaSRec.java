package ventanas;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import utilidades.Logger;
import conf.Conf;

/**
 * Gestiona los eventos en los menús de la Ventana principal.
 */
public class GestorMenuVentanaSRec implements MenuListener {

	@Override
	public void menuCanceled(MenuEvent e) {
		if (Conf.fichero_log) {
			Logger.log_write("Menú cancelado: "
					+ ((JMenu) (e.getSource())).getText());
		}
	}

	@Override
	public void menuDeselected(MenuEvent e) {
		if (Conf.fichero_log) {
			Logger.log_write("Menú deseleccionado: "
					+ ((JMenu) (e.getSource())).getText());
		}
	}

	@Override
	public void menuSelected(MenuEvent e) {
		if (Conf.fichero_log) {
			Logger.log_write("Menú seleccionado: "
					+ ((JMenu) (e.getSource())).getText());
		}
	}
}
