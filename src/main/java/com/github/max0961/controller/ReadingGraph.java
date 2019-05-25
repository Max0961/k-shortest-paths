package com.github.max0961.controller;

import com.github.max0961.model.Graph;
import com.github.max0961.model.ksp.util.TimeMeasuring;
import com.github.max0961.view.GUI;

import java.io.IOException;

public class ReadingGraph implements Runnable {
    private GUI gui;

    public ReadingGraph(GUI gui) {
        this.gui = gui;
    }

    public void run() {
        try {
            gui.resetComponents();
            gui.getGraphTextArea().setText("Reading the graph...");
            TimeMeasuring.start();
            gui.getGraph().readFromFile(gui.getLoadTextField().getText());
            TimeMeasuring.stop();
            String time = String.format("%.6fs\n", TimeMeasuring.getElapsedTime());
            gui.getGraphTextArea().setText(time + gui.getGraph().toString());
            for (Graph.Vertex v : gui.getGraph().getVertices()) {
                gui.updateComboBoxes(v.getLabel(), true);
            }
            gui.setComboBoxIndex();
        } catch (IOException e) {
            gui.getGraphTextArea().setText(e.getMessage());
        }
    }
}
