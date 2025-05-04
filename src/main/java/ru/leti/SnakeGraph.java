package ru.leti;

import ru.leti.wise.task.graph.model.Graph;
import ru.leti.wise.task.plugin.graph.GraphProperty;

public class SnakeGraph implements GraphProperty {
    @Override
    public boolean run(Graph graph) {
        return true;
    }
}
