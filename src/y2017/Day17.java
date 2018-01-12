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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hebik
 */
public class Day17 extends TaskSolver {

    private String input = null;
    private int steps = 0;

    public Day17() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day17-2017.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day17.class.getName()).log(Level.SEVERE, null, ex);
        }

        steps = Integer.parseInt(input);
    }

    private void part1() {
        int index = 0;
        List<Integer> buffer = new LinkedList<>();
        buffer.add(0);
        for (int i = 1; i < 2018; i++) {
            index = ((index + steps) % buffer.size()) + 1;
            buffer.add(index, i);
        }
        System.out.println("[1] Value after 2017 in circular buffer: " + buffer.get(index + 1));
    }

    private void part2() {
        int index = 0;
        int size = 1;
        int value = 0;
        for (int i = 1; i < 50000000; i++) {
            index = ((index + steps) % size) + 1;
            if (index == 1) {
                value = i;
            }
            size++;
        }
        System.out.println("[2] Value after 2017 in circular buffer: " + value);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }
}
