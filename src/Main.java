import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.json.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Paths.get("ass_3_input.json"));
        JSONObject root = new JSONObject(input);
        JSONArray graphs = root.getJSONArray("graphs");
        JSONArray results = new JSONArray();

        for (int i = 0; i < graphs.length(); i++) {
            JSONObject g = graphs.getJSONObject(i);
            List<String> nodes = new ArrayList<>();
            for (Object n : g.getJSONArray("nodes")) nodes.add(n.toString());

            List<Edge> edges = new ArrayList<>();
            JSONArray eArr = g.getJSONArray("edges");
            for (int j = 0; j < eArr.length(); j++) {
                JSONObject e = eArr.getJSONObject(j);
                edges.add(new Edge(e.getString("from"), e.getString("to"), e.getInt("weight")));
            }

            Graph graph = new Graph(nodes, edges);

            // --- Prim
            int[] primOps = {0};
            long startPrim = System.nanoTime();
            List<Edge> primMST = Prim.findMST(graph, primOps);
            long endPrim = System.nanoTime();

            int primCost = primMST.stream().mapToInt(e -> e.weight).sum();

            // --- Kruskal
            int[] kruskalOps = {0};
            long startKruskal = System.nanoTime();
            List<Edge> kruskalMST = Kruskal.findMST(graph, kruskalOps);
            long endKruskal = System.nanoTime();

            int kruskalCost = kruskalMST.stream().mapToInt(e -> e.weight).sum();

            JSONObject result = new JSONObject();
            result.put("graph_id", g.getInt("id"));

            JSONObject stats = new JSONObject();
            stats.put("vertices", nodes.size());
            stats.put("edges", edges.size());
            result.put("input_stats", stats);

            result.put("prim", createAlgorithmResult(primMST, primCost, primOps[0], (endPrim - startPrim)/1_000_000.0));
            result.put("kruskal", createAlgorithmResult(kruskalMST, kruskalCost, kruskalOps[0], (endKruskal - startKruskal)/1_000_000.0));

            results.put(result);
        }

        JSONObject output = new JSONObject();
        output.put("results", results);

        Files.writeString(Paths.get("ass_3_output.json"), output.toString(4));
        System.out.println("✅ Results saved to ass_3_output.json");
    }

    private static JSONObject createAlgorithmResult(List<Edge> mst, int cost, int ops, double time) {
        JSONArray edgesArr = new JSONArray();
        for (Edge e : mst) {
            JSONObject obj = new JSONObject();
            obj.put("from", e.from);
            obj.put("to", e.to);
            obj.put("weight", e.weight);
            edgesArr.put(obj);
        }

        JSONObject res = new JSONObject();
        res.put("mst_edges", edgesArr);
        res.put("total_cost", cost);
        res.put("operations_count", ops);
        res.put("execution_time_ms", time);
        return res;
    }
}
