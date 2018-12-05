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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 * @author hebik
 */
public class Day3 extends TaskSolver {

    private String input = null;
    private List<Claim> claims = new ArrayList<>();
    private int[][] fabric = new int[1000][1000];

    public Day3() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day3-2018.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day3.class.getName()).log(Level.SEVERE, null, ex);
        }
        Pattern p = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");
        Stream.of(input.split("\n")).forEach(ln -> {
            Matcher m = p.matcher(ln);
            if (m.matches()) {
                int id = Integer.parseInt(m.group(1));
                int x = Integer.parseInt(m.group(2));
                int y = Integer.parseInt(m.group(3));
                int w = Integer.parseInt(m.group(4));
                int h = Integer.parseInt(m.group(5));
                claims.add(new Claim(id, x, y, w, h));
            } else {
                System.err.println("Input error!");
            }
        });

    }

    private void part1() {
        AtomicInteger overlap = new AtomicInteger(0);
        claims.stream().forEach(cl -> overlap.addAndGet(cl.place(fabric)));

        System.out.println("[1] Square inches of fabric are within two or more claims: " + overlap.get());
    }

    private void part2() {
        Claim single = claims.stream().filter(cl -> cl.overlap(fabric)).findAny().get();
        System.out.println("[2] ID of the only claim that doesn't overlap: " + single.id);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    private class Claim {

        public int id = 0;
        public Point location = new Point();
        public int width = 0;
        public int height = 0;

        public Claim(int i, int x, int y, int w, int h) {
            id = i;
            location = new Point(x, y);
            width = w;
            height = h;
        }

        public int place(int[][] fabric) {
            int overlap = 0;
            for (int y = location.y; y < location.y + height; y++) {
                for (int x = location.x; x < location.x + width; x++) {
                    switch (fabric[y][x]) {
                        case 0:
                            fabric[y][x] = id;
                            break;
                        case Integer.MAX_VALUE:
                            break;
                        default:
                            fabric[y][x] = Integer.MAX_VALUE;
                            overlap++;
                            break;
                    }
                }
            }
            return overlap;
        }

        public boolean overlap(int[][] fabric) {
            boolean overlap = true;
            for (int y = location.y; y < location.y + height; y++) {
                for (int x = location.x; x < location.x + width; x++) {
                    if (fabric[y][x] != id) {
                        overlap = false;
                        break;
                    }
                }
            }
            return overlap;
        }
    }
}
