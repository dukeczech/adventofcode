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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author hebik
 */
public class Day6 extends TaskSolver {

    private String input = null;
    private Map<Coordinate, Character> coordinates = null;
    private Distance[][] grid = new Distance[500][500];

    public Day6() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day6-2018.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day6.class.getName()).log(Level.SEVERE, null, ex);
        }
        AtomicInteger letter = new AtomicInteger((int) '@');
        coordinates = Stream.of(input.split("\n")).map(s -> {
            String[] n = s.split(", ");
            return new Coordinate(Integer.parseInt(n[0]), Integer.parseInt(n[1]));
        }).collect(Collectors.toMap(p -> p, p -> {
            letter.incrementAndGet();
            return (char) letter.get();
        }));

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == null) {
                    grid[y][x] = new Distance('.');
                }
                if (coordinates.containsKey(new Coordinate(x, y))) {
                    grid[y][x].name = coordinates.get(new Coordinate(x, y));
                    grid[y][x].distance = 0;
                } else {
                    grid[y][x].name = '_';
                }
            }
        }
    }

    private void part1() {

        coordinates.keySet().stream().forEach(p -> p.walk(grid, coordinates));
        int max = coordinates.keySet().stream().mapToInt(p -> p.getArea(grid, coordinates)).max().getAsInt();

        System.out.println("[1] Size of the largest area: " + max);
    }

    private void part2() {
        int regions = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                AtomicInteger xx = new AtomicInteger(x);
                AtomicInteger yy = new AtomicInteger(y);
                int size = coordinates.keySet().stream().mapToInt(p -> p.distance(new Coordinate(xx.intValue(), yy.intValue()))).sum();
                if (size < 10000) {
                    //System.out.println(size);
                    regions++;
                }
            }
        }
        System.out.println("[2] Size of the region containing all locations which have a total distance to all given coordinates of less than 10000: " + regions);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    class Coordinate extends Point {

        public Coordinate(Point p) {
            super(p);
        }

        public Coordinate(int x, int y) {
            super(x, y);
        }

        public int distance(Coordinate o) {
            return Math.abs(x - o.x) + Math.abs(y - o.y);
        }

        public int furthest(Map<Coordinate, Character> coordinates) {
            return coordinates.keySet().stream().filter(p -> !p.equals(this)).mapToInt(p -> p.distance(this)).max().getAsInt();
        }

        public void walk(Distance[][] grid, Map<Coordinate, Character> coordinates) {
            char name = coordinates.get(this);
            int furthest = furthest(coordinates);
            for (int yy = Math.max(0, y - furthest); yy < Math.min(grid.length, y + furthest); yy++) {
                for (int xx = Math.max(0, x - furthest); xx < Math.min(grid[0].length, x + furthest); xx++) {
                    if (xx == x && yy == y) {
                        grid[yy][xx].name = name;
                        grid[yy][xx].distance = 0;
                        continue;
                    }
                    int distance = distance(new Coordinate(xx, yy));
                    if (grid[yy][xx].distance == -1) {
                        grid[yy][xx].distance = distance;
                        grid[yy][xx].name = name;
                    } else if (distance != 0 && grid[yy][xx].distance == distance) {
                        grid[yy][xx].name = '.';
                    } else if (grid[yy][xx].distance > distance) {
                        grid[yy][xx].distance = distance;
                        grid[yy][xx].name = name;
                    }
                }
            }
        }

        public int getArea(Distance[][] grid, Map<Coordinate, Character> coordinates) {
            int area = 0;
            char name = coordinates.get(this);
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    if (grid[y][x].name == name) {
                        if (x == 0 || y == 0 || x == grid[0].length - 1 || y == grid.length - 1) {
                            // Consider this as infinite area
                            return 0;
                        }
                        area++;
                    }
                }
            }
            return area;
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }

    }

    class Distance {

        public char name = '-';
        public int distance = 0;

        public Distance(char n) {
            name = n;
            distance = -1;
        }

    }
}
