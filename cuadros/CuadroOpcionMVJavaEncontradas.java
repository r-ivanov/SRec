package cuadros;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import botones.BotonAceptar;
import botones.BotonCancelar;
import conf.Conf;
import opciones.GestorOpciones;
import opciones.OpcionMVJava;
import utilidades.SelecDireccion;
import utilidades.Texto;
import ventanas.Ventana;
import cuadros.CuadroOpcionMVJava;

public class CuadroOpcionMVJavaEncontradas extends Thread implements ActionListener, KeyListener, MouseListener {
	
	private static final int ANCHO_CUADRO = 500;
	private static final int ALTO_CUADRO = 124;
	
	private JDialog dialogo;
	private Ventana ventana;
	
	private List<JRadioButton> listaRadioButtons;
	private ButtonGroup groupRadioButtons;
	private JPanel panelRadioButtons;
	
	private GridBagConstraints constraint;
	
	private JPanel panelBotones;
	private BotonAceptar aceptar;
	private BotonCancelar cancelar;	
	
	private JPanel panel;
	
	private JScrollPane panelScroll;
	
	private String[] maquinas;
	private String tituloVentana;
	private String textoVentana;
	
	private OpcionMVJava omvj = null;
	
	private GestorOpciones gOpciones = new GestorOpciones();
	OpcionMVJava omvjAux = (OpcionMVJava) this.gOpciones.getOpcion(
			"OpcionMVJava", true);
	
	/**
	 * Inicializa la clase para seleccionar la ruta de las máquinas virtuales encontradas
	 * 
	 * @param maquinas2 Array de String con las rutas encontradas a seleccionar
	 * @param ventana Ventana a la que está asociado el JDialog
	 */
	public CuadroOpcionMVJavaEncontradas(String[] maquinas2, Ventana ventana){
		this.maquinas=maquinas2;
		this.dialogo = new JDialog(ventana, true);
		this.ventana = ventana;
		this.tituloVentana = Texto.get("CSMVE_SELECTITULO",Conf.idioma);
		this.textoVentana = Texto.get("CSMVE_TEXTO",Conf.idioma);
		
		this.start();
	}
	
