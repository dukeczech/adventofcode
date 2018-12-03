/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2017;

import adventofcode.TaskSolver;
import adventofcode.Point3D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hebik
 */
public class Day11 extends TaskSolver {

    private String[] directions = null;
    private String input = null;

    public Day11() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day11-2017.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day11.class.getName()).log(Level.SEVERE, null, ex);
        }
        directions = input.split(",");
    }

    private void part1() {
        int furthest = 0;
        Point3D position = new Point3D(0, 0, 0);
        for (String direction : directions) {
            switch (direction) {
                case "n":
                    position.add(0, 1, -1);
                    break;
                case "ne":
                    position.add(1, 0, -1);
                    break;
                case "se":
                    position.add(1, -1, 0);
                    break;
                case "s":
                    position.add(0, -1, 1);
                    break;
                case "sw":
                    position.add(-1, 0, 1);
                    break;
                case "nw":
                    position.add(-1, 1, 0);
                    break;
                default:
                    System.err.println("Unknown direction!");
                    break;
            }
            int dist = position.distance(new Point3D(0, 0, 0));
            if (dist > furthest) {
                furthest = dist;
            }
        }

        System.out.println("[1] Distance from start point: " + position.distance(new Point3D(0, 0, 0)));
        System.out.println("[2] Furthest distance he ever got: " + furthest);
    }

    private void part2() {

    }

    @Override
    protected void process() {
        part1();
        part2();
    }
}
