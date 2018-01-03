/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2017;

import adventofcode.Knot;
import adventofcode.TaskSolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 *
 * @author hebik
 */
public class Day10 extends TaskSolver {

    private String input = null;
    private int[] elements = null;

    public Day10() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day10-2017.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day10.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void part1() {
        elements = IntStream.range(0, 256).toArray();
        int[] lengths = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();

        int position = 0;
        int skip = 0;
        for (int length : lengths) {
            Knot.reverse(elements, position, length);
            position = (position + length + skip) % elements.length;
            skip++;
        }
        System.out.println("[1] Result of multiplying the first two numbers in the list: " + (elements[0] * elements[1]));
    }

    private void part2() {
        String out = Knot.knot(input);
        System.out.println("[2] Knot Hash of the puzzle input: " + out);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }
}
