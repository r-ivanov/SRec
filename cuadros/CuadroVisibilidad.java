/**	
	@author Antonio Pérez Carrasco
	@version 2006-2007
*/

package cuadros;





import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import conf.*;
import botones.*;
import datos.*;
import utilidades.*;
import ventanas.*;

public class CuadroVisibilidad extends Thread implements ActionListener, KeyListener
{
	static final long serialVersionUID=07;


	DatosTrazaBasicos dtb;
	
	final int anchoCuadro=275;


	JCheckBox botonesMetodos[];
	JCheckBox botonesParametros[][];
	
	JDialog dialogo;
	
	BotonAceptar aceptar=new BotonAceptar();
	BotonCancelar cancelar=new BotonCancelar();
	
	
	int numeroFilas=0;
	
	
	/**
		Constructor: genera un nuevo cuadro de opción
		
		@param ventanaVisualizador Ventana a la que permanecerá asociado el cuadro de diálogo
	*/
	public CuadroVisibilidad (DatosTrazaBasicos dtb)
	{	
		if (dtb!=null)
		{
			dialogo=new JDialog(Ventana.thisventana,true);
			this.dtb=dtb;
			
			this.start();
		}
	}

	/**
		Genera una nueva opción
	*/
	
	public void run()
	{
		// Obtenemos el número de filas que tendrá que tener el panel interior: dos por método y una por cada parámetro de cada método
		numeroFilas=dtb.getNumMetodos()*2-1;
		
		for (int i=0; i<dtb.getNumMetodos(); i++)
		{
			numeroFilas+=dtb.getMetodo(i).getNumParametrosE();
			numeroFilas+=dtb.getMetodo(i).getNumParametrosS();
		}

		botonesMetodos=new JCheckBox[dtb.getNumMetodos()];
		botonesParametros=new JCheckBox[dtb.getNumMetodos()][];
	
		// Panel Datos que se muestran
		JPanel panelDatos = new JPanel();
		GridLayout gl1 = new GridLayout(numeroFilas,1);
		panelDatos.setLayout(gl1);
		panelDatos.setBorder(new TitledBorder(Texto.get("CVIS_BORDER",Conf.idioma)));
		
		for (int i=0; i<dtb.getNumMetodos(); i++)
		{
			DatosMetodoBasicos dmb=dtb.getMetodo(i);
			panelDatos.add(creaPanelMetodo(dmb.getEsVisible(),dmb.getEsPrincipal(),i,dmb.getNombre())  );
			

			if (dmb.esMetodoConRetorno())
				botonesParametros[i]=new JCheckBox[dtb.getMetodo(i).getNumParametrosE()+1];
			else
				botonesParametros[i]=new JCheckBox[dtb.getMetodo(i).getNumParametrosE()*2];


			for (int j=0; j<dmb.getNumParametrosE(); j++)
			{
				panelDatos.add(  creaPanelParametro(Texto.get("ETIQFL_ENTR",Conf.idioma),dmb.getNumParametrosE()==1,dmb.getVisibilidadE(j),
					i,j,dmb.getNombreParametroE(j),dmb.getTipoParametroE(j),dmb.getDimParametroE(j)));
			}
			
			if (!dmb.esMetodoConRetorno())
				for (int j=0; j<dmb.getNumParametrosS(); j++)
				{
					panelDatos.add(  creaPanelParametro(Texto.get("ETIQFL_SALI",Conf.idioma),dmb.getNumParametrosS()==1,dmb.getVisibilidadS(j),
						i,j+dmb.getNumParametrosE(),dmb.getNombreParametroS(j),dmb.getTipoParametroS(j),dmb.getDimParametroS(j)));
				}
			else
			{
				panelDatos.add(  creaPanelParametro(Texto.get("NOMBRE_RETORNO",Conf.idioma),true,dmb.getVisibilidadS(0),
					i,dmb.getNumParametrosE(),"",dmb.getTipoParametroS(0),dmb.getDimParametroS(0)));
			}
			if (i<(dtb.getNumMetodos()-1))
				panelDatos.add (new JSeparator(SwingConstants.HORIZONTAL));
			
			
			/*System.out.println("VISIBLIDAD ---- ["+dmb.getNombre()+"]  "+i+" // "+botonesParametros[i].length);
			for (int j=0; j<dmb.getNumParametrosE(); j++)
				System.out.println("(1)botonesParametros["+i+"]["+j+"]="+botonesParametros[i][j].getText());
			if (!dmb.esMetodoConRetorno())
				for (int j=0; j<dmb.getNumParametrosS(); j++)
					System.out.println("(2)botonesParametros["+i+"]["+(j+dmb.getNumParametrosE())+"]="+botonesParametros[i][j+dmb.getNumParametrosS()].getText());
			else
				System.out.println("(3)botonesParametros["+i+"]["+(dmb.getNumParametrosE())+"]="+botonesParametros[i][dmb.getNumParametrosE()].getText());
			*/
			
		}
		
		ajustarChecks();

	
		
		

		// Panel para el botón
		JPanel panelBoton = new JPanel();
		panelBoton.add(aceptar);
		panelBoton.add(cancelar);

		//aceptar.addActionListener(this);
		aceptar.addActionListener(this);
		aceptar.addKeyListener(this);
		cancelar.addActionListener(this);
		cancelar.addKeyListener(this);
	
		// Panel general
		BorderLayout bl= new BorderLayout();
		JPanel panel = new JPanel();
		panel.setLayout(bl);
	
		panel.add(panelDatos,BorderLayout.NORTH);
		panel.add(panelBoton,BorderLayout.SOUTH);
			
		// Preparamos y mostramos cuadro
		dialogo.getContentPane().add(panel);
		dialogo.setTitle(Texto.get("CVIS_VIS",Conf.idioma));

		if (Ventana.thisventana.msWindows)
		{
			dialogo.setSize(anchoCuadro,numeroFilas*23+90);
			int coord[]=Conf.ubicarCentro(anchoCuadro,numeroFilas*23+90);
			dialogo.setLocation(coord[0],coord[1]);
		}
		else
		{
			dialogo.setSize(anchoCuadro,numeroFilas*23+90);
			int coord[]=Conf.ubicarCentro(anchoCuadro,numeroFilas*23+90);
			dialogo.setLocation(coord[0],coord[1]);
		}
		dialogo.setResizable(false);
		dialogo.setVisible(true);
	}

	
	
	
	private JPanel creaPanelMetodo(boolean valorCheck, boolean activoCheck, int i, String nombreMetodo)
	{
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		botonesMetodos[i]=new JCheckBox(nombreMetodo);
		botonesMetodos[i].addActionListener(this);
		botonesMetodos[i].addKeyListener(this);
		panel.add(botonesMetodos[i],BorderLayout.WEST);
		botonesMetodos[i].setSelected(valorCheck);
		botonesMetodos[i].setEnabled(!activoCheck);

		return panel;
	}
	
