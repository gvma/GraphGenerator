import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CSVReader {

    private GraphGenerator graphGenerator;

    public void existsAndPut(Map<String, VertexContent> map, String[] data, int ...indexes) {
        if (map.get(data[indexes[1]]) == null) {
            VertexContent vertexContent = new VertexContent(data[indexes[0]], data[indexes[1]], data[indexes[2]]);
            map.put(data[indexes[1]], vertexContent);
            this.graphGenerator.getGraph().addVertex(vertexContent);
        }
    }

    public CSVReader(String csvPath, GraphGenerator graphGenerator) {
        this.graphGenerator = graphGenerator;
        Map<String, VertexContent> map = new HashMap<>();
        Graph<VertexContent, DefaultEdge> g = graphGenerator.getGraph();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(csvPath));
            VertexContent vertexContent;
            String row;
            row = bufferedReader.readLine(); // Read first line of the CSV
            while ((row = bufferedReader.readLine()) != null) {
                String data[] = row.split(";");

                // Checking if String from "RepID" already exists for both edges
                existsAndPut(map, data, 0, 1, 2);
                existsAndPut(map, data, 5, 6, 2);

                // Checking the type of connection: B -> A, A -> B or A <-> B, respectively
                if (data[7].equals("From") || data[7].equals("In")) {
                    graphGenerator.getGraph().addEdge(map.get(data[6]), map.get(data[1])); // Add Edge from B -> A
                } else if (data[7].equals("To") || data[7].equals("Out")) {
                    graphGenerator.getGraph().addEdge(map.get(data[1]), map.get(data[6])); // Add Edge from A -> B
                } else {
                    graphGenerator.getGraph().addEdge(map.get(data[6]), map.get(data[1])); // Add Edge from B -> A
                    graphGenerator.getGraph().addEdge(map.get(data[1]), map.get(data[6])); // Add Edge from A -> B
                }

            }
        } catch (IOException e) {
            System.out.println("An exception has occurred while trying to read the file");
        };
    }
}
