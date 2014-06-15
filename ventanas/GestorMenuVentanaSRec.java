package ventanas;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import utilidades.Logger;
import conf.Conf;

public class GestorMenuVentanaSRec implements MenuListener {


	@Override
	public void menuCanceled(MenuEvent e) {
		// TODO Auto-generated method stub
		if (Conf.fichero_log) Logger.log_write("Men� cancelado: "+((JMenu)(e.getSource())).getText());
	}




	@Override
	public void menuDeselected(MenuEvent e) {
		// TODO Auto-generated method stub
		if (Conf.fichero_log) Logger.log_write("Men� deseleccionado: "+((JMenu)(e.getSource())).getText());
	}




	@Override
	public void menuSelected(MenuEvent e) {
		// TODO Auto-generated method stub
		if (Conf.fichero_log) Logger.log_write("Men� seleccionado: "+((JMenu)(e.getSource())).getText());
	}

}
