/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2018;

import adventofcode.TaskSolver;
import java.awt.Point;

/**
 *
 * @author hebik
 */
public class Day11 extends TaskSolver {

    private int input = 4455;
    private int[][] grid = null;
    private int[][] integral = null;

    public Day11() {
        int dimension = 300;
        grid = new int[dimension][dimension];
        integral = new int[dimension][dimension];

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[y][x] = getPowerLevel(x, y);
            }
        }

        for (int y = 0; y < grid.length; y++) {
            int sum = 0;
            for (int x = 0; x < grid[0].length; x++) {
                for (int z = 0; z <= y; z++) {
                    sum += grid[z][x];
                    integral[y][x] = sum;
                }
            }
        }
    }

    private int getPowerLevel(int x, int y) {
        String s = Integer.toString((((x + 10) * y) + input) * (x + 10));
        return s.charAt(s.length() - 3) - '0' - 5;
    }

    private void part1() {

        Point loc = new Point();
        int max = 0;
        for (int y = 1; y < integral.length - 2; y++) {
            for (int x = 1; x < integral[0].length - 2; x++) {
                int a1 = integral[y + 2][x + 2];
                int a2 = integral[y - 1][x - 1];
                int a3 = integral[y + 2][x - 1];
                int a4 = integral[y - 1][x + 2];
                int p = a1 + a2 - a3 - a4;
                if (p > max) {
                    loc.setLocation(x, y);
                    max = p;
                }
            }
        }

        System.out.println("[1] What is the X,Y coordinate of the top-left fuel cell of the 3x3 square with the largest total power: " + loc.x + "," + loc.y);
    }

    private void part2() {
        Point loc = new Point();
        int max = 0;
        int size = 0;
        for (int s = 3; s < 300; s++) {
            for (int y = 0; y < integral.length - s; y++) {
                for (int x = 0; x < integral[0].length - s; x++) {
                    int p = integral[y + s][x + s];
                    if (y - 1 > 0) {
                        if (x - 1 > 0) {
                            p += integral[y - 1][x - 1];
                            p -= integral[y + s][x - 1];
                        }
                        p -= integral[y - 1][x + s];
                    }
                    if (p > max) {
                        loc.setLocation(x, y);
                        max = p;
                        size = s + 1;
                    }
                }
            }
        }

        System.out.println("[2] What is the X,Y,size identifier of the square with the largest total power: " + loc.x + "," + loc.y + "," + size);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

}
