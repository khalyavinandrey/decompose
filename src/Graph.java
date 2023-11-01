import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private Map<Integer, List<Integer>> graph;

    public Graph(Map<Integer, List<Integer>> graph) {
        this.graph = graph;
    }

    public Map<Integer, List<Integer>> getGraph() {
        return graph;
    }
}
