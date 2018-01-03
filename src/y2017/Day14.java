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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hebik
 */
public class Day14 extends TaskSolver {

    private String input = null;
    private Character[][] map = null;

    public Day14() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day14-2017.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day14.class.getName()).log(Level.SEVERE, null, ex);
        }
        map = new Character[128][128];
    }

    private void part1() {
        long used = 0;
        for (int i = 0; i < 128; i++) {
            String out = "";
            for (Character ch : Knot.knot(String.format("%s-%d", input, i)).toCharArray()) {
                out += String.format("%4s", Integer.toBinaryString((ch | 32) % 39 - 9)).replace(' ', '0');
            }
            for (int j = 0; j < 128; j++) {
                map[i][j] = out.charAt(j);
            }
            used += out.chars().filter(c -> c == '1').count();
        }
        System.out.println("[1] Used squares: " + used);
    }

    private void part2() {
        int regions = 0;
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                if (map[i][j] == '1') {
                    regions++;
                    walk(map, i, j);
                }
            }
        }
        System.out.println("[2] Number of regions: " + regions);
    }

    private void walk(Character[][] m, int i, int j) {
        // Visit this node
        m[i][j] = '0';
        // Check up direction
        if (i - 1 >= 0 && m[i - 1][j] == '1') {
            walk(m, i - 1, j);
        }
        // Check down direction
        if (i + 1 < map.length && m[i + 1][j] == '1') {
            walk(m, i + 1, j);
        }
        // Check left direction
        if (j - 1 >= 0 && m[i][j - 1] == '1') {
            walk(m, i, j - 1);
        }
        // Check right direction
        if (j + 1 < map[0].length && m[i][j + 1] == '1') {
            walk(m, i, j + 1);
        }
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

}
