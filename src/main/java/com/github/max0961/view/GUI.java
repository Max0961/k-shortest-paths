package com.github.max0961.view;

import com.github.max0961.controller.AlgorithmRunning;
import com.github.max0961.controller.GeneratingGraph;
import com.github.max0961.controller.ReadingGraph;
import com.github.max0961.model.Graph;
import com.github.max0961.model.ksp.Eppstein.SimpleEppsteinKSP;
import com.github.max0961.model.ksp.GeneralizedDijkstra;
import com.github.max0961.model.ksp.KSP;
import com.github.max0961.model.ksp.YenKSP;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel rootPanel;
    private JTextField saveTextField;
    private JButton loadBrowseButton;
    @Getter
    private JTextField loadTextField;
    private JButton saveBrowseButton;
    private JComboBox<String> sourceComboBox1;
    private JComboBox<String> targetComboBox1;
    private JTextField vertexLabelTextField;
    private JButton generateButton;
    private JButton addEdgeButton;
    private JButton removeEdgeButton;
    private JComboBox<String> verticesComboBox;
    private JButton removeVertexButton;
    private JButton addVertexButton;
    private JTextField edgeWeightTextField;
    private JPanel vertexEditingPanel;
    private JPanel edgesEditingPanel;
    @Getter
    private JTextArea resultTextArea;
    @Getter
    private JTextArea graphTextArea;
    @Getter
    private JComboBox<String> algorithmComboBox;
    private JButton algorithmOKButton;
    @Getter
    private JComboBox<String> sourceComboBox2;
    @Getter
    private JComboBox<String> targetComboBox2;
    @Getter
    private JSpinner kSpinner;
    private JLabel targetLabel;
    private JLabel sourceLabel;
    private JLabel weightLabel;
    @Getter
    private JSpinner spinner1;
    private JButton loadOKButton;
    private JButton saveOKButton;
    @Getter
    private JSpinner spinner2;
    @Getter
    private JCheckBox constantDegreeCheckBox;
    @Getter
    private JCheckBox directedGraphCheckBox;
    private JPanel visualisationPanel;
    private JButton adjustButton;
    @Getter
    private JComboBox pathComboBox;
    private JSlider slider1;
    private JSlider slider2;
    private JTextField textField1;
    private JTextField textField2;
    private JCheckBox considerWeightsCheckBox;
    private JButton startStopButton;
    private JCheckBox directedEdgeCheckBox;
    private JTextField textField4;
    private JSlider slider3;
    private JTextField textField3;
    private JButton showButton;
    private JTextField sourceTextField;
    private JTextField targetTextField;
    private JFileChooser inputBrowseDlg = new JFileChooser();
    private JFileChooser outputBrowseDlg = new JFileChooser();
    @Setter
    @Getter
    private static KSP ksp;
    @Getter
    private Graph graph;
    @Getter
    private GraphDrawing canvas;
    private int addingCount = 0;

    public GUI() {
        add(rootPanel);
        setTitle("K-shortest path routing");

        graph = new Graph();
        graphTextArea.setText(graph.toString());
        spinner1.setValue(6);
        spinner2.setValue(5);

        algorithmComboBox.addItem(YenKSP.class.getName());
        algorithmComboBox.addItem(SimpleEppsteinKSP.class.getName());
        algorithmComboBox.addItem(GeneralizedDijkstra.class.getName());
        kSpinner.setValue(10);

        canvas = new GraphDrawing(graph);
        canvas.setDoubleBuffered(true);
        visualisationPanel.add(canvas);
        textField1.setText(Double.toString(canvas.getC1()));
        textField2.setText(Double.toString(canvas.getC2()));

        loadBrowseButton.addActionListener(e -> {
            if (inputBrowseDlg.showOpenDialog(null) == 0) {
                loadTextField.setText(inputBrowseDlg.getSelectedFile().getAbsolutePath());
            }
        });
        saveBrowseButton.addActionListener(e -> {
            if (outputBrowseDlg.showOpenDialog(null) == 0) {
                saveTextField.setText(outputBrowseDlg.getSelectedFile().getAbsolutePath());
            }
        });
        loadOKButton.addActionListener(e -> {
            Runnable runnable = new ReadingGraph(GUI.this);
            Thread thread = new Thread(runnable);
            thread.start();
            saveTextField.setText(loadTextField.getText());
        });
        saveOKButton.addActionListener(e -> {
            try {
                graph.saveToFile(saveTextField.getText());
            } catch (IOException e1) {
                graphTextArea.setText(e1.getMessage());
            }
        });

        addVertexButton.addActionListener(e -> {
            if (graph.addVertex(vertexLabelTextField.getText())) {
                updateComponents(vertexLabelTextField.getText(), true);
                setComboBoxIndex();
            }
        });
        removeVertexButton.addActionListener(e -> {
            if (verticesComboBox.getSelectedIndex() < 0) return;
            graph.removeVertex((String) verticesComboBox.getSelectedItem());
            updateComponents((String) verticesComboBox.getSelectedItem(), false);
            setComboBoxIndex();
        });
        addEdgeButton.addActionListener(e -> {
            try {
                double edgeWeight = Double.parseDouble(edgeWeightTextField.getText());
                String sourceVertexLabel = (String) sourceComboBox1.getSelectedItem();
                String targetVertexLabel = (String) targetComboBox1.getSelectedItem();
                graph.addEdge(sourceVertexLabel, targetVertexLabel, edgeWeight);
                if (!directedEdgeCheckBox.isSelected()) {
                    graph.addEdge(targetVertexLabel, sourceVertexLabel, edgeWeight);
                }
                graphTextArea.setText(graph.toString());
                updateComponents(null, false);
            } catch (Exception exception) {
                edgeWeightTextField.setText(exception.getMessage());
            }
        });
        removeEdgeButton.addActionListener(e -> {
            if (sourceComboBox1.getSelectedIndex() < 0 || targetComboBox1.getSelectedIndex() < 0) return;
            String sourceVertexLabel = (String) sourceComboBox1.getSelectedItem();
            String targetVertexLabel = (String) targetComboBox1.getSelectedItem();
            graph.removeEdge(sourceVertexLabel, targetVertexLabel);
            if (!directedEdgeCheckBox.isSelected()) {
                graph.removeEdge(targetVertexLabel, sourceVertexLabel);
            }
            graphTextArea.setText(graph.toString());
            updateComponents(null, false);
        });

        generateButton.addActionListener(e -> {
            Runnable runnable = new GeneratingGraph(GUI.this);
            Thread thread = new Thread(runnable);
            thread.start();
        });

        algorithmOKButton.addActionListener(e -> {
            if (sourceComboBox2.getSelectedIndex() < 0 || targetComboBox2.getSelectedIndex() < 0) return;
            Runnable runnable = new AlgorithmRunning(GUI.this);
            Thread thread = new Thread(runnable);
            thread.start();
        });
        pathComboBox.addActionListener(e -> {
            canvas.setPathIndex(pathComboBox.getSelectedIndex());
            canvas.repaint();
        });

        considerWeightsCheckBox.addActionListener(e -> {
            if (considerWeightsCheckBox.isSelected()) {
                canvas.setConsiderWeights(true);
            } else {
                canvas.setConsiderWeights(false);
            }
        });
        slider1.addChangeListener(e -> {
            canvas.setC1((double) slider1.getValue() / 100);
            textField1.setText(Double.toString(canvas.getC1()));
        });
        slider2.addChangeListener(e -> {
            canvas.setC2((double) slider2.getValue() / 100);
            textField2.setText(Double.toString(canvas.getC2()));
        });
        startStopButton.addActionListener(new ActionListener() {
            private boolean pulsing = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (pulsing) {
                    pulsing = false;
                    canvas.stop();
                    startStopButton.setText("Start");
                } else {
                    pulsing = true;
                    canvas.start();
                    startStopButton.setText("Stop");
                }
            }
        });
    }

    public void updateComboBoxes(String value, boolean isAdding) {
        if (isAdding) {
            verticesComboBox.addItem(value);
            sourceComboBox1.addItem(value);
            sourceComboBox2.addItem(value);
            targetComboBox1.addItem(value);
            targetComboBox2.addItem(value);
            ++addingCount;
        } else {
            verticesComboBox.removeItem(value);
            sourceComboBox1.removeItem(value);
            sourceComboBox2.removeItem(value);
            targetComboBox1.removeItem(value);
            targetComboBox2.removeItem(value);
            --addingCount;
        }
    }

    public void setComboBoxIndex() {
        targetComboBox1.setSelectedIndex(addingCount - 1);
        targetComboBox2.setSelectedIndex(addingCount - 1);
    }

    public void resetComponents() {
        verticesComboBox.removeAllItems();
        sourceComboBox1.removeAllItems();
        sourceComboBox2.removeAllItems();
        targetComboBox1.removeAllItems();
        targetComboBox2.removeAllItems();
        pathComboBox.removeAllItems();
        canvas.setPathIndex(-1);
        canvas.setInitialized(false);
        addingCount = 0;
    }

    private void updateComponents(String value, boolean isAdding) {
        if (value != null) {
            updateComboBoxes(value, isAdding);
            graphTextArea.setText(graph.toString());
        }
        canvas.setInitialized(false);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
