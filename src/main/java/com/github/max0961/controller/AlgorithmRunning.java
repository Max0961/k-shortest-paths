package com.github.max0961.controller;

import com.github.max0961.model.Path;
import com.github.max0961.model.ksp.Eppstein.SimpleEppsteinKSP;
import com.github.max0961.model.ksp.GeneralizedDijkstra;
import com.github.max0961.model.ksp.YenKSP;
import com.github.max0961.model.ksp.util.TimeMeasuring;
import com.github.max0961.view.GUI;

public class AlgorithmRunning implements Runnable {
    private GUI gui;

    public AlgorithmRunning(GUI gui) {
        this.gui = gui;
    }

    public void run() {
        String source = (String) gui.getSourceComboBox2().getSelectedItem();
        String target = (String) gui.getTargetComboBox2().getSelectedItem();
        int k = (int) gui.getKSpinner().getValue();
        gui.getResultTextArea().setText("Algorithm execution...");
        TimeMeasuring.start();
        switch (gui.getAlgorithmComboBox().getSelectedIndex()) {
            case 0:
                GUI.setKsp(new YenKSP(gui.getGraph(), source, target, k));
                break;
            case 1:
                GUI.setKsp(new SimpleEppsteinKSP(gui.getGraph(), source, target, k));
                break;
            case 2:
                GUI.setKsp(new GeneralizedDijkstra(gui.getGraph(), source, target, k));
                break;
        }
        TimeMeasuring.stop();
        String time = String.format("%.6fs\n", TimeMeasuring.getElapsedTime());
        gui.getResultTextArea().setText(time + GUI.getKsp().toString());
        gui.getPathComboBox().removeAllItems();
        for (Path path : GUI.getKsp().getKsp()) {
            gui.getPathComboBox().addItem(path.toString());
        }
        gui.getCanvas().setKsp(GUI.getKsp());
    }
}
