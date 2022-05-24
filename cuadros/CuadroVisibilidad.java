package cuadros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import conf.*;
import botones.*;
import datos.*;
import utilidades.*;
import ventanas.*;

/**
 * Permite modificar la visibilidad de los distintos métodos y parámetros que se
 * muestran en las vistas de ejecución.
 * 
 * @author Antonio Pérez Carrasco
 * @version 2006-2007
 */
public class CuadroVisibilidad extends Thread implements ActionListener,
		KeyListener {

	private static final int ANCHO_CUADRO = 275;
	private static final int ALTO_CUADRO = 600;

	private DatosTrazaBasicos dtb;

	private JCheckBox botonesMetodos[];
	private JCheckBox botonesParametros[][];

	private Ventana ventana;
	private JDialog dialogo;

	private BotonAceptar aceptar = new BotonAceptar();
	private BotonCancelar cancelar = new BotonCancelar();

	private int numeroFilas = 0;
	
	private ClaseAlgoritmo ca;

	/**
	 * Genera un nuevo cuadro que permite modificar la visibilidad de los
	 * distintos métodos y parámetros que se muestran en las vistas de
	 * ejecución.
	 * 
	 * @param ventana
	 *            Ventana a la que quedará asociado el cuadro.
	 * @param dtb
	 *            Datos de traza básicos de la traza en ejecución.
	 */
	public CuadroVisibilidad(Ventana ventana, DatosTrazaBasicos dtb, ClaseAlgoritmo ca) {
		if (dtb != null) {
			this.dialogo = new JDialog(ventana, true);
			this.ventana = ventana;
			this.dtb = dtb;
			this.ca = ca;
			this.start();
		}
	}

	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {

		// Obtenemos el número de filas que tendrá que tener el panel interior:
		// dos por método y una por cada parámetro de cada método
		this.numeroFilas = this.dtb.getNumMetodos() * 2 - 1;

		for (int i = 0; i < this.dtb.getNumMetodos(); i++) {
			this.numeroFilas += this.dtb.getMetodo(i).getNumParametrosE();
			this.numeroFilas += this.dtb.getMetodo(i).getNumParametrosS();
		}

		this.botonesMetodos = new JCheckBox[this.dtb.getNumMetodos()];
		this.botonesParametros = new JCheckBox[this.dtb.getNumMetodos()][];

		// Panel Datos que se muestran
		JPanel panelDatos = new JPanel();
		GridLayout gl1 = new GridLayout(this.numeroFilas, 1);
		panelDatos.setLayout(gl1);
		panelDatos.setBorder(new TitledBorder(Texto.get("CVIS_BORDER",
				Conf.idioma)));

		for (int i = 0; i < this.dtb.getNumMetodos(); i++) {
			DatosMetodoBasicos dmb = this.dtb.getMetodo(i);			
			
			for(MetodoAlgoritmo ma:this.ca.getMetodos()) {
				if(dmb.getNombre().equals(ma.getNombre())) {
					dmb.setEsVisible(ma.getMarcadoVisualizar());
				}
			}			
			
			panelDatos.add(creaPanelMetodo(dmb.getEsVisible(),
					dmb.getEsPrincipal(), i, dmb.getNombre()));

			if (dmb.esMetodoConRetorno()) {
				this.botonesParametros[i] = new JCheckBox[this.dtb.getMetodo(i)
						.getNumParametrosE() + 1];
			} else {
				this.botonesParametros[i] = new JCheckBox[this.dtb.getMetodo(i)
						.getNumParametrosE() * 2];
			}

			for (int j = 0; j < dmb.getNumParametrosE(); j++) {
				panelDatos.add(creaPanelParametro(
						Texto.get("ETIQFL_ENTR", Conf.idioma),
						dmb.getNumParametrosE() == 1, dmb.getVisibilidadE(j),
						i, j, dmb.getNombreParametroE(j),
						dmb.getTipoParametroE(j), dmb.getDimParametroE(j)));
			}

			if (!dmb.esMetodoConRetorno()) {
				for (int j = 0; j < dmb.getNumParametrosS(); j++) {
					panelDatos.add(creaPanelParametro(
							Texto.get("ETIQFL_SALI", Conf.idioma),
							dmb.getNumParametrosS() == 1,
							dmb.getVisibilidadS(j), i,
							j + dmb.getNumParametrosE(),
							dmb.getNombreParametroS(j),
							dmb.getTipoParametroS(j), dmb.getDimParametroS(j)));
				}
			} else {
				panelDatos.add(creaPanelParametro(
						Texto.get("NOMBRE_RETORNO", Conf.idioma), true,
						dmb.getVisibilidadS(0), i, dmb.getNumParametrosE(), "",
						dmb.getTipoParametroS(0), dmb.getDimParametroS(0)));
			}
			if (i < (this.dtb.getNumMetodos() - 1)) {
				panelDatos.add(new JSeparator(SwingConstants.HORIZONTAL));
			}
		}

		ajustarChecks();

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(this.aceptar);
		panelBoton.add(this.cancelar);

		this.aceptar.addActionListener(this);
		this.aceptar.addKeyListener(this);
		this.cancelar.addActionListener(this);
		this.cancelar.addKeyListener(this);

		// Panel general
		JScrollPane jsp = new JScrollPane(panelDatos);
		jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
		jsp.setPreferredSize(new Dimension(ANCHO_CUADRO - 10,
				ALTO_CUADRO - 80));
		BorderLayout bl = new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);

		panel.add(jsp, BorderLayout.NORTH);
		panel.add(panelBoton, BorderLayout.SOUTH);

		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(Texto.get("CVIS_VIS", Conf.idioma));
		this.dialogo.setSize(ANCHO_CUADRO, ALTO_CUADRO);
				
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO,
				ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);
		
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}

	/**
	 * Crea un panel para un determinado método.
	 * 
	 * @param valorCheck
	 *            true si el checkbox debe aparecer seleccionado, false en caso
	 *            contrario.
	 * @param activoCheck
	 *            true si debe estar habilitado, false en caso contrario.
	 * @param i
	 *            Número del método.
	 * @param nombreMetodo
	 *            Nombre del método.
	 * 
	 * @return panel de selección para el método.
	 */
	private JPanel creaPanelMetodo(boolean valorCheck, boolean activoCheck,
			int i, String nombreMetodo) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.botonesMetodos[i] = new JCheckBox(nombreMetodo);
		this.botonesMetodos[i].addActionListener(this);
		this.botonesMetodos[i].addKeyListener(this);
		panel.add(this.botonesMetodos[i], BorderLayout.WEST);
		this.botonesMetodos[i].setSelected(valorCheck);
		this.botonesMetodos[i].setEnabled(!activoCheck);

		return panel;
	}

	/**
	 * Crea un panel para un determinado parámetro de un método.
	 * 
	 * @param unico
	 *            true si es el único parámetro que puede mostrarse.
	 * @param valorCheck
	 *            true si debe estar seleccionado, false en caso contrario.
	 * @param i
	 *            Número del método.
	 * @param j
	 *            Número del parámetro.
	 * @param nombreParametro
	 *            Nombre del parámetro.
	 * @param tipoParametro
	 *            Tipo del parámetro.
	 * @param dimParametro
	 *            Número de dimensiones para el parámetro.
	 * 
	 * @return panel de selección para el parámetro.
	 */
	private JPanel creaPanelParametro(String textoLugarParam, boolean unico,
			boolean valorCheck, int i, int j, String nombreParametro,
			String tipoParametro, int dimParametro) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel panelInt = new JPanel();
		panelInt.setPreferredSize(new Dimension(20, 20));
		panel.add(panelInt, BorderLayout.WEST);
		if (nombreParametro.length() != 0) {
			this.botonesParametros[i][j] = new JCheckBox(nombreParametro + ": "
					+ tipoParametro + " "
					+ ServiciosString.cadenaDimensiones(dimParametro) + " ("
					+ textoLugarParam + ")");
		} else {
			this.botonesParametros[i][j] = new JCheckBox(textoLugarParam + ": "
					+ tipoParametro + " "
					+ ServiciosString.cadenaDimensiones(dimParametro));
		}
		this.botonesParametros[i][j].addActionListener(this);
		this.botonesParametros[i][j].addKeyListener(this);

		this.botonesParametros[i][j].setSelected(valorCheck);
		this.botonesParametros[i][j].setEnabled(!unico);
		panel.add(this.botonesParametros[i][j], BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Actualiza todos los checkboxes con el valor adecuado.
	 */
	private void ajustarChecks() {
		for (int i = 0; i < this.botonesMetodos.length; i++) {
			ajustarChecksMetodo(this.botonesMetodos[i],
					this.botonesParametros[i], this.dtb.getMetodo(i)
							.esMetodoConRetorno());
		}
	}

	/**
	 * Actualiza los checkboxes de un método con el valor adecuado.
	 * 
	 * @param metodo
	 *            Contenedor del método.
	 * @param parametros
	 *            Array de contenedores con los parámetros
	 * @param devuelveValor
	 *            true si el método devuelve un valor, false en caso contrario.
	 */
	private void ajustarChecksMetodo(JCheckBox metodo, JCheckBox[] parametros,
			boolean devuelveValor) {
		if (metodo.isSelected()) {
			int longitudRecorrido = 0;

			// Si es método que no devuelve, recorremos mitad array
			if (!devuelveValor) {
				longitudRecorrido = parametros.length / 2;

				for (int i = longitudRecorrido; i < parametros.length; i++) {
					parametros[i].setEnabled(true);
				}
			} else {
				// Si no, recorremos todas las posiciones menos la última, del
				// valor de retorno
				longitudRecorrido = parametros.length ;//- 1;
			}

			for (int i = 0; i < longitudRecorrido; i++) {
				parametros[i].setEnabled(true);
			}

			// Comprobamos cuántos parametros quedan seleccionados
			int paramSeleccionados = 0;
			for (int i = 0; i < longitudRecorrido; i++) {
				if (parametros[i].isSelected()) {
					paramSeleccionados++;
				}
			}

			// Si sólo uno, lo deshabilitamos para que no se pueda eliminar de
			// la animación,
			// Si no, habilitamos todos.
			/*
			if (paramSeleccionados == 1) {
				for (int i = 0; i < longitudRecorrido; i++) {
					if (parametros[i].isSelected()) {
						parametros[i].setEnabled(false);
					}
				}
			} else {
				for (int i = 0; i < longitudRecorrido; i++) {
					parametros[i].setEnabled(true);
				}
			}^*/ //Prueba de fran
			for (int i = 0; i < longitudRecorrido; i++) {
				parametros[i].setEnabled(true);
			}

		} else {
			for (int i = 0; i < parametros.length; i++) {
				parametros[i].setEnabled(false);
			}
		}
	}

	/**
	 * Comprueba que de cada método visible haya al menos un parámetro de
	 * entrada y uno de salida visible.
	 * 
	 * @return nombre del método erróneo o null si todo es correcto.
	 */
	private String comprobarChecks() {
		for (int i = 0; i < this.botonesParametros.length; i++) {
			DatosMetodoBasicos dmb = this.dtb.getMetodo(i);
			boolean unoActivo = false;
			
			if (dmb.esMetodoConRetorno()) {
				for (int j = 0; j < this.botonesParametros[i].length - 1; j++) {
					if (this.botonesParametros[i][j].isSelected()) {
						unoActivo = true;
					}
				}

				if (unoActivo) {
					unoActivo = this.botonesParametros[i][this.botonesParametros[i].length - 1]
							.isSelected();
				}

				if (!unoActivo && this.botonesMetodos[i].isSelected()) {
					return dmb.getNombre();
				}
			} else if(dmb.getNumParametrosE() == 0 && dmb.getNumParametrosS() == 0) {
				unoActivo = true;
			} else {
				for (int j = 0; j < this.botonesParametros[i].length / 2; j++) {
					if (this.botonesParametros[i][j].isSelected()) {
						unoActivo = true;
					}
				}

				if (!unoActivo && this.botonesMetodos[i].isSelected()) {
					return dmb.getNombre();
				}
			}
		}

		return null;
	}

	/**
	 * Gestiona los eventos de acción
	 * 
	 * @param e
	 *            evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JCheckBox) {
			ajustarChecks();
		}

		String metodoError = null;//comprobarChecks();
		if (metodoError != null) {
			new CuadroError(this.dialogo,
					Texto.get("ERROR_PARAM", Conf.idioma), Texto.get(
							"ERROR_VISIBIGRAL", Conf.idioma)
							+ " ("
							+ metodoError + ")");
		} else if (e.getSource() == this.aceptar) {
			actuar();
		} else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
			dialogo.dispose();
		}

	}

	/**
	 * Recoge los valores seleccionados y aplica los cambios sobre el árbol.
	 */
	private void actuar() {
		for (int i = 0; i < this.botonesMetodos.length; i++) {
			this.dtb.getMetodo(i).setEsVisible(
					this.botonesMetodos[i].isSelected());

			if (this.dtb.getMetodo(i).esMetodoConRetorno()) {
				for (int j = 0; j < this.botonesParametros[i].length - 1; j++) {
					this.dtb.getMetodo(i).setVisibilidadE(
							this.botonesParametros[i][j].isSelected(), j);
				}
				this.dtb.getMetodo(i)
						.setVisibilidadS(
								this.botonesParametros[i][this.botonesParametros[i].length - 1]
										.isSelected(), 0);
			} else {
				for (int j = 0; j < this.botonesParametros[i].length / 2; j++) {
					this.dtb.getMetodo(i).setVisibilidadE(
							this.botonesParametros[i][j].isSelected(), j);
				}
				for (int j = this.botonesParametros[i].length / 2; j < this.botonesParametros[i].length; j++) {
					this.dtb.getMetodo(i).setVisibilidadS(
							this.botonesParametros[i][j].isSelected(),
							j - this.botonesParametros[i].length / 2);
				}
			}
		}
		
		ClaseAlgoritmo claseAlgoritmo = Ventana.getInstance().getClase();
		if (claseAlgoritmo != null) {
			for (DatosMetodoBasicos metodo : this.dtb.getMetodos()) {
				for (MetodoAlgoritmo metodoAlgoritmo : claseAlgoritmo.getMetodos()) {
					if (metodo.esIgual(metodoAlgoritmo)) {
						
						metodoAlgoritmo.setMarcadoVisualizar(metodo.getEsVisible());
						
						for (int indiceEntrada = 0; indiceEntrada < metodo.getNumParametrosE(); indiceEntrada++) {
							metodoAlgoritmo.setVisibilidadEntrada(metodo.getVisibilidadE(indiceEntrada),
											indiceEntrada);
							if(metodoAlgoritmo.getTecnica() == MetodoAlgoritmo.TECNICA_AABB) {
								int indiceSolActual = metodoAlgoritmo.getSolParcial();
								int indiceSolMejor = metodoAlgoritmo.getMejorSol();
								int indiceCota = metodoAlgoritmo.getCota();
								if(indiceEntrada == indiceSolActual) {
									Conf.solActualVisible = metodo.getVisibilidadE(indiceEntrada);
								}else if(indiceEntrada == indiceSolMejor) {
									Conf.solMejorVisible = metodo.getVisibilidadE(indiceEntrada);
								}else if(indiceEntrada == indiceCota) {
									Conf.cotaVisible = metodo.getVisibilidadE(indiceEntrada);
								}
							}
						}
						for (int indiceSalida = 0; indiceSalida < metodo.getNumParametrosS(); indiceSalida++) {
							metodoAlgoritmo.setVisibilidadSalida(metodo.getVisibilidadS(indiceSalida),
											indiceSalida);
						}
					}
				}
			}
		}

		Conf.setRedibujarGrafoArbol(true);
		this.ventana.setDTB(this.dtb);
		this.ventana.actualizarEstadoTrazaCompleta();
		this.ventana.refrescarOpciones();
		this.ventana.refrescarFormato();
		Conf.setRedibujarGrafoArbol(false);

		this.dialogo.setVisible(false);
		dialogo.dispose();
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyPressed(KeyEvent e) {

	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
			dialogo.dispose();
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() instanceof JCheckBox) {
				ajustarChecks();
			}

			String metodoError = comprobarChecks();
			if (metodoError != null) {
				new CuadroError(this.dialogo, Texto.get("ERROR_PARAM",
						Conf.idioma),
						Texto.get("ERROR_VISIBIGRAL", Conf.idioma) + " ("
								+ metodoError + ")");
			}

			actuar();
		}

		if (e.getSource() instanceof JCheckBox) {
			ajustarChecks();
		}
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param e
	 *            evento de teclado
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}

}
