import java.util.*;

public class Prim {
    public static List<Edge> findMST(Graph graph, int[] operationsCount) {
        List<Edge> mst = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        String start = graph.nodes.get(0);
        visited.add(start);

        for (Edge e : graph.edges)
            if (e.from.equals(start) || e.to.equals(start)) pq.add(e);

        while (!pq.isEmpty() && mst.size() < graph.nodes.size() - 1) {
            Edge edge = pq.poll();
            operationsCount[0]++;

            String next = visited.contains(edge.from) ? edge.to : edge.from;
            if (visited.contains(next)) continue;

            mst.add(edge);
            visited.add(next);

            for (Edge e : graph.edges) {
                if ((e.from.equals(next) && !visited.contains(e.to)) ||
                        (e.to.equals(next) && !visited.contains(e.from))) {
                    pq.add(e);
                }
            }
        }
        return mst;
    }
}
