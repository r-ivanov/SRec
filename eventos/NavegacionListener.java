package eventos;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JViewport;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import paneles.PanelArbol;

/**
 * Permite gestionar los eventos de navegación sobre el Panel del Árbol.
 */
public class NavegacionListener implements MouseListener, MouseMotionListener,
MouseWheelListener, KeyListener, ComponentListener {

	private JViewport jvp; // JViewport del panel principal
	private double escala;

	private DefaultGraphCell cuadro;

	private int tipoMovimiento = 0; // 1 = se arrastró barra de desplazamiento
	// 2 = se desplazó celda de grafo (cuadro de
	// visor)
	// 3 = se hizo click en un punto del grafo
	// del visor para que el cuadro visor se
	// mueva ahí

	private Point punto = null;
	private PanelArbol pArbol;

	/**
	 * Construye una nueva instancia.
	 * 
	 * @param vp
	 *            ViewPort del panel que contiene el árbol.
	 * @param grafoVisor
	 *            Celdas del grafo del visor.
	 * @param panelArbol
	 *            Panel que contiene el árbol.
	 */
	public NavegacionListener(JViewport vp, Object[] grafoVisor,
			PanelArbol panelArbol) {
		this.setAtributos(vp, grafoVisor, 0.0);
		this.pArbol = panelArbol;
	}

	/**
	 * Permite configurar los elementos del listener.
	 * 
	 * @param vp
	 *            ViewPort del panel que contiene el árbol.
	 * @param grafoVisor
	 *            Celdas del grafo del visor.
	 * @param escala
	 *            Escala actual de visualización.
	 */
	public void setAtributos(JViewport vp, Object[] grafoVisor, double escala) {
		this.jvp = vp;
		this.escala = escala;

		if (grafoVisor != null && grafoVisor.length > 0) {
			this.cuadro = (DefaultGraphCell) grafoVisor[grafoVisor.length - 1];
		}
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param evento
	 *            evento de ratón
	 */
	@Override
	public void mouseDragged(MouseEvent evento) {
		if (evento.getSource().getClass().getName().contains("Scroll")) {
			this.tipoMovimiento = 1; // Hemos movido la barra de scroll
		} else if (evento.getSource().getClass().getName().contains("JGraph")) {
			this.tipoMovimiento = 2; // Hemos arrastrado el cuadro de visor
		}
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param evento
	 *            evento de ratón
	 */
	@Override
	public void mouseMoved(MouseEvent evento) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param evento
	 *            evento de ratón
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent evento) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param evento
	 *            evento de ratón
	 */
	@Override
	public void mouseClicked(MouseEvent evento) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param evento
	 *            evento de ratón
	 */
	@Override
	public void mouseEntered(MouseEvent evento) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param evento
	 *            evento de ratón
	 */
	@Override
	public void mouseExited(MouseEvent evento) {

	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param evento
	 *            evento de ratón
	 */
	@Override
	public void mousePressed(MouseEvent evento) {
		if (evento.getSource().getClass().getName().contains("Scroll")) {
			this.tipoMovimiento = 1; // Hemos pulsado sobre la barra de scroll
			// para que dé un salto
		} else if (evento.getSource().getClass().getName().contains("JGraph")) {
			this.tipoMovimiento = 3; // Hemos hecho click sobre un punto del
			// grafo para que el cuadro de visor se
			// mueva ahí
			this.punto = evento.getPoint();

		}
	}

	/**
	 * Gestiona los eventos de ratón
	 * 
	 * @param evento
	 *            evento de ratón
	 */
	@Override
	public void mouseReleased(MouseEvent evento) {
		this.ejecucion();
		this.tipoMovimiento = 0;
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param KeyEvent
	 *            evento de teclado
	 */
	@Override
	public void keyPressed(KeyEvent e) {

	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param KeyEvent
	 *            evento de teclado
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		Point punto = this.jvp.getViewPosition();

		Rectangle2D r2dcv = GraphConstants.getBounds(this.cuadro
				.getAttributes());
		Rectangle r2d = new Rectangle((int) (punto.getX() / this.escala),
				(int) (punto.getY() / this.escala), (int) r2dcv.getWidth(),
				(int) r2dcv.getHeight());

		GraphConstants.setBounds(this.cuadro.getAttributes(), r2d);
		this.pArbol.visualizar2(true);
	}

	/**
	 * Gestiona los eventos de teclado
	 * 
	 * @param KeyEvent
	 *            evento de teclado
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * Gestiona los eventos del componente
	 * 
	 * @param ComponentEvent
	 *            evento del componente
	 */
	@Override
	public void componentHidden(ComponentEvent e) {
	}

	/**
	 * Gestiona los eventos del componente
	 * 
	 * @param ComponentEvent
	 *            evento del componente
	 */
	@Override
	public void componentMoved(ComponentEvent e) {
	}

	/**
	 * Gestiona los eventos del componente
	 * 
	 * @param ComponentEvent
	 *            evento del componente
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		try {
			this.tipoMovimiento = 1;
			this.ejecucion();
			this.tipoMovimiento = 0;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Gestiona los eventos del componente
	 * 
	 * @param ComponentEvent
	 *            evento del componente
	 */
	@Override
	public void componentShown(ComponentEvent e) {

	}

	/**
	 * Ejecuta la acción correspondiente al evento producido.
	 * 
	 * @param n
	 *            1 = se arrastró barra de desplazamiento, 2 = se desplazó celda
	 *            de grafo (cuadro de visor), 3 = se hizo click en un punto del
	 *            grafo del visor para que el cuadro visor se mueva ahí.
	 */
	public void ejecucion(int n) {
		this.tipoMovimiento = n;
		this.ejecucion();
		this.tipoMovimiento = 0;
	}

	/**
	 * Realiza la acción correspondiente al tipo de movimiento producido.
	 */
	private void ejecucion() {
		if (this.tipoMovimiento == 1) {
			if (this.jvp != null && this.cuadro != null) {
				Point punto = this.jvp.getViewPosition();
				Rectangle2D r2dcv = GraphConstants.getBounds(this.cuadro
						.getAttributes());
				Rectangle r2d = new Rectangle(
						(int) (punto.getX() / this.escala),
						(int) (punto.getY() / this.escala),
						(int) r2dcv.getWidth(), (int) r2dcv.getHeight());

				GraphConstants.setBounds(this.cuadro.getAttributes(), r2d);
				this.pArbol.visualizar2(true);
			}

		} else if (this.tipoMovimiento == 2) {
			Rectangle2D r2d = GraphConstants.getBounds(this.cuadro
					.getAttributes());

			// Revisamos si el cuadro se ha salido del panel, y en ese caso lo
			// reubicamos
			if ((r2d.getMinX() + r2d.getWidth()) > PanelArbol.anchoGrafo()
					|| (r2d.getMinY() + r2d.getHeight()) > PanelArbol
					.altoGrafo()) {
				double valorX, valorY;

				if ((r2d.getMinX() + r2d.getWidth()) > PanelArbol.anchoGrafo()) {
					valorX = PanelArbol.anchoGrafo() - r2d.getWidth();
				} else {
					valorX = r2d.getMinX();
				}

				if ((r2d.getMinY() + r2d.getHeight()) > PanelArbol.altoGrafo()) {
					valorY = PanelArbol.altoGrafo() - r2d.getHeight();
				} else {
					valorY = r2d.getMinY();
				}

				if (valorX < 0) {
					valorX = 0;
				}

				if (valorY < 0) {
					valorY = 0;
				}

				Point p2d = new Point((int) valorX, (int) valorY);
				Dimension dim = new Dimension((int) r2d.getWidth(),
						(int) r2d.getHeight());
				r2d.setFrame(p2d, dim);

				GraphConstants.setBounds(this.cuadro.getAttributes(), r2d);

				this.pArbol.visualizar(false, false, true);
			}

			// Recolocamos el JViewport
			Point p = new Point((int) (r2d.getMinX() * this.escala),
					(int) (r2d.getMinY() * this.escala));
			this.jvp.setViewPosition(p);

		} else if (this.tipoMovimiento == 3) {

			Rectangle2D r2d = GraphConstants.getBounds(this.cuadro
					.getAttributes());
			double anchoCuadro = r2d.getWidth();
			double altoCuadro = r2d.getHeight();
			double posX = (this.punto.getX() / PanelArbol.escala)
					- (anchoCuadro / 2);
			double posY = (this.punto.getY() / PanelArbol.escala)
					- (altoCuadro / 2);

			if (posX < 0) {
				posX = 0;
			}
			if (posY < 0) {
				posY = 0;
			}

			// Revisamos si el cuadro se ha salido del panel, y en ese caso lo
			// reubicamos
			if ((posX + r2d.getWidth()) > PanelArbol.anchoGrafo()
					|| (posY + r2d.getHeight()) > PanelArbol.altoGrafo()) {
				double valorX, valorY;

				if ((posX + r2d.getWidth()) > PanelArbol.anchoGrafo()) {
					valorX = PanelArbol.anchoGrafo() - r2d.getWidth();
				} else {
					valorX = posX;
				}

				if ((posY + r2d.getHeight()) > PanelArbol.altoGrafo()) {
					valorY = PanelArbol.altoGrafo() - r2d.getHeight();
				} else {
					valorY = posY;
				}

				if (valorX < 0) {
					valorX = 0;
				}

				if (valorY < 0) {
					valorY = 0;
				}

				Point p2d = new Point((int) valorX, (int) valorY);
				Dimension dim = new Dimension((int) r2d.getWidth(),
						(int) r2d.getHeight());
				Rectangle rd = new Rectangle(p2d, dim);

				GraphConstants.setBounds(this.cuadro.getAttributes(), rd);
				this.pArbol.visualizar(false, false, true);

				// Recolocamos el JViewport
				Point p = new Point((int) (rd.getMinX() * this.escala),
						(int) (rd.getMinY() * this.escala));
				this.jvp.setViewPosition(p);
			} else {
				Point p2d = new Point((int) posX, (int) posY);
				Dimension dim = new Dimension((int) r2d.getWidth(),
						(int) r2d.getHeight());
				Rectangle rd = new Rectangle(p2d, dim);
				GraphConstants.setBounds(this.cuadro.getAttributes(), rd);
				this.pArbol.visualizar(false, false, true);

				// Recolocamos el JViewport
				Point p = new Point((int) (rd.getMinX() * this.escala),
						(int) (rd.getMinY() * this.escala));
				this.jvp.setViewPosition(p);
			}
			this.punto = null;
		}
	}

}