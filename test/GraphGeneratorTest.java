import org.jgrapht.Graph;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GraphGeneratorTest {

    private GraphGenerator graphGenerator;
    private BufferedReader bufferedReader;

    @BeforeEach
    public void setUp() throws IOException {
//        graphGenerator = new GraphGenerator("test/test_case.csv");
//        bufferedReader = graphGenerator.getBufferedReader();
//        bufferedReader.readLine();
    }

    @Test
    void existsAndPutTest() throws IOException {
//        Map<String, VertexContent> map = new HashMap<>();
//        String data[] = new String(bufferedReader.readLine()).split(";");
//        assertTrue(graphGenerator.existsAndPut(map, data, graphGenerator.getGraph(),0, 1, 2));
//        assertFalse(graphGenerator.existsAndPut(map, data, graphGenerator.getGraph(),0, 1, 2));
    }

    @Test
    void generateGraphTest() {
    }
}