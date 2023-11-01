import java.util.*;

public class GraphAlgorithm {
    public void topologicalDecomposition(Graph graph) {

        int size = graph.getGraph().keySet().size();

        Map<Integer, List<Integer>> connected = new LinkedHashMap<>(size);
        Map<Integer, Integer> united = new HashMap<>();
        Map<Integer, List<Integer>> decomposed = new HashMap<>();
        List<Integer> notUsed = new LinkedList<>(graph.getGraph().keySet());

        List<Edge> edges = transform(graph);

        while (notUsed.size() > 0) {
            List<Integer> R = new ArrayList<>(List.of(notUsed.get(0)));
            List<Integer> Q = new ArrayList<>(List.of(notUsed.get(0)));

            for (int i = 1; i < notUsed.size(); i++) {
                if (dfs(notUsed.get(0), notUsed.get(i), edges, new boolean[size + 1])) {
                    R.add(notUsed.get(i));
                }
                if (dfs(notUsed.get(i), notUsed.get(0), edges, new boolean[size + 1])) {
                    Q.add(notUsed.get(i));
                }
            }

            List<Integer> V = findIntersection(R, Q);

            printResults(notUsed, R, Q, V);

            connected.put(notUsed.get(0), V);

            notUsed.removeAll(V);
            edges.removeIf(edge -> V.contains(edge.from) || V.contains(edge.to));
        }

        connected.keySet().forEach(key -> {
            connected.get(key).forEach(val -> {
                united.put(val, key);
            });
        });


        connected.keySet().forEach(key -> {
            connected.get(key).forEach(val -> {
                graph.getGraph().get(val).forEach(v -> {
                    int key1 = united.get(val);
                    int key2 = united.get(v);

                    if (key1 == key2) return;

                    if (decomposed.containsKey(key1)) {
                        if (decomposed.get(key1).contains(key2)) return;
                    }

                    if (decomposed.containsKey(key1)) {
                        decomposed.get(key1).add(key2);
                    } else {
                        decomposed.put(key1, new ArrayList<>(List.of(key2)));
                    }
                });
            });
        });

        decomposed.forEach((k, v) -> {
            System.out.println(k + " -> " + v);
        });
    }

    public boolean dfs(int from, int to, List<Edge> edges, boolean[] visited) {
        visited[from] = true;

        if (from == to) {
            return true;
        }

        for (int w = 0; w < edges.size(); w++) {
            if (!visited[edges.get(w).to] && edges.get(w).from == from) {
                if (dfs(edges.get(w).to, to, edges, visited)) return true;
            }
        }

        return false;
    }

    public static List<Edge> transform(Graph graph) {
        Map<Integer, List<Integer>> initial = graph.getGraph();

        List<Edge> edges = new LinkedList<>();

        initial.keySet().forEach((k) -> {
            for (int i = 0; i < initial.get(k).size(); i++) {
                edges.add(new Edge(k, initial.get(k).get(i)));
            }
        });

        return edges;
    }

    public List<Integer> findIntersection(List<Integer> R, List<Integer> Q) {
        List<Integer> result = new ArrayList<>();

        R.forEach((e) -> {
            if (Q.contains(e)) result.add(e);
        });

        return result;
    }

    public void printResults(List<Integer> notUsed, List<Integer> R,
                            List<Integer> Q, List<Integer> V) {

        System.out.printf(
                """
                i: %d
                R(%1$d): %s
                Q(%1$d): %s
                V%1$d: %s
                ---------
                """,
                notUsed.get(0), R, Q, V
        );
    }

    public static void main(String[] args) {
        GraphAlgorithm graphAlgorithm = new GraphAlgorithm();

        Map<Integer, List<Integer>> map = new LinkedHashMap<>();

        map.put(1, List.of(4));
        map.put(2, List.of(6, 7));
        map.put(3, List.of(2, 7));
        map.put(4, List.of(8));
        map.put(5, List.of(1, 2, 4, 6, 8, 9));
        map.put(6, List.of(7));
        map.put(7, List.of(6));
        map.put(8, List.of());
        map.put(9, List.of(8, 10, 13));
        map.put(10, List.of(6, 9, 11, 13));
        map.put(11, List.of(7, 14, 15));
        map.put(12, List.of(8, 9));
        map.put(13, List.of(12, 14));
        map.put(14, List.of(15));
        map.put(15, List.of());

        Graph graph = new Graph(map);

        graphAlgorithm.topologicalDecomposition(graph);
    }
}
