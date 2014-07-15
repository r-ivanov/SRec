package paneles;

import javax.swing.JPanel;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import datos.Traza;

public class PanelGrafoDependencia extends JPanel {

	private static final long serialVersionUID = 6889947417508543279L;
	
	private Traza traza;
	private JGraph graph;
	
	public PanelGrafoDependencia(Traza traza) {
		this.traza = traza;
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
		this.graph = new JGraph(model, view);	
		this.graph.getModel().addGraphModelListener(null);
	}
}