	private JPanel creaPanelParametro(String textoLugarParam, boolean unico, boolean valorCheck, 
						int i, int j, String nombreParametro, String tipoParametro, int dimParametro)
	{
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel panelInt=new JPanel();
		panelInt.setPreferredSize(new Dimension(20,20));
		panel.add(panelInt,BorderLayout.WEST);
		if (nombreParametro.length()!=0)
			botonesParametros[i][j]=new JCheckBox(nombreParametro+": "+tipoParametro+" "+ServiciosString.cadenaDimensiones(dimParametro)+" ("+textoLugarParam+")");
		else
			botonesParametros[i][j]=new JCheckBox(textoLugarParam+": "+tipoParametro+" "+ServiciosString.cadenaDimensiones(dimParametro));
		botonesParametros[i][j].addActionListener(this);
		botonesParametros[i][j].addKeyListener(this);
		
		botonesParametros[i][j].setSelected(valorCheck);
		botonesParametros[i][j].setEnabled(!unico);
		panel.add(botonesParametros[i][j],BorderLayout.CENTER);
		
		return panel;
	}	
	
	
	
	
	private void ajustarChecks()
	{
		for (int i=0; i<botonesMetodos.length; i++)
		{
			//System.out.println("Longitud de botonesParametros["+i+"]="+botonesParametros[i].length+" >> "+this.dtb.getMetodo(i).getNombre());
			
			ajustarChecksMetodo(botonesMetodos[i],botonesParametros[i],this.dtb.getMetodo(i).esMetodoConRetorno());
			
			
			
			/*

			
			// Habilitamos checks en función de restricciones mínimas de visibilidad 
			
			int longitudRecorrido=0;
			int tipoMetodo=0;	// 0=void , 1=devuelve valor
			if (botonesParametros[i][botonesParametros[i].length-1].isEnabled())	// Si es método que no devuelve, recorremos mitad array
				longitudRecorrido=botonesParametros[i].length/2;
			else				// Si no, recorremos todas las posiciones menos la última, del valor de retorno
			{
				longitudRecorrido=botonesParametros[i].length-1;
				tipoMetodo=1;
			}
					
			
			int paramSeleccionados=0;	// Comprobamos cuántos parametros quedan seleccionados
			for (int k=0; k<longitudRecorrido; k++)
			{
				if (botonesParametros[i][k].isSelected())
					paramSeleccionados++;
			}
			
			if (paramSeleccionados==1)// Si sólo uno, lo deshabilitamos para que no se pueda eliminar de la animación
			{
				for (int k=0; k<longitudRecorrido; k++)
				{
					if (botonesParametros[i][k].isSelected())
						botonesParametros[i][k].setEnabled(false);
				}
			}
			else // Si no, habilitamos todos
			{
				if (tipoMetodo==0)
					for (int k=0; k<botonesParametros[i].length; k++)
						botonesParametros[i][k].setEnabled(true);
				else
					for (int k=0; k<longitudRecorrido; k++)
						botonesParametros[i][k].setEnabled(true);

			}
			
			// Deshabilitamos todos los check de los métodos no visibles...
			if (!botonesMetodos[i].isSelected())
				for (int j=0; j<botonesParametros[i].length; j++)
					botonesParametros[i][j].setEnabled(false);*/
		}
		
		
		
		
	}
	
	
	private void ajustarChecksMetodo(JCheckBox metodo, JCheckBox[] parametros, boolean devuelveValor)
	{
		if (metodo.isSelected())
		{
			//System.out.println("Método "+metodo.getText());
			int longitudRecorrido=0;

			if (!devuelveValor)	// Si es método que no devuelve, recorremos mitad array
			{
				longitudRecorrido=parametros.length/2;
				
				for (int i=longitudRecorrido; i<parametros.length; i++)
					parametros[i].setEnabled(true);
			}
			else				// Si no, recorremos todas las posiciones menos la última, del valor de retorno
				longitudRecorrido=parametros.length-1;
			
			for (int i=0; i<longitudRecorrido; i++)
				parametros[i].setEnabled(true);
			
			
			int paramSeleccionados=0;	// Comprobamos cuántos parametros quedan seleccionados
			for (int i=0; i<longitudRecorrido; i++)
			{
				if (parametros[i].isSelected())
					paramSeleccionados++;
			}
			
			if (paramSeleccionados==1)// Si sólo uno, lo deshabilitamos para que no se pueda eliminar de la animación
			{
				for (int i=0; i<longitudRecorrido; i++)
				{
					if (parametros[i].isSelected())
						parametros[i].setEnabled(false);
				}
			}
			else // Si no, habilitamos todos
			{
				for (int i=0; i<longitudRecorrido; i++)
					parametros[i].setEnabled(true);
			}

		}
		else
		{
			for (int i=0; i<parametros.length; i++)
				parametros[i].setEnabled(false);
		}
	}
	
	
	
	
	private String comprobarChecks()
	{
		// Habilitamos y deshabilitamos checkboxes de métodos
		//for (int i=0; i<botonesMetodos.length; i++)
		//	for (int j=0; j<botonesParametros[i].length; j++)
		//		botonesParametros[i][j].setEnabled(botonesMetodos[i].isSelected());
		
	
	
	
		// Comprobamos que de cada método visible haya al menos un parámetro de entrada y uno de salida visible
		for (int i=0; i<botonesParametros.length; i++)
		{
			DatosMetodoBasicos dmb=dtb.getMetodo(i);
			boolean unoActivo=false;
			
			if (dmb.esMetodoConRetorno())
			{
				for (int j=0; j<botonesParametros[i].length-1; j++)
				{
					if (botonesParametros[i][j].isSelected())
						unoActivo=true;
				}
				
				if (unoActivo)
					unoActivo=botonesParametros[i][botonesParametros[i].length-1].isSelected();
				
				
				if (!unoActivo && botonesMetodos[i].isSelected())
					return dmb.getNombre();
			}
			else
			{
				for (int j=0; j<botonesParametros[i].length/2; j++)
					if (botonesParametros[i][j].isSelected())
						unoActivo=true;
				
				/*if (unoActivo)
				{
					unoActivo=false;
					for (int j=botonesParametros[i].length/2; j<botonesParametros[i].length; j++)
						if (botonesParametros[i][j].isSelected())
							unoActivo=true;
				}*/
				
				
				if (!unoActivo && botonesMetodos[i].isSelected())
					return dmb.getNombre();
			}
		}
		
		
		return null;
	}	
		

	


	
	

