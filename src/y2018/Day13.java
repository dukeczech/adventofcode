/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2018;

import adventofcode.TaskSolver;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author hebik
 */
public class Day13 extends TaskSolver {

    private String input = null;
    private char[][] map = null;
    private List<Cart> carts = new ArrayList<>();

    public Day13() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day13-2018.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day13.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] lines = input.split("\n");
        map = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            map[i] = new char[lines[i].length()];
            System.arraycopy(lines[i].toCharArray(), 0, map[i], 0, lines[i].length());
            for (int j = 0; j < lines[i].length(); j++) {
                Character ch = lines[i].charAt(j);
                switch (ch) {
                    case '>':
                        carts.add(new Cart(new Point(j, i), Direction.RIGHT));
                        map[i][j] = '-';
                        break;
                    case 'v':
                        carts.add(new Cart(new Point(j, i), Direction.DOWN));
                        map[i][j] = '|';
                        break;
                    case '^':
                        carts.add(new Cart(new Point(j, i), Direction.UP));
                        map[i][j] = '|';
                        break;
                    case '<':
                        carts.add(new Cart(new Point(j, i), Direction.LEFT));
                        map[i][j] = '-';
                        break;
                }
            }
        }
    }

    private List<Cart> getCarts(int x, int y) {
        return carts.stream().filter(c -> (c.location.x == x && c.location.y == y)).collect(Collectors.toList());
    }

    private Point getCrash() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (getCarts(j, i).size() > 1) {
                    return new Point(j, i);
                }
            }
        }
        return null;
    }

    private void print() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                List<Cart> c = getCarts(j, i);
                if (c.isEmpty()) {
                    System.out.print(map[i][j]);
                } else if (c.size() == 1) {
                    System.out.print(c.get(0));
                } else {
                    System.out.print("X");
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private void part1() {
        boolean first = false;
        for (int i = 0; i < 100000; i++) {
            Collections.sort(carts);

            Iterator<Cart> it = carts.iterator();
            ArrayList<Cart> copy = new ArrayList<>(carts);
            for (Cart cart : carts) {
                cart.step(map);
                if (carts.contains(cart)) {
                    copy.removeIf(c -> c.equals(cart));
                }
            }
            carts = new ArrayList<>(copy);
            print();
        }

        /*Point crash = getCrash();
            while (crash != null) {
                if (!first) {
                    System.out.println("[1] Location of the first crash is: [" + crash.x + "," + crash.y + "]");
                    first = true;
                }
                // Remove crashed carts
                carts.removeAll(getCarts(crash.x, crash.y));

                System.out.println("Cart size: " + carts.size());
                if (carts.size() == 1) {
                    System.out.println("[2] Location of the last cart is: [" + carts.get(0).location.x + "," + carts.get(0).location.y + "]");
                    break;
                }

                crash = getCrash();
            }*/
    }

    private void part2() {
    }

    @Override
    protected void process() {
        part1();
        part2();

    }

    public enum Direction {
        DOWN, RIGHT, UP, LEFT;

        public Direction straight() {
            return this;
        }

        public Direction right() {
            switch (this) {
                case DOWN:
                    return LEFT;
                case RIGHT:
                    return DOWN;
                case UP:
                    return RIGHT;
                case LEFT:
                    return UP;
            }
            return null;
        }

        public Direction left() {
            return this.right().right().right();
        }
    }

    public enum Turn {
        LEFT, STRAIGHT, RIGHT;
        private static final Turn[] turns = values();

        public Turn next() {
            return turns[(this.ordinal() + 1) % turns.length];
        }
    }

    class Cart implements Comparable<Cart> {

        public Point location = new Point();
        public Direction direction = Direction.DOWN;
        public Turn last = Turn.RIGHT;

        public Cart(Point p, Direction d) {
            location = p;
            direction = d;
        }

        public boolean step(char[][] map) {
            switch (direction) {
                case DOWN:
                    location.y++;
                    switch (map[location.y][location.x]) {
                        case '/':
                            direction = Direction.LEFT;
                            break;
                        case '\\':
                            direction = Direction.RIGHT;
                            break;
                    }
                    break;
                case RIGHT:
                    location.x++;
                    switch (map[location.y][location.x]) {
                        case '/':
                            direction = Direction.UP;
                            break;
                        case '\\':
                            direction = Direction.DOWN;
                            break;
                    }
                    break;
                case UP:
                    location.y--;
                    switch (map[location.y][location.x]) {
                        case '/':
                            direction = Direction.RIGHT;
                            break;
                        case '\\':
                            direction = Direction.LEFT;
                            break;
                    }
                    break;
                case LEFT:
                    location.x--;
                    switch (map[location.y][location.x]) {
                        case '/':
                            direction = Direction.DOWN;
                            break;
                        case '\\':
                            direction = Direction.UP;
                            break;
                    }
                    break;
            }
            switch (map[location.y][location.x]) {
                case '+': {
                    switch (last) {
                        case LEFT:
                            last = last.next();
                            direction = direction.straight();
                            break;
                        case STRAIGHT:
                            last = last.next();
                            direction = direction.right();
                            break;
                        case RIGHT:
                            last = last.next();
                            direction = direction.left();
                            break;
                    }
                    break;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            String l = ""; //[" + location.x + "," + location.y + "] ";
            switch (direction) {
                case DOWN:
                    return l + "v";
                case RIGHT:
                    return l + ">";
                case UP:
                    return l + "^";
                case LEFT:
                    return l + "<";
            }
            return null;
        }

        @Override
        public int compareTo(Cart o) {
            if (this.location.y < o.location.y) {
                return -1;
            } else if (this.location.y > o.location.y) {
                return 1;
            } else {
                if (this.location.x < o.location.x) {
                    return -1;
                } else if (this.location.x > o.location.x) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }

        @Override
        public int hashCode() {
            return 29 * Objects.hashCode(this.location);
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
            final Cart other = (Cart) obj;
            if (!Objects.equals(this.location, other.location)) {
                return false;
            }
            return true;
        }

    }
}
