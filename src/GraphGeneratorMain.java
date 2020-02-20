import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;


public class GraphGeneratorMain {


    public static void main(String[] args) {

        GraphGenerator graphGenerator = new GraphGenerator();
        System.out.println(args[0]);
        CSVReader csvReader = new CSVReader(args[0], graphGenerator);
        Graph<VertexContent, DefaultEdge> graph = graphGenerator.getGraph();
        VertexContent start = graph.vertexSet().stream().filter(vertexContent -> vertexContent.getRepID().equals("2F5CA5E0BA554BEE94CDCF1735F6468B")).findAny().get();
        Iterator<VertexContent> it = new DepthFirstIterator<>(graph, start);
        while (it.hasNext()) {
            VertexContent v = it.next();
            System.out.println(v.toString());
        }

    }
}
