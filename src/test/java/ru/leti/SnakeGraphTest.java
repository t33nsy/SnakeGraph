package ru.leti;

import ru.leti.wise.task.graph.util.FileLoader;
import org.junit.jupiter.api.Test;


import java.io.FileNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class SnakeGraphTest {
    // Направленная медуза из 4-х вершин
    @Test
    public void GraphTest1() throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph1 = FileLoader.loadGraphFromJson("src/test/resources/graph1.json");
        assertThat(snakeGraph.run(graph1)).isFalse();
    }

    // Путь из 4-ч вершин
    @Test
    public void GraphTest2() throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph2 = FileLoader.loadGraphFromJson("src/test/resources/graph2.json");
        assertThat(snakeGraph.run(graph2)).isTrue();
    }

    // Путь из 8 вершин с циклом 1-2-3-4-1
    @Test
    public void GraphTest3() throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph3 = FileLoader.loadGraphFromJson("src/test/resources/graph3.json");
        assertThat(snakeGraph.run(graph3)).isTrue();
    }

    // Путь из 8 вершин с циклом 1-2-3-4-1 с доп ребром 2-6
    @Test
    public void GraphTest4() throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph4 = FileLoader.loadGraphFromJson("src/test/resources/graph4.json");
        assertThat(snakeGraph.run(graph4)).isFalse();
    }

    // Путь из 8 вершин
    @Test
    public void GraphTest5() throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph5 = FileLoader.loadGraphFromJson("src/test/resources/graph5.json");
        assertThat(snakeGraph.run(graph5)).isTrue();
    }

    // Квадрат
    @Test
    public void GraphTest6() throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph6 = FileLoader.loadGraphFromJson("src/test/resources/graph6.json");
        assertThat(snakeGraph.run(graph6)).isTrue();
    }

    // Куб без одного ребра
    @Test
    public void GraphTest7() throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph7 = FileLoader.loadGraphFromJson("src/test/resources/graph7.json");
        assertThat(snakeGraph.run(graph7)).isTrue();
    }

    // 7 тест с добавленной диагональю
    @Test
    public void GraphTest8() throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph8 = FileLoader.loadGraphFromJson("src/test/resources/graph8.json");
        assertThat(snakeGraph.run(graph8)).isFalse();
    }

    // Мега-медуза из 8 вершин
    @Test
    public void GraphTest9() throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph9 = FileLoader.loadGraphFromJson("src/test/resources/graph9.json");
        assertThat(snakeGraph.run(graph9)).isFalse();
    }

    // Путь из 9 вершин
    @Test
    public void GraphTest10() throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph10 = FileLoader.loadGraphFromJson("src/test/resources/graph10.json");
        assertThat(snakeGraph.run(graph10)).isFalse();
    }
}
