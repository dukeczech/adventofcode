/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.TaskSolver;
import adventofcode.Utils;
import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author karel.hebik
 */
public class Day17 extends TaskSolver {

    private String puzzle = null;

    public Day17() {
        puzzle = "awrkjxxr";
    }

    @Override
    public void process() {
        Node start = new Node(new Point(4, 4));
        Node shortest = null;
        Node longest = new Node(start);

        LinkedList<Node> nodes = start.nextNodes();
        while (!nodes.isEmpty()) {
            LinkedList<Node> nextNodes = new LinkedList<>();
            for (Node node : nodes) {
                if (node.reached()) {
                    if (shortest == null) {
                        shortest = new Node(node);
                    }
                    if (node.getDistance() > longest.getDistance()) {
                        longest = new Node(node);
                    }
                } else {
                    nextNodes.addAll(node.nextNodes());
                }
            }
            nodes = nextNodes;
        }

        System.out.println("Shortest path to reach the vaultnode: " + shortest);
        System.out.println("Longest path to reach the vaultnode: " + longest);
    }

    class Node {

        private Point dimension = null;
        private Point position = null;
        private int steps = 0;
        private String path = null;

        public Node(Point dims) {
            dimension = dims;
            position = new Point(0, 0);
            steps = 0;
            path = "";
        }

        public Node(Node another) {
            dimension = new Point(another.dimension);
            position = new Point(another.position);
            steps = another.steps;
            path = another.path;
        }

        public int getDistance() {
            return steps;
        }

        public boolean reached() {
            return position.x == dimension.x - 1 && position.y == dimension.y - 1;
        }

        public void step(int dx, int dy) {
            if (dx > 1 || dx < -1 || dy > 1 || dy < -1 || Math.abs(dx) - Math.abs(dy) == 0) {
                System.err.println("One step at the time and direction!");
                return;
            }
            position.translate(dx, dy);
            steps++;
            if (dx == 1) {
                path += "R";
            } else if (dx == -1) {
                path += "L";
            } else if (dy == 1) {
                path += "D";
            } else if (dy == -1) {
                path += "U";
            }
        }

        public LinkedList<Node> nextNodes() {
            LinkedList<Node> output = new LinkedList<>();

            String hash = Utils.md5(puzzle + path);

            if (hash.substring(0, 1).matches("[b-f]") && position.y > 0) {
                Node next = new Node(this);
                next.step(0, -1);
                output.add(next);
            }

            if (hash.substring(1, 2).matches("[b-f]") && position.y < dimension.y - 1) {
                Node next = new Node(this);
                next.step(0, 1);
                output.add(next);
            }

            if (hash.substring(2, 3).matches("[b-f]") && position.x > 0) {
                Node next = new Node(this);
                next.step(-1, 0);
                output.add(next);
            }

            if (hash.substring(3, 4).matches("[b-f]") && position.x < dimension.x - 1) {
                Node next = new Node(this);
                next.step(1, 0);
                output.add(next);
            }

            return output;
        }

        @Override
        public String toString() {
            return "[" + position.x + ", " + position.y + "]: (" + steps + ") - " + path;
        }
    }
}
