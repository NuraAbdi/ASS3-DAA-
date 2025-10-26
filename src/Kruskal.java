import java.util.*;

public class Kruskal {
    public static List<Edge> findMST(Graph graph, int[] operationsCount) {
        List<Edge> mst = new ArrayList<>();
        DisjointSet ds = new DisjointSet();

        for (String node : graph.nodes) ds.makeSet(node);
        List<Edge> sortedEdges = new ArrayList<>(graph.edges);
        Collections.sort(sortedEdges);

        for (Edge edge : sortedEdges) {
            operationsCount[0]++; // count comparison
            String rootA = ds.find(edge.from);
            String rootB = ds.find(edge.to);

            if (!rootA.equals(rootB)) {
                mst.add(edge);
                ds.union(rootA, rootB);
            }
        }
        return mst;
    }
}
