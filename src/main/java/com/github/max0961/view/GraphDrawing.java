package com.github.max0961.view;

import com.github.max0961.model.ksp.KSP;
import com.github.max0961.model.DirectedEdge;
import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import com.sun.javafx.geom.Vec2d;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public final class GraphDrawing extends JComponent implements ActionListener {
    private Graph graph;
    private HashMap<Graph.Vertex, Point> points;
    private HashMap<Graph.Vertex, ArrayList<DirectedEdge>> allEdges;
    private double stabilizer1 = 1;
    private double stabilizer2 = 1;

    @Setter
    @Getter
    private double c1 = 2;
    @Setter
    @Getter
    private double c2 = 1;
    @Setter
    @Getter
    private double c3 = 5000;
    @Setter
    @Getter
    private double c4 = 1;

    private double shiftX;
    private double shiftY;
    private double zoom;
    private final double canW = 500;
    private final double canH = 500;
    private double paddingFactor = 0.2;

    private Color pointColor1 = new Color(92, 102, 127);
    private Color pointColor2 = new Color(232, 0, 47);
    private Color edgeColor1 = new Color(92, 102, 127);
    private Color edgeColor2 = new Color(232, 0, 47);
    private Color weightColor1 = new Color(181, 42, 31);
    private Color weightColor2 = new Color(14, 0, 167);

    private Font labelFont = new Font("Courier Nev", Font.PLAIN, 12);
    private Font weightFont = new Font("Courier Nev", Font.PLAIN, 10);

    @Setter
    private KSP ksp;
    @Setter
    private int pathIndex;
    @Setter
    private boolean considerWeights;
    private Timer timer;
    @Setter
    @Getter
    private boolean isInitialized;

    protected GraphDrawing(Graph graph) {
        this.graph = graph;
        points = new HashMap<>();
        allEdges = new HashMap<>();
        timer = new Timer(5, this);
    }

    private void init() {
        zoom = 1;
        shiftX = 0;
        shiftY = 0;
        points.clear();
        allEdges.clear();
        initPointMap(canW, canH, 0, 0);
        initEdgeMap();
        Point.setWidthExtension(getPointExtension(getGraphics()));
    }

    private void animationPerform() {
        simulateSingleStep();
        initCenterAndZoom();
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        double force = getTotalForce();
        g2d.drawString(String.format("force sum: %.6f", force), 0, this.getHeight());

        g2d.setFont(weightFont);
        for (Point point : points.values()) {
            for (DirectedEdge edge : allEdges.get(point.self)) {
                drawEdge(g2d, edge, edgeColor1, true);
            }
        }
        g2d.setFont(labelFont);
        for (Point point : points.values()) {
            drawPoint(g2d, point, pointColor1);
        }
        g2d.setColor(Color.black);
        if (ksp == null || pathIndex == -1 || !isInitialized) return;
        drawPath(g2d);
    }

    private void drawPath(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2.0f));

        Path path = ksp.getKsp().get(pathIndex);
        ArrayList<Point> pointsPath = new ArrayList<>();
        for (Graph.Vertex vertex : path.getVertices()) {
            pointsPath.add(points.get(vertex));
        }
        for (int i = 1; i < pointsPath.size(); ++i) {
            drawEdge(g2d, new DirectedEdge(path.vertexAt(i - 1), path.vertexAt(i)), edgeColor2, false);
        }
        for (Point point : pointsPath) {
            drawPoint(g2d, point, pointColor2);
        }
    }

    private void drawPoint(Graphics2D g2d, Point point, Color color) {
        int x = toXScreenCoordinates(point.getLocation().x);
        int y = toYScreenCoordinates(point.getLocation().y);

        g2d.setColor(color);
        String label = point.self.getLabel();
        int fh = g2d.getFontMetrics().getHeight();
        int fw = g2d.getFontMetrics().charsWidth(label.toCharArray(), 0, label.length());
        g2d.fillOval(x - (Point.size + Point.getWidthExtension()) / 2, y - Point.size / 2,
                Point.size + Point.getWidthExtension(), Point.size);
        g2d.setColor(Color.white);
        g2d.drawString(label, x - (fw / 2), y + (fh / 3));
    }

    private int getPointExtension(Graphics g2d) {
        int max = 0;
        for (Graph.Vertex vertex : graph.getVertices()) {
            String l = vertex.getLabel();
            if (l.length() < 2) continue;
            int w = g2d.getFontMetrics().charsWidth(l.toCharArray(), 2, l.length() - 2);
            if (w > max) max = w;
        }
        return max;
    }

    private void drawEdge(Graphics2D g2d, DirectedEdge edge, Color color, boolean weightValues) {
        Point point = points.get(edge.getSource());
        Point neighbor = points.get(edge.getTarget());
        int x1 = toXScreenCoordinates(point.getLocation().x);
        int y1 = toYScreenCoordinates(point.getLocation().y);
        int x2 = toXScreenCoordinates(neighbor.getLocation().x);
        int y2 = toYScreenCoordinates(neighbor.getLocation().y);
        g2d.setPaint(color);
        g2d.drawLine(x1, y1, x2, y2);
        if (!weightValues) return;

        String weight = String.format("%.2f", edge.getWeight());
        int fh = g2d.getFontMetrics().getHeight();
        int fw = g2d.getFontMetrics().charsWidth(weight.toCharArray(), 0, weight.length());
        int mex = getMiddleValue(x1, x2);
        int mey = getMiddleValue(y1, y2);
        int doubleWeight = (int) Math.signum(point.location.y - neighbor.location.y);
        if (doubleWeight > 0) {
            g2d.setPaint(weightColor1);
        } else {
            g2d.setPaint(weightColor2);
        }
        g2d.drawString(weight, mex - fw / 2, mey + fh / 2 + doubleWeight * fh / 2);
    }

    private int getMiddleValue(int a, int b) {
        return (a + b) / 2;
    }

    private double getTotalForce() {
        computeForces();
        double f = 0;
        Vec2d nv = new Vec2d(0, 0);
        for (Point point : points.values()) {
            f += nv.distance(point.totalForce);
        }
        return f;
    }

    private void simulateSingleStep() {
        computeForces();
        for (Point point : points.values()) {
            point.location.x += c4 * point.totalForce.x;
            point.location.y += c4 * point.totalForce.y;
            point.totalForce.x = 0;
            point.totalForce.y = 0;
        }
    }

    private void initPointMap(double xBoundary, double yBoundary, double xPadding, double yPadding) {
        for (Graph.Vertex vertex : graph.getVertices()) {
            Vec2d location = new Vec2d();
            location.x = Math.random() * (xBoundary - 2 * xPadding) + xPadding;
            location.y = Math.random() * (yBoundary - 2 * yPadding) + yPadding;
            points.put(vertex, new Point(vertex, location));
        }
    }

    private void initEdgeMap() {
        for (Graph.Vertex vertex : graph.getVertices()) {
            allEdges.put(vertex, vertex.getEdges());
        }
    }

    private void computeForces() {
        for (Point a : points.values()) {
            for (Point b : points.values()) {
                if (a != b) {
                    if (a.self.getAdjacencyMap().containsKey(b.self) || b.self.getAdjacencyMap().containsKey(a.self)) {
                        a.totalForce.x += repellingForce(a, b).x + attractiveForce(a, b).x;
                        a.totalForce.y += repellingForce(a, b).y + attractiveForce(a, b).y;
                        continue;
                    }
                    a.totalForce.x += repellingForce(a, b).x;
                    a.totalForce.y += repellingForce(a, b).y;
                }
            }
        }
    }

    private Vec2d repellingForce(Point a, Point b) {
        Vec2d vec = normalizeVector(computeVector(a, b));
        double distance = a.location.distance(b.location);
        double factor = -repellingFunction(distance);
        vec.x *= factor;
        vec.y *= factor;
        return vec;
    }

    private double repellingFunction(double distance) {
        distance = Math.max(distance, stabilizer1);
        return c3 / Math.pow(distance, 2);
    }

    private Vec2d attractiveForce(Point a, Point b) {
        double distance = a.location.distance(b.location);
        Vec2d vec = normalizeVector(computeVector(a, b));
        double factor = attractiveFunction(distance);
        vec.x *= factor;
        vec.y *= factor;
        if (!considerWeights) return vec;

        if (a.self.getAdjacencyMap().containsKey(b.self)) {
            vec.x /= a.self.getAdjacencyMap().get(b.self);
            vec.y /= a.self.getAdjacencyMap().get(b.self);
        } else {
            vec.x /= b.self.getAdjacencyMap().get(a.self);
            vec.y /= b.self.getAdjacencyMap().get(a.self);
        }
        return vec;
    }

    private double attractiveFunction(double distance) {
        if (distance < stabilizer1) {
            distance = stabilizer1;
        }
        return c1 * Math.log(distance / c2) * (1 / (stabilizer2 * graph.verticesNumber()));
    }

    private Vec2d normalizeVector(Vec2d vec) {
        double length = new Vec2d(0, 0).distance(vec);
        vec.x /= length;
        vec.y /= length;
        return vec;
    }

    private Vec2d computeVector(Point a, Point b) {
        return new Vec2d(b.location.x - a.location.x, b.location.y - a.location.y);
    }

    private double[] getBoundaries() {
        double minX = Double.MAX_VALUE, maxX = 0,
                minY = Double.MAX_VALUE, maxY = 0;
        for (Point point : points.values()) {
            if (point.location.x < minX) {
                minX = point.location.x;
            }
            if (point.location.x > maxX) {
                maxX = point.location.x;
            }
            if (point.location.y < minY) {
                minY = point.location.y;
            }
            if (point.location.y > maxY) {
                maxY = point.location.y;
            }
        }
        return new double[]{minX, maxX, minY, maxY};
    }

    private int toXScreenCoordinates(double x) {
        return (int) Math.round(shiftX(zoom(x)));
    }

    private int toYScreenCoordinates(double y) {
        return (int) Math.round(shiftY(zoom(y)));
    }

    private void initCenterAndZoom() {
        double[] vec;
        if (graph.verticesNumber() > 1) {
            vec = getBoundaries();
            zoom = Math.min((1 - paddingFactor) * canW / (vec[1] - vec[0]), (1 - paddingFactor) * canH / (vec[3] - vec[2]));
            shiftX = canW / 2 - zoom(vec[0] + vec[1]) / 2;
            shiftY = canH / 2 - zoom(vec[2] + vec[3]) / 2;
        }
    }

    private double shiftX(double x) {
        return x + shiftX;
    }

    private double shiftY(double y) {
        return y + shiftY;
    }

    private double zoom(double coordinate) {
        return coordinate * zoom;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isInitialized) {
            isInitialized = true;
            init();
        }
        animationPerform();
    }

    protected void start() {
        timer.start();
    }

    protected void stop() {
        timer.stop();
    }

    static private class Point {
        @Getter
        @Setter
        private static int widthExtension = 0;
        @Getter
        @Setter
        private static int size = 20;
        @Getter
        @Setter
        private Graph.Vertex self;
        @Getter
        @Setter
        private Vec2d location;
        @Getter
        @Setter
        private Vec2d totalForce;

        protected Point(Graph.Vertex self, Vec2d location) {
            this.self = self;
            this.location = location;
            this.totalForce = new Vec2d(0, 0);
        }

        @Override
        public String toString() {
            return location.toString();
        }
    }
}
