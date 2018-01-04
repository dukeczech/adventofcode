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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hebik
 */
public class Day15 extends TaskSolver {

    private String input = null;
    private long[] values = null;

    public Day15() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day15-2017.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day15.class.getName()).log(Level.SEVERE, null, ex);
        }

        values = new long[2];

        try (Scanner sc = new Scanner(input)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Pattern p = Pattern.compile("Generator ([A-Z]) starts with (\\d+)");
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    String name = m.group(1);
                    int value = Integer.parseInt(m.group(2));
                    if (name.equals("A")) {
                        values[0] = value;
                    } else {
                        values[1] = value;
                    }
                }
            }
        }
    }

    private void part1() {
        int matches = 0;
        long[] generator = new long[]{values[0], values[1]};
        for (long i = 0; i < 40000000; i++) {
            generator[0] = (generator[0] * 16807) % 2147483647;
            generator[1] = (generator[1] * 48271) % 2147483647;
            if ((generator[0] & 0xFFFF) == (generator[1] & 0xFFFF)) {
                matches++;
            }
        }
        System.out.println("[1] Judge's final count: " + matches);
    }

    private void part2() {
        int matches = 0;
        int pairs = 0;
        long[] generator = new long[]{values[0], values[1]};
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            generator[0] = (generator[0] * 16807) % 2147483647;
            if (generator[0] % 4 != 0) {
                continue;
            }

            for (long j = 0; j < Long.MAX_VALUE; j++) {
                generator[1] = (generator[1] * 48271) % 2147483647;
                if (generator[1] % 8 == 0) {
                    break;
                }
            }
            pairs++;
            if ((generator[0] & 0xFFFF) == (generator[1] & 0xFFFF)) {
                matches++;
            }
            if (pairs >= 5000000) {
                break;
            }
        }
        System.out.println("[2] Judge's final count: " + matches);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

}
