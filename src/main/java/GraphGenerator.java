package main.java;

import com.google.common.graph.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
//import org.jgrapht.*;
//import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
//import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
//import org.jgrapht.alg.shortestpath.ALTAdmissibleHeuristic;
//import org.jgrapht.alg.shortestpath.AStarShortestPath;
//import org.jgrapht.alg.shortestpath.AllDirectedPaths;
//import org.jgrapht.graph.*;
//import org.jgrapht.graph.specifics.DirectedEdgeContainer;
//import org.jgrapht.traverse.*;
//import main.java.GraphGenerator;

import javax.xml.soap.Node;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class GraphGenerator {

    private String csvPath;
    private BufferedReader bufferedReader;
    private MutableGraph<NodeContent> graph = GraphBuilder.directed().build();
    int vertexCount = 0;

    public GraphGenerator(String csvPath) {
        try {
            this.csvPath = csvPath;
            bufferedReader = new BufferedReader(new FileReader(csvPath));
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    public void existsAndPut(Map<String, NodeContent> map, String[] data, MutableGraph<NodeContent> graph, int ...nodeIndexes ) {

        if (map.get(data[nodeIndexes[1]]) == null) {
            NodeContent nodeContent = new NodeContent(data[nodeIndexes[0]], data[nodeIndexes[1]], data[nodeIndexes[2]], data[nodeIndexes[3]], (long) vertexCount++);
            map.put(data[nodeIndexes[1]], nodeContent);
            this.graph.addNode(nodeContent);
        }
    }

    public boolean generateGraph() {
        Map<String, NodeContent> map = new HashMap<String, NodeContent>();
        int edges = 0;
        try {
            String row;
            bufferedReader.readLine(); // Read first line of the CSV (not necessary)

            while ((row = bufferedReader.readLine()) != null) {
                ++edges;
                String[] data = row.split(";");

                // Checking if String from "RepID" already exists for both nodes
                existsAndPut(map, data, graph, 0, 1, 2, 4);
                existsAndPut(map, data, graph, 5, 6, 2, 4);

                // Checking the type of connection: B -> A, A -> B or A <-> B, respectively
                if (data[7].equals("From") || data[7].equals("In")) {
                    this.graph.putEdge(map.get(data[6]), map.get(data[1])); // Add Edge from B -> A
                } else if (data[7].equals("To") || data[7].equals("Out")) {
                    this.graph.putEdge(map.get(data[1]), map.get(data[6])); // Add Edge from A -> B
                } else {
                    this.graph.putEdge(map.get(data[6]), map.get(data[1])); // Add Edge from B -> A
                    this.graph.putEdge(map.get(data[1]), map.get(data[6])); // Add Edge from A -> B
                }
            }
        } catch (IOException e) {
            System.out.println("An exception has occurred while trying to read the file");
            return false;
        };

        System.out.println("Done building the graph with " + vertexCount + " vertexes and " + edges + " edges!");
        return true;
    }

    public void printGraphNodes(Graph<NodeContent> g) {
        for (NodeContent u : g.nodes()) {
            System.out.println("Node " + u.getRepID() + " is connected to:");
            for (NodeContent v : g.adjacentNodes(u)) {
                System.out.println(v);
            }
        }
    }

    public NodeContent findNode(String tagPipe, String drawing) {
        return graph.nodes().stream().filter((tp) -> (tp.getTagPipe().equals(tagPipe) && tp.getDrawing().equals(drawing))).findAny().get();
    }

    public void printPath(NodeContent source, NodeContent destination, NodeContent path[]) {
        System.out.println("================");
        System.out.println("Printing Path!");
        System.out.println("================");

        System.out.println(destination);
        int nodeIndex = destination.getVertexID().intValue();
        System.out.println(path[destination.getVertexID().intValue()]);
        while (path[nodeIndex].getVertexID().intValue() != source.getVertexID().intValue()) {
            System.out.println(path[nodeIndex]);
            nodeIndex = path[nodeIndex].getVertexID().intValue();
        }
        System.out.println(source);
    }

    public boolean checkPath(NodeContent[] path, List<String> drawings) {
        if (drawings.size() == 0) {
            return true;
        }

        int drawingsChecked = 0;

        boolean drawingsCheck[] = new boolean[drawings.size()];
        for (NodeContent node : path) {
            for (int i = 0; i < drawings.size(); ++i) {
                if (node != null) {
                    if (node.getDrawing().equals(drawings.get(i)) && drawingsCheck[i]) {
                        drawingsChecked++;
                        drawingsCheck[i] = true;
                    }
                    if (drawingsChecked == drawings.size()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public NodeContent getNode(String tagPipe, String drawing, String message) {
        NodeContent node;
        if ((node = findNode(tagPipe, drawing)) == null) {
            System.out.println(message);
            System.exit(0);
        }
        return node;
    }

    public void DFS(MutableGraph<NodeContent> subgraph, NodeContent source, NodeContent destination, boolean[] visited) {
        int sourceIndex = source.getVertexID().intValue(), destinationIndex = destination.getVertexID().intValue();
        System.out.println(source);
        for (NodeContent node : subgraph.adjacentNodes(source)) {
//            System.out.println(visited[node.getVertexID().intValue()]);
            if (!visited[node.getVertexID().intValue()]) {
                visited[sourceIndex] = true;
                DFS(subgraph, node, destination, visited);
            }
        }
        visited[sourceIndex] = false;
    }

    public boolean getAllPaths(MutableGraph<NodeContent> subgraph, NodeContent source, NodeContent target, int[] visited) {
        int sourceIndex = source.getVertexID().intValue(), targetIndex = target.getVertexID().intValue();
        if (sourceIndex == targetIndex) {
            return true;
        }

        if (visited[sourceIndex] != 0) {
            return visited[sourceIndex] == 2;
        }

        visited[sourceIndex] = 1;

        for (NodeContent node : graph.adjacentNodes(source)) {
            int nodeIndex = node.getVertexID().intValue();
            if (getAllPaths(subgraph, node, target, visited)) {

                subgraph.addNode(source);
                subgraph.addNode(node);

                subgraph.putEdge(source, node);
                visited[sourceIndex] = 2;
            }
        }

        return visited[sourceIndex] == 2;
    }

    public NodeContent[] BFS(String sourceTagPipe, String sourceDrawing, String destinationTagPipe, String destinationDrawing, List<String> drawingsList) {
        NodeContent sourceNode, destinationNode;
        boolean visited[] = new boolean[graph.nodes().size()];
        NodeContent path[] = new NodeContent[graph.nodes().size()];
        sourceNode = getNode(sourceTagPipe, sourceDrawing, "Source node not found! Exiting the program...");
        destinationNode = getNode(destinationTagPipe, destinationDrawing, "Destination node not found! Exiting the program...");

        System.out.println("Source node: " + sourceNode);
        System.out.println("Destination node: " + destinationNode);

        Queue<NodeContent> queue = new LinkedList<>();
        queue.add(sourceNode);
        int nodeIndex = sourceNode.getVertexID().intValue();
        visited[nodeIndex] = true;

        while (!queue.isEmpty()) {
            NodeContent u = queue.remove();
            for (NodeContent v : graph.adjacentNodes(u)) {
                nodeIndex = v.getVertexID().intValue();

                if (nodeIndex == destinationNode.getVertexID().intValue()) {
                    System.out.println("Node Index == " + nodeIndex);
                    path[nodeIndex] = u;
                    if (checkPath(path, drawingsList)) {
                        printPath(sourceNode, destinationNode, path);
                        return path;
                    }
                }

                if (!visited[nodeIndex]) {
                    path[nodeIndex] = u;
                    visited[nodeIndex] = true;
                    queue.add(v);
                }
            }
        }

        if (visited[destinationNode.getVertexID().intValue()]) {
//            printPath(sourceNode, destinationNode, path);
            return path;
        }

        return null;
    }

    public String getCsvPath() {
        return csvPath;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public Graph<NodeContent> getGraph() {
        return graph;
    }
}
