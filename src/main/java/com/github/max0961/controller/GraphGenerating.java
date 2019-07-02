package com.github.max0961.controller;

import com.github.max0961.view.GUI;
import com.github.max0961.model.Graph;
import com.github.max0961.benchmark.CpuTimeMeasuring;

public class GraphGenerating implements Runnable {
    private GUI gui;

    public GraphGenerating(GUI gui) {
        this.gui = gui;
    }

    public void run() {
        gui.resetComponents();
        gui.getGraphTextArea().setText("Generating the graph...");
        int vertexNumber = (int) gui.getSpinner1().getValue();
        try {
            CpuTimeMeasuring.start();
            gui.getGraph().generateRandomGraph(gui.getDirectedGraphCheckBox().isSelected(),
                    gui.getConstantDegreeCheckBox().isSelected(),
                    vertexNumber, (int) gui.getSpinner2().getValue());
            CpuTimeMeasuring.stop();
        } catch (IllegalArgumentException e) {
            gui.getGraphTextArea().setText(e.getMessage());
            return;
        }
        gui.getGraphTextArea().setText(CpuTimeMeasuring.getElapsedTime() + "\n" + gui.getGraph().toString());
        for (Graph.Vertex v : gui.getGraph().getVertices()) {
            gui.updateComboBoxes(v.getLabel(), true);
        }
        gui.setComboBoxIndex();
    }
}
