/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2017;

import adventofcode.TaskSolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hebik
 */
public class Day19 extends TaskSolver {

    enum DIRECTION {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private String input = null;
    private int X = 0;
    private int Y = 0;
    private byte[][] map = null;

    public Day19() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day19-2017.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day19.class.getName()).log(Level.SEVERE, null, ex);
        }
        X = input.split("\n")[0].length();
        Y = input.split("\n").length;
        map = new byte[Y][X];

        AtomicInteger i = new AtomicInteger(0);
        Arrays.stream(input.split("\n")).forEach(l -> System.arraycopy(l.getBytes(), 0, map[i.getAndIncrement()], 0, X));
    }

    private String walk(int x, int y, DIRECTION last, String letters) {
        int steps = 0;
        boolean quit = false;
        while (!quit) {
            switch ((char) map[y][x]) {
                case '|':
                case '-':
                    switch (last) {
                        case UP:
                            y--;
                            break;
                        case DOWN:
                            y++;
                            break;
                        case LEFT:
                            // Crossroads, direction will continue
                            x--;
                            break;
                        case RIGHT:
                            // Crossroads, direction will continue
                            x++;
                            break;
                    }
                    break;
                case '+':
                    switch (last) {
                        case UP:
                        case DOWN:
                            // Next direction will be L or R
                            if ((x - 1 >= 0) && (char) map[y][x - 1] != ' ') {
                                x--;
                                last = DIRECTION.LEFT;
                            } else {
                                x++;
                                last = DIRECTION.RIGHT;
                            }
                            break;
                        case LEFT:
                        case RIGHT:
                            // Next direction will be U or D
                            if ((y - 1 >= 0) && (char) map[y - 1][x] != ' ') {
                                y--;
                                last = DIRECTION.UP;
                            } else {
                                y++;
                                last = DIRECTION.DOWN;
                            }
                            break;
                    }
                    break;
                case ' ':
                    // This is the end
                    quit = true;
                    break;
                default:
                    // Append a letter, keep the direction
                    letters += (char) map[y][x];
                    switch (last) {
                        case UP:
                            y--;
                            break;
                        case DOWN:
                            y++;
                            break;
                        case LEFT:
                            x--;
                            break;
                        case RIGHT:
                            x++;
                            break;
                    }
                    break;
            }
            if (!quit) {
                steps++;
            }
        }
        System.out.println("[1] The letters: " + letters);
        System.out.println("[2] Number of steps: " + steps);
        return letters;
    }

    private void part1() {
        // Find starting position
        int x = 0;
        for (x = 0; x < X; x++) {
            if ((char) map[0][x] == '|') {
                break;
            }
        }

        DIRECTION last = DIRECTION.DOWN;
        String letters = walk(x, 0, last, "");
    }

    private void part2() {

    }

    @Override
    protected void process() {
        part1();
        part2();
    }

}
