/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.LineProcessor;
import adventofcode.ReadLineFileReader;
import adventofcode.TaskSolver;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day22 extends TaskSolver implements LineProcessor {

    private List<Node> list = null;
    private Set<String> moves = null;

    public Day22() {
        list = new ArrayList<>();
        moves = new HashSet<>();
    }

    @Override
    public void processLine(String line) {
        Pattern nodeLine = Pattern.compile("\\/dev\\/grid\\/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)\\%");
        Matcher mr = nodeLine.matcher(line.trim());

        if (mr.matches()) {
            int x = Integer.parseInt(mr.group(1));
            int y = Integer.parseInt(mr.group(2));
            int size = Integer.parseInt(mr.group(3));
            int used = Integer.parseInt(mr.group(4));
            int avail = Integer.parseInt(mr.group(5));
            int use = Integer.parseInt(mr.group(6));

            list.add(new Node(x, y, size, used));
        }
    }

    @Override
    public void process() {
        ReadLineFileReader reader = new ReadLineFileReader("input/day22.txt", this);
        reader.start();

        State start = new State(list);
        System.out.println(start);

        System.out.println("Dimensions: " + start.getDimension().x + " x " + start.getDimension().y);
        int viable = 0;
        for (Node n1 : list) {
            if (n1.getUsed() > 0) {
                for (Node n2 : list) {
                    if (n1 != n2 && n2.getAvail() >= n1.getUsed()) {
                        viable++;
                    }
                }
            }
        }

        System.out.println("Viable pairs of nodes: " + viable);

        int moveCount = 0;
        // Get shortest path from empty node to the front of goal node
        Node empty = start.getEmpty().get(0);
        Node goal = start.getData();
        Node end = start.getNode(goal.x - 1, goal.y);

        System.out.println("Empty: " + empty);
        System.out.println("Goal: " + goal);
        System.out.println("End: " + end);
        List<Node> path = start.astar(empty, end);

        System.out.println(start);

        // Start to move data
        Node from = empty;
        for (Node to : path.subList(1, path.size())) {
            from.setPath(true);
            if (!start.move(from, to)) {
                System.err.println("Unable to move!");
                return;
            } else {
                moveCount++;
            }
            from = to;
        }

        System.out.println(start);

        while (!start.isDone()) {
            empty = start.getEmpty().get(0);
            goal = start.getData();
            if (!start.move(goal, empty)) {
                System.err.println("Unable to move!");
                return;
            } else {
                moveCount++;
            }

            System.out.println(start);
            
            if (start.isDone()) {
                System.out.println("Data transfer is done");
                break;
            }
            
            empty = start.getEmpty().get(0);
            goal = start.getData();
            end = start.getNode(goal.x - 1, goal.y);
            path = start.astar(empty, end);

            // Start to move data
            from = path.get(0);
            for (Node to : path.subList(1, path.size())) {
                from.setPath(true);
                if (!start.move(from, to)) {
                    System.err.println("Unable to move!");
                    return;
                } else {
                    moveCount++;
                }
                from = to;
            }

            System.out.println(start);
        }

        System.out.println("Moves: " + moveCount);
    }

    class State {

        private List<Node> list = null;
        private Node[][] nodes = null;
        private Point dimension = null;
        private Point data = null;

        public State(List<Node> ln) {
            list = new ArrayList<>();
            dimension = new Point(0, 0);
            ln.stream().forEach(n -> list.add(n));
            dimension.x = list.stream().max(Comparator.comparing(n -> n.x)).get().x + 1;
            dimension.y = list.stream().max(Comparator.comparing(n -> n.y)).get().y + 1;
            data = new Point(dimension.x - 1, 0);
            nodes = new Node[dimension.y][dimension.x];

            list.stream().forEach(n -> nodes[n.y][n.x] = n);
        }

        public State(State another) {
            list = new ArrayList<>();
            dimension = new Point(another.dimension);
            another.list.stream().forEach(n -> list.add(new Node(n)));
            data = new Point(another.data);
            nodes = new Node[dimension.y][dimension.x];

            list.stream().forEach(n -> nodes[n.y][n.x] = n);
        }

        public Point getDimension() {
            return dimension;
        }

        public boolean isDone() {
            return data.x == 0 && data.y == 0;
        }

        public LinkedList<State> nextStates() {
            LinkedList<State> output = new LinkedList<>();

            List<Node> empty = getEmpty();
            for (Node to : empty) {
                List<Node> adjacent = getAdjacent(to);
                for (Node from : adjacent) {
                    State next = new State(this);
                    if (next.move(from.getLocation(), to.getLocation())) {
                        output.add(next);
                    }
                }
            }

            return output;
        }

        public Node getData() {
            return nodes[data.y][data.x];
        }

        public boolean isData(Node n) {
            return n.x == data.x && n.y == data.y;
        }

        public List<Node> getEmpty() {
            List<Node> empty = new ArrayList<>();
            list.stream().filter((nn) -> (nn.getUsed() == 0)).forEach((nn) -> {
                empty.add(nn);
            });
            if (empty.isEmpty()) {
                return null;
            }
            return empty;
        }

        public List<Node> getAdjacent(Node node) {
            List<Node> adjacent = new ArrayList<>();
            for (Node nn : list) {
                if (node == nn) {
                    continue;
                }
                if (nn.getSize() >= 500) {
                    continue;
                }
                if (node.x == nn.x && Math.abs(node.y - nn.y) == 1) {
                    // The nodes above or under
                    adjacent.add(nn);
                }
                if (node.y == nn.y && Math.abs(node.x - nn.x) == 1) {
                    // The nodes above or under
                    adjacent.add(nn);
                }
            }
            return adjacent;
        }

        public Node getNode(int x, int y) {
            return nodes[y][x];
        }

        public List<Node> astar(Node start, Node end) {
            Set<Node> closed = new HashSet<>();
            Set<Node> opened = new HashSet<>();
            opened.add(start);

            Map<Node, Node> came = new HashMap<>();

            start.setG(0);
            start.setH(start.getDistance(end));

            while (!opened.isEmpty()) {
                Node current = opened.stream().min(Comparator.comparing(n -> n.getF())).get();
                if (current == end) {
                    return getPath(end, came);
                }
                opened.remove(current);
                closed.add(current);
                List<Node> adjacent = getAdjacent(current);

                for (Node next : adjacent) {
                    if (closed.contains(next)) {
                        continue;
                    }
                    if (isData(next)) {
                        continue;
                    }
                    int gscore = current.getG() + current.getDistance(next);

                    boolean better = false;
                    if (!opened.contains(next)) {
                        opened.add(next);
                        better = true;
                    } else if (gscore < next.getG()) {
                        better = true;
                    } else {
                        better = false;
                    }

                    if (better) {
                        next.setVisited(true);
                        came.put(next, current);
                        next.setG(gscore);
                        next.setH(next.getDistance(end));
                    }
                }
            }
            return null;
        }

        public Node getPrevious(Node current, Map<Node, Node> came) {
            if (came.containsKey(current)) {
                return came.get(current);
            }
            return null;
        }

        public List<Node> getPath(Node current, Map<Node, Node> came) {
            List<Node> path = new ArrayList<>();
            path.add(current);

            Node previous = getPrevious(current, came);
            while (previous != null) {
                path.add(previous);
                previous = getPrevious(previous, came);
            }
            Collections.reverse(path);
            return path;
        }

        public boolean move(Point from, Point to) {
            Node source = nodes[from.y][from.x];
            Node destination = nodes[to.y][to.x];
            List<Node> adjacent = getAdjacent(source);
            if (!adjacent.contains(destination)) {
                System.err.println("Destination is not adjacent node: " + from + " -> " + to);
                return false;
            }
            if (destination.getSize() < source.getUsed()) {
                System.err.println("Destination node low available space!");
                return false;
            }

            if (destination.getUsed() > source.getSize()) {
                System.err.println("Source node low available space!");
                return false;
            }

            String move = source.toString();
            if (source.x == destination.x) {
                if (source.y < destination.y) {
                    move += " v ";
                } else {
                    move += " ^ ";
                }
            } else if (source.x < destination.x) {
                move += " > ";
            } else {
                move += " < ";
            }
            move += destination;

            moves.add(move);

            System.out.println(move + " OK");

            int size = source.getUsed();
            source.setUsed(destination.getUsed());
            destination.setUsed(size);

            if (from.x == data.x && from.y == data.y) {
                System.out.println("G node moved from " + from.x + "," + from.y + " to " + to.x + "," + to.y);
                data = new Point(to);
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + Arrays.deepHashCode(nodes);
            hash = 53 * hash + Objects.hashCode(data);
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
            final State other = (State) obj;
            if (!Arrays.deepEquals(nodes, other.nodes)) {
                return false;
            }
            if (!Objects.equals(data, other.data)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < nodes.length; y++) {
                for (int x = 0; x < nodes[y].length; x++) {
                    Node nd = nodes[y][x];
                    if (nd.x == 0 && nd.y == 0) {
                        sb.append("(");
                    } else {
                        sb.append(" ");
                    }

                    if (x == data.x && y == data.y) {
                        sb.append("G");
                    } else if (nd.getUsed() == 0) {
                        sb.append("_");
                    } else if (nd.isPath()) {
                        sb.append("O");
                    } else if (nd.isVisited()) {
                        sb.append("*");
                    } else {
                        List<Node> adjacent = getAdjacent(nd);
                        boolean fit = false;
                        if (adjacent != null) {
                            for (Node adj : adjacent) {
                                if (adj.getSize() >= nd.getUsed()) {
                                    fit = true;
                                    break;
                                }
                            }
                        }
                        if (fit && nd.getSize() < 500) {
                            sb.append(".");
                        } else {
                            sb.append("#");
                        }
                    }

                    if (nd.x == 0 && nd.y == 0) {
                        sb.append(")");
                    } else {
                        sb.append(" ");
                    }
                }
                sb.append(System.lineSeparator());
            }

            return sb.toString();
        }
    }

    class Node extends Point {

        private int size = 0;
        private int used = 0;
        private int gscore = 0;
        private int hscore = 0;
        private boolean visited = false;
        private boolean path = false;

        public Node(int x, int y, int s, int u) {
            super(x, y);
            size = s;
            used = u;
            gscore = 0;
            hscore = 0;
            visited = false;
            path = false;
        }

        public Node(Node another) {
            super(another);
            size = another.size;
            used = another.used;
            gscore = another.gscore;
            gscore = another.hscore;
            visited = another.visited;
            path = another.path;
        }

        public int getSize() {
            return size;
        }

        public int getUsed() {
            return used;
        }

        public void setUsed(int u) {
            used = u;
        }

        public int getAvail() {
            return size - used;
        }

        public int getG() {
            return gscore;
        }

        public void setG(int g) {
            gscore = g;
        }

        public int getH() {
            return hscore;
        }

        public void setH(int h) {
            hscore = h;
        }

        public int getF() {
            return gscore + hscore;
        }

        public int getDistance(Node another) {
            return Math.abs(x - another.x) + Math.abs(y - another.y);
        }

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean b) {
            visited = b;
        }

        public boolean isPath() {
            return path;
        }

        public void setPath(boolean b) {
            path = b;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + Objects.hashCode(getLocation());
            hash = 37 * hash + used;
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
            if (used != other.used) {
                return false;
            }
            if (!Objects.equals(getLocation(), other.getLocation())) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "node-x" + x + "-y" + y + ": " + getSize() + "T  " + getUsed() + "T  " + getAvail() + "T";
        }
    }
}
