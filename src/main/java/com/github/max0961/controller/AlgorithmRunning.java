package com.github.max0961.controller;

import com.github.max0961.model.Path;
import com.github.max0961.model.ksp.Eppstein.SimpleEppsteinKSP;
import com.github.max0961.model.ksp.GeneralizedDijkstra;
import com.github.max0961.model.ksp.YenKSP;
import com.github.max0961.benchmark.CpuTimeMeasuring;
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
        CpuTimeMeasuring.start();
        try {
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
        } catch (IllegalArgumentException e) {
            gui.getResultTextArea().setText(e.getLocalizedMessage());
            return;
        }
        CpuTimeMeasuring.stop();
        gui.getResultTextArea().setText(CpuTimeMeasuring.getElapsedTime() + "\n" + GUI.getKsp().toString());
        gui.getPathComboBox().removeAllItems();
        for (Path path : GUI.getKsp().getKsp()) {
            gui.getPathComboBox().addItem(path.toString());
        }
        gui.getCanvas().setKsp(GUI.getKsp());
    }
}
