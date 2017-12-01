/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.TaskSolver;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author karel.hebik
 */
public class Day13 extends TaskSolver {

    private int favourite = 0;
    private Set<Node> moves = null;

    public Day13() {
        favourite = 1358;
        moves = new HashSet<>();
    }

    @Override
    public void process() {
        Node start = new Node(36, 43, new Point(1, 1));
        Node shortest = new Node(start);
        Node coverage = new Node(start);
        System.out.println("Starting area:");
        System.out.println(start);

        LinkedList<Node> nodes = start.nextNodes();
        while (!nodes.isEmpty()) {
            LinkedList<Node> nextNodes = new LinkedList<>();
            for (Node node : nodes) {
                System.out.println(node);
                if (node.getSteps() <= 50) {
                    coverage.join(node);
                }

                if (node.isOnPosition(31, 39)) {
                    if (shortest.getSteps() == 0 || shortest.getSteps() > node.getSteps()) {
                        shortest = node;
                    }
                    break;
                }
                nextNodes.addAll(node.nextNodes());

            }

            nodes = nextNodes;
        }

        System.out.println("Fewest number of steps required for you to reach 31,39: " + shortest.getSteps());
        System.out.println("Total states: " + moves.size());
        System.out.println("Reachable locations under 50 steps: " + coverage.getVisitedPoints());
    }

    public enum State {
        OPEN, WALL, VISITED
    }

    private class Node {

        private State[][] area = null;
        private Point position = null;
        private int steps = 0;

        public Node(int dimx, int dimy) {
            area = new State[dimy][dimx];
            for (int y = 0; y < area.length; y++) {
                for (int x = 0; x < area[y].length; x++) {
                    int result = (x * x + 3 * x + 2 * x * y + y + y * y) + favourite;
                    int count = Integer.bitCount(result);
                    if (count % 2 != 0) {
                        area[y][x] = State.WALL;
                    } else {
                        area[y][x] = State.OPEN;
                    }
                }
            }
            position = new Point(0, 0);
        }

        public Node(int dimx, int dimy, Point start) {
            this(dimx, dimy);
            position = start;
            area[position.y][position.x] = State.VISITED;
        }

        public Node(Node another) {
            area = new State[another.area.length][another.area[0].length];
            for (int y = 0; y < area.length; y++) {
                for (int x = 0; x < area[y].length; x++) {
                    area[y][x] = another.area[y][x];
                }
            }
            position = new Point(another.position);
            steps = another.steps;
        }

        public void step(int dx, int dy) {
            position.translate(dx, dy);
            area[position.y][position.x] = State.VISITED;
            steps++;
        }

        public State getState(int x, int y) {
            return area[y][x];
        }

        public int getSteps() {
            return steps;
        }

        public Point getPosition() {
            return position;
        }

        public boolean isOnPosition(int x, int y) {
            return position.x == x && position.y == y;
        }

        public int getVisitedPoints() {
            int count = 0;
            for (int y = 0; y < area.length; y++) {
                for (int x = 0; x < area[y].length; x++) {
                    if (area[y][x] == State.VISITED) {
                        count++;
                    }
                }
            }
            return count;
        }

        public void join(Node another) {
            for (int y = 0; y < area.length; y++) {
                for (int x = 0; x < area[y].length; x++) {
                    if (another.area[y][x] == State.VISITED) {
                        area[y][x] = another.area[y][x];
                    }
                }
            }
        }

        public LinkedList<Node> nextNodes() {
            LinkedList<Node> output = new LinkedList<>();

            if (position.x > 0 && getState(position.x - 1, position.y) == State.OPEN) {
                Node next = new Node(this);
                next.step(-1, 0);
                if (!moves.contains(next)) {
                    output.add(next);
                    moves.add(next);
                }
            }

            if (position.x < area[0].length - 1 && getState(position.x + 1, position.y) == State.OPEN) {
                Node next = new Node(this);
                next.step(1, 0);
                if (!moves.contains(next)) {
                    output.add(next);
                    moves.add(next);
                }
            }

            if (position.y > 0 && getState(position.x, position.y - 1) == State.OPEN) {
                Node next = new Node(this);
                next.step(0, -1);
                if (!moves.contains(next)) {
                    output.add(next);
                    moves.add(next);
                }
            }

            if (position.y < area.length - 1 && getState(position.x, position.y + 1) == State.OPEN) {
                Node next = new Node(this);
                next.step(0, 1);
                if (!moves.contains(next)) {
                    output.add(next);
                    moves.add(next);
                }
            }

            return output;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + Arrays.deepHashCode(area);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Node other = (Node) obj;
            if (!Arrays.deepEquals(area, other.area)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("   ");
            for (int i = 0; i < area[0].length; i++) {
                if (i < 10) {
                    sb.append(" ");
                } else {
                    sb.append((int) (i / 10));
                }
            }
            sb.append(System.lineSeparator());
            sb.append("   ");
            for (int i = 0; i < area[0].length; i++) {
                if (i < 10) {
                    sb.append(i);
                } else {
                    sb.append((int) (i % 10));
                }
            }
            sb.append(System.lineSeparator());
            for (int y = 0; y < area.length; y++) {
                if (y < 10) {
                    sb.append(" ");
                }
                sb.append(y).append(" ");
                for (int x = 0; x < area[y].length; x++) {
                    switch (area[y][x]) {
                        case OPEN:
                            sb.append(".");
                            break;
                        case WALL:
                            sb.append("#");
                            break;
                        case VISITED:
                            sb.append("O");
                            break;
                        default:
                            sb.append("?");
                            break;
                    }
                }
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        }

    }
}
