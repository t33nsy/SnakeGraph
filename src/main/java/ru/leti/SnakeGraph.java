package ru.leti;

import ru.leti.wise.task.graph.model.Edge;
import ru.leti.wise.task.graph.model.Graph;
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

    private List<String> generateBinaryStrings(int n) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < (1 << n); ++i) {
            String bin = Integer.toBinaryString(i);
            result.add("0".repeat(n - bin.length()) + bin);
        }
        return result;
    }

    private List<String> positionsOfOnes(String s) {
        List<String> pos = new ArrayList<>();
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '1') pos.add(String.valueOf(i));
        }
        return pos;
    }

    private String flipBit(String s, String... positions) {
        char[] chars = s.toCharArray();
        for (String pos : positions) {
            int i = Integer.parseInt(pos);
            chars[i] = chars[i] == '0' ? '1' : '0';
        }
        return new String(chars);
    }

    private int hammingDistance(String a, String b) {
        int count = 0;
        for (int i = 0; i < a.length(); ++i) {
            if (a.charAt(i) != b.charAt(i)) ++count;
        }
        return count;
    }

    private boolean isHyperCube(Graph graph) {
        int nVertices = graph.getVertexCount();
        int n = -1;
        // num of vertices is power of 2
        for (int i = 0; i <= 20; ++i) {
            if ((1 << i) == nVertices) {
                n = i;
                break;
            }
        }
        if (n == -1) return false;
        // labels
        Map<Integer, List<Integer>> adjacency = buildAdjacencyList(graph);
        Map<Integer, String> labels = new HashMap<>();
        Map<String, Integer> inverseLabels = new HashMap<>();
        int start = adjacency.keySet().iterator().next();
        labels.put(start, "0".repeat(n));
        inverseLabels.put("0".repeat(n), start);
        // first neighbours have 1 "1"
        List<Integer> neighbours = adjacency.get(start);
        for (int i = 0; i < neighbours.size(); ++i) {
            String label = "0".repeat(i) + "1" + "0".repeat(n - i - 1);
            labels.put(neighbours.get(i), label);
            inverseLabels.put(label, neighbours.get(i));
        }
        // go to others
        List<String> allBinaryStrings = generateBinaryStrings(n);
        Collections.sort(allBinaryStrings);
        for (String z : allBinaryStrings) {
            if (inverseLabels.containsKey(z)) continue;
            List<String> ones = positionsOfOnes(z);
            if (ones.size() < 2) continue;
            // u, v, w generating
            for (int i = 0; i < ones.size(); ++i) {
                for (int j = i + 1; j < ones.size(); ++j) {
                    String u = flipBit(z, ones.get(i));
                    String v = flipBit(z, ones.get(j));
                    String w = flipBit(z, ones.get(i), ones.get(j));
                    if (inverseLabels.containsKey(u) && inverseLabels.containsKey(v) && inverseLabels.containsKey(w)) {
                        int uNode = inverseLabels.get(u);
                        int vNode = inverseLabels.get(v);
                        int wNode = inverseLabels.get(w);
                        List<Integer> common = new ArrayList<>(adjacency.get(uNode));
                        common.retainAll(adjacency.get(vNode));
                        if (common.size() != 2) continue;

                        for (int candidate : common) {
                            if (candidate != wNode && !labels.containsKey(candidate)) {
                                labels.put(candidate, z);
                                inverseLabels.put(z, candidate);
                                break;
                            }
                        }
                    }
                }
            }
        }
        // hamming distance == 1
        for (int u : adjacency.keySet()) {
            for (int v : adjacency.get(u)) {
                if (u < v) {
                    String a = labels.get(u);
                    String b = labels.get(v);
                    if (hammingDistance(a, b) != 1) return false;
                }
            }
        }

        return true;
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
        return isHyperCube(graph);
    }
}
