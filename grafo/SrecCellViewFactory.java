package grafo;

import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.GraphModel;

public class SrecCellViewFactory extends DefaultCellViewFactory {

	private static final long serialVersionUID = -5338018095276014170L;
	
	/**
	 * Constructs a view for the specified cell and associates it with the
	 * specified object using the specified CellMapper. This calls refresh on
	 * the created CellView to create all dependent views.
	 * <p>
	 * Note: The mapping needs to be available before the views of child cells
	 * and ports are created.
	 * <b>Note: This method must return new instances!</b>
	 * 
	 * @param cell
	 *            reference to the object in the model
	 */
	@Override
	public CellView createView(GraphModel model, Object cell) {
		CellView view = null;
		if (cell instanceof SrecPodaGraphCell) {
			view = this.createPodaView(cell);
		} else {
			view = super.createView(model, cell);
		}
		return view;
	}
	
	/**
	 * Constructs a PortView view for the specified object.
	 */
	protected SrecPodaView createPodaView(Object cell) {
		return new SrecPodaView(cell);
	}
}
