package eventos;


import javax.swing.JViewport;


import java.awt.Point;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import org.jgraph.graph.*;


import paneles.*;

public class NavegacionListener implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, ComponentListener
{
	//private JScrollPane jspP;			// Panel principal
	//private JScrollPane jspV;			// Panel visor
	private JViewport jvp;				// JViewport del panel principal
	//private Object[] gP;				// Grafo principal
	//private Object[] gV;				// Grafo del visor
	private double escala;
	
	private DefaultGraphCell cuadro;	// 
	
	private int tipoMovimiento=0;		// 1 = se arrastró barra de desplazamiento
										// 2 = se desplazó celda de grafo (cuadro de visor)
										// 3 = se hizo click en un punto del grafo del visor para que el cuadro visor se mueva ahí
										
										
	Point punto=null;

	
	
	private PanelArbol pArbol;
	
	
	public NavegacionListener(PanelArbol panelArbol)
	{
		this.setAtributos(panelArbol.getViewport(),panelArbol.getCeldas(),0.0);
		this.pArbol=panelArbol;
	}
	
	public NavegacionListener(JViewport vp, Object[] grafoVisor, PanelArbol panelArbol)
	{
		this.setAtributos(vp,grafoVisor,0.0);
		this.pArbol=panelArbol;
	}
	
	public void setAtributos(JViewport vp, Object[] grafoVisor,double escala)
	{
		this.jvp=vp;
		this.escala=escala;
		
		if (grafoVisor!=null && grafoVisor.length>0)
			this.cuadro=(DefaultGraphCell)grafoVisor[grafoVisor.length-1];
		
	}
	
	public void mouseDragged(MouseEvent evento)
	{
		//System.out.println("NavegacionListener mouseDragged:"+evento.getSource().getClass().getName());

		if ( evento.getSource().getClass().getName().contains("Scroll") )
			tipoMovimiento=1;	// Hemos movido la barra de scroll

		else if ( evento.getSource().getClass().getName().contains("JGraph") )
			tipoMovimiento=2;	// Hemos arrastrado el cuadro de visor

		
	}
	
	public void mouseMoved(MouseEvent evento)
	{
		//System.out.println("NavegacionListener mouseMoved:"+evento.getSource().getClass().getName());
	}
	
	public void mouseWheelMoved(MouseWheelEvent evento)
	{
		//System.out.println("NavegacionListener mouseWheelMoved:"+evento.getSource().getClass().getName());
	}
	
	public void mouseClicked(MouseEvent evento)
	{
		//System.out.println("NavegacionListener mouseClicked:"+evento.getSource().getClass().getName());
	}
	
	public void mouseEntered(MouseEvent evento)
	{
		//System.out.println("NavegacionListener mouseEntered:"+evento.getSource().getClass().getName());
		
	}

	public void mouseExited(MouseEvent evento)
	{
		//System.out.println("NavegacionListener mouseExited:"+evento.getSource().getClass().getName());
	}

	public void mousePressed(MouseEvent evento)
	{
		//System.out.println("NavegacionListener mousePressed:"+evento.getSource().getClass().getName());
		if ( evento.getSource().getClass().getName().contains("Scroll") )
			tipoMovimiento=1;	// Hemos pulsado sobre la barra de scroll para que dé un salto

		else if ( evento.getSource().getClass().getName().contains("JGraph") )
		{
			tipoMovimiento=3;	// Hemos hecho click sobre un punto del grafo para que el cuadro de visor se mueva ahí
			this.punto=evento.getPoint();
			
		}
	}

	public void mouseReleased(MouseEvent evento)
	{
		//System.out.println("NavegacionListener mouseReleased:"+evento.getSource().getClass().getName());
		
		ejecucion();
		
		tipoMovimiento=0;
	}
	
	
	public void keyPressed(KeyEvent e) 
	{
		
	}
	
	public void keyReleased(KeyEvent e)
	{
		//System.out.println("keyReleased . pulsando tecla sobre el Componente");
		//System.out.println("  Detectado pulsacion de tecla en grafo para mover barra de desplazamiento");
			
		Point punto=jvp.getViewPosition();
		//Dimension d=jvp.getExtentSize();
		
		Rectangle2D r2dcv=GraphConstants.getBounds(this.cuadro.getAttributes());
		Rectangle r2d=new Rectangle((int)(punto.getX()/this.escala),(int)(punto.getY()/this.escala),
				(int)r2dcv.getWidth(),(int)r2dcv.getHeight());
		
		GraphConstants.setBounds(this.cuadro.getAttributes(),r2d);
		pArbol.visualizar2(true);
	}
	
	public void keyTyped(KeyEvent e) 
	{
		
	}
	
	
	
	

	
	
	public void componentHidden(ComponentEvent e)
	{
	}
	
	public void componentMoved(ComponentEvent e)
	{
	}
	
	public void componentResized(ComponentEvent e)
	{
		//if (this.panelArbol!=null)
		//	this.panelArbol.visualizar(true,false);
		try {
			tipoMovimiento=1;
			ejecucion();
		
			tipoMovimiento=0;
		} catch (Exception ex) {
			//System.out.println("Excepcion en componentResized");
			//ex.printStackTrace();
		}
		
		
	}
	
	public void componentShown(ComponentEvent e)
	{
	}
	
	
	public void ejecucion(int n)
	{
		tipoMovimiento=n;
		
		ejecucion();
		tipoMovimiento=0;
	}
	
