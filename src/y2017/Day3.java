/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2017;

import adventofcode.TaskSolver;

/**
 *
 * @author hebik
 */
public class Day3 extends TaskSolver {

    public Day3() {
    }

    private void part1() {
        int dest = 325489;

        int n = (int) Math.ceil(Math.sqrt(dest));
        if (n % 2 == 0) {
            n++;
        }

        int x0 = 0;
        int y0 = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // x stores the layer in which (i, j) the element lies

                // Finds minimum of four inputs
                int x = Math.min(Math.min(i, j), Math.min(n - 1 - i, n - 1 - j));

                // For upper right half
                if (i <= j) {

                    if ((n - 2 * x) * (n - 2 * x) - (i - x) - (j - x) == dest) {
                        x0 = j;
                        y0 = i;
                    }
                } // for lower left half
                else if ((n - 2 * x - 2) * (n - 2 * x - 2) + (i - x) + (j - x) == dest) {
                    x0 = j;
                    y0 = i;
                }
            }
        }
        System.out.println("[1] Result: " + (Math.abs(n / 2 - x0) + Math.abs(n / 2 - y0)));
    }

    int walkSpiral(int[][] grid, int dim, int limit) {
        int d = 0; // current direction; 0=RIGHT, 1=DOWN, 2=LEFT, 3=UP
        int s = 1; // chain size

        int x = ((int) Math.floor(dim / 2.0));
        int y = ((int) Math.floor(dim / 2.0));

        grid[x][y] = 1;

        for (int k = 1; k <= (dim - 1); k++) {
            for (int j = 0; j < (k < (dim - 1) ? 2 : 3); j++) {
                for (int i = 0; i < s; i++) {
                    grid[x][y] += adjSum(grid, x, y, dim);

                    if (grid[x][y] > limit) {
                        return grid[x][y];
                    }

                    switch (d) {
                        case 0:
                            y = y + 1;
                            break;
                        case 1:
                            x = x + 1;
                            break;
                        case 2:
                            y = y - 1;
                            break;
                        case 3:
                            x = x - 1;
                            break;
                    }
                }
                d = (d + 1) % 4;
            }
            s++;
        }
        return -1;
    }

    private int adjSum(int[][] grid, int x, int y, int dim) {
        int sum = 0;
        if (x - 1 >= 0) {
            sum += grid[x - 1][y];
            if (y - 1 >= 0) {
                sum += grid[x - 1][y - 1];
            }
            if (y + 1 < dim) {
                sum += grid[x - 1][y + 1];
            }
        }
        if (x + 1 < dim) {
            sum += grid[x + 1][y];
            if (y - 1 >= 0) {
                sum += grid[x + 1][y - 1];
            }
            if (y + 1 < dim) {
                sum += grid[x + 1][y + 1];
            }
        }
        if (y - 1 >= 0) {
            sum += grid[x][y - 1];
        }
        if (y + 1 < dim) {
            sum += grid[x][y + 1];
        }
        grid[x][y] = sum;

        return sum;
    }

    private void part2() {
        int dim = 50;
        int[][] grid = new int[dim][dim];
        System.out.println("[2] Result: " + walkSpiral(grid, dim, 325489));
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

}
