package com.github.max0961.controller;

import com.github.max0961.model.Graph;
import com.github.max0961.benchmark.CpuTimeMeasuring;
import com.github.max0961.view.GUI;

import java.io.IOException;

public class GraphReading implements Runnable {
    private GUI gui;

    public GraphReading(GUI gui) {
        this.gui = gui;
    }

    public void run() {
        try {
            gui.resetComponents();
            gui.getGraphTextArea().setText("Reading the graph...");
            CpuTimeMeasuring.start();
            gui.getGraph().readFromFile(gui.getLoadTextField().getText());
            CpuTimeMeasuring.stop();
            gui.getGraphTextArea().setText(gui.getGraph().toString());
            for (Graph.Vertex v : gui.getGraph().getVertices()) {
                gui.updateComboBoxes(v.getLabel(), true);
            }
            gui.setComboBoxIndex();
        } catch (IOException e) {
            gui.getGraphTextArea().setText(e.getMessage());
        }
    }
}
