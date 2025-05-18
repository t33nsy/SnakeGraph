import ru.leti.wise.task.graph.model.Edge;
import ru.leti.wise.task.graph.model.Graph;
import ru.leti.wise.task.graph.model.Vertex;
import ru.leti.wise.task.plugin.graph.GraphProperty;

import java.util.*;

public class SnakeGraph implements GraphProperty {
    // обход в глубину для поиска гамильтонова пути (по всем вершинам один раз)
    private boolean dfs(int current, Map<Integer, List<Integer>> adjacency, Set<Integer> visited, int totalVertices) {
        visited.add(current);
        if (visited.size() == totalVertices) {
            return true; // все посетили - гамильтонов путь найден
        }
        for (int neighbour : adjacency.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(neighbour)) {
                if (dfs(neighbour, adjacency, visited, totalVertices)) {
                    return true;
                }
            }
        }
        visited.remove(current); // бэктрек - назад откатываемся и пытаемся еще раз
        return false;
    }

    // построение списка смежности по списку ребер графа
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

    // генерация всех возможных бинарных векторов длины n (координаты вершин n-куба) 
    private List<int[]> generateBinaryVectors(int n) {
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < (1 << n); i++) { // от 0 до 2^n - 1
            int[] bits = new int[n];
            for (int j = 0; j < n; j++) {
                bits[n - j - 1] = (i >> j) & 1; // выделяем j-ый бит числа i
            }
            result.add(bits);
        }
        return result;
    }

    // проверка, что все ребра графа соответствуют ребрам n-куба (расстояние Хэмминга 1)
    private boolean validateEmbedding(Map<Integer, int[]> assignment, List<Edge> edges) {
        for (Edge edge : edges) {
            int[] u = assignment.get(edge.getSource());
            int[] v = assignment.get(edge.getTarget());
            if (hammingDistance(u, v) != 1) return false;
        }
        return true;
    }

    // бэктрекинг - попытка сопоставить каждой вершине координату в n-кубе
    private boolean backtrack(Map<Integer, int[]> assignment, Set<Integer> usedIndices,
                              List<Vertex> vertices, List<int[]> hypercubeCoords,
                              Map<Integer, List<Integer>> adjacency,
                              List<Edge> edges, int idx) {
        if (idx == vertices.size()) {
            return validateEmbedding(assignment, edges); // проверяем корректность финального отображения графа в n-куб
        }
        Vertex current = vertices.get(idx);
        for (int i = 0; i < hypercubeCoords.size(); i++) {
            if (usedIndices.contains(i)) continue;
            assignment.put(current.getId(), hypercubeCoords.get(i));
            usedIndices.add(i);
            // проверка промежуточного состояния - соответствуют ли уже присвоенные вершины
            if (isPartialValid(assignment, adjacency, current.getId())) {
                if (backtrack(assignment, usedIndices, vertices, hypercubeCoords, adjacency, edges, idx + 1)) {
                    return true;
                }
            }
            // не соответствуют - откатываемся в бэктрек
            assignment.remove(current.getId());
            usedIndices.remove(i);
        }
        return false;
    }

    // проверка корректности промежуточного состояния графа - соответствуют ли уже присвоенные вершины верным ребрам n-куба
    private boolean isPartialValid(Map<Integer, int[]> assignment, Map<Integer, List<Integer>> adjacency, int currentId) {
        int[] currentVec = assignment.get(currentId);
        for (int neighbor : adjacency.get(currentId)) {
            if (!assignment.containsKey(neighbor)) continue;
            int[] neighborVec = assignment.get(neighbor);
            // расстояние Хэмминга не равно 1 - неверное сопоставление
            if (hammingDistance(currentVec, neighborVec) != 1) return false;
        }
        return true;
    }

    // расстояние Хэмминга между двумя векторами (кол-во разн. компонент)
    private int hammingDistance(int[] a, int[] b) {
        int dist = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) dist++;
        }
        return dist;
    }

    // проверка, можно ли вложить граф в n-куб (отобразить граф на гиперкуб), где n - степень двойки
    private boolean isEmbeddable(Graph graph, int n) {
        int size = 1 << n;
        if (graph.getVertexCount() != size) return false; // не соответствует кол-ву вершин гиперкуба
        List<int[]> hypercubeCoords = generateBinaryVectors(n); // генерация всех координат n-куба
        List<Vertex> vertices = graph.getVertexList();
        List<Edge> edges = graph.getEdgeList();
        Map<Integer, List<Integer>> adjacency = buildAdjacencyList(graph); // построение списка смежности для графа
        return backtrack(new HashMap<>(), new HashSet<>(), vertices, hypercubeCoords, adjacency, edges, 0); // начальное состояние бэктрека
    }

    // проверка, есть ли в графе гамильтонов путь
    private boolean hasHamiltonianPath(Graph graph) {
        int n = graph.getVertexCount();
        if (n == 0) {
            return false;
        }
        // список смежности для графа
        Map<Integer, List<Integer>> adjacency = buildAdjacencyList(graph);
        // пытаемся начать с каждой вершины и провести гамильтонов путь
        for (int start : adjacency.keySet()) {
            Set<Integer> visited = new HashSet<>();
            if (dfs(start, adjacency, visited, n)) {
                return true;
            }
        }
        return false;
    }

    // основной метод интерфейса - проверка, является ли граф графом-змеей
    @Override
    public boolean run(Graph graph) {
        // если нет гамильтонова пути, то нет и змеи
        if (!hasHamiltonianPath(graph)) return false;
        int n = -1;
        for (int i = 0; i < 20; ++i) {
            if (1 << i == graph.getVertexCount()) n = i;
        }
        // если кол-во вершин не степень двойки, то нет и куба
        if (n == -1) return false;
        // проверяем, можно ли отобразить граф в n-куб
        return isEmbeddable(graph, n);
    }
}
