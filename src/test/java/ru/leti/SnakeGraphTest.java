package ru.leti;

import ru.leti.wise.task.graph.util.FileLoader;
import org.junit.jupiter.api.Test;


import java.io.FileNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class SnakeGraphTest {
//
    @Test
    public void GraphTest1 () throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph1 = FileLoader.loadGraphFromJson("src/test/resources/graph1.json");
        assertThat(snakeGraph.run(graph1)).isFalse();
    }
//
    @Test
    public void GraphTest2 () throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph2 = FileLoader.loadGraphFromJson("src/test/resources/graph2.json");
        assertThat(snakeGraph.run(graph2)).isTrue();
    }

    @Test
    public void GraphTest3 () throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph3 = FileLoader.loadGraphFromJson("src/test/resources/graph3.json");
        assertThat(snakeGraph.run(graph3)).isTrue();
    }

    @Test
    public void GraphTest4 () throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph4 = FileLoader.loadGraphFromJson("src/test/resources/graph4.json");
        assertThat(snakeGraph.run(graph4)).isTrue();
    }

    @Test
    public void GraphTest5 () throws FileNotFoundException {
        SnakeGraph snakeGraph = new SnakeGraph();
        var graph5 = FileLoader.loadGraphFromJson("src/test/resources/graph5.json");
        assertThat(snakeGraph.run(graph5)).isTrue();
    }
}
