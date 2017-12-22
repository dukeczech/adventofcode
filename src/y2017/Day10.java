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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
            reverse(position, length);
            position = (position + length + skip) % elements.length;
            skip++;
        }
        System.out.println("[1] Result of multiplying the first two numbers in the list: " + (elements[0] * elements[1]));
    }

    private void part2() {
        elements = IntStream.range(0, 256).toArray();
        byte[] lengths = new byte[input.getBytes().length + 5];
        lengths[lengths.length - 5] = 17;
        lengths[lengths.length - 4] = 31;
        lengths[lengths.length - 3] = 73;
        lengths[lengths.length - 2] = 47;
        lengths[lengths.length - 1] = 23;
        System.arraycopy(input.getBytes(), 0, lengths, 0, input.getBytes().length);

        int position = 0;
        int skip = 0;
        for (int i = 0; i < 64; i++) {
            for (int length : lengths) {
                reverse(position, length);
                position = (position + length + skip) % elements.length;
                skip++;
            }
        }

        StringBuilder sb = new StringBuilder();
        List<Integer> elem = Arrays.stream(elements).boxed().collect(Collectors.toList());
        IntStream.range(0, (elem.size() + 16 - 1) / 16).mapToObj(i -> elem.subList(i * 16, Math.min(16 * (i + 1), elem.size()))).forEach(i -> sb.append(String.format("%02x", xor(i))));
        System.out.println("[2] Knot Hash of the puzzle input: " + sb.toString());
    }

    private Integer xor(List<Integer> l) {
        return l.stream().mapToInt(i -> i).reduce(0, (a, b) -> a ^ b);
    }

    private void reverse(int start, int length) {
        for (int i = 0; i < length / 2; i++) {
            int temp = elements[(start + i) % elements.length];
            elements[(start + i) % elements.length] = elements[(start + length - i - 1) % elements.length];
            elements[(start + length - i - 1) % elements.length] = temp;
        }
    }

    @Override
    protected void process() {
        part1();
        part2();
    }
}
