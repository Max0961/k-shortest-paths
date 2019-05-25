package com.github.max0961.controller;

import com.github.max0961.view.GUI;
import com.github.max0961.model.Graph;
import com.github.max0961.model.ksp.util.TimeMeasuring;

public class GeneratingGraph implements Runnable {
    private GUI gui;

    public GeneratingGraph(GUI gui) {
        this.gui = gui;
    }

    public void run() {
        gui.resetComponents();
        gui.getGraphTextArea().setText("Generating the graph...");
        int vertexNumber = (int) gui.getSpinner1().getValue();
        try {
            TimeMeasuring.start();
            gui.getGraph().generateRandomGraph(gui.getDirectedGraphCheckBox().isSelected(),
                    gui.getConstantDegreeCheckBox().isSelected(),
                    vertexNumber, (int) gui.getSpinner2().getValue());
            TimeMeasuring.stop();
        } catch (IllegalArgumentException e) {
            gui.getGraphTextArea().setText(e.getMessage());
            return;
        }
        String time = String.format("%.6fs\n", TimeMeasuring.getElapsedTime());
        gui.getGraphTextArea().setText(time + gui.getGraph().toString());
        for (Graph.Vertex v : gui.getGraph().getVertices()) {
            gui.updateComboBoxes(v.getLabel(), true);
        }
        gui.setComboBoxIndex();
    }
}
