import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.graph.specifics.DirectedEdgeContainer;
import org.jgrapht.traverse.*;

public class GraphGenerator {

    private Graph<VertexContent, DefaultEdge> graph = new DirectedMultigraph<>(DefaultEdge.class);

    public Graph<VertexContent, DefaultEdge> getGraph() {
        return graph;
    }
}
