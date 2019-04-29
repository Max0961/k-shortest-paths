package com.github.max0961;

import com.github.max0961.ksp.Eppstein.SimpleEppsteinKSP;
import com.github.max0961.ksp.KSP;
import com.github.max0961.ksp.YenKSP;
import com.github.max0961.model.Graph;
import com.github.max0961.util.TimeMeasuring;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Application {
    private static StringBuilder stringBuilder = new StringBuilder();
    private static KSP ksp;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new GUI();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public static void executeAlgorithm(String algorithm, String s, String t, Graph graph, int k) {
        stringBuilder.append("Исток: ").append(s).append("Сток: ").append(t).append("\n");
        stringBuilder.append("Количество путей K:").append(k).append("\n");
        stringBuilder.append("Алгоритм:").append(algorithm).append("\n");
        System.out.print(stringBuilder);
        stringBuilder.setLength(0);
        TimeMeasuring.start();
        switch (algorithm) {
            case "Yen":
                ksp = new YenKSP(graph, s, t, k);
                break;
            case "SimpleEppstein":
                ksp = new SimpleEppsteinKSP(graph, s, t, k);
                break;
            case "Eppstein":
                break;
        }
        stringBuilder.append(ksp);
        System.out.print(stringBuilder);
        stringBuilder.setLength(0);
        TimeMeasuring.stop();
    }

    public static void saveResult(String fileName, double[][] values) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8)) {
            for (double[] value : values) {
                StringBuilder string = new StringBuilder();
                string.append((int) value[0]).append("\t");
                string.append((int) value[1]).append("\t");
                string.append(String.format("%f", value[2])).append("\n");
                writer.write(string.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
