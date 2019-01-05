package com.github.max0961;

import com.github.max0961.ksp.YenKSP;
import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;

public class Process {
    private String name;
    private String[] description;

    public Process(String source, String target) {
        description = new String[2];
        description[0] = source;
        description[1] = target;
        name = "Поиск кратчайшего пути.";
    }

    public Process(String source, String target, int k) {
        description = new String[3];
        description[0] = source;
        description[1] = target;
        description[2] = Integer.toString(k);
        name = "Алгоритм Йена.";
    }

    public void run(Graph graph) {
        if (description.length == 2) {
            Path path = new Path(graph, description[0], description[1]);
        }
        else {
            YenKSP yenKSP = new YenKSP(graph, description[0], description[1], Integer.parseInt(description[2]));
        }
    }

    public String getName() {
        return name;
    }
}
