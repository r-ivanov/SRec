/**
	Representa el cuadro de error
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;




import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;




import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;


import conf.*;
import botones.*;
import utilidades.*;


public class CuadroErrorCompilacion extends Thread implements ActionListener, KeyListener, MouseListener
{
	static final long serialVersionUID=01;

	private static int ancho = 760;
	private static int alto  = 440;
	JLabel etiqueta;
	BotonAceptar aceptar;
	JPanel panelBoton;

	String fichero;
	String errores;

	
	JDialog d;

	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/
	public CuadroErrorCompilacion(JDialog dialogo, String fichero, String errores)
	{
		this(dialogo,fichero, errores,ancho,alto);
	}
	
	/**
		Constructor: crea una nueva instancia del cuadro de error
		
		@param ventana ventana a la que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
	*/
	public CuadroErrorCompilacion(JFrame ventana, String fichero, String errores)
	{
		this(ventana,fichero, errores,ancho,alto);
	}

	/**
		Constructor: crea una nueva instancia del cuadro de error. DESECHADO
		
		@param ventana ventana a la que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
		@param comando comando que ejecutará el cuadro
		@param boton2 texto que aparecerá en el segundo botón de acción
		@param anc ancho del cuadro
		@param alt alto del cuadro
	*/	
	public CuadroErrorCompilacion(JFrame ventana,String fichero, String errores, int anc, int alt)
	{
		d=new JDialog(ventana,true);
		this.fichero=fichero;
		this.errores=errores;
		ancho=anc;
		alto=alt;
		this.start();
	}

	/**
		Constructor: crea una nueva instancia del cuadro de error. DESECHADO
		
		@param dialogo dialogo al que quedará asociado este cuadro
		@param titulo título que llevará el cuadro de error
		@param etiq mensaje que mostrará el cuadro de error
		@param comando comando que ejecutará el cuadro
		@param boton2 texto que aparecerá en el segundo botón de acción
		@param anc ancho del cuadro
		@param alt alto del cuadro
	*/		
	public CuadroErrorCompilacion(JDialog dialogo,String fichero, String errores, int anc, int alt)
	{
		d=new JDialog(dialogo,true);
		this.fichero=fichero;
		this.errores=errores;
		ancho=anc;
		alto=alt;
		this.start();
	}
	
	/**
		crea una nueva instancia del cuadro de error. DESECHADO
	*/
	public synchronized void run()
	{
		JPanel panel=new JPanel();
		this.d.setContentPane(panel);
		panel.setLayout(new BorderLayout());
				
		// Etiqueta de texto
		etiqueta=new JLabel(Texto.get("ERROR_COMP",Conf.idioma)+" "+fichero+":");
		etiqueta.addKeyListener(this);
		JPanel panelEtiqueta=new JPanel();
		panelEtiqueta.add(etiqueta);
		panelEtiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(panelEtiqueta,BorderLayout.NORTH);

		// JTextPane para errores
		
		JTextArea textPane = new JTextArea();
		textPane.setText(errores);
		textPane.setFont(new Font("Courier New",Font.PLAIN, 11));
		//textPane.setBackground(etiqueta.getBackground());

		textPane.setEditable(false);
		textPane.addKeyListener(this);

		JScrollPane jsp=new JScrollPane(textPane);
		panel.add(jsp,BorderLayout.CENTER);	// todo esto estaba antes sin comentar
		
		// Botón Aceptar
		aceptar=new BotonAceptar();//aceptar=new JButton ("Aceptar");
		//aceptar.addActionListener(this);
		aceptar.addKeyListener(this);
		aceptar.addMouseListener(this);
		
		
		
		// Panel de botón Aceptar
		panelBoton = new JPanel();
		panelBoton.add(aceptar);
		panelBoton.addKeyListener(this);
		//panelBoton.transferFocus();
		panel.add(panelBoton,BorderLayout.SOUTH);


		//aceptar.getNextFocusableComponent().transferFocus();
		//aceptar.requestFocusInWindow();
		
		if (Conf.fichero_log) 	Logger.log_write("ERROR COMPILACIÓN ["+this.fichero+"]");
		
		
		int coord[]=Conf.ubicarCentro(ancho,alto);
		d.setLocation(coord[0],coord[1]);
		d.setTitle(Texto.get("ERROR_ARCH",Conf.idioma));
		d.setSize(ancho,alto);
		
		d.setResizable(false);	
		d.setVisible(true);
		

	}

	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/
	public void actionPerformed(ActionEvent e)
	{
		d.setVisible(false);
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/			
	public void keyPressed(KeyEvent e)
	{
	}
	
	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/	
	public void keyReleased(KeyEvent e)
	{
		int code=e.getKeyCode();
		//System.out.println("keyReleased");
		if (code==KeyEvent.VK_ESCAPE || code==KeyEvent.VK_ENTER)
			d.setVisible(false);
	}

	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/	
	public void keyTyped(KeyEvent e)
	{
		int code=e.getKeyCode();
		//System.out.println("keyTyped");
		if (code==KeyEvent.VK_ENTER)
			d.setVisible(false);
	}

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseClicked(MouseEvent e) 
	{
	}
	

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseEntered(MouseEvent e) 
	{
	}
	

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseExited(MouseEvent e) 
	{
	}
	

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mousePressed(MouseEvent e) 
	{
		if (e.getComponent().equals(aceptar))
			d.setVisible(false);
	}
	

	/**
		Gestiona los eventos de ratón
		
		@param e evento de ratón
	*/		
	public void mouseReleased(MouseEvent e)
	{
	}
	

}