	private void ejecucion()
	{
		if (tipoMovimiento==1)
		{
			if (jvp!=null && this.cuadro!=null)
			{
				//System.out.println("  Detectado movimiento de barra de desplazamiento");
				Point punto=jvp.getViewPosition();
				//Dimension d=jvp.getExtentSize();
				
				Rectangle2D r2dcv=GraphConstants.getBounds(this.cuadro.getAttributes());
				Rectangle r2d=new Rectangle((int)(punto.getX()/this.escala),(int)(punto.getY()/this.escala),
						(int)r2dcv.getWidth(),(int)r2dcv.getHeight());
				
				GraphConstants.setBounds(this.cuadro.getAttributes(),r2d);
				pArbol.visualizar2(true);
			}
			
		}
		else if (tipoMovimiento==2)
		{
			//System.out.println("  Detectado movimiento de cuadro de visor");
			Rectangle2D r2d=GraphConstants.getBounds(this.cuadro.getAttributes());
			
			// Revisamos si el cuadro se ha salido del panel, y en ese caso lo reubicamos
			if ( (r2d.getMinX()+r2d.getWidth())>PanelArbol.anchoGrafo() ||
				(r2d.getMinY()+r2d.getHeight())>PanelArbol.altoGrafo() )
			{
				double valorX,valorY;
			
				if ( (r2d.getMinX()+r2d.getWidth())>PanelArbol.anchoGrafo())
					valorX=PanelArbol.anchoGrafo()-r2d.getWidth();
				else
					valorX=r2d.getMinX();
					
				if ( (r2d.getMinY()+r2d.getHeight())>PanelArbol.altoGrafo())
					valorY=PanelArbol.altoGrafo()-r2d.getHeight();
				else
					valorY=r2d.getMinY();
					
				if (valorX<0)
					valorX=0;	
					
				if (valorY<0)
					valorY=0;

				Point p2d=new Point( (int)valorX , (int)valorY );
				Dimension dim=new Dimension( (int)r2d.getWidth() , (int)r2d.getHeight() );
				r2d.setFrame(p2d,dim); 
					
				GraphConstants.setBounds(this.cuadro.getAttributes(),r2d);

				pArbol.visualizar(false,false,true);
			}
			

			
			// Recolocamos el JViewport
			Point p=new Point((int)(r2d.getMinX()*this.escala),(int)(r2d.getMinY()*this.escala));
			jvp.setViewPosition(p);
			//jvp.reshape((int)(r2d.getMinX()*this.escala),(int)(r2d.getMinY()*this.escala),jspP.getWidth(),jspP.getHeight());

		}
		else if (tipoMovimiento==3)
		{
			//System.out.println("  Detectado click de ratón: "+(punto.getX()/PanelArbol.ESCALAVISOR)+
			//								"x"+(punto.getY()/PanelArbol.ESCALAVISOR));
			
			Rectangle2D r2d=GraphConstants.getBounds(this.cuadro.getAttributes());
			double anchoCuadro=r2d.getWidth();
			double altoCuadro=r2d.getHeight();
			double posX=(punto.getX()/PanelArbol.escala)-(anchoCuadro/2);
			double posY=(punto.getY()/PanelArbol.escala)-(altoCuadro/2);
			
			if (posX<0)
				posX=0;
			if (posY<0)
				posY=0;
				
			
			// Revisamos si el cuadro se ha salido del panel, y en ese caso lo reubicamos
			if ( (posX+r2d.getWidth())>PanelArbol.anchoGrafo() ||
				(posY+r2d.getHeight())>PanelArbol.altoGrafo() )
			{
				double valorX,valorY;
			
				if ( (posX+r2d.getWidth())>PanelArbol.anchoGrafo())
					valorX=PanelArbol.anchoGrafo()-r2d.getWidth();
				else
					valorX=posX;
					
				if ( (posY+r2d.getHeight())>PanelArbol.altoGrafo())
					valorY=PanelArbol.altoGrafo()-r2d.getHeight();
				else
					valorY=posY;
					
				if (valorX<0)
					valorX=0;	
					
				if (valorY<0)
					valorY=0;

				Point p2d=new Point( (int)valorX , (int)valorY );
				Dimension dim=new Dimension( (int)r2d.getWidth() , (int)r2d.getHeight() );
				Rectangle rd=new Rectangle(p2d,dim); 
				
				//System.out.println("    valorX - valorY :  "+valorX+" x "+valorY);
					
				GraphConstants.setBounds(this.cuadro.getAttributes(),rd);
				pArbol.visualizar(false,false,true);
				
				// Recolocamos el JViewport
				Point p=new Point((int)(rd.getMinX()*this.escala),(int)(rd.getMinY()*this.escala));
				jvp.setViewPosition(p);	
			}
			else
			{
				Point p2d=new Point( (int)posX , (int)posY );
				Dimension dim=new Dimension( (int)r2d.getWidth() , (int)r2d.getHeight() );
				Rectangle rd=new Rectangle(p2d,dim); 
				GraphConstants.setBounds(this.cuadro.getAttributes(),rd);
				pArbol.visualizar(false,false,true);
				
				// Recolocamos el JViewport
				Point p=new Point((int)(rd.getMinX()*this.escala),(int)(rd.getMinY()*this.escala));
				jvp.setViewPosition(p);	
			}
			punto=null;
		}
		else
		{
			//System.out.println("  Detectado movimiento no registrado");
		}
	}
	

}