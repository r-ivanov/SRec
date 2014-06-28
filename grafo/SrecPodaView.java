package grafo;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

public class SrecPodaView extends VertexView {

	private static final long serialVersionUID = 2987784583123393224L;
	
	/** Renderer for the class. */
	public static transient PodaRenderer renderer;
	
	// Headless environment does not allow renderer
	static {
		try {
			renderer = new PodaRenderer();
		} catch (Error e) {
			// No renderer
		}
	}
		
	public SrecPodaView(Object cell) {
		super(cell);
	}
		
	/**
	 * Returns a renderer for the class.
	 */
	@Override
	public CellViewRenderer getRenderer() {
		return renderer;
	} 
}
