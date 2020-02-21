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
                from[i] = from[i].replace(' ', '$');
                to[i] = to[i].replace(' ', '$');
                System.out.println(from[i] + " and " + to[i]);
            }
            List<String> drawingsList = new LinkedList<String>();
            for (int i = 5; i < args.length; ++i) {
                drawingsList.add(args[i].replace(' ', '$')); // Processing all inputs
            }

            System.out.println("Finding the initial vertex!");
            VertexContent start = graph.vertexSet().stream().filter(vertexContent -> (vertexContent.getTagPipe().equals(from[0]) && vertexContent.getDrawing().equals(from[1]))).findAny().get();
            System.out.println("Done!\n\n");

            System.out.println("Finding the destination vertex!");
            VertexContent end = graph.vertexSet().stream().filter(vertexContent -> (vertexContent.getTagPipe().equals(to[0]) && vertexContent.getDrawing().equals(to[1]))).findAny().get();
            System.out.println("Done!\n\n");
//
            System.out.println("Finding all paths possible!");
            Graph<VertexContent, DefaultEdge> subGraph = graphGenerator.findPathsBetweenVertexes(start, end, drawingsList).getGraph();
            System.out.println("Number of Paths: " + subGraph.vertexSet().size() + "\nDone!\n\n");
            Iterator<VertexContent> it = new DepthFirstIterator<>(subGraph, start);
            while (it.hasNext()) {
                System.out.println(it.next().toString());
            }
//            int count = 0;
//            System.out.println("Printing all Paths: ");
//            for (GraphPath<VertexContent, DefaultEdge> path : paths.getPath()) {
//                System.out.println("Path of number: " + (++count));
//                for (VertexContent vertex : path.getVertexList()) {
//                    System.out.println(vertex.toString());
//                }
//            }
        } catch (NoSuchElementException e) {
            System.out.println("Looks like there isn't any vertex with those identifiers.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }

    }
}
