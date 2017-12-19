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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hebik
 */
public class Day9 extends TaskSolver {

    private String input = null;
    private int count = 0;
    private int score = 0;
    private int random = 0;

    public Day9() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day9-2017.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day9.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int consumeGarbage(int pos) {
        boolean ignore = false;
        for (int i = pos; i < input.length(); i++) {
            if (ignore) {
                ignore = false;
                continue;
            }
            switch (input.charAt(i)) {
                case '>':
                    return i;
                case '!':
                    ignore = true;
                    break;
                default:
                    random++;
                    break;
            }
        }
        return -1;
    }

    private int getGroups(int pos, int level) {
        for (int i = pos; i < input.length(); i++) {
            switch (input.charAt(i)) {
                case '{': {
                    count++;
                    score += level;
                    i = getGroups(i + 1, level + 1);
                    break;
                }
                case '}':
                    return i;
                case '<': {
                    i = consumeGarbage(i + 1);
                    break;
                }
            }
        }
        return 0;
    }

    private void part1() {
        getGroups(0, 1);
        System.out.println("[1] The total score: " + score);
    }

    private void part2() {
        System.out.println("[2] Non-canceled characters are within the garbage: " + random);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }
}
