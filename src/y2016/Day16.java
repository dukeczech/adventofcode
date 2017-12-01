/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.TaskSolver;

/**
 *
 * @author karel.hebik
 */
public class Day16 extends TaskSolver {

    private String puzzle = null;
    private int length = 0;

    public Day16() {
        puzzle = "11011110011011101";
        length = 0;
    }

    @Override
    public void process() {
        part1();
        part2();
    }

    private void part1() {
        length = 272;
        StringBuilder data = new StringBuilder(length);
        data.append(puzzle);
        while (data.length() < length) {
            data = dragon(data);
        }

        data.setLength(length);

        StringBuilder checksum = checksum(data);

        while (checksum.length() % 2 == 0) {
            checksum = checksum(checksum);
        }

        System.out.println("Correct first checksum: " + checksum);
    }

    private void part2() {
        length = 35651584;
        StringBuilder data = new StringBuilder(length);
        data.append(puzzle);
        while (data.length() < length) {
            data = dragon(data);
        }

        data.setLength(length);

        StringBuilder checksum = checksum(data);

        while (checksum.length() % 2 == 0) {
            checksum = checksum(checksum);
        }

        System.out.println("Correct second checksum: " + checksum);
    }

    private StringBuilder dragon(StringBuilder input) {
        StringBuilder output = new StringBuilder();
        output.append(input).append("0");
        for (int i = input.length() - 1; i >= 0; i--) {
            output.append((input.charAt(i) == '0') ? '1' : '0');
        }
        return output;
    }

    private StringBuilder checksum(StringBuilder input) {
        if (input.length() % 2 != 0) {
            System.err.println("Wrong input length!");
            return null;
        }
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i += 2) {
            if (input.charAt(i) == input.charAt(i + 1)) {
                output.append('1');
            } else {
                output.append('0');
            }
        }
        return output;
    }
}