	/**
	 * Ejecuta el thread asociado al cuadro.
	 */
	@Override
	public void run() {
		
		this.omvj = (OpcionMVJava) this.gOpciones.getOpcion("OpcionMVJava",
				true);
		
		//	Panel radio buttons
		
		this.listaRadioButtons = new ArrayList<JRadioButton>();		
		
		this.groupRadioButtons = new ButtonGroup();
		
		this.panelRadioButtons = new JPanel(new GridBagLayout());
		
		this.constraint = new GridBagConstraints();
		this.constraint.gridx = 0;
		this.constraint.anchor = GridBagConstraints.WEST;
		
		this.panelRadioButtons.setLayout(new GridBagLayout());
		int x=0;
		String maqActiva=omvjAux.getDir() + "javac.exe"; 
		for(int i=0; i<this.maquinas.length; i++){
			this.constraint.gridy = i;
			if(!omvjAux.getDir().equals("")) {
				if(maquinas[i].equals(maqActiva)){
					x=i;
				}
			}
			
			this.listaRadioButtons.add(new JRadioButton(maquinas[i]));
			
			this.listaRadioButtons.get(i).addActionListener(this);
			this.groupRadioButtons.add(this.listaRadioButtons.get(i));
			this.panelRadioButtons.add(this.listaRadioButtons.get(i),this.constraint);
		}		
		
		this.constraint.gridy = maquinas.length;
		this.listaRadioButtons.add(new JRadioButton(Texto.get("CSMVE_MANUAL",Conf.idioma)));
		this.listaRadioButtons.get(maquinas.length).addActionListener(this);
		this.groupRadioButtons.add(this.listaRadioButtons.get(maquinas.length));
		this.panelRadioButtons.add(this.listaRadioButtons.get(maquinas.length),this.constraint);
		
	
			this.listaRadioButtons.get(x).setSelected(true);
		
		
		//	Panel scroll
		
		this.panelScroll = new JScrollPane(panelRadioButtons,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,  
				   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.panelScroll.setPreferredSize(new Dimension(ANCHO_CUADRO,ALTO_CUADRO));
		this.panelScroll.setBorder(new TitledBorder(this.textoVentana));
		
		//	Panel botones
		
		// 	Botón Aceptar
		this.aceptar = new BotonAceptar();
		this.aceptar.addKeyListener(this);
		this.aceptar.addMouseListener(this);		
		this.aceptar.setEnabled(true);				

		// Botón Cancelar
		this.cancelar = new BotonCancelar();
		this.cancelar.addKeyListener(this);
		this.cancelar.addMouseListener(this);
		this.cancelar.setEnabled(true);

		// Panel para los botones
		this.panelBotones = new JPanel();
		this.panelBotones.setLayout(new GridBagLayout());
		this.constraint = new GridBagConstraints();
		this.constraint.gridx = 0;
		this.constraint.gridy = 0;
		this.panelBotones.add(this.aceptar,this.constraint);
		this.constraint = new GridBagConstraints();
		this.constraint.gridx = 1;
		this.constraint.gridy = 0;
		this.panelBotones.add(this.cancelar,this.constraint);
		this.panelBotones.setPreferredSize(new Dimension(ANCHO_CUADRO,ALTO_CUADRO/4));
		this.panelBotones.setBorder(new EmptyBorder(0, 0, 5, 0));
		
		//	Panel general
		
		this.panel = new JPanel();
		this.panel.setLayout(new GridBagLayout());
		this.constraint = new GridBagConstraints();
		this.constraint.gridx = 0;
		this.constraint.gridy = 0;
		this.panel.add(this.panelScroll,this.constraint);
		this.constraint = new GridBagConstraints();
		this.constraint.gridx = 0;
		this.constraint.gridy = 1;
		this.panel.add(this.panelBotones,this.constraint);	
		this.panel.setPreferredSize(panel.getPreferredSize());
		
		this.dialogo.getContentPane().add(this.panel);
		
		// Añadimos todo a dialogo, que es la ventana
		
		int coord[] = Conf.ubicarCentro(ANCHO_CUADRO, ALTO_CUADRO);
		this.dialogo.setLocation(coord[0], coord[1]);
		//this.dialogo.setPreferredSize(new Dimension(ANCHO_CUADRO, ALTO_CUADRO));
		this.dialogo.setResizable(false);
		this.dialogo.setTitle(Texto.get("CSMVE_SELECTITULO", Conf.idioma));
		this.dialogo.validate();
		this.dialogo.pack();		
		this.dialogo.setVisible(true);
		this.dialogo.setAlwaysOnTop(true);
		
	}
	
	/**
	 * Comprueba que la dirección seleccionada es correcta
	 * En caso de seleccionar la opción manual, abre la ventana para escribir la máquina virtual
	 * 
	 * @param aceptarPulsado
	 *            true si el usuario pulsó el botón Aceptar false si el método
	 *            es ejecutado por la propia aplicación de manera interna 
	 *            o mediante el botón examinar o mediante la inserción / modificación de texto
	 * @return true si la dirección es válida
	 */
	private boolean valorarSeleccion(boolean aceptarPulsado) {		
		
		String opcion = this.obtenerSeleccionado();
		
		//	Si selecciona una máquina (en principio debería de ser válida...)
		if(!opcion.equals("Error") && !opcion.equals(Texto.get("CSMVE_MANUAL",Conf.idioma))){
			this.omvj.setDir(opcion);
			if (this.omvj.getValida()) {
				this.aceptar.setEnabled(true);
				if (aceptarPulsado) {
					this.gOpciones.setOpcion(this.omvj, 2);
					this.dialogo.setVisible(false);
					new CuadroInformacion(this.ventana, Texto.get("INFO_MVJOK",
							Conf.idioma), Texto.get("INFO_MVJAVA", Conf.idioma),
							550, 100);
				}
				return true;
			} else {
				this.aceptar.setEnabled(false);
				if (aceptarPulsado) {
					new CuadroError(this.dialogo, Texto.get("ERROR_MVJAVA",
							Conf.idioma), Texto.get("ERROR_NOMVJ", Conf.idioma));
				}
				return false;
			}
			
		//	Si selecciona opción manual
		}else if(opcion.equals(Texto.get("CSMVE_MANUAL",Conf.idioma))){
			new CuadroOpcionMVJava(this.ventana, true);
			this.dialogo.setVisible(false);
			return false;
		}else{
			return false;
		}
		
	}
	
	/**
	 * Permite obtener el texto del radio button seleccionado
	 * 
	 * @return Texto del radio button seleccionado
	 */
	private String obtenerSeleccionado(){
		for(int i = 0; i<this.listaRadioButtons.size();i++){
			if(this.listaRadioButtons.get(i).isSelected()){
				return listaRadioButtons.get(i).getText();
			}
		}
		return "Error";
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (this.aceptar.isEnabled() && e.getSource() == this.aceptar) {			
			valorarSeleccion(true);
		} else if (e.getSource() == this.cancelar) {
			this.dialogo.setVisible(false);
		}		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE) {
			this.dialogo.setVisible(false);
		} else if (code == KeyEvent.VK_ENTER && this.aceptar.isFocusOwner()) {			
			valorarSeleccion(true);			
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}	

}
