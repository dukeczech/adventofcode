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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hebik
 */
public class Day5 extends TaskSolver {

    private String input = null;

    public Day5() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day5-2017.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void part1() {
        List<Integer> jumps = new ArrayList<>();

        Scanner sc = new Scanner(input);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            jumps.add(Integer.parseInt(line));
        }
        sc.close();

        int steps = 0;
        int current = 0;
        while (true) {
            int jump = jumps.get(current);
            jumps.set(current, jump + 1);
            current += jump;
            steps++;
            if (current < 0 || current >= jumps.size()) {
                break;
            }
        }
        System.out.println("Steps to reach the exit: " + steps);
    }

    private void part2() {
        List<Integer> jumps = new ArrayList<>();

        Scanner sc = new Scanner(input);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            jumps.add(Integer.parseInt(line));
        }
        sc.close();

        int steps = 0;
        int current = 0;
        while (true) {
            int jump = jumps.get(current);
            if (jump >= 3) {
                jumps.set(current, jump - 1);
            } else {
                jumps.set(current, jump + 1);
            }
            current += jump;
            steps++;
            if (current < 0 || current >= jumps.size()) {
                break;
            }
        }
        System.out.println("Steps to reach the exit: " + steps);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }
}
