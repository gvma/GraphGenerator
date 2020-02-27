import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.ListenableGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import sun.security.provider.certpath.Vertex;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


public class GraphGeneratorMain {

    public static void main(String[] args){

        if (args[0].equals("-h") || args.length < 5) {
            System.out.println("Wrong input format, try again with: <path> <tag_pipe_1> <tag_pipe_1_drawing> <tag_pipe_2> <tag_pipe_2_drawing> <list_of_drawings_to_contain_path>");
            return;
        }
        try {
            GraphGenerator graphGenerator = new GraphGenerator(args[0]);
            Graph<VertexContent, DefaultEdge> graph = graphGenerator.generateGraph();

            // Proccessing input
            String[] from = {args[1], args[2]};
            String[] to = {args[3], args[4]};
            System.out.println("====================\n");

            for (int i = 0; i < 2; ++i) {
                System.out.println(from[i] + " and " + to[i]);
            }

            List<String> drawingsList = new LinkedList<String>();
            for (int i = 5; i < args.length; ++i) {
                drawingsList.add(args[i]); // Processing all drawings
            }

            System.out.println("Finding the initial vertex!");
            VertexContent start = graph.vertexSet().stream().filter(vertexContent -> (vertexContent.getTagPipe().equals(from[0]) && vertexContent.getDrawing().equals(from[1]))).findAny().get();
            System.out.println("Done!\n\n");

            System.out.println("Finding the destination vertex!");
            VertexContent end = graph.vertexSet().stream().filter(vertexContent -> (vertexContent.getTagPipe().equals(to[0]) && vertexContent.getDrawing().equals(to[1]))).findAny().get();
            System.out.println("Done!\n\n");

            System.out.println("Start Vertex:");
            System.out.println(start.toString());
            System.out.println("End Vertex:");
            System.out.println(end.toString());

            System.out.println("Finding all paths possible!");
            List<GraphPath<VertexContent, DefaultEdge>> paths = graphGenerator.findPathsBetweenVertexes(start, end);
            int count = 0;
            System.out.println(paths.size());
            for (GraphPath<VertexContent, DefaultEdge> path : paths) {
                System.out.println("Path of number: " + (++count));
                for (VertexContent vertex : path.getVertexList()) {
                    System.out.println(vertex);
                }
            }

        } catch (NoSuchElementException e) {
            System.out.println("Looks like there isn't any vertex with those identifiers.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }

    }
}
