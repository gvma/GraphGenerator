import org.jgrapht.*;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.ALTAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.*;
import org.jgrapht.graph.specifics.DirectedEdgeContainer;
import org.jgrapht.traverse.*;

import java.io.*;
import java.util.*;

public class GraphGenerator {

    private String csvPath;
    private BufferedReader bufferedReader;
    private Graph<VertexContent, DefaultEdge> graph = new DirectedMultigraph<VertexContent, DefaultEdge>(DefaultEdge.class);
    int vertexCount = 0;

    public GraphGenerator(String csvPath) throws FileNotFoundException {
        this.csvPath = csvPath;
        bufferedReader = new BufferedReader(new FileReader(csvPath));
    }

    public boolean existsAndPut(Map<String, VertexContent> map, String[] data, Graph<VertexContent, DefaultEdge> graph, int ...indexes ) {
        if (map.get(data[indexes[1]]) == null) {
            VertexContent vertexContent = new VertexContent(data[indexes[0]], data[indexes[1]], data[indexes[2]], data[indexes[3]], new Long(vertexCount++));
            map.put(data[indexes[1]], vertexContent);
            this.graph.addVertex(vertexContent);
            return true;
        }
        return false;
    }

    public Graph<VertexContent, DefaultEdge> generateGraph() {
        Map<String, VertexContent> map = new HashMap<String, VertexContent>();
        int edges = 0;
        try {
            String row;
            bufferedReader.readLine(); // Read first line of the CSV (not necessary)

            while ((row = bufferedReader.readLine()) != null) {
                ++edges;
                String data[] = row.split(";");

                // Checking if String from "RepID" already exists for both edges
                existsAndPut(map, data, graph, 0, 1, 2, 4, graph.vertexSet().size());
                existsAndPut(map, data, graph, 5, 6, 2, 4, graph.vertexSet().size());

                // Checking the type of connection: B -> A, A -> B or A <-> B, respectively
                if (data[7].equals("From") || data[7].equals("In")) {
                    this.graph.addEdge(map.get(data[6]), map.get(data[1])); // Add Edge from B -> A
                } else if (data[7].equals("To") || data[7].equals("Out")) {
                    this.graph.addEdge(map.get(data[1]), map.get(data[6])); // Add Edge from A -> B
                } else {
                    this.graph.addEdge(map.get(data[6]), map.get(data[1])); // Add Edge from B -> A
                    this.graph.addEdge(map.get(data[1]), map.get(data[6])); // Add Edge from A -> B
                }
            }
        } catch (IOException e) {
            System.out.println("An exception has occurred while trying to read the file");
        };

        System.out.println("Done building the graph with " + vertexCount + " vertexes and " + edges + " edges!");
        return this.graph;
    }

    public List<GraphPath<VertexContent, DefaultEdge>> findPathsBetweenVertexes(VertexContent start, VertexContent end) {
        AllDirectedPaths<VertexContent, DefaultEdge> pathFinder = new AllDirectedPaths<VertexContent, DefaultEdge>(graph);
        return pathFinder.getAllPaths(start, end, true, 80);
    }

    public String getCsvPath() {
        return csvPath;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public Graph<VertexContent, DefaultEdge> getGraph() {
        return graph;
    }
}
