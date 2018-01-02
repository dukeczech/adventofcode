/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2017;

import adventofcode.TaskSolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hebik
 */
public class Day13 extends TaskSolver {

    private String input = null;
    private List<Layer> layers = null;
    private int maxdepth = 0;

    public Day13() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day13-2017.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day12.class.getName()).log(Level.SEVERE, null, ex);
        }
        layers = new ArrayList<>();

        try (Scanner sc = new Scanner(input)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Pattern p = Pattern.compile("(\\d+): (\\d+)");
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    int d = Integer.parseInt(m.group(1));
                    int r = Integer.parseInt(m.group(2));
                    layers.add(new Layer(d, r));
                }
            }
        }

        maxdepth = layers.stream().max(Comparator.comparingInt(Layer::getDepth)).get().getDepth();
    }

    private void part1() {
        int severity = 0;
        for (int time = 0; time <= maxdepth; time++) {
            for (Layer layer : layers) {
                if (time == layer.getDepth() && layer.getLayer() == 0) {
                    severity += layer.getDepth() * layer.getRange();
                }
                layer.move();
            }
        }

        System.out.println("[1] The severity of your whole trip: " + severity);
    }

    private void part2() {
        int delay = 0;
        boolean caught = false;
        while (!caught) {
            caught = true;
            for (Layer layer : layers) {
                if ((layer.getDepth() + delay) % (2 * layer.getRange() - 2) == 0) {
                    caught = false;
                    delay++;
                    break;
                }
            }
        }

        System.out.println("[2] The delay the packet to pass through the firewall without being caught: " + delay);
    }

    @Override
    protected void process() {
        part1();
        part2();

    }

    class Layer {

        private int depth = 0;
        private int range = 0;
        private int layer = 0;
        private boolean down = true;

        public Layer(int d, int r) {
            depth = d;
            range = r;
            layer = 0;
            down = true;
        }

        public void reset() {
            layer = 0;
            down = true;
        }

        public int getDepth() {
            return depth;
        }

        public int getLayer() {
            return layer;
        }

        public int getRange() {
            return range;
        }

        public void move() {
            if (down) {
                if (layer + 1 >= range) {
                    down = false;
                    layer--;
                } else {
                    layer++;
                }
            } else if (layer <= 0) {
                down = true;
                layer++;
            } else {
                layer--;
            }
        }

        @Override
        public String toString() {
            return depth + ": " + range + " [" + layer + "]";
        }
    }
}
