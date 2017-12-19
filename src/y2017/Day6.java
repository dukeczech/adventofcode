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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hebik
 */
public class Day6 extends TaskSolver {

    private String input = null;

    public Day6() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day6-2017.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day6.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void part1() {
        int[] blocks = Arrays.stream(input.split("\\s+")).mapToInt(Integer::parseInt).toArray();

        Set<MemoryBank> banks = new HashSet<>();

        int cycles = 0;
        MemoryBank current = new MemoryBank(blocks);
        banks.add(current);
        while (true) {
            MemoryBank next = current.reallocation();
            cycles++;
            if (banks.contains(next)) {
                break;
            }
            banks.add(next);
            current = next;
        }

        System.out.println("[1] Redistribution cycles: " + cycles);
    }

    private void part2() {
        int[] blocks = Arrays.stream(input.split("\\s+")).mapToInt(Integer::parseInt).toArray();

        Set<MemoryBank> banks = new HashSet<>();

        boolean same = false;
        int cycles = 0;
        MemoryBank current = new MemoryBank(blocks);
        banks.add(current);
        while (true) {
            MemoryBank next = current.reallocation();
            if (same) {
                cycles++;
            }
            if (banks.contains(next)) {
                if (same) {
                    break;
                }
                same = true;
                banks.clear();
            }
            banks.add(next);
            current = next;
        }

        System.out.println("[2] Redistribution cycles: " + cycles);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    class MemoryBank {

        int[] banks = null;

        public MemoryBank(int[] b) {
            banks = b;
        }

        public MemoryBank reallocation() {
            int most = 0;
            int value = 0;
            for (int i = 0; i < banks.length; i++) {
                if (banks[i] > value) {
                    most = i;
                    value = banks[i];
                }
            }

            // Remove all the blocks
            banks[most] = 0;

            // Redistribute the value
            for (int i = ((most + 1) % banks.length); value > 0; i++, value--) {
                if (i == banks.length) {
                    i = 0;
                }
                banks[i]++;
            }

            return this;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + Arrays.hashCode(this.banks);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MemoryBank other = (MemoryBank) obj;
            return Arrays.equals(this.banks, other.banks);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int bank : banks) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(bank);
            }
            return sb.toString();
        }

    }

}
