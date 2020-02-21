import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import sun.security.provider.certpath.Vertex;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class GraphGeneratorMain {


    public static void main(String[] args) {

        GraphGenerator graphGenerator = new GraphGenerator();
        System.out.println(args[0]);
        CSVReader csvReader = new CSVReader(args[0], graphGenerator);
        Graph<VertexContent, DefaultEdge> graph = graphGenerator.getGraph();
        VertexContent start = graph.vertexSet().stream().filter(vertexContent -> vertexContent.getRepID().equals("358DD09510C641838EED00518D21612A")).findAny().get();
        VertexContent end = graph.vertexSet().stream().filter(vertexContent -> vertexContent.getRepID().equals("BC526B67ED2A4A468357F6DA393D51EA")).findAny().get();
        AllDirectedPaths<VertexContent, DefaultEdge> pathFinder = new AllDirectedPaths<>(graph);
        Set<VertexContent> sourceVertex = new HashSet<>();
        sourceVertex.add(start);
        Set<VertexContent> destinationVertex = new HashSet<>();
        destinationVertex.add(end);
        List<GraphPath<VertexContent, DefaultEdge>> paths = pathFinder.getAllPaths(sourceVertex, destinationVertex, true, null);
        int count = 0;
        System.out.println(paths.size());
        for (GraphPath<VertexContent, DefaultEdge> path : paths) {
            System.out.println("Path of number: " + (++count));
            for (VertexContent vertex : path.getVertexList()) {
                System.out.println(vertex.toString());
            }
        }
    }
}
