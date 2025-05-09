package ru.leti;

import ru.leti.wise.task.graph.model.Edge;
import ru.leti.wise.task.graph.model.Graph;
import ru.leti.wise.task.graph.model.Vertex;
import ru.leti.wise.task.plugin.graph.GraphProperty;

import java.util.*;

public class SnakeGraph implements GraphProperty {
    private boolean dfs(int current, Map<Integer, List<Integer>> adjacency, Set<Integer> visited, int totalVertices) {
        visited.add(current);
        if (visited.size() == totalVertices) {
            return true;
        }
        for (int neighbour : adjacency.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(neighbour)) {
                if (dfs(neighbour, adjacency, visited, totalVertices)) {
                    return true;
                }
            }
        }
        visited.remove(current); // backtracking
        return false;
    }

    private Map<Integer, List<Integer>> buildAdjacencyList(Graph graph) {
        Map<Integer, List<Integer>> adjacency = new HashMap<>();
        for (Edge edge : graph.getEdgeList()) {
            int u = edge.getSource();
            int v = edge.getTarget();
            adjacency.computeIfAbsent(u, key -> new ArrayList<>()).add(v);
            if (!graph.isDirect()) {
                adjacency.computeIfAbsent(v, key -> new ArrayList<>()).add(u);
            }
        }
        return adjacency;
    }

    private List<int[]> generateBinaryVectors(int n) {
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < (1 << n); i++) {
            int[] bits = new int[n];
            for (int j = 0; j < n; j++) {
                bits[n - j - 1] = (i >> j) & 1;
            }
            result.add(bits);
        }
        return result;
    }

    private boolean validateEmbedding(Map<Integer, int[]> assignment, List<Edge> edges) {
        for (Edge edge : edges) {
            int[] u = assignment.get(edge.getSource());
            int[] v = assignment.get(edge.getTarget());
            if (hammingDistance(u, v) != 1) return false;
        }
        return true;
    }

    private boolean backtrack(Map<Integer, int[]> assignment, Set<Integer> usedIndices,
                              List<Vertex> vertices, List<int[]> hypercubeCoords,
                              Map<Integer, List<Integer>> adjacency,
                              List<Edge> edges, int idx) {
        if (idx == vertices.size()) {
            return validateEmbedding(assignment, edges);
        }
        Vertex current = vertices.get(idx);
        for (int i = 0; i < hypercubeCoords.size(); i++) {
            if (usedIndices.contains(i)) continue;
            assignment.put(current.getId(), hypercubeCoords.get(i));
            usedIndices.add(i);
            if (isPartialValid(assignment, adjacency, current.getId())) {
                if (backtrack(assignment, usedIndices, vertices, hypercubeCoords, adjacency, edges, idx + 1)) {
                    return true;
                }
            }
            assignment.remove(current.getId());
            usedIndices.remove(i);
        }
        return false;
    }

    private boolean isPartialValid(Map<Integer, int[]> assignment, Map<Integer, List<Integer>> adjacency, int currentId) {
        int[] currentVec = assignment.get(currentId);
        for (int neighbor : adjacency.get(currentId)) {
            if (!assignment.containsKey(neighbor)) continue;
            int[] neighborVec = assignment.get(neighbor);
            if (hammingDistance(currentVec, neighborVec) != 1) return false;
        }
        return true;
    }

    private int hammingDistance(int[] a, int[] b) {
        int dist = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) dist++;
        }
        return dist;
    }

    public boolean isEmbeddable(Graph graph, int n) {
        int size = 1 << n;
        if (graph.getVertexCount() != size) return false;
        List<int[]> hypercubeCoords = generateBinaryVectors(n);
        List<Vertex> vertices = graph.getVertexList();
        List<Edge> edges = graph.getEdgeList();
        Map<Integer, List<Integer>> adjacency = buildAdjacencyList(graph);
        return backtrack(new HashMap<>(), new HashSet<>(), vertices, hypercubeCoords, adjacency, edges, 0);
    }

    private boolean hasHamiltonianPath(Graph graph) {
        int n = graph.getVertexCount();
        if (n == 0) {
            return false;
        }
        // adjacency list
        Map<Integer, List<Integer>> adjacency = buildAdjacencyList(graph);
        // try to start from every vertex
        for (int start : adjacency.keySet()) {
            Set<Integer> visited = new HashSet<>();
            if (dfs(start, adjacency, visited, n)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean run(Graph graph) {
        if (!hasHamiltonianPath(graph)) return false;
        int n = -1;
        for (int i = 0; i < 20; ++i) {
            if (1 << i == graph.getVertexCount()) n = i;
        }
        if (n == -1) return false;
        return isEmbeddable(graph, n);
    }
}
