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
import java.util.ArrayList;
import java.util.Collections;
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

    private void part1() {
        boolean first = false;
        for (int i = 0; i < 100000; i++) {
            Collections.sort(carts);

            for (Cart c1 : carts) {
                if (!c1.dead) {
                    c1.step(map);
                    for (Cart c2 : carts) {
                        if (!c2.dead && c2 != c1 && c2.location.x == c1.location.x && c2.location.y == c1.location.y) {
                            c1.dead = c2.dead = true;
                            if (!first) {
                                System.out.println("[1] Location of the first crash is: [" + c1.location.x + "," + c1.location.y + "]");
                                first = true;
                            }
                        }
                    }
                }
            }

            List<Cart> remaining = carts.stream().filter(c -> !c.dead).collect(Collectors.toList());
            if (remaining.size() == 1) {
                System.out.println("[2] Location of the last cart is: [" + remaining.get(0).location.x + "," + remaining.get(0).location.y + "]");
                break;
            }
        }
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

        public boolean dead = false;
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
            switch (direction) {
                case DOWN:
                    return "v";
                case RIGHT:
                    return ">";
                case UP:
                    return "^";
                case LEFT:
                    return "<";
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
            return !(this.location.x != other.location.x || this.location.y != other.location.y);
        }

    }
}
