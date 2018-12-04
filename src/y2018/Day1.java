/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2018;

import adventofcode.TaskSolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author hebik
 */
public class Day1 extends TaskSolver {

    private String input = null;

    public Day1() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day1-2018.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void part1() {
        AtomicInteger frequency = new AtomicInteger(0);
        Stream.of(input.split("\n")).forEach(v -> frequency.addAndGet(Integer.parseInt(v)));

        System.out.println("[1] Resulting frequency: " + frequency.intValue());
    }

    private void part2() {

        List<Integer> nums = Stream.of(input.split("\n")).map(Integer::valueOf).collect(Collectors.toList());

        Integer ff = new Integer(0);
        Set<Integer> freqs = new HashSet<>();
        freqs.add(ff);

        for (int i = 0; i < 100000; i++) {
            for (Integer num : nums) {
                ff += num;
                if (freqs.contains(ff)) {
                    System.out.println("[2] First frequency your device reaches twice: " + ff.intValue());
                    return;
                } else {
                    freqs.add(ff);
                }
            }
        }
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

}
