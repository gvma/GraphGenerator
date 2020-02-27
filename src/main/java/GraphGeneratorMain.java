package main.java;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;


public class GraphGeneratorMain {

    public static void main(String[] args){
        com.google.common.graph.Graph<NodeContent> g;
        if (args[0].equals("-h") || args.length < 5) {
            System.out.println("Wrong input format, try again with: <path> <tag_pipe_1> <tag_pipe_1_drawing> <tag_pipe_2> <tag_pipe_2_drawing> <list_of_drawings_to_contain_path>");
            return;
        }

        GraphGenerator graphGenerator = new GraphGenerator(args[0]);
        boolean flag = graphGenerator.generateGraph();
        if (!flag) {
            System.out.println();
        }

        // Proccessing input
        String[] from = {args[1], args[2]};
        String[] to = {args[3], args[4]};
        List<String> drawingsList = new LinkedList<String>();
        for (int i = 5; i < args.length; ++i) {
            drawingsList.add(args[i]); // Processing all drawings
        }

        // Searching paths
        MutableGraph<NodeContent> subgraph = GraphBuilder.directed().build();
        NodeContent u = graphGenerator.findNode(from[0], from[1]), v = graphGenerator.findNode(to[0], to[1]);
        graphGenerator.getAllPaths(subgraph, u, v, new int[graphGenerator.getGraph().nodes().size()]);
//        subgraph.addNode(u);
//        subgraph.addNode(v);
//        graphGenerator.DFS(subgraph, v, v, new boolean[graphGenerator.getGraph().nodes().size()]);
        System.out.println(subgraph.nodes().size());
//        graphGenerator.printGraphNodes(subgraph);
//        NodeContent path[] = graphGenerator.BFS(from[0], from[1], to[0], to[1], drawingsList);
//        if (path == null) {
//            System.out.println("No path found between nodes!");
//        } else {
//
//        }


    }
}