	/**
		Gestiona los eventos de acción
		
		@param e evento de acción
	*/	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JCheckBox)
			ajustarChecks();
		
		String metodoError=comprobarChecks();
		if (metodoError!=null)
			new CuadroError(this.dialogo, Texto.get("ERROR_PARAM",Conf.idioma),Texto.get("ERROR_VISIBIGRAL",Conf.idioma)+" ("+metodoError+")");
		else if (e.getSource()==aceptar)
		{
			actuar();
		}
		else if (e.getSource()==cancelar)
		{
			dialogo.setVisible(false);
		}

		
	}
	
	
	
	private void actuar()
	{
		for (int i=0; i<botonesMetodos.length; i++)
		{
			dtb.getMetodo(i).setEsVisible( botonesMetodos[i].isSelected() );
			
			if (dtb.getMetodo(i).esMetodoConRetorno())
			{
				for (int j=0; j<botonesParametros[i].length-1; j++)
					dtb.getMetodo(i).setVisibilidadE( botonesParametros[i][j].isSelected(),j );
				dtb.getMetodo(i).setVisibilidadS( botonesParametros[i][botonesParametros[i].length-1].isSelected(),0 );
			}
			else
			{
				for (int j=0; j<botonesParametros[i].length/2; j++)
					dtb.getMetodo(i).setVisibilidadE( botonesParametros[i][j].isSelected(),j );
				for (int j=botonesParametros[i].length/2; j<botonesParametros[i].length; j++)
					dtb.getMetodo(i).setVisibilidadS( botonesParametros[i][j].isSelected(),j-botonesParametros[i].length/2 );
			}
		}
		
		//dtb.visualizarDatos();
		Conf.setRedibujarGrafoArbol(true);
		Ventana.thisventana.setDTB(dtb);
		Ventana.thisventana.actualizarEstadoTrazaCompleta();
		Ventana.thisventana.refrescarOpciones();
		Conf.setRedibujarGrafoArbol(false);
		
		this.dialogo.setVisible(false);
		// - poda de traza
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
		if (e.getKeyCode()==KeyEvent.VK_ESCAPE)
			dialogo.setVisible(false);

		if (e.getKeyCode()==KeyEvent.VK_ENTER)
		{
			if (e.getSource() instanceof JCheckBox)
				ajustarChecks();
			
			String metodoError=comprobarChecks();
			if (metodoError!=null)
				new CuadroError(this.dialogo, Texto.get("ERROR_PARAM",Conf.idioma),Texto.get("ERROR_VISIBIGRAL",Conf.idioma)+" ("+metodoError+")");

			actuar();
		}
		
		if (e.getSource() instanceof JCheckBox)
			ajustarChecks();
	}

	/**
		Gestiona los eventos de teclado
		
		@param e evento de teclado
	*/
	public void keyTyped(KeyEvent e)
	{

	}
	
	
}
