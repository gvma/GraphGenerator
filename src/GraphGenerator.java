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
    private Graph<VertexContent, DefaultEdge> graph = new DirectedMultigraph<>(DefaultEdge.class);
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
//        Graph<VertexContent, DefaultEdge> graph = new DirectedMultigraph<>(DefaultEdge.class);
        Map<String, VertexContent> map = new HashMap<>();
        int edges = 0;
        try {
            String row;
            bufferedReader.readLine(); // Read first line of the CSV (not necessary)

            while ((row = bufferedReader.readLine()) != null) {
                ++edges;
                String data[] = row.split(";");
//                if (data[0].equals("-ACR- -889-") || data[5].equals("-ACR- -889-")) {
                data[0] = data[0].replace(' ', '$');
                data[5] = data[5].replace(' ', '$');
//                }


//                System.out.println(data[0] + "    " + data[5]);

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

    public ShortestPathAlgorithm.SingleSourcePaths<VertexContent, DefaultEdge> findPathsBetweenVertexes(VertexContent start, VertexContent end, List<String> drawingsList) {
        AllDirectedPaths<VertexContent, DefaultEdge> pathFinder = new AllDirectedPaths<>(graph);
        Set<VertexContent> sourceVertex = new HashSet<>();
        sourceVertex.add(start);
        Set<VertexContent> destinationVertex = new HashSet<>();
        destinationVertex.add(end);
        Set<VertexContent> landmarks = new HashSet<>();

        for (VertexContent v : graph.vertexSet()) {
            if (drawingsList.contains(v.getDrawing())) {
                landmarks.add(v);
            }
        }
        System.out.println(landmarks.size());
        AStarShortestPath<VertexContent, DefaultEdge> aStarShortestPath = new AStarShortestPath<>(graph, new ALTAdmissibleHeuristic<>(graph, landmarks));
        return aStarShortestPath.getPaths(start);
//        return pathFinder.getAllPaths(sourceVertex, destinationVertex, true, null);
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
