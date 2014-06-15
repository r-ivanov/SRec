/**
	Implementa el cuadro de progreso que informa del estado del procesamiento del fichero que se va a ejecutar
	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;




import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import conf.*;
import botones.*;
import utilidades.*;
import ventanas.*;



public class CuadroProgreso extends Thread //implements ActionListener
{
	static final long serialVersionUID=42;

	static final boolean depurar=false;

	final int anchoCuadro=450;
	final int altoCuadro=110;

	JLabel etiqueta;
	JPanel panel, panelTexto, panelProgreso;
	JProgressBar barra;

	BorderLayout bl;
	GridLayout gl;
	JDialog dialogo;
	JFrame referenciaVentana;
	
	String nombre, texto;
	int valor;

	/**
		Constructor: crea un nuevo cuadro de progreso
		
		@param ventanaVisualizador Ventana a la que quedará asociado
		@param nombre Título que aparecerá en el cuadro
		@param texto texto que se colocará en primera instancia en la etiqueta del panel
		@param valor valor inicial que se asignará a la barra
	*/
	public CuadroProgreso(JFrame ventanaVisualizador,String nombre, String texto, int valor)
	{	
		this.dialogo=new JDialog(ventanaVisualizador,true);
		//this.setAlwaysOnTop(true);

		// Inicializamos atributos del cuadro de parámetros
		
		this.nombre=nombre;
		this.texto=texto;
		this.valor=valor;
		this.etiqueta=new JLabel();
		
		this.barra = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		barra.setSize(300,35);
		this.start();
	}
		
	public void run()
	{
		// Panel de texto
		panelTexto = new JPanel();
		panelTexto.add(etiqueta);
		panelTexto.setBorder(new TitledBorder(Texto.get("CP_PROGPROC",Conf.idioma)));
		panelTexto.setSize(320,80);

		// Panel de progreso
		gl = new GridLayout(0,1);
		panelProgreso = new JPanel();
		panelProgreso.setLayout(gl);
		//panelProgreso.setSize(320,35);
		panelProgreso.add(this.barra);
		panelProgreso.setBorder(new TitledBorder(""));


		// Panel general
		bl= new BorderLayout();
		panel = new JPanel();
		panel.setLayout(bl);

		panel.add(panelProgreso,BorderLayout.SOUTH);
		panel.add(panelTexto,BorderLayout.NORTH);
		
		// Inicializacion de valores
		setValores(texto,valor);

		this.dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		// Preparamos y mostramos cuadro
		this.dialogo.getContentPane().add(panel);
		this.dialogo.setTitle(nombre);
		int coord[]=Conf.ubicarCentro(anchoCuadro,altoCuadro);
		this.dialogo.setLocation(coord[0],coord[1]);
		this.dialogo.setSize(anchoCuadro,altoCuadro);
		this.dialogo.setResizable(false);
		this.dialogo.setVisible(true);
	}


	
	/**
		Asigna valores a la etiqueta y a la barra
		
		@param texto texto que aparecerá en la etiqueta
		@param valor valor que quedará representado en la barra
	*/
	public void setValores(String texto, int valor)
	{
		etiqueta.setText(texto);
		if (valor>=barra.getMinimum() && valor<=barra.getMaximum())
			barra.setValue(valor);
	}

	/**
		Asigna valor a la etiqueta
		
		@param texto texto que aparecerá en la etiqueta
	*/
	public void setValores(String texto)
	{
		this.etiqueta.setText(texto);
	}

	/**
		Asigna valor a la barra
		
		@param valor valor que quedará representado en la barra
	*/
	public void setValores(int valor)
	{
		if (valor>=barra.getMinimum() && valor<=barra.getMaximum())
			barra.setValue(valor);
	}

	/**
		Hace invisible el cuadro, una vez ha finalizado el procesamiento
	*/
	public void cerrar()
	{
		this.dialogo.setVisible(false);
	}

	/**
		Hace invisible el cuadro, una vez ha finalizado el procesamiento
	*/
	public void abrir()
	{
		this.dialogo.setVisible(true);
	}
}
